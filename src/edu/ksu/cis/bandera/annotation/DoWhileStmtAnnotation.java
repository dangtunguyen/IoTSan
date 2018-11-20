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
public class DoWhileStmtAnnotation extends ControlFlowAnnotation {
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public DoWhileStmtAnnotation(Node node) {
	super(node);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseDoWhileStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	DoWhileStmtAnnotation result = new DoWhileStmtAnnotation((Node) node.clone());

	/*
	Stmt[] stmts = getTestStatements();

	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	if (blockAnnotation != null) result.setBlockAnnotation((Annotation) blockAnnotation.clone());
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
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Stmt[] stmts = (blockAnnotation != null) ? blockAnnotation.getStatements() : new Stmt[0];

	Stmt[] testStmts = super.toArray();

	Stmt[] result = new Stmt[stmts.length + testStmts.length];

	for (int i = 0; i < stmts.length; i++) {
		result[i] = stmts[i];
	}

	for (int i = 0; i < testStmts.length; i++) {
		result[i + stmts.length] = testStmts[i];
	}
	
	return result;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return (blockAnnotation == null) ?
		"do while (" + ((ADoStmt) node).getExp().toString().trim() + ")"
	: "do ... while (" + ((ADoStmt) node).getExp().toString().trim() + ")";
}
}
