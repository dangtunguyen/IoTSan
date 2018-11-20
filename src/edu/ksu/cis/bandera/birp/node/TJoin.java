/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class TJoin extends Token
{
    public TJoin()
    {
        super.setText("join");
    }

    public TJoin(int line, int pos)
    {
        super.setText("join");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TJoin(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTJoin(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TJoin text.");
    }
}