package edu.ksu.cis.bandera.jjjc.decompiler;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000 Roby Joehanes (robbyjo@cis.ksu.edu)            *
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
import java.util.*;

/**
 * DecompilerPrinter is a helper class to write things to
 * the file output, pretty printed and (hopefully)
 * do the line to annotation stuff.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class DecompilerPrinter {
	private StringBuffer s = new StringBuffer();
	private int indent = 0;
	private final String indentString = "     ";
	private boolean beginLine = true;
	private boolean doIndent = true;

/**
 * SlabsPrinter constructor comment.
 */
public DecompilerPrinter()
{
	super();
}
public DecompilerPrinter(int i)
{
	super();
	indent = i;
}
public void add(Vector x)
{
	int max = x.size();

	for (int i=0; i<max; i++)
	{
		String st = (String) x.get(i);
		println(st);
	}
}
public void disableIndent()
{
	doIndent = false;
}
public void enableIndent()
{
	doIndent = true;
}
public int getIndent()
{
	return indent;
}
public void print(String str)
{
	if (str.indexOf("}")>-1)
		indent--;

	// If it is the beginning of the line, then print the indentation
	if ((beginLine) && (doIndent))
		for (int i = 0; i < indent; i++)
			s.append(indentString);

	if (str.indexOf("{")>-1)
		indent++;

	// Print the string
	s.append(str);
	beginLine = false;
}
public void println(String str)
{
	print(str+"\n");
	beginLine = true;
}
public void reset()
{
	s = new StringBuffer();
	indent = 0;
}
public void setIndent(int i)
{
	indent = i;
}
public String toString()
{
	return s.toString();
}
}
