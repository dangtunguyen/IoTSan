package edu.ksu.cis.bandera.specification;

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
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.exception.*;
import edu.ksu.cis.bandera.specification.node.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.jjjc.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.grimp.*;
import java.util.*;

public class Compiler extends DepthFirstAdapter {
	private static Grimp grimp = Grimp.v();
	private Hashtable predNodeGrimpTable;
	private HashSet placeHolders;
	private Pattern pattern;

	private Value currentValue;
	private static final String defaultPrefix = "pred__";
	private static final String defaultParam = defaultPrefix+"a";  // robbyjo's patch

	private Hashtable parameterTable = new Hashtable();
	private Hashtable parameterNameTable = new Hashtable();
	private AnnotationManager am = CompilationManager.getAnnotationManager();
	private Iterator parametersIterator;
/**
 * 
 * @param predNodeGrimpTable java.util.Hashtable
 * @param placeHolders java.util.HashSet
 * @param pattern edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern
 * @param node edu.ksu.cis.bandera.specification.node.Node
 */
public Compiler(Hashtable predNodeGrimpTable, HashSet placeHolders, Pattern pattern, Node node) {
	this.predNodeGrimpTable = predNodeGrimpTable;
	this.placeHolders = placeHolders;
	this.pattern = pattern;
	parametersIterator = pattern.getParametersOccurenceOrder().iterator();
	node.apply(this);
	buildFormulaConditions();
}
/**
 * 
 */
private void buildFormulaConditions() {
	Value v = null;
	QuantifiedVariable qv;
	for (Iterator i = placeHolders.iterator(); i.hasNext();) {
		StaticFieldRef sfr = (StaticFieldRef) i.next();
		SootField sf = sfr.getField();
		String fieldName = sf.getName();
		String quantifiedVariableName = fieldName.substring(15);
		qv = CompilationManager.getQuantifier(quantifiedVariableName);
		Value constraint = grimp.newNeExpr(sfr, NullConstant.v());
		if (qv.getConstraint() != null) {
			constraint = new LogicalAndExpr(constraint, QuantifiedVariableFixer.fix(qv));
		}
		if (v == null) {
			v = constraint;
		} else {
			v = new LogicalAndExpr(v, constraint);
		}
	}
	if (v != null) {
		parameterTable.put(defaultParam, v);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	node.getLeft().apply(this);
	Value leftValue = currentValue;
	node.getRight().apply(this);
	Value rightValue = currentValue;

	PBinOp binOp = node.getBinOp();
	if (binOp instanceof AAndBinOp) {
		currentValue = new LogicalAndExpr(leftValue, rightValue);
	} else if (binOp instanceof AOrBinOp) {
		currentValue = new LogicalOrExpr(leftValue, rightValue);
	} else {
		currentValue = new LogicalOrExpr(new ComplementExpr(leftValue), rightValue);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AComplementExp
 */
public void caseAComplementExp(AComplementExp node) {
	node.getExp().apply(this);
	currentValue = new ComplementExpr(currentValue);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AExpWord
 */
public void caseAExpWord(AExpWord node) {
	node.getExp().apply(this);
	String id = (String) parametersIterator.next();
	//		String id2 = "pred__" + id.toLowerCase();
	String id2 = defaultPrefix + id.toLowerCase(); // robbyjo's patch
	parameterNameTable.put(id, id2);
	parameterTable.put(id2, currentValue);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.APredicateExp
 */
public void caseAPredicateExp(APredicateExp node) {
	currentValue = (Value) predNodeGrimpTable.get(node);
}
/**
 * 
 * @return java.lang.String
 * @param mapping java.lang.String
 */
public String getFormula(String mapping) throws SpecificationException {
	if ("LTL".equalsIgnoreCase(mapping)) {
		try {
		  // (!available U (available && P)) || [] (!available)
//			String ltl = "((pred__a) && (" + pattern.expandMapping("ltl", parameterNameTable) + "))";
//			String result = "((!(pred__a)) U " + ltl + ") || ([](!(pred__a)))";

			// robbyjo's patch
			String ltl = "(("+defaultParam+") && (" + pattern.expandMapping("ltl", parameterNameTable) + "))";
			String result = "((!("+defaultParam+")) U " + ltl + ") || ([](!("+defaultParam+")))";
			return result;
		} catch (Exception e) {
			throw new SpecificationException(e.getMessage());
		}
	} else {
		throw new SpecificationException("Mapping to " + mapping + " is currently not supported");
	}
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getParameterTable() throws SpecificationException {
	return parameterTable;
}
}
