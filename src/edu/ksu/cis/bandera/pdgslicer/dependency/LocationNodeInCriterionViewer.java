package edu.ksu.cis.bandera.pdgslicer.dependency;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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

/**
 * Insert the type's description here.
 * Creation date: (00-7-5 11:16:36)
 * @author: 
 */
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
public class LocationNodeInCriterionViewer {
	SootMethod sootMethod;
	Annotation stmtAnnotation;
	SlicePoint slicePoint;
/**
 * LocationNode constructor comment.
 */
public LocationNodeInCriterionViewer() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-5 11:19:08)
 * @param sm ca.mcgill.sable.soot.SootMethod
 * @param sa edu.ksu.cis.bandera.annotation.Annotation
 * @param sp edu.ksu.cis.bandera.pdgslicer.datastructure.SlicePoint
 */
public LocationNodeInCriterionViewer(SootMethod sm, Annotation sa, SlicePoint sp) {
	sootMethod = sm;
	stmtAnnotation = sa;
	slicePoint = sp;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-5 11:20:07)
 * @return java.lang.String
 */
public String toString() {
	return stmtAnnotation + " :: " + sootMethod;
}
}
