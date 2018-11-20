package edu.ksu.cis.bandera.sessions.ui.gui;

import edu.ksu.cis.bandera.sessions.parser.util.SessionFileConverter;
import java.io.File;
import javax.swing.*;

/**
 * The SessionFileConverterView provides an easy way to convert Session files
 * from one version to another.  It allows a user to do several conversions at
 * one time (in a batch mode).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:42 $ 
 */
public final class SessionFileConverterView extends JFrame {
	private JButton ivjAddConversionButton = null;
	private JPanel ivjAddSessionButtonPanel = null;
	private JButton ivjAddSessionCancelButton = null;
	private JDialog ivjAddSessionFileDialog = null;
	private JButton ivjAddSessionOkButton = null;
	private JLabel ivjConversionListLabel = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JPanel ivjJDialogContentPane = null;
	private JPanel ivjJFrameContentPane = null;
	private JButton ivjRemoveConversionButton = null;
	private JList ivjSessionFileConversionList = null;
	private JScrollPane ivjSessionFileConversionListScrollPane = null;
	private JButton ivjSourceBrowseButton = null;
	private JLabel ivjSourceSessionFileLabel = null;
	private JTextField ivjSourceTextField = null;
	private JButton ivjStartConversionButton = null;
	private JButton ivjTargetBrowseButton = null;
	private JLabel ivjTargetSessionLabel = null;
	private JTextField ivjTargetTextField = null;
	private JComboBox ivjVersionNumberComboBox = null;
	private javax.swing.JFileChooser fileChooser;
	public final static int ERROR_STATUS = 3;
	public final static int WORKING_STATUS = 1;
	public final static int COMPLETE_STATUS = 2;
	public final static int DEFAULT_STATUS = 0;
	private JLabel ivjSessionConversionStatusLabel = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SessionFileConverterView.this.getAddConversionButton()) 
				connEtoM1(e);
			if (e.getSource() == SessionFileConverterView.this.getAddSessionOkButton()) 
				connEtoM2(e);
			if (e.getSource() == SessionFileConverterView.this.getAddSessionCancelButton()) 
				connEtoM3(e);
			if (e.getSource() == SessionFileConverterView.this.getAddSessionOkButton()) 
				connEtoC1(e);
			if (e.getSource() == SessionFileConverterView.this.getStartConversionButton()) 
				connEtoC2(e);
			if (e.getSource() == SessionFileConverterView.this.getSourceBrowseButton()) 
				connEtoC3(e);
			if (e.getSource() == SessionFileConverterView.this.getTargetBrowseButton()) 
				connEtoC4(e);
			if (e.getSource() == SessionFileConverterView.this.getRemoveConversionButton()) 
				connEtoC5(e);
			if (e.getSource() == SessionFileConverterView.this.getDoneConversionButton()) 
				connEtoC6(e);
		};
	};
	private JButton ivjDoneConversionButton = null;
/**
 * SessionFileConverterView constructor comment.
 */
public SessionFileConverterView() {
	super();
	initialize();
}
/**
 * SessionFileConverterView constructor comment.
 * @param title java.lang.String
 */
public SessionFileConverterView(String title) {
	super(title);
}
/**
 * When the OK button is pressed, take the source file, the destination
 * file, and the version number and add them to the list of files
 * to be converted.  This means wrapping them into a List and adding
 * it to the ConversionList (JList).
 */
public void addSessionOkButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	String sourceFileString = getSourceTextField().getText();
	File sourceFile = new File(sourceFileString);
	// make sure it exists, is a file, and is readable
	String targetFileString = getTargetTextField().getText();
	File targetFile = new File(targetFileString);
	// make sure it is a file and is writable -> overwrite automatically
	String versionString = getVersionNumberComboBox().getSelectedItem().toString();
	Integer version = new Integer(edu.ksu.cis.bandera.sessions.parser.ParserFactory.DEFAULT_PARSER);
	try {
		version = Integer.valueOf(versionString);
	}
	catch(NumberFormatException nfe) {
		System.err.println("Cannot convert " + versionString + " into an Integer!  This is a major problem!!!");
		version = new Integer(edu.ksu.cis.bandera.sessions.parser.ParserFactory.DEFAULT_PARSER);
	}

	java.util.List l = new java.util.ArrayList(4);
	l.add(sourceFile);
	l.add(targetFile);
	l.add(version);
	l.add(new Integer(DEFAULT_STATUS));
	// add this list to the conversion list
	JList conversionList = getSessionFileConversionList();
	ListModel lm = conversionList.getModel();
	if(lm instanceof DefaultListModel) {
		DefaultListModel dlm = (DefaultListModel)lm;
		dlm.addElement(l);
		System.out.println("Added a conversion: source = " + sourceFile.toString() + ", target = " + targetFile.toString() + ", version = " + version.toString());
	}
	else {
		System.out.println("Don't know how to handle this type of list model: " + lm.getClass().getName());
	}
}
/**
 * connEtoC1:  (AddSessionOkButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionFileConverterView.addSessionOkButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.addSessionOkButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (StartConversionButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionFileConverterView.startConversionButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.startConversionButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (SourceBrowseButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionFileConverterView.sourceBrowseButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.sourceBrowseButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (TargetBrowseButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionFileConverterView.targetBrowseButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.targetBrowseButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (RemoveConversionButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionFileConverterView.removeConversionButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.removeConversionButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (DoneConversionButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionFileConverterView.doneConversionButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.doneConversionButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (AddConversionButton.action.actionPerformed(java.awt.event.ActionEvent) --> AddSessionFileDialog.show()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddSessionFileDialog().show();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM2:  (AddSessionOkButton.action.actionPerformed(java.awt.event.ActionEvent) --> AddSessionFileDialog.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddSessionFileDialog().setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM3:  (AddSessionCancelButton.action.actionPerformed(java.awt.event.ActionEvent) --> AddSessionFileDialog.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddSessionFileDialog().setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * When the done button is pressed, we should clear the list of sessions
 * to convert and hide the session file converter window.
 */
public void doneConversionButton_ActionPerformed() {

	JList conversionList = getSessionFileConversionList();
	conversionList.setListData(new java.util.Vector(0));

	setVisible(false);
}
/**
 * Return the AddConversionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddConversionButton() {
	if (ivjAddConversionButton == null) {
		try {
			ivjAddConversionButton = new javax.swing.JButton();
			ivjAddConversionButton.setName("AddConversionButton");
			ivjAddConversionButton.setText("+");
			ivjAddConversionButton.setBackground(new java.awt.Color(204,204,255));
			ivjAddConversionButton.setMaximumSize(new java.awt.Dimension(63, 25));
			ivjAddConversionButton.setPreferredSize(new java.awt.Dimension(63, 25));
			ivjAddConversionButton.setMinimumSize(new java.awt.Dimension(63, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddConversionButton;
}
/**
 * Return the AddSessionButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getAddSessionButtonPanel() {
	if (ivjAddSessionButtonPanel == null) {
		try {
			ivjAddSessionButtonPanel = new javax.swing.JPanel();
			ivjAddSessionButtonPanel.setName("AddSessionButtonPanel");
			ivjAddSessionButtonPanel.setLayout(new java.awt.GridBagLayout());
			ivjAddSessionButtonPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsAddSessionOkButton = new java.awt.GridBagConstraints();
			constraintsAddSessionOkButton.gridx = 0; constraintsAddSessionOkButton.gridy = 0;
			constraintsAddSessionOkButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddSessionButtonPanel().add(getAddSessionOkButton(), constraintsAddSessionOkButton);

			java.awt.GridBagConstraints constraintsAddSessionCancelButton = new java.awt.GridBagConstraints();
			constraintsAddSessionCancelButton.gridx = 1; constraintsAddSessionCancelButton.gridy = 0;
			constraintsAddSessionCancelButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddSessionButtonPanel().add(getAddSessionCancelButton(), constraintsAddSessionCancelButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddSessionButtonPanel;
}
/**
 * Return the AddSessionCancelButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddSessionCancelButton() {
	if (ivjAddSessionCancelButton == null) {
		try {
			ivjAddSessionCancelButton = new javax.swing.JButton();
			ivjAddSessionCancelButton.setName("AddSessionCancelButton");
			ivjAddSessionCancelButton.setText("Cancel");
			ivjAddSessionCancelButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddSessionCancelButton;
}
/**
 * Return the AddSessionFileDialog property value.
 * @return javax.swing.JDialog
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JDialog getAddSessionFileDialog() {
	if (ivjAddSessionFileDialog == null) {
		try {
			ivjAddSessionFileDialog = new javax.swing.JDialog();
			ivjAddSessionFileDialog.setName("AddSessionFileDialog");
			ivjAddSessionFileDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			ivjAddSessionFileDialog.setBackground(new java.awt.Color(204,204,255));
			ivjAddSessionFileDialog.setBounds(561, 17, 530, 140);
			ivjAddSessionFileDialog.setTitle("Add Session Conversion ...");
			getAddSessionFileDialog().setContentPane(getJDialogContentPane());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddSessionFileDialog;
}
/**
 * Return the AddSessionOkButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddSessionOkButton() {
	if (ivjAddSessionOkButton == null) {
		try {
			ivjAddSessionOkButton = new javax.swing.JButton();
			ivjAddSessionOkButton.setName("AddSessionOkButton");
			ivjAddSessionOkButton.setText("OK");
			ivjAddSessionOkButton.setBackground(new java.awt.Color(204,204,255));
			ivjAddSessionOkButton.setMaximumSize(new java.awt.Dimension(73, 25));
			ivjAddSessionOkButton.setPreferredSize(new java.awt.Dimension(73, 25));
			ivjAddSessionOkButton.setMinimumSize(new java.awt.Dimension(73, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddSessionOkButton;
}
/**
 * Return the ConversionListLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getConversionListLabel() {
	if (ivjConversionListLabel == null) {
		try {
			ivjConversionListLabel = new javax.swing.JLabel();
			ivjConversionListLabel.setName("ConversionListLabel");
			ivjConversionListLabel.setText("Conversion List");
			ivjConversionListLabel.setBackground(new java.awt.Color(204,204,255));
			ivjConversionListLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjConversionListLabel;
}
/**
 * Return the CancelConversionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getDoneConversionButton() {
	if (ivjDoneConversionButton == null) {
		try {
			ivjDoneConversionButton = new javax.swing.JButton();
			ivjDoneConversionButton.setName("DoneConversionButton");
			ivjDoneConversionButton.setText("Done");
			ivjDoneConversionButton.setBackground(new java.awt.Color(204,204,255));
			ivjDoneConversionButton.setMaximumSize(new java.awt.Dimension(63, 25));
			ivjDoneConversionButton.setPreferredSize(new java.awt.Dimension(63, 25));
			ivjDoneConversionButton.setMinimumSize(new java.awt.Dimension(63, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDoneConversionButton;
}
/**
 * Get the shared file chooser for this view.
 *
 * @return javax.swing.JFileChooser
 */
private javax.swing.JFileChooser getFileChooser() {
	
	if(fileChooser == null) {
		fileChooser = new JFileChooser();
		
		// set the start directory
		File f = new File(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(f);
		
		// set the file filter
		fileChooser.setFileFilter(new SessionFileFilter());
		
		// set one selection at a time
		fileChooser.setMultiSelectionEnabled(false);
		
		// set only select files
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
	}
	
	return(fileChooser);
}
/**
 * Return the JDialogContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJDialogContentPane() {
	if (ivjJDialogContentPane == null) {
		try {
			ivjJDialogContentPane = new javax.swing.JPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setLayout(new java.awt.GridBagLayout());
			ivjJDialogContentPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSourceSessionFileLabel = new java.awt.GridBagConstraints();
			constraintsSourceSessionFileLabel.gridx = 0; constraintsSourceSessionFileLabel.gridy = 0;
			constraintsSourceSessionFileLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getSourceSessionFileLabel(), constraintsSourceSessionFileLabel);

			java.awt.GridBagConstraints constraintsTargetSessionLabel = new java.awt.GridBagConstraints();
			constraintsTargetSessionLabel.gridx = 0; constraintsTargetSessionLabel.gridy = 1;
			constraintsTargetSessionLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getTargetSessionLabel(), constraintsTargetSessionLabel);

			java.awt.GridBagConstraints constraintsSourceTextField = new java.awt.GridBagConstraints();
			constraintsSourceTextField.gridx = 1; constraintsSourceTextField.gridy = 0;
			constraintsSourceTextField.gridwidth = 2;
			constraintsSourceTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSourceTextField.weightx = 1.0;
			constraintsSourceTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getSourceTextField(), constraintsSourceTextField);

			java.awt.GridBagConstraints constraintsTargetTextField = new java.awt.GridBagConstraints();
			constraintsTargetTextField.gridx = 1; constraintsTargetTextField.gridy = 1;
			constraintsTargetTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsTargetTextField.weightx = 1.0;
			constraintsTargetTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getTargetTextField(), constraintsTargetTextField);

			java.awt.GridBagConstraints constraintsSourceBrowseButton = new java.awt.GridBagConstraints();
			constraintsSourceBrowseButton.gridx = 3; constraintsSourceBrowseButton.gridy = 0;
			constraintsSourceBrowseButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getSourceBrowseButton(), constraintsSourceBrowseButton);

			java.awt.GridBagConstraints constraintsTargetBrowseButton = new java.awt.GridBagConstraints();
			constraintsTargetBrowseButton.gridx = 3; constraintsTargetBrowseButton.gridy = 1;
			constraintsTargetBrowseButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getTargetBrowseButton(), constraintsTargetBrowseButton);

			java.awt.GridBagConstraints constraintsAddSessionButtonPanel = new java.awt.GridBagConstraints();
			constraintsAddSessionButtonPanel.gridx = 0; constraintsAddSessionButtonPanel.gridy = 2;
			constraintsAddSessionButtonPanel.gridwidth = 4;
			constraintsAddSessionButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsAddSessionButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getAddSessionButtonPanel(), constraintsAddSessionButtonPanel);

			java.awt.GridBagConstraints constraintsVersionNumberComboBox = new java.awt.GridBagConstraints();
			constraintsVersionNumberComboBox.gridx = 2; constraintsVersionNumberComboBox.gridy = 1;
			constraintsVersionNumberComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsVersionNumberComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getVersionNumberComboBox(), constraintsVersionNumberComboBox);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane;
}
/**
 * Return the JFrameContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJFrameContentPane() {
	if (ivjJFrameContentPane == null) {
		try {
			ivjJFrameContentPane = new javax.swing.JPanel();
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.setLayout(new java.awt.GridBagLayout());
			ivjJFrameContentPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSessionFileConversionListScrollPane = new java.awt.GridBagConstraints();
			constraintsSessionFileConversionListScrollPane.gridx = 0; constraintsSessionFileConversionListScrollPane.gridy = 1;
constraintsSessionFileConversionListScrollPane.gridheight = 5;
			constraintsSessionFileConversionListScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSessionFileConversionListScrollPane.weightx = 1.0;
			constraintsSessionFileConversionListScrollPane.weighty = 1.0;
			constraintsSessionFileConversionListScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getSessionFileConversionListScrollPane(), constraintsSessionFileConversionListScrollPane);

			java.awt.GridBagConstraints constraintsAddConversionButton = new java.awt.GridBagConstraints();
			constraintsAddConversionButton.gridx = 1; constraintsAddConversionButton.gridy = 1;
			constraintsAddConversionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getAddConversionButton(), constraintsAddConversionButton);

			java.awt.GridBagConstraints constraintsRemoveConversionButton = new java.awt.GridBagConstraints();
			constraintsRemoveConversionButton.gridx = 1; constraintsRemoveConversionButton.gridy = 2;
			constraintsRemoveConversionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getRemoveConversionButton(), constraintsRemoveConversionButton);

			java.awt.GridBagConstraints constraintsStartConversionButton = new java.awt.GridBagConstraints();
			constraintsStartConversionButton.gridx = 1; constraintsStartConversionButton.gridy = 3;
			constraintsStartConversionButton.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsStartConversionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getStartConversionButton(), constraintsStartConversionButton);

			java.awt.GridBagConstraints constraintsConversionListLabel = new java.awt.GridBagConstraints();
			constraintsConversionListLabel.gridx = 0; constraintsConversionListLabel.gridy = 0;
			constraintsConversionListLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsConversionListLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getConversionListLabel(), constraintsConversionListLabel);

			java.awt.GridBagConstraints constraintsDoneConversionButton = new java.awt.GridBagConstraints();
			constraintsDoneConversionButton.gridx = 1; constraintsDoneConversionButton.gridy = 4;
			constraintsDoneConversionButton.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsDoneConversionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getDoneConversionButton(), constraintsDoneConversionButton);

			java.awt.GridBagConstraints constraintsSessionConversionStatusLabel = new java.awt.GridBagConstraints();
			constraintsSessionConversionStatusLabel.gridx = 1; constraintsSessionConversionStatusLabel.gridy = 5;
			constraintsSessionConversionStatusLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getSessionConversionStatusLabel(), constraintsSessionConversionStatusLabel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJFrameContentPane;
}
/**
 * Return the RemoveConversionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveConversionButton() {
	if (ivjRemoveConversionButton == null) {
		try {
			ivjRemoveConversionButton = new javax.swing.JButton();
			ivjRemoveConversionButton.setName("RemoveConversionButton");
			ivjRemoveConversionButton.setText("-");
			ivjRemoveConversionButton.setBackground(new java.awt.Color(204,204,255));
			ivjRemoveConversionButton.setMaximumSize(new java.awt.Dimension(63, 25));
			ivjRemoveConversionButton.setPreferredSize(new java.awt.Dimension(63, 25));
			ivjRemoveConversionButton.setMinimumSize(new java.awt.Dimension(63, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveConversionButton;
}
/**
 * Return the SessionConversionStatusLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSessionConversionStatusLabel() {
	if (ivjSessionConversionStatusLabel == null) {
		try {
			ivjSessionConversionStatusLabel = new javax.swing.JLabel();
			ivjSessionConversionStatusLabel.setName("SessionConversionStatusLabel");
			ivjSessionConversionStatusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/sessions/ui/gui/images/simpleClock.gif")));
			ivjSessionConversionStatusLabel.setText("");
			ivjSessionConversionStatusLabel.setEnabled(false);
			ivjSessionConversionStatusLabel.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/sessions/ui/gui/images/flag.gif")));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionConversionStatusLabel;
}
/**
 * Return the SessionFileConversionList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getSessionFileConversionList() {
	if (ivjSessionFileConversionList == null) {
		try {
			ivjSessionFileConversionList = new javax.swing.JList();
			ivjSessionFileConversionList.setName("SessionFileConversionList");
			ivjSessionFileConversionList.setModel(new DefaultListModel());
			ivjSessionFileConversionList.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
			ivjSessionFileConversionList.setCellRenderer(new SessionConversionListCellRenderer());
			ivjSessionFileConversionList.setBackground(java.awt.Color.lightGray);
			ivjSessionFileConversionList.setBounds(0, 0, 419, 537);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionFileConversionList;
}
/**
 * Return the SessionFileConversionListScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getSessionFileConversionListScrollPane() {
	if (ivjSessionFileConversionListScrollPane == null) {
		try {
			ivjSessionFileConversionListScrollPane = new javax.swing.JScrollPane();
			ivjSessionFileConversionListScrollPane.setName("SessionFileConversionListScrollPane");
			getSessionFileConversionListScrollPane().setViewportView(getSessionFileConversionList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionFileConversionListScrollPane;
}
/**
 * Return the SourceBrowseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSourceBrowseButton() {
	if (ivjSourceBrowseButton == null) {
		try {
			ivjSourceBrowseButton = new javax.swing.JButton();
			ivjSourceBrowseButton.setName("SourceBrowseButton");
			ivjSourceBrowseButton.setText("Browse");
			ivjSourceBrowseButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSourceBrowseButton;
}
/**
 * Return the SourceSessionFileLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSourceSessionFileLabel() {
	if (ivjSourceSessionFileLabel == null) {
		try {
			ivjSourceSessionFileLabel = new javax.swing.JLabel();
			ivjSourceSessionFileLabel.setName("SourceSessionFileLabel");
			ivjSourceSessionFileLabel.setText("Source");
			ivjSourceSessionFileLabel.setBackground(new java.awt.Color(204,204,255));
			ivjSourceSessionFileLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSourceSessionFileLabel;
}
/**
 * Return the SourceTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getSourceTextField() {
	if (ivjSourceTextField == null) {
		try {
			ivjSourceTextField = new javax.swing.JTextField();
			ivjSourceTextField.setName("SourceTextField");
			ivjSourceTextField.setBackground(java.awt.Color.white);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSourceTextField;
}
/**
 * Return the StartConversionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getStartConversionButton() {
	if (ivjStartConversionButton == null) {
		try {
			ivjStartConversionButton = new javax.swing.JButton();
			ivjStartConversionButton.setName("StartConversionButton");
			ivjStartConversionButton.setText("Go");
			ivjStartConversionButton.setBackground(new java.awt.Color(204,204,255));
			ivjStartConversionButton.setMaximumSize(new java.awt.Dimension(63, 25));
			ivjStartConversionButton.setPreferredSize(new java.awt.Dimension(63, 25));
			ivjStartConversionButton.setMinimumSize(new java.awt.Dimension(63, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStartConversionButton;
}
/**
 * Return the TargetBrowseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getTargetBrowseButton() {
	if (ivjTargetBrowseButton == null) {
		try {
			ivjTargetBrowseButton = new javax.swing.JButton();
			ivjTargetBrowseButton.setName("TargetBrowseButton");
			ivjTargetBrowseButton.setText("Browse");
			ivjTargetBrowseButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTargetBrowseButton;
}
/**
 * Return the TargetSessionLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getTargetSessionLabel() {
	if (ivjTargetSessionLabel == null) {
		try {
			ivjTargetSessionLabel = new javax.swing.JLabel();
			ivjTargetSessionLabel.setName("TargetSessionLabel");
			ivjTargetSessionLabel.setText("Target");
			ivjTargetSessionLabel.setBackground(new java.awt.Color(204,204,255));
			ivjTargetSessionLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTargetSessionLabel;
}
/**
 * Return the TargetTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getTargetTextField() {
	if (ivjTargetTextField == null) {
		try {
			ivjTargetTextField = new javax.swing.JTextField();
			ivjTargetTextField.setName("TargetTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTargetTextField;
}
/**
 * Return the VersionNumberComboBox property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JComboBox getVersionNumberComboBox() {
	if (ivjVersionNumberComboBox == null) {
		try {
			ivjVersionNumberComboBox = new javax.swing.JComboBox();
			ivjVersionNumberComboBox.setName("VersionNumberComboBox");
			ivjVersionNumberComboBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVersionNumberComboBox;
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
	getAddConversionButton().addActionListener(ivjEventHandler);
	getAddSessionOkButton().addActionListener(ivjEventHandler);
	getAddSessionCancelButton().addActionListener(ivjEventHandler);
	getStartConversionButton().addActionListener(ivjEventHandler);
	getSourceBrowseButton().addActionListener(ivjEventHandler);
	getTargetBrowseButton().addActionListener(ivjEventHandler);
	getRemoveConversionButton().addActionListener(ivjEventHandler);
	getDoneConversionButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SessionFileConverterView");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(427, 231);
		setTitle("Session File Converter");
		setContentPane(getJFrameContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	initVersionComboBox();
	// user code end
}
/**
 * Initialize the versions that are available in the combo box.
 *
 */
private void initVersionComboBox() {

	JComboBox versionComboBox = getVersionNumberComboBox();

	versionComboBox.addItem(new Integer(1));
	versionComboBox.addItem(new Integer(2));
	
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {

	SessionFileConverterView sfc = new SessionFileConverterView();
	sfc.setVisible(true);
    sfc.addWindowListener(new java.awt.event.WindowAdapter() {
    	public void windowClosing(java.awt.event.WindowEvent e) {
    		System.exit(0);
        };
    });
	
}
/**
 * When the user presses the remove button, we should prompt the user to
 * make sure they wish to delete all selected items and if they
 * respond with an affirmative, remove all selected items.
 */
public void removeConversionButton_ActionPerformed() {

	JList conversionList = getSessionFileConversionList();
	int[] selectedIndices = conversionList.getSelectedIndices();
	if((selectedIndices != null) && (selectedIndices.length > 0)) {
		ListModel lm = conversionList.getModel();
		if(lm instanceof DefaultListModel) {
			DefaultListModel dlm = (DefaultListModel)lm;
			for(int i = 0; i < selectedIndices.length; i++) {
				System.out.println("removing index " + selectedIndices[i] + " from the list.");
				dlm.removeElementAt(selectedIndices[i]);
			}	
		}
	}
}
/**
 * When the user presses the browse button, a JFileChooser window
 * should be provided to select a .session file.  Once the user selects
 * a session file, we should place the full path into the source
 * text field.
 */
public void sourceBrowseButton_ActionPerformed() {

	JFileChooser chooser = getFileChooser();

	int response = chooser.showOpenDialog(this);
	if(response == JFileChooser.APPROVE_OPTION) {
		File sourceFile = chooser.getSelectedFile();
		if(sourceFile != null) {
			JTextField sourceFileTextField = getSourceTextField();
			sourceFileTextField.setText(sourceFile.toString());
		}
	}
}
/**
 * When the go button is pressed, we should start converting all the
 * files listed in the conversion list.  Once we are finished, we can
 * close the window.
 */
public void startConversionButton_ActionPerformed() {

	Integer workingStatus = new Integer(WORKING_STATUS);
	Integer completeStatus = new Integer(COMPLETE_STATUS);
	Integer errorStatus = new Integer(ERROR_STATUS);

	// set the cursor as the waiting icon
    java.awt.Cursor previousCursor = getCursor();
    setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

    // start up the animated gif
    JLabel statusLabel = getSessionConversionStatusLabel();
    statusLabel.setEnabled(true);
	
	JList conversionList = getSessionFileConversionList();
	ListModel conversionListModel = conversionList.getModel();
	if(conversionListModel != null) {
	    if(conversionListModel instanceof DefaultListModel) {
		DefaultListModel dlm = (DefaultListModel)conversionListModel;
		int conversionCount = conversionListModel.getSize();
		for(int i = 0; i < conversionCount; i++) {
		    Object o = conversionListModel.getElementAt(i);
		    if(o instanceof java.util.List) {
			java.util.List l = (java.util.List)o;
			if((l != null) && (l.size() == 4)) {
			    File sourceFile = (File)l.get(0);
			    File targetFile = (File)l.get(1);
			    Integer version = (Integer)l.get(2);
			    l.set(3, workingStatus);
			    conversionList.repaint();
			    try {
				SessionFileConverter.convert(sourceFile, targetFile, version.intValue());
				l.set(3, completeStatus);
			    }
			    catch(Exception e) {
				System.err.println("Error while converting " + sourceFile.toString() +
						   " into " + targetFile.toString() + ": " + e.toString());
				l.set(3, errorStatus);
			    }
			    // cause the JList to update
			    dlm.set(i, l); // this doesn't seem to work! -tcw
			}
			else {
			    System.out.println("The current row (" + i + ") in the list is null or doesn't have 4 elements.");
			}
		    }
		    else {
			System.out.println("The current row (" + i + ") in the list is not a java.util.List.");
		    }
		}
	    }
	    else {
		System.out.println("The list model is not a DefaultListModel.");
	    }
	}
	else {
		System.out.println("The model for the list is null.");
	}
	
    statusLabel.setEnabled(false);
    setCursor(previousCursor);
}
/**
 * When the user presses the browse button, a JFileChooser window
 * should be provided to select a .session file.  Once the user selects
 * a session file, we should place the full path into the target
 * text field.
 */
public void targetBrowseButton_ActionPerformed() {
	
	JFileChooser chooser = getFileChooser();

	int response = chooser.showOpenDialog(this);
	if(response == JFileChooser.APPROVE_OPTION) {
		File sourceFile = chooser.getSelectedFile();
		if(sourceFile != null) {
			JTextField targetFileTextField = getTargetTextField();
			targetFileTextField.setText(sourceFile.toString());
		}
	}
	
}
}
