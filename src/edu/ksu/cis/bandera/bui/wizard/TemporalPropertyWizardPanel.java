package edu.ksu.cis.bandera.bui.wizard;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;

import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;

import org.apache.log4j.*;

/**
 * The TemporalPropertyWizardPanel is provided so that the user can select a pattern
 * to use to generate the temporal property for the current application.  This provides
 * two drop down boxes: one for the pattern names and the other for the pattern scope.  It
 * also provides a text area that displays the description of the pattern that is currently
 * selected.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:21 $
 */
public final class TemporalPropertyWizardPanel extends BanderaAbstractWizardPanel {
    /**
     *
     */
    private final static java.lang.String message = "Bandera can check safety and liveness properties that describe temporal relationships between events/states specified as BSL observables in your application code.\n\nBandera allows you to express these temporal relationships using the Bandera Temporal Specification Pattern System which is accessed using the pull-down tabs below (see the Bandera User Manual for an explanation of the pattern system and how to add additional patterns to the Bandera Pattern Library).\n\nUsing the tabs below, please select the appropriate temporal pattern and execution trace scope for your temporal property.\n\nThe text area below the pull-down tabs provides a brief description of the selected pattern (if one exists).  This may help you in selecting the appropriate pattern and scope.";
	private javax.swing.JTextArea ivjMessageTextArea = null;
	private javax.swing.JLabel ivjPatternLabel = null;
	private javax.swing.JComboBox ivjPatternNameComboBox = null;
	private javax.swing.JComboBox ivjPatternScopeComboBox = null;
	private javax.swing.JLabel ivjPatternScopeLabel = null;
    /**
     *
     */
    private final static Category log = Category.getInstance(TemporalPropertyWizardPanel.class);
    /**
     *
     */
    private List patternList;
    /**
     *
     */
    private Pattern selectedPattern;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JTextArea ivjPatternDescriptionTextArea = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == TemporalPropertyWizardPanel.this.getPatternNameComboBox()) 
				connEtoC1(e);
			if (e.getSource() == TemporalPropertyWizardPanel.this.getPatternScopeComboBox()) 
				connEtoC2(e);
		};
	};
	private javax.swing.JScrollPane ivjPatternDescriptionScrollPane = null;
    /**
     * TemporalPropertyWizardPanel constructor comment.
     */
    public TemporalPropertyWizardPanel() {
	super();
	initialize();
    }
/**
 * connEtoC1:  (PatternNameComboBox.action.actionPerformed(java.awt.event.ActionEvent) --> TemporalPropertyWizardPanel.patternNameComboBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.patternNameComboBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (PatternScopeComboBox.action.actionPerformed(java.awt.event.ActionEvent) --> TemporalPropertyWizardPanel.patternScopeComboBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.patternScopeComboBox_ActionPerformed(arg1);
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
			ivjMessageTextArea.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
     *
     */
    private Pattern getPattern(String name, String scope) {

	if(patternList != null) {
	    for(int i = 0; i < patternList.size(); i++) {
		Pattern pattern = (Pattern)patternList.get(i);
		if((pattern != null) && (name.equals(pattern.getName())) && (scope.equals(pattern.getScope()))) {
		    return(pattern);
		}
	    }
	}
	return(null);
    }
/**
 * Return the PatternDescriptionScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getPatternDescriptionScrollPane() {
	if (ivjPatternDescriptionScrollPane == null) {
		try {
			ivjPatternDescriptionScrollPane = new javax.swing.JScrollPane();
			ivjPatternDescriptionScrollPane.setName("PatternDescriptionScrollPane");
			getPatternDescriptionScrollPane().setViewportView(getPatternDescriptionTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternDescriptionScrollPane;
}
/**
 * Return the PatternDescriptionTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getPatternDescriptionTextArea() {
	if (ivjPatternDescriptionTextArea == null) {
		try {
			ivjPatternDescriptionTextArea = new javax.swing.JTextArea();
			ivjPatternDescriptionTextArea.setName("PatternDescriptionTextArea");
			ivjPatternDescriptionTextArea.setLineWrap(true);
			ivjPatternDescriptionTextArea.setWrapStyleWord(true);
			ivjPatternDescriptionTextArea.setBackground(new java.awt.Color(204,204,255));
			ivjPatternDescriptionTextArea.setBounds(0, 0, 524, 73);
			ivjPatternDescriptionTextArea.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternDescriptionTextArea;
}
    /**
     * Return the PatternLabel property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getPatternLabel() {
	if (ivjPatternLabel == null) {
	    try {
		ivjPatternLabel = new javax.swing.JLabel();
		ivjPatternLabel.setName("PatternLabel");
		ivjPatternLabel.setText("Pattern");
		ivjPatternLabel.setForeground(java.awt.Color.black);
		// user code begin {1}
		// user code end
	    }
	    catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPatternLabel;
    }
    /**
     * Return the PatternNameComboBox property value.
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getPatternNameComboBox() {
	if (ivjPatternNameComboBox == null) {
		try {
			ivjPatternNameComboBox = new javax.swing.JComboBox();
			ivjPatternNameComboBox.setName("PatternNameComboBox");
			ivjPatternNameComboBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternNameComboBox;
}
    /**
     * Return the PatternScopeComboBox property value.
     * @return javax.swing.JComboBox
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JComboBox getPatternScopeComboBox() {
	if (ivjPatternScopeComboBox == null) {
		try {
			ivjPatternScopeComboBox = new javax.swing.JComboBox();
			ivjPatternScopeComboBox.setName("PatternScopeComboBox");
			ivjPatternScopeComboBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternScopeComboBox;
}
    /**
     * Return the PatternScopeLabel property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getPatternScopeLabel() {
	if (ivjPatternScopeLabel == null) {
	    try {
		ivjPatternScopeLabel = new javax.swing.JLabel();
		ivjPatternScopeLabel.setName("PatternScopeLabel");
		ivjPatternScopeLabel.setText("Pattern Scope");
		ivjPatternScopeLabel.setForeground(java.awt.Color.black);
		// user code begin {1}
		// user code end
	    }
	    catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjPatternScopeLabel;
    }
    /**
     *
     */
    public Pattern getSelectedPattern() {
	return(selectedPattern);
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
	getPatternNameComboBox().addActionListener(ivjEventHandler);
	getPatternScopeComboBox().addActionListener(ivjEventHandler);
}
    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("TemporalPropertyWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(532, 446);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.gridwidth = 2;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsPatternNameComboBox = new java.awt.GridBagConstraints();
		constraintsPatternNameComboBox.gridx = 0; constraintsPatternNameComboBox.gridy = 2;
		constraintsPatternNameComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsPatternNameComboBox.weightx = 1.0;
		constraintsPatternNameComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternNameComboBox(), constraintsPatternNameComboBox);

		java.awt.GridBagConstraints constraintsPatternScopeComboBox = new java.awt.GridBagConstraints();
		constraintsPatternScopeComboBox.gridx = 1; constraintsPatternScopeComboBox.gridy = 2;
		constraintsPatternScopeComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsPatternScopeComboBox.weightx = 1.0;
		constraintsPatternScopeComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternScopeComboBox(), constraintsPatternScopeComboBox);

		java.awt.GridBagConstraints constraintsPatternScopeLabel = new java.awt.GridBagConstraints();
		constraintsPatternScopeLabel.gridx = 1; constraintsPatternScopeLabel.gridy = 1;
		constraintsPatternScopeLabel.anchor = java.awt.GridBagConstraints.WEST;
		constraintsPatternScopeLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternScopeLabel(), constraintsPatternScopeLabel);

		java.awt.GridBagConstraints constraintsPatternLabel = new java.awt.GridBagConstraints();
		constraintsPatternLabel.gridx = 0; constraintsPatternLabel.gridy = 1;
		constraintsPatternLabel.anchor = java.awt.GridBagConstraints.WEST;
		constraintsPatternLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternLabel(), constraintsPatternLabel);

		java.awt.GridBagConstraints constraintsPatternDescriptionScrollPane = new java.awt.GridBagConstraints();
		constraintsPatternDescriptionScrollPane.gridx = 0; constraintsPatternDescriptionScrollPane.gridy = 3;
		constraintsPatternDescriptionScrollPane.gridwidth = 2;
		constraintsPatternDescriptionScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsPatternDescriptionScrollPane.weightx = 1.0;
		constraintsPatternDescriptionScrollPane.weighty = 0.4;
		constraintsPatternDescriptionScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternDescriptionScrollPane(), constraintsPatternDescriptionScrollPane);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	patternList = new ArrayList();
	selectedPattern = null;
	setTitle("Choose a Pattern");
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		TemporalPropertyWizardPanel aTemporalPropertyWizardPanel;
		aTemporalPropertyWizardPanel = new TemporalPropertyWizardPanel();
		frame.setContentPane(aTemporalPropertyWizardPanel);
		frame.setSize(aTemporalPropertyWizardPanel.getSize());
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
 * When a selection is made in the Pattern name box, make sure the pattern
 * description updates as well.
 */
public void patternNameComboBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	String patternName = getPatternNameComboBox().getSelectedItem().toString();
	String patternScope = getPatternScopeComboBox().getSelectedItem().toString();
	selectedPattern = getPattern(patternName, patternScope);
	if(selectedPattern != null) {
		String description = selectedPattern.getDescription();
		getPatternDescriptionTextArea().setText(description);
		getPatternDescriptionTextArea().setCaretPosition(0);
	}
	else {
		getPatternDescriptionTextArea().setText("No description for pattern that has a name " + patternName + " and scope " + patternScope + ".");
		getPatternDescriptionTextArea().setCaretPosition(0);
		log.warn("No pattern can be found that matches patternName: " + patternName +
			", patternScope: " + patternScope);
	}
}
/**
 * When a selection is made in the Pattern scope box, make sure the pattern
 * description updates as well.
 */
public void patternScopeComboBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
	String patternName = getPatternNameComboBox().getSelectedItem().toString();
	String patternScope = getPatternScopeComboBox().getSelectedItem().toString();
	selectedPattern = getPattern(patternName, patternScope);
	if(selectedPattern != null) {
		String description = selectedPattern.getDescription();
		getPatternDescriptionTextArea().setText(description);
	}
	else {
		log.warn("No pattern can be found that matches patternName: " + patternName +
			", patternScope: " + patternScope);
	}
	
}
    /**
     *
     */
    public void setPatternList(List patternList) {

	if((patternList != null) && (patternList.size() > 0)) {
	    this.patternList = patternList;

	    // collect all the pattern name's and scopes to be placed in the combo boxes
	    Set scopeSet = new HashSet();
	    Set nameSet = new HashSet();
	    for(int i = 0; i < patternList.size(); i++) {
		Pattern pattern = (Pattern)patternList.get(i);
		nameSet.add(pattern.getName());
		scopeSet.add(pattern.getScope());
	    }

	    //
	    if(nameSet.size() > 0) {
		JComboBox patternNameComboBox = getPatternNameComboBox();
		Iterator patternNameIterator = nameSet.iterator();
		while(patternNameIterator.hasNext()) {
		    String name = (String)patternNameIterator.next();
		    patternNameComboBox.addItem(name);
		}
	    }
	    else {
		JComboBox patternNameComboBox = getPatternNameComboBox();
		patternNameComboBox.addItem("No pattern names specified.");
	    }

	    if(scopeSet.size() > 0) {
		JComboBox patternScopeComboBox = getPatternScopeComboBox();
		Iterator patternScopeIterator = scopeSet.iterator();
		while(patternScopeIterator.hasNext()) {
		    String scope = (String)patternScopeIterator.next();
		    patternScopeComboBox.addItem(scope);
		}
	    }
	    else {
		JComboBox patternScopeComboBox = getPatternScopeComboBox();
		patternScopeComboBox.addItem("No pattern scopes specified.");
	    }
	    
	}
    }

    /**
     *
     */
    public void setSelectedPattern(Pattern pattern) {

	if((patternList != null) && (patternList.contains(pattern)) &&
	   (pattern != null)) {

	    selectedPattern = pattern;

	    String selectedPatternName = selectedPattern.getName();
	    String selectedPatternScope = selectedPattern.getScope();
	    String description = selectedPattern.getDescription();

	    JComboBox patternNameComboBox = getPatternNameComboBox();
	    JComboBox patternScopeComboBox = getPatternScopeComboBox();
	    JTextArea patternDescriptionTextArea = getPatternDescriptionTextArea();

	    patternNameComboBox.setSelectedItem(selectedPatternName);
	    patternScopeComboBox.setSelectedItem(selectedPatternScope);
	    patternDescriptionTextArea.setText(description);
	    patternDescriptionTextArea.setCaretPosition(0);

	    log.debug("Updated the pattern name combo box: " + selectedPatternName);
	    log.debug("Updated the pattern scope combo box: " + selectedPatternScope);
	    log.debug("Updating the description area: " + description);
	}
	else {
	    log.warn("This pattern is not in the list of patterns so we cannot set it as the selected pattern.");
	}
    }
}
