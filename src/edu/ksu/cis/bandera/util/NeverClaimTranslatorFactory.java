package edu.ksu.cis.bandera.util;

import edu.ksu.cis.bandera.util.NeverClaimTranslator;
import edu.ksu.cis.bandera.util.JPFNeverClaimTranslator;
import edu.ksu.cis.bandera.util.SpinNeverClaimTranslator;
import edu.ksu.cis.bandera.util.Preferences;

import org.apache.log4j.Category;

/**
 * The NeverClaimTranslatorFactory provides an easy, reliable,
 * and smart way to create a NeverClaimTranslator.  It will try
 * to create the most efficient translator when called.
 *
 * Note: This class should be expanded to make it easier to use
 * in more situations.  It should be able to create a particular
 * known instance (given some key) or some undefined instance
 * (given the class name).  It might also be good to define
 * the key->class mapping elsewhere and use reflection.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:46 $
 */
public class NeverClaimTranslatorFactory {

    private static final Category log = Category.getInstance(NeverClaimTranslatorFactory.class);

    /**
     * Create a new NeverClaimTranslator.  If JPF is available, return
     * a JPFNeverClaimTranslator (since it is better).  If not, return
     * the default SpinNeverClaimTranslator.
     *
     * Note: This should be changed to make sure Spin is available
     * before doing this!
     *
     * @return NeverClaimTranslator A new NeverClaimTranslator .. the best available.
     */
    public static NeverClaimTranslator createNeverClaimTranslator() {

	NeverClaimTranslator nct = null;
	if(Preferences.isJPFAvailable()) {
	    log.debug("Creating a JPFNeverClaimTranslator.");
	    nct = new JPFNeverClaimTranslator();
	}
	else {
	    log.debug("Creating a SpinNeverClaimTranslator.");
	    nct = new SpinNeverClaimTranslator();
	}

	return(nct);
    }

}
