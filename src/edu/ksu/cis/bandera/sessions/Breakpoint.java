package edu.ksu.cis.bandera.sessions;

import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The Breakpoint class provides a way to store counter example breakpoint
 * information in the session file so that it may be re-used each time
 * a session is run.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class Breakpoint extends Observable implements Cloneable {

    /**
     * The point at which to break;
     */
    private String point;

    /**
     * Get the point where a break should be set.
     *
     * @return String
     */
    public String getPoint() {
	return(point);
    }

    /**
     * Set the point to break at.
     *
     * @param String point
     */
    public void setPoint(String point) {
	this.point = point;
	setChanged();
	notifyObservers();
    }

    /**
     * Create a new Breakpoint that uses the same point as this Breakpoint.
     *
     * @return Object A new Breakpoint with the same point as this object.
     */
    public Object clone() {
	Breakpoint bp = new Breakpoint();

	bp.setPoint(this.getPoint());

	return(bp);
    }

    public boolean equals(Object object) {
	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof Breakpoint) {
	    Breakpoint bp = (Breakpoint)object;
	    if(((bp.point == null) && (point == null)) ||
	       (point.equals(bp.point))) {
		equal = true;
	    }
	}

	return(equal);
    }

    public int hashCode() {
	int hash = 0;
	if(point != null) {
	    hash += point.hashCode();
	}
	return(hash);
    }

    public String toString() {
	return("Breakpoint at point " + point);
    }

}
