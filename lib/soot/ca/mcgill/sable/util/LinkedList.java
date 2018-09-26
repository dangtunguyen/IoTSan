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

public class LinkedList extends AbstractSequentialList
{
	int size;
	Node first;
	Node last;

	private class LinkedListIterator implements ListIterator
	{
		private Node next;
		private Node prev;
		private Node old;
		private int nextIndex;
		private int prevIndex;
		private int localModCount = modCount;

		LinkedListIterator(int index)
		{
			if(index < size - index)
			{
				prev = null;
				next = first;
				nextIndex = 0;
				prevIndex = -1;

				for(int i = 0; i < index; i++)
				{
					next();
				}
			}
			else
			{
				prev = last;
				next = null;
				nextIndex = size;
				prevIndex = size - 1;

				for(int i = size; i > index; i--)
				{
					previous();
				}
			}
		}

		public void set(Object o)
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(old == null)
			{
				throw new java.util.NoSuchElementException();
			}

			old.setElement(o);
		}

		public void add(Object o)
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(prev == null)
			{
				addFirst(o);
				next = first;
			}
			else if(next == null)
			{
				addLast(o);
				next = last;
			}
			else
			{
				Node node = new Node(o);
				node.setPrevious(prev);
				node.setNext(next);
				next = node;

				modCount++;
				size++;
			}

			localModCount = modCount;
		}

		public int nextIndex()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return nextIndex;
		}

		public int previousIndex()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return prevIndex;
		}

		public boolean hasPrevious()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return prevIndex >= 0;
		}

		public Object previous()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(prev == null)
			{
				throw new java.util.NoSuchElementException();
			}

			next = old = prev;
			prev = prev.getPrevious();
			prevIndex--;
			nextIndex--;

			return old.getElement();
		}

		public boolean hasNext()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return nextIndex < size();
		}

		public Object next()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(next == null)
			{
				throw new java.util.NoSuchElementException();
			}

			prev = old = next;
			next = next.getNext();
			prevIndex++;
			nextIndex++;

			return old.getElement();
		}

		public void remove()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(old == null)
			{
				throw new java.util.NoSuchElementException();
			}

			if(next == old)
			{
				next = next.getNext();
			}
			else
			{
				prev = prev.getPrevious();
				prevIndex--;
				nextIndex--;
			}

			LinkedList.this.removeNode(old);
			localModCount = modCount;
			old = null;
		}
	}

	private class Node
	{
		private Node previous;
		private Node next;
		private Object element;

		Node(Object element)
		{
			this.element = element;
		}

		Object getElement()
		{
			return element;
		}

		void setElement(Object element)
		{
			this.element = element;
		}

		Node getPrevious()
		{
			return previous;
		}

		void setPrevious(Node node)
		{
			if(previous != null)
			{
				previous.next = null;
			}

			if(node != null)
			{
				if(node.next != null)
				{
					node.next.previous = null;
				}

				node.next = this;
			}

			previous = node;
		}

		Node getNext()
		{
			return next;
		}

		void setNext(Node node)
		{
			if(next != null)
			{
				next.previous = null;
			}

			if(node != null)
			{
				if(node.previous != null)
				{
					node.previous.next = null;
				}

				node.previous = this;
			}

			next = node;
		}

	}

	public LinkedList()
	{
	}
	public LinkedList(Collection c)
	{
		ListIterator j = listIterator();

		for(Iterator i = c.iterator(); i.hasNext();)
		{
			j.add(i.next());
			j.next();
		}
	}
	public void addFirst(Object o)
	{
		Node node = new Node(o);

		if(first == null)
		{
			first = node;
			last = node;
		}
		else
		{
			first.setPrevious(node);
			first = node;
		}

		modCount++;
		size++;
	}
	public void addLast(Object o)
	{
		Node node = new Node(o);

		if(last == null)
		{
			last = node;
			first = node;
		}
		else
		{
			last.setNext(node);
			last = node;
		}

		modCount++;
		size++;
	}
	public void clear()
	{
		first = last = null;
	}
	public Object getFirst()
	{
		if(first == null)
		{
			throw new java.util.NoSuchElementException();
		}

		return first.getElement();
	}
	public Object getLast()
	{
		if(last == null)
		{
			throw new java.util.NoSuchElementException();
		}

		return last.getElement();
	}
	public ListIterator listIterator(int index)
	{
		return new LinkedListIterator(index);
	}
	public Object removeFirst()
	{
		if(first == null)
		{
			throw new java.util.NoSuchElementException();
		}

		Object old = first.getElement();
		removeNode(first);

		return old;
	}
	public Object removeLast()
	{
		if(last == null)
		{
			throw new java.util.NoSuchElementException();
		}

		Object old = last.getElement();
		removeNode(last);

		return old;
	}
	private void removeNode(Node node)
	{
		Node prev = node.getPrevious();
		Node next = node.getNext();

		if(prev == null)
		{
			first = next;
		}
		else
		{
			prev.setNext(next);
		}

		if(next == null)
		{
			last = prev;
		}
		else
		{
			next.setPrevious(prev);
		}

		modCount++;
		size--;
	}
	public int size()
	{
		return size;
	}
}
