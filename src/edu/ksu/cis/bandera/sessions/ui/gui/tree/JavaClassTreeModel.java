package edu.ksu.cis.bandera.sessions.ui.gui.tree;

import java.util.Set;
import java.util.Iterator;

import java.lang.Class;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The ClassTreeModel provides a TreeModel that is based upon the parsing of java.lang.Class
 * information.  This will consist of nodes of the following type:
 * <ul>
 * <li>java.lang.Class</li>
 * <li>java.lang.reflect.Field</li>
 * <li>java.lang.reflect.Method</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:43 $
 */
public class JavaClassTreeModel extends DefaultTreeModel {

    public JavaClassTreeModel() {
	super(new DefaultMutableTreeNode("Empty"));
    }
    public JavaClassTreeModel(Set classSet) {
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

		if(o instanceof Class) {
		    Class c = (Class)o;
		    // create a node in the tree for this class
		    DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(c);
		    applicationNode.add(classNode);
		    
		    // create nodes in a subtree for each field
		    Field[] fields = c.getDeclaredFields();
		    if((fields != null) && (fields.length > 0)) {
			for(int i = 0; i < fields.length; i++) {
			    DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(fields[i]);
			    classNode.add(fieldNode);
			}
		    }
		    
		    // create nodes in a subtree for each method
		    Method[] methods = c.getDeclaredMethods();
		    if((methods != null) && (methods.length > 0)) {
			for(int i = 0; i < methods.length; i++) {
			    DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(methods[i]);
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
