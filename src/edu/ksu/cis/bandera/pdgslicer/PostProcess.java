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

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.BitSet;

/**
 * This class is for constructing residual program for
 * each sliced class and method.
 */
public class PostProcess
{
  /**
   * A list of {@link ClassInfo ClassInfo} for each class.
   */
  private List classList;
  /**
   * A list of {@link ClassInfo ClassInfo} which is sliced away.
   */
  List removableClasses = new ArrayList();
  /**
   * A list of {@link SootMethod SootMethod} such that
   * return value should be changed to return void due to
   * the irrelevance of return value.
   */
  List changeReturnTypeToVoidMds = new ArrayList();
  /** 
   * A set of {@link Value Value} which is relevant in 
   * the residual program.
   */
  private Set relevantFields = new ArraySet();
	private Set relevantExceptionClassNames = new ArraySet();
  private AnnotationManager cfanns = null;

/**
   * @param ct a list of {@link ClassInfo ClassInfo}.
   * @param cfs annotation manager.
   */
public PostProcess(List ct, AnnotationManager cfs) {
	classList = ct;
	cfanns = cfs;
}
/**
 * Add all statements in <code>catch</code> block into
 * a set <code>removedSet</code> except for those
 * in the given slice set.
 * <p>
 * @param removedSet a set of {@link Stmt Stmt} which is
 * the result of this method.
 * @param catches a vector of {@link Annotation Annotation}
 * which is that of catch clause.
 * @param stmtList statement list for indexing.
 * @param linePropList a set of {@link Stmt Stmt} which
 * is slice point.
 * @param slcSet slice set indexed in a BitSet.
 */
private void addCatchesToRemovedSet(Set removedSet, Vector catches, StmtList stmtList, Set linePropList, BitSet slcSet) {
	for (Enumeration e2 = catches.elements(); e2.hasMoreElements();) {
		Annotation catchClauseCFANN = (Annotation) e2.nextElement();
		Stmt trapStmts[] = catchClauseCFANN.getStatements();
		for (int i = 0; i < trapStmts.length; i++) {
			Stmt stmt = trapStmts[i];
			int stmtIndex = stmtList.indexOf(trapStmts[i]);
			if (linePropList.contains(stmt))
				continue;
			removedSet.add(stmt);
			if (slcSet.get(stmtIndex))
				slcSet.set(stmtIndex);
		}
	}
}
/**
 * Add all statements in catch blocks into the given slice set.
 * <p>
 * @param slcSet slice set indexed in a BitSet.
 * @param catches a vector of {@link Annotation Annotation}.
 * @param stmtList statement list for indexing.
 */
private void addCatchesToSliceSet(BitSet slcSet, Vector catches, StmtList stmtList) {
	for (Enumeration e2 = catches.elements(); e2.hasMoreElements();) {
		Annotation catchClauseCFANN = (Annotation) e2.nextElement();
		Stmt trapStmts[] = catchClauseCFANN.getStatements();
		for (int i = 0; i < trapStmts.length; i++) {
			slcSet.set(stmtList.indexOf(trapStmts[i]));
			if (i == 0) {
				Stmt caughtStmt = trapStmts[0];
				if (caughtStmt instanceof IdentityStmt) {
					IdentityStmt idCaughtStmt = (IdentityStmt) caughtStmt;
					Value leftOp = idCaughtStmt.getLeftOp();
					Value rightOp = idCaughtStmt.getRightOp();
					if (leftOp instanceof Local) {
						Local leftOpValue = (Local) leftOp;
						Type valueType = leftOpValue.getType();
						String exceptionName = valueType.toString();
						if (!exceptionName.startsWith("java.lang."))
						this.relevantExceptionClassNames.add(exceptionName);
						//System.out.println("left value type: " + valueType);
					}
					/*
					if (rightOp instanceof CaughtExceptionRef) {
						CaughtExceptionRef rightOpEx = (CaughtExceptionRef) rightOp;
						Type caughtType = rightOpEx.getType();
						//System.out.println("Caught tyep: " + caughtType);
					}
					*/
				}
			}
		}
	}
}
/**
 * Get all variables appeared in one statement.
 * <p>
 * @return a set of {@link Value Value}.
 * @param stmt query statement.
 */
static Set allVarsOf(Stmt stmt) {
	Set buff = new ArraySet();
	for (Iterator useBoxIt = stmt.getUseAndDefBoxes().iterator(); useBoxIt.hasNext();)
		buff.add(((ValueBox) useBoxIt.next()).getValue());
	return buff;
}
  /**
   * Chang arguments of an invoke expression.
   * <br> Since parameters of the invoked method has been changed 
   * because of slicing: some of the parameter has been discarded
   * due to irrelevance, correspondingly all invoke expression
   * to that method should also be changed in arguments.
   * <br> For example, given one sliced method like:
   * <br> <code> method(farg1, farg3)</code>
   * <br> , the original invoke expression is
   * <br> <code> obj.method(aarg1, aarg2, aarg3)</code>
   * <br> and it has been modified, in terms of sliced method
   * declaration, into
   * <br> <code>obj.method(aarg1, aarg3, aarg3)</code>
   * <br> This mehtod is to change the argument number from 
   * 3 to 2, for example, to make the invoke expression
   * be consistent with the method declaration:
   * <br> <code>obj.method(aarg1, aarg3)</code>
   * <p>
   * @param inovkeExpr invoke expression.
   */
  private InvokeExpr buildNewInvokeExpr(InvokeExpr invokeExpr)
	{
	  //InvokeExpr newInvokeExpr = null;

	  SootMethod invokedMethod = invokeExpr.getMethod();
	  int paraCount = invokedMethod.getParameterCount();
	  //List argList = new ArrayList();
	  for (int i=0; i<paraCount; i++)
	    //argList.add(i,invokeExpr.getArg(i));
	    invokeExpr.setArg(i, invokeExpr.getArg(i));
	  invokeExpr.setArgCount(paraCount);
	  /*
	  if (invokeExpr instanceof VirtualInvokeExpr)
	{
	  VirtualInvokeExpr virtualInvokeExpr = 
	    (VirtualInvokeExpr) invokeExpr;
	  newInvokeExpr = Jimple.v().newVirtualInvokeExpr(
			              (Local) virtualInvokeExpr.getBase(),
				      invokedMethod,
				      argList);
	}
	  else if (invokeExpr instanceof StaticInvokeExpr)
	{
	  newInvokeExpr =
	    Jimple.v().newStaticInvokeExpr(invokedMethod, argList);
	}
	  else if (invokeExpr instanceof SpecialInvokeExpr)
	{
	  SpecialInvokeExpr specialInvoke = (SpecialInvokeExpr) invokeExpr;
	  newInvokeExpr =
	    Jimple.v().newSpecialInvokeExpr((Local) specialInvoke.getBase(),
					    invokedMethod, 
					    argList);
	}
	  */
	  return invokeExpr;
	}
  /**
   * Process parameters of a call site in slice. Change parameters of an invoke expression
   * in terms of changes in called method.
   * <p>
   * @param callSiteIndexMap a map from {@link Stmt Stmt} to {@link CallSite CallSite}.
   * @param callSiteMap a map from {@link CallSite CallSite} to {@link SootMethod SootMethod}.
   * @param stmt a statement with an invoke expression.
   * <p>
   * @see {#buildNewInvokeExpr(InvokeExpr)}
   */
private void callSiteInSlice(Map callSiteIndexMap, Map callSiteMap, Stmt stmt) {
	CallSite callSite = (CallSite) callSiteIndexMap.get(stmt);
	SootMethod callee = (SootMethod) callSiteMap.get(callSite);
	MethodInfo calleeMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callee);
	if (calleeMdInfo == null) return;
	if (calleeMdInfo.sCriterion == null) {
	} else {
		LinkedList keepedParaOriginalIndex = getKeepedParaOriginalIndex(calleeMdInfo);
		InvokeExpr invokeExpr = callSite.invokeExpr;
		int argCount = invokeExpr.getArgCount();
		LinkedList keepedParameters = new LinkedList();
		for (int i = 0; i < argCount; i++) {
			Value arg = invokeExpr.getArg(i);
			if (keepedParaOriginalIndex.contains(new Integer(i)))
				keepedParameters.addLast(arg);
		}
		if (keepedParameters.size() < argCount) {
			for (int i = 0; i < keepedParameters.size(); i++)
				invokeExpr.setArg(i, (Value) keepedParameters.get(i));
		}
	}
}
  /**
   * Change the target of a control transferring statement, if that target is 
   * not in slice. For example,
   * <br> ...;
   * <br> goto label-1;
   * <br> ....
   * <br> label-1: stmt;
   * <br> .....
   * <br> if the statement <code>label-1: stmt</code> is not in slice, 
   * while <code>goto label-1</code> is in slice, then <code>label-1</code>
   * should be changed to its immediate postdominator.
   * <p>
   * @param stmtList statement list for indexing.
   * @param newTarget new target.
   * @param oldTarget old target.
   * @param jumpTargetMap a map from jump target {@link Stmt Stmt} to jump sources of the 
   * target {@link BitSet BitSet}.
   */
private void changeTarget(StmtList stmtList, Stmt newTarget, Stmt oldTarget, Map jumpTargetMap) {
  //System.out.println("newTarget: " + newTarget);
  //System.out.println("oldTarget: " + oldTarget);
	BitSet jumpSources = (BitSet) jumpTargetMap.get(oldTarget);
	for (int h = 0; h < jumpSources.size(); h++) {
		if (!jumpSources.get(h))
			continue;
		Stmt jumpSource = (Stmt) stmtList.get(h);
		if (jumpSource instanceof GotoStmt)
			 ((GotoStmt) jumpSource).setTarget(newTarget);
		else
			if (jumpSource instanceof IfStmt)
				 ((IfStmt) jumpSource).setTarget(newTarget);
	}
	jumpTargetMap.remove(oldTarget);
	if (jumpTargetMap.containsKey(newTarget)) {
		BitSet sources = (BitSet) jumpTargetMap.get(newTarget);
		sources.or(jumpSources);
	} else
		jumpTargetMap.put(newTarget, jumpSources);
}
/**
 * See if all statements in an array of statement are not in a slice set.
 * <p>
 * @return <code>true</code> if all statements in the array are not in the slice set,
 * <code>false</code> otherwise.
 * @param bodyStmts an array of statement.
 * @param sliceSet slice set.
 * @param stmtList statement list for indexing.
 */
private boolean emptyBody(Stmt[] bodyStmts, BitSet sliceSet, StmtList stmtList) {
	for (int i = 0; i < bodyStmts.length; i++) {
		if (sliceSet.get(stmtList.indexOf(bodyStmts[i])))
			return false;
	}
	return true;
}
/**
 * See if all statements in a set of statement are not in a slice set.
 * <p>
 * @return <code>true</code> if all statements in the set are not in the slice set,
 * <code>false</code> otherwise.
 * @param bodyStmts a set of statement.
 * @param sliceSet slice set.
 * @param stmtList statement list for indexing.
 */
private boolean emptyBody(Set bodyStmts, BitSet sliceSet, StmtList stmtList) {
	for (Iterator i = bodyStmts.iterator(); i.hasNext();) {
		Stmt stmt = (Stmt) i.next();
		if (sliceSet.get(stmtList.indexOf(stmt)))
			return false;
	}
	return true;
}
/**
 * Collect soot fields from a set of field references.
 * <p>
 * @param fieldRefSet a set of {@link FieldRef FieldRef}.
 * @return a set of {@link SootField SootField}.
 */
private Set fieldRefToField(Set fieldRefSet) {
	Set fieldSet = new ArraySet();
	for (Iterator refIt = fieldRefSet.iterator(); refIt.hasNext();)
		fieldSet.add(((FieldRef) refIt.next()).getField());
	return fieldSet;
}
//filering from used variable instead of relevant variabls
/**
   * Collect all {@link FieldRef FieldRef} from a set of {@link Value Value}
   * into the field {@link #relevantFields relevantFields}.
   * <p>
   * @param variableSet a set of {@link Value Value}.
   */
private void filterOutFields(Set variableSet) {
	for (Iterator kk = variableSet.iterator(); kk.hasNext();) {
		Value loc = (Value) kk.next();
		if (loc instanceof FieldRef)
			relevantFields.add(loc);
	}
}
/**
   * Get all {@link Value Value} used/defined in a set of statements.
   * <p>
   * @param ostmtList statement list for indexing.
   * @param stmtIndexSet a set of statements.
   * @return a set of {@link Value Value}
   */
static Set getAllVarsOf(StmtList ostmtList, BitSet stmtIndexSet) {
	Set allVars = new ArraySet();
	for (int i = 0; i < stmtIndexSet.size(); i++) {
		if (!stmtIndexSet.get(i))
			continue;
		Stmt stmt = (Stmt) ostmtList.get(i);
		allVars.addAll(allVarsOf(stmt));
	}
	return allVars;
}
  /**
   * Get all {@link Value Value} used/defined in a method body.
   * <p>
   * @param mdInfo query method.
   * @return a set {@link Value Value}
   */
private Set getAllVarsOf(MethodInfo mdInfo) {
	StmtList originalStmtList = mdInfo.originalStmtList;
	Set allVars = new ArraySet();
	for (int i = 0; i < originalStmtList.size(); i++) {
		Stmt stmt = (Stmt) originalStmtList.get(i);
		allVars.addAll(allVarsOf(stmt));
	}
	return allVars;
}
  /**
   * Get residual parameters for a method.
   * <p>
   * @param mdInfo query method.
   * @return a {@link LinkedList LinkedList} of Integer which
   * is the index of parameter.
   */
private LinkedList getKeepedParaOriginalIndex(MethodInfo mdInfo) {
	Local paraLocals[] = mdInfo.indexMaps.getParaLocalSet();
	Map relevantVars = mdInfo.sCriterion.relVarMap();
	Set relLocs = getRelevantLocals(mdInfo.originalStmtList, mdInfo.sliceSet, relevantVars);
	LinkedList keepedParaOriginalIndex = new LinkedList();
	for (int i = 0; i < paraLocals.length; i++) {
		Local paraLocal = paraLocals[i];
		if (relLocs.contains(paraLocal)) {
			keepedParaOriginalIndex.addLast(new Integer(i));
		}
	}
	return keepedParaOriginalIndex;
}
  /**
   * Get new jump target from a given current target. The new target should
   * be the immediate postdominator of current target.
   * <p>
   * @param currentTarget index of current target (statement).
   * @param bpdg PDG for the method for getting immediate postdominator.
   * @param rmStmtSet a set statement which should not be in residual program.
   * @return the index of new target.
   */
private int getNewJumpTarget(int currentTarget, BuildPDG bpdg, BitSet rmStmtSet) {
	int newTarget = -1;
	boolean foundNewTarget = false;

	while (!foundNewTarget) {
		newTarget = bpdg.immediatePostdominatorOf(currentTarget);
		if (newTarget<0) newTarget = currentTarget+1;
		if (rmStmtSet.get(newTarget))
			currentTarget = newTarget;
		else
			foundNewTarget = true;
	}
	return newTarget;
}
/**
* Get all relevant variables in given slice set.
* <p>
* @param stmtList statement list for indexing.
* @param sliceSet slice set indexed with statement list.
* @param relevantVars a map of relevant variables for every statement,
* from {@link Stmt Stmt} to a {@link Set Set} of {@link Value Value}.
*/
static Set getRelevantLocals(StmtList stmtList, BitSet sliceSet, Map relevantVars) {
	Set relLocs = new ArraySet();
	for (int i = 0; i < sliceSet.size(); i++) {
		if (!sliceSet.get(i))
			continue;
		Set stmtRelLocs = (Set) relevantVars.get((Stmt) stmtList.get(i));
		if (stmtRelLocs != null)
			relLocs.addAll(stmtRelLocs);
	}
	return relLocs;
}
  /**
   * Get all trap hanlders from jimple body.
   * <p>
   * @param jimpBody jimple body
   * @return a set of {@link Stmt Stmt} which is trap handler.
   */
private Set getTrapHandlers(JimpleBody jimpBody) {
	Set trapsHandlerSet = new ArraySet();
	Iterator trapIt = jimpBody.getTraps().iterator();
	while (trapIt.hasNext()) {
		Trap trap = (Trap) trapIt.next();
		Stmt handler = (Stmt) trap.getHandlerUnit();
		trapsHandlerSet.add(handler);
	}
	return trapsHandlerSet;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-14 11:18:28)
 * @return boolean
 * @param superClass ca.mcgill.sable.soot.SootClass
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private boolean isImplementingOneAbstractMd(SootClass superClass, SootMethod sm) {
	int modifiers = superClass.getModifiers();
	if (!Modifier.isAbstract(modifiers))
		return false;
	SootMethod abstractMd = null;
	try {
		abstractMd = superClass.getMethod(sm.getName(), sm.getParameterTypes(), sm.getReturnType());
	} catch (ca.mcgill.sable.soot.NoSuchMethodException nse) {
		return false;
	}
	modifiers = abstractMd.getModifiers();
	if (Modifier.isAbstract(modifiers))
		return true;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-14 10:59:21)
 * @return boolean
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private boolean isImplementingOneAbstractMd(SootMethod sm) {
	boolean isImplementingMd = false;
	SootClass declaringClass = sm.getDeclaringClass();
	while (declaringClass.hasSuperClass()) {
		SootClass superClass = declaringClass.getSuperClass();
		isImplementingMd = isImplementingOneAbstractMd(superClass, sm);
		if (isImplementingMd)
			return isImplementingMd;
		declaringClass = superClass;
	}
	for (Iterator interfaceIt = declaringClass.getInterfaces().iterator(); interfaceIt.hasNext();) {
		SootClass interfaceClass = (SootClass) interfaceIt.next();
		isImplementingMd = isImplementingOneInterfaceMd(interfaceClass, sm);
		if (isImplementingMd)
			return isImplementingMd;
		while (interfaceClass.hasSuperClass()) {
			SootClass superClass = interfaceClass.getSuperClass();
			isImplementingMd = isImplementingOneInterfaceMd(interfaceClass, sm);
			if (isImplementingMd)
				return isImplementingMd;
		}
	}
	return isImplementingMd;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-14 11:29:00)
 * @return boolean
 * @param interfaceClas ca.mcgill.sable.soot.SootClass
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private boolean isImplementingOneInterfaceMd(SootClass interfaceClass, SootMethod sm) {
	int modifiers = interfaceClass.getModifiers();
	if (!Modifier.isInterface(modifiers))
		return false;
	try {
		SootMethod abstractMd = interfaceClass.getMethod(sm.getName(), sm.getParameterTypes(), sm.getReturnType());
	} catch (ca.mcgill.sable.soot.NoSuchMethodException nse) {
		return false;
	}
	return true;
}
  /**
   * See if a given modifier is public and static.
   * <p>
   * @param modifier modifier.
   * @return boolean.
   */
  private boolean isPublicAndStatic(int modifier)
	{
	  if (Modifier.isPublic(modifier) && 
	  Modifier.isStatic(modifier)) 
	return true;
	  else return false;
	}
  /**
   * Keep all parameters for <code>main</code> method, even those parameters are irrelevant.
   * <p>
   * @param mdInfo query method.
   * @param relevantVars relevant variables map.
   * @param stmtList statement list
   * @param sliceSet slice set.
   */
private void keepParasOfMainMd(MethodInfo mdInfo, Map relevantVars, StmtList stmtList, BitSet sliceSet) {
	//to see if there is main method and then keep all parameter identity statement.

	if (mdInfo.sootMethod.getName().equals("main") && isPublicAndStatic(mdInfo.sootMethod.getModifiers())) {
		Stmt[] paraIds = mdInfo.indexMaps.getParaIdentityStmts();

		//add all parameter identitise into slice set of main method
		for (int ids = 0; ids < paraIds.length; ids++) {
			int idIndex = stmtList.indexOf(paraIds[ids]);
			sliceSet.set(idIndex);
			relevantVars.put(paraIds[ids], SlicingMethod.allLocalVarsOf(paraIds[ids]));
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-14 13:21:07)
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private void makeMethodEmpty(MethodInfo methodInfo, Annotation annForSm) {
	if (methodInfo == null)
		return;
	IndexMaps indexMaps = methodInfo.indexMaps;
	Set removableStmts = new ArraySet();
	Set keepStmts = new ArraySet();
	int thisRefStmtIndex = indexMaps.getThisRefStmtIndex().intValue();
	Stmt thisRefStmt = (Stmt) methodInfo.stmtList.get(thisRefStmtIndex);
	Stmt[] paraIdentities = indexMaps.getParaIdentityStmts();
	//extract return statement and assignment to $ret
	Stmt returnStmt = null;
	Stmt ret$Stmt = null;
	Object[] returnAnns = indexMaps.getReturnAnnotations().toArray();
	if (returnAnns.length > 0) {
		Stmt[] returnStmts = ((Annotation) returnAnns[0]).getStatements();
		Type returnType = methodInfo.sootMethod.getReturnType();
		if (returnType instanceof VoidType) {
			for (int i = 0; i < returnStmts.length; i++) {
				if (returnStmts[i] instanceof ReturnVoidStmt) {
					returnStmt = returnStmts[i];
					break;
				}
			}
		} else
			for (int i = 0; i < returnStmts.length; i++) {
				if (returnStmts[i] instanceof ReturnStmt)
					returnStmt = returnStmts[i];
				else
					if (returnStmts[i] instanceof AssignStmt) {
						Value leftOp = ((AssignStmt) returnStmts[i]).getLeftOp();
						if (leftOp instanceof Local)
							if (((Local) leftOp).getName().equals("$ret"))
								ret$Stmt = returnStmts[i];
					}
			}
	}
	// calculate keeped statement set
	keepStmts.add(thisRefStmt);
	if (returnStmt != null)
		keepStmts.add(returnStmt);
	if (ret$Stmt != null)
		keepStmts.add(ret$Stmt);
	for (int i = 0; i < paraIdentities.length; i++)
		keepStmts.add(paraIdentities[i]);
	//calculate removable statements
	for (Iterator stmtIt = methodInfo.stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		if (!keepStmts.contains(stmt))
			removableStmts.add(stmt);
	}
	//remove statement from annotation
	for (Iterator stmtIt = removableStmts.iterator(); stmtIt.hasNext();) {
		try {
			annForSm.removeStmt((Stmt) stmtIt.next());
		} catch (Exception e) {
		}
	}
	//remove statement from statement list
	if (!removableStmts.isEmpty())
		for (Iterator stmtIt = removableStmts.iterator(); stmtIt.hasNext();)
			methodInfo.stmtList.remove(stmtIt.next());
		//change assignment statement $ret=value into $ret=null;
	if (ret$Stmt != null) {
		NullConstant nullCons = NullConstant.v();
		Value leftOp = ((AssignStmt) ret$Stmt).getLeftOp();
		AssignStmt newRetAssign = Jimple.v().newAssignStmt(leftOp, nullCons);
		int k = methodInfo.stmtList.indexOf(ret$Stmt);
		methodInfo.stmtList.remove(k);
		try {
			cfanns.putInlineStmt(newRetAssign, ret$Stmt, methodInfo.sootMethod);
			annForSm.replaceStmt(ret$Stmt, newRetAssign);
		} catch (Exception e) {
		}
		methodInfo.stmtList.add(k, newRetAssign);
	}
}
  /**
   * Modify jump target map in terms of a changed statement map.
   * <p>
   * @param stmtList statement list.
   * @param changedStmtMap a map from {@link Stmt Stmt} to {@link Stmt Stmt}.
   * @param jumpTargetMap jump target map.
   */
private void modifyJumpTargetMap(StmtList stmtList, Map changedStmtMap, Map jumpTargetMap) {
	for (Iterator changedStmtIt = changedStmtMap.keySet().iterator();
	changedStmtIt.hasNext();) {
		Stmt changedStmt = (Stmt) changedStmtIt.next();
		if (jumpTargetMap.containsKey(changedStmt)) {
			Stmt newTarget = (Stmt) changedStmtMap.get(changedStmt);
			changeTarget(stmtList, newTarget, changedStmt, jumpTargetMap);
		}
	}
}
/**
   * Construct residual method.
   * <p>
   * @param mdInfo method to be constructed.
   */
private void modifyMethod(MethodInfo mdInfo) {
	JimpleBody jimpBody = (JimpleBody) mdInfo.sootMethod.getBody(Jimple.v());
	StmtList stmtList = mdInfo.stmtList;
	StmtList stmtListClone = mdInfo.originalStmtList;
	BitSet sliceSet = mdInfo.sliceSet;
	int stmtListSize = stmtListClone.size();
	Map jumpTargetMap = mdInfo.indexMaps.getJumpTargetMap();
	Integer thisRefIndex = mdInfo.indexMaps.getThisRefStmtIndex();
	Integer specialInvokeIndex = null;

	//add for every instance method this reference stmt to initSliceSet required by Robby 1/7/2000

	if (thisRefIndex != null)
		sliceSet.set(thisRefIndex.intValue());
	/*
	//to see if the method is <init method, if it is, include the special
	//invoke into the slice set
	if (mdInfo.sootMethod.getName().equals("<init>")) {
	for (Iterator si = mdInfo.indexMaps.getSpecialInvokeList().iterator(); si.hasNext();) {
	SpecialInvokeStmt invoke = (SpecialInvokeStmt) si.next();
	SpecialInvokeExpr specInvoke = (SpecialInvokeExpr) invoke.invokeExpr;
	if (mdInfo.indexMaps.getThisRefLocal().equals(specInvoke.getBase())) {
	if (specInvoke.getMethod().getName().equals("<init>")) {
	if (mdInfo.sootClass.getSuperClass().equals(specInvoke.getMethod().getDeclaringClass())) {
	int specialInvokeInt = stmtListClone.indexOf(invoke.invokeStmt);
	specialInvokeIndex = new Integer(specialInvokeInt);
	sliceSet.set(specialInvokeInt);
	}
	}
	}
	
	//String specSignature = invoke.invokeExpr.getMethod().getSignature();
	//if (specSignature.startsWith("java.lang")) {
	//int specialInvokeInt = stmtListClone.indexOf(invoke.invokeStmt);
	//specialInvokeIndex = new Integer(specialInvokeInt);
	//sliceSet.set(specialInvokeInt);
	//}
	
	}
	}
	}
	*/
	Map changedStmtMap = new HashMap();
	BuildPDG pdg = mdInfo.methodPDG;
	Map relevantVars = mdInfo.sCriterion.relVarMap();
	Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
	Map callSiteIndexMap = SlicingMethod.callSiteIndex(callSiteMap);
	BitSet stmtIndexToNop = new BitSet(stmtListSize);
	BitSet removedStmtIndexSet = new BitSet(stmtListSize);
	StmtGraph sg = mdInfo.indexMaps.getStmtGraph();
	Set trapsHandlerSet = getTrapHandlers(jimpBody);
	SootMethod sm = mdInfo.sootMethod;
	SootClass sootClass = mdInfo.sootClass;
	Annotation annForSm = cfanns.getAnnotation(sootClass, sm);
	Vector currentCFANNs = annForSm.getAllAnnotations(true);
	keepParasOfMainMd(mdInfo, relevantVars, stmtList, sliceSet);
	Set removedStmtByEmptyBlock = new ArraySet();

	/*	
	if (mdInfo.sootClass.getName().equals("Consumer") && mdInfo.sootMethod.getName().equals("run")){
	System.out.println("Consumer.run");
	}
	*/
	removeEmptyBlock(mdInfo, removedStmtByEmptyBlock, currentCFANNs, stmtList, jimpBody);

	//get relevant variables from sliceSet
	Set relLocs = getRelevantLocals(stmtList, sliceSet, relevantVars);
	boolean changedReturnToVoid = false;
	//operating on stmtList, using stmtListClone to index
	for (int k = 0; k < stmtList.size(); k++) {
		Stmt stmt = (Stmt) stmtList.get(k);
		int stmtIndex = stmtListClone.indexOf(stmt);
		if (sliceSet.get(stmtIndex)) {
			if (thisRefIndex != null) {
				if (stmtIndex == thisRefIndex.intValue())
					continue;
			}
			if (specialInvokeIndex != null) {
				if (stmtIndex == specialInvokeIndex.intValue())
					continue;
			}
			if (stmt instanceof ReturnStmt) {

				//at this moment we only consider the return value is just
				//a local variable
				Value retVal = ((ReturnStmt) stmt).getReturnValue();
				if (!(retVal instanceof Local)) {
					if (changedReturnToVoid) {
						//change return stmt to return void stmt
						Stmt s = (Stmt) stmtList.remove(k);
						ReturnVoidStmt rtvs = Jimple.v().newReturnVoidStmt();
						try {
							cfanns.putInlineStmt(rtvs, s, mdInfo.sootMethod);
							annForSm.replaceStmt(s, rtvs);
						} catch (Exception e) {
						}
						stmtList.add(k, rtvs);
						changedStmtMap.put(stmt, rtvs);

						//change return type of the method into void
						changeReturnTypeToVoidMds.add(mdInfo.sootMethod);
					}
				} else
					if (retVal instanceof Local) {
						if (relLocs.contains(retVal)) {
						} else {
							//change return stmt to return void stmt
							Stmt s = (Stmt) stmtList.remove(k);
							ReturnVoidStmt rtvs = Jimple.v().newReturnVoidStmt();
							try {
								//annForSm.replaceStmt(s, rtvs);
								cfanns.putInlineStmt(rtvs, s, mdInfo.sootMethod);
								annForSm.replaceStmt(s, rtvs);
							} catch (Exception e) {
							}
							stmtList.add(k, rtvs);
							changedStmtMap.put(stmt, rtvs);

							//change return type of the method into void
							changeReturnTypeToVoidMds.add(mdInfo.sootMethod);
							changedReturnToVoid = true;
						}
					}
					//previous part is for method need to change return value to return void.
			} else
				if (trapsHandlerSet.contains(stmt)) {
				} else
					if (callSiteIndexMap.containsKey(stmt)) {
						callSiteInSlice(callSiteIndexMap, callSiteMap, stmt);
					} else {
						Set allRefVariablesInStmt = SlicingMethod.refVarsOf(stmt);
						//Set allVariablesInStmt = allLocalVarsOf(stmt, sg);		
						//if (SetUtil.setIntersection(allVariablesInStmt, relLocs).size() !=0 
						if (relLocs.containsAll(allRefVariablesInStmt) || (stmt instanceof ThrowStmt) || (stmt instanceof ReturnVoidStmt)) {
						} else {

							//it must be a line proposition without relvant variables
							//we can change it to nop
							//and can not be removed after refining

							//the line proposition can be checked in slicing stage, and let them be not in initial slice set, the problem is that if there is some statement on which the line propostion control depend on , so we decide to omit the line proposition statement and change it into nops

							stmtIndexToNop.set(stmtIndex);
							Stmt nopStmt = Jimple.v().newNopStmt();
							Stmt s = (Stmt) stmtList.remove(k);
							try {
								cfanns.putInlineStmt(nopStmt, s, mdInfo.sootMethod);
								annForSm.replaceStmt(s, nopStmt);
							} catch (Exception e) {
							}
							stmtList.add(k, nopStmt);
							changedStmtMap.put(stmt, nopStmt);
						}
					}
		} else {
			//stmt not in slice
			if ((stmt instanceof GotoStmt) && !removedStmtByEmptyBlock.contains(stmt)) {
			} else
				if (stmt instanceof ReturnStmt) {

					//at this moment we only consider the return value is just
					//a local variable
					Value retVal = ((ReturnStmt) stmt).getReturnValue();
					/*
					if (!(retVal instanceof Local)) {
					} else
					*/
					if (retVal instanceof Local) {
						if (!relLocs.contains(retVal)) {
							//} else {
							//change return stmt to return void stmt
							Stmt s = (Stmt) stmtList.remove(k);
							ReturnVoidStmt rtvs = Jimple.v().newReturnVoidStmt();
							try {
								cfanns.putInlineStmt(rtvs, s, mdInfo.sootMethod);
								annForSm.replaceStmt(s, rtvs);
							} catch (Exception e) {
							}
							stmtList.add(k, rtvs);
							changedStmtMap.put(stmt, rtvs);
							//stmtList.set ...

							//change return type of the method into void
							changeReturnTypeToVoidMds.add(mdInfo.sootMethod);
						}
					}
				} else
					if ((stmt instanceof ReturnVoidStmt) || (stmt instanceof RetStmt)) {
					} else {
						//change current stmt into nop if debug
						//remove current stmt from stmtList
						try {
							annForSm.removeStmt((Stmt) stmtList.remove(k));
						} catch (Exception e) {
						}
						k--;
						//remove it from source set of the jump target map
						removeSourceFor(jumpTargetMap, stmtIndex);
						//if the removing stmt is a target of some jumps, we should
						//change all the target of those jumps to the immediate post
						//dominator of the current target

						if (jumpTargetMap.containsKey(stmt)) {
							int newTargetIndex = getNewJumpTarget(stmtIndex, pdg, removedStmtIndexSet);
							//System.out.println("newTargetIndex: " + newTargetIndex);
							//System.out.println("stmt list size: " + stmtListClone.size());
							if (newTargetIndex < stmtListClone.size()) {
								//newTargetIndex = stmtListClone.size()-1;
								Stmt newTarget = (Stmt) stmtListClone.get(newTargetIndex);
								changeTarget(stmtListClone, newTarget, stmt, jumpTargetMap);
							}
						}
						removedStmtIndexSet.set(stmtIndex);
					}
		}
	}

	/*
	//* test for stmtgraph
	try {
	System.out.println("method: " + mdInfo.sootMethod);
	System.out.println("stmtList: " + stmtList);
	StmtGraph sgraph = new CompleteStmtGraph(stmtList);
	StmtBody sb = sgraph.getBody();
	List traps = sb.getTraps();
	System.out.println("traps: " + traps);
	sb.printTo(new PrintWriter(System.out));
	} catch (Throwable ee) {
	ee.printStackTrace();
	}
	*/
	modifyJumpTargetMap(stmtListClone, changedStmtMap, jumpTargetMap);
	Set useAndDefVarsInSlice = getAllVarsOf(stmtListClone, SetUtil.bitSetAndNot(sliceSet, stmtIndexToNop));

	//filering from used variable instead of relevant variabls
	filterOutFields(useAndDefVarsInSlice);
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-10 9:18:31)
 */
private void relevantExceptionClasses() {
	Set relevantExceptionClassInfo = new ArraySet();
	if (relevantExceptionClassNames.isEmpty())
		return;
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		String className = classInfo.sootClass.getName();
		if (this.relevantExceptionClassNames.contains(className))
			relevantExceptionClassInfo.add(classInfo);
	}
	/*
	if (relevantExceptionClassInfo.isEmpty())
		throw new SlicerException("Could not find any class information for all relevant exception classes.");
	if (relevantExceptionClassInfo.size() != relevantExceptionClassNames.size())
		throw new SlicerException("Could not find class information for some relevant exception classes.");
		*/
	for (Iterator classIt = relevantExceptionClassInfo.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (this.removableClasses.contains(classInfo))
			this.removableClasses.remove(classInfo);
	}
}
/**
   * Filter out the classes such that there is no method left in the class, but there are
   * some fields are relevant, i.e., accessible by other relvant classes. So those classes
   * are not removable.
   */
private void relevantFDClass() {
	List restoredClasses = new ArrayList();
	for (Iterator listIt = removableClasses.iterator(); listIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) listIt.next();
		if (Slicer.isObjectClass(classInfo.sootClass))
			continue;
		for (Iterator i = relevantFields.iterator(); i.hasNext();) {
			FieldRef oneField = (FieldRef) i.next();
			SootClass declareClass = oneField.getField().getDeclaringClass();
			if (classInfo.sootClass.equals(declareClass))
				if (!restoredClasses.contains(classInfo))
					restoredClasses.add(classInfo);
		}
	}
	removableClasses.removeAll(restoredClasses);

}
  /**
   * Remove all empty block in one method. 
   * It's including: <code> removeEmptyLock, removeEmeptyTry, removeEmptyTryFinally, 
   * removeEmptyConditionalBlock</code>.
   * <p>
   * @param mdInfo query method.
   * @param rmedStmtByEmptBlck a set of statement removed.
   * @param currentCFANNS a vector of annotations need to be analysed.
   * @param stmtList statement list.
   */
private void removeEmptyBlock(MethodInfo mdInfo, Set rmedStmtByEmptBlck, Vector currentCFANNs, StmtList stmtList, JimpleBody jimpleBody) {
	//remove empty lock pair: entermonitor and exitmonitor
	BitSet sliceSet = mdInfo.sliceSet;
	{
		LockAnalysis lockAna = mdInfo.methodPDG.getLockAnalysis();
		if (lockAna != null)
			removeEmptyLock(lockAna.getLockPairList(), sliceSet, mdInfo.sCriterion.getSlicePoints(), rmedStmtByEmptBlck, stmtList);
	}
	removeEmptyTry(currentCFANNs, sliceSet, mdInfo.sCriterion.getSlicePoints(), rmedStmtByEmptBlck,stmtList, jimpleBody);
	removeEmptyTryFinally(currentCFANNs, sliceSet, mdInfo.sCriterion.getSlicePoints(), rmedStmtByEmptBlck, stmtList, jimpleBody);
	removeEmptyConditionalBlock(currentCFANNs, sliceSet, mdInfo.sCriterion.getSlicePoints(), rmedStmtByEmptBlck, stmtList);

	//including removing empty sequential block	  
}
  /**
   * Remove empty conditional block.
   * <p>
   * @param currentCfanns a vector of annotations need to be analysed.
   * @param slcSet slice set.
   * @param linePropList a set of statement.
   * @param removedStmtByEmptyB removed statements.
   * @param stmtList statement list.
   */
private void removeEmptyConditionalBlock(Vector currentCfanns, BitSet slcSet, Set linePropList, Set removedStmtByEmptyB, StmtList stmtList) {
	for (Enumeration e = currentCfanns.elements(); e.hasMoreElements();) {
		Annotation cfann = (Annotation) e.nextElement();
		if (cfann instanceof ConditionalAnnotation) {
			ConditionalAnnotation conditionalStmtAnn = (ConditionalAnnotation) cfann;
			Set condStmts = new ArraySet(conditionalStmtAnn.getStatements());
			Set bodyStmts = new ArraySet();
			bodyStmts.addAll(condStmts);
			Set testStmts = new ArraySet(conditionalStmtAnn.getTestStatements());
			bodyStmts.removeAll(testStmts);
			if (emptyBody(bodyStmts, slcSet, stmtList)) {
				for (Iterator i = condStmts.iterator(); i.hasNext();) {
					Stmt stmt = (Stmt) i.next();
					if (linePropList.contains(stmt))
						continue;
					removedStmtByEmptyB.add(stmt);
				}
			}
		} //end of if instanceof IfStmtAnnotation
		else
			if (cfann instanceof ControlFlowAnnotation) {
				ControlFlowAnnotation controlFlowStmtAnn = (ControlFlowAnnotation) cfann;
				Stmt bodyStmts[] = controlFlowStmtAnn.getBlockAnnotation().getStatements();
				if (emptyBody(bodyStmts, slcSet, stmtList)) {
					bodyStmts = controlFlowStmtAnn.getStatements();
					for (int i = 0; i < bodyStmts.length; i++) {
						Stmt stmt = bodyStmts[i];
						if (linePropList.contains(stmt))
							continue;
						removedStmtByEmptyB.add(stmt);
					}
				}
			}

			/*
			  else if ((cfann instanceof SequentialAnnotation)
			   && (!(cfann instanceof BreakStmtAnnotation)))
			    {
			      SequentialAnnotation sequentialAnn = 
			(SequentialAnnotation) cfann;
			      Stmt bodyStmts[] = sequentialAnn.getStatements();
			
			      //printout the bodyStmts[] for debug
			      System.out.println("Print out the bodyStmts");
			      for (int ss=0; ss<bodyStmts.length; ss++)
			{
			  System.out.println(bodyStmts[ss]);
			}
			
			      System.out.println("SliceSet: " + slcSet);
			      System.out.println("emptyBody? " + emptyBody(bodyStmts, slcSet, stmtToIndex));
			
			      if (emptyBody(bodyStmts, slcSet, stmtToIndex))
			{
			  // bodyStmts = sequentialAnn.getStatements(); 
			  for (int i=0; i<bodyStmts.length; i++)
			    {
			      Index stmtIndex = (Index) stmtToIndex.get(bodyStmts[i]);
			      if (linePropList.contains(stmtIndex)) continue;
			
			      removedStmtByEmptyB.addElemToSet(stmtIndex);
			     
			      //if (slcSet.contains(stmtIndex))
			      //slcSet.remove(stmtIndex);
			      
			    }
			System.out.println("since SequentialAnn: " +removedStmtByEmptyB);      
			}	
			    }
			  */

	} //end of for enumeration
}
  /**
   * Remove empty lock.
   * <p>
   * @param lockPairList a list of {@link LockPair LockPair}.
   * @param slcSet slice set.
   * @param linePropList a set of statement.
   * @param removedStmtByEmptyB removed statements.
   * @param stmtList statement list.
   */
private void removeEmptyLock(List lockPairList, BitSet slcSet, Set linePropList, Set removedStmtByEmptyB, StmtList stmtList) {
	for (Iterator lockPairIt = lockPairList.iterator(); lockPairIt.hasNext();) {
		MonitorPair lockPair = (MonitorPair) lockPairIt.next();
		Stmt monitorStmt = (Stmt) lockPair.getEnterMonitor();
		Annotation synchroBodyAnn = lockPair.getSynchroBodyAnn();
		Stmt bodyStmts[] = synchroBodyAnn.getStatements();
		if (emptyBody(bodyStmts, slcSet, stmtList)) {
			//remove all stmt between monitor (synchronized stmt)
			monitorStmt = lockPair.getEndSynchroStmt();
			int exitIndex = stmtList.indexOf(monitorStmt);
			monitorStmt = lockPair.getBeginSynchroStmt();
			int enterIndex = stmtList.indexOf(monitorStmt);
			for (int i = enterIndex; i <= exitIndex; i++) {
				Stmt stmt = (Stmt) stmtList.get(i);
				if (linePropList.contains(stmt))
					continue;
				removedStmtByEmptyB.add(stmt);
				if (slcSet.get(i)) {
					slcSet.clear(i);
				}
			}
		} else
			
			//not empty, add exception handling into slice set
			{
			Annotation catchCFANN = lockPair.getCatchAnn();
			Stmt catchStmts[] = catchCFANN.getStatements();
			for (int i = 0; i < catchStmts.length; i++) {
				slcSet.set(stmtList.indexOf(catchStmts[i]));
			}
		}
	}
}
/**
   * Remove empty try block.
   * <p>
   * @param currentCfanns a vector of annotations need to be analysed.
   * @param slcSet slice set.
   * @param linePropList a set of statement.
   * @param removedStmtByEmptyB removed statements.
   * @param stmtList statement list.
   */
private void removeEmptyTry(Vector currentCfanns, BitSet slcSet, Set linePropList, Set removedStmtByEmptyB, StmtList stmtList, JimpleBody jimpleBody) {
	for (Enumeration e = currentCfanns.elements(); e.hasMoreElements();) {
		Annotation cfann = (Annotation) e.nextElement();
		if (cfann instanceof TryStmtAnnotation) {
			TryStmtAnnotation tryCFANN = (TryStmtAnnotation) cfann;
			Annotation tryBody = tryCFANN.getBlockAnnotation();
			Stmt bodyStmts[] = tryBody.getStatements();

			//to see if the sliceset includes any statements in bodyStms

			if (emptyBody(bodyStmts, slcSet, stmtList)) {
				Stmt allBodyStmts[] = tryCFANN.getStatements();
				for (int i = 0; i < allBodyStmts.length; i++) {
					Stmt stmt = allBodyStmts[i];
					int stmtIndex = stmtList.indexOf(stmt);
					if (linePropList.contains(stmt))
						continue;
					removedStmtByEmptyB.add(stmt);
					if (slcSet.get(stmtIndex))
						slcSet.clear(stmtIndex);
				}
				addCatchesToRemovedSet(removedStmtByEmptyB, tryCFANN.getCatchClauses(), stmtList, linePropList, slcSet);
				//remove all traps about this body
				removeTrapsOf(bodyStmts,jimpleBody);
			} else {

				//add exception handling into slice set
				//System.out.println("add catch of try");
				Vector catches = tryCFANN.getCatchClauses();
				addCatchesToSliceSet(slcSet, catches, stmtList);
			}
		}
	}
}
  /**
   * Remove empty try finally block.
   * <p>
   * @param currentCfanns a vector of annotations need to be analysed.
   * @param slcSet slice set.
   * @param linePropList a set of statement.
   * @param removedStmtByEmptyB removed statements.
   * @param stmtList statement list.
   */
private void removeEmptyTryFinally(Vector currentCfanns, BitSet slcSet, Set linePropList, Set removedStmtByEmptyB, StmtList stmtList, JimpleBody jimpleBody) {
	for (Enumeration e = currentCfanns.elements(); e.hasMoreElements();) {
		Annotation cfann = (Annotation) e.nextElement();


		if (cfann instanceof TryFinallyStmtAnnotation) {

			TryFinallyStmtAnnotation tryFinallyCFANN = (TryFinallyStmtAnnotation) cfann;
			Annotation tryBody = tryFinallyCFANN.getBlockAnnotation();
			Stmt bodyStmts[] = tryBody.getStatements();

			//to see if the sliceset includes any statements in bodyStms

			if (emptyBody(bodyStmts, slcSet, stmtList)) {
				for (int i = 0; i < bodyStmts.length; i++) {
					Stmt stmt = bodyStmts[i];
					int stmtIndex = stmtList.indexOf(stmt);
					if (linePropList.contains(stmt))
						continue;
					removedStmtByEmptyB.add(stmt);
					if (slcSet.get(stmtIndex))
						slcSet.clear(stmtIndex);
				}
				addCatchesToRemovedSet(removedStmtByEmptyB, tryFinallyCFANN.getCatchClauses(), stmtList, linePropList, slcSet);
			} else
				//add exception handling into slice set
				{
				//System.out.println("add catch of try");
				Vector catches = tryFinallyCFANN.getCatchClauses();
				addCatchesToSliceSet(slcSet, catches, stmtList);
			}

			// the fianlly part of the try statement should be included in the slice set, whether the body of the try is empty or not
			bodyStmts = tryFinallyCFANN.getFinallyAnnotation().getStatements();
			for (int i = 0; i < bodyStmts.length; i++)
				slcSet.set(stmtList.indexOf(bodyStmts[i]));
			bodyStmts = tryFinallyCFANN.getFinallyExceptionAnnotation().getStatements();
			for (int i = 0; i < bodyStmts.length; i++)
				slcSet.set(stmtList.indexOf(bodyStmts[i]));
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-20 13:56:57)
 * @param sootClass ca.mcgill.sable.soot.SootClass
 * @param sootField ca.mcgill.sable.soot.SootField
 */
private void removeFieldInClinit(SootClass sootClass, SootField sootField) {
	if (!sootClass.declaresMethod("<clinit>"))
		return;
	SootMethod sootMethod = sootClass.getMethod("<clinit>");
	JimpleBody jimpleBody = (JimpleBody) sootMethod.getBody(Jimple.v());
	StmtList stmtList = jimpleBody.getStmtList();
	Set removableStmts = new ArraySet();
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		if (stmt instanceof AssignStmt) {
			Value leftValue = ((AssignStmt) stmt).getLeftOp();
			if (leftValue instanceof FieldRef) {
				SootField leftField = ((FieldRef) leftValue).getField();
				if (leftField.equals(sootField))
					removableStmts.add(stmt);
			}
		}
	}
	if (removableStmts.isEmpty())
		return;
	Annotation annForSm = cfanns.getAnnotation(sootClass, sootMethod);
	for (Iterator stmtIt = removableStmts.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		stmtList.remove(stmt);
		try {
			annForSm.removeStmt(stmt);
		} catch (Exception e) {
		}
	}
}
/*
  private boolean isNextNextRef(Stmt s)
{
  if (! (s instanceof DefinitionStmt)) return false;

  Value rightOp = ((DefinitionStmt) s).getRightOp();

  if (rightOp instanceof NextNextStmtRef) return true;

  return false;
}
  */


// remove irrelevant fields in terms of relevant fields
/**
   * Remove irrelevant fileds for a class.
   * <p>
   * @param classInfo query class.
   */
private void removeIrrelevantFields(ClassInfo classInfo) {
	SootClass sootClass = classInfo.sootClass;
	List fields = sootClass.getFields();
	Set relevantSootFields = fieldRefToField(relevantFields);
	Set removableFields = new ArraySet();
	for (Iterator fieldsIt = fields.iterator(); fieldsIt.hasNext();) {
		SootField sootField = (SootField) fieldsIt.next();
		if (!relevantSootFields.contains(sootField))
			removableFields.add(sootField);
	}
	for (Iterator rf = removableFields.iterator(); rf.hasNext();) {
		SootField rmSootField = (SootField) rf.next();
		sootClass.removeField(rmSootField);
		removeFieldInClinit(sootClass,rmSootField);
	}

	//reset the relevant fiels set for next class
}
  /**
   * Remove one jump source from a jump target map.
   * <p>
   * @param jumpTarMap a map from target {@link Stmt Stmt} to 
   * source {@link BitSet BitSet}.
   * @param removingStmtIndex the source need to be removed.
   */
private void removeSourceFor(Map jumpTarMap, int removingStmtIndex) {
	Set removableTargets = new ArraySet();
	for (Iterator keyIt = jumpTarMap.keySet().iterator(); keyIt.hasNext();) {
		Stmt keyStmt = (Stmt) keyIt.next();
		BitSet jumpSources = (BitSet) jumpTarMap.get(keyStmt);
		if (jumpSources.get(removingStmtIndex))
			jumpSources.clear(removingStmtIndex);
		if (SetUtil.emptyBitSet(jumpSources))
			removableTargets.add(keyStmt);
	}
	for (Iterator i = removableTargets.iterator(); i.hasNext();)
		jumpTarMap.remove(i.next());
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-7 18:30:48)
 * @param bodyStmts ca.mcgill.sable.soot.jimple.Stmt[]
 * @param stmtList ca.mcgill.sable.soot.jimple.StmtList
 */
private void removeTrapsOf(Stmt[] bodyStmts, JimpleBody jimpleBody) {
	Set removableTraps = new ArraySet();
	List traps = jimpleBody.getTraps();
	Stmt firstStmtInBody = bodyStmts[0];
	Stmt lastStmtInBody = bodyStmts[bodyStmts.length - 1];
	for (Iterator trapIt = traps.iterator(); trapIt.hasNext();) {
		Trap trap = (Trap) trapIt.next();
		//if (emptyBodyTrap(bodyStmts, trap))
		Stmt beginStmt = (Stmt) trap.getBeginUnit();
		Stmt endStmt = (Stmt) trap.getEndUnit();
		if (beginStmt.equals(firstStmtInBody) && endStmt.equals(lastStmtInBody))
			removableTraps.add(trap);
	}
	for (Iterator trapIt = removableTraps.iterator(); trapIt.hasNext();) {
		Trap removableTrap = (Trap) trapIt.next();
		jimpleBody.removeTrap(removableTrap);
	}
}
/**
   * Construct residual classes.
   */
public void resClassCons() {
	Map removableMdMap = new HashMap();
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (Slicer.unreachableClasses.contains(classInfo.sootClass)) {
			removableClasses.add(classInfo);
			continue;
		}
		//if (classInfo.sootClass.getName().equals("java.lang.Object") || classInfo.sootClass.getName().equals("Object"))
		if (Slicer.isObjectClass(classInfo.sootClass))
			continue;
		List mdList = classInfo.methodsInfoList;
		MethodInfo initMethodInfo = null;
		List removableMethods = new ArrayList();
		boolean containsStartMethod = false;
		for (Iterator mdIt = mdList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			if (mdInfo.sliceSet == null) {
				if (mdInfo.sootMethod.getName().equals("<init>")) {
					SootMethod sm = mdInfo.sootMethod;
					if (sm.getParameterCount() == 0) {
						initMethodInfo = mdInfo;
					}
					removableMethods.add(mdInfo);
				} else
					if (Modifier.isAbstract(mdInfo.sootMethod.getModifiers())) {
					}
					// this else is for the abpstest examples, and will be 
					// eliminated finally
					else
						if (mdInfo.sootMethod.getName().equals("start")) {
							containsStartMethod = true;
						} else
							removableMethods.add(mdInfo);
			} else
				modifyMethod(mdInfo);
		}
		if (removableMethods.size() != 0) {
			if (removableMethods.size() == mdList.size() || (removableMethods.size() == (mdList.size() - 1) && containsStartMethod)) {
				removableClasses.add(classInfo);
			} else {
				if (initMethodInfo != null) {
					Set varsOfInit = getAllVarsOf(initMethodInfo);
					filterOutFields(varsOfInit);
					removableMethods.remove(initMethodInfo);
				}
				Set implementingMethods = new ArraySet();
				for (Iterator mdInfoIt = removableMethods.iterator(); mdInfoIt.hasNext();) {
					MethodInfo rmvableMdInfo = (MethodInfo) mdInfoIt.next();
					SootMethod rmvableMd = rmvableMdInfo.sootMethod;
					if (isImplementingOneAbstractMd(rmvableMd)) {
						implementingMethods.add(rmvableMdInfo);
						Annotation annForSm = cfanns.getAnnotation(rmvableMd.getDeclaringClass(), rmvableMd);
						makeMethodEmpty(rmvableMdInfo, annForSm);
					}
				}
				if (!implementingMethods.isEmpty())
					removableMethods.removeAll(implementingMethods);
				mdList.removeAll(removableMethods);
			}
		}

		//put removable methods list into the map
		removableMdMap.put(classInfo.sootClass, removableMethods);
	}

	//check relevantField and removableClassses to see if there
	//are any relevant fields is in one class (CLS) which is in 
	//removableClasses, where all methods in CLS are going to be
	//removed, but the fields are relevant maybe for other class.

	relevantFDClass();
	relevantExceptionClasses();
	rmMethodAndClass(removableMdMap);
	returnToVoid();
	rmIrrelevantFds();
	rmIrrelevantParasForMd();
	rmIrrelevantParasForInvoke();
}
/**
   * Construct residual parameter for a given method.
   * <p>
   * @param mdInfo query method.
   */
private void residualParameters(MethodInfo mdInfo) {
	SootMethod sootMethod = mdInfo.sootMethod;
	StmtList stmtList = mdInfo.originalStmtList; //should use the original stmtList
	StmtList slicedStmtList = mdInfo.stmtList;
	List parameterTypes = sootMethod.getParameterTypes();
	Local paraLocals[] = mdInfo.indexMaps.getParaLocalSet();
	Stmt paraIdStmt[] = mdInfo.indexMaps.getParaIdentityStmts();
	Map changedStmtMap = new HashMap();
	BitSet sliceSet = mdInfo.sliceSet;
	Map relevantVars = mdInfo.sCriterion.relVarMap();
	//Set relLocs = getRelevantLocals(stmtList, sliceSet, relevantVars);
	Set relLocs = getAllVarsOf(stmtList, sliceSet);
	Set removableParas = new ArraySet();
	LinkedList keepedParaOriginalIndex = new LinkedList();
	for (int i = 0; i < paraLocals.length; i++) {
		Local paraLocal = paraLocals[i];
		if (!relLocs.contains(paraLocal)) {
			removableParas.add(parameterTypes.get(i));
		} else {
			keepedParaOriginalIndex.addLast(new Integer(i));
		}
	}
	for (Iterator i = removableParas.iterator(); i.hasNext();) {
		Object type = (Object) i.next();
		parameterTypes.remove(type);
	}
	if (removableParas.size() > 0) {
		sootMethod.setParameterTypes(parameterTypes);
		//change parameter index
		for (int i = 0; i < keepedParaOriginalIndex.size(); i++) {
			int originalParaIndex = ((Integer) keepedParaOriginalIndex.get(i)).intValue();
			if (originalParaIndex == i)
				continue;
			Stmt stmtForThisPara = paraIdStmt[originalParaIndex];
			if (!(stmtForThisPara instanceof IdentityStmt))
				throw new NonIdParaAssignmentException("parameter assignment should be identity statement");
			IdentityStmt idStmtForThisPara = (IdentityStmt) stmtForThisPara;
			ParameterRef newParameterRef = Jimple.v().newParameterRef(sootMethod, i);
			IdentityStmt newIdStmt = Jimple.v().newIdentityStmt(idStmtForThisPara.getLeftOp(), newParameterRef);

			//change original id stmt to new id stmt
			int indexOfIdStmt = slicedStmtList.indexOf(stmtForThisPara);
			slicedStmtList.remove(indexOfIdStmt);
			slicedStmtList.add(indexOfIdStmt, newIdStmt);
			//modiy jumptarget map
			changedStmtMap.put(stmtForThisPara, newIdStmt);
		}
		modifyJumpTargetMap(stmtList, changedStmtMap, mdInfo.indexMaps.getJumpTargetMap());
	}
}
/**
   * Change return types of methods in {@link #changeReturnTypeToVoidMds changeReturnTypeToVoidMds}
   * into void.
   */
private void returnToVoid() {
	//change return type to void if necessary
	for (int i = 0; i < changeReturnTypeToVoidMds.size(); i++) {
		SootMethod sootMd = (SootMethod) changeReturnTypeToVoidMds.get(i);
		sootMd.setReturnType(VoidType.v());
	}
}
/**
   * Remove irrelevant fields in every classes.
   */
private void rmIrrelevantFds() {
	//remove irrelevant fields
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (//keepingClasses.contains(classInfo) ||
			Slicer.isObjectClass(classInfo.sootClass) || removableClasses.contains(classInfo)) {
		} else
			removeIrrelevantFields(classInfo);
	}
}
/**
   * Remove irrelevant parameters for invoke expression.
   */
private void rmIrrelevantParasForInvoke() {
	//remove irrelevant actual parameters for every invokeExpr 
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (Slicer.isObjectClass(classInfo.sootClass))
			continue;
		for (Iterator mdIt = classInfo.methodsInfoList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			if (mdInfo.sliceSet != null)
				rmParasOfInvokeExpr(mdInfo);
			JimpleBody body = (JimpleBody) mdInfo.sootMethod.getBody(Jimple.v());
			Transformations.removeUnusedLocals(body);
			cfanns.getAnnotation(mdInfo.sootClass, mdInfo.sootMethod).validate(body);
		}
	}
}
/**
   * Remove irrelevant parameters for method declaration.
   */
private void rmIrrelevantParasForMd() {
	//remove irrelevant parameters for method declaration
	Iterator classIt = classList.iterator();
	while (classIt.hasNext()) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (Slicer.isObjectClass(classInfo.sootClass))
			continue;
		List mdList = classInfo.methodsInfoList;
		Iterator mdIt = mdList.iterator();
		while (mdIt.hasNext()) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			if (mdInfo.sliceSet == null)
				continue;
			if (mdInfo.sootMethod.getName().equals("main")) {
			} else
				residualParameters(mdInfo);
			JimpleBody body = (JimpleBody) mdInfo.sootMethod.getBody(Jimple.v());
			//Transformations.removeUnusedLocals(body);
			cfanns.getAnnotation(mdInfo.sootClass, mdInfo.sootMethod).validate(body);
		}
	}
}
/**
   * Remove methods and classes in terms of given removable method map.
   * <p>
   * @param removableMdMap a map from {@link SootClass SootClass} to 
   * a {@link List List} of {@link SootMethod SootMethod}.
   */
private void rmMethodAndClass(Map removableMdMap) {
	//remove method from sootClass

	for (Iterator classIt = removableMdMap.keySet().iterator(); classIt.hasNext();) {
		SootClass sootClass = (SootClass) classIt.next();
		List removableMethods = (List) removableMdMap.get(sootClass);
		for (int i = 0; i < removableMethods.size(); i++) {
			MethodInfo rmvableMdInfo = (MethodInfo) removableMethods.get(i);
			sootClass.removeMethod(rmvableMdInfo.sootMethod);
		}
		//remove all non-reachable methods
		Set unreachableMds = new ArraySet();
		for (Iterator mdIt = sootClass.getMethods().iterator(); mdIt.hasNext();) {
			SootMethod md = (SootMethod) mdIt.next();
			if (!Slicer.reachableMethods.contains(md))
				unreachableMds.add(md);
		}
		for (Iterator unIt = unreachableMds.iterator(); unIt.hasNext();)
			sootClass.removeMethod((SootMethod) unIt.next());
	}
	if (removableClasses.size() != 0) {
		if (removableClasses.size() == classList.size())
			classList = null;
		else
			classList.removeAll(removableClasses);
	}
}
  /**
   * Remove irrelevant parameters of invoke expressions in a given method.
   * <p>
   * @param mdInfo query method.
   */
private void rmParasOfInvokeExpr(MethodInfo mdInfo) {

	StmtList originalStmtList = mdInfo.originalStmtList; //should use the original stmtList
	StmtList stmtList = mdInfo.stmtList;
	Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
	Map callSiteIndexMap = SlicingMethod.callSiteIndex(callSiteMap);
	BitSet sliceSet = mdInfo.sliceSet;
	Annotation annForSm = cfanns.getAnnotation(mdInfo.sootClass, mdInfo.sootMethod);
	for (Iterator siteIt = callSiteIndexMap.keySet().iterator(); siteIt.hasNext();) {
		Stmt callStmt = (Stmt) siteIt.next();
		if (!sliceSet.get(originalStmtList.indexOf(callStmt)))
			continue;
		if (callStmt instanceof InvokeStmt) {
			InvokeStmt invokeStmt = (InvokeStmt) callStmt;
			InvokeExpr invokeExpr = (InvokeExpr) invokeStmt.getInvokeExpr();
			InvokeExpr newInvokeExpr = buildNewInvokeExpr(invokeExpr);
			invokeStmt.setInvokeExpr(newInvokeExpr);
		} else
			if (callStmt instanceof AssignStmt) {
				AssignStmt assignCallStmt = (AssignStmt) callStmt;
				Value rightOp = assignCallStmt.getRightOp();
				if (rightOp instanceof InvokeExpr) {
					InvokeExpr newInvokeExpr = buildNewInvokeExpr((InvokeExpr) rightOp);
					SootMethod invokedMd = ((InvokeExpr) rightOp).getMethod();
					if (changeReturnTypeToVoidMds.contains(invokedMd)) {
						int indexOfCallStmt = stmtList.indexOf(callStmt);
						stmtList.remove(indexOfCallStmt);
						InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(newInvokeExpr);
						try {
							cfanns.putInlineStmt(newInvokeStmt, callStmt, mdInfo.sootMethod);
							annForSm.replaceStmt(callStmt, newInvokeStmt);
						} catch (Exception e) {
						}
						stmtList.add(indexOfCallStmt, newInvokeStmt);
					} else
						assignCallStmt.setRightOp(newInvokeExpr);
				} else {
					System.out.println("rightOp is not invokeExpr");
				}
			} else {
				System.out.println("callStmt is not invokeStmt nor DefinitionStmt");
			}
	}
}
}
