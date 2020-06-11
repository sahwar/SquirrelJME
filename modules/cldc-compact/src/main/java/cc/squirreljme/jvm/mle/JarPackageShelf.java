// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import java.io.InputStream;

/**
 * This allows access to the library class path and resources.
 *
 * @since 2020/06/07
 */
public final class JarPackageShelf
{
	/**
	 * Returns the classpath of the current program.
	 *
	 * @return The classpath of the current program.
	 * @since 2020/06/07
	 */
	public static native JarPackageBracket[] classPath();
	
	/**
	 * Opens the resource from the input stream.
	 *
	 * @param __jar The JAR to open.
	 * @param __rc The resource to load from the given JAR.
	 * @return Input stream to the resource.
	 * @since 2020/06/07
	 */
	public static native InputStream openResource(JarPackageBracket __jar,
		String __rc);
}