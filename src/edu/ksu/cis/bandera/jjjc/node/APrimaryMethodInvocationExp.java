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

public final class APrimaryMethodInvocationExp extends PExp
{
	private PExp _exp_;
	private TDot _dot_;
	private TId _id_;
	private TLPar _lPar_;
	private final LinkedList _argumentList_ = new TypedLinkedList(new ArgumentList_Cast());
	private TRPar _rPar_;

	private class ArgumentList_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PExp node = (PExp) o;

			if((node.parent() != null) &&
				(node.parent() != APrimaryMethodInvocationExp.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != APrimaryMethodInvocationExp.this))
			{
				node.parent(APrimaryMethodInvocationExp.this);
			}

			return node;
		}
	}
	public APrimaryMethodInvocationExp()
	{
	}
	public APrimaryMethodInvocationExp(
		PExp _exp_,
		TDot _dot_,
		TId _id_,
		TLPar _lPar_,
		List _argumentList_,
		TRPar _rPar_)
	{
		setExp(_exp_);

		setDot(_dot_);

		setId(_id_);

		setLPar(_lPar_);

		{
			Object temp[] = _argumentList_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._argumentList_.add(temp[i]);
			}
		}

		setRPar(_rPar_);

	}
	public APrimaryMethodInvocationExp(
		PExp _exp_,
		TDot _dot_,
		TId _id_,
		TLPar _lPar_,
		XPExp _argumentList_,
		TRPar _rPar_)
	{
		setExp(_exp_);

		setDot(_dot_);

		setId(_id_);

		setLPar(_lPar_);

		if(_argumentList_ != null)
		{
			while(_argumentList_ instanceof X1PExp)
			{
				this._argumentList_.addFirst(((X1PExp) _argumentList_).getPExp());
				_argumentList_ = ((X1PExp) _argumentList_).getXPExp();
			}
			this._argumentList_.addFirst(((X2PExp) _argumentList_).getPExp());
		}

		setRPar(_rPar_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAPrimaryMethodInvocationExp(this);
	}
	public Object clone()
	{
		return new APrimaryMethodInvocationExp(
			(PExp) cloneNode(_exp_),
			(TDot) cloneNode(_dot_),
			(TId) cloneNode(_id_),
			(TLPar) cloneNode(_lPar_),
			cloneList(_argumentList_),
			(TRPar) cloneNode(_rPar_));
	}
	public LinkedList getArgumentList()
	{
		return _argumentList_;
	}
	public TDot getDot()
	{
		return _dot_;
	}
	public PExp getExp()
	{
		return _exp_;
	}
	public TId getId()
	{
		return _id_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	void removeChild(Node child)
	{
		if(_exp_ == child)
		{
			_exp_ = null;
			return;
		}

		if(_dot_ == child)
		{
			_dot_ = null;
			return;
		}

		if(_id_ == child)
		{
			_id_ = null;
			return;
		}

		if(_lPar_ == child)
		{
			_lPar_ = null;
			return;
		}

		if(_argumentList_.remove(child))
		{
			return;
		}

		if(_rPar_ == child)
		{
			_rPar_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_exp_ == oldChild)
		{
			setExp((PExp) newChild);
			return;
		}

		if(_dot_ == oldChild)
		{
			setDot((TDot) newChild);
			return;
		}

		if(_id_ == oldChild)
		{
			setId((TId) newChild);
			return;
		}

		if(_lPar_ == oldChild)
		{
			setLPar((TLPar) newChild);
			return;
		}

		for(ListIterator i = _argumentList_.listIterator(); i.hasNext();)
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

	}
	public void setArgumentList(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_argumentList_.add(temp[i]);
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
	public void setExp(PExp node)
	{
		if(_exp_ != null)
		{
			_exp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_exp_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_exp_)
			+ toString(_dot_)
			+ toString(_id_)
			+ toString(_lPar_)
			+ toString(_argumentList_)
			+ toString(_rPar_);
	}
}
