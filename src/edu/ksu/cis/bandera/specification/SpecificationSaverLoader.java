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
import java.io.*;
import edu.ksu.cis.bandera.specification.pattern.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import java.util.*;
import edu.ksu.cis.bandera.specification.ast.*;
import edu.ksu.cis.bandera.specification.node.*;
import edu.ksu.cis.bandera.specification.analysis.*;
import edu.ksu.cis.bandera.specification.exception.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.lexer.*;
import edu.ksu.cis.bandera.specification.parser.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;

public class SpecificationSaverLoader extends DepthFirstAdapter {
	private Property property = new Property();
	private TemporalLogicProperty currentTLP;
	private AssertionProperty currentAP;
	private static Vector exceptions;
	private PImport importContext;
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AAssert
 */
public void caseAAssert(AAssert node) {
	currentAP = new AssertionProperty(node.getId().toString().trim());
	node.getNames().apply(this);
	property.addAssertionProperty(currentAP);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AAssertionImport
 */
public void caseAAssertionImport(AAssertionImport node) {
	importContext = node;
	super.caseAAssertionImport(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ANameNames
 */
public void caseANameNames(ANameNames node) {
	currentAP.addAssertion(new Name(node.getName().toString()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ANamesNames
 */
public void caseANamesNames(ANamesNames node) {
	node.getNames().apply(this);
	currentAP.addAssertion(new Name(node.getName().toString()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AOnDemandImportName
 */
public void caseAOnDemandImportName(AOnDemandImportName node) {
	if (importContext instanceof ATypeImport) {
		property.importPackage(new Name(node.getName().toString()));
	} else if (importContext instanceof AAssertionImport) {
		property.importAssertionSet(new Name(node.getName().toString()));
	} else if (importContext instanceof APredicateImport) {
		property.importPredicateSet(new Name(node.getName().toString()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.APredicateImport
 */
public void caseAPredicateImport(APredicateImport node) {
	importContext = node;
	super.caseAPredicateImport(node);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ASpecificImportName
 */
public void caseASpecificImportName(ASpecificImportName node) {
	if (importContext instanceof ATypeImport) {
		property.importType(new Name(node.getName().toString()));
	} else if (importContext instanceof AAssertionImport) {
		property.importAssertion(new Name(node.getName().toString()));
	} else if (importContext instanceof APredicateImport) {
		property.importPredicate(new Name(node.getName().toString()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ATl
 */
public void caseATl(ATl node) {
	currentTLP = new TemporalLogicProperty(node.getId().toString().trim());
	String q = "";
	{
		Object temp[] = node.getQtl().toArray();
		for (int i = 0; i < temp.length; i++) {
			q += ((PQtl) temp[i]).toString().trim();
		}
	}
	
	String format = "";
	Vector parameters = new Vector();
	{
		Object temp[] = node.getWord().toArray();
		for (int i = 0; i < temp.length; i++) {
			String word = ((PWord) temp[i]).toString().trim();
			if (word.startsWith("{")) {
				parameters.addElement(word.substring(1, word.length() - 2));
				format += "{p} ";
			} else {
				format += (word + " ");
			}
		}
	}

	try {
		format = Reformatter.format(format);
		Pattern pattern = PatternSaverLoader.getPatternWithFormat(format);
		if (pattern == null) {
			format = "\"" + format.substring(12);
			pattern = PatternSaverLoader.getPatternWithFormat(format);
			if (pattern == null) {
				exceptions.add(new SpecificationException("Can't find matching pattern for '" + node + "'"));
				return;
			}
		}
		currentTLP.setQuantifier(q);
		currentTLP.setPatternName(pattern.getName());
		currentTLP.setPatternScope(pattern.getScope());
		int length = parameters.size();
		int i = 0;
		Iterator k = parameters.iterator();
		for (Iterator j = pattern.getParametersOccurenceOrder().iterator(); i < length; i++) {
			currentTLP.putProposition((String) j.next(), (String) k.next());
		}
	} catch (Exception e) {
		exceptions.add(new SpecificationException(e.getMessage()));
	}
	property.addTemporalLogicProperty(currentTLP);
}
public void caseATypeImport(ATypeImport node)	{
	importContext = node;
	super.caseATypeImport(node);
}
/**
 * 
 * @return java.util.Vector
 */
public static Vector getExceptions() {
	return exceptions;
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.datastructure.Property
 * @param filename java.lang.String
 */
public static Property load(String filename) throws SpecificationException {
	exceptions = new Vector();
	try {
		FileReader fr = new FileReader(filename);
		Node node = new Parser(new Lexer(new PushbackReader(fr))).parse();
		node.apply(new Simplifier());
		SpecificationSaverLoader ssl = new SpecificationSaverLoader();
		node.apply(ssl);
		ssl.property.setFilename(filename);
		fr.close();
		return ssl.property;
	} catch (Exception e) {
		exceptions.addElement(e);
	}
	throw new SpecificationException("Errors occurred when loading property specification");
}
/**
 * 
 * @param property edu.ksu.cis.bandera.specification.datastructure.Property
 */
public static void save(Property property) throws SpecificationException {
	String newline = System.getProperty("line.separator");
	StringBuffer buffer = new StringBuffer();

	for (Iterator i = property.getImportedType().iterator(); i.hasNext();) {
		buffer.append("import " + i.next() + ";" + newline);
	}
	for (Iterator i = property.getImportedPackage().iterator(); i.hasNext();) {
		buffer.append("import " + i.next() + ".*;" + newline);
	}
	for (Iterator i = property.getImportedAssertion().iterator(); i.hasNext();) {
		buffer.append("import assertion " + i.next() + ";" + newline);
	}
	for (Iterator i = property.getImportedAssertionSet().iterator(); i.hasNext();) {
		buffer.append("import assertion " + i.next() + ".*;" + newline);
	}
	for (Iterator i = property.getImportedPredicate().iterator(); i.hasNext();) {
		buffer.append("import predicate " + i.next() + ";" + newline);
	}
	for (Iterator i = property.getImportedPredicateSet().iterator(); i.hasNext();) {
		buffer.append("import predicate " + i.next() + ".*;" + newline);
	}
	
	buffer.append(newline);

	for (Enumeration e = property.getAssertionProperties().elements(); e.hasMoreElements();) {
		AssertionProperty ap = (AssertionProperty) e.nextElement();
		buffer.append(ap.getStringRepresentation() + newline + newline);
	}

	for (Enumeration e = property.getTemporalLogicProperties().elements(); e.hasMoreElements();) {
		TemporalLogicProperty tlp = (TemporalLogicProperty) e.nextElement();
		buffer.append(tlp.getStringRepresentation() + newline + newline);
	}

	try {
		FileWriter fw = new FileWriter(property.getFilename());
		String s = buffer.toString();
		fw.write(s, 0, s.length());
		fw.close();
	} catch (Exception e) {
		throw new SpecificationException(e.getMessage());
	}
}
}
