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
 * An SMV case expression.
 */

public class SmvCaseExpr implements SmvExpr {

	Vector cases;
	SmvCase outerCase;    // outer case if nested inside another SmvCaseExpr

	public SmvCaseExpr() {
	cases = new Vector();
	}
	public void addCase(SmvExpr cond, SmvExpr value) {
	cases.addElement(new SmvCase(cond,value,this));
	}
	/**
	 * Collect all cases (leaves) into a vector.
	 */

	public Vector collectCases(Vector result) {
	for (int i = 0; i < cases.size(); i++) {
	    SmvCase smvCase = (SmvCase) cases.elementAt(i);
	    if (smvCase.expr instanceof SmvCaseExpr)
		((SmvCaseExpr)smvCase.expr).collectCases(result);
	    else if (smvCase.expr instanceof SmvVar)
		result.addElement(smvCase);
	    else
		throw new RuntimeException("Unknown case node: " + smvCase);
	}
	return result;
	}
	public Vector getCases() { return cases; }
	public boolean isBig() { return true; }
	public void print(SmvTrans out) {
	out.println();
	out.println("case ");
	out.indent(1);
	for (int i = 0; i < cases.size(); i++) {
	    SmvCase smvCase = (SmvCase) cases.elementAt(i);
	    smvCase.print(out);
	    out.println();
	}
	out.indent(-1);
	out.print("esac");
	}
}
