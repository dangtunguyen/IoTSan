package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import edu.ksu.cis.bandera.bui.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsFactory;
import edu.ksu.cis.bandera.checker.OptionsView;
import edu.ksu.cis.bandera.checker.CompletionListener;

/**
 * The SpinOptionWizardPanel provides the user a way to configure Spin options
 * in the BanderaWizard.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:21 $
 */
public final class SpinOptionWizardPanel extends BanderaAbstractWizardPanel implements edu.ksu.cis.bandera.checker.CompletionListener {
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private javax.swing.JButton ivjSpinOptionGUIButton = null;
	private javax.swing.JLabel ivjSpinOptionsLabel = null;
	private javax.swing.JTextField ivjSpinOptionsTextField = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private final static java.lang.String message = "Currently, I don't knjow how to walk you through the configuration of Spin command line options.  If you wish to configure them using a graphical interface, you can press the button and the GUI included with Bandera will be used to configure the Spin options.  However, if you would like to modify them yourself you may edit the text box below.  If you don't want to configure them (and use the defaults) just press the next button.";
	private final static Category log = Category.getInstance(SpinOptionWizardPanel.class);

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SpinOptionWizardPanel.this.getSpinOptionGUIButton()) 
				connEtoC1(e);
		};
	};
/**
 * SpinOptionWizardPanel constructor comment.
 */
public SpinOptionWizardPanel() {
	super();
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/2002 10:32:59 PM)
 * @param source java.lang.Object
 */
public void complete(Object source) {
	if((source != null) && (source instanceof OptionsView)) {
		OptionsView ov = (OptionsView)source;
		Options options = ov.getOptions();
		try {
			getSpinOptionsTextField().setText(options.getCommandLineOptions());
		}
		catch(Exception e) {
			getSpinOptionsTextField().setText("An error occured while getting the configured options.");
		}
	}
}
/**
 * connEtoC1:  (SpinOptionGUIButton.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionWizardPanel.spinOptionGUIButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.spinOptionGUIButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
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
 * Return the SpinOptionGUIButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSpinOptionGUIButton() {
	if (ivjSpinOptionGUIButton == null) {
		try {
			ivjSpinOptionGUIButton = new javax.swing.JButton();
			ivjSpinOptionGUIButton.setName("SpinOptionGUIButton");
			ivjSpinOptionGUIButton.setToolTipText("Open the Spin option GUI provided by Bandera");
			ivjSpinOptionGUIButton.setText("Spin Option GUI");
			ivjSpinOptionGUIButton.setBackground(new java.awt.Color(204,204,255));
			ivjSpinOptionGUIButton.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSpinOptionGUIButton;
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 3:35:20 PM)
 * @return java.lang.String
 */
public String getSpinOptions() {
	return(getSpinOptionsTextField().getText());
}
/**
 * Return the SpinOptionsLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSpinOptionsLabel() {
	if (ivjSpinOptionsLabel == null) {
		try {
			ivjSpinOptionsLabel = new javax.swing.JLabel();
			ivjSpinOptionsLabel.setName("SpinOptionsLabel");
			ivjSpinOptionsLabel.setText("Spin Command Line Options");
			ivjSpinOptionsLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSpinOptionsLabel;
}
/**
 * Return the SpinOptionsTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getSpinOptionsTextField() {
	if (ivjSpinOptionsTextField == null) {
		try {
			ivjSpinOptionsTextField = new javax.swing.JTextField();
			ivjSpinOptionsTextField.setName("SpinOptionsTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSpinOptionsTextField;
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
	getSpinOptionGUIButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SpinOptionWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(496, 421);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsSpinOptionGUIButton = new java.awt.GridBagConstraints();
		constraintsSpinOptionGUIButton.gridx = 0; constraintsSpinOptionGUIButton.gridy = 1;
		constraintsSpinOptionGUIButton.gridwidth = 2;
		constraintsSpinOptionGUIButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSpinOptionGUIButton(), constraintsSpinOptionGUIButton);

		java.awt.GridBagConstraints constraintsSpinOptionsTextField = new java.awt.GridBagConstraints();
		constraintsSpinOptionsTextField.gridx = 1; constraintsSpinOptionsTextField.gridy = 2;
		constraintsSpinOptionsTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsSpinOptionsTextField.weightx = 1.0;
		constraintsSpinOptionsTextField.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSpinOptionsTextField(), constraintsSpinOptionsTextField);

		java.awt.GridBagConstraints constraintsSpinOptionsLabel = new java.awt.GridBagConstraints();
		constraintsSpinOptionsLabel.gridx = 0; constraintsSpinOptionsLabel.gridy = 2;
		constraintsSpinOptionsLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSpinOptionsLabel(), constraintsSpinOptionsLabel);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		SpinOptionWizardPanel aSpinOptionWizardPanel;
		aSpinOptionWizardPanel = new SpinOptionWizardPanel();
		frame.setContentPane(aSpinOptionWizardPanel);
		frame.setSize(aSpinOptionWizardPanel.getSize());
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
 * Creation date: (5/29/2002 3:35:11 PM)
 * @param spinOptions java.lang.String
 */
public void setSpinOptions(String spinOptions) {
	getSpinOptionsTextField().setText(spinOptions);
}
/**
 * This will open up a new Spin option GUI window that will allow
 * the user to configure the command line options using the GUI.  When
 * the user closes the GUI window, the command line options should
 * be shown in the text field.
 */
public void spinOptionGUIButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    String checkerOptions = getSpinOptionsTextField().getText();
    OptionsView optionsView = OptionsFactory.createOptionsViewInstance("Spin");
    Options options = OptionsFactory.createOptionsInstance("Spin");
    options.init(checkerOptions);
    optionsView.init(options);
    optionsView.registerCompletionListener(this);
    optionsView.setVisible(true);
    
}
}
