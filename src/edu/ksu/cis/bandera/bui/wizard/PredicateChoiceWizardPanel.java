package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import javax.swing.*;
import java.awt.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;

import edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.bui.ExpressionBuilder;

import edu.ksu.cis.bandera.checker.CompletionListener;


/**
 * The PredicateChoiceWizardPanel provides an easy way to configure simple expressions
 * for use in a temporal property.  It provides a listing of the currently configured
 * expressions for each parameter and GUI for building new expressions using the information
 * that we have about the current application's specification.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:20 $
 */
public final class PredicateChoiceWizardPanel extends BanderaAbstractWizardPanel implements CompletionListener {
	private JTextArea ivjMessageTextArea = null;
	private final static String message = "After selecting the pattern that will be used, you will need to \"fill in the holes\" of the pattern.  To do this, you will first need build an expression to fill each hole in the pattern.  An expression builder GUI is provided to allow you to easily create simple expressions using the predicates that you have defined in your application.  To build each expression, press the corresponding \"build\" button (next to the text field for each parameter).  An expression might be as simple as a predicate and as complex as a series of conjuctions, disjunctions, and implications.";
	private JLabel ivjPatternLabel = null;
	private JPanel ivjPredicatePanel = null;
	private List predicateList;
    private Set predicateSet;
	private Pattern pattern;
	private Map patternToPredicatesMap;
	private Map predicateMap;
	private final static Category log = Category.getInstance(PredicateChoiceWizardPanel.class);
	private JDialog ivjExpressionBuilderDialog = null;
	private JPanel ivjJDialogContentPane = null;
	private JPanel ivjButtonPanel = null;
	private JButton ivjCancelButton = null;
	private JPanel ivjExpressionBuilderPanel = null;
	private JButton ivjMoreButton = null;
	private JButton ivjOkButton = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private java.util.Map textFieldMap;
	private java.lang.String currentExpressionBuilderParameter;
	private java.util.List comboBoxList;
	private JScrollPane ivjExpressionBuilderScrollPane = null;
	private JScrollPane ivjPredicateScrollPane = null;
	private JTextArea ivjExpressionBuilderTextArea = null;
    private ExpressionBuilder expressionBuilder;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == PredicateChoiceWizardPanel.this.getCancelButton()) 
				connEtoM2(e);
			if (e.getSource() == PredicateChoiceWizardPanel.this.getOkButton()) 
				connEtoC1(e);
		};
	};
/**
 * BSLObservationsWizardPanel constructor comment.
 */
public PredicateChoiceWizardPanel() {
	super();
	initialize();
}
/**
 * connEtoC1:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> PredicateChoiceWizardPanel.okButton_ActionPerformed()V)
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
 * connEtoM2:  (CancelButton.action.actionPerformed(java.awt.event.ActionEvent) --> ExpressionBuilderDialog.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getExpressionBuilderDialog().setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 */
private void generateExpressionBuilderContent() {

	if(comboBoxList == null) {
		comboBoxList = new ArrayList();
	}
	else {
		comboBoxList.clear();
	}

	int maxPredicateRows = 4;
	JPanel panel = getExpressionBuilderPanel();
	panel.removeAll();

	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.insets = new Insets(5, 5, 5, 5);

	for(int i = 0; i < maxPredicateRows; i++) {
		
		JComboBox currentOpenParenComboBox = new JComboBox();
		currentOpenParenComboBox.addItem("");
		currentOpenParenComboBox.addItem("(");
		panel.add(currentOpenParenComboBox, gbc);
		comboBoxList.add(currentOpenParenComboBox);
		gbc.gridx++;

		JComboBox currentNotComboBox = new JComboBox();
		currentNotComboBox.addItem("");
		currentNotComboBox.addItem("!");
		panel.add(currentNotComboBox, gbc);
		comboBoxList.add(currentNotComboBox);
		gbc.gridx++;

		JComboBox currentPredicateComboBox = new JComboBox();
		currentPredicateComboBox.addItem("");
		Iterator pli = predicateList.iterator();
		while(pli.hasNext()) {
			String predicateID = (String)pli.next();
			currentPredicateComboBox.addItem(predicateID);
		}
		panel.add(currentPredicateComboBox, gbc);
		comboBoxList.add(currentPredicateComboBox);
		gbc.gridx++;

		// if this is the last row, don't put a connector. -tcw
		if((i + 1) < maxPredicateRows) {
			JComboBox currentConnectorComboBox = new JComboBox();
			currentConnectorComboBox.addItem("");
			currentConnectorComboBox.addItem("&&");
			currentConnectorComboBox.addItem("||");
			currentConnectorComboBox.addItem("->");
			panel.add(currentConnectorComboBox, gbc);
			comboBoxList.add(currentConnectorComboBox);
		}
		gbc.gridx++;
		
		JComboBox currentCloseParenComboBox = new JComboBox();
		currentCloseParenComboBox.addItem("");
		currentCloseParenComboBox.addItem(")");
		panel.add(currentCloseParenComboBox, gbc);
		comboBoxList.add(currentCloseParenComboBox);
		gbc.gridx++;

		gbc.gridy++;
		gbc.gridx = 0;
	}
}
/**
 * Return the ButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getButtonPanel() {
	if (ivjButtonPanel == null) {
		try {
			ivjButtonPanel = new javax.swing.JPanel();
			ivjButtonPanel.setName("ButtonPanel");
			ivjButtonPanel.setLayout(new java.awt.GridBagLayout());
			ivjButtonPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsCancelButton = new java.awt.GridBagConstraints();
			constraintsCancelButton.gridx = 2; constraintsCancelButton.gridy = 0;
			constraintsCancelButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getCancelButton(), constraintsCancelButton);

			java.awt.GridBagConstraints constraintsOkButton = new java.awt.GridBagConstraints();
			constraintsOkButton.gridx = 1; constraintsOkButton.gridy = 0;
			constraintsOkButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getOkButton(), constraintsOkButton);

			java.awt.GridBagConstraints constraintsMoreButton = new java.awt.GridBagConstraints();
			constraintsMoreButton.gridx = 0; constraintsMoreButton.gridy = 0;
			constraintsMoreButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getMoreButton(), constraintsMoreButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButtonPanel;
}
/**
 * Return the CancelButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCancelButton() {
	if (ivjCancelButton == null) {
		try {
			ivjCancelButton = new javax.swing.JButton();
			ivjCancelButton.setName("CancelButton");
			ivjCancelButton.setText("Cancel");
			ivjCancelButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCancelButton;
}
/**
 * Insert the method's description here.
 * Creation date: (11/25/2002 10:30:16 AM)
 * @return java.lang.String
 */
private java.lang.String getCurrentExpressionBuilderParameter() {
	return currentExpressionBuilderParameter;
}
/**
 * Return the ExpressionBuilderDialog property value.
 * @return javax.swing.JDialog
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JDialog getExpressionBuilderDialog() {
	if (ivjExpressionBuilderDialog == null) {
		try {
			ivjExpressionBuilderDialog = new javax.swing.JDialog();
			ivjExpressionBuilderDialog.setName("ExpressionBuilderDialog");
			ivjExpressionBuilderDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			ivjExpressionBuilderDialog.setBounds(541, 21, 530, 330);
			ivjExpressionBuilderDialog.setTitle("Expression Builder");
			getExpressionBuilderDialog().setContentPane(getJDialogContentPane());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpressionBuilderDialog;
}
/**
 * Return the ExpressionBuilderPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getExpressionBuilderPanel() {
	if (ivjExpressionBuilderPanel == null) {
		try {
			ivjExpressionBuilderPanel = new javax.swing.JPanel();
			ivjExpressionBuilderPanel.setName("ExpressionBuilderPanel");
			ivjExpressionBuilderPanel.setLayout(new java.awt.GridBagLayout());
			ivjExpressionBuilderPanel.setBounds(0, 0, 522, 191);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpressionBuilderPanel;
}
/**
 * Return the ExpressionBuilderScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getExpressionBuilderScrollPane() {
	if (ivjExpressionBuilderScrollPane == null) {
		try {
			ivjExpressionBuilderScrollPane = new javax.swing.JScrollPane();
			ivjExpressionBuilderScrollPane.setName("ExpressionBuilderScrollPane");
			getExpressionBuilderScrollPane().setViewportView(getExpressionBuilderPanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpressionBuilderScrollPane;
}
/**
 * Return the ExpressionBuilderTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getExpressionBuilderTextArea() {
	if (ivjExpressionBuilderTextArea == null) {
		try {
			ivjExpressionBuilderTextArea = new javax.swing.JTextArea();
			ivjExpressionBuilderTextArea.setName("ExpressionBuilderTextArea");
			ivjExpressionBuilderTextArea.setLineWrap(true);
			ivjExpressionBuilderTextArea.setWrapStyleWord(true);
			ivjExpressionBuilderTextArea.setText("The Expression Builder provides a way to create a simple BSL expression.  You can use the drop-down boxes to choose operators (not, and, or, implies) and grouping (parenthesis).  When you are done, press OK.  If you don\'t want to use the expression you have configured, choose Cancel.  If there are not enough rows or drop-down boxes (you need a more complex expression), press the More button.");
			ivjExpressionBuilderTextArea.setBackground(new java.awt.Color(204,204,255));
			ivjExpressionBuilderTextArea.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpressionBuilderTextArea;
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

			java.awt.GridBagConstraints constraintsButtonPanel = new java.awt.GridBagConstraints();
			constraintsButtonPanel.gridx = 0; constraintsButtonPanel.gridy = 2;
			constraintsButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsButtonPanel.weightx = 1.0;
			constraintsButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getButtonPanel(), constraintsButtonPanel);

			java.awt.GridBagConstraints constraintsExpressionBuilderScrollPane = new java.awt.GridBagConstraints();
			constraintsExpressionBuilderScrollPane.gridx = 0; constraintsExpressionBuilderScrollPane.gridy = 1;
			constraintsExpressionBuilderScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsExpressionBuilderScrollPane.weightx = 1.0;
			constraintsExpressionBuilderScrollPane.weighty = 1.0;
			constraintsExpressionBuilderScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getExpressionBuilderScrollPane(), constraintsExpressionBuilderScrollPane);

			java.awt.GridBagConstraints constraintsExpressionBuilderTextArea = new java.awt.GridBagConstraints();
			constraintsExpressionBuilderTextArea.gridx = 0; constraintsExpressionBuilderTextArea.gridy = 0;
			constraintsExpressionBuilderTextArea.fill = java.awt.GridBagConstraints.BOTH;
			constraintsExpressionBuilderTextArea.ipadx = 10;
			constraintsExpressionBuilderTextArea.ipady = 10;
			constraintsExpressionBuilderTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getExpressionBuilderTextArea(), constraintsExpressionBuilderTextArea);
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
 * Return the MoreButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getMoreButton() {
	if (ivjMoreButton == null) {
		try {
			ivjMoreButton = new javax.swing.JButton();
			ivjMoreButton.setName("MoreButton");
			ivjMoreButton.setText("More");
			ivjMoreButton.setBackground(new java.awt.Color(204,204,255));
			ivjMoreButton.setMaximumSize(new java.awt.Dimension(73, 25));
			ivjMoreButton.setPreferredSize(new java.awt.Dimension(73, 25));
			ivjMoreButton.setMinimumSize(new java.awt.Dimension(73, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMoreButton;
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
			ivjOkButton.setMaximumSize(new java.awt.Dimension(73, 25));
			ivjOkButton.setPreferredSize(new java.awt.Dimension(73, 25));
			ivjOkButton.setMinimumSize(new java.awt.Dimension(73, 25));
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
 * Return the PatternLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPatternLabel() {
	if (ivjPatternLabel == null) {
		try {
			ivjPatternLabel = new javax.swing.JLabel();
			ivjPatternLabel.setName("PatternLabel");
			ivjPatternLabel.setText("<Pattern>");
			ivjPatternLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternLabel;
}
/**
 * Return the PredicatePanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getPredicatePanel() {
	if (ivjPredicatePanel == null) {
		try {
			ivjPredicatePanel = new javax.swing.JPanel();
			ivjPredicatePanel.setName("PredicatePanel");
			ivjPredicatePanel.setLayout(new java.awt.GridBagLayout());
			ivjPredicatePanel.setBounds(0, 0, 499, 160);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPredicatePanel;
}
/**
 * Return the PredicateScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getPredicateScrollPane() {
	if (ivjPredicateScrollPane == null) {
		try {
			ivjPredicateScrollPane = new javax.swing.JScrollPane();
			ivjPredicateScrollPane.setName("PredicateScrollPane");
			getPredicateScrollPane().setViewportView(getPredicatePanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPredicateScrollPane;
}
/**
 * Get the user defined expression that matches the parameters to the pattern defined.
 * This method will return a Map where the parameter name is the key and the expression
 * is the value.
 *
 * @return Map
 */
public Map getSelectedPredicates() {

	TreeMap selectedPredicatesMap = new TreeMap();

	Iterator iterator = patternToPredicatesMap.keySet().iterator();
	while(iterator.hasNext()) {
		Object key = iterator.next();
		List l = (List)patternToPredicatesMap.get(key);
		String parameter = key.toString();
		selectedPredicatesMap.put(parameter, l);
	}

	return(selectedPredicatesMap);
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
	getCancelButton().addActionListener(ivjEventHandler);
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
		setName("BSLObservationsWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(507, 378);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsPatternLabel = new java.awt.GridBagConstraints();
		constraintsPatternLabel.gridx = 0; constraintsPatternLabel.gridy = 1;
		constraintsPatternLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternLabel(), constraintsPatternLabel);

		java.awt.GridBagConstraints constraintsPredicateScrollPane = new java.awt.GridBagConstraints();
		constraintsPredicateScrollPane.gridx = 0; constraintsPredicateScrollPane.gridy = 2;
		constraintsPredicateScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsPredicateScrollPane.weightx = 1.0;
		constraintsPredicateScrollPane.weighty = 1.0;
		constraintsPredicateScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPredicateScrollPane(), constraintsPredicateScrollPane);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Define Expressions");
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		PredicateChoiceWizardPanel aPredicateChoiceWizardPanel;
		aPredicateChoiceWizardPanel = new PredicateChoiceWizardPanel();
		frame.setContentPane(aPredicateChoiceWizardPanel);
		frame.setSize(aPredicateChoiceWizardPanel.getSize());
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
 * When the user presses the ok button, we will generate the expression
 * using the combo box list and put that text into the current text field.
 */
public void okButton_ActionPerformed() {

    List expressionList = new ArrayList();
    StringBuffer sb = new StringBuffer();
    Iterator cbli = comboBoxList.iterator();
    while (cbli.hasNext()) {
        JComboBox cb = (JComboBox) cbli.next();
        String s = cb.getSelectedItem().toString();
        if ((s != null) && (s.length() > 0) && (!s.equals(""))) {
            if ((!s.equals("("))
                && (!s.equals(")"))
                && (!s.equals("&&"))
                && (!s.equals("!"))
                && (!s.equals("||"))
                && (!s.equals("->"))) {

                Predicate p = (Predicate) predicateMap.get(s);
                if (p != null) {
		    log.debug("adding the predicate " + p.getName().toString() + " to the expressionList.");
                    expressionList.add(p);
		    sb.append(p.getName().toString());
		    sb.append("(");
		    int paramCount = p.getNumOfParams();
		    if(!(p.isStatic())) {
			sb.append(p.getType().toString());
			if(paramCount > 0) {
			    sb.append(", ");
			}
		    }
		    for(int i = 0; i < paramCount; i++) {
			String paramType = p.getParamType(i).toString();
			sb.append(paramType);
			if(i < paramCount) {
			    sb.append(", ");
			}
		    }
		    sb.append(")");
                }
            }
            else {
		log.debug("adding the string " + s + " to the expressionList.");
		sb.append(s);
                expressionList.add(s);
            }
        }
    }

    String parameter = getCurrentExpressionBuilderParameter();
    JTextField tf = (JTextField) textFieldMap.get(parameter);
    if (tf != null) {
        tf.setText(sb.toString());
    }

    patternToPredicatesMap.put(parameter, expressionList);
    
    getExpressionBuilderDialog().setVisible(false);
}
/**
 * Insert the method's description here.
 * Creation date: (4/26/2002 1:07:00 PM)
 * @return java.util.List
 * @param pattern java.lang.String
 */
public static List parsePredicates(String pattern) {

	ArrayList predicates = new ArrayList();
	int start = 0;
	int end = 0;

	// walk through the pattern looking for {predicate}
	while(start < pattern.length()) {
		start = pattern.indexOf("{", end);
		if(start >= 0) {
			end = pattern.indexOf("}", start);
			if(end >= 0) {
				String currentPredicate = pattern.substring(start + 1, end);
				predicates.add(currentPredicate);
			}
			else {
				// opened but didn't close the predicate.
				log.warn("An error occured while parsing the pattern (end was >= 0).  The pattern hole started but didn't end.");
				return(null);
			}
		}
		else {
			// no more predicate starts.  quit now!
			break;
		}
	}

	return(predicates);
}
/**
 * Insert the method's description here.
 * Creation date: (11/25/2002 10:30:16 AM)
 * @param newCurrentExpressionBuilderParameter java.lang.String
 */
private void setCurrentExpressionBuilderParameter(java.lang.String newCurrentExpressionBuilderParameter) {
	currentExpressionBuilderParameter = newCurrentExpressionBuilderParameter;
}
/**
 * Set the pattern that this WizardPanel will display for the user.  This will
 * also cause a side-effect.
 * 
 * @param Pattern pattern
 * @pre predicateList != null
 */
public void setPattern(Pattern pattern) {

	if(pattern == null) {
		log.warn("pattern is null.  The PredicateChoiceWizardPanel cannot work with a null pattern.");
		return;
	}

	if(predicateList == null) {
		log.warn("predicateList is null.  The PredicateChoiceWizardPanel cannot work with a null predicateList.");
		return;
	}

	if(textFieldMap == null) {
		textFieldMap = new HashMap();
	}
	else {
		textFieldMap.clear();
	}

	patternToPredicatesMap = new HashMap();

	this.pattern = pattern;
	
	JLabel patternLabel = getPatternLabel();
	patternLabel.setText(pattern.getFormat());

	JPanel predicatePanel = getPredicatePanel();
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.insets = new Insets(5, 5, 5, 5);

	// now parse out the predicates
	Vector parameters = pattern.getParametersOccurenceOrder();
	if(parameters != null) {
	    for(int i = 0; i < parameters.size(); i++) {
		String currentParameter = (String)parameters.get(i);
		
		JLabel currentLabel = new JLabel(currentParameter);
		JTextField currentExpressionTextField = new JTextField("Use the expression builder.", 50);
		currentExpressionTextField.setEditable(false);
		textFieldMap.put(currentParameter, currentExpressionTextField);
		
		JButton expressionBuilderButton = new JButton("Build " + currentParameter);
		expressionBuilderButton.setActionCommand(currentParameter);
		java.awt.event.ActionListener ac = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent ae) {
				//generateExpressionBuilderContent();
				//getExpressionBuilderDialog().setVisible(true);
				//setCurrentExpressionBuilderParameter(ae.getActionCommand());

				// create an expression builder using the current set of predicates and
				//  the currently configured expression.
				ExpressionBuilder expressionBuilder = getExpressionBuilder();
				expressionBuilder.setPredicateSet(predicateSet);
				String currentExpression = getCurrentExpression(ae.getActionCommand());
				if((currentExpression != null) &&
				   (currentExpression.length() > 0) &&
				   (!currentExpression.equals("Use the expression builder."))) {
				    expressionBuilder.setExpressionString(currentExpression);
				}
				else {
				    expressionBuilder.setExpressionString("");
				}
				expressionBuilder.setVisible(true);

				// setup the wizard panel so that when the expression builder completes, it
				//  will update the correct text field with the configured expression.
				setCurrentExpressionBuilderParameter(ae.getActionCommand());
			}
		};
		expressionBuilderButton.addActionListener(ac);

		//patternToPredicatesMap.put(currentParameter, currentExpressionTextField);

		predicatePanel.add(currentLabel, gbc);
		gbc.gridx++;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		predicatePanel.add(currentExpressionTextField, gbc);
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx++;

		predicatePanel.add(expressionBuilderButton, gbc);
		gbc.gridx = 0;
		gbc.gridy++;

	    }
	}
	else {
		// put a message on the screen saying no predicates were found
		JLabel noPredicateLabel = new JLabel("There were no parameters defined in the pattern.");
		getPredicatePanel().add(noPredicateLabel);
	}
}
/**
 * Set the list of predicates that will be displayed in this stage of the
 * wizard.  This method expects a list of String objects as a parameter.
 *
 * @param List predicateList A List of String objects that represent the possible predicates
 *        that are available to the user at this stage of the Wizard.
 */
public void setPredicates(List predicateList) {
	
	if(predicateList == null) {
		log.warn("predicateList is null.  The PredicateChoiceWizardPanel cannot function with a null predicateList.");
		return;
	}

	ArrayList tempList = new ArrayList(predicateList.size());
	predicateSet = new HashSet(predicateList);
	
	predicateMap = new HashMap(predicateList.size());
	for(int i = 0; i < predicateList.size(); i++) {

		Predicate currentPredicate = (Predicate)predicateList.get(i);
		String predicateID = currentPredicate.getName().toString();

		tempList.add(predicateID);
		predicateMap.put(predicateID, currentPredicate);
		
	}

	this.predicateList = tempList;
	
}

/**
 * Set the currently defined expressions for each parameter.
 *
 * @param Map predicateMap This is a Map from parameter name (i.e. P, R, S) to
 *        an expression.
 */
public void setCurrentExpressions(Map predicateMap) {

    if (pattern == null) {
        log.error(
            "There is currently no pattern set for this panel so setting the current expressions is useless.");
        return;
    }

    // the size of the predicate map should be the same as the number of parameters
    //  in the current pattern. -tcw
    if ((predicateMap != null) && (predicateMap.size() > 0)) {

        Vector parameterVector = pattern.getParametersOccurenceOrder();
        if ((parameterVector != null) && (parameterVector.size() > 0)) {
            Iterator pvi = parameterVector.iterator();
            while (pvi.hasNext()) {
                String parameterName = (String) pvi.next();
                String expression = (String) predicateMap.get(parameterName);

		// in order to properly init the system, we should parse the
		//  expression given into a list and store that list in the Map.
		//  this is probably not the best way to do this so it should
		//  be re-designed to make it more efficient and not such
		//  a HACK! -tcw
		getExpressionBuilder().setPredicateSet(predicateSet);
		getExpressionBuilder().setExpressionString(expression);
		List expressionList = getExpressionBuilder().getExpressionList();
		patternToPredicatesMap.put(parameterName, expressionList);

                JTextField textField = (JTextField) textFieldMap.get(parameterName);
                textField.setText(expression);
            }
        }
    }
}

/**
 * Get the current expression that is shown in the text field for the
 * given parameter.
 * 
 * @return java.lang.String A parameter name (P, S, etc.).
 */
private String getCurrentExpression(String parameterName) {

	String expression = "";
	JTextField textField = (JTextField)textFieldMap.get(parameterName);
	if(textField != null) {
		expression = textField.getText();
	}
	else {
		expression = "";
	}
	return(expression);
}

    /**
     * When tasks are completed (like the ExpressionBuilder), we should update
     * our state based upon the final configuration.
     *
     * @param Object o The object that completed the task.
     */
    public void complete(Object o) {

	if(o == null) {
	    log.debug("Not sure what to do with the completion of a task that passes a null object.");
	}
	else if(o instanceof ExpressionBuilder) {
	    ExpressionBuilder eb = (ExpressionBuilder)o;

	    if(eb.isCancelled()) {
		log.debug("ExpressionBuilder completed but was cancelled.");
	    }
	    else {
		String parameter = getCurrentExpressionBuilderParameter();

		// update the appropriate text field
		JTextField tf = (JTextField) textFieldMap.get(parameter);
		if (tf != null) {
		    String expression = eb.getExpressionString();
		    tf.setText(expression);
		}
		
		// update the current configuration
		List expressionList = eb.getExpressionList();
		patternToPredicatesMap.put(parameter, expressionList);
	    }
	}
	else {
	    log.debug("Not sure what to do with the completion of a task that passes an object of type " +
		      o.getClass().getName());
	}
    }

    protected ExpressionBuilder getExpressionBuilder() {
	if(expressionBuilder == null) {
	    expressionBuilder = new ExpressionBuilder();
	    expressionBuilder.registerCompletionListener(this);
	}
	return(expressionBuilder);
    }
}
