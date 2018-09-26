package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.*;

import edu.ksu.cis.bandera.bui.wizard.jwf.Stage;
import edu.ksu.cis.bandera.bui.wizard.jwf.Wizard;
import edu.ksu.cis.bandera.bui.wizard.jwf.WizardPanel;
import edu.ksu.cis.bandera.bui.wizard.jwf.WizardListener;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;
import edu.ksu.cis.bandera.sessions.Assertion;
import edu.ksu.cis.bandera.sessions.Predicate;
import edu.ksu.cis.bandera.sessions.TemporalProperty;
import edu.ksu.cis.bandera.sessions.Specification;
import edu.ksu.cis.bandera.sessions.Quantification;
import edu.ksu.cis.bandera.sessions.QuantifiedVariable;
import edu.ksu.cis.bandera.sessions.InvalidSessionIDException;
import edu.ksu.cis.bandera.sessions.SessionException;
import edu.ksu.cis.bandera.sessions.SessionNotFoundException;
import edu.ksu.cis.bandera.sessions.DuplicateSessionException;

import java.io.*;
import java.io.File;

import edu.ksu.cis.bandera.jjjc.*;

import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;

import edu.ksu.cis.bandera.specification.pattern.PatternSaverLoader;

import edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet;

import edu.ksu.cis.bandera.specification.predicate.datastructure.PredicateSet;

import java.net.URL;

import edu.ksu.cis.bandera.bui.BUI;

import org.apache.log4j.Category;

/**
 * The BanderaWizard is an application that will walk a user through the
 * process of creating a session file and defining the model reduction
 * and properties that Bandera will use to model check a system.  When
 * it is done, it will be able to write the file out or run Bandera
 * using the newly configured system.
 *
 * To use the system, you can start it up using the main method or
 * by instantiating a new BanderaWizard and starting it.
 *
 * <pre>
 * java edu.ksu.cis.bandera.bui.wizard.BanderaWizard
 * </pre>
 *
 * <pre>
 * BanderaWizard bw = new BanderaWizard();
 * bw.start();
 * </pre>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:19 $
 */
public final class BanderaWizard
    extends JFrame
    implements edu.ksu.cis.bandera.bui.wizard.jwf.WizardListener, WizardController {


    /*
     * This class should be re-written with a better design in mind.  There is way too
     * much re-use of code to not separate it out into methods. -tcw
     */




    /**
     * This is the Wizard we will use.  It provides the entry point to the framework.
     */
    private Wizard wizard;

    /**
     * This is the initial panel to use in the wizard.
     */
    private WizardPanel firstWizardPanel;

    /**
     * The assertionChecks field is a flag used to tell if we are going to do defined
     * assertions to check after the property wizard asks.  This is necessary to hold since
     * the decision will be used in more than one step (or rather more than one call
     * into getNext()).
     */
    private boolean assertionChecks = false;

    /**
     * The temporalPropertyChecks field is a flag used to tell if we are going to do defined
     * a temporal property to check after the property wizard asks.  This is necessary to hold since
     * the decision will be used in more than one step (or rather more than one call
     * into getNext()).
     */
    private boolean temporalPropertyChecks = false;

    /**
     * The SessionManager provides an easy model to work with that controls and contains
     * Session objects.
     */
    private SessionManager sessionManager;

    /**
     * The session field is the currently active session that we are working on.
     */
    private Session session;

    /**
     * The name that the user wants the session to be.  This is necessary to hold since the
     * decision will be used in more than one step (or rather more than one call to getNext()).
     */
    private String sessionName;

    /**
     * The currentPattern is just a String that represents the temporal property pattern
     * that was chosen.  This is necessary to hold since the pattern is used in more than
     * on step (or rathe more than one call to getNext()).
     */
    private Pattern currentPattern;

    private File sessionFile;

    /**
     *
     */
    private File baseDirectory;

    /**
     * The log we will write messages to.
     */
    private static final Category log = Category.getInstance(BanderaWizard.class);


    /**
     * Create a new BanderaWizard.  This will initialize the Wizard
     * to use the default steps in the process.
     */
    public BanderaWizard() {

	super("Bandera Wizard");
	setBackground(new java.awt.Color(204, 204, 255));

	// use the bandera icon in the upper-left hand corner!
	String imageFilename = "edu/ksu/cis/bandera/bui/wizard/jwf/images/bandera-icon.gif";
	URL imageIconURL =
	    ClassLoader.getSystemClassLoader().getResource(imageFilename);
	ImageIcon imageIcon = new ImageIcon(imageIconURL);
	setIconImage(imageIcon.getImage());

	// create a new wizard
	wizard = new Wizard();

	// the wizard will be the main panel and will manage all the other wizard panels
	setContentPane(wizard);

	// set this as the listener for the wizard
	wizard.addWizardListener(this);

	// Create the first panel to show and give it the controller to use (in this case, this)
	IntroWizardPanel introWP = new IntroWizardPanel();
	introWP.setWizardController(this);
	firstWizardPanel = introWP;

	// set up the stages of this wizard
	Stage introStage = new Stage();
	introStage.setName("Introduction");
	introStage.add(IntroWizardPanel.class);
	wizard.registerStage(introStage);

	Stage applicationStage = new Stage();
	applicationStage.setName("Application");
	applicationStage.add(SessionWizardPanel.class);
	applicationStage.add(SessionFileWizardPanel.class);
	applicationStage.add(CloneSessionWizardPanel.class);
	applicationStage.add(MainClassWizardPanel.class);
	applicationStage.add(ClasspathWizardPanel.class);
	applicationStage.add(WorkingDirectoryWizardPanel.class);
	applicationStage.add(OutputNameWizardPanel.class);
	wizard.registerStage(applicationStage);
    
	Stage propertyStage = new Stage();
	propertyStage.setName("Property");
	propertyStage.add(PropertySummaryWizardPanel.class);
	propertyStage.add(PropertyWizardPanel.class);
	propertyStage.add(TemporalPropertyWizardPanel.class);
	propertyStage.add(AssertionWizardPanel.class);
	propertyStage.add(ParameterMappingWizardPanel.class);
	propertyStage.add(PredicateChoiceWizardPanel.class);
	wizard.registerStage(propertyStage);
    
	Stage modelReductionStage = new Stage();
	modelReductionStage.setName("Model Reduction");
	modelReductionStage.add(AbstractionIntroWizardPanel.class);
	modelReductionStage.add(SlicingWizardPanel.class);
	modelReductionStage.add(ModelReductionWizardPanel.class);
	wizard.registerStage(modelReductionStage);

	Stage modelCheckerStage = new Stage();
	modelCheckerStage.setName("Model Checking");
	modelCheckerStage.add(JPFOptionWizardPanel.class);
	modelCheckerStage.add(SpinOptionWizardPanel.class);
	modelCheckerStage.add(DSpinOptionWizardPanel.class);
	//modelCheckerStage.add(HSFSpinOptionWizardPanel.class);
	//modelCheckerStage.add(SMVOptionWizardPanel.class);
	modelCheckerStage.add(ModelCheckerChoiceWizardPanel.class);
	wizard.registerStage(modelCheckerStage);
    
	Stage completionStage = new Stage();
	completionStage.setName("Completion");
	completionStage.add(CompletionWizardPanel.class);
	wizard.registerStage(completionStage);

	baseDirectory = new File(System.getProperty("user.dir"));

    }

    /**
     * Get the next WizardPanel in the Wizard based upon the current WizardPanel's
     * state.
     *
     * @return WizardPanel Return the wizard panel that comes next in the wizard process.
     * @param WizardPanel currentWizardPanel The panel that is currently in view.
     */
    public WizardPanel getNext(WizardPanel currentWizardPanel) {

	if (currentWizardPanel == null) {
	    return (null);
	}

	if (currentWizardPanel instanceof IntroWizardPanel) {

	    // return the SessionWizardPanel
	    SessionWizardPanel sessionWP = new SessionWizardPanel();
	    sessionWP.setWizardController(this);
	    return (sessionWP);
	}

	if (currentWizardPanel instanceof SessionWizardPanel) {
	    /* Grab the session name given */
	    sessionName = ((SessionWizardPanel) currentWizardPanel).getSessionName();

	    // return the SessionFileWizardPanel
	    SessionFileWizardPanel sessionFileWP = new SessionFileWizardPanel();
	    sessionFileWP.setBaseDirectory(baseDirectory);
	    sessionFileWP.setWizardController(this);
	    return (sessionFileWP);
	}

	if (currentWizardPanel instanceof SessionFileWizardPanel) {
	    SessionFileWizardPanel sessionFileWP =
		(SessionFileWizardPanel) currentWizardPanel;
	    sessionFile = sessionFileWP.getSessionFile();
	    baseDirectory = sessionFileWP.getBaseDirectory();

	    sessionManager = SessionManager.createInstance();
	    if (sessionFileWP.isSessionFileCreated()) {
		// create a new session and sessions
		//sessionManager.clearSessions();
		session = new Session();
		session.setName(sessionName);
		try {
		    sessionManager.addSession(session);
		    sessionManager.setActiveSession(session);
		}
		catch(DuplicateSessionException dse) {
		    // this should never happen since this is a new session file/session manager! -tcw
		    // just fail!
		    JOptionPane.showMessageDialog(currentWizardPanel,
						  "An exception occured while adding the session.  A session with this name, " +
						  sessionName + ", exists already." +
						  ".\nPlease go back and enter a new session name.");
		    log.error("Exception while adding the session.", dse);
		    return (currentWizardPanel);
		}
		catch(SessionNotFoundException snfe) {
		    // this should never happen since the session being activated was just added! -tcw
		    // just fail!
		    JOptionPane.showMessageDialog(currentWizardPanel,
						  "An exception occured while activating the session.  A session with this name, " +
						  sessionName + ", doesn't exist." +
						  ".\nPlease go back and enter a new session name.");
		    log.error("Exception while activating this session.", snfe);
		    return (currentWizardPanel);
		}

		// return the MainClassWizardPanel
		MainClassWizardPanel mainClassWP = new MainClassWizardPanel();
		mainClassWP.setBaseDirectory(baseDirectory);
		mainClassWP.setWizardController(this);
		return (mainClassWP);
	    }
	    else {
		// load the session file
		try {
		    sessionManager.load(sessionFile);
		}
		catch(Exception e) {
		    JOptionPane.showMessageDialog(currentWizardPanel,
						  "An exception occured while loading the session file." +
						  ".\nPlease go back and specify a different session file.");
		    log.error("Exception while loading the session file.", e);
		    return (currentWizardPanel);
		}

		// return the CloneSessionWizardPanel
		CloneSessionWizardPanel cloneSessionWP = new CloneSessionWizardPanel();
		cloneSessionWP.setWizardController(this);

		// parse and set the session IDs that exist
		int sessionCount = sessionManager.getSessionCount();
		if(sessionCount > 0) {
		    java.util.List sessionList = new java.util.ArrayList(sessionManager.getSessionCount());
		    sessionList.addAll(sessionManager.getSessions().keySet());
		    for (int i = 0; i < sessionList.size(); i++) {
			cloneSessionWP.addSession((String)sessionList.get(i));
		    }
		}

		return (cloneSessionWP);
	    }
	}

	if (currentWizardPanel instanceof CloneSessionWizardPanel) {

	    /* At this point, we need to see if a session was chosen to clone.  if so, we
	     * need to clone that session and set it as the "active" session that we will
	     * be working on.
	     */

	    String sessionNameToClone =
		((CloneSessionWizardPanel) currentWizardPanel).getSelectedSession();
	    if (sessionNameToClone == null) {
		log.info("No session name to clone from was selected (it's value is null).");
		session = new Session();
		session.setName(sessionName);
		try {
		    sessionManager.addSession(session);
		    sessionManager.setActiveSession(session);
		}
		catch(DuplicateSessionException dse) {
		    // just fail!
		    JOptionPane.showMessageDialog(currentWizardPanel,
						  "An exception occured while adding the session.  A session with this name, " +
						  sessionName + ", exists already." +
						  ".\nPlease go back and enter a new session name.");
		    log.error("Exception while adding the session.", dse);
		    return (currentWizardPanel);
		}
		catch(SessionNotFoundException snfe) {
		    // just fail!
		    JOptionPane.showMessageDialog(currentWizardPanel,
						  "An exception occured while activating the session.  A session with this name, " +
						  sessionName + ", doesn't exist." +
						  ".\nPlease go back and enter a new session name.");
		    log.error("Exception while activating this session.", snfe);
		    return (currentWizardPanel);
		}
	    }
	    else {
		try {
		    sessionManager.cloneSession(sessionName, sessionNameToClone);
		    session = sessionManager.getSession(sessionName);
		    sessionManager.setActiveSession(session);
		}
		catch(SessionNotFoundException snfe) {
		    // instead of cloning, create a new one with the given name
		    session = new Session();
		    session.setName(sessionName);
		    try {
			sessionManager.addSession(session);
			sessionManager.setActiveSession(session);
		    }
		    catch(DuplicateSessionException dse) {
			// just fail!
			JOptionPane.showMessageDialog(currentWizardPanel,
						      "An exception occured while adding the session.  A session with this name, " +
						      sessionName + ", exists already." +
						      ".\nPlease go back and enter a new session name.");
			log.error("Exception while adding the session.", dse);
			return (currentWizardPanel);
		    }
		    catch(SessionNotFoundException snfe2) {
			// just fail!
			JOptionPane.showMessageDialog(currentWizardPanel,
						      "An exception occured while activating the session.  A session with this name, " +
						      sessionName + ", doesn't exist." +
						      ".\nPlease go back and enter a new session name.");
			log.error("Exception while activating this session.", snfe2);
			return (currentWizardPanel);
		    }
		}
		catch(InvalidSessionIDException isie) {
		    // re-prompt the user for a valid session name!
		    JOptionPane.showMessageDialog(currentWizardPanel,
						  "An exception occured while cloning the session.  The name given, " +
						  sessionName + ", is not valid." +
						  ".\nPlease go back and enter a new session name.");
		    log.error("Exception while cloning the session.", isie);
		    return (currentWizardPanel);
		}
		catch(SessionException se) {
		    // instead of cloning, create a new one with the given name
		    session = new Session();
		    session.setName(sessionName);
		    try {
			sessionManager.addSession(session);
			sessionManager.setActiveSession(session);
		    }
		    catch(DuplicateSessionException dse) {
			// just fail!
			JOptionPane.showMessageDialog(currentWizardPanel,
						      "An exception occured while adding the session.  A session with this name, " +
						      sessionName + ", exists already." +
						      ".\nPlease go back and enter a new session name.");
			log.error("Exception while adding the session.", dse);
			return (currentWizardPanel);
		    }
		    catch(SessionNotFoundException snfe) {
			// just fail!
			JOptionPane.showMessageDialog(currentWizardPanel,
						      "An exception occured while activating the session.  A session with this name, " +
						      sessionName + ", doesn't exist." +
						      ".\nPlease go back and enter a new session name.");
			log.error("Exception while activating this session.", snfe);
			return (currentWizardPanel);
		    }
		}
	    }

	    // ASSUME: session is not null and is stored in the sessionManager

	    // return the MainClassWizardPanel
	    MainClassWizardPanel mainClassWP = new MainClassWizardPanel();
	    mainClassWP.setBaseDirectory(baseDirectory);
	    mainClassWP.setWizardController(this);

	    /* if this is a cloned session, we can grab the main class from it */
	    File mainClassFile = session.getMainClassFile();
	    String mainClassFilename = "";
	    if(mainClassFile != null) {
		try {
		    mainClassFilename = mainClassFile.getCanonicalPath();
		}
		catch(Exception e) {
		    log.info("An exception occured while getting the canonical path for the main class file." +
			     "  Using the absolute path instead.");
		    mainClassFilename = mainClassFile.getAbsolutePath();
		}
	    }
	    else {
		log.warn("The main class is null when we have cloned a session.  This should never happen.");
		mainClassFilename = "";
	    }
	    mainClassWP.setMainClassFilename(mainClassFilename);

	    return (mainClassWP);
	}

	// assume: session != null
	if (session == null) {
	    log.fatal("session is null when it should not be.");
	    return(null);
	}

	/*
	 * Note: the MainClassWizardPanel should return a File instead of a String! -tcw
	 */
	if (currentWizardPanel instanceof MainClassWizardPanel) {
	    MainClassWizardPanel mainClassWP = (MainClassWizardPanel) currentWizardPanel;
	    String mainClassFilename = mainClassWP.getMainClassFilename();
	    File mainClassFile = new File(mainClassFilename);
	    session.setMainClassFile(mainClassFile);
	    baseDirectory = mainClassWP.getBaseDirectory();

	    // return the ClasspathWizardPanel
	    ClasspathWizardPanel classpathWP = new ClasspathWizardPanel();
	    classpathWP.setBaseDirectory(baseDirectory);
	    classpathWP.setWizardController(this);

	    /* if this is a cloned session, we will use it's classpath as the basis for this session. */
	    classpathWP.setClasspathList(session.getClasspath());

	    return (classpathWP);
	}

	/*
	 * Note: Is there a better way to pass around a classpath? -tcw
	 */
	if (currentWizardPanel instanceof ClasspathWizardPanel) {

	    ClasspathWizardPanel classpathWP = (ClasspathWizardPanel) currentWizardPanel;
	    String classpath = classpathWP.getClasspath();
	    session.setClasspathString(classpath);
	    baseDirectory = classpathWP.getBaseDirectory();

	    /* At this point, we need to compile the application and see if we can run
	     * Bandera on it to get the BSL stuff that we will use later.
	     */
	    java.awt.Cursor previousCursor = getCursor();
	    try {

		setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

		CompilationManager.setClasspath(session.getClasspathString());
		CompilationManager.setFilename(session.getMainClassFile().getAbsolutePath());
		CompilationManager.setIncludedPackagesOrTypes(session.getIncludesArray());
		CompilationManager.compile();

		/* An alternate way to compile the application.   However, we need to use
		 * the Compilation manager so that the underlying Bandera system will get
		 * initialized correctly: Patterns, Properties, Assertions, etc.
		 *
		 * System.setProperty("java.class.path", session.getClasspath());
		 * String[] classesToCompile = {session.getFilename()};
		 * com.sun.tools.javac.Main compiler = new com.sun.tools.javac.Main();
		 * compiler.compile(classesToCompile);
		 */
	    }
	    catch (Exception e) {
		JOptionPane.showMessageDialog(
					      currentWizardPanel,
					      "An exception occured while compiling the application: "
					      + e.toString()
					      + ".\nPlease go back and make sure all information is correct in order to compile this application.");
		log.error("Exception while compiling the application.", e);
		return (currentWizardPanel);
	    }
	    finally {
		setCursor(previousCursor);
	    }

	    // return the OutputNameWizardPanel
	    OutputNameWizardPanel outputNameWP = new OutputNameWizardPanel();
	    outputNameWP.setWizardController(this);

	    // if we are cloning a session, we should use the name defined in there.  otherwise,
	    //  use the sessionName as the default name to use.
	    String outputName = session.getOutputName();
	    if ((outputName == null) || (outputName.length() < 1)) {
		outputNameWP.setOutputName(sessionName);
	    }
	    else {
		outputNameWP.setOutputName(outputName);
	    }

	    return (outputNameWP);
	}

	if (currentWizardPanel instanceof OutputNameWizardPanel) {

	    OutputNameWizardPanel outputNameWP = (OutputNameWizardPanel) currentWizardPanel;
	    String outputName = outputNameWP.getOutputName();
	    session.setOutputName(outputName);

	    // return the WorkingDirectoryWizardPanel
	    WorkingDirectoryWizardPanel workingDirectoryWP =
		new WorkingDirectoryWizardPanel();
	    workingDirectoryWP.setWizardController(this);
	    workingDirectoryWP.setBaseDirectory(baseDirectory);

	    File workingDirectory = session.getWorkingDirectory();
	    workingDirectoryWP.setWorkingDirectory(workingDirectory);

	    return (workingDirectoryWP);
	}

	if (currentWizardPanel instanceof WorkingDirectoryWizardPanel) {

	    WorkingDirectoryWizardPanel workingDirectoryWP =
		(WorkingDirectoryWizardPanel) currentWizardPanel;
	    File workingDirectory = workingDirectoryWP.getWorkingDirectory();
	    session.setWorkingDirectory(workingDirectory);
	    baseDirectory = workingDirectoryWP.getBaseDirectory();

	    // return the PropertyWizardPanel
	    PropertyWizardPanel propertyWP = new PropertyWizardPanel();
	    propertyWP.setWizardController(this);
	    return (propertyWP);
	}

	/*
	 * RENAME: assertionChecks to assertionCheckingEnabled or checkAssertions
	 * RENAME: temporalPropertyChecks to temporalPropertyCheckingEnabled or checkTemporalProperty
	 */

	if (currentWizardPanel instanceof PropertyWizardPanel) {
	    PropertyWizardPanel propertyWP = (PropertyWizardPanel) currentWizardPanel;
	    assertionChecks = propertyWP.isAssertionCheckingEnabled();
	    temporalPropertyChecks = propertyWP.isTemporalPropertyCheckingEnabled();

	    if (assertionChecks) {
		// return AssertionPropertyWizardPanel
		AssertionWizardPanel assertionWP = new AssertionWizardPanel();
		assertionWP.setWizardController(this);

		// parse and set the assertions to display and determine which ones should be selected
		Set activeAssertionSet = new HashSet(); // the names of all the active assertions in this session
		Specification specification = session.getSpecification();
		if(specification != null) {
		    Set assertionSet = specification.getAssertions();  // the assertions in this session
		    // parse and set the assertions to display and determine which ones should be selected
		    if((assertionSet != null) && (assertionSet.size() > 0)) {
			log.debug("there are " + assertionSet.size() + " enabled assertions in this application.");
			Iterator ai = assertionSet.iterator();
			while(ai.hasNext()) {
			    Assertion currentAssertion = (Assertion)ai.next();
			    if((currentAssertion != null) && (currentAssertion.enabled())) {
				String currentAssertionName = currentAssertion.getName();
				log.debug("Adding an enabled assertion: " + currentAssertionName);
				activeAssertionSet.add(currentAssertionName);
			    }
			}
		    }
		    else {
			log.debug("assertionSet contains no assertions.  no enabled assertions.");
		    }
		}
		else {
		    log.debug("specification is null.  no enabled assertions.");
		}

		List assertionList = parseAssertions(AssertionSet.getAssertionSetTable());
		if ((assertionList != null) && (assertionList.size() > 0)) {
		    log.debug("there are " + assertionList.size() + " assertions defined in this application.");
		    for (int i = 0; i < assertionList.size(); i++) {
			edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion currentAssertion =
			    (edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion)assertionList.get(i);
			String currentAssertionName = currentAssertion.getName().toString();
			log.debug("adding assertion to the AssertionWizardPanel: " + currentAssertionName);
			boolean active = activeAssertionSet.contains(currentAssertionName);
			assertionWP.addAssertion(currentAssertion, active);
		    }
		}
		else {
		    log.debug("no assertions in the application.");
		}

		return (assertionWP);
	    }
	    else {
		// is there a better way to see if BSL was used to define predicates? -tcw
		List predicates = parsePredicates(PredicateSet.getPredicateSetTable());
		if ((temporalPropertyChecks)
		    && (predicates != null)
		    && (predicates.size() >= 1)) {

		    log.debug("Since we have predicates.size() = " + predicates.size() +
			      ", we will work thru the temporal property portion of the wizard.");
		    // return TemporalPropertyWizardPanel
		    TemporalPropertyWizardPanel temporalPropertyWP =
			new TemporalPropertyWizardPanel();
		    temporalPropertyWP.setWizardController(this);
		    List patternList = new ArrayList();
		    Hashtable patternNameTable = PatternSaverLoader.getPatternTable();
		    if((patternNameTable != null) && (patternNameTable.size() > 0)) {
			Iterator patternNameTableIterator = patternNameTable.keySet().iterator();
			while(patternNameTableIterator.hasNext()) {
			    String patternName = (String)patternNameTableIterator.next();
			    Hashtable patternScopeTable = (Hashtable)patternNameTable.get(patternName);
			    if((patternScopeTable != null) && (patternScopeTable.size() > 0)) {
				Iterator patternScopeTableIterator = patternScopeTable.keySet().iterator();
				while(patternScopeTableIterator.hasNext()) {
				    String patternScope = (String)patternScopeTableIterator.next();
				    Pattern pattern = (Pattern)patternScopeTable.get(patternScope);
				    patternList.add(pattern);
				}
			    }
			}
		    }
		    temporalPropertyWP.setPatternList(patternList);

		    // figure out which pattern is set in the session right now!
		    Specification specification = session.getSpecification();
		    if(specification != null) {
			TemporalProperty tp = specification.getTemporalProperty();
			if(tp != null) {
			    Pattern selectedPattern = tp.getPattern();
			    temporalPropertyWP.setSelectedPattern(selectedPattern);
			}
			else {
			    // there is currently not a temporal property to base this panel off
			}
		    }
		    else {
			// there is currently not a specification to base this panel off
		    }

		    return (temporalPropertyWP);
		}
		else {
		    // return the ModelReductionWizardPanel
		    ModelReductionWizardPanel modelReductionWP = new ModelReductionWizardPanel();
		    modelReductionWP.setWizardController(this);
		    return (modelReductionWP);
		}
	    }
	}

	if (currentWizardPanel instanceof TemporalPropertyWizardPanel) {
	    TemporalPropertyWizardPanel temporalPropertyWP =
		(TemporalPropertyWizardPanel) currentWizardPanel;
	    currentPattern = temporalPropertyWP.getSelectedPattern();

	    if (currentPattern == null) {
		log.warn("This pattern could not be found.  Please report this bug to the Santos Laboratory at KSU.");
		return (currentWizardPanel);
	    }
	    Specification specification = session.getSpecification();
	    if(specification == null) {
		specification = new Specification();
		session.setSpecification(specification);
	    }
	    TemporalProperty temporalProperty = specification.getTemporalProperty();
	    if(temporalProperty == null) {
		temporalProperty = new TemporalProperty();
		specification.setTemporalProperty(temporalProperty);
	    }
	    temporalProperty.setPattern(currentPattern);

	    // return the PredictateChoiceWizardPanel
	    PredicateChoiceWizardPanel predicateChoiceWP = new PredicateChoiceWizardPanel();
	    predicateChoiceWP.setWizardController(this);

	    List predicates = parsePredicates(PredicateSet.getPredicateSetTable());
	    predicateChoiceWP.setPredicates(predicates);

	    // make sure to set the predicate list before this!  or risk a null pointer exception! -tcw
	    predicateChoiceWP.setPattern(currentPattern);

	    // build a mapping from predicate name to the expression
	    Map predicateExpressionMap = new HashMap();
	    Set predicateSet = temporalProperty.getPredicates();
	    if((predicateSet != null) && (predicateSet.size() > 0)) {
		Iterator psi = predicateSet.iterator();
		while(psi.hasNext()) {
		    Predicate p = (Predicate)psi.next();
		    String name = p.getName();
		    String expression = p.getExpression();
		    predicateExpressionMap.put(name, expression);
		}
	    }
	    predicateChoiceWP.setCurrentExpressions(predicateExpressionMap);

	    return (predicateChoiceWP);
	}

	if (currentWizardPanel instanceof PredicateChoiceWizardPanel) {

	    PredicateChoiceWizardPanel predicateChoiceWP =
		(PredicateChoiceWizardPanel) currentWizardPanel;
	    Map selectedPredicatesMap = predicateChoiceWP.getSelectedPredicates();

	    // using that information, we should determine if a mapping is necessary.  only
	    // do it if one of the selected predicates has a parameter to it???? -tcw

	    // we should also set the currently configured quantification in the wizard panel. -tcw

	    ParameterMappingWizardPanel parameterMappingWP =
		new ParameterMappingWizardPanel();
	    parameterMappingWP.setWizardController(this);

	    parameterMappingWP.setPattern(currentPattern);

	    parameterMappingWP.setParameterMap(selectedPredicatesMap);
	    return (parameterMappingWP);
	}

	if (currentWizardPanel instanceof ParameterMappingWizardPanel) {
	    ParameterMappingWizardPanel parameterMappingWP =
		(ParameterMappingWizardPanel) currentWizardPanel;
	    /*
	     * To proceed, we need to generate the Quantification based upon the variable names
	     * defined in the ParameterMappingWizardPanel and the types of each parameter
	     * defined in the Predicate itself.  All this information is stored in the
	     * variable map that is generated by the ParameterMappingWizardPanel.  At the same
	     * time, we need to create the property based upon the parameter mapping given
	     * in the ParameterMappingWizardPanel.  To do this, we need to generate a
	     * Hashtable where the key is the parameter name and the value is what that
	     * parameter should be expanded to.  So if you have {P}, the key will be
	     * P and the value would be something like this: Full(b).
	     */
	    Specification specification = session.getSpecification();
	    if(specification == null) {
		specification = new Specification();
		session.setSpecification(specification);
	    }

	    TemporalProperty temporalProperty = specification.getTemporalProperty();
	    if(temporalProperty == null) {
		temporalProperty = new TemporalProperty();
		specification.setTemporalProperty(temporalProperty);
	    }
	    else {
		// if predicates are present, we will need to clear them to make this property accurate
		temporalProperty.clearPredicates();
	    }

	    Quantification quantification = temporalProperty.getQuantification();
	    if(quantification == null) {
		quantification = new Quantification();
		temporalProperty.setQuantification(quantification);
	    }
	    else {
		// if a quantification already exists, we should clear it!
		quantification.clearQuantifiedVariables();
	    }

	    Hashtable formatExpansionHashtable = new Hashtable();
	    Map variableMap = parameterMappingWP.getVariableMap();
	    if((variableMap != null) && (variableMap.size() > 0)) {
		Iterator iterator = variableMap.keySet().iterator();
		while (iterator.hasNext()) {
		    Object parameterNameObject = iterator.next();
		    if (!(parameterNameObject instanceof String)) {
			log.warn("current key (parameterNameObject) is not a String as expected, skipping it.");
			continue;
		    }
		    String parameterName = (String) parameterNameObject;

		    Object variableListObject = variableMap.get(parameterName);
		    if (!(variableListObject instanceof List)) {
			log.warn("current value (variableListObject) is not a List as expected, skipping it.");
			continue;
		    }

		    List variableList = (List) variableListObject;
		    if ((variableList == null) || (variableList.size() < 1)) {
			log.warn("current list (variableList) is not valid, skipping it.");
			continue;
		    }

		    log.debug("Expanding the parameter " + parameterName +
			      " using a variable list of size " + variableList.size() + ".");

		    StringBuffer expressionStringBuffer = new StringBuffer();
		    for (int i = 0; i < variableList.size(); i++) {
			Object o = variableList.get(i);

			if (o == null) {
			    continue;
			}

			if(o instanceof String) {
			    String s = (String)o;
			    expressionStringBuffer.append(s);
			    log.debug("Adding the string " + s + " to the expressionStringBuffer.");
			    continue;
			}

			if (o instanceof edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate) {
			    edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate predicate =
				(edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate) o;

			    StringBuffer sb = new StringBuffer();
			    sb.append(predicate.getName().toString());
			    sb.append("(");
			    int paramCount = predicate.getNumOfParams();
			    if(!(predicate.isStatic())) {
				i++;
				String variableName = (String)variableList.get(i);
				sb.append(variableName);
				if(paramCount > 0) {
				    sb.append(", ");
				}
				log.debug("adding the this parameter to the non-static predicate " + predicate.getName().toString());
				QuantifiedVariable qv = new QuantifiedVariable();
				qv.setName(variableName);
				qv.setType(predicate.getType().toString());
				quantification.addQuantifiedVariable(qv);
			    }
			    for (int j = i + 1; j < i + 1 + paramCount; j++) {
				Object variableNameObject = variableList.get(j);
				if (!(variableNameObject instanceof String)) {
				    log.warn("current variable name (variableNameObject) is not a String as expected, skipping it.");
				    continue;
				}
				String variableName = (String) variableNameObject;
				sb.append(variableName);

				// if this isn't the last variable, add a comma
				if (j < i + paramCount) {
				    sb.append(",");
				}

				String parameterType = predicate.getParamType(j).toString();
				log.debug("adding the variable " + variableName + " with type " + parameterType);

				QuantifiedVariable qv = new QuantifiedVariable();
				qv.setName(variableName);
				qv.setType(parameterType);
				quantification.addQuantifiedVariable(qv);
			    }
			    sb.append(")");

			    // skip ahead in the list
			    i += paramCount;

			    // add the predicate's expansion to the expression String
			    expressionStringBuffer.append(sb.toString());

			    continue;
			}

			log.debug("Not sure what to do with this type of object in the variableList: " + o.getClass().getName());
		    }

		    String finalExpression = expressionStringBuffer.toString().trim();

		    // create a new Predicate
		    Predicate newPredicate = new Predicate();
		    newPredicate.setName(parameterName);
		    newPredicate.setExpression(finalExpression);
		    temporalProperty.addPredicate(newPredicate);
		    
		    log.debug("Final expansion for " + parameterName + " = " + finalExpression);
		    formatExpansionHashtable.put(parameterName, finalExpression);
		}
	    }
	    else {
		log.error("variableMap is null.  Weird!");
	    }

	    PropertySummaryWizardPanel propertySummaryWP = new PropertySummaryWizardPanel();
	    propertySummaryWP.setWizardController(this);

	    propertySummaryWP.setPattern(currentPattern);
	    propertySummaryWP.setProperty(currentPattern.expandFormat(formatExpansionHashtable));
	    propertySummaryWP.setQuantification(quantification.getQuantificationString());

	    log.debug("returning a new PropertySummaryWizardPanel.");
	    return (propertySummaryWP);
	}

	if (currentWizardPanel instanceof PropertySummaryWizardPanel) {

	    // return the ModelReductionWizardPanel
	    ModelReductionWizardPanel modelReductionWP = new ModelReductionWizardPanel();
	    modelReductionWP.setWizardController(this);
	    return (modelReductionWP);
	}

	if (currentWizardPanel instanceof AssertionWizardPanel) {
	    AssertionWizardPanel assertionWP = (AssertionWizardPanel) currentWizardPanel;
	    Set activeAssertions = assertionWP.getActiveAssertions();
	    if((activeAssertions != null) && (activeAssertions.size() > 0)) {
		Specification specification = session.getSpecification();
		if(specification == null) {
		    specification = new Specification();
		    session.setSpecification(specification);
		}
		else {
		    // clear all existing assertions from the spec
		    specification.clearAssertions();
		}
		Iterator activeAssertionIterator = activeAssertions.iterator();
		while (activeAssertionIterator.hasNext()) {
		    String currentAssertionName = (String)activeAssertionIterator.next();
		    if ((currentAssertionName != null) &&
			(currentAssertionName.length() > 0) &&
			(!currentAssertionName.equals("None"))) {

			Assertion currentAssertion = new Assertion();
			currentAssertion.setName(currentAssertionName);
			currentAssertion.enable();
			log.debug("Adding an enabled assertion to the specification: " + currentAssertionName);
			specification.addAssertion(currentAssertion);
		    }
		}
	    }

	    // is there a better way to see if BSL was used to define predicates? -tcw
	    List predicates = parsePredicates(PredicateSet.getPredicateSetTable());
	    if ((temporalPropertyChecks)
		&& (predicates != null)
		&& (predicates.size() >= 1)) {

		log.debug("Since we have predicates.size() = " + predicates.size() +
			  ", we will work thru the temporal property portion of the wizard.");
		TemporalPropertyWizardPanel temporalPropertyWP = new TemporalPropertyWizardPanel();
		List patternList = new ArrayList();
		Hashtable patternNameTable = PatternSaverLoader.getPatternTable();
		if((patternNameTable != null) && (patternNameTable.size() > 0)) {
		    Iterator patternNameTableIterator = patternNameTable.keySet().iterator();
		    while(patternNameTableIterator.hasNext()) {
			String patternName = (String)patternNameTableIterator.next();
			Hashtable patternScopeTable = (Hashtable)patternNameTable.get(patternName);
			if((patternScopeTable != null) && (patternScopeTable.size() > 0)) {
			    Iterator patternScopeTableIterator = patternScopeTable.keySet().iterator();
			    while(patternScopeTableIterator.hasNext()) {
				String patternScope = (String)patternScopeTableIterator.next();
				Pattern pattern = (Pattern)patternScopeTable.get(patternScope);
				patternList.add(pattern);
			    }
			}
		    }
		}
		temporalPropertyWP.setPatternList(patternList);
		temporalPropertyWP.setWizardController(this);
		return (temporalPropertyWP);
	    }
	    else {

		// return the ModelReductionWizardPanel
		ModelReductionWizardPanel modelReductionWP = new ModelReductionWizardPanel();
		modelReductionWP.setWizardController(this);
		return (modelReductionWP);
	    }
	}

	if (currentWizardPanel instanceof ModelReductionWizardPanel) {
	    // return the SlicingWizardPanel
	    SlicingWizardPanel slicingWP = new SlicingWizardPanel();
	    slicingWP.setWizardController(this);
	    slicingWP.setSlicingEnabled(session.isSlicingEnabled());
	    return (slicingWP);
	}

	if (currentWizardPanel instanceof SlicingWizardPanel) {
	    SlicingWizardPanel slicingWP = (SlicingWizardPanel) currentWizardPanel;
	    boolean useSlicing = slicingWP.isSlicingEnabled();
	    session.setSlicingEnabled(useSlicing);

	    AbstractionIntroWizardPanel abstractionIntroWP =
		new AbstractionIntroWizardPanel();
	    abstractionIntroWP.setWizardController(this);
	    return (abstractionIntroWP);
	}

	if (currentWizardPanel instanceof AbstractionIntroWizardPanel) {
	    AbstractionIntroWizardPanel abstractionIntroWP =
		(AbstractionIntroWizardPanel) currentWizardPanel;

	    ModelCheckerChoiceWizardPanel modelCheckerChoiceWP =
		new ModelCheckerChoiceWizardPanel();
	    modelCheckerChoiceWP.setWizardController(this);
	    modelCheckerChoiceWP.registerCheckerChoice("None", -1, true);
	    modelCheckerChoiceWP.registerCheckerChoice("JPF", 0, Session.JPF_CHECKER_NAME_PROPERTY.equals(session.getProperty(Session.CHECKER_NAME_PROPERTY)));
	    modelCheckerChoiceWP.registerCheckerChoice("Spin", 1, Session.SPIN_CHECKER_NAME_PROPERTY.equals(session.getProperty(Session.CHECKER_NAME_PROPERTY)));
	    modelCheckerChoiceWP.registerCheckerChoice("DSpin", 2, Session.DSPIN_CHECKER_NAME_PROPERTY.equals(session.getProperty(Session.CHECKER_NAME_PROPERTY)));
	    //modelCheckerChoiceWP.registerCheckerChoice("HSF-Spin", 3, Session.HSF-SPIN_CHECKER_NAME_PROPERTY.equals(session.getProperty(Session.CHECKER_NAME_PROPERTY)));
	    //modelCheckerChoiceWP.registerCheckerChoice("SMV", 4, Session.SMV_CHECKER_NAME_PROPERTY.equals(session.getProperty(Session.CHECKER_NAME_PROPERTY)));
	    return (modelCheckerChoiceWP);

	}

	if (currentWizardPanel instanceof ModelCheckerChoiceWizardPanel) {

	    ModelCheckerChoiceWizardPanel modelCheckerChoiceWP =
		(ModelCheckerChoiceWizardPanel) currentWizardPanel;
	    int checkerChoice = modelCheckerChoiceWP.getCheckerChoice();
	    switch (checkerChoice) {
	    case -1 :
		session.setModelCheckingEnabled(false);
		CompletionWizardPanel completionWP = new CompletionWizardPanel();
		completionWP.setWizardController(this);
		return (completionWP);
	    case 0 :
		session.setProperty(Session.CHECKER_NAME_PROPERTY, Session.JPF_CHECKER_NAME_PROPERTY);
		session.setModelCheckingEnabled(true);
		JPFOptionWizardPanel jpfOptionWP = new JPFOptionWizardPanel();
		jpfOptionWP.setWizardController(this);
		jpfOptionWP.setJPFOptions(session.getProperty(Session.CHECKER_OPTIONS_PROPERTY));
		return (jpfOptionWP);

	    case 1 :
		session.setProperty(Session.CHECKER_NAME_PROPERTY, Session.SPIN_CHECKER_NAME_PROPERTY);
		session.setModelCheckingEnabled(true);
		SpinOptionWizardPanel spinOptionWP = new SpinOptionWizardPanel();
		spinOptionWP.setWizardController(this);
		spinOptionWP.setSpinOptions(session.getProperty(Session.CHECKER_OPTIONS_PROPERTY));
		return (spinOptionWP);

	    case 2 :
		session.setProperty(Session.CHECKER_NAME_PROPERTY, Session.DSPIN_CHECKER_NAME_PROPERTY);
		session.setModelCheckingEnabled(true);
		DSpinOptionWizardPanel dSpinOptionWP = new DSpinOptionWizardPanel();
		dSpinOptionWP.setWizardController(this);
		dSpinOptionWP.setDSpinOptions(session.getProperty(Session.CHECKER_OPTIONS_PROPERTY));
		return (dSpinOptionWP);

	    case 3 :
		//session.setProperty(Session.CHECKER_NAME_PROPERTY, Session.HSF-SPIN_CHECKER_NAME_PROPERTY);
		//session.setModelCheckingEnabled(true);
		//HSFSpinOptionWizardPanel hsfSpinOptionWP = new HSFSpinOptionWizardPanel();
		//hsfSpinOptionWP.setWizardController(this);
		//return(hsfSpinOptionWP);
		break;
	    case 4 :
		//session.setUseSMV(true);
		//session.setModelCheckingEnabled(true);
		//SMVOptionWizardPanel smvOptionWP = new SMVOptionWizardPanel();
		//smvOptionWP.setWizardController(this);
		//return(smvOptionWP);
		break;
	    }

	    return (null);

	}

	if (currentWizardPanel instanceof JPFOptionWizardPanel) {

	    JPFOptionWizardPanel jpfOptionWP = (JPFOptionWizardPanel) currentWizardPanel;
	    String jpfOptions = jpfOptionWP.getJPFOptions();
	    session.setProperty(Session.CHECKER_OPTIONS_PROPERTY, jpfOptions);

	    CompletionWizardPanel completionWP = new CompletionWizardPanel();
	    completionWP.setWizardController(this);
	    return (completionWP);
	}

	if (currentWizardPanel instanceof SpinOptionWizardPanel) {

	    SpinOptionWizardPanel spinOptionWP = (SpinOptionWizardPanel) currentWizardPanel;
	    String spinOptions = spinOptionWP.getSpinOptions();
	    session.setProperty(Session.CHECKER_OPTIONS_PROPERTY, spinOptions);

	    CompletionWizardPanel completionWP = new CompletionWizardPanel();
	    completionWP.setWizardController(this);
	    return (completionWP);
	}

	if (currentWizardPanel instanceof DSpinOptionWizardPanel) {

	    DSpinOptionWizardPanel dSpinOptionWP =
		(DSpinOptionWizardPanel) currentWizardPanel;
	    String dSpinOptions = dSpinOptionWP.getDSpinOptions();
	    session.setProperty(Session.CHECKER_OPTIONS_PROPERTY, dSpinOptions);

	    CompletionWizardPanel completionWP = new CompletionWizardPanel();
	    completionWP.setWizardController(this);
	    return (completionWP);
	}

	if (currentWizardPanel instanceof CompletionWizardPanel) {
	    return (null);
	}

	// if all else fails, return a null panel -> this should be an error panel. -tcw
	return (null);
    }

    /**
     * Test this wizard.  Create a new BanderaWizard, set the size of it, and
     * start it up.
     *
     * @param args an array of command-line arguments
     */
    public static void main(java.lang.String[] args) {
	
	BanderaWizard bw = new BanderaWizard();
	bw.setSize(new java.awt.Dimension(700, 500));
	//bw.pack();
	bw.setVisible(true);
	bw.start();
	
    }

    /**
     * Parse the Hashtable of Assertions defined in the source that was compiled.  This assumes
     * that each value in the Hashtable is an AssertionSet and that it will give us a Hashtable
     * of Assertions when requested (getAssertionTable()).
     *
     * @param Hashtable table A Hashtable of AssertionSet objects as values (don't care what the key is).
     * @return List A list of assertions (in String form) defined for the system.
     */
    private List parseAssertions(Hashtable table) {

	if(table == null) {
	    log.warn("table is null.  Cannot parse assertions from a null table.");
	    return(null);
	}

	List list = new ArrayList();

	Set keySet = table.keySet();
	Iterator iterator = keySet.iterator();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    Object value = table.get(key);
	    if(value instanceof AssertionSet) {
		AssertionSet set = (AssertionSet) value;
		Hashtable assertionTable = set.getAssertionTable();
		Set currentKeySet = assertionTable.keySet();
		Iterator currentIterator = currentKeySet.iterator();
		while(currentIterator.hasNext()) {
		    Object currentAssertionKey = currentIterator.next();
		    edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion currentAssertion =
			(edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion)assertionTable.get(currentAssertionKey);
		    String assertionName = currentAssertion.getName().toString();
		    log.debug("adding assertion " + assertionName + ".");
		    list.add(currentAssertion);
		}
	    }
	    else {
		// don't know what to do with this
		log.warn("Unhandled type while parsing the assertions.");
		log.warn("value.getClass().getName() = " + value.getClass().getName());
	    }
	}

	log.debug("complete assertion list size = " + list.size());
	return(list);
    }

    /**
     * This will parse out the list of files and directories in the classpath.  This
     * assumes that each entry is seperated by a "+" character.
     *
     * @return List A list of files and directories (in String form) that are in the classpath.
     * @param String classpath The classpath which to parse.
     */
    public List parseClasspath(String classpath) {
	
	if(classpath == null) {
	    return(null);
	}
	
	log.debug("parsing the classpath = " + classpath);
	
	ArrayList classpathList = new ArrayList();
	
	StringTokenizer st = new StringTokenizer(classpath, ";");
	while(st.hasMoreElements()) {
	    String currentEntry = (String)st.nextElement();
	    classpathList.add(currentEntry);
	}
	
	return(classpathList);
    }

    /**
     * Parse out the list of predicates from the table given.
     * 
     * @return List A flat list of Predicate objects that shows all the predicates that are available in the system.
     * @param Hashtable predicateSetTable The table of all predicates that should be parsed.
     */
    private List parsePredicates(Hashtable predicateSetTable) {

	if(predicateSetTable == null) {
	    log.warn("predicateSetTable is null.  Cannot parse a null table to find the predicates!  Returning a null value.");
	    return(null);
	}

	ArrayList predicateList = new ArrayList();
	for (Enumeration e = predicateSetTable.elements(); e.hasMoreElements();) {
	    for (Enumeration e2 = ((PredicateSet) e.nextElement()).getPredicateTable().elements(); e2.hasMoreElements();) {
		predicateList.add(e2.nextElement());
	    }
	}

	log.debug("Returning a predicate list with size " + predicateList.size());	
	return(predicateList);	
    }

    /**
     * Parse the given variable list into a List of String objects.
     * 
     * @return List A list of variable names (in String form) that are in the variables value given.
     * @param String variables A comma-seperated String with variable names.
     */
    private List parseVariables(String variables) {
	
	if(variables == null) {
	    return(null);
	}
	
	ArrayList variableList = new ArrayList();
	StringTokenizer st = new StringTokenizer(variables, ",");
	while(st.hasMoreElements()) {
	    String currentVariableName = (String)st.nextElement();
	    variableList.add(currentVariableName);
	}

	return(variableList);
    }

    /**
     * Start this wizard.
     */
    public void start() {

	if(wizard == null) {
	    return;
	}

	if(firstWizardPanel == null) {
	    return;
	}

	wizard.start(firstWizardPanel);
    }

    /**
     * Called when the wizard is cancelled.  This will do nothing!
     *
     * @param wizard the wizard that was cancelled.
     */
    public void wizardCancelled(edu.ksu.cis.bandera.bui.wizard.jwf.Wizard wizard) {
	setVisible(false);
    }

    /**
     * Called when the wizard finishes.  Before closing, we need to save the current
     * session file and then load up Bandera to use the newly generated session file.
     *
     * @param wizard the wizard that finished.
     */
    public void wizardFinished(edu.ksu.cis.bandera.bui.wizard.jwf.Wizard wizard) {

	log.debug("Wizard Finished.");
	setVisible(false);

	// load the current configured sessions and session into Bandera
	//  so the user can run it.
	try {
	    if(sessionManager.getSessionFile() == null) {
		log.debug("Saving the file as " + sessionFile.getPath());
		sessionManager.saveAs(sessionFile, true);
	    }
	    else {
		log.debug("Saving the file.");
		sessionManager.save();
	    }
	    log.debug("Finished saving the session file.");

	    // since BUI and the SessionManagerView use the shared instance of the session manager,
	    //  we can just modify it
	    SessionManager sharedSM = SessionManager.getInstance();
	    log.debug("Loading the session file that was just created ...");
	    sharedSM.load(sessionFile);
	}
	catch(Exception e) {
	    log.error("An exception occured while saving the session file.", e);
	    return;
	}

	log.debug("Wizard completed successfully.");
	    
    }

    /**
     * Called when a new panel has been displayed in the wizard.
     * @param wizard the wizard that was updated
     */
    public void wizardPanelChanged(edu.ksu.cis.bandera.bui.wizard.jwf.Wizard wizard) {
    }
}
