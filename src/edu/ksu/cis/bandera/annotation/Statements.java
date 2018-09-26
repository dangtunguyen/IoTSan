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
import java.util.*;
public class Statements {
	public final static int REMAINED = 0;
	public final static int SLICED = 1;
	public final static int MODIFIED = 2;
	private Vector statements = new Vector();
	private BitSet removedStatements = null;
	private BitSet modifiedStatements = null;
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public void add(Stmt stmt) {
	statements.addElement(stmt);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean contains(Stmt stmt) {
	return statements.contains(stmt);
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration elements() {
	return statements.elements();
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 11:14:08)
 * @return int
 */
public int getAnnotationState() {
	if (removedStatements != null) {
		if (edu.ksu.cis.bandera.pdgslicer.SetUtil.emptyBitSetWithLength(removedStatements, getNumOfStatements()))
			return SLICED;
		else
			return MODIFIED;
	} else
		if (modifiedStatements != null)
			return MODIFIED;
	return REMAINED;
}
/**
 * 
 * @return int
 */
public int getNumOfStatements() {
	return statements.size();
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt
 * @param index int
 */
public Stmt getStmtAt(int index) {
	return (Stmt) statements.elementAt(index);
}
/**
 * 
 * @return int
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public int indexOf(Stmt stmt) {
	return statements.indexOf(stmt);
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-7 17:57:40)
 */
public void initializeState() {
	removedStatements = null;
	modifiedStatements = null;
}
/**
 * 
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 * @param index int
 */
public  void insertStmtAt(Stmt stmt, int index) {
	statements.insertElementAt(stmt, index);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean remove(Stmt stmt) {
	return statements.removeElement(stmt);
}
/**
 * 
 * @return boolean
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean removeByMark(Stmt stmt) {
	if (statements.contains(stmt)) {
		if (removedStatements == null) {
			removedStatements = new BitSet(getNumOfStatements());
			edu.ksu.cis.bandera.pdgslicer.SetUtil.initializeBitSetToAllTrue(removedStatements);
		}
		removedStatements.clear(indexOf(stmt));
		return true;
	}
	return false;
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replace(Stmt oldStmt, Stmt newStmt) {
	int i = statements.indexOf(oldStmt);
	if (i >= 0) {
		statements.setElementAt(newStmt, i);
		return true;
	} else return false;
}
/**
 * 
 * @return boolean
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public boolean replaceByMark(Stmt oldStmt, Stmt newStmt) {
	int i = statements.indexOf(oldStmt);
	if (i >= 0) {
		if (modifiedStatements == null) {
			modifiedStatements = new BitSet(getNumOfStatements());
			edu.ksu.cis.bandera.pdgslicer.SetUtil.initializeBitSetToAllFalse(modifiedStatements);
		}
		modifiedStatements.set(i);
		return true;
	} else
		return false;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt[]
 */
public Stmt[] toArray() {
	Stmt[] result = new Stmt[statements.size()];

	for (int i = 0; i < result.length; i++) result[i] = (Stmt) statements.elementAt(i);
	
	return result;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return statements.toString();
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	StmtList stmtList = body.getStmtList();

	Vector newStatements = new Vector();
	
	for (Enumeration e = elements(); e.hasMoreElements();) {
		Stmt stmt = (Stmt) e.nextElement();
		if (stmtList.contains(stmt)) newStatements.addElement(stmt);
	}

	statements = newStatements;
}
}
