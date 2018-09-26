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
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootField;

import edu.ksu.cis.bandera.bir.*;
import edu.ksu.cis.bandera.jext.*;

import java.util.*;

import org.apache.log4j.Category;

/**
 * Jimple type switch that constructs a JimpleStore from a BirState
 * by translating BIR literal values into JimpleLiterals (by type).
 * <p>
 * Most cases are straightforward (we just map the BIR Literal to
 * the equivalent Jimple Literal).  For objects (records an arrays),
 * however, we must collect all their components from the BIR state
 * vector into a ClassLiteral or ArrayLiteral.
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:32:53 $
 */
public class JimpleStoreBuilder extends AbstractTypeSwitch implements BirConstants {

    private static Category log = Category.getInstance(JimpleStoreBuilder.class.getName());

    BirState state;               // BIR state we're working from
    Literal [] birStore;          // Local copy of state vector from BirState
    TransSystem system;

    /**
     * The threadBirStoreMap provides a mapping to store the local bir stores for each
     * thread where the key is the thread and the value is an array of Literal objects.
     */
    private Map threadBirStoreMap;

    /**
     * The specificThread will be null when building the global store but will be set to
     * a specific active thread when building each threads local store.
     */
    private ActiveThread specificThread;
                                 
    /**
     * The object table holds the set of ObjectLiterals that we have
     * already constructed.   We want to translate a given object 
     * only once per state, but we may encounter many references 
     * to the same object, so we check this table before constructing
     * an array or record object literal.
     */
    Hashtable objectTable = new Hashtable(); 


    public JimpleStoreBuilder(BirState state) {
	this.state = state;
	this.birStore = state.getStore();
	threadBirStoreMap = new HashMap();
	// populate this map with the local store for each thread! -tcw
	// for each ActiveThread
	ThreadVector threads = state.getThreads();
	if(threads != null) {
	    for(int i = 0; i < threads.size(); i++) {
		ActiveThread activeThread = (ActiveThread)threads.elementAt(i);  // dangerous cast!
		Literal[] threadBirStore = activeThread.getStore();
		Integer threadID = new Integer(activeThread.getTid());
		threadBirStoreMap.put(threadID, threadBirStore);
		log.debug("adding a new birStore for thread " + threadID.toString());
	    }
	}

	if(log.isDebugEnabled()) {
	    if(birStore != null) {
		log.debug("birStore.length = " + birStore.length);
		for(int i = 0; i < birStore.length; i++) {
		    log.debug("birStore[" + i + "] = " + birStore[i]);
		    if(birStore[i] != null) {
			log.debug("birStore[" + i + "].getClass().getName() = " + birStore[i].getClass().getName());
		    }
		}
	    }
	    if(threadBirStoreMap != null) {
		log.debug("threadBirStoreMap.size() = " + threadBirStoreMap.size());
		Iterator threadIDIterator = threadBirStoreMap.keySet().iterator();
		while(threadIDIterator.hasNext()) {
		    Integer threadID = (Integer)threadIDIterator.next();
		    Literal[] birStore = (Literal[])threadBirStoreMap.get(threadID);
		    for(int i = 0; i < birStore.length; i++) {
			log.debug("birStore[" + threadID + "][" + i + "] = " + birStore[i]);
			if(birStore[i] != null) {
			    log.debug("birStore[" + threadID + "][" + i +
				      "].getClass().getName() = " + birStore[i].getClass().getName());
			}
		    }
		}
	    }
	}

	this.system = state.getSystem();
    }

    /**
     * Set the specific thread that we will be constructing the local store for.  If this
     * is set to null the global store will be constructed.
     *
     * @param ActiveThread specificThread The thread to construct the local store for.
     */
    public void setSpecificThread(ActiveThread specificThread) {
	this.specificThread = specificThread;
    }

    /**
     * Get the specific thread whose store will be constructed.
     */
    public ActiveThread getSpecificThread() {
	return(specificThread);
    }


    public void caseArray(Array type, Object o)
    {
	// See if we've already translated this array 
	// (o = Integer holding the address of the array)
	Object alreadyComputed = objectTable.get(o);
	if (alreadyComputed != null) {
	    // If so, return the result of the previous translation
	    //setResult((ArrayLiteral)alreadyComputed);
	    setResult(alreadyComputed);
	    return;
	}
	// Otherwise, collect all the array elements into an ArrayLiteral
	int arrayAddr = getVarOffset(o);
	Object varValue = getVarValue(o);
	if((varValue != null) && (varValue instanceof IntLit)) {
	    int length = ((IntLit)getVarValue(o)).getValue();
	    ArrayLiteral array = new ArrayLiteral(length,arrayAddr);
	    // Store this literal so we won't translate this array again
	    objectTable.put(o, array);
	    for (int i = 0; i < length; i++) {
		int address = arrayAddr + 1 + i;
		// Translate the ith array element
		type.getBaseType().apply(this, new Integer(address));
		array.contents[i] = (JimpleLiteral) getResult();
	    }
	    setResult(array);
	}
	else {
	    if(varValue == null) {
		log.error("While getting the length of the array, we got a null.");
	    }
	    else {
		log.error("While getting the length of the array, we got something besides an IntLit: " +
			  varValue.getClass().getName());
	    }
	    throw new RuntimeException("An error occured while getting the length of an array.");
	}
    }

    public void caseBool(Bool type, Object o) {
	// robbyjo's patch begin
	Literal ll = getVarValue(o);
	boolean value = false;
	if (ll instanceof BoolLit)
	    value = ((BoolLit) ll).getValue();
	else if (ll instanceof IntLit)
	    value = ((IntLit) ll).getValue() == 0;

	// robbyjo's patch end
	setResult(new BooleanLiteral(value));
    }

    public void caseLock(Lock type, Object o) {
	log.debug("o = " + o + " must be a lock.");
	if(o != null) {
	    log.debug("o.getClass().getName() = " + o.getClass().getName());
	}

	Object value = getVarValue(o);

	/*
	 * This is a HACK to get the ClassicDeadlock example working correctly.  If the
	 * value that we get back is not in the local state we will try to check the global
	 * state for the lock information.  There should be a better way to do this! -tcw
	 *
	 * One possible solution is to have a method like getVarValue that takes the type that
	 * we are expecting to get back:
	 * getVarValue(o, LockLit.class)
	 * where this method would first query the local for the thread specified (specificThread)
	 * and if that fails it will query the global state.  -tcw
	 */
	if(!(value instanceof LockLit)) {
	    ActiveThread tempBirThread = specificThread;
	    specificThread = null;
	    value = getVarValue(o);
	    specificThread = tempBirThread;
	}
	/* END HACK */

	if(value instanceof LockLit) {
	    LockLit lock = (LockLit)value;
	    setResult(new LockLiteral(lock));
	}
	else if(value instanceof NullExpr) {
	    setResult(null);
	}
	else {
	    if(value == null) {
		log.warn("When expecting a LockLit, got a null value.  Returning a null result.");
	    }
	    else {
		log.warn("When expecting a LockLit, got a value of type " + value.getClass().getName() + ".  Returning a null result.");
	    }
	    setResult(null);
	}
    }
    public void caseRange(Range type, Object o) 
    {
	if(o == null) {
	    log.error("o is null.");
	    return;
	}
	if(type == null) {
	    log.error("type is null.");
	    return;
	}
	    
	Object valueObject = getVarValue(o);
	if(valueObject == null) {
	    log.error("valueObject is null for variable " + o);
	    return;
	}
	int value = 0;
	if(valueObject instanceof IntLit) {
	    IntLit valueIntLit = (IntLit)valueObject;
	    value = valueIntLit.getValue();
	}
	else {
	    log.error("value for variable " + o + " is not an IntLit (it is a " +
		      valueObject.getClass().getName() + ").  Returning 0 instead.");
	    value = 0;
	    /*
	      Exception e = Exception("Just trying to get a call stack!");
	      e.printStackTrace(System.err);
	      }
	    */
	}

	//int value = ((IntLit)getVarValue(o)).getValue();
	//setResult(new IntegerLiteral(value));
	setResult(new IntegerLiteral(value));
    }
    public void caseRecord(Record type, Object o)
    {
	// See if we've already translated this record 
	// (o = Integer holding the address of the record)
	Object alreadyComputed = objectTable.get(o);
	if (alreadyComputed != null) {
	    // If so, return result of previous translation
	    setResult((ClassLiteral)alreadyComputed);
	    return;
	}
	// Otherwise, collect all the fields into a ClassLiteral
	int recAddr = getVarOffset(o);
	ClassLiteral classLit = 
	    new ClassLiteral((SootClass)system.getSource(type),recAddr);
	// Store this literal so we won't translate this record again
	objectTable.put(o, classLit);
	Vector fields = type.getFields();
	for (int i = 0; i < fields.size(); i++) {
	    Field field = (Field) fields.elementAt(i);
	    log.debug("parsing field: " + field);
	    SootField sField = (SootField) system.getSource(field);
	    int address = recAddr + field.getOffset();
	    // Translate field value
	    Type fieldType = field.getType();
	    fieldType.apply(this, new Integer(address));
	    JimpleLiteral value = (JimpleLiteral)getResult();
	    // ignore the value for the thread ID! -tcw
	    if(!(fieldType instanceof Tid)) {
		// The BIRLock field is special---it has no source
		if (sField != null) {
		    classLit.setFieldValue(sField,value);
		}
		else {
		    classLit.setLockValue(value);
		}
	    }
	    else {
		log.debug("This field is a thread ID which is being ignored.  The value found is: " + value);
		if(value != null) {
		    log.debug("The value's type is : " + value.getClass().getName());
		}
	    }
	}
	setResult(classLit);
    }

    public void caseTid(Tid tid, Object o) {
	log.debug("found a ThreadID ...");
	Object value = getVarValue(o);
	log.debug("the value associated with this tid is: " + value);
	if(value instanceof IntLit) {
	    IntLit intLit = (IntLit)value;
	    setResult(new IntegerLiteral(intLit.getValue()));
	}
	else {
	    setResult(new IntegerLiteral(-1));
	}
    }

    public void caseRef(Ref type, Object o) {
	log.debug("object o = " + o);
	Literal value = getVarValue(o);
	if ((value != null) && (value instanceof RefLit)) {
	    // Translate the target pointed to and then return
	    // a ReferenceLiteral pointing to this value
	    RefLit ref = (RefLit)value;

	    int address = ref.getCollection().getOffset();

	    if (ref.getCollection().getType().isKind(COLLECTION)) 
		address += ref.getIndex() * type.getTargetType().getExtent();

	    Type targetType = ref.getTargetType();
	    targetType.apply(this, new Integer(address));
	    setResult(new ReferenceLiteral((JimpleLiteral)getResult()));
	}
	else if (value instanceof NullExpr) {
	    setResult(new ReferenceLiteral(null));
	}
	/*
	  else if (value instanceof LockLit) {
	  log.warn("When expecting a RefLit in caseRef, we got a LockLit instead ... rolling with it by creating a LockLiteral.");
	  LockLit lock = (LockLit)value;
	  setResult(new LockLiteral(lock));
	  }
	  else if (value instanceof IntLit) {
	  log.warn("When expecting a RefLit in caseRef, we got a IntLit instead ... rolling with it by creating an IntegerLiteral.");
	  IntLit valueIntLit = (IntLit)value;
	  setResult(new IntegerLiteral(valueIntLit.getValue()));
	  }
	*/
	else {
	    if(value != null) {
		log.error("when expecting a RefLit, got a " + value.getClass().getName() + " instead.");
		log.error("with an o = " + o + ", we got a value = " + value);
	    }
	    else {
		log.error("when expecting a RefLit, got a null value instead that is not NullExpr as would be expected.");
	    }
	    setResult(new ReferenceLiteral(null));
	}
    }

    public void defaultCase(Object obj) 
    {
	throw new RuntimeException("Case not handled: " + obj);
    }

    /**
     * Get offset of context in state vector.
     * <p>
     * The context is either an Integer (address) or StateVar.
     */
    int getVarOffset(Object context) {
	if(context == null) {
	    log.error("context is null.");
	    return(-1);
	}		    

	if(context instanceof Integer) {
	    return ((Integer)context).intValue();
	}
	else if(context instanceof StateVar) {
	    return ((StateVar)context).getOffset();
	}
	else {
	    log.error("When expecting an Integer or a StateVar, got a " + context.getClass().getName() + ".");
	    return(-1);
	}
    }

    /**
     * Get the value of a context in the state vector.
     */
    Literal getVarValue(Object context) {

	if(birStore == null) {
	    log.error("birStore is null.");
	    return(null);
	}
	if(context == null) {
	    log.error("context is null.");
	    return(null);
	}

	int offset = getVarOffset(context);
	log.debug("offset for context " + context + " = " + offset);

	Literal[] store = null;
	if(specificThread == null) {
	    log.debug("Query the global store ...");
	    store = birStore;
	}
	else {
	    log.debug("Query the local store for thread " + specificThread.getTid() + " with id " + specificThread.getId() + "...");
	    Integer threadID = new Integer(specificThread.getTid());
	    store = (Literal[])threadBirStoreMap.get(threadID);
	}

	if((store != null) && (offset >= 0) && (offset < store.length)) {
	    return store[offset];
	}
	else {
	    log.error("The store is null for this thread or the offset for context " +
		      context + " is invalid: " + offset + ".  Returning null.");
	    return(null);
	}
    }
}
