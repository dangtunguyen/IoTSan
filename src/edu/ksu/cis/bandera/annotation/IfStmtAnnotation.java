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
public class IfStmtAnnotation extends ConditionalAnnotation {
	private Annotation thenAnnotation = null;
	private Annotation annotation = null;
	private Annotation elseAnnotation = null;
/**
 * IfStmtAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public IfStmtAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseIfStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	IfStmtAnnotation result = new IfStmtAnnotation((Node) node.clone());
	/*
	Stmt[] stmts = getTestStatements();

	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	if (thenAnnotation != null) result.setThenAnnotation((Annotation) thenAnnotation.clone());
	if (annotation != null) result.setAnnotation((Annotation) annotation.clone());
	if (elseAnnotation != null) result.setElseAnnotation((Annotation) elseAnnotation.clone());
	
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
	for (Enumeration e = thenAnnotation.getAllAnnotations(includeSequential).elements(); e.hasMoreElements();) {
		result.addElement(e.nextElement());
	}
	for (Enumeration e = elseAnnotation.getAllAnnotations(includeSequential).elements(); e.hasMoreElements();) {
		result.addElement(e.nextElement());
	}
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 */
public Annotation getAnnotation() {
	return annotation;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	Vector result = new Vector();

	Annotation a = super.getContainingAnnotation(stmt);
	if (a != null) result.addElement(a);

	if (thenAnnotation != null) {
		a = thenAnnotation.getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
	}
	
	if (annotation != null) {
		a = annotation.getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
	}
	
	if (elseAnnotation != null) {
		a = elseAnnotation.getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
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
public Annotation getElseAnnotation() {
	return elseAnnotation;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Stmt[] testStmts = toArray();
	Stmt[] thenStmts = (thenAnnotation != null) ? thenAnnotation.getStatements() : new Stmt[0];
	Stmt[] stmts = (annotation != null) ? annotation.getStatements() : new Stmt[0];
	Stmt[] elseStmts = (elseAnnotation != null) ? elseAnnotation.getStatements() : new Stmt[0];

	Stmt[] result = new Stmt[testStmts.length + thenStmts.length + stmts.length + elseStmts.length];

	for (int i = 0; i < testStmts.length; i++) {
		result[i] = testStmts[i];		
	}

	int index = testStmts.length;

	for (int i = 0; i < thenStmts.length; i++) {
		result[index + i] = thenStmts[i];
	}

	index += thenStmts.length;

	for (int i = 0; i < stmts.length; i++) {
		result[index + i] = stmts[i];
	}

	index += stmts.length;
	
	for (int i = 0; i < elseStmts.length; i++) {
		result[index + i] = elseStmts[i];
	}
	
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 */
public Annotation getThenAnnotation() {
	return thenAnnotation;
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
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public void setAnnotation(Annotation annotation) {
	this.annotation = annotation;
	annotation.setParent(this);
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void setElseAnnotation(Annotation annotation) {
	elseAnnotation = annotation;
	elseAnnotation.setParent(this);
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void setThenAnnotation(Annotation annotation) {
	thenAnnotation = annotation;
	thenAnnotation.setParent(this);
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "if (" + ((AIfStmt) node).getExp().toString().trim() + ")";
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	super.validate(body);
	if (thenAnnotation != null) thenAnnotation.validate(body);
	if (annotation != null) annotation.validate(body);
	if (elseAnnotation != null) elseAnnotation.validate(body);
}
}
