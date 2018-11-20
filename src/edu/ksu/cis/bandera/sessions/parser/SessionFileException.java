package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.SessionException;

/**
 * The SessionFileException is a generic exception that can be thrown when
 * there are problems with the parsing of session file information.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class SessionFileException extends SessionException {

    public SessionFileException() {
	super();
    }

    public SessionFileException(String message) {
	super(message);
    }

    public SessionFileException(Throwable t) {
	super(t);
    }

    public SessionFileException(String message, Throwable t) {
	super(message, t);
    }

}
