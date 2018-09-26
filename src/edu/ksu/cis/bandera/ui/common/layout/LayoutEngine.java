package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The LayoutEngine provides an active layout mechanism that
 * will incrementally perform layout behind the scenes.  It
 * can be started and stopped at any time.  The normal usage model
 * for a LayoutEngine is this:
 * <pre>
 * ...
 * LayoutEngine le;  // create a layout engine
 * le.start();
 * ...
 * le.startLayout();  // when the layout process should start
 * ...
 * le. stopLayout(); // when the layout process should stop
 * ...
 * le.quit(); // when the layout process is completely finished.
 * ...
 * </pre>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface LayoutEngine {

    /**
     * The start method will perform any startup procedure.  Most
     * likely this will be implemented by Thread.
     */
    public void start();

    /**
     * The startLayout method will start the actual layout process.
     */
    public void startLayout();

    /**
     * The stopLayout mtehod will stop the actual layout process.
     */
    public void stopLayout();

    /**
     * The quit method will signal to the Engine that it can stop all activity
     * and cleanup since it will no longer be used.
     */
    public void quit();

}
