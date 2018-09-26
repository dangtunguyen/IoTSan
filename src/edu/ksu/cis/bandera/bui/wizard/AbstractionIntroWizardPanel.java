package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import org.apache.log4j.*;
/**
 * The AbstractionIntroWizardPanel will eventually be an introduction into the
 * configuration of abstraction for an application.  However, right now it just
 * says that we cannot do this at this time.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public final class AbstractionIntroWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private final static java.lang.String message = "At this time, I am not able to help you configure abstraction for your application.  To configure abstraction for this application complete the steps I provide you, save the session, start Bandera and activate this session, and configure using the Abstraction Manager.";
	private final static Category log = Category.getInstance(AbstractionIntroWizardPanel.class);
	private javax.swing.JLabel ivjWizardLabelLogo = null;
/**
 * AbstractionIntroWizardPanel constructor comment.
 */
public AbstractionIntroWizardPanel() {
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
 * Return the WizardLabelLogo property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getWizardLabelLogo() {
	if (ivjWizardLabelLogo == null) {
		try {
			ivjWizardLabelLogo = new javax.swing.JLabel();
			ivjWizardLabelLogo.setName("WizardLabelLogo");
			ivjWizardLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/wizard/images/wizard1.gif")));
			ivjWizardLabelLogo.setText("");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWizardLabelLogo;
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
		setName("AbstractionIntroWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(495, 440);

		java.awt.GridBagConstraints constraintsWizardLabelLogo = new java.awt.GridBagConstraints();
		constraintsWizardLabelLogo.gridx = 0; constraintsWizardLabelLogo.gridy = 0;
		constraintsWizardLabelLogo.weightx = 0.1;
		constraintsWizardLabelLogo.weighty = 1.0;
		constraintsWizardLabelLogo.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLabelLogo(), constraintsWizardLabelLogo);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 1; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 0.9;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Configure Abstraction");
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		AbstractionIntroWizardPanel aAbstractionIntroWizardPanel;
		aAbstractionIntroWizardPanel = new AbstractionIntroWizardPanel();
		frame.setContentPane(aAbstractionIntroWizardPanel);
		frame.setSize(aAbstractionIntroWizardPanel.getSize());
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
}
