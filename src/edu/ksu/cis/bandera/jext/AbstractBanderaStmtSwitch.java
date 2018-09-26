package edu.ksu.cis.bandera.jext;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Shawn Laubach (laubach@acm.org)        *
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
//AbstractBanderaStmtSwitch
import ca.mcgill.sable.soot.jimple.*;

/**
 * The extension to the statement switch to handle any possible new
 * statements. 
 *
 * @author <a href="mailto:laubach@cis.ksu.edu">Shawn Laubach</a>
 *
 * @version 0.1
 */
public abstract class AbstractBanderaStmtSwitch 
  extends AbstractStmtSwitch implements BanderaStmtSwitch
{
  public void caseAssignStmt(AssignStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseBreakpointStmt(BreakpointStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseEnterMonitorStmt(EnterMonitorStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseExitMonitorStmt(ExitMonitorStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseGotoStmt(GotoStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseIdentityStmt(IdentityStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseIfStmt(IfStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseInvokeStmt(InvokeStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseLookupSwitchStmt(LookupSwitchStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseNopStmt(NopStmt stmt)
  {
	defaultCase(stmt);
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
  public void caseTableSwitchStmt(TableSwitchStmt stmt)
  {
	defaultCase(stmt);
  }  
  public void caseThrowStmt(ThrowStmt stmt)
  {
	defaultCase(stmt);
  }  
}
