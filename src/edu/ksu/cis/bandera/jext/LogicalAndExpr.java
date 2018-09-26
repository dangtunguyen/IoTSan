package edu.ksu.cis.bandera.jext;

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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.util.*;
import java.util.*;
public class LogicalAndExpr implements BinopExpr, ToBriefString {
	protected ValueBox op1Box;
	protected ValueBox op2Box;
/**
 * 
 * @param v1 ca.mcgill.sable.soot.jimple.Value
 * @param v2 ca.mcgill.sable.soot.jimple.Value
 */
public LogicalAndExpr(Value v1, Value v2) {
	this(Grimp.v().newArgBox(v1), Grimp.v().newArgBox(v2));
}
/**
 * 
 * @param valueBox1 ca.mcgill.sable.soot.jimple.ValueBox
 * @param valueBox2 ca.mcgill.sable.soot.jimple.ValueBox
 */
public LogicalAndExpr(ValueBox valueBox1, ValueBox valueBox2) {
	op1Box = valueBox1;
	op2Box = valueBox2;
}
/**
 * 
 * @param sw ca.mcgill.sable.util.Switch
 */
public void apply(Switch sw) {
	((BanderaExprSwitch) sw).caseLogicalAndExpr(this);
}
/**
 * getOp1 method comment.
 */
public Value getOp1() {
	return op1Box.getValue();
}
/**
 * getOp1Box method comment.
 */
public ValueBox getOp1Box() {
	return op1Box;
}
/**
 * getOp2 method comment.
 */
public Value getOp2() {
	return op2Box.getValue();
}
/**
 * getOp2Box method comment.
 */
public ValueBox getOp2Box() {
	return op2Box;
}
/**
 * getSymbol method comment.
 */
public String getSymbol() {
	return " && ";
}
/**
 * getType method comment.
 */
public ca.mcgill.sable.soot.Type getType() {
	return ca.mcgill.sable.soot.BooleanType.v();
}
/**
 * getUseBoxes method comment.
 */
public ca.mcgill.sable.util.List getUseBoxes() {
	ca.mcgill.sable.util.List list = new ca.mcgill.sable.util.ArrayList();

	list.addAll(op1Box.getValue().getUseBoxes());
	list.add(op1Box);
	list.addAll(op2Box.getValue().getUseBoxes());
	list.add(op2Box);

	return list;
}
/**
 * setOp1 method comment.
 */
public void setOp1(Value op1) {
	op1Box.setValue(op1);
}
/**
 * setOp2 method comment.
 */
public void setOp2(Value op2) {
	op2Box.setValue(op2);
}
/**
 * toBriefString method comment.
 */
public String toBriefString() {
	Value op1 = op1Box.getValue(), op2 = op2Box.getValue();
	String leftOp = ((ToBriefString)op1).toBriefString(), 
	    rightOp = ((ToBriefString)op2).toBriefString();

	return leftOp + getSymbol() + rightOp;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	Value op1 = op1Box.getValue(), op2 = op2Box.getValue();
	String leftOp = op1.toString(), rightOp = op2.toString();

	return leftOp + getSymbol() + rightOp;
}
}
