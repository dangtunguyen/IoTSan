package edu.ksu.cis.bandera.specification.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.analysis.*;

public final class AFormatUnit extends PUnit
{
	private PFormat _format_;

	public AFormatUnit()
	{
	}
	public AFormatUnit(
		PFormat _format_)
	{
		setFormat(_format_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAFormatUnit(this);
	}
	public Object clone()
	{
		return new AFormatUnit(
			(PFormat) cloneNode(_format_));
	}
	public PFormat getFormat()
	{
		return _format_;
	}
	void removeChild(Node child)
	{
		if(_format_ == child)
		{
			_format_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_format_ == oldChild)
		{
			setFormat((PFormat) newChild);
			return;
		}

	}
	public void setFormat(PFormat node)
	{
		if(_format_ != null)
		{
			_format_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_format_ = node;
	}
	public String toString()
	{
		return ""
			+ toString(_format_);
	}
}