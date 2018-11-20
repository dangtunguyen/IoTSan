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

public final class AMinusAdditiveExp extends PAdditiveExp
{
	private PAdditiveExp _additiveExp_;
	private TMinus _minus_;
	private PMultiplicativeExp _multiplicativeExp_;

	public AMinusAdditiveExp()
	{
	}
	public AMinusAdditiveExp(
		PAdditiveExp _additiveExp_,
		TMinus _minus_,
		PMultiplicativeExp _multiplicativeExp_)
	{
		setAdditiveExp(_additiveExp_);

		setMinus(_minus_);

		setMultiplicativeExp(_multiplicativeExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAMinusAdditiveExp(this);
	}
	public Object clone()
	{
		return new AMinusAdditiveExp(
			(PAdditiveExp) cloneNode(_additiveExp_),
			(TMinus) cloneNode(_minus_),
			(PMultiplicativeExp) cloneNode(_multiplicativeExp_));
	}
	public PAdditiveExp getAdditiveExp()
	{
		return _additiveExp_;
	}
	public TMinus getMinus()
	{
		return _minus_;
	}
	public PMultiplicativeExp getMultiplicativeExp()
	{
		return _multiplicativeExp_;
	}
	void removeChild(Node child)
	{
		if(_additiveExp_ == child)
		{
			_additiveExp_ = null;
			return;
		}

		if(_minus_ == child)
		{
			_minus_ = null;
			return;
		}

		if(_multiplicativeExp_ == child)
		{
			_multiplicativeExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_additiveExp_ == oldChild)
		{
			setAdditiveExp((PAdditiveExp) newChild);
			return;
		}

		if(_minus_ == oldChild)
		{
			setMinus((TMinus) newChild);
			return;
		}

		if(_multiplicativeExp_ == oldChild)
		{
			setMultiplicativeExp((PMultiplicativeExp) newChild);
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
	public void setMinus(TMinus node)
	{
		if(_minus_ != null)
		{
			_minus_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_minus_ = node;
	}
	public void setMultiplicativeExp(PMultiplicativeExp node)
	{
		if(_multiplicativeExp_ != null)
		{
			_multiplicativeExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_multiplicativeExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_additiveExp_)
			+ toString(_minus_)
			+ toString(_multiplicativeExp_);
	}
}
