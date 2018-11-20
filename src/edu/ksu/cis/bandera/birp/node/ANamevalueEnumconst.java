/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class ANamevalueEnumconst extends PEnumconst
{
    private TId _id_;
    private TEquals _equals_;
    private TInt _int_;

    public ANamevalueEnumconst()
    {
    }

    public ANamevalueEnumconst(
        TId _id_,
        TEquals _equals_,
        TInt _int_)
    {
        setId(_id_);

        setEquals(_equals_);

        setInt(_int_);

    }
    public Object clone()
    {
        return new ANamevalueEnumconst(
            (TId) cloneNode(_id_),
            (TEquals) cloneNode(_equals_),
            (TInt) cloneNode(_int_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseANamevalueEnumconst(this);
    }

    public TId getId()
    {
        return _id_;
    }

    public void setId(TId node)
    {
        if(_id_ != null)
        {
            _id_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _id_ = node;
    }

    public TEquals getEquals()
    {
        return _equals_;
    }

    public void setEquals(TEquals node)
    {
        if(_equals_ != null)
        {
            _equals_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _equals_ = node;
    }

    public TInt getInt()
    {
        return _int_;
    }

    public void setInt(TInt node)
    {
        if(_int_ != null)
        {
            _int_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _int_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_id_)
            + toString(_equals_)
            + toString(_int_);
    }

    void removeChild(Node child)
    {
        if(_id_ == child)
        {
            _id_ = null;
            return;
        }

        if(_equals_ == child)
        {
            _equals_ = null;
            return;
        }

        if(_int_ == child)
        {
            _int_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_id_ == oldChild)
        {
            setId((TId) newChild);
            return;
        }

        if(_equals_ == oldChild)
        {
            setEquals((TEquals) newChild);
            return;
        }

        if(_int_ == oldChild)
        {
            setInt((TInt) newChild);
            return;
        }

    }
}