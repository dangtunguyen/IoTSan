package edu.ksu.cis.bandera.util;

import org.apache.log4j.Category;

/**
 * The PlatformInformation provides a central, and easy,
 * method of grabbing platform information.  This includes
 * what OS we are currently running on.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:46 $
 */
public class PlatformInformation {

    /**
     * The log we write messages to.
     */
    private static final Category log = Category.getInstance(PlatformInformation.class);

    /**
     * The Unix family of operating systems that includes Sun Solaris, Linux,
     * etc.
     */
    public static final int UNIX_OS_FAMILY = 100;

    /**
     * The Windows family of &quot;operating systems&quot; that includes Windows 95,
     * through Windows XP.
     */
    public static final int WINDOWS_OS_FAMILY = 101;

    /**
     * The Apple Macintosh family of operating systems that includes MacOS X, etc.
     */
    public static final int MAC_OS_FAMILY = 102;

    /**
     * Any OS that doesn't fit into the other families.
     */
    public static final int UNKNOWN_OS_FAMILY = 199;

    /**
     * The current operating system family.
     */
    private static int osFamily = UNKNOWN_OS_FAMILY;

    static {
	init();
    }

    /**
     * Initialize the platform information.
     */
    private static void init() {

	String osName = System.getProperty("os.name").toLowerCase();
	log.debug("osName = " + osName);
	if(osName.indexOf("win") >= 0) {
	    osFamily = WINDOWS_OS_FAMILY;
	}
	else if(osName.indexOf("mac") >= 0) {
	    // not sure if this is correct? -tcw
	    osFamily = MAC_OS_FAMILY;
	}
	else if(osName.indexOf("linux") >= 0) {
	    osFamily = UNIX_OS_FAMILY;
	}
	else if(osName.indexOf("sunos") >= 0) {
	    osFamily = UNIX_OS_FAMILY;
	}
	else {
	    osFamily = UNKNOWN_OS_FAMILY;
	}

    }

    /**
     * Is the current OS in the Unix family of Operating systems?
     *
     * @return boolean True if the current OS is a Unix-like OS, false otherwise.
     */
    public static boolean isUnixOSFamily() {
	return(osFamily == UNIX_OS_FAMILY);
    }

    /**
     * Is the current OS in the Windows family of Operating systems?
     *
     * @return boolean True if the current OS is a Windows OS, false otherwise.
     */
    public static boolean isWindowsOSFamily() {
	return(osFamily == WINDOWS_OS_FAMILY);
    }

    /**
     * Is the current OS in the Macintosh family of Operating systems?
     *
     * @return boolean True if the current OS is a Mac OS, false otherwise.
     */
    public static boolean isMacOSFamily() {
	return(osFamily == MAC_OS_FAMILY);
    }

    /**
     * Is the current OS not an known OS family?
     *
     * @return boolean True if the current OS not in a known OS family, false otherwise.
     */
    public static boolean isUnknownOSFamily() {
	return(osFamily == UNKNOWN_OS_FAMILY);
    }

    /**
     * Get the OS Family for the current OS.
     *
     * @return int The family the current OS is in.  This value is one of the
     *         int constants defined in this class (example: UNIX_OS_FAMILY).
     */
    public static int getOSFamily() {
	return(osFamily);
    }

}
