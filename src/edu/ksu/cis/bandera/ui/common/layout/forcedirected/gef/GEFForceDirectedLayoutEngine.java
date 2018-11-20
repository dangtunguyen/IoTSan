package edu.ksu.cis.bandera.ui.common.layout.forcedirected.gef;

import edu.ksu.cis.bandera.ui.common.layout.AbstractLayoutEngine;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;
import java.util.Iterator;

import org.tigris.gef.base.Editor;

import org.tigris.gef.graph.GraphModel;

import org.tigris.gef.graph.presentation.NetNode;
import org.tigris.gef.graph.presentation.NetEdge;
import org.tigris.gef.graph.presentation.NetPort;
import org.tigris.gef.graph.presentation.JGraphFrame;
import org.tigris.gef.graph.presentation.JGraph;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.FigObjectNode;
import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef.ObjectNode;

import java.awt.Rectangle;

import java.awt.geom.Point2D;

import edu.ksu.cis.bandera.ui.common.layout.Adjustment;

import org.apache.log4j.Category;

/**
 * The GEFForceDirectedLayoutEngine will perform the layout of a GEF graph
 * incrementally.  It will choose a subset of nodes to modify on each call to
 * layout.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public class GEFForceDirectedLayoutEngine extends AbstractLayoutEngine {


    /*
     * Notes:
     * - Implement logic to quit the layout ... find the equilibrium
     * - Do we need to use FigNode?  Or will Fig do?
     * - Will it be faster to calculate the adjacent edges ahead of time?
     * - The algorithm should use the port x,y instead of the node x,y when calculating distance.???
     */


    private JGraphFrame jGraphFrame;

    private List figList;

    private int nextFigIndex;

    // implement this to save time when performing the calculation.
    //private Map adjacentFigMap;

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(GEFForceDirectedLayoutEngine.class);

    protected double increment;
    protected double electricalRepulsion;
    protected double springLength;
    protected double stiffness;

    // should we make max nodes per layout changable at runtime? -tcw
    // should we make min x and y adjustments changable at runtime? -tcw

    private static final double DEFAULT_INCREMENT = 0.50;
    private static final double DEFAULT_ELECTRICAL_REPULSION = 50.0;
    private static final double DEFAULT_SPRING_LENGTH = 200.0;
    private static final double DEFAULT_STIFFNESS = 30.0;

    private static final double MINIMUM_X_ADJUSTMENT = 0.75;
    private static final double MINIMUM_Y_ADJUSTMENT = 0.75;
    private static final int MAX_NODES_PER_LAYOUT = 30;

    public GEFForceDirectedLayoutEngine() {
	this(null);
    }

    public GEFForceDirectedLayoutEngine(JGraphFrame jGraphFrame) {
	this(jGraphFrame, DEFAULT_INCREMENT, DEFAULT_ELECTRICAL_REPULSION,
	     DEFAULT_SPRING_LENGTH, DEFAULT_STIFFNESS);
    }

    public GEFForceDirectedLayoutEngine(JGraphFrame jGraphFrame,
					    double increment,
					    double electricalReplusion,
					    double springLength,
					    double stiffness) {
	super();
	setJGraphFrame(jGraphFrame);
	setIncrement(increment);
	setElectricalRepulsion(electricalRepulsion);
	setSpringLength(springLength);
	setStiffness(stiffness);
    }

    protected boolean layout() {

	JGraph jgraph = jGraphFrame.getGraph();
	if(jgraph == null) {
	    log.error("The JGraphFrame was not initialized properly since jgraph is null.");
	    return(true);
	}
	Editor editor = jgraph.getEditor();
	if(editor == null) {
	    log.error("The JGraphFrame was not initialized properly since editor is null.");
	    return(true);
	}

	updateNodes();
	for(int i = 0; i < MAX_NODES_PER_LAYOUT; i++) {

	    log.debug("laying out node " + i + " in this cycle.");

	    Fig currentFig = getNextFig();
	    if(!(currentFig instanceof FigNode)) {
		log.debug("Skipping the current fig since it isn't a node.");
		continue;
	    }

	    FigNode currentFigNode = (FigNode)currentFig;

	    Adjustment adjustment = calculateAdjustment(currentFigNode);
	    //Adjustment adjustment = calculateAdjustment(currentFigNode, (Set)adjacentFigMap.get(currentFigNode));

	    log.debug("Adjustment calculation = (" + adjustment.getX() + ", " + adjustment.getY() + ").");
		
	    if((Math.abs(adjustment.getX()) > MINIMUM_X_ADJUSTMENT) ||
	       (Math.abs(adjustment.getY()) > MINIMUM_Y_ADJUSTMENT)) {
		// the adjustment is above the minimum movable value, move the node

		int newX = (new Double(Math.ceil(currentFigNode.getX() + adjustment.getX())).intValue());
		int newY = (new Double(Math.ceil(currentFigNode.getY() + adjustment.getY())).intValue());
		currentFigNode.setX(newX);
		currentFigNode.setY(newY);

		editor.damaged(currentFig);

		log.debug("Done adjusting current node.");
	    }
	}

	// determine if we are done with the layout process: if we have gone through the whole
	//  list and none of the figs have had to be adjusted more than the min.

	return(false);
    }

    /**
     * Update the collection of nodes and the pointer to the next node.  This will
     * re-query the graph model and update if necessary.  We currently use an optimistic
     * assumption that if the graph model node count doesn't change, we don't need to
     * update.
     */
    private void updateNodes() {

	if(jGraphFrame == null) {
	    // this should throw an exception! -tcw
	    log.error("The " + this.getClass().getName() + " was not initialized properly.  jGraphFrame is null.");
	    return;
	}

	GraphModel graphModel = jGraphFrame.getGraphModel();
	Vector nodes = graphModel.getNodes();
	if(figList == null) {
	    figList = generateFigList(nodes);
	    nextFigIndex = 0;
	}
	else if(nodes.size() != figList.size()) {
	    figList.clear();
	    figList = generateFigList(nodes);

	    if(nextFigIndex > figList.size()) {
		nextFigIndex = 0;
	    }
	}
	else {
	    // this probably means the graph model has not changed so don't update
	    //  our list of nodes! -tcw
	}

    }

    /**
     * Get the next fig in the collection and wrap the index if necessary.
     *
     * @return Fig The next fig in our collection.
     */
    private Fig getNextFig() {

	log.debug("The next node is at location " + nextFigIndex);
	Fig nextFig = (Fig)figList.get(nextFigIndex);

	if(nextFigIndex == figList.size() - 1) {
	    nextFigIndex = 0;
	}
	else {
	    nextFigIndex++;
	}

	return(nextFig);
    }

    /**
     * Calculate the adjustment to make to this fig to move it towards equilibrium.
     *
     * @param Fig fig The fig to calculate the adjustment for.
     * @return Adjustment The adjustment (in both x and y) to make to the given fig's location.
     */
    private Adjustment calculateAdjustment(Fig fig) {

	if(fig == null) {
	    log.warn("Cannot make any adjustment to a null fig.");
	    return(new Adjustment());
	}

	Adjustment adjustment = null;
	Object owner = fig.getOwner();
	if((owner != null) && (owner instanceof ObjectNode)) {
	    Set adjacentFigs = generateAdjacentFigSet((ObjectNode)owner);
	    adjustment = calculateAdjustment(fig, adjacentFigs);
	}
	else {
	    log.warn("No sure how to calculate using a " + owner.getClass().getName() + " as the parent of the fig.");
	    adjustment = new Adjustment();
	}

	return(adjustment);
    }

    /**
     * Calculate the adjustment to make to this fig to move it towards equilibrium given
     * the adjacent figs.
     *
     * @param Fig fig The fig to calculate the adjustment for.
     * @param Set adjacentFigs A set of all adjacent figs in the graph.
     * @return Adjustment The adjustment (in both x and y) to make to the given fig's location.
     */
    private Adjustment calculateAdjustment(Fig fig, Set adjacentFigs) {

	if(fig == null) {
	    log.warn("Cannot make any adjustment to a null fig.");
	    return(new Adjustment());
	}

	if((adjacentFigs == null) || (adjacentFigs.size() <= 0)) {
	    log.debug("No need to make adjustments to a fig without any adjacent nodes.");
	    return(new Adjustment());
	}

	if(!(fig instanceof FigNode)) {
	    log.info("No need to make adjustments to a fig that isn't a FigNode.");
	    return(new Adjustment());
	}

	FigNode figNode = (FigNode)fig;

	Adjustment adjustment = new Adjustment();

	// now do the real calculation!
	int thisY = figNode.getY();
	int thisX = figNode.getX();
	double xSpring = 0.0;
	double ySpring = 0.0;
	double xRepulsion = 0.0;
	double yRepulsion = 0.0;
	Iterator afi = adjacentFigs.iterator();
	while(afi.hasNext()) {
	    Fig currentFig = (Fig)afi.next();

	    if(!(currentFig instanceof FigNode)) {
		log.info("We are skipping the adjustment for a fig that isn't a FigNode.");
		continue;
	    }

	    FigNode currentFigNode = (FigNode)currentFig;

	    if(figNode.equals(currentFigNode)) {
		// same fig node ... no reason to move it
		continue;
	    }

	    int adjX = currentFigNode.getX();
	    int adjY = currentFigNode.getY();

	    double distance = Point2D.distance(thisX, thisY, adjX, adjY);
	    if(distance == 0) {
		// a little hack to get around the divide by zero problems! -tcw
		distance = .00001;
	    }

	    xSpring += stiffness * Math.log(distance / springLength) * ((thisX - adjX) / (distance));
	    ySpring += stiffness * Math.log(distance / springLength) * ((thisY - adjY) / (distance));
	    xRepulsion += (electricalRepulsion / distance) * ((thisX - adjX) / (distance));
	    yRepulsion += (electricalRepulsion / distance) * ((thisY - adjY) / (distance));
	}

	double xForce = xSpring - xRepulsion;
	double yForce = ySpring - yRepulsion;
	double xAdj = 0 - (xForce - increment);
	double yAdj = 0 - (yForce - increment);
	adjustment.setX(xAdj);
	adjustment.setY(yAdj);
	
	return(adjustment);
    }

    /**
     * Given a vector of nodes, generate a list of Figs.
     *
     * @param Vector nodes A vector of nodes in a graph.
     * @return List The list of figs for each node in the graph.
     */
    private static List generateFigList(Vector nodes) {

	if(nodes == null) {
	    log.warn("Cannot generate a fig list from a null node vector.");
	    return(new ArrayList(0));
	}

	List figList = new ArrayList(nodes.size());
	for(int i = 0; i < nodes.size(); i++) {
	    // should we test the next node to make sure it is an ObjectNode???
	    ObjectNode objectNode = (ObjectNode)nodes.get(i);
	    FigObjectNode figObjectNode = objectNode.getFigObjectNode();
	    figList.add(figObjectNode);
	}

	return(figList);
    }

    /**
     * Generate a mapping from each fig to it's adjacent figs.
     *
     * @param Vector nodes The nodes in the graph.
     * @return Map A mapping from a Fig to a Set of Figs that are adjacent in the graph
     *         to the key Fig.
     */
    private static Map generateAdjacentFigMap(Vector nodes) {

	if(nodes == null) {
	    log.warn("Cannot generate an adjacent fig map from a null vector.");
	    return(new HashMap(0));
	}

	if(nodes.size() <= 0) {
	    log.warn("Cannot generate an adjacent fig map from an empty vector.");
	    return(new HashMap(0));
	}

	Map adjacentFigMap = new HashMap(nodes.size());
	for(int i = 0; i < nodes.size(); i++) {
	    ObjectNode objectNode = (ObjectNode)nodes.get(i);
	    Set adjacentFigSet = generateAdjacentFigSet(objectNode);
	    FigObjectNode fig = objectNode.getFigObjectNode();
	    adjacentFigMap.put(fig, adjacentFigSet);
	}

	return(adjacentFigMap);
    }

    private static Set generateAdjacentFigSet(ObjectNode node) {

	if(node == null) {
	    log.warn("Cannot generate an adjacent fig set for a null node.");
	    return(new HashSet(0));
	}

	Set adjacentFigSet = new HashSet();
	Vector ports = node.getPorts();
	if(ports != null) {
	    for(int i = 0; i < ports.size(); i++) {
		NetPort port = (NetPort)ports.get(i);
		Vector edges = port.getEdges();
		if(edges != null) {
		    for(int j = 0; j < edges.size(); j++) {
			NetEdge edge = (NetEdge)edges.get(j);
			NetPort destinationPort = edge.getDestPort();
			NetNode destinationNode = destinationPort.getParentNode();
			if((destinationNode != null) && (destinationNode instanceof ObjectNode)) {
			    ObjectNode adjacentNode = (ObjectNode)destinationNode;
			    Fig fig = adjacentNode.getFigObjectNode();
			    adjacentFigSet.add(fig);
			}
		    }
		}
	    }
	}

	// make sure our own fig isn't in the set since we won't
	adjacentFigSet.remove(node.getFigObjectNode());

	return(adjacentFigSet);

    }

    public void setJGraphFrame(JGraphFrame jGraphFrame) {
	this.jGraphFrame = jGraphFrame;
    }

    public JGraphFrame getJGraphFrame() {
	return(jGraphFrame);
    }

    public void setIncrement(double increment) {
	this.increment = increment;
    }
    public double getIncrement() {
	return(increment);
    }

    public void setStiffness(double stiffness) {
	this.stiffness = stiffness;
    }
    public double getStiffness() {
	return(stiffness);
    }

    public void setSpringLength(double springLength) {
	this.springLength = springLength;
    }
    public double getSpringLength() {
	return(springLength);
    }

    public void setElectricalRepulsion(double electricalRepulsion) {
	this.electricalRepulsion = electricalRepulsion;
    }
    public double getElectricalRepulsion() {
	return(electricalRepulsion);
    }

}
