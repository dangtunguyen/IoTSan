package edu.ksu.cis.bandera.smv;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import edu.ksu.cis.bandera.bir.StateVar;

import java.util.*;

/**
 * An SMV variable.
 */

public class SmvVar implements SmvExpr {

	String name;
	String type;           // null if this var is an aggregate
	StateVar source;
	SmvLit initValue;      // Initial value (or null if none)
	SmvVar aggregate;      // Aggregate var we're nested in
	Vector components;     // Components (if this var is aggregate)
	boolean constrained;   // Will a TRANS be generated for this var?
	SmvVar deadFlag;       // Variable live only if this var is true
	String printForm;      // Special form for variable when printed 

	SmvExpr updateExpr;    // TRANS expression for variable updates
	SmvNaryExpr updates;   // Conjunction of conditional updates
	SmvNaryExpr negUpdateConds;  // Conjunction of negated conditions

	/**
	 * Create a temp SmvVar used only to hold a prefix of a name 
	 * (not declared).
	 */
	public SmvVar(String name) {
	this.name = name;
	}
	public SmvVar(String name, String type, StateVar source, SmvLit initVal,
		  boolean constrained, SmvVar deadFlag) {
	this.name = name;
	this.type = type;
	this.source = source;
	this.initValue = initVal;
	this.constrained = constrained;
	this.deadFlag = deadFlag;
	this.components = new Vector();
	updates = new SmvNaryExpr("&",new SmvLit("1"),true);
	negUpdateConds = new SmvNaryExpr("&",new SmvLit("1"),true);
	}
	public void addComponent(SmvVar var) { components.addElement(var); }
	/**
	 * Add conditional update to variable.
	 * <p>
	 * The expression "cond -> assign" is added to the conjunction of
	 * updates, and the negated condition is recorded in negUpdateConds
	 * (which forms the condition of the "variable unchanged" update).
	 */

	public void addUpdate(SmvExpr cond, SmvExpr assign) {
	SmvBinaryExpr update = new SmvBinaryExpr("->",cond,assign);
	updates.add(update);
	negUpdateConds.add(new SmvUnaryExpr("!",cond));
	}
	public SmvVar getComponent(int i) {
	return (SmvVar) components.elementAt(i);
	}
	public Vector getComponents() { return components; }
	public SmvVar getDeadFlag() { return deadFlag; }
	public SmvLit getInitValue() { return initValue; }
	public String getName() { return name; }
	public StateVar getSource() { return source; }
	public String getType() { return type; }
	/**
	 * Build the update expression (TRANS formula) for the variable
	 * <p>
	 * This must be called after all conditional updates have been added.
	 * <p>
	 * To form the update expression, we use the conjunction of all the
	 * negated conditions to build one last update, specifying the 
	 * variable is unchanged if none of the update conditions is true.
	 * We then add this last update to the update conjunction, which
	 * becomes the update expression.  Finally, if there is a dead flag,
	 * then we allow the formula to be true if that variable is false
	 * OR the update expression is true.
	 */

	public SmvExpr getUpdateExpr() { 
	if (updateExpr == null) {
	    updates.add(new SmvBinaryExpr("->",negUpdateConds,
					  SmvTrans.becomes(this,this)));
	    if (deadFlag != null) {
		updateExpr = new SmvUnaryExpr("!",new SmvNextExpr(deadFlag));
		updateExpr = new SmvBinaryExpr("|",updateExpr,updates);
	    } else
		updateExpr = updates;
	}
	return updateExpr;
	}
	public boolean isBig() { return false; }
	public boolean isConstrained() { return constrained; }
	public void print(SmvTrans out) {
	if (printForm != null)   // If no special print form, just use name
	    out.print(printForm);
	else
	    out.print(name);
	}
	public void setAggregate(SmvVar var) { this.aggregate = var; }
	public void setPrintForm(String printForm) { this.printForm = printForm; }
	public String toString() { return name; }
}
