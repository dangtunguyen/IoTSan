package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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

import java.io.*;
import java.util.*;

/**
 * A Location represents a control point in a thread from which 
 * guarded transformations may be taken. 
 * <p>
 * Each location has:
 * <ul>
 * <li> An integer id (this is negative before the locations are numbered,
 * positive afterwards).
 * <li> An optional label for printing (constructed from the id if absent).
 * <li> The thread to which it belongs.
 * <li> An integer mark for use in searches.
 * <li> Vectors of transformations into and out of the location.
 * <li> A vector of local state variables that are live at the location.
 * </ul>
 */

public class Location {

	int id;        // location id
	BirThread thread; // thread we're contained in
	TransVector inTrans;     // incoming Transformations
	TransVector outTrans;    // outgoing Transformations
	StateVarVector liveVars;
	int mark;         // Location mark (for DFS)
	String label;
	boolean first = false;    // first location in the block
	
	/* [Thomas, May 23, 2017]
	 * */
	boolean printed = false; /* Whether this location is already printed */

	static int markCount = 0;   // mark generator
	static int locCount = 0;

	public Location(BirThread thread) {
		this.thread = thread;
		this.inTrans = new TransVector();
		this.outTrans = new TransVector();
		this.mark = 0;
		this.id = - (++locCount);
	}
	/**
	 * Add a transformation out of the location.
	 * @param toLoc target of transformation
	 * @param guard guard expression
	 * @param actions vector of actions
	 * @return the new transformation
	 */
	public Transformation addTrans(Location toLoc, Expr guard, 
			ActionVector actions) {
		Transformation trans = 
				new Transformation(this, toLoc, guard, actions);
		outTrans.addElement(trans);
		toLoc.inTrans.addElement(trans);
		thread.getSystem().addTrans(trans);
		return trans;
	}
	// Accessors
	public int getId() { return id; }
	public TransVector getInTrans() { return inTrans; }
	public String getLabel() { return (label != null) ? label : "s" + id; }
	public StateVarVector getLiveVars() { return liveVars; }
	public int getMark() { return mark; }
	public static int getNewMark() { return ++markCount; }
	public TransVector getOutTrans() { return outTrans; }
	public BirThread getThread() { return thread; }
	public void setFirst() { first = true; }
	public boolean isFirst() { return first; }

	/** 
	 * A location is visible if:
	 * <ul> 
	 * <li> it has no incoming transformations, or
	 * <li> it has no outgoing transformations, or
	 * <li> some incoming transformation is visible.
	 * </ul>
	 */

	public boolean isVisible() {
		if (first)
			return true;
		if (inTrans.size() == 0)
			return true;
		if (outTrans.size() == 0)
			return true;
		for (int i = 0; i < inTrans.size(); i++)
			if (inTrans.elementAt(i).isVisible())
				return true;
		return false;
	}
	public void setId(int id) { this.id = id; }
	public void setLabel(String label) { this.label = label; }
	public void setLiveVars(StateVarVector liveVars) { 
		this.liveVars = liveVars; 
	}
	public void setMark(int mark) { this.mark = mark; }
	// Printing
	public String toString() {
		return "<Loc " + id + ">";
	}
	
	/* [Thomas, May 23, 2017]
	 * */
	public boolean isPrinted()
	{
		return this.printed;
	}
	public void setPrinted()
	{
		this.printed = true;
	}
}
