package edu.ksu.cis.bandera.jjjc.codegenerator;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2002   Robby (robby@cis.ksu.edu)                    *
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
public class LineColumn {
	private int firstLine, firstColumn;
	private int lastLine, lastColumn;
/**
 * 
 * @param firstLine int
 * @param firstColumn int
 * @param lastLine int
 * @param lastColumn int
 */
public LineColumn(int firstLine, int firstColumn, int lastLine, int lastColumn) {
	this.firstLine = firstLine;
	this.firstColumn = firstColumn;
	this.lastLine = lastLine;
	this.lastColumn = lastColumn;
}
/**
 * 
 * @return int
 */
public int getFirstColumn() {
	return firstColumn;
}
/**
 * 
 * @return int
 */
public int getFirstLine() {
	return firstLine;
}
/**
 * 
 * @return int
 */
public int getLastColumn() {
	return lastColumn;
}
/**
 * 
 * @return int
 */
public int getLastLine() {
	return lastLine;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return "(" + firstLine + ", " + firstColumn + ", " + lastLine + ", " + lastColumn + ")";
}
}
