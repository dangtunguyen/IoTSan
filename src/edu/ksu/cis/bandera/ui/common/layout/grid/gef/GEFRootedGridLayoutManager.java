package edu.ksu.cis.bandera.ui.common.layout.grid.gef;

import org.tigris.gef.presentation.Fig;

import edu.ksu.cis.bandera.ui.common.layout.LayoutManager;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.FigObjectNode;
import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.ObjectNode;

import org.tigris.gef.graph.presentation.NetNode;
import org.tigris.gef.graph.presentation.NetEdge;
import org.tigris.gef.graph.presentation.NetPort;
import org.tigris.gef.graph.presentation.JGraphFrame;
import org.tigris.gef.graph.presentation.JGraph;

import org.tigris.gef.base.Editor;

import java.util.Set;
import java.util.HashSet;
import java.util.Vector;

import org.apache.log4j.Category;

/**
 * The GEFRootedGridLayoutManager provides a simple layout
 * algorithm used in graphs that can be rooted like an
 * Object diagram (Class diagram).  To use this, create
 * a new instance, set the root (and margins), and tell
 * it to layout.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:46 $
 */
public class GEFRootedGridLayoutManager implements LayoutManager {

    /*
     * is it better to use fig or node for the root? -tcw
     */

    private JGraphFrame jGraphFrame;

    private Fig rootFig;
    private Set visitedFigSet;

    private int verticalMargin;
    private int horizontalMargin;

    public static final int DEFAULT_VERTICAL_MARGIN = 10;
    public static final int DEFAULT_HORIZONTAL_MARGIN = 30;

    private static final Category log = Category.getInstance(GEFRootedGridLayoutManager.class);

    public GEFRootedGridLayoutManager() {
	this(null, null);
    }

    public GEFRootedGridLayoutManager(Fig rootFig, JGraphFrame jgraphFrame) {
	this(rootFig, jgraphFrame, DEFAULT_VERTICAL_MARGIN, DEFAULT_HORIZONTAL_MARGIN);
    }

    public GEFRootedGridLayoutManager(Fig rootFig, JGraphFrame jgraphFrame, int verticalMargin, int horizontalMargin) {
	super();
	setRootFig(rootFig);
	setJGraphFrame(jgraphFrame);
	setVerticalMargin(verticalMargin);
	setHorizontalMargin(horizontalMargin);
	visitedFigSet = new HashSet();
    }

    /**
     * This is a rude layout algorithm ... won't return until it has completely
     * finished the layout process.  This should be fixed to be nicer!
     */
    public void layout() {

	log.debug("Starting to layout ...");

	if(jGraphFrame == null) {
	    log.error("Cannot layout a graph with a null jGraphFrame.");
	    return;
	}

	if(rootFig == null) {
	    log.error("Cannot layout with a null rootFig.");
	    return;
	}

	int trash = setNodePosition(rootFig, horizontalMargin, verticalMargin);

	log.debug("Finished layout.");

    }

    private int setNodePosition(Fig fig, int nextColumn, int nextRow) {

	if(fig == null) {
	    return(nextRow);
	}

	// assume: jGraphFrame != null since it is checked in layout()

	JGraph jgraph = jGraphFrame.getGraph();
	if(jgraph == null) {
	    log.error("The JGraphFrame was not initialized properly since jgraph is null.");
	    return(nextRow);
	}
	Editor editor = jgraph.getEditor();
	if(editor == null) {
	    log.error("The JGraphFrame was not initialized properly since editor is null.");
	    return(nextRow);
	}

	fig.setX(nextColumn);
	fig.setY(nextRow);
	visitedFigSet.add(fig);

	editor.damaged(fig);

	log.debug("setting the location for fig: " + fig.getX() + ", " + fig.getY());

	nextColumn = fig.getX() + fig.getWidth() + horizontalMargin;
	nextRow = fig.getY();

	int lowestPoint = fig.getY() + fig.getHeight();

	// walk through all the children of this fig
	Object owner = fig.getOwner();
	if((owner != null) && (owner instanceof NetNode)) {
	    NetNode node = (NetNode)owner;
	    Vector ports = node.getPorts();
	    if(ports != null) {
		log.debug("ports.size() = " + ports.size());
		for(int i = 0; i < ports.size(); i++) {
		    NetPort port = (NetPort)ports.get(i);
		    Vector edges = port.getEdges();
		    if(edges != null) {
			log.debug("edges.size() = " + edges.size());
			for(int j = 0; j < edges.size(); j++) {
			    NetEdge edge = (NetEdge)edges.get(j);
			    NetPort destinationPort = edge.getSourcePort();
			    //NetPort destinationPort = edge.getDestPort();
			    NetNode childNode = destinationPort.getParentNode();
			    if((childNode != null) && (childNode instanceof ObjectNode)) {
				ObjectNode objectNode = (ObjectNode)childNode;
				Fig child = objectNode.getFigObjectNode();
				
				// check if we have already visited this fig
				if(!visitedFigSet.contains(child)) {
				    // we have a new fig to layout
				    lowestPoint = Math.max(lowestPoint, setNodePosition(child, nextColumn, nextRow));
				    nextRow = lowestPoint + verticalMargin;
				}
				else {
				    log.debug("child already visited: " + child);
				}
			    }
			    else {
				log.debug("childNode is null or not an ObjectNode.");
			    }
			}
		    }
		    else {
			log.debug("edges is null.");
		    }
		}
	    }
	    else {
		log.debug("ports is null.");
	    }
	}
	else {
	    log.debug("owner is null or is a NetNode.");
	}
	
	return(lowestPoint);
    }

    public JGraphFrame getJGraphFrame() {
	return(jGraphFrame);
    }
    public void setJGraphFrame(JGraphFrame jGraphFrame) {
	this.jGraphFrame = jGraphFrame;
    }

    public Fig getRootFig() {
	return(rootFig);
    }
    public void setRootFig(Fig rootFig) {
	this.rootFig = rootFig;
    }

    public void setVerticalMargin(int verticalMargin) {
	this.verticalMargin = verticalMargin;
    }
    public int getVerticalMargin() {
	return(verticalMargin);
    }

    public void setHorizontalMargin(int horizontalMargin) {
	this.horizontalMargin = horizontalMargin;
    }
    public int getHorizontalMargin() {
	return(horizontalMargin);
    }

}
