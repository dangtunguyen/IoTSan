package edu.ksu.cis.bandera.pdgslicer.dependency;

import javax.swing.*;
import javax.swing.tree.*;
//import javax.swing.plaf.TreeUI;
//import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
//import java.io.IOException;
/**
 * Insert the type's description here.
 * Creation date: (00-11-16 14:21:59)
 * @author: 
 */
public class DraggableMethodTree extends DraggableTree {

/**
 * MethodTree constructor comment.
 */
public DraggableMethodTree() {
	super();
}
/**
 * MethodTree constructor comment.
 * @param model javax.swing.tree.TreeModel
 */
public DraggableMethodTree(javax.swing.tree.TreeModel model) {
	super(model);
}
// DragGestureListener

public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
	TreePath path = getSelectionPath();
	if (path == null) {
		// Nothing selected, nothing to drag
		currentDraggingTreeNode = null;
		draggingTreeNode = false;
		System.out.println("Nothing selected - beep");
		getToolkit().beep();
	} else {
		DefaultMutableTreeNode selection = (DefaultMutableTreeNode) path.getLastPathComponent();
		currentDraggingTreeNode = new TransferableTreeNode(selection);
		draggingTreeNode = true;
		dragSource.startDrag(dragGestureEvent, DragSource.DefaultCopyDrop, currentDraggingTreeNode, DraggableMethodTree.this.dragSourceListener);
	}
}
}
