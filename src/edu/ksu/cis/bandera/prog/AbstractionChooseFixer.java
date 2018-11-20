package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import java.util.Vector;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jext.*;
public class AbstractionChooseFixer implements StmtSwitch {
	private static AbstractionChooseFixer fixer = new AbstractionChooseFixer();
	private static Jimple jimple = Jimple.v();
/**
 * AbstractionChooseFixer constructor comment.
 */
private AbstractionChooseFixer() {
}
/**
 * caseAssignStmt method comment.
 */
public void caseAssignStmt(AssignStmt stmt) {
	Value rightOp = stmt.getRightOp();
	if (rightOp instanceof StaticInvokeExpr) {
		StaticInvokeExpr sie = (StaticInvokeExpr) rightOp;
		SootMethod sm = sie.getMethod();
		if ("edu.ksu.cis.bandera.abstraction.Abstraction".equals(sm.getDeclaringClass().getName())
				&& "choose".equals(sm.getName().trim())) {
			int argCount = sie.getArgCount();
			Vector args = new Vector();
			if (argCount == 0) {
				args.add(IntConstant.v(0));
				args.add(IntConstant.v(1));
			} else if (argCount == 1) {
				int constant = ((IntConstant) sie.getArg(0)).value;
				if (constant == 0) {
					System.out.println("*** Error: Choose with zero value ***");
					return;
				}
				int val = 0;
				do {
					if ((constant & 1) == 1) {
						args.add(IntConstant.v(val));
					}
					val++;
					constant >>= 1;
				} while (constant != 0);
			} else {
				throw new RuntimeException("AbstractionChooseFixer class is not updated!!!");
			}
			stmt.setRightOp(new ChooseExpr(args));
		}
	}
}
/**
 * caseBreakpointStmt method comment.
 */
public void caseBreakpointStmt(BreakpointStmt stmt) {}
/**
 * caseEnterMonitorStmt method comment.
 */
public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {}
/**
 * caseExitMonitorStmt method comment.
 */
public void caseExitMonitorStmt(ExitMonitorStmt stmt) {}
/**
 * caseGotoStmt method comment.
 */
public void caseGotoStmt(GotoStmt stmt) {}
/**
 * caseIdentityStmt method comment.
 */
public void caseIdentityStmt(IdentityStmt stmt) {}
/**
 * caseIfStmt method comment.
 */
public void caseIfStmt(IfStmt stmt) {}
/**
 * caseInvokeStmt method comment.
 */
public void caseInvokeStmt(InvokeStmt stmt) {}
/**
 * caseLookupSwitchStmt method comment.
 */
public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {}
/**
 * caseNopStmt method comment.
 */
public void caseNopStmt(NopStmt stmt) {}
/**
 * caseRetStmt method comment.
 */
public void caseRetStmt(RetStmt stmt) {}
/**
 * caseReturnStmt method comment.
 */
public void caseReturnStmt(ReturnStmt stmt) {}
/**
 * caseReturnVoidStmt method comment.
 */
public void caseReturnVoidStmt(ReturnVoidStmt stmt) {}
/**
 * caseTableSwitchStmt method comment.
 */
public void caseTableSwitchStmt(TableSwitchStmt stmt) {}
/**
 * caseThrowStmt method comment.
 */
public void caseThrowStmt(ThrowStmt stmt) {}
/**
 * defaultCase method comment.
 */
public void defaultCase(Object obj) {}
/**
 * 
 * @param s ca.mcgill.sable.soot.jimple.Stmt
 */
public static void fix(Stmt s) {
	s.apply(fixer);
}
/**
 * 
 * @param sc ca.mcgill.sable.soot.SootClass
 */
public static void fix(SootClass sc) {
	for (Iterator i = sc.getMethods().iterator(); i.hasNext(); ) {
		fix((SootMethod) i.next());
	}
}
/**
 * 
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
public static void fix(SootMethod sm) {
	for (Iterator i = ((JimpleBody) sm.getBody(jimple)).getStmtList().iterator(); i.hasNext();) {
		fix((Stmt) i.next());
	}
}
}
