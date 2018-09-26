package edu.ksu.cis.bandera.pdgslicer;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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

import ca.mcgill.sable.soot.jimple.*;

/**
 * This class is for information of synchronization statement in Jimple
 * like <code>wait</code> and <code>notify</code>.
 */
public class SynchroStmt
{
  /**
   * The lock with which <code>wait</code> or <code>notify</code>
   * is associtated.
   */
  private Value lock;
  /**
   * The statement of <code>wait</code> or <code>notify</code>.
   */
  private Stmt waitNotify;
  /**
   * The kind of the statement: <code>notify</code>,
   * <code>notifyAll</code>, or <code>wait</code>.
   */
  private String stmtKind;

  /**
   * @param s <code>wait</code> or <code>notify</code> statement.
   * @param kind kind of the statement.
   */
  public SynchroStmt(Stmt s, String kind)
	{
	  waitNotify = s;
	  stmtKind = kind;
	}
  /**
   * Get the lock.
   * <p>
   * @return {@link #lock lock}.
   */
  public Value getLock()
	{
	  return lock;
	}
  /**
   * Get the statement.
   * <p>
   * @return {@link #waitNotify waitNotify}.
   */
  public Stmt getWaitNotify()
	{
	  return waitNotify;
	}
  /**
   * See if the statement is <code>notify</code> statement.
   * <p>
   * @return <code>true</code> if {@link #stmtKind stmtKind} is 
   * <code>notify</code> or <code>notifyAll</code>; 
   * <code>false</code> otherwise.
   */ 
  public boolean isNotifyStmt()
	{
	  return (stmtKind.equals("notify") || stmtKind.equals("notifyAll"));
	}
  /**
   * See if the statement is <code>wait</code> statement.
   * <p>
   * @return <code>true</code> if {@link #stmtKind stmtKind} is
   * <code>wait</code>; <code>false</code> otherwise.
   */
  public boolean isWaitStmt()
	{
	  return stmtKind.equals("wait");
	}
  /**
   * Set a value for {@link #lock lock}.
   * <p>
   * @param v the value.
   */
  public void setLock(Value v)
	{
	  lock = v;
	}
  public String toString()
	{
	  return "[ lock: " + lock + " wnStmt: " + waitNotify + " ]";
	}
}
