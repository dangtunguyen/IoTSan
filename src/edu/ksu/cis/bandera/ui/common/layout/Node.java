package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The Node interface provides the contract that will be valid
 * for every implementation of Node.  It provides a storage mechanism
 * for the height and width as well as the x and y location for a given
 * node.  Each node should also have a unique ID.  These will be
 * accessed via normal getter and setter methods.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface Node {

    public int getID();
    public int getHeight();
    public int getWidth();
    public int getX();
    public int getY();
    public Point getPoint();
    public Dimension getDimension();

    public void setID(int id);
    public void setHeight(int height);
    public void setWidth(int width);
    public void setX(int x);
    public void setY(int y);
    public void setPoint(Point point);
    public void setDimension(Dimension dimension);

}
