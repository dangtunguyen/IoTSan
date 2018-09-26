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
public class ChooseExpr extends Constant implements Expr {
	protected List choices; // The list of choices
	protected Type type; // The type

  /**
   * Constructs a new ChooseExpr.
   *
   * @param l the list of choices.
   */
  public ChooseExpr(List l)
  {
	choices = l;
	if (l.size() > 0)
	  type = ((Value)l.get(0)).getType();
	else
	  type = null;
  }  
/**
   * Constructs a new ChooseExpr.
   *
   * @param l the list of choices.
   */
public ChooseExpr(Vector v) {
	choices = new VectorList();
	for (int i = 0; i < v.size(); i++)
		choices.add(v.elementAt(i));
	if (choices.size() > 0)
		type = ((Value) choices.get(0)).getType();
	else
		type = null;
}
  public void apply(Switch sw)
  {
	((BanderaExprSwitch)sw).caseChooseExpr(this);
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
   * Gets the list of choices.
   */
  public List getChoices()
  {
	return choices;
  }  
  /**
   * Gets the type.
   */
  public Type getType()
  {
	return type;
  }  
  /**
   *  Gets the use boxes.
   */
  public List getUseBoxes()
  {
	return new ArrayList();
  }  
  /**
   * Sets the type.
   */
  public void setType(Type t)
  {
	type = t;
  }  
  public String toBriefString()
	{
	  return toString();
	}
  public String toString()
  {
	return "Choose(" + choices + ")";
  }  
}
