package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class ANavigationExp extends PExp
{
	private PExp _exp_;
	private PNavigation _navigation_;

	public ANavigationExp()
	{
	}
	public ANavigationExp(
		PExp _exp_,
		PNavigation _navigation_)
	{
		setExp(_exp_);

		setNavigation(_navigation_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseANavigationExp(this);
	}
	public Object clone()
	{
		return new ANavigationExp(
			(PExp) cloneNode(_exp_),
			(PNavigation) cloneNode(_navigation_));
	}
	public PExp getExp()
	{
		return _exp_;
	}
	public PNavigation getNavigation()
	{
		return _navigation_;
	}
	void removeChild(Node child)
	{
		if(_exp_ == child)
		{
			_exp_ = null;
			return;
		}

		if(_navigation_ == child)
		{
			_navigation_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_exp_ == oldChild)
		{
			setExp((PExp) newChild);
			return;
		}

		if(_navigation_ == oldChild)
		{
			setNavigation((PNavigation) newChild);
			return;
		}

	}
	public void setExp(PExp node)
	{
		if(_exp_ != null)
		{
			_exp_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_exp_ = node;
	}
	public void setNavigation(PNavigation node)
	{
		if(_navigation_ != null)
		{
			_navigation_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_navigation_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_exp_)
			+ toString(_navigation_);
	}
}