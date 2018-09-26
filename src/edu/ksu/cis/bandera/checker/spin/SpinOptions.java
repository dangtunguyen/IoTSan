package edu.ksu.cis.bandera.checker.spin;

import edu.ksu.cis.bandera.checker.Options;

import java.util.Vector;
import java.util.StringTokenizer;

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
 * <li> <tt>SpaceEstimate</tt> : 18 (2^N hashtable entries)
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
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:22 $
 */
public class SpinOptions implements Options {

    /* ********************************************************
     * This class needs some cleanup:
     * 1) All vars should have javadocs
     * 2) All methods should have javadocs
     * 3) -DNOREDUCE is thrown out.  Is that intentional?
     * 4) Move all default values into a single method.  Usually a constructor
     *    or possible an init method.  Or maybe just define them as public static
     *    final constants.  There are just too many places these are defined.
     ********************************************************** */


    /**
     * The log that we will be writing to.
     */
    private static final Category log = Category.getInstance(SpinOptions.class);

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
    //private int memoryCount = 31;
    private int vectorSize = 1024;
    private int spaceEstimate = 18;
    private int searchDepth = 10000;
    private boolean partialOrderReduction = false;
    private boolean compression = false;
    private boolean resourceBounded = true;
    private boolean chooseFreeSearch = false;

    public SpinOptions() {
	init();
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
    
	String commandLineOptions = compilerCommandLineOptions + "+" + panCommandLineOptions;

	return(commandLineOptions);
	
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
	//sb.append("-DMEMCNT=" + memoryCount + " -DVECTORSZ=" + vectorSize + " -DMEMLIM=" + memoryLimit + " ");
	sb.append(" -DVECTORSZ=" + vectorSize + " -DMEMLIM=" + memoryLimit + " ");
	
	if (safety) sb.append("-DSAFETY ");
	if (!applyNeverClaim) sb.append("-DNOCLAIM ");
	if (!partialOrderReduction) sb.append("-DNOREDUCE ");
	if (compression) sb.append("-DCOLLAPSE ");
	if (findShortestTrail) sb.append("-DREACH ");
	if (!resourceBounded) sb.append("-DNORESOURCEBOUNDED ");

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
	//l.add("-DMEMCNT=" + memoryCount);
	l.add("-DVECTORSZ=" + vectorSize);
	l.add("-DMEMLIM=" + memoryLimit);

	if (safety) l.add("-DSAFETY");
	if (!applyNeverClaim) l.add("-DNOCLAIM");
	if (!partialOrderReduction) l.add("-DNOREDUCE");
	if (compression) l.add("-DCOLLAPSE");
	if (findShortestTrail) l.add("-DREACH");
	if (!resourceBounded) l.add("-DNORESOURCEBOUNDED");

	String[] sa = new String[l.size()];
	for(int i = 0; i < l.size(); i++) {
	    sa[i] = l.get(i).toString();
	}

	return(sa);
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

	//panOptions.append("-n -m" + searchDepth + " -w" + (10 + log2(spaceEstimate)) + " ");
	panOptions.append("-n -m" + searchDepth + " -w" + spaceEstimate + " ");

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

	log.debug("Getting the Pan command line options array ...");

	java.util.List l = new java.util.ArrayList();
	
	l.add("-n");
	l.add("-m" + searchDepth);
	//l.add("-w" + (10 + log2(spaceEstimate)));
	l.add("-w" + spaceEstimate);

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
	//memoryCount = 31;
	vectorSize = 1024;
	spaceEstimate = 18;
	searchDepth = 10000;
	
	partialOrderReduction = false;
	compression = false;

	resourceBounded = true;
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
	else {
	    log.error("Error parsing the command line options array.  It was null or had no substance.");
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
		log.debug("compilerCommandLineOptions = " + compilerCommandLineOptions);

		String panCommandLineOptions = st.nextToken();
		log.debug("panCommandLineOptions = " + panCommandLineOptions);

		parseCompilerCommandLineOptions(compilerCommandLineOptions);
		parsePanCommandLineOptions(panCommandLineOptions);
	    }
	    else {
		log.error("Error parsing the command line options.  There was no separator (+) in the String.");
	    }
	}
	else {
	    log.error("Error parsing the command line options.  It was null or had no substrance.");
	}

    }

    /**
     * Spin options are made up of two components: those passed to the compiler
     * and those passed to the compiled application (or pan).  This method will take
     * the command line options for the compiler, parse them, and set the state
     * of the SpinOptions to match.
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

		/* Instead of using the memory count, we convert this value into
		 * a memory limit.  The memory limit is 2^N bytes so we will calculate
		 * the number of bytes convert it into megabytes (which memory limit
		 * is).  (2^N / 1024) / 1024 or 2^N / 1048576 or even 2^(n - 20)
		 */
		if (s.startsWith("-DMEMCNT")) {
		    String temp = s.substring(9);
		    try {
			int memoryCount = Integer.parseInt(temp);
			if(memoryCount > 20) {
			    setMemoryLimit((int)Math.pow(2, memoryCount - 20));
			}
			else {
			    // just round it up to a megabyte of memory.  is that so wrong? -tcw
			    setMemoryLimit(1);
			}
			//setMemoryCount(Integer.parseInt(temp));
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
            
		if (s.equals("-DNORESOURCEBOUNDED")) {
		    setResourceBounded(false);
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
            
		/* Instead of using the memory count, we convert this value into
		 * a memory limit.  The memory limit is 2^N bytes so we will calculate
		 * the number of bytes convert it into megabytes (which memory limit
		 * is).  (2^N / 1024) / 1024 or 2^N / 1048576 or even 2^(n - 20)
		 */
		if (s.startsWith("-DMEMCNT")) {
		    String temp = s.substring(9);
		    try {
			int memoryCount = Integer.parseInt(temp);
			if(memoryCount > 20) {
			    setMemoryLimit((int)Math.pow(2, memoryCount - 20));
			}
			else {
			    // just round it up to a megabyte of memory.  is that so wrong? -tcw
			    setMemoryLimit(1);
			}
			//setMemoryCount(Integer.parseInt(temp));
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
            
		if (s.equals("-DNORESOURCEBOUNDED")) {
		    setResourceBounded(false);
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

		// grab the space estimate (-w) and convert it
		if(opt.startsWith("-w")) {
		    if (i == commandLineOptionsArray.length) {
			log.error("An error occured while parsing the space estimate(-w).  There was nothing after -w.");
			continue;
		    }
		    String temp = opt.substring(2);
		    log.debug("Found a space estimate (-w) with a value of " + temp);
		    try {
			/*
			double x = Double.parseDouble(temp);
			int se = (int)Math.pow(2, x) - 9;
			log.debug("Setting the space estimate as " + se);
			setSpaceEstimate(se);
			*/
			int se = Integer.parseInt(temp);
			setSpaceEstimate(se);
		    }
		    catch(Exception e) {
			log.error("An error occured while parsing the space estimate (-w) specified, " +
				  opt + ", into a double.  Using the default instead.");
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

		// grab the space estimate (-w) and convert it
		if(opt.startsWith("-w")) {
		    if (!st.hasMoreTokens()) {
			log.error("An error occured while parsing the space estimate(-w).  There was nothing after -w.");
			continue;
		    }
		    String temp = opt.substring(2);
		    log.debug("Found a space estimate (-w) with a value of " + temp);
		    try {
			/*
			double x = Double.parseDouble(temp);
			int se = (int)Math.pow(2, x) - 9;
			log.debug("Setting the space estimate as " + se);
			setSpaceEstimate(se);
			*/
			int se = Integer.parseInt(temp);
			setSpaceEstimate(se);
		    }
		    catch(Exception e) {
			log.error("An error occured while parsing the space estimate (-w) specified, " +
				  opt + ", into a double.  Using the default instead.");
		    }
		    continue;
		}

	    }
	}
    }

    /**
     * Set the acceptance cycles flag.  The safety property is the oppposite
     * of this value.
     *
     * @param boolean acceptanceCycles
     */
    public void setAcceptanceCycles(boolean acceptanceCycles) { 
	this.acceptanceCycles = acceptanceCycles;
	safety = !acceptanceCycles; 
    }

    public void setApplyNeverClaim(boolean applyNeverClaim) {
	this.applyNeverClaim = applyNeverClaim;
    }

    public void setAssertions(boolean assertions) {
	this.assertions = assertions;
    }

    public void setCompression(boolean compression) {
	this.compression = compression;
    }

    public void setFindShortestTrail(boolean findShortestTrail) {
	this.findShortestTrail = findShortestTrail;
    }

    /*
    public void setMemoryCount(int memoryCount) {
	this.memoryCount = memoryCount;
    }
    */

    public void setMemoryLimit(int memoryLimit) {
	this.memoryLimit = memoryLimit;
    }

    public void setPartialOrderReduction(boolean partialOrderReduction) {
	this.partialOrderReduction = partialOrderReduction;
    }

    public void setResourceBounded(boolean resourceBounded) {
	this.resourceBounded = resourceBounded;
    }

    public void setSafety(boolean safety) { 
	this.safety = safety; 
	acceptanceCycles = !safety;
	applyNeverClaim = !safety;
    }

    public void setSaveAllTrails(boolean saveAllTrails) {
	this.saveAllTrails = saveAllTrails;
    }

    public void setSearchDepth(int searchDepth) {
	this.searchDepth = searchDepth;
    }

    public void setSearchMode(int searchMode) {
	this.searchMode = searchMode;
    }

    public void setSpaceEstimate(int spaceEstimate) {
	this.spaceEstimate = spaceEstimate;
    }

    public void setStopAtError(int stopAtError) {
	// always stop at the first error. -tcw
	this.stopAtError = 1;
    }

    public void setVectorSize(int vectorSize) {
	this.vectorSize = vectorSize;
    }

    public void setChooseFreeSearch(boolean chooseFreeSearch) {
	this.chooseFreeSearch = chooseFreeSearch;
    }

    public boolean getAcceptanceCycles() {
	return(acceptanceCycles);
    }

    public boolean getAssertions() {
	return(assertions);
    }

    public boolean getApplyNeverClaim() {
	return(applyNeverClaim);
    }

    public boolean getCompression() {
	return(compression);
    }

    public boolean getFindShortestTrail() {
	return(findShortestTrail);
    }

    /*
    public int getMemoryCount() {
	return(memoryCount);
    }
    */

    public int getMemoryLimit() {
	return(memoryLimit);
    }

    /**
     * Get the flag for using partial order reduction on the model.
     *
     * @return boolean
     */
    public boolean getPartialOrderReduction() {
	return(partialOrderReduction);
    }

    public boolean getResourceBounded() {
	return(resourceBounded);
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

    public boolean getChooseFreeSearch() {
	return(chooseFreeSearch);
    }

    /**
     * A simple test to show the command line options when different settings are used.
     */
    public static void main(String args[]) {
	SpinOptions so = new SpinOptions();
    
	System.out.println("Default compile options :" + so.getCompilerCommandLineOptions());
	System.out.println("Default pan options :" + so.getPanCommandLineOptions());

	so = new SpinOptions();
	so.setSearchMode(SUPER_TRACE_SEARCH_MODE);
	System.out.println("Supertrace compile options :" + so.getCompilerCommandLineOptions());
	System.out.println("Supertrace pan options :" + so.getPanCommandLineOptions());

	so = new SpinOptions();
	so.setSearchMode(HASH_COMPACT_SEARCH_MODE);
	System.out.println("Hash-compact compile options :" + so.getCompilerCommandLineOptions());
	System.out.println("Hash-compact pan options :" + so.getPanCommandLineOptions());

	so = new SpinOptions();
	so.setSafety(true);
	System.out.println("Safety compile options :" + so.getCompilerCommandLineOptions());
	System.out.println("Safety pan options :" + so.getPanCommandLineOptions());

	so = new SpinOptions();
	so.setMemoryLimit(1000);
	so.setSearchDepth(50000);
	so.setSpaceEstimate(2050);
	System.out.println("Limits compile options :" + so.getCompilerCommandLineOptions());
	System.out.println("Limits pan options :" + so.getPanCommandLineOptions());

	so = new SpinOptions();
	so.setApplyNeverClaim(false);
	so.setPartialOrderReduction(true);
	so.setCompression(true);
	System.out.println("No never, po, compress compile options :"
			   + so.getCompilerCommandLineOptions());
	System.out.println("No never, po, compress pan options :" + so.getPanCommandLineOptions());
    }
}
