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
import edu.ksu.cis.bandera.annotation.*;
import ca.mcgill.sable.soot.SootClass;
import edu.ksu.cis.bandera.specification.predicate.exception.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.*;
public class PredicateSet {
	private static Hashtable table = new Hashtable();
	private static Hashtable defTable = new Hashtable();
	private Name name;
	private Hashtable predTable = new Hashtable();
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public PredicateSet(Name name) {
	this.name = name;
	table.put(name, this);
}
/**
 * 
 * @return java.util.TreeSet
 * @param key java.lang.Object
 */
public static TreeSet getDefinedPredicates(Object key) {
	TreeSet set = (TreeSet) defTable.get(key);
	if (set == null) {
		set = new TreeSet();
	}
	return set;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Name getName() {
	return name;
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.datastructure.Predicate
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public static Predicate getPredicate(Name name) throws PredicateNotDeclaredException {
	try {
		PredicateSet ps = getPredicateSet(name.getSuperName());
		Predicate p = (Predicate) ps.predTable.get(name);
		if (p == null) {
			throw new PredicateNotDeclaredException("Predicate named '" + name + "' is not declared");
		} else return p;
	} catch (PredicateSetNotDeclaredException psnde) {
		throw new PredicateNotDeclaredException(psnde.getMessage());
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.datastructure.PredicateSet
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public static PredicateSet getPredicateSet(Name name) throws PredicateSetNotDeclaredException {
	PredicateSet result = (PredicateSet) table.get(name);
	if (result == null)
		throw new PredicateSetNotDeclaredException("PredicateSet named '" + name + "' is not declared");
	return result;
}
/**
 *
 * @return java.util.Hashtable
 */
public static Hashtable getPredicateSetTable() {
	return table;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getPredicateTable() {
	return predTable;
}
/**
 * 
 * @param predicate edu.ksu.cis.bandera.predicate.datastructure.Predicate
 */
public void putPredicate(Predicate predicate) throws DuplicatePredicateException {
	if (predicate instanceof ExpressionPredicate) {
		SootClass sc = (SootClass) CompilationManager.getCompiledClasses().get(predicate.getType().getName().toString());
		if (defTable.get(sc) == null) {
			defTable.put(sc, new TreeSet());
		}
		TreeSet ts = (TreeSet) defTable.get(sc);
		ts.add(predicate);
	} else {
		Annotation a = predicate.getAnnotation();
		if (a instanceof LabeledStmtAnnotation) {
			a = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
		}
		if (defTable.get(a) == null) {
			defTable.put(a, new TreeSet());
		}
		TreeSet ts = (TreeSet) defTable.get(a);
		ts.add(predicate);
	}
	if (predTable.put(predicate.getName(), predicate) != null) {
		throw new DuplicatePredicateException("Predicate named '" + predicate.getName() + "' has already been declared");
	}
}
/**
 * 
 * @param predicateSet edu.ksu.cis.bandera.predicate.datastructure.PredicateSet
 */
static void putPredicateSet(PredicateSet predicateSet) {
	table.put(predicateSet.getName(), predicateSet);
}
/**
 * 
 */
public static void reset() {
	table = new Hashtable();
	defTable = new Hashtable();
}
}
