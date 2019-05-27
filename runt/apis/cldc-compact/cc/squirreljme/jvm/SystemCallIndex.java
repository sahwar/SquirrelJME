// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This contains the index of system calls.
 *
 * @since 2019/05/23
 */
public final class SystemCallIndex
{
	/** Checks if the system call is supported. */
	public static final short QUERY_INDEX =
		0;
	
	/** Gets the last error state. */
	public static final short ERROR_GET =
		1;
	
	/** Sets the last error state. */
	public static final short ERROR_SET =
		2;
	
	/** Current wall clock milliseconds (low). */
	public static final short TIME_LO_MILLI_WALL =
		3;
	
	/** Current wall clock milliseconds (high). */
	public static final short TIME_HI_MILLI_WALL =
		4;
	
	/** Current monotonic clock nanoseconds (low). */
	public static final short TIME_LO_NANO_MONO =
		5;
	
	/** Current monotonic clock nanoseconds (high). */
	public static final short TIME_HI_NANO_MONO =
		6;
	
	/** VM Information: Free memory in bytes. */
	public static final short VMI_MEM_FREE =
		7;
	
	/** VM Information: Used memory in bytes. */
	public static final short VMI_MEM_USED =
		8;
	
	/** VM Information: Max memory in bytes. */
	public static final short VMI_MEM_MAX =
		9;
	
	/** Invoke the garbage collector. */
	public static final short GARBAGE_COLLECT =
		10;
	
	/** Exit the VM. */
	public static final short EXIT =
		11;
	
	/** The API Level of the VM. */
	public static final short API_LEVEL =
		12;
	
	/** System call count. */
	public static final int NUM_SYSCALLS =
		13;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/23
	 */
	private SystemCallIndex()
	{
	}
}
