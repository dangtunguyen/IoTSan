package edu.ksu.cis.bandera.specification.assertion.ast;

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
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.specification.assertion.analysis.*;
import edu.ksu.cis.bandera.specification.assertion.exception.*;
import edu.ksu.cis.bandera.specification.assertion.node.*;
import java.lang.reflect.*;
import java.util.*;
public class AssertionBuilder extends DepthFirstAdapter {
	private Vector errors = new Vector();
	private Node node;
	private SymbolTable symbolTable;
	private Annotation annotation;
	private ClassOrInterfaceType type;
	private boolean isStatic;
	private Vector labeledStmtAnnotations;
	private String name;
	private StringBuffer buffer;
	private boolean hasRet;
	public boolean isVoid;
	private int modifiers;
/**
 * 
 * @param node edu.ksu.cis.bandera.assertion.node.Node
 * @param symbolTable edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public AssertionBuilder(Node node, SymbolTable symbolTable, ClassOrInterfaceType type, Annotation annotation) {
	this.node = node;
	this.symbolTable = symbolTable;
	this.annotation = annotation;
	this.type = type;
	Vector v = annotation.getAllAnnotations(false);
	labeledStmtAnnotations = new Vector();
	for (Enumeration e = v.elements(); e.hasMoreElements();) {
		Object o = e.nextElement();
		if (o instanceof LabeledStmtAnnotation) labeledStmtAnnotations.add(o);
	}
	if (annotation instanceof MethodDeclarationAnnotation) {
		isStatic = java.lang.reflect.Modifier.isStatic(((MethodDeclarationAnnotation) annotation).getMethod().getModifiers());
	} else {
		isStatic = false;
	}
}
/**
 * 
 */
public void build() {
	node.apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.assertion.node.AEndOfLineComment
 */
public void caseAEndOfLineComment(AEndOfLineComment node) {
	buffer.append(node.toString().trim().substring(2));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.assertion.node.ALocationAssertion
 */
public void caseALocationAssertion(ALocationAssertion node) {
	buffer = new StringBuffer();
	Object temp[] = node.getComment().toArray();
	for (int i = 0; i < temp.length; i++) {
		((PComment) temp[i]).apply(this);
	}
	Vector v = new Vector();

	if (Modifier.isAbstract(modifiers) || Modifier.isNative(modifiers)) {
		v.add(new BadAssertionDefinitionException("Cannot have location assertion for an abstract/native method: " + node));
	}
	
	String label = node.getLabel().toString().trim();
	boolean found = false;
	Annotation currentAnnotation = annotation;
	for (Enumeration e = labeledStmtAnnotations.elements(); e.hasMoreElements();) {
		LabeledStmtAnnotation lsa = (LabeledStmtAnnotation) e.nextElement();
		if (lsa.getId().equals(label)) {
			currentAnnotation = lsa;
			found = true;
			break;
		}
	}
	if (!found) {
		v.add(new BadAssertionDefinitionException("Invalid label '" + label + "': " + node));
	}
	try {
		Assertion assertion = new LocationAssertion(currentAnnotation, new Name(name + "." + node.getId()), node.getExp(), v, label);
		assertion.setDescription(buffer.toString());
		v.addAll(new TypeChecker(symbolTable, type, currentAnnotation, isStatic).check(node.getExp()));
	} catch (Exception e) {
		errors.addAll(v);
		errors.add(e.getMessage());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.assertion.node.APostAssertion
 */
public void caseAPostAssertion(APostAssertion node) {
	buffer = new StringBuffer();
	Object temp[] = node.getComment().toArray();
	for (int i = 0; i < temp.length; i++) {
		((PComment) temp[i]).apply(this);
	}
	hasRet = false;
	node.getExp().apply(new edu.ksu.cis.bandera.jjjc.analysis.DepthFirstAdapter() {
		public void caseANameExp(edu.ksu.cis.bandera.jjjc.node.ANameExp node) {
			String s = node.toString().trim();
			if ("$ret".equals(s)) {
				hasRet = true;
			}
		}
	});
	Vector v = new Vector();
	if (isVoid && hasRet) {
		v.add(new BadAssertionDefinitionException("Cannot have $ret in void method or constructor: " + node));
	}
	try {
		Assertion assertion = new PostAssertion(annotation, new Name(name + "." + node.getId()), node.getExp(), v, hasRet);
		assertion.setDescription(buffer.toString());
		v.addAll(new TypeChecker(symbolTable, type, annotation, isStatic).check(node.getExp()));
	} catch (Exception e) {
		errors.addAll(v);
		errors.add(e.getMessage());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.assertion.node.APreAssertion
 */
public void caseAPreAssertion(APreAssertion node) {
	buffer = new StringBuffer();
	Object temp[] = node.getComment().toArray();
	for (int i = 0; i < temp.length; i++) {
		((PComment) temp[i]).apply(this);
	}
	Vector v = new Vector();
	try {
		Assertion assertion = new PreAssertion(annotation, new Name(name + "." + node.getId()), node.getExp(), v);
		assertion.setDescription(buffer.toString());
		v.addAll(new TypeChecker(symbolTable, type, annotation, isStatic).check(node.getExp()));
	} catch (Exception e) {
		errors.addAll(v);
		errors.add(e.getMessage());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.assertion.node.AUnit
 */
public void caseAUnit(AUnit node) {
	if(node.getName() != null) {
		name = new Name(node.getName().toString()).toString().trim();
	} else {
		if (annotation instanceof MethodDeclarationAnnotation) {
			MethodDeclarationAnnotation mda = (MethodDeclarationAnnotation) annotation;
			isVoid = "void".equals(mda.getMethod().getReturnType().toString().trim());
			modifiers = mda.getMethod().getModifiers();
			try {
				name = mda.getMethod().getDeclaringClassOrInterface().getFullyQualifiedName()
						+ "." + mda.getMethod().getName().toString();
			} catch (Exception e) {
				throw new RuntimeException("Fatal Error");
			}
		} else {
			isVoid = true;
			ConstructorDeclarationAnnotation cda = (ConstructorDeclarationAnnotation) annotation;
			modifiers = cda.getConstructor().getModifiers();
			try {
				name = cda.getConstructor().getDeclaringClassOrInterface().getFullyQualifiedName()
						+ "." + cda.getConstructor().getDeclaringClassOrInterface().getName().getLastIdentifier();
			} catch (Exception e) {
				throw new RuntimeException("Fatal Error");
			}
		}
	}
	super.caseAUnit(node);
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getErrors() {
	return errors;
}
}
