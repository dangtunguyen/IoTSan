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

public class TypedLinkedList extends LinkedList
{
	Cast cast;

	private class TypedLinkedListIterator implements ListIterator
	{
		ListIterator iterator;

		TypedLinkedListIterator(ListIterator iterator)
		{
			this.iterator = iterator;
		}

		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		public Object next()
		{
			return iterator.next();
		}

		public boolean hasPrevious()
		{
			return iterator.hasPrevious();
		}

		public Object previous()
		{
			return iterator.previous();
		}

		public int nextIndex()
		{
			return iterator.nextIndex();
		}

		public int previousIndex()
		{
			return iterator.previousIndex();
		}

		public void remove()
		{
			iterator.remove();
		}

		public void set(Object o)
		{
			iterator.set(cast.cast(o));
		}

		public void add(Object o)
		{
			iterator.add(cast.cast(o));
		}
	}
	public TypedLinkedList()
	{
		super();

		cast = NoCast.instance;
	}
	public TypedLinkedList(Cast cast)
	{
		super();

		this.cast = cast;
	}
	public TypedLinkedList(Collection c)
	{
		super(c);

		cast = NoCast.instance;
	}
	public TypedLinkedList(Collection c, Cast cast)
	{
		super(c);

		this.cast = cast;
	}
	public void addFirst(Object o)
	{
		super.addFirst(cast.cast(o));
	}
	public void addLast(Object o)
	{
		super.addLast(cast.cast(o));
	}
	public Cast getCast()
	{
		return cast;
	}
	public ListIterator listIterator(int index)
	{
		return new TypedLinkedListIterator(super.listIterator(index));
	}
}
