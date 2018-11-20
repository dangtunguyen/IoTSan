package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
import edu.ksu.cis.bandera.jext.*;
import java.util.Hashtable;
import org.apache.log4j.Category;
public class RenameExpression extends AbstractBanderaValueSwitch {
	private static Category log = 
			Category.getInstance(Inline.class.getName());
	static int count = 0;
	int id = ++count;
	String methodString;
	List parameters;
	Hashtable locals;
	public static List fields = new Vector();
	public ca.mcgill.sable.soot.SootMethod topMethod;
	protected ca.mcgill.sable.soot.SootMethod currentMethod;
	public static java.util.Hashtable newExpr2original = new Hashtable();
	/* [Thomas, July 26, 2017] */
	private boolean isSpecialInitMethod;
	
	
	public RenameExpression(String methodString, List parameters, Hashtable locals, SootMethod topMethod, SootMethod currentMethod) {
		this.methodString = methodString;
		this.parameters = parameters;
		this.locals = locals;
		this.topMethod = topMethod;
		this.currentMethod = currentMethod;
		this.isSpecialInitMethod = false;
	}
	public void caseAddExpr(AddExpr v) {
		setResult(Jimple.v().newAddExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseAndExpr(AndExpr v) {
		setResult(Jimple.v().newAndExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseArrayRef(ArrayRef v) {
		setResult(Jimple.v().newArrayRef(renameExpr(v.getBase()), renameExpr(v.getIndex())));
	}
	public void caseCastExpr(CastExpr v) {
		setResult(Jimple.v().newCastExpr(renameExpr(v.getOp()), v.getCastType()));
	}
	/*
public void caseNextNextStmtRef(NextNextStmtRef v)
{
defaultCase(v);
}
	 */

	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		setResult(v);
		//defaultCase(v);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (4/26/00 3:06:15 PM)
	 */
	public void caseChooseExpr(ChooseExpr v) {
		setResult(v);
	}
	public void caseCmpExpr(CmpExpr v) {
		setResult(Jimple.v().newCmpExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseCmpgExpr(CmpgExpr v) {
		setResult(Jimple.v().newCmpgExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseCmplExpr(CmplExpr v) {
		setResult(Jimple.v().newCmplExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	/**
	 * 
	 * @param expr edu.ksu.cis.bandera.jext.ComplementExpr
	 */
	public void caseComplementExpr(ComplementExpr expr) {
		log.error("Unhandled case: doing nothing");
		setResult(expr);
	}
	public void caseDivExpr(DivExpr v) {
		setResult(Jimple.v().newDivExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseDoubleConstant(DoubleConstant v) {
		setResult(DoubleConstant.v(v.value));
	}
	public void caseEqExpr(EqExpr v) {
		setResult(Jimple.v().newEqExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseFloatConstant(FloatConstant v) {
		setResult(FloatConstant.v(v.value));
	}
	public void caseGeExpr(GeExpr v) {
		setResult(Jimple.v().newGeExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseGtExpr(GtExpr v) {
		setResult(Jimple.v().newGtExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		if (!fields.contains(v.getField())) {
			fields.add(v.getField());
		}
		Value base = renameExpr(v.getBase());
		//	v.setBase(base);
		//defaultCase(v);
		setResult(Jimple.v().newInstanceFieldRef(base, v.getField()));
	}
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		setResult(Jimple.v().newInstanceOfExpr(renameExpr(v.getOp()),v.getCheckType()));
	}
	public void caseIntConstant(IntConstant v) {
		setResult(IntConstant.v(v.value));
	}
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		defaultCase(v);
		//setResult(v);
	}
	public void caseLeExpr(LeExpr v) {
		setResult(Jimple.v().newLeExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseLengthExpr(LengthExpr v) {
		setResult(Jimple.v().newLengthExpr(renameExpr(v.getOp())));
	}
	public void caseLocal(Local v) {
		if ((Inline.typeTable.size() > 0) && (Inline.typeTable.get(v) == null) && Inline.isMethodCompiled) {
			throw new RuntimeException("Cannot find abstraction for local <" + currentMethod + ", " + v + ">");
		}
		Local l;

//		if(methodString.contains("STSwitch"))
//		{
//			System.out.println();
//		}

		if(v.getName().startsWith("_static_"))
		{
			/* [Thomas, May 23, 2017]
			 * Keep static variable's name unchanged
			 * */
			if ((l = (Local) locals.get(v.getName())) == null) {
				l = Jimple.v().newLocal(v.getName(), v.getType());
				//locals.put(methodString + v.getName(), l);
				locals.put(v.getName(), l);
				Inline.newLocal2original.put(l, v);
				if ((Inline.typeTable.size() > 0) && Inline.isMethodCompiled)
					Inline.typeTable.put(l, Inline.typeTable.get(v));
				edu.ksu.cis.bandera.jjjc.CompilationManager.getAnnotationManager().putInlineLocal(new LocalExpr(topMethod, l), new LocalExpr(currentMethod, v));
			} else
				l = l;
		}
		else
		{
			if(this.isSpecialInitMethod)
			{
				if ((l = (Local) locals.get(methodString)) == null) {
					if (methodString.equals(""))
						l = Jimple.v().newLocal(v.getName(), v.getType());
					else
						l = Jimple.v().newLocal(methodString, v.getType());
					locals.put(methodString, l);
					Inline.newLocal2original.put(l, v);
					if ((Inline.typeTable.size() > 0) && Inline.isMethodCompiled)
						Inline.typeTable.put(l, Inline.typeTable.get(v));
					edu.ksu.cis.bandera.jjjc.CompilationManager.getAnnotationManager().putInlineLocal(new LocalExpr(topMethod, l), new LocalExpr(currentMethod, v));
				} else
					l = l;
			}
			else
			{
				if ((l = (Local) locals.get(methodString + v.getName() /* + id*/)) == null) {
					if (methodString.equals(""))
						l = Jimple.v().newLocal(v.getName(), v.getType());
					else
						l = Jimple.v().newLocal(methodString /* + id + "_"*/ +v.getName(), v.getType());
					locals.put(methodString + v.getName() /* + id*/, l);
					Inline.newLocal2original.put(l, v);
					if ((Inline.typeTable.size() > 0) && Inline.isMethodCompiled)
						Inline.typeTable.put(l, Inline.typeTable.get(v));
					edu.ksu.cis.bandera.jjjc.CompilationManager.getAnnotationManager().putInlineLocal(new LocalExpr(topMethod, l), new LocalExpr(currentMethod, v));
				} else
					l = l;
			}
		}
		setResult(l);
	}
	/**
	 * 
	 * @param expr edu.ksu.cis.bandera.jext.LogicalAndExpr
	 */
	public void caseLogicalAndExpr(LogicalAndExpr expr) {
		log.error("Unhandled case: doing nothing");
		setResult(expr);
	}
	/**
	 * 
	 * @param expr edu.ksu.cis.bandera.jext.LogicalOrExpr
	 */
	public void caseLogicalOrExpr(LogicalOrExpr expr) {
		log.error("Unhandled case: doing nothing");
		setResult(expr);
	}
	public void caseLongConstant(LongConstant v) {
		setResult(LongConstant.v(v.value));
	}
	public void caseLtExpr(LtExpr v) {
		setResult(Jimple.v().newLtExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseMulExpr(MulExpr v) {
		setResult(Jimple.v().newMulExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseNeExpr(NeExpr v) {
		setResult(Jimple.v().newNeExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseNegExpr(NegExpr v) {
		setResult(Jimple.v().newNegExpr(renameExpr(v.getOp())));
	}
	public void caseNewArrayExpr(NewArrayExpr v) {
		setResult(Jimple.v().newNewArrayExpr(v.getBaseType(), renameExpr(v.getSize())));
	}
	public void caseNewExpr(NewExpr v) {
		setResult(v);
	}
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		defaultCase(v);
	}
	public void caseNullConstant(NullConstant v) {
		setResult(NullConstant.v());
	}
	public void caseOrExpr(OrExpr v) {
		setResult(Jimple.v().newOrExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseParameterRef(ParameterRef v) {
		if (parameters == null || parameters.size() == 0)
			setResult(v);
		else {
			setResult((Value) parameters.get(0));
			parameters.remove(0);
		}
	}
	public void caseRemExpr(RemExpr v) {
		setResult(Jimple.v().newRemExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseShlExpr(ShlExpr v) {
		setResult(Jimple.v().newShlExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseShrExpr(ShrExpr v) {
		setResult(Jimple.v().newShrExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		ca.mcgill.sable.util.List l = new ca.mcgill.sable.util.VectorList();
		int i;
		for (i = 0; i < v.getArgCount(); i++)
			l.add(renameExpr(v.getArg(i)));
		setResult(Jimple.v().newSpecialInvokeExpr((Local) renameExpr(v.getBase()), v.getMethod(), l));
	}
	public void caseStaticFieldRef(StaticFieldRef v) {
		if (!fields.contains(v.getField())) {
			fields.add(v.getField());
		}
		setResult(v);
	}
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		ca.mcgill.sable.util.List l = new ca.mcgill.sable.util.VectorList();
		int i;
		for (i = 0; i < v.getArgCount(); i++)
			l.add(renameExpr(v.getArg(i)));
		setResult(Jimple.v().newStaticInvokeExpr(v.getMethod(), l));
	}
	public void caseStringConstant(StringConstant v) {
		setResult(StringConstant.v(v.value));
	}
	public void caseSubExpr(SubExpr v) {
		setResult(Jimple.v().newSubExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseThisRef(ThisRef v) {
		if (parameters == null || parameters.size() == 0)
			setResult(v);
		else {
			setResult((Value) parameters.get(0));
			parameters.remove(0);
		}
	}
	public void caseUshrExpr(UshrExpr v) {
		setResult(Jimple.v().newUshrExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		Value base = renameExpr(v.getBase());
		//	v.setBase(base);
		//defaultCase(v);
		ca.mcgill.sable.util.List l = new ca.mcgill.sable.util.VectorList();
		for (int i = 0; i < v.getArgCount(); i++)
			l.add(renameExpr(v.getArg(i)));
		setResult(Jimple.v().newVirtualInvokeExpr((Local) base, v.getMethod(), l));
	}
	public void caseXorExpr(XorExpr v) {
		setResult(Jimple.v().newXorExpr(renameExpr(v.getOp1()), renameExpr(v.getOp2())));
	}
	public void defaultCase(Object v) {
		throw new RuntimeException("Unhandled expression in" + " inline " + v.getClass() + " " + v);
	}
	public Value renameExpr(Value v) {
		v.apply(this);
		newExpr2original.put(getResult(), v);
		return (Value) this.getResult();
	}
	public String toString() {
		return "RE " + id;
	}
	
	public void setSpecialInitMethod()
	{
		this.isSpecialInitMethod = true;
	}
}
