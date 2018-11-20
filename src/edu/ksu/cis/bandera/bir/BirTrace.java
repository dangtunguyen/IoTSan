package edu.ksu.cis.bandera.bir;

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
import ca.mcgill.sable.util.*;

import java.util.*;

import org.apache.log4j.Category;

/**
 * A BIR Trace represents either a counterexample to
 * a property, a positive result (i.e., property holds), or
 * a reason for an incomplete model check.
 * <p>
 * A BIR trace is constructed by a translator after parsing the
 * output of a verifier and interpretting it as a sequence of
 * transitions through the BIR transition system.  The trace
 * may end with a trap (e.g., NullPointerException).
 * <p>
 * Traps indicate run-time errors in the program or that fixed
 * resource bounds on the model have been exceeded.  There are three
 * such traps :<br>   
 * <ul>
 * <li> RangeLimitException : integer value is out of specified range
 * <li> ArraySizeLimitException : attempt to allocate an array bigger
 *      than the defined limit
 * <li> CollectionSizeLimitException : attempt to allocate more than
 *      the defined maximum number of instances
 * <li> MaxThreadsException : attempt to allocate more than
 *      the defined maximum number of threads
 * </ul>
 * <p>
 * In addition to the transformations, the constructor of the
 * trace must specify certain CHOICES made by actions in the
 * trace:
 * <ul>
 * <li> If an AssignAction performs a nondeterministic assignment,
 *   the specific value assigned must be recorded as the CHOICE for
 *   that action.
 * <li> If an AssignAction executes an allocator, the instance number
 *   allocated must be recorded as the CHOICE for that action
 *   (this is not used for dSPIN).
 * </ul>
 * Once all the transformations and choices have been added, invoking
 * the done() method will run the BIR simulator and generate the
 * BIR states for the trace (a state is generated for each action
 * in the trace).
 * <p>
 * The trace can be queried to determine if the check:
 * <ul>
 * <li> Verified the property (<code>isVerified()</code>)
 * <li> Failed to verify the property (<code>!isVerified()</code>)
 * <li> Failed due to a model resource bounds violation (<code>isLimitViolation()</code>)
 * <li> Failed due to a checker problem, e.g., insufficient memory (<code>!isComplete()</code>)
 * </ul>
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @author Roby Joehanes &lt;robbyjo@cis.ksu.edu&gt;
 * @author Radu Iosif &lt;iosif@cis.ksu.edu&gt;
 * @version $Revisione$ - $Date: 2003/04/30 19:32:50 $
 */
public class BirTrace {

    private static Category log = Category.getInstance(BirTrace.class.getName());

    private boolean verified;              // property was verified (no counterexample)
    private boolean completed;             // verifier terminated
    private boolean limitviolation;        // verifier terminated
    private boolean outofmemory;		// verifier out of memory
    private boolean depthexceeded;		// verifier overran search depth
    private boolean vectorexceeded;		// verifier overran vector width
    private TransSystem system;
    private Vector tidVector = new Vector();              // acting threads 
    private TransVector transVector = new TransVector();  // transforms in trace
    private Vector choiceVector = new Vector();           // choices made in trace
    private String trapName;
    private Transformation currentTrans;
    private Transformation trapTrans;                     // transform causing trap
    private int trapActionNum;                            // action that caused trap
    private int numActions = 0;                           // number of actions in trace
    private BirState [] state;                            // BIR states of trace

    public BirTrace(TransSystem system) {
	this.system = system;
	this.completed = false;
	this.verified = false;
	this.limitviolation = false;
	this.outofmemory = false;
	this.depthexceeded = false;
	this.vectorexceeded = false;
    }

    public BirTrace(TransSystem system, java.util.List l) {
	this(system);
	processTrail(l);
    }

    /**
     * Add thread id to the end of the trace.
     */
    public void addTid(int tid) {
	log.debug("adding tid = " + tid);
	tidVector.addElement(new Integer(tid));
    }

    /**
     * Add a transformation to the end of the trace.
     */
    public void addTrans(Transformation trans) {
	if (currentTrans != null)
	    numActions += currentTrans.getActions().size();
	currentTrans = trans;
	transVector.addElement(trans);
	choiceVector.addElement(new int[trans.getActions().size()+1]);
    }

    /**
     * Run simulator to complete construction of trace.
     */
    public void done() {
	int actionCount = 0;
	Vector stateVector = new Vector();
	BirState currentState = 
	    BirState.initialState(system,transVector,choiceVector);
	stateVector.addElement(currentState.copy());
	for (int i = 0; i < transVector.size(); i++) {
	    Transformation trans = transVector.elementAt(i);
	    int tid = ((Integer) tidVector.elementAt(i)).intValue();
	    log.debug("trans " + i + " has tid = " + tid);
	    int choices[] = (int []) choiceVector.elementAt(i);
	    ActionVector actions = trans.getActions();
	    for (int j = 0; j < actions.size(); j++) {
		int choiceNum = choices[j];
		Action action = actions.elementAt(j);

		// don't execute the action that caused trap
		// unless its a thread start action	
		if ((trapTrans == trans && trapActionNum <= j + 1) &&
		    !((action instanceof ThreadAction) &&
		      ((ThreadAction) action).isStart()))
		    break;

		// Apply action to get next state
		log.debug("applying action " + action + " to thread " + tid + " with choice " + choiceNum + ".");
		currentState.applyAction(tid, action, choiceNum);
		if (j < (actions.size() - 1)) {
		    stateVector.addElement(currentState.copy());
		    actionCount ++;	
		}
	    }

	    currentState.completeTrans(trans);
	    stateVector.addElement(currentState.copy());
	    actionCount ++;
	}

	state = new BirState[actionCount + 1];
	for (int i = 0; i < stateVector.size(); i++)
	    state[i] = (BirState) stateVector.elementAt(i);

	// Stutter last state for trap action
	if (stateVector.size() <= actionCount) 
	    state[stateVector.size()] = state[stateVector.size() - 1];
	numActions = actionCount;
    }

    public String evalExpr(Expr expr, int stateIndex) {
	return state[stateIndex].exprValue(expr);
    }

    public int getNumActions() {
	return numActions;
    }

    public BirState getState(int stateIndex) {
	return state[stateIndex];
    }

    public TransSystem getTransSystem() {
	return system;
    }

    public TransVector getTransVector() {
	return transVector;
    }

    public Action getTrapAction() {
	if (trapTrans == null)
	    return null;
	if (trapActionNum > 0)
	    return trapTrans.getActions().elementAt(trapActionNum - 1);
	else // trap in guard --- report Stmt of first Action as source
	    return trapTrans.getActions().elementAt(0);
    }

    public String getTrapName() {
	return trapName;
    }

    public Transformation getTrapTrans() {
	return trapTrans;
    }

    public boolean isComplete() {
	return completed;
    }

    public boolean isDepthExceeded() {
	return depthexceeded;
    }

    public boolean isLimitViolation() {
	return limitviolation;
    }

    public boolean isOutOfMemory() {
	return outofmemory;
    }

    public boolean isVectorExceeded() {
	return vectorexceeded;
    }

    public boolean isVerified() {
	return verified;
    }

    public void print(boolean showStates) {
	if (! completed)
	    System.out.println("CHECKER FAILED");
	else if (verified)
	    System.out.println("VERIFIED");
	else {
	    int actionCount = 0;
	    System.out.println("BIR TRACE:");
	    if (showStates) 
		state[0].print();
	    for (int i = 0; i < transVector.size(); i++) {
		int tid = ((Integer) tidVector.elementAt(i)).intValue();
		Transformation trans = transVector.elementAt(i);
		Location fromLoc = trans.getFromLoc();
		System.out.println("[" + tid + "] TRANS " + 
				   fromLoc.getThread().getName() + ": " +
				   fromLoc.getLabel());
		BirPrinter.printTrans(trans);
		actionCount += trans.getActions().size();
		if (showStates && actionCount < state.length)
		    state[actionCount].print();
	    }
	    if (trapName != null) 
		System.out.println("TRAP " + trapName);
	}
    }
    /**
     * Set the choice number of the given action of the current transformation.
     */

    public void setChoice(int actionNum, int choiceNum) {
	// subtrace 1 since action numbers start from 1
	int actionChoices [] = (int []) choiceVector.lastElement();
	actionChoices[actionNum - 1] = choiceNum;
    }

    public void setDepthExceeded() {
	this.depthexceeded = true;
	this.verified = false;
	this.completed = true;
    }

    public void setOutOfMemory() {
	this.outofmemory = true;
	this.verified = false;
	this.completed = true;
    }
    /**
     * Set the trap info for the trace.
     */

    public void setTrap(String trapName, Transformation trapTrans, 
			int actionNum) { 
	this.trapName = trapName;
	limitviolation = trapName.endsWith("LimitException"); 
	this.trapTrans = trapTrans;
	this.trapActionNum = actionNum;
    }
    public void setVectorExceeded() {
	this.vectorexceeded = true;
	this.verified = false;
	this.completed = true;
    }
    public void setVerified(boolean verified) { 
	this.verified = verified; 
	this.completed = true;
    }

    /**
     * Parse a line of the spin simulator output, possibly adding a transform
     * to the trace.
     * <p>
     * Most of the lines (generated by spin) we ignore, but the PROMELA
     * contains special print statements that generate lines containing
     * the strings "BIR: L T A S" or "BIR? A C".  The string "BIR: L T A S"
     * is printed when executing an action: L is the location number, T
     * is the index of the transform out of that location, A is the index
     * of the action in the transform (0 is the guard, the actions begin
     * with 1), and S is the status of the action (usually OK, but may
     * be a trap name).  The string "BIR? A C" is printed when an
     * action makes a choice (e.g., choose expression): A is the
     * action index (the transformation is assumed to be the last
     * one for which a "BIR: ..." was seen), and C is an integer
     * representing the choice.
     */
    private void processTrail(java.util.List l) {

	if (l == null || l.size() == 0) {
	    log.debug("processTrail(List) got an empty trail.");
	    setVerified(true);
	    return;
	}

	java.util.List threadIDList = new java.util.ArrayList();
	for (java.util.Iterator i = l.iterator(); i.hasNext(); ) {
	    String line = (String) i.next();

	    if(line.startsWith("pan:")) {
		if(line.indexOf("acceptance cycle") >= 0) {
		    livenessPropertyViolation = true;
		    log.debug("Found the pan output denoting a liveness property claim was voilated.");
		}
		else if(line.indexOf("invalid endstate") >= 0) {
		    deadlockViolation = true;
		    log.debug("Found the pan output denoting that a deadlock was found.");
		}
		else if(line.indexOf("claim violated") >= 0) {
		    propertyViolation = true;
		    log.debug("Found the pan output denoting a claim was violated.");
		}
		else if(line.indexOf("assertion violated") >= 0) {
		    assertionViolation = true;
		    log.debug("Found the pan output denoting an assertion was violated.");
		}
		else {
		    log.debug("This pan output was ignored: " + line);
		}

		continue;
	    }
	    else if (!line.startsWith("BIR")) {
		continue;
	    }
	    
	    try {
		StringTokenizer st = new StringTokenizer(line.substring(4));
		if (line.startsWith("BIR:")) {
		    // BIR: <tid> <loc> <trans> <action> <status>
		    log.debug(line);
		    int tid = Integer.parseInt(st.nextToken()) - 1;  // tids are 0-based
		    threadIDList.add(new Integer(tid));
		    int locId = Integer.parseInt(st.nextToken());
		    int transNum = Integer.parseInt(st.nextToken());
		    int actionNum = Integer.parseInt(st.nextToken());
		    String status = st.nextToken();
		    Location loc = system.getLocation(locId);
		    Transformation trans = loc.getOutTrans().elementAt(transNum);
		    if (status.equals("OK")) {
			// Add trans only for first action
			if (actionNum == 1) {
			    addTid(tid);
			    addTrans(trans);
			}
		    }
		    else {
			// Status not OK---record trap
			setTrap(status, trans, actionNum);
		    }
		}
		else if (line.startsWith("BIR?")) {
		    // BIR? <action> <choice>
		    log.debug(line);
		    int actionNum = Integer.parseInt(st.nextToken());
		    int choiceNum = Integer.parseInt(st.nextToken());
		    setChoice(actionNum, choiceNum);
		}
		else {
		    log.debug("this line is ignored: " + line);
		}
	    }
	    catch (Exception e) {
		log.error("Exception while parsing the BIR trace.  line = " + line, e);
		throw new RuntimeException("Exception while parsing the BIR trace.  line = " + line, e);
	    }
	}

	// convert the thread id list to an array of int
	if((threadIDList != null) && (threadIDList.size() > 0)) {
	    threadIDs = new int[threadIDList.size()];
	    for(int i = 0; i < threadIDs.length; i++) {
		Integer integer = (Integer)threadIDList.get(i);
		threadIDs[i] = integer.intValue();
		//System.out.println("threadIDs[" + i + "] = " + threadIDs[i]);
	    }
	}
	
	done();
	setVerified(false);
    }

    /**
     * Get the array of thread IDs that map to statements.  The value at
     * threadIDs[i] is the thread ID of the thread that is running at statement i.
     *
     * @return int[]
     */
    public int[] getThreadIDs() {
	return(threadIDs);
    }
    private int[] threadIDs;


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
