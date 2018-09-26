package edu.ksu.cis.bandera.util;

import edu.ksu.cis.bandera.util.NeverClaimTranslator;

//import gov.nasa.arc.ase.util.graph.Graph;

//import gov.nasa.arc.ase.ltl.LTL2Buchi;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.apache.log4j.Category;

/**
 * The JPFNeverClaimTranslator provides an implementation of
 * a never claim translator using the libraries provided by
 * JPF.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:46 $
 */
public class JPFNeverClaimTranslator implements NeverClaimTranslator {

    private static final Category log = Category.getInstance(JPFNeverClaimTranslator.class);

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

	try {
	    //Graph graph = LTL2Buchi.translate(ltlFile);
	    //graph.save(nvrFile.getPath(), Graph.SPIN_FORMAT);
	}
	catch(Exception e) {
	    log.error("An exception occured while translating the LTL file into a Never claim file.", e);
	    throw new IOException("An exception occured while translating the LTL file into a Never claim file.");
	}

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
