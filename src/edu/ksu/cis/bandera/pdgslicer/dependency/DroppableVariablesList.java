package edu.ksu.cis.bandera.pdgslicer.dependency;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;
//import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
/**
 * Insert the type's description here.
 * Creation date: (00-11-20 10:03:05)
 * @author: 
 */
public class DroppableVariablesList extends DroppableList {
/**
 * DroppableVariablesList constructor comment.
 */
public DroppableVariablesList() {
	super();
}
public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
	Transferable tr = DraggableTree.currentDraggingTreeNode;
	if (tr == null) {
		dropTargetDragEvent.rejectDrag();
	} else if (DraggableTree.draggingTreeNode) {
		Object userObject = null;
		try {
			userObject = tr.getTransferData(TransferableSliceVariable.SLICE_VARIABLE_FLAVOR);
		} catch (IOException io) {
			io.printStackTrace();
			dropTargetDragEvent.rejectDrag();
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
			dropTargetDragEvent.rejectDrag();
		}
		if (userObject instanceof SliceVariable) {
			dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
			return;
		} else
			dropTargetDragEvent.rejectDrag();
}
Object listNode = DraggableList.currentDraggingListNode;
if (listNode == null)
	dropTargetDragEvent.rejectDrag();
else
	dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
}
public void drop(DropTargetDropEvent dropTargetDropEvent) {
	try {
		Transferable tr = DraggableTree.currentDraggingTreeNode;
		if (tr == null || !DraggableTree.draggingTreeNode)
			tr = DraggableList.currentDraggingListNode;
		if (tr.isDataFlavorSupported(TransferableSliceVariable.SLICE_VARIABLE_FLAVOR)) {
			dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			Object userObject = tr.getTransferData(TransferableSliceVariable.SLICE_VARIABLE_FLAVOR);
			criterionViewer.addVarToCriterion(userObject);
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
