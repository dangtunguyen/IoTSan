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

public final class ATypeOnDemandImportDeclaration extends PImportDeclaration
{
	private TImport _import_;
	private PName _name_;
	private TDot _dot_;
	private TStar _star_;
	private TSemicolon _semicolon_;

	public ATypeOnDemandImportDeclaration()
	{
	}
	public ATypeOnDemandImportDeclaration(
		TImport _import_,
		PName _name_,
		TDot _dot_,
		TStar _star_,
		TSemicolon _semicolon_)
	{
		setImport(_import_);

		setName(_name_);

		setDot(_dot_);

		setStar(_star_);

		setSemicolon(_semicolon_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseATypeOnDemandImportDeclaration(this);
	}
	public Object clone()
	{
		return new ATypeOnDemandImportDeclaration(
			(TImport) cloneNode(_import_),
			(PName) cloneNode(_name_),
			(TDot) cloneNode(_dot_),
			(TStar) cloneNode(_star_),
			(TSemicolon) cloneNode(_semicolon_));
	}
	public TDot getDot()
	{
		return _dot_;
	}
	public TImport getImport()
	{
		return _import_;
	}
	public PName getName()
	{
		return _name_;
	}
	public TSemicolon getSemicolon()
	{
		return _semicolon_;
	}
	public TStar getStar()
	{
		return _star_;
	}
	void removeChild(Node child)
	{
		if(_import_ == child)
		{
			_import_ = null;
			return;
		}

		if(_name_ == child)
		{
			_name_ = null;
			return;
		}

		if(_dot_ == child)
		{
			_dot_ = null;
			return;
		}

		if(_star_ == child)
		{
			_star_ = null;
			return;
		}

		if(_semicolon_ == child)
		{
			_semicolon_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_import_ == oldChild)
		{
			setImport((TImport) newChild);
			return;
		}

		if(_name_ == oldChild)
		{
			setName((PName) newChild);
			return;
		}

		if(_dot_ == oldChild)
		{
			setDot((TDot) newChild);
			return;
		}

		if(_star_ == oldChild)
		{
			setStar((TStar) newChild);
			return;
		}

		if(_semicolon_ == oldChild)
		{
			setSemicolon((TSemicolon) newChild);
			return;
		}

	}
	public void setDot(TDot node)
	{
		if(_dot_ != null)
		{
			_dot_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_dot_ = node;
	}
	public void setImport(TImport node)
	{
		if(_import_ != null)
		{
			_import_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_import_ = node;
	}
	public void setName(PName node)
	{
		if(_name_ != null)
		{
			_name_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_name_ = node;
	}
	public void setSemicolon(TSemicolon node)
	{
		if(_semicolon_ != null)
		{
			_semicolon_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_semicolon_ = node;
	}
	public void setStar(TStar node)
	{
		if(_star_ != null)
		{
			_star_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_star_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_import_)
			+ toString(_name_)
			+ toString(_dot_)
			+ toString(_star_)
			+ toString(_semicolon_);
	}
}
