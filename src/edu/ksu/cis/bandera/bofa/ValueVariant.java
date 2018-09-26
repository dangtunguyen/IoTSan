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

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.soot.jimple.Value;

/*
 * ValueVariant.java
 * $Id: ValueVariant.java,v 1.2 2002/02/21 07:42:24 rvprasad Exp $
 */

/**
 * This class represents the values that may flow in the flow graph.
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class ValueVariant
{
	/**
	 * The class of the value variant.  The type in simple words.
	 */
	private ClassToken classToken;

	/**
	 * Index associated with the value variant.
	 */
	private Index      valueIndex;

	/**
	 * Expression in which the value variant gets created.
	 */
	private Value expr;

	/**
	 * Statement in which the value variant gets created.
	 */
	private Stmt stmt;

	/**
	 * Constructor of the class.
	 *
	 * @param classToken the class token associated with the enclosing class.
	 * @param valueIndex the associated index
	 * @param expr a <code>Value</code> value
	 * @param stmt the statement in which the value variant was created.
	 */
	public ValueVariant(ClassToken classToken, Index valueIndex, Value expr,
						Stmt stmt)
    {
		this.classToken = classToken;
		this.valueIndex = valueIndex;
		this.expr = expr;
		this.stmt = stmt;
    }

	/**
	 * Provides the class in which the variant was created.
	 *
	 * @return the class token corresponding to the class in which the variant was created.
	 */
	public ClassToken getClassToken()
    {
		return classToken;
    }

	/**
	 * Provides the associated index.
	 *
	 * @return the associated index.
	 */
	public Index getIndex()
    {
		return valueIndex;
    }

	/**
	 * Provides the statement in which the variant was created.
	 *
	 * @return the statement in which the variant was created.
	 */
	public Stmt getStmt()
	{
		return stmt;
	}

	/**
	 * Provides the statement in which the variant was created.
	 *
	 * @return the statement in which the variant was created.
	 */
	public Value getExpr()
	{
		return expr;
	}

	public String toString() {
		return "[" + expr + ":" + classToken + "]@" + stmt;
	}

}
