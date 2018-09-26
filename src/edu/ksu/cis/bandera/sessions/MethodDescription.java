package edu.ksu.cis.bandera.sessions;

import edu.ksu.cis.bandera.sessions.FieldDescription;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Category;

/**
 * The MethodDescription holds abstraction information for a method.  It
 * holds the name of the method, the return type of the method, and
 * a set of locals declared in the method (and the abstraction used
 * for each local).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class MethodDescription extends Observable implements Observer {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(MethodDescription.class.getName());

    /**
     * The name of this method.
     */
    private String name;

    /**
     * The return type of this method.
     */
    private String returnType;

    /**
     * A set of all local variables declared in this method.
     */
    private Set locals;

    /**
     * Get the name of this method.
     *
     * @return String The name of this method.
     */
    public String getName() {
	return(name);
    }

    /**
     * Get the return type for this method.
     *
     * @return String The return type of this method.
     */
    public String getReturnType() {
	return(returnType);
    }

    /**
     * Get the set of all locals declared in this method.
     *
     * @return Set
     */
    public Set getLocals() {
	return(locals);
    }

    /**
     * Set the name of this method.
     *
     * @param String name The name of the method.
     */
    public void setName(String name) {
	this.name = name;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the return type for this method.
     *
     * @param String returnType
     */
    public void setReturnType(String returnType) {
	this.returnType = returnType;
	setChanged();
	notifyObservers();
    }

    /**
     * Add the given local to the local store.
     *
     * @param FieldDescription local
     */
    public void addLocal(FieldDescription local) {
	if(local == null) {
	    return;
	}
	if(locals == null) {
	    locals = new HashSet();
	}
	boolean changed = locals.add(local);
	if(changed) {
	    local.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove all locals from the local store.
     */
    public void clearLocals() {
	if((locals == null) || (locals.size() <= 0)) {
	    return;
	}
	Iterator li = locals.iterator();
	while(li.hasNext()) {
	    FieldDescription fd = (FieldDescription)li.next();
	    fd.deleteObserver(this);
	}
	locals.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Remove the given local from the local store.
     *
     * @param FieldDescription local
     */
    public void removeLocal(FieldDescription local) {
	if(local == null) {
	    return;
	}
	if(locals == null) {
	    return;
	}

	boolean changed = locals.remove(local);
	if(changed) {
	    local.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Create a new MethodDescription that has the same return type,
     * name, and locals (FieldDescription).
     *
     * @return Object A new MethodDescription that is identical in value
     *         to this object.
     */
    public Object clone() {
	MethodDescription md = new MethodDescription();

	md.setName(this.getName());
	md.setReturnType(this.getReturnType());

	Set fds = this.getLocals();
	if((fds != null) && (fds.size() > 0)) {
	    Iterator i = fds.iterator();
	    while(i.hasNext()) {
		FieldDescription fd = (FieldDescription)i.next();
		md.addLocal((FieldDescription)fd.clone());
	    }
	}

	return(md);
    }

    /**
     * Determine if this object is equal to another object.  This will
     * only return true when the object given is a MethodDescription,
     * it has the same return type and method name as well as all the
     * locals (FieldDescription) are equal.
     *
     * @param Object object The object to test for equality.
     * @return boolean True if these two objects are equalivalent, false otherwise.
     */
    public boolean equals(Object object) {
	boolean equal = false;

	if(object == null) {
	    log.debug("Equality testing with a null object ... returning false.");
	    equal = false;
	}
	else if(object == this) {
	    log.debug("The two objects are the same (==).");
	    equal = true;
	}
	else if(object instanceof MethodDescription) {
	    MethodDescription md = (MethodDescription)object;
	    if(((name == null) && (md.getName() == null)) ||
	       (name.equals(md.getName()))) {
		if(((returnType == null) && (md.getReturnType() == null)) ||
		   (returnType.equals(md.getReturnType()))) {
		    if(((locals == null) && (md.getLocals() == null)) ||
		       (locals.equals(md.getLocals()))) {
			equal = true;
		    }
		    else {
			log.debug("Locals don't match.");
		    }
		}
		else {
		    log.debug("Return type doesn't match.");
		}
	    }
	    else {
		log.debug("Name doesn't match.");
	    }
	}

	return(equal);
    }

    /**
     * Generate the hash value of this object.
     *
     * @return int A hash value.
     */
    public int hashCode() {
	int hash = 0;

	if(name != null) {
	    hash += name.hashCode();
	}

	if(returnType != null) {
	    hash += returnType.hashCode();
	}

	if(locals != null) {
	    hash += locals.hashCode();
	}

	return(hash);
    }

    /**
     * Create a String representation of this object.  In this case we will
     * print the name of the method, the return type, and then all the
     * locals that are defined in this method.  Those locals should
     * print the associated abstraction as well.
     *
     * @return String A String representation of this method and it's locals.
     */
    public String toString() {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println(returnType + " " + name + " {");
	if((locals != null) && (locals.size() > 0)) {
	    Iterator localIterator = locals.iterator();
	    while(localIterator.hasNext()) {
		pw.println(localIterator.next().toString());
	    }
	}
	pw.println("}");

	return(sw.toString());
    }

    /**
     * When one of this object's children is updated we need to tell our
     * parent (probably a ClassDescription) that we have been updated.
     *
     * @param Observable o The child that was updated (probably a FieldDescription)
     * @param Object arg The argument used (probably nothing).
     */
    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }
}
