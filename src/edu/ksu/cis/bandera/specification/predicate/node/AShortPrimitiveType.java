package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class AShortPrimitiveType extends PPrimitiveType
{
	private TShort _short_;

	public AShortPrimitiveType()
	{
	}
	public AShortPrimitiveType(
		TShort _short_)
	{
		setShort(_short_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAShortPrimitiveType(this);
	}
	public Object clone()
	{
		return new AShortPrimitiveType(
			(TShort) cloneNode(_short_));
	}
	public TShort getShort()
	{
		return _short_;
	}
	void removeChild(Node child)
	{
		if(_short_ == child)
		{
			_short_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_short_ == oldChild)
		{
			setShort((TShort) newChild);
			return;
		}

	}
	public void setShort(TShort node)
	{
		if(_short_ != null)
		{
			_short_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_short_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_short_);
	}
}