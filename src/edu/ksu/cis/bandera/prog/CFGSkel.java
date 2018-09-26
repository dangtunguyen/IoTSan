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
//CFGSkel.java
import java.util.Vector;
import edu.ksu.cis.bandera.util.*;

/**
 * Control flow graph skeleton.  This class represents the flow graph
 * and allows a topological search to be performed.
 *
 * @author <a href="mailto:laubach@cis.ksu.edu">Shawn Laubach</a>
 *
 * @version 0.1
 */
public class CFGSkel
{
  /**
   * Internal node.  Keeps the marking, the index, and the
   * descendents.
   */
  class node
  {
	boolean marked;		
	Index index;
	Vector descendents;

	/**
	 * Constructs a new node with false marking.
	 *
	 * @param i the index to put in the node.
	 */
	node(Index i)
	{
	  index = i;
	  marked = false;
	  descendents = new Vector();
	}

	/**
	 * Checks the equality of a node and either another node or an
	 * index.  This is because the main distinguishing part of a node
	 * is the index.
	 */
	public boolean equals(Object o)
	{
	  if (o instanceof node)                    // If object is a node
		return index.equals(((node) o).index); // check the index
	  else if (o instanceof Index) // If object is an index
		return index.equals(o);                // compare indexes
	  else			                   // else
		return false;		           // return false
	}

	public String toString()
	{
	  if (marked)
		return index + ": * --> " + descendents;
	  else
		return index + ": o --> " + descendents;
	}
  }

  /**
   * The list of nodes that comprise the skeleton.  The first one is
   * the head of the skeleton.
   */
  protected Vector skel;

  /**
   * Constructs a new CFG skeleton.  It just creates an empty node
   * list.
   */
  public CFGSkel()
  {
	skel = new Vector();
  }  
  /**
   * Adds a new index to the skeleton.
   *
   * @param i the index to add.
   */
  public void add(Index i)
  {
	skel.addElement(new node(i));
  }  
  /**
   * Adds a descendent to a node.
   *
   * @param p the parent node.
   * @param c the child node.
   */
  public void addDescendent(Index p, Index c)
  {
	int i;
	node n;

	i = find(p);
	if (i < 0)
	  {
		System.out.println("No index found:  " + p);
		return;
	  }

	n =(node) skel.elementAt(i);

	i = find(c);

	if (i < 0)
	  {
		add(c);
		n.descendents.addElement(new Integer(find(c)));
	  }
	else
	  {
		if (n.descendents.indexOf(new Integer(i)) < 0)
		  n.descendents.addElement(new Integer(i));
	  }
  }  
  /**
   * Finds the position of a node.
   *
   * @param i the index to find.
   * 
   * @return The position of the node.
   */
  int find(Index i)
  {
	return skel.indexOf(new node(i));
  }  
  /**
   * Creates a new arc.
   *
   * @param p the parent (start) of the arc.
   * @param c the child of the arc.
   */
  public void makeArc(Index p, Index c)
  {
	addDescendent(p, c);
  }  
  /**
   * Makes an arc and sets the mark of the child to mark.
   *
   * @param p the parent.
   * @param c the child.
   * @param mark the child's mark.
   */
  public void makeArc(Index p, Index c, boolean mark)
  {
	addDescendent(p, c);
	mark(c, mark);
  }  
  /**
   * Marks the index.
   *
   * @param i the index to mark.
   */
  public void mark(Index i)
  {
	node n;

	if (i == null)
	  {
		System.out.println("Index is null.");
		return;
	  }
	if (find(i) < 0)
	  System.out.println("Index " + i + " not found.");
	else
	  {
		n =(node) skel.elementAt(find(i));
		n.marked = true;
	  }
  }  
  /**
   * Sets the mark of an index to what is passed.
   *
   * @param i the index.
   * @param m the value to set the mark.
   */
  public void mark(Index i, boolean m)
  {
	node n;

	if (i == null)
	  {
		System.out.println("Index is null.");
		return;
	  }
	if (find(i) < 0)
	  System.out.println("Index " + i + " not found.");
	else
	  {
		n =(node) skel.elementAt(find(i));
		n.marked = m;
	  }
  }  
  /**
   * Gets the next unmarked index using a topological sort.
   *
   * @return The next unmarked index.
   */
  public Index next()
  {
	int i,
	  j;
	Vector q = new Vector();
	Vector visit = new Vector();
	node n;

	q.addElement(new Integer(0));

	for(i = 0; i < q.size(); i++)
	  {
		n =(node) skel.elementAt(((Integer) q.elementAt(i)).intValue());
		if (n.marked == false)
		  return n.index;
		else if (!visit.contains(q.elementAt(i)))
		  {
			visit.addElement(q.elementAt(i));
			for(j = 0; j < n.descendents.size(); j++)
			  q.addElement(n.descendents.elementAt(j));
		  }
	  }

	return null;
  }  
  /**
   * Sets the index to the starting point.
   *
   * @param i the index.
   */
  public void start(Index i)
  {
	int ind = find(i);

	if (ind == 0)
	  unmark(i);
	else
	  {
		skel.insertElementAt(new node(i), 0);
	  }
  }  
  public String toString()
  {
	int i;
	String s = "";

	for(i = 0; i < skel.size(); i++)
	  s += i + ":  " + skel.elementAt(i) + "\n";

	return s;
  }  
  /**
   * Unmarks an index.
   *
   * @param i the index.
   */
  public void unmark(Index i)
  {
	node n;

	if (i == null)
	  {
		System.out.println("Index is null.");
		return;
	  }
	if (find(i) < 0)
	  System.out.println("Index " + i + " not found.");
	else
	  {
		n =(node) skel.elementAt(find(i));
		n.marked = false;
	  }
  }  
}
