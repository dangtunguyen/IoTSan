package edu.ksu.cis.bandera.jjjc.decompiler;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000 Roby Joehanes (robbyjo@cis.ksu.edu)            *
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

public class DecompilerChanger extends AbstractJimpleValueSwitch {
	private static DecompilerChanger walker = new DecompilerChanger();
	private static Hashtable tempToType  = new Hashtable();
	private static Hashtable tempToValue = new Hashtable();
	private static Grimp grimp = Grimp.v();
public void caseAddExpr(AddExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newAddExpr(v1, v2));
}
public void caseAndExpr(AndExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newAndExpr(v1, v2));
}
public void caseArrayRef(ArrayRef v)
{
	Value v1 = change(v.getBase());
	Value v2 = change(v.getIndex());
	setResult(grimp.newArrayRef(v1, v2));
}
public void caseCastExpr(CastExpr v)
{
	setResult(grimp.newCastExpr(change(v.getOp()), v.getType()));
}
public void caseCaughtExceptionRef(CaughtExceptionRef v)
{
	setResult(v);
}
public void caseCmpExpr(CmpExpr v)
{
}
public void caseCmpgExpr(CmpgExpr v)
{
}
public void caseCmplExpr(CmplExpr v)
{
}
public void caseDivExpr(DivExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newDivExpr(v1, v2));
}
public void caseDoubleConstant(DoubleConstant v)
{
	setResult(v);
}
public void caseEqExpr(EqExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newEqExpr(v1, v2));
}
public void caseFloatConstant(FloatConstant v)
{
	setResult(v);
}
public void caseGeExpr(GeExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newGeExpr(v1, v2));
}
public void caseGtExpr(GtExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newGtExpr(v1, v2));
}
public void caseInstanceFieldRef(InstanceFieldRef v)
{
	setResult(grimp.newInstanceFieldRef(change(v.getBase()), v.getField()));
}
public void caseInstanceOfExpr(InstanceOfExpr v)
{
	setResult(grimp.newInstanceOfExpr(change(v.getOp()),v.getType()));
}
public void caseIntConstant(IntConstant v)
{
	setResult(v);
}
public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v)
{
	Value base = change(v.getBase());
	//setResult(grimp.newInterfaceInvokeExpr(base,v.getMethod(),processArgs(v)));
}
public void caseLeExpr(LeExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newLeExpr(v1, v2));
}
public void caseLengthExpr(LengthExpr v)
{
	setResult(grimp.newLengthExpr(change(v.getOp())));
}
public void caseLocal(Local v)
{
	//res.add(v.getName().trim());
}
public void caseLongConstant(LongConstant v)
{
	setResult(v);
}
public void caseLtExpr(LtExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newLtExpr(v1, v2));
}
public void caseMulExpr(MulExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newMulExpr(v1, v2));
}
public void caseNeExpr(NeExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newNeExpr(v1, v2));
}
public void caseNegExpr(NegExpr v)
{
	setResult(grimp.newNegExpr(change(v.getOp())));
}
public void caseNewArrayExpr(NewArrayExpr v)
{
	setResult(grimp.newNewArrayExpr(v.getType(),change(v.getSize())));
}
public void caseNewExpr(NewExpr v)
{
	setResult(v);
}
public void caseNewInvokeExpr(NewInvokeExpr v)
{
	setResult(grimp.newNewInvokeExpr(v.getBaseType(),v.getMethod(),processArgs(v)));
}
public void caseNewMultiArrayExpr(NewMultiArrayExpr v)
{
	ca.mcgill.sable.util.LinkedList l = new ca.mcgill.sable.util.LinkedList();
	int max = v.getSizeCount();

	// Get all sizes of the array
	for (int i=0; i<max; i++)
	{
		l.addLast(change(v.getSize(i)));
	}
	setResult(grimp.newNewMultiArrayExpr(v.getBaseType(),l));
}
public void caseNullConstant(NullConstant v)
{
	setResult(v);
}
public void caseOrExpr(OrExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newOrExpr(v1, v2));
}
public void caseParameterRef(ParameterRef v)
{
	setResult(v);
}
public void caseRemExpr(RemExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newRemExpr(v1, v2));
}
public void caseShlExpr(ShlExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newShlExpr(v1, v2));
}
public void caseShrExpr(ShrExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newShrExpr(v1, v2));
}
public void caseSpecialInvokeExpr(SpecialInvokeExpr v)
{
	//caseNonStaticInvokeExpr(v);
}
public void caseStaticFieldRef(StaticFieldRef v)
{
	setResult(v);
}
public void caseStaticInvokeExpr(StaticInvokeExpr v)
{
	setResult(grimp.newStaticInvokeExpr(v.getMethod(),processArgs(v)));
}
public void caseStringConstant(StringConstant v)
{
	setResult(v);
}
public void caseSubExpr(SubExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newSubExpr(v1, v2));
}
public void caseThisRef(ThisRef v)
{
	setResult(v);
}
public void caseUshrExpr(UshrExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newUshrExpr(v1, v2));
}
public void caseVirtualInvokeExpr(VirtualInvokeExpr v)
{
	//caseNonStaticInvokeExpr(v);
}
public void caseXorExpr(XorExpr v)
{
	Value v1 = change(v.getOp1());
	Value v2 = change(v.getOp2());
	setResult(grimp.newXorExpr(v1, v2));
}
public static Value change(Value v)
{
	walker.setResult(v);

	if (v != null) v.apply(walker);
	return (Value) walker.getResult();
}
public void defaultCase(Object v)
{
	// Nothing... probably some error
}
public static boolean isJimpleValue(Value v)
{
	String s = v.toString().trim();
	return (v instanceof Local) && ((s.startsWith("JJJCTEMP$")) || (s.startsWith("$ret")));
}
private ca.mcgill.sable.util.LinkedList processArgs(InvokeExpr v)
{
	ca.mcgill.sable.util.LinkedList l = new ca.mcgill.sable.util.LinkedList();
	int param = v.getArgCount();

	for (int i=0; i<param; i++)
	{
		l.addLast(change(v.getArg(i)));
	}
	return l;
}
public static void put (Value lhs, Value rhs)
{
	if (isJimpleValue(lhs))
	{
		tempToType.put(lhs,rhs.getType());
		tempToValue.put(lhs,rhs);
	}
}
public static void reset()
{
	tempToType = new Hashtable();
	tempToValue = new Hashtable();
}
}
