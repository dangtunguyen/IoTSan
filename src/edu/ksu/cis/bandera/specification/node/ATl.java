package edu.ksu.cis.bandera.specification.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.analysis.*;

public final class ATl extends PTl
{
	private TDocumentationComment _documentationComment_;
	private TId _id_;
	private TColon _colon_;
	private final LinkedList _qtl_ = new TypedLinkedList(new Qtl_Cast());
	private final LinkedList _word_ = new TypedLinkedList(new Word_Cast());
	private TSemicolon _semicolon_;

	private class Qtl_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PQtl node = (PQtl) o;

			if((node.parent() != null) &&
				(node.parent() != ATl.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != ATl.this))
			{
				node.parent(ATl.this);
			}

			return node;
		}
	}

	private class Word_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PWord node = (PWord) o;

			if((node.parent() != null) &&
				(node.parent() != ATl.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != ATl.this))
			{
				node.parent(ATl.this);
			}

			return node;
		}
	}
	public ATl()
	{
	}
	public ATl(
		TDocumentationComment _documentationComment_,
		TId _id_,
		TColon _colon_,
		XPQtl _qtl_,
		XPWord _word_,
		TSemicolon _semicolon_)
	{
		setDocumentationComment(_documentationComment_);

		setId(_id_);

		setColon(_colon_);

		if(_qtl_ != null)
		{
			while(_qtl_ instanceof X1PQtl)
			{
				this._qtl_.addFirst(((X1PQtl) _qtl_).getPQtl());
				_qtl_ = ((X1PQtl) _qtl_).getXPQtl();
			}
			this._qtl_.addFirst(((X2PQtl) _qtl_).getPQtl());
		}

		if(_word_ != null)
		{
			while(_word_ instanceof X1PWord)
			{
				this._word_.addFirst(((X1PWord) _word_).getPWord());
				_word_ = ((X1PWord) _word_).getXPWord();
			}
			this._word_.addFirst(((X2PWord) _word_).getPWord());
		}

		setSemicolon(_semicolon_);

	}
	public ATl(
		TDocumentationComment _documentationComment_,
		TId _id_,
		TColon _colon_,
		List _qtl_,
		List _word_,
		TSemicolon _semicolon_)
	{
		setDocumentationComment(_documentationComment_);

		setId(_id_);

		setColon(_colon_);

		{
			this._qtl_.clear();
			this._qtl_.addAll(_qtl_);
		}

		{
			this._word_.clear();
			this._word_.addAll(_word_);
		}

		setSemicolon(_semicolon_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseATl(this);
	}
	public Object clone()
	{
		return new ATl(
			(TDocumentationComment) cloneNode(_documentationComment_),
			(TId) cloneNode(_id_),
			(TColon) cloneNode(_colon_),
			cloneList(_qtl_),
			cloneList(_word_),
			(TSemicolon) cloneNode(_semicolon_));
	}
	public TColon getColon()
	{
		return _colon_;
	}
	public TDocumentationComment getDocumentationComment()
	{
		return _documentationComment_;
	}
	public TId getId()
	{
		return _id_;
	}
	public LinkedList getQtl()
	{
		return _qtl_;
	}
	public TSemicolon getSemicolon()
	{
		return _semicolon_;
	}
	public LinkedList getWord()
	{
		return _word_;
	}
	void removeChild(Node child)
	{
		if(_documentationComment_ == child)
		{
			_documentationComment_ = null;
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

		if(_qtl_.remove(child))
		{
			return;
		}

		if(_word_.remove(child))
		{
			return;
		}

		if(_semicolon_ == child)
		{
			_semicolon_ = null;
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_documentationComment_ == oldChild)
		{
			setDocumentationComment((TDocumentationComment) newChild);
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

		for(ListIterator i = _qtl_.listIterator(); i.hasNext();)
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

		for(ListIterator i = _word_.listIterator(); i.hasNext();)
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

		if(_semicolon_ == oldChild)
		{
			setSemicolon((TSemicolon) newChild);
			return;
		}

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
	public void setDocumentationComment(TDocumentationComment node)
	{
		if(_documentationComment_ != null)
		{
			_documentationComment_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_documentationComment_ = node;
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
	public void setQtl(List list)
	{
		_qtl_.clear();
		_qtl_.addAll(list);
	}
	public void setSemicolon(TSemicolon node)
	{
		if(_semicolon_ != null)
		{
			_semicolon_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_semicolon_ = node;
	}
	public void setWord(List list)
	{
		_word_.clear();
		_word_.addAll(list);
	}
	public String toString()
	{
		return ""
			+ toString(_documentationComment_)
			+ toString(_id_)
			+ toString(_colon_)
			+ toString(_qtl_)
			+ toString(_word_)
			+ toString(_semicolon_);
	}
}