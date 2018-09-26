package edu.ksu.cis.bandera.prog;

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
//BasicBlock.java
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;

/**
 * Holds the basic block and some control flow information.  The
 * structures are the name and a list of statements.  It also keeps
 * two other list of statements to keep track of predecessor and
 * successors.
 *
 * @author <a href="mailto:laubach@cis.ksu.edu">Shawn Laubach</a>
 *
 * @version 0.1
 */
public class BasicBlock {
	/**
	   * Name of the block.
	   */
	protected Index name;

	/**
	   * List of predecessors.
	   */
	protected List preds;

	/**
	   * List of statements in the block.
	   */
	protected ca.mcgill.sable.util.List stmts;

	/**
	   * List of successors in the block.
	   */
	protected List succs;
	protected List goOn;
/**
   * Constructs a new basic block with index n.
   *
   * @param n the name of the basic block.
   */
public BasicBlock(Index n) {
	name = n;
	preds = new ArrayList();
	stmts = new ca.mcgill.sable.util.ArrayList();
	succs = new ArrayList();
}
  /**
   * As a predecessor to the block.
   *
   * @param pred the name of the predecessor.
   */
  public void addPreds(Index pred)
  {
	if (!preds.contains(pred))
	  preds.add(pred);
  }  
  /**
   * Adds a statement to the block.
   *
   * @param stmt the statement to add.
   */
  public void addStmt(Stmt stmt)
  {
	stmts.add(stmt);
  }  
  /**
   * Adds a successor to the block.
   *
   * @param succ the successor name.
   */
  public void addSuccs(Index succ)
  {
	succs.add(succ);
  }  
  /**
   * Changes the name of an index to another name in the predecessor.
   *
   * @param from the index to change.
   * @param to the index to change to.
   */
  public void changePreds(Index from, List to)
  {
	preds.remove(from);
	preds.addAll(to);
  }  
/**
   * Changes the name of an index to another name in the successors.
   *
   * @param from the index to change.
   * @param to the index to change to.
   */
public void changeSuccs(Index from, Index to) {
	while (succs.indexOf(from) != -1)
		succs.set(succs.indexOf(from), to);
}
/**
   * Gets the list of statements.
   *
   * @return The list of statements.
   */
public ca.mcgill.sable.util.List get() {
	return stmts;
}
  /**
   * Get the ith statement from the list.
   *
   * @param i the position of the statement to get.
   *
   * @return The statement at the ith position or null if the position
   * is out of the range of statements.
   */
  public Stmt get(int i)
  {
	if (stmts.size() > i)
	  return (Stmt)stmts.get(i);
	else
	  return null;
  }  
/**
 * Insert the method's description here.
 * Creation date: (4/13/00 2:25:06 PM)
 * @return ca.mcgill.sable.util.List
 */
public List getGoOn() {
	return goOn;
}
  /**
   * Gets the index of a basic block.
   *
   * @return The index of the block.
   */
  public Index getIndex()
  {
	return name;
  }  
  /**
   * Gets the list of predecessors.
   *
   * @return The list of predecessors.
   */
  public List getPreds()
  {
	return preds;
  }  
  /**
   * Gets the ith predecessor.
   *
   * @param i the position of the predecessor.
   *
   * @return The index at position i unless i is out of bounds and
   * then null.
   */
  public Index getPreds(int i)
  {
	if (succs.size() > i)
	  return (Index)preds.get(i);
	else
	  return null;
  }  
  /**
   * Gets the list of successors.
   *
   * @return The list of successors.
   */
  public List getSuccs()
  {
	return succs;
  }  
  /**
   * Gets the ith successor.
   *
   * @param i the position of the successor.
   *
   * @return The index at position i unless i is out of bounds and
   * then null.
   */
  public Index getSuccs(int i)
  {
	if (succs.size() > i)
	  return (Index)succs.get(i);
	else
	  return null;
  }  
/**
 * Insert the method's description here.
 * Creation date: (4/13/00 2:25:06 PM)
 * @param newGoOn ca.mcgill.sable.util.List
 */
public void setGoOn(List newGoOn) {
	goOn = newGoOn;
}
  /**
   * Returns the number of statements.
   *
   * @return The number of statements.
   */
  public int size()
  {
	return stmts.size();
  }  
  public String toString()
  {
	int i;
	String s = preds + "->\n" + name + ":\n";

	for (i = 0; i < stmts.size(); i++)
	  s += stmts.get(i) + "\n";

	s += "-> " + succs + "\n";

	return s;
  }  
}
