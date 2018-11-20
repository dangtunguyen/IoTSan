package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class AAddExpShiftExp extends PShiftExp
{
	private PAddExp _addExp_;

	public AAddExpShiftExp()
	{
	}
	public AAddExpShiftExp(
		PAddExp _addExp_)
	{
		setAddExp(_addExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAAddExpShiftExp(this);
	}
	public Object clone()
	{
		return new AAddExpShiftExp(
			(PAddExp) cloneNode(_addExp_));
	}
	public PAddExp getAddExp()
	{
		return _addExp_;
	}
	void removeChild(Node child)
	{
		if(_addExp_ == child)
		{
			_addExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_addExp_ == oldChild)
		{
			setAddExp((PAddExp) newChild);
			return;
		}

	}
	public void setAddExp(PAddExp node)
	{
		if(_addExp_ != null)
		{
			_addExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_addExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_addExp_);
	}
}