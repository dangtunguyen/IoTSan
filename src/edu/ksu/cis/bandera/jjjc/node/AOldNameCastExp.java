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

public final class AOldNameCastExp extends PCastExp
{
	private TLPar _lPar_;
	private PName _name_;
	private final LinkedList _dim_ = new TypedLinkedList(new Dim_Cast());
	private TRPar _rPar_;
	private PUnaryExpNotPlusMinus _unaryExpNotPlusMinus_;

	private class Dim_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PDim node = (PDim) o;

			if((node.parent() != null) &&
				(node.parent() != AOldNameCastExp.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AOldNameCastExp.this))
			{
				node.parent(AOldNameCastExp.this);
			}

			return node;
		}
	}
	public AOldNameCastExp()
	{
	}
	public AOldNameCastExp(
		TLPar _lPar_,
		PName _name_,
		List _dim_,
		TRPar _rPar_,
		PUnaryExpNotPlusMinus _unaryExpNotPlusMinus_)
	{
		setLPar(_lPar_);

		setName(_name_);

		{
			Object temp[] = _dim_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._dim_.add(temp[i]);
			}
		}

		setRPar(_rPar_);

		setUnaryExpNotPlusMinus(_unaryExpNotPlusMinus_);

	}
	public AOldNameCastExp(
		TLPar _lPar_,
		PName _name_,
		XPDim _dim_,
		TRPar _rPar_,
		PUnaryExpNotPlusMinus _unaryExpNotPlusMinus_)
	{
		setLPar(_lPar_);

		setName(_name_);

		if(_dim_ != null)
		{
			while(_dim_ instanceof X1PDim)
			{
				this._dim_.addFirst(((X1PDim) _dim_).getPDim());
				_dim_ = ((X1PDim) _dim_).getXPDim();
			}
			this._dim_.addFirst(((X2PDim) _dim_).getPDim());
		}

		setRPar(_rPar_);

		setUnaryExpNotPlusMinus(_unaryExpNotPlusMinus_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldNameCastExp(this);
	}
	public Object clone()
	{
		return new AOldNameCastExp(
			(TLPar) cloneNode(_lPar_),
			(PName) cloneNode(_name_),
			cloneList(_dim_),
			(TRPar) cloneNode(_rPar_),
			(PUnaryExpNotPlusMinus) cloneNode(_unaryExpNotPlusMinus_));
	}
	public LinkedList getDim()
	{
		return _dim_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public PName getName()
	{
		return _name_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	public PUnaryExpNotPlusMinus getUnaryExpNotPlusMinus()
	{
		return _unaryExpNotPlusMinus_;
	}
	void removeChild(Node child)
	{
		if(_lPar_ == child)
		{
			_lPar_ = null;
			return;
		}

		if(_name_ == child)
		{
			_name_ = null;
			return;
		}

		if(_dim_.remove(child))
		{
			return;
		}

		if(_rPar_ == child)
		{
			_rPar_ = null;
			return;
		}

		if(_unaryExpNotPlusMinus_ == child)
		{
			_unaryExpNotPlusMinus_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_lPar_ == oldChild)
		{
			setLPar((TLPar) newChild);
			return;
		}

		if(_name_ == oldChild)
		{
			setName((PName) newChild);
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

		if(_rPar_ == oldChild)
		{
			setRPar((TRPar) newChild);
			return;
		}

		if(_unaryExpNotPlusMinus_ == oldChild)
		{
			setUnaryExpNotPlusMinus((PUnaryExpNotPlusMinus) newChild);
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
	public void setLPar(TLPar node)
	{
		if(_lPar_ != null)
		{
			_lPar_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_lPar_ = node;
	}
	public void setName(PName node)
	{
		if(_name_ != null)
		{
			_name_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_name_ = node;
	}
	public void setRPar(TRPar node)
	{
		if(_rPar_ != null)
		{
			_rPar_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_rPar_ = node;
	}
	public void setUnaryExpNotPlusMinus(PUnaryExpNotPlusMinus node)
	{
		if(_unaryExpNotPlusMinus_ != null)
		{
			_unaryExpNotPlusMinus_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_unaryExpNotPlusMinus_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_lPar_)
			+ toString(_name_)
			+ toString(_dim_)
			+ toString(_rPar_)
			+ toString(_unaryExpNotPlusMinus_);
	}
}
