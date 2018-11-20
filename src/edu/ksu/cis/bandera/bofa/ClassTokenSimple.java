/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Shawn Laubach (laubach@acm.org)        *
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

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;

import org.apache.log4j.Category;

/*
 * ClassTokenSimple.java
 * $Id: ClassTokenSimple.java,v 1.2 2002/02/21 07:42:21 rvprasad Exp $
 */

/**
 * This class represents the class token for non array types in the flow analysis.  These are classes
 * in the system being analysed.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class ClassTokenSimple implements ClassToken
{
	/**
	 * Maps <code>SootClass</code> objects to <code>ClassToken</code> objects.
	 */
	private static Map allocatedTokens = new HashMap();

	/**
	 * The <code>ClassToken</code> object representing the type of the unknown values.  Basically the
	 * values which are not created in the code being analyzed.  These are values like command line
	 * arguments in main method.
	 */
	final public static ClassTokenSimple unknownClassToken =
		new ClassTokenSimple(new SootClass("unknownClassToken"));

	/**
	 * The <code>ClassToken</code> corresponding to the null object. It is reasonable to use the same
	 * Class Token for all null values.
	 */
	final public static ClassTokenSimple nullClassToken =
		 new ClassTokenSimple(new SootClass("BOFA_NullValue"));

	/**
	 * Provides logging capability through log4j.
	 *
	 */
	private static Category cat;

	/**
	 * Hook in the mappings between SootClass objects and ClassToken objects for unknown values and
	 * null values.
	 */
	static {
		allocatedTokens.put(unknownClassToken.getSootClass(), unknownClassToken);
		allocatedTokens.put(nullClassToken.getSootClass(), nullClassToken);
		cat = Category.getInstance("edu.ksu.cis.bandera.bofa.ClassTokenSimple");
	}

	/**
	 * Retrieves the ClassToken object corresponding to the SootClass.  If a ClassToken is not
	 * associated, a new object is created and associated.
	 * @param sootClass the soot class for which the class token is requested.
	 * @return the class token corresponding to the soot class.  */
	public static ClassTokenSimple select(SootClass sootClass)
    {
		if (allocatedTokens.containsKey(sootClass)) {
			return (ClassTokenSimple) allocatedTokens.get(sootClass);
		} else {
			ClassTokenSimple classToken = new ClassTokenSimple((SootClass) sootClass);
			allocatedTokens.put(sootClass, classToken);
			initClass(sootClass);
			return classToken;
		}
    }

	/**
	 * The class associated with this class token.
	 */
	protected SootClass sootClass;


	/**
	 * Constructor for the class
	 *
	 * @param sootClass is the class represented by this ClassTokenSimple object.
	 */
	private ClassTokenSimple(SootClass sootClass)
    {
		this.sootClass = sootClass;
    }

	/**
	 * Returns the SootClass associated with this class token.
	 *
	 * @return the SootClass
	 */
	public SootClass getSootClass()
    {
		return sootClass;
    }

	/**
	 * Initializes the instance and class level data for the given class.  Any static initialization
	 * block then it is enclosed in lt; clinit gt; method(in JVM bytecodes).  If any such method is
	 * defined in the class then that method is hooked into the flow graph.
	 *
	 * @param sootClass is the class for which the information should be set up in BOFA before
	 * further processing.
	 */
	static void initClass(SootClass sootClass)
	{
		/*
		 * According to the JVM Spec: during class initialization, all the super class should be
		 * initialized first and only then the current class' initialization should occur.
		 *
		 * This is done in this try-catch block.  Also if the class file for a class is not found to
		 * get the super class it is caught here.  We use classes denote even primitive types and
		 * hence, such a treatment is required.
		 */
		try {
			ClassTokenSimple.select(sootClass.getSuperClass());
		} catch (NoSuperClassException e) {
			cat.info("No super class for " + sootClass);
		} catch (RuntimeException e) {
			cat.info("No class found for " + sootClass + "\n" + e.getMessage());
		}

		for(Iterator i = sootClass.getFields().iterator(); i.hasNext();) {
			SootField sootField = (SootField) i.next();
			// Assume that we only have to separate class fields into two
			// categories: static and instance.
			if (Modifier.isStatic(sootField.getModifiers()))
				StaticFieldManager.select(sootField);
			else
				InstanceVariantManager.select(sootField);
		}
		// Process the class initializing method.
		if (sootClass.declaresMethod("<clinit>")) {
			MethodVariantManager.select(sootClass.getMethod("<clinit>"));
		} // end of if (sootClass.declaresMethod("<clinit>"))
	}

	public String toString() {
		return sootClass.getName();
	}
}
