package edu.ksu.cis.bandera.util;

import java.io.File;
import java.io.IOException;

/**
 * The NeverClaimTranslator provides the contract for the
 * definition of a never claim translator.  Implementations of this
 * interface will provide the logic to take an LTL specification and
 * generate a never claim.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:46 $
 */
public interface NeverClaimTranslator {

    /**
     * Translate the given LTL file into a never claim and put it
     * into the file given.
     *
     * @param File ltlFile The file which contains the LTL specification.
     * @param File nvrFile The file in which to place the generated never claim.
     */
    public void translate(File ltlFile, File nvrFile) throws IOException;

    /**
     * Translate the given LTL file into a never claim and put it
     * into the file given.
     *
     * @param String ltlFilename The name of the file which contains the LTL specification.
     * @param String nvrFilename The name of the file in which to place the generated never claim.
     */
    public void translate(String ltlFilename, String nvrFilename) throws IOException;
}
