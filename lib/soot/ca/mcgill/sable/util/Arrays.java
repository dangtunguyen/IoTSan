package ca.mcgill.sable.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SableUtil, a clean room implementation of the Collection API.     *
 * Copyright (C) 1997, 1998 Raja Vallee-Rai (kor@sable.mcgill.ca).   *
 * All rights reserved.                                              *
 *                                                                   *
 * Modifications by Patrick Lam (plam@sable.mcgill.ca) are           *
 * Copyright (C) 1998 Patrick Lam (plam@sable.mcgill.ca).  All       *
 * rights reserved.                                                  *
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

 - Modified on July 22, 1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   Changed some constructors from private to public to satisfy jikes.

 - Modified on June 19, 1998 by Patrick Lam (plam@sable.mcgill.ca). (*)
   Merged in Arrays.sort implementation.

 - Modified on June 15, 1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   First release of this file.

*/

public class Arrays
{
	private static class ArrayList extends AbstractList
	{
		private Object[] array;

		public ArrayList(Object[] array)
		{
			this.array = array;
		}

		public Object get(int index)
		{
			return array[index];
		}

		public Object set(int index, Object element)
		{
			Object oldElement = array[index];
			array[index] = element;

			return oldElement;
		}

		public int size()
		{
			return array.length;
		}

		public Object clone()
		{
			return array.clone();
		}

	}


  private static void _sort_helper(Object[] toSort,
				   int low, int high, Comparator c)
	throws ClassCastException
	{
	  if (low < high)
	{
	  int q = partition(toSort, low, high, c);
	  _sort_helper(toSort, low, q, c);
	  _sort_helper(toSort, q+1, high, c);
	}
	}
  private static int partition(Object[] toSort,
				   int low, int high, Comparator c)
	throws ClassCastException
	{
	  Object x = toSort[low];
	  int i = low - 1; int j = high + 1;
	  while(true)
	{
	  do { j--; } while (c.compare(toSort[j], x) > 0);
	  do { i++; } while (c.compare(toSort[i], x) < 0);
	  if (i < j)
		{
		  x = toSort[i]; toSort[i] = toSort[j]; toSort[j] = x;
		}
	  return j;
	}
	}
  public static void sort(Object[] toSort, Comparator c)
	throws ClassCastException
	{
	  _sort_helper(toSort, 0, toSort.length-1, c);
	}
	public static List toList(Object[] array)
	{
		return new ArrayList(array);
	}
}
