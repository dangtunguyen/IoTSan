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
import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.soot.jimple.Value;

import java.util.*;

/*
 * ValueVariantManager.java
 * $Id: ValueVariantManager.java,v 1.2 2002/02/21 07:42:24 rvprasad Exp $
 */

/**
 * This class manages the value variants depending on the enclosing class, method and position in
 * code where the variants were created.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class ValueVariantManager
{
	/**
	 * Manages the indices for value variants.
	 */
	private static ValueIndexManager valueIndexManager;

	/**
	 * Manages the indices for position in code.
	 */
	private static CodeIndexManager  codeIndexManager;

	/**
	 * Maps class tokens to map of value indices.
	 */
	private static Map classMap;

	/**
	 * Maps class tokens to values.  This is used for objects like null and so
	 * on.
	 */
	private static Map constClassMap;

	/**
	 * Initilizes the class.
	 *
	 * @param valueIndexManager the manager to be used to manage value indices.
	 * @param codeIndexManager the manager to be used to manage code indices.
	 */
	public static void init(ValueIndexManager valueIndexManager, CodeIndexManager codeIndexManager) {
		ValueVariantManager.valueIndexManager = valueIndexManager;
		ValueVariantManager.codeIndexManager  = codeIndexManager;
		classMap = new HashMap();
		constClassMap = new HashMap();
	}

    /**
	 * Describe <code>reset</code> method here.
	 *
	 */
	public static void reset() {
		if (classMap != null) {
			classMap.clear();
		} // end of if (classMap != null)
		if (codeIndexManager != null) {
			codeIndexManager.reset();
		} // end of if (codeIndexManager != null)
	    if (constClassMap != null) {
			constClassMap.clear();
		} // end of if (constClassMap != null)
		if (valueIndexManager != null) {
			valueIndexManager.reset();
		} // end of if (valueIndexManager != null)
    }

	/**
	 * Provides the value variants associated with constant objects.  Null objects and
	 * BOFA_UnknowValue objects are obtained from here.
	 *
	 * @param classToken the class token representing the type of the value variant.
	 * @return the corresponding value variant.
	 */
	  //  public static ValueVariant select(ClassToken classToken,
	  //									MethodVariant methodVariant)
	  //  {
	  //	if (constClassMap.containsKey(classToken)) {
	  //		return (ValueVariant)constClassMap.get(classToken);
	  //	} // end of if ()
	  //	else {
	  //		ValueVariant valueVariant = new ValueVariant(classToken,
	  //													 methodVariant,
	  //													 (Value)null,
	  //													 (Stmt)null);
	  //		constClassMap.put(classToken, valueVariant);
	  //		return valueVariant;
	  //	} // end of else
	  //  }


	/**
	 * Provides the Value variant associated with given combination.
	 *
	 * @param classToken the class in which the value variant occurs.
	 * @param methodVariant the method in which the value variant occurs.
	 * @param expr a <code>Value</code> value
	 * @param stmt the statement in which the value variant occurs.
	 * @return a <code>ValueVariant</code> value
	 */
	public static ValueVariant select(ClassToken classToken, MethodVariant methodVariant, Value expr, Stmt stmt) {
		Map indexMap;
		Index valueIndex;
		Index  codeIndex;
		ValueVariant valueVariant;

		// compute index based on context info (code index gives position in the code)

		codeIndex  = codeIndexManager.select(expr);
		valueIndex = valueIndexManager.select(methodVariant, codeIndex);

		if (classMap.containsKey(classToken)) {
			// if class is already registered, compute variant index, and see if it is registered.
			indexMap = (Map) classMap.get(classToken);

			if (indexMap.containsKey(valueIndex)) {
				// if variant index is registered, then return value.
				valueVariant = (ValueVariant) indexMap.get(valueIndex);
			} else {
				// index was not registered, so make new value and register.
				valueVariant = new ValueVariant(classToken,valueIndex, expr, stmt);
				indexMap.put(valueIndex,valueVariant);
			}
		} else {
			// if class is not registered, then register it with an index map with the current
			// variant
			indexMap     = new HashMap();
			valueVariant = new ValueVariant(classToken,valueIndex, expr, stmt);
			indexMap.put(valueIndex,valueVariant);
			classMap.put(classToken,indexMap);
		}
		return valueVariant;
    }

	/**
	 * Provide the set of classes in which value variants are managed.
	 *
	 * @return the set of classes in which value variants are managed.
	 */
	public static Set getClasses()
    {
		return classMap.keySet();
    }

	/**
	 * Provide the set of maps of the managed value variants.
	 *
	 * @return the set of maps of the managed value variants.
	 */
	public static Collection getValues()
    {
		// returns a collection of maps //
		return classMap.values();
    }
}
