package edu.ksu.cis.bandera.sessions;

/**
 * The SessionListener provides a way to listen for changes to Session
 * objects so that the model will be able to keep track if the file needs
 * to be saved to disk.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public interface SessionListener {

    /**
     * This method will handle the case when a session's information
     * has been modified.  Most likely, this will mean that the view should
     * be updated and that the session information should be saved out to
     * a file again before being lost.
     */
    public void modified();
}
