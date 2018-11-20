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
 * ArrayIndexManagerValueVariant.java
 * $Id: ArrayIndexManagerValueVariant.java,v 1.2 2002/02/21 07:42:20 rvprasad Exp $
 */

/**
 * This implementation of <code>ArrayIndexManager</code> provides <code>ArrayIndex</code> to
 * partition <code>ArrayVariant</code>s in 1-1 correspondence with <code>ValueVariant</code> for the
 * array creation expression.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class ArrayIndexManagerValueVariant implements ArrayIndexManager
{
	/**
	 * A Map from <code>ValueVariant</code>s to <code>ArrayIndex</code>s.
	 */
	private Map valueVariantMap;

	/**
	 * This implementation of <code>Index</code> provides an <code>ArrayIndex</code> in 1-1
	 * correspondence with the <code>ValueVariant</code> for the array object.
	 */
    class ArrayIndexValueVariant extends Index
	{
		/**
		 * The <code>ValueVariant</code> to which the <code>ArrayIndex</code> corresponds to.
		 */
		ValueVariant valueVariant;

		/**
		 * Creates a new <code>ArrayIndexValueVariant</code> instance.
		 *
		 * @param valueVariant the <code>ValueVariant</code> corresponding to the array creation
		 * expression.
		 */
		ArrayIndexValueVariant(ValueVariant valueVariant) {
			this.valueVariant = valueVariant;
		}
	}

	/**
	 * Constructor for the class.
	 */
	public ArrayIndexManagerValueVariant()
    {
		valueVariantMap = new HashMap();
    }

    /**
	 * Reset the data structures in the Manager.
	 *
	 */
	public void reset()
	{
		if (valueVariantMap != null) {
			valueVariantMap.clear();
		} // end of if (valueVariant != null)
	}

	/**
	 * This method provides the <code>ArrayIndex</code> mapped to the given
	 * <code>ValueVariant</code>.
	 *
	 * @param valueVariant the <code>ValueVariant</code> corresponding to array creation expression.
	 * @return the <code>Index</code> corresponding to the provided <code>ValueVariant</code>. */
	public Index select(ValueVariant valueVariant)
    {
		ArrayIndexValueVariant arrayIndex;

		if (valueVariantMap.containsKey(valueVariant)) {
			// if valueVariant is registered, then return index
			arrayIndex = (ArrayIndexValueVariant)valueVariantMap.get(valueVariant);
		} else {
			// if value variant is not registered, then register it
			// with a new ArrayIndexValueVariant
			arrayIndex = new ArrayIndexValueVariant(valueVariant);
			valueVariantMap.put(valueVariant,arrayIndex);
		}
		return arrayIndex;
    }
}
