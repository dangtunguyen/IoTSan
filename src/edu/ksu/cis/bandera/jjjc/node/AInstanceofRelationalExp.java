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

public final class AInstanceofRelationalExp extends PRelationalExp
{
	private PRelationalExp _relationalExp_;
	private TInstanceof _instanceof_;
	private PReferenceType _referenceType_;

	public AInstanceofRelationalExp()
	{
	}
	public AInstanceofRelationalExp(
		PRelationalExp _relationalExp_,
		TInstanceof _instanceof_,
		PReferenceType _referenceType_)
	{
		setRelationalExp(_relationalExp_);

		setInstanceof(_instanceof_);

		setReferenceType(_referenceType_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAInstanceofRelationalExp(this);
	}
	public Object clone()
	{
		return new AInstanceofRelationalExp(
			(PRelationalExp) cloneNode(_relationalExp_),
			(TInstanceof) cloneNode(_instanceof_),
			(PReferenceType) cloneNode(_referenceType_));
	}
	public TInstanceof getInstanceof()
	{
		return _instanceof_;
	}
	public PReferenceType getReferenceType()
	{
		return _referenceType_;
	}
	public PRelationalExp getRelationalExp()
	{
		return _relationalExp_;
	}
	void removeChild(Node child)
	{
		if(_relationalExp_ == child)
		{
			_relationalExp_ = null;
			return;
		}

		if(_instanceof_ == child)
		{
			_instanceof_ = null;
			return;
		}

		if(_referenceType_ == child)
		{
			_referenceType_ = null;
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

		if(_instanceof_ == oldChild)
		{
			setInstanceof((TInstanceof) newChild);
			return;
		}

		if(_referenceType_ == oldChild)
		{
			setReferenceType((PReferenceType) newChild);
			return;
		}

	}
	public void setInstanceof(TInstanceof node)
	{
		if(_instanceof_ != null)
		{
			_instanceof_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_instanceof_ = node;
	}
	public void setReferenceType(PReferenceType node)
	{
		if(_referenceType_ != null)
		{
			_referenceType_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_referenceType_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_relationalExp_)
			+ toString(_instanceof_)
			+ toString(_referenceType_);
	}
}
