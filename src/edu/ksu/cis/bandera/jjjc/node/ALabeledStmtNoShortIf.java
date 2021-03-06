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

public final class ALabeledStmtNoShortIf extends PLabeledStmtNoShortIf
{
	private TId _id_;
	private TColon _colon_;
	private PStmtNoShortIf _stmtNoShortIf_;

	public ALabeledStmtNoShortIf()
	{
	}
	public ALabeledStmtNoShortIf(
		TId _id_,
		TColon _colon_,
		PStmtNoShortIf _stmtNoShortIf_)
	{
		setId(_id_);

		setColon(_colon_);

		setStmtNoShortIf(_stmtNoShortIf_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseALabeledStmtNoShortIf(this);
	}
	public Object clone()
	{
		return new ALabeledStmtNoShortIf(
			(TId) cloneNode(_id_),
			(TColon) cloneNode(_colon_),
			(PStmtNoShortIf) cloneNode(_stmtNoShortIf_));
	}
	public TColon getColon()
	{
		return _colon_;
	}
	public TId getId()
	{
		return _id_;
	}
	public PStmtNoShortIf getStmtNoShortIf()
	{
		return _stmtNoShortIf_;
	}
	void removeChild(Node child)
	{
		if(_id_ == child)
		{
			_id_ = null;
			return;
		}

		if(_colon_ == child)
		{
			_colon_ = null;
			return;
		}

		if(_stmtNoShortIf_ == child)
		{
			_stmtNoShortIf_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_id_ == oldChild)
		{
			setId((TId) newChild);
			return;
		}

		if(_colon_ == oldChild)
		{
			setColon((TColon) newChild);
			return;
		}

		if(_stmtNoShortIf_ == oldChild)
		{
			setStmtNoShortIf((PStmtNoShortIf) newChild);
			return;
		}

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
	public void setId(TId node)
	{
		if(_id_ != null)
		{
			_id_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_id_ = node;
	}
	public void setStmtNoShortIf(PStmtNoShortIf node)
	{
		if(_stmtNoShortIf_ != null)
		{
			_stmtNoShortIf_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_stmtNoShortIf_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_id_)
			+ toString(_colon_)
			+ toString(_stmtNoShortIf_);
	}
}
