package edu.ksu.cis.bandera.sessions;

/**
 *
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public class DuplicateSessionException extends SessionException {

    public DuplicateSessionException() {
	super();
    }

    public DuplicateSessionException(String message) {
	super(message);
    }

}
