package edu.ksu.cis.bandera.sessions;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.log4j.Category;

/**
 * The Abstraction class will hold the information to be used when
 * generating an abstract model of the application.  This will hold
 * abstraction information for classes, methods, and fields.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:26 $
 */
public final class Abstraction extends Observable implements Cloneable, Observer {

    /**
     *
     */
    private static final Category log = Category.getInstance(Abstraction.class.getName());

    /**
     * When there is no other abstraction specified for real numbers,
     * use this one.
     */
    private String defaultRealAbstraction;

    /**
     * When there is no other abstraction specified for integer numbers,
     * use this one.
     */
    private String defaultIntegralAbstraction;

    /**
     * A set of class descriptions that hold the abstractions for each class.
     */
    private Set classDescriptions;
  

    /**
     * Get all the class descriptions.
     *
     * @return Set The set of all class descriptions in this abstraction.
     */
    public Set getClassDescriptions() {
	return(classDescriptions);
    }

    /**
     * Get the default integer abstraction to use when no other abstraction is
     * specified for integers.
     *
     * @return String The default integer abstraction.
     */
    public String getDefaultIntegralAbstraction() {
	return(defaultIntegralAbstraction);
    }

    /**
     * Get the default real abstraction to use when no other abstraction is
     * specified for real numbers.
     *
     * @return String The default real abstraction.
     */
    public String getDefaultRealAbstraction() {
	return(defaultRealAbstraction);
    }

    /**
     * Add a class description to this abstraction.
     *
     * @param ClassDescription classDescription
     */
    public void addClassDescription(ClassDescription classDescription) {
	if(classDescription == null) {
	    return;
	}
	if(classDescriptions == null) {
	    classDescriptions = new HashSet();
	}
	boolean changed = classDescriptions.add(classDescription);
	if(changed) {
	    classDescription.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove the specified class description from out local store.
     *
     * @param ClassDescription classDescription The class description to
     *        be removed if it is in the local store.
     */
    public void removeClassDescription(ClassDescription classDescription) {
	if(classDescription == null) {
	    return;
	}
	if(classDescriptions == null) {
	    return;
	}
	boolean changed = classDescriptions.remove(classDescription);
	if(changed) {
	    classDescription.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Clear all class descriptions from this abstraction.
     */
    public void clearClassDescriptions() {
	if((classDescriptions == null) || (classDescriptions.size() <= 0)) {
	    return;
	}
	Iterator cdi = classDescriptions.iterator();
	while(cdi.hasNext()) {
	    ClassDescription cd = (ClassDescription)cdi.next();
	    cd.deleteObserver(this);
	}
	classDescriptions.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Set the default abstraction to use for real numbers.
     *
     * @param String
     */
    public void setDefaultRealAbstraction(String defaultRealAbstraction) {
	this.defaultRealAbstraction = defaultRealAbstraction;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the default abstraction to use for integers.
     *
     * @param String
     */
    public void setDefaultIntegralAbstraction(String defaultIntegralAbstraction) {
	this.defaultIntegralAbstraction = defaultIntegralAbstraction;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a new Abstraction that is identical in value to this Abstraction.
     *
     * @return Object An Abstraction object that has the same values as this object.
     */
    public Object clone() {
	Abstraction a = new Abstraction();

	a.setDefaultIntegralAbstraction(this.defaultIntegralAbstraction);
	a.setDefaultRealAbstraction(this.defaultRealAbstraction);

	Set cds = this.getClassDescriptions();
	if((cds != null) && (cds.size() > 0)) {
	    Iterator i = cds.iterator();
	    while(i.hasNext()) {
		ClassDescription cd = (ClassDescription)i.next();
		a.addClassDescription((ClassDescription)cd.clone());
	    }
	}

	return(a);
    }

    /**
     * Create a String representation of this object.  This will include the
     * default abstractions for real and integer numbers as well as the abstractions
     * defined for each class (and all fields, methods, and locals contained in them).
     *
     * @return String A String representation of this object.
     */
    public String toString() {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println("Abstraction:");
	pw.println("Default Abstraction - Real Numbers: " + defaultRealAbstraction);
	pw.println("Default Abstraction - Integers: " + defaultIntegralAbstraction);

	if((classDescriptions != null) && (classDescriptions.size() > 0)) {
	    Iterator classIterator = classDescriptions.iterator();
	    while(classIterator.hasNext()) {
		pw.println(classIterator.next().toString());
	    }
	}

	return(sw.toString());
    }

    /**
     * Determine if the given object is equalivant to this object.  For this to be the case
     * it must be an Abstraction object that has the same default abstractions for real and
     * integer numbers as well as all abstractions for classes, fields, methods, and locals.
     *
     * @return boolean True if the object given is equal to this object, false otherwise.
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
	else if(object instanceof Abstraction) {
	    Abstraction abstraction = (Abstraction)object;
	    if(((defaultIntegralAbstraction == null) && (abstraction.getDefaultIntegralAbstraction() == null)) ||
	       ((defaultIntegralAbstraction.equals(abstraction.getDefaultIntegralAbstraction())))) {
		if(((defaultRealAbstraction == null) && (abstraction.getDefaultRealAbstraction() == null)) ||
		   ((defaultRealAbstraction.equals(abstraction.getDefaultRealAbstraction())))) {
		    if(((classDescriptions == null) && (abstraction.getClassDescriptions() == null)) ||
		       (classDescriptions.equals(abstraction.getClassDescriptions()))) {
			equal = true;
		    }
		}
	    }
	}

	return(equal);
    }

    /**
     * Generate a hash value for this object.
     *
     * @return int The hash value for this object.
     */
    public int hashCode() {
	int hash = 0;
	if(defaultRealAbstraction != null) {
	    hash += defaultRealAbstraction.hashCode();
	}
	if(defaultIntegralAbstraction != null) {
	    hash += defaultIntegralAbstraction.hashCode();
	}
	if(classDescriptions != null) {
	    hash += classDescriptions.hashCode();
	}
	return(hash);
    }

    /**
     * Handle the updates to this object's children by sending a signal to
     * this object's parents.
     *
     * @param Observable o The object that changed (ClassDescription, FieldDescription, or MethodDesciption).
     * @param Object arg The arguments for the change (probably null).
     */
    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }

}
