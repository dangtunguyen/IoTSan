/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000, 2001, 2002                        *
 * John Hatcliff (hatcliff@cis.ksu.edu)                              *
 * Venkatesh Prasad Ranganath (rvprasad@cis.ksu.edu)                 *
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

/*
 * ArraryIndexManager.java
 * $Id: ArrayIndexManager.java,v 1.2 2002/02/21 07:42:20 rvprasad Exp $
 */

/**
 * This is the interface to be provided by any implementation of ArrayIndexManager.  An
 * ArrayIndexManager will manage the ArrayIndex which are used to get to the {@link
 * edu.ksu.cis.bandera.bofa.ArrayVariant#ArrayVariant ArrayVariant}.
 *
 * The ArrayIndexManager will drive the analysis regarding how information about the array objects
 * will be collected.
 *
 * @author <a href="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</a>
 * @author  <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.2 $)
 */
public interface ArrayIndexManager
{
	/**
	 * Returns an index corresponding to the given ValueVariant object.
	 *
	 * @param valueVariant the value variant for which the ArrayIndex is required.
	 * @return the index corresponding to the provided value variant.
	 */
	public Index select(ValueVariant valueVariant);

    /**
	 * Reset the data structures in the Manager.
	 *
	 */
	public void reset();
}
