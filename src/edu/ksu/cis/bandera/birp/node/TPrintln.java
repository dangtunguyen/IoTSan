/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class TPrintln extends Token
{
    public TPrintln()
    {
        super.setText("println");
    }

    public TPrintln(int line, int pos)
    {
        super.setText("println");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TPrintln(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTPrintln(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TPrintln text.");
    }
}