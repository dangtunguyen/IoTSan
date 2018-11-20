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
import java.util.*;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;


public class Property {
	private String filename = "";
	private TreeSet importedType = new TreeSet();
	private TreeSet importedPackage = new TreeSet();
	private TreeSet importedPredicate = new TreeSet();
	private TreeSet importedPredicateSet = new TreeSet();
	private TreeSet importedAssertion = new TreeSet();
	private TreeSet importedAssertionSet = new TreeSet();
	private Hashtable assertionPropertyTable = new Hashtable();
	private TreeSet activatedAssertionProperty = new TreeSet();
	private Hashtable temporalLogicPropertyTable = new Hashtable();
	private TemporalLogicProperty activatedTemporalLogicProperty;
/**
 * 
 * @param ap edu.ksu.cis.bandera.specification.datastructure.AssertionProperty
 */
public void activateOrDeactivateAssertionProperty(AssertionProperty ap) {
	if (!activatedAssertionProperty.contains(ap)) {
		activatedAssertionProperty.add(ap);
	} else {
		activatedAssertionProperty.remove(ap);
	}
}
/**
 * 
 * @param tlp edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty
 */
public void activateOrDeactivateTemporalLogicProperty(TemporalLogicProperty tlp) {
	if (tlp == activatedTemporalLogicProperty)
		activatedTemporalLogicProperty = null;
	else
		activatedTemporalLogicProperty = tlp;
}
/**
 * 
 * @param ap edu.ksu.cis.bandera.specification.datastructure.AssertionProperty
 */
public void addAssertionProperty(AssertionProperty ap) {
	assertionPropertyTable.put(ap.getName(), ap);
}
/**
 * 
 * @param tlp edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty
 */
public void addTemporalLogicProperty(TemporalLogicProperty tlp) {
	temporalLogicPropertyTable.put(tlp.getName(), tlp);
}
/**
 * 
 * @return java.util.TreeSet
 */
public TreeSet getActivatedAssertionProperties() {
	return activatedAssertionProperty;
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty
 */
public TemporalLogicProperty getActivatedTemporalLogicProperty() {
	return activatedTemporalLogicProperty;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getAssertionProperties() {
	TreeSet ts = new TreeSet();
	for (Enumeration e = assertionPropertyTable.elements(); e.hasMoreElements();) {
		ts.add(e.nextElement());
	}
	return new Vector(ts);
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.datastructure.AssertionProperty
 * @param name java.lang.String
 */
public AssertionProperty getAssertionProperty(String name) {
	return (AssertionProperty) assertionPropertyTable.get(name);
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getFilename() {
	return filename;
}
/**
 * 
 * @return java.util.TreeSet
 */
public java.util.TreeSet getImportedAssertion() {
	return importedAssertion;
}
/**
 * 
 * @return java.util.TreeSet
 */
public java.util.TreeSet getImportedAssertionSet() {
	return importedAssertionSet;
}
/**
 * 
 * @return java.util.TreeSet
 */
public java.util.TreeSet getImportedPackage() {
	return importedPackage;
}
/**
 * 
 * @return java.util.TreeSet
 */
public java.util.TreeSet getImportedPredicate() {
	return importedPredicate;
}
/**
 * 
 * @return java.util.TreeSet
 */
public java.util.TreeSet getImportedPredicateSet() {
	return importedPredicateSet;
}
/**
 * 
 * @return java.util.TreeSet
 */
public java.util.TreeSet getImportedType() {
	return importedType;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getTemporalLogicProperties() {
	TreeSet ts = new TreeSet();
	for (Enumeration e = temporalLogicPropertyTable.elements(); e.hasMoreElements();) {
		ts.add(e.nextElement());
	}
	return new Vector(ts);
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty
 * @param name java.lang.String
 */
public TemporalLogicProperty getTemporalLogicProperty(String name) {
	return (TemporalLogicProperty) temporalLogicPropertyTable.get(name);
}
/**
 * 
 * @return boolean
 * @param name java.lang.String
 */
public boolean hasAssertionProperty(String name) {
	return assertionPropertyTable.get(name) != null;
}
/**
 * 
 * @return boolean
 * @param name java.lang.String
 */
public boolean hasTemporalLogicProperty(String name) {
	return temporalLogicPropertyTable.get(name) != null;
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importAssertion(Name name) {
	importedAssertion.add(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importAssertionSet(Name name) {
	importedAssertionSet.add(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importPackage(Name name) {
	importedPackage.add(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importPredicate(Name name) {
	importedPredicate.add(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importPredicateSet(Name name) {
	importedPredicateSet.add(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importType(Name name) {
	importedType.add(name);
}
/**
 *
 * @return boolean
 * @param ap edu.ksu.cis.bandera.specification.datastructure.AssertionProperty
 */
public boolean isActivated(AssertionProperty ap) {
	return activatedAssertionProperty.contains(ap);
}
/**
 * 
 * @return boolean
 * @param tlp edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty
 */
public boolean isActivated(TemporalLogicProperty tlp) {
	return activatedTemporalLogicProperty == tlp;
}
/**
 * 
 * @param ap edu.ksu.cis.bandera.specification.datastructure.AssertionProperty
 */
public void removeAssertionProperty(AssertionProperty ap) {
	for (Enumeration e = assertionPropertyTable.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		Object value = assertionPropertyTable.get(key);
		if (value == ap) {
			assertionPropertyTable.remove(key);
			break;
		}
	}
	activatedAssertionProperty.remove(ap);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void removeImportedAssertion(Name name) {
	importedAssertion.remove(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void removeImportedAssertionSet(Name name) {
	importedAssertionSet.remove(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void removeImportedPackage(Name name) {
	importedPackage.remove(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void removeImportedPredicate(Name name) {
	importedPredicate.remove(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void removeImportedPredicateSet(Name name) {
	importedPredicateSet.remove(name);
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void removeImportedType(Name name) {
	importedType.remove(name);
}
/**
 * 
 * @param tlp edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty
 */
public void removeTemporalLogicProperty(TemporalLogicProperty tlp) {
	for (Enumeration e = temporalLogicPropertyTable.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		Object value = temporalLogicPropertyTable.get(key);
		if (value == tlp) {
			temporalLogicPropertyTable.remove(key);
			break;
		}
	}
	if (activatedTemporalLogicProperty == tlp)
		activatedTemporalLogicProperty = null;
}
/**
 * 
 * @param newFilename java.lang.String
 */
public void setFilename(java.lang.String newFilename) {
	filename = newFilename;
}
}
