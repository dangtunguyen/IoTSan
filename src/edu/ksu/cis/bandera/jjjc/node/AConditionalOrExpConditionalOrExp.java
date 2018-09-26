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

public final class AConditionalOrExpConditionalOrExp extends PConditionalOrExp
{
	private PConditionalOrExp _conditionalOrExp_;
	private TOr _or_;
	private PConditionalAndExp _conditionalAndExp_;

	public AConditionalOrExpConditionalOrExp()
	{
	}
	public AConditionalOrExpConditionalOrExp(
		PConditionalOrExp _conditionalOrExp_,
		TOr _or_,
		PConditionalAndExp _conditionalAndExp_)
	{
		setConditionalOrExp(_conditionalOrExp_);

		setOr(_or_);

		setConditionalAndExp(_conditionalAndExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAConditionalOrExpConditionalOrExp(this);
	}
	public Object clone()
	{
		return new AConditionalOrExpConditionalOrExp(
			(PConditionalOrExp) cloneNode(_conditionalOrExp_),
			(TOr) cloneNode(_or_),
			(PConditionalAndExp) cloneNode(_conditionalAndExp_));
	}
	public PConditionalAndExp getConditionalAndExp()
	{
		return _conditionalAndExp_;
	}
	public PConditionalOrExp getConditionalOrExp()
	{
		return _conditionalOrExp_;
	}
	public TOr getOr()
	{
		return _or_;
	}
	void removeChild(Node child)
	{
		if(_conditionalOrExp_ == child)
		{
			_conditionalOrExp_ = null;
			return;
		}

		if(_or_ == child)
		{
			_or_ = null;
			return;
		}

		if(_conditionalAndExp_ == child)
		{
			_conditionalAndExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_conditionalOrExp_ == oldChild)
		{
			setConditionalOrExp((PConditionalOrExp) newChild);
			return;
		}

		if(_or_ == oldChild)
		{
			setOr((TOr) newChild);
			return;
		}

		if(_conditionalAndExp_ == oldChild)
		{
			setConditionalAndExp((PConditionalAndExp) newChild);
			return;
		}

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
	public void setConditionalOrExp(PConditionalOrExp node)
	{
		if(_conditionalOrExp_ != null)
		{
			_conditionalOrExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_conditionalOrExp_ = node;
	}
	public void setOr(TOr node)
	{
		if(_or_ != null)
		{
			_or_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_or_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_conditionalOrExp_)
			+ toString(_or_)
			+ toString(_conditionalAndExp_);
	}
}
