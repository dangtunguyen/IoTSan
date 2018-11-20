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
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import java.io.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.doc.*;
import edu.ksu.cis.bandera.specification.predicate.parser.*;
import edu.ksu.cis.bandera.specification.predicate.lexer.*;
import edu.ksu.cis.bandera.specification.predicate.node.*;
import edu.ksu.cis.bandera.specification.predicate.ast.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.annotation.*;
import ca.mcgill.sable.soot.*;
public class PredicateExtractor {
	private static AnnotationManager am;
	private static SootClassManager scm;
/**
 * @return java.util.Vector
 * @param sc SootClass
 * @param sm SootMethod
 * @param tags java.util.Vector
 */
public static Vector extract(SootClass sc, SootMethod sm, Vector tags) {
	Vector errors = new Vector();
	scm = CompilationManager.getSootClassManager();
	am = CompilationManager.getAnnotationManager();
	for (Iterator i = tags.iterator(); i.hasNext();) {
		String tag = (String) i.next();
		if ("@observable".equals(new StringTokenizer(tag).nextToken())) {
			try {
				errors.addAll(process(parse(tag.substring(12)), sc, sm));
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
	String s = "predset1\n"
						+ "// hi\n"
						+ "\n"
						+ "RETURN a: (test..test.test.test..test.i > 0)\n"
						+ "// this does this...\n";
	try {
		System.out.println(new Parser(new Lexer(new PushbackReader(new StringReader(s)))).parse());
	} catch (Exception e) {
		e.printStackTrace();
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.bpdl.node.Node
 * @param text java.lang.String
 */
private static Node parse(String text) throws Exception {
	Simplifier.reset();
	Node node = Simplifier.simplify(new Parser(new Lexer(new PushbackReader(new StringReader(text)))).parse());
	if (Simplifier.getExceptions().size() > 0) {
		for (Enumeration e = Simplifier.getExceptions().elements(); e.hasMoreElements();) {
			System.out.println(e.nextElement());
		}
	}// else {
	//	System.out.println(node.toString());
	//}
	return node;
}
/**
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
	TypeChecker typeChecker;
	if (sm == null) {
		typeChecker = new TypeChecker(node, symbolTable, classOrInterfaceType, null);
	} else {
		typeChecker = new TypeChecker(node, symbolTable, classOrInterfaceType, am.getAnnotation(sc, sm));
	}
	typeChecker.check();
	return typeChecker.getErrors();
}
}
