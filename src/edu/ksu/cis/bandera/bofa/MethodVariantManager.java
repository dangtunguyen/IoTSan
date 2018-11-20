/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999                                          *
 * John Hatcliff (hatcliff@cis.ksu.edu)
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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/*
 * MethodVariantManager.java
 * $Id: MethodVariantManager.java,v 1.5 2002/07/12 22:45:15 rvprasad Exp $
 */


/**
 * This class manages the method variants corresponding to various methods on a per class basis.
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <A HREF="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</A>
 * @version $Name:  $($Revision: 1.5 $)
 */
public class MethodVariantManager {
	/**
	 * Provides logging capability through log4j.
	 *
	 */
	private static Logger logger;

	/**
	 * The Manager that provides indices to partition the variants.
	 */
	private static MethodIndexManager methodIndexManager;

	/**
	 * A Map from method to all possible variants.
	 */
	private static Map methodMap;

	static {
		logger = LogManager.getLogger(MethodVariantManager.class);
	}

	/**
	 * Tells whether the given method is managed or not.
	 *
	 * @param sootMethod the method which may or may not be managed.
	 * @return true indicates the method is managed, false otherwise.
	 */
	public static boolean isManaged(SootMethod sootMethod) {
		return (methodMap.containsKey(sootMethod));
	}

	/**
	 * Provides the set of managed methods.
	 *
	 * @return set of managed methods.
	 */
	public static Set getMethods() {
		return methodMap.keySet();
	}

	/**
	 * Provides the set of variants associated with the given method.
	 *
	 * @param sootMethod the method corresponding to which the variants are required.
	 * @return the collection of variants associated with the method.
	 */
	public static Collection getVariants(SootMethod sootMethod) {
		logger.debug("Sootmethod being requested is " +  sootMethod);
		return ((Map)methodMap.get(sootMethod)).values();
	}

	/**
	 * Provides the defining SootMethod for the method.  The SootMethod in which the method is
	 * actually implemented and not just inherited.
	 *
	 * @param sootClass the class in which the sootMethod is available.
	 * @param sootMethod the method for which we need the object corresponding to it's
	 * implementation.
	 * @return the object representing an implementation of the requested method.  null if none
	 * exist.
	 */
	public static SootMethod findDeclaringMethod(SootClass sootClass, SootMethod sootMethod) {
		String methodName     = sootMethod.getName();
		List   parameterTypes = sootMethod.getParameterTypes();
		Type   returnType     = sootMethod.getReturnType();

		return findDeclaringMethod(sootClass, methodName, parameterTypes, returnType);
	}

	/**
	 * Provides the defining SootMethod for the method.  The SootMethod in which the method is
	 * actually implemented and not just inherited.
	 *
	 * @param sootClass the class in which the sootMethod is available.
	 * @param methodName the name of the method
	 * @param parameterTypes the type of the parameters to the method
	 * @param returnType the return type of the method.
	 * @return the object representing an implementation of the requested method.  null if none
	 * exist.
	 */
	public static SootMethod findDeclaringMethod(SootClass sootClass, String methodName, List parameterTypes, Type returnType) {
		logger.info("findDeclaringMethod:-->" + sootClass + "." + methodName + "(" + parameterTypes + "):" + returnType);

		if(sootClass.declaresMethod(methodName, parameterTypes, returnType)) {
			// should we avoid the extra test, call getMethod directly, and catch the exception that
			// it throws if method is not found?
			return sootClass.getMethod(methodName, parameterTypes, returnType);
		} else {
			if(sootClass.hasSuperClass()) {
				SootClass superClass = sootClass.getSuperClass();

				return findDeclaringMethod(superClass, methodName, parameterTypes, returnType);
			} else {
				logger.error("No Such Method. " + sootClass + "." + methodName + "(" + parameterTypes + "):" + returnType);

				return null;
			}
		}
	}

	/**
	 * Initialize the Manager.
	 *
	 * @param methodIndexManager the manager that provides the indices for
	 * managing the variants.
	 */
	public static void init(MethodIndexManager methodIndexManager) {
		MethodVariantManager.methodIndexManager = methodIndexManager;
		MethodVariantManager.methodMap          = new HashMap();
	}

	/**
	 * Describe <code>reset</code> method here.
	 *
	 */
	public static void reset() {
		if(methodMap != null) {
			methodMap.clear();
		}

		if(methodIndexManager != null) {
			methodIndexManager.reset();
		}
	}

	/**
	 * Provides the Method variant associated with  the method.  if none exist then a new variant is
	 * created.
	 *
	 * @param sootMethod the method corresponding to which the Variant is required.
	 * @return the associated method variant.
	 */
	public static MethodVariant select(SootMethod sootMethod) {
		Map   indexMap;
		Index methodIndex = methodIndexManager.select();
		logger.debug("MethodVariant.select():--> " + sootMethod);

		if(methodMap.containsKey(sootMethod)) {
			// if method is already registered, compute variant index, and see if it is registered.
			indexMap = (Map)methodMap.get(sootMethod);

			if(!indexMap.containsKey(methodIndex)) {
				// index was not registered, so make new method and register.
				createMethodVariant(sootMethod, methodIndex, indexMap);
			}
		} else {
			// if method is not registered, then register it with an index map with the current
			// variant
			indexMap = new HashMap();
			methodMap.put(sootMethod, indexMap);

			if(!createMethodVariant(sootMethod, methodIndex, indexMap)) {
				methodMap.remove(indexMap);
			}
		}

		return (MethodVariant)indexMap.get(methodIndex);
	}

	private static boolean createMethodVariant(SootMethod sootMethod, Index methodIndex, Map indexMap) {
		boolean       ret           = true;
		MethodVariant methodVariant = new MethodVariant(sootMethod, methodIndex);
		indexMap.put(methodIndex, methodVariant);

		try {
			methodVariant.process();
		} catch(RuntimeException e) {
			indexMap.remove(methodIndex);
			logger.error("Method variant could not be created successfully.", e);
			ret = false;
		} finally {
			return ret;
		}
	}
}