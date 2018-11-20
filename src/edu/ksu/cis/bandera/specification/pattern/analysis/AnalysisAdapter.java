package edu.ksu.cis.bandera.specification.pattern.analysis;

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
/* This file was generated by SableCC (http://www.sable.mcgill.ca/sablecc/). */

import java.util.*;
import edu.ksu.cis.bandera.specification.pattern.node.*;

public class AnalysisAdapter implements Analysis
{
	private Hashtable in;
	private Hashtable out;

	public void caseAIdIds(AIdIds node)
	{
		defaultCase(node);
	}
	public void caseAIdsIds(AIdsIds node)
	{
		defaultCase(node);
	}
	public void caseAParamResource(AParamResource node)
	{
		defaultCase(node);
	}
	public void caseAPattern(APattern node)
	{
		defaultCase(node);
	}
	public void caseAStringResource(AStringResource node)
	{
		defaultCase(node);
	}
	public void caseAStringsStrings(AStringsStrings node)
	{
		defaultCase(node);
	}
	public void caseAStringStrings(AStringStrings node)
	{
		defaultCase(node);
	}
	public void caseAUnit(AUnit node)
	{
		defaultCase(node);
	}
	public void caseEOF(EOF node)
	{
		defaultCase(node);
	}
	public void caseStart(Start node)
	{
		defaultCase(node);
	}
	public void caseTComma(TComma node)
	{
		defaultCase(node);
	}
	public void caseTEqual(TEqual node)
	{
		defaultCase(node);
	}
	public void caseTId(TId node)
	{
		defaultCase(node);
	}
	public void caseTLBrace(TLBrace node)
	{
		defaultCase(node);
	}
	public void caseTPattern(TPattern node)
	{
		defaultCase(node);
	}
	public void caseTPlus(TPlus node)
	{
		defaultCase(node);
	}
	public void caseTRBrace(TRBrace node)
	{
		defaultCase(node);
	}
	public void caseTStringLiteral(TStringLiteral node)
	{
		defaultCase(node);
	}
	public void caseTWhiteSpace(TWhiteSpace node)
	{
		defaultCase(node);
	}
	public void defaultCase(Node node)
	{
	}
	public Object getIn(Node node)
	{
		if(in == null)
		{
			return null;
		}

		return in.get(node);
	}
	public Object getOut(Node node)
	{
		if(out == null)
		{
			return null;
		}

		return out.get(node);
	}
	public void setIn(Node node, Object in)
	{
		if(this.in == null)
		{
			this.in = new Hashtable(1);
		}

		if(in != null)
		{
			this.in.put(node, in);
		}
		else
		{
			this.in.remove(node);
		}
	}
	public void setOut(Node node, Object out)
	{
		if(this.out == null)
		{
			this.out = new Hashtable(1);
		}

		if(out != null)
		{
			this.out.put(node, out);
		}
		else
		{
			this.out.remove(node);
		}
	}
}