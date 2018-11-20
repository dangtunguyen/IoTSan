package edu.ksu.cis.bandera.annotation;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.util.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import java.util.*;
public class SwitchStmtAnnotation extends ConditionalAnnotation {
	private Hashtable switchCases = new Hashtable();
	private Annotation defaultAnnotation = null;
	private Vector values = new Vector();
/**
 * 
 * @param node Node
 */
public SwitchStmtAnnotation(Node node) {
	super(node);
}
/**
 *
 * @param value ca.mcgill.sable.soot.jimple.Value
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void addSwitchCase(Integer value, Annotation annotation) {
	annotation.setParent(this);
	switchCases.put(value, annotation);
	values.addElement(value);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseSwitchStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	SwitchStmtAnnotation result = new SwitchStmtAnnotation((Node) node.clone());
	/*
	Stmt[] stmts = getTestStatements();

	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	for (Enumeration e = values.elements(); e.hasMoreElements();) {
		Integer value = (Integer) e.nextElement();
		Annotation a = (Annotation) ((Annotation) switchCases.get(value)).clone();
		result.addSwitchCase(new Integer(value.intValue()), a);
	}

	return result;
}
/**
 * 
 * @return java.util.Vector
 * @param includeSequential boolean
 */
public Vector getAllAnnotations(boolean includeSequential) {
	Vector result = new Vector();
	result.addElement(this);
	for (Enumeration e = values.elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((Annotation) switchCases.get(e.nextElement())).getAllAnnotations(includeSequential).elements();
				e2.hasMoreElements();) {
			result.addElement(e2.nextElement());
		}
	}
	if (defaultAnnotation != null)
	{
		for (Enumeration e = defaultAnnotation.getAllAnnotations(includeSequential).elements(); e.hasMoreElements();)
		{
			result.addElement(e.nextElement());
		}
	}
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	Vector result = new Vector();

	if (contains(stmt)) result.addElement(this);

	if (defaultAnnotation != null) {
		Annotation a = defaultAnnotation.getContainingAnnotation(stmt);
		if ((a != null) && !result.contains(a)) result.addElement(a);
	}

	for (Enumeration e = switchCases.elements(); e.hasMoreElements();) {
		Annotation a = ((Annotation) e.nextElement()).getContainingAnnotation(stmt);
		if ((a != null) && !result.contains(a)) result.addElement(a);
	}

	int size = result.size();
	if (size == 0) return null;
	else if (size == 1) return (Annotation) result.elementAt(0);
	throw new AnnotationException("Statement " + stmt + " is contained in two or more annotations");
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 */
public Annotation getDefaultAnnotation() {
	return defaultAnnotation;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Vector result = new Vector();
	
	Stmt[] switchStmts = toArray();

	for (int i = 0; i < switchStmts.length; i++) {
		result.addElement(switchStmts[i]);
	}

	Vector generated = new Vector();

	for (Enumeration e = values.elements(); e.hasMoreElements();) {
		Annotation a = ((Annotation) switchCases.get(e.nextElement()));
		if (!generated.contains(a)) {
			generated.addElement(a);
			Stmt[] stmts = a.getStatements();
			for (int i = 0; i < stmts.length; i++) {
				result.addElement(stmts[i]);
			}
		}
	}

	if (defaultAnnotation != null) {
		Stmt[] stmts = defaultAnnotation.getStatements();
		for (int i = 0; i < stmts.length; i++) {
			result.addElement(stmts[i]);
		}
	}

	Stmt[] stmts = new Stmt[result.size()];

	for (int i = 0; i < stmts.length; i++) {
		stmts[i] = (Stmt) result.elementAt(i);
	}
	
	return stmts;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getSwitchCases() {
	return switchCases;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getValues() {
	return values;
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmt(Stmt stmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(stmt);
	if (a == null) return false;
	else if (a == this) return remove(stmt);
	else return a.removeStmt(stmt);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmtByMark(Stmt stmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(stmt);
	if (a == null) return false;
	else if (a == this) return removeByMark(stmt);
	else return a.removeStmtByMark(stmt);
}
/**
 * 
 * @return boolean
 * @param java.lang.Integer
 */
public boolean removeSwitchCase(Integer value) {
	return (switchCases.remove(value) != null);
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmt(Stmt oldStmt, Stmt newStmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(oldStmt);
	if (a == null) return false;
	else if (a == this) return replace(oldStmt, newStmt);
	else return a.replaceStmt(oldStmt, newStmt);
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmtByMark(Stmt oldStmt, Stmt newStmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(oldStmt);
	if (a == null) return false;
	else if (a == this) return replaceByMark(oldStmt, newStmt);
	else return a.replaceStmtByMark(oldStmt, newStmt);
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void setDefaultAnnotation(Annotation annotation) {
	defaultAnnotation = annotation;
	defaultAnnotation.setParent(this);
}
/**
 * 
 * @param table java.util.Hashtable
 */
public void setSwitchCases(Hashtable table) {
	switchCases = table;
}
/**
 * 
 * @param values java.util.Vector
 */
public void setValues(Vector values) {
	this.values = values;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "switch (" + ((ASwitchStmt) node).getExp().toString().trim() + ")";
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	super.validate(body);
	
	for (Enumeration e = switchCases.elements(); e.hasMoreElements();) {
		((Statements) e.nextElement()).validate(body);
	}

	if (defaultAnnotation != null) defaultAnnotation.validate(body);
}
}
