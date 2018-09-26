package edu.ksu.cis.bandera.abstraction.specification;

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
import java.util.*;
import edu.ksu.cis.bandera.abstraction.specification.node.*;
import edu.ksu.cis.bandera.abstraction.specification.analysis.*;
public class AbstractionPrinter extends DepthFirstAdapter {
	private static final String lineSeparator = System.getProperty("line.separator");
	private StringBuffer buffer = new StringBuffer();
	private Vector expAbstractDefs = new Vector();
	private boolean isFull;
/**
 * 
 * @param isFull boolean
 */
private AbstractionPrinter(boolean isFull) {
	this.isFull = isFull;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AAbstractFunction
 */
public void caseAAbstractFunction(AAbstractFunction node) {
	println("    abstract (" + node.getId().toString().trim() + ")");
	println("      begin");
	{
		Object temp[] = node.getAbstractDef().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PAbstractDef) temp[i]).apply(this);
		}
	}
	println("      end");
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AAnyAbstractDef
 */
public void caseAAnyAbstractDef(AAnyAbstractDef node) {
	if (expAbstractDefs.size() > 0) {
		Iterator i = expAbstractDefs.iterator();
		String result = (String) i.next();
		if (result.startsWith("(")) result = "!" + result;
		else result = "!(" + result + ")";
		while (i.hasNext()) {
			String temp = (String) i.next();
			if (temp.startsWith("(")) result = result + " && !" + temp;
			else result = result + " && !(" + temp + ")";
		}
		println("        " + result + " -> " + node.getId().toString().trim() + ";");
	} else {
		println("        _" + " -> " + node.getId().toString().trim() + ";");
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AAnyPattern
 */
public void caseAAnyPattern(AAnyPattern node) {
	String result = "";
	int i = 0;
	for (StringTokenizer t = new StringTokenizer(node.getTokenTokenSet().toString(), ",{} "); t.hasMoreTokens(); i++) {
		result += (t.nextToken() + ", ");
	}
	if (i == 1) {
		println("        _ -> " + result.substring(0, result.length() - 2) + ";");
	} else {
		println("        _ -> {" + result.substring(0, result.length() - 2) + "};");
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.ADefaultToken
 */
public void caseADefaultToken(ADefaultToken node) {
	println("    DEFAULT = " + node.getId().toString().trim() + ";");
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AExpAbstractDef
 */
public void caseAExpAbstractDef(AExpAbstractDef node) {
	String exp = node.getExp().toString().trim();
	expAbstractDefs.add(exp);
	println("        " + exp + " -> " + node.getId().toString().trim() + ";");
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AOne2oneSet
 */
public void caseAOne2oneSet(AOne2oneSet node) {
	if (node.getIdList() != null) {
		String result = "";
		for (StringTokenizer t = new StringTokenizer(node.getIdList().toString(), ", "); t.hasMoreTokens();) {
			result += (t.nextToken() + ", ");
		}
		println("    ONE2ONE = {" + result.substring(0, result.length() - 2) + "};");
	} else {
		println("    ONE2ONE = {};");
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AOperator
 */
public void caseAOperator(AOperator node) {
	println("    operator " + node.getOp() + node.getId());
	println("      begin");
	{
		Object temp[] = node.getPattern().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PPattern) temp[i]).apply(this);
		}
	}
	println("      end");
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.APatternPattern
 */
public void caseAPatternPattern(APatternPattern node) {
	String result = "";
	int i = 0;
	for (StringTokenizer t = new StringTokenizer(node.getTokenTokenSet().toString(), ",{} "); t.hasMoreTokens();) {
		result += (t.nextToken() + ", ");
		i++;
	}
	if (i == 1) {
		println("        (" + node.getLId().toString().trim() + ", " + node.getRId().toString().trim() + ") -> " + result.substring(0, result.length() - 2) + ";");
	} else {
		println("        (" + node.getLId().toString().trim() + ", " + node.getRId().toString().trim() + ") -> {" + result.substring(0, result.length() - 2) + "};");
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.ATest
 */
public void caseATest(ATest node) {
	println("    test " + node.getTOp() + node.getId());
	println("      begin");
	{
		Object temp[] = node.getPattern().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PPattern) temp[i]).apply(this);
		}
	}
	println("      end");
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.ATokenSet
 */
public void caseATokenSet(ATokenSet node) {
	String result = "";
	for (StringTokenizer t = new StringTokenizer(node.getIdSet().toString(), ",{} "); t.hasMoreTokens();) {
		result += (t.nextToken() + ", ");
	}
	println("    TOKENS = {" + result.substring(0, result.length() - 2) + "};");
}
/**
 * 
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.AUnit
 */
public void caseAUnit(AUnit node) {
	println("abstraction " + node.getId() + "extends " + node.getType());
	println("  begin");
	node.getTokenSet().apply(this);
	if (isFull) {
		if (node.getDefaultToken() != null) {
			node.getDefaultToken().apply(this);
		}
		if (node.getOne2oneSet() != null) {
			node.getOne2oneSet().apply(this);
		}
	}
	node.getAbstractFunction().apply(this);
	
	if (isFull) {
		Object temp[] = node.getOperatorTest().toArray();
		for (int i = 0; i < temp.length; i++) {
			((POperatorTest) temp[i]).apply(this);
		}
	}
	println("  end");
}
/**
 * 
 * @return java.lang.String
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.Start
 * @param isFull boolean
 */
public static String print(Start node, boolean isFull) {
	if (node != null) {
		AbstractionPrinter printer = new AbstractionPrinter(isFull);
		node.apply(printer);
		return printer.buffer.toString();
	}
	return null;
}
/**
 * 
 * @param writer java.io.Writer
 * @param node edu.ksu.cis.bandera.abstraction.specification.node.Start
 * @param boolean isFull
 */
public static void print(Writer writer, Start Node, boolean isFull) throws IOException {
	writer.write(print(Node, isFull));
	writer.flush();
}
/**
 * 
 * @param s java.lang.String
 */
private void println(String s) {
	buffer.append(s + lineSeparator);
}
}
