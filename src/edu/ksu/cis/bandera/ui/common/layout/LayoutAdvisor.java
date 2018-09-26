package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The LayoutAdvisor provides an interactive method of layout
 * where the user will request the next location to place a
 * node or edge route according the a specific algorithm.  An Advisor
 * should keep track of the current state of the layout so it can
 * make decisions based upon that information.
 *
 * @author Todd Wallentine &gt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface LayoutAdvisor {

    /**
     * Get the next location in which to place the given node.
     *
     * @return Point The location to place the given node in the graph.
     * @param Node The node to place in the graph.
     */
    public Point nextNodeLocation(Node node);

    /**
     * Get a route in which to draw the given edge.
     *
     * @return Route The route for the edge in the graph.
     * @param Edge The edge to find routing information for.
     */
    public Route nextEdgeLocation(Edge edge);

}
