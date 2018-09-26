package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import javax.swing.ButtonGroup;
import org.apache.log4j.*;

/**
 * Insert the type's description here.
 * Creation date: (4/22/2002 4:11:19 PM)
 * @author: 
 */
public final class SlicingWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private javax.swing.JRadioButton ivjNoRadioButton = null;
	private javax.swing.JRadioButton ivjYesRadioButton = null;
	private final static java.lang.String message = "Slicing is a technique that uses program dependence information to cut out parts of the source that are guaranteed to be irrelevant for the property that you wish to check.\n\nSlicing is completely automatic and usually inexpensive to compute, and generally you should always apply slicing unless you are already sure that slicing will yield little or no reductions.\n\nDo you want Bandera to use slicing on your application?";
	private boolean slicingEnabled;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JLabel ivjWizardLogoLabel = null;
	private final static Category log = Category.getInstance(SlicingWizardPanel.class);

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SlicingWizardPanel.this.getYesRadioButton()) 
				connEtoC1(e);
			if (e.getSource() == SlicingWizardPanel.this.getNoRadioButton()) 
				connEtoC2(e);
		};
	};
/**
 * SlicingWizardPanel constructor comment.
 */
public SlicingWizardPanel() {
	super();
	initialize();
}
/**
 * connEtoC1:  (YesRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SlicingWizardPanel.yesRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.yesRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (NoRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SlicingWizardPanel.noRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.noRadioButton_ActionPerformed(arg1);
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
 * Return the NoRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getNoRadioButton() {
	if (ivjNoRadioButton == null) {
		try {
			ivjNoRadioButton = new javax.swing.JRadioButton();
			ivjNoRadioButton.setName("NoRadioButton");
			ivjNoRadioButton.setText("No");
			ivjNoRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNoRadioButton;
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
 * Return the YesRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getYesRadioButton() {
	if (ivjYesRadioButton == null) {
		try {
			ivjYesRadioButton = new javax.swing.JRadioButton();
			ivjYesRadioButton.setName("YesRadioButton");
			ivjYesRadioButton.setSelected(true);
			ivjYesRadioButton.setText("Yes");
			ivjYesRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjYesRadioButton;
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
	getYesRadioButton().addActionListener(ivjEventHandler);
	getNoRadioButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SlicingWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(455, 352);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 1; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsYesRadioButton = new java.awt.GridBagConstraints();
		constraintsYesRadioButton.gridx = 1; constraintsYesRadioButton.gridy = 1;
		constraintsYesRadioButton.anchor = java.awt.GridBagConstraints.EAST;
		constraintsYesRadioButton.weightx = 1.0;
		constraintsYesRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getYesRadioButton(), constraintsYesRadioButton);

		java.awt.GridBagConstraints constraintsNoRadioButton = new java.awt.GridBagConstraints();
		constraintsNoRadioButton.gridx = 2; constraintsNoRadioButton.gridy = 1;
		constraintsNoRadioButton.anchor = java.awt.GridBagConstraints.WEST;
		constraintsNoRadioButton.weightx = 1.0;
		constraintsNoRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getNoRadioButton(), constraintsNoRadioButton);

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 0; constraintsWizardLogoLabel.gridy = 0;
constraintsWizardLogoLabel.gridheight = 2;
		constraintsWizardLogoLabel.weightx = 0.1;
		constraintsWizardLogoLabel.weighty = 1.0;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	
	// setup the button group so that only one radio button can be selected at any time
	ButtonGroup yesNoButtonGroup = new ButtonGroup();
	yesNoButtonGroup.add(getYesRadioButton());
	yesNoButtonGroup.add(getNoRadioButton());

	// by default, slicing should be enabled.  this should match that the yes button is enabled!
	slicingEnabled = true;

	setTitle("Should we Slice the Application?");
	
	// user code end
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:59:26 PM)
 * @return boolean
 */
public boolean isSlicingEnabled() {
	return slicingEnabled;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		SlicingWizardPanel aSlicingWizardPanel;
		aSlicingWizardPanel = new SlicingWizardPanel();
		frame.setContentPane(aSlicingWizardPanel);
		frame.setSize(aSlicingWizardPanel.getSize());
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
 * When the No radio button is selected, we should disabled slicing (
 * by setting slicingEnabled to false).
 */
public void noRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	slicingEnabled = false;
}
/**
 * Set the slicing flag for this wizard panel.
 *
 * @param newSlicingEnabled boolean
 */
public void setSlicingEnabled(boolean newSlicingEnabled) {
	slicingEnabled = newSlicingEnabled;
	if(slicingEnabled) {
	    getYesRadioButton().setSelected(true);
	}
	else {
	    getNoRadioButton().setSelected(true);
	}
	System.out.println("slicingEnabled set: " + slicingEnabled);
}
/**
 * When the Yes radio button is selected, we should enabled slicing (
 * by setting slicingEnabled to true).
 */
public void yesRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	slicingEnabled = true;
}
}
