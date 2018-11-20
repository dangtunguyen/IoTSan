package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.*;
import java.io.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (4/18/2002 11:09:41 AM)
 * @author: 
 */
public final class MainClassWizardPanel extends BanderaAbstractWizardPanel {
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JButton ivjMainClassBrowseButton = null;
	private JLabel ivjMainClassLabel = null;
	private JTextField ivjMainClassTextField = null;
	private JTextArea ivjMessageTextArea = null;
	private final static java.lang.String message = "Bandera checks code reachable from an application's main method, and thus it requires a main method to be defined for the application. Select the source code (.java) file that contains the main method for your application and click continue.  The .java file that you select must exist and be readable by this application.";
	private File mainClassFile;
	private javax.swing.JFileChooser chooser;
	private java.io.File baseDirectory;
	private JPanel ivjBottomPanel = null;
	private JLabel ivjWizardLogoLabel = null;
	private final static Category log = Category.getInstance(MainClassWizardPanel.class);

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == MainClassWizardPanel.this.getMainClassBrowseButton()) 
				connEtoC1(e);
		};
	};
/**
 * MainClassWizardStep constructor comment.
 */
public MainClassWizardPanel() {
	super();
	initialize();
}
/**
 * connEtoC1:  (MainClassBrowseButton.action.actionPerformed(java.awt.event.ActionEvent) --> MainClassWizardStep.mainClassBrowseButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.mainClassBrowseButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (6/12/2002 1:49:54 PM)
 * @return java.io.File
 */
public java.io.File getBaseDirectory() {
	return baseDirectory;
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

			java.awt.GridBagConstraints constraintsMainClassBrowseButton = new java.awt.GridBagConstraints();
			constraintsMainClassBrowseButton.gridx = 2; constraintsMainClassBrowseButton.gridy = 0;
			constraintsMainClassBrowseButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBottomPanel().add(getMainClassBrowseButton(), constraintsMainClassBrowseButton);

			java.awt.GridBagConstraints constraintsMainClassTextField = new java.awt.GridBagConstraints();
			constraintsMainClassTextField.gridx = 1; constraintsMainClassTextField.gridy = 0;
			constraintsMainClassTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsMainClassTextField.weightx = 1.0;
			constraintsMainClassTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getBottomPanel().add(getMainClassTextField(), constraintsMainClassTextField);

			java.awt.GridBagConstraints constraintsMainClassLabel = new java.awt.GridBagConstraints();
			constraintsMainClassLabel.gridx = 0; constraintsMainClassLabel.gridy = 0;
			constraintsMainClassLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBottomPanel().add(getMainClassLabel(), constraintsMainClassLabel);
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
 * Return the MainClassBrowseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getMainClassBrowseButton() {
	if (ivjMainClassBrowseButton == null) {
		try {
			ivjMainClassBrowseButton = new javax.swing.JButton();
			ivjMainClassBrowseButton.setName("MainClassBrowseButton");
			ivjMainClassBrowseButton.setText("Browse");
			ivjMainClassBrowseButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassBrowseButton;
}
    public String getMainClassFilename() {
	if(mainClassFile != null) {
	    return(mainClassFile.getPath());
	}
	else {
	    return(getMainClassTextField().getText());
	}
    }
/**
 * Return the MainClassLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getMainClassLabel() {
	if (ivjMainClassLabel == null) {
		try {
			ivjMainClassLabel = new javax.swing.JLabel();
			ivjMainClassLabel.setName("MainClassLabel");
			ivjMainClassLabel.setText("Main Java Class");
			ivjMainClassLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassLabel;
}
/**
 * Return the MainClassTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getMainClassTextField() {
	if (ivjMainClassTextField == null) {
		try {
			ivjMainClassTextField = new javax.swing.JTextField();
			ivjMainClassTextField.setName("MainClassTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassTextField;
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
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getMainClassBrowseButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("MainClassWizardStep");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(535, 436);

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 0; constraintsWizardLogoLabel.gridy = 0;
		constraintsWizardLogoLabel.gridwidth = 2;
		constraintsWizardLogoLabel.weightx = 0.3;
		constraintsWizardLogoLabel.weighty = 0.9;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 2; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 0.9;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsBottomPanel = new java.awt.GridBagConstraints();
		constraintsBottomPanel.gridx = 1; constraintsBottomPanel.gridy = 1;
		constraintsBottomPanel.gridwidth = 2;
		constraintsBottomPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsBottomPanel.weightx = 0.7;
		constraintsBottomPanel.weighty = 0.1;
		constraintsBottomPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getBottomPanel(), constraintsBottomPanel);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	baseDirectory = File.listRoots()[1];
	chooser = new JFileChooser(baseDirectory);
	setTitle("Choose the Java Main Class");
	
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		MainClassWizardPanel aMainClassWizardPanel;
		aMainClassWizardPanel = new MainClassWizardPanel();
		frame.setContentPane(aMainClassWizardPanel);
		frame.setSize(aMainClassWizardPanel.getSize());
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
 * When the browse button is pressed, open up a file selection dialog
 * box.  Once a file is selected, put that text into the MainClassTextField.
 *
 * @param ActionEvent actionEvent ignored.
 */
public void mainClassBrowseButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	chooser.setCurrentDirectory(baseDirectory);
	chooser.setFileFilter(new JavaSourceFileFilter());
    int returnVal = chooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
       // set the variable mainClassFile to this file
       mainClassFile = chooser.getSelectedFile();
       
       // set the text field to the string of this file
       getMainClassTextField().setText(mainClassFile.getPath());
    }

    baseDirectory = chooser.getCurrentDirectory(); 
}
/**
 * Insert the method's description here.
 * Creation date: (6/12/2002 1:49:54 PM)
 * @param newBaseDirectory java.io.File
 */
public void setBaseDirectory(java.io.File newBaseDirectory) {
	baseDirectory = newBaseDirectory;
}
/**
 * Insert the method's description here.
 * Creation date: (4/26/2002 6:23:18 PM)
 * @param mainClassFilename java.lang.String
 */
public void setMainClassFilename(String mainClassFilename) {

	getMainClassTextField().setText(mainClassFilename);
	
}
/**
  * For this step to be valid, the file must be a .java file and it must be readable.
  *
  * At the end of this and the answer is true, we will know that:
  * 1) the main class file exists.
  * 2) the main class file is readable.
  * 3) the main class file has a .java extension.
  */
public boolean validateNext(java.util.List list) {

    // check to see if the user has selected or entered a value for the main class file
    String mainClassFileText = getMainClassTextField().getText();
    if ((mainClassFile == null) && (mainClassFileText == null)) {
        String errorMessage =
            "No main class file was selected and no main class file was entered.";
        list.add(errorMessage);
        return (false);
    }

    // check to make sure that the file selected and the file name entered match
    if ((mainClassFile != null)
        && (mainClassFileText != null)
        && (mainClassFile.getPath().equals(mainClassFileText))) {
        // the mainClassFile and mainClassFileText don't match, use the mainClassFileText
        mainClassFile = new File(mainClassFileText);
    }

    // if the mainClassFile has not been defined by the mainClassFileText is, create the file    
    if ((mainClassFile == null) && (mainClassFileText != null)) {
        mainClassFile = new File(mainClassFileText);
    }

    // make sure that the mainClassFile has a .java extension
    if (!(mainClassFile.getPath().endsWith(".java"))) {
        String errorMessage =
            "The mainClassFile (" + mainClassFile.getPath() + ") does not have a .java extension."
                + "  This is a sign that the main class file is not a java source file."
                + "  Please choose a valid Java source file.";
        list.add(errorMessage);
        return (false);
    }

    // make sure this is actually a file (and not a directory)
    if (!(mainClassFile.isFile())) {
        String errorMessage =
            "The file you entered, "
                + mainClassFile.getPath()
                + ", is not a file.  Please choose a file.";
        list.add(errorMessage);
        return (false);
    }

    // make sure the main class file is readable
    if (!(mainClassFile.canRead())) {
        String errorMessage =
            "The file you entered, "
                + mainClassFile.getPath()
                + ", could not be read.  Please choose another file.";
        list.add(errorMessage);
        return (false);
    }

    return (true);
}
}
