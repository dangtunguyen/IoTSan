package edu.ksu.cis.bandera.sessions;

import java.lang.Exception;

/**
 * The SessionException is a generic exception used when
 * dealing with session information.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class SessionException extends Exception {

    public SessionException() {
	super();
    }

    public SessionException(String message) {
	super(message);
    }

    public SessionException(Throwable t) {
	super(t.toString());
    }

    public SessionException(String message, Throwable t) {
	super(message + " " + t.toString());
    }

}
