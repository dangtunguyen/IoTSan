package edu.ksu.cis.bandera.ui.common.layout;

import java.util.Set;

/**
 * The LayoutCalculator provides the contract that will be used to
 * calculate a layout given a set of nodes and a set of edges.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface LayoutCalculator {

    /**
     * Calculate the proper layout given a set of nodes and edges.
     *
     * @return Layout The layout to use for this set of nodes and edges.
     * @param Set nodes The set of nodes that exist in the graph.
     * @param Set edges The set of edges that exist in the graph.
     */
    public Layout calculate(Set nodes, Set edges);

}
