package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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

import edu.ksu.cis.bandera.prog.DomAnalysis;
import edu.ksu.cis.bandera.prog.PostdomAnalysis;
import edu.ksu.cis.bandera.prog.BackEdgeAnalysis;

public class TestDom
{
  public static void main(String[] args)
	{
	  SootClassManager cm = new SootClassManager();
	  SootClass sClass = cm.getClass(args[0]);
	  sClass.resolveIfNecessary();
	  
	  BuildAndStoreBody bd = 
	new BuildAndStoreBody(Jimple.v(), 
			      new StoredBody(ClassFile.v()), 
			      BuildJimpleBodyOption.NO_PACKING);
	  
	  Iterator methodIt = sClass.getMethods().iterator();
	  
	  while(methodIt.hasNext())
	{
	  SootMethod m = (SootMethod) methodIt.next();
	  bd.resolveFor(m);
	  JimpleBody body = (JimpleBody) new StoredBody(Jimple.v()).resolveFor(m);
	  StmtList stmtList = body.getStmtList();
	  CompleteStmtGraph stmtGraph = new CompleteStmtGraph(stmtList);

	  //dominators
	  DomAnalysis da = new DomAnalysis(stmtList, stmtGraph);
	  Map domMap = da.stmtToDominatorsMap();
	  System.out.println("\n dominators map for method : " + m.getName());
	  System.out.println(domMap);

	  //immediate dominator
	  Map immdomMap = da.stmtToImmediateDom();
	  System.out.println("\n immediate dominators map for method : " + m.getName());
	  System.out.println(immdomMap);
	  
	  //postdominators
	  PostdomAnalysis pda = new PostdomAnalysis(stmtList, stmtGraph);
	  Map postdomMap = pda.stmtToPostdominatorsMap();
	  System.out.println("\n post-dominators map for method : " + m.getName());
	  System.out.println(postdomMap);

	  //immediate postdominator
	  Map immpostdomMap = pda.stmtToImmediatePostdom();
	  System.out.println("\n immediate post-dominators map for method : " + m.getName());
	  System.out.println(immpostdomMap);


	  //back edge analysis
	  BackEdgeAnalysis bea = new BackEdgeAnalysis(stmtList, stmtGraph);
	  List edgeList = bea.backEdgeList();
	  System.out.println("\n back edge list for method: " + m.getName());
	  System.out.println(edgeList);
	}
	}
}