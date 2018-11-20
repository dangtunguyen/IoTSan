package edu.ksu.cis.bandera.annotation;

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
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.util.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;
import java.util.*;
public class LocalDeclarationStmtAnnotation extends SequentialAnnotation {
	private Hashtable declaredLocals = new Hashtable();
	private Type type;
	private int modifiers = 0;
/**
 * LocalDeclarationStmtAnnotation constructor comment.
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public LocalDeclarationStmtAnnotation(edu.ksu.cis.bandera.jjjc.node.Node node) {
	super(node);
}
/**
 * 
 * @param actualName java.lang.String
 * @param local ca.mcgill.sable.soot.jimple.Local
 */
public void addDeclaredLocal(String actualName, Local local) {
	declaredLocals.put(actualName, local);
}
	public void apply(Switch sw)
	{
		((AnnotationSwitch) sw).caseLocalDeclarationStmtAnnotation(this);
	}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	LocalDeclarationStmtAnnotation result = new LocalDeclarationStmtAnnotation((Node) node.clone());
	/*
	Stmt[] stmts = getStatements();
	
	for (int i = 0; i < stmts.length; i++) {
		result.addStmt(JimpleStmtCloner.clone(stmts[i]));
	}
	*/
	return result;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getDeclaredLocals() {
	return declaredLocals;
}
/**
 * 
 * @return int
 */
public int getModifiers() {
	return modifiers;
}
/**
 * 
 * @return ca.mcgill.sable.soot.Type
 */
public Type getType() {
	return type;
}
/**
 * 
 * @param modifiers int
 */
public void setModifiers(int modifiers) {
	this.modifiers = modifiers;
}
/**
 * 
 * @param type ca.mcgill.sable.soot.Type
 */
public void setType(Type type) {
	this.type = type;
}
/**
 * 
 * @param body ca.mcgill.sable.soot.jimple.JimpleBody
 */
public void validate(JimpleBody body) {
	super.validate(body);
	for (Enumeration e = declaredLocals.keys(); e.hasMoreElements();) {
		String actualName = (String) e.nextElement();
		String jimpleName = ((Local) declaredLocals.get(actualName)).getName().trim();
		if (!body.declaresLocal(jimpleName)) {
			declaredLocals.remove(actualName);
		}
	}
}
}
