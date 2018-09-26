package ca.mcgill.sable.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SableUtil, a clean room implementation of the Collection API.     *
 * Copyright (C) 1997, 1998 Etienne Gagnon (gagnon@sable.mcgill.ca). *
 * All rights reserved.                                              *
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

 - Modified on June 7, 1998 by Etienne Gagnon (gagnon@sable.mcgill.ca). (*)
   Changed the license.
*/

import ca.mcgill.sable.util.Map.Entry;

public abstract class AbstractMap implements Map
{
	private Collection values;
	private Set keys;

	private class ValueCollection extends AbstractCollection
	{
		public int size()
		{
			return entries().size();
		}

		public Iterator iterator()
		{
			return new ValueIterator();
		}
	}

	private class ValueIterator implements Iterator
	{
		private Iterator iterator = entries().iterator();

		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		public Object next()
		{
			return ((Entry) iterator.next()).getValue();
		}

		public void remove()
		{
			iterator.remove();
		}
	}

	private class KeySet extends AbstractSet
	{
		public int size()
		{
			return entries().size();
		}

		public Iterator iterator()
		{
			return new KeyIterator();
		}
	}

	private class KeyIterator implements Iterator
	{
		private Iterator iterator = entries().iterator();

		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		public Object next()
		{
			return ((Entry) iterator.next()).getKey();
		}

		public void remove()
		{
			iterator.remove();
		}
	}

	public static abstract class AbstractEntry implements Entry
	{
		public boolean equals(Object o)
		{
			if(o == this)
			{
				return true;
			}

			if(!(o instanceof Entry))
			{
				return false;
			}

			Entry e = (Entry) o;
			Object k = getKey();
			Object v = getValue();

			if((k == null ? e.getKey() == null : k.equals(e.getKey())) &&
				(v == null ? e.getValue() == null : v.equals(e.getValue())))
			{
				return true;
			}

			return false;
		}

		public int hashCode()
		{
			Object k = getKey();
			Object v = getValue();

			return (k == null ? 0 : k.hashCode()) ^
				(v == null ? 0 : v.hashCode());
		}
	}
	public void clear()
	{
		entries().clear();
	}
	public boolean containsKey(Object key)
	{
		for(Iterator i = entries().iterator(); i.hasNext();)
		{
			Object k = ((Entry) i.next()).getKey();

			if(key==null ? k==null : key.equals(k))
			{
				return true;
			}
		}

		return false;
	}
	public boolean containsValue(Object value)
	{
		for(Iterator i = entries().iterator(); i.hasNext();)
		{
			Object v = ((Entry) i.next()).getValue();

			if(value==null ? v==null : value.equals(v))
			{
				return true;
			}
		}

		return false;
	}
	public boolean equals(Object o)
	{
		if(o == this)
		{
			return true;
		}

		if(!(o instanceof Map))
		{
			return false;
		}

		Map m = (Map) o;

		if(m.size() != size())
		{
			return false;
		}

		for(Iterator i = entries().iterator(); i.hasNext();)
		{
			Entry e = (Entry) i.next();
			Object k = e.getKey();

			if(!m.containsKey(k))
			{
				return false;
			}

			Object v = e.getValue();

			if(!(v==null ? m.get(k)==null : v.equals(m.get(k))))
			{
				return false;
			}
		}

		return true;
	}
	public Object get(Object key)
	{
		for(Iterator i = entries().iterator(); i.hasNext();)
		{
			Entry e = (Entry) i.next();

			if(key==null ? e.getKey()==null : key.equals(e.getKey()))
			{
				return e.getValue();
			}
		}

		return null;
	}
	public int hashCode()
	{
		int hashCode = 0;

		for(Iterator i = entries().iterator(); i.hasNext();)
		{
			hashCode += i.next().hashCode();
		}

		return hashCode;
	}
	public boolean isEmpty()
	{
		return size() == 0;
	}
	public Set keySet()
	{
		if(keys == null)
		{
			keys = new KeySet();
		}

		return keys;
	}
	public Object put(Object key, Object value)
	{
		throw new UnsupportedOperationException();
	}
	public void putAll(Map t)
	{
		for(Iterator i = t.entries().iterator(); i.hasNext();)
		{
			Entry e = (Entry) i.next();

			put(e.getKey(), e.getValue());
		}
	}
	public Object remove(Object key)
	{
		for(Iterator i = entries().iterator(); i.hasNext();)
		{
			Entry e = (Entry) i.next();

			if(key==null ? e.getKey()==null : key.equals(e.getKey()))
			{
				Object v = e.getValue();
				i.remove();
				return v;
			}
		}

		return null;
	}
	public int size()
	{
		return entries().size();
	}
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		boolean first = true;

		s.append("{");
		for(Iterator i = entries().iterator(); i.hasNext();)
		{
			Entry e = (Entry) i.next();

			if(!first)
			{
				s.append(",");
			}
			else
			{
				first = false;
			}

			s.append("(");
			s.append(e.getKey());
			s.append(",");
			s.append(e.getValue());
			s.append(")");
		}
		s.append("}");

		return s.toString();
	}
	public Collection values()
	{
		if(values == null)
		{
			values = new ValueCollection();
		}

		return values;
	}
}
