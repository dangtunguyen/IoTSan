/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000, 2001i, 2002                                   *
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



import ca.mcgill.sable.soot.NullType;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.jimple.AbstractJimpleValueSwitch;
import ca.mcgill.sable.soot.jimple.ArrayRef;
import ca.mcgill.sable.soot.jimple.InstanceFieldRef;
import ca.mcgill.sable.soot.jimple.InvokeExpr;
import ca.mcgill.sable.soot.jimple.Local;
import ca.mcgill.sable.soot.jimple.NonStaticInvokeExpr;
import ca.mcgill.sable.soot.jimple.ParameterRef;
import ca.mcgill.sable.soot.jimple.SpecialInvokeExpr;
import ca.mcgill.sable.soot.jimple.StaticFieldRef;
import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.soot.jimple.ThisRef;
import ca.mcgill.sable.soot.jimple.Value;
import ca.mcgill.sable.util.ArrayList;
import ca.mcgill.sable.util.ArraySet;
import ca.mcgill.sable.util.Collection;
import ca.mcgill.sable.util.Comparator;
import ca.mcgill.sable.util.HashMap;
import ca.mcgill.sable.util.HashSet;
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.LinkedList;
import ca.mcgill.sable.util.Map;
import ca.mcgill.sable.util.Set;
import java.sql.Ref;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/*
 * Analysis.java
 * $Id: Analysis.java,v 1.3 2002/03/20 02:54:05 rvprasad Exp $
 */

/**
 * This class is the interface provided by BOFA to the external world.
 *
 * The nasty details of Object Flow engine is hidden behind this class.  This class provides the
 * necessary interface using types defined in ca.mcgill.sable.* packages, in particular, soot and
 * soot.jimple, to query the Object Flow Analysis engine for information.
 *
 * Created: Wed Nov 15 22:15:07 2000
 *
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version  $Name:  $($Revision: 1.3 $)
 */
public class Analysis {

	/**
	 * Stores the singleton object corresponding to the Analysis class.
	 */
	private static Analysis singleton;

	/**
	 * Represents the callgraph of the system being analyzed.
	 *
	 */
	private final CallGraph callGraph;

	/**
	 * Captures the various start to run method pairing possible through java.lang.Thread.start()
	 * method call.
	 *
	 */
	private final StartRunCollectCallBack startrunCollector;

	/**
	 * Provides logging capability through log4j.
	 *
	 */
	private static Logger logger;

	/**
	 * Indicates that the "defining" statements are required.
	 *
	 */
	private final static int DEFS = 1;

	/**
	 * Indicates that the "using" statement are required.
	 *
	 */
	private final static int USES = 2;

	/**
	 * Describe variable <code>valueStmtMap</code> here.
	 *
	 */
	private final Map valueStmtMap;

	/**
	 * Describe variable <code>stmtMethodMap</code> here.import org.apache.log4j.Category;

	 *
	 */
	private final Map stmtMethodMap;

	private final RefValueSwitch refValueSwitch = new RefValueSwitch();

	/**
	 * Creates a new <code>Analysis</code> instance.  This a private constructor to implement the
	 * <i>singleton</i> pattern.
	 */
	private Analysis() {
		callGraph = CallGraph.init(this);
		startrunCollector = new StartRunCollectCallBack();
		logger = LogManager.getLogger(Analysis.class);
		valueStmtMap = new HashMap();
		stmtMethodMap = new HashMap();
	}

	/**
	 * The singleton object corresponding to the Analysis class is created here.
	 *
	 * @return an Analysis object.
	 */
	public static Analysis init()
	{
		if (singleton == null) {
			singleton = new Analysis();
		} // end of if ()
		return singleton;
	}

	/**
	 * This is the hook to maintain the value, statement, and method map.
	 *
	 * @param v the value object in Jimple.
	 * @param s the statement containing the value, <code>v</code>.
	 * @param sm the method containing the statement, <code>s</code>.
	 */
	public void collect(Value v, Stmt s, SootMethod sm)
	{
		valueStmtMap.put(v, s);
		stmtMethodMap.put(s, sm);
	}

	/**
	 * Provides the statement containing the given value.
	 *
	 * @param v is the value to be tracked.
	 * @return the statement containing the given value.
	 */
	public Stmt getEnclosingStmt(Value v)
	{
		return (Stmt)valueStmtMap.get(v);
	}

	/**
	 * Provides the method containing the given statement.
	 *
	 * @param s is the statement to be tracked.
	 * @return the method containing the given statement.
	 */
	public SootMethod getEnclosingMethod(Stmt s)
	{
		return (SootMethod)stmtMethodMap.get(s);
	}

	/**
	 * Registers various flow graph modifying call backs.
	 *
	 * @param cbr call back registry tracking the flow graph modifying call
	 * backs.
	 */
	public void callbackInitFG(CallBackRegistry cbr)
	{
		/*
		 * we are at present interested in only the run method defined by classes extending Thread.
		 * We may include stuff for analysing start() and stop() methods of Applets.
		 */
		cbr.regNonStaticInvoke(new CallByEnvCallBack());
	}

	/**
	 * Registers call backs which collect information as flow analysis is in progress.
	 *
	 * @param cbr a <code>CallBackRegistry</code> value
	 */
	public void callbackInitFA(CallBackRegistry cbr)
	{
		/*
		 * we will insert hooks to be invoked when a new method implementation is called to extend
		 * the call graph.
		 */
		cbr.regNonStaticInvoke(callGraph.getNonStaticCallBack());
		cbr.regStaticInvoke(callGraph.getStaticCallBack());
		// We now insert hooks to collect start-run pairs at start() call sites.
		cbr.regNonStaticInvoke(startrunCollector);
	}

	/**
	 * Reset various data structures.  Primarily the call backs.
	 *
	 */
	public void reset()
	{
		callGraph.reset();
		startrunCollector.reset();
		valueStmtMap.clear();
		stmtMethodMap.clear();
	}

	/**
	 * Provides a map of the invoke expression and the corresponding set of sootMethods.
	 *
	 * @param caller The <code>SootMethod</code> in which the call occurs.
	 * @return a <code>Map</code> from <code>Value</code> to as <code>Set</code> of
	 * <code>SootMethod</code>.
	 */
	public Map getExprCalleeMap(SootMethod caller) {
        Set callees = getCallees(caller);
		ExprStmtMethodTriple esmt;
		Map temp = new HashMap();
		Set set;
		Value v;
		for (Iterator i = callees.iterator(); i.hasNext();) {
			 esmt = (ExprStmtMethodTriple)i.next();
			 v = esmt.getExpr();
			 if (temp.containsKey(v)) {
				 set = (Set)temp.get(v);
			 } // end of (temp.containsKey(esmt.getExpr())) if
			 else {
				 set = new HashSet();
				 temp.put(v, set);
			 } // end of (temp.containsKey(esmt.getExpr())) else
			 set.add(esmt.getSootMethod());
		} // end of for (Iterator i = callees.iterator(); i.hasNext();)
		return temp;
	}

	/**
	 * Provides a set of methods called from this method.
	 *
	 * @param caller the calling method.
	 * @return a set of <code>ExprStmtMethodTriple</code>s containing the method called and the site
	 * at which it was called.
	 */
	public Set getCallees(SootMethod caller)
	{
		return callGraph.getCallees(caller);
	}

	/**
	 * Provides a set of methods which call this method.
	 *
	 * @param callee a <code>SootMethod</code> value
	 * @return a set of caller<code>ExprStmtMethodTriple</code>s containing the calling method and
	 * the site at which it called.
	 */
	public Set getCallers(SootMethod callee)
	{
		return callGraph.getCallers(callee);
	}

	/**
	 * Provides a collection of the run methods that will be invoked as a result of a start() call on
	 * java.lang.Thread and it's subclasses.
	 *
	 * @param e is the invoke expression containing the <code>start</code> call.
	 * @return a collection of <code>SootMethod</code> objects representing the run method
	 * implementation that will be invoked as a result of the given start() call.
	 */
	public Collection getRuns(InvokeExpr e)
	{
		return startrunCollector.getRuns(e);
	}

	/**
	 * Provides a <code>StmtMethodPair</code> object containing the given statement and the method
	 * enclosing the statement.
	 *
	 * @param s the statement for which the enclosing method is requested.
	 * @return a <code>StmtMethodPair</code> object containing the given statement and it's enclosing
	 * method, if one exists.  Otherwise the method will be null in the returned object.  This should
	 * not occur as all statements in Java should occur * inside a method at the JVM level.
	 */
	public StmtMethodPair getStmtMethodPair(Stmt s)
	{
		return new StmtMethodPair(s, (SootMethod)stmtMethodMap.get(s));
	}

    /**
	 * Provides a <code>ExprStmtMethodTriple</code> object containing the given value, containing
	 * statement, and the method enclosing the statement.
	 *
	 * @param v the value for which the enclosing statement and method are requested.
	 * @return a <code>ExprStmtMethodTriple</code> object containing the given value, it's containing
	 * statement and it's enclosing method, if one exists. Otherwise the statement and method will be
	 * null in the returned object.  This should not occur as all statements in Java should occur
	 * inside a method at the JVM level.
	 */
	public ExprStmtMethodTriple getExprStmtMethodTriple(Value v)
	{
		return new ExprStmtMethodTriple(v, getStmtMethodPair((Stmt)valueStmtMap.get(v)));
	}

	/**
	 * This class is acts as a large switch statement.
	 *
	 * The switch is implemented in the parent class but the code to be executed is provided in this
	 * class.  In each of the cases, a particular type of reference is handled.
	 *
	 * AbstractJimpleValueSwitch is extended because RefValueSwitch does not handle switching on
	 * objects of class Local.
	 *
	 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
	 */
	class RefValueSwitch extends AbstractJimpleValueSwitch
	{
		/**
		 * A collection of values which the given reference or <code>Local</code> can refer to.
		 *
		 */
		Collection result;

		/**
		 * The <code>MethodVariant</code> corresponding to a SootMethod.
		 *
		 */
		MethodVariant method;

		/**
		 * This is entry method which builds the collection of expression <code>Value</code>s which
		 * the reference might evaluate to.
		 *
		 * @param v the value of interest.  Mostly <code>Local</code>s and <code>Ref</code> types.
		 * @param sm the method in which the value occurs.
		 * @return a collection of Jimple Values which the given reference/locals will evaluate to.
		 */
		Set build(Value v, SootMethod sm)
		{
			method = MethodVariantManager.select(sm);
			result = null;
			v.apply(this);
			return collect();
		}

		/**
		 * This method collects the <code>Value</code>s from the <code>ValueVariant</code> objects.
		 *
		 * After the <code>ValueVariant</code>s are collected for the given <code>Value</code>
		 * object, <code>Value</code> objects need to be extracted from them to keep the transaction
		 * with the external world in terms of Jimple entities.  This will be called from build.
		 *
		 * @return a collection of <code>Value</code>s that may flow into the <code>Value</code>
		 * given to <code>build</code>.
		 * @see #build build
		 */
		private Set collect()
		{
			Set temp;
			temp = new ArraySet();
			if (result != null) {
				for (Iterator i = result.iterator(); i.hasNext();) {
					ValueVariant v = (ValueVariant)i.next();
					temp.add(getExprStmtMethodTriple(v.getExpr()));
				} // end of for ()
			}
			return temp;
		}

		/**
		 * This method collects <code>Value</code> objects for the given <code>ArrayRef</code>
		 * object.
		 *
		 * @param v <code>ArrayRef</code> representing the array variable.
		 */
		public void caseArrayRef(ArrayRef v)
		{
			result = method.getASTValues(v);
		}

		/**
		 * This method collects <code>Value</code> objects for the given
		 * <code>InstanceFieldRef</code> object.
		 *
		 * @param v <code>InstanceFieldRef</code> representing the instance field.
		 */
		public void caseInstanceFieldRef(InstanceFieldRef v)
		{
			Collection temp = InstanceVariantManager.getVariants(v.getField());
			result = new ArraySet();
			for (Iterator i = temp.iterator(); i.hasNext();) {
				InstanceVariant t = (InstanceVariant)i.next();
				result.addAll(t.getNode() .getValues());
			} // end of for ()
		}

		/**
		 * This method gets the collection of Value objects for the given <code>ParameterRef</code>
		 * object.
		 *
		 * @param v <code>ParameterRef</code> representing the parameter.
		 */
		public void caseParameterRef(ParameterRef v)
		{
			result = method.getParameterValues(v.getIndex());
		}

		/**
		 * This method gets the collection of Value objects for the given <code>StaticFieldRef</code>
		 * object.
		 *
		 * @param v <code>StaticFieldRef</code> representing the static field.
		 */
		public void caseStaticFieldRef(StaticFieldRef v)
		{
			result = StaticFieldManager.get(v.getField()).getValues();
		}

		/**
		 * This method gets the collection of Value objects for the given <code>ThisRef</code>
		 * object.
		 *
		 * @param v <code>ThisRef</code> representing <i>this</i> variable.
		 */
		public void caseThisRef(ThisRef v)
		{
			result = method.getThisValues();
		}

		/**
		 * This method gets the collection of Value objects for the given <code>Local</code> object.
		 *
		 * @param v <code>Value</code> representing the local variable.
		 */
		public void caseLocal(Local v)
		{
			result = method.getLocalValues(v);
		}

		/**
		 * For all the cases which we donot provide value set information, we execute this method.
		 *
		 * @param o Any object type for which we donot cater the data.
		 */
		public void defaultCase(Object o)
		{
			logger.warn("ANALYSIS: We donot handle " + o + " of type " + o.getClass() + ".");
		}
	}

	/**
	 * Provides a set of <code>Stmt</code>(from the soot library)in which the given
	 * <code>Value</code> is defined or used.  A copy of annotationmanager is obtained from
	 * compilationmanager to provide the information.
	 *
	 * @param v the reference variable or local which is of interest.
	 * @param enclosingMethod the method in which the reference variable or local occurs.
	 * @param mode defines the nature of the return value.  It should be USES or DEFS only.
	 * @return a collection of "defining" or "using" <code>Stmt</code>s.
	 */
	private Collection getDefsOrUses(Value v, SootMethod enclosingMethod, int mode) {
		Set ud = new ArraySet();
		Collection c = new ArrayList();
		Collection temp = new ArrayList();
		MethodVariant m =  MethodVariantManager.select(enclosingMethod);
		FGNode f = null;
		if (v instanceof ThisRef) {
			temp.add(m.getThisNode());
		} else if  (v instanceof ParameterRef) {
			temp.add(m.getParameterNode(((ParameterRef)v).getIndex()));
		} else if (v instanceof InstanceFieldRef) {
			temp.add(InstanceVariantManager .select(((InstanceFieldRef)v).getField()).getNode());
		} else if (v instanceof ArrayRef) {
			temp.addAll(m.getASTNode(v).getPreds());
		} else if (v instanceof StaticFieldRef) {
			temp.add(StaticFieldManager.select(((StaticFieldRef)v).getField()));
		} else if (v instanceof Local) {
			temp.add(m.getLocalNode((Local)v));
		} // end of else

		if (mode == DEFS) {
			for (Iterator i = temp.iterator(); i.hasNext();) {
				c.addAll(((FGNode)i.next()).getPreds());
			} // end of for
		} else if (mode == USES) {
			for (Iterator i = temp.iterator(); i.hasNext();) {
				c.addAll(((FGNode)i.next()).getSuccs());
			} // end of for
		} // end of else

		for (Iterator i =  c.iterator(); i.hasNext();) {
			Stmt s = ((FGNodeAST)i.next()).getStmt();
			ud.add(getStmtMethodPair(s));
		}
		return ud;
	}

	/**
	 * A class that encapsulates the statement and the method in which that statement occurs.
	 *
	 */
	public class StmtMethodPair {
		/**
		 * The statement to be paired with the containing Method.
		 *
		 */
		Stmt stmt;

		/**
		 * The method to be paired with the enclosed statement.
		 *
		 */
		SootMethod sm;

		/**
		 * Creates a new <code>StmtMethodPair</code> instance.
		 *
		 * @param s statement to be stored.
		 * @param m method to be stored.
		 */
		StmtMethodPair(Stmt s, SootMethod m)
		{
			stmt = s;
			sm = m;
		}

		/**
		 * Provides the stored statment object.
		 *
		 * @return statement stored in this object.
		 */
		public Stmt getStmt()
		{
			return stmt;
		}

		/**
		 * Provides the stored method object.
		 *
		 * @return method stored in this object.
		 */
		public SootMethod getSootMethod()
		{
			return sm;
		}

		/**
		 * Provides equality checking for two <code>StmtMethodPair</code> objects.  It checks if both
		 * the objects contain references to the same <code>Stmt</code> and <code>SootMethod</code>
		 * objects.
		 *
		 * @param smp the <code>StmtMethodPair</code> object to be compared with.
		 * @return <code>true</code> if the given object is the "equal" to this
		 * object. <code>false</code> otherwise.
		 */
		public boolean equals(Object smp)
		{
			boolean ret;
			if (smp instanceof StmtMethodPair) {
				StmtMethodPair temp = (StmtMethodPair) smp;
				ret = (stmt == temp.stmt && sm == temp.sm);
			} else {
				ret = this == smp;
			} // end of else
			return ret;
		}

		public String toString()
		{
			return "STMT: " + stmt + " - METHOD: " + sm;
		}
	}

	/**
	 * A class the encapsulates the expression and it's enclosinging statement and method.
	 *
	 */
	public class ExprStmtMethodTriple
	{
		/**
		 * The expression couple with the statement and method.
		 *
		 */
		Value expr;

		/**
		 * The object containing the enclosing statement and method.
		 *
		 */
		StmtMethodPair smp;

		/**
		 * Creates a new <code>ExprStmtMethodTriple</code> instance.
		 *
		 * @param expr is the expression to be encapsulated.
		 * @param smp is the <code>StmtMethodPair</code> object conatining the enclsoing statement
		 * and method.
		 */
		ExprStmtMethodTriple(Value expr, StmtMethodPair smp)
		{
			this.expr = expr;
			this.smp = smp;
		}

		/**
		 * Returns the stored expression.
		 *
		 * @return returns the stored expression.
		 */
		public Value getExpr()
		{
			return expr;
		}

		/**
		 * Returns the associated <code>StmtMethodPair</code> object.
		 *
		 * @return the associated <code>StmtMethodPair</code> object.
		 */
		public StmtMethodPair getStmtMethodPair()
		{
			return smp;
		}

		/**
		 * Provides the stored statment object.
		 *
		 * @return statement stored in this object.
		 */
		public Stmt getStmt()
		{
			return smp.getStmt();
		}

		/**
		 * Provides the stored method object.
		 *
		 * @return method stored in this object.
		 */
		public SootMethod getSootMethod()
		{
			return smp.getSootMethod();
		}
		/**
		 *  Provides equality checking for two <code>StmtMethodPair</code> objects.  It checks if
		 * both the objects contain references to the same expression, statement, and method object.
		 *
		 * @param esmt is the object with which equivalence test needs to be done.
		 * @return <code>true</code> if the given object is the "equal" to this
		 * object. <code>false</code> otherwise.
		 */
		public boolean equals(Object esmt)
		{
			boolean ret;
			if (esmt instanceof ExprStmtMethodTriple) {
				ExprStmtMethodTriple temp = (ExprStmtMethodTriple)esmt;
				ret = (expr == temp.expr) && smp.equals(temp.smp);
			} else {
				ret = this == esmt;
			} // end of else
			return ret;
		}

		public String toString()
		{
			return "VALUE: " + expr + " - " + smp.toString();
		}
	}

	/**
	 * Provides a set of <code>Stmt</code>(from the soot library)in which the given
	 * <code>Value</code> is defined.  A copy of annotationmanager is obtained from
	 * compilationmanager to provide the information.
	 *
	 * @param v the variable which is of interest.  This should be a valid reference variable not a
	 * expression.
	 * @param enclosingMethod the method in which the variable occurs.
	 * @return a collection of "defining" <code>StmtMethodPair</code>s.
	 */
	public Collection getDefs(Value v, SootMethod enclosingMethod) {
		return getDefsOrUses(v, enclosingMethod, DEFS);
	}

	/**
	 * Provides a set of <code>Stmt</code>(from the soot library)in which the given
	 * <code>Value</code> is used.  A copy of annotationmanager is obtained from compilationmanager
	 * to provide the information.
	 *
	 * @param v the variable which is of interest.  This should be a valid reference variable not a
	 * expression.
	 * @param enclosingMethod the method in which the variable occurs.
	 * @return a collection of "using" <code>StmtMethodPair</code>s.
	 */
	public Collection getUses(Value v, SootMethod enclosingMethod) {
		return getDefsOrUses(v, enclosingMethod, USES);
	}

	/**
	 * Tells if the given <code>Value</code>, representing a reference, can ever evaluate to null
	 * during run-time.
	 *
	 * @param v the reference variable of interest.
	 * @param enclosingMethod the method in which the variable occurs.
	 * @return true - if <i>v</i> may contain null value; otherwise false.
	 */
	public boolean nullReference(Value v, SootMethod enclosingMethod)
	{
		RefValueSwitch temp = new RefValueSwitch();
		boolean retval = false;
		for (Iterator i = temp.build(v, enclosingMethod).iterator(); i.hasNext() && !retval; ) {
			 Analysis.ExprStmtMethodTriple a = (Analysis.ExprStmtMethodTriple)i.next();
			 retval = a.getExpr().getType().equals(NullType.v());
		} // end of for
		return retval;
	}

	/**
	 * This method provides a set of objects the given reference variable may or will refer.
	 *
	 * The set consists of <code>Value</code> objects representing the object creation expressions.
	 * It is the objects created at this site the given reference may or will refer to.
	 *
	 * @param v the reference variable of interest.
	 * @param enclosingMethod the method in which the value occurs.
	 * @return a set of <code>Value</code>s which the variable may refer.
	 */
	public Set referenceValueSet(Value v, SootMethod enclosingMethod) {
		return refValueSwitch.build(v, enclosingMethod);
	}

	/**
	 * Provides a collection of classes of objects that may flow into the
	 * receiver object at the point of a non-static invocation.
	 *
	 * @param expr the non-static invocation expression as expressed in Jimple.
	 * @param enclosingMethod the method inside which the invocation occurs.
	 * @return collection of possible classes, represented as
	 * <code>SootClass</code>, from which the method implementation may be
	 * invoked after resolution.
	 */
	public Collection invokeExprResolution(NonStaticInvokeExpr expr,
										   SootMethod enclosingMethod)
	{
		// Get the method to be resolved in the invocation expression.
		SootMethod sootMethod = expr.getMethod();

		SootMethod methodImpl;
		SootClass sootClass;
		Collection classes = new ArrayList();
		ValueVariant variant;
		if (expr instanceof SpecialInvokeExpr) {
			classes.add(expr.getMethod().getDeclaringClass());
		} else {
			/*
			 * Get the receiver object.  It will be a local as a result of using
			 * Jimple representation.
			 */
			Local l = (Local)expr.getBase();

			// Iterate over the variants that flow into the receiver object
			for (Iterator variants = FA.summarizeLocalValueVariants(enclosingMethod, l).iterator();
				 variants.hasNext();) {
				variant = (ValueVariant)variants.next();
				if (variant.getClassToken().equals(ClassTokenSimple.nullClassToken) ||
					variant.getClassToken().equals(ClassTokenSimple.unknownClassToken)){
					logger.warn("BOFA:Null may be flowing into invocation site.");
					continue;
				}
				// Get the class of which the current variant is of.
				sootClass = ((ClassTokenSimple)variant.getClassToken()).getSootClass();
				// Get the class in which the method of interest is implemented.
				methodImpl = MethodVariantManager.findDeclaringMethod(sootClass, sootMethod);
				// Add it to the set of possible class implementation.
				classes.add(methodImpl.getDeclaringClass());
			} // end of for ()
		} // end of else

		/* sort the set of possible classes in the reverse order of
		 * inheritence */
		classes = sortUnique(classes, new classComparator());
		return classes;
	}

	/**
	 * Sorts the given collection using the given comparator and returns the sorted collection with
	 * the duplicate removed.
	 *
	 * @param c the collection to be sorted.
	 * @param comparator the comparator that dictates the sorting.
	 * @return the sorted collection.
	 */
	static Collection sortUnique(Collection c, Comparator comparator)
	{
		int i, j, t = 0;
		Object temp;
		Object[] obj;
		LinkedList l = new LinkedList();
		obj = c.toArray();
		if (obj.length == 0) {
			return l;
		} // end of if ()

	outer:
		for (i = 0; i < obj.length - 1; i++) {
			for (j = i + 1; j < obj.length; j++) {
				/* if we have same class appearing twice we will remove
				 * duplicates */
				if (obj[i] == obj[j])
					continue outer;
				t = comparator.compare(obj[i], obj[j]);
				if (t != 0) {
					temp = obj[j];
					obj[j] = obj[i];
					obj[i] = temp;
				} // end of if ()
			} // end of for ()
			if (t != -1)
				l.add(obj[i]);
		} // end of for ()
		l.add(obj[obj.length - 1]);
		return l;
	}

	/**
	 * This class implements the Comparator interface to aid sort classes and interfaces depending on
	 * hierarchy.
	 *
	 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
	 */
	class classComparator implements Comparator
	{
		/**
		 * Compares two classes in the order of hierarchy.
		 *
		 * @param o1 the first SootClass object in the comparison
		 * @param o2 the second SootClass object in the comparison
		 * @return -1 if o1 and o2 are the same SootClasses
		 *          0 if o2 is the immediate superclass of o1
		 *          1 if o1 has no superclass
		 */
		public int compare(Object o1, Object o2)
		{
			if (o1 == o2)
				return -1;
			else if (((SootClass)o1).hasSuperClass())
				if (((SootClass)o1).getSuperClass() !=  (SootClass)o2)
					return compare(((SootClass)o1).getSuperClass(), o2);
				else
					return 0;
			else
				return 1;
		}
	}

}
