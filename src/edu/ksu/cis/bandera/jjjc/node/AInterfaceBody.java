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

public final class AInterfaceBody extends PInterfaceBody
{
	private TLBrace _lBrace_;
	private final LinkedList _interfaceMemberDeclaration_ = new TypedLinkedList(new InterfaceMemberDeclaration_Cast());
	private TRBrace _rBrace_;

	private class InterfaceMemberDeclaration_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PInterfaceMemberDeclaration node = (PInterfaceMemberDeclaration) o;

			if((node.parent() != null) &&
				(node.parent() != AInterfaceBody.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AInterfaceBody.this))
			{
				node.parent(AInterfaceBody.this);
			}

			return node;
		}
	}
	public AInterfaceBody()
	{
	}
	public AInterfaceBody(
		TLBrace _lBrace_,
		List _interfaceMemberDeclaration_,
		TRBrace _rBrace_)
	{
		setLBrace(_lBrace_);

		{
			Object temp[] = _interfaceMemberDeclaration_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._interfaceMemberDeclaration_.add(temp[i]);
			}
		}

		setRBrace(_rBrace_);

	}
	public AInterfaceBody(
		TLBrace _lBrace_,
		XPInterfaceMemberDeclaration _interfaceMemberDeclaration_,
		TRBrace _rBrace_)
	{
		setLBrace(_lBrace_);

		if(_interfaceMemberDeclaration_ != null)
		{
			while(_interfaceMemberDeclaration_ instanceof X1PInterfaceMemberDeclaration)
			{
				this._interfaceMemberDeclaration_.addFirst(((X1PInterfaceMemberDeclaration) _interfaceMemberDeclaration_).getPInterfaceMemberDeclaration());
				_interfaceMemberDeclaration_ = ((X1PInterfaceMemberDeclaration) _interfaceMemberDeclaration_).getXPInterfaceMemberDeclaration();
			}
			this._interfaceMemberDeclaration_.addFirst(((X2PInterfaceMemberDeclaration) _interfaceMemberDeclaration_).getPInterfaceMemberDeclaration());
		}

		setRBrace(_rBrace_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAInterfaceBody(this);
	}
	public Object clone()
	{
		return new AInterfaceBody(
			(TLBrace) cloneNode(_lBrace_),
			cloneList(_interfaceMemberDeclaration_),
			(TRBrace) cloneNode(_rBrace_));
	}
	public LinkedList getInterfaceMemberDeclaration()
	{
		return _interfaceMemberDeclaration_;
	}
	public TLBrace getLBrace()
	{
		return _lBrace_;
	}
	public TRBrace getRBrace()
	{
		return _rBrace_;
	}
	void removeChild(Node child)
	{
		if(_lBrace_ == child)
		{
			_lBrace_ = null;
			return;
		}

		if(_interfaceMemberDeclaration_.remove(child))
		{
			return;
		}

		if(_rBrace_ == child)
		{
			_rBrace_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_lBrace_ == oldChild)
		{
			setLBrace((TLBrace) newChild);
			return;
		}

		for(ListIterator i = _interfaceMemberDeclaration_.listIterator(); i.hasNext();)
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

		if(_rBrace_ == oldChild)
		{
			setRBrace((TRBrace) newChild);
			return;
		}

	}
	public void setInterfaceMemberDeclaration(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_interfaceMemberDeclaration_.add(temp[i]);
		}
	}
	public void setLBrace(TLBrace node)
	{
		if(_lBrace_ != null)
		{
			_lBrace_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_lBrace_ = node;
	}
	public void setRBrace(TRBrace node)
	{
		if(_rBrace_ != null)
		{
			_rBrace_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_rBrace_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_lBrace_)
			+ toString(_interfaceMemberDeclaration_)
			+ toString(_rBrace_);
	}
}
