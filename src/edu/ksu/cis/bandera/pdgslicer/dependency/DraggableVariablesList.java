package edu.ksu.cis.bandera.pdgslicer.dependency;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import java.io.*;
import java.io.IOException;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
/**
 * Insert the type's description here.
 * Creation date: (00-11-20 10:02:34)
 * @author: 
 */
public class DraggableVariablesList extends DraggableList {
/**
 * DraggableVariablesList constructor comment.
 */
public DraggableVariablesList() {
	super();
}
/**
   * a drag gesture has been initiated
   * 
   */

public void dragGestureRecognized(DragGestureEvent event) {
	SliceVariable selected = (SliceVariable) getSelectedValue();
	if (selected != null) {
		currentDraggingListNode = new TransferableSliceVariable(selected);
		dragSource.startDrag(event, DragSource.DefaultCopyDrop, currentDraggingListNode, this);
	} else {
		System.out.println("nothing was selected");
	}
}
}
