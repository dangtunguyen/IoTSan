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

//import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.BitSet;
import java.util.Hashtable;
/**
 * This class is for lock analysis: collecting and processing information
 * of locks in one method.
 */
public class LockAnalysis {
	private StmtGraph stmtGraph;
	private StmtList stmtList;
	private BuildPDG pdgDom;
	private MethodInfo mdInfo;
  /**
   * A list of {@link MonitorPair MonitorPair}.
   */
	private List lockPairList;

	//SynchroStmt List
  /**
   * A list of {@link SynchroStmt SynchroStmt}.
   */
	private List waitStmtList;
  /**
   * A list of {@link SynchroStmt SynchroStmt}.
   */
	private List notifyStmtList;
  /**
   * @param methodInfo method need be analysed.
   * @param cfanns annotation manager.
   */
public LockAnalysis(MethodInfo methodInfo, AnnotationManager cfanns) {
	mdInfo = methodInfo;
	stmtList = methodInfo.originalStmtList;
	stmtGraph = methodInfo.indexMaps.getStmtGraph();
	pdgDom = methodInfo.methodPDG;
	buildLockPairList(cfanns);
	collectWaitNotifyStmt();
	if (lockPairList.isEmpty() && waitStmtList.isEmpty() && notifyStmtList.isEmpty())
		pdgDom.setLockAnalysis(null);
	else
		pdgDom.setLockAnalysis(this);
}
//In current stage, we just consider the most simple condition of enter and
//exit monitor : no embedding and no overlapping
  /**
   * Build lock pair list of current method into {@link #lockPairList lockPairList}.
   */
private void buildLockPairList(AnnotationManager cfanns) {
	lockPairList = new ArrayList();
	SootMethod sm = mdInfo.sootMethod;
	SootClass sootClass = mdInfo.sootClass;
	Annotation annForSm = cfanns.getAnnotation(sootClass, sm);
	Vector synchAnnotations = getSynchAnnotation(stmtList, annForSm);
	for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
		SynchronizedStmtAnnotation cfann = (SynchronizedStmtAnnotation) e.nextElement();
		SynchronizedStmtAnnotation synchCFANN = (SynchronizedStmtAnnotation) cfann;
		Stmt enterMonitorStmt = synchCFANN.getEnterMonitor();
		MonitorPair mp = new MonitorPair();
		mp.setSynchroBodyAnn(synchCFANN.getBlockAnnotation());
		mp.setCatchAnn(synchCFANN.getCatchAnnotation());
		Stmt synchroStmts[] = synchCFANN.getStatements();
		mp.setBeginSynchroStmt(synchroStmts[0]);
		mp.setEndSynchroStmt(synchroStmts[synchroStmts.length - 1]);
		mp.setEnterMonitor((EnterMonitorStmt) enterMonitorStmt);
		mp.setExitMonitors(synchCFANN.getExitMonitors().elements());
		mp.setLock(getActualLock(enterMonitorStmt));
		Annotation catchAnn = synchCFANN.getCatchAnnotation();
		Stmt catchStmts[] = catchAnn.getStatements();
		mp.setExitMonitorInException(catchStmts[1]);
		lockPairList.add(mp);
	}
}
//SynchroStmt list
  /**
   * Collect wait/notify statement in current method into
   * {@link #waitStmtList waitStmtList} and {@link #notifyStmtList notifyStmtList}.
   */
private void collectWaitNotifyStmt() {
	waitStmtList = new ArrayList();
	notifyStmtList = new ArrayList();
	Iterator stmtIt = stmtList.iterator();
	while (stmtIt.hasNext()) {
		Stmt stmt = (Stmt) stmtIt.next();
		if (stmt instanceof InvokeStmt) {
			Value expr = ((InvokeStmt) stmt).getInvokeExpr();
			if (expr instanceof VirtualInvokeExpr) {
				VirtualInvokeExpr vie = (VirtualInvokeExpr) expr;
				if (vie.getMethod().getName().equals("wait") && (vie.getType() instanceof VoidType) && vie.getArgCount() == 0) {
					SynchroStmt synStmt = new SynchroStmt(stmt, "wait");
					//synStmt.setLock(getValueOfLocal(vie.getBase(), stmt, new StmtCls(), stmtGraph));
					synStmt.setLock(vie.getBase());
					waitStmtList.add(synStmt);
				} else
					if ((vie.getMethod().getName().equals("notify") || vie.getMethod().getName().equals("notifyAll")) && vie.getType() instanceof VoidType && vie.getArgCount() == 0) {
						SynchroStmt synStmt = new SynchroStmt(stmt, vie.getMethod().getName());
						//synStmt.setLock(getValueOfLocal(vie.getBase(), stmt, new StmtCls(), stmtGraph));
						synStmt.setLock(vie.getBase());
						notifyStmtList.add(synStmt);
					}
			}
		}
	}
}
  /**
   * Get all monitor pairs on which given statement is
   * synchronization dependent.
   * <br> (1) Get a list of monitor pairs by {@link #dependOnMonitors(Stmt) 
   * dependOnMonitors()};
   * <br> (2) Build a table from {@link EnterMonitorStmt EnterMonitorStmt}
   * to corresponding {@link MonitorPair MonitorPair}.
   * <p>
   * @param stmt query statement.
   * @return a table from {@link EnterMonitorStmt EnterMonitorStmt}
   * to corresponding {@link MonitorPair MonitorPair}, on which given 
   * <code>stmt</code> is
   * synchronization dependent.
   */
public Hashtable dependOnMonitorPairs(Stmt stmt)
{
	List monitorPairList = dependOnMonitors(stmt);
	Hashtable monitorPairs = new Hashtable();
	for (Iterator listIt = monitorPairList.iterator(); listIt.hasNext();) {
		MonitorPair monitorPair = (MonitorPair) listIt.next();
		Stmt monitorStmt = (Stmt) monitorPair.getEnterMonitor();
		/*
		Set exitMonitorSet = exitMonitorsIn(monitorPair);
		monitorPairs.put(monitorStmt, exitMonitorSet);
		*/
		monitorPairs.put(monitorStmt, monitorPair);
	}
	return monitorPairs;
}
  /** 
   * See if one statement is synchronization dependent on any monitor pair.
   * <br> One statement is synchronization dependent on a monitor pair if the 
   * statement is in the monitor section. For example,
   * <br> (1) entermonitor lock 
   * <br> ... ...
   * <br> (i) astatement;
   * <br> ... ...
   * <br> (n) exitmonitor lock
   * <br> Then <code>astatement</code> is synchronization dependent on the 
   * monitor pair (1) and (n).
   * <p>
   * @param stmt query statement.
   * @return a list of {@link MonitorPair MonitorPair}.
   */
public List dependOnMonitors(Stmt stmt) {
	List monitorPairList = new ArrayList();
	Iterator lockPairIt = lockPairList.iterator();
	while (lockPairIt.hasNext()) {
		MonitorPair lockPair = (MonitorPair) lockPairIt.next();
		BitSet codeBetweenMonitor = stmtsBetween(lockPair);
		if (codeBetweenMonitor.get(stmtList.indexOf(stmt)))
			monitorPairList.add(lockPair);
	}
	return monitorPairList;
}
  /**
   * Get all enter/exit monitor statements on which the given statement
   * is synchronization dependent.
   * <p>
   * @param stmt query statement.
   * @return a list of enter/exit monitor statements indexed in a BitSet.
   */
public BitSet dependOnMonitorSet(Stmt stmt)
//Index set
{
	List monitorPairList = dependOnMonitors(stmt);
	Set monitorSet = new ArraySet();
	for (Iterator listIt = monitorPairList.iterator(); listIt.hasNext();) {
		MonitorPair monitorPair = (MonitorPair) listIt.next();
		Stmt monitorStmt = (Stmt) monitorPair.getEnterMonitor();
		monitorSet.add(monitorStmt);
		Set exitMonitorSet = exitMonitorsIn(monitorPair);
		monitorSet.addAll(exitMonitorSet);
	}
	return SetUtil.stmtSetToBitSet(monitorSet,stmtList);
}
/**
 * Get all exit monitor statements in a {@link MonitorPair MonitorPair}.
 * <p>
 * @param mp query monitor pair.
 * @return a set of {@link Stmt Stmt}.
 */
public Set exitMonitorsIn(MonitorPair mp) {
	Set exitMonitorIndexSet = new ArraySet();
	for (Iterator exitIt = mp.getExitMonitors().iterator(); exitIt.hasNext();) {
		Stmt exitMonitor = (Stmt) exitIt.next();
		if (exitMonitor != null)
			//this conditional should be removed
			//after correction of jjjc annotations

			exitMonitorIndexSet.add(exitMonitor);
	}
	Stmt exitMonitorInException = mp.getExitMonitorInException();
	Stmt endSynchroStmt = mp.getEndSynchroStmt();
	int exitMonitorInExceptionIndex = stmtList.indexOf(exitMonitorInException);
	int endSynchroStmtIndex = stmtList.indexOf(endSynchroStmt);
	for (int i = exitMonitorInExceptionIndex; i < endSynchroStmtIndex; i++)
		exitMonitorIndexSet.add((Stmt) stmtList.get(i));
	return exitMonitorIndexSet;
}
/**
 * Get the lock to which a given monitor statement is associated.
 * <p>
 * @param monitorStmt enter/exit monitor statement.
 * @return the value of the lock to which <code>monitorStmt</code> is 
 * associated.
 * @throws MonitorStmtInstanceException if <code>monitorStmt</code> is not 
 * enter/exit monitor statement.
 */

public Value getActualLock(Stmt monitorStmt) {
	if (!(monitorStmt instanceof EnterMonitorStmt) && !(monitorStmt instanceof ExitMonitorStmt))
		throw new MonitorStmtInstanceException("The statement monitorStmt should be an EnterMonitor or ExitMonitor Stmt in getActualLock of LockAnalysis");
	Value lock;
	if (monitorStmt instanceof EnterMonitorStmt)
		lock = ((EnterMonitorStmt) monitorStmt).getOp();
	else
		// monitorStmt instanceof ExitMonitorStmt
		lock = ((ExitMonitorStmt) monitorStmt).getOp();
	//return getValueOfLocal(lock, monitorStmt, new StmtCls(), stmtGraph);
	return lock;
}
/**
 * Get the lock pair list.
 * <p>
 * @return {@link #lockPairList lockPairList}.
 */
  public List getLockPairList()
	{
	  return lockPairList;
	}
/**
 * Get monitor pair from an enter/exit monitor statement.
 * <p>
 * @param monitor an enter/exit monitor statement.
 * @return a monitor pair such that one of the pair is the given
 * <code>monitor</code>.
 * @throws StatementTypeException if the given <code>monitor</code> 
 * is not an enter/exit monitor statement.
 */
public MonitorPair getMonitorPair(Stmt monitor) {
	if (monitor instanceof EnterMonitorStmt)
		return getMonitorPairFromEnter(monitor);
	else
		if (monitor instanceof ExitMonitorStmt)
			return getMonitorPairFromExit(monitor);
		else
			throw new StatementTypeException("The statement should be entermonitor or exitmonitor statement : " + monitor + " in LockAnalysis");
}
/**
 * Get monitor pair from an enter monitor statement.
 * <p>
 * @param enterMonitor query enter monitor statement.
 * @return a monitor pair such that one of the pair is the given
 * <code>enterMonitor</code>.
 * @throws CorrespondingMonitorNotFoundException if such monitor pair can
 * not be found.
 */
private MonitorPair getMonitorPairFromEnter(Stmt enterMonitor) {
	for (Iterator lockPairIt = lockPairList.iterator(); lockPairIt.hasNext();) {
		MonitorPair lockPair = (MonitorPair) lockPairIt.next();
		Stmt lockStmt = (Stmt) lockPair.getEnterMonitor();
		if (enterMonitor.equals(lockStmt))
			return lockPair;
	}
	throw new CorrespondingMonitorNotFoundException("Can't find the corresponding monitor pair according to the statement (index) : " + enterMonitor + " in LockAnalysis");
}
/**
 * Get monitor pair from an exit monitor statement.
 * <p>
 * @param exitMonitor query exit monitor statement.
 * @return a monitor pair such that one of the pair is the given
 * <code>exitrMonitor</code>.
 * @throws CorrespondingMonitorNotFoundException if such monitor pair can
 * not be found.
 */
private MonitorPair getMonitorPairFromExit(Stmt exitMonitor) {
	for (Iterator lockPairIt = lockPairList.iterator(); lockPairIt.hasNext();) {
		MonitorPair lockPair = (MonitorPair) lockPairIt.next();
		Set exitMonitorsInLockPair = exitMonitorsIn(lockPair);
		if (exitMonitorsInLockPair.contains(exitMonitor))
			return lockPair;
	}
	throw new CorrespondingMonitorNotFoundException("Can't find the corresponding monitor pair according to the statement (index) : " + exitMonitor + " in LockAnalysis");
}
/**
 * Get notity statement list.
 * <p>
 * @return {@link #notifyStmtList notifyStmtList}.
 */
  public List getNotifyStmtList()
	{
	  return notifyStmtList;
	}
/**
 * Get all annotations of synchronization (enter/exit monitor) statement
 * in the given statement list.
 * <p>
 * @param stmtList statement list from which synchronization statements are to be selected.
 * @param annotation annotation of current method.
 * @return a vector of {@link Annotation Annotation} of all synchroniztion statements
 * in the given <code>stmtList</code>.
 */
private Vector getSynchAnnotation(StmtList stmtList, Annotation annotation) {
	Vector v = new Vector();
	for (Iterator i = stmtList.iterator(); i.hasNext();) {
		Stmt stmt = (Stmt) i.next();
		if (stmt instanceof JEnterMonitorStmt) {
			Annotation a = null;
			try {
				a = annotation.getContainingAnnotation(stmt);
			} catch (Exception e) {
			}
			if (a == null) {
				System.out.println("HEYYYY! The annotation for method " + mdInfo.sootClass.getName() + "." + mdInfo.sootMethod.getName() + " is null because current version of jjjc make the <init> method without annotation. This will be modified later.");
			} else {
				v.addElement(a);
			}
		}
	}
	return v;
}
/**
 * Get wait statement list.
 * <p>
 * @return {@link #waitStmtList waitStmtList}.
 */
  public List getWaitStmtList()
	{
	  return waitStmtList;
	}
/**
 * Get lock value for every element in {@link #lockPairList lockPairList}.
 * <p>
 * @return a set of lock {@link Value Value}.
 */
private Set lockValueSet() {
	Set lockSet = new ArraySet();
	for (int i = 0; i < this.lockPairList.size(); i++) {
		MonitorPair mp = (MonitorPair) this.lockPairList.get(i);
		Value lockValue = mp.getLock();
		lockSet.add(lockValue);
	}
	return lockSet;
}
//this method is to compute the reachable statements from entermonitor
//statement or wait statement
//statement Index list
/**
 * Get all (control flow) reachable statements from a given statement.
 * <p>
 * @param stmt query statement.
 * @return a list of statement indexed in a BitSet.
 */
public BitSet reachableStmtFrom(Stmt stmt) {
	BitSet reachableStmt = new BitSet(stmtList.size());
	//reachableStmt.set(stmtList.indexOf(stmt));
	LinkedList stack = new LinkedList();
	stack.addFirst(stmt);
	while (!stack.isEmpty()) {
		Stmt currentStmt = (Stmt) stack.removeFirst();
		List succsList = stmtGraph.getSuccsOf(currentStmt);
		for (Iterator succsIt = succsList.iterator(); succsIt.hasNext();) {
			Stmt succ = (Stmt) succsIt.next();
			int succIndex = stmtList.indexOf(succ);
			if (!reachableStmt.get(succIndex)) {
				reachableStmt.set(succIndex);
				stack.addLast(succ);
			}
		}
	}
	return reachableStmt;
}
/**
 * Get all enter monitor statements on which a given statement
 * is ready dependent. If the given statement is reachable from
 * an entermonitor statement, then we say the given statement
 * is ready dependent on the entermonitor statement.
 * <p>
 * @param stmt query statement.
 * @return a set of enter monitor statements.
 */
public Set readyDependOnEnters(Stmt stmt) {
	Set enterSet = new ArraySet();
	for (Iterator lockPairIt = lockPairList.iterator(); lockPairIt.hasNext();) {
		MonitorPair monitorPair = (MonitorPair) lockPairIt.next();
		Stmt monitorStmt = (Stmt) monitorPair.getEnterMonitor();
		BitSet reachableStmt = reachableStmtFrom(monitorStmt);
		if (reachableStmt.get(stmtList.indexOf(stmt))) {
			if (!safeLock(monitorPair))
				//important
				enterSet.add(monitorStmt);
		}
	}
	return enterSet;
}
/**
 * Get all <code>wait</code> statements on which a given statement
 * is ready dependent. If the given statement is reachable from
 * a <code>wait</code> statement, then we say the given statement
 * is ready dependent on the <code>wait</code> statement.
 * <p>
 * @param stmt query statement.
 * @return a set of <code>wait</code> statements indexed in a BitSet.
 */
public BitSet readyDependOnWaits(Stmt stmt) {
	Set waitSet = new ArraySet();
	for (Iterator waitStmtIt = waitStmtList.iterator(); waitStmtIt.hasNext();) {
		SynchroStmt synStmt = (SynchroStmt) waitStmtIt.next();
		Stmt waitStmt = synStmt.getWaitNotify();
		if (stmt.equals(waitStmt)) continue;
		BitSet reachableStmt = reachableStmtFrom(waitStmt);
		if (reachableStmt.get(stmtList.indexOf(stmt)))
			waitSet.add(waitStmt);
	}
	return SetUtil.stmtSetToBitSet(waitSet,stmtList);
}
/**
 * See if an enter/exit monitor statement is with a safe lock.
 * <br> This method is currently empty, need to be filled with programs.
 * <p>
 * @param monitor enter/exit monitor statement
 * @return boolean.
 */
  public boolean safeLock(Stmt monitor)
	{
	  MonitorPair monitorPair = getMonitorPair(monitor);

	  return safeLock(monitorPair);
	}
  //the first method (safeLock) should be recoded further
/**
 * See if a monitor pair is with a safe lock.
 * <br> This method is currently empty, need to be filled with programs.
 * <p>
 * @param monitorPair monitor pair
 * @return boolean.
 */
  public boolean safeLock(MonitorPair monitorPair)
	{
	  // no wait-free indefinite loop
	  /*check each back edge and natural loop in the monitor pair to determine
	if the loop is indefinite loop:
	   if backEdge.from is IF statement, then the loop is not indefinite
	   if backEdge.from is goto, the loop is possibly indefinite
	      if all successors of statements in the loop are in natural loop
	      then the loop will be indefinite, otherwise not
	check if there is a wait in the indefinte loop
	  */
	   
	  // no wait commands for other locks
	  /* check all wait statement between the monitor pair, to see if it is waiting on the same lock with current monitor pair
	   */

	  // no entermonitor or exitmonitor on unsafe locks

	  return true;
	}
//determine the code between each monitor statement pair which are
//synchronization dependent on both enter and exit monitor statement
//it's the same as natural loop algorithm
//Index list
/**
 * Get all statements between a monitor pair.
 * <p>
 * @param monitorPair monitor pair.
 * @return a set of statements between the monitor pair indexed in a BitSet.
 */
private BitSet stmtsBetween(MonitorPair monitorPair) {
	BitSet stmtsBetweenMonitor = new BitSet(stmtList.size());
	Stmt monitorStmt = (Stmt) monitorPair.getEnterMonitor();
	int enterIndex = stmtList.indexOf(monitorStmt);
	monitorStmt = monitorPair.getEndSynchroStmt();
	int endIndex = stmtList.indexOf(monitorStmt);
	List exitMonitors = monitorPair.getExitMonitors();
	for (int i = enterIndex + 1; i <= endIndex; i++)
		if (!exitMonitors.contains(stmtList.get(i)))
			stmtsBetweenMonitor.set(i);
	return stmtsBetweenMonitor;
}
/**
 * See if a given statement is synchronization dependent on an enter/exit monitor
 * statement.
 * <br> Algorithm: check if the given statement is in between the monitor pair.
 * <p>
 * @param stmt query statement.
 * @param monitor enter/exit monitor statement.
 * @return boolean.
 */ 
public boolean stmtSynchroDependOn(Stmt stmt, Stmt monitor)
//exit or enter;
{
	if (monitor instanceof EnterMonitorStmt || monitor instanceof ExitMonitorStmt) {
		//check out the corresponding monitor pair from the pair list
		MonitorPair monitorPair = getMonitorPair(monitor);
		return stmtSynchroDependOn(stmt, monitorPair);
	} else
		throw new StatementTypeException("The statement should be entermonitor or exitmonitor statement : " + monitor + " in LockAnalysis");
}
/**
 * See if a given statement is synchronization dependent on a monitor pair.
 * <br> Algorithm: check if the given statement is in between the monitor pair.
 * <p>
 * @param stmt query statement.
 * @param monitorPair monitor pair.
 * @return boolean.
 */ 
public boolean stmtSynchroDependOn(Stmt stmt, MonitorPair monitorPair)
//check if stmt is belonged to the codes between monitorPair
{
	int stmtIndex = stmtList.indexOf(stmt);
	BitSet codeBetweenMonitor = stmtsBetween(monitorPair);
	if (codeBetweenMonitor.get(stmtIndex))
		return true;
	return false;
}
}
