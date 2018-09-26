package edu.ksu.cis.bandera.checker.spin;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import edu.ksu.cis.bandera.checker.spin.SpinOptions;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsView;
import edu.ksu.cis.bandera.checker.CompletionListener;

import org.apache.log4j.Category;

/**
 * The SpinOption class is a view of the SpinOptions model that are available
 * which represents the command line options that are available for use
 * with the Spin model checker.
 *
 * @see edu.ksu.cis.bandera.checker.spin.SpinOptions
 * @see edu.ksu.cis.bandera.checker.OptionsView
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class SpinOptionsView extends JDialog implements edu.ksu.cis.bandera.checker.OptionsView, java.awt.event.ActionListener, java.awt.event.FocusListener {
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
    public static SpinOptions spinOptions;
    /**
     * The log that we will be writing to.
     */
    private static final Category log = Category.getInstance(SpinOptionsView.class);
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
	private JCheckBox ivjResourceBoundedCheckBox = null;
	private java.util.Set completionListenerSet;
	private JLabel ivjVectorSizeLabel = null;
	private JTextField ivjVectorSizeTextField = null;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.FocusListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SpinOptionsView.this.getNeverClaimCheckBox()) 
				connEtoC1(e);
			if (e.getSource() == SpinOptionsView.this.getLivenessRadioButton()) 
				connEtoC2(e);
			if (e.getSource() == SpinOptionsView.this.getSaftyRadioButton()) 
				connEtoC3(e);
			if (e.getSource() == SpinOptionsView.this.getExhaustiveRadioButton()) 
				connEtoC4(e);
			if (e.getSource() == SpinOptionsView.this.getSupertraceRadioButton()) 
				connEtoC5(e);
			if (e.getSource() == SpinOptionsView.this.getHashCompactRadioButton()) 
				connEtoC6(e);
			if (e.getSource() == SpinOptionsView.this.getOkbutton()) 
				connEtoC7(e);
			if (e.getSource() == SpinOptionsView.this.getSaveTrailCheckBox()) 
				connEtoC8(e);
			if (e.getSource() == SpinOptionsView.this.getShortestCheckBox()) 
				connEtoC9(e);
			if (e.getSource() == SpinOptionsView.this.getPartialOrderCheckBox()) 
				connEtoC10(e);
			if (e.getSource() == SpinOptionsView.this.getCompressionCheckBox()) 
				connEtoC11(e);
			if (e.getSource() == SpinOptionsView.this.getAssertionsCheckBox()) 
				connEtoC16();
			if (e.getSource() == SpinOptionsView.this.getResourceBoundedCheckBox()) 
				connEtoC17(e);
		};
		public void focusGained(java.awt.event.FocusEvent e) {};
		public void focusLost(java.awt.event.FocusEvent e) {
			if (e.getSource() == SpinOptionsView.this.getPhysicalTextField()) 
				connEtoC12(e);
			if (e.getSource() == SpinOptionsView.this.getStateTextField()) 
				connEtoC13(e);
			if (e.getSource() == SpinOptionsView.this.getDepthTextField()) 
				connEtoC14(e);
			if (e.getSource() == SpinOptionsView.this.getErrorsTextField()) 
				connEtoC15(e);
			if (e.getSource() == SpinOptionsView.this.getVectorSizeTextField()) 
				connEtoC18(e);
		};
	};
/*			SpinOption.spinOptions.setApplyNeverClaim(false);
			SpinOption.spinOptions.setAcceptanceCycles(false);
			SpinOption.spinOptions.setSafety(true);*/
/**
 * SpinOptionDlg constructor comment.
 */
public SpinOptionsView() {
	super();
	initialize();
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
     * Remove all listeners that have been registered.
     */
public void clearCompletionListeners() {
	if(completionListenerSet != null) {
	    completionListenerSet.clear();
	}
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
 * connEtoC18:  (VectorSizeTextField.focus.focusLost(java.awt.event.FocusEvent) --> SpinOptionsView.vectorSizeTextField_FocusLost()V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC18(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.vectorSizeTextField_FocusLost();
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
	spinOptions.setSearchMode(SpinOptions.EXHAUSTIVE_SEARCH_MODE);
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
			ivjAssertionsCheckBox.setSelected(false);
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
			ivjDepthTextField.setToolTipText("Set max search depth to N steps (default N=10000)");
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
			ivjExhaustiveRadioButton.setSelected(false);
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
			ivjJDialogContentPane.setLayout(new java.awt.GridBagLayout());
			ivjJDialogContentPane.setBackground(new java.awt.Color(204,204,255));
			ivjJDialogContentPane.setForeground(java.awt.Color.gray);

			java.awt.GridBagConstraints constraintsCorrectnessPanel = new java.awt.GridBagConstraints();
			constraintsCorrectnessPanel.gridx = 0; constraintsCorrectnessPanel.gridy = 0;
			constraintsCorrectnessPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsCorrectnessPanel.weightx = 1.0;
			constraintsCorrectnessPanel.weighty = 1.0;
			constraintsCorrectnessPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getCorrectnessPanel(), constraintsCorrectnessPanel);

			java.awt.GridBagConstraints constraintsModePanel = new java.awt.GridBagConstraints();
			constraintsModePanel.gridx = 1; constraintsModePanel.gridy = 0;
			constraintsModePanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsModePanel.weightx = 1.0;
			constraintsModePanel.weighty = 1.0;
			constraintsModePanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getModePanel(), constraintsModePanel);

			java.awt.GridBagConstraints constraintsLabelPanel = new java.awt.GridBagConstraints();
			constraintsLabelPanel.gridx = 0; constraintsLabelPanel.gridy = 1;
			constraintsLabelPanel.gridwidth = 2;
			constraintsLabelPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsLabelPanel.weightx = 1.0;
			constraintsLabelPanel.weighty = 1.0;
			constraintsLabelPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getLabelPanel(), constraintsLabelPanel);

			java.awt.GridBagConstraints constraintsTrapPanel = new java.awt.GridBagConstraints();
			constraintsTrapPanel.gridx = 0; constraintsTrapPanel.gridy = 2;
			constraintsTrapPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsTrapPanel.weightx = 1.0;
			constraintsTrapPanel.weighty = 1.0;
			constraintsTrapPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getTrapPanel(), constraintsTrapPanel);

			java.awt.GridBagConstraints constraintsRunPanel = new java.awt.GridBagConstraints();
			constraintsRunPanel.gridx = 1; constraintsRunPanel.gridy = 2;
			constraintsRunPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsRunPanel.weightx = 1.0;
			constraintsRunPanel.weighty = 1.0;
			constraintsRunPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getRunPanel(), constraintsRunPanel);

			java.awt.GridBagConstraints constraintsOkbutton = new java.awt.GridBagConstraints();
			constraintsOkbutton.gridx = 0; constraintsOkbutton.gridy = 3;
			constraintsOkbutton.gridwidth = 2;
			constraintsOkbutton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getOkbutton(), constraintsOkbutton);
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
			ivjJLabel2.setText("Estimated State Space Size (2^N Hashtable entries, default = 18):");
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
			ivjJLabel3.setToolTipText("Set max search depth to N steps (default N=10000)");
			ivjJLabel3.setText("Maximum Search Depth (steps) (default = 100000)");
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
			constraintsJLabel1.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getJLabel1(), constraintsJLabel1);

			java.awt.GridBagConstraints constraintsJLabel2 = new java.awt.GridBagConstraints();
			constraintsJLabel2.gridx = 0; constraintsJLabel2.gridy = 1;
			constraintsJLabel2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJLabel2.anchor = java.awt.GridBagConstraints.WEST;
			constraintsJLabel2.weighty = 1.0;
			constraintsJLabel2.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getJLabel2(), constraintsJLabel2);

			java.awt.GridBagConstraints constraintsJLabel3 = new java.awt.GridBagConstraints();
			constraintsJLabel3.gridx = 0; constraintsJLabel3.gridy = 2;
			constraintsJLabel3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJLabel3.anchor = java.awt.GridBagConstraints.WEST;
			constraintsJLabel3.weighty = 1.0;
			constraintsJLabel3.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getJLabel3(), constraintsJLabel3);

			java.awt.GridBagConstraints constraintsPhysicalTextField = new java.awt.GridBagConstraints();
			constraintsPhysicalTextField.gridx = 1; constraintsPhysicalTextField.gridy = 0;
			constraintsPhysicalTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPhysicalTextField.weightx = 1.0;
			constraintsPhysicalTextField.weighty = 1.0;
			constraintsPhysicalTextField.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getPhysicalTextField(), constraintsPhysicalTextField);

			java.awt.GridBagConstraints constraintsStateTextField = new java.awt.GridBagConstraints();
			constraintsStateTextField.gridx = 1; constraintsStateTextField.gridy = 1;
			constraintsStateTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsStateTextField.weightx = 1.0;
			constraintsStateTextField.weighty = 1.0;
			constraintsStateTextField.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getStateTextField(), constraintsStateTextField);

			java.awt.GridBagConstraints constraintsDepthTextField = new java.awt.GridBagConstraints();
			constraintsDepthTextField.gridx = 1; constraintsDepthTextField.gridy = 2;
			constraintsDepthTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDepthTextField.weightx = 1.0;
			constraintsDepthTextField.weighty = 1.0;
			constraintsDepthTextField.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getDepthTextField(), constraintsDepthTextField);

			java.awt.GridBagConstraints constraintsVectorSizeLabel = new java.awt.GridBagConstraints();
			constraintsVectorSizeLabel.gridx = 0; constraintsVectorSizeLabel.gridy = 3;
			constraintsVectorSizeLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsVectorSizeLabel.weighty = 1.0;
			constraintsVectorSizeLabel.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getVectorSizeLabel(), constraintsVectorSizeLabel);

			java.awt.GridBagConstraints constraintsVectorSizeTextField = new java.awt.GridBagConstraints();
			constraintsVectorSizeTextField.gridx = 1; constraintsVectorSizeTextField.gridy = 3;
			constraintsVectorSizeTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsVectorSizeTextField.weightx = 1.0;
			constraintsVectorSizeTextField.insets = new java.awt.Insets(5, 10, 5, 10);
			getLabelPanel().add(getVectorSizeTextField(), constraintsVectorSizeTextField);
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
			ivjLivenessRadioButton.setSelected(false);
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
			constraintsSupertraceRadioButton.gridx = 0; constraintsSupertraceRadioButton.gridy = 1;
			constraintsSupertraceRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSupertraceRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSupertraceRadioButton.weightx = 1.0;
			constraintsSupertraceRadioButton.insets = new java.awt.Insets(10, 10, 0, 10);
			getModePanel().add(getSupertraceRadioButton(), constraintsSupertraceRadioButton);

			java.awt.GridBagConstraints constraintsHashCompactRadioButton = new java.awt.GridBagConstraints();
			constraintsHashCompactRadioButton.gridx = 0; constraintsHashCompactRadioButton.gridy = 2;
			constraintsHashCompactRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsHashCompactRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsHashCompactRadioButton.weightx = 1.0;
			constraintsHashCompactRadioButton.insets = new java.awt.Insets(10, 10, 10, 10);
			getModePanel().add(getHashCompactRadioButton(), constraintsHashCompactRadioButton);
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
			ivjNeverClaimCheckBox.setSelected(false);
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
			ivjOkbutton.setMaximumSize(new java.awt.Dimension(55, 22));
			ivjOkbutton.setPreferredSize(new java.awt.Dimension(55, 22));
			ivjOkbutton.setMinimumSize(new java.awt.Dimension(55, 22));
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
     * Get the Options model that this view is working on.
     *
     * @return Options The Options model that this view is configuring.
     */
public edu.ksu.cis.bandera.checker.Options getOptions() {
	return(spinOptions);
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
			ivjResourceBoundedCheckBox.setSelected(false);
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
			ivjSaveTrailCheckBox.setSelected(false);
			ivjSaveTrailCheckBox.setText("Save All Error Trails");
			ivjSaveTrailCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjSaveTrailCheckBox.setEnabled(true);
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
			ivjStateTextField.setText("18");
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
 * Return the VectorSizeLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getVectorSizeLabel() {
	if (ivjVectorSizeLabel == null) {
		try {
			ivjVectorSizeLabel = new javax.swing.JLabel();
			ivjVectorSizeLabel.setName("VectorSizeLabel");
			ivjVectorSizeLabel.setToolTipText("Allocates memory (in bytes) for state vector usage, e.g.: -DVECTORSZ=2048 (default is 1024)");
			ivjVectorSizeLabel.setText("Physical Memory (bytes)  for State Vector (default = 1024)");
			ivjVectorSizeLabel.setBackground(new java.awt.Color(204,204,255));
			ivjVectorSizeLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVectorSizeLabel;
}
/**
 * Return the VectorSizeTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getVectorSizeTextField() {
	if (ivjVectorSizeTextField == null) {
		try {
			ivjVectorSizeTextField = new javax.swing.JTextField();
			ivjVectorSizeTextField.setName("VectorSizeTextField");
			ivjVectorSizeTextField.setToolTipText("Allocates memory (in bytes) for state vector usage, e.g.: -DVECTORSZ=2048 (default is 1024)");
			ivjVectorSizeTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjVectorSizeTextField.setText("1024");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVectorSizeTextField;
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
	spinOptions.setSearchMode(SpinOptions.HASH_COMPACT_SEARCH_MODE);
	//return;
}
 /**
     * Initialize the view using the given options as the model.
     *
     * @param Options options The Options to use as the model for our view.
     */
public void init(edu.ksu.cis.bandera.checker.Options options) {

	if((options != null) && (options instanceof SpinOptions)) {
		spinOptions = (SpinOptions)options;

		// now get the values and init the GUI view
		getShortestCheckBox().setSelected(spinOptions.getFindShortestTrail());
		getResourceBoundedCheckBox().setSelected(spinOptions.getResourceBounded());
		getSaveTrailCheckBox().setSelected(spinOptions.getSaveAllTrails());
		getAssertionsCheckBox().setSelected(spinOptions.getAssertions());
		getLivenessRadioButton().setSelected(spinOptions.getAcceptanceCycles()); // opposite of safety
		getSaftyRadioButton().setSelected(spinOptions.getSafety()); // opposite of liveness/acceptance cycles
		getPartialOrderCheckBox().setSelected(spinOptions.getPartialOrderReduction());
		getCompressionCheckBox().setSelected(spinOptions.getCompression());
		getSaveTrailCheckBox().setSelected(spinOptions.getSaveAllTrails());
		// memory count
		// vector size
		getDepthTextField().setText(String.valueOf(spinOptions.getSearchDepth()));
		getPhysicalTextField().setText(String.valueOf(spinOptions.getMemoryLimit()));
		getErrorsTextField().setText(String.valueOf(spinOptions.getStopAtError()));
		getStateTextField().setText(String.valueOf(spinOptions.getSpaceEstimate()));
		
		switch(spinOptions.getSearchMode()) {
			case SpinOptions.EXHAUSTIVE_SEARCH_MODE:
				getExhaustiveRadioButton().setSelected(true);
				break;
			case SpinOptions.SUPER_TRACE_SEARCH_MODE:
				getSupertraceRadioButton().setSelected(true);
				break;
			case SpinOptions.HASH_COMPACT_SEARCH_MODE:
				getHashCompactRadioButton().setSelected(true);
				break;
			default:
				// do nothing since there is no other radio button to enable
		}

		// update the GUI
		update(getGraphics());
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
	getVectorSizeTextField().addFocusListener(ivjEventHandler);
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
		SpinOptionsView aSpinOptionsView;
		aSpinOptionsView = new SpinOptionsView();
		SpinOptions spinOptions = new SpinOptions();
		aSpinOptionsView.init(spinOptions);
		CompletionListener cl = new CompletionListener() {
			public void complete(Object o) {
				
				if((o != null) && (o instanceof SpinOptionsView)) {
					SpinOptionsView spinOptionsView = (SpinOptionsView)o;
					spinOptionsView.setVisible(false);
					try {
						SpinOptions spinOptions = (SpinOptions)spinOptionsView.getOptions();
						System.out.println("spinOptions = " + spinOptions.getCommandLineOptions());
						System.out.println("spin compiler options = " + spinOptions.getCompilerCommandLineOptions());
						System.out.println("spin pan options = " + spinOptions.getPanCommandLineOptions());
					}
					catch(Exception e) {
					}
				}
				
				System.out.println("Complete.");
				System.exit(0);
			}
		};
		aSpinOptionsView.registerCompletionListener(cl);
		aSpinOptionsView.setModal(true);
		aSpinOptionsView.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aSpinOptionsView.setVisible(true);
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
 * When the OK button is pressed, hide this view and let all the completion listeners
 * know that we are done configuring the Options.
 */
public void okbutton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
	setVisible(false);

	// inform all completion listeners that we are done!
	if((completionListenerSet != null) && (completionListenerSet.size() > 0)) {
		Iterator i = completionListenerSet.iterator();
		while(i.hasNext()) {
			CompletionListener cl = (CompletionListener)i.next();
			cl.complete(this);
		}
	}
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

	spinOptions.parseCompilerCommandLineOptions(compileOptions);
	    
	switch(spinOptions.getSearchMode()) {
		
	case SpinOptions.SUPER_TRACE_SEARCH_MODE:
	    getSupertraceRadioButton().setSelected(true);
	    break;
		
	case SpinOptions.HASH_COMPACT_SEARCH_MODE:
	    getHashCompactRadioButton().setSelected(true);
	    break;
	    
	case SpinOptions.EXHAUSTIVE_SEARCH_MODE:
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

	spinOptions.parsePanCommandLineOptions(panOptions);
	
	getDepthTextField().setText(Integer.toString(spinOptions.getSearchDepth()));
	getLivenessRadioButton().setSelected(spinOptions.getAcceptanceCycles());
	getAssertionsCheckBox().setSelected(spinOptions.getAssertions());
	getErrorsTextField().setText(Integer.toString(spinOptions.getStopAtError()));
	getSaveTrailCheckBox().setSelected(spinOptions.getSaveAllTrails());
	getShortestCheckBox().setSelected(spinOptions.getFindShortestTrail());

    }
/**
 * Comment
 */
public void partialOrderCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	spinOptions.setPartialOrderReduction(getPartialOrderCheckBox().isSelected());
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
     * Register a CompletionListener that is requesting notification
     * of the completion of the Options configuration.
     *
     * @param CompletionListener completionListener The object to signal when configuration
     *        is complete.
     */
public void registerCompletionListener(edu.ksu.cis.bandera.checker.CompletionListener completionListener) {
	if(completionListener == null) {
	    return;
	}
	if(completionListenerSet == null) {
	    completionListenerSet = new HashSet();
	}
	completionListenerSet.add(completionListener);
}
 /**
     * Remove the given listener that has been registered.
     *
     * @param CompletionListener completionListener The object that should be removed from
     *        the set of listeners.
     */
public void removeCompletionListener(edu.ksu.cis.bandera.checker.CompletionListener completionListener) {
	if(completionListenerSet != null) {
	    completionListenerSet.remove(completionListener);
	}
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
 /**
     * Set the Options model that this view should work on.
     *
     * @param Options options The Options model that this view will configure.
     */
public void setOptions(edu.ksu.cis.bandera.checker.Options options) {
	init(options);
}
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
	spinOptions.setSearchMode(SpinOptions.SUPER_TRACE_SEARCH_MODE);
	//return;
}
/**
 * When the focus is lost for the vector size text field we should
 * update the model with the new value.
 */
public void vectorSizeTextField_FocusLost() {
	try {
		int vectorSize = Integer.parseInt(getVectorSizeTextField().getText());
		spinOptions.setVectorSize(vectorSize);
	}
	catch(Exception e) {
		JOptionPane.showMessageDialog(this, "An error occured while parsing the value specified in the Vector Size text field." +
			"It must be an integer value.  Exception: " + e.toString(),
			"Error with Vector Size",
			JOptionPane.ERROR_MESSAGE);
	}
}
}
