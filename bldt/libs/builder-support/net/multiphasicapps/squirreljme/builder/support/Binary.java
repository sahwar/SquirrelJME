// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.attribute.FileTime;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.squirreljme.runtime.midlet.DependencySet;
import net.multiphasicapps.squirreljme.runtime.midlet.ManifestedDependency;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This class represents a binary which has been loaded by the binary manager.
 *
 * @since 2017/10/31
 */
public final class Binary
{
	/** The name of this binary. */
	protected final SourceName name;
	
	/** The source code for this binary, may be null if there is none. */
	protected final Source source;
	
	/** The path to the binary for this executable. */
	protected final Path path;
	
	/** The cached manifest for this entry. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** Dependencies that this source code relies on. */
	private volatile Reference<DependencySet> _dependencies;
	
	/** Dependencies that are provided by this binary. */
	private volatile Reference<DependencySet> _provideddeps;
	
	/**
	 * Initializes the binary.
	 *
	 * @param __name The name of this binary.
	 * @param __source The source of this binary, may be {@code null} if there
	 * is no source.
	 * @throws InvalidBinaryException If the tiven 
	 * @throws NoSuchBinaryException If the given binary does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/02
	 */
	Binary(SourceName __name, Source __source, Path __path)
		throws InvalidBinaryException, NoSuchBinaryException,
			NullPointerException
	{
		if (__name == null || __path == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.source = __source;
		this.path = __path;
	}
	
	/**
	 * Returns the set of dependencies which are needed for this project to
	 * operate correctly.
	 *
	 * @return The set of dependencies.
	 * @throws InvalidBinaryException If this binary is not valid.
	 * @since 2017/11/17
	 */
	public final DependencySet dependencies()
		throws InvalidBinaryException
	{
		// If the binary is newer then use the dependencies read from the
		// manifest
		if (isBinaryNewer())
		{
			Reference<DependencySet> ref = this._dependencies;
			DependencySet rv;
		
			if (ref == null || null == (rv = ref.get()))
				this._dependencies = new WeakReference<>(
					(rv = DependencySet.neededByManifest(manifest())));
			
			return rv;
		}
		
		// {@squirreljme.error AU0a Cannot get dependencies for the binary
		// because it has no source.}
		Source source = this.source;
		if (source == null)
			throw new InvalidBinaryException("AU0a");
		return source.approximateBinaryDependencySet();
	}
	
	/**
	 * Returns the input stream over the binary itself.
	 *
	 * @return The input stream over the binary.
	 * @throws IOException On read errors.
	 * @since 2017/11/17
	 */
	public final InputStream inputStream()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns if the binary is newer.
	 *
	 * @return If the binary is newer.
	 * @since 2017/11/06
	 */
	public final boolean isBinaryNewer()
	{
		Source source = this.source;
		return lastModifiedTime() >=
			(source != null ? source.lastModifiedTime() : Long.MIN_VALUE);
	}
	
	/**
	 * Returns if the source code is newer.
	 *
	 * @return If the source is newer.
	 * @since 2017/11/06
	 */
	public final boolean isSourceNewer()
	{
		return !isBinaryNewer();
	}
	
	/**
	 * Returns the time that the binary was last modified.
	 *
	 * @return The time the binary was last modified.
	 * @since 2017/11/06
	 */
	public final long lastModifiedTime()
	{
		// The file might not actually exist if it has not been built
		try
		{
			FileTime t = Files.getLastModifiedTime(this.path);
			if (t != null)
				return t.toMillis();
			return Long.MIN_VALUE;
		}
		
		// File does not exist or another error, so unknown an unknown time
		catch (IOException e)
		{
			return Long.MIN_VALUE;
		}
	}
	
	/**
	 * Returns the manifest for this binary.
	 *
	 * @return The manifest for this project.
	 * @since 2017/11/05
	 */
	public final JavaManifest manifest()
	{
		if (isSourceNewer())
			return this.source.approximateBinaryManifest();
		
		// Open the binary instead
		try (ZipBlockReader zip = zipBlock())
		{
			try (InputStream in = zip.open("META-INF/MANIFEST.MF"))
			{
				return new JavaManifest(in);
			}
		}
		
		// {@squirreljme.error AU0b Could not read the binary manifest.}
		catch (IOException e)
		{
			throw new InvalidBinaryException("AU0b", e);
		}
	}
	
	/**
	 * Returns an array containing the dependencies from the inpout
	 * dependencies which match and which this binary statisfies.
	 *
	 * @param __d The input dependencies to check.
	 * @return The dependencies which are matched by this binary, an empty
	 * array will be returned if there are no matches.
	 * @throws NullPointerException On null arguments.
	 * @sine 2017/11/26
	 */
	public final ManifestedDependency[] matchedDependencies(
		ManifestedDependency... __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		return this.matchedDependencies(
			Arrays.<ManifestedDependency>asList(__d));
	}
	
	/**
	 * Returns an array containing the dependencies from the inpout
	 * dependencies which match and which this binary statisfies.
	 *
	 * @param __d The input dependencies to check.
	 * @return The dependencies which are matched by this binary, an empty
	 * array will be returned if there are no matches.
	 * @throws NullPointerException On null arguments.
	 * @sine 2017/11/26
	 */
	public final ManifestedDependency[] matchedDependencies(
		Collection<ManifestedDependency> __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		Set<ManifestedDependency> rv = new LinkedHashSet<>();
		
		// Go through input dependencies and just see return any dependencies
		// we provide which are in there
		DependencySet provided = this.providedDependencies();
		for (ManifestedDependency d : __d)
		{
			if (provided.contains(d))
				rv.add(d);
		}
		
		return rv.<ManifestedDependency>toArray(
			new ManifestedDependency[rv.size()]);
	}
	
	/**
	 * Returns the name of the project.
	 *
	 * @return The project name.
	 * @since 2017/11/02
	 */
	public final SourceName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the dependencies which are provided by this binary.
	 *
	 * @return The dependencies provided by this binary.
	 * @since 2017/11/26
	 */
	public final DependencySet providedDependencies()
	{
		// If the binary is newer then use the dependencies read from the
		// manifest
		if (isBinaryNewer())
		{
			Reference<DependencySet> ref = this._provideddeps;
			DependencySet rv;
		
			if (ref == null || null == (rv = ref.get()))
				this._provideddeps = new WeakReference<>(
					(rv = DependencySet.providedByManifest(manifest())));
			
			return rv;
		}
		
		// {@squirreljme.error AU0e Cannot get provided dependencies for the
		// binary because it has no source code.}
		Source source = this.source;
		if (source == null)
			throw new InvalidBinaryException("AU0e");
		return source.approximateBinaryProvidedDependencies();
	}
	
	/**
	 * Returns the source project that this binary is built from.
	 *
	 * @return The source project or {@code null} if there is no source.
	 * @since 2017/11/06
	 */
	public final Source source()
	{
		return this.source;
	}
	
	/**
	 * Returns the type of project that this is.
	 *
	 * @return The project type.
	 * @since 2017/11/26
	 */
	public final ProjectType type()
	{
		JavaManifestAttributes attr = this.manifest().getMainAttributes();
		
		// Midlet?
		if (attr.definesValue("midlet-name"))
			return ProjectType.MIDLET;
		
		// APIs are like liblets but have a special flag to them
		else if ("true".equals(attr.getValue("x-squirreljme-isapi")))
			return ProjectType.API;
		
		// Otherwise everything else is a liblet
		return ProjectType.LIBLET;
	}
	
	/**
	 * Opens the binary as a ZIP file for reading the contents.
	 *
	 * @return The reader for the ZIP file as a block.
	 * @throws IOException On read errors.
	 * @since 2017/11/06
	 */
	public final ZipBlockReader zipBlock()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Opens the binary as a ZIP stream for reading the contents.
	 *
	 * @return The stream over the ZIP's contents.
	 * @throws IOException On read errors.
	 * @since 2017/11/02
	 */
	public final ZipStreamReader zipStream()
		throws IOException
	{
		throw new todo.TODO();
	}
}

