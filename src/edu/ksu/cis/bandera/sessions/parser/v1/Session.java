package edu.ksu.cis.bandera.sessions.parser.v1;

import java.io.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.util.*;

import edu.ksu.cis.bandera.util.DefaultValues;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

/**
 * The Session object provides a storage facility for configuration
 * information for a single run of the Bandera system.
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class Session implements Comparable {
    private String name;
    private String description;
    private String filename;
    private String classpath;
    private String[] includedPackagesOrTypes;
    private String outputName;
    private String workingDirectory;

    private boolean doSlicer;
    private boolean doSLABS;
    private boolean doChecker;

    private String specFilename;
    private String activeTemporal;
    private HashSet activeAssertions;
    private String absFilename;

    private boolean useJPF;
    private boolean useDSPIN;
    private boolean useSPIN;
    private boolean useSMV;

    private String jpfOptions;
    private String spinOptions;
    private String smvOptions;
    private String dspinOptions;

    private int birMinIntRange;
    private int birMaxIntRange;
    private int birMaxArrayLen;
    private int birMaxInstances;
    private int birMaxThreadInstances;
    private String bircOption;

    private Map resourceMap;

    /**
     * 
     * @param name java.lang.String
     */
    public Session(String name) {
	setName(name);

	activeAssertions = new HashSet();
	includedPackagesOrTypes = new String[0];
	resourceMap = new HashMap();

	birMinIntRange = DefaultValues.birMinIntRange;
	birMaxIntRange = DefaultValues.birMaxIntRange;
	birMaxArrayLen = DefaultValues.birMaxArrayLen;
	birMaxInstances = DefaultValues.birMaxInstances;
	birMaxThreadInstances = DefaultValues.birMaxThreads;

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
	result.birMaxThreadInstances = birMaxThreadInstances;
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
	for (int i = 0; i < includedPackagesOrTypes.length; i++) {
	    result.includedPackagesOrTypes[i] = includedPackagesOrTypes[i];
	}
	Iterator i = resourceMap.keySet().iterator();
	while(i.hasNext()) {
	    String key = (String)i.next();
	    String value = (String)resourceMap.get(key);
	    result.putResource(key, value);
	}
	result.jpfOptions = jpfOptions;
	result.outputName = outputName;
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
	if(absFilename == null) {
	    return("");
	}
	else {
	    return(absFilename);
	}
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
    public int getBirMaxThreadInstances() {
	return(birMaxThreadInstances);
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
     */
    public java.lang.String getSmvOptions() {
	return smvOptions;
    }
    /**
     * 
     * @return java.lang.String
     */
    public java.lang.String getSpecFilename() {
	if(specFilename == null) {
	    return("");
	}
	else {
	    return(specFilename);
	}
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
    public java.lang.String getWorkingDirectory() {
	return workingDirectory;
    }

    /**
     * Get a resource from our local store of miscellaneous resources.
     *
     * @param String key
     * @return String The value associated with the key or null if the key cannot
     *         be found in the local store.
     */
    public String getResource(String key) {
	if(resourceMap == null) {
	    return(null);
	}
        return((String)resourceMap.get(key));
    }

    public Set getResourceKeySet() {
	if(resourceMap == null) {
	    return(null);
	}
	return(resourceMap.keySet());
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
     * @param name java.lang.String
     */
    public void removeActiveAssertion(String name) {
	activeAssertions.remove(name);
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
     * @param newBirMaxThreadInstances int
     */
    public void setBirMaxThreadInstances(int newBirMaxThreadInstances) {
	birMaxThreadInstances = newBirMaxThreadInstances;
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

    public void putResource(String key, String value) {
	if(resourceMap == null) {
	    resourceMap = new HashMap();
	}
	resourceMap.put(key, value);
    }

    /**
     * 
     * @return java.lang.String
     */
    public String toString() {
	return name;
    }
}
