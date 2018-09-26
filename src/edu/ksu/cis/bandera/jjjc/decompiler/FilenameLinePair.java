package edu.ksu.cis.bandera.jjjc.decompiler;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import java.io.*;
public class FilenameLinePair {
	private static boolean isWindows = System.getProperty("os.name").indexOf("Windows") >= 0;
	protected String filename;
	protected int line;
/**
 * FilenameLinePair constructor comment.
 */
public FilenameLinePair(String filename, int line) {
	this.filename = new File(filename).getAbsolutePath();
	this.line = line;
	int i = this.filename.indexOf(File.separator + "." + File.separator);
	if (i >= 0) {
		this.filename = this.filename.substring(0, i) + this.filename.substring(i + 2);
	}
	if (isWindows)
		this.filename = this.filename.toLowerCase();
}
/**
 * 
 * @return boolean
 * @param o java.lang.Object
 */
public boolean equals(Object o) {
	if (o instanceof FilenameLinePair) {
		FilenameLinePair other = (FilenameLinePair) o;
		return filename.equals(other.filename) && (line == other.line);
	} else return false;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getFilename() {
	return filename;
}
/**
 * 
 * @return int
 */
public int getLine() {
	return line;
}
/**
 * 
 * @return int
 */
public int hashCode() {
	return toString().hashCode();
}
/**
 * 
 * @param newFilename java.lang.String
 */
public void setFilename(java.lang.String newFilename) {
	filename = newFilename;
}
/**
 * 
 * @param newLine int
 */
public void setLine(int newLine) {
	line = newLine;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return filename + "#" + line;
}
}
