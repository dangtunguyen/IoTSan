package edu.ksu.cis.bandera.pdgslicer.dependency;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.Collection;
import ca.mcgill.sable.util.Map;
import ca.mcgill.sable.util.Set;
import ca.mcgill.sable.util.ArraySet;
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.List;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.pdgslicer.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.bui.BUI;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.bofa.Analysis;
//import java.util.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.BitSet;
import java.util.Vector;
import java.util.Enumeration;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
public class Dependencies extends JFrame implements java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.WindowListener {
	private Analysis BOFA_Analysis;
	public boolean success = true;
	Slicer slicer = null;
	private QueryPanel queryPanel = null;
	private DependencyValueViewer dependencyValueViewer = null;
	CriterionViewer criterionViewer = null;
	private final int DATA = 0;
	private final int CONTROL = 1;
	private final int DIVERGENT = 2;
	private final int READY = 3;
	private final int INTERFERENCE = 4;
	private final int SYNCHRONIZATION = 5;
	private final int VALUE_PRED = 6;
	private final int VALUE_SUCC = 7;
	private final int VALUE_SET = 8;
	List dependencyKindsList;
	private String dependencyKinds[] = {"Data Dependence", "Control Dependence", "Divergent Dependence", "Ready Dependence", "Interference Dependence", "Synchronization Dependence"};
	List valueKindsList;
	private String valueKinds[] = {"Value Predecessors", "Value Successors", "Value Set"};
	private boolean isSetSelectedByProgram = false;
	Annotation currentMethodDecAnn = null;
	private boolean isChangeCheckBoxByProgram = false;
	private boolean isSetItemByProgram = false;
	private boolean doSuccDataDepend = false;
	private boolean doPredDataDepend = false;
	private boolean doSuccControlDepend = false;
	private boolean doPredControlDepend = false;
	private boolean doSuccReadyDepend = false;
	private boolean doPredReadyDepend = false;
	private boolean doSuccSynchDepend = false;
	private boolean doPredSynchDepend = false;
	private boolean doSuccInterferDepend = false;
	private boolean doPredInterferDepend = false;
	private boolean doSuccDivergentDepend = false;
	private boolean doPredDivergentDepend = false;
	private boolean doPredValue = false;
	private boolean doSuccValue = false;
	private boolean doSetValue = false;
	private Hashtable nodeToConfigTable = new Hashtable();
	Object currentNode = null;
	private LinkedList historyChain = new LinkedList();
	private int currentPositionOfHistoryChain = -1;
	public static Dependencies dependFrame;
	private JPanel ivjCheckBoxesPanel = null;
	private JPanel ivjDependencyFrameContentPane = null;
	private JToolBar ivjDependOperationToolBar = null;
	private JPanel ivjOperationPanel = null;
	private JPanel ivjToolBarPanel = null;
	private JLabel ivjCurrentStmtLabel = null;
	private JComboBox ivjHistoryComboBox = null;
	private JButton ivjBackwardSliceToolBarButton = null;
	private JButton ivjForwardSliceToolBarButton = null;
	private JButton ivjBackToolBarButton = null;
	private JButton ivjClearToolBarButton = null;
	private JButton ivjForwardToolBarButton = null;
	private JLabel ivjValueFlowLabel = null;
	private JCheckBox ivjValuePredCheckBox = null;
	private JCheckBox ivjValueSetCheckBox = null;
	private JCheckBox ivjValueSuccCheckBox = null;
	private JLabel ivjControlLabel = null;
	private JCheckBox ivjControlPredCheckBox = null;
	private JCheckBox ivjControlSuccCheckBox = null;
	private JLabel ivjDataLabel = null;
	private JCheckBox ivjDataPredCheckBox = null;
	private JCheckBox ivjDataSuccCheckBox = null;
	private JLabel ivjInterferLabel = null;
	private JCheckBox ivjInterferPredCheckBox = null;
	private JCheckBox ivjInterferSuccCheckBox = null;
	private JLabel ivjReadyLabel = null;
	private JCheckBox ivjReadyPredCheckBox = null;
	private JCheckBox ivjReadySuccCheckBox = null;
	private JLabel ivjSynchLabel = null;
	private JCheckBox ivjSynchPredCheckBox = null;
	private JCheckBox ivjSynchSuccCheckBox = null;
	private JLabel ivjDiverLabel = null;
	private JCheckBox ivjDiverPredCheckBox = null;
	private JCheckBox ivjDiverSuccCheckBox = null;
	private CodeBrowserPane ivjCodeBrowserPane = null;
	private JSplitPane ivjCodeCritSplitPane = null;
	private JButton ivjAllpredButton = null;
	private JButton ivjAllsuccButton = null;
	private JPanel ivjDependCheckBoxesPanel = null;
	private JLabel ivjDependLabel = null;
	private JLabel ivjDpredLabel = null;
	private JLabel ivjDsuccLabel = null;
	private JPanel ivjEmptyPanel = null;
	private JButton ivjNopredButton = null;
	private JButton ivjNosuccButton = null;
	private JPanel ivjToolBarInPanel = null;
	private JPanel ivjValueCheckBoxesPanel = null;
	private FlowLayout ivjValueCheckBoxesPanelFlowLayout = null;
	private JButton ivjAllDependButton = null;
	private JButton ivjClearDependButton = null;
	private JButton ivjClearValuesButton = null;
	private JButton ivjAllValuesButton = null;
	private ViewerTabbedPane ivjViewerTabbedPane = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.WindowListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == Dependencies.this.getHistoryComboBox()) 
				connEtoC9();
			if (e.getSource() == Dependencies.this.getBackToolBarButton()) 
				connEtoC10(e);
			if (e.getSource() == Dependencies.this.getForwardToolBarButton()) 
				connEtoC11(e);
			if (e.getSource() == Dependencies.this.getClearToolBarButton()) 
				connEtoC15();
			if (e.getSource() == Dependencies.this.getAllpredButton()) 
				connEtoC28(e);
			if (e.getSource() == Dependencies.this.getAllsuccButton()) 
				connEtoC29(e);
			if (e.getSource() == Dependencies.this.getAllDependButton()) 
				connEtoC30(e);
			if (e.getSource() == Dependencies.this.getNopredButton()) 
				connEtoC31(e);
			if (e.getSource() == Dependencies.this.getNosuccButton()) 
				connEtoC32(e);
			if (e.getSource() == Dependencies.this.getClearDependButton()) 
				connEtoC33(e);
			if (e.getSource() == Dependencies.this.getAllValuesButton()) 
				connEtoC34(e);
			if (e.getSource() == Dependencies.this.getClearValuesButton()) 
				connEtoC35(e);
			if (e.getSource() == Dependencies.this.getBackwardSliceToolBarButton()) 
				connEtoC2();
		};
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getSource() == Dependencies.this.getDataPredCheckBox()) 
				connEtoC8(e);
			if (e.getSource() == Dependencies.this.getDataSuccCheckBox()) 
				connEtoC12(e);
			if (e.getSource() == Dependencies.this.getControlPredCheckBox()) 
				connEtoC13(e);
			if (e.getSource() == Dependencies.this.getControlSuccCheckBox()) 
				connEtoC16(e);
			if (e.getSource() == Dependencies.this.getDiverPredCheckBox()) 
				connEtoC17(e);
			if (e.getSource() == Dependencies.this.getDiverSuccCheckBox()) 
				connEtoC18(e);
			if (e.getSource() == Dependencies.this.getReadyPredCheckBox()) 
				connEtoC19(e);
			if (e.getSource() == Dependencies.this.getReadySuccCheckBox()) 
				connEtoC20(e);
			if (e.getSource() == Dependencies.this.getInterferPredCheckBox()) 
				connEtoC21(e);
			if (e.getSource() == Dependencies.this.getInterferSuccCheckBox()) 
				connEtoC22(e);
			if (e.getSource() == Dependencies.this.getSynchPredCheckBox()) 
				connEtoC23(e);
			if (e.getSource() == Dependencies.this.getSynchSuccCheckBox()) 
				connEtoC24(e);
			if (e.getSource() == Dependencies.this.getValuePredCheckBox()) 
				connEtoC25(e);
			if (e.getSource() == Dependencies.this.getValueSuccCheckBox()) 
				connEtoC26(e);
			if (e.getSource() == Dependencies.this.getValueSetCheckBox()) 
				connEtoC27(e);
		};
		public void windowActivated(java.awt.event.WindowEvent e) {};
		public void windowClosed(java.awt.event.WindowEvent e) {
			if (e.getSource() == Dependencies.this) 
				connEtoC1(e);
		};
		public void windowClosing(java.awt.event.WindowEvent e) {};
		public void windowDeactivated(java.awt.event.WindowEvent e) {};
		public void windowDeiconified(java.awt.event.WindowEvent e) {};
		public void windowIconified(java.awt.event.WindowEvent e) {};
		public void windowOpened(java.awt.event.WindowEvent e) {};
	};
/**
 * Dependencies constructor comment.
 */
public Dependencies() {
	super();
	initialize();

}
/**
 * Dependencies constructor comment.
 * @param title java.lang.String
 */
public Dependencies(String title) {
	super(title);
}
/**
 * Method to handle events for the ActionListener interface.
 * @param e java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getHistoryComboBox()) 
		connEtoC9();
	if (e.getSource() == getBackToolBarButton()) 
		connEtoC10(e);
	if (e.getSource() == getForwardToolBarButton()) 
		connEtoC11(e);
	if (e.getSource() == getClearToolBarButton()) 
		connEtoC15();
	if (e.getSource() == getAllpredButton()) 
		connEtoC28(e);
	if (e.getSource() == getAllsuccButton()) 
		connEtoC29(e);
	if (e.getSource() == getAllDependButton()) 
		connEtoC30(e);
	if (e.getSource() == getNopredButton()) 
		connEtoC31(e);
	if (e.getSource() == getNosuccButton()) 
		connEtoC32(e);
	if (e.getSource() == getClearDependButton()) 
		connEtoC33(e);
	if (e.getSource() == getAllValuesButton()) 
		connEtoC34(e);
	if (e.getSource() == getClearValuesButton()) 
		connEtoC35(e);
	if (e.getSource() == getBackwardSliceToolBarButton()) 
		connEtoC2();
	// user code begin {2}
	// user code end
}
public void addSelectedNodeToDependFrame(StmtTreeNode nodeTobeAdded) {
	//Object nodeTobeAdded = getTreeNode();
	if (nodeTobeAdded == null)
		return;
	if (!getClearToolBarButton().isEnabled())
		getClearToolBarButton().setEnabled(true);
	if (!getBackwardSliceToolBarButton().isEnabled())
		getBackwardSliceToolBarButton().setEnabled(true);
	if (!getForwardSliceToolBarButton().isEnabled())
		getForwardSliceToolBarButton().setEnabled(true);
	/*
	if (!getQueryToolBarButton().isEnabled())
	getQueryToolBarButton().setEnabled(true);
	*/
	int itemIndex = historyComboBoxItemsContains(nodeTobeAdded);
	Configuration currentConfig = null;
	if (itemIndex >= 0) {
		int selectedIndex = getHistoryComboBox().getSelectedIndex();
		if (itemIndex == selectedIndex)
			return;
		currentConfig = getCurrentConfig();
		nodeToConfigTable.put(currentNode, currentConfig);
		//put the current node into history chain
		compactHistoryChain(currentNode);
		historyChain.addLast(currentNode);
		currentPositionOfHistoryChain++;
		if (!getBackToolBarButton().isEnabled())
			getBackToolBarButton().setEnabled(true);
		currentNode = getHistoryComboBox().getItemAt(itemIndex);
		Configuration oldConfig = (Configuration) nodeToConfigTable.get(currentNode);
		if (!doDependsInConfig(oldConfig) && !doValuesInConfig(oldConfig)) {
			initializeConfig();
			if (nodeTobeAdded instanceof StmtTreeNode)
				currentMethodDecAnn = ((StmtTreeNode) nodeTobeAdded).currentMethodDeclarationAnnotation;
			dependencyValueViewer.currentDependTreeRoot = new DefaultMutableTreeNode(currentNode);
			dependencyValueViewer.getDependencyTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentDependTreeRoot));
			dependencyValueViewer.currentValueTreeRoot = new DefaultMutableTreeNode(currentNode);
			dependencyValueViewer.getValueTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentValueTreeRoot));
			if (currentConfig != null)
				restoreConfigByAddingNewNode(currentConfig);
		} else
			restoreConfig(oldConfig);
		isSetItemByProgram = true;
		getHistoryComboBox().setSelectedIndex(itemIndex);
		isSetItemByProgram = false;
		return;
	}
	isSetItemByProgram = true;
	getHistoryComboBox().insertItemAt(nodeTobeAdded, 0);
	getHistoryComboBox().setSelectedIndex(0);
	isSetItemByProgram = false;
	if (currentNode != null) {
		//save the current configuration;
		currentConfig = getCurrentConfig();
		nodeToConfigTable.put(currentNode, currentConfig);
		//put the current node into history chain
		compactHistoryChain(currentNode);
		historyChain.addLast(currentNode);
		currentPositionOfHistoryChain++;
		if (!getBackToolBarButton().isEnabled())
			getBackToolBarButton().setEnabled(true);
	}
	//initialize the config;
	initializeConfig();
	currentNode = nodeTobeAdded;
	if (nodeTobeAdded instanceof StmtTreeNode)
		currentMethodDecAnn = ((StmtTreeNode) nodeTobeAdded).currentMethodDeclarationAnnotation;
	dependencyValueViewer.currentDependTreeRoot = new DefaultMutableTreeNode(currentNode);
	dependencyValueViewer.getDependencyTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentDependTreeRoot));
	dependencyValueViewer.currentValueTreeRoot = new DefaultMutableTreeNode(currentNode);
	dependencyValueViewer.getValueTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentValueTreeRoot));
	if (currentConfig != null)
		restoreConfigByAddingNewNode(currentConfig);
}
/**
 * Comment
 */
public void allDependButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getAllDependButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (!doPredDataDepend) {
		doPredDataDepend = true;
		getDataPredCheckBox().setSelected(true);
	}
	if (!doPredControlDepend) {
		doPredControlDepend = true;
		getControlPredCheckBox().setSelected(true);
	}
	if (!doPredReadyDepend) {
		doPredReadyDepend = true;
		getReadyPredCheckBox().setSelected(true);
	}
	if (!doPredSynchDepend) {
		doPredSynchDepend = true;
		getSynchPredCheckBox().setSelected(true);
	}
	if (!doPredInterferDepend) {
		doPredInterferDepend = true;
		getInterferPredCheckBox().setSelected(true);
	}
	if (!doPredDivergentDepend) {
		doPredDivergentDepend = true;
		getDiverPredCheckBox().setSelected(true);
	}
	if (!doSuccDataDepend) {
		doSuccDataDepend = true;
		getDataSuccCheckBox().setSelected(true);
	}
	if (!doSuccControlDepend) {
		doSuccControlDepend = true;
		getControlSuccCheckBox().setSelected(true);
	}
	if (!doSuccReadyDepend) {
		doSuccReadyDepend = true;
		getReadySuccCheckBox().setSelected(true);
	}
	if (!doSuccSynchDepend) {
		doSuccSynchDepend = true;
		getSynchSuccCheckBox().setSelected(true);
	}
	if (!doSuccInterferDepend) {
		doSuccInterferDepend = true;
		getInterferSuccCheckBox().setSelected(true);
	}
	if (!doSuccDivergentDepend) {
		doSuccDivergentDepend = true;
		getDiverSuccCheckBox().setSelected(true);
	}
	isChangeCheckBoxByProgram = false;
	DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
	Set dependencySet = new ca.mcgill.sable.util.ArraySet(this.dependencyKinds);
	for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
		String dependKind = oneChild.toString();
		dependencySet.remove(dependKind);
		boolean foundSuccessor = false;
		boolean foundPredecessor = false;
		for (int j = 0; j < oneChild.getChildCount(); j++) {
			DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
			if (predSuccChild.toString().equals("Successors"))
				foundSuccessor = true;
			else
				if (predSuccChild.toString().equals("Predecessors"))
					foundPredecessor = true;
		}
		if (!foundSuccessor) {
			DefaultMutableTreeNode currentDependencyRoot = oneChild;
			DefaultMutableTreeNode dependencySuccTreeNode = getDependencyTreeNode(dependKind, false);
			treeModel.insertNodeInto(dependencySuccTreeNode, currentDependencyRoot, currentDependencyRoot.getChildCount());
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dependencySuccTreeNode.getPath()));
		}
		if (!foundPredecessor) {
			DefaultMutableTreeNode currentDependencyRoot = oneChild;
			DefaultMutableTreeNode dependencyPredTreeNode = getDependencyTreeNode(dependKind, true);
			treeModel.insertNodeInto(dependencyPredTreeNode, currentDependencyRoot, 0);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dependencyPredTreeNode.getPath()));
		}
	}
	boolean isDependency = true;
	for (Iterator dependIt = dependencySet.iterator(); dependIt.hasNext();) {
		String dependKind = (String) dependIt.next();
		DefaultMutableTreeNode currentDependencyRoot = new DefaultMutableTreeNode(dependKind);
		int insertIndex = dependencyValueViewer.getInsertIndexOf(dependKind, isDependency);
		treeModel.insertNodeInto(currentDependencyRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		DefaultMutableTreeNode dependencyPredTreeNode = getDependencyTreeNode(dependKind, true);
		treeModel.insertNodeInto(dependencyPredTreeNode, currentDependencyRoot, 0);
		DefaultMutableTreeNode dependencySuccTreeNode = getDependencyTreeNode(dependKind, false);
		treeModel.insertNodeInto(dependencySuccTreeNode, currentDependencyRoot, 1);
		// Make sure the user can see the lovely new node.
		dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(currentDependencyRoot.getPath()));
	}
	dependencyValueViewer.expandDependTreeToOneLevel(isDependency);
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void allpredButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getAllpredButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (!doPredDataDepend) {
		doPredDataDepend = true;
		getDataPredCheckBox().setSelected(true);
	}
	if (!doPredControlDepend) {
		doPredControlDepend = true;
		getControlPredCheckBox().setSelected(true);
	}
	if (!doPredReadyDepend) {
		doPredReadyDepend = true;
		getReadyPredCheckBox().setSelected(true);
	}
	if (!doPredSynchDepend) {
		doPredSynchDepend = true;
		getSynchPredCheckBox().setSelected(true);
	}
	if (!doPredInterferDepend) {
		doPredInterferDepend = true;
		getInterferPredCheckBox().setSelected(true);
	}
	if (!doPredDivergentDepend) {
		doPredDivergentDepend = true;
		getDiverPredCheckBox().setSelected(true);
	}
	isChangeCheckBoxByProgram = false;

	//Traverse the dependency tree for each kind of dependency
	//to see if there is predecessors node. If there is, then skip
	//if there is no this node, then create it and calculate the
	//predecessors of that dependency.
	DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
	Set dependencySet = new ca.mcgill.sable.util.ArraySet(this.dependencyKinds);
	for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
		String dependKind = oneChild.toString();
		dependencySet.remove(dependKind);
		boolean foundPredecessor = false;
		for (int j = 0; j < oneChild.getChildCount(); j++) {
			DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
			if (predSuccChild.toString().equals("Predecessors"))
				foundPredecessor = true;
		}
		if (foundPredecessor)
			continue;
		DefaultMutableTreeNode currentDependencyRoot = oneChild;
		DefaultMutableTreeNode dependencyPredTreeNode = getDependencyTreeNode(dependKind, true);
		treeModel.insertNodeInto(dependencyPredTreeNode, currentDependencyRoot, 0);
		// Make sure the user can see the lovely new node.
		dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dependencyPredTreeNode.getPath()));
	}
	boolean isDependency = true;
	for (Iterator dependIt = dependencySet.iterator(); dependIt.hasNext();) {
		String dependKind = (String) dependIt.next();
		DefaultMutableTreeNode currentDependencyRoot = new DefaultMutableTreeNode(dependKind);
		int insertIndex = dependencyValueViewer.getInsertIndexOf(dependKind, isDependency);
		treeModel.insertNodeInto(currentDependencyRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		DefaultMutableTreeNode dependencyPredTreeNode = getDependencyTreeNode(dependKind, true);
		treeModel.insertNodeInto(dependencyPredTreeNode, currentDependencyRoot, 0);
		// Make sure the user can see the lovely new node.
		dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dependencyPredTreeNode.getPath()));
	}
	dependencyValueViewer.expandDependTreeToOneLevel(isDependency);
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void allsuccButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getAllsuccButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (!doSuccDataDepend) {
		doSuccDataDepend = true;
		getDataSuccCheckBox().setSelected(true);
	}
	if (!doSuccControlDepend) {
		doSuccControlDepend = true;
		getControlSuccCheckBox().setSelected(true);
	}
	if (!doSuccReadyDepend) {
		doSuccReadyDepend = true;
		getReadySuccCheckBox().setSelected(true);
	}
	if (!doSuccSynchDepend) {
		doSuccSynchDepend = true;
		getSynchSuccCheckBox().setSelected(true);
	}
	if (!doSuccInterferDepend) {
		doSuccInterferDepend = true;
		getInterferSuccCheckBox().setSelected(true);
	}
	if (!doSuccDivergentDepend) {
		doSuccDivergentDepend = true;
		getDiverSuccCheckBox().setSelected(true);
	}
	isChangeCheckBoxByProgram = false;
	DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
	Set dependencySet = new ca.mcgill.sable.util.ArraySet(this.dependencyKinds);
	for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
		String dependKind = oneChild.toString();
		dependencySet.remove(dependKind);
		boolean foundSuccessor = false;
		for (int j = 0; j < oneChild.getChildCount(); j++) {
			DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
			if (predSuccChild.toString().equals("Successors"))
				foundSuccessor = true;
		}
		if (foundSuccessor)
			continue;
		DefaultMutableTreeNode currentDependencyRoot = oneChild;
		DefaultMutableTreeNode dependencySuccTreeNode = getDependencyTreeNode(dependKind, false);
		treeModel.insertNodeInto(dependencySuccTreeNode, currentDependencyRoot, currentDependencyRoot.getChildCount());
		// Make sure the user can see the lovely new node.
		dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dependencySuccTreeNode.getPath()));
	}
	boolean isDependency = true;
	for (Iterator dependIt = dependencySet.iterator(); dependIt.hasNext();) {
		String dependKind = (String) dependIt.next();
		DefaultMutableTreeNode currentDependencyRoot = new DefaultMutableTreeNode(dependKind);
		int insertIndex = dependencyValueViewer.getInsertIndexOf(dependKind, isDependency);
		treeModel.insertNodeInto(currentDependencyRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		DefaultMutableTreeNode dependencySuccTreeNode = getDependencyTreeNode(dependKind, false);
		treeModel.insertNodeInto(dependencySuccTreeNode, currentDependencyRoot, 0);
		// Make sure the user can see the lovely new node.
		dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dependencySuccTreeNode.getPath()));
	}
	dependencyValueViewer.expandDependTreeToOneLevel(isDependency);
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void allValuesButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getAllValuesButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (!doPredValue) {
		doPredValue = true;
		getValuePredCheckBox().setSelected(true);
	}
	if (!doSuccValue) {
		doSuccValue = true;
		getValueSuccCheckBox().setSelected(true);
	}
	if (!doSetValue) {
		doSetValue = true;
		getValueSetCheckBox().setSelected(true);
	}
	isChangeCheckBoxByProgram = false;
	DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getValueTree().getModel();
	Set valueKindSet = new ca.mcgill.sable.util.ArraySet(this.valueKinds);
	for (int i = 0; i < dependencyValueViewer.currentValueTreeRoot.getChildCount(); i++) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentValueTreeRoot.getChildAt(i);
		String valueKind = oneChild.toString();
		valueKindSet.remove(valueKind);
	}
	boolean isDependency = false;
	for (Iterator kindIt = valueKindSet.iterator(); kindIt.hasNext();) {
		String valueKind = (String) kindIt.next();
		DefaultMutableTreeNode currentValueTreeRoot = dependencyValueViewer.currentValueTreeRoot;
		int insertIndex = dependencyValueViewer.getInsertIndexOf(valueKind, isDependency);
		DefaultMutableTreeNode valueTreeNode = getValueTreeNode(valueKind);
		treeModel.insertNodeInto(valueTreeNode, currentValueTreeRoot, insertIndex);

		// Make sure the user can see the lovely new node.
		dependencyValueViewer.getValueTree().scrollPathToVisible(new TreePath(currentValueTreeRoot.getPath()));
	}
	//and then
	dependencyValueViewer.expandAndCollapseFrom(dependencyValueViewer.currentValueTreeRoot, isDependency);
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void backToolBarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	//save current configuration
	if (currentPositionOfHistoryChain == historyChain.size() - 1) {
		compactHistoryChain(currentNode);
		historyChain.addLast(currentNode);
		currentPositionOfHistoryChain++;
	}
	Configuration config = getCurrentConfig();
	nodeToConfigTable.put(currentNode, config);
	//get the previous configuration
	--currentPositionOfHistoryChain;
	currentNode = historyChain.get(currentPositionOfHistoryChain);
	config = (Configuration) nodeToConfigTable.get(currentNode);
	restoreConfig(config);
	isSetItemByProgram = true;
	getHistoryComboBox().setSelectedItem(currentNode);
	isSetItemByProgram = false;
	if (currentPositionOfHistoryChain == 0)
		getBackToolBarButton().setEnabled(false);
	if (!getForwardToolBarButton().isEnabled())
		getForwardToolBarButton().setEnabled(true);
	return;
}
/**
 * Comment
 */
public void backwardSliceToolBarButton_ActionEvents() {
	this.criterionViewer.runSlicerWithCurrentCriterion();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-20 15:12:55)
 */
private void calculateValueSet(DefaultMutableTreeNode valueRoot) {
	Annotation mda = null;
	Annotation stmtAnnotation = null;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		stmtAnnotation = startTreeNode.currentStmtAnnotation;
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			stmtAnnotation = (Annotation) currentNode;
		} else
			return;
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
		//deterimine the set of Jimple statement in the stmtAnnotation
	Stmt[] stmtsInAnnotation;
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = stmtAnnotation.getStatements();
		//stmtsInAnnotation = new Stmt[1];
		//stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = new Stmt[1];
			Stmt[] testStmts = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
			stmtsInAnnotation[0] = testStmts[0];
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = new Stmt[1];
				Stmt[] testStmts = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
				stmtsInAnnotation[0] = testStmts[0];
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	//Calculate reference value set used in the stmtsInAnnotation
	Set refValueSet = getRefValueSetFrom(stmtsInAnnotation);
	for (Iterator valueIt = refValueSet.iterator(); valueIt.hasNext();) {
		Value value = (Value) valueIt.next();
		DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(value);
		valueRoot.add(valueNode);
		//Analysis bofaAnalysis = Analysis.init();
		Collection valueSet = BOFA_Analysis.referenceValueSet(value, sootMethod);
		for (Iterator valueIt2 = valueSet.iterator(); valueIt2.hasNext();) {
			edu.ksu.cis.bandera.bofa.Analysis.ExprStmtMethodTriple valueTriple = (edu.ksu.cis.bandera.bofa.Analysis.ExprStmtMethodTriple) valueIt2.next();
			edu.ksu.cis.bandera.bofa.Analysis.StmtMethodPair stmtMethodPair = valueTriple.getStmtMethodPair();
			Stmt valueStmt = stmtMethodPair.getStmt();
			SootMethod valueMethod = stmtMethodPair.getSootMethod();
			if (valueStmt instanceof IdentityStmt) {
				ParameterNode paraNode = new ParameterNode(sootMethod.getDeclaringClass(), mda, (IdentityStmt) valueStmt);
				valueNode.add(new DefaultMutableTreeNode(paraNode));
			} else {
				Annotation valueStmtAnnotation = null;
				Annotation methodAnnotation = null;
				if (valueMethod.equals(sootMethod))
					methodAnnotation = mda;
				else
					methodAnnotation = CompilationManager.getAnnotationManager().getAnnotation(valueMethod.getDeclaringClass(), valueMethod);
			try {
				valueStmtAnnotation = methodAnnotation.getContainingAnnotation(valueStmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And ddStmtAnnotation may be null");
			}
			if (!valueStmtAnnotation.toString().equals(""))
				valueNode.add(new DefaultMutableTreeNode(valueStmtAnnotation));
		}
	}
}
}
/**
 * Comment
 */
public void clearDependButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getClearDependButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (doPredDataDepend) {
		doPredDataDepend = false;
		getDataPredCheckBox().setSelected(false);
	}
	if (doPredControlDepend) {
		doPredControlDepend = false;
		getControlPredCheckBox().setSelected(false);
	}
	if (doPredReadyDepend) {
		doPredReadyDepend = false;
		getReadyPredCheckBox().setSelected(false);
	}
	if (doPredSynchDepend) {
		doPredSynchDepend = false;
		getSynchPredCheckBox().setSelected(false);
	}
	if (doPredInterferDepend) {
		doPredInterferDepend = false;
		getInterferPredCheckBox().setSelected(false);
	}
	if (doPredDivergentDepend) {
		doPredDivergentDepend = false;
		getDiverPredCheckBox().setSelected(false);
	}
	if (doSuccDataDepend) {
		doSuccDataDepend = false;
		getDataSuccCheckBox().setSelected(false);
	}
	if (doSuccControlDepend) {
		doSuccControlDepend = false;
		getControlSuccCheckBox().setSelected(false);
	}
	if (doSuccReadyDepend) {
		doSuccReadyDepend = false;
		getReadySuccCheckBox().setSelected(false);
	}
	if (doSuccSynchDepend) {
		doSuccSynchDepend = false;
		getSynchSuccCheckBox().setSelected(false);
	}
	if (doSuccInterferDepend) {
		doSuccInterferDepend = false;
		getInterferSuccCheckBox().setSelected(false);
	}
	if (doSuccDivergentDepend) {
		doSuccDivergentDepend = false;
		getDiverSuccCheckBox().setSelected(false);
	}
	isChangeCheckBoxByProgram = false;
	//remove all predecessor and successsor dependency in current tree and
	dependencyValueViewer.currentDependTreeRoot = new DefaultMutableTreeNode(currentNode);
	dependencyValueViewer.getDependencyTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentDependTreeRoot));
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void clearToolBarButton_ActionEvents() {
	initializeConfig();
	currentMethodDecAnn = null;
	currentNode = null;
	currentPositionOfHistoryChain = -1;
	dependencyValueViewer.currentDependTreeRoot = null;
	dependencyValueViewer.currentValueTreeRoot = null;
	historyChain = new LinkedList();
	nodeToConfigTable = new Hashtable();
	getBackToolBarButton().setEnabled(false);
	getForwardToolBarButton().setEnabled(false);
	getClearToolBarButton().setEnabled(false);
	getBackwardSliceToolBarButton().setEnabled(false);
	getForwardSliceToolBarButton().setEnabled(false);
	isSetItemByProgram = true;
	try {
		getHistoryComboBox().removeAllItems();
	} catch (Exception e) {
	}
	isSetItemByProgram = false;
	dependencyValueViewer.getDependencyTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
	dependencyValueViewer.getValueTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
	return;
}
/**
 * Comment
 */
public void clearValuesButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getClearValuesButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (doPredValue) {
		doPredValue = false;
		getValuePredCheckBox().setSelected(false);
	}
	if (doSuccValue) {
		doSuccValue = false;
		getValueSuccCheckBox().setSelected(false);
	}
	if (doSetValue) {
		doSetValue = false;
		getValueSetCheckBox().setSelected(false);
	}
	isChangeCheckBoxByProgram = false;
	//remove all values in the viewer
	dependencyValueViewer.currentValueTreeRoot = new DefaultMutableTreeNode(currentNode);
	dependencyValueViewer.getValueTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentValueTreeRoot));

	//and then

	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (7/17/2001 4:07:26 PM)
 * @return ca.mcgill.sable.util.Set
 * @param stmtsInAnnotation ca.mcgill.sable.soot.jimple.Stmt[]
 */
private Set collectVarsFromStmts(Stmt[] stmtsInAnnotation) {
	Set varSet = new ArraySet();
	for (int i = 0; i < stmtsInAnnotation.length; i++) {
		for (Iterator valueBoxIt = stmtsInAnnotation[i].getUseAndDefBoxes().iterator(); valueBoxIt.hasNext();) {
			ValueBox valueBox = (ValueBox) valueBoxIt.next();
			Value value = valueBox.getValue();
			if (value instanceof Local) {
				if (!((Local) value).getName().startsWith("JJJCTEMP") && !((Local) value).getName().startsWith("$"))
					varSet.add(value);
			} else
				if ((value instanceof InvokeExpr) || (value instanceof Constant) || (value instanceof BinopExpr) ||(value instanceof NewExpr)) {
				} else
					varSet.add(value);
		}
	}
	return varSet;
}
/**
 * Insert the method's description here.
 * Creation date: (00-5-28 19:36:19)
 * @param node java.lang.Object
 */
private void compactHistoryChain(Object node) {
	int index = historyChain.indexOf(node);
	while (index >= 0) {
		historyChain.remove(index);
		currentPositionOfHistoryChain--;
		index = historyChain.indexOf(node);
	}
}
/**
 * connEtoC1:  (Dependencies.window.windowClosed(java.awt.event.WindowEvent) --> Dependencies.dependencies_WindowClosed(Ljava.awt.event.WindowEvent;)V)
 * @param arg1 java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.WindowEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dependencies_WindowClosed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (BackToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.backToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC10(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.backToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (ForwardToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.forwardToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC11(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.forwardToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (DataSuccCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.dataSuccCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
			
			// user code end
			this.dataSuccCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (ControlPredCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.controlPredCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.controlPredCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (ClearToolBarButton.action. --> Dependencies.clearToolBarButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14() {
	try {
		// user code begin {1}
		// user code end
		this.clearToolBarButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC15:  (QueryToolBarButton.action. --> Dependencies.queryToolBarButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC15() {
	try {
		// user code begin {1}
		// user code end
		this.clearToolBarButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC16:  (ControlSuccCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.controlSuccCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC16(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.controlSuccCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC17:  (DiverPredCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.diverPredCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC17(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.diverPredCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC18:  (DiverSuccCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.diverSuccCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC18(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.diverSuccCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC19:  (ReadyPredCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.readyPredCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC19(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.readyPredCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (BackwardSliceToolBarButton.action. --> Dependencies.backwardSliceToolBarButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.backwardSliceToolBarButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC20:  (ReadySuccCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.readySuccCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC20(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.readySuccCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC21:  (InterferPredCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.interferPredCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC21(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.interferPredCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC22:  (InterferSuccCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.interferSuccCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC22(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.interferSuccCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC23:  (SynchPredCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.synchPredCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC23(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.synchPredCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC24:  (SynchSuccCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.synchSuccCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC24(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.synchSuccCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC25:  (ValuePredCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.valuePredCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC25(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.valuePredCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC26:  (ValueSuccCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.valueSuccCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC26(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.valueSuccCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC27:  (ValueSetCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.valueSetCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC27(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.valueSetCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC28:  (AllpredButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.allpredButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC28(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.allpredButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC29:  (AllsuccButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.allsuccButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC29(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.allsuccButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC30:  (AllDependButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.allDependButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC30(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.allDependButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC31:  (NopredButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.nopredButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC31(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.nopredButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC32:  (NosuccButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.nosuccButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC32(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.nosuccButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC33:  (ClearDependButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.clearDependButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC33(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.clearDependButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC34:  (AllValuesButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.allValuesButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC34(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.allValuesButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC35:  (ClearValuesButton.action.actionPerformed(java.awt.event.ActionEvent) --> Dependencies.clearValuesButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC35(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.clearValuesButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (DataPredCheckBox.item.itemStateChanged(java.awt.event.ItemEvent) --> Dependencies.dataPredCheckBox_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		if (!isChangeCheckBoxByProgram)
		// user code end
		this.dataPredCheckBox_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (HistoryComboBox.action. --> Dependencies.historyComboBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9() {
	try {
		// user code begin {1}
		if (!isSetItemByProgram)
		// user code end
		this.historyComboBox_ActionEvents();
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
public void controlPredCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getControlPredCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doPredControlDepend = !doPredControlDepend;
	if (!doPredControlDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Control Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Predecessors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentControlRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Control Dependence")) {
				currentControlRoot = oneChild;
				break;
			}
		}
		if (currentControlRoot == null) {
			currentControlRoot = new DefaultMutableTreeNode("Control Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Control Dependence", isDependency);
			treeModel.insertNodeInto(currentControlRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode controlPredTreeNode = getControlDependAnns(true);
		if (controlPredTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(controlPredTreeNode, currentControlRoot, 0);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(controlPredTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(controlPredTreeNode, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void controlSuccCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getControlSuccCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSuccControlDepend = !doSuccControlDepend;
	if (!doSuccControlDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Control Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Successors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentControlRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Control Dependence")) {
				currentControlRoot = oneChild;
				break;
			}
		}
		if (currentControlRoot == null) {
			currentControlRoot = new DefaultMutableTreeNode("Control Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Control Dependence", isDependency);
			treeModel.insertNodeInto(currentControlRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode controlSuccTreeNode = getControlDependAnns(false);
		if (controlSuccTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(controlSuccTreeNode, currentControlRoot, currentControlRoot.getChildCount());
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(controlSuccTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(controlSuccTreeNode, isDependency);
		}
	}

	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void dataPredCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getDataPredCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doPredDataDepend = !doPredDataDepend;
	//calculate data predecessor and add it to current tree.
	if (!doPredDataDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Data Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Predecessors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentDataRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Data Dependence")) {
				currentDataRoot = oneChild;
				break;
			}
		}
		if (currentDataRoot == null) {
			currentDataRoot = new DefaultMutableTreeNode("Data Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Data Dependence",isDependency);
			treeModel.insertNodeInto(currentDataRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode dataPredTreeNode = getDataDependAnns(true);
		if (dataPredTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(dataPredTreeNode, currentDataRoot, 0);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dataPredTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(dataPredTreeNode, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void dataSuccCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getDataSuccCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSuccDataDepend = !doSuccDataDepend;
	if (!doSuccDataDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Data Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Successors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentDataRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Data Dependence")) {
				currentDataRoot = oneChild;
				break;
			}
		}
		if (currentDataRoot == null) {
			currentDataRoot = new DefaultMutableTreeNode("Data Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Data Dependence",isDependency);
			treeModel.insertNodeInto(currentDataRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode dataSuccTreeNode = getDataDependAnns(false);
		if (dataSuccTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(dataSuccTreeNode, currentDataRoot, currentDataRoot.getChildCount());
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(dataSuccTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(dataSuccTreeNode, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void dependencies_WindowClosed(java.awt.event.WindowEvent windowEvent) {
	Dependencies.dependFrame = null;
	dependencyValueViewer.dispose();
	return;
}
/**
 * Comment
 */
public void diverPredCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getDiverPredCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doPredDivergentDepend = !doPredDivergentDepend;
	if (!doPredDivergentDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Divergent Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Predecessors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentDivergentRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Divergent Dependence")) {
				currentDivergentRoot = oneChild;
				break;
			}
		}
		if (currentDivergentRoot == null) {
			currentDivergentRoot = new DefaultMutableTreeNode("Divergent Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Divergent Dependence", isDependency);
			treeModel.insertNodeInto(currentDivergentRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode divergentPredTreeNode = getDivergentDependAnns(true);
		if (divergentPredTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(divergentPredTreeNode, currentDivergentRoot, 0);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(divergentPredTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(divergentPredTreeNode, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void diverSuccCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getDiverSuccCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSuccDivergentDepend = !doSuccDivergentDepend;
	if (!doSuccDivergentDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Divergent Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Successors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentDivergentRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Divergent Dependence")) {
				currentDivergentRoot = oneChild;
				break;
			}
		}
		if (currentDivergentRoot == null) {
			currentDivergentRoot = new DefaultMutableTreeNode("Divergent Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Divergent Dependence", isDependency);
			treeModel.insertNodeInto(currentDivergentRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode divergentSuccTreeNode = getDivergentDependAnns(false);
		if (divergentSuccTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(divergentSuccTreeNode, currentDivergentRoot, currentDivergentRoot.getChildCount());
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(divergentSuccTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(divergentSuccTreeNode, isDependency);
		}
	}

	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:14:27)
 * @return boolean
 */
boolean doDepends() {
	if (doSuccDataDepend || doPredDataDepend || doSuccControlDepend || doPredControlDepend || doSuccReadyDepend || doPredReadyDepend || doSuccSynchDepend || doPredSynchDepend || doSuccInterferDepend || doPredInterferDepend || doSuccDivergentDepend || doPredDivergentDepend)
		return true;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:14:27)
 * @return boolean
 */
private boolean doDependsInConfig(Configuration config) {
	if (config.dataSucc|| config.dataPred || config.controlSucc || config.controlPred || config.readySucc || config.readyPred || config.synchSucc || config.synchPred || config.interferSucc || config.interferPred || config.divergentSucc || config.divergentPred)
		return true;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:14:50)
 * @return boolean
 */
boolean doValues() {
	if (doPredValue || doSuccValue || doSetValue)
		return true;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:14:50)
 * @return boolean
 */
private boolean doValuesInConfig(Configuration config) {
	if (config.valuePred || config.valueSucc || config.valueSet)
		return true;
	return false;
}
private Set enterSetEffectedBy(MethodInfo methodInfo, Set exitMonitors){

	Set enterSet = new ArraySet();
	BuildPDG methodPDG = methodInfo.methodPDG;
	Map readyDependMap = methodPDG.getReadyDependMap();
	if (readyDependMap == null)
		return enterSet;

	for (Iterator keyIt = readyDependMap.keySet().iterator(); keyIt.hasNext();) {
		Stmt keyStmt = (Stmt) keyIt.next();
		List readyOnList = (List) readyDependMap.get(keyStmt);
		if (readyOnListContains(readyOnList, exitMonitors))
			enterSet.add(keyStmt);
	}
	return enterSet;
}
/**
 * Comment
 */
public void forwardToolBarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	//save current configuration
	Configuration config = getCurrentConfig();
	nodeToConfigTable.put(currentNode, config);
	//get the previous configuration
	++currentPositionOfHistoryChain;
	currentNode = historyChain.get(currentPositionOfHistoryChain);
	config = (Configuration) nodeToConfigTable.get(currentNode);
	restoreConfig(config);
	isSetItemByProgram = true;
	getHistoryComboBox().setSelectedItem(currentNode);
	isSetItemByProgram = false;
	if (currentPositionOfHistoryChain == (historyChain.size() - 1))
		getForwardToolBarButton().setEnabled(false);
	if (!getBackToolBarButton().isEnabled())
		getBackToolBarButton().setEnabled(true);
	return;
}
/**
 * Return the AllButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAllDependButton() {
	if (ivjAllDependButton == null) {
		try {
			ivjAllDependButton = new javax.swing.JButton();
			ivjAllDependButton.setName("AllDependButton");
			ivjAllDependButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/all.jpg")));
			ivjAllDependButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjAllDependButton.setText("");
			ivjAllDependButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAllDependButton;
}
/**
 * Return the JButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAllpredButton() {
	if (ivjAllpredButton == null) {
		try {
			ivjAllpredButton = new javax.swing.JButton();
			ivjAllpredButton.setName("AllpredButton");
			ivjAllpredButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjAllpredButton.setText("All pred");
			ivjAllpredButton.setBackground(new java.awt.Color(204,204,255));
			ivjAllpredButton.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjAllpredButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAllpredButton;
}
/**
 * Return the JButton2 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAllsuccButton() {
	if (ivjAllsuccButton == null) {
		try {
			ivjAllsuccButton = new javax.swing.JButton();
			ivjAllsuccButton.setName("AllsuccButton");
			ivjAllsuccButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjAllsuccButton.setText("All succ");
			ivjAllsuccButton.setBackground(new java.awt.Color(204,204,255));
			ivjAllsuccButton.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjAllsuccButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAllsuccButton;
}
/**
 * Return the JButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAllValuesButton() {
	if (ivjAllValuesButton == null) {
		try {
			ivjAllValuesButton = new javax.swing.JButton();
			ivjAllValuesButton.setName("AllValuesButton");
			ivjAllValuesButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjAllValuesButton.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjAllValuesButton.setText("All values");
			ivjAllValuesButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAllValuesButton;
}
/**
 * Return the BackToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBackToolBarButton() {
	if (ivjBackToolBarButton == null) {
		try {
			ivjBackToolBarButton = new javax.swing.JButton();
			ivjBackToolBarButton.setName("BackToolBarButton");
			ivjBackToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjBackToolBarButton.setText("back");
			ivjBackToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjBackToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjBackToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjBackToolBarButton.setIcon(null);
			ivjBackToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjBackToolBarButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			ivjBackToolBarButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackToolBarButton;
}
/**
 * Return the BackwardSliceToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JButton getBackwardSliceToolBarButton() {
	if (ivjBackwardSliceToolBarButton == null) {
		try {
			ivjBackwardSliceToolBarButton = new javax.swing.JButton();
			ivjBackwardSliceToolBarButton.setName("BackwardSliceToolBarButton");
			ivjBackwardSliceToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjBackwardSliceToolBarButton.setText("B-Slice");
			ivjBackwardSliceToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjBackwardSliceToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjBackwardSliceToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjBackwardSliceToolBarButton.setIcon(null);
			ivjBackwardSliceToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjBackwardSliceToolBarButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackwardSliceToolBarButton;
}
/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88G52FB4CAAGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E16D3D8FDCD4D57A87AC061BEDD454D24B966D5AE61B356CAE35EED931661635D8D4EE31E5ADEDD6EC51C64516ED36D15D91C8D4D4D4B4B3CCD2D4D4643F0A004ADF859D910512B6AAC6E604510121E1D4B45277B9675C6739F76E5C0199545E7D3D1F170F0FF74E771C67B94FF94E39677FBDC7CADFB1AA59B0A3DD128C49127C3745A0C919878D52740A3AB844F2E67999A4555F27C08B242685C970AC87
	BF67B7438DE9528D8BD3405D886E1E5A61068F6177A269422274F2E07590CFDC19C1CAEF3C7F3E016339289F6319495D4EAFD361B98F289930BE2009C0E9D24017CBE5BC845C73645F0C8CB3A429FFB68B735BBDCBD5F84AEC912E911EE6204CB9AC4C0DDFE6487E9381E3E12201626530A990E696576E5BFDA5F2387014B1127939B5A3663238D47211BA67885EG79A905BD6C4F8C11A8DDB43F0C252D71E847F60B79D86E515EE3396D565E5CC326EE3345E14A6DB1F7745A2C6D96C72E5952E3812C3B7DE4E9
	E98D7E36DAFA25842EF3541E94AE8FF4867748D406990D4CCE95703B8528A79575FEF5EF392CD75BABFF10B2AF4D7728F7E42A50B7BED551FBDA7ADBB3C64DE572A773FCE0FC79606E84923AACEDC5DDE26622AE0B0AD9580CF9FE06E2A063BC441F6238995C7DC0C173D04767F6200E364EBB2418E70AC74C7720E368BC21DB54BCC5472CCA7F999F71323278B44B6F347990AFD09D108DA8GA89468E3DEC6A7772F07E765A1E7353D2B4B5EDD5953D3E5B3BB765A4D969B7CAEAD859F99BBE0E937BB4C12A44A
	6113FBF2F474C11AB85F358CB292E9BBA8E139891E71DB296F119DA312F552D633E47108A9320C3879CA5AC252185DD29A185DD261B7632BE6E51E63059C1F2A42C5DE9D60E50139FB96C07A97211D0332D11754ABC6CBA19FE60E182923CB63097291E3968B9DA6ACD6F491B6FE6D8C33F19C60A9CB609D82AA81EA816ADB02B6DE76FD51E0B62E6FF6DA9C5DA6DB5331C3BDA607336A1855866F00EC6B473F9C4856FAA478E2FC6693F699D3244E2369C732F46D52E34B9F9134C458A3F409E297917772D6CFF1
	C7AEF1AF9B73A88FAF646F4454A5A28F93893F1C6371AADC646DC79CE7EE69C3284700C478431B999E712188DFC6F282B8DE37D441051C29DC8EF3C7005F68E5FE86113F1798D8FDB8AC8364820D850A840A83CAD96EE748D80EF9EC79BE4F4B7728D1153F02771353CB60E969F3BACC755D3DCED3F73B65F31343EABAE403AACE8E3376B4336BA1973E9C7EFE37C39C3BAC5DD0E3BA2D76EE10EE6A32C0E16A95E1B61DDE4042B8AC5C37595AE501883AFAA4143B6D74A2F85ACC3D4E4FFA4CA627C589A17CEBCF
	33B2F15C74B9C88481AC6CF2E62FF8DE6716436F1665E82BBBE7B0AC8C5C91E41789279742F38F94EE0B8DAAEFEBBBD34364B809E5F4DE03BFBC23A1FC4DA7FE06D8A0638ABFC38CD0F9A6BC0102BE75B3CC838ADE8958AA400082327C8C792B7D8CD1C0FDEBA09F56C21CC04EECBF4352F5FE06F0203675FE06248DFE06B1C0F5B9A09BA8E0231FA1A7977290481987E1737D8C9300B28B28FDE84132BF7D9F3F15A28EFF2ADB7699859608F2BC26C0DB76CBF81AA6839E8F14D6883A82B58275G85820581CD86
	0A87CAAB82FF20C6209E20C020B02049C071C0E945608F5488548394889486B499A89EA8ED937883B58275G85820581CD5E04FD0696E35A8CF77D194DD85A197D181B595A173A02914B8FE6E7BDBF16CF6C49720F6515B772D89E323C617D050D7ABC2C9C606FA8AF64664872D87E178F10C6D696FCCDBFAB172C2C71742E90CFD666D8B946B02CCCEA79D8F9F40BFF1DA807FA7130722E45D8791DA73FBBEA3CC695B7AB7B785B3842B51CA89B4F68160DD5C015126803B277FC9F6D42087501B7FA707BC8FE32
	3AE9391772D6CB22FEE63F37F88833D875FB0597B2374A4FCDC0F3878837E38869E7756A8679779C7919A75B9D432C5561AB524156C9227F22974F47BA58B67919284113D53F8BD43FE769644F82A9BB093739D00F24EE06FAF3B356696F1C4E14F8DBD7E56A35E06B364F818D13452C5470A26C74B32C6CD80EDB3A1D58F3F7764AED4B7BE7D83CF23BF758AEE11B144C75E17D20EAE818FA891F434B8B60A0C35D0A227FA26EEFF2BA9D56C3471C16A60B53E96D6ED078BF66653857250B55AB27F5FDB2CBEB8D
	24319548C6E99D4D73915BAF235DFE74D8CFB3EB1965F63018777707C178604DD85FDED24C46EC61647EA9F7AB7EAB0ADB06D01EBC910B477D3DAEB927F1E84B1108433659A90936F101DC5E597B37DCAEBFF9F299E36FA12BC7D81D425E6135F2F9E66F46C639DC57CA225C3377103D5B6C3DAC134BB1FB77D8DDF490688B203D947F95BC7EA8C8FF82D0569668BBGB901C200A68245812517C03D8FE4868AG9A8B948D1434954AB4D0B3D0BF50E820C820E9C09925509F8132818581058345G251641D82A8C
	7371056249867D3E011A5436D157FD4B3D308D5AF0B05B208D55716E555185EDF82E4A457F97C9646DD71BD85EEA4B3D16126570D8FFE29BB2C715679F4AF97E31A64F67CB22EC8C1667D8B6547116C96E3AE05978FF5BFE7F4BA46A741F9ABC37C9BFB76837C9238D7AED524D06015B1CDB8DFAED0E703B03CF885ACFF42B5A307186B6DEEA37F7F7DBF8C3CAAC1218B3FB351925435AEDF59E341ABBAC4EA66BA90BA479173136E8261135C52D703B278C47B3A7D661F8467C79CDD27E46853A73AD01FF5ABAA2
	22CC6ECF16A9639921535DB3D85BE831D93A40D05576E35DCE015F4747FBB25E1B48316B0EFE894F4C6CEA134D364BE16A69EC365BEDD5A6C755B127535E1DE362094A4D5C38F1FB26FAA6AF27DDB77CB715BCFCAD4B2A5E6CF51F2FABA816C4BCCD0702F9BCCDA73B5BBB9B2163D15DE9E9BFDAE56F4B31BAADDD396C3FA6A70CE72BBBCD5D9D96F3769153F1D32E6904B317E7FBEEBD78F3296B5CD569550A7C26DEA32DA05B68C9DE99A8D9D8A8F47CE479C12E637EE396474973E4071263BF66F13888B9E16D
	B65BCF640AC7354D5E6BEE00035C4F13894CAA47B78755E23489D749E1C94F17E43DFEF45048752A3376BA6D0E1355762EC3F616BB9E935E699A50AD45A5C26EAFBE3E1A4B2DB2359F55B529F8B4DA9C076D0EAE77F42B446A247B10D62A2C06A2CE1BFE15BC167E2D187EC5C72E63FA6E34BBCE189C66F32F6AE19D413EEADB10AF213EEB4FB479DCD63FF58B6B391CAED9F325A827A627695C3E2EE60DC43737F56017352010EC7006D1513169D8FB7B3955D1AD51C79D8B0BC8472A5AEBC47DEA6FF6BA6C36F3
	EB4AF6F7213EEA0A791D662A6739B5E73B3BD0DFF3FD132C67B07BCFC51EDB0FDB9C6738E0EAC57A2A63E6D947C39F28F4BC4785D3AB52D79D3748BA3E1AADF4BCE0B119CF1EDBBBBA34A2FD5531846D2856715C5A512195692B0EDBE59D7F13FFAD5711AF889DBE57C5522AA35557B7DCE9B3E2EC2E3A1EDB13DAF5247A2AEB81693ADAFE077EDF69AF8E56DD14F54CD92D52715C9A33D7AB52C79D0B0AE49DFF3FC668783949F64CF2EE6DF8DCAB52D79D316D3963F3150E67560E4735A2FD4DEBECF7CEAD3AD2
	252345F92ED5D4A47A2A61E6FAE7EEEF7C89573152E66B01ECB9071DD713DA222F3D56C2ECF36E7A1E746B05ACB9377AA992FD568F5B1B9FFF72D354CFC8BF379A2AE57A2CA336B70752AF67BAB6584FF196F75B071E43C5CA9BF3E0AE6AF70E33385BBE749CAED25A95EB065C4F35D9CC0EF31E4B6DDA29BEEB0AED4B4DB329AC72AA27775C16C535CCDFF5AC4636656D6DAADB1EF3AD5B35D2FD56935B172BD21B94BD3D1D57F18B6BF92CDC35211D66B858983F096D0273B21EC39EF95C62ABAA11573E3E7A00
	4D6FA530B944F2BFC3F6B92EFDD5A6B2FFDC7B1297782A6DB61B181A843FD165EC5E712206F455DCE2C539581798GCF61BFFB1F1EFFAC79DF2C6B1FC07E290DFA7EE95B507F92DD7FAC721F37DF4F3F047CAF5375B712FF5A81BD7FB6723FDC575FC97E8B1B747CC7127FCFF47DC348BF3DD94FBFCCC93FC94FFF22127EC3FA7ED314746B7A47A969EF57731736D37AF57D5349FF21D94FBF1B7CFF226BDFCE7E6996BD7FC6723FD2575FCC7ECB8E6B7977117FCFF57D8348FFE9071E7FA8727F192E7FD872DF56
	29679FC17E57687AC713FF06D54FBF1E7C2F5575CF5A017E1F9C5173DFCA7E3F50754FA17F4C23FA7E9564FF1D2EFF738E2CC7F213526059EB797698D4D29636F83E47F2927727A5AD547809AA1A7627A53135B41BEA5DDD6079497AEB921B1359DA7BD112DF1A7C0144762F8919E28DA54C44E476B82CDDA647C93611C05E9F3D27D53DBF3A9374E7FB2983D8BA0184EFF6B75FA7202AF29BAB986FB31BF15F74389DE2AF40E419BF35C25EE74D437DF28B068B8177D820288A91AE56A55C17A598AE3D92728248
	D8A96BD3298E37E2AB061B82F83CAADCDA15BA5CE3942E8D70BE2060AA91AE54A5DCC0A9060B86FC9AD0FA358857D82D8E372F0C528178D820A8B9DCDA0DBA5C42F28C578A3833C6899764926EFE8A9783F802AADCC62DBA5C29ED984E8638FF2D92EE34CB385C6D98AE9E70141DCA386C1D6AF04FD1B8A760812AF021AE612E5A016126831E36CB89173FCB9DEEF785066B87BCC895EE2CCB3877A8DCD29D2431CE89D7DE278EF7EBA5068BGFCB45044BA91CE2AD707BBD605610C001B6B15F07EAE6156D5E338
	4900472A42256CD607FB1A42B583EE5B2D048BF089F7F98D15BF4063D561527628435556D2B9G5C899434C7040BF289B7EBA7064BB07A990A0DCA38E623BA5C9FA9DC8860E3D56122DC42FD3B8B43E56E05B6FF2F922EE52FBA5C6ABA8CB78A70F12AF0D3DC42BDC961B29BC0DE03CADE03BA5CA57598EEB460612AF051AE61AAF7E3386CFDD0276ED342356DD307FB13420582BE91A8E61F88B7DD8E175F486BFAA3FEFF10C758A60E3DB6D312FE1D04FB3C7726293F5FE8754B57DD97666FC52310BF32D13B77
	7D0A19CC6E984047B5E2DDBFB6D93DDF2A3237575EEE65DB0F65FA3B7D83751A3140BABF188D4F43D6C72F339EEA55BE2C7B3BBFE03A323D49B2AC575F3F5E40763EE877DA571A2DCE3B833723D50BB549E6FB355DEC290366231A1654BF37DACEG3F78DEE3C2835AAB3291574C2FAED4FFA3521F3AE8449C8F36C95AAFEC1235DF314D54F9EF4D9032AB7721EC73FE7CBE649E42C2882BEEC4AC0A300904259EC02CF6BFE2B5045DCFD8BFE13B8EA0B61630884262882BEFC2AC2B89318788EBA5ECCFB3E2C104
	CDB4115D882BBD04D8D2B369C7D8B9E13B5A91F39296C1D8A8E175E67A16063087884B780C7433A056CCD8A4E181046DBDCC5F899176B0E189046D6BC02C78F344A693E6A3ECFFA7E22389FB1430E8421A2C08259FC4AC0AB0A3E11F9DC14C1F3047898BA76C60D1442691B605301C966C2B3C3D0C75E77877D5ECE45B175BFB425A5DD1DA3AFBA77BC6C0707D016FDFE4655E5C025FA50570EFA4C240BD96A892C8FCA31132A5D5FE87E3G4B78424FA0462B76AD2C2E71F153017CAE3F5B466A21216EB7906FAB
	D411727B98DA93A635BC35DA77FB29480FD60CE87EC23C277DDFA86FEB6A834B92B301C49A9F24B4C6C098693F54C74FD377F1CBGEF83727FAF5A2030046D83130765F25A5E5A4A76FA8D3231C17E7E443F84638D861913A95E142D6AF8D33E04B682487CA5467BF2C921922F8F73A3D817DE104FECF7367B9EC49A6A36E29A7AC03F30AFB18DAF146B25A176CBAC27738B587E45F33021C136E5826911GF19437229E73B76969E1EBC5DB36173A6761EA6BCAF88EBEFB035F917D2194639E86F247D15C160D6A
	386381DF7A9574753E4238B34A58BB6463643F2C6349746D6C9DBA2BFD9028FFDF996A5F883A0D7C8A753FADC7CF7F89247FB3E7217F3B0BD07F212F2C207EE56528FF9468167AB56A9F1A2B27FF4D57D8861FE8E27BE84FFA830404FA3CC6FA34C09C0124C7232E9DA3C80F4B362B74B89BF348FADC3F8D750806B8523FC1BD5E59202707719B54E35186363778AC37B048BA9C5C0EBA34017CE0526102F5FABACCA69DB26BC80733300450E14E8EEA3BC0FEC69B6AD0291B9F4DED28434A0F188EE73985C15661
	EE52211345CFBA9C5035439452413F03F4B8EBBB74D6208EF1A0BF4B04BAA46A5A215504BADC34330A3D5FE73DFFG4B655ACA5423876298C3FA8C573545D452A37B28DA0F332FAF7EC6FACC03B8F28ED11E1C545343FC886B3B2A2A2157F7D7EEE479F9165B86E47DFFDEC5EDAE6896C67AFF34DBCF7F58C3E8476F0F108EE7DD268ED4D3FB8B720B5BD107A3F9DA9DEC6097580EBAB467B19D4EF259DF5661039A54E1B4480FA69D3EBA2027C33A99F518F41CF4B8EBBB5CC9BAE4037CB6B34D3F6B6A90CCBA18
	4FECA79D063AEE0F65393B96F5888579B12443D7AE6FD506C5EE7FACD81E7FDAFBF67D48C72DEC4CFCB66B7AC27F421D54EE02EE019654BF402027FF0485ED785DFEB97EA12F6A0A78DF207823C1F67AE1FA1F966A45EFBC0C715F103DDD44FF962B351807D76F2276926487138E6F6C5553E1B26990718D7BAE64AC965E65789B6A282D8459999D987F736B7562EF6E4078A7B60978071C85F27CB36A291DE4F1D37C0F166A45BF0562EFDFDDA149FD753348852143ED24C39C484F6A2476690CD68776AE37F622
	8EF1DF486560ACD61A319C9C59CDEDA4489FC3BA7C3CC8CF072924C33CE2073350C26870499EEA9FC1FE0E95F5B0690E254CD62CCFC66CF19FCB655A58D9A35E2FD05319A105AEF3153FE970B8D759E7153F9D37EA67AA0F4F62F3150007DBF12E72258F5837BBB8D7F940526E04FA56E61168BB2119ACADC7ADA7E577B5B313311E1670BB317B9AFCB9E3E332954FCA787DECE63F98F0CBC7209C836583B59EC15DD6268CFAE6C8ED5FGE7066C5C670BDEBDC7701B46BF7B4467CFFCF50D3E7085935FA41F78C2
	09EF77FEDF78A6905F03BE71C5911F7100AFFC31447710CFFC89445750648BDF5AD164FB44A73EAC62EBEC76052F0478A619FC61B3925F6EC33E7035915F03BE71B9094F586E8B5FC862FB48A73E9062EBB07B429726641FCFFC93157C337842B705787E6293DF9C719DB86C8B1FE4C33E47FC62CBA73E668EDF7832096F2FBE7115935F671D3E70B5925F93BE7119092F456A8BDF9F71BD6993DFG717D7708AFFC23086FEFBE710DA53E56233E70C5905F53BE71C5935F1D7C7C29F8F0A7F5691C352241F23A70
	5C28B4BE7FDF5125BDFFEA99479B3B3467CF7DF2B31EBF5586FE933BB07EF9DB34FAE7F7EB4EE9513885DF069B5FA79AF72706CF74A7E62572F32A40AF84E8AC50E420E9C0E9F698135B31DF71ED0A5E5A29EB3F42534229F25E06DCF78CB10D4F387165EA5C4DF6D7BE51FF6C3031B4682CE7615A0A3C0FE7D601FA9F0F8DE485820D850A3053BADDDE0AACF72A9D736C47A5C3CBCF309B1FB64F16766825A7EE69BACCCF3B3DAB57FA28AB7758712EDC53A168101B2DCEC818CB77D842F39C3F2E5226AF9F64B7
	83F576E07AF6ECE569E3678F8C6B21B6E96B5052576A4627CDDF0CA6FDC207655BD2A4F18EC28269B023F4E8BA243971E94BCC5B37FABA6C5C04F6687B96F538EB08BACCF263535AA17ADB3DFC5E7E3927F2ABCF7548799A33DB1B2FEC0DF19A48CCF1E03E06ECCC117549F4E0FACE178DADBD81EEFC5A7434B874EC7ACA8E6A50C3BAD4168FCD07FDEEFCDA9D46686A70FCAE6AB001F4F8F308BAA4EE9BCC07693ABADCC8F6C86BC59DEEF213651D8E378F2AC3DD2F1E8E0524C39B697055762169F0540DCF2BC3
	302E8EF72EC39D42C80785BB0626438AB7BE2D8E313ABA74119DA4A76AF05F90F5F8ECD09DCA1CFABADCC4F6E8A49DCED68CCD078B5D7834BA0C545521146CB00AF458D8B9B49D4A5C7834BACC5155618E32C3BC69706C90F5F8EDD09D320F696645C9542162986AB03AEAE8BA5C6A46275521CFD707465D28C3A06950D2BDB49D8E3B71E9F5085068A05A14F75C7AC22CCD198AE1279D43B6653ABC5C8314FA9C75CC2D991A1E4B5C7834FA569C5733D55081542115F418B0C49D9E9ED407A0DD9DEAC907D05241
	D9BBB49D063B71397519B4BA48FBE11AD8FFDE272FC0FDC059DE7C9DE367CDA5102EA5BB07266BCEB7BE2D2EEDA7746CF52F81F568BB01BA448DD1077F8C2AC3342E8E0D7C5CF536BFF39A69B0EA5750F4782D9B1FD6071CBE5DB638816DD053C77395F5C35361109B1FDBFFC8D707E59BD0078924C3CA7D50F4D862462755A16B241E8E334A188EEC6FDD79C954A1FC08BABCB0288E23F4F538638C1607F124C34F6E2169F05A0DCF2BC33C2E8EFFAD423CC8B905BAE4391D176E1D8E05EEFC399A376D141E8E41
	1BF06C65FF8A579C6A67B2AC965C53CF61FC4043A9CCCFDC83E934DB3B59190A426F3BD96C5B993B435AE16DC6EC98475A6D8EFEE819B883B31A177F146F7C8CD95F215C3BE6FBBEE36D1E597AE72CC54C56BFE36D6159831F7B7978ECCFE730897D3E5A077A15G896C6F0499897B269131B642269266A44C349F311104BDC7D888E16D87908BA3AC06300904D91A901BC2588B0445A97A1990137AC9BF425289B39DC2AC1B306788ABA72C3D9D31C642E288B393E6B1A356C7588B04859056E1C1EC94E131040D
	A54CFA983188427EC9D8B4E1C7BB900BA7AC0E30246F916B6AC4ECA9E12F9216C3185D0AD885E17104B593766D9144BA89FB1D30FE42FA0FA296C858B442C69376973E47FA9C3823894B7E0AE1715F0BF7D7E0B59CCBBA6DE748B80D616E7D1AE19560EEBE0DF54912AD6C9B37B6F2E7F277C8F20F59BC1433DF19BCEF4209B87EB424B886BBCF93E9B98FBBE949FC7E5E7776DAFA7924A4BB18CB76FB5139D852B9A7314154E511705B7C5AAD3364B035CEFBF58155970BD2BD57CD4BD2756B264CD4D6CF765A0F
	B95AAD5A7AE7ED6AD272D3E6CB596E9C260E7C3DA267F71A9C9D96377AEB8B6797FE1E78ABD2BD55F1A22DA9A56A3432F3C0C5FF720DF59E3E81E8B4C10DAF4688D5655AB182ABCB21C093CE63982132G5B34A92771BB0170DCB677DDFE864AA6106D8C2D0164F91A337707B041E75C4B19C8431FCAE62B73AB9F47B0B58DEA7DC201BF8AE8BAD06AB0546F2A2A94C91C271AB58C755B4377540F8477A820F143D03F1A2D6A32BBF91850A7861E5307A93A257A095F1970AC7653FFB76AGEF856A81721743487B
	9F363A2425G5F1B155B54F1874BBC21701C200AA3CA7E9D8B4F848FF1278D07769628E43862AF625EDA6A9277A60CFB4FA6F55CC6192F8D1ECE150C11726F90F8068D570FFBA260D3006200248B5469FE3DD4DD8E593E8D11BF99FBF0AC1CFE8166CF708616BFC6F01B017AAF407C39EE23DE7989827FB1C06197F8AABFFFA8F3C9F7A1267B759CBDF913C1CE8C50F40F727A4ADD6495213C58DCBDF9297EFE06CC20E2FFCF72F23639485BCC73303A7A55011CD6209E0F72263A4A5B027236684A7387B941C021
	9E6585EDF711D702726EDE27A7EF824809820A75A82FEE070B3C2DA86FFBDD7D92C0CE5A88A853A3BC49CBF011478EA29372AE55552F846498015ABC4A8B2BF011C76F688EDD7D1CA0E7A4D008C7F966CA97F9D47E466B6A9786F2A682CD71A8EFE9150BBCAAFF171E54139787F2249FC1BF70C71E647D4DD5DEB14A7B4CED1C43A89B641483B5FA14F7D9350B3CCD782E9795623BEE7E913E6B3F61EDC588384782C57E885F75DF393495B1F2DC536119FAA13CCF97FA0A3B3A46A5EEFA17FE66325F26D8662F03
	E7ABD00FC7F9EF394A23F7E9370BBCFF19BF981E21C093BC4A7BF52D0BBCFA17FE36CEDD87C7497C3170CC5011252634112E7ED960AE51E07277F8BBDD622677AE4260D27F4B3CED70F40E9CB86E119A7F90F00769463DEA17BA6EA2FAC7BFDB486CB8D16619A2BF6360A9DD84E576A2CFF6FC42D59E3D23DB9A54E94916794B615928A3CBCDE60DFF9F3883AE52CB4B45F5AEF1537B3CE203BA6ED1B26FD8F8C68C92F73446BF9E5CC9BF560BFBC73DCB5C746E4FABD3473DD4664D01E7450F7563EE1671CE0DFF
	BF3883F563FE43B5EE2AA7461FD13F8B2301F79CD0A450D420E9BA718BF9B76EF611C77D11270A54E9C939D870E740B37F62016DD82371EF81376DE23D34FC39C75D9FE1FB5CC55C2BDC6CB8CC668D026798151CF07977E4F846F850E9BA602981D0D785A87E224F5EB0176D9569A377816EBEA9E15F5CD249420AFEFFED37D91EFC915F6C3F4167D41E07C4FCE7C04C4967ACCCC055B391FB13E39701407BA85CF1BEE7F1578CC96AA26CAD0E3138F7A4A2564767AC76G4D1F01585B9CE3FC0F916FA9BEE7719A
	44F11230F7B8464E383E09647573B98BF62E619B043D4B31B7003ECAA27D781C45ABC03FA16CAD0E31EF5F76A513FEFC4E629D269FE1EFF30CED42F91930D3FC4E62F6201F91768E476E859A7181694767ACBE845EEA425E6558B3C08F91F65A027A99484E6FF36CF93053828AB70C4FD9A4C3DA7FC8719A787C049530B3048D6758AB607E8B25B7114FCFF4GF689E17E9C7B8C644FA25EE4BEBF71E7F05FC8B67D9147EE826CA542E672790947G6BA2ECA4471AC07EE39447ACBEBF71AFF06F20B8FE4C31CD60
	FE1372F70E0DE360FE0D5E2558D6B666B6DB8E1B0E591C7C7B6CFDBD7235F3005B82F83A3E42FDB545813897FC8747F66F35F49F2BB2B9C4593F65841B23BD6CB0F541F034294752AE55DC42783F7C8A47FAE140BF91A886A8897C32AEC1F9CFFD4DE4B82DCE3ED7BB6092F6EFEAA65BCEAD6B795C69E5126A0C1E137CF3F7F690B51FBF6C366038F27F966D38171F5B8372E2AE41F16529F2DC93CA38847BB22F6FE5FD1962CB21FF89E43E947BB2BB8B54F5CABF6001170ABAE174251E6B33F1BAFE1100CD5560
	E29F1AB10F45BD6DD2318FED8161DFF1BC73B285977B536E617DAE66B6025F144BE8FE0C2F791983E15C9508582739B87F9498085F92341DE1365419BF1037A621ADEFEA505BF3B59564CC8BC4DB76B750BA6365E84B1B7877F7B560EE85F2DE0E363C26CABB3F94F0393047A8F80E850A385C5DEE510045EB7024AB7C8CCB2F5033655DFCFEA0678AE133B904BF44F1238A97365C4FF1FE9E9578CD3E826DF6186F35AA7E091F216EA7087D2314E1B6F07793361B2F0F0C8477A842CED4B0ECAC382790964931B8
	F02786A116D94930CCF067935649319AF03790F6E389662119304D3C8C0F85F794E1BF66D8AC3893882B590669C83B9231715BB9866E9A42D66DE0D8A338BB899B4553568F6ED1043D493131600EA22CDE448B6E6904DD4E31542B208D3D8A3152ED180E9C42E6EFC21D7B896B5A04BA87927662E69EAF38A792E65C423049600EA56CD33ECE1340627DA9E247B9D68C6EE6429E60D8A7387B88DBD84C30GF00FA26C02E254B914300B725DEDFFEBBE597E27D85EBFF5DB67C98B96E5B68B1EA5416EE5DD1455E9
	B93C2C860B32DAC478120D8C8FD761727722FC4E0539F3FE866F50552853B83E2F4C9C826FE088E24BF9FDB08C5C4104BDD8C4E9A4EC9671A690F6E4A37226DCCB651FF8B388FB1F47E13BD69DEF8E2F1B7A818B22F07794A06F98422E4FC1DEA55E489C641DCE619A7211B775670471F02DBFD747FBC248834C1F42ADA45EE0429EDF073CCA3C6F2FC35ED88A17CF3C89048571B80C3FD0477BC7218B43FE0158D744BB12309B56A12F926F436B10F78A051BC53CF104FD4E63A89F250E770C5085B073A8445E48
	C35EBE427E3A8EF915F85FDB073C91942E14620DA66C863E8FA767BAF53C8FF32C9C30666BA84FFB113713B05BEE64D5623DF2B7720E23F0C7B7A2EF64F5349722D07BBEE57CD23CC379702C7925277769D35E0E367CD25BA664F13C6F175AB661773958A68403DF42AFA9CF399D5B2E77B374DC0F5805FD8C7387F790E1F19C9B856EF104EDB8C069A1AC6CG4A0BA7EC9367CD9A8D63675108DD4D31CCF01790963D9F659989B3113C9E42E204FE608EA2ECDF92578F5C6388EBBCC37A91F657BA2C0763895B44
	77A4245FGFD379B903B0AE379602EA36CC9DEEEDA191B305CD40CE398E1BF6277CDCD86F78CE17FB4D03C041993B15E74DFA1F6DFA2461BC958515398EFB1E13FDA0D715691163B91638DA64C46771D0C8677D8422ECFC3DDA288DB4E434501BB0130539CCB3991F439115A1D4598EFB1E1DFB420BCFF429E5B07E99BC3D8F1A326EDACE17FAA4434C59036260F5ECF42DEEF4034650CC12CF28346DBC258DDB998EFABE1999BB15ECE427AA95EFE42D6D23C0104BDB897631DCAD8FE99469BC7584D6598EF5ACD
	084D590671E6907664D64C0FFC42AE59013A549036E5BE6A52C2583093284B98427EFA86F599CB58BEDE562600BB16304756D07E92D6329673B765E6AAAFCA5BC7D8C29146D1C15871CDA84FCC58931BD0DE8FE117ECC7DD7C895BC7E98BA64C4E31F0F0CFA62C166C9CCD585D5C4E522F61772F914B5A06EDF886E1B3F97FB29F5C4D0435F22C935CA3898B63D88838C3891B4E3189600EA24CC66D7AD4426E26FEDEFEA80E7916FD456AF35590C4BE898C06F56C46F4E7EFEECDFDD3633E265AEF76B5FCD3ED04
	9F625B61E7A575374312ED1747EF07EBC2C51B50A2BF2737677B0BF14BB3FC7C4AF88241AFA214E1F33FD6FF7F4CF47CBED96C4D917EBBDC7C59786B608C357F37AE7ECC57C9C9EA7F6BDD7C194EDF98043FC85706AF710C42664DF7CA59C54BC74C75102E345F0874944B4F51DF677B9BDFD916A8646415286DB37621C5A38C9E6458E47EBE79493E2D16775A723EB5EBF7C3FF03633A0B79B9D2B160CEGCA7FAD364BE7E80FFE4EEF051C8A79598C4F4E5FFAEA178FB7B0F97D3F956DEFB2615FF2BCD0050BF6
	F9BA9F237373C040AF65F7B4E662654D866EE104BD476B2C51609EC7D8AB6F4BC703FB9AE1DF73F0A9E1501E05D11B3E9E4BEF89E193B8E686F7FF98FDAF1917497484F7A8D084D08CD052AD70EE81958399EF41306DBCACBB9B388730C0A076FDFF98BCA783455FA24EBD106774F3076964159E31F854EE26C371AD834F33064AFA9AB561D8BAE8FCCDFA3F6C35AE5A3B36997FE0F70CA74BE5FF0186D76FA919A3619CD0F2BE1BF3E6E78DF4B86C473A4DB8BF351C77B552EE853B83795F4A6B6B5C9A193F7CD6
	7DB9ECD6364E483F9BA10C99E8312CD35F2D226CB1926F43333CCDE53AC45E0A6F43E53CBCA67D9E6285AA7EBD5A4C0F4E8FB07E5EB56EB6F0BB01C67EDEEF6EE2B9BF77A764775A6F9457F2BCD4050B7761F715781DE2B878E50E25FEAA5FB7618F6EE042E66C42FABF1C30DBF772B6925C3104757376B5815C99FFC06C8A51BE005BC858C33CEDE983F79FE1ABE8FC9FC0588E0E0501BB628F748DC7892B8723411DG14F19B3465C0365B68FBE26E1FA31FFD4D4E458D38CD1CEFAD7C8F1660BC9272F9D3BE75
	0869401E2887165B85D28F45B1CBD26EFA9F8AE1D946FB0591DBA4FD3F156D3BBCE4F7700DC3D937733164D61CF38B85FDA682CD87CA3DDD1C97AE2F1917323DE1CECB1FD34C13CEE145D616E9AA53FFC7CE727E018D64745D0EF2AE2DD8824FCE3B43FA4A5E6DB4591AC1E2233D57AAA6FF855FCD9558CE0EA63E67ABD87E9C37B81C56F6D7AE4173EE85369DD14433251269ECED47B0FB79BC972FEB46E118B3154C9EDDA6875F1BAA529646D41444BBBD3E845FE9BD4277B5D126F4104BE8310CE70FD35EE991
	5F5E72C5AE692F34D9BB3A599946F25ABB4AB16D452463F565ACDD16EEF6EC3DD98EB70E4219A11C48472EED180FFD63C4BE8642F374B8D126657319363B6663488F15FCAC5D211F0F87787B998EF2A2C8275B4B864F47C74BD047E94417DABEF0BE2EA947FC4C3A83F95A4ADD7371F8B966E3B30599D36E1E0F4716AA7938FF5BC07908F53327FCB45F2164235021E893262D678E347FD51B507E7EF7887B0740B36C8ED10F88BD5F5B6CEA7F3D9FA976EF5F22EF7F6FF83FA88264C4D1FACB0A8637FFD3916AA8
	5D09FC17948DEC7F9B0B507E794473EC112B7D5FAAC27B37D118F5456E76AFDB26587F6AE234FF4F1D6EF567ACD9CFECF7D95B486C7F310C7B838F1E45ABF478DBBE75256F2471925F9BB69A5C63883B5D08E33CE842B697E21B91FFA7566F7F4981F3CB95F7413894288FA890A88CA882A896C88A07768E28822895289FA884E8ACD09450B42034BFC21D89E484B283758385810581C5G457E91634B584862E377A230BB8FD2460BBB8D32470BBB8B6A460BBB84587DG1D6345797F6CEC7F116345DD856C9E82
	F676BDBB571E1D9DDFA21FB5BFE63CB843BEF23CB8631F1D5FAFFF3336A15060EA73FED9AF16D6EDFE30F4683DA731B29E2643437D6F9276885261D75BE520360B591B59137D1EA6BFB343DDF9AAB471333C8A899FFA1B6C927FA07A35EA622E70A2DEC9AEB3E47BF0F53BBFA7C71BB79BA4F16E7ECC9DD92BBC44D1A4BFBD759B587B4376BCEF5471DB8D3496E85DA069E07D6D95726FCF6567A2CD986D5EEC76EEFFFC8E7205D141A07E499A77E2AFE432F14007AA77C72AF4AC129E5968DADFAFE2C9117DBE5A
	25DFDF6F64EB91F1708ECE9F0FE303670A307E4B3A5B3D3C285B9EC6A590069136BF463B1576C76871B257C3447793BEFF5682E14CF7E3FCA60AAF50C3FC498344B77AEEED3FE5C99E5AC15EB717271F76AF79FCF598700713AECF9620AE31BA3AA4CA8327BD61EEED5B7BA61FC3CC19GED44840C43120FF1D4CC500FE32074B6CF5026776EC25774E6956927F79457258D78ED244BBA52A5D8C797359EFA3A04CE5026B71B4F830C83FCA245310B6208779047C069CD7A13B63DED1A745ED7281F5E97F8FA5300
	BF63CF280B13F4297B133BAEB387C9EF6B1F3469FD0B2F81F4826E24B8B2A90ED19E6298A83DE35D527BB3CDFA5F73105EB1BC3D9300BF12F419CD3ACC5751E556A069CD3DC71B5E35BCFF17821EF58F46311F62E83CC7BF0E0152EB3EC71B5EE753DD533BF331FEFA5F62EBEABD405FCF3AD47722AEE3F4F419A33926B79C42B0C2DDA63B25F7BCDFFB0B86BC1662983E9163C83FD7BF0E648162483ED795071843AE44790862FBE97D259867A3DA88F350DE955B3D58D7DA4B4F9B8E87F7B4D082505209A0E7A2
	7AEF627E6C4EA31BFCD7913B17075D39A37C6F48F559DBB951B5BD89BA69E3F195CF9C389F91FD2F3A4F7028CB9C33F45E873DBAF030B905E4D558C5D2942F738F4378C2645BBFF95D5E8CFA36CD44FE7F4F7BDC5B47CBD997C366CFBC29DF46BE66E5AC88E40C820A1C48302A83CB8750CB70BD49579A536F03FE78FD983FD1937F61CF1478EF69530FFF92DFC7EA8699ED77E17CEF6C9FBC7E9F73788300A71862FFC7937F7695CA7C07137463BF4D57C2A3C146940A3F634CC0710B71E54CBA8C13F43F98DFE6
	40B37FFEB19690F1862FF655673F190ABE8F9B7575391B2F5355011C667BD14E01B32EF9BF034FF78F837F007B711D2DCD45F7F6427D6EE5F72EB4F0BD98F53F360E3AF6067B983210F5B36574AF1A01691FAA27FFBABCD37FAC46C1A27CECF64D061C2ED779DAED9678678389ACC82C55023B734F180EBF2E46F4047CD9BF9D835535E1FF56764D1EDB2958BCFE2B6B5879CA3EE69B8EBC91FF46F90348B4CCD7CD04C8D7ABBCFBA244D8CE4879679A57FC3DF31592473E357A79FAA30FEBA448890A4034DE3218
	72ACC2BF2D6A3E28361E0D0AD052AAF49FCE7B75264A3ACF07E76AA4B1A6953ADE1125644983DF70BC817F7CC918A74B78FCC2AB38BBA7211E5B7AA8CFA6396BB9FF103295B6C91BA7C7D6AB6568488654B9FC1250B98A1E31134498DA042FDA2368DC4B7B575283102E87D0679BF87BD2826E1A87D0672D24F35F837ABA6B1523D5286783DA1D172DD5F47E228CF58EFEC0689C461E8F08713F887FC93622F3B11F830F867FD85279833E17B07DC128BF9FC46CB6BE3797826EB18FE2BA9A66E3BA229F54CF07C1
	A79DE87B7887356978F736120E77281ECBF8D024A3BD524F109DA95AD0395EB43856BD7DFC3D3C824298A3B12D8F2FD3525A41574907B13FC82CCFAFA89A3CBEDDDD0CE146C58ADDA643B3A6D23465A21EDB57AB71C8FCBFE0827827BCC476937B9A41DD719046FDF87EC0F10BB47EE83EFBDD77638D0A1D3AA93F1D8F895D8260B96AA131B6A1E4BCDE6EDAA770A3EDE5DD77727C9F4742BF0461BFA999FC4EF0EB894E2B4593DFEF4940F3061714601CE166434873679257FA6F69921CB3EC24B073373A5B2014
	CDCF48F30697503851D3DFE520B95B3607357D4ED81A3335BD0CE7D93CA8769702BB64E1BCF342124EB262F35391779B8181ECF458FB4094A7854A8FC6177BE4B1DF79F246A8765B40F24D38869F743B9B24160CF40F778406BFAC5E47E879195F186F4F1EA2AD7B73D819C9FD845EA7A0A31099288FA87091CCDBE8BA53111D4E896AD7BA1D2676CEFE03A29E5A49AEB5D4E03621DE48F666B3CD050C5753D7FB328FC858EDBAEE5AF76808255D59EC6A1068DBC31733BAC703BE639ED16A1648C75C6B99318E79
	BEDF7F1F7A08F63DFEB63F97E91A8A976B1317533DD3A913619D1DCC7B45781A41E4F047917674BADC2F177E0258418D38DE1FC5580DFBE9ED7EAF5847377135D9B3387B89BB41B176CDB9BB9BCBE06F6D556EBFE761713779AF034F9D8EC4A20E8F9B34F12867413473F13E12F8D74337382F9BDECEE70285C3BA4200E200267FC524DF707D0B3F63B653C97BB1A7724D213AB96DD1A82BC00DC066C755FC5D9B747BB4A378982E9F420EFC94439EABE47D169E076530456130181B2C2758779CA27C253C1D9E8B
	E1A7GC90FB16C676BB387C8EF5C23A2CF046CA75633F7314B5AED6DBA5645A58B392F71B1FB9D48EBFE8C75F87394EB9B6BDC6F691365583EE3F7D7D79FF33043E71A1CDDCE23691045A67C365B66717789EA121DB67B896EC35FD2A727239F1E72E96965E7AE887FEB3EE47A09EFD4842340EF7C04592F06DD6161A26FE09A4759559AAE7853CD0BC90EA6A276EE713B9A1EF43907E758E3A23FC6BF06F7B9ACAE52739FC77E4792757CA349BF76341E7FD4727F67CABD7FE90FE1DBBD0CCD01635CAC3F270DAE
	2628365B6C0E5C931DD627FCDF25150F7BF9BD9165E7D8A109F9EC5E4F63FF71BB5CB1EC23DA0A59EFD1F7E774E25D9D37ABD4B2DF12B7827BF35AF2D591A54AD5E014326FC9F907DF207D9B91609F8D94AF07CB7A2B1FE169DFBD550F09FC5FD24EDF357BB766F23CDC050B7A714A3D38FF4388FEE1FF45BAAE38816B477447917BF7835605590455B5E0DDD8CE189F47BA41BD6CF12C77D6D53176BF085C9B391B5D6DA15CFF289C4A39AF73E4195E550B8F8FB98EEF6ACCF90E244A3DFEFB194ED58EFBDC54EF
	91700CFEDC3EA712579F85FCEF4CB44024A7307E18341357B526BECDDD73AC5FB3138361CAA86CCFF630725DEE6F6A31F7C30D324FE1057FCDCA1F6CB73CDF5F89611D44F3D30967FA729E5EBF9B83E147D27898FEEE0589F7A834E058845E16CE05F0531E40BE6F66ED0377F9BF5F26DF17F6733EEA56949893CE4138FFFC022D4BD4E96F93A5D92F707BF3F66A5C75A97C8FE5AD1379756F08C1B9BD4E0F94B9FA4164F04E7D8B61D96DF6FFA7560DCBB66B5DE353BA856F3C8D5F0A6FDD8FE0E32660F7F1A5A7
	59A0CF7BDDDC2EE8E8CBCB2BD986C373D7A59ADED1F7A515E2BFF66A945C97D6486F8BDD7AA45890286EC93C037AD4A256CF896BEE13EC3715686E6F4CB83AF6C45B13623D72FFD2291FCAAA7DE708BAEF65E609ECE12BE0F5E33765C426EB7D1ADD18DF1C43E95326F5F2588995EC82C71D34A6763F325D363A36213976G49CE2F9CA4FC553E6666FDFBA93C196F7169BEE633F92E2F7F23527DF439EF7ABCD24E72693049564B5E95398E5D06F9B981ECB57DC97CAE7169ED18AF79CFE13E4C61F7D6F602FB98
	D070D358D63D31C551694AB26F6D79D219EF7AEF9BAC3D9AFBDED20E7694FABED8226839FB133BAD46EFC2DB04C15AE21FC2DBFC3099ED1179B734C5B13F6320855CBD8C7B9B5AE262D6A50EA5C55E5B62C811EF3698BFD8F80DAD669523ADC41A9EAC46BE43A852BD24D4517D75FC35FD643939BD6E6D4361BDD8D7CE791BE89F62611994AD76634A6BECFC5FCA86E05951B8AF33379047434D516EED6082C9998B33762F934274C70B71B043822335732D57FF497A24B51A7BFE65B13F957D5457998A3F5FFCB7
	CB76133B10323C4CBD6C7D2BF63F7BD676FF6323D445DF557F7DDD2B0ACF9D1D4C3765D3568E9DE077163A74D937ADDC0038CBEC14FFF672F7110A7E9FFF4B6A42FA798ED05734B4FE132E72F3374F4D1F297D5DED346DA35E4FE6F74E3A687C3B7485086B6ADCF20A7CF5F5DEDD49F42ED45F0FC8FE27664AFE4A45F9CABD406FA82B565EE9C77E359DACEF9AD4F77E49E53AC4600AC8D95EBC163E5A2E9E67C9F910A070B55F4D97BA3844A27CE2A54653446E8AF53149AF8EADC0DC57A6B53B48DFE39351765E
	1F239D973077A8A49A478D068DFA7EE164EFDC2767BF117C0F6A7ACFA17FCEDD7FB8767E71EF5A1FDB207D261D79CBCFC3DF7BE91431EE1E1E0C1C27311E3932CC29E7A6F8D5C70ABAEB9155D995CF6B0DF3300D6CAAD7FE9F5E2644354307382EA842387AC86F6D5B9539DBF6A8F2EFAF70DE6E6CC2149BC6F267D7A8F2D3AA953955795E4B3DAA9F6546125C29150A5CA72B9439B3FD107BDF121B79F714FBCB35A277669AC56EEA9F6476115C9612BB3CD611FB59CEC56E819F645EC0F283C9EE47AEC56E78BA
	C56EFBF95E4B5DCFF2A3C86ED575AAFDF7ABF2E3FD10DBC6F225E7C85FBD0A5CB623A2B74F87BB8CA739A5A437E42FA2773686C56E273D5E4BF576225C1EE786FA87AF5D277C3E30D1092BED2377F17DFEA346B50652704DFEA6CB74BD7A8AE93D8E7C2783E5CE75B394CF955F86093E47C1DEE79881EB190AFD0F0B685B967F297A7B2B701B9A76BB984204CED57A9E9326EA7B9E7FB84042FE2E3D33197C6F7F107CF55B417A2F5348DF396BD878A5EF145BA0752D25B2DFF9A93503AE3EE877A792953B1F6A4B
	565AFDA7F4D232D71A2DA61B3DA33747E632F62F4C494C4F49ACA8DCA778A7A7A97C19873C49B7B1CEFB47217E8EAF21F92F6EB82D7971549111327D63273A4EA30978674CD4624F9E027EFCEB024C7F177D5E68AF0F31E6A9FCCF8CA15EB7E6AB7C35E73C09D70CBFD2576378A36BD998EFBF0B630F6675B87E88FA96479F43F958C8F04730F04F627B91B7C7097BE12F6A8BF91DFF2E2ABEC875A54DC2476329782E26BF073A7C1DE729C8DB598C1A2B83FF7F67B0ED974C403405BF07E93B05071D866E54677D
	8CD94F233CA7662B6A11535E276D21858A5F4FD67B1E36D7D7E35A4AC9179B96AA755B0CE50AFC7F34AE15FC9C1B55F719BAAC756D766EECF61CB1BB48BDFBDDAE7CBCE091A7A4E72F4C33180F659D6DBD1657EE6D4DBBE46AB6DB9C263C9EF3C7AF1B46F16419696026BCAB13551BE73259F20F74F42CDC370EECBCAD8DED6C84BDC31FC79B17AFC09B47BC0FB6BE3200054D08017A9248980369FAC71516230B3D2F2B47AFC69B19C956E39FAB79FFF3836A96827ED1B1281B78561E69167A8F54ADF39FBF7F88
	5C2DC04EFF203C3F2876E33DD068FD7EDF192970FD64537BACF46C6E43348510AEDD1FAAE93BE28326EDA278A77C83537654867A7E6985CCDB8ABF1B4686EEFF201097D05EE155BE1BEFFDC8DBEB164237F888E96B1907E99BCB3A942FD65266D706E90B837F2C97B1ED1368FB285697B1EDEF7130C1608E851A78A275BB56286A242D5E272DED2D42573D437E433CD77C0AF0FC33049E1F2FD374D8BF5FF79B1F190FB60EA61B642ED7EC3CC6C29B2747423B980BB66E1650467E31E863909EB69C5CD1C0F131A8
	AFABC751717EB53E37D94BB7AA7CA62F46C9C2773D4528FB52CBFE067217D077AB0BD1F767CB287B3DBCECA838A782C53F043AAF49D5629E3E5D7B72111A2F70D57914AFC2772B28ECCF83DDF27E093A7F1D4A36791F28FBAADF238F8177D82048FF226E49850A8E89DB3C1F6FEBAC71ED3EEF74E061B573FD53CADC67BE1FD669793E4B5C275887913711B556F4163E588AA27B3D31110D672D5DD6A75852FA0A2F4CB4C1406E8E311F62B7057C5EF6300974B25491C0A5C04617F11F4609DCD639D59E525B23D1
	EB6E30E0A2B03E7F66F93E0B401447767D48F791B02EF65055E1E51F1376A27F9171BD079DBA43DDCCBE62BDF92CC3693477F0D0E04772D8E74FE16D68F40A20F84F8B5F9FEC339C6630FCEE937FF6111D135079B22E97BE359577DD849076C5992EAB063D0CEB0449FC1DA98A5C710425F10C1D8918960758CF4B3CDD67E3F270F7544B5E6D3D90F104F99D47D076DE48732A651E57676E6769AE06B4D744613E518F5663D84E8618BFD0A85004B8F1E622600B635FAFCC81ACA68E77BF0C5F1BB900AEE92F2877
	BF041A59BBACAFF929769D6C1F434A1BF6270028A36EA820BEB948EAFD85573A12B3D9A369F12D2B56595EE94115AEBC8341D0C87D78D730DE49AB647DF8F047GA53C02EBECC5C529F25874D761DDFA9563ADE84BD0474BAA195252DD8E2B394A54E164B373A22E6F68FBD97FD769DCC9AEB39C5CD1C0F12FD27DDB2C548DCDBE74BDDED5717573FDA8AEFCB5D6F853FB212E485E148F556E667C7CF5A26CDD6B59BAC23505CDB8635E035CBE755E03481A5A918BA47D3D87C92F497B45DE539DB32A5647FE116B
	0DCE721DA007BD5C8942E7F705CD53781C8A53577C9A3695C67A46B8043050C48C97C5588A3A03ABB59E31CFD7B10C5F11960F6FFF374BBD8AE3G8F0F9F785D96F5705B1B591C46E4889B871476AFBFC3B6D0B9D0B3D0A7D0BFD0A050982089C0D1C031C089C0292F7B99B2010ADF47BA7B5AE256697134EFD0AEF03C62F618447DD8226EFB1776DE543D0E756163C62CB7DB89EBEC40FAF398E18F6C43BD9A23892B277DEA63DEC79B4DEAE0EDDB8C3997F1F7BE5D27FC0D5BBEB5EF0849733E8E9DBB24B83C2D
	C3C5FD77A7F107589BFE0674B7308EDC41EDD481EE639BD887FE5310B9002C3EB754F5E0F0037E3E05B09ED7G048DFE8377AD1CBA4567E9C4FDD95FAD2F0D087AE9D1B556CF93A1FC429BD426F75218E79A56BB7BF672B18F387D01C226E13D73EB55FA6C0DBB3D2FF7AED02D31766C785473BBDE144FDE7282F84929FE78D23546B9220455612A785CB72064FE563027E15F418662AFD22DDD5CD33E5CF35C9071A69E2F7B9C69549D02FF5136D53A714BFD3FEFAA0D753B9A7642EF2C5B4A1538F3CBB2BD47DD
	D8D8A212AD72E4AFDF3FE6BA0C1D066F48BDEE7717EBE9691B2E6F03601BBCA81F76BEC0791DF53BA7FCB092FB3FFE457702943F294C398A7CF79C2FD06122DC1614E239B402DF581B38B77AD5FE1FD37A3F7D8C79C0F5C0EDC04300C27E0DF55E8B45ECC0811DD24809AEFB37DC736D351BADB66C2EB65BFB0C50B135292E5C136B9C373B2A3D33277B5D585E597339C179746DF9066F61887B37561EA3B99E7EEF2DBD97ECC7FBC600DF4EDBD8FEA6EE9ADAFA9F9D14CFBF3DF3B68FAD3DE378BE0D403734693D
	0563A1EFE953DB3A9953BB867CD227E3FAA7972B636F815CFF7A406D0510F7699E14978C61A7CF47FA7271BDD8CF263C0D75E4121117E9F037G753C0D756457FC5DD13C779B3C6A1F09B8CE53DA6348373D5D979A3FC34439192FEF8AB96F66E05FE5425B28FF4986442693F641BA44727F0358AF896BA46C97040DA2EC59C26CC7CD798F5A63B01F23CCFB877AB2C0956FE05836F9485F728E664FD2177C8984FC74BB83670FBC5EAAD2730D831EC82F78663B1C25BB95F826F945F7CD3E1AAFA5414F1011608D
	5FB7AEFC7940D363955F77AEFCAD40E3730A6FE6973EE1409364955F67AEFCE300A75CAB3E8A973E49409363955F05AEFC5301A775DDEF78CE762A79B2012758AB3EF1AE77305581CF6BA0FC229E98D50875C08F049F73AE0E670A8ADD6E369663B931F3DEEC33125F41D8FA17273E0B6F47F7FCBC3774BDE853006A5E437AA22FC212303E6848753E3E38137611373D676BBE7219FC2F01E8EB1F2DE4EDEDB048988BB485A89EA869FDA86B6FE3DB1B1E4C666BBC0DAFC462156105307D4275C3EBF3B2876553EF
	F33E9814CFCB22ED79B7DF1B4BFFDF5B663C4F71727735ED4E4875D8B66A40AF74FD2C533ECBD5471FA68DB7E4818DDEC6ABE78C4DCE7B866553375348C179746D34BA11253B845264EA27DC0E5728F0E1275BE7201D9A41EFAC10087F0D556A78179A069BF28C5E5869BFE9C333D34A02215929F2D0BEFDBB7D1D370395862D1DDE6438512035D34F8234D38B78059B50CE77AEF62973E68CB7944F7046CEE18DC3335378C179746D745620FC7AF68A626B06F5B334F63A16634DB334F6DA338F6D5486FE93E720
	1DFA7B5471E7A78EB714A7FAE327608DC333532F8665533753B30372695B291F2FC1B6A6EA6DB402632D09DABB4D4BC1BBF502DFE4A25A6958BC17BEC952F0C3CD12B7F63A30ECE8F63AF2D0BEFDBBBDBCA81F3E1D0E72B51F16A42D1D1C9CB7A7E96D74EE99357160971504F6721F2F0E3FA4F93841186C0D1DF224215929F4D0BEFDBB758E4A27EF27B483CBF7DB3256CE9FF15C162C3553AFA534D39F78C5A7231D1A0A0716DE5320FC7A693DFAD0BE7D74EE615F89D4FC20CDEF85470D9FE853BB29186AE370
	8B7FG539BB244776066A13E874F8F71BD98464BFB66CCEDFAAF62F84ECCEDFA9716E3FACB40AF84C874797ED068F9DDAF3CF070F53D7BF89F58E26E30B41F6C116FGFF20102DA5588F9F6E35507A61A305E904557762777DE21D692292DCE712D206736FFC1742B387085D6FAD4224145299A600333528D6F87648E1C642935A5832413F918BECE23F69BEB8173D65776D67FB776CE6CB35436AEC6A31D965EB58643B437E1D2B16BFD90EFBBABCD3E78D47FEBE5F07AB64BD3A0E653967394842A268664B7913
	CB7D4DAC10558AA446A2AF64B27BE946A2E28DCB9E0D48FB3D147DE840AB46A22345FEB4F047GA5100E592A7D41A1FB3DDF9F7F5BDE26D3035D616C2472FBF741505E1707866553FFDFBEF03BAF473B7765FABE56CB1F2DFDDFC2B91EB9DB7B3EEC2231E18E78854E467AE1EB112F718B3EDDC3649B66B31FFC6EBA2F5F263925F78D4725B95A747E0E56B6D3412F87C80C9B6756E642B38A5C71C0692970DE82D5G35G59007CD331AEA95CB45078D194DA6D78F1F13EDE7AC6250A36F8ACBCA30022D31536D9
	706D5735CB3C9CAEE96EF043523943F54A5DAD09FAFCB9F2588AF8B683F54E55461765F227EC3F6C9FA8BFC743F39CD0249B5F1AD5FAEBD2D3D56126416F14F96EFD0F8C8D16AF3BEBD438D80F9B416788DA6609BC95580FB956A3E3A2FF66EE71DC5727EF993C2EDF3EC5FF8F472AAD6EFBB83237686F61485D225E43A13022AD2CDE69F0D84DAD942E0430AF885B46E5B26C20554C6BA001D7F33D98DEE7E191A2DE4F75BA4131963C6BD605FDA1E3CD7C6C89C8F2275D013CADDB7899C1D61BCD38DBF998EBB7
	B4DFE48F9357515AE3D269FD18B02637FC66B8DF9F8C003C8886926D597BC6ECCBA64E93EDDEB4BC630158799EA24CC9BA7BAAE53EE873B261D9BCDF0491325F60320D0035G093547B69F76DF4CEAE779AE0A83350AF4E726FC36156A3B0F179B5C5A8B559A57262DE205CD70356FD278363973F9ECE77A9B54FB9164333ED55F0884D48E24C3E13150C19E0F558E2D5EFDE508FCEBF68E2D3E3E172F477A4F575657119C8F1C2F2D2FBF5B0975F5884B77B4EC1F8EECD657B766B4D1D77441B3A0CD29B7C6493F
	4742B3A2CDFF5C938DF8BCD0528245FFB94F03890B6672F741E66D36305E52AECBB725BF142F654AE7B028FEEFAF9D6AD9895E5B7E60F90DC33C5FEF15FB2EC35FABE77513DC079E3693CF52B60677189CE2BBAC613337317EA66D6FEB32B81D566E8E45FF61B696D76F31C3BD4A4AA6467551B6D64F58CFF4733DCCA27DA336AB763ED4757B436D675F7679BFC09C3F5D2C2469F6556F0A4D67BF6EAF4EEB9C224DFE31583D9D0FABD63763C217A037BBD83DCB43AF074877B71F79C45D554757CE17AE5056E9FE
	9C4FDA202D53E69B31CE4B873F2085D827DD6052671F8EF86AC2EF66EAE670F521CCCD58E2F05781358275AC546BA3EE63F3F87E9A3FE0F00782CDG0A52659B390E7145EA7C92401D36885AFD2012C5FAFCB7F2BE23462F8D5CCE2011C0A13AFC23B9DF1846EFA23827G4581C969FAFCAB96EA73B3DD8E178D4F72F4F75B0AFCFB0A4FE1B726EB7373F90E3726EB7353C6F35E1D6097190E79E961F314790BDD632981F78B108DE81846CF3C8BF9BA6FC7314B7BA1305F9639077BFD11FB38B5056E61B6943A07
	9377A03B063BBD5FBD5CF11DF05F69048B528977B31DF0FF5289F73FCE38AE1DF08E1DF087F542356A047B39CE386BF54285763A073B3257BD5CA39B5D43C5EDF42F57FE657358DE5E77B7C43ED7DC7802963B177BB19ACC147757441E5D455A77E0BA47A396EB5F83BF9ADBC701DF4992FC8F6EF659D79A82F85812016BB551767F3352F39F64DF15030F637ED329BF0E132A5C47F149D57A6338B9D56AF11CC8CF827FFEC65EDB3FC179FD697A735FBE5FF8DE63106F0CCBF5AF47BFCDF5AF479B921534972BFE
	7FF1467937436367B58E116659EB5C6DB0FF0D3B9DE296A8E9FFC5757B780273EF074B4EEB9C72F7A60B5D6D704CE2F7BBDC3DCFC97B752A5FB97B4E3F9DEA4FEB9C72DCD31F3B9DAE6BF33743709CA56D972BFE27671CFFBBECBC2FF108B4874CF3374365735C6D70ED391276D32A5F0965675F8E9967B58E1166EB663B5BE154FCF7BBACB1A8E9DF217ABDC6BA7FF678538F904736CDCA1AF62AFEBFD2FC7E630E7D816278112A6CDE267A3D7487A8478567B58E51EF3B051FCD453FA107FED982A33E5676B61D
	E1DE7CA12E352D6567AF5A406D8F9482A4E464535E77310045FD08B27A680C452C25A8C36C2BEF853793A8GC8FC077570E675F7D86C5BAACFF7E80FDEAA7A0F9172933F4392EB0957643AFC2726FCCFC5CB1DC2375CBC5C771B72916AD6144F5757415D8254739176E10BFD5E0FA7782A0648775D90777FAD6567810C7CC85BE77F1463C19FE97B6CB753FA6CA870CBDA06693D29D0A967372AFE17951E7FB25F7C8344312AD8C9D30E6A779F0A4EFF5C8F1F57B84438E5E92D6731530A5A4147CE59357AE3275C
	DA7731D3D12D7E58E9EB2D7B9A58F6D9AEDBA3EDB0F5115C1ADA762E0B739D771A3ACD9DFC3ED73429B9FCCC5E89E5338FC86EEF646337F97E04BD443F8B0882F79CE1E1FC1EC37AF838A173E344BA799CC9B138EB88FB288865B592666472C601FB9CE1917C7B4AC8F0C79336B189F9E389FB1B07AB4998EEE84CC06CB7275DF5A9635FE6365E60E7B0D3B8BFFE66CBBAE0FD043D301A52CBD851CA0CA30A30CBD6E29C3104552DC2F989045D348AF9D316A3161845308CF0D790F6B08B75EBA6ECDAAF6A52C958
	CD3B55710AFA58340655AD72377BFCA9D36099FDF3959C17B265752F2F7939E6E27D6BC123B75F3809FAECAD1D47BD8EF40ADF0E6D0FFDAF36BF591FE09DFFAD8F5B866EBE2040CF308D3B2E816B42B100CD7D84E53CC46744ACDD01B2BE647BB79B415D89B4EC856EB9B928BA0BED3BD7FBCEC41DB0E927D287BD2A7A7D654E73DF9FB94FEB9C723B443F83AC7E091FA1F8851619D7799A3974EBBFC3B8E1AF515DF29104DD414365C33866DF633739F7F12C935CA3890B61D888382309B71B77E1A6G96CF5877
	D4F7A490B60E479B9B64E7B0E6E29BF85A286443052A5FE946731FA76B4EEB9CA24DD76B4C8B7FC2E7DE78A3BE075986F66949C4FB3D456F00B1C3BE799356329973AE10304ABC3475E8420EF27BCF83F76A2708B5EF40B8B2899B4DF531DD6BE7A8A6AC1957537D00D590664841B81A89DB4A77050C86F7B8E1625BAF9647E4426AF99C46DF7899E2887BAD47DA810BA76C904511349231295CAE4D4CCD58F80AE398E143393C72D1FE06A042DE947182B60A30FA0AE3ACE1AF71B812D68DB7E42CC26CAE0AA31F
	30B1C2671F7B99EA881B4BEDE5832C1130DD9407193057F99CE3419DC958E67ECD9C3B83FCAAE1A7967177905A0224AC442E1E0F7216E661FBB3D5B58776126A376387188F3B7887188F6B1F07766FA4BBBC104E301C6B7C8C7D046571B932F240C692B6056C95C2583D7C0C34B8F0A72DC6AC09675D52556A7770D30E6583D6319AED7DE611E25F77D43FAF7C817A31234EEB9C72798DBAFB3E7E663267CBDEA7AF995A7E4C7B8665535F1F795E20FCDA926330EB787839F935F6FCB61A63ED2B3563330DDB31CF
	E2833F49146F1BFD7E26DD70558C116F7BA1FEC33F0CFF17D03CC61B5ED59C2FD823CDEF28728DBD78052D417D19257C2C777435D06781B58359000681852F45FD19436B588B60E9DF66813B1D0F7D597968A6EB37FA0B261C4622BC164650357AEBD393B4F89438C5DAEE64F38631EBC5DA143B35EE65F8028A97E95C4AF166CE4B9EEEF0E6E31EFE22DACBDA2B7A9D76832CABCDBA2FF16019522E776C0D64FDAAF1AE6E983A27F9CA363B7D310C60397CE660E134D836E3DC36D20EC4DCB17C5E2B43F6074575
	5E2B845E4F188E6113568D47BB768A59F7254AFDFE15F8D38B5E9BC35F3FE6914F19A25734346DD55292C3F746B72E53CF4B2C8152E2DE27CD4B459E52F2BDCFCB8F046FA73D1EC9F4552B24C1512BA6096E827520D7220A42A08CA354EB221BDEE9C97AFAE5737170E4889F8DA430CEBE4ECDGF74AFA9CAB256DD3F4FB176E0EADDF2F2FDB320EEE9F4BF863FAC5B791DF6BCA5421F5BD6AF09B47186D7BC907D82E0370FFF495720CD90F76DC16B578DD0F1B32D0EE8445B50CE35340ADED98AE1F7DBC4709E9
	AC5E201F4619832431EE0312C6F92DE76F505A3C32C179745BBC6720FC7AED4082BE2EEE5E20ED8316F13CED03368D38FEAF35F960B7F983560FB77A7CED26BC27B7C43ED70778AD6845FCEE20B8C71BDE710DE8C50EB63D4B69DBD0A3780565605E69EBB7633E68740D43795D25B9702CG8A380D4E431F23145F05FCCC56827E660DD88E6752FD21019B314CDF44BF0D90E5B2A8FF203BC1E7CA835F8DBAEA23763DFB164E5E8C5D086FD07EC657BBCCF772714684700FA41D0266A969706363483ADCE867F371
	5DE949C3BD07658E5EBEE86F528D4A5556DD4373756B2E2B7958F494048F4DC59BFE4F6B59C8F0CF49C5FDED7395FDF345BE2CBC30FB9E6AFBD18E6A9B18672EEF62A07A0E4E536A1B172B2F6FAE5E978E037061F928EF849F9FC603BBB68F752DC9D374FD1A37E766FC984F67233EA7C95F10FCF7FD3565406D1E68FC2D3EEFED5457F7BE57B79C42C764233EAFF2FDE341BDAD9F754DD820683B045B57D9G7D3D8254370D748DADF057F756A07ACEA8506AABF9302F18C7080470D38AD05F982E6FF4F0A79522
	3E53D47A16F3FD7D41AF3090757D1A748DAFF457D7FB872FD65F4905DAFDE7F95077D32EEFB4040FAD44FA641B0574FDC551F0C3E611083F991EE3466377952378585E86D8DF916AFC8B5DDFBD3A48DD67859A1D47C118482261F4D74154A2ED5DF0839FB74781BE3D886D9639D0315B57FC3C5CD78C7197238E2F118EE1456EBA4C5168203DE7FDE23156EECFAD50375B6BFC8CBE9942C797E33D75F191ABC7F4EF9F49A861FD3F4E8ED30F60ED60794D6634930A312D3A0E1F93A7FE5F2C7ADD34757C77650DBF
	C09C19E5CA1A5629FE5F7A83FC7F70608F707DC3759E4F6BE73B778C3EFE5634C7FF7D6C0BBD6E6BE7DF6F51DFBFB36FD12F1F09390F5DFCBEB5F553F0C376A61CAB3A1266D84B89FB18E31D609E36894B64EFF85B96846EB04246F1AC925C71046D485733AF63B99B3B86497CA20E965DB8124FC2BEA3BC63D22C5BBCC9775024EF467AE6B49B6A49EBCD5732232365F7FACC214EBD3707EC26762342FFBABF7BEC00B3300D35BB1B65F89753DDD1C60A7758E2A55E6EA5CA3C4F96F80AD748BA1604327CC11667
	B310F6FE287C5E31D411FF43E9CF72C558CD2ABB971CC3381FA2F78D6A97CEE91D7B31A2F3444A016D7A504A4C016DFA20FED7DD33887B4E2A01E599333C11A5F45C253A4F6145DE5F53DD6E44F4CF23F42FE043A67C9629C1791D297AFD53104EEE752DFE3C67870863F2557ECE753E4FD5BF40DE4F72739A0798135D37995713F336C03FFE8B2E854F5D02631B10AD3896DC3605050D82F79CD0D289160725AB14B2F6218F6773E7E94E4F0FD74919DE2CBEBFDF782F7AD4711F3229DB7AC16E2AB01B1CA63A84
	C66E332B6E91596C43BDE49765613B14C1361B46E5095F2F2CD264DE21F79F392ABEDB3CDA896BDF786D8FE30BF6F16526F2A70E50E51E6A6E110E9C9F4E424BC1FBB413BDDED82358631935AABB678CEC0F196A7BC38AFF20FBCC9C6C2AD057FBCC24750A9E87FD30454DE40BG324514750AAD7E32C1113BF690DB3C1D2304BDD46878E1ECE1152FB6553CAB2F2B6EBC596E03BDAEA7FBCCA4FBBC102B58635EBCC56ECB9B8636C7236AFE4A11BF543BE2E6D732F6B8356FCA3D6A1C59F29F6CF19959A3016C31
	26D231478A555D134FFA34072873AFDC0873F045DB219F3D9577B1F52C247B965A6DDD3956C3DD39470EF7651A8EB9F3ADE62B333434812AEABAB3D3348993392C51A0A39CA8EAAB35892AF3F3AE1B6FFDBA1F23FBFC62366A758387BACFB73EDA314752DAA57E1B53FF20BC6FE5D74EBA6C5ABC4FD01D3770108F3648CC237518D2EAAFF62A5A0BDD0A5C1213837799ADC5AC3FB7D3F8933F7F858A29151DBCE4EF6767C448F38427F8BB15878C2D25F886C5E8DE266439AFE1AB9D2EBA03629FF9EC3E422E1C70
	AE4BFDB78F7B9443CAF16E61C4A93F9744EE36D4B96CA7FAAD7C4E90BCC7FFB73F3351EAB9E1F1B41B8E9D32181927B83B691F796A331B4602BCF63F0BD4B65C108EA4421CAED40749A993E17A6099D006E3403F3B1CD7B39A70F1E55EFC0B3C4C65DC33C860196A955F29A4B55FB460C9A977062FE80D1AAF83F8723D622BF42A79EA002745AB3E8787BD8BDCFF4D6549C1797457DC96EE995A9A446FF81F54D62EDD039847713EF26D9AC465962C7B7C41AF369C4B4125F96A780D5B069B5A36F9E3273BF354FC
	CE609969955F6FDC78C200A74CAB3E2BDC78A6824F942F7802DC786200C75A6E8D5FA5EE5F168FC6026F9A1F79C43E15AC6267FAED5766E7A547B337EB737381EA4BF240AFF0BB66670BAE67C1CE83BCE507B769DD6DF2DEED8670648F42A76A2A4B735833E0D3016C1E1427BE73C6E06979F8978636C14A179A391F579EE82E2F2EB4FE5354E82CEF16F09DF8E9AB6BB070B699DA23DCE80DF221B54A15DB23DCEA0DF215562834D4DB1D4A6D494A0294D95F1A9DB826FB105F3D548F6EA020509D5897B8D23F43
	77F835D5359C6FED05986FC40A370DFFC31DG6E740A61061C8A1C4FFBD367DB757F68FC2BDE20F3F640E61D3383AECFF28FF7D512FB385DBA5F406EDB639E2E45699E6EAB277BBE2DBFEC116877F8556FDDDB4E7F3CC15BF90DC3247906BCF7BB5C1C67EE2F47F3F476356538073BDFA75C03BA61FE23936ED61DF03F56899716631ECF5B972972C675FB7242731FCFAF1D57B8C41A5F5379F6B3B14D5DDE65E96E61AA535C6D35F640F39EEBAABC1DB532031F3F58D2212D576BB8EE2E50566BFFA940FA3D877C
	22AA30DE3FEB4033A1CAAABD459F456B9BE325B67E27B95ED2290D7FB01D71E0863F08CA2C1F3697609AC4CE956AD42B7AEE2CC1757B090273DF06624FEB9CA24D0F96E21ADBA84DCF297660BE2F7AED7B8176630EB82FF10C29E2E74FFED610A93B7D2B5C4BD4300C0930AF95E258F81D30C9558293ED617785B826DBDA0DE320BF943027535EA35F5377FD2A7A7B50F1811BBC5E5357A84B6E2FF67DBED444352088639ACBF1559731FE09D82F236F78DD5E69B888AB558C5C6791FBD5BF2AE769CE073079C04D
	C0B620E1C041C021C093EB68AC51BAB60165E94FAAF5035C762AAEF1B997F4AA481BB608EE7279BB85D81FADC822F3F08A30BE5B1208EB0FD9751E57BFB7548F3E7ED9DC2F3F7ED9DA6F3E7E3923DEFF7D3336DE3D7EA92F3DF2398ECB878C1F555F8FB656FB7EFEF0A31D1374C7CAF3A11D13F490527CA11791B46BAD484050D72464DE0734D6EF2AA73759E16A6E35191CAC2D629ADAC97C99E6C8497F15969D36DAEC6686B01B83C3DA6A25859C457993F6FE34D17A50852BBA56ED36D9D8FA1C52C2A36B8967
	59CC5D9DF9A20AB223146692FC0F65643752E949E014A3C4DC8467DEFDBDDC2D1E9E2774E98DF5C5B3211B1A496F574DE4773CE44A1DE12714F44854FE94D2BD1FE6CA6A799120D30F9715A6BEFFFD628CA929DE7AC0E4E73D14447224DECA66D9D6AFA5C2F64A71297E20249E36BBCE189C66DE26D1323BFFB23F6F5A132F7817546330186151FB2C3D5DA9A502CECEA92552E64BE4388B6B8C19A4B9G63709400A957E09FD086739FD004A70F59AC72634C463DEEC91C21FA9FE7C09CD59B1B58E8C15F84109D
	73DCA72E8CB7FCF8C15AE5C547E6C89F6C14BEB8EC3275F41AA09BF8E4C6E9C6BBC47831A8BD4012574ED872B8E3D30F295DD27625B46B302363908F6E14E6FC3973CB9042DDAC7E15C6E9363313EF0F07D75BA9FD28C8EAE4950F6CD1A6F27AFD786B01BFD2BDC9CD9C4D6461A2A3DD436156F7769AA78F92AE5107F052A0613C556F7F8F773F9C4EA14DF5D9233F6F3ACDAFC47E7D735EC46903FA69234300B4D99C7C26F00B39862A694FAD8E763570CEE9D60F9D4AB03BF4942A2FDE7EC86EF51E3441FB13FE
	780065DB7636DA4C15F8C6A93CC90BC579BF66345A726ACC3D1D3C4E072AB8299B2AE9250EB5D4D6703217AA3F320B38FE0CAB1738965DB5FF75273BE346A5CAB3F6CA33EDF613F927299D1A79FAE9163353E1696D345B4CFDBDBB042014932CE5E2B34509A06EE3CAAECBDCAE5BFECECA9E6D3BB6E0EE6C5C1DD02B74B924BC9E123D22399442DC5DE9F2C0D496C73D131DF9ECF7E4111F5FA74EB58F6D7A30F586EBC75409F202D62C71025A3B8BB44854E800DEA4A877A713722F3C69BF7F650294D62734C229
	C1BAAC762E3C4A43072D5D9692GB58908DFCE62D918DCCD1805FD17AF5E7D57778FC17E56C809DD79797C7F827EFFA17B3F00A3859CA9A884A346CB837EE97D7BF8C12BF70AC2C1ADA0EBFDF32109B4B91FFAE552AF7FD69E9F87295BA9D735E06CB4A5D443312EC396470FC22FDCBEA7E86A45ACAFFAD811C039D0CDF7D9EDA761C78D5FCD524770AF7ABC14789A37924550DD6EC50341FB3867DE7EFF234F89E7DC87B8EFB30B107DB8682AE55B7F89ADBBBCE979BF2E373D4F273A70FFCA77931EF4F7C82BF4
	7B4D2CEBEB2615457F5E5BB62EF352621550E72E176670AEEC936F40E2C73746525B6E307630B5B239236B5A07EE405A8AFB4002FD2755E669D63C66293CBEBBE0F4CA73C40554EDF16601DB74E48CCE29AAB73735CACC6036DA4C47DA0F769EEBED3776364A133A2DB4295B2ACC6A360A254DD6130DEF7BF0CAA5BE2F0A4A2CCC59FECFA5767FE46978517F6112FD29EF3A1F14F8892AB90B9244C788228C157A3E73881919B2D77C8F5A73E7835973FFCA53D17FC75B11B11E7476E6946510963A1F2DD3F96913
	CF04FED313BEC31A81037ECE0B9865A71FE0A77830BA139D3DEA184625053A385CF5GFF498C4698675253533B6D7E7EFF5ED8FBDDC8FBA4743320CDD0ED12603574CC12D6C87616E5CBB103541D12645DA843B59C137EB245251FB2504510B8303FB4D5C7F31750CEE9FE75B10783121B59646CF2E616E6BA25C52AB91ADD8ED3CFA7C76753C40E55524B91756C8817AC7E185019BB871ADBF03A7D9B345A63F23DE816868A176AA15C7F548B7524FB15A5116E7FD31ACED5B4F5A944BED588F293D6FD96CD986E
	85F6CADB073C0F1829720F21F403A4D5B912533273EC1AE3E5AF2FD3AABF1B2D40CC21174FD10A4E2603C13BF1075241A0E6264C3F4ED1EAF61DC5EA547BE91D5236335A0F4BD4FA6BFF204C2976448E254C295819C26F1EED0A3C65BC87A9D76D8C9DCA4AD56C2010A179ACD2AE2FB1065716C8E2FEBEBCE50C14B1F16D887CB6D73B1EB66E9B315E96790D3246A8567FF6733333D25A069B325AF01D6F6A85EC43376EBADF65A1E7D3978C861DA65B514AEE73EE68A5D51DF4DA147B1176ADE472FBB10C6CA97C
	BEDF4874BE226279FFGD0CB878810D8F3DE38D9GGC8D781GD0CB818294G94G88G88G52FB4CAA10D8F3DE38D9GGC8D781G8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG72D9GGGG
**end of data**/
}
/**
 * Return the CheckBoxesPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getCheckBoxesPanel() {
	if (ivjCheckBoxesPanel == null) {
		try {
			ivjCheckBoxesPanel = new javax.swing.JPanel();
			ivjCheckBoxesPanel.setName("CheckBoxesPanel");
			ivjCheckBoxesPanel.setBorder(new javax.swing.border.EtchedBorder());
			ivjCheckBoxesPanel.setLayout(new java.awt.GridBagLayout());
			ivjCheckBoxesPanel.setBackground(new java.awt.Color(204,204,255));
			ivjCheckBoxesPanel.setPreferredSize(new java.awt.Dimension(500, 600));
			ivjCheckBoxesPanel.setMinimumSize(new java.awt.Dimension(500, 600));

			java.awt.GridBagConstraints constraintsHistoryComboBox = new java.awt.GridBagConstraints();
			constraintsHistoryComboBox.gridx = 1; constraintsHistoryComboBox.gridy = 0;
			constraintsHistoryComboBox.gridwidth = 63;
			constraintsHistoryComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsHistoryComboBox.weightx = 1.0;
			constraintsHistoryComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getCheckBoxesPanel().add(getHistoryComboBox(), constraintsHistoryComboBox);

			java.awt.GridBagConstraints constraintsCurrentStmtLabel = new java.awt.GridBagConstraints();
			constraintsCurrentStmtLabel.gridx = 0; constraintsCurrentStmtLabel.gridy = 0;
			constraintsCurrentStmtLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsCurrentStmtLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getCheckBoxesPanel().add(getCurrentStmtLabel(), constraintsCurrentStmtLabel);

			java.awt.GridBagConstraints constraintsValueFlowLabel = new java.awt.GridBagConstraints();
			constraintsValueFlowLabel.gridx = 0; constraintsValueFlowLabel.gridy = 4;
			constraintsValueFlowLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsValueFlowLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getCheckBoxesPanel().add(getValueFlowLabel(), constraintsValueFlowLabel);

			java.awt.GridBagConstraints constraintsValueCheckBoxesPanel = new java.awt.GridBagConstraints();
			constraintsValueCheckBoxesPanel.gridx = 1; constraintsValueCheckBoxesPanel.gridy = 4;
			constraintsValueCheckBoxesPanel.gridwidth = 8;
			constraintsValueCheckBoxesPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsValueCheckBoxesPanel.weightx = 1.0;
			constraintsValueCheckBoxesPanel.insets = new java.awt.Insets(4, 0, 4, 4);
			getCheckBoxesPanel().add(getValueCheckBoxesPanel(), constraintsValueCheckBoxesPanel);

			java.awt.GridBagConstraints constraintsDependLabel = new java.awt.GridBagConstraints();
			constraintsDependLabel.gridx = 0; constraintsDependLabel.gridy = 1;
			constraintsDependLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDependLabel.insets = new java.awt.Insets(12, 4, 4, 4);
			getCheckBoxesPanel().add(getDependLabel(), constraintsDependLabel);

			java.awt.GridBagConstraints constraintsDpredLabel = new java.awt.GridBagConstraints();
			constraintsDpredLabel.gridx = 0; constraintsDpredLabel.gridy = 2;
			constraintsDpredLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDpredLabel.anchor = java.awt.GridBagConstraints.EAST;
			constraintsDpredLabel.insets = new java.awt.Insets(12, 4, 4, 0);
			getCheckBoxesPanel().add(getDpredLabel(), constraintsDpredLabel);

			java.awt.GridBagConstraints constraintsDsuccLabel = new java.awt.GridBagConstraints();
			constraintsDsuccLabel.gridx = 0; constraintsDsuccLabel.gridy = 3;
			constraintsDsuccLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsDsuccLabel.anchor = java.awt.GridBagConstraints.EAST;
			constraintsDsuccLabel.insets = new java.awt.Insets(4, 4, 5, 0);
			getCheckBoxesPanel().add(getDsuccLabel(), constraintsDsuccLabel);

			java.awt.GridBagConstraints constraintsDependCheckBoxesPanel = new java.awt.GridBagConstraints();
			constraintsDependCheckBoxesPanel.gridx = 8; constraintsDependCheckBoxesPanel.gridy = 1;
constraintsDependCheckBoxesPanel.gridheight = 3;
			constraintsDependCheckBoxesPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsDependCheckBoxesPanel.weightx = 1.0;
			constraintsDependCheckBoxesPanel.weighty = 1.0;
			constraintsDependCheckBoxesPanel.insets = new java.awt.Insets(4, 0, 4, 4);
			getCheckBoxesPanel().add(getDependCheckBoxesPanel(), constraintsDependCheckBoxesPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckBoxesPanel;
}
/**
 * Return the JButton5 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClearDependButton() {
	if (ivjClearDependButton == null) {
		try {
			ivjClearDependButton = new javax.swing.JButton();
			ivjClearDependButton.setName("ClearDependButton");
			ivjClearDependButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjClearDependButton.setText("");
			ivjClearDependButton.setBackground(new java.awt.Color(204,204,255));
			ivjClearDependButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/clear.jpg")));
			ivjClearDependButton.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjClearDependButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClearDependButton;
}
/**
 * Return the ClearToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClearToolBarButton() {
	if (ivjClearToolBarButton == null) {
		try {
			ivjClearToolBarButton = new javax.swing.JButton();
			ivjClearToolBarButton.setName("ClearToolBarButton");
			ivjClearToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjClearToolBarButton.setText("clear");
			ivjClearToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjClearToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjClearToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjClearToolBarButton.setIcon(null);
			ivjClearToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjClearToolBarButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			ivjClearToolBarButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClearToolBarButton;
}
/**
 * Return the ClearValuesButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getClearValuesButton() {
	if (ivjClearValuesButton == null) {
		try {
			ivjClearValuesButton = new javax.swing.JButton();
			ivjClearValuesButton.setName("ClearValuesButton");
			ivjClearValuesButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjClearValuesButton.setText("clear");
			ivjClearValuesButton.setBackground(new java.awt.Color(204,204,255));
			ivjClearValuesButton.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjClearValuesButton.setMargin(new java.awt.Insets(2, 14, 2, 14));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjClearValuesButton;
}
/**
 * Return the CodeBrowserPane1 property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.CodeBrowserPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public CodeBrowserPane getCodeBrowserPane() {
	if (ivjCodeBrowserPane == null) {
		try {
			ivjCodeBrowserPane = new edu.ksu.cis.bandera.pdgslicer.dependency.CodeBrowserPane();
			ivjCodeBrowserPane.setName("CodeBrowserPane");
			// user code begin {1}
			ivjCodeBrowserPane.setDependFrame(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCodeBrowserPane;
}
/**
 * Return the JSplitPane1 property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JSplitPane getCodeCritSplitPane() {
	if (ivjCodeCritSplitPane == null) {
		try {
			ivjCodeCritSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
			ivjCodeCritSplitPane.setName("CodeCritSplitPane");
			ivjCodeCritSplitPane.setDividerLocation(151);
			getCodeCritSplitPane().add(getCodeBrowserPane(), "top");
			getCodeCritSplitPane().add(getViewerTabbedPane(), "bottom");
			// user code begin {1}
			getCodeCritSplitPane().setDividerLocation(200);
			
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCodeCritSplitPane;
}
private Set getControlDependAnnotationsFor(Annotation mda, Annotation stmtAnnotation, boolean isPred) {
	Set dependAnnotations = new ArraySet();
	if (stmtAnnotation instanceof MethodDeclarationAnnotation)
		return dependAnnotations;
	if (stmtAnnotation instanceof ConstructorDeclarationAnnotation)
		return dependAnnotations;
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	StmtList stmtList = methodInfo.originalStmtList;
	BuildPDG methodPDG = methodInfo.methodPDG;
	Stmt[] stmtsInAnnotation;
	/*
	if (stmtAnnotation instanceof ControlFlowAnnotation) {
	ControlFlowAnnotation whileAnn = (ControlFlowAnnotation) stmtAnnotation;
	stmtsInAnnotation = new Stmt[1];
	stmtsInAnnotation[0] = whileAnn.getBackpointStmt();
	} else
	stmtsInAnnotation = stmtAnnotation.getStatements();
	*/
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = new Stmt[1];
			Stmt[] testStmts = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
			stmtsInAnnotation[0] = testStmts[0];
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = new Stmt[1];
				Stmt[] testStmts = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
				stmtsInAnnotation[0] = testStmts[0];
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	if (isPred)
		for (int i = 0; i < stmtsInAnnotation.length; i++) {
			BitSet cdSet = methodPDG.controlNodesOf(stmtsInAnnotation[i]);
			if (cdSet == null)
				continue; //should show the entry of one method
			for (int j = 0; j < stmtList.size(); j++) {
				if (cdSet.get(j)) {
					Stmt cdStmt = (Stmt) stmtList.get(j);
					Annotation cdStmtAnnotation = null;
					try {
						cdStmtAnnotation = mda.getContainingAnnotation(cdStmt);
					} catch (AnnotationException ae) {
						System.out.println("there is an AnnotationException! And cdStmtAnnotation may be null");
					}
					if (!cdStmtAnnotation.equals(stmtAnnotation)) {
						if (!cdStmtAnnotation.toString().equals(""))
							dependAnnotations.add(cdStmtAnnotation);
					}
				}
			}
		} else
			for (int i = 0; i < stmtsInAnnotation.length; i++) {
				Set cdSet = methodPDG.controlSuccNodesOf(stmtsInAnnotation[i]);
				if (cdSet == null)
					continue; //should show the entry of one method
				for (Iterator j = cdSet.iterator(); j.hasNext();) {
					Stmt cdStmt = (Stmt) j.next();
					Annotation cdStmtAnnotation = null;
					try {
						cdStmtAnnotation = mda.getContainingAnnotation(cdStmt);
					} catch (AnnotationException ae) {
						System.out.println("there is an AnnotationException! And cdStmtAnnotation may be null");
					}
					if (!cdStmtAnnotation.equals(stmtAnnotation)) {
						if (!cdStmtAnnotation.toString().equals(""))
							dependAnnotations.add(cdStmtAnnotation);
					}
				}
			}
	if (dependAnnotations.size() == 0)
		dependAnnotations.add(mda);
	return dependAnnotations;
}
private DefaultMutableTreeNode getControlDependAnns(boolean isPred) {
	DefaultMutableTreeNode controlRoot = null;
	if (isPred)
		controlRoot = new DefaultMutableTreeNode("Predecessors");
	else
		controlRoot = new DefaultMutableTreeNode("Successors");
	Set controlAnnotationsForOneNode;
	Annotation mda;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		controlAnnotationsForOneNode = getControlDependAnnotationsFor(mda, startTreeNode.currentStmtAnnotation, isPred);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			controlAnnotationsForOneNode = getControlDependAnnotationsFor(mda, (Annotation) currentNode, isPred);
		} else
			return controlRoot;
	if (controlAnnotationsForOneNode.isEmpty())
		return controlRoot;
	Hashtable controlAnnotationsForCurrentLevel = new Hashtable();
	controlAnnotationsForCurrentLevel.put(controlRoot, controlAnnotationsForOneNode);
	Hashtable controlAnnotationsForNextLevel;
	do {
		controlAnnotationsForNextLevel = new Hashtable();
		for (java.util.Iterator controlIt = controlAnnotationsForCurrentLevel.keySet().iterator(); controlIt.hasNext();) {
			DefaultMutableTreeNode currentRootNode = (DefaultMutableTreeNode) controlIt.next();
			Set cdAnnotations = (Set) controlAnnotationsForCurrentLevel.get(currentRootNode);
			//add ddAnnotations to currentRootNode as children of it
			for (Iterator cdIt = cdAnnotations.iterator(); cdIt.hasNext();) {
				Annotation cdAnn = (Annotation) cdIt.next();
				DefaultMutableTreeNode cdNode = new DefaultMutableTreeNode(cdAnn);
				currentRootNode.add(cdNode);
				controlAnnotationsForOneNode = getControlDependAnnotationsFor(mda, cdAnn, isPred);
				if (!controlAnnotationsForOneNode.isEmpty())
					controlAnnotationsForNextLevel.put(cdNode, controlAnnotationsForOneNode);
			}
		}
		controlAnnotationsForCurrentLevel = controlAnnotationsForNextLevel;
	} while (!controlAnnotationsForNextLevel.isEmpty());
	return controlRoot;
}
/**
 * Return the ControlLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getControlLabel() {
	if (ivjControlLabel == null) {
		try {
			ivjControlLabel = new javax.swing.JLabel();
			ivjControlLabel.setName("ControlLabel");
			ivjControlLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/control.jpg")));
			ivjControlLabel.setText("");
			ivjControlLabel.setBackground(new java.awt.Color(204,204,255));
			ivjControlLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjControlLabel;
}
/**
 * Return the ControlPredCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getControlPredCheckBox() {
	if (ivjControlPredCheckBox == null) {
		try {
			ivjControlPredCheckBox = new javax.swing.JCheckBox();
			ivjControlPredCheckBox.setName("ControlPredCheckBox");
			ivjControlPredCheckBox.setText("");
			ivjControlPredCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjControlPredCheckBox;
}
/**
 * Return the ControlSuccCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getControlSuccCheckBox() {
	if (ivjControlSuccCheckBox == null) {
		try {
			ivjControlSuccCheckBox = new javax.swing.JCheckBox();
			ivjControlSuccCheckBox.setName("ControlSuccCheckBox");
			ivjControlSuccCheckBox.setText("");
			ivjControlSuccCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjControlSuccCheckBox;
}
private Configuration getCurrentConfig() {
	Configuration currentConfig = new Configuration();
	currentConfig.viewerState = getViewerTabbedPane().getSelectedIndex();
	currentConfig.queryIndex = queryPanel.getQueryKindComboBox().getSelectedIndex();
	currentConfig.dependValueViewerState = dependencyValueViewer.getViewersCurrentState();
	currentConfig.currentValueTreeRoot = dependencyValueViewer.currentValueTreeRoot;
	currentConfig.currentDependTreeRoot = dependencyValueViewer.currentDependTreeRoot;
	currentConfig.dataPred = doPredDataDepend;
	currentConfig.dataSucc = doSuccDataDepend;
	currentConfig.controlPred = doPredControlDepend;
	currentConfig.controlSucc = doSuccControlDepend;
	currentConfig.divergentPred = doPredDivergentDepend;
	currentConfig.divergentSucc = doSuccDivergentDepend;
	currentConfig.readyPred = doPredReadyDepend;
	currentConfig.readySucc = doSuccReadyDepend;
	currentConfig.synchPred = doPredSynchDepend;
	currentConfig.synchSucc = doSuccSynchDepend;
	currentConfig.interferPred = doPredInterferDepend;
	currentConfig.interferSucc = doSuccInterferDepend;
	currentConfig.valuePred = doPredValue;
	currentConfig.valueSucc = doSuccValue;
	currentConfig.valueSet = doSetValue;
	return currentConfig;
}
/**
 * Return the CurrentStmtLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getCurrentStmtLabel() {
	if (ivjCurrentStmtLabel == null) {
		try {
			ivjCurrentStmtLabel = new javax.swing.JLabel();
			ivjCurrentStmtLabel.setName("CurrentStmtLabel");
			ivjCurrentStmtLabel.setText("Current Stmt : ");
			ivjCurrentStmtLabel.setForeground(java.awt.Color.black);
			ivjCurrentStmtLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCurrentStmtLabel;
}
private Set getDataDependAnnotationsFor(Annotation mda, Annotation stmtAnnotation, boolean isPred) {
	Set dependAnnotations = new ArraySet();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	BuildPDG methodPDG = methodInfo.methodPDG;
	Stmt stmtsInAnnotation[];
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	for (int i = 0; i < stmtsInAnnotation.length; i++) {
		Set ddSet = null;
		if (isPred)
			ddSet = methodPDG.dataNodesOf(stmtsInAnnotation[i]);
		else
			ddSet = methodPDG.dataSuccNodesOf(stmtsInAnnotation[i]);
		if (ddSet == null)
			continue;
		for (Iterator setIt = ddSet.iterator(); setIt.hasNext();) {
			Stmt ddStmt = null;
			if (isPred)
				ddStmt = ((DataBox) setIt.next()).getStmt();
			else
				ddStmt = (Stmt) setIt.next();
			if (ddStmt instanceof IdentityStmt) {
				ParameterNode paraNode = new ParameterNode(methodInfo.sootClass, mda, (IdentityStmt) ddStmt);
				dependAnnotations.add(paraNode);
			} else {
				Annotation ddStmtAnnotation = null;
				try {
					ddStmtAnnotation = mda.getContainingAnnotation(ddStmt);
				} catch (AnnotationException ae) {
					System.out.println("there is an AnnotationException! And ddStmtAnnotation may be null");
				}
				if (!ddStmtAnnotation.toString().equals(""))
					if (!ddStmtAnnotation.equals(stmtAnnotation))
						dependAnnotations.add(ddStmtAnnotation);
			}
		}
	}
	return dependAnnotations;
}
private DefaultMutableTreeNode getDataDependAnns(boolean isPred) {
	DefaultMutableTreeNode dataRoot = null;
	if (isPred)
		dataRoot = new DefaultMutableTreeNode("Predecessors");
	else
		dataRoot = new DefaultMutableTreeNode("Successors");
	Set dataAnnotationsForOneNode;
	Annotation mda;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		dataAnnotationsForOneNode = getDataDependAnnotationsFor(mda, startTreeNode.currentStmtAnnotation, isPred);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			dataAnnotationsForOneNode = getDataDependAnnotationsFor(mda, (Annotation) currentNode, isPred);
		} else
			return dataRoot;
	if (dataAnnotationsForOneNode.isEmpty())
		return dataRoot;
	Hashtable dataAnnotationsForCurrentLevel = new Hashtable();
	dataAnnotationsForCurrentLevel.put(dataRoot, dataAnnotationsForOneNode);
	Hashtable dataAnnotationsForNextLevel;
	do {
		dataAnnotationsForNextLevel = new Hashtable();
		for (java.util.Iterator dataIt = dataAnnotationsForCurrentLevel.keySet().iterator(); dataIt.hasNext();) {
			DefaultMutableTreeNode currentRootNode = (DefaultMutableTreeNode) dataIt.next();
			Set ddAnnotations = (Set) dataAnnotationsForCurrentLevel.get(currentRootNode);
			//add ddAnnotations to currentRootNode as children of it
			for (Iterator ddIt = ddAnnotations.iterator(); ddIt.hasNext();) {
				Object ddObject = ddIt.next();
				if (ddObject instanceof ParameterNode) {
					DefaultMutableTreeNode paraNode = new DefaultMutableTreeNode(ddObject);
					if (ddObject.toString().equals("JJJCTEMP$0"))
						paraNode = new DefaultMutableTreeNode("this");
					currentRootNode.add(paraNode);
				} else {
					Annotation ddAnn = (Annotation) ddObject;
					DefaultMutableTreeNode ddNode = new DefaultMutableTreeNode(ddAnn);
					currentRootNode.add(ddNode);
					if (treePathContains(currentRootNode, ddAnn))
						dataAnnotationsForOneNode = new ArraySet();
					else
						dataAnnotationsForOneNode = getDataDependAnnotationsFor(mda, ddAnn, isPred);
				if (!dataAnnotationsForOneNode.isEmpty())
					dataAnnotationsForNextLevel.put(ddNode, dataAnnotationsForOneNode);
			}
		}
	}
	dataAnnotationsForCurrentLevel = dataAnnotationsForNextLevel;
}
while (!dataAnnotationsForNextLevel.isEmpty());
return dataRoot;
}
/**
 * Return the DataLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDataLabel() {
	if (ivjDataLabel == null) {
		try {
			ivjDataLabel = new javax.swing.JLabel();
			ivjDataLabel.setName("DataLabel");
			ivjDataLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/data.jpg")));
			ivjDataLabel.setText("");
			ivjDataLabel.setBackground(new java.awt.Color(204,204,255));
			ivjDataLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDataLabel;
}
/**
 * Return the DataPredCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDataPredCheckBox() {
	if (ivjDataPredCheckBox == null) {
		try {
			ivjDataPredCheckBox = new javax.swing.JCheckBox();
			ivjDataPredCheckBox.setName("DataPredCheckBox");
			ivjDataPredCheckBox.setText("");
			ivjDataPredCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDataPredCheckBox;
}
/**
 * Return the DataSuccCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDataSuccCheckBox() {
	if (ivjDataSuccCheckBox == null) {
		try {
			ivjDataSuccCheckBox = new javax.swing.JCheckBox();
			ivjDataSuccCheckBox.setName("DataSuccCheckBox");
			ivjDataSuccCheckBox.setText("");
			ivjDataSuccCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDataSuccCheckBox;
}
/**
 * Return the JPanel3 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getDependCheckBoxesPanel() {
	if (ivjDependCheckBoxesPanel == null) {
		try {
			ivjDependCheckBoxesPanel = new javax.swing.JPanel();
			ivjDependCheckBoxesPanel.setName("DependCheckBoxesPanel");
			ivjDependCheckBoxesPanel.setLayout(new java.awt.GridBagLayout());
			ivjDependCheckBoxesPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsDataPredCheckBox = new java.awt.GridBagConstraints();
			constraintsDataPredCheckBox.gridx = 0; constraintsDataPredCheckBox.gridy = 1;
			constraintsDataPredCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getDataPredCheckBox(), constraintsDataPredCheckBox);

			java.awt.GridBagConstraints constraintsDataSuccCheckBox = new java.awt.GridBagConstraints();
			constraintsDataSuccCheckBox.gridx = 0; constraintsDataSuccCheckBox.gridy = 2;
			constraintsDataSuccCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getDataSuccCheckBox(), constraintsDataSuccCheckBox);

			java.awt.GridBagConstraints constraintsDataLabel = new java.awt.GridBagConstraints();
			constraintsDataLabel.gridx = 0; constraintsDataLabel.gridy = 0;
			constraintsDataLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getDataLabel(), constraintsDataLabel);

			java.awt.GridBagConstraints constraintsControlPredCheckBox = new java.awt.GridBagConstraints();
			constraintsControlPredCheckBox.gridx = 1; constraintsControlPredCheckBox.gridy = 1;
			constraintsControlPredCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getControlPredCheckBox(), constraintsControlPredCheckBox);

			java.awt.GridBagConstraints constraintsControlLabel = new java.awt.GridBagConstraints();
			constraintsControlLabel.gridx = 1; constraintsControlLabel.gridy = 0;
			constraintsControlLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getControlLabel(), constraintsControlLabel);

			java.awt.GridBagConstraints constraintsControlSuccCheckBox = new java.awt.GridBagConstraints();
			constraintsControlSuccCheckBox.gridx = 1; constraintsControlSuccCheckBox.gridy = 2;
			constraintsControlSuccCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getControlSuccCheckBox(), constraintsControlSuccCheckBox);

			java.awt.GridBagConstraints constraintsReadyLabel = new java.awt.GridBagConstraints();
			constraintsReadyLabel.gridx = 3; constraintsReadyLabel.gridy = 0;
			constraintsReadyLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getReadyLabel(), constraintsReadyLabel);

			java.awt.GridBagConstraints constraintsReadyPredCheckBox = new java.awt.GridBagConstraints();
			constraintsReadyPredCheckBox.gridx = 3; constraintsReadyPredCheckBox.gridy = 1;
			constraintsReadyPredCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getReadyPredCheckBox(), constraintsReadyPredCheckBox);

			java.awt.GridBagConstraints constraintsReadySuccCheckBox = new java.awt.GridBagConstraints();
			constraintsReadySuccCheckBox.gridx = 3; constraintsReadySuccCheckBox.gridy = 2;
			constraintsReadySuccCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getReadySuccCheckBox(), constraintsReadySuccCheckBox);

			java.awt.GridBagConstraints constraintsInterferLabel = new java.awt.GridBagConstraints();
			constraintsInterferLabel.gridx = 4; constraintsInterferLabel.gridy = 0;
			constraintsInterferLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getInterferLabel(), constraintsInterferLabel);

			java.awt.GridBagConstraints constraintsInterferPredCheckBox = new java.awt.GridBagConstraints();
			constraintsInterferPredCheckBox.gridx = 4; constraintsInterferPredCheckBox.gridy = 1;
			constraintsInterferPredCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getInterferPredCheckBox(), constraintsInterferPredCheckBox);

			java.awt.GridBagConstraints constraintsInterferSuccCheckBox = new java.awt.GridBagConstraints();
			constraintsInterferSuccCheckBox.gridx = 4; constraintsInterferSuccCheckBox.gridy = 2;
			constraintsInterferSuccCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getInterferSuccCheckBox(), constraintsInterferSuccCheckBox);

			java.awt.GridBagConstraints constraintsDiverLabel = new java.awt.GridBagConstraints();
			constraintsDiverLabel.gridx = 2; constraintsDiverLabel.gridy = 0;
			constraintsDiverLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getDiverLabel(), constraintsDiverLabel);

			java.awt.GridBagConstraints constraintsDiverPredCheckBox = new java.awt.GridBagConstraints();
			constraintsDiverPredCheckBox.gridx = 2; constraintsDiverPredCheckBox.gridy = 1;
			constraintsDiverPredCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getDiverPredCheckBox(), constraintsDiverPredCheckBox);

			java.awt.GridBagConstraints constraintsDiverSuccCheckBox = new java.awt.GridBagConstraints();
			constraintsDiverSuccCheckBox.gridx = 2; constraintsDiverSuccCheckBox.gridy = 2;
			constraintsDiverSuccCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getDiverSuccCheckBox(), constraintsDiverSuccCheckBox);

			java.awt.GridBagConstraints constraintsAllpredButton = new java.awt.GridBagConstraints();
			constraintsAllpredButton.gridx = 6; constraintsAllpredButton.gridy = 1;
			constraintsAllpredButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getAllpredButton(), constraintsAllpredButton);

			java.awt.GridBagConstraints constraintsAllsuccButton = new java.awt.GridBagConstraints();
			constraintsAllsuccButton.gridx = 6; constraintsAllsuccButton.gridy = 2;
			constraintsAllsuccButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getAllsuccButton(), constraintsAllsuccButton);

			java.awt.GridBagConstraints constraintsClearDependButton = new java.awt.GridBagConstraints();
			constraintsClearDependButton.gridx = 7; constraintsClearDependButton.gridy = 0;
			constraintsClearDependButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getClearDependButton(), constraintsClearDependButton);

			java.awt.GridBagConstraints constraintsNopredButton = new java.awt.GridBagConstraints();
			constraintsNopredButton.gridx = 7; constraintsNopredButton.gridy = 1;
			constraintsNopredButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getNopredButton(), constraintsNopredButton);

			java.awt.GridBagConstraints constraintsNosuccButton = new java.awt.GridBagConstraints();
			constraintsNosuccButton.gridx = 7; constraintsNosuccButton.gridy = 2;
			constraintsNosuccButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsNosuccButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getNosuccButton(), constraintsNosuccButton);

			java.awt.GridBagConstraints constraintsEmptyPanel = new java.awt.GridBagConstraints();
			constraintsEmptyPanel.gridx = 8; constraintsEmptyPanel.gridy = 0;
			constraintsEmptyPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsEmptyPanel.weightx = 1.0;
			constraintsEmptyPanel.weighty = 1.0;
			constraintsEmptyPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getEmptyPanel(), constraintsEmptyPanel);

			java.awt.GridBagConstraints constraintsAllDependButton = new java.awt.GridBagConstraints();
			constraintsAllDependButton.gridx = 6; constraintsAllDependButton.gridy = 0;
			constraintsAllDependButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getAllDependButton(), constraintsAllDependButton);

			java.awt.GridBagConstraints constraintsSynchLabel = new java.awt.GridBagConstraints();
			constraintsSynchLabel.gridx = 5; constraintsSynchLabel.gridy = 0;
			constraintsSynchLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getSynchLabel(), constraintsSynchLabel);

			java.awt.GridBagConstraints constraintsSynchPredCheckBox = new java.awt.GridBagConstraints();
			constraintsSynchPredCheckBox.gridx = 5; constraintsSynchPredCheckBox.gridy = 1;
			constraintsSynchPredCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getSynchPredCheckBox(), constraintsSynchPredCheckBox);

			java.awt.GridBagConstraints constraintsSynchSuccCheckBox = new java.awt.GridBagConstraints();
			constraintsSynchSuccCheckBox.gridx = 5; constraintsSynchSuccCheckBox.gridy = 2;
			constraintsSynchSuccCheckBox.insets = new java.awt.Insets(4, 4, 4, 4);
			getDependCheckBoxesPanel().add(getSynchSuccCheckBox(), constraintsSynchSuccCheckBox);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependCheckBoxesPanel;
}
/**
 * Return the DependencyFrameContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getDependencyFrameContentPane() {
	if (ivjDependencyFrameContentPane == null) {
		try {
			ivjDependencyFrameContentPane = new javax.swing.JPanel();
			ivjDependencyFrameContentPane.setName("DependencyFrameContentPane");
			ivjDependencyFrameContentPane.setBorder(new javax.swing.border.EtchedBorder());
			ivjDependencyFrameContentPane.setLayout(new java.awt.BorderLayout());
			getDependencyFrameContentPane().add(getOperationPanel(), "North");
			getDependencyFrameContentPane().add(getCodeCritSplitPane(), "Center");
			// user code begin {1}

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependencyFrameContentPane;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-7 10:11:56)
 * @return javax.swing.tree.DefaultMutableTreeNode
 * @param dependKind java.lang.String
 * @param isPred boolean
 */
private DefaultMutableTreeNode getDependencyTreeNode(String dependKind, boolean isPred) {
	int kind = dependencyKindsList.indexOf(dependKind);
	DefaultMutableTreeNode returnNode = null;
	switch (kind) {
		case DATA :
			returnNode = getDataDependAnns(isPred);
			break;
		case CONTROL :
			returnNode = getControlDependAnns(isPred);
			break;
		case DIVERGENT :
			returnNode = getDivergentDependAnns(isPred);
			break;
		case READY :
			returnNode = getReadyDependAnns(isPred);
			break;
		case INTERFERENCE :
			returnNode = getInterferDependAnns(isPred);
			break;
		case SYNCHRONIZATION :
			returnNode = getSynchDependAnns(isPred);
			break;
	}
	return returnNode;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-8 15:05:12)
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.DependencyValueViewer
 */
DependencyValueViewer getDependencyValueViewer() {
	return dependencyValueViewer;
}
/**
 * Return the JLabel3 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDependLabel() {
	if (ivjDependLabel == null) {
		try {
			ivjDependLabel = new javax.swing.JLabel();
			ivjDependLabel.setName("DependLabel");
			ivjDependLabel.setText("Dependencies : ");
			ivjDependLabel.setForeground(java.awt.Color.black);
			ivjDependLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependLabel;
}
/**
 * Return the DependOperationToolBar property value.
 * @return javax.swing.JToolBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JToolBar getDependOperationToolBar() {
	if (ivjDependOperationToolBar == null) {
		try {
			ivjDependOperationToolBar = new javax.swing.JToolBar();
			ivjDependOperationToolBar.setName("DependOperationToolBar");
			ivjDependOperationToolBar.setPreferredSize(new java.awt.Dimension(259, 50));
			ivjDependOperationToolBar.setBackground(new java.awt.Color(204,204,255));
			ivjDependOperationToolBar.setMinimumSize(new java.awt.Dimension(224, 50));
			getDependOperationToolBar().add(getToolBarInPanel(), getToolBarInPanel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependOperationToolBar;
}
private Set getDivergentDependAnnotationsFor(Annotation mda, Annotation stmtAnnotation, boolean isPred) {
	Set dependAnnotations = new ArraySet();
	if (stmtAnnotation instanceof MethodDeclarationAnnotation)
		return dependAnnotations;
	if (stmtAnnotation instanceof ConstructorDeclarationAnnotation)
		return dependAnnotations;
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	StmtList stmtList = methodInfo.originalStmtList;
	BuildPDG methodPDG = methodInfo.methodPDG;
	Stmt[] stmtsInAnnotation;
	/*
	if (stmtAnnotation instanceof ControlFlowAnnotation) {
	ControlFlowAnnotation whileAnn = (ControlFlowAnnotation) stmtAnnotation;
	stmtsInAnnotation = new Stmt[1];
	stmtsInAnnotation[0] = whileAnn.getBackpointStmt();
	} else
	stmtsInAnnotation = stmtAnnotation.getStatements();
	*/
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = new Stmt[1];
			Stmt[] testStmts = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
			stmtsInAnnotation[0] = testStmts[0];
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = new Stmt[1];
				Stmt[] testStmts = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
				stmtsInAnnotation[0] = testStmts[0];
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	if (isPred)
		for (int i = 0; i < stmtsInAnnotation.length; i++) {
			BitSet diverSet = methodPDG.preDivergencePointsOf(stmtsInAnnotation[i]);
			if (diverSet == null)
				continue; //should show the entry of one method
			for (int j = 0; j < stmtList.size(); j++) {
				if (diverSet.get(j)) {
					Stmt diverStmt = (Stmt) stmtList.get(j);
					Annotation diverStmtAnnotation = null;
					try {
						diverStmtAnnotation = mda.getContainingAnnotation(diverStmt);
					} catch (AnnotationException ae) {
						System.out.println("there is an AnnotationException! And cdStmtAnnotation may be null");
					}
					if (!diverStmtAnnotation.equals(stmtAnnotation)) {
						if (!diverStmtAnnotation.toString().equals(""))
							dependAnnotations.add(diverStmtAnnotation);
					}
				}
			}
		} else
			for (int i = 0; i < stmtsInAnnotation.length; i++) {
				BitSet diverSet = methodPDG.succDivergencePointsOf(stmtsInAnnotation[i]);
				if (diverSet == null)
					continue; //should show the entry of one method
				for (int j = 0; j < stmtList.size(); j++) {
					if (diverSet.get(j)) {
						Stmt diverStmt = (Stmt) stmtList.get(j);
						Annotation diverStmtAnnotation = null;
						try {
							diverStmtAnnotation = mda.getContainingAnnotation(diverStmt);
						} catch (AnnotationException ae) {
							System.out.println("there is an AnnotationException! And cdStmtAnnotation may be null");
						}
						if (!diverStmtAnnotation.equals(stmtAnnotation)) {
							if (!diverStmtAnnotation.toString().equals(""))
								dependAnnotations.add(diverStmtAnnotation);
						}
					}
				}
			}
	if (dependAnnotations.size() == 0)
		dependAnnotations.add(mda);
	return dependAnnotations;
}
private DefaultMutableTreeNode getDivergentDependAnns(boolean isPred) {
	DefaultMutableTreeNode divergenceRoot = null;
	if (isPred)
		divergenceRoot = new DefaultMutableTreeNode("Predecessors");
	else
		divergenceRoot = new DefaultMutableTreeNode("Successors");
	Set divergenceAnnotationsForOneNode;
	Annotation mda;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		divergenceAnnotationsForOneNode = getDivergentDependAnnotationsFor(mda, startTreeNode.currentStmtAnnotation, isPred);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			divergenceAnnotationsForOneNode = getDivergentDependAnnotationsFor(mda, (Annotation) currentNode, isPred);
		} else
			return divergenceRoot;
	if (divergenceAnnotationsForOneNode.isEmpty())
		return divergenceRoot;
	Hashtable divergenceAnnotationsForCurrentLevel = new Hashtable();
	divergenceAnnotationsForCurrentLevel.put(divergenceRoot, divergenceAnnotationsForOneNode);
	Hashtable divergenceAnnotationsForNextLevel;
	do {
		divergenceAnnotationsForNextLevel = new Hashtable();
		for (java.util.Iterator divergenceIt = divergenceAnnotationsForCurrentLevel.keySet().iterator(); divergenceIt.hasNext();) {
			DefaultMutableTreeNode currentRootNode = (DefaultMutableTreeNode) divergenceIt.next();
			Set divergenceAnnotations = (Set) divergenceAnnotationsForCurrentLevel.get(currentRootNode);
			//add ddAnnotations to currentRootNode as children of it
			for (Iterator diverIt = divergenceAnnotations.iterator(); diverIt.hasNext();) {
				Annotation diverAnn = (Annotation) diverIt.next();
				DefaultMutableTreeNode diverNode = new DefaultMutableTreeNode(diverAnn);
				currentRootNode.add(diverNode);
				divergenceAnnotationsForOneNode = getDivergentDependAnnotationsFor(mda, diverAnn, isPred);
				if (!divergenceAnnotationsForOneNode.isEmpty())
					divergenceAnnotationsForNextLevel.put(diverNode, divergenceAnnotationsForOneNode);
			}
		}
		divergenceAnnotationsForCurrentLevel = divergenceAnnotationsForNextLevel;
	} while (!divergenceAnnotationsForNextLevel.isEmpty());
	return divergenceRoot;
}
/**
 * Return the DiverLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDiverLabel() {
	if (ivjDiverLabel == null) {
		try {
			ivjDiverLabel = new javax.swing.JLabel();
			ivjDiverLabel.setName("DiverLabel");
			ivjDiverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/divergt.jpg")));
			ivjDiverLabel.setText("");
			ivjDiverLabel.setBackground(new java.awt.Color(204,204,255));
			ivjDiverLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDiverLabel;
}
/**
 * Return the DiverPredCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDiverPredCheckBox() {
	if (ivjDiverPredCheckBox == null) {
		try {
			ivjDiverPredCheckBox = new javax.swing.JCheckBox();
			ivjDiverPredCheckBox.setName("DiverPredCheckBox");
			ivjDiverPredCheckBox.setText("");
			ivjDiverPredCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDiverPredCheckBox;
}
/**
 * Return the DiverSuccCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getDiverSuccCheckBox() {
	if (ivjDiverSuccCheckBox == null) {
		try {
			ivjDiverSuccCheckBox = new javax.swing.JCheckBox();
			ivjDiverSuccCheckBox.setName("DiverSuccCheckBox");
			ivjDiverSuccCheckBox.setText("");
			ivjDiverSuccCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDiverSuccCheckBox;
}
/**
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDpredLabel() {
	if (ivjDpredLabel == null) {
		try {
			ivjDpredLabel = new javax.swing.JLabel();
			ivjDpredLabel.setName("DpredLabel");
			ivjDpredLabel.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjDpredLabel.setText("pred");
			ivjDpredLabel.setForeground(java.awt.Color.black);
			ivjDpredLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDpredLabel;
}
/**
 * Return the JLabel2 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDsuccLabel() {
	if (ivjDsuccLabel == null) {
		try {
			ivjDsuccLabel = new javax.swing.JLabel();
			ivjDsuccLabel.setName("DsuccLabel");
			ivjDsuccLabel.setText("succ");
			ivjDsuccLabel.setForeground(java.awt.Color.black);
			ivjDsuccLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
			ivjDsuccLabel.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjDsuccLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDsuccLabel;
}
/**
 * Return the JPanel5 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getEmptyPanel() {
	if (ivjEmptyPanel == null) {
		try {
			ivjEmptyPanel = new javax.swing.JPanel();
			ivjEmptyPanel.setName("EmptyPanel");
			ivjEmptyPanel.setLayout(null);
			ivjEmptyPanel.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjEmptyPanel;
}
/**
 * 
 * @return java.util.HashSet
 * @param mda edu.ksu.cis.bandera.annotation.MethodDeclarationAnnotation
 * @param monitorPairList ca.mcgill.sable.util.List
 */
private Hashtable getExitMonitorAnnotations(Annotation mda, List monitorPairList) {
	Hashtable anns = new Hashtable();
	for (Iterator monitorIt = monitorPairList.iterator(); monitorIt.hasNext();) {
		MonitorPair mp = (MonitorPair) monitorIt.next();
		Annotation bodyAnnotation = mp.getSynchroBodyAnn();
		Vector bodyAnns = bodyAnnotation.getAllAnnotations(true);
		int indexOfLast = bodyAnns.lastIndexOf(bodyAnns.lastElement());
		Annotation exitMonitorAnn = (Annotation) bodyAnns.get(indexOfLast - 1);
		//Stmt endSynchroStmt = mp.getEndSynchroStmt(); this will get a throw statement
		if (exitMonitorAnn != null)
			anns.put(exitMonitorAnn, mp);
	}
	return anns;
}
/**
 * Return the ForwardSliceToolBarButton2 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getForwardSliceToolBarButton() {
	if (ivjForwardSliceToolBarButton == null) {
		try {
			ivjForwardSliceToolBarButton = new javax.swing.JButton();
			ivjForwardSliceToolBarButton.setName("ForwardSliceToolBarButton");
			ivjForwardSliceToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjForwardSliceToolBarButton.setText("F-Slice");
			ivjForwardSliceToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjForwardSliceToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjForwardSliceToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjForwardSliceToolBarButton.setIcon(null);
			ivjForwardSliceToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjForwardSliceToolBarButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForwardSliceToolBarButton;
}
/**
 * Return the ForwardToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getForwardToolBarButton() {
	if (ivjForwardToolBarButton == null) {
		try {
			ivjForwardToolBarButton = new javax.swing.JButton();
			ivjForwardToolBarButton.setName("ForwardToolBarButton");
			ivjForwardToolBarButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjForwardToolBarButton.setText("forward");
			ivjForwardToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjForwardToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjForwardToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjForwardToolBarButton.setIcon(null);
			ivjForwardToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjForwardToolBarButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForwardToolBarButton;
}
/**
 * Return the JComboBox1 property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JComboBox getHistoryComboBox() {
	if (ivjHistoryComboBox == null) {
		try {
			ivjHistoryComboBox = new javax.swing.JComboBox();
			ivjHistoryComboBox.setName("HistoryComboBox");
			ivjHistoryComboBox.setBackground(java.awt.Color.white);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjHistoryComboBox;
}
private Set getInterClassEnterMonitors(SootMethod currentMd, Set exitMonitors) {
	Set enterStmtTreeNodeSet = new ArraySet();
	for (Iterator sootIt = Slicer.sootMethodInfoMap.keySet().iterator(); sootIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) sootIt.next();
		if (sootMethod.equals(currentMd))
			continue;
		MethodInfo methodInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		Set enterStmtSet = enterSetEffectedBy(methodInfo, exitMonitors);
		Annotation mda = null;
		if (!enterStmtSet.isEmpty()) {
			try {
				mda = (Annotation) CompilationManager.getAnnotationManager().getAnnotation(methodInfo.sootClass, methodInfo.sootMethod);
			} catch (Exception ex) {
				System.out.println("There is an exception in geting Annotation");
			}
		}
		for (Iterator enterIt = enterStmtSet.iterator(); enterIt.hasNext();) {
			Stmt enterStmt = (Stmt) enterIt.next();
			Annotation enterAnn = null;
			try {
				enterAnn = mda.getContainingAnnotation(enterStmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And enterMonitorStmtAnnotation may be null");
			}
			if (enterAnn != null)
				enterStmtTreeNodeSet.add(new StmtTreeNode(methodInfo.sootClass, mda, enterAnn));
		}
	}
	return enterStmtTreeNodeSet;
}
/**
 * Insert the method's description here.
 * Creation date: (00-6-14 21:15:04)
 * @return java.util.HashSet
 * @param currentMd ca.mcgill.sable.soot.SootMethod
 * @param notifySynStmt edu.ksu.cis.bandera.pdgslicer.SynchroStmt
 */
private Set getInterClassWaits(SootMethod currentMd, SynchroStmt notifySynStmt) {
	Set waitStmtTreeNodeSet = new ArraySet();
	for (Iterator sootIt = Slicer.sootMethodInfoMap.keySet().iterator(); sootIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) sootIt.next();
		if (sootMethod.equals(currentMd))
			continue;
		MethodInfo methodInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		Set waitStmtSet = waitSetEffectedBy(methodInfo, notifySynStmt);
		Annotation mda = null;
		if (!waitStmtSet.isEmpty()) {
			try {
				mda = (Annotation) CompilationManager.getAnnotationManager().getAnnotation(methodInfo.sootClass, methodInfo.sootMethod);
			} catch (Exception ex) {
				System.out.println("There is an exception in geting Annotation");
			}
		}
		for (Iterator waitIt = waitStmtSet.iterator(); waitIt.hasNext();) {
			Stmt waitStmt = (Stmt) waitIt.next();
			Annotation waitAnn = null;
			try {
				waitAnn = mda.getContainingAnnotation(waitStmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And enterMonitorStmtAnnotation may be null");
			}
			if (waitAnn != null)
				waitStmtTreeNodeSet.add(new StmtTreeNode(methodInfo.sootClass, mda, waitAnn));
		}
	}
	return waitStmtTreeNodeSet;
}
private DefaultMutableTreeNode getInterferDependAnns(boolean isPred) {
	DefaultMutableTreeNode interferRoot = null;
	if (isPred)
		interferRoot = new DefaultMutableTreeNode("Predecessors");
	else
		interferRoot = new DefaultMutableTreeNode("Successors");
	Set interferNodesForOneNode = new ArraySet();
	Annotation mda;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		if (isPred)
			interferNodesForOneNode = getInterferPredNodesFor(mda, startTreeNode.currentStmtAnnotation);
		else
			 {
				SootMethod sm = null;
				if (mda instanceof MethodDeclarationAnnotation)
					sm = ((MethodDeclarationAnnotation) mda).getSootMethod();
				else
					if (mda instanceof ConstructorDeclarationAnnotation)
						sm = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
				interferNodesForOneNode = getInterferSuccNodesFor(sm, startTreeNode.currentStmtAnnotation);
			}
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			if (isPred)
				interferNodesForOneNode = getInterferPredNodesFor(mda, (Annotation) currentNode);
			else
			{
					SootMethod sm = null;
					if (mda instanceof MethodDeclarationAnnotation)
						sm = ((MethodDeclarationAnnotation) mda).getSootMethod();
					else
						if (mda instanceof ConstructorDeclarationAnnotation)
							sm = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
					interferNodesForOneNode = getInterferSuccNodesFor(sm, (Annotation) currentNode);
				}
		} else
			return interferRoot;
	if (interferNodesForOneNode.isEmpty())
		return interferRoot;
	Hashtable interferNodesForCurrentLevel = new Hashtable();
	interferNodesForCurrentLevel.put(interferRoot, interferNodesForOneNode);
	Hashtable interferNodesForNextLevel;
	do {
		interferNodesForNextLevel = new Hashtable();
		for (java.util.Iterator interferIt = interferNodesForCurrentLevel.keySet().iterator(); interferIt.hasNext();) {
			DefaultMutableTreeNode currentRootNode = (DefaultMutableTreeNode) interferIt.next();
			Set interferNodes = (Set) interferNodesForCurrentLevel.get(currentRootNode);
			//add ddAnnotations to currentRootNode as children of it
			for (Iterator ifrIt = interferNodes.iterator(); ifrIt.hasNext();) {
				StmtTreeNode interferNode = (StmtTreeNode) ifrIt.next();
				if (!treePathContains(currentRootNode, interferNode)) {
					DefaultMutableTreeNode interferTreeNode = new DefaultMutableTreeNode(interferNode);
					currentRootNode.add(interferTreeNode);
					if (isPred)
						interferNodesForOneNode = getInterferPredNodesFor(mda, interferNode.currentStmtAnnotation);
					else
						 {
							SootMethod sm = null;
							if (mda instanceof MethodDeclarationAnnotation)
								sm = ((MethodDeclarationAnnotation) mda).getSootMethod();
							else
								if (mda instanceof ConstructorDeclarationAnnotation)
									sm = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
							interferNodesForOneNode = getInterferSuccNodesFor(sm, interferNode.currentStmtAnnotation);
						}
					if (!interferNodesForOneNode.isEmpty())
						interferNodesForNextLevel.put(interferTreeNode, interferNodesForOneNode);
				}
			}
		}
		interferNodesForCurrentLevel = interferNodesForNextLevel;
	} while (!interferNodesForNextLevel.isEmpty());
	return interferRoot;
}
/**
 * Return the InterferLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getInterferLabel() {
	if (ivjInterferLabel == null) {
		try {
			ivjInterferLabel = new javax.swing.JLabel();
			ivjInterferLabel.setName("InterferLabel");
			ivjInterferLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/interfer.jpg")));
			ivjInterferLabel.setText("");
			ivjInterferLabel.setBackground(new java.awt.Color(204,204,255));
			ivjInterferLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInterferLabel;
}
/**
 * Return the InterferPredCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getInterferPredCheckBox() {
	if (ivjInterferPredCheckBox == null) {
		try {
			ivjInterferPredCheckBox = new javax.swing.JCheckBox();
			ivjInterferPredCheckBox.setName("InterferPredCheckBox");
			ivjInterferPredCheckBox.setText("");
			ivjInterferPredCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInterferPredCheckBox;
}
private Set getInterferPredNodesFor(Annotation mda, Annotation stmtAnnotation) {
	Set dependAnnotations = new ArraySet();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	BuildPDG methodPDG = methodInfo.methodPDG;
	Map interferDependMap = methodPDG.getInterferenceMap();
	if (interferDependMap == null)
		return dependAnnotations;
	Stmt stmtsInAnnotation[];
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	for (int i = 0; i < stmtsInAnnotation.length; i++) {
		if (!interferDependMap.containsKey(stmtsInAnnotation[i]))
			continue;
		List interferDependStmt = (List) interferDependMap.get(stmtsInAnnotation[i]);
		for (Iterator interferIt = interferDependStmt.iterator(); interferIt.hasNext();) {
			InterferStmt interferStmt = (InterferStmt) interferIt.next();
			MethodInfo interferStmtMdInfo = interferStmt.methodInfo;
			Stmt interStmt = interferStmt.interferStmt;
			Annotation interStmtAnnotation = null;
			/*
			MethodDeclarationAnnotation interferMdDec = null;
			try {
			interferMdDec = (MethodDeclarationAnnotation) Slicer.annManagerForSlicer.getAnnotation(interferStmtMdInfo.sootClass, interferStmtMdInfo.sootMethod);
			} catch (Exception ex) {
			// in this case sootMethod must be init method in Jimple, and there is no constructor in the source program
			// and this interfer def stmt must be an assignment in the field declaration
			Value definedValue = null;
			Set interferVars = interferStmt.interferVars;
			for (Iterator valueIt = interferVars.iterator(); valueIt.hasNext();) {
			//suppose there is only on defined value;
			definedValue = (Value) valueIt.next();
			}
			if (definedValue instanceof InstanceFieldRef) {
			InstanceFieldRef insFdRef = (InstanceFieldRef) definedValue;
			SootField sootField = insFdRef.getField();
			interStmtAnnotation = Slicer.annManagerForSlicer.getAnnotation(interferStmtMdInfo.sootClass, sootField);
			}
			}
			if (interStmtAnnotation == null) {
			*/
			try {
				interStmtAnnotation = Slicer.annManagerForSlicer.getContainingAnnotation(interferStmtMdInfo.sootClass, interferStmtMdInfo.sootMethod, interStmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And interReadyStmtAnnotation may be null");
			}
			//}
			if (!interStmtAnnotation.equals(stmtAnnotation)) {
				if (!interStmtAnnotation.toString().equals("")) {
					Annotation interferMdDec = Slicer.annManagerForSlicer.getAnnotation(interferStmtMdInfo.sootClass, interferStmtMdInfo.sootMethod);
					dependAnnotations.add(new StmtTreeNode(interferStmtMdInfo.sootClass, interferMdDec, interStmtAnnotation));
				}
			}
		}
	}
	return dependAnnotations;
}
/**
 * Return the InterferSuccCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getInterferSuccCheckBox() {
	if (ivjInterferSuccCheckBox == null) {
		try {
			ivjInterferSuccCheckBox = new javax.swing.JCheckBox();
			ivjInterferSuccCheckBox.setName("InterferSuccCheckBox");
			ivjInterferSuccCheckBox.setText("");
			ivjInterferSuccCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjInterferSuccCheckBox;
}
private Set getInterferSuccNodesFor(SootMethod currentMd, Annotation stmtAnnotation) {
	Set dependAnnotations = new ArraySet();
	if ((stmtAnnotation instanceof SynchronizedStmtAnnotation) || (stmtAnnotation instanceof ConditionalAnnotation) || (stmtAnnotation instanceof ControlFlowAnnotation))
		return dependAnnotations;
	Map sootMap = Slicer.sootMethodInfoMap;
	Stmt stmtsInAnnotation[] = stmtAnnotation.getStatements();
	Set stmtSetInAnnotation = new ArraySet();
	for (int i = 0; i < stmtsInAnnotation.length; i++)
		stmtSetInAnnotation.add(stmtsInAnnotation[i]);
	for (Iterator sootIt = sootMap.keySet().iterator(); sootIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) sootIt.next();
		if (sootMethod.equals(currentMd))
			continue;
		MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
		BuildPDG methodPDG = methodInfo.methodPDG;
		Map interferDependMap = methodPDG.getInterferenceMap();
		if (interferDependMap == null)
			continue;
		Set stmtSet = stmtSetEffectedBy(interferDependMap, stmtSetInAnnotation);
		Annotation mda = null;
		if (!stmtSet.isEmpty()) {
			try {
				mda = (Annotation) CompilationManager.getAnnotationManager().getAnnotation(methodInfo.sootClass, methodInfo.sootMethod);
			} catch (Exception ex) {
			}
		}
		for (Iterator stmtIt = stmtSet.iterator(); stmtIt.hasNext();) {
			Stmt stmt = (Stmt) stmtIt.next();
			Annotation interStmtAnnotation = null;
			try {
				interStmtAnnotation = mda.getContainingAnnotation(stmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And interReadyStmtAnnotation may be null");
			}
			if (!interStmtAnnotation.equals(stmtAnnotation))
				if (!interStmtAnnotation.toString().equals("")) {
					dependAnnotations.add(new StmtTreeNode(methodInfo.sootClass, mda, interStmtAnnotation));
				}
		}
	}
	return dependAnnotations;
}
private Set getInterReadyNodesFor(Annotation mda, Annotation stmtAnnotation) {
	Set dependAnnotations = new ArraySet();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	BuildPDG methodPDG = methodInfo.methodPDG;
	Map readyDependMap = methodPDG.getReadyDependMap();
	if (readyDependMap == null)
		return dependAnnotations;
	Stmt[] stmtsInAnnotation;
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	for (int i = 0; i < stmtsInAnnotation.length; i++) {
		if (!readyDependMap.containsKey(stmtsInAnnotation[i]))
			continue;
		List readyDependStmt = (List) readyDependMap.get(stmtsInAnnotation[i]);
		for (Iterator readyIt = readyDependStmt.iterator(); readyIt.hasNext();) {
			ReadyDependStmt readyStmt = (ReadyDependStmt) readyIt.next();
			MethodInfo readyStmtMdInfo = readyStmt.methodInfo;
			Stmt interReadyStmt = readyStmt.readyOnStmt;
			Annotation readyStmtAnnotation = null;
			//MethodDeclarationAnnotation readyMdDec = (MethodDeclarationAnnotation) Slicer.annManagerForSlicer.getAnnotation(readyStmtMdInfo.sootClass, readyStmtMdInfo.sootMethod);
			if (interReadyStmt instanceof ExitMonitorStmt) {
				//reassign readyStmtAnnotation

				LockAnalysis lockAnalysisForReadyStmt = readyStmtMdInfo.methodPDG.getLockAnalysis();
				MonitorPair mp = lockAnalysisForReadyStmt.getMonitorPair(interReadyStmt);
				Annotation bodyAnnotation = mp.getSynchroBodyAnn();
				Vector bodyAnns = bodyAnnotation.getAllAnnotations(true);
				int indexOfLast = bodyAnns.lastIndexOf(bodyAnns.lastElement());
				readyStmtAnnotation = (Annotation) bodyAnns.get(indexOfLast - 1);
			} else {
				try {
					readyStmtAnnotation = Slicer.annManagerForSlicer.getContainingAnnotation(readyStmtMdInfo.sootClass, readyStmtMdInfo.sootMethod,interReadyStmt);
				} catch (AnnotationException ae) {
					System.out.println("there is an AnnotationException! And interReadyStmtAnnotation may be null");
				}
			}
			if (!readyStmtAnnotation.toString().equals("")) {
				Annotation readyMdDec = Slicer.annManagerForSlicer.getAnnotation(readyStmtMdInfo.sootClass, readyStmtMdInfo.sootMethod);
				dependAnnotations.add(new StmtTreeNode(readyStmtMdInfo.sootClass, readyMdDec, readyStmtAnnotation));
			}
		}
	}
	return dependAnnotations;
}
/**
 * Return the JButton3 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getNopredButton() {
	if (ivjNopredButton == null) {
		try {
			ivjNopredButton = new javax.swing.JButton();
			ivjNopredButton.setName("NopredButton");
			ivjNopredButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjNopredButton.setText("No pred");
			ivjNopredButton.setBackground(new java.awt.Color(204,204,255));
			ivjNopredButton.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjNopredButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNopredButton;
}
/**
 * Return the JButton4 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getNosuccButton() {
	if (ivjNosuccButton == null) {
		try {
			ivjNosuccButton = new javax.swing.JButton();
			ivjNosuccButton.setName("NosuccButton");
			ivjNosuccButton.setBorder(new javax.swing.border.CompoundBorder());
			ivjNosuccButton.setText("No succ");
			ivjNosuccButton.setBackground(new java.awt.Color(204,204,255));
			ivjNosuccButton.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjNosuccButton.setMargin(new java.awt.Insets(0, 2, 0, 2));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNosuccButton;
}
/**
 * 
 * @return ca.mcgill.sable.util.Set
 * @param notifyStmtList ca.mcgill.sable.util.List
 */
 private Hashtable getNotifyWaitStmtAnnotations(Annotation mda, List nwStmtList) {
	Hashtable anns = new Hashtable();
	for (Iterator nwStmtIt = nwStmtList.iterator(); nwStmtIt.hasNext();) {
		SynchroStmt nwSynStmt = (SynchroStmt) nwStmtIt.next();
		Stmt nwJimpleStmt = nwSynStmt.getWaitNotify();
		Annotation nwAnn = null;
		try {
			nwAnn = mda.getContainingAnnotation(nwJimpleStmt);
		} catch (AnnotationException ae) {
			System.out.println("there is an AnnotationException! And notifyStmtAnnotation may be null");
		}
		if (nwAnn != null)
			anns.put(nwAnn, nwSynStmt);
	}
	return anns;
}
/**
 * Return the OperationPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getOperationPanel() {
	if (ivjOperationPanel == null) {
		try {
			ivjOperationPanel = new javax.swing.JPanel();
			ivjOperationPanel.setName("OperationPanel");
			ivjOperationPanel.setPreferredSize(new java.awt.Dimension(0, 235));
			ivjOperationPanel.setLayout(new java.awt.BorderLayout());
			ivjOperationPanel.setBackground(new java.awt.Color(204,204,255));
			getOperationPanel().add(getToolBarPanel(), "North");
			getOperationPanel().add(getCheckBoxesPanel(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOperationPanel;
}
private Set getReadyDependAnnotationsFor(Annotation mda, Annotation stmtAnnotation) {
	Set dependAnnotations = new ArraySet();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	StmtList stmtList = methodInfo.originalStmtList;
	BuildPDG methodPDG = methodInfo.methodPDG;
	LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
	if (lockAnalysis == null)
		return dependAnnotations;
	Stmt stmtsInAnnotation[];
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	//List waitStmtList = lockAnalysis.getWaitStmtList();
	//List notifyStmtList = lockAnalysis.getNotifyStmtList();
	for (int i = 0; i < stmtsInAnnotation.length; i++) {
		BitSet waitSet = lockAnalysis.readyDependOnWaits(stmtsInAnnotation[i]);
		Set waitStmtSet = SetUtil.bitSetToStmtSet(waitSet, stmtList);
		//Hashtable waitNotifyPairs = SlicingMethod.waitNotifyPairsWithinMd(waitSet, waitStmtList, notifyStmtList, stmtList);
		for (Iterator waitIt = waitStmtSet.iterator(); waitIt.hasNext();) {
			Stmt waitStmt = (Stmt) waitIt.next();
			Annotation waitAnn = null;
			try {
				waitAnn = mda.getContainingAnnotation(waitStmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And enterMonitorStmtAnnotation may be null");
			}
			if (waitAnn != null)
				dependAnnotations.add(waitAnn);
		}
	}
	return dependAnnotations;
}
private DefaultMutableTreeNode getReadyDependAnns(boolean isPred) {
	if (!isPred)
		return getReadySuccDependAnns();
	DefaultMutableTreeNode readyRoot = new DefaultMutableTreeNode("Predecessors");
	Set readyAnnotationsForOneNode;
	Annotation mda;
	Annotation currentAnn;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		currentAnn = startTreeNode.currentStmtAnnotation;
		readyAnnotationsForOneNode = getReadyDependAnnotationsFor(mda, currentAnn);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			currentAnn = (Annotation) currentNode;
			readyAnnotationsForOneNode = getReadyDependAnnotationsFor(mda, currentAnn);
		} else
			return readyRoot;
	Hashtable waitAnnNode = new Hashtable();
	for (Iterator readyIt = readyAnnotationsForOneNode.iterator(); readyIt.hasNext();) {
		Annotation waitAnn = (Annotation) readyIt.next();
		DefaultMutableTreeNode waitNode = new DefaultMutableTreeNode(waitAnn);
		readyRoot.add(waitNode);
		waitAnnNode.put(waitAnn, waitNode);
		//add all notify annotations under the wait annotation
		/*
		Set notifyAnns = (Set) readyAnnotationsForOneNode.get(waitAnn);
		for (Iterator notifyIt = notifyAnns.iterator(); notifyIt.hasNext();) {
			Annotation notifyAnn = (Annotation) notifyIt.next();
			DefaultMutableTreeNode notifyNode = new DefaultMutableTreeNode(notifyAnn);
			waitNode.add(notifyNode);
		}
		*/
	}


	//ready interclass

	Hashtable readyNodesForCurrentLevel = new Hashtable();
	if (waitAnnNode.isEmpty()) {
		//currentAnn may be wait stmt or entermonitor
		Set interReadyNodesForOneNode = getInterReadyNodesFor(mda, currentAnn);
		if (interReadyNodesForOneNode.isEmpty()) {
			//then currentAnn is not a wait stmt, neither entermonitor
			//if (readyAnnotationsForOneNode.isEmpty())
			return readyRoot;
		} else {
			// then currentAnn is a waitStmt or entermonitor

			readyNodesForCurrentLevel.put(readyRoot, interReadyNodesForOneNode);
		}
	} else {
		for (java.util.Iterator waitIt = waitAnnNode.keySet().iterator(); waitIt.hasNext();) {
			Annotation waitAnn = (Annotation) waitIt.next();
			Set interReadyNodesForOneNode = getInterReadyNodesFor(mda, waitAnn);
			readyNodesForCurrentLevel.put(waitAnnNode.get(waitAnn), interReadyNodesForOneNode);
		}
	}
	if (currentAnn instanceof SynchronizedStmtAnnotation) {
		Set interReadyNodesForRoot = getInterReadyNodesFor(mda, currentAnn);
		if (!interReadyNodesForRoot.isEmpty())
			readyNodesForCurrentLevel.put(readyRoot, interReadyNodesForRoot);
	}
	//end of ready interClass for wait

	Hashtable readyNodesForNextLevel;
	do {
		readyNodesForNextLevel = new Hashtable();
		for (java.util.Iterator readyIt = readyNodesForCurrentLevel.keySet().iterator(); readyIt.hasNext();) {
			DefaultMutableTreeNode currentRootNode = (DefaultMutableTreeNode) readyIt.next();
			Set readyNodes = (Set) readyNodesForCurrentLevel.get(currentRootNode);
			//add ddAnnotations to currentRootNode as children of it
			for (Iterator rdIt = readyNodes.iterator(); rdIt.hasNext();) {
				StmtTreeNode rdNode = (StmtTreeNode) rdIt.next();
				if (!treePathContains(currentRootNode, rdNode)) {
					DefaultMutableTreeNode rdTreeNode = new DefaultMutableTreeNode(rdNode);
					currentRootNode.add(rdTreeNode);
					Set interReadyNodesForOneNode = getInterReadyNodesFor(rdNode.currentMethodDeclarationAnnotation, rdNode.currentStmtAnnotation);
					if (!interReadyNodesForOneNode.isEmpty())
						readyNodesForNextLevel.put(rdTreeNode, interReadyNodesForOneNode);
				}
			}
		}
		readyNodesForCurrentLevel = readyNodesForNextLevel;
	} while (!readyNodesForNextLevel.isEmpty());
	return readyRoot;
}
/**
 * Return the ReadyLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getReadyLabel() {
	if (ivjReadyLabel == null) {
		try {
			ivjReadyLabel = new javax.swing.JLabel();
			ivjReadyLabel.setName("ReadyLabel");
			ivjReadyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/ready.jpg")));
			ivjReadyLabel.setText("");
			ivjReadyLabel.setBackground(new java.awt.Color(204,204,255));
			ivjReadyLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjReadyLabel;
}
/**
 * Return the ReadyPredCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getReadyPredCheckBox() {
	if (ivjReadyPredCheckBox == null) {
		try {
			ivjReadyPredCheckBox = new javax.swing.JCheckBox();
			ivjReadyPredCheckBox.setName("ReadyPredCheckBox");
			ivjReadyPredCheckBox.setText("");
			ivjReadyPredCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjReadyPredCheckBox;
}
/**
 * Return the ReadySuccCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getReadySuccCheckBox() {
	if (ivjReadySuccCheckBox == null) {
		try {
			ivjReadySuccCheckBox = new javax.swing.JCheckBox();
			ivjReadySuccCheckBox.setName("ReadySuccCheckBox");
			ivjReadySuccCheckBox.setText("");
			ivjReadySuccCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjReadySuccCheckBox;
}
private Set getReadySuccDependAnnotationsFor(Annotation mda, Annotation stmtAnnotation) {
	Set dependAnnotations = new ArraySet();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	StmtList stmtList = methodInfo.originalStmtList;
	BuildPDG methodPDG = methodInfo.methodPDG;
	LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
	if (lockAnalysis == null)
		return dependAnnotations;

	//
	//Stmt stmtsInAnnotation[] = stmtAnnotation.getStatements();

	// if stmtAnnotation is notify statement

	List notifyStmtList = lockAnalysis.getNotifyStmtList();
	List waitStmtList = lockAnalysis.getWaitStmtList();
	/*
	Hashtable notifyStmtAnnotations = getNotifyWaitStmtAnnotations(mda, notifyStmtList);
	if (notifyStmtAnnotations.containsKey(stmtAnnotation)) {
		//search for corresponding wait() within the method
		SynchroStmt notifySynStmt = (SynchroStmt) notifyStmtAnnotations.get(stmtAnnotation);
		Set waitSet = getCorrespondWait(mda, waitStmtList, notifySynStmt);
		dependAnnotations.addAll(waitSet);
		//serch for wait() intermethod
		Set interWaitSet = getInterClassWaits(sootMethod, notifySynStmt);
		dependAnnotations.addAll(interWaitSet);
		return dependAnnotations;
	}
	*/
	// if stmtAnnotation is wait statement
	Hashtable waitStmtAnnotations = getNotifyWaitStmtAnnotations(mda, waitStmtList);
	if (waitStmtAnnotations.containsKey(stmtAnnotation)) {
		//add all reachable stmts from the wait to dependAnnotations
		SynchroStmt waitStmt = (SynchroStmt) waitStmtAnnotations.get(stmtAnnotation);
		Stmt waitJimpleStmt = waitStmt.getWaitNotify();
		BitSet reachableStmts = lockAnalysis.reachableStmtFrom(waitJimpleStmt);
		for (int j = 0; j < stmtList.size(); j++) {
			if (reachableStmts.get(j)) {
				Stmt stmt = (Stmt) stmtList.get(j);
				Annotation reachableStmtAnnotation = null;
				try {
					reachableStmtAnnotation = mda.getContainingAnnotation(stmt);
				} catch (AnnotationException ae) {
					System.out.println("there is an AnnotationException! And reachable StmtAnnotation may be null");
				}
				if (!reachableStmtAnnotation.equals(stmtAnnotation)) {
					if (!reachableStmtAnnotation.toString().equals(""))
						dependAnnotations.add(reachableStmtAnnotation);
				}
			}
		}
		return dependAnnotations;
	}
	// if stmtAnnotation is exitmonitor
	List monitorPairList = lockAnalysis.getLockPairList();
	Hashtable exitMonitorAnnotations = getExitMonitorAnnotations(mda, monitorPairList);
	if (exitMonitorAnnotations.containsKey(stmtAnnotation)) {
		//search for the entermonitor inside the method
		MonitorPair mp = (MonitorPair) exitMonitorAnnotations.get(stmtAnnotation);
		Set exitMonitors = lockAnalysis.exitMonitorsIn(mp);
		Stmt enterMonitor = (Stmt) mp.getEnterMonitor();
		Annotation enterMonitorAnnotation = null;
		try {
			enterMonitorAnnotation = mda.getContainingAnnotation(enterMonitor);
		} catch (AnnotationException ae) {
			System.out.println("there is an AnnotationException! And enterMonitorAnnotation may be null");
		}
		if (enterMonitorAnnotation != null)
			dependAnnotations.add(enterMonitorAnnotation);

		//search for the extermonitors intermethod
		Set enterSet = getInterClassEnterMonitors(sootMethod, exitMonitors);
		dependAnnotations.addAll(enterSet);
		return dependAnnotations;
	}
	return dependAnnotations;
}
private DefaultMutableTreeNode getReadySuccDependAnns() {
	DefaultMutableTreeNode readyRoot = new DefaultMutableTreeNode("Successors");
	Set readyAnnotationsForOneNode;
	Annotation mda;
	Annotation currentAnn;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		currentAnn = startTreeNode.currentStmtAnnotation;
		readyAnnotationsForOneNode = getReadySuccDependAnnotationsFor(mda, currentAnn);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			currentAnn = (Annotation) currentNode;
			readyAnnotationsForOneNode = getReadySuccDependAnnotationsFor(mda, currentAnn);
		} else
			return readyRoot;


	//ready interclass

	Hashtable readyNodesForCurrentLevel = new Hashtable();
	if (!readyAnnotationsForOneNode.isEmpty())
		readyNodesForCurrentLevel.put(readyRoot, readyAnnotationsForOneNode);
	else
		return readyRoot;
	Hashtable readyNodesForNextLevel;
	do {
		readyNodesForNextLevel = new Hashtable();
		for (java.util.Iterator readyIt = readyNodesForCurrentLevel.keySet().iterator(); readyIt.hasNext();) {
			DefaultMutableTreeNode currentRootNode = (DefaultMutableTreeNode) readyIt.next();
			Set readyNodes = (Set) readyNodesForCurrentLevel.get(currentRootNode);
			//add ddAnnotations to currentRootNode as children of it
			for (Iterator rdIt = readyNodes.iterator(); rdIt.hasNext();) {
				Object rdNode = rdIt.next();
				if (!treePathContains(currentRootNode, rdNode)) {
					DefaultMutableTreeNode rdTreeNode = new DefaultMutableTreeNode(rdNode);
					currentRootNode.add(rdTreeNode);
					Set interReadyNodesForOneNode = null;
					if (rdNode instanceof StmtTreeNode) {
						StmtTreeNode stmtRdNode = (StmtTreeNode) rdNode;
						interReadyNodesForOneNode = getReadySuccDependAnnotationsFor(stmtRdNode.currentMethodDeclarationAnnotation, stmtRdNode.currentStmtAnnotation);
					} else
						if (rdNode instanceof Annotation) {
							//MethodDeclarationAnnotation parentMda = getMda(currentRootNode, readyRoot);
							Annotation parentMda = (Annotation) CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation((Annotation) rdNode);
							interReadyNodesForOneNode = getReadySuccDependAnnotationsFor(parentMda, (Annotation) rdNode);
						} else {
							System.out.println("rdNode should be StmtTreeNode or Annotation");
						}
					if (!interReadyNodesForOneNode.isEmpty())
						readyNodesForNextLevel.put(rdTreeNode, interReadyNodesForOneNode);
				}
			}
		}
		readyNodesForCurrentLevel = readyNodesForNextLevel;
	} while (!readyNodesForNextLevel.isEmpty());
	return readyRoot;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-20 16:18:32)
 * @return ca.mcgill.sable.util.Set
 * @param stmts ca.mcgill.sable.soot.jimple.Stmt[]
 */
private Set getRefValueSetFrom(Stmt[] stmts) {
	Set refValueSet = new ArraySet();
	for (int i = 0; i < stmts.length; i++) {
		Stmt stmt = stmts[i];
		for (Iterator boxIt = stmt.getUseBoxes().iterator(); boxIt.hasNext();) {
			ValueBox valueBox = (ValueBox) boxIt.next();
			Value value = valueBox.getValue();
			if ((value instanceof InvokeExpr) || (value instanceof Constant) || (value instanceof BinopExpr) ||(value instanceof NewExpr)) continue;
			if ((value.getType() instanceof RefType) || (value.getType() instanceof ArrayType))
				refValueSet.add(value);
		}
	}
	return refValueSet;
}
private DefaultMutableTreeNode getSynchDependAnns(boolean isPred) {
	if (!isPred)
		return getSynchSuccDependAnns();
	DefaultMutableTreeNode synchRoot = new DefaultMutableTreeNode("Predecessors");
	Set synchAnnotationsForOneNode;
	Annotation mda;
	Annotation currentAnn;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		currentAnn = startTreeNode.currentStmtAnnotation;
		synchAnnotationsForOneNode = getSynchPredDependAnnotationsFor(mda, currentAnn);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			currentAnn = (Annotation) currentNode;
			synchAnnotationsForOneNode = getSynchPredDependAnnotationsFor(mda, currentAnn);
		} else
			return synchRoot;
	if (synchAnnotationsForOneNode.isEmpty())
		return synchRoot;
	for (Iterator synIt = synchAnnotationsForOneNode.iterator(); synIt.hasNext();) {
		Annotation enterMonitorAnn = (Annotation) synIt.next();
		DefaultMutableTreeNode enterMonitorNode = new DefaultMutableTreeNode(enterMonitorAnn);
		/*
		if (currentAnn instanceof SynchronizedStmtAnnotation)
			enterMonitorNode = synchRoot;
		else
		*/
			synchRoot.add(enterMonitorNode);
		//add all exitmonitor annotations under the entermonitor
		/*
		Set exitMonitorAnns = (Set) synchAnnotationsForOneNode.get(enterMonitorAnn);
		for (Iterator exitIt = exitMonitorAnns.iterator(); exitIt.hasNext();) {
			Object exitAnn = exitIt.next();
			DefaultMutableTreeNode exitNode = new DefaultMutableTreeNode(exitAnn);
			enterMonitorNode.add(exitNode);
		}
		*/
		
	}
	return synchRoot;
}
/**
 * Return the SynchLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSynchLabel() {
	if (ivjSynchLabel == null) {
		try {
			ivjSynchLabel = new javax.swing.JLabel();
			ivjSynchLabel.setName("SynchLabel");
			ivjSynchLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/synchro.jpg")));
			ivjSynchLabel.setText("");
			ivjSynchLabel.setBackground(new java.awt.Color(204,204,255));
			ivjSynchLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSynchLabel;
}
/**
 * Return the SynchPredCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getSynchPredCheckBox() {
	if (ivjSynchPredCheckBox == null) {
		try {
			ivjSynchPredCheckBox = new javax.swing.JCheckBox();
			ivjSynchPredCheckBox.setName("SynchPredCheckBox");
			ivjSynchPredCheckBox.setText("");
			ivjSynchPredCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSynchPredCheckBox;
}
private Set getSynchPredDependAnnotationsFor(Annotation mda, Annotation stmtAnnotation) {
	Set dependAnnotations = new ArraySet();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	BuildPDG methodPDG = methodInfo.methodPDG;
	LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
	if (lockAnalysis == null)
		return dependAnnotations;
	Stmt stmtsInAnnotation[];
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();
	for (int i = 0; i < stmtsInAnnotation.length; i++) {
		Hashtable monitorPairs = lockAnalysis.dependOnMonitorPairs(stmtsInAnnotation[i]);
		for (java.util.Iterator pairsIt = monitorPairs.keySet().iterator(); pairsIt.hasNext();) {
			Stmt enterMonitorStmt = (Stmt) pairsIt.next();
			Annotation enterAnn = null;
			try {
				enterAnn = mda.getContainingAnnotation(enterMonitorStmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And enterMonitorStmtAnnotation may be null");
			}
			if (enterAnn != null)
				dependAnnotations.add(enterAnn);
		}
	}
	return dependAnnotations;
}
/**
 * Return the SynchSuccCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getSynchSuccCheckBox() {
	if (ivjSynchSuccCheckBox == null) {
		try {
			ivjSynchSuccCheckBox = new javax.swing.JCheckBox();
			ivjSynchSuccCheckBox.setName("SynchSuccCheckBox");
			ivjSynchSuccCheckBox.setText("");
			ivjSynchSuccCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSynchSuccCheckBox;
}
private Set getSynchSuccDependAnnotationsFor(Annotation mda, Annotation stmtAnnotation) {
	Set dependAnnotations = new ArraySet();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	StmtList stmtList = methodInfo.originalStmtList;
	BuildPDG methodPDG = methodInfo.methodPDG;
	LockAnalysis lockAnalysis = methodPDG.getLockAnalysis();
	if (lockAnalysis == null)
		return dependAnnotations;
	// to see if stmtAnnotation is exitmonitor
	List monitorPairList = lockAnalysis.getLockPairList();
	Hashtable exitMonitorAnnotations = getExitMonitorAnnotations(mda, monitorPairList);
	if (exitMonitorAnnotations.containsKey(stmtAnnotation)) {
		//search for the entermonitor inside the method
		MonitorPair mp = (MonitorPair) exitMonitorAnnotations.get(stmtAnnotation);
		Set exitMonitors = lockAnalysis.exitMonitorsIn(mp);
		Stmt enterMonitor = (Stmt) mp.getEnterMonitor();
		Annotation enterMonitorAnnotation = null;
		try {
			enterMonitorAnnotation = mda.getContainingAnnotation(enterMonitor);
		} catch (AnnotationException ae) {
			System.out.println("there is an AnnotationException! And enterMonitorAnnotation may be null");
		}
		if (enterMonitorAnnotation != null)
			dependAnnotations.add(enterMonitorAnnotation);

		//search for the extermonitors intermethod
		Set enterSet = getInterClassEnterMonitors(sootMethod, exitMonitors);
		dependAnnotations.addAll(enterSet);
	}
	return dependAnnotations;
}
private DefaultMutableTreeNode getSynchSuccDependAnns() {
	DefaultMutableTreeNode synchRoot = new DefaultMutableTreeNode("Successors");
	Set synchAnnotationsForOneNode;
	Annotation mda;
	Annotation currentAnn;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		currentAnn = startTreeNode.currentStmtAnnotation;
		synchAnnotationsForOneNode = getSynchSuccDependAnnotationsFor(mda, currentAnn);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			currentAnn = (Annotation) currentNode;
			synchAnnotationsForOneNode = getSynchSuccDependAnnotationsFor(mda, currentAnn);
		} else
			return synchRoot;
	if (synchAnnotationsForOneNode.isEmpty())
		return synchRoot;
	for (Iterator synIt = synchAnnotationsForOneNode.iterator(); synIt.hasNext();) {
		Object enterMonitorAnn = synIt.next();
		DefaultMutableTreeNode enterMonitorNode = new DefaultMutableTreeNode(enterMonitorAnn);
		synchRoot.add(enterMonitorNode);
	}
	return synchRoot;
}
/**
 * Return the JPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getToolBarInPanel() {
	if (ivjToolBarInPanel == null) {
		try {
			ivjToolBarInPanel = new javax.swing.JPanel();
			ivjToolBarInPanel.setName("ToolBarInPanel");
			ivjToolBarInPanel.setLayout(new java.awt.GridBagLayout());
			ivjToolBarInPanel.setBackground(new java.awt.Color(204,204,255));
			ivjToolBarInPanel.setMaximumSize(new java.awt.Dimension(3000, 100));
			ivjToolBarInPanel.setComponentOrientation(java.awt.ComponentOrientation.UNKNOWN);
			ivjToolBarInPanel.setPreferredSize(new java.awt.Dimension(0, 50));
			ivjToolBarInPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
			ivjToolBarInPanel.setMinimumSize(new java.awt.Dimension(228, 100));

			java.awt.GridBagConstraints constraintsBackToolBarButton = new java.awt.GridBagConstraints();
			constraintsBackToolBarButton.gridx = 0; constraintsBackToolBarButton.gridy = 0;
			constraintsBackToolBarButton.insets = new java.awt.Insets(5, 5, 12, 10);
			getToolBarInPanel().add(getBackToolBarButton(), constraintsBackToolBarButton);

			java.awt.GridBagConstraints constraintsForwardToolBarButton = new java.awt.GridBagConstraints();
			constraintsForwardToolBarButton.gridx = 1; constraintsForwardToolBarButton.gridy = 0;
			constraintsForwardToolBarButton.insets = new java.awt.Insets(5, 3, 12, 10);
			getToolBarInPanel().add(getForwardToolBarButton(), constraintsForwardToolBarButton);

			java.awt.GridBagConstraints constraintsBackwardSliceToolBarButton = new java.awt.GridBagConstraints();
			constraintsBackwardSliceToolBarButton.gridx = 2; constraintsBackwardSliceToolBarButton.gridy = 0;
			constraintsBackwardSliceToolBarButton.insets = new java.awt.Insets(5, 3, 12, 10);
			getToolBarInPanel().add(getBackwardSliceToolBarButton(), constraintsBackwardSliceToolBarButton);

			java.awt.GridBagConstraints constraintsForwardSliceToolBarButton = new java.awt.GridBagConstraints();
			constraintsForwardSliceToolBarButton.gridx = 3; constraintsForwardSliceToolBarButton.gridy = 0;
			constraintsForwardSliceToolBarButton.insets = new java.awt.Insets(5, 3, 12, 10);
			getToolBarInPanel().add(getForwardSliceToolBarButton(), constraintsForwardSliceToolBarButton);

			java.awt.GridBagConstraints constraintsClearToolBarButton = new java.awt.GridBagConstraints();
			constraintsClearToolBarButton.gridx = 4; constraintsClearToolBarButton.gridy = 0;
			constraintsClearToolBarButton.gridwidth = 5;
			constraintsClearToolBarButton.anchor = java.awt.GridBagConstraints.EAST;
			constraintsClearToolBarButton.weightx = 1.0;
			constraintsClearToolBarButton.ipadx = 7;
			constraintsClearToolBarButton.insets = new java.awt.Insets(5, 3, 12, 0);
			getToolBarInPanel().add(getClearToolBarButton(), constraintsClearToolBarButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjToolBarInPanel;
}
/**
 * Return the ToolBarPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getToolBarPanel() {
	if (ivjToolBarPanel == null) {
		try {
			ivjToolBarPanel = new javax.swing.JPanel();
			ivjToolBarPanel.setName("ToolBarPanel");
			ivjToolBarPanel.setPreferredSize(new java.awt.Dimension(0, 50));
			ivjToolBarPanel.setBorder(new javax.swing.border.EtchedBorder());
			ivjToolBarPanel.setLayout(new java.awt.BorderLayout());
			ivjToolBarPanel.setBackground(new java.awt.Color(204,204,255));
			getToolBarPanel().add(getDependOperationToolBar(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjToolBarPanel;
}
/**
 * Return the JPanel2 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getValueCheckBoxesPanel() {
	if (ivjValueCheckBoxesPanel == null) {
		try {
			ivjValueCheckBoxesPanel = new javax.swing.JPanel();
			ivjValueCheckBoxesPanel.setName("ValueCheckBoxesPanel");
			ivjValueCheckBoxesPanel.setLayout(getValueCheckBoxesPanelFlowLayout());
			ivjValueCheckBoxesPanel.setBackground(new java.awt.Color(204,204,255));
			getValueCheckBoxesPanel().add(getValuePredCheckBox(), getValuePredCheckBox().getName());
			getValueCheckBoxesPanel().add(getValueSuccCheckBox(), getValueSuccCheckBox().getName());
			getValueCheckBoxesPanel().add(getValueSetCheckBox(), getValueSetCheckBox().getName());
			getValueCheckBoxesPanel().add(getAllValuesButton(), getAllValuesButton().getName());
			getValueCheckBoxesPanel().add(getClearValuesButton(), getClearValuesButton().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueCheckBoxesPanel;
}
/**
 * Return the ValueCheckBoxesPanelFlowLayout property value.
 * @return java.awt.FlowLayout
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.FlowLayout getValueCheckBoxesPanelFlowLayout() {
	java.awt.FlowLayout ivjValueCheckBoxesPanelFlowLayout = null;
	try {
		/* Create part */
		ivjValueCheckBoxesPanelFlowLayout = new java.awt.FlowLayout();
		ivjValueCheckBoxesPanelFlowLayout.setAlignment(java.awt.FlowLayout.LEFT);
		ivjValueCheckBoxesPanelFlowLayout.setHgap(6);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjValueCheckBoxesPanelFlowLayout;
}
/**
 * Return the ValueFlowLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getValueFlowLabel() {
	if (ivjValueFlowLabel == null) {
		try {
			ivjValueFlowLabel = new javax.swing.JLabel();
			ivjValueFlowLabel.setName("ValueFlowLabel");
			ivjValueFlowLabel.setText("Value Flow Graph : ");
			ivjValueFlowLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueFlowLabel;
}
private Hashtable getValuePredAnnotationsFor(Annotation mda, Annotation stmtAnnotation) {
	Hashtable valuePredAnnotations = new Hashtable();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	BuildPDG methodPDG = methodInfo.methodPDG;
	Stmt stmtsInAnnotation[];
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();

	//collect all vars in statements in the annotation
	Set varSet = collectVarsFromStmts(stmtsInAnnotation);
	for (Iterator valueIt = varSet.iterator(); valueIt.hasNext();) {
		Value value = (Value) valueIt.next();
		DefaultMutableTreeNode localNode = new DefaultMutableTreeNode(value);
		Collection valuePreds = this.BOFA_Analysis.getDefs(value, sootMethod);
		//transform collection of jimple statement into a set of annotations
		Set predAnnotations = new ArraySet();
		for (Iterator stmtIt = valuePreds.iterator(); stmtIt.hasNext();) {
			edu.ksu.cis.bandera.bofa.Analysis.StmtMethodPair predStmtMethod = (edu.ksu.cis.bandera.bofa.Analysis.StmtMethodPair) stmtIt.next();
			Stmt predStmt = predStmtMethod.getStmt();
			if (predStmt instanceof IdentityStmt) {
				ParameterNode paraNode = new ParameterNode(methodInfo.sootClass, mda, (IdentityStmt) predStmt);
				predAnnotations.add(paraNode);
			} else {
				Annotation predStmtAnnotation = null;
				SootMethod methodEnclosingStmt = predStmtMethod.getSootMethod();
				Annotation methodAnnotation = null;
				if (methodEnclosingStmt.equals(sootMethod))
					methodAnnotation = mda;
				else
					methodAnnotation = CompilationManager.getAnnotationManager().getAnnotation(methodEnclosingStmt.getDeclaringClass(), methodEnclosingStmt);
			try {
				predStmtAnnotation = methodAnnotation.getContainingAnnotation(predStmt);
			} catch (AnnotationException ae) {
				System.out.println("there is an AnnotationException! And ddStmtAnnotation may be null");
			}
			if (predStmtAnnotation == null) {
				//this means predStmt is outside of current method or class.
				//************* this should be solved by reversing Annotation Manager ********
				// need to construct StmtTreeNode
			} else {
				if (!predStmtAnnotation.toString().equals(""))


					
					//if (!predStmtAnnotation.equals(stmtAnnotation))
					predAnnotations.add(predStmtAnnotation);
			}
		}
	}
	valuePredAnnotations.put(localNode, predAnnotations);
}
return valuePredAnnotations;
}
private DefaultMutableTreeNode getValuePredAnns() {
	DefaultMutableTreeNode currentValuePredRoot = new DefaultMutableTreeNode("Value Predecessors");
	Hashtable valuePredAnnotationsForOneNode;
	Annotation mda;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		valuePredAnnotationsForOneNode = getValuePredAnnotationsFor(mda, startTreeNode.currentStmtAnnotation);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			valuePredAnnotationsForOneNode = getValuePredAnnotationsFor(mda, (Annotation) currentNode);
		} else
			return currentValuePredRoot;
	if (valuePredAnnotationsForOneNode.isEmpty())
		return currentValuePredRoot;
	for (java.util.Iterator varIt = valuePredAnnotationsForOneNode.keySet().iterator(); varIt.hasNext();) {
		DefaultMutableTreeNode currentVarNode = (DefaultMutableTreeNode) varIt.next();
		currentValuePredRoot.add(currentVarNode);
		Set valuePredAnnotations = (Set) valuePredAnnotationsForOneNode.get(currentVarNode);
		//add ddAnnotations to currentRootNode as children of it
		for (Iterator predIt = valuePredAnnotations.iterator(); predIt.hasNext();) {
			Object predObject = predIt.next();
			if (predObject instanceof ParameterNode) {
				DefaultMutableTreeNode paraNode = new DefaultMutableTreeNode(predObject);
				if (predObject.toString().equals("JJJCTEMP$0"))
					paraNode = new DefaultMutableTreeNode("this");
				currentVarNode.add(paraNode);
			} else {
				Annotation predAnn = (Annotation) predObject;
				DefaultMutableTreeNode predNode = new DefaultMutableTreeNode(predAnn);
				currentVarNode.add(predNode);
			}
		}
	}
	return currentValuePredRoot;
}
/**
 * Return the JCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getValuePredCheckBox() {
	if (ivjValuePredCheckBox == null) {
		try {
			ivjValuePredCheckBox = new javax.swing.JCheckBox();
			ivjValuePredCheckBox.setName("ValuePredCheckBox");
			ivjValuePredCheckBox.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjValuePredCheckBox.setText("pred");
			ivjValuePredCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValuePredCheckBox;
}
/**
 * Return the JCheckBox2 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getValueSetCheckBox() {
	if (ivjValueSetCheckBox == null) {
		try {
			ivjValueSetCheckBox = new javax.swing.JCheckBox();
			ivjValueSetCheckBox.setName("ValueSetCheckBox");
			ivjValueSetCheckBox.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjValueSetCheckBox.setText("set");
			ivjValueSetCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueSetCheckBox;
}
private Hashtable getValueSuccAnnotationsFor(Annotation mda, Annotation stmtAnnotation) {
	Hashtable valueSuccAnnotations = new Hashtable();
	SootMethod sootMethod = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	Map sootMap = Slicer.sootMethodInfoMap;
	MethodInfo methodInfo = (MethodInfo) sootMap.get(sootMethod);
	BuildPDG methodPDG = methodInfo.methodPDG;
	Stmt stmtsInAnnotation[];
	if (stmtAnnotation instanceof SynchronizedStmtAnnotation) {
		stmtsInAnnotation = new Stmt[1];
		stmtsInAnnotation[0] = ((SynchronizedStmtAnnotation) stmtAnnotation).getEnterMonitor();
	} else
		if (stmtAnnotation instanceof ConditionalAnnotation) {
			stmtsInAnnotation = ((ConditionalAnnotation) stmtAnnotation).getTestStatements();
		} else
			if (stmtAnnotation instanceof ControlFlowAnnotation) {
				stmtsInAnnotation = ((ControlFlowAnnotation) stmtAnnotation).getTestStatements();
			} else
				stmtsInAnnotation = stmtAnnotation.getStatements();

	//collect all vars in statements in the annotation
	Set varSet = collectVarsFromStmts(stmtsInAnnotation);

	for (Iterator valueIt = varSet.iterator(); valueIt.hasNext();) {
		Value value = (Value) valueIt.next();
		DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(value);
		Collection valueSuccs = this.BOFA_Analysis.getUses(value, sootMethod);
		//transform collection of jimple statement into a set of annotations
		Set succAnnotations = new ArraySet();
		for (Iterator stmtIt = valueSuccs.iterator(); stmtIt.hasNext();) {
			edu.ksu.cis.bandera.bofa.Analysis.StmtMethodPair succStmtMethod = (edu.ksu.cis.bandera.bofa.Analysis.StmtMethodPair) stmtIt.next();
			Stmt succStmt = succStmtMethod.getStmt();
			if (succStmt instanceof IdentityStmt) {
				ParameterNode paraNode = new ParameterNode(methodInfo.sootClass, mda, (IdentityStmt) succStmt);
				succAnnotations.add(paraNode);
			} else {
				Annotation succStmtAnnotation = null;
				SootMethod methodEnclosingStmt = succStmtMethod.getSootMethod();
				Annotation methodAnnotation = null;
				if (methodEnclosingStmt.equals(sootMethod))
					methodAnnotation = mda;
				else
					methodAnnotation = CompilationManager.getAnnotationManager().getAnnotation(methodEnclosingStmt.getDeclaringClass(), methodEnclosingStmt);
			
				try {
					succStmtAnnotation = methodAnnotation.getContainingAnnotation(succStmt);
				} catch (AnnotationException ae) {
					System.out.println("there is an AnnotationException! And ddStmtAnnotation may be null");
				}
				if (!succStmtAnnotation.toString().equals(""))
					
					//if (!succStmtAnnotation.equals(stmtAnnotation))
					succAnnotations.add(succStmtAnnotation);
			}
		}
		valueSuccAnnotations.put(valueNode, succAnnotations);
	}
	return valueSuccAnnotations;
}
private DefaultMutableTreeNode getValueSuccAnns() {
	DefaultMutableTreeNode currentValueSuccRoot = new DefaultMutableTreeNode("Value Successors");
	Hashtable valueSuccAnnotationsForOneNode;
	Annotation mda;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) currentNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		currentMethodDecAnn = mda;
		valueSuccAnnotationsForOneNode = getValueSuccAnnotationsFor(mda, startTreeNode.currentStmtAnnotation);
	} else
		if (currentNode instanceof Annotation) {
			mda = currentMethodDecAnn;
			valueSuccAnnotationsForOneNode = getValueSuccAnnotationsFor(mda, (Annotation) currentNode);
		} else
			return currentValueSuccRoot;
	if (valueSuccAnnotationsForOneNode.isEmpty())
		return currentValueSuccRoot;
	for (java.util.Iterator varIt = valueSuccAnnotationsForOneNode.keySet().iterator(); varIt.hasNext();) {
		DefaultMutableTreeNode currentVarNode = (DefaultMutableTreeNode) varIt.next();
		currentValueSuccRoot.add(currentVarNode);
		Set valueSuccAnnotations = (Set) valueSuccAnnotationsForOneNode.get(currentVarNode);
		//add ddAnnotations to currentRootNode as children of it
		for (Iterator succIt = valueSuccAnnotations.iterator(); succIt.hasNext();) {
			Object succObject = succIt.next();
			if (succObject instanceof ParameterNode) {
				DefaultMutableTreeNode paraNode = new DefaultMutableTreeNode(succObject);
				if (succObject.toString().equals("JJJCTEMP$0"))
					paraNode = new DefaultMutableTreeNode("this");
				currentVarNode.add(paraNode);
			} else {
				Annotation succAnn = (Annotation) succObject;
				DefaultMutableTreeNode succNode = new DefaultMutableTreeNode(succAnn);
				currentVarNode.add(succNode);
			}
		}
	}
	return currentValueSuccRoot;
}
/**
 * Return the JCheckBox1 property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBox getValueSuccCheckBox() {
	if (ivjValueSuccCheckBox == null) {
		try {
			ivjValueSuccCheckBox = new javax.swing.JCheckBox();
			ivjValueSuccCheckBox.setName("ValueSuccCheckBox");
			ivjValueSuccCheckBox.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjValueSuccCheckBox.setText("succ");
			ivjValueSuccCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueSuccCheckBox;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-7 10:11:56)
 * @return javax.swing.tree.DefaultMutableTreeNode
 * @param dependKind java.lang.String
 * @param isPred boolean
 */
private DefaultMutableTreeNode getValueTreeNode(String valueKind) {
	int kind = valueKindsList.indexOf(valueKind);
	DefaultMutableTreeNode returnNode = null;
	switch (kind+6) {
		case VALUE_PRED :
			returnNode = getValuePredAnns();
			break;
		case VALUE_SUCC :
			returnNode = getValueSuccAnns();
			break;
		case VALUE_SET :
			returnNode = new DefaultMutableTreeNode("Value Set");
			calculateValueSet(returnNode);
			break;
		
	}
	return returnNode;
}
/**
 * Return the ViewerTabbedPane property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.ViewerTabbedPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private ViewerTabbedPane getViewerTabbedPane() {
	if (ivjViewerTabbedPane == null) {
		try {
			ivjViewerTabbedPane = new edu.ksu.cis.bandera.pdgslicer.dependency.ViewerTabbedPane();
			ivjViewerTabbedPane.setName("ViewerTabbedPane");
			// user code begin {1}
			criterionViewer = ivjViewerTabbedPane.getCriterionViewer();
			criterionViewer.setDependFrame(this);
			queryPanel = ivjViewerTabbedPane.getQueryPanel();
			queryPanel.setDependFrame(this);
			
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViewerTabbedPane;
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
public void historyComboBox_ActionEvents() {
	Object selectedNode = getHistoryComboBox().getSelectedItem();
	if (selectedNode.equals(currentNode))
		return;
	Configuration config = getCurrentConfig();
	nodeToConfigTable.put(currentNode, config);
	compactHistoryChain(currentNode);
	historyChain.addLast(currentNode);
	currentPositionOfHistoryChain++;
	if (!getBackToolBarButton().isEnabled())
		getBackToolBarButton().setEnabled(true);
	currentNode = selectedNode;
	config = (Configuration) nodeToConfigTable.get(selectedNode);
	restoreConfig(config);
	return;
}
/**
 * 
 * @return int
 * @param obj java.lang.Object
 */
int historyComboBoxItemsContains(Object obj) {
	for (int i = 0; i < getHistoryComboBox().getItemCount(); i++) {
		Object item = getHistoryComboBox().getItemAt(i);
		if (item.equals(obj))
			return i;
	}
	return -1;
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	this.addWindowListener(ivjEventHandler);
	getHistoryComboBox().addActionListener(ivjEventHandler);
	getBackToolBarButton().addActionListener(ivjEventHandler);
	getForwardToolBarButton().addActionListener(ivjEventHandler);
	getClearToolBarButton().addActionListener(ivjEventHandler);
	getDataPredCheckBox().addItemListener(ivjEventHandler);
	getDataSuccCheckBox().addItemListener(ivjEventHandler);
	getControlPredCheckBox().addItemListener(ivjEventHandler);
	getControlSuccCheckBox().addItemListener(ivjEventHandler);
	getDiverPredCheckBox().addItemListener(ivjEventHandler);
	getDiverSuccCheckBox().addItemListener(ivjEventHandler);
	getReadyPredCheckBox().addItemListener(ivjEventHandler);
	getReadySuccCheckBox().addItemListener(ivjEventHandler);
	getInterferPredCheckBox().addItemListener(ivjEventHandler);
	getInterferSuccCheckBox().addItemListener(ivjEventHandler);
	getSynchPredCheckBox().addItemListener(ivjEventHandler);
	getSynchSuccCheckBox().addItemListener(ivjEventHandler);
	getValuePredCheckBox().addItemListener(ivjEventHandler);
	getValueSuccCheckBox().addItemListener(ivjEventHandler);
	getValueSetCheckBox().addItemListener(ivjEventHandler);
	getAllpredButton().addActionListener(ivjEventHandler);
	getAllsuccButton().addActionListener(ivjEventHandler);
	getAllDependButton().addActionListener(ivjEventHandler);
	getNopredButton().addActionListener(ivjEventHandler);
	getNosuccButton().addActionListener(ivjEventHandler);
	getClearDependButton().addActionListener(ivjEventHandler);
	getAllValuesButton().addActionListener(ivjEventHandler);
	getClearValuesButton().addActionListener(ivjEventHandler);
	getBackwardSliceToolBarButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		/*
		if (dependFrame != null) {
		dependFrame = null;
		if (dependencyValueViewer != null) {
		dependencyValueViewer.dispose();
		dependencyValueViewer = null;
		}
		}
		*/
		dependFrame = this;

		// user code end
		setName("Dependencies");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(612, 702);
		setTitle("Dependency and Value Flow Browser");
		setContentPane(getDependencyFrameContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	dependencyKindsList = new ca.mcgill.sable.util.ArrayList();
	for (int i = 0; i < dependencyKinds.length; i++) {
		dependencyKindsList.add(i, dependencyKinds[i]);
	}
	valueKindsList = new ca.mcgill.sable.util.ArrayList();
	for (int i = 0; i < valueKinds.length; i++) {
		valueKindsList.add(i, valueKinds[i]);
	}
	dependencyValueViewer = new DependencyValueViewer();
	dependencyValueViewer.setDependFrame(this);
	dependencyValueViewer.setToEmptyState();
	if (!reRunJJJC())
		return;
	Vector criterion = criterionViewer.getSliceInterests();
	if (criterion == null)
		return;
	//System.out.println("criterion: " + criterion);
	criterionViewer.showCriterionInViewer(criterion);
	if (!runBuildingPDG())
		return;
		getCodeBrowserPane().unreachableSootClasses = edu.ksu.cis.bandera.pdgslicer.Slicer.unreachableClasses;
		getCodeBrowserPane().reachableSootMethods = edu.ksu.cis.bandera.pdgslicer.Slicer.reachableMethods;
	getCodeBrowserPane().updateHierTree(null);
	//moved to Slicer class
	//edu.ksu.cis.bandera.bofa.BOFA.analyze();
	//BOFA_Analysis = edu.ksu.cis.bandera.bofa.Analysis.init();
	BOFA_Analysis = Slicer.BOFA_Analysis;

	// user code end
}
/**
 * Insert the method's description here.
 * Creation date: (00-5-15 22:46:25)
 */
private void initializeConfig() {
	isChangeCheckBoxByProgram = true;
	if (doPredDataDepend) {
		doPredDataDepend = false;
		getDataPredCheckBox().setSelected(false);
	}
	if (doPredControlDepend) {
		doPredControlDepend = false;
		getControlPredCheckBox().setSelected(false);
	}
	if (doPredReadyDepend) {
		doPredReadyDepend = false;
		getReadyPredCheckBox().setSelected(false);
	}
	if (doPredSynchDepend) {
		doPredSynchDepend = false;
		getSynchPredCheckBox().setSelected(false);
	}
	if (doPredInterferDepend) {
		doPredInterferDepend = false;
		getInterferPredCheckBox().setSelected(false);
	}
	if (doPredDivergentDepend) {
		doPredDivergentDepend = false;
		getDiverPredCheckBox().setSelected(false);
	}
	if (doSuccDataDepend) {
		doSuccDataDepend = false;
		getDataSuccCheckBox().setSelected(false);
	}
	if (doSuccControlDepend) {
		doSuccControlDepend = false;
		getControlSuccCheckBox().setSelected(false);
	}
	if (doSuccReadyDepend) {
		doSuccReadyDepend = false;
		getReadySuccCheckBox().setSelected(false);
	}
	if (doSuccSynchDepend) {
		doSuccSynchDepend = false;
		getSynchSuccCheckBox().setSelected(false);
	}
	if (doSuccInterferDepend) {
		doSuccInterferDepend = false;
		getInterferSuccCheckBox().setSelected(false);
	}
	if (doSuccDivergentDepend) {
		doSuccDivergentDepend = false;
		getDiverSuccCheckBox().setSelected(false);
	}
	if (doPredValue) {
		doPredValue = false;
		getValuePredCheckBox().setSelected(false);
	}
	if (doSuccValue) {
		doSuccValue = false;
		getValueSuccCheckBox().setSelected(false);
	}
	if (doSetValue) {
		doSetValue = false;
		getValueSetCheckBox().setSelected(false);
	}
	isChangeCheckBoxByProgram = false;
	//remove all predecessor and successsor dependency in current tree and
	dependencyValueViewer.currentDependTreeRoot = new DefaultMutableTreeNode(currentNode);
	dependencyValueViewer.getDependencyTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentDependTreeRoot));
	dependencyValueViewer.currentValueTreeRoot = new DefaultMutableTreeNode(currentNode);
	dependencyValueViewer.getValueTree().setModel(new DefaultTreeModel(dependencyValueViewer.currentValueTreeRoot));
	dependencyValueViewer.changeViewerState();
}
private boolean interferOnListContains(List interferOnList, Set stmts) {
	for (Iterator interferIt = interferOnList.iterator(); interferIt.hasNext();) {
		InterferStmt interferStmt = (InterferStmt) interferIt.next();
		if (stmts.contains(interferStmt.interferStmt))
			return true;
	}
	return false;
}
/**
 * Comment
 */
public void interferPredCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getInterferPredCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doPredInterferDepend = !doPredInterferDepend;
	if (!doPredInterferDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Interference Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Predecessors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentInterferRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Interference Dependence")) {
				currentInterferRoot = oneChild;
				break;
			}
		}
		if (currentInterferRoot == null) {
			currentInterferRoot = new DefaultMutableTreeNode("Interference Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Interference Dependence", isDependency);
			treeModel.insertNodeInto(currentInterferRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode interferPredTreeNode = getInterferDependAnns(true);
		if (interferPredTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(interferPredTreeNode, currentInterferRoot, 0);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(interferPredTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(interferPredTreeNode, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void interferSuccCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getInterferSuccCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSuccInterferDepend = !doSuccInterferDepend;
	if (!doSuccInterferDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Interference Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Successors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentInterferRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Interference Dependence")) {
				currentInterferRoot = oneChild;
				break;
			}
		}
		if (currentInterferRoot == null) {
			currentInterferRoot = new DefaultMutableTreeNode("Interference Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Interference Dependence", isDependency);
			treeModel.insertNodeInto(currentInterferRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode interferSuccTreeNode = getInterferDependAnns(false);
		if (interferSuccTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(interferSuccTreeNode, currentInterferRoot, currentInterferRoot.getChildCount());
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(interferSuccTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(interferSuccTreeNode, isDependency);
		}
	}

	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Method to handle events for the ItemListener interface.
 * @param e java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void itemStateChanged(java.awt.event.ItemEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getDataPredCheckBox()) 
		connEtoC8(e);
	if (e.getSource() == getDataSuccCheckBox()) 
		connEtoC12(e);
	if (e.getSource() == getControlPredCheckBox()) 
		connEtoC13(e);
	if (e.getSource() == getControlSuccCheckBox()) 
		connEtoC16(e);
	if (e.getSource() == getDiverPredCheckBox()) 
		connEtoC17(e);
	if (e.getSource() == getDiverSuccCheckBox()) 
		connEtoC18(e);
	if (e.getSource() == getReadyPredCheckBox()) 
		connEtoC19(e);
	if (e.getSource() == getReadySuccCheckBox()) 
		connEtoC20(e);
	if (e.getSource() == getInterferPredCheckBox()) 
		connEtoC21(e);
	if (e.getSource() == getInterferSuccCheckBox()) 
		connEtoC22(e);
	if (e.getSource() == getSynchPredCheckBox()) 
		connEtoC23(e);
	if (e.getSource() == getSynchSuccCheckBox()) 
		connEtoC24(e);
	if (e.getSource() == getValuePredCheckBox()) 
		connEtoC25(e);
	if (e.getSource() == getValueSuccCheckBox()) 
		connEtoC26(e);
	if (e.getSource() == getValueSetCheckBox()) 
		connEtoC27(e);
	// user code begin {2}
	// user code end
}
/**
 * Comment
 */
public void jimpleCodeForAnnotation(Annotation ann) {
	Stmt[] stmts = ann.getStatements();
	getViewerTabbedPane().getJimpleViewer().getJimpleTitleLabel().setText("Jimple code for " + ann.toString());
	StringBuffer str = new StringBuffer(1024);
	for (int i = 0; i < stmts.length; i++) {
		str.append(stmts[i].toString() + "\n");
	}
	getViewerTabbedPane().getJimpleViewer().getJimpleTextArea().setText(str.toString());
	getViewerTabbedPane().getJimpleViewer().getJimpleTextScrollPane().validate();
	getViewerTabbedPane().getJimpleViewer().getJimpleTextScrollPane().repaint();
	//getJimpleCodeDialog().show();
	return;
}
/**
 * Comment
 */
public void jimpleCodeForSootClass(SootClass sc) {
	Writer str = new StringWriter(1024);
	PrintWriter printWriter = new PrintWriter(str, true);
	sc.printTo(new StoredBody(Jimple.v()), printWriter);
	getViewerTabbedPane().getJimpleViewer().getJimpleTitleLabel().setText("Jimple code for " + sc);
	getViewerTabbedPane().getJimpleViewer().getJimpleTextArea().setText(str.toString());
	getViewerTabbedPane().getJimpleViewer().getJimpleTextScrollPane().validate();
	getViewerTabbedPane().getJimpleViewer().getJimpleTextScrollPane().repaint();
	return;
}
/**
 * Comment
 */
public void jimpleCodeForSootMethod(SootMethod sm) {
	Writer str = new StringWriter(1024);
	PrintWriter printWriter = new PrintWriter(str, true);
	JimpleBody jimpleBody = (JimpleBody) sm.getBody(Jimple.v());
	jimpleBody.printTo(printWriter, 0);
	getViewerTabbedPane().getJimpleViewer().getJimpleTitleLabel().setText("Jimple code for " + sm);
	getViewerTabbedPane().getJimpleViewer().getJimpleTextArea().setText(str.toString());
	getViewerTabbedPane().getJimpleViewer().getJimpleTextScrollPane().validate();
	getViewerTabbedPane().getJimpleViewer().getJimpleTextScrollPane().repaint();
	return;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		Dependencies aDependencies;
		aDependencies = new Dependencies();
		aDependencies.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aDependencies.setVisible(true);

	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}


}
/**
 * Comment
 */
public void nopredButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getNopredButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (doPredDataDepend) {
		doPredDataDepend = false;
		getDataPredCheckBox().setSelected(false);
	}
	if (doPredControlDepend) {
		doPredControlDepend = false;
		getControlPredCheckBox().setSelected(false);
	}
	if (doPredReadyDepend) {
		doPredReadyDepend = false;
		getReadyPredCheckBox().setSelected(false);
	}
	if (doPredSynchDepend) {
		doPredSynchDepend = false;
		getSynchPredCheckBox().setSelected(false);
	}
	if (doPredInterferDepend) {
		doPredInterferDepend = false;
		getInterferPredCheckBox().setSelected(false);
	}
	if (doPredDivergentDepend) {
		doPredDivergentDepend = false;
		getDiverPredCheckBox().setSelected(false);
	}
	isChangeCheckBoxByProgram = false;
	//remove all predecessor dependency in current tree and
	DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
	Set removableChild = new ca.mcgill.sable.util.ArraySet();
	for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
		DefaultMutableTreeNode predecessor = null;
		boolean foundSuccessor = false;
		for (int j = 0; j < oneChild.getChildCount(); j++) {
			DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
			if (predSuccChild.toString().equals("Successors"))
				foundSuccessor = true;
			if (predSuccChild.toString().equals("Predecessors"))
				predecessor = predSuccChild;
		}
		if (predecessor == null)
			continue;
		treeModel.removeNodeFromParent(predecessor);
		if (!foundSuccessor)
			removableChild.add(oneChild);
	}
	for (Iterator childIt = removableChild.iterator(); childIt.hasNext();) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) childIt.next();
		treeModel.removeNodeFromParent(oneChild);
	}
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void nosuccButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if (currentNode == null) {
		selectStatementWarning(getNosuccButton());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	isChangeCheckBoxByProgram = true;
	if (doSuccDataDepend) {
		doSuccDataDepend = false;
		getDataSuccCheckBox().setSelected(false);
	}
	if (doSuccControlDepend) {
		doSuccControlDepend = false;
		getControlSuccCheckBox().setSelected(false);
	}
	if (doSuccReadyDepend) {
		doSuccReadyDepend = false;
		getReadySuccCheckBox().setSelected(false);
	}
	if (doSuccSynchDepend) {
		doSuccSynchDepend = false;
		getSynchSuccCheckBox().setSelected(false);
	}
	if (doSuccInterferDepend) {
		doSuccInterferDepend = false;
		getInterferSuccCheckBox().setSelected(false);
	}
	if (doSuccDivergentDepend) {
		doSuccDivergentDepend = false;
		getDiverSuccCheckBox().setSelected(false);
	}
	isChangeCheckBoxByProgram = false;
	//remove all predecessor dependency in current tree and
	DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
	Set removableChild = new ca.mcgill.sable.util.ArraySet();
	for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
		DefaultMutableTreeNode successor = null;
		boolean foundPredecessor = false;
		for (int j = 0; j < oneChild.getChildCount(); j++) {
			DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
			if (predSuccChild.toString().equals("Successors"))
				successor = predSuccChild;
			if (predSuccChild.toString().equals("Predecessors"))
				foundPredecessor = true;
			;
		}
		if (successor == null)
			continue;
		treeModel.removeNodeFromParent(successor);
		if (!foundPredecessor)
			removableChild.add(oneChild);
	}
	for (Iterator childIt = removableChild.iterator(); childIt.hasNext();) {
		DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) childIt.next();
		treeModel.removeNodeFromParent(oneChild);
	}
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-6-15 0:22:39)
 * @return boolean
 * @param readyOnList ca.mcgill.sable.util.List
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
private boolean readyOnListContains(List readyOnList, Stmt stmt) {
	for (Iterator readyIt = readyOnList.iterator(); readyIt.hasNext();) {
		ReadyDependStmt readyDependStmt = (ReadyDependStmt) readyIt.next();
		if (readyDependStmt.readyOnStmt == stmt)
			return true;
	}
	return false;
}
private  boolean readyOnListContains(List readyOnList, Set exitMonitors) {
	for (Iterator readyIt = readyOnList.iterator(); readyIt.hasNext();) {
		ReadyDependStmt readyDependStmt = (ReadyDependStmt) readyIt.next();
		if (exitMonitors.contains(readyDependStmt.readyOnStmt))
			return true;
	}
	return false;
}
/**
 * Comment
 */
public void readyPredCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getReadyPredCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doPredReadyDepend = !doPredReadyDepend;
	if (!doPredReadyDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Ready Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Predecessors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentReadyRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Ready Dependence")) {
				currentReadyRoot = oneChild;
				break;
			}
		}
		if (currentReadyRoot == null) {
			currentReadyRoot = new DefaultMutableTreeNode("Ready Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Ready Dependence", isDependency);
			treeModel.insertNodeInto(currentReadyRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode readyPredTreeNode = getReadyDependAnns(true);
		if (readyPredTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(readyPredTreeNode, currentReadyRoot, 0);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(readyPredTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(readyPredTreeNode, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void readySuccCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getReadySuccCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSuccReadyDepend = !doSuccReadyDepend;
	if (!doSuccReadyDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Ready Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Successors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentReadyRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Ready Dependence")) {
				currentReadyRoot = oneChild;
				break;
			}
		}
		if (currentReadyRoot == null) {
			currentReadyRoot = new DefaultMutableTreeNode("Ready Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Ready Dependence", isDependency);
			treeModel.insertNodeInto(currentReadyRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode readySuccTreeNode = getReadyDependAnns(false);
		if (readySuccTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(readySuccTreeNode, currentReadyRoot, currentReadyRoot.getChildCount());
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(readySuccTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(readySuccTreeNode, isDependency);
		}
	}

	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * 
 */
boolean reRunJJJC() {
	try {
		CompilationManager.compile();
		if (CompilationManager.getExceptions().size() > 0) {
			System.out.println("exceptions in compilation manager: ");
			for (Enumeration eenu = CompilationManager.getExceptions().elements(); eenu.hasMoreElements();)
				System.out.println(eenu.nextElement());
			throw new Exception("Errors occured when compiling the program");
		}
	} catch (Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, e.getMessage(), "JJJC Phase for Dependencies --- Error", JOptionPane.ERROR_MESSAGE);
		this.success = false;
		return false;
	}
	this.success = true;
	return true;
}
public void restoreConfig(Configuration config) {
	initializeConfig();
	//dependencyValueViewer.setToEmptyState();
	isChangeCheckBoxByProgram = true;
	if (config.dataPred) {
		doPredDataDepend = true;
		getDataPredCheckBox().setSelected(true);
	}
	if (config.controlPred) {
		doPredControlDepend = true;
		getControlPredCheckBox().setSelected(true);
	}
	if (config.readyPred) {
		doPredReadyDepend = true;
		getReadyPredCheckBox().setSelected(true);
	}
	if (config.synchPred) {
		doPredSynchDepend = true;
		getSynchPredCheckBox().setSelected(true);
	}
	if (config.interferPred) {
		doPredInterferDepend = true;
		getInterferPredCheckBox().setSelected(true);
	}
	if (config.divergentPred) {
		doPredDivergentDepend = true;
		getDiverPredCheckBox().setSelected(true);
	}
	if (config.dataSucc) {
		doSuccDataDepend = true;
		getDataSuccCheckBox().setSelected(true);
	}
	if (config.controlSucc) {
		doSuccControlDepend = true;
		getControlSuccCheckBox().setSelected(true);
	}
	if (config.readySucc) {
		doSuccReadyDepend = true;
		getReadySuccCheckBox().setSelected(true);
	}
	if (config.synchSucc) {
		doSuccSynchDepend = true;
		getSynchSuccCheckBox().setSelected(true);
	}
	if (config.interferSucc) {
		doSuccInterferDepend = true;
		getInterferSuccCheckBox().setSelected(true);
	}
	if (config.divergentSucc) {
		doSuccDivergentDepend = true;
		getDiverSuccCheckBox().setSelected(true);
	}
	if (config.valuePred) {
		doPredValue = true;
		getValuePredCheckBox().setSelected(true);
	}
	if (config.valueSucc) {
		doSuccValue = true;
		getValueSuccCheckBox().setSelected(true);
	}
	if (config.valueSet) {
		doSetValue = true;
		getValueSetCheckBox().setSelected(true);
	}
	isChangeCheckBoxByProgram = false;
	//criterionViewer.currentCriterion = config.currentCriterion;
	//need to restore the currentCriterion to the criterion dialog
	//criterionViewer.showInCriterionDialog(currentCriterion);

	dependencyValueViewer.currentDependTreeRoot = config.currentDependTreeRoot;
	dependencyValueViewer.currentValueTreeRoot = config.currentValueTreeRoot;
	if (config.currentDependTreeRoot != null) {
		dependencyValueViewer.getDependencyTree().setModel(new DefaultTreeModel(config.currentDependTreeRoot));
		boolean isDependency = true;
		dependencyValueViewer.expandDependTreeToOneLevel(isDependency);
	}
	if (config.currentValueTreeRoot != null) {
		dependencyValueViewer.getValueTree().setModel(new DefaultTreeModel(config.currentValueTreeRoot));
		boolean isDependency = false;
		dependencyValueViewer.expandDependTreeToOneLevel(isDependency);
	}
	dependencyValueViewer.changeStateFromEmptyTo(config.dependValueViewerState);
	getViewerTabbedPane().setSelectedIndex(config.viewerState);
	queryPanel.getQueryKindComboBox().setSelectedIndex(QueryPanel.NO_QUERY);
	queryPanel.getQueryKindComboBox().setSelectedIndex(config.queryIndex);
	getCodeBrowserPane().setCurrentNodeSelected(currentNode);
}
/**
 * Insert the method's description here.
 * Creation date: (00-5-28 15:04:15)
 * @param config edu.ksu.cis.bandera.pdgslicer.dependency.Configuration
 */
private void restoreConfigByAddingNewNode(Configuration config) {
	if (config.dataPred) {
		getDataPredCheckBox().setSelected(true);
	}
	if (config.controlPred) {
		getControlPredCheckBox().setSelected(true);
	}
	if (config.readyPred) {
		getReadyPredCheckBox().setSelected(true);
	}
	if (config.synchPred) {
		getSynchPredCheckBox().setSelected(true);
	}
	if (config.interferPred) {
		getInterferPredCheckBox().setSelected(true);
	}
	if (config.divergentPred) {
		getDiverPredCheckBox().setSelected(true);
	}
	if (config.dataSucc) {
		getDataSuccCheckBox().setSelected(true);
	}
	if (config.controlSucc) {
		getControlSuccCheckBox().setSelected(true);
	}
	if (config.readySucc) {
		getReadySuccCheckBox().setSelected(true);
	}
	if (config.synchSucc) {
		getSynchSuccCheckBox().setSelected(true);
	}
	if (config.interferSucc) {
		getInterferSuccCheckBox().setSelected(true);
	}
	if (config.divergentSucc) {
		getDiverSuccCheckBox().setSelected(true);
	}
	if (config.valuePred) {
		getValuePredCheckBox().setSelected(true);
	}
	if (config.valueSucc) {
		getValueSuccCheckBox().setSelected(true);
	}
	if (config.valueSet) {
		getValueSetCheckBox().setSelected(true);
	}
	queryPanel.getQueryKindComboBox().setSelectedIndex(QueryPanel.NO_QUERY);
	queryPanel.getQueryKindComboBox().setSelectedIndex(config.queryIndex);
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-4 17:21:42)
 */
 boolean runBuildingPDG() {
	try {
		Hashtable compiledClasses = CompilationManager.getAnnotationManager().getAnnotationTable();
		SootClass[] sootClasses = new SootClass[compiledClasses.size()];
		int i = 0;
		for (Enumeration e = compiledClasses.keys(); e.hasMoreElements(); i++)
			sootClasses[i] = (SootClass) e.nextElement();
		slicer = new Slicer(sootClasses, CompilationManager.getAnnotationManager());
		slicer.buildPDG();
	} catch (Exception e) {
		System.out.println("exception: " + e);
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		//BUI.isExecuting = false;
		CompilationManager.setModifiedMethodTable(new Hashtable());
		this.success = false;
		return false;
	}
	this.success = true;
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-13 14:49:43)
 * @param checkBox javax.swing.JCheckBox
 */
private void selectStatementWarning(AbstractButton checkBox) {
	JOptionPane.showMessageDialog(this, "Please choose statement first", "No statement selected", JOptionPane.WARNING_MESSAGE);
	isChangeCheckBoxByProgram = true;
	checkBox.setSelected(false);
	isChangeCheckBoxByProgram = false;
}
private Set stmtSetEffectedBy(Map interferMap, Set stmtSet) {
	Set effectedStmtSet = new ArraySet();
	for (Iterator keyIt = interferMap.keySet().iterator(); keyIt.hasNext();) {
		Stmt keyStmt = (Stmt) keyIt.next();
		List interferOnList = (List) interferMap.get(keyStmt);
		if (interferOnListContains(interferOnList, stmtSet))
			effectedStmtSet.add(keyStmt);
	}
	return effectedStmtSet;
}
/**
 * Comment
 */
public void synchPredCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getSynchPredCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doPredSynchDepend = !doPredSynchDepend;
	if (!doPredSynchDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Synchronization Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Predecessors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentSynchRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Synchronization Dependence")) {
				currentSynchRoot = oneChild;
				break;
			}
		}
		if (currentSynchRoot == null) {
			currentSynchRoot = new DefaultMutableTreeNode("Synchronization Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Synchronization Dependence", isDependency);
			treeModel.insertNodeInto(currentSynchRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode synchPredTreeNode = getSynchDependAnns(true);
		if (synchPredTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(synchPredTreeNode, currentSynchRoot, 0);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(synchPredTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(synchPredTreeNode, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void synchSuccCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getSynchSuccCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSuccSynchDepend = !doSuccSynchDepend;
	if (!doSuccSynchDepend) {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		//remove data subtree;

		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Synchronization Dependence")) {
				if (oneChild.getChildCount() == 1) {
					treeModel.removeNodeFromParent(oneChild);
					break;
				} else {
					for (int j = 0; j < oneChild.getChildCount(); j++) {
						DefaultMutableTreeNode predSuccChild = (DefaultMutableTreeNode) oneChild.getChildAt(j);
						if (predSuccChild.toString().equals("Successors")) {
							treeModel.removeNodeFromParent(predSuccChild);
							break;
						}
					}
				}
				break;
			}
		}
	} else {
		boolean isDependency = true;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getDependencyTree().getModel();
		DefaultMutableTreeNode currentSynchRoot = null;
		for (int i = 0; i < dependencyValueViewer.currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentDependTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Synchronization Dependence")) {
				currentSynchRoot = oneChild;
				break;
			}
		}
		if (currentSynchRoot == null) {
			currentSynchRoot = new DefaultMutableTreeNode("Synchornization Dependence");
			int insertIndex = dependencyValueViewer.getInsertIndexOf("Synchronization Dependence", isDependency);
			treeModel.insertNodeInto(currentSynchRoot, dependencyValueViewer.currentDependTreeRoot, insertIndex);
		}
		DefaultMutableTreeNode synchSuccTreeNode = getSynchDependAnns(false);
		if (synchSuccTreeNode != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(synchSuccTreeNode, currentSynchRoot, currentSynchRoot.getChildCount());
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getDependencyTree().scrollPathToVisible(new TreePath(synchSuccTreeNode.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(synchSuccTreeNode, isDependency);
		}
	}

	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 19:52:50)
 * @return java.util.Vector
 * @param v java.util.Vector
 */
Vector transformCriterion(Vector v) {
	SootClassManager scm = CompilationManager.getSootClassManager();
	for (Enumeration e = v.elements(); e.hasMoreElements();) {
		SliceInterest si = (SliceInterest) e.nextElement();
		SootClass scInV = si.getSootClass();
		SootClass newSc = scm.getClass(scInV.getName());
		si.setSootClass(newSc);
		if (si instanceof SliceLocal) {
			SliceLocal sloc = (SliceLocal) si;
			SootMethod sm = sloc.getSootMethod();
			SootMethod newSm = newSc.getMethod(sm.getName());
			sloc.setSootMethod(newSm);
			Local loc = sloc.getLocal();
			JimpleBody jimpleBody = (JimpleBody) newSm.getBody(Jimple.v());
			Local newLoc = jimpleBody.getLocal(loc.getName());
			sloc.setLocal(newLoc);
		} else
			if (si instanceof SlicePoint) {
				SlicePoint spt = (SlicePoint) si;
				SootMethod sm = spt.getSootMethod();
				SootMethod newSm = newSc.getMethod(sm.getName());
				spt.setSootMethod(newSm);
				int index = spt.getIndex();
				JimpleBody jimpleBody = (JimpleBody) newSm.getBody(Jimple.v());
				StmtList stmtList = jimpleBody.getStmtList();
				Stmt newStmt = (Stmt) stmtList.get(index);
				spt.setStmt(newStmt);
			} else
				if (si instanceof SliceField) {
					SliceField sfd = (SliceField) si;
					SootField stfd = sfd.getSootField();
					SootField newSf = newSc.getField(stfd.getName());
					sfd.setSootField(newSf);
				}
	}
	return v;
}
private boolean treePathContains(DefaultMutableTreeNode currentLeaf, Object aNode) {
	if (aNode instanceof StmtTreeNode) {
		StmtTreeNode node = (StmtTreeNode) aNode;
		TreeNode[] nodesInPath = currentLeaf.getPath();
		for (int i = 0; i < nodesInPath.length; i++) {
			DefaultMutableTreeNode nodeInPath = (DefaultMutableTreeNode) nodesInPath[i];
			Object objectInNode = nodeInPath.getUserObject();
			if (node.equals(objectInNode))
				return true;
		}
	} else
		if (aNode instanceof Annotation) {
			Annotation nodeAnnotation = (Annotation) aNode;
			TreeNode[] nodesInPath = currentLeaf.getPath();
			for (int i = 0; i < nodesInPath.length; i++) {
				DefaultMutableTreeNode nodeInPath = (DefaultMutableTreeNode) nodesInPath[i];
				Object objectInNode = nodeInPath.getUserObject();
				Object annotationInNode = null;
				if (objectInNode instanceof StmtTreeNode)
					annotationInNode = (Object) ((StmtTreeNode) objectInNode).currentStmtAnnotation;
				else
					annotationInNode = objectInNode;
				if (nodeAnnotation.equals(annotationInNode))
					return true;
			}
		}
	return false;
}
/**
 * Comment
 */
public void valuePredCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getValuePredCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doPredValue = !doPredValue;
	if (!doPredValue) {
		//remove value predecessor from value tree
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getValueTree().getModel();
		//remove value subtree;

		for (int i = 0; i < dependencyValueViewer.currentValueTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentValueTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Value Predecessors")) {
				treeModel.removeNodeFromParent(oneChild);
				break;
			}
		}
	} else {
		//calculate value predecessor and add it to current tree.
		boolean isDependency = false;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getValueTree().getModel();
		//DefaultMutableTreeNode currentValuePredRoot =new DefaultMutableTreeNode("Value Predecessors");
		int insertIndex = dependencyValueViewer.getInsertIndexOf("Value Predecessors", isDependency);
		DefaultMutableTreeNode currentValuePredRoot = getValuePredAnns();
		if (currentValuePredRoot != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(currentValuePredRoot, dependencyValueViewer.currentValueTreeRoot, insertIndex);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getValueTree().scrollPathToVisible(new TreePath(currentValuePredRoot.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(currentValuePredRoot, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void valueSetCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getValueSetCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSetValue = !doSetValue;
	if (doSetValue) {
		//calculate value set and add it to current tree.
		boolean isDependency = false;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getValueTree().getModel();
		DefaultMutableTreeNode currentValueSetRoot = new DefaultMutableTreeNode("Value Set");
		int insertIndex = dependencyValueViewer.getInsertIndexOf("Value Set", isDependency);
		treeModel.insertNodeInto(currentValueSetRoot, dependencyValueViewer.currentValueTreeRoot, insertIndex);
		calculateValueSet(currentValueSetRoot);
		dependencyValueViewer.getValueTree().scrollPathToVisible(new TreePath(currentValueSetRoot.getPath()));
		dependencyValueViewer.expandAndCollapseFrom(currentValueSetRoot, isDependency);
	} else {
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getValueTree().getModel();
		for (int i = 0; i < dependencyValueViewer.currentValueTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentValueTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Value Set")) {
				treeModel.removeNodeFromParent(oneChild);
				break;
			}
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Comment
 */
public void valueSuccCheckBox_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	if (currentNode == null) {
		selectStatementWarning(getValueSuccCheckBox());
		return;
	}
	dependencyValueViewer.setState(Frame.NORMAL);
	dependencyValueViewer.toFront();
	doSuccValue = !doSuccValue;
	if (!doSuccValue) {
		//remove value successors from data tree
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getValueTree().getModel();
		//remove value subtree;

		for (int i = 0; i < dependencyValueViewer.currentValueTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) dependencyValueViewer.currentValueTreeRoot.getChildAt(i);
			if (oneChild.toString().equals("Value Successors")) {
				treeModel.removeNodeFromParent(oneChild);
				break;
			}
		}
	} else {
		//calculate value successors and add it to current tree.
		boolean isDependency = false;
		DefaultTreeModel treeModel = (DefaultTreeModel) dependencyValueViewer.getValueTree().getModel();
		//DefaultMutableTreeNode currentValuePredRoot =new DefaultMutableTreeNode("Value Predecessors");
		int insertIndex = dependencyValueViewer.getInsertIndexOf("Value Successors", isDependency);
		DefaultMutableTreeNode currentValueSuccRoot = getValueSuccAnns();
		if (currentValueSuccRoot != null) {
			//currentTreeRoot.add(dataTreeNode);
			treeModel.insertNodeInto(currentValueSuccRoot, dependencyValueViewer.currentValueTreeRoot, insertIndex);
			// Make sure the user can see the lovely new node.
			dependencyValueViewer.getValueTree().scrollPathToVisible(new TreePath(currentValueSuccRoot.getPath()));

			//getDependencyTree().setModel(new DefaultTreeModel(currentTreeRoot));
			//expand and collapse node path

			dependencyValueViewer.expandAndCollapseFrom(currentValueSuccRoot, isDependency);
		}
	}
	// and then
	dependencyValueViewer.changeViewerState();
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-6-15 0:11:32)
 * @return java.util.HashSet
 * @param mdInfo edu.ksu.cis.bandera.pdgslicer.MethodInfo
 * @param notifyStmt edu.ksu.cis.bandera.pdgslicer.SynchroStmt
 */
private  Set waitSetEffectedBy(MethodInfo methodInfo, SynchroStmt notifyStmt) {
	Set waitSet = new ArraySet();
	BuildPDG methodPDG = methodInfo.methodPDG;
	Map readyDependMap = methodPDG.getReadyDependMap();
	if (readyDependMap == null)
		return waitSet;
	Stmt notifyJimpleStmt = notifyStmt.getWaitNotify();
	for (Iterator keyIt = readyDependMap.keySet().iterator(); keyIt.hasNext();) {
		Stmt keyStmt = (Stmt) keyIt.next();
		List readyOnList = (List) readyDependMap.get(keyStmt);
		if (readyOnListContains(readyOnList, notifyJimpleStmt))
			waitSet.add(keyStmt);
	}
	return waitSet;
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowActivated(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowClosed(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == this) 
		connEtoC1(e);
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowClosing(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowDeactivated(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowDeiconified(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowIconified(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowOpened(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
}
