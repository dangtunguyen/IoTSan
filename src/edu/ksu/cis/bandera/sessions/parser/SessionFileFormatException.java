package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.SessionFileException;

/**
 * The SessionFileFormatException is an exception that will be thrown
 * when a file format is not understood.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class SessionFileFormatException extends SessionFileException {

    public SessionFileFormatException() {
	super();
    }

    public SessionFileFormatException(String message) {
	super(message);
    }

    public SessionFileFormatException(Throwable t) {
	super(t);
    }

    public SessionFileFormatException(String message, Throwable t) {
	super(message, t);
    }

}
