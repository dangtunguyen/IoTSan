package edu.ksu.cis.bandera.prog;

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
import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

import edu.ksu.cis.bandera.prog.Index;
import edu.ksu.cis.bandera.util.GenericSet;

public class DomAnalysis extends AbstractDom
{
  private Map stmtToDoms;
  private Map stmtToImmediateDom;

  public DomAnalysis(StmtList statementsList, StmtGraph sg)
	{
	  super(statementsList, sg);
	  stmtToDoms = new HashMap(stmtList.size() *2 +1, 0.7f);

	  dominatorsMap();
	  stmtToImmediateDom = computeImmedDom(stmtToDoms);
	}
  private void dominatorsMap()
	{      
	  GenericSet dominators;
	  GenericSet tempDoms;
	  Stmt keyStmt;
	  Index stmtIndex;
	  LinkedList workList = new LinkedList();
	  GenericSet visitedNode = new GenericSet();

	  GenericSet startNodes = getStartNodes();

	  //initialize the entry statement's dominator as itself
	  for (int i = 0; i< startNodes.size(); i++)
	{
	  stmtIndex = (Index) startNodes.setRef(i);
	  workList.addFirst(stmtIndex);

	  keyStmt = (Stmt) stmtList.get(stmtIndex.index());
	  dominators = new GenericSet(stmtIndex);
	  stmtToDoms.put(keyStmt, dominators);
	}

	  //initialize the stmtDomin map for other statement as all stmt set
	  //but not including exception handling statement

	  dominators = indexSetWithoutExceptionHandling();
  
	  while (workList.size() != 0)
	{
	  Index nodeIndex = (Index) workList.removeFirst();
	  visitedNode.addElemToSet(nodeIndex);
	  Stmt nodeStmt = (Stmt) stmtList.get(nodeIndex.index());

	  if (! startNodes.contains(nodeIndex))
	    stmtToDoms.put(nodeStmt, dominators);

	  List nodeStmtSuccs = stmtGraph.getSuccsOf(nodeStmt);
	  List newSuccs = removeExceptionCaught(nodeStmtSuccs);
	  Iterator succIt = newSuccs.iterator();
	  while (succIt.hasNext())
	    {
	      Stmt succStmt = (Stmt) succIt.next();
	      Index succIndex = new Index(stmtList.indexOf(succStmt));
	      if (workList.contains(succIndex) || 
		  visitedNode.contains(succIndex))
		{
		}
	      else workList.addLast(succIndex);
	    }
	}

	  //begin fix point iteration

	  boolean change = true;

	  while (change)
	{
	  change = false;
	  workList = new LinkedList();
	  visitedNode = new GenericSet();

	  for (int i = 0; i< startNodes.size(); i++)
	    {
	      stmtIndex = (Index) startNodes.setRef(i);
	      workList.addFirst(stmtIndex);
	    }

	  while (workList.size() != 0)
	    {
	      stmtIndex = (Index) workList.removeFirst();
	      visitedNode.addElemToSet(stmtIndex);

	      keyStmt = (Stmt) stmtList.get(stmtIndex.index());
	      dominators = (GenericSet) stmtToDoms.get(keyStmt);
	      tempDoms = new GenericSet();
	      tempDoms = tempDoms.union(dominators);

	      List preds = stmtGraph.getPredsOf(keyStmt);

	      for (int i = 0; i < preds.size(); i++)
		{
		  Stmt predStmt = (Stmt) preds.get(i);
		  GenericSet predDom = (GenericSet) stmtToDoms.get(predStmt);

		  if (predDom != null)
		    tempDoms = tempDoms.intersect(predDom);
		}

	      tempDoms.addElemToSet(stmtIndex);
	      
	      if (! tempDoms.equals(dominators))
		{
		  change = true;
		  stmtToDoms.remove(keyStmt);
		  stmtToDoms.put(keyStmt, tempDoms);
		}

	      //begin to add nodes into workList
	      List keyStmtSuccs = stmtGraph.getSuccsOf(keyStmt);
	      List newSuccs = removeExceptionCaught(keyStmtSuccs);
	      Iterator succIt = newSuccs.iterator();
	      while (succIt.hasNext())
		{
		  Stmt succStmt = (Stmt) succIt.next();
		  Index succIndex = new Index(stmtList.indexOf(succStmt));
		  if (workList.contains(succIndex) || 
		      visitedNode.contains(succIndex))
		    {
		    }
		  else workList.addLast(succIndex);
		}	      
	    }
	}
	}
  public GenericSet domsOf(Stmt stmt)
	{
	  return (GenericSet) stmtToDoms.get(stmt);
	}
  public Index immediateDomOf(Stmt s)
	{
	  return (Index) stmtToImmediateDom.get(s);
	}
  public Map stmtToDominatorsMap()
	{
	  return stmtToDoms;
	}
  public Map stmtToImmediateDom()
	{
	  return stmtToImmediateDom;
	}
}
