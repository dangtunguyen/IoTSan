package edu.ksu.cis.bandera.specification.predicate.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import edu.ksu.cis.bandera.specification.predicate.analysis.*;

public final class TFloat extends Token
{
	public TFloat()
	{
		super.setText("float");
	}
	public TFloat(int line, int pos)
	{
		super.setText("float");
		setLine(line);
		setPos(pos);
	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseTFloat(this);
	}
	public Object clone()
	{
	  return new TFloat(getLine(), getPos());
	}
	public void setText(String text)
	{
		throw new RuntimeException("Cannot change TFloat text.");
	}
}
