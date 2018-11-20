package edu.ksu.cis.bandera.jext;

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
import ca.mcgill.sable.soot.jimple.*;

public class IRNodes extends AbstractJimpleValueSwitch implements BanderaValueSwitch, BanderaStmtSwitch
{
  Object result;

    public void caseExternalChooseExpr(ExternalChooseExpr v)
    {
	defaultCase(v);
    }

  public void caseAddExpr(AddExpr v)
	{
	  defaultCase(v);
	}
  public void caseAndExpr(AndExpr v)
	{
	  defaultCase(v);
	}
  public void caseArrayRef(ArrayRef v)
	{
	  defaultCase(v);
	}
  public void caseAssignStmt(AssignStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseBreakpointStmt(BreakpointStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseCastExpr(CastExpr v)
	{
	  defaultCase(v);
	}
  public void caseCaughtExceptionRef(CaughtExceptionRef v)
	{
	  defaultCase(v);
	}
  public void caseChooseExpr(ChooseExpr v)
	{
	  defaultCase(v);
	}
  public void caseCmpExpr(CmpExpr v)
	{
	  defaultCase(v);
	}
  public void caseCmpgExpr(CmpgExpr v)
	{
	  defaultCase(v);
	}
  public void caseCmplExpr(CmplExpr v)
	{
	  defaultCase(v);
	}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.ComplementExpr
 */
public void caseComplementExpr(ComplementExpr expr) {
	defaultCase(expr);
}
  public void caseDivExpr(DivExpr v)
	{
	  defaultCase(v);
	}
  public void caseDoubleConstant(DoubleConstant v)
	{
	  defaultCase(v);
	}
  public void caseEnterMonitorStmt(EnterMonitorStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseEqExpr(EqExpr v)
	{
	  defaultCase(v);
	}
  public void caseExitMonitorStmt(ExitMonitorStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseFloatConstant(FloatConstant v)
	{
	  defaultCase(v);
	}
  public void caseGeExpr(GeExpr v)
	{
	  defaultCase(v);
	}
  public void caseGotoStmt(GotoStmt stmt)
	{
	  defaultCase(stmt);
	}
public void caseGtExpr(GtExpr v) {
	defaultCase(v);
}
  public void caseIdentityStmt(IdentityStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseIfStmt(IfStmt stmt)
	{
	  defaultCase(stmt);
	}
/**
 * This method was created in VisualAge.
 * @param v edu.ksu.cis.bandera.jext.InExpr
 */
public void caseInExpr(InExpr v) {
	defaultCase(v);
}
  public void caseInstanceFieldRef(InstanceFieldRef v)
	{
	  defaultCase(v);
	}
  public void caseInstanceOfExpr(InstanceOfExpr v)
	{
	  defaultCase(v);
	}
  public void caseIntConstant(IntConstant v)
	{
	  defaultCase(v);
	}
  public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v)
	{
	  defaultCase(v);
	}
  public void caseInvokeStmt(InvokeStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseLeExpr(LeExpr v)
	{
	  defaultCase(v);
	}
  public void caseLengthExpr(LengthExpr v)
	{
	  defaultCase(v);
	}
  public void caseLocal(Local v)
	{
	  defaultCase(v);
	}
/**
 * 
 * @param v edu.ksu.cis.bandera.jext.LocalExpr
 */
public void caseLocalExpr(LocalExpr v) {
	defaultCase(v);
}
/**
 * 
 * @param e edu.ksu.cis.bandera.jext.LocationTestExpr
 */
public void caseLocationTestExpr(LocationTestExpr e) {
	defaultCase(e);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalAndExpr
 */
public void caseLogicalAndExpr(LogicalAndExpr expr) {
	defaultCase(expr);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalOrExpr
 */
public void caseLogicalOrExpr(LogicalOrExpr expr) {
	defaultCase(expr);
}
  public void caseLongConstant(LongConstant v)
	{
	  defaultCase(v);
	}
  public void caseLookupSwitchStmt(LookupSwitchStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseLtExpr(LtExpr v)
	{
	  defaultCase(v);
	}
  public void caseMulExpr(MulExpr v)
	{
	  defaultCase(v);
	}
  public void caseNeExpr(NeExpr v)
	{
	  defaultCase(v);
	}
  public void caseNegExpr(NegExpr v)
	{
	  defaultCase(v);
	}
  public void caseNewArrayExpr(NewArrayExpr v)
	{
	  defaultCase(v);
	}
  public void caseNewExpr(NewExpr v)
	{
	  defaultCase(v);
	}
  public void caseNewInvokeExpr(NewInvokeExpr v)
	{
	  defaultCase(v);
	}
  public void caseNewMultiArrayExpr(NewMultiArrayExpr v)
	{
	  defaultCase(v);
	}
  public void caseNopStmt(NopStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseNullConstant(NullConstant v)
	{
	  defaultCase(v);
	}
  public void caseOrExpr(OrExpr v)
	{
	  defaultCase(v);
	}
  public void caseParameterRef(ParameterRef v)
	{
	  defaultCase(v);
	}
  public void caseRemExpr(RemExpr v)
	{
	  defaultCase(v);
	}
  public void caseRetStmt(RetStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseReturnStmt(ReturnStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseReturnVoidStmt(ReturnVoidStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseShlExpr(ShlExpr v)
	{
	  defaultCase(v);
	}
  public void caseShrExpr(ShrExpr v)
	{
	  defaultCase(v);
	}
  public void caseSpecialInvokeExpr(SpecialInvokeExpr v)
	{
	  defaultCase(v);
	}
  public void caseStaticFieldRef(StaticFieldRef v)
	{
	  defaultCase(v);
	}
  public void caseStaticInvokeExpr(StaticInvokeExpr v)
	{
	  defaultCase(v);
	}
  public void caseStringConstant(StringConstant v)
	{
	  defaultCase(v);
	}
  public void caseSubExpr(SubExpr v)
	{
	  defaultCase(v);
	}
  public void caseTableSwitchStmt(TableSwitchStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseThisRef(ThisRef v)
	{
	  defaultCase(v);
	}
/**
 * 
 * @param exp edu.ksu.cis.bandera.jext.ThreadExpr
 */
public void caseThreadExpr(ThreadExpr exp) {
  defaultCase(exp);
}
  public void caseThrowStmt(ThrowStmt stmt)
	{
	  defaultCase(stmt);
	}
  public void caseUshrExpr(UshrExpr v)
	{
	  defaultCase(v);
	}
  public void caseVirtualInvokeExpr(VirtualInvokeExpr v)
	{
	  defaultCase(v);
	}
  public void caseXorExpr(XorExpr v)
	{
	  defaultCase(v);
	}
  public void defaultCase(Object v)
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
