package edu.ksu.cis.bandera.bui.counterexample;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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

import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.tree.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.birc.*;
import edu.ksu.cis.bandera.jext.*;
import java.util.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import edu.ksu.cis.bandera.annotation.Annotation;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.bir.ActiveThread;

/**
 * The BIRCTraceManager provides an implementation of a TraceManager
 * for use on BIR based counter example information.
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Yu Chen &lt;yuchen@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.6 $ - $Date: 2003/04/30 19:33:17 $
 */
public class BIRCTraceManager implements TraceManager {

    private static Category log = Category.getInstance(BIRCTraceManager.class);

    private static Jimple jimple = Jimple.v();
    private TypeTable typeTable;
    private AnnotationManager am = CompilationManager.getAnnotationManager();
    private SootClassManager scm = CompilationManager.getSootClassManager();
    private Vector annotationTrace = new Vector();
    private Vector stmtIndexTrace = new Vector();
    private Map valueNodeValueMap;
    private JimpleTrace trace;
    private int traceLength;
    private Stmt[] stmts;
    private JimpleStore currentStore;
    private int currentStep = 0;
    private Map valueNodeMaps;
    private Map threadMap;
    private Map threadAnnotationsMap;
    private List holdingEdges;
    private List waitingEdges;
    private Map threadTreeModelMap;
    private TreeModel variableTreeModel;

    /**
     * The threadHoldingMap provides a mapping from a thread to a set of
     * objects whose lock is held by the thread.  The key will be an Integer
     * which is the thread id) and the value will be a Set.  That Set will
     * contain ClassLiteral objects.
     */
    private Map threadHoldingMap;

    /**
     * BIRCTraceManager constructor comment.
     */
    public BIRCTraceManager(JimpleTrace trace, TypeTable typeTable) {

	// init the store of values
	valueNodeValueMap = new HashMap();

	// init the map from objects to value nodes
	valueNodeMaps = new HashMap();

	threadTreeModelMap = new HashMap();
	threadHoldingMap = new HashMap();
	
	this.trace = trace;
	if(trace != null) {
	    if(trace.isLivenessPropertyViolation()) {
		setViolationType(LIVENESS_PROPERTY_VIOLATION);
	    }
	    else if(trace.isDeadlockViolation()) {
		setViolationType(DEADLOCK_VIOLATION);
	    }
	    else if(trace.isAssertionViolation()) {
		setViolationType(ASSERTION_VIOLATION);
	    }
	    else if(trace.isPropertyViolation()) {
		setViolationType(PROPERTY_VIOLATION);
	    }
	    else {
	    }
	}

	this.am = CompilationManager.getAnnotationManager();
	this.scm = CompilationManager.getSootClassManager();
	this.typeTable = typeTable;
	stmts = trace.getStatements();
	for (int i = 0; i < stmts.length; i++) {
	    try {
		Annotation a = am.getContainingAnnotation((Stmt) stmts[i]);
		if(a != null) {
		    if ("edu.ksu.cis.bandera.annotation.SequentialAnnotation".equals(a.getClass().getName())) continue;
		    if (annotationTrace.size() > 0) {
			if (a != annotationTrace.lastElement()) {
			    stmtIndexTrace.add(new Integer(i));
			    annotationTrace.add(a);
			    //log.debug(a.getClass().getName() + " : " + a);
			}
			else {
			    log.debug("statement " + stmts[i] + " with annotation " + a + " is lost.");
			}
		    }
		    else {
			stmtIndexTrace.add(new Integer(i));
			annotationTrace.add(a);
			//log.debug(a.getClass().getName() + " : " + a);
		    }
		}
		else {
		    log.warn("Annotation for statement " + stmts[i] + " is null.");
		}
	    }
	    catch (Exception e) {
		log.warn("Couldn't find annotation for " + stmts[i], e);
	    }
	}
	traceLength = annotationTrace.size();
	currentStore = trace.getStore(0);
	reset();
    }

    public Set getThreadIDSet() {
	if(threadMap != null) {
	    return(threadMap.keySet());
	}
	else {
	    return(new HashSet(0));
	}
    }

    public int getThreadID(SootMethod sootMethod) {
	if((threadMap != null) && (threadMap.size() > 0)) {
	    Iterator threadIterator = threadMap.keySet().iterator();
	    while(threadIterator.hasNext()) {
		Integer threadID = (Integer)threadIterator.next();
		SootMethod currentSootMethod = (SootMethod)threadMap.get(threadID);
		if(sootMethod.equals(currentSootMethod)) {
		    return(threadID.intValue());
		}
	    }
	}
	return(-1);
    }

    private void updateThreadAnnotations(int index) {

	if(threadAnnotationsMap == null) {
	    threadAnnotationsMap = new HashMap();
	}

	if((threadMap != null) && (threadMap.size() > 0)) {
	    Iterator threadIterator = threadMap.keySet().iterator();
	    while(threadIterator.hasNext()) {
		Integer tid = (Integer)threadIterator.next();
		SootMethod sootMethod = (SootMethod)threadMap.get(tid);
		if(sootMethod != null) {
		    if (((JimpleBody) sootMethod.getBody(jimple)).getStmtList().indexOf(stmts[index]) >= 0) {
			Annotation a = (Annotation) annotationTrace.elementAt(currentStep);
			//threadAnnotations.setElementAt(a, tid.intValue());
			threadAnnotationsMap.put(tid, a);
			log.debug("Adding annotation " + a + " to the thread annotations map for thread " + tid + ".");
		    }
		}
	    }
	}

    }

    private void updateThreadMap() {

	if(threadMap == null) {
	    threadMap = new HashMap();
	}

	Set threadIDSet = currentStore.getThreadIDSet();
	if((threadIDSet != null) && (threadIDSet.size() > 0)) {
	    Iterator threadIterator = threadIDSet.iterator();
	    while(threadIterator.hasNext()) {
		Integer tid = (Integer)threadIterator.next();
		SootMethod sootMethod = currentStore.getSootMethod(tid.intValue());
		threadMap.put(tid, sootMethod);
		//log.debug("thread " + tid + " maps to SootMethod " + sootMethod);
	    }
	}

    }

    /**
     * Update the thread tree models for the active threads as well as the
     * global tree model (variableTreeModel).  While building the trees, we can
     * also re-build the store of values associated with each node in the tree.  It
     * is done this way in order to save time walking through the exact same structures
     * just to find the values.
     */
    private void updateTreeModels() {
	
	if(valueNodeValueMap == null) {
	    log.error("valueNodeValueMap is null.  This is a major problem and therefore we cannot" +
		      " retrieve the VariableTreeModel.");
	    return;
	}
	
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Store");
	DefaultMutableTreeNode staticRoot = new DefaultMutableTreeNode("Static Variables");
	root.add(staticRoot);

	// get the static variables and add them to the tree first.
	Hashtable compiledClasses = CompilationManager.getCompiledClasses();
	for (Enumeration e = compiledClasses.elements(); e.hasMoreElements();) {
	    SootClass sc = (SootClass) e.nextElement();
	    DefaultMutableTreeNode classRoot = null;

	    // find all the static fields for this class and create ValueNodes and nodes in the
	    //  tree for them.
	    ca.mcgill.sable.util.List fieldList = sc.getFields();
	    if((fieldList != null) && (fieldList.size() > 0)) {
		ca.mcgill.sable.util.Iterator fli = fieldList.iterator();
		while(fli.hasNext()) {
		    SootField sf = (SootField) fli.next();
		    if (Modifier.isStatic(sf.getModifiers())) {

			if(classRoot == null) {
			    // only create the class root if there is at least one static field for the class. -tcw
			    classRoot = new DefaultMutableTreeNode(sc.getName());
			    staticRoot.add(classRoot);
			}

			ValueNode vn = getValueNode(sf);
			Object o = currentStore.getValue(sf);
			Object t = typeTable.get(sf);
			if ((t != null) && (t instanceof IntegralAbstraction) && !(t instanceof ConcreteIntegralAbstraction)) {
			    o = getAbstractToken(typeTable.get(sf), ((IntegerLiteral) o).value);
			}
			valueNodeValueMap.put(vn, o);
			log.debug("Adding the variable " + vn + " with value " + o + " to the valueNodeValueMap.");
			classRoot.add(new DefaultMutableTreeNode(vn));

		    }
		}
	    }
	}

	DefaultMutableTreeNode threadRoot = new DefaultMutableTreeNode("Threads");
	root.add(threadRoot);

	// for each thread, get all the fields and method locals and add them to the tree
	Iterator threadIterator = getThreadIDSet().iterator();
	while(threadIterator.hasNext()) {
	    Integer threadID = (Integer)threadIterator.next();
	    int tid = threadID.intValue();
	    log.debug("now parsing thread " + tid + " to build the variable tree model.");
	    
	    DefaultMutableTreeNode threadNode = new DefaultMutableTreeNode("Thread " + tid + " : " + getClassName(tid));
	    threadTreeModelMap.put(threadID, threadNode);
	    threadRoot.add(threadNode);
	    // we should put the status of the thread into a ValueNode here and place it into the valueNodeValueMap. -tcw
	    
	    // if the thread is not alive, we can skip showing it's fields and method locals
	    if (!isAlive(tid)) {
		log.debug("thread " + tid + " is not alive so we will skip showing it's field and locals.");
		continue;
	    }
	    
	    // we will now walk through all the fields and the current method
	    SootClass threadClass = scm.getClass(getClassName(tid));
	    if(threadClass != null) {
		ca.mcgill.sable.util.List fieldList = threadClass.getFields();
		if((fieldList != null) && (fieldList.size() > 0)) {
		    ca.mcgill.sable.util.Iterator fli = fieldList.iterator();
		    while(fli.hasNext()) {
			SootField sf = (SootField) fli.next();
			if((sf != null) && (!Modifier.isStatic(sf.getModifiers()))) {
			    ValueNode vn = getValueNode(tid, sf);
			    DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(vn);
			    threadNode.add(fieldNode);
			    Object o = currentStore.getValue(tid, sf);
			    Object t = typeTable.get(sf);
			    if ((t != null) && (t instanceof IntegralAbstraction) && !(t instanceof ConcreteIntegralAbstraction)) {
				o = getAbstractToken(typeTable.get(sf), ((IntegerLiteral) o).value);

			    }
			    log.debug("Adding the variable " + vn + " with value " + o + " to the valueNodeValueMap. threadID = " + tid);
			    valueNodeValueMap.put(vn, o);
			}
			else {
			    log.debug("The SootField was null or static.");
			}
		    }
		}
		else {
		    log.debug("This thread (" + tid + ") has no fields.");
		}

		// find the current method and print the locals and params
		/*
		Annotation a = (Annotation)threadAnnotationsMap.get(threadID);
		if(a != null) {
		    a = am.getMethodAnnotationContainingAnnotation(a);
		    log.debug("Method annotation: " + a);
		}
		if(a == null) {
		    log.debug("Skipping this annotation since it is null.");
		}
		else if (a instanceof ConstuctorDeclarationAnnotation) {
		    ConstructorDeclarationAnnotation cda = (ConstructorDeclarationAnnotation)a;
		    SootMethod sootMethod = cda.getSootMethod();
		    if(sootMethod != null) {
			Hashtable locals = mda.getDeclaredLocalVariables();
			if(locals != null) {
			    log.debug("Method " + sootMethod.getName() + " has " + locals.size() + " locals.");
			}
			else {
			    log.debug("Method " + sootMethod.getName() + " has no locals.");
			}
			Hashtable parameters = mda.getParameterLocals();
			if(parameters != null) {
			    log.debug("Method " + sootMethod.getName() + " has " + parameters.size() + " parameters.");
			}
			else {
			    log.debug("Method " + sootMethod.getName() + " has no parameters.");
			}
		    }
		    else {
			log.warn("This MethodDeclarationAnnotation doesn't have a SootMethod associated with it!");
		    }
		}
		else if (a instanceof MethodDeclarationAnnotation) {
		    MethodDeclarationAnnotation mda = (MethodDeclarationAnnotation)a;
		    SootMethod sootMethod = mda.getSootMethod();
		    if(sootMethod != null) {
			Hashtable locals = mda.getDeclaredLocalVariables();
			if(locals != null) {
			    log.debug("Method " + sootMethod.getName() + " has " + locals.size() + " locals.");
			}
			else {
			    log.debug("Method " + sootMethod.getName() + " has no locals.");
			}
			Hashtable parameters = mda.getParameterLocals();
			if(parameters != null) {
			    log.debug("Method " + sootMethod.getName() + " has " + parameters.size() + " parameters.");
			}
			else {
			    log.debug("Method " + sootMethod.getName() + " has no parameters.");
			}
		    }
		    else {
			log.warn("This MethodDeclarationAnnotation doesn't have a SootMethod associated with it!");
		    }
		}
		*/

	    }
	    else {
		log.warn("There was no class for thread with ID " + tid);
	    }
	    
	}

	// set the root node we just created as the root of the variableTreeModel
	if(variableTreeModel == null) {
	    variableTreeModel = new DefaultTreeModel(root);
	}
	else {
	    ((DefaultTreeModel)variableTreeModel).setRoot(root);
	}
    }

    /**
     * This will cause the information to be moved back one step in the counter example
     * data.
     */
    public void back() {
	int index = ((Integer) stmtIndexTrace.elementAt(--currentStep)).intValue();
	currentStore = trace.getStore(index);
	/*
	if(log.isDebugEnabled()) {
	    currentStore.print();
	}
	*/
	updateThreadMap();
	updateThreadAnnotations(index);
	updateTreeModels();
	updateEdges();
    }

    /**
     * Check if a specified field name (fully qualified) exists in the compiled classes
     *
     * @return boolean
     * @param fieldName java.lang.String
     */
    public boolean checkField(String fieldName) {
	return false;
    }

    /**
     * cleanup method comment.
     */
    public void cleanup() {
    }

    /**
     * This will cause the information to moved forward one step in the counter example data.
     */
    public void forward() {

	if(currentStep == traceLength - 1) {
	    currentStore = trace.getLastStore();
	    currentStep++;
	    /*
	    if(log.isDebugEnabled()) {
		currentStore.print();
	    }
	    */
	    updateThreadMap();
	    updateTreeModels();
	    updateEdges();
	    return;
	}

	int index = ((Integer) stmtIndexTrace.elementAt(++currentStep)).intValue();
	currentStore = trace.getStore(index);
	/*
	if(log.isDebugEnabled()) {
	    currentStore.print();
	}
	*/
	updateThreadMap();
	updateThreadAnnotations(index);
	updateTreeModels();
	updateEdges();

    }

    /**
     * Get the String that represents the abstract value for the given type and value.
     *
     * @return String
     * @param type Object
     * @param value int
     */
    private String getAbstractToken(Object type, int value) {
	String className = type.getClass().getName();
	String methodName = "getToken";
	try {
	    Class cls = Class.forName(className);
	    Method mtd = cls.getMethod(methodName, new Class[] {int.class});
	    return (String) mtd.invoke(null, new Object[] {new Integer(value)});
	} catch (Exception e) {
	    return "" + value;
	}
    }

    /**
     * Get a list of all the holding edges that exist at this state in the counter example.  It
     * should return a List that contains 0 or more List objects.  Those List objects should
     * have a size of 2 where the first is the holder object and the second is the held object.
     * 
     * @return List A list of all the holding edges in the current state of the system.
     */
    public java.util.List getAllLockHoldingEdges() {
	return(holdingEdges);
    }

    /**
     * Get a list of all the waiting edges that exist at this state in the counter example.  It
     * should return a List that contains 0 or more List objects.  Those List objects should
     * have a size of 2 where the first is the requestor object (thread) and the second is the requested object.
     *
     * @return List A list of all the waiting edges in the current state of the system.
     */
    public java.util.List getAllLockWaitingEdges() {
	return(waitingEdges);
    }

    /**
     * De-Reference the given object down to a 'real' literal.  In other
     * works, go from a ReferenceLiteral down to something like a ClassLiteral,
     * ArrayLiteral, IntLiteral, or BooleanLiteral.
     *
     * @param Object object The object to de-reference.
     * @param boolean complete If true, continue to de-reference until
     *                we find an object that is not of type ReferenceLitreal.
     * @return The de-reference value of the object given.  If given null, returns null.
     */
    private Object dereference(Object object, boolean complete) {

	Object d = null;

	if(object == null) {
	    log.debug("De-referencing a null object.  Returning null.");
	    d = null;
	}
	else if(object instanceof JimpleLiteral) {
	    JimpleLiteral jl = (JimpleLiteral)object;
	    while((jl != null) && (jl instanceof ReferenceLiteral)) {
		ReferenceLiteral rl = (ReferenceLiteral)jl;
		jl = rl.value;
	    }
	    d = jl;
	}
	else {
	    log.warn("Not sure how to de-reference an object of type " +
		     object.getClass().getName() + ".");
	    d = null;
	}

	return(d);
    }

    /**
     * Update the waiting and holding edge lists using the current state.
     *
     * @pre valueNodeValueMap != null
     * @pre updateTreeModels is up to date and should be called before this method
     *      in each step (forward, backward, and reset).
     */
    private void updateEdges() {
	log.debug("starting updateEdges ...");

	// make sure the waiting edges are empty
	if(waitingEdges == null) {
	    waitingEdges = new ArrayList();
	}
	waitingEdges.clear();

	// make sure the holding edges are empty
	if(holdingEdges == null) {
	    holdingEdges = new ArrayList();
	}
	holdingEdges.clear();

	if(valueNodeValueMap == null) {
	    log.error("valueNodeValueMap is null.  " +
		      "We cannot get a list of holding edges without a mapping " +
		      "of ValueNodes to Values.  Returning null.");
	    return;
	}

	// clear the holding sets for each thread
	Iterator thmi = threadHoldingMap.keySet().iterator();
	while(thmi.hasNext()) {
	    Integer tid = (Integer)thmi.next();
	    Set s = (Set)threadHoldingMap.get(tid);
	    s.clear();
	}

	Set keySet = valueNodeValueMap.keySet();
	log.debug("valueNodeValueMap.size = " + valueNodeValueMap.size());
	Iterator iterator = keySet.iterator();
	while(iterator.hasNext()) {
	    Object currentKey = iterator.next();  // this is probably a ValueNode
	    Object currentValue = valueNodeValueMap.get(currentKey);  // this is probably a JimpleLiteral
	    //log.debug("currentKey = " + currentKey + ", currentValue = " + currentValue);
		
	    /* de-reference if necessary */
	    log.debug("currentValue (before dereference) = " + currentValue);
	    currentValue = dereference(currentValue, true);
	    log.debug("currentValue (after dereference) = " + currentValue);
		
	    /* locks are only valid if we have a ClassLiteral (ignore all other JimpleLiterals) */
	    if((currentValue != null) && (currentValue instanceof ClassLiteral)) {
		ClassLiteral classLiteral = (ClassLiteral)currentValue;

		LockLiteral lockLiteral = classLiteral.getLockValue();
		if(lockLiteral == null) {
		    log.debug("no lockLiteral for classLiteral " + classLiteral);
		    continue;
		}

		ActiveThread holdingThread = lockLiteral.getHoldingThread();
		if(holdingThread == null) {
		    log.debug("no holdingThread for lockLiteral " + lockLiteral);
		}
		else {
		    int threadID = holdingThread.getTid();
		    log.debug("id = " + holdingThread.getId() + ", tid = " + holdingThread.getTid());

		    // Add this class literal to the set of objects held by this thread
		    Integer tid = new Integer(threadID);
		    Set holdingSet = (Set)threadHoldingMap.get(tid);
		    if(holdingSet == null) {
			holdingSet = new HashSet();
			threadHoldingMap.put(tid, holdingSet);
		    }
		    holdingSet.add(classLiteral);

		    String className = getClassName(threadID);
		    String holderString = "Thread #" + threadID + ":" + className;
		    String heldString = "#" + classLiteral.getId();
		    String associationString = "";
		    if(currentKey instanceof ValueNode) {
			currentKey = ((ValueNode)currentKey).object;
		    }
		    if(currentKey instanceof SootField) {
			SootField sootField = (SootField)currentKey;
			if(Modifier.isStatic(sootField.getModifiers())) {
			    associationString = sootField.getDeclaringClass().toString() + "." +
				sootField.getName() + ":" + sootField.getType().toString();
			}
			else {
			    associationString = sootField.getDeclaringClass().toString() + "." +
				sootField.getName() + ":" + sootField.getType().toString();
			}
		    }
		    else if(currentKey instanceof Local) {
			Local local = (Local)currentKey;
			associationString = local.getName() + "." + local.getType().toString();
		    }
		    else {
			log.info("Not sure how to display this type of node: " + currentKey.getClass().getName());
			associationString = currentKey.toString();
		    }
		    
		    List l = new ArrayList(3);
		    l.add(holderString);
		    l.add(associationString);
		    l.add(heldString);
		    
		    log.debug("Adding a holding edge: holder = " + holderString +
			      ", association = " + associationString + ", held = " + heldString);
		    holdingEdges.add(l);
		}
	    }
	    else {
		log.debug("currentValue (" + currentValue +
			  ") is null or not a ClassLiteral.  Therefore, there is no thread holding the lock.");
	    }
	}

	// walk thru all the threads and get the object they are blocked-on
	Set threadIDSet = getThreadIDSet();
	if((threadIDSet != null) && (threadIDSet.size() > 0)) {
	    Iterator threadIterator = threadIDSet.iterator();
	    while(threadIterator.hasNext()) {
		Integer threadID = (Integer)threadIterator.next();
		log.debug("checking thread " + threadID + " for block ...");
		Object o = currentStore.getBlockedOn(threadID.intValue());
		if(o != null) {
		    log.debug("block found for thread " + threadID + ", block = " + o);

		    Object requestedObject = resolveObject(o);
		    if(requestedObject != null) {
			if(requestedObject instanceof ValueNode) {
			    requestedObject = ((ValueNode)requestedObject).object;
			}

			String requestedString = "";
			while((o != null) && (o instanceof ReferenceLiteral)) {
			    o = ((ReferenceLiteral)o).value;
			}
			if(o instanceof ClassLiteral) {
			    ClassLiteral classLiteral = (ClassLiteral)o;
			    requestedString = "#" + classLiteral.getId();
			}
			else {
			    requestedString = "Unknown Object ID";
			}

			String associationString = "";
			if(requestedObject instanceof SootField) {
			    SootField sootField = (SootField)requestedObject;
			    if(Modifier.isStatic(sootField.getModifiers())) {
				associationString = sootField.getDeclaringClass().toString() + "." +
				    sootField.getName() + ":" + sootField.getType().toString();
			    }
			    else {
				associationString = sootField.getName() + ":" + sootField.getType().toString();
			    }
			}
			else if(requestedObject instanceof Local) {
			    Local local = (Local)requestedObject;
			    associationString = local.getName() + "." + local.getType().toString();
			}
			else {
			    log.info("Not sure how to display this type of node: " + requestedObject.getClass().getName());
			    associationString = requestedObject.toString();
			}

			String className = getClassName(threadID.intValue());
			String requestorString = "Thread #" + threadID.toString() + ":" + className;
			List l = new ArrayList(3);
			l.add(requestorString);
			l.add(associationString);
			l.add(requestedString);
			waitingEdges.add(l);

			log.debug("found waiting edge: requestor = " + requestorString + ", requested = " +
				  requestedString + ", association = " + associationString);
		    }
		}
		else {
		    log.debug("there is no block for thread " + threadID);
		}
	    }
	}

	if(holdingEdges != null) {
	    log.debug("holding edges reported: " + holdingEdges.size());
	}
	else {
	    log.debug("no holding edges found (actually, holdingEdges is null.");
	}
	
	if(waitingEdges != null) {
	    log.debug("waiting edges reported = " + waitingEdges.size());
	}
	else {
	    log.debug("no waiting edges found (actually, waitingEdges is null.");
	}

	log.debug("finished updateEdges.");
    }

    /**
     * This method will attempt to figure out the field or local that
     * points to this object value.  This will walk through all the values
     * in the current state attempting to find the value that is equivalent
     * to this object and then mapping it back to the field or local.
     *
     * @param Object o The object to resolve.
     */
    private Object resolveObject(Object o) {
	Object resolvedObject = o;

	log.debug("resolving object " + o);

	Set keySet = valueNodeValueMap.keySet();
	Iterator iterator = keySet.iterator();
	while (iterator.hasNext()) {
	    Object currentKey = iterator.next();
	    Object currentValue = valueNodeValueMap.get(currentKey);
	    
	    if (currentValue == null) {
		// skip this value since it is null
		continue;
	    }
	    
	    if (currentValue == o) {
		log.debug("found the match = " + currentKey);
		resolvedObject = currentKey;
		break;
	    }
	    
	    if ((currentValue instanceof ReferenceLiteral)
		&& (o instanceof ReferenceLiteral)) {
		
		ReferenceLiteral currentValueReference = (ReferenceLiteral) currentValue;
		ReferenceLiteral oReference = (ReferenceLiteral) o;
		Object currentValueReferenceValue = currentValueReference.value;
		Object oReferenceValue = oReference.value;
		
		if ((currentValueReferenceValue != null) &&
		    (currentValueReferenceValue.equals(oReferenceValue))) {
		    log.debug("found the match = " + currentKey);
		    resolvedObject = currentKey;
		    break;
		}
		else {
		    log.debug("reference values didn't match: " +
			      "currentValueReferenceValue = " + currentValueReferenceValue +
			      "oReferenceValue = " + oReferenceValue);
		}
	    }
	}

	log.debug("resolved object " + o + " and got " + resolvedObject);
	return(resolvedObject);
    }

    /**
     * getAnnotation method comment.
     */
    public Annotation getAnnotation() {
	if (currentStep == traceLength) return (Annotation) annotationTrace.elementAt(currentStep - 1);
	return (Annotation) annotationTrace.elementAt(currentStep);
    }

    /**
     * getAnnotation method comment.
     */
    public Annotation getAnnotation(int threadID) {
	Integer tid = new Integer(threadID);
	Annotation annotation = (Annotation)threadAnnotationsMap.get(tid);
	return(annotation);
    }

    /**
     * Get the current value for the specified field
     * 
     * @return java.lang.Object
     * @param FieldName java.lang.String
     */
    public Object getFieldValue(String FieldName) {
	throw new UnsupportedOperationException("The call to BIRCTraceManager.getFieldValue(Sting s) is not supported.");
    }

    /**
     * getNumOfSteps method comment.
     */
    public int getNumOfSteps() {
	return traceLength;
    }

    /**
     * getThreadTreeModels method comment.
     */
    public Vector getThreadTreeModels() {
	throw new UnsupportedOperationException("The call to getThreadTreeModels is no longer supported.");
    }

    /**
     * Grab the tree model for the given thread.
     *
     * [Note: This should be moved into a view class instead of the model.]
     */
    public TreeModel getThreadTreeModel(int threadID) {
	TreeNode rootNode = (TreeNode)threadTreeModelMap.get(new Integer(threadID));
	return(new DefaultTreeModel(rootNode));
    }

    /**
     * Get the value associated with this variable (or ValueNode).
     *
     * @param ValueNode vn The variable whose value we want.
     * @return String The value associated with this variable.
     */
    public String getValue(ValueNode vn) {
	return(getValueAsObject(vn).toString());
    }

    /**
     * Insert the method's description here.
     * 
     * @return java.lang.Object
     * @param sootField ca.mcgill.sable.soot.SootField
     */
    public Object getValueAsObject(SootField sootField) {

	/* is this still useful?  or necessary? */


	
	Object value = null;

	// walk thru the valueNodeValueMap and see if the current one is this SootField,
	//  if so, take the value associated with this sootField and return it.
	Set keySet = valueNodeValueMap.keySet();
	Iterator iterator = keySet.iterator();
	while(iterator.hasNext()) {
	    Object currentKey = iterator.next();
	    if(currentKey instanceof ValueNode) {
		ValueNode valueNode = (ValueNode)currentKey;
			
		if((valueNode.object != null) && (valueNode.object == sootField)) {
		    value = valueNodeValueMap.get(currentKey);
		    break;
		}
	    }
	}

	return(value);
    }

    /**
     * Insert the method's description here.
     * 
     * @return java.lang.Object
     * @param valueNode edu.ksu.cis.bandera.bui.counterexample.ValueNode
     */
    public Object getValueAsObject(ValueNode valueNode) {
	Object literal = valueNodeValueMap.get(valueNode);
	return(literal);
    }

    /**
     * Get the ValueNode object that is associated with this object.  The object
     * can be anything but will most likely be a static field (SootField) or
     * a local (Local).  If this is the first time we see this object, we will
     * create a new ValueNode for it (and add it to our collection).
     *
     * @return ValueNode The ValueNode associated with this object.
     * @param Object key The key to find the associated ValueNode.
     */
    public ValueNode getValueNode(Object key) {

	if(key == null) {
	    return(null);
	}

	ValueNode valueNode = null;
	
	Map valueNodeMap = (Map)valueNodeMaps.get("static");
	if(valueNodeMap == null) {
	    valueNodeMap = new HashMap();
	    valueNodeMaps.put("static", valueNodeMap);
	}

	if(valueNodeMap.containsKey(key)) {
	    valueNode = (ValueNode)valueNodeMap.get(key);
	}
	else {
	    valueNode = new ValueNode(key);
	    valueNodeMap.put(key, valueNode);
	}

	return(valueNode);
    }

    /**
     * Get the ValueNode object that is associated with this object in the
     * specified thread.  The object inside the ValueNode can be anything but
     * will likely be a SootField or a Local.  If we have not seen this
     * ValueNode yet, we will create one and place it into our store.
     *
     * @return ValueNode The ValueNode associated with this object.
     * @param Object key The key to find the associated ValueNode.
     * @param int threadID The thread this object is in.
     */
    public ValueNode getValueNode(int threadID, Object key) {

	if(key == null) {
	    return(null);
	}

	ValueNode valueNode = null;

	Map valueNodeMap = (Map)valueNodeMaps.get(new Integer(threadID));
	if(valueNodeMap == null) {
	    valueNodeMap = new HashMap();
	    valueNodeMaps.put(new Integer(threadID), valueNodeMap);
	}

	if(valueNodeMap.containsKey(key)) {
	    valueNode = (ValueNode)valueNodeMap.get(key);
	}
	else {
	    valueNode = new ValueNode(key);
	    valueNodeMap.put(key, valueNode);
	}

	return(valueNode);
    }

    /**
     * Get the value text for a node in the Store tree.
     *
     * The valueText for an array should be:
     * <ol>
     * <li>Object #<id>
     * <li>Lock Held by Thread #<tid> or Lock Not Held !!!!!!!!!!!!!!!!!!!!! needs to be implemented
     * <li>Size of Array: <size>
     * </ol>
     *
     * The valueText for a class should be:
     * <ol>
     * <li>Object #<id>
     * <li>Lock Held by Thread #<tid> or Lock Not Held
     * </ol>
     *
     * The valueText for a reference should be:
     * <ol>
     * <li>de-reference the value and get the value
     * </ol>
     *
     * The valueText for a thread should be:  !!!!!!!!!!!!!!!!!!!!!!! needs to be implemented
     * <ol>
     * <li>Thread #<tid>
     * <li>Status: <status> (Ready, Terminated, Running, Blocked on Lock, Blocked on Wait)
     * <li>Waiting for lock on: <id> (only when status is blocked on lock)
     * <li>Holding locks: <id>, ...
     * <li>Current: <class>.<method> -> <statement>
     * </ol>
     *
     * The valueText for a boolean should be:
     * <ol>
     * <li>True or False
     * </ol>
     *
     * The valueText for an integer should be:
     * <ol>
     * <li>String.valueOf(integer)
     * </ol>
     *
     * The valueText for a method should be:
     * <ol>
     * <li>current statement
     * </ol>
     *
     * @param DefaultMutableTreeNode node The node in the tree for
     *        which to get the value as a String.
     * @return String The value as a String for the given node.  This
     *         might be an empty String or a String with a value of null.
     */
    public String getValueText(DefaultMutableTreeNode node) {

	String valueText = "";
	String newLineString = System.getProperty("line.separator");

	if(node == null) {
	    valueText = "";
	}
	else {
	    Object o = node.getUserObject();
	    if(o == null) {
		valueText = "";
	    }
	    else if(o instanceof ValueNode) {
		ValueNode valueNode = (ValueNode)o;
		Object value = valueNodeValueMap.get(valueNode);
		if(value == null) {
		    log.debug("This ValueNode's value is null.");
		    valueText = "null";
		}
		else if(value instanceof BooleanLiteral) {
		    BooleanLiteral booleanLiteral = (BooleanLiteral)value;
		    if(booleanLiteral.value) {
			valueText = "True";
		    }
		    else {
			valueText = "False";
		    }
		}
		else if(value instanceof IntegerLiteral) {
		    IntegerLiteral integerLiteral = (IntegerLiteral)value;
		    valueText = String.valueOf(integerLiteral.value);
		}
		else if(value instanceof ReferenceLiteral) {
		    ReferenceLiteral referenceLiteral = (ReferenceLiteral)value;
		    if(referenceLiteral.value == null) {
			valueText = "null";
		    }
		    else if(referenceLiteral.value instanceof ArrayLiteral) {
			ArrayLiteral arrayLiteral = (ArrayLiteral)referenceLiteral.value;
			StringBuffer sb = new StringBuffer();
			sb.append("Object #" + arrayLiteral.getId() + newLineString);
		    
			/* Currently not implemented. -tcw
			   LockLiteral lockLiteral = classLiteral.getLockValue();
			   ActiveThread activeThread = lockLiteral.getHoldingThread();
			   if(activeThread != null) {
			   sb.append("Lock Held by Thread #" + String.valueOf(activeThread.getTid()));
			   }
			   else {
			   sb.append("Lock Not Held");
			   }
			*/
		    
			sb.append("Size of Array: " + arrayLiteral.length);
		    
			valueText = sb.toString();
		    }
		    else if(referenceLiteral.value instanceof ClassLiteral) {
			ClassLiteral classLiteral = (ClassLiteral)referenceLiteral.value;
			StringBuffer sb = new StringBuffer();
			sb.append("Object #" + classLiteral.getId() + newLineString);
		    
			LockLiteral lockLiteral = classLiteral.getLockValue();
			if(lockLiteral != null) {
			    ActiveThread activeThread = lockLiteral.getHoldingThread();
			    if(activeThread != null) {
				sb.append("Lock Held by Thread #" + String.valueOf(activeThread.getTid()) + newLineString);
			    }
			    else {
				sb.append("Lock Not Held" + newLineString);
			    }
			}
			else {
			    sb.append("Lock Not Held" + newLineString);
			}
		    
			valueText = sb.toString();
		    }
		    else if(referenceLiteral.value instanceof LockLiteral) {
			valueText = "LockLiteral not handled correctly at this time.  Please fix this.";
		    }
		    else {
			log.info("Not sure how to decode a value with type: " + referenceLiteral.value.getClass().getName());
			valueText = referenceLiteral.value.toString();
		    }
		}
		else if(value instanceof ArrayLiteral) {
		    ArrayLiteral arrayLiteral = (ArrayLiteral)value;
		    StringBuffer sb = new StringBuffer();
		    sb.append("Object #" + arrayLiteral.getId() + newLineString);

		    /* Currently not implemented. -tcw
		       LockLiteral lockLiteral = classLiteral.getLockValue();
		       ActiveThread activeThread = lockLiteral.getHoldingThread();
		       if(activeThread != null) {
		       sb.append("Lock Held by Thread #" + String.valueOf(activeThread.getTid()));
		       }
		       else {
		       sb.append("Lock Not Held");
		       }
		    */

		    sb.append("Size of Array: " + arrayLiteral.length + newLineString);

		    valueText = sb.toString();
		}
		else if(value instanceof ClassLiteral) {
		    ClassLiteral classLiteral = (ClassLiteral)value;
		    StringBuffer sb = new StringBuffer();
		    sb.append("Object #" + classLiteral.getId() + newLineString);

		    LockLiteral lockLiteral = classLiteral.getLockValue();
		    if(lockLiteral != null) {
			ActiveThread activeThread = lockLiteral.getHoldingThread();
			if(activeThread != null) {
			    sb.append("Lock Held by Thread #" + String.valueOf(activeThread.getTid()) + newLineString);
			}
			else {
			    sb.append("Lock Not Held" + newLineString);
			}
		    }
		    else {
			sb.append("Lock Not Held" + newLineString);
		    }

		    valueText = sb.toString();
		}
		else if(value instanceof LockLiteral) {
		    valueText = "LockLiteral not handled correctly at this time.  Please fix this.";
		}
		else {
		    log.info("Not sure how to decode a value with type: " + value.getClass().getName());
		    valueText = value.toString();
		}
	    }
	    else if(o instanceof String) {
		String s = (String)o;
		if(s.startsWith("Thread")) {
		    int end = s.indexOf(" ", 7);
		    if(end > 7) {
			String tidString = s.substring(7, end);
			try {
			    int tid = Integer.parseInt(tidString);
			    StringBuffer sb = new StringBuffer();
			    
			    // Thread <id>
			    sb.append("Thread #" + tid + newLineString);
			    
			    // Status: <status>
			    sb.append("Status: ");
			    if(currentStore.isActive(tid)) {
				sb.append("Active" + newLineString);
			    }
			    else {
				sb.append("Not Active" + newLineString);
			    }
			    /*
			     * Add to the status: blocked on lock, blocked on wait, etc.
			     */

			    // Currently Holding locks on objects: <id>, ...
			    Set holdingSet = (Set)threadHoldingMap.get(new Integer(tid));
			    if((holdingSet == null) || (holdingSet.size() <= 0)) {
			    }
			    else {
				sb.append("Currently Holding locks on objects: ");
				Iterator hsi = holdingSet.iterator();
				while(hsi.hasNext()) {
				    Object value = hsi.next();
				    if(value == null) {
					sb.append("null?");
				    }
				    else if(value instanceof ClassLiteral) {
					ClassLiteral classLiteral = (ClassLiteral)value;
					sb.append("#" + classLiteral.getId());
				    }
				    else {
					sb.append("!ClassLiteral?");
				    }

				    if(hsi.hasNext()) {
					sb.append(", ");
				    }
				    else {
					sb.append(newLineString);
				    }
				}
			    }

			    // Waiting for lock on: <id>
			    JimpleLiteral blockedOn = (JimpleLiteral)currentStore.getBlockedOn(tid);
			    if(blockedOn == null) {
				// nothing i guess!
			    }
			    else if(blockedOn instanceof ClassLiteral) {
				ClassLiteral classLiteral = (ClassLiteral)blockedOn;
				sb.append("Waiting for lock on: Object #" + classLiteral.getId() + newLineString);
			    }
			    else if(blockedOn instanceof ReferenceLiteral) {
				ReferenceLiteral referenceLiteral = (ReferenceLiteral)blockedOn;
				if(referenceLiteral.value == null) {
				}
				else if(referenceLiteral.value instanceof ClassLiteral) {
				    ClassLiteral classLiteral = (ClassLiteral)referenceLiteral.value;
				    sb.append("Waiting for lock on: Object #" + classLiteral.getId() + newLineString);
				}
				else {
				    // not sure what to do here! -tcw
				    sb.append("Error getting the id of the object we are waiting on." + newLineString);
				}
			    }
			    else {
				// not sure what to do here! -tcw
				sb.append("Error getting the id of the object we are waiting on." + newLineString);
			    }

			    // Current statement: <class>.<method> -> <statement>
			    String currentStatement = getCurrentStatement(tid);
			    if((currentStatement == null) || (currentStatement.length() <= 0)) {
				// nothing i guess!
			    }
			    else {
				sb.append("Current statement:" + newLineString);
				sb.append("     " + currentStatement + newLineString);
			    }
			    
			    valueText = sb.toString();
			}
			catch(Exception e) {
			    log.warn("Could not get the thread id from this node in the tree: " + s);
			    valueText = "";
			}
		    }
		    else {
			log.debug("Couldn't find a tid in this String: " + s);
			valueText = "";
		    }
		}
		else {
		    log.debug("This String doesn't start with 'Thread': " + s);
		    valueText = "";
		}
	    }
	    else {
		log.info("Not sure what to do with a node that holds an object of type: " + o.getClass().getName());
		valueText = "";
	    }
	}

	return(valueText);
    }

    /**
     * Get the current statement source code as a String that is being executed in
     * the specified thread.
     *
     * @return String The current source code that is executing at the current step
     *         in the specified thread.
     * @param int threadID The thread in which to get the current statement.
     */
    private String getCurrentStatement(int threadID) {
	
	String currentStatement = null;
	Annotation a = (Annotation)threadAnnotationsMap.get(new Integer(threadID));
	if(a == null) {
	    log.debug("There is no current statement since the annotation is null.");
	}
	else {
	    currentStatement = a.toString();
	}
	return(currentStatement);

    }

    /**
     * Get the children for a node in the tree if it exists.  During the
     * expansion, we need to make sure to add the new values to our table.
     *
     * The children list for an array should include a node for
     * each index in the array from 0 to array length.
     *
     * The children list for a base type (boolean or integer) is null.
     *
     * The children list for a class should include a node for
     * each field in the class that is not static.
     *
     * The children list for a thread should include a node for
     * each field in the class as well as a node for the current method.
     *
     * @param TreeNode node The node in the tree to
     *        get the children for.
     * @return List A list of MutableTreeNode objects that represent
     *         the children of the given node.  This value might
     *         be null or an empty list which will denote no children.
     */
    public List getNodeChildren(DefaultMutableTreeNode node) {

	/*
	 * This should store the child list if possible so it is generated each time. -tcw
	 */

	List childrenList = null;
	if(node == null) {
	    log.debug("The node given is null and therefore has no children.");
	    childrenList = null;
	}
	else {
	    Object o = node.getUserObject();
	    if(o == null) {
		log.debug("The user object for this node was null and therefore has no children.");
		childrenList = null;
	    }
	    else if(o instanceof ValueNode) {
		ValueNode valueNode = (ValueNode)o;
		Object value = valueNodeValueMap.get(valueNode);
		if(value == null) {
		    log.debug("The value associated with this node is null and therefore has no children.");
		    childrenList = null;
		}
		else if(value instanceof ReferenceLiteral) {
		    ReferenceLiteral referenceLiteral = (ReferenceLiteral)value;
		    if(referenceLiteral.value == null) {
			log.debug("The value is a null reference and therefore has no children.");
		    }
		    else if(referenceLiteral.value instanceof BooleanLiteral) {
			log.debug("The value that is referenced is a boolean and therefore has no children.");
			childrenList = null;
		    }
		    else if(referenceLiteral.value instanceof IntegerLiteral) {
			log.debug("The value that is referenced is an integer and therefore has no children.");
			childrenList = null;
		    }
		    else if(referenceLiteral.value instanceof ClassLiteral) {
			log.debug("The value that is referenced is a class ... collecting the fields.");
			ClassLiteral classLiteral = (ClassLiteral)referenceLiteral.value;
			SootClass sc = classLiteral.getSource();
			ca.mcgill.sable.util.Iterator i = sc.getFields().iterator();
			while(i.hasNext()) {

			    // lazy init of the children list so we only create one when
			    //  there are fields in this class.
			    if(childrenList == null) {
				childrenList = new ArrayList();
			    }

			    SootField sf = (SootField) i.next();
			    Object fieldValue = classLiteral.getFieldValue(sf);
			    if((fieldValue != null) && (fieldValue.equals(valueNodeValueMap.get(valueNode)))) {
				log.info("Found a pointer back to ourselves.  Skipping it.");
				/* since this is a pointer back to ourselves, we will skip it to avoid an
				 * an infinte number of nodes in the tree. -tcw */
			    }
			    else {
				log.debug("Adding field: " + sf.getName() + " to class " + sc.getName());
				ValueNode newValueNode = new ValueNode(sf);
				Object classValue = classLiteral.getFieldValue(sf);
				if(classValue != null) {
				    valueNodeValueMap.put(newValueNode, classValue);
				}
				else {
				    log.debug("The value stored in the class literal for this field (" +
					      sf.getName() + " is null.");
				}
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newValueNode);
				childrenList.add(newNode);
			    }
			}
		    
		    }
		    else if(referenceLiteral.value instanceof ArrayLiteral) {
			log.debug("The value that is referenced is an array ... collecting the objects.");
			ArrayLiteral arrayLiteral = (ArrayLiteral)referenceLiteral.value;
			if(arrayLiteral.length < 1) {
			    log.debug("The array contains no data and therefore has no children.");
			    childrenList = null;
			}
			else {
			    log.debug("The array has " + arrayLiteral.length + " objects.");
			    childrenList = new ArrayList(arrayLiteral.length);
			    for(int i = 0; i < arrayLiteral.length; i++) {
				ValueNode newValueNode = new ValueNode(arrayLiteral, i);
				valueNodeValueMap.put(newValueNode, arrayLiteral.contents[i]);
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newValueNode);
				childrenList.add(newNode);
			    }
			}
		    }
		    else if(referenceLiteral.value instanceof LockLiteral) {
			log.debug("The value referenced is a lock and therefore has no children.");
			childrenList = null;
		    }
		    else {
			log.debug("Not sure how to expand a referenced value of type: " +
				  referenceLiteral.value.getClass().getName() +
				  " and therefore has no children.");
			childrenList = null;
		    }
		}
		else if(value instanceof BooleanLiteral) {
		    log.debug("The value for this node is a boolean and therefore has no children.");
		    childrenList = null;
		}
		else if(value instanceof IntegerLiteral) {
		    log.debug("The value for this node is an integer and therefore has no children.");
		    childrenList = null;
		}
		else if(value instanceof ClassLiteral) {
		    log.debug("The value for this node is a class ... collecting the fields.");
		    ClassLiteral classLiteral = (ClassLiteral)value;
		    SootClass sc = classLiteral.getSource();
		    ca.mcgill.sable.util.Iterator i = sc.getFields().iterator();
		    while(i.hasNext()) {

			// lazy init of the children list so we only create one when
			//  there are fields in this class.
			if(childrenList == null) {
			    childrenList = new ArrayList();
			}

			SootField sf = (SootField) i.next();
			Object fieldValue = classLiteral.getFieldValue(sf);
			if((fieldValue != null) && (fieldValue.equals(valueNodeValueMap.get(valueNode)))) {
			    log.info("Found a pointer back to ourselves.  Skipping it.");
			    /* since this is a pointer back to ourselves, we will skip it to avoid an
			     * an infinte number of nodes in the tree. -tcw */
			}
			else {
			    log.debug("Adding field " + sf.getName());
			    ValueNode newValueNode = new ValueNode(sf);
			    valueNodeValueMap.put(newValueNode, classLiteral.getFieldValue(sf));
			    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newValueNode);
			    childrenList.add(newNode);
			}
		    }
		    
		}
		else if(value instanceof ArrayLiteral) {
		    log.debug("The value for this node is an array ... collecting the objects.");
		    ArrayLiteral arrayLiteral = (ArrayLiteral)value;
		    if(arrayLiteral.length < 1) {
			log.debug("The array contained no data and therefore has no children.");
			childrenList = null;
		    }
		    else {
			log.debug("The array has " + arrayLiteral.length + " objects.");
			childrenList = new ArrayList(arrayLiteral.length);
			for(int i = 0; i < arrayLiteral.length; i++) {
			    ValueNode newValueNode = new ValueNode(arrayLiteral, i);
			    valueNodeValueMap.put(newValueNode, arrayLiteral.contents[i]);
			    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newValueNode);
			    childrenList.add(newNode);
			}
		    }
		}
		else if(value instanceof LockLiteral) {
		    log.debug("The value was a lock and therefore has no children.");
		    childrenList = null;
		}
		else {
		    log.debug("Not sure how to handle a value of type: " + value.getClass().getName());
		    childrenList = null;
		}
	    }
	    else {
		log.debug("Not sure what to do with a user object from a node of type" + o.getClass().getName());
	    }
	}

	if(log.isDebugEnabled()) {
	    if(childrenList == null) {
		log.debug("There are no children for this node.");
	    }
	    else {
		log.debug("There are " + childrenList.size() + " children for this node.");
	    }
	}

	return(childrenList);
    }

    /**
     * getValueText method comment.
     */
    public String getValueText(DefaultTreeModel model, DefaultMutableTreeNode node) {

	if (!(node.getUserObject() instanceof ValueNode))
	    return "";
	ValueNode vn = (ValueNode) node.getUserObject();
	Object literal = valueNodeValueMap.get(vn);
	if (literal == null) {
	    log.debug("The value associated with value node " + vn + " is null.");
	    return "";
	}

	if (literal instanceof ReferenceLiteral) {
	    if (((ReferenceLiteral) literal).value == null) {
		log.debug("The value associated with value node " + vn +
			  " is a literal that is a reference to a null value.");
		return "null";
	    }
	    literal = ((ReferenceLiteral) literal).value;
	    if (literal instanceof ArrayLiteral) {
		ArrayLiteral arrayLiteral = (ArrayLiteral) literal;
		if (node.getChildCount() == 0) {
		    int size = arrayLiteral.length;
		    for (int i = 0; i < size; i++) {
			ValueNode vNode = new ValueNode(arrayLiteral, i);
			model.insertNodeInto(new DefaultMutableTreeNode(vNode), node, node.getChildCount());
			valueNodeValueMap.put(vNode, arrayLiteral.contents[i]);
		    }
		}
		return "#" + arrayLiteral.getId();
	    }
	    else {
		ClassLiteral classLiteral = (ClassLiteral) literal;
		if (node.getChildCount() == 0) {
		    SootClass sc = classLiteral.getSource();
		    for (ca.mcgill.sable.util.Iterator i = sc.getFields().iterator(); i.hasNext();) {
			SootField sf = (SootField) i.next();
			Object fieldValue = classLiteral.getFieldValue(sf);
			if((fieldValue != null) && (fieldValue.equals(valueNodeValueMap.get(vn)))) {
			    log.info("Found a pointer back to ourselves.  Skipping it.");
			    /* since this is a pointer back to ourselves, we will skip it to avoid an
			     * an infinte number of nodes in the tree. -tcw */
			}
			else {
			    ValueNode vNode = new ValueNode(sf);
			    model.insertNodeInto(new DefaultMutableTreeNode(vNode), node, node.getChildCount());
			    valueNodeValueMap.put(vNode, classLiteral.getFieldValue(sf));
			}
		    }
		}
		String result = "#" + classLiteral.getId() + "\nHolding: ";
		LockLiteral lockLiteral = classLiteral.getLockValue();
		ActiveThread activeThread = lockLiteral.getHoldingThread();
		if(activeThread != null) {
		    result += "Thread ID" + activeThread.getTid();
		}
		result += "\nWaiting: ";
		if(lockLiteral.getNumWaiting() <= 0) {
		    result += "None.";
		}
		else {
		    for (int i = 0; i < lockLiteral.getNumWaiting(); i++) {
			activeThread = lockLiteral.getWaitingThread(i);
			int tid = activeThread.getTid();
			result += "Thread ID" + tid;
			
			// if this isn't the last one, append a comma
			if (i != (lockLiteral.getNumWaiting() - 1)) {
			    result += ", ";
			}
		    }
		}

		return result;
	    }
	} else if (literal instanceof BooleanLiteral) {
	    return "" + ((BooleanLiteral) literal).value;
	} else if (literal instanceof IntegerLiteral) {
	    return "" + ((IntegerLiteral) literal).value;
	} else {
	    log.debug("The literal's type was not known by this method: " + literal.getClass().getName());
	    return literal.toString();
	}
    }

    /**
     * Retrieve the variable tree model to show the current store of
     * variables.
     *
     * @return TreeModel The tree view of all the variables in the system.
     */
    public TreeModel getVariableTreeModel() {
	return(variableTreeModel);
    }

    /**
     * Check to see if the thread with this thread ID is active.
     *
     * @param int threadID
     * @return True if the thread is active, False otherwise.
     */
    public boolean isAlive(int threadID) {
	//return currentStore.isActive((SootMethod) threads.elementAt(threadID));
	return(currentStore.isActive(threadID));
    }
    
    /**
     * reset method comment.
     */
    public void reset() {
	currentStep = 0;
	currentStore = trace.getStore(0);
	/*
	if(log.isDebugEnabled()) {
	    currentStore.print();
	}
	*/
	updateThreadMap();
	updateThreadAnnotations(0);
	updateTreeModels();
	updateEdges();
    }

    /**
     * Update the values stored in the object table model.  This will update the
     * values based upon those that are stored in the valueNodeValueMap (which is
     * the store of variables and their associated values).
     *
     * @param ObjectTableModel objectTableModel The model to update the values in.
     * @pre valueNodeValueMap is not null.
     * @pre objectTableModel is not null.
     */
    public void updateObjectTableModelValues(ObjectTableModel objectTableModel) {

	if(valueNodeValueMap == null) {
	    log.error("valueNodeValueMap is null.  Cannot update the object table model using a null valueNodeValueMap.");
	    return;
	}

	if(objectTableModel == null) {
	    log.error("objectTableModel is null.  Cannot update a null model.");
	    return;
	}

	Set keySet = valueNodeValueMap.keySet();
	Iterator iterator = keySet.iterator();
	while(iterator.hasNext()) {
	    Object currentKey = iterator.next();
	    Object currentValue = valueNodeValueMap.get(currentKey);

	    /* decode the currentValue:
	     * 1) IntegerLiteral: int
	     * 2) BooleanLiteral: boolean
	     * 3) ReferenceLiteral: dereference
	     * 4) otherwise: object
	     */
	    if((currentValue != null) && (currentValue instanceof ReferenceLiteral)) {
		ReferenceLiteral referenceLiteral = (ReferenceLiteral)currentValue;
		currentValue = referenceLiteral.value;
	    }
		
	    if((currentValue != null) && (currentValue instanceof IntegerLiteral)) {
		IntegerLiteral integerLiteral = (IntegerLiteral)currentValue;
		currentValue = new Integer(integerLiteral.value);
	    }

	    if((currentValue != null) && (currentValue instanceof BooleanLiteral)) {
		BooleanLiteral booleanLiteral = (BooleanLiteral)currentValue;
		currentValue = new Boolean(booleanLiteral.value);
	    }

	    if((currentValue != null) && (currentValue instanceof ClassLiteral)) {
		ClassLiteral classLiteral = (ClassLiteral) currentValue;
		StringBuffer objectInfoStringBuffer = new StringBuffer();
		objectInfoStringBuffer.append("Object #" + classLiteral.getId());

		LockLiteral lockLiteral = classLiteral.getLockValue();
		if(lockLiteral != null) {
		    objectInfoStringBuffer.append(", Holding: ");
		    ActiveThread holdingThread = lockLiteral.getHoldingThread();
		    if (holdingThread != null)
			objectInfoStringBuffer.append("Thread #" + holdingThread.getTid());
		    objectInfoStringBuffer.append(", Waiting: ");
		    for (int i = 0; i < lockLiteral.getNumWaiting(); i++) {

			// grab the current waiting thread ID
			ActiveThread currentWaitingThread = lockLiteral.getWaitingThread(i);
			int currentWaitingThreadID = currentWaitingThread.getTid();

			// if this is the last one, don't append a comma to the end
			if (i == (lockLiteral.getNumWaiting() - 1)) {
			    objectInfoStringBuffer.append("Thread #" + currentWaitingThreadID);
			}
			else {
			    objectInfoStringBuffer.append("Thread #" + currentWaitingThreadID + ", ");
			}
		    }
		}
		currentValue = objectInfoStringBuffer.toString();
	    }

	    log.debug("updating ValueNode " + currentKey + " with value " + currentValue);

	    objectTableModel.add(currentKey, currentValue);
	}

    }
    /**
     * @see edu.ksu.cis.bandera.bui.counterexample.TraceManager#load(List)
     */
    public Object load(List l) {
	throw new UnsupportedOperationException("Currently, we cannot load a counter example using BIR information.");
    }

    /**
     * @see edu.ksu.cis.bandera.bui.counterexample.TraceManager#save()
     */
    public List save() {
	throw new UnsupportedOperationException("Currently, we cannot save a counter example using BIR information.");
    }

    /**
     * Get the class that defines the thread.
     *
     * @param int threadID A thread ID that maps to a current thread.
     * @return String The name of the class that declares the thread.
     */
    public String getClassName(int threadID) {
	String className = "UnknownClass";

	if((threadMap != null) && (threadMap.size() > 0)) {
	    Integer tid = new Integer(threadID);
	    SootMethod sootMethod = (SootMethod)threadMap.get(tid);
	    if(sootMethod != null) {
		SootClass sootClass = sootMethod.getDeclaringClass();
		className = sootClass.toString();
	    }
	    else {
		log.debug("sootMethod for threadID " + threadID + " is null.  className is " + className);
	    }
	}
	else {
	    log.debug("threadMap has no values.  className is " + className);
	}

	//log.debug("className for threadID " + threadID + " is " + className);
	return(className);
    }

    /**
     * Get the active thread ID at this stage in the counter example.  This will
     * allow us to handle that thread a little different in the GUI.  For example,
     * we will only update that thread's counter example window.
     *
     * @return int The currnetly active thread ID.
     */
    public int getActiveThreadID() {
	int activeThreadID = 0;

	if((trace != null) && (stmtIndexTrace != null) &&
	   (currentStep >= 0) && (currentStep < stmtIndexTrace.size())) {
	    int statementIndex = ((Integer) stmtIndexTrace.elementAt(currentStep)).intValue();
	    activeThreadID = trace.getActiveThreadIDAtStatement(statementIndex);
	}
	else {
	    log.warn("Using the default thread ID because an error occured.");
	}

	log.debug("Returning an active thread id: " + activeThreadID);
	return(activeThreadID);
    }

    private int violationType;

    public int getViolationType() {
	return(violationType);
    }

    public void setViolationType(int violationType) {
	switch(violationType) {
	case DEADLOCK_VIOLATION:
	case ASSERTION_VIOLATION:
	case PROPERTY_VIOLATION:
	case LIVENESS_PROPERTY_VIOLATION:
	    this.violationType = violationType;
	    break;
	default:
	    throw new IllegalArgumentException("The violation type must be in the set of possible values.  This value, " +
					       violationType + ", was not.");
	}
    }

    public String getViolationHintsText() {
	return(getViolationHintsText(violationType));
    }

    public List getViolationHints() {
	return(getViolationHints(violationType));
    }

    public String getViolationHintsText(int violationType) {
	String violationHintsText = "";

	List violationHints = getViolationHints(violationType);
	if((violationHints != null) && (violationHints.size() > 0)) {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    Iterator vhi = violationHints.iterator();
	    while(vhi.hasNext()) {
		pw.println(" - " + vhi.next().toString());
	    }
	    violationHintsText = sw.toString();
	}
	else {
	    violationHintsText = "No hints.";
	}

	return(violationHintsText);
    }

    private boolean initializedViolationHints = false;
    public List getViolationHints(int violationType) {

	if(!initializedViolationHints) {
	    initViolationHintsMap();
	}
	
	List violationHints = null;
	switch(violationType) {
	case DEADLOCK_VIOLATION:
	case ASSERTION_VIOLATION:
	case PROPERTY_VIOLATION:
	case LIVENESS_PROPERTY_VIOLATION:
	    violationHints = (List)violationHintsMap.get(new Integer(violationType));
	    break;
	default:
	    throw new IllegalArgumentException("The violation type must be in the set of possible values.  This value, " +
					       violationType + ", was not.");
	}

	return(violationHints);
    }

    private Map violationHintsMap;
    private void initViolationHintsMap() {

	if(violationHintsMap == null) {
	    violationHintsMap = new HashMap();
	}
	else {
	    violationHintsMap.clear();
	}

	Integer deadlockViolationKey = new Integer(DEADLOCK_VIOLATION);
	List deadlockViolationHints = new ArrayList();
	deadlockViolationHints.add("One way to diagnose a deadlock is to skip to the end and step backwards.");
	deadlockViolationHints.add("A good tool when dealing with deadlocks is to use the Lock graph.");
	violationHintsMap.put(deadlockViolationKey, deadlockViolationHints);

	Integer assertionViolationKey = new Integer(ASSERTION_VIOLATION);
	List assertionViolationHints = new ArrayList();
	assertionViolationHints.add("Use the variable watch window to keep track of the values involved in the assertion.");
	violationHintsMap.put(assertionViolationKey, assertionViolationHints);

	Integer propertyViolationKey = new Integer(PROPERTY_VIOLATION);
	List propertyViolationHints = new ArrayList();
	propertyViolationHints.add("Use the variable watch window to keep track of the values involved in the property.");
	violationHintsMap.put(propertyViolationKey, propertyViolationHints);

	Integer livenessPropertyViolationKey = new Integer(LIVENESS_PROPERTY_VIOLATION);
	List livenessPropertyViolationHints = new ArrayList();
	livenessPropertyViolationHints.add("A liveness property will eventually get into a infinite loop in which the property will never be satisfied.");
	violationHintsMap.put(livenessPropertyViolationKey, livenessPropertyViolationHints);

    }
}

