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

public final class AExpListForInit extends PForInit
{
	private final LinkedList _exp_ = new TypedLinkedList(new Exp_Cast());

	private class Exp_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PExp node = (PExp) o;

			if((node.parent() != null) &&
				(node.parent() != AExpListForInit.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AExpListForInit.this))
			{
				node.parent(AExpListForInit.this);
			}

			return node;
		}
	}
	public AExpListForInit()
	{
	}
	public AExpListForInit(
		List _exp_)
	{
		{
			Object temp[] = _exp_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._exp_.add(temp[i]);
			}
		}

	}
	public AExpListForInit(
		XPExp _exp_)
	{
		if(_exp_ != null)
		{
			while(_exp_ instanceof X1PExp)
			{
				this._exp_.addFirst(((X1PExp) _exp_).getPExp());
				_exp_ = ((X1PExp) _exp_).getXPExp();
			}
			this._exp_.addFirst(((X2PExp) _exp_).getPExp());
		}

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAExpListForInit(this);
	}
	public Object clone()
	{
		return new AExpListForInit(
			cloneList(_exp_));
	}
	public LinkedList getExp()
	{
		return _exp_;
	}
	void removeChild(Node child)
	{
		if(_exp_.remove(child))
		{
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		for(ListIterator i = _exp_.listIterator(); i.hasNext();)
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
	public void setExp(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_exp_.add(temp[i]);
		}
	}
	public String toString()
	{
		return ""
			+ toString(_exp_);
	}
}
