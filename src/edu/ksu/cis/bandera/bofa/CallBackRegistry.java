/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001, 2002                                          *
 * Venaktesh Prasad Ranganath (rvprasad@cis.ksu.edu)                 *
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

import ca.mcgill.sable.soot.jimple.NonStaticInvokeExpr;
import ca.mcgill.sable.soot.jimple.StaticInvokeExpr;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Category;

/*
 * CallBackRegistry.java
 * $Id: CallBackRegistry.java,v 1.2 2002/02/21 07:42:20 rvprasad Exp $
 */

/**
 * The user can register with this class the various callback objects to be invoked at suitable
 * points during the analysis.  These callback objects should inherit from CallBack.
 *
 * @author <a href="mailto:rvprasad@cis.ksu.edu">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */

public class CallBackRegistry {

	/**
	 * Registry of callbacks which have to be called when non-static invocations are being
	 * considered.
	 */
	private Vector nonStaticInvokeRegister;

	/**
	 * Registry of callbacks which have to be called when static invocations are being considered.
	 *
	 */
	private Vector staticInvokeRegister;

	/**
	 * Object used to create a category for logging information.
	 *
	 */
	protected static Category cat;

	/**
	 * Creates a new <code>CallBackRegistry</code> instance.
	 *
	 */
	public CallBackRegistry () {
		// at present we are interested only in the virtual invoke expression.
		nonStaticInvokeRegister = new Vector();
		staticInvokeRegister = new Vector();
		cat = Category.getInstance(getClass().getName());
	}

	/**
	 * Registers a callback object to be called when a non-static method will be invoked.
	 *
	 * @param cb object to be called when a non-static invoke expression is encountered during flow
	 * analysis.
	 */
	public void regNonStaticInvoke(CallBack cb) {
		nonStaticInvokeRegister.add(cb);
		cat.debug("Registering non-static callback " + cb);
	}

	/**
	 * Registers a callback object to be called when a static method will be
	 * invoked.
	 *
	 * @param cb object to be called when a static invoke expression is
	 * encountered during flow analysis.
	 */
	public void regStaticInvoke(CallBack cb) {
		staticInvokeRegister.add(cb);
		cat.debug("Registering static callback " + cb);
	}
	/**
	 * Trigger pertaining to non-static invocation occurs during flow analysis.
	 *
	 * @param v object representing the non static invoke expression.
	 * @param obj object which was processing the expression.
	 */
	public void callNonStaticInvoke(NonStaticInvokeExpr v, Object obj) {
		Iterator i = nonStaticInvokeRegister.iterator();
		while (i.hasNext()) {
			((CallBack)i.next()).callback(v, obj);
		} // end of while (i.hasnext())
	}

	/**
	 *  Trigger pertaining to static invocation occurs during flow analysis.
	 *
	 * @param v object representing the non static invoke expression.
	 * @param obj object which was processing the expression.
	 */
	public void callStaticInvoke(StaticInvokeExpr v, Object obj) {
		Iterator i = staticInvokeRegister.iterator();
		while (i.hasNext()) {
			((CallBack)i.next()).callback(v, obj);
		} // end of while (i.hasnext())
	}
}// Callbackregistry
