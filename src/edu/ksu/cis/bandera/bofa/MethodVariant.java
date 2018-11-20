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

import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.VoidType;

import ca.mcgill.sable.soot.jimple.Jimple;
import ca.mcgill.sable.soot.jimple.JimpleBody;
import ca.mcgill.sable.soot.jimple.Local;
import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.soot.jimple.StmtList;

import ca.mcgill.sable.util.HashMap;
import ca.mcgill.sable.util.HashSet;
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.Map;
import ca.mcgill.sable.util.Set;

import org.apache.log4j.Category;


/*
 * MethodVariant.java
 * $Id: MethodVariant.java,v 1.4 2002/07/12 22:45:15 rvprasad Exp $
 */


/**
 * This class represents a variant of a method.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.4 $)
 */
public class MethodVariant {
	/**
	 * Provides logging capability through log4j.
	 *
	 */
	private static Category cat = Category.getInstance(MethodVariant.class.getName());

	/**
	 * The flow graph nodes correpsonding to the local variables in the
	 * method. */
	protected Map localNodes;

	/**
	 * The flow graph nodes corresponding to the AST nodes occuring in the
	 * method. */
	private Map ASTNodes;

	/**
	 * FGStmt object used to extend the flow graph for this variant.
	 */
	private FGStmt fgStmt;

	/**
	 * The index associated with this object.
	 */
	private Index methodIndex;

	/**
	 *The flow graph nodes corresponding to the parameters of the method.
	 */
	private FGNodeParameter[] parameterNodes;

	/**
	 * The flow graph node corresponding to the return point of the method.
	 */
	private FGNodeAST returnNode;

	/**
	 * The method represented by this object.
	 */
	private SootMethod sootMethod;

	/**
	 * The flow graph node corresponding to the "this" variable of the method.
	 */
	private FGNodeThis thisNode;

	/**
	 * Constructor of the class.
	 *
	 * @param sootMethod the method represented by this variant.
	 * @param methodIndex the index associated with this variant.
	 */
	protected MethodVariant(SootMethod sootMethod, Index methodIndex) {
		this.sootMethod  = sootMethod;
		this.methodIndex = methodIndex;
		returnNode = new FGNodeAST((Object)null);
		thisNode   = new FGNodeThis(this);
		ASTNodes   = new HashMap();
		localNodes = new HashMap();

		// Create set variables for each parameter.
		// Hold in an array indexed by parameter number
		int num_parameters = sootMethod.getParameterCount();
		parameterNodes = new FGNodeParameter[num_parameters];

		for(int j = 0; j < num_parameters; j++) {
			parameterNodes[j] = new FGNodeParameter(j, this);
		}
	}

	/**
	 * Assumptions: obj must be a valid reference to Jimple expression within the current method.
	 */
	/**
	 * Provide the set of FG nodes associated with the given AST node.
	 * @param obj the AST node corresonding to which the FG nodes are required.
	 * @return the set of FG nodes assocaited with the given AST node.
	 */
	public Set getASTValues(Object obj) {
		if(ASTNodes.containsKey(obj)) {
			return getASTNode(obj).getValues();
		} else {
			/*
			 * if Node is not found in the ASTNode table and we assume that obj is a valid reference,
			 * this means that the expression referenced by obj has not been included in the flow
			 * graph because it doesn't give rise to any objects (e.g., it is an expression of base
			 * type).  Thus, we return an empty set.
			 */
			return new HashSet();
		}
	}

	/* The following methods have public access.  They form the interface for clients outside of the
	 *  FA package.  These clients should never need to know the structure of the flow graph nor the
	 *  representation for flowgraph nodes.  Typically, the clients should only be looking up the
	 *  results of the flow analysis, i.e., that should only be fetching the set of value variants
	 *  for a particular construct.
	 *   The methods for an interface for callers within the FA package follows
	 *   the public interface.
	 */
	public FGNodeLocal getLocalNode(Local l) {
		return (FGNodeLocal)localNodes.get(l);
	}

	public Set getLocalValues(Local l) {
		if(localNodes.containsKey(l)) {
			return getLocalNode(l).getValues();
		} else {
			/*
			 * if Node is not found in the ASTNode table and we assume that obj is a valid reference,
			 * this means that the expression referenced by obj has not been included in the flow
			 * graph because it doesn't give rise to any objects (e.g., it is an expression of base
			 * type).  Thus, we return an empty set.
			 */
			return new HashSet();
		}
	}

	/**
	 * Provides the method being represented.
	 *
	 * @return the represented method.
	 */
	public SootMethod getMethod() {
		return sootMethod;
	}

	/**
	 * Provides the associated index.
	 *
	 * @return the associated index.
	 */
	public Index getMethodIndex() {
		return methodIndex;
	}

	/**
	 * Provides the set of values flowing into the nth parameter of the method.
	 * @param n an <code>int</code> value
	 * @return set of values flowing into the given parameter.
	 */
	public Set getParameterValues(int n) {
		return getParameterNode(n).getValues();
	}

	/**
	 * Provides the set of values flowing returned by the method.
	 * @return set of values flowing returned by the method.
	 */
	public Set getReturnValues() {
		return getReturnNode().getValues();
	}

	/**
	 * Provides the set of values flowing into "this" variable.
	 * @return set of values flowing into "this" variable.
	 */
	public Set getThisValues() {
		return getThisNode().getValues();
	}

	protected void process() {
		Local      local;
		JimpleBody body;
		StmtList   stmts;

		/* generates graph for each statement */
		fgStmt = new FGStmt(this, ASTNodes);

		try {
			body  = (JimpleBody)sootMethod.getBody(Jimple.v());
			stmts = body.getStmtList();
		} catch(RuntimeException e) {
			cat.info("Exception occurred while processing " + sootMethod + ": " + e.getMessage());

			/*
			 * External methods will not have body to be compiled, hence, an exception will be
			 * raised.  In such a situation we will insert an emptyiness and it will act as a sink
			 * for all values flowing in.  Also, if the return type is not void a value of a
			 * particular type but unknown expression and statement needs to be plugged in.
			 */
			if(!sootMethod.getReturnType().equals(VoidType.v())) {
				FGNode externalNode = new FGNode();
				FGNode.makeArc(externalNode, returnNode);

				FGWork work = new FGWorkSendVals(new FASet(), externalNode);
				FA.workList.insert(work);
			} //end of if (!sootMethod.getReturnType().equals(VoidType.v()))

			return;
		}

		for(Iterator i = body.getLocals().iterator(); i.hasNext();) {
			Local l = (Local)i.next();
			localNodes.put(l, new FGNodeLocal(l));
		} // end of for (Iterator i = body.getLocals().iterator(); i.hasNext();)

		for(int i = 0; i < stmts.size(); i++) {
			fgStmt.build((Stmt)stmts.get(i));
		}
	}

	/**
	 * Provides the flow graph node associated with the AST node.
	 *
	 * @param obj the AST node corresponding to which the flow graph node is required.
	 * @return the flow graph node associated with the AST node.
	 */
	FGNodeAST getASTNode(Object obj) {
		return (FGNodeAST)ASTNodes.get(obj);
	}

	/**
	 * Returns the FGNode for the n'th paramter (0-based indexing)
	 *
	 * @param n the parameter number.
	 * @return the flow graph node describing the nth parameter.
	 */
	FGNodeParameter getParameterNode(int n) {
		if(n < sootMethod.getParameterCount()) {
			return parameterNodes[n];
		} else {
			throw new RuntimeException("Parameter count exceeded:  " + sootMethod.getName() + "  Param#: " + n);
		}
	}

	/**
	 * Provides the flow graph node associated with the return value of the method.
	 *
	 * @return the flow graph node associated with the return value of the method.
	 */
	FGNode getReturnNode() {
		return returnNode;
	}

	/**
	 * Provides the flow graph node associated with "this" variable.
	 *
	 * @return the flow graph node associated with "this" variable.
	 */
	FGNodeThis getThisNode() {
		return thisNode;
	}
}