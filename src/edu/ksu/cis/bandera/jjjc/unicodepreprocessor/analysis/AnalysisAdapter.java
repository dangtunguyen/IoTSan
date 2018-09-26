package edu.ksu.cis.bandera.jjjc.unicodepreprocessor.analysis;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Robby (robby@cis.ksu.edu)              *
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
import java.util.*;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.node.*;

public class AnalysisAdapter implements Analysis
{
	private Hashtable in;
	private Hashtable out;

	public void caseEOF(EOF node)
	{
		defaultCase(node);
	}
	public void caseTErroneousEscape(TErroneousEscape node)
	{
		defaultCase(node);
	}
	public void caseTEvenBackslash(TEvenBackslash node)
	{
		defaultCase(node);
	}
	public void caseTRawInputCharacter(TRawInputCharacter node)
	{
		defaultCase(node);
	}
	public void caseTSub(TSub node)
	{
		defaultCase(node);
	}
	public void caseTUnicodeEscape(TUnicodeEscape node)
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
