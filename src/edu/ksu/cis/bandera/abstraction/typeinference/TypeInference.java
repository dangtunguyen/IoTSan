package edu.ksu.cis.bandera.abstraction.typeinference;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
 * All rights reserved.                                              *
 *                                                                   *
 * Modifications by Robby (robby@cis.ksu.edu) are                    *
 * Copyright (C) 2000 Robby.  All rights reserved.                   *
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
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.gui.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import org.apache.log4j.Category;
public class TypeInference extends IRNodes {
    private static final Category log = Category.getInstance(TypeInference.class);
	private static final Jimple jimple = Jimple.v();
	private CoercionManager coercionManager;
	private TypeTable nodes;
	private List constraints;
	private List params;
	private LeftAssignExprSwitch lswitch;
	private TypeStructure returnValue;
	private Hashtable method2params = new Hashtable();
	private Hashtable method2return = new Hashtable();
	private Hashtable local2method = new Hashtable();
	private SootMethod currentMethod;
	private RefHashTable ast2TypeStruct = new RefHashTable();
	private TypeDependencyGraph tdg = new TypeDependencyGraph();
	private Hashtable errors = new Hashtable();
	private HashSet methodsInheritance = new HashSet();
	private Hashtable interfaceMethodMethod = new Hashtable();
	private Vector conflicts = new Vector();
/**
 * Insert the method's description here.
 * Creation date: (1/14/00 1:31:44 PM)
 */
public TypeInference() {
	nodes = new TypeTable();
	constraints = new Vector();
	params = new Vector();
	this.coercionManager = new CoercionManager(AbstractionLibraryManager.getAbstractions());
}
public void caseAddExpr(AddExpr v) {
	doOp(v);
}
public void caseAndExpr(AndExpr v) {
	doOp(v);
}
public void caseArrayRef(ArrayRef v) {
	TypeStructure varnode = getTypeStructure(v);
	ArrayTypeStructure var = (ArrayTypeStructure) getTypeStructure(v.getBase());
	TypeStructure index = var.getIndex();
	v.getIndex().apply(this);
	constraints.addAll(index.genEqualConstraints((TypeStructure) getResult()));
	TypeStructure base = var.getElements();
	constraints.addAll(varnode.genEqualConstraints(base));
	TypeStructure ts = getCoercedTypeStructure(v);
	constraints.addAll(ts.genCoerceConstraints(varnode));
	setResult(varnode);
}
public void caseAssignStmt(AssignStmt stmt) {
	doDefinitionStmt(stmt);
}
/**
 * This method was created in VisualAge.
 * @param v ca.mcgill.sable.soot.jimple.CastExpr
 */
public void caseCastExpr(CastExpr v) {
	v.getOp().apply(this);
	TypeStructure opts = (TypeStructure) getResult();
	TypeStructure ts = getTypeStructure(v);
	if (v.getType() instanceof BaseType)
		constraints.addAll(ts.genEqualConstraints(opts));
	setResult(ts);
}
/**
 * Insert the method's description here.
 * Creation date: (5/23/00 9:31:52 AM)
 * @param v ca.mcgill.sable.soot.jimple.CaughtExceptionRef
 */
public void caseCaughtExceptionRef(CaughtExceptionRef v) {
	TypeStructure ts = getTypeStructure(v);
	setResult(ts);
}
/**
 * Insert the method's description here.
 * Creation date: (5/31/00 1:48:07 PM)
 * @param v edu.ksu.cis.bandera.jext.ChooseExpr
 */
public void caseChooseExpr(ChooseExpr v) {
	BaseTypeStructure ts = (BaseTypeStructure) getTypeStructure(v);
	Abstraction t = ConcreteIntegralAbstraction.v();
	tdg.addType(t);
	tdg.combine(tdg.getTypeVariable(t), ts.getVar());
	setResult(ts);
}
public void caseCmpExpr(CmpExpr v) {
	doOp(v);
}
public void caseCmpgExpr(CmpgExpr v) {
	doOp(v);
}
public void caseCmplExpr(CmplExpr v) {
	doOp(v);
}
public void caseDivExpr(DivExpr v) {
	doOp(v);
}
public void caseDoubleConstant(DoubleConstant v) {
	TypeStructure cts = getCoercedTypeStructure(v);
	TypeStructure ts = getTypeStructure(v);
	constraints.addAll(cts.genCoerceConstraints(ts));
	setResult(cts);
}
public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
	stmt.getOp().apply(this);
	setResult(null);
}
public void caseEqExpr(EqExpr v) {
	doTest(v);
}
public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
	stmt.getOp().apply(this);
	setResult(null);
}
public void caseFloatConstant(FloatConstant v) {
	TypeStructure cts = getCoercedTypeStructure(v);
	TypeStructure ts = getTypeStructure(v);
	constraints.addAll(cts.genCoerceConstraints(ts));
	setResult(cts);
}
public void caseGeExpr(GeExpr v) {
	doTest(v);
}
public void caseGotoStmt(GotoStmt stmt) {
	setResult(null);
}
public void caseGtExpr(GtExpr v) {
	doTest(v);
}
public void caseIdentityStmt(IdentityStmt stmt) {
	doDefinitionStmt(stmt);
}
public void caseIfStmt(IfStmt stmt) {
	stmt.getCondition().apply(this);
}
/**
 * This method was created in VisualAge.
 * @param v ca.mcgill.sable.soot.jimple.InstanceFieldRef
 */
public void caseInstanceFieldRef(InstanceFieldRef v) {
	doFieldRef(v);
}
public void caseIntConstant(IntConstant v) {
	TypeStructure cts = getCoercedTypeStructure(v);
	TypeStructure ts = getTypeStructure(v);
	constraints.addAll(cts.genCoerceConstraints(ts));
	setResult(cts);
}
/**
 * 
 * @param v ca.mcgill.sable.soot.jimple.InterfaceInvokeExpr
 */
public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
	SootMethod sm = v.getMethod();
	v.setMethod((SootMethod) interfaceMethodMethod.get(sm));
	doInvokeExpr(v);
	v.setMethod(sm);
}
/**
 * Insert the method's description here.
 * Creation date: (5/25/00 2:25:00 PM)
 * @param s ca.mcgill.sable.soot.jimple.InvokeStmt
 */
public void caseInvokeStmt(InvokeStmt s) {
	s.getInvokeExpr().apply(this);
}
public void caseLeExpr(LeExpr v) {
	doTest(v);
}
public void caseLengthExpr(LengthExpr v) {
	ArrayTypeStructure var = (ArrayTypeStructure) getTypeStructure(v.getOp());
	TypeStructure varnode = getTypeStructure(v);
	constraints.addAll(varnode.genEqualConstraints(var.getIndex()));
	setResult(varnode);
}
public void caseLocal(Local v) {
	local2method.put(v, currentMethod);
	TypeStructure node = getCoercedTypeStructure(v);
	TypeStructure varnode = getTypeStructure(v);
	constraints.addAll(node.genCoerceConstraints(varnode));
	setResult(node);
}
public void caseLongConstant(LongConstant v) {
	TypeStructure cts = getCoercedTypeStructure(v);
	TypeStructure ts = getTypeStructure(v);
	constraints.addAll(cts.genCoerceConstraints(ts));
	setResult(cts);
}
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.LookupSwitchStmt
 */
public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
	stmt.getKey().apply(this);
}
public void caseLtExpr(LtExpr v) {
	doTest(v);
}
public void caseMulExpr(MulExpr v) {
	doOp(v);
}
public void caseNeExpr(NeExpr v) {
	doTest(v);
}
public void caseNegExpr(NegExpr v) {
	v.getOp().apply(this);
	TypeStructure op = (TypeStructure) getResult();
	TypeStructure varnode = getTypeStructure(v);
	constraints.addAll(varnode.genEqualConstraints(op));
	setResult(varnode);
}
/**
 * This method was created in VisualAge.
 * @param v ca.mcgill.sable.soot.jimple.NewArrayExpr
 */
public void caseNewArrayExpr(NewArrayExpr v) {
	ArrayTypeStructure node = (ArrayTypeStructure) getTypeStructure(v);
	v.getSize().apply(this);
	TypeStructure index = node.getIndex();
	constraints.addAll(index.genEqualConstraints((TypeStructure) getResult()));
	setResult(node);
}
/**
 * This method was created in VisualAge.
 * @param v ca.mcgill.sable.soot.jimple.NewExpr
 */
public void caseNewExpr(NewExpr v) {
	TypeStructure varnode = getTypeStructure(v);
	setResult(varnode);
}
public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
	defaultCase(v);
}
public void caseNopStmt(NopStmt stmt) {
	setResult(null);
}
public void caseNullConstant(NullConstant v) {
	setResult(getTypeStructure(NullConstant.v()));
}
public void caseOrExpr(OrExpr v) {
	doOp(v);
}
public void caseParameterRef(ParameterRef v) {
	SootMethod method = v.getMethod();
	List l;
	if ((l = (Vector) method2params.get(method)) == null) {
		l = new Vector();
		method2params.put(method, l);
	}
	if (!l.contains(v))
		l.add(v);
	TypeStructure varnode = getTypeStructure(v);
	constraints.addAll(varnode.genEqualConstraints((TypeStructure) params.get(v.getIndex() + 1)));
	setResult(varnode);
}
public void caseRemExpr(RemExpr v) {
	doOp(v);
}
public void caseReturnStmt(ReturnStmt stmt) {
	if (returnValue != null) {
		stmt.getReturnValue().apply(this);
		constraints.addAll(returnValue.genCoerceConstraints((TypeStructure) getResult()));
	}
	setResult(null);
}
/**
 * Insert the method's description here.
 * Creation date: (5/24/00 2:58:48 PM)
 * @param s ca.mcgill.sable.soot.jimple.ReturnVoidStmt
 */
public void caseReturnVoidStmt(ReturnVoidStmt s) {
	setResult(null);
}
public void caseShlExpr(ShlExpr v) {
	doOp(v);
}
public void caseShrExpr(ShrExpr v) {
	doOp(v);
}
/**
 * This method was created in VisualAge.
 * @param v ca.mcgill.sable.soot.jimple.VirtualInvokeExpr
 */
public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
	doInvokeExpr(v);
}
/**
 * Insert the method's description here.
 * Creation date: (1/19/00 10:46:59 PM)
 * @param v ca.mcgill.sable.soot.jimple.StaticFieldRef
 */
public void caseStaticFieldRef(StaticFieldRef v) {
	doFieldRef(v);
}
/**
 * This method was created in VisualAge.
 * @param v ca.mcgill.sable.soot.jimple.StaticInvokeExpr
 */
public void caseStaticInvokeExpr(StaticInvokeExpr v) {
	doInvokeExpr(v);
}
public void caseStringConstant(StringConstant v) {
	TypeStructure varnode = getTypeStructure(v);
	setResult(varnode);
}
public void caseSubExpr(SubExpr v) {
	doOp(v);
}
public void caseThisRef(ThisRef v) {
	TypeStructure ts = getTypeStructure(v);
	setResult(ts);
}
public void caseThrowStmt(ThrowStmt stmt) {
	setResult(null);
}
public void caseUshrExpr(UshrExpr v) {
	doOp(v);
}
/**
 * This method was created in VisualAge.
 * @param v ca.mcgill.sable.soot.jimple.VirtualInvokeExpr
 */
public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
	SootMethod sm = v.getMethod();
	if (interfaceMethodMethod.get(sm) != null) {
		v.setMethod((SootMethod) interfaceMethodMethod.get(sm));
	}
	doInvokeExpr(v);
	v.setMethod(sm);
}
public void caseXorExpr(XorExpr v) {
	doOp(v);
}
/**
 * Insert the method's description here.
 * Creation date: (1/14/00 12:05:12 PM)
 * @param method ca.mcgill.sable.soot.SootMethod
 * @param params ca.mcgill.sable.util.List
 * @param vars ca.mcgill.sable.util.Map
 */
public void createConstraints(SootMethod method, List params, TypeStructure ret) {
    //System.out.print(".");
	if (method2params.get(method) != null) {
		List p = (List) method2params.get(method);
		Iterator i = p.iterator();
		while (i.hasNext()) {
			ParameterRef param = (ParameterRef) i.next();
			TypeStructure varnode = getTypeStructure(param);
			constraints.addAll(varnode.genEqualConstraints((TypeStructure) params.get(param.getIndex() + 1)));
		}
		if ((ret != null) && !(method.getReturnType() instanceof VoidType))
			constraints.addAll(ret.genEqualConstraints((TypeStructure) method2return.get(method)));
		return;
	}
	if ((ret != null) && !(method.getReturnType() instanceof VoidType))
		method2return.put(method, ret);
	SootMethod oldMethod = currentMethod;
	currentMethod = method;
	lswitch = new LeftAssignExprSwitch(constraints, this, null, null);
	TypeStructure oldRet = returnValue;
	returnValue = ret;
	this.params = params;
	try {   // robbyjo's patch
		JimpleBody body = (JimpleBody) method.getBody(Jimple.v());
		StmtList stmts = body.getStmtList();
		ca.mcgill.sable.util.Iterator i = stmts.iterator();
		Stmt current;
		while (i.hasNext()) {
			current = (Stmt) i.next();
			current.apply(this);
		}
	} catch(Exception e)  // robbyjo's patch
	{
	}
	returnValue = oldRet;
	currentMethod = oldMethod;
}
/**
 * 
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private void createInheritanceConstraints(SootMethod sm) {
	if (methodsInheritance.contains(sm) || !sm.isBodyStored(jimple) || sm.isStatic() || "<init>".equals(sm.getName()) || "<clinit>".equals(sm.getName())) return;
	HashSet overwrittenMethods = new HashSet();
	SootClass sc = sm.getDeclaringClass();
	ca.mcgill.sable.util.List parmTypes = sm.getParameterTypes();
	String name = sm.getName();
	SootMethod origSootMethod = sm;
	do {
		overwrittenMethods.add(sm);
		sm = null;
		while (sc.hasSuperClass() && (sm == null)) {
			for (ca.mcgill.sable.util.Iterator i = sc.getInterfaces().iterator(); i.hasNext();) {
				SootClass scInterface = (SootClass) i.next();
				if (scInterface.declaresMethod(name, parmTypes)) {
					interfaceMethodMethod.put(scInterface.getMethod(name, parmTypes), sc.getMethod(name, parmTypes));
				}
			}
			sc = sc.getSuperClass();
			if (sc.declaresMethod(name, parmTypes))	{
				sm = sc.getMethod(name, parmTypes);
				if (Modifier.isAbstract(sm.getModifiers())) {
					interfaceMethodMethod.put(sm, origSootMethod);
					sm = null;
				} else if (!sm.isBodyStored(jimple) || sm.isStatic() || "<init>".equals(sm.getName()) || "<clinit>".equals(sm.getName())) sm = null;
			} else sm = null;
		}
	} while (sm != null && !methodsInheritance.contains(sm));
	if (sm != null) overwrittenMethods.add(sm);

	methodsInheritance.addAll(overwrittenMethods);
	Iterator i = overwrittenMethods.iterator();
	sm = (SootMethod) i.next();
	TypeStructure[] args = new TypeStructure[sm.getParameterCount()];
	TypeStructure ret = createInheritanceConstraintsMethod(sm, args);
	while (i.hasNext()) {
		sm = (SootMethod) i.next();
		TypeStructure[] tempArgs = new TypeStructure[sm.getParameterCount()];
		TypeStructure tempRet = createInheritanceConstraintsMethod(sm, tempArgs);
		for (int j = 0; j < args.length; j++) {
			constraints.addAll(args[j].genEqualConstraints(tempArgs[j]));
		}
		if (ret != null)
			constraints.addAll(ret.genEqualConstraints(tempRet));
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.abps.typing.TypeStructure
 * @param sm ca.mcgill.sable.soot.SootMethod
 * @param args edu.ksu.cis.bandera.abps.typing.TypeStructure[]
 */
private TypeStructure createInheritanceConstraintsMethod(SootMethod sm, TypeStructure[] args) {
	Object[] stmts = ((JimpleBody) sm.getBody(jimple)).getStmtList().toArray();
	Vector returnTypeStructures = new Vector();

	for (int i = 0; i < stmts.length; i++) {
		if (stmts[i] instanceof ReturnStmt) {
			returnTypeStructures.add(getTypeStructure((((ReturnStmt) stmts[i]).getReturnValue())));
		} else if (stmts[i] instanceof IdentityStmt) {
			IdentityStmt is = (IdentityStmt) stmts[i];
			if (is.getRightOp() instanceof ParameterRef) {
				args[((ParameterRef) is.getRightOp()).getIndex()] = getTypeStructure(is.getLeftOp());
			}
		}
	}	

	if (returnTypeStructures.size() == 0) 
		return null;
		
	TypeStructure ts = (TypeStructure) returnTypeStructures.remove(0);
	for (Iterator i = returnTypeStructures.iterator(); i.hasNext();) {
		constraints.addAll(ts.genEqualConstraints((TypeStructure) i.next()));
	}
	method2return.put(sm, ts);
	return ts;
}
public void defaultCase(Object v) {
	System.out.println("Default Case:  " + v + "\t" + v.getClass());
	if (v instanceof Expr)
		throw new RuntimeException("Unhandled expression " + v + ":" + v.getClass() + " in type inference.");
	System.out.println("Unknown in type inference:  " + v.getClass());
}
/**
 * Insert the method's description here.
 * Creation date: (8/18/00 9:11:50 AM)
 * @param stmt ca.mcgill.sable.soot.jimple.DefinitionStmt
 */
public void doDefinitionStmt(DefinitionStmt stmt) {
	stmt.getLeftOp().apply(lswitch);
	TypeStructure left = (TypeStructure) lswitch.getResult();
	stmt.getRightOp().apply(this);
	TypeStructure right = (TypeStructure) getResult();
	constraints.addAll(left.genEqualConstraints(right));
}
/**
 * Insert the method's description here.
 * Creation date: (8/18/00 9:13:47 AM)
 * @param v ca.mcgill.sable.soot.jimple.FieldRef
 */
public void doFieldRef(FieldRef v) {
	TypeStructure fts = getTypeStructure(v.getField());
	TypeStructure ts = getTypeStructure(v);
	constraints.addAll(fts.genEqualConstraints(ts));
	setResult(ts);
}
/**
 * Insert the method's description here.
 * Creation date: (8/18/00 9:22:23 AM)
 * @param v ca.mcgill.sable.soot.jimple.InvokeExpr
 */
public void doInvokeExpr(InvokeExpr v) {
	TypeStructure varnode;
	if (v.getMethod().getReturnType() instanceof RefType) {
		varnode = getTypeStructure(v);
	} else
		if (!(v.getMethod().getReturnType() instanceof VoidType)) {
			varnode = getTypeStructure(v);
		} else {
			varnode = null;
		}
	if (!Util.hasJavaPrefix(v.getMethod().getDeclaringClass().toString()) && !"Bandera".equals(v.getMethod().getDeclaringClass().toString())) {
		Vector p = new Vector();
		if (v instanceof NonStaticInvokeExpr) {
			((NonStaticInvokeExpr) v).getBase().apply(this);
			p.add(getResult());
		} else {
			p.add(new Object());
		}
		for (int i = 0; i < v.getArgCount(); i++) {
			v.getArg(i).apply(this);
			p.add(getResult());
		}
		createConstraints(v.getMethod(), p, varnode);
	}
	setResult(varnode);
}
public void doOp(BinopExpr v) {
	v.getOp1().apply(this);
	TypeStructure left = (TypeStructure) getResult();
	v.getOp2().apply(this);
	TypeStructure right = (TypeStructure) getResult();
	TypeStructure ts = getTypeStructure(v);
	constraints.addAll(ts.genEqualConstraints(left));
	constraints.addAll(ts.genEqualConstraints(right));
	constraints.addAll(left.genEqualConstraints(right));
	setResult(ts);
}
/**
 * This method was created in VisualAge.
 * @param v ConditionalExpr
 */
public void doTest(ConditionExpr v) {
	v.getOp1().apply(this);
	TypeStructure left = (TypeStructure) getResult();
	v.getOp2().apply(this);
	TypeStructure right = (TypeStructure) getResult();
	//Type t = edu.ksu.cis.bandera.abps.lib.BooleanAbstraction.v().getType();
	//tdg.addType(t);
	TypeStructure ts = getTypeStructure(v);
	//constraints.addAll(getTypeStructure(t).genEqualConstraints(ts));
	constraints.addAll(left.genEqualConstraints(right));
	if ((left instanceof BaseTypeStructure)
			&& (right instanceof BaseTypeStructure)
			&& (ts instanceof BaseTypeStructure)) {
		constraints.addAll(left.genEqualConstraints(ts));
		constraints.addAll(right.genEqualConstraints(ts));
	}
	setResult(ts);
}
/**
 * 
 * @return edu.ksu.cis.bandera.abstraction.Abstraction
 * @param t ca.mcgill.sable.soot.Type
 */
public Abstraction getAbstraction(Type vType) {
	if (vType instanceof NullType) {
		return ClassAbstraction.v("java.lang.Object");
	} else if (vType instanceof ArrayType) {
		return ArrayAbstraction.v(getAbstraction(((ArrayType) vType).baseType), ConcreteIntegralAbstraction.v());
	} else if (vType instanceof RefType) {
		return ClassAbstraction.v(((RefType) vType).className);
	} else if ((vType instanceof FloatType) || (vType instanceof DoubleType)) {
		return ConcreteRealAbstraction.v();
	} else if (vType instanceof BaseType) {
		return ConcreteIntegralAbstraction.v();
	} else return null;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:13:15 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public TypeStructure getCoercedTypeStructure(Value v) {
	TypeStructure ats = getTypeStructure(v.getType());
	setAST(ats, "a(" + v + ")");
	return ats;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:13:15 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public TypeStructure getCoercedTypeStructure(SootField f) {
	TypeStructure ats = getTypeStructure(f.getType());
	setAST(ats, "a(" + f + ")");
	return ats;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getErrors() {
	return errors;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getInterfaceMethodMethod() {
	return interfaceMethodMethod;
}
/**
 * 
 * @return ca.mcgill.sable.soot.Type
 * @param ts edu.ksu.cis.bandera.abps.typing.TypeStructure
 */
public Object getType(TypeStructure ts) {
	Object t;
	if (ts instanceof ObjectTypeStructure) {
		t = tdg.getType(((ObjectTypeStructure) ts).getVar());
	} else
		if (ts instanceof BaseTypeStructure) {
			t = tdg.getType(((BaseTypeStructure) ts).getVar());
		} else {
			Abstraction components = (Abstraction) getType(((ArrayTypeStructure) ts).getElements());
			IntegralAbstraction index = (IntegralAbstraction) getType(((ArrayTypeStructure) ts).getIndex());
			t = ArrayAbstraction.v(components, index);
		}
	return t;
}
/**
 * Insert the method's description here.
 * Creation date: (8/18/00 10:10:53 AM)
 * @return ca.mcgill.sable.soot.Type
 * @param f ca.mcgill.sable.soot.SootField
 */
public Object getType(Object o) {
	if (o instanceof SootField) {
		return ((SootField) o).getType();
	} else
		if (o instanceof Value) {
			return ((Value) o).getType();
		} else
			System.out.println("Unknown type " + o);
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:13:15 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public TypeStructure getTypeStructure(Value v) {
	TypeStructure ats = (TypeStructure) ast2TypeStruct.get(v);
	if (ats == null) {
		ats = getTypeStructure(v.getType());
		ast2TypeStruct.put(v, ats);
		setAST(ats, v);
	}
	return ats;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:13:15 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public TypeStructure getTypeStructure(SootField f) {
	TypeStructure ats = (TypeStructure) ast2TypeStruct.get(f);
	if (ats == null) {
		ats = getTypeStructure(f.getType());
		ast2TypeStruct.put(f, ats);
		setAST(ats, f);
	}
	return ats;
}
/**
 * Insert the method's description here.
 * Creation date: (8/21/00 3:32:04 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeStructure
 * @param t ca.mcgill.sable.soot.Type
 */
public TypeStructure getTypeStructure(TypeVariable tv) {
	TypeStructure ats;
	if (tv.getType() instanceof ArrayType) {
		System.out.println("What am I doing here?");
		ats = null;
	} else
		if (tv.getType() instanceof RefType || tv.getType() instanceof NullType) {
			ats = new ObjectTypeStructure(tv);
			tdg.add(tv);
		} else
			if (tv.getType() instanceof ArrayAbstraction) {
				ats = new ObjectTypeStructure(tv);
				tdg.add(tv);
			} else {
				ats = new BaseTypeStructure(tv);
				tdg.add(tv);
			}
	return ats;
}
/**
 * Insert the method's description here.
 * Creation date: (8/21/00 3:32:04 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeStructure
 * @param t ca.mcgill.sable.soot.Type
 */
public TypeStructure getTypeStructure(Object t) {
	TypeStructure ats;
	if (t instanceof ArrayType) {
		ArrayType at = (ArrayType) t;
		TypeStructure index = getTypeStructure(ConcreteIntegralAbstraction.v());
		TypeStructure elements = getTypeStructure(at.baseType);
		ats = new ArrayTypeStructure(elements, index);
	} else
		if (t instanceof RefType || t instanceof NullType) {
			TypeVariable tv = new TypeVariable(t);
			ats = new ObjectTypeStructure(tv);
			tdg.add(tv);
		} else
			if (t instanceof ArrayAbstraction) {
				TypeVariable tv = new TypeVariable(t);
				ats = new ObjectTypeStructure(tv);
				tdg.add(tv);
			} else {
				TypeVariable tv = new TypeVariable(t);
				ats = new BaseTypeStructure(tv);
				tdg.add(tv);
			}
	return ats;
}
/**
 * Insert the method's description here.
 * Creation date: (8/22/00 3:53:25 PM)
 * @param ts edu.ksu.cis.bandera.abps.typing.TypeStructure
 * @param ast java.lang.Object
 */
public void setAST(TypeStructure ts, Object ast) {
	if (ts instanceof BaseTypeStructure)
		 ((BaseTypeStructure) ts).getVar().setAST(ast);
	else
		if (ts instanceof ObjectTypeStructure)
			 ((ObjectTypeStructure) ts).getVar().setAST(ast);
		else {
			ArrayTypeStructure ats = (ArrayTypeStructure) ts;
			setAST(ats.getIndex(), ast);
			setAST(ats.getElements(), ast);
		}
}
/**
 * 
 * @param newErrors java.util.Hashtable
 */
public void setErrors(java.util.Hashtable newErrors) {
	errors = newErrors;
}
public int solveCoercions() {
    //System.out.println("\tRemoving Cycles.");
	log.debug("\tRemoving Cycles.");
	tdg.removeCycles();
	//System.out.println("\tGet Connected.");
	log.debug("\tGet Connected.");
	Set connected = tdg.getNotSingleConnected();
	//System.out.println("Bubbling Data.");
	log.debug("Bubbling Data.");
	Iterator i = connected.iterator();
	while (i.hasNext()) {
		boolean types = false;
		Vector q = new Vector();
		Set vars = new HashSet();
		Set roots = (Set) i.next();
		q.addAll(roots);
		TypeVariable o;
		while (q.size() > 0 && !types) {
			o = (TypeVariable) q.remove(0);
			if (!vars.contains(o)) {
				vars.add(o);
				if (tdg.getType(o) != null) {
					types = true;
				} else
					q.addAll(tdg.getChildren(o));
			}
		}
		if (!types) {
			if (vars.size() > 0) {
				Object vType = ((TypeVariable) vars.toArray()[0]).getType();
				if (vType instanceof RefType) {
					Object t = ClassAbstraction.v(((RefType) vType).className);
					tdg.addType(t);
					Vector v = new Vector(vars);
					while (v.size() > 0)
						tdg.combine(tdg.getTypeVariable(t), (TypeVariable) v.remove(0));
				} else
					if (vType instanceof NullType) {
						tdg.addType(vType);
						Vector v = new Vector(vars);
						while (v.size() > 0)
							tdg.combine(tdg.getTypeVariable(vType), (TypeVariable) v.remove(0));
					} else
						if ((vType instanceof FloatType) || (vType instanceof DoubleType)) {
							Object t = ConcreteRealAbstraction.v();
							tdg.addType(t);
							Vector v = new Vector(vars);
							while (v.size() > 0)
								tdg.combine(tdg.getTypeVariable(t), (TypeVariable) v.remove(0));
						} else
							if (vType instanceof BaseType) {
								Object t = ConcreteIntegralAbstraction.v();
								tdg.addType(t);
								Vector v = new Vector(vars);
								while (v.size() > 0)
									tdg.combine(tdg.getTypeVariable(t), (TypeVariable) v.remove(0));
							} else {
								tdg.addType(vType);
								Vector v = new Vector(vars);
								while (v.size() > 0)
									tdg.combine(tdg.getTypeVariable(vType), (TypeVariable) v.remove(0));
							}
			}
		} else {
			Set children;
			Iterator j;
			Vector v;
			Object t, t2;
			Vector list = new Vector(roots);
			Stack s = new Stack();
			Stack c = new Stack();
			while (list.size() > 0) {
				o = (TypeVariable) list.remove(0);
				s.push(o);
				c.push(new Vector(tdg.getChildren(o)));
				while (!s.empty()) {
					o = (TypeVariable) s.peek();
					v = (Vector) c.peek();
					if (tdg.isLeaf(o)) {
						t = tdg.getType(o);
						if (t == null) {
							t = o.getType();
							if ((t instanceof FloatType) || (t instanceof DoubleType)) {
								t = ConcreteRealAbstraction.v();
								tdg.addType(t);
								tdg.combine(tdg.getTypeVariable(t), (TypeVariable) o);
							} else
								if (t instanceof BaseType) {
									t = ConcreteIntegralAbstraction.v();
									tdg.addType(t);
									tdg.combine(tdg.getTypeVariable(t), (TypeVariable) o);
								} else
									if (t instanceof RefType) {
										t = ClassAbstraction.v(((RefType) t).className);
										tdg.addType(t);
										tdg.combine(tdg.getTypeVariable(t), (TypeVariable) o);
									} else
										if (t instanceof ArrayType) {
											System.out.println("What to do in arrays.");
										} else {
											System.out.println("I shouldn't be printing this.");
										}
						}
						s.pop();
						c.pop();
					} else
						if (v.size() > 0) {
							o = (TypeVariable) v.remove(0);
							s.push(o);
							c.push(new Vector(tdg.getChildren(o)));
						} else {
							// No more
							s.pop();
							c.pop();
							children = new HashSet();
							j = tdg.getChildren(o).iterator();
							while (j.hasNext()) {
								t = tdg.getType((TypeVariable) j.next());
								if (t == null)
									System.out.println("Error:  No type here.");
								else
									children.add(t);
							}
							t = tdg.getType(o);
							t2 = coercionManager.lub(children);
							if (t2 == null) {
								System.out.println("Error lub for " + children + "\n\tt=" + t);
								System.out.println("\t" + o + "\t" + tdg.getChildren(o));
							} else
								if (t == null) {
									tdg.addType(t2);
									tdg.combine(tdg.getTypeVariable(t2), (TypeVariable) o);
								} else
									if (!coercionManager.coerciable((Abstraction) t2, (Abstraction) t)) {
										System.out.println("Trying to coerce " + t2 + " to " + t + " which I say is impossible.");
										conflicts.add(t2 + " to " + t);
									}
						}
				}
			}
		}
	}
	return 0;
}
public void solveConstraints() {
	List con = new Vector();
	Constraint c;
	for (Iterator i = constraints.iterator(); i.hasNext();) {
		c = (Constraint) i.next();
		if (c instanceof EqualConstraint) {
			EqualConstraint e = (EqualConstraint) c;
			TypeVariable l = e.getLeft();
			TypeVariable r = e.getRight();
			tdg.quickCombine(l, r);
		} else {
			con.add(c);
		}
	}
	for (Iterator i = con.iterator(); i.hasNext();) {
		c = (Constraint) i.next();
		if (c instanceof CoerceConstraint) {
			CoerceConstraint a = (CoerceConstraint) c;
			TypeVariable l = a.getLeft();
			TypeVariable r = a.getRight();
			tdg.addChild(l, r);
		} else {
			System.out.println("Warning: *** unknown constraint in TypeInference.solveConstraints() ***");
		}
	}
	constraints = con;
}
/**
 * This method was created in VisualAge.
 * @return boolean
 * @param options Options
 */
public TypeTable type(SootClassManager scm, Hashtable options) {
	for (Enumeration e = options.keys(); e.hasMoreElements();) {
		Object o = e.nextElement();
		if (o instanceof SootField) {
			SootField sf = (SootField) o;
			TypeStructure n = getTypeStructure(sf);
			TypeVariable tv = tdg.addType(options.get(sf));
			constraints.addAll(n.genEqualConstraints(getTypeStructure(tv)));
		}
	}
	for (Enumeration e = options.keys(); e.hasMoreElements();) {
		Object o = e.nextElement();
		if (o instanceof LocalMethod) {
			LocalMethod lm = (LocalMethod) o;
			TypeStructure n = getTypeStructure(lm.getLocal());
			Object t = options.get(new LocalMethod(lm.getMethod(), lm.getLocal()));
			TypeVariable tv = tdg.addType(t);
			constraints.addAll(n.genEqualConstraints(getTypeStructure(tv)));
		}
	}
	for (Enumeration e = options.keys(); e.hasMoreElements();) {
		Object o = e.nextElement();
		if (o instanceof SootClass) {
			SootClass sc = (SootClass) o;
			for (ca.mcgill.sable.util.Iterator i = sc.getMethods().iterator(); i.hasNext();) {
				SootMethod sm = (SootMethod) i.next();
				createInheritanceConstraints(sm);
			}
		}
	}
	for (Enumeration e = options.keys(); e.hasMoreElements();) {
		Object o = e.nextElement();
		if (o instanceof SootClass) {
			SootClass sc = (SootClass) o;
			for (ca.mcgill.sable.util.Iterator i = sc.getMethods().iterator(); i.hasNext();) {
				SootMethod sm = (SootMethod) i.next();
				Vector p = new Vector();
				if (sm.isStatic()) {
					p.add(new Object());
				} else {
					Object t = ClassAbstraction.v(sm.getDeclaringClass().getName());
					TypeStructure v = getTypeStructure(t);
					tdg.addType(t);
					p.add(v);
				}
				for (ca.mcgill.sable.util.Iterator j = sm.getParameterTypes().iterator(); j.hasNext();) {
					p.add(getTypeStructure(j.next()));
				}
				createConstraints(sm, p, getTypeStructure(sm.getReturnType()));
			}
		}
	}
	//status();
	//System.out.println("Solving Constraints.");
	log.debug("Solving Constraints.");
	solveConstraints();
	//System.out.println("Solving Coercions.");
	log.debug("Solving Coercions.");
	solveCoercions();
	//status();
	//System.out.println("Assign Types.");
	log.debug("Assign Types.");
	List unknowns = new Vector();
	Iterator i = ast2TypeStruct.keySet().iterator();
	Object t;
	Object n;
	TypeStructure tv;
	while (i.hasNext()) {
		n = i.next();
		tv = (TypeStructure) ast2TypeStruct.get(n);
		t = getType(tv);
		if (t == null) {
			Type vType = n instanceof Value? ((Value) n).getType() : ((SootField) n).getType();
			Abstraction a = getAbstraction(vType);
			if (a != null)
				nodes.put(n, a);
			else
				unknowns.add(n);
			/*
			if (tv instanceof ArrayTypeStructure) {
				ArrayTypeStructure atv = (ArrayTypeStructure) tv;
				Object index = tdg.getType(((BaseTypeStructure) atv.getIndex()).getVar());
				Object components = null;
				if (atv.getElements() instanceof BaseTypeStructure)
					components = tdg.getType(((BaseTypeStructure) atv.getIndex()).getVar());
				else
					if (atv.getElements() instanceof ObjectTypeStructure)
						components = tdg.getType(((ObjectTypeStructure) atv.getIndex()).getVar());
					else
						System.out.println("Don't handle Array Structs here yet.  Can you help me.  Search for 12345654321 in the code.");
				if (index == null || components == null) {
					unknowns.add(n);
				} else {
					ArrayAbstraction aat = ArrayAbstraction.v((Abstraction) components, (IntegralAbstraction) index);
					nodes.put(n, aat);
				}
			} else {
				unknowns.add(n);
			}
			*/
		} else {
			if (t instanceof ArrayAbstraction) {
				ArrayType vType = (ArrayType) (n instanceof Value? ((Value) n).getType() : ((SootField) n).getType());
				ArrayAbstraction aa = (ArrayAbstraction) t;
				IntegralAbstraction indexAbstraction = aa.getIndexAbstraction();
				if (indexAbstraction == null) indexAbstraction = ConcreteIntegralAbstraction.v();
				Abstraction elementAbstraction = aa.getElementAbstraction();
				if (elementAbstraction == null) elementAbstraction = getAbstraction(vType.baseType);
				t = ArrayAbstraction.v(elementAbstraction, indexAbstraction);
			}
			nodes.put(n, t);
		}
	}
	//status();
	//System.out.println(unknowns);

	if (unknowns.size() != 0) {
		errors.put("Unknowns", unknowns);
	}
	if (conflicts.size() != 0) {
		errors.put("Conflicts", conflicts);
	}
	//Iterator it = unknowns.iterator();
	//while (it.hasNext())
	//System.out.println(ufs.find(it.next()));
	//System.out.println(nodes);
	return nodes;
}
}
