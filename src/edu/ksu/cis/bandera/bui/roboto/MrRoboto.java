package edu.ksu.cis.bandera.bui.roboto;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001   Robby (robby@cis.ksu.edu)                    *
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
import javax.swing.*;
import java.io.*;
import java.util.*;
import edu.ksu.cis.bandera.bui.*;
import edu.ksu.cis.bandera.bui.counterexample.*;
import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;

public class MrRoboto extends Thread {
	private Vector sessionNames;
	private java.lang.String sessionFile;
	private boolean dump;
	private boolean report;
	private String expectedPath;
/**
 * 
 * @param sessionFile java.lang.String
 * @param sessionNames java.util.Vector
 * @param dump boolean
 * @param report boolean
 * @param expectedPath java.lang.String
 */
public MrRoboto(String sessionFile, Vector sessionNames, boolean dump, boolean report, String expectedPath) {
	this.sessionFile = sessionFile;
	this.sessionNames = sessionNames;
	this.dump = dump;
	this.report = report;
	this.expectedPath = expectedPath;
}
/**
 * 
 */
public void run() {
	// wait for the BUI to load up
	System.out.println("MrRoboto is running...");
	Driver.initRoboto(dump, report, expectedPath);
	while (BUI.bui == null) {
		try {
			sleep(10000);
		} catch (Exception e) {
		}
	}

	SessionManager sm = SessionManager.getInstance();

	// load session file
	try {
	    sm.load(sessionFile);
	}
	catch(Exception e) {
	    System.out.println("MrRoboto failed to open the session file (" + sessionFile + ").  Quitting.");
	    return;
	}
	System.out.println("MrRoboto opened the session file...");

	// open session manager
	BUI.bui.sessionManagerAction();
	System.out.println("MrRoboto opened the session manager...");
	//try { sleep(2000); } catch (Exception e) {}

	// run the specified sessions
	for (Iterator i = sessionNames.iterator(); i.hasNext();) {
	    String sessionName = (String)i.next();
	    Session s = sm.getSession(sessionName);
	    System.out.println("MrRoboto told Bandera to run the " + s.getName() + " session...");
	    System.out.println("MrRoboto is taking a nap while Bandera is running the " + s.getName() + " session");
	    BUI.bui.runBanderaAction();
	    //try { sleep(2000); } catch (Exception e) {}
	    while (BUI.isExecuting) {
		try {
		    sleep(5000);
		} catch (Exception e) {
		}
	    }
	}
	System.out.println("MrRoboto's job is finished... back to sleep.");
}
}
