package edu.ksu.cis.bandera.pdgslicer.dependency;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
//import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
/**
 * Insert the type's description here.
 * Creation date: (00-11-16 14:21:20)
 * @author: 
 */
public class DraggableHierTree extends DraggableTree {
/**
 * HierTree constructor comment.
 */
public DraggableHierTree() {
	super();
}
/**
 * HierTree constructor comment.
 * @param model javax.swing.tree.TreeModel
 */
public DraggableHierTree(javax.swing.tree.TreeModel model) {
	//super(model);
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
		Object userObject = selection.getUserObject();
		if (userObject instanceof FieldDeclarationAnnotation) {
			FieldDeclarationAnnotation fda = (FieldDeclarationAnnotation) userObject;
			SliceVariable sf = new SliceField(fda.getSootField().getDeclaringClass(), fda.getSootField());
			currentDraggingTreeNode = new TransferableSliceVariable(sf);
			draggingTreeNode = true;
			dragSource.startDrag(dragGestureEvent, DragSource.DefaultCopyDrop, currentDraggingTreeNode, DraggableHierTree.this.dragSourceListener);
		} else {
			System.out.println("Select wrong node - beep");
			currentDraggingTreeNode = null;
			draggingTreeNode = false;
			getToolkit().beep();
		}
	}
}
}
