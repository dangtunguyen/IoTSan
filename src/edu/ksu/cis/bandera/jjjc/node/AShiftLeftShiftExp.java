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

public final class AShiftLeftShiftExp extends PShiftExp
{
	private PShiftExp _shiftExp_;
	private TShiftLeft _shiftLeft_;
	private PAdditiveExp _additiveExp_;

	public AShiftLeftShiftExp()
	{
	}
	public AShiftLeftShiftExp(
		PShiftExp _shiftExp_,
		TShiftLeft _shiftLeft_,
		PAdditiveExp _additiveExp_)
	{
		setShiftExp(_shiftExp_);

		setShiftLeft(_shiftLeft_);

		setAdditiveExp(_additiveExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAShiftLeftShiftExp(this);
	}
	public Object clone()
	{
		return new AShiftLeftShiftExp(
			(PShiftExp) cloneNode(_shiftExp_),
			(TShiftLeft) cloneNode(_shiftLeft_),
			(PAdditiveExp) cloneNode(_additiveExp_));
	}
	public PAdditiveExp getAdditiveExp()
	{
		return _additiveExp_;
	}
	public PShiftExp getShiftExp()
	{
		return _shiftExp_;
	}
	public TShiftLeft getShiftLeft()
	{
		return _shiftLeft_;
	}
	void removeChild(Node child)
	{
		if(_shiftExp_ == child)
		{
			_shiftExp_ = null;
			return;
		}

		if(_shiftLeft_ == child)
		{
			_shiftLeft_ = null;
			return;
		}

		if(_additiveExp_ == child)
		{
			_additiveExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_shiftExp_ == oldChild)
		{
			setShiftExp((PShiftExp) newChild);
			return;
		}

		if(_shiftLeft_ == oldChild)
		{
			setShiftLeft((TShiftLeft) newChild);
			return;
		}

		if(_additiveExp_ == oldChild)
		{
			setAdditiveExp((PAdditiveExp) newChild);
			return;
		}

	}
	public void setAdditiveExp(PAdditiveExp node)
	{
		if(_additiveExp_ != null)
		{
			_additiveExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_additiveExp_ = node;
	}
	public void setShiftExp(PShiftExp node)
	{
		if(_shiftExp_ != null)
		{
			_shiftExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_shiftExp_ = node;
	}
	public void setShiftLeft(TShiftLeft node)
	{
		if(_shiftLeft_ != null)
		{
			_shiftLeft_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_shiftLeft_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_shiftExp_)
			+ toString(_shiftLeft_)
			+ toString(_additiveExp_);
	}
}
