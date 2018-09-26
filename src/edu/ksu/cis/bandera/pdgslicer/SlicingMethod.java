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


//if (!(nodeStmt instanceof InvokeStmt))
//add this condition on 10/19/99  in slicingMethod() and
//slicingMethodAgain()


import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import edu.ksu.cis.bandera.pdgslicer.exceptions.*;
import java.util.BitSet;
import java.util.Hashtable;
//import java.util.HashSet;
/**
 * This class is for slicing one method
 */
public class SlicingMethod {
	private StmtList stmtList;
	private int stmtListSize;
	private IndexMaps indexMaps;
	MethodInfo methodInfo;
	static boolean criterionChanged;
  /**
   * A {@link Set Set} of {@link MethodInfo MethodInfo} such that some 
   * statement in other method ready dependent on one statement in the method
   */
	private Set originalMethodsFromReady = new ArraySet();
  /*
   * A {@link Set Set} of {@link MethodInfo MethodInfo} such that contains
   * <code>wait</code> statement.
   */
  //static Set originalMdsFromReadyCallSite;
  /**
   * A {@link Set Set} of {@link CallSite CallSite} which has been generated slice
   * criterion because of ready call site.
   */
	static Set alreadyGenCritByReadyCallSite;
	//static Set alreadyGenerateCriterionByWhoCallMe = new ArraySet();
  /**
   * A {@link Set Set} of {@link MethodInfo MethodInfo} such that has been generated
   * slice criterion for exit of the method.
   */
	static Set alreadyGenerateCriterionForExits;
  /**
   * @param mdInfo method to be sliced.
   */
public SlicingMethod(MethodInfo mdInfo) {
	methodInfo = mdInfo;
	stmtList = methodInfo.originalStmtList;
	stmtListSize = stmtList.size();
	indexMaps = methodInfo.indexMaps;
}
private void addCalledMdToWorkList(MethodInfo targetMdInfo, LinkedList workList, Set calculated) {
	Map callSiteMap = targetMdInfo.indexMaps.getCallSiteMap();
	Set calledMdsInfo = new ArraySet();
	for (Iterator calledIt = callSiteMap.values().iterator(); calledIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) calledIt.next();
		MethodInfo calledMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		if (!calculated.contains(calledMdInfo))
			calledMdsInfo.add(calledMdInfo);
	}
	if (!calledMdsInfo.isEmpty())
		workList.addAll(workList.size(), calledMdsInfo);
}
  /*
   * Add all called methods in a given method into a work list.
   * <p>
   * @param targetMdInfo method to be analysed.
   * @param workList a list of {@link MethodInfo MethodInfo} which is called
   * in <code>targetMdInfo</code>.
   * @param calculated a set of {@link MethodInfo MethodInfo} which is already calculated.
   */
  /*
private void addCalledMdToWorkList(MethodInfo targetMdInfo, LinkedList workList, Set calculated) {
	Map callSiteMap = targetMdInfo.indexMaps.getCallSiteMap();
	Set calledMdsInfo = new ArraySet();
	for (Iterator calledIt = callSiteMap.values().iterator(); calledIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) calledIt.next();
		MethodInfo calledMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		if (calledMdInfo == null) continue;
		if (!calculated.contains(calledMdInfo))
			calledMdsInfo.add(calledMdInfo);
	}
	if (!calledMdsInfo.isEmpty())
		workList.addAll(workList.size(), calledMdsInfo);
}
  */
  /*
private void addCallersMdToWorkList(MethodInfo targetMdInfo, LinkedList workList, Set calculated) {
	Set callSites = targetMdInfo.whoCallMe;
	if (callSites==null) return;
	Set callerMdsInfo = new ArraySet();
	for (Iterator callerIt = callSites.iterator(); callerIt.hasNext();) {
	  CallSite callsite = (CallSite) callerIt.next();
		SootMethod sootMethod = callsite.callerSootMethod;
		MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		if (callerMdInfo == null) continue;
		if (!calculated.contains(callerMdInfo))
			callerMdsInfo.add(callerMdInfo);
	}
	if (!callerMdsInfo.isEmpty())
		workList.addAll(workList.size(), callerMdsInfo);
}
  */

private void addToSliceTrace(Set stmtSet, Set callSites, Integer kind, MethodInfo mdin) {
	Set traceNodeSet = getTraceNodeSetFrom(stmtSet, mdin);
	if (traceNodeSet.isEmpty())
		addToSliceTrace(Slicer.sliceTraceRoot, callSites, kind, mdin);
	else {
		for (Iterator nodeIt = traceNodeSet.iterator(); nodeIt.hasNext();) {
			SliceTraceNode stn = (SliceTraceNode) nodeIt.next();
			addToSliceTrace(stn, callSites, kind, mdin);
		}
	}
}
private void addToSliceTrace(Set stmtSet, BitSet assgnSet, Integer kind, MethodInfo mdin) {
	Set traceNodeSet = getTraceNodeSetFrom(stmtSet, mdin);
	if (traceNodeSet.isEmpty())
		addToSliceTrace(Slicer.sliceTraceRoot, assgnSet, kind, mdin);
	else {
		for (Iterator nodeIt = traceNodeSet.iterator(); nodeIt.hasNext();) {
			SliceTraceNode stn = (SliceTraceNode) nodeIt.next();
			addToSliceTrace(stn, assgnSet, kind, mdin);
		}
	}
}
private void addToSliceTrace(SliceTraceNode stn, Stmt stmt, Integer kind, MethodInfo mdin) {
	SliceTraceNode traceNode = new SliceTraceNode(mdin, stmt);
	/*
	if (traceNode.stmtAnnotation.toString().equals("")) {
		System.out.println("stmt: " + stmt +" 's annotation hashcode : " + traceNode.stmtAnnotation.hashCode());
		return;
		}
		*/
	SliceTraceNode containNode = sliceTraceContains(traceNode);
	if (containNode == null) {
		stn.add(traceNode, kind);
		Slicer.allSliceTraceNodes.add(traceNode);
		return;
	}
	if (stn.equals(containNode) || stn.isChild(containNode))
		return;
	stn.add(containNode, kind);
}
private void addToSliceTrace(SliceTraceNode stn, LinkedList stmts, Integer kind, MethodInfo mdin) {
	for (Iterator stmtIt = stmts.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		addToSliceTrace(stn, stmt, kind, mdin);
	}
}
private void addToSliceTrace(SliceTraceNode stn, Set stmtSet, Integer kind, MethodInfo mdin) {
	for (Iterator stmtIt = stmtSet.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		addToSliceTrace(stn, stmt, kind, mdin);
	}
}
private void addToSliceTrace(SliceTraceNode stn, BitSet stmtBitSet, Integer kind, MethodInfo mdin) {
	Set stmtSet = SetUtil.bitSetToStmtSet(stmtBitSet, stmtList);
	addToSliceTrace(stn, stmtSet, kind, mdin);
}
  /**
   * Add a set of statements into a work list.
   * <p>
   * @param wkList work list.
   * @param nodes a set of statements.
   * @param calculatedNodes a set of statements which should not be added into the 
   * work list.
   */
private void addToWorkList(LinkedList wkList, BitSet nodes, Set calculatedNodes) {
	for (int i = 0; i < nodes.size(); i++) {
		if (nodes.get(i)) {
			Stmt nodeStmt = (Stmt) stmtList.get(i);
			if (!wkList.contains(nodeStmt) && !calculatedNodes.contains(nodeStmt))
				wkList.addLast(nodeStmt);
		}
	}
}
  /**
   * Get all local variables in one statement.
   * <p>
   * @param stmt query statement.
   * @return a set of {@link Value Value}.
   */
static Set allLocalVarsOf(Stmt stmt) {
	Set buff = refVarsOf(stmt);
	buff.addAll(defVarsOf(stmt));
	return buff;
}
  /**
   * Get all MOD and REF fields from given sets.
   * <p>
   * @param modField a set of {@link Value Value} which is in MOD set.
   * @param refField a set of {@link Value Value} which is in REF set.
   * @return a set of {@link Value Value}.
   */
static Set allMODREFFields(Set modFields, Set refFields) {
	Set fields = new ArraySet();
	for (Iterator fdIt = modFields.iterator(); fdIt.hasNext();) {
		DataBox dbx = (DataBox) fdIt.next();
		fields.addAll(dbx.getInterferVars());
	}
	for (Iterator fdIt = refFields.iterator(); fdIt.hasNext();) {
		DataBox dbx = (DataBox) fdIt.next();
		fields.addAll(dbx.getInterferVars());
	}
	return fields;
}
  /**
   * Get all assignments to a list of varibles.
   * <p>
   * @param varList a list variables.
   * @param locAssMap a map from local to assignment, 
   * see {@link IndexMaps#locAssIndex IndexMaps.locAssIndex}.
   * @param relVarMap relevant variable map, 
   * see {@link SliceCriterion#relVarMap SliceCriterion.relVarMap}.
   * @return a set of assignments.
   */
private BitSet assToRelVars(Set varList, Map locAssMap, Map relVarMap) {
	BitSet initSet = new BitSet(stmtListSize);
	for (Iterator varIt = varList.iterator(); varIt.hasNext();) {
		Object var =  varIt.next();
		if (locAssMap.containsKey(var)) {
			BitSet assToVar = (BitSet) locAssMap.get(var);
			initSet.or(assToVar);
			putToRelVarMap(var, assToVar, relVarMap);
		}
	}
	return initSet;
}
  /**
   * Get all assignments to a list of varibles without considering 
   * relevant variables.
   * <p>
   * @param varList a list variables.
   * @param locAssMap a map from local to assignment, 
   * see {@link IndexMaps#locAssIndex IndexMaps.locAssIndex}.
   * @return a set of assignments.
   */
private  BitSet assToVarsNoRelVars(int stmtListSize, List varList, Map locAssMap) {
	BitSet initSet = new BitSet(stmtListSize);
	for (Iterator varIt = varList.iterator(); varIt.hasNext();) {
		Object var =  varIt.next();
		if (locAssMap.containsKey(var)) {
			BitSet assToVar = (BitSet) locAssMap.get(var);
			initSet.or(assToVar);
		}
	}
	return initSet;
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-20 19:24:31)
 */
private boolean callerIsRelevant(CallSite callSite) {
	Map methodInfoMap = Slicer.sootMethodInfoMap;
	for (Iterator mdIt = methodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		SootMethod sm = (SootMethod) mdIt.next();
		MethodInfo callerMdInfo = (MethodInfo) methodInfoMap.get(sm);
		if (callerMdInfo == null)
			continue;
		BitSet callerSliceSet = callerMdInfo.sliceSet;
		if (callerSliceSet == null)
			continue;
		StmtList callerStmtList = callerMdInfo.originalStmtList;
		//Map callerRelevantMap = callerMdInfo.sCriterion.relVarMap();
		Value callerBaseValue = callSite.baseValue;
		if (callerBaseValue != null) {
			Set relVarsInCaller = PostProcess.getAllVarsOf(callerStmtList, callerSliceSet);
			if (relVarsInCaller.contains(callerBaseValue))
				return true;
			else
				continue;
		}
	}
	return false;
}
  /*
private boolean callerIsRelevant(CallSite callSite, MethodInfo callerMdInfo)
	{
	  BitSet callerSliceSet = callerMdInfo.sliceSet;
	  if (callerSliceSet == null) return false;
	  StmtList callerStmtList = callerMdInfo.originalStmtList;
	  Map callerRelevantMap = callerMdInfo.sCriterion.relVarMap();

	  Value callerBaseValue = callSite.baseValue;
	  if (callerBaseValue!=null)
	{
	  Set relVarsInCaller = PostProcess.getRelevantLocals(callerStmtList,callerSliceSet, callerRelevantMap);
	  if (relVarsInCaller.contains(callerBaseValue)) return true;
	  else return false;
	}
	  return false;
	}
  */
  /**
   * Get call sites indexed by call statements.
   * <p>
   * @param callSiteMap call site map,
   * see {@link IndexMaps#callSiteMap IndexMaps.callSiteMap}.
   * @return a map from {@link Stmt Stmt} to {@link CallSite CallSite}.
   */
static Map callSiteIndex(Map callSiteMap) {
	Map indexMap = new HashMap();
	for (Iterator keyIt = callSiteMap.keySet().iterator(); keyIt.hasNext();) {
	CallSite callSite = (CallSite) keyIt.next();
		indexMap.put(callSite.callStmt, callSite);
	}
	return indexMap;
}
  /**
   * Get all call site in a set of statements.
   * <p>
   * @param callSiteIndexMap a map from {@link Stmt Stmt} to {@link CallSite CallSite}.
   * @param sliceSet a set of statement.
   * @return a list of {@link CallSite CallSite}.
   */
private List callSiteInSlice(Map callSiteIndexMap, BitSet sliceSet) {
	List calls = new ArrayList();
	Set callSiteStmtSet = callSiteIndexMap.keySet();
	for (int i = 0; i < sliceSet.size(); i++) {
		if (!sliceSet.get(i))
			continue;
		Stmt stmt = (Stmt) stmtList.get(i);
		if (callSiteStmtSet.contains(stmt)) {
			CallSite callSite = (CallSite) callSiteIndexMap.get(stmt);
			calls.add(callSite);
		}
	}
	return calls;
}
  /**
   * Collect call site statement from a set of {@link CallSite CallSite}.
   * <p>
   * @param readyCallSites a set of {@link CallSite CallSite}.
   * @return a set of statement corresponding to those call site.
   */
private Set collectCallSitesFrom(Set readyCallSites)
	{
	  Set callSites = new ArraySet();
for (Iterator siteIt = readyCallSites.iterator(); siteIt.hasNext();)
  {
	CallSite site = (CallSite) siteIt.next();
	callSites.add(site.callStmt);
  }
return callSites;
	}
  /**
   * Collect all methods where call sites are.
   * <p>
   * @param readyCallSites a set of {@link CallSite CallSite}.
   * @return a set of {@link MethodInfo MethodInfo}.
   */
private Set collectCallSitesMdInfoFrom(Set readyCallSites)
	{      Set callSitesMd = new ArraySet();
for (Iterator siteIt = readyCallSites.iterator(); siteIt.hasNext();)
  {
	CallSite site = (CallSite) siteIt.next();
	MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(site.callerSootMethod);
	if (callerMdInfo == null) continue;
	callSitesMd.add(callerMdInfo);
  }
return callSitesMd;
	}
  /**
   * Compute relevant variables for control dependence. 
   * <br> Put all used variables of control statement into relevant variables.
   * <p>
   * @param relVars relevant variables map.
   * @param nodeControled statement that is controlled by <code>cdNodes</code>.
   * @param cdNodes a set of control statement.
   */
private void ctrlRelVarCompute(Map relVars, Stmt nodeControled, BitSet cdNodes) {
	for (int i = 0; i < cdNodes.size(); i++) {
		if (!cdNodes.get(i))
			continue;
		Set relVarsOfNodeCd = (Set) relVars.get(nodeControled);

		//get reference variables of controlNode
		Stmt controlNode = (Stmt) stmtList.get(i);
		Set refsOfNode = refVarsOf(controlNode);
		CallSite site = isCallSite(indexMaps.getCallSiteMap(), controlNode);
		if (site != null) {
			//add the base of the invokation to the relevant variable set
			refsOfNode = new ArraySet();
			Value base = getBaseFrom(site);
			if (base != null)
				refsOfNode.add(base);
		}
		//get relevant variables of controlNdoe --- set union
		refsOfNode.addAll(relVarsOfNodeCd);

		//put relevant set of control node to the relevant varible map
		relVars.put(controlNode, refsOfNode);
	}
}
  /**
   * Compute relevant variables for data dependence.
   * <p>
   * @param relVars relevant variables map.
   * @param refNode statement that is data dependent on <code>dd</code>.
   * @param dd a set of {@link DataBox DataBox} on which <code>refNode</code>
   * is data dependent.
   * @return a set of {@link DataBox DataBox} on which <code>refNode</code>
   * is data dependent with considering relevant variables.
   */
private Set dataRelVarCompute(Map relVars, Stmt refNode, Set dd) {
	Set workSet = new ArraySet();
	Set defVarsOfRefNode = defVarsOf(refNode);
	Set refVarsInRefNode = refVarsOf(refNode);
	//varsInRefNode.addAll(defVarsOfRefNode);
	Set relVarsOfRefNode = (Set) relVars.get(refNode);
	CallSite callsite = isCallSite(indexMaps.getCallSiteMap(), refNode);
	if (callsite == null) {
		if (!relVarsOfRefNode.containsAll(refVarsInRefNode))
			return workSet;
		for (Iterator i = dd.iterator(); i.hasNext();) {
			DataBox defNode = (DataBox) i.next();
			Set dataDependVars = defNode.getDependVar();
			Stmt defNodeStmt = defNode.getStmt();
			workSet.add(defNode);

			//get reference variables of definition Node
			Set refsOfNode = refVarsOf(defNodeStmt);

			//get relevant variables of def Ndoe --- set union
			refsOfNode.addAll(relVarsOfRefNode);

			//put relevant set of control node to the relevant varible map
			relVars.put(defNodeStmt, refsOfNode);
			Set refVars = refVarsOf(refNode);
			refVars.addAll(relVarsOfRefNode);
			relVars.put(refNode, refVars);
		}
	} else {
		for (Iterator i = dd.iterator(); i.hasNext();) {
			DataBox defNode = (DataBox) i.next();
			Set dataDependVars = defNode.getDependVar();
			Stmt defNodeStmt = defNode.getStmt();
			if (varsContainArg(dataDependVars, callsite)){
			}
			else{
			
			dataDependVars.addAll(defVarsOfRefNode);
			//dataDependVars.addAll(refVarsInRefNode); //added on 8/3/2000
			Set interVars = SetUtil.setIntersection(dataDependVars, relVarsOfRefNode);
			if (interVars.size() == 0)
				continue;
			}
			workSet.add(defNode);

			//get reference variables of definition Node
			Set refsOfNode = refVarsOf(defNodeStmt);
			CallSite site = isCallSite(indexMaps.getCallSiteMap(),defNodeStmt);
			if (site != null) {
				//add the base of the invokation to the relevant variable set
				refsOfNode = new ArraySet();
				Value base = getBaseFrom(site);
				if (base != null)
					refsOfNode.add(base);
			}
			//get relevant variables of def Ndoe --- set union
			refsOfNode.addAll(relVarsOfRefNode);

			//put relevant set of control node to the relevant varible map
			relVars.put(defNodeStmt, refsOfNode);
			Set refVars = new ArraySet();
			Value base = getBaseFrom(callsite);
			if (base != null)
				refVars.add(base);
			refVars.addAll(relVarsOfRefNode);
			relVars.put(refNode, refVars);
		}
	}
	return workSet;
}
  /**
   * Get all variables defined in a given statement.
   * <p>
   * @param stmt query statement.
   * @return a set of {@link Value Value} which is 
   * defined in <code>stmt</code>.
   */
static Set defVarsOf(Stmt stmt) {
	Set buff = new ArraySet();
	for (Iterator defBoxIt = stmt.getDefBoxes().iterator(); defBoxIt.hasNext();) {
		ValueBox defBox = (ValueBox) defBoxIt.next();
		Value value = defBox.getValue();
		if ((value instanceof Local) || (value instanceof StaticFieldRef) || (value instanceof InstanceFieldRef))
			buff.add(value);
	}
	return buff;
}
private boolean existCallPathFromOriginalMd(MethodInfo toMd) {
	//if (fromMd.sootMethod.getName().equals("<init>"))
	//return true;
	boolean existCallPath = false;
	Set calculatedMds = new ArraySet();
	LinkedList workList = new LinkedList();
	//workList.addFirst(fromMd);
	workList.addAll(Slicer.originalMethods);
	boolean finished = false;
	while (!finished) {
		if (workList.isEmpty())
			finished = true;
		else {
			MethodInfo targetMdInfo = (MethodInfo) workList.removeFirst();
			calculatedMds.add(targetMdInfo);
			LinkedList calledMethods = new LinkedList();
			addCalledMdToWorkList(targetMdInfo, calledMethods, calculatedMds);
			/*
			if (Slicer.originalMethods.contains(targetMdInfo) 
			|| existMutualLockWithOriginalMd(targetMdInfo) 
			|| originalMdsFromReadyContains(targetMdInfo)
			|| originalMdsFromReadyCallSite.contains(targetMdInfo)) {
			*/
			if (calledMethods.contains(toMd)) {
				existCallPath = true;
				finished = true;
			} else



				
				//add call methods at all callsite of targetMd to workList
				workList.addAll(workList.size(), calledMethods);
		//addCalledMdToWorkList(targetMdInfo, workList, calculatedMds);

	}
}
return existCallPath;
}
  /*
private boolean existCallPathFromOriginalMd(MethodInfo callerMd) {
	//if (fromMd.sootMethod.getName().equals("<init>"))
	//return true;
	boolean existCallPath = false;
	Set calculatedMds = new ArraySet();
	LinkedList workList = new LinkedList();
	workList.addFirst(callerMd);
	boolean finished = false;
	while (!finished) {
		if (workList.isEmpty())
			finished = true;
		else {
			MethodInfo targetMdInfo = (MethodInfo) workList.removeFirst();
			calculatedMds.add(targetMdInfo);
			if (Slicer.originalMethods.contains(targetMdInfo)){
				existCallPath = true;
				finished = true;
			} else
				//add call methods at all callers of targetMd to workList
				addCallersMdToWorkList(targetMdInfo, workList, calculatedMds);
	}
}
return existCallPath;
}
  */
  /*
private boolean existCallPathToOriginalMd(MethodInfo fromMd) {
	//if (fromMd.sootMethod.getName().equals("<init>"))
	//return true;
	boolean existCallPath = false;
	Set calculatedMds = new ArraySet();
	LinkedList workList = new LinkedList();
	workList.addFirst(fromMd);
	boolean finished = false;
	while (!finished) {
		if (workList.isEmpty())
			finished = true;
		else {
			MethodInfo targetMdInfo = (MethodInfo) workList.removeFirst();
			calculatedMds.add(targetMdInfo);
			if (Slicer.originalMethods.contains(targetMdInfo) 
				|| existMutualLockWithOriginalMd(targetMdInfo) 
				|| originalMdsFromReadyContains(targetMdInfo)
				|| originalMdsFromReadyCallSite.contains(targetMdInfo)) {
				existCallPath = true;
				finished = true;
			} else
				//add call methods at all callsite of targetMd to workList
				addCalledMdToWorkList(targetMdInfo, workList, calculatedMds);
	}
}
return existCallPath;
}
  */
//exist mutual lock with original method means current method can possibly
//be executed concurrently with the original method
  /*
private boolean existMutualLockWithOriginalMd(MethodInfo currentMd) {
	boolean existing = false;
	LockAnalysis lockOfCurrentMd = currentMd.methodPDG.getLockAnalysis();
	if (lockOfCurrentMd == null) {
	} else {
		for (Iterator i = Slicer.originalMethods.iterator(); i.hasNext();) {
			MethodInfo originalMdInfo = (MethodInfo) i.next();
			LockAnalysis lockOfOriginalMd = originalMdInfo.methodPDG.getLockAnalysis();
			if (lockOfOriginalMd == null)
				continue;
			existing = (existing || lockOfCurrentMd.mutualLock(lockOfOriginalMd));
		}
	}
	return existing;
}
  */
  /**
   * Extract the line like <code>Bandera.assert</code> from all slice points.
   * <p>
   * @param lineList a set of {@link Stmt Stmt} which is slice point.
   * @return a list of statement which is like <code>Bandera.assert</code>.
   */
private  List extractingAssertLine(Set lineList) {
	List assertStmtList = new ArrayList();
	for (Iterator lineIt = lineList.iterator(); lineIt.hasNext();) {
		Stmt lineStmt = (Stmt) lineIt.next();
		if (isBanderaInvoke(lineStmt))
			assertStmtList.add(lineStmt);
	}
	return assertStmtList;
}
  /**
   * Extract variables used in all <code>Bandera.assert</code> statements.
   * <p>
   * @param assertList a list of <code>Bandera.assert</code> statements.
   * @return a list of {@link Value Value} used in the set of statements.
   */
private  List extractingVarsInAssert(List assertList) {
	List varList = new ArrayList();
	for (Iterator assertIt = assertList.iterator(); assertIt.hasNext();) {
		Stmt assertStmt = (Stmt) assertIt.next();
		List useAndDefBoxes = assertStmt.getUseAndDefBoxes();
		for (Iterator boxIt = useAndDefBoxes.iterator(); boxIt.hasNext();) {
			Value value = ((ValueBox) boxIt.next()).getValue();
			if (value instanceof Local) {
				varList.add(value);
			}
		}
	}
	return varList;
}
  /**
   * Generate new slice criterion for given method from given statement and 
   * a set of relevant variables.
   * <p>
   * @param methodInfo method to be generated new slice criterion.
   * @param interStmt slice point in the new slice criterion.
   * @param newRelVars slice variables in the new slice criterion.
   */
private void generateNewCriterion(MethodInfo methodInfo, Stmt interStmt, Set newRelVars) {

	IndexMaps ims = methodInfo.indexMaps;
	StmtList targetStmtList = methodInfo.originalStmtList;

	int interStmtIndex = targetStmtList.indexOf(interStmt);
	BitSet exitNodesNoThrow = ims.exitNodesWithoutThrow(ims.exitNodes());
	BitSet sliceSet = methodInfo.sliceSet;
	//to avoid null pointer of sliceSet
	if (sliceSet == null)
		sliceSet = new BitSet(targetStmtList.size());
	SliceCriterion increCriterion = methodInfo.increCriterion;
	if (increCriterion != null) {
		//to see if the interClassStmt is in the increCriterion

		//if it is in the increCriterion, then return
		// (because variable set 
		//is the same: used variables set of the interClassStmt)

		if (increCriterion.getSlicePoints().contains(interStmt))
			return;
	}

	//to see if the interClassStmt is in the slice set of that method
	Set relVarsAtInterStmt = new ArraySet();
	relVarsAtInterStmt.addAll(newRelVars);
	if (!sliceSet.get(interStmtIndex)) {
		//if not in it, then creat new criterion, and assign or add 
		//it to increCrit
		Set pointSet = new ArraySet();
		pointSet.add(interStmt);
		if (increCriterion == null)
			increCriterion = new SliceCriterion(pointSet, relVarsAtInterStmt, new ArraySet());
		else {
			increCriterion.getSlicePoints().addAll(pointSet);
			increCriterion.getSliceVars().addAll(relVarsAtInterStmt);
		}
	} else {
		//if it is in the slice set then to see if the used variables set
		//is the same as the relevant variables set of the interClassStmt

		Set relevantVars = getAllVariables(sliceSet,targetStmtList);
		if (relevantVars.containsAll(relVarsAtInterStmt))
			return;
		relVarsAtInterStmt.removeAll(relevantVars);
		if (relVarsAtInterStmt.isEmpty())
			return;
		BitSet newPointsSet = new BitSet(targetStmtList.size());
		Set newRelVarSet = rmAssInSlice(relVarsAtInterStmt, newPointsSet, sliceSet, ims.localAssMap());
		if (newRelVarSet.isEmpty() && SetUtil.emptyBitSet(newPointsSet))
			return;
		Set newPointStmtSet = SetUtil.bitSetToStmtSet(newPointsSet, targetStmtList);
		newPointStmtSet.add(interStmt);
		if (increCriterion == null)
			increCriterion = new SliceCriterion(newPointStmtSet, newRelVarSet, new ArraySet());
		else {
			increCriterion.getSlicePoints().addAll(newPointStmtSet);
			increCriterion.getSliceVars().addAll(newRelVarSet);
		}
	}
	if (!criterionChanged)
		criterionChanged = true;
	Map relVarMapOfNewCriterion = PreProcess.extractRelVarMapFromCriterion(increCriterion);
	increCriterion.setRelVarMap(relVarMapOfNewCriterion);
	methodInfo.increCriterion = increCriterion;
	return;
}
  /**
   * Generate new slice criterion for a caller in terms of a call site.
   * <br> The main work in this method is to calculate relevant variables in
   * the call site according to relevant variables at entry point of callee, and 
   * parameter binding.
   * <p>
   * @param callSite call site which will be the new slice point.
   * @param calleeMdInfo method which is called by the call site.
   * @param relVarmapOfCallee relevant variable map of callee, 
   * see {@link SliceCriterion#relVarMap SliceCriterion.relVarMap}.
   */
private void generateNewCriterionForCaller(CallSite callSite, MethodInfo calleeMdInfo, Map relVarMapOfCallee) {
  MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
  if (callerMdInfo==null) return;

	//get relevant variables for first statement of callee
	/*	Set relVarsOfCallee = new ArraySet();
	Stmt startStmt = startNodeAfterSlicing(calleeMdInfo);
	Set startStmtRelVar = (Set) relVarMapOfCallee.get(startStmt);
	relVarsOfCallee.addAll(startStmtRelVar);
	*/
	Set relVarsOfCallee = PostProcess.getRelevantLocals(calleeMdInfo.originalStmtList, calleeMdInfo.sliceSet, relVarMapOfCallee);
	//get relevant instance field in callee
	Set instanceFdInCallee = allMODREFFields(calleeMdInfo.MOD.instanceFields, calleeMdInfo.REF.instanceFields);
	Set relInstanceFdInCallee = new ArraySet();
	for (Iterator i = instanceFdInCallee.iterator(); i.hasNext();) {
		Value field = (Value) i.next();
		if (relVarsOfCallee.contains(field))
			relInstanceFdInCallee.add(field);
	}

	//get relevant variables in caller at callsite
	//including parameter fields and static variables
	Set relVarsInCaller = new ArraySet();
	InvokeExpr invokeExpr = callSite.invokeExpr;

	//add the base of invokeExpr to relVarsInCaller
	if (invokeExpr instanceof NonStaticInvokeExpr) {
		NonStaticInvokeExpr nonStaticInvokeExpr = (NonStaticInvokeExpr) invokeExpr;
		Value callerBase = nonStaticInvokeExpr.getBase();
		relVarsInCaller.add(callerBase);

		//change base to caller base for every relevant 
		//instance field in callee
		Set relInstanceFdInCaller = BuildPDG.cloneAndChangeBase(relInstanceFdInCallee, callerBase);

		//add instance fields to relVarsInCaller
		for (Iterator i = relInstanceFdInCaller.iterator(); i.hasNext();)
			relVarsInCaller.add((Value) i.next());
	}

	//static variables in caller which are in relVarsOfCallee
	
	Fields MODInCaller = callerMdInfo.MOD;
	Fields REFInCaller = callerMdInfo.REF;
	Set allStaticFieldsInCaller = allMODREFFields(MODInCaller.staticFields, REFInCaller.staticFields);
	for (Iterator i = allStaticFieldsInCaller.iterator(); i.hasNext();) {
		Value staticField = (Value) i.next();
		if (relVarsOfCallee.contains(staticField))
			relVarsInCaller.add(staticField);
	}

	//parameters in callee which are in caller with binding function

	Local paraLocalsOfCallee[] = calleeMdInfo.indexMaps.getParaLocalSet();
	SimpleLocalCopies simpleLocalCopiesInCallee = new SimpleLocalCopies((CompleteStmtGraph) calleeMdInfo.indexMaps.getStmtGraph());
	Fields MODInCallee = calleeMdInfo.MOD;
	Fields REFInCallee = calleeMdInfo.REF;
	Set paraFieldsInCallee = null;
	for (int j = 0; j < 2; j++) {
		if (j == 0)
			paraFieldsInCallee = MODInCallee.paraFields;
		else
			paraFieldsInCallee = REFInCallee.paraFields;
		for (Iterator k = paraFieldsInCallee.iterator(); k.hasNext();) {
			DataBox dbx = (DataBox) k.next();
			Set paraFields = dbx.getInterferVars();
			for (Iterator i = paraFields.iterator(); i.hasNext();) {
				InstanceFieldRef insFieldRef = (InstanceFieldRef) i.next();
				//should also consider the local copy of base
				if (relVarsOfCallee.contains(insFieldRef)) {
					int paraIndex = Fields.getParaIndex(insFieldRef, paraLocalsOfCallee, simpleLocalCopiesInCallee, dbx.getInterferStmt());
					InstanceFieldRef newInsFieldRef = Jimple.v().newInstanceFieldRef(invokeExpr.getArg(paraIndex), insFieldRef.getField());
					relVarsInCaller.add(newInsFieldRef);

					//add the base of paraField ot relVarsInCaller
					relVarsInCaller.add(newInsFieldRef.getBase());
				}
			}
		}
	}
	InvokeExpr callSiteInvoke = callSite.invokeExpr;
	for (int i=0; i<paraLocalsOfCallee.length; i++)
	{
		if (relVarsOfCallee.contains(paraLocalsOfCallee[i]))
		relVarsInCaller.add(callSiteInvoke.getArg(i));
	}
	generateNewCriterion(callerMdInfo, callSite.callStmt, relVarsInCaller);
}
/**
   * Generate new slice criterion for a callee in terms of a call site.
   * <br> The main work in this method is to calculate relevant variables in
   * the exit point of callee according to relevant variables at the call site
   *  of the caller, and parameter binding.
   * <p>
   * @param callSite call site.
   * @param relvarsInCaller relevant variables in the call site.
   * @param callerMdInfo the method of caller.
   * @param targetMdInfo method which is called by the call site.
   * @param relevantVarMap relevant variable map of callee, 
   * see {@link SliceCriterion#relVarMap SliceCriterion.relVarMap}.
   * @param exitStmtSet a set of {@link Stmt Stmt} which is exit point of callee.
   * @param localCopiesInCaller a set of {@link Local Local}. 
   */
private void genNewCritForExit(CallSite callSite, Map relevantVarMap, Set relVarsInCaller, MethodInfo callerMdInfo, MethodInfo targetMdInfo, Set exitStmtSet, Set localCopiesInCaller) {
	//determine the relevant variables at callSite
	//including static, instance(global), and parameters

	//get index of call site and invoke expression

	Stmt callSiteStmt = callSite.callStmt;
	SliceTraceNode stn = new SliceTraceNode(callerMdInfo, callSiteStmt);
	SliceTraceNode stnInTrace = sliceTraceContains(stn);
	if (stnInTrace == null) {
		System.out.println("slice trace node of " + stn + " should not be null");
		System.exit(0);
	}

	//Map stmtToIndexOfCaller = callerMdInfo.indexMaps.stmtToIndex();
	//Index callSiteIndex = (Index) stmtToIndexOfCaller.get(callsiteStmt);

	//get relevant variable set of the call site
	Set relVarsAtCallSite = (Set) relevantVarMap.get(callSiteStmt);


	//determine relevant variable set in target method in terms of relevant
	//variable set of the call site and MOD/REF of target method
	Set relVarsInTargetMd = new ArraySet();
	Fields MODFields = targetMdInfo.MOD;
	Fields REFFields = targetMdInfo.REF;

	//get all static fields use/def in target method
	Set staticFields = allMODREFFields(MODFields.staticFields, REFFields.staticFields);
	//filter out the relevant static variables in target method
	staticFdInTargetMd(relVarsAtCallSite, staticFields, relVarsInTargetMd);
	InvokeExpr invokeExpr = callSite.invokeExpr;
	//static invoke can only handle static fields
	if (invokeExpr instanceof NonStaticInvokeExpr) {
		//get all instance fields use/def in target method
		Set instanceFields = allMODREFFields(MODFields.instanceFields, REFFields.instanceFields);

		//fileter out the relevant instance variables in target md

		instanceFdInTargetMd(relVarsAtCallSite, instanceFields, relVarsInTargetMd, invokeExpr, localCopiesInCaller);
	}

	//begin to process the parameter fields
	Local paraLocals[] = targetMdInfo.indexMaps.getParaLocalSet();
	SimpleLocalCopies simpleLocalCopiesInTargetMd = new SimpleLocalCopies((CompleteStmtGraph) targetMdInfo.indexMaps.getStmtGraph());
	Set paraFieldsInTargetMd = MODFields.paraFields;

	//filter out which one of paraFieldsInTargetMd is relevant in caller md
	paraFdInTargetMd(paraFieldsInTargetMd, relVarsAtCallSite, relVarsInTargetMd, invokeExpr, paraLocals, simpleLocalCopiesInTargetMd);
	paraFieldsInTargetMd = REFFields.paraFields;
	paraFdInTargetMd(paraFieldsInTargetMd, relVarsAtCallSite, relVarsInTargetMd, invokeExpr, paraLocals, simpleLocalCopiesInTargetMd);
	IndexMaps targetMdIndexMaps = targetMdInfo.indexMaps;
	if (targetMdInfo.sootMethod.getName().equals("<init>")) {
		Local thisRef = targetMdIndexMaps.getThisRefLocal();
		relVarsInTargetMd.add(thisRef);

		//includs specialinvoke of thisRef into criterion
		List specialInvokes = targetMdIndexMaps.getSpecialInvokeList();
		for (Iterator i = specialInvokes.iterator(); i.hasNext();) {
			InvokeStmt invoke = (InvokeStmt) i.next();
			if (invoke.getInvokeExpr() instanceof SpecialInvokeExpr) {
				SpecialInvokeExpr specInvoke = (SpecialInvokeExpr) invoke.getInvokeExpr();
				String specSignature = specInvoke.getMethod().getSignature();
				if (!specSignature.startsWith("java.lang"))
					continue;
				Value specBase = specInvoke.getBase();
				if (specBase instanceof Local) {
					if (simpleLocalCopiesInTargetMd.isLocalCopyOfBefore((Local) specBase, thisRef, invoke)) {
						generateNewCriterion(targetMdInfo, invoke, relVarsInTargetMd);
						addToSliceTrace(stnInTrace, invoke, Kind.CALLSITE, targetMdInfo);
					} else
						if (specBase.equals(thisRef)) {
							generateNewCriterion(targetMdInfo, invoke, relVarsInTargetMd);
							addToSliceTrace(stnInTrace, invoke, Kind.CALLSITE, targetMdInfo);
						}
				} else
					throw new NonLocalBaseSpecialInvokeException("we don't know yet how to process non local base of special invoke: " + invoke);
			}
		}
	}



	//Finally, we get relVarsInTargetMd for cirterion generation

	for (Iterator i = exitStmtSet.iterator(); i.hasNext();) {
		Stmt exitStmt = (Stmt) i.next();
		if (callSiteStmt instanceof DefinitionStmt) {
			Value leftValue = ((DefinitionStmt) callSiteStmt).getLeftOp();
			if (relVarsInCaller.contains(leftValue))
				putUsedVarIntoRelSet(exitStmt, relVarsInTargetMd);
		}
		if (exitStmt instanceof ThrowStmt) {
			Set relVarsInTargetMdPlusThrowVar = new ArraySet();
			relVarsInTargetMdPlusThrowVar.addAll(relVarsInTargetMd);
			List useBoxes = exitStmt.getUseBoxes();
			for (Iterator boxIt = useBoxes.iterator(); boxIt.hasNext();) {
				ValueBox useBox = (ValueBox) boxIt.next();
				relVarsInTargetMdPlusThrowVar.add(useBox.getValue());
			}
			generateNewCriterion(targetMdInfo, exitStmt, relVarsInTargetMdPlusThrowVar);
		} else
			generateNewCriterion(targetMdInfo, exitStmt, relVarsInTargetMd);
		addToSliceTrace(stnInTrace, exitStmt, Kind.CALLSITE, targetMdInfo);
	}
}
  /**
   * Get all variables appeared in a set statement..
   * <p>
   * @param stmtIndexSet a set of statement.
   * @param localStmtList statement list for indexing.
   */
private Set getAllVariables(BitSet stmtIndexSet, StmtList localStmtList) {
	Set variableSet = new ArraySet();
	for (int i = 0; i < stmtIndexSet.size(); i++) {
		if (!stmtIndexSet.get(i))
			continue;
		Stmt stmt = (Stmt) localStmtList.get(i);
		variableSet.addAll(allLocalVarsOf(stmt));
	}
	return variableSet;
}
/**
 * Get base value for a call site.
 * <p>
 * @return base value of the call site.
 * @param site call site.
 */
private Value getBaseFrom(CallSite site) {
	Value base = null;
	InvokeExpr invoke = site.invokeExpr;
	if (invoke instanceof NonStaticInvokeExpr)
		base = ((NonStaticInvokeExpr) invoke).getBase();
	return base;
}
  /**
   * Get all the local copies before all the slice points, using the work list algorithm.
   * <p>
   * @param mdInfo method to be analysed.
   * @param linePropList a list of {@link Stmt  Stmt} which is slice point.
   * @return a set of {@link LocalCopy LocalCopy}.
   */
private Set getCompleteLocalCopiesList(MethodInfo mdInfo, Set linePropList) {
	Set copiesList = new ArraySet();
	StmtGraph stmtGraph = mdInfo.indexMaps.getStmtGraph();
	SimpleLocalCopies simpleLocalCopies = new SimpleLocalCopies((CompleteStmtGraph) stmtGraph);

	//initializing the workList
	LinkedList workList = new LinkedList();
	List visitedStmts = new ArrayList();
	for (Iterator lineIt = linePropList.iterator(); lineIt.hasNext();)
		workList.addLast((Stmt) lineIt.next());

	//begin to traverse the stmtGraph be predecessors
	while (!workList.isEmpty()) {
		Stmt stmt = (Stmt) workList.removeFirst();
		if (visitedStmts.contains(stmt))
			continue;
		visitedStmts.add(stmt);
		List copiesListBefore = simpleLocalCopies.getCopiesBefore(stmt);
		//add elements in copiesListBefore into copiesList
		for (Iterator copyIt = copiesListBefore.iterator(); copyIt.hasNext();)
			copiesList.add((LocalCopy) copyIt.next());

		//add all the predecessor of stmt into workList
		if (stmtList.indexOf(stmt) != 0) {
			List predList = stmtGraph.getPredsOf(stmt);
			for (Iterator predIt = predList.iterator(); predIt.hasNext();)
				workList.addLast((Stmt) predIt.next());
		}
	}
	return copiesList;
}
  /**
   * Get all local copies of a value.
   * <p>
   * @param base query value.
   * @param localCopies a set of {@link LocalCopy LocalCopy}.
   * @return a set {@link Local Local} which is local copy of <code>base</code>.
   */
private Set getCopiesOfTheBase(Value base, Set localCopies) {
	if (!(base instanceof Local))
		throw new BaseValueNonLocalException("base should be a local: Slicing.InstanceFdIntargetMd(...)");
	Set baseCopiesList = new ArraySet();

	//at lease the baseLocal is the one in the copiesList
	baseCopiesList.add((Local) base);

	//filter out the copies of the baseLocal from the localCopies
	for (Iterator copiesIt = localCopies.iterator(); copiesIt.hasNext();) {
		LocalCopy localCopy = (LocalCopy) copiesIt.next();
		Local leftLocal = localCopy.getLeftLocal();
		Local rightLocal = localCopy.getRightLocal();
		if (baseCopiesList.contains(leftLocal))
			
			//add right local into copiesList
			baseCopiesList.add(rightLocal);
		else
			if (baseCopiesList.contains(rightLocal))
				
				//add left local into copiesList
				baseCopiesList.add(leftLocal);
	}
	return baseCopiesList;
}
  /**
   * Change the base of given instance field reference into a set of give locals.
   * <p>
   * @param insField instance field reference need to be changed.
   * @param copiesOfBase a set of {@link Local Local} which will be new base of 
   * <code>insField</code>.
   * @return a set of {@link InstanceFieldRef InstanceFieldRef} with new base.
   */
private Set getNewInsFieldRefSet(InstanceFieldRef insField, Set copiesOfBase) {
	Set newInsFieldRefSet = new ArraySet();
	for (Iterator localIt = copiesOfBase.iterator(); localIt.hasNext();) {
		Local baseCopy = (Local) localIt.next();
		InstanceFieldRef newInsFieldRef = Jimple.v().newInstanceFieldRef(baseCopy, insField.getField());
		newInsFieldRefSet.add(newInsFieldRef);
	}
	return newInsFieldRefSet;
}
private Set getTraceNodeSetFrom(Set stmtSet,MethodInfo mdin)
	{
	  Set traceNodeSet = new ArraySet();
	  for (Iterator stmtIt = stmtSet.iterator(); stmtIt.hasNext();)
	{
	  Stmt stmt = (Stmt) stmtIt.next();
	  SliceTraceNode stn = new SliceTraceNode(mdin, stmt);
	  SliceTraceNode containStn = sliceTraceContains(stn);
	  traceNodeSet.add(containStn);
	}
	  return traceNodeSet;
	}
  /**
   * Get string representation of a value.
   * <p>
   * @param value a value.
   * @return the string representation of <code>value</code>.
   */
  private String getValueString(Value value)
	{
	  String valueString = "";

	  if (value instanceof Local)
	valueString = ((Local) value).toBriefString();

	  else if (value instanceof StaticFieldRef)
	valueString = ((StaticFieldRef) value).getField().toString();

	  else if (value instanceof InstanceFieldRef)
	valueString = ((InstanceFieldRef) value).toBriefString();

	  else
	throw new CantProcessException("We can not process other type of value, except Local, StaticFieldRef, InstanceFieldRef in PreProcess");
	   
	  return valueString;
	}
  /**
   * Get statement index set of a given data box set.
   * <p>
   * @param dataBoxSet a set of {@link DataBox DataBox}.
   * @return a set of statement indexed by statement list.
   */
private  BitSet indexSetOf(Set dataBoxSet) {
	BitSet workSet = new BitSet(stmtListSize);
	for (Iterator i = dataBoxSet.iterator(); i.hasNext();) {
		DataBox db = (DataBox) i.next();
		workSet.set(stmtList.indexOf(db.getStmt()));
	}
	return workSet;
}
  /**
   * Get all relevant instance field from a given instance field set.
   * <p>
   * @param relVarsAtCallSite a set of {@link Value Value} which is relevant variable.
   * @param instanceFields a set of {@link InstanceFieldRef InstanceFieldRef}.
   * @param relVarsInTargetMd relevant variables in target method.
   * @param invokeExpr invoke expression of the call site.
   * @param localCopiesInCaller local copies in caller.
   */
private void instanceFdInTargetMd(Set relVarsAtCallSite, Set instanceFields, Set relVarsInTargetMd, InvokeExpr invokeExpr, Set localCopiesInCaller) {
	Value base = ((NonStaticInvokeExpr) invokeExpr).getBase();
	Set copiesOfTheBase = getCopiesOfTheBase(base, localCopiesInCaller);


	/*********************************************
	  //should get all the local copy of base in caller method
	  **********************************************/

	for (Iterator i = instanceFields.iterator(); i.hasNext();) {
		InstanceFieldRef insField = (InstanceFieldRef) i.next();
		Set insFieldSet = getNewInsFieldRefSet(insField, copiesOfTheBase);
		Set interSet = SetUtil.setIntersection(relVarsAtCallSite,insFieldSet);
		if (!interSet.isEmpty()) {
			relVarsInTargetMd.add(insField);
			//add the base of instance fields to relVarsInTargetMd
			base =  insField.getBase();
			relVarsInTargetMd.add(base);
		}
	}
}
  /**
   * See if a statement is <code>Bandera.assert</code>.
   * <p>
   * @return boolean.
   */
public static  boolean isBanderaInvoke(Stmt nodeStmt) {
	if (nodeStmt instanceof InvokeStmt) {
		Value invokeExprValue = ((InvokeStmt) nodeStmt).getInvokeExpr();
		InvokeExpr invokeExpr = (InvokeExpr) invokeExprValue;
		if (invokeExpr instanceof StaticInvokeExpr) {
			SootMethod invokedMethod = invokeExpr.getMethod();
			String methodName = invokedMethod.getName();
			String className = invokedMethod.getDeclaringClass().getName();
			if ((methodName.equals("assert") || methodName.equals("available"))&& className.equals("Bandera"))
				return true;
		}
	}
	return false;
}
  /**
   * See if a statement is a call site.
   * <p>
   * @param callSiteMap call site map.
   * @param node a statement.
   * @return call site if  <code>node</code> is call site, <code>null</code> otherwise.
   */
 public static CallSite isCallSite(Map callSiteMap, Stmt node) {
	Map callSiteIndexMap = callSiteIndex(callSiteMap);
	if (callSiteIndexMap.containsKey(node))
		return ((CallSite) callSiteIndexMap.get(node));
	else
		return null;
}
  /**
   * Analyse method calls in slice.
   * <br>Generate new criterion for exit node of every method call in slice.
   * <p>
   * @param methodInfo current method.
   * @param sliceSet slice set.
   * @param relVarMap relevant variables map.
   * @param localCopiesInCaller local copies in caller.
   */
private void mdCallsInSlice(MethodInfo methodInfo, BitSet sliceSet, Map relVarMap, Set localCopiesInCaller) {
	Set relVarsInCaller = PostProcess.getRelevantLocals(methodInfo.originalStmtList, sliceSet, relVarMap);
	Map callSiteMap = methodInfo.indexMaps.getCallSiteMap();
	if (callSiteMap.size() == 0 || callSiteMap == null)
		return;
	Map callSiteIndexMap = callSiteIndex(callSiteMap);
	List callSiteList = callSiteInSlice(callSiteIndexMap, sliceSet);
	if (callSiteList.isEmpty())
		return;
	for (Iterator siteIt = callSiteList.iterator(); siteIt.hasNext();) {
		CallSite site = (CallSite) siteIt.next();
		MethodInfo targetMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get((SootMethod) callSiteMap.get(site));
		if (targetMdInfo == null) continue;
		if (alreadyGenerateCriterionForExits.contains(targetMdInfo))
			continue;
		else
			alreadyGenerateCriterionForExits.add(targetMdInfo);
		IndexMaps ims = targetMdInfo.indexMaps;
		//BitSet exitNodes = ims.exitNodes();
		BitSet exitNodesNoThrow = ims.exitNodesWithoutThrow(ims.exitNodes());
		if (SetUtil.emptyBitSet(exitNodesNoThrow)) {
			Integer indefiniteNode = BuildPDG.indefiniteFrom(CompilationManager.getAnnotationManager(), targetMdInfo);
			if (indefiniteNode.equals(IndexMaps.specialExitNode))
				throw new NoExitNodeException("Can not find out the exit node or the infinite loop in method " + targetMdInfo.sootMethod);
			exitNodesNoThrow.set(indefiniteNode.intValue());
		}
		//added on 11/12/2000 for keep throw statement
		exitNodesNoThrow.or(ims.exitNodes());
		//
		Set exitStmtSet = SetUtil.bitSetToStmtSet(exitNodesNoThrow, targetMdInfo.originalStmtList);
		genNewCritForExit(site, relVarMap, relVarsInCaller, methodInfo, targetMdInfo, exitStmtSet, localCopiesInCaller);
	}
}
/**
 * See if there is one parameter field is relevant.
 * <p>
 * @return boolean
 * @param mdInfo current method.
 * @param relMap relevant variables map.
 * @param sSet slice set.
 */
private  boolean oneParaFdIsRelevant(MethodInfo mdInfo, Map relMap, BitSet sSet) {
	Set valueSet = PostProcess.getRelevantLocals(stmtList, sSet, relMap);
	Set paraRefFields = mdInfo.REF.paraFields;
	for (Iterator fdIt = paraRefFields.iterator(); fdIt.hasNext();)
	{
		DataBox paraBox = (DataBox) fdIt.next();
		Set paraFds = paraBox.getDependVar();
		Set interFds = SetUtil.setIntersection(paraFds,valueSet);
		if (!interFds.isEmpty()) return true;
	}
	Local[] paraLocals = indexMaps.getParaLocalSet();
	for (int i=0; i<paraLocals.length; i++)
	{
		if (valueSet.contains(paraLocals[i])) return true;
	}
	return false;
}
  /*
private boolean originalMdsFromReadyContains(MethodInfo targetMdInfo) {
	if ((targetMdInfo.sCriterion != null) || (targetMdInfo.increCriterion != null) || (targetMdInfo.sliceSet != null))
		return originalMethodsFromReady.contains(targetMdInfo);
	return false;
}
  */

private void paraFdInTargetMd(Set paraFieldsInTargetMd, Set relVarsAtCallSite, Set relVarsInTargetMd, InvokeExpr invokeExpr, Local[] paraLocals, SimpleLocalCopies simpleLocalCopiesInTargetMd) {
	for (Iterator i = paraFieldsInTargetMd.iterator(); i.hasNext();) {
		DataBox dbx = (DataBox) i.next();
		Set insFieldRefs = dbx.getInterferVars();
		for (Iterator k = insFieldRefs.iterator(); k.hasNext();) {
			InstanceFieldRef insFieldRef = (InstanceFieldRef) k.next();
			int paraIndex = Fields.getParaIndex(insFieldRef, paraLocals, simpleLocalCopiesInTargetMd, dbx.getInterferStmt());
			InstanceFieldRef newInsFieldRef = Jimple.v().newInstanceFieldRef(invokeExpr.getArg(paraIndex), insFieldRef.getField());
			if (relVarsAtCallSite.contains(newInsFieldRef)) {
				relVarsInTargetMd.add(insFieldRef);
				//add the base of paraFields to relVarsInTargetmd
				Local baseLocal = (Local) insFieldRef.getBase();
				relVarsInTargetMd.add(baseLocal);
			}
		}
	}
}
  /**
   * Put a given variable into relevant variable map.
   * <p>
   * @param var varible
   * @param assToVar all assignment to <code>var</code>.
   * @param relVarMap relevant variable map.
   */
private  void putToRelVarMap(Object var, BitSet assToVar, Map relVarMap) {
	Set varSet = new ArraySet();
	varSet.add(var);
	for (int i = 0; i < assToVar.size(); i++) {
		if (!assToVar.get(i)) continue;
		Stmt assStmt = (Stmt) stmtList.get(i);
		if (relVarMap.containsKey(assStmt)) {
			Set prevVarSet = (Set) relVarMap.get(assStmt);
			varSet.addAll(prevVarSet);
		}
		relVarMap.put(assStmt, varSet);
	}
}
  /**
   * Add all used variables in a statement into a set.
   * <p>
   * @param stmt Stmt
   * @param varSet a set of variables.
   */
private void putUsedVarIntoRelSet(Stmt stmt, Set varSet) {
	for (Iterator useBoxIt = stmt.getUseBoxes().iterator(); useBoxIt.hasNext();) {
		ValueBox useBox = (ValueBox) useBoxIt.next();
		Value useValue = useBox.getValue();
		varSet.add(useValue);
	}
}
  /**
   * Calculate all reachable statements from a given statement.
   * <p>
   * @param stmt Stmt
   * @param stmtGraphAsPara statement graph.
   * @return a list of {@link Stmt Stmt}.
   */
public static List reachableStmtFrom(Stmt stmt, StmtGraph stmtGraphAsPara) {
	List reachableStmt = new ArrayList();
	//StmtGraph stmtGraph = indexMaps.getStmtGraph();
	reachableStmt.add(stmt);
	LinkedList stack = new LinkedList();
	stack.addFirst(stmt);
	while (!stack.isEmpty()) {
		Stmt currentStmt = (Stmt) stack.removeFirst();
		List succsList = stmtGraphAsPara.getSuccsOf(currentStmt);
		for (Iterator succsIt = succsList.iterator(); succsIt.hasNext();) {
			Stmt succStmt = (Stmt) succsIt.next();
			if (!reachableStmt.contains(succStmt)) {
				reachableStmt.add(succStmt);
				stack.addLast(succStmt);
			}
		}
	}
	return reachableStmt;
}
/**
   * Calculate all reachable statements from a given statement.
   * <p>
   * @param stmt Stmt
   * @param stmtGraphAsPara statement graph.
   * @return a set of {@link Stmt Stmt}.
   */
static Set reachableStmtSetFrom(Stmt stmt, StmtGraph stmtGraphAsPara) {
	Set reachableStmtSet = new ArraySet();
	//StmtGraph stmtGraph = indexMaps.getStmtGraph();
	List reachableStmtList = reachableStmtFrom(stmt, stmtGraphAsPara);
	for (Iterator sIt = reachableStmtList.iterator(); sIt.hasNext();) {
		reachableStmtSet.add(sIt.next());
	}
	return reachableStmtSet;
}
  /*
private BitSet readyDependOnCallSite(Set callSites, Stmt stmt) {
	Map callSiteMap = indexMaps.getCallSiteMap();
	BitSet readyCallSet = new BitSet(stmtListSize);
	for (Iterator i = callSites.iterator(); i.hasNext();) {
		CallSite callSite = (CallSite) i.next();
		List reachableStmt = reachableStmtFrom(callSite.callStmt,indexMaps.getStmtGraph());
		SootMethod targetMd = (SootMethod) callSiteMap.get(callSite);
		MethodInfo calleeMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(targetMd);
		if (calleeMdInfo==null) continue;
		if (((calleeMdInfo.sCriterion != null) 
			|| (calleeMdInfo.increCriterion != null) 
			|| (calleeMdInfo.sliceSet != null)) 
		  && reachableStmt.contains(stmt))
			readyCallSet.set(stmtList.indexOf(callSite.callStmt));
		//return readyCallSet;
	}
	if (!SetUtil.emptyBitSet(readyCallSet)) return readyCallSet;
	
	for (Iterator i = callSites.iterator(); i.hasNext();) {
		CallSite callSite = (CallSite) i.next();
		List reachableStmt = reachableStmtFrom(callSite.callStmt,indexMaps.getStmtGraph());
		if (reachableStmt.contains(stmt))
			readyCallSet.set(stmtList.indexOf(callSite.callStmt));
	}
	return readyCallSet;
}
  */
  /**
   * Get all variables used in one statement.
   * <p>
   * @return a set of {@link Value Value}.
   */
static Set refVarsOf(Stmt stmt) {
	Set buff = new ArraySet();
	for (Iterator useBoxIt = stmt.getUseBoxes().iterator(); useBoxIt.hasNext();) {
		ValueBox useBox = (ValueBox) useBoxIt.next();
		Value value = useBox.getValue();
		if ((value instanceof Local) || (value instanceof StaticFieldRef) || (value instanceof InstanceFieldRef) || (value instanceof ParameterRef))
			buff.add(value);
	}
	return buff;
}
private Set rmAssInSlice(Set relVarSet, BitSet keepedPoints, BitSet sliceSet, Map localAssMap) {
	Set keepedVars = new ArraySet();
	for (Iterator i = relVarSet.iterator(); i.hasNext();) {
		Object var =  i.next();
		BitSet assignmentSet = (BitSet) localAssMap.get(var);
		if (assignmentSet == null || assignmentSet.size() == 0)
			continue;
		BitSet diffIndexSet = SetUtil.bitSetAndNot(assignmentSet, sliceSet);
		if (!SetUtil.emptyBitSet(diffIndexSet)) {
			keepedVars.add(var);
			keepedPoints.or(diffIndexSet);
		}
	}
	return keepedVars;
}
public static SliceTraceNode sliceTraceContains(SliceTraceNode traceNode) {
	for (Iterator nodeIt = Slicer.allSliceTraceNodes.iterator(); nodeIt.hasNext();) {
		SliceTraceNode stn = (SliceTraceNode) nodeIt.next();
		if (stn.equals(traceNode)) {
			//if (!stn.stmts.contains(stmt)) stn.stmts.addElement(stmt);
			return stn;
		}
	}
	return null;
}
/**
   * Slice one method.
   */
void slicingMethod(boolean goingup) {
	// get slicing criterion from methodInfo
	// slice this method 
	// put the slice set into methodInfo
	SliceCriterion slCri = methodInfo.sCriterion;
	if (slCri == null)
		return;
	Set calculatedNodes = new ArraySet();
	BuildPDG methodPDG = methodInfo.methodPDG;
	LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
	Set varList = slCri.getSliceVars();
	Set linePropList = slCri.getSlicePoints();
	List assertLinePropList = extractingAssertLine(linePropList);
	List varsUsedInAssert = extractingVarsInAssert(assertLinePropList);
	BitSet assToVarsInAssert = assToVarsNoRelVars(stmtListSize, varsUsedInAssert, indexMaps.localAssMap());
	Map relVarMap = slCri.relVarMap();

	//initialize the slice set with assignments to the relevant variables
	BitSet sliceSet = new BitSet(stmtListSize);
	BitSet initSliceSet = assToRelVars(varList, indexMaps.localAssMap(), relVarMap);
	sliceSet.or(initSliceSet);
	LinkedList workList = new LinkedList();
	sliceSet.or(SetUtil.stmtSetToBitSet(linePropList, stmtList));
	//add this on 12/4/2000
	sliceSet.or(SetUtil.stmtSetToBitSet(slCri.getSliceStatements(), stmtList));
	//
	Set readyCallSites = collectCallSitesFrom(methodInfo.possibleReadyDependCallSite);
	sliceSet.or(SetUtil.stmtSetToBitSet(readyCallSites, stmtList));
	Set readyCallSitesMdInfo = collectCallSitesMdInfoFrom(methodInfo.possibleReadyDependCallSite);
	//originalMdsFromReadyCallSite.addAll(readyCallSitesMdInfo);
	addToWorkList(workList, sliceSet, calculatedNodes);
	addToSliceTrace(Slicer.sliceTraceRoot, slCri.getSliceStatements(), Kind.CRITERION, methodInfo);
	addToSliceTrace(Slicer.sliceTraceRoot, linePropList, Kind.CRITERION, methodInfo);
	addToSliceTrace(linePropList, initSliceSet, Kind.ASSIGN, methodInfo);
	addToSliceTrace(linePropList, readyCallSites, Kind.READY, methodInfo);

	// put use variables of the node into relevant variable map
	// this if part may should be put out of the iteration as an
	// initialization of relVarMap


	for (ListIterator workListIt = workList.listIterator(); workListIt.hasNext();) {
		Stmt node = (Stmt) workListIt.next();
		if (!linePropList.contains(node)) {
			//Because node is from workList, all nodes in workList has
			//an image in relevant variable map
			Set relVarsOfNode = refVarsOf(node);
			CallSite site = isCallSite(methodInfo.indexMaps.getCallSiteMap(), node);
			if (site != null) {
				//add the base of the invokation to the relevant variable set
				relVarsOfNode = new ArraySet();
				Value base = getBaseFrom(site);
				if (base != null)
					relVarsOfNode.add(base);
			}
			if (relVarMap.containsKey(node)) {
				Set temp = (Set) relVarMap.get(node);
				relVarsOfNode.addAll(temp);
			}
			relVarMap.put(node, relVarsOfNode);
		}
	}
	while (!workList.isEmpty()) {
		Stmt node = (Stmt) workList.removeFirst();
		SliceTraceNode traceNode = sliceTraceContains(new SliceTraceNode(methodInfo, node));
		if (traceNode == null) {
			System.out.println("traceNode for " + node + " is null!");
			// SliceTraceNode sss = new SliceTraceNode(methodInfo, node);
			//SliceTraceNode ppp = sliceTraceContains(sss);
			//System.exit(0);
		}
		calculatedNodes.add(node);

		//field (static and nonstatic) analysis and new criterion generation.
		//at current state:
		//check the use variable of the node from workList, all (static)
		//field used in the node, look out all the definition of those field
		//in other method of this class, and make it as a slicing point.

		//this method invoke may be copied to slicing method again 
		//staticFieldAnalysis(node, methodInfo, methodsList);

		Set ddNodes = methodPDG.dataNodesOf(node);


		//add computation of relevant variables
		//after data relevant varibal comp, ddNodes set may be changed

		// put use variables of the node into relevant variable map
		// this if part may should be put out of the iteration as an
		// initialization of relVarMap
		ddNodes = dataRelVarCompute(relVarMap, node, ddNodes);
		BitSet ddIndexSet = indexSetOf(ddNodes);
		sliceSet.or(ddIndexSet);
		addToWorkList(workList, ddIndexSet, calculatedNodes);
		addToSliceTrace(traceNode, ddIndexSet, Kind.DATA, methodInfo);
		//if cdNodes == null then it means the node stmt control depend on the special entry node
		BitSet cdNodes = methodPDG.controlNodesOf(node);
		if (cdNodes != null) {
			ctrlRelVarCompute(relVarMap, node, cdNodes);
			sliceSet.or(cdNodes);
			addToWorkList(workList, cdNodes, calculatedNodes);
			addToSliceTrace(traceNode, cdNodes, Kind.CONTROL, methodInfo);
		}
		BitSet preDivergenceNodes = methodPDG.preDivergencePointsOf(node);
		if (preDivergenceNodes != null) {
			ctrlRelVarCompute(relVarMap, node, preDivergenceNodes);
			sliceSet.or(preDivergenceNodes);
			addToWorkList(workList, preDivergenceNodes, calculatedNodes);
			addToSliceTrace(traceNode, preDivergenceNodes, Kind.DIVERGENCE, methodInfo);
		}
		//compute ready dependcy on call site

		//is the current node reachible from any possible ... call site?
		//why need to avoid isBanderaInvoke, and avoid assToVarsInAssert
		/*		if (isBanderaInvoke(node) || assToVarsInAssert.get(stmtList.indexOf(node))) {
		assToVarsInAssert.or(ddIndexSet);
		if (cdNodes != null)
		assToVarsInAssert.or(cdNodes);
		} else {*/
		/*		BitSet readyCallSites = readyDependOnCallSite(methodInfo.possibleReadyDependCallSite, node);
		if (!SetUtil.emptyBitSet(readyCallSites)) {
		ctrlRelVarCompute(relVarMap, node, readyCallSites);
		sliceSet.or(readyCallSites);
		addToWorkList(workList, readyCallSites, calculatedNodes);
		}*/
		//}

		/*if there is no lock (lockAnanlysis will be assigned to null if lockPairList.size() = 0 in the class LockAnalysis), there will be no synchronization and ready dependence within class*/

		if (lockAnalysis != null) {

			//compute synchronization dependence

			BitSet monitorSet = lockAnalysis.dependOnMonitorSet(node);
			if (!SetUtil.emptyBitSet(monitorSet)) {
				ctrlRelVarCompute(relVarMap, node, monitorSet);
				sliceSet.or(monitorSet);
				addToWorkList(workList, monitorSet, calculatedNodes);
				addToSliceTrace(traceNode, monitorSet, Kind.SYNCH, methodInfo);
			}

			//compute ready dependence on entermonitor statement
			//if the lock is not safe (determined in lockAnalysis)

			//ready dependence within class
			//compute ready dependence on wait statement
			//within class
			BitSet waitSet = lockAnalysis.readyDependOnWaits(node);
			if (!SetUtil.emptyBitSet(waitSet)) {
				ctrlRelVarCompute(relVarMap, node, waitSet);
				sliceSet.or(waitSet);
				addToWorkList(workList, waitSet, calculatedNodes);
				addToSliceTrace(traceNode, waitSet, Kind.READY, methodInfo);
				//lookup the notify stmt corresponding to those wait stmt in waitSet within the method, that means wait/notify ready dependence within method
				/*
				BitSet notifySet = waitNotifyWithinMd(waitSet, lockAnalysis.getWaitStmtList(), lockAnalysis.getNotifyStmtList());
				if (!SetUtil.emptyBitSet(notifySet)) {
				ctrlRelVarCompute(relVarMap, node, notifySet);
				sliceSet.or(notifySet);
				addToWorkList(workList, notifySet, calculatedNodes);
				addToSliceTrace(traceNode, notifySet, Kind.READY, methodInfo);
				}
				*/
			}
		}
		methodInfo.sliceSet = sliceSet;

		//if classList.size() == 1 then there will be no ready depend
		//and interference depend
		if (Slicer.classNum > 1) {
			//compute ready dependence

			//suppose all the lock is safe
			Map readyDependMap = methodPDG.getReadyDependMap();
			if (readyDependMap != null) {
				if (readyDependMap.containsKey(node)) {
					List readyDependStmt = (List) readyDependMap.get(node);
					for (Iterator readyIt = readyDependStmt.iterator(); readyIt.hasNext();) {
						ReadyDependStmt readyStmt = (ReadyDependStmt) readyIt.next();
						MethodInfo readyStmtMdInfo = readyStmt.methodInfo;
						originalMethodsFromReady.add(readyStmtMdInfo);
						generateNewCriterion(readyStmtMdInfo, readyStmt.readyOnStmt, refVarsOf(readyStmt.readyOnStmt));
						addToSliceTrace(traceNode, readyStmt.readyOnStmt, Kind.READY, readyStmtMdInfo);
					}
				}
			}

			//end of compute ready dependence interclassed

			//compute interference dependence
			//interclass
			//add this condition on 10/19/99  in slicingMethod() and			
			//slicingMethodAgain()
			if (!(node instanceof InvokeStmt) && (!linePropList.contains(node))) {
				Map interferDependMap = methodPDG.getInterferenceMap();
				if (interferDependMap.containsKey(node)) {
					Set relVarsOfNode = (Set) relVarMap.get(node);
					List interferDependStmt = (List) interferDependMap.get(node);
					for (Iterator interferIt = interferDependStmt.iterator(); interferIt.hasNext();) {
						InterferStmt interferStmt = (InterferStmt) interferIt.next();
						Set interferVars = interferStmt.interferVars;
						Set relVars = refVarsOf(interferStmt.interferStmt);
						CallSite site = isCallSite(interferStmt.methodInfo.indexMaps.getCallSiteMap(), interferStmt.interferStmt);
						if (site != null) {
							//add the base of the invokation to the relevant variable set
							relVars = new ArraySet();
							Value base = getBaseFrom(site);
							if (base != null)
								relVars.add(base);
						}
						relVars.addAll(interferVars);
						if (!interferVars.isEmpty()) {
							generateNewCriterion(interferStmt.methodInfo, interferStmt.interferStmt, relVars);
							addToSliceTrace(traceNode, interferStmt.interferStmt, Kind.INTERFER, interferStmt.methodInfo);
							//Slicer.originalMethods.add(interferStmt.methodInfo);
						}
					}
				}
			}
			//end of compute interference dependence interclassed
		}
	}
	if (goingup) {
		if (methodInfo.whoCallMe != null) {
			//if (Slicer.originalMethods.contains(methodInfo)) {
			for (Iterator callMeIt = methodInfo.whoCallMe.iterator(); callMeIt.hasNext();) {
				CallSite callSite = (CallSite) callMeIt.next();
				MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
				if (callerMdInfo == null)
					continue;
				//if (existCallPathFromOriginalMd(callerMdInfo))

				{
					generateNewCriterionForCaller(callSite, methodInfo, relVarMap);
					addToSliceTrace(Slicer.sliceTraceRoot, callSite.callStmt, Kind.WHOCALLME, callerMdInfo);
					Slicer.originalMethods.add(callerMdInfo);
				}
			}
			/*
			} else
			if (oneParaFdIsRelevant(methodInfo, relVarMap, sliceSet)) {
			if (methodInfo.whoCallMe.size() == 1) {
			for (Iterator callMeIt = methodInfo.whoCallMe.iterator(); callMeIt.hasNext();) {
			CallSite callSite = (CallSite) callMeIt.next();
			MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
			if (callerMdInfo == null)
			continue;
			generateNewCriterionForCaller(callSite, methodInfo, relVarMap);
			addToSliceTrace(Slicer.sliceTraceRoot, callSite.callStmt, Kind.WHOCALLME, callerMdInfo);
			}
			} else {
			for (Iterator callMeIt = methodInfo.whoCallMe.iterator(); callMeIt.hasNext();) {
			CallSite callSite = (CallSite) callMeIt.next();
			MethodInfo callerMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
			if (callerMdInfo == null)
			continue;
			if (callerIsRelevant(callSite)) {
			generateNewCriterionForCaller(callSite, methodInfo, relVarMap);
			addToSliceTrace(Slicer.sliceTraceRoot, callSite.callStmt, Kind.WHOCALLME, callerMdInfo);
			}
			}
			}
			}
			*/
			/*
			if (!alreadyGenerateCriterionByWhoCallMe.contains(methodInfo))
			alreadyGenerateCriterionByWhoCallMe.add(methodInfo);
			*/
		}
	}


	//generate criterion for method call
	//this also should be included in the slicing again method
	/* check the if the slice set contains some method call 
	 for every method call genreate new criteria  */
	// make it as another method

	Set localCopiesList = getCompleteLocalCopiesList(methodInfo, linePropList);
	mdCallsInSlice(methodInfo, sliceSet, relVarMap, localCopiesList);

	//deal with ready call site
	for (Iterator siteIt = MethodCallAnalysis.directReadyForWaitCallSites.iterator(); siteIt.hasNext();) {
		CallSite callSite = (CallSite) siteIt.next();
		if (alreadyGenCritByReadyCallSite.contains(callSite))
			continue;
		Set relVarsOfCurrentMd = PostProcess.getRelevantLocals(stmtList, sliceSet, relVarMap);
		if (relVarsOfCurrentMd.contains(callSite.baseValue)) {
			//add the base of the invokation to the relevant variable set
			Set relVars = new ArraySet();
			Value base = getBaseFrom(callSite);
			if (base != null)
				relVars.add(base);
			relVars.add(callSite.baseValue);
			MethodInfo callSiteMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
			if (callSiteMdInfo == null)
				continue;
			generateNewCriterion(callSiteMdInfo, callSite.callStmt, relVars);
			addToSliceTrace(Slicer.sliceTraceRoot, callSite.callStmt, Kind.READY, callSiteMdInfo);
			alreadyGenCritByReadyCallSite.add(callSite);
			//originalMdsFromReadyCallSite.add(callSiteMdInfo);
		}
	}
}
/**
   * Slice one method again because of new generated slice criterion.
   */
void slicingMethodAgain() {
	if (methodInfo.sCriterion == null) {
		methodInfo.sCriterion = methodInfo.increCriterion;
		methodInfo.increCriterion = null;
		/*
		if (Slicer.originalMethods.contains(methodInfo))
		slicingMethod(true);
		else
		*/
		slicingMethod(true);
		return;
	}
	Set calculatedNodes = new ArraySet();
	BuildPDG methodPDG = methodInfo.methodPDG;
	LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
	SliceCriterion slCri = methodInfo.increCriterion;
	Set varList = slCri.getSliceVars();
	Set linePropList = slCri.getSlicePoints();
	Map newRelVarMap = slCri.relVarMap();
	Map relVarMap = methodInfo.sCriterion.relVarMap();


	//initialize the slice set with assignments to the relevant variables
	BitSet sliceSet = new BitSet(stmtListSize);
	BitSet originalSliceSet = methodInfo.sliceSet;
	if (originalSliceSet == null)
		originalSliceSet = new BitSet(stmtListSize);
	Set originalSliceStmtSet = SetUtil.bitSetToStmtSet(originalSliceSet, stmtList);
	LinkedList workList = new LinkedList(linePropList);
	addToWorkList(workList, sliceSet, calculatedNodes);
	sliceSet.or(SetUtil.stmtSetToBitSet(linePropList, stmtList));

	// put use variables of the node into relevant variable map
	// this if part may should be put out of the iteration as an
	// initialization of relVarMap


	Set removableNode = new ArraySet();
	for (Iterator workListIt = workList.iterator(); workListIt.hasNext();) {
		Stmt node = (Stmt) workListIt.next();
		if (!linePropList.contains(node)) {
			if (originalSliceStmtSet.contains(node)) {
				removableNode.add(node);
				sliceSet.clear(stmtList.indexOf(node));
			} else {
				Set relVarsOfNode = refVarsOf(node);
				Set temp = new ArraySet();
				if (relVarMap.containsKey(node))
					temp = (Set) relVarMap.get(node);
				relVarsOfNode.addAll(temp);
				relVarMap.put(node, relVarsOfNode);
			}
		} else {
			Set relVarsOfNode = (Set) newRelVarMap.get(node);
			if (originalSliceStmtSet.contains(node)) {
				Set temp = (Set) relVarMap.get(node);
				if (!temp.equals(relVarsOfNode)) {
					Set relVars = new ArraySet();
					relVars.addAll(relVarsOfNode);
					relVars.addAll(temp);
					relVarMap.put(node, relVars);
				} else {
					removableNode.add(node);
					sliceSet.clear(stmtList.indexOf(node));
				}
			} else
				relVarMap.put(node, relVarsOfNode);
	}
}

//remove all removable nodes from workList

workList.removeAll(removableNode);
if (workList.isEmpty())
	return;
addToSliceTrace(Slicer.sliceTraceRoot, workList, Kind.CRITERION, methodInfo);
while (!workList.isEmpty()) {
	Stmt node = (Stmt) workList.removeFirst();
	SliceTraceNode traceNode = sliceTraceContains(new SliceTraceNode(methodInfo, node));
	if (traceNode == null) {
		System.out.println("traceNode for " + node + " is null! in SliceAgain");
	}
	calculatedNodes.add(node);
	Set ddNodes = methodPDG.dataNodesOf(node);

	//add computation of relevant variables
	//after data relevant varibal comp, ddNodes set may be changed


	// put use variables of the node into relevant variable map
	// this if part may should be put out of the iteration as an
	// initialization of relVarMap

	ddNodes = dataRelVarCompute(relVarMap, node, ddNodes);
	BitSet ddIndexSet = indexSetOf(ddNodes);
	sliceSet.or(ddIndexSet);
	addToWorkList(workList, ddIndexSet, calculatedNodes);
	addToSliceTrace(traceNode, ddIndexSet, Kind.DATA, methodInfo);
	BitSet cdNodes = methodPDG.controlNodesOf(node);
	if (cdNodes != null) {
		ctrlRelVarCompute(relVarMap, node, cdNodes);
		sliceSet.or(cdNodes);
		addToWorkList(workList, cdNodes, calculatedNodes);
		addToSliceTrace(traceNode, cdNodes, Kind.CONTROL, methodInfo);
	}
	BitSet preDivergenceNodes = methodPDG.preDivergencePointsOf(node);
	if (preDivergenceNodes != null) {
		ctrlRelVarCompute(relVarMap, node, preDivergenceNodes);
		sliceSet.or(preDivergenceNodes);
		addToWorkList(workList, preDivergenceNodes, calculatedNodes);
		addToSliceTrace(traceNode, preDivergenceNodes, Kind.DIVERGENCE, methodInfo);
	}
	//compute ready dependcy on call site

	//is the current node reachible from any possible ... call site?
	/*
	BitSet readyCallSites = readyDependOnCallSite(methodInfo.possibleReadyDependCallSite, node);
	if (!SetUtil.emptyBitSet(readyCallSites)) {
	ctrlRelVarCompute(relVarMap, node, readyCallSites);
	sliceSet.or(readyCallSites);
	addToWorkList(workList, readyCallSites, calculatedNodes);
	addToSliceTrace(traceNode, readyCallSites, Kind.READY, methodInfo);
	}
	*/

	if (lockAnalysis != null) {
		//compute synchronization dependence
		BitSet monitorSet = lockAnalysis.dependOnMonitorSet(node);
		if (!SetUtil.emptyBitSet(monitorSet)) {
			ctrlRelVarCompute(relVarMap, node, monitorSet);
			sliceSet.or(monitorSet);
			addToWorkList(workList, monitorSet, calculatedNodes);
			addToSliceTrace(traceNode, monitorSet, Kind.SYNCH, methodInfo);
		}

		//compute ready dependence on entermonitor statement
		//within one method
		//if the lock is not safe (determined in lockAnalysis)
		//ready dependence within class

		//compute ready dependence on wait statement
		//within class
		BitSet waitSet = lockAnalysis.readyDependOnWaits(node);
		if (!SetUtil.emptyBitSet(waitSet)) {
			ctrlRelVarCompute(relVarMap, node, waitSet);
			sliceSet.or(waitSet);
			addToWorkList(workList, waitSet, calculatedNodes);
			addToSliceTrace(traceNode, waitSet, Kind.READY, methodInfo);
			//lookup the notify stmt corresponding to those wait stmt in waitSet within the method, that means wait/notify ready dependence within method
			/*
			BitSet notifySet = waitNotifyWithinMd(waitSet, lockAnalysis.getWaitStmtList(), lockAnalysis.getNotifyStmtList());
			if (!SetUtil.emptyBitSet(notifySet)) {
			ctrlRelVarCompute(relVarMap, node, notifySet);
			sliceSet.or(notifySet);
			addToWorkList(workList, notifySet, calculatedNodes);
			addToSliceTrace(traceNode, notifySet, Kind.READY, methodInfo);
			}
			*/
		}
	}
	if (Slicer.classNum > 1) {
		//compute ready dependence
		//interclass
		if (node instanceof EnterMonitorStmt) {

			// *****************************************************
			//     this is different from the slicing method
			// *****************************************************
		} else {
			Map readyDependMap = methodPDG.getReadyDependMap();
			if (readyDependMap != null) {
				if (readyDependMap.containsKey(node)) {
					List readyDependStmt = (List) readyDependMap.get(node);
					for (Iterator readyIt = readyDependStmt.iterator(); readyIt.hasNext();) {
						ReadyDependStmt readyStmt = (ReadyDependStmt) readyIt.next();
						generateNewCriterion(readyStmt.methodInfo, readyStmt.readyOnStmt, refVarsOf(readyStmt.readyOnStmt));
						addToSliceTrace(traceNode, readyStmt.readyOnStmt, Kind.READY, readyStmt.methodInfo);
					}
				}
			}
		}

		//compute interference dependence
		//interclass

		if (!(node instanceof InvokeStmt) && (!linePropList.contains(node)))








			
			//add this condition on 10/19/99 in slicingMethod()
			//and slicingMethodAgain()
			{
			Map interferDependMap = methodPDG.getInterferenceMap();
			if (interferDependMap.containsKey(node)) {
				Set relVarsOfNode = (Set) relVarMap.get(node);
				List interferDependStmt = (List) interferDependMap.get(node);
				for (Iterator interferIt = interferDependStmt.iterator(); interferIt.hasNext();) {
					InterferStmt interferStmt = (InterferStmt) interferIt.next();
					Set interferVars = interferStmt.interferVars;
					Set relVars = refVarsOf(interferStmt.interferStmt);
					CallSite site = isCallSite(interferStmt.methodInfo.indexMaps.getCallSiteMap(), interferStmt.interferStmt);
					if (site != null) {
						//add the base of the invokation to the relevant variable set
						relVars = new ArraySet();
						Value base = getBaseFrom(site);
						if (base != null)
							relVars.add(base);
					}
					relVars.addAll(interferVars);
					if (!interferVars.isEmpty()) {
						generateNewCriterion(interferStmt.methodInfo, interferStmt.interferStmt, relVars);
						addToSliceTrace(traceNode, interferStmt.interferStmt, Kind.INTERFER, interferStmt.methodInfo);
					}
				}
			}
		}
	}
}
Set localCopiesList = getCompleteLocalCopiesList(methodInfo, linePropList);
mdCallsInSlice(methodInfo, sliceSet, relVarMap, localCopiesList);
sliceSet.or(originalSliceSet);
methodInfo.sliceSet = sliceSet;
//Set localCopiesList = getCompleteLocalCopiesList(methodInfo, linePropList);
//mdCallsInSlice(methodInfo, sliceSet, relVarMap, localCopiesList);

//chage the incremental criterion to null after using it.
methodInfo.increCriterion = null;


//deal with ready call site
for (Iterator siteIt = MethodCallAnalysis.directReadyForWaitCallSites.iterator(); siteIt.hasNext();) {
	CallSite callSite = (CallSite) siteIt.next();
	if (alreadyGenCritByReadyCallSite.contains(callSite))
		continue;
	Set relVarsOfCurrentMd = PostProcess.getRelevantLocals(stmtList, sliceSet, relVarMap);
	if (relVarsOfCurrentMd.contains(callSite.baseValue)) {
		//add the base of the invokation to the relevant variable set
		Set relVars = new ArraySet();
		Value base = getBaseFrom(callSite);
		if (base != null)
			relVars.add(base);
		relVars.add(callSite.baseValue);
		MethodInfo callSiteMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(callSite.callerSootMethod);
		if (callSiteMdInfo == null)
			continue;
		generateNewCriterion(callSiteMdInfo, callSite.callStmt, relVars);
		addToSliceTrace(Slicer.sliceTraceRoot, callSite.callStmt, Kind.READY, callSiteMdInfo);
		alreadyGenCritByReadyCallSite.add(callSite);
		//originalMdsFromReadyCallSite.add(callSiteMdInfo);
	}
}
//System.out.println("slicing method again: " + methodInfo.sootMethod);
//System.out.println("slice set: " + sliceSet);
}
private Stmt startNodeAfterSlicing(MethodInfo methodInfo) {
	BuildPDG methodPDG = methodInfo.methodPDG;
	if (methodInfo.sliceSet == null)
		return ((Stmt) stmtList.get(0));
	int startStmtIndex = 0;
	while (!methodInfo.sliceSet.get(startStmtIndex))
		startStmtIndex = methodPDG.immediatePostdominatorOf(startStmtIndex);
	return ((Stmt) stmtList.get(startStmtIndex));
}
private void staticFdInTargetMd(Set relVarsAtCallSite, Set fdsInTargetMd, Set relVarsInTargetMd) {
	for (Iterator i =  fdsInTargetMd.iterator(); i.hasNext();) {
		Value field = (Value) i.next();
		if (relVarsAtCallSite.contains(field))
			relVarsInTargetMd.add(field);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-8-3 7:22:29)
 * @return boolean
 * @param vars ca.mcgill.sable.util.Set
 * @param cs edu.ksu.cis.bandera.pdgslicer.CallSite
 */
private boolean varsContainArg(Set vars, CallSite cs) {
	InvokeExpr iepr = cs.invokeExpr;
	for (int i=0; i<iepr.getArgCount(); i++)
	{
		if (vars.contains(iepr.getArg(i))) return true;
	}
	return false;
}
}
