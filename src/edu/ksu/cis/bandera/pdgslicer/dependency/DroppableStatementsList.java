package edu.ksu.cis.bandera.pdgslicer.dependency;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.SlicePoint;
/**
 * Insert the type's description here.
 * Creation date: (00-11-20 10:01:06)
 * @author: 
 */
public class DroppableStatementsList extends DroppableList {
/**
 * DroppableStatementList constructor comment.
 */
public DroppableStatementsList() {
	super();
}
public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
	Transferable tr = DraggableTree.currentDraggingTreeNode;
	if (tr == null) {
		dropTargetDragEvent.rejectDrag();
		return;
	}
	Object userObject = null;
	try {
		userObject = tr.getTransferData(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR);
	} catch (IOException io) {
		io.printStackTrace();
		dropTargetDragEvent.rejectDrag();
	} catch (UnsupportedFlavorException ufe) {
		ufe.printStackTrace();
		dropTargetDragEvent.rejectDrag();
	}
	if (userObject instanceof FieldDeclarationAnnotation)
		dropTargetDragEvent.rejectDrag();
	else
		dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
}
public void drop(DropTargetDropEvent dropTargetDropEvent) {
	try {
		Transferable tr = DraggableTree.currentDraggingTreeNode;
		if (tr.isDataFlavorSupported(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR)) {
			dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			Object userObject = tr.getTransferData(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR);
			SlicePoint[] sp = criterionViewer.calculateSliceStatements(userObject);
			boolean isStatementNode = true;
			for (int i = 0; i < sp.length; i++) {
				criterionViewer.addStatementToCriterion(sp[i], sp, (Annotation) userObject);
			}
			dropTargetDropEvent.getDropTargetContext().dropComplete(true);
		} else {
			System.err.println("Rejected");
			dropTargetDropEvent.rejectDrop();
		}
	} catch (IOException io) {
		io.printStackTrace();
		dropTargetDropEvent.rejectDrop();
	} catch (UnsupportedFlavorException ufe) {
		ufe.printStackTrace();
		dropTargetDropEvent.rejectDrop();
	}
}
}
