/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class TPredicates extends Token
{
    public TPredicates()
    {
        super.setText("predicates");
    }

    public TPredicates(int line, int pos)
    {
        super.setText("predicates");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TPredicates(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTPredicates(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TPredicates text.");
    }
}
