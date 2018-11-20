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
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
public class ComplementExpr implements UnopExpr {
	protected ValueBox opBox;
/**
 * 
 */
public ComplementExpr(Value v) {
	this(Grimp.v().newArgBox(v));
}
/**
 * ComplementExpr constructor comment.
 */
public ComplementExpr(ValueBox valueBox) {
	opBox = valueBox;
}
/**
 * apply method comment.
 */
public void apply(ca.mcgill.sable.util.Switch sw) {
	((BanderaExprSwitch) sw).caseComplementExpr(this);
}
/**
 * getOp method comment.
 */
public Value getOp() {
	return opBox.getValue();
}
/**
 * getOpBox method comment.
 */
public ValueBox getOpBox() {
	return opBox;
}
/**
 * getType method comment.
 */
public Type getType() {
	return getOp().getType();
}
/**
 * getUseBoxes method comment.
 */
public ca.mcgill.sable.util.List getUseBoxes() {
	ca.mcgill.sable.util.List list = new ca.mcgill.sable.util.ArrayList();

	list.addAll(opBox.getValue().getUseBoxes());
	list.add(opBox);

	return list;
}
/**
 * setOp method comment.
 */
public void setOp(Value op) {
	opBox.setValue(op);
}
/**
 * toBriefString method comment.
 */
public String toBriefString() {
	return "! " + ((ToBriefString) opBox.getValue()).toBriefString();
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "! " + opBox.getValue();
}
}
