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
import edu.ksu.cis.bandera.util.Preferences;
import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.specification.datastructure.*;

public class HTMLReportGenerator {
	private static Vector sessions = new Vector();
	private static Hashtable sessionSummaryTable = new Hashtable();
/**
 * 
 * @return boolean
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateBIRCSummary(String expectedPath, String resultPath, PrintWriter pw) {
	boolean isOk = true;
	String path1 = expectedPath + File.separator + "birc";
	String path2 = resultPath + File.separator + "birc";
	File file1 = new File(path1);
	File file2 = new File(path2);
	pw.println("<p><a name = \"BIRC\"><font size=\"5\"><strong>BIRC</strong></font></a></p>");
	pw.println("<ul>");
	isOk = generateFileSummary(expectedPath, resultPath, "report" + File.separator + "birc.report", "Report", pw) && isOk;
	{
		pw.println("<li><font size=\"3\">File(s):</font></li>");
		String[] files = file1.list();
		if (files == null) {
			pw.println("<ul>");
			pw.println("<li><font size=\"3\">Not found</font></li>");
			pw.println("</ul>");
		} else {
			pw.println("<ul>");
			for (int i = 0; i < files.length; i++) {
				isOk = generateFileSummary(expectedPath, resultPath, "birc" + File.separator + files[i], files[i], pw) && isOk;
			}
			pw.println("</ul>");
		}
	}
	pw.println("</ul>");
	return isOk;
}
/**
 * 
 * @return boolean
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateBOFASummary(String expectedPath, String resultPath, PrintWriter pw) {
	boolean isOk = true;
	String path1 = expectedPath + File.separator + "bofa";
	String path2 = resultPath + File.separator + "bofa";
	File file1 = new File(path1);
	File file2 = new File(path2);
	pw.println("<p><a name = \"BOFA\"><font size=\"5\"><strong>BOFA</strong></font></a></p>");
	pw.println("<ul>");
	isOk = generateFileSummary(expectedPath, resultPath, "report" + File.separator + "bofa.report", "Report", pw) && isOk;
	{
		pw.println("<li><font size=\"3\">File(s):</font></li>");
		String[] files = file1.list();
		if (files == null) {
			pw.println("<ul>");
			pw.println("<li><font size=\"3\">Not found</font></li>");
			pw.println("</ul>");
		} else {
			pw.println("<ul>");
			for (int i = 0; i < files.length; i++) {
				isOk = generateFileSummary(expectedPath, resultPath, "bofa" + File.separator + files[i], files[i], pw) && isOk;
			}
			pw.println("</ul>");
		}
	}
	pw.println("</ul>");
	return isOk;
}
/**
 * 
 * @return boolean
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateBSLSummary(String expectedPath, String resultPath, PrintWriter pw) {
	boolean isOk = true;
	String path1 = expectedPath + File.separator + "bsl";
	String path2 = resultPath + File.separator + "bsl";
	File file1 = new File(path1);
	File file2 = new File(path2);
	pw.println("<p><a name = \"BSL\"><font size=\"5\"><strong>BSL</strong></font></a></p>");
	pw.println("<ul>");
	isOk = generateFileSummary(expectedPath, resultPath, "report" + File.separator + "bsl.report", "Report", pw) && isOk;
	{
		pw.println("<li><font size=\"3\">File(s):</font></li>");
		String[] files = file1.list();
		if (files == null) {
			pw.println("<ul>");
			pw.println("<li><font size=\"3\">Not found</font></li>");
			pw.println("</ul>");
		} else {
			pw.println("<ul>");
			for (int i = 0; i < files.length; i++) {
				isOk = generateFileSummary(expectedPath, resultPath, "bsl" + File.separator + files[i], files[i], pw) && isOk;
			}
			pw.println("</ul>");
		}
	}
	pw.println("</ul>");
	return isOk;
}
/**
 * 
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param filename java.lang.String
 * @param name java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateFileSummary(String expectedPath, String resultPath, String filename, String name, PrintWriter pw) {
	boolean result = true;
	String expectedFilePath = expectedPath + File.separator + filename;
	String resultFilePath = resultPath + File.separator + filename;
	if (new File(expectedFilePath).exists()) {
		if (new File(resultFilePath).exists()) {
			pw.print("<li>");
			pw.print("<a href=\"" + filename + "\"><font size=\"3\"><em>" + name + "</em></font></a> ");
			if (isEqual(expectedFilePath, resultFilePath)) {
				pw.print("<font size=\"3\"> (Ok)</font>");
			} else {
				result = false;
				pw.print("<font size=\"3\"> (Failed: <a href=\".." + File.separator + expectedFilePath + "\"><em>expected result</em></a>)</font>");
			}
			pw.println("</li>");
		} else {
			result = false;
			pw.println("<li><font size=\"3\">" + name + " was not available for the current result " + resultFilePath + "</font></li>");
		}
	} else {
		if (new File(resultFilePath).exists()) {
			result = false;
			pw.println("<li><font size=\"3\">" + name + " was not available for the expected result " + expectedFilePath + "</font></li>");
		} else {
			pw.println("<li><font size=\"3\">" + name + " was not available for both the expected and the current result</font></li>");
		}
	}
	return result;
}
/**
 * 
 * @return boolean
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateInlinerSummary(String expectedPath, String resultPath, PrintWriter pw) {
	boolean isOk = true;
	String path1 = expectedPath + File.separator + "inlined";
	String path2 = resultPath + File.separator + "inlined";
	File file1 = new File(path1);
	File file2 = new File(path2);
	pw.println("<p><a name = \"Inliner\"><font size=\"5\"><strong>Inliner</strong></font></a></p>");
	pw.println("<ul>");
	isOk = generateFileSummary(expectedPath, resultPath, "report" + File.separator + "inliner.report", "Report", pw) && isOk;
	{
		pw.println("<li><font size=\"3\">File(s):</font></li>");
		String[] files = file1.list();
		if (files == null) {
			pw.println("<ul>");
			pw.println("<li><font size=\"3\">Not found</font></li>");
			pw.println("</ul>");
		} else {
			pw.println("<ul>");
			for (int i = 0; i < files.length; i++) {
				isOk = generateFileSummary(expectedPath, resultPath, "inlined" + File.separator + files[i], files[i], pw) && isOk;
			}
			pw.println("</ul>");
		}
	}
	pw.println("</ul>");
	return isOk;
}
/**
 * 
 * @return boolean
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateJJJCSummary(String expectedPath, String resultPath, PrintWriter pw) {
	boolean isOk = true;
	String path1 = expectedPath + File.separator + "original";
	String path2 = resultPath + File.separator + "original";
	File file1 = new File(path1);
	File file2 = new File(path2);
	pw.println("<p><a name = \"JJJC\"><font size=\"5\"><strong>JJJC</strong></font></a></p>");
	pw.println("<ul>");
	isOk = generateFileSummary(expectedPath, resultPath, "report" + File.separator + "jjjc.report", "Report", pw) && isOk;
	{
		pw.println("<li><font size=\"3\">File(s):</font></li>");
		String[] files = file1.list();
		if (files == null) {
			pw.println("<ul>");
			pw.println("<li><font size=\"3\">Not found</font></li>");
			pw.println("</ul>");
		} else {
			pw.println("<ul>");
			for (int i = 0; i < files.length; i++) {
				isOk = generateFileSummary(expectedPath, resultPath, "original" + File.separator + files[i], files[i], pw) && isOk;
			}
			pw.println("</ul>");
		}
	}
	pw.println("</ul>");
	return isOk;
}
/**
 * 
 * @param p edu.ksu.cis.bandera.specification.datastructure.Property
 * @param pw java.io.PrintWriter
 */
private static void generatePropertySummary(Property p, PrintWriter pw) {
	TemporalLogicProperty tlp = p.getActivatedTemporalLogicProperty();
	TreeSet aps = p.getActivatedAssertionProperties();
	pw.println("<p><font size=\"5\"><strong>Property</strong></font></p>");
	pw.println("<ul>");
	pw.println("<li><font size=\"3\">Temporal Property:</font></li>");
	pw.println("<ul>");
	if (tlp == null) {
		pw.println("<li><font size=\"3\">None</font></li>");
	} else {
		pw.println("<li><font face=\"Courier New\" size=\"2\">" + tlp.expand() + "</font></li>");
	}
	pw.println("</ul>");
	pw.println("<li>Assertion Properties:</li>");
	pw.println("<ul>");
	if (aps.size() == 0) {
		pw.println("<li><font size=\"3\">None</font></li>");
	} else {
		for (Iterator i = aps.iterator(); i.hasNext();) {
			pw.println("<li><font face=\"Courier New\" size=\"3\">" + ((AssertionProperty) i.next()).getStringRepresentation() + "</font></li>");
		}
	}
	pw.println("</ul>");
	pw.println("</ul>");
}
/**
 * 
 * @param s edu.ksu.cis.bandera.bui.session.datastructure.Session
 * @param pw java.io.PrintWriter
 */
private static void generateSessionInfo(Session s, PrintWriter pw) {
	pw.println("<p><font size=\"5\"><strong>Session</strong></font></p>");
	pw.println("<pre>" + s.toString() + "</pre>");
	pw.println("<p><font size=\"5\"><strong>Files</strong></font></p>");
	pw.println("<ul>");
	pw.println("<li><a href=\".." + File.separator + s.getMainClassFile().toString() + "\"><font size=\"3\"><em>Main java file</em></font></a></li>");
	pw.println("</ul>");
}
/**
 * 
 * @param htmlPath java.lang.String
 * @param session edu.ksu.cis.bandera.bui.session
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 */
public static void generateSessionSummary(String htmlPath, Session session, String expectedPath, String resultPath) {
	sessions.add(session);
	Vector failedComponents = new Vector();
	try {
		String title = "Report for Session " + session.getName();
		PrintWriter pw = new PrintWriter(new FileWriter(htmlPath));
		pw.println("<html>");
		pw.println("<head>");
		pw.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		pw.println("<title>" + title + "</title>");
		pw.println("</head>");
		pw.print("<body bgcolor=\"" + Preferences.getReportHTMLBackgroundColor() + "\"");
		if ("".equals(Preferences.getReportHTMLBackgroundImage())) {
			pw.println(">");
		} else {
			pw.println(" background = \"" + Preferences.getReportHTMLBackgroundImage() + "\">");
		}
		pw.println("<blockquote>");
		pw.println("<p><font size=\"7\"><strong>" + title + "</strong></font></p>");
		pw.println("<p><font size=\"4\"><strong>" + new Date() + "</strong></font></p>");
		pw.println("<p>&nbsp;</p>");
		generateSessionInfo(session, pw);
		generatePropertySummary(edu.ksu.cis.bandera.bui.BUI.property, pw);
		if (!generateJJJCSummary(expectedPath, resultPath, pw)) {
			failedComponents.add("JJJC");
		}
		if (!generateBSLSummary(expectedPath, resultPath, pw)) {
			failedComponents.add("BSL");
		}
		if (!generateBOFASummary(expectedPath, resultPath, pw)) {
			failedComponents.add("BOFA");
		}
		if (!generateSlicerSummary(expectedPath, resultPath, pw)) {
			failedComponents.add("Slicer");
		}
		if (!generateSLABSSummary(expectedPath, resultPath, pw)) {
			failedComponents.add("SLABS");
		}
		if (!generateInlinerSummary(expectedPath, resultPath, pw)) {
			failedComponents.add("Inliner");
		}
		if (!generateBIRCSummary(expectedPath, resultPath, pw)) {
			failedComponents.add("BIRC");
		}
		pw.println("<hr>");
		if ("".equals(Preferences.getReportHTMLBackImage())) {
			pw.println("<a href=\".." + File.separator + "index.html\"><font size=\"3\"><em>Back</em></font></a>");
		} else {
			pw.println("<a href=\".." + File.separator + "index.html\"><img src=\"" + Preferences.getReportHTMLBackImage() + "\" border=\"0\"></a>");
		}
		pw.println("</blockquote>");
		pw.println("</body>");
		pw.println("</html>");
		pw.close();
	} catch (Exception e) {
		failedComponents.add("Error writing " + htmlPath);
	}
	sessionSummaryTable.put(session, failedComponents);
}
/**
 * 
 * @return boolean
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateSLABSSummary(String expectedPath, String resultPath, PrintWriter pw) {
	boolean isOk = true;
	String path1 = expectedPath + File.separator + "abstracted";
	String path2 = resultPath + File.separator + "abstracted";
	File file1 = new File(path1);
	File file2 = new File(path2);
	pw.println("<p><a name = \"SLABS\"><font size=\"5\"><strong>SLABS</strong></font></a></p>");
	pw.println("<ul>");
	isOk = generateFileSummary(expectedPath, resultPath, "report" + File.separator + "slabs.report", "Report", pw) && isOk;
	{
		pw.println("<li><font size=\"3\">File(s):</font></li>");
		String[] files = file1.list();
		if (files == null) {
			pw.println("<ul>");
			pw.println("<li><font size=\"3\">Not found</font></li>");
			pw.println("</ul>");
		} else {
			pw.println("<ul>");
			for (int i = 0; i < files.length; i++) {
				isOk = generateFileSummary(expectedPath, resultPath, "abstracted" + File.separator + files[i], files[i], pw) && isOk;
			}
			pw.println("</ul>");
		}
	}
	pw.println("</ul>");
	return isOk;
}
/**
 * 
 * @return boolean
 * @param expectedPath java.lang.String
 * @param resultPath java.lang.String
 * @param pw java.io.PrintWriter
 */
private static boolean generateSlicerSummary(String expectedPath, String resultPath, PrintWriter pw) {
	boolean isOk = true;
	String path1 = expectedPath + File.separator + "sliced";
	String path2 = resultPath + File.separator + "sliced";
	File file1 = new File(path1);
	File file2 = new File(path2);
	pw.println("<p><a name = \"Slicer\"><font size=\"5\"><strong>Slicer</strong></font></a></p>");
	pw.println("<ul>");
	isOk = generateFileSummary(expectedPath, resultPath, "report" + File.separator + "slicer.report", "Report", pw) && isOk;
	{
		pw.println("<li><font size=\"3\">File(s):</font></li>");
		String[] files = file1.list();
		if (files == null) {
			pw.println("<ul>");
			pw.println("<li><font size=\"3\">Not found</font></li>");
			pw.println("</ul>");
		} else {
			pw.println("<ul>");
			for (int i = 0; i < files.length; i++) {
				isOk = generateFileSummary(expectedPath, resultPath, "sliced" + File.separator + files[i], files[i], pw) && isOk;
			}
			pw.println("</ul>");
		}
	}
	pw.println("</ul>");
	return isOk;
}
/**
 * 
 * @param path java.lang.String
 */
public static void generateSummary(String filename, String path) {
	try {
		String title = "Report for " + filename;
		PrintWriter pw = new PrintWriter(new FileWriter(path));
		pw.println("<html>");
		pw.println("<head>");
		pw.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
		pw.println("<title>" + title + "</title>");
		pw.println("</head>");
		pw.print("<body bgcolor=\"" + Preferences.getReportHTMLBackgroundColor() + "\"");
		if ("".equals(Preferences.getReportHTMLBackgroundImage())) {
			pw.println(">");
		} else {
			pw.println(" background = \"" + Preferences.getReportHTMLBackgroundImage() + "\">");
		}
		pw.println("<blockquote>");
		pw.println("<p><font size=\"7\"><strong>Report for <a href = \"" + filename + "\"><em>" + filename + "</em></a></strong></font></p>");
		pw.println("<p><font size=\"4\"><strong>" + new Date() + "</strong></font></p>");
		pw.println("<p>&nbsp;</p>");
		pw.println("<ul>");
		for (Iterator i = sessions.iterator(); i.hasNext();) {
			Session s = (Session) i.next();
			Vector v = (Vector) sessionSummaryTable.get(s);
			if (v.size() > 0) {
				String componentName = (String) v.firstElement();
				String component = "<a href=\"temp$" + s.getName() + File.separator + "index.html#" + componentName + "\"><font size=\"3\"><em>" + componentName + "<em></font></a>";
				pw.println("<li><font size=\"3\"><a href=\"temp$" + s.getName() + File.separator + "index.html\"><em>" + s.getName() + "</em></a> (Failed: " + component + ")</font></li>");
			} else {
				pw.println("<li><font size=\"3\"><a href=\"temp$" + s.getName() + File.separator + "index.html\"><em>" + s.getName() + "</em></a> (Ok)</font></li>");
			}
		}
		pw.println("</ul>");
		pw.println("<hr>");
		if ("".equals(Preferences.getReportHTMLBackImage())) {
			pw.println("<a href=\".." + File.separator + "index.html\"><font size=\"3\"><em>Back</em></font></a>");
		} else {
			pw.println("<a href=\".." + File.separator + "index.html\"><img src=\"" + Preferences.getReportHTMLBackImage() + "\" border=\"0\"></a>");
		}
		pw.println("</blockquote>");
		pw.println("</body>");
		pw.println("</html>");
		pw.close();
	} catch (Exception e) {
		System.out.println("Error writing " + path);
	}
}
/**
 * 
 * @return boolean
 * @param path1 java.lang.String
 * @param path2 java.lang.String
 */
private static boolean isEqual(String path1, String path2) {
	return new File(path1).length() == new File(path2).length();
}
}
