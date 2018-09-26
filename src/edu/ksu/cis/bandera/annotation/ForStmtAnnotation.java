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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import java.util.*;
public class ForStmtAnnotation extends ControlFlowAnnotation {
	private Annotation initAnnotation = null;
	private Annotation updateAnnotation = null;
	private Hashtable declaredLocals = new Hashtable();
	private Type type = null;
	private int modifiers = 0;
/**
 * ForStmtAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public ForStmtAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
/**
 * 
 * @param actualName java.lang.String
 * @param local ca.mcgill.sable.soot.jimple.Local
 */
public void addDeclaredLocal(String actualName, Local local) {
	declaredLocals.put(actualName, local);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseForStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	ForStmtAnnotation result = new ForStmtAnnotation((Node) node.clone());

	/*
	Stmt[] stmts = getTestStatements();

	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	if (initAnnotation != null) result.setInitAnnotation((Annotation) initAnnotation.clone());
	if (blockAnnotation != null) result.setBlockAnnotation((Annotation) blockAnnotation.clone());
	if (updateAnnotation != null) result.setUpdateAnnotation((Annotation) updateAnnotation.clone());
	result.setDeclaredLocals((Hashtable) declaredLocals.clone());
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
	Stmt result = null;
	
	if (updateAnnotation != null) {
		Stmt[] stmts = updateAnnotation.getStatements();
		if (stmts.length > 0) result = stmts[stmts.length - 1];
	}
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	Vector result = new Vector();

	if (initAnnotation != null) {
		Annotation a = initAnnotation.getContainingAnnotation(stmt);
		if (a != null) result.addElement(a);
	}	

	Annotation a = super.getContainingAnnotation(stmt);
	if (a != null) result.addElement(a);
		
	if (updateAnnotation != null) {
		a = updateAnnotation.getContainingAnnotation(stmt);
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
 */
public Hashtable getDeclaredLocal() {
	return declaredLocals;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getDeclaredLocals() {
	return declaredLocals;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 */
public Annotation getInitAnnotation() {
	return initAnnotation;
}
/**
 * 
 * @return int
 */
public int getModifiers() {
	return modifiers;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] getStatements() {
	Stmt[] initStmts = (initAnnotation != null) ? initAnnotation.getStatements() : new Stmt[0];
	Stmt[] testStmts = toArray();
	Stmt[] blockStmts = (blockAnnotation != null)? blockAnnotation.getStatements() : new Stmt[0];
	Stmt[] updateStmts = (updateAnnotation != null) ? updateAnnotation.getStatements() : new Stmt[0];

	Stmt[] result = new Stmt[initStmts.length + testStmts.length + blockStmts.length + updateStmts.length];

	int index = initStmts.length;

	for (int i = 0; i < index; i++) {
		result[i] = initStmts[i];
	}

	for (int i = 0; i < testStmts.length; i++) {
		result[index + i] = testStmts[i];
	}

	index += testStmts.length;

	for (int i = 0; i < blockStmts.length; i++) {
		result[index + i] = blockStmts[i];
	}

	index += blockStmts.length;

	for (int i = 0; i < updateStmts.length; i++) {
		result[index + i] = updateStmts[i];
	}
		
	return result;
}
/**
 * 
 * @return ca.mcgill.sable.soot.Type
 */
public Type getType() {
	return type;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 */
public Annotation getUpdateAnnotation() {
	return updateAnnotation;
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmt(Stmt stmt) throws AnnotationException {
	return super.removeStmt(stmt) || initAnnotation.removeStmt(stmt)
			|| updateAnnotation.removeStmt(stmt);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeStmtByMark(Stmt stmt) throws AnnotationException {
	return super.removeStmtByMark(stmt) || initAnnotation.removeStmtByMark(stmt)
			|| updateAnnotation.removeStmtByMark(stmt);
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmt(Stmt oldStmt, Stmt newStmt) throws AnnotationException {
	return super.replaceStmt(oldStmt, newStmt) || initAnnotation.replaceStmt(oldStmt, newStmt)
			|| updateAnnotation.replaceStmt(oldStmt, newStmt);
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceStmtByMark(Stmt oldStmt, Stmt newStmt) throws AnnotationException {
	return super.replaceStmtByMark(oldStmt, newStmt) || initAnnotation.replaceStmtByMark(oldStmt, newStmt)
			|| updateAnnotation.replaceStmtByMark(oldStmt, newStmt);
}
/**
 * 
 * @param table java.util.Hashtable
 */
public void setDeclaredLocals(Hashtable table) {
	declaredLocals = table;
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void setInitAnnotation(Annotation annotation) {
	initAnnotation = annotation;
	initAnnotation.setParent(this);
}
/**
 * 
 * @param modifiers int
 */
public void setModifiers(int modifiers) {
	this.modifiers = modifiers;
}
/**
 * 
 * @param type ca.mcgill.sable.soot.Type
 */
public void setType(Type type) {
	this.type = type;
}
/**
 * 
 * @param annotation edu.ksu.cis.bandera.annotations.Statements
 */
public void setUpdateAnnotation(Annotation annotation) {
	updateAnnotation = annotation;
	updateAnnotation.setParent(this);
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	AForStmt node = (AForStmt) this.node;
	String result = "for (";
	if(node.getForInit() != null) result += node.getForInit().toString().trim();

	result += "; ";

	if(node.getExp() != null)	result += node.getExp().toString().trim();
	
	result += "; ";
	
	Object temp[] = node.getForUpdate().toArray();
	for(int i = 0; i < temp.length - 1; i++) {
		result += (temp[i].toString() + ", ");
	}

	if (temp.length > 0) result += temp[temp.length - 1].toString().trim();

	result += ")";
	
	return result;
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	if (initAnnotation != null) initAnnotation.validate(body);

	super.validate(body);

	if (updateAnnotation != null) updateAnnotation.validate(body);

	for (Enumeration e = declaredLocals.keys(); e.hasMoreElements();) {
		String actualName = (String) e.nextElement();
		String jimpleName = ((Local) declaredLocals.get(actualName)).getName().trim();
		if (!body.declaresLocal(jimpleName)) {
			declaredLocals.remove(actualName);
		}
	}
}
}
