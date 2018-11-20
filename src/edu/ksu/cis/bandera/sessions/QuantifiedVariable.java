package edu.ksu.cis.bandera.sessions;

import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The QuantifiedVariable holds the name and type of a variable that
 * will be used in quantification.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class QuantifiedVariable extends Observable implements Cloneable {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(QuantifiedVariable.class.getName());

    /**
     * The name of the quantified variable.
     */
    private String name;

    /**
     * The type of the quantified variable.
     */
    private String type;

    /**
     * Get the name of the quantified variable.
     *
     * @return String
     */
    public String getName() {
	return(name);
    }

    /**
     * Get the type of the quantified variable.
     *
     * @return String
     */
    public String getType() {
	return(type);
    }

    /**
     * Set the name of the quantified variable.
     *
     * @param String name
     */
    public void setName(String name) {
	this.name = name;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the type of the quantified variable.
     *
     * @param String type
     */
    public void setType(String type) {
	this.type = type;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a new QuantifiedVariable that has the same name and type
     * as this QuantifiedVariable.
     *
     * @return Object A new QuantifiedVariable with the same name and type
     *         as this one.
     */
    public Object clone() {
	QuantifiedVariable qv = new QuantifiedVariable();

	qv.setName(this.getName());
	qv.setType(this.getType());

	return(qv);
    }

    /**
     * Test the equality of the given object to this object.  This will return true
     * only when the given object is a QuantifiedVariable that has the same name and
     * type as this one.
     *
     * @param Object object The object to test for equality.
     * @return boolean True if the given object is equal, false otherwise.
     */
    public boolean equals(Object object) {
	boolean equal = false;
	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof QuantifiedVariable) {
	    QuantifiedVariable qv = (QuantifiedVariable)object;
	    if((((name == null) && (qv.name == null)) ||
	       (name.equals(qv.name))) &&
	       (((type == null) && (qv.type == null)) ||
		(type.equals(qv.type)))) {
		equal = true;
	    }
	}
	return(equal);
    }

    /**
     * Get the hash for this object.
     *
     * @return int A hash value.
     */
    public int hashCode() {
	int hash = 0;
	if(name != null) {
	    hash += name.hashCode();
	}
	if(type != null) {
	    hash += type.hashCode();
	}
	return(hash);
    }

    /**
     * Create a String representation of this object.
     *
     * @return String A String representation of this object.
     */
    public String toString() {
	return("Quantified Variable: " + name + " : " + type);
    }

}
