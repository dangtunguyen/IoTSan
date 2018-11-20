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

public final class AConstructorDeclaration extends PConstructorDeclaration
{
	private final LinkedList _modifier_ = new TypedLinkedList(new Modifier_Cast());
	private PConstructorDeclarator _constructorDeclarator_;
	private PThrows _throws_;
	private PConstructorBody _constructorBody_;

	private class Modifier_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PModifier node = (PModifier) o;

			if((node.parent() != null) &&
				(node.parent() != AConstructorDeclaration.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AConstructorDeclaration.this))
			{
				node.parent(AConstructorDeclaration.this);
			}

			return node;
		}
	}
	public AConstructorDeclaration()
	{
	}
	public AConstructorDeclaration(
		List _modifier_,
		PConstructorDeclarator _constructorDeclarator_,
		PThrows _throws_,
		PConstructorBody _constructorBody_)
	{
		{
			Object temp[] = _modifier_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._modifier_.add(temp[i]);
			}
		}

		setConstructorDeclarator(_constructorDeclarator_);

		setThrows(_throws_);

		setConstructorBody(_constructorBody_);

	}
	public AConstructorDeclaration(
		XPModifier _modifier_,
		PConstructorDeclarator _constructorDeclarator_,
		PThrows _throws_,
		PConstructorBody _constructorBody_)
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

		setConstructorDeclarator(_constructorDeclarator_);

		setThrows(_throws_);

		setConstructorBody(_constructorBody_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAConstructorDeclaration(this);
	}
	public Object clone()
	{
		return new AConstructorDeclaration(
			cloneList(_modifier_),
			(PConstructorDeclarator) cloneNode(_constructorDeclarator_),
			(PThrows) cloneNode(_throws_),
			(PConstructorBody) cloneNode(_constructorBody_));
	}
	public PConstructorBody getConstructorBody()
	{
		return _constructorBody_;
	}
	public PConstructorDeclarator getConstructorDeclarator()
	{
		return _constructorDeclarator_;
	}
	public LinkedList getModifier()
	{
		return _modifier_;
	}
	public PThrows getThrows()
	{
		return _throws_;
	}
	void removeChild(Node child)
	{
		if(_modifier_.remove(child))
		{
			return;
		}

		if(_constructorDeclarator_ == child)
		{
			_constructorDeclarator_ = null;
			return;
		}

		if(_throws_ == child)
		{
			_throws_ = null;
			return;
		}

		if(_constructorBody_ == child)
		{
			_constructorBody_ = null;
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

		if(_constructorDeclarator_ == oldChild)
		{
			setConstructorDeclarator((PConstructorDeclarator) newChild);
			return;
		}

		if(_throws_ == oldChild)
		{
			setThrows((PThrows) newChild);
			return;
		}

		if(_constructorBody_ == oldChild)
		{
			setConstructorBody((PConstructorBody) newChild);
			return;
		}

	}
	public void setConstructorBody(PConstructorBody node)
	{
		if(_constructorBody_ != null)
		{
			_constructorBody_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_constructorBody_ = node;
	}
	public void setConstructorDeclarator(PConstructorDeclarator node)
	{
		if(_constructorDeclarator_ != null)
		{
			_constructorDeclarator_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_constructorDeclarator_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_modifier_)
			+ toString(_constructorDeclarator_)
			+ toString(_throws_)
			+ toString(_constructorBody_);
	}
}
