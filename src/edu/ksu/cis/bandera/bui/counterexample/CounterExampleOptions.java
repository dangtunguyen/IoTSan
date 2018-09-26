package edu.ksu.cis.bandera.bui.counterexample;

import edu.ksu.cis.bandera.bui.*;
/**
 * Show an dialog and allow users to define the behavior of the counter example display
 * Creation date: (2001/10/31 AM 02:58:49)
 * @author: Yu Chen
 */
public class CounterExampleOptions extends javax.swing.JFrame {
	private javax.swing.JPanel ivjButtonsPanel = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JButton ivjOkButton = null;
	private javax.swing.JPanel ivjOptionsPanel = null;
	private javax.swing.JCheckBox ivjBreakpointCheckBox = null;
	private javax.swing.JCheckBox ivjConditionCheckBox = null;
	private javax.swing.JPanel ivjConditionPanel = null;
	private javax.swing.JTextField ivjConditionTextField = null;
	private javax.swing.JLabel ivjJLabel1 = null;
	private boolean useStepping = true;
	private boolean enableCodeBreakpoints = true;
	private boolean enableConditionalBreakpoints = false;
	private String conditionString;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JButton ivjApplyButton = null;
	TraceManager traceManager = null;
	ConditionalBreakpoint cb = null;
	private javax.swing.JLabel ivjValidityLabel = null;
	private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
	private javax.swing.JButton ivjAddButton = null;
	private javax.swing.JLabel ivjBreakpointsLabel = null;
	private javax.swing.JPanel ivjBreakpointsPanel = null;
	private javax.swing.JScrollPane ivjBreakpointsScrollPanel = null;
	private javax.swing.JButton ivjClearButton = null;
	private javax.swing.JList ivjBreakpointsList = null;
	private javax.swing.DefaultListModel ivjBreakpointsListModel = null;

class IvjEventHandler implements java.awt.event.ActionListener, javax.swing.event.ListSelectionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == CounterExampleOptions.this.getBreakpointCheckBox()) 
				connEtoC2();
			if (e.getSource() == CounterExampleOptions.this.getConditionCheckBox()) 
				connEtoC3();
			if (e.getSource() == CounterExampleOptions.this.getApplyButton()) 
				connEtoC4(e);
			if (e.getSource() == CounterExampleOptions.this.getOkButton()) 
				connEtoC5(e);
			if (e.getSource() == CounterExampleOptions.this.getAddButton()) 
				connEtoC6(e);
			if (e.getSource() == CounterExampleOptions.this.getClearButton()) 
				connEtoC7(e);
		};
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			if (e.getSource() == CounterExampleOptions.this.getBreakpointsList()) 
				connEtoC8();
		};
	};
public CounterExampleOptions() {
	super();
	initialize();
}
/**
 * CounterExampleOptions constructor comment.
 */
public CounterExampleOptions(TraceManager tm) {
	super();
	traceManager = tm;
	initialize();
}
/**
 * CounterExampleOptions constructor comment.
 * @param title java.lang.String
 */
public CounterExampleOptions(String title, TraceManager tm) {
	super(title);
	traceManager = tm;
}
/**
 * Insert the method's description here.
 * Creation date: (2001/11/7 AM 02:30:46)
 */
public void addBreakpoint(Object o) {
	getBreakpointsListModel().addElement(o);
}
/**
 * Bring BUI to the front
 */
public void addButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	BUI.bui.toFront();
	BUI.bui.requestFocus();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (2001/12/11 PM 10:23:15)
 * @param s java.lang.String
 */
public void appendVariableName(String s) {
	getConditionTextField().setText(getConditionTextField().getText() + s);
}
/**
 * Validate the expression for conditional breakpoint
 */
public void applyButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	conditionString = new String(getConditionTextField().getText());
	// Check the validity of the condition string
	if (enableConditionalBreakpoints) {
		cb = new ConditionalBreakpoint(conditionString, traceManager);
		getValidityLabel().setText("Validity: " + cb.validate());
	}
	
	return;
}
/**
 * Enable or disable code breakpoints
 */
public void breakpointCheckBox_ActionEvents() {
	enableCodeBreakpoints = getBreakpointCheckBox().isSelected();
	return;
}
/**
 * Comment
 */
public void breakpointsList_ListSelectionEvents() {
	if (getBreakpointsList().getSelectedIndex() < 0)
		getClearButton().setEnabled(false);
	else
		getClearButton().setEnabled(true);
		
	return;
}
/**
 * Remove the breakpoint specified
 * Creation date: (2001/11/7 PM 09:09:17)
 * @param o java.lang.Object
 */
public void clearBreakpoint(Object o) {
	getBreakpointsListModel().removeElement(o);
}
/**
 * Remove the selected code break point
 */
public void clearButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	// Grab selected item
	Object itemToBeRemoved = getBreakpointsList().getSelectedValue();

	// Remove item from model
	getBreakpointsListModel().removeElement(itemToBeRemoved);

	// Refresh list from model
	getBreakpointsList().setModel(getBreakpointsListModel());

	getClearButton().setEnabled(false);
	return;
}
/**
 * Enable or disable the conditional breakpoint
 */
public void conditionCheckBox_ActionEvents() {
	enableConditionalBreakpoints = getConditionCheckBox().isSelected();
	if (getConditionCheckBox().isSelected()) {
		getConditionTextField().setEnabled(true);
		getConditionTextField().setBackground(java.awt.Color.white);
	}
	else {
		getConditionTextField().setEnabled(false);
		getConditionTextField().setBackground(java.awt.Color.lightGray);
	}
	return;
}
/**
 * connEtoC2:  (BreakpointCheckBox.action. --> CounterExampleOptions.breakpointCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.breakpointCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (ConditionCheckBox.action. --> CounterExampleOptions.conditionCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3() {
	try {
		// user code begin {1}
		// user code end
		this.conditionCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (ApplyButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExampleOptions.applyButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.applyButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExampleOptions.okButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.okButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (AddButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExampleOptions.addButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.addButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (ClearButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExampleOptions.clearButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.clearButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (BreakpointsList.listSelection. --> CounterExampleOptions.breakpointsList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8() {
	try {
		// user code begin {1}
		// user code end
		this.breakpointsList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connPtoP1SetTarget:  (BreakpointsListModel.this <--> BreakpointsList.model)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP1SetTarget() {
	/* Set the target from the source */
	try {
		getBreakpointsList().setModel(getBreakpointsListModel());
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Tell if the object specified exists in the breakpoints list
 * Creation date: (2001/11/7 PM 09:03:12)
 * @return boolean
 * @param o java.lang.Object
 */
public boolean containsBreakpoint(Object o) {
	if (getBreakpointsListModel().contains(o))
		return true;
	else
		return false;
}
/**
 * Insert the method's description here.
 * Creation date: (2001/12/12 AM 12:04:12)
 */
public void enableCodeBreakpoints() {
	enableCodeBreakpoints = true;
	getBreakpointCheckBox().setSelected(true);
	breakpointCheckBox_ActionEvents();
}
/**
 * Insert the method's description here.
 * Creation date: (2001/12/12 AM 12:04:12)
 */
public void enableConditionalBreakpoints() {
	enableConditionalBreakpoints = true;
	getConditionCheckBox().setSelected(true);
	conditionCheckBox_ActionEvents();
}
/**
 * Return the AddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddButton() {
	if (ivjAddButton == null) {
		try {
			ivjAddButton = new javax.swing.JButton();
			ivjAddButton.setName("AddButton");
			ivjAddButton.setText("Add ...");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddButton;
}
/**
 * Return the AppyButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getApplyButton() {
	if (ivjApplyButton == null) {
		try {
			ivjApplyButton = new javax.swing.JButton();
			ivjApplyButton.setName("ApplyButton");
			ivjApplyButton.setMnemonic('a');
			ivjApplyButton.setText("Apply");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjApplyButton;
}
/**
 * Return the BreakpointCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getBreakpointCheckBox() {
	if (ivjBreakpointCheckBox == null) {
		try {
			ivjBreakpointCheckBox = new javax.swing.JCheckBox();
			ivjBreakpointCheckBox.setName("BreakpointCheckBox");
			ivjBreakpointCheckBox.setText("  Use Code Breakpoints");
			ivjBreakpointCheckBox.setMaximumSize(new java.awt.Dimension(0, 0));
			ivjBreakpointCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
			ivjBreakpointCheckBox.setSelected(true);
			ivjBreakpointCheckBox.setPreferredSize(new java.awt.Dimension(172, 40));
			ivjBreakpointCheckBox.setMinimumSize(new java.awt.Dimension(0, 0));
			ivjBreakpointCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBreakpointCheckBox;
}
/**
 * Return the BreakpointsLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getBreakpointsLabel() {
	if (ivjBreakpointsLabel == null) {
		try {
			ivjBreakpointsLabel = new javax.swing.JLabel();
			ivjBreakpointsLabel.setName("BreakpointsLabel");
			ivjBreakpointsLabel.setText("List of Breakpoints");
			ivjBreakpointsLabel.setForeground(new java.awt.Color(0,0,0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBreakpointsLabel;
}
/**
 * Return the BreakpointsList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getBreakpointsList() {
	if (ivjBreakpointsList == null) {
		try {
			ivjBreakpointsList = new javax.swing.JList();
			ivjBreakpointsList.setName("BreakpointsList");
			ivjBreakpointsList.setBounds(0, 0, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBreakpointsList;
}
/**
 * Return the BreakpointsListModel property value.
 * @return javax.swing.DefaultListModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.DefaultListModel getBreakpointsListModel() {
	if (ivjBreakpointsListModel == null) {
		try {
			ivjBreakpointsListModel = new javax.swing.DefaultListModel();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBreakpointsListModel;
}
/**
 * Return the Page property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBreakpointsPanel() {
	if (ivjBreakpointsPanel == null) {
		try {
			ivjBreakpointsPanel = new javax.swing.JPanel();
			ivjBreakpointsPanel.setName("BreakpointsPanel");
			ivjBreakpointsPanel.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsBreakpointsLabel = new java.awt.GridBagConstraints();
			constraintsBreakpointsLabel.gridx = 0; constraintsBreakpointsLabel.gridy = 0;
			constraintsBreakpointsLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsBreakpointsLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBreakpointsPanel().add(getBreakpointsLabel(), constraintsBreakpointsLabel);

			java.awt.GridBagConstraints constraintsBreakpointsScrollPanel = new java.awt.GridBagConstraints();
			constraintsBreakpointsScrollPanel.gridx = 0; constraintsBreakpointsScrollPanel.gridy = 1;
constraintsBreakpointsScrollPanel.gridheight = 3;
			constraintsBreakpointsScrollPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsBreakpointsScrollPanel.weightx = 1.0;
			constraintsBreakpointsScrollPanel.weighty = 1.0;
			constraintsBreakpointsScrollPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBreakpointsPanel().add(getBreakpointsScrollPanel(), constraintsBreakpointsScrollPanel);

			java.awt.GridBagConstraints constraintsAddButton = new java.awt.GridBagConstraints();
			constraintsAddButton.gridx = 1; constraintsAddButton.gridy = 1;
			constraintsAddButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBreakpointsPanel().add(getAddButton(), constraintsAddButton);

			java.awt.GridBagConstraints constraintsClearButton = new java.awt.GridBagConstraints();
			constraintsClearButton.gridx = 1; constraintsClearButton.gridy = 2;
			constraintsClearButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBreakpointsPanel().add(getClearButton(), constraintsClearButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBreakpointsPanel;
}
/**
 * Return the BreakpointsScrollPanel property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getBreakpointsScrollPanel() {
	if (ivjBreakpointsScrollPanel == null) {
		try {
			ivjBreakpointsScrollPanel = new javax.swing.JScrollPane();
			ivjBreakpointsScrollPanel.setName("BreakpointsScrollPanel");
			getBreakpointsScrollPanel().setViewportView(getBreakpointsList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBreakpointsScrollPanel;
}
/**
 * Return the ButtonsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getButtonsPanel() {
	if (ivjButtonsPanel == null) {
		try {
			ivjButtonsPanel = new javax.swing.JPanel();
			ivjButtonsPanel.setName("ButtonsPanel");
			ivjButtonsPanel.setPreferredSize(new java.awt.Dimension(400, 37));
			ivjButtonsPanel.setLayout(new java.awt.FlowLayout());
			ivjButtonsPanel.setBackground(new java.awt.Color(220,180,120));
			ivjButtonsPanel.setMinimumSize(new java.awt.Dimension(400, 37));
			getButtonsPanel().add(getOkButton(), getOkButton().getName());
			getButtonsPanel().add(getApplyButton(), getApplyButton().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButtonsPanel;
}
/**
 * Return the ClearButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClearButton() {
	if (ivjClearButton == null) {
		try {
			ivjClearButton = new javax.swing.JButton();
			ivjClearButton.setName("ClearButton");
			ivjClearButton.setText("Clear");
			ivjClearButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClearButton;
}
/**
 * Return the ConditionCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getConditionCheckBox() {
	if (ivjConditionCheckBox == null) {
		try {
			ivjConditionCheckBox = new javax.swing.JCheckBox();
			ivjConditionCheckBox.setName("ConditionCheckBox");
			ivjConditionCheckBox.setText("  Use Conditional Breakpoints");
			ivjConditionCheckBox.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
			ivjConditionCheckBox.setBounds(16, 15, 185, 24);
			ivjConditionCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjConditionCheckBox;
}
/**
 * Return the ConditionPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getConditionPanel() {
	if (ivjConditionPanel == null) {
		try {
			ivjConditionPanel = new javax.swing.JPanel();
			ivjConditionPanel.setName("ConditionPanel");
			ivjConditionPanel.setLayout(null);
			ivjConditionPanel.setMaximumSize(new java.awt.Dimension(0, 0));
			ivjConditionPanel.setPreferredSize(new java.awt.Dimension(344, 123));
			ivjConditionPanel.setMinimumSize(new java.awt.Dimension(400, 140));
			ivjConditionPanel.setEnabled(true);
			getConditionPanel().add(getConditionCheckBox(), getConditionCheckBox().getName());
			getConditionPanel().add(getConditionTextField(), getConditionTextField().getName());
			getConditionPanel().add(getJLabel1(), getJLabel1().getName());
			getConditionPanel().add(getValidityLabel(), getValidityLabel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjConditionPanel;
}
/**
 * Return the ConditionTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getConditionTextField() {
	if (ivjConditionTextField == null) {
		try {
			ivjConditionTextField = new javax.swing.JTextField();
			ivjConditionTextField.setName("ConditionTextField");
			ivjConditionTextField.setBounds(16, 69, 223, 20);
			ivjConditionTextField.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjConditionTextField;
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
			ivjJFrameContentPane.setPreferredSize(new java.awt.Dimension(0, 0));
			ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
			ivjJFrameContentPane.setMinimumSize(new java.awt.Dimension(0, 0));
			getJFrameContentPane().add(getButtonsPanel(), "South");
			getJFrameContentPane().add(getJTabbedPane1(), "Center");
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
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("Input condition:");
			ivjJLabel1.setBounds(16, 48, 137, 16);
			ivjJLabel1.setForeground(new java.awt.Color(0,0,0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}
/**
 * Return the JTabbedPane1 property value.
 * @return javax.swing.JTabbedPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTabbedPane getJTabbedPane1() {
	if (ivjJTabbedPane1 == null) {
		try {
			ivjJTabbedPane1 = new javax.swing.JTabbedPane();
			ivjJTabbedPane1.setName("JTabbedPane1");
			ivjJTabbedPane1.insertTab(" General Options  ", null, getOptionsPanel(), null, 0);
			ivjJTabbedPane1.insertTab(" Breakpoints View  ", null, getBreakpointsPanel(), null, 1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJTabbedPane1;
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
			ivjOkButton.setMnemonic('o');
			ivjOkButton.setText("OK");
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
 * Return the OptionsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getOptionsPanel() {
	if (ivjOptionsPanel == null) {
		try {
			ivjOptionsPanel = new javax.swing.JPanel();
			ivjOptionsPanel.setName("OptionsPanel");
			ivjOptionsPanel.setPreferredSize(new java.awt.Dimension(344, 163));
			ivjOptionsPanel.setLayout(new java.awt.GridBagLayout());
			ivjOptionsPanel.setMinimumSize(new java.awt.Dimension(192, 94));

			java.awt.GridBagConstraints constraintsBreakpointCheckBox = new java.awt.GridBagConstraints();
			constraintsBreakpointCheckBox.gridx = 1; constraintsBreakpointCheckBox.gridy = 0;
			constraintsBreakpointCheckBox.fill = java.awt.GridBagConstraints.BOTH;
			getOptionsPanel().add(getBreakpointCheckBox(), constraintsBreakpointCheckBox);

			java.awt.GridBagConstraints constraintsConditionPanel = new java.awt.GridBagConstraints();
			constraintsConditionPanel.gridx = 0; constraintsConditionPanel.gridy = 1;
			constraintsConditionPanel.gridwidth = 2;
			constraintsConditionPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsConditionPanel.weightx = 1.0;
			constraintsConditionPanel.weighty = 1.0;
			getOptionsPanel().add(getConditionPanel(), constraintsConditionPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOptionsPanel;
}
/**
 * Return the ValidityLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getValidityLabel() {
	if (ivjValidityLabel == null) {
		try {
			ivjValidityLabel = new javax.swing.JLabel();
			ivjValidityLabel.setName("ValidityLabel");
			ivjValidityLabel.setText("Validity: False");
			ivjValidityLabel.setBounds(16, 98, 373, 16);
			ivjValidityLabel.setForeground(new java.awt.Color(0,0,0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValidityLabel;
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
	getBreakpointCheckBox().addActionListener(ivjEventHandler);
	getConditionCheckBox().addActionListener(ivjEventHandler);
	getApplyButton().addActionListener(ivjEventHandler);
	getOkButton().addActionListener(ivjEventHandler);
	getAddButton().addActionListener(ivjEventHandler);
	getClearButton().addActionListener(ivjEventHandler);
	getBreakpointsList().addListSelectionListener(ivjEventHandler);
	connPtoP1SetTarget();
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("CounterExampleOptions");
		setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		setSize(426, 260);
		setTitle("Counter Example Options");
		setContentPane(getJFrameContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * Insert the method's description here.
 * Creation date: (2001/10/31 PM 10:28:56)
 * @return boolean
 */
public boolean isCodeBreakpointsEnabled() {
	return enableCodeBreakpoints;
}
/**
 * Insert the method's description here.
 * Creation date: (2001/10/31 PM 10:29:20)
 * @return boolean
 */
public boolean isConditionalBreakpointsEnabled() {
	return enableConditionalBreakpoints;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		CounterExampleOptions aCounterExampleOptions;
		aCounterExampleOptions = new CounterExampleOptions();
		aCounterExampleOptions.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aCounterExampleOptions.show();
		java.awt.Insets insets = aCounterExampleOptions.getInsets();
		aCounterExampleOptions.setSize(aCounterExampleOptions.getWidth() + insets.left + insets.right, aCounterExampleOptions.getHeight() + insets.top + insets.bottom);
		aCounterExampleOptions.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
/**
 * Validate the expression for conditional breakpoint and close the window
 */
public void okButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	conditionString = new String(getConditionTextField().getText());
	// Check the validity of the condition string
	if (enableConditionalBreakpoints) {
		cb = new ConditionalBreakpoint(conditionString, traceManager);
		getValidityLabel().setText("Validity: " + cb.validate());
		if (cb.isValid()) this.hide();
		return;
	}

	this.hide();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (2001/12/6 AM 12:38:13)
 * @param b boolean
 */
public void setUseStepping(boolean b) {
	useStepping = b;
}
/**
 * Insert the method's description here.
 * Creation date: (2001/12/6 AM 12:36:37)
 * @return boolean
 */
public boolean useStepping() {
	return useStepping;
}
}
