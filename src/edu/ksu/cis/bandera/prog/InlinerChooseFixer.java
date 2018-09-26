package edu.ksu.cis.bandera.prog;

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
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.abstraction.*;
import org.apache.log4j.Category;


/**
 * The InlinerChooseFixer provides a quick and easy way to fix some
 * statements in the AST including:
 * <ul>
 * <li>edu.ksu.cis.bandera.abstraction.Abstraction</li>
 * <li>Bandera.choose()</li>
 * <li>Bandera.randomBool()</li>
 * <li>Bandera.randomInt(int) where int is a constant</li>
 * </ul>
 * It also reports exceptions for calls including:
 * <ul>
 * <li>Bandera.randomClass()</li>
 * <li>Bandera.randomReachable()</li>
 * <li>Any call on Bandera that ends with Extern</li>
 * </ul>
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:33:26 $
 */
public class InlinerChooseFixer implements StmtSwitch {

    private static InlinerChooseFixer fixer = new InlinerChooseFixer();
    private static Jimple jimple = Jimple.v();
    private static final Category log = Category.getInstance(InlinerChooseFixer.class);


    /**
     * AbstractionChooseFixer constructor comment.
     */
    private InlinerChooseFixer() {
    }

    /**
     * When an assignment statement is found, check the right side operand to see if we
     * should replace it.  This will work for all cases of calls to randomInt because of
     * how Jimple works ... these calls are always assigned to a temp before manipulating
     * them.
     *
     * @param AssignStmt stmt The current assignment statement to fix.
     */
    public void caseAssignStmt(AssignStmt stmt) {
	Value rightOp = stmt.getRightOp();
	if (rightOp instanceof StaticInvokeExpr) {
	    StaticInvokeExpr sie = (StaticInvokeExpr) rightOp;
	    SootMethod sm = sie.getMethod();
	    String className = sm.getDeclaringClass().getName();
	    String methodName = sm.getName().trim();
	    int argCount = sie.getArgCount();

	    if ("edu.ksu.cis.bandera.abstraction.Abstraction".equals(className)
		&& "choose".equals(methodName)) {
		/*
		 * This logic could be embedded into the Factory. -tcw
		 */
		Vector args = new Vector();
		if (argCount == 0) {
		    args.add(IntConstant.v(0));
		    args.add(IntConstant.v(1));
		} else if (argCount == 1) {
		    int constant = ((IntConstant) sie.getArg(0)).value;
		    if (constant == 0) {
			System.out.println("*** Error: Choose with zero value ***");
			return;
		    }
		    int val = 0;
		    do {
			if ((constant & 1) == 1) {
			    args.add(IntConstant.v(val));
			}
			val++;
			constant >>= 1;
		    } while (constant != 0);
		} else {
		    throw new RuntimeException("AbstractionChooseFixer class is not updated!!!");
		}
		Value chooseExpr = new ChooseExpr(args);
		stmt.setRightOp(chooseExpr);
		if (Inline.typeTable.size() > 0)
		    Inline.typeTable.put(chooseExpr, ConcreteIntegralAbstraction.v());
	    } else if(("Bandera".equals(className)) && (methodName.indexOf("randomInt") >= 0) &&
		      (argCount == 1) && (sie.getArg(0) instanceof IntConstant)) {

		log.debug("Found a call to " + className + "." + methodName + " that should be fixed.");
		IntConstant intConstant = (IntConstant)sie.getArg(0);
		List argList = new ArrayList();
		argList.add(intConstant);
		ChooseExpr chooseExpr = ChooseExprFactory.getChooseExpr(className, methodName, argList);
		stmt.setRightOp(chooseExpr);
		if (Inline.typeTable.size() > 0) {
		    Inline.typeTable.put(chooseExpr, ConcreteIntegralAbstraction.v());
		}
	    } else if(("Bandera".equals(className)) && (methodName.indexOf("randomBool") >= 0) && (argCount == 0)) {
		log.debug("Found a call to " + className + "." + methodName + " that should be fixed.");
		ChooseExpr chooseExpr = ChooseExprFactory.getChooseExpr(className, methodName, new ArrayList());
		stmt.setRightOp(chooseExpr);	
		if (Inline.typeTable.size() > 0) {
		    Inline.typeTable.put(chooseExpr, ConcreteIntegralAbstraction.v());
		}
	    } else if(("Bandera".equals(className)) && (methodName.indexOf("choose") >= 0) && (argCount == 0)) {
		log.debug("Found a call to " + className + "." + methodName + " that should be fixed.");
		ChooseExpr chooseExpr = ChooseExprFactory.getChooseExpr(className, methodName, new ArrayList());
		stmt.setRightOp(chooseExpr);	
		if (Inline.typeTable.size() > 0) {
		    Inline.typeTable.put(chooseExpr, ConcreteIntegralAbstraction.v());
		}
		/*
	        Vector args = new Vector(2);
		args.add(IntConstant.v(0));
		args.add(IntConstant.v(1));
		Value chooseExpr = new ChooseExpr(args);
		stmt.setRightOp(chooseExpr);
		if (Inline.typeTable.size() > 0) {
		    Inline.typeTable.put(chooseExpr, ConcreteIntegralAbstraction.v());
		}
		*/
	    } else if(("Bandera".equals(className)) && 
		      ((methodName.equals("randomClass")) || (methodName.equals("randomReachable")) ||
		       ((methodName.endsWith("Extern"))))) {
		throw new RuntimeException("Cannot handle the call to " + className + "." + methodName + ".");
		/*
	    } else if(("java.lang.Thread".equals(className)) && ("currentThread".equals(methodName)) && (argCount == 0)) {
		// this method call should be replaced with a 'this' reference.
		log.debug("Found a call to Thread.currentThread.  Replacing it with a 'this' reference.");
		SootClass sootClass = sm.getDeclaringClass();
		ThisRef thisReference = Jimple.v().newThisRef(sootClass);
		stmt.setRightOp(thisReference);
		*/
	    } else {
		// nothing
	    }
	}
	else if(rightOp instanceof VirtualInvokeExpr) {
	    VirtualInvokeExpr vie = (VirtualInvokeExpr)rightOp;
	    SootMethod sm = vie.getMethod();
	    String className = sm.getDeclaringClass().getName();
	    String methodName = sm.getName().trim();
	    int argCount = vie.getArgCount();

	    List argList = null;
	    if("java.util.Random".equals(className) && ("nextInt".equals(methodName)) &&
		    (argCount == 1) && (vie.getArg(0) instanceof IntConstant)) {
		log.debug("Found a call to " + className + "." + methodName + " that should be fixed.");
		argList = new ArrayList(1);
		argList.add(vie.getArg(0));
	    }
	    else if(("java.util.Random".equals(className)) && ("nextBoolean".indexOf(methodName) >= 0) && (argCount == 0)) {
		log.debug("Found a call to " + className + "." + methodName + " that should be fixed.");
		argList = new ArrayList(0);
	    }
	    else {
	    }

	    if(argList != null) {
		ChooseExpr chooseExpr = ChooseExprFactory.getChooseExpr(className, methodName, argList);
		if(chooseExpr != null) {
		    stmt.setRightOp(chooseExpr);
		    if(Inline.typeTable.size() > 0) {
			Inline.typeTable.put(chooseExpr, ConcreteIntegralAbstraction.v());
		    }
		}
		else {
		    log.warn("The ChooseExprFactory returned a null when given class " + className + ", method " + methodName + ".");
		}
	    }
	    
	}
	else {
	}
    }

    /**
     * caseBreakpointStmt method comment.
     */
    public void caseBreakpointStmt(BreakpointStmt stmt) {}
    /**
     * caseEnterMonitorStmt method comment.
     */
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {}
    /**
     * caseExitMonitorStmt method comment.
     */
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {}
    /**
     * caseGotoStmt method comment.
     */
    public void caseGotoStmt(GotoStmt stmt) {}
    /**
     * caseIdentityStmt method comment.
     */
    public void caseIdentityStmt(IdentityStmt stmt) {}
    /**
     * caseIfStmt method comment.
     */
    public void caseIfStmt(IfStmt stmt) {}
    /**
     * caseInvokeStmt method comment.
     */
    public void caseInvokeStmt(InvokeStmt stmt) {}
    /**
     * caseLookupSwitchStmt method comment.
     */
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {}
    /**
     * caseNopStmt method comment.
     */
    public void caseNopStmt(NopStmt stmt) {}
    /**
     * caseRetStmt method comment.
     */
    public void caseRetStmt(RetStmt stmt) {}
    /**
     * caseReturnStmt method comment.
     */
    public void caseReturnStmt(ReturnStmt stmt) {}
    /**
     * caseReturnVoidStmt method comment.
     */
    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {}
    /**
     * caseTableSwitchStmt method comment.
     */
    public void caseTableSwitchStmt(TableSwitchStmt stmt) {}
    /**
     * caseThrowStmt method comment.
     */
    public void caseThrowStmt(ThrowStmt stmt) {}
    /**
     * defaultCase method comment.
     */
    public void defaultCase(Object obj) {}
    /**
     * 
     * @param s ca.mcgill.sable.soot.jimple.Stmt
     */
    public static void fix(Stmt s) {
	s.apply(fixer);
    }
    /**
     * 
     * @param sc ca.mcgill.sable.soot.SootClass
     */
    public static void fix(SootClass sc) {
	for (Iterator i = sc.getMethods().iterator(); i.hasNext(); ) {
	    fix((SootMethod) i.next());
	}
    }
    /**
     * 
     * @param sm ca.mcgill.sable.soot.SootMethod
     */
    public static void fix(SootMethod sm) {
	for (Iterator i = ((JimpleBody) sm.getBody(jimple)).getStmtList().iterator(); i.hasNext();) {
	    fix((Stmt) i.next());
	}
    }
}
