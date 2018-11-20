package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.SessionFileException;

/**
 * The FileNotWritableException is an exception that will be thrown
 * when a file cannot be written.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class FileNotWritableException extends SessionFileException {

    public FileNotWritableException() {
	super();
    }

    public FileNotWritableException(String message) {
	super(message);
    }

    public FileNotWritableException(Throwable t) {
	super(t);
    }

    public FileNotWritableException(String message, Throwable t) {
	super(message, t);
    }

}
