package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
 *               2001, 2002   Radu Iosif (iosif@cis.ksu.edu)         *
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
 * A BirState represents a state of a BIR transition system in a trace.
 * <p>
 * The main parts of the state are:
 * <ul>
 * <li> A "state vector": 
 *    a Literal array storing the value of all state variables 
 *    which are laid out as they might be in a compiler.
 * <li> A Location array storing the current location of each thread.
 * <li> A boolean array indicating whether each thread is active.
 * </ul>
 * The interface Literal is implemented by a collection of classes
 * that represent values of non-composite types (BoolLit, IntLit,
 * LockLit, RefLit).  These objects are the contents of the state
 * vector.
 * <p>
 * This class is a BIR expression switch---it translates a BIR expression
 * into either a Literal value (the r-value of the expression in this state)
 * or an Integer (the address in the state vector of the l-value of
 * the expression in this state).   The rules are:
 * <ul>
 * <li> Any expression with an array or record value always evaluates
 *   to the Integer address of that object in the state vector.
 * <li> If the LHS flag is set, then any expression will evaluate
 *  to the Integer address of that entity in the state vector
 *  (in this case, it is an error if the expression is not an l-value).
 *  This flag is set to true only while evaluating the left-hand-side of
 *  an assignment (since we want the address to update).
 * <li> Otherwise, the expression evaluates to a Literal representing
 *  the r-value of the expression in this state.
 * </ul>
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Radu Iosif &lt;iosif@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.4 $ - $Date: 2003/04/30 19:32:50 $
 */
public class BirState extends AbstractExprSwitch implements Cloneable, BirConstants {

    private static Category log = Category.getInstance(BirState.class.getName());

    private TransSystem system;
    private Literal[] store;             // Global store
    private ThreadVector threads;        // Active threads
    private ActiveThread currentThread;  // Currently scheduled thread
    private ActiveThread specificThread; // If non-null perform expression evaluation
  			                 // in the context of this thread. 
    private boolean lhs = false;         // Translating LHS of assign
    private int choice;                  // choice for nondeterministic actions
    private String output = "";          // Output from Bandera.print() stamenets

    BirState(TransSystem system) {
	this.system = system;
    }  

    public void applyAction(int tid, Action action, int choice) {
	currentThread = (ActiveThread) threads.elementAt(tid);
	this.choice = choice;
	action.apply(this);
    }

    public void caseAddExpr(AddExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new IntLit(op1.getValue() + op2.getValue()));
    }

    public void caseAndExpr(AndExpr expr) {
	expr.getOp1().apply(this);
	BoolLit op1 = (BoolLit) getResult();
	expr.getOp2().apply(this);
	BoolLit op2 = (BoolLit) getResult();
	setResult(new BoolLit(op1.getValue() && op2.getValue()));
    }

    public void caseArrayExpr(ArrayExpr expr) {
	boolean saveLhs = lhs;
	// Even if we're in LHS mode, eval index in normal mode
	// (the index could be a constant, which doesn't have an address)
	lhs = false;  
	expr.getIndex().apply(this);
	ConstExpr index = (ConstExpr) getResult();
	// restore mode
	lhs = saveLhs;                           
	expr.getArray().apply(this);
	Address addr = (Address) getResult();
	addr.incOffset(index.getValue() + 1);
	if (lhs || expr.getType().isKind(ARRAY|RECORD)) 
	    setResult(addr);
	else
	    setResult(addr.get());	
    }
  
    public void caseAssertAction(AssertAction assertAction) {
	// Assertions do not modify the state
    }
    /**
     * Execute assignment.
     */

    public void caseAssignAction(AssignAction assign) {
	assign.getRhs().apply(this);
	Literal val = (Literal) getResult();
	lhs = true;
	assign.getLhs().apply(this);
	Address addr = (Address) getResult();
	lhs = false;
	addr.set(val);
    }

    public void caseBoolLit(BoolLit expr) {
	setResult(expr);
    }

    public void caseInternChooseExpr(InternChooseExpr expr) {
	setResult(expr.getChoice(choice));
    }

    public void caseExternChooseExpr(ExternChooseExpr expr) {
	setResult(expr.getChoice(choice));
    }

    StateVar collectionAt(int index) {
	return system.refAnyType().getTargets().elementAt(index);
    }

    public void caseForallExpr(ForallExpr expr) {
	int collect = (choice >> 8) - 1; // collection has to be zero-based
	int index = choice & 255; // ref index is already zero-based
	setResult(new RefLit(collectionAt(collect), index));
    }

    public void caseConstant(Constant expr) {
	setResult(expr);
    }
  
    public void caseDerefExpr(DerefExpr expr) {
	// Even if we're in LHS mode, eval ref expr in normal mode
	// (it could be null, which has no address)
	boolean saveLhs = lhs;
	lhs = false;
	expr.getTarget().apply(this);
	Object result = getResult();
	if (result instanceof RefLit) {
	    RefLit ref = (RefLit) result;
	    lhs = saveLhs;
	    int offset = ref.getCollection().getOffset();
	    if (ref.getCollection().getType().isKind(COLLECTION)) 
		offset += ref.getIndex() * expr.getType().getExtent();
	    // It is assumed that collections are global
	    Address addr = new Address(store, offset);
	    if (lhs || expr.getType().isKind(ARRAY|RECORD)) 
		setResult(addr);
	    else
		setResult(addr.get());	
	}
	else {
	    if(result != null) {
		log.warn("When expecting a RefLit, got " + result + " which is of type " +
			 result.getClass().getName() + ".");
	    }
	    else {
		log.warn("When expecting a RefLit, got a null.");
	    }
	}
    }

    public void caseDivExpr(DivExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new IntLit(op1.getValue() / op2.getValue()));
    }

    public void caseEqExpr(EqExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new BoolLit(op1.getValue() == op2.getValue()));
    }

    public void caseInstanceOfExpr(InstanceOfExpr expr) {
	Ref refType = (Ref) expr.getRefExpr().getType();
	setResult(new BoolLit(refType.isSubtypeOf(expr.getArgType())));
    }

    public void caseIntLit(IntLit expr) {
	setResult(expr);
    }

    public void caseLeExpr(LeExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new BoolLit(op1.getValue() <= op2.getValue()));
    }

    public void caseLengthExpr(LengthExpr expr) {
	expr.getArray().apply(this);
	Address addr = (Address) getResult();
	setResult(addr.get());
    }

    public void caseLockAction(LockAction lockAction) {
	lhs = true;
	lockAction.getLockExpr().apply(this);
	Address addr = (Address) getResult();
	lhs = false;
	LockLit lock = (LockLit) addr.get(); 
	int operation = lockAction.getOperation();
	// BirThread thread = lockAction.getThread();
	addr.set(lock.nextState(operation, currentThread, choice));
    }

    public void caseLockTest(LockTest lockTest) {
	lockTest.getLockExpr().apply(this);
	LockLit lock = (LockLit) getResult();
	setResult(new BoolLit(lock.queryState(lockTest.getOperation(),
					      currentThread)));
    }
  
    public void caseLtExpr(LtExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new BoolLit(op1.getValue() < op2.getValue()));
    }

    public void caseMulExpr(MulExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new IntLit(op1.getValue() * op2.getValue()));
    }

    public void caseNeExpr(NeExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new BoolLit(op1.getValue() != op2.getValue()));
    }

    public void caseNewArrayExpr(NewArrayExpr expr) {
	StateVar target = expr.getCollection();
	Type type = ((Collection) target.getType()).getBaseType();
	BirTypeInit initializer = new BirTypeInit(this);
	// Use length computed from trace
	int length = target.getActualBaseTypeSize();
	int offset = target.getOffset() + choice * length;
	Address addr = new Address(store, offset);
	// Set length field
	addr.set(new IntLit(length));
	type.apply(initializer, addr);
	setResult(new RefLit(target, choice));
    }

    public void caseNewExpr(NewExpr expr) {
	StateVar target = expr.getCollection();
	Type type = ((Collection) target.getType()).getBaseType();
	// Make sure to initialize new object
	BirTypeInit initializer = new BirTypeInit(this);
	int offset = target.getOffset() + choice * type.getExtent();
	// Collections are global
	type.apply(initializer, new Address(store, offset));
	setResult(new RefLit(target, choice));
    }

    public void caseNotExpr(NotExpr expr) {
	expr.getOp().apply(this);
	BoolLit op = (BoolLit) getResult();
	setResult(new BoolLit(! op.getValue()));
    }

    public void caseNullExpr(NullExpr expr) {
	setResult(expr);
    }

    public void caseOrExpr(OrExpr expr) {
	expr.getOp1().apply(this);
	BoolLit op1 = (BoolLit) getResult();
	expr.getOp2().apply(this);
	BoolLit op2 = (BoolLit) getResult();
	setResult(new BoolLit(op1.getValue() || op2.getValue()));
    }

    public void casePrintAction(PrintAction printAction) {
	// Dump the output of Bandera.print() to the 'output' string
	Vector printItems = printAction.getPrintItems();
	for (int i = 0; i < printItems.size(); i++) {
	    Object item = printItems.elementAt(i);
	    if (item instanceof String)
		output += item;
	    else {
		((Expr)item).apply(this);
		output += getResult().toString();
	    }
	}
	output += "\n";
    }

    public void caseRecordExpr(RecordExpr expr) {
	expr.getRecord().apply(this);
	Address addr = (Address) getResult();
	int offset = addr.getOffset() + expr.getField().getOffset();
	if (lhs || expr.getType().isKind(ARRAY|RECORD))  {
	    setResult(new Address(addr.getStore(), offset));
	} else {
	    setResult(addr.getField(expr.getField().getOffset()));	
	}
    }

    public void caseRefExpr(RefExpr expr) {
	setResult(new RefLit(expr.getTarget(),0));
    }

    public void caseRemExpr(RemExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new IntLit(op1.getValue() % op2.getValue()));
    }

    public void caseStateVar(StateVar expr) {
	Literal[] the_store;
    if (specificThread != null) {
      the_store = specificThread.getStore();
    } else if (expr.getThread() != null) {      
	    the_store = currentThread.getStore();
	}
	else 
	    the_store = store;

	if (lhs || expr.getType().isKind(ARRAY|RECORD|COLLECTION))
	    setResult(new Address(the_store, expr.getOffset()));
	else
	    setResult(the_store[expr.getOffset()]);
    }

    public void caseSubExpr(SubExpr expr) {
	expr.getOp1().apply(this);
	ConstExpr op1 = (ConstExpr) getResult();
	expr.getOp2().apply(this);
	ConstExpr op2 = (ConstExpr) getResult();
	setResult(new IntLit(op1.getValue() - op2.getValue()));
    }

    public void caseThreadAction(ThreadAction threadAction) {
	if (threadAction.isStart()) {
	    int tid = threads.size();
	    ActiveThread thread = new ActiveThread(threadAction.getThreadArg(), tid);
	    Literal[] the_store = thread.getStore();

	    thread.initLocals(new BirTypeInit(this));
	    threads.addElement(thread);

	    for (int i = 0; i < thread.getParameters().size(); i ++) {
		StateVar var = thread.getParameters().elementAt(i);
		((Expr) threadAction.getActuals().elementAt(i)).apply(this);
		Literal val = (Literal) getResult();
		the_store[var.getOffset()] = val;
		log.debug("the_store[" + var.getOffset() + "] now equals " + val);
	    }

	    if (threadAction.getLhs() != null) {
		Expr lhs = threadAction.getLhs();
		Expr rhs = new IntLit(tid);
		caseAssignAction(new AssignAction(lhs, rhs));
	    }
	} else if (threadAction.isExit())
	    currentThread.setStatus(THREAD_EXITED);
    }

    public void caseThreadLocTest(ThreadLocTest threadLocTest) {
	threadLocTest.getLhs().apply(this);
	IntLit val = (IntLit) getResult();
	ActiveThread thread = (ActiveThread) threads.elementAt(val.getValue());
	setResult(new BoolLit(thread.getPC() == threadLocTest.getLocation()));
    }

    public void caseRemoteRef(RemoteRef remoteRef) {
	remoteRef.getLhs().apply(this);
	int tid = ((IntLit) getResult()).getValue();
	ActiveThread thread = (ActiveThread) threads.elementAt(tid);
	Literal[] the_store = thread.getStore();
	setResult(the_store[remoteRef.getVar().getOffset()]);
    }

    public void caseThreadTest(ThreadTest threadTest) {
	threadTest.getLhs().apply(this);
	IntLit val = (IntLit) getResult();
	ActiveThread thread = (ActiveThread) threads.elementAt(val.getValue());
	setResult(new BoolLit(thread.getStatus() == THREAD_EXITED));
    }
    /**
     * Update thread location once all transformation actions have completed.
     */

    public void completeTrans(Transformation trans) {
	currentThread.setPC(trans.getToLoc());
    }
    /**
     * Make a copy of this state
     */

    public BirState copy() {
	BirState result = new BirState(system);
	result.store = (Literal []) store.clone();
	result.threads = (ThreadVector) threads.clone();
	result.currentThread = currentThread;
	result.output = output;
	return result;
    }

    public void defaultCase(Object obj) {
	throw new RuntimeException("Forgot to handle case: " + obj);
    }

    public String exprValue(Expr expr) {
	expr.apply(this);
	if (expr.getType().isKind(ARRAY|RECORD))
	    return "Object";
	else 
	    return getResult().toString();
    }

  public void setSpecificThread(ActiveThread t) { specificThread = t; }
    public String getOutput() { return output; }
    public Literal [] getStore() { return store; }
    public ThreadVector getThreads() { return threads; }
    public ActiveThread getCurrentThread() { return currentThread; }
    public TransSystem getSystem() { return system; }
    /**
     * Build the initial state of a BIR transition system.
     */

    public static BirState initialState(TransSystem system, 
					TransVector transVector,
					Vector choiceVector) {
	int stateSize = 
	    setActualSizesFromTrace(system,transVector,choiceVector);
	BirState state = new BirState(system);
	state.threads = new ThreadVector();
	state.store = new Literal[stateSize];

	// Initialize each global variable using BIR type initializer
	BirTypeInit initializer = new BirTypeInit(state);
	StateVarVector vars = system.getStateVars();
	for (int i = 0; i < vars.size(); i++) {
	    StateVar var = vars.elementAt(i);
	    if (var.getThread() == null)
		var.getType().apply(initializer,var);
	}

	// Initialize thread loc and active vars
	//  ThreadVector threads = system.getThreads();
	//      for (int i = 0; i < threads.size(); i++) {
	//        BirThread thread = threads.elementAt(i);
	//        state.location[thread.getId()] = thread.getStartLoc();
	//        state.threadActive[thread.getId()] = thread.isMain();
	//      }

	// Create the main thread
	ThreadVector tvect = system.getThreads();
	BirThread mainThread = null;
	for (int i = 0; i < tvect.size(); i++) {
	    mainThread = tvect.elementAt(i);
	    if (mainThread.isMain()) 
		break;
	}

	if (mainThread == null)
	    throw new RuntimeException("Main thread not found");

	ActiveThread thread = new ActiveThread(mainThread, 0);
	thread.initLocals(initializer);
	state.currentThread = thread;
	state.threads.addElement(thread);

	return state;
    }
    public boolean isActive(ActiveThread thread) { 
	//    return threadActive[thread.getId()];
	return (thread.getStatus() != THREAD_EXITED);
    }
    public Expr isBlocked(int tid) {
	ActiveThread thread = (ActiveThread) threads.elementAt(tid);
	return thread.getBlock(this);
    }
    public void print() {
	System.out.println("STATE");

	//      for (int i = 0; i < store.length; i++)
	//        System.out.print(store[i] + " ");
	//      System.out.println();
	//      ThreadVector threads = system.getThreads();
	//      for (int i = 0; i < threads.size(); i++) {
	//        BirThread thread = threads.elementAt(i);
	//        if (threadActive[thread.getId()])
	//  	System.out.println("    " + thread.getName() + " at " +
	//  			   location[thread.getId()].getLabel());
	//      }

	for (int i = 0; i < threads.size(); i ++) {
	    ActiveThread thread = (ActiveThread) threads.elementAt(i);
	    if (thread.getStatus() == THREAD_ACTIVE) {
		System.out.print("    " + thread.getName() + 
				 "[" + thread.getTid() + "] at " +
				 thread.getPC().getLabel() + " ");
		if (thread.getBlock(this) != null)
		    System.out.print("blocked on " + thread.getBlock(this));
		System.out.println();
	    }
	}
    
	BirTypePrint printer = new BirTypePrint(this);    
	for (int k = 0; k < threads.size(); k ++) {
	    ActiveThread thread = (ActiveThread) threads.elementAt(k);
	    thread.printLocals(printer);
	}

	StateVarVector vars = system.getStateVars();
	for (int i = 0; i < vars.size(); i++) {
	    StateVar var = vars.elementAt(i);
	    if (var.getThread() == null) {
		System.out.print("    " + var.getName() + " = ");
		var.getType().apply(printer, var);
		System.out.println();
	    }	
	}
    }
    /**
     * Given the trace, compute the size of the state vector (which
     * holds the values of all state variables).
     * <p>
     * This used to be done statically given the collection
     * bounds in the BIR, but with dynamic verifiers like dSPIN
     * (which ignore these bounds), we don't know how many
     * collection elements are allocated until we see the trace.
     * <p>
     * We also set the choice for each Allocator action to indicate
     * the index into the collection of the instance that was allocated 
     * (this is just a count of the number allocated so far).  
     * Note that for NewArrayExpr, the choice that comes in is
     * the actual length---we reset it to the instance index
     * (the max length for the collection is computed and stored
     * in the collection).
     */
    public static int setActualSizesFromTrace(TransSystem system, 
					      TransVector transVector,
					      Vector choiceVector) {
	// Count number of allocator calls in trace for each collection
	// Also, for arrays, calculate max size of array in each collection
	StateVarVector vars = system.getStateVars();
	ThreadVector threads = system.getThreads();
	StateVarVector targets = system.refAnyType().getTargets();
	int numAlloc [] = new int[targets.size()]; 
	int maxArrayLength [] = new int[targets.size()]; 

	for (int i = 0; i < transVector.size(); i++) {
	    ActionVector actions = transVector.elementAt(i).getActions();
	    int choices [] = (int []) choiceVector.elementAt(i);
	    for (int j = 0; j < actions.size(); j++) {
		Action action = actions.elementAt(j);
		if (action.isAssignAction()) {
		    Expr rhs = ((AssignAction)action).getRhs();

		    // When we see an allocator, find the target and
		    // update the instance count.  For arrays,
		    // also update the max array size.
		    if (rhs instanceof Allocator) {
			int targetNum = 
			    targets.indexOf(((Allocator)rhs).getCollection());
			if (rhs instanceof NewArrayExpr) {
			    int arrayLength = choices[j];
			    if (arrayLength > maxArrayLength[targetNum])
				maxArrayLength[targetNum] = arrayLength;
			}
			// Set choice for alloc action to # of instance alloced
			choices[j] = numAlloc[targetNum];
			numAlloc[targetNum] += 1;
		    }
		}
	    }
	}

	// Now assign each variable an offset, using the allocator counts
	// to determine the size of collections.  Note that we use the 
	// term "extent" to denote the number of memory slots needed
	// by a variable, while "size" denotes the number of components
	// (e.g., array or collection elements).

	// First we do the locals
	for (int k = 0; k < threads.size(); k++) {      
	    log.debug("Locals for thread " + k + ":");
	    int localStoreSize = 0;
	    BirThread thread = threads.elementAt(k);
	    for (int i = 0; i < thread.getLocals().size(); i++) {
		StateVar var = thread.getLocals().elementAt(i);	
		var.setOffset(localStoreSize);
		int varExtent = 0;
		if (var.getType().isKind(COLLECTION)) {
		    int targetNum = targets.indexOf(var);
		    int numItems = numAlloc[targetNum];
		    var.setActualSize(numItems);
		    Type baseType = ((Collection)var.getType()).getBaseType();
		    int itemExtent = baseType.getExtent();
		    if (baseType.isKind(ARRAY)) {
			int elementExtent = 
			    ((Array)baseType).getBaseType().getExtent();
			int arraySize = maxArrayLength[targetNum];
			var.setActualBaseTypeSize(arraySize);
			itemExtent = 1 + arraySize * elementExtent;
			// Extra 1 for length field  
		    }
		    var.setActualBaseTypeExtent(itemExtent);
		    // Extent of collection depends on # items and their extent
		    varExtent = numItems * itemExtent;
		}
		else  // Extent of normal variable is just extent of its type
		    varExtent = var.getType().getExtent();

		localStoreSize += varExtent;
		log.debug(" Var " + var + " extent = " + varExtent + " offset = " + var.getOffset());
	    }
	}

	log.debug("Globals:");
	// Then the globals
	int globalStoreSize = 0;
	for (int i = 0; i < vars.size(); i ++) {
	    StateVar var = vars.elementAt(i);
	    if (var.getThread() == null) {
		var.setOffset(globalStoreSize);
		int varExtent = 0;
		if (var.getType().isKind(COLLECTION)) {
		    int targetNum = targets.indexOf(var);
		    int numItems = numAlloc[targetNum];
		    var.setActualSize(numItems);
		    Type baseType = ((Collection)var.getType()).getBaseType();
		    int itemExtent = baseType.getExtent();
		    if (baseType.isKind(ARRAY)) {
			int elementExtent = ((Array)baseType).getBaseType().getExtent();
			int arraySize = maxArrayLength[targetNum];
			var.setActualBaseTypeSize(arraySize);
			itemExtent = 1 + arraySize * elementExtent;
			// Extra 1 for length field  
		    }
		    var.setActualBaseTypeExtent(itemExtent);
		    // Extent of collection depends on # items and their extent
		    varExtent = numItems * itemExtent;
		}
		else  // Extent of normal variable is just extent of its type
		    varExtent = var.getType().getExtent();

		globalStoreSize += varExtent;
		log.debug(" Var " + var + " extent = " + varExtent + " offset = " + var.getOffset());
	    }
	}

	return globalStoreSize;
    }
}
