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


import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.annotation.*;
import java.util.BitSet;
/**
 * This class is for analysis of method calls including:
 * <br> (1) Build reverse direction call graph (given a method, we can know
 * which methods call that) based on call site map:
 * {@link #buildCallMeSiteMap() buildCallMeSiteMap()}.
 * <br> (2) Build PDG for each method:
 * {@link #buildMethodPDG(AnnotationManager) buildMethodPDG()}.
 * <br> (3) Collect and calculate MOD/REF information for each method:
 * {@link #MODREFAnalysis() MODREFAnalysis()}.
 */
public class MethodCallAnalysis {
	static Set directReadyForWaitCallSites;
public MethodCallAnalysis() {
  //directReadyForWaitCallSites = new ArraySet();
	Map callMeSiteMap = buildCallMeSiteMap();
	distributeToMdInfo(callMeSiteMap);
}
//to determine that all the called method has been calculated
  /**
   * See if all call sites in given method have been processed
   * according to a set of sootMethods that have been processed.
   * <p>
   * @sm sootMethod to be analysed.
   * @callSiteMap call site map from {@link CallSite CallSite}
   * to {@link SootMethod SootMethod}.
   * @param caledMdSet a set of {@link SootMethod SootMethod} which
   * has been processed.
   * @return <code>true</code> if all call sites have been processed, 
   * <code>false</code> otherwise.
   */
static  boolean allCalculated(SootMethod sm, Map callSiteMap, Set caledMdSet) {
	for (Iterator siteIt = callSiteMap.keySet().iterator(); siteIt.hasNext();) {
		CallSite site = (CallSite) siteIt.next();
		SootMethod calledMd = (SootMethod) callSiteMap.get(site);
		// the condition (calledMd==sm) is for the recursive call
		if (caledMdSet.contains(calledMd) || calledMd==sm)
			continue;
		else if (Slicer.sootMethodInfoMap.get(calledMd) == null)
		  continue;
		
		else
			return false;
	}
	return true;
}
/**
   * Update the local assignment map {@link IndexMaps#locAssIndex locAssIndex}
   * in terms of MOD information in given method information.
   * <p>
   * @param mdInfo method to be analysed.
   */
private void assignmentByMdCall(MethodInfo mdInfo)
//attach the locAssMap
{
	StmtList stmtList = mdInfo.originalStmtList;
	IndexMaps indexMaps = mdInfo.indexMaps;
	Map localAssMap = indexMaps.localAssMap();

	//assignment for static and class global instance field reference
	Fields defFields = mdInfo.MOD;
	for (Iterator i = defFields.staticFields.iterator(); i.hasNext();) {
		DataBox dbx = (DataBox) i.next();
		Stmt defStmt = dbx.getInterferStmt();
		for (Iterator j = dbx.getInterferVars().iterator(); j.hasNext();) {
			Value stcField = (Value) j.next();
			BitSet defSet = new BitSet(stmtList.size());
			defSet.set(stmtList.indexOf(defStmt));
			if (localAssMap.containsKey(stcField))
				defSet.or((BitSet) localAssMap.get(stcField));
			localAssMap.put(stcField, defSet);
		}
	}
	for (Iterator i = defFields.instanceFields.iterator(); i.hasNext();) {
		DataBox dbx = (DataBox) i.next();
		Stmt defStmt = dbx.getInterferStmt();
		for (Iterator j = dbx.getInterferVars().iterator(); j.hasNext();) {
			InstanceFieldRef insField = (InstanceFieldRef) j.next();
			BitSet defSet = new BitSet(stmtList.size());
			defSet.set(stmtList.indexOf(defStmt));
			if (localAssMap.containsKey(insField))
				defSet.or((BitSet) localAssMap.get(insField));
			localAssMap.put(insField, defSet);
		}
	}


	//assignment for parameter field 
	//and assignment for instance fields definition in called md
	Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
	for (Iterator siteIt = callSiteMap.keySet().iterator(); siteIt.hasNext();) {
		CallSite site = (CallSite) siteIt.next();
		InvokeExpr invokeExpr = site.invokeExpr;
		SootMethod calledMd = (SootMethod) callSiteMap.get(site);
		MethodInfo calledMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(calledMd);
		if (calledMdInfo == null)
			continue;
		defFields = calledMdInfo.MOD;
		Stmt callSiteStmt = site.callStmt;
		Set defParas = Fields.parametersLocalize(calledMdInfo, defFields, invokeExpr);
		Set defInstanceField = new ArraySet();
		if (invokeExpr instanceof NonStaticInvokeExpr) {
			NonStaticInvokeExpr nsie = (NonStaticInvokeExpr) invokeExpr;

			//modFields: all instanceFieldRef set
			Set modFields = SlicingMethod.allMODREFFields(defFields.instanceFields, new ArraySet());
			defInstanceField = BuildPDG.cloneAndChangeBase(modFields, nsie.getBase());
		}
		Set defFieldsAtCallSite = new ArraySet();
		defFieldsAtCallSite.addAll(defParas);
		defFieldsAtCallSite.addAll(defInstanceField);
		for (Iterator i = defFieldsAtCallSite.iterator(); i.hasNext();) {
			InstanceFieldRef field = (InstanceFieldRef) i.next();
			BitSet defSet = new BitSet(stmtList.size());
			defSet.set(stmtList.indexOf(callSiteStmt));
			if (localAssMap.containsKey(field))
				defSet.or((BitSet) localAssMap.get(field));
			localAssMap.put(field, defSet);
		}
	}
}
  /**
   * Build callme site map from call site map which is from 
   * {@link CallSite CallSite} to {@link SootMethod SootMethod}.
   * <p>
   * @return a callme site map which is from {@link SootMethod SootMethod}
   * to a {@link Set Set} of {@link CallSite CallSite}. The map means that
   * the sootMethod is called by a set of call sites.
   */
private Map buildCallMeSiteMap() {
	Map callMeSiteMap = new HashMap();
	for (Iterator mdIt = Slicer.sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) mdIt.next();
		MethodInfo mdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		if (mdInfo == null) continue;
		Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
		for (Iterator siteIt = callSiteMap.keySet().iterator(); siteIt.hasNext();) {
			CallSite site = (CallSite) siteIt.next();
			SootMethod calledMd = (SootMethod) callSiteMap.get(site);
			Set callMeSet = new ArraySet();
			callMeSet.add(site);
			if (callMeSiteMap.containsKey(calledMd)) {
				Set prevCallMeSet = (Set) callMeSiteMap.get(calledMd);
				callMeSet.addAll(prevCallMeSet);
			}
			callMeSiteMap.put(calledMd, callMeSet);
		}
	}
	return callMeSiteMap;
}
  /**
   * Build PDG for each method including:
   * <br> (1) {@link #assignmentByMdCall(MethodInfo) assignmentByMdCall()}.
   * <br> (2) {@link BuildPDG#BuildPDG(MethodInfo,AnnotationManager)
   * new BuildPDG()}.
   * <br> (3) {@link LockAnalysis@LockAnalysis(MethodInfo,AnnotationManager)
   * new LockAnalysis()}.
   * <br> (4) {@link #collectPossibleReadyDependCallSite(MethodInfo)
   * collectPossibleReadyDependCallSite()}.
   * <p>
   * @param cfanns annotation manager.
   */
void buildMethodPDG(AnnotationManager cfanns)
//including lockAnalysis()
// also including parameter fields assignment due to method call
{
	for (Iterator mdIt = Slicer.sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) mdIt.next();
		MethodInfo mdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		if (mdInfo == null) continue;
		assignmentByMdCall(mdInfo); //attach the locAssMap

		BuildPDG methodPDG = new BuildPDG(mdInfo, cfanns);
		mdInfo.methodPDG = methodPDG;
		LockAnalysis lockAnalysis = new LockAnalysis(mdInfo, cfanns);
		collectPossibleReadyDependCallSite(mdInfo);
	}
}
  /**
   * Collect all possible ready dependent call sites from given method:
   * A set of {@link CallSite CallSite} such that
   * inside the called method body there may contain
   * some ready dependence.
   * <br> Update the field {@link MethodInfo#possibleReadyDependCallSite
   * possibleReadyDependCallSite}.
   * <p>
   * @param mdInfo method to be analysed.
   */
private void collectPossibleReadyDependCallSite(MethodInfo mdInfo) {
	LockAnalysis lockAnalysis = mdInfo.methodPDG.getLockAnalysis();
	if (lockAnalysis == null)
		return;
	List waitStmtList = lockAnalysis.getWaitStmtList();
	if (waitStmtList.size() == 0)
		return;
	Set whoCallMe = mdInfo.whoCallMe;
	if ((whoCallMe == null) || (whoCallMe.size() == 0))
		return;
	List workSet = new ArrayList();
	Set visitedCallSite = new ArraySet();
	//this is for the previous version
	workSet.addAll(whoCallMe);
	while (workSet.size() != 0) {
		CallSite callSite = (CallSite) workSet.get(0);
		workSet.remove(callSite);
		if (visitedCallSite.contains(callSite))
			continue;
		visitedCallSite.add(callSite);
		MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
		if (callerMdInfo != null){
		callerMdInfo.possibleReadyDependCallSite.add(callSite);
		}
		/*
		Set callerWhoCallMe = callerMdInfo.whoCallMe;
		if ((callerWhoCallMe == null) || (callerWhoCallMe.size() == 0)) {
		} else
			workSet.addAll(callerWhoCallMe);
		*/
	}
	//this is for current version
	for (Iterator siteIt = mdInfo.whoCallMe.iterator(); siteIt.hasNext();) {
		CallSite callSite = (CallSite) siteIt.next();
		callSite.baseValue = getBaseValueFrom(callSite);
		if (callSite.baseValue != null)
			MethodCallAnalysis.directReadyForWaitCallSites.add(callSite);
	}
}
/**
 * Assign value of the field <code>whoCallMe</code> for each 
 * {@link MethodInfo MethodInfo} of each method in terms of given 
 * callme map.
 * <p>
 * @param callMeMap callme map which is from {@link SootMethod SootMethod}
 * to a {@link Set Set} of {@link CallSite CallSite}.
 */
private void distributeToMdInfo(Map callMeMap) {
	for (Iterator calledIt = callMeMap.keySet().iterator(); calledIt.hasNext();) {
		SootMethod calledMethod = (SootMethod) calledIt.next();
		Set callSites = (Set) callMeMap.get(calledMethod);
		MethodInfo mdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(calledMethod);
		if (mdInfo != null){
		mdInfo.whoCallMe = callSites;
		}
	}
}
/**
 * Get the base value from a call site of a method call.
 * <p>
 * @return the base value of the method call.
 * @param callSite call site.
 */
private Value getBaseValueFrom(CallSite callSite) {
	Value base = null;
	InvokeExpr invoke = callSite.invokeExpr;
	if (invoke instanceof NonStaticInvokeExpr)
		base = ((NonStaticInvokeExpr) invoke).getBase();
	if (base != null) {
		MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
		if (callerMdInfo != null)
			return base;
	}
	return base;
}
/**
 * Merge MOD/REF information of callee's into that of caller's.
 * <p>
 * @param mdInfo method information of caller.
 * @param calledMdInfo method information of callee.
 * @param site call site.
 */
private void mergeFields(MethodInfo mdInfo, MethodInfo calledMdInfo, CallSite site) {
	Fields MOD = mdInfo.MOD;
	Fields REF = mdInfo.REF;
	Fields MODInCalled = calledMdInfo.MOD;
	Fields REFInCalled = calledMdInfo.REF;
	MOD.merge(MODInCalled, site, mdInfo, calledMdInfo);
	REF.merge(REFInCalled, site, mdInfo, calledMdInfo);
}
/**
 * Collect and calculate MOD/REF information for each method with
 * considering method calls inside a method body.
 * <br> Update {@link MethodInfo#MOD MOD} and {@link MethodInfo#REF}
 * of each {@link MethodInfo MethodInfo} of each method.
 */
void MODREFAnalysis() {
	boolean hasNewCalculated = true;
	Set calculatedMdSet = new ArraySet();
	Map sootMethodInfoMap = Slicer.sootMethodInfoMap;
	//calculate the initial calculated method set
	for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) mdIt.next();
		MethodInfo mdInfo = (MethodInfo) sootMethodInfoMap.get(sootMethod);
		if (mdInfo== null) continue;
		Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
		if (callSiteMap.size() == 0)
			calculatedMdSet.add(sootMethod);
	}
	while (hasNewCalculated) {
		hasNewCalculated = false;
		for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
			SootMethod currentMd = (SootMethod) mdIt.next();
			MethodInfo mdInfo = (MethodInfo) sootMethodInfoMap.get(currentMd);
			if (calculatedMdSet.contains(currentMd))
				continue;
			else {
				Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
				if (allCalculated(mdInfo.sootMethod, callSiteMap, calculatedMdSet)) {
					for (Iterator siteIt = callSiteMap.keySet().iterator(); siteIt.hasNext();) {
						CallSite site = (CallSite) siteIt.next();
						SootMethod calledMd = (SootMethod) callSiteMap.get(site);
						MethodInfo calledMdInfo = (MethodInfo) sootMethodInfoMap.get(calledMd);
						if (calledMdInfo != null)
						mergeFields(mdInfo, calledMdInfo, site);
					}
					calculatedMdSet.add(currentMd);
				} else{
				  //System.out.println("method : " + mdInfo.sootMethod);
				  hasNewCalculated = true;
				}
		}
	}
}
}
}
