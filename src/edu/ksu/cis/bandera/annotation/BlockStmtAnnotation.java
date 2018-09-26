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
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.node.*;
public class BlockStmtAnnotation extends SequentialAnnotation {
	protected Vector containedAnnotations = new Vector();
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public BlockStmtAnnotation(Node node) {
	super(node);
}
/**
 * 
 * @param annotation Annotation
 */
public void addAnnotation(Annotation annotation) {
	containedAnnotations.addElement(annotation);
	annotation.setParent(this);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseBlockStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	BlockStmtAnnotation result = new BlockStmtAnnotation((Node) node.clone());

	for (Enumeration e = containedAnnotations.elements(); e.hasMoreElements();) {
		result.addAnnotation((Annotation) ((Annotation) e.nextElement()).clone());
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
	for (Enumeration e = containedAnnotations.elements(); e.hasMoreElements();) {
		Annotation a = (Annotation) e.nextElement();
		if (a instanceof BlockStmtAnnotation)
			result.addElement(a);
		for (Enumeration e2 = a.getAllAnnotations(includeSequential).elements();
				e2.hasMoreElements();) {
			result.addElement(e2.nextElement());
		}
	}
	return result;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getContainedAnnotations() {
	return containedAnnotations;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	Vector result = new Vector();
	
	for (Enumeration e = containedAnnotations.elements(); e.hasMoreElements();) {
		Annotation a = ((Annotation) e.nextElement()).getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
	}

	int size = result.size();
	if (size == 0) return null;
	else if (size == 1) return (Annotation) result.elementAt(0);
	throw new AnnotationException("Statement " + stmt + " is contained in two or more annotations");
}
/**
 * 
 * @return java.util.Hashtable
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public Hashtable getDeclaredLocalVariablesUntil(Annotation annotation) {
	Hashtable result = new Hashtable();
	for (Enumeration e = containedAnnotations.elements(); e.hasMoreElements();) {
		Annotation ann = (Annotation) e.nextElement();
		if (ann == annotation) break;
		
		if (ann instanceof LocalDeclarationStmtAnnotation) {
			Hashtable table = ((LocalDeclarationStmtAnnotation) ann).getDeclaredLocals();
			for (Enumeration e2 = table.keys(); e2.hasMoreElements();) {
				Object key = e2.nextElement();
				result.put(key, table.get(key));
			}
		}
	}
	return result;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Vector result = new Vector();
	
	for (Enumeration e = containedAnnotations.elements(); e.hasMoreElements();) {
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
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 * @param index int
 */
public void insertAnnotationAt(Annotation annotation, int index) {
	containedAnnotations.insertElementAt(annotation, index);
	annotation.setParent(this);
}
/**
 * 
 * @return boolean
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public boolean removeAnnotation(Annotation annotation) {
	return containedAnnotations.removeElement(annotation);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmt(Stmt stmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(stmt);
	
	if (a == null) return false;
	
	return a.removeStmt(stmt);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmtByMark(Stmt stmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(stmt);
	if (a == null)
		return false;
	return a.removeStmtByMark(stmt);
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
	
	return a.replaceStmt(oldStmt, newStmt);
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmtByMark(Stmt oldStmt, Stmt newStmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(oldStmt);
	if (a == null)
		return false;
	return a.replaceStmtByMark(oldStmt, newStmt);
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return (containedAnnotations.size() > 0) ? "{...}" : "{}";
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	for (Enumeration e = containedAnnotations.elements(); e.hasMoreElements();) {
		((Annotation) e.nextElement()).validate(body);
	}
}
}
