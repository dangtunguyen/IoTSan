package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.SessionFileFormatException;

/**
 * The OldSessionFileFormatException is an exception that will be thrown
 * when a file is expecting one format and gets an older format instead.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class OldSessionFileFormatException extends SessionFileFormatException {

    public OldSessionFileFormatException() {
	super();
    }

    public OldSessionFileFormatException(String message) {
	super(message);
    }

    public OldSessionFileFormatException(Throwable t) {
	super(t);
    }

    public OldSessionFileFormatException(String message, Throwable t) {
	super(message, t);
    }

}
