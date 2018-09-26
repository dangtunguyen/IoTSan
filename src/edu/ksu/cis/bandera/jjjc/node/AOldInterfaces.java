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

public final class AOldInterfaces extends PInterfaces
{
	private TImplements _implements_;
	private PInterfaceTypeList _interfaceTypeList_;

	public AOldInterfaces()
	{
	}
	public AOldInterfaces(
		TImplements _implements_,
		PInterfaceTypeList _interfaceTypeList_)
	{
		setImplements(_implements_);

		setInterfaceTypeList(_interfaceTypeList_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldInterfaces(this);
	}
	public Object clone()
	{
		return new AOldInterfaces(
			(TImplements) cloneNode(_implements_),
			(PInterfaceTypeList) cloneNode(_interfaceTypeList_));
	}
	public TImplements getImplements()
	{
		return _implements_;
	}
	public PInterfaceTypeList getInterfaceTypeList()
	{
		return _interfaceTypeList_;
	}
	void removeChild(Node child)
	{
		if(_implements_ == child)
		{
			_implements_ = null;
			return;
		}

		if(_interfaceTypeList_ == child)
		{
			_interfaceTypeList_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_implements_ == oldChild)
		{
			setImplements((TImplements) newChild);
			return;
		}

		if(_interfaceTypeList_ == oldChild)
		{
			setInterfaceTypeList((PInterfaceTypeList) newChild);
			return;
		}

	}
	public void setImplements(TImplements node)
	{
		if(_implements_ != null)
		{
			_implements_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_implements_ = node;
	}
	public void setInterfaceTypeList(PInterfaceTypeList node)
	{
		if(_interfaceTypeList_ != null)
		{
			_interfaceTypeList_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_interfaceTypeList_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_implements_)
			+ toString(_interfaceTypeList_);
	}
}
