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

public final class AGtRelationalExp extends PRelationalExp
{
	private PRelationalExp _relationalExp_;
	private TGt _gt_;
	private PShiftExp _shiftExp_;

	public AGtRelationalExp()
	{
	}
	public AGtRelationalExp(
		PRelationalExp _relationalExp_,
		TGt _gt_,
		PShiftExp _shiftExp_)
	{
		setRelationalExp(_relationalExp_);

		setGt(_gt_);

		setShiftExp(_shiftExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAGtRelationalExp(this);
	}
	public Object clone()
	{
		return new AGtRelationalExp(
			(PRelationalExp) cloneNode(_relationalExp_),
			(TGt) cloneNode(_gt_),
			(PShiftExp) cloneNode(_shiftExp_));
	}
	public TGt getGt()
	{
		return _gt_;
	}
	public PRelationalExp getRelationalExp()
	{
		return _relationalExp_;
	}
	public PShiftExp getShiftExp()
	{
		return _shiftExp_;
	}
	void removeChild(Node child)
	{
		if(_relationalExp_ == child)
		{
			_relationalExp_ = null;
			return;
		}

		if(_gt_ == child)
		{
			_gt_ = null;
			return;
		}

		if(_shiftExp_ == child)
		{
			_shiftExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_relationalExp_ == oldChild)
		{
			setRelationalExp((PRelationalExp) newChild);
			return;
		}

		if(_gt_ == oldChild)
		{
			setGt((TGt) newChild);
			return;
		}

		if(_shiftExp_ == oldChild)
		{
			setShiftExp((PShiftExp) newChild);
			return;
		}

	}
	public void setGt(TGt node)
	{
		if(_gt_ != null)
		{
			_gt_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_gt_ = node;
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
	public void setShiftExp(PShiftExp node)
	{
		if(_shiftExp_ != null)
		{
			_shiftExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_shiftExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_relationalExp_)
			+ toString(_gt_)
			+ toString(_shiftExp_);
	}
}
