/*
 * Bandera, a Java(TM) analysis and transformation toolkit
 * Copyright (C) 2001, 2002
 * Venkatesh Prasad Ranganath (rvprasad@cis.ksu.edu)
 * All rights reserved.
 *
 * This work was done as a project in the SAnToS Laboratory, Department of
 * Computing and Information Sciences, Kansas State University, USA
 * (http://www.cis.ksu.edu/santos).  It is understood that any modification not
 * identified as such is not covered by the preceding statement.
 *
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.

 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.

 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA.
 *
 * Java is a trademark of Sun Microsystems, Inc.
 *
 * To submit a bug report, send a comment, or get the latest news on
 * this project and other SAnToS projects, please visit the web-site
 *                http://www.cis.ksu.edu/santos
 */

package edu.ksu.cis.bandera.bofa;

import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.jimple.Expr;
import ca.mcgill.sable.soot.jimple.InvokeExpr;
import ca.mcgill.sable.soot.jimple.Local;
import ca.mcgill.sable.soot.jimple.NonStaticInvokeExpr;
import ca.mcgill.sable.soot.jimple.SpecialInvokeExpr;
import ca.mcgill.sable.soot.jimple.Value;
import ca.mcgill.sable.util.ArraySet;
import ca.mcgill.sable.util.HashMap;
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.Set;

import org.apache.log4j.Category;


/*
 * CallGraph.java
 * $Id: CallGraph.java,v 1.1 2002/02/21 07:42:21 rvprasad Exp $
 */

/**
 * This class represents the Call graph of the system being analysed.
 *
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.1 $)
 */
public class CallGraph  {

	/**
	 * Provides logging facility through log4j.
	 *
	 */
	private static Category cat;

	static
	{
		cat = Category.getInstance(CallGraph.class.getName());
	}

	/**
	 * A mapping from caller to it's callees.
	 *
	 */
	HashMap caller2callees;

	/**
	 * A mapping from callee to it's callers.
	 *
	 */
	HashMap callee2callers;

	/**
	 * The call back which handles the static invocation sites.
	 *
	 */
	CGCnstrStaticCallBack staticInvokeCallBack;

	/**
	 * The call back which handles the non-static invocation sites.
	 *
	 */
	CGCnstrNonStaticCallBack nonstaticInvokeCallBack;

	/**
	 * A single instance of CallGraph required to implement singleton pattern.
	 *
	 */
	private static CallGraph singleton;

	/**
	 * The object that performs higher level analysis.
	 *
	 */
	private Analysis analyser;

	/**
	 * Creates a new <code>CallGraph</code> instance.
	 *
	 * @param analyser the object that performs the higher level analysis.
	 */
	private CallGraph (Analysis analyser)
	{
		callee2callers = new HashMap();
		caller2callees = new HashMap();
		staticInvokeCallBack = new CGCnstrStaticCallBack();
		nonstaticInvokeCallBack = new CGCnstrNonStaticCallBack();
		this.analyser = analyser;
	}

	/**
	 * Provides a <code>CallGraph</code> object.
	 *
	 * @param analyser the object that performs the higher level analysis.
	 * @return a <code>CallGraph</code> singleton object.
	 */
	static CallGraph init(Analysis analyser)
	{
		if (singleton == null) {
			singleton = new CallGraph(analyser);
		} // end of if (singleton == null)
		return singleton;
	}

	/**
	 * Provides the call back object which deals with static invocations.
	 *
	 * @return <code>staticInvokeCallBack</code> object.
	 */
	CallBack getStaticCallBack()
	{
		return staticInvokeCallBack;
	}

	/**
	 * Provides the call back object which deals with non-static invocations.
	 *
	 * @return <code>nonStaticInvokeCallBack</code> object.
	 */
	CallBack getNonStaticCallBack()
	{
		return nonstaticInvokeCallBack;
	}

	/**
	 * Clears the maps from callers to callees and vice versa.
	 *
	 */
	static void reset()
	{
		singleton.caller2callees.clear();
		singleton.callee2callers.clear();
	}

	/**
	 * Provides the set of methods called from the given method.
	 *
	 * @param caller the calling method.
	 * @return a set of <code>ExprStmtMethodTriple</code>s containing the method called and the site
	 * at which it was called.
	 */
	Set getCallees(SootMethod caller)
	{
		Set retval = new ArraySet();

		if (caller2callees.containsKey(caller)) {
			retval.addAll((Set)caller2callees.get(caller));
		} // end of if (caller2callees.contains(caller))

		return retval;
	}

	/**
	 * Provides the set of methods which call the given method.
	 *
	 * @param callee the called method.
	 * @return a set of caller<code>ExprStmtMethodTriple</code>s containing the calling method and
	 * the site at which it called.
	 */
	Set getCallers(SootMethod callee)
	{
		Set retval = new ArraySet();

		if (callee2callers.containsKey(callee)) {
			retval.addAll((Set)callee2callers.get(callee));
		} // end of if (caller2callees.contains(caller))

		return retval;
	}

	/**
	 * The call back class which handles the static invocation sites.
	 *
	 */
	class CGCnstrStaticCallBack implements CallBack
	{
		/**
		 * The call back method which will be called on trigger.
		 *
		 * @param v is the expressions which triggered the call.
		 * @param o is the method in which the expression occurred.
		 */
		public void callback(Expr v, Object o)
		{
			Set callees, callers;
			SootMethod caller = (SootMethod)o;
			SootMethod callee = ((InvokeExpr)v).getMethod();

			// This maintains a caller to callee map
			if (caller2callees.containsKey(caller)) {
				callees = (Set)caller2callees.get(caller);
			} else {
				callees = new ArraySet();
				caller2callees.put(caller, callees);
			} // end of else
			Analysis.ExprStmtMethodTriple estm =
				analyser.getExprStmtMethodTriple(v);
			estm.smp.sm = callee;
			if (!callees.contains(estm)) {
				callees.add(estm);
			} // end of if (!callees.contains(estm))

			// This maintains a callee to caller map
			if (callee2callers.contains(callee)) {
				callers = (Set)callee2callers.get(callee);
			} else {
				callers = new ArraySet();
				callee2callers.put(callee, callers);
			} // end of else
			estm = analyser.getExprStmtMethodTriple(v);
			estm.smp.sm = caller;
			callers.add(estm);
		}
	}

	/**
	 * The call back class which handles the non-static invocation sites.
	 *
	 */
	class CGCnstrNonStaticCallBack implements CallBack
	{
		/**
		 *  The call back method which will be called on trigger.
		 *
		 * @param v is the expressions which triggered the call.
		 * @param o is the <code>FGActionInvoke</code> which handles the given
		 * expression site.
		 */
		public void callback(Expr v, Object o)
		{
			Analysis.ExprStmtMethodTriple estm;
			Set callees, callers;
			SootMethod caller = ((FGActionInvoke)o).getEnclosingSootMethod();
			SootMethod callee = ((InvokeExpr)v).getMethod();

			if (caller2callees.containsKey(caller)) {
				callees = (Set)caller2callees.get(caller);
			} else {
				callees = new ArraySet();
				caller2callees.put(caller, callees);
			} // end of else

			if (v instanceof SpecialInvokeExpr) {
				estm = analyser.getExprStmtMethodTriple(v);
				estm.smp.sm = callee;
				callees.add(estm);

				if (callee2callers.containsKey(callee)) {
					callers = (Set)callee2callers.get(callee);
				} else {
					callers = new ArraySet();
					callee2callers.put(callee, callers);
				} // end of else
				estm = analyser.getExprStmtMethodTriple(v);
				estm.smp.sm = caller;
				callers.add(estm);
			} else {
				Value temp = ((NonStaticInvokeExpr)v).getBase();
				Iterator i = null;
				if (temp instanceof Local) {
					i = FA.summarizeLocalValueVariants(caller, (Local)temp).iterator();
				} else {
					i = FA.summarizeExprValueVariants(caller, (Expr)temp).iterator();
				} // end of else

				for (; i.hasNext(); ) {
					ValueVariant valueVariant = (ValueVariant)i.next();
					if (valueVariant.getClassToken().
						equals(ClassTokenSimple.nullClassToken) ||
						valueVariant.getClassToken().
						equals(ClassTokenSimple.unknownClassToken)) {
						continue;
					} // end of if
					SootClass sootClass =
						((ClassTokenSimple)valueVariant.getClassToken())
						.getSootClass();
					SootMethod declMethod = MethodVariantManager
						.findDeclaringMethod(sootClass, callee);
					estm = analyser.getExprStmtMethodTriple(v);
					estm.smp.sm = declMethod;
					callees.add(estm);

					if (callee2callers.containsKey(declMethod)) {
						callers = (Set)callee2callers.get(declMethod);
					} else {
						callers = new ArraySet();
						callee2callers.put(declMethod, callers);
					} // end of else
					estm = analyser.getExprStmtMethodTriple(v);
					estm.smp.sm = caller;
					callers.add(estm);
				} // end of for (; i.hasNext();)
			}
		}
	}
}// CallGraph
