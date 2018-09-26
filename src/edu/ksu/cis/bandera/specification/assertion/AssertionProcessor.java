package edu.ksu.cis.bandera.specification.assertion;

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
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.*;
import java.util.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
public class AssertionProcessor extends DepthFirstAdapter {
	private static Hashtable assertionTable;
	private static AssertionProcessor processor = new AssertionProcessor();
	private static HashSet preSet;
	private static Hashtable locationTable;
	private static HashSet postSet;
	private static PType type;
	private static boolean hasRet;
	private static Hashtable modifiedMethodTable;
	private static Annotation currentAnnotation;
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.node.PExp
 * @param a edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion
 */
private ca.mcgill.sable.util.List buildArgumentList(Assertion a) {
	PExp exp;

	if (((Collection) assertionTable.get(a)).size() == 0) {
		exp = (PExp) a.getConstraint().clone();
	} else {
		Iterator i = ((Collection) assertionTable.get(a)).iterator();
		if (a instanceof PreAssertion) {
			exp = new ABinaryExp((PExp) a.getConstraint().clone(), new AOrBinaryOperator(new TOr()), (PExp) i.next());
			while (i.hasNext()) {
				exp = new ABinaryExp(exp, new AOrBinaryOperator(new TOr()), (PExp) i.next());
			}
		} else {
			exp = new ABinaryExp((PExp) a.getConstraint().clone(), new AAndBinaryOperator(new TAnd()), (PExp) i.next());
			while (i.hasNext()) {
				exp = new ABinaryExp(exp, new AAndBinaryOperator(new TAnd()), (PExp) i.next());
			}
		}
	}
	
	ca.mcgill.sable.util.LinkedList result = new ca.mcgill.sable.util.LinkedList();
	result.addLast(exp);
	return result;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABlockMethodBody
 */
public void caseABlockMethodBody(ABlockMethodBody node) {
	ca.mcgill.sable.util.LinkedList list = ((ABlock) node.getBlock()).getBlockedStmt();

	boolean isSynchronized = false;
	boolean isStatic = false;
	
	Method m = ((MethodDeclarationAnnotation) currentAnnotation).getMethod();
	isSynchronized = CompilationManager.isSynchTransformed(currentAnnotation.getNode());
	isStatic = java.lang.reflect.Modifier.isStatic(m.getModifiers());

	if (isSynchronized) {
		if (isStatic) {
			ATryStmt ts = (ATryStmt) ((AStmtBlockedStmt) list.iterator().next()).getStmt();
			list = ((ABlock) ts.getBlock()).getBlockedStmt();
			ASynchronizedStmt ss = (ASynchronizedStmt) ((AStmtBlockedStmt) list.iterator().next()).getStmt();
			list = ((ABlock) ss.getBlock()).getBlockedStmt();
		} else {
			ASynchronizedStmt ss = (ASynchronizedStmt) ((AStmtBlockedStmt) list.iterator().next()).getStmt();
			list = ((ABlock) ss.getBlock()).getBlockedStmt();
		}
	}

	for (Iterator i = preSet.iterator(); i.hasNext();) {
		PreAssertion a = (PreAssertion) i.next();
		PName name = new AQualifiedName(new ASimpleName(new TId("Bandera")), new TDot(), new TId("assert"));
		PExp exp = new ANameMethodInvocationExp(name, new TLPar(), buildArgumentList(a), new TRPar());
		PStmt stmt = new AExpStmt(exp, new TSemicolon());
		list.addFirst(new AStmtBlockedStmt(stmt));
	}

	hasRet = false;

	for (Iterator i = postSet.iterator(); i.hasNext();) {
		PostAssertion a = (PostAssertion) i.next();
		if (a.hasRet()) {
			hasRet = true;
			break;
		}
	}

	if (hasRet) {
		ca.mcgill.sable.util.LinkedList varList = new ca.mcgill.sable.util.LinkedList();
		varList.addFirst(new AIdVariableDeclarator(new AVariableDeclaratorId(new TId("$ret"), new ca.mcgill.sable.util.LinkedList())));
		ALocalVariableDeclaration decl = new ALocalVariableDeclaration(new ca.mcgill.sable.util.LinkedList(),
				type, varList);
		list.addFirst(new ALocalVariableDeclarationInBlockedStmt(decl, new TSemicolon()));
	}

	if (type == null) {
		PStmt lastStmt = ((AStmtBlockedStmt) list.getLast()).getStmt();
		if (!(lastStmt instanceof AReturnStmt)) {
			ca.mcgill.sable.util.LinkedList list2 = new ca.mcgill.sable.util.LinkedList();
			for (Iterator i = postSet.iterator(); i.hasNext();) {
				PostAssertion a = (PostAssertion) i.next();
				PName name = new AQualifiedName(new ASimpleName(new TId("Bandera")), new TDot(), new TId("assert"));
				PExp exp2 = new ANameMethodInvocationExp(name, new TLPar(), buildArgumentList(a), new TRPar());
				PStmt stmt = new AExpStmt(exp2, new TSemicolon());
				list2.addLast(new AStmtBlockedStmt(stmt));
			}
			if (list2.size() != 0) {
				ABlock block = new ABlock(new TLBrace(), list2, new TRBrace());
				list.addLast(new AStmtBlockedStmt(new ABlockStmt(block)));
			}
		}
	}
	
	super.caseABlockMethodBody(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABreakStmt
 */
public void caseABreakStmt(ABreakStmt node) {
	if (node.getId() != null) {
		String label = node.getId().toString().trim();
		if (locationTable.get(label) != null) {
			node.getId().setText("assertion$" + label);
		}
	}
	super.caseABreakStmt(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorBody
 */
public void caseAConstructorBody(AConstructorBody node) {
	ca.mcgill.sable.util.LinkedList list = node.getBlockedStmt();
	Method m = ((ConstructorDeclarationAnnotation) currentAnnotation).getConstructor();
	if (preSet.size() > 0) {
		ca.mcgill.sable.util.LinkedList list2 = new ca.mcgill.sable.util.LinkedList();
		for (Iterator i = preSet.iterator(); i.hasNext();) {
			PreAssertion a = (PreAssertion) i.next();
			PName name = new AQualifiedName(new ASimpleName(new TId("Bandera")), new TDot(), new TId("assert"));
			PExp exp = new ANameMethodInvocationExp(name, new TLPar(), buildArgumentList(a), new TRPar());
			PStmt stmt = new AExpStmt(exp, new TSemicolon());
			list2.addLast(new AStmtBlockedStmt(stmt));
		}
		ABlock block = new ABlock(new TLBrace(), list2, new TRBrace());
		list.addFirst(new AStmtBlockedStmt(new ABlockStmt(block)));
	}
	PStmt lastStmt = ((AStmtBlockedStmt) list.getLast()).getStmt();
	if (!(lastStmt instanceof AReturnStmt)) {
		if (postSet.size() > 0) {
			ca.mcgill.sable.util.LinkedList list2 = new ca.mcgill.sable.util.LinkedList();
			for (Iterator i = postSet.iterator(); i.hasNext();) {
				PostAssertion a = (PostAssertion) i.next();
				PName name = new AQualifiedName(new ASimpleName(new TId("Bandera")), new TDot(), new TId("assert"));
				PExp exp2 = new ANameMethodInvocationExp(name, new TLPar(), buildArgumentList(a), new TRPar());
				PStmt stmt = new AExpStmt(exp2, new TSemicolon());
				list2.addLast(new AStmtBlockedStmt(stmt));
			}
			ABlock block = new ABlock(new TLBrace(), list2, new TRBrace());
			list.addLast(new AStmtBlockedStmt(new ABlockStmt(block)));
		}
	}
	super.caseAConstructorBody(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorDeclaration
 */
public void caseAConstructorDeclaration(AConstructorDeclaration node) {
	AConstructorDeclaration newNode = (AConstructorDeclaration) node.clone();
	modifiedMethodTable.put(node, newNode);
	super.caseAConstructorDeclaration(newNode);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AContinueStmt
 */
public void caseAContinueStmt(AContinueStmt node) {
	if (node.getId() != null) {
		String label = node.getId().toString().trim();
		if (locationTable.get(label) != null) {
			node.getId().setText("assertion$" + label);
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALabelStmt
 */
public void caseALabelStmt(ALabelStmt node) {
	String label = node.getId().toString().trim();
	if (locationTable.get(label) != null) {
		ca.mcgill.sable.util.LinkedList list = new ca.mcgill.sable.util.LinkedList();
		
		for (Iterator i = ((Collection) locationTable.get(label)).iterator(); i.hasNext();) {
			LocationAssertion a = (LocationAssertion) i.next();
			PName name = new AQualifiedName(new ASimpleName(new TId("Bandera")), new TDot(), new TId("assert"));
			PExp exp = new ANameMethodInvocationExp(name, new TLPar(), buildArgumentList(a), new TRPar());
			PStmt stmt = new AExpStmt(exp, new TSemicolon());
			list.addFirst(new AStmtBlockedStmt(stmt));
		}

		ABlock block = new ABlock(new TLBrace(), list, new TRBrace());
		PStmt stmt = new ABlockStmt(block);

		list = new ca.mcgill.sable.util.LinkedList();
		list.addLast(new AStmtBlockedStmt(stmt));
		block = new ABlock(new TLBrace(), list, new TRBrace());
		
		stmt = new ALabelStmt(new TId("assertion$" + label), new TColon(), block);

		block = (ABlock) node.parent().parent();
		list = block.getBlockedStmt();
		int index = list.indexOf(node.parent());
		list.add(index, new AStmtBlockedStmt(stmt));
	}
	super.caseALabelStmt(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AMethodDeclaration
 */
public void caseAMethodDeclaration(AMethodDeclaration node) {
	AMethodDeclaration newNode = (AMethodDeclaration) node.clone();
	modifiedMethodTable.put(node, newNode);
	super.caseAMethodDeclaration(newNode);
	//System.out.println(newNode.toString());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AReturnStmt
 */
public void caseAReturnStmt(AReturnStmt node) {
	if (postSet.size() > 0) {
		ca.mcgill.sable.util.LinkedList list = new ca.mcgill.sable.util.LinkedList();
		
		PExp exp = null;
		if (type != null) {
			exp = (PExp) node.getExp().clone();
		}
		
		if (hasRet) {
			exp = new AAssignmentExp(new ANameLeftHandSide(new ASimpleName(new TId("$ret"))), new AAssignAssignmentOperator(new TAssign()), exp);
			list.addFirst(new AStmtBlockedStmt(new AExpStmt(exp, new TSemicolon())));
			exp = new ANameExp(new ASimpleName(new TId("$ret")));
		}

		for (Iterator i = postSet.iterator(); i.hasNext();) {
			PostAssertion a = (PostAssertion) i.next();
			PName name = new AQualifiedName(new ASimpleName(new TId("Bandera")), new TDot(), new TId("assert"));
			PExp exp2 = new ANameMethodInvocationExp(name, new TLPar(), buildArgumentList(a), new TRPar());
			PStmt stmt = new AExpStmt(exp2, new TSemicolon());
			list.addLast(new AStmtBlockedStmt(stmt));
		}

		list.addLast(new AStmtBlockedStmt(new AReturnStmt(node.getReturn(), exp, node.getSemicolon())));
		
		ABlock block = new ABlock(new TLBrace(), list, new TRBrace());
		node.replaceBy(new ABlockStmt(block));
	}
	super.caseAReturnStmt(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATypedMethodHeader
 */
public void caseATypedMethodHeader(ATypedMethodHeader node) {
	type = (PType) node.getType().clone();
	super.caseATypedMethodHeader(node);
}
/**
 *
 * @return java.util.Hashtable
 * @param assertionTable java.util.Hashtable
 */
public static Hashtable process(Hashtable assertionTable) {
	modifiedMethodTable = new Hashtable();
	AssertionProcessor.assertionTable = assertionTable;

	HashSet methodAnnotationSet = new HashSet();
	
	for (Enumeration e = assertionTable.keys(); e.hasMoreElements();) {
		Annotation a = ((Assertion) e.nextElement()).getAnnotation();

		if (a instanceof LabeledStmtAnnotation) {
			a = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
		}

		methodAnnotationSet.add(a);
	}


	StringBuffer sb = new StringBuffer();
	for (Iterator i = methodAnnotationSet.iterator(); i.hasNext();) {
		preSet = new HashSet();
		locationTable = new Hashtable();
		postSet = new HashSet();
		Annotation a = (Annotation) i.next();

		for (Enumeration e = assertionTable.keys(); e.hasMoreElements();) {
			Assertion as = (Assertion) e.nextElement();
			if (as.getExceptions().size() > 0) {
				sb.append("In '" + as + "':\n");
				for (Iterator j = as.getExceptions().iterator(); j.hasNext();) {
					sb.append("* " + ((Exception) j.next()).getMessage() + "\n");
				}
			}
			Annotation a2 = as.getAnnotation();
			if (a2 instanceof LabeledStmtAnnotation) {
				a2 = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a2);
			}
			if (a == a2) {
				if (as instanceof PreAssertion) {
					preSet.add(as);
				} else if (as instanceof LocationAssertion) {
					String label = ((LocationAssertion) as).getLabel();
					if (locationTable.get(label) == null) {
						locationTable.put(label, new HashSet());
					}
					((HashSet) locationTable.get(label)).add(as);
				} else {
					postSet.add(as);
				}
			}
		}

		String s = sb.toString();
		if (s.length() != 0)
			throw new RuntimeException(s.substring(0, s.length() - 1));

		type = null;
		currentAnnotation = a;
		a.getNode().apply(processor);
	}
	return modifiedMethodTable;
}
}
