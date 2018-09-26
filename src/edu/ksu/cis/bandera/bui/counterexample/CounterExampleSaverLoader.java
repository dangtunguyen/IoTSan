package edu.ksu.cis.bandera.bui.counterexample;

import java.io.*;
import java.util.*;

import edu.ksu.cis.bandera.bui.*;
import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;

public class CounterExampleSaverLoader {

    public static void save(String filename, List trail)
    {
        if (trail == null) throw new RuntimeException("No trails to save!");

        StringWriter buf = new StringWriter();
        int errorSize = trail.size();
        String ln = System.getProperty("line.separator");
        try {
	    SessionManager sm = SessionManager.getInstance();
	    Session ses = sm.getActiveSession();
	    String sesfn = ses.getMainClassFile().toString();
            String sesname = ses.getName();
            String type = ses.getProperty(Session.CHECKER_NAME_PROPERTY);
	    if((type == null) || (type.length() <= 0)) {
		throw new RuntimeException("Unknown counter example type.");
	    }
	    else {
		type = type.toLowerCase();
	    }

            buf.write("<session type="+type+" name="+sesname+" file=\""+sesfn+"\" />"+ln);
            for (int i = 0; i < errorSize; i++)
            {
                buf.write("<trail no="+i+">"+ln);
                buf.write(trail.get(i).toString());
                buf.write("</trail>"+ln);
            }
            PrintStream f = new PrintStream(new FileOutputStream(filename));
            f.println(buf.toString());
            f.close();

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Trail load(String filename)
    {
        try {
            LineNumberReader f = new LineNumberReader(new FileReader(filename));
            String s = "";
            String type = null;
            String sessionName = null;
            String sessionFile = null;
            String ln = System.getProperty("line.separator");
            LinkedList ctrex = new LinkedList();
            StringTokenizer tok;
            int size = 0;
            StringBuffer buf = null;
            int state = 0;

            do {
                s = f.readLine();
                if (s == null) break;
            } while (s.indexOf("session") == -1);

            if (s == null) throw new RuntimeException("Invalid trail format!");
            tok = new StringTokenizer(s);
            s = s.trim();
            while (tok.hasMoreTokens())
            {
                String t = tok.nextToken();
                if (t.startsWith("type="))
                {
                    type = t.substring(5).trim();
                    if (type.endsWith("/>"))
                        type = type.substring(0, type.length() - 2);
                } else if (t.startsWith("name="))
                {
                    sessionName = t.substring(5).trim();
                    if (sessionName.endsWith("/>"))
                        sessionName = sessionName.substring(0, sessionName.length() - 2);
                } else if (t.startsWith("file="))
                {
                    sessionFile = t.substring(6).trim();
                    if (sessionFile.endsWith("/>"))
                        sessionFile = sessionFile.substring(0, sessionFile.length() - 2);
                    sessionFile = sessionFile.substring(0, sessionFile.length() - 1);
                }
            }

            if (type == null) throw new RuntimeException("Trail type is unknown!");
            if (sessionName == null) throw new RuntimeException("Session name is unknown!");
            if (sessionFile == null) throw new RuntimeException("Session file is unknown!");

            do {
                s = f.readLine();
                if (s == null) break;
                s = s.trim();
                if (s.startsWith("//")) continue;
                switch(state)
                {
                    case 0:
                        if (!s.startsWith("<trail")) continue;
                        size++; state = 1; buf = new StringBuffer();
                        continue;
                    case 1:
                        if (s.startsWith("</trail"))
                        {
                            state = 0; ctrex.add(buf.toString());
                            continue;
                        }
                        buf.append(s+ln);
                }
            } while (s != null);  // If the string is null, then it's EOF.

            // Close when done.
            f.close();
            return new Trail(type, sessionFile, sessionName, ctrex);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
