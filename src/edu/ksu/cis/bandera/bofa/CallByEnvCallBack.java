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

import ca.mcgill.sable.soot.NoSuchMethodException;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.Type;
import ca.mcgill.sable.soot.VoidType;
import ca.mcgill.sable.soot.jimple.Expr;
import ca.mcgill.sable.soot.jimple.Jimple;
import ca.mcgill.sable.soot.jimple.Local;
import ca.mcgill.sable.soot.jimple.VirtualInvokeExpr;
import ca.mcgill.sable.util.VectorList;

import org.apache.log4j.Category;

/*
 * CallByEnvCallBack.java
 * $Id: CallByEnvCallBack.java,v 1.2 2002/04/05 23:40:36 rvprasad Exp $
 */

/**
 *
 * This class provides the call back method to be invoked when method X is encountered during the
 * flow analysis.
 *
 * @author <a href="mailto:rvprasad@cis.ksu.edu">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */

public class CallByEnvCallBack implements CallBack {
	/**
	 * It is used to create jimple expression AST nodes.
	 *
	 */
	static private Jimple jimple;

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

	static {
		jimple = Jimple.v();
	}

	/**
	 * Provides logging through log4j.
	 *
	 */
	private Category cat;

	/**
	 * Creates a new <code>CallByEnvCallBack</code> instance.
	 */
	public CallByEnvCallBack() {
		cat = Category.getInstance(getClass().getName());
		declClass = "java.lang.Thread";
		mname = "start";
		mParamList = Util.getEmptyParamList();
		mReturnType = VoidType.v();
		pmname = "run";
		pmParamList = Util.getEmptyParamList();
		pmReturnType = VoidType.v();
	}

	/**
	 * Creates a new <code>CallByEnvCallBack</code> instance.
	 *
	 * @param declClass Name of the class declaring the method <code>mname</code>.
	 * @param mname Name of the method that will trigger the inclusion.
	 * @param mParamList Parameter list of the trigger method.
	 * @param mReturnType Return type of the trigger method.
	 * @param pmname Name of the method to be included into the flow graph.
	 * @param pmParamList Parameter list of the plugin method.
	 * @param pmReturnType Return type of the plugin method.
	 */
	public CallByEnvCallBack(String declClass, String mname,
							 VectorList mParamList, Type mReturnType,
							 String pmname, VectorList pmParamList,
							 Type pmReturnType) {
		cat = Category.getInstance(getClass().getName());
		this.declClass = declClass;
		this.mname = mname;
		this.mParamList =mParamList;
		this.mReturnType =mReturnType;
		this.pmname = pmname;
		this.pmParamList = pmParamList;
		this.pmReturnType = pmReturnType;
	}

	/**
	 * <code>callback</code> will be called by BOFA when java.lang.Thread.start method invocation is
	 * encountered during flow analysis.
	 *
	 * @param e <code>Expr</code> object respresenting the expression which triggered the callback.
	 * @param o <code>Object</code> object representing the FGExpr object which was processing the
	 * expression.
	 * @throw <code>ca.mcgill.sable.soot.NoSuchMethodException</code> is thrown when run method is
	 * undefined in the given hierarchy.
	 */
	public void callback(Expr e, Object o) {
		if (!(e instanceof VirtualInvokeExpr))
			return;

		VirtualInvokeExpr v = (VirtualInvokeExpr) e;
		SootMethod sm = v.getMethod();
		try {
			// Check if the declClass is an ancestor of the static type of the
			// receiver and it indeed declares a method by the given name.
			if (Util.isAncestorOf(sm.getDeclaringClass(), declClass)
				&& sm.getName().equals(mname)) {
				SootClass runClass =
					Util.getDeclaringClass(BOFA.sootClassManager.getClass(v.getBase().getType()
																		  .toString()), pmname);
				// Create a new Jimple expression for pmname method and process it to be considered for
				// flow analysis.
				VirtualInvokeExpr v1 =
					jimple.newVirtualInvokeExpr((ca.mcgill.sable.soot.jimple.Local)v.getBase(),
												runClass.getMethod(pmname), Util.getEmptyParamList());
				FGExpr f = (FGExpr)o;
				f.build(v1, f.getCurrentStmt());
				cat.info("Plugging a call to run method of class" + v.getBase().getType().toString() +
						 ".");
			} 
		} catch (Exception ee) {
			cat.warn("We punt here as we do not have info regarding internals of Thread implementation.");
		} // end of try-catch
	}	
}// CallByEnvCallBack
