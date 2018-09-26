package edu.ksu.cis.bandera.specification.ast;

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
import edu.ksu.cis.bandera.specification.analysis.*;
import edu.ksu.cis.bandera.specification.node.*;
import java.util.*;
public class Simplifier extends DepthFirstAdapter {
	PExp exp;
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AAndAndExp
 */
public void caseAAndAndExp(AAndAndExp node) {
	node.getAndExp().apply(this);
	PExp imp = exp;
	node.getUnaryExp().apply(this);
	exp = new ABinaryExp(imp, new AAndBinOp(new TAnd()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AComplementUnaryExp
 */
public void caseAComplementUnaryExp(AComplementUnaryExp node) {
	node.getPrimaryExp().apply(this);
	exp = new AComplementExp(node.getNot(), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AImplicationExp
 */
public void caseAImplicationExp(AImplicationExp node) {
	node.getImplicationExp().apply(this);
	node.replaceBy(exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AImplyImplicationExp
 */
public void caseAImplyImplicationExp(AImplyImplicationExp node) {
	node.getImplicationExp().apply(this);
	PExp imp = exp;
	node.getOrExp().apply(this);
	exp = new ABinaryExp(imp, new AImplyBinOp(new TImply()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AOrOrExp
 */
public void caseAOrOrExp(AOrOrExp node) {
	node.getOrExp().apply(this);
	PExp imp = exp;
	node.getAndExp().apply(this);
	exp = new ABinaryExp(imp, new AOrBinOp(new TOr()), exp);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AParenPrimaryExp
 */
public void caseAParenPrimaryExp(AParenPrimaryExp node) {
	node.getExp().apply(this);
	exp = new AParenExp(node.getLParen(), exp, node.getRParen());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.APredicatePrimaryExp
 */
public void caseAPredicatePrimaryExp(APredicatePrimaryExp node) {
	exp = new APredicateExp(node.getName(), node.getLParen(), node.getArgs(), node.getRParen());
}
}
