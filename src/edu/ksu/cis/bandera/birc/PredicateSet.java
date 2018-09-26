package edu.ksu.cis.bandera.birc;

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
import java.io.*;
import java.util.*;

import edu.ksu.cis.bandera.bir.LocVector;
import edu.ksu.cis.bandera.bir.Location;

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

import org.apache.log4j.Category;

/**
 * PredicateSet allows the caller of BIRC to define state predicates
 * that will appear in the translator input.  
 * <p>
 * Predicates on the location of threads should be specified as value
 * predicates using the LocationTestExpr extension to Jimple.
 * <p>
 * Usage: 
 * <pre>
 * PredicateSet predicates = new PredicateSet();
 * Vector stmts = new Vector();
 * Stmt retStmt = ...;
 * stmt.addElement(retStmt);
 * LocationTestExpr test = new LocationTestExpr(stmts);
 * predicates.addValuePredicate("atReturn",test);
 * Expr xGT0Expr = ...;
 * predicates.addValuePredicate("xGT0",xGT0Expr);
 * </pre>
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:32:53 $
 */
public class PredicateSet {

    private static final Category log = Category.getInstance(PredicateSet.class);

    Vector valuePredicates = new Vector();
    Hashtable predicateName = new Hashtable();  // Map pred -> String
    Hashtable predLocations = new Hashtable();  // Map pred -> LocVector
    Hashtable observableSet = new Hashtable();  // Set of observable objects

    void addPredicateLocation(Stmt stmt, Location loc) {
	LocVector predLocs = (LocVector) predLocations.get(stmt);
	if (predLocs == null) {
	    predLocs = new LocVector();
	    predLocations.put(stmt,predLocs);
	}
	predLocs.addElement(loc);
    }

    /**
     * Add a value predicate (a Jimple Expr)
     *
     * @param name name of predicate
     * @param expr Jimple Expr specifying condition when predicate is true
     */
    public void addValuePredicate(String name, Expr expr) {
	if (predicateName.get(name) != null)
	    throw new RuntimeException("Attempt to redefine pred: " + name);
	valuePredicates.addElement(expr);
	predicateName.put(expr,name);
	ObservableExtractor extractor = 
	    new ObservableExtractor(observableSet);
	expr.apply(extractor);   // updates observableSet
    }

    LocVector getPredicateLocations(Stmt stmt) {
	return (LocVector) predLocations.get(stmt);
    }

    public Vector getValuePredicates() { return valuePredicates; }

    public boolean isObservable(Object o) {
	return observableSet.get(o) != null;
    }

    public String predicateName(Object pred) { 
	return (String) predicateName.get(pred);
    }

    public void print() {
	//System.out.println("Predicate Set:");
	log.debug("Predicate Set:");
	for (int i = 0; i < valuePredicates.size(); i++) {
	    Object pred = valuePredicates.elementAt(i);
	    //System.out.println("  " + predicateName(pred) + " = "  + pred);
	    log.debug("  " + predicateName(pred) + " = "  + pred);
	}			       
    }
}
