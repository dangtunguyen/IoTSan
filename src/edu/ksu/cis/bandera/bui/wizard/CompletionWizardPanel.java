package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import org.apache.log4j.Category;

/**
 * Insert the type's description here.
 * Creation date: (5/22/2002 4:54:09 PM)
 * @author: 
 */
public final class CompletionWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private final static java.lang.String message = "You have successfully configured a session that allows you to use Bandera to validate your application.\n\nAt this time you have several options.  You may want to quit and not save the session you have configured.  If so, press the cancel button.  However, if you want to save the session to the file specified earlier and load this session into Bandera, press the Finish button.";
	/**
	  * The log we will write messages to.
	  */
	private static final Category log = Category.getInstance(CompletionWizardPanel.class);
	private javax.swing.JLabel ivjWizardLogoLabel = null;
/**
 * CompletionWizardPanel constructor comment.
 */
public CompletionWizardPanel() {
	super();
	initialize();
}
 /** Can this panel finish the wizard?
     * @return true if this panel can finish the wizard.
     */
public boolean canFinish() {
	return(true);
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
 /** Is there be a next panel?
     * @return true if there is a panel to move to next
     */
public boolean hasNext() {
	return(false);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("CompletionWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(508, 446);

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 0; constraintsWizardLogoLabel.gridy = 0;
		constraintsWizardLogoLabel.weightx = 0.1;
		constraintsWizardLogoLabel.weighty = 1.0;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 1; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 0.9;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Complete the Wizard");
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		CompletionWizardPanel aCompletionWizardPanel;
		aCompletionWizardPanel = new CompletionWizardPanel();
		frame.setContentPane(aCompletionWizardPanel);
		frame.setSize(aCompletionWizardPanel.getSize());
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
 /** Called to validate the panel before finishing the wizard. Should
     * return false if canFinish returns false.
     * @param list a List of error messages to be displayed.
     * @return true if it is valid for this wizard to finish.
     */
public boolean validateFinish(java.util.List list) {
	return(true);
}
}
