package edu.ksu.cis.bandera.specification;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2002   Robby (robby@cis.ksu.edu)                    *
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.grimp.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
public class QuantifiedVariableFixer extends AbstractBanderaValueSwitch {
	private static Grimp grimp = Grimp.v();
	private static QuantifiedVariableFixer qvf = new QuantifiedVariableFixer();
	private QuantifiedVariable qv;
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.ComplementExpr
 */
public void caseComplementExpr(ComplementExpr expr) {
	expr.getOp().apply(this);
	setResult(new ComplementExpr((Value) getResult()));
}
public void caseEqExpr(EqExpr v) {
	setResult(grimp.newEqExpr(grimp.newStaticFieldRef(CompilationManager.getFieldForQuantifier("quantification$" + qv.getName())), grimp.newStaticFieldRef(CompilationManager.getFieldForQuantifier(((Local) v.getOp2()).getName()))));
}
public void caseInstanceOfExpr(InstanceOfExpr v) {
	setResult(grimp.newInstanceOfExpr(grimp.newStaticFieldRef(CompilationManager.getFieldForQuantifier("quantification$" + qv.getName())), v.getCheckType()));
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalAndExpr
 */
public void caseLogicalAndExpr(LogicalAndExpr expr) {
	expr.getOp1().apply(this);
	Value lValue = (Value) getResult();
	expr.getOp2().apply(this);
	Value rValue = (Value) getResult();
	setResult(new LogicalAndExpr(lValue, rValue));
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalOrExpr
 */
public void caseLogicalOrExpr(LogicalOrExpr expr) {
	expr.getOp1().apply(this);
	Value lValue = (Value) getResult();
	expr.getOp2().apply(this);
	Value rValue = (Value) getResult();
	setResult(new LogicalOrExpr(lValue, rValue));
}
/**
 * 
 * @param v java.lang.Object
 */
public void defaultCase(Object v) {
	throw new RuntimeException("Unexpected value: " + v);
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 * @param qv edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable
 */
public static Value fix(QuantifiedVariable qv) {
	qvf.qv = qv;
	qv.getConstraint().apply(qvf);
	qvf.qv = null;
	Value result = (Value) qvf.getResult();
	qvf.setResult(null);
	return result;
}
}
