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
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import ca.mcgill.sable.soot.*;
import javax.swing.tree.TreeNode;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;
public class MethodTreeNode extends TypeTreeNode {
	static Hashtable methodsTable;
	protected SootMethod method;
/**
 * MethodTreeNode constructor comment.
 */
public MethodTreeNode(SootMethod method) {
	super(method.getName());
	this.method = method;
	methodsTable.put(method, this);
	if (!method.isBodyStored(Jimple.v()))
		new BuildAndStoreBody(Jimple.v(), new StoredBody(ClassFile.v()), 0).resolveFor(method);
	JimpleBody body = (JimpleBody) method.getBody(Jimple.v());
	for (ca.mcgill.sable.util.Iterator i = body.getLocals().iterator(); i.hasNext();) {
		Local l = (Local) i.next();
		if (!l.getName().startsWith("JJJCTEMP$") && !"$ret".equals(l.getName()))
			addChild(new LocalTreeNode(l));
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3/24/00 12:20:36 AM)
 * @return javax.swing.table.TableCellRenderer
 * @param row int
 * @param column int
 */
public TableCellEditor getCellEditor(int column) {
	/*
	if (column == 2) {
	TableCellEditor editor;
	editor = new JTable().getDefaultEditor(Boolean.class);
	return editor;
	}
	*/
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
	if (column == 1) {
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText(method.getParameterTypes().toString());
		return renderer;
	} else
		/*
		if (column == 2) {
		TableCellRenderer renderer;
		renderer = new JTable().getDefaultRenderer(Boolean.class);
		return renderer;
		} else*/
		return null;
}
/**
 * Insert the method's description here.
 * Creation date: (5/19/00 2:43:38 PM)
 * @return ca.mcgill.sable.soot.SootMethod
 */
public SootMethod getMethod() {
	return method;
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:09:20 PM)
 * @return edu.ksu.cis.bandera.abps.options.MethodOptions
 */
public void getOption(Hashtable result) {
	HashSet locals = new HashSet();
	for (int i = 0; i < getChildCount(); i++) {
		LocalTreeNode local = (LocalTreeNode) getChildAt(i);
		if (local.getAbstraction() != null) {
			Local lcl = (Local) local.getVar();
			locals.add(lcl);
			LocalMethod lm = new LocalMethod(method, lcl);
			result.put(lm, local.getAbstraction());
		}
	}
	result.put(method, locals);
	((HashSet) result.get(method.getDeclaringClass())).add(method);
}
		/**
		 * Insert the method's description here.
		 * Creation date: (3/24/00 12:08:42 AM)
		 * @return java.lang.String
		 */
		public String getParams() {
				return method.getParameterTypes().toString().replace('{', '(').replace('}', ')');
		}
		/**
		 * Insert the method's description here.
		 * Creation date: (3/23/00 11:30:08 PM)
		 * @return java.lang.String
		 */
		public String toString() {
			String name = getName().toString();
			if ("<init>".equals(name)) {
				name = method.getDeclaringClass().getName();
				int i = name.lastIndexOf(".");
				if (i != -1) name = name.substring(i + 1);
			}
				return name;
		}
}
