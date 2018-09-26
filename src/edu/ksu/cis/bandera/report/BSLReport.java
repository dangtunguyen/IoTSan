package edu.ksu.cis.bandera.report;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001   Robby (robby@cis.ksu.edu)                    *
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
import java.io.*;
import java.util.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;

public class BSLReport implements IBSLReport {
	private static String filename = "bsl.report";

	private TreeSet predicates = new TreeSet();
	private TreeSet assertions = new TreeSet();
	private TreeSet compiledPredicates = new TreeSet();
	private Hashtable compiledTable = new Hashtable();
	//private Hashtable ltlPredicateTable = new Hashtable();
	//private TreeSet ltlPredicates = new TreeSet();

	//private String ltl = "???";
/**
 * 
 */
public void addAssertionsAndPredicates() {
	for (Enumeration e = AssertionSet.getAssertionSetTable().elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((AssertionSet) e.nextElement()).getAssertionTable().elements(); e2.hasMoreElements();) {
			assertions.add((Assertion) e2.nextElement());
		}
	}
	for (Enumeration e = PredicateSet.getPredicateSetTable().elements(); e.hasMoreElements();) {
		for (Enumeration e2 = ((PredicateSet) e.nextElement()).getPredicateTable().elements(); e2.hasMoreElements();) {
			predicates.add((Predicate) e2.nextElement());
		}
	}
}
/**
 * 
 * @param p edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public void addCompiledPredicate(Predicate p, Value v) {
	compiledPredicates.add(p);
	compiledTable.put(p, v);
}
/**
 * 
 * @return java.lang.String
 */
public String getFilename() {
	return filename;
}
/**
 * getTextRepresentation method comment.
 */
public String getTextRepresentation() {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println("BSL Report");
	pw.println("==========");
	pw.println("");
	pw.println("");
	pw.println("");

	pw.println("Assertions");
	pw.println("----------");

	for (Iterator i = assertions.iterator(); i.hasNext();) {
		Assertion a = (Assertion) i.next();
		Vector exceptions = a.getExceptions();
		if (exceptions.size() > 0) {
			pw.println("* " + a.getName() + " didn't pass type checking");
			pw.println("  + constraint: " + a.getConstraint());
			pw.println("  + description: " + a.getDescription());
			pw.println("  + annotation: " + a.getAnnotation());
			pw.println("  + type checking message(s):");
			for (Iterator j = exceptions.iterator(); j.hasNext();) {
				pw.println("    - " + ((Exception) exceptions.iterator()).getMessage());
			}
		} else {
			pw.println("* " + a.getName() + " passed type checking");
			pw.println("  + constraint: " + a.getConstraint());
			pw.println("  + description: " + a.getDescription());
			pw.println("  + annotation: " + a.getAnnotation());
		}
	}
	
	pw.println("");
	pw.println("");

	pw.println("Predicates");
	pw.println("----------");

	for (Iterator i = predicates.iterator(); i.hasNext();) {
		Predicate p = (Predicate) i.next();
		Vector exceptions = p.getExceptions();
		if (exceptions.size() > 0) {
			pw.println("* " + p.getName() + " didn't pass type checking");
			pw.println("  + static: " + p.isStatic());
			pw.println("  + parameters: " + p.getParams());
			pw.println("  + parameter types: " + p.getParamTypes());
			if (p.getLocations() != null) {
				try
				{
					pw.println("  + locations: " + p.getLocations());
				} catch (Exception e)
				{
					pw.println("  + locations: failed to print");
				}
			}
			pw.println("  + constraint: " + p.getConstraint());
			pw.println("  + description: " + p.getDescription());
			pw.println("  + annotation: " + p.getAnnotation());
			pw.println("  + type checking message(s):");
			for (Iterator j = exceptions.iterator(); j.hasNext();) {
				pw.println("    - " + ((Exception) j.next()).getMessage());
			}
		} else {
			pw.println("* " + p.getName() + " passed type checking");
			pw.println("  + static: " + p.isStatic());
			pw.println("  + parameters: " + p.getParams());
			pw.println("  + parameter types: " + p.getParamTypes());
			if (p.getLocations() != null) {
				try
				{
					pw.println("  + locations: " + p.getLocations());
				} catch (Exception e)
				{
					pw.println("  + locations: failed to print");
				}
			}
			pw.println("  + constraint: " + p.getConstraint());
			pw.println("  + description: " + p.getDescription());
			pw.println("  + annotation: " + p.getAnnotation());
		}
	}

	pw.println("");
	pw.println("");

	pw.println("Compiled Predicates");
	pw.println("-------------------");

	for (Iterator i = compiledPredicates.iterator(); i.hasNext();) {
		Predicate p = (Predicate) i.next();
		Value v = (Value) compiledTable.get(p);
		try {
			pw.println("* " + p.getName() + " = " + v);
		} catch (Exception e)
		{
			pw.println("* " + p.getName() + " = failed to print");
		}
	}
	
	pw.println("");
	pw.println("");
/*
	pw.println("LTL Predicates");
	pw.println("---------------");

	for (Iterator i = ltlPredicates.iterator(); i.hasNext();) {
		String name = (String) i.next();
		Value v = (Value) ltlPredicateTable.get(name);
		pw.println("* " + name + " = " + v);
	}

	pw.println("");
	pw.println("");

	pw.println("LTL Never Claim");
	pw.println("---------------");
	pw.println(ltl);
*/	
	return sw.toString();
}
}
