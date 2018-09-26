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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
public class ClassDeclarationAnnotation extends Annotation {
	private SootClass sootClass;
/**
 * ClassDeclarationAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public ClassDeclarationAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseClassDeclarationAnnotation(this);
	}
/**
 * clone method comment.
 */
public Object clone() {
	ClassDeclarationAnnotation result = new ClassDeclarationAnnotation(node);
	result.setSootClass(getSootClass());
	return result;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootClass
 */
public ca.mcgill.sable.soot.SootClass getSootClass() {
	return sootClass;
}
/**
 * getStatements method comment.
 */
public ca.mcgill.sable.soot.jimple.Stmt[] getStatements() {
	return new Stmt[0];
}
/**
 * removeStmt method comment.
 */
public boolean removeStmt(ca.mcgill.sable.soot.jimple.Stmt stmt) throws AnnotationException {
	return false;
}
/**
 * removeStmt method comment.
 */
public boolean removeStmtByMark(ca.mcgill.sable.soot.jimple.Stmt stmt) throws AnnotationException {
	return false;
}
/**
 * replaceStmt method comment.
 */
public boolean replaceStmt(ca.mcgill.sable.soot.jimple.Stmt oldStmt, ca.mcgill.sable.soot.jimple.Stmt newStmt) throws AnnotationException {
	return false;
}
/**
 * replaceStmt method comment.
 */
public boolean replaceStmtByMark(ca.mcgill.sable.soot.jimple.Stmt oldStmt, ca.mcgill.sable.soot.jimple.Stmt newStmt) throws AnnotationException {
	return false;
}
/**
 * 
 * @param newSootClass ca.mcgill.sable.soot.SootClass
 */
public void setSootClass(ca.mcgill.sable.soot.SootClass newSootClass) {
	sootClass = newSootClass;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return sootClass.toString();
}
}
