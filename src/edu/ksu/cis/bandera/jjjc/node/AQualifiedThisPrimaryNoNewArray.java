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

public final class AQualifiedThisPrimaryNoNewArray extends PPrimaryNoNewArray
{
	private PName _name_;
	private TDot _dot_;
	private TThis _this_;

	public AQualifiedThisPrimaryNoNewArray()
	{
	}
	public AQualifiedThisPrimaryNoNewArray(
		PName _name_,
		TDot _dot_,
		TThis _this_)
	{
		setName(_name_);

		setDot(_dot_);

		setThis(_this_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAQualifiedThisPrimaryNoNewArray(this);
	}
	public Object clone()
	{
		return new AQualifiedThisPrimaryNoNewArray(
			(PName) cloneNode(_name_),
			(TDot) cloneNode(_dot_),
			(TThis) cloneNode(_this_));
	}
	public TDot getDot()
	{
		return _dot_;
	}
	public PName getName()
	{
		return _name_;
	}
	public TThis getThis()
	{
		return _this_;
	}
	void removeChild(Node child)
	{
		if(_name_ == child)
		{
			_name_ = null;
			return;
		}

		if(_dot_ == child)
		{
			_dot_ = null;
			return;
		}

		if(_this_ == child)
		{
			_this_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_name_ == oldChild)
		{
			setName((PName) newChild);
			return;
		}

		if(_dot_ == oldChild)
		{
			setDot((TDot) newChild);
			return;
		}

		if(_this_ == oldChild)
		{
			setThis((TThis) newChild);
			return;
		}

	}
	public void setDot(TDot node)
	{
		if(_dot_ != null)
		{
			_dot_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_dot_ = node;
	}
	public void setName(PName node)
	{
		if(_name_ != null)
		{
			_name_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_name_ = node;
	}
	public void setThis(TThis node)
	{
		if(_this_ != null)
		{
			_this_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_this_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_name_)
			+ toString(_dot_)
			+ toString(_this_);
	}
}
