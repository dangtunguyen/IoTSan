package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The Point interface provides a storage mechanism for
 * the definition of a location in an X-Y coordinate system.
 * This is used to define locations in for nodes in a graph.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface Point {

    public int getX();
    public int getY();

    public void setX(int x);
    public void setY(int y);

}
