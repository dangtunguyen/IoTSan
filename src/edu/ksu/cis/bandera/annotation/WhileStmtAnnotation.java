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

public class WhileStmtAnnotation extends ControlFlowAnnotation {
	private Annotation initAnnotation = null;
/**
 * WhileStmtAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public WhileStmtAnnotation(Node node) {
	super(node);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseWhileStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	WhileStmtAnnotation result = new WhileStmtAnnotation((Node) node.clone());
	/*
	Stmt[] stmts = getTestStatements();

	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	if (blockAnnotation != null) result.setBlockAnnotation((Annotation) blockAnnotation.clone());
	if (initAnnotation != null) result.setInitAnnotation((Annotation) blockAnnotation.clone());
	result.setIndefinite(indefinite);
	
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
 * @return ca.mcgill.sable.soot.jimple.Stmt
 */
public Stmt getBackpointStmt() {
	int size = getNumOfStatements();
	if (size > 0)
		return getStmtAt(size - 1);
	else
		return null;
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

	if (initAnnotation != null) {
		a = initAnnotation.getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
	}

	int size = result.size();
	if (size == 0) return null;
	else if (size == 1) return (Annotation) result.elementAt(0);
	throw new AnnotationException("Statement " + stmt + " is contained in two or more annotations");
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 */
public Annotation getInitAnnotation() {
	return initAnnotation;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Stmt[] initStmts = (initAnnotation != null) ? initAnnotation.getStatements() : new Stmt[0];
	
	Stmt[] stmts = (blockAnnotation != null) ? blockAnnotation.getStatements() : new Stmt[0];

	Stmt[] testStmts = super.toArray();
	
	Stmt[] result = new Stmt[initStmts.length + stmts.length + testStmts.length];

	for (int i = 0; i < initStmts.length; i++) {
		result[i] = initStmts[i];
	}

	int index = initStmts.length;
	
	for (int i = 0; i < stmts.length; i++) {
		result[index + i] = stmts[i];
	}

	index += stmts.length;

	for (int i = 0; i < testStmts.length; i++) {
		result[index + i] = testStmts[i];
	}
	
	return result;
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public void setInitAnnotation(Annotation annotation) {
	initAnnotation = annotation;
	initAnnotation.setParent(this);
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "while (" + ((AWhileStmt) node).getExp().toString().trim() + ")";
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	super.validate(body);

	if (initAnnotation != null) initAnnotation.validate(body);
}
}
