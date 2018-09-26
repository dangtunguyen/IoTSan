package edu.ksu.cis.bandera.specification.assertion;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import java.util.*;
import org.apache.log4j.Category;

public class AssertionSliceInterestCollector {
    private static Jimple jimple = Jimple.v();
    private static Category log = Category.getInstance(AssertionSliceInterestCollector.class.getName());

    /**
     * 
     * @return java.util.Collection
     * @param result java.util.Collection
     * @param assertions java.util.Collection
     */
    public static Collection collect(Collection result) {
	SootMethod assertMethod;
	try {
	    SootClassManager scm = CompilationManager.getSootClassManager();
	    SootClass banderaClass = scm.getClass("Bandera");
	    assertMethod = banderaClass.getMethod("assert");
	    //assertMethod = CompilationManager.getSootClassManager().getClass("Bandera").getMethod("assert");
	}
	catch (Exception e) {
	    log.warn("Exception while getting the Bandera assert method.", e);
	    throw new RuntimeException(e.getMessage());
	}

	for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements();) {
	    SootClass sc = (SootClass) e.nextElement();
	    if (!Modifier.isInterface(sc.getModifiers())) {
		for (ca.mcgill.sable.util.Iterator i = sc.getMethods().iterator(); i.hasNext();) {
		    SootMethod sm = (SootMethod) i.next();
		    JimpleBody body = (JimpleBody) sm.getBody(jimple);
		    Object[] stmts = body.getStmtList().toArray();
		    for (int j = 0; j < stmts.length; j++) {
			if (stmts[j] instanceof InvokeStmt) {
			    Value value = ((InvokeStmt) stmts[j]).getInvokeExpr();
			    if (value instanceof StaticInvokeExpr) {
				StaticInvokeExpr staticInvokeExpr = (StaticInvokeExpr) value;
				if (staticInvokeExpr.getMethod() == assertMethod) {
				    if (staticInvokeExpr.getArg(0) instanceof Local)
					result.add(new SliceLocal(sc, sm, (Local) staticInvokeExpr.getArg(0)));
				    int sind = SlicePoint.getStmtIndex(sm, (Stmt) stmts[j]);
				    result.add(new SlicePoint(sc, sm, (Stmt) stmts[j], sind));
				}
			    }
			}
		    }
		}
	    }
	}
	return result;
    }
}
