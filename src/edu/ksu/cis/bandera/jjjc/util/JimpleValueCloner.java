package edu.ksu.cis.bandera.jjjc.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import ca.mcgill.sable.util.*;

public class JimpleValueCloner extends AbstractJimpleValueSwitch {
	private static Jimple jimple = Jimple.v();
	private static JimpleValueCloner cloner = new JimpleValueCloner();
	private static JimpleBody body;
	private static String postfixName = "$clone";
/**
 * 
 */
private JimpleValueCloner() {
}
public void caseAddExpr(AddExpr v) {
	setResult(jimple.newAddExpr(JimpleValueCloner.clone(v.getOp1()),
			JimpleValueCloner.clone(v.getOp2())));
}
public void caseAndExpr(AndExpr v) {
	setResult(jimple.newAndExpr(JimpleValueCloner.clone(v.getOp1()),
			JimpleValueCloner.clone(v.getOp2())));
}
public void caseArrayRef(ArrayRef v) {
	setResult(jimple.newArrayRef(JimpleValueCloner.clone(v.getBase()),
			JimpleValueCloner.clone(v.getIndex())));
}
public void caseCastExpr(CastExpr v) {
	setResult(jimple.newCastExpr(JimpleValueCloner.clone(v.getOp()),
			v.getType()));
}
public void caseCaughtExceptionRef(CaughtExceptionRef v) {
	setResult(jimple.newCaughtExceptionRef(body));
}
public void caseCmpExpr(CmpExpr v) {
	setResult(jimple.newCmpExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseCmpgExpr(CmpgExpr v) {
	setResult(jimple.newCmpgExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseCmplExpr(CmplExpr v) {
	setResult(jimple.newCmplExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseDivExpr(DivExpr v) {
	setResult(jimple.newDivExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseDoubleConstant(DoubleConstant v) {
	setResult(DoubleConstant.v(v.value));
}
public void caseEqExpr(EqExpr v) {
	setResult(jimple.newEqExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseFloatConstant(FloatConstant v) {
	setResult(FloatConstant.v(v.value));
}
public void caseGeExpr(GeExpr v) {
	setResult(jimple.newGeExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseGtExpr(GtExpr v) {
	setResult(jimple.newGtExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseInstanceFieldRef(InstanceFieldRef v) {
	setResult(jimple.newInstanceFieldRef(JimpleValueCloner.clone(v.getBase()),
			v.getField()));
}
public void caseInstanceOfExpr(InstanceOfExpr v) {
	setResult(jimple.newInstanceOfExpr(JimpleValueCloner.clone(v.getOp()), v.getCheckType()));	
}
public void caseIntConstant(IntConstant v) {
	setResult(IntConstant.v(v.value));
}
public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
	LinkedList args = new LinkedList();

	for (int i = 0; i < v.getArgCount(); i++) {
		args.addLast(JimpleValueCloner.clone(v.getArg(i)));
	}
	
	setResult(jimple.newInterfaceInvokeExpr((Local) JimpleValueCloner.clone(v.getBase()), v.getMethod(), args));	
}
public void caseLeExpr(LeExpr v) {
	setResult(jimple.newLeExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseLengthExpr(LengthExpr v) {
	setResult(jimple.newLengthExpr(JimpleValueCloner.clone(v.getOp())));	
}
public void caseLocal(Local v) {
	String name = v.getName().trim() + postfixName;
	try {
		setResult(body.getLocal(name));
	} catch (NoSuchLocalException nsle) {
		Local lcl = jimple.newLocal(name, v.getType());
		body.addLocal(lcl);
		setResult(lcl);
	}
}
public void caseLongConstant(LongConstant v) {
	setResult(LongConstant.v(v.value));
}
public void caseLtExpr(LtExpr v) {
	setResult(jimple.newLtExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseMulExpr(MulExpr v) {
	setResult(jimple.newMulExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseNeExpr(NeExpr v) {
	setResult(jimple.newNeExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseNegExpr(NegExpr v) {
	setResult(jimple.newNegExpr(JimpleValueCloner.clone(v.getOp())));	
}
public void caseNewArrayExpr(NewArrayExpr v) {
	setResult(jimple.newNewArrayExpr(v.getBaseType(), JimpleValueCloner.clone(v.getSize())));	
}
public void caseNewExpr(NewExpr v) {
	setResult(jimple.newNewExpr(v.getBaseType()));
}
public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
	LinkedList sizes = new LinkedList();

	for (int i = 0; i < v.getSizeCount(); i++) {
		sizes.addLast(JimpleValueCloner.clone(v.getSize(i)));
	}

	setResult(jimple.newNewMultiArrayExpr(v.getBaseType(), sizes));
}
public void caseNullConstant(NullConstant v) {
	setResult(NullConstant.v());
}
public void caseOrExpr(OrExpr v) {
	setResult(jimple.newOrExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseParameterRef(ParameterRef v) {
	setResult(jimple.newParameterRef(v.getMethod(), v.getIndex()));
}
public void caseRemExpr(RemExpr v) {
	setResult(jimple.newRemExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseShlExpr(ShlExpr v) {
	setResult(jimple.newShlExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseShrExpr(ShrExpr v) {
	setResult(jimple.newShrExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
	LinkedList args = new LinkedList();

	for (int i = 0; i < v.getArgCount(); i++) {
		args.addLast(JimpleValueCloner.clone(v.getArg(i)));
	}

	setResult(jimple.newSpecialInvokeExpr((Local) JimpleValueCloner.clone(v.getBase()), v.getMethod(), args));
}
public void caseStaticFieldRef(StaticFieldRef v) {
	setResult(jimple.newStaticFieldRef(v.getField()));
}
public void caseStaticInvokeExpr(StaticInvokeExpr v) {
	LinkedList args = new LinkedList();

	for (int i = 0; i < v.getArgCount(); i++) {
		args.addLast(JimpleValueCloner.clone(v.getArg(i)));
	}

	setResult(jimple.newStaticInvokeExpr(v.getMethod(), args));
}
public void caseStringConstant(StringConstant v) {
	setResult(StringConstant.v(v.value));
}
public void caseSubExpr(SubExpr v) {
	setResult(jimple.newSubExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseThisRef(ThisRef v) {
	setResult(jimple.newThisRef(body.getMethod().getDeclaringClass()));
}
public void caseUshrExpr(UshrExpr v) {
	setResult(jimple.newUshrExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
	LinkedList args = new LinkedList();

	for (int i = 0; i < v.getArgCount(); i++) {
		args.addLast(JimpleValueCloner.clone(v.getArg(i)));
	}
	
	setResult(jimple.newVirtualInvokeExpr((Local) JimpleValueCloner.clone(v.getBase()), v.getMethod(), args));	
}
public void caseXorExpr(XorExpr v) {
	setResult(jimple.newXorExpr(JimpleValueCloner.clone(v.getOp1()), JimpleValueCloner.clone(v.getOp2())));	
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 * @param value ca.mcgill.sable.soot.jimple.Value
 */
public static Value clone(Value value) {
	value.apply(cloner);
	return (Value) cloner.getResult();
}
	public void defaultCase(Object v)
	{
		setResult(null);
	}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public static void setBody(JimpleBody b) {
	body = b;
}
/**
 * 
 * @param name java.lang.String
 */
public void setPostfixName(String name) {
	postfixName = name;
}
}
