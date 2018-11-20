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
import edu.ksu.cis.bandera.bir.BirTrace;
import edu.ksu.cis.bandera.bir.BirState;
import edu.ksu.cis.bandera.bir.BirThread;
import edu.ksu.cis.bandera.bir.ActiveThread;
import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.bir.Transformation;
import edu.ksu.cis.bandera.bir.TransVector;
import edu.ksu.cis.bandera.bir.Action;
import edu.ksu.cis.bandera.bir.ActionVector;

import edu.ksu.cis.bandera.birc.JimpleStore;
import edu.ksu.cis.bandera.birc.ExprExtractor;

import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.soot.jimple.Expr;

import java.io.PrintWriter;

import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import org.apache.log4j.Category;

/**
 * A JimpleTrace is either a sequence of Jimple Stmt's, possibly followed
 * by a Stmt causing a trap (and the name of the trap), an indication
 * of an incomplete model check (e.g., due to insufficient memory), an
 * indication of a model resource limit violation, or simply
 * a token indicating the property was verified.
 * <p>
 * This class can be used to map the output of a verifier (once
 * interpreted as a sequence of transformations) back to a sequence
 * of actions in the Jimple.
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:32:53 $
 */
public class JimpleTrace {

    /**
     * The log we will write to.
     */
    private static Category log = Category.getInstance(JimpleTrace.class);

    boolean verified;              // Property verified (no counterexample)
    boolean completed;    
    boolean limitviolation;
    boolean outofmemory;        // verifier out of memory
    boolean depthexceeded;        // verifier overran search depth
    boolean vectorexceeded;        // verifier overran state width
    Stmt [] stmts;                 // Stmts of counterexample
    String trapName;               // Name of trap (or null)
    Stmt trapStmt;                 // Stmt causing trap
    TransSystem system; 
    BirTrace birTrace;             // BIR trace from which constructed
    Hashtable threadOfStmt = new Hashtable();     // Maps Stmt -> threadName

    /**
     * The activeThreadArray provides a mapping from a statement (or step) in
     * a counter example with the thread that takes that step.  The size of the
     * the array is the number of statements in counter example (or trace).  The
     * value is the thread ID of the thread that changes.
     */
    private int[] activeThreadArray;

    /**
     * The jimpleStoreMap provides a mapping from a statement index
     * to a JimpleStore that holds the state at that step.
     */
    private Map jimpleStoreMap;

    /**
     * Maps Stmt index to state index in trace.
     * <p>
     * The BIR trace is a sequence of BIR transformations, each of which
     * may contain several actions, and not all actions map back to
     * Jimple Stmts.  The BIR trace contains a BirState for each action
     * index (i.e., a starting BirState, and the BirState after each
     * action in the BirTrace).  We must be able to retrieve the
     * BirState for a given Jimple Stmt in the trace.
     * <p>
     * The stateIndex array maintains this mapping.  The element
     * stateIndex[jimpleStmtNum] gives the index of the BirState
     * of the system in the state just *before* the given Jimple
     * Stmt is executed.  For example, suppose the BIR trace
     * contains actions A1,..,A4, but that only A2 and A4 had
     * source statements (call them S1 and S2).  Then
     * stateIndex[] would be { 0, 2, 4 }.  So stateIndex[1] = 2
     * since the state of the system after the first Stmt (S1)
     * is the BirState after the second action (A2) is executed.
     */
    int [] stateIndex;


    /** 
     * Build a JimpleTrace from a BIR trace.
     */
    public JimpleTrace(BirTrace birTrace) {

	this.birTrace = birTrace;

	jimpleStoreMap = new HashMap();

	Transformation trapTrans = birTrace.getTrapTrans();
	Action trapAction = birTrace.getTrapAction();
	system = birTrace.getTransSystem();
	verified = birTrace.isVerified();
	completed = birTrace.isComplete();
	limitviolation = birTrace.isLimitViolation();
	outofmemory = birTrace.isOutOfMemory();
	depthexceeded = birTrace.isDepthExceeded();
	vectorexceeded = birTrace.isVectorExceeded();
	activeThreadArray = birTrace.getThreadIDs();

	/* ****************************************** */
	livenessPropertyViolation = birTrace.isLivenessPropertyViolation();
	deadlockViolation = birTrace.isDeadlockViolation();
	assertionViolation = birTrace.isAssertionViolation();
	propertyViolation = birTrace.isPropertyViolation();
	/* ****************************************** */

	if (limitviolation) trapName = birTrace.getTrapName();

	// If the check was complete and free of limit violations,
	// but the property was not verified, map the BIR trace actions to  
	// their Jimple Stmt sources to build a Jimple trace
	if (completed && !limitviolation && !verified && !outofmemory && !depthexceeded && !vectorexceeded) {
	    TransVector transVector = birTrace.getTransVector();
	    Vector steps = new Vector(transVector.size());
	    stateIndex = new int[birTrace.getNumActions() + 1];
	    stateIndex[0] = 0;  // Start state always at 0
	    int actionCount = 0;

	    // For each transformation 
	    for (int i = 0; i < transVector.size(); i++) {
		Transformation trans = transVector.elementAt(i);
		ActionVector actions = trans.getActions();
		String threadName = "";

		BirThread birThread = trans.getFromLoc().getThread();
		if(birThread != null) {
		    threadName = birThread.getName();
		}

		// For each action of the transformation
		for(int j = 0; j < actions.size(); j++) {
		    Action action = actions.elementAt(j);
		    actionCount++;
		    // If the action has a source Stmt, add that to
		    // the Jimple trace, recording the action count there
		    Object source = system.getSource(action);
		    if (source != null) {
			steps.addElement(source);
			threadOfStmt.put(source,threadName);
			stateIndex[steps.size()] = actionCount;
		    }
		    if (trapTrans == trans && trapAction == action)
			break;   // stop at action that caused trap
		}
	    }

	    // Move the trace Stmts into an array
	    stmts = new Stmt[steps.size()];
	    for(int i = 0; i < steps.size(); i++) 
		stmts[i] = (Stmt) steps.elementAt(i);

	    // If BIR trace ended with trap, set trap info in JimpleTrace
	    if (birTrace.getTrapName() != null) {
		trapName = birTrace.getTrapName();
		trapStmt = (Stmt) system.getSource(trapAction);
		String threadName = 
		    birTrace.getTrapTrans().getFromLoc().getThread().getName();
		threadOfStmt.put(trapStmt,threadName);
	    }
	}
    }

    // Untested query interface (unused)
    String evalExpr(Expr expr, int stmtIndex) {
	ExprExtractor extractor = 
	    new ExprExtractor(system, null, null, null,
			      null, new PredicateSet());
	expr.apply(extractor);
	edu.ksu.cis.bandera.bir.Expr birExpr = 
	    (edu.ksu.cis.bandera.bir.Expr) extractor.getResult();
	return birTrace.evalExpr(birExpr,stateIndex[stmtIndex]);
    }

    /**
     * Get the last store in the trace.
     *
     * @return JimpleStore The last store in the trace.
     */
    public JimpleStore getLastStore() {
	return getStore(stmts.length);
    }

    /**
     * Get Jimple Stmts executed in the trace.
     * <p>
     * If a Stmt caused a trap, it will be included as the last
     * element of this array.
     */
    public Stmt [] getStatements() { return stmts; }

    /**
     * Get the thread ID of the thread that is currently executing at the
     * statement given.
     *
     * @param int statementIndex
     * @return int A thread ID.
     */
    public int getActiveThreadIDAtStatement(int statementIndex) {
	int threadID = 0;

	if((activeThreadArray != null) && (statementIndex < activeThreadArray.length)) {
	    threadID = activeThreadArray[statementIndex];
	}
	else {
	    threadID = 0;
	}

	return(threadID);
    }

    /**
     * Get the store at a particular statement.
     *
     * @param stmtIndex the index of the statement (between 0 and the number of
     *        statements in the trace)
     * @return The JimpleStore representing the variable values just before
     *         that Stmt (if there are N statements, the store at statement index N
     *         represents the final state, after all Stmts).
     */
    public JimpleStore getStore(int stmtIndex) {

	if(birTrace == null) {
	    throw new IllegalStateException("birTrace is null.  The JimpleTrace has not been initialized properly.");
	}

	if((stateIndex == null) || (stateIndex.length <= 0)) {
	    throw new IllegalStateException("stateIndex was not initialized.  The JimpleTrace has not be initialized properly.");
	}

	JimpleStore store = (JimpleStore)jimpleStoreMap.get(new Integer(stmtIndex));
	if(store == null) {
	    BirState state = birTrace.getState(stateIndex[stmtIndex]);
	    if(state != null) {
		store = new JimpleStore(state);
		jimpleStoreMap.put(new Integer(stmtIndex), store);
	    }
	    else {
		log.error("state is null for statement " + stmtIndex + ".");
	    }
	}
	else {
	    log.debug("Found a store for this statement index (" + stmtIndex + ") already created.  Reusing it.");
	}

	return store;
    }

    public TransSystem getTransSystem() {
	return system;
    }

    /**
     * Get the name of the trap (returns null if no trap occurred in trace).
     */
    public String getTrapName() {
	return trapName;
    }

    /**
     * Get the Stmt that caused the trap (returns null if no trap in trace).
     */
    public Stmt getTrapStmt() {
	return trapStmt;
    }

    /**
     * Was the failure due to the checker (e.g., out of memory)?
     */
    public boolean isComplete() {
	return completed;
    }

    /**
     * Was the failure due to a checker exceeding its depth bound?
     */
    public boolean isDepthExceeded() {
	return depthexceeded;
    }

    /**
     * Was the failure due to a model resource limit violation?
     */
    public boolean isLimitViolation() {
	return limitviolation;
    }

    /**
     * Was the failure due to checker running out of memory?
     */
    public boolean isOutOfMemory() {
	return outofmemory;
    }

    /**
     * Was the failure due to a checker exceeding its state vector size?
     */
    public boolean isVectorExceeded() {
	return vectorexceeded;
    }

    /**
     * Was the property verified (i.e., hold on the model)?
     */
    public boolean isVerified() {
	return verified;
    }

    /**
     * Format object in field of given width
     */
    static String pad(Object o, int width) {
	String padding = "                                                   ";
	String item = o.toString();
	return item + padding.substring(0,width - item.length());
    }

    /**
     * Print the state of this object to System.out.
     */
    public void print() {
	print(new PrintWriter(System.out, true),false);
    }

    /**
     * Print a JimpleTrace.
     *
     * @param out PrintWriter to send output to
     * @param showVars show variable values (if false, only Stmts are 
     *        displayed)
     */
    public void print(PrintWriter out, boolean showVars) {
	int threadNameSize = 0;
	Enumeration e = threadOfStmt.elements();
	while (e.hasMoreElements()) {
	    int size = ((String)e.nextElement()).length();
	    if (size > threadNameSize)
		threadNameSize = size;
	}   
	if (verified) 
	    out.println("NO TRACE");
	else {
	    out.println("TRACE OF VIOLATION");
	    for (int i = 0; i < stmts.length; i++) {
		if (showVars)
		    getStore(i).print();
		out.println(pad(threadOfStmt.get(stmts[i]),threadNameSize)
			    + "  " + stmts[i]);
	    }
	    if (showVars)
		getStore(stmts.length).print();
	    if (trapName != null)
		out.println(pad(threadOfStmt.get(trapStmt),threadNameSize)
			    + "  " + trapName + " at " + trapStmt);
	}
    }

    /* *****************************************************************
     * The following code was hacked into this to handle the description
     * of the type of trace this is.  It can be one of 4 types and this
     * will be used later when displaying this to the user. -tcw
     */
    private boolean livenessPropertyViolation = false;
    private boolean deadlockViolation = false;
    private boolean assertionViolation = false;
    private boolean propertyViolation = false;

    public boolean isLivenessPropertyViolation() {
	return(livenessPropertyViolation);
    }
    public boolean isDeadlockViolation() {
	return(deadlockViolation);
    }
    public boolean isAssertionViolation() {
	return(assertionViolation);
    }
    public boolean isPropertyViolation() {
	return(propertyViolation);
    }
    /* ***************************************************************** */
}
