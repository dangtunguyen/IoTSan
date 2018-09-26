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

import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.util.*;

/*
 * FGNodeAST.java
 * $Id: FGNodeAST.java,v 1.3 2002/03/20 02:54:05 rvprasad Exp $
 */

/**
 * This class represents the FG node for any AST nodes.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.3 $)
 */
public class FGNodeAST extends FGNode
{
	/**
	 * The AST object described by this node.
	 */
	private Object obj;

	/**
	 * The statement in which the described AST Node is embedded in.
	 */
	private Stmt stmt;

	/**
	 * Constructor that uses empty sets for values, actions, and succs.
	 * @param obj AST object described by this flowgraph node.
	 */
	public FGNodeAST(Object obj)
    {
		super();
		this.obj = obj;
    }

	/**
	 * Returns the AST object described by this node.
	 *
	 * @return the AST object described by this node.
	 */
	public Object getObj()
    {
		return obj;
    }

	/**
	 * Returns the statement enclosing the described AST node.
	 *
	 * @return the statement enclosing the described AST node.
	 */
	public Stmt getStmt()
	{
		return stmt;
	}

	/**
	 * Sets the statement enclosing the described AST node.  This is required when there are
	 * circularity in constructors and so members need to be initialized after the call to the
	 * construtor.
	 *
	 * @param stmt the statement enclosing the described AST node.
	 */
	void setStmt(Stmt stmt)
	{
		this.stmt = stmt;
	}

}
