package edu.ksu.cis.bandera.specification.assertion.datastructure;

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
import edu.ksu.cis.bandera.specification.assertion.exception.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
public class AssertionSet {
	private static Hashtable table = new Hashtable();
	private static Hashtable defTable = new Hashtable();
	private Name name;
	private Hashtable assertionTable = new Hashtable();
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public AssertionSet(Name name) {
	this.name = name;
	table.put(name, this);
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public static Assertion getAssertion(Name name) throws AssertionNotDeclaredException {
	try {
		AssertionSet as = getAssertionSet(name.getSuperName());
		Assertion a = (Assertion) as.assertionTable.get(name);
		if (a == null) {
			throw new AssertionNotDeclaredException("Assertion named '" + name + "' is not declared");
		} else return a;
	} catch (AssertionSetNotDeclaredException asnde) {
		throw new AssertionNotDeclaredException(asnde.getMessage());
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public static AssertionSet getAssertionSet(Name name) throws AssertionSetNotDeclaredException {
	AssertionSet result = (AssertionSet) table.get(name);
	if (result == null)
		throw new AssertionSetNotDeclaredException("AssertionSet named '" + name + "' is not declared");
	return result;
}
/**
 *
 * @return java.util.Hashtable
 */
public static Hashtable getAssertionSetTable() {
	return table;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getAssertionTable() {
	return assertionTable;
}
/**
 * 
 * @return java.util.TreeSet
 * @param key java.lang.Object
 */
public static TreeSet getDefinedAssertions(Object key) {
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
 * @param predicate edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion
 */
public void putAssertion(Assertion assertion) throws DuplicateAssertionException {
	Annotation a = assertion.getAnnotation();
	if (a instanceof LabeledStmtAnnotation) {
		a = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
	}
	if (defTable.get(a) == null) {
		defTable.put(a, new TreeSet());
	}
	TreeSet ts = (TreeSet) defTable.get(a);
	ts.add(assertion);
	if (assertionTable.put(assertion.getName(), assertion) != null) {
		throw new DuplicateAssertionException("Assertion named '" + assertion.getName() + "' has already been declared");
	}
}
/**
 * 
 * @param predicateSet edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet
 */
static void putAssertionSet(AssertionSet assertionSet) {
	table.put(assertionSet.getName(), assertionSet);
}
/**
 * 
 */
public static void reset() {
	table = new Hashtable();
	defTable = new Hashtable();
}
}
