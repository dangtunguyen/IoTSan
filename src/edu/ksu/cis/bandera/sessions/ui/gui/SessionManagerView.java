package edu.ksu.cis.bandera.sessions.ui.gui;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.Abstraction;
import edu.ksu.cis.bandera.sessions.ClassDescription;
import edu.ksu.cis.bandera.sessions.MethodDescription;
import edu.ksu.cis.bandera.sessions.FieldDescription;
import edu.ksu.cis.bandera.sessions.SessionManager;
import edu.ksu.cis.bandera.sessions.*;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsView;
import edu.ksu.cis.bandera.checker.DefaultOptions;
import edu.ksu.cis.bandera.checker.DefaultOptionsView;
import edu.ksu.cis.bandera.checker.CompletionListener;
import edu.ksu.cis.bandera.checker.OptionsFactory;

import edu.ksu.cis.bandera.checker.jpf.JPFOptions;
import edu.ksu.cis.bandera.checker.jpf.JPFOptionsView;

import edu.ksu.cis.bandera.checker.spin.SpinOptions;
import edu.ksu.cis.bandera.checker.spin.SpinOptionsView;

import edu.ksu.cis.bandera.bui.ExpressionBuilder;

import edu.ksu.cis.bandera.sessions.ui.gui.tree.variable.VariableTreeModelBuilder;

import java.util.Map;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.*;

import java.io.File;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import javax.swing.tree.*;

import javax.swing.*;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Container;
import java.awt.Component;

import edu.ksu.cis.bandera.specification.pattern.PatternSaverLoader;
import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;
import edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate;
import edu.ksu.cis.bandera.specification.predicate.datastructure.PredicateSet;

import org.apache.log4j.Category;

//import com.sun.rsasign.s;

/**
 * The SessionManagerView provides a GUI view of the SessionManager
 * information and a way to configure Session information in a GUI.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt; 
 * @version $Revision: 1.6 $ - $Date: 2003/06/23 18:59:53 $
 */
public final class SessionManagerView extends JFrame implements edu.ksu.cis.bandera.checker.CompletionListener, Observer {

    /*
     * Note: Changes were made to generated portions of this class and should be fixed
     * before trying to integrate with VisualAge for Java again. -tcw
     */

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(SessionManagerView.class);

	private JPanel ivjApplicationPane = null;
	private JPanel ivjGeneralPane = null;
	private JPanel ivjJFrameContentPane = null;
	private JTabbedPane ivjSessionInformationTabbedPane = null;
	private JList ivjSessionNameList = null;
	private JLabel ivjSessionNameListLabel = null;
	private JPanel ivjComponentsPane = null;
	private JPanel ivjResourceBoundsPane = null;
	private JTextArea ivjInfoTextArea = null;
	private JButton ivjOutputNameHelpButton = null;
	private JLabel ivjOutputNameLabel = null;
	private JTextField ivjOutputNameTextField = null;
	private JButton ivjDescriptionHelpButton = null;
	private JLabel ivjDescriptionLabel = null;
	private JTextArea ivjDescriptionTextArea = null;
	private JButton ivjWorkingDirectoryHelpButton = null;
	private JLabel ivjWorkingDirectoryLabel = null;
	private JTextField ivjWorkingDirectoryTextField = null;
	private JButton ivjClasspathAddButton = null;
	private JButton ivjClasspathHelpButton = null;
	private JList ivjClasspathList = null;
	private JPanel ivjClasspathPanel = null;
	private JButton ivjClasspathRemoveButton = null;
	private JButton ivjIncludesAddButton = null;
	private JList ivjIncludesList = null;
	private JButton ivjIncludesListHelpButton = null;
	private JButton ivjIncludesRemoveButton = null;
	private JButton ivjMainClassFileBrowseButton = null;
	private JButton ivjMainClassFileHelpButton = null;
	private JLabel ivjMainClassFileLabel = null;
	private JTextField ivjMainClassFileTextField = null;
	private JPanel ivjIncludesPanel = null;
	private JPanel ivjMainClassFilePanel = null;
	private JCheckBox ivjAbstractionCheckBox = null;
	private JPanel ivjComponentsPanel = null;
	private JButton ivjDSpinOptionsButton = null;
	private JRadioButton ivjDSpinRadioButton = null;
	private JButton ivjJPFOptionsButton = null;
	private JRadioButton ivjJPFRadioButton = null;
	private JCheckBox ivjModelCheckerCheckBox = null;
	private JPanel ivjModelCheckerOptionsPanel = null;
	private JCheckBox ivjSlicerCheckBox = null;
	private JButton ivjSMVOptionsButton = null;
	private JRadioButton ivjSMVRadioButton = null;
	private JButton ivjSpinOptionsButton = null;
	private JRadioButton ivjSpinRadioButton = null;
	private JButton ivjArrayBoundHelpButton = null;
	private JLabel ivjArrayBoundLabel = null;
	private JTextField ivjArrayBoundMaxTextField = null;
	private JButton ivjInstanceBoundHelpButton = null;
	private JLabel ivjInstanceBoundLabel = null;
	private JTextField ivjInstanceBoundMaxTextField = null;
	private JButton ivjIntegerBoundHelpButton = null;
	private JLabel ivjIntegerBoundLabel = null;
	private JTextField ivjIntegerBoundMaxTextField = null;
	private JTextField ivjIntegerBoundMinTextField = null;
	private JButton ivjResetDefaultsButton = null;
	private JButton ivjThreadInstanceBoundHelpButton = null;
	private JLabel ivjThreadInstanceBoundLabel = null;
	private JTextField ivjThreadInstanceBoundMaxTextField = null;
	private JButton ivjWorkingDirectoryBrowseButton = null;
	private JButton ivjInstanceBoundAddButton = null;
	private JPanel ivjInstanceBoundPanel = null;
	private JButton ivjInstanceBoundRemoveButton = null;
	private JButton ivjThreadInstanceBoundAddButton = null;
	private JPanel ivjThreadInstanceBoundPanel = null;
	private JButton ivjThreadInstanceBoundRemoveButton = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private edu.ksu.cis.bandera.sessions.SessionManager sessionManager;
	private JPanel ivjButtonPanel = null;
	private javax.swing.ButtonGroup modelCheckerButtonGroup;
	private JScrollPane ivjInfoTextAreaScollPane = null;
	private JScrollPane ivjSessionListScrollPane = null;
	private JButton ivjActivateSessionButton = null;
	private JMenu ivjFileMenu = null;
	private JMenuBar ivjSessionManagerViewJMenuBar = null;
	private JMenu ivjSessionMenu = null;
	private JCheckBoxMenuItem ivjShowSessionInformationMenuCheckBox = null;
	private JCheckBoxMenuItem ivjShowSessionToolBarMenuCheckBox = null;
	private JMenu ivjViewMenu = null;
	private JPanel ivjFileButtonPanel = null;
	private JPanel ivjSessionButtonPanel = null;
	private JButton ivjCloneSessionButton = null;
	private JButton ivjLoadFileButton = null;
	private JButton ivjNewFileButton = null;
	private JButton ivjNewSessionButton = null;
	private JButton ivjRemoveSessionButton = null;
	private JButton ivjSaveAsFileButton = null;
	private JButton ivjSaveFileButton = null;
	private JMenuItem ivjActivateSessionMenuItem = null;
	private JMenuItem ivjClonSessionMenuItem = null;
	private JMenuItem ivjLoadFileMenuItem = null;
	private JMenuItem ivjNewFileMenuItem = null;
	private JMenuItem ivjNewSessionMenuItem = null;
	private JMenuItem ivjRemoveSessionMenuItem = null;
	private JMenuItem ivjSaveAsFileMenuItem = null;
	private JMenuItem ivjSaveFileMenuItem = null;
	private JMenuItem ivjActivateSessionPopupMenuItem = null;
	private JMenuItem ivjCloneSessionPopupMenuItem = null;
	private JMenuItem ivjNewSessionPopupMenuItem = null;
	private JMenuItem ivjRemoveSessionPopupMenuItem = null;
	private JPopupMenu ivjSessionListPopupMenu = null;
	private SessionListCellRenderer sessionListCellRenderer;
	private JTable ivjInstanceBoundTable = null;
	private JScrollPane ivjInstanceBoundTableScrollPane = null;
	private JTable ivjThreadInstanceTable = null;
	private JScrollPane ivjThreadInstanceTableScrollPane = null;
	private JPanel ivjBIRSearchModePanel = null;
	private JRadioButton ivjChooseFreeRadioButton = null;
	private JLabel ivjDotDotLabel = null;
	private JRadioButton ivjExhaustiveRadioButton = null;
	private JRadioButton ivjResourceBoundedRadioButton = null;
	private javax.swing.ButtonGroup birSearchModeButtonGroup;
	private javax.swing.JFileChooser javaFileChooser;
	private javax.swing.JFileChooser sessionFileChooser;
	private javax.swing.JFileChooser lastFileChooser;
	private javax.swing.JFileChooser directoryFileChooser;
	private javax.swing.JFileChooser classpathFileChooser;
	private JTextArea ivjJTextArea1 = null;
	private JMenuItem ivjAddClasspathPopupMenuItem = null;
	private JPopupMenu ivjClasspathListPopupMenu = null;
	private JMenuItem ivjRemoveClasspathPopupMenuItem = null;
	private JMenuItem ivjAddIncludePopupMenuItem = null;
	private JPopupMenu ivjIncludesListPopupMenu = null;
	private JMenuItem ivjRemoveIncludesPopupMenuItem = null;
	private TreeModel abstractionTreeModel;
	private java.util.Set classSet;
	private JButton ivjAddQuantificationButton = null;
	private JScrollPane ivjAssertionListScrollPane = null;
	private JPanel ivjAssertionPanel = null;
	private JPanel ivjExpandedPropertyPanel = null;
	private JTextArea ivjExpandedPropertyTextArea = null;
	private JScrollPane ivjExpandedPropertyTextAreaScrollPane = null;
	private JButton ivjExpandPropertyButton = null;
	private JLabel ivjPatternLabel = null;
	private JComboBox ivjPatternNameComboBox = null;
	private JPanel ivjPatternPanel = null;
	private JPanel ivjPatternParameterInsidePanel = null;
	private JLabel ivjPatternParameterLabel = null;
	private JPanel ivjPatternParameterPanel = null;
	private JScrollPane ivjPatternParameterScrollPane = null;
	private JComboBox ivjPatternScopeComboBox = null;
	private JTextField ivjPatternTextField = null;
	private JPanel ivjPropertyPanel = null;
	private JLabel ivjQuantificationLabel = null;
	private JPanel ivjQuantificationPanel = null;
	private JTable ivjQuantificationTable = null;
	private JScrollPane ivjQuantificationTableScrollPane = null;
	private JButton ivjRemoveQuantificationButton = null;
	private JButton ivjSelectAllAssertionsButton = null;
	private JPanel ivjSpecificationPanel = null;
	private JPanel ivjSpecificationMessagePanel = null;
	private JPanel ivjSpecificationDisplayPanel = null;
	private JLabel ivjSpecificationMessageLabel = null;
	private JButton ivjUnselectedAllAssertionsButton = null;
	private JPanel ivjBIRArrayBoundPanel = null;
	private JLabel ivjBIRCheckerStrategyLabel = null;
	private JPanel ivjBIRModelCheckerPanel = null;
	private JLabel ivjCheckerNameLabel = null;
	private JLabel ivjCheckerNameLabel1 = null;
	private JPanel ivjDSpinCheckerPanel = null;
	private JPanel ivjJPFCheckerPanel = null;
	private JPanel ivjOtherModelCheckerPanel = null;
	private JPanel ivjSMVCheckerPanel = null;
	private JPanel ivjSpinCheckerPanel = null;
	private JButton ivjVariableBoundAddButton = null;
	private JButton ivjVariableBoundRemoveButton = null;
	private JTable ivjVariableBoundTable = null;
	private JScrollPane ivjVariableBoundTableScrollPane = null;
	private JButton ivjResetPropertyButton = null;
	private JButton ivjSavePropertyButton = null;
	private java.util.Map patternParameterTextFieldMap;
	private java.util.Map patternParameterLabelMap;
	private javax.swing.JTextField activeParameterTextField;
	private JPanel ivjAssertionListPanel = null;
	private java.util.Map assertionCheckBoxMap;
	private JPanel ivjBIRIntegerBoundPanel = null;
	private JPanel ivjAddVariableButtonPanel = null;
	private JButton ivjAddVariableCancelButton = null;
	private JDialog ivjAddVariableDialog = null;
	private JLabel ivjAddVariableMaximumLabel = null;
	private JLabel ivjAddVariableMinimumLabel = null;
	private JTextField ivjAddVariableMinimumTextField = null;
	private JButton ivjAddVariableOkButton = null;
	private JPanel ivjAddVariableTextFieldPanel = null;
	private JTree ivjAddVariableTree = null;
	private JScrollPane ivjAddVariableTreeScrollPane = null;
	private JPanel ivjJDialogContentPane = null;
	private JTextField ivjAddVariableMaximumTextField = null;
	private JPanel ivjJDialogContentPane1 = null;
	private JDialog ivjSessionInformationDialog = null;
	private JButton ivjSessionInformationOkButton = null;
	private JButton ivjShowSessionInformationButton = null;
	private JMenuItem ivjShowSessionInformationMenuItem = null;
	private JMenuItem ivjInfoSessionPopupMenuItem = null;
	private JButton ivjShowHideSessionToolbarButton = null;
	private JButton ivjShowHideSessionInformationButton = null;
	private final static javax.swing.Icon EXPANDED_PANE_ICON = new ImageIcon(SessionManagerView.class.getResource("/edu/ksu/cis/bandera/sessions/ui/gui/images/expandPaneIcon.gif"));
	private final static javax.swing.Icon CONTRACTED_PANE_ICON = new ImageIcon(SessionManagerView.class.getResource("/edu/ksu/cis/bandera/sessions/ui/gui/images/contractPaneIcon.gif"));
	private javax.swing.ButtonGroup searchModeRadioButtonGroup;
    private ExpressionBuilder expressionBuilder;
    private Set allAssertionSet;
    private boolean saveable = false;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener, java.awt.event.MouseListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == SessionManagerView.this.getInstanceBoundAddButton()) 
				connEtoC4(e);
			if (e.getSource() == SessionManagerView.this.getInstanceBoundRemoveButton()) 
				connEtoC5(e);
			if (e.getSource() == SessionManagerView.this.getSpinOptionsButton()) 
				connEtoC6(e);
			if (e.getSource() == SessionManagerView.this.getDSpinOptionsButton()) 
				connEtoC7(e);
			if (e.getSource() == SessionManagerView.this.getSMVOptionsButton()) 
				connEtoC8(e);
			if (e.getSource() == SessionManagerView.this.getJPFOptionsButton()) 
				connEtoC9(e);
			if (e.getSource() == SessionManagerView.this.getMainClassFileBrowseButton()) 
				connEtoC10(e);
			if (e.getSource() == SessionManagerView.this.getClasspathAddButton()) 
				connEtoC11(e);
			if (e.getSource() == SessionManagerView.this.getClasspathRemoveButton()) 
				connEtoC12(e);
			if (e.getSource() == SessionManagerView.this.getIncludesAddButton()) 
				connEtoC13(e);
			if (e.getSource() == SessionManagerView.this.getIncludesRemoveButton()) 
				connEtoC14(e);
			if (e.getSource() == SessionManagerView.this.getWorkingDirectoryBrowseButton()) 
				connEtoC15(e);
			if (e.getSource() == SessionManagerView.this.getResetDefaultsButton()) 
				connEtoC16(e);
			if (e.getSource() == SessionManagerView.this.getSlicerCheckBox()) 
				connEtoC17(e);
			if (e.getSource() == SessionManagerView.this.getAbstractionCheckBox()) 
				connEtoC18(e);
			if (e.getSource() == SessionManagerView.this.getModelCheckerCheckBox()) 
				connEtoC19(e);
			if (e.getSource() == SessionManagerView.this.getShowSessionInformationMenuCheckBox()) 
				connEtoC20(e);
			if (e.getSource() == SessionManagerView.this.getShowSessionToolBarMenuCheckBox()) 
				connEtoC21(e);
			if (e.getSource() == SessionManagerView.this.getLoadFileMenuItem()) 
				connEtoC27(e);
			if (e.getSource() == SessionManagerView.this.getNewSessionButton()) 
				connEtoC2(e);
			if (e.getSource() == SessionManagerView.this.getNewSessionMenuItem()) 
				connEtoC22(e);
			if (e.getSource() == SessionManagerView.this.getCloneSessionButton()) 
				connEtoC1(e);
			if (e.getSource() == SessionManagerView.this.getRemoveSessionButton()) 
				connEtoC3(e);
			if (e.getSource() == SessionManagerView.this.getActivateSessionButton()) 
				connEtoC23(e);
			if (e.getSource() == SessionManagerView.this.getClonSessionMenuItem()) 
				connEtoC24(e);
			if (e.getSource() == SessionManagerView.this.getRemoveSessionMenuItem()) 
				connEtoC25(e);
			if (e.getSource() == SessionManagerView.this.getActivateSessionMenuItem()) 
				connEtoC26(e);
			if (e.getSource() == SessionManagerView.this.getLoadFileButton()) 
				connEtoC28(e);
			if (e.getSource() == SessionManagerView.this.getSaveFileButton()) 
				connEtoC29(e);
			if (e.getSource() == SessionManagerView.this.getSaveFileMenuItem()) 
				connEtoC30(e);
			if (e.getSource() == SessionManagerView.this.getSaveAsFileButton()) 
				connEtoC31(e);
			if (e.getSource() == SessionManagerView.this.getSaveAsFileMenuItem()) 
				connEtoC32(e);
			if (e.getSource() == SessionManagerView.this.getNewFileButton()) 
				connEtoC33(e);
			if (e.getSource() == SessionManagerView.this.getNewFileMenuItem()) 
				connEtoC34(e);
			if (e.getSource() == SessionManagerView.this.getNewSessionPopupMenuItem()) 
				connEtoC35(e);
			if (e.getSource() == SessionManagerView.this.getCloneSessionPopupMenuItem()) 
				connEtoC36(e);
			if (e.getSource() == SessionManagerView.this.getRemoveSessionPopupMenuItem()) 
				connEtoC37(e);
			if (e.getSource() == SessionManagerView.this.getActivateSessionPopupMenuItem()) 
				connEtoC38(e);
			if (e.getSource() == SessionManagerView.this.getThreadInstanceBoundAddButton()) 
				connEtoC50(e);
			if (e.getSource() == SessionManagerView.this.getThreadInstanceBoundRemoveButton()) 
				connEtoC51(e);
			if (e.getSource() == SessionManagerView.this.getAddClasspathPopupMenuItem()) 
				connEtoC56(e);
			if (e.getSource() == SessionManagerView.this.getRemoveClasspathPopupMenuItem()) 
				connEtoC57(e);
			if (e.getSource() == SessionManagerView.this.getAddIncludePopupMenuItem()) 
				connEtoC60(e);
			if (e.getSource() == SessionManagerView.this.getRemoveIncludesPopupMenuItem()) 
				connEtoC61(e);
			if (e.getSource() == SessionManagerView.this.getAddQuantificationButton()) 
				connEtoC66(e);
			if (e.getSource() == SessionManagerView.this.getRemoveQuantificationButton()) 
				connEtoC67(e);
			if (e.getSource() == SessionManagerView.this.getPatternNameComboBox()) 
				connEtoC68(e);
			if (e.getSource() == SessionManagerView.this.getPatternScopeComboBox()) 
				connEtoC69(e);
			if (e.getSource() == SessionManagerView.this.getSavePropertyButton()) 
				connEtoC70(e);
			if (e.getSource() == SessionManagerView.this.getResetPropertyButton()) 
				connEtoC71(e);
			if (e.getSource() == SessionManagerView.this.getExpandPropertyButton()) 
				connEtoC72(e);
			if (e.getSource() == SessionManagerView.this.getSelectAllAssertionsButton()) 
				connEtoC73(e);
			if (e.getSource() == SessionManagerView.this.getUnselectedAllAssertionsButton()) 
				connEtoC74(e);
			if (e.getSource() == SessionManagerView.this.getVariableBoundRemoveButton()) 
				connEtoC76(e);
			if (e.getSource() == SessionManagerView.this.getVariableBoundAddButton()) 
				connEtoM1(e);
			if (e.getSource() == SessionManagerView.this.getAddVariableCancelButton()) 
				connEtoM2(e);
			if (e.getSource() == SessionManagerView.this.getAddVariableOkButton()) 
				connEtoC75(e);
			if (e.getSource() == SessionManagerView.this.getSessionInformationOkButton()) 
				connEtoM3(e);
			if (e.getSource() == SessionManagerView.this.getShowSessionInformationButton()) 
				connEtoC77(e);
			if (e.getSource() == SessionManagerView.this.getShowSessionInformationMenuItem()) 
				connEtoC78(e);
			if (e.getSource() == SessionManagerView.this.getInfoSessionPopupMenuItem()) 
				connEtoC79(e);
			if (e.getSource() == SessionManagerView.this.getShowHideSessionToolbarButton()) 
				connEtoC80(e);
			if (e.getSource() == SessionManagerView.this.getShowHideSessionInformationButton()) 
				connEtoC81(e);
			if (e.getSource() == SessionManagerView.this.getChooseFreeRadioButton()) 
				connEtoC64(e);
			if (e.getSource() == SessionManagerView.this.getResourceBoundedRadioButton()) 
				connEtoC65(e);
			if (e.getSource() == SessionManagerView.this.getExhaustiveRadioButton()) 
				connEtoC82();
		};
		public void focusGained(java.awt.event.FocusEvent e) {};
		public void focusLost(java.awt.event.FocusEvent e) {
			if (e.getSource() == SessionManagerView.this.getIntegerBoundMaxTextField()) 
				connEtoC45(e);
			if (e.getSource() == SessionManagerView.this.getIntegerBoundMinTextField()) 
				connEtoC46(e);
			if (e.getSource() == SessionManagerView.this.getArrayBoundMaxTextField()) 
				connEtoC47(e);
			if (e.getSource() == SessionManagerView.this.getInstanceBoundMaxTextField()) 
				connEtoC48(e);
			if (e.getSource() == SessionManagerView.this.getThreadInstanceBoundMaxTextField()) 
				connEtoC49(e);
			if (e.getSource() == SessionManagerView.this.getOutputNameTextField()) 
				connEtoC52(e);
			if (e.getSource() == SessionManagerView.this.getWorkingDirectoryTextField()) 
				connEtoC53(e);
			if (e.getSource() == SessionManagerView.this.getDescriptionTextArea()) 
				connEtoC54(e);
			if (e.getSource() == SessionManagerView.this.getMainClassFileTextField()) 
				connEtoC55(e);
		};
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getSource() == SessionManagerView.this.getJPFRadioButton()) 
				connEtoC41(e);
			if (e.getSource() == SessionManagerView.this.getSMVRadioButton()) 
				connEtoC42(e);
			if (e.getSource() == SessionManagerView.this.getDSpinRadioButton()) 
				connEtoC43(e);
			if (e.getSource() == SessionManagerView.this.getSpinRadioButton()) 
				connEtoC44(e);
		};
		public void mouseClicked(java.awt.event.MouseEvent e) {};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getSource() == SessionManagerView.this.getSessionNameList()) 
				connEtoC39(e);
			if (e.getSource() == SessionManagerView.this.getClasspathList()) 
				connEtoC58(e);
			if (e.getSource() == SessionManagerView.this.getIncludesList()) 
				connEtoC62(e);
		};
		public void mouseReleased(java.awt.event.MouseEvent e) {
			if (e.getSource() == SessionManagerView.this.getSessionNameList()) 
				connEtoC40(e);
			if (e.getSource() == SessionManagerView.this.getClasspathList()) 
				connEtoC59(e);
			if (e.getSource() == SessionManagerView.this.getIncludesList()) 
				connEtoC63(e);
		};
	};
/**
 * SessionManagerView constructor comment.
 */
public SessionManagerView() {
	super();
	initialize();
}
/**
 * SessionManagerView constructor comment.
 * @param title java.lang.String
 */
public SessionManagerView(String title) {
	super(title);
}
/**
 * When the abstraction check box is selected/deselected we need to update that
 * decision in the model.
 */
public void abstractionCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	boolean enabled = getAbstractionCheckBox().isSelected();
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
	    activeSession.setAbstractionEnabled(enabled);
	}
}
/**
 * When the mouse is pressed, check to see if this is a popup-menu request.
 * If so, show the popup-menu when it is appropriate.
 */
public void abstractionTree_MousePressed(java.awt.event.MouseEvent mouseEvent) {
	return;
}
/**
 * When the mouse is pressed, check to see if this is a popup-menu request.
 * If so, show the popup-menu when it is appropriate.
 */
public void abstractionTree_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
	return;
}
/**
 * Comment
 */
public void activateMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	activateSessionAction();
}
/**
 */
public void activateSessionAction() {

    //log.debug("Setting the active session ...");

    Object selectedValue = getSessionNameList().getSelectedValue();
    if(selectedValue != null) {
	String sessionID = selectedValue.toString();
	
	try {
	    sessionManager.setActiveSession(sessionID);
	}
	catch(edu.ksu.cis.bandera.sessions.SessionNotFoundException snfe) {
	    JOptionPane.showMessageDialog(this, "The session given, " + sessionID + ", was not found when trying to activate it.");
	    return;
	}
	catch(Exception e) {
	    JOptionPane.showMessageDialog(this, "The session given, " + sessionID +
					  ", caused an exception while setting it as active." +
					  "  Exception: " + e.toString());
	    return;
	}
	clearCompiledData();	
	updateView();
    }

    //log.debug("Done setting the active session.");
}
/**
 * Comment
 */
public void activateSessionButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	activateSessionAction();
}
/**
 * When the add quantification button is pressed, a new dialog window
 * should open to allow the user to input a variable name and select
 * the type for the variable name.  The types displayed should match
 * those types that are available in the active session's application.
 * If the user selects OK, the variable name and type should be add
 * to the table.  If the user selects CANCEL, nothing should be added.
 */
public void addQuantificationButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    if((classSet == null) || (classSet.size() <= 0)) {
	JOptionPane.showMessageDialog(this, "There are no classes defined at this time. You should run the compiler to fix this.",
				      "Error Adding Quantified Variable", JOptionPane.ERROR_MESSAGE);
	return;
    }

	JPanel panel = new JPanel();
	panel.setLayout(new java.awt.GridLayout(2, 2));
	JLabel label1 = new JLabel("Variable name");
	JLabel label2 = new JLabel("Variable type");
	JTextField textField = new JTextField("", 10);
	JComboBox comboBox = new JComboBox(classSet.toArray());
	panel.add(label1);
	panel.add(textField);
	panel.add(label2);
	panel.add(comboBox);

	int response = JOptionPane.showConfirmDialog(this, panel, "Add a quantified variable ...", JOptionPane.OK_CANCEL_OPTION);
	if(response == JOptionPane.OK_OPTION) {
		String variableName = textField.getText();
		String typeName = comboBox.getSelectedItem().toString();
		Vector rowData = new Vector(2);
		rowData.add(variableName);
		rowData.add(typeName);
		JTable qt = getQuantificationTable();
		javax.swing.table.TableModel tm = qt.getModel();
		if((tm != null) && (tm instanceof DefaultTableModel)) {
			DefaultTableModel dtm = (DefaultTableModel)tm;
			dtm.addRow(rowData);
		}	
	}
	
}
/**
 * When the ok button is pressed, we will take the node that is selected
 * in the tree, parse it to determine if it is a method local or a field,
 * get the min and max values, and put it into the resource bounds.
 */
public void addVariableOkButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	SessionManager sm = SessionManager.getInstance();
	Session activeSession = sm.getActiveSession();
	if(activeSession == null) {
		return;
	}
	BIROptions birOptions = activeSession.getBIROptions();
	if(birOptions == null) {
		birOptions = new BIROptions();
		activeSession.setBIROptions(birOptions);
	}
	ResourceBounds rbs = birOptions.getResourceBounds();
	if(rbs == null) {
		rbs = new ResourceBounds();
		birOptions.setResourceBounds(rbs);
	}

	int min = rbs.getDefaultIntMin();
	int max = rbs.getDefaultIntMax();
	JTextField minTextField = getAddVariableMinimumTextField();
	String minString = minTextField.getText();
	try {
		min = Integer.parseInt(minString);
	}
	catch(NumberFormatException nfe) {
	}
	JTextField maxTextField = getAddVariableMaximumTextField();
	String maxString = maxTextField.getText();
	try {
		max = Integer.parseInt(maxString);
	}
	catch(NumberFormatException nfe) {
	}

	JTree variableTree = getAddVariableTree();
	TreePath[] selectedPaths = variableTree.getSelectionPaths();
	if(selectedPaths != null) {
		for(int i = 0; i < selectedPaths.length; i++) {
			Object[] pathObjects = selectedPaths[i].getPath();
			Object lastObject = selectedPaths[i].getLastPathComponent();
			if(VariableTreeModelBuilder.isFieldNode(lastObject)) {
				String className = VariableTreeModelBuilder.getClassName(pathObjects[1]);
				String fieldName = VariableTreeModelBuilder.getFieldName(pathObjects[2]);
				//log.debug("Field selected: [" + className + "].[" + fieldName + "]");
				rbs.setFieldMax(className, fieldName, max);
				rbs.setFieldMin(className, fieldName, min);
			}
			else if(VariableTreeModelBuilder.isMethodLocalNode(lastObject)) {
				String className = VariableTreeModelBuilder.getClassName(pathObjects[1]);
				String methodName = VariableTreeModelBuilder.getMethodName(pathObjects[2]);
				String localName = VariableTreeModelBuilder.getMethodLocalName(pathObjects[3]);
				//log.debug("Local selected: [" + className + "].[" + methodName + "].[" + localName + "]");
				rbs.setMethodLocalMax(className, methodName, localName, max);
				rbs.setMethodLocalMin(className, methodName, localName, min);
			}
			else {
				log.debug("Not sure what is selected.");
			}
		}
		updateView();
	}
	getAddVariableDialog().setVisible(false);
}
/**
 * Comment
 */
public void arrayBoundMaxTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String arrayMaxString = getArrayBoundMaxTextField().getText();
	if((arrayMaxString != null) && (arrayMaxString.length() > 0)) {
		try {
			Integer arrayMax = Integer.valueOf(arrayMaxString);
			Session activeSession = sessionManager.getActiveSession();
			if(activeSession != null) {
				BIROptions birOptions = activeSession.getBIROptions();
				if(birOptions != null) {
					edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
					if(rbs != null) {
						rbs.setArrayMax(arrayMax.intValue());
					}
				}
			}
		}
		catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "The number given, " +
				arrayMaxString + ", is not a valid number.");
		}
	}
}
/**
 * When the choose free button is pressed, we need to select that
 * search mode in the BIROptions (which should be available in the
 * active session).
 */
public void chooseFreeRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	SessionManager sm = SessionManager.getInstance();
	Session activeSession = sm.getActiveSession();
	if(activeSession != null) {
		BIROptions birOptions = activeSession.getBIROptions();
		if(birOptions != null) {
			birOptions.setSearchMode(BIROptions.CHOOSE_FREE_MODE);
		}
	}
}
/**
 * Comment
 */
public void classpathAddButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	JFileChooser fc = getClasspathFileChooser();
	if(fc != null) {
		int response = fc.showOpenDialog(this);
		if(response == JFileChooser.APPROVE_OPTION) {
			java.io.File[] files = fc.getSelectedFiles();
			
			/* Used for testing/debugging -tcw
			if(files != null) {
			    log.debug("files.length = " + files.length);
				for(int i = 0; i < files.length; i++) {
					log.debug("files[" + i + "] = " + files[i]);
				}
			}
			*/
			
			if((files != null) && (files.length > 0)) {
				Session activeSession = sessionManager.getActiveSession();
				if(activeSession != null) {
					for(int i = 0; i < files.length; i++) {
						activeSession.addClasspathResource(files[i].toString());
					}
					updateView();
				}
			}
		}
	}
}
/**
 * Comment
 */
public void classpathList_MousePressed(java.awt.event.MouseEvent mouseEvent) {
	if(mouseEvent.isPopupTrigger()) {
		getClasspathListPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}
}
/**
 * Comment
 */
public void classpathList_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
	if(mouseEvent.isPopupTrigger()) {
		getClasspathListPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}
}
/**
 */
public void classpathRemoveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
	JList classpathList = getClasspathList();
	Object[] selectedValues = classpathList.getSelectedValues();
	if((selectedValues != null) && (selectedValues.length > 0)) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			for(int i = 0; i < selectedValues.length; i++) {
				activeSession.removeClasspathResource(selectedValues[i].toString());
			}
			updateView();
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/29/2002 11:28:31 AM)
 */
private void clearExpandedPropertyTextArea() {
	JTextArea ta = getExpandedPropertyTextArea();
	ta.setText("");
}
/**
 * Clear the values defined in the resource bounds tab.
 */
public void clearResourceBoundsTab() {
	
	getIntegerBoundMaxTextField().setText("");
	getIntegerBoundMinTextField().setText("");
	getArrayBoundMaxTextField().setText("");
	getInstanceBoundMaxTextField().setText("");
	// clear the instance bound table
	getThreadInstanceBoundMaxTextField().setText("");
	// clear the thread instance bound table
	
}
/**
 * This method will clear out the view to make sure it is empty in
 * preparation for setting values.
 */
public void clearView() {

	// don't clear the session list -tcw

	// clear the info tab
	getInfoTextArea().setText("");
	
	// clear the general tab
	getOutputNameTextField().setText("");
	getWorkingDirectoryTextField().setText("");
	getDescriptionTextArea().setText("");
	
	// clear the application tab
	getMainClassFileTextField().setText("");
	getClasspathList().setListData(new Object[0]);
	getIncludesList().setListData(new Object[0]);
	
	// clear the components tab
	getSlicerCheckBox().setSelected(false);
	getAbstractionCheckBox().setSelected(false);
	getModelCheckerCheckBox().setSelected(false);
	// can we clear the checker radio buttons? -tcw
	
	// clear the resource bounds tab
	clearResourceBoundsTab();

	// clear the specification tab
	clearSpecificationTab();
}

    /**
     * Clear the values that have been set in the specification tab.
     */
    private void clearSpecificationTab() {

	// clear assertions list
	JPanel assertionListPanel = getAssertionListPanel();
	assertionListPanel.removeAll();
	allAssertionSet.clear();
	    
	// clear quantification table
	Vector rowData = new Vector(0);
	Vector columnNames = new Vector(2);
	columnNames.add("Variable");
	columnNames.add("Type");
	JTable qt = getQuantificationTable();
	javax.swing.table.DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(rowData, columnNames);
	qt.setModel(dtm);	

	// reset the pattern selection
	// clear the pattern parameter text fields
	if(patternParameterTextFieldMap != null) {
	    Iterator pptfmi = patternParameterTextFieldMap.keySet().iterator();
	    while(pptfmi.hasNext()) {
		String parameterName = (String)pptfmi.next();
		JTextField textField = (JTextField)patternParameterTextFieldMap.get(parameterName);
		textField.setText("");
	    }
	}

	// clear the expansion text field
	JTextArea expandedPropertyTextArea = getExpandedPropertyTextArea();
	expandedPropertyTextArea.setText("");

    }
/**
 * clearCompiledData clears the classSet and allAssertionSet, and should be called 
 * whenever an action would deselect the current session in any way.
 * 
 */
public void clearCompiledData(){
	if(classSet != null)
		classSet.clear();
	if(allAssertionSet != null)
		allAssertionSet.clear();
}

/**
 * Comment
 */
public void cloneMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	cloneSessionAction();
}
/**
 * The cloneSessionAction will perform the actions needed to clone
 * a session.  If a session is selected in the list, that will be the
 * session selected.  If not, the first one in the list will be selected.
 * 
 */
public void cloneSessionAction() {
	
	Map sessionMap = sessionManager.getSessions();
	if((sessionMap != null) && (sessionMap.size() > 0)) {

		// generate a list of session IDs in the model
		String[] sessionIDs = new String[sessionMap.size()];
		int i = 0;
		Iterator sessionMapIterator = sessionMap.keySet().iterator();
		while(sessionMapIterator.hasNext()) {
			String sessionID = (String)sessionMapIterator.next();
			sessionIDs[i] = sessionID;
			i++;
		}

		// generate the components for the dialog
		JPanel cloneSessionPanel = new JPanel();
		cloneSessionPanel.setLayout(new java.awt.FlowLayout());
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new java.awt.FlowLayout());
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new java.awt.FlowLayout());
		JLabel sessionListLabel = new JLabel("Session to Clone");
		listPanel.add(sessionListLabel);
		JLabel sessionNameLabel = new JLabel("New Session Name");
		namePanel.add(sessionNameLabel);
		JComboBox sessionList = new JComboBox(sessionIDs);
		listPanel.add(sessionList);
		JTextField sessionNameTextField = new JTextField("NewSession", 20);
		namePanel.add(sessionNameTextField);
		cloneSessionPanel.add(listPanel);
		cloneSessionPanel.add(namePanel);

		// select the session that is selected in the session list
		Object selectedValue = getSessionNameList().getSelectedValue();
		if(selectedValue != null) {
			String selectedSessionID = selectedValue.toString();
			sessionList.setSelectedItem(selectedSessionID);
		}
	
		int response = JOptionPane.showConfirmDialog(this, cloneSessionPanel, "Clone a Session", JOptionPane.OK_CANCEL_OPTION);
		if(response == JOptionPane.OK_OPTION) {
			String sessionID = sessionList.getSelectedItem().toString();
			String newSessionID = sessionNameTextField.getText();
			Session newSession = sessionManager.getSession(newSessionID);
			if(newSession != null) {
				response = JOptionPane.showConfirmDialog(this, "A session with the name " + newSessionID + " exists already." +
					"\nWould you like to over-write it?", "Overwrite Existing Session?", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.NO_OPTION) {
					return;
				}
				else {
					sessionManager.removeSession(newSession);
				}
			}

			try {	
				Session session = sessionManager.getSession(sessionID);
				sessionManager.cloneSession(newSessionID, session);
				sessionManager.setActiveSession(newSessionID);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(this, "An exception occured while trying to clone a session: " + e.toString());
				return;
			}
			
			updateView();
		}
		
	}
	else {
		// show an error message -> There are no sessions to clone, you should create a new session instead.
		JOptionPane.showMessageDialog(this, "There are currently no sessions.  You should instead create a new session.");
	}
	
}
/**
 * When the OptionsView is complete, it will call this method.  This will then need to
 * grab the configured options and place it into the active session.
 *
 * @param object java.lang.Object This will be the OptionsView that completes.
 */
public void complete(Object object) {

    if(object == null) {
    }
    else if(object instanceof OptionsView) {

	// get the command line options from the view
	OptionsView optionsView = (OptionsView)object;
	Options options = optionsView.getOptions();
	String commandLineOptions = "";
	try {
	    commandLineOptions = options.getCommandLineOptions();
	}
	catch(Exception e) {
	    commandLineOptions = "";
	}
	
	// set those options in the active session (if it exists)
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
	    activeSession.setProperty(Session.CHECKER_OPTIONS_PROPERTY, commandLineOptions);
	}
    }
    else if(object instanceof ExpressionBuilder) {
	ExpressionBuilder eb = (ExpressionBuilder)object;
	List expressionList = eb.getExpressionList();
	// now change this into a String that has the params expanded.
	String expression = buildExpressionString(expressionList);
	//String expression = eb.getExpressionString();
	String parameterName = getCurrentExpressionBuilderParameter();
	JTextField textField = getParameterTextField(parameterName);
	textField.setText(expression);
	setCurrentExpressionBuilderParameter("");
    }
}

    private String buildExpressionString(List expressionList) {
	StringBuffer expressionBuffer = new StringBuffer();

	if((expressionList != null) && (expressionList.size() > 0)) {
	    for(int i = 0; i < expressionList.size(); i++) {
		Object o = expressionList.get(i);
		if(o == null) {
		    // do nothing with this
		}
		else if(o instanceof String) {
		    String s = (String)o;
		    expressionBuffer.append(s);
		}
		else if(o instanceof Predicate) {
		    Predicate p = (Predicate)o;
		    expressionBuffer.append(p.getName().toString());
		    expressionBuffer.append("(");
		    int paramCount = p.getNumOfParams();
		    // if the predicate is not static, add the 'this' reference using the
		    //  predicate's type
		    if(!(p.isStatic())) {
			expressionBuffer.append("<" + p.getType().toString() + ">");
			if(paramCount > 0) {
			    expressionBuffer.append(", ");
			}
		    }
		    for(int j = 0; j < paramCount; j++) {
			String currentType = p.getParamType(j).toString();
			expressionBuffer.append("<" + currentType + ">, ");
		    }

		    // delete the last two characters: a comma and a space
		    if(paramCount > 0) {
			int length = expressionBuffer.length();
			expressionBuffer.delete(length - 2, length);
		    }

		    expressionBuffer.append(") ");
		}
		else {
		    // do nothing with this
		}
	    }
	}

	return(expressionBuffer.toString());
    }

/**
 * connEtoC1:  (SessionRemoveButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.sessionRemoveButton_ActionPerformed1(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cloneSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (MainClassFileBrowseButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.mainClassFileBrowseButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC10(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.mainClassFileBrowseButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (ClasspathAddButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.classpathAddButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC11(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathAddButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (ClasspathRemoveButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.classpathRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathRemoveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (IncludesAddButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.includesAddButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.includesAddButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (IncludesRemoveButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.includesRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.includesRemoveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC15:  (WorkingDirectoryBrowseButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.workingDirectoryBrowseButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC15(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.workingDirectoryBrowseButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC16:  (ResetDefaultsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.resetDefaultsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC16(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.resetDefaultsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC17:  (SlicerCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.slicerCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC17(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.slicerCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC18:  (AbstractionCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.abstractionCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC18(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.abstractionCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC19:  (ModelCheckerCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.modelCheckerCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC19(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.modelCheckerCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (SessionAddButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.sessionAddButton_ActionPerformed1(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.newSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC20:  (ShowSessionInformationMenuCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.showSessionInformationMenuCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC20(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showSessionInformationMenuCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC21:  (ShowSessionToolBarMenuCheckBox.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.showSessionToolBarMenuCheckBox_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC21(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showSessionToolBarMenuCheckBox_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC22:  (NewMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.newMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC22(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.newSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC23:  (CloneMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.cloneMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC23(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.activateSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC24:  (RemoveMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.removeMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC24(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cloneSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC25:  (ActivateMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.activateMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC25(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.removeSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC26:  (ActivateSessionButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.activateSessionButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC26(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.activateSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC27:  (LoadMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.loadFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC27(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.loadFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC28:  (LoadButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.loadFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC28(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.loadFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC29:  (SaveFileButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.saveFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC29(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saveFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (SessionCloneButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.sessionCloneButton_ActionPerformed1(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.removeSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC30:  (SaveMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.saveFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC30(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saveFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC31:  (SaveAsFileButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.saveAsFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC31(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saveAsFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC32:  (SaveAsMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.saveAsFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC32(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saveAsFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC33:  (NewFileButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.newFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC33(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.newFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC34:  (NewFileMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.newFileAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC34(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.newFileAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC35:  (NewSessionPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.newSessionAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC35(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.newSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC36:  (CloneSessionPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.cloneSessionAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC36(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cloneSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC37:  (RemoveSessionPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.removeSessionAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC37(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.removeSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC38:  (ActivateSessionPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.activateSessionAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC38(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.activateSessionAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC39:  (SessionNameList.mouse.mousePressed(java.awt.event.MouseEvent) --> SessionManagerView.sessionNameList_MousePressed(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC39(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.sessionNameList_MousePressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (InstanceBoundAddButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.instanceBoundAddButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.instanceBoundAddButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC40:  (SessionNameList.mouse.mouseReleased(java.awt.event.MouseEvent) --> SessionManagerView.sessionNameList_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC40(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.sessionNameList_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC41:  (JPFRadioButton.item.itemStateChanged(java.awt.event.ItemEvent) --> SessionManagerView.jPFRadioButton_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC41(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jPFRadioButton_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC42:  (SMVRadioButton.item.itemStateChanged(java.awt.event.ItemEvent) --> SessionManagerView.sMVRadioButton_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC42(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.sMVRadioButton_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC43:  (DSpinRadioButton.item.itemStateChanged(java.awt.event.ItemEvent) --> SessionManagerView.dSpinRadioButton_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC43(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dSpinRadioButton_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC44:  (SpinRadioButton.item.itemStateChanged(java.awt.event.ItemEvent) --> SessionManagerView.spinRadioButton_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC44(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.spinRadioButton_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC45:  (IntegerBoundMaxTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.integerBoundMaxTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC45(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.integerBoundMaxTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC46:  (IntegerBoundMinTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.integerBoundMinTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC46(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.integerBoundMinTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC47:  (ArrayBoundMaxTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.arrayBoundMaxTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC47(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.arrayBoundMaxTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC48:  (InstanceBoundMaxTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.instanceBoundMaxTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC48(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.instanceBoundMaxTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC49:  (ThreadInstanceBoundMaxTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.threadInstanceBoundMaxTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC49(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.threadInstanceBoundMaxTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (InstanceBoundRemoveButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.instanceBoundRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.instanceBoundRemoveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC50:  (ThreadInstanceBoundAddButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.threadInstanceBoundAddButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC50(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.threadInstanceBoundAddButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC51:  (ThreadInstanceBoundRemoveButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.threadInstanceBoundRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC51(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.threadInstanceBoundRemoveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC52:  (OutputNameTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.outputNameTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC52(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.outputNameTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC53:  (WorkingDirectoryTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.workingDirectoryTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC53(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.workingDirectoryTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC54:  (DescriptionTextArea.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.descriptionTextArea_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC54(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.descriptionTextArea_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC55:  (MainClassFileTextField.focus.focusLost(java.awt.event.FocusEvent) --> SessionManagerView.mainClassFileTextField_FocusLost(Ljava.awt.event.FocusEvent;)V)
 * @param arg1 java.awt.event.FocusEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC55(java.awt.event.FocusEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.mainClassFileTextField_FocusLost(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC56:  (AddClasspathPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.classpathAddButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC56(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathAddButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC57:  (RemoveClasspathPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.classpathRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC57(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathRemoveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC58:  (ClasspathList.mouse.mousePressed(java.awt.event.MouseEvent) --> SessionManagerView.classpathList_MousePressed(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC58(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathList_MousePressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC59:  (ClasspathList.mouse.mouseReleased(java.awt.event.MouseEvent) --> SessionManagerView.classpathList_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC59(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.classpathList_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (SpinOptionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.spinOptionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.spinOptionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC60:  (AddIncludePopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.includesAddButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC60(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.includesAddButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC61:  (RemoveIncludesPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.includesRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC61(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.includesRemoveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC62:  (IncludesList.mouse.mousePressed(java.awt.event.MouseEvent) --> SessionManagerView.includesList_MousePressed(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC62(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.includesList_MousePressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC63:  (IncludesList.mouse.mouseReleased(java.awt.event.MouseEvent) --> SessionManagerView.includesList_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC63(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.includesList_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC64:  (ChooseFreeRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.chooseFreeRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC64(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.chooseFreeRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC65:  (ResourceBoundedRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.resourceBoundedRadioButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC65(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.resourceBoundedRadioButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC66:  (AddQuantificationButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.addQuantificationButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC66(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.addQuantificationButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC67:  (RemoveQuantificationButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.removeQuantificationButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC67(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.removeQuantificationButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC68:  (PatternNameComboBox.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.patternChangeAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC68(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.patternChangeAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC69:  (PatternScopeComboBox.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.patternChangeAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC69(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.patternChangeAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (DSpinOptionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.dSpinOptionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dSpinOptionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC70:  (SavePropertyButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.savePropertyButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC70(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.savePropertyButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC71:  (ResetPropertyButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.resetPropertyButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC71(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.resetPropertyButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC72:  (ExpandPropertyButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.expandPropertyButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC72(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.expandPropertyButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC73:  (SelectAllAssertionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.selectAllAssertionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC73(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.selectAllAssertionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC74:  (UnselectedAllAssertionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.unselectedAllAssertionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC74(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.unselectedAllAssertionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC75:  (AddVariableOkButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.addVariableOkButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC75(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.addVariableOkButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC76:  (VariableBoundRemoveButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.variableBoundRemoveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC76(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.variableBoundRemoveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC77:  (ShowSessionInformationButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.showSessionInformationAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC77(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showSessionInformationAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC78:  (ShowSessionInformationMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.showSessionInformationAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC78(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showSessionInformationAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC79:  (InfoSessionPopupMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.showSessionInformationAction()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC79(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showSessionInformationAction();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (SMVOptionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.sMVOptionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.sMVOptionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC80:  (ShowHideSessionToolbarButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.showHideSessionToolbarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC80(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showHideSessionToolbarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC81:  (ShowHideSessionInformationButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.showHideSessionInformationButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC81(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showHideSessionInformationButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC82:  (ExhaustiveRadioButton.action. --> SessionManagerView.exhaustiveRadioButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC82() {
	try {
		// user code begin {1}
		// user code end
		this.exhaustiveRadioButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (JPFOptionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.jPFOptionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jPFOptionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (ManageSessionButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.show()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddVariableDialog().show();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM2:  (ManageSessionButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerMinimizedView.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddVariableDialog().setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM3:  (SessionInformationOkButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionInformationDialog.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getSessionInformationDialog().setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM4:  (SessionInformationHideButton.action.actionPerformed(java.awt.event.ActionEvent) --> SessionManagerView.pack()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.pack();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (8/18/2002 8:48:24 PM)
 * @return javax.swing.JComboBox
 */
private JComboBox createAbstractionComboBox() {
	JComboBox cb = new JComboBox();
	cb.addItem("Abs1");
	cb.addItem("Abs2");
	cb.addItem("Abs3");
	return(cb);
}
/**
 * When the user updates the session description in the view, update
 * it in the model as well.
 */
public void descriptionTextArea_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String description = getDescriptionTextArea().getText();
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
		activeSession.setDescription(description);
	}
}
/**
 * When the DSpin options button is pressed, we need to show the DSpin options
 * configuration window.  Use the command line options (or checker options) stored
 * in the current active session as the initial values.
 */
public void dSpinOptionsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

    String checkerOptions = "";
    Session activeSession = sessionManager.getActiveSession();
    if (activeSession != null) {
        checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
    }

    OptionsView optionsView = OptionsFactory.createOptionsViewInstance("DSpin");
    Options options = OptionsFactory.createOptionsInstance("DSpin");
    options.init(checkerOptions);
    optionsView.init(options);
    optionsView.registerCompletionListener(this);
    optionsView.setVisible(true);

}
/**
 * When the selected state of the DSpin radio button changes,
 * we need to make sure that the enabled state of the DSpin Options button
 * matches: if DSpin is selected, the DSpin options button is enabled.
 * If DSpin is selected, we need to update the model as well.
 */
public void dSpinRadioButton_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	boolean enabled = getDSpinRadioButton().isSelected();
	getDSpinOptionsButton().setEnabled(enabled);
	if(enabled) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			activeSession.setProperty(Session.CHECKER_NAME_PROPERTY,
				Session.DSPIN_CHECKER_NAME_PROPERTY);
		}
	}
}
/**
 * Comment
 */
public void exhaustiveRadioButton_ActionEvents() {
	SessionManager sm = SessionManager.getInstance();
	Session activeSession = sm.getActiveSession();
	if(activeSession != null) {
		BIROptions birOptions = activeSession.getBIROptions();
		if(birOptions != null) {
			birOptions.setSearchMode(BIROptions.EXHAUSTIVE_MODE);
		}
	}
}
/**
 * When the expand button is pressed by the user, we need to take the
 * configured property (from the quantification table, pattern name combo
 * box, pattern scope combo box, and the parameter predicate text fields)
 * and expand it into the text field (uneditable).  If errors occur during
 * the expansion, place the error message in the expansion text area.
 */
public void expandPropertyButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
    Pattern pattern;
    JComboBox patternNameComboBox = getPatternNameComboBox();
    JComboBox patternScopeComboBox = getPatternScopeComboBox();
    Object selectedPatternNameObject = patternNameComboBox.getSelectedItem();
    Object selectedPatternScopeObject = patternScopeComboBox.getSelectedItem();
    String patternName = "";
    String patternScope = "";
    if (selectedPatternNameObject != null) {
        patternName = selectedPatternNameObject.toString();
    }
    if (selectedPatternScopeObject != null) {
        patternScope = selectedPatternScopeObject.toString();
    }
    pattern = PatternSaverLoader.getPattern(patternName, patternScope);

    // build the property using the pattern (by generating a hashtable from the text fields)
    Hashtable ht = new Hashtable();
    Vector parameters = pattern.getParametersOccurenceOrder();
    Iterator pi = parameters.iterator();
    while(pi.hasNext()) {
	    String parameterName = (String)pi.next();
	    JTextField textField = (JTextField)patternParameterTextFieldMap.get(parameterName);
	    if(textField == null) {
		    continue;
	    }
	    String expression = textField.getText();
	    ht.put(parameterName, expression);
    }
    String expandedProperty = pattern.expandFormat(ht);

    // build the quantification from the quantification table
    String quantification = "";
    JTable quantificationTable = getQuantificationTable();
    TableModel tm = quantificationTable.getModel();
    if(tm != null) {
	    StringBuffer qBuffer = new StringBuffer();
	    for(int i = 0; i < tm.getRowCount(); i++) {
		    String variableName = (String)tm.getValueAt(i, 0);
		    String typeName = (String)tm.getValueAt(i, 1);
		    qBuffer.append("forall[" + variableName + ": " + typeName + "].");
	    }
	    quantification = qBuffer.toString().substring(0, qBuffer.length() - 1);
    }
    
    JTextArea expandedPropertyTextArea = getExpandedPropertyTextArea();
    expandedPropertyTextArea.setText(quantification + expandedProperty);
    
}
/**
 * Return the AbstractionCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getAbstractionCheckBox() {
	if (ivjAbstractionCheckBox == null) {
		try {
			ivjAbstractionCheckBox = new javax.swing.JCheckBox();
			ivjAbstractionCheckBox.setName("AbstractionCheckBox");
			ivjAbstractionCheckBox.setText("Abstraction (SLABS)");
			ivjAbstractionCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAbstractionCheckBox;
}
/**
 * Insert the method's description here.
 * Creation date: (9/12/2002 4:25:05 PM)
 * @return javax.swing.tree.TreeModel
 */
private TreeModel getAbstractionTreeModel() {
	return abstractionTreeModel;
}
/**
 * Return the ActivateSessionButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getActivateSessionButton() {
	if (ivjActivateSessionButton == null) {
		try {
			ivjActivateSessionButton = new javax.swing.JButton();
			ivjActivateSessionButton.setName("ActivateSessionButton");
			ivjActivateSessionButton.setText("Activate");
			ivjActivateSessionButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjActivateSessionButton;
}
/**
 * Return the ActivateMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getActivateSessionMenuItem() {
	if (ivjActivateSessionMenuItem == null) {
		try {
			ivjActivateSessionMenuItem = new javax.swing.JMenuItem();
			ivjActivateSessionMenuItem.setName("ActivateSessionMenuItem");
			ivjActivateSessionMenuItem.setText("Activate");
			ivjActivateSessionMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjActivateSessionMenuItem;
}
/**
 * Return the ActivateSessionPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getActivateSessionPopupMenuItem() {
	if (ivjActivateSessionPopupMenuItem == null) {
		try {
			ivjActivateSessionPopupMenuItem = new javax.swing.JMenuItem();
			ivjActivateSessionPopupMenuItem.setName("ActivateSessionPopupMenuItem");
			ivjActivateSessionPopupMenuItem.setText("Activate");
			ivjActivateSessionPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjActivateSessionPopupMenuItem;
}
/**
 * Insert the method's description here.
 * Creation date: (10/17/2002 11:39:40 AM)
 * @return javax.swing.JTextField
 */
private javax.swing.JTextField getActiveParameterTextField() {
	return activeParameterTextField;
}
/**
 * Return the AddClasspathPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getAddClasspathPopupMenuItem() {
	if (ivjAddClasspathPopupMenuItem == null) {
		try {
			ivjAddClasspathPopupMenuItem = new javax.swing.JMenuItem();
			ivjAddClasspathPopupMenuItem.setName("AddClasspathPopupMenuItem");
			ivjAddClasspathPopupMenuItem.setText("Add");
			ivjAddClasspathPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddClasspathPopupMenuItem;
}
/**
 * Return the AddIncludePopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getAddIncludePopupMenuItem() {
	if (ivjAddIncludePopupMenuItem == null) {
		try {
			ivjAddIncludePopupMenuItem = new javax.swing.JMenuItem();
			ivjAddIncludePopupMenuItem.setName("AddIncludePopupMenuItem");
			ivjAddIncludePopupMenuItem.setText("Add");
			ivjAddIncludePopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddIncludePopupMenuItem;
}
/**
 * Return the AddQuantificationButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddQuantificationButton() {
	if (ivjAddQuantificationButton == null) {
		try {
			ivjAddQuantificationButton = new javax.swing.JButton();
			ivjAddQuantificationButton.setName("AddQuantificationButton");
			ivjAddQuantificationButton.setText("+");
			ivjAddQuantificationButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddQuantificationButton;
}
/**
 * Return the AddVariableButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getAddVariableButtonPanel() {
	if (ivjAddVariableButtonPanel == null) {
		try {
			ivjAddVariableButtonPanel = new javax.swing.JPanel();
			ivjAddVariableButtonPanel.setName("AddVariableButtonPanel");
			ivjAddVariableButtonPanel.setLayout(new java.awt.GridBagLayout());
			ivjAddVariableButtonPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsAddVariableOkButton = new java.awt.GridBagConstraints();
			constraintsAddVariableOkButton.gridx = 0; constraintsAddVariableOkButton.gridy = 0;
			constraintsAddVariableOkButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddVariableButtonPanel().add(getAddVariableOkButton(), constraintsAddVariableOkButton);

			java.awt.GridBagConstraints constraintsAddVariableCancelButton = new java.awt.GridBagConstraints();
			constraintsAddVariableCancelButton.gridx = 1; constraintsAddVariableCancelButton.gridy = 0;
			constraintsAddVariableCancelButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddVariableButtonPanel().add(getAddVariableCancelButton(), constraintsAddVariableCancelButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableButtonPanel;
}
/**
 * Return the AddVariableCancelButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddVariableCancelButton() {
	if (ivjAddVariableCancelButton == null) {
		try {
			ivjAddVariableCancelButton = new javax.swing.JButton();
			ivjAddVariableCancelButton.setName("AddVariableCancelButton");
			ivjAddVariableCancelButton.setText("Cancel");
			ivjAddVariableCancelButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableCancelButton;
}
/**
 * Return the AddVariableDialog property value.
 * @return javax.swing.JDialog
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JDialog getAddVariableDialog() {
	if (ivjAddVariableDialog == null) {
		try {
			ivjAddVariableDialog = new javax.swing.JDialog();
			ivjAddVariableDialog.setName("AddVariableDialog");
			ivjAddVariableDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			ivjAddVariableDialog.setBounds(850, 33, 530, 240);
			ivjAddVariableDialog.setTitle("Add a variable bound ...");
			getAddVariableDialog().setContentPane(getJDialogContentPane());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableDialog;
}
/**
 * Return the AddVariableMaximumLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getAddVariableMaximumLabel() {
	if (ivjAddVariableMaximumLabel == null) {
		try {
			ivjAddVariableMaximumLabel = new javax.swing.JLabel();
			ivjAddVariableMaximumLabel.setName("AddVariableMaximumLabel");
			ivjAddVariableMaximumLabel.setText("Maximum");
			ivjAddVariableMaximumLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableMaximumLabel;
}
/**
 * Return the AddVariableMaxiumTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getAddVariableMaximumTextField() {
	if (ivjAddVariableMaximumTextField == null) {
		try {
			ivjAddVariableMaximumTextField = new javax.swing.JTextField();
			ivjAddVariableMaximumTextField.setName("AddVariableMaximumTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableMaximumTextField;
}
/**
 * Return the AddVariableMinimumLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getAddVariableMinimumLabel() {
	if (ivjAddVariableMinimumLabel == null) {
		try {
			ivjAddVariableMinimumLabel = new javax.swing.JLabel();
			ivjAddVariableMinimumLabel.setName("AddVariableMinimumLabel");
			ivjAddVariableMinimumLabel.setText("Minimum");
			ivjAddVariableMinimumLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableMinimumLabel;
}
/**
 * Return the AddVariableMinimumTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getAddVariableMinimumTextField() {
	if (ivjAddVariableMinimumTextField == null) {
		try {
			ivjAddVariableMinimumTextField = new javax.swing.JTextField();
			ivjAddVariableMinimumTextField.setName("AddVariableMinimumTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableMinimumTextField;
}
/**
 * Return the AddVariableOkButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAddVariableOkButton() {
	if (ivjAddVariableOkButton == null) {
		try {
			ivjAddVariableOkButton = new javax.swing.JButton();
			ivjAddVariableOkButton.setName("AddVariableOkButton");
			ivjAddVariableOkButton.setText("OK");
			ivjAddVariableOkButton.setBackground(new java.awt.Color(204,204,255));
			ivjAddVariableOkButton.setMaximumSize(new java.awt.Dimension(73, 25));
			ivjAddVariableOkButton.setPreferredSize(new java.awt.Dimension(73, 25));
			ivjAddVariableOkButton.setMinimumSize(new java.awt.Dimension(73, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableOkButton;
}
/**
 * Return the AddVariableTextFieldPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getAddVariableTextFieldPanel() {
	if (ivjAddVariableTextFieldPanel == null) {
		try {
			ivjAddVariableTextFieldPanel = new javax.swing.JPanel();
			ivjAddVariableTextFieldPanel.setName("AddVariableTextFieldPanel");
			ivjAddVariableTextFieldPanel.setLayout(new java.awt.GridBagLayout());
			ivjAddVariableTextFieldPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsAddVariableMinimumLabel = new java.awt.GridBagConstraints();
			constraintsAddVariableMinimumLabel.gridx = 0; constraintsAddVariableMinimumLabel.gridy = 0;
			constraintsAddVariableMinimumLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddVariableTextFieldPanel().add(getAddVariableMinimumLabel(), constraintsAddVariableMinimumLabel);

			java.awt.GridBagConstraints constraintsAddVariableMaximumLabel = new java.awt.GridBagConstraints();
			constraintsAddVariableMaximumLabel.gridx = 0; constraintsAddVariableMaximumLabel.gridy = 1;
			constraintsAddVariableMaximumLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddVariableTextFieldPanel().add(getAddVariableMaximumLabel(), constraintsAddVariableMaximumLabel);

			java.awt.GridBagConstraints constraintsAddVariableMinimumTextField = new java.awt.GridBagConstraints();
			constraintsAddVariableMinimumTextField.gridx = 1; constraintsAddVariableMinimumTextField.gridy = 0;
			constraintsAddVariableMinimumTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAddVariableMinimumTextField.weightx = 1.0;
			constraintsAddVariableMinimumTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddVariableTextFieldPanel().add(getAddVariableMinimumTextField(), constraintsAddVariableMinimumTextField);

			java.awt.GridBagConstraints constraintsAddVariableMaximumTextField = new java.awt.GridBagConstraints();
			constraintsAddVariableMaximumTextField.gridx = 1; constraintsAddVariableMaximumTextField.gridy = 1;
			constraintsAddVariableMaximumTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAddVariableMaximumTextField.weightx = 1.0;
			constraintsAddVariableMaximumTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getAddVariableTextFieldPanel().add(getAddVariableMaximumTextField(), constraintsAddVariableMaximumTextField);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableTextFieldPanel;
}
/**
 * Return the AddVariableTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTree getAddVariableTree() {
	if (ivjAddVariableTree == null) {
		try {
			ivjAddVariableTree = new javax.swing.JTree();
			ivjAddVariableTree.setName("AddVariableTree");
			ivjAddVariableTree.setBounds(0, 0, 262, 204);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableTree;
}
/**
 * Return the AddVariableTreeScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getAddVariableTreeScrollPane() {
	if (ivjAddVariableTreeScrollPane == null) {
		try {
			ivjAddVariableTreeScrollPane = new javax.swing.JScrollPane();
			ivjAddVariableTreeScrollPane.setName("AddVariableTreeScrollPane");
			getAddVariableTreeScrollPane().setViewportView(getAddVariableTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddVariableTreeScrollPane;
}
/**
 * Return the ApplicationPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getApplicationPane() {
	if (ivjApplicationPane == null) {
		try {
			ivjApplicationPane = new javax.swing.JPanel();
			ivjApplicationPane.setName("ApplicationPane");
			ivjApplicationPane.setLayout(new java.awt.GridBagLayout());
			ivjApplicationPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsIncludesPanel = new java.awt.GridBagConstraints();
			constraintsIncludesPanel.gridx = 1; constraintsIncludesPanel.gridy = 1;
			constraintsIncludesPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsIncludesPanel.weightx = 0.5;
			constraintsIncludesPanel.weighty = 0.9;
			constraintsIncludesPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getApplicationPane().add(getIncludesPanel(), constraintsIncludesPanel);

			java.awt.GridBagConstraints constraintsClasspathPanel = new java.awt.GridBagConstraints();
			constraintsClasspathPanel.gridx = 0; constraintsClasspathPanel.gridy = 1;
			constraintsClasspathPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsClasspathPanel.weightx = 0.5;
			constraintsClasspathPanel.weighty = 0.9;
			constraintsClasspathPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getApplicationPane().add(getClasspathPanel(), constraintsClasspathPanel);

			java.awt.GridBagConstraints constraintsMainClassFilePanel = new java.awt.GridBagConstraints();
			constraintsMainClassFilePanel.gridx = 0; constraintsMainClassFilePanel.gridy = 0;
			constraintsMainClassFilePanel.gridwidth = 2;
			constraintsMainClassFilePanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsMainClassFilePanel.weightx = 1.0;
			constraintsMainClassFilePanel.weighty = 0.3;
			constraintsMainClassFilePanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getApplicationPane().add(getMainClassFilePanel(), constraintsMainClassFilePanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjApplicationPane;
}
/**
 * Return the ArrayBoundHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getArrayBoundHelpButton() {
	if (ivjArrayBoundHelpButton == null) {
		try {
			ivjArrayBoundHelpButton = new javax.swing.JButton();
			ivjArrayBoundHelpButton.setName("ArrayBoundHelpButton");
			ivjArrayBoundHelpButton.setText("?");
			ivjArrayBoundHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjArrayBoundHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjArrayBoundHelpButton;
}
/**
 * Return the ArrayBoundLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getArrayBoundLabel() {
	if (ivjArrayBoundLabel == null) {
		try {
			ivjArrayBoundLabel = new javax.swing.JLabel();
			ivjArrayBoundLabel.setName("ArrayBoundLabel");
			ivjArrayBoundLabel.setText("Array Bound");
			ivjArrayBoundLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjArrayBoundLabel;
}
/**
 * Return the ArrayBoundMaxTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getArrayBoundMaxTextField() {
	if (ivjArrayBoundMaxTextField == null) {
		try {
			ivjArrayBoundMaxTextField = new javax.swing.JTextField();
			ivjArrayBoundMaxTextField.setName("ArrayBoundMaxTextField");
			ivjArrayBoundMaxTextField.setText("<max>");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjArrayBoundMaxTextField;
}
/**
 * Get the assertion with the given name from the given specification.  If it
 * is not found in the current spec, add it and return the new Assertion.
 * 
 * @return edu.ksu.cis.bandera.sessions.Assertion
 * @param assertionName java.lang.String
 */
private Assertion getAssertion(Specification spec, String assertionName) {

	Assertion assertion = null;
		
    Set assertions = spec.getAssertions();
    if ((assertions != null) && (assertions.size() > 0)) {
        Iterator ai = assertions.iterator();
        while (ai.hasNext()) {
            Assertion currentAssertion = (Assertion) ai.next();
            if(currentAssertion.getName().equals(assertionName)) {
	            assertion = currentAssertion;
	            break;
            }
        }
    }

    if(assertion == null) {
	    assertion = new Assertion();
	    assertion.setName(assertionName);
	    assertion.setEnabled(false);
	    spec.addAssertion(assertion);
    }
    
    return(assertion);
}

/**
 * Get the assertion with the given name from out collection of assertions.  If it
 * is not found in the current store, add it and return the new Assertion.
 * 
 * @return edu.ksu.cis.bandera.sessions.Assertion An Assertion with the given name.
 * @param assertionName java.lang.String
 */
private Assertion getAssertion(String assertionName) {

    Assertion assertion = null;
		
    if ((allAssertionSet != null) && (allAssertionSet.size() > 0)) {
        Iterator aai = allAssertionSet.iterator();
        while (aai.hasNext()) {
            Assertion currentAssertion = (Assertion) aai.next();
            if(currentAssertion.getName().equals(assertionName)) {
	            assertion = currentAssertion;
	            break;
            }
        }
    }

    if(assertion == null) {
	    assertion = new Assertion();
	    assertion.setName(assertionName);
	    assertion.setEnabled(false);
	    allAssertionSet.add(assertion);
    }
    
    return(assertion);
}

/**
 * Get the check box associated with the assertion that has the given name.  If it
 * does not exist, create one and add it to our collection.
 *
 * @return javax.swing.JCheckBox
 * @param assertionName java.lang.String
 */
private JCheckBox getAssertionCheckBox(String assertionName) {

    JCheckBox checkBox = (JCheckBox) assertionCheckBoxMap.get(assertionName);
    if (checkBox == null) {
        checkBox = new JCheckBox(assertionName);
        checkBox.setSelected(false);
        checkBox.setActionCommand(assertionName);
        assertionCheckBoxMap.put(assertionName, checkBox);
        java.awt.event.ActionListener al = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
                String assertionName = ae.getActionCommand();
                toggleAssertion(assertionName);
            }
        };
        checkBox.addActionListener(al);
    }
    
    return (checkBox);
}
/**
 * Return the AssertionListPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getAssertionListPanel() {
	if (ivjAssertionListPanel == null) {
		try {
			ivjAssertionListPanel = new javax.swing.JPanel();
			ivjAssertionListPanel.setName("AssertionListPanel");
			//ivjAssertionListPanel.setPreferredSize(new java.awt.Dimension(95, 20));
			ivjAssertionListPanel.setLayout(new java.awt.GridBagLayout());
			ivjAssertionListPanel.setLocation(0, 0);
			//ivjAssertionListPanel.setMinimumSize(new java.awt.Dimension(95, 20));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionListPanel;
}
/**
 * Return the AssertionListScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getAssertionListScrollPane() {
	if (ivjAssertionListScrollPane == null) {
		try {
			ivjAssertionListScrollPane = new javax.swing.JScrollPane();
			ivjAssertionListScrollPane.setName("AssertionListScrollPane");
			ivjAssertionListScrollPane.setAutoscrolls(false);
			//ivjAssertionListScrollPane.setPreferredSize(new java.awt.Dimension(150, 22));
			//ivjAssertionListScrollPane.setMinimumSize(new java.awt.Dimension(150, 22));
			getAssertionListScrollPane().setViewportView(getAssertionListPanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAssertionListScrollPane;
}
/**
 * Return the AssertionPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getAssertionPanel() {
	if (ivjAssertionPanel == null) {
		try {
			ivjAssertionPanel = new javax.swing.JPanel();
			ivjAssertionPanel.setName("AssertionPanel");
			ivjAssertionPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Assertions"));
			ivjAssertionPanel.setLayout(new java.awt.GridBagLayout());
			ivjAssertionPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsAssertionListScrollPane = new java.awt.GridBagConstraints();
			constraintsAssertionListScrollPane.gridx = 0; constraintsAssertionListScrollPane.gridy = 0;
			constraintsAssertionListScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsAssertionListScrollPane.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsAssertionListScrollPane.weightx = 1.0;
			constraintsAssertionListScrollPane.weighty = 1.0;
			constraintsAssertionListScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getAssertionPanel().add(getAssertionListScrollPane(), constraintsAssertionListScrollPane);

			java.awt.GridBagConstraints constraintsSelectAllAssertionsButton = new java.awt.GridBagConstraints();
			constraintsSelectAllAssertionsButton.gridx = 0; constraintsSelectAllAssertionsButton.gridy = 1;
			constraintsSelectAllAssertionsButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getAssertionPanel().add(getSelectAllAssertionsButton(), constraintsSelectAllAssertionsButton);

			java.awt.GridBagConstraints constraintsUnselectedAllAssertionsButton = new java.awt.GridBagConstraints();
			constraintsUnselectedAllAssertionsButton.gridx = 0; constraintsUnselectedAllAssertionsButton.gridy = 2;
			constraintsUnselectedAllAssertionsButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getAssertionPanel().add(getUnselectedAllAssertionsButton(), constraintsUnselectedAllAssertionsButton);
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
 * Return the BIRArrayBoundPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBIRArrayBoundPanel() {
	if (ivjBIRArrayBoundPanel == null) {
		try {
			ivjBIRArrayBoundPanel = new javax.swing.JPanel();
			ivjBIRArrayBoundPanel.setName("BIRArrayBoundPanel");
			ivjBIRArrayBoundPanel.setPreferredSize(new java.awt.Dimension(139, 33));
			ivjBIRArrayBoundPanel.setLayout(new java.awt.GridBagLayout());
			ivjBIRArrayBoundPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsArrayBoundHelpButton = new java.awt.GridBagConstraints();
			constraintsArrayBoundHelpButton.gridx = 0; constraintsArrayBoundHelpButton.gridy = 0;
			constraintsArrayBoundHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRArrayBoundPanel().add(getArrayBoundHelpButton(), constraintsArrayBoundHelpButton);

			java.awt.GridBagConstraints constraintsArrayBoundLabel = new java.awt.GridBagConstraints();
			constraintsArrayBoundLabel.gridx = 1; constraintsArrayBoundLabel.gridy = 0;
			constraintsArrayBoundLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRArrayBoundPanel().add(getArrayBoundLabel(), constraintsArrayBoundLabel);

			java.awt.GridBagConstraints constraintsArrayBoundMaxTextField = new java.awt.GridBagConstraints();
			constraintsArrayBoundMaxTextField.gridx = 2; constraintsArrayBoundMaxTextField.gridy = 0;
			constraintsArrayBoundMaxTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsArrayBoundMaxTextField.weightx = 1.0;
			constraintsArrayBoundMaxTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRArrayBoundPanel().add(getArrayBoundMaxTextField(), constraintsArrayBoundMaxTextField);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBIRArrayBoundPanel;
}
/**
 * Return the BIRCheckerStrategyLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getBIRCheckerStrategyLabel() {
	if (ivjBIRCheckerStrategyLabel == null) {
		try {
			ivjBIRCheckerStrategyLabel = new javax.swing.JLabel();
			ivjBIRCheckerStrategyLabel.setName("BIRCheckerStrategyLabel");
			ivjBIRCheckerStrategyLabel.setText("Strategy");
			ivjBIRCheckerStrategyLabel.setBackground(new java.awt.Color(204,204,255));
			ivjBIRCheckerStrategyLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBIRCheckerStrategyLabel;
}
/**
 * Return the BIRIntArrayBoundPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBIRIntegerBoundPanel() {
	if (ivjBIRIntegerBoundPanel == null) {
		try {
			ivjBIRIntegerBoundPanel = new javax.swing.JPanel();
			ivjBIRIntegerBoundPanel.setName("BIRIntegerBoundPanel");
			ivjBIRIntegerBoundPanel.setPreferredSize(new java.awt.Dimension(138, 99));
			ivjBIRIntegerBoundPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Integer Range Bounds"));
			ivjBIRIntegerBoundPanel.setLayout(new java.awt.GridBagLayout());
			ivjBIRIntegerBoundPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsIntegerBoundHelpButton = new java.awt.GridBagConstraints();
			constraintsIntegerBoundHelpButton.gridx = 0; constraintsIntegerBoundHelpButton.gridy = 0;
			constraintsIntegerBoundHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getIntegerBoundHelpButton(), constraintsIntegerBoundHelpButton);

			java.awt.GridBagConstraints constraintsIntegerBoundLabel = new java.awt.GridBagConstraints();
			constraintsIntegerBoundLabel.gridx = 1; constraintsIntegerBoundLabel.gridy = 0;
			constraintsIntegerBoundLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getIntegerBoundLabel(), constraintsIntegerBoundLabel);

			java.awt.GridBagConstraints constraintsIntegerBoundMinTextField = new java.awt.GridBagConstraints();
			constraintsIntegerBoundMinTextField.gridx = 2; constraintsIntegerBoundMinTextField.gridy = 0;
			constraintsIntegerBoundMinTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsIntegerBoundMinTextField.weightx = 1.0;
			constraintsIntegerBoundMinTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getIntegerBoundMinTextField(), constraintsIntegerBoundMinTextField);

			java.awt.GridBagConstraints constraintsIntegerBoundMaxTextField = new java.awt.GridBagConstraints();
			constraintsIntegerBoundMaxTextField.gridx = 4; constraintsIntegerBoundMaxTextField.gridy = 0;
			constraintsIntegerBoundMaxTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsIntegerBoundMaxTextField.weightx = 1.0;
			constraintsIntegerBoundMaxTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getIntegerBoundMaxTextField(), constraintsIntegerBoundMaxTextField);

			java.awt.GridBagConstraints constraintsDotDotLabel = new java.awt.GridBagConstraints();
			constraintsDotDotLabel.gridx = 3; constraintsDotDotLabel.gridy = 0;
			constraintsDotDotLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getDotDotLabel(), constraintsDotDotLabel);

			java.awt.GridBagConstraints constraintsVariableBoundAddButton = new java.awt.GridBagConstraints();
			constraintsVariableBoundAddButton.gridx = 0; constraintsVariableBoundAddButton.gridy = 1;
			constraintsVariableBoundAddButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getVariableBoundAddButton(), constraintsVariableBoundAddButton);

			java.awt.GridBagConstraints constraintsVariableBoundRemoveButton = new java.awt.GridBagConstraints();
			constraintsVariableBoundRemoveButton.gridx = 0; constraintsVariableBoundRemoveButton.gridy = 2;
			constraintsVariableBoundRemoveButton.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsVariableBoundRemoveButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getVariableBoundRemoveButton(), constraintsVariableBoundRemoveButton);

			java.awt.GridBagConstraints constraintsVariableBoundTableScrollPane = new java.awt.GridBagConstraints();
			constraintsVariableBoundTableScrollPane.gridx = 1; constraintsVariableBoundTableScrollPane.gridy = 1;
			constraintsVariableBoundTableScrollPane.gridwidth = 4;
constraintsVariableBoundTableScrollPane.gridheight = 2;
			constraintsVariableBoundTableScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsVariableBoundTableScrollPane.weightx = 1.0;
			constraintsVariableBoundTableScrollPane.weighty = 1.0;
			constraintsVariableBoundTableScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRIntegerBoundPanel().add(getVariableBoundTableScrollPane(), constraintsVariableBoundTableScrollPane);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBIRIntegerBoundPanel;
}
/**
 * Return the BIRModelCheckerPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBIRModelCheckerPanel() {
	if (ivjBIRModelCheckerPanel == null) {
		try {
			ivjBIRModelCheckerPanel = new javax.swing.JPanel();
			ivjBIRModelCheckerPanel.setName("BIRModelCheckerPanel");
			ivjBIRModelCheckerPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "BIR-Based"));
			ivjBIRModelCheckerPanel.setLayout(new java.awt.GridBagLayout());
			ivjBIRModelCheckerPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsCheckerNameLabel = new java.awt.GridBagConstraints();
			constraintsCheckerNameLabel.gridx = 0; constraintsCheckerNameLabel.gridy = 0;
			constraintsCheckerNameLabel.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsCheckerNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRModelCheckerPanel().add(getCheckerNameLabel(), constraintsCheckerNameLabel);

			java.awt.GridBagConstraints constraintsSpinCheckerPanel = new java.awt.GridBagConstraints();
			constraintsSpinCheckerPanel.gridx = 0; constraintsSpinCheckerPanel.gridy = 1;
			constraintsSpinCheckerPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSpinCheckerPanel.weightx = 1.0;
			constraintsSpinCheckerPanel.weighty = 1.0;
			constraintsSpinCheckerPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRModelCheckerPanel().add(getSpinCheckerPanel(), constraintsSpinCheckerPanel);

			java.awt.GridBagConstraints constraintsDSpinCheckerPanel = new java.awt.GridBagConstraints();
			constraintsDSpinCheckerPanel.gridx = 0; constraintsDSpinCheckerPanel.gridy = 2;
			constraintsDSpinCheckerPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsDSpinCheckerPanel.weightx = 1.0;
			constraintsDSpinCheckerPanel.weighty = 1.0;
			constraintsDSpinCheckerPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRModelCheckerPanel().add(getDSpinCheckerPanel(), constraintsDSpinCheckerPanel);

			java.awt.GridBagConstraints constraintsSMVCheckerPanel = new java.awt.GridBagConstraints();
			constraintsSMVCheckerPanel.gridx = 0; constraintsSMVCheckerPanel.gridy = 3;
			constraintsSMVCheckerPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSMVCheckerPanel.weightx = 1.0;
			constraintsSMVCheckerPanel.weighty = 1.0;
			constraintsSMVCheckerPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRModelCheckerPanel().add(getSMVCheckerPanel(), constraintsSMVCheckerPanel);

			java.awt.GridBagConstraints constraintsBIRCheckerStrategyLabel = new java.awt.GridBagConstraints();
			constraintsBIRCheckerStrategyLabel.gridx = 1; constraintsBIRCheckerStrategyLabel.gridy = 0;
			constraintsBIRCheckerStrategyLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsBIRCheckerStrategyLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRModelCheckerPanel().add(getBIRCheckerStrategyLabel(), constraintsBIRCheckerStrategyLabel);

			java.awt.GridBagConstraints constraintsBIRSearchModePanel = new java.awt.GridBagConstraints();
			constraintsBIRSearchModePanel.gridx = 1; constraintsBIRSearchModePanel.gridy = 1;
			constraintsBIRSearchModePanel.gridheight = 3;
			constraintsBIRSearchModePanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsBIRSearchModePanel.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBIRSearchModePanel.weightx = 1.0;
			constraintsBIRSearchModePanel.weighty = 1.0;
			constraintsBIRSearchModePanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRModelCheckerPanel().add(getBIRSearchModePanel(), constraintsBIRSearchModePanel);
			
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBIRModelCheckerPanel;
}
/**
 * Insert the method's description here.
 * Creation date: (8/15/2002 2:25:32 PM)
 * @return javax.swing.ButtonGroup
 */
private javax.swing.ButtonGroup getBirSearchModeButtonGroup() {
	if(birSearchModeButtonGroup == null) {
		birSearchModeButtonGroup = new ButtonGroup();
	}
	return birSearchModeButtonGroup;
}
/**
 * Return the BIRSearchModePanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBIRSearchModePanel() {
	if (ivjBIRSearchModePanel == null) {
		try {
			ivjBIRSearchModePanel = new javax.swing.JPanel();
			ivjBIRSearchModePanel.setName("BIRSearchModePanel");
			ivjBIRSearchModePanel.setLayout(new java.awt.GridBagLayout());
			ivjBIRSearchModePanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsChooseFreeRadioButton = new java.awt.GridBagConstraints();
			constraintsChooseFreeRadioButton.gridx = 0; constraintsChooseFreeRadioButton.gridy = 0;
			constraintsChooseFreeRadioButton.fill = java.awt.GridBagConstraints.BOTH;
			constraintsChooseFreeRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsChooseFreeRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRSearchModePanel().add(getChooseFreeRadioButton(), constraintsChooseFreeRadioButton);

			java.awt.GridBagConstraints constraintsResourceBoundedRadioButton = new java.awt.GridBagConstraints();
			constraintsResourceBoundedRadioButton.gridx = 0; constraintsResourceBoundedRadioButton.gridy = 1;
			constraintsResourceBoundedRadioButton.fill = java.awt.GridBagConstraints.BOTH;
			constraintsResourceBoundedRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsResourceBoundedRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRSearchModePanel().add(getResourceBoundedRadioButton(), constraintsResourceBoundedRadioButton);

			java.awt.GridBagConstraints constraintsExhaustiveRadioButton = new java.awt.GridBagConstraints();
			constraintsExhaustiveRadioButton.gridx = 0; constraintsExhaustiveRadioButton.gridy = 2;
			constraintsExhaustiveRadioButton.fill = java.awt.GridBagConstraints.BOTH;
			constraintsExhaustiveRadioButton.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsExhaustiveRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBIRSearchModePanel().add(getExhaustiveRadioButton(), constraintsExhaustiveRadioButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBIRSearchModePanel;
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
			ivjButtonPanel.setMaximumSize(new java.awt.Dimension(150, 100000));

			java.awt.GridBagConstraints constraintsSessionButtonPanel = new java.awt.GridBagConstraints();
			constraintsSessionButtonPanel.gridx = 0; constraintsSessionButtonPanel.gridy = 1;
			constraintsSessionButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSessionButtonPanel.weightx = 1.0;
			constraintsSessionButtonPanel.weighty = 1.0;
			constraintsSessionButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getSessionButtonPanel(), constraintsSessionButtonPanel);

			java.awt.GridBagConstraints constraintsFileButtonPanel = new java.awt.GridBagConstraints();
			constraintsFileButtonPanel.gridx = 0; constraintsFileButtonPanel.gridy = 0;
			constraintsFileButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsFileButtonPanel.weightx = 1.0;
			constraintsFileButtonPanel.weighty = 1.0;
			constraintsFileButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getFileButtonPanel(), constraintsFileButtonPanel);
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
 * Return the CheckerNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getCheckerNameLabel() {
	if (ivjCheckerNameLabel == null) {
		try {
			ivjCheckerNameLabel = new javax.swing.JLabel();
			ivjCheckerNameLabel.setName("CheckerNameLabel");
			ivjCheckerNameLabel.setText("Model Checker");
			ivjCheckerNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckerNameLabel;
}
/**
 * Return the CheckerNameLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getCheckerNameLabel1() {
	if (ivjCheckerNameLabel1 == null) {
		try {
			ivjCheckerNameLabel1 = new javax.swing.JLabel();
			ivjCheckerNameLabel1.setName("CheckerNameLabel1");
			ivjCheckerNameLabel1.setText("Model Checker");
			ivjCheckerNameLabel1.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckerNameLabel1;
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
			ivjChooseFreeRadioButton.setText("Choose Free");
			ivjChooseFreeRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjChooseFreeRadioButton.setEnabled(true);
			// user code begin {1}
			getBirSearchModeButtonGroup().add(ivjChooseFreeRadioButton);
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
 * Return the ClasspathAddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClasspathAddButton() {
	if (ivjClasspathAddButton == null) {
		try {
			ivjClasspathAddButton = new javax.swing.JButton();
			ivjClasspathAddButton.setName("ClasspathAddButton");
			ivjClasspathAddButton.setText("+");
			ivjClasspathAddButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathAddButton;
}
/**
 */
private javax.swing.JFileChooser getClasspathFileChooser() {
	
	// lazy init
	if(classpathFileChooser == null) {
		classpathFileChooser = new JFileChooser();
		classpathFileChooser.setFileFilter(new ClasspathFileFilter());
		classpathFileChooser.setMultiSelectionEnabled(true);
		classpathFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		classpathFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	}

	// use the last directory as this one's working directory
	if((lastFileChooser != null) && (lastFileChooser != classpathFileChooser)) {
		java.io.File currentDirectory = lastFileChooser.getCurrentDirectory();
		classpathFileChooser.setCurrentDirectory(currentDirectory);
	}

	lastFileChooser = classpathFileChooser;
	
	return(classpathFileChooser);
}
/**
 * Return the ClasspathHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClasspathHelpButton() {
	if (ivjClasspathHelpButton == null) {
		try {
			ivjClasspathHelpButton = new javax.swing.JButton();
			ivjClasspathHelpButton.setName("ClasspathHelpButton");
			ivjClasspathHelpButton.setText("?");
			ivjClasspathHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjClasspathHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathHelpButton;
}
/**
 * Return the ClasspathList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getClasspathList() {
	if (ivjClasspathList == null) {
		try {
			ivjClasspathList = new javax.swing.JList();
			ivjClasspathList.setName("ClasspathList");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathList;
}
/**
 * Return the ClasspathListPopupMenu property value.
 * @return javax.swing.JPopupMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPopupMenu getClasspathListPopupMenu() {
	if (ivjClasspathListPopupMenu == null) {
		try {
			ivjClasspathListPopupMenu = new javax.swing.JPopupMenu();
			ivjClasspathListPopupMenu.setName("ClasspathListPopupMenu");
			ivjClasspathListPopupMenu.setBackground(new java.awt.Color(204,204,255));
			ivjClasspathListPopupMenu.add(getAddClasspathPopupMenuItem());
			ivjClasspathListPopupMenu.add(getRemoveClasspathPopupMenuItem());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathListPopupMenu;
}
/**
 * Return the ClasspathPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getClasspathPanel() {
	if (ivjClasspathPanel == null) {
		try {
			ivjClasspathPanel = new javax.swing.JPanel();
			ivjClasspathPanel.setName("ClasspathPanel");
			ivjClasspathPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Classpath"));
			ivjClasspathPanel.setLayout(new java.awt.GridBagLayout());
			ivjClasspathPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsClasspathList = new java.awt.GridBagConstraints();
			constraintsClasspathList.gridx = 0; constraintsClasspathList.gridy = 0;
			constraintsClasspathList.gridheight = 3;
			constraintsClasspathList.fill = java.awt.GridBagConstraints.BOTH;
			constraintsClasspathList.weightx = 1.0;
			constraintsClasspathList.weighty = 1.0;
			constraintsClasspathList.insets = new java.awt.Insets(4, 4, 4, 4);
			getClasspathPanel().add(getClasspathList(), constraintsClasspathList);

			java.awt.GridBagConstraints constraintsClasspathAddButton = new java.awt.GridBagConstraints();
			constraintsClasspathAddButton.gridx = 1; constraintsClasspathAddButton.gridy = 0;
			constraintsClasspathAddButton.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsClasspathAddButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getClasspathPanel().add(getClasspathAddButton(), constraintsClasspathAddButton);

			java.awt.GridBagConstraints constraintsClasspathRemoveButton = new java.awt.GridBagConstraints();
			constraintsClasspathRemoveButton.gridx = 1; constraintsClasspathRemoveButton.gridy = 1;
			constraintsClasspathRemoveButton.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsClasspathRemoveButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getClasspathPanel().add(getClasspathRemoveButton(), constraintsClasspathRemoveButton);

			java.awt.GridBagConstraints constraintsClasspathHelpButton = new java.awt.GridBagConstraints();
			constraintsClasspathHelpButton.gridx = 1; constraintsClasspathHelpButton.gridy = 2;
			constraintsClasspathHelpButton.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsClasspathHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getClasspathPanel().add(getClasspathHelpButton(), constraintsClasspathHelpButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathPanel;
}
/**
 * Return the ClasspathRemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClasspathRemoveButton() {
	if (ivjClasspathRemoveButton == null) {
		try {
			ivjClasspathRemoveButton = new javax.swing.JButton();
			ivjClasspathRemoveButton.setName("ClasspathRemoveButton");
			ivjClasspathRemoveButton.setText("-");
			ivjClasspathRemoveButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClasspathRemoveButton;
}
/**
 * Insert the method's description here.
 * Creation date: (9/12/2002 4:25:49 PM)
 * @return java.util.Set
 */
public java.util.Set getClassSet() {
	return classSet;
}
/**
 * Return the SessionCloneButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCloneSessionButton() {
	if (ivjCloneSessionButton == null) {
		try {
			ivjCloneSessionButton = new javax.swing.JButton();
			ivjCloneSessionButton.setName("CloneSessionButton");
			ivjCloneSessionButton.setText("Clone");
			ivjCloneSessionButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCloneSessionButton;
}
/**
 * Return the CloneSessionPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getCloneSessionPopupMenuItem() {
	if (ivjCloneSessionPopupMenuItem == null) {
		try {
			ivjCloneSessionPopupMenuItem = new javax.swing.JMenuItem();
			ivjCloneSessionPopupMenuItem.setName("CloneSessionPopupMenuItem");
			ivjCloneSessionPopupMenuItem.setText("Clone");
			ivjCloneSessionPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCloneSessionPopupMenuItem;
}
/**
 * Return the CloneMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getClonSessionMenuItem() {
	if (ivjClonSessionMenuItem == null) {
		try {
			ivjClonSessionMenuItem = new javax.swing.JMenuItem();
			ivjClonSessionMenuItem.setName("ClonSessionMenuItem");
			ivjClonSessionMenuItem.setText("Clone");
			ivjClonSessionMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClonSessionMenuItem;
}
/**
 * Return the ComponentsPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getComponentsPane() {
	if (ivjComponentsPane == null) {
		try {
			ivjComponentsPane = new javax.swing.JPanel();
			ivjComponentsPane.setName("ComponentsPane");
			ivjComponentsPane.setLayout(new java.awt.GridBagLayout());
			ivjComponentsPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsComponentsPanel = new java.awt.GridBagConstraints();
			constraintsComponentsPanel.gridx = 0; constraintsComponentsPanel.gridy = 0;
			constraintsComponentsPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsComponentsPanel.weightx = 1.0;
			constraintsComponentsPanel.weighty = 1.0;
			constraintsComponentsPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getComponentsPane().add(getComponentsPanel(), constraintsComponentsPanel);

			java.awt.GridBagConstraints constraintsModelCheckerOptionsPanel = new java.awt.GridBagConstraints();
			constraintsModelCheckerOptionsPanel.gridx = 0; constraintsModelCheckerOptionsPanel.gridy = 1;
			constraintsModelCheckerOptionsPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsModelCheckerOptionsPanel.weightx = 1.0;
			constraintsModelCheckerOptionsPanel.weighty = 1.0;
			constraintsModelCheckerOptionsPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getComponentsPane().add(getModelCheckerOptionsPanel(), constraintsModelCheckerOptionsPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjComponentsPane;
}
/**
 * Return the ComponentsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getComponentsPanel() {
	if (ivjComponentsPanel == null) {
		try {
			ivjComponentsPanel = new javax.swing.JPanel();
			ivjComponentsPanel.setName("ComponentsPanel");
			ivjComponentsPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Enabled Components"));
			ivjComponentsPanel.setLayout(new java.awt.GridBagLayout());
			ivjComponentsPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSlicerCheckBox = new java.awt.GridBagConstraints();
			constraintsSlicerCheckBox.gridx = 0; constraintsSlicerCheckBox.gridy = 0;
			constraintsSlicerCheckBox.insets = new java.awt.Insets(4, 10, 4, 10);
			getComponentsPanel().add(getSlicerCheckBox(), constraintsSlicerCheckBox);

			java.awt.GridBagConstraints constraintsAbstractionCheckBox = new java.awt.GridBagConstraints();
			constraintsAbstractionCheckBox.gridx = 1; constraintsAbstractionCheckBox.gridy = 0;
			constraintsAbstractionCheckBox.insets = new java.awt.Insets(4, 10, 4, 10);
			getComponentsPanel().add(getAbstractionCheckBox(), constraintsAbstractionCheckBox);

			java.awt.GridBagConstraints constraintsModelCheckerCheckBox = new java.awt.GridBagConstraints();
			constraintsModelCheckerCheckBox.gridx = 2; constraintsModelCheckerCheckBox.gridy = 0;
			constraintsModelCheckerCheckBox.insets = new java.awt.Insets(4, 10, 4, 10);
			getComponentsPanel().add(getModelCheckerCheckBox(), constraintsModelCheckerCheckBox);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjComponentsPanel;
}
/**
 * Return the DescriptionHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getDescriptionHelpButton() {
	if (ivjDescriptionHelpButton == null) {
		try {
			ivjDescriptionHelpButton = new javax.swing.JButton();
			ivjDescriptionHelpButton.setName("DescriptionHelpButton");
			ivjDescriptionHelpButton.setText("?");
			ivjDescriptionHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjDescriptionHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDescriptionHelpButton;
}
/**
 * Return the DescriptionLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDescriptionLabel() {
	if (ivjDescriptionLabel == null) {
		try {
			ivjDescriptionLabel = new javax.swing.JLabel();
			ivjDescriptionLabel.setName("DescriptionLabel");
			ivjDescriptionLabel.setText("Description");
			ivjDescriptionLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDescriptionLabel;
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
			ivjDescriptionTextArea.setLineWrap(true);
			ivjDescriptionTextArea.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
			ivjDescriptionTextArea.setWrapStyleWord(true);
			ivjDescriptionTextArea.setText("<description>");
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
 */
private javax.swing.JFileChooser getDirectoryFileChooser() {
	
	// lazy init
	if(directoryFileChooser == null) {
		directoryFileChooser = new JFileChooser();
		directoryFileChooser.setMultiSelectionEnabled(true);
		directoryFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		directoryFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	}

	// use the last directory as this one's working directory
	if((lastFileChooser != null) && (lastFileChooser != directoryFileChooser)) {
		java.io.File currentDirectory = lastFileChooser.getCurrentDirectory();
		directoryFileChooser.setCurrentDirectory(currentDirectory);
	}

	lastFileChooser = directoryFileChooser;
	
	return(directoryFileChooser);
}
/**
 * Return the DotDotLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDotDotLabel() {
	if (ivjDotDotLabel == null) {
		try {
			ivjDotDotLabel = new javax.swing.JLabel();
			ivjDotDotLabel.setName("DotDotLabel");
			ivjDotDotLabel.setText("...");
			ivjDotDotLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDotDotLabel;
}
/**
 * Return the DSpinCheckerPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getDSpinCheckerPanel() {
	if (ivjDSpinCheckerPanel == null) {
		try {
			ivjDSpinCheckerPanel = new javax.swing.JPanel();
			ivjDSpinCheckerPanel.setName("DSpinCheckerPanel");
			ivjDSpinCheckerPanel.setLayout(new java.awt.GridBagLayout());
			ivjDSpinCheckerPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsDSpinRadioButton = new java.awt.GridBagConstraints();
			constraintsDSpinRadioButton.gridx = 0; constraintsDSpinRadioButton.gridy = 0;
			constraintsDSpinRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDSpinCheckerPanel().add(getDSpinRadioButton(), constraintsDSpinRadioButton);

			java.awt.GridBagConstraints constraintsDSpinOptionsButton = new java.awt.GridBagConstraints();
			constraintsDSpinOptionsButton.gridx = 1; constraintsDSpinOptionsButton.gridy = 0;
			constraintsDSpinOptionsButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDSpinCheckerPanel().add(getDSpinOptionsButton(), constraintsDSpinOptionsButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDSpinCheckerPanel;
}
/**
 * Return the DSpinOptionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getDSpinOptionsButton() {
	if (ivjDSpinOptionsButton == null) {
		try {
			ivjDSpinOptionsButton = new javax.swing.JButton();
			ivjDSpinOptionsButton.setName("DSpinOptionsButton");
			ivjDSpinOptionsButton.setText("Options");
			ivjDSpinOptionsButton.setBackground(new java.awt.Color(204,204,255));
			ivjDSpinOptionsButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDSpinOptionsButton;
}
/**
 * Return the DSpinRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getDSpinRadioButton() {
	if (ivjDSpinRadioButton == null) {
		try {
			ivjDSpinRadioButton = new javax.swing.JRadioButton();
			ivjDSpinRadioButton.setName("DSpinRadioButton");
			ivjDSpinRadioButton.setText("DSpin");
			ivjDSpinRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			getModelCheckerButtonGroup().add(ivjDSpinRadioButton);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDSpinRadioButton;
}
/**
 * Return the ExhaustiveRadioButton property value.
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
			ivjExhaustiveRadioButton.setEnabled(true);
			// user code begin {1}
			getBirSearchModeButtonGroup().add(ivjExhaustiveRadioButton);
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
 * Return the ExpandedPropertyPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getExpandedPropertyPanel() {
	if (ivjExpandedPropertyPanel == null) {
		try {
			ivjExpandedPropertyPanel = new javax.swing.JPanel();
			ivjExpandedPropertyPanel.setName("ExpandedPropertyPanel");
			ivjExpandedPropertyPanel.setLayout(new java.awt.GridBagLayout());
			ivjExpandedPropertyPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsExpandPropertyButton = new java.awt.GridBagConstraints();
			constraintsExpandPropertyButton.gridx = 0; constraintsExpandPropertyButton.gridy = 0;
			constraintsExpandPropertyButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsExpandPropertyButton.weightx = 0.3;
			constraintsExpandPropertyButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getExpandedPropertyPanel().add(getExpandPropertyButton(), constraintsExpandPropertyButton);

			java.awt.GridBagConstraints constraintsExpandedPropertyTextAreaScrollPane = new java.awt.GridBagConstraints();
			constraintsExpandedPropertyTextAreaScrollPane.gridx = 0; constraintsExpandedPropertyTextAreaScrollPane.gridy = 1;
			constraintsExpandedPropertyTextAreaScrollPane.gridwidth = 3;
			constraintsExpandedPropertyTextAreaScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsExpandedPropertyTextAreaScrollPane.weightx = 1.0;
			constraintsExpandedPropertyTextAreaScrollPane.weighty = 1.0;
			constraintsExpandedPropertyTextAreaScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getExpandedPropertyPanel().add(getExpandedPropertyTextAreaScrollPane(), constraintsExpandedPropertyTextAreaScrollPane);

			java.awt.GridBagConstraints constraintsSavePropertyButton = new java.awt.GridBagConstraints();
			constraintsSavePropertyButton.gridx = 1; constraintsSavePropertyButton.gridy = 0;
			constraintsSavePropertyButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSavePropertyButton.weightx = 0.3;
			constraintsSavePropertyButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getExpandedPropertyPanel().add(getSavePropertyButton(), constraintsSavePropertyButton);

			java.awt.GridBagConstraints constraintsResetPropertyButton = new java.awt.GridBagConstraints();
			constraintsResetPropertyButton.gridx = 2; constraintsResetPropertyButton.gridy = 0;
			constraintsResetPropertyButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsResetPropertyButton.weightx = 0.3;
			constraintsResetPropertyButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getExpandedPropertyPanel().add(getResetPropertyButton(), constraintsResetPropertyButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpandedPropertyPanel;
}
/**
 * Return the ExpandedPropertyTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getExpandedPropertyTextArea() {
	if (ivjExpandedPropertyTextArea == null) {
		try {
			ivjExpandedPropertyTextArea = new javax.swing.JTextArea();
			ivjExpandedPropertyTextArea.setName("ExpandedPropertyTextArea");
			ivjExpandedPropertyTextArea.setLineWrap(true);
			ivjExpandedPropertyTextArea.setWrapStyleWord(true);
			ivjExpandedPropertyTextArea.setBounds(0, 0, 436, 65);
			ivjExpandedPropertyTextArea.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpandedPropertyTextArea;
}
/**
 * Return the ExpandedPropertyTextAreaScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getExpandedPropertyTextAreaScrollPane() {
	if (ivjExpandedPropertyTextAreaScrollPane == null) {
		try {
			ivjExpandedPropertyTextAreaScrollPane = new javax.swing.JScrollPane();
			ivjExpandedPropertyTextAreaScrollPane.setName("ExpandedPropertyTextAreaScrollPane");
			getExpandedPropertyTextAreaScrollPane().setViewportView(getExpandedPropertyTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpandedPropertyTextAreaScrollPane;
}
/**
 * Return the ExpandPropertyButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getExpandPropertyButton() {
	if (ivjExpandPropertyButton == null) {
		try {
			ivjExpandPropertyButton = new javax.swing.JButton();
			ivjExpandPropertyButton.setName("ExpandPropertyButton");
			ivjExpandPropertyButton.setText("Expand");
			ivjExpandPropertyButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjExpandPropertyButton;
}
/**
 * Return the FileButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getFileButtonPanel() {
	if (ivjFileButtonPanel == null) {
		try {
			ivjFileButtonPanel = new javax.swing.JPanel();
			ivjFileButtonPanel.setName("FileButtonPanel");
			ivjFileButtonPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "File"));
			ivjFileButtonPanel.setLayout(new java.awt.GridBagLayout());
			ivjFileButtonPanel.setBackground(new java.awt.Color(204,204,255));
			ivjFileButtonPanel.setMaximumSize(new java.awt.Dimension(100, 10000));

			java.awt.GridBagConstraints constraintsLoadFileButton = new java.awt.GridBagConstraints();
			constraintsLoadFileButton.gridx = 0; constraintsLoadFileButton.gridy = 1;
			constraintsLoadFileButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsLoadFileButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getFileButtonPanel().add(getLoadFileButton(), constraintsLoadFileButton);

			java.awt.GridBagConstraints constraintsSaveFileButton = new java.awt.GridBagConstraints();
			constraintsSaveFileButton.gridx = 0; constraintsSaveFileButton.gridy = 2;
			constraintsSaveFileButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSaveFileButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getFileButtonPanel().add(getSaveFileButton(), constraintsSaveFileButton);

			java.awt.GridBagConstraints constraintsSaveAsFileButton = new java.awt.GridBagConstraints();
			constraintsSaveAsFileButton.gridx = 0; constraintsSaveAsFileButton.gridy = 3;
			constraintsSaveAsFileButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSaveAsFileButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getFileButtonPanel().add(getSaveAsFileButton(), constraintsSaveAsFileButton);

			java.awt.GridBagConstraints constraintsNewFileButton = new java.awt.GridBagConstraints();
			constraintsNewFileButton.gridx = 0; constraintsNewFileButton.gridy = 0;
			constraintsNewFileButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsNewFileButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getFileButtonPanel().add(getNewFileButton(), constraintsNewFileButton);
			getSaveFileButton().setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFileButtonPanel;
}
/**
 * Return the FileMenu property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getFileMenu() {
	if (ivjFileMenu == null) {
		try {
			ivjFileMenu = new javax.swing.JMenu();
			ivjFileMenu.setName("FileMenu");
			ivjFileMenu.setText("File");
			ivjFileMenu.setBackground(new java.awt.Color(204,204,255));
			ivjFileMenu.add(getNewFileMenuItem());
			ivjFileMenu.add(getLoadFileMenuItem());
			ivjFileMenu.add(getSaveFileMenuItem());
			ivjFileMenu.add(getSaveAsFileMenuItem());
			getSaveFileMenuItem().setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFileMenu;
}
/**
 * Return the GeneralPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getGeneralPane() {
	if (ivjGeneralPane == null) {
		try {
			ivjGeneralPane = new javax.swing.JPanel();
			ivjGeneralPane.setName("GeneralPane");
			ivjGeneralPane.setLayout(new java.awt.GridBagLayout());
			ivjGeneralPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsOutputNameHelpButton = new java.awt.GridBagConstraints();
			constraintsOutputNameHelpButton.gridx = 0; constraintsOutputNameHelpButton.gridy = 0;
			constraintsOutputNameHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getOutputNameHelpButton(), constraintsOutputNameHelpButton);

			java.awt.GridBagConstraints constraintsOutputNameLabel = new java.awt.GridBagConstraints();
			constraintsOutputNameLabel.gridx = 1; constraintsOutputNameLabel.gridy = 0;
			constraintsOutputNameLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsOutputNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getOutputNameLabel(), constraintsOutputNameLabel);

			java.awt.GridBagConstraints constraintsOutputNameTextField = new java.awt.GridBagConstraints();
			constraintsOutputNameTextField.gridx = 2; constraintsOutputNameTextField.gridy = 0;
			constraintsOutputNameTextField.gridwidth = 2;
			constraintsOutputNameTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsOutputNameTextField.weightx = 1.0;
			constraintsOutputNameTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getOutputNameTextField(), constraintsOutputNameTextField);

			java.awt.GridBagConstraints constraintsWorkingDirectoryHelpButton = new java.awt.GridBagConstraints();
			constraintsWorkingDirectoryHelpButton.gridx = 0; constraintsWorkingDirectoryHelpButton.gridy = 1;
			constraintsWorkingDirectoryHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getWorkingDirectoryHelpButton(), constraintsWorkingDirectoryHelpButton);

			java.awt.GridBagConstraints constraintsWorkingDirectoryLabel = new java.awt.GridBagConstraints();
			constraintsWorkingDirectoryLabel.gridx = 1; constraintsWorkingDirectoryLabel.gridy = 1;
			constraintsWorkingDirectoryLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsWorkingDirectoryLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getWorkingDirectoryLabel(), constraintsWorkingDirectoryLabel);

			java.awt.GridBagConstraints constraintsWorkingDirectoryTextField = new java.awt.GridBagConstraints();
			constraintsWorkingDirectoryTextField.gridx = 2; constraintsWorkingDirectoryTextField.gridy = 1;
			constraintsWorkingDirectoryTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsWorkingDirectoryTextField.weightx = 1.0;
			constraintsWorkingDirectoryTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getWorkingDirectoryTextField(), constraintsWorkingDirectoryTextField);

			java.awt.GridBagConstraints constraintsDescriptionHelpButton = new java.awt.GridBagConstraints();
			constraintsDescriptionHelpButton.gridx = 0; constraintsDescriptionHelpButton.gridy = 2;
			constraintsDescriptionHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getDescriptionHelpButton(), constraintsDescriptionHelpButton);

			java.awt.GridBagConstraints constraintsDescriptionLabel = new java.awt.GridBagConstraints();
			constraintsDescriptionLabel.gridx = 1; constraintsDescriptionLabel.gridy = 2;
			constraintsDescriptionLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsDescriptionLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getDescriptionLabel(), constraintsDescriptionLabel);

			java.awt.GridBagConstraints constraintsDescriptionTextArea = new java.awt.GridBagConstraints();
			constraintsDescriptionTextArea.gridx = 1; constraintsDescriptionTextArea.gridy = 3;
			constraintsDescriptionTextArea.gridwidth = 3;
			constraintsDescriptionTextArea.fill = java.awt.GridBagConstraints.BOTH;
			constraintsDescriptionTextArea.weightx = 1.0;
			constraintsDescriptionTextArea.weighty = 1.0;
			constraintsDescriptionTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getDescriptionTextArea(), constraintsDescriptionTextArea);

			java.awt.GridBagConstraints constraintsWorkingDirectoryBrowseButton = new java.awt.GridBagConstraints();
			constraintsWorkingDirectoryBrowseButton.gridx = 3; constraintsWorkingDirectoryBrowseButton.gridy = 1;
			constraintsWorkingDirectoryBrowseButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getGeneralPane().add(getWorkingDirectoryBrowseButton(), constraintsWorkingDirectoryBrowseButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjGeneralPane;
}
/**
 * Return the IncludesAddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getIncludesAddButton() {
	if (ivjIncludesAddButton == null) {
		try {
			ivjIncludesAddButton = new javax.swing.JButton();
			ivjIncludesAddButton.setName("IncludesAddButton");
			ivjIncludesAddButton.setText("+");
			ivjIncludesAddButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIncludesAddButton;
}
/**
 * Return the IncludesList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getIncludesList() {
	if (ivjIncludesList == null) {
		try {
			ivjIncludesList = new javax.swing.JList();
			ivjIncludesList.setName("IncludesList");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIncludesList;
}
/**
 * Return the IncludesListHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getIncludesListHelpButton() {
	if (ivjIncludesListHelpButton == null) {
		try {
			ivjIncludesListHelpButton = new javax.swing.JButton();
			ivjIncludesListHelpButton.setName("IncludesListHelpButton");
			ivjIncludesListHelpButton.setText("?");
			ivjIncludesListHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjIncludesListHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIncludesListHelpButton;
}
/**
 * Return the IncludesListPopupMenu property value.
 * @return javax.swing.JPopupMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPopupMenu getIncludesListPopupMenu() {
	if (ivjIncludesListPopupMenu == null) {
		try {
			ivjIncludesListPopupMenu = new javax.swing.JPopupMenu();
			ivjIncludesListPopupMenu.setName("IncludesListPopupMenu");
			ivjIncludesListPopupMenu.setBackground(new java.awt.Color(204,204,255));
			ivjIncludesListPopupMenu.add(getAddIncludePopupMenuItem());
			ivjIncludesListPopupMenu.add(getRemoveIncludesPopupMenuItem());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIncludesListPopupMenu;
}
/**
 * Return the IncludesPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getIncludesPanel() {
	if (ivjIncludesPanel == null) {
		try {
			ivjIncludesPanel = new javax.swing.JPanel();
			ivjIncludesPanel.setName("IncludesPanel");
			ivjIncludesPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Includes"));
			ivjIncludesPanel.setLayout(new java.awt.GridBagLayout());
			ivjIncludesPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsIncludesList = new java.awt.GridBagConstraints();
			constraintsIncludesList.gridx = 0; constraintsIncludesList.gridy = 0;
constraintsIncludesList.gridheight = 3;
			constraintsIncludesList.fill = java.awt.GridBagConstraints.BOTH;
			constraintsIncludesList.weightx = 1.0;
			constraintsIncludesList.weighty = 1.0;
			constraintsIncludesList.insets = new java.awt.Insets(4, 4, 4, 4);
			getIncludesPanel().add(getIncludesList(), constraintsIncludesList);

			java.awt.GridBagConstraints constraintsIncludesAddButton = new java.awt.GridBagConstraints();
			constraintsIncludesAddButton.gridx = 1; constraintsIncludesAddButton.gridy = 0;
			constraintsIncludesAddButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getIncludesPanel().add(getIncludesAddButton(), constraintsIncludesAddButton);

			java.awt.GridBagConstraints constraintsIncludesRemoveButton = new java.awt.GridBagConstraints();
			constraintsIncludesRemoveButton.gridx = 1; constraintsIncludesRemoveButton.gridy = 1;
			constraintsIncludesRemoveButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getIncludesPanel().add(getIncludesRemoveButton(), constraintsIncludesRemoveButton);

			java.awt.GridBagConstraints constraintsIncludesListHelpButton = new java.awt.GridBagConstraints();
			constraintsIncludesListHelpButton.gridx = 1; constraintsIncludesListHelpButton.gridy = 2;
			constraintsIncludesListHelpButton.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsIncludesListHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getIncludesPanel().add(getIncludesListHelpButton(), constraintsIncludesListHelpButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIncludesPanel;
}
/**
 * Return the IncludesRemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getIncludesRemoveButton() {
	if (ivjIncludesRemoveButton == null) {
		try {
			ivjIncludesRemoveButton = new javax.swing.JButton();
			ivjIncludesRemoveButton.setName("IncludesRemoveButton");
			ivjIncludesRemoveButton.setText("-");
			ivjIncludesRemoveButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIncludesRemoveButton;
}
/**
 * Return the InfoSessionPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getInfoSessionPopupMenuItem() {
	if (ivjInfoSessionPopupMenuItem == null) {
		try {
			ivjInfoSessionPopupMenuItem = new javax.swing.JMenuItem();
			ivjInfoSessionPopupMenuItem.setName("InfoSessionPopupMenuItem");
			ivjInfoSessionPopupMenuItem.setText("Info");
			ivjInfoSessionPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInfoSessionPopupMenuItem;
}
/**
 * Return the InfoTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getInfoTextArea() {
	if (ivjInfoTextArea == null) {
		try {
			ivjInfoTextArea = new javax.swing.JTextArea();
			ivjInfoTextArea.setName("InfoTextArea");
			ivjInfoTextArea.setLineWrap(true);
			ivjInfoTextArea.setWrapStyleWord(true);
			ivjInfoTextArea.setText("Information");
			ivjInfoTextArea.setBackground(java.awt.Color.white);
			ivjInfoTextArea.setMaximumSize(new java.awt.Dimension(600, 100));
			ivjInfoTextArea.setBounds(0, 0, 522, 434);
			ivjInfoTextArea.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInfoTextArea;
}
/**
 * Return the JScrollPane1 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getInfoTextAreaScollPane() {
	if (ivjInfoTextAreaScollPane == null) {
		try {
			ivjInfoTextAreaScollPane = new javax.swing.JScrollPane();
			ivjInfoTextAreaScollPane.setName("InfoTextAreaScollPane");
			ivjInfoTextAreaScollPane.setMaximumSize(new java.awt.Dimension(10000, 10000));
			getInfoTextAreaScollPane().setViewportView(getInfoTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInfoTextAreaScollPane;
}
/**
 * Return the InstanceBoundAddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getInstanceBoundAddButton() {
	if (ivjInstanceBoundAddButton == null) {
		try {
			ivjInstanceBoundAddButton = new javax.swing.JButton();
			ivjInstanceBoundAddButton.setName("InstanceBoundAddButton");
			ivjInstanceBoundAddButton.setText("+");
			ivjInstanceBoundAddButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			ivjInstanceBoundAddButton.setEnabled(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundAddButton;
}
/**
 * Return the InstanceBoundHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getInstanceBoundHelpButton() {
	if (ivjInstanceBoundHelpButton == null) {
		try {
			ivjInstanceBoundHelpButton = new javax.swing.JButton();
			ivjInstanceBoundHelpButton.setName("InstanceBoundHelpButton");
			ivjInstanceBoundHelpButton.setText("?");
			ivjInstanceBoundHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjInstanceBoundHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundHelpButton;
}
/**
 * Return the InstanceBoundLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getInstanceBoundLabel() {
	if (ivjInstanceBoundLabel == null) {
		try {
			ivjInstanceBoundLabel = new javax.swing.JLabel();
			ivjInstanceBoundLabel.setName("InstanceBoundLabel");
			ivjInstanceBoundLabel.setText("Default");
			ivjInstanceBoundLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundLabel;
}
/**
 * Return the InstanceBoundMaxTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getInstanceBoundMaxTextField() {
	if (ivjInstanceBoundMaxTextField == null) {
		try {
			ivjInstanceBoundMaxTextField = new javax.swing.JTextField();
			ivjInstanceBoundMaxTextField.setName("InstanceBoundMaxTextField");
			ivjInstanceBoundMaxTextField.setText("<max>");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundMaxTextField;
}
/**
 * Return the InstanceBoundPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
// Warning this method has been changed regeneration will erase those changes. -mo
private javax.swing.JPanel getInstanceBoundPanel() {
	if (ivjInstanceBoundPanel == null) {
		try {
			ivjInstanceBoundPanel = new javax.swing.JPanel();
			ivjInstanceBoundPanel.setName("InstanceBoundPanel");
			ivjInstanceBoundPanel.setPreferredSize(new java.awt.Dimension(109, 99));
			ivjInstanceBoundPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Instance Bound"));
			ivjInstanceBoundPanel.setLayout(new java.awt.GridBagLayout());
			ivjInstanceBoundPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsInstanceBoundHelpButton = new java.awt.GridBagConstraints();
			constraintsInstanceBoundHelpButton.gridx = 0; constraintsInstanceBoundHelpButton.gridy = 0;
			constraintsInstanceBoundHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getInstanceBoundPanel().add(getInstanceBoundHelpButton(), constraintsInstanceBoundHelpButton);

			java.awt.GridBagConstraints constraintsInstanceBoundMaxTextField = new java.awt.GridBagConstraints();
			constraintsInstanceBoundMaxTextField.gridx = 2; constraintsInstanceBoundMaxTextField.gridy = 0;
			constraintsInstanceBoundMaxTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsInstanceBoundMaxTextField.weightx = 1.0;
			constraintsInstanceBoundMaxTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getInstanceBoundPanel().add(getInstanceBoundMaxTextField(), constraintsInstanceBoundMaxTextField);

			java.awt.GridBagConstraints constraintsInstanceBoundLabel = new java.awt.GridBagConstraints();
			constraintsInstanceBoundLabel.gridx = 1; constraintsInstanceBoundLabel.gridy = 0;
			constraintsInstanceBoundLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getInstanceBoundPanel().add(getInstanceBoundLabel(), constraintsInstanceBoundLabel);

			java.awt.GridBagConstraints constraintsInstanceBoundAddButton = new java.awt.GridBagConstraints();
			constraintsInstanceBoundAddButton.gridx = 0; constraintsInstanceBoundAddButton.gridy = 1;
			constraintsInstanceBoundAddButton.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsInstanceBoundAddButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getInstanceBoundPanel().add(getInstanceBoundAddButton(), constraintsInstanceBoundAddButton);

			java.awt.GridBagConstraints constraintsInstanceBoundRemoveButton = new java.awt.GridBagConstraints();
			constraintsInstanceBoundRemoveButton.gridx = 0; constraintsInstanceBoundRemoveButton.gridy = 2;
			constraintsInstanceBoundRemoveButton.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsInstanceBoundRemoveButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getInstanceBoundPanel().add(getInstanceBoundRemoveButton(), constraintsInstanceBoundRemoveButton);

			java.awt.GridBagConstraints constraintsInstanceBoundTableScrollPane = new java.awt.GridBagConstraints();
			constraintsInstanceBoundTableScrollPane.gridx = 1; constraintsInstanceBoundTableScrollPane.gridy = 1;
			constraintsInstanceBoundTableScrollPane.gridwidth = 2;
			constraintsInstanceBoundTableScrollPane.gridheight = 2;
			constraintsInstanceBoundTableScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsInstanceBoundTableScrollPane.weightx = 1.0;
			constraintsInstanceBoundTableScrollPane.weighty = 1.0;
			constraintsInstanceBoundTableScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getInstanceBoundPanel().add(getInstanceBoundTableScrollPane(), constraintsInstanceBoundTableScrollPane);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundPanel;
}
/**
 * Return the InstanceBoundRemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getInstanceBoundRemoveButton() {
	if (ivjInstanceBoundRemoveButton == null) {
		try {
			ivjInstanceBoundRemoveButton = new javax.swing.JButton();
			ivjInstanceBoundRemoveButton.setName("InstanceBoundRemoveButton");
			ivjInstanceBoundRemoveButton.setText("-");
			ivjInstanceBoundRemoveButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			ivjInstanceBoundRemoveButton.setEnabled(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundRemoveButton;
}
/**
 * Return the InstanceBoundTable property value.
 * @return javax.swing.JTable
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTable getInstanceBoundTable() {
	if (ivjInstanceBoundTable == null) {
		try {
			ivjInstanceBoundTable = new javax.swing.JTable();
			ivjInstanceBoundTable.setName("InstanceBoundTable");
			getInstanceBoundTableScrollPane().setColumnHeaderView(ivjInstanceBoundTable.getTableHeader());
			getInstanceBoundTableScrollPane().getViewport().setBackingStoreEnabled(true);
			ivjInstanceBoundTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			ivjInstanceBoundTable.setBounds(0, 0, 200, 200);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundTable;
}
/**
 * Return the InstanceBoundTableScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getInstanceBoundTableScrollPane() {
	if (ivjInstanceBoundTableScrollPane == null) {
		try {
			ivjInstanceBoundTableScrollPane = new javax.swing.JScrollPane();
			ivjInstanceBoundTableScrollPane.setName("InstanceBoundTableScrollPane");
			ivjInstanceBoundTableScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			ivjInstanceBoundTableScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			getInstanceBoundTableScrollPane().setViewportView(getInstanceBoundTable());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInstanceBoundTableScrollPane;
}
/**
 * Return the IntegerBoundHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getIntegerBoundHelpButton() {
	if (ivjIntegerBoundHelpButton == null) {
		try {
			ivjIntegerBoundHelpButton = new javax.swing.JButton();
			ivjIntegerBoundHelpButton.setName("IntegerBoundHelpButton");
			ivjIntegerBoundHelpButton.setText("?");
			ivjIntegerBoundHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjIntegerBoundHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIntegerBoundHelpButton;
}
/**
 * Return the IntegerBoundLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getIntegerBoundLabel() {
	if (ivjIntegerBoundLabel == null) {
		try {
			ivjIntegerBoundLabel = new javax.swing.JLabel();
			ivjIntegerBoundLabel.setName("IntegerBoundLabel");
			ivjIntegerBoundLabel.setText("Default");
			ivjIntegerBoundLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIntegerBoundLabel;
}
/**
 * Return the IntegerBoundMaxTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getIntegerBoundMaxTextField() {
	if (ivjIntegerBoundMaxTextField == null) {
		try {
			ivjIntegerBoundMaxTextField = new javax.swing.JTextField();
			ivjIntegerBoundMaxTextField.setName("IntegerBoundMaxTextField");
			ivjIntegerBoundMaxTextField.setText("<max>");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIntegerBoundMaxTextField;
}
/**
 * Return the IntegerBoundMinTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getIntegerBoundMinTextField() {
	if (ivjIntegerBoundMinTextField == null) {
		try {
			ivjIntegerBoundMinTextField = new javax.swing.JTextField();
			ivjIntegerBoundMinTextField.setName("IntegerBoundMinTextField");
			ivjIntegerBoundMinTextField.setText("<min>");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIntegerBoundMinTextField;
}
/**
 */
private javax.swing.JFileChooser getJavaFileChooser() {

	// lazy init
	if(javaFileChooser == null) {
		javaFileChooser = new JFileChooser();
		javaFileChooser.setFileFilter(new JavaSourceFileFilter());
		javaFileChooser.setMultiSelectionEnabled(false);
		javaFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		javaFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	}

	// use the last directory as this one's working directory
	if((lastFileChooser != null) && (lastFileChooser != javaFileChooser)) {
		java.io.File currentDirectory = lastFileChooser.getCurrentDirectory();
		javaFileChooser.setCurrentDirectory(currentDirectory);
	}

	lastFileChooser = javaFileChooser;
	
	return(javaFileChooser);
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

			java.awt.GridBagConstraints constraintsAddVariableTreeScrollPane = new java.awt.GridBagConstraints();
			constraintsAddVariableTreeScrollPane.gridx = 0; constraintsAddVariableTreeScrollPane.gridy = 0;
constraintsAddVariableTreeScrollPane.gridheight = 2;
			constraintsAddVariableTreeScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsAddVariableTreeScrollPane.weightx = 1.0;
			constraintsAddVariableTreeScrollPane.weighty = 1.0;
			constraintsAddVariableTreeScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getAddVariableTreeScrollPane(), constraintsAddVariableTreeScrollPane);

			java.awt.GridBagConstraints constraintsAddVariableButtonPanel = new java.awt.GridBagConstraints();
			constraintsAddVariableButtonPanel.gridx = 1; constraintsAddVariableButtonPanel.gridy = 1;
			constraintsAddVariableButtonPanel.gridwidth = 2;
			constraintsAddVariableButtonPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsAddVariableButtonPanel.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsAddVariableButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getAddVariableButtonPanel(), constraintsAddVariableButtonPanel);

			java.awt.GridBagConstraints constraintsAddVariableTextFieldPanel = new java.awt.GridBagConstraints();
			constraintsAddVariableTextFieldPanel.gridx = 1; constraintsAddVariableTextFieldPanel.gridy = 0;
			constraintsAddVariableTextFieldPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsAddVariableTextFieldPanel.weightx = 0.1;
			constraintsAddVariableTextFieldPanel.weighty = 1.0;
			constraintsAddVariableTextFieldPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getAddVariableTextFieldPanel(), constraintsAddVariableTextFieldPanel);
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
 * Return the JDialogContentPane1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJDialogContentPane1() {
	if (ivjJDialogContentPane1 == null) {
		try {
			ivjJDialogContentPane1 = new javax.swing.JPanel();
			ivjJDialogContentPane1.setName("JDialogContentPane1");
			ivjJDialogContentPane1.setLayout(new java.awt.GridBagLayout());
			ivjJDialogContentPane1.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsInfoTextAreaScollPane = new java.awt.GridBagConstraints();
			constraintsInfoTextAreaScollPane.gridx = 0; constraintsInfoTextAreaScollPane.gridy = 0;
			constraintsInfoTextAreaScollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsInfoTextAreaScollPane.weightx = 1.0;
			constraintsInfoTextAreaScollPane.weighty = 1.0;
			constraintsInfoTextAreaScollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane1().add(getInfoTextAreaScollPane(), constraintsInfoTextAreaScollPane);

			java.awt.GridBagConstraints constraintsSessionInformationOkButton = new java.awt.GridBagConstraints();
			constraintsSessionInformationOkButton.gridx = 0; constraintsSessionInformationOkButton.gridy = 1;
			constraintsSessionInformationOkButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane1().add(getSessionInformationOkButton(), constraintsSessionInformationOkButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane1;
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
			ivjJFrameContentPane.setLayout(new java.awt.GridBagLayout());
			ivjJFrameContentPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSessionNameListLabel = new java.awt.GridBagConstraints();
			constraintsSessionNameListLabel.gridx = 0; constraintsSessionNameListLabel.gridy = 0;
			constraintsSessionNameListLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSessionNameListLabel.weightx = 0.1;
			constraintsSessionNameListLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getSessionNameListLabel(), constraintsSessionNameListLabel);

			java.awt.GridBagConstraints constraintsSessionInformationTabbedPane = new java.awt.GridBagConstraints();
			constraintsSessionInformationTabbedPane.gridx = 3; constraintsSessionInformationTabbedPane.gridy = 0;
constraintsSessionInformationTabbedPane.gridheight = 2;
			constraintsSessionInformationTabbedPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSessionInformationTabbedPane.weightx = 1.0;
			constraintsSessionInformationTabbedPane.weighty = 1.0;
			constraintsSessionInformationTabbedPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getSessionInformationTabbedPane(), constraintsSessionInformationTabbedPane);

			java.awt.GridBagConstraints constraintsButtonPanel = new java.awt.GridBagConstraints();
			constraintsButtonPanel.gridx = 1; constraintsButtonPanel.gridy = 0;
constraintsButtonPanel.gridheight = 2;
			constraintsButtonPanel.fill = java.awt.GridBagConstraints.VERTICAL;
			constraintsButtonPanel.weightx = 0.1;
			constraintsButtonPanel.weighty = 1.0;
			getJFrameContentPane().add(getButtonPanel(), constraintsButtonPanel);

			java.awt.GridBagConstraints constraintsSessionListScrollPane = new java.awt.GridBagConstraints();
			constraintsSessionListScrollPane.gridx = 0; constraintsSessionListScrollPane.gridy = 1;
			constraintsSessionListScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSessionListScrollPane.weightx = 0.5; // changed from that which was generated! -tcw
			constraintsSessionListScrollPane.weighty = 1.0;
			constraintsSessionListScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getSessionListScrollPane(), constraintsSessionListScrollPane);

			java.awt.GridBagConstraints constraintsShowHideSessionToolbarButton = new java.awt.GridBagConstraints();
			constraintsShowHideSessionToolbarButton.gridx = 2; constraintsShowHideSessionToolbarButton.gridy = 0;
constraintsShowHideSessionToolbarButton.gridheight = 2;
			constraintsShowHideSessionToolbarButton.fill = java.awt.GridBagConstraints.BOTH;
			getJFrameContentPane().add(getShowHideSessionToolbarButton(), constraintsShowHideSessionToolbarButton);

			java.awt.GridBagConstraints constraintsShowHideSessionInformationButton = new java.awt.GridBagConstraints();
			constraintsShowHideSessionInformationButton.gridx = 4; constraintsShowHideSessionInformationButton.gridy = 0;
constraintsShowHideSessionInformationButton.gridheight = 2;
			constraintsShowHideSessionInformationButton.fill = java.awt.GridBagConstraints.BOTH;
			getJFrameContentPane().add(getShowHideSessionInformationButton(), constraintsShowHideSessionInformationButton);
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
 * Return the JPFCheckerPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPFCheckerPanel() {
	if (ivjJPFCheckerPanel == null) {
		try {
			ivjJPFCheckerPanel = new javax.swing.JPanel();
			ivjJPFCheckerPanel.setName("JPFCheckerPanel");
			ivjJPFCheckerPanel.setLayout(new java.awt.GridBagLayout());
			ivjJPFCheckerPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsJPFRadioButton = new java.awt.GridBagConstraints();
			constraintsJPFRadioButton.gridx = 0; constraintsJPFRadioButton.gridy = 0;
			constraintsJPFRadioButton.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPFRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPFCheckerPanel().add(getJPFRadioButton(), constraintsJPFRadioButton);

			java.awt.GridBagConstraints constraintsJPFOptionsButton = new java.awt.GridBagConstraints();
			constraintsJPFOptionsButton.gridx = 1; constraintsJPFOptionsButton.gridy = 0;
			constraintsJPFOptionsButton.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPFOptionsButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPFCheckerPanel().add(getJPFOptionsButton(), constraintsJPFOptionsButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFCheckerPanel;
}
/**
 * Return the JPFOptionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJPFOptionsButton() {
	if (ivjJPFOptionsButton == null) {
		try {
			ivjJPFOptionsButton = new javax.swing.JButton();
			ivjJPFOptionsButton.setName("JPFOptionsButton");
			ivjJPFOptionsButton.setText("Options");
			ivjJPFOptionsButton.setBackground(new java.awt.Color(204,204,255));
			ivjJPFOptionsButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFOptionsButton;
}
/**
 * Return the JPFRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getJPFRadioButton() {
	if (ivjJPFRadioButton == null) {
		try {
			ivjJPFRadioButton = new javax.swing.JRadioButton();
			ivjJPFRadioButton.setName("JPFRadioButton");
			ivjJPFRadioButton.setText("JPF");
			ivjJPFRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			getModelCheckerButtonGroup().add(ivjJPFRadioButton);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPFRadioButton;
}
/**
 * Return the JTextArea1 property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getJTextArea1() {
	if (ivjJTextArea1 == null) {
		try {
			ivjJTextArea1 = new javax.swing.JTextArea();
			ivjJTextArea1.setName("JTextArea1");
			ivjJTextArea1.setLineWrap(true);
			ivjJTextArea1.setBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 1));
			ivjJTextArea1.setWrapStyleWord(true);
			ivjJTextArea1.setText("Note: These options only effect BIR-based model checkers such as Spin and DSpin. \n" +
															"          You must run JJJC before these bounds can be set properly.");
			ivjJTextArea1.setBackground(java.awt.Color.lightGray);
			ivjJTextArea1.setForeground(java.awt.Color.red);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJTextArea1;
}
/**
 * Return the LoadButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getLoadFileButton() {
	if (ivjLoadFileButton == null) {
		try {
			ivjLoadFileButton = new javax.swing.JButton();
			ivjLoadFileButton.setName("LoadFileButton");
			ivjLoadFileButton.setText("Load");
			ivjLoadFileButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLoadFileButton;
}
/**
 * Return the LoadMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getLoadFileMenuItem() {
	if (ivjLoadFileMenuItem == null) {
		try {
			ivjLoadFileMenuItem = new javax.swing.JMenuItem();
			ivjLoadFileMenuItem.setName("LoadFileMenuItem");
			ivjLoadFileMenuItem.setText("Load");
			ivjLoadFileMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLoadFileMenuItem;
}
/**
 * Return the MainClassFileBrowseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getMainClassFileBrowseButton() {
	if (ivjMainClassFileBrowseButton == null) {
		try {
			ivjMainClassFileBrowseButton = new javax.swing.JButton();
			ivjMainClassFileBrowseButton.setName("MainClassFileBrowseButton");
			ivjMainClassFileBrowseButton.setText("Browse");
			ivjMainClassFileBrowseButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassFileBrowseButton;
}
/**
 * Return the MainClassFileHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getMainClassFileHelpButton() {
	if (ivjMainClassFileHelpButton == null) {
		try {
			ivjMainClassFileHelpButton = new javax.swing.JButton();
			ivjMainClassFileHelpButton.setName("MainClassFileHelpButton");
			ivjMainClassFileHelpButton.setText("?");
			ivjMainClassFileHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjMainClassFileHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassFileHelpButton;
}
/**
 * Return the MainClassFileLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getMainClassFileLabel() {
	if (ivjMainClassFileLabel == null) {
		try {
			ivjMainClassFileLabel = new javax.swing.JLabel();
			ivjMainClassFileLabel.setName("MainClassFileLabel");
			ivjMainClassFileLabel.setText("Main Class File");
			ivjMainClassFileLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassFileLabel;
}
/**
 * Return the MainClassFilePanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getMainClassFilePanel() {
	if (ivjMainClassFilePanel == null) {
		try {
			ivjMainClassFilePanel = new javax.swing.JPanel();
			ivjMainClassFilePanel.setName("MainClassFilePanel");
			ivjMainClassFilePanel.setLayout(new java.awt.GridBagLayout());
			ivjMainClassFilePanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsMainClassFileHelpButton = new java.awt.GridBagConstraints();
			constraintsMainClassFileHelpButton.gridx = 0; constraintsMainClassFileHelpButton.gridy = 0;
			constraintsMainClassFileHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getMainClassFilePanel().add(getMainClassFileHelpButton(), constraintsMainClassFileHelpButton);

			java.awt.GridBagConstraints constraintsMainClassFileLabel = new java.awt.GridBagConstraints();
			constraintsMainClassFileLabel.gridx = 1; constraintsMainClassFileLabel.gridy = 0;
			constraintsMainClassFileLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getMainClassFilePanel().add(getMainClassFileLabel(), constraintsMainClassFileLabel);

			java.awt.GridBagConstraints constraintsMainClassFileTextField = new java.awt.GridBagConstraints();
			constraintsMainClassFileTextField.gridx = 2; constraintsMainClassFileTextField.gridy = 0;
			constraintsMainClassFileTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsMainClassFileTextField.weightx = 1.0;
			constraintsMainClassFileTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getMainClassFilePanel().add(getMainClassFileTextField(), constraintsMainClassFileTextField);

			java.awt.GridBagConstraints constraintsMainClassFileBrowseButton = new java.awt.GridBagConstraints();
			constraintsMainClassFileBrowseButton.gridx = 3; constraintsMainClassFileBrowseButton.gridy = 0;
			constraintsMainClassFileBrowseButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getMainClassFilePanel().add(getMainClassFileBrowseButton(), constraintsMainClassFileBrowseButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassFilePanel;
}
/**
 * Return the MainClassFileTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getMainClassFileTextField() {
	if (ivjMainClassFileTextField == null) {
		try {
			ivjMainClassFileTextField = new javax.swing.JTextField();
			ivjMainClassFileTextField.setName("MainClassFileTextField");
			ivjMainClassFileTextField.setText("<main_class_file>");
			ivjMainClassFileTextField.setEditable(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMainClassFileTextField;
}
/**
 * Insert the method's description here.
 * Creation date: (8/12/2002 3:50:36 PM)
 * @return javax.swing.ButtonGroup
 */
public javax.swing.ButtonGroup getModelCheckerButtonGroup() {
	if(modelCheckerButtonGroup == null) {
		modelCheckerButtonGroup = new ButtonGroup();
	}
	return modelCheckerButtonGroup;
}
/**
 * Return the ModelCheckerCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getModelCheckerCheckBox() {
	if (ivjModelCheckerCheckBox == null) {
		try {
			ivjModelCheckerCheckBox = new javax.swing.JCheckBox();
			ivjModelCheckerCheckBox.setName("ModelCheckerCheckBox");
			ivjModelCheckerCheckBox.setText("Model Checker");
			ivjModelCheckerCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjModelCheckerCheckBox;
}
/**
 * Return the ModelCheckerOptionsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getModelCheckerOptionsPanel() {
	if (ivjModelCheckerOptionsPanel == null) {
		try {
			ivjModelCheckerOptionsPanel = new javax.swing.JPanel();
			ivjModelCheckerOptionsPanel.setName("ModelCheckerOptionsPanel");
			ivjModelCheckerOptionsPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Model Checker Options"));
			ivjModelCheckerOptionsPanel.setLayout(new java.awt.GridBagLayout());
			ivjModelCheckerOptionsPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsOtherModelCheckerPanel = new java.awt.GridBagConstraints();
			constraintsOtherModelCheckerPanel.gridx = 0; constraintsOtherModelCheckerPanel.gridy = 1;
			constraintsOtherModelCheckerPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsOtherModelCheckerPanel.weightx = 1.0;
			constraintsOtherModelCheckerPanel.weighty = 1.0;
			constraintsOtherModelCheckerPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getModelCheckerOptionsPanel().add(getOtherModelCheckerPanel(), constraintsOtherModelCheckerPanel);

			java.awt.GridBagConstraints constraintsBIRModelCheckerPanel = new java.awt.GridBagConstraints();
			constraintsBIRModelCheckerPanel.gridx = 0; constraintsBIRModelCheckerPanel.gridy = 0;
			constraintsBIRModelCheckerPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsBIRModelCheckerPanel.weightx = 1.0;
			constraintsBIRModelCheckerPanel.weighty = 1.0;
			constraintsBIRModelCheckerPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getModelCheckerOptionsPanel().add(getBIRModelCheckerPanel(), constraintsBIRModelCheckerPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjModelCheckerOptionsPanel;
}
/**
 * Return the NewFileButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getNewFileButton() {
	if (ivjNewFileButton == null) {
		try {
			ivjNewFileButton = new javax.swing.JButton();
			ivjNewFileButton.setName("NewFileButton");
			ivjNewFileButton.setText("New");
			ivjNewFileButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNewFileButton;
}
/**
 * Return the NewFileMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getNewFileMenuItem() {
	if (ivjNewFileMenuItem == null) {
		try {
			ivjNewFileMenuItem = new javax.swing.JMenuItem();
			ivjNewFileMenuItem.setName("NewFileMenuItem");
			ivjNewFileMenuItem.setText("New");
			ivjNewFileMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNewFileMenuItem;
}
/**
 * Return the SessionAddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getNewSessionButton() {
	if (ivjNewSessionButton == null) {
		try {
			ivjNewSessionButton = new javax.swing.JButton();
			ivjNewSessionButton.setName("NewSessionButton");
			ivjNewSessionButton.setText("New");
			ivjNewSessionButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNewSessionButton;
}
/**
 * Return the NewMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getNewSessionMenuItem() {
	if (ivjNewSessionMenuItem == null) {
		try {
			ivjNewSessionMenuItem = new javax.swing.JMenuItem();
			ivjNewSessionMenuItem.setName("NewSessionMenuItem");
			ivjNewSessionMenuItem.setText("New");
			ivjNewSessionMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNewSessionMenuItem;
}
/**
 * Return the NewSessionPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getNewSessionPopupMenuItem() {
	if (ivjNewSessionPopupMenuItem == null) {
		try {
			ivjNewSessionPopupMenuItem = new javax.swing.JMenuItem();
			ivjNewSessionPopupMenuItem.setName("NewSessionPopupMenuItem");
			ivjNewSessionPopupMenuItem.setText("New");
			ivjNewSessionPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNewSessionPopupMenuItem;
}
/**
 * Return the OtherModelCheckerPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getOtherModelCheckerPanel() {
	if (ivjOtherModelCheckerPanel == null) {
		try {
			ivjOtherModelCheckerPanel = new javax.swing.JPanel();
			ivjOtherModelCheckerPanel.setName("OtherModelCheckerPanel");
			ivjOtherModelCheckerPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Other"));
			ivjOtherModelCheckerPanel.setLayout(new java.awt.GridBagLayout());
			ivjOtherModelCheckerPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsCheckerNameLabel1 = new java.awt.GridBagConstraints();
			constraintsCheckerNameLabel1.gridx = 0; constraintsCheckerNameLabel1.gridy = 0;
			constraintsCheckerNameLabel1.anchor = java.awt.GridBagConstraints.WEST;
			constraintsCheckerNameLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getOtherModelCheckerPanel().add(getCheckerNameLabel1(), constraintsCheckerNameLabel1);

			java.awt.GridBagConstraints constraintsJPFCheckerPanel = new java.awt.GridBagConstraints();
			constraintsJPFCheckerPanel.gridx = 0; constraintsJPFCheckerPanel.gridy = 1;
			constraintsJPFCheckerPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPFCheckerPanel.weightx = 1.0;
			constraintsJPFCheckerPanel.weighty = 1.0;
			constraintsJPFCheckerPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getOtherModelCheckerPanel().add(getJPFCheckerPanel(), constraintsJPFCheckerPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOtherModelCheckerPanel;
}
/**
 * Return the OutputNameHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOutputNameHelpButton() {
	if (ivjOutputNameHelpButton == null) {
		try {
			ivjOutputNameHelpButton = new javax.swing.JButton();
			ivjOutputNameHelpButton.setName("OutputNameHelpButton");
			ivjOutputNameHelpButton.setText("?");
			ivjOutputNameHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjOutputNameHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOutputNameHelpButton;
}
/**
 * Return the OutputNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getOutputNameLabel() {
	if (ivjOutputNameLabel == null) {
		try {
			ivjOutputNameLabel = new javax.swing.JLabel();
			ivjOutputNameLabel.setName("OutputNameLabel");
			ivjOutputNameLabel.setText("Output Name");
			ivjOutputNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOutputNameLabel;
}
/**
 * Return the OutputNameTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getOutputNameTextField() {
	if (ivjOutputNameTextField == null) {
		try {
			ivjOutputNameTextField = new javax.swing.JTextField();
			ivjOutputNameTextField.setName("OutputNameTextField");
			ivjOutputNameTextField.setText("<output_name>");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOutputNameTextField;
}
/**
 * Get the JLabel associated with this parameter.  If it doesn't exist
 * yet, create it using the parameter name as the text for the label.
 *
 * @return javax.swing.JLabel
 * @param parameterName java.lang.String
 */
private JLabel getParameterLabel(String parameterName) {
	
    JLabel label = (JLabel) patternParameterLabelMap.get(parameterName);
    if (label == null) {
        label = new JLabel(parameterName);
        patternParameterLabelMap.put(parameterName, label);
    }
    
    return(label);
}
/**
 * Get the JTextField associated with the given parameterName.  If this
 * text field has not been created, create it (init with an empty string).
 *
 * @return javax.swing.JTextField
 * @param parameterName java.lang.String
 */
private JTextField getParameterTextField(String parameterName) {

	if(patternParameterTextFieldMap == null) {
		patternParameterTextFieldMap = new HashMap();
	}
	
    JTextField textField = (JTextField) patternParameterTextFieldMap.get(parameterName);
    if (textField == null) {
        textField = new JTextField(15);
        patternParameterTextFieldMap.put(parameterName, textField);
        java.awt.event.MouseListener ml = new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent me) {
                if (me.isPopupTrigger()) {
                    java.awt.Component c = me.getComponent();
                    if (c instanceof JTextField) {
                        setActiveParameterTextField((JTextField) c);
                    }
                    getPredicatePopupMenu().show(c, me.getX(), me.getY());
                }
            }
            public void mouseReleased(java.awt.event.MouseEvent me) {
                if (me.isPopupTrigger()) {
                    java.awt.Component c = me.getComponent();
                    if (c instanceof JTextField) {
                        setActiveParameterTextField((JTextField) c);
                    }
                    getPredicatePopupMenu().show(c, me.getX(), me.getY());
                }
            }
        };
        textField.addMouseListener(ml);
    }
    return (textField);
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
		} catch (java.lang.Throwable ivjExc) {
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
 * Return the PatternPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getPatternPanel() {
	if (ivjPatternPanel == null) {
		try {
			ivjPatternPanel = new javax.swing.JPanel();
			ivjPatternPanel.setName("PatternPanel");
			ivjPatternPanel.setLayout(new java.awt.GridBagLayout());
			ivjPatternPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsPatternLabel = new java.awt.GridBagConstraints();
			constraintsPatternLabel.gridx = 0; constraintsPatternLabel.gridy = 0;
			constraintsPatternLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsPatternLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getPatternPanel().add(getPatternLabel(), constraintsPatternLabel);

			java.awt.GridBagConstraints constraintsPatternNameComboBox = new java.awt.GridBagConstraints();
			constraintsPatternNameComboBox.gridx = 0; constraintsPatternNameComboBox.gridy = 1;
			constraintsPatternNameComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternNameComboBox.weightx = 1.0;
			constraintsPatternNameComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getPatternPanel().add(getPatternNameComboBox(), constraintsPatternNameComboBox);

			java.awt.GridBagConstraints constraintsPatternScopeComboBox = new java.awt.GridBagConstraints();
			constraintsPatternScopeComboBox.gridx = 1; constraintsPatternScopeComboBox.gridy = 1;
			constraintsPatternScopeComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternScopeComboBox.weightx = 1.0;
			constraintsPatternScopeComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getPatternPanel().add(getPatternScopeComboBox(), constraintsPatternScopeComboBox);

			java.awt.GridBagConstraints constraintsPatternTextField = new java.awt.GridBagConstraints();
			constraintsPatternTextField.gridx = 0; constraintsPatternTextField.gridy = 2;
			constraintsPatternTextField.gridwidth = 2;
			constraintsPatternTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternTextField.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsPatternTextField.weightx = 0.1;
			constraintsPatternTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getPatternPanel().add(getPatternTextField(), constraintsPatternTextField);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternPanel;
}
/**
 * Return the PatternParameterInsidePanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getPatternParameterInsidePanel() {
	if (ivjPatternParameterInsidePanel == null) {
		try {
			ivjPatternParameterInsidePanel = new javax.swing.JPanel();
			ivjPatternParameterInsidePanel.setName("PatternParameterInsidePanel");
			ivjPatternParameterInsidePanel.setAutoscrolls(false);
			ivjPatternParameterInsidePanel.setLayout(new java.awt.GridBagLayout());
			ivjPatternParameterInsidePanel.setBounds(0, 0, 387, 51);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternParameterInsidePanel;
}
/**
 * Return the PatternParameterLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPatternParameterLabel() {
	if (ivjPatternParameterLabel == null) {
		try {
			ivjPatternParameterLabel = new javax.swing.JLabel();
			ivjPatternParameterLabel.setName("PatternParameterLabel");
			ivjPatternParameterLabel.setText("Pattern Parameters");
			ivjPatternParameterLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternParameterLabel;
}
/**
 * Return the PatternParameterPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getPatternParameterPanel() {
	if (ivjPatternParameterPanel == null) {
		try {
			ivjPatternParameterPanel = new javax.swing.JPanel();
			ivjPatternParameterPanel.setName("PatternParameterPanel");
			ivjPatternParameterPanel.setLayout(new java.awt.GridBagLayout());
			ivjPatternParameterPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsPatternParameterLabel = new java.awt.GridBagConstraints();
			constraintsPatternParameterLabel.gridx = 0; constraintsPatternParameterLabel.gridy = 0;
			constraintsPatternParameterLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsPatternParameterLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getPatternParameterPanel().add(getPatternParameterLabel(), constraintsPatternParameterLabel);

			java.awt.GridBagConstraints constraintsPatternParameterScrollPane = new java.awt.GridBagConstraints();
			constraintsPatternParameterScrollPane.gridx = 0; constraintsPatternParameterScrollPane.gridy = 1;
			constraintsPatternParameterScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsPatternParameterScrollPane.weightx = 1.0;
			constraintsPatternParameterScrollPane.weighty = 1.0;
			constraintsPatternParameterScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getPatternParameterPanel().add(getPatternParameterScrollPane(), constraintsPatternParameterScrollPane);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternParameterPanel;
}
/**
 * Return the PatternParameterScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getPatternParameterScrollPane() {
	if (ivjPatternParameterScrollPane == null) {
		try {
			ivjPatternParameterScrollPane = new javax.swing.JScrollPane();
			ivjPatternParameterScrollPane.setName("PatternParameterScrollPane");
			ivjPatternParameterScrollPane.setAutoscrolls(false);
			ivjPatternParameterScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			ivjPatternParameterScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			getPatternParameterScrollPane().setViewportView(getPatternParameterInsidePanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternParameterScrollPane;
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
 * Return the PatternTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getPatternTextField() {
	if (ivjPatternTextField == null) {
		try {
			ivjPatternTextField = new javax.swing.JTextField();
			ivjPatternTextField.setName("PatternTextField");
			ivjPatternTextField.setText("<pattern>");
			ivjPatternTextField.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternTextField;
}

    /**
     * Get the set of all current predicates in the system.  Right now, this will
     * actually query the source, PredicateSet has a static set of Predicates and 
     * PredicateSets.
     *
     * @return Set A Set of all current Predicates defined in the system.
     */
    private Set getPredicateSet() {

	Set ps = new HashSet();
	Enumeration e = PredicateSet.getPredicateSetTable().elements();
	while(e.hasMoreElements()) {
	    Enumeration e2 = ((PredicateSet) e.nextElement()).getPredicateTable().elements();
	    while(e2.hasMoreElements()) {
		Object o = e2.nextElement();
		if((o != null) && (o instanceof Predicate)) {
		    Predicate p = (Predicate)o;
		    ps.add(p);
		}
	    }
	}

	return(ps);
    }

/**
 * Get the popup menu that contains all the predicates that are available in the
 * system at this time.  Each menu item in the menu will behave in an identical fashion,
 * it will append it's predicate string into the selected text field.
 *
 * @return JPopupMenu A popup menu with a selection of all available predicates in the system.
 */
private JPopupMenu getPredicatePopupMenu() {

	JPopupMenu popupMenu = new JPopupMenu();
	
	Set ps = getPredicateSet();
	if((ps != null) && (ps.size() > 0)) {
	    Iterator psi = ps.iterator();
	    while(psi.hasNext()) {
		Predicate currentPredicate = (Predicate)psi.next();
		String predicateName = currentPredicate.getName().toString();
		JMenuItem currentMenuItem = new JMenuItem(predicateName);
		// build the action command: <predicate_name>(<predicate_params>)
		// such that predicate_params is a comma separated list of the parameter types: <type>, <type>, ...
		StringBuffer ppBuffer = new StringBuffer();
		Vector paramTypes = currentPredicate.getParamTypes();
		for(int i = 0; i < paramTypes.size(); i++) {
		    ppBuffer.append(paramTypes.get(i).toString());
		    ppBuffer.append(",");
		}
		String predicateParams = ppBuffer.toString().substring(ppBuffer.length() - 1);
		String actionCommand = predicateName + "(" + predicateParams + ")";
		currentMenuItem.setActionCommand(actionCommand);
		java.awt.event.ActionListener al = new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent ae) {
			    insertParameterText(ae.getActionCommand());
			}
		    };
		currentMenuItem.addActionListener(al);
		popupMenu.add(currentMenuItem);
	    }
	}

	Object[] trash = popupMenu.getSubElements();
	if((trash == null) || (trash.length <= 0)) {
		JMenuItem mi = new JMenuItem("No Predicates Found.");
		mi.setActionCommand("");
		popupMenu.add(mi);
	}
	
	return(popupMenu);
}
/**
 * Return the PropertyPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getPropertyPanel() {
	if (ivjPropertyPanel == null) {
		try {
			ivjPropertyPanel = new javax.swing.JPanel();
			ivjPropertyPanel.setName("PropertyPanel");
			ivjPropertyPanel.setPreferredSize(new java.awt.Dimension(300, 300));
			ivjPropertyPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Property"));
			ivjPropertyPanel.setLayout(new java.awt.GridBagLayout());
			ivjPropertyPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsQuantificationPanel = new java.awt.GridBagConstraints();
			constraintsQuantificationPanel.gridx = 0; constraintsQuantificationPanel.gridy = 0;
			constraintsQuantificationPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsQuantificationPanel.weightx = 1.0;
			constraintsQuantificationPanel.weighty = 0.5;
			constraintsQuantificationPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getPropertyPanel().add(getQuantificationPanel(), constraintsQuantificationPanel);

			java.awt.GridBagConstraints constraintsPatternPanel = new java.awt.GridBagConstraints();
			constraintsPatternPanel.gridx = 0; constraintsPatternPanel.gridy = 1;
			constraintsPatternPanel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsPatternPanel.weightx = 1.0;
			constraintsPatternPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getPropertyPanel().add(getPatternPanel(), constraintsPatternPanel);

			java.awt.GridBagConstraints constraintsPatternParameterPanel = new java.awt.GridBagConstraints();
			constraintsPatternParameterPanel.gridx = 0; constraintsPatternParameterPanel.gridy = 2;
			constraintsPatternParameterPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsPatternParameterPanel.weightx = 1.0;
			constraintsPatternParameterPanel.weighty = 0.9;
			constraintsPatternParameterPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getPropertyPanel().add(getPatternParameterPanel(), constraintsPatternParameterPanel);

			java.awt.GridBagConstraints constraintsExpandedPropertyPanel = new java.awt.GridBagConstraints();
			constraintsExpandedPropertyPanel.gridx = 0; constraintsExpandedPropertyPanel.gridy = 3;
			constraintsExpandedPropertyPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsExpandedPropertyPanel.weightx = 1.0;
			constraintsExpandedPropertyPanel.weighty = 0.7;
			constraintsExpandedPropertyPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getPropertyPanel().add(getExpandedPropertyPanel(), constraintsExpandedPropertyPanel);
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
 * Return the QuantificationLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getQuantificationLabel() {
	if (ivjQuantificationLabel == null) {
		try {
			ivjQuantificationLabel = new javax.swing.JLabel();
			ivjQuantificationLabel.setName("QuantificationLabel");
			ivjQuantificationLabel.setText("Quantification");
			ivjQuantificationLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationLabel;
}
/**
 * Return the QuantificationPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getQuantificationPanel() {
	if (ivjQuantificationPanel == null) {
		try {
			ivjQuantificationPanel = new javax.swing.JPanel();
			ivjQuantificationPanel.setName("QuantificationPanel");
			ivjQuantificationPanel.setLayout(new java.awt.GridBagLayout());
			ivjQuantificationPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsQuantificationLabel = new java.awt.GridBagConstraints();
			constraintsQuantificationLabel.gridx = 0; constraintsQuantificationLabel.gridy = 0;
			constraintsQuantificationLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsQuantificationLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getQuantificationPanel().add(getQuantificationLabel(), constraintsQuantificationLabel);

			java.awt.GridBagConstraints constraintsQuantificationTableScrollPane = new java.awt.GridBagConstraints();
			constraintsQuantificationTableScrollPane.gridx = 0; constraintsQuantificationTableScrollPane.gridy = 1;
			constraintsQuantificationTableScrollPane.gridheight = 2;
			constraintsQuantificationTableScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsQuantificationTableScrollPane.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsQuantificationTableScrollPane.weightx = 0.1;
			constraintsQuantificationTableScrollPane.weighty = 1.0;
			constraintsQuantificationTableScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getQuantificationPanel().add(getQuantificationTableScrollPane(), constraintsQuantificationTableScrollPane);

			java.awt.GridBagConstraints constraintsAddQuantificationButton = new java.awt.GridBagConstraints();
			constraintsAddQuantificationButton.gridx = 1; constraintsAddQuantificationButton.gridy = 1;
			constraintsAddQuantificationButton.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsAddQuantificationButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getQuantificationPanel().add(getAddQuantificationButton(), constraintsAddQuantificationButton);

			java.awt.GridBagConstraints constraintsRemoveQuantificationButton = new java.awt.GridBagConstraints();
			constraintsRemoveQuantificationButton.gridx = 1; constraintsRemoveQuantificationButton.gridy = 2;
			constraintsRemoveQuantificationButton.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsRemoveQuantificationButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getQuantificationPanel().add(getRemoveQuantificationButton(), constraintsRemoveQuantificationButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationPanel;
}
/**
 * Return the QuantificationTable property value.
 * @return javax.swing.JTable
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTable getQuantificationTable() {
	if (ivjQuantificationTable == null) {
		try {
			ivjQuantificationTable = new javax.swing.JTable();
			ivjQuantificationTable.setName("QuantificationTable");
			getQuantificationTableScrollPane().setColumnHeaderView(ivjQuantificationTable.getTableHeader());
			getQuantificationTableScrollPane().getViewport().setBackingStoreEnabled(true);
			ivjQuantificationTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			ivjQuantificationTable.setPreferredSize(new java.awt.Dimension(553,86));
			ivjQuantificationTable.setBounds(0, 0, 553, 86);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationTable;
}
/**
 * Return the QuantificationTableScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getQuantificationTableScrollPane() {
	if (ivjQuantificationTableScrollPane == null) {
		try {
			ivjQuantificationTableScrollPane = new javax.swing.JScrollPane();
			ivjQuantificationTableScrollPane.setName("QuantificationTableScrollPane");
			ivjQuantificationTableScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			ivjQuantificationTableScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			getQuantificationTableScrollPane().setViewportView(getQuantificationTable());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQuantificationTableScrollPane;
}
/**
 * Return the RemoveClasspathPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getRemoveClasspathPopupMenuItem() {
	if (ivjRemoveClasspathPopupMenuItem == null) {
		try {
			ivjRemoveClasspathPopupMenuItem = new javax.swing.JMenuItem();
			ivjRemoveClasspathPopupMenuItem.setName("RemoveClasspathPopupMenuItem");
			ivjRemoveClasspathPopupMenuItem.setText("Remove");
			ivjRemoveClasspathPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveClasspathPopupMenuItem;
}
/**
 * Return the RemoveIncludesPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getRemoveIncludesPopupMenuItem() {
	if (ivjRemoveIncludesPopupMenuItem == null) {
		try {
			ivjRemoveIncludesPopupMenuItem = new javax.swing.JMenuItem();
			ivjRemoveIncludesPopupMenuItem.setName("RemoveIncludesPopupMenuItem");
			ivjRemoveIncludesPopupMenuItem.setText("Remove");
			ivjRemoveIncludesPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveIncludesPopupMenuItem;
}
/**
 * Return the RemoveQuantificationButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveQuantificationButton() {
	if (ivjRemoveQuantificationButton == null) {
		try {
			ivjRemoveQuantificationButton = new javax.swing.JButton();
			ivjRemoveQuantificationButton.setName("RemoveQuantificationButton");
			ivjRemoveQuantificationButton.setText("-");
			ivjRemoveQuantificationButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveQuantificationButton;
}
/**
 * Return the SessionRemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveSessionButton() {
	if (ivjRemoveSessionButton == null) {
		try {
			ivjRemoveSessionButton = new javax.swing.JButton();
			ivjRemoveSessionButton.setName("RemoveSessionButton");
			ivjRemoveSessionButton.setText("Remove");
			ivjRemoveSessionButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveSessionButton;
}
/**
 * Return the RemoveMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getRemoveSessionMenuItem() {
	if (ivjRemoveSessionMenuItem == null) {
		try {
			ivjRemoveSessionMenuItem = new javax.swing.JMenuItem();
			ivjRemoveSessionMenuItem.setName("RemoveSessionMenuItem");
			ivjRemoveSessionMenuItem.setText("Remove");
			ivjRemoveSessionMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveSessionMenuItem;
}
/**
 * Return the RemoveSessionPopupMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getRemoveSessionPopupMenuItem() {
	if (ivjRemoveSessionPopupMenuItem == null) {
		try {
			ivjRemoveSessionPopupMenuItem = new javax.swing.JMenuItem();
			ivjRemoveSessionPopupMenuItem.setName("RemoveSessionPopupMenuItem");
			ivjRemoveSessionPopupMenuItem.setText("Remove");
			ivjRemoveSessionPopupMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveSessionPopupMenuItem;
}
/**
 * Return the ResetDefaultsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getResetDefaultsButton() {
	if (ivjResetDefaultsButton == null) {
		try {
			ivjResetDefaultsButton = new javax.swing.JButton();
			ivjResetDefaultsButton.setName("ResetDefaultsButton");
			ivjResetDefaultsButton.setText("Reset Default Values");
			ivjResetDefaultsButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResetDefaultsButton;
}
/**
 * Return the ResetPropertyButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getResetPropertyButton() {
	if (ivjResetPropertyButton == null) {
		try {
			ivjResetPropertyButton = new javax.swing.JButton();
			ivjResetPropertyButton.setName("ResetPropertyButton");
			ivjResetPropertyButton.setText("Reset");
			ivjResetPropertyButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResetPropertyButton;
}
/**
 * Return the ResourceBoundedRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getResourceBoundedRadioButton() {
	if (ivjResourceBoundedRadioButton == null) {
		try {
			ivjResourceBoundedRadioButton = new javax.swing.JRadioButton();
			ivjResourceBoundedRadioButton.setName("ResourceBoundedRadioButton");
			ivjResourceBoundedRadioButton.setText("Resource Bounded");
			ivjResourceBoundedRadioButton.setBackground(new java.awt.Color(204,204,255));
			ivjResourceBoundedRadioButton.setEnabled(true);
			// user code begin {1}
			getBirSearchModeButtonGroup().add(ivjResourceBoundedRadioButton);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResourceBoundedRadioButton;
}
/**
 * Return the ResourceBoundsPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getResourceBoundsPane() {
	if (ivjResourceBoundsPane == null) {
		try {
			ivjResourceBoundsPane = new javax.swing.JPanel();
			ivjResourceBoundsPane.setName("ResourceBoundsPane");
			ivjResourceBoundsPane.setLayout(new java.awt.GridBagLayout());
			ivjResourceBoundsPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsResetDefaultsButton = new java.awt.GridBagConstraints();
			constraintsResetDefaultsButton.gridx = 0; constraintsResetDefaultsButton.gridy = 3;
			constraintsResetDefaultsButton.gridwidth = 2;
			constraintsResetDefaultsButton.weighty = 0.2;
			constraintsResetDefaultsButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getResourceBoundsPane().add(getResetDefaultsButton(), constraintsResetDefaultsButton);

			java.awt.GridBagConstraints constraintsInstanceBoundPanel = new java.awt.GridBagConstraints();
			constraintsInstanceBoundPanel.gridx = 0; constraintsInstanceBoundPanel.gridy = 2;
			constraintsInstanceBoundPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsInstanceBoundPanel.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsInstanceBoundPanel.weightx = 0.5;
			constraintsInstanceBoundPanel.weighty = 0.5;
			constraintsInstanceBoundPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getResourceBoundsPane().add(getInstanceBoundPanel(), constraintsInstanceBoundPanel);

			java.awt.GridBagConstraints constraintsThreadInstanceBoundPanel = new java.awt.GridBagConstraints();
			constraintsThreadInstanceBoundPanel.gridx = 1; constraintsThreadInstanceBoundPanel.gridy = 2;
			constraintsThreadInstanceBoundPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsThreadInstanceBoundPanel.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsThreadInstanceBoundPanel.weightx = 0.5;
			constraintsThreadInstanceBoundPanel.weighty = 0.5;
			constraintsThreadInstanceBoundPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getResourceBoundsPane().add(getThreadInstanceBoundPanel(), constraintsThreadInstanceBoundPanel);

			java.awt.GridBagConstraints constraintsBIRIntegerBoundPanel = new java.awt.GridBagConstraints();
			constraintsBIRIntegerBoundPanel.gridx = 0; constraintsBIRIntegerBoundPanel.gridy = 1;
			constraintsBIRIntegerBoundPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsBIRIntegerBoundPanel.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBIRIntegerBoundPanel.weightx = 0.5;
			constraintsBIRIntegerBoundPanel.weighty = 0.5;
			constraintsBIRIntegerBoundPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getResourceBoundsPane().add(getBIRIntegerBoundPanel(), constraintsBIRIntegerBoundPanel);

			java.awt.GridBagConstraints constraintsJTextArea1 = new java.awt.GridBagConstraints();
			constraintsJTextArea1.gridx = 0; constraintsJTextArea1.gridy = 0;
			constraintsJTextArea1.gridwidth = 2;
			constraintsJTextArea1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJTextArea1.weightx = 1.0;
			constraintsJTextArea1.weighty = 0.1;
			constraintsJTextArea1.insets = new java.awt.Insets(4, 4, 4, 4);
			getResourceBoundsPane().add(getJTextArea1(), constraintsJTextArea1);

			java.awt.GridBagConstraints constraintsBIRArrayBoundPanel = new java.awt.GridBagConstraints();
			constraintsBIRArrayBoundPanel.gridx = 1; constraintsBIRArrayBoundPanel.gridy = 1;
			constraintsBIRArrayBoundPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsBIRArrayBoundPanel.anchor = java.awt.GridBagConstraints.NORTHWEST;
			constraintsBIRArrayBoundPanel.weightx = 0.5;
			constraintsBIRArrayBoundPanel.weighty = 0.5;
			constraintsBIRArrayBoundPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getResourceBoundsPane().add(getBIRArrayBoundPanel(), constraintsBIRArrayBoundPanel);
			recursivelySetEnabled(getResourceBoundsPane(), false);
			getJTextArea1().setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResourceBoundsPane;
}
/**
 * Return the SaveAsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSaveAsFileButton() {
	if (ivjSaveAsFileButton == null) {
		try {
			ivjSaveAsFileButton = new javax.swing.JButton();
			ivjSaveAsFileButton.setName("SaveAsFileButton");
			ivjSaveAsFileButton.setText("Save As");
			ivjSaveAsFileButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveAsFileButton;
}
/**
 * Return the SaveAsMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getSaveAsFileMenuItem() {
	if (ivjSaveAsFileMenuItem == null) {
		try {
			ivjSaveAsFileMenuItem = new javax.swing.JMenuItem();
			ivjSaveAsFileMenuItem.setName("SaveAsFileMenuItem");
			ivjSaveAsFileMenuItem.setText("Save As");
			ivjSaveAsFileMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveAsFileMenuItem;
}
/**
 * Return the SaveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSaveFileButton() {
	if (ivjSaveFileButton == null) {
		try {
			ivjSaveFileButton = new javax.swing.JButton();
			ivjSaveFileButton.setName("SaveFileButton");
			ivjSaveFileButton.setText("Save");
			ivjSaveFileButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveFileButton;
}
/**
 * Return the SaveMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getSaveFileMenuItem() {
	if (ivjSaveFileMenuItem == null) {
		try {
			ivjSaveFileMenuItem = new javax.swing.JMenuItem();
			ivjSaveFileMenuItem.setName("SaveFileMenuItem");
			ivjSaveFileMenuItem.setText("Save");
			ivjSaveFileMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveFileMenuItem;
}
/**
 * Return the SavePropertyButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSavePropertyButton() {
	if (ivjSavePropertyButton == null) {
		try {
			ivjSavePropertyButton = new javax.swing.JButton();
			ivjSavePropertyButton.setName("SavePropertyButton");
			ivjSavePropertyButton.setText("Save");
			ivjSavePropertyButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSavePropertyButton;
}
/**
 * Return the SelectAllAssertionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSelectAllAssertionsButton() {
	if (ivjSelectAllAssertionsButton == null) {
		try {
			ivjSelectAllAssertionsButton = new javax.swing.JButton();
			ivjSelectAllAssertionsButton.setName("SelectAllAssertionsButton");
			ivjSelectAllAssertionsButton.setText("Select All");
			ivjSelectAllAssertionsButton.setBackground(new java.awt.Color(204,204,255));
			ivjSelectAllAssertionsButton.setMaximumSize(new java.awt.Dimension(101, 25));
			ivjSelectAllAssertionsButton.setPreferredSize(new java.awt.Dimension(101, 25));
			ivjSelectAllAssertionsButton.setMinimumSize(new java.awt.Dimension(101, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSelectAllAssertionsButton;
}
/**
 * Return the SessionButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSessionButtonPanel() {
	if (ivjSessionButtonPanel == null) {
		try {
			ivjSessionButtonPanel = new javax.swing.JPanel();
			ivjSessionButtonPanel.setName("SessionButtonPanel");
			ivjSessionButtonPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Session"));
			ivjSessionButtonPanel.setLayout(new java.awt.GridBagLayout());
			ivjSessionButtonPanel.setBackground(new java.awt.Color(204,204,255));
			ivjSessionButtonPanel.setMaximumSize(new java.awt.Dimension(100, 10000));

			java.awt.GridBagConstraints constraintsNewSessionButton = new java.awt.GridBagConstraints();
			constraintsNewSessionButton.gridx = 0; constraintsNewSessionButton.gridy = 0;
			constraintsNewSessionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsNewSessionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSessionButtonPanel().add(getNewSessionButton(), constraintsNewSessionButton);

			java.awt.GridBagConstraints constraintsCloneSessionButton = new java.awt.GridBagConstraints();
			constraintsCloneSessionButton.gridx = 0; constraintsCloneSessionButton.gridy = 1;
			constraintsCloneSessionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsCloneSessionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSessionButtonPanel().add(getCloneSessionButton(), constraintsCloneSessionButton);

			java.awt.GridBagConstraints constraintsRemoveSessionButton = new java.awt.GridBagConstraints();
			constraintsRemoveSessionButton.gridx = 0; constraintsRemoveSessionButton.gridy = 2;
			constraintsRemoveSessionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsRemoveSessionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSessionButtonPanel().add(getRemoveSessionButton(), constraintsRemoveSessionButton);

			java.awt.GridBagConstraints constraintsActivateSessionButton = new java.awt.GridBagConstraints();
			constraintsActivateSessionButton.gridx = 0; constraintsActivateSessionButton.gridy = 3;
			constraintsActivateSessionButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsActivateSessionButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSessionButtonPanel().add(getActivateSessionButton(), constraintsActivateSessionButton);

			java.awt.GridBagConstraints constraintsShowSessionInformationButton = new java.awt.GridBagConstraints();
			constraintsShowSessionInformationButton.gridx = 0; constraintsShowSessionInformationButton.gridy = 4;
			constraintsShowSessionInformationButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSessionButtonPanel().add(getShowSessionInformationButton(), constraintsShowSessionInformationButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionButtonPanel;
}
/**
 */
private javax.swing.JFileChooser getSessionFileChooser() {

	// lazy init
	if(sessionFileChooser == null) {
		sessionFileChooser = new JFileChooser();
		sessionFileChooser.setFileFilter(new SessionFileFilter());
		sessionFileChooser.setMultiSelectionEnabled(false);
		sessionFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		sessionFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	}

	// use the last directory as this one's working directory
	if((lastFileChooser != null) && (lastFileChooser != sessionFileChooser)) {
		java.io.File currentDirectory = lastFileChooser.getCurrentDirectory();
		sessionFileChooser.setCurrentDirectory(currentDirectory);
	}

	lastFileChooser = sessionFileChooser;
	
	return(sessionFileChooser);
}
/**
 * Return the SessionInformationDialog property value.
 * @return javax.swing.JDialog
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JDialog getSessionInformationDialog() {
	if (ivjSessionInformationDialog == null) {
		try {
			ivjSessionInformationDialog = new javax.swing.JDialog();
			ivjSessionInformationDialog.setName("SessionInformationDialog");
			ivjSessionInformationDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			ivjSessionInformationDialog.setBounds(848, 325, 530, 240);
			ivjSessionInformationDialog.setTitle("Session Information");
			getSessionInformationDialog().setContentPane(getJDialogContentPane1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionInformationDialog;
}
/**
 * Return the SessionInformationOkButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSessionInformationOkButton() {
	if (ivjSessionInformationOkButton == null) {
		try {
			ivjSessionInformationOkButton = new javax.swing.JButton();
			ivjSessionInformationOkButton.setName("SessionInformationOkButton");
			ivjSessionInformationOkButton.setText("OK");
			ivjSessionInformationOkButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionInformationOkButton;
}
/**
 * Return the SessionInformationTabbedPane property value.
 * @return javax.swing.JTabbedPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTabbedPane getSessionInformationTabbedPane() {
	if (ivjSessionInformationTabbedPane == null) {
		try {
			ivjSessionInformationTabbedPane = new javax.swing.JTabbedPane();
			ivjSessionInformationTabbedPane.setName("SessionInformationTabbedPane");
			ivjSessionInformationTabbedPane.setTabPlacement(javax.swing.JTabbedPane.TOP);
			ivjSessionInformationTabbedPane.setMaximumSize(new java.awt.Dimension(10000, 10000));
			ivjSessionInformationTabbedPane.setMinimumSize(new java.awt.Dimension(500, 300));
			ivjSessionInformationTabbedPane.insertTab("General", null, getGeneralPane(), null, 0);
			ivjSessionInformationTabbedPane.setBackgroundAt(0, java.awt.Color.gray);
			ivjSessionInformationTabbedPane.setForegroundAt(0, java.awt.Color.black);
			ivjSessionInformationTabbedPane.insertTab("Application", null, getApplicationPane(), null, 1);
			ivjSessionInformationTabbedPane.insertTab("Components", null, getComponentsPane(), null, 2);
			ivjSessionInformationTabbedPane.insertTab("BIR Options", null, getResourceBoundsPane(), null, 3);
			ivjSessionInformationTabbedPane.insertTab("Specification", null, getSpecificationPanel(), null, 4);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionInformationTabbedPane;
}
/**
 * Get the SessionListCellRenderer for this view.  This will perform lazy initialization
 * if not initialized already.
 *
 * @return edu.ksu.cis.bandera.sessions.ui.gui.SessionListCellRenderer
 */
public SessionListCellRenderer getSessionListCellRenderer() {
	if(sessionListCellRenderer == null) {
		sessionListCellRenderer = new SessionListCellRenderer(sessionManager);
	}
	return(sessionListCellRenderer);
}
/**
 * Return the SessionListPopupMenu property value.
 * @return javax.swing.JPopupMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPopupMenu getSessionListPopupMenu() {
	if (ivjSessionListPopupMenu == null) {
		try {
			ivjSessionListPopupMenu = new javax.swing.JPopupMenu();
			ivjSessionListPopupMenu.setName("SessionListPopupMenu");
			ivjSessionListPopupMenu.setBackground(new java.awt.Color(204,204,255));
			ivjSessionListPopupMenu.add(getNewSessionPopupMenuItem());
			ivjSessionListPopupMenu.add(getCloneSessionPopupMenuItem());
			ivjSessionListPopupMenu.add(getRemoveSessionPopupMenuItem());
			ivjSessionListPopupMenu.add(getActivateSessionPopupMenuItem());
			ivjSessionListPopupMenu.add(getInfoSessionPopupMenuItem());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionListPopupMenu;
}
/**
 * Return the SessionListScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getSessionListScrollPane() {
	if (ivjSessionListScrollPane == null) {
		try {
			ivjSessionListScrollPane = new javax.swing.JScrollPane();
			ivjSessionListScrollPane.setName("SessionListScrollPane");
			getSessionListScrollPane().setViewportView(getSessionNameList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionListScrollPane;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 3:22:57 PM)
 * @return edu.ksu.cis.bandera.sessions.SessionManager
 */
public edu.ksu.cis.bandera.sessions.SessionManager getSessionManager() {
	return sessionManager;
}
/**
 * Return the SessionManagerViewJMenuBar property value.
 * @return javax.swing.JMenuBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuBar getSessionManagerViewJMenuBar() {
	if (ivjSessionManagerViewJMenuBar == null) {
		try {
			ivjSessionManagerViewJMenuBar = new javax.swing.JMenuBar();
			ivjSessionManagerViewJMenuBar.setName("SessionManagerViewJMenuBar");
			ivjSessionManagerViewJMenuBar.setBackground(new java.awt.Color(204,204,255));
			ivjSessionManagerViewJMenuBar.add(getFileMenu());
			ivjSessionManagerViewJMenuBar.add(getSessionMenu());
			ivjSessionManagerViewJMenuBar.add(getViewMenu());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionManagerViewJMenuBar;
}
/**
 * Return the SessionMenu property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getSessionMenu() {
	if (ivjSessionMenu == null) {
		try {
			ivjSessionMenu = new javax.swing.JMenu();
			ivjSessionMenu.setName("SessionMenu");
			ivjSessionMenu.setText("Session");
			ivjSessionMenu.setBackground(new java.awt.Color(204,204,255));
			ivjSessionMenu.add(getNewSessionMenuItem());
			ivjSessionMenu.add(getClonSessionMenuItem());
			ivjSessionMenu.add(getRemoveSessionMenuItem());
			ivjSessionMenu.add(getActivateSessionMenuItem());
			ivjSessionMenu.add(getShowSessionInformationMenuItem());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionMenu;
}
/**
 * Return the SessionNameList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getSessionNameList() {
	if (ivjSessionNameList == null) {
		try {
			ivjSessionNameList = new javax.swing.JList();
			ivjSessionNameList.setName("SessionNameList");
			ivjSessionNameList.setCellRenderer(getSessionListCellRenderer());
			ivjSessionNameList.setBounds(0, 0, 102, 446);
			ivjSessionNameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionNameList;
}
/**
 * Return the SessionNameListLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSessionNameListLabel() {
	if (ivjSessionNameListLabel == null) {
		try {
			ivjSessionNameListLabel = new javax.swing.JLabel();
			ivjSessionNameListLabel.setName("SessionNameListLabel");
			ivjSessionNameListLabel.setText("Session List");
			ivjSessionNameListLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSessionNameListLabel;
}
/**
 * Return the ShowHideSessionInformationButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getShowHideSessionInformationButton() {
	if (ivjShowHideSessionInformationButton == null) {
		try {
			ivjShowHideSessionInformationButton = new javax.swing.JButton();
			ivjShowHideSessionInformationButton.setName("ShowHideSessionInformationButton");
			ivjShowHideSessionInformationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/sessions/ui/gui/images/contractPaneIcon.gif")));
			ivjShowHideSessionInformationButton.setText("");
			ivjShowHideSessionInformationButton.setBackground(new java.awt.Color(204,204,255));
			ivjShowHideSessionInformationButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowHideSessionInformationButton;
}
/**
 * Return the ShowHideSessionToolbarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getShowHideSessionToolbarButton() {
	if (ivjShowHideSessionToolbarButton == null) {
		try {
			ivjShowHideSessionToolbarButton = new javax.swing.JButton();
			ivjShowHideSessionToolbarButton.setName("ShowHideSessionToolbarButton");
			ivjShowHideSessionToolbarButton.setText("");
			ivjShowHideSessionToolbarButton.setBackground(new java.awt.Color(204,204,255));
			ivjShowHideSessionToolbarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/sessions/ui/gui/images/contractPaneIcon.gif")));
			ivjShowHideSessionToolbarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowHideSessionToolbarButton;
}
/**
 * Return the ShowSessionInformationButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getShowSessionInformationButton() {
	if (ivjShowSessionInformationButton == null) {
		try {
			ivjShowSessionInformationButton = new javax.swing.JButton();
			ivjShowSessionInformationButton.setName("ShowSessionInformationButton");
			ivjShowSessionInformationButton.setText("Info");
			ivjShowSessionInformationButton.setBackground(new java.awt.Color(204,204,255));
			ivjShowSessionInformationButton.setMaximumSize(new java.awt.Dimension(81, 25));
			ivjShowSessionInformationButton.setPreferredSize(new java.awt.Dimension(81, 25));
			ivjShowSessionInformationButton.setMinimumSize(new java.awt.Dimension(81, 25));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowSessionInformationButton;
}
/**
 * Return the ShowSessionInformationMenuCheckBox property value.
 * @return javax.swing.JCheckBoxMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBoxMenuItem getShowSessionInformationMenuCheckBox() {
	if (ivjShowSessionInformationMenuCheckBox == null) {
		try {
			ivjShowSessionInformationMenuCheckBox = new javax.swing.JCheckBoxMenuItem();
			ivjShowSessionInformationMenuCheckBox.setName("ShowSessionInformationMenuCheckBox");
			ivjShowSessionInformationMenuCheckBox.setSelected(true);
			ivjShowSessionInformationMenuCheckBox.setText("Show Session Information");
			ivjShowSessionInformationMenuCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowSessionInformationMenuCheckBox;
}
/**
 * Return the ShowSessionInformationMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getShowSessionInformationMenuItem() {
	if (ivjShowSessionInformationMenuItem == null) {
		try {
			ivjShowSessionInformationMenuItem = new javax.swing.JMenuItem();
			ivjShowSessionInformationMenuItem.setName("ShowSessionInformationMenuItem");
			ivjShowSessionInformationMenuItem.setText("Info");
			ivjShowSessionInformationMenuItem.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowSessionInformationMenuItem;
}
/**
 * Return the ShowSessionToolBarMenuCheckBox property value.
 * @return javax.swing.JCheckBoxMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBoxMenuItem getShowSessionToolBarMenuCheckBox() {
	if (ivjShowSessionToolBarMenuCheckBox == null) {
		try {
			ivjShowSessionToolBarMenuCheckBox = new javax.swing.JCheckBoxMenuItem();
			ivjShowSessionToolBarMenuCheckBox.setName("ShowSessionToolBarMenuCheckBox");
			ivjShowSessionToolBarMenuCheckBox.setSelected(true);
			ivjShowSessionToolBarMenuCheckBox.setText("Show Session Toolbar");
			ivjShowSessionToolBarMenuCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowSessionToolBarMenuCheckBox;
}
/**
 * Return the SlicerCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getSlicerCheckBox() {
	if (ivjSlicerCheckBox == null) {
		try {
			ivjSlicerCheckBox = new javax.swing.JCheckBox();
			ivjSlicerCheckBox.setName("SlicerCheckBox");
			ivjSlicerCheckBox.setText("Slicer");
			ivjSlicerCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSlicerCheckBox;
}
/**
 * Return the SMVCheckerPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSMVCheckerPanel() {
	if (ivjSMVCheckerPanel == null) {
		try {
			ivjSMVCheckerPanel = new javax.swing.JPanel();
			ivjSMVCheckerPanel.setName("SMVCheckerPanel");
			ivjSMVCheckerPanel.setLayout(new java.awt.GridBagLayout());
			ivjSMVCheckerPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSMVRadioButton = new java.awt.GridBagConstraints();
			constraintsSMVRadioButton.gridx = 0; constraintsSMVRadioButton.gridy = 0;
			constraintsSMVRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSMVCheckerPanel().add(getSMVRadioButton(), constraintsSMVRadioButton);

			java.awt.GridBagConstraints constraintsSMVOptionsButton = new java.awt.GridBagConstraints();
			constraintsSMVOptionsButton.gridx = 1; constraintsSMVOptionsButton.gridy = 0;
			constraintsSMVOptionsButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSMVCheckerPanel().add(getSMVOptionsButton(), constraintsSMVOptionsButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSMVCheckerPanel;
}
/**
 * Return the SMVOptionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSMVOptionsButton() {
	if (ivjSMVOptionsButton == null) {
		try {
			ivjSMVOptionsButton = new javax.swing.JButton();
			ivjSMVOptionsButton.setName("SMVOptionsButton");
			ivjSMVOptionsButton.setText("Options");
			ivjSMVOptionsButton.setBackground(new java.awt.Color(204,204,255));
			ivjSMVOptionsButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSMVOptionsButton;
}
/**
 * Return the SMVRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getSMVRadioButton() {
	if (ivjSMVRadioButton == null) {
		try {
			ivjSMVRadioButton = new javax.swing.JRadioButton();
			ivjSMVRadioButton.setName("SMVRadioButton");
			ivjSMVRadioButton.setText("SMV");
			ivjSMVRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			getModelCheckerButtonGroup().add(ivjSMVRadioButton);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSMVRadioButton;
}
/**
 * 
 * @return
 */
private javax.swing.JLabel getSpecificationMessageLabel(){
	if(ivjSpecificationMessageLabel == null){
		try{
			ivjSpecificationMessageLabel = new JLabel();
			ivjSpecificationMessageLabel.setText("You must run JJJC before you can edit these properties.");
			ivjSpecificationMessageLabel.setName("SpecificationMessageLabel");
			ivjSpecificationMessageLabel.setForeground(new java.awt.Color(255,0,0));
		}	catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		
	}
	return ivjSpecificationMessageLabel;
}

/**
 * 
 * @return
 */
private javax.swing.JPanel getSpecificationPanel(){
	if (ivjSpecificationPanel == null) {
			try {
				ivjSpecificationPanel = new javax.swing.JPanel();
				ivjSpecificationPanel.setName("SpecificationPanel");
				ivjSpecificationPanel.setLayout(new java.awt.GridBagLayout());
				ivjSpecificationPanel.setBackground(new java.awt.Color(204,204,255));
				ivjSpecificationPanel.setEnabled(true);

			    java.awt.GridBagConstraints constraintsSpecificationMessageLabel = new java.awt.GridBagConstraints();
				constraintsSpecificationMessageLabel.gridx = 0; constraintsSpecificationMessageLabel.gridy = 0;
				constraintsSpecificationMessageLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
				//constraintsSpecificationMessageLabel.weightx = 1.0;
				constraintsSpecificationMessageLabel.weighty = 0.10;	
				constraintsSpecificationMessageLabel.insets = new java.awt.Insets(4, 4, 4, 4);
				getSpecificationPanel().add(getSpecificationMessageLabel(), constraintsSpecificationMessageLabel);
			
				java.awt.GridBagConstraints constraintsSpecificationDisplayPanel = new java.awt.GridBagConstraints();
				constraintsSpecificationDisplayPanel.gridx = 0; constraintsSpecificationDisplayPanel.gridy = 1;
				constraintsSpecificationDisplayPanel.fill = java.awt.GridBagConstraints.BOTH;	
				constraintsSpecificationDisplayPanel.weightx = 0.6;
				constraintsSpecificationDisplayPanel.weighty = 1.0;
				constraintsSpecificationDisplayPanel.insets = new java.awt.Insets(4, 4, 4, 4);
				getSpecificationPanel().add(getSpecificationDisplayPanel(), constraintsSpecificationDisplayPanel);
				recursivelySetEnabled(getSpecificationDisplayPanel(), false);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
	return ivjSpecificationPanel;

}
/**
 * Return the SpecificationPanel property value.
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getSpecificationDisplayPanel() {
	if (ivjSpecificationDisplayPanel == null) {
		try {
			ivjSpecificationDisplayPanel = new javax.swing.JPanel();
			ivjSpecificationDisplayPanel.setName("specificationDisplayPanel");
			ivjSpecificationDisplayPanel.setLayout(new java.awt.GridBagLayout());
			ivjSpecificationDisplayPanel.setBackground(new java.awt.Color(204,204,255));
			ivjSpecificationDisplayPanel.setEnabled(true);

			java.awt.GridBagConstraints constraintsAssertionPanel = new java.awt.GridBagConstraints();
			constraintsAssertionPanel.gridx = 0; constraintsAssertionPanel.gridy = 0;
			constraintsAssertionPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsAssertionPanel.weighty = 1.0;
			constraintsAssertionPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getSpecificationDisplayPanel().add(getAssertionPanel(), constraintsAssertionPanel);

			java.awt.GridBagConstraints constraintsPropertyPanel = new java.awt.GridBagConstraints();
			constraintsPropertyPanel.gridx = 1; constraintsPropertyPanel.gridy = 0;
			constraintsPropertyPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsPropertyPanel.weightx = 0.6;
			constraintsPropertyPanel.weighty = 1.0;
			constraintsPropertyPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getSpecificationDisplayPanel().add(getPropertyPanel(), constraintsPropertyPanel);
			
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSpecificationDisplayPanel;
}
/**
 * Return the SpinCheckerPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSpinCheckerPanel() {
	if (ivjSpinCheckerPanel == null) {
		try {
			ivjSpinCheckerPanel = new javax.swing.JPanel();
			ivjSpinCheckerPanel.setName("SpinCheckerPanel");
			ivjSpinCheckerPanel.setLayout(new java.awt.GridBagLayout());
			ivjSpinCheckerPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSpinRadioButton = new java.awt.GridBagConstraints();
			constraintsSpinRadioButton.gridx = 0; constraintsSpinRadioButton.gridy = 0;
			constraintsSpinRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSpinRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSpinCheckerPanel().add(getSpinRadioButton(), constraintsSpinRadioButton);

			java.awt.GridBagConstraints constraintsSpinOptionsButton = new java.awt.GridBagConstraints();
			constraintsSpinOptionsButton.gridx = 1; constraintsSpinOptionsButton.gridy = 0;
			constraintsSpinOptionsButton.anchor = java.awt.GridBagConstraints.EAST;
			constraintsSpinOptionsButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getSpinCheckerPanel().add(getSpinOptionsButton(), constraintsSpinOptionsButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSpinCheckerPanel;
}
/**
 * Return the SpinOptionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSpinOptionsButton() {
	if (ivjSpinOptionsButton == null) {
		try {
			ivjSpinOptionsButton = new javax.swing.JButton();
			ivjSpinOptionsButton.setName("SpinOptionsButton");
			ivjSpinOptionsButton.setText("Options");
			ivjSpinOptionsButton.setBackground(new java.awt.Color(204,204,255));
			ivjSpinOptionsButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSpinOptionsButton;
}
/**
 * Return the SpinRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getSpinRadioButton() {
	if (ivjSpinRadioButton == null) {
		try {
			ivjSpinRadioButton = new javax.swing.JRadioButton();
			ivjSpinRadioButton.setName("SpinRadioButton");
			//ivjSpinRadioButton.setSelected(true);
			ivjSpinRadioButton.setText("Spin");
			ivjSpinRadioButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			getModelCheckerButtonGroup().add(ivjSpinRadioButton);
			ivjSpinRadioButton.setSelected(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSpinRadioButton;
}
/**
 * Return the ThreadInstanceBoundAddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getThreadInstanceBoundAddButton() {
	if (ivjThreadInstanceBoundAddButton == null) {
		try {
			ivjThreadInstanceBoundAddButton = new javax.swing.JButton();
			ivjThreadInstanceBoundAddButton.setName("ThreadInstanceBoundAddButton");
			ivjThreadInstanceBoundAddButton.setText("+");
			ivjThreadInstanceBoundAddButton.setBackground(new java.awt.Color(204,204,255));
			ivjThreadInstanceBoundAddButton.setEnabled(true);
			// user code begin {1}
			ivjThreadInstanceBoundAddButton.setEnabled(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceBoundAddButton;
}
/**
 * Return the ThreadInstanceBoundHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getThreadInstanceBoundHelpButton() {
	if (ivjThreadInstanceBoundHelpButton == null) {
		try {
			ivjThreadInstanceBoundHelpButton = new javax.swing.JButton();
			ivjThreadInstanceBoundHelpButton.setName("ThreadInstanceBoundHelpButton");
			ivjThreadInstanceBoundHelpButton.setText("?");
			ivjThreadInstanceBoundHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjThreadInstanceBoundHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceBoundHelpButton;
}
/**
 * Return the ThreadInstanceBoundLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getThreadInstanceBoundLabel() {
	if (ivjThreadInstanceBoundLabel == null) {
		try {
			ivjThreadInstanceBoundLabel = new javax.swing.JLabel();
			ivjThreadInstanceBoundLabel.setName("ThreadInstanceBoundLabel");
			ivjThreadInstanceBoundLabel.setText("Default");
			ivjThreadInstanceBoundLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceBoundLabel;
}
/**
 * Return the ThreadInstanceBoundMaxTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getThreadInstanceBoundMaxTextField() {
	if (ivjThreadInstanceBoundMaxTextField == null) {
		try {
			ivjThreadInstanceBoundMaxTextField = new javax.swing.JTextField();
			ivjThreadInstanceBoundMaxTextField.setName("ThreadInstanceBoundMaxTextField");
			ivjThreadInstanceBoundMaxTextField.setText("<max>");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceBoundMaxTextField;
}
/**
 * Return the ThreadInstanceBoundPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getThreadInstanceBoundPanel() {
	if (ivjThreadInstanceBoundPanel == null) {
		try {
			ivjThreadInstanceBoundPanel = new javax.swing.JPanel();
			ivjThreadInstanceBoundPanel.setName("ThreadInstanceBoundPanel");
			ivjThreadInstanceBoundPanel.setPreferredSize(new java.awt.Dimension(109, 99));
			ivjThreadInstanceBoundPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(java.awt.Color.black, 2), "Thread Instance Bound"));
			ivjThreadInstanceBoundPanel.setLayout(new java.awt.GridBagLayout());
			ivjThreadInstanceBoundPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsThreadInstanceBoundHelpButton = new java.awt.GridBagConstraints();
			constraintsThreadInstanceBoundHelpButton.gridx = 0; constraintsThreadInstanceBoundHelpButton.gridy = 0;
			constraintsThreadInstanceBoundHelpButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getThreadInstanceBoundPanel().add(getThreadInstanceBoundHelpButton(), constraintsThreadInstanceBoundHelpButton);

			java.awt.GridBagConstraints constraintsThreadInstanceBoundLabel = new java.awt.GridBagConstraints();
			constraintsThreadInstanceBoundLabel.gridx = 1; constraintsThreadInstanceBoundLabel.gridy = 0;
			constraintsThreadInstanceBoundLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getThreadInstanceBoundPanel().add(getThreadInstanceBoundLabel(), constraintsThreadInstanceBoundLabel);

			java.awt.GridBagConstraints constraintsThreadInstanceBoundMaxTextField = new java.awt.GridBagConstraints();
			constraintsThreadInstanceBoundMaxTextField.gridx = 2; constraintsThreadInstanceBoundMaxTextField.gridy = 0;
			constraintsThreadInstanceBoundMaxTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsThreadInstanceBoundMaxTextField.weightx = 1.0;
			constraintsThreadInstanceBoundMaxTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getThreadInstanceBoundPanel().add(getThreadInstanceBoundMaxTextField(), constraintsThreadInstanceBoundMaxTextField);

			java.awt.GridBagConstraints constraintsThreadInstanceBoundAddButton = new java.awt.GridBagConstraints();
			constraintsThreadInstanceBoundAddButton.gridx = 0; constraintsThreadInstanceBoundAddButton.gridy = 1;
			constraintsThreadInstanceBoundAddButton.anchor = java.awt.GridBagConstraints.SOUTH;
			constraintsThreadInstanceBoundAddButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getThreadInstanceBoundPanel().add(getThreadInstanceBoundAddButton(), constraintsThreadInstanceBoundAddButton);

			java.awt.GridBagConstraints constraintsThreadInstanceBoundRemoveButton = new java.awt.GridBagConstraints();
			constraintsThreadInstanceBoundRemoveButton.gridx = 0; constraintsThreadInstanceBoundRemoveButton.gridy = 2;
			constraintsThreadInstanceBoundRemoveButton.anchor = java.awt.GridBagConstraints.NORTH;
			constraintsThreadInstanceBoundRemoveButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getThreadInstanceBoundPanel().add(getThreadInstanceBoundRemoveButton(), constraintsThreadInstanceBoundRemoveButton);

			java.awt.GridBagConstraints constraintsThreadInstanceTableScrollPane = new java.awt.GridBagConstraints();
			constraintsThreadInstanceTableScrollPane.gridx = 1; constraintsThreadInstanceTableScrollPane.gridy = 1;
			constraintsThreadInstanceTableScrollPane.gridwidth = 2;
constraintsThreadInstanceTableScrollPane.gridheight = 2;
			constraintsThreadInstanceTableScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsThreadInstanceTableScrollPane.weightx = 1.0;
			constraintsThreadInstanceTableScrollPane.weighty = 1.0;
			constraintsThreadInstanceTableScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getThreadInstanceBoundPanel().add(getThreadInstanceTableScrollPane(), constraintsThreadInstanceTableScrollPane);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceBoundPanel;
}
/**
 * Return the ThreadInstanceBoundRemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getThreadInstanceBoundRemoveButton() {
	if (ivjThreadInstanceBoundRemoveButton == null) {
		try {
			ivjThreadInstanceBoundRemoveButton = new javax.swing.JButton();
			ivjThreadInstanceBoundRemoveButton.setName("ThreadInstanceBoundRemoveButton");
			ivjThreadInstanceBoundRemoveButton.setText("-");
			ivjThreadInstanceBoundRemoveButton.setBackground(new java.awt.Color(204,204,255));
			ivjThreadInstanceBoundRemoveButton.setEnabled(true);
			// user code begin {1}
			ivjThreadInstanceBoundRemoveButton.setEnabled(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceBoundRemoveButton;
}
/**
 * Return the ThreadInstanceTable property value.
 * @return javax.swing.JTable
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTable getThreadInstanceTable() {
	if (ivjThreadInstanceTable == null) {
		try {
			ivjThreadInstanceTable = new javax.swing.JTable();
			ivjThreadInstanceTable.setName("ThreadInstanceTable");
			getThreadInstanceTableScrollPane().setColumnHeaderView(ivjThreadInstanceTable.getTableHeader());
			getThreadInstanceTableScrollPane().getViewport().setBackingStoreEnabled(true);
			ivjThreadInstanceTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			ivjThreadInstanceTable.setBounds(0, 0, 200, 200);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceTable;
}
/**
 * Return the ThreadInstanceTableScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getThreadInstanceTableScrollPane() {
	if (ivjThreadInstanceTableScrollPane == null) {
		try {
			ivjThreadInstanceTableScrollPane = new javax.swing.JScrollPane();
			ivjThreadInstanceTableScrollPane.setName("ThreadInstanceTableScrollPane");
			ivjThreadInstanceTableScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			ivjThreadInstanceTableScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			getThreadInstanceTableScrollPane().setViewportView(getThreadInstanceTable());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjThreadInstanceTableScrollPane;
}
/**
 * Return the UnselectedAllAssertionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getUnselectedAllAssertionsButton() {
	if (ivjUnselectedAllAssertionsButton == null) {
		try {
			ivjUnselectedAllAssertionsButton = new javax.swing.JButton();
			ivjUnselectedAllAssertionsButton.setName("UnselectedAllAssertionsButton");
			ivjUnselectedAllAssertionsButton.setText("Unselect All");
			ivjUnselectedAllAssertionsButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUnselectedAllAssertionsButton;
}
/**
 * Return the VariableBoundAddButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getVariableBoundAddButton() {
	if (ivjVariableBoundAddButton == null) {
		try {
			ivjVariableBoundAddButton = new javax.swing.JButton();
			ivjVariableBoundAddButton.setName("VariableBoundAddButton");
			ivjVariableBoundAddButton.setText("+");
			ivjVariableBoundAddButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			ivjVariableBoundAddButton.setEnabled(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVariableBoundAddButton;
}
/**
 * Return the VariableBoundRemoveButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getVariableBoundRemoveButton() {
	if (ivjVariableBoundRemoveButton == null) {
		try {
			ivjVariableBoundRemoveButton = new javax.swing.JButton();
			ivjVariableBoundRemoveButton.setName("VariableBoundRemoveButton");
			ivjVariableBoundRemoveButton.setText("-" + ivjVariableBoundRemoveButton.getText());
			ivjVariableBoundRemoveButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			ivjVariableBoundRemoveButton.setEnabled(false);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVariableBoundRemoveButton;
}
/**
 * Return the VariableBoundTable property value.
 * @return javax.swing.JTable
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTable getVariableBoundTable() {
	if (ivjVariableBoundTable == null) {
		try {
			ivjVariableBoundTable = new javax.swing.JTable();
			ivjVariableBoundTable.setName("VariableBoundTable");
			getVariableBoundTableScrollPane().setColumnHeaderView(ivjVariableBoundTable.getTableHeader());
			getVariableBoundTableScrollPane().getViewport().setBackingStoreEnabled(true);
			ivjVariableBoundTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			ivjVariableBoundTable.setBounds(0, 0, 200, 200);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVariableBoundTable;
}
/**
 * Return the VariableBoundTableScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getVariableBoundTableScrollPane() {
	if (ivjVariableBoundTableScrollPane == null) {
		try {
			ivjVariableBoundTableScrollPane = new javax.swing.JScrollPane();
			ivjVariableBoundTableScrollPane.setName("VariableBoundTableScrollPane");
			ivjVariableBoundTableScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			ivjVariableBoundTableScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			getVariableBoundTableScrollPane().setViewportView(getVariableBoundTable());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVariableBoundTableScrollPane;
}
/**
 * Return the ViewMenu property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getViewMenu() {
	if (ivjViewMenu == null) {
		try {
			ivjViewMenu = new javax.swing.JMenu();
			ivjViewMenu.setName("ViewMenu");
			ivjViewMenu.setText("View");
			ivjViewMenu.setBackground(new java.awt.Color(204,204,255));
			ivjViewMenu.add(getShowSessionToolBarMenuCheckBox());
			ivjViewMenu.add(getShowSessionInformationMenuCheckBox());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViewMenu;
}
/**
 * Return the WorkingDirectoryBrowseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getWorkingDirectoryBrowseButton() {
	if (ivjWorkingDirectoryBrowseButton == null) {
		try {
			ivjWorkingDirectoryBrowseButton = new javax.swing.JButton();
			ivjWorkingDirectoryBrowseButton.setName("WorkingDirectoryBrowseButton");
			ivjWorkingDirectoryBrowseButton.setText("Browse");
			ivjWorkingDirectoryBrowseButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWorkingDirectoryBrowseButton;
}
/**
 * Return the WorkingDirectoryHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getWorkingDirectoryHelpButton() {
	if (ivjWorkingDirectoryHelpButton == null) {
		try {
			ivjWorkingDirectoryHelpButton = new javax.swing.JButton();
			ivjWorkingDirectoryHelpButton.setName("WorkingDirectoryHelpButton");
			ivjWorkingDirectoryHelpButton.setText("?");
			ivjWorkingDirectoryHelpButton.setBackground(new java.awt.Color(204,204,255));
			ivjWorkingDirectoryHelpButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWorkingDirectoryHelpButton;
}
/**
 * Return the WorkingDirectoryLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getWorkingDirectoryLabel() {
	if (ivjWorkingDirectoryLabel == null) {
		try {
			ivjWorkingDirectoryLabel = new javax.swing.JLabel();
			ivjWorkingDirectoryLabel.setName("WorkingDirectoryLabel");
			ivjWorkingDirectoryLabel.setText("Working Directory");
			ivjWorkingDirectoryLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWorkingDirectoryLabel;
}
/**
 * Return the WorkingDirectoryTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getWorkingDirectoryTextField() {
	if (ivjWorkingDirectoryTextField == null) {
		try {
			ivjWorkingDirectoryTextField = new javax.swing.JTextField();
			ivjWorkingDirectoryTextField.setName("WorkingDirectoryTextField");
			ivjWorkingDirectoryTextField.setText("<working_directory>");
			ivjWorkingDirectoryTextField.setEditable(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWorkingDirectoryTextField;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	exception.printStackTrace(System.out);
}
/**
 * Comment
 */
public void includesAddButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	String include = JOptionPane.showInputDialog(this, "Please enter the type or package that you would like included.");
	if((include != null) && (include.length() > 0)) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			activeSession.addInclude(include);
			updateView();
		}
	}
}
/**
 * Comment
 */
public void includesList_MousePressed(java.awt.event.MouseEvent mouseEvent) {
	if(mouseEvent.isPopupTrigger()) {
		getIncludesListPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}
}
/**
 * Comment
 */
public void includesList_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
	if(mouseEvent.isPopupTrigger()) {
		getIncludesListPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}
}
/**
 */
public void includesRemoveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	JList includesList = getIncludesList();
	Object[] selectedValues = includesList.getSelectedValues();
	if((selectedValues != null) && (selectedValues.length > 0)) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			for(int i = 0; i < selectedValues.length; i++) {
				activeSession.removeInclude(selectedValues[i].toString());
			}
			updateView();
		}
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
	getInstanceBoundAddButton().addActionListener(ivjEventHandler);
	getInstanceBoundRemoveButton().addActionListener(ivjEventHandler);
	getSpinOptionsButton().addActionListener(ivjEventHandler);
	getDSpinOptionsButton().addActionListener(ivjEventHandler);
	getSMVOptionsButton().addActionListener(ivjEventHandler);
	getJPFOptionsButton().addActionListener(ivjEventHandler);
	getMainClassFileBrowseButton().addActionListener(ivjEventHandler);
	getClasspathAddButton().addActionListener(ivjEventHandler);
	getClasspathRemoveButton().addActionListener(ivjEventHandler);
	getIncludesAddButton().addActionListener(ivjEventHandler);
	getIncludesRemoveButton().addActionListener(ivjEventHandler);
	getWorkingDirectoryBrowseButton().addActionListener(ivjEventHandler);
	getResetDefaultsButton().addActionListener(ivjEventHandler);
	getSlicerCheckBox().addActionListener(ivjEventHandler);
	getAbstractionCheckBox().addActionListener(ivjEventHandler);
	getModelCheckerCheckBox().addActionListener(ivjEventHandler);
	getShowSessionInformationMenuCheckBox().addActionListener(ivjEventHandler);
	getShowSessionToolBarMenuCheckBox().addActionListener(ivjEventHandler);
	getLoadFileMenuItem().addActionListener(ivjEventHandler);
	getNewSessionButton().addActionListener(ivjEventHandler);
	getNewSessionMenuItem().addActionListener(ivjEventHandler);
	getCloneSessionButton().addActionListener(ivjEventHandler);
	getRemoveSessionButton().addActionListener(ivjEventHandler);
	getActivateSessionButton().addActionListener(ivjEventHandler);
	getClonSessionMenuItem().addActionListener(ivjEventHandler);
	getRemoveSessionMenuItem().addActionListener(ivjEventHandler);
	getActivateSessionMenuItem().addActionListener(ivjEventHandler);
	getLoadFileButton().addActionListener(ivjEventHandler);
	getSaveFileButton().addActionListener(ivjEventHandler);
	getSaveFileMenuItem().addActionListener(ivjEventHandler);
	getSaveAsFileButton().addActionListener(ivjEventHandler);
	getSaveAsFileMenuItem().addActionListener(ivjEventHandler);
	getNewFileButton().addActionListener(ivjEventHandler);
	getNewFileMenuItem().addActionListener(ivjEventHandler);
	getNewSessionPopupMenuItem().addActionListener(ivjEventHandler);
	getCloneSessionPopupMenuItem().addActionListener(ivjEventHandler);
	getRemoveSessionPopupMenuItem().addActionListener(ivjEventHandler);
	getActivateSessionPopupMenuItem().addActionListener(ivjEventHandler);
	getSessionNameList().addMouseListener(ivjEventHandler);
	getJPFRadioButton().addItemListener(ivjEventHandler);
	getSMVRadioButton().addItemListener(ivjEventHandler);
	getDSpinRadioButton().addItemListener(ivjEventHandler);
	getSpinRadioButton().addItemListener(ivjEventHandler);
	getIntegerBoundMaxTextField().addFocusListener(ivjEventHandler);
	getIntegerBoundMinTextField().addFocusListener(ivjEventHandler);
	getArrayBoundMaxTextField().addFocusListener(ivjEventHandler);
	getInstanceBoundMaxTextField().addFocusListener(ivjEventHandler);
	getThreadInstanceBoundMaxTextField().addFocusListener(ivjEventHandler);
	getThreadInstanceBoundAddButton().addActionListener(ivjEventHandler);
	getThreadInstanceBoundRemoveButton().addActionListener(ivjEventHandler);
	getOutputNameTextField().addFocusListener(ivjEventHandler);
	getWorkingDirectoryTextField().addFocusListener(ivjEventHandler);
	getDescriptionTextArea().addFocusListener(ivjEventHandler);
	getMainClassFileTextField().addFocusListener(ivjEventHandler);
	getAddClasspathPopupMenuItem().addActionListener(ivjEventHandler);
	getRemoveClasspathPopupMenuItem().addActionListener(ivjEventHandler);
	getClasspathList().addMouseListener(ivjEventHandler);
	getAddIncludePopupMenuItem().addActionListener(ivjEventHandler);
	getRemoveIncludesPopupMenuItem().addActionListener(ivjEventHandler);
	getIncludesList().addMouseListener(ivjEventHandler);
	getAddQuantificationButton().addActionListener(ivjEventHandler);
	getRemoveQuantificationButton().addActionListener(ivjEventHandler);
	getPatternNameComboBox().addActionListener(ivjEventHandler);
	getPatternScopeComboBox().addActionListener(ivjEventHandler);
	getSavePropertyButton().addActionListener(ivjEventHandler);
	getResetPropertyButton().addActionListener(ivjEventHandler);
	getExpandPropertyButton().addActionListener(ivjEventHandler);
	getSelectAllAssertionsButton().addActionListener(ivjEventHandler);
	getUnselectedAllAssertionsButton().addActionListener(ivjEventHandler);
	getVariableBoundRemoveButton().addActionListener(ivjEventHandler);
	getVariableBoundAddButton().addActionListener(ivjEventHandler);
	getAddVariableCancelButton().addActionListener(ivjEventHandler);
	getAddVariableOkButton().addActionListener(ivjEventHandler);
	getSessionInformationOkButton().addActionListener(ivjEventHandler);
	getShowSessionInformationButton().addActionListener(ivjEventHandler);
	getShowSessionInformationMenuItem().addActionListener(ivjEventHandler);
	getInfoSessionPopupMenuItem().addActionListener(ivjEventHandler);
	getShowHideSessionToolbarButton().addActionListener(ivjEventHandler);
	getShowHideSessionInformationButton().addActionListener(ivjEventHandler);
	getChooseFreeRadioButton().addActionListener(ivjEventHandler);
	getResourceBoundedRadioButton().addActionListener(ivjEventHandler);
	getExhaustiveRadioButton().addActionListener(ivjEventHandler);
}
    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
	try {
		// user code begin {1}

	    // set the default color for the selected tabs background color to our blue -tcw
	    UIManager.put("TabbedPane.selected", new java.awt.Color(204, 204, 255));

		// user code end
		setName("SessionManagerView");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setJMenuBar(getSessionManagerViewJMenuBar());
		setSize(807, 476);
		setVisible(true);
		setTitle("Session Manager");
		setContentPane(getJFrameContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	searchModeRadioButtonGroup = new ButtonGroup();
	searchModeRadioButtonGroup.add(getResourceBoundedRadioButton());
	searchModeRadioButtonGroup.add(getChooseFreeRadioButton());
	searchModeRadioButtonGroup.add(getExhaustiveRadioButton());

	patternParameterLabelMap = new HashMap();
	patternParameterTextFieldMap = new HashMap();
	assertionCheckBoxMap = new HashMap();
	allAssertionSet = new HashSet();

	sessionManager = SessionManager.getInstance();
	updateView();  //make sure this view matches the model	
	// user code end
}
/**
 * This will initialize the text field values to match the expressions stored in
 * the temporal property, tp, given.
 * 
 * @param tp edu.ksu.cis.bandera.sessions.TemporalProperty
 */
private void initParameterTextFields(TemporalProperty tp) {

    // for each parameter in the pattern, get the JLabel and JTextField.  Set the
    //  current predicate value in the JTextField.

    Set predicates = tp.getPredicates();
    Map predicateMap = new HashMap(predicates.size());
    Iterator predicateIterator = predicates.iterator();
    while (predicateIterator.hasNext()) {
        edu.ksu.cis.bandera.sessions.Predicate predicate =
            (edu.ksu.cis.bandera.sessions.Predicate) predicateIterator.next();
        predicateMap.put(predicate.getName(), predicate.getExpression());
    }

    Pattern p = tp.getPattern();
    if (p != null) {
        Vector parameters = p.getParametersOccurenceOrder();
        Iterator pi = parameters.iterator();
        while (pi.hasNext()) {
            String parameterName = (String) pi.next();
			JLabel l = getParameterLabel(parameterName);
			JTextField t = getParameterTextField(parameterName);
			String expression = (String)predicateMap.get(parameterName);
			t.setText(expression);
        }
    }
}
/**
 * Insert the given text into the active parameter text field.
 *
 * @param String text The text to insert after the cursor.
 */
private void insertParameterText(String text) {

	JTextField tf = getActiveParameterTextField();
	if(tf != null) {
		String existingText = tf.getText();
		int caretPosition = tf.getCaretPosition();
		StringBuffer sb = new StringBuffer();
		sb.append(existingText.substring(0, caretPosition));
		sb.append(text);
		sb.append(existingText.substring(caretPosition));
		tf.setText(sb.toString());
	}
}
/**
 * Comment
 */
public void instanceBoundAddButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	JPanel classPanel = new JPanel();
	classPanel.setLayout(new java.awt.GridLayout(2,2));
	JLabel classNameLabel = new JLabel("Class");
	classPanel.add(classNameLabel);
	// if the application has not been compiled, give the
	//  user the ability to hand-enter a class name -> This is dangerous! -tcw
	JTextField classNameTextField = null;
	JComboBox classNameComboBox = null;
	if((classSet == null) || (classSet.size() < 1)) {
		classNameTextField = new JTextField("ClassName", 20);
		classPanel.add(classNameTextField);
	}
	else {
		classNameComboBox = new JComboBox(classSet.toArray());
		classPanel.add(classNameComboBox);
	}
	JLabel classBoundLabel = new JLabel("Upper Bound");
	classPanel.add(classBoundLabel);
	JTextField classBoundTextField = new JTextField("<max>", 5);
	classPanel.add(classBoundTextField);

	int response = JOptionPane.showConfirmDialog(this, classPanel, "Instance Bound", JOptionPane.OK_CANCEL_OPTION);
	if(response == JOptionPane.OK_OPTION) {
		String className = "";
		if(classNameTextField != null) {
			className = classNameTextField.getText();
		}
		else {
			className = classNameComboBox.getSelectedItem().toString();
		}
	 	String classBoundString = classBoundTextField.getText();
	 	try {
	 		Integer classBound = Integer.valueOf(classBoundString);
	 		Session activeSession = sessionManager.getActiveSession();
	 		if(activeSession != null) {
		 		BIROptions birOptions = activeSession.getBIROptions();
		 		if(birOptions != null) {
		 			edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
		 			if(rbs != null) {
		 				rbs.setAllocMax(className, classBound.intValue());
		 				updateView();
		 			}
		 		}
	 		}
	 	}
	 	catch(NumberFormatException nfe) {
		 	JOptionPane.showMessageDialog(this, "The number provided as the max for class " +
			 	className + " is not valid: " + classBoundString);
		 	return;
	 	}
	}
}
/**
 * Comment
 */
public void instanceBoundMaxTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String allocMaxString = getInstanceBoundMaxTextField().getText();
	if((allocMaxString != null) && (allocMaxString.length() > 0)) {
		try {
			Integer allocMax = Integer.valueOf(allocMaxString);
			Session activeSession = sessionManager.getActiveSession();
			if(activeSession != null) {
				BIROptions birOptions = activeSession.getBIROptions();
				if(birOptions != null) {
					edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
					if(rbs != null) {
						rbs.setDefaultAllocMax(allocMax.intValue());
					}
				}
			}
		}
		catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "The number given, " +
				allocMaxString + ", is not a valid number.");
		}
	}
}
/**
 * When the user presses the remove button it should remove the selected
 * classes max from the model.
 */
public void instanceBoundRemoveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	JTable table = getInstanceBoundTable();
	int[] selectedIndices = table.getSelectedRows();
	if((selectedIndices != null) && (selectedIndices.length > 0)) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			BIROptions birOptions = activeSession.getBIROptions();
			if(birOptions != null) {
				edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
				if(rbs != null) {
					for(int i = 0; i < selectedIndices.length; i++) {
						String className = (String)table.getValueAt(selectedIndices[i], 0);
						rbs.removeAllocMax(className);
					}
				}
			}
		}
		updateView();
	}
}
/**
 * When the user moves away from this field, update the model
 * using the value provided.
 */
public void integerBoundMaxTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String integerMaxString = getIntegerBoundMaxTextField().getText();
	if((integerMaxString != null) && (integerMaxString.length() > 0)) {
		try {
			Integer integerMax = Integer.valueOf(integerMaxString);
			Session activeSession = sessionManager.getActiveSession();
			if(activeSession != null) {
				BIROptions birOptions = activeSession.getBIROptions();
				if(birOptions != null) {
					edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
					if(rbs != null) {
						rbs.setDefaultIntMax(integerMax.intValue());
					}
				}
			}
		}
		catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "The number given, " +
				integerMaxString + ", is not a valid number.");
		}
	}
}
/**
 * Comment
 */
public void integerBoundMinTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String integerMinString = getIntegerBoundMinTextField().getText();
	if((integerMinString != null) && (integerMinString.length() > 0)) {
		try {
			Integer integerMin = Integer.valueOf(integerMinString);
			Session activeSession = sessionManager.getActiveSession();
			if(activeSession != null) {
				BIROptions birOptions = activeSession.getBIROptions();
				if(birOptions != null) {
					edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
					if(rbs != null) {
						rbs.setDefaultIntMin(integerMin.intValue());
					}
				}
			}
		}
		catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "The number given, " +
				integerMinString + ", is not a valid number.");
		}
	}
}
/**
 * Comment
 */
public void jPFOptionsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
    String checkerOptions = "";
    Session activeSession = sessionManager.getActiveSession();
    if (activeSession != null) {
        checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
    }

    OptionsView optionsView = OptionsFactory.createOptionsViewInstance("JPF");
    Options options = OptionsFactory.createOptionsInstance("JPF");
    options.init(checkerOptions);
    optionsView.init(options);
	optionsView.registerCompletionListener(this);
    optionsView.setVisible(true);
    
}
/**
 * When the selected state of the JPF radio button changes,
 * we need to make sure that the enabled state of the JPF Options button
 * matches: if JPF is selected, the JPF options button is enabled.
 * If JPF is selected, we need to update the model as well.
 */
public void jPFRadioButton_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	boolean enabled = getJPFRadioButton().isSelected();
	getJPFOptionsButton().setEnabled(enabled);

	//disable/enable the BIR strategy buttons
	getChooseFreeRadioButton().setEnabled(!enabled);
	getResourceBoundedRadioButton().setEnabled(!enabled);
	getExhaustiveRadioButton().setEnabled(!enabled);
	
	if(enabled) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			activeSession.setProperty(Session.CHECKER_NAME_PROPERTY,
				Session.JPF_CHECKER_NAME_PROPERTY);
		}
	}
	updateEnabledStatus();
}
    /**
     * This will handle all user requests to load a session file.  This will
     * prompt the user for a File to load and then use the model to load it.  If
     * the model has not saved it's data to file yet (isModified), we should prompt
     * the user to see if we should save it first.
     */
    public void loadFileAction() {

	//log.debug("starting to load ...");
	//Warning!  sessionManager.hasChanged is used for observables and has nothing to do with 
	//whether or not the sessionManager has changed since it was last saved.  (almost always returns false) mo
	if(sessionManager.hasChanged()) {
	    int response = JOptionPane.showConfirmDialog(this, "The SessionManager currently has unsaved changes." +
							 "\nWould you like to save it now (before losing those changes)?", "Save Changes?",
							 JOptionPane.YES_NO_OPTION);
	    if(response == JOptionPane.YES_OPTION) {
		try {
		    sessionManager.save();
		}
		catch(Exception e) {
		    JOptionPane.showMessageDialog(this, "Problems were encountered while saving.  Please try saving before loading.");
		    return;
		}
	    }
	}
	
	sessionManager.clearSessions();
	
	JFileChooser fileChooser = getSessionFileChooser();
	int choice = fileChooser.showOpenDialog(this);
	if(choice == JFileChooser.APPROVE_OPTION) {
	    java.io.File file = fileChooser.getSelectedFile();
	    try {
		sessionManager.load(file);
		saveable = true;
	    }
	    catch(Exception e) {
		JOptionPane.showMessageDialog(this, "There were problems while loading the session file:" +
					      "\nException: " + e.toString());
		e.printStackTrace(System.err);
		return;
	    }
	    clearCompiledData();
	    updateView();
	}
    }
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		SessionManagerView aSessionManagerView;
		aSessionManagerView = new SessionManagerView();
		aSessionManagerView.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aSessionManagerView.show();
		java.awt.Insets insets = aSessionManagerView.getInsets();
		aSessionManagerView.setSize(aSessionManagerView.getWidth() + insets.left + insets.right, aSessionManagerView.getHeight() + insets.top + insets.bottom);
		aSessionManagerView.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
/**
 */
public void mainClassFileBrowseButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	JFileChooser fc = getJavaFileChooser();
	if(fc != null) {
		int response = fc.showOpenDialog(this);
		if(response == JFileChooser.APPROVE_OPTION) {
			java.io.File file = fc.getSelectedFile();
			Session activeSession = sessionManager.getActiveSession();
			if(activeSession != null) {
				activeSession.setMainClassFile(file);
				updateView();
			}
		}
	}
}
/**
 * When the user changes the main class file in the view, update
 * the model to match.
 */
public void mainClassFileTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String mainClassString = getMainClassFileTextField().getText();
	java.io.File mainClass = new java.io.File(mainClassString);
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
		activeSession.setMainClassFile(mainClass);
		updateView();
	}
}
/**
 * When the user changes the state of the model checker component
 * check box, the model should be updated to match the status of the
 * check box.
 */
public void modelCheckerCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	boolean enabled = getModelCheckerCheckBox().isSelected();
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
		activeSession.setModelCheckingEnabled(enabled);
	}
}
/**
 * The newFileAction provides a way to create a new session model.
 */
public void newFileAction() {

	//Warning!  sessionManager.hasChanged is used for observables and has nothing to do with 
	//whether or not the sessionManager has changed since it was last saved.  (almost always returns false) mo
	if(sessionManager.hasChanged()) {
		int response = JOptionPane.showConfirmDialog(this, "There are unsaved changes, do you want to lose them?", "Save changes?", JOptionPane.YES_NO_OPTION);
		if(response == JOptionPane.NO_OPTION) {
			return;
		}
	}

	sessionManager.clearSessions();
	// this should really call something like sessionManager.reset();
	clearCompiledData();
	saveable = false;
	updateView();
}
/**
 * Comment
 */
public void newMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	newSessionAction();
}
/**
 * This method will perform the actions needed to add a new session to
 * the model (SessionManager).  It will prompt the user for the new session
 * name.  It will then make sure a session doesn't already exist in the model
 * with the specified session name (if so, we should prompt the user to overwrite).
 * If all is ok, we will create a new Session with the given name and add it to the
 * model.  We will also activate this session (so that a user can go right to editing
 * the new session).
 */
public void newSessionAction() {

	// create the panels to use in the dialog box (JOptionPane)	
	JPanel newSessionNamePanel = new JPanel();
	newSessionNamePanel.setLayout(new java.awt.GridBagLayout());
	JLabel newSessionNameLabel = new JLabel("New Session Name");
	newSessionNameLabel.setForeground(java.awt.Color.black);
	JTextField newSessionNameTextField = new JTextField("NewSession", 20);
	newSessionNamePanel.add(newSessionNameLabel);
	newSessionNamePanel.add(newSessionNameTextField);

	// get the user's response
	int response = JOptionPane.showConfirmDialog(this, newSessionNamePanel, "Create a New Session", JOptionPane.OK_CANCEL_OPTION);

	// if the user clicks ok
	if(response == JOptionPane.OK_OPTION) {
		String newSessionID = newSessionNameTextField.getText();
		
		// we should validate the newSessionID -tcw

		// check to see if a session exists in the model already with this name	
		Session session = sessionManager.getSession(newSessionID);
		if(session != null) {
			// since a session with this name exists, ask the user if we should over-write it
			response = JOptionPane.showConfirmDialog(this, "A session with the name " + newSessionID + " already exists." +
				"\n\nWould you like to overwrite it?", "Overwrite existing session?", JOptionPane.YES_NO_OPTION);
			if(response == JOptionPane.NO_OPTION) {
				return;
			}
		}
		
		session = new Session();
		session.setName(newSessionID);
		try {
			sessionManager.addSession(session);
			sessionManager.setActiveSession(session);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "An exception occured while creating a new session: " + e.toString(),
				"Exception while creating a New Session.", JOptionPane.OK_OPTION);
			return;
		}
		clearCompiledData();
		updateView();
		selectBIRDefaults();
	}
}
/**
 * When the user has edited the output name we need to update
 * the model to match the user's given value.
 */
public void outputNameTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String outputName = getOutputNameTextField().getText();
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
		activeSession.setOutputName(outputName);
		updateView();
	}
}
/**
 * When one of the pattern combo boxes changes, we should update the
 * pattern text field based upon the selected pattern.  If the pattern
 * selection is invalid, display an error message in the pattern text
 * field.
 */
public void patternChangeAction() {

    Pattern pattern;
    JComboBox patternNameComboBox = getPatternNameComboBox();
    JComboBox patternScopeComboBox = getPatternScopeComboBox();
    Object selectedPatternNameObject = patternNameComboBox.getSelectedItem();
    Object selectedPatternScopeObject = patternScopeComboBox.getSelectedItem();
    String patternName = "";
    String patternScope = "";
    if (selectedPatternNameObject != null) {
        patternName = selectedPatternNameObject.toString();
    }
    if (selectedPatternScopeObject != null) {
        patternScope = selectedPatternScopeObject.toString();
    }
    pattern = PatternSaverLoader.getPattern(patternName, patternScope);

	updatePatternTextField(pattern);
	updatePatternParameterPanel(pattern);
}
/**
 * Comment
 */
public void removeMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	removeSessionAction();
}
/**
 * When the user selects the remove quantification button, we should
 * present them with a confirmation screen that allows them to confirm
 * their desire to delete rows from the quantification table.  This will
 * actually delete the quantified variables from the underlying model instead
 * of taking them from the table itself.
 */
public void removeQuantificationButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	int response = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete the selected quantified variables?", "Delete quantified variables ...", JOptionPane.YES_NO_OPTION);
	if(response == JOptionPane.YES_OPTION) {
		JTable qt = getQuantificationTable();
		javax.swing.table.TableModel tm = qt.getModel();
		if((tm != null) && (tm instanceof DefaultTableModel)) {
			DefaultTableModel dtm = (DefaultTableModel)tm;
			int[] selectedRows = qt.getSelectedRows();
			if(selectedRows != null) {
				for(int i = 0; i < selectedRows.length; i++) {
					dtm.removeRow(selectedRows[i]);
				}
			}
		}
	}
}
/**
 * The removeSessionAction will perform the necessary actions to remove
 * a session from the model.  If no session is selected in the list, just
 * return.  If a session is selected, prompt the user to make sure they
 * wish to remove it.  Only after prompting should we remove it.
 */
public void removeSessionAction() {

	// get the selected session name
	Object selectedValue = getSessionNameList().getSelectedValue();
	if(selectedValue != null) {
		String sessionID = selectedValue.toString();
		int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove the session named " + sessionID + "?",
			"Confirm Session Removal", JOptionPane.YES_NO_OPTION);
		if(response == JOptionPane.YES_OPTION) {
			sessionManager.removeSession(sessionID);
			clearCompiledData();
			updateView();
		}
	}
	
}
/**
 * When the user clicks the reset button, we should reset all the bounds
 * to the default values.  This includes the int max, int min, array max,
 * default alloc max, default thread max, and clear the tables of thread and
 * alloc max values.
 */
public void resetDefaultsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
		BIROptions birOptions = activeSession.getBIROptions();
		if(birOptions != null) {
			edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
			if(rbs != null) {
				rbs.useDefaultValues();
				updateView();
			}
		}
	}
}
/**
 * When the user selects the reset button, the specification should
 * be reset the the last saved specification.  So we should clear the current
 * view of the configured spec and replace it with the spec that is
 * defined in the active session.
 */
public void resetPropertyButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	clearExpandedPropertyTextArea();
    SessionManager sm = SessionManager.getInstance();
    Session activeSession = sm.getActiveSession();
    if (activeSession != null) {
        Specification spec = activeSession.getSpecification();
        if (spec != null) {
            TemporalProperty tp = spec.getTemporalProperty();
            if (tp != null) {
	            updateTemporalPropertyPanel(tp);
            }
        }
    }
}
/**
 * Comment
 */
public void resourceBoundedRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	SessionManager sm = SessionManager.getInstance();
	Session activeSession = sm.getActiveSession();
	if(activeSession != null) {
		BIROptions birOptions = activeSession.getBIROptions();
		if(birOptions != null) {
			birOptions.setSearchMode(BIROptions.RESOURCE_BOUNDED_MODE);
		}
	}
}
/**
 * The saveAsFileAction provides the functionality to save the current model
 * to a file of the user's choice.  This will get a file to save to from the user
 * and try to save the session information to it.  If the file exists, it will
 * prompt the user to see if they want to overwrite it.
 */
public void saveAsFileAction() {

	java.io.File file = null;
	boolean done = false;
	while(!done) {
		try {
			JFileChooser fileChooser = getSessionFileChooser();
			int response = fileChooser.showSaveDialog(this);
			if(response == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				sessionManager.saveAs(file);
				saveable = true;
				done = true;
			}
			else {
				done = true;
			}
		}
		catch(edu.ksu.cis.bandera.sessions.parser.FileExistsException fee) {
			int answer = JOptionPane.showConfirmDialog(this, "The file you selected already exists.  Would you like to overwrite it?", "Overwrite?",
				JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION) {
				try {
					sessionManager.saveAs(file, true);
					saveable = true;
					done = true;
				}
				catch(edu.ksu.cis.bandera.sessions.parser.FileNotWritableException fnwe) {
					JOptionPane.showMessageDialog(this, "The file you chose is not writable.  Please choose again.");
					done = false;
				}
				catch(java.io.FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(this, "The file you chose was not found.  Please choose again.");
					done = false;
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(this, "An error occured while trying to overwrite the file.");
					done = false;
				}
			}
		}
		catch(edu.ksu.cis.bandera.sessions.parser.FileNotWritableException fnwe) {
			JOptionPane.showMessageDialog(this, "The file you chose is not writable.  Please choose again.");
			done = false;
		}
		catch(java.io.FileNotFoundException fnfe) {
			JOptionPane.showMessageDialog(this, "The file you chose was not found.  Please choose again.");
			done = false;
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "An error occured while trying to save.");
			done = false;
		}
	}
	updateView();
}
/**
 */
public void saveFileAction() {

	try {
	    /* this needs to be re-worked. -tcw
	    if(sessionManager.hasChanged()) {
		sessionManager.save();
	    }
	    */
	    sessionManager.save();
	    saveable = true;
	}
	catch(java.io.FileNotFoundException fnfe) {
		JOptionPane.showMessageDialog(this, "We could not save this session file because the file is not found.  Please select Save As instead.");
	}
	catch(edu.ksu.cis.bandera.sessions.SessionException se) {
		JOptionPane.showMessageDialog(this, "Something bad happened that we cannot recover from while trying to save the file." +
			"\nPlease report this problem to the Santos Laboratory.");
		System.err.println("Exception: " + se.toString());
		se.printStackTrace(System.err);
	}
}
/**
 * When the user presses the save button, we need to take the current
 * configured specification from the view and write it to the model.  This
 * method will parse the values in the quantification table and create
 * the appropriate Quantification object.  It will parse the pattern
 * combo boxes and set the appropriate Pattern object.  It will also
 * create the appropriate Predicate objects based upon the predicate text
 * field values.  It will do all this after it checks to make sure this
 * is a valid specification.
 */
public void savePropertyButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	SessionManager sm = SessionManager.getInstance();
	Session activeSession = sm.getActiveSession();
	if(activeSession != null) {
		Specification specification = activeSession.getSpecification();
		if(specification == null) {
			specification = new Specification();
			activeSession.setSpecification(specification);
		}
		
		TemporalProperty temporalProperty = new TemporalProperty();
		specification.setTemporalProperty(temporalProperty);
		String patternName = getPatternNameComboBox().getSelectedItem().toString();
		String patternScope = getPatternScopeComboBox().getSelectedItem().toString();
		Pattern pattern = PatternSaverLoader.getPattern(patternName, patternScope);
		if(pattern != null) {
			temporalProperty.setPattern(pattern);

			Vector parameters = pattern.getParametersOccurenceOrder();
			Iterator pi = parameters.iterator();
			while(pi.hasNext()) {
				String parameterName = (String)pi.next();
				JTextField textField = getParameterTextField(parameterName);
				String expression = textField.getText();
				edu.ksu.cis.bandera.sessions.Predicate predicate =
					new edu.ksu.cis.bandera.sessions.Predicate();
				predicate.setName(parameterName);
				predicate.setExpression(expression);
				temporalProperty.addPredicate(predicate);
			}
		}

		JTable qt = getQuantificationTable();
		TableModel tm = qt.getModel();
		int rowCount = tm.getRowCount();
		if(rowCount > 0) {
			Quantification quantification = new Quantification();
			for(int i = 0; i < rowCount; i++) {
				String variableName = tm.getValueAt(i, 0).toString();
				String typeName = tm.getValueAt(i, 1).toString();
				QuantifiedVariable qv = new QuantifiedVariable();
				qv.setName(variableName);
				qv.setType(typeName);
				quantification.addQuantifiedVariable(qv);
			}
			temporalProperty.setQuantification(quantification);
		}
	}
}
/**
 * When the select all button is pressed, we need to select all
 * the assertion check boxes.
 */
public void selectAllAssertionsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	selectAssertions(true);
}

/**
 * Selected/enable or deselect/disable the named assertion in the model and view.
 *
 * @param String assertionName The name of the assertion to select/enable.
 * @param boolean selected True if the assertion should be selected/enabled, false otherwise.
 */
private void selectAssertion(String assertionName, boolean selected) {
	
	// walk through all the assertions in the allAssertionSet to find the assertion
	//  matching the given name.  once found, enable/disable it.
	if((allAssertionSet != null) && (allAssertionSet.size() > 0)) {
	    Iterator aai = allAssertionSet.iterator();
	    while(aai.hasNext()) {
		Assertion assertion = (Assertion)aai.next();
		if(assertion.getName().equals(assertionName)) {
		    // found the assertion
		    selectAssertion(assertion, selected);
		    break;
		}
	    }
	}
}

    /**
     * Set the enabled flag in the given assertion in the model and in the view to
     * match the selected flag given.  If the assertion is not in the session's spec already,
     * then add it as well.
     *
     * @param Assertion assertion The assertion to modify.
     * @param boolean selected True if the assertion should be enabled, false otherwise.
     */
    private void selectAssertion(Assertion assertion, boolean selected) {

	if(assertion == null) {
	    log.error("Cannot select a null assertion.");
	}

	SessionManager sessionManager = SessionManager.getInstance();
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession == null) {
	    log.warn("There was no active session in which to enable assertions.");
		return;
	}

	Specification spec = activeSession.getSpecification();
	if(spec == null) {
	    log.warn("There was no specfication in which to enable assertions so one was created.");
	    spec = new Specification();
	    activeSession.setSpecification(spec);
	}

	assertion.setEnabled(selected);
	if(selected) {
	    spec.addAssertion(assertion);
	}
	JCheckBox checkBox = (JCheckBox)assertionCheckBoxMap.get(assertion.getName());
	if(checkBox == null) {
	    log.warn("There was no check box to match the given assertion: " + assertion.getName());
	}
	else {
	    checkBox.setSelected(selected);
	}
    }
/**
 * Select or deselect all the assertion check boxes that are currently displayed.  Also
 * set the enabled flag on the assertions in the current specification.
 *
 * @param boolean selected True if all check boxes should be selected, false otherwise.
 */
private void selectAssertions(boolean selected) {

    // for each assertion in the allAssertionSet, set the selected
    if((allAssertionSet != null) && (allAssertionSet.size() > 0)) {
	Iterator aai = allAssertionSet.iterator();
	while(aai.hasNext()) {
	    Assertion assertion = (Assertion)aai.next();
	    selectAssertion(assertion, selected);
	}
    }

}
/**
 * When the add button is pressed, a new window will pop-up and prompt
 * the user for the name of the new session.
 */
public void sessionAddButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	return;
}
/**
 * Comment
 */
public void sessionAddButton_ActionPerformed1(java.awt.event.ActionEvent actionEvent) {
	newSessionAction();
}
/**
 * When the user presses the clone button, a new window will pop-up.  If
 * a session ID is selected in the list of session IDs, the new window will
 * only prompt for the new name.  If a session ID is not selected in the list
 * of session IDs, it will also prompt for the session to clone.
 */
public void sessionCloneButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	return;
}
/**
 * Comment
 */
public void sessionCloneButton_ActionPerformed1(java.awt.event.ActionEvent actionEvent) {
	cloneSessionAction();
}
/**
 * When the mouse is pressed we should see if it is signalling the
 * pop-up menu (a.k.a. right-click menu).  If so, display the
 * session list pop-up menu.
 */
public void sessionNameList_MousePressed(java.awt.event.MouseEvent mouseEvent) {
	if(mouseEvent.isPopupTrigger()) {
		getSessionListPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}
	if(mouseEvent.getClickCount() == 2) {
		activateSessionAction();
	}
}
/**
 * When the mouse is released we should see if it is signalling the
 * pop-up menu (a.k.a. right-click menu).  If so, display the
 * session list pop-up menu.
 */
public void sessionNameList_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
	if(mouseEvent.isPopupTrigger()) {
		getSessionListPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
	}
}
/**
 * When the user presses the remove button, a new window will pop-up to
 * confirm the choice of sessions to remove from the list.  If no session ID
 * is selected, nothing will happen.
 */
public void sessionRemoveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	return;
}
/**
 * Comment
 */
public void sessionRemoveButton_ActionPerformed1(java.awt.event.ActionEvent actionEvent) {
	removeSessionAction();
}
/**
 * Insert the method's description here.
 * Creation date: (9/12/2002 4:25:05 PM)
 * @param newAbstractionTreeModel javax.swing.tree.TreeModel
 */
private void setAbstractionTreeModel(TreeModel newAbstractionTreeModel) {
	abstractionTreeModel = newAbstractionTreeModel;
}
/**
 * Insert the method's description here.
 * Creation date: (10/17/2002 11:39:40 AM)
 * @param newActiveParameterTextField javax.swing.JTextField
 */
private void setActiveParameterTextField(javax.swing.JTextField newActiveParameterTextField) {
	activeParameterTextField = newActiveParameterTextField;
}
/**
 * Insert the method's description here.
 * Creation date: (8/15/2002 2:25:32 PM)
 * @param newBirSearchModeButtonGroup javax.swing.ButtonGroup
 */
private void setBirSearchModeButtonGroup(javax.swing.ButtonGroup newBirSearchModeButtonGroup) {
	birSearchModeButtonGroup = newBirSearchModeButtonGroup;
}
/**
 * When the user creates a new session, we should set all the bounds
 * to the default values.  This includes the int max, int min, array max,
 * default alloc max, default thread max, and clear the tables of thread and
 * alloc max values.
 */
public void selectBIRDefaults() {
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
		BIROptions birOptions = activeSession.getBIROptions();
		if(birOptions != null) {
			edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
			if(rbs != null) {
				rbs.useDefaultValues();
				ivjSpinRadioButton.setSelected(true);
				updateView();
			}
		}
	}
}
/**
 * @param newClasspathFileChooser javax.swing.JFileChooser
 */
void setClasspathFileChooser(javax.swing.JFileChooser newClasspathFileChooser) {
	classpathFileChooser = newClasspathFileChooser;
}
/**
 * Set the collection of classes that can be used to configure abstraction.
 */
public void setClassSet(java.util.Set newClassSet) {
	classSet = newClassSet;
	updateView();
}
/**
 * @param newDirectoryFileChooser javax.swing.JFileChooser
 */
void setDirectoryFileChooser(javax.swing.JFileChooser newDirectoryFileChooser) {
	directoryFileChooser = newDirectoryFileChooser;
}
/**
 * @param newJavaFileChooser javax.swing.JFileChooser
 */
void setJavaFileChooser(javax.swing.JFileChooser newJavaFileChooser) {
	javaFileChooser = newJavaFileChooser;
}
/**
 * Insert the method's description here.
 * Creation date: (8/12/2002 3:50:36 PM)
 * @param newModelCheckerButtonGroup javax.swing.ButtonGroup
 */
public void setModelCheckerButtonGroup(javax.swing.ButtonGroup newModelCheckerButtonGroup) {
	modelCheckerButtonGroup = newModelCheckerButtonGroup;
}
/**
 * @param newSessionFileChooser javax.swing.JFileChooser
 */
void setSessionFileChooser(javax.swing.JFileChooser newSessionFileChooser) {
	sessionFileChooser = newSessionFileChooser;
}
/**
 * This method will set the visiblility of the Session information panel
 * (the panel in which the user can modify the session information).  This will
 * also update the check box state and the icon for the button so that they are
 * consistent (since they are supposed to show if the panel is visible or not, both
 * should convey the same information!).
 *
 * @param visible boolean
 */
public void setSessionInformationPanelVisible(boolean visible) {
	
	getSessionInformationTabbedPane().setVisible(visible);
	pack();
	
	JButton button = getShowHideSessionInformationButton();
	JCheckBoxMenuItem checkBox = getShowSessionInformationMenuCheckBox();
	checkBox.setSelected(visible);
	if(visible) {
		// icon for button should be contraction
		button.setIcon(CONTRACTED_PANE_ICON);
	}
	else {
		// icon for button should be expansion
		button.setIcon(EXPANDED_PANE_ICON);
	}
}
/**
 * Set a new model for this view.  This will cause an update to the view (so that
 * the view always matches the model) as well as other components that rely on an
 * up-to-date model (such as the SessionListCellRenderer).
 *
 * @param newSessionManager edu.ksu.cis.bandera.sessions.SessionManager
 */
public void setSessionManager(edu.ksu.cis.bandera.sessions.SessionManager newSessionManager) {
	sessionManager = newSessionManager;
	getSessionListCellRenderer().setSessionManager(sessionManager);
	updateView();
}
/**
 * This method will set the visiblility of the Session button panel
 * (the panel in which the user can add/remove/clone/activate sessions).
 *
 * @param visible boolean
 */
public void setSessionToolbarVisible(boolean visible) {
	
	getButtonPanel().setVisible(visible);
	pack();
	
	JButton button = getShowHideSessionToolbarButton();
	JCheckBoxMenuItem checkBox = getShowSessionToolBarMenuCheckBox();
	checkBox.setSelected(visible);
	if(visible) {
		// icon for button should be contraction
		button.setIcon(CONTRACTED_PANE_ICON);
	}
	else {
		// icon for button should be expansion
		button.setIcon(EXPANDED_PANE_ICON);
	}

}
/**
 * Comment
 */
public void showHideSessionInformationButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	boolean visible = getSessionInformationTabbedPane().isVisible();
	setSessionInformationPanelVisible(!visible);
}
/**
 * When the hide/show session toolbar button is pressed, toggle the
 * visibility of the session toolbar and change the text/icon for
 * the button.
 */
public void showHideSessionToolbarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	boolean visible = getButtonPanel().isVisible();
	setSessionToolbarVisible(!visible);
}
/**
 * When the information button or menu item is pressed, we should open the
 * session information dialog box.  This box will hold a summary of the
 * active session.
 */
public void showSessionInformationAction() {
	updateInfo();
	JDialog d = getSessionInformationDialog();
	d.setVisible(true);
}
/**
 * Comment
 */
public void showSessionInformationMenuCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	setSessionInformationPanelVisible(getShowSessionInformationMenuCheckBox().isSelected());
}
/**
 * Comment
 */
public void showSessionToolBarMenuCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	setSessionToolbarVisible(getShowSessionToolBarMenuCheckBox().isSelected());
}
/**
 * When the slicer check box changes state, the model should be updated
 * to match.
 */
public void slicerCheckBox_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	boolean enabled = getSlicerCheckBox().isSelected();
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
	    activeSession.setSlicingEnabled(enabled);
	}
}
/**
 * Comment
 */
public void sMVOptionsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
    String checkerOptions = "";
    Session activeSession = sessionManager.getActiveSession();
    if (activeSession != null) {
        checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
    }

    OptionsView optionsView = OptionsFactory.createOptionsViewInstance("SMV");
    Options options = OptionsFactory.createOptionsInstance("SMV");
    options.init(checkerOptions);
    optionsView.init(options);
    optionsView.registerCompletionListener(this);
    optionsView.setVisible(true);
    
}
/**
 * When the selected state of the SMV radio button changes,
 * we need to make sure that the enabled state of the SMV Options button
 * matches: if SMV is selected, the SMV options button is enabled.
 * If SMV is selected, we need to update the model as well.
 */
public void sMVRadioButton_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	boolean enabled = getSMVRadioButton().isSelected();
	getSMVOptionsButton().setEnabled(enabled);
	if(enabled) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			activeSession.setProperty(Session.CHECKER_NAME_PROPERTY,
				Session.SMV_CHECKER_NAME_PROPERTY);
		}
	}
}
/**
 * When the Spin Options button is pressed, we should show the Spin Options view
 * so that the user can configure the options using the GUI view.
 */
public void spinOptionsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
    String checkerOptions = "";
    Session activeSession = sessionManager.getActiveSession();
    if (activeSession != null) {
        checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
    }

    OptionsView optionsView = OptionsFactory.createOptionsViewInstance("Spin");
    Options options = OptionsFactory.createOptionsInstance("Spin");
    options.init(checkerOptions);
    optionsView.init(options);
    optionsView.registerCompletionListener(this);
    optionsView.setVisible(true);
    
}
/**
 * When the selected state of the Spin radio button changes,
 * we need to make sure that the enabled state of the Options button
 * matches: if spin is selected, the spin options button is enabled.
 * If spin is selected, we need to update the model as well.
 */
public void spinRadioButton_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	boolean enabled = getSpinRadioButton().isSelected();
	getSpinOptionsButton().setEnabled(enabled);
	if(enabled) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			activeSession.setProperty(Session.CHECKER_NAME_PROPERTY,
				Session.SPIN_CHECKER_NAME_PROPERTY);
		}
	}
}
/**
 * Comment
 */
public void threadInstanceBoundAddButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
	JPanel threadPanel = new JPanel();
	threadPanel.setLayout(new java.awt.GridLayout(2,2));
	JLabel threadNameLabel = new JLabel("Thread");
	threadPanel.add(threadNameLabel);
	JTextField threadNameTextField = new JTextField("ThreadName", 20);
	threadPanel.add(threadNameTextField);
	JLabel threadBoundLabel = new JLabel("Upper Bound");
	threadPanel.add(threadBoundLabel);
	JTextField threadBoundTextField = new JTextField("<max>", 5);
	threadPanel.add(threadBoundTextField);

	int response = JOptionPane.showConfirmDialog(this, threadPanel, "Thread Bound", JOptionPane.OK_CANCEL_OPTION);
	if(response == JOptionPane.OK_OPTION) {
		String threadName = threadNameTextField.getText();
	 	String threadBoundString = threadBoundTextField.getText();
	 	try {
	 		Integer threadBound = Integer.valueOf(threadBoundString);
	 		Session activeSession = sessionManager.getActiveSession();
	 		if(activeSession != null) {
				BIROptions birOptions = activeSession.getBIROptions();
				if(birOptions != null) {
		 			edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
		 			if(rbs != null) {
		 				rbs.setThreadMax(threadName, threadBound.intValue());
		 				updateView();
		 			}
				}
	 		}
	 	}
	 	catch(NumberFormatException nfe) {
		 	JOptionPane.showMessageDialog(this, "The number provided as the max for thread " +
			 	threadName + " is not valid: " + threadBoundString);
		 	return;
	 	}
	}
}
/**
 * Comment
 */
public void threadInstanceBoundMaxTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String threadMaxString = getThreadInstanceBoundMaxTextField().getText();
	if((threadMaxString != null) && (threadMaxString.length() > 0)) {
		try {
			Integer threadMax = Integer.valueOf(threadMaxString);
			Session activeSession = sessionManager.getActiveSession();
			if(activeSession != null) {
				BIROptions birOptions = activeSession.getBIROptions();
				if(birOptions != null) {
					edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
					if(rbs != null) {
						rbs.setDefaultThreadMax(threadMax.intValue());
					}
				}
			}
		}
		catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "The number given, " +
				threadMaxString + ", is not a valid number.");
		}
	}
}
/**
 * Comment
 */
public void threadInstanceBoundRemoveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	JTable table = getThreadInstanceTable();
	int[] selectedIndices = table.getSelectedRows();
	if((selectedIndices != null) && (selectedIndices.length > 0)) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			BIROptions birOptions = activeSession.getBIROptions();
			if(birOptions != null) {
				edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
				if(rbs != null) {
					for(int i = 0; i < selectedIndices.length; i++) {
						String threadName = (String)table.getValueAt(selectedIndices[i], 0);
						rbs.removeThreadMax(threadName);
					}
				}
			}
		}
		updateView();
	}
}
/**
 * Toggle the current selected/enabled state for the assertion specified.
 *
 * @param String assertionName The name of the assertion to toggle.
 */
private void toggleAssertion(String assertionName) {
	
	SessionManager sessionManager = SessionManager.getInstance();
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession == null) {
		return;
	}
	Specification spec = activeSession.getSpecification();
	if(spec == null) {
		return;
	}
	Set assertions = spec.getAssertions();
	if(assertions == null) {
		return;
	}
	Assertion assertion = null;
	Iterator ai = assertions.iterator();
	while(ai.hasNext()) {
		Assertion currentAssertion = (Assertion)ai.next();
		if(currentAssertion.getName().equals(assertionName)) {
			assertion = currentAssertion;
			break;
		}
	}
	if(assertion == null) {
		return;
	}
	boolean selected = assertion.enabled();
	assertion.setEnabled(!selected);

	JCheckBox checkBox = (JCheckBox)assertionCheckBoxMap.get(assertionName);
	if(checkBox == null) {
		return;
	}
	checkBox.setSelected(!selected);
}
/**
 * When the user presses the unselect all button, we should deselect
 * all the assertion check boxes.
 */
public void unselectedAllAssertionsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	selectAssertions(false);
}
    /**
     * When the model changes, we should update the view.
     */
    public void update(Observable o, Object arg) {
	//updateView();
    }
/**
 */
public void updateInfo() {
	
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession == null) {
		return;
	}
	
	JTextArea infoTextArea = getInfoTextArea();
	infoTextArea.setText(activeSession.toString());
	infoTextArea.setCaretPosition(0);
	
}

    /**
     * Initialize the pattern combo boxes with the patterns that exist in the
     * PatternSaverLoader.
     */
    private void initPatternComboBoxes() {

	Set patternNameSet = new HashSet();
	Set patternScopeSet = new HashSet();

	Hashtable patternNameTable = PatternSaverLoader.getPatternTable();
	if((patternNameTable != null) && (patternNameTable.size() > 0)) {
	    Iterator pnti = patternNameTable.keySet().iterator();
	    while (pnti.hasNext()) {
		String patternName = (String) pnti.next();
		patternNameSet.add(patternName);
		log.debug("Added the patternName " + patternName + " to the set.");

		Hashtable patternScopeTable = (Hashtable) patternNameTable.get(patternName);
		if((patternScopeTable != null) && (patternScopeTable.size() > 0)) {
		    Iterator psti = patternScopeTable.keySet().iterator();
		    while (psti.hasNext()) {
			String patternScope = (String) psti.next();
			patternScopeSet.add(patternScope);
			log.debug("Added the patternScope " + patternScope + " to the set.");
		    }
		}
		else {
		    log.info("There are no pattern scopes from the pattern named " +
			     patternName + " from the PatternSaverLoader.");
		}
	    }
	}
	else {
	    log.info("There are no pattern names available from the PatternSaverLoader.");
	}

	JComboBox patternNameComboBox = getPatternNameComboBox();
	patternNameComboBox.removeAllItems();
	Iterator pnsi = patternNameSet.iterator();
	while (pnsi.hasNext()) {
	    patternNameComboBox.addItem(pnsi.next());
	}
    
	JComboBox patternScopeComboBox = getPatternScopeComboBox();
	patternScopeComboBox.removeAllItems();
	Iterator pssi = patternScopeSet.iterator();
	while (pssi.hasNext()) {
	    patternScopeComboBox.addItem(pssi.next());
	}

    }

    /**
     * Set the selected pattern name and scope for the combo boxes on the
     * specification tab.  Use the given name and scope.  If either of
     * the names are null or blank nothing will happen.  If the name or scope
     * is not in the list, the combo box will not change state.
     *
     * @param String patternName The name to select.
     * @param String patternScope The scope to select.
     */
    private void selectPatternComboBoxes(String patternName, String patternScope) {

	if((patternName == null) || (patternName.length() <= 0) ||
	   (patternScope == null) || (patternScope.length() <= 0)) {
	    log.warn("Cannot select a pattern without a name and a scope.");
	    return;
	}

	JComboBox patternNameComboBox = getPatternNameComboBox();
	patternNameComboBox.setSelectedItem(patternName);
	JComboBox patternScopeComboBox = getPatternScopeComboBox();
	patternScopeComboBox.setSelectedItem(patternScope);

    }

    /**
     * Set the selected pattern name and scope for the combo boxes on the
     * specification tab.  Use the given pattern.  
     *
     * @param Pattern pattern The name to select.
     */
    private void selectPatternComboBoxes(Pattern pattern) {

	if(pattern == null) {
	    log.warn("Cannot select a null pattern.");
	    return;
	}

	String patternName = pattern.getName();
	String patternScope = pattern.getScope();
	selectPatternComboBoxes(patternName, patternScope);

    }

/**
 * Update the pattern combo boxes and mark the given pattern name and pattern scope as
 * selected.
 *
 * @param selectedPatternName java.lang.String The pattern name to be set as selected.
 * @param selectedPatternScope java.lang.String The pattern scope to be set as selected.
 */
private void updatePatternComboBoxes(String selectedPatternName, String selectedPatternScope) {

    // update the pattern name and scope combo boxes
    Set patternNameSet = new HashSet();
    Set patternScopeSet = new HashSet();
    Hashtable patternNameTable = PatternSaverLoader.getPatternTable();
    Iterator pnti = patternNameTable.keySet().iterator();
    while (pnti.hasNext()) {
        String patternName = (String) pnti.next();
        patternNameSet.add(patternName);
        Hashtable patternScopeTable = (Hashtable) patternNameTable.get(patternName);
        Iterator psti = patternScopeTable.keySet().iterator();
        while (psti.hasNext()) {
            String patternScope = (String) psti.next();
            patternScopeSet.add(patternScope);
        }
    }
    JComboBox patternNameComboBox = getPatternNameComboBox();
    patternNameComboBox.removeAllItems();
    Iterator pnsi = patternNameSet.iterator();
    while (pnsi.hasNext()) {
        patternNameComboBox.addItem(pnsi.next());
    }
    if((selectedPatternName != null) && (selectedPatternName.length() > 0)) {
	patternNameComboBox.setSelectedItem(selectedPatternName);
    }
    
    JComboBox patternScopeComboBox = getPatternScopeComboBox();
    patternScopeComboBox.removeAllItems();
    Iterator pssi = patternScopeSet.iterator();
    while (pssi.hasNext()) {
        patternScopeComboBox.addItem(pssi.next());
    }
    if((selectedPatternScope != null) && (selectedPatternScope.length() > 0)) {
	patternScopeComboBox.setSelectedItem(selectedPatternScope);
    }
}
/**
 * Update the pattern paramters panel to include the labels and text fields
 * that match the given pattern.
 * 
 * @param pattern edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern
 */
private void updatePatternParameterPanel(Pattern pattern) {

    if(pattern == null) {
	return;
    }

    // create the parameter panels and set the proper values
    JPanel patternParameterInsidePanel = getPatternParameterInsidePanel();
    patternParameterInsidePanel.removeAll();

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.insets = new java.awt.Insets(2, 2, 2, 2);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.fill = java.awt.GridBagConstraints.NONE;

    Vector parameters = pattern.getParametersOccurenceOrder();
    if((parameters == null) || (parameters.size() <= 0)) {
	return;
    }
    Iterator pi = parameters.iterator();
    while (pi.hasNext()) {
        String parameterName = (String) pi.next();

        // add the label
        JLabel label = getParameterLabel(parameterName);
        patternParameterInsidePanel.add(label, gbc);

        // modify the constraints for the text field
        gbc.gridx++;
        gbc.weightx = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        // add the text field
        JTextField textField = getParameterTextField(parameterName);
        patternParameterInsidePanel.add(textField, gbc);

        // modify the constraints for the expression builder button
        gbc.gridx++;
        gbc.weightx = 0.0;
        gbc.fill = java.awt.GridBagConstraints.NONE;

	// add a button to use the ExpressionBuilder to generate this expression
	JButton expressionBuilderButton = new JButton("Build " + parameterName);
	expressionBuilderButton.setActionCommand(parameterName);
	ActionListener ac = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    String parameterName = ae.getActionCommand();
		    ExpressionBuilder eb = getExpressionBuilder();
		    eb.setPredicateSet(getPredicateSet());
		    JTextField textField = getParameterTextField(parameterName);
		    String currentExpression = textField.getText();
		    if((currentExpression != null) &&
		       (currentExpression.length() > 0)) {
			eb.setExpressionString(currentExpression);
		    }
		    eb.setVisible(true);
		    setCurrentExpressionBuilderParameter(parameterName);
		}
	    };
	expressionBuilderButton.addActionListener(ac);
	patternParameterInsidePanel.add(expressionBuilderButton, gbc);

        // reset the constraints for the next label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.fill = java.awt.GridBagConstraints.NONE;

    }
    getPatternParameterScrollPane().validate();
}
/**
 * Update the pattern text field to use the given pattern format.
 *
 * @param pattern edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern
 */
private void updatePatternTextField(Pattern pattern) {
	JTextField patternTextField = getPatternTextField();
	if(pattern != null) {
		patternTextField.setText(pattern.getFormat());
	}
	else {
		patternTextField.setText("Error: Pattern not found in system.");
	}
}
/**
 * Update the quantification table using the given quantification object.
 *
 * @param quantification edu.ksu.cis.bandera.sessions.Quantification
 */
private void updateQuantificationTable(Quantification quantification) {

    Vector rowData = null;
    Vector columnNames = new Vector(2);
    columnNames.add("Variable");
    columnNames.add("Type");
    JTable qt = getQuantificationTable();

    if (quantification != null) {
        Set quantifiedVariables = quantification.getQuantifiedVariables();
        rowData = new Vector(quantifiedVariables.size());
        Iterator qvi = quantifiedVariables.iterator();
        while (qvi.hasNext()) {
            QuantifiedVariable qv = (QuantifiedVariable) qvi.next();
            Vector v = new Vector(2);
            v.add(qv.getName());
            v.add(qv.getType());
            rowData.add(v);
        }
    }
    else {
        rowData = new Vector(0);
    }

    javax.swing.table.DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(rowData, columnNames);
    qt.setModel(dtm);
}
/**
 * Update the temporal property panel using the given temporal property.  This will update
 * the quantification table, the pattern combo boxes, the pattern text field, and the
 * pattern parameter panel.
 *
 * @param tp edu.ksu.cis.bandera.sessions.TemporalProperty
 */
private void updateTemporalPropertyPanel(TemporalProperty tp) {
	
    Quantification q = tp.getQuantification();
    updateQuantificationTable(q);

    Pattern pattern = tp.getPattern();
    if (pattern != null) {
        //String patternName = pattern.getName();
        //String patternScope = pattern.getScope();
        //updatePatternComboBoxes(patternName, patternScope);
	//selectPatternComboBoxes(patternName, patternScope);
	selectPatternComboBoxes(pattern);
    }
    
    updatePatternTextField(pattern);

    initParameterTextFields(tp);
    updatePatternParameterPanel(pattern);
}

/**
 *  This method allows us to enable or disable any container (most likely a jPanel)
 *  and all of it's children recursively.  We need this because the standard setEnable
 *  method does not affect the children of a given component.
 *
 */
public void recursivelySetEnabled(Container container, boolean enableStatus){
	Component[] components = container.getComponents();
	int numOfComps = container.getComponentCount();
	for (int i = 0;  i < numOfComps ; i++ ){
		components[i].setEnabled(enableStatus);
		
		if( (new Container()).getClass().isAssignableFrom(components[i].getClass()) ){
				recursivelySetEnabled((Container)components[i], enableStatus);
		}
		
	}
	
	
	}
/**
 * This method will update all the values in this view based upon the currently
 * set SessionManager (view) and active Session.
 */
public void updateView() {

    if (sessionManager == null) {
        log.warn("There is no model for this view.");
        return;
    }

    // clear the view to make room for the updated data
    clearView();

    // update the session list
    Map sessionMap = sessionManager.getSessions();
    JList sessionList = getSessionNameList();
    if ((sessionMap == null) || (sessionMap.size() <= 0)) {

        // should we put a "No Session" session in the list? -tcw

        log.info("There are no sessions in the model.");

        sessionList.setListData(new Object[0]); // clear the list

		updateEnabledStatus();
        return;
    }
    else {
        java.util.Set sessionNameSet = sessionMap.keySet();
        Object[] sessionNames = sessionNameSet.toArray();
        sessionList.setListData(sessionNames);
    }

    Session activeSession = sessionManager.getActiveSession();
    if (activeSession == null) {
        log.info("There is no active session in this model.");
        // should we disable the tabs at this point? -tcw
	// should we clear all the tabs at this point? -tcw

		updateEnabledStatus();
        return;
    }

    // general: output, working directory, description
    JTextArea descTextArea = getDescriptionTextArea();
    descTextArea.setText(activeSession.getDescription());
    JTextField outputTextField = getOutputNameTextField();
    outputTextField.setText(activeSession.getOutputName());
    JTextField workingDirectoryTextField = getWorkingDirectoryTextField();
    File wd = activeSession.getWorkingDirectory();
    if(wd != null) {
	workingDirectoryTextField.setText(wd.toString());
    }
		
    // application: main class, classpath, includes
    JTextField mainClassTextField = getMainClassFileTextField();
    File mainClassFile = activeSession.getMainClassFile();
    if(mainClassFile != null) {
	mainClassTextField.setText(mainClassFile.toString());
    }
    else {
	mainClassTextField.setText("");
    }
    JList classpathList = getClasspathList();
    List cl = activeSession.getClasspath();
    if((cl != null) && (cl.size() > 0)) {
	classpathList.setListData(cl.toArray());
    }
    JList includesList = getIncludesList();
    List il = activeSession.getIncludes();
    if((il != null) && (il.size() > 0)) {
	includesList.setListData(il.toArray());
    }

    // components: slicer, abstraction, model checking, model checker, checker options
    boolean enabled = false;

    JCheckBox slicerCheckBox = getSlicerCheckBox();
    enabled = activeSession.isSlicingEnabled();
    //log.debug("slicing enabled? " + enabled);
    slicerCheckBox.setSelected(enabled);

    JCheckBox abstractionCheckBox = getAbstractionCheckBox();
    enabled = activeSession.isAbstractionEnabled();
    //log.debug("abstraction enabled? " + enabled);
    abstractionCheckBox.setSelected(enabled);

    JCheckBox modelCheckingCheckBox = getModelCheckerCheckBox();
    enabled = activeSession.isModelCheckingEnabled();
    //log.debug("model checking enabled? " + enabled);
    modelCheckingCheckBox.setSelected(enabled);

    /* We will allow the user to configure model checking without having it enabled. If
     * this is not the desired behaviour, you can use some of the following code to disable/enable
     * the radio buttons accordingly. -tcw
    getSpinRadioButton().setEnabled(enabled);
    getDSpinRadioButton().setEnabled(enabled);
    getSMVRadioButton().setEnabled(enabled);
    getJPFRadioButton().setEnabled(enabled);
    getModelCheckerOptionsPanel().setEnabled(enabled);
 	*/

    String checkerName = activeSession.getProperty(Session.CHECKER_NAME_PROPERTY);
    if ((checkerName != null) && (checkerName.length() > 0)) {
        if (checkerName.equals(Session.SPIN_CHECKER_NAME_PROPERTY)) {
            // enable the spin radio button -> since it is in a button group, it should disable the others
            getSpinRadioButton().setSelected(true);
        }
        if (checkerName.equals(Session.DSPIN_CHECKER_NAME_PROPERTY)) {
            // enable the dspin radio button -> since it is in a button group, it should disable the others
            getDSpinRadioButton().setSelected(true);
        }
        if (checkerName.equals(Session.SMV_CHECKER_NAME_PROPERTY)) {
            // enable the smv radio button -> since it is in a button group, it should disable the others
            getSMVRadioButton().setSelected(true);
        }
        if (checkerName.equals(Session.JPF_CHECKER_NAME_PROPERTY)) {
            // enable the jpf radio button -> since it is in a button group, it should disable the others
            getJPFRadioButton().setSelected(true);
        }
    }

    // resource bounds: int max, int min, array max, instance max, thread max
    BIROptions birOptions = activeSession.getBIROptions();
    if (birOptions != null) {
        edu.ksu.cis.bandera.sessions.ResourceBounds rb = birOptions.getResourceBounds();
        if (rb != null) {
            JTextField intMaxTextField = getIntegerBoundMaxTextField();
            intMaxTextField.setText(String.valueOf(rb.getIntMax()));
            JTextField intMinTextField = getIntegerBoundMinTextField();
            intMinTextField.setText(String.valueOf(rb.getIntMin()));
            // update the int bounds table
            JTable varBoundTable = getVariableBoundTable();
            Vector columnNames = new Vector(3);
            columnNames.add("Variable");
            columnNames.add("Min");
            columnNames.add("Max");
            Vector rowData = new Vector();
            Map flatVariableMap = rb.getFlatVariableBoundMap();
            if ((flatVariableMap != null) && (flatVariableMap.size() > 0)) {
                Iterator fvmi = flatVariableMap.keySet().iterator();
                while (fvmi.hasNext()) {
                    String fullName = (String) fvmi.next();
                    List l = (List) flatVariableMap.get(fullName);
                    Integer minInteger = (Integer) l.get(0);
                    Integer maxInteger = (Integer) l.get(1);
                    Vector v = new Vector(3);
                    v.add(fullName);
                    v.add(minInteger);
                    v.add(maxInteger);
                    rowData.add(v);
                }
            }
            DefaultTableModel dtm = new DefaultTableModel(rowData, columnNames);
            varBoundTable.setModel(dtm);

            JTextField arrayMaxTextField = getArrayBoundMaxTextField();
            arrayMaxTextField.setText(String.valueOf(rb.getArrayMax()));

            JTextField instanceMaxTextField = getInstanceBoundMaxTextField();
            instanceMaxTextField.setText(String.valueOf(rb.getDefaultAllocMax()));
            java.util.Hashtable instanceBoundHashtable = rb.getAllocMaxTable();
            java.util.Vector columnNameVector = new java.util.Vector(2);
            columnNameVector.add("Class");
            columnNameVector.add("Max");
            if ((instanceBoundHashtable != null) && (instanceBoundHashtable.size() > 0)) {
                java.util.Vector dataVector =
                    new java.util.Vector(instanceBoundHashtable.size());
                Iterator i = instanceBoundHashtable.keySet().iterator();
                while (i.hasNext()) {
                    String className = (String) i.next();
                    Integer classMax = (Integer) instanceBoundHashtable.get(className);
                    java.util.Vector tempVector = new java.util.Vector(2);
                    tempVector.add(className);
                    tempVector.add(classMax);
                    dataVector.add(tempVector);
                }

                javax.swing.table.DefaultTableModel tableModel =
                    new javax.swing.table.DefaultTableModel(dataVector, columnNameVector);
                getInstanceBoundTable().setModel(tableModel);
            }
            else {
                javax.swing.table.DefaultTableModel tableModel =
                    new javax.swing.table.DefaultTableModel(
                        new java.util.Vector(0),
                        columnNameVector);
                getInstanceBoundTable().setModel(tableModel);
            }

            JTextField threadMaxTextField = getThreadInstanceBoundMaxTextField();
            threadMaxTextField.setText(String.valueOf(rb.getDefaultThreadMax()));
            java.util.Hashtable threadInstanceBoundHashtable = rb.getThreadMaxTable();
            columnNameVector = new java.util.Vector(2);
            columnNameVector.add("Thread");
            columnNameVector.add("Max");
            if ((threadInstanceBoundHashtable != null)
                && (threadInstanceBoundHashtable.size() > 0)) {
                java.util.Vector dataVector =
                    new java.util.Vector(threadInstanceBoundHashtable.size());
                Iterator i = threadInstanceBoundHashtable.keySet().iterator();
                while (i.hasNext()) {
                    String threadName = (String) i.next();
                    Integer threadMax = (Integer) threadInstanceBoundHashtable.get(threadName);
                    java.util.Vector tempVector = new java.util.Vector(2);
                    tempVector.add(threadName);
                    tempVector.add(threadMax);
                    dataVector.add(tempVector);
                }

                javax.swing.table.DefaultTableModel tableModel =
                    new javax.swing.table.DefaultTableModel(dataVector, columnNameVector);
                getThreadInstanceTable().setModel(tableModel);
            }
            else {
                javax.swing.table.DefaultTableModel tableModel =
                    new javax.swing.table.DefaultTableModel(
                        new java.util.Vector(0),
                        columnNameVector);
                getThreadInstanceTable().setModel(tableModel);
            }
        }
    }
	
    // update the variable tree dialog
    TreeModel treeModel = VariableTreeModelBuilder.buildTreeModel(classSet);
    getAddVariableTree().setModel(treeModel);
    getAddVariableTree().setCellRenderer(VariableTreeModelBuilder.getTreeCellRenderer(treeModel));

    // update the specification tab
    log.debug("Updating the specification tab ....");
    initPatternComboBoxes();

    updateAssertionList();

    Specification spec = activeSession.getSpecification();
    if (spec != null) {
		TemporalProperty tp = spec.getTemporalProperty();
		if (tp != null) {
	    	updateTemporalPropertyPanel(tp);
		}
    }
    
    log.debug("Finished updating the specification tab.");

	updateEnabledStatus();
}

    private void updateAssertionList() {

	if(allAssertionSet == null) {
	    log.warn("allAssertionSet was not initialized properly and is initialized now.");
	    allAssertionSet = new HashSet();
	}

	// grab the assertion names from BSL and get the assertion with that name: getAssertion(String name)
	Hashtable assertionSetTable =
	    edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet.getAssertionSetTable();
	if (assertionSetTable != null) {
	    Iterator astki = assertionSetTable.keySet().iterator();
	    while (astki.hasNext()) {
		Object key1 = astki.next();
		edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet currentAssertionSet =
		    (edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet)assertionSetTable.get(key1);
		Hashtable currentAssertionSetTable = currentAssertionSet.getAssertionTable();
		if (currentAssertionSetTable != null) {
		    Iterator casti = currentAssertionSetTable.keySet().iterator();
		    while (casti.hasNext()) {
			Object key2 = casti.next();
			edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion currentAssertion =
			    (edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion) currentAssertionSetTable.get(key2);
			Assertion a = getAssertion(currentAssertion.getName().toString());
			allAssertionSet.add(a);
			log.debug("Added the assertion (from the source) " + a.getName() +
				  " to the complete assertion set.");
		    }
		}
	    }
	}
	
	// grab the assertions from the active session (if it exists) and add them to allAssertionSet
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
	    Specification spec = activeSession.getSpecification();
	    if(spec != null) {
		Set assertionSet = spec.getAssertions();
		if((assertionSet != null) && (assertionSet.size() > 0)) {
		    Iterator asi = assertionSet.iterator();
		    while(asi.hasNext()) {
			Assertion currentAssertion = (Assertion)asi.next();
			allAssertionSet.add(currentAssertion);
		    }
		}
	    }
	}

	// this should be moved into a separate method that takes a set
	if((allAssertionSet != null) && (allAssertionSet.size() > 0)) {
	    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	    JPanel assertionListPanel = getAssertionListPanel();
	    assertionListPanel.removeAll();

	    Iterator aasi = allAssertionSet.iterator();
	    while (aasi.hasNext()) {
		Assertion assertion = (Assertion) aasi.next();
		JCheckBox checkBox = getAssertionCheckBox(assertion.getName());
		checkBox.setSelected(assertion.enabled());
		assertionListPanel.add(checkBox, gbc);
		gbc.gridy++;
	    }
	}
    }


/**
 * Comment
 */
public void variableBoundAddButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
	JPanel panel = new JPanel();
	panel.setLayout(new java.awt.GridLayout(3,2));
	JLabel varNameLabel = new JLabel("Variable");
	panel.add(varNameLabel);
	JTextField varNameTextField = new JTextField("<VariableName>", 20);
	panel.add(varNameTextField);
	JLabel lowerBoundLabel = new JLabel("Lower Bound");
	panel.add(lowerBoundLabel);
	JTextField lowerBoundTextField = new JTextField("<min>", 5);
	panel.add(lowerBoundTextField);
	JLabel upperBoundLabel = new JLabel("Upper Bound");
	panel.add(upperBoundLabel);
	JTextField upperBoundTextField = new JTextField("<max>", 5);
	panel.add(upperBoundTextField);

	int response = JOptionPane.showConfirmDialog(this, panel, "Variable Value Bounds", JOptionPane.OK_CANCEL_OPTION);
	if(response == JOptionPane.OK_OPTION) {
		String varName = varNameTextField.getText();
	 	String lowerBoundString = lowerBoundTextField.getText();
	 	String upperBoundString = upperBoundTextField.getText();
	 	try {
	 		Integer lowerBound = Integer.valueOf(lowerBoundString);
	 		Integer upperBound = Integer.valueOf(upperBoundString);
	 		Session activeSession = sessionManager.getActiveSession();
	 		if(activeSession != null) {
				BIROptions birOptions = activeSession.getBIROptions();
				if(birOptions != null) {
		 			edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
		 			if(rbs != null) {
		 				//rbs.setAllocMax(varName, lowerBound.intValue(), upperBound.intValue());
		 				updateView();
		 			}
				}
	 		}
	 	}
	 	catch(NumberFormatException nfe) {
		 	JOptionPane.showMessageDialog(this, "One of the numbers provided for variable " +
			 	varName + " is not valid: " + upperBoundString + " or " + lowerBoundString);
		 	return;
	 	}
	}
}
public void updateEnabledStatus(){
	
	// is this file saveable i.e. does it already have a name or do we need to use saveAs
	boolean saveEnabled = saveable;
	getSaveFileButton().setEnabled(saveEnabled);
	getSaveFileMenuItem().setEnabled(saveEnabled);

	// should the specification pane be enabled? i.e. has JJJC run?
	boolean specEnabled = !((classSet == null) || (classSet.size() <= 0));
	recursivelySetEnabled(getSpecificationDisplayPanel(),specEnabled);
	getSpecificationMessageLabel().setVisible(!specEnabled);
	
	//BiROptionPane section
	boolean jpfSelected = getJPFRadioButton().isSelected();
	
	if (jpfSelected){
		ivjJTextArea1.setText("Note: These options only effect BIR-based model checkers such as Spin and DSpin.");
		//disable bir options panel
		recursivelySetEnabled(getResourceBoundsPane(), false);
		//except for the text area
		ivjJTextArea1.setEnabled(true);
	}
	else{
		//make sure the resourceboundspanel is enabled
		recursivelySetEnabled(getResourceBoundsPane(), true);
		// BIR add/remove bounds buttons should be enabled only if JJJC has been run
		boolean boundsEnabled;
		if (classSet == null || classSet.isEmpty()){
			boundsEnabled = false;
			//let user know why the bounds are disabled
			ivjJTextArea1.setText("You must run JJJC before these bounds can be set properly.");
		}
		else{ 
			boundsEnabled = true;
			//set this to whatever it should. -mo
			ivjJTextArea1.setText("");
		}
		// enable/disable the buttons
		getInstanceBoundAddButton().setEnabled(boundsEnabled);
		getInstanceBoundRemoveButton().setEnabled(boundsEnabled);
		getVariableBoundAddButton().setEnabled(boundsEnabled);
		getVariableBoundRemoveButton().setEnabled(boundsEnabled);
		getThreadInstanceBoundAddButton().setEnabled(boundsEnabled);
		getThreadInstanceBoundRemoveButton().setEnabled(boundsEnabled);
	}
}	
/**
 * When the remove button is pressed for the variable bound
 * table, prompt the user for confirmation and if confirmed remove
 * the selected values from the model and call an update on the view.
 */
public void variableBoundRemoveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	JTable table = getVariableBoundTable();
	int[] selectedIndices = table.getSelectedRows();
	if((selectedIndices != null) && (selectedIndices.length > 0)) {
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession != null) {
			BIROptions birOptions = activeSession.getBIROptions();
			if(birOptions != null) {
				edu.ksu.cis.bandera.sessions.ResourceBounds rbs = birOptions.getResourceBounds();
				if(rbs != null) {
					for(int i = 0; i < selectedIndices.length; i++) {
						String variableName = (String)table.getValueAt(selectedIndices[i], 0);
						StringTokenizer st = new StringTokenizer(variableName, ".");
						if(st.countTokens() == 2) {
							String className = st.nextToken();
							String fieldName = st.nextToken();
							rbs.removeFieldBounds(className, fieldName);
							updateView();
						}
						else if(st.countTokens() == 3) {
							String className = st.nextToken();
							String methodName= st.nextToken();
							String localName = st.nextToken();
							rbs.removeMethodLocalBounds(className, methodName, localName);
							updateView();
						}
						else {
							// do nothing, don't know what this is! -tcw
						}
					}
				}
			}
		}
	}
}
/**
 * Comment
 */
public void workingDirectoryBrowseButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	JFileChooser fc = getDirectoryFileChooser();
	if(fc != null) {
		int response = fc.showOpenDialog(this);
		if(response == JFileChooser.APPROVE_OPTION) {
			java.io.File file = fc.getSelectedFile();
			Session activeSession = sessionManager.getActiveSession();
			if(activeSession != null) {
				activeSession.setWorkingDirectory(file);
				updateView();
			}
		}
	}
}
/**
 * When the user changes the working directory value, update the model
 * to match the user's given working directory.
 */
public void workingDirectoryTextField_FocusLost(java.awt.event.FocusEvent focusEvent) {
	String workingDirectoryString = getWorkingDirectoryTextField().getText();
	java.io.File workingDirectory = new java.io.File(workingDirectoryString);
	Session activeSession = sessionManager.getActiveSession();
	if(activeSession != null) {
		activeSession.setWorkingDirectory(workingDirectory);
		updateView();
	}
}

    private ExpressionBuilder getExpressionBuilder() {
	if(expressionBuilder == null) {
	    expressionBuilder = new ExpressionBuilder();
	    expressionBuilder.registerCompletionListener(this);
	}
	return(expressionBuilder);
    }

    private String currentExpressionBuilderParameter;
    private void setCurrentExpressionBuilderParameter(String parameterName) {
	currentExpressionBuilderParameter = parameterName;
    }
    private String getCurrentExpressionBuilderParameter() {
	return(currentExpressionBuilderParameter);
    }
}
