package edu.ksu.cis.bandera.checker;

/**
 * The Options interface is a simple interface for handling command line options
 * within Bandera.  Each checker that has options should implement this interface.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:22 $
 */
public interface Options {

    /**
     * Initialize these command line options using the default values.
     */
    public void init();

    /**
     * Initialize these command line options using the String.
     *
     * @param String commandLineOptions A single String of command line options
     *        (as it might be seen on the command line for the checker).
     */
    public void init(String commandLineOptions);

    /**
     * Initialize these command line options using the array of Strings.
     *
     * @param String[] commandLineOptions An array Strings of command line options
     *        (as it might be seen on the command line for the checker).
     */
    public void init(String[] commandLineOptionsArray);

    /**
     * Get the String that represents these options that can be used on the
     * command line for the checker.
     *
     * @return String A String of command line options.
     */
    public String getCommandLineOptions() throws Exception;

    /**
     * Get the String array that represents these options that can be used on the
     * command line for the checker.
     *
     * @return String[] An array of Strings of command line options.
     */
    public String[] getCommandLineOptionsArray() throws Exception;
}
