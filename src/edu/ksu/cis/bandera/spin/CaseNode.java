package edu.ksu.cis.bandera.spin;

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
import java.io.*;
import java.util.*;

import edu.ksu.cis.bandera.bir.*;

/**
 * Case node of case tree. 
 * <p>
 * A case node represents a conditional expression.  Each case has
 * a condition and a value (another case tree node).
 * The cases must be mutually exclusive and 
 * the last case must be the default (i.e., is the value of the
 * expression if all the previous conditions are false).
 */

public class CaseNode implements TreeNode {
	
	int size = 0;               // number of cases
	Case [] data;               // cases

	public CaseNode() { this(10); }
	public CaseNode(int capacity) {
	data = new Case[capacity];
	}
	public CaseNode(Case x) {
	this(10);
	addElement(x);
	}
	public void addCase(String cond, TreeNode node) {
	addElement(new Case(cond,node));
	}
	public void addCase(String cond, String expr) {
	addElement(new Case(cond,new ExprNode(expr)));
	}
	public void addElement(Case x) {
	if (x == null)
	    throw new RuntimeException("Element cannot be null");
	if (size == data.length)
	    expand();
	data[size++] = x;
	}
	public void addTrapCase(String cond, String desc, boolean fatal) {
	addElement(new Case(cond,new TrapNode(desc,fatal)));
	}
	/**
	 * Make a copy of this CaseNode (and its Cases).
	 */

	public Object clone() {
	CaseNode result = new CaseNode(this.data.length);
	for (int i = 0; i < this.size(); i++)
	    result.addElement((Case)this.elementAt(i).clone());
	return result;
	}
	/**
	 * Compose this case node with another case tree.
	 * <p>
	 * Basically this just clones the original case tree all the
	 * way down to the leaves so that when we append the other
	 * tree to the leaf, we don't change the original case tree.
	 * <p>
	 * The context argument is used to allow nodes to update
	 * their parent pointers.
	 */

	public TreeNode compose(TreeNode tree, Case context) {
	CaseNode result = (CaseNode) this.clone();
	for (int i = 0; i < result.size(); i++) {
	    Case c = result.elementAt(i);
	    c.parent = context;
	    c.node = c.node.compose(tree,c);
	}
	return result;
	}
	/**
	 * Check if case is inconsistent (conflicts with some parent case's
	 * condition).
	 * <p>
	 * We do a simple text-based match on the expressions looking for
	 * certain common patterns.  This is not a general constraint
	 * consistency check (which is OK, since if we fail to detect
	 * an inconsistency, it just results in more PROMELA code, not
	 * an incorrect model).
	 * <p>
	 * In particular, we look for conditions of the form "x == S"
	 * and "x == R" where S does not equal R.
	 */

	boolean conditionInconsistent(String cond, Case context) {
	// Punt if not an equality or contains &&, ||, !
	if (cond.indexOf("==") < 0 || cond.indexOf("&&") >= 0 
	    || cond.indexOf("||") >= 0 || cond.indexOf("!") >= 0)
	    return false;
	while (context != null) {
	    // Assume cond is of the form: var == val
	    String var = cond.substring(0,cond.indexOf("=="));
	    String val = cond.substring(cond.indexOf("=="));
	    int pos = context.cond.indexOf("==");
	    if (pos > 0 && context.cond.substring(0,pos).equals(var)
		&& ! context.cond.substring(pos).equals(val)) 
		return true;
	    context = context.parent;
	}
	return false;
	}
	/**
	 * Check if condition is redundant (appears as condition of some 
	 * parent case).
	 */

	boolean conditionRedundant(String cond, Case context) {
	while (context != null) {
	    if (context.cond.equals(cond))
		return true;
	    context = context.parent;
	}
	return false;
	}
	public Case elementAt(int pos) {
	if (pos < 0 || pos >= size) 
	    throw new RuntimeException("Position invalid: " + pos);
	return data[pos];
	}
	// Grow case array
	void expand() {
	Case [] newData = new Case[data.length * 2];
	for (int i = 0; i < data.length; i++) 
	    newData[i] = data[i];
	data = newData;
	}
	public Case firstElement() { return elementAt(0); }
	public TreeNode getCase(String cond) {
	for (int i = 0; i < size(); i++) 
	    if (elementAt(i).cond.equals(cond))
		return elementAt(i).node;
	throw new RuntimeException("No such case: " + cond);
	}
	/**
	 * Collect all leaf cases (Case nodes with ExprNode values) 
	 * into the vector.
	 */

	public Vector getLeafCases(Vector leafCases) {
	for (int i = 0; i < size(); i++) 
	    if (elementAt(i).node instanceof CaseNode)
		elementAt(i).node.getLeafCases(leafCases);
	    else if (elementAt(i).node instanceof ExprNode) 
		leafCases.addElement(elementAt(i));
	// Note: we don't count traps as leaves
	return leafCases;
	}
	/**
	 * Collect all leaves (ExprNodes) into the vector.
	 */

	public Vector getLeaves(Vector leaves) {
	for (int i = 0; i < size(); i++) 
	    elementAt(i).node.getLeaves(leaves);
	return leaves;
	}
	void indent(int level) {
	for (int i = 0; i < level; i++)
	    System.out.print("   ");
	}
	public int indexOf(Case x) {
	for (int i = 0; i < size; i++) 
	    if (data[i] == x)
		return i;
	return -1;
	}
	public void insertElementAt(Case x, int pos) {
	if (x == null)
	    throw new RuntimeException("Element cannot be null");
	if (size == data.length)
	    expand();
	if (pos > size || pos < 0)
	    throw new RuntimeException("Position invalid: " + pos);
	// Auto expand like Vector
	for (int i = size; i > pos; i--) 
	    data[i] = data[i-1];
	data[pos] = x;
	size++;
	}
	public void print(int level) {
	System.out.println();
	for (int i = 0; i < size(); i++) {
	    Case c = elementAt(i);
	    indent(level);
	    System.out.print(c.cond + " => ");
	    c.node.print(level+1);
	}
	}
	public int size() { return size; }
	/**
	 * Specialize a case tree to a given context.
	 * <p>
	 * Like compose() above, this clones the case tree
	 * as it walks over it (the second case tree being composed),
	 * and it specializes as it constructs the new cases.
	 * In particular:
	 * <ul>
	 * <li> If some case's condition appeared in
	 *   a parent case, then we specialize the CaseExpr
	 *   to the value of that case's condition.
	 * <li> If the first N-1 case conditions are inconsistent with the
	 *  conditions in the parent cases, then we specialize the CaseExpr
	 *  to the value of the last case.
	 * </ul>
	 */

	public TreeNode specialize(ExprNode leaf, Case context) {
	CaseNode result = (CaseNode) this.clone();
	int numInconsistent = 0;
	for (int i = 0; i < result.size(); i++) {
	    Case c = result.elementAt(i);
	    if (conditionRedundant(c.cond, context) 
		|| ((i == (result.size() - 1)) 
		    && (i == numInconsistent)))
		return c.node.specialize(leaf,context);
	    if (conditionInconsistent(c.cond, context)) 
		numInconsistent++;
	    c.parent = context;
	    c.node = c.node.specialize(leaf,c);
	}
	return result;	
	}
}
