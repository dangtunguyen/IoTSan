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

import ca.mcgill.sable.util.*;

/*
 * FASet.java
 * $Id: FASet.java,v 1.2 2002/02/21 07:42:21 rvprasad Exp $
 */

/**
 * Implements a set capable of calculating the difference between two sets.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.2 $)
 */
public class FASet extends HashSet
{
	/**
	 * Creates a new <code>FASet</code> instance.
	 *
	 */
	public FASet()
    {
        super();
    }

	/**
	 * Creates a new <code>FASet</code> instance.
	 *
	 * @param cap the initial capacity of the set.
	 */
	public FASet(int cap)
    {
        super(cap);
    }

	/**
	 * Creates a new <code>FASet</code> instance.
	 *
	 * @param cap the initial capacity of the set.
	 * @param load the expected load while using this instance of object.
	 */
	public FASet(int cap, float load)
    {
        super(cap,load);
    }

	/**
	 * Create a set which contains the given elements.
	 *
	 * @param element to be added into the set.
	 */
	public FASet(Object element)
    {
        super();
		this.add(element);
    }

	/**
	 * Create a set which contains the given elements.
	 *
	 * @param elements to be added into the set.
	 */
	public FASet(Object[] elements)
    {
		super(elements);
    }

	/**
	 * Builds a new FASet containing the difference between this set and the supplied set s.
	 *
	 * @param s the set to be substracted from this set
	 * @return the difference set
	 */
	public FASet buildDiffSet(Set s)
    {
		FASet diff = new FASet();

		for(Iterator i = this.iterator(); i.hasNext();) {
			Object e = i.next();

			if(!s.contains(e)) {
				diff.add(e);
			}
		}
		return diff;
    }
}
