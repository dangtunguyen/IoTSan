package edu.ksu.cis.bandera.abstraction;

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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import java.util.*;
public class PredicateAbstractor extends AbstractBanderaValueSwitch {
	private static Grimp grimp = Grimp.v();
	private PredicateInterpreter pi = new PredicateInterpreter();
	private TypeTable typeTable;
	private Hashtable variableSetTable;
	private TypeTable coercedTypeTable;
/**
 * 
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 * @param varSetTable java.util.Hashtable
 */
public PredicateAbstractor(TypeTable typeTable, Hashtable varSetTable) {
	this.typeTable = typeTable;
	this.variableSetTable = varSetTable;
}
/**
 * 
 * @param v ca.mcgill.sable.soot.jimple.Value
 */
public static Value abs(CoercionManager cm, TypeTable typeTable, Value v) {
	if (typeTable.size() == 0) return v;
	Hashtable varSetTable = new Hashtable();
	PredicateTypeChecker.check(cm, typeTable, v, varSetTable);
	PredicateAbstractor pa = new PredicateAbstractor(typeTable, varSetTable);
	v.apply(pa);
	return (Value) pa.getResult();
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.ComplementExpr
 */
public void caseComplementExpr(ComplementExpr expr) {
	expr.getOp().apply(this);
	expr.setOp((Value) getResult());
	setResult(expr);
}
/**
 * 
 * @param e edu.ksu.cis.bandera.jext.LocationTestExpr
 */
public void caseLocationTestExpr(LocationTestExpr e) {
	defaultCase(e);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalAndExpr
 */
public void caseLogicalAndExpr(LogicalAndExpr expr) {
	expr.getOp1().apply(this);
	expr.setOp1((Value) getResult());
	expr.getOp2().apply(this);
	expr.setOp2((Value) getResult());
	setResult(expr);
}
/**
 * 
 * @param expr edu.ksu.cis.bandera.jext.LogicalOrExpr
 */
public void caseLogicalOrExpr(LogicalOrExpr expr) {
	expr.getOp1().apply(this);
	expr.setOp1((Value) getResult());
	expr.getOp2().apply(this);
	expr.setOp2((Value) getResult());
	setResult(expr);
}
/**
 * 
 * @param v java.lang.Object
 */
public void defaultCase(Object o) {
	Value v = (Value) o;
	Abstraction a = typeTable.get(v);
	if (!((a instanceof RealAbstraction) && !(a instanceof ConcreteRealAbstraction)) && !((a instanceof IntegralAbstraction) && !(a instanceof ConcreteIntegralAbstraction))) {
		setResult(v);
		return;
	}
	Vector variables = new Vector();
	for (Iterator i = ((Set) variableSetTable.get(v)).iterator(); i.hasNext();) {
		Value var = (Value) i.next();
		if (var.getType() instanceof BaseType)
			variables.add(var);
	}
	int size = variables.size();
	int[] absNumOfTokens = new int[size];
	int numOfPossibleTokens = 1;
	for (int i = 0; i < size; i++) {
		a = typeTable.get(variables.elementAt(i));
		absNumOfTokens[i] = ((Integer) AbstractionClassLoader.invokeMethod(a.getClass().getName(), "getNumOfTokens", new Class[0], null, new Object[0])).intValue();
		numOfPossibleTokens *= absNumOfTokens[i];
	}
	int[] changes = new int[size];
	for (int i = size - 1, change = 1; i >= 0; change *= absNumOfTokens[i], i--) {
		changes[i] = change;
	}
	int tokens[] = new int[size];
	int token[] = new int[size];
	Value result = null;
	for (int i = 0; i < numOfPossibleTokens; i++) {
		for (int j = 0; j < size; j++) {
			token[j] = (i / changes[j]) % absNumOfTokens[j];
			tokens[j] = 1 << token[j];
		}
		if (pi.isTrue(typeTable, variables, tokens, v)) {
			Value temp = null;
			int k = 0; 
			for (Iterator j = variables.iterator(); j.hasNext(); k++) {
				 if (temp == null) {
					 temp = grimp.newEqExpr((Value) j.next(), IntConstant.v(token[k]));
				 } else {
					 temp = new LogicalAndExpr(temp, grimp.newEqExpr((Value) j.next(), IntConstant.v(token[k])));
				 }
			}
			if (result == null) {
				result = temp;
			} else {
				result = new LogicalOrExpr(result, temp);
			}
		}
	}
	setResult(result);
}
}
