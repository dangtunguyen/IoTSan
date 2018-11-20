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
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.HashSet;
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.Set;

import org.apache.log4j.Category;

/*
 * FGActionInvoke.java
 *
 * $Id: FGActionInvoke.java,v 1.3 2002/04/01 00:58:12 rvprasad Exp $
 */

/*
 * Open questions:
 * - what is the appropriate time place for the flowgraph to be
 * generated for the body of the method.  Currently in the
 * driver and FA classes, it seems like the nodes for the body of a method
 * are already created before this action is invoked.
 *
 * - what happens when we have multiple value variants that map to the
 * same method variants (e.g., two different value variants of the
 * same class but allocated at different positions)?
 *
 */

/**
 * This class describes the action to be taken when new values arrive at the method position of a
 * method invocation AST node.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.ci.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Revision: 1.3 $
 */
public class FGActionInvoke implements FGAction
{
	/**
	 * Number of arguments to the method
	 */
	private int      argCount;

	/**
	 * Array of FGNodes corresponding to the argument ASTs
	 */
	private FGNode[] argNodes;

	/**
	 * FGNode for the invocation AST node
	 */
	private FGNodeAST   invokeNode;

	/**
	 * Method in which the invocation expression occurs.
	 *
	 */
	private SootMethod sootMethod;

	/**
	 * Set of method variants that have been hooked into the flow graph for this node.
	 */
	private Set      installedMethodVariants;

	/**
	 * Captures the class of the current value being processed when new values arrive at the node.
	 * This will take on all the values that arrive at the associated invocation site.
	 *
	 */
	private SootClass valueSootClass;


	/**
	 * Provides logging capabilities through log4j.
	 *
	 */
	private static Category cat;

	static {
		cat = Category.getInstance(FGActionInvoke.class.getName());
	}

	/**
	 * Constructor of the class.
	 *
	 * @param argCount number of arguments to the invoked method.
	 * @param argNodes the nodes corresponding to the arguments.
	 * @param invokeNode the node corresponding to the invocation AST.  This will also be the node
	 * which provides the return value of the method during flow analysis.
	 * @param sootMethod the enclosing method.
	 */
	public FGActionInvoke(int      argCount,
						  FGNode[] argNodes,
						  FGNodeAST   invokeNode,
						  SootMethod sootMethod)
    {
		this.argCount      = argCount;
		this.argNodes      = argNodes;
		this.invokeNode    = invokeNode;
		this.sootMethod    = sootMethod;
		this.installedMethodVariants = new HashSet();
    }

	/**
	 * Constructor of the class to be used when SpecialInvokeExpr is being handled.
	 *
	 * @param argCount number of arguments to the invoked method.
	 * @param argNodes the nodes corresponding to the arguments.
	 * @param invokeNode the node corresponding to the invocation AST.This will also be the node
	 * which provides the return value of the method during flow analysis. This will not have any use
	 * as constructors do not return values.
	 * @param sootClass the class to which the method body belongs during invocation.
	 */
	public FGActionInvoke(int      argCount,
						  FGNode[] argNodes,
						  FGNodeAST   invokeNode,
						  SootClass sootClass)
    {
		this.argCount      = argCount;
		this.argNodes      = argNodes;
		this.invokeNode    = invokeNode;
		this.sootMethod    = sootMethod;
		this.installedMethodVariants = new HashSet();
    }

	/**
	 * Plugin in the new value variants into the invocation site.  This will result in extension of
	 * the call graph and the methods being considered for analysis if there are any that were not
	 * considered.
	 *
	 * @param values the set of new values that arrived at the node to to which this action is
	 * attached.
	 */
	public void doAction(FASet values)
    {
		SootMethodSootClass temp = new SootMethodSootClass();
		ClassToken classToken;

		// the declaring method for a particular value variant
		SootMethod declMethod;

		// the selected method variant for the * given base class and method
		// signature.
		MethodVariant methodVariant;

		// Iterate through the given set of value variants
		for(Iterator i = values.iterator(); i.hasNext();) {
			ValueVariant valueVariant = (ValueVariant) i.next();

			/*
			 * valueVariant is a new variant for this site, so...  Scan class hierarchy (starting
			 * from class of value variant) to find the appropriate declaring class for this method,
			 * and call the Method Variant Manager to select the appropriate variant.

			  If the method variant is not already installed here,
			   - hook the code for the method into the flow graph. This
			   involves connecting the argument nodes to parameter nodes,
			   connecting the method outflow node ('last' node) to
			   the node for the AST of the invoke.
			   - generate work to move ALL values
			   at the argument Nodes along to parameter nodes
			   - push current value variant to 'this' parameter.
			   If the method is already installed (i.e., already hooked
			   into the flowgraph), then argument nodes and return
			   points are already connected.  We only ...
			   - push current value variant to 'this' parameter.
			*/

			//  Scan class hierarchy and find the soot method that matches the method signature
			classToken = valueVariant.getClassToken();

			// In case there is a null in the values ignore it
			if ((classToken == ClassTokenSimple.unknownClassToken)
				|| (classToken == ClassTokenSimple.nullClassToken)) {
				cat.debug("BOFA: Null may flow into the invocation site.");
				continue;
			}

			if (classToken instanceof ClassTokenSimple) {
				InvokeExpr expr = (InvokeExpr)invokeNode.getObj();
				SootMethod callee = expr.getMethod();
				/*
				 * The following is required for specialinvoke.  In this the method implementation is
				 * known but not the receiver.  So, we use the class information embedded in the
				 * invoke expression get the class implementing the constructor.  So, it is
				 * reasonable to set valueSootClass to the implementor class even if the class of the
				 * receiver is a subclass of the implementor class.  This should work as the subclass
				 * "is the" super class.
				 */
				if (expr instanceof SpecialInvokeExpr) {
					valueSootClass = callee.getDeclaringClass();
				} else {
					valueSootClass = ((ClassTokenSimple) classToken).getSootClass();
				} // end of else
				declMethod = MethodVariantManager.findDeclaringMethod
					(valueSootClass, callee.getName(), callee.getParameterTypes(),
					 callee.getReturnType());
			} else {
				throw new RuntimeException("BOFA: array object flowing into invoke site");
			}

			// Select the appropriate method variant
			methodVariant = MethodVariantManager.select(declMethod);

			if (!installedMethodVariants.contains(methodVariant)) {
				/* - hook method variant into the flowgraph
				 * For each arg...
				 *   - make arcs from arg to corresponding parameter node
				 *   - add work: send values that are currently
				 *   at arg to corresponding method parameter
				 */
				FGNode paramNode;
				for (int j=0; j<argCount; j++) {
					paramNode = methodVariant.getParameterNode(j);
					FGNode.makeArc(argNodes[j], paramNode);
					try {
						FA.workList.insert(new FGWorkSendVals(argNodes[j].getValues(), paramNode));
					}
					catch (NullPointerException e) {
						cat.error(e.getMessage() + "\n Argument Node: " + argNodes[j]);
					}
				}

				/* The value set for 'this' parameter for the method should include only the objects
				 * that are flowing into this variant.  Therefore, we should not make an arc from the
				 * base node to the 'this' parameter (in analogy with the conventional parameters
				 * above).  Instead, we should send only the current value variant.  In essence, we
				 * are picking up information at a split point --- we know that this variant will
				 * only be called on objects that are represented by the current value variant.  TRY
				 * TO EXPLAIN THIS BETTER.  */
				FA.workList.insert
					(new FGWorkSendVals(new FASet(valueVariant), methodVariant.getThisNode()));

				// make arc from last node of method to AST node.
				FGNode lastNode = methodVariant.getReturnNode();
				FGNode.makeArc(lastNode, invokeNode);
				if (lastNode.getValues().size() > 0) {
					FA.workList.insert(new FGWorkSendVals(lastNode.getValues(), invokeNode));
				} // end of if (lastNode.getValues().size() > 0)

				//  Add the method variant to the list of variants installed at this node.
				installedMethodVariants.add(methodVariant);
			} else {
				// Push current value variant to 'this' node
				FA.workList.insert
					(new FGWorkSendVals(new FASet(valueVariant), methodVariant.getThisNode()));
			} /* end: if */

			BOFA.flowProcessingCallBackReg
				.callNonStaticInvoke((NonStaticInvokeExpr)invokeNode.getObj(), this);
		} /* end: for each valueVariant */
    }

	/**
	 * Provides the method enclosing the expressions corresponding to this object.
	 *
	 * @return the enclosing method.
	 */
	SootMethod getEnclosingSootMethod()
	{
		return sootMethod;
	}


	/**
	 * Provides the class of the current value that flowed into the invocation site.
	 *
	 * @return the class of the current value being processed at the invocation site.
	 */
	SootClass getValueSootClass()
	{
		return valueSootClass;
	}


    /**
     * Pairs a method and class.  Used to move more than one piece of data to the callback method.
     *
     */
    class SootMethodSootClass
    {
        /**
         * Method in which the invocation expression occurs.
         *
         */
        SootMethod enclosingMethod;

        /**
         * Class of the new value that flowed into the invocation site.
         *
         */
        SootClass classOfValue;
    }
}
