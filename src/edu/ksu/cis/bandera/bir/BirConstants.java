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
 * Most BIR constants are defined here, including:
 * <ul>
 * <li> Type kinds
 * <li> Lock actions
 * <li> Lock tests
 * <li> Thread actions
 * <li> Thread tests
 * </ul>
 * We use power of two encoding to allow subsets to be tested for
 * (e.g., type is BOOL or RANGE = (type & (BOOL | RANGE)) != 0).
 */

public interface BirConstants {

	public static final int ANY = -1;

	// Type kinds
	public static final int LOCKTYPE = 1;   // equals LOCK (below)
	public static final int BOOL = 2;
	public static final int ENUMERATED = 4;
	public static final int RANGE = 8;
	public static final int ARRAY = 16;
	public static final int RECORD = 32;
	public static final int FIELD = 64;
	public static final int COLLECTION = 128;
	public static final int REF = 256;
        public static final int TID = 512;      // thread identifier

	// LockAction constants
	public static final int INVALID = 0;
	public static final int LOCK = 1;
	public static final int UNLOCK = 2;
	public static final int WAIT = 4;
	public static final int UNWAIT = 8;
	public static final int NOTIFY = 16;
	public static final int NOTIFYALL = 32;

	// LockTest constants
	public static final int LOCK_AVAILABLE = 1;
	public static final int HAS_LOCK = 2;
	public static final int WAS_NOTIFIED = 4;

	// ThreadAction constants
	public static final int START = 1;
	public static final int EXIT = 2;

	// ThreadTest constants
        public static final int THREAD_TERMINATED = 1;
        public static final int THREAD_READY = 0;
	public static final int THREAD_ACTIVE = 1;
        public static final int THREAD_EXITED = 2;
}



