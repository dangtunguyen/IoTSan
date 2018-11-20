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

/*
 * InstanceVariant.java
 * $Id: InstanceVariant.java,v 1.2 2002/02/21 07:42:23 rvprasad Exp $
 */

/**
 * This class represents instance variables variants.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class InstanceVariant
{
	/**
	 * Field described by this variant.
	 */
	private SootField sootField;

	/**
	 * Index to get to this instance variant.
	 */
	private Index instanceIndex;

	/**
	 * FG node that summarizes the value for this instance variant.
	 */
	private FGNodeField node;

	/**
	 * Constructor of the class.
	 *
	 * @param sootField the field this variant will describe.
	 * @param instanceIndex the index to get to this instance variant.
	 * @param node the flow graph node associated with this variant.
	 */
	public InstanceVariant(SootField     sootField,
						   Index instanceIndex,
						   FGNodeField   node)
    {
		this.sootField     = sootField;
		this.instanceIndex = instanceIndex;
		this.node          = node;
    }

	/**
	 * Provides the associated field.
	 *
	 * @return the associated field.
	 */
	public SootField getField()
    {
		return sootField;
    }

	/**
	 * Provides the associated index.
	 *
	 * @return the associated index.
	 */
	public Index getInstanceIndex()
    {
		return instanceIndex;
    }

	/**
	 * Provides the associated flow graph node.
	 *
	 * @return the associated flow graph node .
	 */
	public FGNodeField getNode()
    {
		return node;
    }
}
