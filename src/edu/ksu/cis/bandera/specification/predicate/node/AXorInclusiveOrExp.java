package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class AXorInclusiveOrExp extends PInclusiveOrExp
{
	private PExclusiveOrExp _exclusiveOrExp_;

	public AXorInclusiveOrExp()
	{
	}
	public AXorInclusiveOrExp(
		PExclusiveOrExp _exclusiveOrExp_)
	{
		setExclusiveOrExp(_exclusiveOrExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAXorInclusiveOrExp(this);
	}
	public Object clone()
	{
		return new AXorInclusiveOrExp(
			(PExclusiveOrExp) cloneNode(_exclusiveOrExp_));
	}
	public PExclusiveOrExp getExclusiveOrExp()
	{
		return _exclusiveOrExp_;
	}
	void removeChild(Node child)
	{
		if(_exclusiveOrExp_ == child)
		{
			_exclusiveOrExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_exclusiveOrExp_ == oldChild)
		{
			setExclusiveOrExp((PExclusiveOrExp) newChild);
			return;
		}

	}
	public void setExclusiveOrExp(PExclusiveOrExp node)
	{
		if(_exclusiveOrExp_ != null)
		{
			_exclusiveOrExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_exclusiveOrExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_exclusiveOrExp_);
	}
}