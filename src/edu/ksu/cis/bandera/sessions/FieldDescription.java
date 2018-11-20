package edu.ksu.cis.bandera.sessions;

import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The FieldDescription object provides a way to store abstraction information
 * for a named field.  The abstraction will be a class name for the abstraction
 * to use for this field.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class FieldDescription extends Observable implements Cloneable {

    /**
     * The log that messages will be written to.
     */
    private static final Category log = Category.getInstance(FieldDescription.class.getName());

    /**
     * The name of the field.
     */
    private String name;

    /**
     * The abstraction to use on this field.
     */
    private String abstraction;

    /**
     * Get the name of this field.
     *
     * @return String The name of the field.
     */
    public String getName() {
	return(name);
    }

    /**
     * Get the abstraction to use on this field.
     *
     * @return String The abstraction to use on this field.
     */
    public String getAbstraction() {
	return(abstraction);
    }

    /**
     * Set the name of this field.
     *
     * @param String name The name of this field.
     */
    public void setName(String name) {
	this.name = name;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the abstraction to use on this field.
     *
     * @param String abstraction The abstraction to use on this field.
     */
    public void setAbstraction(String abstraction) {
	this.abstraction = abstraction;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a new FieldDescription with the same name and abstraction
     * as this one.
     *
     * @return Object A duplicate of this object (same name and abstraction).
     */
    public Object clone() {
	FieldDescription fd = new FieldDescription();

	fd.setName(this.getName());
	fd.setAbstraction(this.getAbstraction());

	return(fd);
    }

    /**
     * Determine if the object given is equal to this object.  This is the case
     * only when the given object is a FieldDescription whose name and abstraction
     * are equal to the name and abstraction in this object.
     *
     * @return boolean True if the object given is equivalent to this one, False otherwise.
     * @param Object object The object to test for equivalence.
     */
    public boolean equals(Object object) {
	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    // since the objects actually point to the same thing, skip any more checking!
	    equal = true;
	}
	else if(object instanceof FieldDescription) {
	    // determine if the name and abstraction values are equal for these two fields/locals
	    FieldDescription fd = (FieldDescription)object;

	    if(((name == null) && (fd.getName() == null)) ||
	       (name.equals(fd.getName()))) {
		if(((abstraction == null) && (fd.getAbstraction() == null)) ||
		   (abstraction.equals(fd.getAbstraction()))) {
		    equal = true;
		}
	    }
	}

	return(equal);
    }

    /**
     * Get the hash code for this object.  This method will use the hash codes generated
     * by it's name and abstraction objects added together.
     *
     * @return int
     */
    public int hashCode() {
	int hash = 0;

	if(name != null) {
	    hash += name.hashCode();
	}
	if(abstraction != null) {
	    hash += abstraction.hashCode();
	}

	return(hash);
    }

    /**
     * Get the String representation of this object.
     * This will return a String that contains a concatenation of
     * the name of this field/local, an arrow, and the field/local abstraction:
     * name -> abstraction
     *
     * @return String
     */
    public String toString() {
	return(name + " -> " + abstraction);
    }
}
