package edu.ksu.cis.bandera.pdgslicer.dependency;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import javax.swing.*;
import java.util.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import edu.ksu.cis.bandera.annotation.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
/**
 * Insert the type's description here.
 * Creation date: (00-10-24 10:40:14)
 * @author: 
 */
public class CriterionViewer extends JPanel {
	private boolean setSelectedByProgram = false;
	private Vector currentCriterion = new Vector();
	private Vector lastTimeRunningCriterion = new Vector();
	Vector runningCriterion = null;
	private JLabel ivjLocationsLabel = null;
	private DroppableLocationsList ivjLocationsList = null;
	private JPanel ivjLocationsPanel = null;
	private JScrollPane ivjLocationsScrollPane = null;
	private JSplitPane ivjPointVarSplitPane = null;
	private JButton ivjRemoveLocationButton = null;
	private JButton ivjRemoveStatementButton = null;
	private JButton ivjRemoveVarsButton = null;
	private JLabel ivjStatementLabel = null;
	private JPanel ivjStatementPanel = null;
	private JScrollPane ivjStatementScrollPane = null;
	private JSplitPane ivjStmtControlSplitPane = null;
	private JLabel ivjVarsLabel = null;
	private DroppableVariablesList ivjVarsList = null;
	private JPanel ivjVarsPanel = null;
	private JScrollPane ivjVarsScrollPane = null;
	private JSplitPane ivjLocationsVarsSplitPane = null;
	private DraggableVariablesList ivjLocationVarsList = null;
	private JScrollPane ivjLocationVarsScrollPane = null;
	private DroppableStatementsList ivjStatementsList = null;
	private Dependencies dependFrame;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.MouseListener, javax.swing.event.ListSelectionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == CriterionViewer.this.getRemoveStatementButton()) 
				connEtoC1();
			if (e.getSource() == CriterionViewer.this.getRemoveLocationButton()) 
				connEtoC2();
			if (e.getSource() == CriterionViewer.this.getRemoveVarsButton()) 
				connEtoC3();
		};
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == CriterionViewer.this.getStatementsList()) 
				connEtoC6(e);
			if (e.getSource() == CriterionViewer.this.getLocationsList()) 
				connEtoC7(e);
		};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mousePressed(java.awt.event.MouseEvent e) {};
		public void mouseReleased(java.awt.event.MouseEvent e) {};
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			if (e.getSource() == CriterionViewer.this.getLocationsList()) 
				connEtoC4();
			if (e.getSource() == CriterionViewer.this.getStatementsList()) 
				connEtoC5();
			if (e.getSource() == CriterionViewer.this.getVarsList()) 
				connEtoC8();
			if (e.getSource() == CriterionViewer.this.getLocationVarsList()) 
				connEtoC9();
		};
	};
/**
 * CriterionViewer constructor comment.
 */
public CriterionViewer() {
	super();
	initialize();
}
/**
 * CriterionViewer constructor comment.
 * @param layout java.awt.LayoutManager
 */
public CriterionViewer(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * CriterionViewer constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public CriterionViewer(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * CriterionViewer constructor comment.
 * @param isDoubleBuffered boolean
 */
public CriterionViewer(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * Insert the method's description here.
 * Creation date: (00-6-21 1:13:28)
 * @param var java.lang.Object
 */
void addLocationToCriterion(Object location, Annotation locationAnnotation) {
	if (!dependFrame.getBackwardSliceToolBarButton().isEnabled())
		dependFrame.getBackwardSliceToolBarButton().setEnabled(true);
	int locationIndex = currentCriterion.indexOf(location);
	if (locationIndex >= 0) {
		System.out.println("the point has been in the current criterion");
		return;
	}
	currentCriterion.add(location);
	DefaultListModel listModel = null;
	//(DefaultListModel) getLocationsList().getModel();
	SlicePoint sp = (SlicePoint) location;
	LocationNodeInCriterionViewer ln = null;
	ln = new LocationNodeInCriterionViewer(sp.getSootMethod(), locationAnnotation, sp);
	listModel = (DefaultListModel) getLocationsList().getModel();

	listModel.addElement(ln);
	setSelectedByProgram = true;
	getLocationsList().setSelectedValue(ln, true);
	getStatementsList().clearSelection();
	calculateLocalsFor(ln);
	setSelectedByProgram = false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-6-21 1:13:28)
 * @param var java.lang.Object
 */
void addStatementToCriterion(Object location, SlicePoint[] locationArray, Annotation locationAnnotation) {
	if (!dependFrame.getBackwardSliceToolBarButton().isEnabled())
		dependFrame.getBackwardSliceToolBarButton().setEnabled(true);
	int locationIndex = currentCriterion.indexOf(location);
	if (locationIndex >= 0) {
		Object location2 = currentCriterion.get(locationIndex);
		if (location2 instanceof SliceStatement) {
			System.out.println("the statement has been in the current criterion");
			return;
		} else
			if (location2 instanceof SlicePoint) {
				currentCriterion.remove(location2);
				currentCriterion.add(location);
				SlicePoint sp = (SlicePoint) location;
				LocationNodeInCriterionViewer ln = new StatementNodeInCriterionViewer(sp.getSootMethod(), locationAnnotation, sp,locationArray);
				DefaultListModel listModel = (DefaultListModel) getStatementsList().getModel();
				listModel.addElement(ln);
				setSelectedByProgram = true;
				getStatementsList().setSelectedValue(ln, true);
				getLocationsList().clearSelection();
				calculateLocalsFor(ln);
				setSelectedByProgram = false;
				//remove location2 from location list
				listModel = (DefaultListModel) getLocationsList().getModel();
				removeLocationFrom(location2, listModel);
				return;
			}
	}
	currentCriterion.add(location);
	DefaultListModel listModel = null;
	//(DefaultListModel) getLocationsList().getModel();
	SlicePoint sp = (SlicePoint) location;
	LocationNodeInCriterionViewer ln = null;

	//ln = new StatementNodeInCriterionViewer(sp.getSootMethod(), locationAnnotation, sp);
	listModel = (DefaultListModel) getStatementsList().getModel();
	if (!statementsListContains(listModel, locationAnnotation))
		ln = new StatementNodeInCriterionViewer(sp.getSootMethod(), locationAnnotation, sp, locationArray);
	if (ln == null)
		return;
	listModel.addElement(ln);
	setSelectedByProgram = true;
	getStatementsList().setSelectedValue(ln, true);
	getLocationsList().clearSelection();
	//calculateLocalsFor(ln);
	setSelectedByProgram = false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-6-21 1:13:28)
 * @param var java.lang.Object
 */
void addVarToCriterion(Object var) {
	if (!dependFrame.getBackwardSliceToolBarButton().isEnabled())
		dependFrame.getBackwardSliceToolBarButton().setEnabled(true);
	if (currentCriterion.contains(var)) {
		System.out.println("the variable has been in the current criterion");
		return;
	}
	currentCriterion.add(var);
	DefaultListModel listModel = (DefaultListModel) getVarsList().getModel();
	listModel.addElement(var);
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-4 13:55:17)
 * @return java.util.Vector
 * @param currentNode java.lang.Object
 */
private Vector buildCriterionForCurrentNode(Object currentNode) {
	Vector returnCriterion = null;
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode stn = (StmtTreeNode) currentNode;
		Annotation currentStmtAnnotation = stn.currentStmtAnnotation;
		if ((currentStmtAnnotation instanceof MethodDeclarationAnnotation) || (currentStmtAnnotation instanceof ConstructorDeclarationAnnotation))
			return null;
		returnCriterion = new Vector();
		Annotation currentMda = stn.currentMethodDeclarationAnnotation;
		SootMethod sm = ((MethodDeclarationAnnotation) currentMda).getSootMethod();
		SootClass sc = stn.currentSootClass;
		Stmt stmts[] = currentStmtAnnotation.getStatements();
		for (int i = 0; i < stmts.length; i++) {
			Stmt stmt = stmts[i];
			int sind = SlicePoint.getStmtIndex(sm, stmt);
			SliceStatement sliceStmt = new SliceStatement(sc, sm, stmt, sind);
			returnCriterion.addElement(sliceStmt);
		}
	}
	return returnCriterion;
}
private Vector buildLocalListFor(Annotation annotation, SootMethod sm) {
	Vector localList = new Vector();
	String className = null;
	SootClass sc = null;
	Object leftAnnotation = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(annotation);
	sc = sm.getDeclaringClass();
	className = sc.getName().trim();
	Hashtable table = annotation.getDeclaredLocalVariables();
	for (Enumeration e = table.keys(); e.hasMoreElements();) {
		String name = (String) e.nextElement();
		Local local = (Local) table.get(name);
		if (local.getType() instanceof ca.mcgill.sable.soot.ArrayType) {
		} else
			if (local.getType() instanceof RefType) {
				SliceLocal sl = new SliceLocal(sc, sm, local);
				localList.addElement(sl);
			} else {
				SliceLocal sl = new SliceLocal(sc, sm, local);
				localList.addElement(sl);
			}
	}
	return localList;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-27 17:41:42)
 * @param ln edu.ksu.cis.bandera.pdgslicer.dependency.LocationNodeInCriterionViewer
 */
private void calculateLocalsFor(LocationNodeInCriterionViewer locationNode) {
	Vector locals = buildLocalListFor(locationNode.stmtAnnotation, locationNode.sootMethod);
	DefaultListModel listModel = (DefaultListModel) getLocationVarsList().getModel();
	listModel.removeAllElements();
	for (Enumeration e = locals.elements(); e.hasMoreElements();)
		listModel.addElement(e.nextElement());
}
/**
 * Comment
 */
SlicePoint calculateSlicePoint(Object userObjectInMethodTree) {
	if (!(userObjectInMethodTree instanceof Annotation))
		return null;
	SlicePoint sp = null;
	Annotation a = (Annotation) userObjectInMethodTree;
	Annotation leftAnnotation = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
	if (leftAnnotation instanceof ConstructorDeclarationAnnotation) {
		ConstructorDeclarationAnnotation ann = (ConstructorDeclarationAnnotation) leftAnnotation;
		SootMethod sm = ann.getSootMethod();
		SootClass sc = sm.getDeclaringClass();
		Stmt stmts[] = a.getStatements();
		Stmt stmt = stmts[0];
		int sind = SlicePoint.getStmtIndex(sm, stmts[0]);
		sp = new SlicePoint(sc, sm, stmt, sind);
		//addLocationToCriterion(sp, a);
	} else
		if (leftAnnotation instanceof MethodDeclarationAnnotation) {
			MethodDeclarationAnnotation ann = (MethodDeclarationAnnotation) leftAnnotation;
			SootMethod sm = ann.getSootMethod();
			SootClass sc = sm.getDeclaringClass();
			Stmt stmts[] = a.getStatements();
			Stmt stmt = stmts[0];
			int sind = SlicePoint.getStmtIndex(sm, stmts[0]);
			sp = new SlicePoint(sc, sm, stmt, sind);
			//addLocationToCriterion(sp, a);
		}
	return sp;
}
/**
 * Comment
 */
SlicePoint[] calculateSliceStatements(Object userObjectInMethodTree) {
	if (!(userObjectInMethodTree instanceof Annotation))
		return null;
	SlicePoint[] sp = null;
	Annotation a = (Annotation) userObjectInMethodTree;
	Annotation leftAnnotation = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
	if (leftAnnotation instanceof ConstructorDeclarationAnnotation) {
		ConstructorDeclarationAnnotation ann = (ConstructorDeclarationAnnotation) leftAnnotation;
		SootMethod sm = ann.getSootMethod();
		SootClass sc = sm.getDeclaringClass();
		Stmt stmts[] = a.getStatements();
		sp = new SlicePoint[stmts.length];
		for (int i = 0; i < stmts.length; i++) {
			Stmt stmt = stmts[i];
			int sind = SlicePoint.getStmtIndex(sm, stmt);
			sp[i] = new SliceStatement(sc, sm, stmt, sind);
		}
		//addLocationToCriterion(sp, a);
	} else
		if (leftAnnotation instanceof MethodDeclarationAnnotation) {
			MethodDeclarationAnnotation ann = (MethodDeclarationAnnotation) leftAnnotation;
			SootMethod sm = ann.getSootMethod();
			SootClass sc = sm.getDeclaringClass();
			Stmt stmts[] = a.getStatements();
			sp = new SlicePoint[stmts.length];
			for (int i = 0; i < stmts.length; i++) {
				Stmt stmt = stmts[i];
				int sind = SlicePoint.getStmtIndex(sm, stmts[i]);
				sp[i] = new SliceStatement(sc, sm, stmt, sind);
			}
			//addLocationToCriterion(sp, a);
		}
	return sp;
}
/**
 * connEtoC1:  (RemoveStatementButton.action. --> CriterionViewer.removeStatementButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		// user code end
		this.removeStatementButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (RemoveLocationButton.action. --> CriterionViewer.removeLocationButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.removeLocationButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (RemoveVarsButton.action. --> CriterionViewer.removeVarsButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3() {
	try {
		// user code begin {1}
		// user code end
		this.removeVarsButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (LocationsList.listSelection. --> CriterionViewer.locationsList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		if (!setSelectedByProgram)
		// user code end
		this.locationsList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (StatementsList.listSelection. --> CriterionViewer.statementsList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5() {
	try {
		// user code begin {1}
		if (!setSelectedByProgram)
		// user code end
		this.statementsList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (StatementsList.mouse.mouseClicked(java.awt.event.MouseEvent) --> CriterionViewer.statementsList_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.statementsList_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (LocationsList.mouse.mouseClicked(java.awt.event.MouseEvent) --> CriterionViewer.locationsList_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.locationsList_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (VarsList.listSelection. --> CriterionViewer.varsList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8() {
	try {
		// user code begin {1}
		if (!setSelectedByProgram)
		// user code end
		this.varsList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (LocationVarsList.listSelection. --> CriterionViewer.locationVarsList_ListSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9() {
	try {
		// user code begin {1}
		if (!setSelectedByProgram)
		// user code end
		this.locationVarsList_ListSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Return the LocationsLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getLocationsLabel() {
	if (ivjLocationsLabel == null) {
		try {
			ivjLocationsLabel = new javax.swing.JLabel();
			ivjLocationsLabel.setName("LocationsLabel");
			ivjLocationsLabel.setText("Control Slice");
			ivjLocationsLabel.setForeground(java.awt.Color.black);
			ivjLocationsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLocationsLabel;
}
/**
 * Return the LocationsList property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.DroppableLocationsList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private DroppableLocationsList getLocationsList() {
	if (ivjLocationsList == null) {
		try {
			ivjLocationsList = new edu.ksu.cis.bandera.pdgslicer.dependency.DroppableLocationsList();
			ivjLocationsList.setName("LocationsList");
			ivjLocationsList.setBounds(0, 0, 160, 120);
			// user code begin {1}
			ivjLocationsList.setCriterionViewer(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLocationsList;
}
/**
 * Return the LocationsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getLocationsPanel() {
	if (ivjLocationsPanel == null) {
		try {
			ivjLocationsPanel = new javax.swing.JPanel();
			ivjLocationsPanel.setName("LocationsPanel");
			ivjLocationsPanel.setLayout(new java.awt.BorderLayout());
			getLocationsPanel().add(getLocationsLabel(), "North");
			getLocationsPanel().add(getRemoveLocationButton(), "South");
			getLocationsPanel().add(getLocationsVarsSplitPane(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLocationsPanel;
}
/**
 * Return the LocationsScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getLocationsScrollPane() {
	if (ivjLocationsScrollPane == null) {
		try {
			ivjLocationsScrollPane = new javax.swing.JScrollPane();
			ivjLocationsScrollPane.setName("LocationsScrollPane");
			getLocationsScrollPane().setViewportView(getLocationsList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLocationsScrollPane;
}
/**
 * Return the LocationsVarsSplitPane property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JSplitPane getLocationsVarsSplitPane() {
	if (ivjLocationsVarsSplitPane == null) {
		try {
			ivjLocationsVarsSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
			ivjLocationsVarsSplitPane.setName("LocationsVarsSplitPane");
			ivjLocationsVarsSplitPane.setDividerSize(1);
			ivjLocationsVarsSplitPane.setDividerLocation(40);
			getLocationsVarsSplitPane().add(getLocationsScrollPane(), "left");
			getLocationsVarsSplitPane().add(getLocationVarsScrollPane(), "right");
			// user code begin {1}
			ivjLocationsVarsSplitPane.setDividerLocation(80);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLocationsVarsSplitPane;
}
/**
 * Return the LocationVarsList property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.DraggableVariablesList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private DraggableVariablesList getLocationVarsList() {
	if (ivjLocationVarsList == null) {
		try {
			ivjLocationVarsList = new edu.ksu.cis.bandera.pdgslicer.dependency.DraggableVariablesList();
			ivjLocationVarsList.setName("LocationVarsList");
			ivjLocationVarsList.setBounds(0, 0, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLocationVarsList;
}
/**
 * Return the LocationVarsScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getLocationVarsScrollPane() {
	if (ivjLocationVarsScrollPane == null) {
		try {
			ivjLocationVarsScrollPane = new javax.swing.JScrollPane();
			ivjLocationVarsScrollPane.setName("LocationVarsScrollPane");
			getLocationVarsScrollPane().setViewportView(getLocationVarsList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLocationVarsScrollPane;
}
/**
 * Return the PointVarSplitPane property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JSplitPane getPointVarSplitPane() {
	if (ivjPointVarSplitPane == null) {
		try {
			ivjPointVarSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
			ivjPointVarSplitPane.setName("PointVarSplitPane");
			ivjPointVarSplitPane.setDividerSize(2);
			ivjPointVarSplitPane.setDividerLocation(200);
			getPointVarSplitPane().add(getStmtControlSplitPane(), "left");
			getPointVarSplitPane().add(getVarsPanel(), "right");
			// user code begin {1}
			//getPointVarSplitPane().setDividerLocation(800);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPointVarSplitPane;
}
/**
 * Return the RemoveLocationButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveLocationButton() {
	if (ivjRemoveLocationButton == null) {
		try {
			ivjRemoveLocationButton = new javax.swing.JButton();
			ivjRemoveLocationButton.setName("RemoveLocationButton");
			ivjRemoveLocationButton.setText("Remove Control Point");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveLocationButton;
}
/**
 * Return the RemoveStatementButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveStatementButton() {
	if (ivjRemoveStatementButton == null) {
		try {
			ivjRemoveStatementButton = new javax.swing.JButton();
			ivjRemoveStatementButton.setName("RemoveStatementButton");
			ivjRemoveStatementButton.setText("Remove Statement");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveStatementButton;
}
/**
 * Return the RemoveVarsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRemoveVarsButton() {
	if (ivjRemoveVarsButton == null) {
		try {
			ivjRemoveVarsButton = new javax.swing.JButton();
			ivjRemoveVarsButton.setName("RemoveVarsButton");
			ivjRemoveVarsButton.setText("Remove Variable");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRemoveVarsButton;
}
/**
 * 
 * @return java.util.Vector
 */
Vector getSliceInterests() {
	SliceInterestEnv sliceInterestEnv = new SliceInterestEnv();
	sliceInterestEnv.setCriterionViewer(this);
	sliceInterestEnv.setDependFrame(this.dependFrame);
	return sliceInterestEnv.getSliceInterests();
}
/**
 * Return the StatementLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getStatementLabel() {
	if (ivjStatementLabel == null) {
		try {
			ivjStatementLabel = new javax.swing.JLabel();
			ivjStatementLabel.setName("StatementLabel");
			ivjStatementLabel.setText("Statement Slice");
			ivjStatementLabel.setForeground(java.awt.Color.black);
			ivjStatementLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStatementLabel;
}
/**
 * Return the StatementPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getStatementPanel() {
	if (ivjStatementPanel == null) {
		try {
			ivjStatementPanel = new javax.swing.JPanel();
			ivjStatementPanel.setName("StatementPanel");
			ivjStatementPanel.setLayout(new java.awt.BorderLayout());
			getStatementPanel().add(getStatementLabel(), "North");
			getStatementPanel().add(getRemoveStatementButton(), "South");
			getStatementPanel().add(getStatementScrollPane(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStatementPanel;
}
/**
 * Return the StatementScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getStatementScrollPane() {
	if (ivjStatementScrollPane == null) {
		try {
			ivjStatementScrollPane = new javax.swing.JScrollPane();
			ivjStatementScrollPane.setName("StatementScrollPane");
			getStatementScrollPane().setViewportView(getStatementsList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStatementScrollPane;
}
/**
 * Return the StatementsList property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.DroppableStatementsList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public DroppableStatementsList getStatementsList() {
	if (ivjStatementsList == null) {
		try {
			ivjStatementsList = new edu.ksu.cis.bandera.pdgslicer.dependency.DroppableStatementsList();
			ivjStatementsList.setName("StatementsList");
			ivjStatementsList.setBounds(0, 0, 160, 120);
			// user code begin {1}
			ivjStatementsList.setCriterionViewer(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStatementsList;
}
/**
 * Return the StmtControlSplitPane property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JSplitPane getStmtControlSplitPane() {
	if (ivjStmtControlSplitPane == null) {
		try {
			ivjStmtControlSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
			ivjStmtControlSplitPane.setName("StmtControlSplitPane");
			ivjStmtControlSplitPane.setDividerSize(2);
			ivjStmtControlSplitPane.setDividerLocation(240);
			getStmtControlSplitPane().add(getStatementPanel(), "left");
			getStmtControlSplitPane().add(getLocationsPanel(), "right");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStmtControlSplitPane;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-29 11:00:17)
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.StmtTreeNode
 * @param ln edu.ksu.cis.bandera.pdgslicer.dependency.LocationNode
 */
private StmtTreeNode getStmtTreeNodeFromLocationNode(LocationNodeInCriterionViewer ln) {
	SootClass sc = ln.sootMethod.getDeclaringClass();
	Annotation mda = CompilationManager.getAnnotationManager().getAnnotation(sc, ln.sootMethod);
	Annotation stmtAnn = ln.stmtAnnotation;
	return new StmtTreeNode(sc, mda, stmtAnn);
}
/**
 * Return the VarsLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getVarsLabel() {
	if (ivjVarsLabel == null) {
		try {
			ivjVarsLabel = new javax.swing.JLabel();
			ivjVarsLabel.setName("VarsLabel");
			ivjVarsLabel.setText("Variable Slice");
			ivjVarsLabel.setForeground(java.awt.Color.black);
			ivjVarsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVarsLabel;
}
/**
 * Return the VarsList property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.DroppableVariablesList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private DroppableVariablesList getVarsList() {
	if (ivjVarsList == null) {
		try {
			ivjVarsList = new edu.ksu.cis.bandera.pdgslicer.dependency.DroppableVariablesList();
			ivjVarsList.setName("VarsList");
			ivjVarsList.setBounds(0, 0, 160, 120);
			// user code begin {1}
			ivjVarsList.setCriterionViewer(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVarsList;
}
/**
 * Return the VarsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getVarsPanel() {
	if (ivjVarsPanel == null) {
		try {
			ivjVarsPanel = new javax.swing.JPanel();
			ivjVarsPanel.setName("VarsPanel");
			ivjVarsPanel.setLayout(new java.awt.BorderLayout());
			getVarsPanel().add(getVarsLabel(), "North");
			getVarsPanel().add(getRemoveVarsButton(), "South");
			getVarsPanel().add(getVarsScrollPane(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVarsPanel;
}
/**
 * Return the VarsScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getVarsScrollPane() {
	if (ivjVarsScrollPane == null) {
		try {
			ivjVarsScrollPane = new javax.swing.JScrollPane();
			ivjVarsScrollPane.setName("VarsScrollPane");
			getVarsScrollPane().setViewportView(getVarsList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVarsScrollPane;
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
	getRemoveStatementButton().addActionListener(ivjEventHandler);
	getRemoveLocationButton().addActionListener(ivjEventHandler);
	getRemoveVarsButton().addActionListener(ivjEventHandler);
	getLocationsList().addListSelectionListener(ivjEventHandler);
	getStatementsList().addListSelectionListener(ivjEventHandler);
	getStatementsList().addMouseListener(ivjEventHandler);
	getLocationsList().addMouseListener(ivjEventHandler);
	getVarsList().addListSelectionListener(ivjEventHandler);
	getLocationVarsList().addListSelectionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("CriterionViewer");
		setLayout(new java.awt.BorderLayout());
		setSize(766, 367);
		add(getPointVarSplitPane(), "Center");
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	// user code end
}
/**
 * Comment
 */
public void locationsList_ListSelectionEvents() {
	Object selectedValue = getLocationsList().getSelectedValue();
	if (selectedValue instanceof LocationNodeInCriterionViewer) {
		setSelectedByProgram = true;
		getStatementsList().clearSelection();
		getLocationVarsList().clearSelection();
		getVarsList().clearSelection();
		setSelectedByProgram = false;
		LocationNodeInCriterionViewer locationNode = (LocationNodeInCriterionViewer) selectedValue;
		calculateLocalsFor(locationNode);
		dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedValue);
	} else
		System.out.println("selected value in the location list should be LocationNodeInCriterionViewer");
	return;
}
/**
 * Comment
 */
public void locationsList_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		int selIndex = getLocationsList().locationToIndex(mouseEvent.getPoint());
	if (selIndex != -1) {
		if (mouseEvent.getClickCount() == 1) {
			//mySingleClick(selRow, selPath);
			//System.out.println("Do nothing --- My sigle click on the list");
		} else
			if (mouseEvent.getClickCount() == 2) {
				//myDoubleClick(selRow, selPath);
				//System.out.println("My DOUBLE click on the list");
				LocationNodeInCriterionViewer selectedStmt = (LocationNodeInCriterionViewer) getLocationsList().getSelectedValue();
				if (selectedStmt == null)
					return;
				StmtTreeNode selectedNode = getStmtTreeNodeFromLocationNode(selectedStmt);
				dependFrame.addSelectedNodeToDependFrame(selectedNode);
				//System.out.println("End adding by double clicking");
			}
	}
	return;
}
/**
 * Comment
 */
public void locationVarsList_ListSelectionEvents() {
	Object selectedValue = getLocationVarsList().getSelectedValue();
	if (selectedValue instanceof SliceVariable) {
		setSelectedByProgram = true;
		getStatementsList().clearSelection();
		getLocationsList().clearSelection();
		getVarsList().clearSelection();
		setSelectedByProgram = false;
		if (selectedValue instanceof SliceField) {
			dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedValue);
		} else
			if (selectedValue instanceof SliceLocal) {
				dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedValue);
			}
	} else
		System.out.println("selected value in the location variables list should be SliceVariable");
	return;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		CriterionViewer aCriterionViewer;
		aCriterionViewer = new CriterionViewer();
		frame.setContentPane(aCriterionViewer);
		frame.setSize(aCriterionViewer.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JPanel");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void removeLocationButton_ActionEvents() {
	LocationNodeInCriterionViewer ln = (LocationNodeInCriterionViewer) getLocationsList().getSelectedValue();
	currentCriterion.remove(ln.slicePoint);
	//remove all variables related to this location and not used in any statement node
	DefaultListModel listModel = (DefaultListModel) getLocationsList().getModel();
	listModel.removeElement(getLocationsList().getSelectedValue());
	if (listModel.isEmpty()) {
		DefaultListModel varListModel = (DefaultListModel) getLocationVarsList().getModel();
		varListModel.removeAllElements();
	}
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-27 16:16:01)
 * @param listModel javax.swing.DefaultListModel
 */
private void removeLocationFrom(Object location, DefaultListModel listModel) {
	for (Enumeration en = listModel.elements(); en.hasMoreElements();) {
		LocationNodeInCriterionViewer ln = (LocationNodeInCriterionViewer) en.nextElement();
		if (location.equals(ln.slicePoint)) {
			listModel.removeElement(ln);
			return;
		}
	}
}
/**
 * Comment
 */
public void removeStatementButton_ActionEvents() {
	StatementNodeInCriterionViewer ln = (StatementNodeInCriterionViewer) getStatementsList().getSelectedValue();
	for (int i = 0; i < ln.locationArray.length; i++)
		currentCriterion.remove(ln.locationArray[i]);
	DefaultListModel listModel = (DefaultListModel) getStatementsList().getModel();
	listModel.removeElement(getStatementsList().getSelectedValue());
	return;
}
/**
 * Comment
 */
public void removeVarsButton_ActionEvents() {
	currentCriterion.remove(getVarsList().getSelectedValue());
	// if the var is not used in any statement node then can be removed
	// otherwise, warning?
	DefaultListModel listModel = (DefaultListModel) getVarsList().getModel();
	listModel.removeElement(getVarsList().getSelectedValue());
	return;
}
void runSlicerWithCurrentCriterion() {
	//currentCriterion is only for storing the criterion specified by users using criterion viewer

	if (currentCriterion.isEmpty()) {
		//make current statement in the dependency frame as the criterion
		runningCriterion = buildCriterionForCurrentNode(dependFrame.currentNode);
	} else
		runningCriterion = (Vector) currentCriterion.clone();
	if ((runningCriterion == null) || runningCriterion.isEmpty()) {
		System.out.println("The current criterion is null or empty");
		return;
	}
	if (runningCriterion.containsAll(lastTimeRunningCriterion) && lastTimeRunningCriterion.containsAll(runningCriterion)) {
		System.out.println("The current criterion is the same as the last one");
		return;
	}
	// else run slicer with current running criterion
	boolean emptyCriterion = dependFrame.slicer.preProcessing(runningCriterion);
	dependFrame.slicer.slicing();
	dependFrame.slicer.postProcessingOnAnnotation();
	//update display in codeBrowserPane
	CodeBrowserPane codeBrowserPane = dependFrame.getCodeBrowserPane();
	codeBrowserPane.setPostProcessValues();
	codeBrowserPane.repaintTrees();
	// finally
	lastTimeRunningCriterion = runningCriterion;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-27 17:19:22)
 * @param dp edu.ksu.cis.bandera.pdgslicer.dependency.Dependencies
 */
void setDependFrame(Dependencies dp) {
	dependFrame = dp;
}
/**
 * 
 */
void showCriterionInViewer(Vector sliceInterests) {
	//empty current criterion in the list;
	DefaultListModel listModel = (DefaultListModel) this.getVarsList().getModel();
	listModel.removeAllElements();
	listModel = (DefaultListModel) this.getLocationsList().getModel();
	listModel.removeAllElements();
	listModel = (DefaultListModel) this.getStatementsList().getModel();
	listModel.removeAllElements();
	for (Enumeration e = sliceInterests.elements(); e.hasMoreElements();) {
		SliceInterest si = (SliceInterest) e.nextElement();
		if ((si instanceof SliceStatement) || (si instanceof SlicePoint)) {
			boolean isStatementNode;
			if (si instanceof SliceStatement)
				isStatementNode = true;
			else
				isStatementNode = false;
			SlicePoint sp = (SlicePoint) si;
			SootClass sootClass = sp.getSootClass();
			SootMethod sootMethod = sp.getSootMethod();
			Stmt stmt = sp.getStmt();
			Annotation locationAnnotation = null;
			try {
				locationAnnotation = CompilationManager.getAnnotationManager().getContainingAnnotation(sootClass, sootMethod, stmt);
			} catch (AnnotationException ae) {
				//throw new AnnotationException("In showCriterionInViewer(): Can not find containing annotation for statement " + stmt);
			}
			if (locationAnnotation != null) {
				if (!isStatementNode)
					addLocationToCriterion(si, locationAnnotation);
				else {
					//addStatementToCriterieon(si, null, locationAnnotation);
				}
			} else {
				addVarToCriterion(si);
			}
		}
	}
}
/**
 * Comment
 */
public void statementsList_ListSelectionEvents() {
	Object selectedValue = getStatementsList().getSelectedValue();
	if (selectedValue instanceof LocationNodeInCriterionViewer) {
		setSelectedByProgram = true;
		getLocationsList().clearSelection();
		getLocationVarsList().clearSelection();
		getVarsList().clearSelection();
		setSelectedByProgram = false;
		LocationNodeInCriterionViewer locationNode = (LocationNodeInCriterionViewer) selectedValue;
		calculateLocalsFor(locationNode);
		dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedValue);
	} else
		System.out.println("selected value in the location list should be LocationNodeInCriterionViewer");
	return;
}
/**
 * Comment
 */
public void statementsList_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int selIndex = getStatementsList().locationToIndex(mouseEvent.getPoint());
	if (selIndex != -1) {
		if (mouseEvent.getClickCount() == 1) {
			//mySingleClick(selRow, selPath);
			//System.out.println("Do nothing --- My sigle click on the list");
		} else
			if (mouseEvent.getClickCount() == 2) {
				//myDoubleClick(selRow, selPath);
				//System.out.println("My DOUBLE click on the list");
				LocationNodeInCriterionViewer selectedStmt = (LocationNodeInCriterionViewer) getStatementsList().getSelectedValue();
				if (selectedStmt == null)
					return;
				StmtTreeNode selectedNode = getStmtTreeNodeFromLocationNode(selectedStmt);
				dependFrame.addSelectedNodeToDependFrame(selectedNode);
				//System.out.println("End adding by double clicking");
			}
	}
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (6/13/2001 2:09:47 PM)
 * @return boolean
 * @param listModel javax.swing.DefaultListModel
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
private boolean statementsListContains(DefaultListModel listModel, Annotation annotation) {
	for (int i = 0; i < listModel.getSize(); i++) {
		Object element = listModel.elementAt(i);
		LocationNodeInCriterionViewer node = (LocationNodeInCriterionViewer) element;
		if (node.stmtAnnotation.equals(annotation))
			return true;
	}
	return false;
}
/**
 * Comment
 */
public void varsList_ListSelectionEvents() {
	Object selectedValue = getVarsList().getSelectedValue();
	if (selectedValue instanceof SliceVariable) {
		setSelectedByProgram = true;
		getStatementsList().clearSelection();
		getLocationsList().clearSelection();
		getLocationVarsList().clearSelection();
		setSelectedByProgram = false;
		if (selectedValue instanceof SliceField) {
			dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedValue);
		} else
			if (selectedValue instanceof SliceLocal) {
				dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedValue);
			}
	} else
		System.out.println("selected value in the variables list should be SliceVariable");
	return;
}
}
