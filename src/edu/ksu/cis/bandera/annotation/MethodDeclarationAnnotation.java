package edu.ksu.cis.bandera.annotation;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import java.util.*;
import ca.mcgill.sable.util.*;
public class MethodDeclarationAnnotation extends BlockStmtAnnotation implements java.lang.Comparable {
	private SootMethod sm;
	private Method m;
	private Vector annotations = null;
	private Hashtable parameterLocals;
	private Local retLocal;
	
public Local getRetLocal()
{
	return retLocal;
}

public void setRetLocal(Local l)
{
	retLocal = l;
}
	
/**
 * MethodDeclarationAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public MethodDeclarationAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
public void apply(Switch sw)
{
	((AnnotationSwitch) sw).caseMethodDeclarationAnnotation(this);
}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	MethodDeclarationAnnotation result = new MethodDeclarationAnnotation((edu.ksu.cis.bandera.jjjc.node.Node) node.clone());

	for (Enumeration e = containedAnnotations.elements(); e.hasMoreElements();) {
		result.addAnnotation((Annotation) ((Annotation) e.nextElement()).clone());
	}

	result.sm = sm;
	result.m = m;
	
	return result;
}
/**
 * 
 * @return int
 * @param o java.lang.Object
 */
public int compareTo(Object o) {
	if (o instanceof FieldDeclarationAnnotation) {
		return 1;
	} else if (o instanceof ConstructorDeclarationAnnotation) {
		return 1;
	} else if (o instanceof MethodDeclarationAnnotation) {
		MethodDeclarationAnnotation mda = (MethodDeclarationAnnotation) o;

		int i = getMethod().getName().compareTo(mda.getMethod().getName());
		
		if (i != 0) return i;
		
		i = getSootMethod().getParameterCount() - mda.getSootMethod().getParameterCount();

		if (i != 0) return i;
		
		return getSootMethod().toString().compareTo(getSootMethod().toString());
	} else {
		return -1;
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.annotation.Annotation
 * @param index int
 */
public Annotation getAnnotationAt(int index) {
	if (annotations == null) {
		annotations = getAllAnnotations(false);
	}
	return (Annotation) annotations.elementAt(index - 1);
}
/**
 * 
 * @return java.util.Hashtable
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public Hashtable getDeclaredLocalVariablesUntil(Annotation annotation) {
	Hashtable result = (Hashtable) getParameterLocals().clone();
	Hashtable table = super.getDeclaredLocalVariablesUntil(annotation);
	for (Enumeration e = table.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		result.put(key, table.get(key));
	}
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 */
public Method getMethod() {
	return m;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getParameterLocals() {
	return parameterLocals;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootMethod
 */
public SootMethod getSootMethod() {
	return sm;
}
/**
 * 
 * @return int
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public int indexOf(Annotation annotation) {
	if (annotations == null) {
		annotations = getAllAnnotations(false);
	}
	return annotations.indexOf(annotation) + 1;
}
/**
 * 
 * @param method edu.ksu.cis.bandera.jjjc.symboltable.Method
 */
public void setMethod(Method method) {
	m = method;
}
/**
 * 
 * @param newParameterLocals java.util.Hashtable
 */
public void setParameterLocals(java.util.Hashtable newParameterLocals) {
	parameterLocals = newParameterLocals;
}
/**
 * 
 * @param sootMethod ca.mcgill.sable.soot.SootMethod
 */
public void setSootMethod(SootMethod sootMethod) {
	sm = sootMethod;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	String result = "";
	try {
		boolean isAbstractNative = Modifier.isAbstract(sm.getModifiers()) || Modifier.isNative(sm.getModifiers());
		result = Modifier.toString(sm.getModifiers()) + " " + sm.getReturnType().toString().trim()
				+ " " + sm.getName().trim() + "(";
		JimpleBody body = (JimpleBody) sm.getBody(Jimple.v());
		String parm = "";
		for (Enumeration e = m.getParameters().elements(); e.hasMoreElements();) {
			Variable v = (Variable) e.nextElement();
			if (body.declaresLocal(v.getName().toString().trim()) || isAbstractNative) {
				parm += (v.toString() + ", ");
			}
		}
		if (parm.length() > 1) 
			parm = parm.substring(0, parm.length() - 2);
		result += (parm + ")");
		String exceptions = sm.getExceptions().toString().trim();
		exceptions = exceptions.substring(1, exceptions.length() - 1);
		if (sm.getExceptions().size() > 0) {
			result += (" throws " + exceptions);
		}
	} catch (Exception e) {}
	return result;
}
}
