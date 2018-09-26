package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The DefaulteEdge provides a simple implementation of the
 * Edge interface to provide storage for edge information.  This
 * includes a unique id, source and target IDs for nodes, and
 * routing information.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:44 $
 */
public class DefaultEdge implements Edge {

    protected int id;

    protected int sourceID;

    protected int targetID;

    protected Route route;

    private static int nextID = 0;

    private static final int DEFAULT_SOURCE_ID = -1;

    private static final int DEFAULT_TARGET_ID = -1;

    public DefaultEdge() {
	this(nextID++);
    }

    public DefaultEdge(int id) {
	this(id, DEFAULT_SOURCE_ID, DEFAULT_TARGET_ID);
    }

    public DefaultEdge(int sourceID, int targetID) {
	this(nextID++, sourceID, targetID);
    }

    public DefaultEdge(int sourceID, int targetID, Route route) {
	this(nextID++, sourceID, targetID, route);
    }

    public DefaultEdge(int id, int sourceID, int targetID) {
	this(id, sourceID, targetID, new DefaultRoute());
    }

    public DefaultEdge(int id, int sourceID, int targetID, Route route) {
	super();
	setID(id);
	setSourceID(sourceID);
	setTargetID(targetID);
	setRoute(route);
    }

    public int getID() {
	return(id);
    }

    public int getSourceID() {
	return(sourceID);
    }

    public int getTargetID() {
	return(targetID);
    }

    public Route getRoute() {
	return(route);
    }

    public void setID(int id) {
	this.id = id;
    }

    public void setSourceID(int sourceID) {
	this.sourceID = sourceID;
    }

    public void setTargetID(int targetID) {
	this.targetID = targetID;
    }

    public void setRoute(Route route) {
	this.route = route;
    }

}
