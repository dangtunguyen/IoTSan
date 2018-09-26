package edu.ksu.cis.bandera.abstraction.gui;

import java.util.Enumeration;
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
import java.util.Stack;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import ca.mcgill.sable.soot.*;
import javax.swing.tree.TreeNode;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import edu.ksu.cis.bandera.abstraction.options.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;
import org.apache.log4j.Category;

public class TypeTree extends AbstractTreeTableModel implements TreeTableModel {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(TypeTree.class.getName());

	static protected String cName[] = {"Tree", "Type", "Abstraction", "Value", "Inferred Type"};
	static protected Class[] cTypes = {TreeTableModel.class, Type.class, Abstraction.class, Abstraction.class, String.class};
	protected edu.ksu.cis.bandera.abstraction.typeinference.TypeTable inferedTypes;
/**
 * TypeTree constructor comment.
 * @param root java.lang.Object
 */
public TypeTree() {
	super(new TypeTreeNode("empty"));
	inferedTypes = new TypeTable();
}
/**
 * TypeTree constructor comment.
 * @param root java.lang.Object
 */
public static AbstractTreeNode createTree(Vector classes) {
	MethodTreeNode.methodsTable = new Hashtable();
	LocalTreeNode.locals = new Vector();
	FieldTreeNode.fields = new Vector();
	AbstractTreeNode root = new TypeTreeNode("Packages");
	Hashtable packages = new Hashtable();
	for (int i = 0; i < classes.size(); i++) {
		SootClass c = (SootClass) classes.get(i);
		String pack;
		if (c.getName().lastIndexOf(".") >= 0)
			pack = c.getName().substring(0, c.getName().lastIndexOf("."));
		else
			pack = "";
		TypeTreeNode node = (TypeTreeNode) packages.get(pack);
		if (node == null) {
			if (pack.equals(""))
				node = new TypeTreeNode("Default Package");
			else
				node = new TypeTreeNode(pack);
			packages.put(pack, node);
			((AbstractTreeNode) root).addChild(node);
		}
		node.addChild(new ClassTreeNode(c));
	}
	return root;
}
/**
 * getChild method comment.
 */
public Object getChild(Object parent, int index) {
	if (parent instanceof AbstractTreeNode)
		return ((AbstractTreeNode) parent).getChildAt(index);
	else
		return null;
}
/**
 * getChildCount method comment.
 */
public int getChildCount(Object parent) {
	if (parent instanceof AbstractTreeNode)
		return ((AbstractTreeNode) parent).getChildCount();
	else
		return 0;
}
		public Class getColumnClass(int column) {
				return cTypes[column];
		}
		/**
		 * getColumnCount method comment.
		 */
		public int getColumnCount() {
				return cName.length;
		}
		/**
		 * getColumnName method comment.
		 */
		public String getColumnName(int column) {
				return cName[column];
		}
/**
 * Insert the method's description here.
 * Creation date: (5/19/00 2:27:38 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeTable
 */
public TypeTable getInferedTypes() {
	return inferedTypes;
}
/**
 * Getting the abstraction options from the TypeTree.  This will
 * be in the form of a Hashtable that ...?
 *
 * @return edu.ksu.cis.bandera.abps.options.Options
 */
public Hashtable getOptions() {

	Hashtable result = new Hashtable();
	int rootChildCount = getChildCount(root);
	log.debug("child count: " + rootChildCount);
	for (int i = 0; i < rootChildCount; i++) {
		Object pack = getChild(root, i);
		for (int j = 0; j < getChildCount(pack); j++) {
			ClassTreeNode cls = (ClassTreeNode) getChild(pack, j);
			log.debug("Getting the options from class " + cls.toString());
			cls.getOption(result);
		}
	}

	if(result == null) {
	    log.debug("The result table is null.");
	}
	else {
	    log.debug("The result table has a size of " + result.size());
	}

	return result;
}
/**
 * getValueAt method comment.
 */
public Object getValueAt(Object node, int column) {
	if (node instanceof VarTreeNode) {
		VarTreeNode vnode = (VarTreeNode) node;
		switch (column) {
			case 0 :
				return vnode.getName();
			case 1 :
				return vnode.getType();
			case 2 :
				return vnode.getAbstraction();
			case 4 :
				//System.out.print(vnode.getVar() + "\t");
				//System.out.println(inferedTypes.get(vnode.getVar()));
				return inferedTypes.get(vnode.getVar());
		}
	} else
		if (node instanceof MethodTreeNode) {
			MethodTreeNode mnode = (MethodTreeNode) node;
			switch (column) {
				case 0 :
					return mnode.getName();
				case 1 :
					return mnode.getParams();
			}
		} else
			if (node instanceof TypeTreeNode) {
				TypeTreeNode tnode = (TypeTreeNode) node;
				switch (column) {
					case 0 :
						return tnode.getName();
				}
			} else
				switch (column) {
				case 0 :
					if (node instanceof Vector)
						return "Root";
					else
						if (node instanceof SootClass)
							return ((SootClass) node).getName();
						else
							if (node instanceof SootField)
								return ((SootField) node).getName();
							else
								return null;
				case 1 :
					if (node instanceof SootField)
						return ((SootField) node).getType();
					else
						if (node instanceof Local)
							return ((Local) node).getType();
						else
							return "";
			}
	return "";
}
/** By default, make the column with the Tree in it the only editable one. 
 *  Making this column editable causes the JTable to forward mouse 
 *  and keyboard events in the Tree column to the underlying JTree. 
 */
public boolean isCellEditable(Object node, int column) {
	return super.isCellEditable(node, column) || ((node instanceof MethodTreeNode || node instanceof VarTreeNode) && column == 2);
}
/**
 * Insert the method's description here.
 * Creation date: (5/19/00 2:27:38 PM)
 * @param newInferedTypes edu.ksu.cis.bandera.abps.typing.TypeTable
 */
public void setInferedTypes(TypeTable newInferedTypes) {
	inferedTypes = newInferedTypes;
	Iterator i;
	if (LocalTreeNode.locals != null) {
		i = LocalTreeNode.locals.iterator();
		while (i.hasNext())
			nodeChanged(((TreeNode) i.next()).getParent());
	}
	if (FieldTreeNode.fields != null) {
		i = FieldTreeNode.fields.iterator();
		while (i.hasNext())
			nodeChanged((TreeNode) i.next());
	}
}
/**
 * 
 * @param table java.util.Hashtable
 */
public void setSelectedTypes(Hashtable table) {
	RefHashTable refTable = new RefHashTable();
	for (Iterator i = LocalTreeNode.locals.iterator(); i.hasNext();) {
		LocalTreeNode ltn = (LocalTreeNode) i.next();
		refTable.put(ltn.getVar(), ltn);
	}
	for (Iterator i = FieldTreeNode.fields.iterator(); i.hasNext();) {
		FieldTreeNode ftn = (FieldTreeNode) i.next();
		refTable.put(ftn.getField(), ftn);
	}
	for (Enumeration e = table.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		if (key instanceof SootField) {
			FieldTreeNode ftn = (FieldTreeNode) refTable.get(key);
			ftn.setAbstraction((Abstraction) table.get(key));
			nodeChanged(ftn.getParent());
		} else if (key instanceof LocalMethod) {
			LocalMethod lm = (LocalMethod) key;
			LocalTreeNode ltn = (LocalTreeNode) refTable.get(lm.getLocal());
			ltn.setAbstraction((Abstraction) table.get(key));
			nodeChanged(ltn.getParent());
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (5/4/00 2:57:29 PM)
 * @param node edu.ksu.cis.bandera.abps.typing.gui.TreeNode
 */
public void setTree(AbstractTreeNode node) {
	root = node;
	inferedTypes = new TypeTable();
}
public void setValueAt(Object aValue, Object node, int column) {
	if (node instanceof VarTreeNode) {
		VarTreeNode vnode = (VarTreeNode) node;
		//System.out.println("\t" + aValue.getClass());
		switch (column) {
			case 2 :
				if (aValue instanceof Abstraction)
					vnode.setAbstraction((Abstraction) aValue);
				else
					vnode.setAbstraction(null);
				break;
		}
	}
}
}
