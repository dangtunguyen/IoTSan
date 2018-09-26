package edu.ksu.cis.bandera.sessions.parser.util;

import edu.ksu.cis.bandera.sessions.parser.Parser;
import edu.ksu.cis.bandera.sessions.parser.ParserFactory;

import java.io.File;

import java.util.List;
import java.util.StringTokenizer;

/**
 * The SessionFileConverter is a utility that will allow a user
 * to convert an existing session file into another session file
 * format.  The default use is to convert an old file format into
 * the default format (convert(oldFile,newFile)).  Other options
 * include specifying which format to use (convert(oldFile, newFile,parser) and
 * convert(oldFile, newFile, version)).  This can also be used as a command
 * line client to convert files.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:29 $
 */
public class SessionFileConverter {

    public static void main(String[] args) {

	if(args.length < 1) {
	    System.out.println("Not enough parameters.");
	    printHelp();
	}

	for(int i = 0; i < args.length; i++) {
	    String fileNames = args[i];
	    StringTokenizer st = new StringTokenizer(fileNames, ":");
	    if(st.countTokens() == 2) {
		String oldFilename = st.nextToken();
		File oldFile = new File(oldFilename);
		String newFilename = st.nextToken();
		File newFile = new File(newFilename);

		System.out.println("Converting " + oldFile + " into " + newFile);

		try {
		    convert(oldFile, newFile);
		}
		catch(Exception e) {
		    System.err.println("Exception (" + e.toString() + ") while converting " + oldFile + " into " + newFile + ".");
		    e.printStackTrace(System.err);
		}
	    }
	}
    }

    public static void printHelp() {
	System.out.println("java edu.ksu.cis.bandera.sessions.parser.util.SessionFileConverter" +
			   " [oldFilename:newFilename]*");
    }

    public static void convert(File oldFile, File newFile) throws Exception {
	Parser parser = ParserFactory.getParser();
	convert(oldFile, newFile, parser);
    }

    public static void convert(File oldFile, File newFile, Parser parser) throws Exception {

	if(oldFile == null) {
	    throw new Exception("To convert from a file, you must supply a valid file.");
	}

	if(newFile == null) {
	    throw new Exception("To convert to a file, you must supply a valid file.");
	}

	if(parser == null) {
	    throw new Exception("Invalid parser given.");
	}

	Parser oldParser = ParserFactory.getParserForFile(oldFile);
	if(oldParser == null) {
	    throw new Exception("No parser can be found to parse " + oldFile);
	}

	List sessionList = oldParser.load(oldFile);
	parser.save(sessionList, newFile, true);

    }

    public static void convert(File oldFile, File newFile, int version) throws Exception {
	Parser parser = ParserFactory.getParser(version);
	convert(oldFile, newFile, parser);
    }

}
