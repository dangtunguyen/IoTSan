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
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.specification.assertion.ast.*;
import edu.ksu.cis.bandera.jjjc.node.AAssertionCompilationUnit;
import edu.ksu.cis.bandera.jjjc.node.PExp;
import ca.mcgill.sable.laleh.java.astfix.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import java.io.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.doc.*;
import edu.ksu.cis.bandera.specification.assertion.parser.*;
import edu.ksu.cis.bandera.specification.assertion.lexer.*;
import edu.ksu.cis.bandera.specification.assertion.node.*;
import edu.ksu.cis.bandera.specification.assertion.analysis.*;
import edu.ksu.cis.bandera.annotation.*;
import ca.mcgill.sable.soot.*;
public class AssertionExtractor extends DepthFirstAdapter {
	private static AnnotationManager am;
	private static SootClassManager scm;
	private Enumeration expressions;
/**
 * 
 * @param expressions java.util.Vector
 */
private AssertionExtractor(Vector expressions) {
	this.expressions = expressions.elements();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.assertion.node.ALocationAssertion
 */
public void caseALocationAssertion(ALocationAssertion node) {
	node.setExp((PExp) expressions.nextElement());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.assertion.node.APostAssertion
 */
public void caseAPostAssertion(APostAssertion node) {
	node.setExp((PExp) expressions.nextElement());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.assertion.node.APreAssertion
 */
public void caseAPreAssertion(APreAssertion node) {
	node.setExp((PExp) expressions.nextElement());
}
/**
 *
 * @return java.uti.Vector
 * @param sc ca.mcgill.sable.soot.SootClass
 * @param sm ca.mcgill.sable.soot.SootMethod
 * @param tags java.util.Vector
 */
public static Vector extract(SootClass sc, SootMethod sm, Vector tags) {
	Vector errors = new Vector();
	scm = CompilationManager.getSootClassManager();
	am = CompilationManager.getAnnotationManager();
	for (Iterator i = tags.iterator(); i.hasNext();) {
		String tag = (String) i.next();
		if ("@assert".equals(new StringTokenizer(tag).nextToken())) {
			try {
				errors.addAll(process(parse(tag.substring(7)), sc, sm));
			} catch (Exception e) {
				errors.add(e);
			}
		}
	}
	return errors;
}
/**
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args) throws Exception {
	removeComment("sdsjdsk // dksj \n dfkofe // ded \n");
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.assertion.node.Node
 * @param text java.lang.String
 */
private static Node parse(String text) throws Exception {
	Vector expressions = new Vector();
	StringBuffer buffer = new StringBuffer();

	String textNoComment = removeComment(text);

	Vector v = new Vector();
	for (StringTokenizer t = new StringTokenizer(textNoComment, ":;", true); t.hasMoreTokens();) {
		String token = t.nextToken();
		if (":".equals(token)) {
			token = t.nextToken();
			String token2 = t.nextToken();
			while (!";".equals(token2)) {
				token += token2;
				token2 = t.nextToken();
			}
			v.add(token);
		}	
	}

	int lastIndex = 0;
	for (Enumeration e = v.elements(); e.hasMoreElements();) {
		String expText = (String) e.nextElement();
		expressions.add(parseExp(expText));
		int index = textNoComment.indexOf(expText, lastIndex);
		buffer.append(text.substring(lastIndex, index));
		lastIndex = index + expText.length();
	}
	buffer.append(text.substring(lastIndex));

	Node node = new Parser(new Lexer(new PushbackReader(new StringReader(buffer.toString())))).parse();

	node.apply(new AssertionExtractor(expressions));
	
	return node;
}
/**
 * 
 * @return edu.ksu.cis.bandera.bpdl.node.Node
 * @param text java.lang.String
 */
private static edu.ksu.cis.bandera.jjjc.node.Node parseExp(String text) throws Exception {
	return ((AAssertionCompilationUnit) new JJCParser(new edu.ksu.cis.bandera.jjjc.lexer.Lexer(new PushbackReader(new StringReader(text)))).parse().getPCompilationUnit()).getExp();
}
/**
 *
 * @return java.util.Vector
 * @param node edu.ksu.cis.bandera.bpdl.node.Node
 * @param sc ca.mcgill.sable.soot.SootClass
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private static Vector process(Node node, SootClass sc, SootMethod sm) {
	ClassOrInterfaceType classOrInterfaceType = null;
	SymbolTable symbolTable = null;
	try {
		classOrInterfaceType = Package.getClassOrInterfaceType(new Name(sc.getName()));
		symbolTable = classOrInterfaceType.getSymbolTable();
	} catch (Exception e) {
	}
	AssertionBuilder assertionBuilder = new AssertionBuilder(node, symbolTable, classOrInterfaceType, am.getAnnotation(sc, sm));
	assertionBuilder.build();
	return assertionBuilder.getErrors();
}
/**
 * 
 * @return java.lang.String
 * @param text java.lang.String
 */
private static String removeComment(String text) {
	StringBuffer buffer = new StringBuffer();
	int lastIndex = 0;
	do {
		int index = text.indexOf("//", lastIndex);
		index = index < 0 ? text.length() : index;
		buffer.append(text.substring(lastIndex, index));
		lastIndex = text.indexOf("\n", index);
		for (int i = index; i < lastIndex; i++) {
			buffer.append(" ");
		}
	} while (lastIndex >= 0);
	return buffer.toString();
}
}
