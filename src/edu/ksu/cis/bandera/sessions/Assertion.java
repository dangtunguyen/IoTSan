package edu.ksu.cis.bandera.sessions;

import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The Assertion object provides a simple data structure for defining
 * assertions for a Specification and keeping track of the active assertions
 * (those that should be checked at any particular time).
 *
 * The Assertion object holds a name (this will relate to the name defined in
 * BSL) and a flag to determine if it is active (enable/disable).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class Assertion extends Observable implements Cloneable {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(Assertion.class.getName());

    /**
     * The flag that defines is this assertions is enabled to be checked in
     * a specification.  In other words, if this is enabled, this assertion
     * will be checked when the model checker runs.
     */
    private boolean enable;

    /**
     * The name of this assertion.
     */
    private String name;

    /**
     * Get the name of this assertion.
     *
     * @return String The name of this assertion.
     */
    public String getName() {
	return(name);
    }

    /**
     * Is this assertion enabled?
     *
     * @return boolean True if this assertion is enabled, false otherwise.
     */
    public boolean enabled() {
	return(enable);
    }

    /**
     * Set the name of this assertion.
     *
     * @param String name The name of this assertion.
     */
    public void setName(String name) {
	this.name = name;
	setChanged();
	notifyObservers();
    }

    /**
     * Set this assertion as enable flag.  If true, this
     * assertion is enabled.
     *
     * @param booelan enable
     */
    public void setEnabled(boolean enable) {
	this.enable = enable;
	setChanged();
	notifyObservers();
    }

    /**
     * Enable this assertion.
     */
    public void enable() {
	setEnabled(true);
    }

    /**
     * Disable this assertion.
     */
    public void disable() {
	setEnabled(false);
    }

    /**
     * Create a new Assertion that has the same name and enabled status
     * as this Assertion.
     *
     * @return Object A new Assertion with the same name and enabled status
     *         as this object.
     */
    public Object clone() {
	Assertion a = new Assertion();

	a.setName(this.getName());
	a.setEnabled(this.enabled());

	return(a);
    }

    /**
     * Create a String representation of this object.
     *
     * @return String
     */
    public String toString() {

	String enabledStatus = "";
	if(this.enabled()) {
	    enabledStatus = "enabled.";
	}
	else {
	    enabledStatus = "disabled.";
	}

	return("Assertion " + name + " is " + enabledStatus);
    }

    /**
     * Determine if the given object is equal to this object.  This
     * will be true only when the given object is an Assertion and
     * the name and enabled status are the same.
     *
     * @param Object object The object to test for equality.
     * @return boolean True when the object is equal, false otherwise.
     */
    public boolean equals(Object object) {

	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof Assertion) {
	    Assertion a = (Assertion)object;
	    if((((name == null) && (a.name == null)) ||
	       (name.equals(a.name))) &&
	       (enable == a.enable)) {
		equal = true;
	    }
	}

	return(equal);
    }

    /**
     * Generate a hash for this object.
     *
     * @return int A hash value.
     */
    public int hashCode() {

	int hash = 0;

	if(name != null) {
	    hash += name.hashCode();
	}

	return(hash);
    }

}
