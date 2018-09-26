package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import ca.mcgill.sable.util.*;

public class StateVarVector { 

	int size = 0;
	StateVar [] data;

	public StateVarVector() { this(10); }
	public StateVarVector(int capacity) {
	data = new StateVar[capacity];
	}
	public StateVarVector(StateVar x) {
	this(10);
	addElement(x);
	}
	public void addElement(StateVar x) {
	if (x == null)
	    throw new RuntimeException("Element cannot be null");
	if (size == data.length)
	    expand();
	data[size++] = x;
	}
	public boolean contains(StateVar x) {
	for (int i = 0; i < size; i++) 
	    if (data[i].equals(x))
		return true;
	return false;
	}
	public StateVar elementAt(int pos) {
	if (pos < 0 || pos >= size) 
	    throw new RuntimeException("Position invalid: " + pos);
	return data[pos];
	}
	void expand() {
	StateVar [] newData = new StateVar[data.length * 2];
	for (int i = 0; i < data.length; i++) 
	    newData[i] = data[i];
	data = newData;
	}
	public StateVar firstElement() { return elementAt(0); }
	public int indexOf(StateVar x) {
	for (int i = 0; i < size; i++) 
	    if (data[i] == x)
		return i;
	return -1;
	}
	public void insertElementAt(StateVar x, int pos) {
	if (x == null)
	    throw new RuntimeException("Element cannot be null");
	if (size == data.length)
	    expand();
	if (pos > size || pos < 0)
	    throw new RuntimeException("Position invalid: " + pos);
	for (int i = size; i > pos; i--) 
	    data[i] = data[i-1];
	data[pos] = x;
	size++;
	}
	public int size() { return size; }
}
