package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The Dimension interface provides a storage mechanism for
 * the definition of the size of a component in an X-Y coordinate
 * system.  This will be used to describe how much space a
 * node takes up in the graph.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface Dimension {

    public int getHeight();
    public int getWidth();

    public void setHeight(int height);
    public void setWidth(int width);

}
