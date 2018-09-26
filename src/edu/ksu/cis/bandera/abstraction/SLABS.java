package edu.ksu.cis.bandera.abstraction;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import java.util.Enumeration;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jext.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;
import java.util.Hashtable;
public class SLABS extends IRNodes {
	private static final Jimple jimple = Jimple.v();
	private static final String prefix = "SLABS$";
	private SootClassManager scm;
	private Vector messages = new Vector();
	private AnnotationManager am;
	private TypeTable typeTable;
	private Annotation methodAnnotation;
	private JimpleBody methodBody;
	private StmtList methodStmtList;
	private Stmt currentStmt;
	private Value currentValue;
	private Abstraction coercedAbstraction;
	private int tempCounter;
	private Hashtable interfaceMethodMethod;
/**
 * 
 * @param annotationManager edu.ksu.cis.bandera.annotation.AnnotationManager
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 * @param interfaceMethodMethod java.util.Hashtable
 */
public SLABS(AnnotationManager annotationManager, TypeTable typeTable, Hashtable interfaceMethodMethod) {
	am = annotationManager;
	this.typeTable = typeTable;
	this.interfaceMethodMethod = interfaceMethodMethod;
}
/**
 *
 * @return java.util.Vector
 * @param classes java.util.Vector
 */
public Vector abstractClasses(java.util.Vector classes) {
	for (java.util.Iterator i = classes.iterator(); i.hasNext();) {
		SootClass sc = (SootClass) i.next();
		if (scm == null)
			scm = sc.getManager();
		for (Iterator j = sc.getFields().iterator(); j.hasNext();) {
			SootField sf = (SootField) j.next();
			Abstraction a = typeTable.get(sf);
			if (((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction))
					|| ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction))) {
				sf.setType(IntType.v());
			}
		}
		for (Iterator j = sc.getMethods().iterator(); j.hasNext();) {
			SootMethod sm = (SootMethod) j.next();
			tempCounter = 0;
			methodBody = (JimpleBody) sm.getBody(jimple);
			for (Iterator k = methodBody.getLocals().iterator(); k.hasNext();) {
				Local lcl = (Local) k.next();
				Abstraction a = typeTable.get(lcl);
				if (((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction))
						|| ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction))) {
					lcl.setType(IntType.v());
				}
			}
			methodStmtList = methodBody.getStmtList();
			methodAnnotation = am.getAnnotation(sc, sm);
			Object[] stmts = methodStmtList.toArray();
			for (int k = 0; k < stmts.length; k++) {
				currentValue = null;
				currentStmt = (Stmt) stmts[k];
				currentStmt.apply(this);
			}
		}
	}
	for (Enumeration e = interfaceMethodMethod.keys(); e.hasMoreElements();) {
		SootMethod key = (SootMethod) e.nextElement();
		SootMethod value = (SootMethod) interfaceMethodMethod.get(key);
		key.setParameterTypes(new LinkedList(value.getParameterTypes()));
		key.setReturnType(value.getReturnType());
	}
	return messages;
}
public void caseAddExpr(AddExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("add", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("add", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseAndExpr(AndExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("and", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("and", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseArrayRef(ArrayRef v) {
	defaultCase(v);
}
public void caseAssignStmt(AssignStmt stmt) {
	coercedAbstraction = (Abstraction) typeTable.get(stmt.getLeftOp());
	stmt.getRightOp().apply(this);
	if (currentValue != null)
		stmt.setRightOp(currentValue);
}
public void caseBreakpointStmt(BreakpointStmt stmt) {
	defaultCase(stmt);
}
public void caseCastExpr(CastExpr v) {
	v.getOp().apply(this);
}
public void caseCaughtExceptionRef(CaughtExceptionRef v) {
	defaultCase(v);
}
public void caseChooseExpr(ChooseExpr v) {
	defaultCase(v);
}
public void caseCmpExpr(CmpExpr v) {
	defaultCase(v);
}
public void caseCmpgExpr(CmpgExpr v) {
	defaultCase(v);
}
public void caseCmplExpr(CmplExpr v) {
	defaultCase(v);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.ComplementExpr
 */
public void caseComplementExpr(ComplementExpr expr) {
	defaultCase(expr);
}
public void caseDivExpr(DivExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("div", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("div", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseDoubleConstant(DoubleConstant v) {
	coerce(v);
}
public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
	defaultCase(stmt);
}
public void caseEqExpr(EqExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("eq", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("eq", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
	defaultCase(stmt);
}
public void caseFloatConstant(FloatConstant v) {
	coerce(v);
}
public void caseGeExpr(GeExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("ge", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("ge", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseGotoStmt(GotoStmt stmt) {
	defaultCase(stmt);
}
public void caseGtExpr(GtExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("gt", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("gt", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseIdentityStmt(IdentityStmt stmt) {
	if (stmt.getRightOp() instanceof ParameterRef) {
		Abstraction a = typeTable.get(stmt.getLeftOp());
		if (((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction))
				|| ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction))) {
			List l = methodBody.getMethod().getParameterTypes();
			int index = ((ParameterRef) stmt.getRightOp()).getIndex();
			l.add(index, IntType.v());
			l.remove(index + 1);
		}
	}
}
public void caseIfStmt(IfStmt stmt) {
	stmt.getCondition().apply(this);
	if (currentValue != null) {
		if (!(currentValue instanceof ConditionExpr)) {
			currentValue = jimple.newNeExpr(makeLocal(currentValue, IntType.v()), IntConstant.v(0));
			typeTable.put(currentValue, ConcreteIntegralAbstraction.v());
		}
		stmt.setCondition(currentValue);
	}
}
/**
 * This method was created in VisualAge.
 * @param v edu.ksu.cis.bandera.jext.InExpr
 */
public void caseInExpr(InExpr v) {
	defaultCase(v);
}
public void caseInstanceFieldRef(InstanceFieldRef v) {
	coerce(v);
}
public void caseInstanceOfExpr(InstanceOfExpr v) {
	defaultCase(v);
}
public void caseIntConstant(IntConstant v) {
	coerce(v);
}
public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
	defaultCase(v);
}
public void caseInvokeStmt(InvokeStmt stmt) {
	stmt.getInvokeExpr().apply(this);
}
public void caseLeExpr(LeExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("le", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("le", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseLengthExpr(LengthExpr v) {
	defaultCase(v);
}
public void caseLocal(Local v) {
	coerce(v);
}
/**
 * 
 * @param v edu.ksu.cis.bandera.jext.LocalExpr
 */
public void caseLocalExpr(LocalExpr v) {
	defaultCase(v);
}
/**
 * 
 * @param e edu.ksu.cis.bandera.jext.LocationTestExpr
 */
public void caseLocationTestExpr(LocationTestExpr e) {
	defaultCase(e);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalAndExpr
 */
public void caseLogicalAndExpr(LogicalAndExpr expr) {
	defaultCase(expr);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalOrExpr
 */
public void caseLogicalOrExpr(LogicalOrExpr expr) {
	defaultCase(expr);
}
public void caseLongConstant(LongConstant v) {
	coerce(v);
}
public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
	Abstraction a = typeTable.get(stmt.getKey());
	if (a instanceof ConcreteIntegralAbstraction) return;
	int tokens = ((Integer) AbstractionClassLoader.invokeMethod(a.getClass().getName(), "getNumOfTokens", new Class[0], null, new Object[0])).intValue();
	Vector[] cases = new Vector[tokens];
	Vector[] caseConstants = new Vector[tokens];
	for (int i = 0; i < tokens; i++) {
		cases[i] = new Vector();
		caseConstants[i] = new Vector();
	}
	int k = 0;
	for (ca.mcgill.sable.util.Iterator i = stmt.getLookupValues().iterator(); i.hasNext(); k++) {
		int caseConstant = ((Integer) i.next()).intValue();
		int token	= ((Integer) AbstractionClassLoader.invokeMethod(a.getClass().getName(), "abs", new Class[] {long.class}, null, new Object[] {new Long(caseConstant)})).intValue();
		cases[token].add(stmt.getTarget(k));
		caseConstants[token].add(new Integer(caseConstant));
	}
	
	Vector newStmts = new Vector();
	ca.mcgill.sable.util.LinkedList newLookupValues = new ca.mcgill.sable.util.LinkedList();
	Unit[] newTargets = new Unit[tokens];
	
	for (int i = 0; i < tokens; i++) {
		int newCaseIdx = newStmts.size();
		if ((cases[i].size() == 0) || !((Boolean) AbstractionClassLoader.invokeMethod(a.getClass().getName(), "isOne2One", new Class[] {int.class}, null, new Object[] {new Integer(i)})).booleanValue()) {
			cases[i].add(stmt.getDefaultTarget());
		}
		int size = cases[i].size() - 1;
		for (int j = 0; j < size; j++) {
			Local lcl = jimple.newLocal(prefix + tempCounter++, IntType.v());
			methodBody.addLocal(lcl);
			typeTable.put(lcl, ConcreteIntegralAbstraction.v());
			newStmts.add(jimple.newAssignStmt(lcl, jimple.newStaticInvokeExpr(scm.getClass("edu.ksu.cis.bandera.abstraction.Abstraction").getMethod("choose", new LinkedList()), new LinkedList())));
			newStmts.add(jimple.newIfStmt(jimple.newEqExpr(lcl, IntConstant.v(1)), stmt.getTarget(stmt.getLookupValues().indexOf(caseConstants[i].elementAt(j)))));
		}
		newStmts.add(jimple.newGotoStmt((Stmt) cases[i].lastElement()));
		newLookupValues.add(new Integer(i));
		newTargets[i] = (Unit) newStmts.elementAt(newCaseIdx);
	}

	Annotation ann = null;
	try {
		ann = am.getContainingAnnotation(methodBody.getMethod().getDeclaringClass(), methodBody.getMethod(), currentStmt);
	} catch (Exception e) {
		System.out.println("Cannot get annotation for statement: " + currentStmt);
	}

	int index = methodStmtList.indexOf(currentStmt) + 1;
	for (java.util.Iterator i = newStmts.iterator(); i.hasNext(); index++) {
		Stmt s = (Stmt) i.next();
		methodStmtList.add(index, s);
		if (ann != null) ann.add(s);
	}
	
	stmt.setLookupValues(newLookupValues);
	stmt.setTargets(newTargets);
}
public void caseLtExpr(LtExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("lt", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("lt", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseMulExpr(MulExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("mul", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("mul", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseNeExpr(NeExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("ne", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("ne", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseNegExpr(NegExpr v) {
	if (v.getType() instanceof LongType) {
		Value c = LongConstant.v(0);
		typeTable.put(c, ConcreteIntegralAbstraction.v());
		Value v2 = jimple.newSubExpr(c, v.getOp());
		typeTable.put(v2, typeTable.get(v));
		v2.apply(this);
	} else if (v.getType() instanceof FloatType) {
		Value c = FloatConstant.v(0);
		typeTable.put(c, ConcreteRealAbstraction.v());
		Value v2 = jimple.newSubExpr(c, v.getOp());
		typeTable.put(v2, typeTable.get(v));
		v2.apply(this);
	} else if (v.getType() instanceof DoubleType) {
		Value c = DoubleConstant.v(0);
		typeTable.put(c, ConcreteRealAbstraction.v());
		Value v2 = jimple.newSubExpr(c, v.getOp());
		typeTable.put(v2, typeTable.get(v));
		v2.apply(this);
	} else {
		Value c = IntConstant.v(0);
		typeTable.put(c, ConcreteIntegralAbstraction.v());
		Value v2 = jimple.newSubExpr(c, v.getOp());
		typeTable.put(v2, typeTable.get(v));
		v2.apply(this);
	}
}
public void caseNewArrayExpr(NewArrayExpr v) {
	defaultCase(v);
}
public void caseNewExpr(NewExpr v) {
	defaultCase(v);
}
public void caseNewInvokeExpr(NewInvokeExpr v) {
	defaultCase(v);
}
public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
	defaultCase(v);
}
public void caseNopStmt(NopStmt stmt) {
	defaultCase(stmt);
}
public void caseNullConstant(NullConstant v) {
	defaultCase(v);
}
public void caseOrExpr(OrExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("or", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("or", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseParameterRef(ParameterRef v) {
	defaultCase(v);
}
public void caseRemExpr(RemExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("rem", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("rem", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseRetStmt(RetStmt stmt) {
	defaultCase(stmt);
}
public void caseReturnStmt(ReturnStmt stmt) {
	Abstraction a = typeTable.get(stmt.getReturnValue());
	if (((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction))
			|| ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction))) {
		methodBody.getMethod().setReturnType(IntType.v());
	}
}
public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
	defaultCase(stmt);
}
public void caseShlExpr(ShlExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("shl", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("shl", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseShrExpr(ShrExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("shr", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("shr", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
	doInvokeExpr(v);
}
public void caseStaticFieldRef(StaticFieldRef v) {
	coerce(v);
}
public void caseStaticInvokeExpr(StaticInvokeExpr v) {
	doInvokeExpr(v);
}
public void caseStringConstant(StringConstant v) {
	defaultCase(v);
}
public void caseSubExpr(SubExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("sub", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("sub", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseTableSwitchStmt(TableSwitchStmt stmt) {
	defaultCase(stmt);
}
public void caseThisRef(ThisRef v) {
	defaultCase(v);
}
public void caseThrowStmt(ThrowStmt stmt) {
	defaultCase(stmt);
}
public void caseUshrExpr(UshrExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("ushr", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("ushr", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
	doInvokeExpr(v);
}
public void caseXorExpr(XorExpr v) {
	Abstraction a = (Abstraction) typeTable.get(v);
	if ((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction)) {
		coercedAbstraction = a;
		doOp("xor", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else if ((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) {
		coercedAbstraction = a;
		doOp("xor", v.getOp1(), v.getOp2());
		coercedAbstraction = ConcreteIntegralAbstraction.v();
	} else {
		currentValue = v;
	}
}
/**
 * 
 * @param abstraction edu.ksu.cis.bandera.abstraction.Abstraction
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
private boolean coerce(Value v) {
	if ((coercedAbstraction instanceof ConcreteIntegralAbstraction) || (coercedAbstraction instanceof ConcreteRealAbstraction)){
		currentValue = v;
		return false;
	}
	if (typeTable.get(v) instanceof ConcreteIntegralAbstraction) {
		if (coercedAbstraction instanceof RealAbstraction) {
			if (v instanceof IntConstant) {
				currentValue = IntConstant.v(((Integer) AbstractionClassLoader.invokeMethod(coercedAbstraction.getClass().getName(), "abs", new Class[] {double.class}, null, new Object[] {new Double(((IntConstant) v).value)})).intValue());
			} else if (v instanceof LongConstant) {
				currentValue = IntConstant.v(((Integer) AbstractionClassLoader.invokeMethod(coercedAbstraction.getClass().getName(), "abs", new Class[] {double.class}, null, new Object[] {new Double(((LongConstant) v).value)})).intValue());
			} else {
				LinkedList params = new LinkedList();
				params.add(LongType.v());
				LinkedList args = new LinkedList();
				args.add(makeLocal(v, LongType.v()));
				currentValue = jimple.newStaticInvokeExpr(scm.getClass(coercedAbstraction.getClass().getName()).getMethod("abs", params), args);
			}
		} else {
			if (v instanceof IntConstant) {
				currentValue = IntConstant.v(((Integer) AbstractionClassLoader.invokeMethod(coercedAbstraction.getClass().getName(), "abs", new Class[] {long.class}, null, new Object[] {new Long(((IntConstant) v).value)})).intValue());
			} else if (v instanceof LongConstant) {
				currentValue = IntConstant.v(((Integer) AbstractionClassLoader.invokeMethod(coercedAbstraction.getClass().getName(), "abs", new Class[] {long.class}, null, new Object[] {new Long(((LongConstant) v).value)})).intValue());
			} else {
				LinkedList params = new LinkedList();
				params.add(LongType.v());
				LinkedList args = new LinkedList();
				args.add(makeLocal(v, LongType.v()));
				currentValue = jimple.newStaticInvokeExpr(scm.getClass(coercedAbstraction.getClass().getName()).getMethod("abs", params), args);
			}
		}
		typeTable.put(currentValue, coercedAbstraction);
		return true;
	} else if (typeTable.get(v) instanceof ConcreteRealAbstraction) {
		if (v instanceof FloatConstant) {
			currentValue = IntConstant.v(((Integer) AbstractionClassLoader.invokeMethod(coercedAbstraction.getClass().getName(), "abs", new Class[] {double.class}, null, new Object[] {new Double(((FloatConstant) v).value)})).intValue());
		} else if (v instanceof DoubleConstant) {
			currentValue = IntConstant.v(((Integer) AbstractionClassLoader.invokeMethod(coercedAbstraction.getClass().getName(), "abs", new Class[] {double.class}, null, new Object[] {new Double(((DoubleConstant) v).value)})).intValue());
		} else {
			LinkedList params = new LinkedList();
			params.add(DoubleType.v());
			LinkedList args = new LinkedList();
			args.add(makeLocal(v, DoubleType.v()));
			currentValue = jimple.newStaticInvokeExpr(scm.getClass(coercedAbstraction.getClass().getName()).getMethod("abs", params), args);
		}
		typeTable.put(currentValue, coercedAbstraction);
		return true;
	} else {
		currentValue = v;
		return false;
	}
}
/**
 * 
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public void defaultCase(Object v) {
	if (v instanceof Value) {
		currentValue = (Value) v;
	}
}
/**
 * 
 * @param v ca.mcgill.sable.soot.jimple.InvokeExpr
 */
private void doInvokeExpr(InvokeExpr v) {
	SootMethod sm = v.getMethod();
	if (sm.isBodyStored(jimple)) {
		Value[] parameterLocals = getParameterLocals(sm);
		for (int i = 0; i < v.getArgCount(); i++) {
			coercedAbstraction = typeTable.get(parameterLocals[i]);
			v.getArg(i).apply(this);
			v.setArg(i, makeLocal(currentValue, currentValue.getType()));
		}
	}
	currentValue = v;
}
/**
 *
 * @param op1 ca.mcgill.sable.soot.jimple.Value
 * @param op2 ca.mcgill.sable.soot.jimple.Value
 */
private void doOp(String methodName, Value op1, Value op2) {
	Abstraction coercedAbstraction = this.coercedAbstraction;
	Object op1Type = typeTable.get(op1);
	if ((op1Type instanceof ConcreteIntegralAbstraction) || (op1Type instanceof ConcreteRealAbstraction)) {
		if (coerce(op1)) op1 = makeLocal(currentValue, IntType.v());
	}
	Object op2Type = typeTable.get(op2);
	if ((op2Type instanceof ConcreteIntegralAbstraction) || (op2Type instanceof ConcreteRealAbstraction)) {
		if (coerce(op2)) op2 = makeLocal(currentValue, IntType.v());
	}
	LinkedList params = new LinkedList();
	params.add(IntType.v());
	params.add(IntType.v());
	LinkedList args = new LinkedList();
	args.add(op1);
	args.add(op2);
	currentValue = jimple.newStaticInvokeExpr(scm.getClass(coercedAbstraction.getClass().getName()).getMethod(methodName, params), args);
	typeTable.put(currentValue, coercedAbstraction);
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value[]
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private Value[] getParameterLocals(SootMethod sm) {
	Value[] result = new Value[sm.getParameterCount()];
	int k = 0;
	for (Iterator i = ((JimpleBody) sm.getBody(jimple)).getStmtList().iterator(); i.hasNext();) {
		Stmt s = (Stmt) i.next();
		if (s instanceof IdentityStmt) {
			IdentityStmt is = (IdentityStmt) s;
			if (is.getRightOp() instanceof ParameterRef) {
				result[k++] = is.getLeftOp();
			}
		} else break;
	}
	return result;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Local
 * @param v ca.mcgill.sable.soot.jimple.Value
 * @param t ca.mcgill.sable.soot.Type
 */
private Value makeLocal(Value v, Type t) {
	if (v == null)
		return v;
	if ((v instanceof Local) || (v instanceof Constant))
		return v;
	Local lcl = jimple.newLocal(prefix + tempCounter++, t);
	typeTable.put(lcl, typeTable.get(v));
	Stmt newStmt = jimple.newAssignStmt(lcl, v);
	methodBody.addLocal(lcl);
	int index = methodStmtList.indexOf(currentStmt);
	methodStmtList.add(index, newStmt);
	methodBody.redirectJumps(currentStmt, newStmt);
	try {
		am.getContainingAnnotation(methodBody.getMethod().getDeclaringClass(), methodBody.getMethod(), currentStmt).insertStmtBefore(newStmt, currentStmt);
	} catch (Exception e) {
		System.out.println("Cannot get annotation for statement: " + currentStmt);
	}
	return lcl;
}
}
