package edu.ksu.cis.bandera.bui.counterexample;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * The RootedBipartiteGraph provides method to draw a bipartite graph based upon
 * a root object.  In other words, it will start drawing the graph at the root
 * and it will only contain nodes reachable from the root.
 *
 * This object relies on the functionality provided by the AbstractBipartiteGrapher.
 *
 * <b>Example Usage:</b>
 * To create a new grapher.
 * <pre>
 * Object r;
 * TraceManager tm;
 * RootedBipartiteGrapher rbg = new RootedBipartiteGrapher(tm, r);
 * rbg.show();
 * </pre>
 * To cause it to update the display.
 * <pre>
 * rbg.update();
 * </pre>
 *
 * Creation date: (1/2/02 3:27:31 PM)
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @see edu.ksu.cis.bandera.bui.counterexample.AbstractBipartiteGrapher
 */
public class RootedBipartiteGrapher extends AbstractBipartiteGrapher {
	
    /**
      * This is the root from which the graph will be drawn.
      */
    private java.lang.Object root;

	private int depth;
    /**
     * This constructor will create a new RootedBipartiteGrapher using the given
     * TraceManager but will not have a defined root.  You must set a root before
     * this will graph anything (setRoot(Object)).
     *
     * @param TraceManager traceManager This is where we will get the values to define
     *        the edges in the graph.
     */
    public RootedBipartiteGrapher(TraceManager traceManager) {
        this(traceManager, null);
    }
    /**
     * This constructor will create a new RootedBipartiteGrapher using thte given
     * TraceManager and will be rooted at the given object.
     *
     * Creation date: (1/2/02 5:18:56 PM)
     *
     * @param TraceManager traceManager This is where we will get the values to define
     *        the edges in the graph.
     * @param Object root The root object from which the graph will be drawn.
     */
    public RootedBipartiteGrapher(TraceManager traceManager, Object root) {

        // set the trace manager (using the constructor from AbstractBipartiteGrapher)
        super(traceManager);

        // set the root object for this rooted graph
        setRoot(root);

        // now cause the graph to be updated ... more specifically to create the graph initially
        // use the update method defined in AbstractBipartiteGrapher
        update();
    }
    /**
     * Generate the graph rooted at the root object (set using setRoot) using the following algorithm.  First,
     * draw a node the represents the root object.  Next, if the lock for the root object has been acquired, draw
     * an edge from the root to the object that holds the lock.  Next, draw edges for all the locks that the root
     * object holds.  Next, if this object is blocked waiting on a lock, get the object the root is trying to
     * acquire.  Last, if my lock is held (determined earlier), draw all the objects that are blocked waiting to
     * acquire my lock.
     * Creation date: (1/2/02 5:13:53 PM)
     *
     * @pre bipartiteGraph is not null
     * @pre root is not null
     */
    protected void generateGraph() {

        // use the bipartiteGraph defined in AbstractBipartiteGrapher
        BipartiteGraph bipartiteGraph = getBipartiteGraph();
        if (bipartiteGraph == null) {
            System.err.println(
                "The bipartiteGraph has not been properly initialized.  No graph generated.");
            return;
        }

        // use the traceManager defined in AbstractBipartiteGrapher
        TraceManager traceManager = getTraceManager();
        if (traceManager == null) {
            System.err.println(
                "The traceManager has not been properly initialized.  No graph generated.");
            return;
        }

        if (root == null) {
            System.err.println(
                "The root has not been properly initialized.  No graph generated.");
            return;
        }

        /* get all the waiting edges */
        List waitingEdges = traceManager.getAllLockWaitingEdges();
        
        /* get all the locked edges */
        List lockedEdges = traceManager.getAllLockHoldingEdges();

        /* for now, draw all edges. so set the depth to -1. */
        setDepth(-1);
        
        /* draw the adjacent nodes */
        drawAdjacentEdges(bipartiteGraph, root, 0, lockedEdges, waitingEdges);

        // now show the graph
        bipartiteGraph.showGraph();

    }
/**
 * Insert the method's description here.
 * Creation date: (2/11/2002 8:41:08 PM)
 * @return int
 */
public int getDepth() {
	return depth;
}
    /**
     * Retrieve the root object.
     *
     * Creation date: (1/2/02 5:19:30 PM)
     *
     * @return Object The root object for this graph.
     */
    public Object getRoot() {
        return root;
    }
    /**
     * This will be used for testing.  Right now it is empty. -tcw
     * @param args an array of command-line arguments
     */
    public static void main(java.lang.String[] args) {
    }
/**
 * Insert the method's description here.
 * Creation date: (2/11/2002 8:41:08 PM)
 * @param newDepth int
 */
public void setDepth(int newDepth) {
	depth = newDepth;
}
    /**
     * Set the root object for this graph.
     *
     * Creation date: (1/2/02 5:19:30 PM)
     *
     * @param Object newRoot The root object for this graph.
     */
    public void setRoot(Object newRoot) {
        root = newRoot;
    }

/**
 * Insert the method's description here.
 * Creation date: (2/11/2002 8:42:50 PM)
 * @param bipartiteGraph edu.ksu.cis.bandera.bui.counterexample.BipartiteGraph
 * @param root java.lang.Object
 * @param currentDepth int
 * @param lockedEdges java.util.List
 * @param waitingEdges java.util.List
 */
private void drawAdjacentEdges(
    BipartiteGraph bipartiteGraph,
    Object root,
    int currentDepth,
    List lockedEdges,
    List waitingEdges) {

    if (root == null) {
        return;
    }

    if ((depth != -1) && (currentDepth > depth)) {
        return;
    }

    List lockedEdgeRemovalList = new ArrayList();
    List waitingEdgeRemovalList = new ArrayList();
    Set newRootSet = new HashSet();

    /* draw the locked edges if they exist */
    if (lockedEdges != null) {
        for (int i = 0; i < lockedEdges.size(); i++) {
            Object currentValue = lockedEdges.get(i);

            if ((currentValue != null) && (currentValue instanceof List)) {
                List currentList = (List) currentValue;
                if (currentList.size() >= 2) {
                    Object o1 = currentList.get(0);
                    Object o2 = currentList.get(1);

                    if (o1 == root) {
                        bipartiteGraph.addLockHoldingEdge(o1, o2);
                        lockedEdgeRemovalList.add(new Integer(i));
                        newRootSet.add(o2);
                    }
                    else {
                        if (o2 == root) {
                            bipartiteGraph.addLockHoldingEdge(o1, o2);
                            lockedEdgeRemovalList.add(new Integer(i));
                            newRootSet.add(o1);
                        }
                    }
                }
            }
        }
    }

    /* draw the waiting edges if they exist */
    if (waitingEdges != null) {
        for (int i = 0; i < waitingEdges.size(); i++) {
            Object currentValue = waitingEdges.get(i);

            if ((currentValue != null) && (currentValue instanceof List)) {
                List currentList = (List) currentValue;
                if (currentList.size() >= 2) {
                    Object o1 = currentList.get(0);
                    Object o2 = currentList.get(1);

                    if (o1 == root) {
                        bipartiteGraph.addLockWaitingEdge(o1, o2);
                        waitingEdgeRemovalList.add(new Integer(i));
                        newRootSet.add(o2);
                    }
                    else {
                        if (o2 == root) {
                            bipartiteGraph.addLockWaitingEdge(o1, o2);
                            waitingEdgeRemovalList.add(new Integer(i));
                            newRootSet.add(o1);
                        }
                    }
                }
            }
        }
    }

    /* remove all of the edges that we have just drawn from the list of locked edges so we
     * don't draw them again. */
    for (int i = 0; i < lockedEdgeRemovalList.size(); i++) {
        Object currentIndexObject = lockedEdgeRemovalList.get(i);
        if ((currentIndexObject != null) && (currentIndexObject instanceof Integer)) {
            int currentIndex = ((Integer) currentIndexObject).intValue();
            lockedEdges.remove(currentIndex);
        }
    }

    /* remove all of the edges that we have just drawn from the list of waiting edges so we
     * don't draw them again. */
    for (int i = 0; i < waitingEdgeRemovalList.size(); i++) {
        Object currentIndexObject = waitingEdgeRemovalList.get(i);
        if ((currentIndexObject != null) && (currentIndexObject instanceof Integer)) {
            int currentIndex = ((Integer) currentIndexObject).intValue();
            waitingEdges.remove(currentIndex);
        }
    }

    /* now recursively draw the next level of edges */
    Iterator iterator = newRootSet.iterator();
    while(iterator.hasNext()) {
        Object newRoot = iterator.next();
        drawAdjacentEdges(
            bipartiteGraph,
            newRoot,
            currentDepth + 1,
            lockedEdges,
            waitingEdges);
    }

}
}
