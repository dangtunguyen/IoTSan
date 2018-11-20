package edu.ksu.cis.bandera.checker;

import javax.swing.*;
import java.util.*;
/**
 * The DefaultOptionsView provides a very basic way for a user to enter checker command line options.
 * It simply provides a text field for entering them in and they will be given to the checker exactly how they
 * are entered in the text field.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:21 $
 */
public class DefaultOptionsView extends JDialog implements OptionsView {
	private JLabel ivjCommandLineOptionsLabel = null;
	private JTextField ivjCommandLineOptionsTextField = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JScrollPane ivjExplainScrollPane = null;
	private JTextArea ivjExplainTextArea = null;
	private JPanel ivjJDialogContentPane = null;
	private JButton ivjOkButton = null;
	private Options options;
	private Set completionListenerSet;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DefaultOptionsView.this.getOkButton()) 
				connEtoC1(e);
		};
	};
/**
 * DefaultOptionsView constructor comment.
 */
public DefaultOptionsView() {
	super();
	initialize();
}
 /**
     * Remove all listeners that have been registered.
     */
public void clearCompletionListeners() {
	if(completionListenerSet != null) {
	    completionListenerSet.clear();
	}
}
/**
 * connEtoC1:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> DefaultOptionsView.okButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.okButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Return the CommandLineOptionsLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getCommandLineOptionsLabel() {
	if (ivjCommandLineOptionsLabel == null) {
		try {
			ivjCommandLineOptionsLabel = new javax.swing.JLabel();
			ivjCommandLineOptionsLabel.setName("CommandLineOptionsLabel");
			ivjCommandLineOptionsLabel.setFont(new java.awt.Font("Arial", 1, 14));
			ivjCommandLineOptionsLabel.setText("Command Line Options");
			ivjCommandLineOptionsLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCommandLineOptionsLabel;
}
/**
 * Return the CommandLineOptionsTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getCommandLineOptionsTextField() {
	if (ivjCommandLineOptionsTextField == null) {
		try {
			ivjCommandLineOptionsTextField = new javax.swing.JTextField();
			ivjCommandLineOptionsTextField.setName("CommandLineOptionsTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCommandLineOptionsTextField;
}
/**
 * Return the ExplainScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getExplainScrollPane() {
	if (ivjExplainScrollPane == null) {
		try {
			ivjExplainScrollPane = new javax.swing.JScrollPane();
			ivjExplainScrollPane.setName("ExplainScrollPane");
			getExplainScrollPane().setViewportView(getExplainTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExplainScrollPane;
}
/**
 * Return the ExplainTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getExplainTextArea() {
	if (ivjExplainTextArea == null) {
		try {
			ivjExplainTextArea = new javax.swing.JTextArea();
			ivjExplainTextArea.setName("ExplainTextArea");
			ivjExplainTextArea.setLineWrap(true);
			ivjExplainTextArea.setWrapStyleWord(true);
			ivjExplainTextArea.setText("Type in the command line options that you want passed to the model checker.  They will be passed as-is.");
			ivjExplainTextArea.setBackground(new java.awt.Color(204,204,255));
			ivjExplainTextArea.setBounds(0, 0, 418, 32);
			ivjExplainTextArea.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExplainTextArea;
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

			java.awt.GridBagConstraints constraintsCommandLineOptionsLabel = new java.awt.GridBagConstraints();
			constraintsCommandLineOptionsLabel.gridx = 0; constraintsCommandLineOptionsLabel.gridy = 0;
			constraintsCommandLineOptionsLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getCommandLineOptionsLabel(), constraintsCommandLineOptionsLabel);

			java.awt.GridBagConstraints constraintsOkButton = new java.awt.GridBagConstraints();
			constraintsOkButton.gridx = 0; constraintsOkButton.gridy = 3;
			constraintsOkButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getOkButton(), constraintsOkButton);

			java.awt.GridBagConstraints constraintsCommandLineOptionsTextField = new java.awt.GridBagConstraints();
			constraintsCommandLineOptionsTextField.gridx = 0; constraintsCommandLineOptionsTextField.gridy = 2;
			constraintsCommandLineOptionsTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsCommandLineOptionsTextField.weightx = 1.0;
			constraintsCommandLineOptionsTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getCommandLineOptionsTextField(), constraintsCommandLineOptionsTextField);

			java.awt.GridBagConstraints constraintsExplainScrollPane = new java.awt.GridBagConstraints();
			constraintsExplainScrollPane.gridx = 0; constraintsExplainScrollPane.gridy = 1;
			constraintsExplainScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsExplainScrollPane.weightx = 1.0;
			constraintsExplainScrollPane.weighty = 0.1;
			constraintsExplainScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getExplainScrollPane(), constraintsExplainScrollPane);
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
 * Return the OkButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOkButton() {
	if (ivjOkButton == null) {
		try {
			ivjOkButton = new javax.swing.JButton();
			ivjOkButton.setName("OkButton");
			ivjOkButton.setText("OK");
			ivjOkButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOkButton;
}
 /**
     * Get the Options model that this view is working on.
     *
     * @return Options The Options model that this view is configuring.
     */
public Options getOptions() {
	return(options);
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
     * Initialize the view using the given options as the model.
     *
     * @param Options options The Options to use as the model for our view.
     */
public void init(Options options) {
	this.options = options;
	if(options != null) {
		try {
			getCommandLineOptionsTextField().setText(options.getCommandLineOptions());
		}
		catch(Exception e) {
			// report this! -tcw
		}
	}
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getOkButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("DefaultOptionsView");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(426, 181);
		setTitle("Checker Options");
		setContentPane(getJDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	try {
		DefaultOptionsView defaultOptionsView;
		defaultOptionsView = new DefaultOptionsView();
		DefaultOptions defaultOptions = new DefaultOptions();
		CompletionListener cl = new CompletionListener() {
			public void complete(Object o) {
				
				if((o != null) && (o instanceof DefaultOptionsView)) {
					DefaultOptionsView defaultOptionsView = (DefaultOptionsView)o;
					defaultOptionsView.setVisible(false);
					try {
						System.out.println("options = " + defaultOptionsView.getOptions().getCommandLineOptions());
					}
					catch(Exception e) {
					}
				}
				
				System.out.println("Complete.");
				System.exit(0);
			}
		};
		defaultOptionsView.registerCompletionListener(cl);
		defaultOptions.init("-something -rather silly");
		defaultOptionsView.init(defaultOptions);
		defaultOptionsView.setModal(true);
		defaultOptionsView.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		defaultOptionsView.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JDialog");
		exception.printStackTrace(System.out);
	}
}
/**
 * When the OK button is pressed, signal all the CompletionListeners that we have completed
 * the configuration (or rather the user finished entering command line options).
 */
public void okButton_ActionPerformed() {
	
	setVisible(false);
	
	// update the options using the text from the command line options text field
	options.init(getCommandLineOptionsTextField().getText());
	if(completionListenerSet != null) {
	    Iterator i = completionListenerSet.iterator();
	    while(i.hasNext()) {
		CompletionListener cl = (CompletionListener)i.next();
		cl.complete(this);
	    }
	}
}
 /**
     * Register a CompletionListener that is requesting notification
     * of the completion of the Options configuration.
     *
     * @param CompletionListener completionListener The object to signal when configuration
     *        is complete.
     */
public void registerCompletionListener(CompletionListener completionListener) {
	if(completionListener == null) {
	    return;
	}
	if(completionListenerSet == null) {
	    completionListenerSet = new HashSet();
	}
	completionListenerSet.add(completionListener);
}
 /**
     * Remove the given listener that has been registered.
     *
     * @param CompletionListener completionListener The object that should be removed from
     *        the set of listeners.
     */
public void removeCompletionListener(CompletionListener completionListener) {
	if(completionListenerSet != null) {
	    completionListenerSet.remove(completionListener);
	}
}
 /**
     * Set the Options model that this view should work on.
     *
     * @param Options options The Options model that this view will configure.
     */
public void setOptions(Options options) {
	init(options);
}
}
