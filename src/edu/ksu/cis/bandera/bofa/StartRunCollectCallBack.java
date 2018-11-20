/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001, 2002                                          *
 * Venkatesh Prasad Ranganath(rvprasad@cis.ksu.edu)
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

import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.Type;
import ca.mcgill.sable.soot.VoidType;
import ca.mcgill.sable.soot.jimple.Expr;
import ca.mcgill.sable.soot.jimple.InvokeExpr;
import ca.mcgill.sable.soot.jimple.VirtualInvokeExpr;
import ca.mcgill.sable.util.Collection;
import ca.mcgill.sable.util.HashMap;
import ca.mcgill.sable.util.HashSet;
import ca.mcgill.sable.util.VectorList;

/*
 * StartRunCollectCallBack.java
 * $Id: StartRunCollectCallBack.java,v 1.1 2002/02/21 07:42:24 rvprasad Exp $
 */

/**
 * This class uses callback to collect start to run mapping in the given system.  In Java
 * java.lang.Thread.start() will call run() method.  start is a native method and hence it's
 * implementation is not avaiable in classfile format.  Hence, this external call will be trapped
 * here once the suitable run method calls have been used to fix the flow graph by
 * <code>CallByEnvCallBack</code> class' callback() method during flow graph construction.  There may
 * be such library intricacies which may hamper program transformation of Java programs.  In such
 * situation, this class can be extended to handle the case suitably.
 *
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.1 $)
 */
public class StartRunCollectCallBack implements CallBack {

	/**
	 * Class in which the method being registered should be declared in the class hierarchy for the
	 * callback to occur.
	 */
	private final String declClass;

	/**
	 * Name of the method when encountered the new method is plugged in.  By default it is
	 * "start". (triggering method).
	 */
	private final String mname;

	/**
	 * Name of the plug-in method.  By default it is "run". (plugin method).
	 */
	private final String pmname;

	/**
	 * The parameter list of the triggering method.
	 *
	 */
	private final VectorList mParamList;

	/**
	 * The return type of the triggering method.
	 *
	 */
	private final Type mReturnType;

	/**
	 * The parameter list of the plugin method.
	 *
	 */
	private final VectorList pmParamList;

	/**
	 * The return type of the plugin method.
	 *
	 */
	private final Type pmReturnType;

	/**
	 * Maps the start method containing invoke expressions to invoked run methods.
	 *
	 */
	private HashMap startrunMap;

	/**
	 * Creates a new <code>StartRunCollectCallBack</code> instance.
	 *
	 */
	public StartRunCollectCallBack () {
		declClass = "java.lang.Thread";
		mname = "start";
		pmname = "run";
		mParamList = Util.getEmptyParamList();
		pmParamList = Util.getEmptyParamList();
		mReturnType = VoidType.v();
		pmReturnType = VoidType.v();
		startrunMap = new HashMap();
	}

	/**
	 * Creates a new <code>StartRunCollectCallBack</code> instance.
	 *
	 * @param declClass Name of the class declaring the trigger method..
	 * @param mname Name of the method that will trigger the inclusion.
	 * @param mParamList Parameter list of the trigger method.
	 * @param mReturnType Return type of the trigger method.
	 * @param pmname Name of the method to be included into the flow graph.
	 * @param pmParamList Parameter list of the plugin method.
	 * @param pmReturnType Return type of the plugin method.
	 */
	public StartRunCollectCallBack(String declClass, String mname, VectorList mParamList, 
								   Type mReturnType, String pmname, VectorList pmParamList, 
								   Type pmReturnType) {  
		this.declClass = declClass;
		this.mname = mname;
		this.mParamList =mParamList;
		this.mReturnType =mReturnType;
		this.pmname = pmname;
		this.pmParamList = pmParamList;
		this.pmReturnType = pmReturnType;
		startrunMap = new HashMap();
	}

	/**
	 * Collects information about the <code>pmname</code> implementation invoked at a particular call
	 * site of <code>mname</code>.
	 *
	 * @param e invoke expression involving <code>mname</code>
	 * @param o <code>SootClass</code> of the class in which the invoked <code>pmname</code>
	 * implementation exists.
	 */
	public void callback(Expr e, Object o)
	{
		if (!(e instanceof VirtualInvokeExpr &&
			  ((VirtualInvokeExpr)e).getMethod().getName().equals(mname))) 
			return;

		VirtualInvokeExpr v = (VirtualInvokeExpr) e;
		SootClass sc = ((FGActionInvoke)o).getValueSootClass();

		SootMethod implMethod = MethodVariantManager.findDeclaringMethod(sc, mname, mParamList,
																		 mReturnType);
		if (implMethod != null && 
			implMethod.getDeclaringClass().getName().equals("java.lang.Thread")) {
			Collection runs;
			if (startrunMap.containsKey(e)) {
				runs = (Collection)startrunMap.get(e);
			} else {
				runs = new HashSet();
				startrunMap.put(e, runs);
			}
			SootMethod runMethod =
				MethodVariantManager.findDeclaringMethod(sc, pmname, pmParamList, pmReturnType);
			if (runMethod != null) {
				runs.add(runMethod);
			} // end of if (runMethod != null)
		} // end of if (implClass.equals("java.lang.Thread"))
	}

	/**
	 * Reset the data structures.
	 *
	 */
	public void reset()
	{
		startrunMap.clear();
	}

	/**
	 * Provides the set of <code>pmname</code> implementations corresponding to an expression
	 * involving <code>mname</code> invocation.
	 *
	 * @param e method invoke expression involving <code>mname</code>.
	 * @return a collection of <code>SootMethod</code> corresponding to the <code>pmname</code>
	 * implementation that was invoked.
	 */
	public Collection getRuns(InvokeExpr e)
	{
		return (Collection)startrunMap.get(e);
	}

}// StartRunCollectCallBack
