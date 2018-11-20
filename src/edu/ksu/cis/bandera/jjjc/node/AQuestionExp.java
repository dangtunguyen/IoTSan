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

public final class AQuestionExp extends PExp
{
	private PExp _first_;
	private TQuestion _question_;
	private PExp _second_;
	private TColon _colon_;
	private PExp _third_;

	public AQuestionExp()
	{
	}
	public AQuestionExp(
		PExp _first_,
		TQuestion _question_,
		PExp _second_,
		TColon _colon_,
		PExp _third_)
	{
		setFirst(_first_);

		setQuestion(_question_);

		setSecond(_second_);

		setColon(_colon_);

		setThird(_third_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAQuestionExp(this);
	}
	public Object clone()
	{
		return new AQuestionExp(
			(PExp) cloneNode(_first_),
			(TQuestion) cloneNode(_question_),
			(PExp) cloneNode(_second_),
			(TColon) cloneNode(_colon_),
			(PExp) cloneNode(_third_));
	}
	public TColon getColon()
	{
		return _colon_;
	}
	public PExp getFirst()
	{
		return _first_;
	}
	public TQuestion getQuestion()
	{
		return _question_;
	}
	public PExp getSecond()
	{
		return _second_;
	}
	public PExp getThird()
	{
		return _third_;
	}
	void removeChild(Node child)
	{
		if(_first_ == child)
		{
			_first_ = null;
			return;
		}

		if(_question_ == child)
		{
			_question_ = null;
			return;
		}

		if(_second_ == child)
		{
			_second_ = null;
			return;
		}

		if(_colon_ == child)
		{
			_colon_ = null;
			return;
		}

		if(_third_ == child)
		{
			_third_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_first_ == oldChild)
		{
			setFirst((PExp) newChild);
			return;
		}

		if(_question_ == oldChild)
		{
			setQuestion((TQuestion) newChild);
			return;
		}

		if(_second_ == oldChild)
		{
			setSecond((PExp) newChild);
			return;
		}

		if(_colon_ == oldChild)
		{
			setColon((TColon) newChild);
			return;
		}

		if(_third_ == oldChild)
		{
			setThird((PExp) newChild);
			return;
		}

	}
	public void setColon(TColon node)
	{
		if(_colon_ != null)
		{
			_colon_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_colon_ = node;
	}
	public void setFirst(PExp node)
	{
		if(_first_ != null)
		{
			_first_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_first_ = node;
	}
	public void setQuestion(TQuestion node)
	{
		if(_question_ != null)
		{
			_question_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_question_ = node;
	}
	public void setSecond(PExp node)
	{
		if(_second_ != null)
		{
			_second_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_second_ = node;
	}
	public void setThird(PExp node)
	{
		if(_third_ != null)
		{
			_third_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_third_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_first_)
			+ toString(_question_)
			+ toString(_second_)
			+ toString(_colon_)
			+ toString(_third_);
	}
}
