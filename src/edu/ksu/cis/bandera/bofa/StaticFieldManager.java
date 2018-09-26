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
 * StaticFieldManager.java
 * $Id: StaticFieldManager.java,v 1.2 2002/02/21 07:42:24 rvprasad Exp $
 */

/**
 * This class manages the static variables.
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class StaticFieldManager
{
	/**
	 * A Map from static fields to the associated flow graph node.
	 */
	private static Map fieldMap;

	/**
	 * Initializes the Manager.
	 */
	public static void init()
    {
		fieldMap = new HashMap();
    }

    /**
	 * Reset the data structures of the manager.
	 *
	 */
	static void reset() {
		if (fieldMap != null) {
			fieldMap.clear();
		} // end of if (fieldMap != null)
    }

	/**
	 * Provides the FG node associated with the provided static field.
	 *
	 * @param sootField the static field corresponding to which the FG node is requried.
	 * @return the associated FG node.
	 */
	static FGNodeField get(SootField sootField)
    {
		return (FGNodeField) fieldMap.get(sootField);
    }

	/**
	 * Adds a static field and the associated FG node.
	 *
	 * @param sootField the static field to be Managed.
	 * @param node the FG node associated with sootField.
	 */
	static void put(SootField sootField, FGNodeField node)
    {
		fieldMap.put(sootField,node);
    }

	/**
	 * Provides the flow graph node associated with the provided static field.  if none exists then a
	 * new one is created.
	 *
	 * @param sootField the static field corresponding to which the flow graph node is required.
	 * @return the associated flow graph node.
	 */
	public static FGNodeField select(SootField sootField)
    {
		FGNodeField node;

		if (fieldMap.containsKey(sootField)) {
			node = get(sootField);
		} else {
			node = new FGNodeField(sootField);
			put(sootField,node);

		}
		return node;
    }
}
