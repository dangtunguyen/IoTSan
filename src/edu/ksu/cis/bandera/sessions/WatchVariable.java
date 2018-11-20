package edu.ksu.cis.bandera.sessions;

import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The WatchVariable object provides a way store counter example
 * watch information in the session file.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public final class WatchVariable extends Observable implements Cloneable {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(WatchVariable.class.getName());

    /**
     * The variable name that should be watched.
     */
    private String variable;

    /**
     * Get the variable to be watched.
     */
    public String getVariable() {
	return(variable);
    }

    /**
     * Set the variable that should be watched.
     *
     * @param String variable
     */
    public void setVariable(String variable) {
	this.variable = variable;
	setChanged();
	notifyObservers();
    }

    /**
     *
     */
    public Object clone() {
	WatchVariable wv = new WatchVariable();

	wv.setVariable(this.getVariable());

	return(wv);
    }

    public boolean equals(Object object) {
	
	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof WatchVariable) {
	    WatchVariable wv = (WatchVariable)object;
	    if(((variable == null) && (wv.variable == null)) ||
	       (variable.equals(wv.variable))) {
		equal = true;
	    }
	}

	return(equal);
    }

    public int hashCode() {

	int hash = 0;

	if(variable != null) {
	    hash += variable.hashCode();
	}

	return(hash);

    }

    public String toString() {
	return("WatchVariable " + variable);
    }

}
