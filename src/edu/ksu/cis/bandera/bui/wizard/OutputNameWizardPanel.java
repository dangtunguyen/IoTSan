package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (5/28/2002 2:37:27 PM)
 * @author: 
 */
public final class OutputNameWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private javax.swing.JLabel ivjOutputNameLabel = null;
	private javax.swing.JTextField ivjOutputNameTextField = null;
	private final static java.lang.String message = "When Bandera runs, it will use temporary locations to store intermediate results.  Those temporary results will be stored in a directory structure based upon an output name.  If you wish to set that name, change the default value provided below.  If the default is acceptable, just press the next button.  The output name can only consist of any number of letters, numbers, '-', and '_' characters.\n\nA good choice of output name would be to re-use the name of the session or the name of the application.  This will make it easier to track down the temporary files if necessary.";
	private javax.swing.JPanel ivjBottomPanel = null;
	private javax.swing.JLabel ivjWizardLogoLabel = null;
	private final static Category log = Category.getInstance(OutputNameWizardPanel.class);
/**
 * OutputNameWizardPanel constructor comment.
 */
public OutputNameWizardPanel() {
	super();
	initialize();
}
/**
 * Return the BottomPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBottomPanel() {
	if (ivjBottomPanel == null) {
		try {
			ivjBottomPanel = new javax.swing.JPanel();
			ivjBottomPanel.setName("BottomPanel");
			ivjBottomPanel.setLayout(new java.awt.GridBagLayout());
			ivjBottomPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsOutputNameTextField = new java.awt.GridBagConstraints();
			constraintsOutputNameTextField.gridx = 1; constraintsOutputNameTextField.gridy = 0;
			constraintsOutputNameTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsOutputNameTextField.weightx = 1.0;
			constraintsOutputNameTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getBottomPanel().add(getOutputNameTextField(), constraintsOutputNameTextField);

			java.awt.GridBagConstraints constraintsOutputNameLabel = new java.awt.GridBagConstraints();
			constraintsOutputNameLabel.gridx = 0; constraintsOutputNameLabel.gridy = 0;
			constraintsOutputNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBottomPanel().add(getOutputNameLabel(), constraintsOutputNameLabel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBottomPanel;
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
 * Insert the method's description here.
 * Creation date: (5/28/2002 2:41:56 PM)
 * @return java.lang.String
 */
public String getOutputName() {
	return(getOutputNameTextField().getText());
}
/**
 * Return the OutputNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getOutputNameLabel() {
	if (ivjOutputNameLabel == null) {
		try {
			ivjOutputNameLabel = new javax.swing.JLabel();
			ivjOutputNameLabel.setName("OutputNameLabel");
			ivjOutputNameLabel.setText("Output Name");
			ivjOutputNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOutputNameLabel;
}
/**
 * Return the OutputNameTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getOutputNameTextField() {
	if (ivjOutputNameTextField == null) {
		try {
			ivjOutputNameTextField = new javax.swing.JTextField();
			ivjOutputNameTextField.setName("OutputNameTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOutputNameTextField;
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
		setName("OutputNameWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(538, 422);

		java.awt.GridBagConstraints constraintsBottomPanel = new java.awt.GridBagConstraints();
		constraintsBottomPanel.gridx = 0; constraintsBottomPanel.gridy = 1;
		constraintsBottomPanel.gridwidth = 2;
		constraintsBottomPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsBottomPanel.weightx = 1.0;
		constraintsBottomPanel.weighty = 0.1;
		constraintsBottomPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getBottomPanel(), constraintsBottomPanel);

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 0; constraintsWizardLogoLabel.gridy = 0;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 1; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Set the Name for Output");
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		OutputNameWizardPanel aOutputNameWizardPanel;
		aOutputNameWizardPanel = new OutputNameWizardPanel();
		frame.setContentPane(aOutputNameWizardPanel);
		frame.setSize(aOutputNameWizardPanel.getSize());
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
 * Creation date: (5/28/2002 2:41:46 PM)
 * @param outputName java.lang.String
 */
public void setOutputName(String outputName) {
	getOutputNameTextField().setText(outputName);
}
/**
  * For this to be a valid step, the user must enter a name into the text field that:
  * <ol>
  * <li>Is at least 1 character.</li>
  * <li>When used to create a directory name, that directory will have a valid name:
  *   <ul>
  *   <li>a-z</li>
  *   <li>A-Z</li>
  *   <li>0-9</li>
  *   <li>_</li>
  *   <li>-</li>
  *   </ul>
  * </ol>
  *
  * @param list a List of error messages to be displayed.
  * @return true if the panel is valid,
  */
public boolean validateNext(java.util.List list) {

    String outputName = getOutputName();
    if ((outputName == null) || (outputName.length() < 1)) {
        list.add("You must provide an output name.");
        return (false);
    }

    for (int i = 0; i < outputName.length(); i++) {
        char currentCharacter = outputName.charAt(i);

        if (!(Character.isLetterOrDigit(currentCharacter))
            && (currentCharacter != '-')
            && (currentCharacter != '_')) {

            list.add(
                "The output name given, "
                    + outputName
                    + " is invalid because of this character, "
                    + currentCharacter
                    + ".  Please enter a different, valid, output name.");
            return (false);
        }

    }

    // if all tests are passed, return true!
    return (true);
}
}
