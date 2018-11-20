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

import java.util.Enumeration;

/**
 * This class is for the analysis of ready dependence
 * and interference dependence.
 */
public class InterClassAnalysis
{
  /**
   * A list of {@link ClassInfo ClassInfo}.
   */
  private List classList;

  /**
   * This constructor calls
   * <br> (1) {@link #readyDependence() readyDependenc()}
   * <br> (2) {@link #interferenceDependence() interferenceDependence()}.
   * <p>
   * @param cl a list of {@link ClassInfo ClassInfo}.'
   */
public InterClassAnalysis(List cl) {
	classList = cl;
	readyDependence();
	interferenceDependence();
}
	/**
	 * Check (find out) all definitions in one method for each field reference
	 * in the set <code>interferRefList</code>. And put those definitions
	 * into the interference map.
	 * <p>
	 * @param interferenceMap interference map.
	 * @param methodInfo method to be checked.
	 * @param interferRefList a list of {@link DataBox DataBox} 
	 * represents a reference of field.
	 */
private void checkOneMdForInsFdsNotInCurrentClass(Map interferenceMap, MethodInfo methodInfo, Set interferRefList) {
	SootClass currentModClass = methodInfo.sootClass;
	List onInterferDefList = new ArrayList();
	onInterferDefList.addAll(methodInfo.MOD.instanceFields);
	onInterferDefList.addAll(methodInfo.MOD.otherInsFds);
	if (onInterferDefList.isEmpty()) return;
	//interference analysis
	for (Iterator interRefIt = interferRefList.iterator(); interRefIt.hasNext();) {
		DataBox interRefBox = (DataBox) interRefIt.next();
		Stmt interferStmt = (Stmt) interRefBox.getInterferStmt();
		Set interferVars = (Set) interRefBox.getInterferVars();
		for (Iterator insFdIt = interferVars.iterator(); insFdIt.hasNext();) {
			InstanceFieldRef refField = (InstanceFieldRef) insFdIt.next();
			SootClass decClass = refField.getField().getDeclaringClass();
			//if (!decClass.equals(currentModClass)) continue;
			for (Iterator onInterDefIt = onInterferDefList.iterator(); onInterDefIt.hasNext();) {
				DataBox onInterDefBox = (DataBox) onInterDefIt.next();
				Set onInterVars = onInterDefBox.getInterferVars();
				Set actualInterVars = containsField(onInterVars, refField.getField());
				if (!actualInterVars.isEmpty()) {
					//put dependent relation to the interferenceMap
					Stmt onStmt = (Stmt) onInterDefBox.getInterferStmt();
					InterferStmt onInterStmt = new InterferStmt(methodInfo, onStmt, actualInterVars);
					List interferList = (List) interferenceMap.get(interferStmt);
					interferList.add(onInterStmt);
				}
			}
		}
	}
}
	/**
	 * Check all definitions in one method such that define values which
	 * are used in an element of <code>interferRefList</code>. And put those 
	 * definitions intp interference map.
	 * <p> 
	 * @param interferenceMap interference map from {@link Stmt Stmt} to
	 * a {@link List List} of {@link InterferStmt InterferStmt}.
	 * @param methodInfo method be checked.
	 * @param interferRefList a list of {@link DataBox DataBox} which represents
	 * a reference of field.
	 * @param havaInstanceField control if the check will include instance field references.
	 */  
private void checkOneMethod(Map interferenceMap, MethodInfo methodInfo, Set interferRefList, boolean haveInstanceField) {
	Set staticDefList = methodInfo.MOD.staticFields;
	List onInterferDefList = new ArrayList();
	onInterferDefList.addAll(staticDefList);
	if (haveInstanceField) 
		onInterferDefList.addAll(methodInfo.MOD.instanceFields);
		if (onInterferDefList.isEmpty()) return;
	//interference analysis
	for (Iterator interRefIt = interferRefList.iterator(); interRefIt.hasNext();) {
		DataBox interRefBox = (DataBox) interRefIt.next();
		Stmt interferStmt = (Stmt) interRefBox.getInterferStmt();
		Set interferRefs = (Set) interRefBox.getInterferVars();
		Set interferVars = BuildPDG.fieldRefToSootField(interferRefs);
		for (Iterator onInterDefIt = onInterferDefList.iterator(); onInterDefIt.hasNext();) {
			DataBox onInterDefBox = (DataBox) onInterDefIt.next();
			Set onInterRefs = onInterDefBox.getInterferVars();
			Set onInterVars = BuildPDG.fieldRefToSootField(onInterRefs);
			Set actualInterVars = SetUtil.setIntersection(interferVars, onInterVars);
			if (!actualInterVars.isEmpty()) {
				//put dependent relation to the interferenceMap
				Stmt onStmt = (Stmt) onInterDefBox.getInterferStmt();
				InterferStmt onInterStmt = new InterferStmt(methodInfo, onStmt, actualInterVars);
				List interferList = (List) interferenceMap.get(interferStmt);
				interferList.add(onInterStmt);
			}
		}
	}
}
/**
 * Get all field references from a set with the given field <code>sf</code>.
 * <p>
 * @return a set of {@link InstanceFieldRef InstanceFieldRef}.
 * @param insFdSet a set of {@link InstanceFieldRef InstanceFieldRef}.
 * @param sf a sootfield.
 */
private Set containsField(Set insFdSet, SootField sf) {
	Set conSet = new ArraySet();
	for (Iterator fdIt = insFdSet.iterator(); fdIt.hasNext();) {
		InstanceFieldRef insFd = (InstanceFieldRef) fdIt.next();
		SootField fd = insFd.getField();
		if (sf.equals(fd)) {
			conSet.add(insFd);
			return conSet;
		}
	}
	return conSet;
}
  /**
   * Initialize interference map with given set.
   * <p>
   * @param fieldRefList a set of field reference {@link DataBox DataBox}.
   * @param interfMap interference map to be initialized.
   */
private void initInterferenceMap(Set fieldRefList, Map interfMap) {
	for (Iterator interStmtIt = fieldRefList.iterator(); interStmtIt.hasNext();) {
		DataBox stmtBox = (DataBox) interStmtIt.next();
		Stmt stmt = stmtBox.getInterferStmt();
		interfMap.put(stmt, new ArrayList());
	}
}
/**
   * Analyse interference dependence for each method by calling
   * {@link #interferForMethod(MethodInfo) interferForMethod()}.
   */
private void interferenceDependence() {
	Map sootMethodInfoMap = Slicer.sootMethodInfoMap;
	for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) mdIt.next();

		MethodInfo methodInfo = (MethodInfo) sootMethodInfoMap.get(sootMethod);
		if (methodInfo == null)
			continue;
		BuildPDG methodPDG = methodInfo.methodPDG;

		//stmt(ref) to interClassStmt(def on share variable) list map
		Map interferenceMap = interferForMethod(methodInfo);
		methodPDG.setInterferenceMap(interferenceMap);
	}
}
/**
   * Analyse intereference dependence for one method by calling
   * <br> (1) {@link #initInterferenceMap(Set,Map) initInterferenceMap()};
   * <br> (2) {@link #checkOneMethod(Map,MethodInfo,Set, boolean) checkOneMethod()};
   * checkOneMethodForStaticBasedFd()};
   * <br> (4) {@link #checkOneMdForInsFdsNotInCurrentClass(Map,MethodInfo,Set)
   * checkOneMdForInsFdsNotInCurrentClss()};
   * <br> (5) {@link #lookupInterferDefStmt(ClassInfo,Map,MethodInfo)
   * lookupInterferDefStmt()}.
   * <p>
   * @param currentMethod current method.
   * @return a map of interference dependence from
   * {@link Stmt Stmt} to a {@link List List} of {@link InterferStmt InterferStmt}.
   */
private Map interferForMethod(MethodInfo currentMethod) {
	Set staticFdRefList = currentMethod.REF.staticFields;

	Set instanceFdRefList = currentMethod.REF.instanceFields;
	Set otherInsFdsRefList = currentMethod.REF.otherInsFds;
	Map interferenceMap = new HashMap();

	//in current, we put all the statement into the map, in the future
	//we will only put the statement that has interference dependence
	//this will save memory a lot.

	//initialization of interference Map to empty list
	initInterferenceMap(instanceFdRefList, interferenceMap);
	initInterferenceMap(staticFdRefList, interferenceMap);

	initInterferenceMap(otherInsFdsRefList, interferenceMap);

	//lookup all the statement in stmtList such that use some shared
	//variable, in current stage we consider all the static reference
	//variable as possible shared variable

	for (Iterator classIter = classList.iterator(); classIter.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIter.next();
		if (!classInfo.sootClass.equals(currentMethod.sootClass)) {
			for (Iterator methodIt = classInfo.methodsInfoList.iterator(); methodIt.hasNext();) {
				MethodInfo methodInfo = (MethodInfo) methodIt.next();
				checkOneMethod(interferenceMap, methodInfo, instanceFdRefList, true);
				checkOneMethod(interferenceMap, methodInfo, staticFdRefList, false);

				checkOneMdForInsFdsNotInCurrentClass(interferenceMap, methodInfo, otherInsFdsRefList);
			}
		} else {
			lookupInterferDefStmt(classInfo, interferenceMap, currentMethod);
		}
	}
	return interferenceMap;
}
/**
 * Find out all definitions in curren class for each use of field reference in 
 * current method. And put those definitions into interference map.
 * <p>
 * @param classInfo current class.
 * @param interferenceMap interference map.
 * @param currentMdInfo current method.
 */
private void lookupInterferDefStmt(ClassInfo classInfo, Map interferenceMap, MethodInfo currentMdInfo) {
	String className = classInfo.sootClass.getName();
	Set staticFdRefList = currentMdInfo.REF.staticFields;
	Set instanceFdRefList = currentMdInfo.REF.instanceFields;
	Set otherInsFdsRefList = currentMdInfo.REF.otherInsFds;
	LockAnalysis lockAnaOfCurrentMd = currentMdInfo.methodPDG.getLockAnalysis();
	boolean initiated = false;
	boolean haveInstanceFd = false;
	for (Iterator methodIt = classInfo.methodsInfoList.iterator(); methodIt.hasNext();) {
		MethodInfo methodInfo = (MethodInfo) methodIt.next();
		if (methodInfo.equals(currentMdInfo))
			continue;
		haveInstanceFd = false;
		Set interferRefList = new ArraySet();
		interferRefList.addAll(staticFdRefList);
		LockAnalysis lockAnaOfDefMd = methodInfo.methodPDG.getLockAnalysis();
		if (methodInfo.sootMethod.getName().equals("<init>") || methodInfo.sootMethod.getName().equals("<clinit>")) {
			interferRefList.addAll(instanceFdRefList);
			haveInstanceFd = true;
			/*
			if (!initiated) {
				initInterferenceMap(instanceFdRefList, interferenceMap);
				initiated = true;
			}
			*/
		} else {
			//if ((lockAnaOfCurrentMd != null) && (lockAnaOfDefMd != null)) {
			//if (lockAnaOfCurrentMd.mutualLock(lockAnaOfDefMd)) {
			//add instance field to interferRefList and onInterferDefList

			interferRefList.addAll(instanceFdRefList);
			haveInstanceFd = true;
			/*
			if (!initiated) {
				initInterferenceMap(instanceFdRefList, interferenceMap);
				initiated = true;
			}
			*/
			//}
		}
		if (!haveInstanceFd)
			checkOneMethod(interferenceMap, methodInfo, staticFdRefList, haveInstanceFd);
		else {
			checkOneMethod(interferenceMap, methodInfo, interferRefList, haveInstanceFd);
		}
		checkOneMdForInsFdsNotInCurrentClass(interferenceMap, methodInfo, otherInsFdsRefList);
	}
}
  /**
   * Look up all ready dependence from a list of method starting from current method
   * with lock pair list and wait statement list.
   * <br> There are two kinds of ready dependences: entermonitor statement in one method may
   * be ready dependent on exitmonitor statement in other method, if they are associated with
   * the same lock; wait statement in one method may be ready dependent on notify or notifyAll
   * statement in other method, if they are associated with the same lock.
   * <p>
   * @param readyMap map for ready dependence.
   * @param methodsList a list of {@link MethodInfo MethodInfo}.
   * @param currentMethodInfo current method.
   * @param lockPairList lock pair list.
   * @param waitStmtList wait statement list.
   */
private void lookupReadyDependStmt(Map readyMap, List methodsList, MethodInfo currentMethodInfo, List lockPairList, List waitStmtList) {
	for (Iterator methodIt = methodsList.iterator(); methodIt.hasNext();) {
		MethodInfo methodInfo = (MethodInfo) methodIt.next();
		if (currentMethodInfo.equals(methodInfo))
			continue;
		BuildPDG methodPDG = methodInfo.methodPDG;
		LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
		if (lockAnalysis == null)
			continue;
		List onLockPairList = lockAnalysis.getLockPairList();
		List notifyStmtList = lockAnalysis.getNotifyStmtList();
		//lock analysis
		for (Iterator lockPairIt = lockPairList.iterator(); lockPairIt.hasNext();) {
			MonitorPair monitorPair = (MonitorPair) lockPairIt.next();
			Value lockValue = monitorPair.getLock();
			Stmt enterStmt = (Stmt) monitorPair.getEnterMonitor();
			for (Iterator onLockPairIt = onLockPairList.iterator(); onLockPairIt.hasNext();) {
				MonitorPair onMonitorPair = (MonitorPair) onLockPairIt.next();
				Value onLockValue = onMonitorPair.getLock();
				if (BuildPDG.mayPointToTheSameRef(lockValue, onLockValue, currentMethodInfo.sootMethod, methodInfo.sootMethod)) {
					//put dependent relation to the readyMap
					for (Iterator exitIt = onMonitorPair.getExitMonitors().iterator(); exitIt.hasNext();) {
						Stmt exitStmt = (Stmt) exitIt.next();
						ReadyDependStmt onExitStmt = new ReadyDependStmt(methodInfo, exitStmt);
						List readyList = (List) readyMap.get(enterStmt);
						readyList.add(onExitStmt);
					}
				}
			}
		}

		//notifyStmt looking up

		for (Iterator waitIt = waitStmtList.iterator(); waitIt.hasNext();) {
			SynchroStmt synStmt = (SynchroStmt) waitIt.next();
			Value lockValue = synStmt.getLock();
			Stmt waitStmt = synStmt.getWaitNotify();
			for (Iterator notifyStmtIt = notifyStmtList.iterator(); notifyStmtIt.hasNext();) {
				SynchroStmt onSynStmt = (SynchroStmt) notifyStmtIt.next();
				Value onLockValue = onSynStmt.getLock();
				if (BuildPDG.mayPointToTheSameRef(lockValue, onLockValue, currentMethodInfo.sootMethod, methodInfo.sootMethod)) {
					//put dependent relation to the readyMap
					Stmt notifyStmt = onSynStmt.getWaitNotify();
					ReadyDependStmt onNotifyStmt = new ReadyDependStmt(methodInfo, notifyStmt);
					List readyList = (List) readyMap.get(waitStmt);
					readyList.add(onNotifyStmt);
				}
			}
		}
	}
}
  /**
   * Analyse ready dependence for each method in the class list 
   * by calling {@link #readyForMethod(MethodInfo,List,List) readyForMethod()}.
   */
private void readyDependence() {
	Map sootMethodInfoMap = Slicer.sootMethodInfoMap;
	for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) mdIt.next();
		MethodInfo methodInfo = (MethodInfo) sootMethodInfoMap.get(sootMethod);
		if (methodInfo == null) continue;
		BuildPDG methodPDG = methodInfo.methodPDG;
		LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
		if (lockAnalysis == null) {
			methodPDG.setReadyDependMap(null);
			continue;
		}
		List lockPairList = lockAnalysis.getLockPairList();
		List waitStmtList = lockAnalysis.getWaitStmtList();
		Map readyMap = readyForMethod(methodInfo, lockPairList, waitStmtList);
		methodPDG.setReadyDependMap(readyMap);
	}
}
//we assume that all relevant classes are diffrent on their name at least
/*this method is to search for, in all method of other than currentclass, exitmonitor statement and notify (notifyAll) statement which are the same lock with the lock in lockPairList or waitStmtList*/
  /**
   * Analyse ready dependence for one method by calling
   * {@link #lookupReadyDependStmt(Map, List MethodInfo,List,List) 
   * lookupReadyDependStmt()}.
   * <p>
   * @param currentMethod current method.
   * @param lockPairList lock pair list of current method.
   * @param waitStmtList wait statement list of current method.
   * @return a of ready dependence from {@link Stmt Stmt} to
   * a {@link List List} of {@link ReadyDependStmt ReadyDependStmt}.
   */
private Map readyForMethod(MethodInfo currentMethod, List lockPairList, List waitStmtList) {
	Map readyMap = new HashMap();

	//initialization of readyMap to empty list

	for (Iterator lockIt = lockPairList.iterator(); lockIt.hasNext();) {
		MonitorPair mp = (MonitorPair) lockIt.next();
		Stmt mpStmt = (Stmt) mp.getEnterMonitor();
		readyMap.put(mpStmt, new ArrayList());
	}
	for (Iterator waitIt = waitStmtList.iterator(); waitIt.hasNext();) {
		SynchroStmt synStmt = (SynchroStmt) waitIt.next();
		Stmt waitStmt = synStmt.getWaitNotify();
		readyMap.put(waitStmt, new ArrayList());
	}
	for (Iterator classIter = classList.iterator(); classIter.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIter.next();
		lookupReadyDependStmt(readyMap, classInfo.methodsInfoList, currentMethod, lockPairList, waitStmtList);
	}
	return readyMap;
}
}
