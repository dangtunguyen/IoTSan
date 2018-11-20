/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000, 2001                                    *
 * Venkatesh Prasad Ranganath (rvprasad@cis.ksu.edu)                 *
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

package edu.ksu.cis.bandera.bofa;

import ca.mcgill.sable.soot.NoSuchMethodException;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.Type;
import ca.mcgill.sable.soot.VoidType;
import ca.mcgill.sable.util.List;
import ca.mcgill.sable.util.VectorList;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Category;

/*
 * Util.java
 * $Id: Util.java,v 1.2 2002/02/21 07:42:24 rvprasad Exp $
 */

/**
 * This class provides utility methods which can be used through out the package.
 *
 * @author <a href="mailto:rvprasad@cis.ksu.edu">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */

public class Util {

	/**
	 * An empty list to be used for queries on no parameter method.
	 *
	 */
	final private static VectorList emptyParamList = new VectorList();

	/**
	 * Provides logging through log4j.
	 *
	 */
	private static Category cat = Category.getInstance(Util.class.getName());

	/**
	 * A private constructor to prevent the instantiation of an object of this class.
	 */
	private Util() {
	}

	/**
	 * Provides a empty parameter list.
	 *
	 * @return returns a empty parameter list.
	 */
	public static VectorList getEmptyParamList()
	{
		return emptyParamList;
	}

	/**
	 * Creates a new object which implements ca.mcgill.sable.util.Collection and copy the contents of
	 * the given java.util.collection into it.
	 *
	 * @param name <code>String</code> is the name of the class which implements
	 * ca.mcgill.sable.util.collection interface and which will be the actual type of the returned
	 * object.
	 * @param c <code>Collection</code> is an object which implements java.util.Collection interface
	 * and contains values that need to be copied into another container object and returned.
	 * @return <code>ca.mcgill.sable.util.Collection</code> implementing object of type
	 * <code>String</code> containing all the values that were in <code>Collection</code>.
	 */
	public static ca.mcgill.sable.util.Collection convert(String name,
														  Collection c) {
		ca.mcgill.sable.util.Collection retval = null;
		try {
			Class collect = Class.forName(name);
			retval = (ca.mcgill.sable.util.Collection)collect.newInstance();
			if (c != null) {
				Iterator i = c.iterator();
				while (i.hasNext()) {
					retval.add(i.next());
				} // end of while (i.hasNext())
			} // end of if (c != null)

		} catch (ClassNotFoundException e) {
			cat.info("The class named " + name + " is not available in the " + "class path.");
			e.printStackTrace();
		} catch (Exception e) {
			cat.info("Error instantiating an object of class " + name + ".");
			e.printStackTrace();
			retval = null;
		} // end of catch

		return retval;
	}

	/**
	 * Creates a new object which implements java.util.Collection and copy the contents of the given
	 * ca.mcgill.sable.util.collection into it.
	 *
	 * @param name <code>String</code> is the name of the class which implements java.util.collection
	 * interface and which will be the actual type of the returned object.
	 * @param c <code>Collection</code> is an object which implements ca.mcgill.sable.util.Collection
	 * interface and contains values that need to be copied into another container object and
	 * returned.
	 * @return <code>java.util.Collection</code> implementing object of type <code>String</code>
	 * containing all the values that were in <code>Collection</code>.
	 */
	public static Collection convert(String name, ca.mcgill.sable.util.Collection c) {
		Collection retval = null;
		try {
			Class collect = Class.forName(name);
			retval = (Collection)collect.newInstance();
			if (c != null) {
				ca.mcgill.sable.util.Iterator i = c.iterator();
				while (i.hasNext()) {
					retval.add(i.next());
				} // end of while (i.hasNext())

			} // end of if (c != null)
		} catch (ClassNotFoundException e) {
			cat.info("The class named " + name + " is not available in the class path.");
			e.printStackTrace();
		} catch (Exception e) {
			cat.info("Error instantiating an object of class " + name + ".");
			e.printStackTrace();
			retval = null;
		} // end of catch

		return retval;
	}

	/**
	 * Shorthand version of Util.getDefiningClass() where the parameter list is empty and the
	 * returnType is void.
	 *
	 * @param sc class in or above which the method may be defined.
	 * @param method name of the method (not the fully classified name).
	 * @return If there is such a class then a SootClass object is returned else null is returned.
	 */
	public static SootClass getDeclaringClass(SootClass sc, String method) {
		return getDeclaringClass(sc, method, emptyParamList, VoidType.v());
	}

	/**
	 * Provides the SootClass which injects the given method into the specific branch of the
	 * inheritence hierarchy to which sc belongs to.
	 *
	 * @param sc class that defines the branch in which the injecting class exists.
	 * @param method name of the method (not the fully classified name).
	 * @param parameterTypes list of type of the parameters of the method.
	 * @param returnType return type of the method.
	 * @return If there is such a class then a SootClass object is returned.
	 * @throw <code>ca.mcgill.sable.soot.NoSuchMethodException</code> is thrown when no such method
	 * is declared in the given hierarchy.
	 */
	public static SootClass getDeclaringClass(SootClass sc, String method, List parameterTypes, 
											  Type returnType) { 
		SootClass contains = sc;
		while(!contains.declaresMethod(method, parameterTypes, returnType)) {
			if (contains.hasSuperClass()) {
				contains = contains.getSuperClass();
			} else {
				throw new NoSuchMethodException(sc + "." + method);
			} // end of else
		}
		return contains;
	}

	/**
	 * <code>isAncestorOf</code> checks if a class is an ancestor of another.  It is assumed that a
	 * class cannot be it's own ancestor.
	 *
	 * @param child <code>SootClass</code> representing the class whose ancestor is of interest.
	 * @param ancestor <code>String</code> representing the name of the ancestor.
	 * @return <code>boolean</code> true if ancestor is indeed is one of the ancestor, false
	 * otherwise.
	 */
	public static boolean isAncestorOf(SootClass child, String ancestor) {

		boolean retval = false;
		while ((retval == false) && child.hasSuperClass()) {
			if (child.getName().equals(ancestor)) {
				retval = true;
			} else {
				child = child.getSuperClass();
			}
		}
		return retval;
	}
}// Util
