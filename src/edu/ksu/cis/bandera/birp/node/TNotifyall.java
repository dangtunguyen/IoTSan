/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class TNotifyall extends Token
{
    public TNotifyall()
    {
        super.setText("notifyAll");
    }

    public TNotifyall(int line, int pos)
    {
        super.setText("notifyAll");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TNotifyall(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTNotifyall(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TNotifyall text.");
    }
}