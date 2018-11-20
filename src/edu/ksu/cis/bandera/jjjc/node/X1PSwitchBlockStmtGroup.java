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

public final class X1PSwitchBlockStmtGroup extends XPSwitchBlockStmtGroup
{
	private XPSwitchBlockStmtGroup _xPSwitchBlockStmtGroup_;
	private PSwitchBlockStmtGroup _pSwitchBlockStmtGroup_;

	public X1PSwitchBlockStmtGroup()
	{
	}
	public X1PSwitchBlockStmtGroup(
		XPSwitchBlockStmtGroup _xPSwitchBlockStmtGroup_,
		PSwitchBlockStmtGroup _pSwitchBlockStmtGroup_)
	{
		setXPSwitchBlockStmtGroup(_xPSwitchBlockStmtGroup_);
		setPSwitchBlockStmtGroup(_pSwitchBlockStmtGroup_);
	}
	public void apply(Switch sw)
	{
		throw new RuntimeException("Switch not supported.");
	}
	public Object clone()
	{
		throw new RuntimeException("Unsupported Operation");
	}
	public PSwitchBlockStmtGroup getPSwitchBlockStmtGroup()
	{
		return _pSwitchBlockStmtGroup_;
	}
	public XPSwitchBlockStmtGroup getXPSwitchBlockStmtGroup()
	{
		return _xPSwitchBlockStmtGroup_;
	}
	void removeChild(Node child)
	{
		if(_xPSwitchBlockStmtGroup_ == child)
		{
			_xPSwitchBlockStmtGroup_ = null;
		}

		if(_pSwitchBlockStmtGroup_ == child)
		{
			_pSwitchBlockStmtGroup_ = null;
		}
	}
	void replaceChild(Node oldChild, Node newChild)
	{
	}
	public void setPSwitchBlockStmtGroup(PSwitchBlockStmtGroup node)
	{
		if(_pSwitchBlockStmtGroup_ != null)
		{
			_pSwitchBlockStmtGroup_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_pSwitchBlockStmtGroup_ = node;
	}
	public void setXPSwitchBlockStmtGroup(XPSwitchBlockStmtGroup node)
	{
		if(_xPSwitchBlockStmtGroup_ != null)
		{
			_xPSwitchBlockStmtGroup_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_xPSwitchBlockStmtGroup_ = node;
	}
	public String toString()
	{
		return "" +
			toString(_xPSwitchBlockStmtGroup_) +
			toString(_pSwitchBlockStmtGroup_);
	}
}
