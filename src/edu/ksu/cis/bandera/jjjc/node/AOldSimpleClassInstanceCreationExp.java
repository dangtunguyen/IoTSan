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

public final class AOldSimpleClassInstanceCreationExp extends PClassInstanceCreationExp
{
	private TNew _new_;
	private PName _name_;
	private TLPar _lPar_;
	private PArgumentList _argumentList_;
	private TRPar _rPar_;
	private PClassBody _classBody_;

	public AOldSimpleClassInstanceCreationExp()
	{
	}
	public AOldSimpleClassInstanceCreationExp(
		TNew _new_,
		PName _name_,
		TLPar _lPar_,
		PArgumentList _argumentList_,
		TRPar _rPar_,
		PClassBody _classBody_)
	{
		setNew(_new_);

		setName(_name_);

		setLPar(_lPar_);

		setArgumentList(_argumentList_);

		setRPar(_rPar_);

		setClassBody(_classBody_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldSimpleClassInstanceCreationExp(this);
	}
	public Object clone()
	{
		return new AOldSimpleClassInstanceCreationExp(
			(TNew) cloneNode(_new_),
			(PName) cloneNode(_name_),
			(TLPar) cloneNode(_lPar_),
			(PArgumentList) cloneNode(_argumentList_),
			(TRPar) cloneNode(_rPar_),
			(PClassBody) cloneNode(_classBody_));
	}
	public PArgumentList getArgumentList()
	{
		return _argumentList_;
	}
	public PClassBody getClassBody()
	{
		return _classBody_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public PName getName()
	{
		return _name_;
	}
	public TNew getNew()
	{
		return _new_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	void removeChild(Node child)
	{
		if(_new_ == child)
		{
			_new_ = null;
			return;
		}

		if(_name_ == child)
		{
			_name_ = null;
			return;
		}

		if(_lPar_ == child)
		{
			_lPar_ = null;
			return;
		}

		if(_argumentList_ == child)
		{
			_argumentList_ = null;
			return;
		}

		if(_rPar_ == child)
		{
			_rPar_ = null;
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
		if(_new_ == oldChild)
		{
			setNew((TNew) newChild);
			return;
		}

		if(_name_ == oldChild)
		{
			setName((PName) newChild);
			return;
		}

		if(_lPar_ == oldChild)
		{
			setLPar((TLPar) newChild);
			return;
		}

		if(_argumentList_ == oldChild)
		{
			setArgumentList((PArgumentList) newChild);
			return;
		}

		if(_rPar_ == oldChild)
		{
			setRPar((TRPar) newChild);
			return;
		}

		if(_classBody_ == oldChild)
		{
			setClassBody((PClassBody) newChild);
			return;
		}

	}
	public void setArgumentList(PArgumentList node)
	{
		if(_argumentList_ != null)
		{
			_argumentList_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_argumentList_ = node;
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
			+ toString(_new_)
			+ toString(_name_)
			+ toString(_lPar_)
			+ toString(_argumentList_)
			+ toString(_rPar_)
			+ toString(_classBody_);
	}
}
