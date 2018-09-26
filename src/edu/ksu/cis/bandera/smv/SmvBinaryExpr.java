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
 * A binary SMV expression.
 */

public class SmvBinaryExpr implements SmvExpr {

	String operator;
	SmvExpr op1;
	SmvExpr op2;
	boolean big;

	public SmvBinaryExpr(String operator, SmvExpr op1, SmvExpr op2) {
	this.operator = operator;
	this.op1 = op1;
	this.op2 = op2;
	big = (operator.equals("&") || operator.equals("|") 
	       || operator.equals("->"));
	}
	public boolean isBig() { return big || op1.isBig() || op2.isBig(); }
	public void print(SmvTrans out) {
	out.print("( ");
	out.indent(1);
	op1.print(out);
	if (big) {
	    out.indent(-1);
	    out.println();
	    out.println(operator + " ");
	    out.indent(1);
	} else {
	    out.print(" " + operator + " ");
	}
	op2.print(out);
	out.indent(-1);
	out.print(" )");
	}
}
