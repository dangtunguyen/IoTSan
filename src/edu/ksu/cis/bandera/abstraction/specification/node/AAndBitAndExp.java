package edu.ksu.cis.bandera.abstraction.specification.node;

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
/* This file was generated by SableCC (http://www.sable.mcgill.ca/sablecc/). */

import java.util.*;
import edu.ksu.cis.bandera.abstraction.specification.analysis.*;

public final class AAndBitAndExp extends PBitAndExp
{
	private PBitAndExp _bitAndExp_;
	private TBitAnd _bitAnd_;
	private PEqExp _eqExp_;

	public AAndBitAndExp()
	{
	}
	public AAndBitAndExp(
		PBitAndExp _bitAndExp_,
		TBitAnd _bitAnd_,
		PEqExp _eqExp_)
	{
		setBitAndExp(_bitAndExp_);

		setBitAnd(_bitAnd_);

		setEqExp(_eqExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAAndBitAndExp(this);
	}
	public Object clone()
	{
		return new AAndBitAndExp(
			(PBitAndExp) cloneNode(_bitAndExp_),
			(TBitAnd) cloneNode(_bitAnd_),
			(PEqExp) cloneNode(_eqExp_));
	}
	public TBitAnd getBitAnd()
	{
		return _bitAnd_;
	}
	public PBitAndExp getBitAndExp()
	{
		return _bitAndExp_;
	}
	public PEqExp getEqExp()
	{
		return _eqExp_;
	}
	void removeChild(Node child)
	{
		if(_bitAndExp_ == child)
		{
			_bitAndExp_ = null;
			return;
		}

		if(_bitAnd_ == child)
		{
			_bitAnd_ = null;
			return;
		}

		if(_eqExp_ == child)
		{
			_eqExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_bitAndExp_ == oldChild)
		{
			setBitAndExp((PBitAndExp) newChild);
			return;
		}

		if(_bitAnd_ == oldChild)
		{
			setBitAnd((TBitAnd) newChild);
			return;
		}

		if(_eqExp_ == oldChild)
		{
			setEqExp((PEqExp) newChild);
			return;
		}

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
	public void setBitAndExp(PBitAndExp node)
	{
		if(_bitAndExp_ != null)
		{
			_bitAndExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_bitAndExp_ = node;
	}
	public void setEqExp(PEqExp node)
	{
		if(_eqExp_ != null)
		{
			_eqExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_eqExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_bitAndExp_)
			+ toString(_bitAnd_)
			+ toString(_eqExp_);
	}
}