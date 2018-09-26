package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *
 * All rights reserved.                                              *
 * Matthew Dwyer (dwyer@cis.ksu.edu)				     *
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
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jjjc.*;

/**
 * Provide builtin library functionality for Jave programs to be 
 * interpreted by Bandera analysis and transformation tools.
 *
 * The idea in this here is to provide proper SootMethod
 * and SootClass definitions for builtin methods and classes.  
 * These methods will be treated specially by components of Bandera and
 * as such the actual field and method definitions may differ from 
 * the typical Java library code (in fact most methods will not even
 * have a body defined for them).
 *
 * Provides API for the <code>Bandera</code> class methods, all
 * methods are static public:
 * <ul>
 * <il> <code>void beginAtomic()</code>
 * <il> <code>void endAtomic()</code>
 * <il> <code>void startThread(String className, Object target)</code>
 * <il> <code>bool randomBool()</code>
 * <il> <code>int randomInt(int maxInt)</code>
 * <il> <code>Object randomClass(String className)</code>
 * <il> <code>Object randomReachable(Object root)</code>
 * <il> <code>bool randomBoolExtern()</code>
 * <il> <code>int randomIntExtern(int maxInt)</code>
 * <il> <code>Object randomClassExtern(String className)</code>
 * <il> <code>Object randomReachableExtern(Object root)</code>
 * <il> <code>void assert(bool expr)</code>
 * </ul>
 * 
 * For backwards compatibility all references to <code>bool choose()</code>
 * should be interpreted as references to <code>bool randomBool()</code>.
 *
 * A trivial SootClass for <code>java.lang.Object</code> is provided since
 * it is considered a class with no fields or methods (it does have methods
 * <code>wait</code>, <code>notify</code>, <code>notifyAll</code> but they
 * are defined implicitly and inherited by all types).
 *
 * The SootClass for <code>java.lang.Thread</code> has a field 
 * <code>target</code> of type <code>java.lang.Runnable</code> (implicit 
 * methods for this class include <code>start</code> and those inherited
 * from <code>Object</code>).
 *
 * @author Matt Dwyer &lt;dwyer@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:25 $
 */
public class BanderaBuiltin {

    private static SootClass banderaClass;

    public static void init(SootClassManager scm) {
	banderaClass = scm.getClass("Bandera");
    }

    public static SootMethod getMethod(String name) {
	if (!(name.equals("beginAtomic") || name.equals("endAtomic") ||
	      name.equals("startThread") || name.equals("assert") ||
	      name.equals("randomBool")  || name.equals("randomInt") ||
	      name.equals("randomClass") || 
	      name.equals("randomReachable") )) {
	    // assert("No such builtin Bandera method",false);

	    System.out.println("No such builtin Bandera method");
	    return null;
	}

	SootMethod method;
	try { 
	    method = banderaClass.getMethod(name); 
	    return method;
	} catch (ca.mcgill.sable.soot.NoSuchMethodException e) { 

	    if (name.equals("beginAtomic") || name.equals("endAtomic")) {
		method = new SootMethod(
					name, new LinkedList(), 
					ca.mcgill.sable.soot.VoidType.v(), 
					Modifier.PUBLIC | Modifier.STATIC);

	    } else if (name.equals("randomBool")) {
		method = new SootMethod(
					name, new LinkedList(), 
					ca.mcgill.sable.soot.BooleanType.v(), 
					Modifier.PUBLIC | Modifier.STATIC);

	    } else if (name.equals("randomInt")) {
		LinkedList parms = new LinkedList();
		parms.addLast(ca.mcgill.sable.soot.IntType.v());
		method = new SootMethod(
					name, parms,
					ca.mcgill.sable.soot.IntType.v(), 
					Modifier.PUBLIC | Modifier.STATIC);

	    } else if (name.equals("randomClass")) {
		LinkedList parms = new LinkedList();
		parms.addLast(
			      ca.mcgill.sable.soot.RefType.v("java.lang.String"));
		method = new SootMethod(
					name, parms,
					ca.mcgill.sable.soot.RefType.v(
								       "java.lang.Object"),
					Modifier.PUBLIC | Modifier.STATIC);

	    } else if (name.equals("randomReachable")) {
		LinkedList parms = new LinkedList();
		parms.addLast(
			      ca.mcgill.sable.soot.RefType.v("java.lang.Object"));
		method = new SootMethod(
					name, parms,
					ca.mcgill.sable.soot.RefType.v(
								       "java.lang.Object"),
					Modifier.PUBLIC | Modifier.STATIC);

	    } else if (name.equals("startThread")) {
		LinkedList parms = new LinkedList();
		parms.addLast(
			      ca.mcgill.sable.soot.RefType.v("java.lang.String"));
		parms.addLast(
			      ca.mcgill.sable.soot.RefType.v("java.lang.Object"));
		method = new SootMethod(
					name, parms,
					ca.mcgill.sable.soot.VoidType.v(), 
					Modifier.PUBLIC | Modifier.STATIC);

	    } else if (name.equals("assert")) { 
		LinkedList parms = new LinkedList();
		parms.addLast(ca.mcgill.sable.soot.BooleanType.v());
		method = new SootMethod(
					name, parms,
					ca.mcgill.sable.soot.VoidType.v(), 
					Modifier.PUBLIC | Modifier.STATIC);

	    } else {
		method = new SootMethod(
					name, new LinkedList(), 
					ca.mcgill.sable.soot.VoidType.v(), 
					Modifier.PUBLIC | Modifier.STATIC);
	    }

	    banderaClass.addMethod(method);
	    return method;
	}
    }


    /**
     * Unit test driver. 
     * takes as input
     * <ul>
     * <li> a name for a dummy verify class
     * </ul>
     */
    public static void main(String args[]) {
	SootClassManager classManager = new SootClassManager();

	init(classManager);

	System.out.println("Bandera.beginAtomic: " + getMethod("beginAtomic"));
	System.out.println("Bandera.endAtomic: " + getMethod("endAtomic"));
	System.out.println("Bandera.assert: " + getMethod("assert"));
	System.out.println("Bandera.randomBool: " + getMethod("randomBool"));
	System.out.println("Bandera.randomInt: " + getMethod("randomInt"));
	System.out.println("Bandera.randomClass: " + getMethod("randomClass"));
	System.out.println("Bandera.randomReachable: " + getMethod("randomReachable"));
	System.out.println("Bandera.startThread: " + getMethod("startThread"));

	CompilationManager.reset();
	CompilationManager.setFilename(args[0]);
	CompilationManager.setClasspath(".");
	CompilationManager.setIncludedPackagesOrTypes(new String[0]);

	try { 
	    CompilationManager.compile(); 
	} catch (Exception e) { 
            e.printStackTrace();
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

	if (CompilationManager.getSootClassManager().managesClass("Bandera")) {
  	    System.out.println("class Bandera is managed");
	} else {
  	    System.out.println("class Bandera is not managed");
	}

	init(CompilationManager.getSootClassManager());

        System.out.println("Bandera.beginAtomic: " + getMethod("beginAtomic"));
        System.out.println("Bandera.endAtomic: " + getMethod("endAtomic"));
        System.out.println("Bandera.assert: " + getMethod("assert"));
        System.out.println("Bandera.randomBool: " + getMethod("randomBool"));
        System.out.println("Bandera.randomInt: " + getMethod("randomInt"));
        System.out.println("Bandera.randomClass: " + getMethod("randomClass"));
        System.out.println("Bandera.randomReachable: " + getMethod("randomReachable"));
        System.out.println("Bandera.startThread: " + getMethod("startThread"));

    }
}
