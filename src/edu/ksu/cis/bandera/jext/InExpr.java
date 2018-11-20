package edu.ksu.cis.bandera.jext;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Shawn Laubach (laubach@acm.org)        *
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
import ca.mcgill.sable.util.VectorSet;
import java.util.Vector;

/**
 * This class was made to be used as a Jimple expression but it might not
 * stand up under some analyses because the op2 does not return an expression
 * but a set instead.  If this becomes a problem, email me at laubach@acm.org
 * and I can update the code.
 */

public class InExpr implements ca.mcgill.sable.soot.jimple.ConditionExpr {
	protected ca.mcgill.sable.soot.jimple.ValueBox op1Box;
	protected ca.mcgill.sable.util.Set op2;
/**
 * InExpr constructor comment.
 */
public InExpr(ca.mcgill.sable.soot.jimple.Value op1, ca.mcgill.sable.util.Set op2) {
	super();
	op1Box = ca.mcgill.sable.soot.jimple.Jimple.v().newVariableBox(op1);
	this.op2 = op2;
}
/**
 * InExpr constructor comment.
 */
public InExpr(ca.mcgill.sable.soot.jimple.Value op1, Vector op2) {
	super();
	op1Box = ca.mcgill.sable.soot.jimple.Jimple.v().newVariableBox(op1);
	this.op2 = new VectorSet();
	for (int i = 0; i < op2.size(); i++)
		this.op2.add(op2.elementAt(i));
}
/**
 * apply method comment.
 */
public void apply(ca.mcgill.sable.util.Switch sw) {
	((BanderaExprSwitch)sw).caseInExpr(this);
}
/**
 * getOp1 method comment.
 */
public ca.mcgill.sable.soot.jimple.Value getOp1() {
	return op1Box.getValue();
}
/**
 * getOp1Box method comment.
 */
public ca.mcgill.sable.soot.jimple.ValueBox getOp1Box() {
	return op1Box;
}
/**
 * getOp2 method comment.
 */
public ca.mcgill.sable.soot.jimple.Value getOp2() {
	return null;
}
/**
 * getOp2Box method comment.
 */
public ca.mcgill.sable.soot.jimple.ValueBox getOp2Box() {
	return null;
}
/**
 * This method was created in VisualAge.
 * @return Set
 */
public ca.mcgill.sable.util.Set getSet() {
	return op2;
}
/**
 * getSymbol method comment.
 */
public String getSymbol() {
	return " in ";
}
/**
 * getType method comment.
 */
public ca.mcgill.sable.soot.Type getType() {
	return getOp1().getType();
}
/**
 * getUseBoxes method comment.
 */
public ca.mcgill.sable.util.List getUseBoxes() {
	ca.mcgill.sable.util.List list = new ca.mcgill.sable.util.ArrayList();
	
	list.addAll(op1Box.getValue().getUseBoxes());
	list.add(op1Box);
	
	return list;
}
/**
 * setOp1 method comment.
 */
public void setOp1(ca.mcgill.sable.soot.jimple.Value op1) {
	op1Box = ca.mcgill.sable.soot.jimple.Jimple.v().newVariableBox(op1);
}
/**
 * setOp2 method comment.
 */
public void setOp2(ca.mcgill.sable.soot.jimple.Value op2) {
}
/**
 * This method was created in VisualAge.
 * @param s Set
 */
public void setOp2(ca.mcgill.sable.util.Set s) {
	op2 = s;
}
/**
 * toBriefString method comment.
 */
public String toBriefString() {
	ca.mcgill.sable.soot.jimple.Value op1 = op1Box.getValue();
	String leftOp = ((ca.mcgill.sable.soot.ToBriefString) op1).toBriefString(), rightOp = op2.toString();
	return leftOp + getSymbol() + rightOp;
}
/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	ca.mcgill.sable.soot.jimple.Value op1 = op1Box.getValue();
	String leftOp = op1.toString(), rightOp = op2.toString();
	return leftOp + getSymbol() + rightOp;
}
}
