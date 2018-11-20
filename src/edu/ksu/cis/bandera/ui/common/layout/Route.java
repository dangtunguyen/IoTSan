package edu.ksu.cis.bandera.ui.common.layout;

import java.util.List;
import java.util.Iterator;

/**
 * The Route interface provides the contract that must be valid
 * for each implementation.  It provides a storage mechanism for
 * an ordered set of Points that represent a route in a graph.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface Route {

    public List getPoints();
    public Iterator iterator();
    public int length();
    public Point getPoint(int index);

    public void clear();
    public void add(Point point);

}
