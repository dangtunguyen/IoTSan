package edu.ksu.cis.bandera.specification.predicate.ast;

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
import edu.ksu.cis.bandera.specification.predicate.exception.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;
import edu.ksu.cis.bandera.specification.predicate.node.*;
import java.util.*;
public class Simplifier extends DepthFirstAdapter {
	private static Simplifier simplifier = new Simplifier();
	private PExp exp;
	private Vector exceptions = new Vector();
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.ANameExp
 * @param name java.lang.String
 */
private ANameExp buildName(String name) {
	StringTokenizer tokenizer = new StringTokenizer(name, ". ");
	PName result = new ASimpleName(new TId(tokenizer.nextToken()));
	
	while (tokenizer.hasMoreTokens()) {
		result = new AQualifiedName(result, new TDot(), new TId(tokenizer.nextToken()));
	}
	
	return new ANameExp(result);
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 * @param baseExp edu.ksu.cis.bandera.predicate.node.PExp
 * @param navExp java.lang.String
 */
private PExp buildNavigation(PExp baseExp, String navExp) {
	StringTokenizer tokenizer = new StringTokenizer(navExp, " ");
	
	do {
		String token = tokenizer.nextToken();
		if (".".equals(token)) {
			baseExp = new ANavigationExp(baseExp,
					new AStrongObjectNavigation(new TDot(), new TId(tokenizer.nextToken())));
		} /*else if ("..".equals(token)) {
			baseExp = new ANavigationExp(baseExp,
					new AWeakObjectNavigation(new TWeakObjectReference(), new TId(tokenizer.nextToken())));
		}*/
	} while (tokenizer.hasMoreTokens());
	return baseExp;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AAndAndExp
 */
public void caseAAndAndExp(AAndAndExp node) {
	node.getAndExp().apply(this);
	PExp left = exp;
	node.getInclusiveOrExp().apply(this);
	exp = new ABinaryExp(left, new AAndBinOp(node.getAnd()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AAndBitAndExp
 */
public void caseAAndBitAndExp(AAndBitAndExp node) {
	node.getBitAndExp().apply(this);
	PExp left = exp;
	node.getEqExp().apply(this);
	exp = new ABinaryExp(left, new ABitAndBinOp(node.getBitAnd()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ABitComplementUnaryNotPlusMinusExp
 */
public void caseABitComplementUnaryNotPlusMinusExp(ABitComplementUnaryNotPlusMinusExp node) {
	node.getUnaryExp().apply(this);
	exp = new ABitComplementExp(node.getBitComplement(), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AComplementUnaryNotPlusMinusExp
 */
public void caseAComplementUnaryNotPlusMinusExp(AComplementUnaryNotPlusMinusExp node) {
	node.getUnaryExp().apply(this);
	exp = new AComplementExp(node.getNot(), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AConditionalExp
 */
public void caseAConditionalExp(AConditionalExp node) {
	node.getConditionalExp().apply(this);
	node.replaceBy(exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AEqEqExp
 */
public void caseAEqEqExp(AEqEqExp node) {
	node.getEqExp().apply(this);
	PExp left = exp;
	node.getRelExp().apply(this);
	exp = new ABinaryExp(left, new AEqualBinOp(node.getEqual()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AGreaterEqualRelExp
 */
public void caseAGreaterEqualRelExp(AGreaterEqualRelExp node) {
	node.getRelExp().apply(this);
	PExp left = exp;
	node.getShiftExp().apply(this);
	exp = new ABinaryExp(left, new AGreaterEqualBinOp(node.getGreaterEqual()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AGreaterRelExp
 */
public void caseAGreaterRelExp(AGreaterRelExp node) {
	node.getRelExp().apply(this);
	PExp left = exp;
	node.getShiftExp().apply(this);
	exp = new ABinaryExp(left, new AGreaterBinOp(node.getGreater()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AInstanceofRelExp
 */
public void caseAInstanceofRelExp(AInstanceofRelExp node) {
	node.getRelExp().apply(this);
	if (node.getType().toString().indexOf("..") >= 0) {
		exceptions.add(new WeedException("Invalid type name " + node.getType()));
	} else {
		exp = new AInstanceofExp(exp, node.getInstanceof(), node.getType());
	}
}
/**
 * @param node edu.ksu.cis.bandera.bpdl.node.ALeftShiftExp
 */
public void caseALeftShiftExp(ALeftShiftExp node) {
	node.getShiftExp().apply(this);
	PExp left = exp;
	node.getAddExp().apply(this);
	exp = new ABinaryExp(left, new AShiftLeftBinOp(node.getShiftLeft()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ALessEqualRelExp
 */
public void caseALessEqualRelExp(ALessEqualRelExp node) {
	node.getRelExp().apply(this);
	PExp left = exp;
	node.getShiftExp().apply(this);
	exp = new ABinaryExp(left, new ALessEqualBinOp(node.getLessEqual()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ALessRelExp
 */
public void caseALessRelExp(ALessRelExp node) {
	node.getRelExp().apply(this);
	PExp left = exp;
	node.getShiftExp().apply(this);
	exp = new ABinaryExp(left, new ALessBinOp(node.getLess()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ALiteralPrimaryExp
 */
public void caseALiteralPrimaryExp(ALiteralPrimaryExp node) {
	exp = new ALiteralExp(node.getLiteral());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AMinusAddExp
 */
public void caseAMinusAddExp(AMinusAddExp node) {
	node.getAddExp().apply(this);
	PExp left = exp;
	node.getMultExp().apply(this);
	exp = new ABinaryExp(left, new AMinusBinOp(node.getMinus()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AMinusUnaryExp
 */
public void caseAMinusUnaryExp(AMinusUnaryExp node) {
	node.getUnaryExp().apply(this);
	exp = new ANegativeExp(node.getMinus(), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ANamePostfixExp
 */
public void caseANamePostfixExp(ANamePostfixExp node) {
	String nameExp = node.getName().toString().trim();
	int i = nameExp.indexOf("..");
	if (i >= 0) {
		String navigationExp = nameExp.substring(i);
		nameExp = nameExp.substring(0, i);
		exp = buildNavigation(buildName(nameExp), navigationExp);
	} else {
		exp = new ANameExp(node.getName());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ANeqEqExp
 */
public void caseANeqEqExp(ANeqEqExp node) {
	node.getEqExp().apply(this);
	PExp left = exp;
	node.getRelExp().apply(this);
	exp = new ABinaryExp(left, new ANotEqualBinOp(node.getNotEqual()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AOrInclusiveOrExp
 */
public void caseAOrInclusiveOrExp(AOrInclusiveOrExp node) {
	node.getInclusiveOrExp().apply(this);
	PExp left = exp;
	node.getExclusiveOrExp().apply(this);
	exp = new ABinaryExp(left, new ABitOrBinOp(node.getBitOr()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AOrOrExp
 */
public void caseAOrOrExp(AOrOrExp node) {
	node.getOrExp().apply(this);
	PExp left = exp;
	node.getAndExp().apply(this);
	exp = new ABinaryExp(left, new AOrBinOp(node.getOr()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AParenPrimaryExp
 */
public void caseAParenPrimaryExp(AParenPrimaryExp node) {
	node.getExp().apply(this);
	exp = new AParenExp(node.getLParen(), exp, node.getRParen());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.APlusAddExp
 */
public void caseAPlusAddExp(APlusAddExp node) {
	node.getAddExp().apply(this);
	PExp left = exp;
	node.getMultExp().apply(this);
	exp = new ABinaryExp(left, new APlusBinOp(node.getPlus()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AQuestionConditionalExp
 */
public void caseAQuestionConditionalExp(AQuestionConditionalExp node) {
	node.getOrExp().apply(this);
	PExp condition = exp;
	node.getTrueExp().apply(this);
	PExp trueExp = exp;
	node.getFalseExp().apply(this);
	exp = new AQuestionExp(condition, node.getQuestion(), trueExp, node.getColon(), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AReturnValuePrimaryExp
 */
public void caseAReturnValuePrimaryExp(AReturnValuePrimaryExp node) {
	exp = new AReturnValueExp(node.getRetVal());
}
/**
 * @param node edu.ksu.cis.bandera.bpdl.node.ASignedRightShiftExp
 */
public void caseASignedRightShiftExp(ASignedRightShiftExp node) {
	node.getShiftExp().apply(this);
	PExp left = exp;
	node.getAddExp().apply(this);
	exp = new ABinaryExp(left, new ASignedShiftRightBinOp(node.getSignedShiftRight()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AStrongArrayNavigation
 */
public void caseAStrongArrayNavigation(AStrongArrayNavigation node) {
	PExp nav = exp;
	node.getExp().apply(this);
	node.setExp(exp);
	exp = new ANavigationExp(exp, node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AStrongDivMultExp
 */
public void caseAStrongDivMultExp(AStrongDivMultExp node) {
	node.getMultExp().apply(this);
	PExp left = exp;
	node.getUnaryExp().apply(this);
	exp = new ABinaryExp(left, new AStrongDivBinOp(node.getStrongDiv()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongExpArrayAccess
 */
public void caseAStrongExpArrayAccess(AStrongExpArrayAccess node) {
	node.getPrimaryExp().apply(this);
	PExp baseExp = exp;
	node.getExp().apply(this);
	exp = new ANavigationExp(baseExp, new AStrongArrayNavigation(node.getLBracket(),
			exp, node.getRBracket()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongExpCastExp
 */
public void caseAStrongExpCastExp(AStrongExpCastExp node) {
	node.getExp().apply(this);
	ANameExp name = (ANameExp) exp;
	if (name.toString().indexOf("..") >= 0) {
		exceptions.add(new WeedException("Invalid type name " + name));
	} else {
		node.getUnaryNotPlusMinusExp().apply(this);
		exp = new AStrongCastExp(node.getLParen(), name.getName(), new LinkedList(), node.getRParen(), exp);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AStrongModMultExp
 */
public void caseAStrongModMultExp(AStrongModMultExp node) {
	node.getMultExp().apply(this);
	PExp left = exp;
	node.getUnaryExp().apply(this);
	exp = new ABinaryExp(left, new AStrongModBinOp(node.getStrongMod()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongNameArrayAccess
 */
public void caseAStrongNameArrayAccess(AStrongNameArrayAccess node) {
	String nameExp = node.getName().toString().trim();
	int i = nameExp.indexOf("..");
	if (i >= 0) {
		String navigationExp = nameExp.substring(i);
		nameExp = nameExp.substring(0, i);
		exp = buildNavigation(buildName(nameExp), navigationExp);
	} else {
		exp = new ANameExp(node.getName());
	}
	PExp baseExp = exp;
	node.getExp().apply(this);
	exp = new ANavigationExp(baseExp, new AStrongArrayNavigation(node.getLBracket(),
			exp, node.getRBracket()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AStrongNameCastExp
 */
public void caseAStrongNameCastExp(AStrongNameCastExp node) {
	if (node.getName().toString().indexOf("..") >= 0) {
		exceptions.add(new WeedException("Invalid type name in cast expression"));
	} else {
		node.getUnaryExp().apply(this);
		exp = new AStrongCastExp(node.getLParen(), node.getName(), node.getDim(),
				node.getRParen(), exp);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongObjectFieldAccess
 */
public void caseAStrongObjectFieldAccess(AStrongObjectFieldAccess node) {
	node.getPrimaryExp().apply(this);
	exp = new ANavigationExp(exp, new AStrongObjectNavigation(node.getDot(), node.getId()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AStrongObjectNavigation
 */
public void caseAStrongObjectNavigation(AStrongObjectNavigation node) {
	exp = new ANavigationExp(exp, node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongPrimitiveCastExp
 */
public void caseAStrongPrimitiveCastExp(AStrongPrimitiveCastExp node) {
	node.getUnaryExp().apply(this);
	PName name = new ASimpleName(new TId(node.getPrimitiveType().toString().trim()));
	exp = new AStrongCastExp(node.getLParen(), name, node.getDim(), node.getRParen(), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AThisPrimaryExp
 */
public void caseAThisPrimaryExp(AThisPrimaryExp node) {
	exp = new AThisExp(node.getThis());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.predicate.node.AThreadPrimaryExp
 */
public void caseAThreadPrimaryExp(AThreadPrimaryExp node) {
	exp = new AThreadExp(node.getThread());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ATimesMultExp
 */
public void caseATimesMultExp(ATimesMultExp node) {
	node.getMultExp().apply(this);
	PExp left = exp;
	node.getUnaryExp().apply(this);
	exp = new ABinaryExp(left, new ATimesBinOp(node.getStar()), exp);
}
/**
 * @param node edu.ksu.cis.bandera.bpdl.node.AUnsignedRightShiftExp
 */
public void caseAUnsignedRightShiftExp(AUnsignedRightShiftExp node) {
	node.getShiftExp().apply(this);
	PExp left = exp;
	node.getAddExp().apply(this);
	exp = new ABinaryExp(left, new AUnsignedShiftRightBinOp(node.getUnsignedShiftRight()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AXorExclusiveOrExp
 */
public void caseAXorExclusiveOrExp(AXorExclusiveOrExp node) {
	node.getExclusiveOrExp().apply(this);
	PExp left = exp;
	node.getBitAndExp().apply(this);
	exp = new ABinaryExp(left, new ABitXorBinOp(node.getBitXor()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AWeakArrayNavigation
public void caseAWeakArrayNavigation(AWeakArrayNavigation node) {
	PExp nav = exp;
	node.getExp().apply(this);
	node.setExp(exp);
	exp = new ANavigationExp(exp, node);
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AWeakDivMultExp
public void caseAWeakDivMultExp(AWeakDivMultExp node) {
	node.getMultExp().apply(this);
	PExp left = exp;
	node.getUnaryExp().apply(this);
	exp = new ABinaryExp(left, new AWeakDivBinOp(node.getWeakDiv()), exp);
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AWeakExpArrayAccess
public void caseAWeakExpArrayAccess(AWeakExpArrayAccess node) {
	node.getPrimaryExp().apply(this);
	PExp baseExp = exp;
	node.getExp().apply(this);
	exp = new ANavigationExp(baseExp, new AWeakArrayNavigation(node.getLWeakArrayReference(),
			exp, node.getRWeakArrayReference()));
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AWeakModMultExp
public void caseAWeakModMultExp(AWeakModMultExp node) {
	node.getMultExp().apply(this);
	PExp left = exp;
	node.getUnaryExp().apply(this);
	exp = new ABinaryExp(left, new AWeakModBinOp(node.getWeakMod()), exp);
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AWeakNameArrayAccess
public void caseAWeakNameArrayAccess(AWeakNameArrayAccess node) {
	String nameExp = node.getName().toString().trim();
	int i = nameExp.indexOf("..");
	if (i >= 0) {
		String navigationExp = nameExp.substring(i);
		nameExp = nameExp.substring(0, i);
		exp = buildNavigation(buildName(nameExp), navigationExp);
	} else {
		exp = new ANameExp(node.getName());
	}
	PExp baseExp = exp;
	node.getExp().apply(this);
	exp = new ANavigationExp(baseExp, new AWeakArrayNavigation(node.getLWeakArrayReference(),
			exp, node.getRWeakArrayReference()));
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AWeakNameCastExp
public void caseAWeakNameCastExp(AWeakNameCastExp node) {
	if (node.getName().toString().indexOf("..") >= 0) {
		int line = node.getLWeakCastParen().getLine();
		int pos = node.getLWeakCastParen().getPos();
		exceptions.add(new WeedException("[" + line + ", " + pos + "] invalid type name"));
	} else {
		node.getUnaryExp().apply(this);
		exp = new AWeakCastExp(node.getLWeakCastParen(), node.getName(), node.getDim(),
				node.getRWeakCastParen(), exp);
	}
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AWeakObjectFieldAccess
public void caseAWeakObjectFieldAccess(AWeakObjectFieldAccess node) {
	node.getPrimaryExp().apply(this);
	exp = new ANavigationExp(exp, new AWeakObjectNavigation(node.getWeakObjectReference(),
			node.getId()));
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AWeakObjectNavigation
public void caseAWeakObjectNavigation(AWeakObjectNavigation node) {
	exp = new ANavigationExp(exp, node);
}
 */
/**
 * 
 * @return java.util.Vector
 */
public static Vector getExceptions() {
	return simplifier.exceptions;
}
/**
 * 
 */
public static void reset() {
	simplifier.exceptions = new Vector();
}
/**
 * 
 * @return edu.ksu.cis.bandera.bpdl.node.Node
 * @param node edu.ksu.cis.bandera.bpdl.node.Node
 */
public static Node simplify(Node node) {
	node.apply(simplifier);
	return node;
}
}
