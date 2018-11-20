package edu.ksu.cis.bandera.sessions.parser;

import java.util.List;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * The AbstractParser provides a common set of methods and an interface
 * for each parser.  To create a new parser, just extend this
 * class and implement the load(File) and save(List, File, boolean)
 * methods.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public abstract class AbstractParser implements Parser {

    /**
     * Load a session file given a filename and return a List of Session objects
     * that represents the session information defined in the session file.
     *
     * @return List A list of Session objects that represents the session information
     *         stored in the session file given.
     * @param String filename The name of a session file.
     */
    public List load(String filename)
	throws FileNotFoundException, FileNotReadableException, NotAFileException, SessionFileException {

	if(filename == null) {
	    throw new FileNotFoundException("Loading a null file is not possible.");
	}

	File file = new File(filename);

	if(!file.exists()) {
	    throw new FileNotFoundException("The file given (" + filename + ") does not exist.");
	}

	if(!file.canRead()) {
	    throw new FileNotReadableException("The file given (" + filename + ") is not readable.");
	}

	if(!file.isFile()) {
	    throw new NotAFileException("The file given (" + filename + ") is not a file.");
	}

	List sessionList = load(file);

	return(sessionList);
    }

    /**
     * Load a session file given a file and return a List of Session objects
     * that represents the session information defined in the session file.
     *
     * @return List A list of Session objects that represents the session information
     *         stored in the session file given.
     * @param File file The session file.
     */
    public abstract List load(File file) throws SessionFileException, FileNotFoundException;

    /**
     * Save the session information given, in a List of Session objects, into
     * the file name given.
     */
    public void save(List sessionList, String filename) throws SessionFileException, FileNotFoundException {
	save(sessionList, filename, false);
    }

    /**
     *
     */
    public void save(List sessionList, File file)  throws SessionFileException, FileNotFoundException {
	save(sessionList, file, false);
    }

    /**
     *
     */
    public void save(List sessionList, String filename, boolean overwrite)
	throws FileNotFoundException, FileExistsException, NotAFileException, SessionFileException {

	if(filename == null) {
	    throw new FileNotFoundException("Cannot save session information to a null file.");
	}

	File file = new File(filename);

	if(!file.exists() && !overwrite) {
	    throw new FileExistsException("The file given (" + filename + ") exists and we are not set to overwrite it.");
	}

	if(!file.canWrite()) {
	    throw new NotAFileException("The file given (" + filename + ") is not writable.");
	}

	if(!file.isFile()) {
	    throw new NotAFileException("The file given (" + filename + ") is not a file.");
	}

	save(sessionList, file, overwrite);
    }

    /**
     *
     */
    public abstract void save(List sessionList, File file, boolean overwrite) throws SessionFileException, FileNotFoundException;


    /**
     *
     */
    public String getRelativePath(File base, File file) throws FileNotFoundException {

	if(base == null) {
	    System.out.println("The base directory is null so we will return the absolute path of the file given.");
	    try {
		return(file.getCanonicalPath());
	    }
	    catch(Exception e) {
		return(file.getAbsolutePath());
	    }
	}

	if(file == null) {
	    System.out.println("file is null.");
	    return("");
	}

	//System.out.println("base = " + base);
	//System.out.println("file = " + file);

	// if given two directories that are equal -> .
	if(base.equals(file)) {
	    return(".");
	}

	// are these two files in the same directory -> fileName
	if(base.equals(file.getParentFile())) {
	    return(file.getName());
	}
	// sometimes, the above test won't work so we will try it with canonical names.
	File tempFile = file;
	File tempBase = base;
	try {
	    tempFile = file.getCanonicalFile();
	    tempBase = base.getCanonicalFile();
	    if(tempBase.equals(tempFile.getParentFile())) {
		return(tempFile.getName());
	    }
	}
	catch(Exception e) {
	    // ignore this and move on.
	}
	
	// is the file in a sub directory of the base -> some\sub\dir\fileName
	try {
	    String relativePath = searchParentDirectory(base.getCanonicalFile(), file.getCanonicalFile());
	    if(relativePath != null) {
		return(relativePath);
	    }
	}
	catch(Exception e) {
	}

	// is the file in a parent directory of the base -> ..\..\fileName
	try {
	    String relativePath = searchSubDirectory(base.getCanonicalFile(), file.getCanonicalFile());
	    if(relativePath != null) {
		return(relativePath);
	    }
	}
	catch(Exception e) {
	}

	// is the file in a parent's sub directory of the base -> ..\..\some\sub\dir\fileName

	// try to get the canonical path
	try {
	    return(file.getCanonicalPath());
	}
	catch(Exception e) {
	    // it looks like all else has failed, just return the absolute path
	    return(file.getAbsolutePath());
	}

    }

    private String searchSubDirectory(File base, File file) throws Exception {

	if(base == null) {
	    throw new Exception("");
	}

	if(file == null) {
	    throw new Exception("");
	}

	//System.out.println("searchSubDirectory.base = " + base);
	//System.out.println("searchSubDirectory.file = " + file);

	if(base.equals(file.getParentFile())) {
	    return(file.getName());
	}

	return("..\\" + searchSubDirectory(base.getParentFile(), file));

    }

    private String searchParentDirectory(File base, File file) throws Exception {

	if(base == null) {
	    throw new Exception("");
	}

	if(file == null) {
	    throw new Exception("");
	}

	//System.out.println("searchParentDirectory.base = " + base);
	//System.out.println("searchParentDirectory.file = " + file);

	if(base.equals(file.getParentFile())) {
	    return(file.getName());
	}

	return(searchParentDirectory(base, file.getParentFile()) + "\\" + file.getName());

    }

    public File getFile(String filename, File base) throws FileNotFoundException {

	if((filename == null) || (filename.length() <= 0)) {
	    throw new FileNotFoundException("A file cannot be found that matches a null String.");
	}

	// if the filename is a root: for windows it will contain *:\
	if(filename.indexOf(":\\") >= 0) {
	    base = null;
	}

	// if the filename is a root: for unix it will start with a /
	if(filename.startsWith("/")) {
	    base = null;
	}

	// if the filename is a root: for windows network (WfW) //
	if(filename.indexOf("//") >= 0) {
	    base = null;
	}


	/*
	 * Note: We should also handle the loading of a relative Unix file on Windows:
	 *  ../../File.java
	 * and the loading of a relative Windows file on Unix:
	 *  ..\..\File.java
	 */


	File file = new File(base, filename);
	try {
	    file = file.getCanonicalFile();
	}
	catch(Exception e) {
	}

	return(file);
    }

}
