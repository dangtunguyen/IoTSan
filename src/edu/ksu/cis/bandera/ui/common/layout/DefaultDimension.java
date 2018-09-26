package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The DefaultDimension class implements the Dimension interface 
 * to provide a simple implemention for the storage of the size of
 * a component in an X-Y coordinate system.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:44 $
 */
public class DefaultDimension implements Dimension {

    protected int height;

    protected int width;

    public static final int DEFAULT_HEIGHT = 0;

    public static final int DEFAULT_WIDTH = 0;

    public DefaultDimension() {
	this(DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    public DefaultDimension(int height, int width) {
	super();
	setHeight(height);
	setWidth(width);
    }

    public int getHeight() {
	return(height);
    }

    public int getWidth() {
	return(width);
    }

    public void setHeight(int height) {
	this.height = height;
    }

    public void setWidth(int width) {
	this.width = width;
    }

}
