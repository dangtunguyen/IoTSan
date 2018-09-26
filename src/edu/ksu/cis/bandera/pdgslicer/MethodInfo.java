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


//import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;
import java.util.BitSet;
import ca.mcgill.sable.soot.jimple.*;
/**
 * This class is for information of one method.
 */
public class MethodInfo {
  public SootMethod sootMethod;
  /**
   * The class where this method is declared.
   */
  public SootClass sootClass;
  public IndexMaps indexMaps;
  public BuildPDG methodPDG;
  /**
   * A set of {@link CallSite CallSite}, which
   * is a call site to this method.
   */
  public Set whoCallMe;
  Fields MOD;
  Fields REF;
  //set of InterClassStmt{className, methodName, callsiteStmt}
  /**
   * (Original) slice criterion for this method.
   */
  SliceCriterion sCriterion;
  BitSet sliceSet;
  /**
   * Generated slice criterion for this method.
   */
  SliceCriterion increCriterion;
  /**
   * A set of {@link CallSite CallSite} such that
   * inside the called method body there may contain
   * some ready dependence.
   */
  Set possibleReadyDependCallSite; //CallSite set
  StmtList stmtList;
  public StmtList originalStmtList;
  Set removedStmt;

  public String toString()
	{
	  return "IndexMaps: " + indexMaps + "\n\n" +  methodPDG + "\n\n whoCallMe: " + whoCallMe;
	}
}
