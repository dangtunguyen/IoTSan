package edu.ksu.cis.bandera.jjjc.codegenerator;

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
import edu.ksu.cis.bandera.jjjc.decompiler.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.util.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import java.util.*;
import java.math.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.optimizer.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.jjjc.exception.*;
import edu.ksu.cis.bandera.jjjc.util.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.doc.*;
import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.List;
import ca.mcgill.sable.util.LinkedList;
import ca.mcgill.sable.util.Iterator;
import edu.ksu.cis.bandera.specification.assertion.*;
import edu.ksu.cis.bandera.specification.predicate.*;

public class JIJCCodeGenerator extends DepthFirstAdapter {
	private SootClassManager scm = null;
	private AnnotationManager am = null;
	private Hashtable docComments = null;
	private Vector exceptions = new Vector();
	private SymbolTable symbolTable = null;
	private Jimple jimple = Jimple.v();
	private ca.mcgill.sable.soot.SootClass currentSootClass = null;
	private ca.mcgill.sable.soot.SootMethod currentSootMethod = null;
	private ClassOrInterfaceType currentClassOrInterfaceType = null;
	private Vector compiledClasses = new Vector();
	private edu.ksu.cis.bandera.jjjc.symboltable.Type integralConstantType = null;
	private boolean assignExp = false;

	// annotations
	private Vector synchAnnotations = new Vector();
	private BlockStmtAnnotation currentAnnotation = null;
	private LocalDeclarationStmtAnnotation localAnnotation = null;
	private Node fieldNode = null;
	private MethodDeclarationAnnotation mann = null;

	// boolean exp
	public Stmt trueBranch = null;
	public Stmt falseBranch = null;

	// new for each method & constructor body
	private NameGenerator nameGen = null;
	private NameGenerator qtNameGen = null; //This line is robbyjo's patch
	private Hashtable currentLocals = null;
	private Hashtable currentLocalNames = null;
	private Hashtable currentLocalNamesIterator = null;
	private Vector currentStmts = null;
	private ca.mcgill.sable.soot.jimple.Value currentValue = null;
	private ca.mcgill.sable.soot.Type currentType = null;
	private ca.mcgill.sable.soot.jimple.Local currentThisLocal = null;
	private JimpleBody currentBody = null;
	private String currentName = null;
	private Vector currentTraps = null;

	// initializers
	private Hashtable staticLocals = null;
	private Vector staticStmts = null;
	private Hashtable instanceLocals = null;
	private Vector instanceStmts = null;
	private ca.mcgill.sable.soot.SootMethod clinit = null;
	private JimpleBody clinitBody = null;
	private ca.mcgill.sable.soot.SootMethod init = null;
	private JimpleBody initBody = null;
	private int staticInitializers = 0;
	private int instanceInitializers = 0;

	// switch
	private LinkedList currentLookupValues = null;
	private LinkedList currentTargets = null;
	private Stmt currentDefaultStmt = null;
	private boolean defaultSwitch = false;

	// try catch /finally
	private TryStmtAnnotation currentTryAnnotation = null;
	private Stmt beginTrapStmt = null;
	private Stmt endTrapStmt = null;
	private Stmt endTryStmt = null;
	private boolean catchHasEnd;

	// label
	private LinkedList labels = null;
	private Hashtable currentLabels = null;
	private Stmt currentBreakTarget = null;
	private Stmt currentContinueTarget = null;

	// for
	private Hashtable forLocals = null;
	private ca.mcgill.sable.soot.Type forInitType = null;
	private int forInitModifiers = 0;

	// break/continue
	private Vector finallyAndSynch = new Vector();
	private Object lastFinallySynchBeforeControl = null;

	private ca.mcgill.sable.soot.SootMethod assertMethod;
	private ca.mcgill.sable.soot.SootMethod chooseMethod;

	private int methodLine;
	/**
	 * 
	 * @param symbolTable edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
	 * @param sootClassManager ca.mcgill.sable.soot.SootClassManager
	 * @param docComments java.util.Hashtable
	 */
	public JIJCCodeGenerator(SymbolTable symbolTable, SootClassManager sootClassManager,
			AnnotationManager annotationManager, Hashtable docComments) {
		this.symbolTable = symbolTable;
		this.scm = sootClassManager;
		this.am = annotationManager;
		this.docComments = docComments;

		symbolTable.setCurrentPackage(null);
		symbolTable.setCurrentClassOrInterfaceType(null);
		while (symbolTable.getNumScopeLevels() > 0) {
			symbolTable.exitScope();
		}
		/*	if (!scm.managesClass("Bandera")) {
		SootClass sc = new SootClass("Bandera", ca.mcgill.sable.soot.Modifier.PUBLIC);

		LinkedList parms = new LinkedList();
		chooseMethod = new SootMethod("choose", parms, ca.mcgill.sable.soot.BooleanType.v(),
				ca.mcgill.sable.soot.Modifier.PUBLIC	| ca.mcgill.sable.soot.Modifier.STATIC);

		sc.addMethod(chooseMethod);

		parms = new LinkedList();
		parms.addLast(ca.mcgill.sable.soot.BooleanType.v());
		assertMethod = new SootMethod("assert", parms, ca.mcgill.sable.soot.VoidType.v(),
				ca.mcgill.sable.soot.Modifier.PUBLIC	| ca.mcgill.sable.soot.Modifier.STATIC);

		sc.addMethod(assertMethod);

		scm.addClass(sc);
	} else {*/
		//SootClass sc = scm.getClass("Bandera");
		//chooseMethod = sc.getMethod("choose");
		//assertMethod = sc.getMethod("assert");
		//}
	}
	/**
	 * 
	 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
	 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
	 */
	private void addRetIfNecessary(JimpleBody body, ca.mcgill.sable.soot.Type retType) {
		if (body.declaresLocal("$ret")) return;

		ca.mcgill.sable.soot.jimple.Local ret = jimple.newLocal("$ret", retType);
		body.addLocal(ret);

		Object[] stmts = body.getStmtList().toArray();
		for (int i = 0; i < stmts.length; i++) {
			if (stmts[i] instanceof ReturnStmt) {
				ReturnStmt rs = (ReturnStmt) stmts[i];
				ca.mcgill.sable.soot.jimple.Local lcl = (ca.mcgill.sable.soot.jimple.Local) rs.getReturnValue();
				for (int j = i - 1; j >= 0; j--) {
					if (stmts[j] instanceof AssignStmt) {
						AssignStmt as = (AssignStmt) stmts[j];
						if (as.getLeftOp() == lcl) {
							as.setLeftOp(ret);
							break;
						}
					}
				}
				rs.setReturnValue(ret);
			}
		}
		return;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AAbstractMethodDeclarationInterfaceMemberDeclaration
	 */
	public void caseAAbstractMethodDeclarationInterfaceMemberDeclaration(AAbstractMethodDeclarationInterfaceMemberDeclaration node) {
		methodLine = LineExtractor.extractLine(node);

		// prepare variables for method processing
		nameGen = new NameGenerator();
		currentLocalNames = new Hashtable();
		currentLocalNamesIterator = new Hashtable();
		currentLocals = new Hashtable();
		currentStmts = new Vector();
		currentValue = null;
		currentType = null;
		currentName = null;
		currentThisLocal = null;

		currentTraps = new Vector();

		// apply method header & body
		symbolTable.enterScope();
		currentSootMethod = null;
		currentAnnotation = mann = new MethodDeclarationAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		currentAnnotation.setFirstLine(lc.getFirstLine());
		currentAnnotation.setFirstColumn(lc.getFirstColumn());
		currentAnnotation.setLastLine(lc.getLastLine());
		currentAnnotation.setLastColumn(lc.getLastColumn());

		//isInterface = true;
		node.getMethodHeader().apply(this);
		//isInterface = false;
		symbolTable.exitScope();

		for (int i = 0; i < currentStmts.size(); i++) {
			Stmt s = (Stmt) currentStmts.elementAt(i);
		}

		/*
	am.addAnnotation(currentSootClass, currentSootMethod, currentAnnotation);
	CompilationManager.addDocTriple(new DocTriple(currentSootClass, currentSootMethod, DocProcessor.tags(node, docComments)));
	// create the actual body
	StmtList stmts = currentBody.getStmtList();
	for (Enumeration e = currentLocals.elements(); e.hasMoreElements();) {
		currentBody.addLocal((ca.mcgill.sable.soot.jimple.Local) e.nextElement());
	}

	for (int i = 0; i < currentStmts.size(); i++) {
		stmts.add((Stmt) currentStmts.elementAt(i));
	}

	Stmt tempStmt = null;
	if (currentStmts.size() > 0) {
		int size = currentStmts.size();
		Stmt lastStmt = (Stmt) currentStmts.elementAt(--size);
		if (!(lastStmt instanceof JReturnStmt) && !(lastStmt instanceof JReturnVoidStmt)
				&& !(lastStmt instanceof JThrowStmt)) {
			tempStmt = jimple.newReturnVoidStmt();
			stmts.add(tempStmt);
			SequentialAnnotation ann = new SequentialAnnotation(null);
			ann.addStmt(tempStmt);
			currentAnnotation.addAnnotation(ann);
		}
	} else {
		SequentialAnnotation ann = new SequentialAnnotation(null);
		Stmt stmt = jimple.newReturnVoidStmt();
		ann.addStmt(stmt);
		stmts.add(stmt);
		currentAnnotation.addAnnotation(ann);
	}

	for (Enumeration e = currentTraps.elements(); e.hasMoreElements();) {
		currentBody.addTrap((ca.mcgill.sable.soot.jimple.Trap) e.nextElement());
	}


	// store body
	if (!(currentSootMethod.getReturnType() instanceof ca.mcgill.sable.soot.VoidType)) {
		addRetIfNecessary(currentBody, currentSootMethod.getReturnType());
	}
	convertLocalTypes(currentBody);
	TemporaryLocalsReduction.reduce(currentBody, currentAnnotation);
	DeadCodeElimination.eliminate(currentBody);
	JumpElimination.eliminate(currentBody);
	DeadCodeEliminator.eliminateDeadCode(currentBody);
	DeadCodeElimination.eliminate(currentBody);
	Transformations.removeUnusedLocals(currentBody);
	patchBody(currentBody);
	//Transformations.cleanupCode(currentBody);

	currentAnnotation.validate(currentBody);
	//checkAnnotation(currentAnnotation, currentBody);
	currentSootMethod.storeBody(jimple, currentBody);
		 */
		am.addAnnotation(currentSootClass, currentSootMethod, currentAnnotation);
		am.putFilenameLinePairAnnotation(new FilenameLinePair(currentClassOrInterfaceType.getPath(),
				LineExtractor.extractLine(node)), currentAnnotation);

		CompilationManager.addDocTriple(new DocTriple(currentSootClass, currentSootMethod, DocProcessor.tags(node, docComments)));


		// cleaning up
		nameGen = null;
		currentLocals = null;
		currentLocalNames = null;
		currentLocalNamesIterator = null;
		currentStmts = null;
		currentValue = null;
		currentType = null;
		currentName = null;
		currentThisLocal = null;
		currentBody = null;
		labels = null;
		currentTraps = null;
		currentAnnotation = null;
		mann = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AArrayInitializer
	 */
	public void caseAArrayInitializer(AArrayInitializer node) {
		Object temp[] = node.getVariableInitializer().toArray();

		ca.mcgill.sable.soot.jimple.Value leftValue = currentValue;

		if (!(leftValue instanceof ca.mcgill.sable.soot.jimple.Local)) {
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), leftValue.getType(), 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, leftValue));
			leftValue = lcl;
		}

		ca.mcgill.sable.soot.ArrayType type = (ca.mcgill.sable.soot.ArrayType) leftValue.getType();
		ca.mcgill.sable.soot.jimple.Value v = jimple.newNewArrayExpr(ca.mcgill.sable.soot.ArrayType.v(type.baseType, type.numDimensions - 1),
				IntConstant.v(temp.length));

		currentStmts.addElement(jimple.newAssignStmt(leftValue, v));

		for (int i = 0; i < temp.length; i++) {
			if (temp[i] instanceof AArrayVariableInitializer) {
				currentValue = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.ArrayType.v(type.baseType, type.numDimensions - 1), 0);
			}
			((PVariableInitializer) temp[i]).apply(this);
			currentStmts.addElement(jimple.newAssignStmt(jimple.newArrayRef(leftValue, IntConstant.v(i)), currentValue));
		}
		currentValue = leftValue;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AAssignedVariableDeclarator
	 */
	public void caseAAssignedVariableDeclarator(AAssignedVariableDeclarator node) {
		node.getVariableDeclaratorId().apply(this);
		if (currentType instanceof ca.mcgill.sable.soot.BooleanType) {
			if (symbolTable.getNumScopeLevels() > 0) {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(currentName, currentType, 0);
				if (currentAnnotation == null) {
					forLocals.put(currentName, lcl);
				} else {
					localAnnotation.addDeclaredLocal(currentName, lcl);
				}
				currentValue = lcl;
				Stmt endStmt = jimple.newNopStmt();
				Stmt prevTrueBranch = trueBranch;
				Stmt prevFalseBranch = falseBranch;
				trueBranch = jimple.newAssignStmt(lcl, IntConstant.v(1));
				falseBranch = jimple.newAssignStmt(lcl, IntConstant.v(0));
				Node n = (Node) node.getVariableInitializer().clone();
				PushComplement.push(n);
				n.apply(new BooleanExpression(this, trueBranch, falseBranch));
				currentStmts.addElement(falseBranch);
				currentStmts.addElement(jimple.newGotoStmt(endStmt));
				currentStmts.addElement(trueBranch);
				currentStmts.addElement(endStmt);
				trueBranch = prevTrueBranch;
				falseBranch = prevFalseBranch;
			} else {
				int start = currentStmts.size();
				String fieldName = currentName;
				nameFieldOrLocalExp(true, currentName);
				ca.mcgill.sable.soot.jimple.Value field = currentValue;
				Stmt endStmt = jimple.newNopStmt();
				Stmt prevTrueBranch = trueBranch;
				Stmt prevFalseBranch = falseBranch;
				trueBranch = jimple.newAssignStmt(field, IntConstant.v(1));
				falseBranch = jimple.newAssignStmt(field, IntConstant.v(0));
				Node n = (Node) node.getVariableInitializer().clone();
				PushComplement.push(n);
				n.apply(new BooleanExpression(this, trueBranch, falseBranch));
				currentStmts.addElement(falseBranch);
				currentStmts.addElement(jimple.newGotoStmt(endStmt));
				currentStmts.addElement(trueBranch);
				currentStmts.addElement(endStmt);
				trueBranch = prevTrueBranch;
				falseBranch = prevFalseBranch;

				int end = currentStmts.size();
				FieldDeclarationAnnotation ann = new FieldDeclarationAnnotation(fieldNode);
				LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
				ann.setFirstLine(lc.getFirstLine());
				ann.setFirstColumn(lc.getFirstColumn());
				ann.setLastLine(lc.getLastLine());
				ann.setLastColumn(lc.getLastColumn());

				for (int i = start; i < end; i++) {
					ann.addStmt((Stmt) currentStmts.elementAt(i));
				}

				try {
					ca.mcgill.sable.soot.SootField sf = getSootField(currentSootClass.getName(), fieldName);
					am.addAnnotation(currentSootClass, sf, ann);
					ann.setSootField(sf);
					ann.setField(currentClassOrInterfaceType.getField(new Name(fieldName)));
					am.putFilenameLinePairAnnotation(
							new FilenameLinePair(currentClassOrInterfaceType.getPath(),
									LineExtractor.extractLine(node.getVariableDeclaratorId())),
							ann);
				} catch (CompilerException e) {}
			}
		} else {
			if (symbolTable.getNumScopeLevels() > 0) {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(currentName, currentType, 0);
				if (currentAnnotation == null) {
					forLocals.put(currentName, lcl);
				} else {
					localAnnotation.addDeclaredLocal(currentName, lcl);
				}
				currentValue = lcl;
				node.getVariableInitializer().apply(this);
				currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
			} else {
				int start = currentStmts.size();

				String fieldName = currentName;

				nameFieldOrLocalExp(true, currentName);
				ca.mcgill.sable.soot.jimple.Value field = currentValue;
				node.getVariableInitializer().apply(this);
				currentStmts.addElement(jimple.newAssignStmt(field, currentValue));

				int end = currentStmts.size();
				FieldDeclarationAnnotation ann = new FieldDeclarationAnnotation(fieldNode);

				for (int i = start; i < end; i++) {
					ann.addStmt((Stmt) currentStmts.elementAt(i));
				}

				try {
					ca.mcgill.sable.soot.SootField sf = getSootField(currentSootClass.getName(), fieldName);
					am.addAnnotation(currentSootClass, sf, ann);
					ann.setSootField(sf);
					ann.setField(currentClassOrInterfaceType.getField(new Name(fieldName)));
					am.putFilenameLinePairAnnotation(
							new FilenameLinePair(currentClassOrInterfaceType.getPath(),
									LineExtractor.extractLine(node.getVariableDeclaratorId())),
							ann);
				} catch (CompilerException e) {}
			}
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AAssignmentExp
	 */
	public void caseAAssignmentExp(AAssignmentExp node) {
		String operator = node.getAssignmentOperator().toString().trim();
		node.getLeftHandSide().apply(this);
		ca.mcgill.sable.soot.jimple.Value firstValue = currentValue;

		if ((firstValue instanceof ca.mcgill.sable.soot.BooleanType) && "=".equals(operator)) {
			Stmt endStmt = jimple.newNopStmt();
			Stmt prevTrueBranch = trueBranch;
			Stmt prevFalseBranch = falseBranch;
			trueBranch = jimple.newAssignStmt(firstValue, IntConstant.v(1));
			falseBranch = jimple.newAssignStmt(firstValue, IntConstant.v(0));
			Node n = (Node) node.getExp().clone();
			PushComplement.push(n);
			if (node.getExp() instanceof AUnaryExp)
				if ("!".equals(((AUnaryExp) node.getExp()).getUnaryOperator().toString().trim()))
					n = ((AUnaryExp) n).getExp();
			n.apply(new BooleanExpression(this, trueBranch, falseBranch));
			currentStmts.addElement(falseBranch);
			currentStmts.addElement(jimple.newGotoStmt(endStmt));
			currentStmts.addElement(trueBranch);
			currentStmts.addElement(endStmt);
			currentValue = firstValue;
			trueBranch = prevTrueBranch;
			falseBranch = prevFalseBranch;
			if (!(currentValue instanceof ca.mcgill.sable.soot.jimple.Local) || !(currentValue instanceof Constant)) {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
				currentValue = lcl;
			}
			return;
		}

		node.getExp().apply(this);
		ca.mcgill.sable.soot.jimple.Value secondValue = currentValue;
		
		currentValue = null;
		if ("=".equals(operator)) {
			currentStmts.addElement(jimple.newAssignStmt(firstValue, secondValue));
			currentValue = firstValue;
			/* [Thomas, May 22, 2017]
			 * */
//			if (!(currentValue instanceof ca.mcgill.sable.soot.jimple.Local) || !(currentValue instanceof Constant)) {
//				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
//				currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
//				currentValue = lcl;
//			}
		} else {
			operator = operator.substring(0, operator.length() - 1);
			ca.mcgill.sable.soot.jimple.Value resultValue = firstValue;
			if (!(firstValue instanceof ca.mcgill.sable.soot.jimple.Local)) {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), firstValue.getType(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, firstValue));
				firstValue = lcl;
			}
			try {
				numericOperation(operator, firstValue, secondValue);
				if (currentValue == null) {
					exceptions.addElement(new CompilerException("Cannot resolve " + node.getLeftHandSide().toString()));
					System.out.println("Cannot resolve " + node.getLeftHandSide().toString());
					return;
				}
				currentStmts.addElement(jimple.newAssignStmt(resultValue, currentValue));
				currentValue = resultValue;
				if (!(currentValue instanceof ca.mcgill.sable.soot.jimple.Local) || !(currentValue instanceof Constant)) {
					ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
					currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
					currentValue = lcl;
				}
			} catch (CompilerException e) {
				exceptions.addElement(e);
				System.out.println(e.getMessage());
			}
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABinaryExp
	 */
	public void caseABinaryExp(ABinaryExp node) {
		node = (ABinaryExp) node.clone();
		TrivialExpression.optimize(node);

		PBinaryOperator op = node.getBinaryOperator();
		Stmt prevTrueBranch = trueBranch;
		Stmt prevFalseBranch = falseBranch;

		if ((op instanceof AOrBinaryOperator) || (op instanceof AAndBinaryOperator)) {
			ca.mcgill.sable.soot.jimple.Local lcl = null;
			if (trueBranch == null) {
				lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.BooleanType.v(), 0);
				trueBranch = jimple.newAssignStmt(lcl, IntConstant.v(1));
				falseBranch = jimple.newAssignStmt(lcl, IntConstant.v(0));
			}

			Node n = (Node) node.clone();
			PushComplement.push(n);
			n.apply(new BooleanExpression(this, trueBranch, falseBranch));

			if (lcl != null) {
				Stmt endStmt = jimple.newNopStmt();
				currentStmts.addElement(falseBranch);
				currentStmts.addElement(jimple.newGotoStmt(endStmt));
				currentStmts.addElement(trueBranch);
				currentStmts.addElement(endStmt);
				trueBranch = prevTrueBranch;
				falseBranch = prevFalseBranch;
				currentValue = lcl;
			}
			return;
		}
		
		/* [Thomas, June 6, 2017]
		 * test code
		 * */
		{
			if(node.getSecond() instanceof ANameExp)
			{
				if(((ANameExp)node.getSecond()).getName() instanceof ASimpleName)
				{
					String name = ((ASimpleName)((ANameExp)node.getSecond()).getName()).getId().getText();
				}
			}
		}

		node.getFirst().apply(this);
		ca.mcgill.sable.soot.jimple.Value firstValue = currentValue;
		if (!(firstValue instanceof ca.mcgill.sable.soot.jimple.Local) && !(firstValue instanceof Constant)) {
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), firstValue.getType(), 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, firstValue));
			firstValue = lcl;
		}
		node.getSecond().apply(this);
		ca.mcgill.sable.soot.jimple.Value secondValue = currentValue;
		currentValue = null;
		if (!(secondValue instanceof ca.mcgill.sable.soot.jimple.Local) && !(secondValue instanceof Constant)) {
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), secondValue.getType(), 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, secondValue));
			secondValue = lcl;
		}

		if (op instanceof AGtBinaryOperator || op instanceof ALtBinaryOperator || op instanceof ALteqBinaryOperator || op instanceof AGteqBinaryOperator || op instanceof AEqBinaryOperator || op instanceof ANeqBinaryOperator) {
			// >  <  <=  >=  == !=
			ca.mcgill.sable.soot.jimple.Local lcl = null;
			if (trueBranch == null) {
				lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.BooleanType.v(), 0);
				trueBranch = jimple.newAssignStmt(lcl, IntConstant.v(1));
				falseBranch = jimple.newAssignStmt(lcl, IntConstant.v(0));
			}

			if ((firstValue.getType() instanceof ca.mcgill.sable.soot.DoubleType)
					|| (secondValue.getType() instanceof ca.mcgill.sable.soot.DoubleType)
					|| (firstValue.getType() instanceof ca.mcgill.sable.soot.FloatType)
					|| (secondValue.getType() instanceof ca.mcgill.sable.soot.FloatType)) {
				if (op instanceof AGtBinaryOperator) {
					currentValue = jimple.newCmpgExpr(firstValue, secondValue);
				} else if (op instanceof ALtBinaryOperator) {
					currentValue = jimple.newCmplExpr(firstValue, secondValue);
				} else if (op instanceof ALteqBinaryOperator) {
					currentValue = jimple.newCmplExpr(firstValue, secondValue);
				} else if (op instanceof AGteqBinaryOperator) {
					currentValue = jimple.newCmpgExpr(firstValue, secondValue);
				} else if (op instanceof AEqBinaryOperator) {
					currentValue = jimple.newCmplExpr(firstValue, secondValue);
				} else if (op instanceof ANeqBinaryOperator) {
					currentValue = jimple.newCmplExpr(firstValue, secondValue);
				}
				ca.mcgill.sable.soot.jimple.Local local = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.IntType.v(), 0);
				currentStmts.addElement(jimple.newAssignStmt(local, currentValue));
				firstValue = local;
				secondValue = IntConstant.v(0);
			}

			if (op instanceof AGtBinaryOperator) {
				currentValue = jimple.newGtExpr(firstValue, secondValue);
			} else if (op instanceof ALtBinaryOperator) {
				currentValue = jimple.newLtExpr(firstValue, secondValue);
			} else if (op instanceof ALteqBinaryOperator) {
				currentValue = jimple.newLeExpr(firstValue, secondValue);
			} else if (op instanceof AGteqBinaryOperator) {
				currentValue = jimple.newGeExpr(firstValue, secondValue);
			} else if (op instanceof AEqBinaryOperator) {
				currentValue = jimple.newEqExpr(firstValue, secondValue);
			} else if (op instanceof ANeqBinaryOperator) {
				currentValue = jimple.newNeExpr(firstValue, secondValue);
			}

			currentStmts.addElement(jimple.newIfStmt(currentValue, trueBranch));

			if (lcl != null) {
				Stmt endStmt = jimple.newNopStmt();
				currentStmts.addElement(falseBranch);
				currentStmts.addElement(jimple.newGotoStmt(endStmt));
				currentStmts.addElement(trueBranch);
				currentStmts.addElement(endStmt);
				trueBranch = prevTrueBranch;
				falseBranch = prevFalseBranch;
				currentValue = lcl;
			}
		} else if (op instanceof APlusBinaryOperator || op instanceof AMinusBinaryOperator || op instanceof AModBinaryOperator || op instanceof ADivBinaryOperator || op instanceof AStarBinaryOperator || op instanceof ABitXorBinaryOperator || op instanceof ABitAndBinaryOperator || op instanceof AShiftLeftBinaryOperator || op instanceof ASignedShiftRightBinaryOperator || op instanceof AUnsignedShiftRightBinaryOperator || op instanceof ABitOrBinaryOperator) {
			//  +  -  %  /  *  ^  &  <<  >>  >>>  |
			try {
				numericOperation(op.toString().trim(), firstValue, secondValue);
			} catch (CompilerException e) {
				exceptions.addElement(e);
				System.out.println(e.getMessage());
			}	
		} else {
			exceptions.addElement(new CompilerException("Unknown binary operator " + op));
			System.out.println("Unknown binary operator " + op);
			currentValue = null;
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABlock
	 */
	public void caseABlock(ABlock node) {
		if (!(node.parent() instanceof ABlockMethodBody)) {
			symbolTable.enterScope();
		}
		labelEnterBlock();
		for (Iterator i = node.getBlockedStmt().iterator(); i.hasNext();) {
			((PBlockedStmt) i.next()).apply(this);
		}
		labelExitBlock();
		if (!(node.parent() instanceof ABlockMethodBody)) {
			symbolTable.exitScope();
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABlockClassBodyDeclaration
	 */
	public void caseABlockClassBodyDeclaration(ABlockClassBodyDeclaration node) {
		if (init == null) {
			try {
				init = getSootMethod(currentSootClass.getName(), "<init>", new Vector());
				init.setReturnType(ca.mcgill.sable.soot.VoidType.v());
				init.setModifiers(ca.mcgill.sable.soot.Modifier.PUBLIC);
				initBody = (JimpleBody) jimple.newBody(init);
			} catch (CompilerException e) {
				exceptions.addElement(e);
				System.out.println(e.getMessage());
				return;
			}
		}
		currentSootMethod = init;
		currentBody = initBody;
		currentLocalNames = new Hashtable();
		currentLocalNamesIterator = new Hashtable();
		currentLocals = instanceLocals;
		currentStmts = instanceStmts;
		currentValue = null;
		currentType = null;
		currentName = null;
		labels = new LinkedList();
		currentAnnotation = new InstanceInitializerAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		currentAnnotation.setFirstLine(lc.getFirstLine());
		currentAnnotation.setFirstColumn(lc.getFirstColumn());
		currentAnnotation.setLastLine(lc.getLastLine());
		currentAnnotation.setLastColumn(lc.getLastColumn());
		currentTraps = new Vector();

		node.getBlock().apply(this);

		for (Enumeration e = currentTraps.elements(); e.hasMoreElements();) {
			initBody.addTrap((JTrap) e.nextElement());
		}

		currentLocals = null;
		currentLocalNames = null;
		currentLocalNamesIterator = null;
		currentStmts = null;
		currentValue = null;
		currentType = null;
		currentName = null;
		currentBody = null;
		labels = null;
		currentTraps = null;
		currentSootMethod = null;

		am.addAnnotation(currentSootClass, "instance initializer " + instanceInitializers++, currentAnnotation);
		currentAnnotation = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABlockMethodBody
	 */
	public void caseABlockMethodBody(ABlockMethodBody node) {
		node.getBlock().apply(this);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABlockStmt
	 */
	public void caseABlockStmt(ABlockStmt node) {
		if(node.getBlock() != null) {
			BlockStmtAnnotation prevAnnotation = currentAnnotation;
			currentAnnotation = new BlockStmtAnnotation(node);
			LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
			currentAnnotation.setFirstLine(lc.getFirstLine());
			currentAnnotation.setFirstColumn(lc.getFirstColumn());
			currentAnnotation.setLastLine(lc.getLastLine());
			currentAnnotation.setLastColumn(lc.getLastColumn());
			am.putFilenameLinePairAnnotation(
					new FilenameLinePair(currentClassOrInterfaceType.getPath(),
							LineExtractor.extractLine(node)),
					currentAnnotation);
			node.getBlock().apply(this);
			if (prevAnnotation != null) {
				prevAnnotation.addAnnotation(currentAnnotation);
				currentAnnotation = prevAnnotation;
			}
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABooleanPrimitiveType
	 */
	public void caseABooleanPrimitiveType(ABooleanPrimitiveType node) {
		currentType = ca.mcgill.sable.soot.BooleanType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABreakStmt
	 */
	public void caseABreakStmt(ABreakStmt node) {
		int start = currentStmts.size();

		if (node.getId() != null) {
			ca.mcgill.sable.soot.jimple.Unit target = getLabelUnit(node.getId().toString().trim());
			if (target == null) {
				exceptions.addElement(new CompilerException("Invalid break statement..."));
				System.out.println("Invalid break statement...");
				return;
			}
			Object lastFinallySynchBeforeControl = ((Vector) getLabelObject(target)).firstElement();
			for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
				Object o = e.nextElement();
				if (o == lastFinallySynchBeforeControl) break;
				if (o instanceof Node) {
					((Node) o).apply(this);
				} else {
					SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) o;
					Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
					sann.addExitMonitor(exitMon);
					currentStmts.addElement(exitMon);
				}
			}
			currentStmts.addElement(jimple.newGotoStmt(target));
		} else {
			if (currentBreakTarget == null) {
				exceptions.addElement(new CompilerException("Invalid break statement..."));
				System.out.println("Invalid break statement...");
				return;
			}
			for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
				Object o = e.nextElement();
				if (o == lastFinallySynchBeforeControl) break;
				if (o instanceof Node) {
					((Node) o).apply(this);
				} else {
					SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) o;
					Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
					sann.addExitMonitor(exitMon);
					currentStmts.addElement(exitMon);
				}
			}
			currentStmts.addElement(jimple.newGotoStmt(currentBreakTarget));
		}

		int end = currentStmts.size();
		BreakStmtAnnotation ann = new BreakStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}
		currentAnnotation.addAnnotation(ann); 
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ABytePrimitiveType
	 */
	public void caseABytePrimitiveType(ABytePrimitiveType node) {
		currentType = ca.mcgill.sable.soot.ByteType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ACaseSwitchLabel
	 */
	public void caseACaseSwitchLabel(ACaseSwitchLabel node) {
		ca.mcgill.sable.soot.jimple.Value tempValue = currentValue;
		TrivialExpression.optimize(node.getExp(), true);
		node.getExp().apply(this);
		if ((currentValue == null) || !isNumberConstant(currentValue)) {
			exceptions.addElement(new CompilerException("Cannot have " + node.getExp().toString() + " as a switch case"));
			System.out.println("Cannot have " + node.getExp().toString() + " as a switch case");
			return;
		}
		if (currentValue instanceof LongConstant) {
			currentValue = IntConstant.v((int) ((LongConstant) currentValue).value);
		}
		currentLookupValues.addLast(new Integer(((IntConstant) currentValue).value));
		currentValue = tempValue;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ACatchClause
	 */
	public void caseACatchClause(ACatchClause node) {
		BlockStmtAnnotation prevAnnotation = currentAnnotation;
		try {
			symbolTable.enterScope();
			labelEnterBlock();
			PFormalParameter formalParameter = (PFormalParameter) node.getFormalParameter();
			formalParameter.apply(this);
			ca.mcgill.sable.soot.jimple.Local tempLocal = declareLocal(((AVariableDeclaratorId)
					((AFormalParameter) formalParameter).getVariableDeclaratorId()).getId().toString().trim(), currentType, 0);

			Stmt handlerStmt = jimple.newIdentityStmt(tempLocal, jimple.newCaughtExceptionRef(currentBody));
			currentStmts.addElement(handlerStmt);

			currentAnnotation = new CatchAnnotation(node);
			LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
			currentAnnotation.setFirstLine(lc.getFirstLine());
			currentAnnotation.setFirstColumn(lc.getFirstColumn());
			currentAnnotation.setLastLine(lc.getLastLine());
			currentAnnotation.setLastColumn(lc.getLastColumn());
			am.putFilenameLinePairAnnotation(
					new FilenameLinePair(currentClassOrInterfaceType.getPath(),
							LineExtractor.extractLine(node)),
					currentAnnotation);
			node.getBlock().apply(this);

			SequentialAnnotation sann = new SequentialAnnotation(null);
			sann.addStmt(handlerStmt);
			currentAnnotation.insertAnnotationAt(sann, 0);

			Stmt lastStmt = (Stmt) currentStmts.lastElement();


			if (!((lastStmt instanceof ReturnStmt) || (lastStmt instanceof ReturnVoidStmt)
					|| (lastStmt instanceof ThrowStmt) || (lastStmt instanceof GotoStmt))) {
				sann = new SequentialAnnotation(null);
				Stmt endCatchStmt = jimple.newGotoStmt(endTryStmt);
				sann.addStmt(endCatchStmt);
				currentStmts.addElement(endCatchStmt);
				currentAnnotation.addAnnotation(sann);
			} else {
				catchHasEnd = false;
			}


			String className = ((ca.mcgill.sable.soot.RefType) tempLocal.getType()).className;
			ca.mcgill.sable.soot.SootClass sc = getSootClass(className);
			ClassOrInterfaceType exceptionType = symbolTable.resolveClassOrInterfaceType(new Name(className));
			Name throwableName = new Name("java.lang.Throwable");
			if (!(exceptionType.hasSuperClass(throwableName) || throwableName.equals(exceptionType.getName()))) {
				throw new NotThrowableException("Cannot have an unthrowable exception type named '" + className + "'");
			}
			currentTraps.addElement(jimple.newTrap(sc, beginTrapStmt, endTrapStmt, handlerStmt));
			symbolTable.exitScope();
			labelExitBlock();
			currentTryAnnotation.addCatchClauseAnnotation(currentAnnotation);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
		currentAnnotation = prevAnnotation;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ACharacterLiteralLiteral
	 */
	public void caseACharacterLiteralLiteral(ACharacterLiteralLiteral node) {
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
		integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.CharType.TYPE;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ACharPrimitiveType
	 */
	public void caseACharPrimitiveType(ACharPrimitiveType node) {
		currentType = ca.mcgill.sable.soot.CharType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AClassBody
	 */
	public void caseAClassBody(AClassBody node) {
		// prepare variables for field processing
		nameGen = new NameGenerator();
		currentLocalNames = new Hashtable();
		currentLocalNamesIterator = new Hashtable();
		currentLocals = new Hashtable();
		currentStmts = new Vector();
		currentValue = null;
		currentType = null;
		currentName = null;
		staticLocals = new Hashtable();
		staticStmts = new Vector();
		instanceLocals = new Hashtable();
		instanceStmts = new Vector();
		currentThisLocal = jimple.newLocal(nameGen.newName(), ca.mcgill.sable.soot.RefType.v(currentSootClass.getName()));
		instanceLocals.put(currentThisLocal.getName(), currentThisLocal);
		instanceStmts.addElement(jimple.newIdentityStmt(currentThisLocal, jimple.newThisRef(currentSootClass)));
		LinkedList ClassBodyDeclList = node.getClassBodyDeclaration();
		PClassBodyDeclaration classBodyDecl;
		if (ClassBodyDeclList.size() > 0) {
			classBodyDecl = (PClassBodyDeclaration) ClassBodyDeclList.getFirst();
			for (Iterator i = ClassBodyDeclList.iterator(); i.hasNext();) {
				classBodyDecl = (PClassBodyDeclaration) i.next();
				if (classBodyDecl instanceof AFieldClassBodyDeclaration)
					classBodyDecl.apply(this);
			}
			for (Iterator i = ClassBodyDeclList.iterator(); i.hasNext();) {
				classBodyDecl = (PClassBodyDeclaration) i.next();
				if (classBodyDecl instanceof AStaticInitializerClassBodyDeclaration)
					classBodyDecl.apply(this);
			}
			for (Iterator i = ClassBodyDeclList.iterator(); i.hasNext();) {
				classBodyDecl = (PClassBodyDeclaration) i.next();
				if (classBodyDecl instanceof ABlockClassBodyDeclaration)
					classBodyDecl.apply(this);
			}
		}
		nameGen = null;
		boolean generateConstructor = false;
		ca.mcgill.sable.soot.jimple.Local thisLocal = currentThisLocal;

		// if there is no constructor declaration, indicate to generate a default one.
		if (ClassBodyDeclList.size() > 0) {
			classBodyDecl = (PClassBodyDeclaration) ClassBodyDeclList.getFirst();
			for (Iterator i = ClassBodyDeclList.iterator(); i.hasNext();) {
				classBodyDecl = (PClassBodyDeclaration) i.next();
				if (classBodyDecl instanceof AConstructorClassBodyDeclaration)
					break;
			}

			// generate default instance constructor if necessary
			if (!(classBodyDecl instanceof AConstructorClassBodyDeclaration)) {
				generateConstructor = true;
			}

			// method or constructor declarations
			currentLocals = null;
			currentStmts = null;
			currentThisLocal = null;
			currentLocalNames = null;
			currentLocalNamesIterator = null;
			for (Iterator i = ClassBodyDeclList.iterator(); i.hasNext();) {
				classBodyDecl = (PClassBodyDeclaration) i.next();
				if (!(classBodyDecl instanceof AFieldClassBodyDeclaration) && !(classBodyDecl instanceof ABlockClassBodyDeclaration) && !(classBodyDecl instanceof AStaticInitializerClassBodyDeclaration))
					classBodyDecl.apply(this);
			}
		} else
			generateConstructor = true;

		// generate instance constructor if necessary
		if (generateConstructor) {
			try {
				ca.mcgill.sable.soot.SootMethod sootMethod;
				JimpleBody body;
				if (init == null) {
					sootMethod = getSootMethod(currentSootClass.getName(), "<init>", new Vector());
					sootMethod.setReturnType(ca.mcgill.sable.soot.VoidType.v());
					sootMethod.setModifiers(ca.mcgill.sable.soot.Modifier.PUBLIC);
					body = (JimpleBody) jimple.newBody(sootMethod);
				} else {
					sootMethod = init;
					body = initBody;
				}

				SequentialAnnotation sann = new SequentialAnnotation(null);
				StmtList stmts = body.getStmtList();
				for (Enumeration e = instanceLocals.elements(); e.hasMoreElements();) {
					body.addLocal((ca.mcgill.sable.soot.jimple.Local) e.nextElement());
				}
				for (int i = 0; i < instanceStmts.size(); i++) {
					sann.addStmt((Stmt) instanceStmts.elementAt(i));
					stmts.add(instanceStmts.elementAt(i));
					if ((i == 0) && (!currentSootClass.getName().equals("java.lang.Object"))) {
						try {
							ca.mcgill.sable.soot.SootClass sc = currentSootClass.getSuperClass();
							ca.mcgill.sable.soot.SootMethod sm = getSootMethod(sc.getName(), "<init>", new Vector());
							Stmt stmt = jimple.newInvokeStmt(jimple.newSpecialInvokeExpr(thisLocal, sm, new LinkedList()));
							stmts.add(stmt);
							sann.addStmt(stmt);
						} catch (NoSuperClassException nsce) {
						}
					}
				}
				Stmt stmt = jimple.newReturnVoidStmt();
				stmts.add(stmt);
				sann.addStmt(stmt);

				ConstructorDeclarationAnnotation bann = new ConstructorDeclarationAnnotation(null);
				bann.addAnnotation(sann);
				bann.setSootMethod(sootMethod);
				bann.setConstructor(currentClassOrInterfaceType.getConstructor(new Vector()));
				am.addAnnotation(currentSootClass, sootMethod, bann);

				TemporaryLocalsReduction.reduce(body, bann);
				DeadCodeElimination.eliminate(body);
				JumpElimination.eliminate(body);
				DeadCodeEliminator.eliminateDeadCode(body);
				Transformations.removeUnusedLocals(body);

				bann.validate(body);

				sootMethod.storeBody(jimple, body);
			} catch (CompilerException e) {
				exceptions.addElement(e);
				System.out.println(e.getMessage());
			}
		}

		// generate static constructor
		if (staticStmts.size() > 0) {
			ca.mcgill.sable.soot.SootMethod sootMethod;
			JimpleBody body;
			if (clinit == null) {
				sootMethod = new ca.mcgill.sable.soot.SootMethod("<clinit>", new LinkedList(), ca.mcgill.sable.soot.VoidType.v(),
						ca.mcgill.sable.soot.Modifier.PUBLIC);
				body = (JimpleBody) jimple.newBody(sootMethod);
			} else {
				sootMethod = clinit;
				body = clinitBody;
			}
			SequentialAnnotation sann = new SequentialAnnotation(null);
			StmtList stmts = body.getStmtList();
			for (Enumeration e = staticLocals.elements(); e.hasMoreElements();) {
				body.addLocal((ca.mcgill.sable.soot.jimple.Local) e.nextElement());
			}
			for (int i = 0; i < staticStmts.size(); i++) {
				stmts.add(staticStmts.elementAt(i));
				sann.addStmt((Stmt) staticStmts.elementAt(i));
			}
			Stmt stmt = jimple.newReturnVoidStmt();
			stmts.add(stmt);
			sann.add(stmt);
			ConstructorDeclarationAnnotation bann = new ConstructorDeclarationAnnotation(null);
			bann.addAnnotation(sann);
			bann.setSootMethod(sootMethod);
			am.addAnnotation(currentSootClass, sootMethod, bann);

			currentSootClass.addMethod(sootMethod);

			TemporaryLocalsReduction.reduce(body, bann);
			DeadCodeElimination.eliminate(body);
			JumpElimination.eliminate(body);
			DeadCodeEliminator.eliminateDeadCode(body);
			Transformations.removeUnusedLocals(body);

			bann.validate(body);
			sootMethod.storeBody(jimple, body);
		} 
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AClassClassBodyDeclaration
	 */
	public void caseAClassClassBodyDeclaration(AClassClassBodyDeclaration node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclaration
	 */
	public void caseAClassDeclaration(AClassDeclaration node) {
		try {
			currentClassOrInterfaceType = symbolTable.getDeclaredType(new Name(node.getId().toString()));
			symbolTable.setCurrentClassOrInterfaceType(currentClassOrInterfaceType);

			currentSootClass = getSootClass(currentClassOrInterfaceType.getName().toString());
			ClassDeclarationAnnotation cda = new ClassDeclarationAnnotation(node);
			LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
			cda.setFirstLine(lc.getFirstLine());
			cda.setFirstColumn(lc.getFirstColumn());
			cda.setLastLine(lc.getLastLine());
			cda.setLastColumn(lc.getLastColumn());
			cda.setSootClass(currentSootClass);
			am.addAnnotation(cda);
			am.putFilenameLinePairAnnotation(new FilenameLinePair(currentClassOrInterfaceType.getPath(),
					LineExtractor.extractLine(node)), cda);

			symbolTable.resetScope();
			if (node.getClassBody() != null) {
				node.getClassBody().apply(this);
			}
			compiledClasses.addElement(currentSootClass.getName());
			CompilationManager.addDocTriple(new DocTriple(currentSootClass, null, DocProcessor.tags(node, docComments)));
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}

		currentClassOrInterfaceType = null;
		symbolTable.setCurrentClassOrInterfaceType(null);
		currentSootClass = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclarationBlockedStmt
	 */
	public void caseAClassDeclarationBlockedStmt(AClassDeclarationBlockedStmt node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclarationInterfaceMemberDeclaration
	 */
	public void caseAClassDeclarationInterfaceMemberDeclaration(AClassDeclarationInterfaceMemberDeclaration node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AClassOrInterfaceTypeExp
	 */
	public void caseAClassOrInterfaceTypeExp(AClassOrInterfaceTypeExp node) {
		try {
			ca.mcgill.sable.soot.RefType refType = ca.mcgill.sable.soot.RefType.v(getSootClass((new Name(node.getClassOrInterfaceType().toString())).toString()).getName());
			currentType = null;
			LinkedList arrayExps = new LinkedList();
			int dimensions = 0;
			currentValue = null;
			for (Iterator i = node.getDimExp().iterator(); i.hasNext();) {
				((PDimExp) i.next()).apply(this);
				arrayExps.addLast(currentValue);
				currentValue = null;
				dimensions++;
			}
			dimensions += node.getDim().size();
			if (dimensions == 1) {
				currentValue = jimple.newNewArrayExpr(refType, (ca.mcgill.sable.soot.jimple.Value) arrayExps.removeFirst());
			} else {
				currentType = ca.mcgill.sable.soot.ArrayType.v(refType, dimensions);
				currentValue = jimple.newNewMultiArrayExpr((ca.mcgill.sable.soot.ArrayType) currentType, arrayExps);
				currentType = null;
			}
			ca.mcgill.sable.soot.jimple.Value newValue = currentValue;
			currentValue = null;
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), newValue.getType(), 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, newValue));
			currentValue = lcl;
			currentType = lcl.getType();
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ACompilationUnit
	 */
	public void caseACompilationUnit(ACompilationUnit node) {
		try {
			if (node.getPackageDeclaration() != null) {
				symbolTable.setCurrentPackage(Package.getPackage(new Name(node.getPackageDeclaration().toString())));
			} else {
				symbolTable.setCurrentPackage(Package.getPackage(new Name("")));
			}
			Object temp[] = node.getTypeDeclaration().toArray();
			for (int i = 0; i < temp.length; i++) {
				((PTypeDeclaration) temp[i]).apply(this);
			}
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorBody
	 */
	public void caseAConstructorBody(AConstructorBody node) {
		try {
			symbolTable.enterScope();
			boolean needHelper = false;
			PConstructorInvocation constructorInvocation = (PConstructorInvocation) node.getConstructorInvocation();
			if (!(constructorInvocation instanceof AThisConstructorInvocation)) {
				needHelper = true;
			}
			if (constructorInvocation != null) {
				constructorInvocation.apply(this);
			} else {
				ca.mcgill.sable.soot.SootMethod sm = getSootMethod(currentSootClass.getSuperClass().getName(), "<init>", new Vector());
				SequentialAnnotation sann = new SequentialAnnotation(null);
				Stmt stmt = jimple.newInvokeStmt(jimple.newSpecialInvokeExpr(currentThisLocal, sm, new LinkedList()));
				currentStmts.addElement(stmt);
				sann.addStmt(stmt);
				currentAnnotation.addAnnotation(sann);
				needHelper = true;
			}

			if ((instanceStmts.size() > 1) && needHelper) {
				SequentialAnnotation sann = new SequentialAnnotation(null);
				JimpleStmtCloner.reset();
				JimpleStmtCloner.setBody(currentBody);

				for (Enumeration e = instanceStmts.elements(); e.hasMoreElements();) {
					Stmt s = (Stmt) e.nextElement();
					Stmt stmt = JimpleStmtCloner.clone(s);
					sann.addStmt(stmt);
					currentStmts.addElement(stmt);
				}
				if (initBody != null)
					JimpleStmtCloner.copyTrap(initBody);
				JimpleStmtCloner.reset();
				JimpleStmtCloner.setBody(null);
				currentAnnotation.addAnnotation(sann);
			}
			labelEnterBlock();
			for (Iterator i = node.getBlockedStmt().iterator(); i.hasNext();) {
				((PBlockedStmt) i.next()).apply(this);
			}
			labelExitBlock();
			symbolTable.exitScope();
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorDeclaration
	 */
	public void caseAConstructorDeclaration(AConstructorDeclaration node) {
		if (CompilationManager.getModifiedMethodTable().get(node) != null) {
			((Node) CompilationManager.getModifiedMethodTable().get(node)).apply(this);
			CompilationManager.addDocTriple(new DocTriple(currentSootClass, currentSootMethod, DocProcessor.tags(node, docComments)));
			return;
		}
		currentSootMethod = null;
		nameGen = new NameGenerator();
		currentLocalNames = new Hashtable();
		currentLocalNamesIterator = new Hashtable();
		currentLocals = new Hashtable();
		currentStmts = new Vector();
		currentValue = null;
		currentType = null;
		currentName = null;
		currentThisLocal = null;
		labels = new LinkedList();
		currentAnnotation = new ConstructorDeclarationAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		currentAnnotation.setFirstLine(lc.getFirstLine());
		currentAnnotation.setFirstColumn(lc.getFirstColumn());
		currentAnnotation.setLastLine(lc.getLastLine());
		currentAnnotation.setLastColumn(lc.getLastColumn());
		currentTraps = new Vector();

		symbolTable.enterScope();
		node.getConstructorDeclarator().apply(this);
		node.getConstructorBody().apply(this);
		symbolTable.exitScope();

		// create the actual body
		StmtList stmts = currentBody.getStmtList();
		for (Enumeration e = currentLocals.elements(); e.hasMoreElements();) {
			currentBody.addLocal((ca.mcgill.sable.soot.jimple.Local) e.nextElement());
		}
		for (int i = 0; i < currentStmts.size(); i++) {
			stmts.add((Stmt) currentStmts.elementAt(i));
		}

		Stmt tempStmt = null;
		if (currentStmts.size() > 0) {
			int size = currentStmts.size();
			Stmt lastStmt = (Stmt) currentStmts.elementAt(--size);
			if (!(lastStmt instanceof JReturnStmt) && !(lastStmt instanceof JReturnVoidStmt)
					&& !(lastStmt instanceof JThrowStmt)) {
				tempStmt = jimple.newReturnVoidStmt();
				stmts.add(tempStmt);
				SequentialAnnotation ann = new SequentialAnnotation(null);
				ann.addStmt(tempStmt);
				currentAnnotation.addAnnotation(ann);
			}
		} else {
			SequentialAnnotation ann = new SequentialAnnotation(null);
			Stmt stmt = jimple.newReturnVoidStmt();
			ann.addStmt(stmt);
			stmts.add(stmt);
			currentAnnotation.addAnnotation(ann);
		}

		for (Enumeration e = currentTraps.elements(); e.hasMoreElements();) {
			currentBody.addTrap((ca.mcgill.sable.soot.jimple.Trap) e.nextElement());
		}

		am.addAnnotation(currentSootClass, currentSootMethod, currentAnnotation);

		// store body
		convertLocalTypes(currentBody);
		TemporaryLocalsReduction.reduce(currentBody, currentAnnotation);
		DeadCodeElimination.eliminate(currentBody);
		JumpElimination.eliminate(currentBody);
		DeadCodeEliminator.eliminateDeadCode(currentBody);
		DeadCodeElimination.eliminate(currentBody);
		//Transformations.removeUnusedLocals(currentBody);
		Transformations.cleanupCode(currentBody);

		currentAnnotation.validate(currentBody);
		//checkAnnotation(currentAnnotation, currentBody);
		currentSootMethod.storeBody(jimple, currentBody);
		am.putFilenameLinePairAnnotation(new FilenameLinePair(currentClassOrInterfaceType.getPath(),
				LineExtractor.extractLine(node)), currentAnnotation);

		CompilationManager.addDocTriple(new DocTriple(currentSootClass, currentSootMethod, DocProcessor.tags(node, docComments)));

		// cleaning up
		nameGen = null;
		currentLocals = null;
		currentLocalNames = null;
		currentLocalNamesIterator = null;
		currentStmts = null;
		currentValue = null;
		currentType = null;
		currentName = null;
		currentThisLocal = null;
		labels = null;
		currentTraps = null;
		currentAnnotation = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorDeclarator
	 */
	public void caseAConstructorDeclarator(AConstructorDeclarator node) {
		// get constructor header information
		String constructorName = "<init>";

		try {	
			// process parameter list
			currentType = null;
			Vector formalParameters = new Vector();
			for (Iterator i = node.getFormalParameter().iterator(); i.hasNext();) {
				((PFormalParameter) i.next()).apply(this);
				formalParameters.addElement(Util.convertType(currentType, symbolTable));
				currentType = null;
			}

			currentSootMethod = getSootMethod(currentSootClass.getName(), constructorName, formalParameters);
			((ConstructorDeclarationAnnotation) currentAnnotation).setSootMethod(currentSootMethod);
			((ConstructorDeclarationAnnotation) currentAnnotation).setConstructor(currentClassOrInterfaceType.getConstructor(formalParameters));
			currentBody = (JimpleBody) jimple.newBody(currentSootMethod);

			// make a local for holding this ref
			{
				String thisName = nameGen.newName();
				currentThisLocal = declareLocal(thisName, ca.mcgill.sable.soot.RefType.v(currentSootClass.getName()), 0);
				SequentialAnnotation ann = new SequentialAnnotation(null);
				Stmt s = jimple.newIdentityStmt(currentThisLocal, jimple.newThisRef(currentSootClass));
				currentStmts.addElement(s);
				ann.addStmt(s);
				currentAnnotation.addAnnotation(ann);
			}

			// declare parameters variable as locals
			SequentialAnnotation ann = new SequentialAnnotation(null);
			int paramNum = 0;
			Method m = currentClassOrInterfaceType.getConstructor(formalParameters);
			for (Enumeration e = m.getParameters().elements(); e.hasMoreElements(); paramNum++) {
				Variable v = (Variable) e.nextElement();
				ca.mcgill.sable.soot.jimple.Local tempLocal = declareLocal(v.getName().toString(), Util.convertType(v.getType()),
						Util.convertModifiers(v.getModifiers()));
				currentLocals.put(tempLocal.getName(), tempLocal);
				Stmt s = jimple.newIdentityStmt(tempLocal, jimple.newParameterRef(currentSootMethod, paramNum));
				currentStmts.addElement(s);
				ann.addStmt(s);
			}
			currentAnnotation.addAnnotation(ann);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AContinueStmt
	 */
	public void caseAContinueStmt(AContinueStmt node) {
		int start = currentStmts.size();

		if (node.getId() != null) {
			ca.mcgill.sable.soot.jimple.Unit target = getLabelUnit(node.getId().toString().trim());
			if (target == null) {
				exceptions.addElement(new CompilerException("Invalid continue statement..."));
				System.out.println("Invalid continue statement...");
				return;
			}
			Object lastFinallySynchBeforeControl = ((Vector) getLabelObject(target)).firstElement();
			for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
				Object o = e.nextElement();
				if (o == lastFinallySynchBeforeControl) break;
				if (o instanceof Node) {
					((Node) o).apply(this);
				} else {
					SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) o;
					Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
					sann.addExitMonitor(exitMon);
					currentStmts.addElement(exitMon);
				}
			}
			currentStmts.addElement(jimple.newGotoStmt(target));
		} else {
			if (currentContinueTarget == null) {
				exceptions.addElement(new CompilerException("Invalid continue statement..."));
				System.out.println("Invalid continue statement...");
				return;
			} 
			for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
				Object o = e.nextElement();
				if (o == lastFinallySynchBeforeControl) break;
				if (o instanceof Node) {
					((Node) o).apply(this);
				} else {
					SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) o;
					Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
					sann.addExitMonitor(exitMon);
					currentStmts.addElement(exitMon);
				}
			}
			currentStmts.addElement(jimple.newGotoStmt(currentContinueTarget));
		}

		int end = currentStmts.size();
		ContinueStmtAnnotation ann = new ContinueStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}
		currentAnnotation.addAnnotation(ann); 
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ADecimalIntegerLiteral
	 */
	public void caseADecimalIntegerLiteral(ADecimalIntegerLiteral node) {
		String literal = node.toString().trim();
		if (literal.endsWith("L") || literal.endsWith("l")) {
			currentValue = LongConstant.v(Long.valueOf(literal.substring(0, literal.length() - 1)).longValue());
			integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.LongType.TYPE;
		} else {
			currentValue = IntConstant.v(Integer.valueOf(literal).intValue());
			integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.IntType.TYPE;
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ADefaultSwitchLabel
	 */
	public void caseADefaultSwitchLabel(ADefaultSwitchLabel node) {
		defaultSwitch = !defaultSwitch;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ADoStmt
	 */
	public void caseADoStmt(ADoStmt node) {
		Stmt doStmt = jimple.newNopStmt();
		Stmt testStmt = jimple.newNopStmt();
		Stmt endStmt = jimple.newNopStmt();

		DoWhileStmtAnnotation ann = new DoWhileStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		Object prevLastSynch = lastFinallySynchBeforeControl;
		if (synchAnnotations.size() > 0)
			lastFinallySynchBeforeControl = (Annotation) synchAnnotations.lastElement();

		Stmt prevBreakTarget = currentBreakTarget;
		Stmt prevContinueTarget = currentContinueTarget;
		currentBreakTarget = endStmt;
		currentContinueTarget = testStmt;

		BlockStmtAnnotation prevAnnotation = currentAnnotation;
		currentAnnotation = new BlockStmtAnnotation(node.getBlock());

		currentStmts.addElement(doStmt);

		node.getBlock().apply(this);

		ann.setBlockAnnotation(getFirstEnclosedAnnotation(currentAnnotation));

		currentStmts.addElement(testStmt);

		int start = currentStmts.size();

		Node n = (Node) node.getExp().clone();
		PushComplement.push(n);
		if (node.getExp() instanceof AUnaryExp)
			if ("!".equals(((AUnaryExp) node.getExp()).getUnaryOperator().toString().trim()))
				n = ((AUnaryExp) n).getExp();

		Stmt prevTrueBranch = trueBranch;
		Stmt prevFalseBranch = falseBranch;
		trueBranch = doStmt;
		falseBranch = endStmt;

		n.apply(new BooleanExpression(this, trueBranch, falseBranch));

		ann.setIndefinite(currentStmts.elementAt(currentStmts.size() - 1) instanceof JGotoStmt);

		int end = currentStmts.size();

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}

		currentStmts.addElement(endStmt);
		currentValue = null;


		trueBranch = prevTrueBranch;
		falseBranch = prevFalseBranch;
		currentBreakTarget = prevBreakTarget;
		currentContinueTarget = prevContinueTarget;
		lastFinallySynchBeforeControl = prevLastSynch;

		currentAnnotation = prevAnnotation;
		currentAnnotation.addAnnotation(ann);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ADoublePrimitiveType
	 */
	public void caseADoublePrimitiveType(ADoublePrimitiveType node) {
		currentType = ca.mcgill.sable.soot.DoubleType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AEmptyStmt
	 */
	public void caseAEmptyStmt(AEmptyStmt node) {
		Annotation ea = new EmptyStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ea.setFirstLine(lc.getFirstLine());
		ea.setFirstColumn(lc.getFirstColumn());
		ea.setLastLine(lc.getLastLine());
		ea.setLastColumn(lc.getLastColumn());
		currentAnnotation.addAnnotation(ea);
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ea);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AExpCastExp
	 */
	public void caseAExpCastExp(AExpCastExp node) {
		try {
			String t = ((ANameExp) node.getFirst()).getName().toString();
			ca.mcgill.sable.soot.Type type =
					Util.convertType(symbolTable.resolveType(new Name(t)));
			int dimensions = Util.countArrayDimensions(t);
			if (dimensions > 0)
				type = ca.mcgill.sable.soot.ArrayType.v((BaseType) type, dimensions);
			node.getSecond().apply(this);
			ca.mcgill.sable.soot.jimple.Value castedValue = currentValue;
			currentValue = null;
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), type, 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newCastExpr(castedValue, type)));
			currentValue = lcl;
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AExpStmt
	 */
	public void caseAExpStmt(AExpStmt node) {
		int start = currentStmts.size();
		if(node.getExp() != null) {
			node.getExp().apply(this);
		}
		int end = currentStmts.size();
		ExpStmtAnnotation ann = new ExpStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}
		currentAnnotation.addAnnotation(ann); 
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AFalseBooleanLiteral
	 */
	public void caseAFalseBooleanLiteral(AFalseBooleanLiteral node) {
		currentValue = IntConstant.v(0);
		integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.BooleanType.TYPE;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AFieldDeclaration
	 */
	public void caseAFieldDeclaration(AFieldDeclaration node) {
		try {
			fieldNode = node;

			int fieldModifiers = Util.convertModifiers(Util.convertModifiers(node.getModifier().toString()));
			if (node.parent() instanceof AConstantDeclarationInterfaceMemberDeclaration) {
				fieldModifiers = ca.mcgill.sable.soot.Modifier.PUBLIC | ca.mcgill.sable.soot.Modifier.STATIC | ca.mcgill.sable.soot.Modifier.FINAL;
			}

			// redirect field initializer to static or instance contstructor body
			if (ca.mcgill.sable.soot.Modifier.isStatic(fieldModifiers)) {
				currentLocals = staticLocals;
				currentStmts = staticStmts;
			} else {
				currentLocals = instanceLocals;
				currentStmts = instanceStmts;
			}

			PVariableDeclarator variableDeclarator;
			for (Iterator i = node.getVariableDeclarator().iterator(); i.hasNext();) {
				variableDeclarator = (PVariableDeclarator) i.next();
				currentType = ca.mcgill.sable.soot.IntType.v();
				variableDeclarator.apply(this);
			}
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
		currentType = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AFloatingPointLiteralLiteral
	 */
	public void caseAFloatingPointLiteralLiteral(AFloatingPointLiteralLiteral node) {
		String literal = node.toString().trim();
		if (literal.equals("Infinity"))
			currentValue = DoubleConstant.v(Double.POSITIVE_INFINITY);
		else if (literal.equals("NaN"))
			currentValue = DoubleConstant.v(Double.NaN);
		else if (literal.endsWith("F") || literal.endsWith("f"))
			currentValue = FloatConstant.v(Float.valueOf(literal).floatValue());
		else
			currentValue = DoubleConstant.v(Double.valueOf(literal).doubleValue());
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AFloatPrimitiveType
	 */
	public void caseAFloatPrimitiveType(AFloatPrimitiveType node) {
		currentType = ca.mcgill.sable.soot.FloatType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AForStmt
	 */
	public void caseAForStmt(AForStmt node) {
		PExp exp = null;

		if (node.getExp() != null) {
			exp = new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.getExp().clone());
			PushComplement.push(exp);
			exp = ((AUnaryExp) exp).getExp();
		}

		ForStmtAnnotation ann = new ForStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());

		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);
		BlockStmtAnnotation prevAnnotation = currentAnnotation;

		Object prevLastSynch = lastFinallySynchBeforeControl;
		if (synchAnnotations.size() > 0)
			lastFinallySynchBeforeControl = (Annotation) synchAnnotations.lastElement();

		Stmt initStmt = jimple.newNopStmt();
		Stmt testStmt = jimple.newNopStmt();
		Stmt blockStmt = jimple.newNopStmt();
		Stmt updateStmt = jimple.newNopStmt();
		Stmt endStmt = jimple.newNopStmt();

		symbolTable.enterScope();
		labelEnterBlock();
		if (node.getForInit() != null) {
			forLocals = new Hashtable();
			forInitType = null;
			forInitModifiers = 0;
			currentStmts.addElement(initStmt);

			int start = currentStmts.size();

			currentAnnotation = null;

			node.getForInit().apply(this);

			currentAnnotation = prevAnnotation;

			int end = currentStmts.size();

			SequentialAnnotation sann = new SequentialAnnotation(null);

			for (int i = start; i < end; i++) {
				sann.addStmt((Stmt) currentStmts.elementAt(i));
			}

			ann.setInitAnnotation(sann);

			for (Enumeration e = forLocals.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				ann.addDeclaredLocal(key, (ca.mcgill.sable.soot.jimple.Local) forLocals.get(key));
			}

			ann.setType(forInitType);
			ann.setModifiers(forInitModifiers);
		} else {
			initStmt = null;
		}

		boolean isIndefiniteLoop = false;

		if (exp != null) {
			currentStmts.addElement(testStmt);
			Stmt prevTrueBranch = trueBranch;
			Stmt prevFalseBranch = falseBranch;
			trueBranch = endStmt;
			falseBranch = blockStmt;

			int start = currentStmts.size();

			exp.apply(new BooleanExpression(this, trueBranch, falseBranch));

			if (currentStmts.elementAt(currentStmts.size() - 1) instanceof JGotoStmt) isIndefiniteLoop = true;

			int end = currentStmts.size();

			for (int i = start; i < end; i++) {
				ann.addStmt((Stmt) currentStmts.elementAt(i));
			}

			trueBranch = prevTrueBranch;
			falseBranch = prevFalseBranch;
		} else {
			testStmt = null;
			isIndefiniteLoop = true;
		}

		currentStmts.addElement(blockStmt);

		currentAnnotation = new BlockStmtAnnotation(node.getBlock());

		Stmt prevBreakTarget = currentBreakTarget;
		Stmt prevContinueTarget = currentContinueTarget;

		currentBreakTarget = endStmt;
		currentContinueTarget = updateStmt;

		node.getBlock().apply(this);

		ann.setBlockAnnotation(getFirstEnclosedAnnotation(currentAnnotation));

		currentBreakTarget = prevBreakTarget;
		currentContinueTarget = prevContinueTarget;

		currentStmts.addElement(updateStmt);

		int start = currentStmts.size();

		for (Iterator i = node.getForUpdate().iterator(); i.hasNext();) {
			((PExp) i.next()).apply(this);
		}

		if (testStmt == null)
			currentStmts.addElement(jimple.newGotoStmt(blockStmt));
		else
			currentStmts.addElement(jimple.newGotoStmt(testStmt));

		int end = currentStmts.size();

		SequentialAnnotation sann = new SequentialAnnotation(null);

		for (int i = start; i < end; i++) {
			sann.addStmt((Stmt) currentStmts.elementAt(i));
		}

		ann.setUpdateAnnotation(sann);

		Stmt backPointStmt = (Stmt) currentStmts.elementAt(currentStmts.size() - 1);
		currentStmts.addElement(endStmt);

		symbolTable.exitScope();
		labelExitBlock();

		ann.setIndefinite(isIndefiniteLoop);

		currentAnnotation = prevAnnotation;
		currentAnnotation.addAnnotation(ann);

		lastFinallySynchBeforeControl = prevLastSynch;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AHexIntegerLiteral
	 */
	public void caseAHexIntegerLiteral(AHexIntegerLiteral node) {
		String literal = node.toString().substring(2).trim();
		if (literal.endsWith("L") || literal.endsWith("l")) {
			currentValue = LongConstant.v(new BigInteger(literal.substring(0, literal.length() - 1), 16).longValue());
			integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.LongType.TYPE;
		} else {
			currentValue = IntConstant.v(Long.valueOf(literal, 16).intValue());
			integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.IntType.TYPE;
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AIdVariableDeclarator
	 */
	public void caseAIdVariableDeclarator(AIdVariableDeclarator node) {
		try {
			node.getVariableDeclaratorId().apply(this);
			if (symbolTable.getNumScopeLevels() > 0) {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(currentName, currentType, 0);
				localAnnotation.addDeclaredLocal(currentName, lcl);
			} else {
				FieldDeclarationAnnotation ann = new FieldDeclarationAnnotation(fieldNode);
				ca.mcgill.sable.soot.SootField sf = getSootField(currentSootClass.getName(), currentName);
				ann.setSootField(sf);
				ann.setField(currentClassOrInterfaceType.getField(new Name(currentName)));
				am.addAnnotation(currentSootClass, sf, ann);
				am.putFilenameLinePairAnnotation(
						new FilenameLinePair(currentClassOrInterfaceType.getPath(),
								LineExtractor.extractLine(node.getVariableDeclaratorId())),
						ann);
			}
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AIfStmt
	 */
	public void caseAIfStmt(AIfStmt node) {
		PExp exp = new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.getExp().clone());
		PushComplement.push(exp);
		exp = ((AUnaryExp) exp).getExp();

		Stmt thenStmt = jimple.newNopStmt();
		Stmt elseStmt = jimple.newNopStmt();
		Stmt endStmt = jimple.newNopStmt();

		BlockStmtAnnotation prevAnnotation = currentAnnotation;
		IfStmtAnnotation ann = new IfStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		Stmt prevTrueBranch = trueBranch;
		Stmt prevFalseBranch = falseBranch;
		trueBranch = elseStmt;
		falseBranch = thenStmt;

		int start = currentStmts.size();

		exp.apply(new BooleanExpression(this, trueBranch, falseBranch));

		trueBranch = prevTrueBranch;
		falseBranch = prevFalseBranch;

		int end = currentStmts.size();

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}

		currentStmts.addElement(thenStmt);

		currentAnnotation = new BlockStmtAnnotation(node.getBlock());

		node.getThenPart().apply(this);

		SequentialAnnotation sann = new SequentialAnnotation(null);
		Stmt gotoStmt = jimple.newGotoStmt(endStmt);
		sann.addStmt(gotoStmt);
		ann.setAnnotation(sann);
		ann.setThenAnnotation(getFirstEnclosedAnnotation(currentAnnotation));

		currentStmts.addElement(gotoStmt);
		currentStmts.addElement(elseStmt);

		currentAnnotation = new BlockStmtAnnotation(node.getBlock());
		node.getBlock().apply(this);

		ann.setElseAnnotation(getFirstEnclosedAnnotation(currentAnnotation));

		currentStmts.addElement(endStmt);

		currentAnnotation = prevAnnotation;
		currentAnnotation.addAnnotation(ann);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AInitClassInterfaceExp
	 */
	public void caseAInitClassInterfaceExp(AInitClassInterfaceExp node) {
		try {
			String className = (new Name(node.getClassOrInterfaceType().toString())).toString();
			ca.mcgill.sable.soot.SootClass sc = getSootClass(className);
			className = sc.getName();
			ca.mcgill.sable.soot.Type type = ca.mcgill.sable.soot.RefType.v(className);
			int dimensions = node.getDim().size();
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.ArrayType.v((BaseType) type, dimensions), 0);
			currentValue = lcl;
			node.getArrayInitializer().apply(this);
			currentValue = lcl;
		} catch (Exception e) {
			exceptions.add(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AInitPrimitiveExp
	 */
	public void caseAInitPrimitiveExp(AInitPrimitiveExp node) {
		node.getPrimitiveType().apply(this);
		ca.mcgill.sable.soot.Type primitiveType = currentType;
		currentType = null;
		int dimensions = node.getDim().size();
		ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(),
				ca.mcgill.sable.soot.ArrayType.v((BaseType) primitiveType, dimensions), 0);
		currentValue = lcl;
		node.getArrayInitializer().apply(this);
		currentValue = lcl;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AInstanceofExp
	 */
	public void caseAInstanceofExp(AInstanceofExp node) {
		node.getExp().apply(this);
		node.getReferenceType().apply(this);

		ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.BooleanType.v(), 0);
		currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newInstanceOfExpr(currentValue, currentType)));

		currentValue = lcl;	
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceBody
	 */
	public void caseAInterfaceBody(AInterfaceBody node) {
		// prepare variables for field processing
		currentThisLocal = null;
		instanceLocals = null;
		currentValue = null;
		currentType = null;
		currentName = null;
		instanceStmts = null;
		nameGen = new NameGenerator();
		currentLocalNames = new Hashtable();
		currentLocalNamesIterator = new Hashtable();
		currentLocals = new Hashtable();
		currentStmts = new Vector();
		staticLocals = currentLocals;
		staticStmts = currentStmts;

		LinkedList interfaceMemberDeclList = node.getInterfaceMemberDeclaration();
		PInterfaceMemberDeclaration interfaceMemberDecl;
		if (interfaceMemberDeclList.size() > 0) {
			// process field decls
			for (Iterator i = interfaceMemberDeclList.iterator(); i.hasNext();) {
				interfaceMemberDecl = (PInterfaceMemberDeclaration) i.next();
				if (interfaceMemberDecl instanceof AConstantDeclarationInterfaceMemberDeclaration)
					interfaceMemberDecl.apply(this);
			}

			// generate static constructor
			if (staticStmts.size() > 0) {
				ca.mcgill.sable.soot.SootMethod sootMethod = new ca.mcgill.sable.soot.SootMethod("<clinit>", new LinkedList(), ca.mcgill.sable.soot.VoidType.v(), ca.mcgill.sable.soot.Modifier.PUBLIC | ca.mcgill.sable.soot.Modifier.STATIC);
				currentSootClass.addMethod(sootMethod);
				JimpleBody body = (JimpleBody) jimple.newBody(sootMethod);
				StmtList stmts = body.getStmtList();
				for (Enumeration e = staticLocals.elements(); e.hasMoreElements();) {
					body.addLocal((ca.mcgill.sable.soot.jimple.Local) e.nextElement());
				}
				for (int i = 0; i < staticStmts.size(); i++) {
					stmts.add(staticStmts.elementAt(i));
				}
				stmts.add(jimple.newReturnVoidStmt());
				sootMethod.storeBody(jimple, body);
			}
			currentLocals = null;
			currentStmts = null;
			nameGen = null;
			staticLocals = currentLocals;
			staticStmts = currentStmts;
			instanceLocals = currentLocals;
			instanceStmts = currentStmts;

			// process the the rest decls
			for (Iterator i = interfaceMemberDeclList.iterator(); i.hasNext();) {
				interfaceMemberDecl = (PInterfaceMemberDeclaration) i.next();
				if (!(interfaceMemberDecl instanceof AConstantDeclarationInterfaceMemberDeclaration))
					interfaceMemberDecl.apply(this);
			}
		}
		compiledClasses.addElement(currentSootClass.getName());
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceDeclaration
	 */
	public void caseAInterfaceDeclaration(AInterfaceDeclaration node) {
		try {
			currentClassOrInterfaceType = symbolTable.getDeclaredType(new Name(node.getId().toString()));
			symbolTable.setCurrentClassOrInterfaceType(currentClassOrInterfaceType);

			currentSootClass = getSootClass(currentClassOrInterfaceType.getName().toString());

			ClassDeclarationAnnotation cda = new ClassDeclarationAnnotation(node);
			LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
			cda.setFirstLine(lc.getFirstLine());
			cda.setFirstColumn(lc.getFirstColumn());
			cda.setLastLine(lc.getLastLine());
			cda.setLastColumn(lc.getLastColumn());
			cda.setSootClass(currentSootClass);
			am.addAnnotation(cda);
			am.putFilenameLinePairAnnotation(new FilenameLinePair(currentClassOrInterfaceType.getPath(),
					LineExtractor.extractLine(node)), cda);

			if (node.getInterfaceBody() != null) {
				node.getInterfaceBody().apply(this);
			}
			compiledClasses.addElement(currentSootClass.getName());
			CompilationManager.addDocTriple(new DocTriple(currentSootClass, null, DocProcessor.tags(node, docComments)));
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}

		currentClassOrInterfaceType = null;
		symbolTable.setCurrentClassOrInterfaceType(null);
		currentSootClass = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceDeclarationClassMemberDeclaration
	 */
	public void caseAInterfaceDeclarationClassMemberDeclaration(AInterfaceDeclarationClassMemberDeclaration node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceDeclarationInterfaceMemberDeclaration
	 */
	public void caseAInterfaceDeclarationInterfaceMemberDeclaration(AInterfaceDeclarationInterfaceMemberDeclaration node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AIntPrimitiveType
	 */
	public void caseAIntPrimitiveType(AIntPrimitiveType node) {
		currentType = ca.mcgill.sable.soot.IntType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ALabelStmt
	 */
	public void caseALabelStmt(ALabelStmt node) {
		LabeledStmtAnnotation ann = new LabeledStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		ann.setId(node.getId().toString().trim());
		try {
			declareLabel(node.getId().toString().trim());
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
			return;
		}
		BlockStmtAnnotation prevAnnotation = currentAnnotation;
		currentAnnotation = new BlockStmtAnnotation(node.getBlock());
		node.getBlock().apply(this);
		ann.setAnnotation(getFirstEnclosedAnnotation(currentAnnotation));
		currentAnnotation = prevAnnotation;
		currentAnnotation.addAnnotation(ann);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ALocalVariableDeclaration
	 */
	public void caseALocalVariableDeclaration(ALocalVariableDeclaration node) {
		// get local var modifiers
		try {
			localAnnotation = new LocalDeclarationStmtAnnotation(node);
			LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
			localAnnotation.setFirstLine(lc.getFirstLine());
			localAnnotation.setFirstColumn(lc.getFirstColumn());
			localAnnotation.setLastLine(lc.getLastLine());
			localAnnotation.setLastColumn(lc.getLastColumn());
			am.putFilenameLinePairAnnotation(
					new FilenameLinePair(currentClassOrInterfaceType.getPath(),
							LineExtractor.extractLine(node)),
					localAnnotation);

			int start = currentStmts.size();

			int modifiers = Util.convertModifiers(Util.convertModifiers(node.getModifier().toString()));
			currentType = null;
			node.getType().apply(this);
			ca.mcgill.sable.soot.Type localType = currentType;
			localAnnotation.setType(localType);
			localAnnotation.setModifiers(modifiers);
			PVariableDeclarator variableDeclarator;
			for (Iterator i = node.getVariableDeclarator().iterator(); i.hasNext();) {
				variableDeclarator = (PVariableDeclarator) i.next();
				currentType = localType;
				variableDeclarator.apply(this);
			}
			currentType = null;

			if (currentAnnotation != null) {
				int end = currentStmts.size();

				for (int i = start; i < end; i++) {
					localAnnotation.addStmt((Stmt) currentStmts.elementAt(i));
				}
				currentAnnotation.addAnnotation(localAnnotation);
			} else {
				forInitType = localType;
				forInitModifiers = modifiers;
			}
			localAnnotation = null;
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ALongPrimitiveType
	 */
	public void caseALongPrimitiveType(ALongPrimitiveType node) {
		currentType = ca.mcgill.sable.soot.LongType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AMethodDeclaration
	 */
	public void caseAMethodDeclaration(AMethodDeclaration node) {
		if (CompilationManager.getModifiedMethodTable().get(node) != null) {
			((Node) CompilationManager.getModifiedMethodTable().get(node)).apply(this);
			return;
		}
		methodLine = LineExtractor.extractLine(node);
		// prepare variables for method processing
		currentSootMethod = null;
		nameGen = new NameGenerator();
		currentLocalNames = new Hashtable();
		currentLocalNamesIterator = new Hashtable();
		currentLocals = new Hashtable();
		currentStmts = new Vector();
		currentValue = null;
		currentType = null;
		currentName = null;
		currentThisLocal = null;
		labels = new LinkedList();
		currentAnnotation = mann = new MethodDeclarationAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		currentAnnotation.setFirstLine(lc.getFirstLine());
		currentAnnotation.setFirstColumn(lc.getFirstColumn());
		currentAnnotation.setLastLine(lc.getLastLine());
		currentAnnotation.setLastColumn(lc.getLastColumn());

		currentTraps = new Vector();

		// apply method header & body
		symbolTable.enterScope();
		node.getMethodHeader().apply(this);
		node.getMethodBody().apply(this);
		symbolTable.exitScope();

		// create the actual body
		StmtList stmts = currentBody.getStmtList();
		for (Enumeration e = currentLocals.elements(); e.hasMoreElements();) {
			currentBody.addLocal((ca.mcgill.sable.soot.jimple.Local) e.nextElement());
		}

		for (int i = 0; i < currentStmts.size(); i++) {
			stmts.add((Stmt) currentStmts.elementAt(i));
		}

		Stmt tempStmt = null;
		if (currentStmts.size() > 0) {
			int size = currentStmts.size();
			Stmt lastStmt = (Stmt) currentStmts.elementAt(--size);
			if (!(lastStmt instanceof JReturnStmt) && !(lastStmt instanceof JReturnVoidStmt)
					&& !(lastStmt instanceof JThrowStmt)) {
				tempStmt = jimple.newReturnVoidStmt();
				stmts.add(tempStmt);
				SequentialAnnotation ann = new SequentialAnnotation(null);
				ann.addStmt(tempStmt);
				currentAnnotation.addAnnotation(ann);
			}
		} else {
			SequentialAnnotation ann = new SequentialAnnotation(null);
			Stmt stmt = jimple.newReturnVoidStmt();
			ann.addStmt(stmt);
			stmts.add(stmt);
			currentAnnotation.addAnnotation(ann);
		}

		for (Enumeration e = currentTraps.elements(); e.hasMoreElements();) {
			currentBody.addTrap((ca.mcgill.sable.soot.jimple.Trap) e.nextElement());
		}

		am.addAnnotation(currentSootClass, currentSootMethod, currentAnnotation);

		// store body
		if (!(currentSootMethod.getReturnType() instanceof ca.mcgill.sable.soot.VoidType)) {
			addRetIfNecessary(currentBody, currentSootMethod.getReturnType());
		}
		convertLocalTypes(currentBody);
		TemporaryLocalsReduction.reduce(currentBody, currentAnnotation);
		DeadCodeElimination.eliminate(currentBody);
		JumpElimination.eliminate(currentBody);
		DeadCodeEliminator.eliminateDeadCode(currentBody);
		DeadCodeElimination.eliminate(currentBody);
		Transformations.removeUnusedLocals(currentBody);
		patchBody(currentBody);
		//Transformations.cleanupCode(currentBody);

		currentAnnotation.validate(currentBody);
		//checkAnnotation(currentAnnotation, currentBody);
		currentSootMethod.storeBody(jimple, currentBody);
		am.putFilenameLinePairAnnotation(new FilenameLinePair(currentClassOrInterfaceType.getPath(),
				LineExtractor.extractLine(node)), currentAnnotation);

		CompilationManager.addDocTriple(new DocTriple(currentSootClass, currentSootMethod, DocProcessor.tags(node, docComments)));

		// cleaning up
		nameGen = null;
		currentLocals = null;
		currentLocalNames = null;
		currentLocalNamesIterator = null;
		currentStmts = null;
		currentValue = null;
		currentType = null;
		currentName = null;
		currentThisLocal = null;
		currentBody = null;
		labels = null;
		currentTraps = null;
		currentAnnotation = null;
		mann = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AMethodDeclarator
	 */
	public void caseAMethodDeclarator(AMethodDeclarator node) {
		// get method header information
		String methodName = node.getId().toString().trim();

		try {
			// process parameter list
			Vector formalParameters = new Vector();
			for (Iterator i = node.getFormalParameter().iterator(); i.hasNext();) {
				((PFormalParameter) i.next()).apply(this);
				formalParameters.addElement(Util.convertType(currentType, symbolTable));
				currentType = null;
			}

			currentSootMethod = getSootMethod(currentSootClass.getName(), methodName, formalParameters);
			((MethodDeclarationAnnotation) currentAnnotation).setSootMethod(currentSootMethod);
			((MethodDeclarationAnnotation) currentAnnotation).setMethod(currentClassOrInterfaceType.getMethod(new Name(methodName), formalParameters));

			//if (!isInterface) {
			currentBody = (JimpleBody) jimple.newBody(currentSootMethod);

			int methodModifiers = currentSootMethod.getModifiers();
			// make a local for holding this ref if the method is not static, abstract, or native
			if (!ca.mcgill.sable.soot.Modifier.isStatic(methodModifiers)&& !ca.mcgill.sable.soot.Modifier.isNative(methodModifiers)) {  //&& !ca.mcgill.sable.soot.Modifier.isAbstract(methodModifiers) ) {
				String thisName = nameGen.newName();
				currentThisLocal = declareLocal(thisName, ca.mcgill.sable.soot.RefType.v(currentSootClass.getName()), 0);
				currentLocals.put(currentThisLocal.getName(), currentThisLocal);

				SequentialAnnotation sann = new SequentialAnnotation(null);
				Stmt stmt = jimple.newIdentityStmt(currentThisLocal, jimple.newThisRef(currentSootClass));
				currentStmts.addElement(stmt);
				sann.addStmt(stmt);
				currentAnnotation.addAnnotation(sann);
			}

			// declare parameters variable as locals
			if (!ca.mcgill.sable.soot.Modifier.isNative(methodModifiers)) { //&& !ca.mcgill.sable.soot.Modifier.isAbstract(methodModifiers)) {
				Method m = currentClassOrInterfaceType.getMethod(new Name(methodName), formalParameters);

				SequentialAnnotation sann = new SequentialAnnotation(null);
				Hashtable parameterLocals = new Hashtable();
				int paramNum = 0;
				for (Enumeration e = m.getParameters().elements(); e.hasMoreElements(); paramNum++) {
					Variable v = (Variable) e.nextElement();
					ca.mcgill.sable.soot.jimple.Local tempLocal = declareLocal(v.getName().toString(), Util.convertType(v.getType()),
							Util.convertModifiers(v.getModifiers()));
					parameterLocals.put(tempLocal.getName(), tempLocal);
					Stmt stmt = jimple.newIdentityStmt(tempLocal, jimple.newParameterRef(currentSootMethod, paramNum));
					currentStmts.addElement(stmt);
					sann.addStmt(stmt);
				}
				((MethodDeclarationAnnotation) currentAnnotation).setParameterLocals(parameterLocals);
				currentAnnotation.addAnnotation(sann);
			}
			if (ca.mcgill.sable.soot.Modifier.isAbstract(methodModifiers)) {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal("$ret", currentSootMethod.getReturnType(), 0);
				mann.setRetLocal(lcl);
				currentStmts.addElement(jimple.newReturnStmt(lcl));
			}
			//}
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}	
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANameArrayAccess
	 */
	public void caseANameArrayAccess(ANameArrayAccess node) {
		String arrayName = (new Name(node.getName().toString())).toString();
		node.getExp().apply(this);
		ca.mcgill.sable.soot.jimple.Value index = currentValue;
		currentValue = null;
		if ((node.parent() instanceof AArrayAccessLeftHandSide) || assignExp) {
			nameExp(false, arrayName);
			currentValue = jimple.newArrayRef(currentValue, index);
		} else {
			nameExp(false, arrayName);
			currentValue = jimple.newArrayRef(currentValue, index);
			/* [Thomas, May 22, 2017]
			 * */
//			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
//			currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
//			currentValue = lcl;
		}
		assignExp = false;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANameArrayType
	 */
	public void caseANameArrayType(ANameArrayType node) {
		try {
			ca.mcgill.sable.soot.BaseType temp =
					ca.mcgill.sable.soot.RefType.v(getSootClass(node.getName().toString().trim()).getName());
			currentType = ca.mcgill.sable.soot.ArrayType.v(temp, node.getDim().size());
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANameCastExp
	 */
	public void caseANameCastExp(ANameCastExp node) {
		try {
			String className = (new Name(node.getName().toString())).toString();
			ca.mcgill.sable.soot.SootClass sc = getSootClass(className);
			className = sc.getName();
			ca.mcgill.sable.soot.Type type = ca.mcgill.sable.soot.RefType.v(className);
			int dimensions = node.getDim().size();
			if (dimensions != 0) {
				type = ca.mcgill.sable.soot.ArrayType.v((ca.mcgill.sable.soot.BaseType) type, dimensions);
			}
			node.getExp().apply(this);
			ca.mcgill.sable.soot.jimple.Value castedValue = currentValue;
			currentValue = null;
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), type, 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newCastExpr(castedValue, type)));
			currentValue = lcl;
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANamedTypeExp
	 */
	public void caseANamedTypeExp(ANamedTypeExp node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANameExp
	 */
	public void caseANameExp(ANameExp node) {
		nameExp(node.parent() instanceof ANameLeftHandSide, (new Name(node.getName().toString())).toString());
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANameLeftHandSide
	 */
	public void caseANameLeftHandSide(ANameLeftHandSide node) {
		nameExp(true, (new Name(node.getName().toString())).toString());
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANameMethodInvocationExp
	 */
	public void caseANameMethodInvocationExp(ANameMethodInvocationExp node) {
		try {
			String methodName = (new Name(node.getName().toString())).toString();
			LinkedList args = new LinkedList();
			currentValue = null;
			Vector types = new Vector();
			
			{
				Object temp[] = node.getArgumentList().toArray();
				for (int i = 0; i < temp.length; i++) {
					((PExp) temp[i]).apply(this);
					args.addLast(currentValue);
					if (currentValue instanceof IntConstant) {
						types.addElement(integralConstantType);
					} else {
						types.addElement(Util.convertType(currentValue instanceof ConditionExpr ? ca.mcgill.sable.soot.BooleanType.v() : currentValue.getType(), symbolTable));
					}
					currentValue = null;
				}
			}
			methodInvocation(methodName, args, types, false);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANameReferenceType
	 */
	public void caseANameReferenceType(ANameReferenceType node) {
		try {
			currentType = ca.mcgill.sable.soot.RefType.v(getSootClass(node.getName().toString().trim()).getName());
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ANullLiteral
	 */
	public void caseANullLiteral(ANullLiteral node) {
		currentValue = NullConstant.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AOctalIntegerLiteral
	 */
	public void caseAOctalIntegerLiteral(AOctalIntegerLiteral node) {
		String literal = node.toString().substring(1).trim();
		if (literal.endsWith("L") || literal.endsWith("l")) {
			currentValue = LongConstant.v(new BigInteger(literal.substring(0, literal.length() - 1), 8).longValue());
			integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.LongType.TYPE;
		} else {
			currentValue = IntConstant.v(Long.valueOf(literal, 8).intValue());
			integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.IntType.TYPE;
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APostDecrementExp
	 */
	public void caseAPostDecrementExp(APostDecrementExp node) {
		postIncDecExp(node, false);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APostIncrementExp
	 */
	public void caseAPostIncrementExp(APostIncrementExp node) {
		postIncDecExp(node, true);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryFieldAccess
	 */
	public void caseAPrimaryFieldAccess(APrimaryFieldAccess node) {
		node.getExp().apply(this);
		if (currentValue.getType() instanceof ca.mcgill.sable.soot.RefType) {
			try {
				boolean isAssign = assignExp;
				assignExp = false;
				fieldAccess((node.parent() instanceof AFieldAccessLeftHandSide) || isAssign, currentValue,
						(currentValue == currentThisLocal) ? currentSootClass.getName() : ((ca.mcgill.sable.soot.RefType) currentValue.getType()).className,
								node.getId().toString().trim());
			} catch (CompilerException e) {
				exceptions.addElement(e);
				System.out.println(e.getMessage());
			}
		} else {
			if ("length".equals(node.getId().toString().trim())) {
				currentValue = jimple.newLengthExpr(currentValue);
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
				currentValue = lcl;
			} else {
				exceptions.addElement(new CompilerException("Arrays does not have field " + node.getId() + " in expression " + node));
				System.out.println("Arrays does not have field " + node.getId() + " in expression " + node);
				currentValue = IntConstant.v(0);
			}
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryMethodInvocationExp
	 */
	public void caseAPrimaryMethodInvocationExp(APrimaryMethodInvocationExp node) {
		try {
			node.getExp().apply(this);
			ca.mcgill.sable.soot.jimple.Value caller = currentValue;
			currentValue = null;
			LinkedList args = new LinkedList();
			Vector types = new Vector();
			{
				Object temp[] = node.getArgumentList().toArray();
				for (int i = 0; i < temp.length; i++) {
					((PExp) temp[i]).apply(this);
					args.addLast(currentValue);
					if (currentValue instanceof IntConstant) {
						types.addElement(integralConstantType);
					} else {
						types.addElement(Util.convertType(currentValue instanceof ConditionExpr ? ca.mcgill.sable.soot.BooleanType.v() : currentValue.getType(), symbolTable));
					}
					currentValue = null;
				}
			}
			String methodName = node.getId().toString().trim();
			methodInvoke(caller, ((ca.mcgill.sable.soot.RefType) caller.getType()).className, methodName, args, types, false);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryNoNewArrayArrayAccess
	 */
	public void caseAPrimaryNoNewArrayArrayAccess(APrimaryNoNewArrayArrayAccess node) {
		node.getFirst().apply(this);
		ca.mcgill.sable.soot.jimple.Value first = currentValue;
		currentValue = null;
		node.getSecond().apply(this);
		if (node.parent() instanceof AArrayAccessLeftHandSide){
			currentValue = jimple.newArrayRef(first, currentValue);
		} else {
			currentValue = jimple.newArrayRef(first, currentValue);
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
			currentValue = lcl;
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveArrayType
	 */
	public void caseAPrimitiveArrayType(APrimitiveArrayType node) {
		node.getPrimitiveType().apply(this);

		ca.mcgill.sable.soot.BaseType temp = (ca.mcgill.sable.soot.BaseType) currentType;
		currentType = ca.mcgill.sable.soot.ArrayType.v(temp, node.getDim().size());
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypeArrayExp
	 */
	public void caseAPrimitiveTypeArrayExp(APrimitiveTypeArrayExp node) {
		node.getPrimitiveType().apply(this);
		ca.mcgill.sable.soot.Type primitiveType = currentType;
		currentType = null;
		LinkedList arrayExps = new LinkedList();
		int dimensions = 0;
		for (Iterator i = node.getDimExp().iterator(); i.hasNext();) {
			((PDimExp) i.next()).apply(this);
			arrayExps.addLast(currentValue);
			currentValue = null;
			dimensions++;
		}
		dimensions += node.getDim().size();
		if (dimensions == 1) {
			currentValue = jimple.newNewArrayExpr(primitiveType, (ca.mcgill.sable.soot.jimple.Value) arrayExps.removeFirst());
		} else {
			currentType = ca.mcgill.sable.soot.ArrayType.v((ca.mcgill.sable.soot.BaseType) primitiveType, dimensions);
			currentValue = jimple.newNewMultiArrayExpr((ca.mcgill.sable.soot.ArrayType) currentType, arrayExps);
			currentType = null;
		}
		ca.mcgill.sable.soot.jimple.Value newValue = currentValue;
		currentValue = null;
		ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), newValue.getType(), 0);
		currentStmts.addElement(jimple.newAssignStmt(lcl, newValue));
		currentValue = lcl;
		currentType = lcl.getType();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypeCastExp
	 */
	public void caseAPrimitiveTypeCastExp(APrimitiveTypeCastExp node) {
		node.getPrimitiveType().apply(this);
		int dimensions = node.getDim().size();
		if (dimensions != 0) {
			currentType = ca.mcgill.sable.soot.ArrayType.v((ca.mcgill.sable.soot.BaseType) currentType, dimensions);
		}
		node.getExp().apply(this);
		ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentType, 0);
		currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newCastExpr(currentValue, currentType)));
		currentValue = lcl;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypePrimaryExp
	 */
	public void caseAPrimitiveTypePrimaryExp(APrimitiveTypePrimaryExp node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedClassInstanceCreationExp
	 */
	public void caseAQualifiedClassInstanceCreationExp(AQualifiedClassInstanceCreationExp node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedConstructorInvocation
	 */
	public void caseAQualifiedConstructorInvocation(AQualifiedConstructorInvocation node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedThisExp
	 */
	public void caseAQualifiedThisExp(AQualifiedThisExp node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AQuestionExp
	 */
	public void caseAQuestionExp(AQuestionExp node) {
		Stmt thenStmt = jimple.newNopStmt();
		Stmt elseStmt = jimple.newNopStmt();
		Stmt endStmt = jimple.newNopStmt();
		Stmt prevTrueBranch = trueBranch;
		Stmt prevFalseBranch = falseBranch;
		trueBranch = elseStmt;
		falseBranch = thenStmt;
		Node n = new AUnaryExp(new AComplementUnaryOperator(new TComplement()), (PExp) node.getFirst().clone());
		PushComplement.push(n);
		n = ((AUnaryExp) n).getExp();
		n.apply(new BooleanExpression(this, trueBranch, falseBranch));
		currentStmts.addElement(thenStmt);
		trueBranch = falseBranch = null;
		node.getSecond().apply(this);
		boolean nullConstant = currentValue.getType() instanceof ca.mcgill.sable.soot.NullType;
		ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
		currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
		currentStmts.addElement(jimple.newGotoStmt(endStmt));
		currentStmts.addElement(elseStmt);
		node.getThird().apply(this);
		if (!(currentValue.getType() instanceof ca.mcgill.sable.soot.NullType) && nullConstant) {
			lcl.setType(currentValue.getType());
		}
		currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
		currentStmts.addElement(endStmt);
		currentValue = lcl;

		trueBranch = prevTrueBranch;
		falseBranch = prevFalseBranch;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AReturnStmt
	 */
	public void caseAReturnStmt(AReturnStmt node) {
		int start = currentStmts.size();

		if (node.getExp() == null) {
			if (!(currentSootMethod.getReturnType() instanceof ca.mcgill.sable.soot.VoidType))
			{
				exceptions.addElement(new CompilerException("Method " + currentSootMethod.getName() + " should not return a value"));
				System.out.println("Method " + currentSootMethod.getName() + " should not return a value");
			}
			
			for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
				SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) e.nextElement();
				Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
				sann.addExitMonitor(exitMon);
				currentStmts.addElement(exitMon);
			}
			currentStmts.addElement(jimple.newReturnVoidStmt());
		} else {
			node.getExp().apply(this);
			boolean f = true;
			if (currentValue instanceof ca.mcgill.sable.soot.jimple.Local) {
				f = !"$ret".equals(((ca.mcgill.sable.soot.jimple.Local) currentValue).getName().trim());
			}
			if (f) {
//				{
//					PExp expr = node.getExp();
//					if(expr instanceof ANameExp)
//					{
//						PName varName = ((ANameExp)expr).getName();
//						if(varName instanceof ASimpleName)
//						{
//							if(((ASimpleName)varName).getId().getText().equals("BonVoyage_falseAlarmThreshold"))
//							{
//								System.out.println();
//							}
//						}
//					}
//				}
				ca.mcgill.sable.soot.jimple.Value exp = currentValue;
				currentValue = null;
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal("$ret", exp.getType(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, exp));
				currentValue = lcl;
			}
			for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
				SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) e.nextElement();
				Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
				sann.addExitMonitor(exitMon);
				currentStmts.addElement(exitMon);
			}
			mann.setRetLocal((ca.mcgill.sable.soot.jimple.Local) currentValue);
			currentStmts.addElement(jimple.newReturnStmt(currentValue));
		}

		int end = currentStmts.size();
		ReturnStmtAnnotation ann = new ReturnStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}
		currentAnnotation.addAnnotation(ann); 
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AShortPrimitiveType
	 */
	public void caseAShortPrimitiveType(AShortPrimitiveType node) {
		currentType = ca.mcgill.sable.soot.ShortType.v();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ASimpleClassInstanceCreationExp
	 */
	public void caseASimpleClassInstanceCreationExp(ASimpleClassInstanceCreationExp node) {
		try {
			ca.mcgill.sable.soot.SootClass sc = getSootClass((new Name(node.getName().toString())).toString());
			String className = sc.getName();
			
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.RefType.v(className), 0);
			currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newNewExpr(ca.mcgill.sable.soot.RefType.v(className))));
			currentValue = null;
			LinkedList args = new LinkedList();
			Vector argTypes = new Vector();
			{
				Object temp[] = node.getArgumentList().toArray();
				for (int i = 0; i < temp.length; i++) {
					((PExp) temp[i]).apply(this);
					args.addLast(currentValue);
					if (currentValue instanceof IntConstant) {
						argTypes.addElement(integralConstantType);
					} else {
						argTypes.addElement(Util.convertType(currentValue.getType(), symbolTable));
					}
					currentValue = null;
				}
			}
			ca.mcgill.sable.soot.SootMethod sm = getSootMethod(className, "<init>", argTypes);
			currentStmts.addElement(jimple.newInvokeStmt(jimple.newSpecialInvokeExpr(lcl, sm, args)));

			for (java.util.Iterator i = CompilationManager.getQuantifiers().iterator(); i.hasNext();) {
				// inline available
				QuantifiedVariable q = (QuantifiedVariable) i.next();
				if (q.isExact()) {
					if (q.getType().toString().equals(sc.getName())) {
						inlineAvailable(q, lcl);
					}
				} else {
					if (isSubtypeOf(className, q.getType().toString())) {
						inlineAvailable(q, lcl);
					}
				}
			}

			currentValue = lcl;
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AStaticInitializerClassBodyDeclaration
	 */
	public void caseAStaticInitializerClassBodyDeclaration(AStaticInitializerClassBodyDeclaration node) {
		if (clinit == null) {
			clinit = new ca.mcgill.sable.soot.SootMethod("<clinit>", new LinkedList(), ca.mcgill.sable.soot.VoidType.v(),
					ca.mcgill.sable.soot.Modifier.PUBLIC | ca.mcgill.sable.soot.Modifier.STATIC);
			clinitBody = (JimpleBody) jimple.newBody(clinit);
		}

		currentSootMethod = clinit;
		currentBody = clinitBody;
		currentLocalNames = new Hashtable();
		currentLocalNamesIterator = new Hashtable();
		currentLocals = staticLocals;
		currentStmts = staticStmts;
		currentValue = null;
		currentType = null;
		currentName = null;
		labels = new LinkedList();
		currentAnnotation = new StaticInitializerAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		currentAnnotation.setFirstLine(lc.getFirstLine());
		currentAnnotation.setFirstColumn(lc.getFirstColumn());
		currentAnnotation.setLastLine(lc.getLastLine());
		currentAnnotation.setLastColumn(lc.getLastColumn());
		currentTraps = new Vector();

		node.getBlock().apply(this);

		for (Enumeration e = currentTraps.elements(); e.hasMoreElements();) {
			clinitBody.addTrap((JTrap) e.nextElement());
		}

		currentLocals = null;
		currentLocalNames = null;
		currentLocalNamesIterator = null;
		currentStmts = null;
		currentValue = null;
		currentType = null;
		currentName = null;
		currentBody = null;
		labels = null;
		currentTraps = null;
		currentSootMethod = null;

		am.addAnnotation(currentSootClass, "static initializer " + staticInitializers++, currentAnnotation);
		currentAnnotation = null;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AStringLiteralLiteral
	 */
	public void caseAStringLiteralLiteral(AStringLiteralLiteral node) {
		currentValue = StringConstant.v(Util.decodeString(node.toString().trim()));
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperConstructorInvocation
	 */
	public void caseASuperConstructorInvocation(ASuperConstructorInvocation node) {
		SuperConstructorInvocationStmtAnnotation ann = new SuperConstructorInvocationStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		try {
			LinkedList args = new LinkedList();
			Vector types = new Vector();
			currentValue = null;
			for (Iterator i = node.getArgumentList().iterator(); i.hasNext();) {
				((PExp) i.next()).apply(this);
				args.addLast(currentValue);
				if (currentValue instanceof IntConstant)
					types.addElement(integralConstantType);
				else
					types.addElement(Util.convertType(currentValue instanceof ConditionExpr ? ca.mcgill.sable.soot.BooleanType.v() : currentValue.getType(), symbolTable));
				currentValue = null;
			}
			ca.mcgill.sable.soot.SootMethod sm = getSootMethod(currentSootClass.getSuperClass().getName(), "<init>", types);
			Stmt stmt = jimple.newInvokeStmt(jimple.newSpecialInvokeExpr(currentThisLocal, sm, args));
			currentStmts.addElement(stmt);
			ann.addStmt(stmt);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
		currentAnnotation.addAnnotation(ann);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperFieldAccess
	 */
	public void caseASuperFieldAccess(ASuperFieldAccess node) {
		try {
			fieldAccess(node.parent() instanceof AFieldAccessLeftHandSide, currentThisLocal,
					currentSootClass.getSuperClass().getName(), node.getId().toString().trim());
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperMethodInvocationExp
	 */
	public void caseASuperMethodInvocationExp(ASuperMethodInvocationExp node) {
		try {
			String methodName = node.getId().toString().trim();
			String className = currentSootClass.getSuperClass().getName();
			ca.mcgill.sable.soot.jimple.Value caller = currentThisLocal;
			currentValue = null;
			LinkedList args = new LinkedList();
			Object temp[] = node.getArgumentList().toArray();
			Vector types = new Vector();
			for (int i = 0; i < temp.length; i++) {
				((PExp) temp[i]).apply(this);
				args.addLast(currentValue);
				if (currentValue instanceof IntConstant) {
					types.addElement(integralConstantType);
				} else {
					types.addElement(Util.convertType(currentValue instanceof ConditionExpr ? ca.mcgill.sable.soot.BooleanType.v() : currentValue.getType(), symbolTable));
				}
			}
			currentValue = null;
			methodInvoke(caller, className, methodName, args, types, true);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ASwitchStmt
	 */
	public void caseASwitchStmt(ASwitchStmt node) {
		LinkedList prevLookupValues = currentLookupValues;
		LinkedList prevTargets = currentTargets;
		Stmt prevDefaultStmt = currentDefaultStmt;
		boolean prevDefaultSwitch = defaultSwitch;

		SwitchStmtAnnotation ann = new SwitchStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);
		BlockStmtAnnotation prevAnnotation = currentAnnotation;

		Stmt startStmt = jimple.newNopStmt();
		Stmt endStmt = jimple.newNopStmt();
		currentTargets = new LinkedList();
		currentDefaultStmt = endStmt;
		Stmt prevBreakTarget = currentBreakTarget;
		Stmt prevContinueTarget = currentContinueTarget;
		currentBreakTarget = endStmt;
		currentContinueTarget = endStmt;
		currentStmts.addElement(startStmt);


		int start = currentStmts.size();

		node.getExp().apply(this);

		int end = currentStmts.size();

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}

		ca.mcgill.sable.soot.jimple.Value switchValue = currentValue;
		currentValue = null;
		Vector tempStmts;
		LinkedList casesStmts = new LinkedList();

		currentLookupValues = new LinkedList();

		for (Iterator i = node.getSwitchBlockStmtGroup().iterator(); i.hasNext();) {
			defaultSwitch = false;
			ASwitchBlockStmtGroup switchBlockStmtGroup = (ASwitchBlockStmtGroup) i.next();
			tempStmts = currentStmts;
			currentStmts = new Vector();

			currentAnnotation = new BlockStmtAnnotation(switchBlockStmtGroup);

			for (Iterator j = switchBlockStmtGroup.getBlockedStmt().iterator(); j.hasNext();) {
				((PBlockedStmt) j.next()).apply(this);
			}

			Stmt caseStmt = jimple.newNopStmt();
			casesStmts.addLast(caseStmt);
			for (int k = 0; k < currentStmts.size(); k++) {
				casesStmts.addLast(currentStmts.elementAt(k));
			}
			currentStmts = tempStmts;

			for (Iterator j = switchBlockStmtGroup.getSwitchLabel().iterator(); j.hasNext();) {
				boolean tempDefault = defaultSwitch;

				((PSwitchLabel) j.next()).apply(this);

				if (tempDefault == defaultSwitch) {
					currentTargets.addLast(caseStmt);
					ann.addSwitchCase((Integer) currentLookupValues.getLast(), currentAnnotation);
				} else {
					currentDefaultStmt = caseStmt;
					ann.setDefaultAnnotation(currentAnnotation);
				}
			}
		}
		Stmt caseStmt = endStmt;
		for (Iterator i = node.getSwitchLabel().iterator(); i.hasNext();) {
			boolean tempDefault = defaultSwitch;
			((PSwitchLabel) i.next()).apply(this);
			if (tempDefault == defaultSwitch)
				currentTargets.addLast(caseStmt);
			else {
				currentDefaultStmt = caseStmt;
			}
		}

		Stmt stmt = jimple.newLookupSwitchStmt(switchValue, currentLookupValues, currentTargets, currentDefaultStmt);
		currentStmts.addElement(stmt);
		ann.addStmt(stmt);

		currentLookupValues = null;
		currentTargets = null;
		currentDefaultStmt = null;
		defaultSwitch = false;
		for (Iterator i = casesStmts.iterator(); i.hasNext();) {
			currentStmts.addElement(i.next());
		}
		currentStmts.addElement(endStmt);
		currentValue = null;
		currentBreakTarget = prevBreakTarget;
		currentContinueTarget = prevContinueTarget;

		currentAnnotation = prevAnnotation;
		currentAnnotation.addAnnotation(ann);

		currentLookupValues = prevLookupValues;
		currentTargets = prevTargets;
		currentDefaultStmt = prevDefaultStmt;
		defaultSwitch = prevDefaultSwitch;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ASynchronizedModifier
	 */
	public void caseASynchronizedModifier(ASynchronizedModifier node) {
		node.getSynchronized();
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ASynchronizedStmt
	 */
	public void caseASynchronizedStmt(ASynchronizedStmt node) {
		try {
			SynchronizedStmtAnnotation ann = new SynchronizedStmtAnnotation(node);
			LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
			ann.setFirstLine(lc.getFirstLine());
			ann.setFirstColumn(lc.getFirstColumn());
			ann.setLastLine(lc.getLastLine());
			ann.setLastColumn(lc.getLastColumn());
			int ln = LineExtractor.extractLine(node);
			if (ln <= 0) ln = methodLine;
			am.putFilenameLinePairAnnotation(
					new FilenameLinePair(currentClassOrInterfaceType.getPath(),
							ln),
					ann);

			Stmt beginStmt = jimple.newNopStmt();
			Stmt stmt1 = jimple.newNopStmt();
			Stmt stmt2 = jimple.newNopStmt();
			Stmt endStmt = jimple.newNopStmt();
			Stmt gotoStmt = jimple.newGotoStmt(endStmt);
			currentStmts.addElement(beginStmt);

			int start = currentStmts.size();
			if (node.getExp() != null)
				node.getExp().apply(this);

			ca.mcgill.sable.soot.jimple.Value lockValue = currentValue;

			ann.setLockValue(lockValue);

			Stmt enterMonitor = jimple.newEnterMonitorStmt(lockValue);
			Stmt exitMonitor = jimple.newExitMonitorStmt(lockValue);

			ann.setEnterMonitor(enterMonitor);
			ann.addExitMonitor(exitMonitor);

			synchAnnotations.insertElementAt(ann, 0);
			finallyAndSynch.insertElementAt(ann, 0);

			currentValue = null;
			currentStmts.addElement(enterMonitor);
			int end = currentStmts.size();

			for (int i = start; i < end; i++) {
				ann.addStmt((Stmt) currentStmts.elementAt(i));
			}

			currentStmts.addElement(stmt1);

			if (node.getBlock() != null) {
				BlockStmtAnnotation prevAnnotation = currentAnnotation;
				currentAnnotation = new BlockStmtAnnotation(node.getBlock());

				node.getBlock().apply(this);

				ann.setBlockAnnotation(currentAnnotation);
				currentAnnotation = prevAnnotation;
			} else {
				ann.setBlockAnnotation(new BlockStmtAnnotation(null));
			}

			Stmt lastStmt = (Stmt) currentStmts.lastElement();

			boolean f = false;
			if ((lastStmt instanceof ReturnStmt) || (lastStmt instanceof ReturnVoidStmt)
					|| (lastStmt instanceof ThrowStmt) || (lastStmt instanceof GotoStmt)) {
				f = true;
				stmt2 = lastStmt;
			} else {
				currentStmts.addElement(stmt2);
				SequentialAnnotation seqann = new SequentialAnnotation(null);
				seqann.addStmt(exitMonitor);
				seqann.addStmt(gotoStmt);
				((BlockStmtAnnotation) ann.getBlockAnnotation()).addAnnotation(seqann);

				currentStmts.addElement(exitMonitor);
				currentStmts.addElement(gotoStmt);
			}


			start = currentStmts.size();

			ca.mcgill.sable.soot.SootClass sc = getSootClass("java.lang.Throwable");
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.RefType.v("java.lang.Throwable"), 0);
			Stmt stmt3 = jimple.newIdentityStmt(lcl, jimple.newCaughtExceptionRef(currentBody));
			currentStmts.addElement(stmt3);

			for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
				SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) e.nextElement();
				Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
				if (sann != ann)	sann.addExitMonitor(exitMon);
				currentStmts.addElement(exitMon);
			}

			Stmt stmt4 = jimple.newThrowStmt(lcl);
			currentStmts.addElement(stmt4);

			end = currentStmts.size();

			SequentialAnnotation sann = new SequentialAnnotation(null);

			for (int i = start; i < end; i++) {
				sann.addStmt((Stmt) currentStmts.elementAt(i));
			}

			ann.setCatchAnnotation(sann);

			if (!f)
				currentStmts.addElement(endStmt);

			currentBody.addTrap(jimple.newTrap(sc, stmt1, stmt2, stmt3));

			synchAnnotations.removeElementAt(0);
			finallyAndSynch.removeElementAt(0);

			currentAnnotation.addAnnotation(ann);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AThisConstructorInvocation
	 */
	public void caseAThisConstructorInvocation(AThisConstructorInvocation node) {
		ThisConstructorInvocationStmtAnnotation ann = new ThisConstructorInvocationStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		try {
			LinkedList args = new LinkedList();
			Vector types = new Vector();
			currentValue = null;
			for (Iterator i = node.getArgumentList().iterator(); i.hasNext();) {
				((PExp) i.next()).apply(this);
				args.addLast(currentValue);
				if (currentValue instanceof IntConstant)
					types.addElement(integralConstantType);
				else
					types.addElement(Util.convertType(currentValue instanceof ConditionExpr ? ca.mcgill.sable.soot.BooleanType.v() : currentValue.getType(), symbolTable));
				currentValue = null;
			}
			ca.mcgill.sable.soot.SootMethod sm = getSootMethod(currentSootClass.getName(), "<init>", types);
			Stmt stmt = jimple.newInvokeStmt(jimple.newSpecialInvokeExpr(currentThisLocal, sm, args));
			currentStmts.addElement(stmt);
			ann.addStmt(stmt);
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
		currentAnnotation.addAnnotation(ann);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AThisExp
	 */
	public void caseAThisExp(AThisExp node) {
		currentValue = currentThisLocal;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AThrowStmt
	 */
	public void caseAThrowStmt(AThrowStmt node) {
		int start = currentStmts.size();

		for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
			SynchronizedStmtAnnotation sann = (SynchronizedStmtAnnotation) e.nextElement();
			Stmt exitMon = jimple.newExitMonitorStmt(sann.getLockValue());
			sann.addExitMonitor(exitMon);
			currentStmts.addElement(exitMon);
		}

		node.getExp().apply(this);
		currentStmts.addElement(jimple.newThrowStmt(currentValue));
		currentValue = null;

		int end = currentStmts.size();
		ThrowStmtAnnotation ann = new ThrowStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}
		currentAnnotation.addAnnotation(ann); 
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ATrueBooleanLiteral
	 */
	public void caseATrueBooleanLiteral(ATrueBooleanLiteral node) {
		currentValue = IntConstant.v(1);
		integralConstantType = edu.ksu.cis.bandera.jjjc.symboltable.BooleanType.TYPE;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ATryFinallyStmt
	 */
	public void caseATryFinallyStmt(ATryFinallyStmt node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
		/*
	finallyAndSynch.insertElementAt(node.getFinally(), 0);

	Stmt prevBeginTrapStmt = beginTrapStmt;
	Stmt prevEndTrapStmt = endTrapStmt;
	Stmt prevEndTryStmt = endTryStmt;

	TryFinallyStmtAnnotation ann = new TryFinallyStmtAnnotation(node);
	am.putFilenameLinePairAnnotation(
			new FilenameLinePair(currentClassOrInterfaceType.getPath(),
					LineExtractor.extractLine(node)),
			ann);

	TryStmtAnnotation prevAnn = currentTryAnnotation;
	currentTryAnnotation = ann;

	BlockStmtAnnotation prevAnnotation = currentAnnotation;

	endTryStmt = jimple.newNopStmt();
	beginTrapStmt = jimple.newNopStmt();

	currentStmts.addElement(beginTrapStmt);

	currentAnnotation = new BlockStmtAnnotation(node.getBlock());

	node.getBlock().apply(this);

	Stmt lastStmt = (Stmt) currentStmts.lastElement();


	SequentialAnnotation sann = new SequentialAnnotation(null);
	endTrapStmt = jimple.newGotoStmt(endTryStmt);
	sann.addStmt(endTrapStmt);
	currentAnnotation.addAnnotation(sann);
	ann.setBlockAnnotation(currentAnnotation);

	currentStmts.addElement(endTrapStmt);

	for (Iterator i = node.getCatchClause().iterator(); i.hasNext();) {
		PCatchClause catchClause = (PCatchClause) i.next();
		catchClause.apply(this);
	}
	currentStmts.addElement(endTryStmt);

	// finally
  currentAnnotation = new BlockStmtAnnotation(node.getFinally());
  node.getFinally().apply(this);
  ann.setFinallyAnnotation(currentAnnotation);

	// goto endStmt
	// temp := @caughtexception
	// finally...
	// throw temp
	Stmt endStmt = jimple.newNopStmt();
	currentAnnotation = new BlockStmtAnnotation(null);
	sann = new SequentialAnnotation(null);
	Stmt stmt = jimple.newGotoStmt(endStmt);
	currentStmts.addElement(stmt);
	sann.addStmt(stmt);
	ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.RefType.v("java.lang.Throwable"), 0);
	stmt = jimple.newIdentityStmt(lcl, jimple.newCaughtExceptionRef(currentBody));
	try {
		ca.mcgill.sable.soot.jimple.Trap trap = jimple.newTrap(getSootClass("java.lang.Throwable"), beginTrapStmt, endTryStmt, stmt);
		currentTraps.addElement(trap);
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	currentStmts.addElement(stmt);
	sann.addStmt(stmt);
	currentAnnotation.addAnnotation(sann);
  node.getFinally().apply(this);
	sann = new SequentialAnnotation(null);
	for (Enumeration e = synchAnnotations.elements(); e.hasMoreElements();) {
		SynchronizedStmtAnnotation syann = (SynchronizedStmtAnnotation) e.nextElement();
		Stmt exitMon = jimple.newExitMonitorStmt(syann.getLockValue());
		syann.addExitMonitor(exitMon);
	  currentStmts.addElement(exitMon);
	  sann.addStmt(exitMon);
	}
	stmt = jimple.newThrowStmt(lcl);
	currentStmts.addElement(stmt);
	sann.addStmt(stmt);
	currentAnnotation.addAnnotation(sann);
	ann.setFinallyExceptionAnnotation(currentAnnotation);

	currentStmts.addElement(endStmt);

	currentAnnotation = prevAnnotation;
	currentAnnotation.addAnnotation(ann);
	currentTryAnnotation = prevAnn;

	beginTrapStmt = prevBeginTrapStmt;
	endTrapStmt = prevEndTrapStmt;
	endTryStmt = prevEndTryStmt;

	finallyAndSynch.removeElementAt(0);
		 */
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.ATryStmt
	 */
	public void caseATryStmt(ATryStmt node) {
		Stmt prevBeginTrapStmt = beginTrapStmt;
		Stmt prevEndTrapStmt = endTrapStmt;
		Stmt prevEndTryStmt = endTryStmt;

		TryStmtAnnotation ann = new TryStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);

		TryStmtAnnotation prevAnn = currentTryAnnotation;
		currentTryAnnotation = ann;

		BlockStmtAnnotation prevAnnotation = currentAnnotation;

		endTryStmt = jimple.newNopStmt();
		beginTrapStmt = jimple.newNopStmt();

		currentStmts.addElement(beginTrapStmt);

		currentAnnotation = new BlockStmtAnnotation(node.getBlock());

		node.getBlock().apply(this);

		Stmt lastStmt = (Stmt) currentStmts.lastElement();

		boolean f = false;

		if ((lastStmt instanceof ReturnStmt) || (lastStmt instanceof ReturnVoidStmt)
				|| (lastStmt instanceof ThrowStmt) || (lastStmt instanceof GotoStmt)) {
			f = true;
			endTrapStmt = lastStmt;
		} else {
			SequentialAnnotation sann = new SequentialAnnotation(null);
			endTrapStmt = jimple.newGotoStmt(endTryStmt);
			sann.addStmt(endTrapStmt);
			currentAnnotation.addAnnotation(sann);
			currentStmts.addElement(endTrapStmt);
		}

		ann.setBlockAnnotation(currentAnnotation);

		catchHasEnd = true;

		for (Iterator i = node.getCatchClause().iterator(); i.hasNext();) {
			PCatchClause catchClause = (PCatchClause) i.next();
			catchClause.apply(this);
		}

		if (!f || catchHasEnd)
			currentStmts.addElement(endTryStmt);

		currentAnnotation = prevAnnotation;
		currentAnnotation.addAnnotation(ann);
		currentTryAnnotation = prevAnn;

		beginTrapStmt = prevBeginTrapStmt;
		endTrapStmt = prevEndTrapStmt;
		endTryStmt = prevEndTryStmt;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AUnaryExp
	 */
	public void caseAUnaryExp(AUnaryExp node) {
		String operator = node.getUnaryOperator().toString().trim();
		if (operator.equals("++")) {
			preIncDecExp(node, true);
		} else if (operator.equals("--")) {
			preIncDecExp(node, false);
		} else if (operator.equals("+")) {
			node.getExp().apply(this);
		} else if (operator.equals("-")) {
			node.getExp().apply(this);
			if (currentValue instanceof IntConstant) {
				currentValue = IntConstant.v(-((IntConstant) currentValue).value);
			} else if (currentValue instanceof LongConstant) {
				currentValue = LongConstant.v(-((LongConstant) currentValue).value);
			} else if (currentValue instanceof FloatConstant) {
				currentValue = FloatConstant.v(-((FloatConstant) currentValue).value);
			} else if (currentValue instanceof DoubleConstant) {
				currentValue = DoubleConstant.v(-((DoubleConstant) currentValue).value);
			} else {
				currentValue = jimple.newNegExpr(currentValue);
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), currentValue.getType(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
				currentValue = lcl;
			}
		} else if (operator.equals("~")) {
			node.getExp().apply(this);
			ca.mcgill.sable.soot.jimple.Value operand;
			if (currentValue.getType() instanceof ca.mcgill.sable.soot.IntType)
				operand = IntConstant.v(-1);
			else
				operand = LongConstant.v(-1);
			currentStmts.addElement(jimple.newAssignStmt(currentValue, jimple.newXorExpr(currentValue, operand)));
		} else if (operator.equals("!")) {
			ca.mcgill.sable.soot.jimple.Local lcl = null;
			if (trueBranch == null) {
				lcl = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.BooleanType.v(), 0);
				trueBranch = jimple.newAssignStmt(lcl, IntConstant.v(1));
				falseBranch = jimple.newAssignStmt(lcl, IntConstant.v(0));
			}

			Node n = (Node) node.clone();
			PushComplement.push(n);
			n = ((AUnaryExp) n).getExp();
			n.apply(new BooleanExpression(this, trueBranch, falseBranch));
			if (!(currentValue instanceof ca.mcgill.sable.soot.jimple.Local)) {
				ca.mcgill.sable.soot.jimple.Local l = declareLocal(nameGen.newName(), ca.mcgill.sable.soot.BooleanType.v(), 0);
				currentStmts.addElement(jimple.newAssignStmt(l, currentValue));
				currentValue = l;
			}

			if (lcl != null) {
				Stmt endStmt = jimple.newNopStmt();
				currentStmts.addElement(falseBranch);
				currentStmts.addElement(jimple.newGotoStmt(endStmt));
				currentStmts.addElement(trueBranch);
				currentStmts.addElement(endStmt);
				trueBranch = null;
				falseBranch = null;
				currentValue = lcl;
			}
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AVariableDeclaratorId
	 */
	public void caseAVariableDeclaratorId(AVariableDeclaratorId node) {
		currentName = new String(node.getId().toString().trim());

		int dimensions = node.getDim().size();

		if (dimensions > 0)
			if (currentType instanceof ca.mcgill.sable.soot.ArrayType) {
				ca.mcgill.sable.soot.ArrayType arrayType = (ca.mcgill.sable.soot.ArrayType) currentType;
				currentType = ca.mcgill.sable.soot.ArrayType.v(arrayType.baseType, arrayType.numDimensions + dimensions);
			} else
				currentType = ca.mcgill.sable.soot.ArrayType.v((ca.mcgill.sable.soot.BaseType) currentType, dimensions);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AVoidExp
	 */
	public void caseAVoidExp(AVoidExp node) {
		System.out.println("Unsupported feature of Java: " + node);
		exceptions.addElement(new CompilerException("Unsupported feature of Java: " + node));
		throw new Error("Unsupported feature of Java: " + node);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AWhileStmt
	 */
	public void caseAWhileStmt(AWhileStmt node) {
		Stmt blockStmt = jimple.newNopStmt();
		Stmt testStmt = jimple.newNopStmt();
		Stmt endStmt = jimple.newNopStmt();
		Stmt initStmt = jimple.newGotoStmt(testStmt);

		WhileStmtAnnotation ann = new WhileStmtAnnotation(node);
		LineColumn lc = TokenLineColumnNumberExtractor.getLineColumn(node);
		ann.setFirstLine(lc.getFirstLine());
		ann.setFirstColumn(lc.getFirstColumn());
		ann.setLastLine(lc.getLastLine());
		ann.setLastColumn(lc.getLastColumn());
		am.putFilenameLinePairAnnotation(
				new FilenameLinePair(currentClassOrInterfaceType.getPath(),
						LineExtractor.extractLine(node)),
				ann);
		BlockStmtAnnotation prevAnnotation = currentAnnotation;

		Object prevLastSynch = lastFinallySynchBeforeControl;
		if (synchAnnotations.size() > 0)
			lastFinallySynchBeforeControl = (Annotation) synchAnnotations.lastElement();

		Stmt prevBreakTarget = currentBreakTarget;
		Stmt prevContinueTarget = currentContinueTarget;
		currentBreakTarget = endStmt;
		currentContinueTarget = testStmt;

		currentStmts.addElement(initStmt);

		currentStmts.addElement(blockStmt);

		currentAnnotation = new BlockStmtAnnotation(node.getBlock());
		node.getBlock().apply(this);

		SequentialAnnotation sann = new SequentialAnnotation(null);
		sann.addStmt(initStmt);
		ann.setInitAnnotation(sann);
		ann.setBlockAnnotation(getFirstEnclosedAnnotation(currentAnnotation));

		currentStmts.addElement(testStmt);

		int start = currentStmts.size();

		Node n = (Node) node.getExp().clone();
		PushComplement.push(n);
		if (node.getExp() instanceof AUnaryExp)
			if ("!".equals(((AUnaryExp) node.getExp()).getUnaryOperator().toString().trim()))
				n = ((AUnaryExp) n).getExp();

		Stmt prevTrueBranch = trueBranch;
		Stmt prevFalseBranch = falseBranch;
		trueBranch = blockStmt;
		falseBranch = endStmt;

		n.apply(new BooleanExpression(this, trueBranch, falseBranch));

		ann.setIndefinite(currentStmts.elementAt(currentStmts.size() - 1) instanceof JGotoStmt);

		int end = currentStmts.size();

		for (int i = start; i < end; i++) {
			ann.addStmt((Stmt) currentStmts.elementAt(i));
		}

		currentStmts.addElement(endStmt);

		trueBranch = prevTrueBranch;
		falseBranch = prevFalseBranch;
		currentBreakTarget = prevBreakTarget;
		currentContinueTarget = prevContinueTarget;

		lastFinallySynchBeforeControl = prevLastSynch;

		currentAnnotation = prevAnnotation;
		currentAnnotation.addAnnotation(ann);
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.Start
	 */
	public void caseStart(Start node) {
		node.getPCompilationUnit().apply(this);
	}
	/**
	 * 
	 * @param annotation edu.ksu.cis.bandera.annotations.Annotation
	 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
	 */
	private void checkAnnotation(Annotation annotation, JimpleBody body) {
		String line = System.getProperty("line.separator");
		boolean failed = false;

		String name = currentSootMethod.toString();
		System.out.print("Checking annotation for " + name + ", phase 1...");

		StmtList stmtList = body.getStmtList();

		for (Iterator i = stmtList.iterator(); i.hasNext();) {
			Stmt stmt = (Stmt) i.next();
			int idx = stmtList.indexOf(stmt);
			try {
				if (annotation.getContainingAnnotation(stmt) == null) {
					System.out.println(line + name + ", statement index: " + idx + ", " + stmt);
					failed = true;
				}
			} catch (Exception e) {
				System.out.println(line + e + name + ", statement index: " + idx + ", " + stmt);
				failed = true;
			}
		}

		if (failed) System.out.println("Phase 1 failed");
		else System.out.println(" passed");

		Object[] bodyStmts = stmtList.toArray();
		Stmt[] stmts = annotation.getStatements();

		System.out.println("Checking annotation for " + name + ", phase 2... "
				+ ((bodyStmts.length == stmts.length) ? "passed" : "failed"));

		System.out.print("Checking annotation for " + name + ", phase 3...");

		failed = false;

		try {

			for (int i = 0; i < stmts.length; i++) {
				if (stmts[i] != bodyStmts[i]) {
					System.out.println(line + name + ", statement index: " + i + ":" + line
							+ bodyStmts[i] + " <!=> " + stmts[i]);
					Annotation a = annotation.getContainingAnnotation(stmts[i]);
					System.out.println(a.getClass().getName());
					System.out.println(stmtList.contains(stmts[i]));
					failed = true;
				}
			}
		} catch (Exception e) {
			failed = true;
		}

		if (failed) System.out.println(line + "Phase 3 failed");
		else System.out.println(" passed");
	}
	/**
	 * 
	 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
	 */
	private void convertLocalTypes(JimpleBody body) {
		for (Iterator i = body.getLocals().iterator(); i.hasNext();) {
			ca.mcgill.sable.soot.jimple.Local l = (ca.mcgill.sable.soot.jimple.Local) i.next();
			ca.mcgill.sable.soot.Type type = l.getType();

			if ((type instanceof ca.mcgill.sable.soot.BooleanType) || (type instanceof ca.mcgill.sable.soot.ByteType)
					|| (type instanceof ca.mcgill.sable.soot.CharType) || (type instanceof ca.mcgill.sable.soot.ShortType)) {
				l.setType(ca.mcgill.sable.soot.IntType.v());
			}
		}
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.jimple.Unit
	 * @param labelName java.lang.String
	 */
	private ca.mcgill.sable.soot.jimple.Unit declareLabel(String labelName) throws CompilerException {
		for (Iterator i = labels.iterator(); i.hasNext();) {
			Hashtable labelTable = (Hashtable) i.next();
			if (labelTable == null) continue;
			if (labelTable.get(labelName) != null)
				throw new CompilerException("Labeled statement cannot contain the same label identifier " + labelName);
		}
		Stmt stmt = jimple.newNopStmt();
		currentStmts.addElement(stmt);
		currentLabels.put(labelName, stmt);
		Vector v = new Vector();
		v.addElement(lastFinallySynchBeforeControl);
		currentLabels.put(stmt, v);
		return stmt;
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.jimple.Local
	 * @param name java.lang.String
	 * @param type ca.mcgill.sable.soot.Type
	 * @param modifiers int
	 */
	private ca.mcgill.sable.soot.jimple.Local declareLocal(String name, ca.mcgill.sable.soot.Type type, int modifiers) {
		if ("$ret".equals(name)) {
			if (currentLocalNames.get(name) != null) {
				return (ca.mcgill.sable.soot.jimple.Local) currentLocals.get(name);
			}
		}
		if (type instanceof ca.mcgill.sable.soot.NullType) {
			type = ca.mcgill.sable.soot.RefType.v("java.lang.Object");
		} 

		String localName;
		if (currentLocalNames.get(name) != null) {
			currentLocalNames.remove(name);
			localName = getUniqueName(name);
			if (symbolTable.getNumScopeLevels() != 0) {
				if (symbolTable.hasLocalVariableDeclared(new Name(name)))
				{
					exceptions.addElement(new CompilerException("ca.mcgill.sable.soot.jimple.Local variable " + name + " has already been declared"));
					System.out.println("ca.mcgill.sable.soot.jimple.Local variable " + name + " has already been declared");
				}
			}
		} else
			localName = name;

		currentLocalNames.put(name, localName);
		if (symbolTable.getNumScopeLevels() != 0) {
			try {
				// modifiers doesn't matter
				symbolTable.declareLocalVariable(new Variable(0, Util.convertType(type, symbolTable),
						new Name(name)));
			} catch (CompilerException e) {
				exceptions.addElement(e);
				System.out.println(e.getMessage());
			}
		}

		ca.mcgill.sable.soot.jimple.Local tempLocal = jimple.newLocal(localName, type);
		currentLocals.put(tempLocal.getName(), tempLocal);
		return tempLocal;
	}
	/**
	 * 
	 * @param lhs boolean
	 * @param caller ca.mcgill.sable.soot.jimple.Value
	 * @param className java.lang.String
	 * @param fieldName java.lang.String
	 */
	private void fieldAccess(boolean lhs, ca.mcgill.sable.soot.jimple.Value caller, String className, String fieldName) throws CompilerException {
		ca.mcgill.sable.soot.SootField sf = getSootField(className, fieldName);
		if (!lhs) {
			String tempName = nameGen.newName();
			ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(tempName, sf.getType(), 0);
			if (ca.mcgill.sable.soot.Modifier.isStatic(sf.getModifiers())) {
//				currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newStaticFieldRef(sf)));
				/* [Thomas, May 22, 2017]
				 * All static fields will start with "_static_"
				 * */
				lcl.setName("_static_" + fieldName);
				currentValue = lcl;
			} else {
				if (caller != null) {
					if(caller.toString().startsWith("_static_"))
					{
						lcl.setName(caller.toString() + "." + fieldName);
					}
					else
					{
						currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newInstanceFieldRef(caller, sf)));
					}
					currentValue = lcl;
				} else {
					exceptions.addElement(new CompilerException("Could not access instance field " + fieldName + " in static method " + currentSootMethod.getName()));
					System.out.println("Could not access instance field " + fieldName + " in static method " + currentSootMethod.getName());
					return;
				}
			}
			currentValue = lcl;
		} else {
			if (ca.mcgill.sable.soot.Modifier.isStatic(sf.getModifiers())) {
				currentValue = jimple.newStaticFieldRef(sf);
			} else {
				if (caller != null) {
					currentValue = jimple.newInstanceFieldRef(caller, sf);
				} else {
					exceptions.addElement(new CompilerException("Could not access instance field " + fieldName + " in static method " + currentSootMethod.getName()));
					System.out.println("Could not access instance field " + fieldName + " in static method " + currentSootMethod.getName());
					currentValue = null;
					return;
				}
			}
		}
	}
	/**
	 * 
	 * @return java.util.Vector
	 */
	public Vector getCompiledClasses() {
		return compiledClasses;
	}
	/**
	 * 
	 * @return java.util.Vector
	 */
	public Vector getCurrentStmts() {
		return currentStmts;
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.Type
	 */
	public ca.mcgill.sable.soot.Type getCurrentType() {
		return currentType;
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.jimple.Value
	 */
	public ca.mcgill.sable.soot.jimple.Value getCurrentValue() {
		return currentValue;
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
	 * @return edu.ksu.cis.bandera.annotations.Annotation
	 * @param annotation edu.ksu.cis.bandera.annotations.BlockStmtAnnotation
	 */
	private Annotation getFirstEnclosedAnnotation(BlockStmtAnnotation annotation) {
		Vector annotations = annotation.getContainedAnnotations();
		return (annotations.size() > 0) ? (Annotation) annotations.elementAt(0)
				: new EmptyStmtAnnotation(null);
	}
	/**
	 * 
	 * @return java.lang.Object
	 * @param unit ca.mcgill.sable.soot.jimple.Unit
	 */
	private Object getLabelObject(ca.mcgill.sable.soot.jimple.Unit unit) {
		for (Iterator i = labels.iterator(); i.hasNext();) {
			Hashtable labelTable = (Hashtable) i.next();
			if (labelTable.get(unit) != null)
				return labelTable.get(unit);
		}
		return null;
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.jimple.Unit
	 * @param name java.lang.String
	 */
	private ca.mcgill.sable.soot.jimple.Unit getLabelUnit(String name) {
		for (Iterator i = labels.iterator(); i.hasNext();) {
			Hashtable labelTable = (Hashtable) i.next();
			if (labelTable.get(name) != null)
				return (ca.mcgill.sable.soot.jimple.Unit) labelTable.get(name);
		}
		return null;
	}
	/**
	 * 
	 * @param q edu.ksu.cis.bandera.specification.datastructure.Quantifier
	 */
	private ca.mcgill.sable.soot.jimple.Value getPlaceHolder(QuantifiedVariable q) {
		String fName = "quantification$" + q.getName();

		ca.mcgill.sable.soot.SootField f;

		if (CompilationManager.getFieldForQuantifier(fName) == null) {
			f = new ca.mcgill.sable.soot.SootField(fName, ca.mcgill.sable.soot.RefType.v(q.getType().toString()), ca.mcgill.sable.soot.Modifier.PUBLIC | ca.mcgill.sable.soot.Modifier.STATIC);
			CompilationManager.addFieldForQuantifier(f);
		} else {
			f = CompilationManager.getFieldForQuantifier(fName);
		}

		return jimple.newStaticFieldRef(f);
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.Type
	 * @param firstType ca.mcgill.sable.soot.Type
	 * @param secondType ca.mcgill.sable.soot.Type
	 */
	private ca.mcgill.sable.soot.Type getResultType(ca.mcgill.sable.soot.Type firstType, ca.mcgill.sable.soot.Type secondType) {
		if (((firstType instanceof ca.mcgill.sable.soot.RefType) && ("java.lang.String".equals(((ca.mcgill.sable.soot.RefType) firstType).className)))
				|| ((secondType instanceof ca.mcgill.sable.soot.RefType) && ("java.lang.String".equals(((ca.mcgill.sable.soot.RefType) secondType).className)))) {
			return ca.mcgill.sable.soot.RefType.v("java.lang.String");
		} else if ((firstType instanceof ca.mcgill.sable.soot.DoubleType)
				|| (secondType instanceof ca.mcgill.sable.soot.DoubleType)) {
			return ca.mcgill.sable.soot.DoubleType.v();
		} else if ((firstType instanceof ca.mcgill.sable.soot.FloatType)
				|| (secondType instanceof ca.mcgill.sable.soot.FloatType)) {
			return ca.mcgill.sable.soot.FloatType.v();
		} else if ((firstType instanceof ca.mcgill.sable.soot.LongType)
				|| (secondType instanceof ca.mcgill.sable.soot.LongType)) {
			return ca.mcgill.sable.soot.LongType.v();
		} else if ((firstType instanceof ca.mcgill.sable.soot.RefType) || (secondType instanceof ca.mcgill.sable.soot.RefType)) {
			return null;
		} else {
			return ca.mcgill.sable.soot.IntType.v();
		}
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.SootClass
	 * @param name java.lang.String
	 */
	private ca.mcgill.sable.soot.SootClass getSootClass(String name) throws CompilerException {
		ClassOrInterfaceType type = symbolTable.resolveClassOrInterfaceType(new Name(name));
		type.loadReferences();
		String typeName = type.getName().toString();

		if (scm.managesClass(typeName))
			return scm.getClass(typeName);


		//if(typeName.startsWith("java.") || typeName.startsWith("Bandera."))
		if(Preferences.getBanderaBuiltinClassSet().contains(typeName))
		{
			ca.mcgill.sable.soot.SootClass result = scm.getClass(typeName);
			result.resolve();
			return result;
		}

		ca.mcgill.sable.soot.SootClass sc = new ca.mcgill.sable.soot.SootClass(typeName);
		sc.setResolved(true);
		scm.addClass(sc);

		int modifiers = Util.convertModifiers(type.getModifiers());

		if (type.isInterface())
			modifiers |= ca.mcgill.sable.soot.Modifier.INTERFACE;

		sc.setModifiers(modifiers);

		if (type.getDirectSuperClass() != null)
			sc.setSuperClass(getSootClass(type.getDirectSuperClass().getName().toString()));

		for (Enumeration e = type.getDirectSuperInterfaces(); e.hasMoreElements();) {
			sc.addInterface(getSootClass(((ClassOrInterfaceType) e.nextElement()).getName().toString()));
		}

		return sc;
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.SootField
	 * @param typeName java.lang.String
	 * @param fieldName java.lang.String
	 */
	private ca.mcgill.sable.soot.SootField getSootField(String typeName, String fieldName) throws CompilerException {
		try {
			ClassOrInterfaceType classType = symbolTable.resolveClassOrInterfaceType(new Name(typeName));
			classType.loadReferences();
			Field f = (Field) classType.getField(new Name(fieldName));
			String declaringType = f.getDeclaringClassOrInterface().toString();
			ca.mcgill.sable.soot.SootClass sc = getSootClass(declaringType);
			if (sc.declaresField(fieldName))
				return sc.getField(fieldName);
			int modifiers = Util.convertModifiers(f.getModifiers());
			if (f.getDeclaringClassOrInterface().isInterface())
				modifiers = ca.mcgill.sable.soot.Modifier.PUBLIC | ca.mcgill.sable.soot.Modifier.STATIC | ca.mcgill.sable.soot.Modifier.FINAL;
			ca.mcgill.sable.soot.SootField sf = new ca.mcgill.sable.soot.SootField(fieldName, Util.convertType(f.getType()), modifiers);
			sc.addField(sf);
			return sf;
		} catch (CompilerException e) {
			throw e;
		}
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.SootMethod
	 * @param typeName java.lang.String
	 * @param methodName java.lang.String
	 * @param parameterTypes ca.mcgill.sable.util.LinkedList
	 */
	private ca.mcgill.sable.soot.SootMethod getSootMethod(String typeName, String methodName, Vector parameterTypes) throws CompilerException {
		/*
	if ("Bandera".equals(typeName)) {
		if ("assert".equals(methodName)) {
		    if(assertMethod == null) {
			SootClass banderaClass = scm.getClass("Bandera");
			assertMethod = banderaClass.getMethod("assert");
		    }
			return assertMethod;
		} else if ("choose".equals(methodName)) {
		    if(chooseMethod == null) {
			SootClass banderaClass = scm.getClass("Bandera");
			chooseMethod = banderaClass.getMethod("choose");
		    }
			return chooseMethod;
		}
	}
		 */
		try {
			LinkedList parmTypes = new LinkedList();
			Method m;
			ca.mcgill.sable.soot.SootMethod sm;
			if ("<init>".equals(methodName)) {
				m = symbolTable.resolveConstructor(new Name(typeName), parameterTypes);
				for (Enumeration e = m.getParameterTypes().elements(); e.hasMoreElements();) {
					parmTypes.addLast(Util.convertType((edu.ksu.cis.bandera.jjjc.symboltable.Type) e.nextElement()));
				}
				ca.mcgill.sable.soot.SootClass sc = getSootClass(m.getDeclaringClassOrInterface().getName().toString());
				if (sc.declaresMethod(methodName, parmTypes))
					return sc.getMethod(methodName, parmTypes);
				sm = new ca.mcgill.sable.soot.SootMethod(methodName, parmTypes, ca.mcgill.sable.soot.VoidType.v());
				sc.addMethod(sm);
			} else {
				m = symbolTable.resolveMethod(new Name(typeName), new Name(methodName), parameterTypes);
				for (Enumeration e = m.getParameterTypes().elements(); e.hasMoreElements();) {
					parmTypes.addLast(Util.convertType((edu.ksu.cis.bandera.jjjc.symboltable.Type) e.nextElement()));
				}
				String declaringType = m.getDeclaringClassOrInterface().getName().toString();
				ca.mcgill.sable.soot.SootClass sc = getSootClass(declaringType);
				if (sc.declaresMethod(methodName, parmTypes))
					return sc.getMethod(methodName, parmTypes);
				sm = new ca.mcgill.sable.soot.SootMethod(methodName, parmTypes, Util.convertType(m.getReturnType()));
				sc.addMethod(sm);
			}
			int modifiers = Util.convertModifiers(m.getModifiers());
			if (m.getDeclaringClassOrInterface().isInterface())
				modifiers |= ca.mcgill.sable.soot.Modifier.ABSTRACT;
			sm.setModifiers(modifiers);
			for (Enumeration e = m.getExceptions(); e.hasMoreElements();) {
				ClassOrInterfaceType exceptionType = (ClassOrInterfaceType) e.nextElement();
				ca.mcgill.sable.soot.SootClass exceptionSootClass = getSootClass(exceptionType.getName().toString());
				Name throwableName = new Name("java.lang.Throwable");
				if (exceptionType.hasSuperClass(throwableName) || throwableName.equals(exceptionType.getName())) {
					sm.addException(exceptionSootClass);
				} else {
					exceptions.addElement(new  NotThrowableException("Cannot have an unthrowable exception type named '" + exceptionType.getName() + "'"));
				}
			}
			return sm;
		} catch (CompilerException e) {
			throw e;
		}
	}
	/**
	 * 
	 * @return java.lang.String
	 * @param varName java.lang.String
	 */
	private String getUniqueName(String varName) {
		String result;
		Integer iteration;
		if (currentLocalNamesIterator.get(varName) == null)
			iteration = new Integer(0);
		else
			iteration = new Integer(((Integer) currentLocalNamesIterator.get(varName)).intValue() + 1);
		result = varName + "$" + iteration.toString();
		currentLocalNamesIterator.put(varName, iteration);
		return result;
	}
	/**
	 * 
	 * @param q edu.ksu.cis.bandera.specification.datastructure.Quantifier
	 * @param c ca.mcgill.sable.soot.jimple.Local
	 */
	private void inlineAvailable(QuantifiedVariable q, ca.mcgill.sable.soot.jimple.Local c) {
		/*
	if (MainClass.P == null) {
	temp = choose(true,false);
	if (temp && constraint) MainClass.P = c;
 	}
		 */
		/*
 	lcl = MainClass.P;
 	if lcl != null goto [nop];
 	if !constraint goto [nop];
 	temp = choose(1, 0);
 	if temp == 0 goto [nop];
 	MainClass.P = c;
 	nop;
		 */

		if (qtNameGen == null) qtNameGen = new NameGenerator("qtvar_");

		Stmt s = jimple.newNopStmt();

		ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(qtNameGen.newName(), c.getType(), 0);
		ca.mcgill.sable.soot.jimple.Value ph = getPlaceHolder(q);
		currentStmts.addElement(jimple.newAssignStmt(lcl, ph));
		currentStmts.addElement(jimple.newIfStmt(jimple.newNeExpr(lcl, NullConstant.v()), s));
		ca.mcgill.sable.soot.jimple.Local temp = declareLocal(qtNameGen.newName(), ca.mcgill.sable.soot.IntType.v(), 0);
		LinkedList l = new LinkedList();
		l.addLast(IntConstant.v(0));
		l.addLast(IntConstant.v(1));
		Stmt ts = jimple.newAssignStmt(temp, new ChooseExpr(l));
		currentStmts.addElement(ts);
		currentStmts.addElement(jimple.newIfStmt(jimple.newEqExpr(temp, IntConstant.v(0)), s));
		currentStmts.addElement(jimple.newAssignStmt(ph, c));
		currentStmts.addElement(s);
	}
	/**
	 * 
	 * @return boolean
	 * @param value ca.mcgill.sable.soot.jimple.Value
	 */
	private boolean isNumberConstant(ca.mcgill.sable.soot.jimple.Value value) {
		return ((value instanceof IntConstant) || (value instanceof LongConstant) || (value instanceof DoubleConstant)
				|| (value instanceof FloatConstant));
	}
	/**
	 * 
	 * @return boolean
	 * @param typeName1 java.lang.String
	 * @param typeName2 java.lang.String
	 */
	private boolean isSubtypeOf(String typeName1, String typeName2) {
		try {
			Name subName = new Name(typeName1);
			Name superName = new Name(typeName2);
			ClassOrInterfaceType type = symbolTable.resolveClassOrInterfaceType(subName);
			return type.hasSuperClass(superName) || type.hasSuperInterface(superName);
		} catch (Exception e) {
			exceptions.addElement(e);
		}
		return false;
	}
	/**
	 * 
	 */
	private void labelEnterBlock() {
		if (currentLabels != null && !labels.contains(currentLabels)) {
			throw new RuntimeException("Unexpected error");
		}
		currentLabels = new Hashtable();
		labels.addLast(currentLabels);
	}
	/**
	 * 
	 */
	private void labelExitBlock() {
		labels.removeLast();
		if (labels.size() > 0)
		{
			currentLabels = (Hashtable) labels.getLast(); 
		}
		else
		{
			currentLabels = null;
		}
	}
	/**
	 * 
	 * @param name java.lang.String
	 * @param args ca.mcgill.sable.util.List
	 * @param specialInvoke boolean
	 */
	private void methodInvocation(String name, List args, Vector types, boolean specialInvoke) {
		LinkedList newArgs = new LinkedList();
		for (Iterator i = args.iterator(); i.hasNext();) {
			ca.mcgill.sable.soot.jimple.Value value = (ca.mcgill.sable.soot.jimple.Value) i.next();
			if (!(value instanceof ca.mcgill.sable.soot.jimple.Local) && !(value instanceof Constant)) {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), value instanceof ConditionExpr ? ca.mcgill.sable.soot.BooleanType.v() : value.getType(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, value));
				newArgs.addLast(lcl);
			} else {
				newArgs.addLast(value);
			}
		}

		args = newArgs;

		String methodName = name.trim();
		Name qName = new Name(methodName);
		ca.mcgill.sable.soot.jimple.Value caller;
		String className;
		if (qName.isSimpleName()) {
			caller = currentThisLocal;
			className = currentSootClass.getName();
		} else {
			Name superName = qName.getSuperName();
			methodName = qName.getLastIdentifier().toString();
			try {
				symbolTable.resolveFieldOrLocal(superName);

				nameFieldOrLocalExp(false, superName.toString());
				caller = currentValue;
				currentValue = null;
				if (caller.getType() instanceof ca.mcgill.sable.soot.ArrayType)
					className = "java.lang.Object";
				else 
					className = ((ca.mcgill.sable.soot.RefType) caller.getType()).className;
			} catch (Exception e) {
				className = superName.toString();
				caller = null;
			}
		}
		methodInvoke(caller, className, methodName, args, types, specialInvoke);
	}
	/**
	 * 
	 * @param caller ca.mcgill.sable.soot.jimple.Value
	 * @param className java.lang.String
	 * @param methodName java.lang.String
	 * @param args ca.mcgill.sable.util.List
	 * @param boolean specialInvoke
	 */
	private void methodInvoke(ca.mcgill.sable.soot.jimple.Value caller, String className, String methodName, List args, Vector types, boolean specialInvoke) {
		try {
			if (caller != null) {
				if (!(caller instanceof ca.mcgill.sable.soot.jimple.Local)) {
					ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), caller.getType(), 0);
					currentStmts.addElement(jimple.newAssignStmt(lcl, caller));
					caller = lcl;
				}
			}
			ca.mcgill.sable.soot.SootMethod sm;
			try {
				sm = getSootMethod(className, methodName, types);
			} catch (CompilerException e) {
				if (ca.mcgill.sable.soot.Modifier.isInterface(scm.getClass(className).getModifiers())) {
					sm = getSootMethod("java.lang.Object", methodName, types);
				} else {
					throw e;
				}
			}
			if (ca.mcgill.sable.soot.Modifier.isStatic(sm.getModifiers())) {
				currentValue = jimple.newStaticInvokeExpr(sm, args);
			} else {
				if (caller != null) {
					if (ca.mcgill.sable.soot.Modifier.isInterface(sm.getDeclaringClass().getModifiers())) 
						currentValue = jimple.newInterfaceInvokeExpr((ca.mcgill.sable.soot.jimple.Local) caller, sm, args);
					else if (specialInvoke || ca.mcgill.sable.soot.Modifier.isPrivate(sm.getModifiers()))
						currentValue = jimple.newSpecialInvokeExpr((ca.mcgill.sable.soot.jimple.Local) caller, sm, args);
					else
						currentValue = jimple.newVirtualInvokeExpr((ca.mcgill.sable.soot.jimple.Local) caller, sm, args);
				} else {
					exceptions.addElement(new CompilerException("Could not invoke instance method " + methodName + " in " + sm.getName()));
					System.out.println("Could not invoke instance method " + methodName + " in " + sm.getName());
					return;
				}
			}
			ca.mcgill.sable.soot.jimple.Value methodValue = currentValue;
			currentValue = null;
			if (sm.getReturnType() instanceof ca.mcgill.sable.soot.VoidType) {
				currentStmts.addElement(jimple.newInvokeStmt(methodValue));
			} else {
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), sm.getReturnType(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, methodValue));
				currentValue = lcl;
			}
		} catch (CompilerException e) {
			exceptions.addElement(e);
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @param lhs boolean
	 * @param name java.lang.String
	 */
	private void nameExp(boolean lhs, String name) {
		String expName = name.trim();
		if (expName.endsWith(".length")) {
			if (lhs) {
				currentValue = null;
				exceptions.addElement(new CompilerException("Cannot have array length expression in left hand side of assignment"));
				System.out.println("Cannot have array length expression in left hand side of assignment");
				return;
			}
			String arrayName = name.substring(0, name.length() - 7);
			nameFieldOrLocalExp(lhs, arrayName);
			if (currentValue != null) {
				String tempName = nameGen.newName();
				ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(tempName, ca.mcgill.sable.soot.IntType.v(), 0);
				currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newLengthExpr(currentValue)));
				currentValue = lcl;
			} else {
				exceptions.addElement(new CompilerException("Could not resolve array name " + arrayName + " in " + expName));
				System.out.println("Could not resolve array name " + arrayName + " in " + expName);
			}
		} else {
			nameFieldOrLocalExp(lhs, expName);
		}
	}
	/**
	 * 
	 * @param lhs boolean
	 * @param name java.lang.String
	 */
	private void nameFieldOrLocalExp(boolean lhs, String name) {
		name = name.trim();
		
		ca.mcgill.sable.soot.jimple.Value caller;
		if (currentLocalNames.get(name) != null) {
			currentValue = (ca.mcgill.sable.soot.jimple.Local) currentLocals.get(currentLocalNames.get(name));
			return;
		} else {
			Name qName = new Name(name);
			String className;
			String fieldName;
			if (qName.isSimpleName()) {
				className = currentSootClass.getName();
				fieldName = name;
				caller = currentThisLocal;
			} else {
				fieldName = qName.getLastIdentifier();
				Name superName = qName.getSuperName();
				boolean isTypeName;
				try {
					superName = symbolTable.resolveClassOrInterfaceType(superName).getName();
					isTypeName = true;
				} catch (Exception e) {
					isTypeName = false;
				}
				if (isTypeName) {
					className = superName.toString();
					caller = null;
				} else {
					nameFieldOrLocalExp(lhs, superName.toString());
					className = ((ca.mcgill.sable.soot.RefType) currentValue.getType()).className;
					caller = currentValue;
					
					/* [Thomas, May 22, 2017]
					 * */
					currentValue = null;
					ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), caller.getType(), 0);
					if(caller.toString().startsWith("_static_"))
					{
						lcl.setName(caller.toString());
					}
					else if(caller instanceof StaticFieldRef)
					{
						lcl.setName("_static_" + ((StaticFieldRef)caller).getField().getName());
					}
					else
					{
						currentStmts.addElement(jimple.newAssignStmt(lcl, caller));
					}
					caller = lcl;
				}
			}
			try {
				symbolTable.resolveExpression(new Name(name));
				fieldAccess(lhs, caller, className, fieldName);
			} catch (CompilerException e) {
				System.out.println(e.getMessage());
				exceptions.addElement(e);
			}
		}
	}
	/**
	 * 
	 * @param operator java.lang.String
	 * @param firstValue ca.mcgill.sable.soot.jimple.Value
	 * @param secondValue ca.mcgill.sable.soot.jimple.Value
	 */
	private void numericOperation(String operator, ca.mcgill.sable.soot.jimple.Value firstValue, ca.mcgill.sable.soot.jimple.Value secondValue) throws CompilerException {
		ca.mcgill.sable.soot.Type firstType = firstValue.getType();
		ca.mcgill.sable.soot.Type secondType = secondValue.getType();
		ca.mcgill.sable.soot.Type resultType = getResultType(firstType, secondType);
		ca.mcgill.sable.soot.jimple.Local lcl;
		if (resultType == null) {
			exceptions.addElement(new CompilerException("Cannot perform operation " + firstType + " " + operator + " " + secondType));
			System.out.println("Cannot perform operation " + firstType + " " + operator + " " + secondType);
			currentValue = null;
			return;
		} else if (resultType instanceof ca.mcgill.sable.soot.RefType) {
			if ("+".equals(operator)) {
				ca.mcgill.sable.soot.RefType refType = ca.mcgill.sable.soot.RefType.v("java.lang.String");
				ClassOrInterfaceType stringType = symbolTable.resolveClassOrInterfaceType(new Name("java.lang.String"));
				Vector types = new Vector();
				types.addElement(stringType);
				LinkedList values = new LinkedList();
				if (!(firstType instanceof ca.mcgill.sable.soot.RefType)) {
					values.addLast(firstValue);
					ca.mcgill.sable.soot.SootMethod valueOf1 = getSootMethod("java.lang.String", "valueOf", types);
					ca.mcgill.sable.soot.jimple.Local lcl1 = declareLocal(nameGen.newName(), refType, 0);
					currentStmts.addElement(jimple.newAssignStmt(lcl1, jimple.newStaticInvokeExpr(valueOf1, values)));
					firstValue = lcl1;
				} else if (!(secondType instanceof ca.mcgill.sable.soot.RefType)) {
					values.addLast(secondValue);
					ca.mcgill.sable.soot.SootMethod valueOf2 = getSootMethod("java.lang.String", "valueOf", types);
					ca.mcgill.sable.soot.jimple.Local lcl2 = declareLocal(nameGen.newName(), refType, 0);
					currentStmts.addElement(jimple.newAssignStmt(lcl2, jimple.newStaticInvokeExpr(valueOf2, values)));
					secondValue = lcl2;
				}
				ca.mcgill.sable.soot.SootMethod concat = getSootMethod("java.lang.String", "concat", types);
				lcl = declareLocal(nameGen.newName(), refType, 0);
				if (!(firstValue instanceof ca.mcgill.sable.soot.jimple.Local)) {
					ca.mcgill.sable.soot.jimple.Local tempLocal = declareLocal(nameGen.newName(), refType, 0);
					currentStmts.addElement(jimple.newAssignStmt(tempLocal, firstValue));
					firstValue = tempLocal;
				}
				String secondTypeString = secondValue.getType().toString().trim();
				if (!"java.lang.String".equals(secondTypeString)) {
					ca.mcgill.sable.soot.jimple.Local tempLocal = declareLocal(nameGen.newName(), refType, 0);
					currentStmts.addElement(jimple.newAssignStmt(tempLocal, jimple.newVirtualInvokeExpr((ca.mcgill.sable.soot.jimple.Local) secondValue,
							getSootMethod("java.lang.Object", "toString", new Vector()), new LinkedList())));
					secondValue = tempLocal;
				}
				values = new LinkedList();
				values.addLast(secondValue);
				currentStmts.addElement(jimple.newAssignStmt(lcl, jimple.newVirtualInvokeExpr((ca.mcgill.sable.soot.jimple.Local) firstValue, concat, values)));
				currentValue = lcl;
				return;
			} else {
				exceptions.addElement(new CompilerException("Cannot perform operation " + firstType + " + " + secondType));
				System.out.println("Cannot perform operation " + firstType + " + " + secondType);
				currentValue = null;
				return;
			}
		} else {
			lcl = declareLocal(nameGen.newName(), resultType, 0);
		}
		if ("+".equals(operator)) {
			currentValue = jimple.newAddExpr(firstValue, secondValue);
		} else if ("-".equals(operator)) {
			currentValue = jimple.newSubExpr(firstValue, secondValue);
		} else if ("*".equals(operator)) {
			currentValue = jimple.newMulExpr(firstValue, secondValue);
		} else if ("%".equals(operator)) {
			currentValue = jimple.newRemExpr(firstValue, secondValue);
		} else if ("/".equals(operator)) {
			currentValue = jimple.newDivExpr(firstValue, secondValue);
		} else if ("&".equals(operator)) {
			currentValue = jimple.newAndExpr(firstValue, secondValue);
		} else if ("|".equals(operator)) {
			currentValue = jimple.newOrExpr(firstValue, secondValue);
		} else if (">>".equals(operator)) {
			currentValue = jimple.newShrExpr(firstValue, secondValue);
		} else if (">>>".equals(operator)) {
			currentValue = jimple.newUshrExpr(firstValue, secondValue);
		} else if ("<<".equals(operator)) {
			currentValue = jimple.newShlExpr(firstValue, secondValue);
		} else {
			currentValue = jimple.newXorExpr(firstValue, secondValue);
		}
		currentStmts.addElement(jimple.newAssignStmt(lcl, currentValue));
		currentValue = lcl;
	}
	/**
	 * 
	 */
	private void patchBody(JimpleBody body) {
		StmtList stmts = body.getStmtList();
		ca.mcgill.sable.soot.jimple.Local thisLocal = null;
		for (ca.mcgill.sable.util.Iterator i = stmts.iterator(); i.hasNext();) {
			Stmt s = (Stmt) i.next();
			if (s instanceof IdentityStmt) {
				IdentityStmt is = (IdentityStmt) s;
				if (is.getRightOp() instanceof ThisRef) {
					thisLocal = (ca.mcgill.sable.soot.jimple.Local) is.getLeftOp();
					thisLocal.setName("JJJCTEMP$0");
					return;
				}
			}
		}
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.PExp
	 * @param isPlus boolean
	 */
	private void postIncDecExp(PExp node, boolean isPlus) {
		String name = null;
		if (isPlus) {
			if (((APostIncrementExp) node).getExp() instanceof ANameExp) {
				name = (new Name((((ANameExp) ((APostIncrementExp) node).getExp()).getName()).toString())).toString();
				nameExp(node.parent() instanceof ANameLeftHandSide, name);
			} else {
				((APostIncrementExp) node).getExp().apply(this);
			}
		} else {
			if (((APostDecrementExp) node).getExp() instanceof ANameExp) {
				name = (new Name((((ANameExp) ((APostDecrementExp) node).getExp()).getName()).toString())).toString();
				nameExp(node.parent() instanceof ANameLeftHandSide, name);
			} else {
				((APostDecrementExp) node).getExp().apply(this);
			}
		}
		ca.mcgill.sable.soot.jimple.Value postValue = currentValue;
		currentValue = null;
		ca.mcgill.sable.soot.jimple.Local lcl = declareLocal(nameGen.newName(), postValue.getType(), 0);
		currentStmts.addElement(jimple.newAssignStmt(lcl, postValue));
		ca.mcgill.sable.soot.jimple.Value operandValue;
		if (postValue.getType() instanceof ca.mcgill.sable.soot.IntType)
			operandValue = IntConstant.v(1);
		else
			operandValue = LongConstant.v(1);
		if (isPlus)
			currentStmts.addElement(jimple.newAssignStmt(postValue, jimple.newAddExpr(postValue, operandValue)));
		else
			currentStmts.addElement(jimple.newAssignStmt(postValue, jimple.newSubExpr(postValue, operandValue)));
		if (name != null) {
			nameExp(true, name);
		} else {
			assignExp = true;
			if (isPlus) {
				((APostIncrementExp) node).getExp().apply(this);
			} else {
				((APostDecrementExp) node).getExp().apply(this);
			}
		}
		currentStmts.addElement(jimple.newAssignStmt(currentValue, postValue));
		currentValue = lcl;
	}
	/**
	 * 
	 * @param node edu.ksu.cis.bandera.jjjc.node.AUnaryExp
	 * @param isPlus boolean
	 */
	private void preIncDecExp(AUnaryExp node, boolean isPlus) {
		String name = null;
		if (node.getExp() instanceof ANameExp) {
			name = (new Name((((ANameExp) node.getExp()).getName()).toString())).toString();
			nameExp(node.parent() instanceof ANameLeftHandSide, name);
		} else {
			node.getExp().apply(this);
		}

		ca.mcgill.sable.soot.jimple.Value preValue = currentValue;
		currentValue = null;
		ca.mcgill.sable.soot.jimple.Value operandValue;
		if (preValue.getType() instanceof ca.mcgill.sable.soot.IntType)
			operandValue = IntConstant.v(1);
		else
			operandValue = LongConstant.v(1);
		if (isPlus)
			currentStmts.addElement(jimple.newAssignStmt(preValue, jimple.newAddExpr(preValue, operandValue)));
		else
			currentStmts.addElement(jimple.newAssignStmt(preValue, jimple.newSubExpr(preValue, operandValue)));

		if (name != null) {
			nameExp(true, name);
		} else {
			assignExp = true;
			node.getExp().apply(this);
		}

		currentStmts.addElement(jimple.newAssignStmt(currentValue, preValue));
		currentValue = preValue;
	}
}
