package edu.ksu.cis.bandera.abstraction.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
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
import java.io.*;
public class RefHashTable extends Dictionary implements Map, Cloneable, java.io.Serializable {
	/**
	 * The hash table data.
	 */
	private transient Entry table[];

	/**
	 * The total number of entries in the hash table.
	 */
	private transient int count;

	/**
	 * The table is rehashed when its size exceeds this threshold.  (The
	 * value of this field is (int)(capacity * loadFactor).)
	 *
	 * @serial
	 */
	private int threshold;

	/**
	 * The load factor for the RefHashTable.
	 *
	 * @serial
	 */
	private float loadFactor;

	/**
	 * The number of times this RefHashTable has been structurally modified
	 * Structural modifications are those that change the number of entries in
	 * the RefHashTable or otherwise modify its internal structure (e.g.,
	 * rehash).  This field is used to make iterators on Collection-views of
	 * the RefHashTable fail-fast.  (See ConcurrentModificationException).
	 */
	private transient int modCount = 0;

	// Views

	private transient Set keySet = null;
	private transient Set entrySet = null;
	private transient Collection values = null;
	private class KeySet extends AbstractSet {
		public Iterator iterator() {
			return new Enumerator(KEYS, true);
		}
		public int size() {
			return count;
		}
		public boolean contains(Object o) {
			return containsKey(o);
		}
		public boolean remove(Object o) {
			return RefHashTable.this.remove(o) != null;
		}
		public void clear() {
			RefHashTable.this.clear();
		}
	}
	private class EntrySet extends AbstractSet {
		public Iterator iterator() {
			return new Enumerator(ENTRIES, true);
		}
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry entry = (Map.Entry) o;
			Object key = entry.getKey();
			Entry tab[] = table;
			int hash = key.hashCode();
			int index = (hash & 0x7FFFFFFF) % tab.length;
			for (Entry e = tab[index]; e != null; e = e.next)
				if (e.hash == hash && e.equals(entry))
					return true;
			return false;
		}
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry entry = (Map.Entry) o;
			Object key = entry.getKey();
			Entry tab[] = table;
			int hash = key.hashCode();
			int index = (hash & 0x7FFFFFFF) % tab.length;
			for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
				if (e.hash == hash && e.equals(entry)) {
					modCount++;
					if (prev != null)
						prev.next = e.next;
					else
						tab[index] = e.next;
					count--;
					e.value = null;
					return true;
				}
			}
			return false;
		}
		public int size() {
			return count;
		}
		public void clear() {
			RefHashTable.this.clear();
		}
	}
	private class ValueCollection extends AbstractCollection {
		public Iterator iterator() {
			return new Enumerator(VALUES, true);
		}
		public int size() {
			return count;
		}
		public boolean contains(Object o) {
			return containsValue(o);
		}
		public void clear() {
			RefHashTable.this.clear();
		}
	}

	/**
	 * RefHashTable collision list.
	 */
	private static class Entry implements Map.Entry {
		int hash;
		Object key;
		Object value;
		Entry next;
		protected Entry(int hash, Object key, Object value, Entry next) {
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}
		protected Object clone() {
			return new Entry(hash, key, value, (next == null ? null : (Entry) next.clone()));
		}

		// Map.Entry Ops 

		public Object getKey() {
			return key;
		}
		public Object getValue() {
			return value;
		}
		public Object setValue(Object value) {
			if (value == null)
				throw new NullPointerException();
			Object oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		public boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry e = (Map.Entry) o;
			return (key == null ? e.getKey() == null : key == e.getKey()) && (value == null ? e.getValue() == null : value.equals(e.getValue()));
		}
		public int hashCode() {
			return hash ^ (value == null ? 0 : value.hashCode());
		}
		public String toString() {
			return key.toString() + "=" + value.toString();
		}
	}

	// Types of Enumerations/Iterations
	private static final int KEYS = 0;
	private static final int VALUES = 1;
	private static final int ENTRIES = 2;

	/**
	 * A hashtable enumerator class.  This class implements both the
	 * Enumeration and Iterator interfaces, but individual instances
	 * can be created with the Iterator methods disabled.  This is necessary
	 * to avoid unintentionally increasing the capabilities granted a user
	 * by passing an Enumeration.
	 */
	private class Enumerator implements Enumeration, Iterator {
		Entry[] table = RefHashTable.this.table;
		int index = table.length;
		Entry entry = null;
		Entry lastReturned = null;
		int type;

		/**
		 * Indicates whether this Enumerator is serving as an Iterator
		 * or an Enumeration.  (true -> Iterator).
		 */
		boolean iterator;

		/**
		 * The modCount value that the iterator believes that the backing
		 * List should have.  If this expectation is violated, the iterator
		 * has detected concurrent modification.
		 */
		private int expectedModCount = modCount;
		Enumerator(int type, boolean iterator) {
			this.type = type;
			this.iterator = iterator;
		}
		public boolean hasMoreElements() {
			while (entry == null && index > 0)
				entry = table[--index];
			return entry != null;
		}
		public Object nextElement() {
			while (entry == null && index > 0)
				entry = table[--index];
			if (entry != null) {
				Entry e = lastReturned = entry;
				entry = e.next;
				return type == KEYS ? e.key : (type == VALUES ? e.value : e);
			}
			throw new NoSuchElementException("RefHashTable Enumerator");
		}

		// Iterator methods
		public boolean hasNext() {
			return hasMoreElements();
		}
		public Object next() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			return nextElement();
		}
		public void remove() {
			if (!iterator)
				throw new UnsupportedOperationException();
			if (lastReturned == null)
				throw new IllegalStateException("RefHashTable Enumerator");
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			synchronized (RefHashTable.this) {
				Entry[] tab = RefHashTable.this.table;
				int index = (lastReturned.hash & 0x7FFFFFFF) % tab.length;
				for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
					if (e == lastReturned) {
						modCount++;
						expectedModCount++;
						if (prev == null)
							tab[index] = e.next;
						else
							prev.next = e.next;
						count--;
						lastReturned = null;
						return;
					}
				}
				throw new ConcurrentModificationException();
			}
		}
	}
	/**
	 * Constructs a new, empty HashPointerTable with a default capacity and load
	 * factor, which is <tt>0.75</tt>. 
	 */
	public RefHashTable() {
		this(101, 0.75f);
	}
	/**
	 * Constructs a new, empty HashPointerTable with the specified initial capacity
	 * and default load factor, which is <tt>0.75</tt>.
	 *
	 * @param     initialCapacity   the initial capacity of the hashtable.
	 * @exception IllegalArgumentException if the initial capacity is less
	 *              than zero.
	 */
	public RefHashTable(int initialCapacity) {
		this(initialCapacity, 0.75f);
	}
	/**
	 * Constructs a new, empty HashPointerTable with the specified initial 
	 * capacity and the specified load factor.
	 *
	 * @param      initialCapacity   the initial capacity of the hashtable.
	 * @param      loadFactor        the load factor of the hashtable.
	 * @exception  IllegalArgumentException  if the initial capacity is less
	 *             than zero, or if the load factor is nonpositive.
	 */
	public RefHashTable(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		if (loadFactor <= 0)
			throw new IllegalArgumentException("Illegal Load: " + loadFactor);
		if (initialCapacity == 0)
			initialCapacity = 1;
		this.loadFactor = loadFactor;
		table = new Entry[initialCapacity];
		threshold = (int) (initialCapacity * loadFactor);
	}
	/**
	 * Constructs a new HashPointerTable with the same mappings as the given 
	 * Map.  The hashtable is created with a capacity of twice the number
	 * of entries in the given Map or 11 (whichever is greater), and a
	 * default load factor, which is <tt>0.75</tt>.
	 *
	 * @since   JDK1.2
	 */
	public RefHashTable(Map t) {
		this(Math.max(2 * t.size(), 11), 0.75f);
		putAll(t);
	}
	/**
	 * Clears this HashPointerTable so that it contains no keys. 
	 */
	public synchronized void clear() {
		Entry tab[] = table;
		modCount++;
		for (int index = tab.length; --index >= 0;)
			tab[index] = null;
		count = 0;
	}
/**
 * Creates a shallow copy of this HashPointerTable. All the structure of the 
 * hashtable itself is copied, but the keys and values are not cloned. 
 * This is a relatively expensive operation.
 *
 * @return  a clone of the hashtable.
 */
public synchronized Object clone() {
	try {
		RefHashTable t = (RefHashTable) super.clone();
		t.table = new Entry[table.length];
		for (int i = table.length; i-- > 0;) {
			t.table[i] = (table[i] != null) ? (Entry) table[i].clone() : null;
		}
		t.keySet = null;
		t.entrySet = null;
		t.values = null;
		t.modCount = 0;
		return t;
	} catch (CloneNotSupportedException e) {
		// this shouldn't happen, since we are Cloneable
		throw new InternalError();
	}
}
	/**
	 * Tests if some key maps into the specified value in this HashPointerTable.
	 * This operation is more expensive than the <code>containsKey</code>
	 * method.<p>
	 *
	 * Note that this method is identical in functionality to containsValue,
	 * (which is part of the Map interface in the collections framework).
	 * 
	 * @param      value   a value to search for.
	 * @return     <code>true</code> if and only if some key maps to the
	 *             <code>value</code> argument in this hashtable as 
	 *             determined by the <tt>equals</tt> method;
	 *             <code>false</code> otherwise.
	 * @exception  NullPointerException  if the value is <code>null</code>.
	 * @see        #containsKey(Object)
	 * @see        #containsValue(Object)
	 * @see	   Map
	 */
	public synchronized boolean contains(Object value) {
		if (value == null) {
			throw new NullPointerException();
		}
		Entry tab[] = table;
		for (int i = tab.length; i-- > 0;) {
			for (Entry e = tab[i]; e != null; e = e.next) {
				if (e.value.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Tests if the specified object is a key in this HashPointerTable.
	 * 
	 * @param   key   possible key.
	 * @return  <code>true</code> if and only if the specified object 
	 *          is a key in this hashtable, as determined by the 
	 *          <tt>equals</tt> method; <code>false</code> otherwise.
	 * @see     #contains(Object)
	 */
	public synchronized boolean containsKey(Object key) {
		Entry tab[] = table;
		int hash = key.hashCode();
		int index = (hash & 0x7FFFFFFF) % tab.length;
		for (Entry e = tab[index]; e != null; e = e.next) {
			if ((e.hash == hash) && e.key == key) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns true if this HashPointerTable maps one or more keys to this value.<p>
	 *
	 * Note that this method is identical in functionality to contains
	 * (which predates the Map interface).
	 *
	 * @param value value whose presence in this HashPointerTable is to be tested.
	 * @see	   Map
	 * @since JDK1.2
	 */
	public boolean containsValue(Object value) {
		return contains(value);
	}
	/**
	 * Returns an enumeration of the values in this HashPointerTable.
	 * Use the Enumeration methods on the returned object to fetch the elements
	 * sequentially.
	 *
	 * @return  an enumeration of the values in this hashtable.
	 * @see     java.util.Enumeration
	 * @see     #keys()
	 * @see	#values()
	 * @see	Map
	 */
	public synchronized Enumeration elements() {
		return new Enumerator(VALUES, false);
	}
	/**
	 * Returns a Set view of the entries contained in this HashPointerTable.
	 * Each element in this collection is a Map.Entry.  The Set is
	 * backed by the HashPointerTable, so changes to the HashPointerTable are reflected in
	 * the Set, and vice-versa.  The Set supports element removal
	 * (which removes the corresponding entry from the HashPointerTable),
	 * but not element addition.
	 *
	 * @see   Map.Entry
	 * @since JDK1.2
	 */
	public Set entrySet() {
		if (entrySet == null)
			entrySet = Collections.synchronizedSet(new EntrySet());
		return entrySet;
	}
	// Comparison and hashing

	/**
	 * Compares the specified Object with this Map for equality,
	 * as per the definition in the Map interface.
	 *
	 * @return true if the specified Object is equal to this Map.
	 * @see Map#equals(Object)
	 * @since JDK1.2
	 */
	public synchronized boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Map))
			return false;
		Map t = (Map) o;
		if (t.size() != size())
			return false;
		Iterator i = entrySet().iterator();
		while (i.hasNext()) {
			Entry e = (Entry) i.next();
			Object key = e.getKey();
			Object value = e.getValue();
			if (value == null) {
				if (!(t.get(key) == null && t.containsKey(key)))
					return false;
			} else {
				if (!value.equals(t.get(key)))
					return false;
			}
		}
		return true;
	}
	/**
	 * Returns the value to which the specified key is mapped in this HashPointerTable.
	 *
	 * @param   key   a key in the hashtable.
	 * @return  the value to which the key is mapped in this hashtable;
	 *          <code>null</code> if the key is not mapped to any value in
	 *          this hashtable.
	 * @see     #put(Object, Object)
	 */
	public synchronized Object get(Object key) {
		Entry tab[] = table;
		int hash = key.hashCode();
		int index = (hash & 0x7FFFFFFF) % tab.length;
		for (Entry e = tab[index]; e != null; e = e.next) {
			if ((e.hash == hash) && e.key == key) {
				return e.value;
			}
		}
		return null;
	}
	/**
	 * Returns the hash code value for this Map as per the definition in the
	 * Map interface.
	 *
	 * @see Map#hashCode()
	 * @since JDK1.2
	 */
	public synchronized int hashCode() {
		int h = 0;
		Iterator i = entrySet().iterator();
		while (i.hasNext())
			h += i.next().hashCode();
		return h;
	}
	/**
	 * Tests if this HashPointerTable maps no keys to values.
	 *
	 * @return  <code>true</code> if this hashtable maps no keys to values;
	 *          <code>false</code> otherwise.
	 */
	public boolean isEmpty() {
		return count == 0;
	}
	/**
	 * Returns an enumeration of the keys in this HashPointerTable.
	 *
	 * @return  an enumeration of the keys in this hashtable.
	 * @see     Enumeration
	 * @see     #elements()
	 * @see	#keySet()
	 * @see	Map
	 */
	public synchronized Enumeration keys() {
		return new Enumerator(KEYS, false);
	}
	/**
	 * Returns a Set view of the keys contained in this HashPointerTable.  The Set
	 * is backed by the HashPointerTable, so changes to the HashPointerTable are reflected
	 * in the Set, and vice-versa.  The Set supports element removal
	 * (which removes the corresponding entry from the HashPointerTable), but not
	 * element addition.
	 *
	 * @since JDK1.2
	 */
	public Set keySet() {
		if (keySet == null)
			keySet = Collections.synchronizedSet(new KeySet());
		return keySet;
	}
	/**
	 * Maps the specified <code>key</code> to the specified 
	 * <code>value</code> in this HashPointerTable. Neither the key nor the 
	 * value can be <code>null</code>. <p>
	 *
	 * The value can be retrieved by calling the <code>get</code> method 
	 * with a key that is equal to the original key. 
	 *
	 * @param      key     the hashtable key.
	 * @param      value   the value.
	 * @return     the previous value of the specified key in this hashtable,
	 *             or <code>null</code> if it did not have one.
	 * @exception  NullPointerException  if the key or value is
	 *               <code>null</code>.
	 * @see     Object#equals(Object)
	 * @see     #get(Object)
	 */
	public synchronized Object put(Object key, Object value) {
		// Make sure the value is not null
		if (value == null) {
			throw new NullPointerException();
		}

		// Makes sure the key is not already in the hashtable.
		Entry tab[] = table;
		int hash = key.hashCode();
		int index = (hash & 0x7FFFFFFF) % tab.length;
		for (Entry e = tab[index]; e != null; e = e.next) {
			if ((e.hash == hash) && e.key == key) {
				Object old = e.value;
				e.value = value;
				return old;
			}
		}
		modCount++;
		if (count >= threshold) {
			// Rehash the table if the threshold is exceeded
			rehash();
			tab = table;
			index = (hash & 0x7FFFFFFF) % tab.length;
		}

		// Creates the new entry.
		Entry e = new Entry(hash, key, value, tab[index]);
		tab[index] = e;
		count++;
		return null;
	}
	/**
	 * Copies all of the mappings from the specified Map to this HashPointerTable
	 * These mappings will replace any mappings that this HashPointerTable had for any
	 * of the keys currently in the specified Map. 
	 *
	 * @since JDK1.2
	 */
	public synchronized void putAll(Map t) {
		Iterator i = t.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry e = (Map.Entry) i.next();
			put(e.getKey(), e.getValue());
		}
	}
	/**
	 * Reconstitute the HashPointerTable from a stream (i.e., deserialize it).
	 */
	private synchronized void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
		// Read in the length, threshold, and loadfactor
		s.defaultReadObject();

		// Read the original length of the array and number of elements
		int origlength = s.readInt();
		int elements = s.readInt();

		// Compute new size with a bit of room 5% to grow but
		// No larger than the original size.  Make the length
		// odd if it's large enough, this helps distribute the entries.
		// Guard against the length ending up zero, that's not valid.
		int length = (int) (elements * loadFactor) + (elements / 20) + 3;
		if (length > elements && (length & 1) == 0)
			length--;
		if (origlength > 0 && length > origlength)
			length = origlength;
		table = new Entry[length];
		count = 0;

		// Read the number of elements and then all the key/value objects
		for (; elements > 0; elements--) {
			Object key = s.readObject();
			Object value = s.readObject();
			put(key, value);
		}
	}
	/**
	 * Increases the capacity of and internally reorganizes this 
	 * HashPointerTable, in order to accommodate and access its entries more 
	 * efficiently.  This method is called automatically when the 
	 * number of keys in the hashtable exceeds this hashtable's capacity 
	 * and load factor. 
	 */
	protected void rehash() {
		int oldCapacity = table.length;
		Entry oldMap[] = table;
		int newCapacity = oldCapacity * 2 + 1;
		Entry newMap[] = new Entry[newCapacity];
		modCount++;
		threshold = (int) (newCapacity * loadFactor);
		table = newMap;
		for (int i = oldCapacity; i-- > 0;) {
			for (Entry old = oldMap[i]; old != null;) {
				Entry e = old;
				old = old.next;
				int index = (e.hash & 0x7FFFFFFF) % newCapacity;
				e.next = newMap[index];
				newMap[index] = e;
			}
		}
	}
	/**
	 * Removes the key (and its corresponding value) from this 
	 * HashPointerTable. This method does nothing if the key is not in the hashtable.
	 *
	 * @param   key   the key that needs to be removed.
	 * @return  the value to which the key had been mapped in this hashtable,
	 *          or <code>null</code> if the key did not have a mapping.
	 */
	public synchronized Object remove(Object key) {
		Entry tab[] = table;
		int hash = key.hashCode();
		int index = (hash & 0x7FFFFFFF) % tab.length;
		for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if ((e.hash == hash) && e.key == key) {
				modCount++;
				if (prev != null) {
					prev.next = e.next;
				} else {
					tab[index] = e.next;
				}
				count--;
				Object oldValue = e.value;
				e.value = null;
				return oldValue;
			}
		}
		return null;
	}
	/**
	 * Returns the number of keys in this HashPointerTable.
	 *
	 * @return  the number of keys in this hashtable.
	 */
	public int size() {
		return count;
	}
	/**
	 * Returns a string representation of this <tt>HashPointerTable</tt> object 
	 * in the form of a set of entries, enclosed in braces and separated 
	 * by the ASCII characters "<tt>,&nbsp;</tt>" (comma and space). Each 
	 * entry is rendered as the key, an equals sign <tt>=</tt>, and the 
	 * associated element, where the <tt>toString</tt> method is used to 
	 * convert the key and element to strings. <p>Overrides to 
	 * <tt>toString</tt> method of <tt>Object</tt>.
	 *
	 * @return  a string representation of this hashtable.
	 */
	public synchronized String toString() {
		int max = size() - 1;
		StringBuffer buf = new StringBuffer();
		Iterator it = entrySet().iterator();
		buf.append("{");
		for (int i = 0; i <= max; i++) {
			Entry e = (Entry) (it.next());
			buf.append(e.key + "=" + e.value);
			if (i < max)
				buf.append(", ");
		}
		buf.append("}");
		return buf.toString();
	}
	/**
	 * Returns a Collection view of the values contained in this HashPointerTable.
	 * The Collection is backed by the HashPointerTable, so changes to the HashPointerTable
	 * are reflected in the Collection, and vice-versa.  The Collection
	 * supports element removal (which removes the corresponding entry from
	 * the HashPointerTable), but not element addition.
	 *
	 * @since JDK1.2
	 */
	public Collection values() {
		if (values == null)
			values = Collections.synchronizedCollection(new ValueCollection());
		return values;
	}
	/**
	 * Save the state of the HashPointerTable to a stream (i.e., serialize it).
	 *
	 * @serialData The <i>capacity</i> of the HashPointerTable (the length of the
	 *		   bucket array) is emitted (int), followed  by the
	 *		   <i>size</i> of the HashPointerTable (the number of key-value
	 *		   mappings), followed by the key (Object) and value (Object)
	 *		   for each key-value mapping represented by the HashPointerTable
	 *		   The key-value mappings are emitted in no particular order.
	 */
	private synchronized void writeObject(java.io.ObjectOutputStream s) throws IOException {
		// Write out the length, threshold, loadfactor
		s.defaultWriteObject();

		// Write out length, count of elements and then the key/value objects
		s.writeInt(table.length);
		s.writeInt(count);
		for (int index = table.length - 1; index >= 0; index--) {
			Entry entry = table[index];
			while (entry != null) {
				s.writeObject(entry.key);
				s.writeObject(entry.value);
				entry = entry.next;
			}
		}
	}
}
