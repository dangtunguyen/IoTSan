/**
 * The Bandera class is a simple class that provides the calls
 * that most people will be making for non-determinism in model
 * checking and a few extra special calls.  Many of them will
 * be replaced during the translation process but a few of them
 * will be inlined and still others will throw exceptions.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/28 17:36:17 $
 */
public class Bandera {

    /**
     * This method will be replaced in Bandera.
     */
    public static void beginAtomic() {
    }

    /**
     * This method will be replaced in Bandera.
     */
    public static void endAtomic() {
    }

    /**
     * This method will be replaced in Bandera.
     */
    public static void startThread(String className, Object target) {
    }

    /**
     * This method will be replaced in Bandera.
     */
    public static boolean randomBool() {
	return(false);
    }

    /**
     * This method will be inlined in Bandera.
     */
    public static int randomInt(int maxInt) {
	beginAtomic();
	int random = 0;
	if(maxInt > 0) {
	    random = maxInt;
	    for(int i = 0; i < maxInt; i++) {
		if(choose()) {
		    random = i;
		    break;
		}
	    }
	}
	endAtomic();
	return(random);
    }

    /**
     * This method is not valid at this time.
     */
    public static Object randomClass(String className) {
	return(null);
    }

    /**
     * This method is not valid at this time.
     */
    public static Object randomReachable(Object root) {
	return(null);
    }

    /**
     * This method is not valid at this time.
     */
    public static boolean randomBoolExtern() {
	return(false);
    }

    /**
     * This method is not valid at this time.
     */
    public static int randomIntExtern(int maxInt) {
	return(0);
    }

    /**
     * This method is not valid at this time.
     */
    public static Object randomClassExtern(String className) {
	return(null);
    }

    /**
     * This method is not valid at this time.
     */
    public static Object randomReachableExtern(Object root) {
	return(null);
    }

    /**
     * This method will be replaced in Bandera.
     */
    public static void assert(boolean expr) {
    }

    /**
     * This method will be replaced in Bandera.
     */
    public static boolean choose() {
	return(false);
    }
}
