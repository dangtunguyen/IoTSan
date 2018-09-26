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
import java.util.*;

import edu.ksu.cis.bandera.jext.*;

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

import org.apache.log4j.Category;

/**
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:32:53 $
 */
public class ObservableExtractor extends AbstractBanderaValueSwitch implements BanderaSpecExprSwitch {

    private static final Category log = Category.getInstance(ObservableExtractor.class);

	Hashtable observableSet;

	public ObservableExtractor(Hashtable observableSet) {
	this.observableSet = observableSet;
	}

    /*
     * The following two methods (caseAllThreadsExpr and caseExistsThreadExpr) were added to
     * get rid of an exception.  This is just a HACK and should be fixed by a knowledgable person. -tcw
     */
    public void caseAllThreadsExpr(AllThreadsExpr expr) {
	log.debug("Handling AllThreadsExpr!");
	expr.getOp().apply(this);
    }
    public void caseExistsThreadExpr(ExistsThreadExpr expr) {
	log.debug("Handling ExistsThreadExpr!");
	expr.getOp().apply(this);
    }
    /* END HACK */

	public void caseAddExpr(AddExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseArrayRef(ArrayRef expr) {
	expr.getBase().apply(this);
	expr.getIndex().apply(this);
	}
	public void caseCastExpr(CastExpr expr) {
	expr.getOp().apply(this);
	}
	public void caseChooseExpr(ChooseExpr expr) {
	}
	public void caseComplementExpr(ComplementExpr expr) {
	expr.getOp().apply(this);
	}
	public void caseDivExpr(DivExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseEqExpr(EqExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseGeExpr(GeExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseGtExpr(GtExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseInExpr(InExpr expr) {
	expr.getOp1().apply(this);
	}
	public void caseInstanceFieldRef(InstanceFieldRef expr) {
	expr.getBase().apply(this);
	SootField field = expr.getField();
	observableSet.put(field,field);
	}
	public void caseInstanceOfExpr(InstanceOfExpr expr) {
	expr.getOp().apply(this);
	}
	public void caseIntConstant(IntConstant expr) {
	}
	public void caseLeExpr(LeExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseLengthExpr(LengthExpr expr) {
	expr.getOp().apply(this);
	}
	public void caseLocalExpr(LocalExpr expr) { 
	SootMethod method = expr.getMethod();
	Local local = expr.getLocal();
	String key = ExprExtractor.localKey(method,local);
	observableSet.put(key,key);
	}
	public void caseLocationTestExpr(LocationTestExpr expr) {
	Vector stmts = expr.getStmts();
	for (int i = 0; i < stmts.size(); i++) 
	    observableSet.put(stmts.elementAt(i),stmts);
	}
	public void caseLogicalAndExpr(LogicalAndExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseLogicalOrExpr(LogicalOrExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseLtExpr(LtExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseMulExpr(MulExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseNeExpr(NeExpr expr)
	{
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseNegExpr(NegExpr expr) {
	expr.getOp().apply(this);
	}
	public void caseNewArrayExpr(NewArrayExpr expr) {
	}
	public void caseNewExpr(NewExpr expr) {
	}
	public void caseNullConstant(NullConstant expr) {
	}
	public void caseRemExpr(RemExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void caseStaticFieldRef(StaticFieldRef expr) {
	SootField field = expr.getField();
	observableSet.put(field,field);
	}
	public void caseStaticInvokeExpr(StaticInvokeExpr expr) {
	throw new RuntimeException("Unhandled static method call: " + expr);
	}
	public void caseStringConstant(StringConstant expr) {
	}
	public void caseSubExpr(SubExpr expr) {
	expr.getOp1().apply(this);
	expr.getOp2().apply(this);
	}
	public void defaultCase(Object o) {
	throw new RuntimeException("Unhandled expression type: " + o);
	}
}
