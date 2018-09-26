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
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.*;
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.LinkedList;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;

/**
 * DecompilerValueSwitch is a recursive analyzer for values.
 * The methods are basically each case possible for values.
 * See DecompilerSwitch for details.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class DecompilerValueSwitch extends AbstractBanderaValueSwitch {
	private static DecompilerValueSwitch walker = new DecompilerValueSwitch();
	private Vector res = new Vector();
	private static boolean ifstmt = false;
public DecompilerValueSwitch()
{
}
public void caseAddExpr(AddExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("+");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseAndExpr(AndExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("&");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseArrayRef(ArrayRef v)
{
	evaluate(v.getBase());
	res.add("[");
	evaluate(v.getIndex());
	res.add("]");
}
public void caseCastExpr(CastExpr v)
{
	res.add("((");
	res.add(v.getType().toString());
	res.add(")");
	res.add(" ");
	evaluate(v.getOp());
	res.add(")");
}
public void caseCaughtExceptionRef(CaughtExceptionRef v)
{
	res.add(v.toString());
}
/**
 * This method was created in VisualAge.
 */
public void caseChooseExpr(ChooseExpr v) {
/*	SootClass sc = CompilationManager.getSootClassManager().getClass("edu.ksu.cis.bandera.abstraction.Abstraction");
	SootMethod sm = sc.getMethod("choose", new ca.mcgill.sable.util.LinkedList());
	
	ca.mcgill.sable.util.List args = v.getChoices();

	setResult(Jimple.v().newStaticInvokeExpr(sm, args).toString());
*/
	// This is a very nasty hack
	res.add("Bandera.choose() ? 0 : 1");
}
public void caseCmpExpr(CmpExpr v)
{
	res.add("/* CMP was added */");
}
public void caseCmpgExpr(CmpgExpr v)
{
	res.add("/* CMPG was added */");
}
public void caseCmplExpr(CmplExpr v)
{
	res.add("/* CMPL was added */");
}
public void caseDivExpr(DivExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("/");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseDoubleConstant(DoubleConstant v)
{
	res.add(v.toString());
}
public void caseEqExpr(EqExpr v)
{
	String sign = "==";
	Value leftOp = v.getOp1();
	Value rightOp = v.getOp2();
	String rightValue = rightOp.toString().trim();

	if (ifstmt) { sign = "!="; ifstmt = false; }
	if (rightValue.equals("0"))
	{
		if (DecompilerInfo.isBool(leftOp))
		{
			if (sign.equals("=="))
			{
				res.add("!");
			}
			evaluate(leftOp);
			return;
		}
	}
	evaluate(leftOp);
	res.add(" ");
	res.add(sign);
	res.add(" ");
	evaluate(rightOp);
}
public void caseFloatConstant(FloatConstant v)
{
	res.add(v.toString());
}
public void caseGeExpr(GeExpr v)
{
	String sign = ">=";
	if (ifstmt) { ifstmt = false; sign = "<"; }
	evaluate(v.getOp1());
	res.add(" ");
	res.add(sign);
	res.add(" ");
	evaluate(v.getOp2());
}
public void caseGtExpr(GtExpr v)
{
	String sign = ">";
	if (ifstmt) { ifstmt = false; sign = "<="; }
	evaluate(v.getOp1());
	res.add(" ");
	res.add(sign);
	res.add(" ");
	evaluate(v.getOp2());
}
public void caseInstanceFieldRef(InstanceFieldRef v)
{
	evaluate(v.getBase());
	res.add(".");
	res.add(v.getField().getName());
}
public void caseInstanceOfExpr(InstanceOfExpr v)
{
	evaluate(v.getOp());
	res.add(" ");
	res.add("instanceof");
	res.add(" ");
	res.add(v.getCheckType().toString());
}
public void caseIntConstant(IntConstant v)
{
	if (Decompiler.typeTable != null)
	{
		Abstraction lt = Decompiler.typeTable.get(v);
		// Is this abstracted?
		if (DecompilerUtil.isAbstracted(lt))
		{
			String tokName = AbstractionClassLoader.getTokenName(lt,v.value);
			String fullName = lt.getClass().getName();
			int lastDot = fullName.lastIndexOf(".");
			String pkgName = fullName.substring(0,lastDot);
			Decompiler.addImports(pkgName+".*");
			res.add(tokName);
		} else res.add(v.toString());
	} else res.add(v.toString());
}
public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v)
{
	caseNonStaticInvokeExpr(v);
}
private void caseInvokeExpr(InvokeExpr v)
{
	int param = v.getArgCount();

	SootMethod sm = v.getMethod();
	String methodName = sm.getName().trim();

	if ((methodName.length()>0) && (!methodName.equals("<init>")))
	{
		res.add(".");
		res.add(methodName);
	}
	res.add("(");

	for (int i=0; i<param; i++)
	{
		Value theParam = v.getArg(i);
		String paramType = sm.getParameterType(i).toString().trim();

		// We don't want integer values passed as booleans
		if ((theParam instanceof IntConstant) && (paramType.equals("boolean")))
		{
			String val = theParam.toString().trim();
			if (val.equals("0")) res.add("false");
				else if (val.equals("1")) res.add("true");
					else	res.add("true /* Bug in JJJC. boolval = "+val+" */");
		} else
		{
			evaluate(theParam);
		}
		if (i!=param-1) res.add(",");
	}

	// Close parentheses
	res.add(")");
}
public void caseLeExpr(LeExpr v)
{
	String sign = "<=";
	if (ifstmt) { ifstmt = false; sign = ">"; }
	evaluate(v.getOp1());
	res.add(" ");
	res.add(sign);
	res.add(" ");
	evaluate(v.getOp2());
}
public void caseLengthExpr(LengthExpr v)
{
	evaluate(v.getOp());
	res.add(".");
	res.add("length");
}
public void caseLocal(Local v)
{
	res.add(v.getName().trim());
}
public void caseLongConstant(LongConstant v)
{
	res.add(v.toString());
}
public void caseLtExpr(LtExpr v)
{
	String sign = "<";
	if (ifstmt) { ifstmt = false; sign = ">="; }
	evaluate(v.getOp1());
	res.add(" ");
	res.add(sign);
	res.add(" ");
	evaluate(v.getOp2());
}
public void caseMulExpr(MulExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("*");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseNeExpr(NeExpr v)
{
	String sign = "!=";
	Value leftOp = v.getOp1();
	Value rightOp = v.getOp2();
	String rightValue = rightOp.toString().trim();

	if (ifstmt) { sign = "=="; ifstmt = false; }
	if (rightValue.equals("0"))
	{
		if (DecompilerInfo.isBool(leftOp))
		{
			if (sign.equals("=="))
			{
				res.add("!");
			}
			evaluate(leftOp);
			return;
		}
	}
	evaluate(leftOp);
	res.add(" ");
	res.add(sign);
	res.add(" ");
	evaluate(rightOp);
}
public void caseNegExpr(NegExpr v)
{
	res.add("-");
	res.add(" ");
	evaluate(v.getOp());
}
public void caseNewArrayExpr(NewArrayExpr v)
{
	res.add("new");
	res.add(" ");
	String baseType = v.getType().toString();
	while (baseType.endsWith("[]"))
		baseType = baseType.substring(0,baseType.length()-2);
	res.add(baseType);
	res.add("[");
	evaluate(v.getSize());
	res.add("]");
}
public void caseNewExpr(NewExpr v)
{
	res.add("new");
	res.add(" ");
	res.add(v.getType().toString());
}
public void caseNewInvokeExpr(NewInvokeExpr v)
{
	res.add("new");
	res.add(" ");
	res.add(v.getBaseType().toString());
	caseInvokeExpr(v);
}
public void caseNewMultiArrayExpr(NewMultiArrayExpr v)
{
	int max = v.getSizeCount();
	
	res.add("new");
	res.add(" ");
	res.add(v.getBaseType().toString());

	// Get all sizes of the array
	for (int i=0; i<max; i++)
	{
		res.add("[");
		evaluate(v.getSize(i));
		res.add("]");
	}
}
private void caseNonStaticInvokeExpr(NonStaticInvokeExpr v)
{
	evaluate(v.getBase());
	caseInvokeExpr(v);
}
public void caseNullConstant(NullConstant v)
{
	//res.add(v.toString()); // i.e. null
	res.add("null");
}
public void caseOrExpr(OrExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("|");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseParameterRef(ParameterRef v)
{
	res.add(v.toString());
}
public void caseRemExpr(RemExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("%");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseShlExpr(ShlExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("<<");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseShrExpr(ShrExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add(">>");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseSpecialInvokeExpr(SpecialInvokeExpr v)
{
	caseNonStaticInvokeExpr(v);
}
public void caseStaticFieldRef(StaticFieldRef v)
{
	SootField sf = v.getField();
	String clsName = sf.getDeclaringClass().getName().trim();
	String fieldname = v.getField().getName().trim();
	if (!clsName.equals(DecompilerInfo.getClassName()))
	{
		res.add(clsName);
		res.add(".");
	}
	res.add(fieldname);
}
public void caseStaticInvokeExpr(StaticInvokeExpr v)
{
	res.add(v.getMethod().getDeclaringClass().getName());
	caseInvokeExpr(v);
}
public void caseStringConstant(StringConstant v)
{
	res.add(v.toString());
}
public void caseSubExpr(SubExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("-");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseThisRef(ThisRef v)
{
	res.add("this");
}
public void caseUshrExpr(UshrExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add(">>>");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void caseVirtualInvokeExpr(VirtualInvokeExpr v)
{
	caseNonStaticInvokeExpr(v);
}
public void caseXorExpr(XorExpr v)
{
	res.add("(");
	evaluate(v.getOp1());
	res.add(" ");
	res.add("^");
	res.add(" ");
	evaluate(v.getOp2());
	res.add(")");
}
public void defaultCase(Object v)
{
	// Nothing... probably some error
	res.add("/* Default case: Probably some error! */");
}
public static Vector evaluate(Value v)
{
	if (v==null) return new Vector();
	v.apply(walker);
	return walker.getRes();
}
private Vector getRes()
{
	return res;
}
public static void isAnIfStmt(boolean b) { ifstmt = b; }
public static void reset()
{
	walker.res = new Vector();
}
}
