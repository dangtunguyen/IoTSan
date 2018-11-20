package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import org.apache.log4j.Category;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsFactory;
import edu.ksu.cis.bandera.checker.OptionsView;
import edu.ksu.cis.bandera.checker.CompletionListener;

/**
 * Insert the type's description here.
 * Creation date: (5/29/2002 3:41:07 PM)
 * @author: 
 */
public final class DSpinOptionWizardPanel extends BanderaAbstractWizardPanel implements CompletionListener {

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DSpinOptionWizardPanel.this.getDSpinOptionsGUIButton()) 
				connEtoC1(e);
		};
	};
	private javax.swing.JButton ivjDSpinOptionsGUIButton = null;
	private javax.swing.JLabel ivjDSpinOptionsLabel = null;
	private javax.swing.JTextField ivjDSpinOptionsTextField = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JTextArea ivjMessageTextArea = null;
	
	/**
	  * The log we will write messages to.
	  */
	private static final Category log = Category.getInstance(DSpinOptionWizardPanel.class);
	private final static java.lang.String message = "Currently, I don't know how to walk you through the configuration of DSpin command line options.  If you wish to configure them using a graphical interface, you can press the button and the GUI included with Bandera will be used to configure the DSpin options.  However, if you would like to modify them yourself you may edit the text box below.  If you don't want to configure them (and use the defaults) just press the next button.";
/**
 * DSpinOptionWizardPanel constructor comment.
 */
public DSpinOptionWizardPanel() {
	super();
	initialize();
}
 /**
     * This method will be called upon completion of the activity and
     * the source will be the Object that completes that activity.
     *
     * @param Object source The Object that completes the task.
     */
public void complete(java.lang.Object source) {

	if((source != null) && (source instanceof OptionsView)) {
		OptionsView ov = (OptionsView)source;
		Options options = ov.getOptions();
		try {
			getDSpinOptionsTextField().setText(options.getCommandLineOptions());
		}
		catch(Exception e) {
			getDSpinOptionsTextField().setText("An error occured while getting the configured options.");
		}
	}
}
/**
 * connEtoC1:  (DSpinOptionsGUIButton.action.actionPerformed(java.awt.event.ActionEvent) --> DSpinOptionWizardPanel.dSpinOptionsGUIButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dSpinOptionsGUIButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Open up a GUI for the user to use to configure the DSpin command line
 * options.  Once the GUI closes, the text field should be updated with
 * the configured values.
 */
public void dSpinOptionsGUIButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    String checkerOptions = getDSpinOptionsTextField().getText();
    OptionsView optionsView = OptionsFactory.createOptionsViewInstance("DSpin");
    Options options = OptionsFactory.createOptionsInstance("DSpin");
    options.init(checkerOptions);
    optionsView.init(options);
    optionsView.registerCompletionListener(this);
    optionsView.setVisible(true);

}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 3:45:56 PM)
 * @return java.lang.String
 */
public String getDSpinOptions() {
	return(getDSpinOptionsTextField().getText());
}
/**
 * Return the DSpinOptionsGUIButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getDSpinOptionsGUIButton() {
	if (ivjDSpinOptionsGUIButton == null) {
		try {
			ivjDSpinOptionsGUIButton = new javax.swing.JButton();
			ivjDSpinOptionsGUIButton.setName("DSpinOptionsGUIButton");
			ivjDSpinOptionsGUIButton.setToolTipText("Open the DSpin options GUI provided by Bandera");
			ivjDSpinOptionsGUIButton.setText("DSpin Options GUI");
			ivjDSpinOptionsGUIButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDSpinOptionsGUIButton;
}
/**
 * Return the DSpinOptionsLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDSpinOptionsLabel() {
	if (ivjDSpinOptionsLabel == null) {
		try {
			ivjDSpinOptionsLabel = new javax.swing.JLabel();
			ivjDSpinOptionsLabel.setName("DSpinOptionsLabel");
			ivjDSpinOptionsLabel.setText("DSpin Command Line Options");
			ivjDSpinOptionsLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDSpinOptionsLabel;
}
/**
 * Return the DSpinOptionsTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getDSpinOptionsTextField() {
	if (ivjDSpinOptionsTextField == null) {
		try {
			ivjDSpinOptionsTextField = new javax.swing.JTextField();
			ivjDSpinOptionsTextField.setName("DSpinOptionsTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDSpinOptionsTextField;
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
			ivjMessageTextArea.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED), new javax.swing.border.EmptyBorder(10,10,10,10)));
			ivjMessageTextArea.setLineWrap(true);
			ivjMessageTextArea.setWrapStyleWord(true);
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
	getDSpinOptionsGUIButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("DSpinOptionWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setSize(488, 427);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsDSpinOptionsTextField = new java.awt.GridBagConstraints();
		constraintsDSpinOptionsTextField.gridx = 1; constraintsDSpinOptionsTextField.gridy = 2;
		constraintsDSpinOptionsTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsDSpinOptionsTextField.weightx = 1.0;
		constraintsDSpinOptionsTextField.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getDSpinOptionsTextField(), constraintsDSpinOptionsTextField);

		java.awt.GridBagConstraints constraintsDSpinOptionsLabel = new java.awt.GridBagConstraints();
		constraintsDSpinOptionsLabel.gridx = 0; constraintsDSpinOptionsLabel.gridy = 2;
		constraintsDSpinOptionsLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getDSpinOptionsLabel(), constraintsDSpinOptionsLabel);

		java.awt.GridBagConstraints constraintsDSpinOptionsGUIButton = new java.awt.GridBagConstraints();
		constraintsDSpinOptionsGUIButton.gridx = 0; constraintsDSpinOptionsGUIButton.gridy = 1;
		constraintsDSpinOptionsGUIButton.gridwidth = 2;
		constraintsDSpinOptionsGUIButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getDSpinOptionsGUIButton(), constraintsDSpinOptionsGUIButton);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 3:45:30 PM)
 * @param dSpinOptions java.lang.String
 */
public void setDSpinOptions(String dSpinOptions) {
	getDSpinOptionsTextField().setText(dSpinOptions);
}
}
