package edu.ksu.cis.bandera.bui.wizard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.*;
import org.apache.log4j.*;

/**
 * Insert the type's description here.
 * Creation date: (4/29/2002 1:57:16 PM)
 * @author: 
 */
public final class QuantificationCalculator {
	private java.util.Map typeMap;
	private final static Category log = Category.getInstance(QuantificationCalculator.class);
/**
 * QuantificationCalculator constructor comment.
 */
public QuantificationCalculator() {
	super();
	typeMap = new HashMap();
}
/**
 * Add this variable name and type to the quantification that is being
 * generated.
 *
 * @param variableName java.lang.String
 * @param variableType java.lang.String
 */
public void add(String variableName, String variableType) {

	if(variableName == null) {
		return;
	}

	if(variableType == null) {
		return;
	}

	if(typeMap == null) {
		return;
	}

	// see if the map contains this type already, if not, create a new set and
	//  add it to the map.  if so, just add this variable name to the current set.
	if(typeMap.containsKey(variableType)) {
		Set variableSet = (Set)typeMap.get(variableType);
		variableSet.add(variableName);
	}
	else {
		Set variableSet = new HashSet();
		variableSet.add(variableName);
		typeMap.put(variableType, variableSet);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (4/29/2002 2:02:04 PM)
 * @return java.lang.String
 */
public String getQuantification() {

	if(typeMap == null) {
		return(null);
	}

	StringBuffer quantification = new StringBuffer();

	/*
	 * for each type in the map, generate a comma seperated list of variables that have the
	 * same type.  then put it inside a forall with the type at the end:
	 * forall[ x, y, z : Type]
	 */
	Set keySet = typeMap.keySet();
	Iterator iterator = keySet.iterator();
	while(iterator.hasNext()) {
	quantification.append("forall[");
		String currentType = (String)iterator.next();
		Set currentVariableSet = (Set)typeMap.get(currentType);
		Iterator variableIterator = currentVariableSet.iterator();
		while(variableIterator.hasNext()) {
			String currentVariableName = (String)variableIterator.next();
			quantification.append(currentVariableName);
			if(variableIterator.hasNext()) {
				quantification.append(", ");
			}
		}
		quantification.append(" : " + currentType + "]");

		if(iterator.hasNext()) {
			quantification.append(".");
		}
	}

	return(quantification.toString());
}
/**
 * Insert the method's description here.
 *
 * @return java.util.List
 * @param type java.lang.String
 */
public List getVariableNameList(String type) {

	ArrayList variableNameList = null;
	
	if(typeMap.containsKey(type)) {
		Set variableSet = (Set)typeMap.get(type);
		variableNameList = new ArrayList(variableSet);
	}
	else {
		log.warn("There is no variable name list for this type (" + type + ").  Returning a null list.");
	}
	
	return(null);
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {

	// a simple test of the calculator
	QuantificationCalculator qc = new QuantificationCalculator();
	qc.add("a1", "Able");
	qc.add("b1", "BoundedBuffer");
	qc.add("b2", "BoundedBuffer");
	qc.add("c1", "Cat");
	qc.add("c2", "Cat");
	qc.add("c3", "Cat");
	System.out.println("quantification = " + qc.getQuantification());

}
/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	return(getQuantification());
}
}
