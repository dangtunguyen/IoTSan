package edu.ksu.cis.bandera.checker;

import edu.ksu.cis.bandera.checker.Options;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Category;

/**
 * The DefaultOptions class provides a basic options class that just holds
 * a sequence of Strings as options.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:21 $
 */
public class DefaultOptions implements Options {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(DefaultOptions.class.getName());

    /**
     * The default starting size for the list of options.
     */
    private static final int DEFAULT_LIST_SIZE = 10;

    /**
     * The list of options.
     */
    private List optionsList;

    /**
     * Create a new DefaultOptions object using the default values.
     */
    public DefaultOptions() {
	optionsList = new ArrayList(DEFAULT_LIST_SIZE);
    }

    public void init() {
	    if((optionsList != null) && (optionsList.size() > 0)) {
		    optionsList.clear();
	    }
    }

    /**
     * Initialize these command line options using the String.  This String
     * will be broken down into tokens divided by whitespace.  Each will
     * be placed, in order, into the sequence.
     *
     * @param String commandLineOptions A single String of command line options
     *        (as it might be seen on the command line for the checker).
     */
    public void init(String commandLineOptions) {
	    
	    init();
	if((commandLineOptions != null) && (commandLineOptions.length() > 0)) {
	    StringTokenizer st = new StringTokenizer(commandLineOptions, " ");
	    while(st.hasMoreTokens()) {
		String t = st.nextToken();
		optionsList.add(t);
	    }
	}
    }

    /**
     * Initialize these command line options using the array of Strings.
     *
     * @param String[] commandLineOptions An array Strings of command line options
     *        (as it might be seen on the command line for the checker).
     */
    public void init(String[] commandLineOptionsArray) {

	    init();
	if((commandLineOptionsArray != null) && (commandLineOptionsArray.length > 0)) {
	    for(int i = 0; i < commandLineOptionsArray.length; i++) {
		optionsList.add(commandLineOptionsArray[i]);
	    }
	}
    }

    /**
     * Get the String that represents these options that can be used on the
     * command line for the checker.
     *
     * @return String A String of command line options.
     */
    public String getCommandLineOptions() {

	String optionsString = "";

	if((optionsList != null) && (optionsList.size() > 0)) {
	    StringBuffer sb = new StringBuffer();
	    for(int i = 0; i < optionsList.size(); i++) {
		sb.append(optionsList.get(i).toString() + " ");
	    }
	    optionsString = sb.toString().trim();
	}

	return(optionsString);
    }

    public String[] getCommandLineOptionsArray() {
	String[] optionsArray = null;

	if((optionsList != null) && (optionsList.size() > 0)) {
	    optionsArray = new String[optionsList.size()];
	    for(int i = 0; i < optionsList.size(); i++) {
		optionsArray[i] = optionsList.get(i).toString();
	    }
	}

	return(optionsArray);
    }

}
