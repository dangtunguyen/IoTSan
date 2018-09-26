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

public final class AIfThenElseStmtNoShortIf extends PIfThenElseStmtNoShortIf
{
	private TIf _if_;
	private TLPar _lPar_;
	private PExp _exp_;
	private TRPar _rPar_;
	private PStmtNoShortIf _stmtNoShortIf1_;
	private TElse _else_;
	private PStmtNoShortIf _stmtNoShortIf2_;

	public AIfThenElseStmtNoShortIf()
	{
	}
	public AIfThenElseStmtNoShortIf(
		TIf _if_,
		TLPar _lPar_,
		PExp _exp_,
		TRPar _rPar_,
		PStmtNoShortIf _stmtNoShortIf1_,
		TElse _else_,
		PStmtNoShortIf _stmtNoShortIf2_)
	{
		setIf(_if_);

		setLPar(_lPar_);

		setExp(_exp_);

		setRPar(_rPar_);

		setStmtNoShortIf1(_stmtNoShortIf1_);

		setElse(_else_);

		setStmtNoShortIf2(_stmtNoShortIf2_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAIfThenElseStmtNoShortIf(this);
	}
	public Object clone()
	{
		return new AIfThenElseStmtNoShortIf(
			(TIf) cloneNode(_if_),
			(TLPar) cloneNode(_lPar_),
			(PExp) cloneNode(_exp_),
			(TRPar) cloneNode(_rPar_),
			(PStmtNoShortIf) cloneNode(_stmtNoShortIf1_),
			(TElse) cloneNode(_else_),
			(PStmtNoShortIf) cloneNode(_stmtNoShortIf2_));
	}
	public TElse getElse()
	{
		return _else_;
	}
	public PExp getExp()
	{
		return _exp_;
	}
	public TIf getIf()
	{
		return _if_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	public PStmtNoShortIf getStmtNoShortIf1()
	{
		return _stmtNoShortIf1_;
	}
	public PStmtNoShortIf getStmtNoShortIf2()
	{
		return _stmtNoShortIf2_;
	}
	void removeChild(Node child)
	{
		if(_if_ == child)
		{
			_if_ = null;
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

		if(_stmtNoShortIf1_ == child)
		{
			_stmtNoShortIf1_ = null;
			return;
		}

		if(_else_ == child)
		{
			_else_ = null;
			return;
		}

		if(_stmtNoShortIf2_ == child)
		{
			_stmtNoShortIf2_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_if_ == oldChild)
		{
			setIf((TIf) newChild);
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

		if(_stmtNoShortIf1_ == oldChild)
		{
			setStmtNoShortIf1((PStmtNoShortIf) newChild);
			return;
		}

		if(_else_ == oldChild)
		{
			setElse((TElse) newChild);
			return;
		}

		if(_stmtNoShortIf2_ == oldChild)
		{
			setStmtNoShortIf2((PStmtNoShortIf) newChild);
			return;
		}

	}
	public void setElse(TElse node)
	{
		if(_else_ != null)
		{
			_else_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_else_ = node;
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
	public void setIf(TIf node)
	{
		if(_if_ != null)
		{
			_if_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_if_ = node;
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
	public void setStmtNoShortIf1(PStmtNoShortIf node)
	{
		if(_stmtNoShortIf1_ != null)
		{
			_stmtNoShortIf1_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_stmtNoShortIf1_ = node;
	}
	public void setStmtNoShortIf2(PStmtNoShortIf node)
	{
		if(_stmtNoShortIf2_ != null)
		{
			_stmtNoShortIf2_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_stmtNoShortIf2_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_if_)
			+ toString(_lPar_)
			+ toString(_exp_)
			+ toString(_rPar_)
			+ toString(_stmtNoShortIf1_)
			+ toString(_else_)
			+ toString(_stmtNoShortIf2_);
	}
}
