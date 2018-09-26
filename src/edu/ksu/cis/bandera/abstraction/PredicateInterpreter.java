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
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import java.util.*;
public class PredicateInterpreter extends AbstractBanderaValueSwitch {
	private TypeTable typeTable;
	private Vector variables;
	private int[] tokens;
	private int result;
public void caseAddExpr(AddExpr v) {
	doOp(v, "add", false);
}
public void caseAndExpr(AndExpr v) {
	doOp(v, "and", false);
}
public void caseArrayRef(ArrayRef v) {
	result = tokens[variables.indexOf(v)];
}
public void caseDivExpr(DivExpr v) {
	doOp(v, "div", false);
}
public void caseDoubleConstant(DoubleConstant v) {
	String aName = typeTable.get(v).getClass().getName();
	result = 1 << ((Integer) AbstractionClassLoader.invokeMethod(aName, "abs", new Class[] {double.class}, null, new Object[] {new Double(v.value)})).intValue();
}
public void caseEqExpr(EqExpr v) {
	doOp(v, "eq", true);
}
public void caseFloatConstant(FloatConstant v) {
	String aName = typeTable.get(v).getClass().getName();
	result = 1 << ((Integer) AbstractionClassLoader.invokeMethod(aName, "abs", new Class[] {double.class}, null, new Object[] {new Double(v.value)})).intValue();
}
public void caseGeExpr(GeExpr v) {
	doOp(v, "ge", true);
}
public void caseGtExpr(GtExpr v) {
	doOp(v, "gt", true);
}
public void caseInstanceFieldRef(InstanceFieldRef v) {
	result = tokens[variables.indexOf(v)];
}
public void caseIntConstant(IntConstant v) {
	Abstraction a = typeTable.get(v);
	String aName = a.getClass().getName();
	if (a instanceof RealAbstraction) {
		result = 1 << ((Integer) AbstractionClassLoader.invokeMethod(aName, "abs", new Class[] {double.class}, null, new Object[] {new Double(v.value)})).intValue();
	} else if (a instanceof IntegralAbstraction) {
		result = 1 << ((Integer) AbstractionClassLoader.invokeMethod(aName, "abs", new Class[] {long.class}, null, new Object[] {new Long(v.value)})).intValue();
	} else {
		throw new RuntimeException("Error in predicate interpreter");
	}
}
public void caseLeExpr(LeExpr v) {
	doOp(v, "le", true);
}
public void caseLengthExpr(LengthExpr v) {
	result = tokens[variables.indexOf(v)];
}
/**
 * 
 * @param v edu.ksu.cis.bandera.jext.LocalExpr
 */
public void caseLocalExpr(LocalExpr v) {
	result = tokens[variables.indexOf(v)];
}
public void caseLongConstant(LongConstant v) {
	Abstraction a = typeTable.get(v);
	String aName = a.getClass().getName();
	if (a instanceof RealAbstraction) {
		result = 1 << ((Integer) AbstractionClassLoader.invokeMethod(aName, "abs", new Class[] {double.class}, null, new Object[] {new Double(v.value)})).intValue();
	} else if (a instanceof IntegralAbstraction) {
		result = 1 << ((Integer) AbstractionClassLoader.invokeMethod(aName, "abs", new Class[] {long.class}, null, new Object[] {new Long(v.value)})).intValue();
	} else {
		throw new RuntimeException("Error in predicate interpreter");
	}
}
public void caseLtExpr(LtExpr v) {
	doOp(v, "lt", true);
}
public void caseMulExpr(MulExpr v) {
	doOp(v, "mul", false);
}
public void caseNeExpr(NeExpr v) {
	doOp(v, "ne", true);
}
public void caseNegExpr(NegExpr v) {
}
public void caseOrExpr(OrExpr v) {
	doOp(v, "or", false);
}
public void caseRemExpr(RemExpr v) {
	doOp(v, "rem", false);
}
public void caseShlExpr(ShlExpr v) {
	doOp(v, "shl", false);
}
public void caseShrExpr(ShrExpr v) {
	doOp(v, "shr", false);
}
public void caseStaticFieldRef(StaticFieldRef v) {
	result = tokens[variables.indexOf(v)];
}
public void caseSubExpr(SubExpr v) {
	doOp(v, "sub", false);
}
public void caseUshrExpr(UshrExpr v) {
	doOp(v, "ushr", false);
}
public void caseXorExpr(XorExpr v) {
	doOp(v, "xor", false);
}
/**
 * 
 * @param o java.lang.Object
 */
public void defaultCase(Object o) {
	throw new RuntimeException(o + " is not handled by the predicate interpreter");
}
/**
 * 
 * @param v ca.mcgill.sable.soot.jimple.BinopExpr
 * @param methodName java.lang.String
 */
private void doOp(BinopExpr v, String methodName, boolean isTest) {
	Abstraction a = typeTable.get(v);
	v.getOp1().apply(this);
	int v1 = result;
	v.getOp2().apply(this);
	int v2 = result;
	try {
		if (isTest) {
			result = ((Byte) AbstractionClassLoader.invokeMethod(a.getClass().getName(), methodName + "Set", new Class[] {int.class, int.class}, null, new Object[] {new Integer(v1), new Integer(v2)})).intValue();
		} else {
			result = ((Integer) AbstractionClassLoader.invokeMethod(a.getClass().getName(), methodName + "Set", new Class[] {int.class, int.class}, null, new Object[] {new Integer(v1), new Integer(v2)})).intValue();
		}
		if (result == -1 || (!isTest && (result == 0))) {
			throw new Exception(v + " give result an invalid token set");
		}
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage());
	}
}
/**
 * 
 * @return boolean
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 * @param variables java.util.Vector
 * @param tokens int[]
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public boolean isFalse(TypeTable typeTable, Vector variables, int[] tokens, Value v) {
	this.typeTable = typeTable;
	this.variables = variables;
	this.tokens = tokens;
	v.apply(this);
	return result == Abstraction.FALSE;
}
/**
 * 
 * @return boolean
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 * @param variables java.util.Vector
 * @param tokens int[]
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public boolean isTop(TypeTable typeTable, Vector variables, int[] tokens, Value v) {
	this.typeTable = typeTable;
	this.variables = variables;
	this.tokens = tokens;
	v.apply(this);
	return result == Abstraction.TRUE_OR_FALSE;
}
/**
 * 
 * @return boolean
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public boolean isTrue(TypeTable typeTable, Vector variables, int[] tokens, Value v) {
	this.typeTable = typeTable;
	this.variables = variables;
	this.tokens = tokens;
	v.apply(this);
	return result == Abstraction.TRUE;
}
}
