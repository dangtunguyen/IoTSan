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

/**
 * A lock action.
 */

public class LockAction extends AbstractAction implements BirConstants {
   
	Expr lockExpr;
	int operation;
	BirThread thread;

	/**
	 * Create a lock action.
	 * @param lockExpr expression naming lock
	 * @param operation code for operation
	 * @param thread thread invoking operation
	 */
	public LockAction(Expr lockExpr, int operation, 
			   BirThread thread) {
	this.lockExpr = lockExpr;
	this.operation = operation;
	this.thread = thread;
	}
	public void apply(Switch sw)
	{
		((ExprSwitch) sw).caseLockAction(this);
	}
	public boolean canSuspend() {
	return (operation == LOCK) || (operation == WAIT);
	}
	public Expr getLockExpr() { return lockExpr; }
	public int getOperation() { return operation; }
	public BirThread getThread() { return thread; }
	public boolean isLock() {
	return operation == LOCK;
	}
	public boolean isLockAction(int operation) { 
	return (operation & this.operation) != 0;
	}
	public boolean isNotify() {
	return operation == NOTIFY;
	}
	public boolean isNotifyAll() {
	return operation == NOTIFYALL;
	}
	public boolean isUnlock() {
	return operation == UNLOCK;
	}
	public boolean isUnwait() {
	return operation == UNWAIT;
	}
	public boolean isWait() {
	return operation == WAIT;
	}
	public static int operationCode(String methodName) {
	if (methodName.equals("wait"))
	    return WAIT;
	if (methodName.equals("notify"))
	    return NOTIFY;
	if (methodName.equals("notifyAll"))
	    return NOTIFYALL;
	return INVALID;
	}
	public static String operationName(int operation) {
	switch (operation) {
	case LOCK: return "lock";
	case UNLOCK: return "unlock";
	case WAIT: return "wait";
	case UNWAIT: return "unwait";
	case NOTIFY: return "notify";
	case NOTIFYALL: return "notifyAll";
	default: return "?";
	}
	}
}
