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

/**
 * Jimple statement switch to determine if statement is visible.
 * <p>
 * Currently, any statement that touches a heap variable, a lock,
 * or a thread is visible.
 */

public class VisibleExtractor extends AbstractExprSwitch
{
	boolean visible = false;   // Stmt is visible?

	public void caseAddExpr(AddExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseAndExpr(AndExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseArrayExpr(ArrayExpr expr)
	{
	expr.getArray().apply(this);
	expr.getIndex().apply(this);
	}
	public void caseAssignAction(AssignAction assign) 
	{
	assign.getLhs().apply(this);
	assign.getRhs().apply(this);
	}
	public void caseDerefExpr(DerefExpr expr)
	{
	visible = true;
	expr.getTarget().apply(this);
	}
	public void caseDivExpr(DivExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseEqExpr(EqExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseLeExpr(LeExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseLengthExpr(LengthExpr expr)
	{
	expr.getArray().apply(this);
	}
	public void caseLockAction(LockAction lockAction) 
	{
		visible = true;
	}
	public void caseLockTest(LockTest lockTest) 
	{
	visible = true;
	}
	public void caseLtExpr(LtExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseMulExpr(MulExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseNeExpr(NeExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseNewExpr(NewExpr expr)
	{
	}
	public void caseNotExpr(NotExpr expr)
	{
	expr.getOp().apply(this);
	}
	public void caseNullExpr(NullExpr expr)
	{
	}
	public void caseOrExpr(OrExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void casePrintAction(PrintAction printAction) 
	{
	}
	public void caseRecordExpr(RecordExpr expr)
	{
	expr.getRecord().apply(this);
	}
	public void caseRefExpr(RefExpr expr)
	{
	}
	public void caseRemExpr(RemExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseStateVar(StateVar expr)
	{
		if (! expr.isLocal() && ! expr.isConstant())
	    visible = true;
	}
	public void caseSubExpr(SubExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseThreadAction(ThreadAction threadAction) 
	{
		visible = true;
	}
	public void caseThreadTest(ThreadTest threadTest) 
	{
		visible = true;
	}
	public boolean isVisible() {
	return visible;
	}
	public void reset() {
	visible = false;
	}
}
