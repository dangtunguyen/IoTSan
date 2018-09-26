package edu.ksu.cis.bandera.abstraction;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;
import edu.ksu.cis.bandera.jjjc.*;
public class PredicateTransformer extends AbstractBanderaValueSwitch {
	private static Grimp grimp = Grimp.v();
	private boolean foundComplement;
/**
 * 
 */
private PredicateTransformer() {}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.ComplementExpr
 */
public void caseComplementExpr(ComplementExpr expr) {
	foundComplement = !foundComplement;
	expr.getOp().apply(this);
}
public void caseEqExpr(EqExpr v) {
	if (foundComplement)
		setResult(grimp.newNeExpr(v.getOp1(), v.getOp2()));
	else
		defaultCase(v);
}
public void caseGeExpr(GeExpr v) {
	if (foundComplement)
		setResult(grimp.newLtExpr(v.getOp1(), v.getOp2()));
	else
		defaultCase(v);
}
public void caseGtExpr(GtExpr v) {
	if (foundComplement)
		setResult(grimp.newLeExpr(v.getOp1(), v.getOp2()));
	else
		defaultCase(v);
}
public void caseLeExpr(LeExpr v) {
	if (foundComplement)
		setResult(grimp.newGtExpr(v.getOp1(), v.getOp2()));
	else
		defaultCase(v);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalAndExpr
 */
public void caseLogicalAndExpr(LogicalAndExpr expr) {
	boolean foundComplement = this.foundComplement;
	expr.getOp1().apply(this);
	Value op1 = (Value) getResult();
	this.foundComplement = foundComplement;
	expr.getOp2().apply(this);
	Value op2 = (Value) getResult();
	if (foundComplement) {
        Value r = new LogicalOrExpr(op1, op2);
        setResult(r);
        if (CompilationManager.hasLocPredicatePair(expr))
        {
            CompilationManager.addLocPredicatePair(r);
        }
	} else {
		expr.setOp1(op1);
		expr.setOp2(op2);
		setResult(expr);
	}
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalOrExpr
 */
public void caseLogicalOrExpr(LogicalOrExpr expr) {
	boolean foundComplement = this.foundComplement;
	expr.getOp1().apply(this);
	Value op1 = (Value) getResult();
	this.foundComplement = foundComplement;
	expr.getOp2().apply(this);
	Value op2 = (Value) getResult();
	if (foundComplement) {
        Value r = new LogicalAndExpr(op1, op2);
        setResult(r);
        if (CompilationManager.hasLocPredicatePair(expr))
        {
            CompilationManager.addLocPredicatePair(r);
        }
	} else {
		expr.setOp1(op1);
		expr.setOp2(op2);
		setResult(expr);
	}
}
public void caseLtExpr(LtExpr v) {
	if (foundComplement)
		setResult(grimp.newGeExpr(v.getOp1(), v.getOp2()));
	else
		defaultCase(v);
}
public void caseNeExpr(NeExpr v) {
	if (foundComplement)
		setResult(grimp.newEqExpr(v.getOp1(), v.getOp2()));
	else
		defaultCase(v);
}
/**
 * 
 * @param o java.lang.Object
 */
public void defaultCase(Object o) {
	if (foundComplement)
    {
		setResult(new ComplementExpr((Value) o));
    }
	else
		setResult(o);
}
/**
 * 
 */
public static Value negate(Value v) {
	return pushComplement(new ComplementExpr(v));
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 */
public static Value pushComplement(Value v) {
	PredicateTransformer pt = new PredicateTransformer();
	v.apply(pt);
	return (Value) pt.getResult();
}
}
