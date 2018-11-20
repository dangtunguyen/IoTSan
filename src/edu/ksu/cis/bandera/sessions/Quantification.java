package edu.ksu.cis.bandera.sessions;

import edu.ksu.cis.bandera.sessions.QuantifiedVariable;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Category;

/**
 * The Quantification defines ...  It holds a set of quantified variables and a binding.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class Quantification extends Observable implements Cloneable, Observer {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(Quantification.class.getName());

    /**
     * The binding for this quantification.
     */
    private Binding binding;

    /**
     * A set of quantified variables defined for this quantification.
     */
    private Set quantifiedVariables;


    /**
     * Get the quantified variables for this quantification.
     *
     * @return Set
     */
    public Set getQuantifiedVariables() {
	return(quantifiedVariables);
    }
    
    /**
     * Get the binding for this quantification.
     *
     * @return Binding
     */
    public Binding getBinding() {
	return(binding);
    }
    
    /**
     * Add a quantified variable to this quantification.
     *
     * @param QuantifiedVariable quantifiedVariable
     */
    public void addQuantifiedVariable(QuantifiedVariable quantifiedVariable) {
	if(quantifiedVariable == null) {
	    return;
	}
	if(quantifiedVariables == null) {
	    quantifiedVariables = new HashSet();
	}
	boolean changed = quantifiedVariables.add(quantifiedVariable);
	if(changed) {
	    quantifiedVariable.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }
    
    /**
     * Remove a quantified variable from this quantification.
     *
     * @param QuantifiedVariable quantifiedVariable
     */
    public void removeQuantifiedVariable(QuantifiedVariable quantifiedVariable) {
	if(quantifiedVariable == null) {
	    return;
	}
	if(quantifiedVariables == null) {
	    return;
	}
	boolean changed = quantifiedVariables.remove(quantifiedVariable);
	if(changed) {
	    quantifiedVariable.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }
    
    /**
     * Clear the quantified variables in this quantification.
     */
    public void clearQuantifiedVariables() {
	if((quantifiedVariables == null) || (quantifiedVariables.size() <= 0)) {
	    return;
	}
	Iterator qvi = quantifiedVariables.iterator();
	while(qvi.hasNext()) {
	    QuantifiedVariable qv = (QuantifiedVariable)qvi.next();
	    qv.deleteObserver(this);
	}
	quantifiedVariables.clear();
	setChanged();
	notifyObservers();
    }
    
    /**
     * Set the binding for this quantification.
     *
     * @param Binding binding
     */
    public void setBinding(Binding binding) {
	this.binding = binding;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a new Quantification that has the same binding and all the same
     * quantified variables as this one.
     *
     * @return Object A Quantification identical in value to this.
     */
    public Object clone() {
	Quantification q = new Quantification();

	q.setBinding(this.getBinding());

	Set qvs = this.getQuantifiedVariables();
	if((qvs != null) && (qvs.size() > 0)) {
	    Iterator i = qvs.iterator();
	    while(i.hasNext()) {
		QuantifiedVariable qv = (QuantifiedVariable)i.next();
		q.addQuantifiedVariable((QuantifiedVariable)qv.clone());
	    }
	}

	return(q);
    }

    /**
     * Create a String representation of this object.
     *
     * @return String The String presentation of this object.
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();

	Set qvs = this.getQuantifiedVariables();
	if((qvs != null) && (qvs.size() > 0)) {
	    Iterator i = qvs.iterator();
	    while(i.hasNext()) {
		QuantifiedVariable qv = (QuantifiedVariable)i.next();
		sb.append(qv.getName() + ":" + qv.getType() + ",");
	    }
	}

	return(sb.toString());
    }

    /**
     * Test the given object for equality against this object.  For this to be true,
     * the given object must be a Quantification that has the same binding and the
     * same quantified variables as this object.
     *
     * @param Object object The object to test for equality.
     * @return boolean True if the given object is equal to this object, false otherwise.
     */
    public boolean equals(Object object) {

	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof Quantification) {
	    Quantification q = (Quantification)object;
	    if((((binding == null) && (q.binding == null)) ||
		(binding.equals(q.binding))) &&
	       (((quantifiedVariables == null) && (q.quantifiedVariables == null)) ||
		(quantifiedVariables.equals(q.quantifiedVariables)))) {
		equal = true;
	    }
	}

	return(equal);
    }

    /**
     * Create a hash value for this object.
     *
     * @return int A hash value.
     */
    public int hashCode() {

	int hash = 0;

	if(binding != null) {
	    hash += binding.hashCode();
	}
	if(quantifiedVariables != null) {
	    hash += quantifiedVariables.hashCode();
	}

	return(hash);
    }

    /**
     * When one of this object's children is updated, we should update our parent.
     *
     * @param Observable o The object that was updated.
     * @param Object arg The argument to the update (probably null).
     */
    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }

    /**
     * 
     */
    public String getQuantificationString() {

	if((quantifiedVariables == null) || (quantifiedVariables.size() <= 0)) {
	    return("");
	}

	// build up a map that maps a type to a set of variables of that type
	Map typeMap = new TreeMap();
	Iterator i = quantifiedVariables.iterator();
	while(i.hasNext()) {
	    QuantifiedVariable qv = (QuantifiedVariable)i.next();
	    String type = qv.getType();
	    Set variableSet = (Set)typeMap.get(type);
	    if(variableSet == null) {
		variableSet = new TreeSet();
		typeMap.put(type, variableSet);
	    }
	    variableSet.add(qv.getName());
	}

	// using the already built map, build up the String that represents
	//  this quantfication: forall[x1,x2,x3:X].forall[y1,y2:Y].
	StringBuffer sb = new StringBuffer();
	i = typeMap.keySet().iterator();
	while(i.hasNext()) {
	    String type = (String)i.next();
	    Set variableSet = (Set)typeMap.get(type);
	    sb.append("forall[");
	    Iterator vi = variableSet.iterator();
	    while(vi.hasNext()) {
		String varName = (String)vi.next();
		sb.append(varName);
		if(vi.hasNext()) {
		    sb.append(", ");
		}
		else {
		    sb.append(" : " + type + "].");
		}
	    }
	    
	}

	return(sb.toString());
    }

}
