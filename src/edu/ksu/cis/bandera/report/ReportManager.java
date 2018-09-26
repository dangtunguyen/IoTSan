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
import java.util.*;

public class ReportManager {
	private Hashtable reports = new Hashtable();
	private static boolean generateJJJCReport; 
	private static boolean generateBSLReport; 
	private static boolean generateBOFAReport; 
	private static boolean generateSlicerReport; 
	private static boolean generateSLABSReport; 
	private static boolean generateDecompilerReport; 
	private static boolean generateBIRCReport; 
/**
 * adds a report r and associated it with the key
 * @param key java.lang.String
 * @param r edu.cis.ksu.bandera.report.Report
 */
public void addReport(String key, Report r) {
	reports.put(key, r);
}
/**
 * gets the hashtable of keys and reports
 * @return java.util.Hashtable
 */
public Hashtable getFilteredReportTable() {
	Hashtable filteredReport = new Hashtable();
	for (Enumeration e = reports.keys(); e.hasMoreElements();) {
		String key = (String) e.nextElement();
		Report r = (Report) reports.get(key);
		if ((r instanceof IJJJCReport && generateJJJCReport)
			  || (r instanceof IBSLReport && generateBSLReport)
			  || (r instanceof IBOFAReport && generateBOFAReport)
			  || (r instanceof ISlicerReport && generateSlicerReport)
			  || (r instanceof ISLABSReport && generateSLABSReport)
			  || (r instanceof IDecompilerReport && generateDecompilerReport)
			  || (r instanceof IBIRCReport && generateBIRCReport)) {
			filteredReport.put(key, r);
		}
	}
	
	return filteredReport;
}
/**
 * gets a report associated with key
 * @return edu.ksu.cis.bandera.report.Report
 * @param key java.lang.String
 */
public Report getReport(String key) {
	return (Report) reports.get(key);
}
/**
 * 
 * @return boolean
 */
public static boolean isGenerateBIRCReport() {
	return generateBIRCReport;
}
/**
 * 
 * @return boolean
 */
public static boolean isGenerateBOFAReport() {
	return generateBOFAReport;
}
/**
 * 
 * @return boolean
 */
public static boolean isGenerateBSLReport() {
	return generateBSLReport;
}
/**
 * 
 * @return boolean
 */
public static boolean isGenerateDecompilerReport() {
	return generateDecompilerReport;
}
/**
 * 
 * @return boolean
 */
public static boolean isGenerateJJJCReport() {
	return generateJJJCReport;
}
/**
 * 
 * @return boolean
 */
public static boolean isGenerateSLABSReport() {
	return generateSLABSReport;
}
/**
 * 
 * @return boolean
 */
public static boolean isGenerateSlicerReport() {
	return generateSlicerReport;
}
/**
 * 
 * @param newGenerateBIRCReport boolean
 */
public static void setGenerateBIRCReport(boolean newGenerateBIRCReport) {
	generateBIRCReport = newGenerateBIRCReport;
}
/**
 * 
 * @param newGenerateBOFAReport boolean
 */
public static void setGenerateBOFAReport(boolean newGenerateBOFAReport) {
	generateBOFAReport = newGenerateBOFAReport;
}
/**
 * 
 * @param newGenerateBSLReport boolean
 */
public static void setGenerateBSLReport(boolean newGenerateBSLReport) {
	generateBSLReport = newGenerateBSLReport;
}
/**
 * 
 * @param newGenerateDecompilerReport boolean
 */
public static void setGenerateDecompilerReport(boolean newGenerateDecompilerReport) {
	generateDecompilerReport = newGenerateDecompilerReport;
}
/**
 * 
 * @param newGenerateJJJCReport boolean
 */
public static void setGenerateJJJCReport(boolean newGenerateJJJCReport) {
	generateJJJCReport = newGenerateJJJCReport;
}
/**
 * 
 * @param newGenerateSLABSReport boolean
 */
public static void setGenerateSLABSReport(boolean newGenerateSLABSReport) {
	generateSLABSReport = newGenerateSLABSReport;
}
/**
 * 
 * @param newGenerateSlicerReport boolean
 */
public static void setGenerateSlicerReport(boolean newGenerateSlicerReport) {
	generateSlicerReport = newGenerateSlicerReport;
}
}
