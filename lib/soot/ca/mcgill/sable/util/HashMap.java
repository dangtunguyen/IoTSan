package ca.mcgill.sable.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SableUtil, a clean room implementation of the Collection API.     *
 * Copyright (C) 1997, 1998, 1999 Raja Vallee-Rai                    *
 * (kor@sable.mcgill.ca).  All rights reserved.                      *
 *                                                                   *
 * This work was done as a project of the Sable Research Group,      *
 * School of Computer Science, McGill University, Canada             *
 * (http://www.sable.mcgill.ca/).  It is understood that any         *
 * modification not identified as such is not covered by the         *
 * preceding statement.                                              *
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
 * License along with this library; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other Sable Research Group projects, please      *
 * visit the web site: http://www.sable.mcgill.ca/                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*
 Reference Version
 -----------------
 This is the latest official version on which this file is based.
 The reference version is: $SableUtilVersion: 1.11 $

 Change History
 --------------
 A) Notes:

 Please use the following template.  Most recent changes should
 appear at the top of the list.

 - Modified on [date (March 1, 1900)] by [name]. [(*) if appropriate]
   [description of modification].

 Any Modification flagged with "(*)" was done as a project of the
 Sable Research Group, School of Computer Science,
 McGill University, Canada (http://www.sable.mcgill.ca/).

 You should add your copyright, using the following template, at
 the top of this file, along with other copyrights.

 *                                                                   *
 * Modifications by [name] are                                       *
 * Copyright (C) [year(s)] [your name (or company)].  All rights     *
 * reserved.                                                         *
 *                                                                   *

 B) Changes:

 - Modified on January 20, 1999 by Raja Vallee-Rai (rvalleerai@sable.mcgill.ca). (*)
   Changed the entries implementation to be linear.


 - Modified on July 21, 1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   Renamed this class to HashMap to avoid conflicts.

 - Modified on June 15, 1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   First release of this file.
*/

public class HashMap extends AbstractMap
{
	java.util.Hashtable table;

	private class HashEntry implements Map.Entry
	{
		private Object key;

		public HashEntry(Object key)
		{
			this.key = key;
		}

		public boolean equals(Object obj)
		{
			if(!(obj instanceof Map.Entry))
				return false;
			else {
				Map.Entry e = (Map.Entry) obj;

				return e.getKey().equals(key) && getValue().equals(e.getValue());
			}
		}

		public Object getKey()
		{
			return key;
		}

		public Object getValue()
		{
			return get(key);
		}

		public Object setValue(Object obj)
		{
			return put(key, obj);
		}

		public int hashCode()
		{
			return (getKey()==null ? 0 : getKey().hashCode()) ^ (getValue()==null ? 0 :
				getValue().hashCode());
		}

		public String toString()
		{
			return key.toString() + " -> " + get(key);
		}
	}

	public HashMap()
	{
		table = new java.util.Hashtable();
	}
	public HashMap(int initialCapacity)
	{
		table = new java.util.Hashtable(initialCapacity);
	}
	public HashMap(int initialCapacity, float loadFactor)
	{
		if(initialCapacity <= 0)
			throw new RuntimeException("initialCapacity is " + initialCapacity);

		if(loadFactor <= 0)
			throw new RuntimeException("loadFactor is " + initialCapacity);


		table = new java.util.Hashtable(initialCapacity, loadFactor);
	}
	public void clear()
	{
		table.clear();
	}
	public boolean contains(Object value)
	{
		return table.contains(value);
	}
	public boolean containsKey(Object key)
	{
		if(key == null)
			throw new RuntimeException("Hey hey!");

		return table.containsKey(key);
	}
	/**
	 * This implementation does not correspond exactly to the 1.2 definition.  The collection
	 * which is returned is not backed by the def'n.
	 */

	public Collection entries()
	{
		java.util.Enumeration entries = table.keys();
		/*Set keySet = new VectorSet();

		while(entries.hasMoreElements())
			keySet.add(new HashEntry(entries.nextElement()));
		  */

		List keyList = new ArrayList();

		while(entries.hasMoreElements())
			keyList.add(new HashEntry(entries.nextElement()));

		return keyList;
	}
	public Object get(Object entry)
	{
		return table.get(entry);
	}
	public boolean isEmpty()
	{
		return table.size() == 0;
	}
	public Set keySet()
	{
		java.util.Enumeration keys = table.keys();
		Set keySet = new HashSet();

		while(keys.hasMoreElements())
			keySet.add(keys.nextElement());

		return keySet;
	}
	public Object put(Object key, Object value)
	{
		return table.put(key, value);

	/*
		Object previousEntry = null;

		if(table.containsKey(key))
			previousEntry = table.get(key);

		table.put(key, value);

		return previousEntry; */
	}
	public Object remove(Object obj)
	{
		return table.remove(obj);
	}
	public int size()
	{
		return table.size();
	}
}
