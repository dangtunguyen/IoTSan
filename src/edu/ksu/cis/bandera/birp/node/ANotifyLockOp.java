/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class ANotifyLockOp extends PLockOp
{
    private TNotify _notify_;

    public ANotifyLockOp()
    {
    }

    public ANotifyLockOp(
        TNotify _notify_)
    {
        setNotify(_notify_);

    }
    public Object clone()
    {
        return new ANotifyLockOp(
            (TNotify) cloneNode(_notify_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANotifyLockOp(this);
    }

    public TNotify getNotify()
    {
        return _notify_;
    }

    public void setNotify(TNotify node)
    {
        if(_notify_ != null)
        {
            _notify_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _notify_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_notify_);
    }

    void removeChild(Node child)
    {
        if(_notify_ == child)
        {
            _notify_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_notify_ == oldChild)
        {
            setNotify((TNotify) newChild);
            return;
        }

    }
}
