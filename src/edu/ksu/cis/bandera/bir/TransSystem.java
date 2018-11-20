package edu.ksu.cis.bandera.bir;

import ca.mcgill.sable.soot.SootField;
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
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.spin.SpinUtil;

import org.apache.log4j.Category;


import java.io.*;
import java.util.*;

/**
 * A BIR transition system.
 * <p>
 * This is the top-level object encapsulating a transition system. 
 * It contains:
 * <ul>
 * <li> A name for the system (this is used for filename generation).
 * <li> A set of threads.
 * <li> A set of state variables.
 * <li> A set of definitions.
 * <li> Tables associating BIR entities with external keys
 * (useful when constructing the transition system from
 * another representation).
 * <li> A naming service (for assigning globally unique names).
 * </ul>
 */

public class TransSystem implements BirConstants {
	private static Category log = 
			Category.getInstance(TransSystem.class.getName());



	String name;                // Name of system (for filename generation)
	int threadCount = 0;        // Number of threads
	int locCount = 0;           // Number of locations in all threads
	int stateSize = 0;          // Size of state vector needed

	ThreadVector threadVector = new ThreadVector();        // threads
	StateVarVector stateVarVector = new StateVarVector();  // state vars
	TransVector transVector = new TransVector();           // transformations
	Vector definitionVector = new Vector();                // definitions
	Vector typeVector = new Vector();                      // types
	Vector subtypeVector = new Vector();                   // subtype clauses
	int aggregateCount;

	Hashtable definitionTable = new Hashtable();  // map String --> Definition
	Hashtable locationOfKey = // map Key --> Location
			new Hashtable();
	Hashtable stateVarOfKey = // map Key --> StateVar
			new Hashtable();
	Hashtable sourceMap =  // map BIR object --> Source Object 
			new Hashtable();

	Location [] locationTable;  // map loc number --> Location
	int numberedLocs;           // Numbered locations

	Hashtable nameTable = new Hashtable();   // set of (global) names 
	Hashtable namedKeyTable = new Hashtable();  // map Object --> String (name)
	Hashtable threadTable = new Hashtable();  // map Object --> BirThread

	Hashtable predTable = new Hashtable();    // assoc Predicate <-> Name
	Vector predVector = new Vector();        // list of predicates

	// The set of all collections (used to build the universal ref type).
	StateVarVector refTargets = new StateVarVector();
	Ref refAnyType = new Ref(refTargets,this);

	/**
	 * This is the set of names that cannot be used for declared entities
	 * in a BIR transition system (because they are used by BIR or one
	 * of the translator input languages).
	 */

	static String [] reservedNames = { 
			// Rerserved by BIR
			"array",
			"boolean",
			"choose",
			"collection",
			"do",
			"end",
			"enum",
			"exit",
			"false",
			"goto",
			"hasLock",
			"instanceof",
			"invisible",
			"join",
			"live",
			"loc",
			"lock",
			"lockAvailable",
			"main",
			"new",
			"notify",
			"notifyAll",
			"null",
			"of",
			"predicates",
			"println",
			"process",
			"reentrant",
			"range",
			"record",
			"ref",
			"start",
			"thread",
			"threadTerminated",
			"true",
			"unlock",
			"unwait",
			"wait",
			"wasNotified",
			"when",
			// Rerserved by SPIN
			"active",
			"assert",
			"atomic",
			"bit",
			"bool",
			"break",
			"byte",
			"chan",
			"d_step",
			"Dproctype",
			"do",
			"else",
			"empty",
			"enabled",
			"fi",
			"full",
			"goto",
			"hidden",
			"if",
			"init",
			"int",
			"len",
			"mtype",
			"nempty",
			"never",
			"nfull",
			"od",
			"of",
			"pcvalue",
			"printf",
			"priority",
			"proctype",
			"provided",
			"run",
			"short",
			"skip",
			"timeout",
			"typedef",
			"unless",
			"unsigned",
			"xr",
			"xs",
			"type",
			// Reserved by SMV
			"DEFINE",
			"FAIRNESS",
			"MODULE",
			"SPEC",
			"TRANS",
			"VAR",
			"INIT",
			"next"
	};

	/**
	 * Create a transition system with the given name.
	 */

	public TransSystem(String name) {
		Type.init();
		reserveNames();
		this.name = createName(null, name);
	}
	/**
	 * Add transformation.
	 */
	public void addTrans(Transformation trans) {
		transVector.addElement(trans);
	}
	/**
	 * Create array type
	 */
	public Array arrayType(Type baseType, ConstExpr size) { 
		Array type = new Array(baseType,size);
		type.setTypeId(createName(null,"type"));
		typeVector.addElement(type);
		return type;
	}
	/**
	 * Create boolean type
	 */
	public Bool booleanType() { 
		Bool type = (Bool) Type.booleanType;
		if (type.getTypeId() == null) {
			type.setTypeId(createName(null,"type"));
			typeVector.addElement(type);
		}
		return type;
	}

	/**
	 * Create tid type
	 */
	public Tid tidType() {
		Tid type = (Tid) Type.tidType;
		if (type.getTypeId() == null) {
			type.setTypeId(createName(null,"type"));
			typeVector.addElement(type);
		}
		return type;
	}

	/**
	 * Create collection type
	 */
	public Collection collectionType(Type baseType, ConstExpr size) { 
		Collection type = new Collection(baseType,size);
		type.setTypeId(createName(null,"type"));
		typeVector.addElement(type);
		return type;
	}
	/**
	 * Create a unqiue name for an external key, given a proposed name
	 * @param key key of entity (may be null)
	 * @param proposedName proposed name
	 * @return unique name close to proposed name
	 * <p>
	 * To facilitate translation, we make the names of all declared
	 * entities (variables, threads, definitions) unique using this
	 * service.
	 * <p>
	 * The actual name is constructed from the proposed name by
	 * replacing '$' and '.' (which appear in nested class/package names)
	 * with '_' (yielding a string we call the 'prefix')
	 * and proceding as follows:
	 * <ul>
	 * <li> If the prefix is not yet taken, use it.
	 * <li> Otherwise, if the prefix concatenated to "_0" is
	 * not yet taken, use it.
	 * <li> Continue trying names formed by appending "_1", "_2", etc.
	 * to the prefix until we find a name that is not yet in use.
	 * </ul>
	 */
	public String createName(Object key, String proposedName) {
		log.debug("creating name using key = " + key + " with proposed name = " + proposedName);
		String prefix;
		String name;
		int count = 0;
		boolean staticVar = false;
		
		/* [Thomas, May 23, 2017]
		 * Keep static variable's name unchanged
		 * */
		if(proposedName.startsWith("_static_"))
		{
			prefix = SpinUtil.getOriginalStaticVarName(proposedName);
			staticVar = true;
		}
		else
		{
			prefix = proposedName.replace('$','_').replace('.','_');
		}
		
		name = prefix;
		while (nameTable.get(name) != null)
		{
			/* [Thomas, May 24, 2017]
			 * Keep static variable's name unchanged
			 * */
			if(staticVar || GUtil.isASystemField(name))
			{
				return name;
			}
			name = prefix + "_" + (count++);
		}
		nameTable.put(name,(key != null) ? key : name);
		if (key != null) {
			Object nameOfKey = namedKeyTable.get(key);
			if (nameOfKey != null)
				throw new RuntimeException("Redefinition of name for: " + key);
			namedKeyTable.put(key,name);
		}
		log.debug("created name (" + name + ") using key = " + key + " with proposed name = " + proposedName);
		return name;
	}
	/**
	 * Create a thread.
	 * @param proposedName the proposed name of the thread
	 * @param key external key to associate with thread
	 * @return new thread
	 */
	public BirThread createThread(String proposedName, Object key, String sootClassName) {
		String name = createName(key, proposedName);
		/* [Thomas, May 21, 2017]
		 * Add one more parameter to the function call "new BirThread"
		 * */
		BirThread thread = new BirThread(this, name, threadVector.size(), sootClassName);
		threadVector.addElement(thread);
		threadTable.put(key, thread);
		setSource(thread,key);
		return thread;
	}
	/**
	 * Declare a predicate
	 * @param proposedName the proposed name
	 * @param expr the value of the predicate (and expression on the state)
	 * <p>
	 * Note: if the proposed name is not legal (e.g., is reserved or
	 * already taken), then the name is changed, but the front end
	 * is not informed---this should be fixed at some point.
	 */

	public void declarePredicate(String proposedName, Expr expr) {
		if (getKeyOf(proposedName) != null)
			throw new RuntimeException("Redefinition of " + proposedName);
		String name = createName(proposedName,proposedName);
		predTable.put(name,expr);
		predTable.put(expr,name);
		predVector.addElement(expr);
	}
	/**
	 * Declare a new state variable.
	 * @param key external key associated with state variable
	 * @param proposedName proposed name of variable
	 * @param thread thread containing variable (null if global)
	 * @param type type of variable
	 * @param initVal initial value (if null, uses default for type)
	 * @return new state variable
	 */
	public StateVar declareVar(Object key, String proposedName, 
			BirThread thread, Type type, Expr initVal) {
		Object value = stateVarOfKey.get(key);
		if (value != null)
		{
//			throw new RuntimeException("Variable already declared: " + key);
			/* [Thomas, May 23, 2017]
			 * Replace runtime exception with a print out
			 * */
			System.out.println("Variable already declared: " + key);
			return null;
		}

		String name = createName(key, proposedName);
		StateVar var = new StateVar(name, thread, type, initVal, this);
		
		if(key instanceof SootField)
		{
			var.setIsGlobal();
		}
		else if(!proposedName.startsWith("_static_"))
		{
			var.setIsLocal();
		}

		if (type.isKind(COLLECTION | RECORD | ARRAY))
			refTargets.addElement(var);

		stateVarVector.addElement(var);
		stateVarOfKey.put(key, var);
		setSource(var, key);

		return var;
	}
	/**
	 * Add definition, associate it with a key.
	 */
	public void define(Object key, Definition definition) {
		definitionTable.put(key, definition);
		definitionVector.addElement(definition);
		if (definition instanceof Record)
			((Record) definition).setUnique(aggregateCount ++);
	}

	public int getAggregateCount() { return aggregateCount; }

	/**
	 * Add subtype clause.
	 */
	public void subtype(Record subclass, Record superclass) {
		log.debug("subtype: subclass is " + subclass + ", superclass is " + superclass);
		subtypeVector.addElement(new Subtype(subclass, superclass));
	}

	public boolean isSubtype(Record subclass, Record superclass) {
		if (subclass == superclass)
			return true;

		boolean result = false;
		for (int i = 0; i < subtypeVector.size() && !result; i ++) {
			Subtype pair = (Subtype) subtypeVector.elementAt(i);
			if (pair.getSubclass() == subclass)
				result = isSubtype(pair.getSuperclass(), superclass);
		}
		return result;
	}

	public Record superType(Record type1, Record type2) {
		log.debug("superType: " + type1 + ", " + type2);
		if (type1.isEmpty())
			return type1;

		if (type2.isEmpty())
			return type1;

		if (type1 == type2)
			return type1;

		if (isSubtype(type1, type2)) 
			return type2;

		if (isSubtype(type2, type1)) 
			return type1;

		Record super1 = new Record();
		Record super2 = new Record();
		for (int i = 0; i < subtypeVector.size(); i++) {
			Subtype pair = (Subtype) subtypeVector.elementAt(i);

			if (pair.getSubclass() == type1)
				super1 = pair.getSuperclass();

			if (pair.getSubclass() == type2)
				super2 = pair.getSuperclass();
		}
		return superType(super1, super2);
	}

	/**
	 * Create enumerated type
	 */
	public Enumerated enumeratedType() { 
		Enumerated type = new Enumerated();
		type.setTypeId(createName(null,"type"));
		typeVector.addElement(type);
		return type;
	}
	/**
	 * Get definition associated with key.
	 */
	public Definition getDefinition(Object key) { 
		return (Definition) definitionTable.get(key);
	}

	public Vector getDefinitions() { return definitionVector; }

	public Vector getSubtypes() { return subtypeVector; }

	/**
	 * Get the key associated with a name.
	 */
	public Object getKeyOf(String name) {
		return nameTable.get(name);
	}
	/**
	 * Get the local variables of a thread.
	 */

	public StateVarVector getLocalStateVars(BirThread thread) {
		StateVarVector vars = new StateVarVector();
		for (int i = 0; i < stateVarVector.size(); i++) 
			if (stateVarVector.elementAt(i).getThread() == thread)
				vars.addElement(stateVarVector.elementAt(i));
		return vars;
	}
	public Location getLocation(int id) { 
		if (locationTable == null)
			throw new RuntimeException("Must invoke numberLocations() before getLocation()");
		return locationTable[id];
	}
	// Accessors

	public String getName() {
		log.debug("getting the name of the system: " + name);
		return name;
	}
	/**
	 * Get the name associated with a key.
	 */
	public String getNameOf(Object key) {
		return (String) namedKeyTable.get(key);
	}
	public Vector getPredicates() { return predVector; }
	/**
	 * Get the source of a BIR object.
	 */
	public Object getSource(Object birObject) {
		/*
      if(log.isDebugEnabled()) {
	  log.debug("looking for birObject = " + birObject +
		    ", birObject.hashCode() = " + birObject.hashCode());
	  java.util.Iterator sourceMapIterator = sourceMap.keySet().iterator();
	  while(sourceMapIterator.hasNext()) {
	      Object key = sourceMapIterator.next();
	      Object value = sourceMap.get(key);
	      log.debug("sourceMap key = " + key + " (hashCode = " +
			key.hashCode() + "), value = " + value);
	  }
      }
		 */

		Object source = sourceMap.get(birObject);
		log.debug("source = " + source + " for birObject " + birObject);
		return(source);
	}
	public StateVarVector getStateVars() { return stateVarVector; }
	public ThreadVector getThreads() { return threadVector; }
	/**
	 * Get the set of transformations (all threads).
	 */
	public TransVector getTransformations() { return transVector; }
	public Vector getTypes() { return typeVector; }
	public void addType(Type type) { typeVector.addElement(type); }
	/**
	 * Retrieve state variable associated with key.
	 */
	public StateVar getVarOfKey(Object key) {
		return (StateVar) stateVarOfKey.get(key);
	}

	public void putVarOfKey(Object key, StateVar var) {
		stateVarOfKey.put(key, var);
	}

	/**
	 * Retrieve location associated with key, or create a new
	 * location (if none is found).
	 * @param key external key associated with location
	 * @param thread thread containing location
	 * @return new location or old location associated with key
	 */
	public Location locationOfKey(Object key, BirThread thread) {
		if (key == null) {
			locCount++;
			return new Location(thread);
		}
		Object value = locationOfKey.get(key);
		if (value != null)
			return (Location) value;
		Location loc = new Location(thread);
		locCount++;
		locationOfKey.put(key, loc);
		return loc;
	}
	/**
	 * Create lock type
	 */
	public Lock lockType(boolean waiting, boolean reentrant) { 
		Lock type = new Lock(waiting,reentrant);
		type.setTypeId(createName(null,"type"));
		typeVector.addElement(type);
		return type;
	}
	void numberLoc(Location loc, LocVector threadLocVector) {
		if (loc.getId() < 0) {
			threadLocVector.addElement(loc);
			loc.setId(++numberedLocs);
			locationTable[numberedLocs] = loc;
			for (int i = 0; i < loc.getOutTrans().size(); i++) {
				Transformation trans = (Transformation)
						loc.getOutTrans().elementAt(i);
				numberLoc(trans.getToLoc(), threadLocVector);
			}
		}
	}
	/**
	 * Number the (reachable) locations depth first (chiefly so they'll
	 * print out in a nice order).
	 */
	public void numberLocations() {
		numberedLocs = 0;
		locationTable = new Location[locCount + 1];
		for (int i = 0; i < threadVector.size(); i++) {
			BirThread thread = threadVector.elementAt(i);
			LocVector threadLocVector = new LocVector();
			numberLoc(thread.getStartLoc(), threadLocVector);
			thread.setLocations(threadLocVector);
		}
	}
	/**
	 * Retrieve the name of a predicate given its expression
	 */

	public String predicateName(Object predicate) {
		return (String) predTable.get(predicate);
	}
	/**
	 * Create default range type
	 */
	public Range rangeType() { 
		Range type = (Range) Type.defaultRangeType; 
		type = new Range(type.getFromVal(), type.getToVal());
		int index = typeVector.indexOf(type);
		if (index >= 0)
			return (Range) typeVector.elementAt(index);
		else
			return rangeType(type.getFromVal(), type.getToVal());
	}
	/**
	 * Create specific range type
	 */
	public Range rangeType(ConstExpr lo, ConstExpr hi) { 
		Range type = new Range(lo,hi);
		type.setTypeId(createName(null,"type"));
		typeVector.addElement(type);
		return type;
	}
	/**
	 * Create record type. The insertion of the record type into the
	 * type vector has to be postponed in order to deal with recursive
	 * record type specifications. Use addType(Type type) to finalize.
	 */
	public Record recordType() { 
		Record type = new Record();
		type.setTypeId(createName(null,"type"));
		return type;
	}

	/**
	 * Return ref any type
	 */
	public Ref refAnyType() { 
		return refAnyType;
	}
	/**
	 * Create ref type
	 */
	public Ref refType() { 
		Ref type = new Ref(this);
		type.setTypeId(createName(null,"type"));
		typeVector.addElement(type);
		return type;
	}
	void reserveNames() {
		for (int i = 0; i < reservedNames.length; i++) 
			nameTable.put(reservedNames[i], reservedNames[i]);
	}

	/**
	 * Set the source (an arbitrary object) of a BIR object
	 * <p>
	 * This is used to map a BIR action back to the entity
	 * (e.g., a Jimple statement) from which it was generated.
	 */
	public void setSource(Object birObject, Object source) {
		sourceMap.put(birObject, source);
		log.debug("birObject = " + birObject + " maps to source = " + source);
	}
	/**
	 * Retrieve thread associated with key.
	 */
	public BirThread threadOfKey(Object key) {
		return (BirThread) threadTable.get(key);
	}
}
