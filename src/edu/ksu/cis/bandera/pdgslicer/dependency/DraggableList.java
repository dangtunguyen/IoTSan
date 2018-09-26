package edu.ksu.cis.bandera.pdgslicer.dependency;

/**
 * This is an example of a component, which serves as a DragSource as 
 * well as Drop Target.
 * To illustrate the concept, JList has been used as a droppable target
 * and a draggable source.
 * Any component can be used instead of a JList.
 * The code also contains debugging messages which can be used for 
 * diagnostics and understanding the flow of events.
 * 
 * @version 1.0
 */

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import java.io.*;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.DefaultListModel;




public abstract class DraggableList extends JList
	implements DragSourceListener, DragGestureListener    {


  /**
   * enables this component to be a Drag Source
   */
  DragSource dragSource = null;

	static Transferable currentDraggingListNode = null;
/**
   * constructor - initializes the DropTarget and DragSource.
   */

public DraggableList() {
	dragSource = new DragSource();
	setModel(new DefaultListModel());
	dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
}
  /**
   * this message goes to DragSourceListener, informing it that the dragging 
   * has ended
   * 
   */

  public void dragDropEnd (DragSourceDropEvent event) {
	  /*
	if ( event.getDropSuccess()){
		removeElement();
	}
	*/
	currentDraggingListNode = null;
  }  
  /**
   * this message goes to DragSourceListener, informing it that the dragging 
   * has entered the DropSite
   * 
   */

  public void dragEnter (DragSourceDragEvent event) {
	//System.out.println( " dragEnter");
  }  
  /**
   * this message goes to DragSourceListener, informing it that the dragging 
   * has exited the DropSite
   * 
   */

  public void dragExit (DragSourceEvent event) {
	//System.out.println( "dragExit");
	currentDraggingListNode = null;
	
  }  
/**
   * a drag gesture has been initiated
   * 
   */

public abstract void dragGestureRecognized(DragGestureEvent event);
/*
{
	SliceVariable selected = (SliceVariable) getSelectedValue();
	if (selected != null) {
		currentDraggingListNode = new TransferableSliceVariable(selected);
		//StringSelection text = new StringSelection(selected.toString());

		// as the name suggests, starts the dragging
		dragSource.startDrag(event, DragSource.DefaultCopyDrop, currentDraggingListNode, this);
	} else {
		System.out.println("nothing was selected");
	}
}
*/
  /**
   * this message goes to DragSourceListener, informing it that the dragging is currently 
   * ocurring over the DropSite
   * 
   */

  public void dragOver (DragSourceDragEvent event) {
	//System.out.println( "dragExit");
	
  }  
  /**
   * is invoked when the user changes the dropAction
   * 
   */
   
  public void dropActionChanged ( DragSourceDragEvent event) {
	System.out.println( "dropActionChanged"); 
  }  
}
