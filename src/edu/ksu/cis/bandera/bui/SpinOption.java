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
import java.util.*;
import java.awt.*;
import javax.swing.*;
import edu.ksu.cis.bandera.spin.*;

import org.apache.log4j.Category;

/**
 * The SpinOption class is a view of the SpinOptions model that are available
 * which represents the command line options that are available for use
 * with the Spin model checker.
 *
 * @see edu.ksu.cis.bandera.spin.SpinOptions
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class SpinOption extends JDialog implements java.awt.event.ActionListener, java.awt.event.FocusListener {
	private JPanel ivjBottomPanel = null;
	private JPanel ivjCorrectnessPanel = null;
	private JPanel ivjJDialogContentPane = null;
	private JLabel ivjJLabel2 = null;
	private JLabel ivjJLabel3 = null;
	private JPanel ivjLabelPanel = null;
	private JPanel ivjModePanel = null;
	private JButton ivjOkbutton = null;
	private JPanel ivjRunPanel = null;
	private JPanel ivjTrapPanel = null;
	private JLabel ivjJLabel1 = null;
	private JLabel ivjJLabel4 = null;

    /**
     * spinOptions is a model of the command line options that are available
     * for use with the Spin tool through Bandera.
     */
    public static edu.ksu.cis.bandera.spin.SpinOptions spinOptions = new SpinOptions();

    /**
     * The log that we will be writing to.
     */
    private static final Category log = Category.getInstance(SpinOption.class);

	private JCheckBox ivjAssertionsCheckBox = null;
	private JCheckBox ivjCompressionCheckBox = null;
	private JTextField ivjErrorsTextField = null;
	private JRadioButton ivjExhaustiveRadioButton = null;
	private JRadioButton ivjHashCompactRadioButton = null;
	private JRadioButton ivjLivenessRadioButton = null;
	private JCheckBox ivjNeverClaimCheckBox = null;
	private JCheckBox ivjPartialOrderCheckBox = null;
	private JRadioButton ivjSaftyRadioButton = null;
	private JCheckBox ivjShortestCheckBox = null;
	private JRadioButton ivjSupertraceRadioButton = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JCheckBox ivjSaveTrailCheckBox = null;
	private JTextField ivjPhysicalTextField = null;
	private JTextField ivjDepthTextField = null;
	private JTextField ivjStateTextField = null;
	private JPanel ivjFillerPanel1 = null;
	private JPanel ivjFillerPanel2 = null;
	private JPanel ivjFillerPanel3 = null;
	private JPanel ivjFillerPanel4 = null;
	private JCheckBox ivjResourceBoundedCheckBox = null;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.FocusListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SpinOption.this.getNeverClaimCheckBox()) 
				connEtoC1(e);
			if (e.getSource() == SpinOption.this.getLivenessRadioButton()) 
				connEtoC2(e);
			if (e.getSource() == SpinOption.this.getSaftyRadioButton()) 
				connEtoC3(e);
			if (e.getSource() == SpinOption.this.getExhaustiveRadioButton()) 
				connEtoC4(e);
			if (e.getSource() == SpinOption.this.getSupertraceRadioButton()) 
				connEtoC5(e);
			if (e.getSource() == SpinOption.this.getHashCompactRadioButton()) 
				connEtoC6(e);
			if (e.getSource() == SpinOption.this.getOkbutton()) 
				connEtoC7(e);
			if (e.getSource() == SpinOption.this.getSaveTrailCheckBox()) 
				connEtoC8(e);
			if (e.getSource() == SpinOption.this.getShortestCheckBox()) 
				connEtoC9(e);
			if (e.getSource() == SpinOption.this.getPartialOrderCheckBox()) 
				connEtoC10(e);
			if (e.getSource() == SpinOption.this.getCompressionCheckBox()) 
				connEtoC11(e);
			if (e.getSource() == SpinOption.this.getAssertionsCheckBox()) 
				connEtoC16();
			if (e.getSource() == SpinOption.this.getResourceBoundedCheckBox()) 
				connEtoC17(e);
		};
		public void focusGained(java.awt.event.FocusEvent e) {};
		public void focusLost(java.awt.event.FocusEvent e) {
			if (e.getSource() == SpinOption.this.getPhysicalTextField()) 
				connEtoC12(e);
			if (e.getSource() == SpinOption.this.getStateTextField()) 
				connEtoC13(e);
			if (e.getSource() == SpinOption.this.getDepthTextField()) 
				connEtoC14(e);
			if (e.getSource() == SpinOption.this.getErrorsTextField()) 
				connEtoC15(e);
		};
	};
/*			SpinOption.spinOptions.setApplyNeverClaim(false);
			SpinOption.spinOptions.setAcceptanceCycles(false);
			SpinOption.spinOptions.setSafety(true);*/
/**
 * SpinOptionDlg constructor comment.
 */
public SpinOption() {
	super();
	initialize();
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Dialog
 */
public SpinOption(java.awt.Dialog owner) {
	super(owner);
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
public SpinOption(java.awt.Dialog owner, String title) {
	super(owner, title);
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
public SpinOption(java.awt.Dialog owner, String title, boolean modal) {
	super(owner, title, modal);
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
public SpinOption(java.awt.Dialog owner, boolean modal) {
	super(owner, modal);
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Frame
 */
public SpinOption(java.awt.Frame owner) {
	super(owner);
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public SpinOption(java.awt.Frame owner, String title) {
	super(owner, title);
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
public SpinOption(java.awt.Frame owner, String title, boolean modal) {
	super(owner, title, modal);
}
/**
 * SpinOptionDlg constructor comment.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
public SpinOption(java.awt.Frame owner, boolean modal) {
	super(owner, modal);
}
/**
 * actionPerformed method comment.
 */
public void actionPerformed(java.awt.event.ActionEvent e) {}

/**
 * When the assertions check box is selected, make sure that the SpinOptions model
 * is updated to match.  If the check box is selected, assertions should be turned on.
 * Otherwise, turn assertions off in the SpinOptions.
 */
public void assertionsCheckBox_ActionEvents() {
    spinOptions.setAssertions(getAssertionsCheckBox().isSelected());
}

/**
 * Comment
 */
public void compressionCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setCompression(getCompressionCheckBox().isSelected());
	//return;
}
/**
 * connEtoC1:  (NeverClaimCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.neverClaimCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.neverClaimCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (PartialOrderCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.partialOrderCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC10(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.partialOrderCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (CompressionCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.compressionCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC11(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.compressionCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (PhysicalFieldMemory.focus.focusLost(java.awt.event.FocusEvent) --> SpinOptionDlg.jTextFieldMemory_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.physicalTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (StateTextField.focus.focusLost(java.awt.event.FocusEvent) --> SpinOptionDlg.stateTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.stateTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (DepthTextField.focus.focusLost(java.awt.event.FocusEvent) --> SpinOptionDlg.depthTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.depthTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC15:  (ErrorsTextField.focus.focusLost(java.awt.event.FocusEvent) --> SpinOptionDlg.errorsTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC15(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.errorsTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC16:  (AssertionsCheckBox.action. --> SpinOption.assertionsCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC16() {
	try {
		// user code begin {1}
		// user code end
		this.assertionsCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC17:  (ResourceBoundedCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOption.resourceBoundedCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC17(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.resourceBoundedCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (LivenessRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.livenessRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.livenessRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (SaftyRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.saftyRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saftyRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (ExhaustiveRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.exhaustiveRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.exhaustiveRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (SupertraceRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.supertraceRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.supertraceRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (HashCompactRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.hashCompactRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.hashCompactRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (Okbutton.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.okbutton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.okbutton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (SaveTrailCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.saverailCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saverailCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (ShortestCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SpinOptionDlg.shortestCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.shortestCheckBox_ActionPerformed(arg1);
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
public void depthTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	try {
		spinOptions.setSearchDepth(Integer.parseInt(getDepthTextField().getText()));
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		getDepthTextField().setText("" + spinOptions.getSearchDepth());
	}
	//return;
}
/**
 * Comment
 */
public void errorsTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	try {
		spinOptions.setStopAtError(Integer.parseInt(getErrorsTextField().getText()));
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		getErrorsTextField().setText("" + spinOptions.getStopAtError());
	}
	//return;
}
/**
 * Comment
 */
public void exhaustiveRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setSearchMode(SpinOptions.Exhaustive);
	//return;
}
/**
 * focusGained method comment.
 */
public void focusGained(java.awt.event.FocusEvent e) {}
/**
 * focusLost method comment.
 */
public void focusLost(java.awt.event.FocusEvent e) {}
/**
 * Return the JCheckBox1 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getAssertionsCheckBox() {
	if (ivjAssertionsCheckBox == null) {
		try {
			ivjAssertionsCheckBox = new javax.swing.JCheckBox();
			ivjAssertionsCheckBox.setName("AssertionsCheckBox");
			ivjAssertionsCheckBox.setSelected(true);
			ivjAssertionsCheckBox.setText("Assertions");
			ivjAssertionsCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			ivjAssertionsCheckBox.setSelected(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionsCheckBox;
}
/**
 * Return the BottomPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBottomPanel() {
	if (ivjBottomPanel == null) {
		try {
			ivjBottomPanel = new javax.swing.JPanel();
			ivjBottomPanel.setName("BottomPanel");
			ivjBottomPanel.setBorder(new javax.swing.border.CompoundBorder());
			ivjBottomPanel.setLayout(new java.awt.GridBagLayout());
			ivjBottomPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsCorrectnessPanel = new java.awt.GridBagConstraints();
			constraintsCorrectnessPanel.gridx = 0; constraintsCorrectnessPanel.gridy = 0;
			constraintsCorrectnessPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsCorrectnessPanel.weightx = 1.0;
			constraintsCorrectnessPanel.weighty = 1.0;
			constraintsCorrectnessPanel.insets = new java.awt.Insets(10, 10, 0, 0);
			getBottomPanel().add(getCorrectnessPanel(), constraintsCorrectnessPanel);

			java.awt.GridBagConstraints constraintsModePanel = new java.awt.GridBagConstraints();
			constraintsModePanel.gridx = 1; constraintsModePanel.gridy = 0;
			constraintsModePanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsModePanel.weightx = 1.0;
			constraintsModePanel.weighty = 1.0;
			constraintsModePanel.insets = new java.awt.Insets(10, 10, 0, 10);
			getBottomPanel().add(getModePanel(), constraintsModePanel);

			java.awt.GridBagConstraints constraintsLabelPanel = new java.awt.GridBagConstraints();
			constraintsLabelPanel.gridx = 0; constraintsLabelPanel.gridy = 1;
			constraintsLabelPanel.gridwidth = 2;
			constraintsLabelPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsLabelPanel.weightx = 1.0;
			constraintsLabelPanel.weighty = 1.0;
			constraintsLabelPanel.insets = new java.awt.Insets(10, 12, 0, 12);
			getBottomPanel().add(getLabelPanel(), constraintsLabelPanel);

			java.awt.GridBagConstraints constraintsTrapPanel = new java.awt.GridBagConstraints();
			constraintsTrapPanel.gridx = 0; constraintsTrapPanel.gridy = 2;
			constraintsTrapPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsTrapPanel.weightx = 1.0;
			constraintsTrapPanel.weighty = 1.0;
			constraintsTrapPanel.insets = new java.awt.Insets(10, 10, 10, 0);
			getBottomPanel().add(getTrapPanel(), constraintsTrapPanel);

			java.awt.GridBagConstraints constraintsRunPanel = new java.awt.GridBagConstraints();
			constraintsRunPanel.gridx = 1; constraintsRunPanel.gridy = 2;
			constraintsRunPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsRunPanel.weightx = 1.0;
			constraintsRunPanel.weighty = 1.0;
			constraintsRunPanel.insets = new java.awt.Insets(10, 10, 10, 10);
			getBottomPanel().add(getRunPanel(), constraintsRunPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBottomPanel;
}
/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88GF9F0E1AAGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E14DBD8DDCD4D576D4D4EE5ABF5B5822322222242456B6AAAC36E50BD6EA29282C5832328D4BED29E857F6ED575DFDBA08142418189828230202A29F0A0608FCA8AA4A182C10E1D2BAB0A3B34E400CB303C2217E4F3D771D7B5E3CF9634C9074DBFF3F631BF76E793A671E7B796E3D8859AB42664C1A1DAD88336688623FAFE689027DA5C18832160DC2DC06562291E47FD682AC94924DE9704C0134308FAD
	1AACA17CE8863C9743FB787B964D92783DC878D52975997861C5CF83249D3EF45A9B6757D3F482754C256F9FEAB361B99F208CF02DC43F1668DF38B2C74477417BFC71B701D9338521F1A521295B1CA343A70BF4F910D68620DDC5E80AD766096923819781B099A0F5A5234DG1A0F28F5A78FD40AF4B1993F908C9BAFCC7F04681249C7B02DE23C234427A02CCBA70E50883F5E379E1E554D162E9A23213744626CAD69B4BBCBBA74BD8623C3DF5251EB2E2AEA311BFB8E58DDE6DBCF0335CB18C9EDBC3EB90372
	8BC29C3C0FD8CDF02FEEA6FE29035FEDG7655E8673B7B2BC5BB0E3DF30B20FDF524D7BB93D6B37BD2D7CBF6D6BEE4189B16CF64FF35B2D364AB03F793G4B43834D0581640159FF43CA34BFB99F6DFFFB8D111FD7E051D4G688A90FF4FDA02B7407BGC0C881666B0F3518AF2B69F6A139F4F47A82AF790AACE07919D4A0652BE27F58D9CBE9BC5DE7A1B11135867482B482D881C681C4826452B8361FD8874F7A8E57CEDBF7372D275EEE5FE1B5B8765B8CC6AB7C2E2A029491F75058E9F3988401452AE5F331
	0ABD88F1EB5C6314G4B5B578246D6686C6B053E955753F8D1662D6138BDBDC9141132C64A5B183544EF57FDC17C16893F895FCEG060F2378A9B2BCAB5FE49ABF645DDE8879DF07FE8EA9C4DBB2FFFE25B0266B077439AA36B4CF3CA6BD22185990DBAC5942FCFCC5B771F18A60B3B6C0BD832884E883685B00BEFE7044A6DFBE5E5B63B2BAFA7456165E8E3B5E615A51EB36C20C093EBE3272FC3ED68346E78E180F79A5E2133C0CE6BCF8CE55AF76181169A19B18BFA2B7C8FEE13AEF712ABBE103E7EC4C67E5
	789D2D93D3B630B254F03C0962D3E5F8D6362934EE5146354422B1GB07A3B8A89BE3E04512F66F2A6D2FCCE2904E7F29E2072493B8952024BAC9AD63E9BEC244D8CAB37E8E2GA68124GE4EE34E88A812AB7E2994F31147AD90F1AE5654BF89F36FC86CFE31F4B215F5B63F469FBBA0D07748E333E43EAF48AA24DC48B71EB079BDF822D5F1D20E30F3187DAA4528A01F4FD3791024949E81E37ACA4B48EA3CDEDB5F79BC1C137DDC0392FD89641532AF73A3E329B74AE23C4415227DAC8CC1C529F8209A0GFE
	0F5AC87CF5A1ED275B61F75FC67455B7F602CB0477E95CAF472C1F423389025BE82D2AEAB0F792B3740EFE0C5130AF0261B9936843B7D9B44D95964DCC00704D507FED31E8523E30E822811AABAD1A99GE1DB61F79550G04EE33E80A2BAD1AD800366D503F56C01F86202B85DA0070BA10D18F36G0C5A817D60CE686FGC6B4C0BD5DE5510C87285B8DE58AD0310777A9ED987BB326DEAF445FBF209A7BB40E7730B8CE5E230C7DA7E81EB29B41BEG9D0095E094C0A4C082C08AC066DEC8875081D881C681C4
	82A4G24GE46E03F4G9D0095E094C0A4C082C08AC0E693248368G2CG23GA28192GD2GB2F5B82EF8FBF356EC5FF51F781B6623463DFE925F937F92FF91DF3925D5B0DF927F12F212271132A3E56426231E1523D2B7A9ABDF7611B2A265C7FE1372A7CFD27EA4F648EFD24EC8DB06F4956EF2C97CF8685E6CDBB78209979227CAFCF3157FB24E8791B25F1398A6312CC6C7E27EFCF2C80C7B5245E2636496FFE2639371D9204013360334195B816AD564EF8358C475382711714CB2850E341F0BD5F4EF7043
	EF24EF7FC27C1DABBED7882CDFA03F4BE5341B4567C78A99CBD4644E8B206CEA81662860178EC1DC90D8AB7BCD4656C5DE68D67A1033558FDD2CBD0FBFC97A44E2E8C79A81CCBAEC532F37EA855A576D50BB0D583B9DF0C047E4B4C8ADBC238D6DA6E5EDBCE56CF1B5986DC698B977381CE25FF2D3B7A983315FBBEE9330CF3A0D6249B8E8A7F4CCCE0E9F573D1061C1063C974574FBE93A5E65F218BBFADD4696234BE56E6912789F2236B85D06D8CEB12FCFB8C8DE03A00F21G91BC2F8DBDA446317F5259EC16
	DEFBAB691945FEF0829D6F47G7DA45E5E9A5612F95D8C7E5EC35F25743F2DE973334C090CC0630469D83056DF9D1AG65CBBA6EADEC90D85FC8E44C9171246CC83DA1ED46820155FD62C3D237170831C5FCCB6A7F8AB1DE193CA7293C2C7DB00686B886B082E0BCC0920070A50C39G1A817AG42G6281D281F28EC0DF8EE08298FDG7D7C6B35D93306463E21283F7FAB40FC13D6C8FC73E37D327AFFA0BFC3892C4EFFE1735EE65558545B2C861BFA1B354FF67EB66920CD2DCDE2E907ED24ECEC27FBE4ED5C
	F7B6B21E6E34757498E9C34BD98436E671EEAB490739476C7A5AEC68B23ADA4C5F9BE1BEF50034D55752B1FBB47CCEB800635D532BF13CEBB8F435D0F6462ABA9F9F3D709769794D2CAD2AEC16463B4C2623F652F69A2D46EEF054CEDBEF0F0B610DF4BEA0621D9A0AFB6760357054963668CF410C45EC5DE9B2F6DAF6587A0A75B4BBA56C51ECF49C37B93A0D0642937AD37A927DE9D789AD0E12FA1A3C0B7CAE52EE58D023F5DBC3AAF6AA249E2DD70853F917372D68D05197822B930B04446C9B281DDF821D83
	0A545CBD341676F8488D5456B2EEEBE67BCD54D61D99520CCE67C13D41EC5B516BF259FA0646D82B2760C02DAD97EDFD39854B7F382BFF488DF5AA2486EA65C66E535BBF3E155A392B4F246FF53A4081C3EE2CD1CDF42096EF926DDDD6466CED69359B9D50B8F58E3D3DCEB551015ADBA15A7B3861B6EAEF235EE95AE96B3603E9C3EE30C9D5F62096EF66B1915170B3EA7381CB4790DAE9932585EA5796EED7C77FF53CCDED7D1FECD4191DF51F0BF55FE4F3380CCE57901B29109A2819155C1F3FEB39195AC966
	98E63D154EEC0656D63B0A64C06D5D4A6DBDFDB60C5ACBE25D814DB4D08D2D391D1E0283EE7F4B182D1B37DD4EFCEB6AF71ABB7556D698CA6DB69B2D066263364EDEE7897DDFE7F33A14A66EA6895E9D2A94F7F4B7972434CC12C48DAB67FE9CBB6BD2961FAE3290BAB406B95DE479EF55C65126EB7A19CDB090F5190648A6031BAC7FED423E6621FA568E6EF2B8EC8E679099E5F497663FD5953C7CF29B2E9B36B15B508C58181DCD7F9DB3FC6335A1992CB1BB5F53DCBDCCE335219828B19BAF589AB61C6334A1
	9A24B1DB7FD494B61CE334A19A20B1DB7B3F3DE5D847E7C3B7B8E3762E786E12A19E1B7D180199334930681A619A178D41200C59B8FB715543B4A69B0A819933713183B78E63F8EC2886E34C5615C7EF983E3158508C44F8FFF95D324B06FAAC76A387E24C2E7F2E8F9E52F1586087E14C1E81D770100E4186BFGE3765C7271A5C3BB7E7AD103AFE6535E527FD21B6A1DCE6803C026D39976CA1DFA8F4A2332D0F5969271525CFB62D7EC4CF950683475BABA0DBBEC3DBD8623E1886B16C3DD32C77DA2F61139B7
	7BEE74AFA7D97BECEB36E82CG414D785D68F5BBC9476FC66247311DB62B15AD5BC25A4CE632A61BDB19ADDBE79D534C767DCD03A7CB9F231ABE1F73477BC81F6EA3DD3B757C69BA9F69FDBE52FF69A37DE59F69F32B4E1F3E49C77AD19F69A3FD24BF60A37D8D9F698B361DBFFD1BEAFA43FF317CE3D4535BF87A76EA35F4ABCFFFD8B5BD082757EED7CB8F6169312A6991BC3D3EC6ADBD0627BF221A1E4853F7562A25A77374B8557499BC7D6C00DAFA66C1CC7F0FEA3A1627871DD1CB2F6069334E2A25B77274
	8BD5535BF93A661CDA3A1D27872B26879F44F6642913D970F49ACF769AFBBA0D6443F313319F77F61D7CD411F6C8EF6D15760C3DF212FC4F334A3ED9B3FC22C37DFB4D8B8E721D5A42653F64C897485EAFA613FDDF9A2FA7B26D8EF3375E51CF86E4627E634415727D4721E0BF592B9C8B4FC9G623A41310F61A9B539029055C2F86797613E649420CD83DCFE8B63B734303A42681E65F4C22BC51353AA51D5364A69AED907F4A3G9FA6230BF5232BDA0FF419DF814DD792DD43D7F23AF7B9DD8860A3E5F489EE
	F43794A3DD5EA10821C392DD6BA1B95D218DC8978178D8995D94B73A0FCB10AE7FEB0B267AEB092E7DEBB95D431CAE827031B23AA9EEF40ED224ABEE03BC36497C5CA627ABAFC33AA840A74868D25C6826F23A6A4310C7G57E1C697F9D8CEB7369C692683BE739B092E789BB95D110DC8E7877C8899DD049B5D02CDBCDEG1FF6C4224BBFA227FB145319G9F8490F60451C539518DD4A0DDB260E78264350BFEEEE7F4A16D04EEBC5F1BDE4AF193A96E3D390270F89A6E1F5D1FA55FBF5F7E4CC855EF2A8412C4
	79A96D4AFD459BFB095C4CEF4116EF31ADE8F54A772240C84B56E9265BBA45FA2DF1493F37B2DC060B543963E60753359706B5FD58B64CF791DB493ECF91AD566FF16B69DEAE45BE56DD86334B66402DBEBBCF5391D42BED274DE0EC0466E5A77F9CFD48ECBC8D7CECAF7C41CA74D75937783D793A0D72BD7B836DBD69733C784674AD734908EFA55FC4BEE8184BE4BB36216CF8C8E7383FF21C709D62DC55082B64388FB84E4EF12736A3AE02637E4EF1D3B82E2F86F1B9C79177A147B5F35C7735089B41F17F64
	38D80E3BEFGF1B3B8EE4B996E37E308FB0063CC9CD7F996F1E19CB7016312B82E6A9C62327408FB08639A75F85E6160C992FB743C8399B77715B8CF1BFB3A2A2A76B540745856454B35EA9561A57BC40274F81661139C5227A49379G599DD057683E6AAF0AB24558AA861C21837B3D6F673B75FB000F83185601FD450D74DBEC6023FA0C6389E749FE449F3DE8ABE01D961A6BC4DBA39BEE905A56DD20FAA6A3417CFD7A4C8E962F791DD25C867FEE6C2CD0G6613FFF21F5801A62293FDF2ED0E5CA7D3BA19CF
	B28DD8BEA76AF710BA7B2357F7F14F7BAF57219DDA5051E6C0BB262CD333E3B43763FD87596B78A317F0C59BBA57238D61A0FFB237A1B1D7E9C336917AF9A35A70D18359ABB6942B334C0EA545E8078EF4849B510E6A15EAF644F0BB9E5ECA6C980A15D7E6476F399D0920A363B85A31CC550E3A63E847534D3BC85BB0940B2A824647398DE8CBAB689945ED7944AC37A58E70A9GD9DDE84BEC8759C7B34865D3D1772692541D8FF25B3BD07719DCE53B9182E951DCF7E3930B4D9F35BAAA5A709637A181642719
	5006B92AB6D41B5006E7F6D29BFE5C7227E8436D25E8C3B3489F41ED58512434A1965212398D3F79141EF77A71CB1B229D5F14219DB3C0C719996DB837CF4D8E13996D48EFA7ED560FDF3744181CDF0EF68C000EA8EEC7C6015C0E9440E71F0076628476A73D65641C1B7FABE4580F6CE8A15F59873D2A496BD2A237DB87B6851FC03B5FD335BB66847A6F3553E42DE9F08B1828BBF8A3EFD3C0EE0685F50F4CD553DDE7C15DB76EA53A8737D8093AABB771B684640E623ABF7CDC291B5421B82EFB7EA952068CF2
	DDD2541D42F5A7015CACAB6A7E22C8ED6C51E845D8E92918AB0DBD44B85835FB954179DC3344FC5FD6013A5BC1EE88577D25EA3DCD603AB3B77F38F10FF39109F37F57B279394C0DEEF316FF5D61F54EB2454A46B5532D4AB94BE7274938B02B9B66E15DB8E779190B6CFF45B94BC118A568FB3A2CC6016F356DA57936987B4577ED3DC4AFDDF390F02F725715011CE32C6C4673E8ADFD4457BAF8B78184810482C4F35B9E6975F9AEF3D75FF94EE51E5E9608DDC95D38AFFDC6C0FCA91C2F3FBA90BE2187797E91
	90DFB6677BE1FBA0FC051CEFE6C0FC551C6FCCCDA0FC4D1C6F5F817199B85F395AC0787AB81FD097885FA84E7760C0A0FCE11CEF5B19C07822B8DFCCC0FC711CEF7B59C078A6F33E078362CB65FC35678261CB33A1DFECC0FCB91C2FF695B99BD3EC73BCDBD38738D6G13CA1A9A8CA868C65B707C7185F4BE9CEED31EE39EC971D1B665B966C5B9F80EB98E528A6D3CAC0A14F99CE5F7579921F8E7FCCD9EFC4B956F898ABE56FF549CA2ED234A9AG76A162FAEFFB155BFD93A0AB6324C5D38CD0F79257986E19
	CF7A83523F341D44B6790EFE3256B69A5EA38162G26G24B9AC9AADC003835B6605275456215C5BE6EF0BD05239B0660BD96B866743859EFCCA9F9A9CEABE7CEC47F2F59F4A66E43816121803BE9AF0E0195FB7C8FB9F75E9EF1442DEE643127599220C84EE03F97D60ECB86D412734A14D694E4762E96EF235F84A835AB2A746535CBC74D523936DDCD6BCB8BB57FB70A96D8CF22A794A3992FD954AEDF8E610B63C665306C4D59BDA398D53388D97EF981C8DD7F970A9ED503A54E2FCC10B1735C28CEF31DC9F
	49D116ABB987D989B2F5AEAC5786B3162B41057959DAB2387C6C7160D366E70CCBAD7EEE5F2E96FFD1C09B4F6D0C4AC5BB27F0BB5F99241D7F74E9E7CE2FDA59D767E2591775228D1125033361C18FBE258D26DEB5DFAD28D373D590500674222F7A1B504EC8EE67313241595963412734B3D955D7776FC39BE6F29B324B87E743AA8FBE258D9527546AC9C1A1696FD46A891F54B21FFE37D24D27BA10F96C947A3436G632F6F94666705C166A759E7FE461FD273691AB5E8C3BC3761320D0333A15403CFE903F0
	DA4D06C7F8BD4CB90DB654EC9A1C8DBA8FBE258D4D2AB61471FAE862B67CE510B68895CA3E9245FB182A8DBDC56807E8EE43B88FD97E59B041278D292AB6DC5C04B6E47461986FCF5F93DC9D3CB77761F82E679479DE046BG4DB6F30FCB40B5005C53E41F014DE16EB277A0CECBF11DB687BD7C4A4E5A77506F0947C066G17FB244F7BD95D0EBE75333A26BE75333ABDFD673FDF60D41F37333C4C3E7F5B06760D86E0388555088B6738519C974DF18B37A3AE0163FE4EF1D3B8EED18D6226F3DC8847E574A3EE
	F1AD6272B86EEA0EAB6338668144B5F05CB39C5746F1874FA04E4AF14FF1DC9047351EC5DC8847BD4FF1919CF7689C62E2B86EC50ECB64B8BD7D56188C6F995FA3AE1A6272603D0263329793DCA33C37F35CF794E70777919C77B0450D0177A80E7B459202CB0277146F319EDD1AC36FF0606FD752F7721D033DAF4D7177FE85FF0168F22F139B07DC077C2E07E5747D714F965018F7BA0DEC7934C16F520BE9EF39968BAAE74D3F54F79B855C1769AA7AC8245965326DA4E729D83F7757A2521F2974FB4DFAE881
	D81F7717D2E51FC77C1B73834CA3FF40BEAF248A5B17069F70DB73EA7A5DF2G5EC3GA2FF40B239FFBEE94362FEF0EF271260BD85C098701C530B770674FBEFC39E6CD7EFC3E27BC9FBA62E32AA5A09097DB9BCCD1A156EF5993B096B44BDD8145F25F7F499BD5A1997A9BFCB7346BF255FDBDB444A25F21D3CDCB67218FABE0778A8DB610BC2F82F86E8D67191131762A6EF93171767B15FA3E0GB9FDG233C4A8BDBAF17C74EF009E31C65243C491E103081AC6F4539243C1360BD95A0638C16F7E69E512DBD
	632E23825E9B815A4FF8535DDAAC573D196725CF754EBABB4889868875AAAFE303DC5E96AE6F302A3CC810938BB0492B3C98B7F9DFF05F7CA387FDB36D8C7A66100978C6FB9666D4G4DE751B7FB4DF25D2633CC57GBCC783041F75265BDEA257DD49F53F3A1D0FB54E226ECDB574CEBEF84FB8E751E84F216E8939C4F765B9F79DBAF8BF8660BA67CDF7FE29DC77D66E477ADCB5BF0EGB9E3G46FB1577C66947B2F96460834B4B1EBA4CCB5CB94C4B257BC8DEE642FBF69034BFC1181733CDEA3A9BA03D8D40
	9A64CD77B5E5EE3A4BB1AF8F6DD3139784F2C2GA23C4AEBAAF713379173F275CA4CCBCC906665A9FA0F6CF4F84F3C4022493FG7352D0A057DDFD815355ACBE8D62336F824986267B130DEE3AB771B514B5F2F923C43EB07199A5BE63BD647D56DDDE854A1B68D64E13C53ED47119F6A1FB66DC2814573B49CD5EE6143759CDDE3148D7A7BEDBAF74666B228AB7F9DBD01E132E291A94FC8370BE9AA05C2B3CBF3A4B7B8265DDC6E3AB9A7892E43CD3445F53D56431367F5D6F495AF19FFF7F8BFD6F97306FB556
	93DA56FF6C6AB10803ED36DF71E2BA06FE0C542DB4445DC6472DA381BE180538B5F40CFAA540B80E9BCBF164EC7CD20EAB246D4283B0D8DF46F1F7F05CC80EDBC747283F875C5FB9EE9C45DD8ACE3A185BD2CC4728641E00319A445D45F1FF6738AEBAC63D8764F5F35CB40A3B8FF00FF11C190ED1AF82DC67EC443DCDF1CE405D4AF196BAC66D855CD40ECB21384781DA383CEEBAC69D87F0146763DD0A3B83F84B53304C7BD611311341F8DC5FEB05E9064DE9BCE0972F68A4B1F891613B0A0EE3C919E5DC045F
	FF1FDEC5E225C35FE969F2100F37C87B96258D065F23B96D7A45A40ECE191DE632371B516D2063DB928F63B9DD6FE23A875466B03A4B3CFCB14A0C3B883F3B5FF31A74AF479D7AEE98CE34580D1DC2DFB09D43AF4631DAB2504EG4881FCA5C0DBB06A09DCC2E4384CAE7AFDB7B6185CF91DC9CC91FD726127E43F3A381B9086B6AEBA4F6AB1E2DB362E8C5B32340B31AD3BE9ABE94B2A613D9540FAB136E557D6496BF6B060C3AFE675A5921E3197FB569D767D659ABAF61CF4B17B7EB20F63EF237869B2BC7BAE
	D3C47164BD7B929853DC02F306A769370D49701E4AF18B9790DC5A255007DC0AB847821C2BE8B96EDF9DB887296038945A378743FBA847EDDE0A72A2B96E6A25A8AF1A63A2FBD1DE8247EDDA0F3CF9A378BCF1BD7296F2DC79DC642D9E417B0965685BD1008B83089AA13665701CBC421BEFFFC647E929A3188FBF62782BA9BE73E7921E79B6176249FBA1240D7A995A34BA8FFD114AF1DDF9181F344B90971F0779496238EDBBB0BF799CF745CA64F5F1DC6ACA648D6238DB76A0EF884759B9EFB247BD43F9D3B9
	6EC64E1BB6927D781F9CF90C36825EB4D29C0741F3B4C078C8F7DFB2FFBDC147F451A3193F66F07C73941FA043B3BF361BF02F4994C8EB9805B69D236B6A91709E43F1AF1D407CCC6138DA8B66A705634616E2FE044BB1BFEF38FD57AF86FC5D65E2DF8ACF53655E62E2AC9DA78EDC2E2CF377D17C184B15F5EEEB8D563998C8537E9F5A14180BF5AE186332F9DC04F2DCAF0F0BF00E7B870F0BE80EEB4BC55E0CAB90B7A197F933B96EE0BD7296DE013E50396D8168A334D708639FF8C6DD614D977F24634C78AB
	14F5A423096E1F3EC2D9C7AE5F07F5C498ED5198C673B50BA634BD1E637E560436CF623843C5E87BB40EFBE59F72D6DE09389A4E5B40F1B91C376DCA4C7775EEF31A884047DC49721A884F64AB3D65FBB73DE3FF4615ACFF1FF0BC3BFBBF6F67921E65FB929D431277BAC80B7CB95AF4042E0167DC85E3742BF87BC7F155702E633805EB386D9CD7C36BE8843C47F15C8ECA17846FA99CF7559A14B7136326F3F999A15C0F3BD1DE9B473D1A0B722C9C37AE8F658DF05C48BC14B702632A36233CA40E53F1F9A99C
	971A4B6D63388FF339FD3FC05CE5C5D8DF5AB86E39A2ACFBAB47D52C43326F63387B87D05EA80E3B194648E4F8CF653837D99CDE8D63012B39BC9D419543FBB547DD2BC3F90DD763F8614ED5A416E44BC3A5DF1BFB8C3653B0B4A037203B1CA58DFBDB1A8F346CBAFA604B23BBF570036DD5FBDD106FD593E2EE713AD7AD74EAF1CCA0BEE7ACFAE38E9B173CB11BC892775CADDEA45AC1B7576D34D9ED0E12AE073EDFCCBF3C1848BF2E37BA25F52F7B3D7208E7BD78B8AC99F467DDC347921F49777E91B9AFEA58
	BABBCBDF38C41ECE56F70E8872741DEE6944972F3971773A25931B3BE731F4663775C7706CCD6B1631C261C5FDDE7FDE06619A716C0D788C1F7F461C79DCCEE9255C7FD14BEC69333C48099579932E11768A32F624546D1B1E908A6DEDA823A904E7F52837F6E4D195E9971AC315FD49CA0A3707AA7B125B36E2DFB29A522607E2BC0E2F423E316DDA44FDCC67C1A6F89F603860EA2C9BE19CF7EBBADFC33E965B459B681909346B203F822883B88690F49D7FAEC35349D9C4F28E0D1CDDA4679CC3F9FA693AB611
	DF19EFEF20D5508609BA6538BA711D69E824BA82396FD7F91F75BC41735E6C3932F8DEA45CCC5B9727111CF3B79A58BC63EFF42CCC52E32E433A7FBE7D5BB7AEE31F0B5547BD64D51C17DCCF474C89C01B84203B1E76731D8D221E346BBDFDB4CB34C3A3C28E5094832C9673D0F7BD0B0BD95CCE4DD2CFBBBFDF0AF606GBD33EB459C492E5F5074F0C89BFFBD5A75E8AF5AB5590BDDF394F6254A6CCA9BA359A54E1752A5FD3F266375AC20499B037A9A67223E46B16A7AE6497435830DFD0C24AFD826CF6CCF69
	FA6BE840070F41788EDF0EF5A31A637E399C6BC6BC4775EA79381163DE64E3A561860C6DABF2499966E6F8F7810482447034B1B4ADDFBCD7CB4E4CCE62E9ABF3553EFF90FE14C564789B4BCAE0BA2AD4F5E0AC4DD10977C0001523AE8F63EA468D98D74932F2FE1F26E75C88ED600DD84E87F7E0B9575D683B1CDB0146F423D44E83B7AA636A5939123E25F4ADE1845004F0FD9376203ED8AF7A64F1BC89E82649744D7450F7E706246F9FF43E14F193646FA654F79F57D7F7136F7A5C8AB4261BE4793BC999471F
	AD45189DF1939FA3FF0EB19B49F159FCEE1048F13F617371E40EFB3B8B3F9DE604E1BCA61AA8AF3C17F15C4B94C74EF0137D9A8C57E172765D0E708DB616724264ED798FDEF5287D8D82FFC1A3FBAE92B6ACA5C15AE1F3500FD331E1F40EEF46F520C6F8BF8690829091464E3133323F70A3296CCDF4EE19F1B314BDC06B4D8477F02976FCF566E616CFF15E369D4BF54C4DFCBED6434B156392EAF939F25C0BFC0C1DFCB3164DDD74BE0A0ADBE09C8AE0828886883B85539F2269513730737664ACBDB9DF1E4453
	EBEB5AC4FE426BAD8F41AAE90DDE6843E4F826232D867B6939DE6493D0EB173C153D7C6FD830F2E1738522E366ADD8EE997324F23B117AAE73D6982B5C0AF5F661CE2C338D372A57D93419BC5B0046FA2BD4E703EED53691D3E47A7EC86D9985B421DC1F399E7545F951B7C726AF89E8D2E47A04F0E59B51D9073114990EB172E09D46D29E47D56D47D82A6338B2BE97EE8D47D81855C47A2EF0F80F83C88618495366533486717E846DED6CDE81F14F65BEF95DA57CB40FA27F796AE7320FF4A906A25DF4E03D76
	27BFBBDF4CA130720B4E144AEF851D5FB5C0BE1BEF43723397B51CC706328F9BG3E5137C96597FE1BB2DEA6FDA2696B22734E71C0934375157A54271141B4D2A6B2FDD937AB6365D7ABB1DE72EE47B8A8DE0D71D247F13767E33C98B86E0B3D98AFFD3773B5A33A47AF9E5EA7832482240D35E872816A46A24D91CA538E6F83GE34632FBB5B2457BB0984DCB5AE332FC91F972FC92F95EFC10FA3B77B4E28B312BEE2C3C1F7940CD17F26EF03E3F0F632B6DA2BE7FC4A6F71160A81077B729E3E9FFD3C0A686B1
	E3F15E70E8017B3CA112308B2C7C8B571C2F7CB50A98280800BEGE031E8CBFB04F20E70559E424766886464AA1963E37C755035A4937038A2503643EBBC6D372FA17606C4E09B9B3CC032F75EDA34B7BEC27D5B219CA683CD2A4C5E34BBA4FB19CD695426AC406781B07DF373087E6ABBD0FFFB16247F16BC54EF3FC3DD3FFC4C978CB421F7C87AA3BD74F7523EBA9A70F1DCFFEFAE511F4C75D7AC1474E7F17DB9F72A6B170FBB0B01266ECEC9FF6B1DCA7D51C558279D3B9375E512CFCEE27A37F41D4B8EE983
	F7E21B7164G5A9021E203324D08811A44BB25B6A379CEE54CC4B7F94EAB1FEEC23BD26F44D8192523FB9E6D7A136420A3234B51611A11B08E691AF46E711E7C29643F71BA34BDFB1C6FB1FBA150D40F13ECEF9EA7594E76EDDE390868EF87FC5FB85CDF795FD964EFCD9D521F703C8986104D8E9B3978201FE1696DD338277533C04EE6E11BG2DC6AB2ED1057C7267C21B556675FE18D0B14F516233CCE0EBD4ACAF7F4CA66D68B4C8CB0B34E88A816AGDA812C111837C28D31111CA482736BDDAEFD27095E1C
	03870C48E5B6921AFC8CE5321713BF5708FBC6B5855E7C83927669CF698FF41C00D8E855137BD3D85F303B8C7B1520C86CAFE24B31DF8967382ACD582F44F3DC6BD66CD7A6C7723D08748E3A0C3B20AF812886E885305E0569EBE9BAB963CC6E0CA0E73C49D9EBF26ED8DC1B5AAA9FABA03FBFE5C674E548E82DAAFCCCC71C1B0E39027F73EA0C617375B92C2D79C39529ABC1F7716F511F13BAE655775BFADDB8FF59348D67AFA9C017F637C5D38CD0F7B7BB834E6A61DDF44D2F8DF0063B311DF8B09D6B5A683B
	FD4F474301A67AEE56D61177043B156D446D8BC9C32C3CD91B258553FB5DBCEF52C45E67161135E7155B4345741B1693FE4FDB5011FF6AC2B2F05BED36DA0D8E3A9D331C6D473C5963CC9F71C992581EF2B77A74138534BDF2749AD9FB54CA7EB414A877AF9D646FC27598CFEB5D57458BB716D594D3583C39C8DC9F5F5509FB9F52FEE9512C207C9043827ECB2D754461B8300D18A93697F9CEECAFD276DCAB98DE39ACFD35205EDED47FD21CD77ED2DA0B4E7EEA7EBCE6FB79D277FC05AE1572F5F32F7F791A5E
	0B790AD34D9766417E39743BB7D75235E62E7F3A7A67222E19817BF0FF1E2453315C33AF7A59F26C33AA46E3DF7E64F2622386F8E7F5A9FF8546CB78F8D617E26019B8DE3C1B064DA3699A61B440CD9F0FF569DDBE2F535E633D5F6763B420E93CC72ACB6D77A86B52548C1226FA83BC27397DBBC223FA73BA27B9D7C89AF6759B4F11267300226EECE4F5E73947799D62839358BAF08F561D4EBC77985B3DCCAA03329D7E177BC70DD86EA17784DA6EB414455F77EAA57D576C71DF7F5BFBD0FFC2407AC72E14FE
	872F1274DF95007EB7397E34DF852A3FFDB57DCD6337E1A916D9752F58DAD6ABBCCD3FE27B50994D231FA3CD9060C381E281A609B4AC463753351294404DG483F1760DE6810470392985FA23AF6C876DFD96F65FB3AA88EEF3197E3A30B74F9BBEC006926B1A7FE5B3A470CE746C781FF5C3D3807EB0E160CC914FB6FCB5844242AEA172B53E4B46C9027A922CFBFB46358F3663D3877EA6D89A23FA28A6AAD00A18A6F707BDE03ED6E4C22DB85EBFA086AF7443C07AECC9F089277C4C4C9EDEE637E3133D8D95C
	14AF157F18D2FF4A1F513EDCCA7259823D3AC940FE2024CF5E8FA4040425AFD4310940E451160C7BA41B24B1446A9ABADF3A8F4B7F079AAC47307BD89B978D4F047B58980A156387F46DB199F0297771B60E4FED726E77BDB6AF831A067B25B62E6DFEE59BB7C1CB46CC7BF47A8E23957F06563F8E10BCFA7CF5814EF3C9A79DE3A86E21E2E99BEB48C4D4F1033848F7F731220D5B446E2EC9F53B4B57F0BF734188F832B670259AE59BC85247C07A787BB13E3394ED60AB85D28C3C97C09B38E3A7368171772B77
	335E5B008F56C83F5FDDAB695FD36F3F7E6B6AD13F70C0207AFFD7A870F89BD5C77CD179G465B9B7CECED6883AC5E4643B36E81B6DE976759F4FDF2B26026BD007136072FFDE5C77B1E4B9782CDF53494EF4D514AF8FB76B4A9EB353F3840526BABC95ED47E4A0148FFF01DA206443FAB52759199B0FA3945DDBCDFFCF21FB20EC85E5B41C6FBB44E6BFFD3A4155B17C57E14DBE07D44B56B245F2357CB3ABE9986DD26E27A1B167F4BFC9CB1A21A75C9E1700C0AE673B6C67355EA24C908E6B1128C4F99A20D38
	66C057A433A6D8B4F993B0C63E5C4B3F9DCF703E5601F1528EB47689D20C84CFD046C8F11954A6615FF75C781D04433FE1357F9B8907FFC3EA62FE52F6F97C6589AEDB3F0B58647E97A078791823A44D7D2FB160FD6AD72ED436D144A723A7607DDB05BA3574F01EBEFD2DDAFA7484EC3FEEAAA01517F51E3BE108E8F37417F4BA0CE0264EF61A5C713F8326DDD616DED8447A26911B647BE512777E100EE3DCE599A40865B2F90234DF493FD8CA2DE531F45186A92E86CA2438FD1E5EB327582F54E155F7DA443E
	E2CDA079926FA2D80BEDE066037E0E43504E56D2494E7DE5121DA7F34FEF67323C406DFCAE8F6DEC88584E02F2494E659BA5BBC7EDBF3F1DA3F38337331E5BB960371DE85B5FE436BDDD645E473E37C9CA9BB760FF9F776F8134A7AAE0FBA6D6C8BA5FECC2FB58FB29CE3E1F0E69FADD073AD28256F593B942A72E47DD3D08549BFEAE12ADC90AE9B7ACA2E5C67EB6B274E9666F69E10C96A06C69E26BFA36ADA4BD7FA1E8C3818CGFDG239E4275421559E442D25F2136D6384B50E544FD0B28AFED3177F32573
	96133926F82E14F0F5022D8EB339251F5F5F1FCD4FEDF450F1BF110F784F69398F174DCE118C37E2B1E9EB9C66AE130B110A78823A8F51EABCCE512C7F3015E17F9181F90B85188E10914376B0B01ABF15A30DB60675B115705409B462B8042ECF9AGE70F41EF7B162DDA417B3CE7CC8CEB57D89C24D6117169CE1B43E16CF41125ACBAD090473696129FF25F931B71A0CFD29B71896DCA4484069FEDA674B08E363B53FFCB3FC99C6C6D1149FE2F6ABBC13DEE361233AB625428D00DC0BEF7AAACAA562E12D92F
	95D7434DC6672AA25CFFF8E49B99875173B81077E9B1B8C7ABBF66F6AF882908D5D5FB9CE6438EFD170E2ED832FAF16BB61C17E9FF0D7332E7284CB6F837935C2FF1CC147405D467C2537D2F673AF9D87742FE9DE87B98C73AB6D14E9F681ECA921BC95C5656E544562C07AD1AC2003A07F14DF9DF277C7BC09B602D8F1FFF4D195551DF517B160300F68CC0ACC092C08AC05AEFAC1A9C00E200BA00B6GBBC0B0C0A8C0A4C08CC0A2C072EF30CE5F10C386125E3EEF0885C2ABB6CC31681D954C7E8FFB65764FG
	F919316737DFBCCFBAD74E27851E8A9FFC2CBC43163B3721EFACEF993298EEB16A9D1DA6AD29EC9038ACEEF5ABB0EE9B413E20D8FE9EFF8546E3F4AC1671A314B6955EB3FEEB51E8FF0B71E858A645E3C2GF32B95BBB09EABFF9BE8BCDED7AD4563A1FECF167D37E86BB8FA2EA00258881074DB3EDF23D1DEAE2900CFFB441F72FCEF0F1CAF87F80A7D627B339BDF9D70347A60E365E2DD0AEBBEA6208FFB045FCD35944BA569914C6B9FA9ED5623D077G6A9E45BB797F7CB916ED9B60C6BF0AED521BA5EEF77F
	31B6094589EB12C43FEE4AC5DD710F22AEBDBD4BA544C19D84A80B439838EA3B9483D1FEFD9FE0E36627364857F3489A0D373D6A0DF14CEF56B877759C26335A64ED1FBEAB07874C3E3ED3C646295D177318CFBE65BDC20C6F5FBE79BC7ACC3A3F7DFA3A96979FA74D4198BE1C62A749702C0CB71C40B21E82E98D0F615DBC3F25EBB491709E87B099E0BAC0766F204CFE07ED622D2B49007C3CF7AF365A6CBAB20015DD134364EB8A3470ECGD947G028146G0C870887188CB01D6BF9A31F0C113D353DAC7024
	26171567569AF227765034FBF475CBCB468CF69883CAFD762DB5683B2C0996CD63C42CE3FF2E41388F1A08F13F34161E550377C400E993B16EAFD8A345FDC2DEGED5FB23E2E949FE85BF7182EE509772F9C77F27F8A9DBAB1DDEBEB31FD2C0C47BCFE4AF176F84CE3BB4DE3843C4782A445E31B39E727DB1B897834477DE97BEE2AF7EBB3012758879F7363ADF58707286CD735765B0DDA5BF1AD8C8C25F2DF2843F22F83FB7A9EC71F9C667BB246BF4E6F0B20345360DDF8825A3CA7305C8F96CA65DE6057DA11
	78F7E50A305C4B1E88345C575135B0A6678EFE671D69893475FAFA06B08C5E2381921F40FEC0338A7B12E900537E9E735B348A735B76FB1451CFE9C743FB84C06C6FD14619556833A440E5A5200C47D6230C46841471D7CA9B846F21G639350E7916BA41F956F755FE7677672B56004C0FDD6D4A279AC1E0F9304A7517E23BADEB7B8EEFDA12F9BCFE2FB7AC2899917C5BC09ED5A38E2B2C7745626113BEA590C135FED32D6DE97E2C0CE62137E542124BCB9DFB2704C700BEF379BDF66D3D06ECF794327F13BCB
	3A82F89A7D62FBD3A767EB879E3B8FBE664753DB3C4F1D4FEE71BDF73EF48BA9ABA34CF9C9D5E738D1DB68F76E63471DC6BEC73FF2CB96476DF5B65B1CE632AA40E27B1AB22C9F41E0F34CD3B8CE2A6CA78D2D576FEBE4D11C2CEB0BDF58D86C3CD60675E17AD37CEFA6513F89D17CB4744FG6DCFE31DDA38916B148BF011CF73F6E8A34AC8FE1A37C314B6AF916691G3AC42CD31F4A565476EE757F5B5A082D44C7BB415B6463A0ABC3FDC07755CBE5EF9BA4DFC440FCECAC342B1A784210289CA3FDC97176C4
	659869C5BEB79F0034A909582F7DF71EDCFF43B3B017FC461FD8FF35CAE937D5648B02E7080A8CE6DFBC1DA3C7BC23347B990A0FFAC6E977B74B506ED8C84BFF165F15C777540CF81651B35CF10A8B93F1AC66FF3755FB5DFAF22B6F3A7542D675F52949DBBD5725DE5F2A3EAE7556D6793A944325ECA5752B8B665CED1C6EFD0EBB4CF1FF23B2896EEB3301FE83E6781954AE02EFB49205081FDDC56CBACDF1EDA2EE2E8CF7D844E5D6115807AC1B48BEDF719EF23A276C3859EAE56F0BA90D39871A8B6E0F4F2B
	080DE63BDEE6778A0EBB4C4BA26B14E779ACBEA5AF9F715C52DCCF3A6B664A69D83C9DDCB1387AF5EC10FC57864C4762F88B1DF347BC2B0C6FDA0A0FFFD6995F71FC6DE19224D5BE0775B2FF07DC7F98400FFF4E1FFAF944639E36B811AF891EA9AAB218FDAB685D8442A4255D05941FB9C9E977BDDA34BB875286A6E13DACE874AC4FF50D1E651E2EC2B7CF05EE368A5D5CC64F78F0AC9D5C5CB648A71F7A5CF602CFBEF5BF9F226B91D3BC7CFC1462D3BC7CBCEDA97AF9862435BF0F71717E67030B6B7F8C122F
	D275DEBA5F79FD1E2E1544BE2F4C6F2B941F703CB23FDD39FC0C8DE955AF605873833A0E9F866FB1GD3GE6GE43E88FD420BB89EFDE9BB797863EDBCFA50E623B7B0114B18E00EAD1FEE0BF70046AEA26BAE669E2B19D0990CFB0CBDBC7FB71F90786FBBE53FB71D986AFBCBBDFD2F9BD69D4CF7CF54F86FA71F29D13F9774459A753B05DF2971F6DFA7CB1FCAF5B95D7E4C906ACA29A1FD096DF40F6C0E5079855EEDDBDC603B8FDFDE205E076797F87661C5856AFDF8E901E79FDEA14AF5700BE2993E2A0044
	915B0B3DDF5F236F127DFD0EBB827A7BBDD28CBCB0C83E540079D87D3C1C2E03D43E282C37D7D3FC430B4AFA3B22966BEDB324C53D487B31E5EE6B9FC996CDF112BF7DD8316A5FAB2A93F9DB61E9CAD2EB4F17F8708D08F42361992E221B65AB112DEDA5A9737B870A0FCBD266370367B79152AA7E406FE15E6959DFF56E746C576E2F7724FB285E33DFDB229BDC39978C126F3B0079443B1E689AD1689F147ECB5BCF7061FFD07A6FF23E7EB49E5232DF62FF974B6DEF797481FE54CB7E444B5D9EF70D0609FCD1
	700CD711416CDBC06F221A7C12526E9C0A1F7612526E1B77215D53A12D6DE5347B23D5039BFFAC714927BE7EE87649271E5FF7685AD64C4B4A7C7E0D6263DFD666775CAABEBE05344A49185F0BD68FAE3E2E9CA45F93817331FCF565937B47CCD66657CE71911315797D102F71C5C1DA4EAB185F603DF27D83009F7D0ABFF179246A3D4861A2EFB4BC93DED10B8B03EABD1CA252CE07E7462BEAFCE505EAFCF9A2ED99BC9B94FC625829044C751C62FA1DF8E75CDA4FB66A5B351EED59FBF91EF45373BC693ED521
	6BD0217B7C0CA75D0AB31EF47F52F9524D5E6F59065E6071B7D57C2B1B63FC7229574DE41FFC6A31FA81DDA3EBFBD5992B17D13C61D5E52CAEAA47D835C35A24D7B1D617F87C2D917F6AD841A0793E8B18CF6CB368FADE65EB9EFD867DDB268D2FF97499FC2D30995222DE437CB6516F606CF72B6C77AB5B06FFEC7E37E15541E25AB44FB34E3B67F956078F681E141CA9B0461A426F8825677B1A615D44F1B7507188B9D7B9B085FD7854B249EFAF4AFE9FDBB67CBE6C9FD69DEC9DDB3F06440E389F0EAE25B1FC
	DE5FA7929E1752C43B2A35129FF649FEBF35E278FD7256306A105F9F6B9ED7F5BBBC634A319263AA0147503D2BB02ED2B96E10F4AF6C6B98D785ABA53F1548FEBF209DFE9FA67E84BA5ABF1772E4147DFEF769706B1E7B79706BF8B9D74AD3326CF7376ADDE2C32B7B12E1554146BD7FAE733E36B0275C775A427CF27535056CF24F350525656AEB8BAB4A65EB8B623DB935122F4748FE97548E3F5FEB06D5075876ACD3E9FB16F936BDBB699E8DF207EB6B6B7C4E3EDAECE70EF15CC23ACF0B5CE1B61E63CAEA31
	5D0A63B8DB9D36DB53B8EE55F214B71337DBAF48FC7E1A6C3769A7707F85BF010ED97B25BCFDAC7BFD15EE78F5C78E2B8E717E77A24F783A3648B33E9E5B07B1227BA346C33A8EE3444EF193791DD841FF4498D95CA479ED396C7758FD436F43581FC047DFD7C9F91AA97BFD712A6157FD63CF2023BBDF4A53A9596F7F2C9EFE5DB97943DF872E5F6BD9874276FA5601E4FE6F7FA49E6F7F570A71BE05632EAC40B6366C8D447D20C35E860E3B3090F91BB9EE66DE648D6338BD05489B45F161CD489B4BF1E37211
	B7AB197F4D3AF2293CC64BFEAFAD9F7E325B7893681835C94A5347325FD7ED7C895A67E15541765EE4EE733C2FE6B53F2FA69F4A39BA192D495813595D5162D95BEA753F99F3024EF3C7GEDC8B21E810D62F7B846A62B1F817DC8105D738AB4531225B320B313E5F772D17B2E6767D5B326CA76E92732FBFC199D13693C2892F08DD3510EEEFE7F2BFD2AEFBB0201A6F42AE4C764D4655D0074CF5C0977E2BDD40B32233D481EA5131DGB4D326CA770ACD1749E676D751F1943957CCF8934FC7FDDD4B4FC73DA9
	653B72CDF6D7A8638BE563AA40353F09795E3A9CED9B712677FBEF670A360D811A71EFCA790EFBD3697FF779D969C4199D535EE4F7CEB2BBCAE9FBA43CE551E43E05F63C4A4F0BD73E655B7FBA20B97616E4076BAD257F4BB6C97E3F2D95E587F911AD77FF8850C43CA55DCB967316527F8F96207F635F427DED4BAAA4FD579686F46FA34818A95317B5CD292F000EFF72GDFB88D75FD3CD996DFCD3E74494F5AB7020C76E912BE3B073E4772B1FF8353F08FE0935F1BB686F01326615E432ED57CEE0FCEDBF709
	39233B2477D4F7093E43D5E2B418DDD5D5DF76DA2D62E98D08B13AC7F084151579A70BA69F207ACF18A78D79D305E25B73731D7E6F3B1D4A6FDEE87DD3207BEEDF26BAD99E9FDD09F99C85F2627E04FB9A857AFDE5A63CE73FED51943F0D76CE121DEFEB8A603E189B79FDB18DEF87EAEF246CFCDBCAADBF1F2BAA67FCF7BE84EF15EC6F74FA46D97C3E443F23C6011E143751AFB1941F7FE7A8C30056BF23DF4ED4C93217AC775FAFA716E3FE2CFF8E54AFDFED134A7133A634B79C64CC7EB35A5BC27159A9D006
	G8DA9E86F1755123DB1816C11DF308E6DEDCF89545EB55B25F2FC21956504284AD1ABC71473EF59F92679BAEF6598184C97EA24B8F93AD6127F525A21113F31DE1279EE3EB719625E57C16E59BDBA48BD3BB3E95B1410227C5EB1076E59CDCAD1FE6F78BF5EAEA6C3DA63BBB876FD5B6DACCAA460E35F7167DB64632A5FA2FD733D98B09F4B170B36ED135ED166778C45CFF9C7195FF47E2DB985521A5F457C5E6AF6FEB08A70716F7A135F775D7602CC861ED43F78AED65DFB621B6F2A007918BF4A6AC83E535E
	D37AE9AB45E73D277453A33C4D4C0334207750CF3665F27D49001F711EBF794DD37D3B2F3E7956854C4772B511361519294A7CA6D2FCCE2AB23F07F91B1B8FE9A3D2B13F596B647AD3GAF3C6FCFFEDFEE1573E583CF21DFFC33E55F966749FE077C845F996F7A893EB34EEA70DCAFC8EF707C1E7E3DECAD68A2596F4C1FE04D26E0D8F50877646F717443EF76F82E1BE454F952FDD267692FADF5120FEAE53F63FF02F55E57FF02357CCB16FB7AE164F2CFBFBCA9DBA3FDC1767BE853707B21EFD8F530BC2FDF67
	690732F51EF17317D6CF3A3F374A69483DE10B042B0E3FB317746F429C17B02F45EE6E519E3053CBF29D42FC77D14D2C5B16DC14F545265E59C27AEEA17D385EEAB769778AF3CE119BE1F4426CCE27CB48559116D2E0A96DA4AC251431452E6FB4EEBBA2FCF45C5155C149DD426CA33B0F00907AF6D2B8ABE8F44247AE935DB7ED6CF33904A51224E632FBDACC5856E7A7C57A1F2020A0BB7CF3898B6B6DF6EB3F165E4D2B2517733A04BC3C49D7DBD85F59E934530FEA5A1D7D1DD6233348A5ACED519FB73A7A35
	0574B2AC59A5A3107631F46929CBD8C4EFB7EDA5371B166EB03BA8B9B8C9F61FA96463C013CB780CDC99282D37DA356C7EBFAD3DBE907C31EC37394720458B8399DADB385366F4D97BD7023A5C2F1CE0G3B93CECBAF05539EB49AFA696D78AEE181C9155D886792B4659BAB617F4DE5E5AEA13D3C0C7CF389334A41EE69F2E31710053741E9456B601C444333286392927EC39C87253B00176EDEFA78E64A29CDD51AB7EE554C9652768A696CE363DEA10DFCB104D2269F94778A9A174DCE0B00AC1065094F0FD5
	64938C3DD598E97EC769BE7603EE78B57C381C1D0F9B40AFADCABAEFD67BCB27E635FB3C8C46B7429078707C2564CBC3A096E70A20C2871534852A12D66F529A6925892B2B20CA2ED5DB01AA01D62347E9553B4816GBC6BCC7F4D1AAD4C79C6D8F41CDC57C79BC024B46E9596D2ACB68464482ECED862065B81CD0355C80E943804CFD9EBEB5577F415B2955BF4C2169BF913311F64E217CED408F8C6CE13D85BAC105C2D430B88357B0D5DB6C73F367E94B4FDA4875A427D9D7D509A96E92165F6587A2A28F39A
	B8677ADD507AF6C39E205D246DB36DBD34AD666F0DE20B6D5476E94B4B3E5BD424E4DD36DF5FE76E6E6D560A776F508B8D8913516E9409558BABCD8E8151859292BFCE0BE7E88F3ABA8BF847EF4CAE79BD0AF95997F97D3B2F499F0A77A7FEA85D89434ECCCD24FF7F3A629FB0177E871E11DAF202DC1A686DEFB834F46B2DD6175EEA296FB1103F6730030403007BD492CE9079CE249193D95AF3A7085DA7E4BC7F8FD0CB8788F8AD5A7CC9BAGG185AGGD0CB818294G94G88G88GF9F0E1AAF8AD5A7CC9
	BAGG185AGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG03BAGGGG
**end of data**/
}
/**
 * Return the JCheckBox6 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getCompressionCheckBox() {
	if (ivjCompressionCheckBox == null) {
		try {
			ivjCompressionCheckBox = new javax.swing.JCheckBox();
			ivjCompressionCheckBox.setName("CompressionCheckBox");
			ivjCompressionCheckBox.setText("Use Compression");
			ivjCompressionCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCompressionCheckBox;
}
/**
 * Return the CorrectnessPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getCorrectnessPanel() {
	if (ivjCorrectnessPanel == null) {
		try {
			ivjCorrectnessPanel = new javax.swing.JPanel();
			ivjCorrectnessPanel.setName("CorrectnessPanel");
			ivjCorrectnessPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Correctness Properties"));
			ivjCorrectnessPanel.setLayout(new java.awt.GridBagLayout());
			ivjCorrectnessPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSaftyRadioButton = new java.awt.GridBagConstraints();
			constraintsSaftyRadioButton.gridx = 0; constraintsSaftyRadioButton.gridy = 0;
			constraintsSaftyRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSaftyRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSaftyRadioButton.weightx = 1.0;
			constraintsSaftyRadioButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getCorrectnessPanel().add(getSaftyRadioButton(), constraintsSaftyRadioButton);

			java.awt.GridBagConstraints constraintsAssertionsCheckBox = new java.awt.GridBagConstraints();
			constraintsAssertionsCheckBox.gridx = 0; constraintsAssertionsCheckBox.gridy = 1;
			constraintsAssertionsCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAssertionsCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsAssertionsCheckBox.weightx = 1.0;
			constraintsAssertionsCheckBox.insets = new java.awt.Insets(10, 28, 0, 10);
			getCorrectnessPanel().add(getAssertionsCheckBox(), constraintsAssertionsCheckBox);

			java.awt.GridBagConstraints constraintsLivenessRadioButton = new java.awt.GridBagConstraints();
			constraintsLivenessRadioButton.gridx = 0; constraintsLivenessRadioButton.gridy = 2;
			constraintsLivenessRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsLivenessRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsLivenessRadioButton.weightx = 1.0;
			constraintsLivenessRadioButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getCorrectnessPanel().add(getLivenessRadioButton(), constraintsLivenessRadioButton);

			java.awt.GridBagConstraints constraintsNeverClaimCheckBox = new java.awt.GridBagConstraints();
			constraintsNeverClaimCheckBox.gridx = 0; constraintsNeverClaimCheckBox.gridy = 3;
			constraintsNeverClaimCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsNeverClaimCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsNeverClaimCheckBox.weightx = 1.0;
			constraintsNeverClaimCheckBox.insets = new java.awt.Insets(10, 28, 10, 10);
			getCorrectnessPanel().add(getNeverClaimCheckBox(), constraintsNeverClaimCheckBox);

			java.awt.GridBagConstraints constraintsFillerPanel1 = new java.awt.GridBagConstraints();
			constraintsFillerPanel1.gridx = 0; constraintsFillerPanel1.gridy = 4;
			constraintsFillerPanel1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFillerPanel1.weightx = 1.0;
			constraintsFillerPanel1.weighty = 1.0;
			constraintsFillerPanel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getCorrectnessPanel().add(getFillerPanel1(), constraintsFillerPanel1);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjCorrectnessPanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCorrectnessPanel;
}
/**
 * Return the JTextField3 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getDepthTextField() {
	if (ivjDepthTextField == null) {
		try {
			ivjDepthTextField = new javax.swing.JTextField();
			ivjDepthTextField.setName("DepthTextField");
			ivjDepthTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjDepthTextField.setText("100000");
			ivjDepthTextField.setSelectionColor(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDepthTextField;
}
/**
 * Return the JTextField4 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getErrorsTextField() {
	if (ivjErrorsTextField == null) {
		try {
			ivjErrorsTextField = new javax.swing.JTextField();
			ivjErrorsTextField.setName("ErrorsTextField");
			ivjErrorsTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjErrorsTextField.setText("1");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjErrorsTextField;
}
/**
 * Return the JRadioButton3 property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getExhaustiveRadioButton() {
	if (ivjExhaustiveRadioButton == null) {
		try {
			ivjExhaustiveRadioButton = new javax.swing.JRadioButton();
			ivjExhaustiveRadioButton.setName("ExhaustiveRadioButton");
			ivjExhaustiveRadioButton.setSelected(true);
			ivjExhaustiveRadioButton.setText("Exhaustive");
			ivjExhaustiveRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExhaustiveRadioButton;
}
/**
 * Return the FillerPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getFillerPanel1() {
	if (ivjFillerPanel1 == null) {
		try {
			ivjFillerPanel1 = new javax.swing.JPanel();
			ivjFillerPanel1.setName("FillerPanel1");
			ivjFillerPanel1.setLayout(null);
			ivjFillerPanel1.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFillerPanel1;
}
/**
 * Return the FillerPanel2 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getFillerPanel2() {
	if (ivjFillerPanel2 == null) {
		try {
			ivjFillerPanel2 = new javax.swing.JPanel();
			ivjFillerPanel2.setName("FillerPanel2");
			ivjFillerPanel2.setLayout(null);
			ivjFillerPanel2.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFillerPanel2;
}
/**
 * Return the FillerPanel3 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getFillerPanel3() {
	if (ivjFillerPanel3 == null) {
		try {
			ivjFillerPanel3 = new javax.swing.JPanel();
			ivjFillerPanel3.setName("FillerPanel3");
			ivjFillerPanel3.setLayout(null);
			ivjFillerPanel3.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFillerPanel3;
}
/**
 * Return the FillerPanel4 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getFillerPanel4() {
	if (ivjFillerPanel4 == null) {
		try {
			ivjFillerPanel4 = new javax.swing.JPanel();
			ivjFillerPanel4.setName("FillerPanel4");
			ivjFillerPanel4.setLayout(null);
			ivjFillerPanel4.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFillerPanel4;
}
/**
 * Return the JRadioButton5 property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getHashCompactRadioButton() {
	if (ivjHashCompactRadioButton == null) {
		try {
			ivjHashCompactRadioButton = new javax.swing.JRadioButton();
			ivjHashCompactRadioButton.setName("HashCompactRadioButton");
			ivjHashCompactRadioButton.setText("HashCompact");
			ivjHashCompactRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjHashCompactRadioButton;
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
			ivjJDialogContentPane.setBorder(new javax.swing.border.EtchedBorder());
			ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
			ivjJDialogContentPane.setBackground(new java.awt.Color(204,204,255));
			ivjJDialogContentPane.setForeground(java.awt.Color.gray);
			getJDialogContentPane().add(getOkbutton(), "South");
			getJDialogContentPane().add(getBottomPanel(), "Center");
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
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("Physical Memory Available (Mbytes) Approx:");
			ivjJLabel1.setForeground(java.awt.Color.black);
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
 * Return the JLabel2 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel2() {
	if (ivjJLabel2 == null) {
		try {
			ivjJLabel2 = new javax.swing.JLabel();
			ivjJLabel2.setName("JLabel2");
			ivjJLabel2.setText("Estimated State Space Size (States x 10^3):");
			ivjJLabel2.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel2;
}
/**
 * Return the JLabel3 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel3() {
	if (ivjJLabel3 == null) {
		try {
			ivjJLabel3 = new javax.swing.JLabel();
			ivjJLabel3.setName("JLabel3");
			ivjJLabel3.setText("Maximum Search Depth (Steps):");
			ivjJLabel3.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel3;
}
/**
 * Return the JLabel4 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel4() {
	if (ivjJLabel4 == null) {
		try {
			ivjJLabel4 = new javax.swing.JLabel();
			ivjJLabel4.setName("JLabel4");
			ivjJLabel4.setText("Stop at error #:");
			ivjJLabel4.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel4;
}
/**
 * Return the LabelPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getLabelPanel() {
	if (ivjLabelPanel == null) {
		try {
			ivjLabelPanel = new javax.swing.JPanel();
			ivjLabelPanel.setName("LabelPanel");
			ivjLabelPanel.setBorder(new javax.swing.border.EtchedBorder());
			ivjLabelPanel.setLayout(new java.awt.GridBagLayout());
			ivjLabelPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
			constraintsJLabel1.gridx = 0; constraintsJLabel1.gridy = 0;
			constraintsJLabel1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJLabel1.anchor = java.awt.GridBagConstraints.WEST;
			constraintsJLabel1.weighty = 1.0;
			constraintsJLabel1.insets = new java.awt.Insets(10, 10, 0, 10);
			getLabelPanel().add(getJLabel1(), constraintsJLabel1);

			java.awt.GridBagConstraints constraintsJLabel2 = new java.awt.GridBagConstraints();
			constraintsJLabel2.gridx = 0; constraintsJLabel2.gridy = 1;
			constraintsJLabel2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJLabel2.anchor = java.awt.GridBagConstraints.WEST;
			constraintsJLabel2.weighty = 1.0;
			constraintsJLabel2.insets = new java.awt.Insets(10, 10, 0, 10);
			getLabelPanel().add(getJLabel2(), constraintsJLabel2);

			java.awt.GridBagConstraints constraintsJLabel3 = new java.awt.GridBagConstraints();
			constraintsJLabel3.gridx = 0; constraintsJLabel3.gridy = 2;
			constraintsJLabel3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJLabel3.anchor = java.awt.GridBagConstraints.WEST;
			constraintsJLabel3.weighty = 1.0;
			constraintsJLabel3.insets = new java.awt.Insets(10, 10, 10, 10);
			getLabelPanel().add(getJLabel3(), constraintsJLabel3);

			java.awt.GridBagConstraints constraintsPhysicalTextField = new java.awt.GridBagConstraints();
			constraintsPhysicalTextField.gridx = 1; constraintsPhysicalTextField.gridy = 0;
			constraintsPhysicalTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPhysicalTextField.weightx = 1.0;
			constraintsPhysicalTextField.weighty = 1.0;
			constraintsPhysicalTextField.insets = new java.awt.Insets(10, 10, 0, 10);
			getLabelPanel().add(getPhysicalTextField(), constraintsPhysicalTextField);

			java.awt.GridBagConstraints constraintsStateTextField = new java.awt.GridBagConstraints();
			constraintsStateTextField.gridx = 1; constraintsStateTextField.gridy = 1;
			constraintsStateTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsStateTextField.weightx = 1.0;
			constraintsStateTextField.weighty = 1.0;
			constraintsStateTextField.insets = new java.awt.Insets(10, 10, 0, 10);
			getLabelPanel().add(getStateTextField(), constraintsStateTextField);

			java.awt.GridBagConstraints constraintsDepthTextField = new java.awt.GridBagConstraints();
			constraintsDepthTextField.gridx = 1; constraintsDepthTextField.gridy = 2;
			constraintsDepthTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDepthTextField.weightx = 1.0;
			constraintsDepthTextField.weighty = 1.0;
			constraintsDepthTextField.insets = new java.awt.Insets(10, 10, 10, 10);
			getLabelPanel().add(getDepthTextField(), constraintsDepthTextField);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLabelPanel;
}
/**
 * Return the JRadioButton2 property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getLivenessRadioButton() {
	if (ivjLivenessRadioButton == null) {
		try {
			ivjLivenessRadioButton = new javax.swing.JRadioButton();
			ivjLivenessRadioButton.setName("LivenessRadioButton");
			ivjLivenessRadioButton.setSelected(true);
			ivjLivenessRadioButton.setText("Liveness (Acceptance Cycles)");
			ivjLivenessRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLivenessRadioButton;
}
/**
 * Return the ModePanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getModePanel() {
	if (ivjModePanel == null) {
		try {
			ivjModePanel = new javax.swing.JPanel();
			ivjModePanel.setName("ModePanel");
			ivjModePanel.setOpaque(true);
			ivjModePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Search Mode"));
			ivjModePanel.setLayout(new java.awt.GridBagLayout());
			ivjModePanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsExhaustiveRadioButton = new java.awt.GridBagConstraints();
			constraintsExhaustiveRadioButton.gridx = 0; constraintsExhaustiveRadioButton.gridy = 0;
			constraintsExhaustiveRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsExhaustiveRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsExhaustiveRadioButton.weightx = 1.0;
			constraintsExhaustiveRadioButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getModePanel().add(getExhaustiveRadioButton(), constraintsExhaustiveRadioButton);

			java.awt.GridBagConstraints constraintsSupertraceRadioButton = new java.awt.GridBagConstraints();
			constraintsSupertraceRadioButton.gridx = 0; constraintsSupertraceRadioButton.gridy = 2;
			constraintsSupertraceRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSupertraceRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSupertraceRadioButton.weightx = 1.0;
			constraintsSupertraceRadioButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getModePanel().add(getSupertraceRadioButton(), constraintsSupertraceRadioButton);

			java.awt.GridBagConstraints constraintsHashCompactRadioButton = new java.awt.GridBagConstraints();
			constraintsHashCompactRadioButton.gridx = 0; constraintsHashCompactRadioButton.gridy = 3;
			constraintsHashCompactRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsHashCompactRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsHashCompactRadioButton.weightx = 1.0;
			constraintsHashCompactRadioButton.insets = new java.awt.Insets(10, 10, 10, 10);
			getModePanel().add(getHashCompactRadioButton(), constraintsHashCompactRadioButton);

			java.awt.GridBagConstraints constraintsFillerPanel2 = new java.awt.GridBagConstraints();
			constraintsFillerPanel2.gridx = 0; constraintsFillerPanel2.gridy = 4;
			constraintsFillerPanel2.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFillerPanel2.weightx = 1.0;
			constraintsFillerPanel2.weighty = 1.0;
			constraintsFillerPanel2.insets = new java.awt.Insets(4, 4, 4, 4);
			getModePanel().add(getFillerPanel2(), constraintsFillerPanel2);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjModePanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjModePanel;
}
/**
 * Return the JCheckBox2 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getNeverClaimCheckBox() {
	if (ivjNeverClaimCheckBox == null) {
		try {
			ivjNeverClaimCheckBox = new javax.swing.JCheckBox();
			ivjNeverClaimCheckBox.setName("NeverClaimCheckBox");
			ivjNeverClaimCheckBox.setSelected(true);
			ivjNeverClaimCheckBox.setText("Apply Never Claim");
			ivjNeverClaimCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNeverClaimCheckBox;
}
/**
 * Return the Okbutton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOkbutton() {
	if (ivjOkbutton == null) {
		try {
			ivjOkbutton = new javax.swing.JButton();
			ivjOkbutton.setName("Okbutton");
			ivjOkbutton.setBorder(new javax.swing.border.EtchedBorder());
			ivjOkbutton.setText("OK");
			ivjOkbutton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOkbutton;
}
/**
 * Return the JCheckBox5 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getPartialOrderCheckBox() {
	if (ivjPartialOrderCheckBox == null) {
		try {
			ivjPartialOrderCheckBox = new javax.swing.JCheckBox();
			ivjPartialOrderCheckBox.setName("PartialOrderCheckBox");
			ivjPartialOrderCheckBox.setText("Use Partial Order Reduction");
			ivjPartialOrderCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPartialOrderCheckBox;
}
/**
 * Return the JTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getPhysicalTextField() {
	if (ivjPhysicalTextField == null) {
		try {
			ivjPhysicalTextField = new javax.swing.JTextField();
			ivjPhysicalTextField.setName("PhysicalTextField");
			ivjPhysicalTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjPhysicalTextField.setText("128");
			ivjPhysicalTextField.setSelectionColor(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPhysicalTextField;
}
/**
 * Return the ResourceBoundedCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getResourceBoundedCheckBox() {
	if (ivjResourceBoundedCheckBox == null) {
		try {
			ivjResourceBoundedCheckBox = new javax.swing.JCheckBox();
			ivjResourceBoundedCheckBox.setName("ResourceBoundedCheckBox");
			ivjResourceBoundedCheckBox.setSelected(true);
			ivjResourceBoundedCheckBox.setOpaque(false);
			ivjResourceBoundedCheckBox.setText("Resource Bounded");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResourceBoundedCheckBox;
}
/**
 * Return the RunPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getRunPanel() {
	if (ivjRunPanel == null) {
		try {
			ivjRunPanel = new javax.swing.JPanel();
			ivjRunPanel.setName("RunPanel");
			ivjRunPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Type of Run"));
			ivjRunPanel.setLayout(new java.awt.GridBagLayout());
			ivjRunPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsPartialOrderCheckBox = new java.awt.GridBagConstraints();
			constraintsPartialOrderCheckBox.gridx = 0; constraintsPartialOrderCheckBox.gridy = 0;
			constraintsPartialOrderCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPartialOrderCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsPartialOrderCheckBox.weightx = 1.0;
			constraintsPartialOrderCheckBox.insets = new java.awt.Insets(10, 10, 0, 10);
			getRunPanel().add(getPartialOrderCheckBox(), constraintsPartialOrderCheckBox);

			java.awt.GridBagConstraints constraintsCompressionCheckBox = new java.awt.GridBagConstraints();
			constraintsCompressionCheckBox.gridx = 0; constraintsCompressionCheckBox.gridy = 1;
			constraintsCompressionCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsCompressionCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsCompressionCheckBox.weightx = 1.0;
			constraintsCompressionCheckBox.insets = new java.awt.Insets(10, 10, 0, 10);
			getRunPanel().add(getCompressionCheckBox(), constraintsCompressionCheckBox);

			java.awt.GridBagConstraints constraintsFillerPanel4 = new java.awt.GridBagConstraints();
			constraintsFillerPanel4.gridx = 0; constraintsFillerPanel4.gridy = 4;
			constraintsFillerPanel4.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFillerPanel4.weightx = 1.0;
			constraintsFillerPanel4.weighty = 1.0;
			constraintsFillerPanel4.insets = new java.awt.Insets(4, 4, 4, 4);
			getRunPanel().add(getFillerPanel4(), constraintsFillerPanel4);

			java.awt.GridBagConstraints constraintsResourceBoundedCheckBox = new java.awt.GridBagConstraints();
			constraintsResourceBoundedCheckBox.gridx = 0; constraintsResourceBoundedCheckBox.gridy = 2;
			constraintsResourceBoundedCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsResourceBoundedCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsResourceBoundedCheckBox.weightx = 1.0;
			constraintsResourceBoundedCheckBox.insets = new java.awt.Insets(10, 10, 10, 10);
			getRunPanel().add(getResourceBoundedCheckBox(), constraintsResourceBoundedCheckBox);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjRunPanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRunPanel;
}
/**
 * Return the JRadioButton1 property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getSaftyRadioButton() {
	if (ivjSaftyRadioButton == null) {
		try {
			ivjSaftyRadioButton = new javax.swing.JRadioButton();
			ivjSaftyRadioButton.setName("SaftyRadioButton");
			ivjSaftyRadioButton.setText("Safety (State Properties)");
			ivjSaftyRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaftyRadioButton;
}
/**
 * Return the JCheckBox3 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getSaveTrailCheckBox() {
	if (ivjSaveTrailCheckBox == null) {
		try {
			ivjSaveTrailCheckBox = new javax.swing.JCheckBox();
			ivjSaveTrailCheckBox.setName("SaveTrailCheckBox");
			ivjSaveTrailCheckBox.setSelected(true);
			ivjSaveTrailCheckBox.setText("Save All Errors Trails");
			ivjSaveTrailCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveTrailCheckBox;
}
/**
 * Return the JCheckBox4 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getShortestCheckBox() {
	if (ivjShortestCheckBox == null) {
		try {
			ivjShortestCheckBox = new javax.swing.JCheckBox();
			ivjShortestCheckBox.setName("ShortestCheckBox");
			ivjShortestCheckBox.setText("Find Shortest Trail (Costly!)");
			ivjShortestCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShortestCheckBox;
}
/**
 * Return the JTextField2 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getStateTextField() {
	if (ivjStateTextField == null) {
		try {
			ivjStateTextField = new javax.swing.JTextField();
			ivjStateTextField.setName("StateTextField");
			ivjStateTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjStateTextField.setText("500");
			ivjStateTextField.setSelectionColor(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStateTextField;
}
/**
 * Return the JRadioButton4 property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getSupertraceRadioButton() {
	if (ivjSupertraceRadioButton == null) {
		try {
			ivjSupertraceRadioButton = new javax.swing.JRadioButton();
			ivjSupertraceRadioButton.setName("SupertraceRadioButton");
			ivjSupertraceRadioButton.setText("SuperTrace/BitState");
			ivjSupertraceRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSupertraceRadioButton;
}
/**
 * Return the TrapPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getTrapPanel() {
	if (ivjTrapPanel == null) {
		try {
			ivjTrapPanel = new javax.swing.JPanel();
			ivjTrapPanel.setName("TrapPanel");
			ivjTrapPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Error Trapping"));
			ivjTrapPanel.setLayout(new java.awt.GridBagLayout());
			ivjTrapPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsJLabel4 = new java.awt.GridBagConstraints();
			constraintsJLabel4.gridx = 0; constraintsJLabel4.gridy = 0;
			constraintsJLabel4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJLabel4.insets = new java.awt.Insets(10, 10, 10, 10);
			getTrapPanel().add(getJLabel4(), constraintsJLabel4);

			java.awt.GridBagConstraints constraintsSaveTrailCheckBox = new java.awt.GridBagConstraints();
			constraintsSaveTrailCheckBox.gridx = 0; constraintsSaveTrailCheckBox.gridy = 1;
			constraintsSaveTrailCheckBox.gridwidth = 3;
			constraintsSaveTrailCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSaveTrailCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSaveTrailCheckBox.insets = new java.awt.Insets(10, 10, 0, 10);
			getTrapPanel().add(getSaveTrailCheckBox(), constraintsSaveTrailCheckBox);

			java.awt.GridBagConstraints constraintsShortestCheckBox = new java.awt.GridBagConstraints();
			constraintsShortestCheckBox.gridx = 0; constraintsShortestCheckBox.gridy = 2;
			constraintsShortestCheckBox.gridwidth = 3;
			constraintsShortestCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsShortestCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsShortestCheckBox.insets = new java.awt.Insets(10, 10, 10, 10);
			getTrapPanel().add(getShortestCheckBox(), constraintsShortestCheckBox);

			java.awt.GridBagConstraints constraintsErrorsTextField = new java.awt.GridBagConstraints();
			constraintsErrorsTextField.gridx = 1; constraintsErrorsTextField.gridy = 0;
			constraintsErrorsTextField.gridwidth = 2;
			constraintsErrorsTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsErrorsTextField.weightx = 1.0;
			constraintsErrorsTextField.insets = new java.awt.Insets(10, 10, 10, 10);
			getTrapPanel().add(getErrorsTextField(), constraintsErrorsTextField);

			java.awt.GridBagConstraints constraintsFillerPanel3 = new java.awt.GridBagConstraints();
			constraintsFillerPanel3.gridx = 0; constraintsFillerPanel3.gridy = 3;
			constraintsFillerPanel3.gridwidth = 3;
			constraintsFillerPanel3.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFillerPanel3.weightx = 1.0;
			constraintsFillerPanel3.weighty = 1.0;
			constraintsFillerPanel3.insets = new java.awt.Insets(4, 4, 4, 4);
			getTrapPanel().add(getFillerPanel3(), constraintsFillerPanel3);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjTrapPanel.getBorder()).setTitleColor(Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTrapPanel;
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
public void hashCompactRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setSearchMode(SpinOptions.HashCompact);
	//return;
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getNeverClaimCheckBox().addActionListener(ivjEventHandler);
	getLivenessRadioButton().addActionListener(ivjEventHandler);
	getSaftyRadioButton().addActionListener(ivjEventHandler);
	getExhaustiveRadioButton().addActionListener(ivjEventHandler);
	getSupertraceRadioButton().addActionListener(ivjEventHandler);
	getHashCompactRadioButton().addActionListener(ivjEventHandler);
	getOkbutton().addActionListener(ivjEventHandler);
	getSaveTrailCheckBox().addActionListener(ivjEventHandler);
	getShortestCheckBox().addActionListener(ivjEventHandler);
	getPartialOrderCheckBox().addActionListener(ivjEventHandler);
	getCompressionCheckBox().addActionListener(ivjEventHandler);
	getPhysicalTextField().addFocusListener(ivjEventHandler);
	getStateTextField().addFocusListener(ivjEventHandler);
	getDepthTextField().addFocusListener(ivjEventHandler);
	getErrorsTextField().addFocusListener(ivjEventHandler);
	getAssertionsCheckBox().addActionListener(ivjEventHandler);
	getResourceBoundedCheckBox().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SpinOption");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setBackground(java.awt.Color.gray);
		setVisible(false);
		setForeground(java.awt.Color.gray);
		setSize(495, 491);
		setTitle("Spin Option");
		setContentPane(getJDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	ButtonGroup group = new ButtonGroup();
	group.add(getExhaustiveRadioButton());
	group.add(getSupertraceRadioButton());
	group.add(getHashCompactRadioButton());
	
	group = new ButtonGroup();
	group.add(getSaftyRadioButton());
	group.add(getLivenessRadioButton());
	
	// user code end
}
/**
 * Comment
 */
public void livenessRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setAcceptanceCycles(true);
	//spinOptions.setAssertions(false);
	spinOptions.setSafety(false);
	//return;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		SpinOption aSpinOption;
		aSpinOption = new SpinOption();
		aSpinOption.setModal(true);
		aSpinOption.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aSpinOption.setVisible(true);
	} catch (Throwable exception) {
		log.error("Exception occurred in main() of javax.swing.JDialog", exception);
	}
}
/**
 * Comment
 */
public void neverClaimCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		spinOptions.setApplyNeverClaim(getNeverClaimCheckBox().isSelected());
	//return;
}
/**
 * Comment
 */
public void okbutton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	setVisible(false);
	//gui.requestFocus();
	//return;
}

    /**
     * Parse the compile options as they would be seen on the command line.  This will make use
     * of the parsing logic in the edu.ksu.cis.bandera.spin.SpinOption class.  The relevant
     * entries in the compiler options that will be displayed to the user are:
     * <ul>
     * <li>Search Mode:
     * <ul>
     * <li>BitState (or -DBITSTATE on the command line)</li>
     * <li>Exhaustive (default)</li>
     * <li>Hash compact (or -DHC on the command line)</li>
     * </ul>
     * </li>
     * <li>Memory Limit (or -DMEMLIMIT on the command line)</li>
     * <li>Safety property (or -DSAFETY on the command line)</li>
     * <li>Compression (or -DCOLLAPSE on the command line)</li>
     * <li>Resource Bounded (or -DNORESOURCEBOUNDED on the command line)</li>
     * </ul>
     *
     * @param String compileOptions The command line options to use with the compiler.
     * @pre spinOptions != null
     */
    public void parseCompileOptions(String compileOptions) {
	
	if(spinOptions == null) {
	    log.error("spinOptions is null.  Cannot parse compile options without a valid spinOptions.");
	    return;
	}

	spinOptions.parseCompileOptions(compileOptions);
	    
	switch(spinOptions.getSearchMode()) {
		
	case SpinOptions.SuperTrace:
	    getSupertraceRadioButton().setSelected(true);
	    break;
		
	case SpinOptions.HashCompact:
	    getHashCompactRadioButton().setSelected(true);
	    break;
	    
	case SpinOptions.Exhaustive:
	    getExhaustiveRadioButton().setSelected(true);
	    break;
	    
	default:
	    log.debug("A new search mode must have been added with a value of " +
			       spinOptions.getSearchMode());
	}
	
	getPhysicalTextField().setText(Integer.toString(spinOptions.getMemoryLimit()));
	getSaftyRadioButton().setSelected(spinOptions.getSafety());
	getCompressionCheckBox().setSelected(spinOptions.getCompression());
	getResourceBoundedCheckBox().setSelected(spinOptions.getResourceBounded());
	

	/* ************************************************************************ */
	/* The following section was removed since it was a duplication of effort with
	 * SpinOptions.parsepaneOptions() -tcw

	Vector vector = new Vector();
	StringTokenizer st = new StringTokenizer(param);
	while(st.hasMoreTokens()){
		vector.add(st.nextToken());
	}
	for(int i =0; i < vector.size(); i++){
		if(((String)vector.elementAt(i)).equals("-DBITSTATE")){
			getSupertraceRadioButton().setSelected(true);
			spinOptions.setSearchMode(SpinOptions.SuperTrace);
		}
		if(((String)vector.elementAt(i)).equals("-DHC")){
				getHashCompactRadioButton().setSelected(true);
				spinOptions.setSearchMode(SpinOptions.HashCompact);
		}
		if(((String)vector.elementAt(i)).equals("-DMEMLIM")){
			i++;
			if(i == vector.size()){
				System.out.println("Your should also specify the value after DMEMLIM");
				System.exit(0);
			}
			else {
				String temp = (String)vector.elementAt(i);
				getPhysicalTextField().setText(temp);
				try {
				spinOptions.setMemoryLimit(Integer.parseInt(temp));
				} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				getPhysicalTextField().setText("" + spinOptions.getMemoryLimit());
				}
			}
		}
		if(((String)vector.elementAt(i)).equals("-DSAFETY")){
			getSaftyRadioButton().setSelected(true);
			spinOptions.setAcceptanceCycles(false);
			//spinOptions.setAssertions(getAssertionsCheckBox().isSelected());
			spinOptions.setSafety(true);
		}
		if(((String)vector.elementAt(i)).equals("-DNOREDUCE")){}
		if(((String)vector.elementAt(i)).equals("-DCOLLAPSE")){
			getCompressionCheckBox().setSelected(true);
			spinOptions.setCompression(getCompressionCheckBox().isSelected());
		}
		if(((String)vector.elementAt(i)).equals("-DNORESOURCEBOUNDED")) {
			getResourceBoundedCheckBox().setSelected(false);
			spinOptions.setResourceBounded(getResourceBoundedCheckBox().isSelected());
		}
	}
	*/
	/* ************************************************************************ */
    }

    /**
     * Parse the pan options as they would be seen on the command line.  This will make use
     * of the parsing logic in the edu.ksu.cis.bandera.spin.SpinOption class.  The relevant
     * entries in the Spin options that will be displayed to the user are:
     * <ul>
     * <li>Search Depth (or -mN on the command line)</li>
     * <li>Acceptance cycles (or ? on the command line)</li>
     * <li>Assertions (or -A on the command line)</li>
     * <li>Error to stop on (or -cN on the command line)</li>
     * <li>Save all trails (or ? on the command line)</li>
     * <li>Find the shortest path (or ? on the command line)</li>
     * </ul>
     *
     * @param String panOptions The command line options to use with pan.
     * @pre spinOptions != null
     */
    public void parsepaneOptions(String panOptions) {

	if(spinOptions == null) {
	    log.error("spinOptions is null.  Cannot parse pan options without a valid spinOptions.");
	    return;
	}

	spinOptions.parsepaneOptions(panOptions);
	getDepthTextField().setText(Integer.toString(spinOptions.getSearchDepth()));
	getLivenessRadioButton().setSelected(spinOptions.getAcceptanceCycles());
	getAssertionsCheckBox().setSelected(spinOptions.getAssertions());
	getErrorsTextField().setText(Integer.toString(spinOptions.getStopAtError()));
	getSaveTrailCheckBox().setSelected(spinOptions.getSaveAllTrails());
	getShortestCheckBox().setSelected(spinOptions.getFindShortestTrail());

	/* ************************************************************************ */
	/* The following section was removed since it was a duplication of effort with
	 * SpinOptions.parsepaneOptions() -tcw

	Vector vector = new Vector();
	StringTokenizer st = new StringTokenizer(para);
	while(st.hasMoreTokens()){
	    vector.add(st.nextToken());
	}
	for(int i =0; i < vector.size(); i++){
	    if(((String)vector.elementAt(i)).equals("-n")){
		i++;
		if( i==vector.size() || !((String)(vector.elementAt(i))).startsWith("-m")){
		    System.out.println("Your input should be -n -mSearchDepth");
		    
		}
		else{
		    String temp = ((String)vector.elementAt(i)).substring(2);
		    getDepthTextField().setText(temp);
		    try{
			spinOptions.setSearchDepth(Integer.parseInt(temp));
		    }catch(Exception e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			getDepthTextField().setText(""+spinOptions.getSearchDepth());
		    }
		}
	    }
	    if(((String)vector.elementAt(i)).equals("-a")){
		getLivenessRadioButton().setSelected(true);
		spinOptions.setAcceptanceCycles(true);
	    }
	    if(((String)vector.elementAt(i)).equals("-A")){
		getAssertionsCheckBox().setSelected(false);
		//spinOptions.setAssertions(false);
	    }
	    if(((String)vector.elementAt(i)).equals("-c")){		
		i++;
		if( i==vector.size()){
		    System.out.println("Your input should be -c StopAtErrors");
		    
		}
		else{
		    String temp = (String)vector.elementAt(i);
		    getErrorsTextField().setText(temp);
		    try{
			spinOptions.setStopAtError(Integer.parseInt(temp));
		    }catch(Exception e){
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			getErrorsTextField().setText(""+spinOptions.getStopAtError());
		    }
		}
	    }
	    if(((String)vector.elementAt(i)).equals("-e")){
		getSaveTrailCheckBox().setSelected(true);
		spinOptions.setSaveAllTrails(true);
	    }
	    if(((String)vector.elementAt(i)).equals("-I")){
		getShortestCheckBox().setSelected(true);
		spinOptions.setFindShortestTrail(true);
	    }
	}
	*/
	/* ************************************************************************ */

    }

/**
 * Comment
 */
public void partialOrderCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setPOReduction(getPartialOrderCheckBox().isSelected());
	//return;
}
/**
 * Comment
 */
public void physicalFieldMemory_FocusLost(java.awt.event.FocusEvent focusEvent) {
	try {
		spinOptions.setMemoryLimit(Integer.parseInt(getPhysicalTextField().getText()));
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		getPhysicalTextField().setText("" + spinOptions.getMemoryLimit());
	}
	//return;
}
/**
 * Comment
 */
public void physicalTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	try {
		spinOptions.setMemoryLimit(Integer.parseInt(getPhysicalTextField().getText()));
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		getPhysicalTextField().setText("" + spinOptions.getMemoryLimit());
	}
	//return;
}
/**
 * Comment
 */
public void resourceBoundedCheckBox_ActionPerformed() {
	spinOptions.setResourceBounded(getResourceBoundedCheckBox().isSelected());;
}
/**
 * Comment
 */
public void saftyRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setAcceptanceCycles(false);
	//spinOptions.setAssertions(getAssertionsCheckBox().isSelected());
	spinOptions.setSafety(true);
	//return;
}
/**
 * Comment
 */
public void saverailCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		spinOptions.setSaveAllTrails(getSaveTrailCheckBox().isSelected());
	//return;
}
public void setAcceptanceCycles(boolean b) { getLivenessRadioButton().setSelected(b);}
public void setApplyNeverClaim(boolean b) { getNeverClaimCheckBox().setSelected(b); }
	public void setSafety(boolean b) { getSaftyRadioButton().setSelected(b); }
/**
 * Comment
 */
public void shortestCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setFindShortestTrail(getShortestCheckBox().isSelected());
	//return;
}
/**
 * Comment
 */
public void stateTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	try {
		spinOptions.setSpaceEstimate(Integer.parseInt(getStateTextField().getText()));
	} catch (Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		getStateTextField().setText("" + spinOptions.getSpaceEstimate());
	}
	//return;
}
/**
 * Comment
 */
public void supertraceRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setSearchMode(SpinOptions.SuperTrace);
	//return;
}
}
