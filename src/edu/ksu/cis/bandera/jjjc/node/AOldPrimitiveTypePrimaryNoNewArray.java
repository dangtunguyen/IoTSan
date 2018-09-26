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

public final class AOldPrimitiveTypePrimaryNoNewArray extends PPrimaryNoNewArray
{
	private PPrimitiveType _primitiveType_;
	private final LinkedList _dim_ = new TypedLinkedList(new Dim_Cast());
	private TDot _dot_;
	private TClass _tClass_;

	private class Dim_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PDim node = (PDim) o;

			if((node.parent() != null) &&
				(node.parent() != AOldPrimitiveTypePrimaryNoNewArray.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AOldPrimitiveTypePrimaryNoNewArray.this))
			{
				node.parent(AOldPrimitiveTypePrimaryNoNewArray.this);
			}

			return node;
		}
	}
	public AOldPrimitiveTypePrimaryNoNewArray()
	{
	}
	public AOldPrimitiveTypePrimaryNoNewArray(
		PPrimitiveType _primitiveType_,
		List _dim_,
		TDot _dot_,
		TClass _tClass_)
	{
		setPrimitiveType(_primitiveType_);

		{
			Object temp[] = _dim_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._dim_.add(temp[i]);
			}
		}

		setDot(_dot_);

		setTClass(_tClass_);

	}
	public AOldPrimitiveTypePrimaryNoNewArray(
		PPrimitiveType _primitiveType_,
		XPDim _dim_,
		TDot _dot_,
		TClass _tClass_)
	{
		setPrimitiveType(_primitiveType_);

		if(_dim_ != null)
		{
			while(_dim_ instanceof X1PDim)
			{
				this._dim_.addFirst(((X1PDim) _dim_).getPDim());
				_dim_ = ((X1PDim) _dim_).getXPDim();
			}
			this._dim_.addFirst(((X2PDim) _dim_).getPDim());
		}

		setDot(_dot_);

		setTClass(_tClass_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldPrimitiveTypePrimaryNoNewArray(this);
	}
	public Object clone()
	{
		return new AOldPrimitiveTypePrimaryNoNewArray(
			(PPrimitiveType) cloneNode(_primitiveType_),
			cloneList(_dim_),
			(TDot) cloneNode(_dot_),
			(TClass) cloneNode(_tClass_));
	}
	public LinkedList getDim()
	{
		return _dim_;
	}
	public TDot getDot()
	{
		return _dot_;
	}
	public PPrimitiveType getPrimitiveType()
	{
		return _primitiveType_;
	}
	public TClass getTClass()
	{
		return _tClass_;
	}
	void removeChild(Node child)
	{
		if(_primitiveType_ == child)
		{
			_primitiveType_ = null;
			return;
		}

		if(_dim_.remove(child))
		{
			return;
		}

		if(_dot_ == child)
		{
			_dot_ = null;
			return;
		}

		if(_tClass_ == child)
		{
			_tClass_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_primitiveType_ == oldChild)
		{
			setPrimitiveType((PPrimitiveType) newChild);
			return;
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

		if(_dot_ == oldChild)
		{
			setDot((TDot) newChild);
			return;
		}

		if(_tClass_ == oldChild)
		{
			setTClass((TClass) newChild);
			return;
		}

	}
	public void setDim(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_dim_.add(temp[i]);
		}
	}
	public void setDot(TDot node)
	{
		if(_dot_ != null)
		{
			_dot_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_dot_ = node;
	}
	public void setPrimitiveType(PPrimitiveType node)
	{
		if(_primitiveType_ != null)
		{
			_primitiveType_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_primitiveType_ = node;
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
			+ toString(_primitiveType_)
			+ toString(_dim_)
			+ toString(_dot_)
			+ toString(_tClass_);
	}
}
