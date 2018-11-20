package edu.ksu.cis.bandera.abstraction.typeinference;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
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
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import java.util.*;
public class TypeDependencyGraph {
	protected Hashtable typeTypeVarTable = new Hashtable();
	protected Hashtable children;
	protected Hashtable parents;
	protected UnionFindSet ufs;
/**
 * Graph constructor comment.
 */
public TypeDependencyGraph() {
	ufs = new UnionFindSet();
	parents = new Hashtable();
	children = new Hashtable();
}
/**
 * Insert the method's description here.
 * Creation date: (8/14/00 8:24:55 PM)
 * @param o java.lang.Object
 */
public void add(TypeVariable o) {
	ufs.add(o);
}
/**
 * Insert the method's description here.
 * Creation date: (8/16/00 1:33:59 PM)
 * @param parent java.lang.Object
 * @param child java.lang.Object
 */
public void addChild(TypeVariable parent, TypeVariable child) {
	TypeVariable p = (TypeVariable) ufs.find(parent);
	TypeVariable c = (TypeVariable) ufs.find(child);
	if (p == null || c == null) {
		System.out.println("Parent or child is null.");
		return;
	} else
		if (p != c) {
			Set s = (Set) children.get(p);
			if (s == null) {
				s = new HashSet();
				children.put(p, s);
			}
			s.add(c);
			s = (Set) parents.get(c);
			if (s == null) {
				s = new HashSet();
				parents.put(c, s);
			}
			s.add(p);
		}
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 3:19:02 PM)
 */
public TypeVariable addType(Object type) {
	TypeVariable typeVar;
	if ((typeVar = (TypeVariable) typeTypeVarTable.get(type)) == null) {
		typeVar = new TypeVariable(type);
		typeVar.setAST(type);
		typeTypeVarTable.put(type, typeVar);
		add(typeVar);
		ufs.setAttribute(typeVar, type);
	}
	return typeVar;
}
/**
 * Insert the method's description here.
 * Creation date: (8/16/00 1:37:04 PM)
 * @param o1 java.lang.Object
 * @param o2 java.lang.Object
 */
public void combine(TypeVariable o1, TypeVariable o2) {
	o1 = (TypeVariable) ufs.find(o1);
	o2 = (TypeVariable) ufs.find(o2);
	if (o1 != o2) {
		ufs.union(o1, o2);
		TypeVariable o3 = (TypeVariable) ufs.find(o1);
		if (o3 == o1) {
			o3 = o2;
		} else
			if (o3 == o2) {
				o3 = o1;
				o1 = o2;
			} else
				System.out.println("How did I get here:  TDG.combine");
		Set s1 = (Set) children.get(o1);
		Set s2 = (Set) children.get(o3);
		if (s1 == null)
			if (s2 == null) {
				s1 = new HashSet();
				children.put(o1, s1);
				s1.remove(o1);
			} else {
				children.put(o1, s2);
				s2.remove(o3);
			} else
				if (s2 == null) {
					// Do Nothing
				} else {
					s1.addAll(s2);
					s1.remove(o1);
					s1.remove(o3);
				}
		Iterator i;
		if (s2 != null) {
			i = s2.iterator();
			while (i.hasNext()) {
				s1 = (Set) parents.get(o2 = (TypeVariable) i.next());
				s1.remove(o3);
				if (o2 != o1)
					s1.add(o1);
			}
		}
		s1 = (Set) parents.get(o1);
		s2 = (Set) parents.get(o3);
		if (s1 == null)
			if (s2 == null) {
				s1 = new HashSet();
				parents.put(o1, s1);
				s1.remove(o1);
			} else {
				parents.put(o1, s2);
				s2.remove(o3);
			} else
				if (s2 == null) {
					// Do Nothing
				} else {
					s1.addAll(s2);
					s1.remove(o1);
					s1.remove(o3);
				}
		if (s2 != null) {
			i = s2.iterator();
			while (i.hasNext()) {
				s1 = (Set) children.get(o2 = (TypeVariable) i.next());
				s1.remove(o3);
				if (o2 != o1)
					s1.add(o1);
			}
		}
		children.remove(o3);
		parents.remove(o3);
	}
}
/**
 * 
 * @param marked java.util.HashSet
 * @param o java.lang.Object
 * @param cycle java.util.Vector
 */
private Vector dfsCycle(HashSet marked, Object o, Vector path) {
	marked.add(o);
	path.add(ufs.find(o));
	for (Iterator i = getChildren((TypeVariable) o).iterator(); i.hasNext();) {
		Object child = i.next();
		if (path.contains(child)) return path;
		if (!marked.contains(child)) {
			Vector cycle = dfsCycle(marked, child, path);
			if (cycle != null) return cycle;
		}
	}
	path.remove(path.size() - 1);
	return null;
}
/**
 * Compares two objects for equality. Returns a boolean that indicates
 * whether this object is equivalent to the specified object. This method
 * is used when an object is stored in a hashtable.
 * @param obj the Object to compare with
 * @return true if these Objects are equal; false otherwise.
 * @see java.util.Hashtable
 */
public boolean equals(Object obj) {
	// Insert code to compare the receiver and obj here.
	// This implementation forwards the message to super.  You may replace or supplement this.
	// NOTE: obj might be an instance of any class
	return super.equals(obj);
}
/**
 * Insert the method's description here.
 * Creation date: (8/14/00 8:31:35 PM)
 * @return java.util.Set
 * @param o java.lang.Object
 */
public Set getChildren(TypeVariable o) {
	Set s = (Set) children.get(ufs.find(o));
	if (s == null)
		s = new HashSet();
	return s;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 3:38:19 PM)
 * @return java.util.Set
 */
public Set getNotSingleConnected() {
	UnionFindSet u = new UnionFindSet();
	for (Enumeration e = children.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		u.add(key);
		for (Iterator i = ((Set) children.get(key)).iterator(); i.hasNext();) {
			Object data = i.next();
			u.add(data);
			u.union(key, data);
		}
	}
	return u;
}
/**
 * Insert the method's description here.
 * Creation date: (8/14/00 8:31:35 PM)
 * @return java.util.Set
 * @param o java.lang.Object
 */
public Set getParents(TypeVariable o) {
	Set s = (Set) parents.get((TypeVariable) ufs.find(o));
	if (s == null)
		s = new HashSet();
	return s;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 3:32:00 PM)
 * @return ca.mcgill.sable.soot.Type
 * @param o java.lang.Object
 */
public Object getType(TypeVariable o) {
	return ufs.getAttribute(o);
}
/**
 * 
 * @return edu.ksu.cis.bandera.abps.typing.TypeVariable
 * @param t ca.mcgill.sable.soot.Type
 */
public TypeVariable getTypeVariable(Object t) {
	return (TypeVariable) typeTypeVarTable.get(t);
}
/**
 * Generates a hash code for the receiver.
 * This method is supported primarily for
 * hash tables, such as those provided in java.util.
 * @return an integer hash code for the receiver
 * @see java.util.Hashtable
 */
public int hashCode() {
	// Insert code to generate a hash code for the receiver here.
	// This implementation forwards the message to super.  You may replace or supplement this.
	// NOTE: if two objects are equal (equals(Object) returns true) they must have the same hash code
	return super.hashCode();
}
/**
 * Insert the method's description here.
 * Creation date: (8/15/00 12:20:55 PM)
 * @return boolean
 * @param o java.lang.Object
 */
public boolean isLeaf(TypeVariable o) {
	Set s = (Set) children.get(o);
	return s == null || s.size() == 0;
}
/**
 * Insert the method's description here.
 * Creation date: (8/15/00 12:20:55 PM)
 * @return boolean
 * @param o java.lang.Object
 */
public boolean isParent(TypeVariable o) {
	Set s = (Set) parents.get(o);
	return s == null || s.size() == 0;
}
/**
 * 
 * @param tv1 edu.ksu.cis.bandera.abps.typing.TypeVariable
 * @param tv2 edu.ksu.cis.bandera.abps.typing.TypeVariable
 */
public void quickCombine(TypeVariable tv1, TypeVariable tv2) {
	ufs.union(tv1, tv2);
}
/**
 * 
 */
public void removeCycles() {
	int count = 0;
	for (Iterator i = getNotSingleConnected().iterator(); i.hasNext();) {
		HashSet marked = new HashSet();
		for (Iterator j = ((Set) i.next()).iterator(); j.hasNext();) {
			Object o = j.next();
			if (!marked.contains(o)) {
				boolean f = true;
				while (f) {
					HashSet localMarked = new HashSet();
					Vector cycle = dfsCycle(localMarked, o, new Vector());
					if (cycle != null) {
						if (cycle.size() > 1) {
							f = true;
							count++;
							o = cycle.remove(0);
							for (Iterator k = cycle.iterator(); k.hasNext();) {
								combine((TypeVariable) o, (TypeVariable) k.next());
							}
						} else {
							f = false;
							marked.addAll(localMarked);
						}
					} else {
						f = false;
						marked.addAll(localMarked);
					}
				}
			}
		}
	}
	//System.out.println("  Cycles removed: " + count);
}
/**
 * Returns a String that represents the value of this object.
 * @return a string representation of the receiver
 */
public String toString() {
	return ufs.toString();
}
}
