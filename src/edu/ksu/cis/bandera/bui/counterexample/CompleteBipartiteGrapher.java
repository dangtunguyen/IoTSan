package edu.ksu.cis.bandera.bui.counterexample;

import java.util.List;

import org.apache.log4j.Category;

/**
 * The CompleteBipartiteGrapher will provide a way to display a complete graph
 * for a state within a counter-example.  This will draw all of the available nodes
 * and edges in the system.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:33:17 $
 * @see edu.ksu.cis.bandera.bui.counterexample.AbstractBipartiteGrapher
 */
public class CompleteBipartiteGrapher extends AbstractBipartiteGrapher {
    /**
     * The log to use when sending logging messages.
     */
    private static Category log = Category.getInstance(CompleteBipartiteGrapher.class);

    /**
     * Create a new instance of a CompleteBipartiteGraph using the given traceManager.
     *
     * @param TraceManager traceManager The TraceManager to use when generating the graph.
     */
    public CompleteBipartiteGrapher(TraceManager traceManager) {
	super(traceManager);
	
	// now cause the graph to be updated ... more specifically to create the graph initially
	update();

    }

    /**
     * This method will generate a complete graph for the system.  This will include all
     * nodes and edges that exist in the system.
     *
     * This satisfies the abstract method requirements for the AbstractBipartiteGrapher.
     *
     * This uses the following algorithm:
     * Get all the objects that are defined in the system.  For each object, draw the
     * adjacent edges.  To draw the edges, first get the locks that are held by the
     * current object.  Next, get the lock holder of the current object.  If the lock
     * is held, get the collection of objects that are requesting a lock on the current
     * object.  Last, get the lock that the current object is requesting.  For each result
     * draw the appropriate edge (either held or waiting).
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.AbstractBipartiteGrapher
     * @pre traceManager is not null
     * @pre bipartiteGraph is not null
     */
    protected void generateGraph() {
	
	BipartiteGraph bipartiteGraph = getBipartiteGraph();
	if(bipartiteGraph == null) {
	    System.err.println("bipartiteGraph is null.  Cannot generate a graph.");
	    return;
	}

	TraceManager traceManager = getTraceManager();
	if(traceManager == null) {
	    System.err.println("traceManager is null.  Cannot generate a graph.");
	    return;
	}

	/* Maybe this is a better plan? -tcw
	   ArrayList threads = traceManager.getAllThreads();
	   for(int i = 0; i < threads.size(); i++) {
	   Object currentThread = threads.get(i);
	   bipartiteGraph.addNode(currentThread);
	 	
	   // draw the requesting object edge for this thread if it exists
	   Object requestingObject = traceManager.getRequestingObject(currentThread);
	   if(requestingObject != null) {
	   bipartiteGraph.addLockWaitingEdge(currentThread, requestingObject);
	   }
		
	   // draw the holding edges for this thread if any exist
	   Object[] heldObjects = traceManager.getLockedObjects(currentThread);
	   if(heldObjects != null){
	   for(int i = 0; i < heldObjects.length; i++) {
	   bipartiteGraph.addLockHoldingEdge(currentThread, heldObjects[i]);
	   }
	   }
	   }
	*/

	/* Maybe this is a better plan? -tcw */
	List heldEdges = traceManager.getAllLockHoldingEdges();
	if(heldEdges != null) {
	    for(int i = 0; i < heldEdges.size(); i++) {
		Object currentValue = heldEdges.get(i);
		if((currentValue != null) && (currentValue instanceof List)) {
		    List currentList = (List)currentValue;
		    if(currentList.size() == 2) {
			Object holder = currentList.get(0);
			Object held = currentList.get(1);
			bipartiteGraph.addLockHoldingEdge(holder, held);
		    }
		    else if(currentList.size() == 3) {
			Object holder = currentList.get(0);
			String association = (String)currentList.get(1);
			Object held = currentList.get(2);
			bipartiteGraph.addLockHoldingEdge(holder, association, held);
		    }
		}
	    }
	}
	 
	List waitingEdges = traceManager.getAllLockWaitingEdges();
	if(waitingEdges != null) {
	    for(int i = 0; i < waitingEdges.size(); i++) {
		Object currentValue = waitingEdges.get(i);
		if((currentValue != null) && (currentValue instanceof List)) {
		    List currentList = (List)currentValue;
		    if(currentList.size() == 2) {
			Object requestor = currentList.get(0);
			Object requested = currentList.get(1);
			bipartiteGraph.addLockWaitingEdge(requestor, requested);
		    }
		    else if(currentList.size() == 3) {
			Object requestor = currentList.get(0);
			String association = (String)currentList.get(1);
			Object requested = currentList.get(2);
			bipartiteGraph.addLockWaitingEdge(requestor, association, requested);
		    }
		}
	    }
	}
	/**/

	/* old attempt! -tcw
	   ArrayList objects = traceManager.getAllObjects();
	   if(objects == null) {
	   System.err.println("objects are null.  Cannot generate a graph.");
	   return;
	   }
	 
	   for(int i = 0; i < objects.size(); i++) {
	   Object currentObject = objects.get(i);
	   bipartiteGraph.addNode(currentObject);		 
	   drawAdjacentEdges(currentObject, bipartiteGraph, traceManager);
	   }
	*/
	 
    }
    /**
     * This method does nothing right now but should provide a simple way to test
     * this class out.  It should probably create a test TraceManager and initialize
     * a new CompleteBipartiteGrapher with it.  Then cause it to update and
     * the user can visual check out the results to make sure it is what is
     * expected.
     *
     * @param args an array of command-line arguments
     */
    public static void main(java.lang.String[] args) {
    }
}
