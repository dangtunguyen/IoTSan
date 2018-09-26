package edu.ksu.cis.bandera.bir;

import org.apache.log4j.Category;

/**
 * The StateVarMarkerExprSwith provides the logic to
 * mark all StateVars in a tree as being global locals
 * as well as giving them the appropriate pid.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:32:51 $
 */
public class StateVarMarkerExprSwitch extends BaseExprSwitch {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(StateVarMarkerExprSwitch.class);

    /**
     * The pid to use for all StateVars in the evaluated expression tree.
     */
    private int pid;

    /**
     * Set the pid to use for all StateVars in the evaluated
     * expression tree.
     *
     * @param int pid
     */
    public void setPid(int pid) {
	this.pid = pid;
    }

    /**
     * Get the pid that is being used for all StateVars in the
     * evaluated expression tree.
     *
     * @return int
     */
    public int getPid() {
	return(pid);
    }

    /**
     * Handle all StateVar nodes in the expression tree.  This
     * will mark the StateVar as being a global local and assign
     * it the configured pid.
     *
     * @param StateVar var
     */
    public void caseStateVar(StateVar var) {
	log.debug("Marking StateVar " + var.toString() + " as a global local with pid " + pid +
		  ", hashCode = " + var.hashCode() + ".");
	var.markAsGlobalLocal();
	var.setGlobalLocalPid(pid);
    }
}
