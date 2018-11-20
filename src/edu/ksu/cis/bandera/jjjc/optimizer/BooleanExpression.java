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
import java.util.*;

import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.codegenerator.*;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

public final class BooleanExpression extends DepthFirstAdapter {
	private JIJCCodeGenerator codeGenerator;
	private boolean foundComplement = false;
	private Vector currentStmts;
	private Jimple jimple = Jimple.v();
	private Value currentValue;
	private Stmt trueBranch;
	private Stmt falseBranch;
	private boolean switchedBranch = false;
/**
 * 
 * @param codeGenerator edu.ksu.cis.bandera.jjjc.codegenerator.JIJCCodeGenerator
 */
public BooleanExpression(JIJCCodeGenerator codeGenerator, Stmt trueBranch, Stmt falseBranch) {
	this.codeGenerator = codeGenerator;
	this.trueBranch = trueBranch;
	this.falseBranch = falseBranch;
	currentStmts = codeGenerator.getCurrentStmts();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AArrayAccessExp
 */
public void caseAArrayAccessExp(AArrayAccessExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AAssignmentExp
 */
public void caseAAssignmentExp(AAssignmentExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	PBinaryOperator op = node.getBinaryOperator();
	if (op instanceof AOrBinaryOperator) {
		Stmt prevFalseBranch = falseBranch;
		falseBranch = jimple.newNopStmt();
		node.getFirst().apply(this);
		currentStmts.add(falseBranch);
		falseBranch = prevFalseBranch;
		node.getSecond().apply(this);
	} else
		if (op instanceof AAndBinaryOperator) {
			Stmt lastStmt = jimple.newNopStmt();
			node.getFirst().replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.getFirst().clone()));
			PushComplement.push(node.getFirst());
			node.getFirst().apply(new BooleanExpression(codeGenerator, falseBranch, lastStmt));
			currentStmts.addElement(lastStmt);
			node.getSecond().apply(this);
		} else {
			Stmt prevTrueBranch = codeGenerator.trueBranch;
			Stmt prevFalseBranch = codeGenerator.falseBranch;
			codeGenerator.trueBranch = trueBranch;
			codeGenerator.falseBranch = falseBranch;
			node.apply(codeGenerator);
			codeGenerator.trueBranch = prevTrueBranch;
			codeGenerator.falseBranch = prevFalseBranch;
		}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFalseBooleanLiteral
 */
public void caseAFalseBooleanLiteral(AFalseBooleanLiteral node) {
	currentStmts.addElement(jimple.newGotoStmt(falseBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFieldAccessExp
 */
public void caseAFieldAccessExp(AFieldAccessExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInstanceofExp
 */
public void caseAInstanceofExp(AInstanceofExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	Value currentValue = codeGenerator.getCurrentValue();
	
	if (foundComplement) {
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	} else {
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameExp
 */
public void caseANameExp(ANameExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameMethodInvocationExp
 */
public void caseANameMethodInvocationExp(ANameMethodInvocationExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryMethodInvocationExp
 */
public void caseAPrimaryMethodInvocationExp(APrimaryMethodInvocationExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQuestionExp
 */
public void caseAQuestionExp(AQuestionExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperMethodInvocationExp
 */
public void caseASuperMethodInvocationExp(ASuperMethodInvocationExp node) {
	Stmt prevTrueBranch = codeGenerator.trueBranch;
	Stmt prevFalseBranch = codeGenerator.falseBranch;
	codeGenerator.trueBranch = trueBranch;
	codeGenerator.falseBranch = falseBranch;
	node.apply(codeGenerator);
	codeGenerator.trueBranch = prevTrueBranch;
	codeGenerator.falseBranch = prevFalseBranch;
	currentValue = codeGenerator.getCurrentValue();
	if (foundComplement)
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(currentValue, IntConstant.v(0)), trueBranch));
	else
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(currentValue, IntConstant.v(0)), trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATrueBooleanLiteral
 */
public void caseATrueBooleanLiteral(ATrueBooleanLiteral node) {
	currentStmts.addElement(jimple.newGotoStmt(trueBranch));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AUnaryExp
 */
public void caseAUnaryExp(AUnaryExp node) {
	PExp exp = node.getExp();
	if ("!".equals(node.getUnaryOperator().toString().trim())) {
		foundComplement = !foundComplement;
		exp.apply(this);
		foundComplement = !foundComplement;
	} else {
		Stmt prevTrueBranch = codeGenerator.trueBranch;
		Stmt prevFalseBranch = codeGenerator.falseBranch;
		codeGenerator.trueBranch = trueBranch;
		codeGenerator.falseBranch = falseBranch;
		exp.apply(codeGenerator);
		codeGenerator.trueBranch = prevTrueBranch;
		codeGenerator.falseBranch = prevFalseBranch;
	}
}
}
