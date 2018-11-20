package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class AAndExclusiveOrExp extends PExclusiveOrExp
{
	private PBitAndExp _bitAndExp_;

	public AAndExclusiveOrExp()
	{
	}
	public AAndExclusiveOrExp(
		PBitAndExp _bitAndExp_)
	{
		setBitAndExp(_bitAndExp_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAAndExclusiveOrExp(this);
	}
	public Object clone()
	{
		return new AAndExclusiveOrExp(
			(PBitAndExp) cloneNode(_bitAndExp_));
	}
	public PBitAndExp getBitAndExp()
	{
		return _bitAndExp_;
	}
	void removeChild(Node child)
	{
		if(_bitAndExp_ == child)
		{
			_bitAndExp_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_bitAndExp_ == oldChild)
		{
			setBitAndExp((PBitAndExp) newChild);
			return;
		}

	}
	public void setBitAndExp(PBitAndExp node)
	{
		if(_bitAndExp_ != null)
		{
			_bitAndExp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_bitAndExp_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_bitAndExp_);
	}
}