package edu.ksu.cis.bandera.abstraction.specification;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import java.util.*;
public class BASL2Java {
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) throws Exception {
	if ((args.length != 2) && (args.length != 1)) {
		System.out.println("Usage: java edu.ksu.cis.bandera.abstraction.specification.BASL2Java <basl-file> [<package-name>]");
		return;
	}
	String packageName = (args.length == 1) ? null : args[1];
	AbstractionGenerator ag = new AbstractionGenerator(new FileReader(args[0]));
	String code = ag.generate(packageName);
	if (ag.getErrors().size() > 0) {
		System.out.println("Errors:");
		System.out.println("=======");
		for (Iterator i = ag.getErrors().iterator(); i.hasNext();) {
			System.out.println("- " + i.next());
		}
		if (ag.getWarnings().size() > 0) {
			System.out.println();
			System.out.println("Warnings:");
			System.out.println("=========");
			for (Iterator i = ag.getWarnings().iterator(); i.hasNext();) {
				System.out.println("- " + i.next());
			}
		}
		return;
	}
	if (ag.getWarnings().size() > 0) {
		System.out.println("Warnings:");
		System.out.println("=========");
		for (Iterator i = ag.getWarnings().iterator(); i.hasNext();) {
			System.out.println("- " + i.next());
		}
		System.out.println();
	}

	FileWriter w = new FileWriter(ag.getAbstractionName() + ".java");
	w.write(code);
	w.flush();
	w.close();

	System.out.println(code);
}
}
