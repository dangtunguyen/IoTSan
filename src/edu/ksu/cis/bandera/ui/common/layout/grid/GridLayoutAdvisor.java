package edu.ksu.cis.bandera.ui.common.layout.grid;

import edu.ksu.cis.bandera.ui.common.layout.LayoutAdvisor;
import edu.ksu.cis.bandera.ui.common.layout.Node;
import edu.ksu.cis.bandera.ui.common.layout.Edge;
import edu.ksu.cis.bandera.ui.common.layout.Point;
import edu.ksu.cis.bandera.ui.common.layout.Route;
import edu.ksu.cis.bandera.ui.common.layout.DefaultPoint;

import java.util.Map;
import java.util.HashMap;

/**
 * The GridLayoutAdvisor provides an interactive layout manager that will
 * assist the user in placing nodes and edges in the view of the graph.  It
 * will take nodes and suggest the placement of the node in the graph.  The
 * user must take the suggestion or the algorithm will fail.  This implements a
 * simple grid layout algorithm where nodes are layed out from left to right
 * and wrapped when the right edge of the graph is found.  It takes height and width
 * as well as some margins (or padding) for each node in the graph: horizontal and vertical.
 *
 * @author Todd Wallentine &gt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:46 $
 */
public class GridLayoutAdvisor implements LayoutAdvisor {

    /*
     * Notes:
     * - What do we do with nodes that have no height and/or width?
     */
    
    protected int horizontalNodeMargin;
    protected int verticalNodeMargin;
    protected int width;
    protected int height;

    protected int nextRow;

    protected Point nextPoint;

    protected Map nodeMap;

    private static final int DEFAULT_HEIGHT = 1000;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HORIZONTAL_NODE_MARGIN = 10;
    private static final int DEFAULT_VERTICAL_NODE_MARGIN = 10;

    public GridLayoutAdvisor() {
	this(DEFAULT_HORIZONTAL_NODE_MARGIN, DEFAULT_VERTICAL_NODE_MARGIN, DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    public GridLayoutAdvisor(int height, int width) {
	this(DEFAULT_HORIZONTAL_NODE_MARGIN, DEFAULT_VERTICAL_NODE_MARGIN, height, width);
    }

    public GridLayoutAdvisor(int horizontalNodeMargin, int verticalNodeMargin, int height, int width) {
	this.horizontalNodeMargin = horizontalNodeMargin;
	this.verticalNodeMargin = verticalNodeMargin;
	this.height = height;
	this.width = width;

	nodeMap = new HashMap();

	// generate the nextPoint and nextRow
	nextPoint = new DefaultPoint(horizontalNodeMargin, verticalNodeMargin);
	nextRow = verticalNodeMargin;
    }

    /**
     * Get the next location in which to place the given node.  This will
     * use a simple grid using the margins and bounds given.
     *
     * @return Point The location to place the given node in the graph.
     * @param Node The node to place in the graph.
     */
    public Point nextNodeLocation(Node node) {

	Point point = null;

	// make sure the node isn't too wide to fit into the graph
	if(node.getWidth() + (horizontalNodeMargin * 2) > width) {
	    throw new RuntimeException("The node is too wide to fit in the graph.");
	}

	// make sure the node isn't too tall to fit into the graph
	if(node.getHeight() + (verticalNodeMargin * 2) > height) {
	    throw new RuntimeException("The node is too tall to fit in the graph.");
	}

	// check to see if we need to wrap this node into the next row
	if(nextPoint.getX() + verticalNodeMargin + node.getWidth() > width) {
	    // we need to wrap the layout around to the next row
	    point = new DefaultPoint(horizontalNodeMargin, nextRow);

	    // determine the next point
	    if(point.getX() + horizontalNodeMargin + node.getWidth() < width) {
		// there is enough room for another node in this row
		nextPoint = new DefaultPoint(point.getX() + horizontalNodeMargin + node.getWidth(), point.getY());
		nextRow = point.getY() + node.getHeight() + verticalNodeMargin;
	    }
	    else {
		nextPoint = new DefaultPoint(horizontalNodeMargin, point.getY() + node.getHeight() + verticalNodeMargin);
		nextRow = nextPoint.getY() + verticalNodeMargin;
	    }
	}
	else {
	    // we can use the current nextPoint as the point to place the current node
	    point = nextPoint;

	    nextRow = Math.max(nextRow, point.getY() + node.getHeight() + verticalNodeMargin);

	    // determine the next point
	    if((point.getX() + horizontalNodeMargin + node.getWidth()) >= width) {
		// wrap to the next line
		nextPoint = new DefaultPoint(horizontalNodeMargin, nextRow);
		nextRow += verticalNodeMargin;
	    }
	    else {
		nextPoint = new DefaultPoint(point.getX() + horizontalNodeMargin + node.getWidth(), point.getY());
	    }
	}

	// clone the node before we put it into our collection
	node.setPoint(point);
	nodeMap.put(new Integer(node.getID()), node);

	return(point);
    }

    /**
     * Get a route in which to draw the given edge.  This will just return the
     * route that is already in the edge since this algorithm doesn't care
     * about where it goes ... in other words, it will just use a straight
     * line from source to target.
     *
     * @return Route The route for the edge in the graph.
     * @param Edge The edge to find routing information for.
     */
    public Route nextEdgeLocation(Edge edge) {
	return(edge.getRoute());
    }

}
