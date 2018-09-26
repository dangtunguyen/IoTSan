/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import edu.ksu.cis.bandera.bui.*;
import edu.ksu.cis.bandera.util.*;
import edu.ksu.cis.bandera.report.*;
import javax.swing.plaf.metal.*;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;

import java.net.URL;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Appender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Priority;

/**
 * Bandera is the main class that will be run to start the Bandera application.  This
 * can start either the command line client (a.k.a. the batch utility) or the Bandera
 * GUI (a.k.a. BUI).  The main method will parse the command line arguments as well as
 * initialize the logging framework (log4j).
 * 
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.9 $ - $Date: 2003/06/13 19:00:14 $
 */
public class Bandera {
  private static final String sessionExtension = ".session";  // The default session file extension
  private static StringBuffer summary = new StringBuffer();
  private static boolean isDump;
  private static boolean genReport;
  private static String expectedPath;
  private static final String log4JConfigurationFilenameOption = "log4JPropertyFile";
  private static final String log4JConfigurationFilenameCommandLineOption = "-" + log4JConfigurationFilenameOption + "=";
  private static final String defaultLog4JConfigurationFilename = "log4j.properties";

  private static void doOneSession(Session ses) {
    if (ses == null)
      throw new RuntimeException("Cannot run null session!");
    //	CoreDriver driver = new CoreDriver(ses);
    Driver driver = new Driver();
    driver.initCmdLine(ses, isDump, genReport, expectedPath);
    summary.append("--------------------------------------------------------------------------------\n");
    summary.append("Session: " + ses.getName() + "\n\n");
    driver.run();
    //	summary.append(driver.getMessage());
    summary.append(driver.getSummary());
    driver = null;
    summary.append("\n--------------------------------------------------------------------------------\n");
  }
  private static void help()
  {
    System.out.println("Bandera parameter list:");
    System.out.println("   -h, --h, --help, -?, --?, -help Print this help screen.");
    System.out.println("   -s   {sesfile} [sesnames...] --> run Bandera with specified session file.");
    System.out.println("        If your session file doesn't end with " + sessionExtension + " Bandera will append it.");
    System.out.println("        If you omit the sesnames part, it will run *ALL* sessions in the");
    System.out.println("        session file.");
    System.out.println("        You can run only some sessions of the session file by listing it, e.g.:");
    System.out.println("          java Bandera -s test ses1 dedlok multitrd3");
    System.out.println("        This will run bandera using test." + sessionExtension + " and run");
    System.out.println("        ses1, dedlok, and multitrd3 sessions.\n");
    System.out.println("   -sd  does the same things as -s except it dumps Jimple and Java codes");
    System.out.println("        in intermediate steps and generate reports for all components.");
    System.out.println("   -sd=(JJJC|BSL|BOFA|Slicer|SLABS|Decompiler|BIRC)+");
    System.out.println("      	does the same things as -sd except it generate reports for the specified components only");
    System.out.println("   -st <path> does the same things as -sd except it generates report comparison");
    System.out.println("   -st=(JJJC|BSL|BOFA|Slicer|SLABS|Decompiler|BIRC)+ <path>");
    System.out.println("        does the same things as -st except it generate reports for the specified components only");
    System.out.println("   -r   {sesfile} [sesnames...] --> run Mr. Roboto with specified session file.");
    System.out.println("   -rd  does the same things as -r except it dumps Jimple and Java codes");
    System.out.println("        in intermediate steps and generate reports for all components.");
    System.out.println("   -rd=(JJJC|BSL|BOFA|Slicer|SLABS|Decompiler|BIRC)+");
    System.out.println("        does the same things as -rd except it it generate reports for the specified components only");
    System.out.println("   -rt <path> does the same things as -rd except it generates report comparison");
    System.out.println("   -rt=(JJJC|BSL|BOFA|Slicer|SLABS|Decompiler|BIRC)+ <path>");
    System.out.println("        does the same things as -rt except it generates reports for the specified components only");
    System.out.println("Running Bandera without parameter will bring you to the GUI directly.\nSee manual for details.");
  }
  /**
   * 
   * @param args java.lang.String[]
   */
  public static void main(String[] args) {
    BanderaTheme banderaTheme = new BanderaTheme();
    MetalLookAndFeel.setCurrentTheme(banderaTheme);
    try {
      /* Configure the Log4J system.  This will first check the command line for the property file.
       * If that doesn't exist, we  will check the system properties.  If that doesn't exist, we
       * will check for the default configuration file.  If all else fails, we will use the
       * hard-coded configuration.
       */
      String log4JConfigurationFilename = null;

      // walk through the command line options and file the name of the file
      for (int i = 0; i < args.length; i++) {
        if (args[i].startsWith(log4JConfigurationFilenameCommandLineOption)) {
          // found it, grab it and break the loop
          log4JConfigurationFilename =
              args[i].substring(log4JConfigurationFilenameCommandLineOption.length());

          /* remove this option from the argument list.  this is a HACK and should be
           * replaced.  this makes it work with the existing command line parsing logic.
           */
          String[] newArgs = new String[args.length - 1];
          for(int j = 0, next = 0; j < args.length; j++) {
            if(j != i) {
              newArgs[next] = args[j];
              next++;
            }
          }
          args = newArgs;
          /* end HACK */

          break;
        }
      }

      // if not found on the command line, look at the system properties
      if (log4JConfigurationFilename == null) {
        try {
          log4JConfigurationFilename =
              System.getProperty(log4JConfigurationFilenameOption, null);
        }
        catch (Exception e) {
          // probably only a security exception ... meaning we aren't allow to query the system properties.
          log4JConfigurationFilename = null;
        }
      }

      // if not found on the command line or in the system properties, try to get the default file
      if (log4JConfigurationFilename == null) {
        try {
          URL log4JPropertyFileURL =
              Bandera.class.getClassLoader().getResource(defaultLog4JConfigurationFilename);
          if (log4JPropertyFileURL == null) {
            log4JConfigurationFilename = null;
          }
          else {
            log4JConfigurationFilename = defaultLog4JConfigurationFilename;
          }
        }
        catch (Exception e) {
          log4JConfigurationFilename = null;
        }
      }

      // if not found on the command line or in the system properties and no default file is found,
      //  use these hard-coded values
      if (log4JConfigurationFilename == null) {
        /* Update this to the newest API of Log4J -> Logger/Level -tcw */

        System.out.println("Configuring using the hard-coded log configuration values.  " +
            "Setting the level to INFO.");
        Category log = Category.getRoot();
        log.setPriority(Priority.INFO);
        String pattern = "%-6r [%p] %t::%c - %m%n";
        Layout layout = new PatternLayout(pattern);
        Appender appender = new ConsoleAppender(layout);
        log.addAppender(appender);
      }
      else {
        // configure using the given filename
        System.out.println("Configuring using this file: " + log4JConfigurationFilename);
        URL log4JConfigurationURL =
            ClassLoader.getSystemClassLoader().getResource(log4JConfigurationFilename);
        PropertyConfigurator.configure(log4JConfigurationURL);
      }
    }
    catch (Exception e) {
      System.err.println("A fatal error occured while attempting to initialize the logging system.  " +
          "Stopping Bandera.");
      return;
    }
    /* end - configure logging system */

    if (args.length == 0) {
      edu.ksu.cis.bandera.bui.BUI.main(args);
    } else {
      // This is where help is
      if ((args.length == 1) && ((args[0].equals("-h")) || (args[0].equals("--help")) || (args[0].equals("-help"))
          || (args[0].equals("-?")) || (args[0].equals("--h") || (args[0].equals("--?"))))) {
        help();
        return;
      }

      // This is the real cmd line driver
      if ((args.length >= 2) && (args[0].equals("-s") || args[0].equals("-sd") || args[0].equals("-st")
          || args[0].startsWith("-sd=") || args[0].startsWith("-st="))) {
        Preferences.load();
        //Logger.on();
        // The second or third parameter would be the session file
        int sessionIdx = 1;
        if (args[0].startsWith("-sd"))
          isDump = true;
        else if (args[0].startsWith("-st")) {
          isDump = true;
          genReport = true;
          expectedPath = args[1];
          sessionIdx = 2;
          if (args.length < 3) {
            help();
            return;
          }
        }
        if (args[0].equals("-st") || args[0].equals("-sd")) {
          ReportManager.setGenerateJJJCReport(true);
          ReportManager.setGenerateBSLReport(true);
          ReportManager.setGenerateBOFAReport(true);
          ReportManager.setGenerateSlicerReport(true);
          ReportManager.setGenerateSLABSReport(true);
          ReportManager.setGenerateDecompilerReport(true);
          ReportManager.setGenerateBIRCReport(true);				
        } else if (args[0].startsWith("-st=") || args[0].startsWith("-sd=")) {
          for (StringTokenizer st = new StringTokenizer(args[0].substring(4), "+"); st.hasMoreTokens();) {
            String token = st.nextToken();
            if ("JJJC".equals(token)) {
              ReportManager.setGenerateJJJCReport(true);
            } else if ("BSL".equals(token)) {
              ReportManager.setGenerateBSLReport(true);
            } else if ("BOFA".equals(token)) {
              ReportManager.setGenerateBOFAReport(true);
            } else if ("Slicer".equals(token)) {
              ReportManager.setGenerateSlicerReport(true);
            } else if ("SLABS".equals(token)) {
              ReportManager.setGenerateSLABSReport(true);
            } else if ("Decompiler".equals(token)) {
              ReportManager.setGenerateDecompilerReport(true);
            } else if ("BIRC".equals(token)) {
              ReportManager.setGenerateBIRCReport(true);				
            } else {
              help();
              return;
            }
          }
          args[0] = args[0].substring(0, 3);
        }
        String st;
        if (args[sessionIdx].endsWith(sessionExtension))
          st = args[sessionIdx];
        else
          st = args[sessionIdx] + sessionExtension;

        // Session filename already parsed
        try {
          SessionManager sm = SessionManager.getInstance();

          try {
            sm.load(st);
          }
          catch(Exception e) {
            throw new Exception("An exception (" + e.toString() +
                ") occured while loading the session file: " + st + ".", e);
          }

          summary.append("Execution Summary:\n\nSession file " + st + " is successfully loaded.\n\n");

          if (args.length == sessionIdx + 1) {
            // since no sessions were specified, we will run them all
            Map sessionMap = sm.getSessions();
            if((sessionMap != null) && (sessionMap.size() > 0)) {
              Iterator sessionIDIterator = sessionMap.keySet().iterator();
              while(sessionIDIterator.hasNext()) {
                String sessionID = (String)sessionIDIterator.next();
                Session session = (Session)sessionMap.get(sessionID);
                doOneSession(session);
              }
            }
            else {
              // report this to the user!
            }
          }
          else {
            // a list of sessions to run was defined on the command line ... just run those.
            List sessionIDList = new ArrayList();
            for (int i = sessionIdx + 1; i < args.length; i++) {
              sessionIDList.add(args[i]);
            }
            List sessions = sm.selectSessions(sessionIDList);
            if((sessions != null) && (sessions.size() > 0)) {
              for(int i = 0; i < sessions.size(); i++) {
                Session currentSession = (Session)sessions.get(i);
                doOneSession(currentSession);
              }
            }
          }

        }
        catch (Exception e) {
          summary.append("An exception (" + e.toString() + ") occured while running the sessions.");
        }

        // Print out the summary of execution
        if (genReport)
          edu.ksu.cis.bandera.report.HTMLReportGenerator.generateSummary(st, "index.html");
        System.out.println(summary);
        //Logger.off();
        Preferences.save();
        System.exit(0);
      } else
        if ((args.length >= 2) && (args[0].equals("-r") || args[0].equals("-rd") || args[0].equals("-rt")
            || args[0].startsWith("-rd=") || args[0].startsWith("-rt="))) {
          Preferences.load();
          //Logger.on();
          // The second or third parameter would be the session file
          int sessionIdx = 1;
          if (args[0].startsWith("-rd"))
            isDump = true;
          if (args[0].startsWith("-rt")) {
            isDump = true;
            genReport = true;
            expectedPath = args[1];
            sessionIdx = 2;
            if (args.length < 3) {
              help();
              return;
            }
          }
          if (args[0].equals("-rt") || args[0].equals("-rd")) {
            ReportManager.setGenerateJJJCReport(true);
            ReportManager.setGenerateBSLReport(true);
            ReportManager.setGenerateBOFAReport(true);
            ReportManager.setGenerateSlicerReport(true);
            ReportManager.setGenerateSLABSReport(true);
            ReportManager.setGenerateDecompilerReport(true);
            ReportManager.setGenerateBIRCReport(true);				
          } else if (args[0].startsWith("-rt=") || args[0].startsWith("-rd=")) {
            for (StringTokenizer st = new StringTokenizer(args[0].substring(4), "+"); st.hasMoreTokens();) {
              String token = st.nextToken();
              if ("JJJC".equals(token)) {
                ReportManager.setGenerateJJJCReport(true);
              } else if ("BSL".equals(token)) {
                ReportManager.setGenerateBSLReport(true);
              } else if ("BOFA".equals(token)) {
                ReportManager.setGenerateBOFAReport(true);
              } else if ("Slicer".equals(token)) {
                ReportManager.setGenerateSlicerReport(true);
              } else if ("SLABS".equals(token)) {
                ReportManager.setGenerateSLABSReport(true);
              } else if ("Decompiler".equals(token)) {
                ReportManager.setGenerateDecompilerReport(true);
              } else if ("BIRC".equals(token)) {
                ReportManager.setGenerateBIRCReport(true);				
              } else {
                help();
                return;
              }
            }
            args[0] = args[0].substring(0, 3);
          }
          String st;
          if (args[sessionIdx].endsWith(sessionExtension))
            st = args[sessionIdx];
          else
            st = args[sessionIdx] + sessionExtension;

          // Session filename already parsed
          try {
            SessionManager sm = SessionManager.getInstance();

            try {
              sm.load(st);
            }
            catch(Exception e) {
              throw new Exception("An exception (" + e.toString() +
                  ") occured while loading the session file: " + st + ".", e);
            }

            summary.append("Execution Summary:\n\nSession file " + st + " is successfully loaded.\n\n");

            Vector sessionNames = new Vector();
            // If no session name mentioned, just run every session inside that file.
            if (args.length == 2) {
              Map sessionMap = sm.getSessions();
              if((sessionMap != null) && (sessionMap.size() > 0)) {
                Iterator sessionIDIterator = sessionMap.keySet().iterator();
                while(sessionIDIterator.hasNext()) {
                  String sessionID = (String)sessionIDIterator.next();
                  sessionNames.add(sessionID);
                }
              }
            }
            else {
              // Otherwise, iterate the args[2,3,...] for the session name wanted
              for (int i = 0; i < args.length - 2; i++) {
                Session s = sm.getSession(args[i + 2]);
                if (s != null)
                  sessionNames.add(s.getName());
                else
                  System.out.println("Session " + args[i + 2] + " is not found!\n");
              }
            }
            edu.ksu.cis.bandera.bui.BUI.main(new String[0]);
            new edu.ksu.cis.bandera.bui.roboto.MrRoboto(st, sessionNames, isDump, genReport, expectedPath).run();
          } catch (Exception e) {
            summary.append("Error: " + e.getMessage());
          }
          // Print out the summary of execution
          if (genReport)
            edu.ksu.cis.bandera.report.HTMLReportGenerator.generateSummary(st, "index.html");
          System.out.println(summary);
          //Logger.off();
          Preferences.save();
          System.exit(0);
        } else { // User says something else, so error...
          System.out.println("Error! See help for details!");
          help();
        }
    }
  }
}
