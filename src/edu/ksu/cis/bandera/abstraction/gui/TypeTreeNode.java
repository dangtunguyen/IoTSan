package edu.ksu.cis.bandera.abstraction.gui;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
 * All rights reserved.                                              *
 *                                                                   *
 * Modifications by Robby (robby@cis.ksu.edu) are                    *
 * Copyright (C) 2000 Robby.  All rights reserved.                   *
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
import java.util.Vector;
import javax.swing.table.*;
import java.util.Hashtable;
import ca.mcgill.sable.soot.*;
import javax.swing.tree.TreeNode;
public class TypeTreeNode extends AbstractTreeNode {
	protected java.lang.String name;
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:57:14 PM)
 * @param name java.lang.String
 */
public TypeTreeNode(String name) {
	this.name = name;
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:56:45 PM)
 * @return boolean
 */
public boolean getAllowsChildren() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (3/24/00 12:20:36 AM)
 * @return javax.swing.table.TableCellRenderer
 * @param row int
 * @param column int
 */
public TableCellEditor getCellEditor(int column) {
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/24/00 12:20:36 AM)
 * @return javax.swing.table.TableCellRenderer
 * @param row int
 * @param column int
 */
public TableCellRenderer getCellRenderer(int column) {
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:08:39 PM)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:08:39 PM)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 1:55:15 PM)
 * @return java.lang.String
 */
public String toString() {
	return getName();
}
}
