package edu.ksu.cis.bandera.bir;

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
import ca.mcgill.sable.util.*;



public abstract class AbstractExprSwitch implements ExprSwitch
{
	Object result;

	public void caseAddExpr(AddExpr expr)
	{
		defaultCase(expr);
	}
	public void caseAndExpr(AndExpr expr)
	{
		defaultCase(expr);
	}
	public void caseArrayExpr(ArrayExpr expr)
	{
		defaultCase(expr);
	}
	public void caseAssertAction(AssertAction assertAction) 
	{
		defaultCase(assertAction);
	}
	public void caseAssignAction(AssignAction assign) 
	{
		defaultCase(assign);
	}
	public void caseBoolLit(BoolLit expr)
	{
		defaultCase(expr);
	}
	public void caseInternChooseExpr(InternChooseExpr expr)
	{
		defaultCase(expr);
	}
	public void caseExternChooseExpr(ExternChooseExpr expr)
	{
		defaultCase(expr);
	}
        public void caseForallExpr(ForallExpr expr)
        {
                defaultCase(expr);
        }
	public void caseConstant(Constant expr)
	{
		defaultCase(expr);
	}
	public void caseDerefExpr(DerefExpr expr)
	{
		defaultCase(expr);
	}
	public void caseDivExpr(DivExpr expr)
	{
		defaultCase(expr);
	}
	public void caseEqExpr(EqExpr expr)
	{
		defaultCase(expr);
	}
	public void caseInstanceOfExpr(InstanceOfExpr expr)
	{
		defaultCase(expr);
	}
	public void caseIntLit(IntLit expr)
	{
		defaultCase(expr);
	}
	public void caseLeExpr(LeExpr expr)
	{
		defaultCase(expr);
	}
	public void caseLengthExpr(LengthExpr expr)
	{
		defaultCase(expr);
	}
	public void caseLockAction(LockAction lockAction) 
	{
		defaultCase(lockAction);
	}
	public void caseLockLit(LockLit expr)
	{
		defaultCase(expr);
	}
	public void caseLockTest(LockTest lockTest) 
	{
		defaultCase(lockTest);
	}
	public void caseLtExpr(LtExpr expr)
	{
		defaultCase(expr);
	}
	public void caseMulExpr(MulExpr expr)
	{
		defaultCase(expr);
	}
	public void caseNeExpr(NeExpr expr)
	{
		defaultCase(expr);
	}
	public void caseNewArrayExpr(NewArrayExpr expr)
	{
		defaultCase(expr);
	}
	public void caseNewExpr(NewExpr expr)
	{
		defaultCase(expr);
	}
	public void caseNotExpr(NotExpr expr)
	{
		defaultCase(expr);
	}
	public void caseNullExpr(NullExpr expr)
	{
		defaultCase(expr);
	}
	public void caseOrExpr(OrExpr expr)
	{
		defaultCase(expr);
	}
	public void casePrintAction(PrintAction printAction) 
	{
		defaultCase(printAction);
	}
	public void caseRecordExpr(RecordExpr expr)
	{
		defaultCase(expr);
	}
	public void caseRefExpr(RefExpr expr)
	{
		defaultCase(expr);
	}
	public void caseRefLit(RefLit expr) 
	{
		defaultCase(expr);
	}
	public void caseRemExpr(RemExpr expr)
	{
		defaultCase(expr);
	}
	public void caseStateVar(StateVar expr)
	{
		defaultCase(expr);
	}
	public void caseSubExpr(SubExpr expr)
	{
		defaultCase(expr);
	}
	public void caseThreadAction(ThreadAction threadAction) 
	{
		defaultCase(threadAction);
	}
	public void caseThreadLocTest(ThreadLocTest threadLocTest) 
	{
		defaultCase(threadLocTest);
	}
	public void caseRemoteRef(RemoteRef remoteRef) 
	{
		defaultCase(remoteRef);
	}
	public void caseThreadTest(ThreadTest threadTest) 
	{
		defaultCase(threadTest);
	}
	public void defaultCase(Object obj)
	{
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
