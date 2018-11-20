/* This file was generated by SableCC (http://www.sablecc.org/). */

package edu.ksu.cis.bandera.birp.node;

import java.util.*;
import edu.ksu.cis.bandera.birp.analysis.*;

public final class ALocation extends PLocation
{
    private TLoc _loc_;
    private TId _id_;
    private TColon _colon_;
    private PLiveset _livevars_;
    private final LinkedList _transformations_ = new TypedLinkedList(new Transformations_Cast());

    public ALocation()
    {
    }

    public ALocation(
        TLoc _loc_,
        TId _id_,
        TColon _colon_,
        PLiveset _livevars_,
        List _transformations_)
    {
        setLoc(_loc_);

        setId(_id_);

        setColon(_colon_);

        setLivevars(_livevars_);

        {
            this._transformations_.clear();
            this._transformations_.addAll(_transformations_);
        }

    }

    public ALocation(
        TLoc _loc_,
        TId _id_,
        TColon _colon_,
        PLiveset _livevars_,
        XPTransformation _transformations_)
    {
        setLoc(_loc_);

        setId(_id_);

        setColon(_colon_);

        setLivevars(_livevars_);

        if(_transformations_ != null)
        {
            while(_transformations_ instanceof X1PTransformation)
            {
                this._transformations_.addFirst(((X1PTransformation) _transformations_).getPTransformation());
                _transformations_ = ((X1PTransformation) _transformations_).getXPTransformation();
            }
            this._transformations_.addFirst(((X2PTransformation) _transformations_).getPTransformation());
        }

    }
    public Object clone()
    {
        return new ALocation(
            (TLoc) cloneNode(_loc_),
            (TId) cloneNode(_id_),
            (TColon) cloneNode(_colon_),
            (PLiveset) cloneNode(_livevars_),
            cloneList(_transformations_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALocation(this);
    }

    public TLoc getLoc()
    {
        return _loc_;
    }

    public void setLoc(TLoc node)
    {
        if(_loc_ != null)
        {
            _loc_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _loc_ = node;
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

    public TColon getColon()
    {
        return _colon_;
    }

    public void setColon(TColon node)
    {
        if(_colon_ != null)
        {
            _colon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _colon_ = node;
    }

    public PLiveset getLivevars()
    {
        return _livevars_;
    }

    public void setLivevars(PLiveset node)
    {
        if(_livevars_ != null)
        {
            _livevars_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _livevars_ = node;
    }

    public LinkedList getTransformations()
    {
        return _transformations_;
    }

    public void setTransformations(List list)
    {
        _transformations_.clear();
        _transformations_.addAll(list);
    }

    public String toString()
    {
        return ""
            + toString(_loc_)
            + toString(_id_)
            + toString(_colon_)
            + toString(_livevars_)
            + toString(_transformations_);
    }

    void removeChild(Node child)
    {
        if(_loc_ == child)
        {
            _loc_ = null;
            return;
        }

        if(_id_ == child)
        {
            _id_ = null;
            return;
        }

        if(_colon_ == child)
        {
            _colon_ = null;
            return;
        }

        if(_livevars_ == child)
        {
            _livevars_ = null;
            return;
        }

        if(_transformations_.remove(child))
        {
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_loc_ == oldChild)
        {
            setLoc((TLoc) newChild);
            return;
        }

        if(_id_ == oldChild)
        {
            setId((TId) newChild);
            return;
        }

        if(_colon_ == oldChild)
        {
            setColon((TColon) newChild);
            return;
        }

        if(_livevars_ == oldChild)
        {
            setLivevars((PLiveset) newChild);
            return;
        }

        for(ListIterator i = _transformations_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

    }

    private class Transformations_Cast implements Cast
    {
        public Object cast(Object o)
        {
            PTransformation node = (PTransformation) o;

            if((node.parent() != null) &&
                (node.parent() != ALocation.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != ALocation.this))
            {
                node.parent(ALocation.this);
            }

            return node;
        }
    }
}