// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.MethodFlags;

/**
 * This class contains the memory information for every single suite which
 * exists within the VM.
 *
 * @since 2019/04/21
 */
public final class SuitesMemory
	extends AbstractReadableMemory
	implements ReadableMemory
{
	/** Configuration and table space size. */
	public static final int CONFIG_TABLE_SIZE =
		1048576;
	
	/** The suite chunk size. */
	public static final int SUITE_CHUNK_SIZE =
		4194304;
	
	/** The suite manage to base from. */
	protected final VMSuiteManager suites;
	
	/** Offset. */
	protected final int offset;
	
	/** The size of this memory region. */
	protected final int size;
	
	/** The suite configuration table (addresses of suites). */
	protected final RawMemory configtable;
	
	/** The individual regions of suite memory. */
	private final SuiteMemory[] _suitemem;
	
	/** This is the mapping of suite names to memory. */
	private final Map<String, SuiteMemory> _suitemap;
	
	/** Was the config table initialized? */
	private volatile boolean _didconfiginit;
	
	/** The address of the code containing the boostrap. */
	volatile int _bootaddr;
	
	/**
	 * Initializes the suites memory.
	 *
	 * @param __off The offset of suite memory.
	 * @param __sm The suite manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public SuitesMemory(int __off, VMSuiteManager __sm)
		throws NullPointerException
	{
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		// Set suites
		this.suites = __sm;
		
		// All the libraries which are available for usage
		String[] libnames = __sm.listLibraryNames();
		
		// Setup configuration space
		this.configtable = new RawMemory(__off, CONFIG_TABLE_SIZE);
		
		// Setup suite memory area
		int n = libnames.length;
		SuiteMemory[] suitemem = new SuiteMemory[n];
		Map<String, SuiteMemory> suitemap = new LinkedHashMap<>();
		
		// Setup memory regions for the various suites
		int off = CONFIG_TABLE_SIZE;
		for (int i = 0; i < n; i++, off += SUITE_CHUNK_SIZE)
		{
			// Set
			String ln = libnames[i];
			SuiteMemory sm;
			suitemem[i] = (sm = new SuiteMemory(off, __sm, ln));
			
			// Also use map for quick access
			suitemap.put(ln, sm);
			
			// Debug
			todo.DEBUG.note("MMap Suite %s -> %08x", ln, __off + off);
		}
		
		// Store all the various suite memories
		this._suitemem = suitemem;
		this._suitemap = suitemap;
		
		// Store final memory parameters
		this.offset = __off;
		this.size = off;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memReadBytes(int __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Reading completely from a single suite?
		if (__addr >= CONFIG_TABLE_SIZE)
		{
			SuiteMemory[] suitemem = this._suitemem;
			int si = (__addr - CONFIG_TABLE_SIZE) / SUITE_CHUNK_SIZE,
				sie = ((__addr + __l) - CONFIG_TABLE_SIZE) / SUITE_CHUNK_SIZE;
			if (si == sie && si >= 0 && si < suitemem.length)
			{
				suitemem[si].memReadBytes(__addr - suitemem[si].offset,
					__b, __o, __l);
				return;
			}
		}
		
		// Between suites or similar
		super.memReadBytes(__addr, __b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadInt(int __addr)
	{
		// Reading from the config table?
		if (__addr < CONFIG_TABLE_SIZE)
		{
			// Needs to be initialized?
			if (!this._didconfiginit)
				this.__init();
			
			// Read from memory
			return this.configtable.memReadInt(__addr);
		}
		
		// Determine the suite index we are wanting to look in memory
		int si = (__addr - CONFIG_TABLE_SIZE) / SUITE_CHUNK_SIZE;
		
		// Instead of failing, return some invalid values
		SuiteMemory[] suitemem = this._suitemem;
		if (si < 0 || si >= suitemem.length)
			return 0xFFFFFFFF;
		
		// Read from suite memory
		return suitemem[si].memReadInt(__addr - suitemem[si].offset);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memRegionOffset()
	{
		return this.offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memRegionSize()
	{
		return 0x7FFFFFFF;
	}
	
	/**
	 * Initializes the configuration space.
	 *
	 * @since 2019/04/21
	 */
	final void __init()
	{
		// Do not initialize twice!
		if (this._didconfiginit)
			return;
		this._didconfiginit = true;
		
		// The bootstrap is in CLDC compact
		SuiteMemory cldc = this._suitemap.get("cldc-compact.jar");
		try
		{
			cldc.__init();
		}
		
		// {@squirreljme.error AE0t Could not initialize CLDC library.}
		catch (IOException e)
		{
			throw new RuntimeException("AE0t", e);
		}
		
		// {@squirreljme.error AE0w The bootstrap class address is not known.}
		int bootmcaddr = cldc._bootmcaddr;
		if (bootmcaddr == 0)
			throw new RuntimeException("AE0w");
		
		// Need to get the position of the bootstrap class from how this chunk
		// sees its own memory
		bootmcaddr += cldc.offset;
		
		// Need to determine the actual pointer to the bootstrap code for
		// the initial PC set
		int bootsdx = this.memReadByte(bootmcaddr + 6),
			smtboff = this.memReadInt(bootmcaddr + 64),
			bootsmo = bootmcaddr + smtboff + (bootsdx * 18),
			mmflags = this.memReadInt(bootsmo + 0),
			mmcdoff = this.memReadShort(bootsmo + 10),
			bootcdo = bootsmo + mmcdoff;
		
		// Store the boot address
		this._bootaddr = this.offset + bootcdo;
		
		// Debug
		todo.DEBUG.note("Bootstrap class at %08x (code=%08x)",
			this.offset + bootmcaddr,
			this.offset + bootcdo);
	}
}
