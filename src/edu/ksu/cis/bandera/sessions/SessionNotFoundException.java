package edu.ksu.cis.bandera.sessions;

/**
 * The SessionNotFoundException will provide a way to
 * throw an exception when a session is not found in the
 * SessionManager.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class SessionNotFoundException extends SessionException {

    public SessionNotFoundException() {
	super();
    }

    public SessionNotFoundException(String message) {
	super(message);
    }

}
