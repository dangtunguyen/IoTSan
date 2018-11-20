package edu.ksu.cis.bandera.abstraction;

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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.abstraction.gui.*;
import edu.ksu.cis.bandera.abstraction.options.*;
public class Main {
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) throws Exception {
	boolean gui = false;
	int idx = 0;
	if ("--gui".equals(args[idx])) {
		gui = true;
		idx++;
	}
	if (args.length < 2 + idx) {
		System.out.println("Usage: java edu.ksu.cis.bandera.abstraction.Main [--gui | <abs-file>] <class-name>+ ");
		return;
	}
	String abstractionFilename = args[0];
	SootClassManager scm = new SootClassManager();
	PrintWriter pw = new PrintWriter(System.out);
	Vector classes = new Vector();
	for (int i = 1; i < args.length; i++) {
		SootClass sc = scm.getClass(args[i]);
		sc.resolveIfNecessary();
		sc.printTo(new BuildAndStoreBody(Jimple.v(), new StoredBody(ClassFile.v())), pw);
		classes.add(sc);
	}
	pw.flush();
	if (gui) {
		TypeGUI typeGUI = new TypeGUI();
		typeGUI.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		typeGUI.setClasses(classes);
		typeGUI.setVisible(true);
	} else {
		System.out.println("Type Inference");
		System.out.println("==============");
		Hashtable options = OptionsSaverLoader.load(scm, new FileReader(abstractionFilename));
		if (options.get("WARNINGS") != null) {
			System.out.println("Warnings");
			System.out.println("--------");
			for (Iterator i = ((List) options.get("WARNINGS")).iterator(); i.hasNext();) {
			System.out.println(i.next());
			} 
			System.out.println();
		}
		System.out.println(new edu.ksu.cis.bandera.abstraction.typeinference.TypeInference().type(scm, options));
	}
}
}
