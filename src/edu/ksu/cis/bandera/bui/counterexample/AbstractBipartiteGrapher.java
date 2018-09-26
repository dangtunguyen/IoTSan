package edu.ksu.cis.bandera.bui.counterexample;

import org.apache.log4j.Category;

/**
 * The AbstractBipartiteGrapher provides the common functionality for the CompleteBipartiteGrapher
 * as well as the RootedBipartiteGrapher.  It will initialize and store the TraceManager and the
 * BipartiteGraph objects.  It will also provide a common way to update a graph.  This is through
 * the use of the update() method.  That method relies on the subclass to define a method
 * generateGraph() that adds the nodes and edges to the graph.  More than likely it will
 * make use of the traceManager (to get the objects in the system and what locks are held, etc.)
 * and make use of the bipartiteGraph (to add nodes and edges).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt; 
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:33:17 $
 */
public abstract class AbstractBipartiteGrapher implements BipartiteGrapher {

    /**
      * The TraceManager to use to build the graph.  This provides a interface
      * to the current state of the system.  It will allow an object to find
      * what threads and objects exist and what locks are held or are being
      * acquired.
      */
    private TraceManager traceManager;

    /**
      * This is the actual graphical depiction of the graph.
      */
    private BipartiteGraph bipartiteGraph;

    /**
     * The log to use when sending logging messages.
     */
    private static Category log = Category.getInstance(AbstractBipartiteGrapher.class);

    /**
     * This is the default constructor.  It will take the TraceManager
     * that the grapher needs to use in order to build the graph.  This will
     * also create a new instance of a BipartiteGraph.
     *
     * @param TraceManager traceManager The TraceManager to use when building the graph.
     * @pre traceManager parameter should be non-null.
     * @post traceManager will be initialized (and not-null) or an error will be reported.
     */
    public AbstractBipartiteGrapher(TraceManager traceManager) {

        if (traceManager == null) {
            log.warn("Initializing the BipartiteGrapher with a null TraceManager.  System will be unstable!");
        }
        setTraceManager(traceManager);

        // create a new bipartiteGraph
        BipartiteGraph bg = BipartiteGraphFactory.getInstance();
        setBipartiteGraph(bg);

    }
    /**
     * This method needs to created when extending this class.  This method should contain
     * the logic to draw the nodes and edges of the graph.  This will most likely make use
     * of the bipartiteGraph (getBipartiteGraph) and traceManager (getTraceManager()) to
     * create the graph but it is not necessary.
     *
     * Note: There is not guarantee that the bipartiteGraph and traceManager are not null.
     *
     */
    protected abstract void generateGraph();

    /**
     * Retrieve the BipartiteGraph for this BipartiteGrapher.
     *
     * @return BipartiteGraph The BipartiteGraph used by this BipartiteGrapher.  This might be null.
     */
    protected BipartiteGraph getBipartiteGraph() {
        return bipartiteGraph;
    }

    /**
     * Retrieve the traceManager for this BipartiteGrapher.
     *
     * @return TraceManager The TraceManager used by this BipartiteGrapher.  This might be null.
     */
    protected TraceManager getTraceManager() {
        return traceManager;
    }

    /**
     * Hide the graph from the user.
     *
     * @pre bipartiteGraph has been initialized (and is not-null!).
     */
    public void hide() {

        if (bipartiteGraph == null) {
            log.warn("Graph has not been initialized properly.  Skipping hide.");
            return;
        }

        bipartiteGraph.hideGraph();
    }

    /**
     * The main method will provide a means to test the
     * the BipartiteGrapher with test data.
     *
     * @param args an array of command-line arguments
     */
    public static void main(String[] args) {

        // create a simple graph of two threads and two objects
        BipartiteGraph graph = BipartiteGraphFactory.getInstance();
        //BipartiteGraph graph = BipartiteGraphFactory.getInstance(BipartiteGraphFactory.JGRAPH);
        //BipartiteGraph graph = BipartiteGraphFactory.getInstance(BipartiteGraphFactory.OPENJGRAPH);
        //BipartiteGraph graph = BipartiteGraphFactory.getInstance(BipartiteGraphFactory.GRAPPA);

        String thread1 = "p1:Process1";
        String thread2 = "p2:Process2";
        String object1 = "Deadlock.lock1:Lock";
        String object2 = "Deadlock.lock2:Lock";

        graph.addLockHoldingEdge(thread1, object1);
        graph.addLockHoldingEdge(thread2, object2);
        graph.addLockWaitingEdge(thread1, object2);
        graph.addLockWaitingEdge(thread2, object1);

        graph.showGraph();
    }

    /**
     * Set the bipartiteGraph for this BipartiteGrapher.
     *
     * @param BipartiteGraph newBipartiteGraph The bipartiteGraph to use for this BipartiteGrapher.
     */
    protected void setBipartiteGraph(BipartiteGraph newBipartiteGraph) {

        if (newBipartiteGraph == null) {
            log.info("Setting a null bipartiteGraph.  This might not be a good idea.");
            if (bipartiteGraph != null) {
                log.info("Overwriting a non-null bipartiteGraph with a null value.  This might not be a good idea.");
            }
        }

        bipartiteGraph = newBipartiteGraph;
    }

    /**
     * @param newTraceManager edu.ksu.cis.bandera.bui.counterexample.TraceManager
     */
    protected void setTraceManager(TraceManager newTraceManager) {
        traceManager = newTraceManager;
    }

    /**
     * Show the graph.
     *
     * @pre bipartiteGraph has been initialized (and is not-null!).
     */
    public void show() {

        if (bipartiteGraph == null) {
            log.warn("Graph has not been initialized properly.  Skipping show.");
            return;
        }

        bipartiteGraph.showGraph();
    }

    /**
     * This will set the visibility of the BipartiteGraph.  If the parameter is true
     * the graph will be visible, otherwise it will be invisible.
     *
     * @param boolean showIt True if the graph should be visible, false otherwise.
     */
    public void show(boolean showIt) {

        if (showIt) {
            show();
        }
        else {
            hide();
        }

    }

    /**
      * This will cause an update to the graph to happen.  This will make
      * the grapher re-construct the graph based upon the current information.  This
      * will then cause the BipartiteGraph to update what it displays.
      *
      * @pre bipartiteGraph is not null
      */
    public void update() {
	log.debug("updating graph ...");

        if (bipartiteGraph == null) {
            log.warn("BipartiteGraph has not been initialized properly.  Skipping update.");
            return;
        }

        /* Note: This is not the optimum way to go about this.  But since there is no way to
         * tell what has changed, there seems to be no other way than to clear it and build it
         * again at each step in the counter-example. -tcw
         */
        // clear the current graph
        bipartiteGraph.clear();

        // generate the graph
        generateGraph();

    }

}
