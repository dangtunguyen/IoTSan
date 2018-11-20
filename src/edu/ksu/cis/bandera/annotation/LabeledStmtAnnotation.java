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
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import java.util.*;
public class LabeledStmtAnnotation extends SequentialAnnotation {
	private String id = null;
	private Annotation annotation = null;
/**
 * LabeledStmtAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public LabeledStmtAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseLabeledStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	LabeledStmtAnnotation result = new LabeledStmtAnnotation((Node) node.clone());
	result.id = id;
	if (annotation != null) result.annotation = (Annotation) annotation.clone();
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
	result.addAll(annotation.getAllAnnotations(includeSequential));
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
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
	return (annotation != null) ? annotation.getContainingAnnotation(stmt) : null;
}
/**
 * 
 * @return java.lang.String
 */
public String getId() {
	return id;
}
/**
 * 
 * @return Stmt[]
 */
public Stmt[] getStatements() {
	return (annotation != null) ? annotation.getStatements() : new Stmt[0];
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmt(Stmt stmt) throws AnnotationException {
	if (annotation != null) return annotation.removeStmt(stmt);
	else return false;
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmtByMark(Stmt stmt) throws AnnotationException {
	if (annotation != null) return annotation.removeStmtByMark(stmt);
	else return false;
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmt(Stmt oldStmt, Stmt newStmt) throws AnnotationException {
	if (annotation != null) return annotation.replaceStmt(oldStmt, newStmt);
	else return false;
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmtByMark(Stmt oldStmt, Stmt newStmt) throws AnnotationException {
	if (annotation != null) return annotation.replaceStmtByMark(oldStmt, newStmt);
	else return false;
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void setAnnotation(Annotation annotation) {
	this.annotation = annotation;
	this.annotation.setParent(this);
}
/**
 * 
 * @param id java.lang.String
 */
public void setId(String id) {
	this.id = id;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return id + ":";
}
/**
 * 
 * @param body JimpleBody
 */
public void validate(JimpleBody body) {
	if (annotation != null) annotation.validate(body);
}
}
