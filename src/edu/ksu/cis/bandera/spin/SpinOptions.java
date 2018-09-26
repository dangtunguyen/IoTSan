package edu.ksu.cis.bandera.spin;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999   Matthew Dwyer (dwyer@cis.ksu.edu)            *
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

import java.util.*; // *** robbyjo's modification
import edu.ksu.cis.bandera.checker.*;

import org.apache.log4j.Category;

/**
 * SpinOptions is used to configure the options to be used in invoking
 * the SPIN model checker on generated PROMELA systems.
 * <p>
 * Most user setable options available through the XSPIN interface
 * can be configured through this class.  Default values for those
 * options are as follows: 
 * <ul> 
 * <li> <tt>Safety</tt> : false
 * <li> <tt>AcceptanceCycles</tt> : true
 * <li> <tt>ApplyNeverClaim</tt> : true
 * <li> <tt>SearchMode</tt> : <tt>Exhaustive</tt><br>
 * 	[out of <tt>{Exhaustive, SuperTrace, HashCompact}</tt>]
 * <li> <tt>StopAtError</tt> : 1
 * <li> <tt>SaveAllTrails</tt> : true
 * <li> <tt>FindShortestTrail</tt> : false
 * <li> <tt>MemoryLimit</tt> : 128 (MBytes)
 * <li> <tt>SpaceEstimate</tt> : 500 (x10^3)
 * <li> <tt>SearchDepth</tt> : 10000
 * <li> <tt>POReduction</tt> : false
 * <li> <tt>Compression</tt> : false
 * </ul>
 * <p>
 * Some options do not make sense when used with Bandera generated
 * models.
 * <ul> 
 * <li> Bandera models will not contain "progress" state labels
 * <li> unreachable model code should not be reported to the user
 * <li> weak-fairness is not supported either 
 * </ul>
 * <p>
 * Some basic guidelines for setting SPIN options are:
 * <p>
 * If a temporal formula is checked then never claims should be applied, 
 * otherwise it is a deadlock check.  If the temporal formula is known
 * to be a safety property then <tt>Safety</tt> should be set, otherwise
 * <tt>AcceptanceCycles</tt> should be set.  
 * <p>
 * Some option settings are linked:
 * <ul>
 * <li> Setting <tt>Safety</tt> to true automatically 
 * sets <tt>AcceptanceCycles</tt> to be false, and vice-versa.
 * </ul>
 *
 * @author Matt Dwyer &lt;dwyer@cis.ksu.edu&gt;
 * @author Roby Joehanes &lt;robbyjo@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class SpinOptions extends CheckerOption {

    /* ********************************************************
     * This class needs some cleanup:
     * 1) Variable names should start with lower-case letter.
     * 2) getters/setters for each variable and get rid of public vars!
     * 3) public static final vars should be all-caps
     * 4) All vars should have javadocs
     * 5) All methods should have javadocs
     * 6) Why compileOptions and compileOptions2?  Can they be one method?
     * 7) -DNOREDUCE is thrown out.  Is that intentional?
     * 8) Is it inefficient to use a Vector with the StringTokenizer to parse the
     *    command line options in parsepaneOptions and parseCompileOptions?  Could
     *    it just use StringTokenizer?  Or maybe something better?
     ********************************************************** */


    /**
     * The log that we will be writing to.
     */
    private static final Category log = Category.getInstance(SpinOptions.class);

    boolean Safety = false;
    boolean AcceptanceCycles = true;
    boolean ApplyNeverClaim = true;
    public static final int Exhaustive = 0;
    public static final int SuperTrace = 1;
    public static final int HashCompact = 2;
    int SearchMode = Exhaustive;
    int StopAtError = 1;
    boolean SaveAllTrails = true;
    boolean FindShortestTrail = false;

    private boolean Assertions = false;

    // robbyjo's patch
    // Used only in conjunction with FindShortestTrail
    // If (FindShortestTrail) { if (isApproximate) option += " -I"; else option += " -i";
    boolean isApproximate = true; 
    int MemoryLimit = 128;
    int MemoryCount = 31;
    int VectorSize = 1024;
    int SpaceEstimate = 500;
    int SearchDepth = 10000;
    boolean POReduction = false;
    boolean Compression = false;
    boolean ResourceBounded = true;

    public SpinOptions() {
    }

    public SpinOptions(String opt) {
	parseOptions(opt);
    }

    /**
     * Generate the command line options for the compiler given the current state of the
     * SpinOptions.
     */
    public String compileOptions() {
	String co = compileOptions2();
	if (!ResourceBounded) co = co + " -DNORESOURCEBOUNDED";
	return co;
    }

    public String compileOptions2() {
	String co;
	switch (SearchMode) {
	case SuperTrace : 
	    co = "-DBITSTATE -DMEMCNT=" + MemoryCount + " -DVECTORSZ=" + VectorSize;
	    break;
	case HashCompact : 
	    co = "-DHC -DMEMCNT=" + MemoryCount + " -DVECTORSZ=" + VectorSize;
	    break;
	default : 
	    co = "-DMEMCNT=" + MemoryCount + " -DVECTORSZ=" + VectorSize;
	    break;
	}
	if (Safety) co = co + " -DSAFETY";
	if (!ApplyNeverClaim) co = co + " -DNOCLAIM";
	if (!POReduction) co = co + " -DNOREDUCE";
	if (Compression) co = co + " -DCOLLAPSE";
	if (FindShortestTrail) co += " -DREACH";
	return co;
    }

    int log2(int x) {
	int log = 0;
	for (int total = 1; total < x; log++) {
	    total = total * 2;
	}
	return log-1;
    }

    /**
     * A simple test to show the default options.
     */
    public static void main (String argv[]) {
	SpinOptions so = new SpinOptions();
	System.out.println("Default compile options :" + so.compileOptions());
	System.out.println("Default pan options :" + so.panOptions());
	(so = new SpinOptions()).setSearchMode(SuperTrace);
	System.out.println("Supertrace compile options :" + so.compileOptions());
	System.out.println("Supertrace pan options :" + so.panOptions());
	(so = new SpinOptions()).setSearchMode(HashCompact);
	System.out.println("Hash-compact compile options :" + so.compileOptions());
	System.out.println("Hash-compact pan options :" + so.panOptions());
	(so = new SpinOptions()).setSafety(true);
	System.out.println("Safety compile options :" + so.compileOptions());
	System.out.println("Safety pan options :" + so.panOptions());
	(so = new SpinOptions()).setMemoryLimit(1000);
	so.setSearchDepth(50000);
	so.setSpaceEstimate(2050);
	System.out.println("Limits compile options :" + so.compileOptions());
	System.out.println("Limits pan options :" + so.panOptions());
	(so = new SpinOptions()).setApplyNeverClaim(false);
	so.setPOReduction(true);
	so.setCompression(true);
	System.out.println("No never, po, compress compile options :" + so.compileOptions());
	System.out.println("No never, po, compress pan options :" + so.panOptions());
    }

    /**
     * Generate the command line options for pan given the current state of the SpinOptions.
     */
    public String panOptions() {

	/* replace the String with a StringBuffer to make it more efficient. -tcw */

	StringBuffer panOptions = new StringBuffer();

	panOptions.append("-n -m" + SearchDepth + " -w" + (10 + log2(SpaceEstimate)));

	if (AcceptanceCycles) {
	    panOptions.append(" -a");
	}

	if (StopAtError > 1) {
	    panOptions.append(" -c" + StopAtError);
	}

	if (SaveAllTrails) {
	    panOptions.append(" -e");
	}

	if (Assertions) {
	    panOptions.append(" -A");
	}

	if (FindShortestTrail) {
	    if (isApproximate) {
		panOptions.append(" -I");
	    }
	    else {
		panOptions.append(" -i");
	    }
	}

	return(panOptions.toString());
    }

    /**
     * This method will parse the compiler options as they would appear on the command line.
     *
     * @param String compileOptions The command line options that will be passed to the call to the compiler
     */
    public void parseCompileOptions(String compileOptions) {
	
	Vector vector = new Vector();
	StringTokenizer st = new StringTokenizer(compileOptions);
	while (st.hasMoreTokens()) {
	    vector.add(st.nextToken());
	}
	for (int i = 0; i < vector.size(); i++) {
	    String s = ((String) vector.elementAt(i));
	    if (s.equals("-DBITSTATE"))
		setSearchMode(SuperTrace);
	    else if (s.equals("-DHC"))
		setSearchMode(HashCompact);
	    else if (s.startsWith("-DVECTORSZ")) {
		String temp = s.substring(11);
		try {
		    setVectorSize(Integer.parseInt(temp));
		} catch (Exception e) {
		    log.warn("Vector size error in DVECTORSZ " + getVectorSize());
		}
	    } else if (s.startsWith("-DMEMCNT")) {
		String temp = s.substring(9);
		try {
		    setMemoryCount(Integer.parseInt(temp));
		} catch (Exception e) {
		    log.warn("Memory limit error in DMEMCNT " + getMemoryCount());
		}
	    } else if (s.startsWith("-DMEMLIM")) {
		String temp = s.substring(9);
		try {
		    setMemoryLimit(Integer.parseInt(temp));
		} catch (Exception e) {
		    log.warn("Memory limit error in DMEMLIM " + getMemoryLimit());
		}
	    } else if (s.equals("-DSAFETY")) {
		setAcceptanceCycles(false);
		setSafety(true);
	    } else if (s.equals("-DNOREDUCE")) {
	    } else if (s.equals("-DCOLLAPSE")) {
		setCompression(true);
	    } else if (s.equals("-DNORESOURCEBOUNDED")) {
		setResourceBounded(false);
	    } else if (s.equals("-DNOCLAIM")) {
		setApplyNeverClaim(false);
	    }
	}
    }

    /**
     * Parsing spin options -- Robbyjo's Modification
     */
    public void parseOptions(String opt) {
	Safety = false;
	AcceptanceCycles = true;
	ApplyNeverClaim = true;
	
	SearchMode = Exhaustive;
	
	StopAtError = 1;
	SaveAllTrails = true;
	FindShortestTrail = false;
	
	MemoryLimit = 128;
	MemoryCount = 31;
	VectorSize = 1024;
	SpaceEstimate = 500;
	SearchDepth = 10000;
	
	POReduction = false;
	Compression = false;

	ResourceBounded = true;
	
	StringTokenizer t = new StringTokenizer(opt, "+");
	if(t.countTokens() >= 2) {
	    String compileOptions = t.nextToken();
	    parseCompileOptions(compileOptions);

	    String panOptions = t.nextToken();
	    parsepaneOptions(panOptions);
	}
	else {
	    log.error("An error occured while parsing the options.  There were not enough tokens." +
			       "  Required: 2, Found: " + t.countTokens());
	}
    }

    /**
     * This method will parse the pan options as they would appear on the command line.
     *
     * @param String panOptions The command line options that will be passed to the call to pan
     */
    public void parsepaneOptions(String panOptions) {
	Vector vector = new Vector();
	StringTokenizer st = new StringTokenizer(panOptions);
	while(st.hasMoreTokens()){
	    vector.add(st.nextToken());
	}
	for(int i =0; i < vector.size(); i++){
	    String opt = (String) vector.elementAt(i);
	    if (opt.equals("-n")) {
		i++;
		opt = (String) vector.elementAt(i);
		if( i==vector.size() || !(opt.startsWith("-m"))) {
		    log.error("Your input should be -n -mSearchDepth");
		    
		}
		else{
		    String temp = opt.substring(2);
		    try{
			setSearchDepth(Integer.parseInt(temp));
		    }catch(Exception e){
			log.warn("Error search depth option: "+getSearchDepth());
		    }
		}
	    } else if (opt.equals("-a")) {
		setAcceptanceCycles(true);
	    } else if (opt.equals("-A")) {
		setAssertions(true);
	    } else if(opt.equals("-c")) {
		i++;
		opt = (String) vector.elementAt(i);
		if( i==vector.size()){
		    log.error("Your input should be -c StopAtErrors");
		    
		}
		else{
		    try{
			setStopAtError(Integer.parseInt(opt));
		    }catch(Exception e){
			log.error("Error " + getStopAtError());
		    }
		}
	    } else if (opt.equals("-e")) {
		setSaveAllTrails(true);
	    } else if (opt.equals("-I")) {
		setFindShortestTrail(true); isApproximate = true;
	    } else if (opt.equals("-i")) {
		setFindShortestTrail(true); isApproximate = false;
	    }
	}
    }

    public boolean getAcceptanceCycles() {
	return(AcceptanceCycles);
    }

    public void setAcceptanceCycles(boolean ac) { 
	AcceptanceCycles = ac;
	Safety = !ac; 
    }

    public void setApplyNeverClaim(boolean nc) {
	ApplyNeverClaim = nc;
    }

    public boolean getCompression() {
	return(Compression);
    }

    public void setCompression(boolean c) {
	Compression = c;
    }

    public boolean getFindShortestTrail() {
	return(FindShortestTrail);
    }

    public void setFindShortestTrail(boolean st) {
	FindShortestTrail = st;
    }

    public int getMemoryCount() {
	return(MemoryCount);
    }

    public void setMemoryCount(int c) {
	MemoryCount = c;
    }

    public int getMemoryLimit() {
	return(MemoryLimit);
    }

    public void setMemoryLimit(int l) {
	MemoryLimit = l;
    }

    public void setPOReduction(boolean po) {
	POReduction = po;
    }

    public boolean getResourceBounded() {
	return(ResourceBounded);
    }

    public void setResourceBounded(boolean b) {
	ResourceBounded = b;
    }

    public boolean getSafety() {
	return(Safety);
    }

    public void setSafety(boolean s) { 
	Safety = s; 
	AcceptanceCycles = !s;
	ApplyNeverClaim = !s;
    }
    
    public boolean getSaveAllTrails() {
	return(SaveAllTrails);
    }

    public void setSaveAllTrails(boolean at) {
	SaveAllTrails = at;
    }

    public int getSearchDepth() {
	return(SearchDepth);
    }
    
    public void setSearchDepth(int d) {
	SearchDepth = d;
    }
    
    public int getSearchMode() {
	return(SearchMode);
    }

    public void setSearchMode(int m) {
	SearchMode = m;
    }
    
    public int getSpaceEstimate() {
	return(SpaceEstimate);
    }

    public void setSpaceEstimate(int e) {
	SpaceEstimate = e;
    }

    public int getStopAtError() {
	return(StopAtError);
    }
    
    public void setStopAtError(int e) {
	StopAtError = 1;
    }
    
    public int getVectorSize() {
	return(VectorSize);
    }

    public void setVectorSize(int s) {
	VectorSize = s;
    }

    public boolean getAssertions() {
	return(Assertions);
    }

    public void setAssertions(boolean assertions) {
	Assertions = assertions;
    }
}
