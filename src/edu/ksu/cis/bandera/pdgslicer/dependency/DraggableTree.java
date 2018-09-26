package edu.ksu.cis.bandera.pdgslicer.dependency;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.plaf.TreeUI;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.IOException;
public abstract class DraggableTree extends JTree implements DragGestureListener {
	static Transferable currentDraggingTreeNode;
	static boolean draggingTreeNode;
	DragSource dragSource = DragSource.getDefaultDragSource();
	DragSourceListener dragSourceListener = new MyDragSourceListener();
	class MyDragSourceListener implements DragSourceListener {
		public void dragDropEnd(DragSourceDropEvent DragSourceDropEvent) {
			//System.out.println("Drag Drop End");
			//currentDraggingTreeNode = null;
			draggingTreeNode = false;
		}
		public void dragEnter(DragSourceDragEvent DragSourceDragEvent) {
			//      System.out.println ("Drag Enter");
		}
		public void dragExit(DragSourceEvent DragSourceEvent) {
			//System.out.println("Drag Exit");
			//currentDraggingTreeNode = null;
			draggingTreeNode=false;
		}
		public void dragOver(DragSourceDragEvent DragSourceDragEvent) {
			//      System.out.println ("Drag Over");
		}
		public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent) {
			//      System.out.println ("Drag Action Changed");
		}
	}
/*
TreePath path = getSelectionPath();
if (path == null) {
// Nothing selected, nothing to drag
System.out.println("Nothing selected - beep");
getToolkit().beep();
} else {
DefaultMutableTreeNode selection = (DefaultMutableTreeNode) path.getLastPathComponent();
synchronized (this) {
currentDraggingTreeNode = new TransferableTreeNode(selection);
System.out.println("currentDraggingTreeNode: " + currentDraggingTreeNode);
}
dragSource.startDrag(dragGestureEvent, DragSource.DefaultCopyDrop, currentDraggingTreeNode, dragSourceListener);
}
}
*/
  public DraggableTree () {
	dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
  }  
  public DraggableTree (TreeModel model) {
	super (model);
	dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
  }  
// DragGestureListener

public abstract void dragGestureRecognized(DragGestureEvent dragGestureEvent) 
;
}
