package edu.ksu.cis.bandera.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000  Roby Joehanes (robbyjo@cis.ksu.edu)           *
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
import java.util.*;
import java.io.*;

import org.apache.log4j.Category;

/**
 * The BanderaUtil is a collection of often used methods
 * in Bandera.  It includes methods that make calls to the OS (exec),
 * manipulate files (copyFile, mkdir, etc.), and call the Java
 * compiler.
 *
 * @author Roby Joehanes &lt;robbyjo@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.5 $ - $Date: 2003/06/23 18:59:52 $
 */
public class BanderaUtil {

    private static final Category log = Category.getInstance(BanderaUtil.class);
    private static Runtime runtime = Runtime.getRuntime();

    public static boolean copyFile(File src, File dest)
    {
	if (!src.exists()) return false;
	if (src.isFile() && dest.isDirectory())
	    {
		dest = new File (dest.getAbsolutePath()+File.separator+src.getName());
	    }
	if (dest.exists()) dest.delete();
	FileInputStream  in = null;
	FileOutputStream out = null;
	try {
	    in  = new FileInputStream(src);
	    out = new FileOutputStream(dest);
	    byte[] buf = new byte[64*1024];
	    int i = 0;
	    while ((i = in.read(buf)) != -1)
		out.write(buf, 0, i);
	    in.close(); out.close();
	} catch (Exception e)
	    {
		try { if (in != null) in.close(); } catch (Exception ex) {}
		try { if (out != null) out.close(); } catch (Exception ex) {}
		return false;
	    }
	return true;
    }

    /**
     * 
     * @return boolean
     * @param dir java.io.File
     */
    public static boolean deleteDir(File dir) {
	if (!dir.exists()) return true;

	File[] files = dir.listFiles();
	if (files == null) return true;  // robbyjo's bugfix
	for (int i = 0; i < files.length; i++) {
	    if (files[i].isDirectory()) {
		deleteDir(files[i]);
	    } else {
		if (!files[i].delete()) {
		    return false;
		}
	    }
	}
	
	return dir.delete();
    }

    public static boolean deleteDir(String s) {
	return deleteDir(new File(s));
    }

    /**
     * Execute the given command placing the stdout into the output StringBuffer, the
     * stderr into the error StringBuffer, using the given level of verbose output.
     *
     * @param String command The command to execute.
     * @param StringBuffer output The buffer into which the stdout should be placed.
     * @param StringBuffer error The buffer into which the stderr should be placed.
     * @param int verbosity The amount of output that should be generated.
     * @return String The command line output.
     */
    public static String exec(String command, StringBuffer output, StringBuffer error, int verbosity) {
	try {
	    Process p = runtime.exec(command);
	    BufferedReader commandOut = new BufferedReader (new InputStreamReader(p.getInputStream()));
	    BufferedReader commandErr = new BufferedReader (new InputStreamReader(p.getErrorStream()));
	    String ln = System.getProperty("line.separator");

	    StringBuffer sb = new StringBuffer();
	    if (verbosity > 5) {
		//System.out.println(command);
		log.debug(command);
		output.append(command+ln);
	    }

	    // Quick and dirty patch
	    if (! (command.startsWith("gcc ") || (command.startsWith(Preferences.getCCAlias()) ) ) ) {
		    String line;
		    while ((line = commandOut.readLine()) != null) {
			sb.append(line+ln);
		    }
		    while ((line = commandErr.readLine()) != null) {
			sb.append(line+ln);
		    }
	    }
	    commandOut.close(); commandErr.close();

	    try {
		p.waitFor();
	    } catch (InterruptedException e) {
		//System.err.println("system error: process was interrupted");
		log.error("System error: Process was interrupted.", e);
	    }
  
	    String result = sb.toString();
	    if (verbosity > 5) {
		//System.out.println(result);
		log.debug(result);
	    }
	    return result;
	} catch (Exception e) {
	    log.error("An exception was thrown while executing the command '" + command + "'.", e);
	    throw new RuntimeException("exec of command '" + command + "' was aborted: \n" + e);
	}
    }

    /**
     * Execute the given command and return the output in String format.
     *
     * @param String command The command to execute.
     * @param boolean verbose Ignored.
     * @return String The output from the execution of the command.
     */
    public static String exec(String command, boolean verbose) {
	StringBuffer sb = new StringBuffer();
	return exec(command, sb, sb, 9);
    }

    public static void main(String[] args) {
	// For a test drive
	moveFile (new File("Driver.java"), new File("output"));
    }

    public static boolean mkdirs(String s) {
	File tempDir = new File(s);
	//String temp = "Error: Can't create temporary directory '" + tempDir.getAbsolutePath() + "'";
	//System.out.println("Making directory "+s);
	if (tempDir.exists() && !deleteDir(tempDir)) return false;
	try {
	    if (!tempDir.mkdirs()) return false;
	    if (!tempDir.exists()) {
		//System.out.println("Strangely enough, it doesn't exists!");
		log.error("Strangely enough, the temporary directory doesn't exist.");
	    }
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

    /**
     * Move the given file into the given directory.
     *
     * @param File src The file to move.
     * @param File dest The directory into which the file should be placed.
     */
    public static void moveFile(File src, File dest) {
	copyFile(src,dest); src.delete();
    }

    /**
     * Move the given file into the given directory.
     *
     * @param String file The file to move.
     * @param String dir The directory into which the file should be placed.
     */
    public static void moveFile(String file, String dir) {
	File f = new File(file);
	if (!f.exists() || dir == null || ("."+File.separator).equals(dir) || ".".equals(dir)) return;
	moveFile(f, new File(dir));
    }

    /**
     * Run the Java compiler (sun.tools.javac.Main.compile(...)) using the
     * given classpath and java source file name.
     *
     * @param classpath java.lang.String The classpath to use.
     * @param filename java.lang.String The java source file to compile.
     */
    public static void runJavac(String classpath, String filename) {
    }
}
