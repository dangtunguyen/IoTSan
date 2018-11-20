/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.sessions.parser.v1.node;

import edu.ksu.cis.bandera.sessions.parser.v1.analysis.*;

public final class X1PResource extends XPResource
{
    private XPResource _xPResource_;
    private PResource _pResource_;

    public X1PResource()
    {
    }

    public X1PResource(
        XPResource _xPResource_,
        PResource _pResource_)
    {
        setXPResource(_xPResource_);
        setPResource(_pResource_);
    }

    public Object clone()
    {
        throw new RuntimeException("Unsupported Operation");
    }

    public void apply(Switch sw)
    {
        throw new RuntimeException("Switch not supported.");
    }

    public XPResource getXPResource()
    {
        return _xPResource_;
    }

    public void setXPResource(XPResource node)
    {
        if(_xPResource_ != null)
        {
            _xPResource_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _xPResource_ = node;
    }

    public PResource getPResource()
    {
        return _pResource_;
    }

    public void setPResource(PResource node)
    {
        if(_pResource_ != null)
        {
            _pResource_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _pResource_ = node;
    }

    void removeChild(Node child)
    {
        if(_xPResource_ == child)
        {
            _xPResource_ = null;
        }

        if(_pResource_ == child)
        {
            _pResource_ = null;
        }
    }

    void replaceChild(Node oldChild, Node newChild)
    {
    }

    public String toString()
    {
        return "" +
            toString(_xPResource_) +
            toString(_pResource_);
    }
}