/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class AExpr1Expr2 extends PExpr2
{
    private PExpr1 _expr1_;

    public AExpr1Expr2()
    {
    }

    public AExpr1Expr2(
        PExpr1 _expr1_)
    {
        setExpr1(_expr1_);

    }
    public Object clone()
    {
        return new AExpr1Expr2(
            (PExpr1) cloneNode(_expr1_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExpr1Expr2(this);
    }

    public PExpr1 getExpr1()
    {
        return _expr1_;
    }

    public void setExpr1(PExpr1 node)
    {
        if(_expr1_ != null)
        {
            _expr1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _expr1_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_expr1_);
    }

    void removeChild(Node child)
    {
        if(_expr1_ == child)
        {
            _expr1_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_expr1_ == oldChild)
        {
            setExpr1((PExpr1) newChild);
            return;
        }

    }
}