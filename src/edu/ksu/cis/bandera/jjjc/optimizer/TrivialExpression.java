package edu.ksu.cis.bandera.jjjc.optimizer;

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
import java.math.*;
import java.util.*;

import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.lexer.*;
import edu.ksu.cis.bandera.jjjc.parser.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.*;

import ca.mcgill.sable.laleh.java.astfix.*;
import ca.mcgill.sable.soot.jimple.*;

public final class TrivialExpression extends DepthFirstAdapter {
	private static TrivialExpression te = new TrivialExpression();
	private static boolean number = false;
/**
 * 
 */
private TrivialExpression() {
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	PBinaryOperator op = node.getBinaryOperator();
	node.getFirst().apply(this);
	node.getSecond().apply(this);

	PExp trueValue = new ALiteralExp(new ABooleanLiteralLiteral(new ATrueBooleanLiteral(new TTrue())));
	PExp falseValue = new ALiteralExp(new ABooleanLiteralLiteral(new AFalseBooleanLiteral(new TFalse())));
	PExp first = getExp(node.getFirst());
	PExp second = getExp(node.getSecond());
	
	if (op instanceof AOrBinaryOperator) {
		String firstExp = first.toString().trim();
		String secondExp = second.toString().trim();
		if ("true".equals(firstExp) || "true".equals(secondExp)) {
			node.replaceBy(trueValue);
		} else if ("false".equals(firstExp)) {
			node.replaceBy(node.getSecond());
		} else if ("false".equals(secondExp)) {
			node.replaceBy(node.getFirst());
		}
	} else if (op instanceof AAndBinaryOperator) {
		String firstExp = first.toString().trim();
		String secondExp = second.toString().trim();
		if ("false".equals(firstExp) || "false".equals(secondExp)) {
			node.replaceBy(falseValue);
		} else if ("true".equals(firstExp)) {
			node.replaceBy(node.getSecond());
		} else if ("true".equals(secondExp)) {
			node.replaceBy(node.getFirst());
		}
	} else if (((op instanceof AGtBinaryOperator) || (op instanceof ALtBinaryOperator)
			|| (op instanceof ALteqBinaryOperator) || (op instanceof AGteqBinaryOperator)
			|| (op instanceof AGteqBinaryOperator))	&& (first instanceof ALiteralExp)
			&& (second instanceof ALiteralExp)) {
		try {
			if (((Boolean) doNumber(op.toString().trim(), ((ALiteralExp) first).getLiteral(),
					((ALiteralExp) second).getLiteral())).booleanValue()) {
				node.replaceBy(trueValue);
			} else {
				node.replaceBy(falseValue);
			}
		} catch (Exception e) {}
	} else if (((op instanceof AEqBinaryOperator) || (op instanceof ANeqBinaryOperator))
			&& (first instanceof ALiteralExp)	&& (second instanceof ALiteralExp)) {
		try {
			if (((Boolean) doNumber(op.toString().trim(), ((ALiteralExp) first).getLiteral(),
					((ALiteralExp) second).getLiteral())).booleanValue()) {
				node.replaceBy(trueValue);
			} else {
				node.replaceBy(falseValue);
			}
		} catch (Exception e) {
			if (first.toString().trim().equals(second.toString().trim())) {
				node.replaceBy(trueValue);
			} else {
				node.replaceBy(falseValue);
			}
		}
	}	else if (op instanceof APlusBinaryOperator || op instanceof AMinusBinaryOperator || op instanceof AModBinaryOperator || op instanceof ADivBinaryOperator || op instanceof AStarBinaryOperator || op instanceof ABitXorBinaryOperator || op instanceof ABitAndBinaryOperator || op instanceof AShiftLeftBinaryOperator || op instanceof ASignedShiftRightBinaryOperator || op instanceof AUnsignedShiftRightBinaryOperator || op instanceof ABitOrBinaryOperator) {
		if (number)
			try {
				Object o = doNumber(op.toString().trim(), ((ALiteralExp) first).getLiteral(),
						((ALiteralExp) second).getLiteral());
	
				if (o instanceof Double) {
					String str = "" + ((Double) o).doubleValue();
					node.replaceBy(new ALiteralExp(new AFloatingPointLiteralLiteral(new TFloatingPointLiteral(str))));
				} else if (o instanceof Long) {
					String str = "" + ((Long) o).longValue() + "L";
					node.replaceBy(new ALiteralExp(new AIntegerLiteralLiteral(new ADecimalIntegerLiteral(new TDecimalIntegerLiteral(str)))));
				}
			} catch (Exception e) {}
	} 
}
/**
 * 
 * @return java.lang.Object
 * @param op java.lang.String
 * @param first double
 * @param second double
 */
private Object doNumber(String op, double first, double second) throws Exception {
		if ("==".equals(op)) {
		return new Boolean(first == second);
	} else if ("!=".equals(op)) {
		return new Boolean(first != second);
	} else if (">=".equals(op)) {
		return new Boolean(first >= second);
	} else if ("<=".equals(op)) {
		return new Boolean(first <= second);
	} else if (">".equals(op)) {
		return new Boolean(first > second);
	} else if ("<".equals(op)) {
		return new Boolean(first < second);
	} else if ("+".equals(op)) {
		return new Double(first + second);
	} else if ("-".equals(op)) {
		return new Double(first - second);
	} else if ("%".equals(op)) {
		return new Double(first % second);
	} else if ("/".equals(op)) {
		return new Double(first / second);
	} else if ("*".equals(op)) {
		return new Double(first * second);
	} else {
		throw new Exception();
	}
}
/**
 * 
 * @return java.lang.Object
 * @param op java.lang.String
 * @param first double
 * @param second long
 */
private Object doNumber(String op, double first, long second) throws Exception {
	return doNumber(op, first, (double) second);
}
/**
 * 
 * @return Object
 * @param op java.lang.String
 * @param first long
 * @param second double
 */
private Object doNumber(String op, long first, double second) throws Exception {
	return doNumber(op, (double) first, second);
}
/**
 * 
 * @return int
 * @param op java.lang.String
 * @param first long
 * @param second long
 */
private Object doNumber(String op, long first, long second) throws Exception {
		if ("==".equals(op)) {
		return new Boolean(first == second);
	} else if ("!=".equals(op)) {
		return new Boolean(first != second);
	} else if (">=".equals(op)) {
		return new Boolean(first >= second);
	} else if ("<=".equals(op)) {
		return new Boolean(first <= second);
	} else if (">".equals(op)) {
		return new Boolean(first > second);
	} else if ("<".equals(op)) {
		return new Boolean(first < second);
	} else if ("+".equals(op)) {
		return new Long(first + second);
	} else if ("-".equals(op)) {
		return new Long(first - second);
	} else if ("%".equals(op)) {
		return new Long(first % second);
	} else if ("/".equals(op)) {
		return new Long(first / second);
	} else if ("*".equals(op)) {
		return new Long(first * second);
	} else if ("^".equals(op)) {
		return new Long(first ^ second);
	} else if ("&".equals(op)) {
		return new Long(first & second);
	} else if ("<<".equals(op)) {
		return new Long(first << second);
	} else if (">>".equals(op)) {
		return new Long(first >> second);
	} else {
		return new Long(first >>> second);
	}
}
/**
 * 
 * @return java.lang.Object
 * @param op java.lang.String
 * @param first edu.ksu.cis.bandera.jjjc.node.PLiteral
 * @param second edu.ksu.cis.bandera.jjjc.node.PLiteral
 */
private Object doNumber(String op, PLiteral first, PLiteral second) throws Exception {
	Node node1, node2;

	if (first instanceof AIntegerLiteralLiteral) {
		node1 = ((AIntegerLiteralLiteral) first).getIntegerLiteral();
	} else {
		node1 = ((AFloatingPointLiteralLiteral) first).getFloatingPointLiteral();
	}

	if (second instanceof AIntegerLiteralLiteral) {
		node2 = ((AIntegerLiteralLiteral) second).getIntegerLiteral();
	} else {
		node2 = ((AFloatingPointLiteralLiteral) second).getFloatingPointLiteral();
	}
	
	Number number1 = getNumber(node1);
	Number number2 = getNumber(node2);

	long longValue1 = 0, longValue2 = 0;
	double doubleValue1 = 0.0, doubleValue2 = 0.0;
	boolean isDouble1, isDouble2;
	
	if (number1 instanceof Double) {
		isDouble1 = true;
		doubleValue1 = number1.doubleValue();
	} else {
		isDouble1 = false;
		longValue1 = number1.longValue();
	}
	
	if (number2 instanceof Double) {
		isDouble2 = true;
		doubleValue2 = number2.doubleValue();
	} else {
		isDouble2 = false;
		longValue2 = number2.longValue();
	}

	if (isDouble1) {
		if (isDouble2)
			return doNumber(op, doubleValue1, doubleValue2);
		else
			return doNumber(op, doubleValue1, longValue2);
	} else {
		if (isDouble2)
			return doNumber(op, longValue1, doubleValue2);
		else
			return doNumber(op, longValue1, longValue2);
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.node.PExp
 * @param exp edu.ksu.cis.bandera.jjjc.node.PExp
 */
private PExp getExp(PExp exp) {
	PExp result = exp;

	while (result instanceof AParExp)
		result = ((AParExp) result).getExp();
		
	return result;
}
/**
 * 
 * @return java.lang.Number
 * @param literal edu.ksu.cis.bandera.jjjc.node.PLiteral
 */
private Number getNumber(Node literal) throws Exception {
	Number result;
	
	if (literal instanceof ADecimalIntegerLiteral) {
		String lit = literal.toString().trim();
		if (lit.endsWith("L") || lit.endsWith("l")) {
			result = Long.valueOf(lit.substring(0, lit.length() - 1));
		} else {
			result = Integer.valueOf(lit);
		}
	} else if (literal instanceof AOctalIntegerLiteral) {
		String lit = literal.toString().substring(1).trim();
		if (lit.endsWith("L") || lit.endsWith("l")) {
			result = new Long((new BigInteger(lit.substring(0, lit.length() - 1), 8)).longValue());
		} else {
			result = new Integer(Long.valueOf(lit, 8).intValue());
		}
	} else if (literal instanceof AHexIntegerLiteral) {
		String lit = literal.toString().substring(2).trim();
		if (lit.endsWith("L") || lit.endsWith("l")) {
			result = new Long((new BigInteger(lit.substring(0, lit.length() - 1), 16)).longValue());
		} else {
			result = new Integer(Long.valueOf(lit, 16).intValue());
		}
	} else if (literal instanceof TFloatingPointLiteral) {
		String lit = literal.toString().trim();
		if (lit.endsWith("F") || lit.endsWith("f")) {
			result = Float.valueOf(lit);
		} else {
			result = Double.valueOf(lit);
		}
	} else throw new Exception();
	
	return result;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public static void optimize(Node node) {
	number = false;
	node.apply(te);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 * @param doNumberOperation boolean
 */
public static void optimize(Node node, boolean doNumberOperation) {
	number = doNumberOperation;
	node.apply(te);
}
}
