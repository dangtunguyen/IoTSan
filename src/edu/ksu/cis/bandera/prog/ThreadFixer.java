package edu.ksu.cis.bandera.prog;

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
 * The ThreadFixer provides a mechanism for cleaning up the calls to
 * <ul>
 * <li>Thread.currentThread()</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:26 $
 */
public class ThreadFixer implements StmtSwitch {

    private static ThreadFixer fixer = new ThreadFixer();
    private static Jimple jimple = Jimple.v();
    private static final Category log = Category.getInstance(ThreadFixer.class);
    private static JimpleBody jimpleBody;

    /**
     * ThreadFixer constructor comment.
     */
    private ThreadFixer() {
    }

    /**
     * The ThreadFixer provides a fix for the static all to Thread.currentThread().  It
     * will replace this with a 'this' reference instead.
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

	    if(("java.lang.Thread".equals(className)) && ("currentThread".equals(methodName)) && (argCount == 0)) {
		// this method call should be replaced with a 'this' reference.
		log.debug("Found a call to Thread.currentThread.  Replacing it with a 'this' reference.");
		if(jimpleBody != null) {
		    Local thisLocal = jimpleBody.getLocal("JJJCTEMP$0");
		    if(thisLocal != null) {
			stmt.setRightOp(thisLocal);
		    }
		    else {
			log.error("There was not Local for the 'this' reference (JJJCTEMP$0) in this JimpleBody." +
				  "  Therefore, we are skipping it for this assignment statement.");
		    }
		}
		else {
		    log.warn("There was no JimpleBody from which to get the 'this' reference." +
			     "  Therefore, we are skipping it for this assignment statement.");
		}
	    } else {
		// nothing
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

    public static void fix(Stmt s, JimpleBody jimpleBody) {
	fixer.jimpleBody = jimpleBody;
	s.apply(fixer);
	fixer.jimpleBody = null;
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
	    //fix((Stmt) i.next());
	    fix((Stmt) i.next(), (JimpleBody)sm.getBody(jimple));
	}
    }
}
