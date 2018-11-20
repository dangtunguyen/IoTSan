package edu.ksu.cis.bandera.jjjc.doc;

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
import java.io.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;
public class DocProcessor extends DepthFirstAdapter {
	private static DocProcessor dp = new DocProcessor();
	private java.lang.Object key;
/**
 * 
 */
private DocProcessor() {}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AAbstractModifier
 */
public void caseAAbstractModifier(AAbstractModifier node) {
	key = node.getAbstract();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABooleanPrimitiveType
 */
public void caseABooleanPrimitiveType(ABooleanPrimitiveType node) {
	key = node.getBoolean();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABytePrimitiveType
 */
public void caseABytePrimitiveType(ABytePrimitiveType node) {
	key = node.getByte();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ACharPrimitiveType
 */
public void caseACharPrimitiveType(ACharPrimitiveType node) {
	key = node.getChar();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclaration
 */
public void caseAClassDeclaration(AClassDeclaration node) {
	if (node.getModifier().size() > 0)
		((Node) node.getModifier().toArray()[0]).apply(this);
	else key = node.getTClass();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorDeclaration
 */
public void caseAConstructorDeclaration(AConstructorDeclaration node) {
	if (node.getModifier().size() > 0) 
		((Node) node.getModifier().toArray()[0]).apply(this);
	else
		node.getConstructorDeclarator().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorDeclarator
 */
public void caseAConstructorDeclarator(AConstructorDeclarator node) {
	key = node.getSimpleName();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ADoublePrimitiveType
 */
public void caseADoublePrimitiveType(ADoublePrimitiveType node) {
	key = node.getDouble();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFinalModifier
 */
public void caseAFinalModifier(AFinalModifier node) {
	key = node.getFinal();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFloatPrimitiveType
 */
public void caseAFloatPrimitiveType(AFloatPrimitiveType node) {
	key = node.getFloat();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceDeclaration
 */
public void caseAInterfaceDeclaration(AInterfaceDeclaration node) {
	if (node.getModifier().size() > 0)
		((Node) node.getModifier().toArray()[0]).apply(this);
	else key = node.getInterface();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AIntPrimitiveType
 */
public void caseAIntPrimitiveType(AIntPrimitiveType node) {
	key = node.getInt();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALongPrimitiveType
 */
public void caseALongPrimitiveType(ALongPrimitiveType node) {
	key = node.getLong();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AMethodDeclaration
 */
public void caseAMethodDeclaration(AMethodDeclaration node) {
	node.getMethodHeader().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANativeModifier
 */
public void caseANativeModifier(ANativeModifier node) {
	key = node.getNative();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrivateModifier
 */
public void caseAPrivateModifier(APrivateModifier node) {
	key = node.getPrivate();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AProtectedModifier
 */
public void caseAProtectedModifier(AProtectedModifier node) {
	key = node.getProtected();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APublicModifier
 */
public void caseAPublicModifier(APublicModifier node) {
	key = node.getPublic();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AShortPrimitiveType
 */
public void caseAShortPrimitiveType(AShortPrimitiveType node) {
	key = node.getShort();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASimpleName
 */
public void caseASimpleName(ASimpleName node) {
	key = node.getId();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AStaticModifier
 */
public void caseAStaticModifier(AStaticModifier node) {
	key = node.getStatic();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASynchronizedModifier
 */
public void caseASynchronizedModifier(ASynchronizedModifier node) {
    key = node.getSynchronized();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATransientModifier
 */
public void caseATransientModifier(ATransientModifier node) {
	key = node.getTransient();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATypedMethodHeader
 */
public void caseATypedMethodHeader(ATypedMethodHeader node) {
	if (node.getModifier().size() > 0)
		((Node) node.getModifier().toArray()[0]).apply(this);
	else
		node.getType().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AVoidMethodHeader
 */
public void caseAVoidMethodHeader(AVoidMethodHeader node) {
	if (node.getModifier().size() > 0) 
		((Node) node.getModifier().toArray()[0]).apply(this);
	else key = node.getVoid();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AVolatileModifier
 */
public void caseAVolatileModifier(AVolatileModifier node) {
	key = node.getVolatile();
}
/**
 * 
 * @return java.lang.Object
 */
public static Object getKey(Node node) {
	node.apply(dp);
	return dp.key;
}
/**
 * 
 * @return java.util.Vector
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 * @param table java.lang.Hashtable
 */
public static java.util.Vector tags(Node node, Hashtable table) {
	node.apply(dp);

	if (table.get(dp.key) == null)
		return new Vector();
	Object[] ignoredTokens = ((List) table.get(dp.key)).toArray();
	String comment = "";
	for (int i = ignoredTokens.length - 1; i >= 0; i--) {
		if (ignoredTokens[i] instanceof TDocumentationComment) {
			comment = ignoredTokens[i].toString();
			break;
		}
	}
	Vector tags = new Vector();
	StringTokenizer t = new StringTokenizer(comment, "@");
	t.nextToken();
	while (t.hasMoreTokens()) {
		tags.add("@" + t.nextToken());
	}
	Vector result = new Vector();
	for (java.util.Iterator i = tags.iterator(); i.hasNext();) {
		LineNumberReader r = new LineNumberReader(new StringReader((String) i.next()), 8192);
		String line;
		String tag = "";
		try {
			while ((line = r.readLine()) != null) {
				try {
					int start, end;
					boolean f = true;
					for (start = 0; (start < line.length()) && f; start++) {
						switch(line.charAt(start)) {
							case ' ':
							case '\n':
							case '\t':
							case '*':
								break;
						default: f = false;
						}
					}
					f = true;
					for (end = line.length() - 1; (end >= 0) && f; end--) {
						switch(line.charAt(end)) {
							case ' ':
							case '\n':
							case '\t':
							case '*':
							case '/':
								break;
							default: f = false;
						}
					}
					tag += line.substring(start - 1 , end + 2) + "\n";
				} catch (Exception e2) {}
			}
		} catch (Exception e) {}
		result.add(tag);
	}
	return result;
}
}
