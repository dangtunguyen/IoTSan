package edu.ksu.cis.bandera.birc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2003   Matthew Dwyer (dwyer@cis.ksu.edu)            *
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
import org.apache.log4j.Category;

import edu.ksu.cis.bandera.birc.ExprExtractor;
import edu.ksu.cis.bandera.birc.TypeExtractor;
import edu.ksu.cis.bandera.birc.PredicateSet;

import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.bir.LocVector;
import edu.ksu.cis.bandera.bir.Location;
import edu.ksu.cis.bandera.bir.StateVarMarkerExprSwitch;

import edu.ksu.cis.bandera.jext.ExistsThreadExpr;
import edu.ksu.cis.bandera.jext.AllThreadsExpr;
import edu.ksu.cis.bandera.jext.BanderaSpecExprSwitch;
import edu.ksu.cis.bandera.jext.LogicalAndExpr;
import edu.ksu.cis.bandera.jext.LogicalOrExpr;
import edu.ksu.cis.bandera.jext.LocationTestExpr;
import edu.ksu.cis.bandera.jext.ComplementExpr;
import edu.ksu.cis.bandera.jext.LocalExpr;

import edu.ksu.cis.bandera.sessions.SessionManager;
import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.sessions.BIROptions;

import ca.mcgill.sable.soot.jimple.Expr;
import ca.mcgill.sable.soot.jimple.Value;
import ca.mcgill.sable.soot.jimple.BinopExpr;
import ca.mcgill.sable.soot.jimple.UnopExpr;
import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.soot.jimple.LocalDefs;
import ca.mcgill.sable.soot.jimple.Local;

import ca.mcgill.sable.soot.SootMethod;

import java.util.Vector;

/**
 * A Jimple value switch that translates Jimple specification expression 
 * into BIR expression values.  The 
 *
 * @author Matt Dwyer &lt;dwyer@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:32:53 $
 */
public class SpecExprExtractor extends ExprExtractor implements BanderaSpecExprSwitch {

    /*
     * Note: The two methods should be re-implemented (refactored?) to take out all the
     * duplicate portions of code if it is possible.  Just a simple step back re-design
     * should work fine.  I just didn't take the time since I don't understand it in
     * the first place! -tcw
     */

    /*
     * Note: We can get rid of the StateVarMarkerExprSwitch now that we handle the LocalExpr
     * nodes locally.  We should just use a global int that represents the global-local pid.
     * Using that field we should set the StateVar to use that pid and the flag that tells
     * later components it is a global-local. -tcw
     *
     * Contd: I have modified this class to handle it so it is no longer calling
     * StateVarMarkerExprSwitch but I did not remove that class.  There shouldn't be
     * anything else that calls it so it could be removed if need be.  Might want to keep
     * it around as an example of how to walk the AST though (and as an example of
     * implementing a default base class). -tcw
     */

    /**
     * The globalLocalPid is used when we are setting a StateVar as being a global local
     * so that the predicate is evaluated correctly.  This will be set before we start to
     * visit an expression and will be reset after visiting it.  Valid values for this
     * are 0..MAX_INT.  -1 signifies the default value, or no pid is set.
     */
    private int globalLocalPid;

    private static Category log = Category.getInstance(SpecExprExtractor.class);

    /**
     * The maximum thread ID that will be used in generating the conjunction or disjunction.
     */
    private int maxThreadID;
    // we might want to replace this with a list or map to make it more flexible! -tcw

    boolean isAllThreads = false;
    
    public SpecExprExtractor(TransSystem system, Stmt stmt, LocalDefs localDefs,
			     SootMethod method, TypeExtractor typeExtract,
			     PredicateSet predSet) {

	super(system, stmt, localDefs, method, typeExtract, predSet);

	// init the maxThreadID variable for use in generating the predicate statement.	
	SessionManager sm = SessionManager.getInstance();
	Session s = sm.getActiveSession();
	if(s == null) {
	    // error, no active session
	    log.error("There is no active session upon which to base the maxThreadID.");
	    return;
	}
	BIROptions bo = s.getBIROptions();
	if(bo == null) {
	    // error, no birOptions
	    log.error("There are no BIROptions in the active session upon which to base the maxThreadID.");
	    return;
	}
	ResourceBounds rb = bo.getResourceBounds();
	if(rb == null) {
	    // error, no resource bounds
	    log.error("There are no ResourceBounds in the active session upon which to base the maxThreadID.");
	    return;
	}
	maxThreadID = rb.getDefaultThreadMax();
	log.debug("The maximum thread id for this system is " + maxThreadID);

    }

    /**
     * The caseExistsThreadExpr handles the conversion of the ExistsThreadExpr
     * into a conjuction or disjunction of tests based upon the operation
     * in the ExistsThreadExpr.
     *
     * <ul>
     * <li>loc => (pid-1[loc(l)] || ... || pid-N[loc(l)]) </li>
     * <li>!loc => (!pid-1[loc(l)] || ... || !pid-N[loc(l)]) </li>
     * <li>loc && e  => (pid-1[loc(l)] && e) || ... || (pid-N[loc(l)] && e) </li>
     * <li>loc || e  => (pid-1[loc(l)] || e) || ... || (pid-N[loc(l)] || e) </li>
     * </ul>
     *
     * @param ExistsThreadExpr expr
     */
    public void caseExistsThreadExpr(ExistsThreadExpr expr) {

	if(expr == null) {
	    log.error("The ExistsThreadExpr given is null.  Cannot translate!");
	    return;
	}

	Expr e = (Expr)expr.getOp();
	if(e == null) {
	    log.error("Not sure what to do with a null expression.");
	    return;
	}
	else if(e instanceof LocationTestExpr) {

	    /*
	     * Example that finds this case: TestPredicates:Property[1-2]
	     */

	    //log.debug("Found a location expression in an Exists thread expression.");
	    log.debug("Exists: loc");

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {
		
		Vector stmts = ((LocationTestExpr)e).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			if(predicateExpr == null) {
			    predicateExpr = pidLocTest;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.OrExpr(predicateExpr, pidLocTest);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	else if((e instanceof ComplementExpr) &&
		(((ComplementExpr)e).getOp() != null) && (((ComplementExpr)e).getOp() instanceof LocationTestExpr)) {

	    //log.debug("Found a !location expression in an Exists thread expression.");
	    log.debug("Exists: !loc");

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {

		Vector stmts = ((LocationTestExpr)((ComplementExpr)e).getOp()).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			edu.ksu.cis.bandera.bir.Expr newExpr =
			    new edu.ksu.cis.bandera.bir.NotExpr(pidLocTest);
			if(predicateExpr == null) {
			    predicateExpr = newExpr;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.OrExpr(predicateExpr, newExpr);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	else if((e instanceof LogicalAndExpr) &&
		(((LogicalAndExpr)e).getOp1() != null) && (((LogicalAndExpr)e).getOp1() instanceof LocationTestExpr) &&
		(((LogicalAndExpr)e).getOp2() != null)) {

	    /*
	     * Example that finds this case: NoTakeWhileEmpty, TestPredicates:Property[1-8]
	     */

	    //log.debug("Found a logical and expression with the first operand being a location test expression in an Exists thread expression.");
	    log.debug("Exists: loc && e");

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {

		// modify all the StateVars in the op2Expr so that they are marked as global locals
		//  and have the pid to match the current threadID, i in this case.
		//StateVarMarkerExprSwitch svmes = new StateVarMarkerExprSwitch();
		//svmes.setPid(i);
		globalLocalPid = i;
		((LogicalAndExpr)e).getOp2().apply(this);
		edu.ksu.cis.bandera.bir.Expr op2Expr = (edu.ksu.cis.bandera.bir.Expr)getResult();
		//op2Expr.apply(svmes);

		Vector stmts = ((LocationTestExpr)((LogicalAndExpr)e).getOp1()).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			edu.ksu.cis.bandera.bir.Expr newExpr =
			    new edu.ksu.cis.bandera.bir.AndExpr(pidLocTest, op2Expr);
			if(predicateExpr == null) {
			    predicateExpr = newExpr;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.OrExpr(predicateExpr, newExpr);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	else if((e instanceof LogicalOrExpr) &&
		(((LogicalOrExpr)e).getOp1() != null) && (((LogicalOrExpr)e).getOp1() instanceof LocationTestExpr) &&
		(((LogicalOrExpr)e).getOp2() != null)) {

	    //log.debug("Found a logical and expression with the first operand being a location test expression in an Exists thread expression.");
	    log.debug("Exists: loc || e");

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {
		
		// modify all the StateVars in the op2Expr so that they are marked as global locals
		//  and have the pid to match the current threadID, i in this case.
		//StateVarMarkerExprSwitch svmes = new StateVarMarkerExprSwitch();
		//svmes.setPid(i);
		globalLocalPid = i;
		((LogicalOrExpr)e).getOp2().apply(this);
		edu.ksu.cis.bandera.bir.Expr op2Expr = (edu.ksu.cis.bandera.bir.Expr)getResult();
		//op2Expr.apply(svmes);

		Vector stmts = ((LocationTestExpr)((LogicalOrExpr)e).getOp1()).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			edu.ksu.cis.bandera.bir.Expr newExpr =
			    new edu.ksu.cis.bandera.bir.OrExpr(pidLocTest, op2Expr);
			if(predicateExpr == null) {
			    predicateExpr = newExpr;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.OrExpr(predicateExpr, newExpr);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	else {
	    StringBuffer sb = new StringBuffer();
	    sb.append("Not sure how to handle an ExistsThreadExpr with an expression has with type of " + e.getClass().getName());

	    if(e instanceof BinopExpr) {
		Value op1 = ((BinopExpr)e).getOp1();
		if(op1 == null) {
		    sb.append(", op1 is null");
		}
		else {
		    sb.append(", op1 type = " + op1.getClass().getName());
		}
		Value op2 = ((BinopExpr)e).getOp2();
		if(op2 == null) {
		    sb.append(", op2 is null");
		}
		else {
		    sb.append(", op2 type = " + op2.getClass().getName());
		}
	    }
	    else if(e instanceof UnopExpr) {
		Value op = ((UnopExpr)e).getOp();
		if(op == null) {
		    sb.append(", op is null");
		}
		else {
		    sb.append(", op type = " + op.getClass().getName());
		}
	    }
	    sb.append(".");
	    log.error(sb.toString());

	    return;
	}

	//could build a specialized extractor for exists

    }

    /**
     * The caseAllThreadsExpr handles the conversion of the AllThreadsExpr
     * into a conjuction or disjunction of tests based upon the operation
     * in the AllThreadsExpr.
     *
     * <ul>
     * <li>loc       => (pid-1[loc(l)] && ... && pid-N[loc(l)]) </li>
     * <li>!loc      => (!pid-1[loc(l)] && ... && !pid-N[loc(l)]) </li>
     * <li>loc && e  => (pid-1[loc(l)] && e) && ... && (pid-N[loc(l)] && e) </li>
     * <li>loc || e  => (pid-1[loc(l)] || e) && ... && (pid-N[loc(l)] || e) </li>
     * <li>!loc || e  => (!pid-1[loc(l)] || e) && ... && (!pid-N[loc(l)] || e) </li>
     * </ul>
     *
     * @param AllThreadsExpr expr
     */
    public void caseAllThreadsExpr(AllThreadsExpr expr) {

	if(expr == null) {
	    log.error("The AllThreadsExpr given is null.  Cannot translate!");
	    return;
	}

	Expr e = (Expr)expr.getOp();
	if(e == null) {
	    log.error("Not sure what to do with a null expression.");
	    return;
	}
	// loc
	else if(e instanceof LocationTestExpr) {
	    //log.debug("Found a location expression in a Forall threads expression.");
	    log.debug("Forall: loc");

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {
		
		Vector stmts = ((LocationTestExpr)e).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			if(predicateExpr == null) {
			    predicateExpr = pidLocTest;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.AndExpr(predicateExpr, pidLocTest);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	// !loc
	else if((e instanceof ComplementExpr) &&
		(((ComplementExpr)e).getOp() != null) && (((ComplementExpr)e).getOp() instanceof LocationTestExpr)) {

	    //log.debug("Found a !location expression in a Forall threads expression.");
	    log.debug("Forall: !loc");

	    /*
	     * Example that finds this case: Stage1Response, TestPredicates:Property[3-4]
	     */

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {

		Vector stmts = ((LocationTestExpr)((ComplementExpr)e).getOp()).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			edu.ksu.cis.bandera.bir.Expr newExpr =
			    new edu.ksu.cis.bandera.bir.NotExpr(pidLocTest);
			if(predicateExpr == null) {
			    predicateExpr = newExpr;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.AndExpr(predicateExpr, newExpr);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	// loc && e
	else if((e instanceof LogicalAndExpr) &&
		(((LogicalAndExpr)e).getOp1() != null) && (((LogicalAndExpr)e).getOp1() instanceof LocationTestExpr) &&
		(((LogicalAndExpr)e).getOp2() != null)) {

	    //log.debug("Found a logical and expression with the first operand being a location test expression in a Forall threads expression.");
	    log.debug("Forall: loc && e");

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {

		// modify all the StateVars in the op2Expr so that they are marked as global locals
		//  and have the pid to match the current threadID, i in this case.
		//StateVarMarkerExprSwitch svmes = new StateVarMarkerExprSwitch();
		//svmes.setPid(i);
		globalLocalPid = i;
		((LogicalAndExpr)e).getOp2().apply(this);
		edu.ksu.cis.bandera.bir.Expr op2Expr = (edu.ksu.cis.bandera.bir.Expr)getResult();
		//op2Expr.apply(svmes);

		Vector stmts = ((LocationTestExpr)((LogicalAndExpr)e).getOp1()).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			edu.ksu.cis.bandera.bir.Expr newExpr =
			    new edu.ksu.cis.bandera.bir.AndExpr(pidLocTest, op2Expr);
			if(predicateExpr == null) {
			    predicateExpr = newExpr;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.AndExpr(predicateExpr, newExpr);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	// loc || e
	else if((e instanceof LogicalOrExpr) &&
		(((LogicalOrExpr)e).getOp1() != null) && (((LogicalOrExpr)e).getOp1() instanceof LocationTestExpr) &&
		(((LogicalOrExpr)e).getOp2() != null)) {

	    //log.debug("Found a logical and expression with the first operand being a location test expression in a Forall threads expression.");
	    log.debug("Forall: loc || e");

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {
		
		// modify all the StateVars in the op2Expr so that they are marked as global locals
		//  and have the pid to match the current threadID, i in this case.
		//StateVarMarkerExprSwitch svmes = new StateVarMarkerExprSwitch();
		//svmes.setPid(i);
		globalLocalPid = i;
		((LogicalOrExpr)e).getOp2().apply(this);
		edu.ksu.cis.bandera.bir.Expr op2Expr = (edu.ksu.cis.bandera.bir.Expr)getResult();
		//op2Expr.apply(svmes);

		Vector stmts = ((LocationTestExpr)((LogicalOrExpr)e).getOp1()).getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			edu.ksu.cis.bandera.bir.Expr newExpr =
			    new edu.ksu.cis.bandera.bir.OrExpr(pidLocTest, op2Expr);
			if(predicateExpr == null) {
			    predicateExpr = newExpr;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.AndExpr(predicateExpr, newExpr);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	// !loc || e
	else if((e instanceof LogicalOrExpr) &&
		(((LogicalOrExpr)e).getOp1() != null) && (((LogicalOrExpr)e).getOp1() instanceof ComplementExpr) &&
		(((ComplementExpr)((LogicalOrExpr)e).getOp1()).getOp() != null) &&
		(((ComplementExpr)((LogicalOrExpr)e).getOp1()).getOp() instanceof LocationTestExpr) &&
		(((LogicalOrExpr)e).getOp2() != null)) {

	    //log.debug("Found a logical and expression with the first operand being a !location test expression in a Forall threads expression.");
	    log.debug("Forall: !loc || e");

	    LogicalOrExpr logicalOrExpr = (LogicalOrExpr)e;
	    ComplementExpr complementExpr = (ComplementExpr)logicalOrExpr.getOp1();
	    LocationTestExpr locationTestExpr = (LocationTestExpr)complementExpr.getOp();

	    // build a new or expression
	    edu.ksu.cis.bandera.bir.Expr predicateExpr = null;
	    for(int i = 0; i < maxThreadID; i++) {
		
		// modify all the StateVars in the op2Expr so that they are marked as global locals
		//  and have the pid to match the current threadID, i in this case.
		//StateVarMarkerExprSwitch svmes = new StateVarMarkerExprSwitch();
		//svmes.setPid(i);
		globalLocalPid = i;
		logicalOrExpr.getOp2().apply(this);
		edu.ksu.cis.bandera.bir.Expr op2Expr = (edu.ksu.cis.bandera.bir.Expr)getResult();
		//op2Expr.apply(svmes);

		//Vector stmts = ((LocationTestExpr)((LogicalOrExpr)e).getOp1()).getStmts();
		Vector stmts = locationTestExpr.getStmts();
		for(int j = 0; j < stmts.size(); j++) {

		    Stmt stmt = (Stmt)stmts.elementAt(j);
		    LocVector locs = predSet.getPredicateLocations(stmt);
		    for(int k = 0; k < locs.size(); k++) {
			Location loc = locs.elementAt(k);
		
			// build up the replacement expression
			edu.ksu.cis.bandera.bir.Expr pidLocTest =
			    new edu.ksu.cis.bandera.bir.ThreadLocTest(loc, new edu.ksu.cis.bandera.bir.IntLit(i));
			edu.ksu.cis.bandera.bir.Expr notExpr =
			    new edu.ksu.cis.bandera.bir.NotExpr(pidLocTest);
			edu.ksu.cis.bandera.bir.Expr newExpr =
			    new edu.ksu.cis.bandera.bir.OrExpr(notExpr, op2Expr);
			if(predicateExpr == null) {
			    predicateExpr = newExpr;
			}
			else {
			    predicateExpr = new edu.ksu.cis.bandera.bir.AndExpr(predicateExpr, newExpr);
			}
		    }
		}
	    }
	    setResult(predicateExpr);

	    return;
	}
	else {
	    StringBuffer sb = new StringBuffer();
	    sb.append("Not sure how to handle an AllThreadsExpr with an expression with a type of " + e.getClass().getName());

	    if(e instanceof BinopExpr) {
		Value op1 = ((BinopExpr)e).getOp1();
		if(op1 == null) {
		    sb.append(", op1 is null");
		}
		else {
		    sb.append(", op1 type = " + op1.getClass().getName());
		}
		Value op2 = ((BinopExpr)e).getOp2();
		if(op2 == null) {
		    sb.append(", op2 is null");
		}
		else {
		    sb.append(", op2 type = " + op2.getClass().getName());
		}
	    }
	    else if(e instanceof UnopExpr) {
		Value op = ((UnopExpr)e).getOp();
		if(op == null) {
		    sb.append(", op is null");
		}
		else {
		    sb.append(", op type = " + op.getClass().getName());
		}
	    }
	    sb.append(".");
	    log.error(sb.toString());

	    return;
	}

	//could build a specialized extractor for all
    }

    /**
     * If a LocalExpr is found, we will create a new StateVar that can be
     * used to set a global local pid.
     *
     * @param LocalExpr expr
     * @post A result will be set that is a StateVar or an exception thrown.
     */
    public void caseLocalExpr(LocalExpr expr) { 
	SootMethod method = expr.getMethod();
	Local local = expr.getLocal();
	String key = ExprExtractor.localKey(method,local);
	edu.ksu.cis.bandera.bir.StateVar var = system.getVarOfKey(key);
	if (var == null) {
	    throw new RuntimeException("Variable not declared: " + expr);
	}
	var.markAsGlobalLocal();
	var.setGlobalLocalPid(globalLocalPid);
	edu.ksu.cis.bandera.bir.StateVar newVar =
	    new edu.ksu.cis.bandera.bir.StateVar(
						 var.getName(),
						 var.getThread(),
						 var.getType(),
						 var.getInitVal(),
						 var.getSystem());
	newVar.markAsGlobalLocal();
	newVar.setGlobalLocalPid(globalLocalPid);
	setResult(newVar);
    }
}
