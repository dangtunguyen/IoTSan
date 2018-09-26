package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
 *               2001, 2002   Radu Iosif (iosif@cis.ksu.edu)         *
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

import java.io.*;
import java.util.*;

/**
 * Print visitor for a transition system.
 * <p>
 * To invoke:
 * <pre>
 * // Parameters 
 * TransSystem system = ...;   // the transition system
 * PrintWriter out = ...;      // PrintWriter to send to
 * BirPrinter.print(system,out);  
 * </pre>
 * or to send to System.out, just:
 * <pre>
 * BirPrinter.print(system);  
 * </pre>
 */

public class BirPrinter extends AbstractExprSwitch implements BirConstants
{
  TransSystem system;
  PrintWriter out;
  boolean inPredicate = false;


  BirPrinter(TransSystem system, PrintWriter out) {
    this.system = system;
    this.out = out;
  }


  public void caseAddExpr(AddExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "+");
  }


  public void caseAndExpr(AndExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "&&");
  }


  public void caseArrayExpr(ArrayExpr expr) 
  {
    expr.getArray().apply(this);
    out.print("[");
    expr.getIndex().apply(this);
    out.print("]");
  }


  public void caseAssertAction(AssertAction assertAction) {
    out.print("assert(");
    assertAction.getCondition().apply(this);
    out.print("); ");
  }


  public void caseAssignAction(AssignAction assign) 
  {
    assign.getLhs().apply(this);
    out.print(" := ");
    assign.getRhs().apply(this);
    out.print("; ");
  }


  public void caseBoolLit(BoolLit expr)
  {
    out.print(expr.getValue());
  }


  private void printChoices(ChooseExpr expr) {
    Vector choices = expr.getChoices();
    ((Expr)choices.elementAt(0)).apply(this);
    for (int i = 1; i < choices.size(); i++) {
      out.print(",");
      ((Expr)choices.elementAt(i)).apply(this);
    }
  }


  public void caseInternChooseExpr(InternChooseExpr expr) {
    out.print("internChoose(");
    printChoices(expr);
    out.print(")");
  }

  
  public void caseExternChooseExpr(ExternChooseExpr expr) {
    out.print("externChoose(");
    printChoices(expr);
    out.print(")");
  }


  public void caseForallExpr(ForallExpr expr) {
    out.print("forall(" + expr.getBaseType().getName() + ")");
  }


  public void caseConstant(Constant expr)
  {
    out.print(expr.getName());
  }


  public void caseDerefExpr(DerefExpr expr)
  {
    expr.getTarget().apply(this);
  }


  public void caseDivExpr(DivExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "/");
  }


  public void caseEqExpr(EqExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "==");
  }


  public void caseInstanceOfExpr(InstanceOfExpr expr)
  {
    out.print("(");
    expr.getRefExpr().apply(this);
    out.print(" instanceof " + expr.getArgType().typeName() + ")");
  }


  public void caseIntLit(IntLit expr)
  {
    out.print(expr.getValue());
  }


  public void caseLeExpr(LeExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "<=");
  }


  public void caseLengthExpr(LengthExpr expr)
  {
    expr.getArray().apply(this);
    out.print(".length");
  }


  public void caseLockAction(LockAction lockAction) 
  {
    String opName = 
      LockAction.operationName(lockAction.getOperation());
    out.print(opName + "(");
    lockAction.getLockExpr().apply(this);
    out.print("); ");
  }


  public void caseLockTest(LockTest lockTest) {
    String opName = 
      LockTest.operationName(lockTest.getOperation());
    out.print(opName + "(");
    lockTest.getLockExpr().apply(this);
    out.print(")");
  }


  public void caseLtExpr(LtExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "<");
  }


  public void caseMulExpr(MulExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "*");
  }


  public void caseNeExpr(NeExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "!=");
  }


  public void caseNewArrayExpr(NewArrayExpr expr)
  {
    out.print("new " + expr.getCollection().getName() + "[");
    expr.getLength().apply(this);
    out.print("]");
  }


  public void caseNewExpr(NewExpr expr)
  {
    out.print("new " + expr.getCollection().getName());
  }


  public void caseNotExpr(NotExpr expr)
  {
    translateUnaryOp(expr.getOp(),"!");
  }


  public void caseNullExpr(NullExpr expr)
  {
    out.print("null");
  }


  public void caseOrExpr(OrExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "||");
  }


  public void casePrintAction(PrintAction printAction) 
  {
    out.print("println(");
    Vector printItems = printAction.getPrintItems();
    for (int i = 0; i < printItems.size(); i++) {
      Object item = printItems.elementAt(i);
      if (i > 0)
	out.print(",");
      if (item instanceof String)
	out.print("\"" + item + "\"");
      else 
	((Expr)item).apply(this);
    }
    out.print("); ");
  }


  public void caseRecordExpr(RecordExpr expr)
  {
    expr.getRecord().apply(this);
    out.print("." + expr.getField().getName());
  }


  public void caseRefExpr(RefExpr expr)
  {
    out.print("ref " + expr.getTarget().getName());
  }


  public void caseRemExpr(RemExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "%");
  }


  public void caseStateVar(StateVar expr)
  {
    if (inPredicate && expr.getThread() != null)
      out.print(expr.getThread().getName() + ":" + expr.getName());
    else
      out.print(expr.getName());
  }


  public void caseSubExpr(SubExpr expr)
  {
    translateBinaryOp(expr.getOp1(), expr.getOp2(), "-");
  }


  public void caseThreadAction(ThreadAction threadAction) 
  {
    String opName = 
      ThreadAction.operationName(threadAction.getOperation());

    if (threadAction.getOperation() == START)
      {
	Expr lhs = threadAction.getLhs();
	if (lhs != null) {
	  lhs.apply(this);
	  out.print(" := ");
	}

	String threadName = threadAction.getThreadArg().getName();
	out.print(opName + " " + threadName + "(");

	Vector args = threadAction.getActuals();
	for (int i = 0; i < args.size(); i ++) {
	  ((Expr) args.elementAt(i)).apply(this);
	  if (i < args.size() - 1)
	    out.print(", ");
	}

	out.print("); ");
      }
    else
      out.print(opName + "; ");
  }

  public void caseThreadLocTest(ThreadLocTest threadLocTest) {
    String threadName = threadLocTest.getThread().getName();
    String locLabel = threadLocTest.getLocation().getLabel();
    out.print(threadName + "(");
    threadLocTest.getLhs().apply(this);
    out.print(")@" + locLabel);
  }

  public void caseRemoteRef(RemoteRef remoteRef) {
    String threadName = remoteRef.getThread().getName();
    String varName = remoteRef.getVar().getName();
    out.print(threadName + "(");
    remoteRef.getLhs().apply(this);
    out.print("):" + varName);
  }

  public void caseThreadTest(ThreadTest threadTest) {
    String opName = ThreadTest.operationName(threadTest.getOperation());
    out.print(opName + "(");
    // String threadName = threadTest.getThreadArg().getName();
    threadTest.getLhs().apply(this);
    out.print(")");
  }


  public void defaultCase(Object obj)
  {
    throw new RuntimeException("Trans type not handled: " + obj);
  }


  /**
   * Print the transition system to System.out
   * @param system the transition system
   */

  public static void print(TransSystem system) {
    PrintWriter writerOut = new PrintWriter(System.out, true);
    print(system, writerOut);
  }


  /**
   * Print the transition system.
   * @param system the transition system
   * @param out a PrintWriter to send the output to
   */

  public static void print(TransSystem system, PrintWriter out) {
    (new BirPrinter(system,out)).run();
  }


  void printDefs() {
    Vector definitions = system.getDefinitions();
    for (int i = 0; i < definitions.size(); i++) {
      Definition def = (Definition) definitions.elementAt(i);
      out.println("  " + def.getName() + " = " + def.getDef() + ";");
    }

    Vector subtypes = system.getSubtypes();
    for (int i = 0; i < subtypes.size(); i ++) {
      Subtype sub = (Subtype) subtypes.elementAt(i);
      out.println("  " + sub.getSubclass().getName() + " extends " 
		  + sub.getSuperclass().getName() + ";");
    }
  }


  public void printLocation(Location loc)
  {
    out.print("loc " + loc.getLabel()  + ":");
    if (loc.getLiveVars() != null) {
      StateVarVector liveVars = loc.getLiveVars();
      if (liveVars.size() == 0)
	out.print(" live { }");
      else {
	out.print(" live { " +  liveVars.elementAt(0).getName());
	for (int i = 1; i < liveVars.size(); i++) 
	  out.print(", " + liveVars.elementAt(i).getName());
	out.print(" } ");
      }
    }
    out.println();
    TransVector outTrans = loc.getOutTrans();
    for (int i = 0; i < outTrans.size(); i++) 
      translateTrans(outTrans.elementAt(i));
  }


  void printPredicates() {
    Vector preds = system.getPredicates();
    if (preds.size() > 0)
      out.println("predicates");
    inPredicate = true;
    for (int i = 0; i < preds.size(); i++) {
      Expr pred = (Expr) preds.elementAt(i);
      String name = system.predicateName(pred);
      out.print("  " + name + " = ");
      pred.apply(this);
      out.println(";");
    }
    inPredicate = false;
  }


  void printThreads() {
    ThreadVector threadVector = system.getThreads();
    for (int i = 0; i < threadVector.size(); i++) {
      BirThread thread = threadVector.elementAt(i);
      if (thread.isMain())
	out.print("main ");
      out.print("thread " + thread.getName());
      printParam(thread);
      out.println("");
      printVars(thread);
      LocVector threadLocVector = thread.getLocations();
      for (int j = 0; j < threadLocVector.size(); j++) 
	printLocation(threadLocVector.elementAt(j));
      out.println("end " + thread.getName() + ";");
    }
  }


  static void printTrans(Transformation trans) {
    PrintWriter writerOut = new PrintWriter(System.out, true);
    TransSystem system = trans.getFromLoc().getThread().getSystem();
    (new BirPrinter(system,writerOut)).translateTrans(trans);
  }


  void printVars(BirThread thread) {
    StateVarVector vars; 
    if (thread == null) {
      vars = system.getStateVars();
      for (int i = 0; i < vars.size(); i++) {
        StateVar var = vars.elementAt(i);
        if (!var.isLocal()) {
	  out.print("  " + var.getName() + " : " 
		    + var.getType().typeSpec());
	  if (var.getInitVal() != null) {
	    out.print(" := ");
	    ((Expr)var.getInitVal()).apply(this);
	  }
	  out.println(";");
        }
      }
    } else {
      vars = thread.getLocals();
      for (int i = 0; i < vars.size(); i++) {
        StateVar var = vars.elementAt(i);
	out.print("  " + var.getName() + " : " 
	          + var.getType().typeSpec());
	if (var.getInitVal() != null) {
	  out.print(" := ");
	  ((Expr)var.getInitVal()).apply(this);
	}
	out.println(";");
      }
    }
  }

  void printParam(BirThread thread) {
    StateVarVector param = thread.getParameters();
    out.print("(");
    for (int i = 0; i < param.size(); i ++) {
      StateVar var = param.elementAt(i);
      out.print(var.getName() + " : " + var.getType().typeSpec());
      if (i < param.size() - 1)
	out.print(", ");
    }
    out.print(")");
  }

  void run() {
    try {
      out.println("process " + system.getName() + "()");
      printDefs();
      printVars(null);
      printThreads();
      printPredicates();
      out.println("end " + system.getName() + ";");
    } catch (RuntimeException e) {
      out.flush();
      out.close();
      throw(e);
    }
  }


  void translateBinaryOp(Expr e1, Expr e2, String op) {
    out.print("(");
    e1.apply(this);
    out.print(" " + op + " ");
    e2.apply(this);
    out.print(")");
  }


  public void translateTrans(Transformation trans)
  {
    out.print("  when ");
    if (trans.getGuard() != null) 
      trans.getGuard().apply(this);
    else
      out.print("true");
    out.print(" do");
    if (! trans.isVisible())
      out.print(" invisible");
    out.print(" { ");
    ActionVector actions = trans.getActions();
    for (int i = 0; i < actions.size(); i++) {
      actions.elementAt(i).apply(this);
      if ((i+1) < actions.size())
	out.print("\n    ");
    }
    out.println("} goto " + trans.getToLoc().getLabel() + ";");
  }


  void translateUnaryOp(Expr e, String op) {
    out.print("(" + op + " ");
    e.apply(this);
    out.print(")");
  }
}
