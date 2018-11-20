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
import edu.ksu.cis.bandera.jext.AbstractBanderaValueSwitch;
import java.util.Hashtable;
import org.apache.log4j.Category;
import ca.mcgill.sable.soot.jimple.UnopExpr;
import ca.mcgill.sable.soot.jimple.BinopExpr;
import edu.ksu.cis.bandera.jext.InExpr;
import edu.ksu.cis.bandera.jext.LocalExpr;
import edu.ksu.cis.bandera.jext.LocationTestExpr;
import edu.ksu.cis.bandera.jext.LogicalAndExpr;
import edu.ksu.cis.bandera.jext.LogicalOrExpr;
import edu.ksu.cis.bandera.jext.ChooseExpr;
import edu.ksu.cis.bandera.jext.ComplementExpr;

/*
 * FGExpr.java
 * $Id: FGExpr.java,v 1.3 2002/04/01 01:04:20 rvprasad Exp $
 */

/**
 * This class provides methods to create various FGNodes for various expressions that may occur in
 * the Jimple statment.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <A HREF="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</A>
 * @version $Name:  $($Revision: 1.3 $)
 */
public class FGExpr extends AbstractBanderaValueSwitch
{
	/**
	 * Stores the common set of class tokens used during analysis.  An example would be a class
	 * corresponding to null object.
	 */
	private static Hashtable commonTokens = new Hashtable();

	/**
	 * Represents the method enclosing the expressions being processed by this object.
	 */
	private MethodVariant methodVariant;

	/**
	 * Maps AST nodes and flow graph nodes.
	 */
	private Map astNodes;

	/**
	 * Represents the expression being processed.  We shall use the the Jimple expression to denote
	 * the allocator site. This will be unique in terms of object involved and not the value of the
	 * object
	 */
	private Value value;

	/**
	 * Represents the statement in which the expression being processed occurs. <code>stmt</code> is
	 * used to map the <code>value</code> to jimple statements
	 */
	private Stmt stmt;

	/**
	 * Provides logging capabilities through log4j.
	 *
	 */
	private static Category cat;

	static {
		cat = Category.getInstance(FGExpr.class.getName());
	}

	/**
	 * Constructor of the class.
	 *
	 * @param methodVariant the method enclosing the expression being processed.
	 * @param astNodes the map of AST nodes to flow graph nodes in the method.
	 */
	public FGExpr(MethodVariant methodVariant, Map astNodes)
    {
		this.methodVariant = methodVariant;
		this.astNodes      = astNodes;
	}

	/** Note: there must be many LHSs that aren't handled now:  field access, array indexing, etc.
	 */

	/**
	 * Builds the flow graph node for the given expression.
	 *
	 * @param v the expression for which the flow graph node should be constructed.
	 * @param s the statement in which the expression occurs.
	 * @return the flow graph node associated with the expression.
	 */
	public FGNodeAST build(Value v, Stmt s)
    {
		stmt = s;
		value = v;
		BOFA.analyser.collect(v, s, methodVariant.getMethod());
		v.apply(this);
		FGNodeAST astNode = (FGNodeAST)this.getResult();
		if (astNode != null) {
			astNode.setStmt(stmt);
		} // end of if (astNode != null)
		return astNode;
    }

	/**
	 * Provides the statement enclosing the current expression being processed.
	 *
	 * @return the statement enclosing the expression being processed.
	 */
	public Stmt getCurrentStmt()
	{
		return stmt;
	}

	/*
	 * We are interested in the object values created by the expressions and not the constants
	 * generated
	 */

	/**
	 * We do not handle double values.
	 *
	 * @param v a <code>DoubleConstant</code> value
	 */
	public void caseDoubleConstant(DoubleConstant v)
    {
		setResult(null);
    }

	/**
	 * We do not handle float values.
	 *
	 * @param v a <code>FloatConstant</code> value
	 */
	public void caseFloatConstant(FloatConstant v)
    {
		setResult(null);
    }

	/**
	 * We do not handle integer values.
	 *
	 * @param v an <code>IntConstant</code> value
	 */
	public void caseIntConstant(IntConstant v)
    {
		ValueVariant valueVariant = null;
		try {
			SootClass sootClass = FA.classManager.getClass(RefType.v("int").className);
			ClassToken classToken = ClassTokenSimple.select(sootClass);

			valueVariant = ValueVariantManager.select(classToken, methodVariant, v, stmt);
		} catch (ClassFileNotFoundException e) {
			cat.warn(e.getMessage());
		} // end of try-catch

		FGNodeAST node = new FGNodeAST(v);
		FGWork work = new FGWorkSendVals(new FASet(valueVariant), node);
		FA.workList.insert(work);

		astNodes.put(v, node);
		setResult(node);
    }

	/**
	 * We do not handle long values.
	 *
	 * @param v a <code>LongConstant</code> value
	 */
	public void caseLongConstant(LongConstant v)
    {
		this.setResult(null);
    }

	/**
	 * Handles the case where the expression evaluates to a null object.  This is necessary as
	 * reference types can evaluate to null values.
	 *
	 * @param v a <code>NullConstant</code> value
	 */
	public void caseNullConstant(NullConstant v)
    {
		ValueVariant nullVariant = ValueVariantManager.select(ClassTokenSimple.nullClassToken,
															  methodVariant, v, stmt);
		FGNodeAST nullNode = new FGNodeAST(v);
		FGWork work = new FGWorkSendVals(new FASet(nullVariant), nullNode);
		FA.workList.insert(work);
		astNodes.put(v, nullNode);
		setResult(nullNode);
    }

	/**
	 * Handles the case where the expression evaluates to a string.
	 *
	 * @param v the string constant involved.
	 */
	public void caseStringConstant(StringConstant v)
    {
		SootClass sootClass =
			FA.classManager.getClass(RefType.v("java.lang.String").className);
		ClassToken classToken = ClassTokenSimple.select(sootClass);
		ValueVariant valueVariant = ValueVariantManager.select(classToken, methodVariant, v, stmt);

		// create new node with a string class object in it.
		FGNodeAST node = new FGNodeAST(v);
		FGWork work = new FGWorkSendVals(new FASet(valueVariant), node);
		FA.workList.insert(work);

		astNodes.put(v,node);
		setResult(node);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v an <code>AddExpr</code> value
	 */
	public void caseAddExpr(AddExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v an <code>AndExpr</code> value
	 */
	public void caseAndExpr(AndExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>CmpExpr</code> value
	 */
	public void caseCmpExpr(CmpExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>CmpgExpr</code> value
	 */
	public void caseCmpgExpr(CmpgExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>CmplExpr</code> value
	 */
	public void caseCmplExpr(CmplExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>DivExpr</code> value
	 */
	public void caseDivExpr(DivExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v an <code>EqExpr</code> value
	 */
	public void caseEqExpr(EqExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>GeExpr</code> value
	 */
	public void caseGeExpr(GeExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>GtExpr</code> value
	 */
	public void caseGtExpr(GtExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>LeExpr</code> value
	 */
	public void caseLeExpr(LeExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>LtExpr</code> value
	 */
	public void caseLtExpr(LtExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>MulExpr</code> value
	 */
	public void caseMulExpr(MulExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>NeExpr</code> value
	 */
	public void caseNeExpr(NeExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v an <code>OrExpr</code> value
	 */
	public void caseOrExpr(OrExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>RemExpr</code> value
	 */
	public void caseRemExpr(RemExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>ShlExpr</code> value
	 */
	public void caseShlExpr(ShlExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>ShrExpr</code> value
	 */
	public void caseShrExpr(ShrExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>SubExpr</code> value
	 */
	public void caseSubExpr(SubExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v an <code>UshrExpr</code> value
	 */
	public void caseUshrExpr(UshrExpr v)
    {
		doBinary(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>XorExpr</code> value
	 */
	public void caseXorExpr(XorExpr v)
    {
		doBinary(v);
    }

	/**
	 * Handles the case when the expression is an array reference on RHS.
	 *
	 * @param v the expression for array reference.
	 */
	public void caseArrayRef(ArrayRef v)
    {
		FGNode baseNode = build(v.getBase(), stmt);

		// The "type" attribute of the array ref AST is the component type of the array.  Make an
		// array type to use in ClassTokenArray.select
		ArrayType arrayType = ClassTokenArray.componentToArrayType(v.getType());

		ClassTokenArray classTokenArray = ClassTokenArray.select(arrayType);
		// Here, we need to attach an action to the flowgraph as in method invocation.
		FGNodeAST arrayRefNode = new FGNodeAST(v);
		/* generate action: Different array objects (represented by value variants) may flow into the
		 *  base node.
		 *
		 * For any new value variant that flows into the base node, the array variant FGNode that
		 * summarizes the contents of the array will be found and an FG arc will be made from the
		 * contents node to the AST node for the current array reference AST node
		 */
		baseNode.addAction(new FGActionArrayRef(classTokenArray, arrayRefNode, methodVariant));
		setResult(arrayRefNode);
		astNodes.put(v,arrayRefNode);
    }

	/**
	 * Handles the case when the expression is an interface invoke expression.
	 *
	 * @param v the expression for the interface invocation
	 */
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v)
    {
		/*
		 * We assume that the analysis needs to be done on object of the class that implements the
		 * interface.  So, it is reasonable to treat a call to a method inherited from an interface
		 * as equivalent to virtual invoke.
		 */
		SootMethod sootMethod     = v.getMethod();
		int        argCount       = v.getArgCount();
		FGNode     baseNode       = build(v.getBase(), stmt);

		/*
		 * With interface method invoke, we generate flowgraphs for subexpressions (base and args),
		 * but arg value and return flows cannot be connected now.  An action is attached to the node
		 * that will fire each time a value flows into the node.  When a new value flows in, the
		 * action will be fired.
		 */

		// Now loop through args and generate graphs for the arcs.
		FGNode[] argNodes = new FGNode[argCount];

		for (int i=0; i<argCount; i++) {
			argNodes[i] = build(v.getArg(i), stmt);
		}

		FGNodeAST invokeNode = new FGNodeAST(v);

		// generate action
		baseNode.addAction(new FGActionInvoke(argCount, argNodes, invokeNode,
											  methodVariant.getMethod()));
		setResult(invokeNode);
		astNodes.put(v,invokeNode);
    }

	/**
	 * Handles the case when the expression is an special invoke expression.
	 *
	 * @param v the expression for the special invocation.
	 */
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v)
    {
		/*
		 * invokespecial does not leave a value on the stack and so we don't need to do anything with
		 * a return value here, i.e., there is no need to hook something into the flow graph for this
		 * node.
		 */

		/* The specialinvoke is just another method invocation with no return values.  We can treat
		 * them as static methods at class level, but this would result in polluting the value space
		 * for all objects of a class.  So, it is better to treat them like virtual invoke in the
		 * context of object flow analysis even though the exact method implementation to be executed
		 * is embedded in the invoke expression and may not correspond to the static or dynamic type
		 * of the receiver, but may be to it's super type.
		 */

		/* The specialinvoke is just another method invocation with no return value.  We can treat
		 * them as virtual methods but only that there is not such need as we already know the
		 * implementation that will be invoked.  Hence, it is better to plug in the flow graph for
		 * the method now.
		 */
		SootMethod sootMethod     = v.getMethod();
		int        argCount       = v.getArgCount();
		FGNode     baseNode       = build(v.getBase(), stmt);

		/*
		 * With virtual method invoke, we generate flowgraphs for subexpressions (base and args), but
		 * arg value and return flows cannot be connected now.  An action is attached to the node
		 * that will fire each time a value flows into the node.  When a new value flows in, the
		 * action will be fired.
		 */

		// Now loop through args and generate graphs for the arcs.
		FGNode[] argNodes = new FGNode[argCount];

		for (int i=0; i<argCount; i++) {
			argNodes[i] = build(v.getArg(i), stmt);
		}

		FGNodeAST invokeNode = new FGNodeAST(v);

		// generate action to handle constructor call
		baseNode.addAction(new FGActionInvoke(argCount, argNodes, invokeNode,
											  methodVariant.getMethod()));
		setResult(invokeNode);
		astNodes.put(v,invokeNode);
    }

	/*
	 * With static invoke, no action needs to be generated.  We can simple connect the arcs.
	 * However, there is a question of when the call graph gets built.  Should it be built now, in a
	 * depth-first manner?  In fact, if this is simply a reference to another stored class, then we
	 * would have already generated the flowgraph for that method (or we will generate it).
	 *
	 * Well, hmmm, maybe it should still be action driven.  No the variant manager should take care
	 * of that caching the generation of flowgraphs in any order.
	 */

	/**
	 * Handles the case when the expression is an static invoke expression.
	 *
	 * @param v the expression for the invocation
	 */
	public void caseStaticInvokeExpr(StaticInvokeExpr v)
    {
		SootMethod sootMethod     = v.getMethod();
		int        argCount       = v.getArgCount();
		FGNode[]   argNodes       = new FGNode[argCount];
		FGNode paramNode;

		// assume argCount matches method parameter count
		MethodVariant methodVariant  = MethodVariantManager.select(sootMethod);

		/* Now loop through args and generate graphs for the arcs, and
		 * make arcs from each arg to corresponding parameter node.
		 */
		for (int i=0; i<argCount; i++) {
			argNodes[i]      = build(v.getArg(i), stmt);
			paramNode = methodVariant.getParameterNode(i);
			FGNode.makeArc(argNodes[i], paramNode);
		}

		FGNodeAST invokeNode = new FGNodeAST(v);
		/* make arc from last node of method to AST node. */
		FGNode lastNode = methodVariant.getReturnNode();
		FGNode.makeArc(lastNode, invokeNode);

		astNodes.put(v,invokeNode);
		setResult(invokeNode);
		BOFA.flowProcessingCallBackReg.callStaticInvoke(v, this.methodVariant.getMethod());
    }

	/**
	 * Handles the case when the expression is an virtual invoke expression.
	 *
	 * @param v the expression for the invocation
	 */
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v)
    {
		SootMethod sootMethod     = v.getMethod();
		int        argCount       = v.getArgCount();
		FGNode     baseNode       = build(v.getBase(), stmt);

		/*
		 * With virtual method invoke, we generate flowgraphs for subexpressions (base and args), but
		 * arg value and return flows cannot be connected now.  An action is attached to the node
		 * that will fire each time a value flows into the node.  When a new value flows in, the
		 * action will be fired.
		 */

		// Now loop through args and generate graphs for the arcs.
		FGNode[] argNodes = new FGNode[argCount];

		for (int i=0; i<argCount; i++) {
			argNodes[i] = build(v.getArg(i), stmt);
		}

		FGNodeAST invokeNode = new FGNodeAST(v);

		// generate action
		baseNode.addAction(new FGActionInvoke(argCount, argNodes, invokeNode,
											  methodVariant.getMethod()));
		setResult(invokeNode);
		astNodes.put(v,invokeNode);
		BOFA.fgConstructCallBackReg.callNonStaticInvoke(v, this);
	}

	/**
	 * Handles a case when the expression is a cast expression.
	 *
	 * @param v the cast expression.
	 */
	public void caseCastExpr(CastExpr v)
    {
		/*
		 * Since we donot perform type checking in our analysis, we just pass on all the values
		 * flowing into the base node into the node corresponding to the cast expression.  We achieve
		 * this by just creating an arc.
		 */
		FGNode baseNode = build(v.getOp(), stmt);
		FGNodeAST node = new FGNodeAST(v);
		FGNode.makeArc(baseNode, node);
		setResult(node);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v an <code>InstanceOfExpr</code> value
	 */
	public void caseInstanceOfExpr(InstanceOfExpr v)
    {
		defaultCase(v);
    }

	/**
	 * Handles the case when the expression is an new array object creation
	 * expression.
	 *
	 * @param v the array object creation expression.
	 */
	public void caseNewArrayExpr(NewArrayExpr v)
    {
		ArrayType  arrayType = (ArrayType) v.getType();
		// We only want to process array creation expressions of ref type
		if (!(arrayType.baseType instanceof RefType)) {
			setResult(null);
			return;
		} // end of if (!(arrayType.baseType instanceof RefType))

		ClassToken classToken = ClassTokenArray.select(arrayType);

		/*
		 * WARNING: the interface to ValueVariantManager is not completely settled, since we may want
		 * to adjust the way that events/code positions are indexed.  For now, the third argument is
		 * some int representing a position in the code.
		 */
		ValueVariant valueVariant = ValueVariantManager.select(classToken, methodVariant, v, stmt);
		FGNodeAST node = new FGNodeAST(v);
		FA.workList.insert(new FGWorkSendVals(new FASet(valueVariant), node));

		/*
		 * Note: at this point we have NOT created the flow graph node corresponding to the
		 * components of the newly created array.  I'm not sure if this is the right thing to do, but
		 * it follows what we are currently doing for instance variables of newly created instances.
		 */
		astNodes.put(v,node);
		setResult(node);

		/*
		 * Get an arrayVariant for the given class of array and new arrayVariant that needs to be
		 * created by the new expression.
		 */
		ArrayVariant arrayVariant = ArrayVariantManager.select(classToken, valueVariant);
		/*
		 * We get the element type of the array object and create a constant(value) of that type.  We
		 * obtain a value variant for this constant and push this value into the values set of
		 * arrayNode.  We donot want to create a new SootClass for the base type whereas we need to
		 * use the one already in use.
		 */
		valueVariant = ValueVariantManager.select(ClassTokenSimple.nullClassToken, methodVariant, v,
												  stmt);
		Value temp = NullConstant.v();


		/* we can use getToken(elementType) instead of
		 * ClassTokenSimple.select(new SootClass(elementType)) for the
		 * following case
		 * arr = new t[3];  (1)
		 * i = arr;
		 * arr1 = new t[4]; (2)
		 * it will only contain one null value resulting from site (1) and
		 * not (2).  It will contain null value from site (1) and (2) if
		 * ClassTokenSimple.select(elementClass) is used. */
		ClassTokenSimple elementClass =	ClassTokenSimple.nullClassToken;
		valueVariant = ValueVariantManager.select(elementClass, methodVariant, temp, stmt);
		BOFA.analyser.collect(temp, stmt, methodVariant.getMethod());

		arrayVariant.getNode().values.add(valueVariant);
    }

	/**
	 * Handles the case when the expression is an new multidimensional array object creation
	 * expression.
	 *
	 * @param v the multidimensional array expression.
	 */
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v)
    {
		ArrayType  arrayType = (ArrayType) v.getType();
		if (!(arrayType.baseType instanceof RefType)) {
			setResult(null);
			return;
		} // end of if (!(arrayType.baseType instanceof RefType))

		ClassToken classToken = ClassTokenArray.select(arrayType);

		/*
		 * WARNING: the interface to ValueVariantManager is not completely settled, since we may want
		 * to adjust the way that events/code positions are indexed.  For now, the third argument is
		 * some int representing a position in the code.
		 */
		ValueVariant valueVariant = ValueVariantManager.select(classToken, methodVariant, v, stmt);

		FGNodeAST node = new FGNodeAST(v);
		FGWork work = new FGWorkSendVals(new FASet(valueVariant), node);
		FA.workList.insert(work);

		/*
		 * Note: at this point we have NOT created the flow graph node corresponding to the
		 * components of the newly created array.  I'm not sure if this is the right thing to do, but
		 * it follows what we are currently doing for instance variables of newly created instances.
		 */
		astNodes.put(v,node);
		setResult(node);

		/*
		 * v.getSizeCount() gives us the number of dimensions for which size has been specified.
		 * During declaring new array, the sizes of all the dimension following the dimension whose
		 * size is unspecified should also be unspecified.  So, it is safe only to plug in the values
		 * for dimensions whose size is specified.
		 */
		int numSpecDim = v.getSizeCount();
		int count;
		Value valueOfCountDim;
		ArrayType componentType;
		ArrayVariant arrayVariant;

		for (int j = arrayType.numDimensions - 1; j > arrayType.numDimensions -
			 numSpecDim; j--) {
			// Create a new ArrayType for the immediate component array object, an array less by one
			// dimension.
			componentType = arrayType.v(arrayType.baseType, j);

			// Get an arrayVariant for the given class of array and new arrayVariant that needs to be
			// created by the new expression.
			arrayVariant = ArrayVariantManager.select(classToken, valueVariant);
			// Get a new ClassToken for component type of array
			classToken = ClassTokenArray.select(componentType);
			// Obtain the valuevariant and inject it into the arrayNode value set
			valueVariant = ValueVariantManager.select(classToken, methodVariant, value, stmt);
			// inject the valuevariant into the value set of the arrayvariant
			arrayVariant.getNode().values.add(valueVariant);
		} // end of for ()

		// if the new expressions does specify the size of all dimension then we need to inject the
		// value for the base type of the array object.
		if ( numSpecDim == arrayType.numDimensions) {
			// Get an arrayVariant for the last array component
			arrayVariant = ArrayVariantManager.select(classToken, valueVariant);
			// Get a new Value object for the base type's default initial value
			Value value = NullConstant.v();
			/*
			 * Get the classToken for the base type.  To do so we must use the SootClass
			 * corresponding to the base type and should not create a new SootClass. Also obtain the
			 * valuevariant and inject it into the arrayNode value set.
			 */
			valueVariant = ValueVariantManager.select(ClassTokenSimple.nullClassToken, methodVariant,
													  value, stmt);
			BOFA.analyser.collect(value, stmt, methodVariant.getMethod());

			// inject the value into the value set associated with the arrayvariant
			arrayVariant.getNode().values.add(valueVariant);
		} // end of if ()
    }

	/**
	 * Handles the case when the expression is an new object creation expression.
	 *
	 * @param v the object creation expression.
	 */
	public void caseNewExpr(NewExpr v)
    {
		RefType      refType   = v.getBaseType();
		String       className = refType.className;

		/*
		 * If this class is "external" (something that we don't want to analyze the code for, we
		 * could insert a marker here and avoid calling the class manager (which loads the class).
		 */
		SootClass    sootClass = FA.classManager.getClass(className);
		ClassToken   classToken = ClassTokenSimple.select(sootClass);

		/*
		 * WARNING: the interface to ValueVariantManager is not completely settled, since we may want
		 * to adjust the way that events/code positions are indexed.  For now, the third argument is
		 * some int representing a position in the code.
		 */
		ValueVariant valueVariant = ValueVariantManager.select(classToken, methodVariant, v, stmt);

		FGNodeAST node = new FGNodeAST(v);

		FGWork work = new FGWorkSendVals(new FASet(valueVariant), node);

		FA.workList.insert(work);

		astNodes.put(v,node);
		setResult(node);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>LengthExpr</code> value
	 */
	public void caseLengthExpr(LengthExpr v)
    {
		defaultCase(v);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>NegExpr</code> value
	 */
	public void caseNegExpr(NegExpr v)
    {
		doUnary(v);
    }

	/*
	 * Create a node for this expression.  For each ValueVariant flowing into the base, create an arc
	 * from the instance variable of that class into this node.
	 *
	 * Currently, we ignore inheritance and keep track of only one variant of the instance.  The
	 * instance in the declaring class.  This means that we can totally avoid examining the set of
	 * values found in the base expression. (This will probably result in a lot of imprecision.
	 */

	/**
	 * Handles the case when the expression is an instance field reference expression.
	 *
	 * @param v the instance field reference expression.
	 */
	public void caseInstanceFieldRef(InstanceFieldRef v)
    {
		SootField sootField = v.getField();
		InstanceVariant instanceVariant = InstanceVariantManager.select(sootField);
		FGNodeAST astNode   = new FGNodeAST(v);
		FGNode.makeArc(instanceVariant.getNode(), astNode);
		astNodes.put(v,astNode);

		setResult(astNode);
    }

	/**
	 * Handles the case when the expression is an local variable reference expression.
	 *
	 * @param v the local variable reference expression.
	 */
	public void caseLocal(Local v)
    {
		FGNodeLocal localNode = methodVariant.getLocalNode(v);
		FGNodeAST   astNode   = new FGNodeAST(v);
		FGNode.makeArc(localNode, astNode);
		astNodes.put(v, astNode);

		setResult(astNode);
    }

	/**
	 * Handles the case when the expression is an parameter reference expression.
	 *
	 * @param v the parameter reference expression.
	 */
	public void caseParameterRef(ParameterRef v)
    {
		FGNodeParameter parameterNode = methodVariant.getParameterNode(v.getIndex());
		FGNodeAST astNode    = new FGNodeAST(v);
		FGNode.makeArc(parameterNode, astNode);
		astNodes.put(v,astNode);

		setResult(astNode);
    }

	/**
	 * We donot handle expressions that do not evaluate to objects.
	 *
	 * @param v a <code>CaughtExceptionRef</code> value
	 */
	public void caseCaughtExceptionRef(CaughtExceptionRef v)
    {
		defaultCase(v);
    }

	/**
	 * Handles the case when the expression is an "this" variable reference expression.
	 *
	 * @param v the "this" variable reference expression.
	 */
	public void caseThisRef(ThisRef v)
    {
		FGNodeThis thisNode  = methodVariant.getThisNode();
		FGNodeAST astNode   = new FGNodeAST(v);
		FGNode.makeArc(thisNode, astNode);
		astNodes.put(v,astNode);

		setResult(astNode);
    }

	/**
	 * Handles the case when the expression is an static field reference expression.
	 *
	 * @param v the static field reference expression.
	 */
	public void caseStaticFieldRef(StaticFieldRef v)
    {
		SootField sootField = v.getField();
		FGNodeAST astNode   = new FGNodeAST(v);
		FGNode.makeArc(StaticFieldManager.select(sootField), astNode);
		astNodes.put(v, astNode);

		setResult(astNode);
    }

	public void caseChooseExpr(ChooseExpr v) {
		List temp = v.getChoices();
		FGNodeAST astNode = new FGNodeAST(v);
		for (Iterator i = temp.iterator(); i.hasNext();) {
			 Value t = (Value)i.next();
			 build(t, stmt);
			 FGNode arg = (FGNode)getResult();
			 FGNode.makeArc(arg, astNode);
		} // end of for (Iterator i = temp.iterator(); i.hasNext();)
		astNodes.put(v, astNode);
		setResult(astNode);
	}

	public void caseComplementExpr(ComplementExpr v) {
		FGNodeAST astNode = new FGNodeAST(v);
		build(v.getOp(), stmt);
		FGNode.makeArc((FGNode)getResult(), astNode);
		astNodes.put(v, astNode);
		setResult(astNode);
	}

	public void caseInExpr(InExpr v) {
		doBinary(v);
	}

	public void caseLocalExpr(LocalExpr v) {
		FGNodeAST astNode = new FGNodeAST(v);
		build(v.getLocal(), stmt);
		FGNode.makeArc((FGNode)getResult(), astNode);
		astNodes.put(v, astNode);
		setResult(astNode);
	}

	public void caseLocationTestExpr(LocationTestExpr v) {
		cat.error("LocationTestExpr is unhandled.");
	}

	public void caseLogicalAndExpr(LogicalAndExpr v) {
		doBinary(v);
	}

	public void caseLogicalOrExpr(LogicalOrExpr v) {
		doBinary(v);
	}

	/**
	 * Handles the cases which BOFA is not interested in.
	 *
	 * @param v an <code>Object</code> value
	 */
	public void defaultCase(Object v)
    {
		cat.debug("Unhandled expression in" + " FGExpr:" + v.getClass() + " " + v);
    }

	private void doBinary(BinopExpr v) {
		build(v.getOp1(), stmt);
		build(v.getOp2(), stmt);
		setResult(null);
	}

	private void doUnary(UnopExpr v) {
		build(v.getOp(), stmt);
		setResult(null);
	}


	/**
	 * Returns ClassTokenSimple object for the class, type.  A New ClassToken is created if one
	 * doesnot exist of the given class and registered along with type.  If a ClassToken already
	 * exists then it is returned.
	 * @param type the class for which the ClassToken is requested.
	 *
	 * @return the class token associated with the given type.
	 */
	protected static ClassTokenSimple getToken(String type)
	{
		if (!commonTokens.containsKey(type)) {
			commonTokens.put(type, ClassTokenSimple.select(new SootClass(type)));
		} // end of if
		return (ClassTokenSimple)commonTokens.get(type);
	}
}
