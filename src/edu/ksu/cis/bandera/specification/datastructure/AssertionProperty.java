package edu.ksu.cis.bandera.specification.datastructure;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import java.util.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;

public class AssertionProperty implements Comparable {
	private String name;
	private TreeSet assertions = new TreeSet();
	private String description;
/**
 * 
 * @param name java.lang.String
 */
public AssertionProperty(String name) {
	this.name = name;
}
/**
 * 
 * @param name edu.ksu.cis.bandera.symboltable.Name
 */
public void addAssertion(Name name) {
	assertions.add(name);
}
/**
 * 
 * @return int
 * @param o java.lang.Object
 */
public int compareTo(Object o) {
	if (o instanceof AssertionProperty) {
		return name.compareTo(((AssertionProperty) o).name);
	} else {
		return -1;
	}
}
/**
 * 
 * @return java.util.TreeSet
 */
public java.util.TreeSet getAssertions() {
	return assertions;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * 
 * @return java.lang.String
 */
public String getStringRepresentation() {
	StringBuffer buffer = new StringBuffer(name + ": enable assertions { ");
	
	Iterator i = assertions.iterator();
	if (i.hasNext()) {
		buffer.append(i.next());
	}
	while (i.hasNext()) {
		buffer.append(", " + i.next());
	}
	buffer.append(" };");
	
	return buffer.toString();
}
/**
 * 
 * @param edu.ksu.cis.bandera.symboltable.Name
 */
public void removeAssertion(Name name) {
	assertions.remove(name);
}
/**
 * 
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * 
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
}
