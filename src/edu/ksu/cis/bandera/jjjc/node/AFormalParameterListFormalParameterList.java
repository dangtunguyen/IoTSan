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

public final class AFormalParameterListFormalParameterList extends PFormalParameterList
{
	private PFormalParameterList _formalParameterList_;
	private TComma _comma_;
	private PFormalParameter _formalParameter_;

	public AFormalParameterListFormalParameterList()
	{
	}
	public AFormalParameterListFormalParameterList(
		PFormalParameterList _formalParameterList_,
		TComma _comma_,
		PFormalParameter _formalParameter_)
	{
		setFormalParameterList(_formalParameterList_);

		setComma(_comma_);

		setFormalParameter(_formalParameter_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAFormalParameterListFormalParameterList(this);
	}
	public Object clone()
	{
		return new AFormalParameterListFormalParameterList(
			(PFormalParameterList) cloneNode(_formalParameterList_),
			(TComma) cloneNode(_comma_),
			(PFormalParameter) cloneNode(_formalParameter_));
	}
	public TComma getComma()
	{
		return _comma_;
	}
	public PFormalParameter getFormalParameter()
	{
		return _formalParameter_;
	}
	public PFormalParameterList getFormalParameterList()
	{
		return _formalParameterList_;
	}
	void removeChild(Node child)
	{
		if(_formalParameterList_ == child)
		{
			_formalParameterList_ = null;
			return;
		}

		if(_comma_ == child)
		{
			_comma_ = null;
			return;
		}

		if(_formalParameter_ == child)
		{
			_formalParameter_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_formalParameterList_ == oldChild)
		{
			setFormalParameterList((PFormalParameterList) newChild);
			return;
		}

		if(_comma_ == oldChild)
		{
			setComma((TComma) newChild);
			return;
		}

		if(_formalParameter_ == oldChild)
		{
			setFormalParameter((PFormalParameter) newChild);
			return;
		}

	}
	public void setComma(TComma node)
	{
		if(_comma_ != null)
		{
			_comma_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_comma_ = node;
	}
	public void setFormalParameter(PFormalParameter node)
	{
		if(_formalParameter_ != null)
		{
			_formalParameter_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_formalParameter_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_formalParameterList_)
			+ toString(_comma_)
			+ toString(_formalParameter_);
	}
}
