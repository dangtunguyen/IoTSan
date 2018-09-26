package edu.ksu.cis.bandera.pdgslicer.dependency;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;
public abstract class DroppableList extends JList implements DropTargetListener {
	DropTarget dropTarget;
	CriterionViewer criterionViewer;
  public DroppableList() {
	dropTarget = new DropTarget (this, this);
	setModel(new DefaultListModel());
  }  
public abstract void dragEnter(DropTargetDragEvent dropTargetDragEvent);
/*
{
	System.out.println("drag enter drop");
	dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
}
*/
  public void dragExit (DropTargetEvent dropTargetEvent) {
	 // System.out.println("drag exit drop");
  }  
  public void dragOver (DropTargetDragEvent dropTargetDragEvent) {
	 // System.out.println("drap over drop");
  }  
public abstract void drop(DropTargetDropEvent dropTargetDropEvent);
/*
7{
	try {
		//Transferable tr = dropTargetDropEvent.getTransferable();

		Transferable tr = DraggableTree.currentDraggingTreeNode;
		if (tr.isDataFlavorSupported(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR)) {
			dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			Object userObject = tr.getTransferData(TransferableTreeNode.DEFAULT_MUTABLE_TREENODE_FLAVOR);
			((DefaultListModel) getModel()).addElement(userObject);
			dropTargetDropEvent.getDropTargetContext().dropComplete(true);
		} else
			if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				String string = (String) tr.getTransferData(DataFlavor.stringFlavor);
				((DefaultListModel) getModel()).addElement(string);
				dropTargetDropEvent.getDropTargetContext().dropComplete(true);
			} else
				if (tr.isDataFlavorSupported(DataFlavor.plainTextFlavor)) {
					dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					Object stream = tr.getTransferData(DataFlavor.plainTextFlavor);
					if (stream instanceof InputStream) {
						InputStreamReader isr = new InputStreamReader((InputStream) stream);
						BufferedReader reader = new BufferedReader(isr);
						String line;
						while ((line = reader.readLine()) != null) {
							((DefaultListModel) getModel()).addElement(line);
						}
						dropTargetDropEvent.getDropTargetContext().dropComplete(true);
					} else
						if (stream instanceof Reader) {
							BufferedReader reader = new BufferedReader((Reader) stream);
							String line;
							while ((line = reader.readLine()) != null) {
								((DefaultListModel) getModel()).addElement(line);
							}
							dropTargetDropEvent.getDropTargetContext().dropComplete(true);
						} else {
							System.err.println("Unknown type: " + stream.getClass());
							dropTargetDropEvent.rejectDrop();
						}
				} else
					if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						List fileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
						Iterator iterator = fileList.iterator();
						while (iterator.hasNext()) {
							File file = (File) iterator.next();
							Hashtable hashtable = new Hashtable();
							hashtable.put("name", file.getName());
							hashtable.put("url", file.toURL().toString());
							((DefaultListModel) getModel()).addElement(hashtable);
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
*/
  public void dropActionChanged (DropTargetDragEvent dropTargetDragEvent) {
	  System.out.println("dropActionChanged");
  }  
/**
 * Insert the method's description here.
 * Creation date: (00-11-21 11:45:59)
 * @param cv edu.ksu.cis.bandera.pdgslicer.dependency.CriterionViewer
 */
void setCriterionViewer(CriterionViewer cv) {
	criterionViewer = cv;
}
}
