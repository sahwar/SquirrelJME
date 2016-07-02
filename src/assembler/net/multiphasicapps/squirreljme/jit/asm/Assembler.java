// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.asm;

/**
 * This class represents the base of the assembler which is used to ultimately
 * generate native machine code.
 *
 * @since 2016/07/02
 */
public class Assembler
{
	/**
	 * Initializes the assembler instance.
	 *
	 * @param __ac The configuration used for basic assembly operations.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/02
	 */
	public Assembler(AssemblerConfig __ac)
		throws NullPointerException
	{
		// Check
		if (__ac == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

