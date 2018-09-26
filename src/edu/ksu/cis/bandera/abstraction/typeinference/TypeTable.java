package edu.ksu.cis.bandera.abstraction.typeinference;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
 * All rights reserved.                                              *
 *                                                                   *
 * Modifications by Robby (robby@cis.ksu.edu) are                    *
 * Copyright (C) 2000 Robby.  All rights reserved.                   *
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
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;
public class TypeTable {
	private RefHashTable table;
/**
 * TypeTable constructor comment.
 */
public TypeTable() {
	super();
	table = new RefHashTable();
}
public Abstraction get(Object key) {
	return (Abstraction) table.get(key);
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/00 3:26:49 PM)
 */
public Set keySet() {
	Set l = new HashSet();
	Iterator i = table.keySet().iterator();
	while (i.hasNext()) {
		l.add(i.next());
	}
	return l;
}
public void put(Object key, Object type) {
	table.put(key, type);
}
/**
 * 
 * @return int
 */
public int size() {
	return table.size();
}
/**
 * Insert the method's description here.
 * Creation date: (5/19/00 10:00:21 AM)
 * @return java.lang.String
 */
public String toString() {
	String s = "TypeTable \n";
	Iterator i = table.keySet().iterator();
	while (i.hasNext()) {
		Object o = i.next();
		s += o + "\t" + table.get(o) + "\n";
	}
	return s;
}
}
