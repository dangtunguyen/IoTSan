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
import edu.ksu.cis.bandera.specification.ast.*;
import java.io.*;
import edu.ksu.cis.bandera.specification.lexer.*;
import edu.ksu.cis.bandera.specification.parser.*;
import edu.ksu.cis.bandera.specification.node.*;
import edu.ksu.cis.bandera.specification.pattern.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import java.util.*;

public class TemporalLogicProperty implements Comparable {
	private String name;
	private String quantifier = "";
	private String patternName;
	private String patternScope;
	private Hashtable propTable = new Hashtable();
	private Vector exceptions = new Vector();
	private Hashtable tlPredicates;
	private Hashtable tlQuantifiers;
	private Hashtable pqTable;
	private Hashtable tlQuantifierConstraints;
	private Node node;
	private Vector quantifiedVariables;
/**
 * 
 * @param name java.lang.String
 */
public TemporalLogicProperty(String name) {
	this.name = name;
}
/**
 * compareTo method comment.
 */
public int compareTo(Object o) {
	if (o instanceof TemporalLogicProperty) {
		return name.compareTo(((TemporalLogicProperty) o).name);
	} else {
		return -1;
	}
}
/**
 * 
 * @return java.lang.String
 */
public String expand() {
	Pattern p = PatternSaverLoader.getPattern(patternName, patternScope);
	if (p == null) return null;
	
	StringBuffer buffer = new StringBuffer(quantifier);
	buffer.append(p.expandFormat(propTable) + ";");
	return buffer.toString();
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getExceptions() {
	return exceptions;
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
 * @return edu.ksu.cis.bandera.specification.node.Node
 */
public edu.ksu.cis.bandera.specification.node.Node getNode() {
	return node;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getPatternName() {
	return patternName;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getPatternScope() {
	return patternScope;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getPredicateQuantifierTable() {
	return pqTable;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getPredicatesTable() {
	return tlPredicates;
}
/**
 * 
 * @return java.lang.String
 * @param propName java.lang.String
 */
public String getProposition(String propName) {
	return (String) propTable.get(propName);
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getQuantifiedVariables() {
	return quantifiedVariables;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getQuantifier() {
	return quantifier;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getQuantifiersTable() {
	return tlQuantifiers;
}
/**
 * 
 * @return java.lang.String
 */
public String getStringRepresentation() {
	return name + ": " + expand();
}
/**
 * 
 * @param propName java.lang.String
 * @param exp java.lang.String
 */
public void putProposition(String propName, String exp) {
	propTable.put(propName, exp);
}
/**
 * 
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * 
 * @param newNode edu.ksu.cis.bandera.specification.node.Node
 */
public void setNode(edu.ksu.cis.bandera.specification.node.Node newNode) {
	node = newNode;
}
/**
 * 
 * @param newPatternName java.lang.String
 */
public void setPatternName(java.lang.String newPatternName) {
	patternName = newPatternName;
}
/**
 * 
 * @param newPatternScope java.lang.String
 */
public void setPatternScope(java.lang.String newPatternScope) {
	patternScope = newPatternScope;
}
/**
 * 
 * @param newQuantifier java.lang.String
 */
public void setQuantifier(java.lang.String newQuantifier) {
	quantifier = newQuantifier;
}
/**
 * 
 * @param importedType java.util.Set
 * @param importedPackage java.util.Set
 * @param importedAssertion java.util.Set
 * @param importedAssertionSet java.util.Set
 * @param importedPredicate java.util.Set
 * @param importedPredicateSet java.util.Set
 */
public void validate(Set importedType, Set importedPackage, Set importedAssertion, Set importedAssertionSet, Set importedPredicate, Set importedPredicateSet) {
	exceptions = new Vector();
	
	String text = name + ":" + expand();
	try {
		node = new Parser(new Lexer(new PushbackReader(new StringReader(text)))).parse();
		node.apply(new Simplifier());
		Checker checker = new Checker(importedType, importedPackage, importedAssertion, importedAssertionSet, importedPredicate, importedPredicateSet);
		node.apply(checker);
		exceptions.addAll(checker.getExceptions());
		tlPredicates = (Hashtable) checker.getTLPredicatesTable().get(name);
		tlQuantifiers = (Hashtable) checker.getTLQuantifiersTable().get(name);
		pqTable = checker.getPredicateQuantifierTable();
		quantifiedVariables = (Vector) checker.getTLQuantifiedVariables().get(name);
	} catch (Exception e) {
		exceptions.addElement(e);
	}
}
}
