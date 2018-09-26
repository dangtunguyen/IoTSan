package edu.ksu.cis.bandera.specification.predicate.datastructure;

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
import java.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.specification.predicate.exception.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
public class ReturnPredicate extends MethodPredicate implements Comparable {
	private boolean ret;
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType type
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 * @param node edu.ksu.cis.bandera.specification.predicate.node.Node
 * @param expTable java.util.Hashtable
 * @param exceptions java.util.Vector
 * @param exact boolean
 * @param ret boolean
 */
protected ReturnPredicate(Name name, ClassOrInterfaceType type, Annotation annotation, edu.ksu.cis.bandera.specification.predicate.node.Node node, Vector exceptions, boolean ret) throws DuplicatePredicateException {
	super(name, type, annotation, node, exceptions);
}
/**
 * 
 * @param qv edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable
 */
public void applyThis(QuantifiedVariable qv) {
	locations = new Hashtable();
	methods = new Vector();

	SootMethod sm = ((MethodDeclarationAnnotation) annotation).getSootMethod();
	String smName = sm.getName();
	ca.mcgill.sable.util.List smParmTypes = sm.getParameterTypes();

	if (!((RefType) qv.getType()).className.equals(sm.getDeclaringClass().getName())) {
		try {
			SootClass sc = CompilationManager.getSootClassManager().getClass(((RefType) qv.getType()).className);
			sm = sc.getMethod(smName, smParmTypes);
			annotation = CompilationManager.getAnnotationManager().getAnnotation(sc, sm);
		} catch (Exception e) {
			//ignore
		}
	}
	
	Hashtable methodTable = new Hashtable();
	TreeSet methodSigs = new TreeSet();
	if (!Modifier.isAbstract(sm.getModifiers()) && !Modifier.isInterface(sm.getDeclaringClass().getModifiers())) {
		String smSig = sm.toString();
		methodTable.put(smSig, sm);
		methodSigs.add(smSig);
	}

	// add inheritance methods
	if (qv != null) {
		if (!qv.isExact()) {
			try {
				ClassOrInterfaceType mType = ((MethodDeclarationAnnotation) annotation).getMethod().getDeclaringClassOrInterface();
				Hashtable compiledClasses = CompilationManager.getCompiledClasses();
				for (Enumeration e = compiledClasses.elements(); e.hasMoreElements();) {
					SootClass sc = (SootClass) e.nextElement();
					ClassOrInterfaceType t = Package.getClassOrInterfaceType(new Name(sc.getName()));
					if (!t.isInterface() && (t.hasSuperClass(mType) || t.hasSuperInterface(mType))) {
						for (ca.mcgill.sable.util.Iterator i = sc.getMethods().iterator(); i.hasNext();) {
							SootMethod sm2 = (SootMethod) i.next();
							if (smName.equals(sm2.getName()) && smParmTypes.equals(sm2.getParameterTypes())) {
								String smSig2 = sm2.toString();
								methodTable.put(smSig2, sm2);
								methodSigs.add(smSig2);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	for (Iterator j = methodSigs.iterator(); j.hasNext();) {
		SootMethod m = (SootMethod) methodTable.get(j.next());
		this.methods.add(m);
		Vector locs = new Vector();
		Object[] stmts = ((JimpleBody) m.getBody(Jimple.v())).getStmtList().toArray();
		for (int k = 0; k < stmts.length; k++) {
			if ((stmts[k] instanceof ReturnStmt) || ((stmts[k] instanceof ReturnVoidStmt))) {
				locs.add(stmts[k]);
			}
		}
		locations.put(m, locs);
	}
}
/**
 * 
 * @return int
 * @param o java.lang.Object
 */
public int compareTo(Object o) {
	if (o instanceof InvokePredicate) {
		return 1;
	} else if (o instanceof LocationPredicate) {
		return 1;
	} else if (o instanceof ReturnPredicate) {
		return name.compareTo(((ReturnPredicate) o).getName());
	} else {
		return -1;
	}
}
/**
 * 
 * @return boolean
 */
public boolean hasRet() {
	return ret;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	Object o = ((edu.ksu.cis.bandera.specification.predicate.node.AReturnPropositionDefinition) node).getParams();
	String s = "RETURN " + name + "(" + (o != null? o.toString().trim() : "") + ")";
	if (constraint == null) return s;
	else return s + ": " + constraint.toString().trim();
}
}
