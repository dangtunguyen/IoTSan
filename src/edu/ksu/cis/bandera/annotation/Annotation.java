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
import edu.ksu.cis.bandera.pdgslicer.PostProcessOnAnnotation;
import org.apache.log4j.Category;

public abstract class Annotation extends Statements implements Cloneable, Switchable {
    private static Category log = Category.getInstance(Annotation.class.getName());
    protected Annotation parent = null;
    protected Node node;
    protected Object userObject = null;
    protected int firstLine, firstColumn, lastLine, lastColumn;

/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public Annotation(Node node) {
	this.node = node;
}
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public void addStmt(Stmt stmt) {
	add(stmt);
}
/**
 * 
 * @return java.lang.Object
 */
public abstract Object clone();
/**
 * 
 * @return boolean
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public boolean contains(Annotation annotation) {
	return getAllAnnotations(true).contains(annotation);
}
/**
 *
 * @return java.util.Vector
 * @param includeSequential boolean
 */
public Vector getAllAnnotations(boolean includeSequential) {
	Vector result = new Vector();
	result.addElement(this);
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	if (contains(stmt)) return this;
	else return null;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getDeclaredLocalVariables() {
	Hashtable result = new Hashtable();

	if (parent == null) {
	    log.info("Annotation's parent is null.  Returning an empty table of locals.");
	    return result;
	}

	if (parent instanceof BlockStmtAnnotation) {
	    log.debug("Annotation's parent is a BlockStmt.");
	    Hashtable table = ((BlockStmtAnnotation) parent).getDeclaredLocalVariablesUntil(this);
	    for (Enumeration e = table.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		result.put(key, table.get(key));
		log.debug("adding local (blockStmt) " + key);
	    }
	} else if (parent instanceof ForStmtAnnotation) {
	    log.debug("Annotation's parent is a ForStmt.");
	    Hashtable table = ((ForStmtAnnotation) parent).getDeclaredLocals();
	    for (Enumeration e = table.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		result.put(key, table.get(key));
		log.debug("adding local (forStmt) " + key);
	    }
	}

	log.debug("processing the parent's locals...");
	Hashtable table = parent.getDeclaredLocalVariables();
	for (Enumeration e = table.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		result.put(key, table.get(key));
		log.debug("adding local (parent) " + key);
	}
	
	return result;
}
/**
 * 
 * @return int
 */
public int getFirstColumn() {
	return firstColumn;
}
/**
 * 
 * @return int
 */
public int getFirstLine() {
	return firstLine;
}
/**
 * 
 * @return int
 */
public int getLastColumn() {
	return lastColumn;
}
/**
 * 
 * @return int
 */
public int getLastLine() {
	return lastLine;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.node.Node
 */
public Node getNode() {
	return node;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 */
public Annotation getParent() {
	return parent;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public abstract Stmt[] getStatements();
/**
 * 
 * @return java.lang.Object
 */
public Object getUserObject() {
	return userObject;
}
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 * @param point ca.mcgill.sable.soot.jimple.Stmt
 */
public void insertStmtAfter(Stmt stmt, Stmt point) {
	try {
		Annotation a = getContainingAnnotation(point);
		if (a != null) {
			int i = a.indexOf(point);
			a.insertStmtAt(stmt, i + 1);
		}
	} catch (Exception e) {
	}
}
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 * @param point ca.mcgill.sable.soot.jimple.Stmt
 */
public void insertStmtBefore(Stmt stmt, Stmt point) {
	try {
		Annotation a = getContainingAnnotation(point);
		if (a != null) {
			int i = a.indexOf(point);
			a.insertStmtAt(stmt, i);
		}
	} catch (Exception e) {
	}
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public abstract boolean removeStmt(Stmt stmt) throws AnnotationException;
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public abstract boolean removeStmtByMark(Stmt stmt) throws AnnotationException;
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public abstract boolean replaceStmt(Stmt oldStmt, Stmt newStmt) throws AnnotationException;
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public abstract boolean replaceStmtByMark(Stmt oldStmt, Stmt newStmt) throws AnnotationException;
/**
 * 
 * @param newFirstColumn int
 */
public void setFirstColumn(int newFirstColumn) {
	firstColumn = newFirstColumn;
}
/**
 * 
 * @param newFirstLine int
 */
public void setFirstLine(int newFirstLine) {
	firstLine = newFirstLine;
}
/**
 * 
 * @param newLastColumn int
 */
public void setLastColumn(int newLastColumn) {
	lastColumn = newLastColumn;
}
/**
 * 
 * @param newLastLine int
 */
public void setLastLine(int newLastLine) {
	lastLine = newLastLine;
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public void setParent(Annotation annotation) {
	parent = annotation;
}
/**
 * 
 * @param object java.lang.Object
 */
public void setUserObject(Object object) {
	userObject = object;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	if (node == null) return "";

	String result = node.toString().trim();
	
	return (result.endsWith(";")) ? result.substring(0, result.length() - 1) : result;
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	super.validate(body);
}
}
