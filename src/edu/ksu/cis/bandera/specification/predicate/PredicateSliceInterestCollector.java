package edu.ksu.cis.bandera.specification.predicate;

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
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;

public class PredicateSliceInterestCollector {
	private static HashSet sliceInterests;
/**
 * 
 * @return java.util.Collection
 * @param predicates java.util.Collection
 */
public static Collection collect(Collection predicates) {
	Jimple jimple = Jimple.v();
	sliceInterests = new HashSet();
	for (Iterator i = predicates.iterator(); i.hasNext();) {
		Predicate p = (Predicate) i.next();

		try {
			Annotation a = p.getAnnotation();
			if (a instanceof LabeledStmtAnnotation) {
				a = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
			}
			SootClass sc = CompilationManager.getSootClassManager().getClass(p.getType().getName().toString());
			SootMethod sm = null;
			if (!(p instanceof ExpressionPredicate)) {
				for (Iterator k = p.getMethods().iterator(); k.hasNext();) {
					sm = (SootMethod) k.next();
					for (Iterator j = ((Vector) p.getLocations().get(sm)).iterator(); j.hasNext();) {
						Stmt s = (Stmt) j.next();
						int sind = SlicePoint.getStmtIndex(sm, s);
						sliceInterests.add(new SlicePoint(sc, sm, s, sind));
						if (p instanceof ReturnPredicate) {
							if (((ReturnPredicate) p).hasRet()) {
								sliceInterests.add(new SliceLocal(sc, sm, (Local) ((ReturnStmt) s).getReturnValue()));
							}
						}
					}
				}					
			}
			if (p.getConstraint() != null) {
				for (Enumeration e = p.getVariablesUsed().elements(); e.hasMoreElements();) {
					Object o = e.nextElement();
					if (o instanceof Local) {
						sliceInterests.add(new SliceLocal(sc, sm, (Local) o));
					} else if (o instanceof Field) {
						try {
							SootClass sootClass = CompilationManager.getSootClassManager().getClass(((Field) o).getDeclaringClassOrInterface().getName().toString());
							SootField sf = sootClass.getField(((Field) o).getName().toString());
							sliceInterests.add(new SliceField(sootClass, sf));
						} catch (Exception ex) { ex.printStackTrace(); }
					} else if ("this".equals(o)) {
						if (sm != null) {
							try {
								Local l = ((JimpleBody) sm.getBody(jimple)).getLocal("JJJCTEMP$0");
								sliceInterests.add(new SliceLocal(sc, sm, l));
							} catch (Exception ex) { ex.printStackTrace(); }
						}
					}
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	return sliceInterests;
}
}
