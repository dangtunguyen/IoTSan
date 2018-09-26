package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import ca.mcgill.sable.util.*;

import java.io.*;
import java.util.*;

/**
 * An enumerated type---essentially a set of named integer constants.
 */

public class Enumerated extends Type implements BirConstants {

	int minElement;
	int maxElement;
	Hashtable elementName = new Hashtable();
	Hashtable elementValue = new Hashtable();
	Vector constants = new Vector();

	/**
	 * Add a new enumeration constant (value automatically assigned to
	 * be 1 greater than existing maximum).
	 * @param name name of constant
	 * @return Constant definition 
	 */

	public Constant add(String name) {
	if (elementName.size() == 0) 
	    return add(name, 0);
	else 
	    return add(name, maxElement + 1);
	}
	/**
	 * Add a new enumeration constant with a given value.
	 * @param name name of constant
	 * @param value of constant
	 * @return Constant definition 
	 */

	public Constant add(String name, int value) {
	if (elementName.size() == 0) 
	    minElement = maxElement = value;
	else if (value > maxElement)
	    maxElement = value;
	else if (value < minElement)
	    minElement = value;
	Integer val = new Integer(value);
	elementName.put(val, name);
	elementValue.put(name, val);
	Constant constant = new Constant(name,value,this);
	constants.addElement(constant);
	return constant;
	}
	public void apply(TypeSwitch sw, Object o)
	{
		sw.caseEnumerated(this, o);
	}
	public boolean compatibleWith(Type type) {
	return this == type;
	}
	public boolean containsValue(Object value) { 
	return (value instanceof Constant) &&
	    (((Constant)value).getType() == this);
	}
	public Expr defaultVal() { 
	return new Constant(getNameOf(minElement),minElement,this);
	}
	public boolean equals(Object o) {
	if (o instanceof Enumerated) {
	    Vector o_constants = ((Enumerated)o).constants;
	    for (int i = 0; i < constants.size(); i++)
		if (! o_constants.contains(constants.elementAt(i)))
		    return false;
	    return o_constants.size() == constants.size();
	}
	return false;
	}
	/**
	 * Get Vector of enumeration Constant's.
	 * @return Vector of Constant objects for enumerated type
	 */

	public Vector getConstants() { return constants; }
	/**
	 * Get size of enumerated type---this is not the number of constants
	 * but rather the range of those constants (e.g., size {0,1,4} = 5 ).
	 * @return size of type.
	 */

	public int getEnumeratedSize() { 
	return (elementName.size() == 0) ? 0 : (maxElement - minElement) + 1;
	}
	public int getFirstElement() { return minElement; }
	public String getNameOf(int value) {
	Object name = elementName.get(new Integer(value));
	if (name != null)
	    return (String) name;
	else 
	    return null;
	}
	public int getValueOf(String name) {
	return ((Integer)elementValue.get(name)).intValue();
	}
	public boolean isKind(int kind) { 
	return (kind & ENUMERATED) != 0;
	}
	// Printing
	public String toString() {
	String range = "enum {";
	boolean first = true;
	for (int i = getFirstElement(); i < getEnumeratedSize(); i++) {
	    String name = (String) elementName.get(new Integer(i));
	    if (name != null) {
		if (! first) 
		    range += ",";
		else
		    first = false;
		range += name + "=" + i;
	    }
	}
	return range + "}";
	}
}
