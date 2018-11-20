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
 * FGNode.java
 * $Id: FGNode.java,v 1.3 2002/03/20 02:54:05 rvprasad Exp $
 */

/**
 * The class representing a node in the flow graph.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.3 $)
 */
public class FGNode
{
	/**
	 * Values flowing into the FG node
	 */
	FASet values;

	/**
	 * Actions to be invoked when a new value flows into the FG node
	 */
	private Set actions;

	/**
	 * Successor FG nodes in the FlowGraph
	 */
	private Set succs;

	/**
	 * Predecessor FG nodes in the FlowGraph
	 */
	private Set preds;

	/**
	 * Constructor that creates empty sets for values, actions, and succs.
	 *
	 */
	public FGNode()
    {
		this.values = new FASet();
		actions = new VectorSet();
		succs = new VectorSet();
		preds = new VectorSet();
    }

	/**
	 * Constructor that receives values for all node components.
	 *
	 * @param values set of values that have flowed into this node.  These values are refered to as
	 * flow values.
	 * @param actions set of actions to be executed when values arrive at this node.
	 * @param succs successors of this node in the flowgraph.
	 * @param preds a <code>Set</code> value
	 */
	public FGNode(FASet values, Set actions, Set succs, Set preds)
    {
		this.values = values;
		this.actions = actions;
		this.succs = succs;
		this.preds = preds;
    }

	/**
	 * Returns the set of flow values for this node.
	 *
	 * @return the set of flow values for this node.
	 */
	public FASet getValues()
    {
		return values;
    }

	/**
	 * Adds an action to this node.
	 *
	 * @param action the action to be associated with the FG node.
	 */
	public void addAction(FGAction action)
    {
		actions.add(action);
    }

	/**
	 * Returns the set of actions associated with this node.
	 *
	 * @return the set of actions associated with this node.
	 */
	public Set getActions()
    {
		return actions;
    }

	/**
	 * Connects one node to another node in predecessor-successor pattern.
	 *
	 * @param source the source node in the connection.
	 * @param dest the destination node in the conneciton.
	 */
	static void makeArc(FGNode source, FGNode dest)
	{
		if (source != null && dest != null) {
			source.succs.add(dest);
			dest.preds.add(source);
			FA.workList.insert(new FGWorkSendVals(source.values.buildDiffSet(dest.values), dest));
		} // end of if ()
	}

	/**
	 * Returns the set of successors for this node.
	 *
	 * @return the set of successors for this node.
	 */
	public Set getSuccs()
    {
		return succs;
    }

	/**
	 * Returns the set of predecessors for this node.
	 *
	 * @return the set of predcessors for this node.
	 */
	public Set getPreds()
    {
		return preds;
    }
}
