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
import ca.mcgill.sable.soot.jimple.Value;
import ca.mcgill.sable.util.*;

/*
 * CodeIndexManagerAlloc.java
 * $Id: CodeIndexManagerAlloc.java,v 1.2 2002/02/21 07:42:21 rvprasad Exp $
 */

/**
 * This class generates an index to partition object creation sites.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.2 $)
 */
public class CodeIndexManagerAlloc implements CodeIndexManager
{
	/**
	 * Maps the <code>Value</code> object to a index.
	 */
	Map positionMap;

	/**
	 * An implementation of Index for tracking code.
	 */
	class CodeIndexAlloc extends Index
	{
		/**
		 * The <code>Value</code> object associated with this object.
		 */
		private Value position;

		/**
		 * Constructor of the class.
		 *
		 * @param position <code>Value</code> object associated with this object.
		*/
		CodeIndexAlloc(Value position)
		{
			this.position = position;
		}

		/**
		 * Returns the corresponding Value object.
		 *
		 * @return The corresponding Value object.
		 */
		public Value getPosition()
		{
			return position;
		}
	}

	/**
	 * Constructor for the class.
	 */
	public CodeIndexManagerAlloc()
    {
		positionMap = new HashMap();
    }

	/**
	 * Resets the data structures inside the manager.
	 *
	 */
	public void reset()
	{
		positionMap.clear();
	}

	/**
	 * Provides the index corresponding to the given position.  If an index does not exist then a new
	 * one is created.
	 *
	 * @param position The <code>Value</code> for which the Index is requested.
	 * @return The <code>Index</code> corresponding to the given Value object.
	 */
	public Index select(Value position)
    {
		Index codeIndex;
		if (positionMap.containsKey(position)) {
			// if position is already registered, return the associated
			// index object.
			codeIndex = (Index) positionMap.get(position);
		} else {
			// if position is not registered, then register it
			codeIndex = new CodeIndexAlloc(position);
			positionMap.put(position,codeIndex);
		}
		return codeIndex;
    }
}
