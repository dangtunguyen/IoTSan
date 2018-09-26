package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import org.apache.log4j.*;
/**
 * Insert the type's description here.
 * Creation date: (5/14/2002 3:50:24 PM)
 * @author: 
 */
public final class PropertySummaryWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private javax.swing.JLabel ivjPatternNameLabel = null;
	private javax.swing.JLabel ivjPatternValueLabel = null;
	private javax.swing.JLabel ivjPropertyNameLabel = null;
	private javax.swing.JLabel ivjQuantificationNameLabel = null;
	private javax.swing.JLabel ivjQuantificationValueLabel = null;
	private final static java.lang.String message = "You have now finished defining the property to verfiy.  Please look over the expanded property below to make sure it is exactly what you want.  If it is correct, press the next button and continue with the wizard.  If it is not correct, please go back through the wizard process to re-define the property.";
	private javax.swing.JTextArea ivjPropertyTextArea = null;
	private final static Category log = Category.getInstance(PropertySummaryWizardPanel.class);
	private javax.swing.JScrollPane ivjPropertyScrollPane = null;
/**
 * PropertySummaryWizardPanel constructor comment.
 */
public PropertySummaryWizardPanel() {
	super();
	initialize();
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
 * Return the PatternNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPatternNameLabel() {
	if (ivjPatternNameLabel == null) {
		try {
			ivjPatternNameLabel = new javax.swing.JLabel();
			ivjPatternNameLabel.setName("PatternNameLabel");
			ivjPatternNameLabel.setText("Pattern");
			ivjPatternNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternNameLabel;
}
/**
 * Return the PatternValueLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPatternValueLabel() {
	if (ivjPatternValueLabel == null) {
		try {
			ivjPatternValueLabel = new javax.swing.JLabel();
			ivjPatternValueLabel.setName("PatternValueLabel");
			ivjPatternValueLabel.setFont(new java.awt.Font("Arial", 1, 12));
			ivjPatternValueLabel.setText("<Pattern>");
			ivjPatternValueLabel.setForeground(java.awt.Color.red);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternValueLabel;
}
/**
 * Return the PropertyNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPropertyNameLabel() {
	if (ivjPropertyNameLabel == null) {
		try {
			ivjPropertyNameLabel = new javax.swing.JLabel();
			ivjPropertyNameLabel.setName("PropertyNameLabel");
			ivjPropertyNameLabel.setText("Property");
			ivjPropertyNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropertyNameLabel;
}
/**
 * Return the PropertyScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getPropertyScrollPane() {
	if (ivjPropertyScrollPane == null) {
		try {
			ivjPropertyScrollPane = new javax.swing.JScrollPane();
			ivjPropertyScrollPane.setName("PropertyScrollPane");
			getPropertyScrollPane().setViewportView(getPropertyTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropertyScrollPane;
}
/**
 * Return the PropertyTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getPropertyTextArea() {
	if (ivjPropertyTextArea == null) {
		try {
			ivjPropertyTextArea = new javax.swing.JTextArea();
			ivjPropertyTextArea.setName("PropertyTextArea");
			ivjPropertyTextArea.setLineWrap(true);
			ivjPropertyTextArea.setWrapStyleWord(true);
			ivjPropertyTextArea.setText("<Property>");
			ivjPropertyTextArea.setBackground(new java.awt.Color(204,204,255));
			ivjPropertyTextArea.setForeground(java.awt.Color.red);
			ivjPropertyTextArea.setFont(new java.awt.Font("Arial", 1, 12));
			ivjPropertyTextArea.setBounds(0, 0, 441, 46);
			ivjPropertyTextArea.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropertyTextArea;
}
/**
 * Return the QuantificationNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getQuantificationNameLabel() {
	if (ivjQuantificationNameLabel == null) {
		try {
			ivjQuantificationNameLabel = new javax.swing.JLabel();
			ivjQuantificationNameLabel.setName("QuantificationNameLabel");
			ivjQuantificationNameLabel.setText("Quantification");
			ivjQuantificationNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationNameLabel;
}
/**
 * Return the QuantificationValueLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getQuantificationValueLabel() {
	if (ivjQuantificationValueLabel == null) {
		try {
			ivjQuantificationValueLabel = new javax.swing.JLabel();
			ivjQuantificationValueLabel.setName("QuantificationValueLabel");
			ivjQuantificationValueLabel.setFont(new java.awt.Font("Arial", 1, 12));
			ivjQuantificationValueLabel.setText("<Quantification>");
			ivjQuantificationValueLabel.setForeground(java.awt.Color.red);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationValueLabel;
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
		setName("PropertySummaryWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(536, 446);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsQuantificationNameLabel = new java.awt.GridBagConstraints();
		constraintsQuantificationNameLabel.gridx = 0; constraintsQuantificationNameLabel.gridy = 1;
		constraintsQuantificationNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getQuantificationNameLabel(), constraintsQuantificationNameLabel);

		java.awt.GridBagConstraints constraintsQuantificationValueLabel = new java.awt.GridBagConstraints();
		constraintsQuantificationValueLabel.gridx = 1; constraintsQuantificationValueLabel.gridy = 1;
		constraintsQuantificationValueLabel.gridwidth = 2;
		constraintsQuantificationValueLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getQuantificationValueLabel(), constraintsQuantificationValueLabel);

		java.awt.GridBagConstraints constraintsPatternNameLabel = new java.awt.GridBagConstraints();
		constraintsPatternNameLabel.gridx = 0; constraintsPatternNameLabel.gridy = 2;
		constraintsPatternNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternNameLabel(), constraintsPatternNameLabel);

		java.awt.GridBagConstraints constraintsPatternValueLabel = new java.awt.GridBagConstraints();
		constraintsPatternValueLabel.gridx = 1; constraintsPatternValueLabel.gridy = 2;
		constraintsPatternValueLabel.gridwidth = 2;
		constraintsPatternValueLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternValueLabel(), constraintsPatternValueLabel);

		java.awt.GridBagConstraints constraintsPropertyNameLabel = new java.awt.GridBagConstraints();
		constraintsPropertyNameLabel.gridx = 0; constraintsPropertyNameLabel.gridy = 3;
		constraintsPropertyNameLabel.anchor = java.awt.GridBagConstraints.NORTH;
		constraintsPropertyNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPropertyNameLabel(), constraintsPropertyNameLabel);

		java.awt.GridBagConstraints constraintsPropertyScrollPane = new java.awt.GridBagConstraints();
		constraintsPropertyScrollPane.gridx = 1; constraintsPropertyScrollPane.gridy = 3;
		constraintsPropertyScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsPropertyScrollPane.weightx = 1.0;
		constraintsPropertyScrollPane.weighty = 1.0;
		constraintsPropertyScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPropertyScrollPane(), constraintsPropertyScrollPane);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Property Summary");
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		PropertySummaryWizardPanel aPropertySummaryWizardPanel;
		aPropertySummaryWizardPanel = new PropertySummaryWizardPanel();
		frame.setContentPane(aPropertySummaryWizardPanel);
		frame.setSize(aPropertySummaryWizardPanel.getSize());
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
 * Creation date: (5/14/2002 3:59:46 PM)
 * @param pattern edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern
 */
public void setPattern(Pattern pattern) {
	if(pattern == null) {
		log.warn("pattern is null.  Cannot use a null pattern in the PropertySummaryWizardPanel.");
		return;
	}
	
	getPatternValueLabel().setText(pattern.getFormat());
}
/**
 * Set the expanded property that should be displayed to the user to make sure the property is
 * exactly what they were trying to define.
 *
 * @param String property The expanded property to display to the user.
 * @pre property != null
 */
public void setProperty(String property) {
	
	if(property == null) {
		log.warn("property is null.  Because of this, we will default to an empty String property value.");
		property = "";
	}
	
	getPropertyTextArea().setText(property);
	getPropertyTextArea().setCaretPosition(0);
}
/**
 * Insert the method's description here.
 * Creation date: (5/14/2002 4:01:15 PM)
 * @param quantification java.lang.String
 */
public void setQuantification(String quantification) {

	if(quantification == null) {
		log.warn("quantification is null.");
		return;
	}

	getQuantificationValueLabel().setText(quantification);
}
}
