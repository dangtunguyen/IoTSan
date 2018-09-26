package edu.ksu.cis.bandera.bui;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import java.io.*;
import edu.ksu.cis.bandera.annotation.*;
import ca.mcgill.sable.soot.*;
import java.util.*;
import edu.ksu.cis.bandera.specification.pattern.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.specification.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;




/**
 * This class is no longer in use.  It's function has been replaced
 * in the SessionManagerView.  This is kept around just as an artifact.
 */
public class PropertyManager extends JFrame {
	private boolean inNameFocusLost = false;
	private boolean inComboBoxChange = false;
	private JPanel ivjAssertionPanel = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JButton ivjOkButton = null;
	private JPanel ivjPropertyManagerContentPane = null;
	private JTabbedPane ivjPropertyManagerTabbedPane = null;
	private JPanel ivjTLPanel = null;
	private JList ivjAssertionList = null;
	private JScrollPane ivjAssertionScrollPane = null;
	private JList ivjEnabledAssertionList = null;
	private JScrollPane ivjEnabledAssertionScrollPane = null;
	private JList ivjTLList = null;
	private JScrollPane ivjTLScrollPane = null;
	private JButton ivjAddAssertionButton = null;
	private JButton ivjAddEnabledAssertionButton = null;
	private JButton ivjAddTLButton = null;
	private JButton ivjRemoveAssertionButton = null;
	private JButton ivjRemoveEnabledAssertionButton = null;
	private JButton ivjRemoveTLButton = null;
	private JPanel ivjFormulaPanel = null;
	private JPanel ivjEnabledAssertionPanel = null;
	private JLabel ivjPatternNameLabel = null;
	private JLabel ivjPatternScopeLabel = null;
	private JLabel ivjPropositionLabel = null;
	private JTextField ivjPropositionTextField = null;
	private JComboBox ivjPatternNameComboBox = null;
	private JComboBox ivjPatternScopeComboBox = null;
	private JComboBox ivjPropositionComboBox = null;
	private JButton ivjExpandButton = null;
	private JButton ivjShowErrorButton = null;
	private JButton ivjShowMappingButton = null;
	private JButton ivjImportAssertionButton = null;
	private JButton ivjImportAssertionSetButton = null;
	private JList ivjImportList = null;
	private JButton ivjImportPackageButton = null;
	private JPanel ivjImportPanel = null;
	private JButton ivjImportPredicateButton = null;
	private JButton ivjImportPredicateSetButton = null;
	private JScrollPane ivjImportScrollPane = null;
	private JButton ivjImportTypeButton = null;
	private JButton ivjOpenButton = null;
	private JButton ivjRemoveImportButton = null;
	private JButton ivjSaveButton = null;
	private JPanel ivjPropertyPanel = null;
	private JButton ivjSaveAsButton = null;
	private JButton ivjActivateAssertionButton = null;
	private JButton ivjActivateTLButton = null;
	private JLabel ivjAssertionNameLabel = null;
	private JTextField ivjAssertionNameTextField = null;
	private JLabel ivjQuantifierLabel = null;
	private JLabel ivjTLNameLabel = null;
	private JTextField ivjTLNameTextField = null;
	private JButton ivjTLDescriptionButton = null;
	private JPanel ivjDescriptionPanel = null;
	private JScrollPane ivjDescriptionScrollPane = null;
	private JTextArea ivjDescriptionTextArea = null;
	private JTextField ivjExpandedPatternTextField = null;
	private JButton ivjAssertionDescriptionButton = null;
	private JButton ivjNewButton = null;
	private JMenuItem ivjAddPredicateMenuItem = null;
	private JPopupMenu ivjPropositionPopupMenu = null;
	private boolean isClean = true; // robbyjo's patch
	private JTextArea ivjQuantificationTextArea = null;
	private JScrollPane ivjQuantificationScrollPane = null;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.KeyListener, java.awt.event.MouseListener, javax.swing.event.ListSelectionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == PropertyManager.this.getOkButton()) 
				connEtoM1(e);
			if (e.getSource() == PropertyManager.this.getImportTypeButton()) 
				connEtoC1();
			if (e.getSource() == PropertyManager.this.getImportPackageButton()) 
				connEtoC2();
			if (e.getSource() == PropertyManager.this.getImportPredicateButton()) 
				connEtoC3();
			if (e.getSource() == PropertyManager.this.getImportPredicateSetButton()) 
				connEtoC4();
			if (e.getSource() == PropertyManager.this.getImportAssertionButton()) 
				connEtoC5();
			if (e.getSource() == PropertyManager.this.getImportAssertionSetButton()) 
				connEtoC6();
			if (e.getSource() == PropertyManager.this.getRemoveImportButton()) 
				connEtoC7();
			if (e.getSource() == PropertyManager.this.getAddAssertionButton()) 
				connEtoC9();
			if (e.getSource() == PropertyManager.this.getRemoveAssertionButton()) 
				connEtoC10();
			if (e.getSource() == PropertyManager.this.getActivateAssertionButton()) 
				connEtoC11();
			if (e.getSource() == PropertyManager.this.getAddEnabledAssertionButton()) 
				connEtoC13();
			if (e.getSource() == PropertyManager.this.getRemoveEnabledAssertionButton()) 
				connEtoC14();
			if (e.getSource() == PropertyManager.this.getAddTLButton()) 
				connEtoC20();
			if (e.getSource() == PropertyManager.this.getRemoveTLButton()) 
				connEtoC21();
			if (e.getSource() == PropertyManager.this.getActivateTLButton()) 
				connEtoC22();
			if (e.getSource() == PropertyManager.this.getPatternNameComboBox()) 
				connEtoC27();
			if (e.getSource() == PropertyManager.this.getPatternScopeComboBox()) 
				connEtoC28();
			if (e.getSource() == PropertyManager.this.getPropositionComboBox()) 
				connEtoC29();
			if (e.getSource() == PropertyManager.this.getExpandButton()) 
				connEtoC32();
			if (e.getSource() == PropertyManager.this.getShowErrorButton()) 
				connEtoC33();
			if (e.getSource() == PropertyManager.this.getShowMappingButton()) 
				connEtoC34();
			if (e.getSource() == PropertyManager.this.getNewButton()) 
				connEtoC35();
			if (e.getSource() == PropertyManager.this.getOpenButton()) 
				connEtoC36();
			if (e.getSource() == PropertyManager.this.getSaveAsButton()) 
				connEtoC37();
			if (e.getSource() == PropertyManager.this.getSaveButton()) 
				connEtoC38();
			if (e.getSource() == PropertyManager.this.getAddPredicateMenuItem()) 
				connEtoC39();
		};
		public void focusGained(java.awt.event.FocusEvent e) {};
		public void focusLost(java.awt.event.FocusEvent e) {
			if (e.getSource() == PropertyManager.this.getAssertionNameTextField()) 
				connEtoC18(e);
			if (e.getSource() == PropertyManager.this.getTLNameTextField()) 
				connEtoC24(e);
			if (e.getSource() == PropertyManager.this.getPropositionTextField()) 
				connEtoC31(e);
			if (e.getSource() == PropertyManager.this.getQuantificationTextArea()) 
				connEtoC41(e);
		};
		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getSource() == PropertyManager.this.getAssertionNameTextField()) 
				connEtoC17();
			if (e.getSource() == PropertyManager.this.getTLNameTextField()) 
				connEtoC23();
			if (e.getSource() == PropertyManager.this.getPropositionTextField()) 
				connEtoC30();
			if (e.getSource() == PropertyManager.this.getQuantificationTextArea()) 
				connEtoC40();
		};
		public void keyReleased(java.awt.event.KeyEvent e) {
			if (e.getSource() == PropertyManager.this.getAssertionNameTextField()) 
				connEtoC17();
			if (e.getSource() == PropertyManager.this.getTLNameTextField()) 
				connEtoC23();
			if (e.getSource() == PropertyManager.this.getPropositionTextField()) 
				connEtoC30();
			if (e.getSource() == PropertyManager.this.getQuantificationTextArea()) 
				connEtoC40();
		};
		public void keyTyped(java.awt.event.KeyEvent e) {
			if (e.getSource() == PropertyManager.this.getAssertionNameTextField()) 
				connEtoC17();
			if (e.getSource() == PropertyManager.this.getTLNameTextField()) 
				connEtoC23();
			if (e.getSource() == PropertyManager.this.getPropositionTextField()) 
				connEtoC30();
			if (e.getSource() == PropertyManager.this.getQuantificationTextArea()) 
				connEtoC40();
		};
		public void mouseClicked(java.awt.event.MouseEvent e) {};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mousePressed(java.awt.event.MouseEvent e) {};
		public void mouseReleased(java.awt.event.MouseEvent e) {
			if (e.getSource() == PropertyManager.this.getPropositionTextField()) 
				connEtoC16(e);
		};
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			if (e.getSource() == PropertyManager.this.getImportList()) 
				connEtoC8();
			if (e.getSource() == PropertyManager.this.getAssertionList()) 
				connEtoC12();
			if (e.getSource() == PropertyManager.this.getEnabledAssertionList()) 
				connEtoC15();
			if (e.getSource() == PropertyManager.this.getTLList()) 
				connEtoC19();
		};
	};
/**
 * PropertyManager constructor comment.
 */
public PropertyManager() {
	super();
	initialize();
}
/**
 * PropertyManager constructor comment.
 * @param title java.lang.String
 */
public PropertyManager(String title) {
	super(title);
}
/**
 * Comment
 */
public void activateAssertionButton_ActionEvents() {
	Object[] objects = getAssertionList().getSelectedValues();
	int[] indices = getAssertionList().getSelectedIndices();
	for (int i = 0; i < objects.length; i++) {
	    //BUI.property.activateOrDeactivateAssertionProperty((AssertionProperty) objects[i]);
	}
	updateAssertionList();
	updateEnabledAssertionList();
	//getAddEnabledAssertionButton().setEnabled(BUI.property.getActivatedAssertionProperties().size() == 1);
	getAssertionList().setSelectedIndices(indices);
	//BUI.manager.updateProperty();
}
/**
 * Comment
 */
public void activateTLButton_ActionEvents() {
	TemporalLogicProperty tlp = (TemporalLogicProperty) getTLList().getSelectedValue();
	//BUI.property.activateOrDeactivateTemporalLogicProperty(tlp);
	
	updateTLList();
	getTLList().setSelectedValue(tlp, true);
	//tlp = BUI.property.getActivatedTemporalLogicProperty();
	if (tlp == null) {
		setTLEnabledAll(false);
	}
	String s = tlp.getQuantifier();
	if (s == null) {
		s = "";
		tlp.setQuantifier(s);
	}
	getQuantificationTextArea().setText(s);

	Hashtable table = PatternSaverLoader.getPatternTable();

	inComboBoxChange = true;
	try {
		getPatternNameComboBox().removeAllItems();
	} catch (Exception e) {}
	
	TreeSet ts = new TreeSet();
	for (Enumeration e = table.keys(); e.hasMoreElements();) {
		ts.add(e.nextElement());
	}

	for (Iterator i = ts.iterator(); i.hasNext();) {
		getPatternNameComboBox().addItem(i.next());
	}
	inComboBoxChange = false;

	s = tlp.getPatternName();

	if (s == null) getPatternNameComboBox().setSelectedIndex(0);
	else getPatternNameComboBox().setSelectedItem(s);

	patternNameComboBox_ActionEvents();

	s = (String) getPatternNameComboBox().getSelectedItem();

	//BUI.property.getActivatedTemporalLogicProperty().setPatternName(s);
	
	s = (String) getPatternScopeComboBox().getSelectedItem();

	//BUI.property.getActivatedTemporalLogicProperty().setPatternScope(s);
	
	Pattern p = (Pattern) ((Hashtable) PatternSaverLoader.getPatternTable().get(getPatternNameComboBox().getSelectedItem())).get(s);

	Hashtable parameters = new Hashtable();
	for (Iterator i = p.getParameters().iterator(); i.hasNext();) {
		Object parm = i.next();
		parameters.put(parm, parm);
	}
	String template = p.expandFormat(parameters);
	getExpandedPatternTextField().setText(template);
	getExpandedPatternTextField().setToolTipText(template);
	setTLEnabledAll(true);
	//BUI.manager.updateProperty();
}
/**
 * Comment
 */
public void addAssertionButton_ActionEvents() {
	for (int i = 1; i <= 32000; i++) {
		if (!BUI.property.hasAssertionProperty("Untitled" + i)) {
			AssertionProperty ap = new AssertionProperty("Untitled" + i);
			//BUI.property.addAssertionProperty(ap);
			updateAssertionList();
			getAssertionList().setSelectedValue(ap, true);
			isClean = false;
			return;
		}
	}
}
/**
 * Comment
 */
public void addEnabledAssertionButton_ActionEvents() {
	AssertionProperty ap = (AssertionProperty) BUI.property.getActivatedAssertionProperties().iterator().next();
	Vector v = new Vector();
	for (Enumeration e = AssertionSet.getAssertionSetTable().elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((AssertionSet) e.nextElement()).getAssertionTable().elements(); e2.hasMoreElements();) {
			v.add(e2.nextElement());
		}
	}
	int size = v.size();
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = ((Assertion) e.nextElement()).getName();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose assertion", "Enable Assertion", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			try {
				Assertion a = AssertionSet.getAssertion((Name) selectedValue);
				ap.addAssertion(a.getName());
				updateEnabledAssertionList();
				getEnabledAssertionList().setSelectedValue(a, true);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no assertions to choose from", "Enable Assertion", JOptionPane.INFORMATION_MESSAGE); 
	}
}
/**
 * Comment
 */
public void addPredicateMenuItem_ActionEvents() {
	Vector v = new Vector();
	for (Enumeration e = PredicateSet.getPredicateSetTable().elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((PredicateSet) e.nextElement()).getPredicateTable().elements(); e2.hasMoreElements();) {
			v.add(e2.nextElement());
		}
	}
	int size = v.size();
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = ((Predicate) e.nextElement()).getName();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose predicate", "Add Predicate", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			try {
				Predicate p = PredicateSet.getPredicate((Name) selectedValue);
				//BUI.propertyManager.getPropositionTextField().setText(BUI.propertyManager.getPropositionTextField().getText() + p.getName());
				//BUI.propertyManager.propositionTextField_KeyEvents();
			} catch (Exception e) {
			}
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no predicates to choose from", "Add Predicate", JOptionPane.INFORMATION_MESSAGE); 
	}
}
/**
 * Comment
 */
public void addTLButton_ActionEvents() {
	for (int i = 1; i <= 32000; i++) {
		if (!BUI.property.hasTemporalLogicProperty("Untitled" + i)) {
			TemporalLogicProperty tlp = new TemporalLogicProperty("Untitled" + i);
			BUI.property.addTemporalLogicProperty(tlp);
			updateTLList();
			getTLList().setSelectedValue(tlp, true);
			getActivateTLButton().setEnabled(true);
			isClean = false;
			return;
		}
	}
}
/**
 * Comment
 */
public void assertionList_ListSelectionEvents() {
	getRemoveAssertionButton().setEnabled(true);
	getActivateAssertionButton().setEnabled(true);
	boolean f = (BUI.property.getActivatedAssertionProperties().size() == 1) && (getAssertionList().getSelectedValues().length == 1);
	if (!f) {
		getAssertionNameTextField().setText("");
		getAssertionNameTextField().setEditable(false);
	} else {
		AssertionProperty ap = (AssertionProperty) getAssertionList().getSelectedValue();
		getAssertionNameTextField().setText(ap.getName());
		getAssertionNameTextField().setEditable(BUI.property.isActivated(ap));
	}
}
/**
 * Comment
 */
public void assertionNameTextField_FocusLost() {
	String s = getAssertionNameTextField().getText().trim();
	if (!inNameFocusLost && !Util.isValidId(s)) {
		inNameFocusLost = true;
		do {
			s = JOptionPane.showInputDialog("Please input a valid assertion property name");
		} while ((s == null) || !Util.isValidId(s));
		getAssertionNameTextField().setText(s);
		AssertionProperty ap = (AssertionProperty) BUI.property.getActivatedAssertionProperties().iterator().next();
		ap.setName(s);
		getAssertionList().repaint();
		inNameFocusLost = false;
	}
}
/**
 * Comment
 */
public void assertionNameTextField_KeyEvents() {
	AssertionProperty ap = (AssertionProperty) BUI.property.getActivatedAssertionProperties().iterator().next();
	ap.setName(getAssertionNameTextField().getText().trim());
	getAssertionList().repaint();
}
/**
 * connEtoC1:  (ImportTypeButton.action. --> PropertyManager.importTypeButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		// user code end
		this.importTypeButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (RemoveAssertionButton.action. --> PropertyManager.removeAssertionButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC10() {
	try {
		// user code begin {1}
		// user code end
		this.removeAssertionButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (ActivateAssertionButton.action. --> PropertyManager.activateAssertionButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC11() {
	try {
		// user code begin {1}
		// user code end
		this.activateAssertionButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (AssertionList.listSelection. --> PropertyManager.assertionList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12() {
	try {
		// user code begin {1}
		// user code end
		this.assertionList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (AddEnabledAssertionButton.action. --> PropertyManager.addEnabledAssertionButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13() {
	try {
		// user code begin {1}
		// user code end
		this.addEnabledAssertionButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (RemoveEnabledAssertionButton.action. --> PropertyManager.removeEnabledAssertionButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14() {
	try {
		// user code begin {1}
		// user code end
		this.removeEnabledAssertionButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC15:  (EnabledAssertionList.listSelection. --> PropertyManager.enabledAssertionList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC15() {
	try {
		// user code begin {1}
		// user code end
		this.enabledAssertionList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC16:  (GotoAssertionDefinitionButton.action. --> PropertyManager.gotoAssertionDefinitionButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC16() {
	try {
		// user code begin {1}
		// user code end
		this.gotoAssertionDefinitionButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC16:  (PropositionTextField.mouse.mouseReleased(java.awt.event.MouseEvent) --> PropertyManager.propositionTextField_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC16(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.propositionTextField_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC17:  (AssertionNameTextField.key. --> PropertyManager.assertionNameTextField_KeyEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC17() {
	try {
		// user code begin {1}
		// user code end
		this.assertionNameTextField_KeyEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC18:  (AssertionNameTextField.focus.focusLost(java.awt.event.FocusEvent) --> PropertyManager.assertionNameTextField_FocusLost()V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC18(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.assertionNameTextField_FocusLost();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC19:  (TLList.listSelection. --> PropertyManager.tLList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC19() {
	try {
		// user code begin {1}
		// user code end
		this.tLList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (ImportPackageButton.action. --> PropertyManager.importPackageButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.importPackageButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC20:  (AddTLButton.action. --> PropertyManager.addTLButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC20() {
	try {
		// user code begin {1}
		// user code end
		this.addTLButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC21:  (RemoveTLButton.action. --> PropertyManager.removeTLButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC21() {
	try {
		// user code begin {1}
		// user code end
		this.removeTLButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC22:  (ActivateTLButton.action. --> PropertyManager.activateTLButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC22() {
	try {
		// user code begin {1}
		// user code end
		this.activateTLButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC23:  (TLNameTextField.key. --> PropertyManager.tLNameTextField_KeyEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC23() {
	try {
		// user code begin {1}
		// user code end
		this.tLNameTextField_KeyEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC24:  (TLNameTextField.focus.focusLost(java.awt.event.FocusEvent) --> PropertyManager.tLNameTextField_FocusLost()V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC24(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.tLNameTextField_FocusLost();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC27:  (PatternNameComboBox.action. --> PropertyManager.patternNameComboBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC27() {
	try {
		// user code begin {1}
		// user code end
		this.patternNameComboBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC28:  (PatternScopeComboBox.action. --> PropertyManager.patternScopeComboBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC28() {
	try {
		// user code begin {1}
		// user code end
		this.patternScopeComboBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC29:  (PropositionComboBox.action. --> PropertyManager.propositionComboBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC29() {
	try {
		// user code begin {1}
		// user code end
		this.propositionComboBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (ImportPredicateButton.action. --> PropertyManager.importPredicateButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3() {
	try {
		// user code begin {1}
		// user code end
		this.importPredicateButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC30:  (PropositionTextField.key. --> PropertyManager.propositionTextField_KeyEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC30() {
	try {
		// user code begin {1}
		// user code end
		this.propositionTextField_KeyEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC31:  (PropositionTextField.focus.focusLost(java.awt.event.FocusEvent) --> PropertyManager.propositionTextField_FocusLost()V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC31(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.propositionTextField_FocusLost();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC32:  (ExpandButton.action. --> PropertyManager.expandButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC32() {
	try {
		// user code begin {1}
		// user code end
		this.expandButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC33:  (ShowErrorButton.action. --> PropertyManager.showErrorButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC33() {
	try {
		// user code begin {1}
		// user code end
		this.showErrorButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC34:  (ShowMappingButton.action. --> PropertyManager.showMappingButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC34() {
	try {
		// user code begin {1}
		// user code end
		this.showMappingButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC35:  (NewButton.action. --> PropertyManager.newButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC35() {
	try {
		// user code begin {1}
		// user code end
		this.newButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC36:  (OpenButton.action. --> PropertyManager.openButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC36() {
	try {
		// user code begin {1}
		// user code end
		this.openButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC37:  (SaveAsButton.action. --> PropertyManager.saveAsButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC37() {
	try {
		// user code begin {1}
		// user code end
		this.saveAsButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC38:  (SaveButton.action. --> PropertyManager.saveButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC38() {
	try {
		// user code begin {1}
		// user code end
		this.saveButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC39:  (AddPredicateMenuItem.action. --> PropertyManager.addPredicateMenuItem_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC39() {
	try {
		// user code begin {1}
		// user code end
		this.addPredicateMenuItem_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (ImportPredicateSetButton.action. --> PropertyManager.importPredicateSetButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		// user code end
		this.importPredicateSetButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC40:  (quantificationTextArea.key. --> PropertyManager.quantificationTextArea_KeyEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC40() {
	try {
		// user code begin {1}
		// user code end
		this.quantificationTextArea_KeyEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC41:  (quantificationTextArea.focus.focusLost(java.awt.event.FocusEvent) --> PropertyManager.quantificationTextArea_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC41(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.quantificationTextArea_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (ImportAssertionButton.action. --> PropertyManager.importAssertionButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5() {
	try {
		// user code begin {1}
		// user code end
		this.importAssertionButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (ImportAssertionSetButton.action. --> PropertyManager.importAssertionSetButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6() {
	try {
		// user code begin {1}
		// user code end
		this.importAssertionSetButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (RemoveImportButton.action. --> PropertyManager.removeImportButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7() {
	try {
		// user code begin {1}
		// user code end
		this.removeImportButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (ImportList.listSelection. --> PropertyManager.importList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8() {
	try {
		// user code begin {1}
		// user code end
		this.importList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (AddAssertionButton.action. --> PropertyManager.addAssertionButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9() {
	try {
		// user code begin {1}
		// user code end
		this.addAssertionButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> PropertyManager.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Comment
 */
public void constraintTextField_MouseReleased() {
	return;
}
/**
 * Comment
 */
public void enabledAssertionList_ListSelectionEvents() {
	boolean f = getEnabledAssertionList().getSelectedValues().length == 1;
	try {
		AssertionSet.getAssertion((Name) getEnabledAssertionList().getSelectedValue());
	} catch (Exception e) {
		f = false;
	}
	getRemoveEnabledAssertionButton().setEnabled(BUI.property.getActivatedAssertionProperties().size() == 1);
}
/**
 * Comment
 */
public void expandButton_ActionEvents() {
	TemporalLogicProperty tlp = BUI.property.getActivatedTemporalLogicProperty();
	if (tlp == null) return;
	getExpandedPatternTextField().setText(tlp.expand());
	tlp.validate(BUI.property.getImportedType(), BUI.property.getImportedPackage(),
			BUI.property.getImportedAssertion(), BUI.property.getImportedAssertionSet(),
			BUI.property.getImportedPredicate(), BUI.property.getImportedPredicateSet());
	if (tlp.getExceptions().size() > 0) {
		getShowErrorButton().setEnabled(true);
		getShowMappingButton().setEnabled(false);
		showErrorButton_ActionEvents();
	} else {
		getShowErrorButton().setEnabled(false);
		//getShowMappingButton().setEnabled(true);
	}
}
/**
 * Return the SelectAssertionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getActivateAssertionButton() {
	if (ivjActivateAssertionButton == null) {
		try {
			ivjActivateAssertionButton = new javax.swing.JButton();
			ivjActivateAssertionButton.setName("ActivateAssertionButton");
			ivjActivateAssertionButton.setMnemonic('a');
			ivjActivateAssertionButton.setText("Activate / Deactivate");
			ivjActivateAssertionButton.setBackground(new java.awt.Color(204,204,255));
			ivjActivateAssertionButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjActivateAssertionButton;
}
/**
 * Return the SelectTLButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getActivateTLButton() {
	if (ivjActivateTLButton == null) {
		try {
			ivjActivateTLButton = new javax.swing.JButton();
			ivjActivateTLButton.setName("ActivateTLButton");
			ivjActivateTLButton.setMnemonic('a');
			ivjActivateTLButton.setText("Activate / Deactivate");
			ivjActivateTLButton.setBackground(new java.awt.Color(204,204,255));
			ivjActivateTLButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjActivateTLButton;
}
/**
 * Return the AddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddAssertionButton() {
	if (ivjAddAssertionButton == null) {
		try {
			ivjAddAssertionButton = new javax.swing.JButton();
			ivjAddAssertionButton.setName("AddAssertionButton");
			ivjAddAssertionButton.setText("New");
			ivjAddAssertionButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddAssertionButton;
}
/**
 * Return the AddEnabledButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddEnabledAssertionButton() {
	if (ivjAddEnabledAssertionButton == null) {
		try {
			ivjAddEnabledAssertionButton = new javax.swing.JButton();
			ivjAddEnabledAssertionButton.setName("AddEnabledAssertionButton");
			ivjAddEnabledAssertionButton.setText("New");
			ivjAddEnabledAssertionButton.setBackground(new java.awt.Color(204,204,255));
			ivjAddEnabledAssertionButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddEnabledAssertionButton;
}
/**
 * Return the AddPredicateMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getAddPredicateMenuItem() {
	if (ivjAddPredicateMenuItem == null) {
		try {
			ivjAddPredicateMenuItem = new javax.swing.JMenuItem();
			ivjAddPredicateMenuItem.setName("AddPredicateMenuItem");
			ivjAddPredicateMenuItem.setText("Add a predicate");
			ivjAddPredicateMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddPredicateMenuItem;
}
/**
 * Return the JButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddTLButton() {
	if (ivjAddTLButton == null) {
		try {
			ivjAddTLButton = new javax.swing.JButton();
			ivjAddTLButton.setName("AddTLButton");
			ivjAddTLButton.setText("New");
			ivjAddTLButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddTLButton;
}
/**
 * Return the AssertionDescriptionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAssertionDescriptionButton() {
	if (ivjAssertionDescriptionButton == null) {
		try {
			ivjAssertionDescriptionButton = new javax.swing.JButton();
			ivjAssertionDescriptionButton.setName("AssertionDescriptionButton");
			ivjAssertionDescriptionButton.setMnemonic('d');
			ivjAssertionDescriptionButton.setText("Description");
			ivjAssertionDescriptionButton.setBackground(new java.awt.Color(204,204,255));
			ivjAssertionDescriptionButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionDescriptionButton;
}
/**
 * Return the AssertionList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getAssertionList() {
	if (ivjAssertionList == null) {
		try {
			ivjAssertionList = new javax.swing.JList();
			ivjAssertionList.setName("AssertionList");
			ivjAssertionList.setBackground(new java.awt.Color(204,204,204));
			ivjAssertionList.setBounds(0, 0, 160, 120);
			ivjAssertionList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			// user code begin {1}
			ivjAssertionList.setCellRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof AssertionProperty) {
						AssertionProperty ap = (AssertionProperty) value;
						setText(ap.getName());
						if (BUI.property.isActivated(ap)) {
							setIcon(IconLibrary.arrow);
						} else {
							setIcon(IconLibrary.earrow);
						}
					}
					return this;
				}
			});
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionList;
}
/**
 * Return the AssertionNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getAssertionNameLabel() {
	if (ivjAssertionNameLabel == null) {
		try {
			ivjAssertionNameLabel = new javax.swing.JLabel();
			ivjAssertionNameLabel.setName("AssertionNameLabel");
			ivjAssertionNameLabel.setDisplayedMnemonic('n');
			ivjAssertionNameLabel.setText("Name:");
			ivjAssertionNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionNameLabel;
}
/**
 * Return the AssertionNameTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getAssertionNameTextField() {
	if (ivjAssertionNameTextField == null) {
		try {
			ivjAssertionNameTextField = new javax.swing.JTextField();
			ivjAssertionNameTextField.setName("AssertionNameTextField");
			ivjAssertionNameTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjAssertionNameTextField.setFocusAccelerator('n');
			ivjAssertionNameTextField.setEnabled(true);
			ivjAssertionNameTextField.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionNameTextField;
}
/**
 * Return the AssertionPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JPanel getAssertionPanel() {
	if (ivjAssertionPanel == null) {
		try {
			ivjAssertionPanel = new javax.swing.JPanel();
			ivjAssertionPanel.setName("AssertionPanel");
			ivjAssertionPanel.setLayout(new java.awt.GridBagLayout());
			ivjAssertionPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsAssertionScrollPane = new java.awt.GridBagConstraints();
			constraintsAssertionScrollPane.gridx = 0; constraintsAssertionScrollPane.gridy = 0;
constraintsAssertionScrollPane.gridheight = 6;
			constraintsAssertionScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsAssertionScrollPane.weightx = 1.0;
			constraintsAssertionScrollPane.weighty = 0.3;
			constraintsAssertionScrollPane.insets = new java.awt.Insets(10, 10, 0, 0);
			getAssertionPanel().add(getAssertionScrollPane(), constraintsAssertionScrollPane);

			java.awt.GridBagConstraints constraintsAddAssertionButton = new java.awt.GridBagConstraints();
			constraintsAddAssertionButton.gridx = 1; constraintsAddAssertionButton.gridy = 1;
			constraintsAddAssertionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAddAssertionButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getAssertionPanel().add(getAddAssertionButton(), constraintsAddAssertionButton);

			java.awt.GridBagConstraints constraintsRemoveAssertionButton = new java.awt.GridBagConstraints();
			constraintsRemoveAssertionButton.gridx = 1; constraintsRemoveAssertionButton.gridy = 2;
			constraintsRemoveAssertionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsRemoveAssertionButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getAssertionPanel().add(getRemoveAssertionButton(), constraintsRemoveAssertionButton);

			java.awt.GridBagConstraints constraintsEnabledAssertionPanel = new java.awt.GridBagConstraints();
			constraintsEnabledAssertionPanel.gridx = 0; constraintsEnabledAssertionPanel.gridy = 6;
			constraintsEnabledAssertionPanel.gridwidth = 2;
			constraintsEnabledAssertionPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsEnabledAssertionPanel.weightx = 1.0;
			constraintsEnabledAssertionPanel.weighty = 0.7;
			constraintsEnabledAssertionPanel.insets = new java.awt.Insets(15, 9, 10, 7);
			getAssertionPanel().add(getEnabledAssertionPanel(), constraintsEnabledAssertionPanel);

			java.awt.GridBagConstraints constraintsActivateAssertionButton = new java.awt.GridBagConstraints();
			constraintsActivateAssertionButton.gridx = 1; constraintsActivateAssertionButton.gridy = 0;
			constraintsActivateAssertionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsActivateAssertionButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getAssertionPanel().add(getActivateAssertionButton(), constraintsActivateAssertionButton);

			java.awt.GridBagConstraints constraintsAssertionNameLabel = new java.awt.GridBagConstraints();
			constraintsAssertionNameLabel.gridx = 1; constraintsAssertionNameLabel.gridy = 3;
			constraintsAssertionNameLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAssertionNameLabel.insets = new java.awt.Insets(15, 10, 0, 8);
			getAssertionPanel().add(getAssertionNameLabel(), constraintsAssertionNameLabel);

			java.awt.GridBagConstraints constraintsAssertionNameTextField = new java.awt.GridBagConstraints();
			constraintsAssertionNameTextField.gridx = 1; constraintsAssertionNameTextField.gridy = 4;
			constraintsAssertionNameTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAssertionNameTextField.insets = new java.awt.Insets(5, 10, 0, 10);
			getAssertionPanel().add(getAssertionNameTextField(), constraintsAssertionNameTextField);

			java.awt.GridBagConstraints constraintsAssertionDescriptionButton = new java.awt.GridBagConstraints();
			constraintsAssertionDescriptionButton.gridx = 1; constraintsAssertionDescriptionButton.gridy = 5;
			constraintsAssertionDescriptionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAssertionDescriptionButton.insets = new java.awt.Insets(0, 10, 0, 10);
			getAssertionPanel().add(getAssertionDescriptionButton(), constraintsAssertionDescriptionButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionPanel;
}
/**
 * Return the AssertionScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getAssertionScrollPane() {
	if (ivjAssertionScrollPane == null) {
		try {
			ivjAssertionScrollPane = new javax.swing.JScrollPane();
			ivjAssertionScrollPane.setName("AssertionScrollPane");
			ivjAssertionScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			getAssertionScrollPane().setViewportView(getAssertionList());
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
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88G36FBD4ACGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E16D3D8FDCD4D57ABFCEC59B95653451C6E5AD15EDD4D4D6346B96DB34D1D1D2512EA5959595959595ED56DAD90D02080A0A0ACA05B0A0A8AAAAAA9AA9AAAA9A96A6A59995DB0C4CA864400C432028E85F671CF31F677E1DE106364F7773FB7D3E3EDE0F43FD1F675FB967B94FBD775CF36FB56797C7CC9CBFA15FEC9EBF51AC7D7BE43C596C59BC5EBC7E2BB39EC3AC655AA119E6453F922019660CF5D970
	1B81E57D2F1AB2734C8BBE4D01632C6B06E436241AB267415F334D7D7B9A5602C2AFF6ACD0D6774FEB2E71ED2778E734B3099FEF4A4B055F69C0B9E02B92288FA84F7CEDDE0104B74171F469EFC663A7184DE557B31E705E82859EA3710D055FDCA80F67BC6E3CC2295C81470CA798FE432F973C4361F7B2776E602E9A541393E1362D8A4D1E46ECA974A3658D97325552AF7C154D9AA2536C59398CFEEB1B8E6C5FE83775AEBF50533B3C3D33E779DEEB374D6E36AE5F5B5B39EEDD135B69323BBDC7F6DA3B2D7B
	6DEE609FB61C79F9DBEF8E5781FE42F1558D8CDBFB0835CD9A9C1B613860867475551D3512AFED2FDCEB36BCF91ED7DFBDB7889F43EF10FD8D8DF9E8FC430DCC7FC53C6D19DC92338718706559BC74A574C674E5CD91630D0363E420B1045FD143705C1B06E41681555E04BEBE3389FDF4F45CE0CEDDF1E1768CAFBEB65FA4FC6B3FC976B139BBFA4207BCBE7EFC00757188A8CB864AG4A0F9E12D98D548C74B10F4B03BFAF015F4DFBBDDB1DDDDD4E6E4DAE5796074D3D53E933BB606FF56B20C4423E3637BB5D
	B633D9445E1D3D15867EA0B92255F147C8546DDBB346CA7804BF197B0A2F607D2F2DDB7D3E2359C1120E30E839EE2E1BD93B9D6AE36D968DB8137B8EC86057887CE69997FDD54BE301737D995A792F584EE19568CB6E6F071A076EBFB5FB1201AFCD77DC1A1D93A3FC3044483E08B6B6F531B6EE843C83A888A89CE884D0D28C36712DBF2F9E280DF7F4FB6C6EEE2BE3F76FDE17556D59525B690038175A7A52BCDFEDEDC4C26E7A0065C43BC42ED6765158DBCEB7EC97D74CF959E32476487D1B5CAE42F61AD75B
	15FF534746F46A432B78D8297B1B68430904C7F33CD9010B3EDD4FF1F65C81E589C0027F21F58C2F3ACD70FFC5FAA62CE7F8328297FA6E61E3119D075C8E63646FC3B2C57FAEF7329C989187798FE894D0BAD06EDDC3B2AB00EA6F42BE4EBE30424FF1542468DFA1FB7B013970EB6F73382DBB3AFBBC566EF67B9E2B3B533A57E16FB1CBBC7187D83B6ED5495D4F47F7BB58784C5E8DD95253696C866D56AEBB84D30F60F96440CC4663367352664EAEBB9868F219D16F1387E63371EA6D71FC6332D9BDF619C314
	BFFF004544A16B9E50888A606F503BD8FBBD46F3EEAB7C5DF79736558FAE06A542F19A350B55B18BFE3F006036BB562D2B6FECE7EED85DC7B0C67B7B06436FB860F75C8D7D87D4F58F0CA1201CF868CB2094207AFBA13F82655C8798D0B2D07D48A11951C01589C3B2A300326E9F12E982CA832AFDGF4G0D85EAFA907482D5A58286B48E287521A11929C0357FG8CE8ACD043BF07E48E859A8354B08AFE01729F867DC0E1C0E301A29E81BB2369DC520263E46CF3FFB2477F79F7066304D154E89173F1233563
	6486DE7FF440F3134096D0A31083A894A88AA881A89DA877D1A887EA84F2G0582C581A5G258365BE8665C00DC08E20D020A8208420F4205C47219C28914881948A948514G148E141B8C65C00DC08E20D02061C009C099C0F9CFC09B82B5BD01730FAD07E2E1BE415A0B7501371C427ACB0B3176E77D91C8EE7207D8FF8E44437A1075B90B9F9697AC26D85F9A7132789AC89F0BB1A31C45890BE996471C4F0BEF490A76E1316A4D8E0BD7961F3EFC895734A99BCFAC1635FCECBCF953B1CCD1B646C0B6C52ACF
	16B44EDC713EFD0A5044C603228E15D23B07CBED4846E63E0405C93FECCC8F5487ACDFF0793B0CF9538CF4D4C9B2A2165F64313C43AC665A5E6CFCE8002D87DA93C00C7ACB1B7C607996E8B350FCA0B647E0579A5FF861AD76C35F8AAFB81BBB31F36CB469F82B973E0F95FF2F74E1E78350E783787209661875539786FC33FD68182378FB27C179CC69379CE8A9500C81FCDA20B95E26787BAB69F719747BA950AE696F5D526FA2BF7AE02A741B6925FC3181B659AC4767CC733D8759DC4685F9BA94A86A89BC97
	8FF5D84CFC0E32455AE347D949AEB7CCA86CB6794CACF8AF68E2EDE1BFE46F76545BDDF6388A6B7674C8F30208AEE6CF1A2F6CF31AF1AE71A70E33796BD618D074909E55B5D36020C3B97B4172E8DEEE75F85C1DFBFBBD765DF60F2733FB3FAC9F43FD69D1CD0DFB243A0EF473EBAB28E3B2D0BA55F5DBB70B8B1CF7B4BA1D87FADD4DECC6A54DDFBE6057E966A7615A70C9BC1FDDDF44305AA74539DFE037F00C7134905F662296BF7A1111D17E74C64CAE4679CC7377FE5B1868A71FB226040D516BA24CE23E12
	A96919A445920BC1B656E7184598E1ED494652BCA946D89B331C43F28A9B9365D2EC3358E463080D8116CBD88EE063180D8516A75938E7F90D65AC16C35878E1F90065DDB6365998351A4538E163E9B7791A331E795A8FEDB48CA8017591D061D3F09E87F2G058145G2582652640F5AED0ABD0B0D09450A820F1C0E5CF439C874883B494A88EA89DA87F99B81F815900C20122011201321E05798DD0B3D0BF50B0208420B1C00529E09F488194160A71F1FFCD5004DF5F2E18EFFC352DB2D78652464A767596AB
	56G626A7F517FBE9231687A1445622F09C31C8F799B0BEC9E4266C37E4624B29E3D5545683C7F7F687FBBA44E49B5CE6FF30ACDCE63B945B627711C628B276FB943EE27511CC114FD6FE4E341F938DBB18779494956A95A1D5D5DF6BE91A29133D81FFE35195523333B5373ED27ED3F5D333B73289D2E49D259DC62F53E969283FFA726625A40618538B6E05BF329396A78A143F5CE534CBFE6D7BCA74E7B354F49EB834227B6971B5B589D76AEE8282D4E5EEE0F407771F596896F4964587B25FB6157D2316B40
	16DE0F4759DDE96555D8AEFE1A6C6EFDCEF7175DD671337510F5397530E7B96F06651BF971B67677E24B32E59BAD1AB5764A9E3BE7CFE7CFA7CC1CAA3EDF3CE7F11548A5334D851E2F383D9DDDAE275B53FC44E5D75BB5D45629E17EC9E13E2782542FB4237EAB97FF2B505FE4EDBFG9A7CB6216257D9D9A559687B7A87258D1885F736DBBD81D8D1CB686C2C166CC46F359A5901188D5C9489692CD5D35B6D992F2C57661E9E5047C35ACFE39A891D25B5121D75BB2CC6F6822917DEC8E7EDAD556B63355FF0FB
	DF5B3B1C076C422A9F165CBAF61D0DCF484666225D0ABAB5F676F8AA9D705FEE98ED83D606F17F54286416AD54C87AD3D609BAEC36598269982B0EDDD707CF298E439634AA5AA990B3EEA3897DA82D92F67EF230CD54854A8FC1EC86D4A1E3993D2D1542D6767E6F05AD647737EB2CCA81EF3D33F295365D14969B764F36EEF63D98E8B799CB696B35DA58BBB36BE7C5DF8D42245B07205EEAB556731DAFBB38DD2D203F4DEAB710735A3AEB044DFDC7DA54FD7825354B5E8C677C6D1DF6072D72007D48GDD2862
	7F698B7B91050DCF040D6602933EEC6CF3367776AC677FB7BAFBBC5A5361F6D66063EC6845136D240FF9D203ED7C757AC66ECBF3233F2D6AE974590E1F8A3D9D56860C5166C67F2292797467C2E9AC67FE7E25A28E7DD26BD6316AB52F44F6786E2B2FD5F942BF27B54CFA7DB4E6E79D78DCEAE7BF834A5368A312D62DC63D5715398C757E4AA052DAD7C54F2AEAE1FBF91F98ABCDB045353BF93CEDF5F66DF5EEF17679ECB6171EDF5FF252D859764577CA9B3B5BC1D5A0C6D482FAABB48E769F176A82EA1CBD1D
	0C4DBFB3FAFE3D95E9CC044DEB565A70A390DC865C6AE8D8AD0D8F7321235E757F4A00B074C295952BE9AC3D32D5447A36BE17355B6647B832AB9875B3D49AC39396B6F13D3BBB1C0737395DCE379F2AFB543CFA6D52B96E81CFB3695EE9F53960C24DCF6DAAEE3DFEE92C84FB6A397EAF6D077D505B0DDCFAFD52F9EAC467F6F1AD855728FEA8F4921BDE63DAEC5FB96D225FF6DB597C4A1F6AAB9875FABF216B24AF3613DEBF35FA53C9637568520B3D47FA173337473E1C7F7FB51C222CBD7A0B4A1D2CB050F8
	5F29525868D565FAF629D3C3F35D2B6764432F4253A6272B5735535E5DAB4A3B0FCC015F4F993043E36F22F1F43D67FBBC07523595B28DF4B25589685B72D33453EC93396FE02F355B53390F09C9555E6C36DB3DE7A6E3FEF5EE2A16F25375A597783271AB331397CF647C246DABD9A16FAB791A3E7600E80B2FFE433638A26782DFB67E97340538EF75FE8FDB5BE9FDEEC8268BA864B93CEF750C0B1563FDAB69665CD627C31A8CC2D94AF3EC4DE95A11FC45BAD244F3E27F67F4AA7FD4D5DE713CA877402FA83F
	4530BC044A579FB5AA9FCA65B106654329FC43B12372B8AA3F4D30BC094AB7759B1527D3F92CE1397985AC5FFC5C28BC1F4A7FEED8DEC165DBCF981557D2F91CE1F99315EF7B45285CC665F79B1677D179E7E62372D0AA0FB7AC0F20729D630D4A23297CBE4372F8AA7FE202D1F9B215BFE4D81EC16527E69A15E725E1F9F616D1F98115079A16D7D2793409C665F5D4FE36E1F9B3154F48B6AA6F20F213E1F9BF154F1CE4D4EE22725F9B168F237259B9C665A328BC4C30BC014A67CCB6AACF2172BF98160F2172
	F9D30C4AF3DE4472F04372C2AA7FF02AD1F995140B7D6597CED56EAF2FFFD1EABF78B5110E0726A9F5C43E284DE16FCDB34C91A4FFE23AE10E20728F8C4B4DAFE1F9508C23F2597EDF3C160BDC7D6041BC786D319F6C35F7375B59FA881C12F05F6541D91A32BDD6C72F3C1F7349036C9E1CC331AFC1608F390D577CC73B595E7F83247FF1F736196D4B94BA45BD0A61D62653656E6C323A0F300D9652DE7F39B315FD118F75E74F855440EFA31010356DE577CB34BB6C057C7BC5CAF91BA44F7E8E053FEBDEE6BC
	ED6B71D901E10045GA53FA476D6943EAC6D4D62FCE53548978C47C301E2DE967A92D5FCCF90DFC37A104C36F419AFA8DD49F74E86649B8EF83C02AFCD4537EDA372953EB2A4337A95192F7995A55F5B449782F80402AFD645F76DA6649B83F86E2BB2DF652BCABEEB9D7235815E27608BD7714D590CFC710027G19DF93FC052FA97992092F91F09BD03044972E62BBFD8B6983BCC541E74ED072ED580AFC5500B7E648FC8E955F6B44978178088C593FA4955F70FA644BF9FDC82665F5D9DF5D6BCA3E8776A2DF
	9F60A6855FF0955F496D4817863879DF32DD4B3F14FCEBED485782388B4874AF41972162FB157892A53EB4094F72062A9EF664EB823C83A8648D41B7CC45F7E09F6983BC8DA8E70C54EFE3D4F13F9F796A81EF9DA373752B781208AF9270D885DF0A0AAF348379F25F041C8BD47F26944FEFAA796ABB112FDF628B137892D4FC59BFD3BC831E7B964471DBD23F3D25643B1D78DC0007A878A2D5FCBD8710AF89708C85DF5E3F15FC0B9C48D787F88BD05F3F85DF080A6FA5629BA171A5CAFCB9E315FCD1DD48D783
	F89310EB2C608BD671593B11AF8A70B885DF2A0A6FC3A772653D8D734337E53E063715FC7793DF9F60A62061EF8B3EB8955F6FDC48178EF856BBB2DF59BBCA3EF587E91C83EED370852B78DEA53EA8406394FC29AA3E4B5C94AF6F0A76ABFBD770353D2BEA179E5287F89CD02A44B7D645D764C13E0AF190A3C0ED63841FEB1C126F91620B1078C6C8FC9992DF6DFB0CAF6790BEB7350230E20EBD859770AFE66333A9BB73144F1A35BEF41E61FDF5C6C16FC97B085F53BE33331A5F2F0F86BC6EBD3C6E7A26C739
	DFF0F3CF0F333D13DF64C96765AC0F721E3B40A6FB585A423ECEF70FE7C7374D5E076776999E66ABFB26C2022573F353E7C64F81ED33F5FA1CEE5C0E3975B03FDA5CCA9B9176F45A8FCBF53FF0157259357E563E6C29DE6A1EA455F92C226E854E6889420FEB16EE70570FE667D6274D5EG53940DC7786C1D6B8876DB4D7B788C5C6B043988F39FC5AC1230B788CBA14CF38C31028F90FB1330A6428E75A396C2583F890BA52C6FB8E2E389FB1B30AA33C9420E1EC02C0330F7890BA02C7F97441288FB0F303C71
	081DB0A356C0588704859196B49E3191040D1F00D886E1A7F30C75DF458444F2B291EBA3ACA693E543895B1005FC09045D1905FCB9190855CFC42C0E303B886BA3ECFBB6E243893B0730B4429AA6A1E649C26CDE42DA887BBC87B193E1A3890BA72CF1B2E26609085DCFD88DE1DFCE2178A36CC142A289EB1ACA718718989B5FCDE14FC37267D05B1DDD4BBB77F6AD6FBD54355C3A57335C8E83E45DBA3E1438273327576A60D3ECE99C7CE3AA36F1C136C9FA3E33E89A1B4BB741F187D0D0B65AF8F1EE690063EF
	C7BBDB6C913A2B27D31FE6236FB79316C8585A9954271350565B9F95E0FD5852D45F721E431D5D7B219A5B59F3E2D28C4EE4F2ECEFE05DA4747D25594C77BEB8B681C5123E6C02C2E3FD346EAA641796B379D810CB824AG924F591D4E1F3F63BE6618B26B00443556672A6B75BE40A3006200443ADE467AD5709B7826A64C3DD339DE75EEA6BC27BCB337CE329BD59FE9CE3C79B24367ED731E3CB4BB897C6179F4320972EAC67678C9D91346CF92755BD90B4FA3B6GCF50E42C5F732B34759B81E5491331FE51
	3513647AC9BEE7CE37406FC01B26C49D4CCD4ABA34ACB953308E891D415999126F85D364BA244E5C10B996C854A1FB83564186BCC327E09D4E2EB42A4328A9989B4D7B97103FBEF7E0C94FDC5EC2F652C007E5AA5AB9F529111D1629E8A7E5CA39EC47779EACB3BE5F593D91EDB9C04FB03265AAB16C9732F54B3A8A3DAD2F1B15042D52CDE8AB8374D464222D51730DEC3565222D572B64FA8D3013036A75885972001EA832F5CB21112DD43235FCE1055E56G75BA238EED0D85BDD55350560CD2A3DB9D535056
	5D9F33FCB770B6AC2C53464DE8279FF4C4139D4BCA251DF44073261BB22B2763983AE2B343555BACF07C3FB43D046A6BE30F85356B5B64C383688F1A0EBE2CDA282BAB14A5138FEDDB74637856192C7E83EF61C25BB7EE2178813D95B35076BB96A35BEDB3301DEB7614D3BB7B9BBF7B36D27C001EA832355150D6AA59EADF30C82A279F3B38A4DB9F55D37C001E2ABC347518C55B2F9DD0961A07362E1CDB46EC793155C858593A976DC4000EA43253DF2E2DD35ECC081F19E867059F970BFE72EFB79535613F49
	DE83688A1A097692F4ED486C0DA0FB87ABAB293F7CB3A96CDD530E7692C0D756AC34B72DC45B0E35D056B68B6D1D78056D598FE06716D43F1FEC94A320AB0A6CBD233217BACB44C86EEC9C8BF75B24B54B65427EBF17AED0762377BDB8125DF9F634EB811DAD2477041D4D517C3F1507B9E04A4FBE6CAB77DBC8769FA57BA6309DBF9B6BBDD3574E66FCD3E6D9BE7AD7374FFB9E3AE796FB2E4267BEAF0A2B30FDE83F86F43B72517ECADD8E0A043284327F6EBEFD8E0A36FEACE25B5BFEAA49664E7DE8B3857495
	4CC11BA7F467C9E633E98E46567A49C5944BDECDC8B646F7507C826407120DD05DF9045998C5B6FE1CE9D1649CEFD6040D1B49C69A48DB66220DE596ED4CB29BADF3316D8ABB55B17BB1DFDF7135E1CC586B6A247985689AC67AC6F58EAED6F73A0D6CAA03D458DD71B35ACD861B7973301EBB0A1575EC845C8394BE8F7D7A434F7A9819350C0D8FBFF610C97D18C63623C0EFAA59BEEB29116D42826C474B7A4BE4BBBEB71189BBC38F201DAA5051D100F6AEB43493C1F6C6FF426BB370AEB229BECD8E34938DBA
	52494E02A55A18E1F64ABE44367C3BC39DB35573CA54F63DE4B99C8F333BE81C03CE9769BD5AB5385819DA63553EAA4B217D0749FEA458CE79906B7DEF5DBC1C553B60A3742F36DB9FC3EF6CE4F16BEB531930F9F6B75A2C84FD360F50662C45C6B607FE04FDFAC1B7FBA6ED001DE7D23D36B85146F010CFA39B3506B6AC9F230D295F16C8B6FC6EBF2376FB0B6CD4038E4747E867268A2D1DE1D0967FB1365FFFDC7A76BB7F8B36C669F50F9A593C5EC5639E746597224D9F279859ECAC443A651BBF02DF6FDB56
	D0FF7BC1545F863261247F04AEC7B37D09245F194D7A5F5766B5695C6EC67D29A0DBB89F75B7659B69EF1E0F7A23A6FCA469776D7FC8525F81329173314DD7754029589C58A6B69CD7F76F2F73B62E82583DA645FF8F7A17843E659551BA419AE57D9BGE74F1D3B0A507FB7FB74B13378EBBEBF76E3E31A307D1987ED87035E9832FDC511527698408B0BCD19F545E87BBA0FBA4F0D5C47722B9F3B3324BE79006CB6034ED0523B2FF7F0F9AEED4FC66F767D5C13C56FA5DB253A2F705ECDDE6FAB44950BF51324
	E213663E027BB0DBC79B83F80E45A45DD7B843431ED3447B8ADF43D44F5A3D5FE1B753B311FC1FA23429F4DC5B4B6C727B7AE65CABF6FD9F6B17C250D9835484548194C2B66E6C9D707DE25B7AFC3CDF2C77C8A06F890B843BC26E2D0064E2C86E7051C06492C9EEECC0F229A4F764D8A0F2E3C96E1D0064F2CBD06ED8FFA0F296129B9710DCB5499DBF9E08DC83493D9F10DCAB497DF2A290B9974919FF89C4AE1864CE8AC8AE1C64A61883110BA239D382120BA5392C7101480DA239D38312CBA3396C8901480D
	A339537C124BAB95792D2C946526E57AA3D7A349B5125C9DFE49A135115C16ACFF64BC123D90120B77CB8EE9A8496D1898085CF0123BAFA039B8127BA2BB9039A412CB88C8AE1D64F6CE8AC44E3CG659E88C8AE1F64F66584A2D7C1F209814955125CD7138311EBA239FF84A4E7A3395DD382116BA339D1814905125CB7D383110BA0395106F2518B44380917FE15A45E1579FBFE7FB2994A59BCE8BC6197F1BCCD010B79DD59B4FCA76E98A8EBA9C37B1FCE8FE81EC0F23784A417C8F26BE784B4CFA039D3E7B2
	3931E57A76E014DB2E46ADF0AC5A6347FCD66F6AF2519E1954CE9D9C2FD760221DB2E663BD50E6A89BC1FA2ED843704205DABD57F33CF221D64F4AE254D38BE5918B319EC3568FD47F2CC56A3A88390B87142B504809F9780C5DEC1EEAF0CFD8DC2219711D53FF4CD76EA32D83DDECFF288DFE7B9661BD6C1378FE83B6A7B781A6663F930F32EB4AF8B8CE859A4764AACC1935C0ADC07D95B88FAEBC445EB563FB9EACB635084B222D5A37FDC86D303BD65B8EC3AB5475B6EEBF2BCEEE3966F804C60FE8174ACD5A
	F6615754401BDA016DB2FFB55E239FD7016D52FF0C35CB75E238A6G6AG8A860A840A83CADB0C6D52F548E83F0FD13B68B77B486FEA93F54C5DB0383679C8A727ED1B1CA5EAB9E97FC9A5565B328463BBF610BEBCB0208FAD06BE44ACC59FDC6403E76360FCB8D9A72775A1525007ABCB5007D87221E25360FCD82B13537AB056500769C568C35ED274A1F910BE3CB4208F8DCB0DFCF83D90FDE8A39FCCF5037361921D1C5607F0C39F3617B29F585E3B6164C37D6641797035CECE6BC35AD2231CBBF2B1FBD717
	C14EE57714243C727DC6E5DEB1D70A7B721570DBD709F9655B1538EF2925926B7241A06BB2ED4BC0F5B1D59A653F85455A7C9789FC89C0A964E767C26C77316467881DBD7F7C3CFBC0BF2B1699757B4A92BCB7B5ACC39F9CDB876743D11D1C5607A0C39FFAAD98FB61640325FEF0BEAC5749E9FDC8DCE654DFA34BF4F1B59D4EB740BBEE997657CC4AD7794B697AEA6F607C7CD6A7277533F139111FE996EDDCB1BFED405B379C7D3C291C669B6467FB0374F3EA7BC0FE46AFB76A53CC0A2B947261861DAE7FFC38
	FDC09F8AD69835D5D20936CF59DE05AA602DDFC17BC3CBB0D7342EC0BF6D364179592313537A993642A86F15D7F859FFC8EB7142578FA9C7C7112F736D0373F529CECE6BEB6A8AA3392786E96F15816D0DB36C43513AF1496EED67D719B2AB2AE8CFAE0D4B3AAAEC178B778D4E4FC81D1C564F3EAAA3BFA34B0C723D0979C8FEF6D03E0FA1BFF76F9F643CDCA72775F3CC1551383C1946E56ECA74E1D24760FC182313537AD03F524807BBA90EDB4907DB8769433D837A90E668034D026D90C5BE74F48E4E0713F4
	F23A313452C8EE4D4F033337D5A7279BCBAB0D724CFE873BF6B248B33807C00A654DA53AD8E6E3EE950C39D534473F9873E65DAAEC3FD787D91FF1835627EF15D19F46ADC59FCC6443E587866743F01D1C5607F8C39F9EA29FD24807BD0E4179E05349E9FDA8D8ED644377CBB02FD42DC69FBE6C9A1C8F0BF4F2DA9FBAD69B49BDBEC8FBAF8EE8AF48304E4BABE94EC8F58E6B9E1C8F17696434BEA49A7A30E3B17A10C6BEB4B88767439E1D1C5687CB35118FBD64C3CDB55DF39B248FD3DC8379605278A0724537
	D30C4EFDA140BB349A7345CB9518EF23494F1BF5767C73734E817DCCB7EC2B7326208FD9EB50074E0303736130CECE6BC36D9A4373EFA97A50CCBE4CF78FF28E2713537A90EA6843E9796843B0726121C17A7014CE2EC8F31C3C46C8AE2CE710E3D5A7272D73980DBDF16E0B1F68655C075BCA2436193F9673F96EDA3A4764991C2FBF6864343E562FB572757E63DEFC450DA052381BEF514E0D591E2FD65069DA0B63AE398873C38855E7226EBBF77E55E736CECE5B57311A7A08B55D987E8CE39214252D45755B
	7B8E31F735639E12A6E7E737470C7BC78E9FE66F01F03ABB77F7F6A3564F31F6271B3F60DAE0FF69737E6E6D5B7A0C5F3D9D57E77C6E6D7B7AFCFF4F639FFD5E5E4DAD3E3D914A1F8F9D8B754B7F4414A930E9C7912BA06CEC42EA891BF18C31A642CC0459881B590FD89FE13FA7AC14305947910BA0AC0C30E842661CC0AC1E30BF9016CC583CDF904BA0AC1C302C9A44BEB4A3D6C0580504D5927671F8446A889BCAD8B3E173A7A056C158A59C6B2F91F1A6304B09AF0C784AB211AF1230EFB311AF1630A442C6
	9176DD165E460FD94817C6FCCF91B60E3036090865FD0A585304159136B79B319A421EA52C11B05BA4445A88FB0EB08FE17BF2908BA16C8542069256B1993161043DC8D89CE1BFCFC1AC09301789CBA74CB195B173BA44DE65D8BE9C179136AB17E1D5F05C308E73D0B8CB8178BDC8455FCB26797B1D184110F48D751B5A90F5EE1A0EED6320F6F80830A0423E1EC171CB58D07E7CEAA49CA7907646CCEC4394425C339916359E465CFAECD7DBBE5B53D8C6475D7C18BDFBAA0E5F1A9DC85D191E405BF7C6FEA0B6
	3CFD974A370DEB5630FAB5D2BDEF66476C7D8E6278136201FCE88C30EEFA9F1A863491E85D14B41F4B3F60E15FB151FD4701BD4DE04677905E3DFE3244334D635C495E49A76DDDDF6C65D969A6AB1CF9459CE1B44D879D6B71D969F33358FD78E1F09C83B40A5A78F73AFDC169D0E62E05739ED0C52D3EBD2577C79C75FE2E1EFB5478DCDDF814A5059EE72F3B5D2EBD9FAFB8DAC0E5721DDDB611E34DA2F8AA393C476A5EEF571D4FD7F3F9D166CD3E762837F33E6817FB6732BA395843D7E26FED3D55E3157A63
	0CDAC5FFECE57DA12DBBED42362E2D4536BE461F997640F1A85030DAEC6B5255C6FB97E220BC91A855EBDB7F6E187736BE6B18F1DB1FFB4CFBDBDFF8EC60363E7C1877363E7A5840EDFD53B1EFEDAD5AF36B86E5FB2E2271F7DF25D19B0D0536495DE04A34EC5037115077EF153E55246F0E25C67A2AC1CF83D02BD7FD57ECD46A2BA6FDD1A5C67ADC20A798A85C2B3E1FB6A9752DA1FD330B0C74C5011ED820D1DE754D2ED36ADBCB7A5EAAB4521786FA468165ED74266F9E153ECFC85F9725DAFDE52023862891
	288D482351A9749D5B2C5447762DCBEB165B58D8B348DF3B5B5DCE0703E5B19ADB3F5F0857EEA19BF1EC9D28E2E3AB960E1300B2B66258EADD69EDDFD04EA6D3E661A6EF75DE3BC569A77BBE0538967BA0C7F9ADD635C93C6B2195FEDD1B70DA6C07E29C7BA11B503F74855CBFB8CE824A58C4676C05C67E655401EFC0D5F55E7CFBED4B9405FFAB2BE8FF04E19C56031E96A087D0D01DD13FDC3ED525EFA55D6B35E8750501FCA4D08CD0A2D02A213EEF6BD57AD6517D42F26C3B31F558B6659656B6D51BE16C81
	35ED463619EBD897C6FD40933AD9EDB7E2B3BE139134D7E5FBB55A3650BD11684DE83B380459CE07632CAD5066DB687EFC39E1FE00728620D6A0579623FAD7372BECD753B7G8D6B928CBA42012200E28D752529752D413A6415E2BB0E5A02F5E9616D183B956663C035DB695E3557F6EC861E0E2D5EE2EC28CDE57B932CCB11EA6C77EB64CDF0BC8CE884D002D4A63DCF5A2E52C76F6C1E3BCF05FF0AF51CC1311232956B584A6BD8D08F47C08D7534F663350EED406329D77B98D20F3172104A762AAA341DD206
	31B2349EED0F65E3F8949C2783E5EDC35BDD06E338904A2B006A37F9EB5FB3762BEC53183B4530AEAD204781948494364DA8D6B6F628742DC2FD713ADC9D8972B1C009C029C0E38D757DCB2DEFB56A6BD465045C6DEAD98B9CD783B5A8F069FD671DAAFD74BD97474FAA7C936C03A3B4DEDB37E39FE472719A8647D1C0F15BE97FEB312F7D1F494017315D280E6587D436EB68FE23617CGA96BB3388E7B4CC85F23EAFD1F223EC7FC6A2B84DDF5067A4EF2A8752D26BC7F5392253EE60DEC879C779B6AEB6AD269
	23F74A4F6AD66194BB2BF573AE93689D86B482A881A845504EC835BE0A1DAF97EB750D81791C86483DC0D5C0758DC67AFEF12A74D1DE6D5369EB81F987D090D098D024213E55AE15BE4A2BC3685EC1CC83465CC513D94C0D01635C9D10DBF7E04C3DD62135DD8BE5CDC0B6203E9D5E46FD3A5A76DA2C4B053A3D752120A382A89AA85E2B3E0803AAFD1427EF510D7BE45011811475B9446F675E74B53BD57AE8CC0466EB75D5020EBA20E6208E2F7AF2FAD4791D3EABB1D2052F2631D23A56E82C7403FE13018D59
	CE30C7292F1AF279B7346FEC586758279973D91FA6437198205CAF30CF1FD64D3B4B3E106D55405F0DC0EDC01EAF34392D36D7E5FBA5DE935D514F763D3377A80A633B787191B34EFB6D7C3DBF621AE9DB37CDDAA0976F103C1C2FB50F4EB21BE7CCC02C0C2FAB3F8C58B704DD413157G3B143005FCDD79BD40DEA46CCA0E3D82987BB01940AA7A116F98E1D7F16CC5406EA1EC89DFD73EEA3C784E2A402E6158934037F8BCE215A750178304DDFB826DBECA324B793A7202897C7BB492F63DF897A1B44C8564F3
	95DFD71E8D588B045D403153C17F4A09082D626B4A4F020DBF125D1B88FB1030EA3E067C30D48F5187F33FE5633D2BD334FE8F79F30B608579127178BC4336CC96B7A1DF484F0F885EEE3E3E7C9070F6122D57B2312F9EA63EDE3E16FC97E067925FDB9C3B9D30BB89BB4C571247030333298F46F2ECA2E03B89BB4257125F84591388FB07E3CF83F683E147A6E1DFBDCE58B80EDD8BF41C5A71B8DFCB1E4F5A113077B956BEC1097D425712EF82BA07B0B34F1DA347AB3113B8767A84A5B6012F9B4F4CD2E227F0
	6CDD966BD9F42EDA4B5679BC76BECF983437597C92E841BE3B61B09B4F7B5C56AE38CC5C6D32371BAB9BCDECFDBC972F4F1224B11AAE7D66C23930351F2F4D1603228F49FE9A47BE0663FBA84EBA79FA6DBC40DE1B00BE5DB4132D51596C7B2C3D8E4FD60733473E4BE5F7DB256FDBC4F19F9E180973D68F9C8B3F264CC23FB260B8EBA71CC7006A01ECBBF1AEFA7AECAE5369614FD80BBA0F662F0C94652DC7597B83B46FB95F6A6C7670FBD96C82D9704D68E64FDC9BFD9DC65A772E4A6341E0BFFC27E823A878
	0D5D294F31A276A7CD64735F1DA276A792BE0B63E98ADCBC63F381DFDBE44763202C6DCBDC4B3E1847F19C9CA79176BC475261F89CE1DF70B84E59E54A346CC2ACBA8757462BF7513DFD55FE7CB640BD3BC49DC260F768AEEF75194A5F2BB7FC1770FBA661059CCFD66022BEA77175BBF61C5384F30BA674695355683B65AB3A9F369AFD2FA4ACF5A57ADEC7585315A8EBA2EC4BB2149DAA6BDB01324389CBDA0A32662F4917CA144DA5EC49C2143590F6E7894A3A88ABABC559FE429EA8C1D9132C2F88E5D388BB
	3388E55389CB31202CF9B75DAB1D0F32CD04F54FC759D6427AC856C5585ED20C1BD8427ED40A7AC691B6E9816ACBA16CE3EA03B10415F2597AE6D3E6CBB34652792AE7DC9C4D6A5889EA7696CBCF71752730E66D73EFAFF2FCD833767937039CE74751D0D6708D7A5433127A00302BA8EECC5F603BC41666F8D9AFEB366E5DEB3729564BBE21774CC6FF03731FCB781CB69D0E3376405CFB8F563FCDF79DD53DC7543981FEDB77286B6F5A034F7F7517B1FD41FB34E369AC0E8F5F239DCB7FDA00E3A9994ABA3E45
	3A1E3A907BF38CE14FAF44B6B137A0F6C7A936C9BEE1BBA926ED04DDD70232AE424AC9B61830D09EEF2360B80D30F3B8B68E0E733EC36CB18B5AA8A3ECBD5928A26CB00D1BFA421EAFC69BE15FE1FB67285619A23FD337EF4CF75E626D823E8E12781DB65EAE67F84AF75AF8DBEA41F84B0032666F5127E5647B88422EADC35F9388FB002F45E440F16E8F68FB39EA1D4282F8758FD22C40EF6B8F5EFCBF17DF433BFE90BECEA27C0F9C8FD1604277F20E336388A84BEAC51F9EAC473E68A02C0AE245C3D88675C5
	88E13D545FC90495D37C249396C46DB316309324AF77BFF48F1E7A3675BF58BEF3D457DF2E7F285BA378BF5E5AA70177D578FF34FD7B8847A37F236D5B9F29EFC7C0D9610F34C701FC8FA2EC8775ED98E1EB78F898814789047D0E46C8B2E141BCCFD77EE44AEC78097AE0BE551B30597CDC600263601F30AD72D5F19E7E133A6ED1BFF9EB0BC4DE37581F34ED7118087F1F34ED31175AA2854A6A5BE8FF8535C5A4E1D7D0DB4490F6B745F9CA9B7A7EDC19EE4D22CD781BE30576362AFD2F32E2CE8C61392F5E2A
	4D0997F05CE555664477A9A78E05320A3D546E14932389FB0E62BC1630BD8B30BE2388FB08E23A2C9D31D5F41E2FA2AC127A3B1E30177898890363C8421E62FCB1F01CC8D845823411CA5869F48E4EA06C88594832A1363588EDB49396446DF640F1BFE189B49F88A5AC1E4BA643F186E15378B84C3243DC470E7DF7C215272A81EF300BBEEB05DF175DDB6CBD4873CE30DD74534704BF4771B885AE7AEFB24759F156BE081FFD3447231862ECBF1D0328EDF3BBE83E341466E904A5ACC6BE97E12F90DFB0E15B17
	A05FA8428EF1AC8D0E47917677A5143BBA298F16E2BB369276F9A54E35DC041DCDF355E042DE237926093012CA34B10C308C72EF8CE17645E8A367E71A6BACC69B0504CDD902B6AA88ABDB06B6EA89FB2E8267F321048DA19B91047D13ECC493D6CDB6E289FB036AB10A308B66230D3283349FDD37EED7F3C044D4A37C369DD0471767G660759D378796E000823E994DF259C0FD260A23EAE65F3B6F61C88E5358E74A91B57A7920EE3885B4531C4B8CEA1ACDA549B0E333A907B3E826BD8C0588DD3304DBA88FB
	F38A36D9BFE1FB26E21B0590361C76FF8DA56CA11A372793B607F206391B6215F2C6AEE11F4857AE04DD1ECF31C9588B79941B04B54C2158A46C7C8D941B04FD4A778233FDECE3883B04E3E6A7340B937B79FB3A6FD189D81DD35D2F4DF05C81542F40C5FF5A0AD83F191C5AF3D8B747A31C5AF358DB73719CB69C4A72DD6853D81E6F7A58B1E1EE1EC70643F1BCE1B114AB1389FB011F874C87C15FC144D672FE2900635A0374DE9F3E5EA05EAB3722C679DE396879C732F14DCB1BAF1B8ECA79D47AE56F159357
	9E21BC9E180C894A628F323510DBF2156F6AE3673B5333441EE9D11E2EAAE757BB7B25F26157529F70DBB24DEBEFB4D71CF2F276B4AFFE653905BF55526F30692FCCCC0FFDE83CD8A7F90E3FB3C5F179347C5B4EEE1B7370D6E7F70F475A6D69D9DE3FE3F7532E5D5BFE5A7565CFDB9B618FE17FE93332DD4CB1D1DE5F3767106C86C93FE3E73FB2D134CB55ACEC1791EEEC1718D94AFAB39F5FD6354B2B33346D465E79AD17DBE6AB4B192F0FE6AA4B3F1E2DEDD7141772DDBE1EAFB25CF4EE1F4747C68F645E9E
	442EAC40F155C818B58BE32E2D075E394557F40261B81C30820E31EF6CC5523370FF4C72F78FA153638F5FF449AE7B156E677BEDE3221FFC58C778BB5B9C4D6B5655ED6FF2F6F7360B3540119CFB8AA8368757795E1B28DE57BC191914F45CD6406EA36C35369F586FF676F65BF0CD5143574292C1C7B2D021076B4E317870AD47235C579C34897B566641BEFBE493762D0730264D583761041D32857BB64A03FDB6F3A333BD8E0E8BFA613A8F2899288FE8A8D0ACD0EAAF6DEF633C6CFB876C1B09ED52378E2B25
	EF9466CA5F96943C27ED88B2288B534DFED3FBFD77FF75G656324B6E17E95C87EE259D0455F42172B8DFD19EC16471FAFDB137C0C1D99D24CCDD514CDD37CADE264FB76D804948BF77235452CC370B7100B7D1A5FDDD9AF71579EB23E8F076798C6CD4043E80EE45BF6C83E1FA16C59A649768E703956886009A3FBB72CC0FB99DE6CE57A3017F5D8EB2FB6C73637174F3B72002770B05A5B3C906DB59CB636B751073DD61D3D7C49323D1D7CBC5881BC9E3277DC895A0B70E2EF128FFB51BAFB698AFB517CBC9E
	8BBC89E46F5F9634B7560B3D49BE6C6576E96D95CD156D1D4F672C8540D356077606103D26BEE3FBD3FD450B421E30F1A6EF436700DCD0A6FCF8CB61C3951F5B85C1D9A879300A7AF404979F267970A1C16103481D1F1632BE113E91A159EDAA4573ECD29F2D89EE44DC18CE58BB9BB1470D6B236B7EAA3E6F7C885D6F6747A9142B8ED78D76598226F3A09EE143B4E89B7E6CC797EDB4142FAF5B40A757913CDF953B926FD78D85ECB8D0A2D086D05ED1EC0FDA1F6F9E2CBA6A6D3AFA963F3E29BF2ADD7FAB62F8
	6BD16D7A5F3049387E67013224233446DA0C672F5AE3083DEE4173D79BE15F513A080730A39C8B076328E3D81F134BACAC9661B889E8ACD0E1BF4C7301ECC041C0117D48FBAE67E55FFA31C95F28E56F9CE15F20CD849E76ED568C629D3BA0C82ABF1357368933E554FEA33C604C9774AB58C01F910D25BAFEDC063E981D3F3C4DFD3C45929B1FACAF3101BE434F38E4FE087C104A3AD50A41990BF84EBF8EF5BB0E79A12914F27E71404FB12D4735B9B1E61AECEFB43F9E6AG9E8F592BA5FB91DE6C79BA47C49F
	5766234BAD7AFCF40B8573D16CF11C8FC6DA7473465BB9F6A750286318CF43264B7E0B754CD4A84BA07F4F26F3D661094067GD5A734B9FD8679DAFB0256484BB1F7B691F6201C667EA7E8CEEFE139F2B89DDF450F5937174571CA4BFF63392C61867514F367677F959B5EE2DF34519E5E8F8960C752894C1DF1740E182CDFE01E89D483548254778B767993657CB98C7C008EFB21A1697CB76DFB8D7F855B3836845BBD053073CAB07FE590B60356D0720390DBC16B2E95C1589F9770354ABEB80EG0AA77C8A0E
	33EFAFD924EFDBCB4F2017E0DBB2DEE5BB7485697BA042G9BA8A74CB457138D6D5C59288F8239CEE3A446100325A9291D3F63EBCB99606B38A09CC313A887D41C242F83DE67FB9BC335A7E94750A5B4069ACE42F1DC1FA77BF0956F43D6A86BB889FDB80F46F118979FFC653DC81D8FF964C374C99893152598CF710475D11CA41DC46F9766633662E4FAE7A2BFE65FB2171EC164E30C157F5AF166BDAE0EF39B7E4CE786C3220DCE677D50G75E8BE9947F19B0D63A04042002201C6810DB99947F1E3A93BB05B
	6BF47387219ACEE1F26D252C0D72CF417E3C18E2AA7414016776914093FD0A5C1F7127E84FAB974DE781443FB631393D9DD2085B6AF13A25EB8BBE57FF82E8944889ECAC4718EC6AA96837584341BE8B4B4ECE02EFF391464918D3B0A616D23E4F8926F997657B02E00C0BC7F8DCF450F10AB8FE836324C3170B996FFFB7CECE525978EF460CE82333F81C74036FA14198A7B194A7510045832581651C8A7976D46C0337A827B71C0A6DB82E8C73370930C3F4DFAF1AB0BB4DDF6389BB15662F6927E21B1F4D6724
	E361B86FF7E09B289E2895288F48849475BBBABFF3DE766D37B4682356B1C378BBA5E261B781F84A5E9C1219CE3CD9E5C1704BF4CC1774875AF62D12AC7A20ADE73E66C97E7A6E63C55C97DC9F99CC7F62BA4B8C33719CF81254CF13FD6890F93C1009CBF1B11D4FDFF3CF03B1F19A0E7B2E85B86E6BCF8BFC7E5AF21AF67EDAB3CB3677834F55B6E60B6C5DCEF9E62897FB3E662F43CF536619728A7D7CF5F68966081853B0173CD52A1F3FCEDB0073574453703C17B7DB767FC6DE1E82E569643F097CAF8889FC
	7EDA9922BD6FB9A81F5504202F379B64EBB1B7ED8CC1BF1E253511A0AFFEA873F59870C40648FE4404E85B313BC21FEBDFA75F62493732E26FF9FDC3B166756490757C0F2FE39076B24D8947052873875FC7F9BA5DD726F3FD7569B84E9FAEE14F338643F184D0ACD08A10798C10832A83EA8572G0583C5830D824AB88365DF667279E702CE205C3706E406G357C9B2E0561379930263110E760B78EFEAB5E9E1299F62678CE1374AE0112C4CDDBB33BC663AF5CGE73E36CA7ECC176AD12770531F5C60B160B3
	FB11E5ED04FF471EBED05E5A2E2B5BB4AF7CF38C3049E6112BBED644A04BDFEC8F7B07FE54EB3AD96C3377168B35241CDB7BDA4796636526E29CAF51E76A5B8157F2B0774483CF721972F849B8D3BBEE3F23796A38B3E9FDF4A95A488B555B182431D186BCB521320D46D02D0DB797230D16D03411D509B69C86B6461B55EB22C1216A7C93263361DC02B6A2C2F10CCF19234FBFFFDC4272CFB47971689274A34D408FDC1B459CB48EF8724E127DA8BBCB178717229FD5E761FC24072F313F8ED4FF966667AEF6FB
	D012A960EB7E4DD056F6967AD5C36D93FA16F17B78DA2F0D507975A17995C5FE1D4D7BE5A4D0AC7935ED0E6C57EA5E2609D016CCFE2DDF06FE651CED9C9BF80F0579D588BCD5E74BFE551F2D75EBBA45C653596857880EBD8EE4BB9B7D1ABFD7766BBF5CAF8F14851D0DFECDA13F228C7C9A2F71AB96F8C6A97CCA5379757779B4BFBF9B7378B5D3B05F678C213DC539186FAB894B1C0C793E0E308513F18EE99BC26F13646F78F341F188D084508820C420F4622960BCD96F8C490C83E26FE53741EF16896618EF
	63FC7F4A49ED704BE43C650E90CD194B872F12501F8872676F5C1637FC2A4DE7B8067C3105EB1BF82FE736F97EE27DDC27FAB11E1BABCC74DD40456AFB3CEF72D891E5A3979B5F63FD166B29819D75A60C21039598C3AE13F18CA967F4414093EE12E3A84A240D214726607D6891A60C637B6749F17C8B5F53938FE5234807ED396803791C01671579672847FD45B9DA9F5EA94539E175B968439585328FFF647315FA26679C7461CB3AD754EF6043C40D8FA60D8F43F4BEA4703D8EAFG8DA79F0EFFA87BF0106F
	9D0A05328472613A8DF40F4E4007C99A9FF2FF2F764172FB6DFC7E240F641838F92DF1CC5C4F77A1D502EC4D6FF15C9E6467C79B9C879176ED896E8D8AA3AC1276A98FA32C056710B1F01CFBAEE2919CAB04636AF3F1CC7DDC446237010EBD7C38EE9C0E6FF1737F5B6F55E23614EDB953BC27683FEDA3D373B74C4DCB14676354F3D91B1750B79D9CF09C8CB482A8810855DF481DEEB16E2FCBF95FD406C1FF05617A40BF697D0DB6407A004201C6GA505E13EF83FC4FF0DB0076EDD0EA53E8603FB1CBF15627A
	CE6EF978FD1D65EC2AA8BDF37AF1D6B97C6E317EACFDBDE79BD8F13A7173B91FDB8FD9F76D7D595E6EE9366E2736BFA757573DBDEF3992241BF1EFD65875971BDB9CC732271A0D77C6951EA77A3BD67A2DB23FB2117D8A7FCFABE4ED6600329020A820B820A4200C73705D12D67E2DBC7685A02850E60F475A5E5145DE0104B55B53E9BFAC436CA1C1217BEA9E6FD9FF006B1ABF202E482C255E5A88C0FC66CE20FB7897CE257B003382EDA7A94666E0CC54018FADC021C091FF906F1391FD3DE99E0BCD07750833
	5703F2FB66215C8860CD849A8B14FB3E10937170885F1FD586D895D06B798C6B4BAA70611330F72C0E496DBAA0BEFDA5303ADD2C4DB44F9C2A1EB192FC4FCCE43F0B1C0B245C769EFBE528D4F6D58E7B6EF5377D30053EE63B5569F03AABD6D5D5D7F2DA33E631441B1643F265EE28F187455472BE0C29744F061993422E4E1E69A526061DAFE2A971FCF99FA24FAD62BAFF934BE1D96126CC51169F50372B5AG6B830AG0A8E975F1A91EDF9046F07CAGACA99C4F8397EE4173C056854667E4659AC18170D4DE
	A04E836C386E826D7A400ECD6C3AFFC7174B6976482FBB92E567FDA917B59FF15915FD73603159D4568419883AC67AEA19A87F3EFE0EDC0E0F0252F74D84CFCECF011EE7B7FB3717ECE764D7321E4DBDBD90887C65F4AABDD79CAF5073E874385CEC816BEB18BA9D328BCED4A2E2262EC40E19C7D77A93B3E2AFE565AADCDBED0436ADB60BEBDD3E21C87AA7747F2754CCB1D558230CA95B93E1598B3C44D430544723AED0471450F971825967A1AB7C7779199568F36E05B87F68AD13FD6DAE177596AF74DF2FFB
	A16A2DA73D5B974AFAFFD9A46B3D3344FF3DF369FDC37D24775B8AD9EF53E2D96F43967F75D650F7D922C9EF4592D9EF49D2D9EFD7GFAE3AD28B71D743E2D50F5D9A94FD7EE4CD77F2A144B7282E86FF6EA6F320B504EFD4BE4DD7BCBD959BE2B2347CEF9E874C61607DAAF42FCB2F393669353C5467984F3854BFB4300E744C5F2BEC938C81BCF52BE1FC2E311FFD2CB2A631D4BE53FAE99F0FCD5D5E39D1F276FECA5DD04FB150BD6E977AA3FECD66EB5EEDA255D2BBCDED5FE72AA6DDE6575631565512B34FB
	15D1DE4CD9F75375EC5ED05ADB4C778F5542F18B100370B20E336F92326FDB306FA8C97BBBA7FB1BDF220EC067170E215AB96C1DDEED60F5D320B6146BF162BC96C17355102152F98CFE23070A3A0A7358C7E21F82E0C9C0D997B34C35E060B941BA5E4EB0A9EDE62FA590F1F4518AB90E9E2FE339D64E78725B7AC47954ADAC67EF3659F4A78ED17E5097AC0E45B9415B396533850CE71B18739293FF31A971AC1C4CAEE25937B18F4189CC2BC9709C7EF01652D776D24AC66BDE26C4145FFBEC2E361C3E4CC439
	617B0F14BC75761EF6F727CB69F44AF3ACBEB7D814715BF831688B977C0AF2E711323CAFC71487DFCCFBFD2A64B67EF3297F79E872825ABBC23A9C6C3524D28E302F1275FE94005EAE4A7F0924F743EAD957C396E51E9358A3961EE32D18E357D64B7C0EG4EBD3753B9A2679234BDFD0D2CEB21C51FE39B4A79756AA518E3CFAE479C6B3944F70EE56BC8A140B37492B9478E3FC41BE317707DBB9487A24DCA751C34D6766D32B27F736CCBE5F4EF6D924C3385655ABC4B56EC65BC79F9396FBC5BDF2E4D331BD5
	F9763AF2EFF9D678BFFD61BA337A9999D6555D4AFDCB0E65BB3FE9EC5E5154386D279DDFB6EF7BFA4F66461FF6EFEB5C3635F9472EAF31BF6CA52CBF46DC02F36F5B685BC32DFF94B94B833FA1C0EC9F2548D9B5BC16A3G0B7AA37663F63AC71874C76FEB50388E138EBC6688399F73A3347D98DE7711C1EED13EF4D47065FF762148E3DAD6CD2EBAB023107219B12B0811091F4AB172DCGE321064621A58247426B8ADDB72B4602745E1CF5F2F9CFG366E26F157C236AC35322EC586636E9B3E774594C17B4C
	293F62A23C0FBB3C5F1E84BC690A7EB2DF2A6D2F2BFB728D7A0B4524706F438D32FF9B8398FB3F233117FBA90E3D68B25FE36F79B25FE32FE000317779GE36FC8A91BE0EF61EBD35B2D6D9E277B4872F6379D4EEF0D4E43F638D85BE2BFE4F708720A45623AA6E435725934549D27E6E71A0D2FEB6AAF957D51FC2976593487A84602AFB53E1E72FE0DD53DD1EE7F1173F97E6FDEA49D4F60EB089EF72FF4ED5BD2240F1FE61E37E3AE25BD2D1437B3AE751D37593D142C4BCC1985174971D3F919B6FE962FE3EB
	AA4D0D4AF97132CD324F978690B34FD04C54DC06B113B7C03E5EB2C03E768C90B3FF9AA0E6267D6AFCAD72F3F9B966E757E5A2BF07406F504B449CDADA276757BE510045DE06F34AFE1FF3CAA97755496D7D38B3177707F65D6378BC36EE88793639D1B5074BEFE4FAC4FE25A229ACF79E53355D696E6AF5D87974D0604B4633B5941C9B2214A8CB6F1D4A6D4B13C10197B54DA0DC1E84CAEB3F1F4F66E57A491F185BDDD8241D7B31B6CB3F8C67FEBF97EB67FE2C5CFCB9665877956D7345827F7375A5B4774BA7
	DDBFEC1147E673D6D96F45814C7DB2A89FB412DEFEAB9867C2A57A39DF5982755C6FAC174C5F9D006DBB64DCC4366D87E5DD279469ED5FEE89A4EF8ABB79F42E0B39BC501C3741AD7B13D32C77273998732048F13709395065B8974AAF45B153B4CC0C358E786D9FA63E73A45D9767FD9B86D844B05A27C4675604E1835FDFCE811EB143645C18F305B6B71E6AE46324496A71585D725511A88B5BB8D3AE5B5D6EF4612513A47B440F335532742DC9D1EE494E5748A303A84FAE6065EE274B595349C6948DBAD13E
	6D07596AF20D7E5BE765294B953EDF32176505EDFDAEEB37CDB976CF1BDCC0B8DB4E663EE96C9E6AE4ED383B43F9F81B5B6DF42BF2D138E526D436536AF2C12E554C859BE6B2592FFA2D5D1E4EFD1DF637B23F5CF738C0D156EED54FBAC51E28A8556691968F79D7E09E39EF01D1F98515DF3C44283C164ABFDB2AACCF1DA54A1B28BCE33E113C6D8AFC4FFDAAFD9B346F0AC0474C1C9EF94C38D473C8E9DD3092F58F8FD877AB8A5D39CB743A57106EB4AF3A051E3BBC321E5FAD510F6B1B17841EE72650F7C4AD
	FFB2321D31493BBF97774A7E1C32D46F4F9915BC4F74E14E7D43A119FFEE257FB937135A2765CF18F33B8F4B3A2A8CECDF31185B36236D7DFDB27F074B7C376DD80636CDFE37CF46A6E1F3539159E679E23D0F8F8A9F3B50471A23B2FFDAG6D331EFC0C8F58473CE3324DC767ABFD946702D1740DCD731562DCD0G3F15C0EC7DD21C8B3E647BFB6A81EB3A9267DD9FCD9EF85E152630FD73EE36C63C4BE5572C47CD5A40705D56C3F6B53E291C4D1370DED3B76531871767CA7C1BFB1439B5ED9B4B470A9912E2
	BEF64FAEB62F7D52FE1872257836C6F329328E6C7B222C4E7DD0C75315F8CF740B45F8AE95F5FA3ADF2E532E8A7F7B70328A6C4348AB839D07FFBDAE5BFCFD8A77076EFB5EF8C2AEDB106B3FBF07F2511F94AF7E887DE77CA26B1F2919E31DE2164BAA833807718B5D43A80874E57B0771327EE773396D9E345DBCC1AE8B5D603F6D27B6206DA61F367F12A9377F70AC5956309A7F6FAD07D878BB165C76FDF6B7E4675D1DC765E76E443E0D9070A18CC84C53CE65733458C85ADFCF4FE50D09743EBFFD32D41F1C
	2BCC1905D749732D2A2B3473AD67FC466BF69EEE30DB45F714045D53586B174D35D7E14C7FE5BE9BFBDD1D5D1DDD3DDD5CEB6133F877DE8370B593EF6DF636062D1ED75067C4256729F794637B0502C0EE44D578CE511AA36C6620761D224B456618F56BE04665E2DBDD4489CD6A3FF30A719AB863AAFC37686DDCFF4555264CBA2096A031B6F1B4936F390EDBFC1D5911FD0961FBFB8AEF8D4DF6DDAD5AB76CEA791EEBFCF77484517FEFCC147B7F06816F894A576BEF53BD41442B71FAFDE50976FA1DFD63C83E
	5EEEAF71FD3DFEDE0976FA5DBEC1D9FECF096F6B754AD27DF96AD0A92E13087DAAF77239F34EB5264CB2208620D6A057B538F7655A0F590D6D4DFB0D7640EC336D37637B0CF0BF1029507BF7434EAFE471A3FDB70CC9355B77D85D1DEC392D8765A38A795EA7A7A46FAE269F71BF9572E7741DAE8E8A6C1AC29E631D7BBBBC02D542EFA8E4FD61306F6330402E1F4F5A083E8DA63659C87CFF66F3D198821ECE2B23732818AF83E377FE91539F4C41188E05F6093B86E3FA22255CD7CCEF7334F758B122F1FE7859
	DC5AFFFE8D46F46FDC263FBA4A145988E48392F6CF70B587465B8FD8F4945AFDFE396A5B3EACB0572D93F69A45069EE90DF59E5ACA0FC2DBE3384E32EBE12E8E54FCADDDF36748F13FA74B1F3CA7465D83EB15FB9D58D872763EAC473552F3DA572A77BA08B8EC1DECE1E3874A5201722E03EB8D20DA20E6208E20206BB0A6B797333931377DD422A1F8DC3A2D1D5D9E699AEE5317E66379B43F79A8F5D3059103329FAB96D7DACA45CC25F4B1663CC5F558CFC34147D157E1BF2DECAB547753E76ECE5B966BFE51
	D1221F1CF534BEFCBD765345DCE7BD9C3782F92E47FEFAEC0A5CCFFDDB7CBF970E5C0279A9747AC06715B772F72DC84F562FD26EC90B83DDC9577B3E17AD7AB7026F73CD875E1C61B067842A87EA81F28185830583C5814581A583E5GE55DGF3D4208A20DA20269BB086AEE34D6FB58624C61603007CFFE10552FF9B686B3B41377F527BD8D45FE18A8519883F64BED63D27B79AE4627D12DB26FA2FE8B248E478A5F7176A7BC9D9B7C27B5D680FDC072AFE15A0D3B7001C0841D7B686FE1D3AE59346E2730D01
	46624A69F2AC3E2C7A7EDEA8680A9840E771EE331FF92E714693AD6990757BE5BA4BE738C7D13DAFE078EC66C7F323721A65518AA62B5DC8A072C4FCB16609F830E13E8957BA64E768EBEE42713F113FEB588547A1C091B76178DF425FB1A0ECFD69577A2D48536F3A156FB59CB76CC643BC5D3C501DBD62A6D17F641B54F9DA6434B776F97996ACDF00927571707AB03BF951D847BF51B7649B22F10F4C74854A7E8B82BCAC5A1FD8F52B46D4A4484478A597279AD309A013BA001CE8679F4B830F711B64EF91C6
	8F9C632B793FFFCAB17ED91E9C632F695E8FBD90891F0F15FC674567404FF7529DD18BC5B54F3DA2261FAF41182E3A994E53B7E3FFAF277D6591B7537775CA99EF929CE7G657C99E33AEE269C53E5812C5F9F166FD77FF960B65E477FBDAF35F141AC390DB728622985F4B97E6CCFBC0DD445D3902B4FGF2220E87823A77AA6DB9243AC67AD1D744C4DDB394F5BDB360F892676F0BE77377137D994F3F2D9B58F50137736FD6274DAEE650E8771F2AB69E83FAF27E62CFDB7DBC0831F7BB0D3D423F845AD6A372
	6536FAA1603692E3E2E7B90E09E6309F7A979C9367156318087B8B0E09BBB96FB8B84E9F81732291B8A6EE604FA50AB678BE007BF48F95E16E2D9F01F6AFAD465C5BBF82F36F652A5C9B8DF87C88FFE27ECDD5BFA603CC06DFF2F72B64327E8A73123F7AA37721EA0CD502CC5DGF2224DC6156C7292B70167BF692EAFE4BD513F3525543F60CB68DF311D03A967457D957B77C64EBB8E0E73EF017E3D857B77EA45B3FBF78410737269BEEC7DAD014676497CD9BC21C7BC57435A387F9674FFFC99E251040DDA02
	D8BAE1A716A2D6F6ABE20B0A90EB3D95E36CC55577DE42G0F3C552F392F6A3B10B1A01368175CBA15DCAA480C75CBEE066A7B1939B126CCCB0CBFF25DAAFB55A05360175C41E5CA39D610F179A5D727128B8619708164C43C0E1B02719A857C29B158E775D3B1DE8B7F06716A1E4A77CC403183A878EF982F7C736BD23C0E756B199B699D5D25180F06120DEC712DDBB8CE874A3A8D6365CA55B7AF8B812F3A4D1FF6B926FA1E2C9EE4DA7C128BD6DD73B8C0A648AF39548D2A388619488164C49B364C175B3031
	E43E368D6BBB3B6C5DBDB03D2AD8DDE9D9D33518F8AFAA897C9C18A16FB938AD50BCD125F83E6AE26E3340F937CB78BF6797F977FF55AA2982223DF6797CFEAE23B11A7693F27F99D02E4830FF7EB0201C1644BBC2D773EB2E3C5B6535F40157F23CD0010BF1F597DD4FD5C0D9686D78FC644BFC4FFC869C67461AB24B00EA01DA017AE2F17E746FAC3689FB73DE4FD6E7D71733DB1AC571ADD4383C5A6CF4B5320EED32320719BA9D503B22FE6F2B2E71C3C1E7C42C6F7893B6C7E6FBDF1B7DC77640EB330FE70B
	67C8775B5953E282CB49E6FB5E1C7B767558E98D78396CBC42F674B4C91BA6046F93678EAE96E68FA8E79C8B0D834A9947420BFCCDB6BAD69B8B7F62F8EC2CB6960E4D45D8C800322ABBB027854F8B543E107B7DA065C686ACA76A55D1407C8F3BC3DB5F039C0F38C3DB5FF7E8EDBA8A4A727E0E313F0D6F3D76403189A89AA881A885E86C5FB176FBF25866FEEF578EDFBB1D7CAEB07B78243533DB3914AB45577E495E6338FB7240F1FCF83271BD066313757798CE1EECFC0FA1E4327E9E43D912DE160759B6A5
	64BDF7B2DBEB914F5ACACF39D2FFAD2B9BDCBF2F9F241CA7E0B99187B3B7337E4E3DD39B9F9FF13C60CEEDFCDCCEEB62E5D096F2A70E0744ADCA7B69001B63FC67AE69FBAB7CB94C7CB8E1C7E0BBB9D699A7FFABE936B9BA57FBEC5C1ABBF0EC44659A4746FD397A58F8B057B8B69E4E554746E3396CDC3C5F5DE9EBA13E27887B0E30D42E13E15FF65A78F34A82FF097B457086BBB308F886776BB047DAA46CCD85761D043D134B72B4D4394369C6598FF259B57D3ECE07C39CE7CEE3BC1D5D103629BDF226B19F
	BBDDD6055F53887B0E7A627D957A7E492CD2760F741D2705FA3EF3976A7944F74454FC1F146A796ACB74FCBBCA74FC4996BD5F5396BD5FAF867E852B7C93F17C196A3B308DD0566AD79CCF20B8E64FA48AEC9245B1FB4E91778F323E588A514B12053079C040739A693D2A03141BE399DC2E38D12CDD47E9F3452D9C6F0F53660AFA8B660A90A8CB0F43F349F9FC4FCE53DDF0AD82E4820A840A819AF5971ECB3AE7783A97A86E3249A790D12FA32AFA255DA57AED1C741BF7B74C596EB61A03DCD324ED0F9A60EB
	84EA8372G05E864C47F0A6F9328E3EA4D82FD6C1DEE9073E79B447C6386B11FD2220F51912A6FA28E855F065F6D4F75CDFA496062651DC14A2DAE9DDC1CA5723515383B35F176A847936F564659DEDA37C906323AFB701C542F32BF8C70917778B3161B8D7A6BBB037EFA46203FDEB0682F3BD4FEA4008FA98378A146402533D92E681156DB256FAB9A7876DA295EE61CAAC646003D1CF8FFE2E4F779607A5ABAC8398B831693317049C2FE6D962F0D11BA0ED744EBE364DEDA3B2D063270F8FA8731EA6C0F135A
	A86FDE71DB863FB5779A650CBF69F2EFA3703581F900C200065EEB14B3EEB14899379B640CDC833E1986FCCF984443B38631DAE5902BEB8CE2F5CB40E3D648B58DD26E74C1660805FC2DE9783D5A7EDF417118FB357DBF02F2C4BC14D55EC76F2ED32D171BG9FF61FBF6344E6687788C9B681FED36EB30A1BEC43FB8FE3A45E1C11264C4211C6F20FAEB1122B12F86B613745D06E94559A28C343938447E106F2153AF3A223C809B786FE93C79A453759E0BEB651E0BED6DFE9B03F2B5473FDE540374700EF61D2
	BD5F5225FA3ED38D741DE120AFE4191E6F2CE5FA3EEF8D78DA176947550429039B9FF903145B91301C98BF4F73F56254115AF1759A47B3C6EA47D55FD49CD763202CAD814755B92A719187F8D2020817F478B55F2F9FE3424ED1BEFE736F57FE77B21863E58ADC584FDD00FB782A21AC7CFEFCBF5DED957AFE3933C25FFF3F4F55731D1F2B673B48607AA142607A665D8DFABE73C6FDBC3C291AD30D833F739E701DF76673FA464EE6EB8E1D5D0ECE3612E233FFE66FA61D1FDA4C747796455FA30B7F5B6F445347
	DB6AEFEAC34C074694FBDF83F837186F2395EBG7B1CA4B35E42F017556D596A30767490BE09BF07C57BC6F75BBD9E18E54965B3AC4CD6CF6FDE173CCA0B366672E70F1C073B79FA0F74EDDABA3F323D3C827B084E2F1FD0BB3DB6D76E17310A3FCF1A7B5B7751057F83B63A8B64BA9DD67CEDAE786DEDFF781B5A90FD7FE50977987C26E460F5289FCB0C5721ECA57AF5284E9263F5A8E709F29DCA54FD5EE6392DAD0A3F2F2A7B6D5B7D6F3F298D693AEA0BBE2F26EFD166D5310F4D4A677F521EEF3E54A67042
	3EE9B20ECBED121E9FB769F537EFB2386E36486DFA07626F7F4EB723FC37F17BEFEAC34455A5255EE37B0A5201E37B3AD263580EAE554776DFCB0DE37B76D2E5EC0B3EB07179E3991C9F6B9F40733BD877EB01638E42B6533BEBBD043D4D1FF7090363E4427E4E77B444341BB2B3884B66FBE9329EB4E566BF0858FE0ED9603806309E3251C858135C867BCE7DD0429EAC57475428F2FDCCBDD2AA4751D30A3FBBCAFF7B18BA69B735A12D819BDC1F069BDC1FA69B70BDED34C6E6502EA9866D3AE1215C165B95FF
	BFD87E5B37EB5AEFEAC35479959E4343A15E92A966BE2598CEA56C42E20C578C426E60D8DDA2DCB3A6A27637E29C938E4246DBD0DF90E1FBADB8A6C2893B193E8B92C1D8C2910E090CC43CA628DCA077439A455F377D8F447A687F01D877F7BD7898EF2F74AFCD19D98FD1BE23B64CA34C465B3A8D0E7B881BDF063221041DA8C359F0421EA8C259B442B216206CB8423EDA0A32B97FC06C4C25A85BC258059CE35F0DF19076BC497693F6AB3F46CA3619B293885BDA04F114C2D8E4914A2693F6CF9146E56DBFB1
	E652954F283C29787B44545FBEE64E7FCDEDC86DA03EA38D6D5574CFEC07AC9EB3EDF06CA16C14B29CF7610425AC44B60C22768ADA406E7D0D2363B37831E5949E4FDA70EB6B44F47BEE37253F5AC666006D16EBF03E1829BADF087BEDD7726BBCDBE70F4BE1BDE2376D14BE42E046E7F86F60E30CFD0721FE94BE8F7B06C57D9D06E7F92E924F733650733C4D230C1F671DA47DB20A56FC172FE31476F95E3CA2B6E75A67F45B556F042F60E341837CC12370997E17B9F6AFD084797AFD115A57DB797B00C55905
	E5466F1BB771DCBF9CF4440C4267144F20FA25F9291772BBB6111A6FD70DD354CB5818C45F2A4AF998FD4DAA765D97C5743D2A4207F1FF623965F25B5FC05F8C2AFCF86036D77A480E6B9E56367D13DE5A7EA56EC7A37037BC0CED7FFCB136FD9F79B6044FAB24EF190BE741D95943582689742E1DD8AF7ECE7362AFEB53D18FEB5B74CDEA539472E17FA239FD5EA0FBE38C6C4957C80258F7A6F29E116D95BE22ED1FE3966376B917573582782B9FC1BFB6D548FE1CD7C6E36411013FF9417C68D078517F08365E
	E2EE4E6C05103DF90BE5FB9D741D27C883FBCC4FE4054D18C7545F58C854590BDE0A76129F41677A6FDC4A736AA358376F114D2A515E7B965F67DEBFDA731D3E51DA1B299558373651D847F794F53C3D886D759958D3761BD16C076A6C3DD504B19EBE9A6B7888FF06FC44E89C2F9FAF50FFCBE0953D2B2DA0C93CEB248AFE6B015837FC246F7471F3D5ABE09DC974ADAE7A96CBD812F77F313DA201A7A6C976BFB1C99B1BC5B3D81962DDA14A57EFCA6F8639D575DEB9F32570B199F4E5A4E19D13AC7C1D945D0B
	24FD4D274B6F0DFD947B619F0AF70B0FD43C8FBC1EBF272F5EAB3DFC2F83722DA81FA05E61E54377DD44D54A329D813CD373D672290EFC9A2AF8DFF811417B4C56942B6B750C7C5EA55261D9AE572BCB71FE6659965F75FACFD32FFD0A778EDF9D40B38F6F5133CF4349270DABE5DD392A77228919B2FACEA26DD1DF6FE11A3BCA7EFB06621D434397F82B1B74ECFD996A37BC66CB7F8B8A1DAB0AFD6B3C005E5F54E2289377E747A95ECF7C44D25FFD7068D216F0835BBB3E1C5E51E4726947D0453B085F2D5077
	C1C6916A09FF0CE26993D9A6F30078C9AF5D6D4577401F61D1E4GFA0E677B85F8CF4A7C38A93366F1FCAE62328518E3DD0F63B38B091CB7920EE301129E276F5A554875F9B8007762D4AC423649F8BC507D795B78BBF9051EB7A84EAB1251D7717CD99B9C77811912699B3E2AFBC9110047A47BF38FBBD67D5CAC48247AA57702EA6F43D81049FD429F39892AFB6D96102976CBEE31CA2E81E4DA7D129B293A576E8219E03F64AED15DB38F8719A83F64D62B1E9B0A8519D1FE493D293A571E86B2637C12EBDBB0
	38FB435D03143BB2E0B9F16F363E0CBFFF7124761E72CE0E97AAF0B1FE9F2171DB81E521CFE21CAFDB24341F81F856D37E3453D8155F859A19CA839D423FBB78F82BFBCAFBAFFAA4471B943870FBF7B95E0BEE0332382770BA7D8A033DB7D79B6CE59DED4017EC4077FE311EAF53E00F4507866BD1C5866BD1F586FC5B8C78443B0BB57B5D4A74FCB79B6C453845E0AF466786FBB1F6996C4548B5286F4CE27D1A6F677C5AD75A472678BB79D72FC778B106FFD39B521E19C57AF65831C85FDEB34AF0CD29A285E3
	7087DAA32CC94171F48FFFEFF6889DBF440FA36978BB3E0E4D4AFF3BF6F3F39B3E56FBFE8D096B1A1AB2D62F78943C96F1537569B8033AE92F25721EB6E516BDADDF0B54BC2D3D96B9BD171D4F95B391FAF91AF8A76A34054A77F90DA9B2E53275387A27719A644B324067F21753BC356569C06799AF73F77BCB4F0A72B5C4F65DE5FA9A7BBDD2F55E0C87BC79E9FF726C0E055E4E8F99A01F750CA933G287299D917347F51A02F4499641536053886DA778C46F5922D01B693B63D826FBD749176AD2DCB07BE03
	572927F35988F67C8CFEF7713505F84D1884D8BAD06E33D0865478AC4A55F2DD2C6F9C041DCA576F7D4F629CB2F53D19E2647E81671072BB6766509C5274AC3EF32EE5116FF7441F314877BB67E296E95FB927FEC77C2B0BFC3FF3EEF7057ABD7CC3D55785227FBFD21D1F63417764E77D59EF3D12729A73C9E0EBA92F31FA4E63B697FFB2F70A599C750AF922471C2FF8EDF31353556B5AE96F6E751873700DBE966901AC0FF9422E839EF34EE61B4D421EEE74182732BF25CF5FF94C537891FE444EE31E29BA36
	30FDC10207DE12A27150316049962F177718B33F349FD69C4E41D748DBD6D86A6DD669C0476627D436F871B4B030B7C8DB786B25ADBB6DBDBDD0170A1E459EF3AE4725D7CB235AAC764AD60FF992FB632A455A83G7B4BE31E416AE135383036BDEE73F4F5F40E3FE25EA9F9C3D677CEB0E7EFB7E7C3EF3BBA2CBB4C938F31376CB51AA734032E0F9B19488A90D9514EC4D6F0415DAEEB3BFD7D8F66497B5C7B77F2F668089F367FGCA7851C17389F3E123F90A2703BF8F887952E31EA7EBEAE28FF5C8856B7B78
	EB60BFC8C87840857FFA1868C7AA44ED1EC1EE6F60CFB9259CDA3DAE73594BB3A71833F61833450E049D66AC362DG5C673B8EF618B3BDCE17508D7F26A09968E708657F97FC890C4FE31ECC417D3F472B7FA18BDA3E09C66DB33D594EDE95E8F5D89A1D7BBB5B7F2FB6123143CA54EC088E0E5737D76C5F8C9FFCD394648F9F5787499B133A3FA27F9778FCD5491FB8765E306CDC1DC35A353E7993AC8371CD112CCC9A400A3FBA7D35224D6233742F1C58FC76232323FE2A4F1FE01EG093C43AEB2774443ECE3
	9F4F5DEC19B75347706814F866FA4C9359335B6BE082201829FBC327C9DFF630C825537118FF068200DC457C838E7354EFCF6ED927F3A9C7D3ED20G521A59907DEF6AB37B50E7EC57D85ABF5E01429603FE0AAE9C3DA4EF7CC6EF4CB665372C8A734F4A46392CF66E997B2A740D5ED76577A10A67D3DFD84FCC673D86737E5770F954CF56339BAE5E3E073E3B4B6AF0F82C0E831B3BED6C5B68DB0EF86C72DE727475CCFF8F72C805226C0D754C6F1F95B27F87D0CB87887E109744C2D7GG1CC181GD0CB8182
	94G94G88G88G36FBD4AC7E109744C2D7GG1CC181G8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGFCD7GGGG
**end of data**/
}
/**
 * Return the DescriptionPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getDescriptionPanel() {
	if (ivjDescriptionPanel == null) {
		try {
			ivjDescriptionPanel = new javax.swing.JPanel();
			ivjDescriptionPanel.setName("DescriptionPanel");
			ivjDescriptionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Description"));
			ivjDescriptionPanel.setLayout(new java.awt.GridBagLayout());
			ivjDescriptionPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsDescriptionScrollPane = new java.awt.GridBagConstraints();
			constraintsDescriptionScrollPane.gridx = 0; constraintsDescriptionScrollPane.gridy = 0;
			constraintsDescriptionScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsDescriptionScrollPane.weightx = 1.0;
			constraintsDescriptionScrollPane.weighty = 1.0;
			constraintsDescriptionScrollPane.insets = new java.awt.Insets(10, 10, 10, 10);
			getDescriptionPanel().add(getDescriptionScrollPane(), constraintsDescriptionScrollPane);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjDescriptionPanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDescriptionPanel;
}
/**
 * Return the DescriptionScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getDescriptionScrollPane() {
	if (ivjDescriptionScrollPane == null) {
		try {
			ivjDescriptionScrollPane = new javax.swing.JScrollPane();
			ivjDescriptionScrollPane.setName("DescriptionScrollPane");
			ivjDescriptionScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			getDescriptionScrollPane().setViewportView(getDescriptionTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDescriptionScrollPane;
}
/**
 * Return the DescriptionTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getDescriptionTextArea() {
	if (ivjDescriptionTextArea == null) {
		try {
			ivjDescriptionTextArea = new javax.swing.JTextArea();
			ivjDescriptionTextArea.setName("DescriptionTextArea");
			ivjDescriptionTextArea.setBackground(new java.awt.Color(204,204,204));
			ivjDescriptionTextArea.setBounds(0, 0, 160, 120);
			ivjDescriptionTextArea.setEnabled(true);
			ivjDescriptionTextArea.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDescriptionTextArea;
}
/**
 * Return the EnabledAssertionList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JList getEnabledAssertionList() {
	if (ivjEnabledAssertionList == null) {
		try {
			ivjEnabledAssertionList = new javax.swing.JList();
			ivjEnabledAssertionList.setName("EnabledAssertionList");
			ivjEnabledAssertionList.setBackground(new java.awt.Color(204,204,204));
			ivjEnabledAssertionList.setBounds(0, 0, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjEnabledAssertionList;
}
/**
 * Return the EnabledAssertionPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getEnabledAssertionPanel() {
	if (ivjEnabledAssertionPanel == null) {
		try {
			ivjEnabledAssertionPanel = new javax.swing.JPanel();
			ivjEnabledAssertionPanel.setName("EnabledAssertionPanel");
			ivjEnabledAssertionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Enabled Assertion"));
			ivjEnabledAssertionPanel.setLayout(new java.awt.GridBagLayout());
			ivjEnabledAssertionPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsEnabledAssertionScrollPane = new java.awt.GridBagConstraints();
			constraintsEnabledAssertionScrollPane.gridx = 0; constraintsEnabledAssertionScrollPane.gridy = 0;
			constraintsEnabledAssertionScrollPane.gridwidth = 2;
constraintsEnabledAssertionScrollPane.gridheight = 4;
			constraintsEnabledAssertionScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsEnabledAssertionScrollPane.weightx = 1.0;
			constraintsEnabledAssertionScrollPane.weighty = 1.0;
			constraintsEnabledAssertionScrollPane.insets = new java.awt.Insets(10, 10, 10, 0);
			getEnabledAssertionPanel().add(getEnabledAssertionScrollPane(), constraintsEnabledAssertionScrollPane);

			java.awt.GridBagConstraints constraintsAddEnabledAssertionButton = new java.awt.GridBagConstraints();
			constraintsAddEnabledAssertionButton.gridx = 2; constraintsAddEnabledAssertionButton.gridy = 0;
			constraintsAddEnabledAssertionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAddEnabledAssertionButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getEnabledAssertionPanel().add(getAddEnabledAssertionButton(), constraintsAddEnabledAssertionButton);

			java.awt.GridBagConstraints constraintsRemoveEnabledAssertionButton = new java.awt.GridBagConstraints();
			constraintsRemoveEnabledAssertionButton.gridx = 2; constraintsRemoveEnabledAssertionButton.gridy = 1;
			constraintsRemoveEnabledAssertionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsRemoveEnabledAssertionButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getEnabledAssertionPanel().add(getRemoveEnabledAssertionButton(), constraintsRemoveEnabledAssertionButton);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjEnabledAssertionPanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjEnabledAssertionPanel;
}
/**
 * Return the EnabledAssertionScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getEnabledAssertionScrollPane() {
	if (ivjEnabledAssertionScrollPane == null) {
		try {
			ivjEnabledAssertionScrollPane = new javax.swing.JScrollPane();
			ivjEnabledAssertionScrollPane.setName("EnabledAssertionScrollPane");
			ivjEnabledAssertionScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			getEnabledAssertionScrollPane().setViewportView(getEnabledAssertionList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjEnabledAssertionScrollPane;
}
/**
 * Return the ExpandButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getExpandButton() {
	if (ivjExpandButton == null) {
		try {
			ivjExpandButton = new javax.swing.JButton();
			ivjExpandButton.setName("ExpandButton");
			ivjExpandButton.setMnemonic('x');
			ivjExpandButton.setText("Expand");
			ivjExpandButton.setBackground(new java.awt.Color(204,204,255));
			ivjExpandButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpandButton;
}
/**
 * Return the ExpandedPatternTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getExpandedPatternTextField() {
	if (ivjExpandedPatternTextField == null) {
		try {
			ivjExpandedPatternTextField = new javax.swing.JTextField();
			ivjExpandedPatternTextField.setName("ExpandedPatternTextField");
			ivjExpandedPatternTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjExpandedPatternTextField.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpandedPatternTextField;
}
/**
 * Return the JPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getFormulaPanel() {
	if (ivjFormulaPanel == null) {
		try {
			ivjFormulaPanel = new javax.swing.JPanel();
			ivjFormulaPanel.setName("FormulaPanel");
			ivjFormulaPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Formula"));
			ivjFormulaPanel.setLayout(new java.awt.GridBagLayout());
			ivjFormulaPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsPatternNameLabel = new java.awt.GridBagConstraints();
			constraintsPatternNameLabel.gridx = 0; constraintsPatternNameLabel.gridy = 2;
			constraintsPatternNameLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternNameLabel.insets = new java.awt.Insets(15, 10, 0, 0);
			getFormulaPanel().add(getPatternNameLabel(), constraintsPatternNameLabel);

			java.awt.GridBagConstraints constraintsPatternScopeLabel = new java.awt.GridBagConstraints();
			constraintsPatternScopeLabel.gridx = 3; constraintsPatternScopeLabel.gridy = 2;
			constraintsPatternScopeLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternScopeLabel.insets = new java.awt.Insets(15, 10, 0, 10);
			getFormulaPanel().add(getPatternScopeLabel(), constraintsPatternScopeLabel);

			java.awt.GridBagConstraints constraintsPatternNameComboBox = new java.awt.GridBagConstraints();
			constraintsPatternNameComboBox.gridx = 0; constraintsPatternNameComboBox.gridy = 3;
			constraintsPatternNameComboBox.gridwidth = 2;
			constraintsPatternNameComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternNameComboBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getFormulaPanel().add(getPatternNameComboBox(), constraintsPatternNameComboBox);

			java.awt.GridBagConstraints constraintsPatternScopeComboBox = new java.awt.GridBagConstraints();
			constraintsPatternScopeComboBox.gridx = 3; constraintsPatternScopeComboBox.gridy = 3;
			constraintsPatternScopeComboBox.gridwidth = 2;
			constraintsPatternScopeComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternScopeComboBox.insets = new java.awt.Insets(5, 10, 0, 10);
			getFormulaPanel().add(getPatternScopeComboBox(), constraintsPatternScopeComboBox);

			java.awt.GridBagConstraints constraintsPropositionTextField = new java.awt.GridBagConstraints();
			constraintsPropositionTextField.gridx = 1; constraintsPropositionTextField.gridy = 5;
			constraintsPropositionTextField.gridwidth = 4;
			constraintsPropositionTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPropositionTextField.weightx = 1.0;
			constraintsPropositionTextField.insets = new java.awt.Insets(5, 10, 0, 10);
			getFormulaPanel().add(getPropositionTextField(), constraintsPropositionTextField);

			java.awt.GridBagConstraints constraintsPropositionComboBox = new java.awt.GridBagConstraints();
			constraintsPropositionComboBox.gridx = 0; constraintsPropositionComboBox.gridy = 5;
			constraintsPropositionComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPropositionComboBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getFormulaPanel().add(getPropositionComboBox(), constraintsPropositionComboBox);

			java.awt.GridBagConstraints constraintsPropositionLabel = new java.awt.GridBagConstraints();
			constraintsPropositionLabel.gridx = 0; constraintsPropositionLabel.gridy = 4;
			constraintsPropositionLabel.gridwidth = 2;
			constraintsPropositionLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPropositionLabel.weightx = 1.0;
			constraintsPropositionLabel.insets = new java.awt.Insets(15, 10, 0, 0);
			getFormulaPanel().add(getPropositionLabel(), constraintsPropositionLabel);

			java.awt.GridBagConstraints constraintsExpandButton = new java.awt.GridBagConstraints();
			constraintsExpandButton.gridx = 0; constraintsExpandButton.gridy = 6;
			constraintsExpandButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsExpandButton.insets = new java.awt.Insets(15, 10, 0, 0);
			getFormulaPanel().add(getExpandButton(), constraintsExpandButton);

			java.awt.GridBagConstraints constraintsExpandedPatternTextField = new java.awt.GridBagConstraints();
			constraintsExpandedPatternTextField.gridx = 1; constraintsExpandedPatternTextField.gridy = 6;
			constraintsExpandedPatternTextField.gridwidth = 4;
			constraintsExpandedPatternTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsExpandedPatternTextField.weightx = 1.0;
			constraintsExpandedPatternTextField.insets = new java.awt.Insets(15, 10, 0, 10);
			getFormulaPanel().add(getExpandedPatternTextField(), constraintsExpandedPatternTextField);

			java.awt.GridBagConstraints constraintsShowErrorButton = new java.awt.GridBagConstraints();
			constraintsShowErrorButton.gridx = 3; constraintsShowErrorButton.gridy = 7;
			constraintsShowErrorButton.gridwidth = 2;
			constraintsShowErrorButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsShowErrorButton.weightx = 1.0;
			constraintsShowErrorButton.weighty = 1.0;
			constraintsShowErrorButton.insets = new java.awt.Insets(15, 10, 10, 10);
			getFormulaPanel().add(getShowErrorButton(), constraintsShowErrorButton);

			java.awt.GridBagConstraints constraintsShowMappingButton = new java.awt.GridBagConstraints();
			constraintsShowMappingButton.gridx = 0; constraintsShowMappingButton.gridy = 7;
			constraintsShowMappingButton.gridwidth = 2;
			constraintsShowMappingButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsShowMappingButton.weightx = 1.0;
			constraintsShowMappingButton.weighty = 1.0;
			constraintsShowMappingButton.insets = new java.awt.Insets(15, 10, 10, 0);
			getFormulaPanel().add(getShowMappingButton(), constraintsShowMappingButton);

			java.awt.GridBagConstraints constraintsQuantifierLabel = new java.awt.GridBagConstraints();
			constraintsQuantifierLabel.gridx = 0; constraintsQuantifierLabel.gridy = 0;
			constraintsQuantifierLabel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsQuantifierLabel.insets = new java.awt.Insets(10, 10, 0, 0);
			getFormulaPanel().add(getQuantifierLabel(), constraintsQuantifierLabel);

			java.awt.GridBagConstraints constraintsQuantificationScrollPane = new java.awt.GridBagConstraints();
			constraintsQuantificationScrollPane.gridx = 1; constraintsQuantificationScrollPane.gridy = 0;
			constraintsQuantificationScrollPane.gridwidth = 4;
constraintsQuantificationScrollPane.gridheight = 2;
			constraintsQuantificationScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsQuantificationScrollPane.weightx = 1.0;
			constraintsQuantificationScrollPane.weighty = 1.0;
			constraintsQuantificationScrollPane.insets = new java.awt.Insets(10, 10, 0, 10);
			getFormulaPanel().add(getQuantificationScrollPane(), constraintsQuantificationScrollPane);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjFormulaPanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFormulaPanel;
}
/**
 * Return the ImportAssertionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getImportAssertionButton() {
	if (ivjImportAssertionButton == null) {
		try {
			ivjImportAssertionButton = new javax.swing.JButton();
			ivjImportAssertionButton.setName("ImportAssertionButton");
			ivjImportAssertionButton.setText("Add Assertion");
			ivjImportAssertionButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportAssertionButton;
}
/**
 * Return the ImportAssertionSetButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getImportAssertionSetButton() {
	if (ivjImportAssertionSetButton == null) {
		try {
			ivjImportAssertionSetButton = new javax.swing.JButton();
			ivjImportAssertionSetButton.setName("ImportAssertionSetButton");
			ivjImportAssertionSetButton.setText("Add Assertion Set");
			ivjImportAssertionSetButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportAssertionSetButton;
}
/**
 * Return the ImportList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getImportList() {
	if (ivjImportList == null) {
		try {
			ivjImportList = new javax.swing.JList();
			ivjImportList.setName("ImportList");
			ivjImportList.setBackground(new java.awt.Color(204,204,204));
			ivjImportList.setBounds(0, 0, 157, 117);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportList;
}
/**
 * Return the ImportPackageButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getImportPackageButton() {
	if (ivjImportPackageButton == null) {
		try {
			ivjImportPackageButton = new javax.swing.JButton();
			ivjImportPackageButton.setName("ImportPackageButton");
			ivjImportPackageButton.setText("Add Package");
			ivjImportPackageButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportPackageButton;
}
/**
 * Return the ImportPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getImportPanel() {
	if (ivjImportPanel == null) {
		try {
			ivjImportPanel = new javax.swing.JPanel();
			ivjImportPanel.setName("ImportPanel");
			ivjImportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Import"));
			ivjImportPanel.setLayout(new java.awt.GridBagLayout());
			ivjImportPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsImportScrollPane = new java.awt.GridBagConstraints();
			constraintsImportScrollPane.gridx = 0; constraintsImportScrollPane.gridy = 0;
constraintsImportScrollPane.gridheight = 8;
			constraintsImportScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsImportScrollPane.weightx = 1.0;
			constraintsImportScrollPane.weighty = 1.0;
			constraintsImportScrollPane.insets = new java.awt.Insets(10, 10, 10, 0);
			getImportPanel().add(getImportScrollPane(), constraintsImportScrollPane);

			java.awt.GridBagConstraints constraintsImportTypeButton = new java.awt.GridBagConstraints();
			constraintsImportTypeButton.gridx = 1; constraintsImportTypeButton.gridy = 0;
			constraintsImportTypeButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsImportTypeButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getImportPanel().add(getImportTypeButton(), constraintsImportTypeButton);

			java.awt.GridBagConstraints constraintsImportPackageButton = new java.awt.GridBagConstraints();
			constraintsImportPackageButton.gridx = 1; constraintsImportPackageButton.gridy = 1;
			constraintsImportPackageButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsImportPackageButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getImportPanel().add(getImportPackageButton(), constraintsImportPackageButton);

			java.awt.GridBagConstraints constraintsImportPredicateButton = new java.awt.GridBagConstraints();
			constraintsImportPredicateButton.gridx = 1; constraintsImportPredicateButton.gridy = 2;
			constraintsImportPredicateButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsImportPredicateButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getImportPanel().add(getImportPredicateButton(), constraintsImportPredicateButton);

			java.awt.GridBagConstraints constraintsImportPredicateSetButton = new java.awt.GridBagConstraints();
			constraintsImportPredicateSetButton.gridx = 1; constraintsImportPredicateSetButton.gridy = 3;
			constraintsImportPredicateSetButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsImportPredicateSetButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getImportPanel().add(getImportPredicateSetButton(), constraintsImportPredicateSetButton);

			java.awt.GridBagConstraints constraintsImportAssertionButton = new java.awt.GridBagConstraints();
			constraintsImportAssertionButton.gridx = 1; constraintsImportAssertionButton.gridy = 4;
			constraintsImportAssertionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsImportAssertionButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getImportPanel().add(getImportAssertionButton(), constraintsImportAssertionButton);

			java.awt.GridBagConstraints constraintsImportAssertionSetButton = new java.awt.GridBagConstraints();
			constraintsImportAssertionSetButton.gridx = 1; constraintsImportAssertionSetButton.gridy = 5;
			constraintsImportAssertionSetButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsImportAssertionSetButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getImportPanel().add(getImportAssertionSetButton(), constraintsImportAssertionSetButton);

			java.awt.GridBagConstraints constraintsRemoveImportButton = new java.awt.GridBagConstraints();
			constraintsRemoveImportButton.gridx = 1; constraintsRemoveImportButton.gridy = 6;
			constraintsRemoveImportButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsRemoveImportButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getImportPanel().add(getRemoveImportButton(), constraintsRemoveImportButton);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjImportPanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportPanel;
}
/**
 * Return the ImportPredicateButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getImportPredicateButton() {
	if (ivjImportPredicateButton == null) {
		try {
			ivjImportPredicateButton = new javax.swing.JButton();
			ivjImportPredicateButton.setName("ImportPredicateButton");
			ivjImportPredicateButton.setText("Add Predicate");
			ivjImportPredicateButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportPredicateButton;
}
/**
 * Return the ImportPredicateSetButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getImportPredicateSetButton() {
	if (ivjImportPredicateSetButton == null) {
		try {
			ivjImportPredicateSetButton = new javax.swing.JButton();
			ivjImportPredicateSetButton.setName("ImportPredicateSetButton");
			ivjImportPredicateSetButton.setText("Add Predicate Set");
			ivjImportPredicateSetButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportPredicateSetButton;
}
/**
 * Return the ImportScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getImportScrollPane() {
	if (ivjImportScrollPane == null) {
		try {
			ivjImportScrollPane = new javax.swing.JScrollPane();
			ivjImportScrollPane.setName("ImportScrollPane");
			ivjImportScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			getImportScrollPane().setViewportView(getImportList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportScrollPane;
}
/**
 * Return the ImportTypeButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getImportTypeButton() {
	if (ivjImportTypeButton == null) {
		try {
			ivjImportTypeButton = new javax.swing.JButton();
			ivjImportTypeButton.setName("ImportTypeButton");
			ivjImportTypeButton.setText("Add Type");
			ivjImportTypeButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjImportTypeButton;
}
/**
 * Return the NewButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getNewButton() {
	if (ivjNewButton == null) {
		try {
			ivjNewButton = new javax.swing.JButton();
			ivjNewButton.setName("NewButton");
			ivjNewButton.setMnemonic('n');
			ivjNewButton.setText("New");
			ivjNewButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNewButton;
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
			ivjOkButton.setText("Ok");
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
 * Return the OpenButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOpenButton() {
	if (ivjOpenButton == null) {
		try {
			ivjOpenButton = new javax.swing.JButton();
			ivjOpenButton.setName("OpenButton");
			ivjOpenButton.setMnemonic('o');
			ivjOpenButton.setText("Open");
			ivjOpenButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOpenButton;
}
/**
 * Return the JComboBox1 property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JComboBox getPatternNameComboBox() {
	if (ivjPatternNameComboBox == null) {
		try {
			ivjPatternNameComboBox = new javax.swing.JComboBox();
			ivjPatternNameComboBox.setName("PatternNameComboBox");
			ivjPatternNameComboBox.setEnabled(false);
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
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPatternNameLabel() {
	if (ivjPatternNameLabel == null) {
		try {
			ivjPatternNameLabel = new javax.swing.JLabel();
			ivjPatternNameLabel.setName("PatternNameLabel");
			ivjPatternNameLabel.setText("Pattern Name:");
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
 * Return the JComboBox2 property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JComboBox getPatternScopeComboBox() {
	if (ivjPatternScopeComboBox == null) {
		try {
			ivjPatternScopeComboBox = new javax.swing.JComboBox();
			ivjPatternScopeComboBox.setName("PatternScopeComboBox");
			ivjPatternScopeComboBox.setEnabled(false);
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
 * Return the JLabel2 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPatternScopeLabel() {
	if (ivjPatternScopeLabel == null) {
		try {
			ivjPatternScopeLabel = new javax.swing.JLabel();
			ivjPatternScopeLabel.setName("PatternScopeLabel");
			ivjPatternScopeLabel.setText("Pattern Scope:");
			ivjPatternScopeLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternScopeLabel;
}
/**
 * Return the PropertyManagerContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getPropertyManagerContentPane() {
	if (ivjPropertyManagerContentPane == null) {
		try {
			ivjPropertyManagerContentPane = new javax.swing.JPanel();
			ivjPropertyManagerContentPane.setName("PropertyManagerContentPane");
			ivjPropertyManagerContentPane.setBorder(new javax.swing.border.EtchedBorder());
			ivjPropertyManagerContentPane.setLayout(new java.awt.BorderLayout());
			ivjPropertyManagerContentPane.setBackground(new java.awt.Color(204,204,255));
			getPropertyManagerContentPane().add(getOkButton(), "South");
			getPropertyManagerContentPane().add(getPropertyManagerTabbedPane(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropertyManagerContentPane;
}
/**
 * Return the PropertyManagerTabbedPane property value.
 * @return javax.swing.JTabbedPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JTabbedPane getPropertyManagerTabbedPane() {
	if (ivjPropertyManagerTabbedPane == null) {
		try {
			ivjPropertyManagerTabbedPane = new javax.swing.JTabbedPane();
			ivjPropertyManagerTabbedPane.setName("PropertyManagerTabbedPane");
			ivjPropertyManagerTabbedPane.insertTab("Property", null, getPropertyPanel(), null, 0);
			ivjPropertyManagerTabbedPane.insertTab("Temporal Logic", null, getTLPanel(), null, 1);
			ivjPropertyManagerTabbedPane.insertTab("Assertion", null, getAssertionPanel(), null, 2);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropertyManagerTabbedPane;
}
/**
 * Return the SummaryPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JPanel getPropertyPanel() {
	if (ivjPropertyPanel == null) {
		try {
			ivjPropertyPanel = new javax.swing.JPanel();
			ivjPropertyPanel.setName("PropertyPanel");
			ivjPropertyPanel.setLayout(new java.awt.GridBagLayout());
			ivjPropertyPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsOpenButton = new java.awt.GridBagConstraints();
			constraintsOpenButton.gridx = 1; constraintsOpenButton.gridy = 2;
			constraintsOpenButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsOpenButton.weightx = 1.0;
			constraintsOpenButton.insets = new java.awt.Insets(15, 10, 10, 0);
			getPropertyPanel().add(getOpenButton(), constraintsOpenButton);

			java.awt.GridBagConstraints constraintsSaveButton = new java.awt.GridBagConstraints();
			constraintsSaveButton.gridx = 3; constraintsSaveButton.gridy = 2;
			constraintsSaveButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSaveButton.weightx = 1.0;
			constraintsSaveButton.insets = new java.awt.Insets(15, 10, 10, 10);
			getPropertyPanel().add(getSaveButton(), constraintsSaveButton);

			java.awt.GridBagConstraints constraintsImportPanel = new java.awt.GridBagConstraints();
			constraintsImportPanel.gridx = 0; constraintsImportPanel.gridy = 1;
			constraintsImportPanel.gridwidth = 4;
			constraintsImportPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsImportPanel.weightx = 1.0;
			constraintsImportPanel.weighty = 1.0;
			constraintsImportPanel.insets = new java.awt.Insets(15, 9, 0, 8);
			getPropertyPanel().add(getImportPanel(), constraintsImportPanel);

			java.awt.GridBagConstraints constraintsSaveAsButton = new java.awt.GridBagConstraints();
			constraintsSaveAsButton.gridx = 2; constraintsSaveAsButton.gridy = 2;
			constraintsSaveAsButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSaveAsButton.weightx = 1.0;
			constraintsSaveAsButton.insets = new java.awt.Insets(15, 10, 10, 0);
			getPropertyPanel().add(getSaveAsButton(), constraintsSaveAsButton);

			java.awt.GridBagConstraints constraintsDescriptionPanel = new java.awt.GridBagConstraints();
			constraintsDescriptionPanel.gridx = 0; constraintsDescriptionPanel.gridy = 0;
			constraintsDescriptionPanel.gridwidth = 4;
			constraintsDescriptionPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsDescriptionPanel.weightx = 1.0;
			constraintsDescriptionPanel.weighty = 1.0;
			constraintsDescriptionPanel.insets = new java.awt.Insets(10, 9, 0, 8);
			getPropertyPanel().add(getDescriptionPanel(), constraintsDescriptionPanel);

			java.awt.GridBagConstraints constraintsNewButton = new java.awt.GridBagConstraints();
			constraintsNewButton.gridx = 0; constraintsNewButton.gridy = 2;
			constraintsNewButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsNewButton.weightx = 1.0;
			constraintsNewButton.insets = new java.awt.Insets(15, 10, 10, 0);
			getPropertyPanel().add(getNewButton(), constraintsNewButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropertyPanel;
}
/**
 * Return the PropositionComboBox property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JComboBox getPropositionComboBox() {
	if (ivjPropositionComboBox == null) {
		try {
			ivjPropositionComboBox = new javax.swing.JComboBox();
			ivjPropositionComboBox.setName("PropositionComboBox");
			ivjPropositionComboBox.setEnabled(false);
			// user code begin {1}
			ivjPropositionComboBox.addItem(" ");
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropositionComboBox;
}
/**
 * Return the JLabel4 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPropositionLabel() {
	if (ivjPropositionLabel == null) {
		try {
			ivjPropositionLabel = new javax.swing.JLabel();
			ivjPropositionLabel.setName("PropositionLabel");
			ivjPropositionLabel.setText("Proposition:");
			ivjPropositionLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropositionLabel;
}
/**
 * Return the PropositionPopupMenu property value.
 * @return javax.swing.JPopupMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPopupMenu getPropositionPopupMenu() {
	if (ivjPropositionPopupMenu == null) {
		try {
			ivjPropositionPopupMenu = new javax.swing.JPopupMenu();
			ivjPropositionPopupMenu.setName("PropositionPopupMenu");
			ivjPropositionPopupMenu.add(getAddPredicateMenuItem());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropositionPopupMenu;
}
/**
 * Return the JTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JTextField getPropositionTextField() {
	if (ivjPropositionTextField == null) {
		try {
			ivjPropositionTextField = new javax.swing.JTextField();
			ivjPropositionTextField.setName("PropositionTextField");
			ivjPropositionTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjPropositionTextField.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPropositionTextField;
}
/**
 * Return the quantificationScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getQuantificationScrollPane() {
	if (ivjQuantificationScrollPane == null) {
		try {
			ivjQuantificationScrollPane = new javax.swing.JScrollPane();
			ivjQuantificationScrollPane.setName("QuantificationScrollPane");
			ivjQuantificationScrollPane.setPreferredSize(new java.awt.Dimension(3, 50));
			ivjQuantificationScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjQuantificationScrollPane.setMinimumSize(new java.awt.Dimension(22, 50));
			getQuantificationScrollPane().setViewportView(getQuantificationTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationScrollPane;
}
/**
 * Return the quantificationTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getQuantificationTextArea() {
	if (ivjQuantificationTextArea == null) {
		try {
			ivjQuantificationTextArea = new javax.swing.JTextArea();
			ivjQuantificationTextArea.setName("QuantificationTextArea");
			ivjQuantificationTextArea.setBorder(new javax.swing.border.CompoundBorder());
			ivjQuantificationTextArea.setBackground(new java.awt.Color(204,204,204));
			ivjQuantificationTextArea.setBounds(0, 0, 306, 27);
			ivjQuantificationTextArea.setEditable(false);
			ivjQuantificationTextArea.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationTextArea;
}
/**
 * Return the QuantifierLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getQuantifierLabel() {
	if (ivjQuantifierLabel == null) {
		try {
			ivjQuantifierLabel = new javax.swing.JLabel();
			ivjQuantifierLabel.setName("QuantifierLabel");
			ivjQuantifierLabel.setText("Quantifications:");
			ivjQuantifierLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantifierLabel;
}
/**
 * Return the RemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveAssertionButton() {
	if (ivjRemoveAssertionButton == null) {
		try {
			ivjRemoveAssertionButton = new javax.swing.JButton();
			ivjRemoveAssertionButton.setName("RemoveAssertionButton");
			ivjRemoveAssertionButton.setText("Remove");
			ivjRemoveAssertionButton.setBackground(new java.awt.Color(204,204,255));
			ivjRemoveAssertionButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveAssertionButton;
}
/**
 * Return the RemoveEnabledButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveEnabledAssertionButton() {
	if (ivjRemoveEnabledAssertionButton == null) {
		try {
			ivjRemoveEnabledAssertionButton = new javax.swing.JButton();
			ivjRemoveEnabledAssertionButton.setName("RemoveEnabledAssertionButton");
			ivjRemoveEnabledAssertionButton.setText("Remove");
			ivjRemoveEnabledAssertionButton.setBackground(new java.awt.Color(204,204,255));
			ivjRemoveEnabledAssertionButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveEnabledAssertionButton;
}
/**
 * Return the RemoveImportButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveImportButton() {
	if (ivjRemoveImportButton == null) {
		try {
			ivjRemoveImportButton = new javax.swing.JButton();
			ivjRemoveImportButton.setName("RemoveImportButton");
			ivjRemoveImportButton.setMnemonic('r');
			ivjRemoveImportButton.setText("Remove");
			ivjRemoveImportButton.setBackground(new java.awt.Color(204,204,255));
			ivjRemoveImportButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveImportButton;
}
/**
 * Return the JButton2 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveTLButton() {
	if (ivjRemoveTLButton == null) {
		try {
			ivjRemoveTLButton = new javax.swing.JButton();
			ivjRemoveTLButton.setName("RemoveTLButton");
			ivjRemoveTLButton.setText("Remove");
			ivjRemoveTLButton.setBackground(new java.awt.Color(204,204,255));
			ivjRemoveTLButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveTLButton;
}
/**
 * Return the SaveAsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSaveAsButton() {
	if (ivjSaveAsButton == null) {
		try {
			ivjSaveAsButton = new javax.swing.JButton();
			ivjSaveAsButton.setName("SaveAsButton");
			ivjSaveAsButton.setMnemonic('a');
			ivjSaveAsButton.setText("Save as");
			ivjSaveAsButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveAsButton;
}
/**
 * Return the SaveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSaveButton() {
	if (ivjSaveButton == null) {
		try {
			ivjSaveButton = new javax.swing.JButton();
			ivjSaveButton.setName("SaveButton");
			ivjSaveButton.setMnemonic('s');
			ivjSaveButton.setText("Save");
			ivjSaveButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveButton;
}
/**
 * Return the ShowErrorButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getShowErrorButton() {
	if (ivjShowErrorButton == null) {
		try {
			ivjShowErrorButton = new javax.swing.JButton();
			ivjShowErrorButton.setName("ShowErrorButton");
			ivjShowErrorButton.setMnemonic('e');
			ivjShowErrorButton.setText("Show Error Message(s)");
			ivjShowErrorButton.setBackground(new java.awt.Color(204,204,255));
			ivjShowErrorButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowErrorButton;
}
/**
 * Return the ShowMappingButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getShowMappingButton() {
	if (ivjShowMappingButton == null) {
		try {
			ivjShowMappingButton = new javax.swing.JButton();
			ivjShowMappingButton.setName("ShowMappingButton");
			ivjShowMappingButton.setMnemonic('m');
			ivjShowMappingButton.setText("Show Mapping");
			ivjShowMappingButton.setBackground(new java.awt.Color(204,204,255));
			ivjShowMappingButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowMappingButton;
}
/**
 * Return the TLDescriptionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getTLDescriptionButton() {
	if (ivjTLDescriptionButton == null) {
		try {
			ivjTLDescriptionButton = new javax.swing.JButton();
			ivjTLDescriptionButton.setName("TLDescriptionButton");
			ivjTLDescriptionButton.setMnemonic('d');
			ivjTLDescriptionButton.setText("Description");
			ivjTLDescriptionButton.setBackground(new java.awt.Color(204,204,255));
			ivjTLDescriptionButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTLDescriptionButton;
}
/**
 * Return the TLList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getTLList() {
	if (ivjTLList == null) {
		try {
			ivjTLList = new javax.swing.JList();
			ivjTLList.setName("TLList");
			ivjTLList.setBackground(new java.awt.Color(204,204,204));
			ivjTLList.setBounds(0, 0, 160, 120);
			ivjTLList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			// user code begin {1}
			ivjTLList.setCellRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof TemporalLogicProperty) {
						TemporalLogicProperty tlp = (TemporalLogicProperty) value;
						setText(tlp.getName());
						if (BUI.property.isActivated(tlp)) {
							setIcon(IconLibrary.arrow);
						} else {
							setIcon(IconLibrary.earrow);
						}
					}
					return this;
				}
			});
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTLList;
}
/**
 * Return the TLNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getTLNameLabel() {
	if (ivjTLNameLabel == null) {
		try {
			ivjTLNameLabel = new javax.swing.JLabel();
			ivjTLNameLabel.setName("TLNameLabel");
			ivjTLNameLabel.setDisplayedMnemonic('n');
			ivjTLNameLabel.setText("Name:");
			ivjTLNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTLNameLabel;
}
/**
 * Return the TLNameTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getTLNameTextField() {
	if (ivjTLNameTextField == null) {
		try {
			ivjTLNameTextField = new javax.swing.JTextField();
			ivjTLNameTextField.setName("TLNameTextField");
			ivjTLNameTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjTLNameTextField.setEditable(false);
			ivjTLNameTextField.setFocusAccelerator('n');
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTLNameTextField;
}
/**
 * Return the TLPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JPanel getTLPanel() {
	if (ivjTLPanel == null) {
		try {
			ivjTLPanel = new javax.swing.JPanel();
			ivjTLPanel.setName("TLPanel");
			ivjTLPanel.setLayout(new java.awt.GridBagLayout());
			ivjTLPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsTLScrollPane = new java.awt.GridBagConstraints();
			constraintsTLScrollPane.gridx = 0; constraintsTLScrollPane.gridy = 0;
constraintsTLScrollPane.gridheight = 7;
			constraintsTLScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsTLScrollPane.weightx = 1.0;
			constraintsTLScrollPane.weighty = 1.0;
			constraintsTLScrollPane.insets = new java.awt.Insets(10, 10, 0, 0);
			getTLPanel().add(getTLScrollPane(), constraintsTLScrollPane);

			java.awt.GridBagConstraints constraintsAddTLButton = new java.awt.GridBagConstraints();
			constraintsAddTLButton.gridx = 1; constraintsAddTLButton.gridy = 1;
			constraintsAddTLButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAddTLButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getTLPanel().add(getAddTLButton(), constraintsAddTLButton);

			java.awt.GridBagConstraints constraintsRemoveTLButton = new java.awt.GridBagConstraints();
			constraintsRemoveTLButton.gridx = 1; constraintsRemoveTLButton.gridy = 2;
			constraintsRemoveTLButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsRemoveTLButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getTLPanel().add(getRemoveTLButton(), constraintsRemoveTLButton);

			java.awt.GridBagConstraints constraintsFormulaPanel = new java.awt.GridBagConstraints();
			constraintsFormulaPanel.gridx = 0; constraintsFormulaPanel.gridy = 7;
			constraintsFormulaPanel.gridwidth = 2;
			constraintsFormulaPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFormulaPanel.weightx = 1.0;
			constraintsFormulaPanel.weighty = 1.0;
			constraintsFormulaPanel.insets = new java.awt.Insets(15, 9, 10, 7);
			getTLPanel().add(getFormulaPanel(), constraintsFormulaPanel);

			java.awt.GridBagConstraints constraintsActivateTLButton = new java.awt.GridBagConstraints();
			constraintsActivateTLButton.gridx = 1; constraintsActivateTLButton.gridy = 0;
			constraintsActivateTLButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsActivateTLButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getTLPanel().add(getActivateTLButton(), constraintsActivateTLButton);

			java.awt.GridBagConstraints constraintsTLNameLabel = new java.awt.GridBagConstraints();
			constraintsTLNameLabel.gridx = 1; constraintsTLNameLabel.gridy = 3;
			constraintsTLNameLabel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsTLNameLabel.insets = new java.awt.Insets(15, 10, 0, 10);
			getTLPanel().add(getTLNameLabel(), constraintsTLNameLabel);

			java.awt.GridBagConstraints constraintsTLNameTextField = new java.awt.GridBagConstraints();
			constraintsTLNameTextField.gridx = 1; constraintsTLNameTextField.gridy = 4;
			constraintsTLNameTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsTLNameTextField.insets = new java.awt.Insets(5, 10, 0, 10);
			getTLPanel().add(getTLNameTextField(), constraintsTLNameTextField);

			java.awt.GridBagConstraints constraintsTLDescriptionButton = new java.awt.GridBagConstraints();
			constraintsTLDescriptionButton.gridx = 1; constraintsTLDescriptionButton.gridy = 6;
			constraintsTLDescriptionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsTLDescriptionButton.insets = new java.awt.Insets(0, 10, 0, 10);
			getTLPanel().add(getTLDescriptionButton(), constraintsTLDescriptionButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTLPanel;
}
/**
 * Return the TLScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getTLScrollPane() {
	if (ivjTLScrollPane == null) {
		try {
			ivjTLScrollPane = new javax.swing.JScrollPane();
			ivjTLScrollPane.setName("TLScrollPane");
			ivjTLScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			getTLScrollPane().setViewportView(getTLList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTLScrollPane;
}
/**
 * Comment
 */
public void gotoAssertionDefinitionButton_ActionEvents() {
	try {
		Assertion p = AssertionSet.getAssertion((Name) getEnabledAssertionList().getSelectedValue());
		Annotation a = p.getAnnotation();
		if (a instanceof LabeledStmtAnnotation) {
			a = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
		}
		BUI.sessionPane.select(a, p);
	} catch (Exception e) {
	}
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
 * Comment
 */
public void importAssertionButton_ActionEvents() {
	Vector v = new Vector();
	for (Enumeration e = AssertionSet.getAssertionSetTable().elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((AssertionSet) e.nextElement()).getAssertionTable().elements(); e2.hasMoreElements();) {
			v.add(e2.nextElement());
		}
	}
	int size = v.size();
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = ((Assertion) e.nextElement()).getName();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose assertion", "Import Assertion", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			try {
				Assertion a = AssertionSet.getAssertion((Name) selectedValue);
				BUI.property.importAssertion(a.getName());
				updateImportList();
				getImportList().setSelectedValue(a, true);
				isClean = false;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no assertions to choose from", "Import Assertion", JOptionPane.INFORMATION_MESSAGE); 
	}
}
/**
 * Comment
 */
public void importAssertionSetButton_ActionEvents() {
	int size = AssertionSet.getAssertionSetTable().size();
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = AssertionSet.getAssertionSetTable().elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = ((AssertionSet) e.nextElement()).getName();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose assertion set", "Import Assertion Set", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			try {
				AssertionSet as = AssertionSet.getAssertionSet((Name) selectedValue);
				BUI.property.importAssertionSet(as.getName());
				updateImportList();
				getImportList().setSelectedValue(as, true);
				isClean = false;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no assertion sets to choose from", "Import Assertion Set", JOptionPane.INFORMATION_MESSAGE); 
	}
}
/**
 * Comment
 */
public void importList_ListSelectionEvents() {
	getRemoveImportButton().setEnabled(true);
}
/**
 * Comment
 */
public void importPackageButton_ActionEvents() {
	Vector v = new Vector();
	for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements();) {
		try {
			Name cName = new Name(((SootClass) e.nextElement()).getName());
			if (!cName.isSimpleName()) {
				Package p = Package.getPackage(cName.getSuperName());
				if (!v.contains(p)) {
					v.add(p);
				}
			}
		} catch (Exception ex) {
		}
	}
	int size = v.size();
	
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = e.nextElement();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose package", "Import Package", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			try {
				Package p = (Package) selectedValue;
				BUI.property.importPackage(p.getName()); 
				updateImportList();
				getImportList().setSelectedValue(p, true);
				isClean = false;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no packages to choose from", "Import Package", JOptionPane.INFORMATION_MESSAGE); 
	}
}
/**
 * Comment
 */
public void importPredicateButton_ActionEvents() {
	Vector v = new Vector();
	for (Enumeration e = PredicateSet.getPredicateSetTable().elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((PredicateSet) e.nextElement()).getPredicateTable().elements(); e2.hasMoreElements();) {
			v.add(e2.nextElement());
		}
	}
	int size = v.size();
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = ((Predicate) e.nextElement()).getName();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose predicate", "Import Predicate", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			try {
				Predicate p = PredicateSet.getPredicate((Name) selectedValue);
				BUI.property.importPredicate(p.getName());
				updateImportList();
				getImportList().setSelectedValue(p, true);
				isClean = false;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no predicates to choose from", "Import Predicate", JOptionPane.INFORMATION_MESSAGE); 
	}
}
/**
 * Comment
 */
public void importPredicateSetButton_ActionEvents() {
	int size = PredicateSet.getPredicateSetTable().size();
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = PredicateSet.getPredicateSetTable().elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = ((PredicateSet) e.nextElement()).getName();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose predicate set", "Import Predicate Set", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			try {
				PredicateSet ps = PredicateSet.getPredicateSet((Name) selectedValue);
				BUI.property.importPredicateSet(ps.getName());
				updateImportList();
				getImportList().setSelectedValue(ps, true);
				isClean = false;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no predicate sets to choose from", "Import Predicate Set", JOptionPane.INFORMATION_MESSAGE); 
	}
}
/**
 * Comment
 */
public void importTypeButton_ActionEvents() {
	int size = CompilationManager.getCompiledClasses().size();
	if (size > 0) {
		Object[] possibleValues = new Object[size];
		int i = 0;
		for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements(); i++) {
			possibleValues[i] = e.nextElement();
		}
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose type", "Import Type", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
		if (selectedValue != null) {
			BUI.property.importType(new Name(((SootClass) selectedValue).getName()));
			updateImportList();
			getImportList().setSelectedValue(selectedValue, true);
			isClean = false;
		}
	} else {
		JOptionPane.showMessageDialog(null, "There are no types to choose from", "Import Type", JOptionPane.INFORMATION_MESSAGE); 
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
	getImportTypeButton().addActionListener(ivjEventHandler);
	getImportPackageButton().addActionListener(ivjEventHandler);
	getImportPredicateButton().addActionListener(ivjEventHandler);
	getImportPredicateSetButton().addActionListener(ivjEventHandler);
	getImportAssertionButton().addActionListener(ivjEventHandler);
	getImportAssertionSetButton().addActionListener(ivjEventHandler);
	getRemoveImportButton().addActionListener(ivjEventHandler);
	getImportList().addListSelectionListener(ivjEventHandler);
	getAddAssertionButton().addActionListener(ivjEventHandler);
	getRemoveAssertionButton().addActionListener(ivjEventHandler);
	getActivateAssertionButton().addActionListener(ivjEventHandler);
	getAssertionList().addListSelectionListener(ivjEventHandler);
	getAddEnabledAssertionButton().addActionListener(ivjEventHandler);
	getRemoveEnabledAssertionButton().addActionListener(ivjEventHandler);
	getEnabledAssertionList().addListSelectionListener(ivjEventHandler);
	getAssertionNameTextField().addKeyListener(ivjEventHandler);
	getAssertionNameTextField().addFocusListener(ivjEventHandler);
	getTLList().addListSelectionListener(ivjEventHandler);
	getAddTLButton().addActionListener(ivjEventHandler);
	getRemoveTLButton().addActionListener(ivjEventHandler);
	getActivateTLButton().addActionListener(ivjEventHandler);
	getTLNameTextField().addKeyListener(ivjEventHandler);
	getTLNameTextField().addFocusListener(ivjEventHandler);
	getPatternNameComboBox().addActionListener(ivjEventHandler);
	getPatternScopeComboBox().addActionListener(ivjEventHandler);
	getPropositionComboBox().addActionListener(ivjEventHandler);
	getPropositionTextField().addKeyListener(ivjEventHandler);
	getPropositionTextField().addFocusListener(ivjEventHandler);
	getExpandButton().addActionListener(ivjEventHandler);
	getShowErrorButton().addActionListener(ivjEventHandler);
	getShowMappingButton().addActionListener(ivjEventHandler);
	getNewButton().addActionListener(ivjEventHandler);
	getOpenButton().addActionListener(ivjEventHandler);
	getSaveAsButton().addActionListener(ivjEventHandler);
	getSaveButton().addActionListener(ivjEventHandler);
	getPropositionTextField().addMouseListener(ivjEventHandler);
	getAddPredicateMenuItem().addActionListener(ivjEventHandler);
	getQuantificationTextArea().addKeyListener(ivjEventHandler);
	getQuantificationTextArea().addFocusListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("PropertyManager");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(492, 636);
		setTitle("Property Manager");
		setContentPane(getPropertyManagerContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
		setTitle("Property Manager<Untitled>");
	// user code end
}
	public boolean isDialogClean() { return isClean; /* Another robbyjo's patch */ }
/**
 * Comment
 */
public void jTextField1_KeyEvents() {
	return;
}
/**
 * 
 * @param filename java.lang.String
 */
public void loadFile(String filename) throws Exception {
	BUI.property = SpecificationSaverLoader.load(filename);
	setTitle("Property Manager<" + new File(filename).getName() + ">");
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		PropertyManager aPropertyManager;
		aPropertyManager = new PropertyManager();
		aPropertyManager.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aPropertyManager.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void newButton_ActionEvents() {
	BUI.property = new Property();
	updatePropertyManager();
	try {
	    //BUI.sessions.getActiveSession().setSpecFilename(null);
	    //BUI.manager.updateProperty();
	} catch (Exception e) {
	}
	setTitle("Property Manager<Untitled>");
	isClean = true;
}
/**
 * Comment
 */
public void openButton_ActionEvents() {
	FileChooser.chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	FileChooser.chooser.setSelectedFile(new File(""));
	FileChooser.chooser.setFileFilter(FileChooser.SPECIFICATION);
	FileChooser.chooser.setFileSelectionMode(FileChooser.FILES_ONLY);
	
	if (FileChooser.chooser.showOpenDialog(this) == FileChooser.APPROVE_OPTION) {
		try {
			if (FileChooser.chooser.getSelectedFile().isDirectory()) {
				JOptionPane.showMessageDialog(this, "Cannot choose a directory to open a specification file", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (!FileChooser.chooser.getSelectedFile().exists()) {
				JOptionPane.showMessageDialog(this, "Selected file does not exist", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String filename = FileChooser.chooser.getSelectedFile().getCanonicalPath();
			loadFile(filename);
			updatePropertyManager();
			try {
			    //BUI.sessions.getActiveSession().setSpecFilename(filename);
			    //BUI.manager.updateProperty();
				isClean = false;
			} catch (Exception ex) {}
		} catch (Exception e) {
			StringBuffer buffer = new StringBuffer();
			for (Iterator i = SpecificationSaverLoader.getExceptions().iterator(); i.hasNext();) {
				buffer.append(((Exception) i.next()).getMessage() + "\n");
			}
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
		}
	}
}
/**
 * Comment
 */
public void patternNameComboBox_ActionEvents() {
	if (inComboBoxChange) return;
	
	String s = (String) getPatternNameComboBox().getSelectedItem();

	BUI.property.getActivatedTemporalLogicProperty().setPatternName(s);
	
	Hashtable table = (Hashtable) PatternSaverLoader.getPatternTable().get(s);

	inComboBoxChange = true;
	try {
		getPatternScopeComboBox().removeAllItems();
	} catch (Exception e) {}
	
	TreeSet ts = new TreeSet();
	for (Enumeration e = table.keys(); e.hasMoreElements();) {
		ts.add(e.nextElement());
	}

	for (Iterator i = ts.iterator(); i.hasNext();) {
		getPatternScopeComboBox().addItem(i.next());
	}
	inComboBoxChange = false;

	s = BUI.property.getActivatedTemporalLogicProperty().getPatternScope();
	if (s == null)
		getPatternScopeComboBox().setSelectedIndex(0);
	else
		getPatternScopeComboBox().setSelectedItem(s);

	patternScopeComboBox_ActionEvents();
}
/**
 * Comment
 */
public void patternScopeComboBox_ActionEvents() {
	if (inComboBoxChange) return;
	
	String s = (String) getPatternScopeComboBox().getSelectedItem();

	BUI.property.getActivatedTemporalLogicProperty().setPatternScope(s);
	
	Pattern p = (Pattern) ((Hashtable) PatternSaverLoader.getPatternTable().get(getPatternNameComboBox().getSelectedItem())).get(s);

	inComboBoxChange = true;
	try {
		getPropositionComboBox().removeAllItems();
	} catch (Exception e) {}
	
	for (Iterator i = p.getParameters().iterator(); i.hasNext();) {
		getPropositionComboBox().addItem(i.next());
	}
	inComboBoxChange = false;

	getPropositionComboBox().setSelectedIndex(0);

	propositionComboBox_ActionEvents();

	Hashtable parameters = new Hashtable();
	for (Iterator i = p.getParameters().iterator(); i.hasNext();) {
		Object parm = i.next();
		parameters.put(parm, parm);
	}
	String template = p.expandFormat(parameters);
	getExpandedPatternTextField().setText(template);
	getExpandedPatternTextField().setToolTipText(template);
}
/**
 * Comment
 */
public void propositionComboBox_ActionEvents() {
	if (inComboBoxChange) return;
	String s = (String) getPropositionComboBox().getSelectedItem();

	TemporalLogicProperty tlp = BUI.property.getActivatedTemporalLogicProperty();
	String prop = tlp.getProposition(s);

	if (prop == null) {
		prop = "";
		tlp.putProposition(s, prop);
	}
	
	getPropositionTextField().setText(prop);
}
/**
 * Comment
 */
public void propositionTextField_FocusLost() {
	String prop = (String) getPropositionComboBox().getSelectedItem();
	String s = getPropositionTextField().getText().trim();
	BUI.property.getActivatedTemporalLogicProperty().putProposition(prop, s);
}
/**
 * Comment
 */
public void propositionTextField_KeyEvents() {
	String prop = (String) getPropositionComboBox().getSelectedItem();
	String s = getPropositionTextField().getText().trim();
	BUI.property.getActivatedTemporalLogicProperty().putProposition(prop, s);
}
/**
 * Comment
 */
public void propositionTextField_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
	if (mouseEvent.isMetaDown()) {
		getPropositionPopupMenu().show(getPropositionTextField(), mouseEvent.getX(), mouseEvent.getY());
	}
}
/**
 * Comment
 */
public void quantificationTextArea_FocusLost(java.awt.event.FocusEvent focusEvent) {
	BUI.property.getActivatedTemporalLogicProperty().setQuantifier(getQuantificationTextArea().getText().trim());
}
/**
 * Comment
 */
public void quantificationTextArea_KeyEvents() {
	BUI.property.getActivatedTemporalLogicProperty().setQuantifier(getQuantificationTextArea().getText().trim());
}
/**
 * Comment
 */
public void removeAssertionButton_ActionEvents() {
	Object[] objects = getAssertionList().getSelectedValues();
	for (int i = 0; i < objects.length; i++) {
		BUI.property.removeAssertionProperty((AssertionProperty) objects[i]);
	}
	updateAssertionList();
	getRemoveAssertionButton().setEnabled(false);
	getActivateAssertionButton().setEnabled(false);
	getAddEnabledAssertionButton().setEnabled(BUI.property.getActivatedAssertionProperties().size() == 1);
	getRemoveEnabledAssertionButton().setEnabled(BUI.property.getActivatedAssertionProperties().size() == 1);
	getAssertionNameTextField().setEditable(false);
	getAssertionNameTextField().setText("");
}
/**
 * Comment
 */
public void removeEnabledAssertionButton_ActionEvents() {
	Object[] objects = getEnabledAssertionList().getSelectedValues();
	AssertionProperty ap = (AssertionProperty) BUI.property.getActivatedAssertionProperties().iterator().next();
	for (int i = 0; i < objects.length; i++) {
		ap.removeAssertion((Name) objects[i]);
	}
	updateEnabledAssertionList();
	getRemoveEnabledAssertionButton().setEnabled(false);
}
/**
 * Comment
 */
public void removeImportButton_ActionEvents() {
	Object[] objects = getImportList().getSelectedValues();
	for (int i = 0; i < objects.length; i++) {
		String s = (String) objects[i];
		if (s.startsWith("import predicate ")) {
			if (s.endsWith(".*")) {
				BUI.property.removeImportedPredicateSet(new Name(s.substring("import predicate ".length(), s.length() - 2)));
			} else {
				BUI.property.removeImportedPredicate(new Name(s.substring("import predicate ".length())));
			}
		} else if (s.startsWith("import assertion ")) {
			if (s.endsWith(".*")) {
				BUI.property.removeImportedAssertionSet(new Name(s.substring("import assertion ".length(), s.length() - 2)));
			} else {
				BUI.property.removeImportedAssertion(new Name(s.substring("import assertion ".length())));
			}
		} else {
			if (s.endsWith(".*")) {
				BUI.property.removeImportedPackage(new Name(s.substring("import ".length(), s.length() - 2)));
			} else {
				BUI.property.removeImportedType(new Name(s.substring("import ".length())));
			}
		}
	}
	updateImportList();
	getRemoveImportButton().setEnabled(false);
}
/**
 * Comment
 */
public void removeTLButton_ActionEvents() {
	Object[] objects = getTLList().getSelectedValues();
	for (int i = 0; i < objects.length; i++) {
		BUI.property.removeTemporalLogicProperty((TemporalLogicProperty) objects[i]);
	}
	updateTLList();
	getRemoveTLButton().setEnabled(false);
	getActivateTLButton().setEnabled(false);
	getTLNameTextField().setEditable(false);
	getTLNameTextField().setText("");

	if (BUI.property.getActivatedTemporalLogicProperty() == null) {
		setTLEnabledAll(false);
	}
}
/**
 * 
 */
public void reset() {
	BUI.property = new Property();
	setTitle("Property Manager<Untitled>");
	isClean = true;
}
/**
 * Comment
 */
public void saveAsButton_ActionEvents() {
	FileChooser.chooser.setFileFilter(FileChooser.SPECIFICATION);
	FileChooser.chooser.setSelectedFile(new File(System.getProperty("user.dir")));
	FileChooser.chooser.setFileSelectionMode(FileChooser.FILES_ONLY);
	FileChooser.chooser.setSelectedFile(new File(""));

	if (FileChooser.chooser.showSaveDialog(this) == FileChooser.APPROVE_OPTION) {
		try {
			if (FileChooser.chooser.getSelectedFile().isDirectory()) {
				JOptionPane.showMessageDialog(this, "Cannot choose a directory to save a specification file", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			boolean f = true;
			if (FileChooser.chooser.getSelectedFile().exists()) {
				String filename = FileChooser.chooser.getSelectedFile().getCanonicalPath();
				f = JOptionPane.showConfirmDialog(this, "Overwrite " + filename + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			}
			if (f) {
				String filename = FileChooser.chooser.getSelectedFile().getCanonicalPath();
				BUI.property.setFilename(filename);
				saveButton_ActionEvents();
				try {
				    //BUI.sessions.getActiveSession().setSpecFilename(filename);
				    //BUI.manager.updateProperty();
				} catch (Exception ex) {}
				setTitle("Property Manager<" + new File(filename).getName() + ">");
				return;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
		}
	}
}
/**
 * Comment
 */
public void saveButton_ActionEvents() {
	if ("".equals(BUI.property.getFilename())) {
		saveAsButton_ActionEvents();
		return;
	}

	try {
		SpecificationSaverLoader.save(BUI.property);
	} catch (Exception e) {
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}
/**
 * 
 * @param c java.util.Collection
 */
public void selectAssertionProperties(Collection c) {
	int indices[] = new int[c.size()];
	int j = 0;
	JList list = getAssertionList();
	for (Iterator i = c.iterator(); i.hasNext(); j++) {
		list.setSelectedValue(i.next(), false);
		indices[j] = list.getSelectedIndex();
	}
	list.setSelectedIndices(indices);
	activateAssertionButton_ActionEvents();
}
/**
 * 
 * @param tlp edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty
 */
public void selectTemporalLogicProperty(TemporalLogicProperty tlp) {
	getTLList().setSelectedValue(tlp, true);
	activateTLButton_ActionEvents();
}
/**
 * 
 * @param b boolean
 */
private void setTLEnabledAll(boolean b) {
	getQuantificationTextArea().setBackground(Color.white);
	getQuantificationTextArea().setEditable(b);
	getPatternNameComboBox().setEnabled(b);
	getPatternScopeComboBox().setEnabled(b);
	getPropositionComboBox().setEnabled(b);
	getPropositionTextField().setEditable(b);
	getExpandButton().setEnabled(b);

	if (!b) {
		inComboBoxChange = true;
		getQuantificationTextArea().setText("");
	getQuantificationTextArea().setBackground(new Color(204, 204, 204));
		getPatternNameComboBox().removeAllItems();
		getPatternScopeComboBox().removeAllItems();
		getPropositionComboBox().removeAllItems();
		getPropositionComboBox().addItem(" ");
		getPropositionTextField().setText("");
		getExpandedPatternTextField().setText("");
		getShowMappingButton().setEnabled(false);
		getShowErrorButton().setEnabled(false);
		inComboBoxChange = false;
	}
}
/**
 * Comment
 */
public void showErrorButton_ActionEvents() {
	TemporalLogicProperty tlp = BUI.property.getActivatedTemporalLogicProperty();
	if (tlp == null) return;

	StringBuffer buffer = new StringBuffer();
	for (Iterator i = tlp.getExceptions().iterator(); i.hasNext();) {
		buffer.append(i.next().toString() + "\n");
	}
	JOptionPane.showMessageDialog(this, buffer.toString(), "Error", JOptionPane.ERROR_MESSAGE);
}
/**
 * Comment
 */
public void showMappingButton_ActionEvents() {
}
/**
 * Comment
 */
public void tLList_ListSelectionEvents() {
	getRemoveTLButton().setEnabled(true);
	getActivateTLButton().setEnabled(getTLList().getSelectedValues().length == 1);
	TemporalLogicProperty tlp = (TemporalLogicProperty) getTLList().getSelectedValue();
	getTLNameTextField().setText(tlp.getName());
	getTLNameTextField().setEditable(BUI.property.isActivated(tlp));
}
/**
 * Comment
 */
public void tLNameTextField_FocusLost() {
	String s = getTLNameTextField().getText().trim();
	if (!inNameFocusLost && !Util.isValidId(s)) {
		inNameFocusLost = true;
		do {
			s = JOptionPane.showInputDialog("Please input a valid temporal logic property name");
		} while ((s == null) || !Util.isValidId(s));
		getTLNameTextField().setText(s);
		TemporalLogicProperty tlp = BUI.property.getActivatedTemporalLogicProperty();
		tlp.setName(s);
		getTLList().repaint();
		inNameFocusLost = false;
	}
}
/**
 * Comment
 */
public void tLNameTextField_KeyEvents() {
	TemporalLogicProperty tlp = BUI.property.getActivatedTemporalLogicProperty();
	tlp.setName(getTLNameTextField().getText().trim());
	getTLList().repaint();
}
/**
 * 
 */
private void updateAssertionList() {
	getAssertionList().setValueIsAdjusting(true);
	getAssertionList().setListData(BUI.property.getAssertionProperties());
	getAssertionList().setValueIsAdjusting(false);
	getAssertionScrollPane().validate();
	getAssertionScrollPane().repaint();
}
/**
 * 
 */
public void updateEnabledAssertionList() {
	HashSet hs = new HashSet();
	for (Iterator i = BUI.property.getActivatedAssertionProperties().iterator(); i.hasNext();) {
		hs.addAll(((AssertionProperty) i.next()).getAssertions());
	}
	getEnabledAssertionList().setValueIsAdjusting(true);
	getEnabledAssertionList().setListData(new Vector(hs));
	getEnabledAssertionList().setValueIsAdjusting(false);
	getEnabledAssertionScrollPane().validate();
	getEnabledAssertionScrollPane().repaint();
}
/**
 * 
 */
private void updateImportList() {
	Vector v = new Vector();

	for (Iterator i = BUI.property.getImportedType().iterator(); i.hasNext();) {
		v.add("import " + i.next().toString());
	}
	for (Iterator i = BUI.property.getImportedPackage().iterator(); i.hasNext();) {
		v.add("import " + i.next().toString() + ".*");
	}
	for (Iterator i = BUI.property.getImportedAssertion().iterator(); i.hasNext();) {
		v.add("import assertion " + i.next().toString());
	}
	for (Iterator i = BUI.property.getImportedAssertionSet().iterator(); i.hasNext();) {
		v.add("import assertion " + i.next().toString() + ".*");
	}
	for (Iterator i = BUI.property.getImportedPredicate().iterator(); i.hasNext();) {
		v.add("import predicate " + i.next().toString());
	}
	for (Iterator i = BUI.property.getImportedPredicateSet().iterator(); i.hasNext();) {
		v.add("import predicate " + i.next().toString() + ".*");
	}
	getImportList().setValueIsAdjusting(true);
	getImportList().setListData(v);
	getImportList().setValueIsAdjusting(false);
	getImportScrollPane().validate();
	getImportScrollPane().repaint();
}
/**
 * 
 */
public void updateLists() {
	updateImportList();
	updateAssertionList();
	updateEnabledAssertionList();
	updateTLList();
}
/**
 * 
 */
private void updatePropertyManager() {
	updateLists();

	getRemoveImportButton().setEnabled(false);

	getAddTLButton().setEnabled(true);
	getRemoveTLButton().setEnabled(false);
	getActivateTLButton().setEnabled(false);
	getTLNameTextField().setEditable(false);
	getTLNameTextField().setText("");
	getTLDescriptionButton().setEnabled(false);
	getQuantificationTextArea().setBackground(new Color(204, 204, 204));
	getQuantificationTextArea().setEditable(false);
	getQuantificationTextArea().setText("");
	try {
		getPropositionComboBox().removeAllItems();
	} catch (Exception e) {}
	getPropositionComboBox().setEnabled(false);
	try {
		getPatternScopeComboBox().removeAllItems();
	} catch (Exception e) {}
	getPatternScopeComboBox().setEnabled(false);
	try {
		getPatternNameComboBox().removeAllItems();
	} catch (Exception e) {}
	getPatternNameComboBox().setEnabled(false);
	getPropositionTextField().setEditable(false);
	getPropositionTextField().setText("");
	getExpandButton().setEnabled(false);
	getExpandedPatternTextField().setEditable(false);
	getExpandedPatternTextField().setText("");
	getShowMappingButton().setEnabled(false);
	getShowErrorButton().setEnabled(false);

	getAddAssertionButton().setEnabled(true);
	getRemoveAssertionButton().setEnabled(false);
	getActivateAssertionButton().setEnabled(false);
	getAssertionNameTextField().setEditable(false);
	getAssertionNameTextField().setText("");
	getAssertionDescriptionButton().setEnabled(false);
	getAddEnabledAssertionButton().setEnabled(false);
	getRemoveEnabledAssertionButton().setEnabled(false);
}
/**
 * 
 */
private void updateTLList() {
	getTLList().setValueIsAdjusting(true);
	getTLList().setListData(BUI.property.getTemporalLogicProperties());
	getTLList().setValueIsAdjusting(false);
	getTLScrollPane().validate();
	getTLScrollPane().repaint();
}
}
