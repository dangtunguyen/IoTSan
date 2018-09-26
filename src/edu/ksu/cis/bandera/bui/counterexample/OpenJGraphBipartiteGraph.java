package edu.ksu.cis.bandera.bui.counterexample;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import edu.ksu.cis.bandera.bui.counterexample.BipartiteGraph;

import salvo.jesus.graph.Graph;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.GraphFactory;

import salvo.jesus.graph.visual.VisualGraph;
import salvo.jesus.graph.visual.VisualEdge;
import salvo.jesus.graph.visual.VisualVertex;
import salvo.jesus.graph.visual.GraphScrollPane;

import salvo.jesus.graph.visual.layout.ForceDirectedLayout;
import salvo.jesus.graph.visual.layout.OrthogonalLineLayout;
import salvo.jesus.graph.visual.layout.StraightLineLayout;
import salvo.jesus.graph.visual.layout.LayeredTreeLayout;

import org.apache.log4j.Category;


/**
 * This is an implementation of a BipartiteGraph using the OpenJGraph
 * graphing package.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:18 $
 */
public class OpenJGraphBipartiteGraph extends JFrame implements BipartiteGraph {

    /**
     * The log to use.
     */
    private static Category log = Category.getInstance(OpenJGraphBipartiteGraph.class);

    /**
     * The graph view that is used to display the graph model.
     */
    private VisualGraph visualGraph;

    /**
     * The graph model that holds the edges and vertices that make up the lock graph.
     */
    private Graph graph;

    /**
     * Common store of vertices for all graphs.  This allows us to re-use Vertex
     * objects across all OpenJGraphBipartiteGraph objects.  This should save a
     * great deal of memory and time (less time for object creation).
     */
    private static Map verticesMap;

    /**
     * Common store of edges for all graphs.  This allows us to re-use Edge
     * objects across all OpenJGraphBipartiteGraph objects.  This should save a
     * great deal of memory and time (less time for object creation).
     */
    private static ArrayList edgeList;

    /**
     * The factory object that will create Vertex and Edge objects.
     */
    private GraphFactory graphFactory;

    /**
     * A local store of edges that are used in this instance of a lock graph.  All edges
     * in this store are currently visible to the user.
     */
    private Set edgeStatusSet;

    /**
     * A local store of vertices that are used in this instance of a lock graph.  All
     * vertices in this store are currently visible to the user.
     */
    private Set vertexStatusSet;

    private boolean graphHasData;

    private static final Color THREAD_OBJECT_FILL_COLOR = Color.green;
    private static final Color OBJECT_FILL_COLOR = Color.yellow;
    private static final Color HOLDING_EDGE_FILL_COLOR = Color.blue;
    private static final Color WAITING_EDGE_FILL_COLOR = Color.red;

    /**
     * The layout manager that we are using.
     * The ForceDirectedLayout manager ...
     * The OrthogonalLineLayout manager ...
     * The StraightLineLayout manager ...
     * The LayeredTreeLayout manager ...
     */
    private ForceDirectedLayout layout;
    //private OrthogonalLineLayout layout;
    //private StraightLineLayout layout;
    //private LayeredTreeLayout layout;

    /**
     * Create a new instance of the OpenJGraph using the default
     * title.
     */
    public OpenJGraphBipartiteGraph() {
	this("Lock Graph");
    }

    /**
     * Create a new instance of the OpenJGraph using the specified
     * title.
     */
    public OpenJGraphBipartiteGraph(String title) {
        super(title);

	// Initialize the common store of vertices if it isn't already.
	// this should probably be sync! -tcw
	if(verticesMap == null) {
	    verticesMap = new HashMap();
	}

	// Initialize the common store of edges if it isn't already.
	// this should probably be sync! -tcw
	if(edgeList == null) {
	    edgeList = new ArrayList();
	}

	// initialize the local store of edges.
	edgeStatusSet = new HashSet();

	// initialize the local store of vertices.
	vertexStatusSet = new HashSet();

	// create and initialize the graph model and view and put it inside a scroll pane
        graph = new DirectedGraphImpl();
        visualGraph = new VisualGraph();
        visualGraph.setGraph(graph);
        GraphScrollPane graphScrollPane = new GraphScrollPane();
        graphScrollPane.setVisualGraph(visualGraph);

	// Initialize the common graph factory object if it isn't already.
	// this should probably be sync! -tcw
	if(graphFactory == null) {
	    graphFactory = graph.getGraphFactory();
	}

	/* Set up the auto layout of the graph */
	layout = new ForceDirectedLayout(visualGraph);
	layout.setSpringLength(200); // default: 50
	layout.setStiffness(10); // default: 50
	layout.setEletricalRepulsion(150); // default: 400
	layout.setIncrement(0.75); // default: 0.50

	/* Other layout manager available but not appropriate for these graphs. -tcw */
	//layout = new OrthogonalLineLayout(visualGraph);
	//layout = new StraightLineLayout(visualGraph);
	//layout = new LayeredTreeLayout(visualGraph);

	visualGraph.setGraphLayoutManager(layout);

	// Add the scroll pane that contains the graph view to this JFrame's content pane
	//  and set the default size.
        this.getContentPane().add(graphScrollPane);
        this.setSize(new Dimension(600, 400));

	graphHasData = false;

	/*
	if(log.isDebugEnabled()) {
	    getToolkit().addAWTEventListener(new AWTEventListener() {
		    public void eventDispatched(AWTEvent e) {
			System.out.println("Event caught: " + e);
		    }
		}, AWTEvent.PAINT_EVENT_MASK |
		   AWTEvent.ACTION_EVENT_MASK |
		   AWTEvent.CONTAINER_EVENT_MASK |
		   AWTEvent.COMPONENT_EVENT_MASK);	    
	}
	*/
    }

    /**
     * Add a new edge to the graph that represents an object (or more specifically a thread)
     * holding a lock on an object.
     *
     * @param Object holderObject The object that is holding the lock.
     * @param Object heldObject The object whose lock is held by the holderObject.
     * @post The two objects will be added to the graph with an edge in between them
     *       or an error message will be printed.
     * @pre holderObject is not null.
     * @pre heldObject is not null.
     * @pre graph is not null.
     * @pre visualGraph is not null.
     * @pre layout is not null.
     */
    public void addLockHoldingEdge(Object holderObject, Object heldObject) {
	addLockHoldingEdge(holderObject, "holds", heldObject);
    }

    /**
     * Add a new edge to the graph that represents an object (or more specifically a thread)
     * holding a lock on an object with the given association name.
     *
     * @param Object holderObject The object that is holding the lock.
     * @param String associationName The name to use for the association.
     * @param Object heldObject The object whose lock is held by the holderObject.
     * @post The two objects will be added to the graph with an edge in between them
     *       or an error message will be printed.
     * @pre holderObject is not null.
     * @pre heldObject is not null.
     * @pre graph is not null.
     * @pre visualGraph is not null.
     * @pre layout is not null.
     */
    public void addLockHoldingEdge(Object holderObject, String associationName, Object heldObject) {

	if(holderObject == null) {
	    log.error("holderObject is null.  Cannot add a lock holding edge when the holder is null.");
	    return;
	}

	if(heldObject == null) {
	    log.error("heldObject is null.  Cannot add a lock holding edge when the held object is null.");
	    return;
	}

	if(layout == null) {
	    log.error("layout is null.  The lock graph has not been initialized properly.  Quitting.");
	    return;
	}

	if(graph == null) {
	    log.error("graph is null.  The lock graph has not been initialized properly.  Quitting.");
	    return;
	}

	if(visualGraph == null) {
	    log.error("visualGraph is null.  The lock graph has not been initialized properly.  Quitting.");
	    return;
	}

	if(associationName == null) {
	    associationName = "";
	}

        try {
            Vertex holderVertex = getVertex(holderObject);
	    if(holderVertex == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("holderVertex is null.  Cannot add an edge to a null Vertex.");
		return;
	    }
	    if(!(vertexStatusSet.contains(holderVertex))) {
		graph.add(holderVertex);
		vertexStatusSet.add(holderVertex);
	    }

            Vertex heldVertex = getVertex(heldObject);
	    if(heldVertex == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("heldVertex is null.  Cannot add an edge to a null Vertex.");
		return;
	    }
	    if(!(vertexStatusSet.contains(heldVertex))) {
		graph.add(heldVertex);
		vertexStatusSet.add(heldVertex);
	    }

	    Edge edge = getEdge(holderVertex, heldVertex);
	    if(edge == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("edge is null.  Cannot set properties on a null edge.");
		return;
	    }
	    if(!(edgeStatusSet.contains(edge))) {
		graph.addEdge(edge);
		edgeStatusSet.add(edge);
	    }

            VisualEdge visualEdge = visualGraph.getVisualEdge(edge);
	    if(visualEdge == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("visualEdge is null.  Cannot set properties on a null edge.");
		return;
	    }
            visualEdge.setFillcolor(HOLDING_EDGE_FILL_COLOR);
            visualEdge.setOutlinecolor(HOLDING_EDGE_FILL_COLOR);
	    visualEdge.setText(associationName);
	    VisualVertex holderVisualVertex = visualGraph.getVisualVertex(holderVertex);
	    holderVisualVertex.setFillcolor(THREAD_OBJECT_FILL_COLOR);
	    VisualVertex heldVisualVertex = visualGraph.getVisualVertex(heldVertex);
	    heldVisualVertex.setFillcolor(OBJECT_FILL_COLOR);

	    if(!graphHasData) {
		graphHasData = true;
		visualGraph.layout();
	    }

        }
        catch(Exception e) {
            log.error("Exception caught while adding a lock holding edge.", e);
        }

    }

    /**
     * Add a new edge to the graph that represents an object (or more specifically a thread)
     * waiting on a lock on an object.
     *
     * @param Object requestorObject The object that is requesting the lock.
     * @param Object requestedObject The object whose lock is currently held and is being
     *               requested by the requestorObject given.
     * @post The two objects will be added to the graph with an edge in between them
     *       or an error message will be printed.
     * @pre requestorObject is not null.
     * @pre requestedObject is not null.
     * @pre graph is not null.
     * @pre visualGraph is not null.
     * @pre layout is not null.
     */
    public void addLockWaitingEdge(Object requestorObject, Object requestedObject) {
	addLockWaitingEdge(requestorObject, "blocks", requestedObject);
    }

    public void addLockWaitingEdge(Object requestorObject, String associationName, Object requestedObject) {

	if(requestorObject == null) {
	    log.error("requestorObject is null.  Cannot add a lock waiting edge when the requestor is null.");
	    return;
	}

	if(requestedObject == null) {
	    log.error("requestedObject is null.  Cannot add a lock waiting edge when the requested object is null.");
	    return;
	}

	if(layout == null) {
	    log.error("layout is null.  The lock graph has not been initialized properly.  Quitting.");
	    return;
	}

	if(graph == null) {
	    log.error("graph is null.  The lock graph has not been initialized properly.  Quitting.");
	    return;
	}

	if(visualGraph == null) {
	    log.error("visualGraph is null.  The lock graph has not been initialized properly.  Quitting.");
	    return;
	}

	if(associationName == null) {
	    associationName = "";
	}

        try {
            Vertex requestorVertex = getVertex(requestorObject);
	    if(requestorVertex == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("The requestorVertex was null.  This is a sign that a problem exists in the Lock Graph.");
		return;
	    }
	    if(!(vertexStatusSet.contains(requestorVertex))) {
		graph.add(requestorVertex);
		vertexStatusSet.add(requestorVertex);
	    }

            Vertex requestedVertex = getVertex(requestedObject);
	    if(requestedVertex == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("The requestedVertex was null.  This is a sign that a problem exists in the Lock Graph.");
		return;
	    }
	    if(!(vertexStatusSet.contains(requestedVertex))) {
		graph.add(requestedVertex);
		vertexStatusSet.add(requestedVertex);
	    }

	    // the source of the edge will be the requested object so that this
	    //  graph will create cycles. If this is not the desired look, it
	    //  can be switched right here so that the source of the edge is
	    //  the requestor and the destination is the requested.  This will
	    //  create a graph where all edges originate from a thread object. -tcw
            Edge edge = getEdge(requestedVertex, requestorVertex);
	    if(edge == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("The edge was null.  This is a sign that a problem exists in the Lock Graph.");
		return;
	    }
	    if(!(edgeStatusSet.contains(edge))) {
		graph.addEdge(edge);
		edgeStatusSet.add(edge);
	    }

            VisualEdge visualEdge = visualGraph.getVisualEdge(edge);
	    if(visualEdge == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("The visualEdge was null.  This is a sign that a problem exists in the Lock Graph.");
		return;
	    }
            visualEdge.setFillcolor(WAITING_EDGE_FILL_COLOR);
            visualEdge.setOutlinecolor(WAITING_EDGE_FILL_COLOR);
	    visualEdge.setText(associationName);
	    VisualVertex requestorVisualVertex = visualGraph.getVisualVertex(requestorVertex);
	    requestorVisualVertex.setFillcolor(THREAD_OBJECT_FILL_COLOR);
	    VisualVertex requestedVisualVertex = visualGraph.getVisualVertex(requestedVertex);
	    requestedVisualVertex.setFillcolor(OBJECT_FILL_COLOR);

	    if(!graphHasData) {
		graphHasData = true;
		visualGraph.layout();
	    }
        }
        catch(Exception e) {
            log.error("Exception caught while adding a lock waiting edge.", e);

        }

    }

    /**
     * Add the given node to the graph.
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.BipartiteGraph
     */
    public void addNode(java.lang.Object node) {
	// this might not be the best organized way to go about this! -todd
	try {
	    Vertex vertex = getVertex(node);
	    if(vertex == null) {
		// an error occured, should we throw an Exception? -tcw
		log.error("The vertex was null.  This is a sign that a problem exists in the Lock Graph.");
		return;
	    }

	    if(!(vertexStatusSet.contains(vertex))) {
		graph.add(vertex);
		vertexStatusSet.add(vertex);
	    }
	}
	catch(Exception e) {
	    log.error("Exception while adding a node.", e);
	}
    }

    /**
     * Clear this graph so that a new one can be drawn.
     *
     * @pre edgeStatusSet is not null.
     * @pre vertexStatusSet is not null.
     * @post The graph is empty and is ready to handle edges and nodes being added.
     */
    public void clear() {

	log.debug("clearing the graph ...");

	if(edgeStatusSet == null) {
	    log.error("edgeStatusSet is null.  This lock graph has not been initialized properly.");
	    return;
	}

	if(vertexStatusSet == null) {
	    log.error("vertexStatusSet is null.  This lock graph has not been initialized properly.");
	    return;
	}

	if(graphHasData) {
	    graphHasData = false;
	    visualGraph.layout(); // stop the layout engine! -tcw
	}

	// remove all edges from the graph
	Iterator iterator = edgeStatusSet.iterator();
	while(iterator.hasNext()) {
	    Edge currentEdge = (Edge)iterator.next();
	    try {
		graph.removeEdge(currentEdge);
	    }
	    catch(Exception e) {
		// skip this edge
		log.debug("Skipping this edge since removing it throws an exception.", e);
	    }
	}

	// remove all vertices from the graph
	iterator = vertexStatusSet.iterator();
	while(iterator.hasNext()) {
	    Vertex currentVertex = (Vertex)iterator.next();
	    try {
		graph.remove(currentVertex);
	    }
	    catch(Exception e) {
		// skip this vertex
		log.debug("Skipping this vertex since removing it throws an exception.", e);
	    }
	}

	// clear the edgeStatusSet
	edgeStatusSet.clear();

	// clear the vertexStatusSet
	vertexStatusSet.clear();
    }

    /**
     * Get the vertex associated with this object or create a new one
     * based upon it.
     *
     * @param Object object The object that will represent this vertex.
     * @return Vertex The vertex associated with the given object.
     * @pre object is not null.
     * @pre verticesMap is not null.
     */
    private synchronized Vertex getVertex(Object object) {

	if(object == null) {
	    log.error("object is null.  Cannot create or get a Vertex for a null object.  Returning null.");
	    return(null);
	}

	if(verticesMap == null) {
	    log.error("verticesMap is null.  This lock graph has not been initialized properly.");
	    return(null);
	}

        String key = object.toString();
	log.debug("Getting a vertex: " + key);
        Vertex vertex = (Vertex)verticesMap.get(key);

        try {
            if(vertex == null) {
		log.debug("Creating a new Vertex: " + key);
                vertex = new VertexImpl(key);

		/* This would be the proper way to do it but the interface doesn't allow
		 * for the setting of a name for the vertex! -tcw
		vertex = graphFactory.createVertex();
		if(vertex instanceof VertexImpl) {
		    ((VertexImpl)vertex).setObject(key);
		}
		else {
		    log.debug("This is weird, the vertex is not a VertexImpl.  It is a " + vertex.getClass().getName());
		}
		*/

                verticesMap.put(key, vertex);
            }
        }
        catch(Exception e) {
	    log.error("An exception occured while getting a Vertex from the current store.", e);
	    vertex = null;
        }

        return(vertex);
    }

    /**
     * Get the Edge associated with this source Vertex and this destination Vertex or
     * create a new Edge if it doesn't exist yet.
     *
     * @pre sourceVertex is not null.
     * @pre destinationVertex is not null.
     * @pre edgeList is not null.
     */
    private synchronized Edge getEdge(Vertex sourceVertex, Vertex destinationVertex) {

	if(sourceVertex == null) {
	    log.error("sourceVertex is null.  Cannot find get/create an edge that has a null Vertex.");
	    return(null);
	}
	if(destinationVertex == null) {
	    log.error("destinationVertex is null.  Cannot get/create an edge that has a null Vertex.");
	    return(null);
	}
	if(edgeList == null) {
	    log.error("edgeList is null.  The lock graph has not been initialized properly.  Quitting.");
	    return(null);
	}

	// check to see if this edge already exists in the common store of edges
	Edge edge = null;
	for(int i = 0; i < edgeList.size(); i++) {
	    List currentList = (List)edgeList.get(i);
	    Vertex currentSourceVertex = (Vertex)currentList.get(0);
	    Vertex currentDestinationVertex = (Vertex)currentList.get(1);
	    Edge currentEdge = (Edge)currentList.get(2);
	    if((currentSourceVertex == sourceVertex) &&
	       (currentDestinationVertex == destinationVertex)) {
		edge = currentEdge;
		log.debug("found edge: " + currentEdge.toString());
		break;
	    }
	}

	// if this edge has not been created, create it
	if(edge == null) {
	    try {
		// create a new edge
		//edge = graph.addEdge(sourceVertex, destinationVertex);
		//edge = new EdgeImpl(sourceVertex, destinationVertex);
		edge = graphFactory.createEdge(sourceVertex, destinationVertex);

		// add the new edge to the collection
		List newList = new ArrayList(3);
		newList.add(sourceVertex);
		newList.add(destinationVertex);
		newList.add(edge);
		edgeList.add(newList);
	    }
	    catch(Exception e) {
		log.error("Exception caught while adding an edge to the graph.", e);
		edge = null;
	    }
	}

	return(edge);
    }

    /**
     * Make this graph invisible to the user.
     */
    public void hideGraph() {
        this.setVisible(false);
    }

    /**
     * Make this graph visible to the user.
     */
    public void showGraph() {
        this.setVisible(true);
    }

}
