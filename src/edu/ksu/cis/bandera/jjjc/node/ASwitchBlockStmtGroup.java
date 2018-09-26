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

public final class ASwitchBlockStmtGroup extends PSwitchBlockStmtGroup
{
	private final LinkedList _switchLabel_ = new TypedLinkedList(new SwitchLabel_Cast());
	private final LinkedList _blockedStmt_ = new TypedLinkedList(new BlockedStmt_Cast());

	private class SwitchLabel_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PSwitchLabel node = (PSwitchLabel) o;

			if((node.parent() != null) &&
				(node.parent() != ASwitchBlockStmtGroup.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != ASwitchBlockStmtGroup.this))
			{
				node.parent(ASwitchBlockStmtGroup.this);
			}

			return node;
		}
	}

	private class BlockedStmt_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PBlockedStmt node = (PBlockedStmt) o;

			if((node.parent() != null) &&
				(node.parent() != ASwitchBlockStmtGroup.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != ASwitchBlockStmtGroup.this))
			{
				node.parent(ASwitchBlockStmtGroup.this);
			}

			return node;
		}
	}
	public ASwitchBlockStmtGroup()
	{
	}
	public ASwitchBlockStmtGroup(
		List _switchLabel_,
		List _blockedStmt_)
	{
		{
			Object temp[] = _switchLabel_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._switchLabel_.add(temp[i]);
			}
		}

		{
			Object temp[] = _blockedStmt_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._blockedStmt_.add(temp[i]);
			}
		}

	}
	public ASwitchBlockStmtGroup(
		XPSwitchLabel _switchLabel_,
		XPBlockedStmt _blockedStmt_)
	{
		if(_switchLabel_ != null)
		{
			while(_switchLabel_ instanceof X1PSwitchLabel)
			{
				this._switchLabel_.addFirst(((X1PSwitchLabel) _switchLabel_).getPSwitchLabel());
				_switchLabel_ = ((X1PSwitchLabel) _switchLabel_).getXPSwitchLabel();
			}
			this._switchLabel_.addFirst(((X2PSwitchLabel) _switchLabel_).getPSwitchLabel());
		}

		if(_blockedStmt_ != null)
		{
			while(_blockedStmt_ instanceof X1PBlockedStmt)
			{
				this._blockedStmt_.addFirst(((X1PBlockedStmt) _blockedStmt_).getPBlockedStmt());
				_blockedStmt_ = ((X1PBlockedStmt) _blockedStmt_).getXPBlockedStmt();
			}
			this._blockedStmt_.addFirst(((X2PBlockedStmt) _blockedStmt_).getPBlockedStmt());
		}

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseASwitchBlockStmtGroup(this);
	}
	public Object clone()
	{
		return new ASwitchBlockStmtGroup(
			cloneList(_switchLabel_),
			cloneList(_blockedStmt_));
	}
	public LinkedList getBlockedStmt()
	{
		return _blockedStmt_;
	}
	public LinkedList getSwitchLabel()
	{
		return _switchLabel_;
	}
	void removeChild(Node child)
	{
		if(_switchLabel_.remove(child))
		{
			return;
		}

		if(_blockedStmt_.remove(child))
		{
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		for(ListIterator i = _switchLabel_.listIterator(); i.hasNext();)
		{
			if(i.next() == oldChild)
			{
				if(newChild != null)
				{
					i.set(newChild);
					oldChild.parent(null);
					return;
				}

				i.remove();
				oldChild.parent(null);
				return;
			}
		}

		for(ListIterator i = _blockedStmt_.listIterator(); i.hasNext();)
		{
			if(i.next() == oldChild)
			{
				if(newChild != null)
				{
					i.set(newChild);
					oldChild.parent(null);
					return;
				}

				i.remove();
				oldChild.parent(null);
				return;
			}
		}

	}
	public void setBlockedStmt(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_blockedStmt_.add(temp[i]);
		}
	}
	public void setSwitchLabel(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_switchLabel_.add(temp[i]);
		}
	}
	public String toString()
	{
		return ""
			+ toString(_switchLabel_)
			+ toString(_blockedStmt_);
	}
}
