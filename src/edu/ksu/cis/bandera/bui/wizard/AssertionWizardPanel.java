package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import javax.swing.JCheckBox;
import java.awt.event.*;
import java.util.*;
import org.apache.log4j.Category;

import edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion;
import edu.ksu.cis.bandera.specification.assertion.datastructure.LocationAssertion;
import edu.ksu.cis.bandera.specification.assertion.datastructure.PostAssertion;
import edu.ksu.cis.bandera.specification.assertion.datastructure.PreAssertion;

/**
 *
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:19 $
 */
public final class AssertionWizardPanel extends BanderaAbstractWizardPanel {
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private final static java.lang.String message = "A list of all the BSL assertions that Bandera has found in your application are presented below.  Select those assertions that you wish to validate in this session.";
	private Map assertions;
	private javax.swing.JPanel ivjAssertionCheckBoxPanel = null;
	private javax.swing.BoxLayout ivjAssertionCheckBoxPanelBoxLayout = null;
	private javax.swing.JScrollPane ivjAssertionScrollPane = null;
	private static final Category log = Category.getInstance(AssertionWizardPanel.class);
	private javax.swing.JLabel ivjWizardLogoLabel = null;
/**
 * AssertionWizardPanel constructor comment.
 */
public AssertionWizardPanel() {
	super();
	initialize();
}
/**
 * Add an assertion to the current list of assertions that can be selected.
 *
 * @param Assertion assertion
 */
public void addAssertion(Assertion assertion) {

    addAssertion(assertion, false);

}
/**
 * Add an assertion to the current list of assertions that can be selected.
 *
 * @param Assertion assertion
 * @param active boolean True if this assertion is currently active, false otherwise.
 */
public void addAssertion(Assertion assertion, boolean active) {

    if(assertion == null) {
	return;
    }

    String assertionName = assertion.getName().toString().trim();
    String assertionType = "unknown assertion type";
    if(assertion instanceof LocationAssertion) {
	LocationAssertion la = (LocationAssertion)assertion;
	assertionType = "Location assertion for label " + la.getLabel();
    }
    if(assertion instanceof PostAssertion) {
	assertionType = "Post-condition assertion";
    }
    if(assertion instanceof PreAssertion) {
	assertionType = "Pre-condition assertion";
    }
    String assertionConstraint = assertion.getConstraint().toString().trim();
    String assertionText = assertionName + "(" + assertionType + ", " + assertionConstraint + ")";
    JCheckBox checkBox = new JCheckBox(assertionText);
    checkBox.setSelected(active);

    getAssertionCheckBoxPanel().add(checkBox);

    assertions.put(assertionName, checkBox);
	
}
/**
 * Get all assertions that are set to being active at this time.
 *
 * @return java.util.Set The set of all assertions that are active.  These will all be
 *         String objects that represent the name of the assertion to activate.
 */
public Set getActiveAssertions() {
	
	if(assertions == null) {
		return(null);
	}

	// walk through all the assertions that are selected and add them to a set of
	//  active assertions to be returned.
	Set activeAssertions = new HashSet();
	Iterator iterator = assertions.keySet().iterator();
	while(iterator.hasNext()) {
		String key = (String)iterator.next();
		JCheckBox checkBox = (JCheckBox)assertions.get(key);
		if(checkBox.isSelected()) {
			activeAssertions.add(key);
		}
	}

	return(activeAssertions);
}
/**
 * Return the RadioButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getAssertionCheckBoxPanel() {
	if (ivjAssertionCheckBoxPanel == null) {
		try {
			ivjAssertionCheckBoxPanel = new javax.swing.JPanel();
			ivjAssertionCheckBoxPanel.setName("AssertionCheckBoxPanel");
			ivjAssertionCheckBoxPanel.setLayout(getAssertionCheckBoxPanelBoxLayout());
			ivjAssertionCheckBoxPanel.setBounds(0, 0, 181, 192);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionCheckBoxPanel;
}
/**
 * Return the AssertionCheckBoxPanelBoxLayout property value.
 * @return javax.swing.BoxLayout
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.BoxLayout getAssertionCheckBoxPanelBoxLayout() {
	javax.swing.BoxLayout ivjAssertionCheckBoxPanelBoxLayout = null;
	try {
		/* Create part */
		ivjAssertionCheckBoxPanelBoxLayout = new javax.swing.BoxLayout(getAssertionCheckBoxPanel(), javax.swing.BoxLayout.Y_AXIS);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjAssertionCheckBoxPanelBoxLayout;
}
/**
 * Return the RadioButtonScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getAssertionScrollPane() {
	if (ivjAssertionScrollPane == null) {
		try {
			ivjAssertionScrollPane = new javax.swing.JScrollPane();
			ivjAssertionScrollPane.setName("AssertionScrollPane");
			getAssertionScrollPane().setViewportView(getAssertionCheckBoxPanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionScrollPane;
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
 * Return the WizardLogoLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getWizardLogoLabel() {
	if (ivjWizardLogoLabel == null) {
		try {
			ivjWizardLogoLabel = new javax.swing.JLabel();
			ivjWizardLogoLabel.setName("WizardLogoLabel");
			ivjWizardLogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/wizard/images/wizard3.gif")));
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
		setName("AssertionWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(512, 411);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 0.1;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsAssertionScrollPane = new java.awt.GridBagConstraints();
		constraintsAssertionScrollPane.gridx = 0; constraintsAssertionScrollPane.gridy = 1;
		constraintsAssertionScrollPane.gridwidth = 2;
		constraintsAssertionScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsAssertionScrollPane.weightx = 1.0;
		constraintsAssertionScrollPane.weighty = 1.0;
		constraintsAssertionScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getAssertionScrollPane(), constraintsAssertionScrollPane);

		java.awt.GridBagConstraints constraintsWizardLogoLabel = new java.awt.GridBagConstraints();
		constraintsWizardLogoLabel.gridx = 1; constraintsWizardLogoLabel.gridy = 0;
		constraintsWizardLogoLabel.weightx = 1.0;
		constraintsWizardLogoLabel.weighty = 0.1;
		constraintsWizardLogoLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getWizardLogoLabel(), constraintsWizardLogoLabel);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	assertions = new HashMap();
	setTitle("Enable Assertions");
	// user code end
}


/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		AssertionWizardPanel aAssertionWizardPanel;
		aAssertionWizardPanel = new AssertionWizardPanel();
		frame.setContentPane(aAssertionWizardPanel);
		frame.setSize(aAssertionWizardPanel.getSize());
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
