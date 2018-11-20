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

public final class AOldConstructorDeclarator extends PConstructorDeclarator
{
	private TId _id_;
	private TLPar _lPar_;
	private PFormalParameterList _formalParameterList_;
	private TRPar _rPar_;

	public AOldConstructorDeclarator()
	{
	}
	public AOldConstructorDeclarator(
		TId _id_,
		TLPar _lPar_,
		PFormalParameterList _formalParameterList_,
		TRPar _rPar_)
	{
		setId(_id_);

		setLPar(_lPar_);

		setFormalParameterList(_formalParameterList_);

		setRPar(_rPar_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldConstructorDeclarator(this);
	}
	public Object clone()
	{
		return new AOldConstructorDeclarator(
			(TId) cloneNode(_id_),
			(TLPar) cloneNode(_lPar_),
			(PFormalParameterList) cloneNode(_formalParameterList_),
			(TRPar) cloneNode(_rPar_));
	}
	public PFormalParameterList getFormalParameterList()
	{
		return _formalParameterList_;
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

		if(_formalParameterList_ == child)
		{
			_formalParameterList_ = null;
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

		if(_formalParameterList_ == oldChild)
		{
			setFormalParameterList((PFormalParameterList) newChild);
			return;
		}

		if(_rPar_ == oldChild)
		{
			setRPar((TRPar) newChild);
			return;
		}

	}
	public void setFormalParameterList(PFormalParameterList node)
	{
		if(_formalParameterList_ != null)
		{
			_formalParameterList_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_formalParameterList_ = node;
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
			+ toString(_id_)
			+ toString(_lPar_)
			+ toString(_formalParameterList_)
			+ toString(_rPar_);
	}
}
