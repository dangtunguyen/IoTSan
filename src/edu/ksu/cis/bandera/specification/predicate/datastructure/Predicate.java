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
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.specification.predicate.node.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.exception.*;
import ca.mcgill.sable.soot.jimple.*;

public abstract class Predicate {
	protected static Jimple jimple = Jimple.v();
	protected Name name;
	protected Node node;
	protected Annotation annotation;
	protected Vector exceptions;
	protected PExp constraint;
	protected String description;
	protected boolean isStatic;
	protected ClassOrInterfaceType type;
	protected Hashtable variablesUsed;
	protected Vector params;
	protected Vector paramTypes;
	protected Hashtable locations;
	protected Vector methods;
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 * @param edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType type
 * @param node edu.ksu.cis.bandera.predicate.node.Node
 * @param exceptions java.util.Vector
 */
protected Predicate(Name name, ClassOrInterfaceType type, Annotation annotation, Node node, Vector exceptions) throws DuplicatePredicateException {
	this.name = name;
	this.annotation = annotation;
	this.type = type;
	this.node = node;
	this.exceptions = exceptions;

	Name psName = name.getSuperName();
	try {
		PredicateSet ps = PredicateSet.getPredicateSet(psName);
		ps.putPredicate(this);
	} catch (Exception e) {
		PredicateSet ps = new PredicateSet(psName);
		ps.putPredicate(this);
		PredicateSet.putPredicateSet(ps);
	}
}
/**
 * 
 * @param qv edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable
 */
public abstract void applyThis(QuantifiedVariable qv);
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 */
public edu.ksu.cis.bandera.annotation.Annotation getAnnotation() {
	return annotation;
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 */
public edu.ksu.cis.bandera.specification.predicate.node.PExp getConstraint() {
	return constraint;
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
 * @return java.util.Hashtable
 */
public java.util.Hashtable getLocations() {
	return locations;
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getMethods() {
	return methods;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Name getName() {
	return name;
}
	public Node getNode() { return node; /* robbyjo's patch */ }
/**
 * 
 * @return int
 */
public int getNumOfParams() {
	return params.size();
}
/**
 * 
 * @return java.lang.String
 * @param index int
 */
public String getParam(int index) {
	return (String) params.elementAt(index);
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getParams() {
	return params;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param index int
 */
public Type getParamType(int index) {
	return (Type) paramTypes.elementAt(index);
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getParamTypes() {
	return paramTypes;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType getType() {
	return type;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getVariablesUsed() {
	return variablesUsed;
}
/**
 * 
 * @return boolean
 */
public boolean isStatic() {
	return isStatic;
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
 * @param newConstraint edu.ksu.cis.bandera.specification.predicate.node.PExp
 */
public void setConstraint(edu.ksu.cis.bandera.specification.predicate.node.PExp newConstraint) {
	constraint = newConstraint;
}
/**
 * 
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription.toString();
	if ("".equals(description)) {
		description = null;
	}
}
/**
 * 
 * @param newParams java.util.Vector
 */
public void setParams(java.util.Vector newParams) {
	params = newParams;
}
/**
 * 
 * @param newParamTypes java.util.Vector
 */
public void setParamTypes(java.util.Vector newParamTypes) {
	paramTypes = newParamTypes;
}
/**
 * 
 * @param newIsStatic boolean
 */
public void setStatic(boolean isStatic) {
	this.isStatic = isStatic;
}
/**
 * 
 * @param newType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void setType(edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType newType) {
	type = newType;
}
/**
 * 
 * @param newVariablesUsed java.util.Hashtable
 */
public void setVariablesUsed(java.util.Hashtable newVariablesUsed) {
	variablesUsed = newVariablesUsed;
}
}
