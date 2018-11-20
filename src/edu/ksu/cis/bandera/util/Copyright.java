package edu.ksu.cis.bandera.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000  Roby Joehanes (robbyjo@cis.ksu.edu)           *
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
/**
 * This program simply giving copyright stamp on all Bandera programs.
*/
import java.io.*;
import java.util.*;

public class Copyright {
	//private static String name, year, email;
	private static String year = "1998-2001";
	private static Hashtable authors = null;
	private static Hashtable modif = null;
	private static String version = "0.4.0";
	private static CustomFileFilter filter = new CustomFileFilter();
	private static boolean recurseDir = false;
	private static boolean keepAuthor = true;
	private static String lineSeparator = System.getProperty("line.separator");
/**
 * 
 * @param fn java.lang.String
 */
public static boolean analyzeFile(File fn)
{
	String prolog = "/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *";
	String epilog = " * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */";
	boolean error = false;
	int state = 0;
	String copymsg = null;
	StringBuffer program = new StringBuffer("");
	LineNumberReader f;

	try
	{
		f = new LineNumberReader(new FileReader(fn));
	} catch (Exception e)
	{
		System.out.println("Failure to read file '"+fn.getAbsolutePath()+"'");
		return false;
	}

	System.out.print(fn+" -> ");
	String s;
	do
	{
		try {
			s = f.readLine();
			switch (state)
			{
				case 0:	if (s.indexOf(prolog)!=-1) state = 1;
						else program.append(s+lineSeparator);
						break;
				case 1:	if (s.indexOf(epilog)!=-1) state = 2; else
						if (s.indexOf("Copyright (C)")!=-1 && copymsg != null) copymsg = s;
						break;
				case 2:	if (s != null) program.append(s+lineSeparator);
			}
		} catch (Exception e) { break; }
	} while (s != null);

	try { f.close(); } catch (Exception ex) {}

	if (keepAuthor && copymsg != null)
	{
		copymsg = get(copymsg)+lineSeparator;
	} else
	{
		String author = "", email = "", pad = " * Copyright (C) "+year;
		StringBuffer buf = new StringBuffer(padString(" * Version: "+version,69)+"*"+lineSeparator);
		for (Enumeration e = authors.keys(); e.hasMoreElements(); )
		{
			author = (String) e.nextElement();
			email = (String) authors.get(author);
			buf.append(padString(pad+" "+author+" ("+email+")",69)+"*"+lineSeparator);
			pad = padString(" *",pad.length());
		}
		if (!modif.isEmpty())
		{
			buf.append(padString(" * All rights reserved.",69)+"*"+lineSeparator);
			buf.append(padString(" *",69)+"*"+lineSeparator);

			pad = padString(" * Modified by :",pad.length());
			for (Enumeration e = modif.keys(); e.hasMoreElements(); )
			{
				author = (String) e.nextElement();
				email = (String) authors.get(author);
				buf.append(padString(pad+" "+author+" ("+email+")",69)+"*"+lineSeparator);
				pad = padString(" *",pad.length());
			}
			buf.append(padString(" * Modifications are copyrighted into the respective person.",69)+"*"+lineSeparator);
		}
	}

	try
	{
		PrintStream out = new PrintStream(new FileOutputStream(fn));
		out.print(copymsg+program.toString());
		out.close();
	} catch (IOException ex)
	{
		System.out.println("Cannot write to file '" + fn + "'");
	}
	return true;
}
private static void dirLister(File f)
{
	File[] files = f.listFiles(filter);
	for (int i=0; i < files.length; i++)
	{
		if (files[i].isDirectory())
		{
			if (recurseDir) dirLister(files[i]);
		} else
		{
			analyzeFile(files[i]);
			//System.out.println(files[i]);
		}
	}
}
private static void error(String s)
{
	System.out.println(s);
	System.exit(1);
}
public static String get()
{
	return get("1998-2001","SAnToS Laboratories","santos@cis.ksu.edu");
}
public static String get(String s)
{
	StringBuffer buf = new StringBuffer();

	buf.append("/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *"+lineSeparator);
	buf.append(" * Bandera, a Java(TM) analysis and transformation toolkit           *"+lineSeparator);
	buf.append(s);
	buf.append(" * All rights reserved.                                              *"+lineSeparator);
	buf.append(" *                                                                   *"+lineSeparator);
	buf.append(" * This work was done as a project in the SAnToS Laboratory,         *"+lineSeparator);
	buf.append(" * Department of Computing and Information Sciences, Kansas State    *"+lineSeparator);
	buf.append(" * University, USA (http://www.cis.ksu.edu/santos).                  *"+lineSeparator);
	buf.append(" * It is understood that any modification not identified as such is  *"+lineSeparator);
	buf.append(" * not covered by the preceding statement.                           *"+lineSeparator);
	buf.append(" *                                                                   *"+lineSeparator);
	buf.append(" * This work is free software; you can redistribute it and/or        *"+lineSeparator);
	buf.append(" * modify it under the terms of the GNU Library General Public       *"+lineSeparator);
	buf.append(" * License as published by the Free Software Foundation; either      *"+lineSeparator);
	buf.append(" * version 2 of the License, or (at your option) any later version.  *"+lineSeparator);
	buf.append(" *                                                                   *"+lineSeparator);
	buf.append(" * This work is distributed in the hope that it will be useful,      *"+lineSeparator);
	buf.append(" * but WITHOUT ANY WARRANTY; without even the implied warranty of    *"+lineSeparator);
	buf.append(" * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *"+lineSeparator);
	buf.append(" * Library General Public License for more details.                  *"+lineSeparator);
	buf.append(" *                                                                   *"+lineSeparator);
	buf.append(" * You should have received a copy of the GNU Library General Public *"+lineSeparator);
	buf.append(" * License along with this toolkit; if not, write to the             *"+lineSeparator);
	buf.append(" * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *"+lineSeparator);
	buf.append(" * Boston, MA  02111-1307, USA.                                      *"+lineSeparator);
	buf.append(" *                                                                   *"+lineSeparator);
	buf.append(" * Java is a trademark of Sun Microsystems, Inc.                     *"+lineSeparator);
	buf.append(" *                                                                   *"+lineSeparator);
	buf.append(" * To submit a bug report, send a comment, or get the latest news on *"+lineSeparator);
	buf.append(" * this project and other SAnToS projects, please visit the web-site *"+lineSeparator);
	buf.append(" *                http://www.cis.ksu.edu/santos                      *"+lineSeparator);
	buf.append(" * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */"+lineSeparator);
	return buf.toString();
}
public static String get(String year, String name, String email)
{
	StringBuffer copyrite = new StringBuffer(" * Copyright (C) "+year+" "+name+" ("+email+")");

	while (copyrite.length()<69) { copyrite.append(" "); }
	copyrite.append("*"+lineSeparator);

	return get(copyrite.toString());
}
/**
 * Help
 */
private static void help()
{
	System.out.println("Usage:   java Copyright [options] filename1 [filename2...]");
	System.out.println("Switches:");
	System.out.println("   -a    To specify the authors");
	System.out.println("   -m    To specify the modifiers of the source");
	System.out.println("   -p    To specify the path");
	System.out.println("   -s    To search for subdirectories as well");
	System.out.println("   -v    To specify the version");
	System.out.println("   -y    To specify the copyright year");
	System.out.println("Example: java Copyright -s \"*.java\"");
	System.out.println();
	System.out.println("Warning: You have to surround the wildcards in quotes if you use it in UNIX");
	System.exit(1);
}
/**
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args)
{
	if (args.length < 1) help();
	int start = 0;
	LinkedList l = new LinkedList();
	String path = ".";

	for (int i=0; i<args.length; i++)
	{
		if (args[i].startsWith("-s")) recurseDir = true;
		else if (args[i].startsWith("-a"))
		{
			try {
				authors = parseEmail(args[i].substring(2));
				keepAuthor = false;
			} catch (Exception e)
			{
				error("Error when parsing author name!");
			}
		} else if (args[i].startsWith("-y"))
		{
			try {
				year = args[i].substring(2);
				if (year == null || year.equals("")) throw new Exception();
			} catch (Exception e)
			{
				error("Error when parsing the year!");
			}
		} else if (args[i].startsWith("-m"))
		{
			try {
				modif = parseEmail(args[i].substring(2));
			} catch (Exception e)
			{
				error("Error when parsing modifier name!");
			}
		} else if (args[i].startsWith("-v"))
		{
			try {
				version = args[i].substring(2);
				if (version == null || version.equals("")) throw new Exception();
			} catch (Exception e)
			{
				error("Error when parsing version name!");
			}
		} else if (args[i].startsWith("-p"))
		{
			path = args[i].substring(2).trim();
			if (path == null || path.equals("")) path = ".";
		} else
		{
			l.addLast(args[i].trim());
		}
	}
	if (authors == null)
	{
		authors = new Hashtable();
		authors.put("SAnToS Laboratories","santos@cis.ksu.edu");
	}
	if (modif == null) modif = new Hashtable();
	if (l.isEmpty()) error("No file names specified");

	filter.setFileList(l);
	System.out.println("Analyzing:");
	//dirLister(new File(path));
}
private static String padString(String s, int n)
{
	StringBuffer buf = new StringBuffer(s);
	while (buf.length()<n) { buf.append(" "); }
	return buf.toString();
}
private static Hashtable parseEmail(String s)
{
	Hashtable tbl = new Hashtable();
	StringTokenizer tok = new StringTokenizer(s, ";");
	while (tok.hasMoreTokens())
	{
		StringTokenizer etok = new StringTokenizer(tok.nextToken(), ",");
		String author = etok.nextToken().trim();
		String email = etok.nextToken().trim();
		if (author == null || email == null) throw new RuntimeException();
		tbl.put(author.replace('_',' '), email);
	}
	return tbl;
}
}
