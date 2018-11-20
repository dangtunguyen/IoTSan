package edu.ksu.cis.bandera.jext;

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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
public class LocalExpr implements Expr {
	protected SootMethod sm;
	protected Local local;
/**
 * LocationTestExpr constructor comment.
 */
public LocalExpr(SootMethod sootMethod, Local local) {
	sm = sootMethod;
	this.local = local;
}
/**
 * apply method comment.
 */
public void apply(Switch sw) {
	((BanderaExprSwitch) sw).caseLocalExpr(this);
}
/**
 * 
 * @return boolean
 * @param o java.lang.Object
 */
public boolean equals(Object o) {
	if (o instanceof LocalExpr) {
		LocalExpr other = (LocalExpr) o;
		return ((local.equals(other.local)) && (sm.equals(other.sm)));
	} else return false;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Local
 */
public Local getLocal() {
	return local;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootMethod
 */
public SootMethod getMethod() {
	return sm;
}
/**
 * getType method comment.
 */
public Type getType() {
	return null;
}
/**
 * getUseBoxes method comment.
 */
public ca.mcgill.sable.util.List getUseBoxes() {
	ca.mcgill.sable.util.List list = new ca.mcgill.sable.util.ArrayList();
	return list;
}
/**
 * 
 * @return int
 */
public int hashCode() {
	return (sm.toString() + local.toString()).hashCode();
}
/**
 * print method comment.
 */
public void print() {}
/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toBriefString() {
	return "LocalExpr";
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "<" + sm + ", " + local + ">";
}
}
