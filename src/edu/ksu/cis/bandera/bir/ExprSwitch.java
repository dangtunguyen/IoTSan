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

/**
 * Switch for expression kinds.
 */

public interface ExprSwitch extends Switch
{
  public abstract void caseAddExpr(AddExpr expr);
  public abstract void caseAndExpr(AndExpr expr);
  public abstract void caseArrayExpr(ArrayExpr expr);
  public abstract void caseAssertAction(AssertAction assertAction);
  public abstract void caseAssignAction(AssignAction assign);
  public abstract void caseBoolLit(BoolLit expr);
  public abstract void caseInternChooseExpr(InternChooseExpr expr);
  public abstract void caseExternChooseExpr(ExternChooseExpr expr);
  public abstract void caseForallExpr(ForallExpr expr);
  public abstract void caseConstant(Constant expr);
  public abstract void caseDerefExpr(DerefExpr expr);
  public abstract void caseDivExpr(DivExpr expr);
  public abstract void caseEqExpr(EqExpr expr);
  public abstract void caseInstanceOfExpr(InstanceOfExpr expr);
  public abstract void caseIntLit(IntLit expr);
  public abstract void caseLeExpr(LeExpr expr);
  public abstract void caseLengthExpr(LengthExpr expr);
  public abstract void caseLockAction(LockAction lockAction);
  public abstract void caseLockLit(LockLit expr);
  public abstract void caseLockTest(LockTest lockTest);
  public abstract void caseLtExpr(LtExpr expr);
  public abstract void caseMulExpr(MulExpr expr);
  public abstract void caseNeExpr(NeExpr expr);
  public abstract void caseNewArrayExpr(NewArrayExpr expr);
  public abstract void caseNewExpr(NewExpr expr);
  public abstract void caseNotExpr(NotExpr expr);
  public abstract void caseNullExpr(NullExpr expr);
  public abstract void caseOrExpr(OrExpr expr);
  public abstract void casePrintAction(PrintAction printAction);
  public abstract void caseRecordExpr(RecordExpr expr);
  public abstract void caseRefExpr(RefExpr expr);
  public abstract void caseRefLit(RefLit expr);
  public abstract void caseRemExpr(RemExpr expr);
  public abstract void caseStateVar(StateVar expr);
  public abstract void caseSubExpr(SubExpr expr);
  public abstract void caseThreadAction(ThreadAction threadAction);
  public abstract void caseThreadLocTest(ThreadLocTest threadLocTest);
  public abstract void caseRemoteRef(RemoteRef remoteRef);
  public abstract void caseThreadTest(ThreadTest threadTest);
  public abstract void defaultCase(Object obj);
}
