package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The Edge interface provides a contract that must be valid
 * for each implementation of an Edge.  It provides the storage
 * mechanism for edge information including source node id, target
 * node id, and routing information.  It must also include a
 * unique ID.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface Edge {

    public int getID();
    public int getSourceID();
    public int getTargetID();
    public Route getRoute();

    public void setID(int id);
    public void setSourceID(int sourceID);
    public void setTargetID(int targetID);
    public void setRoute(Route route);

}
