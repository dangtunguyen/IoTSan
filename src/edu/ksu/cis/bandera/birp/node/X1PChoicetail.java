/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class X1PChoicetail extends XPChoicetail
{
    private XPChoicetail _xPChoicetail_;
    private PChoicetail _pChoicetail_;

    public X1PChoicetail()
    {
    }

    public X1PChoicetail(
        XPChoicetail _xPChoicetail_,
        PChoicetail _pChoicetail_)
    {
        setXPChoicetail(_xPChoicetail_);
        setPChoicetail(_pChoicetail_);
    }

    public Object clone()
    {
        throw new RuntimeException("Unsupported Operation");
    }

    public void apply(Switch sw)
    {
        throw new RuntimeException("Switch not supported.");
    }

    public XPChoicetail getXPChoicetail()
    {
        return _xPChoicetail_;
    }

    public void setXPChoicetail(XPChoicetail node)
    {
        if(_xPChoicetail_ != null)
        {
            _xPChoicetail_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _xPChoicetail_ = node;
    }

    public PChoicetail getPChoicetail()
    {
        return _pChoicetail_;
    }

    public void setPChoicetail(PChoicetail node)
    {
        if(_pChoicetail_ != null)
        {
            _pChoicetail_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _pChoicetail_ = node;
    }

    void removeChild(Node child)
    {
        if(_xPChoicetail_ == child)
        {
            _xPChoicetail_ = null;
        }

        if(_pChoicetail_ == child)
        {
            _pChoicetail_ = null;
        }
    }

    void replaceChild(Node oldChild, Node newChild)
    {
    }

    public String toString()
    {
        return "" +
            toString(_xPChoicetail_) +
            toString(_pChoicetail_);
    }
}
