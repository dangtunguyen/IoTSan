package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsFactory;
import edu.ksu.cis.bandera.checker.OptionsView;
import edu.ksu.cis.bandera.checker.CompletionListener;

import edu.ksu.cis.bandera.checker.jpf.JPFOptions;
import edu.ksu.cis.bandera.checker.jpf.JPFOptionsView;

/**
 * Insert the type's description here.
 * Creation date: (5/29/2002 1:09:10 PM)
 * @author: 
 */
public final class JPFOptionWizardPanel extends BanderaAbstractWizardPanel implements edu.ksu.cis.bandera.checker.CompletionListener {
	private javax.swing.JButton ivjJPFOptionGUIButton = null;
	private javax.swing.JLabel ivjJPFOptionLabel = null;
	private javax.swing.JTextField ivjJPFOptionTextField = null;
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private final static java.lang.String message = "Currently, I don't knjow how to walk you through the configuration of JPF command line options.  If you wish to configure them using a graphical interface, you can press the button and the GUI included with Bandera will be used to configure the JPF options.  However, if you would like to modify them yourself you may edit the text box below.  If you don't want to configure them (and use the defaults) just press the next button.";
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JPFOptions jpfOptions;
	private final static Category log = Category.getInstance(JPFOptionWizardPanel.class);

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == JPFOptionWizardPanel.this.getJPFOptionGUIButton()) 
				connEtoC1(e);
		};
	};
/**
 * JPFOptionWizardPanel constructor comment.
 */
public JPFOptionWizardPanel() {
	super();
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/2002 10:34:50 PM)
 * @param source java.lang.Object
 */
public void complete(Object source) {
	if((source != null) && (source instanceof OptionsView)) {
		OptionsView ov = (OptionsView)source;
		Options options = ov.getOptions();
		try {
			getJPFOptionTextField().setText(options.getCommandLineOptions());
		}
		catch(Exception e) {
			getJPFOptionTextField().setText("An error occured while getting the configured options.");
		}
	}
}
/**
 * connEtoC1:  (JPFOptionGUIButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionWizardPanel.jPFOptionGUIButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jPFOptionGUIButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Return the JPFOptionGUIButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJPFOptionGUIButton() {
	if (ivjJPFOptionGUIButton == null) {
		try {
			ivjJPFOptionGUIButton = new javax.swing.JButton();
			ivjJPFOptionGUIButton.setName("JPFOptionGUIButton");
			ivjJPFOptionGUIButton.setToolTipText("Open up the JPF Option GUI provided by Bandera");
			ivjJPFOptionGUIButton.setText("JPF Option GUI");
			ivjJPFOptionGUIButton.setBackground(new java.awt.Color(204,204,255));
			ivjJPFOptionGUIButton.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFOptionGUIButton;
}
/**
 * Return the JPFOptionLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJPFOptionLabel() {
	if (ivjJPFOptionLabel == null) {
		try {
			ivjJPFOptionLabel = new javax.swing.JLabel();
			ivjJPFOptionLabel.setName("JPFOptionLabel");
			ivjJPFOptionLabel.setText("JPF Command Line Options");
			ivjJPFOptionLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFOptionLabel;
}
/**
 * Insert the method's description here.
 * Creation date: (5/29/2002 1:13:28 PM)
 * @return java.lang.String
 */
public String getJPFOptions() {
	return(getJPFOptionTextField().getText());
}
/**
 * Return the JPFOptionTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJPFOptionTextField() {
	if (ivjJPFOptionTextField == null) {
		try {
			ivjJPFOptionTextField = new javax.swing.JTextField();
			ivjJPFOptionTextField.setName("JPFOptionTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFOptionTextField;
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
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getJPFOptionGUIButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("JPFOptionWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(559, 441);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsJPFOptionTextField = new java.awt.GridBagConstraints();
		constraintsJPFOptionTextField.gridx = 1; constraintsJPFOptionTextField.gridy = 2;
		constraintsJPFOptionTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsJPFOptionTextField.weightx = 1.0;
		constraintsJPFOptionTextField.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getJPFOptionTextField(), constraintsJPFOptionTextField);

		java.awt.GridBagConstraints constraintsJPFOptionGUIButton = new java.awt.GridBagConstraints();
		constraintsJPFOptionGUIButton.gridx = 0; constraintsJPFOptionGUIButton.gridy = 1;
		constraintsJPFOptionGUIButton.gridwidth = 2;
		constraintsJPFOptionGUIButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getJPFOptionGUIButton(), constraintsJPFOptionGUIButton);

		java.awt.GridBagConstraints constraintsJPFOptionLabel = new java.awt.GridBagConstraints();
		constraintsJPFOptionLabel.gridx = 0; constraintsJPFOptionLabel.gridy = 2;
		constraintsJPFOptionLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getJPFOptionLabel(), constraintsJPFOptionLabel);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * Open up the Bandera JPF Options GUI and let the user configure the
 * options.  Once they are done, take the options from it and put
 * them into the text field.
 */
public void jPFOptionGUIButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    String checkerOptions = getJPFOptionTextField().getText();
    OptionsView optionsView = OptionsFactory.createOptionsViewInstance("JPF");
    Options options = OptionsFactory.createOptionsInstance("JPF");
    options.init(checkerOptions);
    optionsView.init(options);
    optionsView.registerCompletionListener(this);
    optionsView.setVisible(true);
    
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		JPFOptionWizardPanel aJPFOptionWizardPanel;
		aJPFOptionWizardPanel = new JPFOptionWizardPanel();
		frame.setContentPane(aJPFOptionWizardPanel);
		frame.setSize(aJPFOptionWizardPanel.getSize());
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
 * Creation date: (5/29/2002 1:14:02 PM)
 * @param jpfOptions java.lang.String
 */
public void setJPFOptions(String jpfOptions) {

	getJPFOptionTextField().setText(jpfOptions);
	
}
}
