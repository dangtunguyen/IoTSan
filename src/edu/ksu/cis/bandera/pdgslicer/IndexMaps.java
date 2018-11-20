package edu.ksu.cis.bandera.pdgslicer;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.pdgslicer.exceptions.*;
import edu.ksu.cis.bandera.annotation.*;
import java.util.BitSet;
import java.util.Vector;
import java.util.Enumeration;

/**
* This class is for collecting all necessary information for one method.
*/
public class IndexMaps {
	/** a map from {@link Value Value} to {@link BitSet BitSet}
	* representing all assignments for one local.
	*/
	private Map locAssIndex;
	private JimpleBody jimpleBody;
	private StmtGraph stmtGraph;
	private StmtList stmtList;
	/**
	* StmtList before slicing, since slicing will change the contents of stmtList.
	*/
	private StmtList originalStmtList;
	/**
	* an array of all locals assigned value by parameters identity statements.
	* <br> The index of the array is the index of parameters.
	* <br> For example,
	* <br> <code> m:=@para0; </code>
	* <br> <code> n:=@para1; </code>
	* <br> will make <code>paraLocalSet = {m,n}</code>.
	*/
	private Local paraLocalSet[]; //local (Local) set
	/** an array of parameter identity statements.
	* <br> The index of the array is the index of parameters.
	* <br> For example,
	* <br> <code> m:=@para0; </code>
	* <br> <code> n:=@para1; </code>
 	* <br> will make <code>paraIdentityStmt = {m:=@para0,n:=@para1}</code>.
	*/
	private Stmt paraIdentityStmt[];
	/**
	* local variable for <b>this</b> reference.
	* <br> For example,
	* <br> <code> JJJCTEMP$0:=@this; </code>
	* <br> will make <code>thisRefLocal</code> be <code>JJJCTEMP$0</code>.
	*/
	private Local thisRefLocal = null;
	/**
	* statement index of this reference statement.
	*/
	private Integer thisRefStmtIndex;
	private Stmt thisRefStmt;
		/**
	* a map from {@link CallSite CallSite} to {@link SootMethod SootMethod}
	* where SootMethod is called in the CallSite.
	*/
	private Map callSiteMap;
	private Fields MOD = new Fields();
	private Fields REF = new Fields();
	/**
	* a list of {@link SpecialInvokeStmt SpecialInvokeStmt}.
	*/
	private List specialInvokeList = new ArrayList();
	/**
	* a map from {@link Stmt Stmt} to {@link BitSet BitSet}
	* representing a set of (control flow transfer) statements from which
	* they can <code>goto</code> the key statement of the map.
	*/
	private Map jumpTargetMap = new HashMap();
	protected final static int ENTRY = -1;
	protected final static int SPECIALEXIT = -2;
	protected final static Integer specialExitNode = new Integer(SPECIALEXIT);
	public ca.mcgill.sable.soot.SootMethod sootMethod;
	protected final static java.lang.Integer EntryNode = new Integer(ENTRY);
	private Set returnAnnotations = new ArraySet();
/**
* Analyse one method:
* <br> {@link #buildJumpTargetMap() buildJumpTargetMap()};
* <br> {@link #buildLocalAssIndexMap() buildLocalAssIndexMap()};
* <br> {@link #collectFieldReferences() collectFieldReferences()};
* <br> {@link #collectSpecialInvokes() collectSpecialInvokes()};
* <br> {@link #buildCallSiteMap() buildCallSiteMap()}.
*/
public IndexMaps(SootMethod sm) {
	sootMethod = sm;
	jimpleBody = (JimpleBody) sootMethod.getBody(Jimple.v());
	stmtList = jimpleBody.getStmtList();
	originalStmtList = new StmtList(jimpleBody);
	originalStmtList.addAll(stmtList);
	stmtGraph = new CompleteStmtGraph(stmtList);
	buildJumpTargetMap();
	buildLocalAssIndexMap();
	collectFieldReferences();

	//previous four procedure can be combined into one

	//at this moment, we only collect the virtualinvoke call site,
	//special invoke callsite and
	//not  static invoke callsite
	collectSpecialInvokes();
	buildCallSiteMap();
	//collectSpecialInvokes();
	collectReturnAnnotations();
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-14 14:54:19)
 * @return boolean
 * @param ann edu.ksu.cis.bandera.annotation.Annotation
 */
private boolean annotationContainsReturn(Annotation ann) {
	Stmt[] statements = ann.getStatements();
	for (int i = 0; i < statements.length; i++) {
		if ((statements[i] instanceof ReturnStmt) || (statements[i] instanceof ReturnVoidStmt))
			return true;
	}
	return false;
}
/**
   * Build call site map into {@link #callSiteMap callSiteMap}.
   */
private void buildCallSiteMap() {
	callSiteMap = new HashMap();
	InvokeExpr invokeExpr = null;
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		if (SlicingMethod.isBanderaInvoke(stmt))
			continue;
		if ((stmt instanceof InvokeStmt))
			invokeExpr = (InvokeExpr) ((InvokeStmt) stmt).getInvokeExpr();
		else

			
			//suppose there is only one invokeExpr in one Stmt
			invokeExpr = getInvokeExprFrom(stmt);
		if (invokeExpr != null) {
			SootMethod calledMethod;
			if (invokeExpr instanceof InterfaceInvokeExpr)
				calledMethod = getImplementedMd(invokeExpr);
			else
				calledMethod = invokeExpr.getMethod();
			if (calledMethod == null)
				continue;
			int modifier = calledMethod.getModifiers();
			if (Modifier.isAbstract(modifier)) {
				calledMethod = getImplementedMd(calledMethod);
			}
			if (calledMethod == null)
				continue;
			String calledMethodSignature = calledMethod.getSignature();
			if (InfoAnalysis.nativeMdSig.contains(calledMethod))
				continue;
			if (calledMethodSignature.startsWith("java.lang.Thread.start()"))
				calledMethod = getInvokeMethodForStart(invokeExpr, stmt);
			else
				if (calledMethodSignature.startsWith("java.") || calledMethodSignature.startsWith("javax."))
					continue;
			if (calledMethod == null)
				continue;
			CallSite callSite = new CallSite();
			callSite.invokeExpr = invokeExpr;
			callSite.callerSootMethod = jimpleBody.getMethod();
			callSite.callStmt = stmt;
			callSiteMap.put(callSite, calledMethod);
		}
	}
}
  /** 
   * Build a map from jump target to jump sources into {@link #jumpTargetMap jumpTargetMap}.
   */
private void buildJumpTargetMap() {
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		if (stmt instanceof GotoStmt) {
			Stmt gotoTarget = (Stmt) ((GotoStmt) stmt).getTarget();
			putTargetIntoMap(gotoTarget, stmt);
		} else
			if (stmt instanceof IfStmt) {
				Stmt ifTarget = ((IfStmt) stmt).getTarget();
				putTargetIntoMap(ifTarget, stmt);
			}
	}
}
  /**
   * Build local assignments map into {@link #locAssIndex locAssIndex};
   * <br> Fill the array {@link #paraLocalSet paraLocalSet};
   * <br> Fill the array {@link #paraIdentityStmt paraIdentityStmt}.
   */
private void buildLocalAssIndexMap() {
	int paraCount = sootMethod.getParameterCount();
	paraLocalSet = new Local[paraCount];
	paraIdentityStmt = new Stmt[paraCount];
	locAssIndex = new HashMap();
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		List defBoxes = stmt.getDefBoxes();
		for (Iterator boxIt = defBoxes.iterator(); boxIt.hasNext();) {
			Value defValue = ((ValueBox) boxIt.next()).getValue();
			BitSet defSet = new BitSet(stmtList.size());
			defSet.set(stmtList.indexOf(stmt));
			if (locAssIndex.containsKey(defValue))
				defSet.or((BitSet) locAssIndex.get(defValue));
			locAssIndex.put(defValue, defSet);
		}
		if (stmt instanceof IdentityStmt) {
			IdentityStmt defStmt = (IdentityStmt) stmt;
			Value rightOp = defStmt.getRightOp();
			if (rightOp instanceof ParameterRef) {
				Value left = defStmt.getLeftOp();
				ParameterRef pr = (ParameterRef) rightOp;
				if (left instanceof Local) {
					paraLocalSet[pr.getIndex()] = (Local) left;
					paraIdentityStmt[pr.getIndex()] = stmt;
				} else
					throw new BaseValueNonLocalException("parameter ref should be a local variable in method: IndexMaps.buildLocalAssIndexMap()");
			} else
				if (rightOp instanceof ThisRef) {
					Value left = defStmt.getLeftOp();
					if (left instanceof Local) {
						thisRefLocal = (Local) left;
						thisRefStmtIndex = new Integer(stmtList.indexOf(stmt));
						thisRefStmt = defStmt;
					} else
						throw new BaseValueNonLocalException("this ref should be a local variable in method: IndexMaps.buildLocalAssIndexMap()");
				}
		}
	}
}
//add assignment due to call using call site map
//in MethodCallAnalysis class : assignmentByMdCall()
/**
   * Get MOD/REF information on field references for one method.
   */
private void collectFieldReferences() {
	Set modStaticFields = new ArraySet();
	Set modInstanceFields = new ArraySet();
	Set modParaFields = new ArraySet();
	Set modOtherInsFds = new ArraySet();
	Set refStaticFields = new ArraySet();
	Set refInstanceFields = new ArraySet();
	Set refParaFields = new ArraySet();
	Set refOtherInsFds = new ArraySet();
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt originalStmt = (Stmt) stmtIt.next();
		Stmt newStmt = originalStmt;
		List useBoxes = originalStmt.getUseBoxes();
		List defBoxes = originalStmt.getDefBoxes();
		Set instanceFdsNotInCurrentClass = new ArraySet();
		Set staticFields = new ArraySet();
		Set instanceFields = new ArraySet();
		Set paraFields = new ArraySet();
		for (Iterator varBoxIt = useBoxes.iterator(); varBoxIt.hasNext();) {
			Value v = ((ValueBox) varBoxIt.next()).getValue();
			/*
			if (v instanceof ArrayRef) {
				Value basev = ((ArrayRef) v).getBase();
				StmtCls stmtCls = new StmtCls();
				v = LockAnalysis.getValueOfLocal(basev, originalStmt, stmtCls, stmtGraph);
				newStmt = stmtCls.stmt;
			}
			*/
			if (v instanceof StaticFieldRef)
				staticFields.add((StaticFieldRef) v);
			else
				if (v instanceof InstanceFieldRef) {
					//It's not so simple as just to add this instance field 
					//into the set. We have to determine that
					//if this InstancFieldRef is the field of this class
					InstanceFieldRef insFieldRef = ((InstanceFieldRef) v);
					if (Fields.isThisRef(sootMethod, insFieldRef.getBase(), thisRefStmt))
						instanceFields.add(insFieldRef);

					//otherwise, to see 
					//if this InstanceFieldRef is the parameter local
					else
						if (isParaField(insFieldRef, newStmt))
							paraFields.add(insFieldRef);
						else
							instanceFdsNotInCurrentClass.add(insFieldRef);
				}
		}
		if (staticFields.size() != 0) {
			DataBox newdbx = new DataBox(originalStmt, staticFields);
			refStaticFields.add(newdbx);
		}
		if (instanceFields.size() != 0) {
			DataBox newdbx = new DataBox(originalStmt, instanceFields);
			refInstanceFields.add(newdbx);
		}
		if (paraFields.size() != 0) {
			DataBox newdbx = new DataBox(originalStmt, paraFields);
			refParaFields.add(newdbx);
		}
		if (!instanceFdsNotInCurrentClass.isEmpty()) {
			DataBox newdbx = new DataBox(originalStmt, instanceFdsNotInCurrentClass);
			refOtherInsFds.add(newdbx);
		}
		staticFields = new ArraySet();
		instanceFields = new ArraySet();
		paraFields = new ArraySet();
		instanceFdsNotInCurrentClass = new ArraySet();
		newStmt = originalStmt;
		for (Iterator varBoxIt = defBoxes.iterator(); varBoxIt.hasNext();) {
			Value v = ((ValueBox) varBoxIt.next()).getValue();
			/*
			if (v instanceof ArrayRef) {
				Value basev = ((ArrayRef) v).getBase();
				StmtCls stmtCls = new StmtCls();
				v = LockAnalysis.getValueOfLocal(basev, originalStmt, stmtCls, stmtGraph);
				newStmt = stmtCls.stmt;
			}
			*/
			if (v instanceof StaticFieldRef)
				staticFields.add((StaticFieldRef) v);
			else
				if (v instanceof InstanceFieldRef) {
					InstanceFieldRef insFieldRef = ((InstanceFieldRef) v);
					if (Fields.isThisRef(sootMethod, insFieldRef.getBase(), thisRefStmt))
						instanceFields.add(insFieldRef);
					else
						if (isParaField(insFieldRef, newStmt))
							paraFields.add(insFieldRef);
						else
							instanceFdsNotInCurrentClass.add(insFieldRef);
				}
		}
		if (staticFields.size() != 0) {
			DataBox newdbx = new DataBox(originalStmt, staticFields);
			modStaticFields.add(newdbx);
		}
		if (instanceFields.size() != 0) {
			DataBox newdbx = new DataBox(originalStmt, instanceFields);
			modInstanceFields.add(newdbx);
		}
		if (paraFields.size() != 0) {
			DataBox newdbx = new DataBox(originalStmt, paraFields);
			modParaFields.add(newdbx);
		}
		if (!instanceFdsNotInCurrentClass.isEmpty()) {
			DataBox newdbx = new DataBox(originalStmt, instanceFdsNotInCurrentClass);
			modOtherInsFds.add(newdbx);
		}
	}
	MOD.staticFields = modStaticFields;
	MOD.instanceFields = modInstanceFields;
	MOD.paraFields = modParaFields;
	MOD.otherInsFds = modOtherInsFds;
	REF.staticFields = refStaticFields;
	REF.instanceFields = refInstanceFields;
	REF.paraFields = refParaFields;
	REF.otherInsFds = refOtherInsFds;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-14 12:55:28)
 */
private void collectReturnAnnotations() {
	SootClass sc= sootMethod.getDeclaringClass();
	/*
	if (sc.getName().equals("RWPrinter") && sootMethod.getName().equals("read_")){
		System.out.println("printer.read");
	}
	*/
	Annotation annForSm = Slicer.annManagerForSlicer.getAnnotation(sootMethod.getDeclaringClass(), sootMethod);
	Vector currentCFANNs = annForSm.getAllAnnotations(true);
	for (Enumeration e = currentCFANNs.elements(); e.hasMoreElements();) {
		Annotation cfann = (Annotation) e.nextElement();
		if (annotationContainsReturn(cfann))
			returnAnnotations.add(cfann);
	}
}
/**
   * Collect all special invoke statement/expression into {@link #specialInvokeList specialInvokeList}.
   */
private void collectSpecialInvokes() {
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		InvokeExpr invokeExpr = null;
		Stmt stmt = (Stmt) stmtIt.next();
		if (stmt instanceof InvokeStmt) {
			Value invokeValue = ((InvokeStmt) stmt).getInvokeExpr();
			invokeExpr = (InvokeExpr) invokeValue;
		}
		//specialInvokes will only be in the invoke statement, as far as we know,
		//specialInovkes will not be any expression in an assignment.
		/*else
		if (stmt instanceof AssignStmt) {
		Value rightOp = ((AssignStmt) stmt).getRightOp();
		if (rightOp instanceof InvokeExpr)
		invokeExpr = (InvokeExpr) rightOp;
		}
		*/
		if (invokeExpr != null)
			if (invokeExpr instanceof SpecialInvokeExpr)
				specialInvokeList.add(stmt);
	}
}
  /**
   * Get all exit nodes including <code>throw</code> statements 
   * in the control flow graph of the method.
   */
public BitSet exitNodes() {
	BitSet exitNodeSet = new BitSet(stmtList.size());
	List tails = stmtGraph.getTails();
	for (Iterator tailIt = tails.iterator(); tailIt.hasNext();) {
		Stmt tail = (Stmt) tailIt.next();
		exitNodeSet.set(stmtList.indexOf(tail));
	}

	//element of tails are those successor is 0
	//add return stmt to exitNode, whose successor is not 0
	//this is caused from nextnextstmtAddress, 
	//the successor of these returns must be an exception catch

	//and some throw statements are also have non 0 successors because
	//of the catch "caught"

	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt retStmt = (Stmt) stmtIt.next();
		if ((retStmt instanceof ReturnStmt) || (retStmt instanceof ReturnVoidStmt) || (retStmt instanceof ThrowStmt)) {
			List succs = stmtGraph.getSuccsOf(retStmt);
			if (succs.size() != 0)
				
				//maybe should limit to succ.size() == 1, and the succ
				//is an exeception handler
				exitNodeSet.set(stmtList.indexOf(retStmt));
		}
	}
	return exitNodeSet;
}
  /**
   * Exclude all <code>throw</code> statements in the exit nodes set.
   * <p>
   * @param exitNodeSet a set of all exit nodes.
   * @return a set of exit nodes without <code>throw</code>s.
   */
public BitSet exitNodesWithoutThrow(BitSet exitNodeSet) {

	BitSet nodeSet = (BitSet) exitNodeSet.clone();
	for (int i = 0; i < exitNodeSet.size(); i++) {
		if (! exitNodeSet.get(i))
			continue;
		Stmt exitStmt = (Stmt) stmtList.get(i);
		if (exitStmt instanceof ThrowStmt)
			nodeSet.clear(i);
	}
	return nodeSet;
}
  /**
   * Get call site map.
   * <p>
   * @return {@link #callSiteMap callSiteMap}.
   */
  public Map getCallSiteMap()
	{
	  return callSiteMap;
	}
  /**
   * Get all assignments to a variable.
   * <p>
   * @param basev query variable.
   * @return a set of assignments defining values to <code>basev</code>.
   */
private BitSet getDefSetFrom(Value basev)
	{
	  String baseStr = basev.toString();
	  for (Iterator keyIt = locAssIndex.keySet().iterator(); keyIt.hasNext();)
	{
	  Value keyValue = (Value) keyIt.next();
	  if (keyValue.toString().equals(baseStr)) return ((BitSet) locAssIndex.get(keyValue));
	}
	  return null;
	}
/**
 * Get the method that a invoke expression invokes: especially useful
 * for methods rewrited in <code>subclass</code> or declared in an 
 * <code>interface</code>.
 * <p>
 * @return the method invoked
 * @param invokeExp invoke expression
 */
private SootMethod getImplementedMd(InvokeExpr invokeExp) {
	SootMethod sm = invokeExp.getMethod();
	SootClass interfaceClass = sm.getDeclaringClass();
	Set implementingClassSet = (Set) Slicer.interfaceImplementedByMap.get(interfaceClass);
	if (implementingClassSet == null || implementingClassSet.isEmpty())
		return null;
	SootClass implementingClass = getImplementingClassByBOFA(implementingClassSet);
	SootMethod impMd = implementingClass.getMethod(sm.getName(), sm.getParameterTypes());
	return impMd;
}
/**
 * Get the method that implements a given abstract method.
 * <p>
 * @return the implementing method.
 * @param abstractMd abstract method.
 */
private SootMethod getImplementedMd(SootMethod abstractMd) {
	SootClass interfaceClass = abstractMd.getDeclaringClass();
	Set implementingClassSet = (Set) Slicer.interfaceImplementedByMap.get(interfaceClass);
	if (implementingClassSet == null || implementingClassSet.isEmpty())
		return null;
	SootClass implementingClass = getImplementingClassByBOFA(implementingClassSet);
	SootMethod impMd = implementingClass.getMethod(abstractMd.getName(), abstractMd.getParameterTypes());
	return impMd;
}
/**
 * A temporary interface with BOFA.
 * <p>
 * @return one class that implements the interface at current point.
 * @param impClasses a set of classes that implements the same one interface.
 */
private SootClass getImplementingClassByBOFA(Set impClasses) {
	SootClass returnClass = null;
	for (Iterator impIt = impClasses.iterator(); impIt.hasNext();) {
		returnClass = (SootClass) impIt.next();
		break;
	}
	return returnClass;
}
/**
 * From all used value in one statement, extract one invoke expression, if any.
 * <br>Suppose there is at most one invoke expression in one statement.
 *<p>
 * @return an invoke expression in the statement.
 * @param stmt query statement.
 */
private InvokeExpr getInvokeExprFrom(Stmt stmt) {
	//suppose there is only one invokeExpr in one Stmt
	InvokeExpr invokeExpr = null;
	for (Iterator useBoxIt = stmt.getUseBoxes().iterator(); useBoxIt.hasNext();) {
		Value useValue = ((ValueBox) useBoxIt.next()).getValue();
		if (useValue instanceof InvokeExpr)
			return ((InvokeExpr) useValue);
	}
	return invokeExpr;
}
/**
   * Get invoke method for <code>start</code>.
   * <br>For example, <code>a.start();</code> is actually calling 
   * <code>run</code> method in the class of <code>a</code> --- <b>A</b>, 
   * where <b>A</b> is subclass of <code>java.lang.Thread</code> or 
   * an implementation of <code>java.lang.Runnable</code>.
   * <br> Special attention is going to the case such that
   * <br> <code> Thread T1; </code>
   * <br> <code> T1 = new Thread(new CThread(...), ...); </code>
   * <br> <code> ... ... </code>
   * <br> <code> T1.start(); </code>
   * <br> In this case, <code>T1's</code> is <code>java.lang.Thread</code>. 
   * But in <code>T1.start()</code>, we should determine that <code>T1's</code>
   * should be <code>CThread</code> which should be a subclass of 
   * <code>java.lang.Thread</code>, 
   * although <code>T1</code> is declared as type of <code>Thread</code>.
   * <br> The method {@link #lookupSootClassByThread(NonStaticInvokeExpr, Stmt) 
   * lookupSootClassByThread()} 
   * is looking for type of <code>T1</code> in this case.
   * <p>
   * @param invokeExpr invoke expression calling <code>start</code>.
   * @param invokeStmt invoke statement which involves <code>invokeExpr</code>.
   * @return corresponding <code>run</code> method in an appropriate class. 
   * Null is returned if there is no such method is found.
   * @throws SlicerException if the type of base value for an invoke expression is 
   * not a reference type.
   */
private SootMethod getInvokeMethodForStart(InvokeExpr invokeExpr, Stmt invokeStmt) {
	SootMethod returnMd = null;
	List paraList = new ArrayList();
	if (invokeExpr instanceof StaticInvokeExpr)
		throw new SlicerException("the invokeExpr of start should be nonStaticInvokeExpr");
	NonStaticInvokeExpr startInvoke = (NonStaticInvokeExpr) invokeExpr;
	Type baseType = startInvoke.getBase().getType();
	//System.out.println("baseType: " + baseType);
	if (baseType instanceof RefType) {
		if (baseType.toString().equals("java.lang.Thread")) {
			// System.out.println("get sootClass by others");
			SootClass sootClass = lookupSootClassByThread(startInvoke, invokeStmt);
			while (returnMd == null) {
				if (sootClass != null) {
					try {
						returnMd = sootClass.getMethod("run", paraList);
					} catch (ca.mcgill.sable.soot.NoSuchMethodException nsme) {
						sootClass = sootClass.getSuperClass();
					}
				} else
					return returnMd;
			}
			//else 
			//throw new SlicerException("can not find the target runnable class by the invoke expr: " + invokeExpr + " in sootMethod: " + sootMethod);
		} else {
			SootClass sootClass = lookupSootClassByName(((RefType) baseType).className);
			while (returnMd == null) {
				if (sootClass != null) {
					try {
						returnMd = sootClass.getMethod("run", paraList);
					} catch (ca.mcgill.sable.soot.NoSuchMethodException nsme) {
						sootClass = sootClass.getSuperClass();
					}
				} else
					return returnMd;
			}
		}
	} else
		throw new SlicerException("the base type of invokeExpr should be RefType");
	return returnMd;
}
  /**
   * Get jump target map.
   * <p>
   * @return {@link #jumpTargetMap jumpTargetMap}.
   */
  public Map getJumpTargetMap()
	{
	  return jumpTargetMap;
	}
  /** 
   * Get MOD information of this method.
   * <p>
   * @return {@link #MOD MOD}.
   */
  Fields getMOD()
	{
	  return MOD;
	}
/**
 * Get original statement list before slicing.
 * <p>
 * @return {@link #originalStmtList originalStmtList}.
 */
StmtList getOriginalStmtList() {
	return originalStmtList;
}
  /**
   * Get the array of parameter identity statements.
   * <p>
   * @return {@link #paraIdentityStmt paraIdentityStmt}.
   */
  Stmt [] getParaIdentityStmts()
	{
	  return paraIdentityStmt;
	}
  /**
   * Get the set of parameter locals.
   * <p>
   * @return {@link #paraLocalSet paraLocalSet}.
   */
  Local[] getParaLocalSet()
	{
	  return paraLocalSet;
	}
  /**
   * Get REF information of the method.
   * <p>
   * @return {@link #REF REF}.
   */
  Fields getREF()
	{
	  return REF;
	}
/**
 * Insert the method's description here.
 * Creation date: (00-11-14 13:06:01)
 * @return ca.mcgill.sable.util.Set
 */
Set getReturnAnnotations() {
	return returnAnnotations;
}
  /**
   * Get special invoke statement such that of with the same given base value
   * and invoke the mehtod <code>java.lang.Thread.init</code>, i.e.,
   * the special invoke statement such that 
   * <br> <code>specialinvoke base.[java.lang.Thread.init():void]();</code>
   * <p>
   * @return a special invoke statement. Null is returned if there is no such statement.
   */
private InvokeStmt getSpecialInvokeFor(Value base) {
	for (Iterator specIt = specialInvokeList.iterator(); specIt.hasNext();) {
		InvokeStmt specInvoke = (InvokeStmt) specIt.next();
		SpecialInvokeExpr specExpr = (SpecialInvokeExpr) specInvoke.getInvokeExpr();
		if (base == specExpr.getBase()) {
			String signature = specExpr.getMethod().getSignature();
			if (signature.startsWith("java.lang.Thread.<init>"))
				return ((InvokeStmt) (specInvoke));
		}
	}
	return null;
}
 /**
   * Get special invoke statement for a <code>start</code> invoke in the case such that
   * <br> <code> Thread T1; </code>
   * <br> <code> T1 = new Thread(new CThread(...), ...); </code>
   * <br> <code> ... ... </code>
   * <br> <code> T1.start(); </code>
   * <br> This method is to get invoke statement for <code>T1</code>, which is 
   * <code> T1 = new Thread(new CThread(...), ...); </code>.
   * <br> The idea is:
   * <br> (1) Get all definitions of <code>T1</code> before the given invoke statement.
   * <br> (2) From those definitons of <code>T1</code>, extract one special invoke statement
   * which satisfies the constrains given by <code>base</code> and <code>invokeStmt</code>.
   * <p>
   * @param base the base value of the <code>invokeStmt</code>, e.g., <code>T1</code>.
   * @param invokeStmt invoke statement for <code>start()</code>, e.g., 
   * <code>T1.start();</code>.
   * @return a special invoke statement that assigns value to <code>base</code>, e.g.,
   *  <code> T1 = new Thread(new CThread(...), ...); </code>. 
   */
private InvokeStmt getSpecialInvokeForThread(Value base, Stmt invokeStmt) {
	boolean found = false;
	List baseList = new ArrayList();
	baseList.add(base);
	Set visitedBase = new ArraySet();
	InvokeStmt specInvoke = null;
	while (!found) {
		if (baseList.isEmpty())
			found = true;
		else {
			Value baseV = (Value) baseList.remove(0);
			specInvoke = getSpecialInvokeFor(baseV);
			if (specInvoke != null)
				found = true;
			else {
				visitedBase.add(baseV);
				BitSet defSet = null;
				if (baseV instanceof Local)
					defSet = (BitSet) locAssIndex.get(baseV);
				else
					defSet = getDefSetFrom(baseV);
		if (defSet == null)
			continue;
		Set defStmtSet = SetUtil.bitSetToStmtSet(defSet, stmtList);
		for (Iterator defIt = defStmtSet.iterator(); defIt.hasNext();) {
			Stmt defStmt = (Stmt) defIt.next();
			List reachableStmts = SlicingMethod.reachableStmtFrom(defStmt, stmtGraph);
			if (!reachableStmts.contains(invokeStmt))
				continue;
			if (!(defStmt instanceof AssignStmt))
				continue;
			AssignStmt assDefStmt = (AssignStmt) defStmt;
			Value rightOp = assDefStmt.getRightOp();
			if (visitedBase.contains(rightOp))
				continue;
			if (baseList.contains(rightOp))
				continue;
			baseList.add(rightOp);
		}
	}
}
}
return specInvoke;
}
  //callsite map is the map from callsite{className, methodName, callStmt}
  //to called method{className, methodName}, in one work it's the map from
  //InterClassStmt to InterClassStmt
  /**
   * Get special invoke statement list.
   * <p>
   * @return {@link #specialInvokeList specialInvokeList}.
   */
  List getSpecialInvokeList()
	{
	  return specialInvokeList;
	}
  /**
   * Get statement graph provided by Jimple.
   * <p>
   * @return {@link #stmtGraph stmtGraph}
   */
  public StmtGraph getStmtGraph()
	{
	  return stmtGraph;
	}
/**
 * Get statement list.
 * <p>
 * @return {@link #stmtList stmtList}.
 */
StmtList getStmtList() {
	return stmtList;
}
  /**
   * Get this reference local.
   * <p>
   * @return {@link #thisRefLocal thisRefLocal}.
   */
  Local getThisRefLocal()
	{
	  return thisRefLocal;
	}
/**
   * Get the index of this reference statement.
   * <p>
   * @return {@link #thisRefStmtIndex thisRefStmtIndex}
   */
Stmt getThisRefStmt() {
	return thisRefStmt;
}
  /**
   * Get the index of this reference statement.
   * <p>
   * @return {@link #thisRefStmtIndex thisRefStmtIndex}
   */
Integer getThisRefStmtIndex() {
	return thisRefStmtIndex;
}
  /**
   * Get all statements exception those for exception handling.
   * <br> Since we are not dealing with anything about exception handling, 
   * in some cases, we need to exclude those statements in exception handling
   * block.
   * <p>
   * @return a set of statement without those of exception handling.
   */
  BitSet indexSetWithoutExceptionHandling()
	{
	  LinkedList workList = new LinkedList();
	  BitSet indexSet = new BitSet(stmtList.size());

	  workList.addFirst(new Integer(0));

	  while (workList.size() != 0)
	{
	  int nodeIndex = ((Integer) workList.removeFirst()).intValue();
	  indexSet.set(nodeIndex);
	  Stmt nodeStmt = (Stmt) stmtList.get(nodeIndex);
	  List nodeStmtSuccs = stmtGraph.getSuccsOf(nodeStmt);
	  List newSuccs = removeExceptionCaught(nodeStmtSuccs);
	  for (Iterator succIt = newSuccs.iterator();succIt.hasNext();)
	    {
	      Stmt succStmt = (Stmt) succIt.next();
	      int succIndex = stmtList.indexOf(succStmt);
	      if (workList.contains(new Integer(succIndex)) || indexSet.get(succIndex))
		{
		}
	      else workList.addLast(new Integer(succIndex));
	    }
	}
	  return indexSet;
	}
  /** 
   * See if an instance field reference is based on parameter. 
   * This method will call the other overloaded method
   * {@link #isParaField(Local,Stmt) isParaField()}.
   * <br>For example, 
   * <br><code> void md(A paraA, ...){</code>
   * <br><code> ....;</code>
   * <br><code> x = paraA.fd+1;</code>
   * <br><code> ...;</code>
   * <br><code>paraA.fd</code> is a parameter field since the base value
   * <code>paraA</code> is from parameter.
   * <p>
   * @param fieldRef query instance field reference.
   * @param stmt statement that use the reference.
   * @return <code>true</code> if <code>fieldRef</code> is parameter field; 
   * <code>false</code> otherwise.
   * @throws BaseValueNonLocalException if the base value is not local variable.
   */ 
boolean isParaField(InstanceFieldRef fieldRef, Stmt stmt) {
	//SootMethod sootMethod = jimpleBody.getMethod();
	Value base = fieldRef.getBase();
	Local baseLocal = null;
	if (base instanceof Local) {
		baseLocal = (Local) base;
	} else
		throw new BaseValueNonLocalException("the base of field reference should be local in the method isParaField() in IndexMaps.java");
	return isParaField(baseLocal, stmt);
}
//using paraLocalSet to see if the type of para local is an class
//to see if the baseName is a local copy of those para locals

boolean isParaField(Local baseLocal, Stmt stmt) {
	Set paraLocalObjects = new ArraySet();
	for (int i = 0; i < paraLocalSet.length; i++) {
		Local paraLocal = paraLocalSet[i];
		Type paraType = paraLocal.getType();
		if (paraType instanceof BaseType)
			if (paraType instanceof RefType)
				paraLocalObjects.add(paraLocal);
	}
	SimpleLocalCopies simpleLocalCopies = new SimpleLocalCopies((CompleteStmtGraph) stmtGraph);
	for (Iterator j = paraLocalObjects.iterator(); j.hasNext();) {
		Local paraObj = (Local) j.next();
		if (simpleLocalCopies.isLocalCopyOfBefore(baseLocal, paraObj, stmt)) {
			return true;
		} else
			if (baseLocal.equals(paraObj))
				return true;
	}
	return false;
}
  /**
   * Get the map from local to assignments.
   * <p>
   * @return {@link #locAssIndex locAssIndex}.
   */
  public Map localAssMap()
	{
	  return locAssIndex;
	}
  /**
   * Look up sootclass by a given class name through 
   * {@link Slicer#relevanClassArray Slicer.relevantClassArray}.
   * <p>
   * @param className query class name.
   * @return a sootclass with the name <code>className</code>.
   * @throws SlicerException if can not find the sootclass with name 
   * <code>className</code>.
   */
public static SootClass lookupSootClassByName(String className) {
	SootClass classArray[] = Slicer.relevantClassArray;
	for (int i = 0; i < classArray.length; i++)
		if (classArray[i].getName().equals(className))
			return classArray[i];
	return null;
	//throw new SlicerException("can not find the SootClass in relevant sootClass array by the class name: " + className);
}
  /**
   * Get the type of a thread for the case such that
   * <br> <code> Thread T1; </code>
   * <br> <code> T1 = new Thread(new CThread(...), ...); </code>
   * <br> <code> ... ... </code>
   * <br> <code> T1.start(); </code>
   * <br> In this case, <code>T1's</code> is <code>java.lang.Thread</code>. 
   * But in <code>T1.start()</code>, we should determine that <code>T1's</code>
   * should be <code>CThread</code> which should be a subclass of 
   * <code>java.lang.Thread</code>,
   * although <code>T1</code> is declared as type of <code>Thread</code>.
   * <br> The basic idea of this method is
   * <br> (1) Get special invoke statement such that 
   * <code> T1 = new Thread(new CThread(...), ...); </code> from <code>T1</code>
   * by {@link #getSpecialInvokeForThread(Value,Stmt) getSpecialInvokeForThread()}.
   * <br> (2) Analyse the arguments of the special invoke statement to recognize one
   * argument of it whose type is a reference type (e.g., CThread) and a subclass of 
   * <code>java.lang.Thread</code> or an implementing class of 
   * <code>java.lang.Runnable</code>.
   * <p>
   * @param startInvoke the invoke expression which inovkes <code>start()</code>,
   * e.g., <code>T1.start()</code>.
   * @param invokeStmt the invoke statement which invokes <code>start()</code>,
   * e.g., <code>T1.start()</code>.
   * @return a soot class which is the thread type of <code>T1</code>, e.g., 
   * <code>CThread</code>. Null is returned is there is no such soot class can be found.
   * @throws SlicerExeption if can not find the soot class by allocator.
   */
private SootClass lookupSootClassByThread(NonStaticInvokeExpr startInvoke, Stmt invokeStmt) {
	Value base = startInvoke.getBase();
	//The type of base is java.lang.Thread
	InvokeStmt sInvokeStmt = getSpecialInvokeForThread(base, invokeStmt);
	if (sInvokeStmt == null)
		return null;
	SpecialInvokeExpr sInvoke = (SpecialInvokeExpr) sInvokeStmt.getInvokeExpr();

	//definitely cheating !!!!!!!!!
	//return lookupSootClassByName("MessageThread");

	//analyse the arguments to figure out which argument is implementing Runnable interface
	for (int i = 0; i < sInvoke.getArgCount(); i++) {
		Value arg = sInvoke.getArg(i);
		Type argType = arg.getType();
		if (argType instanceof RefType) {
			if (argType.toString().equals("java.lang.Runnable")) {
				//look for allocator of the arg --- BOFA
				SimpleLocalDefs simpleLocalDefs = new SimpleLocalDefs((CompleteStmtGraph) stmtGraph);
				Local argLocal = (Local) arg;
				List defList = simpleLocalDefs.getDefsOfAt(argLocal, sInvokeStmt);
				for (Iterator defIt = defList.iterator(); defIt.hasNext();) {
					DefinitionStmt oneDef = (DefinitionStmt) defIt.next();
					Value rightOp = oneDef.getRightOp();
					if (!(rightOp instanceof NewExpr))
						continue;
					NewExpr newExpr = (NewExpr) rightOp;
					Type opType = newExpr.getType();
					SootClass sootClass = lookupSootClassByName(opType.toString());
					if (sootClass != null) {
						if (sootClass.implementsInterface("java.lang.Runnable"))
							return sootClass;
						else
							if (sootClass.getSuperClass().getName().equals("java.lang.Thread"))
								return sootClass;
					}
				}
				throw new SlicerException("can not find the SootClass by allocator");
			} else {
				SootClass sootClass = lookupSootClassByName(((RefType) argType).className);
				if (sootClass != null) {
					if (sootClass.implementsInterface("java.lang.Runnable"))
						return sootClass;
					else
						if (sootClass.getSuperClass().getName().equals("java.lang.Thread"))
							return sootClass;
				}
			}
		}
	}
	return null;
}
  /**
   * Put given jump target and source into the jump target map 
   * {@link #jumpTargetMap jumpTargetMap}.
   * <p>
   * @param target jump target statement.
   * @param source jump source statement.
   */
private void putTargetIntoMap(Stmt target, Stmt source) {
	BitSet sourceSet;
	if (jumpTargetMap.containsKey(target))
		sourceSet = (BitSet) jumpTargetMap.get(target);
	else
		sourceSet = new BitSet(stmtList.size());
	sourceSet.set(stmtList.indexOf(source));
	jumpTargetMap.put(target, sourceSet);
}
  /**
   * Remove exception caught statement from a list of statement.
   * <p>
   * @param actualSuccs a list of {@link Stmt Stmt}.
   * @return a list of {@link Stmt Stmt} without any statement 
   * for exception handling.
   */
  private List removeExceptionCaught(List actualSuccs)
	{
	  List newSuccs = new ArrayList();
	  Iterator asuccsIt = actualSuccs.iterator();
	  while (asuccsIt.hasNext())
	{
	  Stmt succStmt = (Stmt) asuccsIt.next();
	  if (!jimpleBody.getTraps().contains(succStmt))
	    newSuccs.add(succStmt);
	}

	  return newSuccs;
	}
}
