package edu.ksu.cis.bandera.jjjc.symboltable;

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
import java.lang.reflect.Modifier;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.exception.*;

public class Method implements Named, Typed {
	private int modifiers = 0;
	private Type returnType = null;
	private Name name;
	private Vector parameters = new Vector();
	private Vector parameterTypes = new Vector();
	private Vector exceptions = new Vector();
	private ClassOrInterfaceType declaringClassOrInterface = null;
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Method(Name name) throws InvalidNameException {
	if (!name.isSimpleName())
		throw new InvalidNameException("Cannot have a method named '" + name.toString() + "'");
	this.name = name;
}
/**
 * 
 * @param exceptionType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void addException(ClassOrInterfaceType exceptionType) throws AlreadyDeclaredException,
		ClassOrInterfaceTypeNotFoundException {
	for (int i = 0; i < exceptions.size(); i++) {
		if (exceptionType.getName().equals(((ClassOrInterfaceType) exceptions.elementAt(i)).getName()))
			throw new AlreadyDeclaredException("Method '" + name.toString() + "' in class named '"
					+ declaringClassOrInterface + "' already has exception type '" + exceptionType.toString() + "'");
	}

	// check later
	/*
	if (exceptionType.isInterface()	|| !exceptionType.hasSuperClass(new Name("java.lang.Throwable")))
		throw new NotThrowableException("Method '" + name.toString() + "' in class named '"
					+ declaringClassOrInterface + "' cannot have un-throwable exception type '" + exceptionType.toString() + "'");
	*/
	exceptions.addElement(exceptionType);
}
/**
 * 
 * @param variable edu.ksu.cis.bandera.jjjc.symboltable.Variable
 */
public void addParameterVariable(Variable variable) throws AlreadyDeclaredException {
	Name varName = variable.getName();
	
	for (int i = 0; i < parameters.size(); i++) {
		if (varName.equals(((Variable) parameters.elementAt(i)).getName()))
			throw new AlreadyDeclaredException("Method '" + name.toString() + "' already has a parameter named '"
					+ variable.getName().toString() + "'"); 
	}

	parameters.addElement(variable);
	parameterTypes.addElement(variable.getType());
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public static boolean areValidClassMethodModifiers(int modifiers) {
	if (Modifier.isTransient(modifiers) || Modifier.isVolatile(modifiers))
		return false;
	else if (Modifier.isPublic(modifiers) && Modifier.isPrivate(modifiers))
		return false;
	else if (Modifier.isProtected(modifiers) && Modifier.isPrivate(modifiers))
		return false;
	else if (Modifier.isPublic(modifiers) && Modifier.isProtected(modifiers))
		return false;
	else if (Modifier.isAbstract(modifiers) && (Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers)
			|| Modifier.isNative(modifiers) || Modifier.isSynchronized(modifiers)))
		return false;
	else return true;
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public static boolean areValidInterfaceMethodModifiers(int modifiers) {
	if (Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)|| Modifier.isStatic(modifiers)
			|| Modifier.isNative(modifiers) || Modifier.isSynchronized(modifiers) || Modifier.isTransient(modifiers)
			|| Modifier.isVolatile(modifiers) || Modifier.isFinal(modifiers))
		return false;
	else return true;
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public boolean areValidModifiers(int modifiers) {
	return areValidModifiers(modifiers, declaringClassOrInterface.isInterface());
}
/**
 * 
 * @return boolean
 * @param modifiers int
 * @param isInterface boolean
 */
public static boolean areValidModifiers(int modifiers, boolean isInterface) {
	return (isInterface) ? areValidInterfaceMethodModifiers(modifiers) : areValidClassMethodModifiers(modifiers);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public ClassOrInterfaceType getDeclaringClassOrInterface() throws NotDeclaredException {
	if (declaringClassOrInterface == null)
		throw new NotDeclaredException("Method named '" + name.toString() + "' has not been declared yet.");
	else return declaringClassOrInterface;
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getExceptions() {
	return exceptions.elements();
}
/**
 * 
 * @return int
 */
public int getModifiers() {
	return modifiers;
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
 * @return java.util.Vector
 */
public Vector getParameters() {
	return parameters;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getParameterTypes() {
	return parameterTypes;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public Type getReturnType() {
	return returnType;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public Type getType() {
	return returnType;
}
/**
 * 
 * @return boolean
 * @param otherMethod edu.ksu.cis.bandera.jjjc.symboltable.Method
 */
public boolean hasSameSignature(Method otherMethod) {
	if (otherMethod == null)
		return false;

	return hasSignature(otherMethod.name, otherMethod.parameterTypes);
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param parameterTypes java.util.Vector
 */
public boolean hasSignature(Name name, Vector parameterTypes) {
	if ((name == null) || (parameterTypes == null))
		return false;
	else if (!(name.equals(name)))
		return false;
	else if (this.parameterTypes.size() != parameterTypes.size())
		return false;

	for (int i = 0; i < this.parameterTypes.size(); i++) {
		if (!(this.parameterTypes.elementAt(i).equals(parameterTypes.elementAt(i))))
			return false;
	}
		
	return true;
}
/**
 * 
 * @return boolean
 * @param otherType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public boolean isAccessible(ClassOrInterfaceType otherType) {
	if (declaringClassOrInterface == otherType)
		return true;
	else if (!declaringClassOrInterface.isAccesible(otherType))
		return false;
		
	if (Modifier.isPublic(modifiers))
		return true;
	else if (Modifier.isPrivate(modifiers))
		return false;
	else if (Modifier.isProtected(modifiers) && otherType.hasSuperClass(declaringClassOrInterface))
		return true;
	else return declaringClassOrInterface.getContainingPackage() == otherType.getContainingPackage();
}
/**
 * 
 * @return boolean
 * @param argumentTypes java.util.Vector
 */
public boolean isApplicable(Vector argumentTypes) {
	if (parameterTypes.size() != argumentTypes.size())
		return false;

	for (int i = 0; i < parameterTypes.size(); i++) {
		if (!((Type) argumentTypes.elementAt(i)).isValidMethodInvocationConversion((Type) parameterTypes.elementAt(i)))
			return false;
	}
	
	return true;
}
/**
 * 
 * @return boolean
 * @param otherMethod edu.ksu.cis.bandera.jjjc.symboltable.Method
 */
public boolean isMoreSpecific(Method otherMethod) {
	return declaringClassOrInterface.isValidMethodInvocationConversion(otherMethod.declaringClassOrInterface) &&
			otherMethod.isApplicable(parameterTypes);
}
/**
 * 
 * @param classOrInterface edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void setDeclaringClassOrInterface(ClassOrInterfaceType classOrInterface) {
	declaringClassOrInterface = classOrInterface;
}
/**
 * 
 * @param modifiers int
 */
public void setModifiers(int modifiers) throws InvalidModifiersException {
	if (areValidModifiers(modifiers))
		this.modifiers = modifiers;
	else
		throw new InvalidModifiersException("Invalid modifiers for method named '" + name
				+ "' in class or interface type named '" + declaringClassOrInterface.getName() + "'");
}
/**
 * 
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public void setReturnType(Type type) {
	returnType = type;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	String parm = parameterTypes.toString();
	return name.toString() + "(" + parm.substring(1, parm.length() - 1) + ")";
}
}
