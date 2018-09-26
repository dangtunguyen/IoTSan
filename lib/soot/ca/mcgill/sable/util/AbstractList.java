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

public abstract class AbstractList extends AbstractCollection implements List
{
	protected transient int modCount;

	private class AbstractListIterator implements ListIterator
	{
		private int index = 0;
		private int lastIndex = -1;
		private int localModCount = modCount;

		AbstractListIterator(int index)
		{
			this.index = index;
		}

		public void set(Object o)
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(lastIndex == -1)
			{
				throw new java.util.NoSuchElementException();
			}

			AbstractList.this.set(lastIndex, o);
		}

		public void add(Object o)
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			AbstractList.this.add(index, o);
			localModCount = modCount;
		}

		public int nextIndex()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return index;
		}

		public int previousIndex()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return index - 1;
		}

		public boolean hasPrevious()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return index > 0;
		}

		public Object previous()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			lastIndex = --index;
			return get(index);
		}

		public boolean hasNext()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return index < size();
		}

		public Object next()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			lastIndex = index;
			return get(index++);
		}

		public void remove()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(lastIndex == -1)
			{
				throw new java.util.NoSuchElementException();
			}

			if(lastIndex != index)
			{
				index = lastIndex;
			}

			AbstractList.this.remove(lastIndex);
			localModCount = modCount;
			lastIndex = -1;
		}
	}
	public void add(int index, Object element)
	{
		throw new UnsupportedOperationException();
	}
	public boolean add(Object o)
	{
		add(size(), o);
		return true;
	}
	public boolean addAll(int index, Collection c)
	{
		boolean modified = false;

		for(Iterator i = c.iterator(); i.hasNext();)
		{
			add(index++, i.next());
			modified = true;
		}

		return modified;
	}
	public boolean equals(Object o)
	{
		if(o == this)
		{
			return true;
		}

		if(!(o instanceof List))
		{
			return false;
		}

		List list = (List) o;

		Iterator j = list.iterator();
		for(Iterator i = iterator(); i.hasNext();)
		{
			if(!j.hasNext())
			{
				return false;
			}

			if(!i.next().equals(j.next()))
			{
				return false;
			}
		}

		if(j.hasNext())
		{
			return false;
		}

		return true;
	}
	public int hashCode()
	{
	   int hashCode = 0;

	   Iterator i = iterator();

	   while(i.hasNext())
	   {
		   Object obj = i.next();
		   hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
	   }

	   return hashCode;
	}
	public int indexOf(Object o)
	{
		if(size() == 0)
		{
			return -1;
		}

		return indexOf(o, 0);
	}
	public int indexOf(Object o, int index)
	{
		ListIterator i = listIterator(index);
		do
		{
			if(o == null ? i.next() == null : o.equals(i.next()))
			{
				return i.previousIndex();
			}
		}
		while(i.hasNext());

		return -1;
	}
	public Iterator iterator()
	{
		return listIterator();
	}
	public int lastIndexOf(Object o)
	{
		int size = size();

		if(size == 0)
		{
			return -1;
		}

		return lastIndexOf(o, size - 1);
	}
	public int lastIndexOf(Object o, int index)
	{
		for(ListIterator i = listIterator(index + 1); i.hasPrevious();)
		{
			if(o == null ? i.previous() == null : o.equals(i.previous()))
			{
				return i.nextIndex();
			}
		}

		return -1;
	}
	public ListIterator listIterator()
	{
		return listIterator(0);
	}
	public ListIterator listIterator(int index)
	{
		return new AbstractListIterator(index);
	}
	public Object remove(int index)
	{
		throw new UnsupportedOperationException();
	}
	public void removeRange(int fromIndex, int toIndex)
	{
		int size = size();

		if(fromIndex < 0 || fromIndex >= size || toIndex > size || toIndex < fromIndex)
		{
			throw new ArrayIndexOutOfBoundsException();
		}

		int count = toIndex - fromIndex;

		for(int i = 0; i < count; i++)
		{
			remove(fromIndex);
		}
	}
	public Object set(int index, Object element)
	{
		throw new UnsupportedOperationException();
	}
}
