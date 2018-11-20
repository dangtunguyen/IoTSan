package edu.ksu.cis.bandera.sessions;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The Specification object holds a specification for a particular
 * Session.  This means it will contain a set of assertions and possibly
 * a property (TemporalProperty) to used to verify the application associated
 * with a Session.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public final class Specification extends Observable implements Cloneable, Observer {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(Specification.class.getName());

    /**
     * The temporal property for this specification.
     */
    private TemporalProperty temporalProperty;

    /**
     * A set of assertions for this specification.
     */
    private Set assertions;

    /**
     * Get the temporal property for this specification.
     *
     * @return TemporalProperty
     */
    public TemporalProperty getTemporalProperty() {
	return(temporalProperty);
    }

    /**
     * Get all the assertions in this specification.
     *
     * @return Set
     */
    public Set getAssertions() {
	return(assertions);
    }

    /**
     * Clear all assertions from this specification.
     */
    public void clearAssertions() {
	if((assertions == null) || (assertions.size() <= 0)) {
	    return;
	}
	Iterator ai = assertions.iterator();
	while(ai.hasNext()) {
	    Assertion a = (Assertion)ai.next();
	    a.deleteObserver(this);
	}
	assertions.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Add an assertion to this specification.
     *
     * @param Assertion assertion
     */
    public void addAssertion(Assertion assertion) {
	if(assertion == null) {
	    return;
	}
	if(assertions == null) {
	    assertions = new HashSet();
	}
	boolean changed = assertions.add(assertion);
	if(changed) {
	    assertion.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove the assertion from this specification.
     *
     * @param Assertion assertion
     */
    public void removeAssertion(Assertion assertion) {
	if(assertion == null) {
	    return;
	}
	if(assertions == null) {
	    return;
	}
	boolean changed = assertions.remove(assertion);
	if(changed) {
	    assertion.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }
    
    /**
     * Set the temporal property for this specification.
     *
     * @param TemporalProperty temporalProperty
     */
    public void setTemporalProperty(TemporalProperty temporalProperty) {
	if(((this.temporalProperty == null) && (temporalProperty == null)) ||
	   ((this.temporalProperty != null) && (this.temporalProperty.equals(temporalProperty)))) {
	    // no change!  setting the same temporal property that we already have
	    return;
	}
	if(this.temporalProperty != null) {
	    this.temporalProperty.deleteObserver(this);
	}
	this.temporalProperty = temporalProperty;
	if(this.temporalProperty != null) {
	    this.temporalProperty.addObserver(this);
	}
	setChanged();
	notifyObservers();
    }

    /**
     *
     */
    public Object clone() {
	Specification s = new Specification();

	if(temporalProperty != null) {
	    s.setTemporalProperty((TemporalProperty)temporalProperty.clone());
	}

	if((assertions != null) && (assertions.size() > 0)) {
	    Iterator i = assertions.iterator();
	    while(i.hasNext()) {
		Assertion a = (Assertion)i.next();
		s.addAssertion((Assertion)a.clone());
	    }
	}

	return(s);
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();

	sb.append("TemporalProperty:" + System.getProperty("line.separator"));
	sb.append("--------------------" + System.getProperty("line.separator"));
	if(temporalProperty != null) {
	    sb.append(temporalProperty.toString() + System.getProperty("line.separator"));
	}
	else {
	    sb.append("None." + System.getProperty("line.separator"));
	}

	sb.append("Assertions:" + System.getProperty("line.separator"));
	sb.append("--------------------" + System.getProperty("line.separator"));
	Set assertions = this.getAssertions();
	if((assertions != null) && (assertions.size() > 0)) {
	    Iterator i = assertions.iterator();
	    while(i.hasNext()) {
		Assertion a = (Assertion)i.next();
		sb.append(a.toString() + System.getProperty("line.separator"));
	    }
	}
	else {
	    sb.append("None." + System.getProperty("line.separator"));
	}

	return(sb.toString());
    }

    public boolean equals(Object object) {
	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof Specification) {
	    Specification s = (Specification)object;
	    if((((temporalProperty == null) && (s.temporalProperty == null)) ||
		((temporalProperty != null) && (temporalProperty.equals(s.temporalProperty)))) &&
	       (((assertions == null) && (s.assertions == null)) ||
		((assertions != null) && (assertions.equals(s.assertions))))) {
		equal = true;
	    }
	}

	return(equal);
    }

    public int hashCode() {
	int hash = 0;
	return(hash);
    }

    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }

}
