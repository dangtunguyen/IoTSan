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
import java.util.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.specification.assertion.exception.*;
import ca.mcgill.sable.soot.*;
public class LocationAssertion extends Assertion implements Comparable {
	private String label;
/**
 * LocationAssertion constructor comment.
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param exp edu.ksu.cis.bandera.jjjc.node.PExp
 * @param exceptions java.util.Vector
 * @param label java.lang.String
 */
public LocationAssertion(Annotation annotation, edu.ksu.cis.bandera.jjjc.symboltable.Name name, edu.ksu.cis.bandera.jjjc.node.PExp exp, Vector exceptions, String label) throws DuplicateAssertionException {
	super(annotation, name, exp, exceptions);
	this.label = label;
}
/**
 * 
 * @return int
 * @param o java.lang.Object
 */
public int compareTo(Object o) {
	if (o instanceof PreAssertion) {
		return 1;
	} else if (o instanceof LocationAssertion) {
		return name.compareTo(((LocationAssertion) o).getName());
	} else {
		return -1;
	}
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getLabel() {
	return label;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	String text = "LOCATION [" + label + "] " + name;
	if (exp == null) return text;
	else return text + ": " + exp.toString().trim();
}
}
