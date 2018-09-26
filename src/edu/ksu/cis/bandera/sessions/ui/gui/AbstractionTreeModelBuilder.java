package edu.ksu.cis.bandera.sessions.ui.gui;

import java.util.Set;
import java.util.Iterator;

import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.lang.Class;

import ca.mcgill.sable.soot.SootClass;

import edu.ksu.cis.bandera.sessions.Session;

/**
 * The AbstractionTreeModelBuilder provides an easy and abstract way to
 * to create a TreeModel for use in Abstraction configuration.  This tree
 * will be based upon the set of clases given.  The root node will be the "application"
 * with sub-trees being for each class.  Each classes subtree will consist of that classes
 * fields and methods.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:42 $
 */
public class AbstractionTreeModelBuilder {


    public static TreeModel buildTreeModel(Set classSet, Session session) {

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
		treeModel = new ClassTreeModel(classSet);
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
    public static TreeCellRenderer getTreeCellRenderer(TreeModel treeModel) {

	TreeCellRenderer treeCellRenderer = null;

	if(treeModel == null) {
	    treeCellRenderer = new DefaultTreeCellRenderer();
	}
	else if(treeModel instanceof SootClassTreeModel) {
	    treeCellRenderer = new SootClassTreeCellRenderer();
	}
	else if(treeModel instanceof ClassTreeModel) {
	    treeCellRenderer = new ClassTreeCellRenderer();
	}
	else {
	    treeCellRenderer = new DefaultTreeCellRenderer();
	}

	return(treeCellRenderer);
    }
}
