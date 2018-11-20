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
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.io.*;

import org.apache.log4j.Category;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import edu.ksu.cis.bandera.jjjc.*;

/**
 * Provides a collection of static methods that enrich
 * the set of queries about the class/interface hierarchy that are 
 * supported directly by Soot.
 *
 * Three such queries are supported
 * <ul>
 * <li> Whether a class implementsInterface an interface
 * <li> Whether a class extendsClass another class
 * <li> Produce list of classes that lie below a class or interface in the
 *      hierarchy where a class precedes any of its ancestors in the list.
 * </ul>
 * Note that in Java an interface, <code>a</code>, can be declared as a 
 * sub-interface of another interface, <code>b</code>, 
 * e.g., <code>interface a extends b {...</code>.  
 * Soot represents such a relationship between interfaces as an "implementation"
 * relationship, e.g., if <code>ar</code> is the 
 * <code>SootClass</code> reference to the class named "a" 
 * then <code>ar.implementsInterface("b")</code> would return true.
 *
 * This code is not re-entrant.  
 * A single instance of this class can be active in any program run
 * as there is write-once static data created and accessed by the 
 * methods.
 */
public class HierarchyQuery {
/**
 * Class logger 
 */
	private static Category log = 
	        Category.getInstance(HierarchyQuery.class.getName());

/**
 * Stores a mapping from classes to list of immediate descendants in both
 * the class and interface hierarchies.  
 */
	private static Hashtable subType; 

/**
 * Determines whether <code>theClass</code> implements <code>theInterface</code>
 * either directly or through sub-classing/interfacing.
 * 
 * @param theClass	class in question
 * @param theInterface	interface which class implements (or not)
 * @return		true if <code>theClass</code> 
 *			implements <code>theInterface</code>
 */
//@ requires theClass != null;
//@ requires theInterface != null;
public static boolean implementsInterface(SootClass theClass, 
					  SootClass theInterface) {
	List workList = new ArrayList();

	// iterates up the type and interface hierarchy
	workList.add(theClass);
	while (!workList.isEmpty()) {
	    SootClass current = (SootClass)workList.remove(0);

	    List interfaces = current.getInterfaces();
	    if (interfaces.contains(theInterface)) return true;

	    workList.addAll(interfaces);
	    if (current.hasSuperClass()) workList.add(current.getSuperClass());
	}
	return false;
}

/**
 * Determines whether <code>theClass</code> extends <code>theSuperType</code> 
 * either directly or through sub-classing.
 * 
 * @param theClass	Class in question
 * @param theSuperType	Class which <code>theClass</code> extends (or not)
 * @return		true if <code>theClass</code> extends 
 *			<code>theSuperType</code>
 */
//@ requires theClass != null;
//@ requires theSuperType != null;
public static boolean extendsClass(SootClass theClass, 
				   SootClass theSuperType) {
	SootClass current = theClass;
	while (current.hasSuperClass()) {
	    if (current.getSuperClass() == theSuperType) return true;
	    current = current.getSuperClass();
	}
	return false;
}

/**
 * Collects all classes that lie below the given class/interface in the 
 * inheritence/implements hierarchy.  This is a private helper method
 * for <code>orderedClasses</code>  That method insures
 * that the List it produces is (partially) ordered according to
 * the inheritence relation.  To reduce the cost of producing that
 * ordering, we collect the sub/implementing classes in a depth-first
 * fashion.  Since we may start with an interface and descend through
 * a sequence of sub-interfaces, we take care to skip adding them to 
 * the list, but we do traverse them to find the classes that implement
 * them.
 *
 * @param theClassInterface	The class or interface in question
 * @param classList		The accumulated list of classes that lie
 *                              below <code>theClassInterface</code>
 */
//@ requires theClassInterface != null;
//@ requires classList != null;
private static void lowerClasses(SootClass theClassInterface, List classList) {
	List children = (List)subType.get(theClassInterface);

	for (Iterator childIt = children.iterator(); childIt.hasNext(); ) {
	    SootClass current = (SootClass)childIt.next();
 	    lowerClasses(current, classList);
	    if (!Modifier.isInterface(current.getModifiers()) &&
		!classList.contains(current)) {
	        classList.add(current);
	    } 
	}
}

/**
 * Returns an ordered list of classes that lie beneath the given class/interface
 * in the inheritence/implements hierarchy.  The classes should be ordered
 * so that no ancestor of a given class precedes it in the list.
 * We exploit the fact that the depth-first traversal 
 * in <code>lowerClasses</code>
 * will produce a quasi-ordered list.  We do a bubble sort of
 * the list (which is efficient for nearly sorted data) to 
 * properly order the classes, then pack them into a list to be returned.
 *
 * @param theClassInterface	The class or interface in question
 * @return  			The list of classes that lie below 
 *				<code>theClassInterface</code> partially 
 *				ordered by the <code>extends()</code> relation
 */
//@ requires theClassInterface != null;
public static List orderedClasses(SootClass theClassInterface) {
	buildSubMap(theClassInterface);

	List classList = new ArrayList();
	lowerClasses(theClassInterface, classList);

	Object oa[] = classList.toArray();
	
	// bubble-sort the quasi-ordered list by the extends partial order
	for (int i = 0; i < oa.length-1; i++) { 
	    for (int j = oa.length-1; j > i; j--) {
		if ( extendsClass((SootClass)oa[j], (SootClass)oa[j-1]) ) {
		    Object tmp = oa[j]; 
		    oa[j] = oa[j-1]; 
		    oa[j-1] = tmp; 
	        } 
	    } 
	} 

	// create a new list to load the values in the order
	classList = new ArrayList();
	for (int i = 0; i < oa.length; i++) { 
	    classList.add(i,oa[i]);
	}
	return classList;
}

/**
 * Builds an explicit representation of descendents in the class/interface
 * hierarchy.
 * For each class, a SootClass explicitly represents direct ancestors
 * in the class/interface hierarchy, it does not represent descendents.
 * This private helper method calculates the direct descendents of a
 * given class and stores them in subType (which maps classes to lists of
 * classes)
 *
 * @param theClass	A class used to access the list of all classes
 *			in the compiled unit.
 */
//@ requires theClass != null;
private static void buildSubMap(SootClass theClass) {

log.debug("buildSubMap: " + theClass);

	subType = new Hashtable();
	List allClasses = theClass.getManager().getClasses();
	for (Iterator classIt = allClasses.iterator(); classIt.hasNext(); ) {
	    SootClass current = (SootClass)classIt.next();

	    // Look for a parent in the class hierarchy
            if (current.hasSuperClass()) {
		SootClass parent = current.getSuperClass();
		if (subType.get(parent) == null) {
		    subType.put(parent, new ArrayList());
		}
		((List)subType.get(parent)).add(current);
	    }

	    // Look for parents in the interface hierarchy
            if (current.getInterfaceCount() > 0) {
		List interfaces = current.getInterfaces();
		for (Iterator interfaceIt = interfaces.iterator(); 
		     interfaceIt.hasNext(); ) {
		    SootClass anInterface = (SootClass)interfaceIt.next();
		    if (subType.get(anInterface) == null) {
		        subType.put(anInterface, new ArrayList());
		    }
		    ((List)subType.get(anInterface)).add(current);
		}
	    }

	    // If not already created, we create a list of descendents
	    if (subType.get(current) == null) { 
		subType.put(current, new ArrayList());
	    }

	}
}

/**
 * Unit test driver. 
 * takes as input
 * <ul>
 * <li> a name for a Java source file
 * <li> the names of either two classes or a class and an interface
 * </ul>
 * compiles the source file, prints out whether their is an extends or
 * implementation relationship between the other inputs and finaly 
 * prints the ordered list of descendents of the two classes/interfaces.
 */
public static void main(String args[]) {
	SootClassManager classManager = new SootClassManager();
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

	Hashtable storedClasses = CompilationManager.getCompiledClasses();
	for (Enumeration scIter = storedClasses.elements(); 
	     scIter.hasMoreElements(); ) {
	    ((SootClass) scIter.nextElement()).resolveIfNecessary();
	}

	SootClass a = null;
	SootClass b = null;

	for (Enumeration scIter = storedClasses.elements(); 
	     scIter.hasMoreElements(); ) {
	    SootClass tmp = (SootClass) scIter.nextElement();
	    if (tmp.getName().equals(args[1])) {
	      a = tmp;
	    }
	    if (args.length > 2 && tmp.getName().equals(args[2])) {
	      b = tmp; 
	    }
	}

        if (args.length > 2) {
	    if (extendsClass(a,b)) {
	        System.out.println(a.getName() + " extends " + b.getName());
	    } else {
	        System.out.println(a.getName() + " does not extend " + b.getName());
	    }

	    if (implementsInterface(a,b)) {
	        System.out.println(a.getName() + " implements " + b.getName());
	    } else {
	        System.out.println(a.getName() + " does not implement " + b.getName());
	    }
	}

	List curDescendants = orderedClasses(a);
	System.out.print("Descendants of " + a.getName() + " are: ");
	for (Iterator classIt = curDescendants.iterator(); 
	     classIt.hasNext(); ) {
	    System.out.print(((SootClass)classIt.next()).getName());
	    if (classIt.hasNext()) System.out.print(", ");
	}
	System.out.println("");

	if (args.length > 2) {
	    curDescendants = orderedClasses(b);
	    System.out.print("Descendants of " + b.getName() + " are: ");
	    for (Iterator classIt = curDescendants.iterator(); 
	         classIt.hasNext(); ) {
	        System.out.print(((SootClass)classIt.next()).getName());
	        if (classIt.hasNext()) System.out.print(", ");
	    }
	    System.out.println("");
	}
}
}
