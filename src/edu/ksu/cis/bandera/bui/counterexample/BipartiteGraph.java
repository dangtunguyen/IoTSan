package edu.ksu.cis.bandera.bui.counterexample;

/**
  * The BipartiteGraph interface provides a common way of interacting
  * with a BipartiteGraph.  It provides a means to add and delete edges
  * as well as showing and hiding it.
  *
  * <p>
  * <b>Example Usage:</b>
  * <pre>
  * BipartiteGraph graph = BipartiteGraphFactory.getInstance();
  * graph.addLockHoldingEdge(thread1, object1);
  * graph.addLockHoldingEdge(thread2, object2);
  * graph.addLockWaitingEdge(thread1, object2);
  * graph.addLockWaitingEdge(thread2, object1);
  * graph.show();
  * </pre>
  * </p>
  *
  * <p>
  * <b>Example Usage:</b>
  * <pre>
  * BipartiteGraph graph = BipartiteGraphFactory.getInstance();
  * graph.addLockHoldingEdge(thread1, "a1:A", object1);
  * graph.addLockHoldingEdge(thread2, "b1:B", object2);
  * graph.addLockWaitingEdge(thread1, "b2:B", object2);
  * graph.addLockWaitingEdge(thread2, "a2:A", object1);
  * graph.show();
  * </pre>
  * </p>
  * 
  * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
  * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:33:17 $
  */
public interface BipartiteGraph {

    /*
    public void deleteLockHoldingEdge(Object holderObject, Object heldObject);
    public void deleteLockWaitingEdge(Object requestorObject, Object requestedObject);
    public void clear();
    */

    /**
      * Create a new edge in the graph with the held lock style.  This will
      * create a directed edge from the holder object to the held object. 
      */
    public void addLockHoldingEdge(Object holderObject, Object heldObject);

    /**
      * Create a new edge in the graph with the acquiring lock style.  This will
      * create a directed edge from the requestor object to the requested object. 
      */
    public void addLockWaitingEdge(Object requestorObject, Object requestedObject);

    /**
      * Create a new edge in the graph with the held lock style.  This will
      * create a directed edge from the holder object to the held object. 
      */
    public void addLockHoldingEdge(Object holderObject, String associationName, Object heldObject);

    /**
      * Create a new edge in the graph with the acquiring lock style.  This will
      * create a directed edge from the requestor object to the requested object. 
      */
    public void addLockWaitingEdge(Object requestorObject, String associationName, Object requestedObject);

    /**
     * Insert the method's description here.
     * Creation date: (12/18/01 11:17:36 AM)
     * @param node java.lang.Object
     */
    void addNode(Object node);

    /**
     * Insert the method's description here.
     * Creation date: (1/2/02 5:09:52 PM)
     */
    void clear();

    /**
     * Hide the graph from the user.  This should be associated with
     * the window being hidden at this point.  More than likely, this will
     * be a direct call to the JFrame that holds the graph to make it
     * not-visible (or invisible).
     */
    public void hideGraph();

    /**
     * Show the graph on the screen.  This should be associated with
     * the window being shown at this point.  More than likely, this will
     * be a direct call to the JFrame that holds the graph to make it
     * visible.
     */
    public void showGraph();
}
