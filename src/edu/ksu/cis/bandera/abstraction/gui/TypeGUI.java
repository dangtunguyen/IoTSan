package edu.ksu.cis.bandera.abstraction.gui;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
 * All rights reserved.                                              *
 *                                                                   *
 * Modifications by Robby (robby@cis.ksu.edu) are                    *
 * Copyright (C) 2000 Robby.  All rights reserved.                   *
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

import javax.swing.*;

import javax.swing.event.*;

import edu.ksu.cis.bandera.bui.*;

import javax.swing.tree.TreeNode;

import javax.swing.filechooser.FileFilter;

import edu.ksu.cis.bandera.abstraction.*;

import edu.ksu.cis.bandera.abstraction.options.*;

import edu.ksu.cis.bandera.abstraction.typeinference.*;

import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;
import edu.ksu.cis.bandera.sessions.Abstraction;
import edu.ksu.cis.bandera.sessions.AbstractionConverter;

import ca.mcgill.sable.soot.SootClass;

import org.apache.log4j.Category;

/**
 * The TypeGUI provides an easy way to configure abstraction for an application.
 *
 * @author Shawn Laubach &lt;laubach@acm.org&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:32:49 $
 */
public class TypeGUI extends JFrame {

    /**
     * The log that will be used to write messages to.
     */
    private static final Category log = Category.getInstance(TypeGUI.class.getName());

	static TypeGUI typeGUI;
	private JScrollPane ivjJScrollPane1 = null;
	protected java.util.Vector classes;
	private JToolBar ivjJToolBar1 = null;
	private JPanel ivjJFrameContentPane = null;
	protected TypeTree tree;
	protected JTreeTable treeTable;
	protected java.util.Vector methods;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	protected java.io.File file;
	private JButton ivjTypeCheckButton = null;
	private JButton ivjSaveOptionsButton = null;
	private JFileChooser chooser;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == TypeGUI.this.getTypeCheckButton()) 
				connEtoC1(e);
			if (e.getSource() == TypeGUI.this.getSaveOptionsButton()) 
				connEtoC2(e);
		};
	};
/**
 * TypeGUI constructor comment.
 */
public TypeGUI() {
	super();
	initialize();
}
/**
 * TypeGUI constructor comment.
 */
public TypeGUI(Vector v) {
	super();
	this.classes = v;
	initialize();
}
/**
 * connEtoC1:  (JToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> TypeGUI.typeCheck()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.typeCheck();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
		ivjExc.printStackTrace();
	}
}
/**
 * connEtoC2:  (JToolBarButton2.action.actionPerformed(java.awt.event.ActionEvent) --> TypeGUI.jToolBarButton2_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saveOptions();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * 
 */
public void expandTree() {
	JTree tree = treeTable.getTree();
	for (int i = 0; i < tree.getRowCount(); i++) {
		tree.expandRow(i);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (5/25/00 3:10:49 PM)
 * @return java.io.File
 */
public java.io.File getFile() {
	return file;
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
			ivjJFrameContentPane.setBackground(new java.awt.Color(255,255,255));
			ivjJFrameContentPane.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
			ivjJFrameContentPane.setMinimumSize(new java.awt.Dimension(277, 145));
			ivjJFrameContentPane.setBounds(0, 0, 0, 0);

			java.awt.GridBagConstraints constraintsJToolBar1 = new java.awt.GridBagConstraints();
			constraintsJToolBar1.gridx = 0; constraintsJToolBar1.gridy = 0;
			constraintsJToolBar1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJToolBar1.weightx = 1.0;
			constraintsJToolBar1.ipadx = 227;
			getJFrameContentPane().add(getJToolBar1(), constraintsJToolBar1);

			java.awt.GridBagConstraints constraintsJScrollPane1 = new java.awt.GridBagConstraints();
			constraintsJScrollPane1.gridx = 0; constraintsJScrollPane1.gridy = 1;
			constraintsJScrollPane1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJScrollPane1.weightx = 1.0;
			constraintsJScrollPane1.weighty = 1.0;
			constraintsJScrollPane1.ipadx = 138;
			constraintsJScrollPane1.ipady = 98;
			getJFrameContentPane().add(getJScrollPane1(), constraintsJScrollPane1);
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
 * Return the JScrollPane1 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane1() {
	if (ivjJScrollPane1 == null) {
		try {
			ivjJScrollPane1 = new javax.swing.JScrollPane();
			ivjJScrollPane1.setName("JScrollPane1");
			ivjJScrollPane1.setOpaque(true);
			// user code begin {1}
			getJScrollPane1().setViewportView(treeTable = new JTreeTable(tree = new TypeTree()));
			//t.setBackground(getJPanel1().getBackground());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			ivjExc.printStackTrace();
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane1;
}
/**
 * Return the JToolBar1 property value.
 * @return javax.swing.JToolBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JToolBar getJToolBar1() {
	if (ivjJToolBar1 == null) {
		try {
			ivjJToolBar1 = new javax.swing.JToolBar();
			ivjJToolBar1.setName("JToolBar1");
			ivjJToolBar1.setFloatable(false);
			ivjJToolBar1.setAlignmentY(0.5F);
			ivjJToolBar1.setMaximumSize(new java.awt.Dimension(1000, 30));
			ivjJToolBar1.setMinimumSize(new java.awt.Dimension(50, 30));
			ivjJToolBar1.add(getTypeCheckButton());
			ivjJToolBar1.add(getSaveOptionsButton());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJToolBar1;
}
/**
 * Insert the method's description here.
 * Creation date: (5/3/00 4:35:18 PM)
 * @return java.util.Hashtable
 */
public Hashtable getOptions() {
	return tree.getOptions();
}
/**
 * Return the JToolBarButton2 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSaveOptionsButton() {
	if (ivjSaveOptionsButton == null) {
		try {
			ivjSaveOptionsButton = new javax.swing.JButton();
			ivjSaveOptionsButton.setName("SaveOptionsButton");
			ivjSaveOptionsButton.setToolTipText("Saves the typing options.");
			ivjSaveOptionsButton.setAlignmentY(0.5F);
			ivjSaveOptionsButton.setText("");
			ivjSaveOptionsButton.setMaximumSize(new java.awt.Dimension(30, 30));
			ivjSaveOptionsButton.setHorizontalTextPosition(0);
			ivjSaveOptionsButton.setActionCommand("SaveOptionsButton");
			ivjSaveOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/SaveAs24.gif")));
			ivjSaveOptionsButton.setAlignmentX(0.5F);
			ivjSaveOptionsButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjSaveOptionsButton.setMinimumSize(new java.awt.Dimension(20, 20));
			ivjSaveOptionsButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/SaveAs24.gif")));
			ivjSaveOptionsButton.setBorder(new javax.swing.border.CompoundBorder());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveOptionsButton;
}
/**
 * Return the JToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getTypeCheckButton() {
	if (ivjTypeCheckButton == null) {
		try {
			ivjTypeCheckButton = new javax.swing.JButton();
			ivjTypeCheckButton.setName("TypeCheckButton");
			ivjTypeCheckButton.setToolTipText("Type check.");
			ivjTypeCheckButton.setAlignmentY(0.5F);
			ivjTypeCheckButton.setText("");
			ivjTypeCheckButton.setMaximumSize(new java.awt.Dimension(30, 30));
			ivjTypeCheckButton.setHorizontalTextPosition(0);
			ivjTypeCheckButton.setActionCommand("JToolBarButton1");
			ivjTypeCheckButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Run24.gif")));
			ivjTypeCheckButton.setAlignmentX(0.5F);
			ivjTypeCheckButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjTypeCheckButton.setMinimumSize(new java.awt.Dimension(20, 20));
			ivjTypeCheckButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/Run24.gif")));
			ivjTypeCheckButton.setBorder(new javax.swing.border.CompoundBorder());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTypeCheckButton;
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
	getTypeCheckButton().addActionListener(ivjEventHandler);
	getSaveOptionsButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		typeGUI = this;
		// user code end
		setName("TypeGUI");
		setSize(426, 240);
		setContentPane(getJFrameContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Abstract Type Inference<Untitled>");
	chooser = new JFileChooser();
	// Note: source for ExtensionFileFilter can be found in the SwingSet demo 
	chooser.setFileFilter(FileChooser.ABSTRACTION);
	chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
	// user code end
}
/**
 * 
 */
public void reset() {
	setTitle("Abstract Type Inference<Untitled>");
	if (classes != null) {
		tree.setRoot(TypeTree.createTree(classes));
		tree.nodeStructureChanged((TreeNode) tree.getRoot());
	}
}
/**
 * Save the configured abstraction to the Session manager.  We will first need to convert the data
 * stored in the JTreeTable model into Class, Method, and Field descriptions and add them to a new
 * Abstraction object.  Once this is complete, we can get the active session and set the abstraction
 * for that session to the generated Abstraction.
 */
public void saveOptions() {

    SessionManager sessionManager = SessionManager.getInstance();
    Session activeSession = sessionManager.getActiveSession();
    if(activeSession == null) {
	log.error("There was no active session in which to write this abstraction information.");
	return;
    }

    AbstractionConverter ac = new AbstractionConverter();
    Abstraction abstraction = ac.convert(tree.getOptions());
    activeSession.setAbstraction(abstraction);

}

/**
 * Insert the method's description here.
 * Creation date: (5/3/00 4:29:31 PM)
 * @param v java.util.Vector
 */
public void setClasses(Vector v) {
	//Vector c;
	this.classes = v;
	reset();
}
/**
 * Insert the method's description here.
 * Creation date: (5/25/00 3:10:49 PM)
 * @param newFile java.io.File
 */
public void setFile(java.io.File newFile) {
	file = newFile;
}
/**
 * 
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 */
public void setInferredTypes(TypeTable typeTable) {
	expandTree();
	tree.setInferedTypes(typeTable);
}
/**
 * 
 * @param options java.util.Hashtable
 */
public void setSelectedTypes(Hashtable options, String filename) {
	setTitle("Abstract Type Inference<" + new File(filename).getName() + ">");
	expandTree();
	tree.setSelectedTypes(options);
}
/**
 * Comment
 */
public void typeCheck() {
	try {
		TypeTable table;
		//System.out.println(tree.getOptions());
		//System.out.println("Pretty:\n" + tree.getOptions().prettyPrint());
		TypeInference ti = new TypeInference();
		table = ti.type(((SootClass) classes.get(0)).getManager(), getOptions());
		tree.setInferedTypes(table);
		if (ti.getErrors().size() != 0) {
			Hashtable errorTable = ti.getErrors();
			Iterator i = errorTable.keySet().iterator();
			while (i.hasNext()) {
				Object o = i.next();
				JOptionPane.showMessageDialog(this, errorTable.get(o), o.toString(), JOptionPane.ERROR_MESSAGE);
				System.err.println(o + ":  " + errorTable.get(o));
			}
		} else {
			JOptionPane.showMessageDialog(this, "OK", "Typing", JOptionPane.INFORMATION_MESSAGE);
		}

		//System.out.println(TypeInference.getErrors());
	} catch (Exception e) {
		e.printStackTrace();
	}
	return;
}

    /**
     * Use this abstraction as the initial configuration of this view.
     *
     * @param Abstraction abstraction The abstraction information to use initially.
     */
    public void setAbstraction(Abstraction abstraction) {

	AbstractionConverter ac = new AbstractionConverter();
	Hashtable table = ac.convert(abstraction);

	tree.setSelectedTypes(table);
    }
}
