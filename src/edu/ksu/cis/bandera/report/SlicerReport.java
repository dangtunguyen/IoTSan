package edu.ksu.cis.bandera.report;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001                                                *
 * Venkatesh Prasad Ranganath (rvprasad@cis.ksu.edu)                 *
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


import ca.mcgill.sable.soot.BuildAndStoreBody;
import ca.mcgill.sable.soot.ClassFile;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.StoredBody;
import ca.mcgill.sable.soot.jimple.Jimple;
import edu.ksu.cis.bandera.report.Report;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Comparator;

/**
 * SlicerReport.java
 *
 *
 * Created: Thu Feb 21 09:40:25 2002
 *
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Revision: 1.2 $ $Name:  $
 */

public class SlicerReport implements ISlicerReport {

	private static final Logger cat = Logger.getLogger(SlicerReport.class.getName());

	private final StringWriter sw = new StringWriter();

	private final PrintWriter pw = new PrintWriter(sw);

	private static final BuildAndStoreBody bodyexpr= new BuildAndStoreBody(Jimple.v(), new StoredBody(ClassFile.v()));

	private static final Comparator comparator = new Comparator() {
			public int compare(Object o1, Object o2) {
				String s1 = o1.toString();
				String s2 = o2.toString();
				return s1.compareTo(s2);
			}

			public boolean equals(Object o) {
				return super.equals(o);
			}
		};

	/**
	 *
	 * @return <description>
	 */
	public String getFilename() {
		return "slicer.report";
	}

	public void extractReportData(SootClass[] result) {
		pw.flush();
		sw.flush();

		List temp = Arrays.asList(result);
		Collections.sort(temp, comparator);

		for (Iterator i = temp.iterator(); i.hasNext();) {
			SootClass cls = (SootClass)i.next();
			StoredBody bodyexpr= new StoredBody(Jimple.v());
			cls.printTo(bodyexpr, pw);
			pw.println("\n-----------------------------------\n");
		} // end of for (int i = 0; i < result.length; i++)
	}

	/**
	 *
	 * @return <description>
	 */
	public String getTextRepresentation() {
		return sw.toString();
	}

}// BOFAReport
