// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sun.javadoc;

public interface ProgramElementDoc
	extends Doc
{
	public abstract AnnotationDesc[] annotations();
	
	public abstract ClassDoc containingClass();
	
	public abstract PackageDoc containingPackage();
	
	public abstract boolean isFinal();
	
	public abstract boolean isPackagePrivate();
	
	public abstract boolean isPrivate();
	
	public abstract boolean isProtected();
	
	public abstract boolean isPublic();
	
	public abstract boolean isStatic();
	
	public abstract int modifierSpecifier();
	
	public abstract String modifiers();
	
	public abstract String qualifiedName();
}


