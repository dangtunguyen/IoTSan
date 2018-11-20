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
/**
 * DecompilerPair is basically a helper class to point out between output
 * line number to an annotation required by Counter Example.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
 */
import java.io.*;
public class DecompilerPair extends FilenameLinePair {
	private static String outputPath = "";
public DecompilerPair (int lineNum)
{
	super(outputPath + DecompilerInfo.getClassName()+"."+Decompiler.fileSuffix,lineNum);
}
/**
 * FilenameLinePair constructor comment.
 */
public DecompilerPair(String filename, int line) {
	super(filename,line);
}
public void addBy(int offset) { line += offset; }
public static void setOutputPath(String op)
{
	outputPath = op + File.separator;
}
}
