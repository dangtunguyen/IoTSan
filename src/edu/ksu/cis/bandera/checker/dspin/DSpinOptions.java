package edu.ksu.cis.bandera.checker.dspin;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.util.Preferences;

import java.util.Vector;
import java.util.StringTokenizer;


import org.apache.log4j.Category;

/**
 * DSpinOptions is used to configure the options to be used in invoking
 * the DSPIN model checker on generated PROMELA systems.
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
public class DSpinOptions implements Options {

    /* ********************************************************
     * This class needs some cleanup:
     * 1) getters/setters for each variable and get rid of public vars!
     * 2) All vars should have javadocs
     * 3) All methods should have javadocs
     * 4) -DNOREDUCE is thrown out.  Is that intentional?
     ********************************************************** */


    /**
     * The log that we will be writing to.
     */
    private static final Category log = Category.getInstance(DSpinOptions.class);

    public static final int EXHAUSTIVE_SEARCH_MODE = 0;
    public static final int SUPER_TRACE_SEARCH_MODE = 1;
    public static final int HASH_COMPACT_SEARCH_MODE = 2;
    
    private boolean safety = false;
    private boolean acceptanceCycles = true;
    private boolean applyNeverClaim = true;
    private int searchMode = EXHAUSTIVE_SEARCH_MODE;
    private int stopAtError = 1;
    private boolean saveAllTrails = true;
    private boolean findShortestTrail = false;
    private boolean assertions = false;

    // robbyjo's patch
    // Used only in conjunction with FindShortestTrail
    // If (FindShortestTrail) { if (isApproximate) option += " -I"; else option += " -i";
    private boolean isApproximate = true; 
    private int memoryLimit = 128;
    private int memoryCount = 31;
    private int vectorSize = 1024;
    private int spaceEstimate = 500;
    private int searchDepth = 10000;
    private boolean partialOrderReduction = false;
    private boolean compression = false;

    public DSpinOptions() {
    }
    public boolean getAcceptanceCycles() {
	return(acceptanceCycles);
    }
    public boolean getAssertions() {
	return(assertions);
    }
 /**
     * Get the String that represents these options that can be used on the
     * command line for the checker.
     *
     * @return String A String of command line options.
     */
public java.lang.String getCommandLineOptions() throws java.lang.Exception {

	String compilerCommandLineOptions = getCompilerCommandLineOptions();
	String panCommandLineOptions = getPanCommandLineOptions();
	return(compilerCommandLineOptions + "+" + panCommandLineOptions);
	
}
 /**
     * Get the String array that represents these options that can be used on the
     * command line for the checker.
     *
     * @return String[] An array of Strings of command line options.
     */
public java.lang.String[] getCommandLineOptionsArray() throws java.lang.Exception {

	String[] compilerCommandLineOptionsArray = getCompilerCommandLineOptionsArray();
	String[] panCommandLineOptionsArray = getPanCommandLineOptionsArray();
	String[] commandLineOptionsArray = new String[compilerCommandLineOptionsArray.length + 1 + panCommandLineOptionsArray.length];
	System.arraycopy(compilerCommandLineOptionsArray, 0, commandLineOptionsArray, 0, compilerCommandLineOptionsArray.length);
	commandLineOptionsArray[compilerCommandLineOptionsArray.length] = "+";
	System.arraycopy(panCommandLineOptionsArray, 0, commandLineOptionsArray, compilerCommandLineOptionsArray.length + 1, panCommandLineOptionsArray.length);
	return(commandLineOptionsArray);
	
}
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will get
 * the command line options for the compiler using the current state to generate
 * the String.
 *
 * @return java.lang.String
 */
public String getCompilerCommandLineOptions() {
	StringBuffer sb = new StringBuffer();
	
	switch (searchMode) {
	case SUPER_TRACE_SEARCH_MODE : 
	    sb.append("-DBITSTATE ");
	    break;
	case HASH_COMPACT_SEARCH_MODE : 
	    sb.append("-DHC ");
	    break;
	case EXHAUSTIVE_SEARCH_MODE :
		// do nothing
		break;
	default :
		// do nothing
	}
	sb.append("-DMEMCNT=" + memoryCount + " -DVECTORSZ=" + vectorSize + " -DMEMLIM=" + memoryLimit + " ");
	
	if (safety) sb.append("-DSAFETY ");
	if (!applyNeverClaim) sb.append("-DNOCLAIM ");
	if (!partialOrderReduction) sb.append("-DNOREDUCE ");
	if (compression) sb.append("-DCOLLAPSE ");
	if (findShortestTrail) sb.append("-DREACH ");

	return(sb.toString().trim());
}
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will get
 * the command line options for the compiler using the current state to generate
 * the String array.
 *
 * @return java.lang.String[]
 */
public String[] getCompilerCommandLineOptionsArray() {

	java.util.List l = new java.util.ArrayList();
	
	switch (searchMode) {
	case SUPER_TRACE_SEARCH_MODE : 
	    l.add("-DBITSTATE");
	    break;
	case HASH_COMPACT_SEARCH_MODE : 
	    l.add("-DHC");
	    break;
	case EXHAUSTIVE_SEARCH_MODE :
		// do nothing
		break;
	default :
		// do nothing
	}
	l.add("-DMEMCNT=" + memoryCount);
	l.add("-DVECTORSZ=" + vectorSize);
	l.add("-DMEMLIM=" + memoryLimit);

	if (safety) l.add("-DSAFETY");
	if (!applyNeverClaim) l.add("-DNOCLAIM");
	if (!partialOrderReduction) l.add("-DNOREDUCE");
	if (compression) l.add("-DCOLLAPSE");
	if (findShortestTrail) l.add("-DREACH");

	String[] sa = new String[l.size()];
	for(int i = 0; i < l.size(); i++) {
		sa[i] = l.get(i).toString();
	}

	return(sa);
}
    public boolean getCompression() {
	return(compression);
    }
    public boolean getFindShortestTrail() {
	return(findShortestTrail);
    }
    public int getMemoryCount() {
	return(memoryCount);
    }
    public int getMemoryLimit() {
	return(memoryLimit);
    }
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will get
 * the command line options for the application (pan) using the current state to generate
 * the String.
 *
 * @return java.lang.String
 */
public String getPanCommandLineOptions() {
	StringBuffer panOptions = new StringBuffer();

	panOptions.append("-n -m" + searchDepth + " -w" + (10 + log2(spaceEstimate)) + " ");

	if (acceptanceCycles) {
	    panOptions.append("-a ");
	}

	if (stopAtError > 1) {
	    panOptions.append("-c" + stopAtError + " ");
	}

	if (saveAllTrails) {
	    panOptions.append("-e ");
	}

	if (assertions) {
	    panOptions.append("-A ");
	}

	if (findShortestTrail) {
	    if (isApproximate) {
		panOptions.append("-I ");
	    }
	    else {
		panOptions.append("-i ");
	    }
	}

	return(panOptions.toString().trim());
}
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will get
 * the command line options for the application (pan) using the current state to generate
 * the String array.
 *
 * @return java.lang.String[]
 */
public String[] getPanCommandLineOptionsArray() {

	java.util.List l = new java.util.ArrayList();
	
	l.add("-n");
	l.add("-m" + searchDepth);
	l.add("-w" + (10 + log2(spaceEstimate)));

	if (acceptanceCycles) {
	    l.add("-a");
	}

	if (stopAtError > 1) {
	    l.add("-c" + stopAtError);
	}

	if (saveAllTrails) {
	    l.add("-e");
	}

	if (assertions) {
	    l.add("-A");
	}

	if (findShortestTrail) {
	    if (isApproximate) {
		l.add("-I");
	    }
	    else {
		l.add("-i");
	    }
	}

	String[] sa = new String[l.size()];
	for(int i = 0; i < l.size(); i++) {
		sa[i] = l.get(i).toString();
	}

	return(sa);
}
/**
 * Get the flag for using partial order reduction on the model.
 *
 * @return boolean
 */
public boolean getPartialOrderReduction() {
	return(partialOrderReduction);
}
    public boolean getSafety() {
	return(safety);
    }
    public boolean getSaveAllTrails() {
	return(saveAllTrails);
    }
    public int getSearchDepth() {
	return(searchDepth);
    }
    public int getSearchMode() {
	return(searchMode);
    }
    public int getSpaceEstimate() {
	return(spaceEstimate);
    }
    public int getStopAtError() {
	return(stopAtError);
    }
    public int getVectorSize() {
	return(vectorSize);
    }
 /**
     * Initialize these command line options using the default values.
     */
public void init() {
	safety = false;
	acceptanceCycles = true;
	applyNeverClaim = true;
	
	searchMode = EXHAUSTIVE_SEARCH_MODE;
	
	stopAtError = 1;
	saveAllTrails = true;
	findShortestTrail = false;
	
	memoryLimit = 128;
	memoryCount = 31;
	vectorSize = 1024;
	spaceEstimate = 500;
	searchDepth = 10000;
	
	partialOrderReduction = false;
	compression = false;

}
 /**
     * Initialize these command line options using the array of Strings.
     *
     * @param String[] commandLineOptions An array Strings of command line options
     *        (as it might be seen on the command line for the checker).
     */
public void init(java.lang.String[] commandLineOptionsArray) {
	init();

	if((commandLineOptionsArray != null) && (commandLineOptionsArray.length > 0)) {

		// find where the two sets of options are separated at (denoted by a +)
		int pos = -1;
		for(int i = 0; i < commandLineOptionsArray.length; i++) {
			if((commandLineOptionsArray[i] != null) && (commandLineOptionsArray[i].equals("+"))) {
				pos = i;
				break;
			}
		}

		// break out the two components and init the system using them	
		int compilerOptionsCount = pos - 1;
		int panOptionsCount = commandLineOptionsArray.length - (pos + 1);
		String[] compilerCommandLineOptionsArray = new String[compilerOptionsCount];
		String[] panCommandLineOptionsArray = new String[panOptionsCount];
		System.arraycopy(commandLineOptionsArray, 0, compilerCommandLineOptionsArray, 0, compilerOptionsCount);
		System.arraycopy(commandLineOptionsArray, pos + 1, panCommandLineOptionsArray, 0, panOptionsCount);
		parseCompilerCommandLineOptions(compilerCommandLineOptionsArray);
		parsePanCommandLineOptions(panCommandLineOptionsArray);
	}
}
/**
    * Initialize these command line options using the String.
    *
    * @param String commandLineOptions A single String of command line options
    *        (as it might be seen on the command line for the checker).
    */
public void init(java.lang.String commandLineOptions) {

    // init using the default values first
    init();

    // parse the String given into it's two components: compiler and pan options
    if ((commandLineOptions != null) && (commandLineOptions.length() > 0)) {
        StringTokenizer st = new StringTokenizer(commandLineOptions, "+");
        if (st.countTokens() == 2) {
            String compilerCommandLineOptions = st.nextToken();
            String panCommandLineOptions = st.nextToken();
            parseCompilerCommandLineOptions(compilerCommandLineOptions);
            parsePanCommandLineOptions(panCommandLineOptions);
        }
    }

}
    int log2(int x) {
	int log = 0;
	for (int total = 1; total < x; log++) {
	    total = total * 2;
	}
	return log-1;
    }
/**
 * A simple test to show the command line options when different settings are used.
 */
public static void main(String args[]) {
    DSpinOptions so = new DSpinOptions();
    
    System.out.println("Default compile options :" + so.getCompilerCommandLineOptions());
    System.out.println("Default pan options :" + so.getPanCommandLineOptions());

    so = new DSpinOptions();
    so.setSearchMode(SUPER_TRACE_SEARCH_MODE);
    System.out.println("Supertrace compile options :" + so.getCompilerCommandLineOptions());
    System.out.println("Supertrace pan options :" + so.getPanCommandLineOptions());

    so = new DSpinOptions();
    so.setSearchMode(HASH_COMPACT_SEARCH_MODE);
    System.out.println("Hash-compact compile options :" + so.getCompilerCommandLineOptions());
    System.out.println("Hash-compact pan options :" + so.getPanCommandLineOptions());

    so = new DSpinOptions();
    so.setSafety(true);
    System.out.println("Safety compile options :" + so.getCompilerCommandLineOptions());
    System.out.println("Safety pan options :" + so.getPanCommandLineOptions());

    so = new DSpinOptions();
    so.setMemoryLimit(1000);
    so.setSearchDepth(50000);
    so.setSpaceEstimate(2050);
    System.out.println("Limits compile options :" + so.getCompilerCommandLineOptions());
    System.out.println("Limits pan options :" + so.getPanCommandLineOptions());

    so = new DSpinOptions();
    so.setApplyNeverClaim(false);
    so.setPartialOrderReduction(true);
    so.setCompression(true);
    System.out.println("No never, po, compress compile options :"
            + so.getCompilerCommandLineOptions());
    System.out.println("No never, po, compress pan options :" + so.getPanCommandLineOptions());
}
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will take
 * the command line options for the compiler, parse them, and set the state
 * of the DSpinOptions to match.
 * 
 * @param commandLineOptionsArray java.lang.String[]
 */
public void parseCompilerCommandLineOptions(String[] commandLineOptionsArray) {

	if((commandLineOptionsArray != null) && (commandLineOptionsArray.length > 0)) {
        for(int i = 0; i < commandLineOptionsArray.length; i++) {
            String s = commandLineOptionsArray[i];

            if (s.equals("-DBITSTATE")) {
                setSearchMode(SUPER_TRACE_SEARCH_MODE);
                continue;
            }
            
            if (s.equals("-DHC")) {
                setSearchMode(HASH_COMPACT_SEARCH_MODE);
                continue;
            }
            
            if (s.startsWith("-DVECTORSZ")) {
                String temp = s.substring(11);
                try {
                    setVectorSize(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.warn("An error occured while converting the vector size (DVECTORSZ) specified, " + temp + ", into an int.  Using the default value instead.");
                }
                continue;
            }
            
            if (s.startsWith("-DMEMCNT")) {
                String temp = s.substring(9);
                try {
                    setMemoryCount(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.warn("An error occured while converting the memory count (DMEMCNT) specified, " + temp + ", into an int.  Using the default value instead.");
                }
                continue;
            }
            
            if (s.startsWith("-DMEMLIM")) {
                String temp = s.substring(9);
                try {
                    setMemoryLimit(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.warn("An error occured while converting the memory limit (DMEMLIM) specified, " + temp + ", into an int.  Using the default value instead.");
                }
                continue;
            }
            
            if (s.equals("-DSAFETY")) {
                setAcceptanceCycles(false);
                setSafety(true);
                continue;
            }
            
            if (s.equals("-DNOREDUCE")) {
	            // should we be ignoring this? -tcw
	            continue;
            }
            
            if (s.equals("-DCOLLAPSE")) {
                setCompression(true);
                continue;
            }
            
            if (s.equals("-DNOCLAIM")) {
                setApplyNeverClaim(false);
                continue;
            }
        }
	}
}
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will take
 * the command line options for the compiler, parse them, and set the state
 * of the SpinOptions to match.
 * 
 * @param commandLineOptions java.lang.String
 */
public void parseCompilerCommandLineOptions(String commandLineOptions) {

    if ((commandLineOptions != null) && (commandLineOptions.length() > 0)) {
        StringTokenizer st = new StringTokenizer(commandLineOptions);
        while (st.hasMoreTokens()) {
            String s = st.nextToken();

            if (s.equals("-DBITSTATE")) {
                setSearchMode(SUPER_TRACE_SEARCH_MODE);
                continue;
            }
            
            if (s.equals("-DHC")) {
                setSearchMode(HASH_COMPACT_SEARCH_MODE);
                continue;
            }
            
            if (s.startsWith("-DVECTORSZ")) {
                String temp = s.substring(11);
                try {
                    setVectorSize(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.warn("An error occured while converting the vector size (DVECTORSZ) specified, " + temp + ", into an int.  Using the default value instead.");
                }
                continue;
            }
            
            if (s.startsWith("-DMEMCNT")) {
                String temp = s.substring(9);
                try {
                    setMemoryCount(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.warn("An error occured while converting the memory count (DMEMCNT) specified, " + temp + ", into an int.  Using the default value instead.");
                }
                continue;
            }
            
            if (s.startsWith("-DMEMLIM")) {
                String temp = s.substring(9);
                try {
                    setMemoryLimit(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.warn("An error occured while converting the memory limit (DMEMLIM) specified, " + temp + ", into an int.  Using the default value instead.");
                }
                continue;
            }
            
            if (s.equals("-DSAFETY")) {
                setAcceptanceCycles(false);
                setSafety(true);
                continue;
            }
            
            if (s.equals("-DNOREDUCE")) {
	            // should we be ignoring this? -tcw
	            continue;
            }
            
            if (s.equals("-DCOLLAPSE")) {
                setCompression(true);
                continue;
            }
            
            if (s.equals("-DNOCLAIM")) {
                setApplyNeverClaim(false);
                continue;
            }
        }
    }
}
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will take
 * the command line options for the application (pan), parse them, and set the state
 * of the SpinOptions to match.
 *
 * @param commandLineOptionsArray java.lang.String[]
 */
public void parsePanCommandLineOptions(String[] commandLineOptionsArray) {

	if((commandLineOptionsArray != null) && (commandLineOptionsArray.length > 0)) {
		for(int i = 0; i < commandLineOptionsArray.length; i++) {
            String opt = commandLineOptionsArray[i];

            if (opt.equals("-a")) {
                setAcceptanceCycles(true);
                continue;
            }

            if (opt.equals("-A")) {
                setAssertions(true);
                continue;
            }

            if (opt.equals("-e")) {
                setSaveAllTrails(true);
                continue;
            }

            if (opt.equals("-I")) {
                setFindShortestTrail(true);
                isApproximate = true;
                continue;
            }

            if (opt.equals("-i")) {
                setFindShortestTrail(true);
                isApproximate = false;
                continue;
            }

            if (opt.equals("-c")) {
                if (i == commandLineOptionsArray.length) {
                    log.error("An error occured while parsing the number of errors to stop after (-c).  There was nothing after -c.");
                    continue;
                }
                opt = commandLineOptionsArray[++i];
                try {
                   setStopAtError(Integer.parseInt(opt));
                }
                catch (Exception e) {
                    log.error("An error occured while parsing the number of errors to stop after (-c) specified, "
                            + opt
                            + ", into an int.  Using the default instead.");
                }
                continue;
            }

            if (opt.equals("-n")) {
                if (i == commandLineOptionsArray.length) {
                    log.error("When -n is specified, -mSearchDepth is assumed to follow."
                            + "  In the input given, there was nothing after the -n.");
                    continue;
                }
                opt = commandLineOptionsArray[++i];
                if (!(opt.startsWith("-m"))) {
                    log.error("When -n is specified, -mSearchDepth is assumed to follow."
                            + "  In the input given, the next options was not -m.");
                    continue;
                }
                String temp = opt.substring(2);
                try {
                    setSearchDepth(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.error("An error occured while parsing the search depth (-m) specified, "
                            + temp
                            + ", into an int.  Using the default value instead.");
                }
                continue;
            }
        }
	}
}
/**
 * Spin options are made up of two components: those passed to the compiler
 * and those passed to the compiled application (or pan).  This method will take
 * the command line options for the application (pan), parse them, and set the state
 * of the SpinOptions to match.
 *
 * @param commandLineOptions java.lang.String
 */
public void parsePanCommandLineOptions(String commandLineOptions) {

    if ((commandLineOptions != null) && (commandLineOptions.length() > 0)) {
        StringTokenizer st = new StringTokenizer(commandLineOptions);
        while (st.hasMoreTokens()) {
            String opt = st.nextToken();

            if (opt.equals("-a")) {
                setAcceptanceCycles(true);
                continue;
            }

            if (opt.equals("-A")) {
                setAssertions(true);
                continue;
            }

            if (opt.equals("-e")) {
                setSaveAllTrails(true);
                continue;
            }

            if (opt.equals("-I")) {
                setFindShortestTrail(true);
                isApproximate = true;
                continue;
            }

            if (opt.equals("-i")) {
                setFindShortestTrail(true);
                isApproximate = false;
                continue;
            }

            if (opt.equals("-c")) {
                if (!st.hasMoreTokens()) {
                    log.error("An error occured while parsing the number of errors to stop after (-c).  There was nothing after -c.");
                    continue;
                }
                opt = st.nextToken();
                try {
                   setStopAtError(Integer.parseInt(opt));
                }
                catch (Exception e) {
                    log.error("An error occured while parsing the number of errors to stop after (-c) specified, "
                            + opt
                            + ", into an int.  Using the default instead.");
                }
                continue;
            }

            if (opt.equals("-n")) {
                if (!st.hasMoreTokens()) {
                    log.error("When -n is specified, -mSearchDepth is assumed to follow."
                            + "  In the input given, there was nothing after the -n.");
                    continue;
                }
                opt = st.nextToken();
                if (!(opt.startsWith("-m"))) {
                    log.error("When -n is specified, -mSearchDepth is assumed to follow."
                            + "  In the input given, the next options was not -m.");
                    continue;
                }
                String temp = opt.substring(2);
                try {
                    setSearchDepth(Integer.parseInt(temp));
                }
                catch (Exception e) {
                    log.error("An error occured while parsing the search depth (-m) specified, "
                            + temp
                            + ", into an int.  Using the default value instead.");
                }
                continue;
            }
        }
    }
}
    public void setAcceptanceCycles(boolean ac) { 
	acceptanceCycles = ac;
	safety = !ac; 
    }
    public void setApplyNeverClaim(boolean nc) {
	applyNeverClaim = nc;
    }
    public boolean getApplyNeverClaim() {
	return(applyNeverClaim);
    }
    public void setAssertions(boolean assertions) {
	this.assertions = assertions;
    }
    public void setCompression(boolean c) {
	compression = c;
    }
    public void setFindShortestTrail(boolean st) {
	findShortestTrail = st;
    }
    public void setMemoryCount(int c) {
	memoryCount = c;
    }
    public void setMemoryLimit(int l) {
	memoryLimit = l;
    }
    public void setPartialOrderReduction(boolean po) {
	partialOrderReduction = po;
    }
    public void setSafety(boolean s) { 
	safety = s; 
	acceptanceCycles = !s;
	applyNeverClaim = !s;
    }
    public void setSaveAllTrails(boolean at) {
	saveAllTrails = at;
    }
    public void setSearchDepth(int d) {
	searchDepth = d;
    }
    public void setSearchMode(int m) {
	searchMode = m;
    }
    public void setSpaceEstimate(int e) {
	spaceEstimate = e;
    }
    public void setStopAtError(int e) {
	stopAtError = 1;
    }
    public void setVectorSize(int s) {
	vectorSize = s;
    }

    /* **************************************************************** */
    /* Added to get this to compile with Matt's new SpinTrans.java -tcw */
    private boolean chooseFreeSearch = false;
    public boolean getChooseFreeSearch() {
	return(chooseFreeSearch);
    }
    public void setChooseFreeSearch(boolean b) {
	chooseFreeSearch = b;
    }
    /* **************************************************************** */

}
