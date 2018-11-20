package edu.ksu.cis.bandera.specification.node;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.analysis.*;

public final class AAssert extends PAssert
{
	private TDocumentationComment _documentationComment_;
	private TId _id_;
	private TColon _colon_;
	private TEnable _enable_;
	private TAssertions _assertions_;
	private TLBrace _lBrace_;
	private PNames _names_;
	private TRBrace _rBrace_;
	private TSemicolon _semicolon_;

	public AAssert()
	{
	}
	public AAssert(
		TDocumentationComment _documentationComment_,
		TId _id_,
		TColon _colon_,
		TEnable _enable_,
		TAssertions _assertions_,
		TLBrace _lBrace_,
		PNames _names_,
		TRBrace _rBrace_,
		TSemicolon _semicolon_)
	{
		setDocumentationComment(_documentationComment_);

		setId(_id_);

		setColon(_colon_);

		setEnable(_enable_);

		setAssertions(_assertions_);

		setLBrace(_lBrace_);

		setNames(_names_);

		setRBrace(_rBrace_);

		setSemicolon(_semicolon_);

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAAssert(this);
	}
	public Object clone()
	{
		return new AAssert(
			(TDocumentationComment) cloneNode(_documentationComment_),
			(TId) cloneNode(_id_),
			(TColon) cloneNode(_colon_),
			(TEnable) cloneNode(_enable_),
			(TAssertions) cloneNode(_assertions_),
			(TLBrace) cloneNode(_lBrace_),
			(PNames) cloneNode(_names_),
			(TRBrace) cloneNode(_rBrace_),
			(TSemicolon) cloneNode(_semicolon_));
	}
	public TAssertions getAssertions()
	{
		return _assertions_;
	}
	public TColon getColon()
	{
		return _colon_;
	}
	public TDocumentationComment getDocumentationComment()
	{
		return _documentationComment_;
	}
	public TEnable getEnable()
	{
		return _enable_;
	}
	public TId getId()
	{
		return _id_;
	}
	public TLBrace getLBrace()
	{
		return _lBrace_;
	}
	public PNames getNames()
	{
		return _names_;
	}
	public TRBrace getRBrace()
	{
		return _rBrace_;
	}
	public TSemicolon getSemicolon()
	{
		return _semicolon_;
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

		if(_enable_ == child)
		{
			_enable_ = null;
			return;
		}

		if(_assertions_ == child)
		{
			_assertions_ = null;
			return;
		}

		if(_lBrace_ == child)
		{
			_lBrace_ = null;
			return;
		}

		if(_names_ == child)
		{
			_names_ = null;
			return;
		}

		if(_rBrace_ == child)
		{
			_rBrace_ = null;
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

		if(_enable_ == oldChild)
		{
			setEnable((TEnable) newChild);
			return;
		}

		if(_assertions_ == oldChild)
		{
			setAssertions((TAssertions) newChild);
			return;
		}

		if(_lBrace_ == oldChild)
		{
			setLBrace((TLBrace) newChild);
			return;
		}

		if(_names_ == oldChild)
		{
			setNames((PNames) newChild);
			return;
		}

		if(_rBrace_ == oldChild)
		{
			setRBrace((TRBrace) newChild);
			return;
		}

		if(_semicolon_ == oldChild)
		{
			setSemicolon((TSemicolon) newChild);
			return;
		}

	}
	public void setAssertions(TAssertions node)
	{
		if(_assertions_ != null)
		{
			_assertions_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_assertions_ = node;
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
	public void setEnable(TEnable node)
	{
		if(_enable_ != null)
		{
			_enable_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_enable_ = node;
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
	public void setLBrace(TLBrace node)
	{
		if(_lBrace_ != null)
		{
			_lBrace_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_lBrace_ = node;
	}
	public void setNames(PNames node)
	{
		if(_names_ != null)
		{
			_names_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_names_ = node;
	}
	public void setRBrace(TRBrace node)
	{
		if(_rBrace_ != null)
		{
			_rBrace_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_rBrace_ = node;
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
	public String toString()
	{
		return ""
			+ toString(_documentationComment_)
			+ toString(_id_)
			+ toString(_colon_)
			+ toString(_enable_)
			+ toString(_assertions_)
			+ toString(_lBrace_)
			+ toString(_names_)
			+ toString(_rBrace_)
			+ toString(_semicolon_);
	}
}