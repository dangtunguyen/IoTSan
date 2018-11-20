package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class AGreaterBinOp extends PBinOp
{
	private TGreater _greater_;

	public AGreaterBinOp()
	{
	}
	public AGreaterBinOp(
		TGreater _greater_)
	{
		setGreater(_greater_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAGreaterBinOp(this);
	}
	public Object clone()
	{
		return new AGreaterBinOp(
			(TGreater) cloneNode(_greater_));
	}
	public TGreater getGreater()
	{
		return _greater_;
	}
	void removeChild(Node child)
	{
		if(_greater_ == child)
		{
			_greater_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_greater_ == oldChild)
		{
			setGreater((TGreater) newChild);
			return;
		}

	}
	public void setGreater(TGreater node)
	{
		if(_greater_ != null)
		{
			_greater_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_greater_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_greater_);
	}
}