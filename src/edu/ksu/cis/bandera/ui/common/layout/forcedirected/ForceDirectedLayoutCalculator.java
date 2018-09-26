package edu.ksu.cis.bandera.ui.common.layout.forcedirected;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import edu.ksu.cis.bandera.ui.common.layout.Node;
import edu.ksu.cis.bandera.ui.common.layout.Edge;
import edu.ksu.cis.bandera.ui.common.layout.Layout;
import edu.ksu.cis.bandera.ui.common.layout.DefaultLayout;
import edu.ksu.cis.bandera.ui.common.layout.LayoutCalculator;

import java.awt.geom.Point2D;

/**
 * The ForceDirectedLayoutCalculator provides an easy way to
 * calculate a layout for a given graph (set of nodes and set of edges)
 * for use in displaying a graph visually.  It uses a Force Directed
 * Layout that was adapted from the OpenJGraph algorithm that
 * was adapted from a book entitled Graph Drawing ...
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public class ForceDirectedLayoutCalculator implements LayoutCalculator {

    protected double increment;
    protected double electricalRepulsion;
    protected double springLength;
    protected double stiffness;

    private static final double DEFAULT_INCREMENT = 0.50;
    private static final double DEFAULT_ELECTRICAL_REPULSION = 200.0;
    private static final double DEFAULT_SPRING_LENGTH = 30.0;
    private static final double DEFAULT_STIFFNESS = 30.0;
    private static final double MINIMUM_X_ADJUSTMENT = 0.75;
    private static final double MINIMUM_Y_ADJUSTMENT = 0.75;

    public ForceDirectedLayoutCalculator() {
	this(DEFAULT_INCREMENT, DEFAULT_ELECTRICAL_REPULSION, DEFAULT_SPRING_LENGTH, DEFAULT_STIFFNESS);
    }

    public ForceDirectedLayoutCalculator(double increment, double electricalReplusion, double springLength, double stiffness) {
	super();
	this.increment = increment;
	this.electricalRepulsion = electricalRepulsion;
	this.springLength = springLength;
	this.stiffness = stiffness;
    }

    /**
     * Calculate the proper layout given a set of nodes and edges.
     *
     * @return Layout The layout to use for this set of nodes and edges.
     * @param Set nodes The set of nodes that exist in the graph.
     * @param Set edges The set of edges that exist in the graph.
     */
    public Layout calculate(Set nodes, Set edges) {

	Layout layout = new DefaultLayout();
	Iterator ni = nodes.iterator();
	while(ni.hasNext()) {
	    layout.addNode((Node)ni.next());
	}
	Iterator ei = edges.iterator();
	while(ei.hasNext()) {
	    layout.addEdge((Edge)ei.next());
	}

	// adjust the nodes in the layout until it comes to an equilibrium
	boolean needsMoreAdjustment = true;
	while(needsMoreAdjustment) {
	    needsMoreAdjustment = false;
	    ni = nodes.iterator();
	    while(ni.hasNext()) {
		Node currentNode = (Node)ni.next();

		// we should probably calculate all adjacent nodes for each node before
		//  calling this method and we can then pass that information in instead
		//  of the nodes and edges:
		//  Adjustment adjustment = calculateAdjustment(currentNode, adjacentNodes);
		//  this will give a distinct speed advantage but might be problematic
		//  when it comes to space??? -tcw
		Adjustment adjustment = calculateAdjustment(currentNode, nodes, edges);

		if((Math.abs(adjustment.getX()) > MINIMUM_X_ADJUSTMENT) &&
		   (Math.abs(adjustment.getY()) > MINIMUM_Y_ADJUSTMENT)) {
		    // the adjustment is above the minimum movable value, move the node

		    int newX = (new Double(Math.ceil(currentNode.getX() + adjustment.getX())).intValue());
		    int newY = (new Double(Math.ceil(currentNode.getY() + adjustment.getY())).intValue());
		    currentNode.setX(newX);
		    currentNode.setY(newY);

		    // since one of the nodes moved a significant amount, we should re-calculate
		    // since it has not come to an equilibrium.
		    needsMoreAdjustment = true;
		}
	    }
	}

	return(layout);
    }

    /**
     *
     *
     * @param Node node The current node to adjust.
     * @param Set nodes The complete set of nodes in the graph.
     * @param Set edges The complete set of edges in the graph.
     */
    protected Adjustment calculateAdjustment(Node node, Set nodes, Set edges) {

	// create the set of all adjacent nodes for the current node
	Set adjacentNodes = new HashSet();
	Iterator ei = edges.iterator();
	while(ei.hasNext()) {
	    Edge currentEdge = (Edge)ei.next();

	    // determine if this edge is connected on either side to the current node
	    int sourceEdgeID = currentEdge.getSourceID();
	    int targetEdgeID = currentEdge.getTargetID();
	    int otherNodeID = -1;
	    if(sourceEdgeID == node.getID()) {
		otherNodeID = targetEdgeID;
	    }
	    else if(targetEdgeID == node.getID()) {
		otherNodeID = sourceEdgeID;
	    }
	    else {
		continue;
	    }

	    // get the other node in this edge and add it to the set of adjacent nodes
	    Iterator ni = nodes.iterator();
	    while(ni.hasNext()) {
		Node currentNode = (Node)ni.next();
		if((currentNode.getID() == otherNodeID) &&
		   (currentNode.getID() != node.getID())) {
		    adjacentNodes.add(currentNode);
		    break;
		}
	    }
	}

	int thisY = node.getY();
	int thisX = node.getX();
	double xSpring = 0.0;
	double ySpring = 0.0;
	double xRepulsion = 0.0;
	double yRepulsion = 0.0;
	Iterator ani = adjacentNodes.iterator();
	while(ani.hasNext()) {
	    Node currentNode = (Node)ani.next();

	    if(node.getID() == currentNode.getID()) {
		// same node ... no reason to move it
		continue;
	    }

	    int adjX = currentNode.getX();
	    int adjY = currentNode.getY();

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
	Adjustment adjustment = new Adjustment();
	adjustment.setX(xAdj);
	adjustment.setY(yAdj);
	return(adjustment);
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

    class Adjustment {

	public double x;
	public double y;

	public double getX() {
	    return(x);
	}

	public void setX(double x) {
	    this.x = x;
	}

	public double getY() {
	    return(y);
	}

	public void setY(double y) {
	    this.y = y;
	}
    }

}
