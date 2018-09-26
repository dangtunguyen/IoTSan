package edu.ksu.cis.bandera.pdgslicer;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.Vector;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import edu.ksu.cis.bandera.pdgslicer.exceptions.*;
/**
 * This class is for extracting and storing slice criterion for deadlock checking.
 */
public class DeadlockRelatedCriterion {
  /**
   * All classes need to be checked for deadlock. These classes will be sliced for
   * deadlock checking.
   */
	private SootClass[] classes;
  /**
   * A list of {@link String String} which is a signature of all method invokes
   * related to deadlock checking. 
   * <br>
   * It's currently including:
   * <br>(1) <code>java.lang.Object.notifyAll():void</code>
   * <br>(2) <code>java.lang.Object.notify():void</code>
   * <br>(3) <code>java.lang.Object.wait():void</code>
   * <br>(4) <code>java.lang.Thread.stop():void</code>
   * <br>(5) <code>java.lang.Thread.destroy():void</code>
   * <br>(6) <code>java.lang.Thread.suspend():void</code>
   * <br>(7) <code>java.lang.Thread.resume():void</code>
   * <br>(8) <code>java.lang.Thread.join():void</code>
   */
	private List deadlockRelatedInvokes = new ArrayList();
  /**
   * A list of extracted {@link SliceInterest SliceInterest}.
   */
	private Vector sliceInterests = new Vector();
  /**
   * Constructor of this class:
   * <br> (1) Initializing {@link #deadlockRelatedInvokes deadlockRelatedInvokes}.
   * <br> (2) {@link #extractSliceInterestForDL() extractSliceInterestForDL()}.
   * <p>
   * @param classArr an array of sootclasses.
   */
  public DeadlockRelatedCriterion(SootClass[] classArr)
	{
	  classes = classArr;
	  String[] invokes =
	  {"java.lang.Object.notifyAll():void",
	   "java.lang.Object.notify():void",
	   "java.lang.Object.wait():void",
	   "java.lang.Thread.stop():void",
	   "java.lang.Thread.destroy():void",
	   "java.lang.Thread.suspend():void",
	   "java.lang.Thread.resume():void",
	   "java.lang.Thread.join():void"
	  };

	  deadlockRelatedInvokes.addAll(new ArraySet(invokes));
	  extractSliceInterestForDL();
	}
  /**
   * Extract slicing interests for deadlock checking.
   * <br> Algorithm: scanning every class in {@link #classes classes}, 
   * then {@link #extractSliceInterestFromMethod(SootClass, SootMethod) 
   * extractSliceInterestFromMethod(sootClass, sootMethod)} 
   * except for <code>interface</code> and <code>native</code> methods.
   */
private void extractSliceInterestForDL() {
	for (int i = 0; i < classes.length; i++) {
		SootClass sootClass = classes[i];
		int modifiers = sootClass.getModifiers();
		if (Modifier.isInterface(modifiers)) {
			System.out.println("Class " + sootClass.getName() + " is an interface");
			continue;
		}
		for (Iterator sootMdIt = sootClass.getMethods().iterator(); sootMdIt.hasNext();) {
			SootMethod sootMethod = (SootMethod) sootMdIt.next();
			if (InfoAnalysis.nativeMdSig.contains(sootMethod) || !Slicer.reachableMethods.contains(sootMethod))
				continue;
			extractSliceInterestFromMethod(sootClass, sootMethod);
		}
	}
}
  /**
   * Extrace slice interests from a method.
   * Put all <code>synchronization</code> statements and deadlock related 
   * method invokes into {@link #sliceInterests sliceInterests}.
   * <p>
   * @param sootClass class where the method is declared.
   * @param sootMethod method which will be processed.
   */
private void extractSliceInterestFromMethod(SootClass sootClass, SootMethod sootMethod) {
	StmtList stmtList = ((MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod)).originalStmtList;
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		if (stmt instanceof EnterMonitorStmt) {
			Value op = ((EnterMonitorStmt) stmt).getOp();
			if (op instanceof Local) {
				sliceInterests.addElement(new SliceLocal(sootClass, sootMethod, (Local) op));
				sliceInterests.addElement(new SlicePoint(sootClass, sootMethod, stmt, stmtList.indexOf(stmt)));
			} else
				throw new ValueTypeException("The operator " + op + " should be local.");
		} else
			if (stmt instanceof ExitMonitorStmt) {
				Value op = ((ExitMonitorStmt) stmt).getOp();
				if (op instanceof Local) {
					sliceInterests.addElement(new SliceLocal(sootClass, sootMethod, (Local) op));
					sliceInterests.addElement(new SlicePoint(sootClass, sootMethod, stmt, stmtList.indexOf(stmt)));
				} else
					throw new ValueTypeException("The operator " + op + " should be local.");
			} else
				if (stmt instanceof InvokeStmt) {
					Value invokeExpr = ((InvokeStmt) stmt).getInvokeExpr();
					if (invokeExpr instanceof VirtualInvokeExpr) {
						VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
						SootMethod invokeMethod = virtualInvokeExpr.getMethod();
						if (deadlockRelatedInvokes.contains(invokeMethod.getSignature())) {
							sliceInterests.addElement(new SlicePoint(sootClass, sootMethod, stmt,stmtList.indexOf(stmt)));
							Iterator useBoxesIt = stmt.getUseBoxes().iterator();
							while (useBoxesIt.hasNext()) {
								ValueBox valueBox = (ValueBox) useBoxesIt.next();
								Value usedValue = valueBox.getValue();
								if (usedValue instanceof Local) {
									sliceInterests.addElement(new SliceLocal(sootClass, sootMethod, (Local)usedValue));
								}
							}
						}
					}
				}
	}
}
/**
 * Get slice interests for deadlock checking.
 * <p>
 * @return {@link #sliceInterests sliceInterests}.
 */
Vector getSliceInterestForDL() {
	return sliceInterests;
}
}
