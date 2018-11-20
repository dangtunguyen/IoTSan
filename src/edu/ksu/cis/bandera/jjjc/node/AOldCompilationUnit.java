package edu.ksu.cis.bandera.jjjc.node;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;

public final class AOldCompilationUnit extends PCompilationUnit
{
	private PPackageDeclaration _packageDeclaration_;
	private final LinkedList _importDeclaration_ = new TypedLinkedList(new ImportDeclaration_Cast());
	private final LinkedList _typeDeclaration_ = new TypedLinkedList(new TypeDeclaration_Cast());

	private class ImportDeclaration_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PImportDeclaration node = (PImportDeclaration) o;

			if((node.parent() != null) &&
				(node.parent() != AOldCompilationUnit.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AOldCompilationUnit.this))
			{
				node.parent(AOldCompilationUnit.this);
			}

			return node;
		}
	}

	private class TypeDeclaration_Cast implements Cast
	{
		public Object cast(Object o)
		{
			PTypeDeclaration node = (PTypeDeclaration) o;

			if((node.parent() != null) &&
				(node.parent() != AOldCompilationUnit.this))
			{
				node.parent().removeChild(node);
			}

			if((node.parent() == null) ||
				(node.parent() != AOldCompilationUnit.this))
			{
				node.parent(AOldCompilationUnit.this);
			}

			return node;
		}
	}
	public AOldCompilationUnit()
	{
	}
	public AOldCompilationUnit(
		PPackageDeclaration _packageDeclaration_,
		List _importDeclaration_,
		List _typeDeclaration_)
	{
		setPackageDeclaration(_packageDeclaration_);

		{
			Object temp[] = _importDeclaration_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._importDeclaration_.add(temp[i]);
			}
		}

		{
			Object temp[] = _typeDeclaration_.toArray();
			for(int i = 0; i < temp.length; i++)
			{
				this._typeDeclaration_.add(temp[i]);
			}
		}

	}
	public AOldCompilationUnit(
		PPackageDeclaration _packageDeclaration_,
		XPImportDeclaration _importDeclaration_,
		XPTypeDeclaration _typeDeclaration_)
	{
		setPackageDeclaration(_packageDeclaration_);

		if(_importDeclaration_ != null)
		{
			while(_importDeclaration_ instanceof X1PImportDeclaration)
			{
				this._importDeclaration_.addFirst(((X1PImportDeclaration) _importDeclaration_).getPImportDeclaration());
				_importDeclaration_ = ((X1PImportDeclaration) _importDeclaration_).getXPImportDeclaration();
			}
			this._importDeclaration_.addFirst(((X2PImportDeclaration) _importDeclaration_).getPImportDeclaration());
		}

		if(_typeDeclaration_ != null)
		{
			while(_typeDeclaration_ instanceof X1PTypeDeclaration)
			{
				this._typeDeclaration_.addFirst(((X1PTypeDeclaration) _typeDeclaration_).getPTypeDeclaration());
				_typeDeclaration_ = ((X1PTypeDeclaration) _typeDeclaration_).getXPTypeDeclaration();
			}
			this._typeDeclaration_.addFirst(((X2PTypeDeclaration) _typeDeclaration_).getPTypeDeclaration());
		}

	}
	public void apply(Switch sw)
	{
		((Analysis) sw).caseAOldCompilationUnit(this);
	}
	public Object clone()
	{
		return new AOldCompilationUnit(
			(PPackageDeclaration) cloneNode(_packageDeclaration_),
			cloneList(_importDeclaration_),
			cloneList(_typeDeclaration_));
	}
	public LinkedList getImportDeclaration()
	{
		return _importDeclaration_;
	}
	public PPackageDeclaration getPackageDeclaration()
	{
		return _packageDeclaration_;
	}
	public LinkedList getTypeDeclaration()
	{
		return _typeDeclaration_;
	}
	void removeChild(Node child)
	{
		if(_packageDeclaration_ == child)
		{
			_packageDeclaration_ = null;
			return;
		}

		if(_importDeclaration_.remove(child))
		{
			return;
		}

		if(_typeDeclaration_.remove(child))
		{
			return;
		}

	}
	void replaceChild(Node oldChild, Node newChild)
	{
		if(_packageDeclaration_ == oldChild)
		{
			setPackageDeclaration((PPackageDeclaration) newChild);
			return;
		}

		for(ListIterator i = _importDeclaration_.listIterator(); i.hasNext();)
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

		for(ListIterator i = _typeDeclaration_.listIterator(); i.hasNext();)
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
	public void setImportDeclaration(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_importDeclaration_.add(temp[i]);
		}
	}
	public void setPackageDeclaration(PPackageDeclaration node)
	{
		if(_packageDeclaration_ != null)
		{
			_packageDeclaration_.parent(null);
		}

		if(node != null)
		{
			if(node.parent() != null)
			{
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		_packageDeclaration_ = node;
	}
	public void setTypeDeclaration(List list)
	{
		Object temp[] = list.toArray();
		for(int i = 0; i < temp.length; i++)
		{
			_typeDeclaration_.add(temp[i]);
		}
	}
	public String toString()
	{
		return ""
			+ toString(_packageDeclaration_)
			+ toString(_importDeclaration_)
			+ toString(_typeDeclaration_);
	}
}
