package edu.ksu.cis.bandera.bui;

import javax.swing.tree.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import edu.ksu.cis.bandera.bui.counterexample.*;
import edu.ksu.cis.bandera.bui.session.*;
import edu.ksu.cis.bandera.bui.session.datastructure.*;
import edu.ksu.cis.bandera.bui.dialog.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.gui.*;
import edu.ksu.cis.bandera.pdgslicer.dependency.*;

import ca.mcgill.sable.soot.SootClass;

import edu.ksu.cis.bandera.bui.SplashScreen;

import java.net.URL;

import edu.ksu.cis.bandera.bui.wizard.BanderaWizard;

import edu.ksu.cis.bandera.sessions.SessionManager;
import edu.ksu.cis.bandera.sessions.Session;

import edu.ksu.cis.bandera.sessions.ui.gui.SessionManagerView;
import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Category;

/**
 * BUI is the main Graphical User Interface for interacting with
 * Bandera.  It provides tools that allow a user to configure, run,
 * and interpret Bandera information.
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Yu Chen
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.12 $ - $Date: 2003/06/13 19:00:14 $
 */
public class BUI extends JFrame implements Observer {

    /**
     * The log that we will write messages to.
     */
    private static final Category log = Category.getInstance(BUI.class);

    public static BUI bui;
    private SessionManager sessionManager;
    public static Property property = new Property();
    private SessionManagerView sessionManagerView;
    static PatternManager patternManager;
    static PredicateBrowser predicateBrowser;
    static AssertionBrowser assertionBrowser;
    public static BUISessionPane sessionPane;
    static AbstractionLibraryManager abstractionLibraryManager;
    public static TypeGUI typeGUI;
    //public static SpinOption spinOption;
    public static IROptions irOptions;
    //static DSpinOption dSpinOption;
    //static JPFOption jpfOption;
    static boolean doJJJC = true;
    static boolean doSlicer = false;
    static boolean doSLABS = false;
    static boolean doChecker = false;
    public static boolean isExecuting = false;
    public static File currentDir = new File(System.getProperty("user.dir"));
	private JPanel ivjBUIContentPane = null;
	private JMenuBar ivjBUIJMenuBar = null;
	private JMenuItem ivjExitMenuItem = null;
	private JSeparator ivjJSeparator1 = null;
	private JButton ivjRunToolBarButton = null;
	private JButton ivjJ3CToolBarButton = null;
	private JButton ivjSlicerToolBarButton = null;
	private JButton ivjBircToolBarButton = null;
	private JButton ivjABPSToolBarButton = null;
	private JMenuItem ivjAbstractionManagerMenuItem = null;
	private JPanel ivjBottomPanel = null;
	private JMenuItem ivjAboutMenuItem = null;
	private JMenu ivjHelpMenu = null;
	private JButton ivjSeparator1ToolBarButton = null;
	private JSeparator ivjJSeparator4 = null;
	private JMenuItem ivjPatternManagerMenuItem = null;
	private JMenuItem ivjPredicateBrowserMenuItem = null;
	private JMenuItem ivjAssertionBrowserMenuItem = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JButton ivjAssertionToolBarButton = null;
	private JPanel ivjBUIToolbarPanel = null;
	private JButton ivjPatternToolBarButton = null;
	private JButton ivjPredicateToolBarButton = null;
	private JButton ivjSeparator1ToolBarButton111 = null;
	private JButton ivjSessionToolBarButton = null;
	private JButton ivjDependToolBarButton = null;
	private JButton ivjAbstractionToolBarButton = null;
	private JButton ivjSeparator1ToolBarButton1111 = null;
	private JLabel ivjJPFLabel = null;
	private JLabel ivjNuSMVLabel = null;
	private JLabel ivjSpinLabel = null;
	private JSeparator ivjJSeparator3 = null;
	private JMenuItem ivjAbstractionLibraryManagerMenuItem = null;
    // Added by MC 11/06/2001
    public static Driver currentDriver = null;
	private JLabel ivjCheckerNameLabel = null;
	private JPanel ivjCheckerNamePanel = null;
	private JLabel ivjCheckerNameTitleLabel = null;
	private JScrollPane ivjMessageWindowScrollPane = null;
	private JTextArea ivjMessageWindowTextArea = null;
	private JLabel ivjSessionNameLabel = null;
	private JPanel ivjSessionNamePanel = null;
	private JLabel ivjSessionNameTitleLabel = null;
    private BanderaWizard wizard;
	private JButton ivjSeparator1ToolBarButton112 = null;
	private JButton ivjWizardToolBarButton = null;
	private JMenuItem ivjLoadCounterExampleMenuItem = null;
	private JPanel ivjCheckerNameIconPanel = null;
	private JMenu ivjMainMenu = null;
	private JMenuItem ivjPDGMenuItem = null;
	private JMenuItem ivjPreferencesMenuItem = null;
	private JMenuItem ivjSessionManagerMenuItem = null;
	private JToolBar ivjSessionToolBar = null;
	private JPanel ivjSessionToolBarPanel = null;
	private JMenuItem ivjSessionWizardMenuItem = null;
	private JPanel ivjSplitPanePanel = null;
	private JMenu ivjToolsMenu = null;
	private JToolBar ivjToolsToolBar = null;
	private JPanel ivjToolsToolBarPanel = null;
	private JButton ivjSessionActivationButton = null;
	private JComboBox ivjSessionActivationComboBox = null;
	private JPanel ivjSessionActivationPanel = null;
	private java.awt.GridLayout ivjSplitPanePanelGridLayout = null;
    private java.util.Map checkerIconMap;
	private java.awt.GridLayout ivjCheckerNameIconPanelGridLayout = null;
    private static final Icon SLICER_ACTIVE_ICON =
	new javax.swing.ImageIcon(BUI.class.getResource("/edu/ksu/cis/bandera/bui/images/SlicerSelected24.gif"));
    private static final Icon SLICER_INACTIVE_ICON =
	new javax.swing.ImageIcon(BUI.class.getResource("/edu/ksu/cis/bandera/bui/images/Slicer24.gif"));
    private static final Icon ABSTRACTION_ACTIVE_ICON =
	new javax.swing.ImageIcon(BUI.class.getResource("/edu/ksu/cis/bandera/bui/images/SlabsSelected24.gif"));
    private static final Icon ABSTRACTION_INACTIVE_ICON =
	new javax.swing.ImageIcon(BUI.class.getResource("/edu/ksu/cis/bandera/bui/images/Slabs24.gif"));
    private static final Icon CHECKER_ACTIVE_ICON =
	new javax.swing.ImageIcon(BUI.class.getResource("/edu/ksu/cis/bandera/bui/images/CheckerSelected24.gif"));
    private static final Icon CHECKER_INACTIVE_ICON =
	new javax.swing.ImageIcon(BUI.class.getResource("/edu/ksu/cis/bandera/bui/images/Checker24.gif"));
	private JLabel ivjBanderaLogoLabel = null;
	private JMenuItem ivjSessionConverterMenuItem = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == BUI.this.getExitMenuItem()) 
				connEtoC2(e);
			if (e.getSource() == BUI.this.getSessionManagerMenuItem()) 
				connEtoC1(e);
			if (e.getSource() == BUI.this.getJ3CToolBarButton()) 
				connEtoC3(e);
			if (e.getSource() == BUI.this.getSlicerToolBarButton()) 
				connEtoC4(e);
			if (e.getSource() == BUI.this.getABPSToolBarButton()) 
				connEtoC5(e);
			if (e.getSource() == BUI.this.getBircToolBarButton()) 
				connEtoC6(e);
			if (e.getSource() == BUI.this.getRunToolBarButton()) 
				connEtoC7(e);
			if (e.getSource() == BUI.this.getPatternManagerMenuItem()) 
				connEtoC8(e);
			if (e.getSource() == BUI.this.getPredicateBrowserMenuItem()) 
				connEtoC13(e);
			if (e.getSource() == BUI.this.getAbstractionManagerMenuItem()) 
				connEtoC16(e);
			if (e.getSource() == BUI.this.getAboutMenuItem()) 
				connEtoC17(e);
			if (e.getSource() == BUI.this.getAssertionBrowserMenuItem()) 
				connEtoC18(e);
			if (e.getSource() == BUI.this.getDependToolBarButton()) 
				connEtoC28();
			if (e.getSource() == BUI.this.getPreferencesMenuItem()) 
				connEtoC29(e);
			if (e.getSource() == BUI.this.getAbstractionLibraryManagerMenuItem()) 
				connEtoC30(e);
			if (e.getSource() == BUI.this.getSessionWizardMenuItem()) 
				connEtoC31(e);
			if (e.getSource() == BUI.this.getLoadCounterExampleMenuItem()) 
				connEtoC33(e);
			if (e.getSource() == BUI.this.getAssertionToolBarButton()) 
				connEtoC11(e);
			if (e.getSource() == BUI.this.getSessionToolBarButton()) 
				connEtoC9(e);
			if (e.getSource() == BUI.this.getWizardToolBarButton()) 
				connEtoC10(e);
			if (e.getSource() == BUI.this.getPredicateToolBarButton()) 
				connEtoC12(e);
			if (e.getSource() == BUI.this.getPatternToolBarButton()) 
				connEtoC14(e);
			if (e.getSource() == BUI.this.getPDGMenuItem()) 
				connEtoC15(e);
			if (e.getSource() == BUI.this.getAbstractionToolBarButton()) 
				connEtoC19(e);
			if (e.getSource() == BUI.this.getSessionActivationButton()) 
				connEtoC20(e);
			if (e.getSource() == BUI.this.getSessionConverterMenuItem()) 
				connEtoC21(e);
		};
	};
    /**
     * BUI constructor comment.
     */
    public BUI() {
	super();
	initialize();
    }
    /**
     * Comment
     */
    public void aboutAction() {
	AboutBox box = new AboutBox();
	box.show();
    }
    /**
     * When the shortcut Abstraction button is pressed, toggle the
     * abstraction in the current active session.  This is only valid
     * when a session is active.
     */
    public void abstractionAction() {
	//Icon temp = getABPSToolBarButton().getPressedIcon();
	//getABPSToolBarButton().setPressedIcon(getABPSToolBarButton().getIcon());
	//getABPSToolBarButton().setIcon(temp);
	//doSLABS = !doSLABS;
	try {
	    Session activeSession = sessionManager.getActiveSession();
	    if(activeSession != null) {
		activeSession.setAbstractionEnabled(!activeSession.isAbstractionEnabled());
	    }
	    updateAbstractionButton();
	}
	catch (Exception e) {
	    System.err.println("Exception while trying to toggle the status of the abstraction flag: " + e.toString());
	}
    }
    /**
     * Comment
     */
    public void abstractionLibraryManagerAction() {
	abstractionLibraryManager.setVisible(true);
    }
    /**
     * When the user presses the typer menu item or button the
     * typer (abstraction manager) should be shown.
     */
    public void abstractionManagerAction() {
	typeGUI.setVisible(true);
    }
    /**
     * When the user presses the assertion browser menu item or
     * the assertion browser button, we should show the assertion
     * browser window.
     */
    public void assertionBrowserAction() {
	assertionBrowser.setVisible(true);
    }
    private void changeSessions(Sessions sessions)
    {
	changeSessions(sessions, true);
    }
    /**
     * 
     * @param sessions edu.ksu.cis.bandera.bui.session.Sessions
     */
    private void changeSessions(Sessions sessions, boolean showManager) {
	//this.sessions = sessions;
	CompilationManager.reset();
	PredicateSet.reset();
	AssertionSet.reset();

	sessionPane.updateLeftTree();
	
	/*
	  f = patternManager.isVisible();
	  patternManager.dispose();
	  patternManager.setVisible(f);
	*/

	PredicateSet.reset();
	AssertionSet.reset();

	assertionBrowser.updateTree();
	predicateBrowser.updateTree();	
	
	//jpfOption.setVisible(false);
	//jpfOption.dispose();
	//spinOption.setVisible(false);
	typeGUI.setFile(null);
    }
    /**
     * When the user presses the checker shortcut button, we should
     * toggle the checker option in the active session.  This is only
     * valid when there is an active session.
     */
    public void checkerAction() {
	//Icon temp = getBircToolBarButton().getPressedIcon();
	//getBircToolBarButton().setPressedIcon(getBircToolBarButton().getIcon());
	//getBircToolBarButton().setIcon(temp);
	//doChecker = !doChecker;
	try {
	    Session activeSession = sessionManager.getActiveSession();
	    if(activeSession != null) {
		activeSession.setModelCheckingEnabled(!activeSession.isModelCheckingEnabled());
	    }
	}
	catch (Exception e) {
	    System.err.println("An exception occured while trying to toggle the state of the checker: " + e.toString());
	}
    }
    /**
     * connEtoC1:  (SessionManagerMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.sessionManagerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.sessionManagerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC10:  (WizardToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.sessionWizardAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC10(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.sessionWizardAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC11:  (AssertionToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.assertionBrowserAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC11(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.assertionBrowserAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC12:  (PredicateToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.predicateBrowserAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC12(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.predicateBrowserAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC13:  (PredicateBrowserMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.predicateBrowserAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC13(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.predicateBrowserAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC14:  (PatternToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.patternManagerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC14(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.patternManagerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC15:  (PDGMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.pdgAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC15(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.pdgAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC16:  (AbstractionManagerMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.abstractionManagerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC16(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.abstractionManagerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC17:  (AboutMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.aboutAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC17(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.aboutAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC18:  (AssertionBrowserMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.assertionBrowserAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC18(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.assertionBrowserAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC19:  (AbstractionToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.abstractionManagerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC19(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.abstractionManagerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC2:  (ExitMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.exitAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.exitAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC20:  (SessionActivationButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.sessionActivationAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC20(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.sessionActivationAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
/**
 * connEtoC21:  (SessionConverterMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.sessionConverterAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC21(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.sessionConverterAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
    /**
     * connEtoC28:  (PDGToolBarButton.action. --> BUI.pDGToolBarButton_ActionEvents()V)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC28() {
	try {
	    // user code begin {1}
	    // user code end
	    this.pdgAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC29:  (PreferencesMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.preferencesAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC29(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.preferencesAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC3:  (J3CToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.jjjcAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.jjjcAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC30:  (AbstractionLibraryManagerMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.abstractionLibraryManagerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC30(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.abstractionLibraryManagerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC31:  (SessionWizardMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.sessionWizardAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC31(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.sessionWizardAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC33:  (LoadCounterExampleMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.loadCounterExampleAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC33(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.loadCounterExampleAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC4:  (SlicerToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.slicerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.slicerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC5:  (ABPSToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.abstractionAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.abstractionAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC6:  (BircToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.checkerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.checkerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC7:  (RunToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.runBanderaAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC7(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.runBanderaAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC8:  (PatternManagerMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.patternManagerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC8(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.patternManagerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC9:  (SessionToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> BUI.sessionManagerAction()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC9(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.sessionManagerAction();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * Comment
     */
    public void exitAction() {
	System.exit(0);
    }
    /**
     * Return the AboutMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getAboutMenuItem() {
	if (ivjAboutMenuItem == null) {
	    try {
		ivjAboutMenuItem = new javax.swing.JMenuItem();
		ivjAboutMenuItem.setName("AboutMenuItem");
		ivjAboutMenuItem.setMnemonic('a');
		ivjAboutMenuItem.setText("About");
		ivjAboutMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjAboutMenuItem;
    }
    /**
     * Return the ABPSToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getABPSToolBarButton() {
	if (ivjABPSToolBarButton == null) {
		try {
			ivjABPSToolBarButton = new javax.swing.JButton();
			ivjABPSToolBarButton.setName("ABPSToolBarButton");
			ivjABPSToolBarButton.setToolTipText("SLABS");
			ivjABPSToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjABPSToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjABPSToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/SlabsSelected24.gif")));
			ivjABPSToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
			ivjABPSToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjABPSToolBarButton.setText("  SLABS  ");
			ivjABPSToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjABPSToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
			ivjABPSToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjABPSToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Slabs24.gif")));
			ivjABPSToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
			ivjABPSToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
			ivjABPSToolBarButton.setBorderPainted(false);
			ivjABPSToolBarButton.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjABPSToolBarButton;
}
    /**
     * Return the AbstractionLibraryManagerMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getAbstractionLibraryManagerMenuItem() {
	if (ivjAbstractionLibraryManagerMenuItem == null) {
	    try {
		ivjAbstractionLibraryManagerMenuItem = new javax.swing.JMenuItem();
		ivjAbstractionLibraryManagerMenuItem.setName("AbstractionLibraryManagerMenuItem");
		ivjAbstractionLibraryManagerMenuItem.setMnemonic('a');
		ivjAbstractionLibraryManagerMenuItem.setText("Abstraction Library Manager");
		ivjAbstractionLibraryManagerMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjAbstractionLibraryManagerMenuItem;
    }
    /**
     * Return the AbstractionManagerMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getAbstractionManagerMenuItem() {
	if (ivjAbstractionManagerMenuItem == null) {
	    try {
		ivjAbstractionManagerMenuItem = new javax.swing.JMenuItem();
		ivjAbstractionManagerMenuItem.setName("AbstractionManagerMenuItem");
		ivjAbstractionManagerMenuItem.setMnemonic('t');
		ivjAbstractionManagerMenuItem.setText("Typer");
		ivjAbstractionManagerMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjAbstractionManagerMenuItem;
    }
    /**
     * Return the AbstractionToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getAbstractionToolBarButton() {
	if (ivjAbstractionToolBarButton == null) {
	    try {
		ivjAbstractionToolBarButton = new javax.swing.JButton();
		ivjAbstractionToolBarButton.setName("AbstractionToolBarButton");
		ivjAbstractionToolBarButton.setToolTipText("Type Inference");
		ivjAbstractionToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjAbstractionToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjAbstractionToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Abstraction24.gif")));
		ivjAbstractionToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
		ivjAbstractionToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
		ivjAbstractionToolBarButton.setText("  Typer   ");
		ivjAbstractionToolBarButton.setBackground(new java.awt.Color(204,204,255));
		ivjAbstractionToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
		ivjAbstractionToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjAbstractionToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Abstraction24.gif")));
		ivjAbstractionToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
		ivjAbstractionToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
		ivjAbstractionToolBarButton.setBorderPainted(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjAbstractionToolBarButton;
    }
    /**
     * Return the AssertionBrowserMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getAssertionBrowserMenuItem() {
	if (ivjAssertionBrowserMenuItem == null) {
	    try {
		ivjAssertionBrowserMenuItem = new javax.swing.JMenuItem();
		ivjAssertionBrowserMenuItem.setName("AssertionBrowserMenuItem");
		ivjAssertionBrowserMenuItem.setMnemonic('a');
		ivjAssertionBrowserMenuItem.setText("Assertion Browser");
		ivjAssertionBrowserMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjAssertionBrowserMenuItem;
    }
    /**
     * Return the AssertionToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getAssertionToolBarButton() {
	if (ivjAssertionToolBarButton == null) {
	    try {
		ivjAssertionToolBarButton = new javax.swing.JButton();
		ivjAssertionToolBarButton.setName("AssertionToolBarButton");
		ivjAssertionToolBarButton.setToolTipText("Assertion Browser");
		ivjAssertionToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjAssertionToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjAssertionToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Assertion24.gif")));
		ivjAssertionToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
		ivjAssertionToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
		ivjAssertionToolBarButton.setText("  Assertion  ");
		ivjAssertionToolBarButton.setBackground(new java.awt.Color(204,204,255));
		ivjAssertionToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
		ivjAssertionToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjAssertionToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Assertion24.gif")));
		ivjAssertionToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
		ivjAssertionToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
		ivjAssertionToolBarButton.setBorderPainted(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjAssertionToolBarButton;
    }
/**
 * Return the BanderaLogoLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getBanderaLogoLabel() {
	if (ivjBanderaLogoLabel == null) {
		try {
			ivjBanderaLogoLabel = new javax.swing.JLabel();
			ivjBanderaLogoLabel.setName("BanderaLogoLabel");
			ivjBanderaLogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/banderaFlagLogoSmall.gif")));
			ivjBanderaLogoLabel.setText("");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBanderaLogoLabel;
}
    /**
     * Return the BircToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getBircToolBarButton() {
	if (ivjBircToolBarButton == null) {
		try {
			ivjBircToolBarButton = new javax.swing.JButton();
			ivjBircToolBarButton.setName("BircToolBarButton");
			ivjBircToolBarButton.setToolTipText("Checker");
			ivjBircToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjBircToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjBircToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/CheckerSelected24.gif")));
			ivjBircToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
			ivjBircToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjBircToolBarButton.setText("  Checker  ");
			ivjBircToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjBircToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
			ivjBircToolBarButton.setDisabledIcon(null);
			ivjBircToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjBircToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Checker24.gif")));
			ivjBircToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
			ivjBircToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
			ivjBircToolBarButton.setBorderPainted(false);
			ivjBircToolBarButton.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBircToolBarButton;
}
    /**
     * Return the JPanel1 property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getBottomPanel() {
	if (ivjBottomPanel == null) {
	    try {
		ivjBottomPanel = new javax.swing.JPanel();
		ivjBottomPanel.setName("BottomPanel");
		ivjBottomPanel.setBorder(new javax.swing.border.EtchedBorder());
		ivjBottomPanel.setLayout(new java.awt.GridBagLayout());
		ivjBottomPanel.setBackground(new java.awt.Color(204,204,255));
		ivjBottomPanel.setMaximumSize(new java.awt.Dimension(152, 116));
		ivjBottomPanel.setPreferredSize(new java.awt.Dimension(152, 116));

		java.awt.GridBagConstraints constraintsMessageWindowScrollPane = new java.awt.GridBagConstraints();
		constraintsMessageWindowScrollPane.gridx = 2; constraintsMessageWindowScrollPane.gridy = 1;
		constraintsMessageWindowScrollPane.gridheight = 2;
		constraintsMessageWindowScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageWindowScrollPane.weightx = 0.9;
		constraintsMessageWindowScrollPane.weighty = 1.0;
		constraintsMessageWindowScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		getBottomPanel().add(getMessageWindowScrollPane(), constraintsMessageWindowScrollPane);

		java.awt.GridBagConstraints constraintsSessionNamePanel = new java.awt.GridBagConstraints();
		constraintsSessionNamePanel.gridx = 0; constraintsSessionNamePanel.gridy = 1;
		constraintsSessionNamePanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsSessionNamePanel.weightx = 0.1;
		constraintsSessionNamePanel.insets = new java.awt.Insets(4, 4, 4, 4);
		getBottomPanel().add(getSessionNamePanel(), constraintsSessionNamePanel);

		java.awt.GridBagConstraints constraintsCheckerNamePanel = new java.awt.GridBagConstraints();
		constraintsCheckerNamePanel.gridx = 0; constraintsCheckerNamePanel.gridy = 2;
		constraintsCheckerNamePanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsCheckerNamePanel.weightx = 0.1;
		constraintsCheckerNamePanel.weighty = 1.0;
		constraintsCheckerNamePanel.insets = new java.awt.Insets(4, 4, 4, 4);
		getBottomPanel().add(getCheckerNamePanel(), constraintsCheckerNamePanel);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjBottomPanel;
    }
    /**
     * Return the BUIContentPane property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getBUIContentPane() {
	if (ivjBUIContentPane == null) {
		try {
			ivjBUIContentPane = new javax.swing.JPanel();
			ivjBUIContentPane.setName("BUIContentPane");
			ivjBUIContentPane.setBorder(new javax.swing.border.EtchedBorder());
			ivjBUIContentPane.setLayout(new java.awt.GridBagLayout());
			ivjBUIContentPane.setBackground(new java.awt.Color(204,204,255));
			ivjBUIContentPane.setVisible(true);

			java.awt.GridBagConstraints constraintsBUIToolbarPanel = new java.awt.GridBagConstraints();
			constraintsBUIToolbarPanel.gridx = 0; constraintsBUIToolbarPanel.gridy = 1;
			constraintsBUIToolbarPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsBUIToolbarPanel.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsBUIToolbarPanel.weightx = 1.0;
			constraintsBUIToolbarPanel.weighty = 0.1;
			getBUIContentPane().add(getBUIToolbarPanel(), constraintsBUIToolbarPanel);

			java.awt.GridBagConstraints constraintsToolsToolBarPanel = new java.awt.GridBagConstraints();
			constraintsToolsToolBarPanel.gridx = 0; constraintsToolsToolBarPanel.gridy = 0;
			constraintsToolsToolBarPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsToolsToolBarPanel.anchor = java.awt.GridBagConstraints.NORTH;
			getBUIContentPane().add(getToolsToolBarPanel(), constraintsToolsToolBarPanel);

			java.awt.GridBagConstraints constraintsSessionToolBarPanel = new java.awt.GridBagConstraints();
			constraintsSessionToolBarPanel.gridx = 0; constraintsSessionToolBarPanel.gridy = 1;
			constraintsSessionToolBarPanel.gridwidth = 2;
			constraintsSessionToolBarPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSessionToolBarPanel.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsSessionToolBarPanel.weightx = 1.0;
			getBUIContentPane().add(getSessionToolBarPanel(), constraintsSessionToolBarPanel);

			java.awt.GridBagConstraints constraintsBottomPanel = new java.awt.GridBagConstraints();
			constraintsBottomPanel.gridx = 0; constraintsBottomPanel.gridy = 3;
			constraintsBottomPanel.gridwidth = 2;
			constraintsBottomPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsBottomPanel.weightx = 1.0;
			constraintsBottomPanel.weighty = 0.1;
			getBUIContentPane().add(getBottomPanel(), constraintsBottomPanel);

			java.awt.GridBagConstraints constraintsSplitPanePanel = new java.awt.GridBagConstraints();
			constraintsSplitPanePanel.gridx = 0; constraintsSplitPanePanel.gridy = 2;
			constraintsSplitPanePanel.gridwidth = 2;
			constraintsSplitPanePanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSplitPanePanel.weightx = 1.0;
			constraintsSplitPanePanel.weighty = 1.0;
			getBUIContentPane().add(getSplitPanePanel(), constraintsSplitPanePanel);

			java.awt.GridBagConstraints constraintsBanderaLogoLabel = new java.awt.GridBagConstraints();
			constraintsBanderaLogoLabel.gridx = 1; constraintsBanderaLogoLabel.gridy = 0;
			constraintsBanderaLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBUIContentPane().add(getBanderaLogoLabel(), constraintsBanderaLogoLabel);
			// user code begin {1}

		/*
		  java.awt.GridBagConstraints constraintsSessionPane = new java.awt.GridBagConstraints();
		  constraintsSessionPane.gridx = 0;
		  constraintsSessionPane.gridy = 1;
		  constraintsSessionPane.fill = java.awt.GridBagConstraints.BOTH;
		  constraintsSessionPane.anchor = java.awt.GridBagConstraints.CENTER;
		  constraintsSessionPane.weightx = 1.0;
		  constraintsSessionPane.weighty = 0.9;
		  getBUIContentPane().add(sessionPane, constraintsSessionPane);
		*/
			
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBUIContentPane;
}
    /**
     * Return the BUIJMenuBar property value.
     * @return javax.swing.JMenuBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuBar getBUIJMenuBar() {
	if (ivjBUIJMenuBar == null) {
	    try {
		ivjBUIJMenuBar = new javax.swing.JMenuBar();
		ivjBUIJMenuBar.setName("BUIJMenuBar");
		ivjBUIJMenuBar.setBorder(new javax.swing.border.EtchedBorder());
		ivjBUIJMenuBar.setBackground(new java.awt.Color(204,204,255));
		ivjBUIJMenuBar.add(getMainMenu());
		ivjBUIJMenuBar.add(getToolsMenu());
		ivjBUIJMenuBar.add(getHelpMenu());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjBUIJMenuBar;
    }
    /**
     * Return the BUIToolbarPanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getBUIToolbarPanel() {
	if (ivjBUIToolbarPanel == null) {
	    try {
		ivjBUIToolbarPanel = new javax.swing.JPanel();
		ivjBUIToolbarPanel.setName("BUIToolbarPanel");
		ivjBUIToolbarPanel.setLayout(new java.awt.GridBagLayout());
		ivjBUIToolbarPanel.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjBUIToolbarPanel;
    }
    /**
     * Get the JLabel that houses the checker icon for the checker that
     * matches the checker name given.  Null if we cannot find the appropriate
     * JLabel.
     *
     * @return javax.swing.JLabel
     * @param checkerName java.lang.String
     */
    private JLabel getCheckerIconLabel(String checkerName) {

	if(checkerIconMap == null) {
	    checkerIconMap = new HashMap();
	    checkerIconMap.put(Session.SPIN_CHECKER_NAME_PROPERTY, getSpinLabel());
	    checkerIconMap.put(Session.SMV_CHECKER_NAME_PROPERTY, getNuSMVLabel());
	    checkerIconMap.put(Session.JPF_CHECKER_NAME_PROPERTY, getJPFLabel());
	}

	JLabel checkerIconLabel = (JLabel)checkerIconMap.get(checkerName);
	return(checkerIconLabel);
    }
    /**
     * Return the CheckerNameIconPanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getCheckerNameIconPanel() {
	if (ivjCheckerNameIconPanel == null) {
	    try {
		ivjCheckerNameIconPanel = new javax.swing.JPanel();
		ivjCheckerNameIconPanel.setName("CheckerNameIconPanel");
		ivjCheckerNameIconPanel.setLayout(getCheckerNameIconPanelGridLayout());
		ivjCheckerNameIconPanel.setBackground(new java.awt.Color(204,204,255));
		ivjCheckerNameIconPanel.setMaximumSize(new java.awt.Dimension(50, 50));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjCheckerNameIconPanel;
    }
    /**
     * Return the CheckerNameIconPanelGridLayout property value.
     * @return java.awt.GridLayout
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private java.awt.GridLayout getCheckerNameIconPanelGridLayout() {
	java.awt.GridLayout ivjCheckerNameIconPanelGridLayout = null;
	try {
	    /* Create part */
	    ivjCheckerNameIconPanelGridLayout = new java.awt.GridLayout();
	    ivjCheckerNameIconPanelGridLayout.setColumns(1);
	} catch (java.lang.Throwable ivjExc) {
	    handleException(ivjExc);
	};
	return ivjCheckerNameIconPanelGridLayout;
    }
    /**
     * Return the CheckerNameLabel property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getCheckerNameLabel() {
	if (ivjCheckerNameLabel == null) {
	    try {
		ivjCheckerNameLabel = new javax.swing.JLabel();
		ivjCheckerNameLabel.setName("CheckerNameLabel");
		ivjCheckerNameLabel.setText("<Checker Name>");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjCheckerNameLabel;
    }
    /**
     * Return the CheckerNamePanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getCheckerNamePanel() {
	if (ivjCheckerNamePanel == null) {
	    try {
		ivjCheckerNamePanel = new javax.swing.JPanel();
		ivjCheckerNamePanel.setName("CheckerNamePanel");
		ivjCheckerNamePanel.setBorder(new javax.swing.border.EtchedBorder());
		ivjCheckerNamePanel.setLayout(new java.awt.GridBagLayout());
		ivjCheckerNamePanel.setBackground(new java.awt.Color(204,204,255));

		java.awt.GridBagConstraints constraintsCheckerNameTitleLabel = new java.awt.GridBagConstraints();
		constraintsCheckerNameTitleLabel.gridx = 1; constraintsCheckerNameTitleLabel.gridy = 0;
		constraintsCheckerNameTitleLabel.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraintsCheckerNameTitleLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		getCheckerNamePanel().add(getCheckerNameTitleLabel(), constraintsCheckerNameTitleLabel);

		java.awt.GridBagConstraints constraintsCheckerNameLabel = new java.awt.GridBagConstraints();
		constraintsCheckerNameLabel.gridx = 1; constraintsCheckerNameLabel.gridy = 1;
		constraintsCheckerNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		getCheckerNamePanel().add(getCheckerNameLabel(), constraintsCheckerNameLabel);

		java.awt.GridBagConstraints constraintsCheckerNameIconPanel = new java.awt.GridBagConstraints();
		constraintsCheckerNameIconPanel.gridx = 0; constraintsCheckerNameIconPanel.gridy = 0;
		constraintsCheckerNameIconPanel.gridheight = 2;
		constraintsCheckerNameIconPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsCheckerNameIconPanel.weightx = 1.0;
		constraintsCheckerNameIconPanel.weighty = 1.0;
		constraintsCheckerNameIconPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		getCheckerNamePanel().add(getCheckerNameIconPanel(), constraintsCheckerNameIconPanel);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjCheckerNamePanel;
    }
    /**
     * Return the CheckerNameTitleLabel property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getCheckerNameTitleLabel() {
	if (ivjCheckerNameTitleLabel == null) {
	    try {
		ivjCheckerNameTitleLabel = new javax.swing.JLabel();
		ivjCheckerNameTitleLabel.setName("CheckerNameTitleLabel");
		ivjCheckerNameTitleLabel.setText("Checker");
		ivjCheckerNameTitleLabel.setForeground(java.awt.Color.black);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjCheckerNameTitleLabel;
    }
    /**
     * Return the PDGToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public javax.swing.JButton getDependToolBarButton() {
	if (ivjDependToolBarButton == null) {
	    try {
		ivjDependToolBarButton = new javax.swing.JButton();
		ivjDependToolBarButton.setName("DependToolBarButton");
		ivjDependToolBarButton.setToolTipText("Program Dependency Browser");
		ivjDependToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjDependToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjDependToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/PDG24.gif")));
		ivjDependToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
		ivjDependToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
		ivjDependToolBarButton.setText("  PDG  ");
		ivjDependToolBarButton.setBackground(new java.awt.Color(204,204,255));
		ivjDependToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
		ivjDependToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjDependToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/PDG24.gif")));
		ivjDependToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
		ivjDependToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
		ivjDependToolBarButton.setBorderPainted(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjDependToolBarButton;
    }
    /**
     * Return the ExitMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getExitMenuItem() {
	if (ivjExitMenuItem == null) {
	    try {
		ivjExitMenuItem = new javax.swing.JMenuItem();
		ivjExitMenuItem.setName("ExitMenuItem");
		ivjExitMenuItem.setMnemonic('x');
		ivjExitMenuItem.setText("Exit");
		ivjExitMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjExitMenuItem;
    }
    /**
     * Return the HelpMenu property value.
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getHelpMenu() {
	if (ivjHelpMenu == null) {
	    try {
		ivjHelpMenu = new javax.swing.JMenu();
		ivjHelpMenu.setName("HelpMenu");
		ivjHelpMenu.setMnemonic('h');
		ivjHelpMenu.setText("Help");
		ivjHelpMenu.setBackground(new java.awt.Color(204,204,255));
		ivjHelpMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
		ivjHelpMenu.add(getAboutMenuItem());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjHelpMenu;
    }
    /**
     * Return the J3CToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getJ3CToolBarButton() {
	if (ivjJ3CToolBarButton == null) {
		try {
			ivjJ3CToolBarButton = new javax.swing.JButton();
			ivjJ3CToolBarButton.setName("J3CToolBarButton");
			ivjJ3CToolBarButton.setToolTipText("JJJC");
			ivjJ3CToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjJ3CToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjJ3CToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/JJJC24.gif")));
			ivjJ3CToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
			ivjJ3CToolBarButton.setRequestFocusEnabled(true);
			ivjJ3CToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjJ3CToolBarButton.setText("  JJJC  ");
			ivjJ3CToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjJ3CToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
			ivjJ3CToolBarButton.setDisabledIcon(null);
			ivjJ3CToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjJ3CToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/JJJCSelected24.gif")));
			ivjJ3CToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
			ivjJ3CToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
			ivjJ3CToolBarButton.setBorderPainted(false);
			ivjJ3CToolBarButton.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJ3CToolBarButton;
}
    /**
     * Return the JLabel2 property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public javax.swing.JLabel getJPFLabel() {
	if (ivjJPFLabel == null) {
	    try {
		ivjJPFLabel = new javax.swing.JLabel();
		ivjJPFLabel.setName("JPFLabel");
		ivjJPFLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/JPF2.gif")));
		ivjJPFLabel.setText("");
		ivjJPFLabel.setBounds(35, 554, 106, 74);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjJPFLabel;
    }
    /**
     * Return the JSeparator1 property value.
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator1() {
	if (ivjJSeparator1 == null) {
	    try {
		ivjJSeparator1 = new javax.swing.JSeparator();
		ivjJSeparator1.setName("JSeparator1");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjJSeparator1;
    }
    /**
     * Return the JSeparator3 property value.
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator3() {
	if (ivjJSeparator3 == null) {
	    try {
		ivjJSeparator3 = new javax.swing.JSeparator();
		ivjJSeparator3.setName("JSeparator3");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjJSeparator3;
    }
    /**
     * Return the JSeparator4 property value.
     * @return javax.swing.JSeparator
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSeparator getJSeparator4() {
	if (ivjJSeparator4 == null) {
	    try {
		ivjJSeparator4 = new javax.swing.JSeparator();
		ivjJSeparator4.setName("JSeparator4");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjJSeparator4;
    }
    /**
     * Return the LoadCounterExampleMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getLoadCounterExampleMenuItem() {
	if (ivjLoadCounterExampleMenuItem == null) {
	    try {
		ivjLoadCounterExampleMenuItem = new javax.swing.JMenuItem();
		ivjLoadCounterExampleMenuItem.setName("LoadCounterExampleMenuItem");
		ivjLoadCounterExampleMenuItem.setMnemonic('L');
		ivjLoadCounterExampleMenuItem.setText("Load Counter Example");
		ivjLoadCounterExampleMenuItem.setBackground(new java.awt.Color(204,204,255));
		ivjLoadCounterExampleMenuItem.setEnabled(true);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjLoadCounterExampleMenuItem;
    }
    /**
     * Return the MainMenu property value.
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getMainMenu() {
	if (ivjMainMenu == null) {
	    try {
		ivjMainMenu = new javax.swing.JMenu();
		ivjMainMenu.setName("MainMenu");
		ivjMainMenu.setText("Main");
		ivjMainMenu.setBackground(new java.awt.Color(204,204,255));
		ivjMainMenu.add(getPreferencesMenuItem());
		ivjMainMenu.add(getLoadCounterExampleMenuItem());
		ivjMainMenu.add(getExitMenuItem());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjMainMenu;
    }
    /**
     * Return the MessageWindowScrollPane property value.
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getMessageWindowScrollPane() {
	if (ivjMessageWindowScrollPane == null) {
	    try {
		ivjMessageWindowScrollPane = new javax.swing.JScrollPane();
		ivjMessageWindowScrollPane.setName("MessageWindowScrollPane");
		getMessageWindowScrollPane().setViewportView(getMessageWindowTextArea());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjMessageWindowScrollPane;
    }
    /**
     * Return the MessageWindowTextArea property value.
     * @return javax.swing.JTextArea
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextArea getMessageWindowTextArea() {
	if (ivjMessageWindowTextArea == null) {
	    try {
		ivjMessageWindowTextArea = new javax.swing.JTextArea();
		ivjMessageWindowTextArea.setName("MessageWindowTextArea");
		ivjMessageWindowTextArea.setBackground(new java.awt.Color(204,204,204));
		ivjMessageWindowTextArea.setBounds(0, 0, 744, 95);
		ivjMessageWindowTextArea.setEditable(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjMessageWindowTextArea;
    }
    /**
     * Return the JLabel3 property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public javax.swing.JLabel getNuSMVLabel() {
	if (ivjNuSMVLabel == null) {
	    try {
		ivjNuSMVLabel = new javax.swing.JLabel();
		ivjNuSMVLabel.setName("NuSMVLabel");
		ivjNuSMVLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/NuSMV.gif")));
		ivjNuSMVLabel.setText("");
		ivjNuSMVLabel.setBounds(18, 624, 106, 74);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjNuSMVLabel;
    }
    /**
     * Return the SpecificationPatternMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getPatternManagerMenuItem() {
	if (ivjPatternManagerMenuItem == null) {
	    try {
		ivjPatternManagerMenuItem = new javax.swing.JMenuItem();
		ivjPatternManagerMenuItem.setName("PatternManagerMenuItem");
		ivjPatternManagerMenuItem.setMnemonic('a');
		ivjPatternManagerMenuItem.setText("Pattern Manager");
		ivjPatternManagerMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPatternManagerMenuItem;
    }
    /**
     * Return the PatternToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getPatternToolBarButton() {
	if (ivjPatternToolBarButton == null) {
	    try {
		ivjPatternToolBarButton = new javax.swing.JButton();
		ivjPatternToolBarButton.setName("PatternToolBarButton");
		ivjPatternToolBarButton.setToolTipText("Pattern Manager");
		ivjPatternToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjPatternToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjPatternToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Pattern24.gif")));
		ivjPatternToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
		ivjPatternToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
		ivjPatternToolBarButton.setText("  Pattern  ");
		ivjPatternToolBarButton.setBackground(new java.awt.Color(204,204,255));
		ivjPatternToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
		ivjPatternToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjPatternToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Pattern24.gif")));
		ivjPatternToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
		ivjPatternToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
		ivjPatternToolBarButton.setBorderPainted(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPatternToolBarButton;
    }
    /**
     * Return the PDGMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getPDGMenuItem() {
	if (ivjPDGMenuItem == null) {
	    try {
		ivjPDGMenuItem = new javax.swing.JMenuItem();
		ivjPDGMenuItem.setName("PDGMenuItem");
		ivjPDGMenuItem.setText("PDG");
		ivjPDGMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPDGMenuItem;
    }
    /**
     * Return the PropositionMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getPredicateBrowserMenuItem() {
	if (ivjPredicateBrowserMenuItem == null) {
	    try {
		ivjPredicateBrowserMenuItem = new javax.swing.JMenuItem();
		ivjPredicateBrowserMenuItem.setName("PredicateBrowserMenuItem");
		ivjPredicateBrowserMenuItem.setMnemonic('r');
		ivjPredicateBrowserMenuItem.setText("Predicate Browser");
		ivjPredicateBrowserMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPredicateBrowserMenuItem;
    }
    /**
     * Return the PredicateToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getPredicateToolBarButton() {
	if (ivjPredicateToolBarButton == null) {
	    try {
		ivjPredicateToolBarButton = new javax.swing.JButton();
		ivjPredicateToolBarButton.setName("PredicateToolBarButton");
		ivjPredicateToolBarButton.setToolTipText("PredicateBrowser");
		ivjPredicateToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjPredicateToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjPredicateToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Predicate24.gif")));
		ivjPredicateToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
		ivjPredicateToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
		ivjPredicateToolBarButton.setText("  Predicate  ");
		ivjPredicateToolBarButton.setBackground(new java.awt.Color(204,204,255));
		ivjPredicateToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
		ivjPredicateToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjPredicateToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Predicate24.gif")));
		ivjPredicateToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
		ivjPredicateToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
		ivjPredicateToolBarButton.setBorderPainted(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPredicateToolBarButton;
    }
    /**
     * Return the IRMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getPreferencesMenuItem() {
	if (ivjPreferencesMenuItem == null) {
	    try {
		ivjPreferencesMenuItem = new javax.swing.JMenuItem();
		ivjPreferencesMenuItem.setName("PreferencesMenuItem");
		ivjPreferencesMenuItem.setMnemonic('g');
		ivjPreferencesMenuItem.setText("Preferences");
		ivjPreferencesMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPreferencesMenuItem;
    }
    /**
     * Return the RunToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getRunToolBarButton() {
	if (ivjRunToolBarButton == null) {
		try {
			ivjRunToolBarButton = new javax.swing.JButton();
			ivjRunToolBarButton.setName("RunToolBarButton");
			ivjRunToolBarButton.setToolTipText("Run");
			ivjRunToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjRunToolBarButton.setSelected(false);
			ivjRunToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjRunToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Run24.gif")));
			ivjRunToolBarButton.setRequestFocusEnabled(false);
			ivjRunToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
			ivjRunToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjRunToolBarButton.setText("  Run  ");
			ivjRunToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjRunToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
			ivjRunToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjRunToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Run24.gif")));
			ivjRunToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
			ivjRunToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
			ivjRunToolBarButton.setBorderPainted(false);
			ivjRunToolBarButton.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRunToolBarButton;
}
    /**
     * Return the Separator1ToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getSeparator1ToolBarButton() {
	if (ivjSeparator1ToolBarButton == null) {
		try {
			ivjSeparator1ToolBarButton = new javax.swing.JButton();
			ivjSeparator1ToolBarButton.setName("Separator1ToolBarButton");
			ivjSeparator1ToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjSeparator1ToolBarButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
			ivjSeparator1ToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjSeparator1ToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
			ivjSeparator1ToolBarButton.setMinimumSize(new java.awt.Dimension(20, 50));
			ivjSeparator1ToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjSeparator1ToolBarButton.setText("");
			ivjSeparator1ToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjSeparator1ToolBarButton.setMaximumSize(new java.awt.Dimension(20, 50));
			ivjSeparator1ToolBarButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
			ivjSeparator1ToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjSeparator1ToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
			ivjSeparator1ToolBarButton.setPreferredSize(new java.awt.Dimension(30, 50));
			ivjSeparator1ToolBarButton.setBorderPainted(false);
			ivjSeparator1ToolBarButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSeparator1ToolBarButton;
}
    /**
     * Return the Separator1ToolBarButton111 property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getSeparator1ToolBarButton111() {
	if (ivjSeparator1ToolBarButton111 == null) {
	    try {
		ivjSeparator1ToolBarButton111 = new javax.swing.JButton();
		ivjSeparator1ToolBarButton111.setName("Separator1ToolBarButton111");
		ivjSeparator1ToolBarButton111.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjSeparator1ToolBarButton111.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton111.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjSeparator1ToolBarButton111.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton111.setMinimumSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton111.setBorder(new javax.swing.border.CompoundBorder());
		ivjSeparator1ToolBarButton111.setText("");
		ivjSeparator1ToolBarButton111.setBackground(new java.awt.Color(204,204,255));
		ivjSeparator1ToolBarButton111.setMaximumSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton111.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton111.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjSeparator1ToolBarButton111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton111.setPreferredSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton111.setBorderPainted(false);
		ivjSeparator1ToolBarButton111.setEnabled(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSeparator1ToolBarButton111;
    }
    /**
     * Return the Separator1ToolBarButton1111 property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getSeparator1ToolBarButton1111() {
	if (ivjSeparator1ToolBarButton1111 == null) {
	    try {
		ivjSeparator1ToolBarButton1111 = new javax.swing.JButton();
		ivjSeparator1ToolBarButton1111.setName("Separator1ToolBarButton1111");
		ivjSeparator1ToolBarButton1111.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjSeparator1ToolBarButton1111.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton1111.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjSeparator1ToolBarButton1111.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton1111.setMinimumSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton1111.setBorder(new javax.swing.border.CompoundBorder());
		ivjSeparator1ToolBarButton1111.setText("");
		ivjSeparator1ToolBarButton1111.setBackground(new java.awt.Color(204,204,255));
		ivjSeparator1ToolBarButton1111.setMaximumSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton1111.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton1111.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjSeparator1ToolBarButton1111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton1111.setPreferredSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton1111.setBorderPainted(false);
		ivjSeparator1ToolBarButton1111.setEnabled(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSeparator1ToolBarButton1111;
    }
    /**
     * Return the Separator1ToolBarButton112 property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getSeparator1ToolBarButton112() {
	if (ivjSeparator1ToolBarButton112 == null) {
	    try {
		ivjSeparator1ToolBarButton112 = new javax.swing.JButton();
		ivjSeparator1ToolBarButton112.setName("Separator1ToolBarButton112");
		ivjSeparator1ToolBarButton112.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjSeparator1ToolBarButton112.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton112.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjSeparator1ToolBarButton112.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton112.setMinimumSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton112.setBorder(new javax.swing.border.CompoundBorder());
		ivjSeparator1ToolBarButton112.setText("");
		ivjSeparator1ToolBarButton112.setBackground(new java.awt.Color(204,204,255));
		ivjSeparator1ToolBarButton112.setMaximumSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton112.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton112.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjSeparator1ToolBarButton112.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/separator.gif")));
		ivjSeparator1ToolBarButton112.setPreferredSize(new java.awt.Dimension(20, 50));
		ivjSeparator1ToolBarButton112.setBorderPainted(false);
		ivjSeparator1ToolBarButton112.setEnabled(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSeparator1ToolBarButton112;
    }
    /**
     * Return the SessionActivationButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getSessionActivationButton() {
	if (ivjSessionActivationButton == null) {
	    try {
		ivjSessionActivationButton = new javax.swing.JButton();
		ivjSessionActivationButton.setName("SessionActivationButton");
		ivjSessionActivationButton.setText("Activate");
		ivjSessionActivationButton.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionActivationButton;
    }
    /**
     * Return the SessionActivationComboBox property value.
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getSessionActivationComboBox() {
	if (ivjSessionActivationComboBox == null) {
	    try {
		ivjSessionActivationComboBox = new javax.swing.JComboBox();
		ivjSessionActivationComboBox.setName("SessionActivationComboBox");
		ivjSessionActivationComboBox.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionActivationComboBox;
    }
    /**
     * Return the SessionActivationPanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getSessionActivationPanel() {
	if (ivjSessionActivationPanel == null) {
	    try {
		ivjSessionActivationPanel = new javax.swing.JPanel();
		ivjSessionActivationPanel.setName("SessionActivationPanel");
		ivjSessionActivationPanel.setLayout(new java.awt.GridBagLayout());
		ivjSessionActivationPanel.setBackground(new java.awt.Color(204,204,255));

		java.awt.GridBagConstraints constraintsSessionActivationComboBox = new java.awt.GridBagConstraints();
		constraintsSessionActivationComboBox.gridx = 0; constraintsSessionActivationComboBox.gridy = 0;
		constraintsSessionActivationComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsSessionActivationComboBox.weightx = 1.0;
		constraintsSessionActivationComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
		getSessionActivationPanel().add(getSessionActivationComboBox(), constraintsSessionActivationComboBox);

		java.awt.GridBagConstraints constraintsSessionActivationButton = new java.awt.GridBagConstraints();
		constraintsSessionActivationButton.gridx = 1; constraintsSessionActivationButton.gridy = 0;
		constraintsSessionActivationButton.insets = new java.awt.Insets(4, 4, 4, 4);
		getSessionActivationPanel().add(getSessionActivationButton(), constraintsSessionActivationButton);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionActivationPanel;
    }
/**
 * Return the SessionConverterMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getSessionConverterMenuItem() {
	if (ivjSessionConverterMenuItem == null) {
		try {
			ivjSessionConverterMenuItem = new javax.swing.JMenuItem();
			ivjSessionConverterMenuItem.setName("SessionConverterMenuItem");
			ivjSessionConverterMenuItem.setText("Session File Converter");
			ivjSessionConverterMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionConverterMenuItem;
}
    /**
     * Return the CloseMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getSessionManagerMenuItem() {
	if (ivjSessionManagerMenuItem == null) {
	    try {
		ivjSessionManagerMenuItem = new javax.swing.JMenuItem();
		ivjSessionManagerMenuItem.setName("SessionManagerMenuItem");
		ivjSessionManagerMenuItem.setMnemonic('m');
		ivjSessionManagerMenuItem.setText("Session Manager");
		ivjSessionManagerMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionManagerMenuItem;
    }
    /**
     * Return the SessionNameLabel property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getSessionNameLabel() {
	if (ivjSessionNameLabel == null) {
	    try {
		ivjSessionNameLabel = new javax.swing.JLabel();
		ivjSessionNameLabel.setName("SessionNameLabel");
		ivjSessionNameLabel.setText("<Session Name>");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionNameLabel;
    }
    /**
     * Return the SessionNamePanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getSessionNamePanel() {
	if (ivjSessionNamePanel == null) {
	    try {
		ivjSessionNamePanel = new javax.swing.JPanel();
		ivjSessionNamePanel.setName("SessionNamePanel");
		ivjSessionNamePanel.setBorder(new javax.swing.border.EtchedBorder());
		ivjSessionNamePanel.setLayout(new java.awt.GridBagLayout());
		ivjSessionNamePanel.setBackground(new java.awt.Color(204,204,255));

		java.awt.GridBagConstraints constraintsSessionNameTitleLabel = new java.awt.GridBagConstraints();
		constraintsSessionNameTitleLabel.gridx = 0; constraintsSessionNameTitleLabel.gridy = 0;
		constraintsSessionNameTitleLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		getSessionNamePanel().add(getSessionNameTitleLabel(), constraintsSessionNameTitleLabel);

		java.awt.GridBagConstraints constraintsSessionNameLabel = new java.awt.GridBagConstraints();
		constraintsSessionNameLabel.gridx = 1; constraintsSessionNameLabel.gridy = 0;
		constraintsSessionNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		getSessionNamePanel().add(getSessionNameLabel(), constraintsSessionNameLabel);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionNamePanel;
    }
    /**
     * Return the SessionNameTitleLabel property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getSessionNameTitleLabel() {
	if (ivjSessionNameTitleLabel == null) {
	    try {
		ivjSessionNameTitleLabel = new javax.swing.JLabel();
		ivjSessionNameTitleLabel.setName("SessionNameTitleLabel");
		ivjSessionNameTitleLabel.setText("Session");
		ivjSessionNameTitleLabel.setForeground(java.awt.Color.black);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionNameTitleLabel;
    }
    /**
     * Return the BUIToolBar property value.
     * @return javax.swing.JToolBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JToolBar getSessionToolBar() {
	if (ivjSessionToolBar == null) {
	    try {
		ivjSessionToolBar = new javax.swing.JToolBar();
		ivjSessionToolBar.setName("SessionToolBar");
		ivjSessionToolBar.setFloatable(false);
		ivjSessionToolBar.setBackground(new java.awt.Color(204,204,255));
		ivjSessionToolBar.add(getJ3CToolBarButton());
		ivjSessionToolBar.add(getSlicerToolBarButton());
		ivjSessionToolBar.add(getABPSToolBarButton());
		ivjSessionToolBar.add(getBircToolBarButton());
		getSessionToolBar().add(getSeparator1ToolBarButton(), getSeparator1ToolBarButton().getName());
		getSessionToolBar().add(getRunToolBarButton(), getRunToolBarButton().getName());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionToolBar;
    }
    /**
     * Return the SessionToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getSessionToolBarButton() {
	if (ivjSessionToolBarButton == null) {
	    try {
		ivjSessionToolBarButton = new javax.swing.JButton();
		ivjSessionToolBarButton.setName("SessionToolBarButton");
		ivjSessionToolBarButton.setToolTipText("Session Manager");
		ivjSessionToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjSessionToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjSessionToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Session24.gif")));
		ivjSessionToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
		ivjSessionToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
		ivjSessionToolBarButton.setText("  Session  ");
		ivjSessionToolBarButton.setBackground(new java.awt.Color(204,204,255));
		ivjSessionToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
		ivjSessionToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjSessionToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Session24.gif")));
		ivjSessionToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
		ivjSessionToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
		ivjSessionToolBarButton.setBorderPainted(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionToolBarButton;
    }
    /**
     * Return the BUIMiddleToolbarPanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getSessionToolBarPanel() {
	if (ivjSessionToolBarPanel == null) {
	    try {
		ivjSessionToolBarPanel = new javax.swing.JPanel();
		ivjSessionToolBarPanel.setName("SessionToolBarPanel");
		ivjSessionToolBarPanel.setBorder(new javax.swing.border.EtchedBorder());
		ivjSessionToolBarPanel.setLayout(new java.awt.GridLayout());
		ivjSessionToolBarPanel.setBackground(new java.awt.Color(204,204,255));
		ivjSessionToolBarPanel.add(getSessionActivationPanel());
		getSessionToolBarPanel().add(getSessionToolBar(), getSessionToolBar().getName());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionToolBarPanel;
    }
    /**
     * Return the WizardMenuItem property value.
     * @return javax.swing.JMenuItem
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenuItem getSessionWizardMenuItem() {
	if (ivjSessionWizardMenuItem == null) {
	    try {
		ivjSessionWizardMenuItem = new javax.swing.JMenuItem();
		ivjSessionWizardMenuItem.setName("SessionWizardMenuItem");
		ivjSessionWizardMenuItem.setMnemonic('W');
		ivjSessionWizardMenuItem.setText("Session Wizard");
		ivjSessionWizardMenuItem.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSessionWizardMenuItem;
    }
    /**
     * Return the SlicerToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getSlicerToolBarButton() {
	if (ivjSlicerToolBarButton == null) {
		try {
			ivjSlicerToolBarButton = new javax.swing.JButton();
			ivjSlicerToolBarButton.setName("SlicerToolBarButton");
			ivjSlicerToolBarButton.setToolTipText("Slicer");
			ivjSlicerToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjSlicerToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjSlicerToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/SlicerSelected24.gif")));
			ivjSlicerToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
			ivjSlicerToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjSlicerToolBarButton.setText("  Slicer  ");
			ivjSlicerToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjSlicerToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
			ivjSlicerToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjSlicerToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Slicer24.gif")));
			ivjSlicerToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
			ivjSlicerToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
			ivjSlicerToolBarButton.setBorderPainted(false);
			ivjSlicerToolBarButton.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSlicerToolBarButton;
}
    /**
     * Return the JLabel1 property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public javax.swing.JLabel getSpinLabel() {
	if (ivjSpinLabel == null) {
	    try {
		ivjSpinLabel = new javax.swing.JLabel();
		ivjSpinLabel.setName("SpinLabel");
		ivjSpinLabel.setText("");
		ivjSpinLabel.setMaximumSize(new java.awt.Dimension(50, 50));
		ivjSpinLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Spin.gif")));
		ivjSpinLabel.setPreferredSize(new java.awt.Dimension(50, 50));
		ivjSpinLabel.setBounds(19, 689, 106, 74);
		ivjSpinLabel.setMinimumSize(new java.awt.Dimension(50, 50));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSpinLabel;
    }
    /**
     * Return the SplitPanePanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getSplitPanePanel() {
	if (ivjSplitPanePanel == null) {
	    try {
		ivjSplitPanePanel = new javax.swing.JPanel();
		ivjSplitPanePanel.setName("SplitPanePanel");
		ivjSplitPanePanel.setLayout(getSplitPanePanelGridLayout());
		ivjSplitPanePanel.setBackground(java.awt.Color.white);
		ivjSplitPanePanel.setMaximumSize(new java.awt.Dimension(100000, 100000));
		// user code begin {1}
			
		// add the session pane into the main body of the BUI window
		ivjSplitPanePanel.add(sessionPane);
			
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjSplitPanePanel;
    }
    /**
     * Return the SplitPanePanelGridLayout property value.
     * @return java.awt.GridLayout
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private java.awt.GridLayout getSplitPanePanelGridLayout() {
	java.awt.GridLayout ivjSplitPanePanelGridLayout = null;
	try {
	    /* Create part */
	    ivjSplitPanePanelGridLayout = new java.awt.GridLayout();
	    ivjSplitPanePanelGridLayout.setColumns(1);
	} catch (java.lang.Throwable ivjExc) {
	    handleException(ivjExc);
	};
	return ivjSplitPanePanelGridLayout;
    }
    /**
     * Return the ToolsMenu property value.
     * @return javax.swing.JMenu
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JMenu getToolsMenu() {
	if (ivjToolsMenu == null) {
		try {
			ivjToolsMenu = new javax.swing.JMenu();
			ivjToolsMenu.setName("ToolsMenu");
			ivjToolsMenu.setText("Tools");
			ivjToolsMenu.setBackground(new java.awt.Color(204,204,255));
			ivjToolsMenu.add(getSessionManagerMenuItem());
			ivjToolsMenu.add(getSessionWizardMenuItem());
			ivjToolsMenu.add(getSessionConverterMenuItem());
			ivjToolsMenu.add(getJSeparator4());
			ivjToolsMenu.add(getAssertionBrowserMenuItem());
			ivjToolsMenu.add(getPredicateBrowserMenuItem());
			ivjToolsMenu.add(getPatternManagerMenuItem());
			ivjToolsMenu.add(getJSeparator1());
			ivjToolsMenu.add(getPDGMenuItem());
			ivjToolsMenu.add(getJSeparator3());
			ivjToolsMenu.add(getAbstractionManagerMenuItem());
			ivjToolsMenu.add(getAbstractionLibraryManagerMenuItem());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjToolsMenu;
}
    /**
     * Return the BUIMiddleToolBar property value.
     * @return javax.swing.JToolBar
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JToolBar getToolsToolBar() {
	if (ivjToolsToolBar == null) {
	    try {
		ivjToolsToolBar = new javax.swing.JToolBar();
		ivjToolsToolBar.setName("ToolsToolBar");
		ivjToolsToolBar.setFloatable(false);
		ivjToolsToolBar.setBackground(new java.awt.Color(204,204,255));
		getToolsToolBar().add(getSessionToolBarButton(), getSessionToolBarButton().getName());
		ivjToolsToolBar.add(getWizardToolBarButton());
		ivjToolsToolBar.add(getSeparator1ToolBarButton112());
		getToolsToolBar().add(getAssertionToolBarButton(), getAssertionToolBarButton().getName());
		getToolsToolBar().add(getPredicateToolBarButton(), getPredicateToolBarButton().getName());
		ivjToolsToolBar.add(getPatternToolBarButton());
		getToolsToolBar().add(getSeparator1ToolBarButton111(), getSeparator1ToolBarButton111().getName());
		getToolsToolBar().add(getDependToolBarButton(), getDependToolBarButton().getName());
		getToolsToolBar().add(getSeparator1ToolBarButton1111(), getSeparator1ToolBarButton1111().getName());
		getToolsToolBar().add(getAbstractionToolBarButton(), getAbstractionToolBarButton().getName());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjToolsToolBar;
    }
    /**
     * Return the BUITopToolbarPanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getToolsToolBarPanel() {
	if (ivjToolsToolBarPanel == null) {
	    try {
		ivjToolsToolBarPanel = new javax.swing.JPanel();
		ivjToolsToolBarPanel.setName("ToolsToolBarPanel");
		ivjToolsToolBarPanel.setBorder(new javax.swing.border.EtchedBorder());
		ivjToolsToolBarPanel.setLayout(new java.awt.GridLayout());
		ivjToolsToolBarPanel.setBackground(new java.awt.Color(204,204,255));
		getToolsToolBarPanel().add(getToolsToolBar(), getToolsToolBar().getName());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjToolsToolBarPanel;
    }
    /**
     * Get the Bandera wizard attribute (and initialize it if this is
     * the first use of it).
     *
     * @return edu.ksu.cis.bandera.bui.wizard.BanderaWizard
     */
    public BanderaWizard getWizard() {

	// lazy init of the wizard. -tcw
	if(wizard == null) {
	    System.out.println("Creating a new Bandera wizard ...");
	    wizard = new BanderaWizard();
	    wizard.setSize(new java.awt.Dimension(750, 600));
	    System.out.println("Bandera wizard created.");
	}
	
	return(wizard);
    }
    /**
     * Return the WizardToolBarButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getWizardToolBarButton() {
	if (ivjWizardToolBarButton == null) {
	    try {
		ivjWizardToolBarButton = new javax.swing.JButton();
		ivjWizardToolBarButton.setName("WizardToolBarButton");
		ivjWizardToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ivjWizardToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ivjWizardToolBarButton.setMinimumSize(new java.awt.Dimension(65, 50));
		ivjWizardToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
		ivjWizardToolBarButton.setText("Wizard");
		ivjWizardToolBarButton.setBackground(new java.awt.Color(204,204,255));
		ivjWizardToolBarButton.setMaximumSize(new java.awt.Dimension(65, 50));
		ivjWizardToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ivjWizardToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/wizard/images/wizardHat.gif")));
		ivjWizardToolBarButton.setPreferredSize(new java.awt.Dimension(65, 50));
		ivjWizardToolBarButton.setFont(new java.awt.Font("dialog", 0, 10));
		ivjWizardToolBarButton.setBorderPainted(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjWizardToolBarButton;
    }
    /**
     * Called whenever the part throws an exception.
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {
	log.error("--------- UNCAUGHT EXCEPTION ---------", exception);
    }
    /**
     * Initializes connections
     * @exception java.lang.Exception The exception description.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getExitMenuItem().addActionListener(ivjEventHandler);
	getSessionManagerMenuItem().addActionListener(ivjEventHandler);
	getJ3CToolBarButton().addActionListener(ivjEventHandler);
	getSlicerToolBarButton().addActionListener(ivjEventHandler);
	getABPSToolBarButton().addActionListener(ivjEventHandler);
	getBircToolBarButton().addActionListener(ivjEventHandler);
	getRunToolBarButton().addActionListener(ivjEventHandler);
	getPatternManagerMenuItem().addActionListener(ivjEventHandler);
	getPredicateBrowserMenuItem().addActionListener(ivjEventHandler);
	getAbstractionManagerMenuItem().addActionListener(ivjEventHandler);
	getAboutMenuItem().addActionListener(ivjEventHandler);
	getAssertionBrowserMenuItem().addActionListener(ivjEventHandler);
	getDependToolBarButton().addActionListener(ivjEventHandler);
	getPreferencesMenuItem().addActionListener(ivjEventHandler);
	getAbstractionLibraryManagerMenuItem().addActionListener(ivjEventHandler);
	getSessionWizardMenuItem().addActionListener(ivjEventHandler);
	getLoadCounterExampleMenuItem().addActionListener(ivjEventHandler);
	getAssertionToolBarButton().addActionListener(ivjEventHandler);
	getSessionToolBarButton().addActionListener(ivjEventHandler);
	getWizardToolBarButton().addActionListener(ivjEventHandler);
	getPredicateToolBarButton().addActionListener(ivjEventHandler);
	getPatternToolBarButton().addActionListener(ivjEventHandler);
	getPDGMenuItem().addActionListener(ivjEventHandler);
	getAbstractionToolBarButton().addActionListener(ivjEventHandler);
	getSessionActivationButton().addActionListener(ivjEventHandler);
	getSessionConverterMenuItem().addActionListener(ivjEventHandler);
}
    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
	try {
	    // user code begin {1}
	    IconLibrary.initialize();
	    FileChooser.initialize();
	    bui = this;

	    // this gets created only when needed (in sessionManagerAction()) -tcw
	    //sessionManagerView = new SessionManagerView();
	    //sessionManagerView.pack();
	    //sessionManagerView.setVisible(false);

	    sessionManager = SessionManager.getInstance();
	    sessionManager.addObserver(this);

	    patternManager = new PatternManager();
	    predicateBrowser = new PredicateBrowser();
	    assertionBrowser = new AssertionBrowser();
	    sessionPane = new BUISessionPane();
	    abstractionLibraryManager = new AbstractionLibraryManager();
	    typeGUI = new TypeGUI();
	    irOptions = new IROptions();

	    // this is no longer necessary since it is in the SessionManagerView. -tcw
	    //spinOption = new SpinOption();
	    //dSpinOption = new DSpinOption();
	    //jpfOption = new JPFOption();
	    //spinOption.pack();
	    //dSpinOption.pack();
	    //jpfOption.pack();

	    bui.pack();
	    patternManager.pack();
	    predicateBrowser.pack();
	    assertionBrowser.pack();
	    abstractionLibraryManager.pack();
	    typeGUI.pack();
	    // user code end
	    setName("BUI");
	    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	    setJMenuBar(getBUIJMenuBar());
	    setSize(760, 460);
	    setTitle("Bandera");
	    setContentPane(getBUIContentPane());
	    initConnections();
	} catch (java.lang.Throwable ivjExc) {
	    handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
    }
    /**
     * When the JJJC button is pressed, we will toggle the
     * JJJC setting.
     */
    public void jjjcAction() {
	Icon temp = getJ3CToolBarButton().getPressedIcon();
	getJ3CToolBarButton().setPressedIcon(getJ3CToolBarButton().getIcon());
	getJ3CToolBarButton().setIcon(temp);
	doJJJC = !doJJJC;
    }
    /**
     * Loading counter example
     */
    public void loadCounterExampleAction() {
	// TO DO: Warn users before loading

	/*
	  LinkedList l = new LinkedList();
	  l.add("*.cet");
	  JFileChooser fc = FileDialogFactory.create(l,"Counter example files");
	  File fn = FileDialogFactory.display(fc, this, "Open");
	  Trail trail = CounterExampleSaverLoader.load(fn.getAbsolutePath());

	  String sessionFile = trail.getSessionFile();
	  String sessionName = trail.getSessionName();

	  try {
	  File selectedFile = new File(sessionFile);
	  selectedFile = selectedFile.getParentFile();
	  if (selectedFile != null && selectedFile.isDirectory()) currentDir = selectedFile;
	  } catch (Exception e)
	  {
	  e.printStackTrace();
	  JOptionPane.showOptionDialog(this, "Cannot open the counter example file:\n"+e.getMessage(),
	  "Error!", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[] {"OK"}, "OK");
	  return;
	  }

	  if (isExecuting) {
	  JOptionPane.showMessageDialog(this, "Please wait until current execution is finished", "Information", JOptionPane.INFORMATION_MESSAGE);
	  return;
	  }
	  isExecuting = true;
	  currentDriver = new Driver(trail);
	  SessionManager sm = SessionManager.getInstance();
	  currentDriver.init(sm.getActiveSession(), false, false, ".");
	  currentDriver.start();
	*/
    }
    /**
     * main entrypoint - starts the part when it is run as an application
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
	
	SplashScreen splashScreen = null;

	try {
	    /* This filename should be configurable outside this method:
	     * 1) public static final String
	     * 2) from property
	     * 3) from command line argument
	     * 4) ???
	     */
	    String imageFilename = "edu/ksu/cis/bandera/bui/images/santos-logo.gif";
	    URL url = ClassLoader.getSystemClassLoader().getResource(imageFilename);
        
	    if(url != null) {
        	splashScreen = new SplashScreen(url);
	    }
        
	    if(splashScreen != null) {
        	splashScreen.setVisible(true);
	    }
	}
	catch (Exception e) {
	    System.err.println("SplashScreen loading caused an exception: " + e.toString());
	}

	try {
	    BUI aBUI;
	    edu.ksu.cis.bandera.util.Preferences.load();
	    //edu.ksu.cis.bandera.util.Logger.on();
	    aBUI = new BUI();
	    aBUI.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent e) {
			//edu.ksu.cis.bandera.util.Logger.off();
			edu.ksu.cis.bandera.util.Preferences.save();
			System.exit(0);
		    };
		});

	    // show the Bandera UI
	    aBUI.setVisible(true);

	}
	catch (Throwable exception) {
	    System.err.println("Exception occurred in main() of javax.swing.JFrame");
	    exception.printStackTrace(System.out);
	}
    
	// hide the splashScreen
	if (splashScreen != null) {
	    splashScreen.setVisible(false);
	}

    }
    /**
     * When the user presses the pattern manager menu item or button
     * the pattern manager window should be shown.
     */
    public void patternManagerAction() {
	patternManager.setVisible(true);
    }
    /**
     * When the user presses the PDG menu item or button is pressed
     * the pdg browser should be shown.
     */
    public void pdgAction() {
	if (Dependencies.dependFrame != null)
	    Dependencies.dependFrame.dispose();
		
	Dependencies.dependFrame = null;
	Dependencies.dependFrame = new Dependencies();
	Dependencies.dependFrame.show();
	/*else
	  if (!Dependencies.dependFrame.isVisible())
	  Dependencies.dependFrame.setVisible(true);*/
	/*
	  Object leftTreeUsrObj = null;
	  DefaultMutableTreeNode leftTreeSelectedNode = (DefaultMutableTreeNode) BUI.sessionPane.getLeftTree().getLastSelectedPathComponent();
	  if (leftTreeSelectedNode != null)
	  leftTreeUsrObj = ((DefaultMutableTreeNode) leftTreeSelectedNode).getUserObject();
	  Object rightTreeUsrObj = null;
	  Object rightTreeSelectedNode = BUI.sessionPane.getRightTree().getLastSelectedPathComponent();
	  if (rightTreeSelectedNode != null)
	  rightTreeUsrObj = ((DefaultMutableTreeNode) rightTreeSelectedNode).getUserObject();
	  DependencyFrame dependFrame = new DependencyFrame(leftTreeUsrObj, rightTreeUsrObj);
	  dependFrame.show();
	*/
    }
    /**
     * When the predicate browser button or predicate browser menu
     * item is pressed, we should show the predicate browser window.
     */
    public void predicateBrowserAction() {
	predicateBrowser.setVisible(true);
    }
    /**
     * When the user presses the preferences menu item, we should display
     * the preferences (IR Options) window.
     */
    public void preferencesAction() {
	irOptions.setVisible(true);
    }
    /**
     * Add a new message to the message window.
     *
     * @param String message A new message to be added to the message window.
     */
    public void printMessage(String message) {
	JTextArea messageWindowTextArea = getMessageWindowTextArea();
	messageWindowTextArea.append(message + "\n");

	// now make it scroll to the bottom of the window -tcw
	// this is probably not the best way to go about it but I wanted to make sure this
	//  worked before the 0.2 release.  in other words, this is a HACK! -tcw
	try {
	    int pos = messageWindowTextArea.getLineEndOffset(messageWindowTextArea.getLineCount() - 1);
	    messageWindowTextArea.setCaretPosition(pos);
	}
	catch (Exception e) {
	    System.err.println("Exception caught while scrolling.  Exception = " + e.toString());
	}
    }
    /**
     * When the user presses the run button we should start bandera
     * using the currently configured active session.  This is only valid if
     * there is an active session.
     */
    public void runBanderaAction() {
	if (isExecuting) {
	    JOptionPane.showMessageDialog(this, "Please wait until current execution is finished", "Information", JOptionPane.INFORMATION_MESSAGE);
	    return;
	}
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
	    isExecuting = true;
	    currentDriver = new Driver();
	    String expectedPath = ".";
	    File workingDirectory = activeSession.getWorkingDirectory();
	    if(workingDirectory != null) {
		expectedPath = workingDirectory.toString();
	    }
	    currentDriver.init(activeSession, false, false, expectedPath);
	    currentDriver.start();
	}
	else {
	    printMessage("No session is active.  Therefore, there is nothing to run.  Please activate a session.");
	}
    }
    /**
     * When the activate button is pressed, get the session name selected
     * in the session combo box and activate the corresponding session.
     */
    public void sessionActivationAction() {

	JComboBox sessionComboBox = getSessionActivationComboBox();
	Object selectedItem = sessionComboBox.getSelectedItem();
	if(selectedItem != null) {
	    String sessionName = selectedItem.toString();
	    SessionManager sm = SessionManager.getInstance();
	    try {
		sm.setActiveSession(sessionName);
	    }
	    catch(Exception e) {
		printMessage("An exception occured while activating the session: " +
			     sessionName + ".  Exception: " + e.toString());
	    }
	}
	else {
	    // no item selected, report the problem to the user
	}
    }

/**
 * When the session converter menu item is pressed, we should display
 * the session converter view and allow the user to convert session
 * files from one format to another.
 */
public void sessionConverterAction() {

	edu.ksu.cis.bandera.sessions.ui.gui.SessionFileConverterView sfcv =
		new edu.ksu.cis.bandera.sessions.ui.gui.SessionFileConverterView();
	sfcv.setVisible(true);
	
}

    /**
     * When the user selects the session manager button or menu item, we
     * should show the session manager window.
     */
    public void sessionManagerAction() {
	if(sessionManagerView == null) {
	    sessionManagerView = new SessionManagerView();
	}
	sessionManagerView.pack();
	sessionManagerView.setVisible(true);
    }

    /**
     * When the session model (or rather the session manager) has changed, we need to
     * update a few components in BUI:
     * <ul>
     *   <li>Session name label - should match the active session name</li>
     *   <li>Checker name label - should match the checker defined in the active session<li>
     *   <li>Checker name icon - should find the appropriate checker icon that matches the checker defined
     *       in the active session</li>
     *   <li>Session names combo box - should match the list of sessions in the session manager.</li>
     *   <li>JJJC button enabled - should be reset to enabled since we will need to compile the new application.</li>
     *   <li>Slicer button enabled - should match the enabled flag for the slicer in the active session.</li>
     *   <li>Abstraction (SLABS) button enabled - should match the enabled flag for abstraction in the active session.</li>
     *   <li>Checker button enabled - should match the enabled flag for the checker in the active session.</li>
     *   <li>Reset Compilation Manager - should be empty since we have yet to compile the new application.</li>
     *   <li>Reset the Predicate Set - should be empty since we have yet to compile the BSL statements in the new application.</li>
     *   <li>Reset the Assertion Set - should be empty since we have yet to compile the BSL statements in the new application.</li>
     *   <li>Update Session Pane left side tree - should be empty since we have yet to compile the new application.</li>
     *   <li>Update the Assertion Browser tree - should be empty since we have yet to compile the BSL statements in the new application.</li>
     *   <li>Update the Predicate Browser tree - should be empty since we have yet to compile the BSL statements in the new application.</li>
     * </ul>
     */
    private void sessionModelChanged() {

	// doesn't look like we need to do this! -tcw	
	//CompilationManager.reset();
	//PredicateSet.reset();
	//AssertionSet.reset();

	sessionPane.updateLeftTree();
	assertionBrowser.updateTree();
	predicateBrowser.updateTree();

	SessionManager sm = SessionManager.getInstance();

	JComboBox sessionNameComboBox = getSessionActivationComboBox();
	sessionNameComboBox.removeAllItems();
	Map sessionMap = sm.getSessions();
	if((sessionMap != null) && (sessionMap.size() > 0)) {
	    Iterator sni = sessionMap.keySet().iterator();
	    while(sni.hasNext()) {
		sessionNameComboBox.addItem(sni.next());
	    }
	}
	else {
	    sessionNameComboBox.addItem("No sessions available.");
	}
	
	Session activeSession = sm.getActiveSession();
	if(activeSession != null) {

	    sessionNameComboBox.setSelectedItem(activeSession.getName());
		
	    JLabel sessionNameLabel = getSessionNameLabel();
	    sessionNameLabel.setText(activeSession.getName());

	    String checkerName = activeSession.getProperty(Session.CHECKER_NAME_PROPERTY);
	    JLabel checkerNameLabel = getCheckerNameLabel();
	    checkerNameLabel.setText(checkerName);

	    JLabel checkerNameIconLabel = getCheckerIconLabel(checkerName);
	    if(checkerNameIconLabel != null) {
		JPanel checkerNameIconPanel = getCheckerNameIconPanel();
		checkerNameIconPanel.removeAll();
		checkerNameIconPanel.add(checkerNameIconLabel);
	    }

	    JButton jjjcButton = getJ3CToolBarButton();
	    jjjcButton.setSelected(true);

	    updateSlicerButton();
	    updateAbstractionButton();
	    updateCheckerButton();
	}
	else {
	    JLabel sessionNameLabel = getSessionNameLabel();
	    sessionNameLabel.setText("No Session Active");

	    String checkerName = "No Checker Selected";
	    JLabel checkerNameLabel = getCheckerNameLabel();
	    checkerNameLabel.setText(checkerName);

	    JPanel checkerNameIconPanel = getCheckerNameIconPanel();
	    checkerNameIconPanel.removeAll();

	    JButton jjjcButton = getJ3CToolBarButton();
	    jjjcButton.setSelected(false);

	    updateSlicerButton();
	    updateAbstractionButton();
	    updateCheckerButton();
	}
	
    }
    /**
     * Open up the Bandera Wizard window and disable the rest of the GUI until
     * the user has completed the Wizard (or cancelled it).
     */
    public void sessionWizardAction() {

	log.debug("Starting the session wizard...");

	try {
	    BanderaWizard bw = getWizard();
	    if(bw == null) {
		log.error("For some reason the bandera wizard could not be created.");
	    }
	    else {
		bw.setVisible(true);
		bw.start();
	    }
	}
	catch(Exception e) {
	    log.error("Unable to create and start a Bandera Wizard because of an exception.", e);
	}
    }
    /**
     * Set the name of the checker that is currently being used.
     *
     * @param String checkerName The name of the checker currently in use.  Ex. JPF, Spin, dSpin, SMV
     */
    public void setCheckerName(String checkerName) {
	JLabel checkerNameLabel = getCheckerNameLabel();
	checkerNameLabel.setText(checkerName);
    }
    /**
     * Set the name of the currently active session.
     *
     * @param String sessionName The name of the currently active (or seleted) session.
     */
    public void setSessionName(String sessionName) {
	JLabel sessionNameLabel = getSessionNameLabel();
	sessionNameLabel.setText(sessionName);
    }
    /**
     * Insert the method's description here.
     * Creation date: (6/10/2002 2:58:42 PM)
     * @param newWizard edu.ksu.cis.bandera.bui.wizard.BanderaWizard
     */
    void setWizard(BanderaWizard newWizard) {
	wizard = newWizard;
    }
    /**
     * When the slicing shortcut button is pressed, we should toggle
     * slicing in the current active session.  This action is only
     * valid when a session is active.
     */
    public void slicerAction() {
	//Icon temp = getSlicerToolBarButton().getPressedIcon();
	//getSlicerToolBarButton().setPressedIcon(getSlicerToolBarButton().getIcon());
	//getSlicerToolBarButton().setIcon(temp);
	//doSlicer = !doSlicer;
	
	try {
	    Session activeSession = sessionManager.getActiveSession();
	    if(activeSession != null) {
		activeSession.setSlicingEnabled(!activeSession.isSlicingEnabled());
	    }
	    updateSlicerButton();
	}
	catch (Exception e) {
	    System.err.println("An exception occured while trying to toggle the state of the slicer: " + e.toString());
	}
    }
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param   o     the observable object.
     * @param   arg   an argument passed to the <code>notifyObservers</code>
     *                 method.
     */
    public void update(Observable o, Object arg) {

	if(o == null) {
	    log.warn("Update was called with a null Observable ... this is odd.");
	}
	else if(o instanceof SessionManager) {
	    log.debug("Update called with a SessionManager.  Updating the view with the new model data.");
	    sessionModelChanged();
	}
	else {
	    log.debug("Update was called with an unknown type of Observable: " + o.getClass().getName());
	}

    }
    /**
     * Make sure the Abstraction/Slabs button's state is the same as the active session's state.  If
     * the active session has abstraction enabled, the button should be selected and use the
     * ABSTRACTION_ACTIVE_ICON.  Otherwise, the button should not be selected and use the
     * ABSTRACTION_INACTIVE_ICON.
     */
    private void updateAbstractionButton() {
	JButton slabsButton = getABPSToolBarButton();
	Session activeSession = sessionManager.getActiveSession();
	if((activeSession != null) && (activeSession.isAbstractionEnabled())) {
	    slabsButton.setSelected(true);
	    slabsButton.setIcon(ABSTRACTION_ACTIVE_ICON);
	}
	else {
	    slabsButton.setSelected(false);
	    slabsButton.setIcon(ABSTRACTION_INACTIVE_ICON);
	}
    }
    /**
     * Make sure the Model checker button's state is the same as the active session's state.  If
     * the active session has model checking enabled, the button should be selected and use the
     * CHECKER_ACTIVE_ICON.  Otherwise, the button should not be selected and use the
     * CHECKER_INACTIVE_ICON.
     */
    private void updateCheckerButton() {
	JButton checkerButton = getBircToolBarButton();
	Session activeSession = sessionManager.getActiveSession();
	if((activeSession != null) && (activeSession.isModelCheckingEnabled())) {
	    checkerButton.setSelected(true);
	    checkerButton.setIcon(CHECKER_ACTIVE_ICON);
	}
	else {
	    checkerButton.setSelected(false);
	    checkerButton.setIcon(CHECKER_INACTIVE_ICON);
	}
    }
    /**
     * Make sure the Slicer button's state is the same as the active session's state.  If
     * the active session has slicing enabled, the button should be selected and use the
     * SLICER_ACTIVE_ICON.  Otherwise, the button should not be selected and use the
     * SLICER_INACTIVE_ICON.
     */
    private void updateSlicerButton() {
	JButton slicerButton = getSlicerToolBarButton();
	Session activeSession = sessionManager.getActiveSession();
	if((activeSession != null) && (activeSession.isSlicingEnabled())) {
	    slicerButton.setSelected(true);
	    slicerButton.setIcon(SLICER_ACTIVE_ICON);
	}
	else {
	    slicerButton.setSelected(false);
	    slicerButton.setIcon(SLICER_INACTIVE_ICON);
	}
    }

    public void finishedJJJC() {
	//printMessage("Driver completed JJJC ... updating the session manager view.");
	// collect all the classes
	Hashtable compiledClasses = CompilationManager.getCompiledClasses();
	if((compiledClasses != null) && (compiledClasses.size() > 0)) {
	    Set classSet = new HashSet();
	    Iterator cci = compiledClasses.keySet().iterator();
	    while(cci.hasNext()) {
		Object key = cci.next();
		Object value = compiledClasses.get(key);
		if(value == null) {
		}
		else if(value instanceof SootClass) {
		    classSet.add(value);
		}
		else {
		}
	    }
	    sessionManagerView.setClassSet(classSet);
	}
	sessionManagerView.updateView();
    }

    public void finishedBSL() {
	//printMessage("Driver completed BSL ... updating the session manager view.");
	sessionManagerView.updateView();
    }
}
