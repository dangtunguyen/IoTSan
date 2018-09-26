package edu.ksu.cis.bandera.sessions;

import edu.ksu.cis.bandera.sessions.FieldDescription;
import edu.ksu.cis.bandera.sessions.MethodDescription;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.log4j.Category;

/**
 * The ClassDescription provides a way to collect abstractions for fields
 * and methods into one convienent location.  
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class ClassDescription extends Observable implements Cloneable, Observer {

    /**
     * The log that messages will be written to.
     */
    private static final Category log = Category.getInstance(ClassDescription.class.getName());

    /**
     * The name of the class this description is for.
     */
    private String name;

    /**
     * A set of method descriptions.
     */
    private Set methodDescriptions;

    /**
     * A set of field descriptions.
     */
    private Set fieldDescriptions;

    /**
     * Get all the field descriptions.
     *
     * @return Set A set of all field descriptions for this class's fields.
     */
    public Set getFieldDescriptions() {
	return(fieldDescriptions);
    }

    /**
     * Get all the method descriptions
     *
     * @return Set A set of all method descriptions for this class's methods.
     */
    public Set getMethodDescriptions() {
	return(methodDescriptions);
    }

    /**
     * Get the name of the class this class description is for.
     *
     * @return String The name of the class this description describes.
     */
    public String getName() {
	return(name);
    }

    /**
     * Clear out all the field descriptions.
     */
    public void clearFieldDescriptions() {
	if((fieldDescriptions == null) || (fieldDescriptions.size() <= 0)) {
	    return;
	}

	Iterator fdi = fieldDescriptions.iterator();
	while(fdi.hasNext()) {
	    FieldDescription fd = (FieldDescription)fdi.next();
	    fd.deleteObserver(this);
	}

	fieldDescriptions.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Clear out all the method descriptions.
     */
    public void clearMethodDescriptions() {
	if((methodDescriptions == null) || (methodDescriptions.size() <= 0)) {
	    return;
	}

	Iterator mdi = methodDescriptions.iterator();
	while(mdi.hasNext()) {
	    MethodDescription md = (MethodDescription)mdi.next();
	    md.deleteObserver(this);
	}

	methodDescriptions.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Add a field description to our local store.
     *
     * @param FieldDescription fieldDescription
     */
    public void addFieldDescription(FieldDescription fieldDescription) {
	if(fieldDescription == null) {
	    return;
	}
	if(fieldDescriptions == null) {
	    fieldDescriptions = new HashSet();
	}
	boolean changed = fieldDescriptions.add(fieldDescription);
	if(changed) {
	    fieldDescription.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Add a method description to out local store.
     *
     * @param MethodDescription methodDescription
     * @pre methodDescription != null
     * @post methodDescription will be in methodDescriptions
     */
    public void addMethodDescription(MethodDescription methodDescription) {
	if(methodDescription == null) {
	    return;
	}
	if(methodDescriptions == null) {
	    methodDescriptions = new HashSet();
	}
	boolean changed = methodDescriptions.add(methodDescription);
	if(changed) {
	    methodDescription.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove the given field description from our local store.
     *
     * @param FieldDescription fieldDescription
     * @pre fieldDescription != null
     * @pre fieldDescriptions != null
     * @post fieldDescription will not be in fieldDescriptions
     */
    public void removeFieldDescription(FieldDescription fieldDescription) {
	if(fieldDescription == null) {
	    return;
	}
	if(fieldDescriptions == null) {
	    return;
	}
	boolean changed = fieldDescriptions.remove(fieldDescription);
	if(changed) {
	    fieldDescription.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove the given method description from our local store.
     *
     * @param MethodDescription methodDescription
     * @pre methodDescription != null
     * @pre methodDescriptions != null
     * @post methodDescription will not be in methodDescriptions
     */
    public void removeMethodDescription(MethodDescription methodDescription) {
	if(methodDescription == null) {
	    return;
	}
	if(methodDescriptions == null) {
	    return;
	}
	boolean changed = methodDescriptions.remove(methodDescription);
	if(changed) {
	    methodDescription.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Set the name of this class.
     *
     * @param String name The name of this class.
     */
    public void setName(String name) {
	this.name = name;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a new ClassDescription that has the same name, fields, and
     * methods as this object.
     *
     * @return Object A ClassDescription that is identical in value to this object.
     */
    public Object clone() {
	ClassDescription cd = new ClassDescription();

	cd.setName(this.getName());

	Set mds = this.getMethodDescriptions();
	if((mds != null) && (mds.size() > 0)) {
	    Iterator i = mds.iterator();
	    while(i.hasNext()) {
		MethodDescription md = (MethodDescription)i.next();
		cd.addMethodDescription((MethodDescription)md.clone());
	    }
	}

	Set fds = this.getFieldDescriptions();
	if((fds != null) && (fds.size() > 0)) {
	    Iterator i = fds.iterator();
	    while(i.hasNext()) {
		FieldDescription fd = (FieldDescription)i.next();
		cd.addFieldDescription((FieldDescription)fd.clone());
	    }
	}

	return(cd);
    }

    /**
     * Test the equality of the given object with this object.  This will
     * return true only when the object given is a ClassDescription that
     * has the same name, fields, and methods as this object.
     *
     * @return boolean True if the objects are equal, false otherwise.
     * @param Object An object to test for equality.
     */
    public boolean equals(Object object) {
	boolean equal = false;

	if(object == null) {
	    log.debug("Testing a null object for equality ... returning false.");
	    equal = false;
	}
	else if(object == this) {
	    log.debug("The object points to the same object ... return true.");
	    equal = true;
	}
	else if(object instanceof ClassDescription) {
	    ClassDescription cd = (ClassDescription)object;
	    if(((name == null) && (cd.name == null)) ||
	       ((name != null) && (name.equals(cd.getName())))) {
		if(((fieldDescriptions == null) && (cd.getFieldDescriptions() == null)) ||
		   (fieldDescriptions.equals(cd.getFieldDescriptions()))) {
		    if(((methodDescriptions == null) && (cd.getMethodDescriptions() == null)) ||
		       (methodDescriptions.equals(cd.getMethodDescriptions()))) {
			equal = true;
		    }
		    else {
			log.debug("MethodDescriptions are not equal.");
		    }
		}
		else {
		    log.debug("FieldDescriptions are not equal.");
		}
	    }
	    else {
		log.debug("Name is not equal.");
	    }
	}

	return(equal);
    }

    /**
     * Return a hash for this object.
     *
     * @return int A hash value for this object.
     */
    public int hashCode() {
	int hash = 0;

	if(name != null) {
	    hash += name.hashCode();
	}

	if(fieldDescriptions != null) {
	    hash += fieldDescriptions.hashCode();
	}

	if(methodDescriptions != null) {
	    hash += methodDescriptions.hashCode();
	}

	return(hash);
    }

    /**
     * Get a String representation for this ClassDescription.  This will consist
     * of the class name, a listing of the class' fields and corresponding abstractions,
     * and a listing of the class' methods (which will include any locals and corresponding
     * abstractions).
     *
     * @return String A String representation of this object.
     */
    public String toString() {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println(name + "{");
	if((fieldDescriptions != null) && (fieldDescriptions.size() > 0)) {
	    Iterator fieldIterator = fieldDescriptions.iterator();
	    while(fieldIterator.hasNext()) {
		pw.println(fieldIterator.next().toString());
	    }
	}
	if((methodDescriptions != null) && (methodDescriptions.size() > 0)) {
	    Iterator methodIterator = methodDescriptions.iterator();
	    while(methodIterator.hasNext()) {
		pw.println(methodIterator.next().toString());
	    }
	}
	pw.println("}");

	return(sw.toString());
    }

    /**
     * When a child of this ClassDescription (FieldDescription and MethodDescription) is updated,
     * we should report that update to our parent.
     *
     * @param Observable o The object that was updated (either a FieldDescription or MethodDescription).
     * @param Object arg The argument (probably null).
     */
    public void update(Observable o, Object args) {
	setChanged();
	notifyObservers();
    }
}
