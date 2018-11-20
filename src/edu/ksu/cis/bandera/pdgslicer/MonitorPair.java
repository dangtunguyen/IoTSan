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

import edu.ksu.cis.bandera.annotation.*;

import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import java.util.Enumeration;

/**
 * This class is for information of monitor pair, i.e., 
 * <code>entermonitor</code> and <code>exitmonitor</code> in 
 * Jimple.
 */
public class MonitorPair
{
  /**
   * The lock of this monitor pair.
   */
  private Value lock;
  private EnterMonitorStmt enterMonitor;
  /**
   * A list of {@link ExitMonitorStmt ExitMonitorStmt}.
   */
  private List exitMonitors;
  private Stmt endSynchroStmt;
  private Stmt exitMonitorInException;
  private Stmt beginSynchroStmt;
  private Annotation synchroBodyAnn;
  private Annotation catchAnn;

  //this MonitorPair implies that EnterMonitorStmt is synchronize
  //dependent on its corresponding ExitMonitorStmt
  //namely, they must appear pairly

  public Stmt getBeginSynchroStmt()
	{
	  return beginSynchroStmt;
	}
  public Annotation getCatchAnn()
	{
	  return catchAnn;
	}
  public Stmt getEndSynchroStmt()
	{
	  return endSynchroStmt;
	}
  public EnterMonitorStmt getEnterMonitor()
	{
	  return enterMonitor;
	}
  public Stmt getExitMonitorInException()
	{
	  return exitMonitorInException;
	}
  public List getExitMonitors()
	{
	  return exitMonitors;
	}
  public Value getLock()
	{
	  return lock;
	}
  public Annotation getSynchroBodyAnn()
	{
	  return synchroBodyAnn;
	}
  public void setBeginSynchroStmt(Stmt s)
	{
	  beginSynchroStmt = s;
	}
  public void setCatchAnn(Annotation catAnn)
	{
	  catchAnn = catAnn;
	}
  public void setEndSynchroStmt(Stmt s)
	{
	  endSynchroStmt = s;
	}
  public void setEnterMonitor(EnterMonitorStmt s)
	{
	  enterMonitor = s;
	}
  public void setExitMonitorInException(Stmt s)
	{
	  exitMonitorInException = s;
	}
  public void setExitMonitors(Enumeration s)
	{
	  exitMonitors = new ArrayList();

	  while (s.hasMoreElements())
	exitMonitors.add((Stmt) s.nextElement());
	}
  public void setLock(Value v)
	{
	  lock = v;
	}
  public void setSynchroBodyAnn(Annotation synAnn)
	{
	  synchroBodyAnn = synAnn;
	}
  public String toString()
	{
	  return "[ lock:" + lock + " enter:" + enterMonitor + " exit: " + exitMonitors + " ] " + " endSynchroStmt: " + endSynchroStmt + " exitMonitorInException: " + exitMonitorInException;
	}
}
