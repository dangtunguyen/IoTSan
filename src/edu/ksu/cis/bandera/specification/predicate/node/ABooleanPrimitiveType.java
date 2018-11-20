package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class ABooleanPrimitiveType extends PPrimitiveType
{
	private TBoolean _boolean_;

	public ABooleanPrimitiveType()
	{
	}
	public ABooleanPrimitiveType(
		TBoolean _boolean_)
	{
		setBoolean(_boolean_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseABooleanPrimitiveType(this);
	}
	public Object clone()
	{
		return new ABooleanPrimitiveType(
			(TBoolean) cloneNode(_boolean_));
	}
	public TBoolean getBoolean()
	{
		return _boolean_;
	}
	void removeChild(Node child)
	{
		if(_boolean_ == child)
		{
			_boolean_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_boolean_ == oldChild)
		{
			setBoolean((TBoolean) newChild);
			return;
		}

	}
	public void setBoolean(TBoolean node)
	{
		if(_boolean_ != null)
		{
			_boolean_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_boolean_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_boolean_);
	}
}