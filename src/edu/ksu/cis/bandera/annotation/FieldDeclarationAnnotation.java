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
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import java.util.*;
public class FieldDeclarationAnnotation extends SpecializedAnnotation implements java.lang.Comparable {
	private SootField sf;
	private Field f;
/**
 * FieldDeclarationAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public FieldDeclarationAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseFieldDeclarationAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	FieldDeclarationAnnotation result = new FieldDeclarationAnnotation((Node) node.clone());
	/*
	Stmt[] stmts = getStatements();
	
	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	result.sf = sf;
	result.f = f;
	return result;
}
/**
 * 
 * @return int
 * @param o java.lang.Object
 */
public int compareTo(Object o) {
	if (o instanceof FieldDeclarationAnnotation) {
		FieldDeclarationAnnotation fda = (FieldDeclarationAnnotation) o;
		return getField().getName().compareTo(fda.getField().getName());
	} else {
		return -1;
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Field
 */
public Field getField() {
	return f;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootField
 */
public SootField getSootField() {
	return sf;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	return toArray();
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmt(Stmt stmt) {
	return remove(stmt);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmtByMark(Stmt stmt) {
	return removeByMark(stmt);
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmt(Stmt oldStmt, Stmt newStmt) {
	return replace(oldStmt, newStmt);
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmtByMark(Stmt oldStmt, Stmt newStmt) {
	return replaceByMark(oldStmt, newStmt);
}
/**
 * 
 * @param field edu.ksu.cis.bandera.jjjc.symboltable.Field
 */
public void setField(Field field) {
	f = field;
}
/**
 * 
 * @param sootField ca.mcgill.sable.soot.SootField
 */
public void setSootField(SootField sootField) {
	sf = sootField;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	AFieldDeclaration node = (AFieldDeclaration) this.node;
	
	String result = node.getType().toString();

	Object[] modifiers = node.getModifier().toArray();
	for (int i = modifiers.length - 1; i >= 0; i--) {
		result = modifiers[i].toString() + result;
	}

	Object[] fields = node.getVariableDeclarator().toArray();
		
	for (int i = 0; i < fields.length; i++) {
		if (fields[i] instanceof AAssignedVariableDeclarator) {
			String name = ((AAssignedVariableDeclarator) fields[i]).getVariableDeclaratorId().toString().trim();

			if (name.equals(sf.getName().trim())) {
				result += fields[i].toString().trim();
				break;
			}
		} else {
			String name = ((AIdVariableDeclarator) fields[i]).getVariableDeclaratorId().toString().trim();

			if (name.equals(sf.getName().trim())) {
				result += name;
				break;
			}
		}
	}

	return result;
}
}
