/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999                                          *
 * John Hatcliff (hatcliff@cis.ksu.edu)
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
 * ArrayVariantManager.java
 * $Id: ArrayVariantManager.java,v 1.2 2002/02/21 07:42:20 rvprasad Exp $
 */

/**
 * This class maps various Array classes and value variants(array creation site expression) to
 * <code>ArrayVariant</code>s.
 *
 * We maintain a map from each <code>ClassTokenArray</code>(array class) to IndexMap.  The IndexMap
 * maps the <code>Index</code>(array indices) to <code>ArrayVariant</code>s.
 *
 * @author <a href="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</a>
 * @author  <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class ArrayVariantManager
{
	/**
	 * <code>ArrayIndexManager</code> used to obtain indices for <code>ValueVariant</code>s.
	 */
	private static ArrayIndexManager arrayIndexManager;

	/**
	 * Maps the <code>ClassTokenArray</code>s to a map of  <code>ArrayIndex</code> to
	 * <code>ArrayVariant</code>s.
	 */
	private static Map arrayClassMap;

	/**
	 * Initializes the class with an <code>ArrayIndexManager</code>.
	 *
	 * @param arrayIndexManager the <code>ArrayIndexManager</code> to use to get <code>Index</code>.
	 */
	public static void init(ArrayIndexManager arrayIndexManager)
    {
		ArrayVariantManager.arrayIndexManager = arrayIndexManager;
		arrayClassMap     = new HashMap();
    }

    /**
	 * Resets the data structures in the Manager.
	 *
	 */
	public static void reset() {
		if (arrayClassMap != null) {
			arrayClassMap.clear();
		} // end of if (arrayClassMap != null)
		if (arrayIndexManager != null) {
			arrayIndexManager.reset();
		} // end of if (arrayIndexManager != null)
    }

	/**
	 * Returns an <code>ArrayVariant</code> corresponding to the given <code>ClassTokenArray</code>
	 * and <code>ValueVaraint</code>.  If none is available, a new array variant is created and
	 * returned.
	 *
	 * @param classToken the class of the array.
	 * @param valueVariant the array creation expression.
	 * @return the <code>ArrayVariant</code> registered for the array objects of type
	 * <code>classToken</code> corresponding to the <code>valueVaraint</code>.
	 */
	public static ArrayVariant select(ClassToken classToken, ValueVariant  valueVariant)
    {
		Map             arrayIndexMap;
		Index           arrayIndex;
		ArrayVariant    arrayVariant;
		FGNodeArray     node;
		ClassTokenArray classTokenArray;

		// type-check classToken to make sure it describes an array Should throw an appropriate
		// exception if test fails, but let type system do it for now.

		classTokenArray = (ClassTokenArray)classToken;

		// compute index based on value variant

		arrayIndex = arrayIndexManager.select(valueVariant);

		if (arrayClassMap.containsKey(classTokenArray)) {
			// if class is already registered, compute variant index, and see if it is registered.
			arrayIndexMap = (Map) arrayClassMap.get(classTokenArray);

			if (arrayIndexMap.containsKey(arrayIndex)) {
				// if array index is registered, then return array variant.
				arrayVariant = (ArrayVariant) arrayIndexMap.get(arrayIndex);
			} else {
				/*
				 * index was not registered, so make new array variant and register.  There is a
				 * circular reference here: FGNodeArray references the arrayVariant, and the
				 * arrayVariant references the FGNode.  So create the node first with a null
				 * reference for arrayVariant.  Then, fill in the arrayVariant field after the
				 * arrayVariant has been created.
				 */
				node = new FGNodeArray(null);
				arrayVariant = new ArrayVariant(classTokenArray, arrayIndex, node);
				node.putArrayVariant(arrayVariant);
				arrayIndexMap.put(arrayIndex,arrayVariant);
			}
		} else {
			/*
			 * if class token is not registered, then register it with an index map with the current
			 * variant.
			 */
			arrayIndexMap = new HashMap();
			/*
			 * There is a circular reference here:  FGNodeArray references the arrayVariant, and the
			 * arrayVariant references the FGNode.  So create the node first with a null reference
			 * for arrayVariant.  Then, fill in the arrayVariant field after the arrayVariant has
			 * been created.
			 */
			node = new FGNodeArray(null);
			arrayVariant = new ArrayVariant(classTokenArray, arrayIndex, node);
			node.putArrayVariant(arrayVariant);

			arrayIndexMap.put(arrayIndex,arrayVariant);
			arrayClassMap.put(classTokenArray,arrayIndexMap);
		}
		return arrayVariant;
    }

	/**
	 * Provide the <code>ClassTokenArray</code> objects registered with the Manager.
	 *
	 * @return the set of registered <code>ClassTokenArray</code> objects.
	 */
	public static Set getClassTokens()
    {
		return arrayClassMap.keySet();
    }

	/**
	 * Provide a collection of maps corresponding to each distinct <code>ClassTokenArray</code>
	 * registered.
	 *
	 * @return the collection of maps.
	 */
	public static Collection getValues()
    {
		// returns a collection of maps //
		return arrayClassMap.values();
    }

}
