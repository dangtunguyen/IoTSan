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

public final class AInterfaces extends PInterfaces
{
	private TImplements _implements_;
	private final LinkedList _name_ = new TypedLinkedList(new Name_Cast());

	private class Name_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PName node = (PName) o;

			if((node.parent() != null) &&
				(node.parent() != AInterfaces.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AInterfaces.this))
			{
				node.parent(AInterfaces.this);
			}

			return node;
		}
	}
	public AInterfaces()
	{
	}
	public AInterfaces(
		TImplements _implements_,
		List _name_)
	{
		setImplements(_implements_);

		{
			Object temp[] = _name_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._name_.add(temp[i]);
			}
		}

	}
	public AInterfaces(
		TImplements _implements_,
		XPName _name_)
	{
		setImplements(_implements_);

		if(_name_ != null)
		{
			while(_name_ instanceof X1PName)
			{
				this._name_.addFirst(((X1PName) _name_).getPName());
				_name_ = ((X1PName) _name_).getXPName();
			}
			this._name_.addFirst(((X2PName) _name_).getPName());
		}

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAInterfaces(this);
	}
	public Object clone()
	{
		return new AInterfaces(
			(TImplements) cloneNode(_implements_),
			cloneList(_name_));
	}
	public TImplements getImplements()
	{
		return _implements_;
	}
	public LinkedList getName()
	{
		return _name_;
	}
	void removeChild(Node child)
	{
		if(_implements_ == child)
		{
			_implements_ = null;
			return;
		}

		if(_name_.remove(child))
		{
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_implements_ == oldChild)
		{
			setImplements((TImplements) newChild);
			return;
		}

		for(ListIterator i = _name_.listIterator(); i.hasNext();)
		{
			if(i.next() == oldChild)
			{
				if(newChild != null)
				{
					i.set(newChild);
					oldChild.parent(null);
					return;
				}

				i.remove();
				oldChild.parent(null);
				return;
			}
		}

	}
	public void setImplements(TImplements node)
	{
		if(_implements_ != null)
		{
			_implements_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_implements_ = node;
	}
	public void setName(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_name_.add(temp[i]);
		}
	}
	public String toString()
	{
		return ""
			+ toString(_implements_)
			+ toString(_name_);
	}
}
