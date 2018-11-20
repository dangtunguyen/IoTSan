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
import edu.ksu.cis.bandera.specification.assertion.exception.*;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
public abstract class Assertion {
	protected Annotation annotation;
	protected Name name;
	protected PExp exp;
	protected String description;
	protected Vector exceptions;
/**
 *
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param exp edu.ksu.cis.bandera.jjjc.node.PExp
 * @param exceptions java.lang.Vector
 */
public Assertion(Annotation annotation, Name name, PExp exp, Vector exceptions) throws DuplicateAssertionException {
	this.annotation = annotation;
	this.name = name;
	this.exp = exp;
	this.exceptions = exceptions;
	
	Name asName = name.getSuperName();
	try {
		AssertionSet as = AssertionSet.getAssertionSet(asName);
		as.putAssertion(this);
	} catch (Exception e) {
		AssertionSet as = new AssertionSet(asName);
		as.putAssertion(this);
		AssertionSet.putAssertionSet(as);
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 */
public edu.ksu.cis.bandera.annotation.Annotation getAnnotation() {
	return annotation;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.node.PExp
 */
public edu.ksu.cis.bandera.jjjc.node.PExp getConstraint() {
	return exp;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getExceptions() {
	return exceptions;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public edu.ksu.cis.bandera.jjjc.symboltable.Name getName() {
	return name;
}
/**
 * 
 * @return boolean
 */
public boolean isValid() {
	return exceptions.size() == 0;
}
/**
 * 
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * 
 * @param newExceptions java.util.Vector
 */
public void setExceptions(java.util.Vector newExceptions) {
	exceptions = newExceptions;
}
}
