package edu.ksu.cis.bandera.abstraction.gui;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
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
import java.util.*;
import javax.swing.tree.*;
import javax.swing.table.*;
public abstract class AbstractTreeNode implements TreeNode {
	Vector children;
	protected javax.swing.tree.TreeNode parent;
public AbstractTreeNode() {
	children = new Vector();
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:04:50 PM)
 * @param node javax.swing.tree.TreeNode
 */
public void addChild(AbstractTreeNode node) {
	children.add(node);
	node.setParent(this);
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:04:50 PM)
 * @param node javax.swing.tree.TreeNode
 */
public void addChild(TreeNode node) {
	children.add(node);
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:52:47 PM)
 * @return java.util.Enumeration
 */
public Enumeration children() {
	return children.elements();
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:55:31 PM)
 * @return boolean
 */
public abstract boolean getAllowsChildren();
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:18:30 PM)
 * @return javax.swing.table.TableCellEditor
 * @param column int
 */
public abstract TableCellEditor getCellEditor(int column);
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:18:30 PM)
 * @return javax.swing.table.TableCellEditor
 * @param column int
 */
public abstract TableCellRenderer getCellRenderer(int column);
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:54:18 PM)
 * @return javax.swing.tree.TreeNode
 * @param i int
 */
public TreeNode getChildAt(int i) {
	return (TreeNode) children.get(i);
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:51:38 PM)
 * @return int
 */
public int getChildCount() {
	return children.size();
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:53:49 PM)
 * @return int
 * @param node javax.swing.tree.TreeNode
 */
public int getIndex(TreeNode node) {
	return children.indexOf(node);
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:34:59 PM)
 * @return javax.swing.tree.TreeNode
 */
public javax.swing.tree.TreeNode getParent() {
	return parent;
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:52:12 PM)
 * @return boolean
 */
public boolean isLeaf() {
	return (getChildCount() == 0);
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:34:59 PM)
 * @param newParent javax.swing.tree.TreeNode
 */
public void setParent(javax.swing.tree.TreeNode newParent) {
	parent = newParent;
}
}
