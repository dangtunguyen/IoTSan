package edu.ksu.cis.bandera.smv;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
/**
 * A case of a SmvCaseExpr.
 */

public class SmvCase implements SmvExpr {

	SmvExpr cond;        // Condition
	SmvExpr expr;        // Result
	SmvCaseExpr parent;  // SmvCaseExpr we're contained in

	public SmvCase(SmvExpr cond, SmvExpr expr, SmvCaseExpr parent) {
	this.cond = cond;
	this.expr = expr;
	this.parent = parent;
	if (expr instanceof SmvCaseExpr) 
	    ((SmvCaseExpr)expr).outerCase = this;
	}
	public boolean isBig() { return cond.isBig() || expr.isBig(); }
	public void print(SmvTrans out) {
	if (isBig()) {
	    cond.print(out);
	    out.println();
	    out.print(": ");
	    out.indent(1);
	    expr.print(out);
	    out.print("; ");
	    out.indent(-1);
	} else {
	    cond.print(out);
	    out.print(" : ");
	    expr.print(out);
	    out.print("; ");
	}
	}
	public String toString() { 
	return cond.toString() + " => " + expr.toString();
	}
	/**
	 * Update the expr of this case.
	 */

	public void updateCase(SmvExpr expr) {
	this.expr = expr;
	if (expr instanceof SmvCaseExpr) 
	    ((SmvCaseExpr)expr).outerCase = this;
	}
}
