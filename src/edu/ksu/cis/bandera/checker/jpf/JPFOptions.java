package edu.ksu.cis.bandera.checker.jpf;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

import edu.ksu.cis.bandera.checker.Options;

/**
 * The JPFOptions class provides a model for configuring JPF command line
 * options.
 *
 * The defaults and rules are based upon gov.nasa.arc.ase.jpf.JPFOption
 * and gov.nasa.arc.ase.jpf.jvm.JVMOptions.
 * <ul>
 * <li>Heuristics are only valid when one of these strategies is enabled: Best First, A*, or Beam.</li>
 * <li>The default strategy is DFS.</li>
 * <li>The default value for search depth is -1.</li>
 * <li>Search depth is only valid when the DFS stategy is enabled.</li>
 * <li>The default value for queue limit is -1.</li>
 * <li>Queue limit is only valid when one of these strategies is enabled: BFS, Best First, A*, or Beam.</li>
 * <li>Assertions is enabled by default.</li>
 * <li>Deadlocks are enabled by default.</li>
 * <li>Garbage collection is enabled by default.</li>
 * <li>Atomic lines are enabled by default.</li>
 * <li>The default race level is 3.</li>
 * <li>Race conditions are disabled by default.</li>
 * <li>Post verify is disabled by default.</li>
 * <li>Hash compaction is disabled by default.</li>
 * </ul>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:22 $
 */
public class JPFOptions implements Options {

    /*
     * Todo:
     * 1) Define better var names that match the purpose (look at JPF code and docs).
     * 2) Optimize the command line output using the JPF default assumptions.
     * 3) Allow only certain vars to be set accoring to JPF rules (document logic used)?
     * 4) Document methods and vars.
     * 5) Use the defined static final String values when parsing and generating.
     */



    public static final int DEFAULT_DEBUGS = 0;
    public static final String DEBUGS_CL_STRING = "-debugs";

    public static final int RACE_DEBUGS = 2001;
    public static final String RACE_DEBUGS_CL_STRING = "race";

    public static final int LOCK_DEBUGS = 2002;
    public static final String LOCK_DEBUGS_CL_STRING = "lock";

    public static final int DEPEND_DEBUGS = 2003;
    public static final String DEPEND_DEBUGS_CL_STRING = "depend";

    public static final int DEFAULT_RACE_LEVEL = 3;
    public static final String RACE_CL_STRING = "-race";
    public static final String RACE_LEVEL_CL_STRING = "-race-level";

    public static final int DEFAULT_QUEUE_LIMIT = -1;
    public static final String QUEUE_LIMIT_CL_STRING = "-queue-limit";

    /**
     * The default depth to search in a depth first search (DFS).
     */
    public static final int DEFAULT_SEARCH_DEPTH = -1;

    public static final String SEARCH_DEPTH_CL_STRING = "-search-depth";

    public final static int BFS_STRATEGY = 100;
    public final static String BFS_STRATEGY_STRING = "BFS";
    public final static String BFS_STRATEGY_CL_STRING = "bfs";

    public final static int DFS_STRATEGY = 101;
    public final static String DFS_STRATEGY_STRING = "DFS";
    public final static String DFS_STRATEGY_CL_STRING = "";

    public final static int BEST_FIRST_STRATEGY = 102;
    public final static String BEST_FIRST_STRATEGY_STRING = "Best First";
    public final static String BEST_FIRST_STRATEGY_CL_STRING = "";

    public final static int ASTAR_STRATEGY = 103;
    public final static String ASTAR_STRATEGY_STRING = "A*";
    public final static String ASTAR_STRATEGY_CL_STRING = "-astar";

    public final static int BEAM_STRATEGY = 104;
    public final static String BEAM_STRATEGY_STRING = "Beam";
    public final static String BEAM_STRATEGY_CL_STRING = "-beam";

    public final static int DEFAULT_STRATEGY = DFS_STRATEGY;

    public static final String HEURISTIC_CL_STRING = "-heuristic";

    public static final int DEFAULT_HEURISTIC = 0;

    public static final int INTERLEAVING_HEURISTIC = 1001;
    public static final String INTERLEAVING_HEURISTIC_STRING = "Interleaving";
    public static final String INTERLEAVING_HEURISTIC_CL_STRING = "interleaving";

    public static final int CHOOSE_FREE_HEURISTIC = 1002;
    public static final String CHOOSE_FREE_HEURISTIC_STRING = "Choose-free";
    public static final String CHOOSE_FREE_HEURISTIC_CL_STRING = "choose-free";

    public static final int MOST_BLOCKED_HEURISTIC = 1003;
    public static final String MOST_BLOCKED_HEURISTIC_STRING = "Most Blocked";
    public static final String MOST_BLOCKED_HEURISTIC_CL_STRING = "most-blocked";

    public static final int BRANCH_PATH_HEURISTIC = 1004;
    public static final String BRANCH_PATH_HEURISTIC_STRING = "Branch Path";
    public static final String BRANCH_PATH_HEURISTIC_CL_STRING = "branch-path";

    public static final int BRANCH_GLOBAL_HEURISTIC = 1005;
    public static final String BRANCH_GLOBAL_HEURISTIC_STRING = "Branch Global";
    public static final String BRANCH_GLOBAL_HEURISTIC_CL_STRING = "branch-global";

    public static final String USER_HEURISTIC_STRING = "User";
    public static final String USER_HEURISTIC_CL_STRING = "user";
    public static final int USER_HEURISTIC = 1006;

    /**
     * A flag the defines if a search depth is being used.
     */
    private boolean limitSearchDepth;

    /**
     * The maximum depth for which to search when performing a depth first search (DFS).
     * The default value is -1.
     */
    private int searchDepth;

    private boolean verify;                            // default = true
    private int strategy;                              // default = DFS
    private int heuristic;                             // default = empty
    private boolean limitQueue;                        // default = false
    private int queueLimit;                            // default = -1
    private boolean usePostOrderReduction;             // default = false
    private boolean useGarbageCollection;              // default = true
    private boolean useHashCompaction;                 // default = false
    private boolean useHeapSymmetry;                   // default = false
    private boolean useAtomicLines;                    // default = false
    private boolean useAssertions;                     // default = true
    private boolean useDeadlocks;                      // default = true
    private boolean checkRaceConditions;               // default = false
    private int raceLevel;                             // default = 3
    private boolean usePostVerify;                     // default = false
    private boolean checkLock;                         // default = false
    private boolean checkDepend;                       // default = false
    private boolean useDebugs;                         // default = false
    private int debugs;                                // default = empty


    public void init() {
	verify = true;
	strategy = DEFAULT_STRATEGY;
	heuristic = DEFAULT_HEURISTIC;
	limitQueue = false;
	queueLimit = DEFAULT_QUEUE_LIMIT;
	limitSearchDepth = false;
	searchDepth = DEFAULT_SEARCH_DEPTH;
	usePostOrderReduction = false;
	useGarbageCollection= true;
	useHashCompaction = false;
	useHeapSymmetry = false;
	useAtomicLines = false;
	useAssertions = true;
	useDeadlocks = true;
	checkRaceConditions = false;
	raceLevel = DEFAULT_RACE_LEVEL;
	usePostVerify = false;
	checkLock = false;
	checkDepend = false;
	useDebugs = false;
	debugs = DEFAULT_DEBUGS;
    }

    public void init(String[] commandLineOptionsArray) {
	if((commandLineOptionsArray != null) && (commandLineOptionsArray.length > 0)) {
	    StringBuffer sb = new StringBuffer();
	    for(int i = 0; i < commandLineOptionsArray.length; i++) {
		sb.append(commandLineOptionsArray[i] + " ");
	    }
	    init(sb.toString().trim());
	}
    }

    public void init(String commandLineOptions) {

	// the first thing to do is to set all values to the default.
	init();

	// now walk through options and configured based upon what we find at each step
	StringTokenizer t = new StringTokenizer(commandLineOptions);
	while(t.hasMoreTokens()) {
	    String opt = t.nextToken();
	    boolean enabled = !opt.startsWith("-no");
	    if (!enabled) opt = opt.substring(3);

	    if ("-heuristic".equals(opt)) {
		// grab the heuristic that is defined
		if(t.hasMoreTokens()) {
		    String heuristicName = t.nextToken();
				
		    if(heuristicName == null) {
			continue;
		    }
				
		    if(heuristicName.equals("bfs")) {
			strategy = BFS_STRATEGY;
		    } else {
			// we will assume for now that the user wants Best First since A* and Beam
			//  will be turned on by -astar or -beam (which is parsed later).
			strategy = BEST_FIRST_STRATEGY;
			if(heuristicName.equals("interleaving")) {
			    heuristic = INTERLEAVING_HEURISTIC;
			} else if(heuristicName.equals("choose-free")) {
			    heuristic = CHOOSE_FREE_HEURISTIC;
			} else if(heuristicName.equals("most-blocked")) {
			    heuristic = MOST_BLOCKED_HEURISTIC;
			} else if(heuristicName.equals("branch-path")) {
			    heuristic = BRANCH_PATH_HEURISTIC;
			} else if(heuristicName.equals("branch-global")) {
			    heuristic = BRANCH_GLOBAL_HEURISTIC;
			} else if(heuristicName.equals("user")) {
			    heuristic = USER_HEURISTIC;
			}
		    }
		}
			
	    } else if ("-astar".equals(opt)) {
		strategy = ASTAR_STRATEGY;
	    } else if ("-beam".equals(opt)) {
		strategy = BEAM_STRATEGY;
	    } else if ("-po-reduction".equals(opt)) {
		usePostOrderReduction = enabled;
	    } else if ("-garbage-collection".equals(opt)) {
		useGarbageCollection = enabled;
	    } else if ("-hashcompact".equals(opt)) {
		useHashCompaction = enabled;
	    } else if ("-search-depth".equals(opt)) {
		limitSearchDepth = true;
		try {
		    searchDepth = Integer.parseInt(t.nextToken());
		}
		catch(Exception e) {
		    searchDepth = 0;  // this should be some global default! -tcw
		}
	    } else if ("-queue-limit".equals(opt)) {
		limitQueue = true;
		try {
		    queueLimit = Integer.parseInt(t.nextToken());
		}
		catch(Exception e) {
		    queueLimit = 0; // this should be some global default! -tcw
		}
	    } else if ("-heap-symmetry".equals(opt)) {
		useHeapSymmetry = enabled;
	    } else if ("-atomic-lines".equals(opt)) {
		useAtomicLines = enabled;
	    } else if ("-assertions".equals(opt)) {
		useAssertions = enabled;
	    } else if ("-deadlocks".equals(opt)) {
		useDeadlocks = enabled;
	    } else if ("-verify".equals(opt)) {
		verify = enabled;
	    } else if ("-race".equals(opt)) {
		checkRaceConditions = true;
		String trash = t.nextToken(); // the next token can be thrown out! -tcw
		try {
		    raceLevel = Integer.parseInt(t.nextToken());
		}
		catch(Exception e) {
		    raceLevel = 0; // this should be some global default! -tcw
		}
	    } else if ("-lock".equals(opt)) {
		checkLock = true;
	    } else if ("-post-verify".equals(opt)) {
		usePostVerify = true;
	    } else if ("-depend".equals(opt)) {
		checkDepend = true;
	    } else if ("-debugs".equals(opt)) {
		String subOpt = t.nextToken();
		if ("race".equals(subOpt)) {
		    debugs = RACE_DEBUGS;
		} else if ("lock".equals(subOpt)) {
		    debugs = LOCK_DEBUGS;
		} else if ("depend".equals(subOpt)) {
		    debugs = DEPEND_DEBUGS;
		}
	    } 
	}
    }

    /**
     * Build a String that holds the command line options that are defined by
     * the current configuration.  This should only return valid sequences of command
     * line options (even when the current state is inconsistent in which case it will
     * do it's best to harvest the user intentions).  The result of this could be appended
     * to the name of the executable and used on the command line or in a shell.
     *
     * @return String
     */
    public String getCommandLineOptions() throws Exception {
	StringBuffer result = new StringBuffer();

	String[] sa = getCommandLineOptionsArray();
	if((sa != null) && (sa.length > 0)) {
	    for(int i = 0; i < sa.length; i++) {
		result.append(sa[i]);
		result.append(" ");
	    }
	}

	return (result.toString().trim());
    }

    /**
     * Build a String array that holds the command line options that are defined by
     * the current configuration.  This should only return valid sequences of command
     * line options (even when the current state is inconsistent in which case it will
     * do it's best to harvest the user intentions).  The result from this could be
     * used by a main method (public static void main(String args)).
     *
     * @return String[] 
     */
    public String[] getCommandLineOptionsArray() throws Exception {

	List list = new ArrayList();

	// if verification is turned on, parse those options
	if (verify) {

	    if (strategy == DFS_STRATEGY) {
		// add nothing to the result since the default stategy is DFS so we
		//  need not specify it on the command line. -tcw

		// search depth is only relevant when DFS is chosen. -tcw
		if (limitSearchDepth) {
		    list.add("-search-depth");
		    list.add(new Integer(searchDepth));
		}

	    }
	    else {
		// since we know that we will be adding a heuristic at this point, just do it!
		list.add("-heuristic");

		// if the strategy is BFS, pass the "-heuristic BFS" flag back
		if (strategy == BFS_STRATEGY) {
		    list.add("bfs");
		}
		else {
		    // this means the strategy is one of the following: ASTAR, Beam, or Best First

		    /* It will only be one of the following since they are in the heuristicButtonGroup */
		    switch(heuristic) {
		    case INTERLEAVING_HEURISTIC :
			list.add("interleaving");
			break;
		    case CHOOSE_FREE_HEURISTIC :
			list.add("choose-free");
			break;
		    case MOST_BLOCKED_HEURISTIC :
			list.add("most-blocked");
			break;
		    case BRANCH_PATH_HEURISTIC :
			list.add("branch-path");
			break;
		    case BRANCH_GLOBAL_HEURISTIC :
			list.add("branch-global");
			break;
		    case USER_HEURISTIC :
			list.add("user");
			break;
		    default :
			throw new Exception("Invalid state that cannot be recovered from." +
					    "  heuristic has an invalid value = " + heuristic);
		    }

		    if (strategy == ASTAR_STRATEGY) {
			list.add("-astar");
		    }
		    else if (strategy == BEAM_STRATEGY) {
			list.add("-beam");
		    }
		    else {
			// this is Best First so don't add anything! -tcw
		    }
		}

		if (limitQueue) {
		    list.add("-queue-limit");
		    list.add(new Integer(queueLimit));
		}

	    }

	    if (usePostOrderReduction) {
		list.add("-po-reduction");
	    }
	    else {
		list.add("-no-po-reduction");
	    }

	    if (useGarbageCollection) {
		list.add("-garbage-collection");
	    }
	    else {
		list.add("-no-garbage-collection");
	    }

	    if (useHashCompaction) {
		list.add("-hashcompact");
	    }
	    else {
		list.add("-no-hashcompact");
	    }

	    if (useHeapSymmetry) {
		list.add("-heap-symmetry");
	    }
	    else {
		list.add("-no-heap-symmetry");
	    }

	    if (useAtomicLines) {
		list.add("-atomic-lines");
	    }
	    else {
		list.add("-no-atomic-lines");
	    }

	    if (useAssertions) {
		// this is one by default so we can leave it off.
		// Note: Make this configurable as optimized or not. -tcw
		//list.add("-assertions");
	    }
	    else {
		list.add("-no-assertions");
	    }

	    if (useDeadlocks) {
		// this is one by default so we can leave it off.
		// Note: Make this configurable as optimized or not. -tcw
		//list.add("-deadlocks");
	    }
	    else {
		list.add("-no-deadlocks");
	    }
	}
	else {
	    list.add("-no-verify");

	    if (checkRaceConditions) {
		list.add("-race");
		list.add("-race-level");
		list.add(new Integer(raceLevel));
	    }

	    if (checkLock) {
		list.add("-lock");
	    }

	    if (usePostVerify) {
		list.add("-post-verify");
	    }

	    if (checkDepend) {
		list.add("-depend");
	    }

	    if(useDebugs) {
		list.add("-debugs");
		switch(debugs) {
		case RACE_DEBUGS:
		list.add("race");
		    break;
		case LOCK_DEBUGS:
		    list.add("lock");
		    break;
		case DEPEND_DEBUGS:
		    list.add("depend");
		    break;
		default:
		    throw new Exception("Invalid state that we cannot recover from." +
					"  debugs has an invalid value = " + debugs);
		}
	    }
	}

	String[] sa = new String[list.size()];
	for(int i = 0; i < list.size(); i++) {
	    sa[i] = list.get(i).toString();
	}
	return(sa);
    }

    /**
     * Set the search strategy to use.  The valid values are defined as
     * constants:
     * <ul>
     * <li>BFS_STRATEGY</li>
     * <li>DFS_STRATEGY</li>
     * <li>BEST_FIRST_STRATEGY</li>
     * <li>ASTAR_STRATEGY</li>
     * <li>BEAM_STRATEGY</li>
     * </ul>
     *
     * @param int strategy The constant that represents the strategy to set.
     * @exception Exception Thrown when the int is not one of the known values.
     */
    public void setStrategy(int strategy) throws Exception {
	switch(strategy) {
	case BFS_STRATEGY:
	case DFS_STRATEGY:
	case BEST_FIRST_STRATEGY:
	case ASTAR_STRATEGY:
	case BEAM_STRATEGY:
	    this.strategy = strategy;
	    break;
	default:
	    throw new Exception("Invalid stategy.");
	}
    }

    /**
     * Get the search strategy that is set.  The values that can be returned
     * are defined by constants:
     * <ul>
     * <li>BFS_STRATEGY</li>
     * <li>DFS_STRATEGY</li>
     * <li>BEST_FIRST_STRATEGY</li>
     * <li>ASTAR_STRATEGY</li>
     * <li>BEAM_STRATEGY</li>
     * </ul>
     *
     * @return int The constant that represents the strategy set.
     */
    public int getStrategy() {
	return(strategy);
    }

    /**
     * Get the Heuristic used.  This value is only valid when the strategy
     * is in the set: {Best First, A*, Beam}.  The values that will be returned
     * are defined as constants:
     * <ul>
     * <li>INTERLEAVING_HEURISTIC</li>
     * <li>CHOOSE_FREE_HEURISTIC</li>
     * <li>MOST_BLOCKED_HEURISTIC</li>
     * <li>BRANCH_PATH_HEURISTIC</li>
     * <li>BRANCH_GLOBAL_HEURISTIC</li>
     * <li>USER_HEURISTIC</li>
     * </ul>
     *
     * @return int The constant that represents the heuristic set.
     */
    public int getHeuristic() {
	return(heuristic);
    }

    /**
     * Set the heuristic to use.  This is only valid if the
     * strategy is in the set: {Best First, A*, Beam}.  The valid
     * values for this are defined as constants:
     * <ul>
     * <li>INTERLEAVING_HEURISTIC</li>
     * <li>CHOOSE_FREE_HEURISTIC</li>
     * <li>MOST_BLOCKED_HEURISTIC</li>
     * <li>BRANCH_PATH_HEURISTIC</li>
     * <li>BRANCH_GLOBAL_HEURISTIC</li>
     * <li>USER_HEURISTIC</li>
     * </ul>
     *
     * @param int heuristic This must be one of the defined constants or an exception
     *            will be thrown.
     * @exception Exception Thrown when the heuristic value is unknown (not one of the 
     *            constants defined).
     */
    public void setHeuristic(int heuristic) throws Exception {
	switch(heuristic) {
	case INTERLEAVING_HEURISTIC:
	case CHOOSE_FREE_HEURISTIC:
	case MOST_BLOCKED_HEURISTIC:
	case BRANCH_PATH_HEURISTIC:
	case BRANCH_GLOBAL_HEURISTIC:
	case USER_HEURISTIC:
	    this.heuristic = heuristic;
	    break;
	default:
	    throw new Exception("Invalid heuristic.");
	}
    }

    /**
     * Enable verification.
     *
     * @post verify == true
     */
    public void enableVerification() {
	verify = true;
    }

    /**
     * Disable verification.
     *
     * @post verify == false
     */
    public void disableVerification() {
	verify = false;
    }

    /**
     * Check to see if verification is turned on.
     *
     * @return boolean True if verification is on, False otherwise.
     */
    public boolean isVerificationEnabled() {
	return(verify);
    }

    /**
     * The search depth provides a maximum depth for which to search when performing
     * a depth first seach (DFS).  Therefore, this is only valid when the heuristic set
     * is DFS.
     *
     * @param int searchDepth The maximum depth to search to when performing a depth-first-search (DFS).
     */
    public void setSearchDepth(int searchDepth) {
	limitSearchDepth = true;
	this.searchDepth = searchDepth;
    }

    /**
     * Disable the search depth.
     */
    public void disableSearchDepth() {
	limitSearchDepth = false;
    }

    /**
     * The queue limit provides ...  It is only valid when the search strategy is
     * in this set: {BFS, Best First, A*, Beam}.
     *
     * @param int queueLimit
     */
    public void setQueueLimit(int queueLimit) {
	limitQueue = true;
	this.queueLimit = queueLimit;
    }

    /**
     * Disable the queue limit.
     */
    public void disableQueueLimit() {
	limitQueue = false;
    }

    /**
     * Test to see if this is configured to use Post Order Reduction.
     *
     * @return boolean True if we should use post order reduction, False otherwise.
     */
    public boolean usePostOrderReduction() {
	return(usePostOrderReduction);
    }

    /**
     * Enable the use of Post Order Reduction.
     *
     * @post usePostOrderReduction == true
     */
    public void enablePostOrderReduction() {
	usePostOrderReduction = true;
    }

    /**
     * Disable the use of Post Order Reduction.
     *
     * @post usePostOrderReduction == false
     */
    public void disablePostOrderReduction() {
	usePostOrderReduction = false;
    }

    /**
     * Test to see if garbage collection is configured to be used.
     * 
     * @return boolean True if garbage collection should be used, False otherwise.
     */
    public boolean useGarbageCollection() {
	return(useGarbageCollection);
    }

    /**
     * Enable the use of garbage collection.
     *
     * @post useGarbageCollection == true
     */
    public void enableGarbageCollection() {
	useGarbageCollection = true;
    }

    /**
     * Disable the use of garbage collection.
     *
     * @post useGarbageCollection == false
     */
    public void disableGarbageCollection() {
	useGarbageCollection = false;
    }

    public boolean useHashCompaction() {
	return(useHashCompaction);
    }

    public void enableHashCompaction() {
	useHashCompaction = true;
    }

    public void disableHashCompaction() {
	useHashCompaction = false;
    }

    public boolean useHeapSymmetry() {
	return(useHeapSymmetry);
    }

    public void enableHeapSymmetry() {
	useHeapSymmetry = true;
    }

    public void disableHeapSymmetry() {
	useHeapSymmetry = false;
    }

    public boolean useAtomicLines() {
	return(useAtomicLines);
    }

    public void enableAtomicLines() {
	useAtomicLines = true;
    }

    public void disableAtomicLines() {
	useAtomicLines = false;
    }

    public boolean useAssertions() {
	return(useAssertions);
    }

    public void enableAssertions() {
	useAssertions = true;
    }

    public void disableAssertions() {
	useAssertions = false;
    }

    public boolean useDeadlocks() {
	return(useDeadlocks);
    }

    public void enableDeadlocks() {
	useDeadlocks = true;
    }

    public void disableDeadlocks() {
	useDeadlocks = false;
    }

    public void disableRaceLevel() {
	checkRaceConditions = false;
    }

    public void setRaceLevel(int raceLevel) {
	checkRaceConditions = true;
	this.raceLevel = raceLevel;
    }

    public boolean usePostVerify() {
	return(usePostVerify);
    }

    public void enablePostVerify() {
	usePostVerify = true;
    }

    public void disablePostVerify() {
	usePostVerify = false;
    }

    public boolean checkLock() {
	return(checkLock);
    }

    public void enableLockCheck() {
	checkLock = true;
    }

    public void disableLockCheck() {
	checkLock = false;
    }

    public boolean checkDepend() {
	return(checkDepend);
    }

    public void enableDependCheck() {
	checkDepend = true;
    }

    public void disableDependCheck() {
	checkDepend = false;
    }

    public boolean useDebugs() {
	return(useDebugs);
    }

    public void enableDebugs() {
	useDebugs = true;
    }

    public void disableDebugs() {
	useDebugs = false;
    }

    public int getDebugs() {
	return(debugs);
    }

    public void setDebugs(int debugs) throws Exception {
	switch(debugs) {
	case RACE_DEBUGS:
	case LOCK_DEBUGS:
	case DEPEND_DEBUGS:
	    this.debugs = debugs;
	    break;
	default:
	    throw new Exception("The value given for debugs is not valid.");
	}
    }

/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 12:36:57 PM)
 * @return int
 */
public int getQueueLimit() {
	return queueLimit;
}

/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 12:36:57 PM)
 * @return int
 */
public int getSearchDepth() {
	return searchDepth;
}

/**
 * Test to see if the queue limit is currently enabled.
 * 
 * @return boolean True if the queue limit should be used, False otherwise.
 */
public boolean isQueueLimitEnabled() {
	return(limitQueue);
}

/**
 * Test to see if the search depth should be used.
 *
 * @return boolean True if the search depth should be used, False otherwise.
 */
public boolean isSearchDepthEnabled() {
	return(limitSearchDepth);
}

/**
 * Test to see if the search depth should be used.
 *
 * @return boolean True if the search depth should be used, False otherwise.
 */
public boolean isSearchDepthLimitEnabled() {
	return(limitSearchDepth);
}

/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 10:47:15 AM)
 * @param newLimitQueue boolean
 */
void setLimitQueue(boolean newLimitQueue) {
	limitQueue = newLimitQueue;
}

/**
 * Insert the method's description here.
 * Creation date: (12/5/2002 10:47:15 AM)
 * @param newLimitSearchDepth boolean
 */
void setLimitSearchDepth(boolean newLimitSearchDepth) {
	limitSearchDepth = newLimitSearchDepth;
}
}
