package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import java.util.*;
import java.util.Hashtable;
import org.apache.log4j.Category;
public class RenameStatement extends AbstractStmtSwitch {
	private static Category log = 
			Category.getInstance(Inline.class.getName());
	static int count = 0;
	int id = ++count;
	String methodString;
	List parameters;
	Hashtable stmt2stmt;
	Value var;
	Stmt ret;
	Hashtable locals;
	RenameExpression re;
	protected edu.ksu.cis.bandera.annotation.AnnotationManager annotationManager;
	public RenameStatement(SootMethod method, String methodString, Value var, List parameters, Stmt ret, Hashtable locals, Hashtable stmt2stmt, SootMethod topMethod) {
		this.annotationManager = edu.ksu.cis.bandera.jjjc.CompilationManager.getAnnotationManager();
		this.methodString = methodString;
		this.var = var;
		this.parameters = parameters;
		this.ret = ret;
		this.locals = locals;
		this.stmt2stmt = stmt2stmt;
		re = new RenameExpression(methodString, parameters, locals, topMethod, method);
	}
	/**
	 * Assign - if the right hand side is an
	 * invoke, it inlines the method otherwise it rename
	 * the left and right operand and returns the new
	 * statement.
	 */
	public void caseAssignStmt(AssignStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		stmts.add(Jimple.v().newAssignStmt(re.renameExpr(s.getLeftOp()), re.renameExpr(s.getRightOp())));
		setResult(stmts);
	}
	public void caseEnterMonitorStmt(EnterMonitorStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		stmts.add(Jimple.v().newEnterMonitorStmt(re.renameExpr(s.getOp())));
		setResult(stmts);
	}
	public void caseExitMonitorStmt(ExitMonitorStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		stmts.add(Jimple.v().newExitMonitorStmt(re.renameExpr(s.getOp())));
		setResult(stmts);
	}
	/**
	 * Goto - renames the target statement and then
	 * creates a new goto to the new target.
	 */
	public void caseGotoStmt(GotoStmt s) {
		ca.mcgill.sable.util.List target = (ca.mcgill.sable.util.List) stmt2stmt.get(s.getTarget());
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		if (target == null) {
			target = renameStmt((Stmt) s.getTarget());
		}
		stmts.add(Jimple.v().newGotoStmt((Stmt) target.get(0)));
		setResult(stmts);
	}
	/**
	 * Identity - renames the left and right
	 * operand and returns the new statement.
	 */
	public void caseIdentityStmt(IdentityStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		Value v = re.renameExpr(s.getRightOp());
		if (v instanceof ParameterRef || v instanceof ThisRef || v instanceof CaughtExceptionRef)
		{
			stmts.add(Jimple.v().newIdentityStmt(re.renameExpr(s.getLeftOp()), v));
		}
		else
		{
//			Value newLeft = re.renameExpr(s.getLeftOp());
//			/* [Thomas, May 23, 2017]
//			 * */
//			if(v.toString().startsWith("_static_") && (s.getLeftOp() instanceof Local))
//			{
//				if(v.toString().equals("_static_LightOffWhenClosed_switch1"))
//				{
//					System.out.println();
//				}
//				((Local)newLeft).setName(v.toString());
//			}
//			else
//			{
//				stmts.add(Jimple.v().newAssignStmt(newLeft, v));
//			}
			stmts.add(Jimple.v().newAssignStmt(re.renameExpr(s.getLeftOp()), v));
		}
		setResult(stmts);
	}
	/**
	 * If - renames the target statement and if expression
	 * and then creates a new if with a branch to the new
	 * target.
	 */
	public void caseIfStmt(IfStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		ca.mcgill.sable.util.List target = (ca.mcgill.sable.util.List) stmt2stmt.get(s.getTarget());
		if (target == null) {
			target = renameStmt((Stmt) s.getTarget());
		}
		stmts.add(Jimple.v().newIfStmt(re.renameExpr(s.getCondition()), (Stmt) target.get(0)));
		setResult(stmts);
	}
	public void caseInvokeStmt(InvokeStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		stmts.add(Jimple.v().newInvokeStmt(re.renameExpr(s.getInvokeExpr())));
		setResult(stmts);
	}
	/**
	 * 
	 * @param stmt ca.mcgill.sable.soot.jimple.LookupSwitchStmt
	 */
	public void caseLookupSwitchStmt(LookupSwitchStmt s) {
		ca.mcgill.sable.util.List targets = new ca.mcgill.sable.util.LinkedList();
		for (int i = 0; i < s.getTargetCount(); i++) {
			ca.mcgill.sable.util.List result = (ca.mcgill.sable.util.List) stmt2stmt.get(s.getTarget(i));
			ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
			if (result == null) {
				targets.add(renameStmt((Stmt) s.getTarget(i)).get(0));
			} else {
				targets.add(result.get(0));
			}
		}


		Stmt defaultTarget;
		{
			ca.mcgill.sable.util.List result = (ca.mcgill.sable.util.List) stmt2stmt.get(s.getDefaultTarget());
			ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
			if (result == null) {
				defaultTarget = (Stmt) renameStmt((Stmt) s.getDefaultTarget()).get(0);
			} else {
				defaultTarget = (Stmt) result.get(0);
			}
		}


		Value key = re.renameExpr(s.getKey());


		ca.mcgill.sable.util.List lookupValues = new ca.mcgill.sable.util.LinkedList();
		{
			for (ca.mcgill.sable.util.Iterator i = s.getLookupValues().iterator(); i.hasNext();) {
				lookupValues.add(new Integer(((Integer) i.next()).intValue()));
			}
		}


		ca.mcgill.sable.util.LinkedList stmts = new ca.mcgill.sable.util.LinkedList();
		stmts.add(Jimple.v().newLookupSwitchStmt(key, lookupValues, targets, defaultTarget));
		setResult(stmts);
	}
	public void caseNopStmt(NopStmt v) {
		ca.mcgill.sable.util.List l = new ca.mcgill.sable.util.VectorList();
		l.add(Jimple.v().newNopStmt());
		setResult(l);
	}
	/**
	 * Return - renames the expression and then if there
	 * is a variable the return value gets assigned to,
	 * creates a new assignment statement, otherwise it
	 * creates a new return statement.
	 */
	public void caseReturnStmt(ReturnStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		if (var == null)
			stmts.add(Jimple.v().newReturnStmt(re.renameExpr(s.getReturnValue())));
		else {
			Stmt stmt = Jimple.v().newAssignStmt(var, re.renameExpr(s.getReturnValue()));
			stmts.add(stmt);
			stmt2stmt.put(s, stmts);
			stmt = Jimple.v().newGotoStmt(ret);
			stmts.add(stmt);
		}
		setResult(stmts);
	}
	/**
	 * ReturnVoid - renames the expression then if there
	 * is a statement to return to, creates a goto to that
	 * statement, otherwise creates a new ReturnVoid.
	 */
	public void caseReturnVoidStmt(ReturnVoidStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		if (ret == null)
			stmts.add(Jimple.v().newReturnVoidStmt());
		else {
			Stmt stmt = Jimple.v().newGotoStmt(ret);
			stmts.add(stmt);
		}
		setResult(stmts);
	}
	public void caseThrowStmt(ThrowStmt s) {
		ca.mcgill.sable.util.List stmts = new ca.mcgill.sable.util.ArrayList();
		stmts.add(Jimple.v().newThrowStmt(re.renameExpr(s.getOp())));
		setResult(stmts);
	}
	public void defaultCase(Object obj) {
		log.fatal("Unhandled Statement:  " + obj.getClass() + "\n\t " + obj);
		throw new RuntimeException("Unhandled Statement:  " + obj.getClass());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/21/00 4:16:30 PM)
	 * @return edu.ksu.cis.bandera.prog.RenameExpression
	 */
	public RenameExpression getRenameExpr() {
		return re;
	}
	/**
	 * This method was created in VisualAge.
	 * @return boolean
	 * @param s java.lang.String
	 */
	public static boolean javaPrefix(String s) {
		if (s.length() >= 4)
			return s.substring(0, 4).equals("java");
		else
			return false;
	}
	protected ca.mcgill.sable.util.List renameStmt(Stmt s) {
		if (stmt2stmt.get(s) != null)
			return (ca.mcgill.sable.util.List) stmt2stmt.get(s);
		s.apply(this);
		stmt2stmt.put(s, getResult());
		return (ca.mcgill.sable.util.List) getResult();
	}
	public String toString()
	{
		return "RS " + id;
	}  
}
