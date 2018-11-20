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

public final class AVoidPrimaryNoNewArray extends PPrimaryNoNewArray
{
	private TVoid _void_;
	private TDot _dot_;
	private TClass _tClass_;

	public AVoidPrimaryNoNewArray()
	{
	}
	public AVoidPrimaryNoNewArray(
		TVoid _void_,
		TDot _dot_,
		TClass _tClass_)
	{
		setVoid(_void_);

		setDot(_dot_);

		setTClass(_tClass_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAVoidPrimaryNoNewArray(this);
	}
	public Object clone()
	{
		return new AVoidPrimaryNoNewArray(
			(TVoid) cloneNode(_void_),
			(TDot) cloneNode(_dot_),
			(TClass) cloneNode(_tClass_));
	}
	public TDot getDot()
	{
		return _dot_;
	}
	public TClass getTClass()
	{
		return _tClass_;
	}
	public TVoid getVoid()
	{
		return _void_;
	}
	void removeChild(Node child)
	{
		if(_void_ == child)
		{
			_void_ = null;
			return;
		}

		if(_dot_ == child)
		{
			_dot_ = null;
			return;
		}

		if(_tClass_ == child)
		{
			_tClass_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_void_ == oldChild)
		{
			setVoid((TVoid) newChild);
			return;
		}

		if(_dot_ == oldChild)
		{
			setDot((TDot) newChild);
			return;
		}

		if(_tClass_ == oldChild)
		{
			setTClass((TClass) newChild);
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
	public void setTClass(TClass node)
	{
		if(_tClass_ != null)
		{
			_tClass_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_tClass_ = node;
	}
	public void setVoid(TVoid node)
	{
		if(_void_ != null)
		{
			_void_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_void_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_void_)
			+ toString(_dot_)
			+ toString(_tClass_);
	}
}
