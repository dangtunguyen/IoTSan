package edu.ksu.cis.bandera.bui.counterexample;

import java.util.*;

public class Trail {
    private List trails;
    private String sessionName;
    private String sessionFile;
    private String type;

public Trail(String t, String f, String n, List l)
{
    if (t == null || f == null || n == null || l == null)
        throw new RuntimeException("Null trail!");
    type = t; sessionName = n; sessionFile = f; trails = l;
}

	/**
	 * Returns the sessionFile.
	 * @return String
	 */
	public String getSessionFile() {
		return sessionFile;
	}

	/**
	 * Returns the sessionName.
	 * @return String
	 */
	public String getSessionName() {
		return sessionName;
	}

	/**
	 * Returns the trails.
	 * @return List
	 */
	public List getTrails() {
		return trails;
	}

	/**
	 * Returns the type.
	 * @return String
	 */
	public String getType() {
		return type;
	}

    public String toString()
    {
        return "counter example: "+type+" session name: "+sessionName+ " file: "+sessionFile;
    }
}
