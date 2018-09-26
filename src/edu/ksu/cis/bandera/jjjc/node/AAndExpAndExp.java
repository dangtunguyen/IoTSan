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

public final class AAndExpAndExp extends PAndExp
{
	private PAndExp _andExp_;
	private TBitAnd _bitAnd_;
	private PEqualityExp _equalityExp_;

	public AAndExpAndExp()
	{
	}
	public AAndExpAndExp(
		PAndExp _andExp_,
		TBitAnd _bitAnd_,
		PEqualityExp _equalityExp_)
	{
		setAndExp(_andExp_);

		setBitAnd(_bitAnd_);

		setEqualityExp(_equalityExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAAndExpAndExp(this);
	}
	public Object clone()
	{
		return new AAndExpAndExp(
			(PAndExp) cloneNode(_andExp_),
			(TBitAnd) cloneNode(_bitAnd_),
			(PEqualityExp) cloneNode(_equalityExp_));
	}
	public PAndExp getAndExp()
	{
		return _andExp_;
	}
	public TBitAnd getBitAnd()
	{
		return _bitAnd_;
	}
	public PEqualityExp getEqualityExp()
	{
		return _equalityExp_;
	}
	void removeChild(Node child)
	{
		if(_andExp_ == child)
		{
			_andExp_ = null;
			return;
		}

		if(_bitAnd_ == child)
		{
			_bitAnd_ = null;
			return;
		}

		if(_equalityExp_ == child)
		{
			_equalityExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_andExp_ == oldChild)
		{
			setAndExp((PAndExp) newChild);
			return;
		}

		if(_bitAnd_ == oldChild)
		{
			setBitAnd((TBitAnd) newChild);
			return;
		}

		if(_equalityExp_ == oldChild)
		{
			setEqualityExp((PEqualityExp) newChild);
			return;
		}

	}
	public void setAndExp(PAndExp node)
	{
		if(_andExp_ != null)
		{
			_andExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_andExp_ = node;
	}
	public void setBitAnd(TBitAnd node)
	{
		if(_bitAnd_ != null)
		{
			_bitAnd_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_bitAnd_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_andExp_)
			+ toString(_bitAnd_)
			+ toString(_equalityExp_);
	}
}
