/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import edu.ksu.cis.bandera.birp.analysis.*;

public final class X2PSubtype extends XPSubtype
{
    private PSubtype _pSubtype_;

    public X2PSubtype()
    {
    }

    public X2PSubtype(
        PSubtype _pSubtype_)
    {
        setPSubtype(_pSubtype_);
    }

    public Object clone()
    {
        throw new RuntimeException("Unsupported Operation");
    }

    public void apply(Switch sw)
    {
        throw new RuntimeException("Switch not supported.");
    }

    public PSubtype getPSubtype()
    {
        return _pSubtype_;
    }

    public void setPSubtype(PSubtype node)
    {
        if(_pSubtype_ != null)
        {
            _pSubtype_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _pSubtype_ = node;
    }

    void removeChild(Node child)
    {
        if(_pSubtype_ == child)
        {
            _pSubtype_ = null;
        }
    }

    void replaceChild(Node oldChild, Node newChild)
    {
    }

    public String toString()
    {
        return "" +
            toString(_pSubtype_);
    }
}