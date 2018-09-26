package edu.ksu.cis.bandera.birc;

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
import java.io.*;
import java.util.*;

import edu.ksu.cis.bandera.bir.*;

/**
 * Reducer does two things:
 * <ol>
 * <li> collapses trivial transformations together to reduce the number
 *  of locations (a trivial transformation is one with no actions).
 * <li> uses VisibleExtractor to mark each transformation as
 *  visible/invisible (note: we no longer collapse sequences of
 * invisible transitions together in BIRC but simply mark them
 * as invisible and let the translator collapse them if this
 * is appropriate).
 * </ol>
 */

public class Reducer {

	TransSystem system;
	int mark;
	Vector newTransformations = new Vector();

	public Reducer(TransSystem system) {
	this.system = system;
	this.mark = Location.getNewMark();
	}
	/**
	 * Follow all invisible transformations from the given location,
	 * appending them to the transition sequence, until we reach
	 * a visible transformation. 
	 */

	public void reduce(Location currentLoc, TransSequence seq, 
		       boolean visible) {
	// if currentLoc visible, mark it
	if (visible) 
	    currentLoc.setMark(mark);
	// For each transformation out of currentLoc
	TransVector outTrans = currentLoc.getOutTrans();
	for (int i = 0; i < outTrans.size(); i++) {
	    Transformation trans = outTrans.elementAt(i);
	    // If the transformation is not visible,
	    //    is not a self-loop,
	    //    does not loop back to some location already in the sequence,
	    //    and is followed by some other transformation,
	    // Then delete the transformation, add it to the sequence,
	    //    and continue collapsing transformations from the next loc.
	    if (! trans.isVisible() && 
		(trans.getFromLoc() != trans.getToLoc()) &&
		(! seq.containsLoc(trans.getToLoc())) &&
		(trans.getToLoc().getOutTrans().size() > 0)) {
		trans.markDeleted();
		reduce(trans.getToLoc(), seq.add(trans), false);
	    }
	    else {  
		// Visible trans: delete it, add it to the sequence,
		// and add the sequence as a new transformation.
		trans.markDeleted();
		newTransformations.addElement(seq.add(trans));
		// If we haven't visited the toLoc before, continue from there
		if ((trans.getToLoc().getMark() != mark))
		    reduce(trans.getToLoc(), new TransSequence(), true);
	    }
	}
	}
	public void run() {
	// First mark all Transformations as trivial/nontrivial 
	// (use visible flag)
	TransVector transVector = system.getTransformations();
	for (int i = 0; i < transVector.size(); i++) {
	    Transformation trans = transVector.elementAt(i);
	    trans.setVisible(trans.getActions().size() > 0);
	}

		// Apply reduction, deleting transformations that will be collapsed
	ThreadVector threadVector = system.getThreads();
	for (int i = 0; i < threadVector.size(); i++) {
	    BirThread thread = threadVector.elementAt(i);
	    reduce(thread.getStartLoc(), new TransSequence(), true);
	}
	Transformation.purge();

		// Add collapsed transformations
	for (int i = 0; i < newTransformations.size(); i++) {
	    TransSequence seq =(TransSequence)newTransformations.elementAt(i);
	    seq.fromLoc().addTrans(seq.toLoc(),  seq.guard(), 
				   seq.actions());
	}	

	// Finally, mark all Transformations as visible/invisible
	VisibleExtractor vis = new VisibleExtractor();
	transVector = system.getTransformations();
	for (int i = 0; i < transVector.size(); i++) {
	    Transformation trans = transVector.elementAt(i);
	    trans.setVisible(false);
	    ActionVector actions = trans.getActions();
	    for (int j = 0; j < actions.size(); j++) {
		vis.reset();
		actions.elementAt(j).apply(vis);
		// Transformation is visible if one of its actions is
		// visible or observable.
		if (actions.elementAt(j).isObservable() || vis.isVisible())
		    trans.setVisible(true);
	    }
	}
	}
}
