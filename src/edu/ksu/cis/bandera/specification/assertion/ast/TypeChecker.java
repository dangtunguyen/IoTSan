package edu.ksu.cis.bandera.specification.assertion.ast;

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
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.specification.assertion.exception.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.util.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import java.util.*;
public class TypeChecker extends DepthFirstAdapter {
	private SymbolTable symbolTable;
	private Vector usedTypeNames = new Vector();
	private Vector exceptions = new Vector();
	private ClassOrInterfaceType type;
	private Type currentType;
	private LabeledStmtAnnotation currentAnnotation;
	private Annotation annotation;
	boolean isStatic;
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AWeakArrayNavigation
public void caseAWeakArrayNavigation(AWeakArrayNavigation node) {
	if (currentType instanceof ArrayType) {
		currentType = new ArrayType(((ArrayType) currentType).baseType,
				((ArrayType) currentType).nDimensions - 1);
	} else {
		int line = node.getLWeakArrayReference().getLine();
		int pos = node.getLWeakArrayReference().getPos();
		exceptions.add(new TypeException("[" + line + ", " + pos + "] not an array in array navigation"));
		currentType = VoidType.TYPE;
	}
	expTable.put(node, currentType);
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AWeakCastExp
public void caseAWeakCastExp(AWeakCastExp node) {
	currentType = VoidType.TYPE;
	Type cast;
	try {
		cast = symbolTable.resolveType(new Name(node.getType().toString()));
	} catch (Exception e) {
		int line = node.getLWeakCastParen().getLine();
		int pos = node.getLWeakCastParen().getPos();
		exceptions.add(new TypeException("[" + line + ", " + pos + "] invalid casting type"));
		expTable.put(node, currentType);
		return;
	}
	int i;
	if ((i = node.getDim().toArray().length) > 0) {
		cast = new ArrayType(cast, i);
	}
	node.getExp().apply(this);
	if (!currentType.isValidCastingConversion(cast)) {
		int line = node.getLWeakCastParen().getLine();
		int pos = node.getLWeakCastParen().getPos();
		exceptions.add(new TypeException("[" + line + ", " + pos + "] invalid casting type"));
	}
	currentType = cast;
	expTable.put(node, currentType);
}
 */
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AWeakObjectNavigation
public void caseAWeakObjectNavigation(AWeakObjectNavigation node) {
	String id = node.getId().toString().trim();
	if ("length".equals(id) && (currentType instanceof ArrayType)) {
		currentType = IntType.TYPE;
	} else if (currentType instanceof ClassOrInterfaceType) {
		try {
			currentType = ((ClassOrInterfaceType) currentType).getField(new Name(id)).getType();
		} catch (Exception e) {
			int line = node.getWeakObjectReference().getLine();
			int pos = node.getWeakObjectReference().getPos();
			exceptions.add(new TypeException("[" + line + ", " + pos + "] field undefined"));
			currentType = VoidType.TYPE;
		}
	} else {
		int line = node.getWeakObjectReference().getLine();
		int pos = node.getWeakObjectReference().getPos();
		exceptions.add(new TypeException("[" + line + ", " + pos + "] not an object in object navigation"));
		currentType = VoidType.TYPE;
	}
	expTable.put(node, currentType);
}
 */
/**
 * 
 * @param symbolTable edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 * @param isStatic boolean
 */
public TypeChecker(SymbolTable symbolTable, ClassOrInterfaceType type, Annotation annotation, boolean isStatic) {
	this.symbolTable = symbolTable;
	symbolTable.setCurrentClassOrInterfaceType(type);
	this.type = type;
	if (annotation instanceof LabeledStmtAnnotation) {
		currentAnnotation = (LabeledStmtAnnotation) annotation;
		this.annotation = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(annotation);
	} else {
		this.annotation = annotation;
	}
	symbolTable.setCurrentClassOrInterfaceType(null);
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 * @param name edu.ksu.cis.bandera.predicate.node.AQualifiedName
 */
private PExp buildFieldAccess(AQualifiedName name) {
	PExp exp;
	if (name.getName() instanceof AQualifiedName) {
		exp = buildFieldAccess((AQualifiedName) name.getName());
	} else {
		exp = buildFieldAccess((ASimpleName) name.getName());
	}
	
	return new AFieldAccessExp(new APrimaryFieldAccess(exp, new TDot(), name.getId()));
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 * @param name edu.ksu.cis.bandera.predicate.node.ASimpleName
 */
private PExp buildFieldAccess(ASimpleName name) {
	return new ANameExp(name);
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 * @param typeExp edu.ksu.cis.bandera.predicate.node.PExp
 * @param name java.lang.String
 */
private PExp buildFieldAccess(PExp typeExp, String name) {
	if ("".equals(name.trim())) return typeExp;
	for (StringTokenizer tokenizer = new StringTokenizer(name, "."); tokenizer.hasMoreTokens();) {
		String token = (String) tokenizer.nextToken();
		if (currentType instanceof ClassOrInterfaceType) {
			ClassOrInterfaceType type = (ClassOrInterfaceType) currentType;
			try {
				currentType = type.getField(new Name(token)).getType();
			} catch (Exception e) {
				exceptions.add(e);
				currentType = VoidType.TYPE;
			}
		}
		typeExp = new AFieldAccessExp(new APrimaryFieldAccess(typeExp, new TDot(), new TId(token)));
	}
	return typeExp;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AArrayInitializer
 */
public void caseAArrayInitializer(AArrayInitializer node) {
	try {
		ArrayType at = (ArrayType) currentType;
		currentType = new ArrayType(at.baseType, at.nDimensions - 1);
	
		Object temp[] = node.getVariableInitializer().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PVariableInitializer) temp[i]).apply(this);
		}
	} catch (Exception e) {
		exceptions.add(new TypeException("Type mismatch in array initializer"));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AAssignmentExp
 */
public void caseAAssignmentExp(AAssignmentExp node) {
	String op = node.getAssignmentOperator().toString().trim();
	if ("=".equals(op)) {
		node.getLeftHandSide().apply(this);
		Type lhsType = currentType;
		node.getExp().apply(this);
		if (!(currentType.isValidIdentityConversion(lhsType) || currentType.isValidWideningConversion(lhsType))) {
			exceptions.add(new TypeException("Type mismatch in assignment operation"));
		}
		currentType = lhsType;
	} else {
		node.getLeftHandSide().apply(this);
		String s = currentType.toString();
		PName name = null;
		if ("java.lang.String".equals(s)) {
			name = new AQualifiedName(new AQualifiedName(new ASimpleName(new TId("java")), new TDot(), new TId("lang")), new TDot(), new TId("lang"));
		} else if ("boolean".equals(s)) {
			name = new ASimpleName(new TId("boolean"));
		} else if ("byte".equals(s)) {
			name = new ASimpleName(new TId("byte"));
		} else if ("short".equals(s)) {
			name = new ASimpleName(new TId("short"));
		} else if ("char".equals(s)) {
			name = new ASimpleName(new TId("char"));
		} else if ("int".equals(s)) {
			name = new ASimpleName(new TId("int"));
		} else if ("long".equals(s)) {
			name = new ASimpleName(new TId("long"));
		} else if ("float".equals(s)) {
			name = new ASimpleName(new TId("float"));
		} else if ("double".equals(s)) {
			name = new ASimpleName(new TId("double"));
		} else {
			currentType = VoidType.TYPE;
			exceptions.add(new TypeException("Type mismatch for assignment operator " + op));
			return;
		}
		PBinaryOperator binop;
		if ("+=".equals(op)) {
			binop = new APlusBinaryOperator();
		} else if ("-=".equals(op)) {
			binop = new AMinusBinaryOperator();
		} else if ("*=".equals(op)) {
			binop = new AStarBinaryOperator();
		} else if ("/=".equals(op)) {
			binop = new ADivBinaryOperator();
		} else if ("%=".equals(op)) {
			binop = new AModBinaryOperator();
		} else if ("<<=".equals(op)) {
			binop = new AShiftLeftBinaryOperator();
		} else if (">>=".equals(op)) {
			binop = new ASignedShiftRightBinaryOperator();
		} else if ("<<<=".equals(op)) {
			binop = new AUnsignedShiftRightBinaryOperator();
		} else if ("&=".equals(op)) {
			binop = new ABitAndBinaryOperator();
		} else if ("|=".equals(op)) {
			binop = new ABitOrBinaryOperator();
		} else if ("^=".equals(op)) {
			binop = new ABitXorBinaryOperator();
		} else {
			currentType = VoidType.TYPE;
			exceptions.add(new TypeException("Unknown operator " + op));
			return;
		}

		PExp exp1;

		if (node.getLeftHandSide() instanceof AFieldAccessLeftHandSide) {
			exp1 = new AFieldAccessExp((PFieldAccess) ((AFieldAccessLeftHandSide) node.getLeftHandSide()).getFieldAccess().clone());
		} else if (node.getLeftHandSide() instanceof AArrayAccessLeftHandSide) {
			exp1 = new AArrayAccessExp((PArrayAccess) ((AArrayAccessLeftHandSide) node.getLeftHandSide()).getArrayAccess().clone());
		} else {
			exp1 = new ANameExp((PName) ((ANameLeftHandSide) node.getLeftHandSide()).getName().clone());
		}

		new AAssignmentExp((PLeftHandSide) node.getLeftHandSide().clone(), new AAssignAssignmentOperator(new TAssign()),
				new AExpCastExp(new TLPar(), new ANameExp(name), new TRPar(), new ABinaryExp(exp1, binop, (PExp) node.getExp().clone()))).apply(this);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	node.getFirst().apply(this);
	Type leftType = currentType;
	node.getSecond().apply(this);
	Type rightType = currentType;

	PBinaryOperator binOp = node.getBinaryOperator();
	if ((binOp instanceof AAndBinaryOperator) || (binOp instanceof AOrBinaryOperator)) {
		// && ||
		currentType = BooleanType.TYPE;
		if ((leftType != BooleanType.TYPE) || (rightType != BooleanType.TYPE)) {
			exceptions.add(new TypeException("Expecting boolean expression"));
		}
	} else if ((binOp instanceof ABitAndBinaryOperator) || (binOp instanceof ABitOrBinaryOperator)
			 || (binOp instanceof ABitXorBinaryOperator)) {
		// & | ^
		currentType = IntType.TYPE;
		if ((leftType == BooleanType.TYPE) && (rightType == BooleanType.TYPE)) {
			currentType = BooleanType.TYPE;
		} else if (getIntegralType(leftType, rightType) != null) {
			currentType = getIntegralType(leftType, rightType);
		} else {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof APlusBinaryOperator) || (binOp instanceof AMinusBinaryOperator)
			 || (binOp instanceof AStarBinaryOperator) || (binOp instanceof ADivBinaryOperator)
			 || (binOp instanceof AModBinaryOperator)) {
		// + - * / % /| %% 
		currentType = IntType.TYPE;
		if (getNumericType(leftType, rightType) != null) {
			currentType = getNumericType(leftType, rightType);
		} else {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof AShiftLeftBinaryOperator) || (binOp instanceof ASignedShiftRightBinaryOperator)
			|| (binOp instanceof AUnsignedShiftRightBinaryOperator)) {
		// << >> >>>
		currentType = IntType.TYPE;
		if (getIntegralType(leftType, rightType) != null) {
			currentType = getIntegralType(leftType, rightType);
		} else {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof ALtBinaryOperator) || (binOp instanceof ALteqBinaryOperator)
			 || (binOp instanceof AGtBinaryOperator) || (binOp instanceof AGteqBinaryOperator)) {
		// < <= > >=
		currentType = BooleanType.TYPE;
		if (getNumericType(leftType, rightType) == null) {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof AEqBinaryOperator) || (binOp instanceof ANeqBinaryOperator)) {
		// == !=
		currentType = BooleanType.TYPE;
		if ((leftType == BooleanType.TYPE) && (rightType == BooleanType.TYPE)) {
		} else if ((leftType instanceof ReferenceType) && (rightType instanceof ReferenceType)) {
		} else if (getNumericType(leftType, rightType) == null) {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABooleanPrimitiveType
 */
public void caseABooleanPrimitiveType(ABooleanPrimitiveType node) {
	currentType = BooleanType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABytePrimitiveType
 */
public void caseABytePrimitiveType(ABytePrimitiveType node) {
	currentType = ByteType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ACharacterLiteralLiteral
 */
public void caseACharacterLiteralLiteral(ACharacterLiteralLiteral node) {
	currentType = CharType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ACharPrimitiveType
 */
public void caseACharPrimitiveType(ACharPrimitiveType node) {
	currentType = CharType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassOrInterfaceTypeExp
 */
public void caseAClassOrInterfaceTypeExp(AClassOrInterfaceTypeExp node) {
	for (ca.mcgill.sable.util.Iterator i = node.getDimExp().iterator(); i.hasNext();) {
		((PDimExp) i.next()).apply(this);
		if (!(currentType instanceof IntegralType)) {
			exceptions.add(new TypeException("Expecting integral expression in dim exp"));
		}
	}

	try {
		currentType = symbolTable.resolveType(new Name(node.getClassOrInterfaceType().toString()));
		int dimensions = node.getDimExp().size() + node.getDim().size();
		currentType = new ArrayType(currentType, dimensions);
	} catch (Exception e) {
		exceptions.add(e);
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ADecimalIntegerLiteral
 */
public void caseADecimalIntegerLiteral(ADecimalIntegerLiteral node) {
	String literal = node.toString().trim();
	if (literal.endsWith("L") || literal.endsWith("l")) {
		currentType = LongType.TYPE;
	} else {
		currentType = IntType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ADoublePrimitiveType
 */
public void caseADoublePrimitiveType(ADoublePrimitiveType node) {
	currentType = DoubleType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AExpCastExp
 */
public void caseAExpCastExp(AExpCastExp node) {
	currentType = VoidType.TYPE;
	Type destType;
	try {
		destType = symbolTable.resolveType(new Name(node.getFirst().toString()));
	} catch (Exception e) {
		exceptions.add(new TypeException("Invalid casting type " + node.getFirst()));
		return;
	}
	node.getSecond().apply(this);
	if (!currentType.isValidCastingConversion(destType)) {
		exceptions.add(new TypeException("Invalid casting type"));
	}
	currentType = destType;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AExpVariableInitializer
 */
public void caseAExpVariableInitializer(AExpVariableInitializer node) {
	Type destType = currentType;
	node.getExp().apply(this);
	if (!(currentType.isValidIdentityConversion(destType) || currentType.isValidWideningConversion(destType))) {
		exceptions.add(new TypeException("Type mismatch in array initializer"));
	}
	currentType = destType;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFalseBooleanLiteral
 */
public void caseAFalseBooleanLiteral(AFalseBooleanLiteral node) {
	currentType = BooleanType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFloatingPointLiteralLiteral
 */
public void caseAFloatingPointLiteralLiteral(AFloatingPointLiteralLiteral node) {
	String literal = node.toString().trim();
	if (literal.endsWith("F") || literal.endsWith("f"))
		currentType = FloatType.TYPE;
	else
		currentType = DoubleType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFloatPrimitiveType
 */
public void caseAFloatPrimitiveType(AFloatPrimitiveType node) {
	currentType = FloatType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AHexIntegerLiteral
 */
public void caseAHexIntegerLiteral(AHexIntegerLiteral node) {
	String literal = node.toString().substring(2).trim();
	if (literal.endsWith("L") || literal.endsWith("l")) {
		currentType = LongType.TYPE;
	} else {
		currentType = IntType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInitClassInterfaceExp
 */
public void caseAInitClassInterfaceExp(AInitClassInterfaceExp node) {
	try {
		currentType = symbolTable.resolveType(new Name(node.getClassOrInterfaceType().toString()));
		Type resultType = currentType = new ArrayType(currentType, node.getDim().size());
		node.getArrayInitializer().apply(this);
		currentType = resultType;
	} catch (Exception e) {
		exceptions.add(e);
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInitPrimitiveExp
 */
public void caseAInitPrimitiveExp(AInitPrimitiveExp node) {
	node.getPrimitiveType().apply(this);
	Type resultType = currentType = new ArrayType(currentType, node.getDim().size());
	node.getArrayInitializer().apply(this);
	currentType = resultType;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInstanceofExp
 */
public void caseAInstanceofExp(AInstanceofExp node) {
	node.getExp().apply(this);
	boolean flag = false;
	try {
		Type type = symbolTable.resolveClassOrInterfaceType(new Name(node.getReferenceType().toString()));
		if ((type instanceof ReferenceType) && (currentType instanceof ReferenceType))
			flag = true;
	} catch (Exception e) {
		exceptions.add(e);
	}
	if (!flag) {
		exceptions.add(new TypeException("Invalid type in instanceof expression"));
		currentType = BooleanType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AIntPrimitiveType
 */
public void caseAIntPrimitiveType(AIntPrimitiveType node) {
	currentType = IntType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALongPrimitiveType
 */
public void caseALongPrimitiveType(ALongPrimitiveType node) {
	currentType = LongType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameArrayAccess
 */
public void caseANameArrayAccess(ANameArrayAccess node) {
	new ANameExp((PName) node.getName().clone()).apply(this);
	if (!(currentType instanceof ArrayType)) {
		exceptions.add(new TypeException("Not an array type in array access"));
		currentType = VoidType.TYPE;
		return;
	}
	ArrayType arrayType = (ArrayType) currentType;
	node.getExp().apply(this);
	if (!(currentType instanceof IntegralType)) {
		exceptions.add(new TypeException("Expecting integral expression in array access"));
	}
	if (arrayType.nDimensions > 1) {
		currentType = new ArrayType(arrayType.baseType , arrayType.nDimensions - 1);
	} else {
		currentType = arrayType.baseType;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameCastExp
 */
public void caseANameCastExp(ANameCastExp node) {
	currentType = VoidType.TYPE;
	Type destType;
	try {
		destType = symbolTable.resolveType(new Name(node.getName().toString()));
	} catch (Exception e) {
		exceptions.add(new TypeException("Invalid casting type " + node.getName()));
		return;
	}
	int dimension = node.getDim().size();
	if (dimension > 0) {
		destType = new ArrayType(destType, dimension);
	}
	node.getExp().apply(this);
	if (!currentType.isValidCastingConversion(destType)) {
		exceptions.add(new TypeException("Invalid casting type"));
	}
	currentType = destType;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANamedTypeExp
 */
public void caseANamedTypeExp(ANamedTypeExp node) {
	currentType = VoidType.TYPE;
	exceptions.add(new TypeException("unsupported expression " + node));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameExp
 */
public void caseANameExp(ANameExp node) {
	if ("$ret".equals(node.toString().trim())) {
		if (annotation instanceof MethodDeclarationAnnotation) {
			currentType = ((MethodDeclarationAnnotation) annotation).getMethod().getReturnType();
		} else {
			currentType = VoidType.TYPE;
		}
		return;
	}
	if (node.getName() instanceof ASimpleName) {
		Hashtable visibleLocals = getDeclaredLocals();
		String local = node.getName().toString().trim();
		Name localOrFieldName = new Name(local);
		if (visibleLocals.get(local) != null) {
			try {
				currentType = Util.convertType(((Local) visibleLocals.get(local)).getType(), symbolTable);
			} catch (Exception e) {
				exceptions.add(e);
			}
		} else {
			try {
				Field f = type.getField(localOrFieldName);
				currentType = f.getType();
				if (isStatic && !java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
					TId id = ((ASimpleName) node.getName()).getId();
					exceptions.add(new TypeException("Cannot refer to instance field '"
							+ id.toString().trim() + "' in a static predicate"));
				}
			} catch (Exception e) {
				try {
					currentType = symbolTable.resolveType(new Name(node.getName().toString()));
				} catch (Exception e2) {
					currentType = VoidType.TYPE;
					exceptions.add(new TypeException("Unresolved name expression '" + localOrFieldName.toString()
							+ "' in " + type.getFullyQualifiedName()));
				}
			}
		}
	} else {
		Name name = new Name(node.getName().toString()).getSuperName();
		ClassOrInterfaceType type = null;
		PName typeName = ((AQualifiedName) node.getName()).getName();
		while (!name.isSimpleName()) {
			try {
				type = (ClassOrInterfaceType) symbolTable.resolveType(name);
				break;
			} catch (Exception e) {
				name = name.getSuperName();
				typeName = ((AQualifiedName) typeName).getName();
			}
		}
		
		if (type == null) {
			try {
				type = (ClassOrInterfaceType) symbolTable.resolveType(name);
			} catch (Exception e) {
				try {
					currentType = (ClassOrInterfaceType) symbolTable.resolveType(new Name(node.getName().toString()));
					usedTypeNames.add(node);
					return;
				} catch (Exception e2) {
					PExp exp = buildFieldAccess((AQualifiedName) node.getName().clone());
					node.replaceBy(exp);
					exp.apply(this);
					return;
				}
			}
		}

		String nameExp = new Name(node.getName().toString()).toString();
		Name n = new Name(nameExp.substring(type.getFullyQualifiedName().length() + 1));
		nameExp = n.getSubName().toString();
		PName newName = (PName) typeName.parent().clone();
		try {
			currentType = type.getField(new Name(n.getFirstIdentifier())).getType();
		} catch (Exception e) {
			exceptions.add(e);
			currentType = VoidType.TYPE;
		}
		PExp exp = buildFieldAccess(new ANameExp(newName), nameExp);
		node.replaceBy(exp);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameMethodInvocationExp
 */
public void caseANameMethodInvocationExp(ANameMethodInvocationExp node) {
	Vector types = new Vector();
	{
		Object temp[] = node.getArgumentList().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PExp) temp[i]).apply(this);
			types.add(currentType);
		}
	}

	Name methodName;
	if (node.getName() instanceof AQualifiedName) {
		new ANameExp((PName) ((AQualifiedName) node.getName()).getName().clone()).apply(this);
		methodName = new Name(((AQualifiedName) node.getName()).getId().toString());
	} else {
		currentType = type;
		methodName = new Name(((ASimpleName) node.getName()).getId().toString());
	}

	try {
		Method m = symbolTable.resolveMethod(currentType.getName(), methodName, types);
		currentType = m.getReturnType();
	} catch (Exception e) {
		exceptions.add(e);
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANullLiteral
 */
public void caseANullLiteral(ANullLiteral node) {
	currentType = NullType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AOctalIntegerLiteral
 */
public void caseAOctalIntegerLiteral(AOctalIntegerLiteral node) {
	String literal = node.toString().substring(1).trim();
	if (literal.endsWith("L") || literal.endsWith("l")) {
		currentType = LongType.TYPE;
	} else {
		currentType = IntType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APostDecrementExp
 */
public void caseAPostDecrementExp(APostDecrementExp node) {
	node.getExp().apply(this);
	if (!(currentType instanceof IntegralType)) {
		currentType = IntType.TYPE;
		exceptions.add(new TypeException("Expecting an integral expression for unary operator --"));
	} 
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APostIncrementExp
 */
public void caseAPostIncrementExp(APostIncrementExp node) {
	node.getExp().apply(this);
	if (!(currentType instanceof IntegralType)) {
		currentType = IntType.TYPE;
		exceptions.add(new TypeException("Expecting an integral expression for unary operator ++"));
	} 
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryFieldAccess
 */
public void caseAPrimaryFieldAccess(APrimaryFieldAccess node) {
	node.getExp().apply(this);
	if ((currentType instanceof ArrayType) && ("length".equals(node.getId().toString().trim()))) {
		currentType = IntType.TYPE;
	} else if (currentType instanceof ClassOrInterfaceType) {
		try {
			currentType = ((ClassOrInterfaceType) currentType).getField(new Name(node.getId().toString())).getType();
		} catch (Exception e) {
			exceptions.add(new TypeException("unresolved field name " + node.getId()));
			currentType = VoidType.TYPE;
		}
	} else {
		exceptions.add(new TypeException("Invalid type in field access"));
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryMethodInvocationExp
 */
public void caseAPrimaryMethodInvocationExp(APrimaryMethodInvocationExp node) {
	Vector types = new Vector();
	{
		Object temp[] = node.getArgumentList().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PExp) temp[i]).apply(this);
			types.add(currentType);
		}
	}

	Name methodName = methodName = new Name(node.getId().toString());
	node.getExp().apply(this);
	try {
		Method m = symbolTable.resolveMethod(currentType.getName(), methodName, types);
		currentType = m.getReturnType();
	} catch (Exception e) {
		exceptions.add(e);
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryNoNewArrayArrayAccess
 */
public void caseAPrimaryNoNewArrayArrayAccess(APrimaryNoNewArrayArrayAccess node) {
	node.getFirst().apply(this);
	if (!(currentType instanceof ArrayType)) {
		exceptions.add(new TypeException("Not an array type in array access"));
		currentType = VoidType.TYPE;
		return;
	}
	ArrayType arrayType = (ArrayType) currentType;
	node.getSecond().apply(this);
	if (!(currentType instanceof IntegralType)) {
		exceptions.add(new TypeException("Expecting integral expression in array access"));
	}
	if (arrayType.nDimensions > 1) {
		currentType = new ArrayType(arrayType.baseType , arrayType.nDimensions - 1);
	} else {
		currentType = arrayType.baseType;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypeArrayExp
 */
public void caseAPrimitiveTypeArrayExp(APrimitiveTypeArrayExp node) {
	for (ca.mcgill.sable.util.Iterator i = node.getDimExp().iterator(); i.hasNext();) {
		((PDimExp) i.next()).apply(this);
		if (!(currentType instanceof IntegralType)) {
			exceptions.add(new TypeException("Expecting integral expression in dim exp"));
		}
	}

	node.getPrimitiveType().apply(this);
	int dimensions = node.getDimExp().size() + node.getDim().size();
	currentType = new ArrayType(currentType, dimensions);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypeCastExp
 */
public void caseAPrimitiveTypeCastExp(APrimitiveTypeCastExp node) {
	node.getPrimitiveType().apply(this);
	Type destType = currentType;
	int dimension = node.getDim().size();
	if (dimension > 0) {
		destType = new ArrayType(destType, dimension);
	}
	node.getExp().apply(this);
	
	if (!currentType.isValidCastingConversion(destType)) {
		exceptions.add(new TypeException("Invalid casting type"));
	}
	currentType = destType;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypePrimaryExp
 */
public void caseAPrimitiveTypePrimaryExp(APrimitiveTypePrimaryExp node) {
	currentType = VoidType.TYPE;
	exceptions.add(new TypeException("unsupported expression " + node));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedClassInstanceCreationExp
 */
public void caseAQualifiedClassInstanceCreationExp(AQualifiedClassInstanceCreationExp node) {
	currentType = VoidType.TYPE;
	exceptions.add(new TypeException("unsupported expression " + node));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedThisExp
 */
public void caseAQualifiedThisExp(AQualifiedThisExp node) {
	currentType = VoidType.TYPE;
	exceptions.add(new TypeException("unsupported expression " + node));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQuestionExp
 */
public void caseAQuestionExp(AQuestionExp node) {
	node.getFirst().apply(this);
	if (currentType != BooleanType.TYPE) {
		exceptions.add(new TypeException("Expecting a boolean expression for ?: first expression"));
	}
	node.getSecond().apply(this);
	Type trueType = currentType;
	node.getThird().apply(this);
	Type falseType = currentType;

	boolean flag = true;
	if (trueType == falseType) {
		currentType = trueType;
	} else if (trueType instanceof NumericType) {
		if (((trueType == ByteType.TYPE) && (falseType == ShortType.TYPE))
				|| ((trueType == ShortType.TYPE) && (falseType == ByteType.TYPE))) {
			currentType = ShortType.TYPE;
		} else if (((trueType == ByteType.TYPE) || (trueType == ShortType.TYPE)
				|| (trueType == CharType.TYPE)) && (falseType == IntType.TYPE)
				&& (node.getThird() instanceof ALiteralExp)) {
			currentType = trueType;
		} else if (((falseType == ByteType.TYPE) || (falseType == ShortType.TYPE)
				|| (falseType == CharType.TYPE)) && (trueType == IntType.TYPE)
				&& (node.getSecond() instanceof ALiteralExp)) {
			currentType = falseType;
		} else {
			currentType = getNumericType(trueType, falseType);
		}
	} else if ((trueType == NullType.TYPE) && (falseType instanceof ReferenceType)) {
		currentType = trueType;
	} else if ((trueType instanceof ReferenceType) && (falseType == NullType.TYPE)) {
		currentType = falseType;
	} else if ((trueType instanceof ReferenceType) && (falseType instanceof ReferenceType)) {
		if (trueType.isValidIdentityConversion(falseType)
				&& trueType.isValidWideningConversion(falseType)) {
			currentType = falseType;
		} else if (falseType.isValidIdentityConversion(trueType)
				&& falseType.isValidWideningConversion(trueType)) {
			currentType = trueType;
		} else flag = false;
	} else {
		flag = false;
	}

	if (!flag) {
		currentType = VoidType.TYPE;
		exceptions.add(new TypeException("Type mismatch in question expression"));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AShortPrimitiveType
 */
public void caseAShortPrimitiveType(AShortPrimitiveType node) {
	currentType = ShortType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASimpleClassInstanceCreationExp
 */
public void caseASimpleClassInstanceCreationExp(ASimpleClassInstanceCreationExp node) {
	currentType = VoidType.TYPE;
	exceptions.add(new TypeException("unsupported expression " + node));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AStringLiteralLiteral
 */
public void caseAStringLiteralLiteral(AStringLiteralLiteral node) {
	try {
		currentType = symbolTable.resolveType(new Name("java.lang.String"));
	} catch (Exception e) {
		exceptions.add(e);
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperFieldAccess
 */
public void caseASuperFieldAccess(ASuperFieldAccess node) {
	try {
		currentType = type.getDirectSuperClass().getField(new Name(node.getId().toString())).getType();
	} catch (Exception e) {
		exceptions.add(new TypeException("Invalid field in super field access"));
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperMethodInvocationExp
 */
public void caseASuperMethodInvocationExp(ASuperMethodInvocationExp node) {
	Vector types = new Vector();
	{
		Object temp[] = node.getArgumentList().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PExp) temp[i]).apply(this);
			types.add(currentType);
		}
	}

	Name methodName = methodName = new Name(node.getId().toString());
	currentType = type.getDirectSuperClass();
	try {
		Method m = symbolTable.resolveMethod(currentType.getName(), methodName, types);
		currentType = m.getReturnType();
	} catch (Exception e) {
		exceptions.add(e);
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AThisExp
 */
public void caseAThisExp(AThisExp node) {
	currentType = type;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATrueBooleanLiteral
 */
public void caseATrueBooleanLiteral(ATrueBooleanLiteral node) {
	currentType = BooleanType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AUnaryExp
 */
public void caseAUnaryExp(AUnaryExp node) {
	PUnaryOperator op = node.getUnaryOperator();
	node.getExp().apply(this);
	if (op instanceof AIncrementUnaryOperator) {
		if (!(currentType instanceof IntegralType)) {
			currentType = IntType.TYPE;
			exceptions.add(new TypeException("Expecting an integral expression for unary operator ++"));
		} 
	} else if (op instanceof ADecrementUnaryOperator) {
		if (!(currentType instanceof IntegralType)) {
			currentType = IntType.TYPE;
			exceptions.add(new TypeException("Expecting an integral expression for unary operator --"));
		} 
	} else if (op instanceof APlusUnaryOperator) {
		if (!(currentType instanceof NumericType)) {
			currentType = IntType.TYPE;
			exceptions.add(new TypeException("Expecting a numeric expression for unary operator +"));
		}
	} else if (op instanceof AMinusUnaryOperator) {
		if (!(currentType instanceof NumericType)) {
			currentType = IntType.TYPE;
			exceptions.add(new TypeException("Expecting a numeric expression for unary operator -"));
		}
	} else if (op instanceof ABitComplementUnaryOperator) {
		if (!(currentType instanceof IntegralType)) {
			currentType = IntType.TYPE;
			exceptions.add(new TypeException("Expecting an integral expression for unary operator ~"));
		}
		if (!(currentType instanceof LongType)) {
			currentType = IntType.TYPE;
		}
	} else if (op instanceof AComplementUnaryOperator) {
		if (currentType != BooleanType.TYPE) {
			currentType = BooleanType.TYPE;
			exceptions.add(new TypeException("Expecting a boolean expression for unary operator !"));
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AVoidExp
 */
public void caseAVoidExp(AVoidExp node) {
	currentType = VoidType.TYPE;
	exceptions.add(new TypeException("unsupported expression " + node));
}
/**
 * 
 * @return java.lang.Vector
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public Vector check(Node node) {
	node.apply(this);

	if (currentType != BooleanType.TYPE) {
		exceptions.add(new TypeException("Assertion expression should be boolean type"));
	}
	
	return exceptions;
}
/**
 * 
 * @return java.util.Hashtable
 */
private Hashtable getDeclaredLocals() {
	if (currentAnnotation != null) {
		Hashtable visibleLocals;
		if (annotation instanceof MethodDeclarationAnnotation) {
			visibleLocals = ((MethodDeclarationAnnotation) annotation).getParameterLocals();
		} else {
			visibleLocals = ((ConstructorDeclarationAnnotation) annotation).getParameterLocals();
		}
		for (Enumeration e = currentAnnotation.getDeclaredLocalVariables().elements();
				e.hasMoreElements();) {
			Local l = (Local) e.nextElement();
			visibleLocals.put(l.getName().trim(), l);
		}
		return visibleLocals;
	} else {
		if (annotation instanceof MethodDeclarationAnnotation) {
			return ((MethodDeclarationAnnotation) annotation).getParameterLocals();
		} else {
			return ((ConstructorDeclarationAnnotation) annotation).getParameterLocals();
		}
	}
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getExceptions() {
	return exceptions;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param leftType edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param rightType edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
private Type getIntegralType(Type leftType, Type rightType) {
	if (!(leftType instanceof IntegralType) || !(rightType instanceof IntegralType))
		return null;
	if ((leftType == LongType.TYPE) || (rightType == LongType.TYPE))
		return LongType.TYPE;
	else if ((leftType instanceof IntegralType) || (rightType instanceof IntegralType))
		return IntType.TYPE;
	else return null;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param leftType edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param rightType edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
private Type getNumericType(Type leftType, Type rightType) {
	if (!(leftType instanceof NumericType) || !(rightType instanceof NumericType))
		return null;
	if ((leftType == LongType.TYPE) || (rightType == LongType.TYPE))
		return LongType.TYPE;
	else if ((leftType == FloatType.TYPE) || (rightType == FloatType.TYPE))
		return FloatType.TYPE;
	else if ((leftType == DoubleType.TYPE) || (rightType == DoubleType.TYPE))
		return DoubleType.TYPE;
	else if ((leftType instanceof IntegralType) || (rightType instanceof IntegralType))
		return IntType.TYPE;
	else return null;
}
}
