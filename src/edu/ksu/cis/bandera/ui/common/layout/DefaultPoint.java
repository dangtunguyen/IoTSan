package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The DefaultPoint class implements the Point interface to
 * provide a location in an X-Y coordinate system.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public class DefaultPoint implements Point {

    protected int x;

    protected int y;

    private static final int DEFAULT_X = 0;

    private static final int DEFAULT_Y = 0;

    public DefaultPoint() {
	this(DEFAULT_X, DEFAULT_Y);
    }

    public DefaultPoint(int x, int y) {
	super();
	setX(x);
	setY(y);
    }

    public int getX() {
	return(x);
    }

    public int getY() {
	return(y);
    }

    public void setX(int x) {
	this.x = x;
    }

    public void setY(int y) {
	this.y = y;
    }

}
