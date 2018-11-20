package edu.ksu.cis.bandera.sessions.ui.gui;

import java.util.Set;
import java.util.Iterator;

import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootField;
import ca.mcgill.sable.soot.SootMethod;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * The SootClassTreeModel provides a TreeModel that is based upon the parsing of ca.mcgill.sable.soot.SootClass
 * information.  This will consist of nodes of the following type:
 * <ul>
 * <li>ca.mcgill.sable.soot.SootClass</li>
 * <li>ca.mcgill.sable.soot.SootField</li>
 * <li>ca.mcgill.sable.soot.SootMethod</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:42 $
 */
public class SootClassTreeModel extends DefaultTreeModel {

    public SootClassTreeModel() {
	super(new DefaultMutableTreeNode("Empty"));
    }
    public SootClassTreeModel(Set classSet) {
	this();
	setRoot(buildRootNode(classSet));
    }
    private TreeNode buildRootNode(Set classSet) {

	DefaultMutableTreeNode applicationNode = new DefaultMutableTreeNode("Application");

	if((classSet != null) && (classSet.size() > 0)) {
	    Iterator classIterator = classSet.iterator();
	    while(classIterator.hasNext()) {
		Object o = classIterator.next();
		if(o == null) {
		    continue;
		}

		if(o instanceof SootClass) {
		    SootClass sc = (SootClass)o;
		    DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(sc);
		    applicationNode.add(classNode);
		    
		    // create nodes in a subtree for each field
		    ca.mcgill.sable.util.List fields = sc.getFields();
		    if((fields != null) && (fields.size() > 0)) {
			for(int i = 0; i < fields.size(); i++) {
			    DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(fields.get(i));
			    classNode.add(fieldNode);
			}
		    }
		    
		    // create nodes in a subtree for each method
		    ca.mcgill.sable.util.List methods = sc.getMethods();
		    if((methods != null) && (methods.size() > 0)) {
			for(int i = 0; i < methods.size(); i++) {
			    DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(methods.get(i));
			    classNode.add(methodNode);
			    // what about parameters & locals? -tcw
			}
		    }
		    continue;
		}
	    }
	}

	return(applicationNode);
    }
}
