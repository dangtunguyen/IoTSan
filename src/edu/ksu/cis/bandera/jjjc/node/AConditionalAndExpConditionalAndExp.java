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

public final class AConditionalAndExpConditionalAndExp extends PConditionalAndExp
{
	private PConditionalAndExp _conditionalAndExp_;
	private TAnd _and_;
	private PInclusiveOrExp _inclusiveOrExp_;

	public AConditionalAndExpConditionalAndExp()
	{
	}
	public AConditionalAndExpConditionalAndExp(
		PConditionalAndExp _conditionalAndExp_,
		TAnd _and_,
		PInclusiveOrExp _inclusiveOrExp_)
	{
		setConditionalAndExp(_conditionalAndExp_);

		setAnd(_and_);

		setInclusiveOrExp(_inclusiveOrExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAConditionalAndExpConditionalAndExp(this);
	}
	public Object clone()
	{
		return new AConditionalAndExpConditionalAndExp(
			(PConditionalAndExp) cloneNode(_conditionalAndExp_),
			(TAnd) cloneNode(_and_),
			(PInclusiveOrExp) cloneNode(_inclusiveOrExp_));
	}
	public TAnd getAnd()
	{
		return _and_;
	}
	public PConditionalAndExp getConditionalAndExp()
	{
		return _conditionalAndExp_;
	}
	public PInclusiveOrExp getInclusiveOrExp()
	{
		return _inclusiveOrExp_;
	}
	void removeChild(Node child)
	{
		if(_conditionalAndExp_ == child)
		{
			_conditionalAndExp_ = null;
			return;
		}

		if(_and_ == child)
		{
			_and_ = null;
			return;
		}

		if(_inclusiveOrExp_ == child)
		{
			_inclusiveOrExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_conditionalAndExp_ == oldChild)
		{
			setConditionalAndExp((PConditionalAndExp) newChild);
			return;
		}

		if(_and_ == oldChild)
		{
			setAnd((TAnd) newChild);
			return;
		}

		if(_inclusiveOrExp_ == oldChild)
		{
			setInclusiveOrExp((PInclusiveOrExp) newChild);
			return;
		}

	}
	public void setAnd(TAnd node)
	{
		if(_and_ != null)
		{
			_and_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_and_ = node;
	}
	public void setConditionalAndExp(PConditionalAndExp node)
	{
		if(_conditionalAndExp_ != null)
		{
			_conditionalAndExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_conditionalAndExp_ = node;
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
			+ toString(_conditionalAndExp_)
			+ toString(_and_)
			+ toString(_inclusiveOrExp_);
	}
}
