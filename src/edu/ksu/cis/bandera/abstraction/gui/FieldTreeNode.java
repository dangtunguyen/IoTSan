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
import java.util.Vector;
import javax.swing.table.*;
import javax.swing.JComboBox;
import ca.mcgill.sable.soot.*;
import javax.swing.tree.TreeNode;
import javax.swing.DefaultCellEditor;
import edu.ksu.cis.bandera.abstraction.*;
import org.apache.log4j.Category;

public class FieldTreeNode extends TypeTreeNode implements VarTreeNode {
    static Vector fields;
    protected SootField field;
    protected Abstraction abstraction;
    protected javax.swing.JComboBox comboBox;

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(FieldTreeNode.class.getName());

/**
 * FieldTreeNode constructor comment.
 */
public FieldTreeNode(SootField field) {
	super(field.getName());
	this.field = field;
	fields.add(this);
	if (JTreeTable.absTable.get(field.getType()) != null) {
		comboBox = new JComboBox((Vector) JTreeTable.absTable.get(field.getType()));
		comboBox.setSelectedItem(ConcreteIntegralAbstraction.v());
	} else
		if (field.getType() instanceof RefType) {
			comboBox = new JComboBox();
			comboBox.addItem("No Selection");
			comboBox.addItem(ClassAbstraction.v(((RefType) field.getType()).className));
		} else {
			comboBox = new JComboBox();
			comboBox.addItem("No Selection");
		}
		/*else
			if (field.getType().getClass() == AbstractRefType.class) {
				comboBox = new JComboBox();
				comboBox.addItem(ObjectAbstraction.v((AbstractRefType) field.getType()).getType());
			} else
				if (field.getType().getClass() == IntType.class) {
					comboBox = new JComboBox();
					comboBox.addItem("4");
					comboBox.addItem(new Integer(3));
					comboBox.addItem("");
					comboBox.addItem("None");
				}*/
}
		public Abstraction getAbstraction() {
				return abstraction;
		}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 12:56:45 PM)
 * @return boolean
 */
public boolean getAllowsChildren() {
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (3/24/00 12:20:36 AM)
 * @return javax.swing.table.TableCellRenderer
 * @param row int
 * @param column int
 */
public TableCellEditor getCellEditor(int column) {
	if (column == 2)
		return new DefaultCellEditor(comboBox);
	else
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
 * 
 * @return javax.swing.JComboBox
 */
public javax.swing.JComboBox getComboBox() {
	return comboBox;
}
/**
 * Insert the method's description here.
 * Creation date: (5/12/00 11:01:52 AM)
 * @return ca.mcgill.sable.soot.SootField
 */
public SootField getField() {
	return field;
}
    /**
     * Insert the method's description here.
     * Creation date: (5/12/00 11:18:27 AM)
     * @return edu.ksu.cis.bandera.abps.options.FieldOptions
     */
    public void getOption(Hashtable result) {
	if (abstraction != null) {
	    log.debug("Adding this field's (" + field.getName() + ") abstraction (" + abstraction.toString() + ") to the table.");
	    ((HashSet) result.get(field.getDeclaringClass())).add(field);
	    result.put(field, abstraction);
	}
	else {
	    log.debug("No abstraction for this field (" + field.getName() + ").");
	}
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/23/00 11:50:00 PM)
     * @return ca.mcgill.sable.soot.Type
     */
    public Type getType() {
	return field.getType();
    }
    /**
     * Insert the method's description here.
     * Creation date: (5/19/00 2:31:39 PM)
     * @return java.lang.Object
     */
    public Object getVar() {
	return field;
    }
    public void setAbstraction(Abstraction type) {
	abstraction = type;
    }
    /**
     * size method comment.
     */
    public int size() {
	return 0;
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
