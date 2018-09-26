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
 * ArrayVariant.java
 * $Id: ArrayVariant.java,v 1.2 2002/02/21 07:42:20 rvprasad Exp $
 */

/**
 * This class represents the array object that may flow into an array reference.  These array objects
 * created using <i>new</i> are referred to as ArrayVariants.
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad"">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.2 $)
 */

public class ArrayVariant
{
	/**
	 * Describes an <code>Array</code> class.
	 */
	ClassTokenArray  classTokenArray;

	/**
	 * The <code>Index</code> corresponding to this variant.  This Index is obtained from
	 * <code>ArrayIndexManager</code> and used by <code>ArrayVariantManager</code> to map the
	 * <code>ValueVariant</code> and <code>ClassTokenArray</code> to the <code>ArrayVariant</code>.
	 */
	Index            arrayIndex;

	/**
	 * The flow graph node corresponding to this variant.  This node corresponds to the array
	 * creation site.
	 */
	FGNodeArray      node;

	/**
	 * Constructor of the class.
	 *
	 * @param classTokenArray the Array class this arrayVariant represents.
	 * @param arrayIndex the Index corresponding to this variant.
	 * @param node the flow graph node corresponding to this variant's creation site.
	 */
	public ArrayVariant(ClassTokenArray classTokenArray, Index arrayIndex,
						FGNodeArray node)
    {
		this.classTokenArray = classTokenArray;
		this.arrayIndex      = arrayIndex;
		this.node            = node;
    }

	/**
	 * Provides the flow graph node corresponding to new expression in which this variant was
	 * created.
	 *
	 * @return the flow graph node corresponding to this variant's creation site. 
	 */
	public FGNodeArray getNode()
    {
		return node;
    }
}
