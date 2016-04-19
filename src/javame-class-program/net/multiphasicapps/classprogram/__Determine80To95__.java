// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * Determines the stack operations for opcodes 80 to 95.
 *
 * @since 2016/04/18
 */
class __Determine80To95__
	extends __VMWorkers__.__Determiner__
{
	/**
	 * {@inheritDoc}
	 * @since 2016/04/18
	 */
	@Override
	public void determine(__DetermineTypes__ __dt, CPOp __op)
	{
		// Depends on the opcode
		int opcode = __op.instructionCode();
		switch (opcode)
		{
				// Store int/byte/char/short into array
			case 79:
			case 84:
			case 85:
			case 86:
				__arraystore(__dt, __op, CPVariableType.INTEGER);
				break;
				
				// Store long into array
			case 80:
				__arraystore(__dt, __op, CPVariableType.LONG);
				break;
				
				// Store float into array
			case 81:
				__arraystore(__dt, __op, CPVariableType.FLOAT);
				break;
				
				// Store double into array
			case 82:
				__arraystore(__dt, __op, CPVariableType.DOUBLE);
				break;
				
				// Store object into array
			case 83:
				__arraystore(__dt, __op, CPVariableType.OBJECT);
				break;
			
				// Dup
			case 89:
				__dup(__dt, __op);
				break;
				
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Store value into array.
	 *
	 * @param __dt The determiner.
	 * @param __op The current operation.
	 * @param __t The type of value to store.
	 * @since 2016/04/19
	 */
	static void __arraystore(__DetermineTypes__ __dt, CPOp __op,
		CPVariableType __t)
	{
		__dt.operate(__op, null, __t, CPVariableType.INTEGER,
			CPVariableType.OBJECT, null);
	}
	
	/**
	 * Duplicates the top-most stack item.
	 *
	 * @param __dt The determiner.
	 * @param __op The current operation.
	 * @since 2016/04/12
	 */
	static void __dup(__DetermineTypes__ __dt, CPOp __op)
	{
		// Handle
		CPVariables xin = __op.variables();
		
		// Get the topmost variable
		int top;
		CPVariables.Slot at = xin.get((top = xin.getStackTop()) - 1);
		
		// {@squirreljme.error CP1g Cannot duplicate long or double types on
		// the stack. (The operation address; The topmost stack item)}
		CPVariableType ttt = at.type();
		if (ttt.isWide() || ttt == CPVariableType.TOP)
			throw new CPProgramException(String.format("CP1g %d %s",
				__op.address(), ttt));
		
		// Duplicate it
		__dt.set(__op, top, top + 1, ttt);
	}
}

