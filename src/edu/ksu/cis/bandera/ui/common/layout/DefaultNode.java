package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The DefaultNode provides a simple implementation of the
 * Node interface to provide storage of node information
 * including id, location (x and y), and dimension (height and width).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public class DefaultNode implements Node {

    protected int id;

    protected Point point;

    protected Dimension dimension;

    private static int nextID = 0;

    public DefaultNode() {
	this(nextID++, new DefaultPoint(), new DefaultDimension());
    }

    public DefaultNode(int id) {
	this(id, new DefaultPoint(), new DefaultDimension());
    }

    public DefaultNode(int id, int x, int y, int height, int width) {
	this(id, new DefaultPoint(x, y), new DefaultDimension(height, width));
    }

    public DefaultNode(int id, Point point, Dimension dimension) {
	super();
	setID(id);
	setPoint(point);
	setDimension(dimension);
    }

    public int getID() {
	return(id);
    }

    public int getHeight() {
	if(dimension == null) {
	    dimension = new DefaultDimension();
	}
	return(dimension.getHeight());
    }

    public int getWidth() {
	if(dimension == null) {
	    dimension = new DefaultDimension();
	}
	return(dimension.getWidth());
    }

    public int getX() {
	if(point == null) {
	    point = new DefaultPoint();
	}
	return(point.getX());
    }

    public int getY() {
	if(point == null) {
	    point = new DefaultPoint();
	}
	return(point.getY());
    }

    public Point getPoint() {
	return(point);
    }

    public Dimension getDimension() {
	return(dimension);
    }

    public void setID(int id) {
	this.id = id;
    }

    public void setHeight(int height) {
	if(dimension == null) {
	    dimension = new DefaultDimension();
	}
	dimension.setHeight(height);
    }

    public void setWidth(int width) {
	if(dimension == null) {
	    dimension = new DefaultDimension();
	}
	dimension.setWidth(width);
    }

    public void setX(int x) {
	if(point == null) {
	    point = new DefaultPoint();
	}
	point.setX(x);
    }

    public void setY(int y) {
	if(point == null) {
	    point = new DefaultPoint();
	}
	point.setY(y);
    }

    public void setPoint(Point point) {
	this.point = point;
    }

    public void setDimension(Dimension dimension) {
	this.dimension = dimension;
    }

}
