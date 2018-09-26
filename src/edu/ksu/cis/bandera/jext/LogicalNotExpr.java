package edu.ksu.cis.bandera.jext;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
/**
 * Extending jimple/grimp with Logical Not operation
 * Creation date: (4/15/01 2:38:58 AM)
 * @author: Roby Joehanes
 */
public class LogicalNotExpr implements Expr {
	private ValueBox op;
public LogicalNotExpr(Value v) { this(Grimp.v().newArgBox(v)); }
public LogicalNotExpr(ValueBox v) { op = v; }
/**
 * apply method comment.
 */
public void apply(Switch sw)
{
	((PAExprSwitch) sw).caseLogicalNotExpr(this);
}
public String getSymbol() { return "!"; }
/**
 * getType method comment.
 */
public Type getType() {
	return BooleanType.v();
}
/**
 * getUseBoxes method comment.
 */
public List getUseBoxes() {
	ca.mcgill.sable.util.List list = new ca.mcgill.sable.util.ArrayList();
	list.addAll(op.getValue().getUseBoxes());
	list.add(op);
	return list;
}
/**
 * toBriefString method comment.
 */
public String toBriefString() {
	return getSymbol() + ((ToBriefString)op).toBriefString();
}
public String toString()
{
	return getSymbol() + op.toString();
}
}
