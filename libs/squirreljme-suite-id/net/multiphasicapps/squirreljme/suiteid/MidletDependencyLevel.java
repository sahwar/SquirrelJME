// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

/**
 * This represents the level of the dependency.
 *
 * @since 2017/02/22
 */
public enum MidletDependencyLevel
{
	/** Required. */
	REQUIRED,
	
	/** Optional. */
	OPTIONAL,
	
	/** End. */
	;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public String toString()
	{
		// Convert string
		switch (this)
		{
			case REQUIRED:	return "required";
			case OPTIONAL:	return "optional";
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the dependency level based on the input string.
	 *
	 * @param __s The input string to parse.
	 * @return The dependency level for the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public static MidletDependencyLevel of(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__s.trim())
		{
			case "required":	return REQUIRED;
			case "optional":	return OPTIONAL;
			
				// Should not happen
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

