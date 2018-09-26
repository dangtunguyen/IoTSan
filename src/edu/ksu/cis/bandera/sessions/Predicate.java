package edu.ksu.cis.bandera.sessions;

import org.apache.log4j.Category;

import java.util.Observable;

/**
 * A Predicate defines a portion of the pattern.  It has a unique name and holds an expression.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class Predicate extends Observable implements Cloneable {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(Predicate.class.getName());

    /**
     * The name of this predicate.
     */
    private String name;
    
    /**
     * The expression for this predicate.
     */
    private String expression;

    /**
     * Get the name of this predicate.
     *
     * @return String
     */
    public String getName() {
	return(name);
    }

    /**
     * Get the expression for this predicate.
     *
     * @return String
     */
    public String getExpression() {
	return(expression);
    }

    /**
     * Set the name of this predicate.
     *
     * @param String name
     */
    public void setName(String name) {
	this.name = name;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the expression for this predicate.
     *
     * @param String expression
     */
    public void setExpression(String expression) {
	this.expression = expression;
	setChanged();
	notifyObservers();
    }

    /**
     *
     */
    public Object clone() {
	Predicate p = new Predicate();

	p.setName(this.getName());
	p.setExpression(this.getExpression());

	return(p);
    }

    public String toString() {
	return("Predicate " + name + " = " + expression);
    }

    public boolean equals(Object object) {

	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof Predicate) {
	    Predicate p = (Predicate)object;
	    if((((name == null) && (p.name == null)) ||
		(name.equals(p.name))) &&
	       (((expression == null) && (p.expression == null)) ||
		(expression.equals(p.expression)))) {
		equal = true;
	    }
	}

	return(equal);
    }

    public int hashCode() {
	int hash = 0;

	if(name != null) {
	    hash += name.hashCode();
	}

	if(expression != null) {
	    hash += expression.hashCode();
	}

	return(hash);
    }

}
