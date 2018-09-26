package edu.ksu.cis.bandera.bui.counterexample;

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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.net.URL;
import javax.swing.*;
import javax.swing.tree.*;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.bui.*;
import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.*;
import edu.ksu.cis.bandera.bui.dialog.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.util.*;
import edu.ksu.cis.bandera.annotation.*;
//import gov.nasa.arc.ase.jpf.jvm.examine.*;
import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;


/**
 * The CounterExample class provides a GUI for viewing a counter example
 * of a verification run.  This GUI allows the user to view the states
 * that lead up to the failure of property.  It provides many tools to
 * assist the user the user in determining the exact nature of the
 * failure (or root cause).  For example, it will allow the user to step
 * forwards and backwards through the counter example states.  It will also
 * allow the user to set variables to watch as they step through the
 * states.
 *
 * @author Robby <robby@cis.ksu.edu>;
 * @author Todd Wallentine <tcw@cis.ksu.edu>;
 * @author Yu Chen <yuchen@cis.ksu.edu>;
 * @version $Revision: 1.11 $ - $Date: 2003/06/13 19:00:14 $
 */
public class CounterExample extends JFrame {
	private AnnotationManager am = CompilationManager.getAnnotationManager();
	TraceManager traceManager;
	private Vector threadCounterExamples = new Vector();
	private JButton ivjBackButton = null;
	private JButton ivjCloseButton = null;
	private JPanel ivjCounterExampleDialogContentPane = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JButton ivjForwardButton = null;
	private JButton ivjResetButton = null;
	private JPanel ivjButtonPanel = null;
	private JTextArea ivjValueTextArea = null;
	private JScrollPane ivjVariableScrollPane = null;
	private JTree ivjVariableTree = null;
	private JLabel ivjStepLabel = null;
	private int step = 0;
	private int stepLength;
	private JLabel ivjSteppingCaptionLabel = null;
	private JLabel ivjSteppingLabel = null;
	private CounterExampleOptions options = null;
	private JButton ivjOptionsButton = null;
	private JSlider ivjStepSelectionSlider = null;
	private JButton ivjBackRunToolBarButton = null;
	private JButton ivjBackStepToolBarButton = null;
	private JButton ivjForwardRunToolBarButton = null;
	private JButton ivjForwardStepToolBarButton = null;
	private JButton ivjResetToolBarButton = null;
	private JButton ivjOptionsToolBarButton = null;
	private JButton ivjWatchToolBarButton = null;
	private JMenuItem ivjAddConditionalBreakpointMenuItem = null;
	private JPopupMenu ivjVariableSelectionPopupMenu = null;
	private VariableWatchWindow variableWatchWindow;
	private JButton ivjWatchButton = null;
	private JSeparator ivjJSeparator1 = null;
	private JMenuItem ivjWatchThisVariableMenuItem = null;
	private JMenuItem ivjShowLockVisualizationMenuItem = null;
	private BipartiteGrapher bipartiteGrapher;
	private java.util.ArrayList rootedGraphers;
	private JButton ivjCompleteGraphToolBarButton = null;
	private CompleteBipartiteGrapher completeBipartiteGrapher;
	private static Category log = Category.getInstance(CounterExample.class.getName());
	private JMenuItem ivjObjectDiagram = null;
	private java.util.ArrayList objectDiagramManagers;
	private JPanel ivjJFrameContentPane = null;
	private JFrame ivjMinimizedCounterExampleFrame = null;
	private JButton ivjShowMinimizedWindowButton = null;
	private JPanel ivjSliderPanel = null;
	private JPanel ivjSteppingButtonPanel = null;
	private JPanel ivjTextAndButtonPanel = null;
	private JPanel ivjWindowButtonPanel = null;
	private JButton ivjShowMaximizedWindowButton = null;
	private JButton ivjBackRunToolBarButton1 = null;
	private JButton ivjBackStepToolBarButton1 = null;
	private JButton ivjCompleteGraphToolBarButton1 = null;
	private JButton ivjForwardRunToolBarButton1 = null;
	private JButton ivjForwardStepToolBarButton1 = null;
	private JPanel ivjJPanel = null;
	private JButton ivjOptionsToolBarButton1 = null;
	private JButton ivjResetToolBarButton1 = null;
	private JPanel ivjSliderPanel1 = null;
	private JLabel ivjStepLabel1 = null;
	private JPanel ivjSteppingButtonPanel1 = null;
	private JLabel ivjSteppingCaptionLabel1 = null;
	private JLabel ivjSteppingLabel1 = null;
	private JSlider ivjStepSelectionSlider1 = null;
	private JButton ivjWatchToolBarButton1 = null;
	private BorderLayout ivjTextAndButtonPanelBorderLayout = null;
    private Map threadCounterExampleMap;
	private JButton ivjSaveButton = null;
	private JButton ivjSaveButton1 = null;
	private JLabel ivjViolationHintsLabel = null;
	private JTextArea ivjViolationHintsTextArea = null;
	private JPanel ivjViolationHintsPanel = null;
	private JScrollPane ivjViolationHintsScrollPane = null;
	private JLabel ivjViolationNameLabel = null;
	private JPanel ivjViolationNamePanel = null;
	private JTextField ivjViolationNameTextField = null;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.MouseListener, java.awt.event.WindowListener, javax.swing.event.ChangeListener, javax.swing.event.TreeSelectionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == CounterExample.this.getResetButton()) 
				connEtoC1();
			if (e.getSource() == CounterExample.this.getForwardButton()) 
				connEtoC2();
			if (e.getSource() == CounterExample.this.getBackButton()) 
				connEtoC3();
			if (e.getSource() == CounterExample.this.getCloseButton()) 
				connEtoC4();
			if (e.getSource() == CounterExample.this.getOptionsButton()) 
				connEtoC6(e);
			if (e.getSource() == CounterExample.this.getResetToolBarButton()) 
				connEtoC9(e);
			if (e.getSource() == CounterExample.this.getBackRunToolBarButton()) 
				connEtoC10(e);
			if (e.getSource() == CounterExample.this.getBackStepToolBarButton()) 
				connEtoC11(e);
			if (e.getSource() == CounterExample.this.getForwardStepToolBarButton()) 
				connEtoC12(e);
			if (e.getSource() == CounterExample.this.getForwardRunToolBarButton()) 
				connEtoC13(e);
			if (e.getSource() == CounterExample.this.getOptionsToolBarButton()) 
				connEtoC14(e);
			if (e.getSource() == CounterExample.this.getAddConditionalBreakpointMenuItem()) 
				connEtoC16();
			if (e.getSource() == CounterExample.this.getWatchButton()) 
				connEtoC17(e);
			if (e.getSource() == CounterExample.this.getWatchToolBarButton()) 
				connEtoC18(e);
			if (e.getSource() == CounterExample.this.getWatchThisVariableMenuItem()) 
				connEtoC19(e);
			if (e.getSource() == CounterExample.this.getShowLockVisualizationMenuItem()) 
				connEtoC20(e);
			if (e.getSource() == CounterExample.this.getCompleteGraphToolBarButton()) 
				connEtoC23(e);
			if (e.getSource() == CounterExample.this.getObjectDiagram()) 
				connEtoC24(e);
			if (e.getSource() == CounterExample.this.getShowMinimizedWindowButton()) 
				connEtoM1(e);
			if (e.getSource() == CounterExample.this.getShowMinimizedWindowButton()) 
				connEtoM2(e);
			if (e.getSource() == CounterExample.this.getShowMaximizedWindowButton()) 
				connEtoM3(e);
			if (e.getSource() == CounterExample.this.getShowMaximizedWindowButton()) 
				connEtoM4(e);
			if (e.getSource() == CounterExample.this.getBackRunToolBarButton1()) 
				connEtoC26(e);
			if (e.getSource() == CounterExample.this.getBackStepToolBarButton1()) 
				connEtoC27(e);
			if (e.getSource() == CounterExample.this.getResetToolBarButton1()) 
				connEtoC28(e);
			if (e.getSource() == CounterExample.this.getForwardStepToolBarButton1()) 
				connEtoC29(e);
			if (e.getSource() == CounterExample.this.getForwardRunToolBarButton1()) 
				connEtoC30(e);
			if (e.getSource() == CounterExample.this.getCompleteGraphToolBarButton1()) 
				connEtoC32(e);
			if (e.getSource() == CounterExample.this.getOptionsToolBarButton1()) 
				connEtoC33(e);
			if (e.getSource() == CounterExample.this.getWatchToolBarButton1()) 
				connEtoC34(e);
			if (e.getSource() == CounterExample.this.getSaveButton()) 
				connEtoC37(e);
			if (e.getSource() == CounterExample.this.getSaveButton1()) 
				connEtoC38(e);
		};
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == CounterExample.this.getVariableTree()) 
				connEtoC21(e);
		};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mousePressed(java.awt.event.MouseEvent e) {
			if (e.getSource() == CounterExample.this.getVariableTree()) 
				connEtoC22(e);
		};
		public void mouseReleased(java.awt.event.MouseEvent e) {
			if (e.getSource() == CounterExample.this.getStepSelectionSlider()) 
				connEtoC7(e);
			if (e.getSource() == CounterExample.this.getVariableTree()) 
				connEtoC15(e);
			if (e.getSource() == CounterExample.this.getStepSelectionSlider1()) 
				connEtoC35(e);
		};
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			if (e.getSource() == CounterExample.this.getStepSelectionSlider()) 
				connEtoC8(e);
			if (e.getSource() == CounterExample.this.getStepSelectionSlider1()) 
				connEtoC31(e);
		};
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			if (e.getSource() == CounterExample.this.getVariableTree()) 
				connEtoC5();
		};
		public void windowActivated(java.awt.event.WindowEvent e) {};
		public void windowClosed(java.awt.event.WindowEvent e) {};
		public void windowClosing(java.awt.event.WindowEvent e) {
			if (e.getSource() == CounterExample.this) 
				connEtoC25(e);
			if (e.getSource() == CounterExample.this.getMinimizedCounterExampleFrame()) 
				connEtoC36(e);
		};
		public void windowDeactivated(java.awt.event.WindowEvent e) {};
		public void windowDeiconified(java.awt.event.WindowEvent e) {};
		public void windowIconified(java.awt.event.WindowEvent e) {};
		public void windowOpened(java.awt.event.WindowEvent e) {};
	};
/**
 * Constructor
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public CounterExample() {
	super();
	initialize();
}
/**
 * CounterExample constructor comment.
 */
public CounterExample(TraceManager traceManager) {
	
	super();
	
	this.traceManager = traceManager;
	try {
		stepLength = traceManager.getNumOfSteps();
	}
	catch (Exception e) {
		System.err.println("Exception while retrieving the number of steps in the counter example.");
		e.printStackTrace(System.err);
	}
	options = new CounterExampleOptions(traceManager);
	initialize();

	// create the Variable watch window but make it hidden to start with.
	// added by tcw
	variableWatchWindow = new VariableWatchWindow("Variable Watch Window", traceManager, this);
	variableWatchWindow.hide();

	// create a collection to store the rooted graphs
	// added by tcw
	rootedGraphers = new ArrayList();

	// create a complete graph and make it hidden
	// added by tcw
	completeBipartiteGrapher = new CompleteBipartiteGrapher(traceManager);

	// create a collection to store the rooted object diagrams
	// added by tcw
	objectDiagramManagers = new ArrayList();
}
/**
 * Comment
 */
public void addConditionalBreakpointMenuItem_ActionEvents() {
	CounterExample ce;

	if (BUI.currentDriver != null) {
		if ((ce = BUI.currentDriver.getCurrentCounterExample()) != null) {
			Object selectedObject = ((DefaultMutableTreeNode)getVariableTree().getLastSelectedPathComponent()).getUserObject();
			if (selectedObject instanceof ValueNode) {
				ValueNode vn = (ValueNode)selectedObject;
				if (vn.object instanceof SootField) {
					SootField sf = (SootField)vn.object;
					ce.getCounterExampleOptions().show();
					ce.getCounterExampleOptions().enableConditionalBreakpoints();
					ce.getCounterExampleOptions().conditionCheckBox_ActionEvents();
					ce.getCounterExampleOptions().appendVariableName(sf.getDeclaringClass().getName() + "." + sf.getName());
				}
			}
		}
	}
	return;
}
/**
 * Comment
 */
public void backButton_ActionEvents() {
	try {
		updateButtons(false);
		for (int i = 0; step >0; i++) {
			step--;
			traceManager.back();

			// check for conditional breakpoints
			if (options.isConditionalBreakpointsEnabled() && options.cb.isSatisfied())
				break;

			// check for code breakpoints
			Object o = traceManager.getAnnotation();
			if (o instanceof ClassDeclarationAnnotation)
				o = ((ClassDeclarationAnnotation)o).getSootClass();
			if (options.isCodeBreakpointsEnabled() && options.containsBreakpoint(o))
				break;

			// check if stepping is currently used
			if (options.useStepping())
				break;
		}
		getStepSelectionSlider().setValue(step);
		getStepSelectionSlider1().setValue(step);
		update();
		updateButtons(true);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
/**
 * Comment
 */
public void backRunToolBarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	options.setUseStepping(false);
	backButton_ActionEvents();
	return;
}
/**
 * Comment
 */
public void backStepToolBarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	options.setUseStepping(true);
	backButton_ActionEvents();
	return;
}
    /**
     * Clean up the counter example and all connections.  This includes the
     * variable watch window, the graphers, the thread counter example windows,
     * and the options window.
     */
    public void closeButton_ActionEvents() {

	if((threadCounterExampleMap != null) && (threadCounterExampleMap.size() > 0)) {
	    Iterator threadIterator = threadCounterExampleMap.keySet().iterator();
	    while(threadIterator.hasNext()) {
		Integer threadID = (Integer)threadIterator.next();
		ThreadCounterExample tce = (ThreadCounterExample)threadCounterExampleMap.get(threadID);
		if(tce != null) {
		    tce.setVisible(false);
		    tce.dispose();
		}
	    }
	}
	
	options.dispose();
	
	// now get rid of the complete graph window
	// added by tcw
	if(completeBipartiteGrapher != null) {
	    completeBipartiteGrapher.hide();
	    completeBipartiteGrapher = null;
	}

	// get rid of the rooted graph windows
	// added by tcw
	if(rootedGraphers != null) {
		for(int i = 0; i < rootedGraphers.size(); i++) {
			RootedBipartiteGrapher rbg = (RootedBipartiteGrapher)rootedGraphers.get(i);
			rbg.hide();
		}
		rootedGraphers.clear();
		rootedGraphers = null;
	}

	// dispose of the variable watch window
	// added by tcw
	if(variableWatchWindow != null) {
		variableWatchWindow.setVisible(false);
		variableWatchWindow = null;
	}

	// dispose of all the rooted object diagram managers
	// added by tcw
	if(objectDiagramManagers != null) {
		for(int i = 0; i < objectDiagramManagers.size(); i++) {
			RootedObjectDiagramManager rodm = (RootedObjectDiagramManager)objectDiagramManagers.get(i);
			rodm.close();
		}
		objectDiagramManagers.clear();
		objectDiagramManagers = null;
	}
	
	setVisible(false);
	dispose();
	traceManager.cleanup();
    }
/**
 * Handle the menu-item to show a complete graph for this system.  This will cause the
 * complete graph to be created and displayed for this system.
 */
public void completeGraphToolBarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	if(completeBipartiteGrapher != null) {
		completeBipartiteGrapher.show();
	}
	else {
		completeBipartiteGrapher = new CompleteBipartiteGrapher(traceManager);
		completeBipartiteGrapher.show();
	}
}
/**
 * connEtoC1:  (ResetButton.action. --> CounterExample.resetButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		// user code end
		this.resetButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (BackRunToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.backRunToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC10(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.backRunToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (BackStepToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.backStepToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC11(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.backStepToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (ForwardStepToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.forwardStepToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.forwardStepToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (ForwardRunToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.forwardRunToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.forwardRunToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (OptionsToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.optionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.optionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC15:  (VariableTree.mouse.mouseReleased(java.awt.event.MouseEvent) --> CounterExample.variableTree_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC15(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.variableTree_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC16:  (AddConditionalBreakpointMenuItem.action. --> CounterExample.addConditionalBreakpointMenuItem_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC16() {
	try {
		// user code begin {1}
		// user code end
		this.addConditionalBreakpointMenuItem_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC17:  (WatchButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.watchButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC17(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.watchButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC18:  (WatchToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.watchButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC18(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.watchButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC19:  (WatchThisVariableMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.watchThisVariableMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC19(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.watchThisVariableMenuItem_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (ForwardButton.action. --> CounterExample.forwardButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.forwardButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC20:  (ShowLockVisualizationMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.showLockVisualizationMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC20(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.showLockVisualizationMenuItem_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC21:  (VariableTree.mouse.mouseClicked(java.awt.event.MouseEvent) --> CounterExample.variableTree_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC21(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.variableTree_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC22:  (VariableTree.mouse.mousePressed(java.awt.event.MouseEvent) --> CounterExample.variableTree_MousePressed(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC22(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.variableTree_MousePressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC23:  (CompleteGraphToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.completeGraphToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC23(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.completeGraphToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC24:  (ObjectDiagram.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.objectDiagram_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC24(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.objectDiagram_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC25:  (CounterExample.window.windowClosing(java.awt.event.WindowEvent) --> CounterExample.closeButton_ActionEvents()V)
 * @param arg1 java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC25(java.awt.event.WindowEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.closeButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC26:  (BackRunToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.backRunToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC26(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.backRunToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC27:  (BackStepToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.backStepToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC27(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.backStepToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC28:  (ResetToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.resetButton_ActionEvents()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC28(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.resetButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC29:  (ForwardStepToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.forwardStepToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC29(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.forwardStepToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (BackButton.action. --> CounterExample.backButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3() {
	try {
		// user code begin {1}
		// user code end
		this.backButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC30:  (ForwardRunToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.forwardRunToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC30(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.forwardRunToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC31:  (StepSelectionSlider1.change.stateChanged(javax.swing.event.ChangeEvent) --> CounterExample.stepSelectionSlider_StateChanged(Ljavax.swing.event.ChangeEvent;)V)
 * @param arg1 javax.swing.event.ChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC31(javax.swing.event.ChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.stepSelectionSlider1_StateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC32:  (CompleteGraphToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.completeGraphToolBarButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC32(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.completeGraphToolBarButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC33:  (OptionsToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.optionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC33(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.optionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC34:  (WatchToolBarButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.watchButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC34(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.watchButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC35:  (StepSelectionSlider1.mouse.mouseReleased(java.awt.event.MouseEvent) --> CounterExample.stepSelectionSlider1_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC35(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.stepSelectionSlider1_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC36:  (MinimizedCounterExampleFrame.window.windowClosing(java.awt.event.WindowEvent) --> CounterExample.closeButton_ActionEvents()V)
 * @param arg1 java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC36(java.awt.event.WindowEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.closeButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC37:  (SaveButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.saveButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC37(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saveButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC38:  (SaveButton1.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.saveButton1_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC38(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.saveButton1_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (CloseButton.action. --> CounterExample.closeButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		// user code end
		this.closeButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (VariableTree.treeSelection. --> CounterExample.variableTree_TreeSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5() {
	try {
		// user code begin {1}
		// user code end
		this.variableTree_TreeSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (OptionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.optionsButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.optionsButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (StepSelectionSlider.mouse.mouseReleased(java.awt.event.MouseEvent) --> CounterExample.stepSelectionSlider_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.stepSelectionSlider_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (StepSelectionSlider.change.stateChanged(javax.swing.event.ChangeEvent) --> CounterExample.stepSelectionSlider_StateChanged(Ljavax.swing.event.ChangeEvent;)V)
 * @param arg1 javax.swing.event.ChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8(javax.swing.event.ChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.stepSelectionSlider_StateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (ResetToolBarButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.resetButton_ActionEvents()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.resetButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (ShowMinimizedWindowButton.action.actionPerformed(java.awt.event.ActionEvent) --> MinimizedCounterExampleFrame.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getMinimizedCounterExampleFrame().setVisible(true);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM2:  (ShowMinimizedWindowButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2(java.awt.event.ActionEvent arg1) {
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
 * connEtoM3:  (ShowMaximizedWindowButton.action.actionPerformed(java.awt.event.ActionEvent) --> MinimizedCounterExampleFrame.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getMinimizedCounterExampleFrame().setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM4:  (ShowMaximizedWindowButton.action.actionPerformed(java.awt.event.ActionEvent) --> CounterExample.setVisible(Z)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.setVisible(true);
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
public void forwardButton_ActionEvents() {
	try {
		updateButtons(false);
		for (int i = 0; step < stepLength; i++) {
			step++;
			traceManager.forward();

			// check for conditional breakpoints
			if (options.isConditionalBreakpointsEnabled() && options.cb.isSatisfied())
				break;

			// check for code breakpoints
			Object o = traceManager.getAnnotation();
			if (o instanceof ClassDeclarationAnnotation)
				o = ((ClassDeclarationAnnotation)o).getSootClass();
			if (options.isCodeBreakpointsEnabled() && options.containsBreakpoint(o))
				break;

			// check if stepping is currently used
			if (options.useStepping())
				break;
		}
		getStepSelectionSlider().setValue(step);
		getStepSelectionSlider1().setValue(step);
		update();
		updateButtons(true);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
/**
 * Comment
 */
public void forwardRunToolBarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	options.setUseStepping(false);
	forwardButton_ActionEvents();
	return;
}
/**
 * Comment
 */
public void forwardStepToolBarButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	options.setUseStepping(true);
	forwardButton_ActionEvents();
	return;
}
/**
 * Return the AddConditionalBreakpointMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getAddConditionalBreakpointMenuItem() {
	if (ivjAddConditionalBreakpointMenuItem == null) {
		try {
			ivjAddConditionalBreakpointMenuItem = new javax.swing.JMenuItem();
			ivjAddConditionalBreakpointMenuItem.setName("AddConditionalBreakpointMenuItem");
			ivjAddConditionalBreakpointMenuItem.setMnemonic('c');
			ivjAddConditionalBreakpointMenuItem.setText("Add to Conditional Breakpoint");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjAddConditionalBreakpointMenuItem;
}
/**
 * Return the BackButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBackButton() {
	if (ivjBackButton == null) {
		try {
			ivjBackButton = new javax.swing.JButton();
			ivjBackButton.setName("BackButton");
			ivjBackButton.setToolTipText("Back Specified Number of Steps");
			ivjBackButton.setMnemonic('b');
			ivjBackButton.setText("<< Back");
			ivjBackButton.setBackground(new java.awt.Color(204,204,255));
			ivjBackButton.setPreferredSize(new java.awt.Dimension(56, 22));
			ivjBackButton.setBounds(75, 803, 66, 22);
			ivjBackButton.setEnabled(false);
			ivjBackButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
			ivjBackButton.setMinimumSize(new java.awt.Dimension(56, 22));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackButton;
}
/**
 * Return the DefaultToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBackRunToolBarButton() {
	if (ivjBackRunToolBarButton == null) {
		try {
			ivjBackRunToolBarButton = new javax.swing.JButton();
			ivjBackRunToolBarButton.setName("BackRunToolBarButton");
			ivjBackRunToolBarButton.setToolTipText("Backward Running");
			ivjBackRunToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackRunRollover1.gif")));
			ivjBackRunToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjBackRunToolBarButton.setDisabledSelectedIcon(null);
			ivjBackRunToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjBackRunToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackRunPressed1.gif")));
			ivjBackRunToolBarButton.setText("");
			ivjBackRunToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjBackRunToolBarButton.setDisabledIcon(null);
			ivjBackRunToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjBackRunToolBarButton.setBorderPainted(false);
			ivjBackRunToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackRunNormal1.gif")));
			ivjBackRunToolBarButton.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
			ivjBackRunToolBarButton.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackRunToolBarButton;
}
/**
 * Return the BackRunToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBackRunToolBarButton1() {
	if (ivjBackRunToolBarButton1 == null) {
		try {
			ivjBackRunToolBarButton1 = new javax.swing.JButton();
			ivjBackRunToolBarButton1.setName("BackRunToolBarButton1");
			ivjBackRunToolBarButton1.setToolTipText("Backward Running");
			ivjBackRunToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackRunRollover1.gif")));
			ivjBackRunToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjBackRunToolBarButton1.setDisabledSelectedIcon(null);
			ivjBackRunToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjBackRunToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackRunPressed1.gif")));
			ivjBackRunToolBarButton1.setText("");
			ivjBackRunToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjBackRunToolBarButton1.setDisabledIcon(null);
			ivjBackRunToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjBackRunToolBarButton1.setBorderPainted(false);
			ivjBackRunToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackRunNormal1.gif")));
			ivjBackRunToolBarButton1.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
			ivjBackRunToolBarButton1.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackRunToolBarButton1;
}
/**
 * Return the BackStepToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBackStepToolBarButton() {
	if (ivjBackStepToolBarButton == null) {
		try {
			ivjBackStepToolBarButton = new javax.swing.JButton();
			ivjBackStepToolBarButton.setName("BackStepToolBarButton");
			ivjBackStepToolBarButton.setToolTipText("Backward Stepping");
			ivjBackStepToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackStepRollover1.gif")));
			ivjBackStepToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjBackStepToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjBackStepToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackStepPressed1.gif")));
			ivjBackStepToolBarButton.setText("");
			ivjBackStepToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjBackStepToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjBackStepToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackStepNormal1.gif")));
			ivjBackStepToolBarButton.setBorderPainted(false);
			ivjBackStepToolBarButton.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackStepToolBarButton;
}
/**
 * Return the BackStepToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBackStepToolBarButton1() {
	if (ivjBackStepToolBarButton1 == null) {
		try {
			ivjBackStepToolBarButton1 = new javax.swing.JButton();
			ivjBackStepToolBarButton1.setName("BackStepToolBarButton1");
			ivjBackStepToolBarButton1.setToolTipText("Backward Stepping");
			ivjBackStepToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackStepRollover1.gif")));
			ivjBackStepToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjBackStepToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjBackStepToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackStepPressed1.gif")));
			ivjBackStepToolBarButton1.setText("");
			ivjBackStepToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjBackStepToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjBackStepToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/BackStepNormal1.gif")));
			ivjBackStepToolBarButton1.setBorderPainted(false);
			ivjBackStepToolBarButton1.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackStepToolBarButton1;
}
/**
 * Return the JPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getButtonPanel() {
	if (ivjButtonPanel == null) {
		try {
			ivjButtonPanel = new javax.swing.JPanel();
			ivjButtonPanel.setName("ButtonPanel");
			ivjButtonPanel.setBorder(new javax.swing.border.EtchedBorder());
			ivjButtonPanel.setLayout(new java.awt.GridBagLayout());
			ivjButtonPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsStepLabel = new java.awt.GridBagConstraints();
			constraintsStepLabel.gridx = 0; constraintsStepLabel.gridy = 0;
			constraintsStepLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getStepLabel(), constraintsStepLabel);

			java.awt.GridBagConstraints constraintsSteppingButtonPanel = new java.awt.GridBagConstraints();
			constraintsSteppingButtonPanel.gridx = 0; constraintsSteppingButtonPanel.gridy = 1;
			constraintsSteppingButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSteppingButtonPanel.weightx = 1.0;
			constraintsSteppingButtonPanel.weighty = 1.0;
			constraintsSteppingButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getSteppingButtonPanel(), constraintsSteppingButtonPanel);

			java.awt.GridBagConstraints constraintsWindowButtonPanel = new java.awt.GridBagConstraints();
			constraintsWindowButtonPanel.gridx = 0; constraintsWindowButtonPanel.gridy = 3;
			constraintsWindowButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsWindowButtonPanel.weightx = 1.0;
			constraintsWindowButtonPanel.weighty = 1.0;
			constraintsWindowButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getButtonPanel().add(getWindowButtonPanel(), constraintsWindowButtonPanel);

			java.awt.GridBagConstraints constraintsSliderPanel = new java.awt.GridBagConstraints();
			constraintsSliderPanel.gridx = 0; constraintsSliderPanel.gridy = 2;
			constraintsSliderPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSliderPanel.weightx = 1.0;
			constraintsSliderPanel.weighty = 1.0;
			constraintsSliderPanel.insets = new java.awt.Insets(10, 4, 4, 4);
			getButtonPanel().add(getSliderPanel(), constraintsSliderPanel);
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
 * Return the CloseButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCloseButton() {
	if (ivjCloseButton == null) {
		try {
			ivjCloseButton = new javax.swing.JButton();
			ivjCloseButton.setName("CloseButton");
			ivjCloseButton.setToolTipText("Close Couter Example Display");
			ivjCloseButton.setMnemonic('c');
			ivjCloseButton.setText("Close");
			ivjCloseButton.setBackground(new java.awt.Color(204,204,255));
			ivjCloseButton.setPreferredSize(new java.awt.Dimension(44, 22));
			ivjCloseButton.setBounds(243, 802, 54, 22);
			ivjCloseButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
			ivjCloseButton.setMinimumSize(new java.awt.Dimension(44, 22));
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
 * Return the CompleteGraphToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCompleteGraphToolBarButton() {
	if (ivjCompleteGraphToolBarButton == null) {
		try {
			ivjCompleteGraphToolBarButton = new javax.swing.JButton();
			ivjCompleteGraphToolBarButton.setName("CompleteGraphToolBarButton");
			ivjCompleteGraphToolBarButton.setToolTipText("Open the Complete Lock Graph Window");
			ivjCompleteGraphToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/LockGraphPressed1.GIF")));
			ivjCompleteGraphToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjCompleteGraphToolBarButton.setSelected(false);
			ivjCompleteGraphToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjCompleteGraphToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/LockGraphPressed1.GIF")));
			ivjCompleteGraphToolBarButton.setText("");
			ivjCompleteGraphToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjCompleteGraphToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjCompleteGraphToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/LockGraphNormal1.GIF")));
			ivjCompleteGraphToolBarButton.setBorderPainted(false);
			ivjCompleteGraphToolBarButton.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCompleteGraphToolBarButton;
}
/**
 * Return the CompleteGraphToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCompleteGraphToolBarButton1() {
	if (ivjCompleteGraphToolBarButton1 == null) {
		try {
			ivjCompleteGraphToolBarButton1 = new javax.swing.JButton();
			ivjCompleteGraphToolBarButton1.setName("CompleteGraphToolBarButton1");
			ivjCompleteGraphToolBarButton1.setToolTipText("Open the Complete Lock Graph Window");
			ivjCompleteGraphToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/LockGraphPressed1.GIF")));
			ivjCompleteGraphToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjCompleteGraphToolBarButton1.setSelected(false);
			ivjCompleteGraphToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjCompleteGraphToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/LockGraphPressed1.GIF")));
			ivjCompleteGraphToolBarButton1.setText("");
			ivjCompleteGraphToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjCompleteGraphToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjCompleteGraphToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/LockGraphNormal1.GIF")));
			ivjCompleteGraphToolBarButton1.setBorderPainted(false);
			ivjCompleteGraphToolBarButton1.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCompleteGraphToolBarButton1;
}
/**
 * Return the CounterExampleDialogContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getCounterExampleDialogContentPane() {
	if (ivjCounterExampleDialogContentPane == null) {
		try {
			ivjCounterExampleDialogContentPane = new javax.swing.JPanel();
			ivjCounterExampleDialogContentPane.setName("CounterExampleDialogContentPane");
			ivjCounterExampleDialogContentPane.setBorder(new javax.swing.border.EtchedBorder());
			ivjCounterExampleDialogContentPane.setLayout(new java.awt.GridBagLayout());
			ivjCounterExampleDialogContentPane.setBackground(new java.awt.Color(204,204,255));
			ivjCounterExampleDialogContentPane.setPreferredSize(new java.awt.Dimension(600, 420));
			ivjCounterExampleDialogContentPane.setMinimumSize(new java.awt.Dimension(550, 420));

			java.awt.GridBagConstraints constraintsVariableScrollPane = new java.awt.GridBagConstraints();
			constraintsVariableScrollPane.gridx = 0; constraintsVariableScrollPane.gridy = 1;
			constraintsVariableScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsVariableScrollPane.weightx = 0.9;
			constraintsVariableScrollPane.weighty = 1.0;
			constraintsVariableScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getCounterExampleDialogContentPane().add(getVariableScrollPane(), constraintsVariableScrollPane);

			java.awt.GridBagConstraints constraintsTextAndButtonPanel = new java.awt.GridBagConstraints();
			constraintsTextAndButtonPanel.gridx = 1; constraintsTextAndButtonPanel.gridy = 1;
			constraintsTextAndButtonPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsTextAndButtonPanel.weightx = 0.2;
			constraintsTextAndButtonPanel.weighty = 1.0;
			constraintsTextAndButtonPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getCounterExampleDialogContentPane().add(getTextAndButtonPanel(), constraintsTextAndButtonPanel);

			java.awt.GridBagConstraints constraintsViolationNamePanel = new java.awt.GridBagConstraints();
			constraintsViolationNamePanel.gridx = 0; constraintsViolationNamePanel.gridy = 0;
			constraintsViolationNamePanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsViolationNamePanel.weighty = 0.4;
			constraintsViolationNamePanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getCounterExampleDialogContentPane().add(getViolationNamePanel(), constraintsViolationNamePanel);

			java.awt.GridBagConstraints constraintsViolationHintsPanel = new java.awt.GridBagConstraints();
			constraintsViolationHintsPanel.gridx = 1; constraintsViolationHintsPanel.gridy = 0;
			constraintsViolationHintsPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsTextAndButtonPanel.weightx = 1.0;
			constraintsViolationHintsPanel.weighty = 0.4;
			constraintsViolationHintsPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getCounterExampleDialogContentPane().add(getViolationHintsPanel(), constraintsViolationHintsPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCounterExampleDialogContentPane;
}
public CounterExampleOptions getCounterExampleOptions() {
	return options;
}
/**
 * Return the ForwardButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getForwardButton() {
	if (ivjForwardButton == null) {
		try {
			ivjForwardButton = new javax.swing.JButton();
			ivjForwardButton.setName("ForwardButton");
			ivjForwardButton.setToolTipText("Forward Specified Number of Steps");
			ivjForwardButton.setMnemonic('f');
			ivjForwardButton.setText("Forward >>");
			ivjForwardButton.setBackground(new java.awt.Color(204,204,255));
			ivjForwardButton.setPreferredSize(new java.awt.Dimension(76, 22));
			ivjForwardButton.setBounds(148, 803, 86, 22);
			ivjForwardButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
			ivjForwardButton.setMinimumSize(new java.awt.Dimension(76, 22));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForwardButton;
}
/**
 * Return the ForwardRunToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getForwardRunToolBarButton() {
	if (ivjForwardRunToolBarButton == null) {
		try {
			ivjForwardRunToolBarButton = new javax.swing.JButton();
			ivjForwardRunToolBarButton.setName("ForwardRunToolBarButton");
			ivjForwardRunToolBarButton.setToolTipText("Forward Running");
			ivjForwardRunToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardRunRollover1.gif")));
			ivjForwardRunToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjForwardRunToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjForwardRunToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardRunPressed1.gif")));
			ivjForwardRunToolBarButton.setText("");
			ivjForwardRunToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjForwardRunToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjForwardRunToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardRunNormal1.gif")));
			ivjForwardRunToolBarButton.setBorderPainted(false);
			ivjForwardRunToolBarButton.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForwardRunToolBarButton;
}
/**
 * Return the ForwardRunToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getForwardRunToolBarButton1() {
	if (ivjForwardRunToolBarButton1 == null) {
		try {
			ivjForwardRunToolBarButton1 = new javax.swing.JButton();
			ivjForwardRunToolBarButton1.setName("ForwardRunToolBarButton1");
			ivjForwardRunToolBarButton1.setToolTipText("Forward Running");
			ivjForwardRunToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardRunRollover1.gif")));
			ivjForwardRunToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjForwardRunToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjForwardRunToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardRunPressed1.gif")));
			ivjForwardRunToolBarButton1.setText("");
			ivjForwardRunToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjForwardRunToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjForwardRunToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardRunNormal1.gif")));
			ivjForwardRunToolBarButton1.setBorderPainted(false);
			ivjForwardRunToolBarButton1.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForwardRunToolBarButton1;
}
/**
 * Return the ForwardStepToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getForwardStepToolBarButton() {
	if (ivjForwardStepToolBarButton == null) {
		try {
			ivjForwardStepToolBarButton = new javax.swing.JButton();
			ivjForwardStepToolBarButton.setName("ForwardStepToolBarButton");
			ivjForwardStepToolBarButton.setToolTipText("Forward Stepping");
			ivjForwardStepToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardStepRollover1.gif")));
			ivjForwardStepToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjForwardStepToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjForwardStepToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardStepPressed1.gif")));
			ivjForwardStepToolBarButton.setText("");
			ivjForwardStepToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjForwardStepToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjForwardStepToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardStepNormal1.gif")));
			ivjForwardStepToolBarButton.setBorderPainted(false);
			ivjForwardStepToolBarButton.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForwardStepToolBarButton;
}
/**
 * Return the ForwardStepToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getForwardStepToolBarButton1() {
	if (ivjForwardStepToolBarButton1 == null) {
		try {
			ivjForwardStepToolBarButton1 = new javax.swing.JButton();
			ivjForwardStepToolBarButton1.setName("ForwardStepToolBarButton1");
			ivjForwardStepToolBarButton1.setToolTipText("Forward Stepping");
			ivjForwardStepToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardStepRollover1.gif")));
			ivjForwardStepToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjForwardStepToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjForwardStepToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardStepPressed1.gif")));
			ivjForwardStepToolBarButton1.setText("");
			ivjForwardStepToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjForwardStepToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjForwardStepToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ForwardStepNormal1.gif")));
			ivjForwardStepToolBarButton1.setBorderPainted(false);
			ivjForwardStepToolBarButton1.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjForwardStepToolBarButton1;
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

			java.awt.GridBagConstraints constraintsJPanel = new java.awt.GridBagConstraints();
			constraintsJPanel.gridx = 0; constraintsJPanel.gridy = 3;
			constraintsJPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPanel.weightx = 1.0;
			constraintsJPanel.weighty = 1.0;
			constraintsJPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getJPanel(), constraintsJPanel);

			java.awt.GridBagConstraints constraintsStepLabel1 = new java.awt.GridBagConstraints();
			constraintsStepLabel1.gridx = 0; constraintsStepLabel1.gridy = 0;
			constraintsStepLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getStepLabel1(), constraintsStepLabel1);

			java.awt.GridBagConstraints constraintsSteppingButtonPanel1 = new java.awt.GridBagConstraints();
			constraintsSteppingButtonPanel1.gridx = 0; constraintsSteppingButtonPanel1.gridy = 1;
			constraintsSteppingButtonPanel1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSteppingButtonPanel1.weightx = 1.0;
			constraintsSteppingButtonPanel1.weighty = 1.0;
			constraintsSteppingButtonPanel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getSteppingButtonPanel1(), constraintsSteppingButtonPanel1);

			java.awt.GridBagConstraints constraintsSliderPanel1 = new java.awt.GridBagConstraints();
			constraintsSliderPanel1.gridx = 0; constraintsSliderPanel1.gridy = 2;
			constraintsSliderPanel1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSliderPanel1.weightx = 1.0;
			constraintsSliderPanel1.weighty = 1.0;
			constraintsSliderPanel1.insets = new java.awt.Insets(1, 1, 1, 1);
			getJFrameContentPane().add(getSliderPanel1(), constraintsSliderPanel1);
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
 * Return the JPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel() {
	if (ivjJPanel == null) {
		try {
			ivjJPanel = new javax.swing.JPanel();
			ivjJPanel.setName("JPanel");
			ivjJPanel.setLayout(new java.awt.GridBagLayout());
			ivjJPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsCompleteGraphToolBarButton1 = new java.awt.GridBagConstraints();
			constraintsCompleteGraphToolBarButton1.gridx = 0; constraintsCompleteGraphToolBarButton1.gridy = 0;
			constraintsCompleteGraphToolBarButton1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel().add(getCompleteGraphToolBarButton1(), constraintsCompleteGraphToolBarButton1);

			java.awt.GridBagConstraints constraintsOptionsToolBarButton1 = new java.awt.GridBagConstraints();
			constraintsOptionsToolBarButton1.gridx = 1; constraintsOptionsToolBarButton1.gridy = 0;
			constraintsOptionsToolBarButton1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel().add(getOptionsToolBarButton1(), constraintsOptionsToolBarButton1);

			java.awt.GridBagConstraints constraintsWatchToolBarButton1 = new java.awt.GridBagConstraints();
			constraintsWatchToolBarButton1.gridx = 2; constraintsWatchToolBarButton1.gridy = 0;
			constraintsWatchToolBarButton1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel().add(getWatchToolBarButton1(), constraintsWatchToolBarButton1);

			java.awt.GridBagConstraints constraintsShowMaximizedWindowButton = new java.awt.GridBagConstraints();
			constraintsShowMaximizedWindowButton.gridx = 4; constraintsShowMaximizedWindowButton.gridy = 0;
			constraintsShowMaximizedWindowButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel().add(getShowMaximizedWindowButton(), constraintsShowMaximizedWindowButton);

			java.awt.GridBagConstraints constraintsSaveButton1 = new java.awt.GridBagConstraints();
			constraintsSaveButton1.gridx = 3; constraintsSaveButton1.gridy = 0;
			constraintsSaveButton1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel().add(getSaveButton1(), constraintsSaveButton1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanel;
}
/**
 * Return the JSeparator1 property value.
 * @return javax.swing.JSeparator
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JSeparator getJSeparator1() {
	if (ivjJSeparator1 == null) {
		try {
			ivjJSeparator1 = new javax.swing.JSeparator();
			ivjJSeparator1.setName("JSeparator1");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJSeparator1;
}
/**
 * Return the MinimizedCounterExampleFrame property value.
 * @return javax.swing.JFrame
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JFrame getMinimizedCounterExampleFrame() {
	if (ivjMinimizedCounterExampleFrame == null) {
		try {
			ivjMinimizedCounterExampleFrame = new javax.swing.JFrame();
			ivjMinimizedCounterExampleFrame.setName("MinimizedCounterExampleFrame");
			ivjMinimizedCounterExampleFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			ivjMinimizedCounterExampleFrame.setBounds(583, 512, 300, 163);
			ivjMinimizedCounterExampleFrame.setTitle("Counter Example (minimized)");
			getMinimizedCounterExampleFrame().setContentPane(getJFrameContentPane());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMinimizedCounterExampleFrame;
}
/**
 * Return the ObjectDiagram property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getObjectDiagram() {
	if (ivjObjectDiagram == null) {
		try {
			ivjObjectDiagram = new javax.swing.JMenuItem();
			ivjObjectDiagram.setName("ObjectDiagram");
			ivjObjectDiagram.setToolTipText("Draw a simple rooted Object diagram using this object as the root.");
			ivjObjectDiagram.setText("Draw Object Diagram");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjObjectDiagram;
}
/**
 * Return the OptionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOptionsButton() {
	if (ivjOptionsButton == null) {
		try {
			ivjOptionsButton = new javax.swing.JButton();
			ivjOptionsButton.setName("OptionsButton");
			ivjOptionsButton.setToolTipText("Show Options");
			ivjOptionsButton.setMnemonic('o');
			ivjOptionsButton.setText("Options ...");
			ivjOptionsButton.setBackground(new java.awt.Color(204,204,255));
			ivjOptionsButton.setPreferredSize(new java.awt.Dimension(68, 22));
			ivjOptionsButton.setBounds(381, 799, 78, 22);
			ivjOptionsButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
			ivjOptionsButton.setMinimumSize(new java.awt.Dimension(68, 22));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOptionsButton;
}
/**
 * Return the OptionsToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOptionsToolBarButton() {
	if (ivjOptionsToolBarButton == null) {
		try {
			ivjOptionsToolBarButton = new javax.swing.JButton();
			ivjOptionsToolBarButton.setName("OptionsToolBarButton");
			ivjOptionsToolBarButton.setToolTipText("Counter Example Options");
			ivjOptionsToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsRollover1.gif")));
			ivjOptionsToolBarButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsRollover1.gif")));
			ivjOptionsToolBarButton.setDoubleBuffered(false);
			ivjOptionsToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjOptionsToolBarButton.setSelected(true);
			ivjOptionsToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjOptionsToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsPressed1.gif")));
			ivjOptionsToolBarButton.setOpaque(true);
			ivjOptionsToolBarButton.setText("");
			ivjOptionsToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjOptionsToolBarButton.setVisible(true);
			ivjOptionsToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjOptionsToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsNormal1.gif")));
			ivjOptionsToolBarButton.setBorderPainted(false);
			ivjOptionsToolBarButton.setContentAreaFilled(true);
			ivjOptionsToolBarButton.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOptionsToolBarButton;
}
/**
 * Return the OptionsToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOptionsToolBarButton1() {
	if (ivjOptionsToolBarButton1 == null) {
		try {
			ivjOptionsToolBarButton1 = new javax.swing.JButton();
			ivjOptionsToolBarButton1.setName("OptionsToolBarButton1");
			ivjOptionsToolBarButton1.setToolTipText("Counter Example Options");
			ivjOptionsToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsRollover1.gif")));
			ivjOptionsToolBarButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsRollover1.gif")));
			ivjOptionsToolBarButton1.setDoubleBuffered(false);
			ivjOptionsToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjOptionsToolBarButton1.setSelected(true);
			ivjOptionsToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjOptionsToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsPressed1.gif")));
			ivjOptionsToolBarButton1.setOpaque(true);
			ivjOptionsToolBarButton1.setText("");
			ivjOptionsToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjOptionsToolBarButton1.setVisible(true);
			ivjOptionsToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjOptionsToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/OptionsNormal1.gif")));
			ivjOptionsToolBarButton1.setBorderPainted(false);
			ivjOptionsToolBarButton1.setContentAreaFilled(true);
			ivjOptionsToolBarButton1.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOptionsToolBarButton1;
}
/**
 * Return the ResetButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getResetButton() {
	if (ivjResetButton == null) {
		try {
			ivjResetButton = new javax.swing.JButton();
			ivjResetButton.setName("ResetButton");
			ivjResetButton.setToolTipText("Reset the Counter Example");
			ivjResetButton.setMnemonic('r');
			ivjResetButton.setText("Reset");
			ivjResetButton.setBackground(new java.awt.Color(204,204,255));
			ivjResetButton.setPreferredSize(new java.awt.Dimension(42, 22));
			ivjResetButton.setBounds(15, 805, 52, 22);
			ivjResetButton.setEnabled(false);
			ivjResetButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
			ivjResetButton.setMinimumSize(new java.awt.Dimension(42, 22));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResetButton;
}
/**
 * Return the ResetToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getResetToolBarButton() {
	if (ivjResetToolBarButton == null) {
		try {
			ivjResetToolBarButton = new javax.swing.JButton();
			ivjResetToolBarButton.setName("ResetToolBarButton");
			ivjResetToolBarButton.setToolTipText("Reset the Counter Example");
			ivjResetToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ResetRollover1.gif")));
			ivjResetToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjResetToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjResetToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ResetPressed1.gif")));
			ivjResetToolBarButton.setText("");
			ivjResetToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjResetToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjResetToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ResetNormal1.gif")));
			ivjResetToolBarButton.setBorderPainted(false);
			ivjResetToolBarButton.setContentAreaFilled(true);
			ivjResetToolBarButton.setRolloverEnabled(true);
			ivjResetToolBarButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResetToolBarButton;
}
/**
 * Return the ResetToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getResetToolBarButton1() {
	if (ivjResetToolBarButton1 == null) {
		try {
			ivjResetToolBarButton1 = new javax.swing.JButton();
			ivjResetToolBarButton1.setName("ResetToolBarButton1");
			ivjResetToolBarButton1.setToolTipText("Reset the Counter Example");
			ivjResetToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ResetRollover1.gif")));
			ivjResetToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjResetToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjResetToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ResetPressed1.gif")));
			ivjResetToolBarButton1.setText("");
			ivjResetToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjResetToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjResetToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/ResetNormal1.gif")));
			ivjResetToolBarButton1.setBorderPainted(false);
			ivjResetToolBarButton1.setContentAreaFilled(true);
			ivjResetToolBarButton1.setRolloverEnabled(true);
			ivjResetToolBarButton1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResetToolBarButton1;
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
			ivjSaveButton.setToolTipText("Save this Counter Example to a file");
			ivjSaveButton.setText("Save");
			ivjSaveButton.setBackground(new java.awt.Color(204,204,255));
			ivjSaveButton.setEnabled(false);
			// user code begin {1}
            ivjSaveButton.setEnabled(true);
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
 * Return the SaveButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSaveButton1() {
	if (ivjSaveButton1 == null) {
		try {
			ivjSaveButton1 = new javax.swing.JButton();
			ivjSaveButton1.setName("SaveButton1");
			ivjSaveButton1.setToolTipText("Save this Counter Example to a file");
			ivjSaveButton1.setText("Save");
			ivjSaveButton1.setBackground(new java.awt.Color(204,204,255));
			ivjSaveButton1.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSaveButton1;
}
/**
 * Return the ShowLockVisualizationMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getShowLockVisualizationMenuItem() {
	if (ivjShowLockVisualizationMenuItem == null) {
		try {
			ivjShowLockVisualizationMenuItem = new javax.swing.JMenuItem();
			ivjShowLockVisualizationMenuItem.setName("ShowLockVisualizationMenuItem");
			ivjShowLockVisualizationMenuItem.setMnemonic('v');
			ivjShowLockVisualizationMenuItem.setText("Draw Rooted Lock Graph");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowLockVisualizationMenuItem;
}
/**
 * Return the ShowMaximizedWindowButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getShowMaximizedWindowButton() {
	if (ivjShowMaximizedWindowButton == null) {
		try {
			ivjShowMaximizedWindowButton = new javax.swing.JButton();
			ivjShowMaximizedWindowButton.setName("ShowMaximizedWindowButton");
			ivjShowMaximizedWindowButton.setToolTipText("Show the Maximized version of the Counter Example GUI");
			ivjShowMaximizedWindowButton.setText("Maximize");
			ivjShowMaximizedWindowButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowMaximizedWindowButton;
}
/**
 * Return the ShowMinimizedWindowButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getShowMinimizedWindowButton() {
	if (ivjShowMinimizedWindowButton == null) {
		try {
			ivjShowMinimizedWindowButton = new javax.swing.JButton();
			ivjShowMinimizedWindowButton.setName("ShowMinimizedWindowButton");
			ivjShowMinimizedWindowButton.setToolTipText("Show the Minimized version of the Counter Example window");
			ivjShowMinimizedWindowButton.setText("Minimize");
			ivjShowMinimizedWindowButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjShowMinimizedWindowButton;
}
/**
 * Return the SliderPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSliderPanel() {
	if (ivjSliderPanel == null) {
		try {
			ivjSliderPanel = new javax.swing.JPanel();
			ivjSliderPanel.setName("SliderPanel");
			ivjSliderPanel.setPreferredSize(new java.awt.Dimension(100, 24));
			ivjSliderPanel.setLayout(new java.awt.GridBagLayout());
			ivjSliderPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsStepSelectionSlider = new java.awt.GridBagConstraints();
			constraintsStepSelectionSlider.gridx = 1; constraintsStepSelectionSlider.gridy = 1;
			constraintsStepSelectionSlider.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsStepSelectionSlider.weightx = 1.0;
			constraintsStepSelectionSlider.ipady = 9;
			constraintsStepSelectionSlider.insets = new java.awt.Insets(0, 4, 0, 4);
			getSliderPanel().add(getStepSelectionSlider(), constraintsStepSelectionSlider);

			java.awt.GridBagConstraints constraintsSteppingCaptionLabel = new java.awt.GridBagConstraints();
			constraintsSteppingCaptionLabel.gridx = 2; constraintsSteppingCaptionLabel.gridy = 1;
			constraintsSteppingCaptionLabel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSteppingCaptionLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSteppingCaptionLabel.weightx = 0.1;
			constraintsSteppingCaptionLabel.weighty = 1.0;
			constraintsSteppingCaptionLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getSliderPanel().add(getSteppingCaptionLabel(), constraintsSteppingCaptionLabel);

			java.awt.GridBagConstraints constraintsSteppingLabel = new java.awt.GridBagConstraints();
			constraintsSteppingLabel.gridx = 3; constraintsSteppingLabel.gridy = 1;
			constraintsSteppingLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getSliderPanel().add(getSteppingLabel(), constraintsSteppingLabel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSliderPanel;
}
/**
 * Return the SliderPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSliderPanel1() {
	if (ivjSliderPanel1 == null) {
		try {
			ivjSliderPanel1 = new javax.swing.JPanel();
			ivjSliderPanel1.setName("SliderPanel1");
			ivjSliderPanel1.setPreferredSize(new java.awt.Dimension(100, 24));
			ivjSliderPanel1.setLayout(new java.awt.GridBagLayout());
			ivjSliderPanel1.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsSteppingCaptionLabel1 = new java.awt.GridBagConstraints();
			constraintsSteppingCaptionLabel1.gridx = 1; constraintsSteppingCaptionLabel1.gridy = 0;
			constraintsSteppingCaptionLabel1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSteppingCaptionLabel1.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSteppingCaptionLabel1.weightx = 0.1;
			constraintsSteppingCaptionLabel1.weighty = 1.0;
			constraintsSteppingCaptionLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getSliderPanel1().add(getSteppingCaptionLabel1(), constraintsSteppingCaptionLabel1);

			java.awt.GridBagConstraints constraintsSteppingLabel1 = new java.awt.GridBagConstraints();
			constraintsSteppingLabel1.gridx = 2; constraintsSteppingLabel1.gridy = 0;
			constraintsSteppingLabel1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsSteppingLabel1.anchor = java.awt.GridBagConstraints.WEST;
			constraintsSteppingLabel1.weightx = 0.1;
			constraintsSteppingLabel1.weighty = 1.0;
			constraintsSteppingLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getSliderPanel1().add(getSteppingLabel1(), constraintsSteppingLabel1);

			java.awt.GridBagConstraints constraintsStepSelectionSlider1 = new java.awt.GridBagConstraints();
			constraintsStepSelectionSlider1.gridx = 0; constraintsStepSelectionSlider1.gridy = 0;
			constraintsStepSelectionSlider1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsStepSelectionSlider1.weightx = 0.8;
			constraintsStepSelectionSlider1.weighty = 1.0;
			constraintsStepSelectionSlider1.ipady = 9;
			constraintsStepSelectionSlider1.insets = new java.awt.Insets(0, 4, 0, 4);
			getSliderPanel1().add(getStepSelectionSlider1(), constraintsStepSelectionSlider1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSliderPanel1;
}
/**
 * Return the StepLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getStepLabel() {
	if (ivjStepLabel == null) {
		try {
			ivjStepLabel = new javax.swing.JLabel();
			ivjStepLabel.setName("StepLabel");
			ivjStepLabel.setText("Current Step #:");
			ivjStepLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStepLabel;
}
/**
 * Return the StepLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getStepLabel1() {
	if (ivjStepLabel1 == null) {
		try {
			ivjStepLabel1 = new javax.swing.JLabel();
			ivjStepLabel1.setName("StepLabel1");
			ivjStepLabel1.setText("Current Step #:");
			ivjStepLabel1.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStepLabel1;
}
/**
 * Return the SteppingButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSteppingButtonPanel() {
	if (ivjSteppingButtonPanel == null) {
		try {
			ivjSteppingButtonPanel = new javax.swing.JPanel();
			ivjSteppingButtonPanel.setName("SteppingButtonPanel");
			ivjSteppingButtonPanel.setLayout(new java.awt.GridLayout());
			ivjSteppingButtonPanel.setBackground(java.awt.Color.lightGray);
			getSteppingButtonPanel().add(getBackRunToolBarButton(), getBackRunToolBarButton().getName());
			ivjSteppingButtonPanel.add(getBackStepToolBarButton());
			ivjSteppingButtonPanel.add(getResetToolBarButton());
			ivjSteppingButtonPanel.add(getForwardStepToolBarButton());
			ivjSteppingButtonPanel.add(getForwardRunToolBarButton());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSteppingButtonPanel;
}
/**
 * Return the SteppingButtonPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSteppingButtonPanel1() {
	if (ivjSteppingButtonPanel1 == null) {
		try {
			ivjSteppingButtonPanel1 = new javax.swing.JPanel();
			ivjSteppingButtonPanel1.setName("SteppingButtonPanel1");
			ivjSteppingButtonPanel1.setLayout(new java.awt.GridLayout());
			ivjSteppingButtonPanel1.setBackground(java.awt.Color.lightGray);
			getSteppingButtonPanel1().add(getBackRunToolBarButton1(), getBackRunToolBarButton1().getName());
			ivjSteppingButtonPanel1.add(getBackStepToolBarButton1());
			ivjSteppingButtonPanel1.add(getResetToolBarButton1());
			ivjSteppingButtonPanel1.add(getForwardStepToolBarButton1());
			ivjSteppingButtonPanel1.add(getForwardRunToolBarButton1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSteppingButtonPanel1;
}
/**
 * Return the SteppingCaptionLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSteppingCaptionLabel() {
	if (ivjSteppingCaptionLabel == null) {
		try {
			ivjSteppingCaptionLabel = new javax.swing.JLabel();
			ivjSteppingCaptionLabel.setName("SteppingCaptionLabel");
			ivjSteppingCaptionLabel.setText("Target Step:");
			ivjSteppingCaptionLabel.setForeground(new java.awt.Color(0,0,0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSteppingCaptionLabel;
}
/**
 * Return the SteppingCaptionLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSteppingCaptionLabel1() {
	if (ivjSteppingCaptionLabel1 == null) {
		try {
			ivjSteppingCaptionLabel1 = new javax.swing.JLabel();
			ivjSteppingCaptionLabel1.setName("SteppingCaptionLabel1");
			ivjSteppingCaptionLabel1.setText("Target Step:");
			ivjSteppingCaptionLabel1.setForeground(new java.awt.Color(0,0,0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSteppingCaptionLabel1;
}
/**
 * Return the SteppingLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSteppingLabel() {
	if (ivjSteppingLabel == null) {
		try {
			ivjSteppingLabel = new javax.swing.JLabel();
			ivjSteppingLabel.setName("SteppingLabel");
			ivjSteppingLabel.setText("0");
			ivjSteppingLabel.setForeground(new java.awt.Color(0,0,0));
			ivjSteppingLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSteppingLabel;
}
/**
 * Return the SteppingLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getSteppingLabel1() {
	if (ivjSteppingLabel1 == null) {
		try {
			ivjSteppingLabel1 = new javax.swing.JLabel();
			ivjSteppingLabel1.setName("SteppingLabel1");
			ivjSteppingLabel1.setText("0");
			ivjSteppingLabel1.setForeground(new java.awt.Color(0,0,0));
			ivjSteppingLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSteppingLabel1;
}
/**
 * Return the SteppingSelectionSlider property value.
 * @return javax.swing.JSlider
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JSlider getStepSelectionSlider() {
	if (ivjStepSelectionSlider == null) {
		try {
			ivjStepSelectionSlider = new javax.swing.JSlider();
			ivjStepSelectionSlider.setName("StepSelectionSlider");
			ivjStepSelectionSlider.setPaintLabels(true);
			ivjStepSelectionSlider.setToolTipText("Step Selection Control");
			ivjStepSelectionSlider.setInverted(false);
			ivjStepSelectionSlider.setPaintTicks(false);
			ivjStepSelectionSlider.setValue(0);
			ivjStepSelectionSlider.setSnapToTicks(false);
			ivjStepSelectionSlider.setMinimum(0);
			ivjStepSelectionSlider.setMinimumSize(new java.awt.Dimension(36, 12));
			ivjStepSelectionSlider.setOpaque(true);
			ivjStepSelectionSlider.setBorder(new javax.swing.border.EtchedBorder());
			ivjStepSelectionSlider.setBackground(new java.awt.Color(204,204,255));
			ivjStepSelectionSlider.setForeground(java.awt.SystemColor.desktop);
			ivjStepSelectionSlider.setPaintTrack(true);
			ivjStepSelectionSlider.setPreferredSize(new java.awt.Dimension(100, 12));
			ivjStepSelectionSlider.setMaximum(2000);
			// user code begin {1}
			ivjStepSelectionSlider.setMaximum(stepLength);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStepSelectionSlider;
}
/**
 * Return the StepSelectionSlider1 property value.
 * @return javax.swing.JSlider
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JSlider getStepSelectionSlider1() {
	if (ivjStepSelectionSlider1 == null) {
		try {
			ivjStepSelectionSlider1 = new javax.swing.JSlider();
			ivjStepSelectionSlider1.setName("StepSelectionSlider1");
			ivjStepSelectionSlider1.setPaintLabels(true);
			ivjStepSelectionSlider1.setToolTipText("Step Selection Control");
			ivjStepSelectionSlider1.setInverted(false);
			ivjStepSelectionSlider1.setPaintTicks(false);
			ivjStepSelectionSlider1.setValue(0);
			ivjStepSelectionSlider1.setSnapToTicks(false);
			ivjStepSelectionSlider1.setMinimum(0);
			ivjStepSelectionSlider1.setMinimumSize(new java.awt.Dimension(36, 12));
			ivjStepSelectionSlider1.setOpaque(true);
			ivjStepSelectionSlider1.setBorder(new javax.swing.border.EtchedBorder());
			ivjStepSelectionSlider1.setBackground(new java.awt.Color(204,204,255));
			ivjStepSelectionSlider1.setForeground(java.awt.SystemColor.desktop);
			ivjStepSelectionSlider1.setPaintTrack(true);
			ivjStepSelectionSlider1.setPreferredSize(new java.awt.Dimension(100, 12));
			ivjStepSelectionSlider1.setMaximum(2000);
			// user code begin {1}
			ivjStepSelectionSlider1.setMaximum(stepLength);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjStepSelectionSlider1;
}
/**
 * Return the TextAndButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getTextAndButtonPanel() {
	if (ivjTextAndButtonPanel == null) {
		try {
			ivjTextAndButtonPanel = new javax.swing.JPanel();
			ivjTextAndButtonPanel.setName("TextAndButtonPanel");
			ivjTextAndButtonPanel.setLayout(getTextAndButtonPanelBorderLayout());
			getTextAndButtonPanel().add(getValueTextArea(), "Center");
			getTextAndButtonPanel().add(getButtonPanel(), "South");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTextAndButtonPanel;
}
/**
 * Return the TextAndButtonPanelBorderLayout property value.
 * @return java.awt.BorderLayout
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.BorderLayout getTextAndButtonPanelBorderLayout() {
	java.awt.BorderLayout ivjTextAndButtonPanelBorderLayout = null;
	try {
		/* Create part */
		ivjTextAndButtonPanelBorderLayout = new java.awt.BorderLayout();
		ivjTextAndButtonPanelBorderLayout.setVgap(2);
		ivjTextAndButtonPanelBorderLayout.setHgap(2);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjTextAndButtonPanelBorderLayout;
}
/**
 * Return the JTextArea1 property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getValueTextArea() {
	if (ivjValueTextArea == null) {
		try {
			ivjValueTextArea = new javax.swing.JTextArea();
			ivjValueTextArea.setName("ValueTextArea");
			ivjValueTextArea.setBorder(new javax.swing.border.EtchedBorder());
			ivjValueTextArea.setBackground(new java.awt.Color(204,204,204));
			ivjValueTextArea.setPreferredSize(new java.awt.Dimension(150, 250));
			ivjValueTextArea.setEditable(false);
			ivjValueTextArea.setEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueTextArea;
}
/**
 * Return the JScrollPane1 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getVariableScrollPane() {
	if (ivjVariableScrollPane == null) {
		try {
			ivjVariableScrollPane = new javax.swing.JScrollPane();
			ivjVariableScrollPane.setName("VariableScrollPane");
			ivjVariableScrollPane.setPreferredSize(new java.awt.Dimension(240, 259));
			ivjVariableScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			ivjVariableScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			ivjVariableScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			getVariableScrollPane().setViewportView(getVariableTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVariableScrollPane;
}
/**
 * Return the VariableSelectionPopupMenu property value.
 * @return javax.swing.JPopupMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPopupMenu getVariableSelectionPopupMenu() {
	if (ivjVariableSelectionPopupMenu == null) {
		try {
			ivjVariableSelectionPopupMenu = new javax.swing.JPopupMenu();
			ivjVariableSelectionPopupMenu.setName("VariableSelectionPopupMenu");
			ivjVariableSelectionPopupMenu.add(getAddConditionalBreakpointMenuItem());
			ivjVariableSelectionPopupMenu.add(getJSeparator1());
			ivjVariableSelectionPopupMenu.add(getWatchThisVariableMenuItem());
			ivjVariableSelectionPopupMenu.add(getShowLockVisualizationMenuItem());
			ivjVariableSelectionPopupMenu.add(getObjectDiagram());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjVariableSelectionPopupMenu;
}
/**
 * Return the JTree1 property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTree getVariableTree() {
	if (ivjVariableTree == null) {
		try {
			ivjVariableTree = new javax.swing.JTree();
			ivjVariableTree.setName("VariableTree");
			ivjVariableTree.setAutoscrolls(true);
			ivjVariableTree.setPreferredSize(new java.awt.Dimension(230, 250));
			ivjVariableTree.setBackground(new java.awt.Color(204,204,204));
			ivjVariableTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
               ivjVariableTree.setPreferredSize(null); // robbyjo's patch
			ivjVariableTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(Color.black);
					return this;
				}
			}.setAngledColor());
			ivjVariableTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			/*
			ivjVariableTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					setIcon(null);
					
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					if (o instanceof ValueNode) {
						ValueNode vn = (ValueNode) o;
						o = vn.object;
						if (o instanceof Local) {
							Local lcl = (Local) o;
							if ("JJJCTEMP$0".equals(lcl.getName()))
								setText("this: " + lcl.getType());
							else
								setText(lcl.getName() + ": " + lcl.getType());
						} else if (o instanceof SootField) {
							SootField sf = (SootField) o;
							setText(sf.getName() + ": " + sf.getType());
						} else {
							setText("" + vn.i);
						}
					}
					
					return this;
				}
			});
			*/
			((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setLeafIcon(null);
			((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setOpenIcon(null);
			((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setClosedIcon(null);
			((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
			((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
			ivjVariableTree.putClientProperty("JTree.lineStyle", "Angled");
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
 * Return the ViolationHintsLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getViolationHintsLabel() {
	if (ivjViolationHintsLabel == null) {
		try {
			ivjViolationHintsLabel = new javax.swing.JLabel();
			ivjViolationHintsLabel.setName("ViolationHintsLabel");
			ivjViolationHintsLabel.setText("Hints");
			ivjViolationHintsLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViolationHintsLabel;
}
/**
 * Return the ViolationHintsList property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getViolationHintsTextArea() {
	if (ivjViolationHintsTextArea == null) {
		try {
			ivjViolationHintsTextArea = new javax.swing.JTextArea();
			ivjViolationHintsTextArea.setName("ViolationHintsTextArea");
			ivjViolationHintsTextArea.setBackground(java.awt.Color.lightGray);
			ivjViolationHintsTextArea.setBounds(0, 0, 152, 90);
			ivjViolationHintsTextArea.setLineWrap(true);
			ivjViolationHintsTextArea.setWrapStyleWord(true);
			// user code begin {1}
			int violationType = traceManager.getViolationType();
			switch(violationType) {
				case TraceManager.DEADLOCK_VIOLATION:
				case TraceManager.ASSERTION_VIOLATION:
				case TraceManager.PROPERTY_VIOLATION:
				case TraceManager.LIVENESS_PROPERTY_VIOLATION:
					getViolationHintsPanel().setVisible(true);
					getViolationNamePanel().setVisible(true);
					java.util.List violationHints = traceManager.getViolationHints(violationType);
					if((violationHints != null)){
						ivjViolationHintsTextArea.setText("");
						for (int i = 0; i < violationHints.size() ; i++){
					 		ivjViolationHintsTextArea.setText(ivjViolationHintsTextArea.getText() 
					 						+	violationHints.get(i) + "\n");
					 	}
					}
					break;
				default:
					getViolationHintsPanel().setVisible(false);
					getViolationNamePanel().setVisible(false);
					break;
			}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViolationHintsTextArea;
}
/**
 * Return the ViolationHintsPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getViolationHintsPanel() {
	if (ivjViolationHintsPanel == null) {
		try {
			ivjViolationHintsPanel = new javax.swing.JPanel();
			ivjViolationHintsPanel.setName("ViolationHintsPanel");
			ivjViolationHintsPanel.setLayout(new java.awt.GridBagLayout());
			ivjViolationHintsPanel.setBackground(new java.awt.Color(204,204,255));
			
			java.awt.GridBagConstraints constraintsViolationHintsLabel = new java.awt.GridBagConstraints();
			constraintsViolationHintsLabel.gridx = 0; constraintsViolationHintsLabel.gridy = 0;
			constraintsViolationHintsLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsViolationHintsLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getViolationHintsPanel().add(getViolationHintsLabel(), constraintsViolationHintsLabel);

			java.awt.GridBagConstraints constraintsViolationHintsScrollPane = new java.awt.GridBagConstraints();
			constraintsViolationHintsScrollPane.gridx = 0; constraintsViolationHintsScrollPane.gridy = 1;
			constraintsViolationHintsScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsViolationHintsScrollPane.weightx = 1.0;
			constraintsViolationHintsScrollPane.weighty = 1.0;
			constraintsViolationHintsScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getViolationHintsPanel().add(getViolationHintsScrollPane(), constraintsViolationHintsScrollPane);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViolationHintsPanel;
}
/**
 * Return the ViolationHintsScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getViolationHintsScrollPane() {
	if (ivjViolationHintsScrollPane == null) {
		try {
			ivjViolationHintsScrollPane = new javax.swing.JScrollPane();
			ivjViolationHintsScrollPane.setName("ViolationHintsScrollPane");
			ivjViolationHintsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			ivjViolationHintsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			ivjViolationHintsScrollPane.setWheelScrollingEnabled(true);
			getViolationHintsScrollPane().setViewportView(getViolationHintsTextArea());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViolationHintsScrollPane;
}
/**
 * Return the ViolationNameLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getViolationNameLabel() {
	if (ivjViolationNameLabel == null) {
		try {
			ivjViolationNameLabel = new javax.swing.JLabel();
			ivjViolationNameLabel.setName("ViolationNameLabel");
			ivjViolationNameLabel.setText("Violation Type");
			ivjViolationNameLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViolationNameLabel;
}
/**
 * Return the ViolationNamePanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getViolationNamePanel() {
	if (ivjViolationNamePanel == null) {
		try {
			ivjViolationNamePanel = new javax.swing.JPanel();
			ivjViolationNamePanel.setName("ViolationNamePanel");
			ivjViolationNamePanel.setLayout(new java.awt.GridBagLayout());
			ivjViolationNamePanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsViolationNameLabel = new java.awt.GridBagConstraints();
			constraintsViolationNameLabel.gridx = 0; constraintsViolationNameLabel.gridy = 0;
			constraintsViolationNameLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsViolationNameLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getViolationNamePanel().add(getViolationNameLabel(), constraintsViolationNameLabel);

			java.awt.GridBagConstraints constraintsViolationNameTextField = new java.awt.GridBagConstraints();
			constraintsViolationNameTextField.gridx = 0; constraintsViolationNameTextField.gridy = 1;
			constraintsViolationNameTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsViolationNameTextField.weightx = 1.0;
			constraintsViolationNameTextField.insets = new java.awt.Insets(4, 4, 4, 4);
			getViolationNamePanel().add(getViolationNameTextField(), constraintsViolationNameTextField);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViolationNamePanel;
}
/**
 * Return the ViolationNameTextField property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getViolationNameTextField() {
	if (ivjViolationNameTextField == null) {
		try {
			ivjViolationNameTextField = new javax.swing.JTextField();
			ivjViolationNameTextField.setName("ViolationNameTextField");
			ivjViolationNameTextField.setText("<violation_type>");
			ivjViolationNameTextField.setBackground(java.awt.Color.lightGray);
			ivjViolationNameTextField.setForeground(java.awt.Color.black);
			// user code begin {1}
			String violationTypeText = null;
			int type = traceManager.getViolationType();
			switch(type) {
				case TraceManager.DEADLOCK_VIOLATION:
					violationTypeText = "Deadlock";
					break;
				case TraceManager.ASSERTION_VIOLATION:
					violationTypeText = "Assertion";
					break;
				case TraceManager.PROPERTY_VIOLATION:
					violationTypeText = "Property";
					break;
				case TraceManager.LIVENESS_PROPERTY_VIOLATION:
					violationTypeText = "Liveness Property";
					break;
			}
			
			if(violationTypeText != null) {
				ivjViolationNameTextField.setText(violationTypeText);
			}
			else {
				// hide the violation name panel and violation hints panel
			}
			
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjViolationNameTextField;
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
			ivjWatchButton.setToolTipText("Open the Watch Variable Window");
			ivjWatchButton.setMnemonic('w');
			ivjWatchButton.setText("Watch ...");
			ivjWatchButton.setBackground(new java.awt.Color(204,204,255));
			ivjWatchButton.setPreferredSize(new java.awt.Dimension(67, 22));
			ivjWatchButton.setBounds(306, 802, 67, 22);
			ivjWatchButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
			ivjWatchButton.setMinimumSize(new java.awt.Dimension(67, 22));
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
 * Return the WatchThisVariableMenuItem property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getWatchThisVariableMenuItem() {
	if (ivjWatchThisVariableMenuItem == null) {
		try {
			ivjWatchThisVariableMenuItem = new javax.swing.JMenuItem();
			ivjWatchThisVariableMenuItem.setName("WatchThisVariableMenuItem");
			ivjWatchThisVariableMenuItem.setMnemonic('w');
			ivjWatchThisVariableMenuItem.setText("Watch This Variable");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWatchThisVariableMenuItem;
}
/**
 * Return the WatchToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getWatchToolBarButton() {
	if (ivjWatchToolBarButton == null) {
		try {
			ivjWatchToolBarButton = new javax.swing.JButton();
			ivjWatchToolBarButton.setName("WatchToolBarButton");
			ivjWatchToolBarButton.setToolTipText("Open the Watch Variable Window");
			ivjWatchToolBarButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchRollover1.gif")));
			ivjWatchToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjWatchToolBarButton.setSelected(false);
			ivjWatchToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjWatchToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchPressed1.gif")));
			ivjWatchToolBarButton.setText("");
			ivjWatchToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjWatchToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjWatchToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchNormal1.gif")));
			ivjWatchToolBarButton.setBorderPainted(false);
			ivjWatchToolBarButton.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWatchToolBarButton;
}
/**
 * Return the WatchToolBarButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getWatchToolBarButton1() {
	if (ivjWatchToolBarButton1 == null) {
		try {
			ivjWatchToolBarButton1 = new javax.swing.JButton();
			ivjWatchToolBarButton1.setName("WatchToolBarButton1");
			ivjWatchToolBarButton1.setToolTipText("Open the Watch Variable Window");
			ivjWatchToolBarButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchRollover1.gif")));
			ivjWatchToolBarButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjWatchToolBarButton1.setSelected(false);
			ivjWatchToolBarButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjWatchToolBarButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchPressed1.gif")));
			ivjWatchToolBarButton1.setText("");
			ivjWatchToolBarButton1.setBackground(new java.awt.Color(204,204,255));
			ivjWatchToolBarButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjWatchToolBarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/counterexample/images/WatchNormal1.gif")));
			ivjWatchToolBarButton1.setBorderPainted(false);
			ivjWatchToolBarButton1.setRolloverEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWatchToolBarButton1;
}
/**
 * Return the WindowButtonPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getWindowButtonPanel() {
	if (ivjWindowButtonPanel == null) {
		try {
			ivjWindowButtonPanel = new javax.swing.JPanel();
			ivjWindowButtonPanel.setName("WindowButtonPanel");
			ivjWindowButtonPanel.setLayout(new java.awt.GridBagLayout());
			ivjWindowButtonPanel.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsCompleteGraphToolBarButton = new java.awt.GridBagConstraints();
			constraintsCompleteGraphToolBarButton.gridx = 0; constraintsCompleteGraphToolBarButton.gridy = 0;
			constraintsCompleteGraphToolBarButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getWindowButtonPanel().add(getCompleteGraphToolBarButton(), constraintsCompleteGraphToolBarButton);

			java.awt.GridBagConstraints constraintsOptionsToolBarButton = new java.awt.GridBagConstraints();
			constraintsOptionsToolBarButton.gridx = 1; constraintsOptionsToolBarButton.gridy = 0;
			constraintsOptionsToolBarButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getWindowButtonPanel().add(getOptionsToolBarButton(), constraintsOptionsToolBarButton);

			java.awt.GridBagConstraints constraintsWatchToolBarButton = new java.awt.GridBagConstraints();
			constraintsWatchToolBarButton.gridx = 2; constraintsWatchToolBarButton.gridy = 0;
			constraintsWatchToolBarButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getWindowButtonPanel().add(getWatchToolBarButton(), constraintsWatchToolBarButton);

			java.awt.GridBagConstraints constraintsShowMinimizedWindowButton = new java.awt.GridBagConstraints();
			constraintsShowMinimizedWindowButton.gridx = 4; constraintsShowMinimizedWindowButton.gridy = 0;
			constraintsShowMinimizedWindowButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getWindowButtonPanel().add(getShowMinimizedWindowButton(), constraintsShowMinimizedWindowButton);

			java.awt.GridBagConstraints constraintsSaveButton = new java.awt.GridBagConstraints();
			constraintsSaveButton.gridx = 3; constraintsSaveButton.gridy = 0;
			constraintsSaveButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getWindowButtonPanel().add(getSaveButton(), constraintsSaveButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjWindowButtonPanel;
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
     * Handle the popup window requests for the Store.  This will display a popup window
     * when the mouse event is a popup trigger.
     *
     * @param MouseEvent mouseEvent A mouse event that may cause a popup window to be shown.
     */
    private void handlePopupTrigger(java.awt.event.MouseEvent mouseEvent) {
	if (mouseEvent.isPopupTrigger()) {
	    // locate the closest node and select it
	    TreePath tp = getVariableTree().getClosestPathForLocation(mouseEvent.getX(), mouseEvent.getY());
	    getVariableTree().setSelectionPath(tp);
	    
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode) getVariableTree().getLastSelectedPathComponent();
	    
	    // show the popup menu only when the selected node is a ValueNode	
	    if((node != null) && (node.getUserObject() instanceof ValueNode)) {
		ValueNode vn = (ValueNode)node.getUserObject();
		if (vn.object instanceof SootField) {
		    getVariableSelectionPopupMenu().show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
		}
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
	getResetButton().addActionListener(ivjEventHandler);
	getForwardButton().addActionListener(ivjEventHandler);
	getBackButton().addActionListener(ivjEventHandler);
	getCloseButton().addActionListener(ivjEventHandler);
	getVariableTree().addTreeSelectionListener(ivjEventHandler);
	getOptionsButton().addActionListener(ivjEventHandler);
	getStepSelectionSlider().addMouseListener(ivjEventHandler);
	getStepSelectionSlider().addChangeListener(ivjEventHandler);
	getResetToolBarButton().addActionListener(ivjEventHandler);
	getBackRunToolBarButton().addActionListener(ivjEventHandler);
	getBackStepToolBarButton().addActionListener(ivjEventHandler);
	getForwardStepToolBarButton().addActionListener(ivjEventHandler);
	getForwardRunToolBarButton().addActionListener(ivjEventHandler);
	getOptionsToolBarButton().addActionListener(ivjEventHandler);
	getVariableTree().addMouseListener(ivjEventHandler);
	getAddConditionalBreakpointMenuItem().addActionListener(ivjEventHandler);
	getWatchButton().addActionListener(ivjEventHandler);
	getWatchToolBarButton().addActionListener(ivjEventHandler);
	getWatchThisVariableMenuItem().addActionListener(ivjEventHandler);
	getShowLockVisualizationMenuItem().addActionListener(ivjEventHandler);
	getCompleteGraphToolBarButton().addActionListener(ivjEventHandler);
	getObjectDiagram().addActionListener(ivjEventHandler);
	getShowMinimizedWindowButton().addActionListener(ivjEventHandler);
	this.addWindowListener(ivjEventHandler);
	getShowMaximizedWindowButton().addActionListener(ivjEventHandler);
	getBackRunToolBarButton1().addActionListener(ivjEventHandler);
	getBackStepToolBarButton1().addActionListener(ivjEventHandler);
	getResetToolBarButton1().addActionListener(ivjEventHandler);
	getForwardStepToolBarButton1().addActionListener(ivjEventHandler);
	getForwardRunToolBarButton1().addActionListener(ivjEventHandler);
	getCompleteGraphToolBarButton1().addActionListener(ivjEventHandler);
	getOptionsToolBarButton1().addActionListener(ivjEventHandler);
	getWatchToolBarButton1().addActionListener(ivjEventHandler);
	getStepSelectionSlider1().addChangeListener(ivjEventHandler);
	getStepSelectionSlider1().addMouseListener(ivjEventHandler);
	getMinimizedCounterExampleFrame().addWindowListener(ivjEventHandler);
	getSaveButton().addActionListener(ivjEventHandler);
	getSaveButton1().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("CounterExample");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(645, 388);
		setTitle("Counter Example");
		setContentPane(getCounterExampleDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	try {
		update();
	} catch (Exception e) {
        e.printStackTrace();
	}
	// user code end
}
/**
 * Jump to a specified step by using the slider
 * Creation date: (2001/12/6 AM 12:56:55)
 * @param newstep int
 */
public void jumpToStep(int newstep) {
	try {
		if (newstep != step) {
			updateButtons(false);
			for (int i = 0; i < Math.abs(newstep - step); i++) {
				if (newstep > step)
					traceManager.forward();
				else
					traceManager.back();
			}
			step = newstep;
			update();
			updateButtons(true);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}	
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		CounterExample aCounterExample;
		aCounterExample = new CounterExample(null);
		aCounterExample.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aCounterExample.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
/**
 * This method will create a new object diagram based upon the current
 * object selected in the tree.  If a diagram exists for this object
 * we will simply set it to visible and bring it to the front.
 */
public void objectDiagram_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	
	// grab the selected node from the tree and add set it to be watched in the list
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) getVariableTree().getLastSelectedPathComponent();
	ValueNode valueNode = (ValueNode)node.getUserObject();  // we assume it is a value node :)

	showObjectDiagram(valueNode);

}
/**
 * Comment
 */
public void optionsButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	options.show();
	return;
}
/**
 * Comment
 */
public void resetButton_ActionEvents() {
	try {
		updateButtons(false);
		step = 0;
		traceManager.reset();
		update();
		updateButtons(true);
		getStepSelectionSlider().setValue(0);
		getStepSelectionSlider1().setValue(0);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
/**
 * Comment
 */
public void saveButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
    java.util.List errList = traceManager.save();

    LinkedList l = new LinkedList();
    l.add("*.cet");
    JFileChooser fc = FileDialogFactory.create(l,"Counter example files");
    File fn = FileDialogFactory.display(fc, this, "Save");
    CounterExampleSaverLoader.save(fn.getAbsolutePath(), errList);
}
/**
 * Comment
 */
public void saveButton1_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	saveButton_ActionPerformed(actionEvent);
}
/**
 * Handle the popup menu item selection event that creates a lock graph based upon
 * an object (a.k.a. a rooted bipartite graph).  This will determine what node is currently
 * selected in tree and use it as the root of the graph.  If there is already a graph based upon
 * this root we will just call show upon it.
 */
public void showLockVisualizationMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	// grab the selected node from the tree and add set it to be watched in the list
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) getVariableTree().getLastSelectedPathComponent();
	ValueNode valueNode = (ValueNode)node.getUserObject();  // we assume it is a value node :)

	// check to see if this node is already represented in the collection
	RootedBipartiteGrapher rbg = null;
	boolean rootedGraphExists = false;
	for(int i = 0; i < rootedGraphers.size(); i++) {
		RootedBipartiteGrapher currentRBG = (RootedBipartiteGrapher)rootedGraphers.get(i);
		if(((ValueNode)currentRBG.getRoot()).object == valueNode.object) {
			rootedGraphExists = true;
			rbg = currentRBG;
			break;  // now break out of the loop because we found it!
		}
	}
	
	if(rootedGraphExists) {
		// already a rooted graph for this root object, just show it.
		rbg.show();
	}
	else {
		// create a new rooted graph
		rbg = new RootedBipartiteGrapher(traceManager, valueNode);
		rootedGraphers.add(rbg);
		rbg.show();
	}

}
/**
 * Show the Object Diagram for this ValueNode.  If it already exists, just update it
 * and show it to the user.  If it doesn't exist, create it and add it to our collection.
 *
 * Creation date: (2/25/2002 10:16:19 AM)
 *
 * @param valueNode edu.ksu.cis.bandera.bui.counterexample.ValueNode
 */
public void showObjectDiagram(ValueNode valueNode) {

	if(objectDiagramManagers == null) {
		log.error("objectDiagramManagers is null.  Cannot add a new object diagram in this state.");
		return;
	}

	// check to see if this node is already represented in the collection
	RootedObjectDiagramManager rootedObjectDiagramManager = null;
	/* Just take this out for testing. -tcw
	for(int i = 0; i < objectDiagramManagers.size(); i++) {
		rootedObjectDiagramManager = (RootedObjectDiagramManager)objectDiagramManagers.get(i);
		if(((ValueNode)rootedObjectDiagramManager.getRootObject()).object == valueNode.object) {
			break;
		}
		rootedObjectDiagramManager = null;
	}
	*/
	
	if(rootedObjectDiagramManager == null) {
		rootedObjectDiagramManager = new RootedObjectDiagramManager(traceManager, valueNode);
		objectDiagramManagers.add(rootedObjectDiagramManager);
	}

	// assume: rootedObjectDiagramManager != null
	
	rootedObjectDiagramManager.update();
	rootedObjectDiagramManager.show();

}
/**
 * Comment
 */
public void stepSelectionSlider_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
/*
	int step = 0;
	JFrame minimizedWindow = getMinimizedCounterExampleFrame();
	if(minimizedWindow.isVisible()) {
		step = getStepSelectionSlider1().getValue();
		getStepSelectionSlider().setValue(step);
		getSteppingLabel().setText(Integer.toString(step));
	}
	else {
		step = getStepSelectionSlider().getValue();
		getStepSelectionSlider1().setValue(step);
		getSteppingLabel1().setText(Integer.toString(step));
	}
	*/
	
	int step = getStepSelectionSlider().getValue();

	// update the other slider bar (in the minimized window)
	getStepSelectionSlider1().setValue(step);
	getSteppingLabel1().setText(Integer.toString(step));
	
	jumpToStep(step);
	return;
}
/**
 * Comment
 */
public void stepSelectionSlider_StateChanged(javax.swing.event.ChangeEvent stateChangeEvent) {
	getSteppingLabel().setText(Integer.toString(getStepSelectionSlider().getValue()));
	return;
}
/**
 * Comment
 */
public void stepSelectionSlider1_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
	int step = getStepSelectionSlider1().getValue();

	// update the other slider bar (in the maximized window)
	getStepSelectionSlider().setValue(step);
	getSteppingLabel().setText(Integer.toString(step));
	
	jumpToStep(step);
	return;
}
/**
 * Comment
 */
public void stepSelectionSlider1_StateChanged(javax.swing.event.ChangeEvent stateChangeEvent) {
	getSteppingLabel1().setText(Integer.toString(getStepSelectionSlider1().getValue()));
	return;
}
/**
 * 
 */
private void update() {
	JTree tree = getVariableTree();
	tree.setModel(traceManager.getVariableTreeModel());
	for (int i = 0; i < tree.getRowCount(); i++) {
		tree.expandRow(i);
	}

	// reset the text area to be blank
	getValueTextArea().setText("");

	// let the main BUI window have the focus
	BUI.bui.requestFocus();

	Annotation a = traceManager.getAnnotation();
	SootMethod sootMethod = null;
	if (a != null) {
	    if ((a instanceof MethodDeclarationAnnotation) ||
		(a instanceof ConstructorDeclarationAnnotation) ||
		(a instanceof FieldDeclarationAnnotation)) {
		BUI.sessionPane.select(a, a);
	    }
	    else {
		if (a instanceof ClassDeclarationAnnotation) {
		    BUI.sessionPane.select(((ClassDeclarationAnnotation) a).getSootClass(),
					   ((ClassDeclarationAnnotation) a).getSootClass());
		}
		else {
		    Annotation ta = am.getMethodAnnotationContainingAnnotation(a);
		    BUI.sessionPane.select(ta, a);
		    if (ta instanceof MethodDeclarationAnnotation) {
			sootMethod = ((MethodDeclarationAnnotation) ta).getSootMethod();
		    }
		    else {
			if (ta instanceof ConstructorDeclarationAnnotation) {
			    sootMethod = ((ConstructorDeclarationAnnotation) ta).getSootMethod();
			}
		    }
		}
	    }
	}

	//updateThreadCounterExamples();
	int activeThreadID = traceManager.getActiveThreadID();
	updateThreadCounterExamples(activeThreadID);

	// update the label that shows what step we are currently on in the maximized window
	getStepLabel().setText("Step #: " + step + " of " + stepLength);
	getStepLabel().validate();
	getStepLabel().repaint();
	
	// update the label that shows what step we are currently on in the minimized window
	getStepLabel1().setText("Step #: " + step + " of " + stepLength);
	getStepLabel1().validate();
	getStepLabel1().repaint();

	// Update the VariableWatchWindow
	// added by tcw
	if (variableWatchWindow != null)
        variableWatchWindow.update();

	// Update the CompleteBipartiteGrapher (if it exists)
	// added by tcw
	if(completeBipartiteGrapher != null) {
		completeBipartiteGrapher.update();
	}

	// Update all the RootedBipartiteGraphers
	// added by tcw
    if (rootedGraphers != null)
	for(int i = 0; i < rootedGraphers.size(); i++) {
		RootedBipartiteGrapher rbg = (RootedBipartiteGrapher)rootedGraphers.get(i);
		rbg.update();
	}

	// Update the CompleteObjectDiagramManager
	/*
	if(completeObjectDiagramManager != null) {
		completeObjectDiagramManager.update();
	}
	*/

	// Update all the RootedObjectDiagramManagers
	// added by tcw
    if (objectDiagramManagers != null)
	for(int i = 0; i < objectDiagramManagers.size(); i++) {
		RootedObjectDiagramManager rodm = (RootedObjectDiagramManager)objectDiagramManagers.get(i);
		rodm.update();
	}
}
/**
 * 
 * @param enable boolean
 */
private void updateButtons(boolean enable) {
	if (enable) {
		getResetButton().setEnabled(step != 0); // no longer actually seen! -tcw
		getResetToolBarButton().setEnabled(step != 0);
		getResetToolBarButton1().setEnabled(step != 0);

		getForwardButton().setEnabled(step != stepLength); // no longer actually seen! -tcw
		getForwardRunToolBarButton().setEnabled(step != stepLength);
		getForwardStepToolBarButton().setEnabled(step != stepLength);
		getForwardRunToolBarButton1().setEnabled(step != stepLength);
		getForwardStepToolBarButton1().setEnabled(step != stepLength);

		getBackButton().setEnabled(step != 0); // no longer actually seen! -tcw
		getBackRunToolBarButton().setEnabled(step != 0);
		getBackStepToolBarButton().setEnabled(step != 0);
		getBackRunToolBarButton1().setEnabled(step != 0);
		getBackStepToolBarButton1().setEnabled(step != 0);

		getCloseButton().setEnabled(true); // no longer actually seen! -tcw

		getWatchButton().setEnabled(true); // no longer actually seen! -tcw
		getWatchToolBarButton().setEnabled(true);
		getWatchToolBarButton1().setEnabled(true);

		getCompleteGraphToolBarButton().setEnabled(true);
		getCompleteGraphToolBarButton1().setEnabled(true);

		getOptionsButton().setEnabled(true); // no longer actually seen! -tcw
		getOptionsToolBarButton().setEnabled(true);
		getOptionsToolBarButton1().setEnabled(true);

		getShowMaximizedWindowButton().setEnabled(true);
		getShowMinimizedWindowButton().setEnabled(true);

	}
	else {
		getResetButton().setEnabled(false); // no longer actually seen! -tcw
		getResetToolBarButton().setEnabled(false);
		getResetToolBarButton1().setEnabled(false);

		getForwardButton().setEnabled(false); // no longer actually seen! -tcw
		getForwardRunToolBarButton().setEnabled(false);
		getForwardStepToolBarButton().setEnabled(false);
		getForwardRunToolBarButton1().setEnabled(false);
		getForwardStepToolBarButton1().setEnabled(false);

		getBackButton().setEnabled(false); // no longer actually seen! -tcw
		getBackRunToolBarButton().setEnabled(false);
		getBackStepToolBarButton().setEnabled(false);
		getBackRunToolBarButton1().setEnabled(false);
		getBackStepToolBarButton1().setEnabled(false);

		getCloseButton().setEnabled(false); // no longer actually seen! -tcw

		getWatchButton().setEnabled(false); // no longer actually seen! -tcw
		getWatchToolBarButton().setEnabled(false);
		getWatchToolBarButton1().setEnabled(false);

		getCompleteGraphToolBarButton().setEnabled(false);
		getCompleteGraphToolBarButton1().setEnabled(false);

		getOptionsButton().setEnabled(false); // no longer actually seen! -tcw
		getOptionsToolBarButton().setEnabled(false);
		getOptionsToolBarButton1().setEnabled(false);

		getShowMaximizedWindowButton().setEnabled(false);
		getShowMinimizedWindowButton().setEnabled(false);

	}
}
    /**
     * Update the thread counter example windows as well as creating new windows and hiding inactive windows.
     */
    private void updateThreadCounterExamples() {

	if(threadCounterExampleMap == null) {
	    threadCounterExampleMap = new HashMap();
	}

	Set threadIDSet = traceManager.getThreadIDSet();
	if((threadIDSet != null) && (threadIDSet.size() > 0)) {
	    Iterator threadIDIterator = threadIDSet.iterator();
	    while(threadIDIterator.hasNext()) {
		Integer threadID = (Integer)threadIDIterator.next();
		log.debug("updating thread counter examples: thread " + threadID);
		ThreadCounterExample tce = (ThreadCounterExample)threadCounterExampleMap.get(threadID);
		String threadClassName = traceManager.getClassName(threadID.intValue());
		if((traceManager.isAlive(threadID.intValue())) &&
		   (threadClassName != null) &&
		   (!threadClassName.equals("Unknown Class"))) {
		    if(tce == null) {
			log.debug("Creating a new thread counter example window for thread: " + threadID);
			tce = new ThreadCounterExample(this, "Thread<" + threadClassName + ">, ID: " + threadID.toString(), threadID.intValue());
			threadCounterExampleMap.put(threadID, tce);
		    }

		    // assume: tce != null

		    tce.setVariableTreeModel(traceManager.getThreadTreeModel(threadID.intValue()));
		    tce.update(traceManager.getAnnotation(threadID.intValue()));
		    tce.setVisible(true);
		}
		else {
		    log.debug("thread " + threadID + " is not alive.");
		    if(tce != null) {
			log.debug("hiding the thread counter example window for thread " + threadID);
			tce.setVisible(false);
		    }
		}
	    }
	}
    }
    /**
     * Update the thread counter example windows as well as creating new windows and hiding inactive windows.
     * The only thread counter example that will actually be updated will be the active thread given.
     */
    private void updateThreadCounterExamples(int activeThreadID) {

	if(threadCounterExampleMap == null) {
	    threadCounterExampleMap = new HashMap();
	}

	Set threadIDSet = traceManager.getThreadIDSet();
	if((threadIDSet != null) && (threadIDSet.size() > 0)) {
	    Iterator threadIDIterator = threadIDSet.iterator();
	    while(threadIDIterator.hasNext()) {
		Integer threadID = (Integer)threadIDIterator.next();
		log.debug("updating thread counter examples: thread " + threadID);
		ThreadCounterExample tce = (ThreadCounterExample)threadCounterExampleMap.get(threadID);
		String threadClassName = traceManager.getClassName(threadID.intValue());
		if((traceManager.isAlive(threadID.intValue())) &&
		   (threadClassName != null) &&
		   (!threadClassName.equals("Unknown Class"))) {
		    if(tce == null) {
			log.debug("Creating a new thread counter example window for thread: " + threadID);
			tce = new ThreadCounterExample(this, "Thread<" + threadClassName + ">, ID: " + threadID.toString(), threadID.intValue());
			threadCounterExampleMap.put(threadID, tce);
		    }

		    // assume: tce != null

		    if(threadID.intValue() == activeThreadID) {
			tce.setVariableTreeModel(traceManager.getThreadTreeModel(threadID.intValue()));
			tce.update(traceManager.getAnnotation(threadID.intValue()));
			tce.setVisible(true);
		    }
		    else {
			log.debug("Thread " + threadID + " is not active so not updating.");
		    }
		}
		else {
		    log.debug("thread " + threadID + " is not alive.");
		    if(tce != null) {
			log.debug("hiding the thread counter example window for thread " + threadID);
			tce.setVisible(false);
		    }
		    else {
			log.warn("The thread is not alive and the ThreadCounterExample is null.  This is weird.");
		    }
		}
	    }
	}
    }
    public void variableTree_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	handlePopupTrigger(mouseEvent);
    }
    public void variableTree_MousePressed(java.awt.event.MouseEvent mouseEvent) {
	handlePopupTrigger(mouseEvent);
    }
    public void variableTree_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
	handlePopupTrigger(mouseEvent);
    }

    /**
     * Handle tree selection events for the variable tree.  This will cause the value
     * associated with the selected node in the tree to be displayed in the text area
     * and the subtree (if one exists) to be generated and added for the current node.
     */
    public void variableTree_TreeSelectionEvents() {
	log.debug("Handling tree selection events ...");

	try {
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode) getVariableTree().getLastSelectedPathComponent();
	    //DefaultTreeModel model = (DefaultTreeModel) getVariableTree().getModel();
	
	    // set the value of the text area to the value associated with the selected node.
	    //String text = traceManager.getValueText(model, node);
	    String text = traceManager.getValueText(node);
	    getValueTextArea().setText(text);
	    
	    int childCount = node.getChildCount();
	    if(childCount > 0) {
		log.debug("Node model has already been expanded.  No need to do this again.  childCount = " + childCount);
		return;
	    }

	    // expand the children of the current selected node
	    java.util.List children = traceManager.getNodeChildren(node);
	    if((children != null) && (children.size() > 0)) {
		log.debug("Found " + children.size() + " children for the current node.");
		Iterator ci = children.iterator();
		while(ci.hasNext()) {
		    Object child = ci.next();
		    if((child != null) && (child instanceof MutableTreeNode)) {
			MutableTreeNode mtn = (MutableTreeNode)child;
			node.add(mtn);
		    }
		}
	    }
	    else {
		log.debug("There are no children for the node.");
	    }
	}
	catch(Exception e) {
	    log.warn("An exception occured while expanding the children of a node.", e);
	}

	log.debug("Finished handling tree selection events.");
    }

/**
 * Comment
 */
public void watchButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	variableWatchWindow.show();
}
/**
 * Comment
 */
public void watchThisVariableMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	// grab the selected node from the tree and add set it to be watched in the list
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) getVariableTree().getLastSelectedPathComponent();
	ValueNode valueNode = (ValueNode)node.getUserObject();  // we assume it is a value node :)
	variableWatchWindow.watch(valueNode);
}
}
