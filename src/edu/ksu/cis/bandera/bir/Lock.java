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

import java.io.*;
import java.util.*;

/**
 * A lock type.
 */

public class Lock extends Type implements BirConstants {

	boolean waiting;
	boolean reentrant;

	/**
	 * Construct a lock type.
	 * @param waiting does the lock support wait/notify?
	 * @param reentrant can the lock be reacquired without blocking?
	 */

	public Lock(boolean waiting, boolean reentrant) {
	this.waiting = waiting;
	this.reentrant = reentrant;
	}
	public void apply(TypeSwitch sw, Object o)
	{
		sw.caseLock(this, o);
	}
	public boolean isKind(int kind) { 
	return (kind & LOCKTYPE) != 0;
	}
	public boolean isReentrant() { return reentrant; }
	public boolean isWaiting() { return waiting; }
	public void setWaiting(boolean waiting) { this.waiting = waiting; }
	public String toString() {
	String s = "lock";
	if (waiting)
	    s += " wait";
	if (reentrant)
	    s += " reentrant";
	return s;
	}
}