package edu.ksu.cis.bandera.sessions;

import edu.ksu.cis.bandera.sessions.parser.Parser;
import edu.ksu.cis.bandera.sessions.parser.ParserFactory;
import edu.ksu.cis.bandera.sessions.parser.SessionFileException;
import edu.ksu.cis.bandera.sessions.parser.SessionFileFormatException;
import edu.ksu.cis.bandera.sessions.parser.OldSessionFileFormatException;
import edu.ksu.cis.bandera.sessions.parser.FileExistsException;
import edu.ksu.cis.bandera.sessions.parser.FileNotWritableException;
import edu.ksu.cis.bandera.sessions.parser.FileNotReadableException;
import edu.ksu.cis.bandera.sessions.parser.NotAFileException;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionException;
import edu.ksu.cis.bandera.sessions.DuplicateSessionException;
import edu.ksu.cis.bandera.sessions.SessionNotFoundException;
import edu.ksu.cis.bandera.sessions.InvalidSessionIDException;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Category;

/**
 * The SessionManager provides a consist model for dealing with session information.  If a
 * shared instance is needed (as it is now in the current Bandera system), you can call getInstance().
 * The SessionManager manages the currently loaded session information.  At any one time, only
 * one session may be active.  However, the SessionManager can hold several sessions.  The SessionManager
 * also provides the logic to load the store session information to/from files.
 *
 * [Note: Adding a session and then loading a session file will merge the two with the existing sessions
 * taking precedence.  If this behaviour is not what you want, clear the existing sessions before loading.]
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/06/17 16:05:24 $
 */
public final class SessionManager extends Observable implements Observer {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(SessionManager.class.getName());

    /**
     * At any one time, only a single set of sessions will be visible.  These will
     * all be stored in this sessionFile.
     */
    private File sessionFile;

    /**
     * When using the singleton pattern, we will keep a shared, static instance of the
     * session manager.  To get this shared instance, a user must call getInstance().
     */
    private static SessionManager sessionManager;

    /**
     * The session file will be loaded and stored using a Parser.  This parser will be
     * retrieved using the ParserFactory but a current instance of the Parser will be stored
     * in this object to make it faster.
     */
    private Parser parser;

    /**
     * The active session.
     */
    private Session activeSession;
    
    /**
     * The current store of visible sessions with the key's being the name of the session (String)
     * and the values being the Session object.
     */
    private Map sessions;

    /**
     * Create an instance of the session manager.  This is private so that we can manage the creation
     * of them through the create/getInstance methods.
     */
    private SessionManager() {

	/* do not set the sessionManager to null.  this causes lots of unwanted bugs! -tcw
	 * sessionManager = null;
	 */

	sessionFile = null;
	parser = null;
	activeSession = null;
	sessions = new HashMap();
    }

    /**
     * This method will create a new instance of the SessionManager.
     *
     * @return SessionManager A new SessionManager instance.
     */
    public static SessionManager createInstance() {
        SessionManager sessionManager = new SessionManager();
	return(sessionManager);
    }

    /**
     * Get the shared instance of the SessionManager.  This is used to make the transition from
     * the old session file framework to the new framework easier.  In the old framework, it relied
     * upon a static instance of the SessionSaverLoader object and a static instance of Sessions.
     *
     * @return SessionManager The shared instance of SessionManager.
     */
    public static SessionManager getInstance() {
	if(sessionManager == null) {
	    sessionManager = createInstance();
	}
	return(sessionManager);
    }

    /**
     * Load the session information from the file name given.
     *
     * @param String filename The name of the session file to load the session information from.
     * @pre file != null
     * @pre file exists
     * @pre file is not a directory
     * @pre file is readable
     */
    public void load(String filename)
	throws FileNotFoundException, FileNotReadableException, NotAFileException, SessionException {

	if(filename == null) {
	    throw new FileNotFoundException("Loading a null file is not possible.");
	}

	File file = new File(filename);
	load(file);
    }

    /**
     * Load the session information from the file given.
     *
     * @param File file The session file to load the session information from.
     * @pre file != null
     * @pre file exists
     * @pre file is not a directory
     * @pre file is readable
     */
    public void load(File file) throws FileNotFoundException, FileNotReadableException, NotAFileException, SessionException {

	if(file == null) {
	    throw new FileNotFoundException("Loading a null file is not possible.");
	}

	if(!file.exists()) {
	    throw new FileNotFoundException("Loading a non-existent file is not possible.");
	}

	if(!file.canRead()) {
	    throw new FileNotReadableException("Loading an unreadable file is not possible.");
	}

	if(!file.isFile()) {
	    throw new NotAFileException("Loading a directory is not possible.  We can only load files.");
	}

	sessionFile = file;

	List sessionList = null;
	try {
	    parser = ParserFactory.getParserForFile(file);
	    if(parser != null) {
	    	sessionList = parser.load(file);
		log.debug("The parser loaded (based upon the file contents) has type " + parser.getClass().getName());
	    }
	    else {
		throw new SessionException("The ParserFactory returned a null parser.");
	    }
	}
	catch(Exception e) {
	    throw new SessionException(e.toString());
	}

	if((sessionList != null) && (sessionList.size() > 0)) {
	    for(int i = 0; i < sessionList.size(); i++) {
		Session currentSession = (Session)sessionList.get(i);
		log.debug("adding currentSession " + currentSession.getName());
		try {
		    addSession(currentSession);
		}
		catch(DuplicateSessionException dse) {
		    log.warn("A session exists in the local store that has the name " + currentSession.getName() +
				       " so we will not overwrite it.");
		}
		catch(Exception e) {
		    // ignore this "problematic" session and move on to the next one. -tcw
		    log.warn("An exception occured while adding session " + currentSession.getName() + ".  Skipping it.", e);
		}
	    }


	    // if the old active session is not longer in the local store, reset it to null!
	    if((activeSession == null) || (sessions == null) || (!sessions.containsKey(activeSession.getName()))) {
		activeSession = null;
	    }

	}
	else {
	    log.warn("No sessions were loaded when loading the file " + file);
	}

    }

    /**
     * Save the session information to a file.  This will take the local store of Session objects and write
     * it out to the sessionFile set for this SessionManager.  The sessionFile will only get set through a call
     * to load(File) or saveAs(File).
     *
     * @pre sessions.size > 0
     * @pre sessionFile != null
     * @pre parser != null
     */
    public void save() throws FileNotFoundException, SessionException {

	// if there is no session information, no reason to waste time writing an empty file!
	if(sessions.size() <= 0) {
	    return;
	}

	if(sessionFile == null) {
	    throw new FileNotFoundException("This session information has not been saved to a file yet.  Please use saveAs first.");
	}

	if(parser == null) {
	    throw new SessionException("The parser is null when it should not be.");
	}

	List sessionList = new ArrayList();
	Iterator i = sessions.keySet().iterator();
	while(i.hasNext()) {
	    String sessionID = (String)i.next();
	    Session session = (Session)sessions.get(sessionID);
	    sessionList.add(session);
	}
	parser.save(sessionList, sessionFile, true);

    }

    /**
     * Save the session information to the given file name.  This will take the local store of Session objects
     * and write them out to a file based upon the given file name.  This will also set the sessionFile for this
     * SessionManager to this file (so all subsequent calls to save() will write to this file).
     *
     * @param String filename The file name in which to write the session information.
     * @pre filename != null
     */
    public void saveAs(String filename)
	throws FileNotFoundException, FileExistsException, FileNotWritableException, SessionException {
	saveAs(filename, false);
    }

    /**
     * Save the session information to the given file name.  This will take the local store of Session objects
     * and write them out to a file based upon the given file name.  This will also set the sessionFile for this
     * SessionManager to this file (so all subsequent calls to save() will write to this file).
     *
     * @param String filename The file name in which to write the session information.
     * @param boolean overwrite If true and the file already exists, overwrite it with the new information.  If
     *                false and the file exists, throw a FileExistsException.
     * @pre filename != null
     */
    public void saveAs(String filename, boolean overwrite)
	throws FileNotFoundException, FileExistsException, FileNotWritableException, SessionException {


	if(filename == null) {
	    throw new FileNotFoundException("Saving to a null file is not possible.");
	}

	File file = new File(filename);
	saveAs(file, overwrite);

    }

    /**
     * Save the session information to the given file.  This will take the local store of Session objects
     * and write them out to a file.  This will also set the sessionFile for this
     * SessionManager to this file (so all subsequent calls to save() will write to this file).
     *
     * @param String filename The file name in which to write the session information.
     * @pre filename != null
     */
    public void saveAs(File file)
	throws FileNotFoundException, FileExistsException, FileNotWritableException, SessionException {
	saveAs(file, false);
    }

    /**
     * Save the session information to the given file.  This will take the local store of Session objects
     * and write them out to a file.  This will also set the sessionFile for this
     * SessionManager to this file (so all subsequent calls to save() will write to this file).
     *
     * @param String filename The file name in which to write the session information.
     * @param boolean overwrite If true and the file already exists, overwrite it with the new information.  If
     *                false and the file exists, throw a FileExistsException.
     * @pre filename != null
     */
    public void saveAs(File file, boolean overwrite)
	throws FileNotFoundException, FileExistsException, FileNotWritableException, SessionException {

	// if there is no session information, no reason to waste time writing an empty file!
	if(sessions.size() <= 0) {
	    log.warn("No sessions to save.");
	    return;
	}

	if(file == null) {
	    throw new FileNotFoundException("Saving to a null file is not possible.");
	}

	if(file.exists() && !overwrite) {
	    throw new FileExistsException("File exists already.");
	}

	/*
	if(!file.canWrite()) {
	    throw new FileNotWritableException("Saving to a file without write access is not possible.");
	}
	*/

	if(parser == null) {
	    try {
		parser = ParserFactory.getParser();
		log.debug("The parser loaded (default) has type " + parser.getClass().getName());
	    }
	    catch(Exception e) {
		throw new SessionException(e.toString());
	    }
	}

	List sessionList = new ArrayList();
	Iterator i = sessions.keySet().iterator();
	while(i.hasNext()) {
	    String sessionID = (String)i.next();
	    Session session = getSession(sessionID);
	    if(session != null) {
		log.debug("adding session to list of sessions to be saved: " + sessionID);
		sessionList.add(session);
	    }
	    else {
		log.warn("No session can be found with name " + sessionID);
	    }
	}
	sessionFile = file;
	try {
	    parser.save(sessionList, sessionFile, overwrite);
	}
	catch(SessionFileException sfe) {
	    throw new SessionException(sfe.toString());
	}
    }

    /**
     * Get the Session object that we have in the local store.  The keys
     * are the session ID and the value is the Session object.
     *
     * @return Map A Map of session ID (String) to Session objects that are stored
     *         in this instance of the SessionManager.
     */
    public Map getSessions() {
	return(sessions);
    }

    /**
     * Clone the session with the originalSessionID as the session ID and
     * name the new session as newSessionID and place it into the local store.
     *
     * @param String originalSessionID The session ID of the session to clone.
     * @param String newSessionID The name of the cloned session.
     * @pre originalSessionID != null
     * @pre newSessionID != null
     * @pre originalSessionID must exist in the local store (otherwise throw SessionNotFoundException)
     * @pre newSesionID doesn't exist in the local store already
     */
    public void cloneSession(String newSessionID, String originalSessionID)
	throws DuplicateSessionException, SessionNotFoundException, InvalidSessionIDException, SessionException {

	if(originalSessionID == null) {
	    throw new SessionNotFoundException("The SessionManager cannot find a session with a null session name.");
	}

	if(newSessionID == null) {
	    throw new InvalidSessionIDException("The session ID given (null) is invalid.");
	}

	Session originalSession = getSession(originalSessionID);
	if(originalSession == null) {
	    throw new SessionNotFoundException("A session with the name " + originalSessionID +
					       " was not found and therefore we cannot clone it.");
	}
	cloneSession(newSessionID, originalSession);
    }

    /**
     * Clone the given session and give it the session ID (sessionID) given and place
     * it into the local store.
     *
     * @param String newSessionID The session ID for the new Session.
     * @param Session oldSession The Session to clone.
     * @pre newSessionID != null
     * @pre newSessionID doesn't exist in the local store already
     * @pre oldSession != null
     */
    public void cloneSession(String newSessionID, Session oldSession)
	throws InvalidSessionIDException, SessionException {

	if(oldSession == null) {
	    throw new SessionException("Cannot clone a null session.");
	}

	if(newSessionID == null) {
	    throw new InvalidSessionIDException("The session ID given (null) is invalid.");
	}

	if(!sessions.containsKey(oldSession.getName())) {
	    throw new SessionNotFoundException("The session given to clone doesn't exist in the SessionManager.");
	}

	Session newSession = (Session)oldSession.clone();
	newSession.setName(newSessionID);
	addSession(newSession);

    }

    /**
     * Add the given session to the local store.
     *
     * @param Session session The session to add.
     * @pre session != null
     * @pre session.getName() doesn't exist in the local store already.
     */
    public void addSession(Session session) throws DuplicateSessionException {
	addSession(session, false);
    }

    /**
     * Add the given session to the local store.
     *
     * @param Session session The session to add.
     * @param boolean overwrite If true and the session name exists in the store, this
     *                new session will overwrite the old one.  If false and the session name
     *                exists in the store, a DuplicateSessionException will be thrown.
     * @pre session != null
     * @pre session.getName() doesn't exist in the local store already.
     */
    public void addSession(Session session, boolean overwrite) throws DuplicateSessionException {

	if(session == null) {
	    log.debug("Adding a null session is silly.  We will just return.");
	    return;
	}

	String sessionID = session.getName();
	if((!overwrite) && (sessions.containsKey(sessionID))) {
	    throw new DuplicateSessionException("The name of the session given already exists in the local store.");
	}

	sessions.put(sessionID, session);
	session.addObserver(this);
	setChanged();
	notifyObservers();

	log.debug("Added session " + sessionID + ".");
    }

    /**
     * Remove the given session from the local store.
     *
     * @param Session session The session to be removed from the local store.
     * @post There will not be a Session in the local store that matches the given Session.
     */
    public void removeSession(Session session) {
	if(session == null) {
	    return;
	}

	String sessionID = session.getName();
	removeSession(sessionID);
    }

    /**
     * Remove the session defined by the given session ID from the local store.
     *
     * @param String sessionID An ID for a session.
     * @post There will not be a Session in the local store that matches the given Session.
     */
    public void removeSession(String sessionID) {

	if(sessionID == null) {
	    return;
	}

	if((sessions == null) || (sessions.size() <= 0)) {
	    return;
	}

	if(sessions.containsKey(sessionID)) {
	    Session session = (Session)sessions.get(sessionID);
	    session.deleteObserver(this);
	    sessions.remove(sessionID);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove all Session objects from the local store.
     *
     * @post sessions.size() == 0
     */
    public void clearSessions() {
	if((sessions == null) || (sessions.size() <= 0)) {
	    return;
	}
	activeSession = null;
	Iterator si = sessions.keySet().iterator();
	while(si.hasNext()) {
	    String sessionID = (String)si.next();
	    Session session = (Session)sessions.get(sessionID);
	    session.deleteObserver(this);
	}
	sessions.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Get the active session for this SessionManager.
     *
     * @return Session The active session set or null if there is not active session.
     */
    public Session getActiveSession() {
	return(activeSession);
    }

    /**
     * Set the given Session as the one that is active.  If this session is not
     * in the local store a SessionNotFoundException will be thrown.
     *
     * @param Session session The session to be active.
     */
    public void setActiveSession(Session session) throws SessionNotFoundException {

	if(session == null) {
	    throw new SessionNotFoundException("A null session cannot be set as active.");
	}

	String sessionID = session.getName();
	if(sessions.containsKey(sessionID)) {
	    activeSession = session;
	    setChanged();
	    notifyObservers();
	}
	else {
	    throw new SessionNotFoundException("The session given is not in the local store.");
	}
    }

    /**
     * Set the Session that matches the given sessionID as the one that is active.  If there is no
     * session that matches this sessionID, a SessionNotFoundException will be thrown.
     *
     * @param String sessionID The session ID for the Session that should be active.
     */
    public void setActiveSession(String sessionID) throws SessionNotFoundException {

	if(sessionID == null) {
	    throw new SessionNotFoundException("A null session cannot be set as active.");
	}
	
	if(sessions.containsKey(sessionID)) {
	    activeSession = (Session)sessions.get(sessionID);
	    setChanged();
	    notifyObservers();
	}
	else {
	    throw new SessionNotFoundException("The session given is not in the local store.");
	}
    }

    /**
     * Get the session whose session ID matches the sessionID given.
     *
     * @param String sessionID The session ID for the Session object to be retrieved.
     * @return Session The Session in the local store that matches the given sessionID or null if
     *         no Session exists in the local store with a matching session ID.
     * @pre sessionID != null
     */
    public Session getSession(String sessionID) {
	if(sessionID == null) {
	    return(null);
	}

	Session session = (Session)sessions.get(sessionID);
	return(session);
    }

    /**
     * Select a group of sessions given a list of session IDs.  The
     * resulting List will have the same order as the given list.
     */
    public List selectSessions(List sessionIDList) {

	List sessionList = new ArrayList();

	if(sessionIDList == null) {
	    // return an empty list
	    return(sessionList);
	}

	for(int i = 0; i < sessionIDList.size(); i++) {
	    Object currentKey = sessionIDList.get(i);
	    if(currentKey instanceof String) {
		Session currentSession = (Session)sessions.get(currentKey);
		if(currentSession != null) {
		    sessionList.add(currentSession);
		}
	    }
	}

	return(sessionList);
    }

    /**
     * Select a group of sessions given an array of session IDs.  The
     * resulting List will have the same order as the given array.  If a
     * session ID doesn't match a Session in the local store, nothing will
     * be added to the list.  The list may be empty.
     *
     * @param String[] sessionIDs A String array of session IDs
     * @return A List of Session objects that match the session IDs given (and
     *         in the same order).  The List may be empty.
     * @pre sessionIDs != null
     */
    public List selectSessions(String[] sessionIDs) {

	List sessionList = new ArrayList();

	if(sessionIDs == null) {
	    // return an empty list
	    return(sessionList);
	}

	for(int i = 0; i < sessionIDs.length; i++) {
	    if(sessionIDs[i] != null) {
		Session currentSession = (Session)sessions.get(sessionIDs[i]);
		if(currentSession != null) {
		    sessionList.add(currentSession);
		}
	    }
	}

	return(sessionList);
    }

    /**
     * Add a set of Session objects to the local store and ignore those that cause conflicts.
     *
     * @param Set sessions A set of Session objects to be added to the local store.
     * @pre sessions != null
     * @pre sessions.size() > 0
     */
    public void addSessions(Set sessions) {
	addSessions(sessions, false);
    }

    /**
     * Add a set of Session objects to the local store and overwrite those
     * that conflict (the new one taking precedence) if overwrite is true.  If
     * overwrite is false and a DuplicateSessionException happens, throw away the
     * new session (and keep the old one).
     *
     * @param Set sessions A set of Session objects to be added to the local store.
     * @param boolean overwrite If true, overwrite the old session when a new one conflicts.
     * @pre sessions != null
     * @pre sessions.size() > 0
     */
    public void addSessions(Set sessions, boolean overwrite) {

	if(sessions == null) {
	    return;
	}

	if(sessions.size() <= 0) {
	    return;
	}

	Iterator i = sessions.iterator();
	while(i.hasNext()) {
	    Object currentObject = i.next();
	    if(currentObject instanceof Session) {
		try {
		    Session currentSession = (Session)currentObject;
		    addSession(currentSession, overwrite);
		}
		catch(DuplicateSessionException dse) {
		    // just throw this session away since one exists with this name. -tcw
		}
	    }
	}
    }

    /**
     * Get the number of sessions stored in the SessionManager at this time.
     *
     * @return int The number of Session objects in the local store.
     */
    public int getSessionCount() {
	return(sessions.size());
    }

    /**
     * Get the session file that we are storing the session information in.
     *
     * @return File The session file that we will be storing session information in (might be null).
     */
    public File getSessionFile() {
	return(sessionFile);
    }

    /**
     * [Satisfy the Observer interface] This will handle the signal when a Session that
     * is stored in this SessionManager is modified.  This will help us to keep track of
     * when the sessionFile needs to be saved or when the view needs to be updated.
     */
    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }

    /**
     * Create a String representation of this object.  In this case we will print a list
     * of the sessions that are stored in this manager with an asterisk next to the
     * active session.
     *
     * @return String A String representation of this object.
     */
    public String toString() {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println("Session Manager Session list:");
	pw.println("-----------------------------------");

	if((sessions == null) || (sessions.size() <= 0)) {
	    pw.println("No sessions.");
	}
	else {
	    Iterator si = sessions.keySet().iterator();
	    while(si.hasNext()) {
		String sessionID = (String)si.next();
		if((activeSession != null) && (sessionID.equals(activeSession.getName()))) {
		    pw.println("    " + sessionID + " *");
		}
		else {
		    pw.println("    " + sessionID);
		}
	    }
	}

	return(sw.toString());
    }
    
}
