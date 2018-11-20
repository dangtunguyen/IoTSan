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

public final class AOldInterfaceDeclaration extends PInterfaceDeclaration
{
	private final LinkedList _modifier_ = new TypedLinkedList(new Modifier_Cast());
	private TInterface _interface_;
	private TId _id_;
	private PExtendsInterfaces _extendsInterfaces_;
	private PInterfaceBody _interfaceBody_;

	private class Modifier_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PModifier node = (PModifier) o;

			if((node.parent() != null) &&
				(node.parent() != AOldInterfaceDeclaration.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AOldInterfaceDeclaration.this))
			{
				node.parent(AOldInterfaceDeclaration.this);
			}

			return node;
		}
	}
	public AOldInterfaceDeclaration()
	{
	}
	public AOldInterfaceDeclaration(
		List _modifier_,
		TInterface _interface_,
		TId _id_,
		PExtendsInterfaces _extendsInterfaces_,
		PInterfaceBody _interfaceBody_)
	{
		{
			Object temp[] = _modifier_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._modifier_.add(temp[i]);
			}
		}

		setInterface(_interface_);

		setId(_id_);

		setExtendsInterfaces(_extendsInterfaces_);

		setInterfaceBody(_interfaceBody_);

	}
	public AOldInterfaceDeclaration(
		XPModifier _modifier_,
		TInterface _interface_,
		TId _id_,
		PExtendsInterfaces _extendsInterfaces_,
		PInterfaceBody _interfaceBody_)
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

		setInterface(_interface_);

		setId(_id_);

		setExtendsInterfaces(_extendsInterfaces_);

		setInterfaceBody(_interfaceBody_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldInterfaceDeclaration(this);
	}
	public Object clone()
	{
		return new AOldInterfaceDeclaration(
			cloneList(_modifier_),
			(TInterface) cloneNode(_interface_),
			(TId) cloneNode(_id_),
			(PExtendsInterfaces) cloneNode(_extendsInterfaces_),
			(PInterfaceBody) cloneNode(_interfaceBody_));
	}
	public PExtendsInterfaces getExtendsInterfaces()
	{
		return _extendsInterfaces_;
	}
	public TId getId()
	{
		return _id_;
	}
	public TInterface getInterface()
	{
		return _interface_;
	}
	public PInterfaceBody getInterfaceBody()
	{
		return _interfaceBody_;
	}
	public LinkedList getModifier()
	{
		return _modifier_;
	}
	void removeChild(Node child)
	{
		if(_modifier_.remove(child))
		{
			return;
		}

		if(_interface_ == child)
		{
			_interface_ = null;
			return;
		}

		if(_id_ == child)
		{
			_id_ = null;
			return;
		}

		if(_extendsInterfaces_ == child)
		{
			_extendsInterfaces_ = null;
			return;
		}

		if(_interfaceBody_ == child)
		{
			_interfaceBody_ = null;
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

		if(_interface_ == oldChild)
		{
			setInterface((TInterface) newChild);
			return;
		}

		if(_id_ == oldChild)
		{
			setId((TId) newChild);
			return;
		}

		if(_extendsInterfaces_ == oldChild)
		{
			setExtendsInterfaces((PExtendsInterfaces) newChild);
			return;
		}

		if(_interfaceBody_ == oldChild)
		{
			setInterfaceBody((PInterfaceBody) newChild);
			return;
		}

	}
	public void setExtendsInterfaces(PExtendsInterfaces node)
	{
		if(_extendsInterfaces_ != null)
		{
			_extendsInterfaces_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_extendsInterfaces_ = node;
	}
	public void setId(TId node)
	{
		if(_id_ != null)
		{
			_id_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_id_ = node;
	}
	public void setInterface(TInterface node)
	{
		if(_interface_ != null)
		{
			_interface_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_interface_ = node;
	}
	public void setInterfaceBody(PInterfaceBody node)
	{
		if(_interfaceBody_ != null)
		{
			_interfaceBody_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_interfaceBody_ = node;
	}
	public void setModifier(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_modifier_.add(temp[i]);
		}
	}
	public String toString()
	{
		return ""
			+ toString(_modifier_)
			+ toString(_interface_)
			+ toString(_id_)
			+ toString(_extendsInterfaces_)
			+ toString(_interfaceBody_);
	}
}
