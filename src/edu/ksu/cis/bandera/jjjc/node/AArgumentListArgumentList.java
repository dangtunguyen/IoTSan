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

public final class AArgumentListArgumentList extends PArgumentList
{
	private PArgumentList _argumentList_;
	private TComma _comma_;
	private PExp _exp_;

	public AArgumentListArgumentList()
	{
	}
	public AArgumentListArgumentList(
		PArgumentList _argumentList_,
		TComma _comma_,
		PExp _exp_)
	{
		setArgumentList(_argumentList_);

		setComma(_comma_);

		setExp(_exp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAArgumentListArgumentList(this);
	}
	public Object clone()
	{
		return new AArgumentListArgumentList(
			(PArgumentList) cloneNode(_argumentList_),
			(TComma) cloneNode(_comma_),
			(PExp) cloneNode(_exp_));
	}
	public PArgumentList getArgumentList()
	{
		return _argumentList_;
	}
	public TComma getComma()
	{
		return _comma_;
	}
	public PExp getExp()
	{
		return _exp_;
	}
	void removeChild(Node child)
	{
		if(_argumentList_ == child)
		{
			_argumentList_ = null;
			return;
		}

		if(_comma_ == child)
		{
			_comma_ = null;
			return;
		}

		if(_exp_ == child)
		{
			_exp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_argumentList_ == oldChild)
		{
			setArgumentList((PArgumentList) newChild);
			return;
		}

		if(_comma_ == oldChild)
		{
			setComma((TComma) newChild);
			return;
		}

		if(_exp_ == oldChild)
		{
			setExp((PExp) newChild);
			return;
		}

	}
	public void setArgumentList(PArgumentList node)
	{
		if(_argumentList_ != null)
		{
			_argumentList_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_argumentList_ = node;
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
	public void setExp(PExp node)
	{
		if(_exp_ != null)
		{
			_exp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_exp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_argumentList_)
			+ toString(_comma_)
			+ toString(_exp_);
	}
}
