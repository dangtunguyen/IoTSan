/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class TFalse extends Token
{
    public TFalse()
    {
        super.setText("false");
    }

    public TFalse(int line, int pos)
    {
        super.setText("false");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TFalse(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFalse(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TFalse text.");
    }
}