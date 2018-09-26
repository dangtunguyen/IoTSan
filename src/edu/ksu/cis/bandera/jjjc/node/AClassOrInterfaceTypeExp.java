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

public final class AClassOrInterfaceTypeExp extends PExp
{
	private TNew _new_;
	private PName _classOrInterfaceType_;
	private final LinkedList _dimExp_ = new TypedLinkedList(new DimExp_Cast());
	private final LinkedList _dim_ = new TypedLinkedList(new Dim_Cast());

	private class DimExp_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PDimExp node = (PDimExp) o;

			if((node.parent() != null) &&
				(node.parent() != AClassOrInterfaceTypeExp.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AClassOrInterfaceTypeExp.this))
			{
				node.parent(AClassOrInterfaceTypeExp.this);
			}

			return node;
		}
	}

	private class Dim_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PDim node = (PDim) o;

			if((node.parent() != null) &&
				(node.parent() != AClassOrInterfaceTypeExp.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AClassOrInterfaceTypeExp.this))
			{
				node.parent(AClassOrInterfaceTypeExp.this);
			}

			return node;
		}
	}
	public AClassOrInterfaceTypeExp()
	{
	}
	public AClassOrInterfaceTypeExp(
		TNew _new_,
		PName _classOrInterfaceType_,
		List _dimExp_,
		List _dim_)
	{
		setNew(_new_);

		setClassOrInterfaceType(_classOrInterfaceType_);

		{
			Object temp[] = _dimExp_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._dimExp_.add(temp[i]);
			}
		}

		{
			Object temp[] = _dim_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._dim_.add(temp[i]);
			}
		}

	}
	public AClassOrInterfaceTypeExp(
		TNew _new_,
		PName _classOrInterfaceType_,
		XPDimExp _dimExp_,
		XPDim _dim_)
	{
		setNew(_new_);

		setClassOrInterfaceType(_classOrInterfaceType_);

		if(_dimExp_ != null)
		{
			while(_dimExp_ instanceof X1PDimExp)
			{
				this._dimExp_.addFirst(((X1PDimExp) _dimExp_).getPDimExp());
				_dimExp_ = ((X1PDimExp) _dimExp_).getXPDimExp();
			}
			this._dimExp_.addFirst(((X2PDimExp) _dimExp_).getPDimExp());
		}

		if(_dim_ != null)
		{
			while(_dim_ instanceof X1PDim)
			{
				this._dim_.addFirst(((X1PDim) _dim_).getPDim());
				_dim_ = ((X1PDim) _dim_).getXPDim();
			}
			this._dim_.addFirst(((X2PDim) _dim_).getPDim());
		}

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAClassOrInterfaceTypeExp(this);
	}
	public Object clone()
	{
		return new AClassOrInterfaceTypeExp(
			(TNew) cloneNode(_new_),
			(PName) cloneNode(_classOrInterfaceType_),
			cloneList(_dimExp_),
			cloneList(_dim_));
	}
	public PName getClassOrInterfaceType()
	{
		return _classOrInterfaceType_;
	}
	public LinkedList getDim()
	{
		return _dim_;
	}
	public LinkedList getDimExp()
	{
		return _dimExp_;
	}
	public TNew getNew()
	{
		return _new_;
	}
	void removeChild(Node child)
	{
		if(_new_ == child)
		{
			_new_ = null;
			return;
		}

		if(_classOrInterfaceType_ == child)
		{
			_classOrInterfaceType_ = null;
			return;
		}

		if(_dimExp_.remove(child))
		{
			return;
		}

		if(_dim_.remove(child))
		{
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_new_ == oldChild)
		{
			setNew((TNew) newChild);
			return;
		}

		if(_classOrInterfaceType_ == oldChild)
		{
			setClassOrInterfaceType((PName) newChild);
			return;
		}

		for(ListIterator i = _dimExp_.listIterator(); i.hasNext();)
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

		for(ListIterator i = _dim_.listIterator(); i.hasNext();)
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
	public void setClassOrInterfaceType(PName node)
	{
		if(_classOrInterfaceType_ != null)
		{
			_classOrInterfaceType_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_classOrInterfaceType_ = node;
	}
	public void setDim(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_dim_.add(temp[i]);
		}
	}
	public void setDimExp(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_dimExp_.add(temp[i]);
		}
	}
	public void setNew(TNew node)
	{
		if(_new_ != null)
		{
			_new_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_new_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_new_)
			+ toString(_classOrInterfaceType_)
			+ toString(_dimExp_)
			+ toString(_dim_);
	}
}
