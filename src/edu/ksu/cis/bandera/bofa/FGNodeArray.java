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
 * FGNodeArray.java
 * $Id: FGNodeArray.java,v 1.2 2002/02/21 07:42:22 rvprasad Exp $
 */

/**
 * This class represents the node that describes array variant(associated with the array object) in
 * the flow graph.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class FGNodeArray extends FGNode
{
	/**
	 * Array variant described by the FG node
	 */
	private ArrayVariant arrayVariant;

	/**
	 * Constructor that uses empty sets for values, actions, and succs.
	 *
	 * @param arrayVariant array variant described by this flowgraph node.
	 */
	public FGNodeArray(ArrayVariant arrayVariant)
    {
		super();
		this.arrayVariant = arrayVariant;
    }

	/**
	 * Returns the arrayVariant described by this node.
	 *
	 * @return the arrayVariant described by this node.
	 */
	public ArrayVariant getArrayVariant()
    {
		return this.arrayVariant;
    }

	/**
	 * Changes the array variant described by this node.
	 *
	 * @param arrayVariant the new arra variant to be described by this node.
	 */
	void putArrayVariant(ArrayVariant arrayVariant)
    {
		this.arrayVariant = arrayVariant;
    }
}