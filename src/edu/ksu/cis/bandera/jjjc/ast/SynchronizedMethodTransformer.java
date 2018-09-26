package edu.ksu.cis.bandera.jjjc.ast;

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
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.doc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import java.util.Vector;
import java.util.Hashtable;
public class SynchronizedMethodTransformer extends DepthFirstAdapter {
	private String packageName = null;
	private String className;
	private Vector v;
	private Hashtable docComments;
/**
 * 
 * @param synchTransformed java.util.Vector
 */
public SynchronizedMethodTransformer(Vector synchTransformed, Hashtable docComments) {
	v = synchTransformed;
	this.docComments = docComments;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclaration
 */
public void caseAClassDeclaration(AClassDeclaration node) {
	className = node.getId().toString().trim();
	if (packageName != null) {
		className = packageName + "." + className;
	}
	super.caseAClassDeclaration(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AMethodDeclaration
 */
public void caseAMethodDeclaration(AMethodDeclaration node) {
	PMethodHeader methodHeader = node.getMethodHeader();
	List modifiers;
	boolean isStatic = false;
	boolean isSynchronized = false;
	boolean isNative = false;
	boolean isAbstract = false;
	Object synchronizedModifier = null;
	if (methodHeader instanceof ATypedMethodHeader) {
		modifiers = ((ATypedMethodHeader) methodHeader).getModifier();
	} else {
		modifiers = ((AVoidMethodHeader) methodHeader).getModifier();
	}
	for (Iterator i = modifiers.iterator(); i.hasNext();) {
		Object o = i.next();
		String mod = o.toString().trim();
		if ("static".equals(mod)) {
			isStatic = true;
		} else
			if ("synchronized".equals(mod)) {
				isSynchronized = true;
				synchronizedModifier = ((ASynchronizedModifier) o).getSynchronized();
			} else
				if ("native".equals(mod)) {
					isNative = true;
				} else
					if ("abstract".equals(mod)) {
						isAbstract = true;
					}
	}
	if (!isSynchronized)
		return;
	else
		if (isNative || isAbstract)
			return;
	PMethodBody methodBody = node.getMethodBody();
	if (methodBody instanceof AEmptyMethodBody)
		return;
	PBlock methodBlock = ((ABlockMethodBody) methodBody).getBlock();
	PExp lock;
	if (isStatic) {
		LinkedList args = new LinkedList();
		args.addLast(new ALiteralExp(new AStringLiteralLiteral(new TStringLiteral("\"" + className + "\""))));
		lock = new ANameMethodInvocationExp(new AQualifiedName(new ASimpleName(new TId("Class")), new TDot(), new TId("forName")), new TLPar(), args, new TRPar());
	} else {
		lock = new AThisExp(new TThis());
	}
	ASynchronizedStmt synchStmt = new ASynchronizedStmt(new TSynchronized(), new TLPar(), lock, new TRPar(), null);
	LinkedList blockedStmts = new LinkedList();
	blockedStmts.addLast(new AStmtBlockedStmt(synchStmt));
	if (isStatic) {
		LinkedList catchClauses = new LinkedList();
		AFormalParameter formal = new AFormalParameter(new LinkedList(), new AReferenceType(new ANameReferenceType(new ASimpleName(new TId("ClassNotFoundException")))), new AVariableDeclaratorId(new TId("JJJCTEMP$E"), new LinkedList()));
		//new name l_par [argument_list]:exp* r_par class_body?
		PExp expNewRuntime = new ASimpleClassInstanceCreationExp(new TNew(), new AQualifiedName(new AQualifiedName(new ASimpleName(new TId("java")), new TDot(), new TId("lang")), new TDot() ,new TId("RuntimeException")), new TLPar(), new LinkedList(), new TRPar(), null);
		AThrowStmt throwStmt = new AThrowStmt(new TThrow(), expNewRuntime,new TSemicolon());		
		LinkedList throwBlockedStmt = new LinkedList();
		throwBlockedStmt.addLast(new AStmtBlockedStmt(throwStmt));
		catchClauses.addLast(new ACatchClause(new TCatch(), new TLPar(), formal, new TRPar(), new ABlock(new TLBrace(), throwBlockedStmt, new TRBrace())));
		ATryStmt tryStmt = new ATryStmt(new TTry(), new ABlock(new TLBrace(), blockedStmts, new TRBrace()), catchClauses);
		blockedStmts = new LinkedList();
		blockedStmts.addLast(new AStmtBlockedStmt(tryStmt));
	}
	ABlock block = new ABlock(new TLBrace(), blockedStmts, new TRBrace());
	methodBlock.replaceBy(block);
	synchStmt.setBlock(methodBlock);
	if (DocProcessor.getKey(node).equals(synchronizedModifier)) {
		modifiers.remove(synchronizedModifier);
		Object value = docComments.get(synchronizedModifier);
		docComments.remove(synchronizedModifier);
		docComments.put(DocProcessor.getKey(node), value);
	} else {
		modifiers.remove(synchronizedModifier);
	}
	v.add(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APackageDeclaration
 */
public void caseAPackageDeclaration(APackageDeclaration node) {
	packageName = (new Name(node.getName().toString())).toString();
}
}
