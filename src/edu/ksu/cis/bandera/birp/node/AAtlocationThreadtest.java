/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class AAtlocationThreadtest extends PThreadtest
{
    private TId _threadname_;
    private TLbrace _lbrace_;
    private PLhs _lhs_;
    private TRbrace _rbrace_;
    private TAt _at_;
    private TId _locname_;

    public AAtlocationThreadtest()
    {
    }

    public AAtlocationThreadtest(
        TId _threadname_,
        TLbrace _lbrace_,
        PLhs _lhs_,
        TRbrace _rbrace_,
        TAt _at_,
        TId _locname_)
    {
        setThreadname(_threadname_);

        setLbrace(_lbrace_);

        setLhs(_lhs_);

        setRbrace(_rbrace_);

        setAt(_at_);

        setLocname(_locname_);

    }
    public Object clone()
    {
        return new AAtlocationThreadtest(
            (TId) cloneNode(_threadname_),
            (TLbrace) cloneNode(_lbrace_),
            (PLhs) cloneNode(_lhs_),
            (TRbrace) cloneNode(_rbrace_),
            (TAt) cloneNode(_at_),
            (TId) cloneNode(_locname_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAtlocationThreadtest(this);
    }

    public TId getThreadname()
    {
        return _threadname_;
    }

    public void setThreadname(TId node)
    {
        if(_threadname_ != null)
        {
            _threadname_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _threadname_ = node;
    }

    public TLbrace getLbrace()
    {
        return _lbrace_;
    }

    public void setLbrace(TLbrace node)
    {
        if(_lbrace_ != null)
        {
            _lbrace_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lbrace_ = node;
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

    public TRbrace getRbrace()
    {
        return _rbrace_;
    }

    public void setRbrace(TRbrace node)
    {
        if(_rbrace_ != null)
        {
            _rbrace_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _rbrace_ = node;
    }

    public TAt getAt()
    {
        return _at_;
    }

    public void setAt(TAt node)
    {
        if(_at_ != null)
        {
            _at_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _at_ = node;
    }

    public TId getLocname()
    {
        return _locname_;
    }

    public void setLocname(TId node)
    {
        if(_locname_ != null)
        {
            _locname_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _locname_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_threadname_)
            + toString(_lbrace_)
            + toString(_lhs_)
            + toString(_rbrace_)
            + toString(_at_)
            + toString(_locname_);
    }

    void removeChild(Node child)
    {
        if(_threadname_ == child)
        {
            _threadname_ = null;
            return;
        }

        if(_lbrace_ == child)
        {
            _lbrace_ = null;
            return;
        }

        if(_lhs_ == child)
        {
            _lhs_ = null;
            return;
        }

        if(_rbrace_ == child)
        {
            _rbrace_ = null;
            return;
        }

        if(_at_ == child)
        {
            _at_ = null;
            return;
        }

        if(_locname_ == child)
        {
            _locname_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_threadname_ == oldChild)
        {
            setThreadname((TId) newChild);
            return;
        }

        if(_lbrace_ == oldChild)
        {
            setLbrace((TLbrace) newChild);
            return;
        }

        if(_lhs_ == oldChild)
        {
            setLhs((PLhs) newChild);
            return;
        }

        if(_rbrace_ == oldChild)
        {
            setRbrace((TRbrace) newChild);
            return;
        }

        if(_at_ == oldChild)
        {
            setAt((TAt) newChild);
            return;
        }

        if(_locname_ == oldChild)
        {
            setLocname((TId) newChild);
            return;
        }

    }
}