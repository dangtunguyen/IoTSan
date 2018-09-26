package edu.ksu.cis.bandera.abstraction.util;

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
import java.util.*;

public class UnionFindSet implements Set {
	private Hashtable elements = new Hashtable();
	private Hashtable partAttrTable = new Hashtable();
	private class Element {
		int rank;
		Element parent;
		Object object;
		Element(int rank, Object object) {
			this.rank = rank;
			this.parent = this;
			this.object = object;
		}
	}
/**
 * 
 */
public UnionFindSet() {}
/**
 * 
 */
public UnionFindSet(Collection c) {
	for (Iterator i = new HashSet(c).iterator(); i.hasNext();) {
		Object o = i.next();
		elements.put(o, new Element(0, o));
	}
}
/**
 * 
 * @return boolean
 * @param o java.lang.Object
 */
public boolean add(Object o) {
	if (find(o) == null) {
		elements.put(o, new Element(0, o));
		return true;
	} else {
		return false;
	}
}
/**
 * 
 * @return boolean
 * @param c java.util.Collection
 */
public boolean addAll(Collection c) {
	boolean result = false;
	for (Iterator i = c.iterator(); i.hasNext();) {
		result = result || add(i.next());
	}
	return result;
}
/**
 * 
 * @return boolean
 * @param c java.util.Collection
 * @param o java.lang.Object
 */
public boolean addAllTo(Collection c, Object orep) {
	boolean result = false;
	for (Iterator i = c.iterator(); i.hasNext();) {
		result = result || addTo(i.next(), orep);
	}
	return result;
}
/**
 * 
 * @return boolean
 * @param o java.lang.Object
 * @param orep java.lang.Object
 */
public boolean addTo(Object o, Object orep) {
	if (add(o)) {
		union(o, orep);
	}
	return false;
}
/**
 * 
 */
public void clear() {
	elements = new Hashtable();
	partAttrTable = new Hashtable();
}
/**
 * 
 * @return boolean
 * @param o java.lang.Object
 */
public boolean contains(Object o) {
	return toSet().contains(o);
}
/**
 * 
 * @return boolean
 * @param c java.util.Collection
 */
public boolean containsAll(Collection c) {
	return toSet().containsAll(c);
}
/**
 * 
 * @return java.lang.Object
 * @param o java.lang.Object
 */
public Object find(Object o) {
	try {
		Element e = (Element) elements.get(o);
		if (e == null) return null;
		Element above = e.parent;
		while (above != above.parent) {
			e.parent = above.parent;
			above = above.parent.parent;
		}
		return above.object;
	} catch (Exception e) {
		return null;
	}
}
/**
 * 
 * @return java.lang.Object
 * @param o java.lang.Object
 */
public Object getAttribute(Object o) {
	o = find(o);
	if (o == null) return null;
	else return partAttrTable.get(o);
}
/**
 * 
 * @return boolean
 */
public boolean isEmpty() {
	return elements.size() == 0;
}
/**
 * 
 * @return java.util.Iterator
 */
public Iterator iterator() {
	return toSet().iterator();
}
/**
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args) {
	if (args.length != 2) {
		System.out.println("needs two positive-integer arguments");
		return;
	}
	
	UnionFindSet ufs = new UnionFindSet();

	int size = Integer.parseInt(args[0]);
	int cmod = Integer.parseInt(args[1]);

	Integer[] ints = new Integer[size];

	for (int i = 0; i < size; i++) {
		ufs.add(ints[i] = new Integer(i));
	}

	System.out.println(ufs.toString());

	for (int i = 0; i < size; i++) {
		for (int j = 0; j < cmod; j++) {
			if (i % cmod == j) {
				ufs.union(ints[j], ints[i]);
			}
		}
	}

	System.out.println(ufs);

	for (int i = 0; i < size; i++) {
		System.out.println(ints[i] + " is represented as " + ufs.find(ints[i]));
	}
}
/**
 * 
 * @return boolean
 * @param object java.lang.Object
 */
public boolean remove(Object object) {
	try {
		Element e = (Element) elements.get(object);
		if (e.parent == e) {
			Element top = null;
			for (Enumeration enumVar = elements.elements(); enumVar.hasMoreElements();) {
				Element e2 = (Element) enumVar.nextElement();
				if (e2.parent == e) {
					if (top == null) {
						top = e2;
						top.rank = e.rank;
					}
					e2.parent = top;
				}
			}
			partAttrTable.put(top.object, partAttrTable.get(e.object));
		} else {
			for (Enumeration enumVar = elements.elements(); enumVar.hasMoreElements();) {
				Element e2 = (Element) enumVar.nextElement();
				if (e2.parent == e) {
					e2.parent = e.parent;
				}
			}
		}
		elements.remove(object);
		return true;
	} catch (Exception e) {
		return false;
	}
}
/**
 * 
 * @return boolean
 * @param c java.util.Collection
 */
public boolean removeAll(Collection c) {
	boolean result = false;
	for (Iterator i = c.iterator(); i.hasNext();) {
		result = result || remove(i.next());
	}
	return result;
}
/**
 * 
 * @return boolean
 * @param c java.util.Collection
 */
public boolean retainAll(Collection c) {
	boolean result = false;
	for (Enumeration e = elements.elements(); e.hasMoreElements();) {
		Object o = e.nextElement();
		if (!c.contains(o)) {
			remove(o);
		}
	}
	return result;
}
/**
 * 
 * @return boolean
 * @param o java.lang.Object
 * @param attr java.lang.Object
 */
public boolean setAttribute(Object o, Object attr) {
	o = find(o);
	if (o == null) return false;
	partAttrTable.put(o, attr);
	return true;
}
/**
 * 
 * @return int
 */
public int size() {
	return toSet().size();
}
/**
 * 
 * @return java.lang.Object[]
 */
public Object[] toArray() {
	return toSet().toArray();
}
/**
 * 
 * @return java.lang.Object[]
 * @param objects java.lang.Object[]
 */
public Object[] toArray(Object[] objects) {
	return null;
}
/**
 * 
 * @return java.util.Set
 */
private Set toSet() {
	Hashtable table = new Hashtable();
	for (Enumeration e = elements.elements(); e.hasMoreElements();) {
		Element el = (Element) e.nextElement();
		Element rel = (Element) elements.get(find(el.object));
		if (table.get(rel) == null) {
			table.put(rel, new HashSet());
		}
		((HashSet) table.get(rel)).add(el.object);
	}
	HashSet set = new HashSet();
	for (Enumeration e = table.elements(); e.hasMoreElements();) {
		set.add(e.nextElement());
	}
	return set;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	Set set = toSet();
	StringBuffer temp = new StringBuffer(set.size() + "= {\n");
	for (Iterator i = set.iterator(); i.hasNext();) {
		Set s = (Set) i.next();
		
		temp.append("(" + getAttribute(s.iterator().next()) + ", " + s + "),\n");
	}
	temp.append("}\n");
	return temp.toString();
}
/**
 *
 * @return boolean
 * @param o1 java.lang.Object
 * @param o2 java.lang.Object
 */
public boolean union(Object o1, Object o2) {
	o1 = find(o1);
	o2 = find(o2);
	if ((o1 == null) || (o2 == null))
		return false;
	if (getAttribute(o1) != null && getAttribute(o2) != null)
		if (getAttribute(o1) != getAttribute(o2))
			System.out.println("Warning: trying to merge two partitions that have different attributes");
	Object attr = getAttribute(o1);
	if (attr == null)
		attr = getAttribute(o2);
	partAttrTable.remove(o1);
	partAttrTable.remove(o2);
	try {
		Element e1 = (Element) elements.get(o1);
		Element e2 = (Element) elements.get(o2);
		if (e1.rank >= e2.rank) {
			e2.parent = e1;
			if (e1.rank == e2.rank) {
				e1.rank++;
			}
		} else {
			e1.parent = e2;
		}
	} catch (Exception e) {
		return false;
	}
	o1 = find(o1);
	if (attr != null)
		partAttrTable.put(o1, attr);
	return true;
}
}
