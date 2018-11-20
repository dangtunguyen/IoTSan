package edu.ksu.cis.bandera.sessions;

import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Category;

/**
 * The TemporalProperty is a definition of a property that should hold
 * for an application.  It will be used to verify the application defined
 * in the Session.  A TemporalProperty is defined by a pattern, a
 * quantification, and a set of predicates (that are associated with the pattern).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public final class TemporalProperty extends Observable implements Cloneable, Observer {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(TemporalProperty.class.getName());

    /**
     * A set of predicates defined for this property.
     */
    private Set predicates;

    /**
     * The pattern to use for this property.
     */
    private Pattern pattern;

    /**
     * The quantification for this property.
     */
    private Quantification quantification;

    /**
     * Get the quantification for this property.
     *
     * @return Quantification
     */
    public Quantification getQuantification() {
	return(quantification);
    }

    /**
     * Get the pattern for this property.
     *
     * @return Pattern
     */
    public Pattern getPattern() {
	return(pattern);
    }

    /**
     * Get the set of predicates for this property.
     *
     * @return Set A Set of Predicate objects.
     */
    public Set getPredicates() {
	return(predicates);
    }

    /**
     * Set the quantification for this property.
     *
     * @param Quantification quantification
     */
    public void setQuantification(Quantification quantification) {
	if(quantification == null) {
	    return;
	}
	this.quantification = quantification;
	quantification.addObserver(this);
	setChanged();
	notifyObservers();
    }

    /**
     * Set the pattern for this property.
     *
     * @param Pattern pattern
     */
    public void setPattern(Pattern pattern) {
	if(pattern == null) {
	    return;
	}
	this.pattern = pattern;
	setChanged();
	notifyObservers();
    }

    /**
     * Add a predicate to this property.
     *
     * @param Predicate predicate The predicate to be added.
     */
    public void addPredicate(Predicate predicate) {
	if(predicate == null) {
	    return;
	}
	if(predicates == null) {
	    predicates = new HashSet();
	}
	boolean changed = predicates.add(predicate);
	if(changed) {
	    predicate.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Clear the predicates in this property.
     */
    public void clearPredicates() {
	if((predicates == null) || (predicates.size() <= 0)) {
	    return;
	}
	Iterator pi = predicates.iterator();
	while(pi.hasNext()) {
	    Predicate p = (Predicate)pi.next();
	    p.deleteObserver(this);
	}
	predicates.clear();
    }

    /**
     * Remove the given predicate from this property.
     *
     * @param Predicate predicate The predicate to be removed.
     */
    public void removePredicate(Predicate predicate) {
	if(predicate == null) {
	    return;
	}
	if(predicates == null) {
	    return;
	}
	boolean changed = predicates.remove(predicate);
	if(changed) {
	    predicate.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Create a new TemporalProperty that has the same values for quantification, predictes,
     * and pattern as this object (deep-copy).
     *
     * @return Object A new TemporalProperty that has the same values as this object.
     */
    public Object clone() {
	TemporalProperty tp = new TemporalProperty();

	tp.setPattern(this.getPattern());
	if(quantification != null) {
	    tp.setQuantification((Quantification)quantification.clone());
	}

	Set predicates = this.getPredicates();
	if((predicates != null) && (predicates.size() > 0)) {
	    Iterator i = predicates.iterator();
	    while(i.hasNext()) {
		Predicate p = (Predicate)i.next();
		tp.addPredicate((Predicate)p.clone());
	    }
	}
	
	return(tp);
    }

    /**
     * Create a String representation of this TemporalProperty.
     *
     * @return String
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();

	// print the quantification
	sb.append("Quantification: ");
	if(quantification != null) {
	    sb.append(quantification.toString());
	}
	else {
	    sb.append("None.");
	}
	sb.append(System.getProperty("line.separator"));

	// print the pattern
	sb.append("Pattern: ");
	if(pattern != null) {
	    sb.append(pattern.getName());
	}
	else {
	    sb.append("None.");
	}
	sb.append(System.getProperty("line.separator"));

	// print the predicates store
	sb.append("Predicates:" + System.getProperty("line.separator"));
	sb.append("----------------------" + System.getProperty("line.separator"));
	Set predicates = this.getPredicates();
	if((predicates != null) && (predicates.size() > 0)) {
	    Iterator i = predicates.iterator();
	    while(i.hasNext()) {
		Predicate p = (Predicate)i.next();
		sb.append(p.toString() + System.getProperty("line.separator"));
	    }
	}
	else {
	    sb.append("None." + System.getProperty("line.separator"));
	}

	return(sb.toString());
    }

    /**
     * Test to see if the object given is equivalent to this object.  For this to
     * be true, the given object must be a TemporalProperty that has the same pattern,
     * the same quantification, and the same predicates as this object.
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
	else if(object instanceof TemporalProperty) {
	    TemporalProperty tp = (TemporalProperty)object;
	    if((((pattern == null) && (tp.pattern == null)) ||
		((pattern != null) && (pattern.equals(tp.pattern)))) &&
	       (((quantification == null) && (tp.quantification == null)) ||
		((quantification != null) && (quantification.equals(tp.quantification)))) &&
	       (((predicates == null) && (tp.predicates == null)) ||
		((predicates != null) && (predicates.equals(tp.predicates))))) {
		equal = true;
	    }
	}

	return(equal);
    }

    /**
     * Create a hash for this object.
     *
     * @return int A hash value.
     */
    public int hashCode() {
	int hash = 0;

	if(pattern != null) {
	    hash += pattern.hashCode();
	}
	if(quantification != null) {
	    hash += quantification.hashCode();
	}
	if(predicates != null) {
	    hash += predicates.hashCode();
	}

	return(hash);
    }

    /**
     * When one of the children of this object is updated, we should pass the word
     * along to my parent's.
     *
     * @param Observable o The object that changed.
     * @param Object arg The argument for this change (probably null).
     */
    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }

}
