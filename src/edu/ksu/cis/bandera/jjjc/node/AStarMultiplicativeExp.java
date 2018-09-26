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

public final class AStarMultiplicativeExp extends PMultiplicativeExp
{
	private PMultiplicativeExp _multiplicativeExp_;
	private TStar _star_;
	private PUnaryExp _unaryExp_;

	public AStarMultiplicativeExp()
	{
	}
	public AStarMultiplicativeExp(
		PMultiplicativeExp _multiplicativeExp_,
		TStar _star_,
		PUnaryExp _unaryExp_)
	{
		setMultiplicativeExp(_multiplicativeExp_);

		setStar(_star_);

		setUnaryExp(_unaryExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAStarMultiplicativeExp(this);
	}
	public Object clone()
	{
		return new AStarMultiplicativeExp(
			(PMultiplicativeExp) cloneNode(_multiplicativeExp_),
			(TStar) cloneNode(_star_),
			(PUnaryExp) cloneNode(_unaryExp_));
	}
	public PMultiplicativeExp getMultiplicativeExp()
	{
		return _multiplicativeExp_;
	}
	public TStar getStar()
	{
		return _star_;
	}
	public PUnaryExp getUnaryExp()
	{
		return _unaryExp_;
	}
	void removeChild(Node child)
	{
		if(_multiplicativeExp_ == child)
		{
			_multiplicativeExp_ = null;
			return;
		}

		if(_star_ == child)
		{
			_star_ = null;
			return;
		}

		if(_unaryExp_ == child)
		{
			_unaryExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_multiplicativeExp_ == oldChild)
		{
			setMultiplicativeExp((PMultiplicativeExp) newChild);
			return;
		}

		if(_star_ == oldChild)
		{
			setStar((TStar) newChild);
			return;
		}

		if(_unaryExp_ == oldChild)
		{
			setUnaryExp((PUnaryExp) newChild);
			return;
		}

	}
	public void setMultiplicativeExp(PMultiplicativeExp node)
	{
		if(_multiplicativeExp_ != null)
		{
			_multiplicativeExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_multiplicativeExp_ = node;
	}
	public void setStar(TStar node)
	{
		if(_star_ != null)
		{
			_star_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_star_ = node;
	}
	public void setUnaryExp(PUnaryExp node)
	{
		if(_unaryExp_ != null)
		{
			_unaryExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_unaryExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_multiplicativeExp_)
			+ toString(_star_)
			+ toString(_unaryExp_);
	}
}
