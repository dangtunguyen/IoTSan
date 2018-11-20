package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class X1TDim extends XTDim
{
	private XTDim _xTDim_;
	private TDim _tDim_;

	public X1TDim()
	{
	}
	public X1TDim(
		XTDim _xTDim_,
		TDim _tDim_)
	{
		setXTDim(_xTDim_);
		setTDim(_tDim_);
	}
	public void apply(Switch sw)
	{
		throw new RuntimeException("Switch not supported.");
	}
	public Object clone()
	{
		throw new RuntimeException("Unsupported Operation");
	}
	public TDim getTDim()
	{
		return _tDim_;
	}
	public XTDim getXTDim()
	{
		return _xTDim_;
	}
	void removeChild(Node child)
	{
		if(_xTDim_ == child)
		{
			_xTDim_ = null;
		}

		if(_tDim_ == child)
		{
			_tDim_ = null;
		}
	}
	void replaceChild(Node oldChild, Node newChild)
	{
	}
	public void setTDim(TDim node)
	{
		if(_tDim_ != null)
		{
			_tDim_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_tDim_ = node;
	}
	public void setXTDim(XTDim node)
	{
		if(_xTDim_ != null)
		{
			_xTDim_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_xTDim_ = node;
	}
	public String toString()
	{
		return "" +
			toString(_xTDim_) +
			toString(_tDim_);
	}
}