/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Shawn Laubach (laubach@acm.org)        *
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

package edu.ksu.cis.bandera.bofa;

import ca.mcgill.sable.util.*;

/*
 * FGWorkSendVals.java
 * $Id: FGWorkSendVals.java,v 1.2 2002/02/21 07:42:23 rvprasad Exp $
 */

/**
 * This class is a specialized form of work concerned with pushing along new
 * values to flow graph nodes.
 *
 * @author <A HREF="mailto:hatcliff@cis.ksu.edu">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.2 $)
 */
public class FGWorkSendVals extends FGWork
{
	/**
	 * Set of values to be pushed into the destination node.
	 */
	private FASet values;

	/**
	 * Constructor for the class.
	 *
	 * @param values the set of values to be pushed.
	 * @param dest the node where the values are to be sent.  */
	public FGWorkSendVals(FASet values, FGNode dest)
    {
		this.values = values;
		this.dest   = dest;
    }

	/*
	 * Carries out the work associated with this work object.  First, we find the "new" values that
	 * will be arriving at destination by building a set <em>deltaValues<\em> containing the
	 * difference between the current values at dest and the sent values.  If deltaValues is
	 * non-empty, then we invoke each action for this node on deltaValues.  Finally, we generate new
	 * work that will send deltaValues to each successor of this node.
	*/

	/**
	 * Pushes the given values into the given flow graph node.
	 *
	 */
	public void doWork()
    {
		FASet destValues, deltaValues;

		destValues = dest.getValues();
		deltaValues = values.buildDiffSet(destValues);

		if (!deltaValues.isEmpty()) {
			destValues.addAll(deltaValues);

			FGAction action;
			// carry out each action
			for (Iterator i = dest.getActions().iterator(); i.hasNext();) {
				action = (FGAction) i.next();
				action.doAction(deltaValues);
			}
			FGNode succ;
			// send new values to successors
			for (Iterator i = dest.getSuccs().iterator(); i.hasNext();) {
				succ = (FGNode) i.next();
				FA.workList.insert(new FGWorkSendVals(deltaValues,succ));
			}

		}
    }
}
