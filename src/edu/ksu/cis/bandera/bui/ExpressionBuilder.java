package edu.ksu.cis.bandera.bui;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate;

import edu.ksu.cis.bandera.checker.CompletionListener;

import org.apache.log4j.Category;

/**
 * The ExpressionBuilder dialog provides an easy way for the user to configure a BSL
 * expression using a GUI tool.  It provides combo boxes for all the parts of a valid expression.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:15 $
 */
public final class ExpressionBuilder extends JDialog {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(ExpressionBuilder.class.getName());

    private JPanel ivjButtonPanel = null;
    private JButton ivjCancelButton = null;
    private JPanel ivjExpressionPanel = null;
    private JScrollPane ivjExpressionPanelScrollPane = null;
    private JPanel ivjJDialogContentPane = null;
    private JButton ivjMoreButton = null;
    private JButton ivjOKButton = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private boolean canceled = false;

    /**
     * A Set of CompletionListener objects that will notified when the view
     * has completed configuration.
     */
    private Set completionListenerSet;

    /**
     * This is a list of the JComboBox objects that make up the expression
     * builder interface.
     */
    private List comboBoxList;

    /**
     * This is the number of rows to generate initially for use in the expression
     * builder interface.
     */
    private final static int INITIAL_ROWS = 5;

    /**
     * When the user presses the "More" button, this is the number of rows that
     * will be added.
     */
    private final static int ROW_INCREMENT = 5;

    /**
     * This provides a mapping from predicate names back to the predicate.
     */
    private Map predicateNameMap;

    /**
     * This is a sorted set of the predicates that will be used in the interface.
     */
    private SortedSet sortedPredicateSet;

    /**
     * This provides a current count of the rows provided in the interface.
     */
    private int currentRowCount;

    class IvjEventHandler implements java.awt.event.ActionListener {
	public void actionPerformed(java.awt.event.ActionEvent e) {
	    if (e.getSource() == ExpressionBuilder.this.getMoreButton()) 
		connEtoC1(e);
	    if (e.getSource() == ExpressionBuilder.this.getOKButton()) 
		connEtoC2(e);
	    if (e.getSource() == ExpressionBuilder.this.getCancelButton()) 
		connEtoC3(e);
	};
    };
    private JTextArea ivjMessageTextArea = null;
    /**
     * ExpressionBuilder constructor comment.
     */
    public ExpressionBuilder() {
	super();
	initialize();
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Dialog
     */
    public ExpressionBuilder(java.awt.Dialog owner) {
	super(owner);
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     */
    public ExpressionBuilder(java.awt.Dialog owner, String title) {
	super(owner, title);
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     * @param modal boolean
     */
    public ExpressionBuilder(java.awt.Dialog owner, String title, boolean modal) {
	super(owner, title, modal);
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Dialog
     * @param modal boolean
     */
    public ExpressionBuilder(java.awt.Dialog owner, boolean modal) {
	super(owner, modal);
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Frame
     */
    public ExpressionBuilder(java.awt.Frame owner) {
	super(owner);
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     */
    public ExpressionBuilder(java.awt.Frame owner, String title) {
	super(owner, title);
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     * @param modal boolean
     */
    public ExpressionBuilder(java.awt.Frame owner, String title, boolean modal) {
	super(owner, title, modal);
    }
    /**
     * ExpressionBuilder constructor comment.
     * @param owner java.awt.Frame
     * @param modal boolean
     */
    public ExpressionBuilder(java.awt.Frame owner, boolean modal) {
	super(owner, modal);
    }

    /**
     * When the Cancel button is pressed, we should generate a signal to all the completion listeners.
     */
    public void cancelButton_ActionPerformed() {
	canceled = true;
	fireCompletion();
	setVisible(false);
    }
    /**
     * Insert the method's description here.
     * Creation date: (12/14/2002 12:35:39 PM)
     */
    private void clearExpressionComboBoxes() {}
    /**
     * connEtoC1:  (MoreButton.action.actionPerformed(java.awt.event.ActionEvent) --> ExpressionBuilder.moreButton_ActionPerformed()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.moreButton_ActionPerformed();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC2:  (OKButton.action.actionPerformed(java.awt.event.ActionEvent) --> ExpressionBuilder.oKButton_ActionPerformed()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.oKButton_ActionPerformed();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoC3:  (CancelButton.action.actionPerformed(java.awt.event.ActionEvent) --> ExpressionBuilder.cancelButton_ActionPerformed()V)
     * @param arg1 java.awt.event.ActionEvent
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
	    // user code begin {1}
	    // user code end
	    this.cancelButton_ActionPerformed();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * Determine if the given combo box contains the object, item, given.
     *
     * @return boolean
     * @param comboBox javax.swing.JComboBox
     * @param item java.lang.Object
     */
    private boolean doesComboBoxContainItem(JComboBox comboBox, Object item) {

	boolean contains = false;
	if ((comboBox == null) || (comboBox.getItemCount() <= 0)) {
	    contains = false;
	}
	else {
	    for (int i = 0; i < comboBox.getItemCount(); i++) {
		Object currentItem = comboBox.getItemAt(i);
		if (((currentItem == null) && (item == null))
		    || ((currentItem != null) && (currentItem.equals(item)))) {
		    contains = true;
		    break;
		}
	    }
	}

	return(contains);
    }

    /**
     * This method will generate the specified number of expression combo boxes
     * based upon the number of rows specified.  Each row will contain:
     * <ol>
     * <li>Open parenthesis combo box</li>
     * <li>Not combo box</li>
     * <li>Predicate combo box</li>
     * <li>Connector (conjuction, disjunction, implies) combo box</li>
     * <li>Close parenthesis combo box</li>
     * </ol>
     * 
     * @param rows int
     */
    private void generateExpressionComboBoxes(int rows) {

	if(sortedPredicateSet == null) {
	    log.error("The ExpressionBuilder was not initialized properly.  sortedPredicateSet is null.");
	    return;
	}

	log.debug("Generating " + rows + " rows of combo boxes.");

	// clear the combo box list since we will be regenerating it
	if(comboBoxList == null) {
	    comboBoxList = new ArrayList();
	}
	else {
	    comboBoxList.clear();
	}

	JPanel expressionPanel  = getExpressionPanel();
	expressionPanel.removeAll();
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.insets = new Insets(5, 5, 5, 5);

	for(int i = 0; i < rows; i++) {
	    List cbList = generateExpressionComboBoxList();
		
	    for(int j = 0; j < cbList.size(); j++) {
		JComboBox cb = (JComboBox)cbList.get(j);
			
		// we will need to skip the last connector combo box in the last row which
		//  means the last row (row - 1) and the 2nd to last combo box (cbList.size() - 2).
		if((i == rows - 1) && (j == cbList.size() - 2)) {
		    // skip this last connector combo box
		    log.debug("skipping the last connector box.");
		}
		else {
		    expressionPanel.add(cb, gbc);
		    comboBoxList.add(cb);
		}

		// move to the next column
		gbc.gridx++;
	    }

	    // move to the next row
	    gbc.gridy++;
	    gbc.gridx = 0;
		
	}

	currentRowCount = rows;
	
    }
    /**
     * Generate a List of ComboBoxes that represent a row in the expression builder.
     *
     * @return java.util.List
     */
    private List generateExpressionComboBoxList() {

	if(sortedPredicateSet == null) {
	    log.error("The ExpressionBuilder was not properly initialized.   The sortedPredicateSet was null.");
	    return(new ArrayList(0));
	}

	ArrayList cbList = new ArrayList(5);
    
	JComboBox currentOpenParenComboBox = new JComboBox();
	currentOpenParenComboBox.addItem("");
	currentOpenParenComboBox.addItem("(");
	cbList.add(currentOpenParenComboBox);

	JComboBox currentNotComboBox = new JComboBox();
	currentNotComboBox.addItem("");
	currentNotComboBox.addItem("!");
	cbList.add(currentNotComboBox);

	JComboBox currentPredicateComboBox = new JComboBox();
	currentPredicateComboBox.addItem("");
	Iterator spsi = sortedPredicateSet.iterator();
	while (spsi.hasNext()) {
	    Predicate predicate = (Predicate) spsi.next();
	    String predicateID = predicate.getName().toString();
	    currentPredicateComboBox.addItem(predicateID);
	}
	cbList.add(currentPredicateComboBox);

	JComboBox currentConnectorComboBox = new JComboBox();
	currentConnectorComboBox.addItem("");
	currentConnectorComboBox.addItem("&&");
	currentConnectorComboBox.addItem("||");
	currentConnectorComboBox.addItem("->");
	cbList.add(currentConnectorComboBox);

	JComboBox currentCloseParenComboBox = new JComboBox();
	currentCloseParenComboBox.addItem("");
	currentCloseParenComboBox.addItem(")");
	cbList.add(currentCloseParenComboBox);

	return (cbList);
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

		java.awt.GridBagConstraints constraintsOKButton = new java.awt.GridBagConstraints();
		constraintsOKButton.gridx = 1; constraintsOKButton.gridy = 0;
		constraintsOKButton.insets = new java.awt.Insets(4, 4, 4, 4);
		getButtonPanel().add(getOKButton(), constraintsOKButton);

		java.awt.GridBagConstraints constraintsCancelButton = new java.awt.GridBagConstraints();
		constraintsCancelButton.gridx = 2; constraintsCancelButton.gridy = 0;
		constraintsCancelButton.insets = new java.awt.Insets(4, 4, 4, 4);
		getButtonPanel().add(getCancelButton(), constraintsCancelButton);

		java.awt.GridBagConstraints constraintsMoreButton = new java.awt.GridBagConstraints();
		constraintsMoreButton.gridx = 0; constraintsMoreButton.gridy = 0;
		constraintsMoreButton.insets = new java.awt.Insets(4, 4, 4, 50);
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
     * Using the current state of the ComboBoxes, generate a List
     * that represents the expression parts.  This will be a list of
     * String and Predicate objects.
     *
     * @return java.util.List
     */
    public List getExpressionList() {

	List expressionList = new ArrayList();

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

		    Predicate p = (Predicate) predicateNameMap.get(s);
		    if (p != null) {
			//log.debug("adding the predicate " + p.getName().toString() + " to the expressionList.");
			expressionList.add(p);
		    }
		}
		else {
		    //log.debug("adding the string " + s + " to the expressionList.");
		    expressionList.add(s);
		}
	    }
	}

	return (expressionList);
    }
    /**
     * Return the ExpressionPanel property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getExpressionPanel() {
	if (ivjExpressionPanel == null) {
	    try {
		ivjExpressionPanel = new javax.swing.JPanel();
		ivjExpressionPanel.setName("ExpressionPanel");
		ivjExpressionPanel.setLayout(new java.awt.GridBagLayout());
		ivjExpressionPanel.setLocation(0, 0);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjExpressionPanel;
    }
    /**
     * Return the ExpressionPanelScrollPane property value.
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getExpressionPanelScrollPane() {
	if (ivjExpressionPanelScrollPane == null) {
	    try {
		ivjExpressionPanelScrollPane = new javax.swing.JScrollPane();
		ivjExpressionPanelScrollPane.setName("ExpressionPanelScrollPane");
		getExpressionPanelScrollPane().setViewportView(getExpressionPanel());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjExpressionPanelScrollPane;
    }
    /**
     * Get the expression as a String that is currently configured in the
     * view.
     *
     * @return java.lang.String
     */
    public String getExpressionString() {

	StringBuffer sb = new StringBuffer();

	List expressionList = getExpressionList();
	if((expressionList != null) && (expressionList.size() > 0)) {
	    for(int i = 0; i < expressionList.size(); i++) {
		Object o = expressionList.get(i);
			
		if(o == null) {
		    continue;
		}
			
		if(o instanceof String) {
		    sb.append(o.toString());
		    continue;
		}
			
		if(o instanceof Predicate) {
		    Predicate p = (Predicate)o;
		    sb.append(p.getName().toString());
		    continue;
		}
	    }
	}

	return(sb.toString().trim());
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

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		getJDialogContentPane().add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsExpressionPanelScrollPane = new java.awt.GridBagConstraints();
		constraintsExpressionPanelScrollPane.gridx = 0; constraintsExpressionPanelScrollPane.gridy = 1;
		constraintsExpressionPanelScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsExpressionPanelScrollPane.weightx = 1.0;
		constraintsExpressionPanelScrollPane.weighty = 1.0;
		constraintsExpressionPanelScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		getJDialogContentPane().add(getExpressionPanelScrollPane(), constraintsExpressionPanelScrollPane);

		java.awt.GridBagConstraints constraintsButtonPanel = new java.awt.GridBagConstraints();
		constraintsButtonPanel.gridx = 0; constraintsButtonPanel.gridy = 2;
		constraintsButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsButtonPanel.weightx = 1.0;
		constraintsButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		getJDialogContentPane().add(getButtonPanel(), constraintsButtonPanel);
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
     * Return the JTextArea1 property value.
     * @return javax.swing.JTextArea
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextArea getMessageTextArea() {
	if (ivjMessageTextArea == null) {
	    try {
		ivjMessageTextArea = new javax.swing.JTextArea();
		ivjMessageTextArea.setName("MessageTextArea");
		ivjMessageTextArea.setLineWrap(true);
		ivjMessageTextArea.setWrapStyleWord(true);
		ivjMessageTextArea.setEditable(false);
		ivjMessageTextArea.setText("The Expression Builder provides a way to create a simple BSL expression.  You can use the drop-down boxes to choose operators (not, and, or, implies) and grouping (parenthesis).  When you are done, press OK.  If you don\'t want to use the expression you have configured, choose Cancel.  If there are not enough rows or drop-down boxes (you need a more complex expression), press the More button.");
		ivjMessageTextArea.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
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
     * Return the OKButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getOKButton() {
	if (ivjOKButton == null) {
	    try {
		ivjOKButton = new javax.swing.JButton();
		ivjOKButton.setName("OKButton");
		ivjOKButton.setText("OK");
		ivjOKButton.setBackground(new java.awt.Color(204,204,255));
		ivjOKButton.setMaximumSize(new java.awt.Dimension(73, 25));
		ivjOKButton.setPreferredSize(new java.awt.Dimension(73, 25));
		ivjOKButton.setMinimumSize(new java.awt.Dimension(73, 25));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjOKButton;
    }
    /**
     * Insert the method's description here.
     * Creation date: (12/13/2002 4:32:23 PM)
     * @return java.util.Set
     */
    public Set getPredicateSet() {
	return(sortedPredicateSet);
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
	getMoreButton().addActionListener(ivjEventHandler);
	getOKButton().addActionListener(ivjEventHandler);
	getCancelButton().addActionListener(ivjEventHandler);
    }
    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
	try {
	    // user code begin {1}
	    // user code end
	    setName("ExpressionBuilder");
	    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	    setSize(520, 381);
	    setTitle("Expression Builder");
	    setContentPane(getJDialogContentPane());
	    initConnections();
	} catch (java.lang.Throwable ivjExc) {
	    handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
    }

    /**
     * When the more button is pressed, we need to generate more combo boxes
     * so the user can generate a more complex expression.  This will save the currently
     * configured expression, generate more combo boxes, and re-do the saved
     * expression in the combo boxes.
     */
    public void moreButton_ActionPerformed() {

	String currentExpression = getExpressionString();
	clearExpressionComboBoxes();
	generateExpressionComboBoxes(currentRowCount + ROW_INCREMENT);
	setExpressionString(currentExpression);
	
    }

    /**
     * When the OK button is pressed, we should generate a signal to ...
     */
    public void oKButton_ActionPerformed() {
	// when the OK button is pressed, we should report completion
	canceled = false;
	fireCompletion();
	setVisible(false);
    }

    /**
     * Set the current expression based upon this list of Strings and Predicates.
     *
     * @param expressionList java.util.List
     */
    public void setExpressionList(List expressionList) {

	log.debug("setting an expression list...");

	/*
	 * This method should figure out how many rows are necessary or have some
	 * form of catching the error, increasing the size, and trying again! -tcw
	 */

	if ((comboBoxList == null) || (comboBoxList.size() <= 0)) {
	    log.error("The ExpressionBuilder was not properly initialized.  The comboBoxList is null.");
	    return;
	}

	clearExpressionComboBoxes();
	comboBoxList.clear();

	if ((expressionList == null) || (expressionList.size() <= 0)) {
	    log.debug("The expression list had nothing of interest.");
	    return;
	}
	else {
	    log.debug("The expression list has " + expressionList.size() + " parts.");
	}

	// generate a rough estimate of the number of rows of expression to generate
	//  this will be the size of the expression (number of parts to the expression) divided
	//  by the number of parts per row times 2.  we will take the ceiling value of this and
	//  convert it to an int.
	int roughEstimate = new Double(Math.ceil((expressionList.size() / 5) * 2)).intValue();
	if(roughEstimate < INITIAL_ROWS) {
	    roughEstimate = INITIAL_ROWS;
	}
	log.debug("The rough estimate for rows is " + roughEstimate);
	generateExpressionComboBoxes(roughEstimate);
    
	int currentComboBox = 0;

	for (int i = 0; i < expressionList.size(); i++) {
	    Object o = expressionList.get(i);
	    if (o == null) {
		continue;
	    }
	    String item = "";

	    if (o instanceof String) {
		item = (String) o;
	    }

	    if (o instanceof Predicate) {
		Predicate predicate = (Predicate) o;
		item = predicate.getName().toString();
	    }
        
	    if(currentComboBox >= comboBoxList.size()) {
		log.error("We got to the end of the combo box list before the last part of the expression.");
	    }

	    // find the next combo box that holds this string and set it as selected
	    for (int j = currentComboBox; j < comboBoxList.size(); j++) {
		JComboBox cb = (JComboBox) comboBoxList.get(j);
		log.debug("checking combo box " + j + ".");

		if (doesComboBoxContainItem(cb, item)) {
		    log.debug("Found the correct combo box for item " + item);
		    cb.setSelectedItem(item);
		    currentComboBox = j;
		    break;
		}
	    }
	}

    }
    /**
     * This will set the current expression, as a String, for this expression builder.  This
     * will update the combo boxes to match this expression.
     * 
     * @param expression java.lang.String
     */
    public void setExpressionString(String expression) {

	log.debug("Setting an expression: " + expression);

	if(predicateNameMap == null) {
	    log.error("The expression builder was not initialized properly.  predicateNameMap was null. Quitting.");
	    return;
	}

	List expressionList = null;
	
	if((expression == null) || (expression.length() == 0)) {
	    expressionList = new ArrayList(0);
	}
	else {
	    expressionList = new ArrayList(expression.length());
		
	    for(int i = 0; i < expression.length(); i++) {
		char currentChar = expression.charAt(i);
		log.debug("currentChar = " + currentChar);

		if((currentChar == ' ') || (currentChar == '\n') ||
		   (currentChar == '\t') || (currentChar == '\r')) {
		    // skip all whitespace characters
		}
		else if(currentChar == '!') {
		    expressionList.add(String.valueOf(currentChar));
		}
		else if(currentChar == '(') {
		    expressionList.add(String.valueOf(currentChar));
		}
		else if(currentChar == ')') {
		    expressionList.add(String.valueOf(currentChar));
		}
		else if((currentChar == '&') && (expression.charAt(i + 1) == '&')) {
		    // add the && to the expression list and skip the next character
		    expressionList.add("&&");
		    i++;
		}
		else if((currentChar == '|') && (expression.charAt(i + 1) == '|')) {
		    // add the || to the expression list and skip the next character
		    expressionList.add("||");
		    i++;
		}
		else if((currentChar == '-') && (expression.charAt(i + 1) == '>')) {
		    // add the -> to the expression list and skip the next character
		    expressionList.add("->");
		    i++;
		}
		else {
		    log.debug("Found the start of a name.");
		    // this is the start of a predicate
		    int nameStart = i;
		    int nameEnd = expression.indexOf("(", nameStart);
		    if(nameEnd == -1) {
			log.error("The expression has a name that doesn't close it's parameter list." +
				  "  The name starts at location " + nameStart + ".");
		    }

		    String predicateName = expression.substring(nameStart, nameEnd).trim();
		    Predicate predicate = (Predicate)predicateNameMap.get(predicateName);
		    if(predicate == null) {
			log.error("The expression has a name that doesn't map to a predicate." +
				  "  The name is " + predicateName + ".");
		    }
		    expressionList.add(predicate);

		    // skip the pointer to the end of the predicate parameters.
		    int end = expression.indexOf(")", nameEnd);
		    i = end ;
		}
	    }
	}
	
	setExpressionList(expressionList);
	
    }
    /**
     * Set the predicates to use in the expression builder.  These predicates
     * will be used to generate the combo box listing of predicates and will
     * be returned to the caller of getExpressionList().
     *
     * @param predicateSet java.util.Set
     */
    public void setPredicateSet(Set predicateSet) {

	if(predicateNameMap == null) {
	    log.debug("creating a new predicateNameMap.");
	    predicateNameMap = new HashMap();
	}
	else {
	    log.debug("clearing the predicateNameMap.");
	    predicateNameMap.clear();
	}

	if(sortedPredicateSet == null) {
	    log.debug("creating a new sortedPredicateSet.");
	    sortedPredicateSet = new TreeSet();
	}
	else {
	    log.debug("clearing the sortedPredicateSet.");
	    sortedPredicateSet.clear();
	}

	if((predicateSet != null) && (predicateSet.size() > 0)) {
	    Iterator psi = predicateSet.iterator();
	    while(psi.hasNext()) {
		Predicate p = (Predicate)psi.next();
		log.debug("Adding predicate " + p.getName().toString() + " to the expression builder.");
		sortedPredicateSet.add(p);
		predicateNameMap.put(p.getName().toString(), p);
	    }
	}

	// with new predicates comes new combo boxes
	if(currentRowCount == 0) {
	    generateExpressionComboBoxes(INITIAL_ROWS);
	}
	else {
	    generateExpressionComboBoxes(INITIAL_ROWS);
	}
    }

    /**
     * Register a CompletionListener that is requesting notification
     * of the completion of the expression configuration.
     *
     * @param CompletionListener completionListener The object to signal when configuration
     *        is complete.
     *
     * @post The given completionListener will in the completionListenerSet.
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
     * Remove all listeners that have been registered.
     *
     * @post The completionListenerSet is empty (or null).
     */
    public void clearCompletionListeners() {
	if(completionListenerSet != null) {
	    completionListenerSet.clear();
	}
    }

    /**
     * Remove the given listener that has been registered.
     *
     * @param CompletionListener completionListener The object that should be removed from
     *        the set of listeners.
     * @post The given CompletionListener is not in the completionListenerSet.
     */
    public void removeCompletionListener(CompletionListener completionListener) {
	if(completionListenerSet != null) {
	    completionListenerSet.remove(completionListener);
	}
    }

    /**
     * When the ExpressionBuilder has completed it's tasks, it will call the fireCompletion
     * method which will signal all CompletionListeners that the configuration has
     * completed.
     *
     * @post All registered CompletionListeners will be notified of completion.
     */
    protected void fireCompletion() {
	if(completionListenerSet != null) {
	    Iterator i = completionListenerSet.iterator();
	    while(i.hasNext()) {
		CompletionListener cl = (CompletionListener)i.next();
		cl.complete(this);
	    }
	}
    }

    /**
     * Check to see if the configuration was cancelled after
     * the completion of configuration and therefore all the
     * state information is irrelevant.
     *
     * @return boolean True if configuration was canceled, False otherwise.
     */
    public boolean isCancelled() {
	return(canceled);
    }
}
