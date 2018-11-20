package ca.mcgill.sable.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SableUtil, a clean room implementation of the Collection API.     *
 * Copyright (C) 1997, 1998 Etienne Gagnon (gagnon@sable.mcgill.ca). *
 * All rights reserved.                                              *
 *                                                                   *
 * Modifications by Raja Vallee-Rai                                  *
 * Copyright (C) 1998 Raja Vallee-Rai.  All rights reserved.         *
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

 - Modified on July 23, 1998 by Etienne Gagnon (gagnon@sable.mcgill.ca). (*)
   Added toArray(Object[]).

 - Modified on June 15, 1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   Fixed a bug with the remove() method.  Was missing a next().

 - Modified on June 7, 1998 by Etienne Gagnon (gagnon@sable.mcgill.ca). (*)
   Changed the license.

*/

public abstract class AbstractCollection implements Collection
{
	public boolean add(Object o)
	{
		throw new UnsupportedOperationException();
	}
	public boolean addAll(Collection c)
	{
		boolean modified = false;

		for(Iterator i = c.iterator(); i.hasNext();)
		{
			modified |= add(i.next());
		}

		return modified;
	}
	public void clear()
	{
		for(Iterator i = iterator(); i.hasNext();)
		{
			i.next();
			i.remove();
		}
	}
	public boolean contains(Object o)
	{
		for(Iterator i = iterator(); i.hasNext();)
		{
			if((o==null) ? i.next()==null : o.equals(i.next()))
			{
				return true;
			}
		}

		return false;
	}
	public boolean containsAll(Collection c)
	{
		for(Iterator i = c.iterator(); i.hasNext();)
		{
			if(!contains(i.next()))
			{
				return false;
			}
		}

		return true;
	}
	public boolean isEmpty()
	{
		return size() == 0;
	}
	public boolean remove(Object o)
	{
		for(Iterator i = iterator(); i.hasNext();)
		{
			if(o==null ? i.next()==null : o.equals(i.next()))
			{
				i.remove();
				return true;
			}
		}

		return false;
	}
	public boolean removeAll(Collection c)
	{
		boolean modified = false;

		for(Iterator i = iterator(); i.hasNext();)
		{
			Object e = i.next();

			if(c.contains(e))
			{
				modified = true;
				i.remove();
			}
		}

		return modified;
	}
	public boolean retainAll(Collection c)
	{
		boolean modified = false;

		for(Iterator i = iterator(); i.hasNext();)
		{
			Object e = i.next();

			if(!c.contains(e))
			{
				modified = true;
				i.remove();
			}
		}

		return modified;
	}
	public Object[] toArray()
	{
		Object[] a = new Object[size()];
		int pos = 0;

		for(Iterator i = iterator(); i.hasNext();)
		{
			a[pos++] = i.next();
		}

		return a;
	}
	public void toArray(Object[] a)
	{
		int pos = 0;

		for(Iterator i = iterator(); i.hasNext();)
		{
			a[pos++] = i.next();
		}
	}
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		boolean first = true;

		s.append("{");
		for(Iterator i = iterator(); i.hasNext();)
		{
			if(!first)
			{
				s.append(",");
			}
			else
			{
				first = false;
			}

			s.append(i.next());
		}
		s.append("}");

		return s.toString();
	}
}
