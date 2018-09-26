package edu.ksu.cis.bandera.pdgslicer.dependency;

import javax.swing.tree.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;

public class TransferableSliceVariable extends SliceVariable implements Transferable {
  final static int SLICE_VARIABLE = 0;



  final public static DataFlavor SLICE_VARIABLE_FLAVOR = 
	new DataFlavor(SliceVariable.class, "Slice Variable");

  static DataFlavor flavors[] = {SLICE_VARIABLE_FLAVOR};
  //, DataFlavor.stringFlavor, DataFlavor.plainTextFlavor};
//  static DataFlavor flavors[] = {DEFAULT_MUTABLE_TREENODE_FLAVOR}; // works fine

  private SliceVariable data;
						   
  public TransferableSliceVariable(SliceVariable data) {
	this.data = data;
  }  
public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
	//Object returnObject;
	if (isDataFlavorSupported(flavor))
		return this.data;
	return null;
}
  public DataFlavor[] getTransferDataFlavors() {
   return flavors;
  }  
public boolean isDataFlavorSupported(DataFlavor flavor) {
	boolean returnValue = false;
	for (int i = 0, n = flavors.length; i < n; i++) {
		if (flavor.equals(flavors[i])) {
			returnValue = true;
			break;
		}
	}
	return returnValue;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-21 10:24:54)
 * @return java.lang.String
 */
public String toString() {
	return data.toString();
}
}
