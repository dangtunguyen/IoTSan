package edu.ksu.cis.bandera.jjjc.util;

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
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.LinkedList;

public class JimpleStmtCloner implements StmtSwitch {
	private static JimpleStmtCloner cloner = new JimpleStmtCloner();
	private static Jimple jimple = Jimple.v();
	private static Hashtable targets = new Hashtable();
	private static JimpleBody body;
	private Stmt result = null;
/**
 * 
 */
private JimpleStmtCloner() {
}
/**
 * caseAssignStmt method comment.
 */
public void caseAssignStmt(AssignStmt stmt) {
	result = jimple.newAssignStmt(JimpleValueCloner.clone(stmt.getLeftOp()),
			JimpleValueCloner.clone(stmt.getRightOp()));
}
/**
 * caseBreakpointStmt method comment.
 */
public void caseBreakpointStmt(BreakpointStmt stmt) {
	result = jimple.newBreakpointStmt();
}
/**
 * caseEnterMonitorStmt method comment.
 */
public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
	result = jimple.newEnterMonitorStmt(JimpleValueCloner.clone(stmt.getOp()));
}
/**
 * caseExitMonitorStmt method comment.
 */
public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
	result = jimple.newExitMonitorStmt(JimpleValueCloner.clone(stmt.getOp()));
}
/**
 * caseGotoStmt method comment.
 */
public void caseGotoStmt(GotoStmt stmt) {
	result = jimple.newGotoStmt(JimpleStmtCloner.clone((Stmt) stmt.getTarget()));
}
/**
 * caseIdentityStmt method comment.
 */
public void caseIdentityStmt(IdentityStmt stmt) {
	result = jimple.newIdentityStmt(JimpleValueCloner.clone(stmt.getLeftOp()),
			JimpleValueCloner.clone(stmt.getRightOp()));
}
/**
 * caseIfStmt method comment.
 */
public void caseIfStmt(IfStmt stmt) {
	result = jimple.newIfStmt(JimpleValueCloner.clone(stmt.getCondition()),
			JimpleStmtCloner.clone(stmt.getTarget()));
}
/**
 * caseInvokeStmt method comment.
 */
public void caseInvokeStmt(InvokeStmt stmt) {
	result = jimple.newInvokeStmt(JimpleValueCloner.clone(stmt.getInvokeExpr()));
}
/**
 * caseLookupSwitchStmt method comment.
 */
public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
	Stmt defaultTarget = JimpleStmtCloner.clone((Stmt) stmt.getDefaultTarget());
	LinkedList newTargets = new LinkedList();
	LinkedList newValues = new LinkedList();

	for (Iterator i = stmt.getTargets().iterator(); i.hasNext();) {
		newTargets.addLast(JimpleStmtCloner.clone((Stmt) i.next()));
	}

	for (Iterator i = stmt.getLookupValues().iterator(); i.hasNext();) {
		newValues.addLast(JimpleValueCloner.clone((Value) i.next()));
	}

	result = jimple.newLookupSwitchStmt(JimpleValueCloner.clone(stmt.getKey()), newValues,
				newTargets, defaultTarget);
}
/**
 * caseNopStmt method comment.
 */
public void caseNopStmt(NopStmt stmt) {
	result = jimple.newNopStmt();
}
/**
 * caseRetStmt method comment.
 */
public void caseRetStmt(RetStmt stmt) {
	result = jimple.newRetStmt(JimpleValueCloner.clone(stmt.getStmtAddress()));
}
/**
 * caseReturnStmt method comment.
 */
public void caseReturnStmt(ReturnStmt stmt) {
	result = jimple.newReturnStmt(JimpleValueCloner.clone(stmt.getReturnValue()));
}
/**
 * caseReturnVoidStmt method comment.
 */
public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
	result = jimple.newReturnVoidStmt();
}
/**
 * caseTableSwitchStmt method comment.
 */
public void caseTableSwitchStmt(TableSwitchStmt stmt) {
	Stmt defaultTarget = JimpleStmtCloner.clone((Stmt) stmt.getDefaultTarget());
	LinkedList newTargets = new LinkedList();

	for (Iterator i = stmt.getTargets().iterator(); i.hasNext();) {
		newTargets.addLast(JimpleStmtCloner.clone((Stmt) i.next()));
	}

	result = jimple.newTableSwitchStmt(JimpleValueCloner.clone(stmt.getKey()), stmt.getLowIndex(),
				stmt.getHighIndex(), newTargets, defaultTarget);
}
/**
 * caseThrowStmt method comment.
 */
public void caseThrowStmt(ThrowStmt stmt) {
	result = jimple.newThrowStmt(JimpleValueCloner.clone(stmt.getOp()));
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public static Stmt clone(Stmt stmt) {
	if (targets.get(stmt) != null) return (Stmt) targets.get(stmt);
	
	stmt.apply(cloner);
	Stmt result = cloner.getResult();
	targets.put(stmt, result);
	
	return result;
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public static void copyTrap(JimpleBody body) {
	for (Iterator i = body.getTraps().iterator(); i.hasNext();) {
		JTrap trap = (JTrap) i.next();
		Stmt begin = (Stmt) targets.get(trap.getBeginUnit());
		Stmt end = (Stmt) targets.get(trap.getEndUnit());
		Stmt handler = (Stmt) targets.get(trap.getHandlerUnit());
		if ((begin != null) && (end != null) && (handler != null)) {
			JimpleStmtCloner.body.addTrap(jimple.newTrap(trap.getException(), begin, end, handler));
		}
	}
}
/**
 * defaultCase method comment.
 */
public void defaultCase(Object obj) {
	result = null;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt
 */
private Stmt getResult() {
	return result;
}
/**
 * 
 */
public static void reset() {
	targets = new Hashtable();
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public static void setBody(JimpleBody b) {
	body = b;
	JimpleValueCloner.setBody(b);
}
}
