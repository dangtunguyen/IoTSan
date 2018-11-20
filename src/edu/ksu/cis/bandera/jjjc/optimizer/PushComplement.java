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
import edu.ksu.cis.bandera.jjjc.lexer.*;
import edu.ksu.cis.bandera.jjjc.parser.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.*;

import ca.mcgill.sable.laleh.java.astfix.*;

public final class PushComplement extends DepthFirstAdapter {
	private boolean foundComplement;
	private static PushComplement p = new PushComplement();
/**
 * 
 */
private PushComplement() {
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AArrayAccessExp
 */
public void caseAArrayAccessExp(AArrayAccessExp node) {
	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AAssignmentExp
 */
public void caseAAssignmentExp(AAssignmentExp node) {
	/*
	boolean prevFoundComplement = foundComplement;
	foundComplement = false;
	*/
	node.getExp().apply(this);		
	/*
	foundComplement = prevFoundComplement;
	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
	*/
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	PBinaryOperator op = node.getBinaryOperator();
	boolean prevFoundComplement = foundComplement;
	if (foundComplement) {
		if (op instanceof AOrBinaryOperator) {
			node.setBinaryOperator(new AAndBinaryOperator(new TAnd()));
		} else if (op instanceof AAndBinaryOperator) {
			node.setBinaryOperator(new AOrBinaryOperator(new TOr()));
		} else if (op instanceof AGtBinaryOperator) {
			node.setBinaryOperator(new ALteqBinaryOperator(new TLteq()));
			foundComplement = false;
		} else if (op instanceof ALtBinaryOperator) {
			node.setBinaryOperator(new AGteqBinaryOperator(new TGteq()));
			foundComplement = false;
		} else if (op instanceof ALteqBinaryOperator) {
			node.setBinaryOperator(new AGtBinaryOperator(new TGt()));
			foundComplement = false;
		} else if (op instanceof AGteqBinaryOperator) {
			node.setBinaryOperator(new ALtBinaryOperator(new TLt()));
			foundComplement = false;
		} else if (op instanceof AEqBinaryOperator) {
			node.setBinaryOperator(new ANeqBinaryOperator(new TNeq()));
			foundComplement = false;
		} else if (op instanceof ANeqBinaryOperator) {
			node.setBinaryOperator(new AEqBinaryOperator(new TEq()));
			foundComplement = false;
		}
	}
	node.getFirst().apply(this);
	node.getSecond().apply(this);
	foundComplement = prevFoundComplement;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFalseBooleanLiteral
 */
public void caseAFalseBooleanLiteral(AFalseBooleanLiteral node) {
	if (foundComplement)
		node.replaceBy(new ATrueBooleanLiteral(new TTrue()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFieldAccessExp
 */
public void caseAFieldAccessExp(AFieldAccessExp node) {
	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInstanceofExp
 */
public void caseAInstanceofExp(AInstanceofExp node) {
	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameExp
 */
public void caseANameExp(ANameExp node) {
	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameMethodInvocationExp
 */
public void caseANameMethodInvocationExp(ANameMethodInvocationExp node) {
	boolean prevFoundComplement = foundComplement;
	foundComplement = false;
		
	{
		Object temp[] = node.getArgumentList().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PExp) temp[i]).apply(this);
		}
	}

	foundComplement = prevFoundComplement;

	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryMethodInvocationExp
 */
public void caseAPrimaryMethodInvocationExp(APrimaryMethodInvocationExp node) {
	boolean prevFoundComplement = foundComplement;
	foundComplement = false;
		
	{
		Object temp[] = node.getArgumentList().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PExp) temp[i]).apply(this);
		}
	}

	foundComplement = prevFoundComplement;

	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQuestionExp
 */
public void caseAQuestionExp(AQuestionExp node) {
	boolean prevFoundComplement = foundComplement;
	
	foundComplement = false;
	node.getSecond().apply(this);

	foundComplement = false;
	node.getThird().apply(this);
		
	foundComplement = prevFoundComplement;
	node.getFirst().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperMethodInvocationExp
 */
public void caseASuperMethodInvocationExp(ASuperMethodInvocationExp node) {
	boolean prevFoundComplement = foundComplement;
	foundComplement = false;
		
	{
		Object temp[] = node.getArgumentList().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PExp) temp[i]).apply(this);
		}
	}

	foundComplement = prevFoundComplement;

	if (foundComplement) {
		node.replaceBy(new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.clone()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATrueBooleanLiteral
 */
public void caseATrueBooleanLiteral(ATrueBooleanLiteral node) {
	if (foundComplement)
		node.replaceBy(new AFalseBooleanLiteral(new TFalse()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AUnaryExp
 */
public void caseAUnaryExp(AUnaryExp node) {
	PExp exp = node.getExp();
	if ("!".equals(node.getUnaryOperator().toString().trim())) {
		foundComplement = !foundComplement;
		node.replaceBy(exp);
		exp.apply(this);
		foundComplement = !foundComplement;
	} else {
		exp.apply(this);
	}
}
/**
 * 
 */
private void init() {
	foundComplement = false;
}
/**
 * 
 * @param exp edu.ksu.cis.bandera.jjjc.node.Node
 */
public static void push(Node exp) {
	p.init();
	exp.apply(p);
}
}
