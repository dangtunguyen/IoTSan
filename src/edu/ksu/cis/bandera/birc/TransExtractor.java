package edu.ksu.cis.bandera.birc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import java.io.*;
import java.util.Vector;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.bir.ActionVector;
import edu.ksu.cis.bandera.bir.Action;
import edu.ksu.cis.bandera.bir.BirThread;
import edu.ksu.cis.bandera.bir.BirConstants;
import edu.ksu.cis.bandera.bir.LockAction;
import edu.ksu.cis.bandera.bir.LockTest;
import edu.ksu.cis.bandera.bir.ThreadAction;
import edu.ksu.cis.bandera.bir.ThreadTest;
import edu.ksu.cis.bandera.bir.PrintAction;
import edu.ksu.cis.bandera.bir.AssertAction;
import edu.ksu.cis.bandera.bir.Location;
import edu.ksu.cis.bandera.bir.Transformation;
import edu.ksu.cis.bandera.bir.AssignAction;
import edu.ksu.cis.bandera.bir.StateVar;
import edu.ksu.cis.bandera.bir.StateVarVector;

//import edu.ksu.cis.bandera.syncgen.code.BirTransformer;
import edu.ksu.cis.bandera.jext.*;

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

/**
 * A Jimple statement switch that translates Jimple statements into
 * BIR transitions.
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:32:53 $
 */
public class TransExtractor extends AbstractBanderaStmtSwitch
    implements BirConstants {

    private static Category log = Category.getInstance(TransExtractor.class);

    TransSystem system;             // TransSystem being built
    LocalDefs localDefs;            // Jimple LocalDefs of thread body
    LiveLocals liveLocals;          // Jimple LiveLocals of thread body
    BirThread thread;               // BIR thread being built
    StmtGraph stmtGraph;            // Jimple statement graph of method
    SootMethod method;              // Soot method of thread being built
    TypeExtractor typeExtract;      // Type extractor for TransSystem
    PredicateSet predSet;           // Predicate set 
    SootClassManager classManager;
    //	BirTransformer syncGenTransformer;

    public TransExtractor(TransSystem system, BirThread thread, 
			  StmtGraph stmtGraph, LocalDefs localDefs,
			  LiveLocals liveLocals, SootMethod method,
			  TypeExtractor typeExtract,  PredicateSet predSet) {
	//			  BirTransformer bt) {
	this.system = system;
	this.thread = thread;
	this.stmtGraph = stmtGraph;
	this.localDefs = localDefs;
	this.liveLocals = liveLocals;
	this.method = method;
	this.classManager = method.getDeclaringClass().getManager();
	this.typeExtract = typeExtract;
	this.predSet = predSet;
	//	this.syncGenTransformer = bt;
    }
    /**
     * Build an AssertAction from a Bandera.assert() statement.
     */

    AssertAction buildAssertAction(Stmt stmt, Value expr) {
	// Translate the assertion expression
	ExprExtractor extractor = 
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
	expr.apply(extractor);
	edu.ksu.cis.bandera.bir.Expr condition = 
	    (edu.ksu.cis.bandera.bir.Expr)extractor.getResult();
	AssertAction action = new AssertAction(condition);
	return action;
    }
    /**
     * Create a PrintAction for a Bandera.print() call.
     * <p>
     * Note: this code is very brittle.  It scans the
     * Jimple looking for the arguments to Bandera.print(String s),
     * which are concatentated using StringBuffer calls.
     * Look at the Jimple to better understand the logic.
     */

    PrintAction buildPrintAction(Stmt stmt, Value stringArg) {
	ExprExtractor extractor = 
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
	PrintAction action = new PrintAction();
	// Argument might be simple String
	if (stringArg instanceof StringConstant) {
	    stringArg.apply(extractor);
	    action.addPrintItem(extractor.getResult());
	    return action;
	} else if (! (stringArg instanceof Local)) {
	    log.error("Warning: ignoring Bandera.print() call");
	    return null;
	}
	// Otherwise argument should be a Local ref to a String
	// Find the statement that defines this local, which should
	// be a toString() call on a StringBuffer that was used
	// to concatenate the arguments.
	List defsOfUse = localDefs.getDefsOfAt((Local)stringArg, stmt);
	DefinitionStmt def = (DefinitionStmt) defsOfUse.get(0);
	InvokeExpr expr = (InvokeExpr) def.getRightOp();
	Local strBuf;
	// Loop through the StringBuffer.append() calls, adding their
	// arguments to the PrintAction.
	do {
	    strBuf = (Local) ((VirtualInvokeExpr)expr).getBase();
	    defsOfUse = localDefs.getDefsOfAt(strBuf, def);
	    def = (DefinitionStmt) defsOfUse.get(0);
	    if (! (def.getRightOp() instanceof InvokeExpr))
		break;
	    expr = (InvokeExpr) def.getRightOp();
	    expr.getArg(0).apply(extractor);
	    action.addPrintItemFront(extractor.getResult());
	} while (true);
	// The first argument to Bandera.print() was passed to the
	// constructor of the StringBuffer
	InvokeStmt initStmt = (InvokeStmt) stmtGraph.getSuccsOf(def).get(0);
	SpecialInvokeExpr initExpr = 
	    (SpecialInvokeExpr) initStmt.getInvokeExpr();
	initExpr.getArg(0).apply(extractor);
	action.addPrintItemFront(extractor.getResult());
	return action;
    }
    public void caseAssignStmt(AssignStmt stmt) {
	caseDefinitionStmt(stmt);
    }
    /**
     * Create a transformation for an assignment statement.
     */

    public void caseDefinitionStmt(DefinitionStmt stmt) {
	// Only process if type of LHS is represented
	if (typeRepresented(stmt.getLeftOp().getType())) {
	    // Build expression extractor and use it to translate both sides
	    ExprExtractor extractor = 
		new ExprExtractor(system,stmt, localDefs, method, 
				  typeExtract, predSet);
	    stmt.getRightOp().apply(extractor);
	    edu.ksu.cis.bandera.bir.Expr rhsExpr = 
		(edu.ksu.cis.bandera.bir.Expr) extractor.getResult();
	    stmt.getLeftOp().apply(extractor);
	    edu.ksu.cis.bandera.bir.Expr lhsExpr = 
		(edu.ksu.cis.bandera.bir.Expr) extractor.getResult(); 

	    // If we see 'x = new C;' where C is a thread class,
	    // assign the instance ref to the 'this' var for the thread.
	    if (stmt.getRightOp() instanceof NewExpr) {
			RefType type = ((NewExpr)stmt.getRightOp()).getBaseType();
			SootClass threadClass = classManager.getClass(type.className);
			StateVar var = system.getVarOfKey(threadClass);
			if (var != null) {
			    AssignAction setThis = new AssignAction(var,lhsExpr);
			    Location loc = makeLocation();
			    makeTrans(locationOfStmt(stmt), loc, null,
				      new AssignAction(lhsExpr,rhsExpr), 
				      getLiveVars(stmt), stmt,extractor);
			    makeTrans(loc,  locationOfNextStmt(stmt), null,
				      new AssignAction(var,lhsExpr), 
				      getLiveVars(stmt), stmt,extractor);
			    return;
			}
	    }
	    // Normal case
	    makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), null,
		      new AssignAction(lhsExpr,rhsExpr), getLiveVars(stmt),
		      stmt,extractor);

	}
	else  // Type not represented---just add trivial transformation
	    makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), null,
		      null, getLiveVars(stmt), stmt, null);
    }
    /**
     * Create transformation for enter monitor statement.
     * <p>
     * Note: we actually create two transformatios.  The first
     * has the lockAvailable guard and the lock action.  The second,
     * has the hasLock guard.
     */

    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
	// Translate monitor expression
	ExprExtractor extractor = 
	    new ExprExtractor(system, stmt, localDefs, method, typeExtract,
			      predSet);
	stmt.getOp().apply(extractor);
	edu.ksu.cis.bandera.bir.Expr lockRef = 
	    (edu.ksu.cis.bandera.bir.Expr)extractor.getResult();
	edu.ksu.cis.bandera.bir.Expr lock =  getLockFieldExpr(lockRef);
	Location loc = makeLocation();
	StateVarVector liveVars = getLiveVars(stmt);
	makeTrans(locationOfStmt(stmt), loc, 
		  new LockTest(lock, LOCK_AVAILABLE, thread),
		  new LockAction(lock, LOCK, thread),liveVars,stmt,extractor);
	makeTrans(loc, locationOfNextStmt(stmt),
		  new LockTest(lock, HAS_LOCK, thread), 
		  null, liveVars, stmt, extractor);
    }
    /**
     * Create a transformation for exit monitor statement.
     */

    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
	ExprExtractor extractor =
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
	stmt.getOp().apply(extractor);
	edu.ksu.cis.bandera.bir.Expr lockRef = 
	    (edu.ksu.cis.bandera.bir.Expr)extractor.getResult();
	edu.ksu.cis.bandera.bir.Expr lock =  getLockFieldExpr(lockRef);
	makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), null,
		  new LockAction(lock, UNLOCK, thread), getLiveVars(stmt), 
		  stmt, extractor);
    }
    /**
     * Create transformation for goto statement.
     */

    public void caseGotoStmt(GotoStmt stmt) {
	makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), null, null,
		  getLiveVars(stmt), stmt, null);
    }
    public void caseIdentityStmt(IdentityStmt stmt) {
	caseDefinitionStmt(stmt);
    }
    /**
     * Create transformations for IF statement.
     */

    public void caseIfStmt(IfStmt stmt) {
	ExprExtractor extractor =
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
	stmt.getCondition().apply(extractor);
	edu.ksu.cis.bandera.bir.Expr condition = 
	    (edu.ksu.cis.bandera.bir.Expr) extractor.getResult();
	edu.ksu.cis.bandera.bir.Expr notCondition =
	    new edu.ksu.cis.bandera.bir.NotExpr(condition);
	StateVarVector liveVars = getLiveVars(stmt);
	Iterator stmtIt = stmtGraph.getSuccsOf(stmt).iterator();
	// Use null PrintAction on branches so 
	//  1) Transformations won't be trivial (and collapsed by Reducer)
	//  2) We can record the IfStmt as the source of the action
	//     so it will show up in the counterexample display
	PrintAction action = new PrintAction();
	while (stmtIt.hasNext()) {
	    Stmt nextStmt = (Stmt) stmtIt.next();
	    if (nextStmt == stmt.getTarget())
		makeTrans(locationOfStmt(stmt), locationOfStmt(nextStmt),
			  condition, action, liveVars, stmt, extractor);
	    else
		makeTrans(locationOfStmt(stmt), locationOfStmt(nextStmt),
			  notCondition, action, liveVars, stmt, extractor);
	}		
    }
    /**
     * Create transformation for method invokation.
     * <p>
     * Most methods should have been inlined.  Here, we handle
     * only special cases (e.g., wait(), notify(), assert()).
     */

    public void caseInvokeStmt(InvokeStmt stmt) {
	Action action = null;

	// For virtual invocations, look for lock operations
	if (stmt.getInvokeExpr() instanceof VirtualInvokeExpr) {
	    VirtualInvokeExpr expr = (VirtualInvokeExpr)stmt.getInvokeExpr();
	    String methodName = expr.getMethod().getName();

	    int opCode = LockAction.operationCode(methodName);
	    if (opCode != INVALID) {
		lockOperation(stmt, expr, opCode);
		return;
	    }
	}

	// For static invocations, look for Bandera builtin operations
	//   Bandera.print, Bandera.assert
	//   Bandera.beginAtomic, Bandera.endAtomic
	//   Bandera.startThread
        //  
	// and if we are extracting a model from a syncgen system
	// look for calls of the form:
	//   GICluster$<cluster-name>.<region-name>$enter()
	//   GICluster$<cluster-name>.<region-name>$exit()
	// then we need to access the enter/exit commands for
        // <cluster-name>.<region-name> and emit those.
        //    * we should add a "buildSyncAction" method 
        //    * we should also generate the variables
	//
	if (stmt.getInvokeExpr() instanceof StaticInvokeExpr) {
	    StaticInvokeExpr expr = (StaticInvokeExpr) stmt.getInvokeExpr();
	    String methodName = expr.getMethod().getName();
	    // We still accept the old static syntax for thread ops
	    int opCode = ThreadAction.operationCode(methodName);
	    if (opCode != INVALID) {
		oldThreadOperation(stmt, expr, opCode);
		return;
	    }
	    String className = expr.getMethod().getDeclaringClass().getName();
	    if (className.equals("Bandera")) {
		if (methodName.equals("print")) {
		    action = buildPrintAction(stmt,expr.getArg(0));
		} else if (methodName.equals("assert")) {
		    action = buildAssertAction(stmt,expr.getArg(0));
		} else if (methodName.equals("beginAtomic")) {
		    action = action; 
		} else if (methodName.equals("endAtomic")) {
		    action = action; 
		} else if (methodName.equals("startThread")) {
		    action = 
			buildStartAction(stmt,
                                         ((StringConstant)expr.getArg(0)).value,
                                         expr.getArg(1));
		}
		/* SyncGen Code
		   } else if (className.startsWith("GICluster$")) {
		   String clusterName = 
		   className.substring("GICluster$".length());
		   if (methodName.endsWith("$enter")) { 
		   String regionName = 
		   methodName.substring(0,
		   methodName.length()-"$enter".length());
		   syncGenTransformer.buildSyncEnterAction(
		   system, thread, clusterName+"."+regionName,
		   locationOfStmt(stmt), locationOfNextStmt(stmt));
		   return;
		   } else if (methodName.endsWith("$exit")) { 
		   String regionName = 
		   methodName.substring(0,
		   methodName.length()-"$exit".length());
		   syncGenTransformer.buildSyncExitAction(
		   system, thread, clusterName+"."+regionName,
		   locationOfStmt(stmt), locationOfNextStmt(stmt));
		   return;
		   } else {
		   log.error("syncgen call misnamed");
		   }
		*/
            }
	}

	// if we're not suppose to ignore this method call, issue a warning
	if (! ignoringMethodCall((InvokeExpr)stmt.getInvokeExpr())) 
	    log.error("Warning: ignoring method call: " + stmt);

	makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), null,
		  action, getLiveVars(stmt), stmt, null);
    }
    /**
     * Create transformations for a lookup swicth statement.
     */

    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
	ExprExtractor extractor =
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
	stmt.getKey().apply(extractor);
	edu.ksu.cis.bandera.bir.Expr key = 
	    (edu.ksu.cis.bandera.bir.Expr) extractor.getResult();
	edu.ksu.cis.bandera.bir.Expr otherwise = null;
	StateVarVector liveVars = getLiveVars(stmt);
	// Use null print action (for same reasons as in IF statement)
	PrintAction action = new PrintAction();
	for (int i = 0; i < stmt.getTargetCount(); i++) {
	    // Make equality test for this target
	    edu.ksu.cis.bandera.bir.Expr match = 
		matchCase(key, stmt.getLookupValue(i));
	    otherwise = (otherwise == null) ? match :
		new edu.ksu.cis.bandera.bir.OrExpr(match,otherwise);
	    // Branch to target i if test is true
	    makeTrans(locationOfStmt(stmt), locationOfStmt(stmt.getTarget(i)),
		      match, action, liveVars, stmt, extractor);
	}
	// Branch to default target if none of equality tests were true
	makeTrans(locationOfStmt(stmt), 
		  locationOfStmt(stmt.getDefaultTarget()),
		  new edu.ksu.cis.bandera.bir.NotExpr(otherwise), action,
		  liveVars, stmt, extractor);
    }
    public void caseNopStmt(NopStmt stmt) {
	makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), null, null,
		  getLiveVars(stmt), stmt, null);
    }
    /**
     * Create transformations for table switch statement.
     */

    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
	ExprExtractor extractor =
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
	stmt.getKey().apply(extractor);
	edu.ksu.cis.bandera.bir.Expr key = 
	    (edu.ksu.cis.bandera.bir.Expr) extractor.getResult();
	StateVarVector liveVars = getLiveVars(stmt);
	// Use null print action (for same reasons as in IF statement)
	PrintAction action = new PrintAction();
	for (int i = stmt.getLowIndex(); i <= stmt.getHighIndex(); i++) 
	    // Branch to target i 
	    makeTrans(locationOfStmt(stmt), 
		      locationOfStmt(stmt.getTarget(i - stmt.getLowIndex())),
		      matchCase(key, i), action, liveVars, stmt, extractor);
	// Branch to default if key value outside range
	edu.ksu.cis.bandera.bir.Expr lowIndex, tooLow, highIndex, tooHigh;
	lowIndex = new edu.ksu.cis.bandera.bir.IntLit(stmt.getLowIndex());
	highIndex = new edu.ksu.cis.bandera.bir.IntLit(stmt.getHighIndex());
	tooLow = new edu.ksu.cis.bandera.bir.LtExpr(key, lowIndex);
	tooHigh = new edu.ksu.cis.bandera.bir.LtExpr(highIndex, key);
	makeTrans(locationOfStmt(stmt), 
		  locationOfStmt(stmt.getDefaultTarget()),
		  new edu.ksu.cis.bandera.bir.OrExpr(tooLow,tooHigh), action,
		  liveVars, stmt, extractor);
    }
    // public void caseThrowStmt(ThrowStmt stmt) {}
    //public void caseRetStmt(RetStmt stmt) {}
    //public void caseReturnStmt(ReturnStmt stmt) {}
    //public void caseReturnVoidStmt(ReturnVoidStmt stmt) {}

    public void defaultCase(Object o) {
	throw new RuntimeException("Unhandled statement type: " + o);
    }
    /**
     * Get the locals live before a Stmt.
     */

    StateVarVector getLiveVars(Stmt stmt) {
	StateVarVector liveVars = new StateVarVector(10);
	List liveList = liveLocals.getLiveLocalsBefore(stmt);
	Iterator liveIt = liveList.iterator();
	while (liveIt.hasNext()) {
	    Local local = (Local) liveIt.next();
	    if (typeRepresented(local.getType())) {
		String key = ExprExtractor.localKey(method, local);
		StateVar var = system.getVarOfKey(key);
		liveVars.addElement(var);
	    }
	}
	return liveVars;
    }
    /**
     * Get the BIR expression for the lock field of a record.
     * <p>
     * In Jimple, the argument to a lock operation is simply a reference
     * to an object---the lock is implicitly part of that object.
     * In BIR, locks are explicit fields of records.   Here,
     * we take a BIR expression that is a reference to a record
     * (the translation of the Jimple expression naming the locked
     * object) and we convert it to a BIR expression for the lock
     * field of that record by dereferencing and selecting the
     * special field BIRLock.  
     */
    edu.ksu.cis.bandera.bir.Expr getLockFieldExpr(edu.ksu.cis.bandera.bir.Expr lockRef) {
	log.debug("LOCK FIELD (1): " + lockRef);
	edu.ksu.cis.bandera.bir.Ref refType = 
	    (edu.ksu.cis.bandera.bir.Ref)lockRef.getType();
	log.debug("LOCK FIELD (2): " + refType);
	edu.ksu.cis.bandera.bir.Record recType = 
	    (edu.ksu.cis.bandera.bir.Record)refType.getTargetType();
	log.debug("LOCK FIELD (3): " + recType);
	edu.ksu.cis.bandera.bir.Field lockField = recType.getField("BIRLock");
	log.debug("LOCK FIELD (4): " + lockField);
	edu.ksu.cis.bandera.bir.Expr rec = 
	    new edu.ksu.cis.bandera.bir.DerefExpr(lockRef);
	log.debug("LOCK FIELD (5): " + rec);
	return new edu.ksu.cis.bandera.bir.RecordExpr(rec,lockField);
    }
    /**
     * Test if method call should be ignored.
     */
    boolean ignoringMethodCall(InvokeExpr expr) {
	String className = expr.getMethod().getDeclaringClass().getName();
	if (className.equals("java.io.PrintStream")
	    || className.equals("java.lang.String")
	    || className.equals("java.lang.StringBuffer") 
	    || className.equals("Bandera")
	    || className.startsWith("GICluster") )
	    return true;
	return false;
    }
    /**
     * Get location of Jimple Stmt that follows a given statement.
     * <p>
     * The Stmt must have at most one successor.  
     */

    Location locationOfNextStmt(Stmt stmt) {
	List successors = stmtGraph.getSuccsOf(stmt);
	if (successors.size() > 1) 
	    throw new RuntimeException("No unique successor to " + stmt);
	if (successors.size() == 1) {
	    Stmt nextStmt = (Stmt) successors.get(0);
	    Location loc = system.locationOfKey(nextStmt, thread);
	    // If the next statement is return, then this is the end
	    // of the thread body, so mark all locals as dead.
	    if (nextStmt instanceof ReturnVoidStmt)
		loc.setLiveVars(new StateVarVector());
	    return loc;
	}
	else { // no successors---just make a new location.
	    Location endLoc = makeLocation();
	    endLoc.setLiveVars(new StateVarVector());
	    return endLoc;
	}
    }
    /**
     * Get location associated with Jimple Stmt (created if it doesn't exist)
     */

    Location locationOfStmt(Unit stmt) {
	return system.locationOfKey(stmt, thread);
    }
    /**
     * Build transformations for a lock operation (either wait() or notify()).
     */

    void lockOperation(Stmt stmt, VirtualInvokeExpr expr, int opCode) {
	log.debug("TransExtractor.lockOperation: for " + expr);
	// First translate the ref expr that designates the lock object
	ExprExtractor extractor = 
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
	expr.getBase().apply(extractor);
	edu.ksu.cis.bandera.bir.Expr lockRef = 
	    (edu.ksu.cis.bandera.bir.Expr)extractor.getResult();
	edu.ksu.cis.bandera.bir.Expr lock =  getLockFieldExpr(lockRef);
	StateVarVector liveVars = getLiveVars(stmt);
	log.debug("TransExtractor.lockOperation: " + LockAction.operationName(opCode) + " for lock " + lock);
	if (opCode == WAIT) {
	    // For wait(), we make two transformations: a WAIT, then
	    // an UNWAIT guarded by lockAvailable and wasNotified tests.
	    Location loc = makeLocation();
	    makeTrans(locationOfStmt(stmt), loc, null,
		      new LockAction(lock, WAIT, thread), 
		      liveVars, stmt, extractor);
	    LockTest e1 = new LockTest(lock, LOCK_AVAILABLE, thread);
	    LockTest e2 = new LockTest(lock, WAS_NOTIFIED, thread);
	    makeTrans(loc, locationOfNextStmt(stmt), 
		      new edu.ksu.cis.bandera.bir.AndExpr(e1, e2),
		      new LockAction(lock, UNWAIT, thread),liveVars,null,null);
	}
	else // notify, notifyAll
	    makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), null,
		      new LockAction(lock, opCode, thread), 
		      liveVars, stmt, extractor);
    }
    /**
     * Create a new location.
     */

    Location makeLocation() {
	return system.locationOfKey(null, thread);
    }
    /**
     * Make transformation from one location to another with a given
     * guard and action.
     * <p>
     * This is used to create all transformations and is straightforward
     * except for handling observable Stmts, some of which might translate
     * (by default) to trivial transformations that would then be collapsed
     * away by the Reducer.  To prevent this, we test if either the
     * Stmt itself is observable (via a ThreadLocTest) or if the Stmt
     * contains some expression that is observable (e.g., by reference
     * to a global).  If so, we make sure there exists an action to
     * mark observable by inserting an empty PrintAction.   
     * For thread location tests, we add an extra location just
     * after the Stmt---the test will be true at that location
     * (note: in this case, two transformations are added).
     */

    Transformation makeTrans(Location fromLoc, Location toLoc, 
			     edu.ksu.cis.bandera.bir.Expr guard, Action action,
			     StateVarVector liveVars, Stmt stmt, 
			     ExprExtractor extractor) {
	Transformation trans;

	// For observable Stmt, make sure we have action to mark observable
	if (observableLoc(stmt) || observableValue(extractor)) {
	    if (action == null)
		action = new PrintAction();  // Null action
	    action.setObservable(true);
	}

	// For statement in location test, add an extra location just after.
	// Location test will be true here.
	if (observableLoc(stmt)) {
	    Location afterLoc = makeLocation();
	    trans = fromLoc.addTrans(afterLoc,guard, new ActionVector(action));
	    afterLoc.addTrans(toLoc, null, new ActionVector());
	    // Register this location with the predicate set
	    predSet.addPredicateLocation(stmt,afterLoc);
	} else
	    trans = fromLoc.addTrans(toLoc, guard, new ActionVector(action));

	// Set live variables and source
	fromLoc.setLiveVars(liveVars);
	if (stmt != null && action != null)
	    system.setSource(action, stmt);
	return trans;
    }
    /**
     * Generate an BIR equality test for a switch key value to be equal to
     * an integer constant.
     */

    edu.ksu.cis.bandera.bir.Expr matchCase(edu.ksu.cis.bandera.bir.Expr key, 
					   int value) {
	edu.ksu.cis.bandera.bir.Expr val = 
	    new edu.ksu.cis.bandera.bir.IntLit(value);
	return new edu.ksu.cis.bandera.bir.EqExpr(key, val);
    }
    /**
     * Check if Stmt is observable (appears in a ThreadLocTest).
     */

    boolean observableLoc(Unit stmt) {
	return (stmt != null) &&  predSet.isObservable(stmt);
    }
    /**
     * Check if some value extracted from an expression is observable.
     */

    boolean observableValue(ExprExtractor extractor) {
	return (extractor != null) &&  extractor.isObservable();
    }
    /**
     * This method was used when thread operations were static.
     */

    void oldThreadOperation(Stmt stmt, StaticInvokeExpr expr, int opCode) {
	SootMethod method = 
	    expr.getMethod().getDeclaringClass().getMethod("run");
	ThreadAction action = new ThreadAction(opCode,thread);
	log.debug("threadArg(1) = " + system.threadOfKey(method));
	action.setThreadArg(system.threadOfKey(method));

	StateVarVector liveVars = getLiveVars(stmt);
	makeTrans(locationOfStmt(stmt), locationOfNextStmt(stmt), 
		  null, action, liveVars, stmt,null);
    }
    /**
     * Construct the transformations for a thread operation (e.g., start()).
     * <p>
     * This is slightly complicated by the fact that the thread operation
     * may be invoked either on an instance of a Thread subtype or on
     * an instance of thread that was parameterized by a Runnable class 
     * instance.
     * <p>
     * For thread sub-types, the receiver object of the invoke is a 
     * reference to the instance of Thread which has a BIR "tid" that 
     * should be either initialized in case of a "start" opCode or 
     * accessed for any other opCode.
     * <p>
     * For runnable classes, the receiver object of the invoke is an 
     * instance of Thread whose "target" field holds the reference to the
     * Runnable object.  This runnable object has a BIR "tid" that should
     * be either initialized in case of a "start" opCode or accessed for 
     * any other opCode.
     */
    Action buildStartAction(Stmt stmt, String runClassName, 
			    Value thisExpr) {
	ExprExtractor extractor =
	    new ExprExtractor(system, stmt, localDefs, method, 
			      typeExtract, predSet);
        thisExpr.apply(extractor);
	edu.ksu.cis.bandera.bir.Expr thisBirExpr = 
            (edu.ksu.cis.bandera.bir.Expr) extractor.getResult();

        // create the start action, for the given runnable class and 
        // this reference
	ThreadAction threadStart = new ThreadAction(BirConstants.START, thread);

        // BIR thread's are associated with "run" (or "main") methods
        // in the runnable class, so we have to access the method here.
	SootClass runClass = classManager.getClass(runClassName);
	ca.mcgill.sable.util.LinkedList args =
	    new ca.mcgill.sable.util.LinkedList();
	args.add(ca.mcgill.sable.soot.ArrayType.v(
						  RefType.v("java.lang.String"), 1));

	SootMethod runMethod;
	if (runClass.declaresMethod("main", args, VoidType.v())) {
            runMethod = runClass.getMethod("main", args, VoidType.v());
	} else {
            runMethod = runClass.getMethod("run", new ArrayList(), VoidType.v());
	}
          
        threadStart.setThreadArg(system.threadOfKey(runMethod));
	log.debug("threadArg(2) = " + system.threadOfKey(runMethod));
        threadStart.addActual(thisBirExpr);

	log.debug("TransExtractor.buildStartAction: lhs expr " + thisBirExpr);

        // Construct the bir expression for "thisExpr.tid" as target of 
        // start action to record the tid of the newly started thread.
        edu.ksu.cis.bandera.bir.Expr rec = 
            new edu.ksu.cis.bandera.bir.DerefExpr(thisBirExpr);

        edu.ksu.cis.bandera.bir.Record recType = 
            (edu.ksu.cis.bandera.bir.Record)
            ((edu.ksu.cis.bandera.bir.Ref)thisBirExpr.getType()).getTargetType();
        edu.ksu.cis.bandera.bir.Field field = recType.getField("tid");

	log.debug("TransExtractor.buildStartAction: start action field " + field + " (of " + recType + ")");
        threadStart.setLhs(new edu.ksu.cis.bandera.bir.RecordExpr(rec,field));
        return threadStart;
    }
    /**
     * Return true if type represented.
     */

    public boolean typeRepresented(Type sootType) {
	sootType.apply(typeExtract);
	edu.ksu.cis.bandera.bir.Type type = 
	    (edu.ksu.cis.bandera.bir.Type) typeExtract.getResult();
	return (type != null) ;
    }
}
