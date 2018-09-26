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
import java.util.Vector;

public class ThreadAction extends AbstractAction implements BirConstants {
   
  Expr lhs;
  BirThread threadArg;
  int operation;
  BirThread thread;
  Vector actuals = new Vector();

  public ThreadAction() {}

  public ThreadAction(int operation, BirThread thread)
  {
    this.operation = operation;
    this.thread = thread;
  }

  public void apply(Switch sw)
  {
    ((ExprSwitch) sw).caseThreadAction(this);
  }

  public void setLhs(Expr lhs) { this.lhs = lhs; }
  public Expr getLhs() { return lhs; }
  public void setOperation(int operation) { this.operation = operation; }
  public int getOperation() { return operation; }
  public void setThread(BirThread thread) { this.thread = thread; }
  public BirThread getThread() { return thread; }
  public void setThreadArg(BirThread threadArg) { this.threadArg = threadArg; }
  public BirThread getThreadArg() { return threadArg; }
  public void addActual(Expr actual) { actuals.add(actual); }
  public Vector getActuals() { return actuals; }

  public boolean isExit() {
    return operation == EXIT;
  }
//    public boolean isJoin() {
//      return operation == JOIN;
//    }
  public boolean isStart() {
    return operation == START;
  }
  public boolean isThreadAction(int operation) { 
    return (operation & this.operation) != 0;
  }
  public static int operationCode(String methodName) {
    if (methodName.equals("start"))
      return START;
//      if (methodName.equals("join"))
//        return JOIN;
    if (methodName.equals("exit"))
      return EXIT;

    return INVALID;
  }
  public static String operationName(int operation) {
    switch (operation) {
    case START: return "start";
//    case JOIN: return "join";
    case EXIT: return "exit";
    default: return "?";
    }
  }
}




