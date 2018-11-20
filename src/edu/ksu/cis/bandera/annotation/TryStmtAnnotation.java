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

public class TryStmtAnnotation extends SpecializedAnnotation {
	protected Annotation blockAnnotation = null; 
	protected Vector catchClauses = new Vector();
/**
 * TryStmtAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public TryStmtAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Statements
 */
public void addCatchClauseAnnotation(Annotation annotation) {
	catchClauses.addElement(annotation);
	annotation.setParent(this);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseTryStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	TryStmtAnnotation result = new TryStmtAnnotation((Node) node.clone());

	if (blockAnnotation != null) result.setBlockAnnotation((Annotation) blockAnnotation.clone());

	for (Enumeration e = catchClauses.elements(); e.hasMoreElements();) {
		result.addCatchClauseAnnotation((Annotation) ((Annotation) e.nextElement()).clone());
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
	for (Enumeration e = blockAnnotation.getAllAnnotations(includeSequential).elements(); e.hasMoreElements();) {
		result.addElement(e.nextElement());
	}
	for (Enumeration e = catchClauses.elements(); e.hasMoreElements();) {
		Annotation a = (Annotation) e.nextElement();
		result.addElement(a);
		for (Enumeration e2 = a.getAllAnnotations(includeSequential).elements();	e2.hasMoreElements();) {
			result.addElement(e2.nextElement());
		}
	}
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 */
public Annotation getBlockAnnotation() {
	return blockAnnotation;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getCatchClauses() {
	return catchClauses;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	Vector result = new Vector();

	if (blockAnnotation != null) {
		Annotation a = blockAnnotation.getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
	}
	
	for (Enumeration e = catchClauses.elements(); e.hasMoreElements();) {
		Annotation a = (Annotation) e.nextElement();
		a = a.getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
	}

	int size = result.size();
	if (size == 0) return null;
	else if (size == 1) return (Annotation) result.elementAt(0);
	throw new AnnotationException("Statement " + stmt + " is contained in two or more annotations");
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Vector result = new Vector();
	
	Stmt[] blockStmts = (blockAnnotation != null) ? blockAnnotation.getStatements() : new Stmt[0];

	for (int i = 0; i < blockStmts.length; i++) {
		result.addElement(blockStmts[i]);
	}

	for (Enumeration e = catchClauses.elements(); e.hasMoreElements();) {
		Stmt[] stmts = ((Annotation) e.nextElement()).getStatements();
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
 * @return boolean
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public boolean removeCatchClauseAnnotation(Annotation annotation) {
	return catchClauses.removeElement(annotation);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmt(Stmt stmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(stmt);
	if (a == null) return false;
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
	else return a.replaceStmtByMark(oldStmt, newStmt);
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void setBlockAnnotation(Annotation annotation) {
	blockAnnotation = annotation;
	blockAnnotation.setParent(this);
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "try";
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	if (blockAnnotation != null) blockAnnotation.validate(body);
	
	for (Enumeration e = catchClauses.elements(); e.hasMoreElements();) {
		((Annotation) e.nextElement()).validate(body);
	}
}
}
