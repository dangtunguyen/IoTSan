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

public final class AOldCaseSwitchLabel extends PSwitchLabel
{
	private TCase _case_;
	private PConstantExp _constantExp_;
	private TColon _colon_;

	public AOldCaseSwitchLabel()
	{
	}
	public AOldCaseSwitchLabel(
		TCase _case_,
		PConstantExp _constantExp_,
		TColon _colon_)
	{
		setCase(_case_);

		setConstantExp(_constantExp_);

		setColon(_colon_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldCaseSwitchLabel(this);
	}
	public Object clone()
	{
		return new AOldCaseSwitchLabel(
			(TCase) cloneNode(_case_),
			(PConstantExp) cloneNode(_constantExp_),
			(TColon) cloneNode(_colon_));
	}
	public TCase getCase()
	{
		return _case_;
	}
	public TColon getColon()
	{
		return _colon_;
	}
	public PConstantExp getConstantExp()
	{
		return _constantExp_;
	}
	void removeChild(Node child)
	{
		if(_case_ == child)
		{
			_case_ = null;
			return;
		}

		if(_constantExp_ == child)
		{
			_constantExp_ = null;
			return;
		}

		if(_colon_ == child)
		{
			_colon_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_case_ == oldChild)
		{
			setCase((TCase) newChild);
			return;
		}

		if(_constantExp_ == oldChild)
		{
			setConstantExp((PConstantExp) newChild);
			return;
		}

		if(_colon_ == oldChild)
		{
			setColon((TColon) newChild);
			return;
		}

	}
	public void setCase(TCase node)
	{
		if(_case_ != null)
		{
			_case_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_case_ = node;
	}
	public void setColon(TColon node)
	{
		if(_colon_ != null)
		{
			_colon_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_colon_ = node;
	}
	public void setConstantExp(PConstantExp node)
	{
		if(_constantExp_ != null)
		{
			_constantExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_constantExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_case_)
			+ toString(_constantExp_)
			+ toString(_colon_);
	}
}
