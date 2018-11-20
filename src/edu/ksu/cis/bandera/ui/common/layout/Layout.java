package edu.ksu.cis.bandera.ui.common.layout;

import java.util.Set;

/**
 * The Layout interface provides the contract for
 * interacting with layout information.  This will
 * store the locations for all nodes and edges in
 * a graph.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface Layout {

    public Set getNodes();
    public Set getEdges();
    public int getEdgeCount();
    public int getNodeCount();
    public Edge getEdge(int edgeID);
    public Node getNode(int nodeID);

    public void clear();
    public void clearEdges();
    public void clearNodes();
    public void addEdge(Edge edge);
    public void addNode(Node node);

}
