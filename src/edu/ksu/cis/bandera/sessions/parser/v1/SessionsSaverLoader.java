package edu.ksu.cis.bandera.sessions.parser.v1;

import java.io.*;
import edu.ksu.cis.bandera.sessions.parser.v1.analysis.*;
import edu.ksu.cis.bandera.sessions.parser.v1.lexer.*;
import edu.ksu.cis.bandera.sessions.parser.v1.node.*;
import edu.ksu.cis.bandera.sessions.parser.v1.parser.*;
import edu.ksu.cis.bandera.jjjc.util.*;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.ksu.cis.bandera.util.DefaultValues;

import edu.ksu.cis.bandera.sessions.parser.v1.Session;
import edu.ksu.cis.bandera.sessions.parser.NotAFileException;
import edu.ksu.cis.bandera.sessions.parser.FileNotReadableException;
import edu.ksu.cis.bandera.sessions.parser.FileNotWritableException;
import edu.ksu.cis.bandera.sessions.parser.FileExistsException;

import edu.ksu.cis.bandera.jjjc.util.Util;


/**
 * The SessionsSaverLoader provides a way to parse the version 1
 * session file format into a List of Session objects.
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class SessionsSaverLoader extends DepthFirstAdapter {

    private static List sessions;
    private Session session;
    private StringBuffer buffer;

    /**
     * When we get to a new session name, create a new current session so
     * that all the parsed properties (resources) will get set on this
     * current session.
     *
     * @param ASession node The current node in the AST.
     */
    public void inASession(ASession node) {
	String sessionName = node.getId().toString().trim();
	session = new Session(sessionName);
    }

    /**
     * When we get to the end of a new session, add the current session
     * to our collection.
     *
     * @param ASession node The current node in the AST ... ignored in this case.
     */
    public void outASession(ASession node) {
	sessions.add(session);
    }

    /**
     * 
     */
    public void caseAStringResource(AStringResource node) {
	String key = node.getId().toString().trim();
	buffer = new StringBuffer();
	node.getStrings().apply(this);

	if ("source".equals(key)) {
	    session.setFilename(buffer.toString());
	} else if ("classpath".equals(key)) {
	    String classpath = buffer.toString().replace('+', File.pathSeparatorChar);
	    session.setClasspath(classpath);
	} else if ("included".equals(key)) {
	    StringTokenizer t = new StringTokenizer(buffer.toString(), "+");
	    String[] included = new String[t.countTokens()];
	    for (int i = 0; t.hasMoreTokens(); i++) {
		included[i] = t.nextToken();
		//System.out.println("included[" + i + "] = " + included[i]);
	    }
	    session.setIncludedPackagesOrTypes(included);
	} else if ("output".equals(key)) {
	    session.setOutputName(buffer.toString());
	} else if ("directory".equals(key)) {
	    session.setWorkingDirectory(buffer.toString());
	} else if ("description".equals(key)) {
	    session.setDescription(buffer.toString());
	} else if ("components".equals(key)) {
	    for (StringTokenizer tokenizer = new StringTokenizer(buffer.toString(), "+ ");
		 tokenizer.hasMoreTokens();) {
		String s = tokenizer.nextToken().toLowerCase();
		if ("slicer".equals(s)) {
		    session.setDoSlicer(true);
		} else if ("slabs".equals(s) || "babs".equals(s)) {
		    session.setDoSLABS(true);
		} else if ("checker".equals(s)) {
		    session.setDoChecker(true);
		}
	    }
	} else if ("specification".equals(key)) {
	    session.setSpecFilename(buffer.toString());
	} else if ("temporal".equals(key)) {
	    session.setActiveTemporal(buffer.toString());
	} else if ("assertion".equals(key)) {
	    StringTokenizer tokenizer = new StringTokenizer(buffer.toString(), "+ ");
	    while(tokenizer.hasMoreTokens()) {
		String currentAssertion = tokenizer.nextToken();
		//System.out.println("* currentAssertion = " + currentAssertion);
		session.addActiveAssertion(currentAssertion);
	    }
	} else if ("abstraction".equals(key)) {
	    session.setAbsFilename(buffer.toString());
	} else if ("birc".equals(key)) {
	    session.setBIRCOption(buffer.toString());
	} else if ("spin".equals(key)) {
	    session.setSpinOptions(buffer.toString());
	    session.setUseSPIN(true);
	} else if ("jpf".equals(key)) {
	    session.setJpfOptions(buffer.toString());
	    session.setUseJPF(true);
	} else if ("smv".equals(key)) {
	    session.setSmvOptions(buffer.toString());
	    session.setUseSMV(true);
	} else if ("dspin".equals(key)) {
	    session.setDspinOptions(buffer.toString());
	    session.setUseDSPIN(true);
	} else if ("birbounds".equals(key)) {
	    StringTokenizer tok = new StringTokenizer(buffer.toString(), ",");
	    int minInt = DefaultValues.birMinIntRange;
	    int maxInt = DefaultValues.birMaxIntRange;
	    int maxArr = DefaultValues.birMaxArrayLen;
	    int maxIns = DefaultValues.birMaxInstances;
	    int maxThreadIns = DefaultValues.birMaxThreads;
	    try {
		if(tok.hasMoreTokens()) {
		    String token = tok.nextToken();
		    //System.out.println("minInt token = " + token);
		    minInt = Integer.parseInt(token.trim());
		}
		if(tok.hasMoreTokens()) {
		    String token = tok.nextToken();
		    //System.out.println("maxInt token = " + token);
		    maxInt = Integer.parseInt(token.trim());
		}
		if (minInt > maxInt) {
		    System.err.println("Warning: Invalid integer range specified in the session file." + 
				       "  The min is greater than the max.  Using the default values instead.");
		    minInt = DefaultValues.birMinIntRange;
		    maxInt = DefaultValues.birMaxIntRange;
		}

		if(tok.hasMoreTokens()) {
		    String token = tok.nextToken();
		    //System.out.println("maxArr token = " + token);
		    maxArr = Integer.parseInt(token.trim());
		}
		if(tok.hasMoreTokens()) {
		    String token = tok.nextToken();
		    //System.out.println("maxIns token = " + token);
		    maxIns = Integer.parseInt(token.trim());
		}
		if(tok.hasMoreTokens()) {
		    String token = tok.nextToken();
		    //System.out.println("maxThreadIns token = " + token);
		    maxThreadIns = Integer.parseInt(token.trim());
		}
	    }
	    catch (Exception e) {
		System.err.println("WARNING: An exception occured while parsing the BIR resource bounds." +
				   " Using the default values instead.");
		minInt = DefaultValues.birMinIntRange;
		maxInt = DefaultValues.birMaxIntRange;
		maxArr = DefaultValues.birMaxArrayLen;
		maxIns = DefaultValues.birMaxInstances;
		maxThreadIns = DefaultValues.birMaxThreads;
	    }

	    session.setBirMinIntRange(minInt);
	    session.setBirMaxIntRange(maxInt);
	    session.setBirMaxArrayLen(maxArr);
	    session.setBirMaxInstances(maxIns);
	    session.setBirMaxThreadInstances(maxThreadIns);
	} else {
	    session.putResource(key, buffer.toString());
	}
    }

    /**
     * 
     * @param node edu.ksu.cis.bandera.bui.session.node.AStringsStrings
     */
    public void caseAStringsStrings(AStringsStrings node) {
	node.getStrings().apply(this);
	buffer.append(Util.decodeString(node.getStringLiteral().toString().trim()));
    }

    /**
     * 
     * @param node edu.ksu.cis.bandera.bui.session.node.AStringStrings
     */
    public void caseAStringStrings(AStringStrings node) {
	buffer.append(Util.decodeString(node.getStringLiteral().toString().trim()));
    }

    /**
     * Load up a list of sessions from the given file.
     *
     * @param String filename The filename to read session information from.
     */
    public static List load(String filename) throws Exception {
	sessions = new ArrayList();
	new Parser(new Lexer(new PushbackReader(new FileReader(filename)))).parse().apply(new SessionsSaverLoader());
	return(sessions);
    }

    /**
     * Save this session list to the given file.
     *
     * @param List sessions A list of Session objects to be saved.
     * @param File file The file in which to write these sessions.
     */
    public static void save(List sessions, File file, boolean overwrite)
	throws FileNotFoundException, FileExistsException, FileNotWritableException, NotAFileException, IOException {

	if((sessions == null) || (sessions.size() <= 0)) {
	    System.out.println("Cannot save session data for an empty session list.");
	}

	if(file == null) {
	    throw new FileNotFoundException("Cannot save to a null file.");
	}

	if(file.exists() && !overwrite) {
	    throw new FileExistsException("Cannot overwrite an existing file (" + file.getAbsolutePath() + ").");
	}

	if(!file.exists()) {
	    file.createNewFile();
	}

	if(!file.canWrite()) {
	    throw new FileNotWritableException("Cannot write to the given file (" + file.getAbsolutePath() + ").");
	}

	if(!file.isFile()) {
	    throw new NotAFileException("Cannot write to a directory.");
	}

	System.out.println("Writing a session file: " + file.getAbsolutePath());
	System.out.println("------------------------------------------------------");
	FileWriter fw = new FileWriter(file);
	PrintWriter pw = new PrintWriter(fw, true);
	for (int i = 0; i < sessions.size(); i++) {
	    Session session = (Session)sessions.get(i);
	    String sessionString = getStringRepresentation(session);
	    System.out.println(sessionString);
	    pw.println(sessionString);
	}
	pw.flush();
	pw.close();
	fw.close();
	System.out.println("------------------------------------------------------");
    }


    /**
     * Create a String representation of a session.  This is the actual file format
     * for the V1 session file.  It is a list of key/value properties delimited by
     * '{' and '}' characters with a session name before the opening '{'.
     *
     * @param Session session A session to write to the file format.
     * @return String A String version of the session given.
     */
    public static String getStringRepresentation(Session session) {

	if(session == null) {
	    return("");
	}

	String line = System.getProperty("line.separator");
	StringBuffer buffer = new StringBuffer("session " + session.getName() + " {" + line);
	buffer.append("  source = \"" + Util.encodeString(session.getFilename()) + "\"" + line);
	buffer.append("  classpath = \"" +
		      Util.encodeString(session.getClasspath().replace(File.pathSeparatorChar, '+')) +
		      "\"" + line);

	buffer.append("  included = \"");
	String[] includedPackagesOrTypes = session.getIncludedPackagesOrTypes();
	int length = includedPackagesOrTypes.length;
	for (int i = 0; i < length - 1; i++) {
	    buffer.append(Util.encodeString(includedPackagesOrTypes[i]) + "+");
	}
	if (length > 0)
	    buffer.append(Util.encodeString(includedPackagesOrTypes[length - 1]));
	buffer.append("\"" + line);

	buffer.append("  output = \"" + Util.encodeString(session.getOutputName()) + "\"" + line);
	buffer.append("  directory = \"" + Util.encodeString(session.getWorkingDirectory()) + "\"" + line);
	buffer.append("  description = \"" + Util.encodeString(session.getDescription()) + "\"" + line);

	buffer.append("  components = \"");
	StringBuffer componentsText = new StringBuffer();
	if(session.isDoSlicer()) {
	    componentsText.append("Slicer+");
	}
	if(session.isDoSLABS()) {
	    componentsText.append("SLABS+");
	}
	if(session.isDoChecker()) {
	    componentsText.append("Checker+");
	}
	if(componentsText.charAt(componentsText.length() - 1) == '+') {
	    buffer.append(componentsText.substring(0, componentsText.length() - 1));
	}
	else {
	    buffer.append(componentsText.toString());
	}
	buffer.append("\"" + line);
	String specFilename = session.getSpecFilename();
	if((specFilename == null) || (specFilename.length() <= 0)) {
	    buffer.append("  specification = \"\"" + line);
	}
	else {
	    buffer.append("  specification = \"" + Util.encodeString(specFilename) + "\"" + line);
	}

	String activeTemporal = session.getActiveTemporal();
	buffer.append("  temporal = \"" + ((activeTemporal == null) ? "" : activeTemporal) + "\"" + line);

	HashSet activeAssertions = session.getActiveAssertions();
	if (activeAssertions.size() == 0) {
	    buffer.append("  assertion = \"\"" + line);
	} else {
	    Iterator i = activeAssertions.iterator();
	    buffer.append("  assertion = \"" + i.next());
	    while (i.hasNext()) {
		buffer.append("+" + i.next());
	    }
	    buffer.append("\"" + line);
	}

	String absFilename = session.getAbsFilename();
	if((absFilename == null) || (absFilename.length() <= 0)) {
	    buffer.append("  abstraction = \"\"" + line);
	}
	else {
	    buffer.append("  abstraction = \"" + Util.encodeString(absFilename) + "\"" + line);
	}

	String bircOption = session.getBIRCOption();
	buffer.append("  birc = \"" + ((bircOption == null) ? "" : bircOption) + "\"" + line);

	if (session.isUseSPIN()) {
	    buffer.append("  spin = \"" + session.getSpinOptions() + "\"" + line);
	}
	if (session.isUseJPF()) {
	    buffer.append("  jpf = \"" + session.getJpfOptions() + "\"" + line);
	}
	if (session.isUseDSPIN()) {
	    buffer.append("  dspin = \"" + session.getDspinOptions() + "\"" + line);
	}
	if (session.isUseSMV()) {
	    buffer.append("  smv = \"" + session.getSmvOptions() + "\"" + line);
	}

	if (!session.isUseJPF())
	    buffer.append("   birbounds =\"" +
			  session.getBirMinIntRange() + ", " +
			  session.getBirMaxIntRange() + ", " +
			  session.getBirMaxArrayLen() + ", " +
			  session.getBirMaxInstances() + ", " +
			  session.getBirMaxThreadInstances() + "\"");

	Set keySet = session.getResourceKeySet();
	Iterator i = keySet.iterator();
	while(i.hasNext()) {
	    String key = (String)i.next();
	    String value = session.getResource(key);
	    buffer.append("  " + key + " = \"" + Util.encodeString(value) + "\"" + line);
	}

	buffer.append("}" + line);
	return buffer.toString();
    }
}
