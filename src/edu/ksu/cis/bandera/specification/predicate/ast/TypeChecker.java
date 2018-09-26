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
import ca.mcgill.sable.soot.Modifier;
import java.util.*;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;
import edu.ksu.cis.bandera.specification.predicate.node.*;
import edu.ksu.cis.bandera.specification.predicate.exception.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.jjjc.util.Util;
public class TypeChecker extends DepthFirstAdapter {
	private Vector errors = new Vector();
	private Vector exceptions = new Vector();
	private Node node;
	private SymbolTable symbolTable;
	private ClassOrInterfaceType type;
	private Annotation annotation;
	private int mode;
	private Vector labeledStmtAnnotations;
	private LabeledStmtAnnotation currentAnnotation;
	private Type currentType;
	private String predName;
	private AnnotationManager am;
	private final static int EXP = 1;
	private final static int RETURN = 2;
	private final static int INVOKE = 4;
	private final static int LOCATION = 8;
	private boolean isStatic;
	private boolean usePrivate;
	private boolean ret;
	private StringBuffer buffer;
	private Vector usedTypeNames = new Vector();
	private Hashtable variablesUsed;
	private Vector params;
	private Vector paramTypes;
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
 * @param node edu.ksu.cis.bandera.bpdl.node.Node
 * @param symbolTable edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public TypeChecker(Node node, SymbolTable symbolTable, ClassOrInterfaceType type, Annotation annotation) {
	this.node = node;
	this.symbolTable = symbolTable;
	this.annotation = annotation;
	this.type = type;
	if (annotation != null) {
		Vector v = annotation.getAllAnnotations(false);
		labeledStmtAnnotations = new Vector();
		for (Enumeration e = v.elements(); e.hasMoreElements();) {
			Object o = e.nextElement();
			if (o instanceof LabeledStmtAnnotation) labeledStmtAnnotations.add(o);
		}
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 * @param name edu.ksu.cis.bandera.predicate.node.AQualifiedName
 */
private PExp buildNavigation(AQualifiedName name) {
	PExp exp;
	if (name.getName() instanceof AQualifiedName) {
		exp = buildNavigation((AQualifiedName) name.getName());
	} else {
		exp = buildNavigation((ASimpleName) name.getName());
	}
	
	return new ANavigationExp(exp, new AStrongObjectNavigation(new TDot(), name.getId()));
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 * @param name edu.ksu.cis.bandera.predicate.node.ASimpleName
 */
private PExp buildNavigation(ASimpleName name) {
	return new ANameExp(name);
}
/**
 * 
 * @return edu.ksu.cis.bandera.predicate.node.PExp
 * @param typeExp edu.ksu.cis.bandera.predicate.node.PExp
 * @param name java.lang.String
 */
private PExp buildNavigation(PExp typeExp, String name) {
	for (StringTokenizer tokenizer = new StringTokenizer(name); tokenizer.hasMoreTokens();) {
		String token = (String) tokenizer.nextToken();
		typeExp = new ANavigationExp(typeExp, new AStrongObjectNavigation(new TDot(), new TId(token)));
	}
	return typeExp;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	node.getLeft().apply(this);
	Type leftType = currentType;
	node.getRight().apply(this);
	Type rightType = currentType;

	PBinOp binOp = node.getBinOp();
	if ((binOp instanceof AAndBinOp) || (binOp instanceof AOrBinOp)) {
		// && ||
		currentType = BooleanType.TYPE;
		if ((leftType != BooleanType.TYPE) || (rightType != BooleanType.TYPE)) {
			exceptions.add(new TypeException("Expecting boolean expression"));
		}
	} else if ((binOp instanceof ABitAndBinOp) || (binOp instanceof ABitOrBinOp)
			 || (binOp instanceof ABitXorBinOp)) {
		// & | ^
		currentType = IntType.TYPE;
		if ((leftType == BooleanType.TYPE) && (rightType == BooleanType.TYPE)) {
			currentType = BooleanType.TYPE;
		} else if (getIntegralType(leftType, rightType) != null) {
			currentType = getIntegralType(leftType, rightType);
		} else {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof APlusBinOp) || (binOp instanceof AMinusBinOp)
			 || (binOp instanceof ATimesBinOp) || (binOp instanceof AStrongDivBinOp)
			 || (binOp instanceof AStrongModBinOp)) {// || (binOp instanceof AWeakDivBinOp)
			 //|| (binOp instanceof AWeakModBinOp)) {
		// + - * / % /| %% 
		currentType = IntType.TYPE;
		if (getNumericType(leftType, rightType) != null) {
			currentType = getNumericType(leftType, rightType);
		} else {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof AShiftLeftBinOp) || (binOp instanceof ASignedShiftRightBinOp)
			|| (binOp instanceof AUnsignedShiftRightBinOp)) {
		// << >> >>>
		currentType = IntType.TYPE;
		if (getIntegralType(leftType, rightType) != null) {
			currentType = getIntegralType(leftType, rightType);
		} else {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof ALessBinOp) || (binOp instanceof ALessEqualBinOp)
			 || (binOp instanceof AGreaterBinOp) || (binOp instanceof AGreaterEqualBinOp)) {
		// < <= > >=
		currentType = BooleanType.TYPE;
		if (getNumericType(leftType, rightType) == null) {
			exceptions.add(new TypeException("Type mismatch for binary operator " + binOp.toString().trim()));
		}
	} else if ((binOp instanceof AEqualBinOp) || (binOp instanceof ANotEqualBinOp)) {
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
 * @param node edu.ksu.cis.bandera.predicate.node.ABitComplementExp
 */
public void caseABitComplementExp(ABitComplementExp node) {
	node.getExp().apply(this);
	if (!(currentType instanceof IntegralType)) {
		currentType = IntType.TYPE;
		exceptions.add(new TypeException("Expecting boolean expression for unary operator ~"));
	}
	if (!(currentType instanceof LongType)) {
		currentType = IntType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ACharLiteral
 */
public void caseACharLiteral(ACharLiteral node) {
	currentType = CharType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AComplementExp
 */
public void caseAComplementExp(AComplementExp node) {
	node.getExp().apply(this);
	if (currentType != BooleanType.TYPE) {
		currentType = BooleanType.TYPE;
		exceptions.add(new TypeException("Expecting boolean expression for unary operator !"));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ADecIntLiteral
 */
public void caseADecIntLiteral(ADecIntLiteral node) {
	currentType = IntType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ADecLongLiteral
 */
public void caseADecLongLiteral(ADecLongLiteral node) {
	currentType = LongType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ADoubleLiteral
 */
public void caseADoubleLiteral(ADoubleLiteral node) {
	currentType = DoubleType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AEndOfLineComment
 */
public void caseAEndOfLineComment(AEndOfLineComment node) {
	String s = node.toString().trim();
	s = s.substring(2, s.length());
	buffer.append(s.trim() + " ");
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AExpressionPropositionDefinition
 */
public void caseAExpressionPropositionDefinition(AExpressionPropositionDefinition node) { 
	usePrivate = false;
	variablesUsed = new Hashtable();
	buffer = new StringBuffer();
	if (node.getComment() != null) {
		for (Iterator i = node.getComment().iterator(); i.hasNext();) {
			((PComment) i.next()).apply(this);
		}
	}
	isStatic = !(node.getParams() instanceof AInstanceParams);
	params = new Vector();
	paramTypes = new Vector();
	
	if (node.getParams() != null) {
		node.getParams().apply(this);
	}
	if (annotation != null) {
		exceptions.add(new BadPredicateDefinitionException("Expression predicate cannot be in method/constructor level: " + node));
	} else {
		mode = EXP;
		node.getColonExp().apply(this);
		if (currentType != BooleanType.TYPE) {
			exceptions.add(new BadPredicateDefinitionException("Expression predicate should be type boolean: " + node));
		}
	}
	String name = predName + "." + node.getId().toString().trim();
	
	try {
		Predicate p = PredicateFactory.createExpressionPredicate(new Name(name), type, node, params, paramTypes, isStatic, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
	} catch (Exception e) {
		errors.addAll(exceptions);
		errors.add(e);
	}
	exceptions = new Vector();
	usedTypeNames = new Vector();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AFalseLiteral
 */
public void caseAFalseLiteral(AFalseLiteral node) {
	currentType = BooleanType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AFloatLiteral
 */
public void caseAFloatLiteral(AFloatLiteral node) {
	currentType = FloatType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AHexIntLiteral
 */
public void caseAHexIntLiteral(AHexIntLiteral node) {
	currentType = IntType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AHexLongLiteral
 */
public void caseAHexLongLiteral(AHexLongLiteral node) {
	currentType = LongType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AInstanceofExp
 */
public void caseAInstanceofExp(AInstanceofExp node) {
	currentType = BooleanType.TYPE;
	node.getExp().apply(this);
	boolean flag = false;
	try {
		Type type = symbolTable.resolveClassOrInterfaceType(new Name(node.getType().toString()));
		if ((type instanceof ReferenceType) && (currentType instanceof ReferenceType))
			flag = true;
	} catch (Exception e) {
		exceptions.add(e);
	}
	if (!flag) {
		exceptions.add(new TypeException("Invalid type in instanceof expression"));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AInvokePropositionDefinition
 */
public void caseAInvokePropositionDefinition(AInvokePropositionDefinition node) {
	usePrivate = false;
	variablesUsed = new Hashtable();
	buffer = new StringBuffer();
	if (node.getComment() != null) {
		for (Iterator i = node.getComment().iterator(); i.hasNext();) {
			((PComment) i.next()).apply(this);
		}
	}
	String name = predName + "." + node.getId().toString().trim();
	params = new Vector();
	paramTypes = new Vector();
	
	if (node.getParams() != null) {
		node.getParams().apply(this);
	}
	if (annotation == null) {
		exceptions.add(new BadPredicateDefinitionException("Invoke predicate cannot be in class/interface level: " + node));
	} else {
		mode = INVOKE;
		
		if (annotation instanceof MethodDeclarationAnnotation) {
			int modifiers = ((MethodDeclarationAnnotation) annotation).getSootMethod().getModifiers();
			if (Modifier.isStatic(modifiers)) {
				isStatic = true;
				if (node.getParams() instanceof AInstanceParams) {
					exceptions.add(new BadPredicateDefinitionException("Cannot define instance invoke predicate in a static method: " + node));
				}
			} else {
				isStatic = false;
				if (!(node.getParams() instanceof AInstanceParams)) {
					exceptions.add(new BadPredicateDefinitionException("Cannot define static invoke predicate in an instance method: " + node));
				}
			}
			if (Modifier.isNative(modifiers)) {
				exceptions.add(new BadPredicateDefinitionException("Invoke predicate cannot be in native method: " + node));
				try {
					Predicate p = PredicateFactory.createInvokePredicate(new Name(name), type, annotation, node, params, paramTypes, isStatic, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
				} catch (Exception e) {
					errors.addAll(exceptions);
					errors.add(e);
				}
				exceptions = new Vector();
				usedTypeNames = new Vector();
				return;
			}
		}
		if (node.getColonExp() != null) {
			node.getColonExp().apply(this);
			if (currentType != BooleanType.TYPE) {
				exceptions.add(new BadPredicateDefinitionException("Invoke predicate constraint should be type boolean: " + node));
			}
		}
	}
	try {
		Predicate p = PredicateFactory.createInvokePredicate(new Name(name), type, annotation, node, params, paramTypes, isStatic, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
	} catch (Exception e) {
		errors.addAll(exceptions);
		errors.add(e);
	}
	exceptions = new Vector();
	usedTypeNames = new Vector();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ALiteralExp
 */
public void caseALiteralExp(ALiteralExp node) {
	super.caseALiteralExp(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ALocationPropositionDefinition
 */
public void caseALocationPropositionDefinition(ALocationPropositionDefinition node) {
	usePrivate = false;
	variablesUsed = new Hashtable();
	buffer = new StringBuffer();
	if (node.getComment() != null) {
		for (Iterator i = node.getComment().iterator(); i.hasNext();) {
			((PComment) i.next()).apply(this);
		}
	}
	String name = predName + "." + node.getId().toString().trim();
	params = new Vector();
	paramTypes = new Vector();
	
	if (node.getParams() != null) {
		node.getParams().apply(this);
	}
	if (annotation == null) {
		exceptions.add(new BadPredicateDefinitionException("Location predicate cannot be in class/interface level: " + node));
	} else {
		mode = LOCATION;
		if (annotation instanceof MethodDeclarationAnnotation) {
			int modifiers = ((MethodDeclarationAnnotation) annotation).getSootMethod().getModifiers();
			if (Modifier.isStatic(modifiers)) {
				isStatic = true;
				if (node.getParams() instanceof AInstanceParams) {
					exceptions.add(new BadPredicateDefinitionException("Cannot define instance invoke predicate in a static method: " + node));
				}
			} else {
				isStatic = false;
				if (!(node.getParams() instanceof AInstanceParams)) {
					exceptions.add(new BadPredicateDefinitionException("Cannot define static invoke predicate in an instance method: " + node));
				}
			}
			if (Modifier.isNative(modifiers) || Modifier.isAbstract(modifiers)) {
				exceptions.add(new BadPredicateDefinitionException("Location predicate cannot be in abstract/native method: " + node));
				try {
					Predicate p = PredicateFactory.createLocationPredicate(new Name(name), type, annotation, node, node.getLabel().toString().trim(), params, paramTypes, isStatic, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
				} catch (Exception e) {
					errors.addAll(exceptions);
					errors.add(e);
				}
				exceptions = new Vector();
				usedTypeNames = new Vector();
				return;
			}
		}
		String label = node.getLabel().toString().trim();
		boolean found = false;
		for (Enumeration e = labeledStmtAnnotations.elements(); e.hasMoreElements();) {
			LabeledStmtAnnotation lsa = (LabeledStmtAnnotation) e.nextElement();
			if (lsa.getId().equals(label)) {
				currentAnnotation = lsa;
				found = true;
				break;
			}
		}
		if (!found) {
			exceptions.add(new BadPredicateDefinitionException("Invalid label '" + label + "': " + node));
			try {
				Predicate p = PredicateFactory.createLocationPredicate(new Name(name), type, currentAnnotation, node, node.getLabel().toString().trim(), params, paramTypes, isStatic, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
			} catch (Exception e) {
				errors.addAll(exceptions);
				errors.add(e);
			}
			exceptions = new Vector();
			usedTypeNames = new Vector();
			return;
		}
		if (node.getColonExp() != null) {
			node.getColonExp().apply(this);
			if (currentType != BooleanType.TYPE) {
				exceptions.add(new BadPredicateDefinitionException("Location predicate constraint should be of type boolean: " + node));
			}
		}
	}
	try {
		Predicate p = PredicateFactory.createLocationPredicate(new Name(name), type, currentAnnotation, node, node.getLabel().toString().trim(), params, paramTypes, isStatic, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
	} catch (Exception e) {
		errors.addAll(exceptions);
		errors.add(e);
	}
	exceptions = new Vector();
	usedTypeNames = new Vector();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ANameExp
 */
public void caseANameExp(ANameExp node) {
	if (node.getName() instanceof ASimpleName) {
		Hashtable visibleLocals = getDeclaredLocals();
		String local = node.getName().toString().trim();
		if (params.contains(local)) {
			currentType = (Type) paramTypes.elementAt(params.indexOf(local));
			variablesUsed.put(node, local);
		} else {
			Name localOrFieldName = new Name(local);
			Local lcl = (Local) visibleLocals.get(local);
			if (lcl != null) {
				try {
					currentType = Util.convertType(lcl.getType(), symbolTable);
					variablesUsed.put(node, lcl);
				} catch (Exception e) {
					exceptions.add(e);
				}
			} else {
				try {
					Field f = type.getField(localOrFieldName);
					currentType = f.getType();
					if (isStatic && !java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
						TId id = ((ASimpleName) node.getName()).getId();
						exceptions.add(new BadPredicateDefinitionException("Cannot refer to instance field '" + id.toString().trim() + "' in a static predicate"));
					} else {
						if (java.lang.reflect.Modifier.isPrivate(f.getModifiers())) {
							usePrivate = true;
						}
						variablesUsed.put(node, f);
					}
				} catch (Exception e) {
					try {
						currentType = symbolTable.resolveType(new Name(node.getName().toString()));
					} catch (Exception e2) {
						currentType = VoidType.TYPE;
						exceptions.add(new TypeException("Unresolved name expression '" + localOrFieldName.toString() + "' in " + type.getFullyQualifiedName()));
					}
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
					PExp exp = buildNavigation((AQualifiedName) node.getName());
					node.replaceBy(exp);
					exp.apply(this);
					return;
				}
			}
		}
		String nameExp = new Name(node.getName().toString()).toString();
		nameExp = nameExp.substring(type.getFullyQualifiedName().length() + 1);
		PExp exp = buildNavigation(new ANameExp(typeName), nameExp);
		node.replaceBy(exp);
		exp.apply(this);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.ANegativeExp
 */
public void caseANegativeExp(ANegativeExp node) {
	node.getExp().apply(this);
	if (!(currentType instanceof NumericType)) {
		currentType = IntType.TYPE;
		exceptions.add(new TypeException("Expecting numeric expression for unary operator -"));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ANullLiteral
 */
public void caseANullLiteral(ANullLiteral node) {
	currentType = NullType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AOctIntLiteral
 */
public void caseAOctIntLiteral(AOctIntLiteral node) {
	currentType = IntType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AOctLongLiteral
 */
public void caseAOctLongLiteral(AOctLongLiteral node) {
	currentType = LongType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.predicate.node.AParamParamList
 */
public void caseAParamParamList(AParamParamList node) {
	Type type;
	try {
		type = symbolTable.resolveType(new Name(node.getName().toString()));
	} catch (Exception e) {
		exceptions.add(new TypeException("Invalid parameter type"));
		return;
	}
	int i;
	if ((i = node.getDim().toArray().length) > 0) {
		type = new ArrayType(type, i);
	}

	paramTypes.add(type);
	params.add(node.getId().toString().trim());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.predicate.node.AParamsParamList
 */
public void caseAParamsParamList(AParamsParamList node) {
	node.getParamList().apply(this);
	Type type;
	try {
		type = symbolTable.resolveType(new Name(node.getName().toString()));
	} catch (Exception e) {
		exceptions.add(new TypeException("Invalid parameter type"));
		return;
	}
	int i;
	if ((i = node.getDim().toArray().length) > 0) {
		type = new ArrayType(type, i);
	}

	paramTypes.add(type);
	params.add(node.getId().toString().trim());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AParenExp
 */
public void caseAParenExp(AParenExp node) {
	super.caseAParenExp(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AQuestionExp
 */
public void caseAQuestionExp(AQuestionExp node) {
	node.getExp().apply(this);
	if (currentType != BooleanType.TYPE) {
		exceptions.add(new TypeException("Expecting a boolean expression for ?: expression"));
	}
	node.getTrueExp().apply(this);
	Type trueType = currentType;
	node.getFalseExp().apply(this);
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
				&& (node.getFalseExp() instanceof ALiteralExp)) {
			currentType = trueType;
		} else if (((falseType == ByteType.TYPE) || (falseType == ShortType.TYPE)
				|| (falseType == CharType.TYPE)) && (trueType == IntType.TYPE)
				&& (node.getTrueExp() instanceof ALiteralExp)) {
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
 * @param node edu.ksu.cis.bandera.bpdl.node.AReturnPropositionDefinition
 */
public void caseAReturnPropositionDefinition(AReturnPropositionDefinition node) {
	usePrivate = false;
	variablesUsed = new Hashtable();
	buffer = new StringBuffer();
	if (node.getComment() != null) {
		for (Iterator i = node.getComment().iterator(); i.hasNext();) {
			((PComment) i.next()).apply(this);
		}
	}
	String name = predName + "." + node.getId().toString().trim();
	params = new Vector();
	paramTypes = new Vector();
	
	if (node.getParams() != null) {
		node.getParams().apply(this);
	}
	if (annotation == null) {
		exceptions.add(new BadPredicateDefinitionException("Return predicate cannot be in class/interface level: " + node));
	} else {
		mode = RETURN;
		ret = false;
		
		if (annotation instanceof MethodDeclarationAnnotation) {
			int modifiers = ((MethodDeclarationAnnotation) annotation).getSootMethod().getModifiers();
			if (Modifier.isStatic(modifiers)) {
				isStatic = true;
				if (node.getParams() instanceof AInstanceParams) {
					exceptions.add(new BadPredicateDefinitionException("Cannot define instance invoke predicate in a static method: " + node));
				}
			} else {
				isStatic = false;
				if (!(node.getParams() instanceof AInstanceParams)) {
					exceptions.add(new BadPredicateDefinitionException("Cannot define static invoke predicate in an instance method: " + node));
				}
			}
			if (Modifier.isNative(modifiers)) {
				exceptions.add(new BadPredicateDefinitionException("Return predicate cannot be in native method: " + node));
				try {
					Predicate p = PredicateFactory.createReturnPredicate(new Name(name), type, annotation, node, params, paramTypes, isStatic, ret, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
				} catch (Exception e) {
					errors.addAll(exceptions);
					errors.add(e);
				}
				exceptions = new Vector();
				usedTypeNames = new Vector();
				return;
			}
		}
		
		if (node.getColonExp() != null) {
			node.getColonExp().apply(this);
			if (currentType != BooleanType.TYPE) {
				exceptions.add(new BadPredicateDefinitionException("Return predicate constraint should be type boolean: " + node));
			}
		}
	}
	try {
		Predicate p = PredicateFactory.createReturnPredicate(new Name(name), type, annotation, node, params, paramTypes, isStatic, ret, variablesUsed, node.getColonExp() != null ? ((AColonExp) node.getColonExp()).getExp() : null, buffer.toString(), exceptions);
	} catch (Exception e) {
		errors.addAll(exceptions);
		errors.add(e);
	}
	exceptions = new Vector();
	usedTypeNames = new Vector();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AReturnValueExp
 */
public void caseAReturnValueExp(AReturnValueExp node) {
	if (mode != RETURN) {
		exceptions.add(new BadPredicateDefinitionException("Can only have " + node.getRetVal().toString() + "in return predicate"));
		currentType = VoidType.TYPE;
		return;
	}

	if (annotation instanceof ConstructorDeclarationAnnotation) {
		exceptions.add(new BadPredicateDefinitionException("Cannot use " + node.getRetVal().toString() + "in a constructor return predicate"));
		currentType = VoidType.TYPE;
		return;
	}

	currentType = ((MethodDeclarationAnnotation) annotation).getMethod().getReturnType();
	variablesUsed.put(node, ((MethodDeclarationAnnotation) annotation).getRetLocal());
	ret = true;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AStringLiteral
 */
public void caseAStringLiteral(AStringLiteral node) {
	try {
		currentType = Package.getClassOrInterfaceType(new Name("java.lang.String"));
	} catch (Exception e) {
		exceptions.add(e);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongArrayNavigation
 */
public void caseAStrongArrayNavigation(AStrongArrayNavigation node) {
	Type baseType = currentType;
	node.getExp().apply(this);
	if (currentType instanceof IntegralType) {
		if (baseType instanceof ArrayType) {
			if (((ArrayType) baseType).nDimensions == 1) {
				currentType = ((ArrayType) baseType).baseType;
			} else {
				currentType = new ArrayType(((ArrayType) baseType).baseType, ((ArrayType) baseType).nDimensions - 1);
			}
		} else {
			exceptions.add(new TypeException("Not an array in array access"));
			currentType = VoidType.TYPE;
		}
	} else {
		exceptions.add(new TypeException("Not a numeric type in array access"));
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongCastExp
 */
public void caseAStrongCastExp(AStrongCastExp node) {
	currentType = VoidType.TYPE;
	Type cast;
	try {
		cast = symbolTable.resolveType(new Name(node.getType().toString()));
	} catch (Exception e) {
		exceptions.add(new TypeException("Invalid casting type"));
		return;
	}
	int i;
	if ((i = node.getDim().toArray().length) > 0) {
		cast = new ArrayType(cast, i);
	}
	node.getExp().apply(this);
	if (!currentType.isValidCastingConversion(cast)) {
		exceptions.add(new TypeException("Invalid casting type"));
	}
	variablesUsed.put(node, cast);
	currentType = cast;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AStrongObjectNavigation
 */
public void caseAStrongObjectNavigation(AStrongObjectNavigation node) {
	String id = node.getId().toString().trim();
	if ("length".equals(id) && (currentType instanceof ArrayType)) {
		currentType = IntType.TYPE;
	} else if (currentType instanceof ClassOrInterfaceType) {
		try {
			Field f = ((ClassOrInterfaceType) currentType).getField(new Name(id));
			currentType = f.getType();
			variablesUsed.put(node, f);
			if (usedTypeNames.contains(((ANavigationExp) node.parent()).getExp())) {
				if (!f.isAccessible(type)) {
					exceptions.add(new TypeException("Field '"
							+ currentType.getFullyQualifiedName() + "." +  f.getName()
							+ "' is from accessible from '" + type.getFullyQualifiedName()));
					currentType = VoidType.TYPE;
				}
			}
		} catch (Exception e) {
			exceptions.add(new TypeException("Field undefined"));
			currentType = VoidType.TYPE;
		}
	} else {
		exceptions.add(new TypeException("Not an object in field access"));
		currentType = VoidType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.predicate.node.AThisExp
 */
public void caseAThisExp(AThisExp node) {
	if (isStatic) {
		exceptions.add(new BadPredicateDefinitionException("Cannot have " + node.getThis().toString() + "in non-static predicate"));
		currentType = NullType.TYPE;
		return;
	}
	variablesUsed.put(node, "this");
	if (annotation instanceof MethodDeclarationAnnotation) {
		try {
			currentType =
					((MethodDeclarationAnnotation) annotation).getMethod().getDeclaringClassOrInterface();
		} catch (Exception e) {}
	} else if (annotation instanceof ConstructorDeclarationAnnotation) {
		try {
			currentType =
					((ConstructorDeclarationAnnotation) annotation).getConstructor().getDeclaringClassOrInterface();
		} catch (Exception e) {}
	} else {
		currentType = type;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.predicate.node.AThreadExp
 */
public void caseAThreadExp(AThreadExp node) {
	try {
		currentType = symbolTable.resolveType(new Name("Thread"));
	} catch (Exception e) {
		exceptions.add(e);
		currentType = NullType.TYPE;
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.ATrueLiteral
 */
public void caseATrueLiteral(ATrueLiteral node) {
	currentType = BooleanType.TYPE;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.bpdl.node.AUnit
 */
public void caseAUnit(AUnit node) {
	if(node.getName() != null) {
		predName = new Name(node.getName().toString()).toString().trim();
	} else {
		if (annotation != null) {
			if (annotation instanceof MethodDeclarationAnnotation) {
				MethodDeclarationAnnotation mda = (MethodDeclarationAnnotation) annotation;
				try {
					predName = mda.getMethod().getDeclaringClassOrInterface().getFullyQualifiedName()
							+ "." + mda.getMethod().getName().toString();
				} catch (Exception e) {
					throw new RuntimeException("Fatal Error");
				}
			} else {
				ConstructorDeclarationAnnotation cda = (ConstructorDeclarationAnnotation) annotation;
				try {
					predName = cda.getConstructor().getDeclaringClassOrInterface().getFullyQualifiedName()
							+ "." + cda.getConstructor().getDeclaringClassOrInterface().getName().getLastIdentifier();
				} catch (Exception e) {
					throw new RuntimeException("Fatal Error");
				}
			}
		} else {
			predName = type.getFullyQualifiedName();
		}
	}
	super.caseAUnit(node);
}
/**
 * 
 */
public void check() {
	node.apply(this);
}
/**
 * 
 * @return java.util.Hashtable
 */
private Hashtable getDeclaredLocals() {
	if (mode == LOCATION) {
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
	} else if (mode != EXP) {
		if (annotation instanceof MethodDeclarationAnnotation) {
			return ((MethodDeclarationAnnotation) annotation).getParameterLocals();
		} else {
			return ((ConstructorDeclarationAnnotation) annotation).getParameterLocals();
		}
	} else {
		return new Hashtable();
	}
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getErrors() {
	return errors;
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
