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
 * A guarded transformation.
 */

public class Transformation {

	Location fromLoc;                // from location
	Location toLoc;                  // to location
	Expr guard;                      // guard (or null)
	ActionVector actions;            // actions
	boolean markedDeleted = false;   // to be deleted
	boolean visible = true;          // is visible

	static Vector deletedVector = new Vector();

	public Transformation(Location fromLoc, Location toLoc, Expr guard,
			  ActionVector actions) {
	this.fromLoc = fromLoc;
	this.toLoc = toLoc;
	this.guard = guard;
	this.actions = actions;
	}
	void deleteTrans() {
	fromLoc.getOutTrans().removeElement(this);
	toLoc.getInTrans().removeElement(this);
	}
	public ActionVector getActions() { return actions; }
	// Accessors
	public Location getFromLoc() { return fromLoc; }
	public Expr getGuard() { return guard; }
	public Location getToLoc() { return toLoc; }
	public boolean isVisible() { return visible; }
	/**
	 * Mark for future deletion (actual deletion done when purge() is called).
	 */

	public void markDeleted() {
	if (! markedDeleted) {
	    markedDeleted = true;
	    deletedVector.addElement(this);
	}
	}
	/**
	 * Delete all transformations marked for deletion.
	 */

	public static void purge() {
	for (int i = 0; i < deletedVector.size(); i++) 
	    ((Transformation)deletedVector.elementAt(i)).deleteTrans();
	deletedVector = new Vector();
	}
	public void setVisible(boolean visible) { this.visible = visible; }
	// Printing
	public String toString() {
	return "<Trans " + fromLoc.getId() + "," + toLoc.getId() + ">";
	}
}
