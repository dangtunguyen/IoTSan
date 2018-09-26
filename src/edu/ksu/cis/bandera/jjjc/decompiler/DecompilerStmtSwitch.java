package edu.ksu.cis.bandera.jjjc.decompiler;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000 Roby Joehanes (robbyjo@cis.ksu.edu)            *
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

/**
 * DecompilerStmtSwitch is a recursive analyzer for statements.
 * The methods are basically each case possible for statements.
 * See DecompilerSwitch for details.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class DecompilerStmtSwitch implements StmtSwitch {
	private static DecompilerStmtSwitch walker = new DecompilerStmtSwitch();
	private Vector result = new Vector();
/**
 * SlabsStmtSwitch constructor comment.
 */
public DecompilerStmtSwitch()
{
	result = new Vector();
}
/**
 * caseAssignStmt method comment.
 */
public void caseAssignStmt(AssignStmt stmt)
{
	Value leftOp = stmt.getLeftOp();
	Value rightOp = stmt.getRightOp();
	boolean isBool = DecompilerInfo.isBool(leftOp) || leftOp.getType().toString().trim().equals("boolean");
	String rightValue = rightOp.toString().trim();

	if (DecompilerInfo.qvDef(leftOp) == 0)  // haven't been defined yet
	{
		result.add(leftOp.getType().toString());
		result.add(" ");
	}

	DecompilerInfo.putVarInfo(leftOp,rightOp);
	result.addAll(DecompilerValueSwitch.evaluate(leftOp));
	DecompilerValueSwitch.reset();
	result.add(" ");
	result.add("=");
	result.add(" ");
	if (isBool)
	{
		if (rightValue.equals("0")) result.add("false");
			else if (rightValue.equals("1")) result.add("true");
				else result.addAll(DecompilerValueSwitch.evaluate(rightOp));
	} else {
		Type leftType = leftOp.getType();
		Type rightType = rightOp.getType();
		if (leftType instanceof IntType && rightType instanceof LongType)
			result.add("("+leftType.toString()+") ");
	     result.addAll(DecompilerValueSwitch.evaluate(rightOp));
	}
	DecompilerValueSwitch.reset();
}
/**
 * caseBreakpointStmt method comment.
 */
public void caseBreakpointStmt(BreakpointStmt stmt)
{
	// Unused
}
/**
 * caseEnterMonitorStmt method comment.
 */
public void caseEnterMonitorStmt(EnterMonitorStmt stmt)
{
	result.add("synchronized");
	result.add(" ");
	result.add("(");
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getOp()));
	DecompilerValueSwitch.reset();
	result.add(")");
}
/**
 * caseExitMonitorStmt method comment.
 */
public void caseExitMonitorStmt(ExitMonitorStmt stmt)
{
	// What's the op for ?
	//result.add("}");
}
/**
 * caseGotoStmt method comment.
 */
public void caseGotoStmt(GotoStmt stmt) {
	result.add(DecompilerUtil.gotoStr);
	result.add(" ");
	result.add(DecompilerGotoMapper.mapStmt((Stmt) stmt.getTarget()));
}
/**
 * caseIdentityStmt method comment.
 */
public void caseIdentityStmt(IdentityStmt stmt)
{
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getLeftOp()));
	DecompilerValueSwitch.reset();
	result.add(" ");
	result.add(":=");
	result.add(" ");
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getRightOp()));
	DecompilerValueSwitch.reset();
}
/**
 * caseIfStmt method comment.
 */
public void caseIfStmt(IfStmt stmt)
{
	result.add("if");
	result.add(" ");
	result.add("(");
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getCondition()));
	DecompilerValueSwitch.reset();
	result.add(")");
	result.add(" ");
	result.add(DecompilerUtil.gotoStr);
	result.add(" ");
	result.add(DecompilerGotoMapper.mapStmt(stmt.getTarget()));
}
/**
 * caseInvokeStmt method comment.
 */
public void caseInvokeStmt(InvokeStmt stmt)
{
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getInvokeExpr()));
	DecompilerValueSwitch.reset();
}
/**
 * caseLookupSwitchStmt method comment.
 */
public void caseLookupSwitchStmt(LookupSwitchStmt stmt)
{
	result.add("switch");
	result.add(" ");
	result.add("(");
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getKey()));
	result.add(")");
	DecompilerValueSwitch.reset();


/*	
// We won't need this at this moment
	result.add("{");

	// process the cases
	int max = stmt.getLookupValues().size();
	for (int i = 0; i<max; i++)
	{
		int cases = stmt.getLookupValue(i);
		result.add("case"+String.valueOf(cases)+":");
		result.add(SlabsUtil.gotoStr);
		result.add(""); // stmtToName?
	}
	Stmt def = (Stmt) stmt.getDefaultTarget();
	if (def!=null)
	{
		result.add("default:");
		result.add(SlabsUtil.gotoStr);
		result.add(""); // stmtToName?
	}
*/

}
/**
 * caseNopStmt method comment.
 */
public void caseNopStmt(NopStmt stmt)
{
	// Basically nothing...
	// result.add("nop");
}
/**
 * caseRetStmt method comment.
 */
public void caseRetStmt(RetStmt stmt)
{
	// Unused
}
/**
 * caseReturnStmt method comment.
 */
public void caseReturnStmt(ReturnStmt stmt)
{
	result.add("return");
	result.add(" ");
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getReturnValue()));
	DecompilerValueSwitch.reset();
}
/**
 * caseReturnVoidStmt method comment.
 */
public void caseReturnVoidStmt(ReturnVoidStmt stmt)
{
	result.add("return");
}
/**
 * caseTableSwitchStmt method comment.
 */
public void caseTableSwitchStmt(TableSwitchStmt stmt)
{
	// Unused
}
/**
 * caseThrowStmt method comment.
 */
public void caseThrowStmt(ThrowStmt stmt)
{
	result.add("throw");
	result.add(" ");
	result.addAll(DecompilerValueSwitch.evaluate(stmt.getOp()));
	DecompilerValueSwitch.reset();
}
/**
 * defaultCase method comment.
 */
public void defaultCase(Object obj)
{
	// Unused basically
}
public static Vector evaluate(Stmt stmt)
{
	if (stmt==null) return new Vector();
	stmt.apply(walker);
	return walker.getResult();
}
private Vector getResult()
{
	return result;
}
public static void reset()
{
	walker.result = new Vector();
}
}
