package edu.ksu.cis.bandera.abstraction.typeinference;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
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
public class TypeVariable {
	protected Object type;
	public Object ast;
/**
 * TypeVariable constructor comment.
 */
public TypeVariable(Object type) {
	this.type = type;
}
/**
 * Insert the method's description here.
 * Creation date: (8/22/00 4:21:29 PM)
 * @return java.lang.Object
 */
public Object getAST() {
	return ast;
}
/**
 * Insert the method's description here.
 * Creation date: (8/21/00 10:14:56 AM)
 * @return ca.mcgill.sable.soot.Type
 */
public Object getType() {
	return type;
}
/**
 * Insert the method's description here.
 * Creation date: (8/22/00 3:50:27 PM)
 * @param ast java.lang.Object
 */
public void setAST(Object ast) {
	this.ast = ast;
}
/**
 * Insert the method's description here.
 * Creation date: (8/21/00 10:14:56 AM)
 * @param newType ca.mcgill.sable.soot.Type
 */
protected void setType(Object newType) {
	type = newType;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:28:49 PM)
 * @return java.lang.String
 */
public String toString() {
	if (ast == null)
		return super.toString();
	else
		return ast.toString();
}
}
