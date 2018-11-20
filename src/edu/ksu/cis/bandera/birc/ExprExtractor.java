package edu.ksu.cis.bandera.birc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;

import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.bir.StateVar;
import edu.ksu.cis.bandera.bir.LocVector;
import edu.ksu.cis.bandera.bir.ThreadLocTest;
import edu.ksu.cis.bandera.bir.IntLit;
import edu.ksu.cis.bandera.bir.NullExpr;
import edu.ksu.cis.bandera.jext.*;

import org.apache.log4j.Category;

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

/**
 * A Jimple value switch that translates Jimple expression values into
 * BIR expression values.
 *
 * @author James Corbett &gt;corbett@hawaii.edu&lt;
 * @version $Revision: 1.4 $ - $Date: 2003/04/30 19:32:52 $
 */
public class ExprExtractor extends AbstractBanderaValueSwitch {

    private static Category log = Category.getInstance(ExprExtractor.class);

    TransSystem system;
    Stmt stmt;
    LocalDefs localDefs;
    SootMethod method;
    TypeExtractor typeExtract;
    PredicateSet predSet;

    boolean observable = false;

    public ExprExtractor(TransSystem system, Stmt stmt, LocalDefs localDefs,
			 SootMethod method, TypeExtractor typeExtract,
			 PredicateSet predSet) {
	// These params provide a context for the extraction.
	// Not all of them are necessary (e.g., the method is
	// required only if the expression might contain a Local).
	this.system = system;
	this.stmt = stmt;
	this.localDefs = localDefs;
	this.method = method;
	this.typeExtract = typeExtract;
	this.predSet = predSet;
    }
    public void caseAddExpr(AddExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.AddExpr(expr1, expr2));
    }
    public void caseAndExpr(AndExpr expr) {
	// We don't handle the bitwise integer AND and OR
	defaultCase(expr);
    }
    public void caseArrayRef(ArrayRef expr) {
	expr.getBase().apply(this);
	edu.ksu.cis.bandera.bir.Expr arrayRef =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	// Need to explicitly deref to access element
	edu.ksu.cis.bandera.bir.Expr array = 
	    new edu.ksu.cis.bandera.bir.DerefExpr(arrayRef);
	expr.getIndex().apply(this);
	edu.ksu.cis.bandera.bir.Expr index =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.ArrayExpr(array, index));	
    }
    public void caseCastExpr(CastExpr expr) {
	// Casts are implicit in BIR
	expr.getOp().apply(this);
    }
    /**
     * Jimple extension: choose from a set of values.
     */

    public void caseChooseExpr(ChooseExpr expr) {
	Object choices [] = expr.getChoices().toArray();
	((Value)choices[0]).apply(this);
	edu.ksu.cis.bandera.bir.Expr choice = 
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	edu.ksu.cis.bandera.bir.ChooseExpr choose = 
	    new edu.ksu.cis.bandera.bir.InternChooseExpr(choice);
	for (int i = 1; i < choices.length; i++) {
	    ((Value)choices[i]).apply(this);
	    choose.addChoice((edu.ksu.cis.bandera.bir.Expr) getResult());
	}
	setResult(choose);
    }

    public void caseExternalChooseExpr(ExternalChooseExpr expr) {
        Object choices [] = expr.getChoices().toArray();
        ((Value)choices[0]).apply(this);
        edu.ksu.cis.bandera.bir.Expr choice = 
            (edu.ksu.cis.bandera.bir.Expr) getResult();
        edu.ksu.cis.bandera.bir.ChooseExpr choose = 
            new edu.ksu.cis.bandera.bir.ExternChooseExpr(choice);
        for (int i = 1; i < choices.length; i++) {
            ((Value)choices[i]).apply(this);
            choose.addChoice((edu.ksu.cis.bandera.bir.Expr) getResult());
        }
        setResult(choose);
    }

    public void caseComplementExpr(ComplementExpr expr) {
	expr.getOp().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.NotExpr(expr1));
    }
    public void caseDivExpr(DivExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.DivExpr(expr1, expr2));
    }
    public void caseEqExpr(EqExpr expr)
    {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.EqExpr(expr1, expr2));
    }
    public void caseGeExpr(GeExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.LeExpr(expr2, expr1));
	//setResult(new edu.ksu.cis.bandera.bir.LtExpr(expr2, expr1));
    }
    public void caseGtExpr(GtExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.LtExpr(expr2, expr1));
	//setResult(new edu.ksu.cis.bandera.bir.LeExpr(expr2, expr1));
    }

    /**
     * InExpr is a Jimple extension to support tests on abstracted types.
     * It tests a value for membership in a set of constants.  We translate
     * this test to a disjunction of equality tests.
     */
    public void caseInExpr(InExpr expr) {
	edu.ksu.cis.bandera.bir.Expr result = null;
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr op1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	Set values = expr.getSet();
	Iterator valIt = values.iterator();
	while (valIt.hasNext()) {
	    Value val = (Value) valIt.next();
	    val.apply(this);
	    edu.ksu.cis.bandera.bir.Expr element  =
		(edu.ksu.cis.bandera.bir.Expr) getResult();
	    edu.ksu.cis.bandera.bir.Expr compare = 
		new edu.ksu.cis.bandera.bir.EqExpr(op1,element);
	    if (result == null)
		result = compare;
	    else
		result = new edu.ksu.cis.bandera.bir.OrExpr(result,compare);
	}
	setResult(result);
    }
    public void caseInstanceFieldRef(InstanceFieldRef expr) {
	log.debug("ExprExtractor.caseInstanceFieldRef:field " + expr.getField());
	if (predSet.isObservable(expr.getField()))
	    observable = true;
	expr.getBase().apply(this);
	edu.ksu.cis.bandera.bir.Expr base =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	// Need to explicitly deref to access element
	edu.ksu.cis.bandera.bir.Expr rec = 
	    new edu.ksu.cis.bandera.bir.DerefExpr(base);
	edu.ksu.cis.bandera.bir.Record recType = 
	    (edu.ksu.cis.bandera.bir.Record)
	    ((edu.ksu.cis.bandera.bir.Ref)base.getType()).getTargetType();
	log.debug("ExprExtractor.caseInstanceFieldRef: recType " + recType);
	edu.ksu.cis.bandera.bir.Field field = 
	    recType.getField(expr.getField().getName());
	edu.ksu.cis.bandera.bir.RecordExpr re = new edu.ksu.cis.bandera.bir.RecordExpr(rec,field);
	log.debug("ExprExtractor.caseInstanceFieldRef: record " + rec + " and field " + field);
	setResult(re);
    }
    public void caseInstanceOfExpr(InstanceOfExpr expr) {
	expr.getOp().apply(this);
	edu.ksu.cis.bandera.bir.Expr refExpr =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();

	// Use type extractor to get BIR type for SootClass

	//	expr.getCheckType().apply(typeExtract);
	//	edu.ksu.cis.bandera.bir.Ref refType = 
	//	    (edu.ksu.cis.bandera.bir.Ref) typeExtract.getResult();
	//	setResult(new edu.ksu.cis.bandera.bir.InstanceOfExpr(refExpr,refType));

	edu.ksu.cis.bandera.bir.Record recType = 
	    typeExtract.getRecordType((RefType)expr.getCheckType());
	edu.ksu.cis.bandera.bir.InstanceOfExpr iofExpr = 
	    new edu.ksu.cis.bandera.bir.InstanceOfExpr(refExpr,recType);
	setResult(new edu.ksu.cis.bandera.bir.NeExpr(iofExpr,IntLit.ZERO));
    }
    public void caseIntConstant(IntConstant expr) {
	setResult(new IntLit(expr.value));
    }
    public void caseLeExpr(LeExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.LeExpr(expr1, expr2));
    }
    public void caseLengthExpr(LengthExpr expr) {
	expr.getOp().apply(this);
	edu.ksu.cis.bandera.bir.Expr arrayRef =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	// Need to explicitly deref to access element
	edu.ksu.cis.bandera.bir.Expr array = 
	    new edu.ksu.cis.bandera.bir.DerefExpr(arrayRef);
	setResult(new edu.ksu.cis.bandera.bir.LengthExpr(array));
    }
    public void caseLocal(Local expr) {
	if (method == null) {
	    throw new RuntimeException("Local appears in predicate outside of a LocalExpr.  Expression: " + expr);
	}
	String key = localKey(method,expr);
	// Mark expression as observable if Local is 
	if (predSet.isObservable(key))
	    observable = true;
	edu.ksu.cis.bandera.bir.StateVar var = system.getVarOfKey(key);
	if (var == null) {
	    throw new RuntimeException("Variable " + expr + " was not declared.  method = " + method + ", key = " + key);
	}
	setResult(var);
    }
    /**
     * A LocalExpr is a Jimple extension that carries its own method
     * (so a Local can appear in a predicate expression, which is
     * extracted outside the context of any method).
     */

    public void caseLocalExpr(LocalExpr expr) { 
	SootMethod method = expr.getMethod();
	Local local = expr.getLocal();
	String key = ExprExtractor.localKey(method,local);
	edu.ksu.cis.bandera.bir.StateVar var = system.getVarOfKey(key);
	if (var == null)
	    throw new RuntimeException("Variable not declared: " + expr);
	setResult(var);	
    }
    public void caseLocationTestExpr(LocationTestExpr expr) {
	// Build a large disjunction of ThreadLocTest expressions
	// for all the Stmts and their locations.
	// Note that the predicate set holds the set of locations
	// for a given Stmt.
	edu.ksu.cis.bandera.bir.Expr predExpr = null;
	Vector stmts = expr.getStmts();
	for (int i = 0; i < stmts.size(); i++) {
	    Stmt stmt = (Stmt) stmts.elementAt(i);
	    LocVector locs = predSet.getPredicateLocations(stmt);
	    for (int j = 0; j < locs.size(); j++) {

		// HACK: what do we actually pass as LHS here!!!
		ThreadLocTest locTest = new ThreadLocTest(locs.elementAt(j),
							  new NullExpr(system));
		if (predExpr == null)
		    predExpr = locTest;
		else
		    predExpr =
			new edu.ksu.cis.bandera.bir.OrExpr(predExpr,locTest);
	    }	    
	}
	if (predExpr == null)
	    predExpr = new edu.ksu.cis.bandera.bir.BoolLit(false);
	setResult(predExpr);
    }
    public void caseLogicalAndExpr(LogicalAndExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.AndExpr(expr1,expr2));
    }
    public void caseLogicalOrExpr(LogicalOrExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.OrExpr(expr1,expr2));
    }
    public void caseLtExpr(LtExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.LtExpr(expr1, expr2));
    }
    public void caseMulExpr(MulExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.MulExpr(expr1, expr2));
    }
    public void caseNeExpr(NeExpr expr)
    {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.NeExpr(expr1, expr2));
    }
    public void caseNegExpr(NegExpr expr) {
	expr.getOp().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.SubExpr(IntLit.ZERO,expr1));
    }
    public void caseNewArrayExpr(NewArrayExpr expr) {
	StateVar col = system.getVarOfKey(expr);
	expr.getSize().apply(this);
	edu.ksu.cis.bandera.bir.Expr size =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.NewArrayExpr(col,size));
    }
    public void caseNewExpr(NewExpr expr) {
	StateVar col = system.getVarOfKey(expr);
	if (col != null)
	    setResult(new edu.ksu.cis.bandera.bir.NewExpr(col));
	else  
	    // No collection for that allocator---must be for built-in
	    // class we're not modeling explicitly (e.g., Thread)
	    setResult(new edu.ksu.cis.bandera.bir.NullExpr(system));
    }
    public void caseNullConstant(NullConstant expr) {
	setResult(new edu.ksu.cis.bandera.bir.NullExpr(system));
    }
    public void caseOrExpr(OrExpr expr) {
	// We don't handle the bitwise integer AND and OR
	defaultCase(expr);
    }
    public void caseRemExpr(RemExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.RemExpr(expr1, expr2));
    }
    public void caseStaticFieldRef(StaticFieldRef expr) {
	// Mark expression as observable if field is observable
	if (predSet.isObservable(expr.getField()))
	    observable = true;
	edu.ksu.cis.bandera.bir.StateVar var = 
	    system.getVarOfKey(expr.getField());
	if (var == null)
	    throw new RuntimeException("Field not declared: " + expr);
	setResult(var);
    }
    public void caseStaticInvokeExpr(StaticInvokeExpr expr) {
	throw new RuntimeException("Unhandled static method call: " + expr);
    }
    public void caseStringConstant(StringConstant expr) {
	String s = expr.toString();
	setResult(s.substring(1,s.length() - 1));
    }
    public void caseSubExpr(SubExpr expr) {
	expr.getOp1().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr1 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	expr.getOp2().apply(this);
	edu.ksu.cis.bandera.bir.Expr expr2 =
	    (edu.ksu.cis.bandera.bir.Expr) getResult();
	setResult(new edu.ksu.cis.bandera.bir.SubExpr(expr1, expr2));	
    }
    public void caseThisRef(ThisRef thisRef) {
	String threadName = ((RefType)thisRef.getType()).className;
	SootClass sClass = method.getDeclaringClass(); 
	log.debug("referencing " + sClass.getName() + "_this");
	StateVar var = system.getVarOfKey(sClass.getName() + "_this");
	setResult(var);
    }
    public void defaultCase(Object o) {
	throw new RuntimeException("Unhandled expression type " + 
				   o.getClass().getName() + ": " + o);
    }
    public boolean isObservable() { return observable; }

    /**
     * Construct a unique key for a Jimple Local by concatenating
     * the class, method, and local name.
     * <p>
     * We need to do this rather than just use the Local object
     * as a key because the Soot designers decided that two Locals
     * would be equal() if they had the same name, even if they
     * were in different methods.
     */
    public static String localKey(SootMethod method, Local local) {
	return  method.getDeclaringClass().getName() + "."
	    + method.getName() + "." + local.getName();
    }
}
