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
import edu.ksu.cis.bandera.jjjc.util.Util;
import edu.ksu.cis.bandera.jjjc.exception.*;

public class Name implements Comparable {
	public static String separator = ".";
	private String[] identifiers;
/**
 * 
 * @param identifiers java.lang.String[]
 */
protected Name(String[] identifiers) {
	this(identifiers, 0, identifiers.length - 1);
}
/**
 * 
 * @param identifiers java.lang.String[]
 * @param start int
 * @param end int
 */
protected Name(String[] identifiers, int from, int to) {
	if ((identifiers == null) || (from > to)) {
		this.identifiers = new String[1];
		this.identifiers[0] = "";
	} else {
		if (from < 0) from = 0;
		if (to >= identifiers.length) to = identifiers.length - 1;

		this.identifiers = new String[to - from + 1];

		for (int i = 0; i < this.identifiers.length; i++) {
			this.identifiers[i] = identifiers[i + from];
		}
	}
}
/**
 * 
 * @param name1 edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param name2 edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Name(Name name1, Name name2) {
	int length1 = (name1 != null) ? name1.identifiers.length : 0;
	int length2 = (name2 != null) ? name2.identifiers.length : 0;

	identifiers = new String[length1 + length2];

	for (int i = 0; i < length1; i++) {
		this.identifiers[i] = name1.identifiers[i];
	}
	
	for (int i = 0; i < length2; i++) {
		this.identifiers[i + length1] = name2.identifiers[i];
	}
}
/**
 * 
 * @param name1 edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param s java.lang.String
 */
public Name(Name name1, String name2) {
	this(name1, new Name(name2));
}
/**
 * 
 * @param s java.lang.String
 */
public Name(String name) {
	identifiers = Util.splitString(name.trim(), separator + "\t []");
}
/**
 * 
 * @return int
 * @param object java.lang.Object
 */
public int compareTo(Object object) {
	return toString().compareTo(object.toString());
}
/**
 * 
 * @return boolean
 * @param otherName edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public boolean equals(Object obj) {
	if (obj == null) return false;
	else if (!(obj instanceof Name)) return false;
	else return this.toString().equals(obj.toString());
}
/**
 * 
 * @return java.lang.String
 */
public String getFirstIdentifier() {
	return identifiers[0];
}
/**
 * 
 * @return java.lang.String
 */
public String getLastIdentifier() {
	if (identifiers.length == 0)
		return "";
	else
		return identifiers[identifiers.length - 1];
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Name getSubName() {
	return new Name(identifiers, 1, identifiers.length - 1);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Name getSuperName() {
	return new Name(identifiers, 0, identifiers.length - 2);
}
/**
 * 
 * @return int
 */
public int hashCode() {
	return toString().hashCode();
}
/**
 * 
 * @return boolean
 */
public boolean isSimpleName() {
	return (identifiers.length == 1);
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return Util.combineStrings(identifiers, separator);
}
}
