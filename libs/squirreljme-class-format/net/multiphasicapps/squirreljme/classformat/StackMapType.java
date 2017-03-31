// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;

/**
 * This represents the type of value which is stored in a variable either on
 * the stack or in a local.
 *
 * @since 2016/05/12
 */
public enum StackMapType
{
	/** Nothing is stored here. */
	NOTHING(false),
	
	/** 32-bit Integer. */
	INTEGER(false),
	
	/** 64-bit Integer. */
	LONG(true),
	
	/** 32-bit Float. */
	FLOAT(false),
	
	/** 64-bit Double. */
	DOUBLE(true),
	
	/** Object. */
	OBJECT(false),
	
	/** The top of a long or double. */
	TOP(false),
	
	/** End. */
	;
	
	/** Is this type wide? */
	protected final boolean iswide;
	
	/**
	 * Initializes the variable type.
	 *
	 * @param __w If {@code true} then the type consumes two spaces.
	 * @since 2016/05/12
	 */
	private StackMapType(boolean __w)
	{
		this.iswide = __w;
	}
	
	/**
	 * This returns {@code true} if the entry has a value. The tops of long
	 * or double values do not have values.
	 *
	 * @return {@code true} if not {@link #TOP} and not {@link #NOTHING}.
	 * @since 2017/02/08
	 */
	public final boolean hasValue()
	{
		return this != TOP && this != NOTHING;
	}
	
	/**
	 * Is this a valid type for storing of a value?
	 *
	 * @return {@code true} if this is not nothing or the top type.
	 * @since 2017/03/31
	 */
	public final boolean isValid()
	{
		return this != NOTHING && this != TOP;
	}
	
	/**
	 * Returns {@code true} if this is a wide type.
	 *
	 * @return {@code true} if a wide type.
	 * @since 2016/05/12
	 */
	public final boolean isWide()
	{
		return this.iswide;
	}
	
	/**
	 * Obtains the type of variable to use by its symbol.
	 *
	 * @param __sym The symbol to use for the variable.
	 * @return The variable which is associated with the given symbol.
	 * @throws ClassFormatException If the type is not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/23
	 */
	public static StackMapType bySymbol(FieldSymbol __sym)
		throws ClassFormatException, NullPointerException
	{
		// Check
		if (__sym == null)
			throw new NullPointerException("NARG");
		
		// If an array then it is always an object
		if (__sym.isArray())
			return OBJECT;
		
		// Depends on the first character otherwise
		switch (__sym.charAt(0))
		{
			case 'L': return OBJECT;
			case 'D': return DOUBLE;
			case 'F': return FLOAT;
			case 'J': return LONG;
				
				// All of these map to integer (promotion)
			case 'B':
			case 'C':
			case 'I':
			case 'S':
			case 'Z':
				return INTEGER;
				
				// Unknown
			default:
				// {@squirreljme.error AY3r The specified field symbol
				// cannot be mapped to a variable type. (The field symbol)}
				throw new ClassFormatException(String.format("AY3r %s", __sym));
		}
	}
}


