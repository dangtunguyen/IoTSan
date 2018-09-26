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

public final class AOneSynchronizedStmt extends POneSynchronizedStmt
{
	private TSynchronized _synchronized_;
	private TLPar _lPar_;
	private PExp _exp_;
	private TRPar _rPar_;
	private PBlock _block_;

	public AOneSynchronizedStmt()
	{
	}
	public AOneSynchronizedStmt(
		TSynchronized _synchronized_,
		TLPar _lPar_,
		PExp _exp_,
		TRPar _rPar_,
		PBlock _block_)
	{
		setSynchronized(_synchronized_);

		setLPar(_lPar_);

		setExp(_exp_);

		setRPar(_rPar_);

		setBlock(_block_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOneSynchronizedStmt(this);
	}
	public Object clone()
	{
		return new AOneSynchronizedStmt(
			(TSynchronized) cloneNode(_synchronized_),
			(TLPar) cloneNode(_lPar_),
			(PExp) cloneNode(_exp_),
			(TRPar) cloneNode(_rPar_),
			(PBlock) cloneNode(_block_));
	}
	public PBlock getBlock()
	{
		return _block_;
	}
	public PExp getExp()
	{
		return _exp_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	public TSynchronized getSynchronized()
	{
		return _synchronized_;
	}
	void removeChild(Node child)
	{
		if(_synchronized_ == child)
		{
			_synchronized_ = null;
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

		if(_block_ == child)
		{
			_block_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_synchronized_ == oldChild)
		{
			setSynchronized((TSynchronized) newChild);
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

		if(_block_ == oldChild)
		{
			setBlock((PBlock) newChild);
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
	public void setSynchronized(TSynchronized node)
	{
		if(_synchronized_ != null)
		{
			_synchronized_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_synchronized_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_synchronized_)
			+ toString(_lPar_)
			+ toString(_exp_)
			+ toString(_rPar_)
			+ toString(_block_);
	}
}
