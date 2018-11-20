package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (4/17/2002 3:26:49 PM)
 * @author: 
 */
public final class SessionWizardPanel extends BanderaAbstractWizardPanel {
	private JTextArea ivjMessageTextArea = null;
	private JLabel ivjSessionNameLabel = null;
	private JTextField ivjSessionNameTextField = null;
	private static java.lang.String message = "Bandera uses a session to collect information about a single check of an application.  A session will hold information that relates to the source code and the properties you wish to check in a single run of Bandera.\n\nThe name of the session should describe either the application or the property that is being checked. [Note: The session name can be anything you choose but should be meaningful to you.]  For example, if your application is named MyApplication, you might want to name the session MyApplication.  Or, if you will be checking several properties of the application, you might want to base it on the property of the application that is being checked.  For example, if you were checking deadlock in MyApplicaiton, you might name it MyApplicationDeadlock.\n\nPlease enter the name of the session and click continue.\n\nNote: A valid name will contain only letters and numbers as well as the '-' and '_' characters.";
	private final static Category log = Category.getInstance(SessionWizardPanel.class);
/**
 * Insert the method's description here.
 * Creation date: (4/17/2002 3:27:06 PM)
 */
public SessionWizardPanel() {
	super();
	initialize();
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
 * Insert the method's description here.
 * Creation date: (4/26/2002 6:09:21 PM)
 * @return java.lang.String
 */
public String getSessionName() {
	return(getSessionNameTextField().getText());
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
			ivjSessionNameLabel.setText("Session Name");
			ivjSessionNameLabel.setForeground(java.awt.Color.black);
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
 * Return the SessionNameTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getSessionNameTextField() {
	if (ivjSessionNameTextField == null) {
		try {
			ivjSessionNameTextField = new javax.swing.JTextField();
			ivjSessionNameTextField.setName("SessionNameTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionNameTextField;
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
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SessionWizardStep");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(453, 369);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsSessionNameTextField = new java.awt.GridBagConstraints();
		constraintsSessionNameTextField.gridx = 1; constraintsSessionNameTextField.gridy = 1;
		constraintsSessionNameTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsSessionNameTextField.weightx = 0.9;
		constraintsSessionNameTextField.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSessionNameTextField(), constraintsSessionNameTextField);

		java.awt.GridBagConstraints constraintsSessionNameLabel = new java.awt.GridBagConstraints();
		constraintsSessionNameLabel.gridx = 0; constraintsSessionNameLabel.gridy = 1;
		constraintsSessionNameLabel.weightx = 0.1;
		constraintsSessionNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSessionNameLabel(), constraintsSessionNameLabel);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Choose Session Name");
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		SessionWizardPanel aSessionWizardPanel;
		aSessionWizardPanel = new SessionWizardPanel();
		frame.setContentPane(aSessionWizardPanel);
		frame.setSize(aSessionWizardPanel.getSize());
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
 * Insert the method's description here.
 * Creation date: (6/3/2002 2:35:47 PM)
 * @param sessionName java.lang.String
 */
public void setSessionName(String sessionName) {
	getSessionNameTextField().setText(sessionName);
}
/**
 * Validate that this step has been completed correctly by the user before
 * moving on in the Wizard.  In this case, the session name must be of a valid format.
 * This means that it must only consist of alpha-numeric characters and '-' or '-'.
 *
 * @return boolean True if the wizard should continue to the next step or False if
 *         problems exist.
 */
public boolean validateNext(java.util.List list) {
	
	String sessionName = getSessionNameTextField().getText();

	// make sure the user entered a session name
	if((sessionName == null) || (sessionName.length() == 0)) {
		String errorMessage = "You must enter a session name.";
		list.add(errorMessage);
		return(false);
	}

	for(int i = 0; i < sessionName.length(); i++) {
		char currentCharacter = sessionName.charAt(i);
		
        if (!(Character.isLetterOrDigit(currentCharacter))
            && (currentCharacter != '-')
            && (currentCharacter != '_')) {

            list.add(
                "The session name given, "
                    + sessionName
                    + " is invalid because of this character, "
                    + currentCharacter
                    + ".  Please enter a different, valid, session name.");
            return (false);
        }
	}
	
	return(true);
	
}
}
