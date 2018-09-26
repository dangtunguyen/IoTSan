package edu.ksu.cis.bandera.sessions;

import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The ObjectDiagram class provides a way to store counter example
 * object diagram information in the session file.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class ObjectDiagram extends Observable implements Cloneable {

    /**
     * The log where messages will be written.
     */
    private static final Category log = Category.getInstance(ObjectDiagram.class.getName());

    /**
     * The root object for the object diagram.
     */
    private String root;

    /**
     * Get the root object where the diagram should start.
     *
     * @return String
     */
    public String getRoot() {
	return(root);
    }

    /**
     * Set the root object where the diagram should start.
     *
     * @param String root
     */
    public void setRoot(String root) {
	this.root = root;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a new ObjectDiagram that has the same root as this ObjectDiagram.
     *
     * @return Object An ObjectDiagram with the same root as this one.
     */
    public Object clone() {
	ObjectDiagram od = new ObjectDiagram();

	od.setRoot(this.getRoot());

	return(od);
    }

    /**
     * Test to see if the given object is equivalent to this ObjectDiagram.  For this
     * to be true, the object given must be an ObjectDiagram with the same root.
     *
     * @return boolean True if the object given is equal to this object.
     * @param Object object The object to test for equality.
     */
    public boolean equals(Object object) {

	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof ObjectDiagram) {
	    ObjectDiagram od = (ObjectDiagram)object;
	    if(((od.root == null) && (root == null)) ||
	       (root.equals(od.root))) {
		equal = true;
	    }
	}

	return(equal);
    }

    /**
     * Generate the hash for this object.
     *
     * @return int A hash.
     */
    public int hashCode() {

	int hash = 0;

	if(root != null) {
	    hash += root.hashCode();
	}

	return(hash);
    }

    /**
     * Create a String representation of this object.
     *
     * @return String A String representation of this object.
     */
    public String toString() {
	return("ObjectDiagram - root: " + root);
    }

}
