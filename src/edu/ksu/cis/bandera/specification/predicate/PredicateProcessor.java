package edu.ksu.cis.bandera.specification.predicate;

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
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.jjjc.util.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;
import edu.ksu.cis.bandera.specification.predicate.node.*;
import java.util.*;
public class PredicateProcessor extends DepthFirstAdapter {
	private static Grimp grimp = Grimp.v();
	private static Jimple jimple = Jimple.v();
	private static Value ph;
	private static Hashtable phParams;
	private Value currentValue;
	private Vector constraintValues = new Vector();
	private static Hashtable variablesUsed;
	private static SootMethod sm;
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	node.getLeft().apply(this);
	Value leftValue = currentValue;
	node.getRight().apply(this);
	
	PBinOp op = node.getBinOp();
	if (op instanceof AEqualBinOp) {
		currentValue = grimp.newEqExpr(leftValue, currentValue);
	} else if (op instanceof ANotEqualBinOp) {
		currentValue = grimp.newNeExpr(leftValue, currentValue);
	} else if (op instanceof ALessBinOp) {
		currentValue = grimp.newLtExpr(leftValue, currentValue);
	} else if (op instanceof ALessEqualBinOp) {
		currentValue = grimp.newLeExpr(leftValue, currentValue);
	} else if (op instanceof AGreaterBinOp) {
		currentValue = grimp.newGtExpr(leftValue, currentValue);
	} else if (op instanceof AGreaterEqualBinOp) {
		currentValue = grimp.newGeExpr(leftValue, currentValue);
	} else if (op instanceof AAndBinOp) {
		currentValue = new LogicalAndExpr(leftValue, currentValue);
	} else if (op instanceof AOrBinOp) {
		currentValue = new LogicalOrExpr(leftValue, currentValue);
	} else if (op instanceof APlusBinOp) {
		currentValue = grimp.newAddExpr(leftValue, currentValue);
	} else if (op instanceof AMinusBinOp) {
		currentValue = grimp.newSubExpr(leftValue, currentValue);
	} else if (op instanceof ATimesBinOp) {
		currentValue = grimp.newMulExpr(leftValue, currentValue);
	} else if (op instanceof AShiftLeftBinOp) {
		currentValue = grimp.newShlExpr(leftValue, currentValue);
	} else if (op instanceof ASignedShiftRightBinOp) {
		currentValue = grimp.newShrExpr(leftValue, currentValue);
	} else if (op instanceof AUnsignedShiftRightBinOp) {
		currentValue = grimp.newUshrExpr(leftValue, currentValue);
	} else if (op instanceof ABitAndBinOp) {
		currentValue = grimp.newAndExpr(leftValue, currentValue);
	} else if (op instanceof ABitOrBinOp) {
		currentValue = grimp.newOrExpr(leftValue, currentValue);
	} else if (op instanceof ABitXorBinOp) {
		currentValue = grimp.newXorExpr(leftValue, currentValue);
	} else if (op instanceof AStrongDivBinOp) {
		constraintValues.add(grimp.newNeExpr(currentValue, IntConstant.v(0)));
		currentValue = grimp.newDivExpr(leftValue, currentValue);
	} else if (op instanceof AStrongModBinOp) {
		constraintValues.add(grimp.newNeExpr(currentValue, IntConstant.v(0)));
		currentValue = grimp.newRemExpr(leftValue, currentValue);
	} 
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ABitComplementExp
 */
public void caseABitComplementExp(ABitComplementExp node) {
	node.getExp().apply(this);
	if (currentValue.getType() instanceof ca.mcgill.sable.soot.LongType) {
		currentValue = grimp.newXorExpr(currentValue, LongConstant.v(-1));
	} else {
		currentValue = grimp.newXorExpr(currentValue, IntConstant.v(-1));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ACharLiteral
 */
public void caseACharLiteral(ACharLiteral node) {
	String literal = node.toString().trim();
	literal = literal.substring(1, literal.length() - 1);
	int value = 0;

	if (literal.startsWith("\\")) {
		switch (literal.charAt(1)) {
			case 'b': value = '\b';
				break;
			case 't': value = '\t';
				break;
			case 'n': value = '\n';
				break;
			case 'f': value = '\f';
				break;
			case 'r': value = '\r';
				break;
			case '\"': value = '\"';
				break;
			case '\'': value = '\'';
				break;
			case '\\': value = '\\';
				break;
			default: value = Integer.valueOf(literal.substring(1), 8).intValue();
		}
	} else if (literal.startsWith("\\u")) {
		value = Integer.valueOf(literal.substring(2), 16).intValue();
	} else value = literal.charAt(0);
	
	currentValue = IntConstant.v(value);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AComplementExp
 */
public void caseAComplementExp(AComplementExp node) {
	node.getExp().apply(this);
	currentValue = new ComplementExpr(currentValue);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ADecIntLiteral
 */
public void caseADecIntLiteral(ADecIntLiteral node) {
	currentValue = IntConstant.v(Integer.parseInt(node.toString().trim()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ADecLongLiteral
 */
public void caseADecLongLiteral(ADecLongLiteral node) {
	String literal = node.toString().trim();
	literal = literal.substring(0, literal.length() - 1);
	currentValue = LongConstant.v(Long.parseLong(literal));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ADoubleLiteral
 */
public void caseADoubleLiteral(ADoubleLiteral node) {
	currentValue = DoubleConstant.v(Double.valueOf(node.toString().trim()).floatValue());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AFalseLiteral
 */
public void caseAFalseLiteral(AFalseLiteral node) {
	currentValue = IntConstant.v(0);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AFloatLiteral
 */
public void caseAFloatLiteral(AFloatLiteral node) {
	currentValue = FloatConstant.v(Float.valueOf(node.toString().trim()).floatValue());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AHexIntLiteral
 */
public void caseAHexIntLiteral(AHexIntLiteral node) {
	currentValue = IntConstant.v(Integer.parseInt(node.toString().trim(), 16));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AHexLongLiteral
 */
public void caseAHexLongLiteral(AHexLongLiteral node) {
	String literal = node.toString().substring(2).trim();
	literal = literal.substring(0, literal.length() - 1);
	currentValue = LongConstant.v(Long.parseLong(literal, 16));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AInstanceofExp
 */
public void caseAInstanceofExp(AInstanceofExp node) {
	node.getExp().apply(this);
	currentValue = grimp.newInstanceOfExpr(currentValue, RefType.v(new Name(node.toString()).toString()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ANameExp
 */
public void caseANameExp(ANameExp node) {
	Object o = variablesUsed.get(node);
	if (o instanceof String) {
		currentValue = (Value) phParams.get(o);
	} else if (o instanceof Local) {
		currentValue = new LocalExpr(sm, (Local) o);
	} else if (o instanceof Field) {
		Field f = (Field) variablesUsed.get(node);
		try {
			SootClass sc = CompilationManager.getSootClassManager().getClass(f.getDeclaringClassOrInterface().toString());
			SootField sf = sc.getField(f.getName().toString());
			if (Modifier.isStatic(sf.getModifiers()))
				currentValue = grimp.newStaticFieldRef(sf);
			else 
				currentValue = grimp.newInstanceFieldRef(ph, sf);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error: " + e);
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ANegativeExp
 */
public void caseANegativeExp(ANegativeExp node) {
	node.getExp().apply(this);
	currentValue = grimp.newNegExpr(currentValue);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ANullLiteral
 */
public void caseANullLiteral(ANullLiteral node) {
	currentValue = NullConstant.v();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AOctIntLiteral
 */
public void caseAOctIntLiteral(AOctIntLiteral node) {
	currentValue = IntConstant.v(Integer.parseInt(node.toString().trim(), 8));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AOctLongLiteral
 */
public void caseAOctLongLiteral(AOctLongLiteral node) {
	String literal = node.toString().substring(1).trim();
	literal = literal.substring(0, literal.length() - 1);
	currentValue = LongConstant.v(Long.parseLong(literal, 8));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AQuestionExp
 */
public void caseAQuestionExp(AQuestionExp node) {
	node.getExp().apply(this);
	Value conditionValue = currentValue;
	node.getTrueExp().apply(this);
	Value trueValue = currentValue;
	node.getFalseExp().apply(this);
	currentValue = new LogicalOrExpr(new LogicalAndExpr(conditionValue, trueValue),
			new LogicalAndExpr(new ComplementExpr(conditionValue), currentValue)); 
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AReturnValueExp
 */
public void caseAReturnValueExp(AReturnValueExp node) {
	currentValue = new LocalExpr(sm, ((JimpleBody) sm.getBody(Jimple.v())).getLocal("$ret"));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStringLiteral
 */
public void caseAStringLiteral(AStringLiteral node) {
	currentValue = StringConstant.v(Util.decodeString(node.toString().trim()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongArrayNavigation
 */
public void caseAStrongArrayNavigation(AStrongArrayNavigation node) {
	Value baseValue = currentValue;
	node.getExp().apply(this);

	constraintValues.add(grimp.newNeExpr(baseValue, NullConstant.v()));
	constraintValues.add(grimp.newGeExpr(currentValue, IntConstant.v(0)));
	constraintValues.add(grimp.newLtExpr(currentValue, grimp.newLengthExpr(baseValue)));

	currentValue = grimp.newArrayRef(baseValue, currentValue);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongCastExp
 */
public void caseAStrongCastExp(AStrongCastExp node) {
	ca.mcgill.sable.soot.Type type = Util.convertType((edu.ksu.cis.bandera.jjjc.symboltable.Type) variablesUsed.get(node));
	node.getExp().apply(this);

	constraintValues.add(grimp.newInstanceOfExpr(currentValue, type));
	
	currentValue = grimp.newCastExpr(currentValue, type);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongObjectNavigation
 */
public void caseAStrongObjectNavigation(AStrongObjectNavigation node) {
	constraintValues.add(grimp.newNeExpr(currentValue, NullConstant.v()));
	Hashtable vu = variablesUsed;
	Field f = (Field) vu.get(node);
	try {
		SootClass sc = CompilationManager.getSootClassManager().getClass(f.getDeclaringClassOrInterface().toString());
		SootField sf = sc.getField(f.getName().toString());
		if (Modifier.isStatic(sf.getModifiers()))
			currentValue = grimp.newStaticFieldRef(sf);
		else 
			currentValue = grimp.newInstanceFieldRef(currentValue, sf);
	} catch (Exception e) {
		throw new RuntimeException("Unexpected error: " + e);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AThisExp
 */
public void caseAThisExp(AThisExp node) {
	currentValue = ph;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.predicate.node.AThreadExp
 */
public void caseAThreadExp(AThreadExp node) {
	currentValue = ThreadExpr.v();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ATrueLiteral
 */
public void caseATrueLiteral(ATrueLiteral node) {
	currentValue = IntConstant.v(1);
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 * @param placeHolder ca.mcgill.sable.soot.jimple.Value
 * @param placeHolderParams java.util.Hashtable
 * @param p edu.ksu.cis.bandera.predicate.datastructure.Predicate
 */
public static Value process(Value placeHolder, Hashtable placeHolderParams, Predicate p) {
	ph = placeHolder;
	phParams = placeHolderParams;
	variablesUsed = p.getVariablesUsed();
	Value constraintValue = null;
	if (p instanceof ExpressionPredicate) {
		if (p.getAnnotation() != null) {
			Annotation a = p.getAnnotation();
			if (a instanceof LabeledStmtAnnotation) {
				a = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
			}
			sm = ((MethodDeclarationAnnotation) a).getSootMethod();
		}
		if (p.getConstraint() != null) {
			PredicateProcessor pp = new PredicateProcessor();
			p.getConstraint().apply(pp);
			constraintValue = pp.currentValue;
			Object[] constraints = pp.constraintValues.toArray();
			for (int i = constraints.length - 1; i >= 0; i--) {
				constraintValue = new LogicalAndExpr((Value) constraints[i], constraintValue);
			}
		}
		return constraintValue;
	}
	Value vv = null;
	for (Iterator j = p.getMethods().iterator(); j.hasNext();) {
		sm = (SootMethod) j.next();
		Vector v = (Vector) p.getLocations().get(sm);
		constraintValue = null;
		if (p.getConstraint() != null) {
			PredicateProcessor pp = new PredicateProcessor();
			p.getConstraint().apply(pp);
			constraintValue = pp.currentValue;
			Object[] constraints = pp.constraintValues.toArray();
			for (int i = constraints.length - 1; i >= 0; i--) {
				constraintValue = new LogicalAndExpr((Value) constraints[i], constraintValue);
			}
			Value result = constraintValue;
			if (ph != null) {
				Value temp = grimp.newEqExpr(ph, new LocalExpr(sm, ((JimpleBody) sm.getBody(jimple)).getLocal("JJJCTEMP$0")));
				result = new LogicalAndExpr(result, temp);
			}
			constraintValue = new LogicalAndExpr(new LocationTestExpr(v), result);
			CompilationManager.addLocPredicatePair(constraintValue);
		} else {
			Value result = new LocationTestExpr(v);
			if (ph != null) {
				Value temp = grimp.newEqExpr(ph, new LocalExpr(sm, ((JimpleBody) sm.getBody(jimple)).getLocal("JJJCTEMP$0")));
				result = new LogicalAndExpr(result, temp);
				CompilationManager.addLocPredicatePair(result);
			}
			constraintValue = result;
		}
		if (vv == null) vv = constraintValue;
		else vv = new LogicalOrExpr(vv, constraintValue);
	}
	return vv;
}
}
