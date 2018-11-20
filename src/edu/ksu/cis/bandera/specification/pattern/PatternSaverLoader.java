package edu.ksu.cis.bandera.specification.pattern;

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
import edu.ksu.cis.bandera.specification.pattern.analysis.*;
import edu.ksu.cis.bandera.specification.pattern.exception.*;
import edu.ksu.cis.bandera.specification.pattern.lexer.*;
import edu.ksu.cis.bandera.specification.pattern.node.*;
import edu.ksu.cis.bandera.specification.pattern.parser.*;
import edu.ksu.cis.bandera.jjjc.util.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import java.util.*;
public class PatternSaverLoader extends DepthFirstAdapter {
	private static Hashtable patternTable;
	private static Hashtable formatPatternTable = new Hashtable();
	private java.util.Vector exceptions;
	private static String currentFilename;
	private Pattern currentPattern;
	private StringBuffer buffer;

	static {
		try {
			reset();
		} catch (Exception e) {
		}
	}
/**
 * 
 * @param node edu.ksu.cis.bandera.pattern.node.AIdIds
 */
public void caseAIdIds(AIdIds node) {
	try {
		currentPattern.addParameter(node.getId().toString().trim());
	} catch (Exception e) {
		exceptions.add(e);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.pattern.node.AIdsIds
 */
public void caseAIdsIds(AIdsIds node) {
	node.getIds().apply(this);
	try {
		currentPattern.addParameter(node.getId().toString().trim());
	} catch (Exception e) {
		exceptions.add(e);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.pattern.node.AParamResource
 */
public void caseAParamResource(AParamResource node) {
	if ("parameters".equals(node.getId().toString().trim())) {
		if (node.getIds() != null) {
			node.getIds().apply(this);
		}
	} else {
		exceptions.add(new PatternException("Unsupported resource type: " + node.getId()));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.pattern.node.APattern
 */
public void caseAPattern(APattern node) {
	exceptions = new Vector();
	currentPattern = new Pattern();
	currentPattern.setExceptions(exceptions);
	super.caseAPattern(node);

	currentPattern.setFilename(currentFilename);

	if (patternTable.get(currentPattern.getName()) == null) {
		Hashtable table = new Hashtable();
		patternTable.put(currentPattern.getName(), table);
	}

	currentPattern.validate();
	
	Hashtable table = (Hashtable) patternTable.get(currentPattern.getName());
	formatPatternTable.put(currentPattern.getCompareFormat(), currentPattern);
	if (table.put(currentPattern.getScope(), currentPattern) != null) {
		exceptions.add(new PatternException("Pattern with name '" + currentPattern.getName()
				+ "' and scope '" + currentPattern.getScope() + "' has already been declared"));
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.pattern.node.AStringResource
 */
public void caseAStringResource(AStringResource node) {
	buffer = new StringBuffer();

	String id = node.getId().toString().trim();
	if ("name".equals(id)) {
		node.getStrings().apply(this);
		currentPattern.setName(buffer.toString());
	} else if ("scope".equals(id)) {
		node.getStrings().apply(this);
		currentPattern.setScope(buffer.toString());
	} else if ("format".equals(id)) {
		node.getStrings().apply(this);
		try {
			currentPattern.setFormat(buffer.toString());
		} catch (PatternException e) {
			exceptions.add(e);
		}
	} else if ("url".equals(id)) {
		node.getStrings().apply(this);
		currentPattern.setURL(buffer.toString());
	} else if ("description".equals(id)) {
		node.getStrings().apply(this);
		currentPattern.setDescription(buffer.toString());
	} else if ("ltl".equals(id)) {
		node.getStrings().apply(this);
		currentPattern.addMapping("ltl", buffer.toString());
	} else if ("ctl".equals(id)) {
		node.getStrings().apply(this);
		currentPattern.addMapping("ctl", buffer.toString());
	} else {
		node.getStrings().apply(this);
		currentPattern.addMiscData(id, buffer.toString());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.pattern.node.AStringsStrings
 */
public void caseAStringsStrings(AStringsStrings node) {
	node.getStrings().apply(this);
	buffer.append(Util.decodeString(node.getStringLiteral().toString().trim()));
}
/**
 * 
 * @param node edu.ksu.cis.bandera.pattern.node.AStringStrings
 */
public void caseAStringStrings(AStringStrings node) {
	buffer.append(Util.decodeString(node.getStringLiteral().toString().trim()));
}
/**
 * 
 * @return edu.ksu.cis.bandera.pattern.Pattern
 * @param name java.lang.String
 * @param scope java.lang.String
 */
public static Pattern getPattern(String name, String scope) {
	Hashtable table = (Hashtable) patternTable.get(name.trim());
	if (table == null) return null;
	
	return (Pattern) table.get(scope.trim());
}
/**
 * 
 * @return java.util.Hashtable
 */
public static java.util.Hashtable getPatternTable() {
	return patternTable;
}
/**
 * 
 * @return edu.ksu.cis.bandera.pattern.Pattern
 * @param format java.lang.String
 */
public static Pattern getPatternWithFormat(String format) {
	return (Pattern) formatPatternTable.get(format);
}
/**
 * 
 * @param filename java.lang.String
 */
public static void load(String filename) throws PatternException {
	try {
		currentFilename = filename;
		PatternSaverLoader loader = new PatternSaverLoader();
		FileReader fr = new FileReader(currentFilename);
		new Parser(new Lexer(new PushbackReader(fr))).parse().apply(loader);
		fr.close();
	} catch (Exception e) {
		throw new PatternException("Exceptions were raised when extracting patterns:" + e.getMessage());
	}
}
/**
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args) throws Exception {
	reset();
	for (int i = 0; i < args.length; i++) {
		load(args[i]);
	}
	Hashtable t = getPatternTable();
	System.out.println(t);
}
/**
 * 
 * @param pattern edu.ksu.cis.bandera.pattern.Pattern
 */
public static void removePattern(Pattern pattern) {
	Hashtable table = (Hashtable) patternTable.get(pattern.getName());
	if (table != null) {
		table.remove(pattern.getScope());
	}
}
/**
 * 
 */
public static void reset() throws PatternException {
	patternTable = new Hashtable();
	PatternSaverLoader loader = new PatternSaverLoader();
	try {
		currentFilename = "bandera.pattern";
		new Parser(new Lexer(new PushbackReader(new InputStreamReader(PatternSaverLoader.class.getResourceAsStream(currentFilename))))).parse().apply(loader);
	} catch (Exception ex) {
		throw new PatternException("Exceptions were raised when loading patterns: " + ex.getMessage());
	}
}
/**
 * 
 * @param newPatternTable java.util.Hashtable
 */
public static void setPatternTable(java.util.Hashtable newPatternTable) {
	patternTable = newPatternTable;
}
}
