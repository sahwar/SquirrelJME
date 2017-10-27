// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to parse the stack map and initialize the initial
 * snapshot states for jump targets within the method.
 *
 * @since 2017/04/16
 */
final class __StackMapParser__
{
	/** The stream to decode from. */
	protected final DataInputStream in;
	
	/** The number of stack entries. */
	protected final int maxstack;
	
	/** The number of local entries. */
	protected final int maxlocals;
	
	/** The method byte code. */
	protected final ByteCode code;
	
	/** Constant pool. */
	protected final Pool pool;
	
	/** Verification targets. */
	private final Map<Integer, StackMapTableState> _targets;
	
	/** The next stack state. */
	private final StackMapTableEntry[] _nextstack;
	
	/** The next local variable state. */
	private final StackMapTableEntry[] _nextlocals;
	
	/** The placement address. */
	private volatile int _placeaddr;
	
	/** The top of the stack. */
	private volatile int _stacktop;
	
	/**
	 * Initializes the stack map parser.
	 *
	 * @param __p The constant pool.
	 * @param __m The method this code exists within.
	 * @param __new Should the new stack map table format be used?
	 * @param __in The data for the stack map table.
	 * @param __bc The owning byte code.
	 * @throws InvalidClassFormatException If the stack map table is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	__StackMapParser__(Pool __p, Method __m, boolean __new, byte[] __in,
		ByteCode __bc)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__p == null || __m == null || __in == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Set
		DataInputStream xin;
		this.in = (xin = new DataInputStream(
			new ByteArrayInputStream(__in)));
		int maxstack = __bc.maxStack(),
			maxlocals = __bc.maxLocals();
		this.maxstack = maxstack;
		this.maxlocals = maxlocals;
		this.code = __bc;
		this.pool = __p;
		
		// This is used to set which variables appear next before a state is
		// constructed with them
		StackMapTableEntry[] nextstack, nextlocals;
		this._nextstack = (nextstack = new StackMapTableEntry[maxstack]);
		this._nextlocals = (nextlocals = new StackMapTableEntry[maxlocals]);
		
		// Setup initial state
		// {@squirreljme.error JI26 The arguments that are required for the
		// given method exceeds the maximum number of permitted local
		// variables. (The method in question; The required number of local
		// variables; The maximum number of local variables)}
		MethodHandle handle = __m.handle();
		boolean isinstance = !__m.flags().isStatic();
		JavaType[] jis = handle.javaStack(isinstance);
		int jn = jis.length;
		if (jn > maxlocals)
			throw new InvalidClassFormatException(
				String.format("JI26 %s %d %d", handle, jn, maxlocals));
		
		// Setup entries
		// If this is an instance initializer method then only the first
		// argument is not initialized
		boolean isiinit = isinstance && __m.name().isInstanceInitializer();
		for (int i = 0; i < jn; i++)
			nextlocals[i] = new StackMapTableEntry(jis[i],
				(isiinit ? (i != 0) : true));
		
		// Where states go
		Map<Integer, StackMapTableState> targets = new LinkedHashMap<>();
		this._targets = targets;
		
		// Record state
		__next(0, true);
		
		// Parse the stack map table
		try (DataInputStream in = xin)
		{
			// Parsing the class stack map table
			if (!__new)
			{
				// Read the number of entries in the table
				int ne = xin.readUnsignedShort();
			
				// All entries in the table are full frames
				for (int i = 0; i < ne; i++)
					__next(__oldStyle(), true);
			}
		
			// The modern stack map table
			else
			{
				// Read the number of entries in the table
				int ne = xin.readUnsignedShort();
			
				// Read them all
				for (int i = 0; i < ne; i++)
				{
					// Read the frame type
					int type = xin.readUnsignedByte();
					int addr;
				
					// Full frame?
					if (type == 255)
						addr = __fullFrame();
				
					// Same frame?
					else if (type >= 0 && type <= 63)
						addr = __sameFrame(type);
				
					// Same locals but a single stack item
					else if (type >= 64 && type <= 127)
						addr = __sameLocalsSingleStack(type - 64);
				
					// Same locals, single stack item, explicit delta
					else if (type == 247)
						addr = __sameLocalsSingleStackExplicit();
				
					// Chopped frame
					else if (type >= 248 && type <= 250)
						addr = __choppedFrame(251 - type);
				
					// Same frame but with a supplied delta
					else if (type == 251)
						addr = __sameFrameDelta();
				
					// Appended frame
					else if (type >= 252 && type <= 254)
						addr = __appendFrame(type - 251);
				
					// {@squirreljme.error JI27 Unknown StackMapTable
					// verification type. (The verification type)}
					else
						throw new InvalidClassFormatException(
							String.format("JI27 %d", type));
					
					// Setup next
					__next(addr, false);
				}
			}
		}
		
		// {@squirreljme.error JI28 Failed to parse the stack map table.}
		catch (IOException e)
		{
			throw new InvalidClassFormatException("JI28", e);
		}
	}
	
	/**
	 * Returns the stack map table.
	 *
	 * @return The parse stack map table.
	 * @since 2017/10/16
	 */
	public StackMapTable get()
	{
		return new StackMapTable(this._targets);
	}
	
	/**
	 * Append extra locals to the frame and clear the stack.
	 *
	 * @param __addlocs The number of local variables to add.
	 * @return The address offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __appendFrame(int __addlocs)
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// Stack is cleared
		this._stacktop = 0;
		
		// Read in local variables
		StackMapTableEntry[] nextlocals = this._nextlocals;
		int n = this.maxlocals;
		for (int i = 0; __addlocs > 0 && i < n; i++)
		{
			// Get slot here
			StackMapTableEntry s = nextlocals[i];
			
			// If it is not empty, ignore it
			if (!s.equals(JavaType.NOTHING))
				continue;
			
			// Set it
			StackMapTableEntry aa;
			nextlocals[i] = (aa = __loadInfo());
			__addlocs--;
			
			// If a wide element was added, then the next one becomes TOP
			if (aa.isWide())
				nextlocals[++i] = StackMapTableEntry.TOP_UNDEFINED;
		}
		
		// Error if added stuff remains
		// {@squirreljme.error JI29 Appending local variables to the frame
		// however there is no room to place them. (The remaining local count)}
		if (__addlocs != 0)
			throw new InvalidClassFormatException(
				String.format("JI29 %d", __addlocs));
		
		return rv;
	}
	
	/**
	 * Similar frame with no stack and the top few locals removed.
	 *
	 * @param __chops The number of variables which get chopped.
	 * @return The address offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __choppedFrame(int __chops)
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// No stack
		this._stacktop = 0;
		
		// Chop off some locals
		StackMapTableEntry[] nextlocals = this._nextlocals;
		int i, n = this.maxlocals;
		for (i = n - 1; __chops > 0 && i >= 0; i--)
		{
			// Get slot here
			StackMapTableEntry s = nextlocals[i];
			
			// If it is empty, ignore it
			if (s.equals(StackMapTableEntry.NOTHING))
				continue;
			
			// Clear it
			nextlocals[i] = StackMapTableEntry.NOTHING;
			__chops--;
		}
		
		// Still chops left?
		// {@squirreljme.error JI2a Could not chop off all local variables
		// because there are no variables remaining to be chopped. (The
		// remaining variables to remove)}
		if (__chops != 0)
			throw new InvalidClassFormatException(
				String.format("JI2a %d", __chops));
		
		return rv;
	}
	
	/**
	 * This reads and parses the full stack frame.
	 *
	 * @return The address offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __fullFrame()
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// Read in local variables
		int nl = in.readUnsignedShort();
		
		// {@squirreljme.error JI2b The number of specified local variables in
		// the full frame exceeds the maximum permitted local variable
		// count. (The read local variable count; The number of locals the
		// method uses)}
		int maxlocals = this.maxlocals,
			maxstack = this.maxstack;
		if (nl > maxlocals)
			throw new InvalidClassFormatException(
				String.format("JI2b %d %d", nl, maxlocals));
		int i;
		StackMapTableEntry[] nextlocals = this._nextlocals;
		for (i = 0; i < nl; i++)
			nextlocals[i] = __loadInfo();
		for (;i < maxlocals; i++)
			nextlocals[i] = StackMapTableEntry.NOTHING;
		
		// Read in stack variables
		StackMapTableEntry[] nextstack = this._nextstack;
		int ns = in.readUnsignedShort();
		for (i = 0; i < ns; i++)
			nextstack[i] = __loadInfo();
		this._stacktop = ns;
		
		return rv;
	}
	
	/**
	 * Loads type information for the stack.
	 *
	 * @return The type which was parsed.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private StackMapTableEntry __loadInfo()
		throws IOException
	{
		// Read the tag
		DataInputStream in = this.in;
		int tag = in.readUnsignedByte();
		
		// Depends on the tag
		switch (tag)
		{
				// Top
			case 0:
				return StackMapTableEntry.TOP_UNDEFINED;
				
				// Integer
			case 1:
				return StackMapTableEntry.INTEGER;
				
				// Float
			case 2:
				return StackMapTableEntry.FLOAT;
				
				// Double
			case 3:
				return StackMapTableEntry.DOUBLE;
				
				// Long
			case 4:
				return StackMapTableEntry.LONG;
				
				// Nothing
			case 5:
				return StackMapTableEntry.NOTHING;
				
				// Uninitialized this
			case 6:
				throw new todo.TODO();
				
				// Initialized object
			case 7:
				return new StackMapTableEntry(new JavaType(
					this.pool.<ClassName>get(ClassName.class,
					in.readUnsignedShort()).field()), true);
				
				// Uninitialized variable for a new instruction, the pc points
				// to the new instruction so the class must be read from
				// that instruction to determine the type of that actual
				// object
			case 8:
				int pc = in.readUnsignedShort();
				throw new todo.TODO();
				
				// Unknown
			default:
				// {@squirreljme.error JI2c The verification tag in the
				// StackMap/StackMapTable attribute is not valid. (The tag)}
				throw new InvalidClassFormatException(
					String.format("JI2c %d", tag));
		}
	}
	
	/**
	 * Initializes the next state.
	 *
	 * @param __au The address offset.
	 * @param __abs Absolute position?
	 * @return The state for the next address.
	 * @since 2016/05/20
	 */
	StackMapTableState __next(int __au, boolean __abs)
	{
		// Generate it
		StackMapTableState rv = new StackMapTableState(this._nextlocals,
			this._nextstack, this._stacktop);
		
		// Set new placement address
		int naddr = this._placeaddr;
		int pp = (__abs ? __au :
			naddr + (__au + (naddr == 0 ? 0 : 1)));
		this._placeaddr = pp;
	
		// {@squirreljme.error JI2d A duplicate stack map information for the
		// specified address has already been loaded. (The address; The
		// already existing information; The information to be placed there)}
		Map<Integer, StackMapTableState> targets = this._targets;
		if (targets.containsKey(pp))
			throw new IllegalStateException(String.format("JI2d %d %s %s",
				pp, targets.get(pp), rv));
		targets.put(pp, rv);
		
		// Debug
		System.err.printf("DEBUG -- Read state @%d: %s%n", pp, rv);
	
		// The stored state
		return rv;
	}
	
	/**
	 * Reads in an old style full frame.
	 *
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __oldStyle()
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// Read in local variables
		int nl = in.readUnsignedShort();
		
		// {@squirreljme.error JI2e Old-style full frame specified more local
		// variables than there are in the method. (The number of locals; The
		// maximum number of locals)}
		int maxlocals = this.maxlocals;
		if (nl > maxlocals)
			throw new InvalidClassFormatException(
				String.format("JI2e %d %d", nl, maxlocals));
		StackMapTableEntry[] nextlocals = this._nextlocals;
		int i = 0;
		for (i = 0; i < nl; i++)
			nextlocals[i] = __loadInfo();
		for (;i < maxlocals; i++)
			nextlocals[i] = null;
		
		// Read in stack variables
		StackMapTableEntry[] nextstack = this._nextstack;
		int ns = in.readUnsignedShort();
		for (i = 0; i < ns; i++)
			nextstack[i] = __loadInfo();
		this._stacktop = ns;
		
		return rv;
	}
	
	/**
	 * The same frame is used with no changes.
	 *
	 * @param __delta The offset from the earlier offset.
	 * @return The address information.
	 * @since 2016/03/26
	 */
	private int __sameFrame(int __delta)
	{
		return __delta;
	}
	
	/**
	 * Same frame but with a supplied delta rather than using it with the type.
	 *
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __sameFrameDelta()
		throws IOException
	{
		return this.in.readUnsignedShort();
	}
	
	/**
	 * Same locals but the stack has only a single entry.
	 *
	 * @param __delta The delta offset.
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __sameLocalsSingleStack(int __delta)
		throws IOException
	{
		// Set the single stack
		this._stacktop = 1;
		this._nextstack[0] = __loadInfo();
		
		return __delta;
	}
	
	/**
	 * Same locals but the stack has only a single entry, the delta offset
	 * is specified.
	 *
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __sameLocalsSingleStackExplicit()
		throws IOException
	{
		return __sameLocalsSingleStack(this.in.readUnsignedShort());
	}
}

