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

public class BackEdgeAnalysis
{
  private List backEdgesList;
  private StmtList stmtList;
  private StmtGraph stmtGraph;

  public BackEdgeAnalysis(StmtList statementsList, StmtGraph sg)
	{
	  stmtList = statementsList;
	  stmtGraph =sg;

	  backEdgeAnalysis();
	}
  private void backEdgeAnalysis()
	{

	  DomAnalysis da = new DomAnalysis(stmtList, stmtGraph);
	  Map stmtToDoms = da.stmtToDominatorsMap();

	  backEdgesList = new ArrayList();

	  Iterator keyStmtIt = stmtToDoms.keySet().iterator();
	  while (keyStmtIt.hasNext())
	{
	  Stmt keyStmt = (Stmt) keyStmtIt.next();
	  Index stmtIndex = new Index(stmtList.indexOf(keyStmt));

	  List succsOfKeyStmt = stmtGraph.getSuccsOf(keyStmt);
	  GenericSet succSet = stmtListToIndexSet(succsOfKeyStmt);

	  GenericSet dominators = (GenericSet) stmtToDoms.get(keyStmt);
	  for (int i = 0; i < dominators.size(); i++)
	    {   
	      Index domIndex = (Index) dominators.setRef(i);
	      if (succSet.contains(domIndex))
		{
		  Edge backEdge = new Edge(stmtIndex, domIndex);
		  List natLoop = naturalLoop(stmtIndex, domIndex);
		  backEdge.setNaturalLoop(natLoop);

		  //this if should be refined further
		  //if (stmtIndex.index() > domIndex.index())
		  //backEdge.setIndefiniteLoop();

		  backEdgesList.add(backEdge);
		}
	    }    
	}
	}
  public List backEdgeList()
	{
	  return backEdgesList;
	}
  private List naturalLoop(Index fromIndex, Index toIndex)
	{
	  List loop = new ArrayList();

	  loop.add(fromIndex);
	  loop.add(toIndex);

	  if (fromIndex.equals(toIndex)) return loop;

	  LinkedList stack = new LinkedList();
	  stack.addFirst(fromIndex);

	  while (!stack.isEmpty())
	{
	  Index currentIndex = (Index) stack.removeFirst();
	  Stmt currentStmt = (Stmt) stmtList.get(currentIndex.index());
	  List predsList = stmtGraph.getPredsOf(currentStmt);
	  Iterator predsIt = predsList.iterator();
	  while (predsIt.hasNext())
	    {
	      Index predIndex = new Index(stmtList.indexOf(predsIt.next()));
	      if (!loop.contains(predIndex))
		{
		  loop.add(predIndex);
		  stack.addLast(predIndex);
		}
	    }
	}

	  return loop;
	}
 private GenericSet stmtListToIndexSet(List succList)
	{
	  GenericSet returnSet = new GenericSet();

	  for (int i = 0; i<succList.size(); i++)
	{
	  Stmt s = (Stmt) succList.get(i);
	  Index sIndex = new Index(stmtList.indexOf(s));
	  returnSet.addElemToSet(sIndex);
	}
	  return returnSet;
	}
}
