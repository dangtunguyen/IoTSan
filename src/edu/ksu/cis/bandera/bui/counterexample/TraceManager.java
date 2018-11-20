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

import edu.ksu.cis.bandera.annotation.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;

/**
 * The TraceManager is an interface that provides the ability to
 * query Counter Example trace for the current state of the system.
 * It also provides the ability to change the current state by
 * stepping back and forth in the trace.
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Yu Chen
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.4 $ - $Date: 2003/04/30 19:33:18 $
 */
public interface TraceManager {

    public static final int DEADLOCK_VIOLATION = 100;
    public static final int ASSERTION_VIOLATION = 200;
    public static final int PROPERTY_VIOLATION = 300;
    public static final int LIVENESS_PROPERTY_VIOLATION = 301;

    int getViolationType();
    String getViolationHintsText();
    List getViolationHints();
    String getViolationHintsText(int violationType);
    List getViolationHints(int violationType);

/**
 * 
 */
void back();
/**
 * 
 */
void cleanup();
/**
 * 
 */
void forward();
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 */
Annotation getAnnotation();
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 * @param threadID int
 */
Annotation getAnnotation(int threadID);
/**
 * 
 * @return int
 */
int getNumOfSteps();
/**
 * 
 * @return java.util.Vector
 */
Vector getThreadTreeModels();
/**
 * 
 * @return java.lang.String
 * @param model javax.swing.tree.DefaultTreeModel
 * @param node javax.swing.tree.DefaultMutableTreeNode
 */
String getValueText(DefaultTreeModel model, DefaultMutableTreeNode node);
/**
 * 
 * @return javax.swing.tree.TreeModel
 */
TreeModel getVariableTreeModel();
/**
 * 
 * @return boolean
 * @param threadID int
 */
public boolean isAlive(int threadID);
/**
 * 
 */
void reset();

/**
 * Check if a specified field name (full qualified) exists in the compiled classes
 * Creation date: (2001/11/3 AM 01:22:30)
 * @return boolean
 * @param fieldName java.lang.String
 */
boolean checkField(String fieldName);

/**
 * Insert the method's description here.
 * Creation date: (2001/11/5 AM 03:10:12)
 * @return java.lang.String
 * @param FieldName java.lang.String
 */
Object getFieldValue(String FieldName);

/**
 * Get the value associated with this variable.
 * Creation date: (12/11/01 2:23:24 PM)
 *
 * @return java.lang.String The value for this variable.
 * @param valueNode edu.ksu.cis.bandera.bui.counterexample.ValueNode The variable for which we want the value.
 */
String getValue(ValueNode valueNode);

/**
 * This method provides a way to update values in an ObjectTableModel.  When implemented,
 * it should make sure that the values stored in this object are up to date.
 * Creation date: (12/11/01 2:22:02 PM)
 *
 * @param objectTableModel edu.ksu.cis.bandera.bui.counterexample.ObjectTableModel
 */
void updateObjectTableModelValues(ObjectTableModel objectTableModel);

/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:26:49 PM)
 * @return java.util.List
 */
List getAllLockHoldingEdges();

/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:27:06 PM)
 * @return java.util.List
 */
List getAllLockWaitingEdges();

/**
 * Save counter example trails.
 * Should return List of Strings
 * @return java.util.List
 */
List save();

/**
 * Load counter example trails.
 * Expect a list of Strings. One element
 * on the list is one counter example trail.
 * @param l java.util.List
 * @return java.lang.Object
 */
Object load(List l);

    /**
     * Get the set of thread IDs in current state of the counter example.
     */
    Set getThreadIDSet();

    /**
     * Get the tree model for the specified thread.
     */
    TreeModel getThreadTreeModel(int threadID);

    String getClassName(int threadID);

    /**
     * Get the value associated with this node as a text String.
     *
     * @param MutableTreeNode node The node in the tree to get the value for.
     */
    String getValueText(javax.swing.tree.DefaultMutableTreeNode node);

    /**
     * Get the expansion of this node if one exists.  In other words, get
     * all the possible children associated with this node.
     *
     * @param MutableTreeNode node The node in the tree to get the value for.
     */
    List getNodeChildren(javax.swing.tree.DefaultMutableTreeNode node);

    /**
     * Get the active thread ID at this stage in the counter example.  This will
     * allow us to handle that thread a little different in the GUI.  For example,
     * we will only update that thread's counter example window.
     *
     * @return int The currnetly active thread ID.
     */
    public int getActiveThreadID();
}

