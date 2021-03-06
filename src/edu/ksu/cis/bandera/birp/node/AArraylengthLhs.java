/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class AArraylengthLhs extends PLhs
{
    private PLhs _lhs_;
    private TDot _dot_;
    private TLength _length_;

    public AArraylengthLhs()
    {
    }

    public AArraylengthLhs(
        PLhs _lhs_,
        TDot _dot_,
        TLength _length_)
    {
        setLhs(_lhs_);

        setDot(_dot_);

        setLength(_length_);

    }
    public Object clone()
    {
        return new AArraylengthLhs(
            (PLhs) cloneNode(_lhs_),
            (TDot) cloneNode(_dot_),
            (TLength) cloneNode(_length_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAArraylengthLhs(this);
    }

    public PLhs getLhs()
    {
        return _lhs_;
    }

    public void setLhs(PLhs node)
    {
        if(_lhs_ != null)
        {
            _lhs_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lhs_ = node;
    }

    public TDot getDot()
    {
        return _dot_;
    }

    public void setDot(TDot node)
    {
        if(_dot_ != null)
        {
            _dot_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _dot_ = node;
    }

    public TLength getLength()
    {
        return _length_;
    }

    public void setLength(TLength node)
    {
        if(_length_ != null)
        {
            _length_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _length_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_lhs_)
            + toString(_dot_)
            + toString(_length_);
    }

    void removeChild(Node child)
    {
        if(_lhs_ == child)
        {
            _lhs_ = null;
            return;
        }

        if(_dot_ == child)
        {
            _dot_ = null;
            return;
        }

        if(_length_ == child)
        {
            _length_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_lhs_ == oldChild)
        {
            setLhs((PLhs) newChild);
            return;
        }

        if(_dot_ == oldChild)
        {
            setDot((TDot) newChild);
            return;
        }

        if(_length_ == oldChild)
        {
            setLength((TLength) newChild);
            return;
        }

    }
}
