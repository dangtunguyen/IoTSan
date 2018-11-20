package edu.ksu.cis.bandera.abstraction;

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
import edu.ksu.cis.bandera.abstraction.util.*;
public class CoercionManager {
	private Hashtable coercions;
	private Hashtable reverse;
/**
 * 
 */
public CoercionManager(Collection abstractionNames) {
	coercions = new Hashtable();
	reverse = new Hashtable();
	
	
	for (Iterator i = abstractionNames.iterator(); i.hasNext();) {
		Abstraction a = (Abstraction) AbstractionClassLoader.invokeMethod((String) i.next(), "v", new Class[0], null, new Object[0]);
		if (a instanceof IntegralAbstraction) {
			add(edu.ksu.cis.bandera.abstraction.lib.integral.Point.v(), a);
			add(a, ConcreteIntegralAbstraction.v());
		} else if (a instanceof RealAbstraction) {
			add(edu.ksu.cis.bandera.abstraction.lib.real.Point.v(), a);
			add(a, ConcreteRealAbstraction.v());
			((Set) coercions.get(ConcreteIntegralAbstraction.v())).add(a);
			((Set) reverse.get(a)).add(ConcreteIntegralAbstraction.v());
		}
	}
	
	((Set) coercions.get(ConcreteIntegralAbstraction.v())).add(ConcreteRealAbstraction.v());
	HashSet set = new HashSet();
	set.add(ConcreteIntegralAbstraction.v());
	reverse.put(ConcreteRealAbstraction.v(), set);
}
public void add(edu.ksu.cis.bandera.abstraction.Abstraction ac, edu.ksu.cis.bandera.abstraction.Abstraction abs) {
	Set l;
	if ((l = (Set) coercions.get(abs)) == null) {
		l = new HashSet();
		coercions.put(abs, l);
	}
	l.add(ac);
	if ((l = (Set) reverse.get(ac)) == null) {
		l = new HashSet();
		reverse.put(ac, l);
	}
	l.add(abs);
	process();
}
public boolean coerciable(edu.ksu.cis.bandera.abstraction.Abstraction from, edu.ksu.cis.bandera.abstraction.Abstraction to) {
	if (from.equals(to)) return true;
	Set l = (Set) coercions.get(from);
	if (l == null)
		return false;
	else
		return l.contains(to);
}
public Set get(edu.ksu.cis.bandera.abstraction.Abstraction t) {
	Set s = (Set) coercions.get(t);

	// robbyjo's patch begin
	if (s == null)
	{
		HashSet temp = new HashSet();
		// We won't do class Abstraction yet, so up to Object
		if (t instanceof ClassAbstraction)
		{
			temp.add(ClassAbstraction.v("java.lang.Object"));
		}
		return temp;
	}
	// robbyjo's patch end
	else
		return s;
}
/**
 * Insert the method's description here.
 * Creation date: (8/17/00 11:39:54 PM)
 * @return ca.mcgill.sable.soot.Type
 * @param s java.util.Set
 */
public edu.ksu.cis.bandera.abstraction.Abstraction lub(Set types) {
	boolean allCommon, commonLUB;
	Set s, p, t;
	Abstraction childBox, common;
	Iterator it;
	s = null;
	p = null;
	allCommon = true;
	commonLUB = true;
	common = null;
	if (types.size() == 0) {
		System.out.println("What do I do in lub.");
		return null;
	} else {
		for (it = types.iterator(); it.hasNext();) {
			t = new HashSet();
			Object o = it.next();
			childBox = (edu.ksu.cis.bandera.abstraction.Abstraction) o;
			if (childBox != null) {
				t.addAll(get(childBox));
				t.add(childBox);
				if (common == null)
					common = childBox;
				else
					if (common != childBox && !common.equals(childBox)) {
						allCommon = false;
						// robbyjo's patch begin (quick and dirty solution)
						if (common instanceof ClassAbstraction)
						{
							if (childBox instanceof ClassAbstraction || childBox instanceof ArrayAbstraction)
							{
								common = ClassAbstraction.v("java.lang.Object");
							} else
							if (!get(childBox).contains(common))
								if (get(common).contains(childBox))
									common = childBox;
								else
									commonLUB = false;
						} else
						{
							if (common instanceof ArrayAbstraction)
							{
								if (childBox instanceof ClassAbstraction || childBox instanceof ArrayAbstraction)
								{
									common = ClassAbstraction.v("java.lang.Object");
								}
							} else
							if (!get(childBox).contains(common))
								if (get(common).contains(childBox))
									common = childBox;
								else
									commonLUB = false;
						}
						// robbyjo's patch end
					}
			} else {
				allCommon = false;
				commonLUB = false;
			}
			if (s == null) {
				s = new HashSet();
				s.addAll(t);
			} else
				s.retainAll(t);
			if (t.size() > 0)
				if (p == null) {
					p = new HashSet();
					p.addAll(t);
				} else
					p.retainAll(t);
		}
		if (s == null) {
			return null;
		} else
			if (s.size() == 1) {
				return (edu.ksu.cis.bandera.abstraction.Abstraction) s.toArray()[0];
			} else
				if (allCommon) {
					return common;
				} else
					if (commonLUB) {
						return common;
					} else {
						return null;
					}
	}
}
protected void process() {
	boolean change = true;
	while (change) {
		change = false;
		Iterator i = coercions.keySet().iterator();
		while (i.hasNext()) {
			Object a = i.next();
			Set s = (Set) coercions.get(a);
			if (s != null) {
				Iterator j = s.iterator();
				HashSet temp = new HashSet();
				while (j.hasNext()) {
					Object b = j.next();
					Set t = (Set) coercions.get(b);
					if (t != null) {
						temp.addAll(t);
					}
				}
				if (s.addAll(temp))
					change = true;	
			}
		}
	}
	Iterator i = coercions.keySet().iterator();
	while (i.hasNext()) {
		Object a = i.next();
		Set s = (Set) coercions.get(a);
		Iterator j = s.iterator();
		while (j.hasNext()) {
			Object b = j.next();
			Set t = (Set) reverse.get(b);
			t.add(a);
		}
	}
}
}
