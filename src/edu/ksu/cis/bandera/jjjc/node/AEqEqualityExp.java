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

public final class AEqEqualityExp extends PEqualityExp
{
	private PEqualityExp _equalityExp_;
	private TEq _eq_;
	private PRelationalExp _relationalExp_;

	public AEqEqualityExp()
	{
	}
	public AEqEqualityExp(
		PEqualityExp _equalityExp_,
		TEq _eq_,
		PRelationalExp _relationalExp_)
	{
		setEqualityExp(_equalityExp_);

		setEq(_eq_);

		setRelationalExp(_relationalExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAEqEqualityExp(this);
	}
	public Object clone()
	{
		return new AEqEqualityExp(
			(PEqualityExp) cloneNode(_equalityExp_),
			(TEq) cloneNode(_eq_),
			(PRelationalExp) cloneNode(_relationalExp_));
	}
	public TEq getEq()
	{
		return _eq_;
	}
	public PEqualityExp getEqualityExp()
	{
		return _equalityExp_;
	}
	public PRelationalExp getRelationalExp()
	{
		return _relationalExp_;
	}
	void removeChild(Node child)
	{
		if(_equalityExp_ == child)
		{
			_equalityExp_ = null;
			return;
		}

		if(_eq_ == child)
		{
			_eq_ = null;
			return;
		}

		if(_relationalExp_ == child)
		{
			_relationalExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_equalityExp_ == oldChild)
		{
			setEqualityExp((PEqualityExp) newChild);
			return;
		}

		if(_eq_ == oldChild)
		{
			setEq((TEq) newChild);
			return;
		}

		if(_relationalExp_ == oldChild)
		{
			setRelationalExp((PRelationalExp) newChild);
			return;
		}

	}
	public void setEq(TEq node)
	{
		if(_eq_ != null)
		{
			_eq_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_eq_ = node;
	}
	public void setEqualityExp(PEqualityExp node)
	{
		if(_equalityExp_ != null)
		{
			_equalityExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_equalityExp_ = node;
	}
	public void setRelationalExp(PRelationalExp node)
	{
		if(_relationalExp_ != null)
		{
			_relationalExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_relationalExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_equalityExp_)
			+ toString(_eq_)
			+ toString(_relationalExp_);
	}
}
