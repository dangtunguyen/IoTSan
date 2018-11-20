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
import java.lang.reflect.*;
import edu.ksu.cis.bandera.jjjc.exception.*;

public class Field implements Named, Typed {
	protected int modifiers = 0;
	private Type type;
	private Name name;
	private ClassOrInterfaceType declaringClassOrInterface = null;
	protected boolean isArrayLength = false;
	protected ArrayType arrayType = null;
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public Field(Name name, Type type) throws InvalidNameException {
	if (!(name.isSimpleName()))
		throw new InvalidNameException("Cannot have a method named '" + name.toString() + "'");

	this.name = name;
	this.type = type;
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public static boolean areValidClassFieldModifiers(int modifiers) {
	if (Modifier.isPublic(modifiers) && Modifier.isProtected(modifiers))
		return false;
	else if (Modifier.isPublic(modifiers) && Modifier.isPrivate(modifiers))
		return false;
	else if (Modifier.isProtected(modifiers) && Modifier.isPrivate(modifiers))
		return false;
	else return true;
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public static boolean areValidInterfaceFieldModifiers(int modifiers) {
	if (Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)|| Modifier.isAbstract(modifiers)
			|| Modifier.isNative(modifiers) || Modifier.isSynchronized(modifiers) || Modifier.isTransient(modifiers)
			|| Modifier.isVolatile(modifiers))
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
	return (isInterface) ? areValidInterfaceFieldModifiers(modifiers) : areValidClassFieldModifiers(modifiers);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public ClassOrInterfaceType getDeclaringClassOrInterface() throws NotDeclaredException {
	if (declaringClassOrInterface == null)
		throw new NotDeclaredException("Field named '" + name.toString() + "' has not been declared yet.");
	else return declaringClassOrInterface;
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
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public Type getType() {
	return type;
}
/**
 * 
 * @return boolean
 * @param otherType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public boolean isAccessible(ClassOrInterfaceType otherType) {
	if (isArrayLength)
		return true;
		
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
 * @param declaringClassOrInterface edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void setDeclaringClassOrInterface(ClassOrInterfaceType declaringClassOrInterface) {
	this.declaringClassOrInterface = declaringClassOrInterface;
}
/**
 * 
 * @param modifiers int
 */
public void setModifiers(int modifiers) throws InvalidModifiersException {
	if (areValidModifiers(modifiers))
		this.modifiers = modifiers;
	else
		throw new InvalidModifiersException("Invalid modifiers for field named '" + name
				+ "' in class or interface type named '" + declaringClassOrInterface.getName() + "'");
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return name.toString();
}
}
