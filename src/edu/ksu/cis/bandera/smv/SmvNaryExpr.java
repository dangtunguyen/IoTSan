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
import java.util.Vector;

/**
 * An N-ary expression.
 * <p>
 * Although we could nest binary expressions, the output is much more
 * readable if long conjunctions and disjunctions are indented as units.
 */

public class SmvNaryExpr implements SmvExpr {

	String operator;
	SmvExpr identity;
	boolean skip;
	Vector elements;

	public SmvNaryExpr(String operator, SmvExpr identity, boolean skip) {
	this.operator = operator;
	this.identity = identity;
	this.skip = skip;
	elements = new Vector();
	}
	public void add(SmvExpr expr) {
	elements.addElement(expr);
	}
	public boolean isBig() { return true; }
	public void print(SmvTrans out) {
	if (elements.size() == 0) 
	    identity.print(out);
	else if (elements.size() == 1) 
	    ((SmvExpr)elements.firstElement()).print(out);
	else {
	    out.print("( ");
	    out.indent(1);
	    ((SmvExpr)elements.firstElement()).print(out);
	    for (int i = 1; i < elements.size(); i++) {
		out.println();
		out.indent(-1);
		out.println(operator + " ");
		out.indent(1);
		((SmvExpr)elements.elementAt(i)).print(out);
	    }
	    out.indent(-1);
	    out.print(" )");
	}	    	
	}
	public int size() { return elements.size(); }
}
