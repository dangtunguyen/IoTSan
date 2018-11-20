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

public final class AVoidMethodHeader extends PMethodHeader
{
	private final LinkedList _modifier_ = new TypedLinkedList(new Modifier_Cast());
	private TVoid _void_;
	private PMethodDeclarator _methodDeclarator_;
	private PThrows _throws_;

	private class Modifier_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PModifier node = (PModifier) o;

			if((node.parent() != null) &&
				(node.parent() != AVoidMethodHeader.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AVoidMethodHeader.this))
			{
				node.parent(AVoidMethodHeader.this);
			}

			return node;
		}
	}
	public AVoidMethodHeader()
	{
	}
	public AVoidMethodHeader(
		List _modifier_,
		TVoid _void_,
		PMethodDeclarator _methodDeclarator_,
		PThrows _throws_)
	{
		{
			Object temp[] = _modifier_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._modifier_.add(temp[i]);
			}
		}

		setVoid(_void_);

		setMethodDeclarator(_methodDeclarator_);

		setThrows(_throws_);

	}
	public AVoidMethodHeader(
		XPModifier _modifier_,
		TVoid _void_,
		PMethodDeclarator _methodDeclarator_,
		PThrows _throws_)
	{
		if(_modifier_ != null)
		{
			while(_modifier_ instanceof X1PModifier)
			{
				this._modifier_.addFirst(((X1PModifier) _modifier_).getPModifier());
				_modifier_ = ((X1PModifier) _modifier_).getXPModifier();
			}
			this._modifier_.addFirst(((X2PModifier) _modifier_).getPModifier());
		}

		setVoid(_void_);

		setMethodDeclarator(_methodDeclarator_);

		setThrows(_throws_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAVoidMethodHeader(this);
	}
	public Object clone()
	{
		return new AVoidMethodHeader(
			cloneList(_modifier_),
			(TVoid) cloneNode(_void_),
			(PMethodDeclarator) cloneNode(_methodDeclarator_),
			(PThrows) cloneNode(_throws_));
	}
	public PMethodDeclarator getMethodDeclarator()
	{
		return _methodDeclarator_;
	}
	public LinkedList getModifier()
	{
		return _modifier_;
	}
	public PThrows getThrows()
	{
		return _throws_;
	}
	public TVoid getVoid()
	{
		return _void_;
	}
	void removeChild(Node child)
	{
		if(_modifier_.remove(child))
		{
			return;
		}

		if(_void_ == child)
		{
			_void_ = null;
			return;
		}

		if(_methodDeclarator_ == child)
		{
			_methodDeclarator_ = null;
			return;
		}

		if(_throws_ == child)
		{
			_throws_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		for(ListIterator i = _modifier_.listIterator(); i.hasNext();)
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

		if(_void_ == oldChild)
		{
			setVoid((TVoid) newChild);
			return;
		}

		if(_methodDeclarator_ == oldChild)
		{
			setMethodDeclarator((PMethodDeclarator) newChild);
			return;
		}

		if(_throws_ == oldChild)
		{
			setThrows((PThrows) newChild);
			return;
		}

	}
	public void setMethodDeclarator(PMethodDeclarator node)
	{
		if(_methodDeclarator_ != null)
		{
			_methodDeclarator_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_methodDeclarator_ = node;
	}
	public void setModifier(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_modifier_.add(temp[i]);
		}
	}
	public void setThrows(PThrows node)
	{
		if(_throws_ != null)
		{
			_throws_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_throws_ = node;
	}
	public void setVoid(TVoid node)
	{
		if(_void_ != null)
		{
			_void_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_void_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_modifier_)
			+ toString(_void_)
			+ toString(_methodDeclarator_)
			+ toString(_throws_);
	}
}
