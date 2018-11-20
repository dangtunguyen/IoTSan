package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.SessionFileException;

/**
 * The NotAFileException is an exception that will be thrown
 * when the file given is not a file (usually a directory).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class NotAFileException extends SessionFileException {

    public NotAFileException() {
	super();
    }

    public NotAFileException(String message) {
	super(message);
    }

    public NotAFileException(Throwable t) {
	super(t);
    }

    public NotAFileException(String message, Throwable t) {
	super(message, t);
    }

}
