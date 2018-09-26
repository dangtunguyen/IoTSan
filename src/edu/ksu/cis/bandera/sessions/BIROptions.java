package edu.ksu.cis.bandera.sessions;

import java.util.Observer;
import java.util.Observable;

import edu.ksu.cis.bandera.sessions.ResourceBounds;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.log4j.Category;

/**
 * The BIROptions class holds configuration information that can be
 * used for all BIR-based model checkers.  This includes the search mode
 * that will be used (Choose Free, Resource Bounded, or Exhaustive) as well
 * as resource bounds (integer range, max array size, max instances of classes,
 * and max thread instances).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class BIROptions extends Observable implements Cloneable, Observer {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(BIROptions.class.getName());

    /**
     * The bounds to use.
     */
    private ResourceBounds resourceBounds;

    /**
     * The searchMode.  This will be one of the defined values: CHOOSE_FREE_MODE,
     * RESOURCE_BOUNDED_MODE, or EXHAUSTIVE_MODE.
     */
    private int searchMode;

    /**
     * One of the possible search modes.
     */
    public final static int CHOOSE_FREE_MODE = 1;

    /**
     * One of the possible search modes.
     */
    public final static int RESOURCE_BOUNDED_MODE = 2;

    /**
     * One of the possible search modes.
     */
    public final static int EXHAUSTIVE_MODE = 3;

    /**
     * The default search mode: Exhaustive.
     */
    public final static int DEFAULT_MODE = EXHAUSTIVE_MODE;

    /**
     * Create a new, empty BIROptions object.
     */
    public BIROptions() {
	searchMode = DEFAULT_MODE;
    }

    /**
     * Compares two objects for equality. Returns a boolean that indicates
     * whether this object is equivalent to the specified object.
     *
     * @param obj the Object to compare with
     * @return true if these Objects are equal; false otherwise.
     */
    public boolean equals(Object obj) {
	boolean equal = false;

	if(obj == null) {
	    equal = false;
	}
	else if(obj == this) {
	    equal = true;
	}
	else if(obj instanceof BIROptions) {
	    BIROptions bo = (BIROptions)obj;
	    if((searchMode == bo.searchMode) &&
	       (((resourceBounds == null) && (bo.resourceBounds == null)) ||
		(resourceBounds.equals(bo.resourceBounds)))) {
		equal = true;
	    }
	}
	return(equal);
    }

    /**
     * Get the bounds set.  If none have been set, return the default bounds.
     *
     * @return ResourceBounds The bounds to use.
     */
    public ResourceBounds getResourceBounds() {
	
	if(resourceBounds == null) {
	    // if the resource bounds has not been defined, just create one
	    //  with the default values!
	    resourceBounds = new ResourceBounds();
	}
	
	return(resourceBounds);
    }

    /**
     * Get the search mode for all BIR-based model checkers to use.
     *
     * @return int This is the search mode to use for all BIR-based model checkers.  This
     *         will be one of the defined values: CHOOSE_FREE_MODE, EXHAUSTIVE_MODE, RESOURCE_BOUNDED_MODE.
     */
    public int getSearchMode() {
	return(searchMode);
    }
    /**
     * Generates a hash code for the receiver.
     * This method is supported primarily for
     * hash tables, such as those provided in java.util.
     *
     * @return an integer hash code for the receiver
     */
    public int hashCode() {

	int hash = 0;

	hash += searchMode;
	if(resourceBounds != null) {
	    hash += resourceBounds.hashCode();
	}

	return(hash);
    }

    /**
     * Set the resource bounds to use.
     *
     * @param ResourceBounds resourceBounds The bounds to use.
     */
    public void setResourceBounds(ResourceBounds resourceBounds) {
	if(resourceBounds == null) {
	    return;
	}
	this.resourceBounds = resourceBounds;
	resourceBounds.addObserver(this);
	setChanged();
	notifyObservers();
    }

    /**
     * Set the search mode for all BIR-based model checkers to use.
     *
     * @param int searchMode The mode to use for all BIR-based model checkers.  This must be
     *        one of the pre-defined values: CHOOSE_FREE_MODE, EXHAUSTIVE_MODE, RESOURCE_BOUNDED_MODE.
     */
    public void setSearchMode(int searchMode) {

	switch (searchMode) {
        case CHOOSE_FREE_MODE :
        case EXHAUSTIVE_MODE :
        case RESOURCE_BOUNDED_MODE :
            this.searchMode = searchMode;
	    setChanged();
	    notifyObservers();
            break;
        default :
	    log.error("The searchMode given, " + searchMode + ", is not valid.");
	}

    }

    /**
     * When this object's children (ResourceBounds) is modified, we need to report
     * this change to our parent object.
     *
     * @param Observable o The object that changed.
     * @param Object arg This is probably null.
     */
    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }

    /**
     * Create the String representation of these BIROptions.
     *
     * @return String The String representation of this object.
     */
    public String toString() {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	pw.println("BIR Options:");

	String searchModeString = "InvalidSearchMode";
	switch (searchMode) {
        case CHOOSE_FREE_MODE :
	    searchModeString = "Choose Free";
	    break;
        case EXHAUSTIVE_MODE :
	    searchModeString = "Exhaustive";
	    break;
        case RESOURCE_BOUNDED_MODE :
	    searchModeString = "Resource Bounded";
            break;
	}
	pw.println("SearchMode: " + searchModeString);

	if(resourceBounds != null) {
	    pw.println(resourceBounds.toString());
	}
	else {
	    pw.println("No resource bounds set.");
	}
	return(sw.toString());
    }

    /**
     * Create a new BIROptions object that has the same values as this object.
     *
     * @return Object A new BIROptions object with the same values as this one.
     */
    public Object clone() {

	BIROptions bo = new BIROptions();
	bo.searchMode = searchMode;
	if(resourceBounds != null) {
	    bo.resourceBounds = (ResourceBounds)resourceBounds.clone();
	}

	return(bo);
    }
}
