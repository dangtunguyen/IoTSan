package edu.ksu.cis.bandera.bui.counterexample.objectdiagram;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.util.Iterator;
import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (1/17/02 5:42:20 PM)
 * @author: 
 */
public class ObjectData {
	private String name;
	private String type;
	private ArrayList references;
	private java.util.Vector fieldsVector;
	private java.lang.Object value;
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 4:16:52 PM)
 */
public ObjectData(String arrayType) {
	references = new ArrayList();
	fieldsVector = new Vector();
	value = null;
	
	setName("Array");
	setType(arrayType);
}
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 3:32:52 PM)
 * @param name java.lang.String
 * @param type java.lang.String
 */
public ObjectData(String name, String type) {

	value = null;	
	fieldsVector = new Vector();
	references = new ArrayList();
	setName(name);
	setType(type);

}
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 3:37:01 PM)
 * @param name java.lang.String
 * @param type java.lang.Class
 * @param value java.lang.Object
 */
public void addField(String name, String type, Object value) {

	this.value = null;
	
	Vector fieldVector = new Vector(3);
	fieldVector.add(name);
	fieldVector.add(type);
	fieldVector.add(value);
	fieldsVector.add(fieldVector);

	if(value instanceof ObjectData) {
		references.add(value);
	}
}
/**
 * Check the equality of this object with another object.  For them to be equal, the
 * given object must be of type ObjectData and they must have the same name.
 *
 * Creation date: (2/11/2002 10:36:05 PM)
 *
 * @return boolean True if the names are equal, false otherwise.
 * @param Object object The object to compare this object to.
 */
public boolean equals(Object object) {

	if(object == null) {
		return(false);
	}

	if(!(object instanceof ObjectData)) {
		return(false);
	}

	ObjectData objectData = (ObjectData)object;
	String objectDataName = objectData.getName();
	if((objectDataName != null) && (objectDataName.equals(getName()))) {
		return(true);
	}

	return(false);
}
/**
 * Generate a TableModel based upon the fields in this Object.
 *
 * Creation date: (1/17/02 5:45:02 PM)
 *
 * @return TableModel
 */
public TableModel getFields() {
	
	// if there is no value set and it isn't an array or an object, use
	//  the string with a value of "null"
	if((value == null) && (fieldsVector.size() <= 0)) {
		DefaultTableModel dtm = new DefaultTableModel(1, 1);

		dtm.setValueAt("<null>", 0, 0);

		return(dtm);
	}

	// if there is a value set for this object, use it
	if(value != null) {

		DefaultTableModel dtm = new DefaultTableModel(1, 1);

		dtm.setValueAt(value, 0, 0);

		return(dtm);
	}

	// if this is an array, just show the index and value
	if(getName().equals("Array")) {
		DefaultTableModel dtm = new DefaultTableModel(fieldsVector.size() + 1, 2);

		dtm.setValueAt("Index", 0, 0);
		dtm.setValueAt("Value", 0, 1);

		for(int i = 0; i < fieldsVector.size(); i++) {
			Object temp = fieldsVector.get(i);
			if((temp != null) && (temp instanceof Vector)) {
				Vector tempVector = (Vector)temp;
				dtm.setValueAt(tempVector.get(0), i + 1, 0);
				dtm.setValueAt(tempVector.get(2), i + 1, 1);
			}
		}

		return(dtm);
	}

	// otherwise, we will treat it as an object and print the fields (name, type, and value)
	DefaultTableModel dtm = new DefaultTableModel(fieldsVector.size() + 1, 3);

	// the setting of the column names should be done when the vector is created. -tcw
	dtm.setValueAt("Name", 0, 0);
	dtm.setValueAt("Type", 0, 1);
	dtm.setValueAt("Value", 0, 2);
	
	for(int i = 0; i < fieldsVector.size(); i++) {
		Object temp = fieldsVector.get(i);
		if((temp != null) && (temp instanceof Vector)) {
			Vector tempVector = (Vector)temp;
			dtm.setValueAt(tempVector.get(0), i + 1, 0);
			dtm.setValueAt(tempVector.get(1), i + 1, 1);
			dtm.setValueAt(tempVector.get(2), i + 1, 2);
		}
	}
	
	return(dtm);
}
/**
 * Insert the method's description here.
 * Creation date: (1/17/02 5:43:08 PM)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * Insert the method's description here.
 * Creation date: (1/18/02 10:17:29 AM)
 * @return java.util.Iterator
 */
public Iterator getReferences() {
	return(references.iterator());
}
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 3:31:16 PM)
 * @return java.lang.String
 */
public String getType() {
	return(type);
}
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 8:30:59 PM)
 * @return java.lang.Object
 */
public Object getValue() {
	return(value);
}
/**
 * Insert the method's description here.
 * Creation date: (1/18/02 10:15:27 AM)
 * @return boolean
 */
public boolean hasReferences() {
	return(references.size() > 0);
}
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 4:17:49 PM)
 * @return boolean
 */
public boolean isArray() {
	return(getName().equals("Array"));
}
/**
 * Insert the method's description here.
 * Creation date: (1/17/02 5:43:08 PM)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 3:35:01 PM)
 * @param type java.lang.String
 */
public void setType(String type) {
	this.type = type;
}
/**
 * Insert the method's description here.
 * Creation date: (1/28/02 8:30:47 PM)
 * @param value java.lang.Object
 */
public void setValue(Object value) {
	this.value = value;
}
/**
 * Create a String representation of this ObjectData object.  This will print
 * the name and type in this form:
 * <name>:<type>
 *
 * Creation date: (2/6/2002 4:31:26 PM)
 *
 * @return java.lang.String
 */
public String toString() {
	
	// this should probably try to print the value and fields. -tcw
	
	return(name + " : " + type);
}
}
