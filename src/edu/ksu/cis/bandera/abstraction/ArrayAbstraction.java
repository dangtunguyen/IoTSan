package edu.ksu.cis.bandera.abstraction;

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
import edu.ksu.cis.bandera.abstraction.util.*;
public class ArrayAbstraction extends Abstraction {
	private final Abstraction elementAbstraction;
	private final IntegralAbstraction indexAbstraction;
/**
 * 
 * @param elementAbstraction edu.ksu.cis.bandera.abstraction.Abstraction
 * @param indexAbstraction edu.ksu.cis.bandera.abstraction.IntegralAbstraction
 */
private ArrayAbstraction(Abstraction elementAbstraction, IntegralAbstraction indexAbstraction) {
	this.elementAbstraction = elementAbstraction;
	this.indexAbstraction = indexAbstraction;
}
/**
 * 
 * @return boolean
 * @param other java.lang.Object
 */
public boolean equals(Object other) {
	if (other instanceof ArrayAbstraction) 
		return toString().equals(other.toString());
	else
		return false;
}
/**
 * 
 * @return java.lang.String
 * @param packageName java.lang.String;
 */
public String generateAbstraction(String packageName) {
	String lineSeparator = System.getProperty("line.separator");
	boolean isConcreteIndex = indexAbstraction instanceof ConcreteIntegralAbstraction;
	String indexAbstractionClassName = indexAbstraction.getClass().getName();
	StringBuffer buffer = new StringBuffer();
	if (packageName == null) packageName = "";

	if (elementAbstraction instanceof IntegralAbstraction) {
		if ("".equals(packageName)) {
			buffer.append("package " + packageName + ";" + lineSeparator + lineSeparator);
		}
		String className = elementAbstraction.toString() + indexAbstraction + "ArrayAbstraction";
		buffer.append("public class " + className + " {" + lineSeparator);
		buffer.append("  private final int[] elements;" + lineSeparator);
		if (isConcreteIndex) {
			buffer.append("  public " + className + "(int length) {" + lineSeparator);
		} else {
			buffer.append("  public " + className + "() {" + lineSeparator);
			buffer.append("    int length = " + AbstractionClassLoader.invokeMethod(indexAbstractionClassName, "getNumOfTokens", new Object[0], null, new Vector()) + ";" + lineSeparator);
		}
		buffer.append("    elements = new int[length];" + lineSeparator);
		buffer.append("  }" + lineSeparator);
		buffer.append("  public int lookup(int index) {" + lineSeparator);
		buffer.append("    int result;" + lineSeparator);
		if (isConcreteIndex) {
			buffer.append("    result = elements[index];" + lineSeparator);
		} else {
			buffer.append("    if (" + indexAbstractionClassName + ".isOne2One(index)) {" + lineSeparator);
			buffer.append("      result = elements[index];" + lineSeparator);
			buffer.append("    } else {" + lineSeparator);
			buffer.append("      result = Abstraction.choose(elements[index]);" + lineSeparator);
			buffer.append("    }" + lineSeparator);
		}
		buffer.append("    return result;" + lineSeparator);
		buffer.append("  }" + lineSeparator);
		buffer.append("  public void update(int index, int value) {" + lineSeparator);
		if (isConcreteIndex) {
			buffer.append("    elements[index] = value;" + lineSeparator);
		} else {
			buffer.append("    if (" + indexAbstractionClassName + ".isOne2One(index)) {" + lineSeparator);
			buffer.append("      elements[index] = value;" + lineSeparator);
			buffer.append("    } else {" + lineSeparator);
			buffer.append("      elements[index] = elements[index] | value;" + lineSeparator);
			buffer.append("    }" + lineSeparator);
		}
		buffer.append("  }" + lineSeparator);
		buffer.append("}" + lineSeparator);
	} else {
	}
	return buffer.toString();
}
/**
 * 
 * @return edu.ksu.cis.bandera.abstraction.Abstraction
 */
public final Abstraction getElementAbstraction() {
	return elementAbstraction;
}
/**
 * 
 * @return edu.ksu.cis.bandera.abstraction.IntegralAbstraction
 */
public final IntegralAbstraction getIndexAbstraction() {
	return indexAbstraction;
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
 * @return java.lang.String
 */
public String toString() {
	return elementAbstraction + "[" + indexAbstraction + "]";
}
/**
 * 
 * @return edu.ksu.cis.bandera.abstraction.ArrayAbstraction
 * @param elementAbstraction edu.ksu.cis.bandera.abstraction.Abstraction
 * @param indexAbstraction edu.ksu.cis.bandera.abstraction.IntegralAbstraction
 */
public static ArrayAbstraction v(Abstraction elementAbstraction, IntegralAbstraction indexAbstraction) {
	return new ArrayAbstraction(elementAbstraction, indexAbstraction);
}
}
