package edu.ksu.cis.bandera.dspin;

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
import java.util.Properties;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.List;
import java.util.LinkedList;

import edu.ksu.cis.bandera.bir.*;

import edu.ksu.cis.bandera.spin.ExprNode;
import edu.ksu.cis.bandera.spin.CaseNode;
import edu.ksu.cis.bandera.spin.TreeNode;
import edu.ksu.cis.bandera.spin.TrapNode;
import edu.ksu.cis.bandera.spin.Case;

import edu.ksu.cis.bandera.checker.dspin.DSpinOptions;
import edu.ksu.cis.bandera.checker.OptionsFactory;
import edu.ksu.cis.bandera.checker.Checker;

import edu.ksu.cis.bandera.util.Preferences;

import org.apache.log4j.Category;

/**
 * DSpinTrans is the main class of the dSPIN translator, which generates
 * PROMELA source representing a transition system.
 * <p>
 * The dSPIN translator is invoked on a transition system as follows:
 * <pre>
 * // Parameters 
 * TransSystem system = ...;   // the transition system
 * DSpinOptions options = new DSpinOptions();   // SPIN options
 * options.setMemoryLimit(256);            // set various options
 * ...
 * DSpinTrans spin = DSpinTrans.translate(system,options); // invoke translator
 * spin.runSpin();                           // Run SPIN on generated PROMELA
 * BirTrace trace = spin.parseOutput();      // Parse output as BIR trace
 * </pre>
 */

public class DSpinTrans extends AbstractExprSwitch implements BirConstants, Checker
{
	private static Category log = Category.getInstance(DSpinTrans.class);
	
	TransSystem system;
	PrintWriter out;
	DSpinTypeName namer;
	int indentLevel = 0;
	boolean inDefine = false;
	boolean newLine = true;
	boolean resetTemp = false;
	boolean resetNew = false;
	Transformation currentTrans;
	int actionNum;
	DSpinOptions options;
	boolean ranVerifier = false;

	static String includeDir = "";
	static Runtime runtime = Runtime.getRuntime();
	public static final String normal = "NORMAL_CASE";
	
	static final int BUFSIZE = 50000;
	static byte [] buffer = new byte[BUFSIZE];

	static {
		/*
		includeDir = Bandera.getProperty("dspin.include");
		*/
	}
	DSpinTrans(TransSystem system, DSpinOptions options, PrintWriter out) {
	this.system = system;
	this.out = out;
	this.namer = new DSpinTypeName();
	if(options == null) {
	    this.options = (DSpinOptions)OptionsFactory.createOptionsInstance("DSpin");
	    this.options.init();
	}
	else {
	    this.options = options;
	}
	}
	String actionId() {
	int locNum = currentTrans.getFromLoc().getId();
	int transNum = 
	    currentTrans.getFromLoc().getOutTrans().indexOf(currentTrans);
	return locNum + " " + transNum + " " + actionNum;
	}
	String actionIdWithCommas() {
	int locNum = currentTrans.getFromLoc().getId();
	int transNum = 
	    currentTrans.getFromLoc().getOutTrans().indexOf(currentTrans);
	return locNum + "," + transNum + "," + actionNum;
	}
	CaseNode applyTo(Expr expr) {
	expr.apply(this);
	return (CaseNode) getResult();
	}
	public void caseAddExpr(AddExpr expr)
	{
	translateBinaryOp(expr.getOp1(), expr.getOp2(), " + ");
	}
	public void caseAndExpr(AndExpr expr)
	{
	translateBinaryOp(expr.getOp1(), expr.getOp2(), " && ");
	}
	public void caseArrayExpr(ArrayExpr expr)
	{
	String ref = "";  // Need extra deref for arrays of Refs
	if (expr.getType().isKind(REF))
	    ref = ".ref";
	TreeNode arrayTree = applyTo(expr.getArray());
	TreeNode indexTree = applyTo(expr.getIndex());
	TreeNode result = arrayTree.compose(indexTree,null);
	Vector leafCases = result.getLeafCases(new Vector());
	for (int i = 0; i < leafCases.size(); i++) {
	    Case leafCase = (Case) leafCases.elementAt(i);
	    ExprNode leaf = (ExprNode) leafCase.node;
	    leafCase.insertTrap(leaf.expr2 + " >= " + leaf.expr1 + ".length",
				"ArrayIndexOutOfBoundsException",true);
	    leafCase.insertTrap(leaf.expr2 + " < 0",
				"ArrayIndexOutOfBoundsException",true);
	    leaf.update(leaf.expr1 + ".elements[" + leaf.expr2 + "]" + ref);
	}
	setResult(result);
	}
	public void caseAssertAction(AssertAction assertAction) {
	CaseNode condTree = (CaseNode) applyTo(assertAction.getCondition());
	Vector leaves = condTree.getLeaves(new Vector());
	for (int i = 0; i < leaves.size(); i++) {
	    ExprNode leaf = (ExprNode) leaves.elementAt(i);
	    leaf.update("assert(" + leaf.expr1 + ");");
	}
	printStatement(condTree.getCase(normal));
	}
	public void caseAssignAction(AssignAction assign) 
	{
	TreeNode rhsTree = applyTo(assign.getRhs());
	TreeNode lhsTree = applyTo(assign.getLhs());
	CaseNode result = (CaseNode) lhsTree.compose(rhsTree,null);
	Vector leafCases = result.getLeafCases(new Vector());
	for (int i = 0; i < leafCases.size(); i++) {
	    Case leafCase = (Case) leafCases.elementAt(i);
	    ExprNode leaf = (ExprNode) leafCase.node;
	    Type rhsType = assign.getRhs().getType();
	    Type lhsType = assign.getLhs().getType();
	    // Make run-time checks on assignment
	    if (! rhsType.isSubtypeOf(lhsType)) {

		// Check subrange
		if (lhsType.isKind(RANGE)
		    && ! lhsType.containsValue(assign.getRhs())) {
		    Range range = (Range) lhsType;
		    leafCase.insertTrap(leaf.expr2 + " < " 
					+ range.getFromVal(),
					"RangeLimitException", false);
		    leafCase.insertTrap(leaf.expr2 + " > " 
					+ range.getToVal(),
					"RangeLimitException", false);
		}

		// Check ref assignment
		if (lhsType.isKind(REF)
		    && ! (assign.getRhs() instanceof NullExpr)) {
		    String trapDesc = "ClassCastException";
		    StateVarVector lhsTargets = ((Ref)lhsType).getTargets();
		    StateVarVector rhsTargets = ((Ref)rhsType).getTargets();
		    for (int j = 0; j < rhsTargets.size(); j++) 
			if (! lhsTargets.contains(rhsTargets.elementAt(j))) {
			    String check = leaf.expr2 + ".refIndex == " +
				refIndex(rhsTargets.elementAt(j));
			    leafCase.insertTrap(check,
						"ClassCastException",true);
			}
		}
	    }
	    leaf.update(leaf.expr1 + " = " + leaf.expr2 + ";");
	}	    
	printStatement(result.getCase(normal));
	}
	public void caseBoolLit(BoolLit expr)
	{
	setResult(specialize("" + expr.getValue()));
	}
	public void caseChooseExpr(ChooseExpr expr)
	{
	Vector choices = expr.getChoices();
	println("if");
	for (int i = 0; i < choices.size(); i++) {
	    Expr choice = (Expr) choices.elementAt(i);
	    ExprNode leaf = (ExprNode) applyTo(choice).getCase(normal);
	    println(":: _temp_ = " + leaf.expr1 + "; printf(\"BIR? " +
		    actionNum + " " + i + "\\n\"); ");
	}
	println("fi;");
	resetTemp = true;
	setResult(specialize("_temp_"));        
	}
	public void caseConstant(Constant expr)
	{
	setResult(specialize(expr.getName()));
	}
	public void caseDerefExpr(DerefExpr expr)
	{
	TreeNode result = applyTo(expr.getTarget());
	StateVarVector targets = 
	    ((Ref)expr.getTarget().getType()).getTargets();
	Vector leafCases = result.getLeafCases(new Vector());
	for (int i = 0; i < leafCases.size(); i++) {
	    Case leafCase = (Case) leafCases.elementAt(i);
	    ExprNode leaf = (ExprNode) leafCase.node;
	    CaseNode caseNode = new CaseNode();
	    String cond = "(" + leaf.expr1 + " != null)";
	    caseNode.addCase(cond, leaf.expr1);
	    cond = "(" + leaf.expr1 + " == null)";
	    caseNode.addTrapCase(cond,"NullPointerException", true);
	    leafCase.replace(caseNode);
	}
	setResult(result);
	}
	public void caseDivExpr(DivExpr expr)
	{	
	TreeNode e1Tree = applyTo(expr.getOp1());
	TreeNode e2Tree = applyTo(expr.getOp2());
	TreeNode result = e1Tree.compose(e2Tree,null);
	Vector leafCases = result.getLeafCases(new Vector());
	for (int i = 0; i < leafCases.size(); i++) {
	    Case leafCase = (Case) leafCases.elementAt(i);
	    ExprNode leaf = (ExprNode) leafCase.node;
	    leafCase.insertTrap(leaf.expr2 + " == 0",
				"ArithmeticException",true);
	    leaf.update("(" + leaf.expr1 + " / " + leaf.expr2 + ")");
	}
	setResult(result);
	}
	public void caseEqExpr(EqExpr expr)
	{
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " == ");
	}
	public void caseInstanceOfExpr(InstanceOfExpr expr)
	{
	TreeNode result = applyTo(expr.getRefExpr());
	// commented out to match SpinTrans.java and to get it to compile. -tcw
	//StateVarVector targets = expr.getRefType().getTargets();
	Vector leafCases = result.getLeafCases(new Vector());
	for (int i = 0; i < leafCases.size(); i++) {
	    Case leafCase = (Case) leafCases.elementAt(i);
	    ExprNode leaf = (ExprNode) leafCase.node;
	    CaseNode caseNode = new CaseNode();

	    /* commented out to match SpinTrans.java and to get it to compile. -tcw
	    String elseCond = "! (";
	    for (int j = 0; j < targets.size(); j++) {
		StateVar target = targets.elementAt(j);
		String cond = "(" + leaf.expr1 + ".refIndex == " 
		    + refIndex(target) + ")";
		caseNode.addCase(cond,"1");
		if (j > 0) elseCond += " || ";
		elseCond += cond;
	    }
	    elseCond += ")";
	    caseNode.addCase(elseCond,"0");
	    */

	    leafCase.replace(caseNode);
	}
	setResult(result);
	}
	public void caseIntLit(IntLit expr)
	{
	setResult(specialize("" + expr.getValue()));
	}
	public void caseLeExpr(LeExpr expr)
	{
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " <= ");
	}
	public void caseLengthExpr(LengthExpr expr)
	{
	TreeNode result = applyTo(expr.getArray());
	Vector leaves = result.getLeaves(new Vector());
	for (int i = 0; i < leaves.size(); i++) {
	    ExprNode leaf = (ExprNode) leaves.elementAt(i);
	    leaf.update(leaf.expr1 + ".length");
	}
	setResult(result);
	}
	public void caseLockAction(LockAction lockAction) 
	{
	CaseNode lockTree = (CaseNode) applyTo(lockAction.getLockExpr());
	String opName = "_" +  
	    LockAction.operationName(lockAction.getOperation());
	if (lockAction.isWait())
	    opName += "_" + lockAction.getThread().getId();
	Lock lockType = (Lock) lockAction.getLockExpr().getType();
	if (lockType.isReentrant() 
	    && ! lockAction.isLockAction(NOTIFY|NOTIFYALL))
	    opName += "_R";
	Vector leaves = lockTree.getLeaves(new Vector());
	for (int i = 0; i < leaves.size(); i++) {
	    ExprNode leaf = (ExprNode) leaves.elementAt(i);
	    leaf.update(opName + "(" + leaf.expr1 + "," +
			actionIdWithCommas() + ");");
	}
	printStatement(lockTree.getCase(normal));
	}
	public void caseLockTest(LockTest lockTest) {
	TreeNode lockTree;
	Vector leaves;
	switch (lockTest.getOperation()) {
	case LOCK_AVAILABLE:
	    String opName = "_lockAvailable";
	    if (((Lock)lockTest.getLockExpr().getType()).isReentrant())
		opName += "_R";
	    lockTree = applyTo(lockTest.getLockExpr());
	    leaves = lockTree.getLeaves(new Vector());
	    for (int i = 0; i < leaves.size(); i++) {
		ExprNode leaf = (ExprNode) leaves.elementAt(i);
		leaf.update(opName + "(" + leaf.expr1 + ")");
	    }
	    setResult(lockTree);
	    return;
	case HAS_LOCK:
	    setResult(specialize("true"));
	    return;
	case WAS_NOTIFIED:
	    lockTree = applyTo(lockTest.getLockExpr());
	    leaves = lockTree.getLeaves(new Vector());
	    for (int i = 0; i < leaves.size(); i++) {
		ExprNode leaf = (ExprNode) leaves.elementAt(i);
		leaf.update("_wasNotified_" + lockTest.getThread().getId() 
			    + "(" + leaf.expr1 + ")");
	    }
	    setResult(lockTree);
	    return;	    
	}	
	}
	public void caseLtExpr(LtExpr expr)
	{
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " < ");
	}
	public void caseMulExpr(MulExpr expr)
	{
	translateBinaryOp(expr.getOp1(), expr.getOp2(), " * ");
	}
	public void caseNeExpr(NeExpr expr)
	{
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " != ");
	}
	public void caseNewArrayExpr(NewArrayExpr expr)
	{
	Collection colType =  (Collection)expr.getCollection().getType();
	Array arrayType = (Array)colType.getBaseType();
	String arrayTypeName = dSpinTypeName(arrayType,null);
	Type baseType = arrayType.getBaseType();
	String baseTypeName = dSpinTypeName(baseType,null);
	if (baseType.isKind(REF)) 
	    baseTypeName = baseTypeName.substring(0,baseTypeName.length() - 4)
		+ "_aref";
	int refIndex = refIndex(expr.getCollection());
	CaseNode setLength = applyTo(expr.getLength());
	Vector leafCases = setLength.getLeafCases(new Vector());
	for (int i = 0; i < leafCases.size(); i++) {
	    Case leafCase = (Case) leafCases.elementAt(i);
	    ExprNode leaf = (ExprNode) leafCase.node;
	    leaf.update("_temp_ = " + leaf.expr1 + ";");
	}
	printStatement(setLength.getCase(normal));
	resetTemp = true;
	println("_new_ = new " + arrayTypeName + ";");
	println("_new_.refIndex = " + refIndex + ";");
	println("_new_.length = _temp_;");
	println("_new_.elements = new " + baseTypeName + "[_temp_];");
	println("printf(\"BIR? " + actionNum + " %d\\n\",_temp_); ");
	resetNew = true;
	setResult(specialize("_new_"));
	}
	public void caseNewExpr(NewExpr expr)
	{
	Type type = ((Collection)expr.getCollection().getType()).getBaseType();
	String typeName = dSpinTypeName(type,null);
	println("_new_ = new " + typeName + ";");
	println("_new_.refIndex = " + refIndex(expr.getCollection()) + ";");
	resetNew = true;
	setResult(specialize("_new_"));
	}
	public void caseNotExpr(NotExpr expr)
	{
	translateUnaryOp(expr.getOp(),"! ");
	}
	public void caseNullExpr(NullExpr expr)
	{
		setResult(specialize("null"));
	}
	public void caseOrExpr(OrExpr expr)
	{
	translateBinaryOp(expr.getOp1(), expr.getOp2(), " || ");
	}
	public void casePrintAction(PrintAction printAction) {
	// Perhaps remove this---get output from BIR trace instead
	if (printAction.getPrintItems().size() > 0) {
	    String format = "BIR|";
	    String params = "";
	    Vector printItems = printAction.getPrintItems();
	    for (int i = 0; i < printItems.size(); i++) {
		Object item = printItems.elementAt(i);
		if (item instanceof String)
		    format += item;
		else {
		    format += "%d";
		    CaseNode varTree = (CaseNode) applyTo((Expr)item);
		    params += "," + ((ExprNode)varTree.getCase(normal)).expr1;
		}
	    }
	    println("printf(\"" + format + "\\n\"" + params + "); ");
	}
	}
	public void caseRecordExpr(RecordExpr expr)
	{
	TreeNode result = applyTo(expr.getRecord());
	String field = expr.getField().getName();
	Vector leaves = result.getLeaves(new Vector());
	for (int i = 0; i < leaves.size(); i++) {
	    ExprNode leaf = (ExprNode) leaves.elementAt(i);
	    leaf.update(leaf.expr1 + "." + field);
	}
	setResult(result);
	}
	public void caseRefExpr(RefExpr expr)
	{
	setResult(specialize("& " + expr.getTarget().getName()));
	}
	public void caseRemExpr(RemExpr expr)
	{	
	TreeNode e1Tree = applyTo(expr.getOp1());
	TreeNode e2Tree = applyTo(expr.getOp2());
	TreeNode result = e1Tree.compose(e2Tree,null);
	Vector leafCases = result.getLeafCases(new Vector());
	for (int i = 0; i < leafCases.size(); i++) {
	    Case leafCase = (Case) leafCases.elementAt(i);
	    ExprNode leaf = (ExprNode) leafCase.node;
	    leafCase.insertTrap(leaf.expr2 + " == 0",
				"ArithmeticException",true);
	    leaf.update("(" + leaf.expr1 + " % " + leaf.expr2 + ")");
	}
	setResult(result);
	}
	public void caseStateVar(StateVar expr)
	{
	setResult(specialize(expr.getName()));
	}
	public void caseSubExpr(SubExpr expr)
	{
	translateBinaryOp(expr.getOp1(), expr.getOp2(), " - ");
	}
	public void caseThreadAction(ThreadAction threadAction) 
	{
	String opName = 
	    ThreadAction.operationName(threadAction.getOperation());
	int threadId = threadAction.getThreadArg().getId();
	println("_" + opName + "Thread_" + threadId + ";");
	}
	public void caseThreadLocTest(ThreadLocTest threadLocTest) {
	String threadName = threadLocTest.getThread().getName();
	int threadId = 1 + threadLocTest.getThread().getId();
	String locLabel = locLabel(threadLocTest.getLocation());
	String test = threadName + "[" + threadId + "]@" + locLabel;
	setResult(specialize(test));
	}
	public void caseThreadTest(ThreadTest threadTest) {
	switch (threadTest.getOperation()) {
	case THREAD_TERMINATED:
	    /* Commented out to match SpinTrans.java and to get this to compile. This might
	     * be completely wrong! -tcw
	    String opName = 
		ThreadTest.operationName(threadTest.getOperation());
	    int threadId = threadTest.getThreadArg().getId();
	    String test = "_" + opName + "_" + threadId;
	    */
	    /* Added to match SpinTrans.java and to get this to compile.  This might
	     * be completely wrong! -tcw */
	    CaseNode lhs = applyTo(threadTest.getLhs());
	    ExprNode leaf = (ExprNode) lhs.getCase(normal);
	    String test = "_threadActive[" + leaf.expr1 + "] == THREAD_EXITED ";
	    setResult(specialize(test));
	    return;
	}	
	}
	public String check() { return runSpin(); }
	static String currentDir() { return System.getProperty("user.dir"); }
	public void defaultCase(Object obj)
	{
	throw new RuntimeException("Trans type not handled: " + obj);
	}
	void definitions() {
	Vector definitions = system.getDefinitions();
	for (int i = 0; i < definitions.size(); i++) {
	    Definition def = (Definition) definitions.elementAt(i);
	    if (def instanceof Constant) 
		println("#define " + def.getName() + " " + def.getDef());
	}
	Vector types = system.getTypes();
	for (int i = 0; i < types.size(); i++) {
	    Type type = (Type) types.elementAt(i);
	    if (type.isKind(BOOL|RANGE|LOCK|ENUMERATED|REF)) {
		println("/* " + type.typeName() + " = " + type + " */");
		print("#define " + type.typeName() + " ");
		println(dSpinTypeName(type,this));
		if (type.isKind(ENUMERATED)) {
		    Enumerated enumVar = (Enumerated) type;
		    for (int j = 0; j < enumVar.getEnumeratedSize(); j++) {
			String name = enumVar.getNameOf(enumVar.getFirstElement()+j);
			if (name != null)
			    println("#define " + name + " " + 
				    (enumVar.getFirstElement()+j));
		    }
		}
		println();
	    }
	    if (type.isKind(RECORD|ARRAY)) {
		println("typedef " + type.typeName() + ";");
		println();
	    }
	}
	for (int i = 0; i < types.size(); i++) {
	    Type type = (Type) types.elementAt(i);
	    if (type.isKind(REF)) {
		String refRoot = 
		    type.typeName().substring(0,type.typeName().length() - 4);
		println("typedef " + refRoot + "_aref { " +
			type.typeName() + " ref; }");
		println();
	    }
	}
	for (int i = 0; i < types.size(); i++) {
	    Type type = (Type) types.elementAt(i);
	    if (! type.isKind(BOOL|RANGE|LOCK|ENUMERATED|REF|COLLECTION)) {
		println("/* " + type.typeName() + " = " + type + " */");
		print("typedef " + type.typeName() + " ");
		println(dSpinTypeName(type,this));
		println();
	    }
	}
	println();
	}
	String dSpinTypeName(Type type, Object o) {
	type.apply(namer,o);
	return (String) namer.getResult();
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
 * getCounterExample method comment.
 */
public List getCounterExample() {
	return null;
}
	public DSpinOptions getDSpinOptions() { return options; }
	String getSimpleResult() {
	TreeNode result = ((CaseNode)getResult()).getCase(normal);
	if (result instanceof ExprNode) 
	    return ((ExprNode)result).expr1;
	else
	    throw new RuntimeException("Result not simple: " + result);
	}
	boolean guardPresent(Transformation trans) {
	Expr guard = trans.getGuard();
	if (guard == null)
	    return false;
	if (guard instanceof BoolLit)
	    return ! ((BoolLit)guard).getValue();
	if ((guard instanceof LockTest) 
	    && ((LockTest)guard).getOperation() == HAS_LOCK)
	    return false;
	return true;
	}
	void header() {
	ThreadVector threadVector = system.getThreads();

	println("/*  (dSPIN) Promela for transition system: " 
		    + system.getName() + " */");
	println();

	// Lock types (R = reentrant, W = waiting)
	println("/* Lock operations */");
	println("typedef lock_ {\n  byte owner;\n};");
	println("typedef lock_W {\n  byte owner;\n  int waiting;\n};");
	println("typedef lock_R {\n  byte count;\n  byte owner;\n};");
	println("typedef lock_RW {\n  byte count;\n  byte owner;\n  int waiting;\n};");

		// Monitor operations - most have separate reentrant version (_R)
	println("#define _lock(sync,locNum,transNum,actionNum) \\");
	println("  sync.owner = _pid ");
	println("#define _lock_R(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
		println("  :: sync.owner == _pid -> sync.count++; \\");
	println("  :: else -> sync.owner = _pid; \\");
	println("  fi ");

	println("#define _unlock(sync,locNum,transNum,actionNum) \\");
	println("  sync.owner = 0 ");
	println("#define _unlock_R(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
		println("  :: sync.count > 0 -> sync.count--; \\");
	println("  :: else -> sync.owner = 0; \\");
	println("  fi ");

	println("#define _lockAvailable(sync) \\");
	println("  (sync.owner == 0)");
	println("#define _lockAvailable_R(sync) \\");
	println("  (sync.owner == 0 || sync.owner == _pid) ");

	println("#define _unwait(sync,locNum,transNum,actionNum) \\");
	println("  sync.owner = _pid ");
	println("#define _unwait_R(sync,locNum,transNum,actionNum) \\");
	println("  sync.owner = _pid; sync.count = _temp_; _temp_ = 0 ");

	println("#define _notifyAll(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
		println("  :: sync.owner == _pid -> sync.waiting = 0; \\");
	println("  :: else -> printf(\"BIR: locNum transNum actionNum IllegalMonitorStateException\"); assert(0); \\");
	println("  fi ");

	println("#define _notify(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
	for (int i = 0; i < threadVector.size(); i++) {
	    int thread = (1 << i);
	    println("  :: (sync.owner == _pid) && (sync.waiting & " + thread + ") -> sync.waiting = sync.waiting & " + ~ thread + " ; printf(\"BIR? %d " + i + "\",actionNum); \\");
	}
	println("  :: (sync.owner == _pid) && (sync.waiting == 0) -> printf(\"BIR? %d " + threadVector.size() + "\\n\",actionNum); \\");
	println("  :: else -> printf(\"BIR: locNum transNum actionNum IllegalMonitorStateException\"); assert(0); \\");
	println("  fi  ");

	for (int i = 0; i < threadVector.size(); i++) {
	    int thread = (1 << i);

	    println("#define _wait_" + i + "(sync,locNum,transNum,actionNum) \\");
	    println("  if \\");
	    println("  :: sync.owner == _pid -> sync.waiting = sync.waiting | " + thread + ";  \\");
	    println("       sync.owner = 0; \\");
	    println("  :: else -> printf(\"BIR: locNum transNum actionNum IllegalMonitorStateException\"); assert(0); \\");
	    println("  fi ");
	    println("#define _wait_" + i + "_R(sync,locNum,transNum,actionNum) \\");
	    println("  if \\");
	    println("  :: sync.owner == _pid -> sync.waiting = sync.waiting | " + thread + ";  \\");
	    println("       _temp_ = sync.count; sync.count = 0; sync.owner = 0; \\");
	    println("  :: else -> printf(\"BIR: locNum transNum actionNum IllegalMonitorStateException\"); assert(0); \\");
	    println("  fi ");

	    println("#define _wasNotified_" + i + "(sync) \\");
	    println("  !(sync.waiting & " + thread + ") ");
	    
	    if (! threadVector.elementAt(i).isMain()) {
		println("#define _startThread_" + i + " \\");
		println("  _threadActive_" + i + " = 1; _threadStart_" + 
			    i + "!ready");
		println("#define _beginThread_" + i + " \\");
		println("  _threadStart_" + i + "?ready");
		println("#define _joinThread_" + i + " \\");
		println("  skip");
		println("#define _exitThread_" + i + " \\");
		println("  _threadActive_" + i + " = 0; goto endtop_"  + i);
		println("#define _threadTerminated_" + i + " \\");
		println("  (_threadActive_" + i + " == 0)");
	    }
	}
	
	println("typedef object {\n  byte refIndex;\n  byte length; object& elements\n};");
	println("#define TRAP 1");
	println("mtype { ready };");
	println();
	}
	void initBlock() {
	ThreadVector threadVector = system.getThreads();
	println("init {");
	indentLevel = 2;
	println("atomic {");
	indentLevel++;
	StateVarVector stateVarVector = system.getStateVars();
	DSpinTypeInit initializer = new DSpinTypeInit(this,out);
	for (int i = 0; i < stateVarVector.size(); i++) {
	    StateVar var = stateVarVector.elementAt(i);
	    var.getType().apply(initializer,var);
	}
	for (int i = 0; i < threadVector.size(); i++) {
	    BirThread thread = threadVector.elementAt(i);
	    println("assert(run " + thread.getName() + "() == " +
		    (1 + thread.getId()) + ");");
	}
	indentLevel--;
	println("}");
	println("assert(! limit_exception);");
	indentLevel = 0;
	println("}");
	}
	boolean isIfBranch(TransVector outTrans) {
	if (outTrans.size() != 2) 
	    return false;
	Expr guard1 = outTrans.elementAt(0).getGuard();
	Expr guard2 = outTrans.elementAt(1).getGuard();
	if (guard1 == null || guard2 == null) 
	    return false;
	if ((guard1 instanceof NotExpr) 
	    && ((NotExpr)guard1).getOp().equals(guard2))
	    return true;
	if ((guard2 instanceof NotExpr) 
	    && ((NotExpr)guard2).getOp().equals(guard1))
	    return true;
	return false;
	}
	static String locLabel(Location loc) {
	if (loc.getOutTrans().size() == 0)
	    return "endloc_" + loc.getId();
	else
	    return "loc_" + loc.getId();
	}
	static String locLabel(Location loc1, int branch, Location loc2) {
	return "loc_" + loc1.getId() + "_" + branch + "_" + loc2.getId();
	}
	boolean nonNegative(Expr expr) {
	if (expr instanceof ConstExpr) 
	    return ((ConstExpr)expr).getValue() >= 0;
	if (expr.getType() instanceof Range) {
	    Range type = (Range) expr.getType();
	    return (type.getFromVal().getValue() >= 0);
	}
	return false;
	}
	static int parseInt(String s) {
	try {
	    int result = Integer.parseInt(s);
	    return result;
	} catch (NumberFormatException e) {
	    throw new RuntimeException("Integer didn't parse: " + s);
	}
	}
	void parseLine(String line, BirTrace trace) {
	int pos = line.indexOf("BIR:");
	if (pos >= 0) {
	    StringTokenizer st = 
		new StringTokenizer(line.substring(pos + 4));
	    int locId = parseInt(st.nextToken());
	    int transNum = parseInt(st.nextToken());
	    int actionNum = parseInt(st.nextToken());
	    String status = st.nextToken();
	    Location loc = system.getLocation(locId);
	    Transformation trans = 
		loc.getOutTrans().elementAt(transNum);
	    if (status.equals("OK")) {
		if (actionNum == 1)
		    trace.addTrans(trans);
	    }
	    else
		trace.setTrap(status,trans,actionNum);
	    return;
	}
	pos = line.indexOf("BIR?");
	if (pos >= 0) {
	    StringTokenizer st = 
		new StringTokenizer(line.substring(pos + 4));
	    int actionNum = parseInt(st.nextToken());
	    int choiceNum = parseInt(st.nextToken());
	    trace.setChoice(actionNum,choiceNum);
	}
	}
	/**
	 * Parse the output of 'pan' and interpret it as either a violation 
	 * (sequence of transformations) or as a error-free analysis.
	 * <p>
	 * @return a vector of transformations (if there was an error found), or null (otherwise)
	 */

	public BirTrace parseOutput() {
	if (! ranVerifier) 
	    throw new RuntimeException("Did not run verifier on input");
	try {
			File sourceFile = new File(currentDir(), 
				       system.getName() + ".dprom");
			File trailFile = new File(currentDir(), 
				      system.getName() + ".dprom.trail");
	    BirTrace trace = new BirTrace(system);
	    // We use the presence of the trail file to detect success/failure
	    if (! trailFile.exists()) {
		trace.setVerified(true);   // property verified
		return trace;
	    }
		log.info("Using  command <" +Preferences.getDSpinAlias() + "> to run dspin.");
	    String output = 
		execAndWait(Preferences.getDSpinAlias() +" -t " + sourceFile.getAbsolutePath(), true);
	    BufferedReader reader = 
		new BufferedReader(new StringReader(output));
	    String line = reader.readLine();
	    while (line != null) {
		parseLine(line,trace);
		line = reader.readLine();
	    }
	    trace.done();
	    trace.setVerified(false);
	    return trace;
		} catch(IOException e) {
			throw new RuntimeException("Error parsing dSPIN output: \n" + e);
		}	
	}
	void predicates() {
	println("/* Predicates */");
	Vector preds = system.getPredicates();
	for (int i = 0; i < preds.size(); i++) {
	    Expr pred = (Expr) preds.elementAt(i);
	    String name = system.predicateName(pred);
	    inDefine = true;
	    println("#define " + name + " ");
	    indentLevel = 1;
	    printGuard(applyTo(pred).getCase(normal));
	    inDefine = false;
	    println("");
	    indentLevel = 0;
	}
	println("");
	}
	void print(String s) {
	if (newLine) {
	    for (int i = 0; i < indentLevel; i++)
		out.print("   ");
	    newLine = false;
	}
	out.print(s);
	}
	void printGuard(TreeNode tree) {
	if (tree instanceof ExprNode) {
	    ExprNode node = (ExprNode) tree;
	    print(node.expr1);
	}
	else if (tree instanceof TrapNode) {
	    TrapNode node = (TrapNode) tree;
	    print("TRAP");
	}
	else {
	    CaseNode node = (CaseNode) tree;
	    print("(  ");
	    for (int i = 0; i < node.size(); i++) {
		Case c = node.elementAt(i);
		indentLevel++;
		println("(  " + c.cond);
		print("&& ");
		indentLevel++;
		printGuard(c.node);
		indentLevel--;
		print(" )");
		indentLevel--;
		if (i < node.size() - 1) {
		    println();
		    print("|| ");
		}
	    }
	    println(" )");
	}
	}
	void println() {
	if (inDefine)
	    out.print("\\");
	out.println();
	newLine = true;
	}
	void println(String s) {
	print(s);
	println();
	}
	void printStatement(TreeNode tree) {
	if (tree instanceof ExprNode) {
	    ExprNode node = (ExprNode) tree;
	    println(node.expr1);
	}
	else if (tree instanceof TrapNode) {
	    TrapNode node = (TrapNode) tree;
	    print("   printf(\"BIR: " + actionId() 
		  + " " + node.desc + "\\n\"); ");
	    if (node.fatal)
		println("assert(0);");
	    else
		println("limit_exception = 1; goto endtrap;");
	}
	else {
	    CaseNode node = (CaseNode) tree;
	    println("if ");
	    for (int i = 0; i < node.size(); i++) {
		Case c = node.elementAt(i);
		String cond = (i < node.size() - 1) ? c.cond : "else";
		println(":: " + cond + " -> ");
		indentLevel++;
		printStatement(c.node);
		indentLevel--;
	    }
	    println("fi; ");
	}	
	}
	void property() {
	if (options.getApplyNeverClaim())
	    println("#include \"" + system.getName() + ".never\"");
	}
	int refIndex(StateVar target) {
	return 1 + system.refAnyType().getTargets().indexOf(target);
	}
	void resetDeadVariables(Location fromLoc, Location toLoc) {
	StateVarVector liveBefore = fromLoc.getLiveVars();
	StateVarVector liveAfter = toLoc.getLiveVars();
	if (liveBefore != null && liveAfter!= null) 
	    for (int i = 0; i < liveBefore.size(); i++) 
		if (liveAfter.indexOf(liveBefore.elementAt(i)) == -1) {
		    // var was live, now is not---reset it
		    StateVar var = liveBefore.elementAt(i);
		    var.apply(this);
		    print(getSimpleResult() + " = ");
		    var.getInitVal().apply(this);
		    println(getSimpleResult() + ";");
		}
	}
	DSpinTrans run() {
	header();
	definitions();
	stateVariables();
	transitions();
	predicates();
	property();
	return this;
	}
	/**
	 * Run SPIN to generate an analyzer (pan), and then run the analyzer.
	 * <p>
	 * The translator must have been executed for the transition system
	 * (the file NAME.dprom must exist and contain the translated PROMELA).
	 * @return the output of 'pan' (as a String)
	 */

	public String runSpin() {
	try {
	    if (options.getApplyNeverClaim()) {
		File ltlFile = new File(currentDir(), 
				       system.getName() + ".spin.ltl");
		if (! ltlFile.exists())
		    throw new RuntimeException("Cannot find LTL file: " +
					       ltlFile.getAbsolutePath());
		log.info("Using  command <" +Preferences.getDSpinAlias() + "> to run dspin.");
		String neverClaim = 
		    execAndWait(Preferences.getDSpinAlias() + " -F " + ltlFile.getAbsolutePath(), true);
		File neverFile = new File(currentDir(), 
					  system.getName() + ".never");
		FileOutputStream streamOut = new FileOutputStream(neverFile);
		PrintWriter writerOut = new PrintWriter(streamOut);
		writerOut.println(neverClaim);
		writerOut.close();
	    }
			File sourceFile = new File(currentDir(), 
				       system.getName() + ".dprom");
	    if (! sourceFile.exists())
		throw new RuntimeException("Cannot find dSPIN source file: " +
					   sourceFile.getAbsolutePath());
			File trailFile = new File(currentDir(), 
				      system.getName() + ".dprom.trail");
	    if (trailFile.exists())
		trailFile.delete();
	    File panFile = new File(currentDir(), 
				    "pan.exe");
		log.info("Using  command <" +Preferences.getDSpinAlias() + "> to run dspin.");
	    execAndWait(Preferences.getDSpinAlias() + " -a " + sourceFile.getAbsolutePath(), true);
		log.info("Using  command <" +Preferences.getCCAlias() + "> for c compiler.");
	    execAndWait(Preferences.getCCAlias() + " " + options.getCompilerCommandLineOptions() +
			Preferences.getCCOutputFileFlag() +" pan.exe -I " + includeDir + " pan.c", true);
	    ranVerifier = true;
	    return execAndWait(panFile.getCanonicalPath() + " " +
			       options.getPanCommandLineOptions(), true);
	    
		} catch(Exception e)
		{
			throw new RuntimeException("Could not produce SPIN file (" + e + ")");
		}	
	}
	TreeNode specialize(String expr) {
	return new CaseNode(new Case(normal, new ExprNode(expr)));
	}
	void stateVariables() {
	println("bool limit_exception = 0;");
	println("byte _i_ = 0;");
	println("object& _new_;");
	ThreadVector threads = system.getThreads();
	for (int i = 0; i < threads.size(); i++) {
	    if (! threads.elementAt(i).isMain()) {
		println("chan _threadStart_" + i + " = [1] of { bit };");
		println("bit _threadActive_" + i + ";");
	    }
	}
	println();
	StateVarVector stateVarVector = system.getStateVars();
	for (int i = 0; i < stateVarVector.size(); i++) {
	    StateVar var = stateVarVector.elementAt(i);
	    String refIndex = "   ";
	    if (var.getType().isKind(COLLECTION)) 
		println("/* Collection: " + var.getName() + ", ref index = "
			+ refIndex(var) + " */");
	    else {
		if (var.getType().isKind(RECORD|ARRAY))
		    refIndex += "/*  ref index = " + refIndex(var) + " */";
		println(dSpinTypeName(var.getType(),null) + " " 
			+ var.getName() + ";" + refIndex);
	    }
	}
	println();
	}
	void transitions() {
	ThreadVector threadVector = system.getThreads();
	for (int i = 0; i < threadVector.size(); i++) {
	    BirThread thread = threadVector.elementAt(i);
	    println("proctype " + thread.getName() + "() { ");
	    println("  int _temp_;");
	    if (! thread.isMain()) 
		println("endtop_" + i + ":\n   _beginThread_" + i + ";");
	    LocVector threadLocVector = thread.getLocations();
	    for (int j = 0; j < threadLocVector.size(); j++) 
		translateLocation(threadLocVector.elementAt(j));
	    println("endtrap:");
	    println("  0;");
	    println("}");
	    println();
	}
	initBlock();
	}
	/**
	 * Generate PROMELA source representing a transition system.
	 * <p>
	 * As above, but with default options.
	 * @param system the transition system
	 * @return the DSpinTrans control object
	 */

	public static DSpinTrans translate(TransSystem system) {
	return translate(system,null);
	}
	/**
	 * Generate PROMELA source representing a transition system.
	 * <p>
	 * The PROMELA is written to a file NAME.dprom where NAME
	 * is the name of the transition system.  A set of
	 * options can be provided in a DSpinOptions object.
	 * @param system the transition system
	 * @param options the SPIN verifier options
	 * @return the SpinTrans control object
	 */

	public static DSpinTrans translate(TransSystem system, 
				       DSpinOptions options) {
	try {
			File sourceFile = new File(currentDir(), 
				       system.getName() + ".dprom");

			FileOutputStream streamOut = new FileOutputStream(sourceFile);
			PrintWriter writerOut = new PrintWriter(streamOut);
	    DSpinTrans result = translate(system, options, writerOut);
			writerOut.close();
	    return result;
		} catch(IOException e)
		{
			throw new RuntimeException("Could not produce dSPIN file: " + e);
		}
	}
	/**
	 * Generate PROMELA source representing a transition system.
	 * <p>
	 * As above, but the PROMELA is written to the PrintWriter provided.
	 * @param system the transition system
	 * @param out the PrintWriter to write the PROMELA to
	 * @return the DSpinTrans control object
	 */

	public static DSpinTrans translate(TransSystem system, 
				       DSpinOptions options,
				       PrintWriter out) {
	return (new DSpinTrans(system,options,out)).run();
	}
	void translateBinaryOp(Expr e1, Expr e2, String op) {
	TreeNode e1Tree = applyTo(e1);
	TreeNode e2Tree = applyTo(e2);
	TreeNode result = e1Tree.compose(e2Tree,null);
	Vector leaves = result.getLeaves(new Vector());
	for (int i = 0; i < leaves.size(); i++) {
	    ExprNode leaf = (ExprNode) leaves.elementAt(i);
	    leaf.update("(" + leaf.expr1 + op + leaf.expr2 + ")");
	}
	setResult(result);
	}
	public void translateLocation(Location loc)
	{
	if (! loc.isVisible())
	    return;
	println(locLabel(loc) + ":");
	indentLevel++;
	TransVector outTrans = loc.getOutTrans();
	if (outTrans.size() == 1) 
	    translateSequence(outTrans.elementAt(0),0);
	else if (outTrans.size() == 0)  
	    println("0;");
	else {
	    println("if");
	    for (int i = 0;  i < outTrans.size(); i++) {
		print(":: ");
		indentLevel++;
		translateSequence(outTrans.elementAt(i),i);
		indentLevel--;
	    }
	    println("fi;");
	}
	indentLevel--;
	}
	void translateSeq(LocVector locSet, Transformation trans, 
		      int branch, boolean supressGuard) {
	translateTrans(trans, supressGuard);
	if (trans.getToLoc().isVisible()) {
	    resetDeadVariables(locSet.firstElement(), trans.getToLoc());
	    println("goto " + locLabel(trans.getToLoc()) + ";");
	}
	else if (locSet.contains(trans.getToLoc()))
	    println("goto " + 
		    locLabel(locSet.firstElement(),branch,trans.getToLoc()) 
		    + ";");
	else {
	    locSet.addElement(trans.getToLoc());
	    if (trans.getToLoc().getInTrans().size() > 1)
		println(locLabel(locSet.firstElement(),branch,trans.getToLoc())
			+ ":");
	    TransVector successors = trans.getToLoc().getOutTrans();
	    if (successors.size() == 1) 
		translateSeq(locSet, successors.elementAt(0), branch,false);
	    else if (isIfBranch(successors)) {
		println("if");
		print(":: ");
		indentLevel++;
		translateSeq(locSet, successors.elementAt(0), branch, false);
		indentLevel--;
		print(":: else -> ");
		indentLevel++;
		translateSeq(locSet, successors.elementAt(1), branch, true);
		indentLevel--;
		println("fi;");
	    }
	    else {
		println("if");
		for (int i = 0; i < successors.size(); i++) {
		    print(":: ");
		    indentLevel++;
		    translateSeq(locSet, successors.elementAt(i),branch,false);
		    indentLevel--;
		}
		println("fi;");
	    }	    
	}
	}
	void translateSequence(Transformation trans, int branch) {
	Location startLoc = trans.getFromLoc();
		println("atomic { ");
	indentLevel++;
	translateSeq(new LocVector(startLoc), trans, branch, false);
	indentLevel--;
	println("}");
	}
	void translateTrans(Transformation trans, boolean supressGuard)
	{
	currentTrans = trans;
	actionNum = 0;
	if (! supressGuard && guardPresent(trans)) {
	    printGuard(applyTo(trans.getGuard()).getCase(normal));
	    println("-> ");
	}
	ActionVector actions = trans.getActions();
	for (int i = 0; i < actions.size(); i++) {
	    actionNum = i + 1;
	    println("printf(\"BIR: " + actionId() + " OK\\n\"); ");
	    actions.elementAt(i).apply(this);
	}
	if (resetTemp) {
	    println("_temp_ = 0;");
	    resetTemp = false;
	}
	if (resetNew) {
	    println("_new_ = null;");
	    resetNew = false;
	}
	}
	void translateUnaryOp(Expr e, String op) {
	TreeNode result = applyTo(e);
	Vector leaves = result.getLeaves(new Vector());
	for (int i = 0; i < leaves.size(); i++) {
	    ExprNode leaf = (ExprNode) leaves.elementAt(i);
	    leaf.update("(" + op + leaf.expr1 + ")");
	}
	setResult(result);
	}
}
