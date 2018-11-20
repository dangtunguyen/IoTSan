/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999                                          *
 * Robby (robby@cis.ksu.edu)                                         *
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

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.baf.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import java.io.*;

import org.apache.log4j.Category;

/*
 * FA.java
 * $Id: FA.java,v 1.4 2002/07/12 22:45:15 rvprasad Exp $
 */

/**
 * This static class provides the entry point to Object Flow analysis. All the managers and the
 * WorkList is managed in this class.
 *
 * Running the analysis with different levels of precision yields different classes of value
 * variants, e.g., value variants indexed by allocator site, or by a single "point" index, etc.
 *
 * @author <a href="mailto:robby@cis.ksu.edu">Robby</a>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.4 $)
 */
public class FA {

	/**
	 * The class manager which is queried to get information regarding various
	 * classes.
	 */
	static SootClassManager classManager;

	/**
	 * The WorkList object used for object flow analysis.
	 *
	 */
	static WorkList workList;

	/**
	 * Provides logging capability through log4j.
	 *
	 */
	private static Category cat;

	private static ArrayIndexManager arrayIndexManager;

	private static CodeIndexManager codeIndexManager;

	private static InstanceIndexManager instanceIndexManager;

	private static MethodIndexManager methodIndexManager;

	private static ValueIndexManager valueIndexManager;

	static {
		cat = Category.getInstance(FA.class.getName());
	}

	/**
	 * In this method we initialize the various IndexManagers which partition the data.  We also,
	 * create the required sets for the static and instance variables via <code>initClassInfo</code>.
	 * This needs to be dynamic as we donot know before hand how the variables need to be tracked,
	 * per class or per object or any other way.  We also trigger the creation of the FlowGraph here,
	 * which will create the initial work in the WorkList and then we get into an infinite loop, till
	 * the WorkList is emptied, calling doWork() on each work object.
	 *
	 * @param classManager the class manager that provides the soot classes comprising the system
	 * being analyzed.
	 * @param rootSootClass the class from which to start the analysis
	 * @param rootSootMethod the method from which to start the analysis
	 */
	public static void init(SootClassManager classManager, Collection rootSootClasses) {

		FA.classManager  = classManager;
		Collection storedClasses = classManager.getClasses();

		/*
		 * Initialize the various managers used.  We need to implement a framework where the Managers
		 * are provided from outside and we just use them instead of creating them here.
		 */
		MethodVariantManager.init(methodIndexManager);
		StaticFieldManager.init();
		InstanceVariantManager.init(instanceIndexManager);
		ValueVariantManager.init(valueIndexManager, codeIndexManager);
		ArrayVariantManager.init(arrayIndexManager);

		workList = new WorkList();

		/*
		 * Request a classtoken for the root class and plugin the variant for the method from where
		 * the analysis needs to start.
		 */
		 for (Iterator iter = rootSootClasses.iterator(); iter.hasNext();) {
			BOFA.EntryPoint element = (BOFA.EntryPoint) iter.next();
			ClassTokenSimple.select(element.rClass);
			MethodVariantManager.select(element.rMethod);			
		}
		
	}

	/**
	 * Tells whether a given method is hooked into the FlowGraph or not. In simple terms whether the
	 * method is reachable in the system.
	 *
	 * @param sootMethod the method which needs to be checked for reachability.
	 * @return a value indicating the reachability of the given method.  <code>true</code> - method
	 * will be reached.  <code>false</code> - method will not be reached.
	 */
	public static boolean isReachable(SootMethod sootMethod)
    {
		return MethodVariantManager.isManaged(sootMethod);
    }

    /**
	 * Reset the data structures.  Clear all the stored data pertaining to any analysis performed in
	 * the past.
	 *
	 */
	public static void reset() {
	    classManager = null;
		ArrayVariantManager.reset();
		InstanceVariantManager.reset();
		MethodVariantManager.reset();
		StaticFieldManager.reset();
		ValueVariantManager.reset();
		if (workList != null) {
			workList.clear();
		} // end of if (workList != null)
    }

	/**
	 * Triggers the flow in the FlowGraph.
	 *
	 */
	public static void run() {
		workList.doWork();
	}

	static void setMode(String mode) {
		Class temp;
		Class[] methodConstructorParams = { SootMethod.class, Index.class };

		arrayIndexManager = new ArrayIndexManagerValueVariant();
		codeIndexManager = new CodeIndexManagerAlloc();
		instanceIndexManager = new InstanceIndexManagerPoint();
		valueIndexManager = new ValueIndexManagerAlloc();
		methodIndexManager = new MethodIndexManagerPoint();
	}

	/**
	 * Iterates through each of the variants of the given method and unions the value variants
	 * produced by the given expression.  Thus, the results of a (possibly polymorphic) analysis are
	 * collected into a set for the specified expression.
	 *
	 * @param sootMethod the method containing the given expression
	 * @param expr the expression for which the value variants need to be summarized.
	 * @return returns a set of value variants.
	 * @see    edu.ksu.cis.bandera.bofa.ValueVariant
	 */
	public static Set summarizeExprValueVariants(SootMethod sootMethod, Expr expr)
    {
		Set valueVariantSummarySet;

		valueVariantSummarySet = new HashSet();

		for (Iterator i = MethodVariantManager.getVariants(sootMethod) .iterator(); i.hasNext();) {
			MethodVariant methodVariant = (MethodVariant) i.next();
			valueVariantSummarySet.addAll(methodVariant.getASTValues(expr));
		}

		return valueVariantSummarySet;
    }

	/**
	 * Iterates over all the variants of the instance variable and collects the possible values that
	 * may flow into the instance variable.
	 *
	 * @param sootField the instance field of the class.
	 * @return set of values that may flow into the instance variables.
	 */
	public static Set summarizeInstanceFieldVariants(SootField sootField)
	{
		Set valueVariantSummarySet;
		FGNodeField node;
		valueVariantSummarySet = new HashSet();
		for (Iterator variants = InstanceVariantManager.getVariants(sootField).iterator();
			 variants.hasNext();) {
			node = (FGNodeField)((InstanceVariant)variants.next()).getNode();
			valueVariantSummarySet.addAll(node.getValues());
		} // end of for ()
		return valueVariantSummarySet;
    }

	/**
	 * Iterates through each of the variants of the given method and unions the value variants
	 * returned from the method.
	 *
	 * @param sootMethod the method for which the return value variants are requested.
	 * @return returns a set of value variants.
	 * @see    edu.ksu.cis.bandera.bofa.ValueVariant
	 */

	public static Set summarizeLastValueVariants(SootMethod sootMethod)
    {
		Set valueVariantSummarySet;

		valueVariantSummarySet = new HashSet();

		for (Iterator i = MethodVariantManager.getVariants(sootMethod).iterator(); i.hasNext();)
			{
				MethodVariant methodVariant = (MethodVariant) i.next();
				valueVariantSummarySet.addAll(methodVariant.getReturnValues());
			}

		return valueVariantSummarySet;
    }

	/**
	 * Iterates through each of the variants of the given method and unions the value variants
	 * flowing into the given locals.
	 *
	 * @param sootMethod the method enclosing the given expression
	 * @param local the value variants are summarized for the given local variable.
	 * @return returns a set of value variants.
	 * @see    edu.ksu.cis.bandera.bofa.ValueVariant
	 *
	*/
	public static Set summarizeLocalValueVariants(SootMethod sootMethod, Local local)
    {
		Set valueVariantSummarySet;
		MethodVariant methodVariant;

		valueVariantSummarySet = new HashSet();

		for (Iterator i = MethodVariantManager.getVariants(sootMethod).iterator(); i.hasNext();) {
			methodVariant = (MethodVariant)i.next();
			valueVariantSummarySet.addAll(methodVariant.getLocalValues(local));
		}
		return valueVariantSummarySet;
    }

	/**
	 * Iterates through each of the variants of the given method and unions the value variants
	 * flowing into parameter number <code>n</code>.
	 *
	 * <p> Note: Not sure how error is handled when <code>n</code> exceeds the the number of
	 * parameters. </p>
	 *
	 * @param sootMethod the method enclosing the given expression
	 * @param n the value variants are summarized for the given parameter number
	 * @return returns a set of value variants.
	 * @see    edu.ksu.cis.bandera.bofa.ValueVariant
	 */
	public static Set summarizeParameterValueVariants(SootMethod sootMethod, int n)
    {
		Set valueVariantSummarySet;

		valueVariantSummarySet = new HashSet();

		for (Iterator i = MethodVariantManager.getVariants(sootMethod).iterator(); i.hasNext();) {
			MethodVariant methodVariant = (MethodVariant) i.next();
			valueVariantSummarySet.addAll(methodVariant.getParameterValues(n));
		}
		return valueVariantSummarySet;
    }

	/**
	 * Gathers all the values that may flow into the static field variable specified.
	 *
	 * @param sootField the static field for which the summary set is requested.
	 * @return set of values that may flow into the static field. */
	public static Set summarizeStaticFieldVariants(SootField sootField)
	{
		Set valueVariantSummarySet;

		valueVariantSummarySet = new HashSet();
		valueVariantSummarySet.addAll(StaticFieldManager.get(sootField).getValues());
		return valueVariantSummarySet;
    }

	/**
	 * Iterates through each of the variants of the given method and unions the value variants
	 * flowing into the implicit <code>this</code> parameter.
	 *
	 * @param sootMethod the method corresponding to <code>this</code> parameter.
	 * @return returns a set of value variants.
	 * @see    edu.ksu.cis.bandera.bofa.ValueVariant
	 */
	public static Set summarizeThisValueVariants(SootMethod sootMethod)
    {
		Set valueVariantSummarySet;
		MethodVariant methodVariant;
		valueVariantSummarySet = new HashSet();

		for (Iterator i = MethodVariantManager.getVariants(sootMethod) .iterator(); i.hasNext();) {
			methodVariant = (MethodVariant) i.next();
			valueVariantSummarySet.addAll(methodVariant.getThisValues());
		}
		return valueVariantSummarySet;
    }

}
