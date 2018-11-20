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
import edu.ksu.cis.bandera.jjjc.exception.*;

public class Variable implements Named, Typed {
	private int modifiers = 0;
	private Type type;
	private Name name;
/**
 * 
 * @param modifiers int
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Variable(int modifiers, Type type, Name name) throws InvalidModifiersException {
	this(type, name);
	if (areValidModifiers(modifiers))
		this.modifiers = modifiers;
	else
		throw new InvalidModifiersException("Invalid modifiers for variable named '" + name + "'");
}
/**
 * 
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Variable(Type type, Name name) {
	this.type = type;
	this.name = name;
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public static boolean areValidModifiers(int modifiers) {
	if (Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)|| Modifier.isStatic(modifiers)
			|| Modifier.isNative(modifiers) || Modifier.isSynchronized(modifiers) || Modifier.isTransient(modifiers)
			|| Modifier.isVolatile(modifiers) || Modifier.isPublic(modifiers) || Modifier.isAbstract(modifiers))
		return false;
	else return true;
}
/**
 * 
 * @return boolean
 * @param otherVariable edu.ksu.cis.bandera.jjjc.symboltable.Variable
 */
public boolean equals(Variable otherVariable) {
	if (otherVariable == null) return false;
	else return name.equals(otherVariable.name) && type.equals(otherVariable.type);
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
 * @return java.lang.String
 */
public String toString() {
	String result = Modifier.toString(modifiers) + " " + type.toString() + " " + name.toString();
	return result.trim();
}
}
