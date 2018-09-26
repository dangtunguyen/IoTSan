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
public abstract class Abstraction {
	public static final int FALSE = 0;
	public static final int TRUE = 1;
	public static final int TRUE_OR_FALSE = 2;
	public static final int NOTHING = 0;

	//TOP tokens used for generation of dummy values
	public final static int TOP_INT = 0;
	public final static byte TOP_BYTE = 0;
	public final static short TOP_SHORT = 0;
	public final static double TOP_DOUBLE = 0;
	public final static float TOP_FLOAT = 0;
	public final static long TOP_LONG = 0;
	public final static boolean TOP_BOOL = false;
	public final static char TOP_CHAR = '\0';
	public final static Object TOP_REF = null;

/**
 * 
 * @return int
 */
public static boolean choose() {
	return true;
}
/**
 * 
 * @return int
 * @param values int
 */
public static int choose(int values) {
	if (values == 0) {
		throw new RuntimeException("Abstraction.choose(int) called with 0-valued\n");
	}
	int value;
	for (value = 0; values != 1; value++, values >>= 1) {
		if ((values & 1) == 1) {
			if (choose()) {
				return value;
			}
		}
	}
	return value;
}
/**
 *
 * @return int
 * @param tokens1 int
 * @param tokens2 int
 * @param isTest boolean
 */
public static int meetArith(int tokens1, int tokens2) {
	return tokens1 | tokens2;
}
/**
 * 
 * @return byte
 * @param tokens1 int
 * @param tokens2 int
 */
public static byte meetTest(int tokens1, int tokens2) {
	switch (tokens1) {
		case FALSE:	if (tokens2 == TRUE) return TRUE_OR_FALSE;
			return (byte) tokens2;
		case TRUE: if (tokens2 == FALSE) return TRUE_OR_FALSE;
			return (byte) tokens2;
		case TRUE_OR_FALSE:	return TRUE_OR_FALSE;
		default :	throw new RuntimeException();
	}
}
/**
 * 
 * @return java.lang.String
 */
public abstract String toString();
}
