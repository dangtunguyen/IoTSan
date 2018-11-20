package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *
 * All rights reserved.                                              *
 * Matthew Dwyer (dwyer@cis.ksu.edu)				     *
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jjjc.*;

/**
 * Provides access to the API for selected java.lang.* methods.
 * <ul>
 * <il> <code>java.lang.thread.<init>(): void</code>
 * </ul>
 * 
 * The idea in this class is to handle the situation where SOOT
 * does not generate a SootMethod for a method that is needed in
 * the target language of some Jimple transformation.
 *
 * @author Matt Dwyer &lt;dwyer@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:26 $
 */
public class JavaLang {

    private static SootClass jltClass;

    public static void init(SootClassManager scm) {
	if (scm.managesClass("java.lang.Thread")) {
	    jltClass = scm.getClass("java.lang.Thread");

	    if (!jltClass.declaresMethod(
					 "<init>", new LinkedList(), VoidType.v())) {
		SootMethod method = 
		    new SootMethod("<init>", new LinkedList(), 
				   VoidType.v(), Modifier.PUBLIC);
		jltClass.addMethod(method);
	    }
	} else {
	    jltClass = 
		new SootClass("java.lang.Thread", Modifier.PUBLIC);

	    SootMethod method = new SootMethod(
					       "<init>", new LinkedList(), 
					       ca.mcgill.sable.soot.VoidType.v(), 
					       Modifier.PUBLIC);
	    jltClass.addMethod(method);

	    scm.addClass(jltClass);
	}
    }
}
