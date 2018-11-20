/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class X1PLocation extends XPLocation
{
    private XPLocation _xPLocation_;
    private PLocation _pLocation_;

    public X1PLocation()
    {
    }

    public X1PLocation(
        XPLocation _xPLocation_,
        PLocation _pLocation_)
    {
        setXPLocation(_xPLocation_);
        setPLocation(_pLocation_);
    }

    public Object clone()
    {
        throw new RuntimeException("Unsupported Operation");
    }

    public void apply(Switch sw)
    {
        throw new RuntimeException("Switch not supported.");
    }

    public XPLocation getXPLocation()
    {
        return _xPLocation_;
    }

    public void setXPLocation(XPLocation node)
    {
        if(_xPLocation_ != null)
        {
            _xPLocation_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _xPLocation_ = node;
    }

    public PLocation getPLocation()
    {
        return _pLocation_;
    }

    public void setPLocation(PLocation node)
    {
        if(_pLocation_ != null)
        {
            _pLocation_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _pLocation_ = node;
    }

    void removeChild(Node child)
    {
        if(_xPLocation_ == child)
        {
            _xPLocation_ = null;
        }

        if(_pLocation_ == child)
        {
            _pLocation_ = null;
        }
    }

    void replaceChild(Node oldChild, Node newChild)
    {
    }

    public String toString()
    {
        return "" +
            toString(_xPLocation_) +
            toString(_pLocation_);
    }
}