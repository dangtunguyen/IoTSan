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
public class PredicateTypeChecker extends AbstractBanderaValueSwitch {
	private CoercionManager cm;
	private TypeTable typeTable;
	private Vector messages = new Vector();
	private Hashtable variableSetTable;
/**
 * 
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 */
private PredicateTypeChecker(CoercionManager cm, TypeTable typeTable, Hashtable varSetTable) {
	this.cm = cm;
	this.typeTable = typeTable;
	variableSetTable = varSetTable;
}
public void caseAddExpr(AddExpr v) {
	doBinop(v);
}
public void caseAndExpr(AndExpr v) {
	doBinop(v);
}
public void caseArrayRef(ArrayRef v) {
	v.getBase().apply(this);
	typeTable.put(v, ((ArrayAbstraction) typeTable.get(v.getBase())).getIndexAbstraction());
	HashSet variableSet = new HashSet(2);
	variableSet.add(v);
	variableSetTable.put(v, variableSet);
}
public void caseCastExpr(CastExpr v) {
	v.getOp().apply(this);
	typeTable.put(v, typeTable.get(v.getOp()));
	HashSet variableSet = new HashSet();
	variableSet.addAll((Set) variableSetTable.get(v.getOp()));
	variableSetTable.put(v, variableSet);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.ComplementExpr
 */
public void caseComplementExpr(ComplementExpr expr) {
	expr.getOp().apply(this);
	HashSet variableSet = new HashSet();
	variableSet.addAll((Set) variableSetTable.get(expr.getOp()));
	variableSetTable.put(expr, variableSet);
}
public void caseDivExpr(DivExpr v) {
	doBinop(v);
}
public void caseDoubleConstant(DoubleConstant v) {
	typeTable.put(v, ConcreteRealAbstraction.v());
	variableSetTable.put(v, new HashSet(0));
}
public void caseEqExpr(EqExpr v) {
	doBinop(v);
}
public void caseFloatConstant(FloatConstant v) {
	typeTable.put(v, ConcreteRealAbstraction.v());
	variableSetTable.put(v, new HashSet(0));
}
public void caseGeExpr(GeExpr v) {
	doBinop(v);
}
public void caseGtExpr(GtExpr v) {
	doBinop(v);
}
public void caseInstanceFieldRef(InstanceFieldRef v) {
	v.getBase().apply(this);
	typeTable.put(v, typeTable.get(v.getField()));
	HashSet variableSet = new HashSet();
	variableSet.add(v);
	variableSet.addAll((Set) variableSetTable.get(v.getBase()));
	variableSetTable.put(v, variableSet);
}
public void caseInstanceOfExpr(InstanceOfExpr v) {
	v.getOp().apply(this);
	typeTable.put(v, ConcreteIntegralAbstraction.v());
	HashSet variableSet = new HashSet();
	variableSet.addAll((Set) variableSetTable.get(v.getOp()));
	variableSetTable.put(v, variableSet);
}
public void caseIntConstant(IntConstant v) {
	typeTable.put(v, ConcreteIntegralAbstraction.v());
	variableSetTable.put(v, new HashSet(0));
}
public void caseLeExpr(LeExpr v) {
	doBinop(v);
}
public void caseLengthExpr(LengthExpr v) {
	v.getOp().apply(this);
	typeTable.put(v, ((ArrayAbstraction) typeTable.get(v.getOp())).getIndexAbstraction());
	HashSet variableSet = new HashSet();
	variableSet.add(v);
	variableSet.addAll((Set) variableSetTable.get(v.getOp()));
	variableSetTable.put(v, variableSet);
}
/**
 * 
 * @param v edu.ksu.cis.bandera.jext.LocalExpr
 */
public void caseLocalExpr(LocalExpr v) {
	typeTable.put(v, typeTable.get(v.getLocal()));
	HashSet variableSet = new HashSet(2);
	variableSet.add(v);
	variableSetTable.put(v, variableSet);
}
/**
 * 
 * @param e edu.ksu.cis.bandera.jext.LocationTestExpr
 */
public void caseLocationTestExpr(LocationTestExpr e) {
	variableSetTable.put(e, new HashSet());
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalAndExpr
 */
public void caseLogicalAndExpr(LogicalAndExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	HashSet variableSet = new HashSet();
	variableSet.addAll((Set) variableSetTable.get(expr.getOp1()));
	variableSet.addAll((Set) variableSetTable.get(expr.getOp2()));
	variableSetTable.put(expr, variableSet);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalOrExpr
 */
public void caseLogicalOrExpr(LogicalOrExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	HashSet variableSet = new HashSet();
	variableSet.addAll((Set) variableSetTable.get(expr.getOp1()));
	variableSet.addAll((Set) variableSetTable.get(expr.getOp2()));
	variableSetTable.put(expr, variableSet);
}
public void caseLongConstant(LongConstant v) {
	typeTable.put(v, ConcreteIntegralAbstraction.v());
	variableSetTable.put(v, new HashSet(0));
}
public void caseLtExpr(LtExpr v) {
	doBinop(v);
}
public void caseMulExpr(MulExpr v) {
	doBinop(v);
}
public void caseNeExpr(NeExpr v) {
	doBinop(v);
}
public void caseNegExpr(NegExpr v) {
	v.getOp().apply(this);
	typeTable.put(v, typeTable.get(v.getOp()));
	HashSet variableSet = new HashSet();
	variableSet.addAll((Set) variableSetTable.get(v.getOp()));
	variableSetTable.put(v, variableSet);
}
public void caseNullConstant(NullConstant v) {
	typeTable.put(v, ClassAbstraction.v("java.lang.Object"));
	variableSetTable.put(v, new HashSet(0));
}
public void caseOrExpr(OrExpr v) {
	doBinop(v);
}
public void caseRemExpr(RemExpr v) {
	doBinop(v);
}
public void caseShlExpr(ShlExpr v) {
	doBinop(v);
}
public void caseShrExpr(ShrExpr v) {
	doBinop(v);
}
public void caseStaticFieldRef(StaticFieldRef v) {
	/*
	if (v.getField().getName().startsWith("loc$"))
	{
		typeTable.put(v.getField(), ConcreteIntegralAbstraction.v());
	}
	*/
	typeTable.put(v, typeTable.get(v.getField()));
	HashSet variableSet = new HashSet(2);
	variableSet.add(v);
	variableSetTable.put(v, variableSet);
}
public void caseStringConstant(StringConstant v) {
	typeTable.put(v, ClassAbstraction.v("java.lang.String"));
	variableSetTable.put(v, new HashSet(0));
}
public void caseSubExpr(SubExpr v) {
	doBinop(v);
}
public void caseUshrExpr(UshrExpr v) {
	doBinop(v);
}
public void caseXorExpr(XorExpr v) {
	doBinop(v);
}
/**
 * 
 * @param cm edu.ksu.cis.bandera.abstraction.CoercionManager
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 * @param predicate ca.mcgill.soot.sable.jimple
 * @param varSetTable java.util.Hashtable
 */
public static Vector check(CoercionManager cm, TypeTable typeTable, Value predicate, Hashtable varSetTable) {
	PredicateTypeChecker ptc = new PredicateTypeChecker(cm, typeTable, varSetTable);
	predicate.apply(ptc);
	return ptc.messages;
}
/**
 * 
 * @param v ca.mcgill.sable.soot.jimple.BinopExpr
 */
private void doBinop(BinopExpr v) {
	// robbyjo's patch begin
	Value v1 = v.getOp1();
	Value v2 = v.getOp2();
	Type v1type = v1.getType();
	v1.apply(this);
	if (v2 instanceof NullConstant)
	{
		if (v1type instanceof RefType)
		{
			typeTable.put(v2, ClassAbstraction.v(((RefType) v1type).className));
			variableSetTable.put(v2, new HashSet(0));
		} else if (v1type instanceof ArrayType)
		{
//			typeTable.put(v2, ArrayAbstraction.v(ClassAbstraction.v()));
//			variableSetTable.put(v2, new HashSet(0));
			v2.apply(this);
		} else v2.apply(this);
		
	} else v2.apply(this);
	// robbyjo's patch end
//	v.getOp1().apply(this);
//	v.getOp2().apply(this);
	HashSet types = new HashSet();
	types.add(typeTable.get(v.getOp1()));
	types.add(typeTable.get(v.getOp2()));
	Abstraction a = cm.lub(types);
	if (a != null) 
		typeTable.put(v, a);
	else
		messages.add("Type mismatch for " + v);
	if ((a instanceof ClassAbstraction) || (a instanceof ArrayAbstraction)) {
		
	} else if (((a != typeTable.get(v.getOp1())) && !(v.getOp1() instanceof Constant))
			|| ((a != typeTable.get(v.getOp2())) && !(v.getOp2() instanceof Constant))) {
		throw new RuntimeException("Type mismatch for " + v + "\nNote: Cannot coerce a variable in a predicate");
	}
	typeTable.put(v.getOp1(), a);
	typeTable.put(v.getOp2(), a);
	HashSet variableSet = new HashSet();
	variableSet.addAll((Set) variableSetTable.get(v.getOp1()));
	variableSet.addAll((Set) variableSetTable.get(v.getOp2()));
	variableSetTable.put(v, variableSet);
}
}
