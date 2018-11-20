package edu.ksu.cis.bandera.ui.common.layout;

import edu.ksu.cis.bandera.ui.common.layout.LayoutEngine;

/**
 * The AbstractLayoutEngine provides an easy way for engine implementors
 * to implement layout algorithms by providing the starting and stopping
 * logic.  To use this class you should just extend it and implement the
 * layout method.  That method should perform the layout.  It is suggested
 * to make that method return rather quickly so that the user response
 * to the stopLayout will happen quickly.  So it is suggested that the
 * layout method performs a small subset of the whole layout on each call.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:44 $
 */
public abstract class AbstractLayoutEngine extends Thread implements LayoutEngine {

    protected boolean done = false;

    /**
     * The startLayout method will start the actual layout process.
     */
    public synchronized void startLayout() {
	notify();
    }

    /**
     * The stopLayout mtehod will stop the actual layout process.
     */
    //public synchronized void stopLayout() {  -> does this method need to be synchronized? -tcw
    public void stopLayout() {
	interrupt();
    }

    /**
     * Since this is a Thread, this method will be called by start() (that is
     * in the LayoutEngine interface).  It provides the running logic to
     * handle the start and stop calls properly and will call the layout
     * method at the proper time.
     */
    public void run() {

	while(!done) {

	    // wait until we are requested to actually run the layout algorithm
	    synchronized(this) {
		try {
		    wait();
		}
		catch(InterruptedException ie) {
		    // if this is interrupted, that means a request to quit the layout has
		    //  been sent (called the quit() method).  therefore, finish the loop
		    //  and finish the run method.
		    done = true;
		    break;
		}
	    }

	    // while we have not been told to stop (stopLayout()) and the layout
	    //  is not done, continue to call layout.
	    boolean finishedLayout = false;
	    while((!isInterrupted()) && (!finishedLayout)) {
		finishedLayout = layout();
	    }
	}

    }

    /**
     * The layout method needs to be implemented and it is expected
     * to return a boolean value that will indicate if layout has
     * completed.  When true is returned from this method, the layout
     * has come to some form of final state.
     *
     * @return boolen True if the layout engine is finished, false otherwise.
     */
    protected abstract boolean layout();

    /**
     * The quit method will signal to the Engine that it can stop all activity
     * and cleanup since it will no longer be used.
     */
    //public synchronized void quit() {  -> does this method need to be synchronized? -tcw
    public void quit() {
	done = true;
	interrupt();
    }

}
