// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.Objects;

/**
 * This is the SquirrelJME kernel which uses a kernel interface to interface
 * with the native system, this class manages processes and threads within
 * the virtual machine.
 *
 * @since 2016/10/31
 */
public class Kernel
{
	/** The minimum number of permissible cycles. */
	public static final int MIN_CYCLES =
		16;
	
	/** The interface used by the kernel to do native things. */
	protected final KernelInterface ki;
	
	/** The thread execution model used. */
	protected final ThreadingExecutionModel threadexecmodel;
	
	/**
	 * Initializes the kernel using the given interface.
	 *
	 * @param __ki The kernel interface to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/31
	 */
	public Kernel(KernelInterface __ki)
		throws NullPointerException
	{
		// Check
		if (__ki == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.ki = __ki;
		
		// {@squirreljme.error BH01 No threading execution model was
		// specified.}
		this.threadexecmodel = Objects.requireNonNull(
			__ki.threadingExecutionModel(), "BH01");
	}
	
	/**
	 * Runs the kernel loop.
	 *
	 * @return {@code true} if the kernel has not yet exited.
	 * @throws InterruptedException If the loop operation was interrupted.
	 * @since 2016/10/31
	 */
	public final boolean run()
		throws InterruptedException
	{
		KernelInterface ki = this.ki;
		for (int i = 0, cycles = Math.max(MIN_CYCLES, ki.runCycleCount());;)
		{
			// Which threading model is used?
			switch (this.threadexecmodel)
			{
					// SquirrelJME does not manage threading
				case EXTERNAL_THREADING:
					__threadExternal();
					break;
			
					// SquirrelJME slices threads itself
				case SLICE_THREADING:
					__threadInternal();
					break;
			
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Yield to let other host processes run (if required)
			if ((++i) >= cycles)
			{
				i = 0;
				cycles = Math.max(MIN_CYCLES, ki.runCycleCount());
				
				// {@squirreljme.error BH02 The kernel was interrupted.}
				if (ki.isKernelInterrupted())
					throw new InterruptedException("BH02");
				
				// Execute yield
				ki.cooperativeHostYield();
			}
		}
	}
	
	/**
	 * Threads not sliced by SquirrelJME.
	 *
	 * @throws InterruptedException If execution is interrupted.
	 * @since 2016/10/31
	 */
	private void __threadExternal()
		throws InterruptedException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Threads are sliced by SquirrelJME.
	 *
	 * @throws InterruptedException If execution is interrupted.
	 * @since 2016/10/31
	 */
	private void __threadInternal()
		throws InterruptedException
	{
		throw new Error("TODO");
	}
}

