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

public class ChooseExpander {

	TransSystem system;
	int mark;
	TransVector newTransformations = new TransVector();

	public ChooseExpander(TransSystem system) {
	this.system = system;
	this.mark = Location.getNewMark();
	}
	void expand(Location currentLoc) {
	currentLoc.setMark(mark);
	TransVector outTrans = currentLoc.getOutTrans();
	for (int i = 0; i < outTrans.size(); i++) {
	    Transformation trans = outTrans.elementAt(i);
	    expandTrans(trans);
	    if ((trans.getToLoc().getMark() != mark))
		    expand(trans.getToLoc());
	}
	}
	void expandTrans(Transformation trans) {
	ActionVector actions = trans.getActions();
	if (actions.size() > 0) {
	    if (actions.size() > 1)
		throw new RuntimeException("ChooseExpander doesn't handle multiple assignments per transformation" + trans);
	    if (actions.elementAt(0).isAssignAction()) {
		AssignAction assign = 
		    (AssignAction) actions.elementAt(0);
		if (assign.getRhs() instanceof ChooseExpr) {
		    trans.markDeleted();
		    Vector choices = 
			((ChooseExpr)assign.getRhs()).getChoices();
		    for (int i = 0; i < choices.size(); i++) {
			Expr choice = (Expr) choices.elementAt(i);
			ActionVector newActions = new ActionVector();
			AssignAction choiceAssign = 
			    new AssignAction(assign.getLhs(), choice);
			newActions.addElement(choiceAssign);
			trans.getFromLoc().addTrans(trans.getToLoc(),
						    trans.getGuard(),
						    newActions);
		    }
		}
	    }
	}
	}
	public void run() {
	ThreadVector threadVector = system.getThreads();
	for (int i = 0; i < threadVector.size(); i++) {
	    BirThread thread = threadVector.elementAt(i);
	    expand(thread.getStartLoc());
	}
	Transformation.purge();
	}
}
