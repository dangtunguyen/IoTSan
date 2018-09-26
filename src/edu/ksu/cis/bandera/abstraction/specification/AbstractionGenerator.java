package edu.ksu.cis.bandera.abstraction.specification;

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
import java.io.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.specification.node.*;
import edu.ksu.cis.bandera.abstraction.specification.lexer.*;
import edu.ksu.cis.bandera.abstraction.specification.parser.*;
import edu.ksu.cis.bandera.abstraction.specification.analysis.*;
public class AbstractionGenerator extends DepthFirstAdapter {
	private static final String lineSeparator = System.getProperty("line.separator");
	private static final Hashtable opNameTable = new Hashtable();
	static {
		opNameTable.put("+", "add");
		opNameTable.put("-", "sub");
		opNameTable.put("*", "mul");
		opNameTable.put("/", "div");
		opNameTable.put("%", "rem");
		opNameTable.put("==", "eq");
		opNameTable.put("!=", "ne");
		opNameTable.put(">=", "ge");
		opNameTable.put("<=", "le");
		opNameTable.put(">", "gt");
		opNameTable.put("<", "lt");
	}

	private Vector errors = new Vector();
	private Vector warnings = new Vector();
	private String type;
	
	//
	private StringBuffer buffer;
	private Node node;
	private String superTypeName;
	private String packageName;
	private String abstractionName;
	private Vector tokenSet;
	private HashSet one2oneSet;
	private Collection tempCollection;
	private String[] fields;
	private Vector absFunctions;
	private String paramAbsFunction;
	private boolean foundAnyAbstractDef;
	private boolean isTest;
	private Hashtable opTable;
	private Vector currentOp;
	private int[][] opLeftRight;
	private int[][] opLeftResult;
	private int[][] opRightResult;
	private HashSet possibleArguments;
/**
 * 
 * @param reader
 */
public AbstractionGenerator(Reader reader) {
	try {
		node = new Parser(new Lexer(new PushbackReader(reader))).parse();
	} catch (Exception e) {
		errors.add("Error occured when generating abstraction: \"" + e.getMessage() + "\"");
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AAbstractFunction
 */
public void caseAAbstractFunction(AAbstractFunction node) {
	paramAbsFunction = node.getId().toString().trim();
	{
		Object temp[] = node.getAbstractDef().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PAbstractDef) temp[i]).apply(this);
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AAnyAbstractDef
 */
public void caseAAnyAbstractDef(AAnyAbstractDef node) {
	if (foundAnyAbstractDef) {
		warnings.add("Found '_' before '" + node.toString() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
	} else {
		foundAnyAbstractDef = true;
		String token = node.getId().toString().trim();
		if (tokenSet.contains(token)) {
			absFunctions.add("if (n == n) return " + token + ";");
		} else {
			warnings.add("Invalid token '" + token + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AAnyPattern
 */
public void caseAAnyPattern(AAnyPattern node) {
	if (possibleArguments.size() == 0) {
		warnings.add("Unnecessary " + (isTest? "test" : "operator") + " pattern '" + node.toString() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
		return;
	}
	tempCollection = new HashSet();
	node.getTokenTokenSet().apply(this);
	int result = 0;
	if (isTest) {
		boolean hasTrue = tempCollection.remove("TRUE");
		boolean hasFalse = tempCollection.remove("FALSE");
		for (Iterator i = tempCollection.iterator(); i.hasNext();) {
			warnings.add("Invalid token '" + i.next() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
		}
		if (hasTrue && hasFalse) {
			result = Abstraction.TRUE_OR_FALSE;
			currentOp.add("if (left$ == left$) return Abstraction.choose();");
		} else
			if (hasTrue) {
				result = Abstraction.TRUE;
				currentOp.add("if (left$ == left$) return true;");
			} else
				if (hasFalse) {
					result = Abstraction.FALSE;
					currentOp.add("if (left$ == left$) return false;");
				} else {
					warnings.add("Invalid test pattern definition at line " + node.getSemicolon().getLine() + ". Ignored...");
					return;
				}
	} else {
		int size = 0;
		int lastIndex = -1;
		for (Iterator i = tempCollection.iterator(); i.hasNext();) {
			String token = (String) i.next();
			int index = tokenSet.indexOf(token);
			if (index != -1) {
				result += (1 << index);
				lastIndex = index;
				size++;
			} else {
				warnings.add("Invalid token '" + token + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
			}
		}
		if (size == 1) {
			currentOp.add("if (left$ == left$) return " + tokenSet.elementAt(lastIndex) + ";");
		} else
			if (size > 1) {
				currentOp.add("if (left$ == left$) return Abstraction.choose(" + getExpandedString(result) + ");");
			} else {
				warnings.add("Invalid test pattern definition at line " + node.getSemicolon().getLine() + ". Ignored...");
				return;
			}
	}
	for (Iterator i = possibleArguments.iterator(); i.hasNext();) {
		String tuple = (String) i.next();
		int index = tuple.indexOf(":");
		int leftArg = tokenSet.indexOf(tuple.substring(0, index));
		int rightArg = tokenSet.indexOf(tuple.substring(index + 1));
		opLeftResult[leftArg][result] |= (1 <<  rightArg);
		opLeftRight[leftArg][rightArg] = result;
		opRightResult[rightArg][result] |= (1 << leftArg);
	}
	possibleArguments.clear();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.ADefaultToken
 */
public void caseADefaultToken(ADefaultToken node) {
	String defaultToken = node.getId().toString().trim();
	if (tokenSet.remove(defaultToken)) {
		tokenSet.insertElementAt(defaultToken, 0);
	} else {
		errors.add("Invalid default token '" + defaultToken + "' at line " + node.getSemicolon().getLine());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AExpAbstractDef
 */
public void caseAExpAbstractDef(AExpAbstractDef node) {
	if (foundAnyAbstractDef) {
		warnings.add("Found '_' before '" + node.toString() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
	} else {
		node.getExp().apply(this);
		String token = node.getId().toString().trim();
		if (tokenSet.contains(token)) {
			absFunctions.add("if (" + node.getExp().toString().trim() + ") return " + token + ";");
		} else {
			warnings.add("Invalid token '" + token + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AIdIdList
 */
public void caseAIdIdList(AIdIdList node) {
	TId tid = node.getId();
	String token = tid.toString().trim();
	if (tempCollection.contains(token)) {
		warnings.add("Duplicate occurence of token '" + token + "' at line " + tid.getLine() + ". Ignored...");
	} else {
		tempCollection.add(token);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AIdPrimaryExp
 */
public void caseAIdPrimaryExp(AIdPrimaryExp node) {
	String n = node.getId().toString().trim();
	if (!n.equals(paramAbsFunction)) {
		errors.add("Variable '" + n + "' is undefined at line " + node.getId().getLine());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AIdsIdList
 */
public void caseAIdsIdList(AIdsIdList node) {
	node.getIdList().apply(this);
	TId tid = node.getId();
	String token = tid.toString().trim();
	if (tempCollection.contains(token)) {
		warnings.add("Duplicate occurence of token '" + token + "' at line " + tid.getLine() + ". Ignored...");
	} else {
		tempCollection.add(token);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AIntegralType
 */
public void caseAIntegralType(AIntegralType node) {
	superTypeName = "IntegralAbstraction";
	type = "integral";
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AOperator
 */
public void caseAOperator(AOperator node) {
	String op = node.getOp().toString().trim();
	currentOp = new Vector();
	opLeftRight = new int[fields.length][fields.length];
	opLeftResult = new int[fields.length][1 << fields.length];
	opRightResult = new int[fields.length][1 << fields.length];
	isTest = false;
	createPossibleArguments();
	/*
	for (int i = 0; i < fields.length; i++) {
		for (int j = 0; j < (1 << fields.length); i++) {
			opLeftResult[i][j] = 0;
			opRightResult[i][j] = 0;
		}
	}
	*/
	{
		Object temp[] = node.getPattern().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PPattern) temp[i]).apply(this);
		}
	}
	opTable.put(op, new Object[] { opLeftResult, currentOp, opLeftRight, opRightResult });
	if (possibleArguments.size() > 0)
		warnings.add("Cases " +  possibleArguments + " are not handled in " + abstractionName + "." + opNameTable.get(op));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.APatternPattern
 */
public void caseAPatternPattern(APatternPattern node) {
	if (possibleArguments.size() == 0) {
		warnings.add("Unnecessary " + (isTest ? "test" : "operator") + " pattern '" + node.toString() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
		return;
	}
	tempCollection = new HashSet();
	node.getTokenTokenSet().apply(this);
	int result = 0;
	int lastIndex = -1;
	boolean useChoose = false;
	//HashSet resultToken = new HashSet();
	HashSet otherToken = new HashSet();
	if (isTest) {
		boolean hasTrue = tempCollection.remove("TRUE");
		boolean hasFalse = tempCollection.remove("FALSE");
		for (Iterator i = tempCollection.iterator(); i.hasNext();) {
			warnings.add("Invalid token '" + i.next() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
		}
		if (hasTrue && hasFalse) {
			result = Abstraction.TRUE_OR_FALSE;
			useChoose = true;
			//resultToken.add("TRUE");
			//resultToken.add("FALSE");
		} else
			if (hasTrue) {
				result = Abstraction.TRUE;
				//resultToken.add("TRUE");
			} else
				if (hasFalse) {
					result = Abstraction.FALSE;
					//resultToken.add("FALSE");
				} else {
					warnings.add("Invalid test pattern definition (does not return TRUE/FALSE/T) at line " + node.getSemicolon().getLine() + ". Ignored...");
					return;
				}
	} else {
		int size = 0;
		for (Iterator i = tempCollection.iterator(); i.hasNext();) {
			String token = (String) i.next();
			int index = tokenSet.indexOf(token);
			if (index != -1) {
				result += (1 << index);
				lastIndex = index;
				size++;
				//resultToken.add(token);
			} else {
				otherToken.add(token);
			}
		}
		if (size > 1) {
			useChoose = true;
		}
	}
	String rid = node.getRId().toString().trim();
	String lid = node.getLId().toString().trim();
	int caseNo = 0;
	if ("_".equals(lid)) {
		if ("_".equals(rid)) {
            APatternPattern nodeClone = (APatternPattern) node.clone();
			new AAnyPattern(new TAny(), nodeClone.getRightarrow(), nodeClone.getTokenTokenSet(), nodeClone.getSemicolon()).apply(this);
		} else
			if (tokenSet.contains(rid)) {
				int index = tokenSet.indexOf(rid);
				boolean necessary = false;
				for (int i = 0; i < fields.length; i++) {
					if (possibleArguments.remove(fields[i] + ":" + fields[index])) {
						necessary = true;
						opLeftResult[i][result] |= (1 << index);
						opLeftRight[i][index] = result;
						opRightResult[index][result] |= (1 << i);
					}
				}
				if (necessary) {
					if (isTest) {
						switch (result) {
							case Abstraction.FALSE:
								currentOp.add("if (right$ == " + rid + ") return false;");
								break;
							case Abstraction.TRUE:
								currentOp.add("if (right$ == " + rid + ") return true;");
								break;
							case Abstraction.TRUE_OR_FALSE:
								currentOp.add("if (right$ == " + rid + ") return Abstraction.choose();");
								break;
							default: throw new RuntimeException();
						}
					} else {
						for (Iterator i = otherToken.iterator(); i.hasNext();) {
							warnings.add("Invalid token '" + i.next() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
						}
						if (result == 0) {
							warnings.add("Invalid operator pattern definition (no valid result) at line " + node.getSemicolon().getLine() + ". Ignored...");
							return;
						}
						if (useChoose) {
							currentOp.add("if (right$ == " + rid + ") return Abstraction.choose(" + getExpandedString(result) + ");");
						} else {
							currentOp.add("if (right$ == " + rid + ") return " + fields[lastIndex] + ";");
						}
					}
				} else {
					warnings.add("Unnecessary " + (isTest ? "test" : "operator") + " pattern '" + node.toString() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
					return;
				}
			} else {
				warnings.add("Invalid " + (isTest ? "test" : "operator") + " pattern definition (mixing Wild and Var) at line " + node.getSemicolon().getLine() + ". Ignored...");
			}
	} else
		if ("_".equals(rid)) {
			if (tokenSet.contains(lid)) {
				int index = tokenSet.indexOf(lid);
				boolean necessary = false;
				for (int i = 0; i < fields.length; i++) {
					if (possibleArguments.remove(fields[index] + ":" + fields[i])) {
						necessary = true;
						opLeftResult[index][result] |= (1 << i);
						opLeftRight[index][i] = result;
						opRightResult[i][result] |= (1 << index);
					}
				}
				if (necessary) {
					if (isTest) {
						switch (result) {
							case Abstraction.FALSE:
								currentOp.add("if (left$ == " + lid + ") return false;");
								break;
							case Abstraction.TRUE:
								currentOp.add("if (left$ == " + lid + ") return true;");
								break;
							case Abstraction.TRUE_OR_FALSE:
								currentOp.add("if (left$ == " + lid + ") return Abstraction.choose(); // [TRUE, FALSE]");
								break;
							default: throw new RuntimeException();
						}
					} else {
						if (useChoose) {
							currentOp.add("if (left$ == " + lid + ") return Abstraction.choose(" + getExpandedString(result) + ");");
						} else {
							currentOp.add("if (left$ == " + lid + ") return " + fields[lastIndex] + ";");
						}
					}
				} else {
					warnings.add("Unnecessary " + (isTest ? "test" : "operator") + " pattern '" + node.toString() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
					return;
				}
			} else {
				warnings.add("Invalid " + (isTest ? "test" : "operator") + " pattern definition (mixing Wild and Var) at line " + node.getSemicolon().getLine() + ". Ignored...");
			}
		} else
			if (!tokenSet.contains(lid)) {
				if (isTest) {
					warnings.add("Invalid test pattern definition (using Var) at line " + node.getSemicolon().getLine() + ". Ignored...");
					return;
				}
				if (!tokenSet.contains(rid)) {
					warnings.add("Invalid operator pattern definition (using two Vars) at line " + node.getSemicolon().getLine() + ". Ignored...");
					return;
				} else {
					if ((otherToken.size() == 1) && (result == 0) && (otherToken.iterator().next().equals(lid))) {
						int index = tokenSet.indexOf(rid);
						boolean necessary = false;
						for (int i = 0; i < fields.length; i++) {
							if (possibleArguments.remove(fields[i] + ":" + fields[index])) {
								necessary = true;
								opLeftResult[i][result] |= (1 << index);
								opLeftRight[i][index] = result;
								opRightResult[index][result] |= (1 << i);
							}
						}
						if (necessary) {
							currentOp.add("if (right$ == " + rid + ") return left$;");
						} else {
							warnings.add("Invalid operator pattern definition (Var declared but not used) at line " + node.getSemicolon().getLine() + ". Ignored...");
							return;
						}
					}
				}
			} else
				if (!tokenSet.contains(rid)) {
					if (isTest) {
						warnings.add("Invalid test pattern definition (using Var) at line " + node.getSemicolon().getLine() + ". Ignored...");
						return;
					}
					if ((otherToken.size() == 1) && (result == 0) && (otherToken.iterator().next().equals(rid))) {
						int index = tokenSet.indexOf(lid);
						boolean necessary = false;
						for (int i = 0; i < fields.length; i++) {
							if (possibleArguments.remove(fields[index] + ":" + fields[i])) {
								necessary = true;
								opLeftResult[index][result] |= (1 << i);
								opLeftResult[index][i] = result;
								opRightResult[i][result] |= (1 << index);
							}
						}
						if (necessary) {
							currentOp.add("if (left$ == " + lid + ") return right$;");
						} else {
							warnings.add("Invalid operator pattern definition (Var declared but not used) at line " + node.getSemicolon().getLine() + ". Ignored...");
							return;
						}
					}
				} else {
					if (possibleArguments.remove(lid + ":" + rid)) {
						if (isTest) {
							switch (result) {
								case Abstraction.FALSE:
									currentOp.add("if (left$ == " + lid + " && right$ == " + rid + ") return false;");
									break;
								case Abstraction.TRUE:
									currentOp.add("if (left$ == " + lid + " && right$ == " + rid + ") return true;");
									break;
								case Abstraction.TRUE_OR_FALSE:
									currentOp.add("if (left$ == " + lid + " && right$ == " + rid + ") return Abstraction.choose();");
									break;
								default: throw new RuntimeException();
							}
							int lidx = tokenSet.indexOf(lid);
							int ridx = tokenSet.indexOf(rid);
							opLeftResult[lidx][result] |= (1 << ridx);
							opLeftRight[lidx][ridx] = result;
							opRightResult[ridx][result] |= (1 << lidx);
						} else {
							for (Iterator i = otherToken.iterator(); i.hasNext();) {
								warnings.add("Invalid token '" + i.next() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
							}
							if (result == 0) {
								warnings.add("Invalid operator pattern definition (no valid result) at line " + node.getSemicolon().getLine() + ". Ignored...");
								return;
							}
							int lidx = tokenSet.indexOf(lid);
							int ridx = tokenSet.indexOf(rid);
							opLeftResult[lidx][result] |= (1 << ridx);
							opLeftRight[lidx][ridx] = result;
							opRightResult[ridx][result] |= (1 << lidx);
							if (useChoose) {
								currentOp.add("if (left$ == " + lid + " && right$ == " + rid + ") return Abstraction.choose(" + getExpandedString(result) + ");");
							} else {
								currentOp.add("if (left$ == " + lid + " && right$ == " + rid + ") return " + fields[lastIndex] + ";");
							}
						}
					} else {
						warnings.add("Unnecessary " + (isTest ? "test" : "operator") + " pattern '" + node.toString() + "' at line " + node.getSemicolon().getLine() + ". Ignored...");
						return;
					}
				}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.ARealType
 */
public void caseARealType(ARealType node) {
	superTypeName = "RealAbstraction";
	type = "real";
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.ATest
 */
public void caseATest(ATest node) {
	String op = node.getTOp().toString().trim();
	currentOp = new Vector();
	opLeftRight = new int[fields.length][fields.length];
	opLeftResult = new int[fields.length][3];
	opRightResult = new int[fields.length][3];
	isTest = true;
	createPossibleArguments();
	for (int i = 0; i < fields.length; i++) {
		for (int j = 0; j < fields.length; j++) {
			opLeftRight[i][j] = -1;
		}
	}
	
	{
		Object temp[] = node.getPattern().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PPattern) temp[i]).apply(this);
		}
	}
	opTable.put(op, new Object[] {opLeftResult, currentOp, opLeftRight, opRightResult});
	if (possibleArguments.size() > 0)
		warnings.add("Cases " +  possibleArguments + " are not handled in " + abstractionName + "." + opNameTable.get(op));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.ATokenTokenTokenSet
 */
public void caseATokenTokenTokenSet(ATokenTokenTokenSet node) {
	String token = node.getId().toString().trim();
	if ("T".equals(token)) {
		if (isTest) {
			tempCollection.add("TRUE");
			tempCollection.add("FALSE");
		} else {
			for (int i = 0; i < fields.length; i++) {
				tempCollection.add(fields[i]);
			}
		}
	} else {
		tempCollection.add(token);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AUnit
 */
public void caseAUnit(AUnit node) {
	node.getType().apply(this);
	
	abstractionName = node.getId().toString().trim();

	tempCollection = new Vector();
		
	node.getTokenSet().apply(this);

	tokenSet = (Vector) tempCollection;

	if (node.getDefaultToken() != null)	{
		node.getDefaultToken().apply(this);
	} else {
		errors.add("Default token is undefined");
	}

	fields = new String[tokenSet.size()];
	
	System.arraycopy(tokenSet.toArray(), 0, fields, 0, tokenSet.size());

	tempCollection = new HashSet();
	
	if (node.getOne2oneSet() != null)
		node.getOne2oneSet().apply(this);

	one2oneSet = new HashSet();

	for (Iterator i = tempCollection.iterator(); i.hasNext();) {
		String token = (String) i.next();
		if (tokenSet.contains(token)) {
			one2oneSet.add(token);
		} else {
			warnings.add("Invalid token '" + token + "' at ONE2ONE token set definition");
		}
	}

	absFunctions = new Vector();
	
	node.getAbstractFunction().apply(this);

	opTable = new Hashtable();
	
	{
		Object temp[] = node.getOperatorTest().toArray();
		for (int i = 0; i < temp.length; i++) {
			((POperatorTest) temp[i]).apply(this);
		}
	}
}
/**
 * 
 */
private void createPossibleArguments() {
	possibleArguments = new HashSet();
	for (int i = 0; i < fields.length; i++) {
		for (int j = 0; j < fields.length; j++) {
			possibleArguments.add(fields[i] + ":" + fields[j]);
		}
	}
}
/**
 * 
 * @return java.lang.String
 */
public String generate(String packageName) {
	if (node != null) node.apply(this);
	if (errors.size() > 0) return null;

	buffer = new StringBuffer("// This file was generated by Bandera (http://www.cis.ksu.edu/santos/bandera)." + lineSeparator);

	if (warnings.size() > 0) {
		println("//");
		println("// WARNING:");
		println("// --------");
		for (Iterator i = warnings.iterator(); i.hasNext();) {
			println("// * " + i.next());
		}
		println("//");
		println("//");
	}
	
	String typeName = abstractionName;

	if (packageName != null) {
		if ("".equals(packageName))
			this.packageName = type;
		else
			this.packageName = packageName + ("." + type);
		println("package " + this.packageName + ";");
	}
	println("import edu.ksu.cis.bandera.abstraction.*;");
	println("public class " + typeName + " extends " + superTypeName + " {");
	println("  private static final " + typeName + " v = new " + typeName + "();");

	for (int i = 0; i < fields.length; i++) {
		println("  public static final int " + fields[i] + " = " + i + ";");
	}

	println("  private " + typeName + "() {");
	println("  }");

	println("  public static " + typeName + " v() {");
	println("    return v;");
	println("  }");

	println("  public static int getNumOfTokens() {");
	println("    return " + fields.length + ";");
	println("  }");
	

	println("  public static String getToken(int value) {");
	println("    switch(value) {");
	for (int i = 0; i < fields.length; i++) {
		println("      case " + fields[i] + ": return \"" + abstractionName + "." + fields[i] + "\";");
	}
	println("    }");
	println("    return \"" + abstractionName + ".???\";");
	println("  }");

	println("  public static boolean isOne2One(int value) {");
	if (one2oneSet.size() > 0) {
		println("    switch(value) {");
		for (Iterator i = one2oneSet.iterator(); i.hasNext();) {
			println("      case " + i.next() + ": return true;");
		}
		println("    }");
	}
	println("    return false;");
	println("  }");

	println("  public static int abs(" + ("integral".equals(type) ? "long " : "double ") + paramAbsFunction + ") {");
	for (Iterator i = absFunctions.iterator(); i.hasNext();) {
		println("    " + i.next());
	}
	println("    throw new RuntimeException(\"" + typeName + " cannot abstract value '\" + " + paramAbsFunction + " + \"'\");");
	println("  }");

	generateOperators();
	
	generateBASL();
	
	println("  public static String getName() {");
	println("    return \"" + abstractionName + "\";");
	println("  }");
	println("  public String toString() {");
	println("    return getName();");
	println("  }");
	println("}");

	return buffer.toString();
}
/**
 * 
 */
private void generateBASL() {
	println("  public static String getBASLRepresentation() {");
	println("    return");
	try {
		LineNumberReader r = new LineNumberReader(new StringReader(AbstractionPrinter.print((Start) node, true)));
		String line = r.readLine();
		println("      \"" + line + "\\n\"");
		while ((line = r.readLine()) != null) {
			println("      + \"" + line + "\\n\"");
		}
	} catch (Exception e) {
        e.printStackTrace();
	}
	println("    ;");
	println("  }");
}
/**
 * 
 */
private void generateOperators() {
	String typeName = abstractionName;
	
	for (Enumeration e = opNameTable.keys(); e.hasMoreElements();) {
		String op = (String) e.nextElement();
		String opName = (String) opNameTable.get(op);
		boolean isTest = (op.length() > 1) || ">".equals(op) || "<".equals(op); 
		Object[] o = (Object[]) opTable.get(op);
		if (o == null) continue;
		opLeftResult = (int[][]) o[0];
		currentOp = (Vector) o[1];
		opLeftRight = (int[][]) o[2];
		opRightResult = (int[][]) o[3];

		if (isTest) {
			println("  public static boolean " + opName + "(int left$, int right$) {");
		} else {
			println("  public static int " + opName + "(int left$, int right$) {");
		}
		for (Iterator j = currentOp.iterator(); j.hasNext();) {
			println("    " + j.next());
		}
		println("    throw new RuntimeException(\"" + typeName + "." + opName + "(\" + left$ + \", \" + right$ + \") is undefined\");"); 
		println("  }");

		if (isTest) {
			println("  private static byte " + opName + "NoChoose(int left, int right) {");
			println("    byte result = -1;");
		} else {
			println("  private static int " + opName + "NoChoose(int left, int right) {");
			println("    int result = 0;");
		}
		println("    switch (left) {");
		for (int i = 0; i < fields.length; i++) {
			println("      case " + i + ":");
			println("        switch (right) {");
			for (int j = 0; j < fields.length; j++) {
				if ((!isTest && opLeftRight[i][j] != 0) || (isTest && opLeftRight[i][j] != -1)) {
					println("          case " + j + ":");
					println("            result = " + opLeftRight[i][j] + ";");
					println("            break;");
				}
			}
			println("        }");
			println("        break;");
		}
		println("    }");
		if (isTest) {
			println("    if (result == -1) throw new RuntimeException(\"" + typeName + "." + opName + "NoChoose(\" + left + \", \" + right + \") is undefined\");");
		} else {
			println("    if (result == 0) throw new RuntimeException(\"" + typeName + "." + opName + "NoChoose(\" + left + \", \" + right + \") is undefined\");");
		}
		println("    return result;"); 
		println("  }");
		

		if (isTest) {
			println("  public static byte " + opName + "Set(int leftTokens, int rightTokens) {");
			println("    byte result = -1;");
		} else {
			println("  public static int " + opName + "Set(int leftTokens, int rightTokens) {");
			println("    int result = -1;");
		}
		println("    for (int left = 0; (1 << left) <= leftTokens; left++) {");
		println("      if ((leftTokens & (1 << left)) == 0) continue;");
		println("      for (int right = 0; (1 << right) <= rightTokens; right++) {");
		println("        if ((rightTokens & (1 << right)) != 0) {");
		println("          if (result == -1) result = " + opName + "NoChoose(left, right);");
		if (isTest) {
			println("          else result = Abstraction.meetTest(result, " + opName + "NoChoose(left, right));");
		} else {
			println("          else result = Abstraction.meetArith(result, " + opName + "NoChoose(left, right));");
		}
		println("        }");
		println("      }");
		println("    }");
		println("    return result;");
		println("  }");
		
		/*
		println("  public static int " + opName + "LeftArg(int rightArg, int result) {");
		println("    switch (rightArg) {");
		for (int j = 0; j < fields.length; j++) {
			println("      case " + fields[j] + ":");
			println("        switch (result) {");
			if (isTest) {
				for (int k = 0; k < 3; k++) {
					println("          case " + (k == 0 ? "FALSE" : (k == 1 ? "TRUE" : "TRUE_OR_FALSE")) + ": return " + getExpandedString(opRightResult[j][k]) + ";");
				}
			} else {
				for (int k = 1; k < (1 << fields.length); k++) {
					println("          case " + getExpandedString(k) + ": return " + getExpandedString(opRightResult[j][k]) + ";");
				}
			}
			println("        }");
			println("        break;");
		}
		println("    }");
		println("    return -1;");
		println("  }");

		println("  public static int " + opName + "RightArg(int leftArg, int result) {");
		println("    switch (leftArg) {");
		for (int j = 0; j < fields.length; j++) {
			println("      case " + fields[j] + ":");
			println("        switch (result) {");
			if (isTest) {
				for (int k = 0; k < 3; k++) {
					println("          case " + (k == 0 ? "FALSE" : (k == 1 ? "TRUE" : "TRUE_OR_FALSE")) + ": return " + getExpandedString(opLeftResult[j][k]) + ";");
				}
			} else {
				for (int k = 1; k < (1 << fields.length); k++) {
					println("          case " + getExpandedString(k) + ": return " + getExpandedString(opLeftResult[j][k]) + ";");
				}
			}
			println("        }");
			println("        break;");
		}
		println("    }");
		println("    return -1;");
		println("  }");
		*/
	}
}
/**
 * 
 */
private void generateOperatorSetTable() {
	/*
	String typeName = abstractionName;
	for (Enumeration e = opNameTable.keys(); e.hasMoreElements();) {
		String op = (String) e.nextElement();
		String opName = (String) opNameTable.get(op);
		opLeftRight = (int[][]) ((Object[]) opTable.get(op))[2];
		int size = 1 << fields.length;
		boolean isTest = (op.length() > 1) || ">".equals(op) || "<".equals(op); 
		println("  private static int[][] " + opName + "SetTable = {");
		for (int i = 1; i < size; i++) {
			print("    {");
			for (int j = 1; j < size; j++) {
				int result = -1;
				for (int left = 0; left < fields.length; left++) {
					for (int right = 0; right < fields.length; right++) {
						if (((i & (1 << left)) == (1 << left)) && ((j & (1 << right)) == (1 << right))) {
							if (result == -1)
								result = opLeftRight[left][right];
							else
								result = Abstraction.meet(result, opLeftRight[left][right], isTest);
						}
					}
				}
				if (j + 1 == size) {
					print(result + "}");
					if (i + 1 != size) {
						println(",");
					}	else {
						println("");
					}
				} else {
					print(result + ", ");
				}
			}
		}
		println("  };");
	}
	*/
}
/**
 * 
 */
private void generateOperatorsTable() {
	/*
	String typeName = abstractionName;
	for (Enumeration e = opNameTable.keys(); e.hasMoreElements();) {
		String op = (String) e.nextElement();
		String opName = (String) opNameTable.get(op);
		opLeftRight = (int[][]) ((Object[]) opTable.get(op))[2];
		boolean isTest = (op.length() > 1) || ">".equals(op) || "<".equals(op);

		if (isTest) {
			println("  private static byte[][] " + opName + "Table = {");
		} else {
			println("  private static int[][] " + opName + "Table = {");
		}
		for (int i = 0; i < fields.length; i++) {
			print("    {");
			for (int j = 0; j < fields.length; j++) {
				if (j + 1 == fields.length) {
					print(Integer.toString(opLeftRight[i][j]));
				} else {
					print(opLeftRight[i][j] + ", ");
				}
			}
			if (i + 1 == fields.length) {
				println("}");
			} else {
				println("},");
			}
		}
		println("  };");
	}
	*/
}
/**
 * 
 * @return java.lang.String
 */
public String getAbstractionFullyQualifiedName() {
	return packageName + "." + getAbstractionName();
}
/**
 * 
 * @return java.lang.String
 */
public String getAbstractionName() {
	return abstractionName;
}
/**
 * 
 * @return java.lang.String
 */
public String getAbstractionType() {
	return type;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getErrors() {
	return errors;
}
/**
 * 
 * @return java.lang.String
 * @param values int
 */
private String getExpandedString(int values) {
	HashSet set = new HashSet();
	for (int l = 0, mask = 1; l < fields.length; l++, mask <<= 1) {
		if ((values & mask) == mask) {
			set.add(fields[l]);
		}
	}
	switch (set.size()) {
		case 0:
			return "NOTHING";
		case 1:
			return "(1 << " + set.iterator().next() + ")";
		default:
			Iterator i = set.iterator();
			String result = (String) ("(1 << " + i.next() + ")");
			while (i.hasNext()) result += (" | (1 << " + i.next() + ")");
			return result;			
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.abstraction.specification.node.Start
 */
public Start getNode() {
	return (Start) node;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getWarnings() {
	return warnings;
}
/**
 * 
 * @param s java.lang.String
 */
private void print(String s) {
	buffer.append(s);
}
/**
 * 
 * @param s java.lang.String
 */
private void println(String s) {
	buffer.append(s + lineSeparator);
}
}
