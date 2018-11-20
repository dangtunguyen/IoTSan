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

public final class AVariableInitializersVariableInitializers extends PVariableInitializers
{
	private PVariableInitializers _variableInitializers_;
	private TComma _comma_;
	private PVariableInitializer _variableInitializer_;

	public AVariableInitializersVariableInitializers()
	{
	}
	public AVariableInitializersVariableInitializers(
		PVariableInitializers _variableInitializers_,
		TComma _comma_,
		PVariableInitializer _variableInitializer_)
	{
		setVariableInitializers(_variableInitializers_);

		setComma(_comma_);

		setVariableInitializer(_variableInitializer_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAVariableInitializersVariableInitializers(this);
	}
	public Object clone()
	{
		return new AVariableInitializersVariableInitializers(
			(PVariableInitializers) cloneNode(_variableInitializers_),
			(TComma) cloneNode(_comma_),
			(PVariableInitializer) cloneNode(_variableInitializer_));
	}
	public TComma getComma()
	{
		return _comma_;
	}
	public PVariableInitializer getVariableInitializer()
	{
		return _variableInitializer_;
	}
	public PVariableInitializers getVariableInitializers()
	{
		return _variableInitializers_;
	}
	void removeChild(Node child)
	{
		if(_variableInitializers_ == child)
		{
			_variableInitializers_ = null;
			return;
		}

		if(_comma_ == child)
		{
			_comma_ = null;
			return;
		}

		if(_variableInitializer_ == child)
		{
			_variableInitializer_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_variableInitializers_ == oldChild)
		{
			setVariableInitializers((PVariableInitializers) newChild);
			return;
		}

		if(_comma_ == oldChild)
		{
			setComma((TComma) newChild);
			return;
		}

		if(_variableInitializer_ == oldChild)
		{
			setVariableInitializer((PVariableInitializer) newChild);
			return;
		}

	}
	public void setComma(TComma node)
	{
		if(_comma_ != null)
		{
			_comma_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_comma_ = node;
	}
	public void setVariableInitializer(PVariableInitializer node)
	{
		if(_variableInitializer_ != null)
		{
			_variableInitializer_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_variableInitializer_ = node;
	}
	public void setVariableInitializers(PVariableInitializers node)
	{
		if(_variableInitializers_ != null)
		{
			_variableInitializers_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_variableInitializers_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_variableInitializers_)
			+ toString(_comma_)
			+ toString(_variableInitializer_);
	}
}
