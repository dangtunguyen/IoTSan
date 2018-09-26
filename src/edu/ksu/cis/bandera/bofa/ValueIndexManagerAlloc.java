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

import ca.mcgill.sable.util.*;

/*
 * ValueIndexManagerAlloc.java
 * $Id: ValueIndexManagerAlloc.java,v 1.2 2002/02/21 07:42:24 rvprasad Exp $
 */

/**
 * Class that provides indices to identify various value variants associated with the same method and
 * position in the method.
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class ValueIndexManagerAlloc extends ValueIndexManager
{
	/**
	 * A map from method variant to code index.
	 */
	private Map methodVariantMap;

	/**
	 * Implementation of Index which are method variant and code position specific.
	 */
	public class ValueIndexAlloc extends Index
	{
		/**
		 * The method variant in which the index's variant originated.
		 */
		MethodVariant methodVariant;

		/**
		 * The position in code where the index's variant originated.
		 */
		Index     codeIndex;

		/**
		 * Constructor of the class.
		 *
		 * @param methodVariant the method in which the variant originated.
		 * @param codeIndex the position in the method where the variant originated.
		 */
		public ValueIndexAlloc(MethodVariant methodVariant, Index codeIndex)
		{
			this.methodVariant = methodVariant;
			this.codeIndex     = codeIndex;
		}
	}

	/**
	 * Constructor of the class.
	 */
	public ValueIndexManagerAlloc()
    {
		methodVariantMap = new HashMap();
    }

	/**
	 * Provide value index corresponding to the given method variant and code index. if none exists
	 * then create a new index.
	 *
	 * @param methodVariant the method to which the value index is associated.
	 * @param codeIndex the position to which the value index is associated.
	 * @return the associated index. 
	 */
	public Index select(MethodVariant methodVariant, Index codeIndex)
    {
		ValueIndexAlloc valueIndex;
		Map             codeIndexMap;

		if (methodVariantMap.containsKey(methodVariant)) {
			// if methodVariant is registered, then return method.
			codeIndexMap = (Map) methodVariantMap.get(methodVariant);

			if (codeIndexMap.containsKey(codeIndex)) {
				// return value index for this method variant and code index
				valueIndex = (ValueIndexAlloc) codeIndexMap.get(codeIndex);
			} else {
				// position was not registered, so add position and make new index
				valueIndex = new ValueIndexAlloc(methodVariant,codeIndex);
				codeIndexMap.put(codeIndex,valueIndex);
			}
		} else {
			// if method variant is not registered, then register it with an empty position Map, and
			// make a new position entry as well.

			codeIndexMap   = new HashMap();
			valueIndex    = new ValueIndexAlloc(methodVariant,codeIndex);
			codeIndexMap.put(codeIndex,valueIndex);
			methodVariantMap.put(methodVariant,codeIndexMap);
		}

		return valueIndex;
    }
}
