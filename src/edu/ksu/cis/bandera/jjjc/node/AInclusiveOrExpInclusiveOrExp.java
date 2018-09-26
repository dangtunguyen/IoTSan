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

public final class AInclusiveOrExpInclusiveOrExp extends PInclusiveOrExp
{
	private PInclusiveOrExp _inclusiveOrExp_;
	private TBitOr _bitOr_;
	private PExclusiveOrExp _exclusiveOrExp_;

	public AInclusiveOrExpInclusiveOrExp()
	{
	}
	public AInclusiveOrExpInclusiveOrExp(
		PInclusiveOrExp _inclusiveOrExp_,
		TBitOr _bitOr_,
		PExclusiveOrExp _exclusiveOrExp_)
	{
		setInclusiveOrExp(_inclusiveOrExp_);

		setBitOr(_bitOr_);

		setExclusiveOrExp(_exclusiveOrExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAInclusiveOrExpInclusiveOrExp(this);
	}
	public Object clone()
	{
		return new AInclusiveOrExpInclusiveOrExp(
			(PInclusiveOrExp) cloneNode(_inclusiveOrExp_),
			(TBitOr) cloneNode(_bitOr_),
			(PExclusiveOrExp) cloneNode(_exclusiveOrExp_));
	}
	public TBitOr getBitOr()
	{
		return _bitOr_;
	}
	public PExclusiveOrExp getExclusiveOrExp()
	{
		return _exclusiveOrExp_;
	}
	public PInclusiveOrExp getInclusiveOrExp()
	{
		return _inclusiveOrExp_;
	}
	void removeChild(Node child)
	{
		if(_inclusiveOrExp_ == child)
		{
			_inclusiveOrExp_ = null;
			return;
		}

		if(_bitOr_ == child)
		{
			_bitOr_ = null;
			return;
		}

		if(_exclusiveOrExp_ == child)
		{
			_exclusiveOrExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_inclusiveOrExp_ == oldChild)
		{
			setInclusiveOrExp((PInclusiveOrExp) newChild);
			return;
		}

		if(_bitOr_ == oldChild)
		{
			setBitOr((TBitOr) newChild);
			return;
		}

		if(_exclusiveOrExp_ == oldChild)
		{
			setExclusiveOrExp((PExclusiveOrExp) newChild);
			return;
		}

	}
	public void setBitOr(TBitOr node)
	{
		if(_bitOr_ != null)
		{
			_bitOr_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_bitOr_ = node;
	}
	public void setExclusiveOrExp(PExclusiveOrExp node)
	{
		if(_exclusiveOrExp_ != null)
		{
			_exclusiveOrExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_exclusiveOrExp_ = node;
	}
	public void setInclusiveOrExp(PInclusiveOrExp node)
	{
		if(_inclusiveOrExp_ != null)
		{
			_inclusiveOrExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_inclusiveOrExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_inclusiveOrExp_)
			+ toString(_bitOr_)
			+ toString(_exclusiveOrExp_);
	}
}
