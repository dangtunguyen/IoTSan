package edu.ksu.cis.bandera.abstraction.specification.node;

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
/* This file was generated by SableCC (http://www.sable.mcgill.ca/sablecc/). */

import java.util.*;
import edu.ksu.cis.bandera.abstraction.specification.analysis.*;

public final class AAbstractFunction extends PAbstractFunction
{
	private TAbstract _abstract_;
	private TLParen _lParen_;
	private TId _id_;
	private TRParen _rParen_;
	private TBegin _begin_;
	private final LinkedList _abstractDef_ = new TypedLinkedList(new AbstractDef_Cast());
	private TEnd _end_;

	private class AbstractDef_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PAbstractDef node = (PAbstractDef) o;

			if((node.parent() != null) &&
				(node.parent() != AAbstractFunction.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AAbstractFunction.this))
			{
				node.parent(AAbstractFunction.this);
			}

			return node;
		}
	}
	public AAbstractFunction()
	{
	}
	public AAbstractFunction(
		TAbstract _abstract_,
		TLParen _lParen_,
		TId _id_,
		TRParen _rParen_,
		TBegin _begin_,
		XPAbstractDef _abstractDef_,
		TEnd _end_)
	{
		setAbstract(_abstract_);

		setLParen(_lParen_);

		setId(_id_);

		setRParen(_rParen_);

		setBegin(_begin_);

		if(_abstractDef_ != null)
		{
			while(_abstractDef_ instanceof X1PAbstractDef)
			{
				this._abstractDef_.addFirst(((X1PAbstractDef) _abstractDef_).getPAbstractDef());
				_abstractDef_ = ((X1PAbstractDef) _abstractDef_).getXPAbstractDef();
			}
			this._abstractDef_.addFirst(((X2PAbstractDef) _abstractDef_).getPAbstractDef());
		}

		setEnd(_end_);

	}
	public AAbstractFunction(
		TAbstract _abstract_,
		TLParen _lParen_,
		TId _id_,
		TRParen _rParen_,
		TBegin _begin_,
		List _abstractDef_,
		TEnd _end_)
	{
		setAbstract(_abstract_);

		setLParen(_lParen_);

		setId(_id_);

		setRParen(_rParen_);

		setBegin(_begin_);

		{
			Object temp[] = _abstractDef_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._abstractDef_.add(temp[i]);
			}
		}

		setEnd(_end_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAAbstractFunction(this);
	}
	public Object clone()
	{
		return new AAbstractFunction(
			(TAbstract) cloneNode(_abstract_),
			(TLParen) cloneNode(_lParen_),
			(TId) cloneNode(_id_),
			(TRParen) cloneNode(_rParen_),
			(TBegin) cloneNode(_begin_),
			cloneList(_abstractDef_),
			(TEnd) cloneNode(_end_));
	}
	public TAbstract getAbstract()
	{
		return _abstract_;
	}
	public LinkedList getAbstractDef()
	{
		return _abstractDef_;
	}
	public TBegin getBegin()
	{
		return _begin_;
	}
	public TEnd getEnd()
	{
		return _end_;
	}
	public TId getId()
	{
		return _id_;
	}
	public TLParen getLParen()
	{
		return _lParen_;
	}
	public TRParen getRParen()
	{
		return _rParen_;
	}
	void removeChild(Node child)
	{
		if(_abstract_ == child)
		{
			_abstract_ = null;
			return;
		}

		if(_lParen_ == child)
		{
			_lParen_ = null;
			return;
		}

		if(_id_ == child)
		{
			_id_ = null;
			return;
		}

		if(_rParen_ == child)
		{
			_rParen_ = null;
			return;
		}

		if(_begin_ == child)
		{
			_begin_ = null;
			return;
		}

		if(_abstractDef_.remove(child))
		{
			return;
		}

		if(_end_ == child)
		{
			_end_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_abstract_ == oldChild)
		{
			setAbstract((TAbstract) newChild);
			return;
		}

		if(_lParen_ == oldChild)
		{
			setLParen((TLParen) newChild);
			return;
		}

		if(_id_ == oldChild)
		{
			setId((TId) newChild);
			return;
		}

		if(_rParen_ == oldChild)
		{
			setRParen((TRParen) newChild);
			return;
		}

		if(_begin_ == oldChild)
		{
			setBegin((TBegin) newChild);
			return;
		}

		for(ListIterator i = _abstractDef_.listIterator(); i.hasNext();)
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

		if(_end_ == oldChild)
		{
			setEnd((TEnd) newChild);
			return;
		}

	}
	public void setAbstract(TAbstract node)
	{
		if(_abstract_ != null)
		{
			_abstract_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_abstract_ = node;
	}
	public void setAbstractDef(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_abstractDef_.add(temp[i]);
		}
	}
	public void setBegin(TBegin node)
	{
		if(_begin_ != null)
		{
			_begin_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_begin_ = node;
	}
	public void setEnd(TEnd node)
	{
		if(_end_ != null)
		{
			_end_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_end_ = node;
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
	public void setLParen(TLParen node)
	{
		if(_lParen_ != null)
		{
			_lParen_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_lParen_ = node;
	}
	public void setRParen(TRParen node)
	{
		if(_rParen_ != null)
		{
			_rParen_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_rParen_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_abstract_)
			+ toString(_lParen_)
			+ toString(_id_)
			+ toString(_rParen_)
			+ toString(_begin_)
			+ toString(_abstractDef_)
			+ toString(_end_);
	}
}