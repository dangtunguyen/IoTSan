package edu.ksu.cis.bandera.specification.predicate;

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
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.annotation.*;
import java.util.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jext.*;
public class PredicateUpdate extends AbstractBanderaValueSwitch {
	private static boolean isNewBIRC = true;
	private static PredicateUpdate pu = new PredicateUpdate();
	private static Grimp grimp = Grimp.v();
	private static Jimple jimple = Jimple.v();
	private static Stmt currentStmt;
	private Value currentValue;
/**
 * caseAddExpr method comment.
 */
public void caseAddExpr(AddExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newAddExpr(v1, v2);
}
/**
 * caseAndExpr method comment.
 */
public void caseAndExpr(AndExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newAndExpr(v1, v2);
}
/**
 * caseArrayRef method comment.
 */
public void caseArrayRef(ArrayRef v) {
	v.getBase().apply(this);
	Value base = currentValue;
	v.getIndex().apply(this);
	Value index = currentValue;
	currentValue = grimp.newArrayRef(base, index);
}
/**
 * caseCastExpr method comment.
 */
public void caseCastExpr(CastExpr v) {
	v.getOp().apply(this);
	currentValue = grimp.newCastExpr(currentValue, v.getType());
}
/**
 * caseCaughtExceptionRef method comment.
 */
public void caseCaughtExceptionRef(CaughtExceptionRef v) {
	defaultCase(v);
}
/**
 * caseChooseExpr method comment.
 */
public void caseChooseExpr(ChooseExpr v) {
	Vector values = new Vector();
	for (ca.mcgill.sable.util.Iterator i = v.getChoices().iterator(); i.hasNext();) {
		((Value) i.next()).apply(this);
		values.add(currentValue);
	}
	currentValue = new ChooseExpr(values);
}
/**
 * caseCmpExpr method comment.
 */
public void caseCmpExpr(CmpExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newCmpExpr(v1, v2);
}
/**
 * caseCmpgExpr method comment.
 */
public void caseCmpgExpr(CmpgExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newCmpgExpr(v1, v2);
}
/**
 * caseCmplExpr method comment.
 */
public void caseCmplExpr(CmplExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newCmplExpr(v1, v2);
}
/**
 * caseComplementExpr method comment.
 */
public void caseComplementExpr(ComplementExpr expr) {
	expr.getOp().apply(this);
    if (expr.getOp() instanceof LocationTestExpr)
    {
        currentValue = new AllThreadsExpr(new ComplementExpr(((ExistsThreadExpr) currentValue).getOp()));
    }
    else
    {
	   currentValue = new ComplementExpr(currentValue);
    }
}
/**
 * caseDivExpr method comment.
 */
public void caseDivExpr(DivExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newDivExpr(v1, v2);
}
/**
 * caseDoubleConstant method comment.
 */
public void caseDoubleConstant(DoubleConstant v) {
	currentValue = DoubleConstant.v(v.value);
}
/**
 * caseEqExpr method comment.
 */
public void caseEqExpr(EqExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newEqExpr(v1, v2);
}
/**
 * caseFloatConstant method comment.
 */
public void caseFloatConstant(FloatConstant v) {
	currentValue = FloatConstant.v(v.value);
}
/**
 * caseGeExpr method comment.
 */
public void caseGeExpr(GeExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newGeExpr(v1, v2);
}
/**
 * caseGtExpr method comment.
 */
public void caseGtExpr(GtExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newGtExpr(v1, v2);
}
/**
 * caseInExpr method comment.
 */
public void caseInExpr(InExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;

	Vector values = new Vector();
	for (ca.mcgill.sable.util.Iterator i = v.getSet().iterator(); i.hasNext();) {
		((Value) i.next()).apply(this);
		values.add(currentValue);
	}
	currentValue = new InExpr(v1, values);
}
/**
 * caseInstanceFieldRef method comment.
 */
public void caseInstanceFieldRef(InstanceFieldRef v) {
	v.getBase().apply(this);
	currentValue = grimp.newInstanceFieldRef(currentValue, v.getField());
}
/**
 * caseInstanceOfExpr method comment.
 */
public void caseInstanceOfExpr(InstanceOfExpr v) {
	v.getOp().apply(this);
	currentValue = grimp.newInstanceOfExpr(currentValue, v.getType());
}
/**
 * caseIntConstant method comment.
 */
public void caseIntConstant(IntConstant v) {
	currentValue = IntConstant.v(v.value);
}
/**
 * caseInterfaceInvokeExpr method comment.
 */
public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
	defaultCase(v);
}
/**
 * caseLeExpr method comment.
 */
public void caseLeExpr(LeExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newLeExpr(v1, v2);
}
/**
 * caseLengthExpr method comment.
 */
public void caseLengthExpr(LengthExpr v) {
	v.getOp().apply(this);
	currentValue = grimp.newLengthExpr(currentValue);
}
/**
 * caseLocal method comment.
 */
public void caseLocal(Local l) {
	defaultCase(l);
}
/**
 * caseLocalExpr method comment.
 */
public void caseLocalExpr(LocalExpr v) {
	AnnotationManager am = CompilationManager.getAnnotationManager();
	HashSet locals = am.getInlinedLocal(v);
	Hashtable stmtLocalTable = am.getStmtLocalTable();
	HashSet stmtLocals = (HashSet) stmtLocalTable.get(currentStmt);

	for (Iterator i = locals.iterator(); i.hasNext();) {
		LocalExpr newV = (LocalExpr) i.next();
		if (stmtLocals.contains(newV)) {
			Hashtable localPackTable = am.getLocalPackingTable();
			if (localPackTable.get(newV) != null) {
				currentValue = (Value) localPackTable.get(newV);
			} else {
				currentValue = newV;
			}
			return;
		} 
	}

	System.out.println("*** WARNING: Can't find mapping to " + v + " ***");
}
/**
 * caseLocationTestExpr method comment.
 */
public void caseLocationTestExpr(LocationTestExpr lte) {
	AnnotationManager am = CompilationManager.getAnnotationManager();
	Vector newStmts = new Vector();
	for (Iterator i = lte.getStmts().iterator(); i.hasNext();) {
		Stmt stmt = (Stmt) i.next();
		if (am.getReplacedStmt(stmt) != null)
			stmt = am.getReplacedStmt(stmt);
		if (am.getInlinedStmt(stmt) != null)
			newStmts.addAll(am.getInlinedStmt(stmt));
		else {
			//System.out.println("*** WARNING : Can't update '" + value + "'");
			throw new RuntimeException();
		}
	}
	Grimp grimp = Grimp.v();
	Vector result = new Vector();
	for (Iterator i = newStmts.iterator(); i.hasNext();) {
		currentStmt = (Stmt) i.next();
		SootMethod sm = getThreadBody(currentStmt);
		if (sm != null) {
			if (isNewBIRC)
			{
                /*
				SootField sf = createLoc(sm, currentStmt, result.size());
				result.add(grimp.newGtExpr(grimp.newStaticFieldRef(sf), IntConstant.v(0)));
                */
                result.add(currentStmt);
			}
			else
			{
				result.add(currentStmt);
			}
		}
	}
	if (isNewBIRC)
	{
        /*
		currentValue = (Value) result.firstElement();
		result.remove(currentValue);
		for (Iterator i = result.iterator(); i.hasNext();)
		{
			currentValue = new LogicalOrExpr(currentValue, (Value) i.next());
		}
        */
        currentValue = new ExistsThreadExpr(new LocationTestExpr(result));
	}
	else
	{
		currentValue = new LocationTestExpr(result);
	}
}


private SootField createLoc(SootMethod sm, Stmt stmt, int i)
{
	SootClass sc = sm.getDeclaringClass();
	SootField result = new SootField("loc$" + i, IntType.v(), Modifier.STATIC);
	sc.addField(result);
	
	Jimple jimple = Jimple.v();
	
	JimpleBody jb = (JimpleBody) sm.getBody(jimple);
	StmtList stmtList = jb.getStmtList();

	String localName = "bsl$loc";
	
	if (!jb.declaresLocal(localName))
	{
		Local l = jimple.newLocal(localName, IntType.v());
		jb.addLocal(l);
	}
	
	Local l = jb.getLocal(localName);

	int stmtIndex = stmtList.indexOf(stmt);
	
	//  1: Bandera.beginAtomic();
	//  2: bsl$loc = <declaringclass>.loc$<i>;
	//  3: bsl$loc = bsl$loc + 1;
	//  4: <declaringclass>.loc$<i> = bsl$loc;
	//  5: bsl$loc = 0;
	//  6: Bandera.endAtomic();
	//  7: Bandera.beginAtomic();
	// <stmt>
	//  8: bsl$loc = <declaringclass>.loc$<i>;
	//  9: bsl$loc = bsl$loc - 1;
	// 10: <declaringclass>.loc$<i> = bsl$loc;
	// 11: bsl$loc = 0;
	// 12: Bandera.endAtomic();

	
	Stmt stmt1 = jimple.newInvokeStmt(jimple.newStaticInvokeExpr(getBeginAtomic(), new ca.mcgill.sable.util.LinkedList()));
	Stmt stmt2 = jimple.newAssignStmt(l, jimple.newStaticFieldRef(result));
	Stmt stmt3 = jimple.newAssignStmt(l, jimple.newAddExpr(l, IntConstant.v(1)));
	Stmt stmt4 = jimple.newAssignStmt(jimple.newStaticFieldRef(result), l);
	Stmt stmt5 = jimple.newAssignStmt(l, IntConstant.v(0));
	Stmt stmt6 = jimple.newInvokeStmt(jimple.newStaticInvokeExpr(getEndAtomic(), new ca.mcgill.sable.util.LinkedList()));
	Stmt stmt7 = jimple.newInvokeStmt(jimple.newStaticInvokeExpr(getBeginAtomic(), new ca.mcgill.sable.util.LinkedList()));
	Stmt stmt8 = jimple.newAssignStmt(l, jimple.newStaticFieldRef(result));
	Stmt stmt9 = jimple.newAssignStmt(l, jimple.newSubExpr(l, IntConstant.v(1)));
	Stmt stmt10 = jimple.newAssignStmt(jimple.newStaticFieldRef(result), l);
	Stmt stmt11 = jimple.newAssignStmt(l, IntConstant.v(0));
	Stmt stmt12 = jimple.newInvokeStmt(jimple.newStaticInvokeExpr(getEndAtomic(), new ca.mcgill.sable.util.LinkedList()));
	
	stmtList.add(stmtIndex++, stmt1);
	jb.redirectJumps(stmt, stmt1);
	
	stmtList.add(stmtIndex++, stmt2);
	stmtList.add(stmtIndex++, stmt3);
	stmtList.add(stmtIndex++, stmt4);
	stmtList.add(stmtIndex++, stmt5);
	stmtList.add(stmtIndex++, stmt6);
	stmtList.add(stmtIndex++, stmt7);

	if (stmt instanceof ReturnStmt || stmt instanceof ReturnVoidStmt)
	{
		stmtList.add(stmtIndex++, stmt8);
		stmtList.add(stmtIndex++, stmt9);
		stmtList.add(stmtIndex++, stmt10);
		stmtList.add(stmtIndex++, stmt11);
		stmtList.add(stmtIndex, stmt12);
	}
	else
	{
		stmtIndex = stmtList.indexOf(stmt) + 1;
		
		stmtList.add(stmtIndex, stmt12);
		stmtList.add(stmtIndex, stmt11);
		stmtList.add(stmtIndex, stmt10);
		stmtList.add(stmtIndex, stmt9);
		stmtList.add(stmtIndex, stmt8);
	}
	
	return result;
}

private SootMethod getBeginAtomic()
{
	SootClassManager scm = CompilationManager.getSootClassManager();
	SootClass bandera = scm.getClass("Bandera");
	return bandera.getMethod("beginAtomic");
}

private SootMethod getEndAtomic()
{
	SootClassManager scm = CompilationManager.getSootClassManager();
	SootClass bandera = scm.getClass("Bandera");
	return bandera.getMethod("endAtomic");
}

/**
 * caseLogicalAndExpr method comment.
 */
public void caseLogicalAndExpr(LogicalAndExpr expr) {
	if (CompilationManager.hasLocPredicatePair(expr)) {
		AnnotationManager am = CompilationManager.getAnnotationManager();
		LocationTestExpr lte = (LocationTestExpr) expr.getOp1();
		Value constraint = (Value) expr.getOp2();
		Vector newStmts = new Vector();
		for (Iterator i = lte.getStmts().iterator(); i.hasNext();) {
			Stmt stmt = (Stmt) i.next();
			if (am.getReplacedStmt(stmt) != null)
				stmt = am.getReplacedStmt(stmt);
			if (am.getInlinedStmt(stmt) != null)
				newStmts.addAll(am.getInlinedStmt(stmt));
			else {
				//System.out.println("*** WARNING : Can't update '" + value + "'");
				throw new RuntimeException();
			}
		}
		Vector result = new Vector();
		for (Iterator i = newStmts.iterator(); i.hasNext();) {
			currentStmt = (Stmt) i.next();
			SootMethod sm = getThreadBody(currentStmt);
			if (sm != null) {
				if (isNewBIRC)
				{
                    /*
					SootField sf = createLoc(sm, currentStmt, result.size());
					Value v = grimp.newGtExpr(grimp.newStaticFieldRef(sf), IntConstant.v(0));
					pu.currentValue = null;
					constraint.apply(pu);
					result.add(new LogicalAndExpr(v, pu.currentValue));
                    */
                    Vector v = new Vector();
                    v.add(currentStmt);
                    LocationTestExpr newLte = new LocationTestExpr(v);
                    pu.currentValue = null;
                    constraint.apply(pu);
                    result.add(new ExistsThreadExpr(new LogicalAndExpr(newLte, pu.currentValue)));
				}
				else
				{
					Vector v = new Vector();
					v.add(currentStmt);
					LocationTestExpr newLte = new LocationTestExpr(v);
					pu.currentValue = null;
					constraint.apply(pu);
					result.add(new LogicalAndExpr(newLte, pu.currentValue));
				}
			}
		}
		currentValue = (Value) result.firstElement();
		result.remove(currentValue);
		for (Iterator i = result.iterator(); i.hasNext();)
		{
			currentValue = new LogicalOrExpr(currentValue, (Value) i.next());
		}
	} else {
		expr.getOp1().apply(this);
		Value v1 = currentValue;
		expr.getOp2().apply(this);
		Value v2 = currentValue;
		currentValue = new LogicalAndExpr(v1, v2);
	}
}
/**
 * caseLogicalOrExpr method comment.
 */
public void caseLogicalOrExpr(LogicalOrExpr expr) {
    if (CompilationManager.hasLocPredicatePair(expr)) {
        AnnotationManager am = CompilationManager.getAnnotationManager();
        ComplementExpr ce = (ComplementExpr) expr.getOp1();
        LocationTestExpr lte = (LocationTestExpr) ce.getOp();
        Value constraint = (Value) expr.getOp2();
        Vector newStmts = new Vector();
        for (Iterator i = lte.getStmts().iterator(); i.hasNext();) {
            Stmt stmt = (Stmt) i.next();
            if (am.getReplacedStmt(stmt) != null)
                stmt = am.getReplacedStmt(stmt);
            if (am.getInlinedStmt(stmt) != null)
                newStmts.addAll(am.getInlinedStmt(stmt));
            else {
                //System.out.println("*** WARNING : Can't update '" + value + "'");
                throw new RuntimeException();
            }
        }
        Vector result = new Vector();
        for (Iterator i = newStmts.iterator(); i.hasNext();) {
            currentStmt = (Stmt) i.next();
            SootMethod sm = getThreadBody(currentStmt);
            if (sm != null) {
                if (isNewBIRC)
                {
                    /*
                    SootField sf = createLoc(sm, currentStmt, result.size());
                    Value v = grimp.newGtExpr(grimp.newStaticFieldRef(sf), IntConstant.v(0));
                    pu.currentValue = null;
                    constraint.apply(pu);
                    result.add(new LogicalAndExpr(v, pu.currentValue));
                    */
                    Vector v = new Vector();
                    v.add(currentStmt);
                    LocationTestExpr newLte = new LocationTestExpr(v);
                    pu.currentValue = null;
                    constraint.apply(pu);
                    result.add(new AllThreadsExpr(new LogicalOrExpr(new ComplementExpr(newLte), pu.currentValue)));
                }
                else
                {
                    Vector v = new Vector();
                    v.add(currentStmt);
                    LocationTestExpr newLte = new LocationTestExpr(v);
                    pu.currentValue = null;
                    constraint.apply(pu);
                    result.add(new LogicalOrExpr(new ComplementExpr(newLte), pu.currentValue));
                }
            }
        }
        currentValue = (Value) result.firstElement();
        result.remove(currentValue);
        for (Iterator i = result.iterator(); i.hasNext();)
        {
            currentValue = new LogicalAndExpr(currentValue, (Value) i.next());
        }
    } else {
    	expr.getOp1().apply(this);
    	Value v1 = currentValue;
    	expr.getOp2().apply(this);
    	Value v2 = currentValue;
    	currentValue = new LogicalOrExpr(v1, v2);
    }
}
/**
 * caseLongConstant method comment.
 */
public void caseLongConstant(LongConstant v) {
	currentValue = LongConstant.v(v.value);
}
/**
 * caseLtExpr method comment.
 */
public void caseLtExpr(LtExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newLtExpr(v1, v2);
}
/**
 * caseMulExpr method comment.
 */
public void caseMulExpr(MulExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newMulExpr(v1, v2);
}
/**
 * caseNeExpr method comment.
 */
public void caseNeExpr(NeExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newNeExpr(v1, v2);
}
/**
 * caseNegExpr method comment.
 */
public void caseNegExpr(NegExpr v) {
	v.getOp().apply(this);
	currentValue = grimp.newNegExpr(currentValue);
}
/**
 * caseNewArrayExpr method comment.
 */
public void caseNewArrayExpr(NewArrayExpr v) {
	defaultCase(v);
}
/**
 * caseNewExpr method comment.
 */
public void caseNewExpr(NewExpr v) {
	defaultCase(v);
}
/**
 * caseNewInvokeExpr method comment.
 */
public void caseNewInvokeExpr(NewInvokeExpr v) {
	defaultCase(v);
}
/**
 * caseNewMultiArrayExpr method comment.
 */
public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
	defaultCase(v);
}
/**
 * caseNullConstant method comment.
 */
public void caseNullConstant(NullConstant v) {
	currentValue = NullConstant.v();
}
/**
 * caseOrExpr method comment.
 */
public void caseOrExpr(OrExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newOrExpr(v1, v2);
}
/**
 * caseParameterRef method comment.
 */
public void caseParameterRef(ParameterRef v) {
	defaultCase(v);
}
/**
 * caseRemExpr method comment.
 */
public void caseRemExpr(RemExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newRemExpr(v1, v2);
}
/**
 * caseShlExpr method comment.
 */
public void caseShlExpr(ShlExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newShlExpr(v1, v2);
}
/**
 * caseShrExpr method comment.
 */
public void caseShrExpr(ShrExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newShrExpr(v1, v2);
}
/**
 * caseSpecialInvokeExpr method comment.
 */
public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
	defaultCase(v);
}
/**
 * caseStaticFieldRef method comment.
 */
public void caseStaticFieldRef(StaticFieldRef v) {
	currentValue = grimp.newStaticFieldRef(v.getField());
}
/**
 * caseStaticInvokeExpr method comment.
 */
public void caseStaticInvokeExpr(StaticInvokeExpr v) {
	defaultCase(v);
}
/**
 * caseStringConstant method comment.
 */
public void caseStringConstant(StringConstant v) {
	currentValue = StringConstant.v(v.value);
}
/**
 * caseSubExpr method comment.
 */
public void caseSubExpr(SubExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newSubExpr(v1, v2);
}
/**
 * caseThisRef method comment.
 */
public void caseThisRef(ThisRef v) {
	defaultCase(v);
}
/**
 * caseUshrExpr method comment.
 */
public void caseUshrExpr(UshrExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newUshrExpr(v1, v2);
}
/**
 * caseVirtualInvokeExpr method comment.
 */
public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
	defaultCase(v);
}
/**
 * caseXorExpr method comment.
 */
public void caseXorExpr(XorExpr v) {
	v.getOp1().apply(this);
	Value v1 = currentValue;
	v.getOp2().apply(this);
	Value v2 = currentValue;
	currentValue = grimp.newXorExpr(v1, v2);
}
/**
 * 
 * @return java.util.HashSet
 */
private static HashSet getThreadMethods() {
	HashSet result = new HashSet();
	
	for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements();) {
		SootClass sc = (SootClass) e.nextElement();
		if (sc.declaresMethod("main")) {
			result.add(sc.getMethod("main"));
		} else if (sc.declaresMethod("run")) {
			result.add(sc.getMethod("run"));
		}
	}

	return result;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootMethod
 * @param s java.util.Vector
 */
private static SootMethod getThreadBody(Stmt stmt) {
	Jimple jimple = Jimple.v();
	AnnotationManager am = CompilationManager.getAnnotationManager();
	
	HashSet stmtListSet = new HashSet();
	for (Iterator i = getThreadMethods().iterator(); i.hasNext();) {
		SootMethod sm = (SootMethod) i.next();
		StmtList sl = ((JimpleBody) sm.getBody(jimple)).getStmtList();
		if (sl.indexOf(stmt) >= 0) {
			return sm;
		}
	}
	
	return null;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 * @param value ca.mcgill.sable.soot.jimple.Value
 */
public static Value update(Value value) {
	value.apply(pu);
	value = pu.currentValue;
	pu.currentValue = null;
	return value;
}
}
