package edu.ksu.cis.bandera.util;

import edu.ksu.cis.bandera.util.NeverClaimTranslator;

import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * The SpinNeverClaimTranslator provides an implementation of
 * a never claim translator using Spin.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:46 $
 */
public class SpinNeverClaimTranslator implements NeverClaimTranslator {

    /**
     * Translate the given LTL file into a never claim and put it
     * into the file given.
     *
     * @param File ltlFile The file which contains the LTL specification.
     * @param File nvrFile The file in which to place the generated never claim.
     */
    public void translate(File ltlFile, File nvrFile) throws IOException {

	if((ltlFile == null) || (!ltlFile.exists())) {
	    throw new FileNotFoundException("The LTL file could not be found (either it was null or did not exist.");
	}

	if(nvrFile == null) {
	    throw new FileNotFoundException("The Never file given could not be found (it was null).");
	}
	if(!nvrFile.exists()) {
	    nvrFile.createNewFile();
	}
	if(!nvrFile.canWrite()) {
	    throw new IOException("The Never file given could not be written to.");
	}
	
	String neverClaim = BanderaUtil.exec("spin -F " + ltlFile.getPath(), true);
	PrintWriter pw = new PrintWriter(new FileWriter(nvrFile));
	pw.print(neverClaim);
	pw.flush();
	pw.close();

    }

    /**
     * Translate the given LTL file into a never claim and put it
     * into the file given.
     *
     * @param String ltlFilename The name of the file which contains the LTL specification.
     * @param String nvrFilename The name of the file in which to place the generated never claim.
     */
    public void translate(String ltlFilename, String nvrFilename) throws IOException {

	File ltlFile = new File(ltlFilename);
	File nvrFile = new File(nvrFilename);
	translate(ltlFile, nvrFile);

    }
}
