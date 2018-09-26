package edu.ksu.cis.bandera.jjjc.util;

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
import java.io.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.exception.*;

public final class Util {
/**
 * 
 * @return java.lang.String
 * @param strs java.lang.String[]
 * @param s java.lang.String
 */
public static String combineStrings(String[] strs, String s) {
	StringBuffer result = new StringBuffer();
	int length = strs.length;

	for (int i = 0; i < (length - 1); i++) {
		result.append(strs[i]);
		result.append(s);	
	}
	
	if (length > 0)
		result.append(strs[length - 1]);
	
	return result.toString();
}
/**
 * 
 * @return int
 * @param modifiers int
 */
public static int convertModifiers(int modifiers) {
	int result = 0;
	
	if (java.lang.reflect.Modifier.isAbstract(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.ABSTRACT;
	if (java.lang.reflect.Modifier.isFinal(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.FINAL;
	if (java.lang.reflect.Modifier.isNative(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.NATIVE;
	if (java.lang.reflect.Modifier.isPrivate(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.PRIVATE;
	if (java.lang.reflect.Modifier.isProtected(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.PROTECTED;
	if (java.lang.reflect.Modifier.isPublic(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.PUBLIC;
	if (java.lang.reflect.Modifier.isStatic(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.STATIC;
	if (java.lang.reflect.Modifier.isSynchronized(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.SYNCHRONIZED;
	if (java.lang.reflect.Modifier.isTransient(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.TRANSIENT;
	if (java.lang.reflect.Modifier.isVolatile(modifiers))
		result |= ca.mcgill.sable.soot.Modifier.VOLATILE;
		
	return result;
}
/**
 * 
 * @return int
 * @param modifier java.lang.String
 */
public static int convertModifiers(String modifiers) throws InvalidModifiersException {
	String[] modifier = splitString(modifiers, " \t\r\n{},");
	int result = 0;
	for (int i = 0; i < modifier.length; i++) {
		if ("abstract".equals(modifier[i]) && !java.lang.reflect.Modifier.isAbstract(result))
			result |= java.lang.reflect.Modifier.ABSTRACT;
		else if ("final".equals(modifier[i]) && !java.lang.reflect.Modifier.isFinal(result))
			result |= java.lang.reflect.Modifier.FINAL;
		//else if ("interface".equals(modifier[i]) && !Modifier.isInterface(result))
			//result |= Modifier.INTERFACE;
		else if ("native".equals(modifier[i]) && !java.lang.reflect.Modifier.isNative(result))
			result |= java.lang.reflect.Modifier.NATIVE;
		else if ("private".equals(modifier[i]) && !java.lang.reflect.Modifier.isPrivate(result))
			result |= java.lang.reflect.Modifier.PRIVATE;
		else if ("protected".equals(modifier[i]) && !java.lang.reflect.Modifier.isProtected(result))
			result |= java.lang.reflect.Modifier.PROTECTED;
		else if ("public".equals(modifier[i]) && !java.lang.reflect.Modifier.isPublic(result))
			result |= java.lang.reflect.Modifier.PUBLIC;
		else if ("static".equals(modifier[i]) && !java.lang.reflect.Modifier.isStatic(result))
			result |= java.lang.reflect.Modifier.STATIC;
		else if ("synchronized".equals(modifier[i]) && !java.lang.reflect.Modifier.isSynchronized(result))
			result |= java.lang.reflect.Modifier.SYNCHRONIZED;
		else if ("transient".equals(modifier[i]) && !java.lang.reflect.Modifier.isTransient(result))
			result |= java.lang.reflect.Modifier.TRANSIENT;
		else if ("volatile".equals(modifier[i]) && !java.lang.reflect.Modifier.isVolatile(result))
			result |= java.lang.reflect.Modifier.VOLATILE;
		else
			throw new InvalidModifiersException("Invalid modifiers: " + modifiers);
	}
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param type ca.mcgill.sable.soot.Type
 * @param symbolTable SymbolTable
 */
public static edu.ksu.cis.bandera.jjjc.symboltable.Type convertType(ca.mcgill.sable.soot.Type type,
		edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable symbolTable) throws CompilerException{
	if (type instanceof ca.mcgill.sable.soot.VoidType)
		return edu.ksu.cis.bandera.jjjc.symboltable.VoidType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.BooleanType)
		return edu.ksu.cis.bandera.jjjc.symboltable.BooleanType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.ByteType)
		return edu.ksu.cis.bandera.jjjc.symboltable.ByteType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.CharType)
		return edu.ksu.cis.bandera.jjjc.symboltable.CharType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.IntType)
		return edu.ksu.cis.bandera.jjjc.symboltable.IntType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.ShortType)
		return edu.ksu.cis.bandera.jjjc.symboltable.ShortType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.LongType)
		return edu.ksu.cis.bandera.jjjc.symboltable.LongType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.FloatType)
		return edu.ksu.cis.bandera.jjjc.symboltable.FloatType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.DoubleType)
		return edu.ksu.cis.bandera.jjjc.symboltable.DoubleType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.RefType) {
		edu.ksu.cis.bandera.jjjc.symboltable.Name name =
				new edu.ksu.cis.bandera.jjjc.symboltable.Name(((ca.mcgill.sable.soot.RefType) type).className);
		return symbolTable.resolveClassOrInterfaceType(name);
	} else if (type instanceof ca.mcgill.sable.soot.NullType)
		return edu.ksu.cis.bandera.jjjc.symboltable.NullType.TYPE;
	else if (type instanceof ca.mcgill.sable.soot.VoidType)
		return edu.ksu.cis.bandera.jjjc.symboltable.VoidType.TYPE;
	
	ca.mcgill.sable.soot.ArrayType arrayType = (ca.mcgill.sable.soot.ArrayType) type;
	return new edu.ksu.cis.bandera.jjjc.symboltable.ArrayType(convertType(arrayType.baseType, symbolTable),
			arrayType.numDimensions);
}
/**
 * 
 * @return ca.mcgill.sable.soot.Type
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public static ca.mcgill.sable.soot.Type convertType(edu.ksu.cis.bandera.jjjc.symboltable.Type type) {
	if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.VoidType)
		return ca.mcgill.sable.soot.VoidType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.BooleanType)
		return ca.mcgill.sable.soot.BooleanType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.ByteType)
		return ca.mcgill.sable.soot.ByteType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.CharType)
		return ca.mcgill.sable.soot.CharType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.IntType)
		return ca.mcgill.sable.soot.IntType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.ShortType)
		return ca.mcgill.sable.soot.ShortType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.LongType)
		return ca.mcgill.sable.soot.LongType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.FloatType)
		return ca.mcgill.sable.soot.FloatType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.DoubleType)
		return ca.mcgill.sable.soot.DoubleType.v();
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType)
		return ca.mcgill.sable.soot.RefType.v(((edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType)
				type).getName().toString());
	else if (type instanceof edu.ksu.cis.bandera.jjjc.symboltable.NullType)
		return ca.mcgill.sable.soot.NullType.v();

	edu.ksu.cis.bandera.jjjc.symboltable.ArrayType arrayType = (edu.ksu.cis.bandera.jjjc.symboltable.ArrayType) type;
	return ca.mcgill.sable.soot.ArrayType.v((ca.mcgill.sable.soot.BaseType) convertType(arrayType.baseType),
			arrayType.nDimensions);
}
/**
 * 
 * @return int
 * @param s java.lang.String
 */
public static int countArrayDimensions(String s) {
	int result = 0;
	
	for (int i = 0; i < s.length(); i++) 
		if (s.charAt(i) == '[')
			result++;
			
	return result;
}
/**
 * 
 * @return java.lang.String
 * @param s java.lang.String
 */
public static String decodeString(String s) {
	StreamTokenizer st = new StreamTokenizer(new StringReader(s));
	try {
		st.nextToken();
		return st.sval;
	} catch (Exception e) {
		return null;
	}
}
/**
 * 
 * @return java.lang.String
 * @param s java.lang.String
 */
public static String encodeString(String s) {
	StringBuffer buffer = new StringBuffer();
	for (int i = 0; i < s.length(); i++) {
		int c = s.charAt(i);
		int hibyte = c & 0xff00;
		if (hibyte != 0) {
			String temp = Integer.toString(c & 0xffff, 16);
			for (int j = 0; j < (4 - temp.length()); j++) {
				temp = "0" + temp;
			}
			buffer.append("\\u" + temp);
		} else {
			switch ((char) c) {
				case '\b':
					buffer.append("\\b");
					break;
				case '\t':
					buffer.append("\\t");
					break;
				case '\n':
					buffer.append("\\n");
					break;
				case '\f':
					buffer.append("\\f");
					break;
				case '\r':
					buffer.append("\\r");
					break;
				case '\"':
					buffer.append("\\\"");
					break;
				case '\'':
					buffer.append("\\\'");
					break;
				case '\\':
					buffer.append("\\\\");
					break;
				default:
					buffer.append((char) c);
			}
		}
	}
	return buffer.toString();
}
/**
 * 
 * @return java.lang.String[]
 * @param s java.lang.String
 * @param limiter java.lang.String
 */
public static String[] splitString(String s, String limiter) {
	StringTokenizer tokens = new StringTokenizer(s, limiter);
	int length = tokens.countTokens();
	String[] result = new String[length];

	for (int i = 0; i < length; i++) {
		result[i] = tokens.nextToken();
	}
	
	return result;
}
}
