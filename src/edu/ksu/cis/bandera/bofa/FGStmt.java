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
import ca.mcgill.sable.util.*;

import org.apache.log4j.Category;

/**
 * The class in which the statements of a method are processed to extend the
 * FlowGraph for the MethodVariant.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class FGStmt extends AbstractStmtSwitch
{
	/**
	 * The method variant for which the flow graph needs to be extended.
	 */
	private MethodVariant methodVariant;

	/**
	 * The method associated with the method variant.
	 */
	private SootMethod sootMethod;

	/**
	 * Maps the AST nodes occuring in the method to flow graph nodes.
	 */
	private Map astNodes;

	/**
	 * The FGExpr object which will process the expressions that occur in the statement that will be
	 * processed.
	 */
	private FGExpr fgExpr;

	/**
	 * The current statement in the method being processed.
	 */
	private Stmt stmt;

	/**
	 * The object used to process LHS expressions.
	 *
	 */
	private TempAbstJmplValSwitch sw;

	/**
	 * Provides logging capability through log4j.
	 *
	 */
	private static Category cat;

	static {
		cat = Category.getInstance(FGStmt.class.getName());
	}

	/* This code seems to be tightly coupled to MethodVariant.  Perhaps it should be placed in that
	   class (same for FGExpr).
	*/
	/**
	 * Constructor of the class.
	 *
	 * @param methodVariant the method whose statements needs to be processed.
	 * @param astNodes the map of AST nodes for the given method.
	 */
	public FGStmt(MethodVariant methodVariant, Map astNodes)
    {
		this.methodVariant = methodVariant;
		this.sootMethod    = methodVariant.getMethod();
		this.fgExpr        = new FGExpr(methodVariant, astNodes);
		this.astNodes      = astNodes;
		sw = new TempAbstJmplValSwitch();
    }

	/**
	 * Starts the extension of flow graph for the given statement.
	 *
	 * @param s the statement for which the FG needs to be created.
	 */
	public void build(Stmt s)
    {
		stmt = s;
		s.apply(this);
    }

	/**
	 * The class in which nodes corresponding to LHS expressions are created.
	 */
	class TempAbstJmplValSwitch extends AbstractJimpleValueSwitch
	{
		/**
		 * Handles a local variable on LHS.
		 *
		 * @param l the local variable on LHS.
		 */
		public void caseLocal(Local l)
		{
			FGNodeAST astNode   = new FGNodeAST(l);
			FGNode.makeArc(astNode, methodVariant.getLocalNode(l));
			astNodes.put(l, astNode);
			((AbstractJimpleValueSwitch)this).setResult(astNode);
		}

		/**
		 * Handles a static field variable on LHS.
		 *
		 * @param f the static field variable on LHS.
		 */
		public void caseStaticFieldRef(StaticFieldRef f)
		{
			SootField field = f.getField();
			FGNodeAST astNode = new FGNodeAST(f);
			FGNode.makeArc(astNode, StaticFieldManager.select(field));
			astNodes.put(f, astNode);
			((AbstractJimpleValueSwitch)this).setResult(astNode);
		}

		/**
		 * Handles a instance field variable on LHS.
		 *
		 * @param f the instance field variable on LHS.  */
		public void caseInstanceFieldRef(InstanceFieldRef f)
		{
			SootField field = f.getField();
			FGNodeAST astNode =
				new FGNodeAST(f);
			FGNode.makeArc(astNode, InstanceVariantManager.select(field).getNode());
			astNodes.put(f, astNode);
			((AbstractJimpleValueSwitch)this).setResult(astNode);
		}

		// should we also handle CaughtExceptionRef() and NextNextStmtRef()

		/**
		 * Handles array references on LHS.
		 *
		 * @param ref the array reference on LHS.
		 */
		public void caseArrayRef(ArrayRef ref)
		{
			FGNode baseNode = fgExpr.build(ref.getBase(), stmt);

			/*
			 * The "type" attribute of the array ref AST is the component type of the array.  Make an
			 * array type to use in ClassTokenArray.select()
			 */
			ArrayType arrayType =
				ClassTokenArray.componentToArrayType(ref.getType());

			ClassTokenArray classTokenArray = ClassTokenArray.select(arrayType);

			// Here, we need to attach an action to the flowgraph as in RHS array reference.
			FGNodeAST arrayRefNode = new FGNodeAST(ref);
			/*
			 * generate action: Different array objects (represented by value variants) may flow into
			 * the base node.
			 *
			 * For any new value variant that flows into the base node, the array variant FGNode that
			 * summarizes the contents of the array will be found and an FG arc will be made from the
			 * AST node to the array variant node.
			 */
			baseNode.addAction(new FGActionArrayStore(classTokenArray, arrayRefNode));
			astNodes.put(ref,arrayRefNode);
			((AbstractJimpleValueSwitch)this).setResult(arrayRefNode);
		}

		/**
		 * Handles all cases that are unhandled at present.
		 *
		 * @param o the object representing the value occuring on LHS which is currently not handled.
		 */
		public void defaultCase(Object o) {
			cat.debug("Unhandled LHS: " + o.getClass());
			((AbstractJimpleValueSwitch)this).setResult(null);
		}
	}

	/**
	 * Extends the part of flow graph node for LHS expressions.
	 *
	 * @param v the object representing the LHS expression.
	 * @return the flow node corresponding to the given LHS expression.
	 */
	public FGNodeAST getLHSNode(Value v)
    {
		v.apply(sw);
		FGNodeAST astNode = (FGNodeAST)sw.getResult();
		if (astNode != null) {
			astNode.setStmt(stmt);
		} // end of if (astNode != null)
		return astNode;
	}

	/**
	 * Extends the part of flow graph for identity statement.
	 *
	 * @param s the object representing an identity statement.
	 */
	public void caseIdentityStmt(IdentityStmt s)
    {
		/*
		 * Identity: Generate graph for RHS. Then, make a FG arc from RHS node to node for (variable)
		 * that appears on LHS.  Note that if no object values are produced in the RHS, then rhsNode
		 * should be null and FG.makeArc is defined to do nothing when the source node is null.
		 *
		 * Remember: identity is used to represent actions that happen during JVM method invocation
		 * and manipulation of exceptions.  For example, in the actual byte code, there is an
		 * implicit transfer of the actual parameters into slots in the frame (which represent formal
		 * parameters which are represented the same as locals).  In this case, we have a Jimple
		 * statement like local-a := @param2 --- this represents the transfer from the stack into the
		 * frame location associated with the local variable 'local-a'.
		 *
		 * In a similar way, catch blocks like try {...} catch (Exception e) {...}  are parameterized
		 * on the exception e, and there is an implicit transfer when the JVM is executing from the
		 * current exception to the variable e.  So this generates identity statements like e :=
		 * @caughtexception.
		 */
		Value lhs, rhs;
		FGNodeAST lhsNode, rhsNode;

		lhs = s.getLeftOp();
		rhs = s.getRightOp();

		lhsNode = getLHSNode(lhs);
		rhsNode = fgExpr.build(rhs, s);

		FGNode.makeArc(rhsNode, lhsNode);
    }

	/**
	 * Extends the part of flow graph for assignment statement.
	 *
	 * @param s the object representing an assignment statement.
	 */
	public void caseAssignStmt(AssignStmt s)
    {
		Value lhs, rhs;
		FGNodeAST lhsNode, rhsNode;

		lhs = s.getLeftOp();
		rhs = s.getRightOp();

		lhsNode = getLHSNode(lhs);
		rhsNode = fgExpr.build(rhs, s);

		FGNode.makeArc(rhsNode, lhsNode);
    }

	/**
	 * Extends the part of flow graph for if statement.  We just process the boolean expression.
	 *
	 * @param s the object representing the if statement.
	 */
	public void caseIfStmt(IfStmt s)
    {
		/*
		 * If: Generate graph for test expression.  No outgoing arc since there is nothing returned
		 * here.  We may be able to eliminate traversing the expression if there is not object flow
		 * within (e.g., no method applications).
		 */
		fgExpr.build(s.getCondition(), s);
    }

	/**
	 * Extends the part of flow graph for return statement.
	 *
	 * @param s the object representing the return statment.
	 */
	public void caseReturnStmt(ReturnStmt s)
    {
		/**
		 * Return:  Generate a FG for return expression, and make an arc from the return expression
		 * to the exit node for this method.
		 */
		FGNode.makeArc(fgExpr.build((Value)s.getReturnValue(), s),
				   methodVariant.getReturnNode());
    }

	/**
	 * Extends the part of flow graph for invoke statement.
	 *
	 * @param s the object representing the invocation statement.
	 */
	public void caseInvokeStmt(InvokeStmt s)
    {
		fgExpr.build(s.getInvokeExpr(), s);
    }

	/**
	 * Extends the part of flow graph for the entermonitor statement.  We just process the
	 * expression.
	 *
	 * @param s the object representing the entermonitor statement.
	 */
	public void caseEnterMonitorStmt(EnterMonitorStmt s)
    {
		fgExpr.build(s.getOp(), s);
	}

	/**
	 * Extends the part of flow graph for the exitmonitor statement.  We just process the expression.
	 *
	 * @param s the object representing the exitmonitor statement.
	 */
	public void caseExitMonitorStmt(ExitMonitorStmt s)
    {
		fgExpr.build(s.getOp(), s);
    }

	/**
	 * Extends the part of flow graph for the throw statement.  We just process the expression.
	 * @param s the object representing the throw statement.
	 */
	public void caseThrowStmt(ThrowStmt s)
    {
		fgExpr.build(s.getOp(), s);
    }

	/**
	 * Raise an exception for all unhandled statement form.
	 *
	 * @param obj the object representing the statement.
	 */
	public void defaultCase(Object obj)
    {
		cat.debug("Unhandled Statement:  " + obj.getClass());
    }
}
