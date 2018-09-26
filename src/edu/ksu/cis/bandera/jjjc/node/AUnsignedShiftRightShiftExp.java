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

public final class AUnsignedShiftRightShiftExp extends PShiftExp
{
	private PShiftExp _shiftExp_;
	private TUnsignedShiftRight _unsignedShiftRight_;
	private PAdditiveExp _additiveExp_;

	public AUnsignedShiftRightShiftExp()
	{
	}
	public AUnsignedShiftRightShiftExp(
		PShiftExp _shiftExp_,
		TUnsignedShiftRight _unsignedShiftRight_,
		PAdditiveExp _additiveExp_)
	{
		setShiftExp(_shiftExp_);

		setUnsignedShiftRight(_unsignedShiftRight_);

		setAdditiveExp(_additiveExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAUnsignedShiftRightShiftExp(this);
	}
	public Object clone()
	{
		return new AUnsignedShiftRightShiftExp(
			(PShiftExp) cloneNode(_shiftExp_),
			(TUnsignedShiftRight) cloneNode(_unsignedShiftRight_),
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
	public TUnsignedShiftRight getUnsignedShiftRight()
	{
		return _unsignedShiftRight_;
	}
	void removeChild(Node child)
	{
		if(_shiftExp_ == child)
		{
			_shiftExp_ = null;
			return;
		}

		if(_unsignedShiftRight_ == child)
		{
			_unsignedShiftRight_ = null;
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

		if(_unsignedShiftRight_ == oldChild)
		{
			setUnsignedShiftRight((TUnsignedShiftRight) newChild);
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
	public void setUnsignedShiftRight(TUnsignedShiftRight node)
	{
		if(_unsignedShiftRight_ != null)
		{
			_unsignedShiftRight_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_unsignedShiftRight_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_shiftExp_)
			+ toString(_unsignedShiftRight_)
			+ toString(_additiveExp_);
	}
}
