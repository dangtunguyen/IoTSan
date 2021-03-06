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

public final class AMinusAddExp extends PAddExp
{
	private PAddExp _addExp_;
	private TMinus _minus_;
	private PMultExp _multExp_;

	public AMinusAddExp()
	{
	}
	public AMinusAddExp(
		PAddExp _addExp_,
		TMinus _minus_,
		PMultExp _multExp_)
	{
		setAddExp(_addExp_);

		setMinus(_minus_);

		setMultExp(_multExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAMinusAddExp(this);
	}
	public Object clone()
	{
		return new AMinusAddExp(
			(PAddExp) cloneNode(_addExp_),
			(TMinus) cloneNode(_minus_),
			(PMultExp) cloneNode(_multExp_));
	}
	public PAddExp getAddExp()
	{
		return _addExp_;
	}
	public TMinus getMinus()
	{
		return _minus_;
	}
	public PMultExp getMultExp()
	{
		return _multExp_;
	}
	void removeChild(Node child)
	{
		if(_addExp_ == child)
		{
			_addExp_ = null;
			return;
		}

		if(_minus_ == child)
		{
			_minus_ = null;
			return;
		}

		if(_multExp_ == child)
		{
			_multExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_addExp_ == oldChild)
		{
			setAddExp((PAddExp) newChild);
			return;
		}

		if(_minus_ == oldChild)
		{
			setMinus((TMinus) newChild);
			return;
		}

		if(_multExp_ == oldChild)
		{
			setMultExp((PMultExp) newChild);
			return;
		}

	}
	public void setAddExp(PAddExp node)
	{
		if(_addExp_ != null)
		{
			_addExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_addExp_ = node;
	}
	public void setMinus(TMinus node)
	{
		if(_minus_ != null)
		{
			_minus_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_minus_ = node;
	}
	public void setMultExp(PMultExp node)
	{
		if(_multExp_ != null)
		{
			_multExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_multExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_addExp_)
			+ toString(_minus_)
			+ toString(_multExp_);
	}
}
