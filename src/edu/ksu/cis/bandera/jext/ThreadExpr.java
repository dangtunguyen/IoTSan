package edu.ksu.cis.bandera.jext;

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
import java.util.Vector;

//ChooseExpr.java
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

/**
 * This class represents the Choose expression created for ABPS.  It
 * has a list of choices that can be chosen from
 * nondeterministically.  The apply method is not yet implemented.
 *
 * @author <a href="mailto:laubach@cis.ksu.edu">Shawn Laubach</a>
 *
 * @version 0.1
 */
public class ThreadExpr extends Constant implements Expr {
	private static ThreadExpr te;
/**
 * 
 */
private ThreadExpr() {}
  public void apply(Switch sw)
  {
	((BanderaExprSwitch)sw).caseThreadExpr(this);
  }      
/**
 * Insert the method's description here.
 * Creation date: (4/13/00 2:05:35 PM)
 * @return boolean
 * @param c ca.mcgill.sable.soot.jimple.Constant
 */
public boolean equals(Constant c) {
	return false;
}
/**
 * 
 * @return ca.mcgill.sable.soot.Type
 */
public Type getType() {
	return RefType.v("java.lang.Thread");
}
  /**
   *  Gets the use boxes.
   */
  public List getUseBoxes()
  {
	return new ArrayList();
  }    
  public String toBriefString()
	{
	  return toString();
	}
/**
 *
 * @return edu.ksu.cis.bandera.jext.ThreadExpr
 */
public static ThreadExpr v() {
	if (te == null) te = new ThreadExpr();
	return te;
}
}
