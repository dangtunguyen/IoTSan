package edu.ksu.cis.bandera.bui.counterexample;

import java.util.*;
//import gov.nasa.arc.ase.jpf.jvm.examine.*;

import ca.mcgill.sable.soot.IntType;
import ca.mcgill.sable.soot.ShortType;

/**
 * Definition and behavior of conditional breakpoints
 * Creation date: (2001/11/3 AM 12:48:44)
 * @author: Yu Chen (MC)
 */

/**
 * Simple syntax of the expression for conditional breakpoints:
 * fqf = full qualified field name
 * operator = > | < | = | !
 *
 * fql operator fql
 * fql *
 * fql operator literal
 *
 */
 
public class ConditionalBreakpoint {
	private String conditionalString = null;
	private TraceManager traceManager = null;
	private boolean validity = false;
	private String token1 = null;
	private String token2 = null;
	private String operator = null;
	
	/**
	 * ConditionalBreakpoint constructor comment.
	 */
	public ConditionalBreakpoint() {
		super();
	}
	public ConditionalBreakpoint(String s, TraceManager tm) {
		conditionalString = s;
		traceManager = tm;
		validate();
	}
/**
 * Check if the condition expression is satisfied by the current store
 * Creation date: (2001/11/5 AM 02:52:46)
 * @return boolean
 */
public boolean isSatisfied() {
	String value1 = null, value2 = null;

	// only does valid conditional express make sense
	if (validity == false) {
		return false;
	} else {
		value1 = traceManager.getFieldValue(token1).toString();
		if (!operator.equals("*")) {
			// get value from the store if token2 is a full qualified name and can be found
			if (traceManager.checkField(token2)) {
				value2 = traceManager.getFieldValue(token2).toString();
			}
			// otherwise, treat token2 as a literal
			else {
				value2 = token2;
			}
		}
		
		switch (operator.charAt(0)) {
			case '>':
				return Integer.parseInt(value1) > Integer.parseInt(value2);
			case '<':
				return Integer.parseInt(value1) > Integer.parseInt(value2);
			case '=':
				return value1.equals(value2);
			case '!':
				return !(value1.equals(value2));
			case '*':
				// to be implemented
				return false;
		}
	}
	return false;
}

	public boolean isValid() {
		return validity;
	}

	// check the validity of the conditional expression
	public String validate() {
		String falseString = "False, ";
		boolean isIntToken1 = false;
		
		// create tokenizer instance returning tokens as well as separators
		StringTokenizer st = new StringTokenizer(conditionalString, "><=!*", true);

		// First token must be a field name
		if (st.hasMoreTokens()) {
			token1 = st.nextToken().trim();
			if (!traceManager.checkField(token1)) {
				validity = false;
				return falseString + "Token 1 (" + token1 + ") not found";
			} else {
				// to be modifed to adapt to more than static field
				if ((traceManager.getFieldValue(token1) instanceof IntType) || (traceManager.getFieldValue(token1) instanceof ShortType)) {
					isIntToken1 = true;
				}
			}
		} else {
			validity = false;
			return falseString + "Missing token 1";
		}

		// Second token must be an operator
		if (st.hasMoreTokens()) {
			operator = st.nextToken().trim();
			// must be one of >, <, =, !, *
			if (!(operator.equals(">") || operator.equals("<") || operator.equals("=") || operator.equals("!") || operator.equals("*"))) {
				validity = false;
				return falseString + "Invalid operator (" + operator + ")";
			} else {
				// for operator ">" and "<", token1 must be of type int or short
				if (operator.equals(">") || operator.equals("<")) {
					if (!(traceManager.getFieldValue(token1) instanceof IntType) && !(traceManager.getFieldValue(token1) instanceof ShortType)) {
						validity = false;
						return falseString + "Token 1 (" + token1 + ") must be of type int or short";
					}
				}
			}
		} else {
			validity = false;
			return falseString + "Missing operator";
		}
		
		// Third token could be literal, null, or another field name
		if (operator.equals("*")) {
			if (st.hasMoreTokens()) {
				validity = false;
				return falseString + "Unexpected token met";
			} else {
				validity = true;
				return "True";
			}		
		}
		if (st.hasMoreTokens()) {
			token2 = st.nextToken().trim();
			// field?
			if (traceManager.checkField(token2)) {
				// for operator ">" and "<", token1 must be of type int or short
				if (operator.equals(">") || operator.equals("<") || isIntToken1) {
					if (!(traceManager.getFieldValue(token2) instanceof IntType) && !(traceManager.getFieldValue(token2) instanceof ShortType)) {
						validity = false;
						return falseString + "Token 2 (" + token2 + ") must be of type int or short";
					}
				}
			}
			// literal? could be integer literal, null or string literal
			else {
				// for operator ">" and "<", literal token2 must be a integer
				if (operator.equals(">") || operator.equals("<") || isIntToken1) {
					try {
						token2 = Integer.toString(Integer.parseInt(token2));
					} catch (Exception e) {
						validity = false;
						return falseString + "Token 2 (" + token2 + ") must be of type int or short";
					}
				}
				// handle the case of null or string literal
				else {
					if (token2.equals("null")) {
						token2 = null;
					}
					else if (token2.startsWith("\"") && token2.endsWith("\"")) {
						token2 = token2.substring(1, token2.length() - 1);
					}
					else {
						return falseString + "Token 2 (" + token2 + ") must be null or string literal";
					}
				}
			}
		} else {
			validity = false;
			return falseString + "Missing token 2";
		}

		// No fourth token is allowed
		if (st.hasMoreTokens()) {
			validity = false;
			return falseString + "Unexpected token met";
		}
		
		validity = true;
		return "True";
	}
}
