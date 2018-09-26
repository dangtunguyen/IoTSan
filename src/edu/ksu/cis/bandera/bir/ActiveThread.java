package edu.ksu.cis.bandera.bir;

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
import ca.mcgill.sable.util.*;

import java.util.*;

import org.apache.log4j.Category;

/**
 * An ActiveThread is a running instance of a BirThread.
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:32:50 $
 */
public class ActiveThread extends BirThread implements BirConstants {

    private static Category log = Category.getInstance(ActiveThread.class.getName());
    private int tid;
    private int status;
    private Location pc;
    private Literal[] store;

    public ActiveThread(BirThread thread, int the_id) { 
	super(thread);
	tid = the_id;
	status = THREAD_ACTIVE;
	pc = startLoc;
	store = new Literal[local.size()];
	log.debug("Creating a new ActiveThread from a BirThread using tid " + tid + " and id " + id);
    }

    public ActiveThread(ActiveThread other) {
	super(other);
	tid = other.tid;
	status = other.status;
	pc = other.pc;
	store = (Literal []) other.store.clone();
	log.debug("Creating a new ActiveThread from an ActiveThread using tid " + tid);
    }

    public void initLocals(BirTypeInit init) {
	for (int i = 0; i < local.size(); i ++) {
	    StateVar var = local.elementAt(i);
	    var.getType().apply(init, new LocalVar(var, this));
	}
    }

    public void printLocals(BirTypePrint print) {
	for (int i = 0; i < local.size(); i ++) {
	    StateVar var = local.elementAt(i);
	    System.out.print("    " + name + "[" + tid + "]:"
			     + var.getName() + " = ");
	    var.getType().apply(print, new LocalVar(var, this));
	    System.out.println();
	}
    }

    public void setPC(Location pc) { this.pc = pc; }
    public Location getPC() { return pc; }
    public void setStatus(int status) { this.status = status; }
    public int getStatus() { return status; }
    public Literal[] getStore() { return store; }
    public int getTid() { return tid; }

    public Object clone() {
	return new ActiveThread(this);
    }

    public Expr getBlock(BirState state) {
	for (int i = 0; i < pc.getOutTrans().size(); i ++) {
	    Expr guard = pc.getOutTrans().elementAt(i).getGuard();
	    if (guard instanceof LockTest &&
		((LockTest) guard).getOperation() == LOCK_AVAILABLE) {
	state.setSpecificThread(this);
		guard.apply(state);
	state.setSpecificThread(null);
		BoolLit val = (BoolLit) state.getResult();
		if (val.isFalse()) {
		    Expr lock = ((LockTest) guard).getLockExpr();
		    if (lock instanceof RecordExpr) {
			Expr record = ((RecordExpr) lock).getRecord();
			if (record instanceof DerefExpr)
			    return ((DerefExpr) record).getTarget();
			else
			    return record;
		    }
		}
	    }
	}
	return null;
    }

    /**
     * Test the equality of another object to this one.  They are equal if they are the
     * same object (this == object) or if the thread IDs are equal.
     */
    /*
    public boolean equals(Object object) {
	if(object == null) {
	    return(false);
	}

	boolean equality = false;
	if(object == this) {
	    equality = true;
	}
	else if(object instanceof ActiveThread) {
	    ActiveThread activeThread = (ActiveThread)object;
	    if(tid == activeThread.getTid()) {
		equality = true;
	    }
	}
	return(equality);
    }

    public int hashCode() {
	return(tid * 31);
    }
    */

}
