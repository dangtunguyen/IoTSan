/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class ARangeTypespec extends PTypespec
{
    private TRange _range_;
    private PConst _low_;
    private TDotdot _dotdot_;
    private PConst _hi_;

    public ARangeTypespec()
    {
    }

    public ARangeTypespec(
        TRange _range_,
        PConst _low_,
        TDotdot _dotdot_,
        PConst _hi_)
    {
        setRange(_range_);

        setLow(_low_);

        setDotdot(_dotdot_);

        setHi(_hi_);

    }
    public Object clone()
    {
        return new ARangeTypespec(
            (TRange) cloneNode(_range_),
            (PConst) cloneNode(_low_),
            (TDotdot) cloneNode(_dotdot_),
            (PConst) cloneNode(_hi_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseARangeTypespec(this);
    }

    public TRange getRange()
    {
        return _range_;
    }

    public void setRange(TRange node)
    {
        if(_range_ != null)
        {
            _range_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _range_ = node;
    }

    public PConst getLow()
    {
        return _low_;
    }

    public void setLow(PConst node)
    {
        if(_low_ != null)
        {
            _low_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _low_ = node;
    }

    public TDotdot getDotdot()
    {
        return _dotdot_;
    }

    public void setDotdot(TDotdot node)
    {
        if(_dotdot_ != null)
        {
            _dotdot_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _dotdot_ = node;
    }

    public PConst getHi()
    {
        return _hi_;
    }

    public void setHi(PConst node)
    {
        if(_hi_ != null)
        {
            _hi_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _hi_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_range_)
            + toString(_low_)
            + toString(_dotdot_)
            + toString(_hi_);
    }

    void removeChild(Node child)
    {
        if(_range_ == child)
        {
            _range_ = null;
            return;
        }

        if(_low_ == child)
        {
            _low_ = null;
            return;
        }

        if(_dotdot_ == child)
        {
            _dotdot_ = null;
            return;
        }

        if(_hi_ == child)
        {
            _hi_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_range_ == oldChild)
        {
            setRange((TRange) newChild);
            return;
        }

        if(_low_ == oldChild)
        {
            setLow((PConst) newChild);
            return;
        }

        if(_dotdot_ == oldChild)
        {
            setDotdot((TDotdot) newChild);
            return;
        }

        if(_hi_ == oldChild)
        {
            setHi((PConst) newChild);
            return;
        }

    }
}