package edu.ksu.cis.bandera.bui.counterexample;

import javax.swing.tree.*;
import javax.swing.*;

/**
 * The VariableWatchWindow provides a way to look at variables values during the
 * execution (term used loosely) of the counter-example.
 * Creation date: (12/6/01 1:40:07 PM)
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class VariableWatchWindow extends JFrame {
	private JButton ivjCloseButton = null;
	private JButton ivjIgnoreButton = null;
	private JPanel ivjJFrameContentPane = null;
	private JButton ivjWatchButton = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JScrollPane ivjJScrollPane1 = null;
	private JTable ivjScrollPaneTable = null;
	public ObjectTableModel objectTableModel;
	private TraceManager traceManager;
	private JButton ivjAdd = null;
	private JFrame ivjAddWatchWindow = null;
	private JButton ivjCancel = null;
	private JButton ivjDelete = null;
	private JPanel ivjJFrameContentPane1 = null;
	private JLabel ivjJLabel1 = null;
	private JLabel ivjJLabel2 = null;
	private JList ivjJList1 = null;
	private JScrollPane ivjJScrollPane2 = null;
	private JScrollPane ivjJScrollPane3 = null;
	private JButton ivjOK = null;
	private JTree ivjVariableTree = null;
	private CounterExample counterExample;
	private JButton ivjObjectDiagramDisplayButton = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == VariableWatchWindow.this.getCloseButton()) 
				connEtoM1(e);
			if (e.getSource() == VariableWatchWindow.this.getIgnoreButton()) 
				connEtoC1(e);
			if (e.getSource() == VariableWatchWindow.this.getWatchButton()) 
				connEtoM2(e);
			if (e.getSource() == VariableWatchWindow.this.getCancel()) 
				connEtoM3(e);
			if (e.getSource() == VariableWatchWindow.this.getOK()) 
				connEtoM4(e);
			if (e.getSource() == VariableWatchWindow.this.getAdd()) 
				connEtoC2(e);
			if (e.getSource() == VariableWatchWindow.this.getOK()) 
				connEtoC3(e);
			if (e.getSource() == VariableWatchWindow.this.getDelete()) 
				connEtoC4(e);
			if (e.getSource() == VariableWatchWindow.this.getCancel()) 
				connEtoC5(e);
			if (e.getSource() == VariableWatchWindow.this.getObjectDiagramDisplayButton()) 
				connEtoC6();
		};
	};
/**
 * VariableWatchWindow constructor comment.
 */
public VariableWatchWindow() {
	super();
	initialize();
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/01 2:17:45 PM)
 * @param traceManager edu.ksu.cis.bandera.bui.counterexample.TraceManager
 */
public VariableWatchWindow(TraceManager traceManager) {
	this("Watch Window", traceManager);
}
/**
 * VariableWatchWindow constructor comment.
 * @param title java.lang.String
 */
private VariableWatchWindow(String title) {
	this(title, null);
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/01 2:17:45 PM)
 * @param traceManager edu.ksu.cis.bandera.bui.counterexample.TraceManager
 */
private VariableWatchWindow(String title, TraceManager traceManager) {
	this(title, traceManager, null);
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/01 2:17:45 PM)
 * @param traceManager edu.ksu.cis.bandera.bui.counterexample.TraceManager
 */
public VariableWatchWindow(String title, TraceManager traceManager, CounterExample counterExample) {
	super(title);
	this.traceManager = traceManager;
	this.counterExample = counterExample;
	
	if(traceManager != null) {
		initializeObjectTableModel();
	}
	else {
		System.err.println("Creating a VariableWatchWindow without a TraceManager isn't a good idea.");
	}
	initialize();
}
/**
 * This method will handle the action performed signal for the add
 * variable button (>).  It will take the currently selected node
 * in the variable tree and add it to the variables list.
 */
public void add_ActionPerformed() {

    try {
        // get the JList model
        JList list = getJList1();
        if (list == null) {
            System.err.println("Cannot get the list.  Fatal Error, quitting.");
            return; // not a good design decision! -todd
        }
        ListModel listModel = list.getModel();
        if(listModel instanceof DefaultListModel) {
			DefaultListModel defaultListModel = (DefaultListModel)listModel;
 	       if (listModel == null) {
  	          System.err.println("Cannot get the list model.  Fatal Error, quitting.");
   	       }
 	       else {
	 	       
		        // get the Tree model
 		       DefaultMutableTreeNode node = (DefaultMutableTreeNode) (getVariableTree().getLastSelectedPathComponent());

		        // get the current selected node in the tree model
 		       Object variable = node.getUserObject();
  		       System.out.println("variable = " + variable.toString());

		        // add that node to the list model
 		       defaultListModel.addElement(variable);
 	       }
        }
        else {
	        System.err.println("List model is not a DefaultListModel.");
        }

    }
    catch (Exception e) {
        System.err.println("Exception caught while handling the add variable event.");
        e.printStackTrace(System.err);
    }
}
/**
 * Comment
 */
public void cancel_ActionPerformed() {
	try {
        JList list = getJList1();
        if (list == null) {
            System.err.println("Cannot get the list.  Fatal Error, quitting.");
            return;
        }

        ListModel listModel = list.getModel();
        if(listModel instanceof DefaultListModel) {
	        DefaultListModel defaultListModel = (DefaultListModel) listModel;
        
        	if (defaultListModel == null) {
         	   System.err.println("Cannot get the list model.  Fatal Error, quitting.");
        	}
        	else {	
	        	defaultListModel.clear();
        	}
        }
        else {
	        System.err.println("The listModel is not a DefaultListModel.  We cannot clear it.");
        }
	}
	catch(Exception e) {
		System.err.println("Exception caught while closing the add watch window.");
		e.printStackTrace(System.err);
	}	
}
/**
 * connEtoC1:  (IgnoreButton.action.actionPerformed(java.awt.event.ActionEvent) --> VariableWatchWindow.ignoreButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.ignoreButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (Add.action.actionPerformed(java.awt.event.ActionEvent) --> VariableWatchWindow.add_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.add_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (OK.action.actionPerformed(java.awt.event.ActionEvent) --> VariableWatchWindow.oK_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.oK_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (Delete.action.actionPerformed(java.awt.event.ActionEvent) --> VariableWatchWindow.delete_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.delete_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (Cancel.action.actionPerformed(java.awt.event.ActionEvent) --> VariableWatchWindow.cancel_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cancel_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (ObjectDiagramDisplayButton.action. --> VariableWatchWindow.objectDiagramDisplayButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6() {
	try {
		// user code begin {1}
		// user code end
		this.objectDiagramDisplayButton_ActionPerformed(null);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (CloseButton.action.actionPerformed(java.awt.event.ActionEvent) --> VariableWatchWindow.setVisible(Z)V)
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
 * connEtoM2:  (WatchButton.action.actionPerformed(java.awt.event.ActionEvent) --> AddWatchWindow.show()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddWatchWindow().show();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM3:  (Cancel.action.actionPerformed(java.awt.event.ActionEvent) --> AddWatchWindow.dispose()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddWatchWindow().dispose();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM4:  (OK.action.actionPerformed(java.awt.event.ActionEvent) --> AddWatchWindow.dispose()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getAddWatchWindow().dispose();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * This method will handle the delete variable button (<).  It will take
 * the selected index and remove that object from the list.
 */
public void delete_ActionPerformed() {

    try {
        JList list = getJList1();
        if (list == null) {
            System.err.println("Cannot get the list.  Fatal Error, quitting.");
            return;
        }

        DefaultListModel listModel = (DefaultListModel) list.getModel();
        if (listModel == null) {
            System.err.println("Cannot get the list model.  Fatal Error, quitting.");
            return;
        }

        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex >= 0) {
            System.out.println("Removing item " + selectedIndex + " from list.");
            listModel.remove(selectedIndex);
        }
        else {
            System.out.println("Invalid index to remove from the list.");
        }
    }
    catch (Exception e) {
        System.err.println("Exception while deleting a variable from the list.");
        e.printStackTrace(System.err);
    }
}
/**
 * Return the Add property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getAdd() {
	if (ivjAdd == null) {
		try {
			ivjAdd = new javax.swing.JButton();
			ivjAdd.setName("Add");
			ivjAdd.setText(">");
			ivjAdd.setBackground(new java.awt.Color(204,204,255));
			ivjAdd.setBounds(314, 114, 85, 25);
			ivjAdd.setActionCommand("Add");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAdd;
}
/**
 * Return the AddWatchWindow property value.
 * @return javax.swing.JFrame
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JFrame getAddWatchWindow() {
	if (ivjAddWatchWindow == null) {
		try {
			ivjAddWatchWindow = new javax.swing.JFrame();
			ivjAddWatchWindow.setName("AddWatchWindow");
			ivjAddWatchWindow.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			ivjAddWatchWindow.setBounds(30, 426, 755, 323);
			ivjAddWatchWindow.setTitle("Add Watch Window");
			getAddWatchWindow().setContentPane(getJFrameContentPane1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddWatchWindow;
}
/**
 * Return the Cancel property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCancel() {
	if (ivjCancel == null) {
		try {
			ivjCancel = new javax.swing.JButton();
			ivjCancel.setName("Cancel");
			ivjCancel.setText("Cancel");
			ivjCancel.setBackground(new java.awt.Color(204,204,255));
			ivjCancel.setBounds(313, 246, 85, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCancel;
}
/**
 * Return the CloseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCloseButton() {
	if (ivjCloseButton == null) {
		try {
			ivjCloseButton = new javax.swing.JButton();
			ivjCloseButton.setName("CloseButton");
			ivjCloseButton.setText("Close");
			ivjCloseButton.setBackground(new java.awt.Color(204,204,255));
			ivjCloseButton.setBounds(652, 206, 188, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCloseButton;
}
/**
 * Return the Delete property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getDelete() {
	if (ivjDelete == null) {
		try {
			ivjDelete = new javax.swing.JButton();
			ivjDelete.setName("Delete");
			ivjDelete.setText("<");
			ivjDelete.setBackground(new java.awt.Color(204,204,255));
			ivjDelete.setBounds(313, 143, 85, 25);
			ivjDelete.setActionCommand("Delete");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDelete;
}
/**
 * Return the IgnoreButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getIgnoreButton() {
	if (ivjIgnoreButton == null) {
		try {
			ivjIgnoreButton = new javax.swing.JButton();
			ivjIgnoreButton.setName("IgnoreButton");
			ivjIgnoreButton.setText("Ignore");
			ivjIgnoreButton.setBackground(new java.awt.Color(204,204,255));
			ivjIgnoreButton.setBounds(442, 206, 188, 25);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjIgnoreButton;
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
			ivjJFrameContentPane.setLayout(null);
			ivjJFrameContentPane.setBackground(new java.awt.Color(204,204,255));
			getJFrameContentPane().add(getWatchButton(), getWatchButton().getName());
			getJFrameContentPane().add(getIgnoreButton(), getIgnoreButton().getName());
			getJFrameContentPane().add(getCloseButton(), getCloseButton().getName());
			getJFrameContentPane().add(getJScrollPane1(), getJScrollPane1().getName());
			getJFrameContentPane().add(getObjectDiagramDisplayButton(), getObjectDiagramDisplayButton().getName());
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
 * Return the JFrameContentPane1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJFrameContentPane1() {
	if (ivjJFrameContentPane1 == null) {
		try {
			ivjJFrameContentPane1 = new javax.swing.JPanel();
			ivjJFrameContentPane1.setName("JFrameContentPane1");
			ivjJFrameContentPane1.setLayout(null);
			ivjJFrameContentPane1.setBackground(new java.awt.Color(204,204,255));
			getJFrameContentPane1().add(getJScrollPane2(), getJScrollPane2().getName());
			getJFrameContentPane1().add(getAdd(), getAdd().getName());
			getJFrameContentPane1().add(getDelete(), getDelete().getName());
			getJFrameContentPane1().add(getOK(), getOK().getName());
			getJFrameContentPane1().add(getCancel(), getCancel().getName());
			getJFrameContentPane1().add(getJScrollPane3(), getJScrollPane3().getName());
			getJFrameContentPane1().add(getJLabel1(), getJLabel1().getName());
			getJFrameContentPane1().add(getJLabel2(), getJLabel2().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJFrameContentPane1;
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
			ivjJLabel1.setText("Available Variables");
			ivjJLabel1.setBounds(10, 7, 155, 14);
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
			ivjJLabel2.setText("Variables to Add ...");
			ivjJLabel2.setBounds(426, 11, 306, 14);
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
 * Return the JList1 property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getJList1() {
	if (ivjJList1 == null) {
		try {
			ivjJList1 = new javax.swing.JList();
			ivjJList1.setName("JList1");
			ivjJList1.setModel(new javax.swing.DefaultListModel());
			ivjJList1.setBounds(0, 0, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJList1;
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
			ivjJScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			ivjJScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			ivjJScrollPane1.setBounds(16, 15, 840, 183);
			getJScrollPane1().setViewportView(getScrollPaneTable());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane1;
}
/**
 * Return the JScrollPane2 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane2() {
	if (ivjJScrollPane2 == null) {
		try {
			ivjJScrollPane2 = new javax.swing.JScrollPane();
			ivjJScrollPane2.setName("JScrollPane2");
			ivjJScrollPane2.setBounds(7, 29, 288, 247);
			getJScrollPane2().setViewportView(getVariableTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane2;
}
/**
 * Return the JScrollPane3 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane3() {
	if (ivjJScrollPane3 == null) {
		try {
			ivjJScrollPane3 = new javax.swing.JScrollPane();
			ivjJScrollPane3.setName("JScrollPane3");
			ivjJScrollPane3.setBounds(425, 31, 318, 243);
			getJScrollPane3().setViewportView(getJList1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane3;
}
/**
 * Return the ObjectDiagramDisplayButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getObjectDiagramDisplayButton() {
	if (ivjObjectDiagramDisplayButton == null) {
		try {
			ivjObjectDiagramDisplayButton = new javax.swing.JButton();
			ivjObjectDiagramDisplayButton.setName("ObjectDiagramDisplayButton");
			ivjObjectDiagramDisplayButton.setToolTipText("Show the Object Diagram for the selected row");
			ivjObjectDiagramDisplayButton.setText("Show Object Diagram");
			ivjObjectDiagramDisplayButton.setBackground(new java.awt.Color(204,204,255));
			ivjObjectDiagramDisplayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ObjectDiagramNormal1.GIF")));
			ivjObjectDiagramDisplayButton.setBounds(22, 206, 188, 25);
			ivjObjectDiagramDisplayButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ObjectDiagramPressed1.GIF")));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjObjectDiagramDisplayButton;
}
/**
 * Get the table model defined for this method.
 * Creation date: (12/7/01 1:00:12 PM)
 *
 * @return edu.ksu.cis.bandera.bui.counterexample.ObjectTableModel
 */
private ObjectTableModel getObjectTableModel() {
	return(objectTableModel);
}
/**
 * Return the OK property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOK() {
	if (ivjOK == null) {
		try {
			ivjOK = new javax.swing.JButton();
			ivjOK.setName("OK");
			ivjOK.setText("OK");
			ivjOK.setBackground(new java.awt.Color(204,204,255));
			ivjOK.setBounds(314, 218, 85, 25);
			ivjOK.setActionCommand("OK");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOK;
}
/**
 * Return the ScrollPaneTable property value.
 * @return javax.swing.JTable
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTable getScrollPaneTable() {
	if (ivjScrollPaneTable == null) {
		try {
			ivjScrollPaneTable = new javax.swing.JTable();
			ivjScrollPaneTable.setName("ScrollPaneTable");
			getJScrollPane1().setColumnHeaderView(ivjScrollPaneTable.getTableHeader());
			getJScrollPane1().getViewport().setBackingStoreEnabled(true);
			ivjScrollPaneTable.setModel(getObjectTableModel());
			ivjScrollPaneTable.setBounds(0, 0, 200, 200);
			// user code begin {1}
			ivjScrollPaneTable.getColumnModel().getColumn(1).setCellRenderer(new ObjectTableCellRenderer());
			//ivjScrollPaneTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonTableCellRenderer());
			ivjScrollPaneTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonCellRenderer(counterExample));
			//ivjScrollPaneTable.getColumnModel().getColumn(2).setCellEditor(null);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjScrollPaneTable;
}
/**
 * Return the VariableTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTree getVariableTree() {
	if (ivjVariableTree == null) {
		try {
			ivjVariableTree = new javax.swing.JTree();
			ivjVariableTree.setName("VariableTree");
			ivjVariableTree.setModel(traceManager.getVariableTreeModel());
			ivjVariableTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVariableTree;
}
/**
 * Return the WatchButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getWatchButton() {
	if (ivjWatchButton == null) {
		try {
			ivjWatchButton = new javax.swing.JButton();
			ivjWatchButton.setName("WatchButton");
			ivjWatchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchNormal1.gif")));
			ivjWatchButton.setText("Watch");
			ivjWatchButton.setBackground(new java.awt.Color(204,204,255));
			ivjWatchButton.setBounds(232, 206, 188, 25);
			ivjWatchButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchPressed1.gif")));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWatchButton;
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
 * Insert the method's description here.
 * Creation date: (12/8/01 10:41:08 AM)
 * @param row int
 */
public void ignore(int row) {
	objectTableModel.ignore(row);
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/01 3:41:03 PM)
 * @param objectToIgnore java.lang.Object
 */
void ignore(Object objectToIgnore) {
	objectTableModel.ignore(objectToIgnore);
}
/**
 * Ignore the current row selected in the table.
 */
public void ignoreButton_ActionPerformed() {
	int rowSelected = getScrollPaneTable().getSelectedRow();
	
	if(rowSelected >= 0) {
		System.out.println("ignoring row " + rowSelected);
		ignore(rowSelected);
	}
	else {
		System.out.println("invalid row ... nothing will happen.");
	}
	update();
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getCloseButton().addActionListener(ivjEventHandler);
	getIgnoreButton().addActionListener(ivjEventHandler);
	getWatchButton().addActionListener(ivjEventHandler);
	getCancel().addActionListener(ivjEventHandler);
	getOK().addActionListener(ivjEventHandler);
	getAdd().addActionListener(ivjEventHandler);
	getDelete().addActionListener(ivjEventHandler);
	getObjectDiagramDisplayButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("VariableWatchWindow");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(866, 263);
		setContentPane(getJFrameContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * Create and initialize the object table model.
 * Creation date: (12/8/01 2:17:00 PM)
 */
private void initializeObjectTableModel() {
	objectTableModel = new ObjectTableModel(traceManager);
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	// Insert code to start the application here.
	VariableWatchWindow vww = new VariableWatchWindow((TraceManager)null);
	vww.show();
}
/**
 * Comment
 */
public void objectDiagramDisplayButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

	// get the row number
	int row = getScrollPaneTable().getSelectedRow();

	// check to make sure it is a valid row
	if(row < 0) {
		return;
	}

	// get the value node at that row
	Object value = getObjectTableModel().getValueAt(row, 2);

	// tell the counter example window to display the object diagram for this ValueNode
	if((value != null) && (value instanceof ValueNode)) {
		ValueNode valueNode = (ValueNode)value;
		counterExample.showObjectDiagram(valueNode);
	}
	
}
/**
 * This method will handle the ok buttons actions.  This will walk thru
 * the list of objects that are in the list and add them to the
 * object table model's watched list.  It will then close the window.
 */
public void oK_ActionPerformed() {

	try {
        JList list = getJList1();
        if (list == null) {
            System.err.println("Cannot get the list.  Fatal Error, quitting.");
            return;
        }

        DefaultListModel listModel = (DefaultListModel) list.getModel();
        if (listModel == null) {
            System.err.println("Cannot get the list model.  Fatal Error, quitting.");
            return;
        }

		int elementCount = listModel.size();
		System.out.println("elementCount = " + elementCount);
		for(int i = 0; i < elementCount; i++) {
			Object element = listModel.getElementAt(i);
			System.out.println("element = " + element.toString());
			watch(element);
		}
		update();
		listModel.clear();
	}
	catch(Exception e) {
		System.err.println("Exception while adding the list to the watched items.");
		e.printStackTrace(System.err);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/01 2:20:20 PM)
 */
public void update() {
	objectTableModel.update();
	// update the UI
	getScrollPaneTable().updateUI();
}
/**
 * Insert the method's description here.
 * Creation date: (12/7/01 3:39:53 PM)
 * @param objectToWatch java.lang.Object
 */
public void watch(Object objectToWatch) {
	objectTableModel.watch(objectToWatch);
	update();
}
}
