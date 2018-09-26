package edu.ksu.cis.bandera.checker.jpf;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsView;
import edu.ksu.cis.bandera.checker.CompletionListener;

import edu.ksu.cis.bandera.checker.jpf.JPFOptions;

public class JPFOptionsView extends JDialog implements edu.ksu.cis.bandera.checker.OptionsView {
	private JPanel ivjJPFOptionDialogContentPane = null;
	private JPanel ivjRuntimeAnalysisPanel = null;
	private JButton ivjOkButton = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JCheckBox ivjHashCompactCheckBox = null;
	private JCheckBox ivjSearchDepthCheckBox = null;
	private JTextField ivjSearchDepthTextField = null;
	private JCheckBox ivjAtomicLinesCheckBox = null;
	private JCheckBox ivjGarbageCollectionCheckBox = null;
	private JCheckBox ivjHeapSymmetryCheckBox = null;
	private JCheckBox ivjPOCheckBox = null;
	private JCheckBox ivjRaceCheckBox = null;
	private JPanel ivjFillerPanel4 = null;
	private JPanel ivjFillerPanel5 = null;
	private JCheckBox ivjPostVerifyCheckBox = null;
	private JTextField ivjRaceLevelTextField = null;
	private JCheckBox ivjLockOrderCheckBox = null;
	private JCheckBox ivjDDependCheckBox = null;
	private JPanel ivjDebugPanel = null;
	private JCheckBox ivjDependCheckBox = null;
	private JCheckBox ivjDLockOrderCheckBox = null;
	private JCheckBox ivjDRaceCheckBox = null;
	private JCheckBox ivjAssertionCheckBox = null;
	private JPanel ivjAdvancedOptionsPanel = null;
	private JTextField ivjAdvancedOptionsTextField = null;
	private JRadioButton ivjAStarRadioButton = null;
	private JRadioButton ivjBeamRadioButton = null;
	private JRadioButton ivjBestFirstRadioButton = null;
	private JRadioButton ivjBFSRadioButton = null;
	private JRadioButton ivjBranchGlobalRadioButton = null;
	private JRadioButton ivjBranchPathRadioButton = null;
	private JRadioButton ivjChooseFreeRadioButton = null;
	private JPanel ivjHeuristicPanel = null;
	private JRadioButton ivjInterleavingRadioButton = null;
	private JRadioButton ivjMostBlockedRadioButton = null;
	private JCheckBox ivjQueueLimitCheckBox = null;
	private JTextField ivjQueueLimitTextField = null;
	private JPanel ivjStrategyPanel = null;
	private JRadioButton ivjUserRadioButton = null;
	private javax.swing.ButtonGroup heuristicButtonGroup;
	private javax.swing.ButtonGroup strategyButtonGroup;
	private JTabbedPane ivjJPFOptionsTabbedPane = null;
	private JCheckBox ivjDeadlockCheckBox = null;
	private JPanel ivjVerificationPanel = null;
	private JCheckBox ivjCommandLineOptionCheckBox = null;
	private JPanel ivjVerificationOptionsPanel = null;
	private JRadioButton ivjDFSRadioButton = null;
	private JPFOptions jpfOptions;
	private JPanel ivjQueueLimitPanel = null;
	private JPanel ivjQueueLimitSearchDepthPanel = null;
	private JPanel ivjSearchDepthPanel = null;
	private java.util.Set completionListenerSet;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.FocusListener, java.beans.PropertyChangeListener, javax.swing.event.ChangeListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == JPFOptionsView.this.getLockOrderCheckBox()) 
				connEtoC7();
			if (e.getSource() == JPFOptionsView.this.getDependCheckBox()) 
				connEtoC10();
			if (e.getSource() == JPFOptionsView.this.getPostVerifyCheckBox()) 
				connEtoC8();
			if (e.getSource() == JPFOptionsView.this.getGarbageCollectionCheckBox()) 
				connEtoC4();
			if (e.getSource() == JPFOptionsView.this.getHeapSymmetryCheckBox()) 
				connEtoC5();
			if (e.getSource() == JPFOptionsView.this.getSearchDepthCheckBox()) 
				connEtoC6(e);
			if (e.getSource() == JPFOptionsView.this.getQueueLimitCheckBox()) 
				connEtoC9(e);
			if (e.getSource() == JPFOptionsView.this.getDeadlockCheckBox()) 
				connEtoC15(e);
			if (e.getSource() == JPFOptionsView.this.getAssertionCheckBox()) 
				connEtoC16(e);
			if (e.getSource() == JPFOptionsView.this.getAtomicLinesCheckBox()) 
				connEtoC17(e);
			if (e.getSource() == JPFOptionsView.this.getPOCheckBox()) 
				connEtoC18(e);
			if (e.getSource() == JPFOptionsView.this.getHashCompactCheckBox()) 
				connEtoC19(e);
			if (e.getSource() == JPFOptionsView.this.getInterleavingRadioButton()) 
				connEtoC23(e);
			if (e.getSource() == JPFOptionsView.this.getChooseFreeRadioButton()) 
				connEtoC24(e);
			if (e.getSource() == JPFOptionsView.this.getMostBlockedRadioButton()) 
				connEtoC25(e);
			if (e.getSource() == JPFOptionsView.this.getBranchPathRadioButton()) 
				connEtoC26(e);
			if (e.getSource() == JPFOptionsView.this.getBranchGlobalRadioButton()) 
				connEtoC27(e);
			if (e.getSource() == JPFOptionsView.this.getUserRadioButton()) 
				connEtoC28(e);
			if (e.getSource() == JPFOptionsView.this.getBeamRadioButton()) 
				connEtoC13(e);
			if (e.getSource() == JPFOptionsView.this.getAStarRadioButton()) 
				connEtoC14(e);
			if (e.getSource() == JPFOptionsView.this.getBestFirstRadioButton()) 
				connEtoC20(e);
			if (e.getSource() == JPFOptionsView.this.getBFSRadioButton()) 
				connEtoC21(e);
			if (e.getSource() == JPFOptionsView.this.getDFSRadioButton()) 
				connEtoC22(e);
			if (e.getSource() == JPFOptionsView.this.getCommandLineOptionCheckBox()) 
				connEtoC2(e);
			if (e.getSource() == JPFOptionsView.this.getOkButton()) 
				connEtoC29(e);
		};
		public void focusGained(java.awt.event.FocusEvent e) {};
		public void focusLost(java.awt.event.FocusEvent e) {
			if (e.getSource() == JPFOptionsView.this.getQueueLimitTextField()) 
				connEtoC11(e);
			if (e.getSource() == JPFOptionsView.this.getSearchDepthTextField()) 
				connEtoC12(e);
		};
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() == JPFOptionsView.this.getJPFOptionsTabbedPane() && (evt.getPropertyName().equals("tabPlacement"))) 
				connEtoC3(evt);
		};
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			if (e.getSource() == JPFOptionsView.this.getRaceCheckBox()) 
				connEtoC1(e);
		};
	};
/**
 * JPFOption constructor comment.
 */
public JPFOptionsView() {
	super();
	initialize();
}
/**
 * When the assertion check box changes state we need to update the model
 * to match.
 */
public void assertionCheckBox_ActionPerformed() {
	if(getAssertionCheckBox().isSelected()) {
		jpfOptions.enableAssertions();
	}
	else {
		jpfOptions.disableAssertions();
	}
}
/**
 * When the A* radio button is selected, update the model, disable search depth, enable
 * queue limit, and enable heuristics.
 */
public void aStarRadioButton_ActionPerformed() {
    if (getAStarRadioButton().isSelected()) {
        try {
            jpfOptions.setStrategy(JPFOptions.ASTAR_STRATEGY);
            jpfOptions.disableSearchDepth();
        }
        catch (Exception e) {
        }
        getInterleavingRadioButton().setEnabled(true);
        getChooseFreeRadioButton().setEnabled(true);
        getMostBlockedRadioButton().setEnabled(true);
        getBranchPathRadioButton().setEnabled(true);
        getBranchGlobalRadioButton().setEnabled(true);
        getUserRadioButton().setEnabled(true);
        getSearchDepthCheckBox().setEnabled(false);
        getSearchDepthTextField().setEnabled(false);
        getQueueLimitCheckBox().setEnabled(true);
        if (jpfOptions.isQueueLimitEnabled()) {
            getQueueLimitTextField().setEnabled(true);
        }
    }
}
/**
 * When the atomic lines check box changes state we need to update the
 * model to match.
 */
public void atomicLinesCheckBox_ActionPerformed() {
	if(getAtomicLinesCheckBox().isSelected()) {
		jpfOptions.enableAtomicLines();
	}
	else {
		jpfOptions.disableAtomicLines();
	}
}
/**
 * When the Beam radio button is selected, update the model, disable search depth,
 * enable queue limit, and enable heuristics.
 */
public void beamRadioButton_ActionPerformed() {
    if (getBeamRadioButton().isSelected()) {
        try {
            jpfOptions.setStrategy(JPFOptions.BEAM_STRATEGY);
            jpfOptions.disableSearchDepth();
        }
        catch (Exception e) {
        }
        
        getInterleavingRadioButton().setEnabled(true);
        getChooseFreeRadioButton().setEnabled(true);
        getMostBlockedRadioButton().setEnabled(true);
        getBranchPathRadioButton().setEnabled(true);
        getBranchGlobalRadioButton().setEnabled(true);
        getUserRadioButton().setEnabled(true);
        
        getSearchDepthCheckBox().setEnabled(false);
        getSearchDepthTextField().setEnabled(false);
        
        getQueueLimitCheckBox().setEnabled(true);
        if(jpfOptions.isQueueLimitEnabled()) {
	        getQueueLimitTextField().setEnabled(true);
        }
    }
}
/**
 * When the best first radio button is selected we should update the
 * model, enable the heuristics, enable queue limit, and disable search depth.
 */
public void bestFirstRadioButton_ActionPerformed() {
    if (getBestFirstRadioButton().isSelected()) {
        try {
            jpfOptions.setStrategy(JPFOptions.BEST_FIRST_STRATEGY);
            jpfOptions.disableSearchDepth();
        }
        catch (Exception e) {
        }
        
        getInterleavingRadioButton().setEnabled(true);
        getChooseFreeRadioButton().setEnabled(true);
        getMostBlockedRadioButton().setEnabled(true);
        getBranchPathRadioButton().setEnabled(true);
        getBranchGlobalRadioButton().setEnabled(true);
        getUserRadioButton().setEnabled(true);
        
        getSearchDepthCheckBox().setEnabled(false);
        getSearchDepthTextField().setEnabled(false);
        
        getQueueLimitCheckBox().setEnabled(true);
        if(jpfOptions.isQueueLimitEnabled()) {
	        getQueueLimitTextField().setEnabled(true);
        }

    }
}
/**
 * When the BFS radio button is selected we should make sure the model is
 * updated to use BFS as the strategy, disable the heuristics, enable queue limit,
 * and disable search depth.
 */
public void bFSRadioButton_ActionPerformed() {
    if (getBFSRadioButton().isSelected()) {
        try {
            jpfOptions.setStrategy(JPFOptions.BFS_STRATEGY);
            jpfOptions.disableSearchDepth();
        }
        catch (Exception e) {
        }
        getInterleavingRadioButton().setEnabled(false);
        getChooseFreeRadioButton().setEnabled(false);
        getMostBlockedRadioButton().setEnabled(false);
        getBranchPathRadioButton().setEnabled(false);
        getBranchGlobalRadioButton().setEnabled(false);
        getUserRadioButton().setEnabled(false);
        getSearchDepthCheckBox().setEnabled(false);
        getSearchDepthTextField().setEnabled(false);
        getQueueLimitCheckBox().setEnabled(true);
        if(jpfOptions.isQueueLimitEnabled()) {
	        getQueueLimitTextField().setEnabled(true);
        }
    }
}
/**
 * Comment
 */
public void branchGlobalRadioButton_ActionPerformed() {
	if(getBranchGlobalRadioButton().isSelected()) {
		try {
			jpfOptions.setHeuristic(JPFOptions.BRANCH_GLOBAL_HEURISTIC);
		}
		catch(Exception e) {
		}
	}
}
/**
 * Comment
 */
public void branchPathRadioButton_ActionPerformed() {
	if(getBranchPathRadioButton().isSelected()) {
		try {
			jpfOptions.setHeuristic(JPFOptions.BRANCH_PATH_HEURISTIC);
		}
		catch(Exception e) {
		}
	}
}
/**
 * When the choose free radio button is selected, update the model.
 */
public void chooseFreeRadioButton_ActionPerformed() {
	if(getChooseFreeRadioButton().isSelected()) {
		try {
			jpfOptions.setHeuristic(JPFOptions.CHOOSE_FREE_HEURISTIC);
		}
		catch(Exception e) {
		}
	}
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
 * If the Command line options check box is turned on (or checked), then
 * we need to enable the command line options TextField.  If the command
 * line options check box is turned off (or unchecked), then we need to
 * disable the command line options TextField.  Also, fill in the command
 * line options window with the correct command line options based upon
 * the current state of the system when it goes from unchecked to checked
 * in order to give the user the current command line options as they
 * have configured in the GUI.
 */
public void commandLineOptionCheckBox_ActionPerformed() {
    boolean commandLineOptionsSelected =
        getCommandLineOptionCheckBox().isSelected();

    // if the command line check box is selected, enable the text box,  otherwise, disable it	
    getAdvancedOptionsTextField().setEnabled(commandLineOptionsSelected);

    // update the command line options text field
    if (commandLineOptionsSelected) {

	    // set the current command line options that are configured in the current model
        try {
            getAdvancedOptionsTextField().setText(jpfOptions.getCommandLineOptions());
        }
        catch (Exception e) {
            getAdvancedOptionsTextField().setText("");
        }

        // enable only those components that should be enabled, leave the rest as disabled.
        int strategy = jpfOptions.getStrategy();
        if (strategy == JPFOptions.DFS_STRATEGY) {
            getSearchDepthCheckBox().setEnabled(true);
            getSearchDepthTextField().setEnabled(true);
        }
        else {
            getQueueLimitCheckBox().setEnabled(true);
            getQueueLimitTextField().setEnabled(true);
            if ((strategy == JPFOptions.BEST_FIRST_STRATEGY)
                || (strategy == JPFOptions.BEAM_STRATEGY)
                || (strategy == JPFOptions.ASTAR_STRATEGY)) {
                Enumeration heuristicButtonEnumeration = heuristicButtonGroup.getElements();
                while (heuristicButtonEnumeration.hasMoreElements()) {
                    AbstractButton currentButton =
                        (AbstractButton) heuristicButtonEnumeration.nextElement();
                    currentButton.setEnabled(true);
                }
            }
        }

    }
    else {
        getAdvancedOptionsTextField().setText("");
        getSearchDepthCheckBox().setEnabled(false);
        getSearchDepthTextField().setEnabled(false);
        getQueueLimitCheckBox().setEnabled(false);
        getQueueLimitTextField().setEnabled(false);
        Enumeration heuristicButtonEnumeration = heuristicButtonGroup.getElements();
        while (heuristicButtonEnumeration.hasMoreElements()) {
            AbstractButton currentButton =
                (AbstractButton) heuristicButtonEnumeration.nextElement();
            currentButton.setEnabled(false);
        }
    }

    // disable the rest of the JPF Options window when the command line
    //  options check box is selected and enabled otherwise.

    // walk thru the strategy buttons
    Enumeration strategyButtonEnumeration = strategyButtonGroup.getElements();
    while (strategyButtonEnumeration.hasMoreElements()) {
        AbstractButton currentButton =
            (AbstractButton) strategyButtonEnumeration.nextElement();
        currentButton.setEnabled(!commandLineOptionsSelected);
    }

    // enable/disable each check box
    getRaceCheckBox().setEnabled(!commandLineOptionsSelected);
    getPostVerifyCheckBox().setEnabled(!commandLineOptionsSelected);
    getPOCheckBox().setEnabled(!commandLineOptionsSelected);
    getLockOrderCheckBox().setEnabled(!commandLineOptionsSelected);
    getHeapSymmetryCheckBox().setEnabled(!commandLineOptionsSelected);
    getHashCompactCheckBox().setEnabled(!commandLineOptionsSelected);
    getGarbageCollectionCheckBox().setEnabled(!commandLineOptionsSelected);
    getDRaceCheckBox().setEnabled(!commandLineOptionsSelected);
    getDLockOrderCheckBox().setEnabled(!commandLineOptionsSelected);
    getDependCheckBox().setEnabled(!commandLineOptionsSelected);
    getDeadlockCheckBox().setEnabled(!commandLineOptionsSelected);
    getDDependCheckBox().setEnabled(!commandLineOptionsSelected);
    getAtomicLinesCheckBox().setEnabled(!commandLineOptionsSelected);
    getAssertionCheckBox().setEnabled(!commandLineOptionsSelected);
}
/**
 * connEtoC1:  (RaceCheckBox.change.stateChanged(javax.swing.event.ChangeEvent) --> JPFOption.raceCheckBox_StateChanged(Ljavax.swing.event.ChangeEvent;)V)
 * @param arg1 javax.swing.event.ChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(javax.swing.event.ChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.raceCheckBox_StateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (DependCheckBox.action. --> JPFOption.dependCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC10() {
	try {
		// user code begin {1}
		// user code end
		this.dependCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (QueueLimitTextField.focus.focusLost(java.awt.event.FocusEvent) --> JPFOptionsView.queueLimitTextField_FocusLost()V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC11(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.queueLimitTextField_FocusLost();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (SearchDepthTextField.focus.focusLost(java.awt.event.FocusEvent) --> JPFOptionsView.searchDepthTextField_FocusLost()V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.searchDepthTextField_FocusLost();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (DFSRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.dFSRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.beamRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (BFSRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.bFSRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.aStarRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC15:  (DeadlockCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.deadlockCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC15(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.deadlockCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC16:  (AssertionCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.assertionCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC16(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.assertionCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC17:  (AtomicLinesCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.atomicLinesCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC17(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.atomicLinesCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC18:  (POCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.pOCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC18(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.pOCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC19:  (HashCompactCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.hashCompactCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC19(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.hashCompactCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (CommandLineOptionCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.commandLineOptionCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.commandLineOptionCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC20:  (BestFirstRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.bestFirstRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC20(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.bestFirstRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC21:  (BFSRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.bFSRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC21(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.bFSRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC22:  (DFSRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.dFSRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC22(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dFSRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC23:  (InterleavingRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.interleavingRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC23(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.interleavingRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC24:  (ChooseFreeRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.chooseFreeRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC24(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.chooseFreeRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC25:  (MostBlockedRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.mostBlockedRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC25(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.mostBlockedRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC26:  (BranchPathRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.branchPathRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC26(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.branchPathRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC27:  (BranchGlobalRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.branchGlobalRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC27(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.branchGlobalRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC28:  (UserRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.userRadioButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC28(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.userRadioButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC29:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.okButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC29(java.awt.event.ActionEvent arg1) {
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
 * connEtoC3:  (JPFOptionsTabbedPane.tabPlacement --> JPFOptionsView.jPFOptionsTabbedPane_StateChanged(Ljavax.swing.event.ChangeEvent;)V)
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.beans.PropertyChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jPFOptionsTabbedPane_StateChanged(null);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (GarbageCollectionCheckBox.action. --> JPFOption.garbageCollectionCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		// user code end
		this.garbageCollectionCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (HeapSymmetryCheckBox.action. --> JPFOption.heapSymmetryCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5() {
	try {
		// user code begin {1}
		// user code end
		this.heapSymmetryCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (SearchDepthCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.searchDepthCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.searchDepthCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (LockOrderCheckBox.action. --> JPFOption.lockOrderCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7() {
	try {
		// user code begin {1}
		// user code end
		this.lockOrderCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (PostVerifyCheckBox.action. --> JPFOption.postVerifyCheckBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8() {
	try {
		// user code begin {1}
		// user code end
		this.postVerifyCheckBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (QueueLimitCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOptionsView.queueLimitCheckBox_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.queueLimitCheckBox_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> JPFOption.dispose()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dispose();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * When the deadlock check box changes state, we should update the model
 * to match.
 */
public void deadlockCheckBox_ActionPerformed() {
	if(getDeadlockCheckBox().isSelected()) {
		jpfOptions.enableDeadlocks();
	}
	else {
		jpfOptions.disableDeadlocks();
	}
}
/**
 * Comment
 */
public void dependCheckBox_ActionEvents() {
	if (getDependCheckBox().isSelected()) {
		getDDependCheckBox().setEnabled(true);
	} else {
		getDDependCheckBox().setEnabled(false);
		getDDependCheckBox().setSelected(false);
	}
}
/**
 * When the DFS button is selected we should disable the heuristic buttons and make
 * sure the search depth check box is enabled so the user can set the search depth if
 * they choose.  The queue limit is not valid for use with DFS so we will disable the check box,
 * text field, and the flag in the model.
 * We should also update the model so the strategy is DFS.
 */
public void dFSRadioButton_ActionPerformed() {
    if (getDFSRadioButton().isSelected()) {
        try {
            jpfOptions.setStrategy(JPFOptions.DFS_STRATEGY);
            jpfOptions.disableQueueLimit();
        }
        catch (Exception e) {
            // this should never happen since we are using the JPFOptions constant
        }
        
        getInterleavingRadioButton().setEnabled(false);
        getChooseFreeRadioButton().setEnabled(false);
        getMostBlockedRadioButton().setEnabled(false);
        getBranchPathRadioButton().setEnabled(false);
        getBranchGlobalRadioButton().setEnabled(false);
        getUserRadioButton().setEnabled(false);
        
        getSearchDepthCheckBox().setEnabled(true);
        if(jpfOptions.isSearchDepthEnabled()) {
	        getSearchDepthTextField().setEnabled(true);
        }
        
        getQueueLimitCheckBox().setEnabled(false);
        getQueueLimitTextField().setEnabled(false);
    }
}
/**
 * If the garbage collection check box is turned on (or checked), then
 * the heap symmetry check box must also be turned on (or checked).
 */
public void garbageCollectionCheckBox_ActionEvents() {
	if (getGarbageCollectionCheckBox().isSelected()) {
		getHeapSymmetryCheckBox().setSelected(true);
		jpfOptions.enableGarbageCollection();
		jpfOptions.enableHashCompaction();
	}
	else {
		jpfOptions.disableGarbageCollection();
	}
}
/**
 * Return the AdvancedOptionsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getAdvancedOptionsPanel() {
	if (ivjAdvancedOptionsPanel == null) {
		try {
			ivjAdvancedOptionsPanel = new javax.swing.JPanel();
			ivjAdvancedOptionsPanel.setName("AdvancedOptionsPanel");
			ivjAdvancedOptionsPanel.setLayout(new java.awt.GridBagLayout());
			ivjAdvancedOptionsPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsAdvancedOptionsTextField = new java.awt.GridBagConstraints();
			constraintsAdvancedOptionsTextField.gridx = 1; constraintsAdvancedOptionsTextField.gridy = 0;
			constraintsAdvancedOptionsTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAdvancedOptionsTextField.weightx = 1.0;
			constraintsAdvancedOptionsTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getAdvancedOptionsPanel().add(getAdvancedOptionsTextField(), constraintsAdvancedOptionsTextField);

			java.awt.GridBagConstraints constraintsCommandLineOptionCheckBox = new java.awt.GridBagConstraints();
			constraintsCommandLineOptionCheckBox.gridx = 0; constraintsCommandLineOptionCheckBox.gridy = 0;
			constraintsCommandLineOptionCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getAdvancedOptionsPanel().add(getCommandLineOptionCheckBox(), constraintsCommandLineOptionCheckBox);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAdvancedOptionsPanel;
}
/**
 * Return the AdvancedOptionsTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getAdvancedOptionsTextField() {
	if (ivjAdvancedOptionsTextField == null) {
		try {
			ivjAdvancedOptionsTextField = new javax.swing.JTextField();
			ivjAdvancedOptionsTextField.setName("AdvancedOptionsTextField");
			ivjAdvancedOptionsTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjAdvancedOptionsTextField.setBackground(java.awt.Color.lightGray);
			ivjAdvancedOptionsTextField.setForeground(java.awt.Color.black);
			ivjAdvancedOptionsTextField.setEditable(true);
			ivjAdvancedOptionsTextField.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAdvancedOptionsTextField;
}
/**
 * Return the AssetionCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getAssertionCheckBox() {
	if (ivjAssertionCheckBox == null) {
		try {
			ivjAssertionCheckBox = new javax.swing.JCheckBox();
			ivjAssertionCheckBox.setName("AssertionCheckBox");
			ivjAssertionCheckBox.setSelected(true);
			ivjAssertionCheckBox.setText("Assertions");
			ivjAssertionCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionCheckBox;
}
/**
 * Return the AStarRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getAStarRadioButton() {
	if (ivjAStarRadioButton == null) {
		try {
			ivjAStarRadioButton = new javax.swing.JRadioButton();
			ivjAStarRadioButton.setName("AStarRadioButton");
			ivjAStarRadioButton.setText("A*");
			ivjAStarRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAStarRadioButton;
}
/**
 * Return the AtomicLinesCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getAtomicLinesCheckBox() {
	if (ivjAtomicLinesCheckBox == null) {
		try {
			ivjAtomicLinesCheckBox = new javax.swing.JCheckBox();
			ivjAtomicLinesCheckBox.setName("AtomicLinesCheckBox");
			ivjAtomicLinesCheckBox.setSelected(true);
			ivjAtomicLinesCheckBox.setText("Atomic Lines");
			ivjAtomicLinesCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAtomicLinesCheckBox;
}
/**
 * Return the BeamRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getBeamRadioButton() {
	if (ivjBeamRadioButton == null) {
		try {
			ivjBeamRadioButton = new javax.swing.JRadioButton();
			ivjBeamRadioButton.setName("BeamRadioButton");
			ivjBeamRadioButton.setText("Beam");
			ivjBeamRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBeamRadioButton;
}
/**
 * Return the BestFirstRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getBestFirstRadioButton() {
	if (ivjBestFirstRadioButton == null) {
		try {
			ivjBestFirstRadioButton = new javax.swing.JRadioButton();
			ivjBestFirstRadioButton.setName("BestFirstRadioButton");
			ivjBestFirstRadioButton.setText("Best-first");
			ivjBestFirstRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBestFirstRadioButton;
}
/**
 * Return the BFSRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getBFSRadioButton() {
	if (ivjBFSRadioButton == null) {
		try {
			ivjBFSRadioButton = new javax.swing.JRadioButton();
			ivjBFSRadioButton.setName("BFSRadioButton");
			ivjBFSRadioButton.setSelected(false);
			ivjBFSRadioButton.setToolTipText("Use a Breadth first search algorithm");
			ivjBFSRadioButton.setText("BFS");
			ivjBFSRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBFSRadioButton;
}
/**
 * Return the BranchGlobalRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getBranchGlobalRadioButton() {
	if (ivjBranchGlobalRadioButton == null) {
		try {
			ivjBranchGlobalRadioButton = new javax.swing.JRadioButton();
			ivjBranchGlobalRadioButton.setName("BranchGlobalRadioButton");
			ivjBranchGlobalRadioButton.setText("Branch Global");
			ivjBranchGlobalRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjBranchGlobalRadioButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBranchGlobalRadioButton;
}
/**
 * Return the BranchPathRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getBranchPathRadioButton() {
	if (ivjBranchPathRadioButton == null) {
		try {
			ivjBranchPathRadioButton = new javax.swing.JRadioButton();
			ivjBranchPathRadioButton.setName("BranchPathRadioButton");
			ivjBranchPathRadioButton.setText("Branch Path");
			ivjBranchPathRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjBranchPathRadioButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBranchPathRadioButton;
}
/**
 * Return the ChooseFreeRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getChooseFreeRadioButton() {
	if (ivjChooseFreeRadioButton == null) {
		try {
			ivjChooseFreeRadioButton = new javax.swing.JRadioButton();
			ivjChooseFreeRadioButton.setName("ChooseFreeRadioButton");
			ivjChooseFreeRadioButton.setText("Choose-free");
			ivjChooseFreeRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjChooseFreeRadioButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjChooseFreeRadioButton;
}
/**
 * Return the CommandLineOptionCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getCommandLineOptionCheckBox() {
	if (ivjCommandLineOptionCheckBox == null) {
		try {
			ivjCommandLineOptionCheckBox = new javax.swing.JCheckBox();
			ivjCommandLineOptionCheckBox.setName("CommandLineOptionCheckBox");
			ivjCommandLineOptionCheckBox.setText("Command Line Options");
			ivjCommandLineOptionCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCommandLineOptionCheckBox;
}
/**
 * Look at the current state of the JPF Option window and convert those settings
 * into command line options that will be passed to JPF.
 *
 * @return String The command line options for JPF based upon the current state
 *         of the window.
 */
private String getCommandLineOptionsText() {
    StringBuffer result = new StringBuffer();

    // if verification is turned on, parse those options
    if (isVerification()) {

        if (getDFSRadioButton().isSelected()) {
            // add nothing to the result since the default heuristic is DFS so we
            //  need not specify it on the command line. -tcw

            // search depth is only relevant when DFS is chosen. -tcw
            if (getSearchDepthCheckBox().isSelected()) {
                result.append("-search-depth ");
                result.append(getSearchDepthTextField().getText().trim());
                result.append(" ");
            }

        }
        else {
            // if the BFS radio button is selected, pass the "-heuristic BFS" flag back
            if (getBFSRadioButton().isSelected()) {
                result.append("-heuristic BFS ");
            }
            else {

                /* It will only be one of the following since they are in the heuristicButtonGroup */
                if (getInterleavingRadioButton().isSelected()) {
                    result.append("-heuristic interleaving ");
                }
                if (getChooseFreeRadioButton().isSelected()) {
                    result.append("-heuristic choose-free ");
                }
                if (getMostBlockedRadioButton().isSelected()) {
                    result.append("-heuristic most-blocked ");
                }
                if (getBranchPathRadioButton().isSelected()) {
                    result.append("-heuristic branch-path ");
                }
                if (getBranchGlobalRadioButton().isSelected()) {
                    result.append("-heuristic branch-global ");
                }
                if (getUserRadioButton().isSelected()) {
                    result.append("-heuristic user ");
                }

                if (getAStarRadioButton().isSelected()) {
                    result.append("-astar ");
                }
                if (getBeamRadioButton().isSelected()) {
                    result.append("-beam ");
                }
            }

            if (getQueueLimitCheckBox().isSelected()) {
                result.append("-queue-limit ");
                result.append(getQueueLimitTextField().getText().trim());
                result.append(" ");
            }

        }

        if (getPOCheckBox().isSelected()) {
            result.append("-po-reduction ");
        }
        else {
            result.append("-no-po-reduction ");
        }

        if (getGarbageCollectionCheckBox().isSelected()) {
            result.append("-garbage-collection ");
        }
        else {
            result.append("-no-garbage-collection ");
        }

        if (getHashCompactCheckBox().isSelected()) {
            result.append("-hashcompact ");
        }
        else {
            result.append("-no-hashcompact ");
        }

        if (getHeapSymmetryCheckBox().isSelected()) {
            result.append("-heap-symmetry ");
        }
        else {
            result.append("-no-heap-symmetry ");
        }

        if (getAtomicLinesCheckBox().isSelected()) {
            result.append("-atomic-lines ");
        }
        else {
            result.append("-no-atomic-lines ");
        }

        if (getAssertionCheckBox().isSelected()) {
            result.append("-assertions ");
        }
        else {
            result.append("-no-assertions ");
        }

        if (getDeadlockCheckBox().isSelected()) {
            result.append("-deadlocks ");
        }
        else {
            result.append("-no-deadlocks ");
        }
    }
    else {
        result.append("-no-verify ");

        if (getRaceCheckBox().isSelected()) {
            result.append("-race ");
            result.append("-race-level ");
            result.append(getRaceLevelTextField().getText().trim());
            result.append(" ");
        }

        if (getLockOrderCheckBox().isSelected()) {
            result.append("-lock ");
        }

        if (getPostVerifyCheckBox().isSelected()) {
            result.append("-post-verify ");
        }

        if (getDependCheckBox().isSelected()) {
            result.append("-depend ");
        }

        if (getDRaceCheckBox().isSelected()) {
            result.append("-debugs race ");
        }

        if (getLockOrderCheckBox().isSelected()) {
            result.append("-debugs lock ");
        }

        if (getDDependCheckBox().isSelected()) {
            result.append("-debugs depend ");
        }
    }

    return (result.toString().trim());
}
/**
 * Return the DDependCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDDependCheckBox() {
	if (ivjDDependCheckBox == null) {
		try {
			ivjDDependCheckBox = new javax.swing.JCheckBox();
			ivjDDependCheckBox.setName("DDependCheckBox");
			ivjDDependCheckBox.setText("Depend");
			ivjDDependCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjDDependCheckBox.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDDependCheckBox;
}
/**
 * Return the DeadlockCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDeadlockCheckBox() {
	if (ivjDeadlockCheckBox == null) {
		try {
			ivjDeadlockCheckBox = new javax.swing.JCheckBox();
			ivjDeadlockCheckBox.setName("DeadlockCheckBox");
			ivjDeadlockCheckBox.setSelected(true);
			ivjDeadlockCheckBox.setText("Deadlock");
			ivjDeadlockCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDeadlockCheckBox;
}
/**
 * Return the FillerPanel6 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getDebugPanel() {
	if (ivjDebugPanel == null) {
		try {
			ivjDebugPanel = new javax.swing.JPanel();
			ivjDebugPanel.setName("DebugPanel");
			ivjDebugPanel.setBorder(BorderFactory.createTitledBorder("Debug"));
			ivjDebugPanel.setLayout(new java.awt.GridBagLayout());
			ivjDebugPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsDRaceCheckBox = new java.awt.GridBagConstraints();
			constraintsDRaceCheckBox.gridx = 0; constraintsDRaceCheckBox.gridy = 0;
			constraintsDRaceCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDRaceCheckBox.weightx = 1.0;
			constraintsDRaceCheckBox.insets = new java.awt.Insets(10, 10, 10, 0);
			getDebugPanel().add(getDRaceCheckBox(), constraintsDRaceCheckBox);

			java.awt.GridBagConstraints constraintsDLockOrderCheckBox = new java.awt.GridBagConstraints();
			constraintsDLockOrderCheckBox.gridx = 1; constraintsDLockOrderCheckBox.gridy = 0;
			constraintsDLockOrderCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDLockOrderCheckBox.weightx = 1.0;
			constraintsDLockOrderCheckBox.insets = new java.awt.Insets(10, 10, 10, 0);
			getDebugPanel().add(getDLockOrderCheckBox(), constraintsDLockOrderCheckBox);

			java.awt.GridBagConstraints constraintsDDependCheckBox = new java.awt.GridBagConstraints();
			constraintsDDependCheckBox.gridx = 2; constraintsDDependCheckBox.gridy = 0;
			constraintsDDependCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDDependCheckBox.weightx = 1.0;
			constraintsDDependCheckBox.insets = new java.awt.Insets(10, 10, 10, 10);
			getDebugPanel().add(getDDependCheckBox(), constraintsDDependCheckBox);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDebugPanel;
}
/**
 * Return the JCheckBox3 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDependCheckBox() {
	if (ivjDependCheckBox == null) {
		try {
			ivjDependCheckBox = new javax.swing.JCheckBox();
			ivjDependCheckBox.setName("DependCheckBox");
			ivjDependCheckBox.setText("Depend");
			ivjDependCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjDependCheckBox.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependCheckBox;
}
/**
 * Return the DFSRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getDFSRadioButton() {
	if (ivjDFSRadioButton == null) {
		try {
			ivjDFSRadioButton = new javax.swing.JRadioButton();
			ivjDFSRadioButton.setName("DFSRadioButton");
			ivjDFSRadioButton.setSelected(true);
			ivjDFSRadioButton.setToolTipText("Use a Depth first search algorithm");
			ivjDFSRadioButton.setText("DFS");
			ivjDFSRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDFSRadioButton;
}
/**
 * Return the DLockOrderCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDLockOrderCheckBox() {
	if (ivjDLockOrderCheckBox == null) {
		try {
			ivjDLockOrderCheckBox = new javax.swing.JCheckBox();
			ivjDLockOrderCheckBox.setName("DLockOrderCheckBox");
			ivjDLockOrderCheckBox.setText("Lock Ordering");
			ivjDLockOrderCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjDLockOrderCheckBox.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDLockOrderCheckBox;
}
/**
 * Return the DRaceCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDRaceCheckBox() {
	if (ivjDRaceCheckBox == null) {
		try {
			ivjDRaceCheckBox = new javax.swing.JCheckBox();
			ivjDRaceCheckBox.setName("DRaceCheckBox");
			ivjDRaceCheckBox.setText("Race Condition");
			ivjDRaceCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjDRaceCheckBox.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDRaceCheckBox;
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
 * Return the FillerPanel5 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getFillerPanel5() {
	if (ivjFillerPanel5 == null) {
		try {
			ivjFillerPanel5 = new javax.swing.JPanel();
			ivjFillerPanel5.setName("FillerPanel5");
			ivjFillerPanel5.setLayout(null);
			ivjFillerPanel5.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFillerPanel5;
}
/**
 * Return the GarbageCollectionCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getGarbageCollectionCheckBox() {
	if (ivjGarbageCollectionCheckBox == null) {
		try {
			ivjGarbageCollectionCheckBox = new javax.swing.JCheckBox();
			ivjGarbageCollectionCheckBox.setName("GarbageCollectionCheckBox");
			ivjGarbageCollectionCheckBox.setSelected(true);
			ivjGarbageCollectionCheckBox.setText("Garbage Collection");
			ivjGarbageCollectionCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjGarbageCollectionCheckBox;
}
/**
 * Return the HashCompactCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getHashCompactCheckBox() {
	if (ivjHashCompactCheckBox == null) {
		try {
			ivjHashCompactCheckBox = new javax.swing.JCheckBox();
			ivjHashCompactCheckBox.setName("HashCompactCheckBox");
			ivjHashCompactCheckBox.setMnemonic('h');
			ivjHashCompactCheckBox.setText("Hash Compaction");
			ivjHashCompactCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjHashCompactCheckBox;
}
/**
 * Return the HeapSymmetryCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getHeapSymmetryCheckBox() {
	if (ivjHeapSymmetryCheckBox == null) {
		try {
			ivjHeapSymmetryCheckBox = new javax.swing.JCheckBox();
			ivjHeapSymmetryCheckBox.setName("HeapSymmetryCheckBox");
			ivjHeapSymmetryCheckBox.setSelected(true);
			ivjHeapSymmetryCheckBox.setText("Heap Symmetry");
			ivjHeapSymmetryCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjHeapSymmetryCheckBox;
}
/**
 * Return the HeuristicPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getHeuristicPanel() {
	if (ivjHeuristicPanel == null) {
		try {
			ivjHeuristicPanel = new javax.swing.JPanel();
			ivjHeuristicPanel.setName("HeuristicPanel");
			ivjHeuristicPanel.setBorder(new TitledBorder(new EtchedBorder(), "Heuristic"));
			ivjHeuristicPanel.setLayout(new java.awt.GridBagLayout());
			ivjHeuristicPanel.setBackground(new java.awt.Color(204,204,255));
			ivjHeuristicPanel.setEnabled(true);

			java.awt.GridBagConstraints constraintsInterleavingRadioButton = new java.awt.GridBagConstraints();
			constraintsInterleavingRadioButton.gridx = 0; constraintsInterleavingRadioButton.gridy = 0;
			constraintsInterleavingRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsInterleavingRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getHeuristicPanel().add(getInterleavingRadioButton(), constraintsInterleavingRadioButton);

			java.awt.GridBagConstraints constraintsChooseFreeRadioButton = new java.awt.GridBagConstraints();
			constraintsChooseFreeRadioButton.gridx = 0; constraintsChooseFreeRadioButton.gridy = 1;
			constraintsChooseFreeRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsChooseFreeRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getHeuristicPanel().add(getChooseFreeRadioButton(), constraintsChooseFreeRadioButton);

			java.awt.GridBagConstraints constraintsMostBlockedRadioButton = new java.awt.GridBagConstraints();
			constraintsMostBlockedRadioButton.gridx = 0; constraintsMostBlockedRadioButton.gridy = 2;
			constraintsMostBlockedRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsMostBlockedRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getHeuristicPanel().add(getMostBlockedRadioButton(), constraintsMostBlockedRadioButton);

			java.awt.GridBagConstraints constraintsBranchPathRadioButton = new java.awt.GridBagConstraints();
			constraintsBranchPathRadioButton.gridx = 0; constraintsBranchPathRadioButton.gridy = 3;
			constraintsBranchPathRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBranchPathRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getHeuristicPanel().add(getBranchPathRadioButton(), constraintsBranchPathRadioButton);

			java.awt.GridBagConstraints constraintsBranchGlobalRadioButton = new java.awt.GridBagConstraints();
			constraintsBranchGlobalRadioButton.gridx = 0; constraintsBranchGlobalRadioButton.gridy = 4;
			constraintsBranchGlobalRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBranchGlobalRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getHeuristicPanel().add(getBranchGlobalRadioButton(), constraintsBranchGlobalRadioButton);

			java.awt.GridBagConstraints constraintsUserRadioButton = new java.awt.GridBagConstraints();
			constraintsUserRadioButton.gridx = 0; constraintsUserRadioButton.gridy = 5;
			constraintsUserRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsUserRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getHeuristicPanel().add(getUserRadioButton(), constraintsUserRadioButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjHeuristicPanel;
}
/**
 * Return the InterleavingRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getInterleavingRadioButton() {
	if (ivjInterleavingRadioButton == null) {
		try {
			ivjInterleavingRadioButton = new javax.swing.JRadioButton();
			ivjInterleavingRadioButton.setName("InterleavingRadioButton");
			ivjInterleavingRadioButton.setText("Interleaving");
			ivjInterleavingRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjInterleavingRadioButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInterleavingRadioButton;
}
/**
 * Return the JPFOptionDialogContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPFOptionDialogContentPane() {
	if (ivjJPFOptionDialogContentPane == null) {
		try {
			ivjJPFOptionDialogContentPane = new javax.swing.JPanel();
			ivjJPFOptionDialogContentPane.setName("JPFOptionDialogContentPane");
			ivjJPFOptionDialogContentPane.setLayout(new java.awt.GridBagLayout());
			ivjJPFOptionDialogContentPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsJPFOptionsTabbedPane = new java.awt.GridBagConstraints();
			constraintsJPFOptionsTabbedPane.gridx = 0; constraintsJPFOptionsTabbedPane.gridy = 0;
			constraintsJPFOptionsTabbedPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPFOptionsTabbedPane.weightx = 0.1;
			constraintsJPFOptionsTabbedPane.weighty = 0.1;
			constraintsJPFOptionsTabbedPane.insets = new java.awt.Insets(4, 4, 15, 4);
			getJPFOptionDialogContentPane().add(getJPFOptionsTabbedPane(), constraintsJPFOptionsTabbedPane);

			java.awt.GridBagConstraints constraintsAdvancedOptionsPanel = new java.awt.GridBagConstraints();
			constraintsAdvancedOptionsPanel.gridx = 0; constraintsAdvancedOptionsPanel.gridy = 1;
			constraintsAdvancedOptionsPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAdvancedOptionsPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPFOptionDialogContentPane().add(getAdvancedOptionsPanel(), constraintsAdvancedOptionsPanel);

			java.awt.GridBagConstraints constraintsOkButton = new java.awt.GridBagConstraints();
			constraintsOkButton.gridx = 0; constraintsOkButton.gridy = 2;
			constraintsOkButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPFOptionDialogContentPane().add(getOkButton(), constraintsOkButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFOptionDialogContentPane;
}
/**
 * Return the JPFOptionsTabbedPane property value.
 * @return javax.swing.JTabbedPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTabbedPane getJPFOptionsTabbedPane() {
	if (ivjJPFOptionsTabbedPane == null) {
		try {
			ivjJPFOptionsTabbedPane = new javax.swing.JTabbedPane();
			ivjJPFOptionsTabbedPane.setName("JPFOptionsTabbedPane");
			ivjJPFOptionsTabbedPane.setBackground(new java.awt.Color(204,204,255));
			ivjJPFOptionsTabbedPane.insertTab("Verification", null, getVerificationPanel(), null, 0);
			ivjJPFOptionsTabbedPane.setBackgroundAt(0, new java.awt.Color(204,204,255));
			ivjJPFOptionsTabbedPane.insertTab("Runtime Analysis", null, getRuntimeAnalysisPanel(), null, 1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFOptionsTabbedPane;
}
/**
 * Return the LockOrderCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getLockOrderCheckBox() {
	if (ivjLockOrderCheckBox == null) {
		try {
			ivjLockOrderCheckBox = new javax.swing.JCheckBox();
			ivjLockOrderCheckBox.setName("LockOrderCheckBox");
			ivjLockOrderCheckBox.setText("Lock Ordering");
			ivjLockOrderCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLockOrderCheckBox;
}
/**
 * Return the MostBlockedRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getMostBlockedRadioButton() {
	if (ivjMostBlockedRadioButton == null) {
		try {
			ivjMostBlockedRadioButton = new javax.swing.JRadioButton();
			ivjMostBlockedRadioButton.setName("MostBlockedRadioButton");
			ivjMostBlockedRadioButton.setText("Most-blocked");
			ivjMostBlockedRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjMostBlockedRadioButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMostBlockedRadioButton;
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
 * Get the Options model that is being used for this view.
 *
 * @return Options The Options model that is being configured by this view.
 */
public Options getOptions() {

	// if the advanced options check box is selected then the user has probably
	//  enter some form of advanced command.  therefore, grab that text and
	//  set that information in the current model before returning it.
	JCheckBox cloCheckBox = getCommandLineOptionCheckBox();
	if(cloCheckBox.isSelected()) {
		JTextField cloTextField = getAdvancedOptionsTextField();
		jpfOptions.init(cloTextField.getText());
	}

		return(jpfOptions);
		
}
/**
 * Return the POCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getPOCheckBox() {
	if (ivjPOCheckBox == null) {
		try {
			ivjPOCheckBox = new javax.swing.JCheckBox();
			ivjPOCheckBox.setName("POCheckBox");
			ivjPOCheckBox.setText("Partial Order Reduction");
			ivjPOCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPOCheckBox;
}
/**
 * Return the JCheckBox2 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getPostVerifyCheckBox() {
	if (ivjPostVerifyCheckBox == null) {
		try {
			ivjPostVerifyCheckBox = new javax.swing.JCheckBox();
			ivjPostVerifyCheckBox.setName("PostVerifyCheckBox");
			ivjPostVerifyCheckBox.setText("Post Verify");
			ivjPostVerifyCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjPostVerifyCheckBox.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPostVerifyCheckBox;
}
/**
 * Return the QueueLimitCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getQueueLimitCheckBox() {
	if (ivjQueueLimitCheckBox == null) {
		try {
			ivjQueueLimitCheckBox = new javax.swing.JCheckBox();
			ivjQueueLimitCheckBox.setName("QueueLimitCheckBox");
			ivjQueueLimitCheckBox.setText("Queue Limit");
			ivjQueueLimitCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjQueueLimitCheckBox.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueueLimitCheckBox;
}
/**
 * Return the QueueLimitPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getQueueLimitPanel() {
	if (ivjQueueLimitPanel == null) {
		try {
			ivjQueueLimitPanel = new javax.swing.JPanel();
			ivjQueueLimitPanel.setName("QueueLimitPanel");
			ivjQueueLimitPanel.setLayout(new java.awt.GridBagLayout());
			ivjQueueLimitPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsQueueLimitCheckBox = new java.awt.GridBagConstraints();
			constraintsQueueLimitCheckBox.gridx = 0; constraintsQueueLimitCheckBox.gridy = 0;
			constraintsQueueLimitCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getQueueLimitPanel().add(getQueueLimitCheckBox(), constraintsQueueLimitCheckBox);

			java.awt.GridBagConstraints constraintsQueueLimitTextField = new java.awt.GridBagConstraints();
			constraintsQueueLimitTextField.gridx = 1; constraintsQueueLimitTextField.gridy = 0;
			constraintsQueueLimitTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsQueueLimitTextField.weightx = 1.0;
			constraintsQueueLimitTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getQueueLimitPanel().add(getQueueLimitTextField(), constraintsQueueLimitTextField);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueueLimitPanel;
}
/**
 * Return the QueueLimitSearchDepthPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getQueueLimitSearchDepthPanel() {
	if (ivjQueueLimitSearchDepthPanel == null) {
		try {
			ivjQueueLimitSearchDepthPanel = new javax.swing.JPanel();
			ivjQueueLimitSearchDepthPanel.setName("QueueLimitSearchDepthPanel");
			ivjQueueLimitSearchDepthPanel.setLayout(new java.awt.GridBagLayout());
			ivjQueueLimitSearchDepthPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsQueueLimitPanel = new java.awt.GridBagConstraints();
			constraintsQueueLimitPanel.gridx = 1; constraintsQueueLimitPanel.gridy = 1;
			constraintsQueueLimitPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsQueueLimitPanel.weightx = 1.0;
			constraintsQueueLimitPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getQueueLimitSearchDepthPanel().add(getQueueLimitPanel(), constraintsQueueLimitPanel);

			java.awt.GridBagConstraints constraintsSearchDepthPanel = new java.awt.GridBagConstraints();
			constraintsSearchDepthPanel.gridx = 2; constraintsSearchDepthPanel.gridy = 1;
			constraintsSearchDepthPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSearchDepthPanel.weightx = 1.0;
			constraintsSearchDepthPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getQueueLimitSearchDepthPanel().add(getSearchDepthPanel(), constraintsSearchDepthPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueueLimitSearchDepthPanel;
}
/**
 * Return the QueueLimitTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getQueueLimitTextField() {
	if (ivjQueueLimitTextField == null) {
		try {
			ivjQueueLimitTextField = new javax.swing.JTextField();
			ivjQueueLimitTextField.setName("QueueLimitTextField");
			ivjQueueLimitTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjQueueLimitTextField.setText("-1");
			ivjQueueLimitTextField.setBackground(java.awt.Color.lightGray);
			ivjQueueLimitTextField.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueueLimitTextField;
}
/**
 * Return the RaceCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getRaceCheckBox() {
	if (ivjRaceCheckBox == null) {
		try {
			ivjRaceCheckBox = new javax.swing.JCheckBox();
			ivjRaceCheckBox.setName("RaceCheckBox");
			ivjRaceCheckBox.setText("Race Condition");
			ivjRaceCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRaceCheckBox;
}
/**
 * Return the JTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getRaceLevelTextField() {
	if (ivjRaceLevelTextField == null) {
		try {
			ivjRaceLevelTextField = new javax.swing.JTextField();
			ivjRaceLevelTextField.setName("RaceLevelTextField");
			ivjRaceLevelTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjRaceLevelTextField.setText("10");
			ivjRaceLevelTextField.setBackground(java.awt.Color.lightGray);
			ivjRaceLevelTextField.setEditable(true);
			ivjRaceLevelTextField.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRaceLevelTextField;
}
/**
 * Return the RuntimeAnalysisPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getRuntimeAnalysisPanel() {
	if (ivjRuntimeAnalysisPanel == null) {
		try {
			ivjRuntimeAnalysisPanel = new javax.swing.JPanel();
			ivjRuntimeAnalysisPanel.setName("RuntimeAnalysisPanel");
			ivjRuntimeAnalysisPanel.setToolTipText("Set the options to be used when running JPF in Runtime Analysis mode.");
			ivjRuntimeAnalysisPanel.setLayout(new java.awt.GridBagLayout());
			ivjRuntimeAnalysisPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsRaceCheckBox = new java.awt.GridBagConstraints();
			constraintsRaceCheckBox.gridx = 0; constraintsRaceCheckBox.gridy = 0;
			constraintsRaceCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsRaceCheckBox.insets = new java.awt.Insets(15, 10, 0, 0);
			getRuntimeAnalysisPanel().add(getRaceCheckBox(), constraintsRaceCheckBox);

			java.awt.GridBagConstraints constraintsLockOrderCheckBox = new java.awt.GridBagConstraints();
			constraintsLockOrderCheckBox.gridx = 0; constraintsLockOrderCheckBox.gridy = 1;
			constraintsLockOrderCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsLockOrderCheckBox.insets = new java.awt.Insets(0, 10, 0, 0);
			getRuntimeAnalysisPanel().add(getLockOrderCheckBox(), constraintsLockOrderCheckBox);

			java.awt.GridBagConstraints constraintsDependCheckBox = new java.awt.GridBagConstraints();
			constraintsDependCheckBox.gridx = 0; constraintsDependCheckBox.gridy = 3;
			constraintsDependCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDependCheckBox.insets = new java.awt.Insets(0, 40, 0, 0);
			getRuntimeAnalysisPanel().add(getDependCheckBox(), constraintsDependCheckBox);

			java.awt.GridBagConstraints constraintsRaceLevelTextField = new java.awt.GridBagConstraints();
			constraintsRaceLevelTextField.gridx = 1; constraintsRaceLevelTextField.gridy = 0;
			constraintsRaceLevelTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsRaceLevelTextField.weightx = 1.0;
			constraintsRaceLevelTextField.insets = new java.awt.Insets(15, 10, 0, 0);
			getRuntimeAnalysisPanel().add(getRaceLevelTextField(), constraintsRaceLevelTextField);

			java.awt.GridBagConstraints constraintsFillerPanel4 = new java.awt.GridBagConstraints();
			constraintsFillerPanel4.gridx = 1; constraintsFillerPanel4.gridy = 3;
			constraintsFillerPanel4.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFillerPanel4.weightx = 1.0;
			constraintsFillerPanel4.insets = new java.awt.Insets(4, 4, 4, 4);
			getRuntimeAnalysisPanel().add(getFillerPanel4(), constraintsFillerPanel4);

			java.awt.GridBagConstraints constraintsFillerPanel5 = new java.awt.GridBagConstraints();
			constraintsFillerPanel5.gridx = 2; constraintsFillerPanel5.gridy = 3;
			constraintsFillerPanel5.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFillerPanel5.weightx = 1.0;
			constraintsFillerPanel5.insets = new java.awt.Insets(4, 4, 4, 4);
			getRuntimeAnalysisPanel().add(getFillerPanel5(), constraintsFillerPanel5);

			java.awt.GridBagConstraints constraintsDebugPanel = new java.awt.GridBagConstraints();
			constraintsDebugPanel.gridx = 0; constraintsDebugPanel.gridy = 4;
			constraintsDebugPanel.gridwidth = 3;
			constraintsDebugPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsDebugPanel.weightx = 1.0;
			constraintsDebugPanel.weighty = 1.0;
			constraintsDebugPanel.insets = new java.awt.Insets(15, 10, 10, 10);
			getRuntimeAnalysisPanel().add(getDebugPanel(), constraintsDebugPanel);

			java.awt.GridBagConstraints constraintsPostVerifyCheckBox = new java.awt.GridBagConstraints();
			constraintsPostVerifyCheckBox.gridx = 0; constraintsPostVerifyCheckBox.gridy = 2;
			constraintsPostVerifyCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPostVerifyCheckBox.insets = new java.awt.Insets(5, 25, 0, 0);
			getRuntimeAnalysisPanel().add(getPostVerifyCheckBox(), constraintsPostVerifyCheckBox);
			// user code begin {1}

			// BEGIN HACK
			//Disabling the Runtime Analysis before ETAPS for Willem.
			getRaceCheckBox().setEnabled(false);
			getLockOrderCheckBox().setEnabled(false);
			// END HACK

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRuntimeAnalysisPanel;
}
/**
 * Return the SearchDepthCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getSearchDepthCheckBox() {
	if (ivjSearchDepthCheckBox == null) {
		try {
			ivjSearchDepthCheckBox = new javax.swing.JCheckBox();
			ivjSearchDepthCheckBox.setName("SearchDepthCheckBox");
			ivjSearchDepthCheckBox.setText("Maximum Search Depth:");
			ivjSearchDepthCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchDepthCheckBox;
}
/**
 * Return the SearchDepthPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSearchDepthPanel() {
	if (ivjSearchDepthPanel == null) {
		try {
			ivjSearchDepthPanel = new javax.swing.JPanel();
			ivjSearchDepthPanel.setName("SearchDepthPanel");
			ivjSearchDepthPanel.setLayout(new java.awt.GridBagLayout());
			ivjSearchDepthPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSearchDepthCheckBox = new java.awt.GridBagConstraints();
			constraintsSearchDepthCheckBox.gridx = 0; constraintsSearchDepthCheckBox.gridy = 0;
			constraintsSearchDepthCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getSearchDepthPanel().add(getSearchDepthCheckBox(), constraintsSearchDepthCheckBox);

			java.awt.GridBagConstraints constraintsSearchDepthTextField = new java.awt.GridBagConstraints();
			constraintsSearchDepthTextField.gridx = 1; constraintsSearchDepthTextField.gridy = 0;
			constraintsSearchDepthTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSearchDepthTextField.weightx = 1.0;
			constraintsSearchDepthTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getSearchDepthPanel().add(getSearchDepthTextField(), constraintsSearchDepthTextField);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchDepthPanel;
}
/**
 * Return the SearchDepthTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getSearchDepthTextField() {
	if (ivjSearchDepthTextField == null) {
		try {
			ivjSearchDepthTextField = new javax.swing.JTextField();
			ivjSearchDepthTextField.setName("SearchDepthTextField");
			ivjSearchDepthTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjSearchDepthTextField.setText("-1");
			ivjSearchDepthTextField.setBackground(java.awt.Color.lightGray);
			ivjSearchDepthTextField.setEnabled(false);
			ivjSearchDepthTextField.setEditable(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchDepthTextField;
}
/**
 * Return the StrategyPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getStrategyPanel() {
	if (ivjStrategyPanel == null) {
		try {
			ivjStrategyPanel = new javax.swing.JPanel();
			ivjStrategyPanel.setName("StrategyPanel");
			ivjStrategyPanel.setBorder(new TitledBorder(new EtchedBorder(), "Strategy"));
			ivjStrategyPanel.setLayout(new java.awt.GridBagLayout());
			ivjStrategyPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsBFSRadioButton = new java.awt.GridBagConstraints();
			constraintsBFSRadioButton.gridx = 0; constraintsBFSRadioButton.gridy = 1;
			constraintsBFSRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBFSRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getStrategyPanel().add(getBFSRadioButton(), constraintsBFSRadioButton);

			java.awt.GridBagConstraints constraintsBestFirstRadioButton = new java.awt.GridBagConstraints();
			constraintsBestFirstRadioButton.gridx = 0; constraintsBestFirstRadioButton.gridy = 2;
			constraintsBestFirstRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBestFirstRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getStrategyPanel().add(getBestFirstRadioButton(), constraintsBestFirstRadioButton);

			java.awt.GridBagConstraints constraintsAStarRadioButton = new java.awt.GridBagConstraints();
			constraintsAStarRadioButton.gridx = 0; constraintsAStarRadioButton.gridy = 3;
			constraintsAStarRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsAStarRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getStrategyPanel().add(getAStarRadioButton(), constraintsAStarRadioButton);

			java.awt.GridBagConstraints constraintsBeamRadioButton = new java.awt.GridBagConstraints();
			constraintsBeamRadioButton.gridx = 0; constraintsBeamRadioButton.gridy = 4;
			constraintsBeamRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBeamRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getStrategyPanel().add(getBeamRadioButton(), constraintsBeamRadioButton);

			java.awt.GridBagConstraints constraintsDFSRadioButton = new java.awt.GridBagConstraints();
			constraintsDFSRadioButton.gridx = 0; constraintsDFSRadioButton.gridy = 0;
			constraintsDFSRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsDFSRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getStrategyPanel().add(getDFSRadioButton(), constraintsDFSRadioButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStrategyPanel;
}
/**
 * Return the UserRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getUserRadioButton() {
	if (ivjUserRadioButton == null) {
		try {
			ivjUserRadioButton = new javax.swing.JRadioButton();
			ivjUserRadioButton.setName("UserRadioButton");
			ivjUserRadioButton.setText("User");
			ivjUserRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjUserRadioButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUserRadioButton;
}
/**
 * Return the QueueLimitPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getVerificationOptionsPanel() {
	if (ivjVerificationOptionsPanel == null) {
		try {
			ivjVerificationOptionsPanel = new javax.swing.JPanel();
			ivjVerificationOptionsPanel.setName("VerificationOptionsPanel");
			ivjVerificationOptionsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Verification Options"));
			ivjVerificationOptionsPanel.setLayout(new java.awt.GridBagLayout());
			ivjVerificationOptionsPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsHashCompactCheckBox = new java.awt.GridBagConstraints();
			constraintsHashCompactCheckBox.gridx = 0; constraintsHashCompactCheckBox.gridy = 1;
			constraintsHashCompactCheckBox.gridwidth = 2;
			constraintsHashCompactCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsHashCompactCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getHashCompactCheckBox(), constraintsHashCompactCheckBox);

			java.awt.GridBagConstraints constraintsPOCheckBox = new java.awt.GridBagConstraints();
			constraintsPOCheckBox.gridx = 1; constraintsPOCheckBox.gridy = 1;
			constraintsPOCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsPOCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getPOCheckBox(), constraintsPOCheckBox);

			java.awt.GridBagConstraints constraintsAssertionCheckBox = new java.awt.GridBagConstraints();
			constraintsAssertionCheckBox.gridx = 2; constraintsAssertionCheckBox.gridy = 2;
			constraintsAssertionCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsAssertionCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getAssertionCheckBox(), constraintsAssertionCheckBox);

			java.awt.GridBagConstraints constraintsAtomicLinesCheckBox = new java.awt.GridBagConstraints();
			constraintsAtomicLinesCheckBox.gridx = 1; constraintsAtomicLinesCheckBox.gridy = 2;
			constraintsAtomicLinesCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsAtomicLinesCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getAtomicLinesCheckBox(), constraintsAtomicLinesCheckBox);

			java.awt.GridBagConstraints constraintsGarbageCollectionCheckBox = new java.awt.GridBagConstraints();
			constraintsGarbageCollectionCheckBox.gridx = 0; constraintsGarbageCollectionCheckBox.gridy = 3;
			constraintsGarbageCollectionCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsGarbageCollectionCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getGarbageCollectionCheckBox(), constraintsGarbageCollectionCheckBox);

			java.awt.GridBagConstraints constraintsHeapSymmetryCheckBox = new java.awt.GridBagConstraints();
			constraintsHeapSymmetryCheckBox.gridx = 0; constraintsHeapSymmetryCheckBox.gridy = 2;
			constraintsHeapSymmetryCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsHeapSymmetryCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getHeapSymmetryCheckBox(), constraintsHeapSymmetryCheckBox);

			java.awt.GridBagConstraints constraintsDeadlockCheckBox = new java.awt.GridBagConstraints();
			constraintsDeadlockCheckBox.gridx = 2; constraintsDeadlockCheckBox.gridy = 1;
			constraintsDeadlockCheckBox.anchor = java.awt.GridBagConstraints.WEST;
			constraintsDeadlockCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getDeadlockCheckBox(), constraintsDeadlockCheckBox);

			java.awt.GridBagConstraints constraintsQueueLimitSearchDepthPanel = new java.awt.GridBagConstraints();
			constraintsQueueLimitSearchDepthPanel.gridx = 0; constraintsQueueLimitSearchDepthPanel.gridy = 0;
			constraintsQueueLimitSearchDepthPanel.gridwidth = 3;
			constraintsQueueLimitSearchDepthPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsQueueLimitSearchDepthPanel.weightx = 1.0;
			constraintsQueueLimitSearchDepthPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationOptionsPanel().add(getQueueLimitSearchDepthPanel(), constraintsQueueLimitSearchDepthPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVerificationOptionsPanel;
}
/**
 * Return the HeuristicSearchPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getVerificationPanel() {
	if (ivjVerificationPanel == null) {
		try {
			ivjVerificationPanel = new javax.swing.JPanel();
			ivjVerificationPanel.setName("VerificationPanel");
			ivjVerificationPanel.setToolTipText("Set the options to be used when running JPF in Verification mode.");
			ivjVerificationPanel.setLayout(new java.awt.GridBagLayout());
			ivjVerificationPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsStrategyPanel = new java.awt.GridBagConstraints();
			constraintsStrategyPanel.gridx = 0; constraintsStrategyPanel.gridy = 0;
			constraintsStrategyPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsStrategyPanel.weightx = 1.0;
			constraintsStrategyPanel.weighty = 1.0;
			constraintsStrategyPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationPanel().add(getStrategyPanel(), constraintsStrategyPanel);

			java.awt.GridBagConstraints constraintsHeuristicPanel = new java.awt.GridBagConstraints();
			constraintsHeuristicPanel.gridx = 1; constraintsHeuristicPanel.gridy = 0;
			constraintsHeuristicPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsHeuristicPanel.weightx = 1.0;
			constraintsHeuristicPanel.weighty = 1.0;
			constraintsHeuristicPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationPanel().add(getHeuristicPanel(), constraintsHeuristicPanel);

			java.awt.GridBagConstraints constraintsVerificationOptionsPanel = new java.awt.GridBagConstraints();
			constraintsVerificationOptionsPanel.gridx = 0; constraintsVerificationOptionsPanel.gridy = 1;
			constraintsVerificationOptionsPanel.gridwidth = 2;
			constraintsVerificationOptionsPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsVerificationOptionsPanel.weightx = 1.0;
			constraintsVerificationOptionsPanel.weighty = 1.0;
			constraintsVerificationOptionsPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getVerificationPanel().add(getVerificationOptionsPanel(), constraintsVerificationOptionsPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVerificationPanel;
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
 * When the hash compaction check box changes state update the model.
 */
public void hashCompactCheckBox_ActionPerformed() {
	if(getHashCompactCheckBox().isSelected()) {
		jpfOptions.enableHashCompaction();
	}
	else {
		jpfOptions.disableHashCompaction();
	}
}
/**
 * If the Heap symmetry box is not turned on, turn off the garbage
 * collection box.  Update the model as well.
 */
public void heapSymmetryCheckBox_ActionEvents() {
	if (!getHeapSymmetryCheckBox().isSelected()) {
		getGarbageCollectionCheckBox().setSelected(false);
		jpfOptions.disableGarbageCollection();
		jpfOptions.disableHeapSymmetry();
	}
	else {
		jpfOptions.enableHeapSymmetry();
	}
}
 /**
     * Initialize the view using the given options as the model.
     *
     * @param Options options The Options to use as the model for our view.
     * @pre options != null
     * @pre options instanceof JPFOptions
     */
public void init(edu.ksu.cis.bandera.checker.Options options) {
	
	if((options != null) && (options instanceof JPFOptions)) {
		jpfOptions = (JPFOptions)options;

		// now init the view to match the settings in these options
		switch(jpfOptions.getStrategy()) {
			case JPFOptions.DFS_STRATEGY:
				getDFSRadioButton().setSelected(true);
				getSearchDepthCheckBox().setEnabled(true);
				getQueueLimitCheckBox().setEnabled(false);
				getQueueLimitTextField().setEnabled(false);
				getInterleavingRadioButton().setEnabled(false);
				getChooseFreeRadioButton().setEnabled(false);
				getMostBlockedRadioButton().setEnabled(false);
				getBranchPathRadioButton().setEnabled(false);
				getBranchGlobalRadioButton().setEnabled(false);
				getUserRadioButton().setEnabled(false);
				if(jpfOptions.isSearchDepthEnabled()) {
					int searchDepth = jpfOptions.getSearchDepth();
					getSearchDepthCheckBox().setSelected(true);
					getSearchDepthTextField().setEnabled(true);
					getSearchDepthTextField().setText(String.valueOf(searchDepth));
				}
				break;
			case JPFOptions.BFS_STRATEGY:
				getBFSRadioButton().setSelected(true);
				getQueueLimitCheckBox().setEnabled(true);
				getSearchDepthCheckBox().setEnabled(false);
				getSearchDepthTextField().setEnabled(false);
				getInterleavingRadioButton().setEnabled(false);
				getChooseFreeRadioButton().setEnabled(false);
				getMostBlockedRadioButton().setEnabled(false);
				getBranchPathRadioButton().setEnabled(false);
				getBranchGlobalRadioButton().setEnabled(false);
				getUserRadioButton().setEnabled(false);
				if(jpfOptions.isQueueLimitEnabled()) {
					int queueLimit = jpfOptions.getQueueLimit();
					getQueueLimitCheckBox().setSelected(true);
					getQueueLimitTextField().setEnabled(true);
					getQueueLimitTextField().setText(String.valueOf(queueLimit));
				}
				break;
			case JPFOptions.BEST_FIRST_STRATEGY:
				getBestFirstRadioButton().setSelected(true);
				getQueueLimitCheckBox().setEnabled(true);
				getSearchDepthCheckBox().setEnabled(false);
				getSearchDepthTextField().setEnabled(false);
				getInterleavingRadioButton().setEnabled(true);
				getChooseFreeRadioButton().setEnabled(true);
				getMostBlockedRadioButton().setEnabled(true);
				getBranchPathRadioButton().setEnabled(true);
				getBranchGlobalRadioButton().setEnabled(true);
				getUserRadioButton().setEnabled(true);
				switch(jpfOptions.getHeuristic()) {
					case JPFOptions.INTERLEAVING_HEURISTIC:
						getInterleavingRadioButton().setSelected(true);
						break;
					case JPFOptions.CHOOSE_FREE_HEURISTIC:
						getChooseFreeRadioButton().setSelected(true);
						break;
					case JPFOptions.MOST_BLOCKED_HEURISTIC:
						getMostBlockedRadioButton().setSelected(true);
						break;
					case JPFOptions.BRANCH_PATH_HEURISTIC:
						getBranchPathRadioButton().setSelected(true);
						break;
					case JPFOptions.BRANCH_GLOBAL_HEURISTIC:
						getBranchGlobalRadioButton().setSelected(true);
						break;
					case JPFOptions.USER_HEURISTIC:
						getUserRadioButton().setSelected(true);
						break;
					case JPFOptions.DEFAULT_HEURISTIC:
						// this should only happen when the default isn't one of the ones specified above. -tcw
						//  in this case, don't enable any of the radio buttons
						break;
					default:
						// report an error
				}
				if(jpfOptions.isQueueLimitEnabled()) {
					int queueLimit = jpfOptions.getQueueLimit();
					getQueueLimitCheckBox().setSelected(true);
					getQueueLimitTextField().setEnabled(true);
					getQueueLimitTextField().setText(String.valueOf(queueLimit));
				}
				break;
			case JPFOptions.ASTAR_STRATEGY:
				getAStarRadioButton().setSelected(true);
				getQueueLimitCheckBox().setEnabled(true);
				getSearchDepthCheckBox().setEnabled(false);
				getSearchDepthTextField().setEnabled(false);
				getInterleavingRadioButton().setEnabled(true);
				getChooseFreeRadioButton().setEnabled(true);
				getMostBlockedRadioButton().setEnabled(true);
				getBranchPathRadioButton().setEnabled(true);
				getBranchGlobalRadioButton().setEnabled(true);
				getUserRadioButton().setEnabled(true);
				switch(jpfOptions.getHeuristic()) {
					case JPFOptions.INTERLEAVING_HEURISTIC:
						getInterleavingRadioButton().setSelected(true);
						break;
					case JPFOptions.CHOOSE_FREE_HEURISTIC:
						getChooseFreeRadioButton().setSelected(true);
						break;
					case JPFOptions.MOST_BLOCKED_HEURISTIC:
						getMostBlockedRadioButton().setSelected(true);
						break;
					case JPFOptions.BRANCH_PATH_HEURISTIC:
						getBranchPathRadioButton().setSelected(true);
						break;
					case JPFOptions.BRANCH_GLOBAL_HEURISTIC:
						getBranchGlobalRadioButton().setSelected(true);
						break;
					case JPFOptions.USER_HEURISTIC:
						getUserRadioButton().setSelected(true);
						break;
					case JPFOptions.DEFAULT_HEURISTIC:
						// this should only happen when the default isn't one of the ones specified above. -tcw
						//  in this case, don't enable any of the radio buttons
						break;
					default:
						// report an error
				}
				if(jpfOptions.isQueueLimitEnabled()) {
					int queueLimit = jpfOptions.getQueueLimit();
					getQueueLimitCheckBox().setSelected(true);
					getQueueLimitTextField().setEnabled(true);
					getQueueLimitTextField().setText(String.valueOf(queueLimit));
				}
				break;
			case JPFOptions.BEAM_STRATEGY:
				getBeamRadioButton().setSelected(true);
				getQueueLimitCheckBox().setEnabled(true);
				getSearchDepthCheckBox().setEnabled(false);
				getSearchDepthTextField().setEnabled(false);
				getInterleavingRadioButton().setEnabled(true);
				getChooseFreeRadioButton().setEnabled(true);
				getMostBlockedRadioButton().setEnabled(true);
				getBranchPathRadioButton().setEnabled(true);
				getBranchGlobalRadioButton().setEnabled(true);
				getUserRadioButton().setEnabled(true);
				switch(jpfOptions.getHeuristic()) {
					case JPFOptions.INTERLEAVING_HEURISTIC:
						getInterleavingRadioButton().setSelected(true);
						break;
					case JPFOptions.CHOOSE_FREE_HEURISTIC:
						getChooseFreeRadioButton().setSelected(true);
						break;
					case JPFOptions.MOST_BLOCKED_HEURISTIC:
						getMostBlockedRadioButton().setSelected(true);
						break;
					case JPFOptions.BRANCH_PATH_HEURISTIC:
						getBranchPathRadioButton().setSelected(true);
						break;
					case JPFOptions.BRANCH_GLOBAL_HEURISTIC:
						getBranchGlobalRadioButton().setSelected(true);
						break;
					case JPFOptions.USER_HEURISTIC:
						getUserRadioButton().setSelected(true);
						break;
					case JPFOptions.DEFAULT_HEURISTIC:
						// this should only happen when the default isn't one of the ones specified above. -tcw
						//  in this case, don't enable any of the radio buttons
						break;
					default:
						// report an error
				}
				if(jpfOptions.isQueueLimitEnabled()) {
					int queueLimit = jpfOptions.getQueueLimit();
					getQueueLimitCheckBox().setSelected(true);
					getQueueLimitTextField().setEnabled(true);
					getQueueLimitTextField().setText(String.valueOf(queueLimit));
				}
				break;
			default:
				// report an error
		}

		getDeadlockCheckBox().setSelected(jpfOptions.useDeadlocks());
		getAssertionCheckBox().setSelected(jpfOptions.useAssertions());
		getAtomicLinesCheckBox().setSelected(jpfOptions.useAtomicLines());
		getGarbageCollectionCheckBox().setSelected(jpfOptions.useGarbageCollection());
		getHeapSymmetryCheckBox().setSelected(jpfOptions.useHeapSymmetry());
		getHashCompactCheckBox().setSelected(jpfOptions.useHashCompaction());
		getPOCheckBox().setSelected(jpfOptions.usePostOrderReduction());
		
		// ignore race, race level, debugs, post verify, etc. for now -tcw
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
	getLockOrderCheckBox().addActionListener(ivjEventHandler);
	getDependCheckBox().addActionListener(ivjEventHandler);
	getPostVerifyCheckBox().addActionListener(ivjEventHandler);
	getGarbageCollectionCheckBox().addActionListener(ivjEventHandler);
	getHeapSymmetryCheckBox().addActionListener(ivjEventHandler);
	getJPFOptionsTabbedPane().addPropertyChangeListener(ivjEventHandler);
	getRaceCheckBox().addChangeListener(ivjEventHandler);
	getSearchDepthCheckBox().addActionListener(ivjEventHandler);
	getQueueLimitCheckBox().addActionListener(ivjEventHandler);
	getQueueLimitTextField().addFocusListener(ivjEventHandler);
	getSearchDepthTextField().addFocusListener(ivjEventHandler);
	getDeadlockCheckBox().addActionListener(ivjEventHandler);
	getAssertionCheckBox().addActionListener(ivjEventHandler);
	getAtomicLinesCheckBox().addActionListener(ivjEventHandler);
	getPOCheckBox().addActionListener(ivjEventHandler);
	getHashCompactCheckBox().addActionListener(ivjEventHandler);
	getInterleavingRadioButton().addActionListener(ivjEventHandler);
	getChooseFreeRadioButton().addActionListener(ivjEventHandler);
	getMostBlockedRadioButton().addActionListener(ivjEventHandler);
	getBranchPathRadioButton().addActionListener(ivjEventHandler);
	getBranchGlobalRadioButton().addActionListener(ivjEventHandler);
	getUserRadioButton().addActionListener(ivjEventHandler);
	getBeamRadioButton().addActionListener(ivjEventHandler);
	getAStarRadioButton().addActionListener(ivjEventHandler);
	getBestFirstRadioButton().addActionListener(ivjEventHandler);
	getBFSRadioButton().addActionListener(ivjEventHandler);
	getDFSRadioButton().addActionListener(ivjEventHandler);
	getCommandLineOptionCheckBox().addActionListener(ivjEventHandler);
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
		setName("JPFOptionsView");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(518, 490);
		setTitle("JPF Options");
		setContentPane(getJPFOptionDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	strategyButtonGroup = new ButtonGroup();
	strategyButtonGroup.add(getDFSRadioButton());
	strategyButtonGroup.add(getBFSRadioButton());
	strategyButtonGroup.add(getBeamRadioButton());
	strategyButtonGroup.add(getBestFirstRadioButton());
	strategyButtonGroup.add(getAStarRadioButton());
	
	heuristicButtonGroup = new ButtonGroup();
	heuristicButtonGroup.add(getInterleavingRadioButton());
	heuristicButtonGroup.add(getChooseFreeRadioButton());
	heuristicButtonGroup.add(getMostBlockedRadioButton());
	heuristicButtonGroup.add(getBranchPathRadioButton());
	heuristicButtonGroup.add(getBranchGlobalRadioButton());
	heuristicButtonGroup.add(getUserRadioButton());
	
	// user code end
}

/**
 * When the interleaving radio button is pressed, we should update the model.
 */
public void interleavingRadioButton_ActionPerformed() {
	if(getInterleavingRadioButton().isSelected()) {
		try {
			jpfOptions.setHeuristic(JPFOptions.INTERLEAVING_HEURISTIC);
		}
		catch(Exception e) {
		}
	}
}
/**
 * Return true if verification is turned on, false otherwise.  This
 * is based upon what tab is currently selected.  The default is to
 * have the Verification tab selected.
 *
 * @return boolean True if verification is active, false otherwise.
 */
public boolean isVerification() {
	boolean verificationSelected = true;
	int tabSelected = getJPFOptionsTabbedPane().getSelectedIndex();
	if(tabSelected == 0) {
		verificationSelected = true;
	}
	else {
		verificationSelected = false;
	}
	
	return(verificationSelected);
}
/**
 * Each time the user changes between the Verification tab and the
 * Runtime Analysis tab, we need to reset the command line options
 * text box.  We should also turn verification on and off depending upon
 * the active tab.
 */
public void jPFOptionsTabbedPane_StateChanged(javax.swing.event.ChangeEvent stateChangeEvent) {
	
	getAdvancedOptionsTextField().setText("");
	
	if(getJPFOptionsTabbedPane().getSelectedComponent() == getVerificationPanel()) {
		jpfOptions.enableVerification();
	}
	else {
		jpfOptions.disableVerification();
	}
}
/**
 * Comment
 */
public void lockOrderCheckBox_ActionEvents() {
	if (getLockOrderCheckBox().isSelected()) {
		getPostVerifyCheckBox().setEnabled(true);
		getDLockOrderCheckBox().setEnabled(true);
	} else {
		if (!getRaceCheckBox().isSelected()) {
			getPostVerifyCheckBox().setEnabled(false);
			getPostVerifyCheckBox().setSelected(false);
			getDependCheckBox().setEnabled(false);
			getDependCheckBox().setSelected(false);
			getDDependCheckBox().setEnabled(false);
			getDDependCheckBox().setSelected(false);
		}
		getDLockOrderCheckBox().setEnabled(false);
		getDLockOrderCheckBox().setSelected(false);
	}
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JPFOptionsView aJPFOption;
		aJPFOption = new JPFOptionsView();
		JPFOptions jpfOptions = new JPFOptions();
		CompletionListener cl = new CompletionListener() {
			public void complete(Object o) {
				
				if((o != null) && (o instanceof JPFOptionsView)) {
					JPFOptionsView jpfOptionsView = (JPFOptionsView)o;
					jpfOptionsView.setVisible(false);
					try {
						System.out.println("jpfOptions = " + jpfOptionsView.getOptions().getCommandLineOptions());
					}
					catch(Exception e) {
					}
				}
				
				System.out.println("Complete.");
				System.exit(0);
			}
		};
		aJPFOption.registerCompletionListener(cl);
		jpfOptions.init();
		aJPFOption.init(jpfOptions);
		aJPFOption.setModal(true);
		aJPFOption.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aJPFOption.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JDialog");
		exception.printStackTrace(System.out);
	}
}
/**
 * When the most blocked radio button is selected, update the model.
 */
public void mostBlockedRadioButton_ActionPerformed() {
	if(getMostBlockedRadioButton().isSelected()) {
		try {
			jpfOptions.setHeuristic(JPFOptions.MOST_BLOCKED_HEURISTIC);
		}
		catch(Exception e) {
		}
	}
}
/**
 * When the OK button is pressed, we need to inform all the CompletionListeners
 * that we are complete.
 */
public void okButton_ActionPerformed() {

	setVisible(false);
	
	if((completionListenerSet != null) && (completionListenerSet.size() > 0)) {
		Iterator i = completionListenerSet.iterator();
		while(i.hasNext()) {
			CompletionListener cl = (CompletionListener)i.next();
			cl.complete(this);
		}
	}
	
}
/**
 * When the partial order reduction check box changes update the model.
 */
public void pOCheckBox_ActionPerformed() {
	if(getPOCheckBox().isSelected()) {
		jpfOptions.enablePostOrderReduction();
	}
	else {
		jpfOptions.disablePostOrderReduction();
	}
}
/**
 * Comment
 */
public void postVerifyCheckBox_ActionEvents() {
	if (getPostVerifyCheckBox().isSelected()) {
		getDependCheckBox().setEnabled(true);
	} else {
		getDependCheckBox().setEnabled(false);
		getDependCheckBox().setSelected(false);
		getDDependCheckBox().setEnabled(false);
		getDDependCheckBox().setSelected(false);
	}
}
/**
 * When the queue limit check box is selected the queue limit text field should be
 * enabled to allow the user to enter a value for the queue limit.  When the check box
 * is not selected the text field should be disabled.  Also, when it is selected, we should update
 * the model to make sure it has the current value that has been entered into the text field.  When
 * the check box is not selected, we can disable queue limit in the model as well.
 */
public void queueLimitCheckBox_ActionPerformed() {
	boolean selected = getQueueLimitCheckBox().isSelected();
	if(selected) {
		getQueueLimitTextField().setEnabled(true);
		int queueLimit = JPFOptions.DEFAULT_QUEUE_LIMIT;
		try {
			queueLimit = Integer.parseInt(getQueueLimitTextField().getText());
		}
		catch(Exception e) {
		}
		jpfOptions.setQueueLimit(queueLimit);
	}
	else {
		getQueueLimitTextField().setEnabled(false);
		jpfOptions.disableQueueLimit();
	}
}
/**
 * When the queue limit text field loses focus we should update the model with
 * the value that was entered into the text field.
 */
public void queueLimitTextField_FocusLost() {
	JTextField queueLimitTextField = getQueueLimitTextField();
	try {
		int queueLimit = Integer.parseInt(queueLimitTextField.getText());
		jpfOptions.setQueueLimit(queueLimit);
	}
	catch(Exception e) {
		queueLimitTextField.setText("Invalid value.");
	}
}
/**
 * Comment
 */
public void raceCheckBox_StateChanged(
    javax.swing.event.ChangeEvent stateChangeEvent) {
    boolean raceCheckBoxSelected = getRaceCheckBox().isSelected();

    // the text field should only be enabled when the check box is selected.
    getRaceLevelTextField().setEnabled(raceCheckBoxSelected);

    if (raceCheckBoxSelected) {
        getPostVerifyCheckBox().setEnabled(true);
        getDRaceCheckBox().setEnabled(true);
    }
    else {
        if (!getLockOrderCheckBox().isSelected()) {
            getPostVerifyCheckBox().setEnabled(false);
            getPostVerifyCheckBox().setSelected(false);
            getDependCheckBox().setEnabled(false);
            getDependCheckBox().setSelected(false);
            getDDependCheckBox().setEnabled(false);
            getDDependCheckBox().setSelected(false);
        }
        getDRaceCheckBox().setEnabled(false);
        getDRaceCheckBox().setSelected(false);
    }
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
 * When the search depth check box is selected, the search depth text field should
 * be enabled to allow the user to enter a value for search depth.  When the check box
 * is not selected, the text field should be disabled.
 */
public void searchDepthCheckBox_ActionPerformed() {
	boolean selected = getSearchDepthCheckBox().isSelected();
	if(selected) {
		getSearchDepthTextField().setEnabled(	true);
		int searchDepth = JPFOptions.DEFAULT_SEARCH_DEPTH;
		try {
			searchDepth = Integer.parseInt(getSearchDepthTextField().getText());
		}
		catch(Exception e) {
		}
		jpfOptions.setSearchDepth(searchDepth);
	}
	else {
		getSearchDepthTextField().setEnabled(	false);
		jpfOptions.disableSearchDepth();
	}
}
/**
 * When the focus is lost to another component, get the value that was entered by the
 * user in the search depth text field, parse it into an int, and set it in the model.  If an
 * error occurs print "Invalid value" in the text field.
 */
public void searchDepthTextField_FocusLost() {
	try {
		int searchDepth = Integer.parseInt(getSearchDepthTextField().getText());
		jpfOptions.setSearchDepth(searchDepth);
	}
	catch(Exception e) {
		getSearchDepthTextField().setText("Invalid value");
	}
}
/**
 * Set the Options model that is being used for this view.
 *
 * @param Options The Options model that is being configured by this view.
 */
public void setOptions(Options options) {
	init(options);		
}
/**
 * Comment
 */
public void userRadioButton_ActionPerformed() {
	if(getUserRadioButton().isSelected()) {
		try {
			jpfOptions.setHeuristic(JPFOptions.USER_HEURISTIC);
		}
		catch(Exception e) {
		}
	}
}
}
