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

public final class AExpCastExp extends PExp
{
	private TLPar _lPar_;
	private PExp _first_;
	private TRPar _rPar_;
	private PExp _second_;

	public AExpCastExp()
	{
	}
	public AExpCastExp(
		TLPar _lPar_,
		PExp _first_,
		TRPar _rPar_,
		PExp _second_)
	{
		setLPar(_lPar_);

		setFirst(_first_);

		setRPar(_rPar_);

		setSecond(_second_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAExpCastExp(this);
	}
	public Object clone()
	{
		return new AExpCastExp(
			(TLPar) cloneNode(_lPar_),
			(PExp) cloneNode(_first_),
			(TRPar) cloneNode(_rPar_),
			(PExp) cloneNode(_second_));
	}
	public PExp getFirst()
	{
		return _first_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	public PExp getSecond()
	{
		return _second_;
	}
	void removeChild(Node child)
	{
		if(_lPar_ == child)
		{
			_lPar_ = null;
			return;
		}

		if(_first_ == child)
		{
			_first_ = null;
			return;
		}

		if(_rPar_ == child)
		{
			_rPar_ = null;
			return;
		}

		if(_second_ == child)
		{
			_second_ = null;
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

		if(_first_ == oldChild)
		{
			setFirst((PExp) newChild);
			return;
		}

		if(_rPar_ == oldChild)
		{
			setRPar((TRPar) newChild);
			return;
		}

		if(_second_ == oldChild)
		{
			setSecond((PExp) newChild);
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
			+ toString(_lPar_)
			+ toString(_first_)
			+ toString(_rPar_)
			+ toString(_second_);
	}
}
