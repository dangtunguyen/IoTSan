package edu.ksu.cis.bandera.bui.session.datastructure;

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
import edu.ksu.cis.bandera.jjjc.util.*;
import edu.ksu.cis.bandera.bui.BUI;
//import edu.ksu.cis.bandera.bui.JPFOption;
import edu.ksu.cis.bandera.util.DefaultValues;

public class Session implements Comparable {
	private String name;
	private String description = "none";
	private String filename;
	private String classpath = ".";
	private String[] includedPackagesOrTypes = new String[0];
	private String outputName = "default";
	private String workingDirectory = System.getProperty("user.dir");
	private boolean doSlicer = false;
	private boolean doSLABS = false;
	private boolean doChecker = false;
	private String specFilename;
	private String activeTemporal;
	private HashSet activeAssertions = new HashSet();
	private String absFilename;
	private String bircOption;
	private Hashtable resources = new Hashtable();
	private boolean useJPF = false;
	private String jpfOptions;
	private boolean useSPIN = false;
	private String spinOptions;
	private boolean useSMV = false;
	private String smvOptions;
	private boolean useDSPIN = false;
	private String dspinOptions;
	private int birMinIntRange = DefaultValues.birMinIntRange;
	private int birMaxIntRange = DefaultValues.birMaxIntRange;
	private int birMaxArrayLen = DefaultValues.birMaxArrayLen;
	private int birMaxInstances = DefaultValues.birMaxInstances;
	private int birMaxThreads = DefaultValues.birMaxThreads;
/**
 * 
 * @param name java.lang.String
 */
public Session(String name) {
	setName(name);
}
/**
 * 
 * @param name java.lang.String
 */
public void addActiveAssertion(String name) {
	activeAssertions.add(name);
}
/**
 * 
 * @return java.lang.Object
 */
public Object clone() {
	Session result = new Session(name + "_clone");
	result.absFilename = absFilename;
	result.activeAssertions = (HashSet) activeAssertions.clone();
	result.activeTemporal = activeTemporal;
	result.bircOption = bircOption;
	result.birMaxArrayLen = birMaxArrayLen;
	result.birMaxInstances = birMaxInstances;
	result.birMaxThreads = birMaxThreads;
	result.birMinIntRange = birMinIntRange;
	result.birMaxIntRange = birMaxIntRange;
	result.classpath = classpath;
	result.description = description;
	result.doChecker = doChecker;
	result.doSLABS = doSLABS;
	result.doSlicer = doSlicer;
	result.dspinOptions = dspinOptions;
	result.filename = filename;
	result.includedPackagesOrTypes = new String[includedPackagesOrTypes.length];
	for (int i = 0; i < includedPackagesOrTypes.length; i++)
	{
		result.includedPackagesOrTypes[i] = includedPackagesOrTypes[i];
	}
	result.jpfOptions = jpfOptions;
	result.outputName = outputName;
	result.resources = (Hashtable) resources.clone();
	result.smvOptions = smvOptions;
	result.specFilename = specFilename;
	result.spinOptions = spinOptions;
	result.useDSPIN = useDSPIN;
	result.useJPF = useJPF;
	result.useSMV = useSMV;
	result.useSPIN = useSPIN;
	result.workingDirectory = workingDirectory;
	return result;
}
/**
 * 
 * @return int
 * @param object java.lang.Object
 */
public int compareTo(Object object) {
	return name.compareTo(((Session) object).getName());
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getAbsFilename() {
	return absFilename;
}
/**
 * 
 * @return java.util.HashSet
 */
public java.util.HashSet getActiveAssertions() {
	return activeAssertions;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getActiveTemporal() {
	return activeTemporal;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getBIRCOption() {
	return bircOption;
}
/**
 * 
 * @return int
 */
public int getBirMaxArrayLen() {
	return birMaxArrayLen;
}
/**
 * 
 * @return int
 */
public int getBirMaxInstances() {
	return birMaxInstances;
}
/**
 * 
 * @return int
 */
public int getBirMaxThreads() {
	return birMaxThreads;
}
/**
 * 
 * @return int
 */
public int getBirMaxIntRange() {
	return birMaxIntRange;
}
/**
 * 
 * @return int
 */
public int getBirMinIntRange() {
	return birMinIntRange;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getClasspath() {
	return classpath;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getDspinOptions() {
	return dspinOptions;
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
 * @return java.lang.String[]
 */
public java.lang.String[] getIncludedPackagesOrTypes() {
	return includedPackagesOrTypes;
}
/**
 * 
 * @return java.lang.String
 */
public String getInfo() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("Session name: " + name + "\n\n");
	buffer.append("Description: " + description + "\n\n");
	buffer.append("Java main source code: " + (filename == null ? "none" : filename) + "\n");
	buffer.append("Classpath: " + classpath + "\n");
	buffer.append("Included package or type: ");
	buffer.append((includedPackagesOrTypes.length == 0) ? "none\n" : "\n");
	for (int i = 0; i < includedPackagesOrTypes.length; i++) {
		buffer.append(" - " + includedPackagesOrTypes[i] + "\n");
	}
	buffer.append("Enabled components: ");
	String text = doSlicer ? "Slicer, " : "";
	text = doSLABS ? text + "SLABS, " : text;
	text = doChecker ? text + "Checker, " : text;
	if ("".equals(text)) {
		buffer.append("none\n");
	} else {
		buffer.append(text.substring(0, text.length() - 2) + "\n");
	}
	buffer.append("Specification file: " + (specFilename == null ? "none" : specFilename) + "\n");
	buffer.append("Temporal property specification: " + (activeTemporal == null ? "none" : activeTemporal) + "\n");
	if (activeAssertions.size() == 0) {
		buffer.append("Assertion property specification: none\n");
	} else {
		Iterator i = activeAssertions.iterator();
		buffer.append("Assertion property specification: " + i.next());
		while (i.hasNext()) {
			buffer.append("," + i.next());
		}
		buffer.append("\n");
	}
	buffer.append("Abstraction file: " + (absFilename == null ? "none" : absFilename) +"\n");
	buffer.append("\nOutput name: " + outputName + "\n");
	buffer.append("\nWorking directory: " + workingDirectory + "\n");
	return buffer.toString();
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getJpfOptions() {
	return jpfOptions;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getOutputName() {
	return outputName;
}
/**
 * 
 * @return java.lang.String
 * @param filename java.lang.String
 */
private String getPath(String filename) {
	boolean f = false;
	try {
		File f1 = new File(filename).getParentFile();
		File f2 = new File(".".equals(workingDirectory) ? System.getProperty("user.dir") : workingDirectory);
		f = f1.getCanonicalPath().equals(f2.getCanonicalPath());
	} catch (Exception e) {
	}
	if (f)
		return Util.encodeString(new File(filename).getName());
	else
		return Util.encodeString(filename == null ? "" : filename);
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getSmvOptions() {
	return smvOptions;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getSpecFilename() {
	return specFilename;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getSpinOptions() {
	return spinOptions;
}
/**
 * 
 * @return java.lang.String
 */
public String getStringRepresentation() {
	String line = System.getProperty("line.separator");
	StringBuffer buffer = new StringBuffer("session " + name + " {" + line);
	buffer.append("  source = \"" + getPath(filename) + "\"" + line);
	buffer.append("  classpath = \"" + Util.encodeString(classpath.replace(File.pathSeparatorChar, '+')) + "\"" + line);
	buffer.append("  included = \"");
	int length = includedPackagesOrTypes.length;
	for (int i = 0; i < length - 1; i++) {
		buffer.append(Util.encodeString(includedPackagesOrTypes[i]) + "+");
	}
	if (length > 0)
		buffer.append(Util.encodeString(includedPackagesOrTypes[length - 1]));
	buffer.append("\"" + line);
	buffer.append("  output = \"" + Util.encodeString(outputName) + "\"" + line);
	buffer.append("  directory = \"" + Util.encodeString(workingDirectory) + "\"" + line);
	buffer.append("  description = \"" + Util.encodeString(description) + "\"" + line);
	buffer.append("  components = \"");
	String text = doSlicer ? "Slicer+" : "";
	text = doSLABS ? text + "SLABS+" : text;
	text = doChecker ? text + "Checker+" : text;
	if ("".equals(text)) {
		buffer.append("\"" + line);
	} else {
		buffer.append(text.substring(0, text.length() - 1) + "\"" + line);
	}
	buffer.append("  specification = \"" + getPath(specFilename) + "\"" + line);
	buffer.append("  temporal = \"" + ((activeTemporal == null) ? "" : activeTemporal) + "\"" + line);
	if (activeAssertions.size() == 0) {
		buffer.append("  assertion = \"\"" + line);
	} else {
		Iterator i = activeAssertions.iterator();
		buffer.append("  assertion = \"" + i.next());
		while (i.hasNext()) {
			buffer.append("+" + i.next());
		}
		buffer.append("\"" + line);
	}
  //saveOptions();
	buffer.append("  abstraction = \"" + getPath(absFilename) + "\"" + line);
	//buffer.append("  slabs = \"" + ((abpsOption == null) ? "" : abpsOption) + "\"" + line);
	buffer.append("  birc = \"" + ((bircOption == null) ? "" : bircOption) + "\"" + line);
	if (useSPIN) {
		buffer.append("  spin = \"" + spinOptions + "\"" + line);
	}
	if (useJPF) {
		buffer.append("  jpf = \"" + jpfOptions + "\"" + line);
	}
	if (useDSPIN) {
		buffer.append("  dspin = \"" + dspinOptions + "\"" + line);
	}
	if (useSMV) {
		buffer.append("  smv = \"" + smvOptions + "\"" + line);
	}
	for (Enumeration e = resources.keys(); e.hasMoreElements();) {
		String key = (String) e.nextElement();
		String value = (String) resources.get(key);
		buffer.append("  " + key + " = \"" + Util.encodeString(value) + "\"" + line);
	}
	if (!useJPF)
	buffer.append("   birbounds =\"" + birMinIntRange+", "+ birMaxIntRange+", " +
	              birMaxArrayLen+", " + birMaxInstances + ", " + birMaxThreads + "\"");
	buffer.append("}" + line);
	return buffer.toString();
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getWorkingDirectory() {
	return workingDirectory;
}
/**
 * 
 * @return boolean
 */
public boolean isDoChecker() {
	return doChecker;
}
/**
 * 
 * @return boolean
 */
public boolean isDoSLABS() {
	return doSLABS;
}
/**
 * 
 * @return boolean
 */
public boolean isDoSlicer() {
	return doSlicer;
}
/**
 * 
 * @return boolean
 */
public boolean isUseDSPIN() {
	return useDSPIN;
}
/**
 * 
 * @return boolean
 */
public boolean isUseJPF() {
	return useJPF;
}
/**
 * 
 * @return boolean
 */
public boolean isUseSMV() {
	return useSMV;
}
/**
 * 
 * @return boolean
 */
public boolean isUseSPIN() {
	return useSPIN;
}
/**
 * 
 * @param key java.lang.String
 * @param value java.lang.String
 */
public void putResource(String key, String value) {
	resources.put(key, value);
}
/**
 * 
 * @param name java.lang.String
 */
public void removeActiveAssertion(String name) {
	activeAssertions.remove(name);
}
/**
 * 
 */
public void saveOptions() {
    /*
	spinOptions = BUI.bui.spinOption.spinOptions.compileOptions()
			+ "+" + BUI.bui.spinOption.spinOptions.panOptions();
	jpfOptions = JPFOption.getOptions();
    */
    
	if (BUI.typeGUI.getFile() != null) {
		absFilename = BUI.typeGUI.getFile().getAbsolutePath();
	}
}
/**
 * 
 * @param newAbsFilename java.lang.String
 */
public void setAbsFilename(java.lang.String newAbsFilename) {
	absFilename = newAbsFilename;
}
/**
 * 
 * @param newActiveAssertions java.util.HashSet
 */
public void setActiveAssertions(java.util.HashSet newActiveAssertions) {
	activeAssertions = newActiveAssertions;
}
/**
 * 
 * @param newActiveTemporal java.lang.String
 */
public void setActiveTemporal(java.lang.String newActiveTemporal) {
	activeTemporal = newActiveTemporal;
}
/**
 * 
 * @param newBircOption java.lang.String
 */
public void setBIRCOption(java.lang.String newBircOption) {
	bircOption = newBircOption;
}
/**
 * 
 * @param newBirMaxArrayLen int
 */
public void setBirMaxArrayLen(int newBirMaxArrayLen) {
	birMaxArrayLen = newBirMaxArrayLen;
}
/**
 * 
 * @param newBirMaxInstances int
 */
public void setBirMaxInstances(int newBirMaxInstances) {
	birMaxInstances = newBirMaxInstances;
}
/**
 * 
 * @param newBirMaxThreads int
 */
public void setBirMaxThreads(int newBirMaxThreads) {
	birMaxThreads = newBirMaxThreads;
}
/**
 * 
 * @param newBirMaxIntRange int
 */
public void setBirMaxIntRange(int newBirMaxIntRange) {
	birMaxIntRange = newBirMaxIntRange;
}
/**
 * 
 * @param newBirMinIntRange int
 */
public void setBirMinIntRange(int newBirMinIntRange) {
	birMinIntRange = newBirMinIntRange;
}
/**
 * 
 * @param newClasspath java.lang.String
 */
public void setClasspath(java.lang.String newClasspath) {
	classpath = newClasspath;
}
/**
 * 
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * 
 * @param newDoChecker boolean
 */
public void setDoChecker(boolean newDoChecker) {
	doChecker = newDoChecker;
}
/**
 * 
 * @param newDoBABS boolean
 */
public void setDoSLABS(boolean newDoSLABS) {
	doSLABS = newDoSLABS;
}
/**
 * 
 * @param newDoSlicer boolean
 */
public void setDoSlicer(boolean newDoSlicer) {
	doSlicer = newDoSlicer;
}
/**
 * 
 * @param newDspinOptions java.lang.String
 */
public void setDspinOptions(java.lang.String newDspinOptions) {
	dspinOptions = newDspinOptions;
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
 * @param newIncludedPackagesOrTypes java.lang.String[]
 */
public void setIncludedPackagesOrTypes(java.lang.String[] newIncludedPackagesOrTypes) {
	includedPackagesOrTypes = newIncludedPackagesOrTypes;
}
/**
 * 
 * @param newJpfOptions java.lang.String
 */
public void setJpfOptions(java.lang.String newJpfOptions) {
	jpfOptions = newJpfOptions;
}
/**
 * 
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * 
 * @param newOutputName java.lang.String
 */
public void setOutputName(java.lang.String newOutputName) {
	outputName = newOutputName;
}
/**
 * 
 * @param newSmvOptions java.lang.String
 */
public void setSmvOptions(java.lang.String newSmvOptions) {
	smvOptions = newSmvOptions;
}
/**
 * 
 * @param newSpecFilename java.lang.String
 */
public void setSpecFilename(java.lang.String newSpecFilename) {
	specFilename = newSpecFilename;
}
/**
 * 
 * @param newSpinOptions java.lang.String
 */
public void setSpinOptions(java.lang.String newSpinOptions) {
	spinOptions = newSpinOptions;
}
/**
 * 
 * @param newUseDSPIN boolean
 */
public void setUseDSPIN(boolean newUseDSPIN) {
	useDSPIN = newUseDSPIN;
}
/**
 * 
 * @param newUseJPF boolean
 */
public void setUseJPF(boolean newUseJPF) {
	useJPF = newUseJPF;
}
/**
 * 
 * @param newUseSMV boolean
 */
public void setUseSMV(boolean newUseSMV) {
	useSMV = newUseSMV;
}
/**
 * 
 * @param newUseSPIN boolean
 */
public void setUseSPIN(boolean newUseSPIN) {
	useSPIN = newUseSPIN;
}
/**
 * 
 * @param newWorkingDirectory java.lang.String
 */
public void setWorkingDirectory(java.lang.String newWorkingDirectory) {
	workingDirectory = newWorkingDirectory;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return name;
}
}
