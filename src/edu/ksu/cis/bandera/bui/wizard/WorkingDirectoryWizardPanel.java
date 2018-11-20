package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import javax.swing.*;
import java.io.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (5/28/2002 2:45:54 PM)
 * @author: 
 */
public final class WorkingDirectoryWizardPanel extends BanderaAbstractWizardPanel {
	private JButton ivjBrowseButton = null;
	private JTextArea ivjMessageTextArea = null;
	private JTextField ivjWorkingDirectoryTextField = null;
	private final static java.lang.String message = "When Bandera runs, it will create several temporary files and directories.  If you wish to use another location besides the default, please select it below.";
	private JFileChooser chooser;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private File workingDirectory;
	private java.io.File baseDirectory;
	private JPanel ivjBottomPanel = null;
	private JLabel ivjWizardLogoLabel = null;
	private final static Category log = Category.getInstance(WorkingDirectoryWizardPanel.class);

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == WorkingDirectoryWizardPanel.this.getBrowseButton()) 
				connEtoC1(e);
		};
	};
/**
 * WorkingDirectoryWizardPanel constructor comment.
 */
public WorkingDirectoryWizardPanel() {
	super();
	initialize();
}
/**
 * When the user presses the Browse button, open up a dialog box so that they may choose a directory
 * in which to place the files.
 *
 * @param ActionEvent actionEvent ignored.
 */
public void browseButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	chooser.setCurrentDirectory(baseDirectory);
 	chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    int returnVal = chooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
       // set the variable workingDirectory to this file
       workingDirectory = chooser.getSelectedFile();
       
       // set the text field to the string of this file
       getWorkingDirectoryTextField().setText(workingDirectory.getPath());
    }

	baseDirectory = chooser.getCurrentDirectory();
}
/**
 * connEtoC1:  (BrowseButton.action.actionPerformed(java.awt.event.ActionEvent) --> WorkingDirectoryWizardPanel.browseButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.browseButton_ActionPerformed(arg1);
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
 * Creation date: (6/12/2002 12:46:27 PM)
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

			java.awt.GridBagConstraints constraintsBrowseButton = new java.awt.GridBagConstraints();
			constraintsBrowseButton.gridx = 1; constraintsBrowseButton.gridy = 0;
			constraintsBrowseButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBottomPanel().add(getBrowseButton(), constraintsBrowseButton);

			java.awt.GridBagConstraints constraintsWorkingDirectoryTextField = new java.awt.GridBagConstraints();
			constraintsWorkingDirectoryTextField.gridx = 0; constraintsWorkingDirectoryTextField.gridy = 0;
			constraintsWorkingDirectoryTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsWorkingDirectoryTextField.weightx = 1.0;
			constraintsWorkingDirectoryTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getBottomPanel().add(getWorkingDirectoryTextField(), constraintsWorkingDirectoryTextField);
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
 * Return the BrowseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBrowseButton() {
	if (ivjBrowseButton == null) {
		try {
			ivjBrowseButton = new javax.swing.JButton();
			ivjBrowseButton.setName("BrowseButton");
			ivjBrowseButton.setText("Browse");
			ivjBrowseButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBrowseButton;
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
			ivjMessageTextArea.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
 * Insert the method's description here.
 * Creation date: (5/28/2002 2:59:01 PM)
 * @return java.io.File
 */
public java.io.File getWorkingDirectory() {
	return workingDirectory;
}
/**
 * Return the WorkingDirectoryTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getWorkingDirectoryTextField() {
	if (ivjWorkingDirectoryTextField == null) {
		try {
			ivjWorkingDirectoryTextField = new javax.swing.JTextField();
			ivjWorkingDirectoryTextField.setName("WorkingDirectoryTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWorkingDirectoryTextField;
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
	getBrowseButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("WorkingDirectoryWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(478, 451);

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 0; constraintsWizardLogoLabel.gridy = 0;
		constraintsWizardLogoLabel.weightx = 1.0;
		constraintsWizardLogoLabel.weighty = 0.9;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 1; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 0.9;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsBottomPanel = new java.awt.GridBagConstraints();
		constraintsBottomPanel.gridx = 0; constraintsBottomPanel.gridy = 1;
		constraintsBottomPanel.gridwidth = 2;
		constraintsBottomPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsBottomPanel.weightx = 1.0;
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
	setTitle("Choose a Working Directory");

	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		WorkingDirectoryWizardPanel aWorkingDirectoryWizardPanel;
		aWorkingDirectoryWizardPanel = new WorkingDirectoryWizardPanel();
		frame.setContentPane(aWorkingDirectoryWizardPanel);
		frame.setSize(aWorkingDirectoryWizardPanel.getSize());
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
 * Creation date: (6/12/2002 12:46:27 PM)
 * @param newBaseDirectory java.io.File
 */
public void setBaseDirectory(java.io.File newBaseDirectory) {
	baseDirectory = newBaseDirectory;
}
/**
 *
 *
 * @param File newWorkingDirectory
 * @pre newWorkingDirectory != null
 */
public void setWorkingDirectory(java.io.File newWorkingDirectory) {

    if(newWorkingDirectory == null) {
	log.warn("newWorkingDirectory is null.  Cannot set a null working directory.");
	workingDirectory = null;
	getWorkingDirectoryTextField().setText("");
    }
    else {
	workingDirectory = newWorkingDirectory;
	getWorkingDirectoryTextField().setText(workingDirectory.getPath());
    }
}
/**
  * For this step to be valid, it must have a directory selected that exists (or it will be
  * created) and is readable and writable.
  *
  * @param List list a List of error messages to be shown to the user.
  * @return boolean True if this step is valid, false otherwise.
  */
public boolean validateNext(java.util.List list) {

    String directoryName = getWorkingDirectoryTextField().getText();
    if ((directoryName == null) || (directoryName.length() < 1)) {
        list.add(
            "You must enter a valid directory name in the text field or choose one using the browse button.");
        return(false);
    }

    // assume: directoryName != null and length > 0

    if((workingDirectory != null) && (!(workingDirectory.getPath().equals(directoryName)))) {
	    // the workingDirectory doesn't match the one specified by the user in the text field
	    //  reset the workingDirectory to null so that it will be created from the text field value!
	    workingDirectory = null;
    }
    
    if(workingDirectory == null) {
	    // the user has entered a working directory in the text field
	    workingDirectory = new File(directoryName);
	    if(!(workingDirectory.exists())) {
		    // the file does not exist, ask the user if we should create it?
		    int response = JOptionPane.showConfirmDialog(this, "The file you entered does not exist.  Shall I create it for you?", "Create Directory?",
			    JOptionPane.YES_NO_OPTION);
		    if(response != JOptionPane.YES_OPTION) {
			    list.add("You chose to not create the directory.  Please choose another directory or create a new one.");
			    return(false);
		    }

		    // create the directory
		    try {
		    	boolean fileCreated = workingDirectory.createNewFile();
		    	if(!(fileCreated)) {
			    	list.add("The directory specified could not be created.  Please select one or create a new one.");
			    	return(false);
		    	}
		    }
		    catch(Exception e) {
			    list.add("An exception was thrown while creating the specified directory: " + e.toString());
			    return(false);
		    }
	    }
    }

    // assume: workingDirectory != null and it exists and it matches the directoryName in the text field

    if(!(workingDirectory.isDirectory())) {
	    list.add("The working directory specified is not a directory.");
	    return(false);
    }

    if(!(workingDirectory.canRead())) {
	    list.add("The working directory specified cannot be read.");
	    return(false);
    }
    
    if(!(workingDirectory.canWrite())) {
	    list.add("The working directory specified cannot be written to.");
	    return(false);
    }

    // since it has passed all tests at this point, return true to signify this step has validated!
    return (true);
}
}
