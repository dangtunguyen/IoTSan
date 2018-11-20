package edu.ksu.cis.bandera.birc;

import edu.ksu.cis.bandera.bir.ActiveThread;
import edu.ksu.cis.bandera.bir.BirConstants;
import edu.ksu.cis.bandera.bir.BirState;
import edu.ksu.cis.bandera.bir.BirThread;
import edu.ksu.cis.bandera.bir.Expr;
import edu.ksu.cis.bandera.bir.StateVar;
import edu.ksu.cis.bandera.bir.StateVarVector;
import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.bir.Type;
import edu.ksu.cis.bandera.bir.ThreadVector;

import edu.ksu.cis.bandera.birc.JimpleTrace;
import edu.ksu.cis.bandera.birc.JimpleStoreBuilder;

import ca.mcgill.sable.soot.SootField;
import ca.mcgill.sable.soot.SootMethod;

import ca.mcgill.sable.soot.jimple.Local;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.BitSet;
import java.util.Iterator;

import org.apache.log4j.Category;

/**
 * The JimpleStore provides a storage depot for all current values
 * in the system.  This includes:
 * <ul>
 * <li>Static fields</li>
 * <li>Static method locals</li>
 * <li>Fields</li>
 * <li>Method locals</li>
 * <li>Thread status (active and what it is blocked on)</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.4 $ - $Date: 2003/04/30 19:32:52 $
 */
public class JimpleStore implements BirConstants {

    private static Category log = Category.getInstance(JimpleStore.class);

    /**
     * The staticValueMap provides a map from static fields and method locals
     * to their current value.  The keys will be String values that represent
     * the full name of the variable (example: SomeClass.someField:SomeType or
     * SomeClass.someMethod.someLocal:SomeType) and the values will be
     * Objects (most likely JimpleLiterals).
     */
    private Map staticValueMap;

    /**
     * The threadValueMaps provides a map from threads to their individual
     * value map.  The keys will be Integer objects that represent the
     * thread IDs while the value will be a Map.  The Map will hold
     * values in the same way the staticValueMap does except they will
     * be specific to each thread.
     */
    private Map threadValueMaps;

    /**
     * The threadBlockMap provides a mapping from threads to the object that
     * they are currently blocked on.  The keys will be Integer objects that
     * represent the thread IDs while the value will be an Object.
     */
    private Map threadBlockMap;

    private Map threadMap;

    private TransSystem system;

    /**
     * The threadActiveBitSet provides a simple way to keep track of the active
     * threads.
     */
    private BitSet threadActiveBitSet;

    /**
     * Create a new empty JimpleStore.
     *
     * @post staticValueMap != null
     * @post threadValueMap != null
     * @post threadActiveBitSet != null
     * @post threadBlockMap != null
     * @post threadMap != null
     */
    private JimpleStore() {
	super();
	staticValueMap = new HashMap();
	threadValueMaps = new HashMap();
	threadActiveBitSet = new BitSet();
	threadBlockMap = new HashMap();
	threadMap = new HashMap();
    }

    /**
     * Create a new JimpleStore based upon the state information given
     * in the BirState provided.
     *
     * @param BirState birState The information to base the current store on.
     * @post staticValueMap != null
     * @post threadValueMap != null
     * @post threadActiveBitSet != null
     * @post threadBlockMap != null
     * @post threadMap != null
     */
    public JimpleStore(BirState birState) {
	this();

	system = birState.getSystem();

	JimpleStoreBuilder builder = new JimpleStoreBuilder(birState);

	Map threadStateVarMap = new HashMap();
	StateVarVector vars = system.getStateVars();
	if((vars != null) && (vars.size() > 0)) {
	    for(int i = 0; i < vars.size(); i++) {
		StateVar var = vars.elementAt(i);
		if(var == null) {
		    log.debug("The current StateVar (" + i + ") is null.");
		}
		else if(var.getThread() == null) {
		    Object javaVariable = system.getSource(var);
		    Object value = getVarValue(var, builder);
		    staticValueMap.put(javaVariable.toString(), value);
		    log.debug("Found a static StateVar (" + i + "). var = " + var +
			      ", javaVariable = " + javaVariable +
			      ", value = " + value);
		}
		else {
		    BirThread birThread = var.getThread();
		    Integer id = new Integer(birThread.getId());
		    Set stateVarSet = (Set)threadStateVarMap.get(id);
		    if(stateVarSet == null) {
			stateVarSet = new HashSet();
			threadStateVarMap.put(id, stateVarSet);
		    }
		    stateVarSet.add(var);
		    log.debug("Found an instance StateVar (" + i + ").  var = " + var +
			      ", id = " + id);
		}
	    }
	}
	else {
	    log.debug("The current system has no StateVars.");
	}

	ThreadVector threads = birState.getThreads();
	if((threads != null) && (threads.size() > 0)) {
	    for(int i = 0; i < threads.size(); i++) {
		BirThread birThread = threads.elementAt(i);
		if((birThread != null) && (birThread instanceof ActiveThread)) {
		    ActiveThread activeThread = (ActiveThread)birThread;
		    int threadID = activeThread.getTid();
		    int id = activeThread.getId();
		    threadActiveBitSet.set(threadID);
		    threadMap.put(new Integer(threadID), activeThread);

		    Set stateVarSet = (Set)threadStateVarMap.get(new Integer(id));
		    if((stateVarSet != null) && (stateVarSet.size() > 0)) {
			Map threadValueMap = new HashMap(stateVarSet.size());
			threadValueMaps.put(new Integer(threadID), threadValueMap);
			Iterator svsi = stateVarSet.iterator();
			while(svsi.hasNext()) {
			    StateVar var = (StateVar)svsi.next();
			    Object javaVariable = system.getSource(var);
			    Object value = getVarValue(var, builder, activeThread);
			    threadValueMap.put(javaVariable.toString(), value);
			    log.debug("Found an instance StateVar: id = " + id + ", threadID = " + threadID +
				      ", var = " + var + ", value = " + value + ", javaVariable = " + javaVariable);
			}
		    }

		    Expr e = activeThread.getBlock(birState);
		    if((e != null) && (e instanceof StateVar)) {
			StateVar var = (StateVar)e;
			Object javaVariable = system.getSource(var);

			Object block = null;
			Map threadValueMap = (Map)threadValueMaps.get(new Integer(threadID));
			if(threadValueMap != null) {
			    log.debug("Retrieving block from threadValueMap.");
			    block = threadValueMap.get(javaVariable.toString());
			}
			else {
			    log.debug("Getting block from getVarValue.");
			    block = getVarValue(var, builder, activeThread);
			}

			if(block != null) {

			    /*
			     * I think this should be translated back into a "global instance variable"? -tcw
			     */

			    threadBlockMap.put(new Integer(threadID), block);
			}
			else {
			    log.error("The thread reports a block on an object but we cannot get the translated value.");
			}
		    }
		}
	    }
	}
	else {
	    log.error("There are no threads in this state!");
	}

	/*
	if(log.isDebugEnabled()) {
	    print();
	}
	*/

    }

    /**
     * Get the value associated with the given static field.
     *
     * @param SootField sootField The field whose value is requested.
     * @return Object The value of the given instance of the field.  If
     *         not found or an error occurs, this may return null.
     * @throws IllegalStateException This will be thrown when the JimpleStore
     *         has not been initialized properly.  In this case, the staticValueMap
     *         is null.
     * @pre staticValueMap != null
     * @pre staticValueMap has been initialized and contains all the static field values.
     * @pre sootField != null
     */
    public Object getValue(SootField sootField) {
	if(staticValueMap == null) {
	    log.error("The static value mapping has not been initialized.");
	    throw new IllegalStateException("The static value mapping has not been initialized.");
	}

	if(sootField == null) {
	    return(null);
	}

	String javaVariable = sootField.getSignature();
	Object value = staticValueMap.get(javaVariable);
	return(value);
    }

    /**
     * Get the value associated with the given field in the given
     * instance of a thread.
     *
     * @param int threadID The thread in which the field resides.
     * @param SootField sootField The field whose value is requested.
     * @return Object The value of the given instance of the field.  If
     *         not found or an error occurs, this may return null.
     * @throws IllegalStateException This will be thrown when the JimpleStore
     *         has not been initialized properly.  In this case, the threadValueMaps
     *         is null.
     * @pre threadValueMaps != null
     * @pre threadValueMaps has been initialized and contains all the thread fields.
     */
    public Object getValue(int threadID, SootField sootField) {

	if(threadValueMaps == null) {
	    log.error("The thread value mappings have not been initialized.");
	    throw new IllegalStateException("The thread value mappings have not been initialized.");
	}

	Object value = null;
	Map threadValueMap = (Map)threadValueMaps.get(new Integer(threadID));
	if(threadValueMap != null) {
	    String javaVariable = sootField.getSignature();
	    value = threadValueMap.get(javaVariable);
	    log.debug("The variable " + javaVariable + " has value " + value + " in thread " + threadID + ".");
	}
	else {
	    log.debug("There are no associated values with this thread (" + threadID + ").");
	}

	return(value);
    }

    /**
     * Get the value associated with the given static method local.
     *
     * @param SootMethod sootMethod The method in which the locals is defined.
     * @param Local local The local whose value is requested.
     * @return Object The value of the given static method local.  If not
     *         found or an error occurs, this may return null.
     * @throws IllegalStateException This will be thrown when the JimpleStore
     *         has not been initialized properly.  In this case, the staticValueMap
     *         is null.
     * @pre staticValueMap != null
     * @pre staticValueMap has been initialized and contains all the static method local values.
     */
    public Object getValue(SootMethod sootMethod, Local local) {
	if(staticValueMap == null) {
	    log.error("The static value mapping has not been initialized.");
	    throw new IllegalStateException("The static value mapping has not been initialized.");
	}

	String javaVariable = sootMethod.getDeclaringClass().getName() + "." +
	    sootMethod.getName() + "." + local.getName() + ":" + local.getType().toString();
	Object value = staticValueMap.get(javaVariable);

	return(value);
    }

    /**
     * Get the value associated with the given method local in the
     * given instance of a thread.
     *
     * @param int threadID The thread in which the method local resides.
     * @param SootMethod sootMethod The method in which the local is defined.
     * @param Local local The local whose value is requested.
     * @return Object The value of the given instance of the method local.  If
     *         not found or an error occurs, this may return null.
     * @throws IllegalStateException This will be thrown when the JimpleStore
     *         has not been initialized properly.  In this case, the threadValueMaps
     *         is null.
     * @pre threadValueMaps != null
     * @pre threadValueMaps has been initialized and contains all the thread fields.
     */
    public Object getValue(int threadID, SootMethod sootMethod, Local local) {

	if(threadValueMaps == null) {
	    log.error("The thread value mappings have not been initialized.");
	    throw new IllegalStateException("The thread value mappings have not been initialized.");
	}

	if((sootMethod == null) || (local == null)) {
	    log.error("Cannot get the value for a null SootMethod or null Local.");
	}

	Object value = null;
	Map threadValueMap = (Map)threadValueMaps.get(new Integer(threadID));
	if(threadValueMap != null) {
	    String javaVariable = sootMethod.getDeclaringClass().getName() + "." +
		sootMethod.getName() + "." + local.getName() + ":" + local.getType().toString();
	    value = threadValueMap.get(javaVariable);
	}
	else {
	    log.debug("There was no Map associated with this thread (" + threadID + ").");
	}

	return(value);
    }

    /**
     * Determine if the given thread is active at this time.
     *
     * @param int threadID The thread whose block is requested.
     * @return boolean True if the given thread is active, false otherwise.
     * @throws IllegalStateException This will be thrown when the JimpleStore
     *         has not been initialized properly.  In this case, the threadActiveBitSet
     *         is null.
     * @pre threadActiveBitSet != null
     * @pre threadActiveBitSet has been initialized and contains flags for each active thread.
     */
    public boolean isActive(int threadID) {
	if(threadActiveBitSet == null) {
	    log.error("The thread active set has not been initialized.");
	    throw new IllegalStateException("The thread active set has not been initialized.");
	}
	boolean active = threadActiveBitSet.get(threadID);
	return(active);
    }

    /**
     * Get the object that the given thread is blocked on waiting for
     * the lock.  If the thread is not blocked on an object, this will
     * return null.
     *
     * @param int threadID The thread whose block is requested.
     * @return Object The object that this thread is blocked on.
     * @throws IllegalStateException This will be thrown when the JimpleStore
     *         has not been initialized properly.  In this case, the threadBlockMap
     *         is null.
     * @pre threadBlockMap != null
     * @pre threadBlockMap has been initialized and contains all blocks for active threads.
     */
    public Object getBlockedOn(int threadID) {
	if(threadBlockMap == null) {
	    log.error("The thread block mapping has not been initialized.");
	    throw new IllegalStateException("The thread block mapping has not been initialized.");
	}

	Object value = threadBlockMap.get(new Integer(threadID));
	return(value);
    }

    /**
     * Get the Set of all current thread IDs.
     *
     * @return Set The set of all thread IDs wrapped in Integer objects.
     */
    public Set getThreadIDSet() {
	if(threadValueMaps == null) {
	    log.error("The thread value mappings have not been initialized.");
	    throw new IllegalStateException("The thread value mappings have not been initialized.");
	}

	Set threadIDSet = threadValueMaps.keySet();
	return(threadIDSet);
    }

    /**
     * Get the SootMethod that is associated with the given thread ID.  This
     * will be a direct association with the run method that is declared.
     *
     * @param int threadID The thread ID.
     * @return SootMethod The SootMethod associated with the given thread ID.
     */
    public SootMethod getSootMethod(int threadID) {
	if(threadMap == null) {
	    log.error("The thread mapping from id to Active thread has not been initialized.");
	    throw new IllegalStateException("The thread mapping from id to Active thread has not been initialized.");
	}

	if(system == null) {
	    log.error("The system has not been initialized.");
	    throw new IllegalStateException("The system has not been initialized.");
	}

	SootMethod sootMethod = null;
	ActiveThread activeThread = (ActiveThread)threadMap.get(new Integer(threadID));
	if(activeThread != null) {
	    Object temp = system.getSource(activeThread);
	    if((temp != null) && (temp instanceof SootMethod)) {
		sootMethod = (SootMethod)temp;
	    }
	    else {
		log.error("The source of the thread was null or not a SootMethod.");
	    }
	}
	else {
	    log.error("The ActiveThread for threadID " + threadID + " was null.");
	}

	return(sootMethod);
    }

    /**
     * Print the current store.  Print the active threads, the static values, and
     * the instance values to the screen.
     */
    public void print() {
	
	if((staticValueMap == null) || (threadValueMaps == null) ||
	   (threadActiveBitSet == null) || (threadBlockMap == null)) {
	    log.error("The JimpleStore was not initialized properly.");
	    throw new IllegalStateException("The JimpleStore was not initialized properly.");
	}

	// print the active threads, blocks if they exist, and instance values
	for(int i = threadActiveBitSet.nextSetBit(0); i >= 0; i = threadActiveBitSet.nextSetBit(i + 1)) {
	    System.out.println("Thread #" + i + " is active.");

	    Object block = threadBlockMap.get(new Integer(i));
	    if(block != null) {
		while((block != null) && (block instanceof ReferenceLiteral)) {
		    System.out.println("    Blocked on " + block);
		    block = ((ReferenceLiteral)block).value;
		}
		if(block instanceof ClassLiteral) {
		    ClassLiteral cl = (ClassLiteral)block;
		    System.out.println("    Blocked on Object #" + cl.getId());
		}
		else {
		    System.out.println("    Blocked on " + block);
		}
	    }

	    Map threadValueMap = (Map)threadValueMaps.get(new Integer(i));
	    if((threadValueMap != null) && (threadValueMap.size() > 0)) {
		Iterator tvmi = threadValueMap.keySet().iterator();
		while(tvmi.hasNext()) {
		    String variable = (String)tvmi.next();
		    Object value = threadValueMap.get(variable);
		    if(value == null) {
			System.out.println("    Variable " + variable + " = null");
		    }
		    else if(value instanceof ReferenceLiteral) {
			StringBuffer sb = new StringBuffer();
			sb.append(value);
			while((value != null) && (value instanceof ReferenceLiteral)) {
			    value = ((ReferenceLiteral)value).value;

			    if((value != null) && (value instanceof ClassLiteral)) {
				sb.append(" -> Object #" + ((ClassLiteral)value).getId());
			    }
			    else {
				sb.append(" -> " + value);
			    }
			}
			System.out.println("    Variable " + variable + " = " + sb.toString());
		    }
		    else if(value instanceof ClassLiteral) {
			System.out.println("    Variable " + variable + " = Object #" + ((ClassLiteral)value).getId());
		    }
		    else {
			System.out.println("    Variable " + variable + " = " + value);
		    }
		}
	    }
	}

	// print the static values
	Iterator svmi = staticValueMap.keySet().iterator();
	while(svmi.hasNext()) {
	    String variable = (String)svmi.next();
	    Object value = staticValueMap.get(variable);
	    if(value == null) {
		System.out.println("Static variable " + variable + " = null");
	    }
	    else if(value instanceof ReferenceLiteral) {
		StringBuffer sb = new StringBuffer();
		sb.append(value);
		while((value != null) && (value instanceof ReferenceLiteral)) {
		    value = ((ReferenceLiteral)value).value;
		    if((value != null) && (value instanceof ClassLiteral)) {
			sb.append(" -> Object #" + ((ClassLiteral)value).getId());
		    }
		    else {
			sb.append(" -> " + value);
		    }
		}
		System.out.println("Static variable " + variable + " = " + sb.toString());
	    }
	    else if(value instanceof ClassLiteral) {
		System.out.println("Static variable " + variable + " = Object #" + ((ClassLiteral)value).getId());
	    }
	    else {
		System.out.println("Static variable " + variable + " = " + value);
	    }
	}

    }

    /**
     * The value associated with this StateVar.  This will use the JimpleStoreBuilder
     * to convert the var into a value that can be used elsewhere (from a Literal to
     * a JimpleLiteral).  This assume that the value is a global.
     */
    private Object getVarValue(StateVar var, JimpleStoreBuilder builder) {
	return(getVarValue(var, builder, null));
    }

    /**
     * The value associated with this StateVar.  This will use the JimpleStoreBuilder
     * to convert that var into a value that can be used elsewhere (from a Literal to
     * a JimpleLiteral).  If given a ActiveThread, we will assume it will be an
     * instance variable instead of a global.
     */
    private Object getVarValue(StateVar var, JimpleStoreBuilder builder, ActiveThread activeThread) {

	Object value = null;
	Type type = var.getType();
	if((type != null) && (!type.isKind(COLLECTION))) {
	    builder.setSpecificThread(activeThread);
	    type.apply(builder, var);
	    builder.setSpecificThread(null);
	    value = builder.getResult();
	}

	return(value);

    }

}
