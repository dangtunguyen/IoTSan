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

public final class AOneSwitchStmt extends POneSwitchStmt
{
	private TSwitch _switch_;
	private TLPar _lPar_;
	private PExp _exp_;
	private TRPar _rPar_;
	private TLBrace _lBrace_;
	private final LinkedList _switchBlockStmtGroup_ = new TypedLinkedList(new SwitchBlockStmtGroup_Cast());
	private final LinkedList _switchLabel_ = new TypedLinkedList(new SwitchLabel_Cast());
	private TRBrace _rBrace_;

	private class SwitchBlockStmtGroup_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PSwitchBlockStmtGroup node = (PSwitchBlockStmtGroup) o;

			if((node.parent() != null) &&
				(node.parent() != AOneSwitchStmt.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AOneSwitchStmt.this))
			{
				node.parent(AOneSwitchStmt.this);
			}

			return node;
		}
	}

	private class SwitchLabel_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PSwitchLabel node = (PSwitchLabel) o;

			if((node.parent() != null) &&
				(node.parent() != AOneSwitchStmt.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AOneSwitchStmt.this))
			{
				node.parent(AOneSwitchStmt.this);
			}

			return node;
		}
	}
	public AOneSwitchStmt()
	{
	}
	public AOneSwitchStmt(
		TSwitch _switch_,
		TLPar _lPar_,
		PExp _exp_,
		TRPar _rPar_,
		TLBrace _lBrace_,
		List _switchBlockStmtGroup_,
		List _switchLabel_,
		TRBrace _rBrace_)
	{
		setSwitch(_switch_);

		setLPar(_lPar_);

		setExp(_exp_);

		setRPar(_rPar_);

		setLBrace(_lBrace_);

		{
			Object temp[] = _switchBlockStmtGroup_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._switchBlockStmtGroup_.add(temp[i]);
			}
		}

		{
			Object temp[] = _switchLabel_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._switchLabel_.add(temp[i]);
			}
		}

		setRBrace(_rBrace_);

	}
	public AOneSwitchStmt(
		TSwitch _switch_,
		TLPar _lPar_,
		PExp _exp_,
		TRPar _rPar_,
		TLBrace _lBrace_,
		XPSwitchBlockStmtGroup _switchBlockStmtGroup_,
		XPSwitchLabel _switchLabel_,
		TRBrace _rBrace_)
	{
		setSwitch(_switch_);

		setLPar(_lPar_);

		setExp(_exp_);

		setRPar(_rPar_);

		setLBrace(_lBrace_);

		if(_switchBlockStmtGroup_ != null)
		{
			while(_switchBlockStmtGroup_ instanceof X1PSwitchBlockStmtGroup)
			{
				this._switchBlockStmtGroup_.addFirst(((X1PSwitchBlockStmtGroup) _switchBlockStmtGroup_).getPSwitchBlockStmtGroup());
				_switchBlockStmtGroup_ = ((X1PSwitchBlockStmtGroup) _switchBlockStmtGroup_).getXPSwitchBlockStmtGroup();
			}
			this._switchBlockStmtGroup_.addFirst(((X2PSwitchBlockStmtGroup) _switchBlockStmtGroup_).getPSwitchBlockStmtGroup());
		}

		if(_switchLabel_ != null)
		{
			while(_switchLabel_ instanceof X1PSwitchLabel)
			{
				this._switchLabel_.addFirst(((X1PSwitchLabel) _switchLabel_).getPSwitchLabel());
				_switchLabel_ = ((X1PSwitchLabel) _switchLabel_).getXPSwitchLabel();
			}
			this._switchLabel_.addFirst(((X2PSwitchLabel) _switchLabel_).getPSwitchLabel());
		}

		setRBrace(_rBrace_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOneSwitchStmt(this);
	}
	public Object clone()
	{
		return new AOneSwitchStmt(
			(TSwitch) cloneNode(_switch_),
			(TLPar) cloneNode(_lPar_),
			(PExp) cloneNode(_exp_),
			(TRPar) cloneNode(_rPar_),
			(TLBrace) cloneNode(_lBrace_),
			cloneList(_switchBlockStmtGroup_),
			cloneList(_switchLabel_),
			(TRBrace) cloneNode(_rBrace_));
	}
	public PExp getExp()
	{
		return _exp_;
	}
	public TLBrace getLBrace()
	{
		return _lBrace_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public TRBrace getRBrace()
	{
		return _rBrace_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	public TSwitch getSwitch()
	{
		return _switch_;
	}
	public LinkedList getSwitchBlockStmtGroup()
	{
		return _switchBlockStmtGroup_;
	}
	public LinkedList getSwitchLabel()
	{
		return _switchLabel_;
	}
	void removeChild(Node child)
	{
		if(_switch_ == child)
		{
			_switch_ = null;
			return;
		}

		if(_lPar_ == child)
		{
			_lPar_ = null;
			return;
		}

		if(_exp_ == child)
		{
			_exp_ = null;
			return;
		}

		if(_rPar_ == child)
		{
			_rPar_ = null;
			return;
		}

		if(_lBrace_ == child)
		{
			_lBrace_ = null;
			return;
		}

		if(_switchBlockStmtGroup_.remove(child))
		{
			return;
		}

		if(_switchLabel_.remove(child))
		{
			return;
		}

		if(_rBrace_ == child)
		{
			_rBrace_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_switch_ == oldChild)
		{
			setSwitch((TSwitch) newChild);
			return;
		}

		if(_lPar_ == oldChild)
		{
			setLPar((TLPar) newChild);
			return;
		}

		if(_exp_ == oldChild)
		{
			setExp((PExp) newChild);
			return;
		}

		if(_rPar_ == oldChild)
		{
			setRPar((TRPar) newChild);
			return;
		}

		if(_lBrace_ == oldChild)
		{
			setLBrace((TLBrace) newChild);
			return;
		}

		for(ListIterator i = _switchBlockStmtGroup_.listIterator(); i.hasNext();)
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

		if(_rBrace_ == oldChild)
		{
			setRBrace((TRBrace) newChild);
			return;
		}

	}
	public void setExp(PExp node)
	{
		if(_exp_ != null)
		{
			_exp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_exp_ = node;
	}
	public void setLBrace(TLBrace node)
	{
		if(_lBrace_ != null)
		{
			_lBrace_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_lBrace_ = node;
	}
	public void setLPar(TLPar node)
	{
		if(_lPar_ != null)
		{
			_lPar_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_lPar_ = node;
	}
	public void setRBrace(TRBrace node)
	{
		if(_rBrace_ != null)
		{
			_rBrace_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_rBrace_ = node;
	}
	public void setRPar(TRPar node)
	{
		if(_rPar_ != null)
		{
			_rPar_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_rPar_ = node;
	}
	public void setSwitch(TSwitch node)
	{
		if(_switch_ != null)
		{
			_switch_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_switch_ = node;
	}
	public void setSwitchBlockStmtGroup(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_switchBlockStmtGroup_.add(temp[i]);
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
			+ toString(_switch_)
			+ toString(_lPar_)
			+ toString(_exp_)
			+ toString(_rPar_)
			+ toString(_lBrace_)
			+ toString(_switchBlockStmtGroup_)
			+ toString(_switchLabel_)
			+ toString(_rBrace_);
	}
}
