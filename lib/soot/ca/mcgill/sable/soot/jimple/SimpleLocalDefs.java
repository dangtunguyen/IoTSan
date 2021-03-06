package ca.mcgill.sable.soot.jimple;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Jimple, a 3-address code Java(TM) bytecode representation.        *
 * Copyright (C) 1997, 1998 Raja Vallee-Rai (kor@sable.mcgill.ca)    *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project of the Sable Research Group,      *
 * School of Computer Science, McGill University, Canada             *
 * (http://www.sable.mcgill.ca/).  It is understood that any         *
 * modification not identified as such is not covered by the         *
 * preceding statement.                                              *
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
 * License along with this library; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other Sable Research Group projects, please      *
 * visit the web site: http://www.sable.mcgill.ca/                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*
 Reference Version
 -----------------
 This is the latest official version on which this file is based.
 The reference version is: $SootVersion: 1.beta.4 $

 Change History
 --------------
 A) Notes:

 Please use the following template.  Most recent changes should
 appear at the top of the list.

 - Modified on [date (March 1, 1900)] by [name]. [(*) if appropriate]
   [description of modification].

 Any Modification flagged with "(*)" was done as a project of the
 Sable Research Group, School of Computer Science,
 McGill University, Canada (http://www.sable.mcgill.ca/).

 You should add your copyright, using the following template, at
 the top of this file, along with other copyrights.

 *                                                                   *
 * Modifications by [name] are                                       *
 * Copyright (C) [year(s)] [your name (or company)].  All rights     *
 * reserved.                                                         *
 *                                                                   *

 B) Changes:
 
 - Modified on March 13, 1999 by Raja Vallee-Rai (rvalleerai@sable.mcgill.ca) (*)
   Re-organized the timers.

 - Modified on November 2, 1998 by Raja Vallee-Rai (kor@sable.mcgill.ca) (*)
   Repackaged all source files and performed extensive modifications.
   First initial release of Soot.

 - Modified on 23-Jul-1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   Renamed the uses of Hashtable to HashMap.

 - Modified on 15-Jun-1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   First internal release (Version 0.1).
*/

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;

// FSet version

public class SimpleLocalDefs implements LocalDefs
{
	Map localStmtPairToDefs;

	/*
	public List getDefsOfBefore(Local l, Stmt s)
	{
		IntPair pair = (IntPair) analysis.localToIntPair.get(l);
		FSet value = (FSet) analysis.getValueBeforeStmt(s);

		List localDefs = value.toList(pair.op1, pair.op2);

		return localDefs;
	}*/

/*
			Object[] elements = ((FSet) analysis.getValueBeforeStmt(s)).toArray();
			List listOfDefs = new LinkedList();

			// Extract those defs which correspond to this local
			{
				for(int i = 0; i < elements.length; i++)
				{
					DefinitionStmt d = (DefinitionStmt) elements[i];

					if(d.getLeftOp() == l)
						listOfDefs.add(d);
				}
			}

			// Convert the array so that it's of an appropriate form
			{
				Object[] objects = listOfDefs.toArray();
				DefinitionStmt[] defs = new DefinitionStmt[objects.length];

				for(int i = 0; i < defs.length; i++)
					defs[i] = (DefinitionStmt) objects[i];

				return defs;
			}

		}
	}
*/

	/*
	public DefinitionStmt[] getDefsOfAfter(Local l, Stmt s)
	{
	   Object[] elements = ((FSet) analysis.getValueAfterStmt(s)).toArray();
		   List listOfDefs = new LinkedList();

		// Extract those defs which correspond to this local
		 {
			 for(int i = 0; i < elements.length; i++)
			{
				   DefinitionStmt d = (DefinitionStmt) elements[i];

				if(d.getLeftOp() == l)
					listOfDefs.add(d);
			}
		   }

		   // Convert the array so that it's of an appropriate form
		   {
				Object[] objects = listOfDefs.toArray();
				DefinitionStmt[] defs = new DefinitionStmt[objects.length];

				for(int i = 0; i < defs.length; i++)
					defs[i] = (DefinitionStmt) objects[i];

				return defs;
			}
	}

	public DefinitionStmt[] getDefsBefore(Stmt s)
	{
		Object[] elements = ((FSet) analysis.getValueBeforeStmt(s)).toArray();
		DefinitionStmt[] defs = new DefinitionStmt[elements.length];

		for(int i = 0; i < elements.length; i++)
			defs[i] = (DefinitionStmt) elements[i];

		return defs;
	}

	public DefinitionStmt[] getDefsAfter(Stmt s)
	{
		Object[] elements = ((FSet) analysis.getValueAfterStmt(s)).toArray();
		DefinitionStmt[] defs = new DefinitionStmt[elements.length];

		for(int i = 0; i < elements.length; i++)
			defs[i] = (DefinitionStmt) elements[i];

		return defs;
	}
	*/
	public SimpleLocalDefs(CompleteStmtGraph g)
	{
		if(Main.isProfilingOptimization)
			Main.defsTimer.start();
		
		if(Main.isVerbose)
			System.out.println("[" + g.getBody().getMethod().getName() +
				"]     Constructing SimpleLocalDefs...");
	
		LocalDefsFlowAnalysis analysis = new LocalDefsFlowAnalysis(g);
		
		if(Main.isProfilingOptimization)
			Main.defsPostTimer.start();

		// Build localStmtPairToDefs map
		{
			Iterator stmtIt = g.iterator();

			localStmtPairToDefs = new HashMap(g.size() * 2 + 1, 0.7f);

			while(stmtIt.hasNext())
			{
				Stmt s = (Stmt) stmtIt.next();

				Iterator boxIt = s.getUseBoxes().iterator();

				while(boxIt.hasNext())
				{
					ValueBox box = (ValueBox) boxIt.next();

					if(box.getValue() instanceof Local)
					{
						Local l = (Local) box.getValue();
						LocalStmtPair pair = new LocalStmtPair(l, s);

						if(!localStmtPairToDefs.containsKey(pair))
						{
							IntPair intPair = (IntPair) analysis.localToIntPair.get(l);
							BoundedFlowSet value = (BoundedFlowSet) analysis.getFlowBeforeStmt(s);

							List localDefs = value.toList(intPair.op1, intPair.op2);

							localStmtPairToDefs.put(pair, Collections.unmodifiableList(localDefs));
						}
					}
				}
			}
		}

		if(Main.isProfilingOptimization)
			Main.defsPostTimer.end();
				
		if(Main.isProfilingOptimization)
			Main.defsTimer.end();
	}
	public List getDefsOfAt(Local l, Stmt s)
	{
		LocalStmtPair pair = new LocalStmtPair(l, s);

		return (List) localStmtPairToDefs.get(pair);
	}
}
