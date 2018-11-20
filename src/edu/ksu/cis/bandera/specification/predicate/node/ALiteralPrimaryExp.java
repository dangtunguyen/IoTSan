package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class ALiteralPrimaryExp extends PPrimaryExp
{
	private PLiteral _literal_;

	public ALiteralPrimaryExp()
	{
	}
	public ALiteralPrimaryExp(
		PLiteral _literal_)
	{
		setLiteral(_literal_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseALiteralPrimaryExp(this);
	}
	public Object clone()
	{
		return new ALiteralPrimaryExp(
			(PLiteral) cloneNode(_literal_));
	}
	public PLiteral getLiteral()
	{
		return _literal_;
	}
	void removeChild(Node child)
	{
		if(_literal_ == child)
		{
			_literal_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_literal_ == oldChild)
		{
			setLiteral((PLiteral) newChild);
			return;
		}

	}
	public void setLiteral(PLiteral node)
	{
		if(_literal_ != null)
		{
			_literal_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_literal_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_literal_);
	}
}