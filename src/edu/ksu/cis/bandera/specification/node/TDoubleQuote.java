package edu.ksu.cis.bandera.specification.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import edu.ksu.cis.bandera.specification.analysis.*;

public final class TDoubleQuote extends Token
{
	public TDoubleQuote()
	{
		super.setText("\"");
	}
	public TDoubleQuote(int line, int pos)
	{
		super.setText("\"");
		setLine(line);
		setPos(pos);
	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseTDoubleQuote(this);
	}
	public Object clone()
	{
	  return new TDoubleQuote(getLine(), getPos());
	}
	public void setText(String text)
	{
		throw new RuntimeException("Cannot change TDoubleQuote text.");
	}
}