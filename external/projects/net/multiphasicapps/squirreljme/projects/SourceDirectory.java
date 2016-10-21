// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;


/**
 * This contains the directory of source projects which may be compiled into
 * binary projects.
 *
 * The source directory is namespaced which permits multiple namespaces to
 * exist and for the categorization of source projects.
 *
 * @since 2016/10/20
 */
public final class SourceDirectory
	extends AbstractMap<ProjectName, SourceProject>
{
	/**
	 * Initializes the source directory.
	 *
	 * @param __d The owning project directory.
	 * @param __s The directory contianing namespaces which contain project
	 * sources.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	SourceDirectory(ProjectDirectory __d, Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__d == null || __p == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/20
	 */
	@Override
	public Set<Map.Entry<ProjectName, SourceProject>> entrySet()
	{
		throw new Error("TODO");
	}
}

