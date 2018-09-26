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
public class Constraint {
	protected TypeVariable left;
	protected TypeVariable right;
/**
 * Insert the method's description here.
 * Creation date: (1/19/00 9:06:02 PM)
 * @param left edu.ksu.cis.bandera.abps.typing.Node
 * @param right edu.ksu.cis.bandera.abps.typing.Node
 */
public Constraint(TypeVariable left, TypeVariable right) {
	this.left = left;
	this.right = right;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:05:08 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
public TypeVariable getLeft() {
	return left;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:05:21 PM)
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
public TypeVariable getRight() {
	return right;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:05:08 PM)
 * @param newLeft edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
public void setLeft(TypeVariable newLeft) {
	left = newLeft;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 9:05:21 PM)
 * @param newRight edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
public void setRight(TypeVariable newRight) {
	right = newRight;
}
/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	return left + " ** " + right;
}
}
