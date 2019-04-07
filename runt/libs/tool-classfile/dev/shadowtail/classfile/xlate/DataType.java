// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * Represents the data type to use for read/write.
 *
 * @since 2019/03/24
 */
public enum DataType
{
	/** Byte. */
	BYTE,
	
	/** Short. */
	SHORT,
	
	/** Character. */
	CHARACTER,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** Object. */
	OBJECT,
	
	/** End. */
	;
	
	/**
	 * Returns this as a basic Java type.
	 *
	 * @return The Java type of this.
	 * @since 2019/04/06
	 */
	public final JavaType toJavaType()
	{
		switch (this)
		{
			case OBJECT:	return JavaType.OBJECT;
			case BYTE:
			case SHORT:
			case INTEGER:	return JavaType.INTEGER;
			case FLOAT:		return JavaType.FLOAT;
			case LONG:		return JavaType.LONG;
			case DOUBLE:	return JavaType.DOUBLE;
		}
		
		throw new todo.OOPS(this.name());
	}
	
	/**
	 * Returns the data type used for the primitive type.
	 *
	 * @param __t The type to use, {@code null} is treated as a pointer.
	 * @return The data type used.
	 * @since 2019/03/24
	 */
	public static final DataType of(PrimitiveType __t)
	{
		if (__t == null)
			return OBJECT;
		
		switch (__t)
		{
			case BOOLEAN:
			case BYTE:		return BYTE;
			case CHARACTER:	return CHARACTER;
			case SHORT:		return SHORT;
			case INTEGER:	return INTEGER;
			case LONG:		return LONG;
			case FLOAT:		return FLOAT;
			case DOUBLE:	return DOUBLE;
			
			default:
				throw new todo.OOPS(__t.name());
		}
	}
	
	/**
	 * Returns the data type of the given Java type.
	 *
	 * @param __t The type to get.
	 * @return The data type used for this type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/27
	 */
	public static final DataType of(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return DataType.of(__t.type().primitiveType());
	}
	
	/**
	 * Returns the data type of the given field descriptor.
	 *
	 * @param __t The type to get.
	 * @return The data type used for this type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	public static final DataType of(FieldDescriptor __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return DataType.of(__t.primitiveType());
	}
}
