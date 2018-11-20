package edu.ksu.cis.bandera.report;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001   Robby (robby@cis.ksu.edu)                    *
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
import edu.ksu.cis.bandera.abstraction.typeinference.*;
public class SLABSReport implements ISLABSReport {
	private String typeInfo;
/**
 * getFilename method comment.
 */
public String getFilename() {
	return "slabs.report";
}
/**
 * getTextRepresentation method comment.
 */
public String getTextRepresentation() {
	return typeInfo;
}
/**
 * 
 * @param t edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 */
public void setTypeTable(TypeTable t) {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println("SLABS Report");
	pw.println("============");
	pw.println("");
	pw.println("");
	pw.println("");

	pw.println("Type Info");
	pw.println("---------");
	pw.println("");
	
	Hashtable h = edu.ksu.cis.bandera.jjjc.CompilationManager.getCompiledClasses();

	TreeSet classNames = new TreeSet();
	for (Enumeration e = h.keys(); e.hasMoreElements();) {
		classNames.add(e.nextElement());
	}

	for (Iterator i = classNames.iterator(); i.hasNext();) {
		String className = (String) i.next();
		pw.println("class " + className + " {");

		SootClass sc = (SootClass) h.get(className);

		TreeSet fieldNames = new TreeSet();
		Object[] sootFields = sc.getFields().toArray();
		for (int j = 0; j < sootFields.length; j++) {
			 fieldNames.add(((SootField) sootFields[j]).getName());
		}

		for (Iterator j = fieldNames.iterator(); j.hasNext();) {
			String fieldName = (String) j.next();
			SootField sf = sc.getField(fieldName);
			pw.println("  " + sf.getName() + " : " + t.get(sf));
		}

		TreeSet methodSignatures = new TreeSet();
		Hashtable methodTable = new Hashtable();
		Object[] sootMethods = sc.getMethods().toArray();
		for (int j = 0; j < sootMethods.length; j++) {
			SootMethod sm = (SootMethod) sootMethods[j];
			String methodSignature = sm.toString();
			methodSignatures.add(methodSignature);
			methodTable.put(methodSignature, sm);	
		}

		for (Iterator j = methodSignatures.iterator(); j.hasNext();) {
			String methodSignature = (String) j.next();

			pw.println("  " + methodSignature + " {");
				
			SootMethod sm = (SootMethod) methodTable.get(methodSignature);
			JimpleBody jb = (JimpleBody) sm.getBody(Jimple.v());

			TreeSet localNames = new TreeSet();
			Object[] locals = jb.getLocals().toArray();

			for (int k = 0; k < locals.length; k++) {
				localNames.add(((Local) locals[k]).getName());
			}

			for (Iterator k = localNames.iterator(); k.hasNext();) {
				String localName = (String) k.next();
				Local l = jb.getLocal(localName);
				pw.println("    " + localName + " : " + t.get(l));
			}
			
			pw.println("  }");
		}
			
		pw.println("}");
		pw.println("");
	}
	
	typeInfo = sw.toString();
}
}
