package edu.ksu.cis.bandera.birc;

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
import edu.ksu.cis.bandera.bir.*;

import java.util.*;

/**
 * A simple linked list representing a sequence of Transformations.
 * <p>
 * Note: there is always a dummy cell on the end of the list
 * (thus the expression 'new TransSequence()' represents an empty
 * sequence). 
 * <p>
 * This class was designed to handle collapsing sequences of
 * nontrivial transformations with guards, so it can record
 * the values of variables that appear in guards and update
 * them in the guard expression.  For example, collapsing:
 * <pre>
 * when true do { x := x + 1; } 
 * when x > 10 do { y := y * 2; }
 * when x > y do { z := 1; } 
 * </pre>
 * would have to produce:
 * <pre>
 * when x + 1 > 10 and x + 1 > y * 2 do { x := x + 1; y := y * 2; z := 1; } 
 * </pre>
 * Now that nontrivial transitions are no longer collapsed, this
 * functionality is unused.
 */

public class TransSequence {

	Transformation trans;    // Transformation
	TransSequence next;      // Next in chain

	static Hashtable variableBindings = new Hashtable();
	static BindingSubstituter substituter = 
	new BindingSubstituter(variableBindings);

	/**
	 * Actions of sequence.
	 */

	public ActionVector actions() {
	ActionVector actions = new ActionVector();
	TransSequence seq = this;
	while (seq.next != null) {
	    ActionVector transActions = seq.trans.getActions();
	    for (int i = 0; i < transActions.size(); i++) 
		actions.insertElementAt(transActions.elementAt(i), i);
	    seq = seq.next;
	}
	return actions;	
	}
	/**
	 * Add transformation to end of sequence.
	 */

	public TransSequence add(Transformation newTrans) {
	TransSequence seq = new TransSequence();
	seq.trans = newTrans;
	seq.next = this;
	return seq;
	}
	/**
	 * Does sequence contain location?
	 */

	public boolean containsLoc(Location loc) {
	if (next == null)
	    return false;
	if (loc == trans.getFromLoc())
	    return true;
	return next.containsLoc(loc);
	}
	public boolean empty() { return next == null; }
	/**
	 * From location of sequence.
	 */

	public Location fromLoc() {
	if (next.next == null)
	    return trans.getFromLoc();
	return next.fromLoc();
	}
	/**
	 * Guard of sequence (must be computed from variable bindings).
	 */

	public Expr guard() {
	int count = this.size();
	Transformation [] trans = new Transformation[count];
	for (TransSequence seq = this; seq.next != null; seq = seq.next) 
	    trans[--count] = seq.trans;
	variableBindings.clear();
	Expr guard = null;
	for (int i = 0; i < trans.length; i++) {
	    if (trans[i].getGuard() != null) {
		trans[i].getGuard().apply(substituter);
		Expr expr = (Expr) substituter.getResult();
		guard = (guard == null) ? expr : new AndExpr(guard,expr);
	    }
	    updateBindings(trans[i]);
	}
	return guard;
	}
	/**
	 * Number of transformations in sequence
	 */

	public int size() {
	int result = 0;
	for (TransSequence seq = this; seq.next != null; seq = seq.next) 
	    result++;
	return result;
	}
	/**
	 * To location of sequence.
	 */

	public Location toLoc() {
	return trans.getToLoc();
	}
	void updateBindings(Transformation trans) {
	ActionVector actions = trans.getActions();
	if (actions != null) 
	    for (int j = 0; j < actions.size(); j++) 
		if (actions.elementAt(j) instanceof AssignAction) {
		    AssignAction assign = 
			(AssignAction) actions.elementAt(j);
		    assign.getRhs().apply(substituter);
		    variableBindings.put(assign.getLhs(), 
					 substituter.getResult());
		}
	}
}
