package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.V1Parser;
import edu.ksu.cis.bandera.sessions.parser.V2Parser;
import edu.ksu.cis.bandera.sessions.parser.Parser;
import edu.ksu.cis.bandera.sessions.parser.FileNotReadableException;
import edu.ksu.cis.bandera.sessions.parser.NotAFileException;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.List;

import org.apache.log4j.Category;

/**
 * The ParserFactory provides a convienent way to create a Parser
 * for session files.  Calling getParser() will return the default
 * parser for session files.  Calling getParser(int) will get a
 * parser of the specified version (the versions being the public static
 * final int values).  Calling getParserForFile(File) will figure out the correct
 * parser type for that file and return it.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:28 $
 */
public class ParserFactory {

    private static final Category log = Category.getInstance(ParserFactory.class.getName());

    /**
     * The version 1 parser.  This is the original session file parser
     * where each session is denoted with '{' and '}' characters and
     * contains key/value pairs.
     */
    public static final int V1_PARSER = 1;

    /**
     * The version 2 parser.  This is the first XML based session file.
     */
    public static final int V2_PARSER = 2;

    /**
     * The default parser will be the version 2 parser.
     */
    public static final int DEFAULT_PARSER = V2_PARSER;

    /**
     * Get the default parser.  This is the same as calling
     * getParser(DEFAULT_PARSER).
     *
     * @return Parser The default parser.
     */
    static public Parser getParser() throws Exception {
	return(getParser(DEFAULT_PARSER));
    }

    /**
     * Get the parser with this version number.
     *
     * @return Parser The parser associated with this version
     *         number.
     * @throws Exception Thrown when the version specified has no associated parser.
     */
    static public Parser getParser(int version) throws Exception {

	Parser parser;

	switch(version) {

	case V1_PARSER:
	    parser = new V1Parser();
	    break;

	case V2_PARSER:
	    parser = new V2Parser();
	    break;

	default:
	    throw new Exception("The version of parser requested (" + version + ") is not available.");
	}

	return(parser);
    }

    /**
     * Get a parser that will be able to parse this file.
     *
     * @param File file The file to be parsed.
     * @return Parser A parser that can handle the content in the file or
     *         null if a matching parser cannot be found.
     * @throws FileNotFoundException When the filename is null or the file does not exist.
     * @throws FileNotReadableException When the file cannot be read.
     * @throws NotAFileException When the file is not actually a file but rather a directory.
     */
    static public Parser getParserForFile(File file)
	throws FileNotFoundException, NotAFileException, FileNotReadableException, Exception {

	if(file == null) {
	    throw new FileNotFoundException("Cannot find a parser for a null file.");
	}

	if(!file.exists()) {
	    throw new FileNotFoundException("The file given (" + file.getAbsolutePath() + ") does not exist.");
	}

	if(!file.canRead()) {
	    throw new FileNotReadableException("The file given (" + file.getAbsolutePath() + ") is not readable.");
	}

	if(!file.isFile()) {
	    throw new NotAFileException("The file given (" + file.getAbsolutePath() + ") is not a file.");
	}

	Parser parser = null;

	int parserCount = 2;
	int[] parsers = new int[parserCount];
	// try the most recent version first (highest version number to lowest) -tcw
	parsers[0] = V2_PARSER;
	parsers[1] = V1_PARSER;

	for(int i = 0; i < parserCount; i++) {
	    try {
		log.debug("Trying parser " + i + ".");
		parser = getParser(parsers[i]);
		log.debug("Trying to load using parser " + parser.getClass().getName() + ".");
		List sessions = parser.load(file);
		log.debug("No exception thrown while loading using parser " + parser.getClass().getName() +
				   ".  We will use this parser.");
		break;
	    }
	    catch(Exception e) {
		// no parser found yet ... continue looking
		log.debug("Exception while loading using parser " + parser.getClass().getName() + ".", e);
		parser = null;
	    }
	}

	if(parser != null) {
	    log.debug("Returning parser of type " + parser.getClass().getName() + ".");
	}
	else {
	    log.debug("Returning a null parser.");
	}
	return(parser);
    }

    /**
     * Get a parser that will be able to parse this file.
     *
     * @param String filename The file name for a session file to be parsed.
     * @return Parser A parser that can handle the content in the file or
     *         null if a matching parser cannot be found.
     * @throws FileNotFoundException When the filename is null or the file does not exist.
     * @throws FileNotReadableException When the file cannot be read.
     * @throws NotAFileException When the file is not actually a file but rather a directory.
     */
    static public Parser getParserForFile(String filename)
	throws FileNotFoundException, NotAFileException, FileNotReadableException, Exception {

	if(filename == null) {
	    throw new FileNotFoundException("A parser cannot be found for a null file.");
	}

	File file = new File(filename);

	if(!file.exists()) {
	    throw new FileNotFoundException("The file given (" + filename + ") does not exist.");
	}

	if(!file.canRead()) {
	    throw new FileNotReadableException("The file given (" + filename + ") is not readable.");
	}

	if(!file.isFile()) {
	    throw new NotAFileException("The file given (" + filename + ") is not a file.");
	}

	return(getParserForFile(file));
    }

}
