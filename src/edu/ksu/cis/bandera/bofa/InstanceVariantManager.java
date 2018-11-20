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
 * InstanceVariantManager.java
 * $Id: InstanceVariantManager.java,v 1.2 2002/02/21 07:42:23 rvprasad Exp $
 */

/**
 * This class that manages the instance variable variants.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class InstanceVariantManager
{
	/**
	 * Manager of the indices associated with instance variants.
	 *
	 */
	private static InstanceIndexManager instanceIndexManager;

	/**
	 * Mapping from field to instance indices map.
	 *
	 */
	private static Map                  instanceMap;

	/**
	 * Initialize the Manager.
	 * @param instanceIndexManager the manager who will provide the indices to get to the instance
	 * variants.
	 */
	public static void init(InstanceIndexManager instanceIndexManager) {
		InstanceVariantManager.instanceIndexManager = instanceIndexManager;
		InstanceVariantManager.instanceMap          = new HashMap();
	}

    /**
	 * Reset the data structures of the manager.
	 *
	 */
	public static void reset() {
		if (instanceMap != null) {
			instanceMap.clear();
		} // end of if (instanceMap != null)
		if (instanceIndexManager != null) {
			instanceIndexManager.reset();
		} // end of if (instanceIndexManager != null)
    }

	/**
	 * Select the variant corresponding to the instance variable.  If none exists, then a new one is
	 * created.  In this implementation only one instance variant is associated with one field
	 * variable of a class.
	 *
	 * @param sootField the field for which variant is requested.
	 * @return the corresponding variant.
	 */
	public static InstanceVariant select(SootField  sootField)
    {
		Map             indexMap;
		Index           instanceIndex;
		InstanceVariant instanceVariant;
		FGNodeField     node;

		// compute index based on context info
		instanceIndex = instanceIndexManager.select();

		if (instanceMap.containsKey(sootField)) {
			// if field is already registered, compute variant index, and
			// see if it is registered.
			indexMap = (Map) instanceMap.get(sootField);

			if (indexMap.containsKey(instanceIndex)) {
				// if variant index is registered, then return variant
				instanceVariant = (InstanceVariant) indexMap.get(instanceIndex);
			} else {
				// index was not registered, so make new instance and register.
				node = new FGNodeField(sootField);
				instanceVariant = new InstanceVariant(sootField, instanceIndex, node);
				indexMap.put(instanceIndex,instanceVariant);
			}
		} else {
			// if instance field is not registered, then register it with an index map with the
			// current variant
			indexMap = new HashMap();
			node = new FGNodeField(sootField);
			instanceVariant = new InstanceVariant(sootField, instanceIndex, node);
			indexMap.put(instanceIndex,instanceVariant);
			instanceMap.put(sootField,indexMap);
		}
		return instanceVariant;
    }

	/**
	 * Provides the set of instance variables managed.
	 *
	 * @return set of instance variants managed.
	 */
	public static Set getFields()
    {
		return instanceMap.keySet();
    }

	/**
	 * Provides the collection of instance variants managed.
	 *
	 * @param sootField a <code>SootField</code> value
	 * @return collection of instance variants managed.
	 */
	public static Collection getVariants(SootField sootField)
    {
		return ((Map) instanceMap.get(sootField)).values();
    }
}
