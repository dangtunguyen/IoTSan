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
import java.io.*;
import java.util.*;
import edu.ksu.cis.bandera.bui.session.datastructure.*;

/**
 * This is a logging and bug-reporting tool. Creates log files under (USER_HOME_DIR)/.bandera/log/*.log.
*/

public class Logger {
	private static PrintStream stdout = null;
	private static PrintStream stderr = null;
	private static boolean keepLog = true;
	private static BanderaLog theLog = null;
	private static File logDir = new File(Preferences.getUserPrefDir() + File.separator + "log");
	private static String logPath = logDir.getAbsolutePath() + File.separator;
	public static void dump(Session s)
	{
		theLog.setInternal(true);
		System.out.println("User select the following session: N/A");
		//String st = s.getStringRepresentation();
		//if (st!=null) System.out.println(st);
		theLog.setInternal(false);
	}
public static void keep() { keepLog = true; }
public static void off()
{
	// Return system's standard out and standard error
	System.setOut(stdout);
	System.setErr(stderr);

	// Flush and close the interceptor
	theLog.flush();
	theLog.close();

	// If we don't keep the log, delete it.
	if (!keepLog)
	{
		try
		{
			File logFile = new File(logPath + "current.log");
			if (logFile.exists()) { logFile.delete(); }
		} catch (Exception e)
		{
			System.out.println("WARNING: Cannot delete unused log data! You may want to delete current.log manually.");
		}
	} else renameCurrentLog();
}
public static void on()
{
	try
	{
		if (!logDir.exists() && !logDir.mkdir()) throw new Exception("Error: Can't setup log directory");
		renameCurrentLog();

		// Save system's output and errors.
		stdout = System.out;
		stderr = System.err;

		PrintStream ps = new PrintStream(new FileOutputStream(logPath + "current.log"));

		theLog = new BanderaLog(stdout, ps);

		// Set the marks
		ps.println("This log file is created at " + Calendar.getInstance().getTime().toString());
		ps.println("------------------------------------------------------------------------");
		ps.println("System properties are:\n");
		System.getProperties().list(ps);
		ps.println("------------------------------------------------------------------------");
		ps.println("User's preferences are:\n");
		Preferences.getProperties().list(ps);
		ps.println("------------------------------------------------------------------------");
		ps.println("The log begins here:\n");

		// Intercept system's output.
		System.setOut(theLog);
		System.setErr(theLog);
	} catch (Exception e)
	{
		System.out.println(e.getMessage());
		System.out.println("Error: Unable to setup Bandera logging system! Aborting...");
		System.exit(0);
	}
}
private static void renameCurrentLog() throws RuntimeException
{
	File logFile = new File(logPath + "current.log");

	// If the log file exists, it may be the remnant of previous run-time failure. So, keep it.
	if (logFile.exists())
	{
		int i = 0;
		File f = null;
		do {
			f = new File(logPath + String.valueOf(i) + ".log");
			i++;
		} while (f.exists());
		if (f != null)
			logFile.renameTo(f);
		else
			throw new RuntimeException("Error: Unable to rename previous log file. Try renaming current.log!");
	}
}
}
