package edu.ksu.cis.bandera.pdgslicer;

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

import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
/**
 * This is for information of slice criterion.
 */
public class SliceCriterion {
	/**
	   * A map from {@link Stmt Stmt} to a {@link Set Set} of
	   * {@link Value Value} representing all relevant variables 
	   * for every statement.
	   */
	private Map relVarMap = new HashMap();
	/**
	   * A set of {@link SlicePoint SlicePoint}.
	   */
	private Set slicePoints;
	/**
	   * A set of {@link SliceVariable SliceVariable}
	   */
	private Set sliceVars;
	private Set sliceStatements;
/**
   * @param p a set of slice points.
   * @param v a set of slice variables.
   */
public SliceCriterion(Set p, Set v, Set s) {
	this.slicePoints = p;
	this.sliceVars = v;
	this.sliceStatements = s;
}
Set getSlicePoints() {
	return slicePoints;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-4 16:34:27)
 * @return ca.mcgill.sable.util.Set
 */
Set getSliceStatements() {
	return sliceStatements;
}
Set getSliceVars() {
	return sliceVars;
}
Map relVarMap() {
	return relVarMap;
}
void setRelVarMap(Map m) {
	relVarMap = m;
}
  public String toString()
	{
	  String s;
	  
	  s = "slicePoint: " + this.slicePoints + " sliceVars: " + this.sliceVars;
	  return s;
	}
}
