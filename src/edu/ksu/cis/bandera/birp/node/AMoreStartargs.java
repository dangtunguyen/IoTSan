/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class AMoreStartargs extends PStartargs
{
    private PStartarg _startarg_;
    private TComma _comma_;
    private PStartargs _startargs_;

    public AMoreStartargs()
    {
    }

    public AMoreStartargs(
        PStartarg _startarg_,
        TComma _comma_,
        PStartargs _startargs_)
    {
        setStartarg(_startarg_);

        setComma(_comma_);

        setStartargs(_startargs_);

    }
    public Object clone()
    {
        return new AMoreStartargs(
            (PStartarg) cloneNode(_startarg_),
            (TComma) cloneNode(_comma_),
            (PStartargs) cloneNode(_startargs_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMoreStartargs(this);
    }

    public PStartarg getStartarg()
    {
        return _startarg_;
    }

    public void setStartarg(PStartarg node)
    {
        if(_startarg_ != null)
        {
            _startarg_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _startarg_ = node;
    }

    public TComma getComma()
    {
        return _comma_;
    }

    public void setComma(TComma node)
    {
        if(_comma_ != null)
        {
            _comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _comma_ = node;
    }

    public PStartargs getStartargs()
    {
        return _startargs_;
    }

    public void setStartargs(PStartargs node)
    {
        if(_startargs_ != null)
        {
            _startargs_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _startargs_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_startarg_)
            + toString(_comma_)
            + toString(_startargs_);
    }

    void removeChild(Node child)
    {
        if(_startarg_ == child)
        {
            _startarg_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_startargs_ == child)
        {
            _startargs_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_startarg_ == oldChild)
        {
            setStartarg((PStartarg) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_startargs_ == oldChild)
        {
            setStartargs((PStartargs) newChild);
            return;
        }

    }
}