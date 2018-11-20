package edu.ksu.cis.bandera.sessions.ui.gui.tree.variable;

import java.util.Set;
import java.util.Iterator;

import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.lang.Class;

import java.lang.reflect.Field;

import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootField;

import edu.ksu.cis.bandera.sessions.Session;

import edu.ksu.cis.bandera.sessions.ui.gui.tree.SootClassTreeModel;
import edu.ksu.cis.bandera.sessions.ui.gui.tree.JavaClassTreeModel;

/**
 * The AbstractionTreeModelBuilder provides an easy and abstract way to
 * to create a TreeModel for use in Abstraction configuration.  This tree
 * will be based upon the set of clases given.  The root node will be the "application"
 * with sub-trees being for each class.  Each classes subtree will consist of that classes
 * fields and methods.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:43 $
 */
public class VariableTreeModelBuilder {

    public static TreeModel buildTreeModel(Set classSet) {

	TreeModel treeModel = null;

	if((classSet == null) || (classSet.size() < 1)) {
	    // build an empty tree model
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode("Empty Tree");
	    DefaultTreeModel dtm = new DefaultTreeModel(node);
	    treeModel = dtm;
	}
	else {
	    // determine the type of object in the classSet
	    Iterator classIterator = classSet.iterator();
	    Object o = classIterator.next();
	    if(o instanceof Class) {
		treeModel = new JavaClassTreeModel(classSet);
	    }
	    else if(o instanceof SootClass) {
		treeModel = new SootClassTreeModel(classSet);
	    }
	    else {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Unknown type.");
		DefaultTreeModel dtm = new DefaultTreeModel(node);
		treeModel = dtm;
	    }
	}

	return(treeModel);
    }
/**
 * If this node is a class, get the name of the class and return it.
 *
 * @return String
 * @param node java.lang.Object
 */
public static String getClassName(Object node) {
	if(node == null) {
		return("");
	}
	else if(isClassNode(node)) {
		return(node.toString());
	}
	else {
		return("");
	}
}
/**
 * If this node is a field, get the name of the field and return it.
 *
 * @return String
 * @param node java.lang.Object
 */
public static String getFieldName(Object node) {
	if(node == null) {
		return("");
	}
	else if(isFieldNode(node)) {
		if(node instanceof DefaultMutableTreeNode) {
			node = ((DefaultMutableTreeNode)node).getUserObject();
		}
		if(node instanceof SootField) {
			SootField sf = (SootField)node;
			return(sf.getName());
		}
		else if(node instanceof Field) {
			Field f = (Field)node;
			return(f.getName());
		}
		else {
			return("");
		}
	}
	else {
		return("");
	}
}
/**
 * If this node is a method local, get the name of the local and return it.
 *
 * @return String
 * @param node java.lang.Object
 */
public static String getMethodLocalName(Object node) {
	if(node == null) {
		return("");
	}
	else if(isMethodLocalNode(node)) {
		return(node.toString());
	}
	else {
		return("");
	}
}
/**
 * If this node is a method, get the name of the method and return it.
 *
 * @return String
 * @param node java.lang.Object
 */
public static String getMethodName(Object node) {
	if(node == null) {
		return("");
	}
	else {
		return(node.toString());
	}
}
    public static TreeCellRenderer getTreeCellRenderer(TreeModel treeModel) {

	TreeCellRenderer treeCellRenderer = null;

	if(treeModel == null) {
	    treeCellRenderer = new DefaultTreeCellRenderer();
	}
	else if(treeModel instanceof SootClassTreeModel) {
	    treeCellRenderer = new SootClassTreeCellRenderer();
	}
	else if(treeModel instanceof JavaClassTreeModel) {
	    treeCellRenderer = new JavaClassTreeCellRenderer();
	}
	else {
	    treeCellRenderer = new DefaultTreeCellRenderer();
	}

	return(treeCellRenderer);
    }
/**
 * Determine if the given object is a class node in the tree.  This will basically test
 * if this is a SootClass or Class object.  If so, return true.  Otherwise, return false.
 *
 * @return boolean
 * @param node java.lang.Object
 */
public static boolean isClassNode(Object node) {
	if(node == null) {
		return(false);
	}
	if(node instanceof DefaultMutableTreeNode) {
		node = ((DefaultMutableTreeNode)node).getUserObject();
	}

	if(node instanceof SootClass) {
		return(true);
	}
	else if(node instanceof Class) {
		return(true);
	}
	else {
		return(false);
	}
}
/**
 * Determine if the given object is a field node in the tree.  This will basically test
 * if this is a SootField or Field object.  If so, return true.  Otherwise, return false.
 *
 * @return boolean
 * @param node java.lang.Object
 */
public static boolean isFieldNode(Object node) {
	if(node == null) {
		return(false);
	}
	
	if(node instanceof DefaultMutableTreeNode) {
		node = ((DefaultMutableTreeNode)node).getUserObject();
	}
	
	if(node instanceof SootField) {
		return(true);
	}
	else if(node instanceof Field) {
		return(true);
	}
	else {
		return(false);
	}
}
/**
 * Determine if the given object is a method local node in the tree.  This will basically test
 * if ...
 *
 * @return boolean
 * @param node java.lang.Object
 */
public static boolean isMethodLocalNode(Object node) {
	if(node == null) {
		return(false);
	}
	else if(!isClassNode(node) && !isFieldNode(node)) {
		return(true);
	}
	else {
		return(false);
	}
}
}
