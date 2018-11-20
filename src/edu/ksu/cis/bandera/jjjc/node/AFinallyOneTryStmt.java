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

public final class AFinallyOneTryStmt extends POneTryStmt
{
	private TTry _try_;
	private PBlock _block_;
	private final LinkedList _catchClause_ = new TypedLinkedList(new CatchClause_Cast());
	private PFinally _finally_;

	private class CatchClause_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PCatchClause node = (PCatchClause) o;

			if((node.parent() != null) &&
				(node.parent() != AFinallyOneTryStmt.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AFinallyOneTryStmt.this))
			{
				node.parent(AFinallyOneTryStmt.this);
			}

			return node;
		}
	}
	public AFinallyOneTryStmt()
	{
	}
	public AFinallyOneTryStmt(
		TTry _try_,
		PBlock _block_,
		List _catchClause_,
		PFinally _finally_)
	{
		setTry(_try_);

		setBlock(_block_);

		{
			Object temp[] = _catchClause_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._catchClause_.add(temp[i]);
			}
		}

		setFinally(_finally_);

	}
	public AFinallyOneTryStmt(
		TTry _try_,
		PBlock _block_,
		XPCatchClause _catchClause_,
		PFinally _finally_)
	{
		setTry(_try_);

		setBlock(_block_);

		if(_catchClause_ != null)
		{
			while(_catchClause_ instanceof X1PCatchClause)
			{
				this._catchClause_.addFirst(((X1PCatchClause) _catchClause_).getPCatchClause());
				_catchClause_ = ((X1PCatchClause) _catchClause_).getXPCatchClause();
			}
			this._catchClause_.addFirst(((X2PCatchClause) _catchClause_).getPCatchClause());
		}

		setFinally(_finally_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAFinallyOneTryStmt(this);
	}
	public Object clone()
	{
		return new AFinallyOneTryStmt(
			(TTry) cloneNode(_try_),
			(PBlock) cloneNode(_block_),
			cloneList(_catchClause_),
			(PFinally) cloneNode(_finally_));
	}
	public PBlock getBlock()
	{
		return _block_;
	}
	public LinkedList getCatchClause()
	{
		return _catchClause_;
	}
	public PFinally getFinally()
	{
		return _finally_;
	}
	public TTry getTry()
	{
		return _try_;
	}
	void removeChild(Node child)
	{
		if(_try_ == child)
		{
			_try_ = null;
			return;
		}

		if(_block_ == child)
		{
			_block_ = null;
			return;
		}

		if(_catchClause_.remove(child))
		{
			return;
		}

		if(_finally_ == child)
		{
			_finally_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_try_ == oldChild)
		{
			setTry((TTry) newChild);
			return;
		}

		if(_block_ == oldChild)
		{
			setBlock((PBlock) newChild);
			return;
		}

		for(ListIterator i = _catchClause_.listIterator(); i.hasNext();)
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

		if(_finally_ == oldChild)
		{
			setFinally((PFinally) newChild);
			return;
		}

	}
	public void setBlock(PBlock node)
	{
		if(_block_ != null)
		{
			_block_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_block_ = node;
	}
	public void setCatchClause(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_catchClause_.add(temp[i]);
		}
	}
	public void setFinally(PFinally node)
	{
		if(_finally_ != null)
		{
			_finally_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_finally_ = node;
	}
	public void setTry(TTry node)
	{
		if(_try_ != null)
		{
			_try_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_try_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_try_)
			+ toString(_block_)
			+ toString(_catchClause_)
			+ toString(_finally_);
	}
}
