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
import java.util.*;
import java.io.*;

public class BanderaLog extends PrintStream {
	private PrintStream stdout = null;
	private PrintStream fileio = null;
	private boolean internal = false;
	protected boolean fails = false;
/**
 * BanderaLog constructor comment.
 * @param out java.io.OutputStream
 */
public BanderaLog(java.io.OutputStream out) {
	super(out);
}
/**
 * BanderaLog constructor comment.
 * @param out java.io.OutputStream
 * @param autoFlush boolean
 */
public BanderaLog(java.io.OutputStream out, boolean autoFlush) {
	super(out, autoFlush);
}
	public BanderaLog(PrintStream out, PrintStream f)
	{
		super(f); stdout = out; fileio = f;
	}
public boolean isError() { return fails; }
public void print(char[] s)  { if (!internal) stdout.print(s); fileio.print(s); }
public void print(char c)    { if (!internal) stdout.print(c); fileio.print(c); }
public void print(double d)  { if (!internal) stdout.print(d); fileio.print(d); }
public void print(float f)   { if (!internal) stdout.print(f); fileio.print(f); }
public void print(int i)     { if (!internal) stdout.print(i); fileio.print(i); }
public void print(long l)    { if (!internal) stdout.print(l); fileio.print(l); }
public void print(Object obj)  { if (!internal) stdout.print(obj); fileio.print(obj); }
public void print(String s)    { if (!internal) stdout.print(s); fileio.print(s); }
public void print(boolean b) { if (!internal) stdout.print(b); fileio.print(b); }
public void println()          { if (!internal) stdout.println(); fileio.println(); }
public void println(char[] x)  { if (!internal) stdout.println(x); fileio.println(x); }
public void println(char x)    { if (!internal) stdout.println(x); fileio.println(x); }
public void println(double x)  { if (!internal) stdout.println(x); fileio.println(x); }
public void println(float x)   { if (!internal) stdout.println(x); fileio.println(x); }
public void println(int x)     { if (!internal) stdout.println(x); fileio.println(x); }
public void println(long x)    { if (!internal) stdout.println(x); fileio.println(x); }
public void println(Object x)  { if (!internal) stdout.println(x); fileio.println(x); }
public void println(String x)  { if (!internal) stdout.println(x); fileio.println(x); }
public void println(boolean x) { if (!internal) stdout.println(x); fileio.println(x); }
protected void setError() { fails = true; }
public void setInternal(boolean b) { internal = b; }
public void write(byte[] buf, int off, int len)  { if (!internal) stdout.write(buf,off,len); fileio.write(buf,off,len); }
public void write(int b) { if (!internal) stdout.write(b); fileio.write(b); }
}
