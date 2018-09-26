package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.SessionFileException;

/**
 * The FileExistsException is an exception that will be thrown
 * when a file is about to be overwritten (file already exists
 * and we are about to write over it's data).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class FileExistsException extends SessionFileException {

    public FileExistsException() {
	super();
    }

    public FileExistsException(String message) {
	super(message);
    }

    public FileExistsException(Throwable t) {
	super(t);
    }

    public FileExistsException(String message, Throwable t) {
	super(message, t);
    }

}
