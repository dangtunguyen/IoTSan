package edu.ksu.cis.bandera.jjjc.node;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import edu.ksu.cis.bandera.jjjc.analysis.*;

public abstract class Node implements Switchable, Cloneable
{
	private Node parent;

	public abstract Object clone();
	protected List cloneList(List list)
	{
		List clone = new LinkedList();

		for(Iterator i = list.iterator(); i.hasNext();)
		{
			clone.add(((Node) i.next()).clone());
		}

		return clone;
	}
	protected Node cloneNode(Node node)
	{
		if(node != null)
		{
			return (Node) node.clone();
		}

		return null;
	}
	public Node parent()
	{
		return parent;
	}
	void parent(Node parent)
	{
		this.parent = parent;
	}
	abstract void removeChild(Node child);
	public void replaceBy(Node node)
	{
		if(parent != null)
		{
			parent.replaceChild(this, node);
		}
	}
	abstract void replaceChild(Node oldChild, Node newChild);
	protected String toString(List list)
	{
		StringBuffer s = new StringBuffer();

		for(Iterator i = list.iterator(); i.hasNext();)
		{
			s.append(i.next());
		}

		return s.toString();
	}
	protected String toString(Node node)
	{
		if(node != null)
		{
			return node.toString();
		}

		return "";
	}
}
