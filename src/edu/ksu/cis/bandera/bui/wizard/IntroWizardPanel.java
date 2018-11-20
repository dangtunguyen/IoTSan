package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (4/17/2002 2:08:57 PM)
 * @author: 
 */
public final class IntroWizardPanel extends BanderaAbstractWizardPanel {
	private final static java.lang.String message = "I am the Bandera Wizard and I will help you configure Bandera to check properties of your application.  This will including the following steps...\n- collecting information (file names, etc) about the application you wish to check\n- configuring the properties that you wish to check\n- helping you decide which model reduction techniques (e.g., slicing and abstraction) to apply to make the checks easier and more efficient\n- choosing and configuring a backend model checker.\n\nOnce we have completed the steps above to configure Bandera, you will be able to run the check of your application using Bandera.\n\nTo get started, click next.  If you wish to quit at any time in this wizard, click cancel.  This will stop the wizard and cause the configuration information that you have entered so far to be discarded.";
	private JTextArea ivjMessageTextArea = null;
	private JLabel ivjWizardLogoLabel = null;
	private final static Category log = Category.getInstance(IntroWizardPanel.class);
/**
 * IntroWizardStep constructor comment.
 */
public IntroWizardPanel() {
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
			ivjMessageTextArea.setMaximumSize(new java.awt.Dimension(600, 400));
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
 * Return the WizardLogoLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getWizardLogoLabel() {
	if (ivjWizardLogoLabel == null) {
		try {
			ivjWizardLogoLabel = new javax.swing.JLabel();
			ivjWizardLogoLabel.setName("WizardLogoLabel");
			ivjWizardLogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/wizard/images/wizard1.gif")));
			ivjWizardLogoLabel.setText("");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWizardLogoLabel;
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
		setName("IntroWizardStep");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(452, 372);
		setMaximumSize(new java.awt.Dimension(600, 400));

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 1; constraintsWizardLogoLabel.gridy = 1;
		constraintsWizardLogoLabel.anchor = java.awt.GridBagConstraints.WEST;
		constraintsWizardLogoLabel.weightx = 0.1;
		constraintsWizardLogoLabel.weighty = 1.0;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 2; constraintsMessageTextArea.gridy = 1;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 0.9;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Introduction");
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		IntroWizardPanel aIntroWizardPanel;
		aIntroWizardPanel = new IntroWizardPanel();
		frame.setContentPane(aIntroWizardPanel);
		frame.setSize(aIntroWizardPanel.getSize());
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
}
