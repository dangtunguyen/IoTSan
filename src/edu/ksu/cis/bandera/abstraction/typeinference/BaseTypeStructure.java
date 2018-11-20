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
import java.util.*;
public class BaseTypeStructure implements TypeStructure {
	protected TypeVariable var;
/**
 * Insert the method's description here.
 * Creation date: (8/21/00 10:05:39 AM)
 * @param var edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
public BaseTypeStructure(TypeVariable var) {
	this.var = var;
}
/**
 * getCoerceConstraints method comment.
 */
public java.util.Set genCoerceConstraints(TypeStructure ats) {
	Set set = new HashSet();
	if (ats instanceof BaseTypeStructure) {
		BaseTypeStructure bts = (BaseTypeStructure) ats;
		set.add(new CoerceConstraint(var, bts.var));
	} else
		System.out.println("Cannot mix type structures.");
	return set;
}
/**
 * getEqualConstraints method comment.
 */
public java.util.Set genEqualConstraints(TypeStructure ats) {
	Set set = new HashSet();
	if (ats instanceof BaseTypeStructure) {
		BaseTypeStructure bts = (BaseTypeStructure) ats;
		set.add(new EqualConstraint(var, bts.var));
	} else
		System.out.println("Cannot mix type structures.");
	return set;
}
/**
 * Insert the method's description here.
 * Creation date: (8/21/00 10:06:47 AM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
public TypeVariable getVar() {
	return var;
}
/**
 * Insert the method's description here.
 * Creation date: (8/21/00 10:06:47 AM)
 * @param newVar edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
protected void setVar(TypeVariable newVar) {
	var = newVar;
}
/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	// Insert code to print the receiver here.
	// This implementation forwards the message to super. You may replace or supplement this.
	return super.toString();
}
}
