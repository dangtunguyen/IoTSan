package edu.ksu.cis.bandera.birc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import edu.ksu.cis.bandera.bir.*;

import ca.mcgill.sable.soot.*;

public class Duplicator extends AbstractExprSwitch 
{
	Object result;

	public void caseAddExpr(AddExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new AddExpr(op1, op2));
	}
	public void caseAndExpr(AndExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new AndExpr(op1, op2));
	}
	public void caseArrayExpr(ArrayExpr expr)
	{
	expr.getArray().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getIndex().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new ArrayExpr(op1, op2));
	}
	public void caseAssignAction(AssignAction assign) 
	{
	assign.getLhs().apply(this);
	Expr lhs = (Expr) getResult();
	assign.getRhs().apply(this);
	Expr rhs = (Expr) getResult();
		setResult(new AssignAction(lhs, rhs));
	}
	public void caseBoolLit(BoolLit expr)
	{
		setResult(expr);
	}
	public void caseInternChooseExpr(InternChooseExpr expr)
	{
		setResult(expr);
	}
	public void caseExternChooseExpr(ExternChooseExpr expr)
	{
		setResult(expr);
	}
	public void caseForallExpr(ForallExpr expr)
	{
		setResult(expr);
	}
	public void caseConstant(Constant expr)
	{
		setResult(expr);
	}
	public void caseDerefExpr(DerefExpr expr)
	{
	expr.getTarget().apply(this);
	Expr target = (Expr) getResult();
	setResult(new DerefExpr(target));
	}
	public void caseDivExpr(DivExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new DivExpr(op1, op2));
	}
	public void caseEqExpr(EqExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new EqExpr(op1, op2));
	}
	public void caseIntLit(IntLit expr)
	{
		setResult(expr);
	}
	public void caseLeExpr(LeExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new LeExpr(op1, op2));
	}
	public void caseLengthExpr(LengthExpr expr)
	{
	expr.getArray().apply(this);
	Expr array = (Expr) getResult();
		setResult(new LengthExpr(array));
	}
	public void caseLockAction(LockAction lockAction) 
	{
	lockAction.getLockExpr().apply(this);
	setResult(new LockAction((Expr)getResult(),
				      lockAction.getOperation(),
				      lockAction.getThread()));
	}
	public void caseLockTest(LockTest lockTest) 
	{
	lockTest.getLockExpr().apply(this);
	setResult(new LockTest((Expr)getResult(),
				    lockTest.getOperation(),
				    lockTest.getThread()));
	}
	public void caseLtExpr(LtExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new LtExpr(op1, op2));
	}
	public void caseMulExpr(MulExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new MulExpr(op1, op2));
	}
	public void caseNeExpr(NeExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new NeExpr(op1, op2));
	}
	public void caseNewArrayExpr(NewArrayExpr expr)
	{
	setResult(new NewArrayExpr(expr.getCollection(), expr.getLength()));
	}
	public void caseNewExpr(NewExpr expr)
	{
	setResult(new NewExpr(expr.getCollection()));
	}
	public void caseNotExpr(NotExpr expr)
	{
	expr.getOp().apply(this);
	Expr op1 = (Expr) getResult();
		setResult(new NotExpr(op1));
	}
	public void caseNullExpr(NullExpr expr)
	{
		setResult(expr);
	}
	public void caseOrExpr(OrExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new OrExpr(op1, op2));
	}
	public void caseRecordExpr(RecordExpr expr)
	{
	expr.getRecord().apply(this);
	Expr record = (Expr) getResult();
		setResult(new RecordExpr(record, expr.getField()));	
	}
	public void caseRefExpr(RefExpr expr)
	{
	setResult(new RefExpr(expr.getTarget()));
	}
	public void caseRemExpr(RemExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new RemExpr(op1, op2));
	}
	public void caseStateVar(StateVar expr)
	{
		setResult(expr);
	}
	public void caseSubExpr(SubExpr expr)
	{
	expr.getOp1().apply(this);
	Expr op1 = (Expr) getResult();
	expr.getOp2().apply(this);
	Expr op2 = (Expr) getResult();
		setResult(new SubExpr(op1, op2));
	}
	public void caseRemoteRef(RemoteRef expr)
	{
		setResult(expr);
	}
	public Object getResult()
	{
		return result;
	}
	public void setResult(Object result)
	{
		this.result = result;
	}
}
