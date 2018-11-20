package edu.ksu.cis.bandera.sessions;

import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The LockGraph object provides a way to store lock graph
 * information into a session file.  It simply holds the root
 * object to be used in the lock graph.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class LockGraph extends Observable implements Cloneable {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(LockGraph.class.getName());

    /**
     * The root object of this lock graph.
     */
    private String root;

    /**
     * Get the root object for this graph.
     *
     * @param String The root object of this graph.
     */
    public String getRoot() {
	return(root);
    }

    /**
     * Set the root object for this graph.
     *
     * @param String root The object to be the root of this lock graph.
     */
    public void setRoot(String root) {
	this.root = root;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a copy of this LockGraph.
     *
     * @return Object A LockGraph object with the same values as this object.
     */
    public Object clone() {
	LockGraph lg = new LockGraph();

	lg.setRoot(root);

	return(lg);
    }

    /**
     * Test to see if the object given is equal to this object.  For this to
     * be true, the object given must be a LockGraph and the root must be
     * the same as this object.
     *
     * @param Object object The object to test for equivalence.
     * @return boolean True if the objects are equal, false otherwise.
     */
    public boolean equals(Object object) {
	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof LockGraph) {
	    LockGraph lg = (LockGraph)object;
	    if(((lg.root == null) && (root == null)) ||
	       (root.equals(lg.root))) {
		equal = true;
	    }
	}

	return(equal);
    }

    /**
     * Create a hash value for this object.
     *
     * @return int The hash value for this object.
     */
    public int hashCode() {

	int hash = 0;

	if(root != null) {
	    hash += root.hashCode();
	}

	return(hash);
    }

    /**
     * Create a String representation of this LockGraph.
     *
     * @return String A String representation of this LockGraph.
     */
    public String toString() {
	return("LockGraph with a root = " + root);
    }

}
