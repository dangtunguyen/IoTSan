package edu.ksu.cis.bandera.jext;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
/**
 * Hook "? : " expression extension for grimp
 * Creation date: (4/9/01 8:18:39 AM)
 * @author: Roby Joehanes
 */
public class HookExpr implements Expr {
	private ValueBox op1Box, op2Box, op3Box;
	private Type t;
public HookExpr(Value v1, Value v2, Value v3)
{
	this(Grimp.v().newArgBox(v1), Grimp.v().newArgBox(v2), Grimp.v().newArgBox(v3));
}
public HookExpr(ValueBox v1, ValueBox v2, ValueBox v3)
{
	if (v1.getValue().getType() != BooleanType.v()) throw new RuntimeException("Test expression must be boolean!");
	op1Box = v1;	op2Box = v2;	op3Box = v3;

	t = v2.getValue().getType();
	Type t2 = v3.getValue().getType();

	// Type conformance test
	if (t == VoidType.v() || t2 == VoidType.v()) throw new RuntimeException("Then part or else part cannot be void!");
	if (t instanceof ArrayType && t2 instanceof ArrayType)
	{
		if (t.equals(t2)) return; else throw new RuntimeException("Then part or else part array are not the same!");
	}

	if (t == t2) return;

	if (t == ByteType.v())
	{
		if (t2 == IntType.v() || t2 == ShortType.v() || t2 == FloatType.v() || t2 == DoubleType.v() || t2 == LongType.v()) { t = t2; return; }
	} else if (t == ShortType.v())
	{
		if (t2 == ByteType.v()) return;
		if (t2 == IntType.v() || t2 == FloatType.v() || t2 == DoubleType.v() || t2 == LongType.v()) { t = t2; return; }
	} else if (t == IntType.v())
	{
		if (t2 == ByteType.v() || t2 == ShortType.v()) return;
		if (t2 == FloatType.v() || t2 == DoubleType.v() || t2 == LongType.v()) { t = t2; return; }

	} else if (t == LongType.v())
	{
		if (t2 == ByteType.v() || t2 == ShortType.v() || t2 == IntType.v()) return;
		if (t2 == FloatType.v() || t2 == DoubleType.v()) { t = t2; return; }
	} else if (t == FloatType.v())
	{
		if (t2 == ByteType.v() || t2 == ShortType.v() || t2 == IntType.v() || t2 == LongType.v()) return;
		if (t2 == DoubleType.v()) { t = t2; return; }
	} else if (t == DoubleType.v())
	{
		if (t2 == ByteType.v() || t2 == IntType.v() || t2 == ShortType.v() || t2 == FloatType.v() || t2 == LongType.v()) return;
	} else if (t == NullType.v())
	{
		if (t2 instanceof RefType || t2 instanceof ArrayType) { t = t2; return; }
	} else if (t instanceof RefType || t instanceof ArrayType)
	{
		if (t2 == NullType.v()) return;
	}

	throw new RuntimeException("Type mismatch on then part and else part!");
}
/**
 * Need WORK! Don't use this at the moment!!!
 */
public void apply(Switch sw)
{
	((PAExprSwitch) sw).caseHookExpr(this);
}
	public Value getElseOp() { return op3Box.getValue(); }
	public ValueBox getElseOpBox() { return op3Box; }
	public Value getTestOp() { return op1Box.getValue(); }
	public ValueBox getTestOpBox() { return op1Box; }
	public Value getThenOp() { return op2Box.getValue(); }
	public ValueBox getThenOpBox() { return op2Box; }
/**
 * getType method comment.
 */
public Type getType() {
	return t;
}
/**
 * getUseBoxes method comment.
 */
public List getUseBoxes() {
	ca.mcgill.sable.util.List list = new ca.mcgill.sable.util.ArrayList();

	list.addAll(op1Box.getValue().getUseBoxes());
	list.add(op1Box);
	list.addAll(op2Box.getValue().getUseBoxes());
	list.add(op2Box);
	list.addAll(op3Box.getValue().getUseBoxes());
	list.add(op3Box);

	return list;
}
/**
 * toBriefString method comment.
 */
public String toBriefString() {
	return ((ToBriefString) op1Box.getValue()).toBriefString() + "?" +
	       ((ToBriefString) op2Box.getValue()).toBriefString() + ":" +
	       ((ToBriefString) op3Box.getValue()).toBriefString();
}
public String toString()
{
	return op1Box.getValue().toString() + "?" +
	       op2Box.getValue().toString() + ":" +
	       op3Box.getValue().toString();
}
}
