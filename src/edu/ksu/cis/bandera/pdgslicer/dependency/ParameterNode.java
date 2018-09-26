package edu.ksu.cis.bandera.pdgslicer.dependency;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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

/**
 * Insert the type's description here.
 * Creation date: (00-5-21 16:03:30)
 * @author: 
 */
import ca.mcgill.sable.soot.SootClass;
import edu.ksu.cis.bandera.annotation.Annotation;
import ca.mcgill.sable.soot.jimple.IdentityStmt;
import ca.mcgill.sable.soot.jimple.Value;
import ca.mcgill.sable.soot.jimple.Local;
public class ParameterNode {
	SootClass sootClass;
	Annotation mda;
	IdentityStmt identityStmt;
/**
 * ParameterNode constructor comment.
 */
public ParameterNode(SootClass sc, Annotation m, IdentityStmt idStmt) {
	this.sootClass = sc;
	this.mda = m;
	this.identityStmt = idStmt;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-20 17:16:42)
 * @return boolean
 * @param obj java.lang.Object
 */
public boolean equals(Object obj) {
	if (!(obj instanceof ParameterNode))
		return false;
	ParameterNode paraObj = (ParameterNode) obj;
	if (this.identityStmt.equals(paraObj.identityStmt))
		return true;
	else
		return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-5-21 16:25:39)
 * @return java.lang.String
 */
public String toString() {
	Value leftOp = this.identityStmt.getLeftOp();
	if (leftOp instanceof Local)
		return ((Local) leftOp).toString();
	System.out.println("the leftOp of Identity stmt: " + this.identityStmt + "  should be local variable!");
	return "";
}
}
