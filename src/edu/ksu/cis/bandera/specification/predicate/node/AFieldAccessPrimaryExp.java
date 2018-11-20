package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class AFieldAccessPrimaryExp extends PPrimaryExp
{
	private PFieldAccess _fieldAccess_;

	public AFieldAccessPrimaryExp()
	{
	}
	public AFieldAccessPrimaryExp(
		PFieldAccess _fieldAccess_)
	{
		setFieldAccess(_fieldAccess_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAFieldAccessPrimaryExp(this);
	}
	public Object clone()
	{
		return new AFieldAccessPrimaryExp(
			(PFieldAccess) cloneNode(_fieldAccess_));
	}
	public PFieldAccess getFieldAccess()
	{
		return _fieldAccess_;
	}
	void removeChild(Node child)
	{
		if(_fieldAccess_ == child)
		{
			_fieldAccess_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_fieldAccess_ == oldChild)
		{
			setFieldAccess((PFieldAccess) newChild);
			return;
		}

	}
	public void setFieldAccess(PFieldAccess node)
	{
		if(_fieldAccess_ != null)
		{
			_fieldAccess_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_fieldAccess_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_fieldAccess_);
	}
}