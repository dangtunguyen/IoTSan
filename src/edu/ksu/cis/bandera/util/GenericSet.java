package edu.ksu.cis.bandera.util;

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

/**
 * Generic set class.  It is not a fully functional set class yet.  It only has
 * a union method.  It does not construct a new set when union is used.  All
 * will be changed as soon as time permits.  It is not need right now and it
 * should not drastically change the interface, but it might.  It is used in
 * wrapper classes so it shouldn't affect things very much.
 *</P>
 * All elements should be mappable.
 *
 * @author <A HREF="mailto:laubach@cis.ksu.edu">Shawn Laubach</A>
 *
 * @version 0.2
 */
public class GenericSet
{
  Vector set;

  /**
   * Creates a new empty set.
   */
  public GenericSet()
	{
	  set = new Vector();
	}
  /** 
   * Creates a new set with a single element.
   *
   * @param e the single element.
   */
  public GenericSet(Object e)
	{
	  set = new Vector();
	  set.addElement(e);
	}
  public GenericSet(Vector v)
	{
	  set = (Vector)v.clone();
	}
  public void addElemToSet(Object ele)
	{
	  if (!set.contains(ele)) set.addElement(ele);
	}
  /**
   * Checks if an element is in the set.
   *
   * @param m the element
   *
   * @return True if m is an element of the set, otherwise false.
   */
  public boolean contains(Object e)
	{
	  return set.contains(e);
	}
  /**
   * Finds the difference between two sets.
   *
   * @param s another set.
   */
  public GenericSet difference(GenericSet s)
	{
	  int i;
	  GenericSet n = new GenericSet();

	  for (i = 0; i < set.size(); i++)
	if (!s.set.contains(set.elementAt(i)))
	  n.set.addElement(set.elementAt(i));

	  return n;
	}
  public boolean equals(GenericSet another)
	{
	  int i =0;
	  boolean includes = true;
	  
	  while (i<set.size() && includes)
	{
	  includes = another.set.contains(set.elementAt(i));
	  i++;
	}

	  if (! includes) return false;

	  i= 0;

	  while (i<another.set.size() && includes)
	{
	  includes = set.contains(another.set.elementAt(i));
	  i++;
	}
	  return includes;
	}
  /**
   * Intersects another set together.
   *
   * @param s another set.
   */
  public GenericSet intersect(GenericSet s)
	{
	  int i;
	  GenericSet n = new GenericSet();

	  for (i = 0; i < set.size(); i++)
	if (s.set.contains(set.elementAt(i)))
	  n.set.addElement(set.elementAt(i));

	  return n;
	}
  public static void main(String args[])
	{
	  String aa[] = {"a", "b", "c"};
	  GenericSet a = new GenericSet("a");
	  GenericSet b = new GenericSet("b");
	  GenericSet c = a.union(b);
	  GenericSet d = a.union(c);
	  GenericSet e = new GenericSet("c");
	  
	  d.addElemToSet("dddd") ;

	  System.out.println(a + "\n" + b + "\n" + c);
	  System.out.println(d);
	  System.out.println(d.union(e).intersect(b.union(e)));
	  System.out.println(d.union(e).intersect(e.union(a)));
	  System.out.println(d.union(e).difference(a));
	  System.out.println(d.union(e).difference(e));
	  System.out.println(d.union(e).difference(a.union(e)));
	}
  public void remove(Object ele)
	{
	  if (set.contains(ele)) set.removeElement(ele);
	}
  /**
   * Gets the ith element from the set.
   *
   * @param i the element position
   *
   * @return The ith element.
   */
  public Object setRef(int i)
	{
	  return set.elementAt(i);
	}
  /**
   * Returns the number of elements in the set.
   *
   * @return The number of elements in the set.
   */
  public int size()
	{
	  return set.size();
	}
  /**
   * Converts the set to a string
   *
   * @return String representation of the set.
   */
  public String toString()
	{
	  return set.toString();
	}
  /**
   * Unions another set together.
   *
   * @param s another set.
   */
  public GenericSet union(GenericSet s)
	{
	  int i;
	  GenericSet n = new GenericSet(set);
	  
	  for (i = 0; i < s.set.size(); i++)
	if (!n.contains(s.set.elementAt(i)))
	  n.set.addElement(s.set.elementAt(i));
	  
	  return n;
	}
}
