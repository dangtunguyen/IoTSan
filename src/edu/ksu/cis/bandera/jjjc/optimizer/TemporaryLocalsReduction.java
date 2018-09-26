package edu.ksu.cis.bandera.jjjc.optimizer;

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
import java.util.*;
import edu.ksu.cis.bandera.annotation.*;
import ca.mcgill.sable.util.Iterator;

public class TemporaryLocalsReduction {
/**
 * 
 * @return 
 * @param body JimpleBody
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public static void reduce(JimpleBody body, Annotation annotation) {
	SootClass sc = body.getMethod().getDeclaringClass();
	StmtList stmtList = body.getStmtList();
	Hashtable localToDefCount = new Hashtable();
	Hashtable localToUseCount = new Hashtable();

	// Count number of definitions for each local.
	{
		for (Iterator i = stmtList.iterator(); i.hasNext();) {
			Stmt s = (Stmt) i.next();
			if (s instanceof DefinitionStmt && ((DefinitionStmt) s).getLeftOp() instanceof Local) {
				Local l = (Local) ((DefinitionStmt) s).getLeftOp();
				if (!localToDefCount.containsKey(l))
					localToDefCount.put(l, new Integer(1));
				else
					localToDefCount.put(l, new Integer(((Integer) localToDefCount.get(l)).intValue() + 1));
			}
			for (Iterator j = s.getUseBoxes().iterator(); j.hasNext();) {
				ValueBox vb = (ValueBox) j.next();
				if (vb.getValue() instanceof Local) {
					Local l = (Local) vb.getValue();
					if (!localToUseCount.containsKey(l))
						localToUseCount.put(l, new Integer(1));
					else
						localToUseCount.put(l, new Integer(((Integer) localToUseCount.get(l)).intValue() + 1));
				}
			}
		}
	}

	CompleteStmtGraph graph = new CompleteStmtGraph(stmtList);
	Local thisLocal = null;
	Stmt thisIdentityStmt = null;
	
	for (Iterator i = graph.pseudoTopologicalOrderIterator(); i.hasNext();) {
		Stmt stmt = (Stmt) i.next();
		if (stmt instanceof JAssignStmt) {
			Value leftOp = ((JAssignStmt) stmt).getLeftOp();
			Value rightOp = ((JAssignStmt) stmt).getRightOp();
			if ((leftOp instanceof Local) && (rightOp instanceof Local)) {
				Local leftLocal = (Local) leftOp;
				Local rightLocal = (Local) rightOp;
				if (!leftLocal.getName().startsWith("JJJCTEMP$") && rightLocal.getName().startsWith("JJJCTEMP$")
						&& (((Integer) localToDefCount.get(rightLocal)).intValue() == 1)) {
					for (Iterator j = stmtList.iterator(); j.hasNext();) {
						for (Iterator k = ((Stmt) j.next()).getUseAndDefBoxes().iterator(); k.hasNext();) {
							ValueBox vb = (ValueBox) k.next();
							if (vb.getValue() == rightLocal) {
								vb.setValue(leftLocal);
							}
						}
					}
				}
			} else if ((leftOp instanceof Local) && (rightOp instanceof InvokeExpr)) {
				Local leftLocal = (Local) leftOp;
				if (leftLocal.getName().startsWith("JJJCTEMP$") && (localToUseCount.get(leftLocal) == null)) {
					Stmt s = Jimple.v().newInvokeStmt(rightOp);
					try {
						annotation.replaceStmt(stmt, s);
					} catch (AnnotationException e) {}
					stmtList.set(stmtList.indexOf(stmt), s);
					body.redirectJumps(stmt, s);
				}
			}
		} else if (stmt instanceof JIdentityStmt) {
			Value leftOp = ((JIdentityStmt) stmt).getLeftOp();
			Value rightOp = ((JIdentityStmt) stmt).getRightOp();
			if (rightOp instanceof ThisRef) {
				if (thisLocal == null) {
					thisLocal = (Local) leftOp;
					thisIdentityStmt = stmt;
				} else {
					for (Iterator j = stmtList.iterator(); j.hasNext();) {
						for (Iterator k = ((Stmt) j.next()).getUseAndDefBoxes().iterator(); k.hasNext();) {
							ValueBox vb = (ValueBox) k.next();
							if (vb.getValue() == leftOp) {
								vb.setValue(thisLocal);
							}
						}
					}
				}
			}
		}
	}

	for (Iterator i = stmtList.iterator(); i.hasNext();) {
		Stmt s = (Stmt) i.next();
		if ((s instanceof JIdentityStmt) && (((JIdentityStmt) s).getRightOp() instanceof ThisRef)
				&& (s != thisIdentityStmt))
			stmtList.remove(s);
	}
}
}
