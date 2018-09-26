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

public final class ASignedShiftRightAssignAssignmentOperator extends PAssignmentOperator
{
	private TSignedShiftRightAssign _signedShiftRightAssign_;

	public ASignedShiftRightAssignAssignmentOperator()
	{
	}
	public ASignedShiftRightAssignAssignmentOperator(
		TSignedShiftRightAssign _signedShiftRightAssign_)
	{
		setSignedShiftRightAssign(_signedShiftRightAssign_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseASignedShiftRightAssignAssignmentOperator(this);
	}
	public Object clone()
	{
		return new ASignedShiftRightAssignAssignmentOperator(
			(TSignedShiftRightAssign) cloneNode(_signedShiftRightAssign_));
	}
	public TSignedShiftRightAssign getSignedShiftRightAssign()
	{
		return _signedShiftRightAssign_;
	}
	void removeChild(Node child)
	{
		if(_signedShiftRightAssign_ == child)
		{
			_signedShiftRightAssign_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_signedShiftRightAssign_ == oldChild)
		{
			setSignedShiftRightAssign((TSignedShiftRightAssign) newChild);
			return;
		}

	}
	public void setSignedShiftRightAssign(TSignedShiftRightAssign node)
	{
		if(_signedShiftRightAssign_ != null)
		{
			_signedShiftRightAssign_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_signedShiftRightAssign_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_signedShiftRightAssign_);
	}
}
