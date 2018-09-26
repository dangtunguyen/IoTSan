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
import edu.ksu.cis.bandera.jext.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.decompiler.*;
public class AnnotationManager {
	private Hashtable annotations = new Hashtable();
	private Hashtable inlineTable = new Hashtable();
	private Hashtable methodStmtTable = new Hashtable();
	private Hashtable localTable = new Hashtable();
	private Hashtable newOldLocalTable = new Hashtable();
	private Hashtable oldNewLocalTable = new Hashtable();
	private Hashtable oldNewStmtTable = new Hashtable();
	private Hashtable newOldStmtTable = new Hashtable();
	private Hashtable stmtLocalTable = new Hashtable();
	private Hashtable localPackingTable = new Hashtable();
	private Hashtable replacedStmt = new Hashtable();
	private Hashtable filenameLinePairAnnotationTable = new Hashtable();
	private Hashtable stmtAnnotationTable = new Hashtable();
/**
 * AnnotationManager constructor comment.
 */
public AnnotationManager() {
}
/**
 * 
 * @param sc SootClass
 * @param o java.lang.Object
 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
 */
public void addAnnotation(SootClass sc, Object o, Annotation annotation) {
	Hashtable table = (Hashtable) annotations.get(sc);
	
	if (table == null) {
		table = new Hashtable();
		annotations.put(sc, table);
	}

	table.put(o, annotation);
}
/**
 * 
 * @param cda edu.ksu.cis.bandera.annotation.ClassDeclarationAnnotation
 */
public void addAnnotation(ClassDeclarationAnnotation cda) {
	SootClass sc = cda.getSootClass();
	Hashtable table = (Hashtable) annotations.get(sc);
	
	if (table == null) {
		table = new Hashtable();
		annotations.put(sc, table);
	}
	table.put(sc, cda);
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootClass
 * @param sc ca.mcgill.sable.soot.SootClass
 */
public Annotation getAnnotation(SootClass sc) {
	Hashtable table = (Hashtable) annotations.get(sc);
	if (table == null) return null;
	return (Annotation) table.get(sc);
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotations.Annotation
 * @param sc ca.mcgill.sable.soot.SootClass
 * @param o java.lang.Object
 */
public Annotation getAnnotation(SootClass sc, Object o) {
	Hashtable table = (Hashtable) annotations.get(sc);
	if (table == null) return null;
	return (Annotation) table.get(o);
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 * @param flp edu.ksu.cis.bandera.ase.FilenameLinePair
 */
public Annotation getAnnotation(FilenameLinePair flp) {
	return (Annotation) filenameLinePairAnnotationTable.get(flp);
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getAnnotationTable() {
	return annotations;
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getCompiledClasses() {
	return annotations.keys();
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(Stmt stmt) throws AnnotationException {
	if (inlineTable.get(stmt) != null) {
		return (Annotation) inlineTable.get(stmt);
	}
	if (stmtAnnotationTable.get(stmt) != null) {
		return (Annotation) stmtAnnotationTable.get(stmt);
	}
	Vector result = new Vector();
	for (Enumeration e = annotations.keys(); e.hasMoreElements();) {
		SootClass sc = (SootClass) e.nextElement();
		Annotation a = getContainingAnnotation(sc, stmt);
		if (a != null) if (!result.contains(a)) result.addElement(a);
	}
	if (result.size() > 1)
		throw new AnnotationException("Statement " + stmt + " is contained in two or more annotations");
	else if (result.size() < 1) return null;
	else {
		Annotation a = (Annotation) result.firstElement();
		stmtAnnotationTable.put(stmt, a);
		return a;
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 * @param sootClass ca.mcgill.sable.soot.SootClass
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(SootClass sootClass, Stmt stmt) throws AnnotationException {
	if (inlineTable.get(stmt) != null) {
		return (Annotation) inlineTable.get(stmt);
	}
	if (stmtAnnotationTable.get(stmt) != null) {
		return (Annotation) stmtAnnotationTable.get(stmt);
	}
	Vector result = new Vector();

	Hashtable table = (Hashtable) annotations.get(sootClass);
	if (table == null) return null;

	Annotation a = null;
	for (Enumeration e = table.elements(); e.hasMoreElements();) {
		try {
			Annotation ann = (Annotation) e.nextElement();
			if (!(ann instanceof FieldDeclarationAnnotation))
				a = ann.getContainingAnnotation(stmt);
		} catch (Exception exception) {
		  a = null;
		}
		if (a != null) if (!result.contains(a)) result.addElement(a);
	}

	if (result.size() > 1)
		throw new AnnotationException("Statement " + stmt + " is contained in two or more annotations");
	else if (result.size() < 1) return null;
	else {
		a = (Annotation) result.firstElement();
		stmtAnnotationTable.put(stmt, a);
		return a;
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 * @param sootClass ca.mcgill.sable.soot.SootClass
 * @param object java.lang.Object
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Annotation getContainingAnnotation(SootClass sootClass, Object object, Stmt stmt) throws AnnotationException {
	if (inlineTable.get(stmt) != null) {
		return (Annotation) inlineTable.get(stmt);
	}
	if (stmtAnnotationTable.get(stmt) != null) {
		return (Annotation) stmtAnnotationTable.get(stmt);
	}
	else {
		Annotation a = getAnnotation(sootClass, object).getContainingAnnotation(stmt);
		stmtAnnotationTable.put(stmt, a);
		return a;
	}
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootMethod
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public SootMethod getContainingSootMethod(Stmt stmt) {
	return (SootMethod) methodStmtTable.get(stmt);
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getFilenameLinePairAnnotationTable() {
	return filenameLinePairAnnotationTable;
}
/**
 * 
 * @return java.util.HashSet
 * @param e edu.ksu.cis.bandera.jext.LocalExpr
 */
public HashSet getInlinedLocal(LocalExpr e) {
	return (HashSet) oldNewLocalTable.get(e);
}
/**
 * 
 * @return java.util.Vector
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public Vector getInlinedStmt(Stmt oldStmt) {
	/*
	System.out.println("Invoking getInlinedStmt with: " + oldStmt);
	for (Enumeration e = oldNewStmtTable.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		if (key == oldStmt) {
			System.out.println("Old: " + oldStmt + ", New: " + oldNewStmtTable.get(key));
		}
	}
	for (Enumeration e = newOldStmtTable.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		Object value = newOldStmtTable.get(key);
		if (((Vector) value).contains(oldStmt)) {
			System.out.println("New: " + key + ", Old: " + value);
		}
	}
	*/
	return (Vector) oldNewStmtTable.get(oldStmt);
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getLocalPackingTable() {
	return localPackingTable;
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public Annotation getMethodAnnotationContainingAnnotation(Annotation annotation) {
	for (Enumeration e = annotations.elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((Hashtable) e.nextElement()).elements(); e2.hasMoreElements();) {
			Annotation a = (Annotation) e2.nextElement();
			if (a.contains(annotation) && ((a instanceof MethodDeclarationAnnotation)
				|| (a instanceof ConstructorDeclarationAnnotation))) return a;
		}
	}
	return null;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt
 * @param s ca.mcgill.sable.soot.jimple.Stmt
 */
public Stmt getReplacedStmt(Stmt s) {
	return (Stmt) replacedStmt.get(s);
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getStmtLocalTable() {
	return stmtLocalTable;
}
/**
 * 
 * @param flp edu.ksu.cis.bandera.ase.FilenameLinePair
 * @param a edu.ksu.cis.bandera.annotation.Annotation
 */
public void putFilenameLinePairAnnotation(FilenameLinePair flp, Annotation a) {
	Annotation oldAnn = getAnnotation(flp);
	if (oldAnn == null) {
		filenameLinePairAnnotationTable.put(flp, a);
		//System.out.println(flp + " --> " + a);
	} else {
		String cname = oldAnn.getClass().getName();
		if ("edu.ksu.cis.bandera.annotation.SequentialAnnotation".equals(cname)
				|| "edu.ksu.cis.bandera.annotation.BlockStmtAnnotation".equals(cname)) {
			filenameLinePairAnnotationTable.put(flp, a);
			//System.out.println(flp + " --> " + a);
		}
	}
}
/**
 * 
 * @return boolean
 * @param newLocal ca.mcgill.sable.soot.jimple.Local
 * @param oldLocal ca.mcgill.sable.soot.jimple.Local
 * @param method ca.mcgill.sable.soot.SootMethod
 */
public boolean putInlineLocal(LocalExpr newLocal, LocalExpr oldLocal) {
	if (newOldLocalTable.get(oldLocal) != null) {
		oldLocal = (LocalExpr) newOldLocalTable.get(oldLocal);
	}
	if (oldNewLocalTable.get(oldLocal) == null) {
		oldNewLocalTable.put(oldLocal, new HashSet());
	}
	((HashSet) oldNewLocalTable.get(oldLocal)).add(newLocal);
	if (newOldLocalTable.get(newLocal) != null) {
		if (newOldLocalTable.get(newLocal) != oldLocal) {
			//System.out.println("*** Warning: " + newLocal + " is already mapped to " + newOldLocalTable.get(newLocal) + ". Cannot map it to " + oldLocal);
			return false;
		}
	}
	newOldLocalTable.put(newLocal, oldLocal);
	return true;
}
/**
 * 
 * @return boolean
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param method ca.mcgill.sable.soot.SootMethod
 */
public boolean putInlineStmt(Stmt newStmt, Stmt oldStmt, SootMethod method) {
	try {
		if (newOldStmtTable.get(oldStmt) != null) {
			Stmt s = (Stmt) newOldStmtTable.get(oldStmt);
			if (s != null) {
				if (getContainingAnnotation(s) != null) {
					oldStmt = s;
				}
			}
		}

		// put here to simulate step into
		if (newOldStmtTable.get(newStmt) != null) {
			if (newOldStmtTable.get(newStmt) != oldStmt) {
				//System.out.println("*** Warning: " + newStmt + " is already mapped to " + newOldStmtTable.get(newStmt) + ". Cannot map it to " + oldStmt);
				return false;
			}
		}
		Annotation a = getContainingAnnotation(oldStmt);
		if (a == null) {
			//System.out.println("Failed to put inline annotation: Can't find annotation for oldStmt '"
			//		+ oldStmt + "'@" + Integer.toString(oldStmt.hashCode(), 16).toUpperCase());
			return false;
		}
		inlineTable.put(newStmt, a);
		methodStmtTable.put(newStmt, method);
		if (oldNewStmtTable.get(oldStmt) == null) {
			oldNewStmtTable.put(oldStmt, new Vector());
		}
		((Vector) oldNewStmtTable.get(oldStmt)).addElement(newStmt);
		
		// put here to simulate step over
		/*
		if (newOldStmtTable.get(newStmt) != null) {
			if (newOldStmtTable.get(newStmt) != oldStmt) {
				//System.out.println("*** Warning: " + newStmt + " is already mapped to " + newOldStmtTable.get(newStmt) + ". Cannot map it to " + oldStmt);
				return false;
			}
		}
		*/
		newOldStmtTable.put(newStmt, oldStmt);
		return true;
	} catch (Exception e) {
		//System.out.println("Failed to put inline annotation: " + e.getMessage());
		return false;
	}
}
/**
 * 
 * @param newStmt ca.mcgill.sable.soot.jimple.Stmt
 * @param oldStmt ca.mcgill.sable.soot.jimple.Stmt
 */
public void putReplacedStmt(Stmt newStmt, Stmt oldStmt) {
	replacedStmt.put(oldStmt, newStmt);
}
/**
 * 
 * @param newFilenameLinePairAnnotationTable java.util.Hashtable
 */
public void setFilenameLinePairAnnotationTable(java.util.Hashtable newFilenameLinePairAnnotationTable) {
	filenameLinePairAnnotationTable = newFilenameLinePairAnnotationTable;
}
/**
 * 
 * @param newLocalPackingTable java.util.Hashtable
 */
public void setLocalPackingTable(java.util.Hashtable newLocalPackingTable) {
	localPackingTable = newLocalPackingTable;
}
/**
 * 
 * @param newStmtLocalTable java.util.Hashtable
 */
public void setStmtLocalTable(java.util.Hashtable newStmtLocalTable) {
	stmtLocalTable = newStmtLocalTable;
}
}
