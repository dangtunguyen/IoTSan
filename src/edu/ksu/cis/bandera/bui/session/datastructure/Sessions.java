package edu.ksu.cis.bandera.bui.session.datastructure;

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
import java.util.*;
public class Sessions {
	private String filename = "";
	private TreeSet sortedSet = new TreeSet();
	private Session activeSession;
	private boolean saved = false;
/**
 * 
 * @return edu.ksu.cis.bandera.bui.session.Session
 */
public Session getActiveSession() {
	return activeSession;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getFilename() {
	return filename;
}
/**
 * 
 * @return edu.ksu.cis.bandera.bui.datastructure.Session
 * @param name java.lang.String
 */
public Session getSession(String name) {
	for (Iterator i = sortedSet.iterator(); i.hasNext();) {
		Session session = (Session) i.next();
		if (session.getName().equals(name))
			return session;
	}
	return null;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getSessions() {
	return new Vector(sortedSet);
}
/**
 * 
 * @return boolean
 */
public boolean isSaved() {
	return saved;
}
/**
 * 
 * @param session edu.ksu.cis.bandera.bui.datastructure.Session
 */
public void putSession(Session session) {
	sortedSet.add(session);
}
/**
 * 
 * @param session edu.ksu.cis.bandera.bui.datastructure.Session
 */
public void removeSession(Session session) {
	sortedSet.remove(session);
}
/**
 * 
 * @param newActiveSession edu.ksu.cis.bandera.bui.session.Session
 */
public void setActiveSession(Session newActiveSession) {
	activeSession = newActiveSession;
}
/**
 * 
 * @param newFilename java.lang.String
 */
public void setFilename(java.lang.String newFilename) {
	filename = newFilename;
}
/**
 * 
 * @param newSaved boolean
 */
public void setSaved(boolean newSaved) {
	saved = newSaved;
}
}
