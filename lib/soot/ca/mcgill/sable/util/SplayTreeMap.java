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

public class SplayTreeMap extends AbstractMap
{
	private int size;
	private Comparator comparator;
	private Node root;
	private boolean toggle;
	private Collection entries;
	private transient int modCount;

	private class EntryCollection extends AbstractCollection
	{
		public int size()
		{
			return size;
		}

		public Iterator iterator()
		{
			return new EntryIterator();
		}
	}

	private class EntryIterator implements Iterator
	{
		private Node node;
		private Object lastKey;
		private int localModCount = modCount;

		EntryIterator()
		{
			node = root;

			while((node != null) && (node.getLeft() != null))
			{
				node = node.getLeft();
			}
		}

		public boolean hasNext()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			return node != null;
		}

		public Object next()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			lastKey = node.getKey();
			Object result = node;

			if(node.getRight() != null)
			{
				node = node.getRight();

				while(node.getLeft() != null)
				{
					node = node.getLeft();
				}
			}
			else
			{
				Node child = node;
				node = node.getParent();

				while((node != null) && (child == node.getRight()))
				{
					child = node;
					node = node.getParent();
				}
			}

			return result;
		}

		public void remove()
		{
			if(localModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}

			if(lastKey == null)
			{
				throw new java.util.NoSuchElementException();
			}

			SplayTreeMap.this.remove(lastKey);
			localModCount = modCount;
			lastKey = null;
		}
	}

	private static class Node extends AbstractEntry
	{
		private Object key;
		private Object value;

		private Node left;
		private Node right;
		private Node parent;

		Node(Object key, Object value)
		{
			if((key == null) || (value == null))
			{
				throw new NullPointerException();
			}

			this.key = key;
			this.value = value;
		}

		public Object getKey()
		{
			return key;
		}

		public Object getValue()
		{
			return value;
		}

		public Object setValue(Object value)
		{
			if(value == null)
			{
				throw new NullPointerException();
			}

			Object oldValue = this.value;
			this.value = value;

			return oldValue;
		}

		Node getLeft()
		{
			return left;
		}

		void setLeft(Node node)
		{
			if(left != null)
			{
				left.parent = null;
			}

			if(node != null)
			{
				if(node.parent != null)
				{
					node.parent.removeChild(node);
				}

				node.parent = this;
			}

			left = node;
		}

		Node getRight()
		{
			return right;
		}

		void setRight(Node node)
		{
			if(right != null)
			{
				right.parent = null;
			}

			if(node != null)
			{
				if(node.parent != null)
				{
					node.parent.removeChild(node);
				}

				node.parent = this;
			}

			right = node;
		}

		Node getParent()
		{
			return parent;
		}

		private void removeChild(Node node)
		{
			if(left == node)
			{
				setLeft(null);
			}

			if(right == node)
			{
				setRight(null);
			}
		}
	}
	public SplayTreeMap()
	{
	}
	public SplayTreeMap(Comparator comparator)
	{
		this.comparator = comparator;
	}
	public SplayTreeMap(Map map)
	{
		for(Iterator i = map.entries().iterator(); i.hasNext();)
		{
			Entry e = (Entry) i.next();
			put(e.getKey(), e.getValue());
		}
	}
	public SplayTreeMap(Map map, Comparator comparator)
	{
		this.comparator = comparator;

		for(Iterator i = map.entries().iterator(); i.hasNext();)
		{
			Entry e = (Entry) i.next();
			put(e.getKey(), e.getValue());
		}
	}
	public void clear()
	{
		modCount++;
		root = null;
	}
	public Object clone()
	{
		return new SplayTreeMap(this, comparator);
	}
	private int compare(Object key1, Object key2)
	{
		if(comparator != null)
		{
			return comparator.compare(key1, key2);
		}

		return ((Comparable) key1).compareTo(key2);
	}
	public boolean containsKey(Object key)
	{
		if(key == null)
		{
			throw new NullPointerException();
		}

		Node node = find(key);

		return node != null;
	}
	private Object delete(Object key)
	{
		Node node = root;
		Node lastVisited = null;

		while(node != null)
		{
			lastVisited = node;

			int comp = compare(key, node.getKey());

			if(comp == 0)
			{
				if(node.getLeft() == null)
				{
					Node right = node.getRight();

					if(node.getParent() == null)
					{
						node.setRight(null);
						root = right;
						return node.getValue();
					}

					Node parent = node.getParent();

					if(node == parent.getLeft())
					{
						parent.setLeft(right);
					}
					else
					{
						parent.setRight(right);
					}

					splay(right);
					return node.getValue();
				}

				if(node.getRight() == null)
				{
					Node left = node.getLeft();

					if(node.getParent() == null)
					{
						node.setLeft(null);
						root = left;
						return node.getValue();
					}

					Node parent = node.getParent();

					if(node == parent.getRight())
					{
						parent.setRight(left);
					}
					else
					{
						parent.setLeft(left);
					}

					splay(left);
					return node.getValue();
				}

				Node leaf;

				toggle = !toggle;
				if(toggle)
				{
					leaf = node.getRight();

					while(leaf.getLeft() != null)
					{
						leaf = leaf.getLeft();
					}
				}
				else
				{
					leaf = node.getLeft();

					while(leaf.getRight() != null)
					{
						leaf = leaf.getRight();
					}
				}

				delete(leaf.getKey());

				if(node.getParent() == null)
				{
					leaf.setRight(node.getRight());
					leaf.setLeft(node.getLeft());
					root = leaf;
					return node.getValue();
				}

				Node parent = node.getParent();

				if(node == parent.getLeft())
				{
					parent.setLeft(leaf);
				}
				else
				{
					parent.setRight(leaf);
				}

				leaf.setRight(node.getRight());
				leaf.setLeft(node.getLeft());

				splay(leaf);
				return node.getValue();
			}

			if(comp < 0)
			{
				node = node.getLeft();
			}
			else
			{
				node = node.getRight();
			}
		}

		splay(lastVisited);
		return null;
	}
	public Collection entries()
	{
		if(entries == null)
		{
			entries = new EntryCollection();
		}

		return entries;
	}
	private Node find(Object key)
	{
		Node node = root;
		Node lastVisited = null;

		while(node != null)
		{
			lastVisited = node;

			int comp = compare(key, node.getKey());

			if(comp == 0)
			{
				splay(node);
				return node;
			}

			if(comp < 0)
			{
				node = node.getLeft();
			}
			else
			{
				node = node.getRight();
			}
		}

		splay(lastVisited);
		return null;
	}
	public Object get(Object key)
	{
		if(key == null)
		{
			throw new NullPointerException();
		}

		Node node = find(key);

		if(node != null)
		{
			return node.getValue();
		}

		return null;
	}
	public Comparator getComparator()
	{
		return comparator;
	}
	private Object insert(Object key, Object value)
	{
		Node node = root;
		Node lastVisited = null;

		while(node != null)
		{
			lastVisited = node;

			int comp = compare(key, node.getKey());

			if(comp == 0)
			{
				Object oldValue = node.getValue();
				node.setValue(value);
				splay(node);
				return oldValue;
			}

			if(comp < 0)
			{
				node = node.getLeft();
			}
			else
			{
				node = node.getRight();
			}
		}

		if(lastVisited != null)
		{
			int comp = compare(key, lastVisited.getKey());

			if(comp < 0)
			{
				lastVisited.setLeft(new Node(key, value));
				splay(lastVisited.getLeft());
				return null;
			}
			else
			{
				lastVisited.setRight(new Node(key, value));
				splay(lastVisited.getRight());
				return null;
			}
		}

		compare(key, key);
		root = new Node(key, value);
		return null;
	}
	public Object put(Object key, Object value)
	{
		modCount++;
		Object result = insert(key, value);

		if(result == null)
		{
			size++;
		}

		return result;
	}
	public Object remove(Object key)
	{
		modCount++;
		Object result = delete(key);

		if(result != null)
		{
			size--;
		}

		return result;
	}
	public int size()
	{
		return size;
	}
	private void splay(Node node)
	{
		if(node == null)
		{
			return;
		}

		while(node.getParent() != null)
		{
			Node parent = node.getParent();
			Node grandParent = parent.getParent();

			if(grandParent == null)
			{
				if(node == parent.getLeft())
				{
					parent.setLeft(node.getRight());
					node.setRight(parent);
				}
				else
				{
					parent.setRight(node.getLeft());
					node.setLeft(parent);
				}
			}
			else
			{
				Node grandGrandParent = grandParent.getParent();
				boolean grandParentIsLeft = false;

				if(grandGrandParent != null)
				{
					if(grandParent == grandGrandParent.getLeft())
					{
						grandParentIsLeft = true;
					}
				}

				if(parent == grandParent.getLeft())
				{
					if(node == parent.getLeft())
					{
						grandParent.setLeft(parent.getRight());
						parent.setRight(grandParent);
						parent.setLeft(node.getRight());
						node.setRight(parent);
					}
					else
					{
						grandParent.setLeft(node.getRight());
						node.setRight(grandParent);
						parent.setRight(node.getLeft());
						node.setLeft(parent);
					}
				}
				else
				{
					if(node == parent.getRight())
					{
						grandParent.setRight(parent.getLeft());
						parent.setLeft(grandParent);
						parent.setRight(node.getLeft());
						node.setLeft(parent);
					}
					else
					{
						grandParent.setRight(node.getLeft());
						node.setLeft(grandParent);
						parent.setLeft(node.getRight());
						node.setRight(parent);
					}
				}

				if(grandGrandParent != null)
				{
					if(grandParentIsLeft)
					{
						grandGrandParent.setLeft(node);
					}
					else
					{
						grandGrandParent.setRight(node);
					}
				}
			}
		}

		root = node;
	}
}
