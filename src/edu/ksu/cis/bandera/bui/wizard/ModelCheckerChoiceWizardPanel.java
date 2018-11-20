package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.apache.log4j.*;

/**
 * Insert the type's description here.
 * Creation date: (5/29/2002 1:40:09 PM)
 * @author: 
 */
public final class ModelCheckerChoiceWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JPanel ivjCheckerChoicePanel = null;
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private final static java.lang.String message = "For Bandera to verify your application, you must specify which model checker you wish to use.  A list of available model checkers are listed.  Note: This is a list of model checkers that Bandera will work with.  For Bandera to work, the selected model checker must be installed on the machine you will be running the check on.";
	private javax.swing.ButtonGroup checkerButtonGroup;
	private int currentCheckerChoice;
	private javax.swing.BoxLayout ivjCheckerChoicePanelBoxLayout = null;
	private final static Category log = Category.getInstance(ModelCheckerChoiceWizardPanel.class);
/**
 * ModelCheckerChoiceWizardPanel constructor comment.
 */
public ModelCheckerChoiceWizardPanel() {
	super();
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 1:47:11 PM)
 * @return int
 */
public int getCheckerChoice() {
	return(currentCheckerChoice);
}
/**
 * Return the CheckerChoicePanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getCheckerChoicePanel() {
	if (ivjCheckerChoicePanel == null) {
		try {
			ivjCheckerChoicePanel = new javax.swing.JPanel();
			ivjCheckerChoicePanel.setName("CheckerChoicePanel");
			ivjCheckerChoicePanel.setLayout(getCheckerChoicePanelBoxLayout());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckerChoicePanel;
}
/**
 * Return the CheckerChoicePanelBoxLayout property value.
 * @return javax.swing.BoxLayout
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.BoxLayout getCheckerChoicePanelBoxLayout() {
	javax.swing.BoxLayout ivjCheckerChoicePanelBoxLayout = null;
	try {
		/* Create part */
		ivjCheckerChoicePanelBoxLayout = new javax.swing.BoxLayout(getCheckerChoicePanel(), javax.swing.BoxLayout.Y_AXIS);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjCheckerChoicePanelBoxLayout;
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
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("ModelCheckerChoiceWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(540, 473);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 0.6;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsCheckerChoicePanel = new java.awt.GridBagConstraints();
		constraintsCheckerChoicePanel.gridx = 1; constraintsCheckerChoicePanel.gridy = 0;
		constraintsCheckerChoicePanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsCheckerChoicePanel.weightx = 0.4;
		constraintsCheckerChoicePanel.weighty = 1.0;
		constraintsCheckerChoicePanel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getCheckerChoicePanel(), constraintsCheckerChoicePanel);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}

/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 1:45:42 PM)
 * @param checkerName java.lang.String
 * @param response int
 */
public void registerCheckerChoice(String checkerName, int response, boolean selected) {

	// create a new radio button
	JRadioButton rb = new JRadioButton(checkerName);

	// to keep the currentCheckerChoice up to date, each time the
	//  radio button is selected, it will update the value to the
	//  defined response value.
	rb.setActionCommand(String.valueOf(response));
	rb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			String actionCommand = ae.getActionCommand();
			int choice = Integer.parseInt(actionCommand);
			currentCheckerChoice = choice;
		}
	});
	
	// add it to the radio button group
	if(checkerButtonGroup == null) {
		checkerButtonGroup = new ButtonGroup();
	}
	checkerButtonGroup.add(rb);
	rb.setSelected(selected);
	if(selected) {
		currentCheckerChoice = response;
	}
	
	// add the radio button to the panel
	getCheckerChoicePanel().add(rb);
	
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		ModelCheckerChoiceWizardPanel aModelCheckerChoiceWizardPanel;
		aModelCheckerChoiceWizardPanel = new ModelCheckerChoiceWizardPanel();
		frame.setContentPane(aModelCheckerChoiceWizardPanel);
		frame.setSize(aModelCheckerChoiceWizardPanel.getSize());
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
}
