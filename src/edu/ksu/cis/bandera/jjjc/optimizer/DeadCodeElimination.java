package edu.ksu.cis.bandera.jjjc.optimizer;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import ca.mcgill.sable.util.*;

import java.util.*;

public final class DeadCodeElimination {
/**
 * 
 * @param body JimpleBody
 * @param cfanns java.util.Vector
 */
public static void eliminate(JimpleBody body) {
	StmtList stmtList = body.getStmtList();
	Object[] stmts = stmtList.toArray();
	StmtGraph g = new CompleteStmtGraph(stmtList);

	try {
		for (int i = 0; i < stmts.length; i++) {
		  if ((i != 0) && (g.getPredsOf((Stmt) stmts[i]).size() == 0)) {
				stmtList.remove(stmts[i]);
			} else if (stmts[i] instanceof JNopStmt) {
				stmtList.remove(stmts[i]);
			} else if ((stmts[i] instanceof JAssignStmt)
					&& (((JAssignStmt) stmts[i]).getLeftOp() == ((JAssignStmt) stmts[i]).getRightOp())) {
				stmtList.remove(stmts[i]);
			}
		}
	} catch (Exception e) {}
}
}
