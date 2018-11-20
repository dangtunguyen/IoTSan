package edu.ksu.cis.bandera.birc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2002   James Corbett (corbett@hawaii.edu)      *
 *                           Matthew Dwyer (dwyer@hawaii.edu)        *
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
 * General Public License for more details.                          *
 *                                                                   *
 * You should have received a copy of the GNU General Public         *
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
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;

import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.bir.BirThread;
import edu.ksu.cis.bandera.bir.BirConstants;
import edu.ksu.cis.bandera.bir.LockAction;
import edu.ksu.cis.bandera.bir.LockTest;
import edu.ksu.cis.bandera.bir.ThreadLocTest;
import edu.ksu.cis.bandera.bir.Location;
import edu.ksu.cis.bandera.bir.LocVector;
import edu.ksu.cis.bandera.bir.StateVar;
import edu.ksu.cis.bandera.bir.Transformation;
import edu.ksu.cis.bandera.bir.ActionVector;
import edu.ksu.cis.bandera.bir.TransVector;
import edu.ksu.cis.bandera.bir.BirTrace;
import edu.ksu.cis.bandera.prog.IdentifyRunnableClasses;
//import edu.ksu.cis.bandera.syncgen.code.BirTransformer;

import org.apache.log4j.Category;

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.spin.SpinUtil;

/**
 * Builder is the main class of BIRC, which extracts a transition
 * system from a restricted Jimple representation of a Java program.
 * <p>
 * See the <a href="http://www.cis.ksu.edu/bandera/beer.html">BIR
 * Pages</a> for information on what subset of Jimple is
 * currently supported by BIRC.
 * <p>
 * BIRC is invoked on an array of Soot classes as follows:
 * <pre>
 *  // Parameters
 *  SootClass [] staticClasses = ...;  // An array of SootClass 
 *                                     // for threads/runnable objects
 *  String name = ...;                 // Name of system (used for filenames)
 *  PredicateSet preds = ...;          // Predicates to embed
 *  ResourceBounds bounds = ...;       // Bounds set from defaults or by user
 *  BirTransformer syncGenTrans = ...; // SyncGen to BIR transformer
 * 
 *  // Invoke BIRC to build the transition system (BIR)
 *  TransSystem system = Builder.createTransSystem(staticClasses, name, preds,
 *					  	   bounds, syncGenTrans);
 * 
 *  // Print the BIR
 *  PrintWriter writerOut = new PrintWriter(System.out, true);
 *  BirPrinter.print(system, writerOut);
 * </pre>
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Matt Dwyer &lt;dwyer@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:32:52 $
 */
public class Builder implements BirConstants {

	private static Category log = Category.getInstance(Builder.class);

	public static ResourceBounds bounds;
	//	public static BirTransformer syncGenTransformer;

	BirThread thread;     // Current thread being built
	StmtGraph stmtGraph;  // Statement graph of current thread
	SootMethod method;    // Method whose body is current thread
	int mark;             // Mark value for depth-first search of Stmt graph
	Vector reachableStmts; // Set of reachable Stmts in Stmt graph

	static TransSystem system;        // Transition system being built
	static TypeExtractor typeExtract; // Type extractor we're using
	static DynamicMap dynamicMap;     // Dynamic map we're using
	static HashSet dynamicClasses;
	static HashSet threadClasses;     // Subtypes of "java.lang.Thread" 
	static Hashtable threadSubTypes;  // Record allocated subtypes of thread types
	static HashSet lockedClasses;     // Classes that are locked somewhere in the program
	static HashSet parentClasses;     // Supertypes of dynamically allocated classes 
	static Hashtable extendsRelation; // Individual inheritence relationships among allocated classes and their ancestors

	private Builder(BirThread thread, StmtGraph stmtGraph, SootMethod method) {
		this.thread = thread;
		this.stmtGraph = stmtGraph;
		this.method = method;
	}
	/**
	 * Build a BIR thread from the run() method of a Jimple class.
	 */
	static void buildThread(SootClass sClass, PredicateSet predSet) {
		boolean mainClass; 

		// check if this is a class for "java.lang.Thread" and if so,
		// create a dummy BIR thread
		if (sClass.getName().equals("java.lang.Thread")) {
			SootMethod method = sClass.getMethod("run", new ArrayList(), VoidType.v());
			BirThread jlt = system.threadOfKey(method);
			//BirThread jlt = system.threadOfKey(sClass);
			Location l = system.locationOfKey(null, jlt);
			jlt.setStartLoc(l);
			log.debug("Building java.lang.Thread thread");
			return;
		}

		// determine whether class declares a: 
		//   static public void main(String[] args)
		// method, and if so consider it the main entry point, if not
		// look for a method:
		//   void run() 
		// method. 
		//	        ca.mcgill.sable.util.LinkedList args = 
		//		    new ca.mcgill.sable.util.LinkedList();
		/* [Thomas, April 29, 2017]
		 * We will accept main function with the following format:
		 * public void main() {}
		 * */
		//        args.add(ca.mcgill.sable.soot.ArrayType.v(RefType.v("java.lang.String"), 1));
		//		mainClass = sClass.declaresMethod("main", args, VoidType.v());
		mainClass = SpinUtil.containEvtHandlerMethod(sClass);
		if (mainClass) {
			for(SootMethod method : SpinUtil.getEvtHandlerMethods(sClass))
			{
				BirThread thread = system.threadOfKey(method);

				// Get statement graph for method body
				JimpleBody body = (JimpleBody) 
						new StoredBody(Jimple.v()).resolveFor(method);
				StmtList stmtList = body.getStmtList();
				if (stmtList.size() == 0)
					return;
				CompleteStmtGraph cStmtGraph = new CompleteStmtGraph(stmtList);
				LocalDefs localDefs = new SimpleLocalDefs(cStmtGraph);
				LiveLocals liveLocals = new SparseLiveLocals(cStmtGraph);
				BriefStmtGraph stmtGraph = new BriefStmtGraph(stmtList);

				// Set start location and main
				Stmt head = (Stmt) stmtList.get(0);
				Location startLoc = system.locationOfKey(head, thread);
				log.debug("Builder: thread " + thread + " has start " + startLoc);
				thread.setStartLoc(startLoc);
				thread.setMain(mainClass);

				//	    if (!mainClass) {
				//		    /** 
				//		     * Declare a 'this' variable for each thread except the main
				//		     * thread to hold the reference to the Runnable or Thread object 
				//		     * that owns the thread's run() method.  It is a parameter to 
				//		     * the thread.
				//		     */
				//		    String thisKey = sClass.getName() + "_this";
				//		    StateVar threadThis = declareVar(thisKey, "this", 
				//						     RefType.v(sClass.getName()), thread);
				//		    if (threadThis != null) {       
				//				log.debug("declaring thread parameter " + sClass.getName() + "_this");
				//				thread.addParameter(threadThis);
				//		    } else {
				//		    		log.debug("could not declare this for type " + sClass.getName());
				//		    }
				//	    }

				// Make state vars for locals
				Iterator localIt = body.getLocals().iterator();
				while (localIt.hasNext()) {
					Local local = (Local) localIt.next();
					String key = ExprExtractor.localKey(method, local);
					log.debug("declaring local " + local.getName() + " in thread " + sClass.getName());
					StateVar theLocal = declareVar(key, local.getName(), local.getType(), thread);
					if (theLocal != null) {
						log.debug("declaring local " + theLocal);
						thread.addLocal(theLocal);
					} else {
						log.debug("skipping declaration of local " + theLocal);
					}
				}

				// Collect reachable statements by walking stmtGraph
				Builder builder = new Builder(thread, stmtGraph, method);
				Vector reachableStmts = builder.reachableFrom(head);

				// Extract BIR from each reachable statement
				TransExtractor extractor = 
						new TransExtractor(system, thread, stmtGraph, localDefs, 
								liveLocals, method, typeExtract, predSet);
				//	    new TransExtractor(system, thread, stmtGraph, localDefs, 
				//			       liveLocals, method, typeExtract, predSet, syncGenTransformer);
				for (int i = 0; i < reachableStmts.size(); i++) 
					((Stmt)reachableStmts.elementAt(i)).apply(extractor);
			}
		}
	}
	/**
	 * Check for statics on the left and right side of a definition Stmt.
	 * If found, and the class of the static has not already been processed,
	 * declare a new BIR global variable for the static fields of the class.
	 */

	static void checkForStatics(DefinitionStmt defStmt, Hashtable done) {
		if (defStmt.getRightOp() instanceof StaticFieldRef) {
			StaticFieldRef fieldRef = (StaticFieldRef)defStmt.getRightOp();
			SootClass sClass = fieldRef.getField().getDeclaringClass();
			if (done.get(sClass) == null) {
				declareClassGlobals(sClass);
				done.put(sClass,sClass);
			}
		}
		if (defStmt.getLeftOp() instanceof StaticFieldRef) {
			StaticFieldRef fieldRef = (StaticFieldRef)defStmt.getLeftOp();
			SootClass sClass = fieldRef.getField().getDeclaringClass();
			if (done.get(sClass) == null) {
				declareClassGlobals(sClass);
				done.put(sClass,sClass);
			}
		}
	}
	/**
	 * Given the dynamic map, create a collection for each collection key
	 * that was declared in the map.  The size of a collection is the
	 * sum of the sizes of all map entries stored in that collection.
	 */

	static void createCollections() {
		// For each collection key K of class C, 
		// create K_col = collection [N] of C, where N is the sum
		// of the sizes of all allocators with key K.
		// Also, add K_col to C_ref.  
		Vector keys = dynamicMap.getCollections();
		for (int i = 0; i < keys.size(); i++) {
			String name;
			edu.ksu.cis.bandera.bir.Type elementType;
			edu.ksu.cis.bandera.bir.Type collectType;

			// First, create the collection type and variable
			Object key = keys.elementAt(i);
			int size = dynamicMap.getCollectionSize(key);
			SootClass sootClass = dynamicMap.getCollectionClass(key);
			log.debug("class " + sootClass + " has collection size " + size);
			ArrayType arrayType = dynamicMap.getCollectionArray(key);
			// key is either class collection or array collection
			if (sootClass != null) {
				elementType = typeExtract.getRecordType(sootClass);
				name = sootClass.getName();
			} else {
				elementType = typeExtract.getArrayType(arrayType);
				name = typeExtract.arrayName(arrayType);
			}
			collectType = typeExtract.createCollectionType(elementType,size);
			StateVar col = 
					system.declareVar(key,name + "_col",null,collectType,null);
			
			/* [Thomas, May 23, 2017]
			 * Add null check
			 * */
			if(col != null)
			{
				// Now add the collection to the ref types that could refer to it
				if (sootClass != null) {
					// A class collection can be referenced by the ref type
					// for that class, all superclasses, and any 
					// implemented interfaces
	
					typeExtract.getRefType(sootClass).addTarget(col);
					Vector superClasses = typeExtract.getSuperClasses(sootClass);
					for (int j = 0; j < superClasses.size(); j++) {
						SootClass supClass = (SootClass) superClasses.elementAt(j);
						log.debug("createCollections: " + supClass + " can refer to " + col);
						typeExtract.getRefType(supClass).addTarget(col);
					}
					Vector intClasses = typeExtract.getInterfaceClasses(sootClass);
					for (int j = 0; j < intClasses.size(); j++) {
						SootClass intClass = (SootClass) intClasses.elementAt(j);
						typeExtract.getRefType(intClass).addTarget(col);
					}
				} else
					// An array collection can be referenced only by the
					// ref type of the array
					typeExtract.getRefType(arrayType).addTarget(col);
			}
		}	
	}
	/**
	 * Given the information collected about inheritence for supertypes
	 * of potentially allocated types.  Create the extends relation
	 * in the transition system
	 */
	static void createExtends() {
		for (java.util.Enumeration subs = extendsRelation.keys(); 
				subs.hasMoreElements() ;) {
			SootClass child = (SootClass)subs.nextElement();
			SootClass parent = (SootClass)extendsRelation.get(child);
			log.debug("createExtends: child is " + child + ", parent is " + parent); 
			system.subtype(typeExtract.getRecordType(child),
					typeExtract.getRecordType(parent));
		}
	}
	/**
	 * For each Jimple class and array type, create a corresponding
	 * BIR record/array type using the type extractor.
	 */

	static void createRecordAndArrayTypes() {
		// Record the supertypes of dynamically allocated classes
		// also determine which of those are locked
		Vector sootClasses = dynamicMap.getClasses();
		SootClass runnableClass = null;  
		for (int i = 0; i < sootClasses.size(); i++) {
			SootClass sootClass = (SootClass) sootClasses.elementAt(i);
			log.debug("Analyzing class " + sootClass.getName() + " to see if it is Runnable.");

			SootClass ancestor = sootClass;
			while (ancestor.hasSuperClass()) {
				log.debug("The ancestor, " + ancestor.getName() + " has a super class.");

				if (IdentifyRunnableClasses.implementsInterface(ancestor,"java.lang.Runnable")) {
					runnableClass = ancestor;
				}

				ancestor = ancestor.getSuperClass();
				if (lockedClasses.contains(ancestor)) {
					log.debug("Adding class " + sootClass.getName() + " to the list of locked classes.");
					lockedClasses.add(sootClass);
				}
			}
		}

		// Create a record for runnable instance if necessary
		if (runnableClass != null) {
			typeExtract.createRecordType(runnableClass.getManager().getClass("java.lang.Runnable"), false, true);
		}

		// For each class C, create C_rec = record { ... }
		// 
		// There are two cases:
		//   1) the dynamically allocated classes which should be
		//      created with an indication of whether instances may be locked
		//   2) the unallocated classes that are locked, i.e., that
		//      may be supertypes of dynamically allocated classes
		//

		// First handle all the dynamically allocated classes
		sootClasses = dynamicMap.getClasses();
		for (int i = 0; i < sootClasses.size(); i++) {
			SootClass sootClass = (SootClass) sootClasses.elementAt(i);
			log.debug("createRecordTypes: dynamic class " + sootClass);
			typeExtract.createRecordType(sootClass, 
					lockedClasses.contains(sootClass),
					sootClass.getName().equals("java.lang.Thread") || 
					threadClasses.contains(sootClass));
		}

		// Second handle all the unallocated locked classes
		for (Iterator pIt = parentClasses.iterator(); pIt.hasNext(); ) {
			SootClass sootClass = (SootClass) pIt.next();
			log.debug("createRecordTypes: locked class " + sootClass);
			typeExtract.createRecordType(sootClass, 
					lockedClasses.contains(sootClass),
					sootClass.getName().equals("java.lang.Thread") || 
					threadClasses.contains(sootClass));
		}

		// For each array type T, create T_arr = array [K] of T
		Vector arrayTypes = dynamicMap.getArrays();
		for (int i = 0; i < arrayTypes.size(); i++) {
			ArrayType arrayType = (ArrayType) arrayTypes.elementAt(i);
			typeExtract.createArrayType(arrayType,bounds.getArrayMax());
		}
	}
	/**
	 * Once the dynamic map has been constructed, create a reference
	 * type for each class, array type, and interface.  These types
	 * will be used to represent the Java reference types of the Jimple.
	 * <p>
	 * Note that, at this point, we have not yet created the collections,
	 * the the reference types are unresolved.
	 */
	static void createRefTypes() {
		// For each class C, create C_ref = ref {} (unresolved)
		Vector sootClasses = dynamicMap.getClasses();
		for (int i = 0; i < sootClasses.size(); i++) {
			SootClass sootClass = (SootClass) sootClasses.elementAt(i);
			typeExtract.createRefType(sootClass);
		}

		// For each parent of an allocated class create a ref
		for (Iterator pIt = parentClasses.iterator(); pIt.hasNext(); ) {
			SootClass sootClass = (SootClass) pIt.next();
			typeExtract.createRefType(sootClass);
		}

		// For each array type T, create T_ref = ref {} (unresolved)
		Vector arrayTypes = dynamicMap.getArrays();
		for (int i = 0; i < arrayTypes.size(); i++) {
			ArrayType arrayType = (ArrayType) arrayTypes.elementAt(i);
			typeExtract.createRefType(arrayType);
		}

		// For each interface I, create I_ref = ref {} (unresolved)
		Vector interfaces = dynamicMap.getInterfaces();
		for (int i = 0; i < interfaces.size(); i++) {
			SootClass sootClass = (SootClass) interfaces.elementAt(i);
			typeExtract.createRefType(sootClass);
		}
	}

	/**
	 * Creates a transition system from a set of Soot classes.
	 *
	 * @param sootClasses an array of SootClass objects containing
	 *  methods that represent the code executed by threads.
	 *  The first class must have a static method <tt>main()</tt>
	 *  and the remaining classes must each have a static method
	 *  <tt>run()</tt>.  This assumes that any inherited <tt>run()</tt>
	 *  methods have been copied into the elements of this array parameter.
	 * @param name name of transition system (used to generate filenames)
	 * @param predSet set of predicates to embed (can be null)
	 * @param bounds the resource bounds object for this system (can be null)
	 * @param bt the SyncGen to bir transformer (can be null)
	 * @return a transition system representing the Soot classes
	 */
	public static TransSystem createTransSystem(SootClass [] sootClasses,
			String name, 
			PredicateSet predSet,
			ResourceBounds theBounds) 
	{
		//BirTransformer bt) {

		// if there are no bounds given (bounds == null), use the default bounds
		if (theBounds != null) {
			bounds = theBounds;
		} else {
			bounds = new ResourceBounds();
		} 

		// Override the static type definitions for bir with the
		// given resource bounds
		edu.ksu.cis.bandera.bir.Type.defaultRangeType = 
				new edu.ksu.cis.bandera.bir.Range(bounds.getIntMin(), 
						bounds.getIntMax());
		edu.ksu.cis.bandera.bir.Type.tidType = 
				new edu.ksu.cis.bandera.bir.Tid(bounds.getDefaultThreadMax());

		//	syncGenTransformer = bt;
		if (predSet == null) 
			predSet = new PredicateSet();   // Create empty set so non-null
		system = new TransSystem(name);
		typeExtract = new TypeExtractor(system, sootClasses[0].getManager());

		// Declare classes, threads, and global variables
		declareClasses(sootClasses);

		for (int i = 0; i < sootClasses.length; i++) {
			declareThread(sootClasses[i]);
		}

		// If an instance of java.lang.Thread is referenced declare
		// "dummy" thread
		if (dynamicClasses.contains(sootClasses[0].getManager().getClass("java.lang.Thread"))) {
			declareThread(sootClasses[0].getManager().getClass("java.lang.Thread"));
		}

		declareGlobals(sootClasses);

		// Generate the synchronization state variables
		/* SyncGen code
		   if (syncGenTransformer != null) {
		   syncGenTransformer.buildSyncGenGlobals(system);
		   }
		 */

		// Build all threads
		for (int i = 0; i < sootClasses.length; i++) {
			buildThread(sootClasses[i], predSet);
		}


		// If an instance of java.lang.Thread is referenced declare
		// "dummy" thread
		if (dynamicClasses.contains(sootClasses[0].getManager().getClass("java.lang.Thread"))) {
			buildThread(sootClasses[0].getManager().getClass("java.lang.Thread"), predSet);
		}

		// Reduce the transition system and number the locations
		//(new Reducer(system)).run();
		system.numberLocations();

		// Declare the predicates
		log.debug("Declaring predicates ...");
		declarePredicates(predSet);
		log.debug("Finished declaring predicates.");

		return system;
	}
	/**
	 * Analyze the classes to identify those for which instances
	 * may be allocated and those for which locks may be acquired
	 * (in doing this record the supertypes of the allocated classes)
	 * <p>
	 * BIRC tries to optimize the extracted model by ignoring classes
	 * that do not have allocated instances or have subtypes that
	 * have allocated instances.   Furthermore, the BIR types will
	 * only contain locks fields for classes that have monitor statements 
	 * in the program.
	 * <ul>
	 * <li> Create references in BIR for all of those classes
	 * <li> Create records for all of those classes (only include a lock
	 *      for ones that have monitor calls)
	 * <li> Create collections only for those that are actually allocated
	 * <li> Create inheritence relationships for all of those classes.
	 * </ul>
	 * Note: we ignore the built-in classes in any subpackage of java.*
	 * except for Object which is the root of every hierarchy.
	 */
	static void declareClasses(SootClass [] sootClasses) {
		// Identify thread/runnable classes to enable
		// proper creation of BIR records
		threadClasses = new HashSet();	
		for (int i = 0; i < sootClasses.length; i++) {
			threadClasses.add(sootClasses[i]);
			log.debug("declareClasses: adding thread class "+ sootClasses[i].getName());
		}

		threadSubTypes = new Hashtable();	
		dynamicClasses = new HashSet();
		identifyAllocatorsLocks(sootClasses);
		createRefTypes();
		createRecordAndArrayTypes();
		createExtends();
		createCollections();
	}
	/**
	 * Declare a BIR variable for each static field of a class.
	 * <p>
	 * Note: we ignore the built-in classes in any subpackage of java.*
	 * except for java.lang.Thread and java.lang.Object
	 */
	static void declareClassGlobals(SootClass sClass) {
		// Ignore built-in classes
		if (sClass.getName().startsWith("java.") && 
				!(sClass.getName().startsWith("java.lang.Object") ||
						sClass.getName().startsWith("java.lang.Thread")) )
			return;

		// Declare state variable for each static field
		Iterator fieldIt = sClass.getFields().iterator();        
		while(fieldIt.hasNext()) {
			SootField field = (SootField) fieldIt.next();
			if (Modifier.isStatic(field.getModifiers())) 
				declareVar(field, field.getName(), field.getType(), null);
		}

		// Scan the <clinit> method for initializations to static fields
		if (sClass.declaresMethod("<clinit>")) {
			SootMethod method = sClass.getMethod("<clinit>");
			JimpleBody body = (JimpleBody) 
					new StoredBody(Jimple.v()).resolveFor(method);
			StmtList stmtList = body.getStmtList();
			for (int i = 0; i < stmtList.size(); i++) {
				if (stmtList.get(i) instanceof AssignStmt) {
					AssignStmt stmt = (AssignStmt) stmtList.get(i);
					if (stmt.getLeftOp() instanceof StaticFieldRef) {
						StaticFieldRef lhs = (StaticFieldRef)stmt.getLeftOp();
						if (lhs.getType() instanceof RefType)
							log.error("ignoring initialization of " + lhs);
						else
							initializeGlobal(lhs, stmt.getRightOp());
					}
				}
			}
		}
	}
	/**
	 * Declare global BIR variables for all Jimple static variables
	 * referenced in the body of some thread.
	 */

	static void declareGlobals(SootClass [] sootClasses) {
		Hashtable done = new Hashtable();  // Classes we've already processed
		
		for (int i = 0; i < sootClasses.length; i++) {
			SootClass sClass = sootClasses[i];
			
			/* [Thomas, June 18, 2017]
			 * */
			if (done.get(sClass) == null) {
				declareClassGlobals(sClass);
				done.put(sClass,sClass);
			}
//			log.debug("declaring globals for class " + sClass.getName().toString());
//			List methods = sootClasses[i].getMethods();
//			Iterator methodIt = methods.iterator();
//			while (methodIt.hasNext()) {
//				SootMethod method = (SootMethod) methodIt.next();
//				log.debug("declaring globals for method " + method.getName().toString());
//				JimpleBody body = (JimpleBody) 
//						new StoredBody(Jimple.v()).resolveFor(method);
//				StmtList stmtList = body.getStmtList();
//				for (int j = 0; j < stmtList.size(); j++) {
//					Stmt stmt = (Stmt) stmtList.get(j);
//					if (stmt instanceof DefinitionStmt) {
//						log.debug("declaring globals for definition stmt " + stmt.toString());
//						checkForStatics((DefinitionStmt)stmt,done);
//					}
//				}
//			}
		}
	}
	/**
	 * For each predicate declared, use an expression extractor 
	 * to translate the Jimple Expr for the predicate into a BIR Expr,
	 * then declare that as the BIR predciate.
	 */
	static void declarePredicates(PredicateSet predSet) {
		Vector valPreds = predSet.getValuePredicates();
		for (int i = 0; i < valPreds.size(); i++) {
			Expr valPred = (Expr) valPreds.elementAt(i);

			log.debug("Starting to use the SpecExprExtractor.");
			SpecExprExtractor extractor = 
					new SpecExprExtractor(system, null, null, null, typeExtract, predSet);
			valPred.apply(extractor);
			edu.ksu.cis.bandera.bir.Expr predExpr = 
					(edu.ksu.cis.bandera.bir.Expr) extractor.getResult();
			log.debug("Finished using the SpecExprExtractor.");

			system.declarePredicate(predSet.predicateName(valPred),predExpr);
		}
	}
	/**
	 * Declare threads for classes with a "main" method and for those
	 * runnable classes for which an allocator is present in the program.
	 */
	static void declareThread(SootClass sClass) {
		log.debug("declaring thread for class " + sClass);
		ca.mcgill.sable.util.LinkedList args = 
				new ca.mcgill.sable.util.LinkedList();
		/* [Thomas, April 29, 2017]
		 * We will accept main function with the following format:
		 * public void main() {}
		 * */
		//	args.add(ca.mcgill.sable.soot.ArrayType.v(
		//						  RefType.v("java.lang.String"), 1));

		/* [Thomas, May 21, 2017]
		 * Add one more parameter to the function call createThread
		 * */
		if (SpinUtil.containEvtHandlerMethod(sClass)) {
			for(SootMethod sm : SpinUtil.getEvtHandlerMethods(sClass))
			{
				system.createThread(sClass.getName() + "_" + sm.getName(), sm, sClass.getName());
				log.debug("creating thread for class " + sClass + ":" + sm);
			}
		} else {
			SootMethod method = sClass.getMethod("run", new ArrayList(), VoidType.v());
			system.createThread(sClass.getName(), method, sClass.getName());
			log.debug("creating thread for class " + sClass + ":" + method);
		}
	}
	/**
	 * Declare a BIR variable in the transition system.
	 * <p>
	 * The type extractor is used to convert the SootType into a BIR type.
	 * If the SootType is not representable (type extractor returns null),
	 * then we ignore this variable (with a warning).
	 */

	static StateVar declareVar(Object key, String name, Type sootType, 
			BirThread thread) {
		sootType.apply(typeExtract);
		edu.ksu.cis.bandera.bir.Type type = 
				(edu.ksu.cis.bandera.bir.Type) typeExtract.getResult();
		if (type == null) {
//			log.error("ignoring object of type " + sootType);
			return null;
		}
		// if ((thread == null) || type.isKind(RANGE | BOOL | ENUMERATED))
		return system.declareVar(key, name, thread, type, null);
	}

	void findReachableFrom(Stmt stmt) {
		Location loc = system.locationOfKey(stmt, thread);
		if (loc.getMark() != mark) {
			loc.setMark(mark);
			Iterator stmtIt = stmtGraph.getSuccsOf(stmt).iterator();
			// Don't include the 'return' statement at the end
			if (stmtIt.hasNext())
				reachableStmts.addElement(stmt);
			while (stmtIt.hasNext()) {
				Stmt nextStmt = (Stmt) stmtIt.next();
				findReachableFrom(nextStmt);
			}
		}
	}
	/**
	 * Heap objects are stored in BIR collections, which must be declared.
	 * We build a "dynamic map" that defines, for each allocator site
	 * (NewExpr or NewArrayExpr), which collection will hold the elements 
	 * of that site.
	 * <p>
	 * Currently, we simply create a collection for each allocator site.
	 * <p>
	 * This method accepts the soot classes that define the bodies of the
	 * threads, and it looks for allocator expressions in the code for
	 * these threads, defining a dynamic map entry for each.
	 * <p>
	 * In addition this code determines the set of types that are
	 * explicitly locked in statements that are reachable from the
	 * threads.  These will be used to determine which classes need
	 * to have locks allocated in the model.
	 */
	static void identifyAllocatorsLocks(SootClass [] sootClasses) {
		dynamicMap = new DynamicMap(sootClasses[0].getManager());
		lockedClasses = new HashSet();	
		// Look through all Stmts of all methods of all classes
		for (int i = 0; i < sootClasses.length; i++) {
			/* [Thomas, July 27, 2017]
			 * Add if condition 
			 * */
//			if(sootClasses[i].getName().equals("STInitializer"))
			{
				List methods = sootClasses[i].getMethods();
				Iterator methodIt = methods.iterator();
				while (methodIt.hasNext()) {
					SootMethod method = (SootMethod) methodIt.next();
					JimpleBody body = (JimpleBody) 
							new StoredBody(Jimple.v()).resolveFor(method);
					StmtList stmtList = body.getStmtList();
					for (int j = 0; j < stmtList.size(); j++) {
						Stmt stmt = (Stmt) stmtList.get(j);
						// Look for Definition statements with a NewExpr on the RHS
						if (stmt instanceof DefinitionStmt) {
							DefinitionStmt defStmt = (DefinitionStmt)stmt;
	
							if (defStmt.getRightOp() instanceof NewExpr) {
								NewExpr newExpr = (NewExpr) defStmt.getRightOp();
								// Filter out exception classes
								SootClass newClass = sootClasses[0].getManager().getClass(newExpr.getBaseType().className);
	
								boolean modeledClass = true;
								SootClass ancestor = newClass;
								while (ancestor.hasSuperClass()) {
									ancestor = ancestor.getSuperClass();
									if (ancestor.getName().equals("java.lang.Throwable")) {
										modeledClass = false;
									}
								}
	
								if (newClass.getName().startsWith("java.") && 
										!(newClass.getName().startsWith("java.lang.Object") ||
												newClass.getName().startsWith("java.lang.Thread")) ) {
									modeledClass = false;
								}
	
								if (modeledClass) {
									SootClass sc = sootClasses[0].getManager().getClass(newExpr.getBaseType().className);
									dynamicMap.addEntry(newExpr, newExpr, 
											bounds.getAllocMax(sc.getName()));
									dynamicClasses.add(sc);
									log.debug("identifyAllocatorsLocks: adding map entry for " +
											sc + " with bound of " + bounds.getAllocMax(sc.getName()));
								}
							}
							if (defStmt.getRightOp() instanceof NewArrayExpr) {
								NewArrayExpr newExpr = 
										(NewArrayExpr) defStmt.getRightOp();
								dynamicMap.addEntry(newExpr, newExpr, 
										bounds.getDefaultAllocMax(),
										bounds.getArrayMax());
							}			
							if (defStmt.getRightOp() instanceof NewMultiArrayExpr) {
								log.error("multi-dimensional arrays not supported");
							}
						}
	
						// Look for enter monitor statements and record type of arg
						if (stmt instanceof EnterMonitorStmt) {
							log.debug("identifyAllocatorsLocks: found monitor stmt");
							log.debug("monitor stmt: " + stmt.toString());
							EnterMonitorStmt enterStmt = (EnterMonitorStmt)stmt;
							Type type = enterStmt.getOp().getType();
							if (type instanceof RefType) {  
								log.debug("Adding class " + sootClasses[0].getManager().getClass(((RefType)type).className) +
										" to the list of locked classes.");
								lockedClasses.add(sootClasses[0].getManager().getClass(((RefType)type).className));
							}
						}
					}	    
				}
			}
		}

		parentClasses = new HashSet();
		extendsRelation = new Hashtable();
		for (Iterator dcIt = dynamicClasses.iterator(); dcIt.hasNext(); ) {
			SootClass sootClass = (SootClass) dcIt.next();
			log.debug("identifyAllocatorsLocks: class hierarchy analysis for "+sootClass.getName());

			if (threadClasses.contains(sootClass)) {
				HashSet newSet = new HashSet();
				newSet.add(sootClass);
				threadSubTypes.put(sootClass, newSet);
			}

			// Generate set of parents and inheritence relation for classes
			// for which there is a constructor in the program.
			SootClass child = sootClass;
			while (child.hasSuperClass()) {
				SootClass parent = child.getSuperClass();
				log.debug("identifyAllocatorsLocks: accessing parent of "+sootClass.getName() + " which is " + parent.getName());

				// Do not explicitly record a subtype of java.lang.Object
				// (which is identified in Soot as a class with no parent)
				// unless an instance of that class has been allocated.
				if (parent.hasSuperClass()) {
					extendsRelation.put(child,parent);
				}

				if (dynamicClasses.contains(child.getManager().getClass("java.lang.Object")) &&
						!parent.hasSuperClass()) {
					extendsRelation.put(child,
							child.getManager().getClass("java.lang.Object"));
				}

				if (child.getName().equals("java.lang.Thread") ||
						IdentifyRunnableClasses.implementsInterface(child, "java.lang.Runnable")) {
					extendsRelation.put(child,
							child.getManager().getClass("java.lang.Runnable"));

					// If "Object" is allocated it should be the root of
					// the class hierarchy.
					if (dynamicClasses.contains(child.getManager().getClass("java.lang.Object"))) {
						extendsRelation.put(child.getManager().getClass("java.lang.Runnable"),
								child.getManager().getClass("java.lang.Object"));
					}
				}

				if (threadClasses.contains(parent)) {
					if (threadSubTypes.get(parent) == null) {
						HashSet newSet = new HashSet();
						newSet.add(sootClass);
						threadSubTypes.put(parent, newSet);
					} else {
						((HashSet)threadSubTypes.get(parent)).add(sootClass);
					}
				}
				if (!dynamicClasses.contains(parent)) {
					parentClasses.add(parent);
				}
				child = parent;
			}
		}
		dynamicMap.print();
	}
	/**
	 * Set initial value of static field.
	 * <p>
	 * Currently, this only works if the assigned value is static.
	 */
	static void initializeGlobal(StaticFieldRef lhs, Value rhs) {
		StateVar var = system.getVarOfKey(lhs.getField());
		ExprExtractor extractor = new ExprExtractor(system, null, null, null,
				typeExtract, 
				new PredicateSet());
		rhs.apply(extractor);
		if (extractor.getResult() != null)
			var.setInitVal((edu.ksu.cis.bandera.bir.Expr)
					extractor.getResult());
	}
	/**
	 * Interpret a BIR trace in terms of the Jimple code from which
	 * it was created.
	 * 
	 * @param trace - a BIR trace of a transition system created by BIRC
	 * @return the corresponding Jimple trace 
	 */
	public static JimpleTrace interpretTrace(BirTrace trace) {
		return new JimpleTrace(trace);
	}
	/**
	 * Do a depth first search of the statement graph and return
	 * a vector of all statements reachable from a given statement.
	 * <p>
	 * We do this to eliminate exception handlers, which are not
	 * reachable in the BriefStmtGraph (the CompleteStmtGraph
	 * contains arcs representing possible exception raising,
	 * so the exception handlers are reachable in that graph).
	 */


	Vector reachableFrom(Stmt stmt) {
		reachableStmts = new Vector();
		mark = Location.getNewMark();
		findReachableFrom(stmt);
		return reachableStmts;
	}
}
