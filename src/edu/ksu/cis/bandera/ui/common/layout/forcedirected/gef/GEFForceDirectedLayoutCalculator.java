package edu.ksu.cis.bandera.ui.common.layout.forcedirected.gef;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import edu.ksu.cis.bandera.ui.common.layout.Node;
import edu.ksu.cis.bandera.ui.common.layout.Edge;
import edu.ksu.cis.bandera.ui.common.layout.Layout;
import edu.ksu.cis.bandera.ui.common.layout.Adjustment;
import edu.ksu.cis.bandera.ui.common.layout.LayoutCalculator;

import java.awt.geom.Point2D;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigEdge;

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
public class GEFForceDirectedLayoutCalculator implements LayoutCalculator {

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

    public GEFForceDirectedLayoutCalculator() {
	this(DEFAULT_INCREMENT, DEFAULT_ELECTRICAL_REPULSION, DEFAULT_SPRING_LENGTH, DEFAULT_STIFFNESS);
    }

    public GEFForceDirectedLayoutCalculator(double increment, double electricalReplusion,
					    double springLength, double stiffness) {
	super();
	this.increment = increment;
	this.electricalRepulsion = electricalRepulsion;
	this.springLength = springLength;
	this.stiffness = stiffness;
    }

    /**
     * Calculate the proper layout given a set of nodes and edges.
     *
     * @return Layout The layout to use for this set of nodes and edges.  This is meaningless!
     * @param Set nodes The set of nodes that exist in the graph.  This should be a set of FigNodes.
     * @param Set edges The set of edges that exist in the graph.  These are ignored!
     */
    public Layout calculate(Set nodes, Set edges) {

	// calculate all the adjacent figs mapping
	Map adjacentFigNodesMap = new HashMap();
	Iterator ni = nodes.iterator();
	while(ni.hasNext()) {
	    FigNode currentFigNode = (FigNode)ni.next();
	    Set adjacentFigNodes = new HashSet();
	    Vector figEdges = currentFigNode.getFigEdges();
	    for(int i = 0; i < figEdges.size(); i++) {
		FigEdge currentFigEdge = (FigEdge)figEdges.get(i);
		Fig destinationFig = currentFigEdge.getDestFigNode();
		Fig sourceFig = currentFigEdge.getSourceFigNode();
		if(sourceFig.equals(currentFigNode)) {
		    adjacentFigNodes.add(destinationFig);
		}
		else if(destinationFig.equals(currentFigNode)) {
		    adjacentFigNodes.add(sourceFig);
		}
		else {
		    // this is weird!
		}
	    }

	    adjacentFigNodesMap.put(currentFigNode, adjacentFigNodes);
	}

	// adjust the nodes in the layout until it comes to an equilibrium
	boolean needsMoreAdjustment = true;
	while(needsMoreAdjustment) {
	    needsMoreAdjustment = false;
	    ni = nodes.iterator();
	    while(ni.hasNext()) {
		FigNode currentFigNode = (FigNode)ni.next();

		Adjustment adjustment = calculateAdjustment(currentFigNode, (Set)adjacentFigNodesMap.get(currentFigNode));

		if((Math.abs(adjustment.getX()) > MINIMUM_X_ADJUSTMENT) &&
		   (Math.abs(adjustment.getY()) > MINIMUM_Y_ADJUSTMENT)) {
		    // the adjustment is above the minimum movable value, move the node

		    int newX = (new Double(Math.ceil(currentFigNode.getX() + adjustment.getX())).intValue());
		    int newY = (new Double(Math.ceil(currentFigNode.getY() + adjustment.getY())).intValue());
		    currentFigNode.setX(newX);
		    currentFigNode.setY(newY);

		    // since one of the nodes moved a significant amount, we should re-calculate
		    // since it has not come to an equilibrium.
		    needsMoreAdjustment = true;
		}
	    }
	}

	return(null);
    }

    /**
     *
     */
    protected Adjustment calculateAdjustment(FigNode figNode, Set adjacentFigNodes) {

	int thisY = figNode.getY();
	int thisX = figNode.getX();
	double xSpring = 0.0;
	double ySpring = 0.0;
	double xRepulsion = 0.0;
	double yRepulsion = 0.0;
	Iterator afni = adjacentFigNodes.iterator();
	while(afni.hasNext()) {
	    FigNode currentFigNode = (FigNode)afni.next();

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

}
