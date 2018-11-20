package edu.ksu.cis.bandera.bui.counterexample;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import edu.ksu.cis.bandera.birc.*;
import java.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

/**
 * The ValueNode object is a simple holder for other types of objects
 * that will exist in the counter example GUI.  It holds SootMethod,
 * SootClass, SootField, and Local objects.
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:18 $
 */
public class ValueNode {
    public java.lang.Object object;
    public int i;

    /**
     * Create a new ValueNode with the given object.
     *
     * @param object java.lang.Object
     */
    public ValueNode(Object object) {
	this.object = object;
    }

    /**
     * Create a new ValueNode with the given object and the
     * given id.
     * 
     * @param object java.lang.Object
     * @param id int
     */
    public ValueNode(Object object, int id) {
	this.object = object;
	this.i = id;
    }

    /**
     * Get the String value of this object.  The actual result is
     * dependent upon what type of object is actually stored.
     *
     * @return java.lang.String
     */
    public String toString() {

	// this first check should change! Threads are no longer represented with
	//  SootMethod objects. -tcw
	if (object instanceof SootMethod) {
	    return "Thread<" + object + ">, ID: " + i;
	}
	else if (object instanceof Local) {
	    Local l = (Local) object;
	    String name = l.getName().trim();
	    if ("JJJCTEMP$0".equals(name)) name = "this";
	    return name + ":" + l.getType().toString().trim();
	}
	else if (object instanceof SootField) {
	    SootField sootField = (SootField)object;
	    return(sootField.getName() + ":" + sootField.getType().toString());
	}
	else {
	    return "" + i;
	}
    }

}
