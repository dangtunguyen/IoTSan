package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.*;
import org.apache.log4j.Category;

/**
 * Insert the type's description here.
 * Creation date: (4/17/2002 10:04:05 PM)
 * @author: 
 */
public final class CloneSessionWizardPanel extends BanderaAbstractWizardPanel {
	private JTextArea ivjMessageTextArea = null;
	private JRadioButton ivjNoneRadioButton = null;
	private JPanel ivjRadioButtonPanel = null;
	private JScrollPane ivjRadioButtonScrollPane = null;
	private final static java.lang.String message = "Since you are using an existing session file, you may want to use the settings of an existing session as the default settings for this session (this may allow you to avoid reentering several settings if they are similar to those found in an existing session).\n\nPlease choose an existing session upon which the default settings for the current session will be based, or choose None if you want to start with empty values for the remaining settings in this session.";
	private BoxLayout ivjRadioButtonPanelBoxLayout = null;
	private javax.swing.ButtonGroup radioButtonGroup;
	private java.lang.String selectedSession;
	
	/**
	  * The log we will write messages to.
	  */
	private static final Category log = Category.getInstance(CloneSessionWizardPanel.class);
/**
 * CloneSessionWizardStep constructor comment.
 */
public CloneSessionWizardPanel() {
	super();
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:43:49 PM)
 * @param sessionName java.lang.String
 */
public void addSession(String sessionName) {

	// create a new radio button with the session name as the text and action command
	JRadioButton radioButton = new JRadioButton(sessionName);
	radioButton.setActionCommand(sessionName);

	// add the action listener so that when the radio button is selected, it will set
	//  the selectSession to the current radio button action command text.
	radioButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			selectedSession = ae.getActionCommand();
		}
	});

	// add this button to the button group
	radioButtonGroup.add(radioButton);

	// add this button to the radio button panel
	getRadioButtonPanel().add(radioButton);

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
			ivjMessageTextArea.setMargin(new java.awt.Insets(10, 10, 10, 10));
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
 * Return the NoneRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getNoneRadioButton() {
	if (ivjNoneRadioButton == null) {
		try {
			ivjNoneRadioButton = new javax.swing.JRadioButton();
			ivjNoneRadioButton.setName("NoneRadioButton");
			ivjNoneRadioButton.setSelected(true);
			ivjNoneRadioButton.setText("None");
			ivjNoneRadioButton.setBounds(568, 31, 109, 28);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNoneRadioButton;
}
/**
 * Return the RadioButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getRadioButtonPanel() {
	if (ivjRadioButtonPanel == null) {
		try {
			ivjRadioButtonPanel = new javax.swing.JPanel();
			ivjRadioButtonPanel.setName("RadioButtonPanel");
			ivjRadioButtonPanel.setLayout(getRadioButtonPanelBoxLayout());
			ivjRadioButtonPanel.setBounds(0, 0, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRadioButtonPanel;
}
/**
 * Return the RadioButtonPanelBoxLayout property value.
 * @return javax.swing.BoxLayout
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.BoxLayout getRadioButtonPanelBoxLayout() {
	javax.swing.BoxLayout ivjRadioButtonPanelBoxLayout = null;
	try {
		/* Create part */
		ivjRadioButtonPanelBoxLayout = new javax.swing.BoxLayout(getRadioButtonPanel(), javax.swing.BoxLayout.Y_AXIS);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjRadioButtonPanelBoxLayout;
}
/**
 * Return the RadioButtonScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getRadioButtonScrollPane() {
	if (ivjRadioButtonScrollPane == null) {
		try {
			ivjRadioButtonScrollPane = new javax.swing.JScrollPane();
			ivjRadioButtonScrollPane.setName("RadioButtonScrollPane");
			getRadioButtonScrollPane().setViewportView(getRadioButtonPanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRadioButtonScrollPane;
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:46:16 PM)
 * @return java.lang.String
 */
public java.lang.String getSelectedSession() {
	return selectedSession;
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
		setName("CloneSessionWizardStep");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(533, 421);

		java.awt.GridBagConstraints constraintsRadioButtonScrollPane = new java.awt.GridBagConstraints();
		constraintsRadioButtonScrollPane.gridx = 1; constraintsRadioButtonScrollPane.gridy = 0;
		constraintsRadioButtonScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsRadioButtonScrollPane.weightx = 0.9;
		constraintsRadioButtonScrollPane.weighty = 1.0;
		constraintsRadioButtonScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getRadioButtonScrollPane(), constraintsRadioButtonScrollPane);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 0.3;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	// create a button group and put the none radio button in it
	radioButtonGroup = new ButtonGroup();
	radioButtonGroup.add(getNoneRadioButton());

	getRadioButtonPanel().add(getNoneRadioButton());

	setTitle("Clone a Session?");
	
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		CloneSessionWizardPanel aCloneSessionWizardPanel;
		aCloneSessionWizardPanel = new CloneSessionWizardPanel();
		frame.setContentPane(aCloneSessionWizardPanel);
		frame.setSize(aCloneSessionWizardPanel.getSize());
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
 * Creation date: (4/23/2002 3:46:16 PM)
 * @param newSelectedSession java.lang.String
 */
public void setSelectedSession(java.lang.String newSelectedSession) {
	selectedSession = newSelectedSession;
}
}
