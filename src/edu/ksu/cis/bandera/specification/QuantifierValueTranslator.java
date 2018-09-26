package edu.ksu.cis.bandera.specification;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2002   Roby Joehanes (robbyjo@cis.ksu.edu)          *
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

import java.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

import edu.ksu.cis.bandera.jext.*;

/**
 * @author Roby Joehanes
 */
public class QuantifierValueTranslator extends AbstractBanderaValueSwitch {
    private static QuantifierValueTranslator walker = new QuantifierValueTranslator();
    private StringBuffer buf;

    private QuantifierValueTranslator() {}

    public static String translate(Value val)
    {
        walker.buf = new StringBuffer();
        val.apply(walker);
        return walker.buf.toString();
    }

	/**
	 * @see edu.ksu.cis.bandera.jext.BanderaExprSwitch#caseLogicalAndExpr(LogicalAndExpr)
	 */
	public void caseLogicalAndExpr(LogicalAndExpr expr) {
        buf.append("(");
        expr.getOp1().apply(this);
        buf.append(" && ");
        expr.getOp2().apply(this);
        buf.append(")");
	}

	/**
	 * @see edu.ksu.cis.bandera.jext.BanderaExprSwitch#caseLogicalOrExpr(LogicalOrExpr)
	 */
	public void caseLogicalOrExpr(LogicalOrExpr expr) {
        buf.append("(");
		expr.getOp1().apply(this);
        buf.append(" || ");
        expr.getOp2().apply(this);
        buf.append(")");
	}

	/**
	 * @see ca.mcgill.sable.soot.jimple.ExprSwitch#caseInstanceOfExpr(InstanceOfExpr)
	 */
	public void caseInstanceOfExpr(InstanceOfExpr v) {
        v.getOp().apply(this);
        // If you modify this to include array, then
        // You must change the typeString and prepend the/
        // L with [
        Type type = v.getCheckType();
        String typeString = "L"+type.toString().replace('.','/')+";";
        buf.append(".instanceOf(\""+typeString+"\")");
	}

	/**
	 * @see edu.ksu.cis.bandera.jext.BanderaExprSwitch#caseComplementExpr(ComplementExpr)
	 */
	public void caseComplementExpr(ComplementExpr expr) {
        buf.append("(!");
        expr.getOp().apply(this);
        buf.append(")");
	}

	/**
	 * @see ca.mcgill.sable.soot.jimple.JimpleValueSwitch#caseLocal(Local)
	 */
	public void caseLocal(Local l) {
        buf.append(l.getName().trim().replace('@','_'));
	}

}
