package edu.ksu.cis.bandera.bui.session;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import java.io.*;
import edu.ksu.cis.bandera.bui.session.datastructure.*;
import edu.ksu.cis.bandera.bui.session.analysis.*;
import edu.ksu.cis.bandera.bui.session.lexer.*;
import edu.ksu.cis.bandera.bui.session.node.*;
import edu.ksu.cis.bandera.bui.session.parser.*;
import edu.ksu.cis.bandera.jjjc.util.*;
import java.util.*;
import edu.ksu.cis.bandera.bui.BUI;
//import edu.ksu.cis.bandera.bui.JPFOption;

public class SessionsSaverLoader extends DepthFirstAdapter {
	private static Sessions sessions;
	private Session session;
	private StringBuffer buffer;
/**
 * 
 * @param node edu.ksu.cis.bandera.bui.session.node.ASession
 */
public void caseASession(ASession node) {
	session = new Session(node.getId().toString().trim());
	Object temp[] = node.getResource().toArray();
	for (int i = 0; i < temp.length; i++) {
		((PResource) temp[i]).apply(this);
	}
	sessions.putSession(session);
}

// robbyjo's patch for resolving filenames begin
private String resolveFilename(String fn)
{
    if (fn == null || fn.equals("")) return "";

    // If it's an absolute filename, then return it as it is.
    if (fn.startsWith(File.separator) ||  // Unix style
        fn.indexOf(":\\") > 0 || // Windows style
        fn.indexOf("//") == 0 // Windows network (WfW) style
        ) return fn;

    // Otherwise, the relative directory is resolved relative to the session's path
    File parentFile = new File(sessions.getFilename()).getParentFile();
    if (parentFile == null) return fn; // We can't resolve its directory... weird...
    String parentPath = parentFile.getAbsolutePath();
    if (!parentPath.endsWith(File.separator)) parentPath += File.separator;

    return parentPath + fn;
}
// robbyjo's patch for resolving filenames end

/**
 * 
 * @param node edu.ksu.cis.bandera.bui.session.node.AStringResource
 */
public void caseAStringResource(AStringResource node) {
	String key = node.getId().toString().trim();
	buffer = new StringBuffer();
	node.getStrings().apply(this);

	if ("source".equals(key)) {
		session.setFilename(resolveFilename(buffer.toString())); // robbyjo's patch
	} else if ("classpath".equals(key)) {
		StringTokenizer tok = new StringTokenizer(buffer.toString(), "+");
		String classPath = "";
		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			classPath += resolveFilename(token);
			if (tok.hasMoreTokens()) classPath += File.pathSeparator;
		}

          File parentFile = new File(sessions.getFilename()).getParentFile();
          if (parentFile != null)
          {
          	 if (!classPath.equals("")) classPath += File.pathSeparatorChar;
                classPath += parentFile.getAbsolutePath() ;
          }
 		session.setClasspath(classPath);
	} else if ("included".equals(key)) {
          File parentFile = new File(sessions.getFilename()).getParentFile();
          if (parentFile != null)
          {
               buffer = new StringBuffer(parentFile.getAbsolutePath() + "+" + buffer.toString());
          }
		StringTokenizer t = new StringTokenizer(buffer.toString(), "+");
		String[] included = new String[t.countTokens()];
		for (int i = 0; t.hasMoreTokens(); i++) {
			included[i] = t.nextToken();
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
		session.setSpecFilename(resolveFilename(buffer.toString()));  // robbyjo's patch
	} else if ("temporal".equals(key)) {
		session.setActiveTemporal(buffer.toString());
	} else if ("assertion".equals(key)) {
		for (StringTokenizer tokenizer = new StringTokenizer(buffer.toString(), "+ ");
				tokenizer.hasMoreTokens();) {
			session.addActiveAssertion(tokenizer.nextToken());
		}
	} else if ("abstraction".equals(key)) {
		session.setAbsFilename(resolveFilename(buffer.toString())); // robbyjo's patch
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
		int minInt, maxInt, maxArr, maxIns, maxThreads;
		try {
			minInt = Integer.parseInt(tok.nextToken().trim());
			maxInt = Integer.parseInt(tok.nextToken().trim());
			if (minInt > maxInt)
			{
				System.out.println("WARNING: Illegal integer values detected!");
				throw new Exception();
			}
			maxArr = Integer.parseInt(tok.nextToken().trim());
			maxIns = Integer.parseInt(tok.nextToken().trim());
			maxThreads = Integer.parseInt(tok.nextToken().trim());
		} catch (Exception e)
		{
			System.out.println("WARNING: BIR bounds setting is not acceptable. Revert to default value!");
			minInt = edu.ksu.cis.bandera.util.DefaultValues.birMinIntRange;
			maxInt = edu.ksu.cis.bandera.util.DefaultValues.birMaxIntRange;
			maxArr = edu.ksu.cis.bandera.util.DefaultValues.birMaxArrayLen;
			maxIns = edu.ksu.cis.bandera.util.DefaultValues.birMaxInstances;
			maxThreads = edu.ksu.cis.bandera.util.DefaultValues.birMaxThreads;
		}
		session.setBirMinIntRange(minInt);
		session.setBirMaxIntRange(maxInt);
		session.setBirMaxArrayLen(maxArr);
		session.setBirMaxInstances(maxIns);
		session.setBirMaxThreads(maxThreads);
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
 * 
 * @return edu.ksu.cis.bandera.bui.session.Sessions
 * @param filename java.lang.String
 */
public static Sessions load(String filename) throws Exception {
	sessions = new Sessions();
     sessions.setFilename(filename); // robbyjo's patch
	new Parser(new Lexer(new PushbackReader(new FileReader(filename)))).parse().apply(new SessionsSaverLoader());
	return sessions;
}
/**
 * 
 * @param sessions edu.ksu.cis.bandera.bui.session.Sessions
 */
public static void save(Sessions sessions) throws Exception {
	if (sessions.getActiveSession() != null) sessions.getActiveSession().saveOptions(); 
	PrintWriter pw = new PrintWriter(new FileWriter(sessions.getFilename()), true);
	for (Enumeration e = sessions.getSessions().elements(); e.hasMoreElements();) {
		Session s = (Session) e.nextElement();
		pw.println(s.getStringRepresentation());
	}
	sessions.setSaved(true);
}
}
