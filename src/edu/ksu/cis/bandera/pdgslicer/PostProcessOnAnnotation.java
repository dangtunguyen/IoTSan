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
import edu.ksu.cis.bandera.jjjc.symboltable.Name;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
/**
 * This class is for constructing residual program for
 * each sliced class and method.
 */
public class PostProcessOnAnnotation {
	/*
	public final static int UNTOUCHED = -1;
	private final static int UNREACHABLE = -2;
	private final static int REMAINED = 0;
	private final static int SLICED = 1;
	private final static int STMT_RETURN_TO_VOID = 2;
	private final static int STMT_PARAMETER_CHANGED = 3;
	private final static int MD_DEC_RETURN_TO_VOID = 4;
	private final static int MD_DEC_PARAMETER_CHANGED = 5;
	private final static int LOCAL_DEC_CHANGED = 6;
	*/
	private Map modifiedSootMethodToRemovableLocals = new HashMap();
	private Set removableSootMethods = new ArraySet();
	private Set unreachableSootMethods = new ArraySet();
	private Set unreachableSootClasses = Slicer.unreachableClasses;
	private Set removableSootFields = new ArraySet();
	private Set parameterModifiedSootMethods = new ArraySet();
	/**
	   * A list of {@link ClassInfo ClassInfo} for each class.
	   */
	private List classList;
	/**
	   * A list of {@link ClassInfo ClassInfo} which is sliced away.
	   */
	private Set removableSootClasses = new ArraySet(); //it's including unreachable sootclasses
	/**
	   * A list of {@link SootMethod SootMethod} such that
	   * return value should be changed to return void due to
	   * the irrelevance of return value.
	   */
	private List changeReturnTypeToVoidMds = new ArrayList();
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
public PostProcessOnAnnotation(List ct, AnnotationManager cfs) {
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
private boolean buildNewInvokeExpr(InvokeExpr invokeExpr) {
	SootMethod invokedMethod = invokeExpr.getMethod();
	if (parameterModifiedSootMethods.contains(invokedMethod))
		return true;
	return false;
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
		/*
		if (keepedParameters.size() < argCount) {
			for (int i = 0; i < keepedParameters.size(); i++)
				invokeExpr.setArg(i, (Value) keepedParameters.get(i));
		}
		*/
	}
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
 * Insert the method's description here.
 * Creation date: (00-12-7 10:58:43)
 * @return ca.mcgill.sable.util.Set
 * @param packageName java.lang.String
 */
private Set getAllClassesFor(Package pckg) {
	Set classes = new ArraySet();
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		SootClass sootClass = ((ClassInfo) classIt.next()).sootClass;
		Name cName = new Name(sootClass.getName());
		if (cName.getSuperName().equals(pckg.getName()))
			classes.add(sootClass);
	}
	return classes;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-7 10:42:45)
 * @return ca.mcgill.sable.util.Set
 */
private Set getAllPackages() {
	Set packages = new ArraySet();
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		SootClass sootClass = ((ClassInfo) classIt.next()).sootClass;
		Name pn = new Name(sootClass.getName()).getSuperName();
		Package pk = null;
		try {
			pk = Package.getPackage(pn);
		} catch (Exception e) {
		}
		if (pk != null)
			packages.add(pk);
	}
	return packages;
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
		allVars.addAll(PostProcess.allVarsOf(stmt));
	}
	return allVars;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 15:23:43)
 * @return ca.mcgill.sable.util.Set
 */
private Set getCurrentResidualClasses() {
	Set classSet = new ArraySet();
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		SootClass sootClass = ((ClassInfo) classIt.next()).sootClass;
		if (!removableSootClasses.contains(sootClass))
			classSet.add(sootClass);
	}
	return classSet;
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
	Set relLocs = PostProcess.getRelevantLocals(mdInfo.originalStmtList, mdInfo.sliceSet, relevantVars);
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
 * Insert the method's description here.
 * Creation date: (00-12-7 10:13:23)
 * @return ca.mcgill.sable.util.Set
 */
public Set getModifiedPackages() {
	Set packages = new ArraySet();
	Set removablePackages = getRemovablePackages();
	Set modifiedSootClasses = getModifiedSootClasses();
	for (Iterator pIt = getAllPackages().iterator(); pIt.hasNext();) {
		Package pck = (Package) pIt.next();
		if (removablePackages.contains(pck))
			continue;
		for (Iterator classIt = getAllClassesFor(pck).iterator(); classIt.hasNext();) {
			SootClass sootClass = (SootClass) classIt.next();
			if (this.removableSootClasses.contains(sootClass) || modifiedSootClasses.contains(sootClass)) {
				packages.add(pck);
				break;
			}
		}
	}
	return packages;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-7 10:12:44)
 * @return ca.mcgill.sable.util.Set
 */
public Set getModifiedSootClasses() {
	Set modifiedClasses = new ArraySet();
	Set modifiedSootMethods = this.getModifiedSootMethods();
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		SootClass sootClass = ((ClassInfo) classIt.next()).sootClass;
		if (this.removableSootClasses.contains(sootClass))
			continue;
		for (Iterator mdIt = sootClass.getMethods().iterator(); mdIt.hasNext();) {
			SootMethod sm = (SootMethod) mdIt.next();
			if (this.removableSootMethods.contains(sm) || this.unreachableSootMethods.contains(sm) || modifiedSootMethods.contains(sm))
				modifiedClasses.add(sootClass);
		}
	}
	return modifiedClasses;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 16:55:43)
 * @return ca.mcgill.sable.util.Set
 */
public Set getModifiedSootMethods() {
	return modifiedSootMethodToRemovableLocals.keySet();
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-18 11:54:07)
 * @return ca.mcgill.sable.util.Set
 */
public Set getParameterModifiedMethods() {
	return parameterModifiedSootMethods;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 15:38:21)
 * @return ca.mcgill.sable.util.Set
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
public Set getRemovableLocals(SootMethod sm) {
	return (ArraySet) modifiedSootMethodToRemovableLocals.get(sm);
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-7 10:13:55)
 * @return ca.mcgill.sable.util.Set
 */
public Set getRemovablePackages() {
	Set packages = new ArraySet();
	for (Iterator pIt = getAllPackages().iterator(); pIt.hasNext();) {
		Package pck = (Package) pIt.next();
		boolean removable = true;
		for (Iterator classIt = getAllClassesFor(pck).iterator(); classIt.hasNext();) {
			SootClass sootClass = (SootClass) classIt.next();
			if (!this.removableSootClasses.contains(sootClass)) {
				removable = false;
				break;
			}
		}
		if (removable)
			packages.add(pck);
	}
	return packages;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 17:10:18)
 * @return ca.mcgill.sable.util.Set
 */
public Set getRemovableSootClasses() {
	return removableSootClasses;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 15:33:00)
 * @return ca.mcgill.sable.util.Set
 */
public Set getRemovableSootFields() {
	return removableSootFields;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 15:27:28)
 * @return ca.mcgill.sable.util.Set
 */
public Set getRemovableSootMethods() {
	return removableSootMethods;
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
 * Creation date: (00-12-6 15:32:10)
 * @return ca.mcgill.sable.util.Set
 */
public Set getUnreachableSootClasses() {
	return unreachableSootClasses;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 15:31:31)
 * @return ca.mcgill.sable.util.Set
 */
public Set getUnreachableSootMethods() {
	return unreachableSootMethods;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 14:54:27)
 * @return ca.mcgill.sable.util.Set
 * @param jimpleBody ca.mcgill.sable.soot.jimple.JimpleBody
 */
private Set getUnusedLocals(MethodInfo mdInfo) {
	StmtList stmtList = mdInfo.stmtList;
	Set removedStmt = mdInfo.removedStmt;
	JimpleBody listBody = (JimpleBody) mdInfo.sootMethod.getBody(Jimple.v());
	Set unusedLocals = new ArraySet();
	// Set all locals as unused
	unusedLocals.addAll(listBody.getLocals());

	// Traverse statements noting all the uses
	{
		Iterator stmtIt = stmtList.iterator();
		while (stmtIt.hasNext()) {
			Stmt s = (Stmt) stmtIt.next();
			if (removedStmt.contains(s))
				continue;
			// Remove all locals in defBoxes from unusedLocals
			{
				Iterator boxIt = s.getDefBoxes().iterator();
				while (boxIt.hasNext()) {
					Value value = ((ValueBox) boxIt.next()).getValue();
					if (value instanceof Local && unusedLocals.contains(value))
						unusedLocals.remove(value);
				}
			}

			// Remove all locals in useBoxes from unusedLocals
			{
				Iterator boxIt = s.getUseBoxes().iterator();
				while (boxIt.hasNext()) {
					Value value = ((ValueBox) boxIt.next()).getValue();
					if (value instanceof Local && unusedLocals.contains(value))
						unusedLocals.remove(value);
				}
			}
		}
	}
	return unusedLocals;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-7 18:06:38)
 */
private void initializeMdInfoAndAnnotation() {
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (Slicer.unreachableClasses.contains(classInfo.sootClass))
			continue;
		if (Slicer.isObjectClass(classInfo.sootClass))
			continue;
		List mdList = classInfo.methodsInfoList;
		for (Iterator mdIt = mdList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			mdInfo.removedStmt = new ArraySet();
			Annotation annForSm = cfanns.getAnnotation(mdInfo.sootClass, mdInfo.sootMethod);
			Vector currentCFANNs = annForSm.getAllAnnotations(true);
			for (Enumeration annEnum = currentCFANNs.elements(); annEnum.hasMoreElements();) {
				Annotation cfann = (Annotation) annEnum.nextElement();
				cfann.initializeState();
			}
		}
	}
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
			Stmt sss = (Stmt) stmtIt.next();
			annForSm.removeStmtByMark(sss);
			methodInfo.removedStmt.add(sss);
		} catch (Exception e) {
		}
	}
	/*
	//remove statement from statement list
	if (!removableStmts.isEmpty())
	for (Iterator stmtIt = removableStmts.iterator(); stmtIt.hasNext();)
	methodInfo.stmtList.remove(stmtIt.next());
	*/
	//change assignment statement $ret=value into $ret=null;
	if (ret$Stmt != null) {
		NullConstant nullCons = NullConstant.v();
		Value leftOp = ((AssignStmt) ret$Stmt).getLeftOp();
		AssignStmt newRetAssign = Jimple.v().newAssignStmt(leftOp, nullCons);
		//int k = methodInfo.stmtList.indexOf(ret$Stmt);
		//methodInfo.stmtList.remove(k);
		try {
			//cfanns.putInlineStmt(newRetAssign, ret$Stmt, methodInfo.sootMethod);
			annForSm.replaceStmtByMark(ret$Stmt, newRetAssign);
		} catch (Exception e) {
		}
		//methodInfo.stmtList.add(k, newRetAssign);
	}
}
/**
   * Construct residual method.
   * <p>
   * @param mdInfo method to be constructed.
   */
private boolean modifyMethod(MethodInfo mdInfo) {
	boolean modified = false;
	JimpleBody jimpBody = (JimpleBody) mdInfo.sootMethod.getBody(Jimple.v());
	StmtList stmtList = mdInfo.stmtList;
	StmtList stmtListClone = mdInfo.originalStmtList;
	BitSet sliceSet = mdInfo.sliceSet;
	int stmtListSize = stmtListClone.size();
	Integer thisRefIndex = mdInfo.indexMaps.getThisRefStmtIndex();
	Integer specialInvokeIndex = null;

	//add for every instance method this reference stmt to initSliceSet required by Robby 1/7/2000

	if (thisRefIndex != null)
		sliceSet.set(thisRefIndex.intValue());
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
	removeEmptyBlock(mdInfo, removedStmtByEmptyBlock, currentCFANNs, stmtList, jimpBody);

	//get relevant variables from sliceSet
	Set relLocs = PostProcess.getRelevantLocals(stmtList, sliceSet, relevantVars);
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

						Stmt s = (Stmt) stmtList.get(k);
						ReturnVoidStmt rtvs = Jimple.v().newReturnVoidStmt();
						try {
							annForSm.replaceStmtByMark(s, rtvs);
							modified = true;
						} catch (Exception e) {
						}


						//change return type of the method into void
						changeReturnTypeToVoidMds.add(mdInfo.sootMethod);
					}
				} else
					if (retVal instanceof Local) {
						if (relLocs.contains(retVal)) {
						} else {
							//change return stmt to return void stmt

							Stmt s = (Stmt) stmtList.get(k);
							ReturnVoidStmt rtvs = Jimple.v().newReturnVoidStmt();
							try {
								annForSm.replaceStmtByMark(s, rtvs);
								modified = true;
							} catch (Exception e) {
							}


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
							//Stmt s = (Stmt) stmtList.remove(k);
							Stmt s = (Stmt) stmtList.get(k);
							try {
								annForSm.replaceStmtByMark(s, nopStmt);
								modified = true;
							} catch (Exception e) {
							}
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
					if (retVal instanceof Local) {
						if (!relLocs.contains(retVal)) {

							//change return stmt to return void stmt

							Stmt s = (Stmt) stmtList.get(k);
							ReturnVoidStmt rtvs = Jimple.v().newReturnVoidStmt();
							try {
								annForSm.replaceStmtByMark(s, rtvs);
								modified = true;
							} catch (Exception e) {
							}

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
							annForSm.removeStmtByMark((Stmt) stmtList.get(k));
							mdInfo.removedStmt.add(stmtList.get(k));
							modified = true;
						} catch (Exception e) {
						}
						removedStmtIndexSet.set(stmtIndex);
					}
		}
	}
	Set useAndDefVarsInSlice = PostProcess.getAllVarsOf(stmtListClone, SetUtil.bitSetAndNot(sliceSet, stmtIndexToNop));

	//filering from used variable instead of relevant variabls
	filterOutFields(useAndDefVarsInSlice);
	return modified;
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
	for (Iterator classIt = relevantExceptionClassInfo.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (this.removableSootClasses.contains(classInfo.sootClass))
			this.removableSootClasses.remove(classInfo.sootClass);
	}
}
/**
   * Filter out the classes such that there is no method left in the class, but there are
   * some fields are relevant, i.e., accessible by other relvant classes. So those classes
   * are not removable.
   */
private void relevantFDClass() {
	Set restoredClasses = new ArraySet();
	for (Iterator listIt = removableSootClasses.iterator(); listIt.hasNext();) {
		SootClass sootClass = (SootClass) listIt.next();
		if (Slicer.isObjectClass(sootClass))
			continue;
		for (Iterator i = relevantFields.iterator(); i.hasNext();) {
			FieldRef oneField = (FieldRef) i.next();
			SootClass declareClass = oneField.getField().getDeclaringClass();
			if (sootClass.equals(declareClass))
				restoredClasses.add(sootClass);
		}
	}
	removableSootClasses.removeAll(restoredClasses);
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 15:16:27)
 */
private void relevantInterfaceAndSuperClasses() {
	Set currentResidualClasses = getCurrentResidualClasses();
	Set unremovableClasses = new ArraySet();
	for (Iterator classIt = removableSootClasses.iterator(); classIt.hasNext();) {
		SootClass sootClass = (SootClass) classIt.next();
		if (!Slicer.isRemovableClass(sootClass, currentResidualClasses))
			unremovableClasses.add(sootClass);
	}
	if (!unremovableClasses.isEmpty())
		removableSootClasses.removeAll(unremovableClasses);
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
				//removeTrapsOf(bodyStmts,jimpleBody);
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
			annForSm.removeStmtByMark(stmt);
		} catch (Exception e) {
		}
	}
}
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
	removableSootFields = new ArraySet();
	for (Iterator fieldsIt = fields.iterator(); fieldsIt.hasNext();) {
		SootField sootField = (SootField) fieldsIt.next();
		if (!relevantSootFields.contains(sootField)) {
			removableSootFields.add(sootField);
			removeFieldInClinit(sootClass, sootField);
		}
	}
}
/**
   * Construct residual classes.
   */
public void resClassCons() {
	initializeMdInfoAndAnnotation();
	Map removableMdMap = new HashMap();
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (Slicer.unreachableClasses.contains(classInfo.sootClass)) {
			removableSootClasses.add(classInfo.sootClass);
			continue;
		}
		if (Slicer.isObjectClass(classInfo.sootClass))
			continue;
		List mdList = classInfo.methodsInfoList;
		MethodInfo initMethodInfo = null;
		List removableMethods = new ArrayList();
		boolean containsStartMethod = false;
		for (Iterator mdIt = mdList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			/*
			if (mdInfo.sootClass.getName().equals("Thread") && mdInfo.sootMethod.getName().equals("stopChargingCPUTime")) {
			System.out.println("for debug");
			}
			*/
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
			} else {
				boolean modified = modifyMethod(mdInfo);
				if (modified)
					modifiedSootMethodToRemovableLocals.put(mdInfo.sootMethod, new ArraySet());
			}
		}
		if (removableMethods.size() != 0) {
			if (removableMethods.size() == mdList.size() || (removableMethods.size() == (mdList.size() - 1) && containsStartMethod)) {
				removableSootClasses.add(classInfo.sootClass);
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
						modifiedSootMethodToRemovableLocals.put(rmvableMdInfo.sootMethod, new ArraySet());
					}
				}
				if (!implementingMethods.isEmpty())
					removableMethods.removeAll(implementingMethods);
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
	
	rmIrrelevantFds();
	rmIrrelevantParasForMd();
	rmIrrelevantParasForInvoke();
	rmUnusedLocals();
	relevantInterfaceAndSuperClasses();
}
/**
   * Construct residual parameter for a given method.
   * <p>
   * @param mdInfo query method.
   */
private void residualParameters(MethodInfo mdInfo) {
	SootMethod sootMethod = mdInfo.sootMethod;
	StmtList stmtList = mdInfo.originalStmtList; //should use the original stmtList
	Annotation annForSm = cfanns.getAnnotation(mdInfo.sootClass, mdInfo.sootMethod);
	StmtList slicedStmtList = mdInfo.stmtList;
	List parameterTypes = sootMethod.getParameterTypes();
	Local paraLocals[] = mdInfo.indexMaps.getParaLocalSet();
	Stmt paraIdStmt[] = mdInfo.indexMaps.getParaIdentityStmts();
	BitSet sliceSet = mdInfo.sliceSet;
	Map relevantVars = mdInfo.sCriterion.relVarMap();
	Set relLocs = PostProcess.getAllVarsOf(stmtList, sliceSet);
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
	}
	if (removableParas.size() > 0) {
		parameterModifiedSootMethods.add(sootMethod);
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
			try {
				annForSm.replaceStmtByMark(idStmtForThisPara, newIdStmt);
				modifiedSootMethodToRemovableLocals.put(mdInfo.sootMethod, new ArraySet());
			} catch (Exception e) {
			}
		}
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
			Slicer.isObjectClass(classInfo.sootClass) || removableSootClasses.contains(classInfo.sootClass)) {
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
		if (Slicer.isObjectClass(classInfo.sootClass) || removableSootClasses.contains(classInfo.sootClass))
			continue;
		for (Iterator mdIt = classInfo.methodsInfoList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			if (mdInfo.sliceSet != null)
				rmParasOfInvokeExpr(mdInfo);
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
		if (Slicer.isObjectClass(classInfo.sootClass) || removableSootClasses.contains(classInfo.sootClass))
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
			removableSootMethods.add(rmvableMdInfo.sootMethod);
		}
		for (Iterator mdIt = sootClass.getMethods().iterator(); mdIt.hasNext();) {
			SootMethod md = (SootMethod) mdIt.next();
			if (!Slicer.reachableMethods.contains(md))
				unreachableSootMethods.add(md);
		}
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
			boolean paraChanged = buildNewInvokeExpr(invokeExpr);
			if (paraChanged) {
				try {
					annForSm.replaceStmtByMark(invokeStmt, invokeStmt);
					modifiedSootMethodToRemovableLocals.put(mdInfo.sootMethod, new ArraySet());
				} catch (Exception e) {
				}
			}
		} else
			if (callStmt instanceof AssignStmt) {
				AssignStmt assignCallStmt = (AssignStmt) callStmt;
				Value rightOp = assignCallStmt.getRightOp();
				if (rightOp instanceof InvokeExpr) {
					boolean paraChanged = buildNewInvokeExpr((InvokeExpr) rightOp);
					SootMethod invokedMd = ((InvokeExpr) rightOp).getMethod();
					if (changeReturnTypeToVoidMds.contains(invokedMd)) {
						int indexOfCallStmt = stmtList.indexOf(callStmt);
						//InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(newInvokeExpr);
						try {
							annForSm.replaceStmtByMark(callStmt, callStmt);
							modifiedSootMethodToRemovableLocals.put(mdInfo.sootMethod, new ArraySet());
						} catch (Exception e) {
						}
					} else {
						if (paraChanged) {
							try {
								annForSm.replaceStmtByMark(callStmt, callStmt);
								modifiedSootMethodToRemovableLocals.put(mdInfo.sootMethod, new ArraySet());
							} catch (Exception e) {
							}
						}
					}
				} else {
					System.out.println("rightOp is not invokeExpr");
				}
			} else {
				System.out.println("callStmt is not invokeStmt nor DefinitionStmt");
			}
	}
}
/**
   * Remove irrelevant parameters for invoke expression.
   */
private void rmUnusedLocals() {
	//remove irrelevant actual parameters for every invokeExpr 
	for (Iterator classIt = classList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		if (Slicer.isObjectClass(classInfo.sootClass) || removableSootClasses.contains(classInfo.sootClass))
			continue;
		for (Iterator mdIt = classInfo.methodsInfoList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			Set unusedLocals = getUnusedLocals(mdInfo);
			if (!unusedLocals.isEmpty())
				modifiedSootMethodToRemovableLocals.put(mdInfo.sootMethod, unusedLocals);
		}
	}
}
}
