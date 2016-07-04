// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITCPUVariant;

/**
 * This provider allows the creation of simulations that simulate a given
 * operating system (and any of its variants) on a given CPU. The purpose of
 * the simulator is to test SquirrelJME although it may be possible to run a
 * more limited set of programs on it.
 *
 * This is used with the service loader.
 *
 * @since 2016/07/04
 */
public abstract class SimulationProvider
{
	/** Service lookup. */
	private static final ServiceLoader<SimulationProvider> _SERVICES =
		ServiceLoader.<SimulationProvider>load(SimulationProvider.class);

	/** The architecture which is simulated. */
	protected final String architecture;
	
	/** The operating system to simulate. */
	protected final String operatingsystem;
	
	/** The variant of the operating system. */
	protected final String operatingsystemvar;
	
	/**
	 * Initializes the base simulation provider.
	 *
	 * @param __arch The architecture being simulated.
	 * @param __os The operating system to simulate.
	 * @param __osvar The operating system variant.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/04
	 */
	public SimulationProvider(String __arch, String __os, String __osvar)
		throws NullPointerException
	{
		// Check
		if (__arch == null || __os == null || __osvar == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.architecture = __arch;
		this.operatingsystem = __os;
		this.operatingsystemvar = __osvar;
	}
	
	/**
	 * Creates a new simulation to run the given system.
	 *
	 * @param __grp The simulation group.
	 * @param __archvar The variant of the architecture.
	 * @param __archend The endianess of the architecture.
	 * @param __prog The path to the program to run.
	 * @param __args The arguments to the program.
	 * @return The newly created simulation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public abstract Simulation create(SimulationGroup __grp,
		JITCPUVariant __archvar, JITCPUEndian __archend, Path __prog,
		String... __args)
		throws NullPointerException;
	
	/**
	 * Is this a supported CPU variant and endian?
	 *
	 * @param __archvar The variant to check.
	 * @param __archend The endianess.
	 * @return {@code true} if it is supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public abstract boolean isGivenVariantAndEndian(JITCPUVariant __archvar,
		JITCPUEndian __archend)
		throws NullPointerException;
	
	/**
	 * Returns the simulated architecture.
	 *
	 * @return The simulated architecture.
	 * @since 2016/07/04
	 */
	public final String architectureName()
	{
		return this.architecture;
	}
	
	/**
	 * Is this a provider which supports the given system?
	 *
	 * @param __arch The input architecture.
	 * @param __archvar The architecture variant.
	 * @param __archend The endianess of the CPU.
	 * @param __os The operating system.
	 * @param __osvar The operating system variant.
	 * @return {@code true} if the given system is simulated by this provider.
	 * @since 2016/07/04
	 */
	public final boolean isGivenSystem(String __arch, JITCPUVariant __archvar,
		JITCPUEndian __archend, String __os, String __osvar)
		throws NullPointerException
	{
		// Check
		if (__arch == null || __archvar == null || __archend == null ||
			__os == null || __osvar == null)
			throw new NullPointerException("NARG");
		
		// Must be the same
		return this.architecture.equals(__arch) &&
			this.operatingsystem.equals(__os) &&
			this.operatingsystemvar.equals(__osvar) &&
			isGivenVariantAndEndian(__archvar, __archend);
	}
	
	/**
	 * Returns the name of the operating system to simulate.
	 *
	 * @return The name of the simulated operating system.
	 * @since 2016/07/04
	 */
	public final String operatingSystemName()
	{
		return this.operatingsystem;
	}
	
	/**
	 * Returns the variant of the operating system to simulate.
	 *
	 * @return The operating system variant to simulate.
	 * @since 2016/07/04
	 */
	public final String operatingSystemVariant()
	{
		return this.operatingsystemvar;
	}
	
	/**
	 * Goes through all simulation provider services and locates resources that
	 * are avaialble to the classes that the provider is a part of. This is
	 * used so that configurations may be read from providers which may reside
	 * in other JAR files.
	 *
	 * @param __res The resource to locate.
	 * @return The stream of the given resource or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public static InputStream getResourceAsStream(String __res)
		throws NullPointerException
	{
		// Lock
		ServiceLoader<SimulationProvider> services = _SERVICES;
		synchronized (services)
		{
			for (SimulationProvider sp : services)
			{
				// Get resource off the class
				InputStream rv = sp.getClass().getResourceAsStream(__res);
				
				// Return if found
				if (rv != null)
					return rv;
			}
		}
		
		// Not found
		return null;
	}
}

