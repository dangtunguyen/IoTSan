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

public final class AClassDeclaration extends PClassDeclaration
{
	private final LinkedList _modifier_ = new TypedLinkedList(new Modifier_Cast());
	private TClass _tClass_;
	private TId _id_;
	private PSuper _super_;
	private PInterfaces _interfaces_;
	private PClassBody _classBody_;

	private class Modifier_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PModifier node = (PModifier) o;

			if((node.parent() != null) &&
				(node.parent() != AClassDeclaration.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AClassDeclaration.this))
			{
				node.parent(AClassDeclaration.this);
			}

			return node;
		}
	}
	public AClassDeclaration()
	{
	}
	public AClassDeclaration(
		List _modifier_,
		TClass _tClass_,
		TId _id_,
		PSuper _super_,
		PInterfaces _interfaces_,
		PClassBody _classBody_)
	{
		{
			Object temp[] = _modifier_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._modifier_.add(temp[i]);
			}
		}

		setTClass(_tClass_);

		setId(_id_);

		setSuper(_super_);

		setInterfaces(_interfaces_);

		setClassBody(_classBody_);

	}
	public AClassDeclaration(
		XPModifier _modifier_,
		TClass _tClass_,
		TId _id_,
		PSuper _super_,
		PInterfaces _interfaces_,
		PClassBody _classBody_)
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

		setTClass(_tClass_);

		setId(_id_);

		setSuper(_super_);

		setInterfaces(_interfaces_);

		setClassBody(_classBody_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAClassDeclaration(this);
	}
	public Object clone()
	{
		return new AClassDeclaration(
			cloneList(_modifier_),
			(TClass) cloneNode(_tClass_),
			(TId) cloneNode(_id_),
			(PSuper) cloneNode(_super_),
			(PInterfaces) cloneNode(_interfaces_),
			(PClassBody) cloneNode(_classBody_));
	}
	public PClassBody getClassBody()
	{
		return _classBody_;
	}
	public TId getId()
	{
		return _id_;
	}
	public PInterfaces getInterfaces()
	{
		return _interfaces_;
	}
	public LinkedList getModifier()
	{
		return _modifier_;
	}
	public PSuper getSuper()
	{
		return _super_;
	}
	public TClass getTClass()
	{
		return _tClass_;
	}
	void removeChild(Node child)
	{
		if(_modifier_.remove(child))
		{
			return;
		}

		if(_tClass_ == child)
		{
			_tClass_ = null;
			return;
		}

		if(_id_ == child)
		{
			_id_ = null;
			return;
		}

		if(_super_ == child)
		{
			_super_ = null;
			return;
		}

		if(_interfaces_ == child)
		{
			_interfaces_ = null;
			return;
		}

		if(_classBody_ == child)
		{
			_classBody_ = null;
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

		if(_tClass_ == oldChild)
		{
			setTClass((TClass) newChild);
			return;
		}

		if(_id_ == oldChild)
		{
			setId((TId) newChild);
			return;
		}

		if(_super_ == oldChild)
		{
			setSuper((PSuper) newChild);
			return;
		}

		if(_interfaces_ == oldChild)
		{
			setInterfaces((PInterfaces) newChild);
			return;
		}

		if(_classBody_ == oldChild)
		{
			setClassBody((PClassBody) newChild);
			return;
		}

	}
	public void setClassBody(PClassBody node)
	{
		if(_classBody_ != null)
		{
			_classBody_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_classBody_ = node;
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
	public void setInterfaces(PInterfaces node)
	{
		if(_interfaces_ != null)
		{
			_interfaces_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_interfaces_ = node;
	}
	public void setModifier(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_modifier_.add(temp[i]);
		}
	}
	public void setSuper(PSuper node)
	{
		if(_super_ != null)
		{
			_super_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_super_ = node;
	}
	public void setTClass(TClass node)
	{
		if(_tClass_ != null)
		{
			_tClass_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_tClass_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_modifier_)
			+ toString(_tClass_)
			+ toString(_id_)
			+ toString(_super_)
			+ toString(_interfaces_)
			+ toString(_classBody_);
	}
}
