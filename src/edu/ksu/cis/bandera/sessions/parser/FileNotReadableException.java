package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.SessionFileException;

/**
 * The FileNotReadableException is an exception that will be thrown
 * when a file cannot be read.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class FileNotReadableException extends SessionFileException {

    public FileNotReadableException() {
	super();
    }

    public FileNotReadableException(String message) {
	super(message);
    }

    public FileNotReadableException(Throwable t) {
	super(t);
    }

    public FileNotReadableException(String message, Throwable t) {
	super(message, t);
    }

}
