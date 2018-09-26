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

public final class APrimaryNoNewArrayArrayAccess extends PArrayAccess
{
	private PExp _first_;
	private TLBracket _lBracket_;
	private PExp _second_;
	private TRBracket _rBracket_;

	public APrimaryNoNewArrayArrayAccess()
	{
	}
	public APrimaryNoNewArrayArrayAccess(
		PExp _first_,
		TLBracket _lBracket_,
		PExp _second_,
		TRBracket _rBracket_)
	{
		setFirst(_first_);

		setLBracket(_lBracket_);

		setSecond(_second_);

		setRBracket(_rBracket_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAPrimaryNoNewArrayArrayAccess(this);
	}
	public Object clone()
	{
		return new APrimaryNoNewArrayArrayAccess(
			(PExp) cloneNode(_first_),
			(TLBracket) cloneNode(_lBracket_),
			(PExp) cloneNode(_second_),
			(TRBracket) cloneNode(_rBracket_));
	}
	public PExp getFirst()
	{
		return _first_;
	}
	public TLBracket getLBracket()
	{
		return _lBracket_;
	}
	public TRBracket getRBracket()
	{
		return _rBracket_;
	}
	public PExp getSecond()
	{
		return _second_;
	}
	void removeChild(Node child)
	{
		if(_first_ == child)
		{
			_first_ = null;
			return;
		}

		if(_lBracket_ == child)
		{
			_lBracket_ = null;
			return;
		}

		if(_second_ == child)
		{
			_second_ = null;
			return;
		}

		if(_rBracket_ == child)
		{
			_rBracket_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_first_ == oldChild)
		{
			setFirst((PExp) newChild);
			return;
		}

		if(_lBracket_ == oldChild)
		{
			setLBracket((TLBracket) newChild);
			return;
		}

		if(_second_ == oldChild)
		{
			setSecond((PExp) newChild);
			return;
		}

		if(_rBracket_ == oldChild)
		{
			setRBracket((TRBracket) newChild);
			return;
		}

	}
	public void setFirst(PExp node)
	{
		if(_first_ != null)
		{
			_first_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_first_ = node;
	}
	public void setLBracket(TLBracket node)
	{
		if(_lBracket_ != null)
		{
			_lBracket_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_lBracket_ = node;
	}
	public void setRBracket(TRBracket node)
	{
		if(_rBracket_ != null)
		{
			_rBracket_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_rBracket_ = node;
	}
	public void setSecond(PExp node)
	{
		if(_second_ != null)
		{
			_second_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_second_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_first_)
			+ toString(_lBracket_)
			+ toString(_second_)
			+ toString(_rBracket_);
	}
}
