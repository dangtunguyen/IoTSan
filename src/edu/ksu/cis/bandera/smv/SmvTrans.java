package edu.ksu.cis.bandera.smv;

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
import java.util.Hashtable;

import edu.ksu.cis.bandera.bir.*;
import edu.ksu.cis.bandera.checker.*;
import java.util.List;

/**
 * SmvTrans is the main class of the SMV translator, which generates
 * SMV source representing a transition system.
 * <p>
 * The SMV translator is invoked on a transition system as follows:
 * <pre>
 * // Parameters 
 * TransSystem system = ...;   // the transition system
 * SmvOptions options = new SmvOptions();        // Options
 * options.setSafety(true);          // Safety property
 * options.setInterleaving(true);    // Interleaving (vs. simulataneous) model
 * SmvTrans smv = SmvTrans.translate(sys,options);   // Generate input
 * smv.runSmv();   // Run nusmv
 * BirTrace trace = smv.parseOutput();   // Parse the output
 * </pre>
 */

public class SmvTrans extends AbstractExprSwitch implements BirConstants, Checker
{
	TransSystem system;
	PrintWriter out;             // Destination for translator output

	boolean ranVerifier;         // Was the verifier run (using runSmv())?

	SmvOptions options;          // Options for translation:
	boolean liveness;            //   liveness property (vs. safety)
	boolean ctlproperty;         //   ctl property (vs. deadlock)
	boolean interleaving;        //   interleaving model (vs. simulataneous)

	int indentLevel = 0;         // Indentation level in output
	boolean newLine = true;      // At beginning of line in output?

	Hashtable varTable = new Hashtable();  // Maps name (String) -> SmvVar
	Vector varList = new Vector();         // Holds all SmvVars

	Hashtable guardTable = new Hashtable();  // Maps guard Exprs -> SmvExpr
	// (so we don't translate a given guard expression more than once)

	int maxCollectionSize;  // Size of largest collection
	String threadType;      // SMV type for thread = "{ NoThread, T1, T2 ... }"

	Transformation currentTrans;        // Current trans being processed
	SmvExpr currentGuard;               // Guard of that trans

	SmvVar choiceVar;    // Choice variable (nondeterministic)
	SmvVar trapVar;      // Trap variable (holds trap name)
	SmvVar runningVar;   // Running variable (holds name of thread taking step)
	

	final static SmvLit ZERO = new SmvLit("0");
	final static SmvLit ONE = new SmvLit("1");
	final static SmvLit NOTHREAD = new SmvLit("NoThread");
	final static String TRAP_TYPE = 
	"{ None, NullPointerException, ArrayBoundsException, ClassCastException, RangeLimitException, CollectionLimitException, ArrayLimitException }";

	// This type controls the number of times a thread may reacquire a lock
	final static String LOCK_COUNT_TYPE = "0 .. 3"; 

	// Variables used in parsing SMV output
	boolean doneParsing;         // finished
	boolean foundFalse;         // finished
	boolean foundTrace;          // found a counterexample in the output
	int stateCount;              // Number of states in counterexample so far
	Location [] threadLocs;      // Current location of each thread in trace
	Transformation lastTrans;    // Last transform from trace
	Hashtable threadTable;       // Maps name of thread -> thread ID

	// Vars for execAndWait()
	static Runtime runtime = Runtime.getRuntime();
	static final int BUFSIZE = 50000;
	static byte [] buffer = new byte[BUFSIZE];
	SmvTrans(TransSystem system, SmvOptions options, PrintWriter out) {
	this.system = system;
	this.options = options;
	this.out = out;
	liveness = ! options.getSafety();
	ctlproperty = options.getProperty();
	interleaving = options.getInterleaving();

	// Build thread type
	ThreadVector threads = system.getThreads();
	threadType = "NoThread";
	for (int i = 0; i < threads.size(); i++) 
	    threadType += ", " + threads.elementAt(i).getName();
	threadType = "{ " + threadType + " }";
	}
	// These functions define how the SMV variables are assigned names

	String activeVar(BirThread thread) {
	return thread.getName() + "_active";
	}
	/**
	 * Add the conditional context to an N-ary AND-expression.
	 * <p>
	 * The context passed is an SmvCase that is part of a case tree.
	 * The condition on every ancestor of the conext is added to the 
	 * AND-expression.  The context can be null, in which case
	 * nothing is added.
	 */

	SmvNaryExpr addConditionalContext(SmvNaryExpr expr, SmvCase context) {
	while (context != null) {
	    expr.add(context.cond);
	    context = context.parent.outerCase;
	}
	return expr;
	}
	SmvExpr and(SmvExpr expr1, SmvExpr expr2) 
	{
	return new SmvBinaryExpr("&",expr1,expr2);
	}
	/**
	 * Generate SMV expression that says var becomes expr in next state.
	 * <p>
	 * To simplify reference assignments (from one var to another),
	 * we test to see if the var is an aggregate (which means its a reference)
	 * and if the expr is also a variable---in which case we generate
	 * a conjunction saying each component is equal (this avoid the
	 * multiplication that would otherwise be generated to view
	 * the reference as a single value).
	 */

	static SmvExpr becomes(SmvVar var, SmvExpr expr) 
	{
	if (var.getComponents().size() > 0 && (expr instanceof SmvVar)) {
	    SmvNaryExpr componentsEqual = new SmvNaryExpr("&",ONE,true);
	    for (int i = 0; i < var.getComponents().size(); i++) 
		componentsEqual.add(becomes(var.getComponent(i),
					    ((SmvVar)expr).getComponent(i)));
	    return componentsEqual;
	} else
	    return new SmvBinaryExpr("=",new SmvNextExpr(var),expr);
	}
	String blockedVar(BirThread thread) {
	return thread.getName() + "_blocked";
	}
	/**
	 * Build an expression for a given transformation.
	 */

	SmvExpr buildTransExpr(Transformation trans, StateVarVector locals,
			   SmvVar locVar) {
	currentTrans = trans;
	Expr guard = trans.getGuard();

	// We cache the translation of the guard expression so we 
	// don't need to repeat it
	currentGuard = (guard == null) ? ONE : translate(guard);
	guardTable.put(trans,currentGuard);
	// Start with basic transTaken expr (loc updated and guard true)
	SmvNaryExpr transExpr = transTakenExpr(trans);

	// If the trans has an assignment to a local, we process that,
	// else check for updates to globals
	AssignAction localAssign = findLocalAssign(trans);
	if (localAssign == null)
	    recordGlobalVarUpdates();

	// If live local set specifed, use that, else assume all locals live
	StateVarVector liveLocals = locals;
	if (trans.getToLoc().getLiveVars() != null)
	    liveLocals = trans.getToLoc().getLiveVars();

	// Each live local is either assigned by the trans or left unchanged
	for (int i = 0; i < liveLocals.size() ; i++) {
	    StateVar local = liveLocals.elementAt(i);
	    SmvVar localVar = getVar(local.getName());
	    if (localAssign != null && localAssign.getLhs().equals(local)) {
		SmvExpr rhsExpr = translate(localAssign.getRhs());
		checkAssign(local,localAssign.getRhs(),localVar,rhsExpr);
		transExpr.add(becomes(localVar,rhsExpr));
	    } else // local unchanged
		transExpr.add(becomes(localVar,localVar));
	}

	// For liveness properties, update T_idle
	if (liveness) {
	    SmvVar idleVar = getVar(idleVar(trans.getFromLoc().getThread()));
	    transExpr.add(becomes(idleVar,ZERO));
	}

	currentTrans = null;
	currentGuard = null;
	return transExpr;
	}
	public void caseAddExpr(AddExpr expr)
	{
	   translateBinaryOp(expr.getOp1(), expr.getOp2(), "+");
	}
	public void caseAndExpr(AndExpr expr)
	{
	   translateBinaryOp(expr.getOp1(), expr.getOp2(), "&");
	}
	/**
	 * Build an SMV expression for a BIR array selection.
	 */

	public void caseArrayExpr(ArrayExpr expr) {
	SmvExpr array = translate(expr.getArray());
	SmvExpr index = translate(expr.getIndex());
	int size = ((Array)expr.getArray().getType()).getSize().getValue();

	if (array instanceof SmvCaseExpr) { // array on heap (from Deref)
	    Vector cases = ((SmvCaseExpr)array).collectCases(new Vector());
	    for (int i = 0; i < cases.size(); i++) {
		SmvCase smvCase = (SmvCase) cases.elementAt(i);
		SmvVar arrayVar = (SmvVar) smvCase.expr;
		SmvCaseExpr elemSelect = new SmvCaseExpr();
		for (int j = 0; j < size; j++) {
		    SmvLit elemNum = new SmvLit("" + j);
		    SmvExpr elemCond = new SmvBinaryExpr("=",index,elemNum);
		    SmvVar elem = getVar(elementVar(arrayVar.getName(),j));
		    elemSelect.addCase(elemCond,elem);
		}
		smvCase.updateCase(elemSelect);
	    }
	    setResult(array);  // Leaf cases have been modified

	} else if (array instanceof SmvVar) { // Singleton array
	    SmvVar arrayVar = (SmvVar) array;
	    SmvCaseExpr elemSelect = new SmvCaseExpr();
	    for (int j = 0; j < size; j++) {
		SmvLit elemNum = new SmvLit("" + j);
		SmvExpr elemCond = new SmvBinaryExpr("=",index,elemNum);
		SmvVar elem = getVar(elementVar(arrayVar.getName(),j));
		elemSelect.addCase(elemCond,elem);
	    }
	    setResult(elemSelect);
	} else
	    throw new RuntimeException("Unexpected array expr: " + expr);
	}
	public void caseBoolLit(BoolLit expr)
	{
	setResult(new SmvLit(expr.getValue() ? "1" : "0"));
	}
	/**
	 * Build an SMV expression for a choose() operation.
	 * <p>
	 * Given choices C0, C1, ..., CN, we generate:
	 * <pre>
	 *   case
	 *     choiceVar = 0 : C0;
	 *     choiceVar = 1 : C1;
	 *     ...
	 *     1 : CN;
	 *   esac
	 * </pre>
	 * where choiceVar is an unconstrained variable.
	 */

	public void caseChooseExpr(ChooseExpr expr)
	{
	Vector choices = expr.getChoices();
	SmvCaseExpr choose = new SmvCaseExpr();
	for (int i = 0; i < choices.size() ; i++) {
	    SmvExpr val = translate((Expr)choices.elementAt(i));
	    SmvExpr test = (i + 1 == choices.size()) ? ONE : 
		equal(choiceVar,new SmvLit("" + i));
	    choose.addCase(test,val);
	}
	setResult(choose);
	}
	public void caseConstant(Constant expr)
	{
	setResult(new SmvLit(expr.getName()));
	}
	/**
	 * Generate an SMV expression for a BIR dereference.
	 */

	public void caseDerefExpr(DerefExpr expr) {
	// Can assume ref expr is simple variable
	SmvVar refVar = (SmvVar) translate((StateVar)expr.getTarget());
	SmvVar refIndexVar = refVar.getComponent(0);
	SmvVar instNumVar = refVar.getComponent(1);
	StateVarVector targets = 
	    ((Ref)expr.getTarget().getType()).getTargets();
	// Build nested case expressions: outer selects collection
	SmvCaseExpr colSelect = new SmvCaseExpr();
	for (int i = 0; i < targets.size(); i++) {
	    StateVar target = targets.elementAt(i);
	    SmvLit refIndexVal = new SmvLit("" + refIndex(target));
	    SmvExpr cond = equal(refIndexVar,refIndexVal);

	    // inner selects instance of collection (unless target singleton).
	    if (target.getType().isKind(COLLECTION)) {
		SmvCaseExpr instSelect = new SmvCaseExpr();
		int size = ((Collection)target.getType()).getSize().getValue();
		for (int j = 0; j < size; j++) {
		    SmvLit instNumVal = new SmvLit("" + j);
		    SmvExpr instCond = equal(instNumVar,instNumVal);
		    SmvVar inst = getVar(instanceVar(target.getName(),j));
		    instSelect.addCase(instCond,inst);
		}
		colSelect.addCase(cond,instSelect);
	    } else {  // Singleton
		SmvVar singleton = getVar(target.getName());
		colSelect.addCase(cond,singleton);
	    }
	}
	
	// Check for null reference (refIndex = 0) and set trap
	SmvNaryExpr couldBeTaken = transCouldBeTakenExpr(currentTrans);
	couldBeTaken.add(equal(refIndexVar,ZERO));
	trapVar.addUpdate(couldBeTaken,
			  becomes(trapVar,new SmvLit("NullPointerException")));

	setResult(colSelect);
	}
	public void caseDivExpr(DivExpr expr)
	{
	   translateBinaryOp(expr.getOp1(), expr.getOp2(), "/");
	}
	public void caseEqExpr(EqExpr expr)
	{
	SmvExpr expr1 = translate(expr.getOp1());
	SmvExpr expr2 = translate(expr.getOp2());
	setResult(equal(expr1,expr2));
	}
	/**
	 * Build SMV expr to check if object is instanceof class.
	 * <p>
	 * We use an SMV set expression to test if the refIndex var
	 * of the reference is "in" the set of refIndexes of
	 * the class/interface.
	 */

	public void caseInstanceOfExpr(InstanceOfExpr expr)
	{
	// This could be generalized to handle case trees
	// but this suffices for the current Jimple/BIRC-generated BIR.
	if (! (expr.getRefExpr() instanceof StateVar))
	    throw new RuntimeException("InstanceOf must apply to var");	
	SmvVar refVar = (SmvVar) translate(expr.getRefExpr());
	SmvVar refIndexVar = getVar(refIndexVar(refVar.getName()));
	String indexSet = "";
	/* commented out to match SpinTrans.java and to get it to compile.  This might
	 * be completely wrong! -tcw
	StateVarVector targets = expr.getRefType().getTargets();
	for (int j = 0; j < targets.size(); j++) {
	    StateVar target = targets.elementAt(j);
	    if (j > 0) indexSet += ", ";
	    indexSet += refIndex(target);
	}
	*/
	SmvLit set = new SmvLit("{ " + indexSet + " }");
	setResult(new SmvBinaryExpr("in",refIndexVar,set));
	}
	public void caseIntLit(IntLit expr)
	{
	setResult(new SmvLit("" + expr.getValue()));
	}
	public void caseLeExpr(LeExpr expr)
	{
		translateBinaryOp(expr.getOp1(), expr.getOp2(), "<=");
	}
	/**
	 * Build SMV expr to retrieve array length.
	 */

	public void caseLengthExpr(LengthExpr expr)
	{
	SmvExpr array = translate(expr.getArray());
	if (array instanceof SmvCaseExpr) { //  array on heap (from Deref)
	    Vector cases = ((SmvCaseExpr)array).collectCases(new Vector());
	    for (int i = 0; i < cases.size(); i++) {
		SmvCase smvCase = (SmvCase) cases.elementAt(i);
		SmvVar arrayVar = (SmvVar) smvCase.expr;
		SmvVar lengthVar = getVar(lengthVar(arrayVar.getName()));
		smvCase.updateCase(lengthVar);
	    }
	    setResult(array);  // Leaf cases have been modified
	} else if (array instanceof SmvVar) { // static array
	    SmvVar arrayVar = (SmvVar) array;
	    SmvVar lengthVar = getVar(lengthVar(arrayVar.getName()));
	    setResult(lengthVar);
	} else
	    throw new RuntimeException("Unexpected array expr: " + expr);
	}
	/**
	 * Build an SMV expression for a lock test.
	 */

	public void caseLockTest(LockTest lockTest) {
	BirThread thread = lockTest.getThread();
	SmvExpr lock = translate(lockTest.getLockExpr());
	Vector cases;
	if (lock instanceof SmvCaseExpr) {  // lock on heap (from Deref)
	    cases = ((SmvCaseExpr)lock).collectCases(new Vector());
	    setResult(lock);  // cases updated below
	} else
	    throw new RuntimeException("Unexpected lock expr: " + lock);
	switch (lockTest.getOperation()) {
	case LOCK_AVAILABLE:
	    for (int i = 0; i < cases.size(); i++) {
		SmvCase smvCase = (SmvCase) cases.elementAt(i);
		SmvVar lockVar = (SmvVar) smvCase.expr;
		SmvVar ownerVar = getVar(ownerVar(lockVar.getName()));
		SmvExpr free = equal(ownerVar,NOTHREAD);
		SmvExpr relock = equal(ownerVar,new SmvLit(thread.getName()));
		SmvExpr avail = new SmvBinaryExpr("|",free,relock);
		smvCase.updateCase(avail);
	    }
	    return;
	case HAS_LOCK:
	    setResult(ONE);  // Not used in this translation
	    return;
	case WAS_NOTIFIED:
	    for (int i = 0; i < cases.size(); i++) {
		SmvCase smvCase = (SmvCase) cases.elementAt(i);
		SmvVar lockVar = (SmvVar) smvCase.expr;
		SmvVar waitVar = getVar(waitVar(lockVar.getName(),thread));
		smvCase.updateCase(not(waitVar));
	    }
	    return;	    
	}	
	}
	public void caseLtExpr(LtExpr expr)
	{
		translateBinaryOp(expr.getOp1(), expr.getOp2(), "<");
	}
	public void caseMulExpr(MulExpr expr)
	{
	   translateBinaryOp(expr.getOp1(), expr.getOp2(), "*");
	}
	public void caseNeExpr(NeExpr expr)
	{
	SmvExpr expr1 = translate(expr.getOp1());
	SmvExpr expr2 = translate(expr.getOp2());
	SmvExpr equals = equal(expr1,expr2);
	setResult(not(equals));
	}
	/**
	 * Build SMV expression for array allocator.
	 * <p>
	 * Similar to NewExpr, but must initialize length/contents of array.
	 */

	public void caseNewArrayExpr(NewArrayExpr expr)
	{
	// First, generate the case expr that gives the value of
	// the allocator (depending on the current values of the inuse vars)
	StateVar target = expr.getCollection();
	String collectName = target.getName();
	int size = ((Collection)target.getType()).getSize().getValue();
	int refIndex = refIndex(target);
	SmvExpr setLength = translate(expr.getLength());
	SmvCaseExpr instNum = new SmvCaseExpr();
	for (int i = 0; i < size; i++) {
	    SmvVar inuse = getVar(inUseVar(collectName,i));
	    instNum.addCase(not(inuse), refValue(refIndex,i));
	}

	// Then record the potential updates to the inuse vars
	// when this trans executes: inuse_i changes from 0 to 1
	// if this trans is executed when inuse_j = 1 for all j < i
	for (int i = 0; i < size; i++) {
	    SmvNaryExpr taken = transTakenExpr(currentTrans);
	    SmvVar inuse_i = getVar(inUseVar(collectName,i));
	    for (int j = 0; j < i; j++) {
		SmvVar inuse_j = getVar(inUseVar(collectName,j));
		taken.add(equal(inuse_j,ONE));
	    }
	    taken.add(equal(inuse_i,ZERO));
	    inuse_i.addUpdate(taken,becomes(inuse_i,ONE));

	    // Also initialize the length and elements of the newly allocated
	    // array
	    SmvVar instVar = getVar(instanceVar(collectName,i));
	    Array type = (Array) ((Collection)target.getType()).getBaseType();
	    int maxLen = type.getSize().getValue();
	    SmvVar lengthVar = getVar(lengthVar(instVar.getName()));
	    lengthVar.addUpdate(taken,becomes(lengthVar,setLength));
	    for (int j = 0; j < maxLen; j++) {
		SmvVar elemVar = getVar(elementVar(instVar.getName(),j));
		initAllComponents(elemVar,taken);
	    }
	}

	setResult(instNum);
	}
	/**
	 * Build SMV expression for allocator.
	 */

	public void caseNewExpr(NewExpr expr)
	{
	// First, generate the case expr that gives the value of
	// the allocator (depending on the current values of the inuse vars):
	//   case 
	//     ! inuse0 : 0;   // actually need refValue of instance 0, not 0
	//     ! inuse1 : 1;
	//     ...
	//   esac
	StateVar target = expr.getCollection();
	String collectName = target.getName();
	int refIndex = refIndex(target);
	int size = ((Collection)target.getType()).getSize().getValue();
	SmvCaseExpr instNum = new SmvCaseExpr();
	for (int i = 0; i < size; i++) {
	    SmvVar inuse = getVar(inUseVar(collectName,i));
	    instNum.addCase(not(inuse), refValue(refIndex,i));
	}

	// Then record the potential updates to the inuse vars
	// when this trans executes: inuse_i changes from 0 to 1
	// if this trans is executed when inuse_j = 1 for all j < i
	for (int i = 0; i < size; i++) {
	    SmvNaryExpr taken = transTakenExpr(currentTrans);
	    SmvVar inuse_i = getVar(inUseVar(collectName,i));
	    for (int j = 0; j < i; j++) {
		SmvVar inuse_j = getVar(inUseVar(collectName,j));
		taken.add(equal(inuse_j,ONE));
	    }
	    taken.add(equal(inuse_i,ZERO));
	    inuse_i.addUpdate(taken,becomes(inuse_i,ONE));

	    // Also initialize all variables in the newly allocated item
	    SmvVar instVar = getVar(instanceVar(collectName,i));
	    initAllComponents(instVar,taken);
	}

	setResult(instNum);
	}
	public void caseNotExpr(NotExpr expr)
	{
	translateUnaryOp(expr.getOp(),"!");
	}
	public void caseNullExpr(NullExpr expr)
	{
	setResult(ZERO);
	}
	public void caseOrExpr(OrExpr expr)
	{
	   translateBinaryOp(expr.getOp1(), expr.getOp2(), "|");
	}
	/**
	 * Build SMV expression for record field selection.
	 */

	public void caseRecordExpr(RecordExpr expr) {
	SmvExpr record = translate(expr.getRecord());
	Field field = expr.getField();
	if (record instanceof SmvCaseExpr) {  // record on heap (from Deref)
	    Vector cases = ((SmvCaseExpr)record).collectCases(new Vector());
	    for (int i = 0; i < cases.size(); i++) {
		SmvCase smvCase = (SmvCase) cases.elementAt(i);
		SmvVar recVar = (SmvVar) smvCase.expr;
		SmvVar fieldVar = getVar(fieldVar(recVar.getName(),field));
		smvCase.updateCase(fieldVar);
	    }
	    setResult(record);  // Leaves updates

	} else if (record instanceof SmvVar) { // static record
	    setResult(getVar(fieldVar(((SmvVar)record).getName(),field)));
	} else
	    throw new RuntimeException("Unexpected record expr: " + expr);
	}
	public void caseRefExpr(RefExpr expr)
	{
	setResult(refValue(refIndex(expr.getTarget()),0));
	}
	public void caseRemExpr(RemExpr expr)
	{
	   translateBinaryOp(expr.getOp1(), expr.getOp2(), "mod");
	}
	public void caseStateVar(StateVar expr)
	{
	setResult(getVar(expr.getName()));
	}
	public void caseSubExpr(SubExpr expr)
	{
	   translateBinaryOp(expr.getOp1(), expr.getOp2(), "-");
	}
	public void caseThreadLocTest(ThreadLocTest threadLocTest) 
	{
	SmvVar threadLocVar = getVar(locVar(threadLocTest.getThread()));
	SmvLit locName = new SmvLit(locName(threadLocTest.getLocation()));
	SmvExpr testExpr = new SmvBinaryExpr("=",threadLocVar,locName);
	setResult(testExpr);
	}
	public void caseThreadTest(ThreadTest threadTest) {
	switch (threadTest.getOperation()) {
	case THREAD_TERMINATED:
	    /* commented out to get this to compile.  this is definitely wrong! -tcw
	    SmvVar activeVar = getVar(activeVar(threadTest.getThreadArg()));
	    setResult(not(activeVar));
	    */
	    return;
	}	
	}
	public String check() { return runSmv(); }
	/**
	 * Check an assignment for ClassCastExceptions (updating trapVar).
	 * Check an assignment for IntLimitExceptions (updating trapVar).
	 */

	void checkAssign(StateVar lhs, Expr rhs, SmvVar var, SmvExpr value) {
	// If RHS type not subtype of LHS type
	if (! rhs.getType().isSubtypeOf(lhs.getType())) {

	    // If LHS is REF type and RHS not 'null'
	    if (lhs.getType().isKind(REF) && ! (rhs instanceof NullExpr)) {

		if (! (rhs instanceof StateVar))  // Should be generalized
		    throw new RuntimeException("Can only check var to var ref assignments");

		// ClassCastException occurs if this transformation could
		// be taken and the target of the RHS is not in the
		// set of targets of the LHS reference type.
		SmvVar refIndexVar = 
		    getVar(refIndexVar(((StateVar)rhs).getName()));
		SmvNaryExpr couldBeTaken = transCouldBeTakenExpr(currentTrans);
		SmvNaryExpr notSupClass = new SmvNaryExpr("|",ONE,true);
		couldBeTaken.add(notSupClass);
		SmvLit trap = new SmvLit("ClassCastException");
		StateVarVector lhsTargets = ((Ref)lhs.getType()).getTargets();
		StateVarVector rhsTargets = ((Ref)rhs.getType()).getTargets();
		for (int j = 0; j < rhsTargets.size(); j++) {
		    StateVar target = rhsTargets.elementAt(j);
		    if (! lhsTargets.contains(target)) {
			SmvLit refIndex = new SmvLit("" + refIndex(target));
			notSupClass.add(equal(refIndexVar,refIndex));
		    }
		}
		trapVar.addUpdate(couldBeTaken, becomes(trapVar,trap));
	    }
	}
	// If LHS is out of range 
		//if (lhs.getType().isKind(RANGE)) {
	//    Range range = (Range) lhs.getType();
	//    SmvNaryExpr couldBeTaken = transCouldBeTakenExpr(currentTrans);
		//    SmvExpr low = greaterThan(value,
		//                              new SmvLit("" + range.getFromVal()));
		//    SmvExpr hi = lessThan(value,
		//                          new SmvLit("" + range.getToVal()));
	//    couldBeTaken.add(not(and(hi,low)));
	//    SmvLit trap = new SmvLit("RangeLimitException");
		//    trapVar.addUpdate(couldBeTaken, becomes(trapVar,trap));
	//}
		}
	String countVar(String var) {
	return var + "_count";
	}
	static String currentDir() { return System.getProperty("user.dir"); }
	/**
	 * Declare an SMV state variable
	 * @param name of variable 
	 * @param its SMV type (or null if it's an aggregate)
	 * @param the BIR StateVar it represents (a part of)
	 * @param its initial value (or null if it has none)
	 * @param the aggregate SmvVar it is nested within (or null)
	 * @param flag indicating whether var is constrained (has TRANS formula)
	 * @param an SmvVar that, if false, indicates this variable is dead
	 */

	public SmvVar declareVar(String name, String smvType, StateVar source, 
			     String initValue, SmvVar aggregate,
			     boolean constrained, SmvVar deadFlag) {
	SmvLit initVal = (initValue != null) ? new SmvLit(initValue) : null;
	SmvVar var = new SmvVar(name, smvType, source, initVal, 
				constrained, deadFlag);
	if (varTable.get(name) != null) 
	    throw new RuntimeException("Error: name conflict " + name);
	if (aggregate != null) {
	    aggregate.addComponent(var);
	    var.setAggregate(aggregate);
	}
	varList.addElement(var);
	varTable.put(name,var);
	return var;
	}
	/**
	 * Declare all variables using SmvTypeDecl to walk down through
	 * composite types.
	 */

	void declareVariables() {
	SmvTypeDecl declarator = new SmvTypeDecl(this, system);
	StateVarVector stateVarVector = system.getStateVars();

	// For an interleaving model, we have a runningVar that contains
	// the name of the thread executing on this step
	if (interleaving)
	    runningVar = declareVar("runningThread",threadType,null,null,
				    null,false,null);

	// Create location/active variables for each thread
	ThreadVector threadVector = system.getThreads();
	for (int i = 0; i < threadVector.size(); i++) {
	    BirThread thread = threadVector.elementAt(i);
	    LocVector threadLocVector = thread.getLocations();
	    String locType = "";     // Enumerated location type
	    for (int j = 0; j < threadLocVector.size(); j++) {
		Location loc = threadLocVector.elementAt(j);
		if (j > 0) locType += ", ";
		locType += locName(loc);
	    }
	    locType = "{ " + locType + " }";
	    declareVar(locVar(thread), locType, null, 
		       locName(thread.getStartLoc()),null,false,null);

	    // Need active flag if not main
	    if (! thread.isMain())
		declareVar(activeVar(thread),"boolean",null,"0",
			   null,true,null);

	    // For a liveness property, need an idle variable for each thread
	    // that is true if the thread idles on this step
	    if (liveness)
		declareVar(idleVar(thread),"boolean",null,"0",null,false,null);

	    // Each thread also has a temp variable (to store the lock count
	    // while waiting)
	    declareVar(tempVar(thread), LOCK_COUNT_TYPE, 
		       null,null,null,true,null);

	    // Also declare locals for this thread using SmvTypeDecl switch
	    for (int j = 0; j < stateVarVector.size(); j++) {
		StateVar var =  stateVarVector.elementAt(j);
		if (var.getThread() == thread) 
		    var.getType().apply(declarator, var);
	    }
	}

	// Create one or more variables for each global StateVar
	for (int j = 0; j < stateVarVector.size(); j++) {
	    StateVar var =  stateVarVector.elementAt(j);
	    if (var.getThread() == null) 
		var.getType().apply(declarator, var);
	}

	// Create a (unconstrained) variable for use is ChooseExpr
	choiceVar = 
	    declareVar("smv_choice","0 .. 31",null,null,null,false,null);

	// Create a global to store the trap condition (if any)
	trapVar = 
	    declareVar("smv_trap",TRAP_TYPE,null,"None",null,true,null);

	// Retrieve max collection size (used in deref)
	maxCollectionSize = declarator.getMaxCollectionSize();
	}
	public void defaultCase(Object obj)
	{
	throw new RuntimeException("Trans type not handled: " + obj);
	}
	/**
	 * Generate DEFINE sectoin, which defines the following symbols:
	 * <ol>
	 * <li> MAX_COLLECT_SIZE
	 * <li> Enumeration and constant names
	 * <li> T_blocked for each thread T
	 * <li> T_terminated for each thread T
	 * </ol>
	 */

	void definitions() {
	Vector definitions = system.getDefinitions();
	println("DEFINE");
	println("  MAX_COLLECT_SIZE := " + maxCollectionSize + ";");

	// BIR definitions (constants)
	for (int i = 0; i < definitions.size(); i++) {
	    Definition def = (Definition) definitions.elementAt(i);
	    if (def instanceof Constant) 
		println("  " + def.getName() + " := " + def.getDef() +
			    ";");
	    else if (def instanceof Enumerated) {
		Enumerated enumVar = (Enumerated) def;
		for (int j = 0; j < enumVar.getEnumeratedSize(); j++) {
		    String name = enumVar.getNameOf(enumVar.getFirstElement()+j);
		    if (name != null)
			println("  " + name + " := " + 
				   (enumVar.getFirstElement()+j) + ";");
		}
	    }
	}

	// Add predicates for CTL property or deadlock
	if (ctlproperty) {
	    // User defined predicates
	    Vector preds = system.getPredicates();
	    for (int i = 0; i < preds.size(); i++) {
		Expr pred = (Expr) preds.elementAt(i);
		String name = system.predicateName(pred);
		println("  " + name + " := ");
		indent(2);
// NOT FINISHED! Need to convert the Expr to an SMVExpr
// print(applyTo(pred).getCase(normal));
		println("true");
// printed true just to make legal SMV
		println(";");
		indent(-2);
	    }
	} else {
	    // Thread blocked/terminated expressions
	    ThreadVector threadVector = system.getThreads();
	    for (int i = 0; i < threadVector.size(); i++) {
	        BirThread thread = threadVector.elementAt(i);
	        SmvExpr blocked = threadBlockedExpr(thread);
	        println("  " + blockedVar(thread) + " := ");
	        indent(2);
	        blocked.print(this);
	        println(";");
	        indent(-2);
	        SmvExpr terminated = threadTerminatedExpr(thread);
	        println("  " + terminatedVar(thread) + " := ");
	        indent(2);
	        terminated.print(this);
	        println(";");
	        indent(-2);
	    }
	}
	println();
	}
	String elementVar(String var, int i) {
	return var + "_e" + i;
	}
	SmvExpr equal(SmvExpr expr1, SmvExpr expr2) 
	{
	return new SmvBinaryExpr("=",expr1,expr2);
	}
	String execAndWait(String command, boolean verbose) {
	try {
	    if (verbose)
		System.out.println(command);
	    Process p = runtime.exec(command);
	    InputStream commandErr = p.getErrorStream();
	    InputStream commandOut = p.getInputStream();
	    int count = 0;       // total chars read
	    int charsRead = 0;   // chars read on read
	    int charsAvail = 0;  // chars available
	    while ((count < BUFSIZE) && (charsRead >= 0)) {
		charsAvail = commandErr.available();
		if (charsAvail > 0) 
		    count += commandErr.read(buffer,count,charsAvail);
		charsRead = commandOut.read(buffer,count,BUFSIZE - count);
		if (charsRead > 0)
		    count += charsRead;
	    }
	    p.waitFor();
	    String output = new String(buffer,0,count);
	    if (verbose && count > 0)
		System.out.println(output);
	    return output;
	}
	catch (Exception e) {
	    throw new RuntimeException("exec of command '" + command
				       + "' was aborted: \n" + e);
	}
	}
	/**
	 * Generate FAIRNESS clause for each thread T of the form:
	 * <pre>
	 * ! T_idle | T_blocked | T_terminated
	 * </pre>
	 */

	void fairness() {
	ThreadVector threadVector = system.getThreads();
	for (int i = 0; i < threadVector.size(); i++) {
	    BirThread thread = threadVector.elementAt(i);
	    SmvNaryExpr expr = new SmvNaryExpr("|",ZERO,true);
	    expr.add(not(getVar(idleVar(thread))));
	    expr.add(new SmvVar(blockedVar(thread)));
	    expr.add(new SmvVar(terminatedVar(thread)));
	    println("FAIRNESS  -- " + thread.getName());
	    expr.print(this);
	    println();
	}
	}
	String fieldVar(String var, Field type) {
	return var + "_" + type.getName();
	}
	/**
	 * Finds an assignment to a local variable in the transformation (or
	 * returns null if not found).
	 */

	AssignAction findLocalAssign(Transformation trans) {
	if (trans.getActions().size() > 1)
	    throw new RuntimeException("Multiple actions on transformation: " + trans);
	if (trans.getActions().size() == 0)
	    return null;
	Action action = trans.getActions().elementAt(0);
	if (action.isAssignAction()) {
	    AssignAction assign = (AssignAction) action;
	    if (assign.getLhs() instanceof StateVar) {
		StateVar var = (StateVar) assign.getLhs();
		if (var.getThread() != null)
		    return assign;
	    }
	} 
	return null;
	}
/**
 * getCounterExample method comment.
 */
public List getCounterExample() {
	return null;
}
	TransSystem getSystem() { return system; }
	String getThreadType() { return threadType; }
	/**
	 * Get SmvVar with given name.
	 */

	SmvVar getVar(String name) { 
	return (SmvVar) varTable.get(name); 
	}
	SmvExpr greaterThan(SmvExpr expr1, SmvExpr expr2) 
	{
	return new SmvBinaryExpr(">=",expr1,expr2);
	}
	/**
	 * Generate SMV header
	 */

	void header() {
	println("--  SMV for transition system: " 
		    + system.getName());
	println();
	println("MODULE main");
	}
	String idleVar(BirThread thread) {
	return thread.getName() + "_idle";
	}
	void indent(int delta) {
	indentLevel += delta;
	}
	/**
	 * Generate INIT formula that sets each non-aggregate variable
	 * with an initial value to that value.
	 */

	void init() {
	println("INIT");
	SmvNaryExpr expr = new SmvNaryExpr("&", ONE, false);
	for (int i = 0; i < varList.size(); i++) {
	    SmvVar var = (SmvVar)varList.elementAt(i);
	    // If not aggregate and is initially allocated
	    if (var.getType() != null && var.getDeadFlag() == null) {
		SmvLit initVal = var.getInitValue();
		// And has an initial value
		if (initVal != null)
		    expr.add(equal(var,initVal));
	    }
	}
	expr.print(this);
	println();	
	}
	/**
	 * Initialize all components in an SmvVar if indicated condition is true.
	 */

	void initAllComponents(SmvVar var, SmvExpr cond) {
	Vector components = var.getComponents();
	// If aggregate, initialize all components
	if (components.size() > 0) {
	    for (int i = 0; i < components.size(); i++)
		initAllComponents((SmvVar)components.elementAt(i),cond);
	} else {
	    // Else if simple var and has init value, make conditional update
	    if (var.getInitValue() != null) {
		var.addUpdate(cond,becomes(var,var.getInitValue()));
	    }
	}
	}
	String instanceVar(String var, int i) {
	return var + "_i" + i;
	}
	String instNumVar(String var) {
	return var + "_instNum";
	}
	String inUseVar(String var, int i) {
	return var + "_inuse" + i;
	}
	String lengthVar(String var) {
	return var + "_length";
	}
	SmvExpr lessThan(SmvExpr expr1, SmvExpr expr2) 
	{
	return new SmvBinaryExpr("<=",expr1,expr2);
	}
	/**
	 * Name of location.
	 */

	String locName(Location loc) {
	return "loc" + loc.getId();
	}
	String locVar(BirThread thread) {
	return thread.getName() + "_loc";
	}
	SmvExpr minus1(SmvExpr expr) 
	{
	return new SmvBinaryExpr("-",expr,ONE);
	}
	SmvExpr not(SmvExpr expr)
	{
	if (expr instanceof SmvUnaryExpr &&
	    ((SmvUnaryExpr)expr).operator.equals("!"))
	    return ((SmvUnaryExpr)expr).op;
	else
	    return new SmvUnaryExpr("!",expr);
	}
	SmvExpr or(SmvExpr expr1, SmvExpr expr2) 
	{
	return new SmvBinaryExpr("|",expr1,expr2);
	}
	String ownerVar(String var) {
	return var + "_owner";
	}
	static int parseInt(String s) {
	try {
	    int result = Integer.parseInt(s);
	    return result;
	} catch (NumberFormatException e) {
	    throw new RuntimeException("Integer didn't parse: " + s);
	}
	}
	/** 
	 * Parse one line of the SMV output file, updating the trace.
	 */

	void parseLine(String line, BirTrace trace) {
	if (!doneParsing) {

	    // Look for "is true" or "is false" to see if verifier finished
	    if (line.indexOf("is true") >= 0) {
	      if (foundFalse) {
	        foundTrace = false;
	      } else {
		trace.setVerified(true);
		doneParsing = true;
		return;
	      }
	    }
	    if (line.indexOf("is false") >= 0) {
	      if (foundFalse) {
	        foundTrace = false;
	      } else {
		trace.setVerified(false);
		foundTrace = true;
		foundFalse = true;
		return;
	      }
	    }

	    if (foundTrace) {
		if (line.startsWith("State"))
		    stateCount += 1;

		// Look for update to thread loc variable
		if (stateCount > 1 && line.indexOf("_loc =") >= 0) 
		    parseThreadStep(line, trace);

		// Look for update to trap variable
		if (stateCount > 1 && line.startsWith("smv_trap =")) 
		    parseTrap(line, trace);
	    }
	}
	}
	/**
	 * Parse the output of SMV and interpret it as either a violation 
	 * (sequence of transformations) or as a error-free analysis.
	 * <p>
	 * @return a BirTrace 
	 */

	public BirTrace parseOutput() {
	if (! ranVerifier) 
	    throw new RuntimeException("Did not run verifier on input");
	try {
	    BirTrace trace = new BirTrace(system);
			File smvFile = new File(currentDir(),
				    system.getName() + ".smv");
	    if (! smvFile.exists())
		throw new RuntimeException("Cannot find SMV output: \n" 
					   + smvFile);
	    prepareParse();
	    BufferedReader reader = 
		new BufferedReader(new FileReader(smvFile));
	    String line = reader.readLine();
	    while (line != null) {
		parseLine(line,trace);
		line = reader.readLine();
	    }
	    reader.close();
	    trace.done();
	    return trace;
		} catch(IOException e) {
			throw new RuntimeException("Error parsing SMV output: \n" + e);
		} 
	}
	/**
	 * Parse line containing update to thread location.
	 * <p>
	 * Find transformation taken and add to trace.
	 * Line of form:
	 * <pre>
	 * threadName_loc = locXXX
	 * </pre>
	 * where XXX is a number.
	 */

	void parseThreadStep(String line, BirTrace trace) {
	int end = line.indexOf("_loc =");
	String threadName = line.substring(0,end);
	Integer threadNum = (Integer) threadTable.get(threadName);
	if (threadNum == null)   // wasn't a thread after all
	    return;

	// Fetch current and next location of this thread
	Location fromLoc = threadLocs[threadNum.intValue()];
	int locPos = 3 + line.indexOf("loc",end + 5);
	int locId = parseInt(line.substring(locPos));
	Location toLoc = system.getLocation(locId);
	
	// Use these locations to find the transformation
	TransVector outTrans = fromLoc.getOutTrans();
	for (int i = 0; i < outTrans.size(); i++) {
	    if (outTrans.elementAt(i).getToLoc() == toLoc) {
		threadLocs[threadNum.intValue()] = toLoc;
		lastTrans = outTrans.elementAt(i);
		trace.addTrans(lastTrans);
		return;
	    }
	}
	throw new RuntimeException("Error parsing SMV output: can't find trans for " + threadName + " from location " + fromLoc + " to " + toLoc);
	}
	/**
	 * Parse line updating trap variable---record trap name.
	 */

	void parseTrap(String line, BirTrace trace) {
	int end = line.indexOf("=");
	String trapName = line.substring(end + 2);
	trace.setTrap(trapName,lastTrans,0);
	}
	SmvExpr plus1(SmvExpr expr) 
	{
	return new SmvBinaryExpr("+",expr,ONE);
	}
	/** 
	 * Prepare to parse SMV output file
	 */

	void prepareParse() {
	doneParsing = false;
	foundFalse = false;
	foundTrace = false;
	stateCount = 0;
	ThreadVector threads = system.getThreads();
	threadLocs = new Location[threads.size()];
	threadTable = new Hashtable();
	for (int i = 0; i < threadLocs.length; i++) {
	    BirThread thread = threads.elementAt(i);
	    threadTable.put(thread.getName(), new Integer(i));
	    threadLocs[i] = thread.getStartLoc();
	}
	}
	/**
	 * Support for printing things nicely indented.
	 */

	void print(String s) {
	if (newLine) {           // at start of line---indent
	    for (int i = 0; i < indentLevel; i++)
		out.print("  ");
	    newLine = false;
	}
	out.print(s);
	}
	void println() {
	if (! newLine) {
	    out.println();
	    newLine = true;
	}
	}
	void println(String s) {
	print(s);
	println();
	}
	/**
	 * Print variable declarations
	 * <p>
	 * Aggregate variables (e.g., arrays, records, collections, refs)
	 * have a null type and are printed as comments.
	 */

	void printVariableDecls() {
	println("VAR");
	for (int i = 0; i < varList.size(); i++) {
	    SmvVar var = (SmvVar)varList.elementAt(i);
	    if (var.getType() != null) 
		println("  " + var + " : " + var.getType() + ";");
	    else {  // Is aggregate
		String comment = "";   // Print refIndex if array/rec/collect
		StateVar source = var.getSource();
		if (source != null 
		    && source.getType().isKind(COLLECTION|ARRAY|RECORD)) 
		    comment = "(refIndex = " + refIndex(source) + ")";
		println("  -- Aggregate: " + var + " " + comment);
	    }
	}
	println();
	}
	void property() {
	indent(2);
	// Catch any exceptions as violations
	println("INVARSPEC");
	println("smv_trap = None");

	indent(-2);
	if (ctlproperty) {
	    println("SPEC");
	    // Read in the file 
			File ctlFile = new File(currentDir(),
									system.getName() + ".smv.ctl");
			if (! ctlFile.exists())
				throw new RuntimeException("Cannot find CTL file: " +
										   ctlFile.getAbsolutePath());
			if (! ctlFile.canRead())
				throw new RuntimeException("Cannot read CTL file: " +
										   ctlFile.getAbsolutePath());
	    try {
	        BufferedReader in = 
		    new BufferedReader(new FileReader(ctlFile));
			   String line;
 	       indent(2);
			   while((line = in.readLine()) != null) 
				  println(line);
			   in.close();
	    } catch (IOException e) {
	    }
 	    indent(-2);
	} else {
	    // Deadlock is an invariant stating that
	    //   All threads are either blocked or terminated and
	    //   at least one thread has not terminated
	    indent(2);
	    println("INVARSPEC");
			ThreadVector threadVector = system.getThreads();
	    indent(2);
	    println("!(");
	    indent(2);
	    println("(");
	    indent(2);
			for (int i = 0; i < threadVector.size(); i++) {
				BirThread thread = threadVector.elementAt(i);
		if (i < threadVector.size()-1)
					println("(" + blockedVar(thread) + " | " + 
								  terminatedVar(thread) + ") &");
				else
					println("(" + blockedVar(thread) + " | " + 
								  terminatedVar(thread) + ")");
			}
	    indent(-2);
	    println(") & (");
	    indent(2);
			for (int i = 0; i < threadVector.size(); i++) {
				BirThread thread = threadVector.elementAt(i);
		if (i < threadVector.size()-1)
					println("!" + terminatedVar(thread) + " |");
				else
					println("!" + terminatedVar(thread));
			}
	    indent(-2);
	    println(")");
	    indent(-2);
	    println(")");
 	    indent(-2);
	}
	}
	/**
	 * Records updates to globals made by an assignment.
	 * <p>
	 * If the LHS may reference more than one variable
	 * (e.g., contains a pointer deref or array indexing)
	 * then the LHS expression will be an SmvCase whose
	 * leaves are all the possible updated variables.
	 * In this case, we recurse through all cases of
	 * the tree until we reach a leaf (SmvVar).
	 * When an SmvVar is finally reached, we add a conditional
	 * update to that variable to the value given by the RHS expression
	 * on that condition that the current trans is taken *and*
	 * the specific case context is true (i.e., all conditions
	 * for all cases we're contained in are true).
	 */

	void recordGlobalVarAssignments(SmvExpr lhs, 
				    SmvExpr rhs, SmvCase context) {
	if (lhs instanceof SmvVar) {
	    SmvVar global = (SmvVar) lhs;
	    SmvNaryExpr taken = transTakenExpr(currentTrans);
	    addConditionalContext(taken,context);
	    global.addUpdate(taken,becomes(global,rhs));
			//if (lhs.getType().isKind(RANGE)) {
	    //  Range range = (Range) lhs.getType();
	    //  SmvNaryExpr couldBeTaken = transCouldBeTakenExpr(currentTrans);
			//  SmvExpr low = greaterThan(rhs,
			//                            new SmvLit("" + range.getFromVal()));
			//  SmvExpr hi = lessThan(rhs,
			//                        new SmvLit("" + range.getToVal()));
	    //  couldBeTaken.add(not(and(hi,low)));
	    //  SmvLit trap = new SmvLit("RangeLimitException");
			//  trapVar.addUpdate(couldBeTaken, becomes(trapVar,trap));
	  //}
	} else if (lhs instanceof SmvCaseExpr) {
	    Vector cases = ((SmvCaseExpr)lhs).getCases();
	    for (int i = 0; i < cases.size(); i++) {
		SmvCase smvCase = (SmvCase)cases.elementAt(i);
		recordGlobalVarAssignments(smvCase.expr,rhs,smvCase);
	    }
	} else
	    throw new RuntimeException("Unknown LHS of assignment");
	}
	/**
	 * Records updates to global variables made by the current trans.
	 */

	void recordGlobalVarUpdates() {
	if (currentTrans.getActions().size() == 0)
	    return;
	Action action = currentTrans.getActions().elementAt(0);
	if (action.isAssignAction()) {
	    AssignAction assign = (AssignAction) action;
	    SmvExpr lhs = translate(assign.getLhs());
	    SmvExpr rhs = translate(assign.getRhs());
	    recordGlobalVarAssignments(lhs,rhs,null);
	} else if (action.isThreadAction(ANY)) {
	    recordThreadStatusUpdates((ThreadAction)action);
	} else if  (action.isLockAction(ANY)) {
	    recordLockStatusUpdates((LockAction)action);
	} 
	}
	/**
	 * Record updates to the lock variables by a lock action.
	 */

	void recordLockStatusUpdates(LockAction action) {
	ThreadVector threads = system.getThreads();
	BirThread currentThread = action.getThread();
	SmvLit currentThreadLit = new SmvLit(currentThread.getName());

	// We're assuming the lock is on the heap
	SmvCaseExpr lockExpr = (SmvCaseExpr) translate(action.getLockExpr());
	Vector cases = lockExpr.collectCases(new Vector());

	// For each leaf (which is a lock)
	for (int i = 0; i < cases.size(); i++) {

	    // Prepare the condition for the updates we'll make
	    SmvNaryExpr taken = transTakenExpr(currentTrans);
	    SmvCase smvCase = (SmvCase)cases.elementAt(i);
	    addConditionalContext(taken,smvCase);

	    // Grab all the relevant variables
	    SmvVar lockVar = (SmvVar) smvCase.expr;
	    SmvVar ownerVar = getVar(ownerVar(lockVar.getName()));
	    SmvVar countVar = getVar(countVar(lockVar.getName()));
	    SmvVar waitVar = getVar(waitVar(lockVar.getName(),currentThread));
	    SmvVar tempVar = getVar(tempVar(currentThread));
	    SmvExpr update;
	    SmvNaryExpr sum;
	    SmvNaryExpr nextSum;

	    switch (action.getOperation()) {
	    case LOCK:
		// Update the owner var to be the current thread 
		update = becomes(ownerVar,currentThreadLit);
		ownerVar.addUpdate(taken,update);
		// Update the count var to be 0 if lock currently free
		// (owner = NOTHREAD), else increment it 
		update = or(and(equal(ownerVar,NOTHREAD),
				becomes(countVar,ZERO)),
			    and(equal(ownerVar,currentThreadLit),
				becomes(countVar,plus1(countVar))));
		countVar.addUpdate(taken,update);
		break;

	    case UNLOCK:
		// Update the owner var to be NOTHREAD if the count is 0,
		// else leave it unchanged (should be currentThreadLit)
		update = or(and(equal(countVar,ZERO),
				becomes(ownerVar,NOTHREAD)),
			    and(not(equal(countVar,ZERO)),
				becomes(ownerVar,currentThreadLit)));
		ownerVar.addUpdate(taken,update);
		// If the count var is 0, leave the next value unconstrained
		// (var is dead), else decrement it
		update = or(equal(countVar,ZERO),
			    and(not(equal(countVar,ZERO)),
				becomes(countVar,minus1(countVar))));
		countVar.addUpdate(taken,update);
		break;

	    case WAIT:
		// Store the current lock count in the thread's temp var
		update = becomes(tempVar,countVar);
		tempVar.addUpdate(taken,update);
		// Release the lock by setting owner var to NOTHREAD
		update = becomes(ownerVar,NOTHREAD);
		ownerVar.addUpdate(taken,update);
		// Unconstrain the count var (now dead)
		countVar.addUpdate(taken,ONE); 
		// Set the wait var for this thread to 1
		update = becomes(waitVar,ONE);
		waitVar.addUpdate(taken,update);
		break;

	    case UNWAIT: 
		// Restore the count from the thread's temp var
		update = becomes(countVar,tempVar);
		countVar.addUpdate(taken,update);
		// Unconstrain the temp var (now dead)
		tempVar.addUpdate(taken,ONE); 
		// Set owner to currentThread
		update = becomes(ownerVar,currentThreadLit);
		ownerVar.addUpdate(taken,update);
		break;

	    case NOTIFY: 
		// Keep sums of wait vars for *other* threads on this lock 
		// (both current and next states).  
		sum = new SmvNaryExpr("+",ZERO,true);
		nextSum = new SmvNaryExpr("+",ZERO,true);
		for (int j = 0; j < threads.size(); j++) {
		    BirThread thread = threads.elementAt(j);
		    if (thread != currentThread) {
			SmvVar otherWaitVar = 
			    getVar(waitVar(lockVar.getName(),thread));
			sum.add(otherWaitVar);
			SmvExpr nextOtherWaitVar = 
			    new SmvNextExpr(otherWaitVar);
			nextSum.add(nextOtherWaitVar);
			// For each other thread, constrain the next value of
			// its wait var to be less than its current value
			// (i.e., can change from 1 to 0 or stay at 0)
			update = lessThan(nextOtherWaitVar,otherWaitVar);
			otherWaitVar.addUpdate(taken,update);
		    }
		}
		// For the thread executing notify (whose wait var must be 0)
		// keep its wait var at 0 *and* constrain the sums of
		// the other wait vars to satisfy one of the following
		// two conditions:
		//  (1) Sum of current wait vars is zero (i.e., no thread
		//     is waiting on this lock, so notify does nothing)
		//  (2) Sum of current wair vars is one greater than
		//     the sum of the next wait vars (i.e., exactly one
		//     wait var for some waiting thread changed from 1 to 0)
		update = and(becomes(waitVar,ZERO),
			     or(equal(sum,ZERO),
				equal(nextSum,minus1(sum))));
		waitVar.addUpdate(taken,update);
		break;

	    case NOTIFYALL: 
		// Set all wait vars for other threads to 0
		for (int j = 0; j < threads.size(); j++) {
		    BirThread thread = threads.elementAt(j);
		    if (thread != currentThread) {
			SmvVar otherWaitVar =
			    getVar(waitVar(lockVar.getName(),thread));
			update = becomes(otherWaitVar,ZERO);
			otherWaitVar.addUpdate(taken,update);
		    }
		}
		break;
	    }
	}
	}
	/**
	 * Records updates to the status of the threads.
	 * <p>
	 * A start action for a thread changes its T_active var to 1.
	 */

	void recordThreadStatusUpdates(ThreadAction action) {
	if (action.isThreadAction(START)) {
	    BirThread thread = action.getThreadArg();
	    SmvNaryExpr taken = transTakenExpr(currentTrans);
	    SmvVar active = getVar(activeVar(thread));
	    active.addUpdate(taken,becomes(active,ONE));
	}
	}
	/**
	 * Return the refIndex of a BIR state variable (i.e., its position
	 * in the universal reference type refAny).
	 */

	int refIndex(StateVar target) {
	return 1 + system.refAnyType().getTargets().indexOf(target);
	}
	String refIndexVar(String var) {
	return var + "_refIndex";
	}
	/**
	 * Build SMV reference value from refIndex and instNum values.
	 */

	SmvLit refValue(int refIndex, int instNum) {
	return new SmvLit("(" + refIndex + " * MAX_COLLECT_SIZE + " 
			  + instNum + ")");
	}
	/**
	 * Run SMV translator, generate each part of the output in turn.
	 */

	SmvTrans run() {
	header();
	stateVariables();
	init();
	transitions();
	if (liveness)
	    fairness();
	definitions();
	property();
	return this;
	}
	/**
	 * Run SMV.
	 * <p>
	 * The translator must have been executed for the transition system
	 * (the file NAME.trans must exist and contain the translated input).
	 * @return the output of SMV (as a String)
	 */

	public String runSmv() {
	try {
			File sourceFile = new File(currentDir(),
				       system.getName() + ".trans");
	    if (! sourceFile.exists())
		throw new RuntimeException("Cannot find SMV input file: " +
					   sourceFile.getName());
	    ranVerifier = true;
	    String output = execAndWait("nusmv " + options.runOptions() + sourceFile.getName(), true);
	    // Write output to file
	    File outFile = new File(currentDir(), 
				    system.getName() + ".smv");
			FileOutputStream streamOut = new FileOutputStream(outFile);
			PrintWriter writerOut = new PrintWriter(streamOut);
	    writerOut.print(output);
	    writerOut.close();
System.out.println("Parsing output and printing BIR trace");
parseOutput().print(true);
	    return output;
		} catch(Exception e) {
			throw new RuntimeException("Could not produce SMV file (" + e + ")");
		}	
	}
	/**
	 * Generate SMV variables
	 */

	void stateVariables() {
	declareVariables();
	printVariableDecls();
	}
	String tempVar(BirThread thread) {
	return thread.getName() + "_temp";
	}
	String terminatedVar(BirThread thread) {
	return thread.getName() + "_terminated";
	}
	/**
	 * Build an expression that is true if the thread is blocked.
	 * <p>
	 * The thread is blocked if it is in a location and the guards
	 * for all transforms out of that location are false.  If
	 * there are no transforms out of the location, the thread
	 * is considered terminated, not blocked.
	 */

	SmvExpr threadBlockedExpr(BirThread thread) {
	LocVector locations = thread.getLocations();
	SmvVar locVar = getVar(locVar(thread));
	SmvNaryExpr blocked = new SmvNaryExpr("|",ZERO,true);
	for (int i = 0; i < locations.size(); i++) {
	    SmvLit locLabel = new SmvLit(locName(locations.elementAt(i)));

	    // Build expression for being blocked at this location
	    SmvNaryExpr blockedAtLoc = new SmvNaryExpr("&",ONE,true);
	    TransVector transitions = locations.elementAt(i).getOutTrans();
	    for (int j = 0; j < transitions.size(); j++) {
		SmvExpr guard = 
		    (SmvExpr) guardTable.get(transitions.elementAt(j));
		if (guard != null && ! guard.equals(ONE)) {
		    // Nontrivial guard: can block if it is false
		    blockedAtLoc.add(not(guard));
		} else {
		    // True or missing guard: cannot block in this location
		    blockedAtLoc = null;
		    break;
		}
	    }
	    // If we didn't find any transforms with true/missing guards
	    // and we found at least one transform, then we can block here
	    if (blockedAtLoc != null && blockedAtLoc.size() > 0)
		blocked.add(and(equal(locVar,locLabel),blockedAtLoc));
	}
	return blocked;
	}
	/**
	 * Build expression that is true if a thread is terminated.
	 * <p>
	 * A thread is terminated if it is in a location with no
	 * out transformations.
	 */

	SmvExpr threadTerminatedExpr(BirThread thread) {
	LocVector locations = thread.getLocations();
	SmvVar locVar = getVar(locVar(thread));
	SmvNaryExpr terminated = new SmvNaryExpr("|",ZERO,true);
	for (int i = 0; i < locations.size(); i++) {
	    SmvLit locLabel = new SmvLit(locName(locations.elementAt(i)));
	    if (locations.elementAt(i).getOutTrans().size() == 0) 
		terminated.add(equal(locVar,locLabel));
	}
	return terminated;
	}
	/**
	 * Generate an expression that is true if a transformation could be taken.
	 * <p>
	 * Similar to transTakenExpr, except we do not require the location
	 * be updated or the thread be running.
	 */

	SmvNaryExpr transCouldBeTakenExpr(Transformation trans) {
	SmvVar locVar = getVar(locVar(trans.getFromLoc().getThread()));
	SmvLit fromLoc = new SmvLit(locName(trans.getFromLoc()));
	SmvNaryExpr taken = new SmvNaryExpr("&",ONE,true);
	taken.add(equal(locVar,fromLoc));
	if (currentGuard != null && ! currentGuard.equals(ONE))
	    taken.add(currentGuard);
	BirThread thread = trans.getFromLoc().getThread();
	if (! thread.isMain() 
	    && trans.getFromLoc().equals(thread.getStartLoc())) {
	    SmvVar active = getVar(activeVar(trans.getFromLoc().getThread()));
	    taken.add(equal(active,ONE));
	}
	return taken;
	}
	/**
	 * Generate a TRANS formula for each thread and global variable.
	 */

	void transitions() {
	// Generate TRANS for each thread
	ThreadVector threadVector = system.getThreads();
	for (int i = 0; i < threadVector.size(); i++) {
	    BirThread thread = threadVector.elementAt(i);
	    SmvExpr expr = translateThread(thread);
	    println("TRANS  -- " + thread.getName());
	    expr.print(this);
	    println();
	}

	// Generate TRANS for each constrained global
	for (int i = 0; i < varList.size(); i++) {
	    SmvVar var = (SmvVar) varList.elementAt(i);
	    if (var.isConstrained()) {
		SmvExpr expr = translateGlobal(var);
		println("TRANS  -- " + var);
		expr.print(this);		
		println();
	    }
	}
	println();
	}
	/**
	 * Translate a BIR expression to an SmvExpr
	 */

	SmvExpr translate(Expr expr) {
	expr.apply(this);
	return (SmvExpr) getResult();
	}
	/**
	 * Generate SMV source representing a transition system.
	 * <p>
	 * The SMV input is written to a file NAME.trans where NAME
	 * is the name of the transition system.  A set of
	 * options can be provided in a SmvOptions object.
	 * @param system the transition system
	 * @param options the SMV verifier options
	 * @return the SmvTrans control object
	 */

	public static SmvTrans translate(TransSystem system, 
				     SmvOptions options) {
	try {
			File sourceFile = new File(currentDir(),
				       system.getName() + ".trans");

			FileOutputStream streamOut = new FileOutputStream(sourceFile);
			PrintWriter writerOut = new PrintWriter(streamOut);
	    SmvTrans result = translate(system, options, writerOut);
			writerOut.close();
	    return result;
		} catch(IOException e) {
			throw new RuntimeException("Could not produce SMV file: " + e);
		}
	}
	/**
	 * Generate SMV source representing a transition system.
	 * <p>
	 * As above, but the SMV is written to the PrintWriter provided.
	 * @param system the transition system
	 * @param out the PrintWriter to write the SMV to
	 * @return the SmvTrans control object
	 */

	public static SmvTrans translate(TransSystem system, SmvOptions options,
				     PrintWriter out) {
	return (new SmvTrans(system,options,out)).run();
	}
	void translateBinaryOp(Expr e1, Expr e2, String op) {
	setResult(new SmvBinaryExpr(op,translate(e1),translate(e2)));
	}
	/**
	 * Return update expression for global variable (most work done in SmvVar).
	 */

	SmvExpr translateGlobal(SmvVar var) {
	SmvExpr updateExpr = var.getUpdateExpr();
	return updateExpr;
	}
	/**
	 * Build the TRANS expression for a thread.
	 */

	SmvExpr translateThread(BirThread thread) {
	StateVarVector locals = system.getLocalStateVars(thread);
	SmvNaryExpr threadExpr = new SmvNaryExpr("|",ZERO,true);
	SmvNaryExpr skipExpr = new SmvNaryExpr("&",ONE,true);
	SmvVar locVar = getVar(locVar(thread));

	// Build skip expression (thread does not take step)
	skipExpr.add(becomes(locVar,locVar));
	for (int i = 0; i < locals.size(); i++) {
	    SmvVar var = getVar(locals.elementAt(i).getName());
	    skipExpr.add(becomes(var,var));
	}
	if (liveness) {  // For liveness properties, set T_idle
	    SmvVar idleVar = getVar(idleVar(thread));
	    skipExpr.add(becomes(idleVar,ONE));
	}
	// idle is one possible action for the thread
	threadExpr.add(skipExpr);

	// For each transformation in the thread, generate a disjunct
	LocVector locs = thread.getLocations();
	for (int i = 0; i < locs.size(); i++) {
	    TransVector transVector = locs.elementAt(i).getOutTrans();
	    for (int j = 0; j < transVector.size(); j++) {
		SmvExpr transExpr = buildTransExpr(transVector.elementAt(j),
						   locals, locVar);
		threadExpr.add(transExpr);
	    }
	}

	return threadExpr;
	}
	void translateUnaryOp(Expr e, String op) {
	setResult(new SmvUnaryExpr(op,translate(e)));
	}
	/**
	 * Build an expression that is true if a given transformation is taken.
	 */

	SmvNaryExpr transTakenExpr(Transformation trans) {
	SmvVar locVar = getVar(locVar(trans.getFromLoc().getThread()));
	SmvLit fromLoc = new SmvLit(locName(trans.getFromLoc()));
	SmvLit toLoc = new SmvLit(locName(trans.getToLoc()));
	SmvNaryExpr taken = new SmvNaryExpr("&",ONE,true);
	// Location goes from fromLoc to toLoc
	taken.add(equal(locVar,fromLoc));
	taken.add(becomes(locVar,toLoc));
	// If interleaving model, this trans in the thread that's running
	if (interleaving) {
	    BirThread thread = trans.getFromLoc().getThread();
	    taken.add(equal(runningVar,new SmvLit(thread.getName())));
	}
	BirThread thread = trans.getFromLoc().getThread();
	// If not a main thread, the thread has been started
	if (! thread.isMain() 
	    && trans.getFromLoc().equals(thread.getStartLoc())) {
	    SmvVar active = getVar(activeVar(trans.getFromLoc().getThread()));
	    taken.add(equal(active,ONE));
	}
	// If the guard is present and nontrivial, it must be true
	if (currentGuard != null && ! currentGuard.equals(ONE))
	    taken.add(currentGuard);
	return taken;
	}
	String waitVar(String var, BirThread thread) {
	return var + "_wait_" + thread.getName();
	}
}
