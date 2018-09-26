package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.*;
import java.io.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (4/17/2002 4:36:45 PM)
 * @author: 
 */
public final class SessionFileWizardPanel extends BanderaAbstractWizardPanel {
	private JButton ivjBrowseButton = null;
	private JTextArea ivjMessageTextArea = null;
	private JLabel ivjSessionFileLabel = null;
	private JTextField ivjSessionFileTextField = null;
	private final static java.lang.String message = "Bandera collects multiple sessions together in a file, so that that they can be saved and used again in future invocations of Bandera.\n\nTypically, all the sessions that apply to a particular application are held in the same session file.\n- If you have already created a session file for the application that you specified earlier, you should probably save this current session that you are creating in that file.\n- If this is the first session that you are creating for this application, you should probably create a new session file to hold sessions for this application.\n\nIf you would like to use an existing session file to store this session in, please enter it in the text box or browse for it.  If you don't have an existing session file and wish to create a new one, enter the name (and directory) in the text box and one will be created for you.  [Note: If you leave off the .session extension, it will automatically be added for you.]\n\nAlso, if you use an existing session file, you will be able to clone the new session from an existing session so that you may re-use the settings of an existing session.\n\nNote: If you are creating a new session file, I will prompt you for confirmation that you wish to create it.";
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private File sessionFile;
	private javax.swing.JFileChooser chooser;
	private boolean sessionFileCreated = false;
	private java.io.File baseDirectory;
	private final static Category log = Category.getInstance(SessionFileWizardPanel.class);

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SessionFileWizardPanel.this.getBrowseButton()) 
				connEtoC1(e);
		};
	};
/**
 * SessionFileWizardStep constructor comment.
 */
public SessionFileWizardPanel() {
	super();
	initialize();
}
/**
 * When the browse button is pressed, we need to show the FileChooser and
 * grab the result and put it into the SessionFileTextField.
 *
 * @param ActionEvent actionEvent ignored.
 */
public void browseButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	
	chooser.setCurrentDirectory(baseDirectory);
 	chooser.setFileFilter(new SessionFileFilter());
    int returnVal = chooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
       // set the variable sessionFile to this file
       sessionFile = chooser.getSelectedFile();
       
       // set the text field to the string of this file
       getSessionFileTextField().setText(sessionFile.getPath());
    }

    baseDirectory = chooser.getCurrentDirectory();

}
/**
 * connEtoC1:  (BrowseButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionFileWizardStep.browseButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.browseButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Get the base directory set for this panel.
 *
 * @return File The base directory in which the session file was found.
 */
public File getBaseDirectory() {
	return(baseDirectory);
}
/**
 * Return the BrowseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBrowseButton() {
	if (ivjBrowseButton == null) {
		try {
			ivjBrowseButton = new javax.swing.JButton();
			ivjBrowseButton.setName("BrowseButton");
			ivjBrowseButton.setText("Browse");
			ivjBrowseButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBrowseButton;
}
/**
 * Return the MessageTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getMessageTextArea() {
	if (ivjMessageTextArea == null) {
		try {
			ivjMessageTextArea = new javax.swing.JTextArea();
			ivjMessageTextArea.setName("MessageTextArea");
			ivjMessageTextArea.setLineWrap(true);
			ivjMessageTextArea.setBorder(new javax.swing.border.EtchedBorder());
			ivjMessageTextArea.setWrapStyleWord(true);
			ivjMessageTextArea.setBackground(java.awt.Color.white);
			ivjMessageTextArea.setEditable(false);
			// user code begin {1}
			ivjMessageTextArea.setText(message);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMessageTextArea;
}
/**
 * Retrieve the session file that was selected by the user.
 *
 * @return File The session file selected by the user in this panel.
 */
public java.io.File getSessionFile() {
	return(sessionFile);
}
/**
 * Return the SessionFileLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSessionFileLabel() {
	if (ivjSessionFileLabel == null) {
		try {
			ivjSessionFileLabel = new javax.swing.JLabel();
			ivjSessionFileLabel.setName("SessionFileLabel");
			ivjSessionFileLabel.setText("Session File");
			ivjSessionFileLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionFileLabel;
}
/**
 * Return the SessionFileTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getSessionFileTextField() {
	if (ivjSessionFileTextField == null) {
		try {
			ivjSessionFileTextField = new javax.swing.JTextField();
			ivjSessionFileTextField.setName("SessionFileTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionFileTextField;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getBrowseButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SessionFileWizardStep");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(448, 388);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 3;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsSessionFileTextField = new java.awt.GridBagConstraints();
		constraintsSessionFileTextField.gridx = 1; constraintsSessionFileTextField.gridy = 1;
		constraintsSessionFileTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsSessionFileTextField.weightx = 1.0;
		constraintsSessionFileTextField.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSessionFileTextField(), constraintsSessionFileTextField);

		java.awt.GridBagConstraints constraintsBrowseButton = new java.awt.GridBagConstraints();
		constraintsBrowseButton.gridx = 2; constraintsBrowseButton.gridy = 1;
		constraintsBrowseButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getBrowseButton(), constraintsBrowseButton);

		java.awt.GridBagConstraints constraintsSessionFileLabel = new java.awt.GridBagConstraints();
		constraintsSessionFileLabel.gridx = 0; constraintsSessionFileLabel.gridy = 1;
		constraintsSessionFileLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSessionFileLabel(), constraintsSessionFileLabel);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Choose a Session File");

	// init the baseDirectory to the 2nd root (hopefully c:) just in case it doesn't get set
	baseDirectory = File.listRoots()[1];

	// init the
	chooser = new JFileChooser(baseDirectory);
	
	// user code end
}
/**
 * If we had to create a session file, this will return true.  Otherwise,
 * it will return false which means the session file selected by the user
 * already existed.
 *
 * @return boolean True if the session file was created by this wizard step, false otherwise.
 */
public boolean isSessionFileCreated() {
	return sessionFileCreated;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		SessionFileWizardPanel aSessionFileWizardPanel;
		aSessionFileWizardPanel = new SessionFileWizardPanel();
		frame.setContentPane(aSessionFileWizardPanel);
		frame.setSize(aSessionFileWizardPanel.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JPanel");
		exception.printStackTrace(System.out);
	}
}
/**
 * Set the base directory from which the session file will be found.
 * 
 * @param File baseDirectory The base directory from which to find the session file.
 * @pre baseDirectory != null
 */
public void setBaseDirectory(java.io.File baseDirectory) {

	if(baseDirectory == null) {
		log.warn("baseDirectory is null.  Cannot set a null directory so we will use the default instead.");
		return;
	}
	
	this.baseDirectory = baseDirectory;
}
/**
 * Set the initial value for the session file panel.
 *
 * @param File sessionFile The initial value for the session file.
 */
public void setSessionFile(File sessionFile) {

	if(sessionFile == null) {
		log.warn("sessionFile is null.  Cannot set a null session file.  Skipping.");
		this.sessionFile = null;
		return;
	}

	this.sessionFile = sessionFile;
	getSessionFileTextField().setText(sessionFile.getPath());
	baseDirectory = sessionFile.getParentFile();
	
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 11:13:56 AM)
 * @param newSessionFileCreated boolean
 */
public void setSessionFileCreated(boolean newSessionFileCreated) {
	sessionFileCreated = newSessionFileCreated;
}
/**
  * To make this a valid step, the session file must exist, be readable, and writeable.  It must
  * also have a valid extension (.session).  If the file does not exist, it will be created.  If a
  * relative file name is entered, it will be placed relative to the base of the FileChooser.
  *
  * At the end of this and the return value is true, we know that:
  * 1) The session file exists
  * 2) The session file has a .session extension
  * 3) The session file is readable.
  * 4) The session file is writable.
  * 5) The session file is a file.
  */
public boolean validateNext(java.util.List list) {

    /*
     * Testing:
     * 1) User doesn't enter anything.
     * -> Fail: Must enter a session file.
     *
     * 2) User enters a file that is not readable
     * -> Fail: The session file must be readable.
     *
     * 3) User enters a file that is not writable
     * -> Fail: The session file must be writable.
     *
     * 4) User enters a file that doesn't exist.
     * -> If the file can be created, Pass, otherwise Fail -> File doesn't exist and could not be created.
     *
     * 5) User enters a file that doesn't have a .session extension
     * -> Add the .session extension
     *
     * 6) User enters a file that doesn't have a .session extension but one
     *    exists with that name already: FILENAME is entered and FILENAME.session exists
     * ->
     *
     * 7) User enters a directory name.
     * -> Fail: The session file must be a file and not a directory.
     *
     * 8) User enters a relative file.
     * -> If the file exists, Pass, if the file doesn't exist and can be created, Pass, otherwise, Fail -> File doesn't exist and could not be created.
     *
     * 9) User enters an absolute file.
     * -> If the file exists, Pass, if the file doesn't exist and can be created, Pass, otherwise, Fail -> File doesn't exist and could not be created.
     *
     * 10) User browses and finds a .session file and then edits the text field.
     * -> The text field will take precedence and the sessionFile will be thrown out.  All other tests apply.
     */

    // check to see if the user has not selected a file and not entered one in the text field
    String sessionFileText = getSessionFileTextField().getText();
    if ((sessionFile == null)
        && ((sessionFileText == null) || (sessionFileText.length() < 1))) {
        String errorMessage =
            "No session file was selected and no session file was entered.";
        list.add(errorMessage);
        return (false);
    }

    // check to see if the sessionFile and sessionFileText match
    if ((sessionFile != null)
        && (sessionFileText != null)
        && (!(sessionFile.getPath().equals(sessionFileText)))) {

        // sessionFile does not match the sessionFileText, use the sessionFileText
        log.debug(
            "sessionFile doesn't match sessionFileText.  Using sessionFileText ...");
        sessionFile = new File(sessionFileText);
    }

    // if the session was just entered in the text field, we need to create the sessionFile
    // from it.
    if ((sessionFile == null) && (sessionFileText != null)) {
        sessionFile = new File(sessionFileText);
    }

    // assume: sessionFile != null
    if (sessionFile == null) {
        String errorMessage =
            "The system is in an inconsistent state.  The sessionFile is null when it should not be.";
        list.add(errorMessage);
        return (false);
    }

    // assume: sessionFile matches what was given in sessionFileText

    // make sure the sessionFile has a .session extension
    if (!(sessionFile.getPath().endsWith(".session"))) {
        // file does not have a .session extension, adding one
        log.info(
            "sessionFile does not have a .session extension.  Adding one ...");
        sessionFile = new File(sessionFile.getPath() + ".session");
    }

    // check to see if the file exists already, if not, create it!
    if (!(sessionFile.exists())) {
	// ask the user if they want to create the file. -tcw
	int result = JOptionPane.showConfirmDialog(this, "Do you want to create the session file specified: " + sessionFile.getPath(), "Create File?", JOptionPane.YES_NO_OPTION);
	if(result == JOptionPane.YES_OPTION) {
	    try {
		boolean succeed = sessionFile.createNewFile();
		if (!succeed) {
		    String errorMessage = "The session file could not be created.";
		    list.add(errorMessage);
		    return (false);
		    
		}
		else {
		    log.info("A new session file was created: " + sessionFile.getPath());
		    sessionFileCreated = true;
		}
	    }
	    catch (IOException ioe) {
		String errorMessage =
		    "The session file could not be created because an exception was thrown: "
                    + ioe.toString();
		list.add(errorMessage);
		return (false);
	    }
	}
	else {
	    // user does not want to create, tell them they must choose again
	    String errorMessage = "You have chosen to avoid creating a new file.  You must either choose an existing session or create a new file.";
	    list.add(errorMessage);
	    return(false);
	}
    }

    // assume: sessionFile exists

    // make sure the file given is a file
    if (!(sessionFile.isFile())) {
        String errorMessage =
            "The session file, " + sessionFile.getPath() + ", you specified is not a file.";
        list.add(errorMessage);
        return (false);
    }

    // make sure that the session file is readable
    if (!(sessionFile.canRead())) {
        String errorMessage =
            "The session file, "
                + sessionFile.getPath()
                + ", you specified could not be read.";
        list.add(errorMessage);
        return (false);
    }

    // make sure that the session file is writable
    if (!(sessionFile.canRead())) {
        String errorMessage =
            "The session file, "
                + sessionFile.getPath()
                + ", you specified could not be written to.";
        list.add(errorMessage);
        return (false);
    }

    return (true);
}
}
