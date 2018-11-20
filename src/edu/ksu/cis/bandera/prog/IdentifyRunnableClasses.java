package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Enumeration;
import java.io.*;

import org.apache.log4j.Category;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.spin.*;

/**
 * Provides a static method, <code>identify</code>, that returns a hashSet 
 * of SootClass objects that are potentially executable as threads.
 *
 * A SootClass is returned in the set if:
 * <ul>
 * <li> it is a sub-type of <code>java.lang.Thread</code>
 * <li> it implements <code>java.lang.Runnable</code>
 * </ul>
 *
 * Subtyping and implementation relationships may be transitive so this
 * code searches the inheritence and interface hierarchies.
 *
 * In addition to determining "runnability" of a class.  This code
 * copies the appropriate <code>run</code> method from super-types down to
 * "runnable" sub-types if they do not define a <code>run</code>
 * method locally..
 *
 * This code is not re-entrant, but cannot be executed concurrently.  
 * A single instance of this class can be active in any program run
 * as there is static data created and accessed by the methods.
 */
public class IdentifyRunnableClasses {
/**
 * Logger
 */
	private static Category log = 
	        Category.getInstance(IdentifyRunnableClasses.class.getName());

	private static SootMethod theRun;

/**
 * Determines whether <code>theClass</code> is "runnable" and if so
 * it sets <code>theRun</code> to be the <code>run</code> that would
 * be executed when the class is "started".
 * 
 * @param theClass	class in question
 * @return		true if <code>theClass</code> is "runnable"
 */
//@ requires theClass != null;
private static boolean FindRun(SootClass theClass) {
	// Check to see if this class implements Runnable
	if (theClass.implementsInterface("java.lang.Runnable")) {
	  // return run method
	  theRun = theClass.getMethod("run", new ArrayList(), VoidType.v());
	  log.debug("propogating run method from " + theClass);
	  return true; 
	}

	// Check to see if this class extends Thread
	//   check to see if it implements a run method
	if (theClass.hasSuperClass() &&
	    theClass.getSuperClass().getName().equals("java.lang.Thread")) {
	  // if there is no run method generate an empty one
	  if (!theClass.declaresMethod("run", new ArrayList(), VoidType.v())) {
	    theRun = new SootMethod("run", new ArrayList(), VoidType.v());
	    JimpleBody body = (JimpleBody) Jimple.v().newBody(theRun);
	    theClass.addMethod(theRun);
	    log.debug("adding empty method for " + theClass);
	  } else {
	    theRun = theClass.getMethod("run", new ArrayList(), VoidType.v());
	    log.debug("propogating run method from " + theClass);
	  }
	  return true; 
	}

	// search up
	if (theClass.hasSuperClass() && FindRun(theClass.getSuperClass())) {
	  // if there is a local "run" method then reset theRun
	  if (theClass.declaresMethod("run", new ArrayList(), VoidType.v())) {
	    theRun = theClass.getMethod("run", new ArrayList(), VoidType.v());
	    log.debug("propogating run method from " + theClass);
	  } else {
	    log.debug("propogating run method");
          }
	  return true; 
	}

	return false;
}

/**
 * Returns true if the given interface, named as a <code>String</code>, 
 * is implemented by the given <code>SootClass</code> either directly or
 * indirectly.
 *
 * @param theClass	name of candidate implementor of interface
 * @param name	        name of interface
 * @return		indication of implements relationship
 */
//@ requires theClass != null;
//@ requires name != null;
public static boolean implementsInterface(SootClass theClass, String intName) {
	boolean verdict = false;
	if ( theClass.implementsInterface(intName) ) {
	  verdict = true;
	} else {
	  if ( theClass.getInterfaceCount() == 0 ) {
	    verdict = false;
	  } else {
            Iterator iiIter = theClass.getInterfaces().iterator();
	    while (iiIter.hasNext()) {
	      SootClass theInterface = (SootClass)iiIter.next();
	      verdict = implementsInterface(theInterface, intName);
	      if (verdict) break;

	      if (theInterface.hasSuperClass()) {
		verdict = implementsInterface(theInterface.getSuperClass(), intName);
	      }
	      if (verdict) break;
	    }
	  }
	}
	return verdict;
}


/**
 * Returns the subset of <code>SootClass</code> iterated over by the
 * parameter that correspond to "runnable" classes (either by
 * sub-typing <code>java.lang.Thread</code> or implementing 
 * <code>java.lang.Runnable</code>.
 * 
 * @param scIter	iterator for candidate <code>SootClass</code>es
 * @return		set of "runnable" classes
 */
//@ requires scIter != null;
//@ ensures ret != null;
public static java.util.HashSet identify(Enumeration scIter) {
	java.util.HashSet tmpClasses = new java.util.HashSet();

	while (scIter.hasMoreElements()) {
	  SootClass theClass = (SootClass) scIter.nextElement();

	  log.debug("trying to identify " + theClass);

	  /* [Thomas, April 29, 2017]
	   * We will accept main function with the following format:
	   * public void main() {}
	   * */
	  
  	  // Check to see if this class has a "main" method
	  ca.mcgill.sable.util.LinkedList args = new ca.mcgill.sable.util.LinkedList();
//	  args.add(ca.mcgill.sable.soot.ArrayType.v(RefType.v("java.lang.String"), 1));
//	  if (theClass.declaresMethod("main", args, VoidType.v())) {
	  if (SpinUtil.containEvtHandlerMethod(theClass)) {
//	    int mods = 
//	        theClass.getMethod("main", args, VoidType.v()).getModifiers();
	    tmpClasses.add(theClass);
//	    if (Modifier.isPublic(mods) && Modifier.isStatic(mods)) {
//  	      tmpClasses.add(theClass);
//	      continue;
//	    }
	  }

  	  // Check to see if this class implements Runnable or a
          // sub-interface of Runable
	  if (implementsInterface(theClass, "java.lang.Runnable")) {
	    tmpClasses.add(theClass);
	    continue;
	  }

	  // Check to see if this class extends Thread
	  //   check to see if it implements a run method
	  if (theClass.hasSuperClass() &&
	      theClass.getSuperClass().getName().equals("java.lang.Thread")) {
	    // if there is no run method generate an empty one
 	    if (!theClass.declaresMethod("run", new ArrayList(), 
						VoidType.v())) {
	      SootMethod newRun = new SootMethod("run", new ArrayList(), 
							VoidType.v());
	      JimpleBody body = (JimpleBody) Jimple.v().newBody(newRun);
	      theClass.addMethod(newRun);
	      log.debug("adding empty run method for " + theClass);
	    }
	    tmpClasses.add(theClass);
	    continue;
	  }

  	  // If neither of the above then search up the type hierarchy
	  // to see if a supertype extends Thread
	  //   recurse up the hierarchy searching
	  //   if found then pick up the lowest "run" method coming down
	  //
	  // Also check to see if a supertype implements Runnable
	  // directly or indirectly  
	  //   recurse up the hierarchy searching
	  //   if found then pick up the lowest "run" method coming down
	  {
	    theRun = null;
	    if (theClass.hasSuperClass() && FindRun(theClass.getSuperClass())) {
              // ensure that there is no local "run" method
 	      if (!theClass.declaresMethod("run", new ArrayList(), 
	 					  VoidType.v())) {
	        SootMethod newRun = new SootMethod("run", new ArrayList(), 
							  VoidType.v());
	        JimpleBody body = (JimpleBody) Jimple.v().newBody(newRun);
	        body.getStmtList().addAll(
		  ((JimpleBody) theRun.getBody(Jimple.v())).getStmtList());
	        body.getLocals().addAll(
		  ((JimpleBody) theRun.getBody(Jimple.v())).getLocals());
	        body.getTraps().addAll(
		  ((JimpleBody) theRun.getBody(Jimple.v())).getTraps());
	        newRun.storeBody(Jimple.v(), body);
	        theClass.addMethod(newRun);
	        log.debug("adding inherited run method for " + theClass);
	      }
	      tmpClasses.add(theClass);
	      continue;
	    }
	  }
	}
	return tmpClasses;
}

public static void main(String args[]) {
	SootClassManager classManager = new SootClassManager();
	CompilationManager.reset();
	CompilationManager.setFilename(args[0]);
	CompilationManager.setClasspath(".");
	CompilationManager.setIncludedPackagesOrTypes(new String[0]);

	//Compile 
	try { CompilationManager.compile(); } 
		catch (Exception e) { 
	  System.out.println(e);
	  System.out.println("Compilation failed");
	}

        Hashtable exceptions = CompilationManager.getExceptions();
        if (exceptions.size() > 0) {
            System.out.println("Compilation failed:");
            for (Enumeration e = exceptions.keys(); e.hasMoreElements();) {
                Object filename = e.nextElement();
                System.out.println("- " + filename);
                Vector es = (Vector) exceptions.get(filename);
                for (java.util.Iterator i = es.iterator(); i.hasNext();) {
                    System.out.println("  * " + i.next());
                }
            }
	    return;
        }

	Hashtable storedClasses = CompilationManager.getCompiledClasses();

	Enumeration scIter = storedClasses.elements();
	while (scIter.hasMoreElements()) {
	  ((SootClass) scIter.nextElement()).resolveIfNecessary();
	}

	java.util.HashSet rClasses = IdentifyRunnableClasses.identify(storedClasses.elements());

	for (java.util.Iterator cIt = rClasses.iterator(); cIt.hasNext(); ) {
	  ((SootClass)cIt.next()).printTo(new StoredBody(Jimple.v()), new PrintWriter(System.out, true));
	}
}
}
