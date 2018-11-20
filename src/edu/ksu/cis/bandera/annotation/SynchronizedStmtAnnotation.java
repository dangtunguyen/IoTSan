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
public class SynchronizedStmtAnnotation extends SpecializedAnnotation {
	private Annotation blockAnnotation = null;
	private Annotation catchAnnotation = null;
	private Vector exitMonitors = new Vector();
	private Value lockValue = null;
	private Stmt enterMonitor = null;
/**
 * SynchronizedStmtAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public SynchronizedStmtAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public void addExitMonitor(Stmt stmt) {
	exitMonitors.addElement(stmt);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseSynchronizedStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	SynchronizedStmtAnnotation result = new SynchronizedStmtAnnotation((Node) node.clone());
	/*
	Stmt[] stmts = toArray();

	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	if (blockAnnotation != null) result.setBlockAnnotation((Annotation) blockAnnotation.clone());
	if (catchAnnotation != null) result.setCatchAnnotation((Annotation) catchAnnotation.clone());
	/*
	result.setExitMonitors(exitMonitors);
	result.setLockValue(lockValue);
	result.setEnterMonitor(enterMonitor);
	*/
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
 * @return annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public Annotation getCatchAnnotation() {
	return catchAnnotation;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	Vector result = new Vector();

	Annotation a = super.getContainingAnnotation(stmt);
	if (a != null) result.addElement(this);

	a = blockAnnotation.getContainingAnnotation(stmt);
	if (a != null) result.addElement(a);

	if ((catchAnnotation != null) && (catchAnnotation.getContainingAnnotation(stmt) != null))
		result.addElement(this);

	int size = result.size();
	if (size == 0) return null;
	else if (size == 1) return (Annotation) result.elementAt(0);
	throw new AnnotationException("Statement " + stmt + " is contained in two or more annotations");
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt
 */
public Stmt getEnterMonitor() {
	return enterMonitor;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getExitMonitors() {
	return exitMonitors;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getLockStatements() {
	return toArray();
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 */
public Value getLockValue() {
	return lockValue;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Stmt[] stmts = super.toArray();
	Stmt[] blockStmts = (blockAnnotation != null) ? blockAnnotation.getStatements() : new Stmt[0];
	Stmt[] catchStmts = (catchAnnotation != null) ? catchAnnotation.getStatements() : new Stmt[0];

	Stmt[] result = new Stmt[stmts.length + blockStmts.length + catchStmts.length];

	for (int i = 0; i < stmts.length; i++) {
		result[i] = stmts[i];
	}

	int index = stmts.length;

	for (int i = 0; i < blockStmts.length; i++) {
		result[index + i] = blockStmts[i];
	}

	index += blockStmts.length;

	for (int i = 0; i < catchStmts.length; i++) {
		result[index + i] = catchStmts[i];
	}
		
	return result;
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmt(Stmt stmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(stmt);
	if (a == null) return false;
	else if (a == this) {
		boolean r = false;
		if (catchAnnotation != null) {
			r = catchAnnotation.removeStmt(stmt);
		}
		return remove(stmt) || r; 
	} else return a.removeStmt(stmt);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmtByMark(Stmt stmt) throws AnnotationException {
	Annotation a = getContainingAnnotation(stmt);
	if (a == null) return false;
	else if (a == this) {
		boolean r = false;
		if (catchAnnotation != null) {
			r = catchAnnotation.removeStmtByMark(stmt);
		}
		return removeByMark(stmt) || r; 
	} else return a.removeStmtByMark(stmt);
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
	else if (a == this) {
		boolean r = false;
		if (catchAnnotation != null) {
			r = catchAnnotation.replaceStmt(oldStmt, newStmt);
		}
		return replace(oldStmt, newStmt) || r; 
	} else return a.replaceStmt(oldStmt, newStmt);
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
	else if (a == this) {
		boolean r = false;
		if (catchAnnotation != null) {
			r = catchAnnotation.replaceStmtByMark(oldStmt, newStmt);
		}
		return replaceByMark(oldStmt, newStmt) || r; 
	} else return a.replaceStmtByMark(oldStmt, newStmt);
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
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void setCatchAnnotation(Annotation annotation) {
	catchAnnotation = annotation;
	catchAnnotation.setParent(this);
}
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public void setEnterMonitor(Stmt stmt) {
	enterMonitor = stmt;
}
/**
 * 
 * @param exitMonitors java.util.Vector
 */
public void setExitMonitors(Vector exitMonitors) {
}
/**
 * 
 * @param value ca.mcgill.sable.soot.jimple.Value
 */
public void setLockValue(Value value) {
	lockValue = value;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "synchronized (" + ((ASynchronizedStmt) node).getExp().toString().trim() + ")";
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	StmtList stmts = body.getStmtList();
	
	super.validate(body);

	if (blockAnnotation != null) blockAnnotation.validate(body);

	if (catchAnnotation != null) catchAnnotation.validate(body);

	Vector newExitMonitors = new Vector();

	for (Enumeration e = exitMonitors.elements(); e.hasMoreElements();) {
		Stmt stmt = (Stmt) e.nextElement();
		if (stmts.contains(stmt)) {
			newExitMonitors.addElement(stmt);
		}
	}

	exitMonitors = newExitMonitors;

	if (!stmts.contains(enterMonitor)) enterMonitor = null;
}
}
