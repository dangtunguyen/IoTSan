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

import edu.ksu.cis.bandera.util.GenericSet;
import edu.ksu.cis.bandera.prog.Index;

public abstract class AbstractDom
{
  protected StmtList stmtList;
  protected StmtGraph stmtGraph;
  protected final static Index specialExitNode = new Index(-2);
  protected final static Index STARTNODE = new Index(-1);

  public AbstractDom(StmtList statementsList, StmtGraph sg)
	{
	  stmtList = statementsList;
	  stmtGraph = sg;
	}
  protected Map computeImmedDom(Map stmtToSet) 
	{
	  Map stmtToImmediate =  new HashMap(stmtList.size() *2 +1, 0.7f);

	  Iterator keyStmtIt = stmtToSet.keySet().iterator();
	  while (keyStmtIt.hasNext())
	{
	  Stmt keyStmt = (Stmt) keyStmtIt.next();
	  GenericSet doms = (GenericSet) stmtToSet.get(keyStmt);

	  GenericSet workSet = new GenericSet();
	  workSet = workSet.union(doms);
	  workSet.remove(new Index(stmtList.indexOf(keyStmt)));
	  
	  GenericSet immediate = new GenericSet();
	  immediate = immediate.union(workSet);

	  for (int i=0; i<workSet.size(); i++)
	    {
	      Index domIndex = (Index) workSet.setRef(i);
	      GenericSet domX = (GenericSet) stmtToSet.get((Stmt) stmtList.get(domIndex.index()));
	      GenericSet domX2 = new GenericSet();
	      domX2 = domX2.union(domX);
	      domX2.remove(domIndex);
	      
	      GenericSet interDoms = domX2.intersect(immediate);

	      if (interDoms.size() != 0) immediate = immediate.difference(interDoms);
	    }

	  if (immediate.size() == 1) 
	    stmtToImmediate.put(keyStmt, (Index) immediate.setRef(0));
	  else if (immediate.size() == 0) 
	    stmtToImmediate.put(keyStmt, STARTNODE);
	  else
	    {
	      System.out.println("immediate dominator and postdominator should be unique or none!");
	      System.exit(0);
 	    }
	}
	  return stmtToImmediate;
	}
  protected GenericSet getStartNodes()
	{
	  List heads = stmtGraph.getHeads();
	  Iterator headIt = heads.iterator();
	  
	  GenericSet nodeSet = new GenericSet();

	  while (headIt.hasNext())
	{
	  Stmt head = (Stmt) headIt.next();
	  Index headIndex = new Index(stmtList.indexOf(head));
	  nodeSet.addElemToSet(headIndex);
	}
	  if (nodeSet.size() == 0) nodeSet.addElemToSet(new Index(0));
	  return nodeSet;
	}
  protected GenericSet indexSetWithoutExceptionHandling()
	{
	  LinkedList workList = new LinkedList();
	  GenericSet startSet = this.getStartNodes();
	  GenericSet indexSet = new GenericSet();

	  for (int i=0; i<startSet.size(); i++)
	{
	  Index nodeIndex = (Index) startSet.setRef(i);
	  workList.addFirst(nodeIndex);
	}

	  while (workList.size() != 0)
	{
	  Index nodeIndex = (Index) workList.removeFirst();
	  indexSet.addElemToSet(nodeIndex);
	  Stmt nodeStmt = (Stmt) stmtList.get(nodeIndex.index());
	  List nodeStmtSuccs = stmtGraph.getSuccsOf(nodeStmt);
	  List newSuccs = removeExceptionCaught(nodeStmtSuccs);
	  Iterator succIt = newSuccs.iterator();
	  while (succIt.hasNext())
	    {
	      Stmt succStmt = (Stmt) succIt.next();
	      Index succIndex = new Index(stmtList.indexOf(succStmt));
	      if (workList.contains(succIndex) || indexSet.contains(succIndex))
		{
		}
	      else workList.addLast(succIndex);
	    }
	}
	  return indexSet;
	}
  private  boolean isExceptionHandler(Stmt succStmt)
	{
	  boolean isHandler = false;

	  Integer succStmtIndex = new Integer(stmtList.indexOf(succStmt));
	  JimpleBody jimpleBody = (JimpleBody) stmtList.getBody();
	  Iterator trapIt = jimpleBody.getTraps().iterator();
	  while(trapIt.hasNext())
	{
	  Trap trap = (Trap) trapIt.next();
	  Stmt trapHandler = (Stmt) trap.getHandlerUnit();
	  Integer handlerIndex = new Integer(stmtList.indexOf(trapHandler));
	  if (succStmtIndex.equals(handlerIndex))
	    {
	      isHandler = true;
	      break;
	    }
	}

	  return isHandler;
	}
  protected List removeExceptionCaught(List actualSuccs)
	{
	  List newSuccs = new ArrayList();
	  Iterator asuccsIt = actualSuccs.iterator();
	  while (asuccsIt.hasNext())
	{
	  Stmt succStmt = (Stmt) asuccsIt.next();
	  if (!isExceptionHandler(succStmt))
	    newSuccs.add(succStmt);
	}

	  return newSuccs;
	}
}
