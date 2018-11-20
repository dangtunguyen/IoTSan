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

public final class X1PModifier extends XPModifier
{
	private XPModifier _xPModifier_;
	private PModifier _pModifier_;

	public X1PModifier()
	{
	}
	public X1PModifier(
		XPModifier _xPModifier_,
		PModifier _pModifier_)
	{
		setXPModifier(_xPModifier_);
		setPModifier(_pModifier_);
	}
	public void apply(Switch sw)
	{
		throw new RuntimeException("Switch not supported.");
	}
	public Object clone()
	{
		throw new RuntimeException("Unsupported Operation");
	}
	public PModifier getPModifier()
	{
		return _pModifier_;
	}
	public XPModifier getXPModifier()
	{
		return _xPModifier_;
	}
	void removeChild(Node child)
	{
		if(_xPModifier_ == child)
		{
			_xPModifier_ = null;
		}

		if(_pModifier_ == child)
		{
			_pModifier_ = null;
		}
	}
	void replaceChild(Node oldChild, Node newChild)
	{
	}
	public void setPModifier(PModifier node)
	{
		if(_pModifier_ != null)
		{
			_pModifier_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_pModifier_ = node;
	}
	public void setXPModifier(XPModifier node)
	{
		if(_xPModifier_ != null)
		{
			_xPModifier_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_xPModifier_ = node;
	}
	public String toString()
	{
		return "" +
			toString(_xPModifier_) +
			toString(_pModifier_);
	}
}
