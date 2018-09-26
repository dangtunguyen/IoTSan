package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (4/22/2002 12:56:23 PM)
 * @author: 
 */
public final class PropertyWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JCheckBox ivjAssertionCheckBox = null;
	private javax.swing.JPanel ivjCheckBoxPanel = null;
	private javax.swing.JCheckBox ivjDeadlockCheckBox = null;
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private javax.swing.JCheckBox ivjTemporalPropertyCheckBox = null;
	private final static java.lang.String message = "Now that Bandera has loaded the application you wish to check, I will walk you through selecting properties to check.\n\nBandera can check your application for three types of properties:\n(1) deadlock (invalid end states)\n(2) assertion violations\n(3) temporal property violations\n\nBandera always checks for deadlocks.  You can configure this session so that Bandera will simultaneously check multiple assertions and one temporal property in addition to checking for deadlock.  However, it is probably more efficient to create separate sessions for checking various subsets of declared assertions and a separate session for checking each temporal property.  Separate sessions will allow Bandera to more effectively generate models that are customized to properties specified in each session.  In addition, the algorithms used for checking a temporal property are more expensive than those used for checking deadlocks and assertions.\n\nThe recommend practice is to declare a separate session for:\n- deadlock checking,\n- each subset of assertions to that refer to different aspects of the program, and\n- each temporal property to be checked.\n\nWhat type of properties do you want to check with this session?";
	private boolean assertionCheckingEnabled = false;
	private boolean temporalPropertyCheckingEnabled = false;
	private javax.swing.JLabel ivjWizardLogoLabel = null;
	private final static Category log = Category.getInstance(PropertyWizardPanel.class);
/**
 * PropertyWizardPanel constructor comment.
 */
public PropertyWizardPanel() {
	super();
	initialize();
}
/**
 * Return the AssertionCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getAssertionCheckBox() {
	if (ivjAssertionCheckBox == null) {
		try {
			ivjAssertionCheckBox = new javax.swing.JCheckBox();
			ivjAssertionCheckBox.setName("AssertionCheckBox");
			ivjAssertionCheckBox.setText("Assertions");
			ivjAssertionCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionCheckBox;
}

/**
 * Return the CheckBoxPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getCheckBoxPanel() {
	if (ivjCheckBoxPanel == null) {
		try {
			ivjCheckBoxPanel = new javax.swing.JPanel();
			ivjCheckBoxPanel.setName("CheckBoxPanel");
			ivjCheckBoxPanel.setLayout(new java.awt.GridBagLayout());
			ivjCheckBoxPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsDeadlockCheckBox = new java.awt.GridBagConstraints();
			constraintsDeadlockCheckBox.gridx = 0; constraintsDeadlockCheckBox.gridy = 0;
			constraintsDeadlockCheckBox.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsDeadlockCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getCheckBoxPanel().add(getDeadlockCheckBox(), constraintsDeadlockCheckBox);

			java.awt.GridBagConstraints constraintsAssertionCheckBox = new java.awt.GridBagConstraints();
			constraintsAssertionCheckBox.gridx = 0; constraintsAssertionCheckBox.gridy = 1;
			constraintsAssertionCheckBox.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsAssertionCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getCheckBoxPanel().add(getAssertionCheckBox(), constraintsAssertionCheckBox);

			java.awt.GridBagConstraints constraintsTemporalPropertyCheckBox = new java.awt.GridBagConstraints();
			constraintsTemporalPropertyCheckBox.gridx = 0; constraintsTemporalPropertyCheckBox.gridy = 2;
			constraintsTemporalPropertyCheckBox.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsTemporalPropertyCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getCheckBoxPanel().add(getTemporalPropertyCheckBox(), constraintsTemporalPropertyCheckBox);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckBoxPanel;
}

/**
 * Return the DeadlockCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDeadlockCheckBox() {
	if (ivjDeadlockCheckBox == null) {
		try {
			ivjDeadlockCheckBox = new javax.swing.JCheckBox();
			ivjDeadlockCheckBox.setName("DeadlockCheckBox");
			ivjDeadlockCheckBox.setSelected(true);
			ivjDeadlockCheckBox.setText("Deadlock");
			ivjDeadlockCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjDeadlockCheckBox.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDeadlockCheckBox;
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
			ivjMessageTextArea.setBorder(new javax.swing.plaf.basic.BasicBorders.MarginBorder());
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
 * Return the TemporalPropertyCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getTemporalPropertyCheckBox() {
	if (ivjTemporalPropertyCheckBox == null) {
		try {
			ivjTemporalPropertyCheckBox = new javax.swing.JCheckBox();
			ivjTemporalPropertyCheckBox.setName("TemporalPropertyCheckBox");
			ivjTemporalPropertyCheckBox.setText("TemporalProperty");
			ivjTemporalPropertyCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTemporalPropertyCheckBox;
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
			ivjWizardLogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/wizard/images/wizard3.gif")));
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
		setName("PropertyWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(486, 363);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
constraintsMessageTextArea.gridheight = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 1; constraintsWizardLogoLabel.gridy = 0;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);

		java.awt.GridBagConstraints constraintsCheckBoxPanel = new java.awt.GridBagConstraints();
		constraintsCheckBoxPanel.gridx = 1; constraintsCheckBoxPanel.gridy = 1;
		constraintsCheckBoxPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsCheckBoxPanel.weightx = 1.0;
		constraintsCheckBoxPanel.weighty = 1.0;
		constraintsCheckBoxPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getCheckBoxPanel(), constraintsCheckBoxPanel);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Property Specification");
	// user code end
}

/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 2:29:21 PM)
 * @return boolean
 */
public boolean isAssertionCheckingEnabled() {
	return assertionCheckingEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 2:29:43 PM)
 * @return boolean
 */
public boolean isTemporalPropertyCheckingEnabled() {
	return temporalPropertyCheckingEnabled;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		PropertyWizardPanel aPropertyWizardPanel;
		aPropertyWizardPanel = new PropertyWizardPanel();
		frame.setContentPane(aPropertyWizardPanel);
		frame.setSize(aPropertyWizardPanel.getSize());
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
		System.err.println("Exception occurred in main() of edu.ksu.cis.bandera.bui.wizard.BanderaAbstractWizardPanel");
		exception.printStackTrace(System.out);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 2:29:21 PM)
 * @param newAssertionCheckingEnabled boolean
 */
public void setAssertionCheckingEnabled(boolean newAssertionCheckingEnabled) {
	assertionCheckingEnabled = newAssertionCheckingEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 2:29:43 PM)
 * @param newTemporalPropertyCheckingEnabled boolean
 */
public void setTemporalPropertyCheckingEnabled(boolean newTemporalPropertyCheckingEnabled) {
	temporalPropertyCheckingEnabled = newTemporalPropertyCheckingEnabled;
}
/**
* This step is auto-matically valid.  It will just return true.  But while we do this,
* we will set the values for:
* 1) Assertion checking
* 2) Temporal Property checking
*/
public boolean validateNext(java.util.List list) {

	/*
	 * This logic should be moved so this method can use the default implementation. -tcw
	 */
	
    assertionCheckingEnabled = false;
    temporalPropertyCheckingEnabled = false;

    if (getAssertionCheckBox().isSelected()) {
        assertionCheckingEnabled = true;
    }

    if (getTemporalPropertyCheckBox().isSelected()) {
        temporalPropertyCheckingEnabled = true;
    }

    return (true);
}
}
