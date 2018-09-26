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
import javax.swing.table.*;
import ca.mcgill.sable.soot.*;
import javax.swing.tree.TreeNode;
import org.apache.log4j.Category;

public class ClassTreeNode extends TypeTreeNode {

    /**
     * The SootClass for this node in the tree.
     */
    SootClass cls;

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(ClassTreeNode.class.getName());


    /**
     * ClassTreeNode constructor comment.
     */
    public ClassTreeNode(SootClass cls) {
	super(cls.getName());
	this.cls = cls;
	for (ca.mcgill.sable.util.Iterator i = cls.getFields().iterator(); i.hasNext();) {
	    SootField field = (SootField) i.next();
	    if (field.getName().indexOf("quantification$") == -1)
		addChild(new FieldTreeNode(field));
	}
	for (ca.mcgill.sable.util.Iterator i = cls.getMethods().iterator(); i.hasNext();) {
	    SootMethod method = (SootMethod) i.next();
	    if (!method.getName().equals("<clinit>"))
		addChild(new MethodTreeNode(method));
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
     * Add the abstraction options for this class to the Hashtable given.
     *
     * @param result java.util.Hashtable
     */
    public void getOption(Hashtable result) {

	result.put(cls, new HashSet());

	log.debug("class child count =  " + getChildCount());

	for (int i = 0; i < getChildCount(); i++) {
	    log.debug("looking at child " + i);
	    if (getChildAt(i) instanceof FieldTreeNode) {
		FieldTreeNode field = (FieldTreeNode) getChildAt(i);
		log.debug("child is a field ...");
		field.getOption(result);
	    } else {
		MethodTreeNode method = (MethodTreeNode) getChildAt(i);
		log.debug("child is a method ...");
		method.getOption(result);
	    }
	}
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/23/00 11:30:08 PM)
     * @return java.lang.String
     */
    public String toString() {
	return getName().toString();
    }
}
