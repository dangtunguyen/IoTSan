package edu.ksu.cis.bandera.sessions.parser;

import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public interface Parser {

    /**
     * Load a session file given a filename and return a List of Session objects
     * that represents the session information defined in the session file.
     *
     * @return List A list of Session objects that represents the session information
     *         stored in the session file given.
     * @param String filename The name of a session file.
     */
    public List load(String filename) throws SessionFileException, FileNotFoundException;

    /**
     * Load a session file given a file and return a List of Session objects
     * that represents the session information defined in the session file.
     *
     * @return List A list of Session objects that represents the session information
     *         stored in the session file given.
     * @param File file The session file.
     */
    public List load(File file) throws SessionFileException, FileNotFoundException;

    /**
     * Save the session information given, in a List of Session objects, into
     * the file name given.
     */
    public void save(List sessionList, String filename) throws SessionFileException, FileNotFoundException;

    /**
     *
     */
    public void save(List sessionList, File file) throws SessionFileException, FileNotFoundException;

    /**
     *
     */
    public void save(List sessionList, String filename, boolean overwrite) throws SessionFileException, FileNotFoundException;

    /**
     *
     */
    public void save(List sessionList, File file, boolean overwrite) throws SessionFileException, FileNotFoundException;

    /**
     *
     */
    public String getRelativePath(File base, File file) throws FileNotFoundException;

    /**
     *
     */
    public File getFile(String filename, File base) throws FileNotFoundException;
}
