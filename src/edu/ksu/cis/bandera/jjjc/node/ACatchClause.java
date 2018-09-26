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

public final class ACatchClause extends PCatchClause
{
	private TCatch _catch_;
	private TLPar _lPar_;
	private PFormalParameter _formalParameter_;
	private TRPar _rPar_;
	private PBlock _block_;

	public ACatchClause()
	{
	}
	public ACatchClause(
		TCatch _catch_,
		TLPar _lPar_,
		PFormalParameter _formalParameter_,
		TRPar _rPar_,
		PBlock _block_)
	{
		setCatch(_catch_);

		setLPar(_lPar_);

		setFormalParameter(_formalParameter_);

		setRPar(_rPar_);

		setBlock(_block_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseACatchClause(this);
	}
	public Object clone()
	{
		return new ACatchClause(
			(TCatch) cloneNode(_catch_),
			(TLPar) cloneNode(_lPar_),
			(PFormalParameter) cloneNode(_formalParameter_),
			(TRPar) cloneNode(_rPar_),
			(PBlock) cloneNode(_block_));
	}
	public PBlock getBlock()
	{
		return _block_;
	}
	public TCatch getCatch()
	{
		return _catch_;
	}
	public PFormalParameter getFormalParameter()
	{
		return _formalParameter_;
	}
	public TLPar getLPar()
	{
		return _lPar_;
	}
	public TRPar getRPar()
	{
		return _rPar_;
	}
	void removeChild(Node child)
	{
		if(_catch_ == child)
		{
			_catch_ = null;
			return;
		}

		if(_lPar_ == child)
		{
			_lPar_ = null;
			return;
		}

		if(_formalParameter_ == child)
		{
			_formalParameter_ = null;
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
		if(_catch_ == oldChild)
		{
			setCatch((TCatch) newChild);
			return;
		}

		if(_lPar_ == oldChild)
		{
			setLPar((TLPar) newChild);
			return;
		}

		if(_formalParameter_ == oldChild)
		{
			setFormalParameter((PFormalParameter) newChild);
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
	public void setCatch(TCatch node)
	{
		if(_catch_ != null)
		{
			_catch_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_catch_ = node;
	}
	public void setFormalParameter(PFormalParameter node)
	{
		if(_formalParameter_ != null)
		{
			_formalParameter_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_formalParameter_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_catch_)
			+ toString(_lPar_)
			+ toString(_formalParameter_)
			+ toString(_rPar_)
			+ toString(_block_);
	}
}
