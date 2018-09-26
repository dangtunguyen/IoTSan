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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;

import java.io.*;
import java.util.*;
/**
 * Decompiler driver, for internal testing only.
 * If you'd like to use the decompiler in a stand-alone manner, i.e. to test
 * the functionality of the Decompiler package, you can refer to DecompilerDriver class
 * and run the main method.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class DecompilerDriver {
public static void main(String[] args)
{

	// Set the compiler ready
	CompilationManager.setDoBSL(false);
	CompilationManager.reset();
	CompilationManager.setFilename("InheritScheduler.java");
	CompilationManager.setClasspath(".");
	CompilationManager.setIncludedPackagesOrTypes(new String[0]);

	// Do the real stuff: Compile
	try
	{
		CompilationManager.compile();
		if (CompilationManager.getExceptions().size() > 0)
		{
			throw new Exception("Compilation failed!\n");
		}
		System.out.println("Compiled successfully!\n");
	} catch (Exception e)
	{
		System.out.println("Compilation failed!\n");
	}

	// Then decompile!
	DecompilerUtil.setDebugInfo(false);
	Decompiler decompiler = new Decompiler(CompilationManager.getCompiledClasses(), CompilationManager.getAnnotationManager(),
		"E:\\HOME\\Robbyjo\\Bandera\\Output", "java");
	decompiler.decompile();

	/*
	Hashtable tbl = decompiler.getLineToAnnotationTable();

	for (Enumeration e = tbl.keys(); e.hasMoreElements();)
	{
		DecompilerPair p = (DecompilerPair) e.nextElement();
		Annotation an = (Annotation) tbl.get(p);
		if (an != null)
		System.out.println(p.toString()+" = "+an.toString());
		else
		System.out.println(p.toString()+" = null");
	}*/
	//CompilationManager.getAnnotationManager().setFilenameLinePairAnnotationTable(decompiler.getLineToAnnotationTable());
}
public String readln()
{
	StringBuffer s = new StringBuffer();
	char c;
	try
	{
		while ((c = (char) System.in.read()) != '\n')
			s.append(c);
	} catch (Exception e)
	{
		System.out.println("Error: " + e.toString());
		return null;
	}
	return s.toString();
}
}
