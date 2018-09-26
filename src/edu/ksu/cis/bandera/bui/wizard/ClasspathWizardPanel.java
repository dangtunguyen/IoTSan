package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.*;
import java.io.File;
import java.util.*;
import edu.ksu.cis.bandera.bui.wizard.jwf.WizardPanel;
import org.apache.log4j.Category;

/**
 * Insert the type's description here.
 * Creation date: (4/18/2002 11:20:56 AM)
 * @author: 
 */
public final class ClasspathWizardPanel extends BanderaAbstractWizardPanel {
	private JButton ivjClasspathAddButton = null;
	private JLabel ivjClasspathLabel = null;
	private JList ivjClasspathList = null;
	private JButton ivjClasspathRemoveButton = null;
	private JScrollPane ivjClasspathScrollPane = null;
	private JTextArea ivjMessageTextArea = null;
	private JPanel ivjSelectionPanel = null;
	private final static java.lang.String message = "Bandera needs to be able to find Java source code (.java) files and Java class files for all the classes reached (referred to) when executing the application's main method.  This means Bandera needs to be given a classpath (e.g., as you might use when compiling your application) that covers the locations of all reachable .java and .class files.\n\nIf there are other Java source files, Java class files, Java archive files, or directories besides the file containing the application's main method that are necessary to compile and run the application, please provide the classpath below that includes their location or simply hit the next button if no other needed files are located outside of the directory containing the root class.\n\nTo add to the classpath, click the add button.  This will pop-up a file choosing dialog box in which you may select one or more files and/or directories to add to the classpath.  If you want to remove any of the items in the classpath, you may select them and press the remove button.\n\nOnce you are done adding files and/or directories to the classpath, press the next button.  At that point, I will compile your application.  If an error occurs, you will be brought back to this screen to attempt to set the classpath correctly or possibly reset the main class name.  This step may take a little while.";
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JFileChooser chooser;
	/**
	  * The log we will write messages to.
	  */
	private static final Category log = Category.getInstance(ClasspathWizardPanel.class);
	private java.io.File baseDirectory;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ClasspathWizardPanel.this.getClasspathAddButton()) 
				connEtoC1(e);
			if (e.getSource() == ClasspathWizardPanel.this.getClasspathRemoveButton()) 
				connEtoC2(e);
		};
	};
/**
 * ClasspathWizardPanel constructor comment.
 */
public ClasspathWizardPanel() {
	super();
	initialize();
}
/**
 * When the add button comes up, we need to provide the user a file
 * chooser dialog.  When a file, or files, are selected, we need to
 * add them to the ClasspathList.
 *
 * @post The chooser will only change the file location after this method.
 */
public void classpathAddButton_ActionPerformed(
    java.awt.event.ActionEvent actionEvent) {

	    chooser.setCurrentDirectory(baseDirectory);
    chooser.setFileFilter(new ClasspathFileFilter());
    chooser.setMultiSelectionEnabled(true);
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    int returnVal = chooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        // add each file selected to the ClasspathList
        JList classpathList = getClasspathList();
        ListModel listModel = classpathList.getModel();
        if ((listModel != null) && (listModel instanceof DefaultListModel)) {
            DefaultListModel defaultListModel = (DefaultListModel) listModel;
            File[] files = chooser.getSelectedFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String currentFilename = files[i].getPath();
                    defaultListModel.addElement(currentFilename);
                }
            }
        }
        else {
            log.warn("listModel = " + listModel);
            log.warn(
                "Not a default list model.  listModel.getClass().getName() = "
                    + listModel.getClass().getName());
        }
    }

	baseDirectory = chooser.getCurrentDirectory();
}
/**
 * When this button is clicked, we need to remove all elements from
 * the ClasspathList that are selected.
 *
 * @param ActionEvent actionEvent ignored
 */
public void classpathRemoveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	JList classpathList = getClasspathList();
	int [] selectedIndices = classpathList.getSelectedIndices();
	if(selectedIndices != null) {
		DefaultListModel listModel = (DefaultListModel)classpathList.getModel();
		for (int i = 0; i < selectedIndices.length; i++) {
			listModel.remove(selectedIndices[i]);
		}
	}
}
/**
 * connEtoC1:  (ClasspathAddButton.action.actionPerformed(java.awt.event.ActionEvent) --> ClasspathWizardPanel.classpathAddButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathAddButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (ClasspathRemoveButton.action.actionPerformed(java.awt.event.ActionEvent) --> ClasspathWizardPanel.classpathRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathRemoveButton_ActionPerformed(arg1);
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
 * Creation date: (6/12/2002 1:51:17 PM)
 * @return java.io.File
 */
public java.io.File getBaseDirectory() {
	return baseDirectory;
}
/**
 * Get the classpath as a single string.
 *
 * @return String A String of all the files and directories that should be in
 *         the classpath.
 */
public String getClasspath() {
	StringBuffer classpathStringBuffer = new StringBuffer();
	
	String seperator = System.getProperty("path.separator");
	log.debug("seperator = " + seperator);

	// walk through the list appending the next file in the list to the
	//  end of the classpath.	
    JList classpathList = getClasspathList();
    ListModel listModel = classpathList.getModel();
    if ((listModel != null) && (listModel instanceof DefaultListModel)) {
        DefaultListModel defaultListModel = (DefaultListModel) listModel;
        for (int i = 0; i < defaultListModel.size(); i++) {
            String currentFilename = (String) defaultListModel.get(i);
            classpathStringBuffer.append(currentFilename);
	    if(i + 1 < defaultListModel.size()) {
		classpathStringBuffer.append(seperator);
	    }
        }
    }
    else {
	log.error("The list model was not a DefaultListModel.  Returning an empty classpath.");
	classpathStringBuffer = new StringBuffer(); // very inefficient. -tcw
    }

    log.debug("classpath = " + classpathStringBuffer.toString());
    return(classpathStringBuffer.toString());
}
/**
 * Return the ClasspathAddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClasspathAddButton() {
	if (ivjClasspathAddButton == null) {
		try {
			ivjClasspathAddButton = new javax.swing.JButton();
			ivjClasspathAddButton.setName("ClasspathAddButton");
			ivjClasspathAddButton.setText("Add");
			ivjClasspathAddButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathAddButton;
}
/**
 * Return the ClasspathLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getClasspathLabel() {
	if (ivjClasspathLabel == null) {
		try {
			ivjClasspathLabel = new javax.swing.JLabel();
			ivjClasspathLabel.setName("ClasspathLabel");
			ivjClasspathLabel.setText("Classpath");
			ivjClasspathLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathLabel;
}
/**
 * Return the ClasspathList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getClasspathList() {
	if (ivjClasspathList == null) {
		try {
			ivjClasspathList = new javax.swing.JList();
			ivjClasspathList.setName("ClasspathList");
			ivjClasspathList.setModel(new javax.swing.DefaultListModel());
			ivjClasspathList.setBackground(new java.awt.Color(204,204,204));
			ivjClasspathList.setBounds(0, 0, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathList;
}
/**
 * Return the ClasspathRemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClasspathRemoveButton() {
	if (ivjClasspathRemoveButton == null) {
		try {
			ivjClasspathRemoveButton = new javax.swing.JButton();
			ivjClasspathRemoveButton.setName("ClasspathRemoveButton");
			ivjClasspathRemoveButton.setText("Remove");
			ivjClasspathRemoveButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathRemoveButton;
}
/**
 * Return the ClasspathScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getClasspathScrollPane() {
	if (ivjClasspathScrollPane == null) {
		try {
			ivjClasspathScrollPane = new javax.swing.JScrollPane();
			ivjClasspathScrollPane.setName("ClasspathScrollPane");
			getClasspathScrollPane().setViewportView(getClasspathList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathScrollPane;
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
 * Return the SelectionPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSelectionPanel() {
	if (ivjSelectionPanel == null) {
		try {
			ivjSelectionPanel = new javax.swing.JPanel();
			ivjSelectionPanel.setName("SelectionPanel");
			ivjSelectionPanel.setLayout(new java.awt.GridBagLayout());
			ivjSelectionPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsClasspathLabel = new java.awt.GridBagConstraints();
			constraintsClasspathLabel.gridx = 0; constraintsClasspathLabel.gridy = 0;
			constraintsClasspathLabel.gridwidth = 2;
			constraintsClasspathLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsClasspathLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getSelectionPanel().add(getClasspathLabel(), constraintsClasspathLabel);

			java.awt.GridBagConstraints constraintsClasspathAddButton = new java.awt.GridBagConstraints();
			constraintsClasspathAddButton.gridx = 1; constraintsClasspathAddButton.gridy = 1;
			constraintsClasspathAddButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsClasspathAddButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsClasspathAddButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSelectionPanel().add(getClasspathAddButton(), constraintsClasspathAddButton);

			java.awt.GridBagConstraints constraintsClasspathRemoveButton = new java.awt.GridBagConstraints();
			constraintsClasspathRemoveButton.gridx = 1; constraintsClasspathRemoveButton.gridy = 2;
			constraintsClasspathRemoveButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsClasspathRemoveButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsClasspathRemoveButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSelectionPanel().add(getClasspathRemoveButton(), constraintsClasspathRemoveButton);

			java.awt.GridBagConstraints constraintsClasspathScrollPane = new java.awt.GridBagConstraints();
			constraintsClasspathScrollPane.gridx = 0; constraintsClasspathScrollPane.gridy = 1;
constraintsClasspathScrollPane.gridheight = 2;
			constraintsClasspathScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsClasspathScrollPane.weightx = 1.0;
			constraintsClasspathScrollPane.weighty = 1.0;
			constraintsClasspathScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getSelectionPanel().add(getClasspathScrollPane(), constraintsClasspathScrollPane);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSelectionPanel;
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
	getClasspathAddButton().addActionListener(ivjEventHandler);
	getClasspathRemoveButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("ClasspathWizardStep");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(523, 432);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 3;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsSelectionPanel = new java.awt.GridBagConstraints();
		constraintsSelectionPanel.gridx = 2; constraintsSelectionPanel.gridy = 1;
		constraintsSelectionPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsSelectionPanel.weightx = 1.0;
		constraintsSelectionPanel.weighty = 1.0;
		constraintsSelectionPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getSelectionPanel(), constraintsSelectionPanel);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	baseDirectory = File.listRoots()[1];
	chooser = new JFileChooser(baseDirectory);
	setTitle("Set a Classpath");
	
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		ClasspathWizardPanel aClasspathWizardPanel;
		aClasspathWizardPanel = new ClasspathWizardPanel();
		frame.setContentPane(aClasspathWizardPanel);
		frame.setSize(aClasspathWizardPanel.getSize());
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
 * Insert the method's description here.
 * Creation date: (6/12/2002 1:51:17 PM)
 * @param newBaseDirectory java.io.File
 */
public void setBaseDirectory(java.io.File newBaseDirectory) {
	baseDirectory = newBaseDirectory;
}
/**
 * Set the classpath as defined externally.  This allows the controller to set
 * a pre-existing classpath for this wizard panel.
 *
 * @param classpathList java.util.List
 */
public void setClasspathList(List classpathList) {

	if(classpathList == null) {
		return;
	}

    JList cl = getClasspathList();
    ListModel listModel = cl.getModel();
    if ((listModel != null) && (listModel instanceof DefaultListModel)) {
        DefaultListModel defaultListModel = (DefaultListModel) listModel;
        for (int i = 0; i < classpathList.size(); i++) {
            String currentFilename = (String) classpathList.get(i);
            defaultListModel.addElement(currentFilename);
        }
    }
    else {
        log.warn("listModel = " + listModel);
        log.warn(
            "Not a default list model.  listModel.getClass().getName() = "
                + listModel.getClass().getName());
    }
}
}
