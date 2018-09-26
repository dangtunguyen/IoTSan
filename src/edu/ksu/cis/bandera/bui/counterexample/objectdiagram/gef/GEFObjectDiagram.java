package edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.ObjectNode;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectDiagram;
import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectData;

import edu.ksu.cis.bandera.ui.common.layout.Node;
import edu.ksu.cis.bandera.ui.common.layout.Point;
import edu.ksu.cis.bandera.ui.common.layout.DefaultNode;
import edu.ksu.cis.bandera.ui.common.layout.LayoutEngine;
import edu.ksu.cis.bandera.ui.common.layout.LayoutAdvisor;
import edu.ksu.cis.bandera.ui.common.layout.LayoutCalculator;
import edu.ksu.cis.bandera.ui.common.layout.LayoutManager;

import edu.ksu.cis.bandera.ui.common.layout.grid.GridLayoutAdvisor;

import edu.ksu.cis.bandera.ui.common.layout.grid.gef.GEFRootedGridLayoutEngine;
import edu.ksu.cis.bandera.ui.common.layout.grid.gef.GEFRootedGridLayoutManager;

import edu.ksu.cis.bandera.ui.common.layout.forcedirected.gef.GEFForceDirectedLayoutEngine;

import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.graph.presentation.JGraphFrame;
import org.tigris.gef.graph.presentation.DefaultGraphModel;
import org.tigris.gef.graph.presentation.NetPort;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigEdge;

import org.tigris.gef.base.LayerManager;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Editor;

import org.tigris.gef.ui.ToolBar;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.Rectangle;

import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//import javax.swing.JMenu;
//import javax.swing.JRadioButtonMenuItem;
//import javax.swing.JCheckBoxMenuItem;
//import javax.swing.ButtonGroup;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.*;

import org.apache.log4j.Category;

/**
 * The GEFObjectDiagram is an ObjectDiagram that is implemented using the GEF
 * (Graphical Editing Framework) library.  This library is an open source project
 * that is located here:
 * <a href="http://gef.tigris.org/">GEF</a>
 *
 * This provides the functionality to draw an object diagram that consists of
 * nodes and references.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:33:18 $
 */
public class GEFObjectDiagram
    implements edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectDiagram {

    /**
      * This is the actual JFrame that will contain the graphical information.
      */
    private JGraphFrame jgraphFrame;

    /**
      * A collection of the nodes in the diagram keyed on ObjectData objects
      * with values of type ObjectNode.
      */
    private Map nodeMap;

    /**
      * The default frame title if one is not specified.
      */
    public static final String DEFAULT_TITLE = "Object Diagram";

    /**
      * The default frame bounds if one is not specified.
      */
    public static final Rectangle DEFAULT_BOUNDS = new Rectangle(10, 10, 600, 400);

    private ArrayList edgeList;

    /**
      * This is the log to write to.
      */
    private static Category log = Category.getInstance(GEFObjectDiagram.class);

    /**
     * A group of radio buttons that represents the choices for depth.
     */
    //private ButtonGroup depthFilterMenuButtonGroup;

    /**
     * A layout engine for use in laying out the graph visually.
     */
    private LayoutEngine layoutEngine;

    /**
     * A layout advisor for use in layout out the graph visually.
     */
    private LayoutAdvisor layoutAdvisor;

    /**
     * A layout calculator for use in layout out the graph visually.
     */
    private LayoutCalculator layoutCalculator;

    /**
     * A layout manager for use in layout of the graph visually.
     */
    private LayoutManager layoutManager;

    private boolean useLayoutEngine;

    private boolean useLayoutAdvisor;

    private boolean useLayoutCalculator;

    private boolean useLayoutManager;

    private Fig rootFig;

    /**
     * Create a new ObjectDiagram that uses the GEF library.  This will get the
     * diagram ready to handle additions of nodes and references.  To do this,
     * it will create a new JGraphFrame.  This will use the default title and
     * default bounds.
     */
    public GEFObjectDiagram() {
        this(DEFAULT_TITLE);
    }

    /**
     * Create a new ObjectDiagram that uses the GEF library.  This will get the
     * diagram ready to handle additions of nodes and references.  To do this,
     * it will create a new JGraphFrame.  This will use the given title and
     * default bounds.
     */
    public GEFObjectDiagram(String title) {
        this(title, DEFAULT_BOUNDS);
    }

    /**
     * Create a new ObjectDiagram that uses the GEF library.  This will get the
     * diagram ready to handle additions of nodes and references.  To do this,
     * it will create a new JGraphFrame.  This will use the given title and
     * given bounds.
     */
    public GEFObjectDiagram(String title, Rectangle bounds) {

        nodeMap = new HashMap();
        edgeList = new ArrayList();

        // init the jgraphFrame and set it invisible
        jgraphFrame = new JGraphFrame(title);
        jgraphFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                jgraphFrame.dispose();
            }
            /*public void windowClosed(WindowEvent event) {
                System.exit(0);
            }
            */ });

        // make sure there is no menu bar
        JMenuBar emptyJMenuBar = new JMenuBar();
        jgraphFrame.setJMenuBar(emptyJMenuBar);

	// add a menu bar that allows the user to filter the diagram based upon type and/or depth
	/*
	JMenuBar mainMenuBar = new JMenuBar();
	JMenu filterMenu = new JMenu("Filter");

	int[] depthFilterItems = {-1, 0, 1, 2, 3, 4, 5};
	depthFilterMenuButtonGroup = new ButtonGroup();
	JMenu depthFilterMenu = new JMenu("Depth");
	for(int i = 0; i < depthFilterItems.length; i++) {
	    JRadioButtonMenuItem x = new JRadioButtonMenuItem(Integer.toString(depthFilterItems[i]));
	    // add an action listener! -tcw
	    depthFilterMenuButtonGroup.add(x);
	    depthFilterMenu.add(x);

	    // make sure the first radio button in the list is selected
	    if(i == 0) {
		x.setSelected(true);
	    }
	}
	filterMenu.add(depthFilterMenu);

	JMenu typeFilterMenu = new JMenu("Type");
	JMenuItem noTypeFilterMenuItem = new JCheckBoxMenuItem("None");
	// add an action listener! -tcw
	typeFilterMenu.add(noTypeFilterMenuItem);
	JMenuItem allTypeFilterMenuItem = new JCheckBoxMenuItem("All");
	// add an action listener! -tcw
	typeFilterMenu.add(allTypeFilterMenuItem);
	filterMenu.add(typeFilterMenu);

	jgraphFrame.setJMenuBar(mainMenuBar);
	*/

        // make sure there is no tool bar
        ToolBar emptyToolBar = new ToolBar();
        jgraphFrame.setToolBar(emptyToolBar);

        // set the default size - this should be global and settable/gettable
        jgraphFrame.setBounds(bounds);

        LayerManager lm = jgraphFrame.getGraph().getEditor().getLayerManager();
        LayerPerspective lay = (LayerPerspective) lm.getActiveLayer();
        lay.addNodeTypeRegion(ObjectNode.class, new Rectangle(10, 10, 100, 100));
        lay.addNodeTypeRegion(FigObjectNode.class, new Rectangle(10, 10, 100, 100));

        // don't show the diagram yet
        show(false);

    }

    /**
     * This method will add a node to our diagram using the given ObjectData.  If the node already
     * exists this will not do anything.  If it doesn't exist, we will create a new node and add
     * it to the graph.
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.ObjectDiagram
     * @param ObjectData objectDataNode The node to add to the graph.
     * @pre jgraphFrame is not null.
     * @pre objectDataNode is not null.
     * @pre nodeMap is not null.
     */
    public void addNode(ObjectData objectDataNode) {

        ObjectNode objectNode = getObjectNode(objectDataNode);
        if (objectNode == null) {

            // add the node to the graph
            objectNode = new ObjectNode(objectDataNode);

            DefaultGraphModel dgm = (DefaultGraphModel) jgraphFrame.getGraphModel();
            dgm.addNode(objectNode);

	    if(useLayoutAdvisor) {
		// layout the object in the graph
		FigObjectNode figObjectNode = objectNode.getFigObjectNode();
		Rectangle currentBounds = figObjectNode.getBounds();
		Node node = new DefaultNode();
		node.setHeight(currentBounds.height);
		node.setWidth(currentBounds.width);
		Point point = layoutAdvisor.nextNodeLocation(node);
		currentBounds.x = node.getX();
		currentBounds.y = node.getY();
		log.debug("setting this node (" + node.getID() +
			  ") at point (" + node.getX() + ", " +
			  node.getY() + ").");
		figObjectNode.setBounds(currentBounds);

		JGraph jgraph = jgraphFrame.getGraph();
		Editor editor = jgraph.getEditor();
		editor.damaged(figObjectNode);
	    }

            // add the node to our internal store
            nodeMap.put(objectDataNode, objectNode);

	    // we assume the first node added will be the root. -tcw
	    if(rootFig == null) {
		rootFig = objectNode.getFigObjectNode();
	    }
        }
    }

    /**
     * Add a reference from one node's field to another node.  This represents a pointer
     * to another object in the underlying system.  Before adding the reference between the
     * two nodes, we will make sure the two nodes are added to the graph first.
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.ObjectDiagram
     * @param ObjectData fromObjectDataNode This is the source object of the pointer.
     * @param int fieldNumber This is the field in the source object that holds the value of the pointer
     *        to the object.
     * @param ObjectData toObjectDataNode This is the destination of the pointer.
     * @pre fromObjectDataNode is not null.
     * @pre fieldNumber is > 0 and is a valid field number in the fromObjectDataNode
     * @pre toObjectDataNode is not null.
     * @pre jgraphFrame is not null
     */
    public void addReference(ObjectData fromObjectDataNode,
			     int fieldNumber,
			     ObjectData toObjectDataNode) {

        // precondition!
        if (fromObjectDataNode == null) {
            log.error("fromObjectDataNode is null.  Cannot add a reference from a null.");
            return;
        }

        // precondition!
        if (toObjectDataNode == null) {
            log.error("toObjectDataNode is null.  Cannot add a reference to a null.");
            return;
        }

        // precondition!
        if (jgraphFrame == null) {
            log.error(
                "jgraphFrame is null.  Cannot add a reference without a valid JGraphFrame.");
            return;
        }

        ObjectNode fromObjectNode = getObjectNode(fromObjectDataNode);
        if (fromObjectNode == null) {
            addNode(fromObjectDataNode);
            fromObjectNode = getObjectNode(fromObjectDataNode);
            // should we check to see if it is null again? -tcw
        }

        // precondition!
        int fromObjectNodePortCount = fromObjectNode.getPorts().size();
        if (fieldNumber > fromObjectNodePortCount) {
            log.error(
                "fieldNumber ("
                    + fieldNumber
                    + ") is greater than the available references ("
                    + fromObjectNodePortCount
                    + ").  Cannot add the reference.");
        }

        ObjectNode toObjectNode = getObjectNode(toObjectDataNode);
        if (toObjectNode == null) {
            addNode(toObjectDataNode);
            toObjectNode = getObjectNode(toObjectDataNode);
            // should we check to see if it is null again? -tcw
        }

        // assume: fromObjectNode is not null
        // assume: toObjectNode is not null

        // now add the reference between the two nodes using the given field number!
        DefaultGraphModel dgm = (DefaultGraphModel) jgraphFrame.getGraphModel();
        if (dgm == null) {
            log.error("DefaultGraphModel is null.  This should never happen!");
            return;
        }

        // assume: dgm is not null

        // get the port where the edge is coming from (or source)
        NetPort fromObjectPort = fromObjectNode.getPort(fieldNumber + 1);
        if ((fromObjectPort == null) || (!(fromObjectPort instanceof ObjectPort))) {
            log.error(
                "fromObjectPort is invalid (either null or not an ObjectPort."
                    + "  Cannot add the reference.");
            return;
        }

        // get the port where the edge is going to (or destination)
        NetPort toObjectPort = toObjectNode.getPort(0);
        if ((toObjectPort == null) || (!(toObjectPort instanceof ObjectPort))) {
            log.error(
                "toObjectPort is invalid (either null or not an ObjectPort."
                    + "  Cannot add the reference.");
            return;
        }

        // assume: fromObjectPort is not null
        // assume: toObjectPort is not null

        // connect the from port (source) to the to port (destination)
        ObjectEdge edge = new ObjectEdge();
        edge.connect(dgm, toObjectPort, fromObjectPort);
        dgm.addEdge(edge);
        edgeList.add(edge);

    }

    /**
     * Clear all nodes and references from the current diagram.
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.ObjectDiagram
     * @pre nodeMap is not null
     * @pre jgraphFrame is not null
     */
    public void clear() {

        if (nodeMap == null) {
            log.error("Cannot clear the GEFObjectDiagram.  nodeMap is null.  System is unstable.");
            return;
        }

        if (jgraphFrame == null) {
            log.error("Cannot clear the GEFObjectDiagram.  jgraphFrame is null.  System is unstable.");
            return;
        }

        DefaultGraphModel dgm = (DefaultGraphModel) jgraphFrame.getGraphModel();
        Editor editor = jgraphFrame.getGraph().getEditor();

        // walk thru and delete all nodes out of the DefaultGraphModel
        Set keySet = nodeMap.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            Object nextKey = iterator.next();
            Object nextValue = nodeMap.get(nextKey);

            if (nextValue instanceof ObjectNode) {
                ObjectNode currentObjectNode = (ObjectNode) nextValue;
                dgm.removeNode(currentObjectNode);
                editor.remove((Fig) currentObjectNode.getFigObjectNode());
            }
        }

        // walk thru and delete all the edges that are in teh DefaultGraphModel
        for (int i = 0; i < edgeList.size(); i++) {
            ObjectEdge objectEdge = (ObjectEdge) edgeList.get(i);
            dgm.removeEdge(objectEdge);
        }

	rootFig = null;

        // clear the nodeMap
        nodeMap.clear();

        // now update the GUI ?????
        jgraphFrame.update(jgraphFrame.getGraphics());

    }

    /**
     * Clean up and close the window.
     */
    public void close() {

        // hide the graph from the user
        hide();

        // clear the graph of all nodes and edges
        clear();

	if(layoutEngine != null) {
	    // stop the layout engine
	    layoutEngine.quit();
	    /* should we join? -tcw
	       try {
	       layoutEngine.join();
	       }
	       catch(Exception e) {
	       }
	    */
	}

        // dispose of the JGraphFrame if it exists
        if (jgraphFrame != null) {
            jgraphFrame.dispose();
        }
    }

    /**
     * Get the ObjectNode associated with the given ObjectData.
     *
     * @param ObjectData objectData The ObjectData for which the user is requesting
     *        the matching ObjectNode.
     * @return ObjectNode The ObjectNode that matches the ObjectData given or
     *         null if it cannot be found (or rather is not defined in the
     *         current store yet).
     * @pre nodeMap is not null.
     * @pre objectData is not null.
     */
    private ObjectNode getObjectNode(ObjectData objectData) {

        if (nodeMap == null) {
            log.error(
                "nodeMap is null.  The GEFObjectDiagram has not been initialized properly.");
            return (null);
        }

        if (objectData == null) {
            log.error("objectData is null.  No ObjectNode matches a null value.");
            return (null);
        }

        return ((ObjectNode) nodeMap.get(objectData));

    }

    /**
     * Hide this graph from the user.
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectDiagram
     */
    public void hide() {
        show(false);
    }

    /**
     * Perform auto-layout for this diagram.
     */
    public void layout() {

	log.debug("Laying out the GEFObjectDiagram ...");

	if(useLayoutCalculator) {
	    // since we have not implemented a proper layout calculator for GEF, just
	    //  make use of the grid layout advisor
	    GridLayoutAdvisor gla = new GridLayoutAdvisor(10, 10, 10000, 1000);
	    
	    int id = 0;
	    Set keySet = nodeMap.keySet();
	    Iterator iterator = keySet.iterator();
	    while (iterator.hasNext()) {
		Object key = iterator.next();
		ObjectNode objectNode = (ObjectNode) nodeMap.get(key);
		FigObjectNode figObjectNode = objectNode.getFigObjectNode();
		Rectangle currentBounds = figObjectNode.getBounds();
		
		Node node = new DefaultNode(id++,
					    currentBounds.x, currentBounds.y,
					    currentBounds.height, currentBounds.width);
		Point point = gla.nextNodeLocation(node);
		log.debug("setting this node (" + node.getID() +
			  ") at point (" + node.getX() + ", " +
			  node.getY() + ").");
		
		currentBounds.x = node.getX();
		currentBounds.y = node.getY();
		figObjectNode.setBounds(currentBounds);
	    }
	}

	log.debug("useLayoutManager = " + useLayoutManager);
	log.debug("layoutManager = " + layoutManager);
	if((useLayoutManager) && (layoutManager != null)) {
	    log.debug("Laying out the graph using a LayoutManager ...");

	    if(layoutManager instanceof GEFRootedGridLayoutManager) {
		GEFRootedGridLayoutManager grglm = (GEFRootedGridLayoutManager)layoutManager;
		grglm.setRootFig(rootFig);
		log.debug("set the rootFig in the GEFRootedGridLayoutManager = " + rootFig);
	    }

	    layoutManager.layout();
	}

	log.debug("Finished layout of the GEFObjectDiagram.");
    }

    /**
     * Show this graph to the user.
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectDiagram
     */
    public void show() {
        show(true);
    }

    /**
     * Show or hide this diagram from the user depending on the value given.  It
     * showIt is true, the diagram will be visible to the user.  Otherwise, it
     * will be hidden from the user.
     *
     * @see edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectDiagram
     * @param boolean showIt true if the user should see the diagram, false otherwise.
     * @pre jgraphFrame is not null.
     */
    public void show(boolean showIt) {

	if(jgraphFrame == null) {
	    return;
	}

	if(showIt) {
	    jgraphFrame.setVisible(true);
	    jgraphFrame.toFront();

	    // only start a layout engine if it has been created and
	    //  it is supposed to be used.
	    if((layoutEngine != null) && (useLayoutEngine)) {
		layoutEngine.startLayout();
	    }
	}
	else {
	    jgraphFrame.setVisible(false);

	    // if the layout engine exists, make sure it is stopped.
	    if(layoutEngine != null) {
		layoutEngine.stopLayout();
	    }
	}

    }

    /**
     * Set the LayoutEngine to use with this ObjectDiagram.
     *
     * @param LayoutEngine An instance of LayoutEngine to use for the
     *        layout of objects in the diagram.
     */
    public void setLayoutEngine(LayoutEngine layoutEngine) {
	this.layoutEngine = layoutEngine;

	/*
	 * THIS IS A HACK! -tcw
	 */
	if((layoutEngine != null) && (layoutEngine instanceof GEFRootedGridLayoutEngine)) {
	    GEFRootedGridLayoutEngine rgle = (GEFRootedGridLayoutEngine)layoutEngine;
	    rgle.setJGraphFrame(jgraphFrame);
	}
	/*
	 * END HACK.
	 */
    }

    /**
     * Set the LayoutEngine to use with this ObjectDiagram.
     *
     * @param LayoutEngine layoutEngine An instance of LayoutEngine to use for the
     *        layout of objects in the diagram.
     * @param boolean useLayoutEngine A flag to tell the ObjectDiagram to use
     *        the given LayoutEngine instead of a LayoutCalculator or
     *        LayoutAdvisor.
     */
    public void setLayoutEngine(LayoutEngine layoutEngine, boolean useLayoutEngine) {
	setLayoutEngine(layoutEngine);
	setUseLayoutEngine(useLayoutEngine);
    }

    /**
     * Set the usage flag for LayoutEngine.
     *
     * @param boolean useLayoutEngine True if the LayoutEngine should be used, false otherwise.
     */
    public void setUseLayoutEngine(boolean useLayoutEngine) {
	this.useLayoutEngine = useLayoutEngine;
	this.useLayoutCalculator = !useLayoutManager;
	this.useLayoutManager = !useLayoutManager;
	this.useLayoutAdvisor = !useLayoutManager;

	if((layoutEngine != null) && (useLayoutEngine)) {
	    layoutEngine.start();
	}
    }
    public boolean usingLayoutEngine() {
	return(useLayoutEngine);
    }
    public LayoutEngine getLayoutEngine() {
	return(layoutEngine);
    }

    /**
     * Set the LayoutAdvisor to use with this ObjectDiagram.
     *
     * @param LayoutAdvisor An instance of LayoutAdvisor to use for the
     *        layout of objects in the diagram.
     */
    public void setLayoutAdvisor(LayoutAdvisor layoutAdvisor) {
	this.layoutAdvisor = layoutAdvisor;
    }

    /**
     * Set the LayoutAdvisor to use with this ObjectDiagram.
     *
     * @param LayoutAdvisor layoutAdvisor An instance of LayoutAdvisor to use for the
     *        layout of objects in the diagram.
     * @param boolean useLayoutAdvisor A flag to tell the ObjectDiagram to use
     *        the given LayoutAdvisor instead of a LayoutCalculator or
     *        LayoutEngine.
     */
    public void setLayoutAdvisor(LayoutAdvisor layoutAdvisor, boolean useLayoutAdvisor) {
	setLayoutAdvisor(layoutAdvisor);
	setUseLayoutAdvisor(useLayoutAdvisor);
    }
    public void setUseLayoutAdvisor(boolean useLayoutAdvisor) {
	this.useLayoutAdvisor = useLayoutAdvisor;
	this.useLayoutCalculator = !useLayoutManager;
	this.useLayoutManager = !useLayoutManager;
	this.useLayoutEngine = !useLayoutManager;
    }
    public boolean usingLayoutAdvisor() {
	return(useLayoutAdvisor);
    }
    public LayoutAdvisor getLayoutAdvisor() {
	return(layoutAdvisor);
    }

    /**
     * Set the LayoutCalculator to use with this ObjectDiagram.
     *
     * @param LayoutCalculator An instance of LayoutCalculator to use for the
     *        layout of objects in the diagram.
     */
    public void setLayoutCalculator(LayoutCalculator LayoutCalculator) {
	this.layoutCalculator = layoutCalculator;
    }

    /**
     * Set the LayoutCalculator to use with this ObjectDiagram.
     *
     * @param LayoutCalculator layoutCalculator An instance of Layoutcalculator to use for the
     *        layout of objects in the diagram.
     * @param boolean useLayoutCalculator A flag to tell the ObjectDiagram to use
     *        the given LayoutCalculator instead of a LayoutEngine or
     *        LayoutAdvisor.
     */
    public void setLayoutCalculator(LayoutCalculator layoutCalculator, boolean useLayoutCalculator) {
	setLayoutCalculator(layoutCalculator);
	setUseLayoutCalculator(useLayoutCalculator);
    }
    public void setUseLayoutCalculator(boolean useLayoutCalculator) {
	this.useLayoutCalculator = useLayoutCalculator;
	this.useLayoutAdvisor = !useLayoutManager;
	this.useLayoutManager = !useLayoutManager;
	this.useLayoutEngine = !useLayoutManager;
    }
    public boolean usingLayoutCalculator() {
	return(useLayoutCalculator);
    }
    public LayoutCalculator getLayoutCalculator() {
	return(layoutCalculator);
    }

    /**
     * Set the LayoutManager to use with this ObjectDiagram.
     *
     * @param LayoutManager An instance of LayoutManager to use for the
     *        layout of objects in the diagram.
     */
    public void setLayoutManager(LayoutManager layoutManager) {

	log.debug("Setting the layoutManager: " + layoutManager);

	this.layoutManager = layoutManager;

	if((layoutManager != null) && (layoutManager instanceof GEFRootedGridLayoutManager)) {
	    log.debug("The layoutManager is a GEFRootedGridLayoutManager.");
	    GEFRootedGridLayoutManager grglm = (GEFRootedGridLayoutManager)layoutManager;
	    grglm.setJGraphFrame(jgraphFrame);
	}
    }

    /**
     * Set the LayoutManager to use with this ObjectDiagram.
     *
     * @param LayoutManager layoutManager An instance of LayoutManager to use for the
     *        layout of objects in the diagram.
     * @param boolean useLayoutManager A flag to tell the ObjectDiagram to use
     *        the given LayoutManager instead of a LayoutEngine or
     *        LayoutAdvisor or LayoutCalculator.
     */
    public void setLayoutManager(LayoutManager layoutManager, boolean useLayoutManager) {
	setLayoutManager(layoutManager);
	setUseLayoutManager(useLayoutManager);
    }
    public void setUseLayoutManager(boolean useLayoutManager) {
	this.useLayoutManager = useLayoutManager;
	this.useLayoutAdvisor = !useLayoutManager;
	this.useLayoutCalculator = !useLayoutManager;
	this.useLayoutEngine = !useLayoutManager;
    }
    public boolean usingLayoutManager() {
	return(useLayoutManager);
    }
    public LayoutManager getLayoutManager() {
	return(layoutManager);
    }

}
