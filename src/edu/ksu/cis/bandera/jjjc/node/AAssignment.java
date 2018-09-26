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

public final class AAssignment extends PAssignment
{
	private PLeftHandSide _leftHandSide_;
	private PAssignmentOperator _assignmentOperator_;
	private PAssignmentExp _assignmentExp_;

	public AAssignment()
	{
	}
	public AAssignment(
		PLeftHandSide _leftHandSide_,
		PAssignmentOperator _assignmentOperator_,
		PAssignmentExp _assignmentExp_)
	{
		setLeftHandSide(_leftHandSide_);

		setAssignmentOperator(_assignmentOperator_);

		setAssignmentExp(_assignmentExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAAssignment(this);
	}
	public Object clone()
	{
		return new AAssignment(
			(PLeftHandSide) cloneNode(_leftHandSide_),
			(PAssignmentOperator) cloneNode(_assignmentOperator_),
			(PAssignmentExp) cloneNode(_assignmentExp_));
	}
	public PAssignmentExp getAssignmentExp()
	{
		return _assignmentExp_;
	}
	public PAssignmentOperator getAssignmentOperator()
	{
		return _assignmentOperator_;
	}
	public PLeftHandSide getLeftHandSide()
	{
		return _leftHandSide_;
	}
	void removeChild(Node child)
	{
		if(_leftHandSide_ == child)
		{
			_leftHandSide_ = null;
			return;
		}

		if(_assignmentOperator_ == child)
		{
			_assignmentOperator_ = null;
			return;
		}

		if(_assignmentExp_ == child)
		{
			_assignmentExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_leftHandSide_ == oldChild)
		{
			setLeftHandSide((PLeftHandSide) newChild);
			return;
		}

		if(_assignmentOperator_ == oldChild)
		{
			setAssignmentOperator((PAssignmentOperator) newChild);
			return;
		}

		if(_assignmentExp_ == oldChild)
		{
			setAssignmentExp((PAssignmentExp) newChild);
			return;
		}

	}
	public void setAssignmentExp(PAssignmentExp node)
	{
		if(_assignmentExp_ != null)
		{
			_assignmentExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_assignmentExp_ = node;
	}
	public void setAssignmentOperator(PAssignmentOperator node)
	{
		if(_assignmentOperator_ != null)
		{
			_assignmentOperator_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_assignmentOperator_ = node;
	}
	public void setLeftHandSide(PLeftHandSide node)
	{
		if(_leftHandSide_ != null)
		{
			_leftHandSide_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_leftHandSide_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_leftHandSide_)
			+ toString(_assignmentOperator_)
			+ toString(_assignmentExp_);
	}
}
