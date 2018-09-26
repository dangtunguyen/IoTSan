package edu.ksu.cis.bandera.ui.common.layout;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The DefaultLayout provides a simple implementation of the
 * Layout interface to use as a storage vehicle for graph
 * layout information.  This includes a set of nodes
 * and edges that represent a graph layout.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:44 $
 */
public class DefaultLayout implements Layout {

    protected Set nodes;

    protected Set edges;

    public DefaultLayout() {
	nodes = new HashSet();
	edges = new HashSet();
    }

    public Set getNodes() {
	return(nodes);
    }

    public Set getEdges() {
	return(edges);
    }

    public int getEdgeCount() {
	return(edges.size());
    }

    public int getNodeCount() {
	return(nodes.size());
    }

    public Edge getEdge(int edgeID) {
	Edge edge = null;

	Iterator ei = edges.iterator();
	while(ei.hasNext()) {
	    edge = (Edge)ei.next();
	    if(edge.getID() == edgeID) {
		break;
	    }
	}

	return(edge);
    }

    public Node getNode(int nodeID) {
	Node node = null;

	Iterator ni = nodes.iterator();
	while(ni.hasNext()) {
	    node = (Node)ni.next();
	    if(node.getID() == nodeID) {
		break;
	    }
	}

	return(node);
    }

    public void clear() {
	clearEdges();
	clearNodes();
    }

    public void clearEdges() {
	edges.clear();
    }

    public void clearNodes() {
	nodes.clear();
    }

    public void addEdge(Edge edge) {
	edges.add(edge);
    }

    public void addNode(Node node) {
	nodes.add(node);
    }

}
