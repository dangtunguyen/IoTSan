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

public final class AConstructorDeclarator extends PConstructorDeclarator
{
	private TId _simpleName_;
	private TLPar _lPar_;
	private final LinkedList _formalParameter_ = new TypedLinkedList(new FormalParameter_Cast());
	private TRPar _rPar_;

	private class FormalParameter_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PFormalParameter node = (PFormalParameter) o;

			if((node.parent() != null) &&
				(node.parent() != AConstructorDeclarator.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AConstructorDeclarator.this))
			{
				node.parent(AConstructorDeclarator.this);
			}

			return node;
		}
	}
	public AConstructorDeclarator()
	{
	}
	public AConstructorDeclarator(
		TId _simpleName_,
		TLPar _lPar_,
		List _formalParameter_,
		TRPar _rPar_)
	{
		setSimpleName(_simpleName_);

		setLPar(_lPar_);

		{
			Object temp[] = _formalParameter_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._formalParameter_.add(temp[i]);
			}
		}

		setRPar(_rPar_);

	}
	public AConstructorDeclarator(
		TId _simpleName_,
		TLPar _lPar_,
		XPFormalParameter _formalParameter_,
		TRPar _rPar_)
	{
		setSimpleName(_simpleName_);

		setLPar(_lPar_);

		if(_formalParameter_ != null)
		{
			while(_formalParameter_ instanceof X1PFormalParameter)
			{
				this._formalParameter_.addFirst(((X1PFormalParameter) _formalParameter_).getPFormalParameter());
				_formalParameter_ = ((X1PFormalParameter) _formalParameter_).getXPFormalParameter();
			}
			this._formalParameter_.addFirst(((X2PFormalParameter) _formalParameter_).getPFormalParameter());
		}

		setRPar(_rPar_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAConstructorDeclarator(this);
	}
	public Object clone()
	{
		return new AConstructorDeclarator(
			(TId) cloneNode(_simpleName_),
			(TLPar) cloneNode(_lPar_),
			cloneList(_formalParameter_),
			(TRPar) cloneNode(_rPar_));
	}
	public LinkedList getFormalParameter()
	{
		return _formalParameter_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	public TId getSimpleName()
	{
		return _simpleName_;
	}
	void removeChild(Node child)
	{
		if(_simpleName_ == child)
		{
			_simpleName_ = null;
			return;
		}

		if(_lPar_ == child)
		{
			_lPar_ = null;
			return;
		}

		if(_formalParameter_.remove(child))
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
		if(_simpleName_ == oldChild)
		{
			setSimpleName((TId) newChild);
			return;
		}

		if(_lPar_ == oldChild)
		{
			setLPar((TLPar) newChild);
			return;
		}

		for(ListIterator i = _formalParameter_.listIterator(); i.hasNext();)
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
	public void setFormalParameter(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_formalParameter_.add(temp[i]);
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
	public void setSimpleName(TId node)
	{
		if(_simpleName_ != null)
		{
			_simpleName_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_simpleName_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_simpleName_)
			+ toString(_lPar_)
			+ toString(_formalParameter_)
			+ toString(_rPar_);
	}
}
