/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Shawn Laubach (laubach@acm.org)        *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package edu.ksu.cis.bandera.bofa;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;

/*
 * ClassTokenArray.java
 * $Id: ClassTokenArray.java,v 1.2 2002/02/21 07:42:21 rvprasad Exp $
 */

/**
 * This class represents the class token for array types in the analysis.  In java array types are
 * represented as classes.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.2 $)
 */
public class ClassTokenArray implements ClassToken
{
	/**
	 * Maps an array type to a class token,<code>ClassToken</code>.
	 *
	 * An integer array of 3 dimension is an array type.
	 */
	private static Map allocatedTokens = new HashMap();

	/**
	 * Provides the class token associated with the given array type.
	 *
	 * @param arrayType the array type for which the class token is requested.
	 * @return the class token associated with the array type.
	 */
	public static ClassTokenArray select(ArrayType arrayType)
    {
		ClassTokenArray classTokenArray;

		/*
		 * Currently, Soot allows multiple array type instances to describe the same array type.  We
		 * want a single ClassTokenArray instance for each distinct logical array type.  in simple
		 * words, Soot associates a unique SootClass object with each 3 dimensional array of type A
		 * whereas we require one SootClass object to be associated with all 3 dimensional arrays of
		 * type A.  Therefore, before allocating a new ClassTokenArray instance, we want to check to
		 * see if a ClassTokenArray instance has already been allocated for a given logical type.
		 */

		/*
		 * First, check to see if the the given array type is already stored in
		 * the table.
		 */
		if (allocatedTokens.containsKey(arrayType)) {
			classTokenArray = (ClassTokenArray) allocatedTokens.get(arrayType);
		} else {
			/*
			 * Check if a logically equivalent array type is stored in the
			 * table.  If so, use that or create a new one.
			 */
			ArrayType canonicalArrayType =
				getCanonicalArrayTypeInstance(arrayType);

			if (canonicalArrayType != null) {
				// return the classToken associated with the array type
				classTokenArray = (ClassTokenArray) allocatedTokens.get(canonicalArrayType);
			} else {
				// there is no logically equivalent stored array type, so create a new class token
				// for it, and the current array type becomes the canonical representation.
				classTokenArray = new ClassTokenArray(arrayType);
				allocatedTokens.put(arrayType,classTokenArray);
			}
		}
		return classTokenArray;
    }

	/**
	 * Provides the class token associated with the given array type.
	 *
	 * In this method, array types are tested for logical equality rather than object equality.  It
	 * is the case that there may be more than one SootClass object pertaining to equivalent array
	 * types.  Hence, the name getCanonicalArrayTypeInstance.
	 *
	 * @param arrayType the array type which needs to be checked if registered
	 * @return the class token associated with the array type, if any, else null.
	 */
	private static ArrayType getCanonicalArrayTypeInstance(ArrayType arrayType)
    {
		/**
		 *  Iterates over stored array types in allocatedTokens map to see if we
		 *  have already allocated a ClassTokenArray for a logically
		 *  equivalent array type.  If we have, return that array type
		 *  instance.  If we have * not, return null.
		 */
		ArrayType storedArrayType;
		for (Iterator i = allocatedTokens.keySet().iterator(); i.hasNext();)
			{
				storedArrayType = (ArrayType) i.next();
				if (storedArrayType.equals(arrayType)) {
					return storedArrayType;
				}
			}
		return null;
    }

	/**
	 * Constructs a new array type in which the component type is of the given
	 * type.
	 *
	 * @param componentType the component type of the requested array type.
	 * @return the new array type with the given component type.
	 */
	public static ArrayType componentToArrayType(Type componentType)
    {
		if (componentType instanceof BaseType) {
			return ArrayType.v((BaseType) componentType, 1);
		} else
			if (componentType instanceof ArrayType) {
				return ArrayType.v(((ArrayType) componentType).baseType,
								   ((ArrayType) componentType).numDimensions + 1);
			} else
				throw new RuntimeException("Invalid type");
    }

	/**
	 * Extracts the component type of the given array type.
	 *
	 * @param arrayType the array type whose component type is requested.
	 * @return the component type of the given array type.
	 */
	public static Type arrayTypeToComponentType(ArrayType arrayType)
    {
		if (arrayType.numDimensions == 1) {
			return arrayType.baseType;
		} else {
			return ArrayType.v(arrayType.baseType, arrayType.numDimensions - 1);
		}
    }

	/**
	 * The ArrayType object associated with this class token object.
	 */
	ArrayType arrayType;

	/**
	 * Creates a new object.
	 *
	 * @param arrayType the array type associated with this class token.
	 */
	private ClassTokenArray(ArrayType arrayType) {
		this.arrayType = arrayType;
    }

	/**
	 * Returns the ArrayType associated with this class token.
	 *
	 * @return a soot object representing the array type associated with this class token.
	 */
	public ArrayType getArrayType() {
		return arrayType;
    }

	public String toString() {
		return arrayType.toString();
	}
}
