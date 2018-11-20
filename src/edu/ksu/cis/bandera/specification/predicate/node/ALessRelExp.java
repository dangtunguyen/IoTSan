package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class ALessRelExp extends PRelExp
{
	private PRelExp _relExp_;
	private TLess _less_;
	private PShiftExp _shiftExp_;

	public ALessRelExp()
	{
	}
	public ALessRelExp(
		PRelExp _relExp_,
		TLess _less_,
		PShiftExp _shiftExp_)
	{
		setRelExp(_relExp_);

		setLess(_less_);

		setShiftExp(_shiftExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseALessRelExp(this);
	}
	public Object clone()
	{
		return new ALessRelExp(
			(PRelExp) cloneNode(_relExp_),
			(TLess) cloneNode(_less_),
			(PShiftExp) cloneNode(_shiftExp_));
	}
	public TLess getLess()
	{
		return _less_;
	}
	public PRelExp getRelExp()
	{
		return _relExp_;
	}
	public PShiftExp getShiftExp()
	{
		return _shiftExp_;
	}
	void removeChild(Node child)
	{
		if(_relExp_ == child)
		{
			_relExp_ = null;
			return;
		}

		if(_less_ == child)
		{
			_less_ = null;
			return;
		}

		if(_shiftExp_ == child)
		{
			_shiftExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_relExp_ == oldChild)
		{
			setRelExp((PRelExp) newChild);
			return;
		}

		if(_less_ == oldChild)
		{
			setLess((TLess) newChild);
			return;
		}

		if(_shiftExp_ == oldChild)
		{
			setShiftExp((PShiftExp) newChild);
			return;
		}

	}
	public void setLess(TLess node)
	{
		if(_less_ != null)
		{
			_less_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_less_ = node;
	}
	public void setRelExp(PRelExp node)
	{
		if(_relExp_ != null)
		{
			_relExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_relExp_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_relExp_)
			+ toString(_less_)
			+ toString(_shiftExp_);
	}
}