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

public class ArrayType extends ReferenceType {
	public final Type baseType;
	public int nDimensions;
	public Field length;
/**
 * 
 * @param baseType edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param nDimensions int
 */
public ArrayType(Type baseType, int nDimensions) {
	this.baseType = baseType;
	this.nDimensions = nDimensions;
	name = baseType.getName();
	try {
		length = new Field(new Name("length"), IntType.TYPE);
		length.modifiers = Modifier.PUBLIC | Modifier.FINAL;
		length.isArrayLength = true;
		length.arrayType = this;
	} catch (InvalidNameException e) {};
}
/**
 * 
 * @return java.lang.String
 */
public String getFullyQualifiedName() {
	StringBuffer result = new StringBuffer(name.toString());
	for (int i = 0; i < nDimensions; i++) {
		result.append("[]");
	}
	
	return result.toString();
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
 * @return boolean
 * @param otherType edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public boolean isValidNarrowingConversion(Type otherType){
	if ((otherType instanceof ArrayType) && (baseType instanceof ReferenceType)) {
		ArrayType otherArrayType = (ArrayType) otherType;
		return (otherArrayType.baseType instanceof ReferenceType) && (this.nDimensions == otherArrayType.nDimensions)
				&& (baseType.isValidNarrowingConversion(otherArrayType.baseType));
	} else return false;
}
/**
 * 
 * @return boolean
 * @param otherType edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public boolean isValidWideningConversion(Type otherType) {
	if ("java.lang.Object".equals(otherType.getFullyQualifiedName()) ||
			"java.lang.Cloneable".equals(otherType.getFullyQualifiedName()))
		return true;
	else if ((otherType instanceof ArrayType) && (baseType instanceof ReferenceType)) {
		ArrayType otherArrayType = (ArrayType) otherType;
		return (otherArrayType.baseType instanceof ReferenceType) && (this.nDimensions == otherArrayType.nDimensions)
				&& (baseType.isValidWideningConversion(otherArrayType.baseType));
	} else return false;
}
}
