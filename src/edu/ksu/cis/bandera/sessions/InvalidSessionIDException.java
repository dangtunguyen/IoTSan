package edu.ksu.cis.bandera.sessions;

/**
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public class InvalidSessionIDException extends SessionException {

    public InvalidSessionIDException() {
	super();
    }

    public InvalidSessionIDException(String message) {
	super(message);
    }

}
