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

public class PostdomAnalysis extends AbstractDom
{
  private Map stmtToPostdoms;
  private Map stmtToImmediatePostdom;

  public PostdomAnalysis(StmtList statementsList, StmtGraph sg)
	{
	  super(statementsList, sg);
	  stmtToPostdoms = new HashMap(stmtList.size() *2 +1, 0.7f);

	  postdominatorsMap();
	  stmtToImmediatePostdom = computeImmedDom(stmtToPostdoms);
	}
  private GenericSet exitNodes()
	{
	  GenericSet exitNodeSet = new GenericSet();
	  List tails = stmtGraph.getTails();
	  Iterator tailIt = tails.iterator();

	  while (tailIt.hasNext())
	{
	  Stmt tail = (Stmt) tailIt.next();
	  Index tailIndex = new Index(stmtList.indexOf(tail));
	  exitNodeSet.addElemToSet(tailIndex);
	}

	  //element of tails are those successor is 0
	  //add return stmt to exitNode, whose successor is not 0
	  //this is caused from nextnextstmtAddress, 
	  //the successor of these returns must be an exception catch

	  Iterator stmtIt = stmtList.iterator();
	  while (stmtIt.hasNext())
	{
	  Stmt retStmt = (Stmt) stmtIt.next();
	  if ((retStmt instanceof ReturnStmt) || (retStmt instanceof ReturnVoidStmt))
	    {
	      List succs = stmtGraph.getSuccsOf(retStmt);
	      if (succs.size() != 0) 
		//maybe should limit to succ.size() == 1, and the succ
		//is an exeception handler
		{
		  Index retStmtIndex = new Index(stmtList.indexOf(retStmt));
		  exitNodeSet.addElemToSet(retStmtIndex);
		}
	    }
	}

	  return exitNodeSet;
	}
  private GenericSet exitNodesWithoutThrow(GenericSet exitNodeSet)
	{
	  if (exitNodeSet == null) 
	{
	  System.out.println("exitNodeSet should not be null in exitNodesWithoutThrow method in IndexMaps.java");
	  System.exit(0);
	}
	 
	  GenericSet nodeSet = new GenericSet();

	  for (int i = 0; i < exitNodeSet.size(); i++)
	{
	  Index exitIndex = (Index) exitNodeSet.setRef(i);
	  Stmt exitStmt = (Stmt) stmtList.get(exitIndex.index());
	  if (!(exitStmt instanceof ThrowStmt))
	    nodeSet.addElemToSet(exitIndex);
	}

	  return nodeSet;
	}
  public Index immediatePostdomOf(Stmt s)
	{
	  return (Index) stmtToImmediatePostdom.get(s);
	}
  private Index indefiniteFrom()
	{
	  BackEdgeAnalysis bea = new BackEdgeAnalysis(stmtList, stmtGraph);
	  List backEdgesList = bea.backEdgeList();

	  Iterator edgeIt = backEdgesList.iterator();
	  while (edgeIt.hasNext())
	{
	  Edge edge = (Edge) edgeIt.next();
	  if (edge.isPossibleIndefiniteLoop()) 
	      return edge.fromNode;
	}

	  return AbstractDom.specialExitNode;
	}
  private void postdominatorsMap()
	{
	  boolean change = true;
	  GenericSet postDominators;
	  GenericSet tempPostdoms;
	  Stmt keyStmt;
	  Index stmtIndex;
	  LinkedList workList = new LinkedList();
	  GenericSet visitedNode = new GenericSet();

	  //initialize the exit statement's postdom as itself
	  //if there is no exit node, that means there must be an
	  //indefinite loop

	  GenericSet exitNodeList = exitNodes();
	  GenericSet exitNodesNoThrow = exitNodesWithoutThrow(exitNodeList);

	  if (exitNodesNoThrow.size() == 0)
	{
	  Index indefiniteNode = indefiniteFrom();
	  if (indefiniteNode.equals(AbstractDom.specialExitNode))
	    {
	      System.out.println("Can not find out the exit node or the infinite loop");
	      System.exit(0);
	    }
	  workList.addFirst(indefiniteNode);
	  
	  keyStmt = (Stmt) stmtList.get(indefiniteNode.index());
	  postDominators = new GenericSet(indefiniteNode);
	  stmtToPostdoms.put(keyStmt, postDominators);
	}
	  else
	{
	  for (int i=0; i< exitNodesNoThrow.size(); i++)
	    {
	      stmtIndex = (Index) exitNodesNoThrow.setRef(i);
	      workList.addFirst(stmtIndex);
	      
	      keyStmt = (Stmt) stmtList.get(stmtIndex.index());
	      postDominators = new GenericSet(stmtIndex);
	      stmtToPostdoms.put(keyStmt, postDominators);
	    }
	}

	  // Initialize postdoms for other statement as all index set
	  // but not including exception handling statement

	  GenericSet initialWorkList = new GenericSet();
	  for (int i=0; i<workList.size(); i++)
	{
	  initialWorkList.addElemToSet((Index) workList.get(i));
	}

	  GenericSet startNodes = getStartNodes();

	  //add start nodes into the worklist 
	  for (int i = 0; i< startNodes.size(); i++)
	{
	  stmtIndex = (Index) startNodes.setRef(i);
	  workList.addFirst(stmtIndex);
	}

	  postDominators = indexSetWithoutExceptionHandling();

	  while (workList.size() != 0)
	{
	  Index nodeIndex = (Index) workList.removeFirst();
	  visitedNode.addElemToSet(nodeIndex);
	  Stmt nodeStmt = (Stmt) stmtList.get(nodeIndex.index());

	  if (! initialWorkList.contains(nodeIndex))
	    stmtToPostdoms.put(nodeStmt, postDominators);

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

	  while (change)
	{	  
	  change = false;
	  workList = new LinkedList();
	  visitedNode = new GenericSet();

	  //initializing worklist everytime
	  for (int i=0; i<initialWorkList.size(); i++)
	    {
	      workList.addFirst((Index) initialWorkList.setRef(i));
	    }
	  for (int i = 0; i< startNodes.size(); i++)
	    {
	      stmtIndex = (Index) startNodes.setRef(i);
	      workList.addFirst(stmtIndex);
	    }

	  //System.out.println("workList: " + workList);

	  while (workList.size() != 0)
	    {
	      stmtIndex = (Index) workList.removeFirst();
	      visitedNode.addElemToSet(stmtIndex);

	      keyStmt = (Stmt) stmtList.get(stmtIndex.index());
	      postDominators = (GenericSet) stmtToPostdoms.get(keyStmt);
	      tempPostdoms = new GenericSet();
	      tempPostdoms = tempPostdoms.union(postDominators);
	      
	      List realSuccs = stmtGraph.getSuccsOf(keyStmt);
	      	      
	      List succs = removeExceptionCaught(realSuccs);
	      
	      for (int i = 0; i < succs.size(); i++)
		{
		  Stmt succStmt = (Stmt) succs.get(i);
		  GenericSet succDom = (GenericSet) stmtToPostdoms.get(succStmt);
		  // System.out.println("succStmt: " + succStmt + " ( " + (Index) stmtToInd.get(succStmt) + " ) " + " postdom: " + succDom);
		  //if (predDom != null)
		  tempPostdoms = tempPostdoms.intersect(succDom);
		}

	      tempPostdoms.addElemToSet(stmtIndex);
	      
	      if (! tempPostdoms.equals(postDominators))
		{
		  change = true;
		  stmtToPostdoms.remove(keyStmt);
		  stmtToPostdoms.put(keyStmt, tempPostdoms);
		}

	      //begin to add nodes into workList

	      for (int i=0; i<succs.size(); i++)
		{
		  Stmt succStmt = (Stmt) succs.get(i);
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
  public GenericSet postdomsOf(Stmt stmt)
	{
	  return (GenericSet) stmtToPostdoms.get(stmt);
	}
  public Map stmtToImmediatePostdom()
	{
	  return stmtToImmediatePostdom;
	}
  public Map stmtToPostdominatorsMap()
	{
	  return stmtToPostdoms;
	}
}
