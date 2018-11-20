package edu.ksu.cis.bandera.ui.common.layout;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public class DefaultRoute implements Route {

    protected List points;

    public DefaultRoute() {
	super();
	points = new ArrayList();
    }

    public List getPoints() {
	return(points);
    }

    public Iterator iterator() {
	return(points.iterator());
    }

    public int length() {
	return(points.size());
    }

    public Point getPoint(int index) {
	if(index < points.size()) {
	    return((Point)points.get(index));
	}
	// this should throw an exception! -tcw
	return(null);
    }

    public void clear() { 
	points.clear();
    }

    public void add(Point point) {
	if(point == null) {
	    return;
	}

	points.add(point);
    }

}
