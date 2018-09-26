package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import ca.mcgill.sable.util.*;

import java.util.*;

import org.apache.log4j.Category;

/**
 * A BIR Thread, which represents a piece of active state.
 * <p>
 * Each BIR thread has:
 * <ul>
 * <li> A name
 * <li> An integer id
 * <li> A flag indicating whether it is the main thread (the
 * main thread is enabled in the initial state, other threads must be started)
 * <li> A set of locations, including a start location
 * </ul>
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.5 $ - $Date: 2003/06/19 15:33:23 $
 */
public class BirThread implements Cloneable {

	private static Category log = Category.getInstance(BirThread.class);
	protected TransSystem system;
	protected String name;
	/* [Thomas, May 21, 2017]
	 * Add sootClassName
	 * */
	protected String sootClassName;
	protected Location startLoc;
	protected int id;
	protected LocVector threadLocVector;
	protected boolean main = false;
	protected StateVarVector param = new StateVarVector();
	protected StateVarVector local = new StateVarVector();

	public BirThread(TransSystem sys, String name, int id, String sootClassName) {
		this.system = sys;
		this.name = name;
		this.id = id;
		this.sootClassName = sootClassName;
	}

	protected BirThread(BirThread other) {
		system = other.system;
		name = other.name;
		startLoc = other.startLoc;
		id = other.id;
		threadLocVector = other.threadLocVector;
		main = other.main;
		param = other.param;
		local = other.local;
		this.sootClassName = other.sootClassName;
	}

	public int getId() { return id; }
	public LocVector getLocations() { return threadLocVector; }
	public String getName() { return name; }
	public String getSootClassName() { return sootClassName; }
	public Location getStartLoc() { return startLoc; }
	public void addParameter(StateVar var) { param.addElement(var); }
	public void addLocal(StateVar var) {
		log.debug("Adding local: " + var);
		local.addElement(var);
	}
	public StateVarVector getParameters() { return param; }
	public StateVarVector getLocals() { return local; }
	public TransSystem getSystem() { return system; }
	public boolean isMain() { return main; }
	public void setLocations(LocVector v) { threadLocVector = v; }
	public void setMain(boolean isMain) { main = isMain; }
	public void setStartLoc(Location loc) { startLoc = loc; }
	public String toString() {  return "Thread" + id; }

	public Object clone() {
		return new BirThread(this);
	}

	/**
	 * Test the equality of an object to this BirThread.  This object
	 * is equal to another object if it is a BirThread and the id is
	 * the same.
	 *
	 * @param Object object The object to test for equality.
	 * @return boolean True if the object is equal (it is a BirThread and the id is equal), False otherwise.
	 */   
	public boolean equals(Object object) {
		if(object == null) {
			return(false);
		}

		if(object == this) {
			return(true);
		}

		if(object instanceof BirThread) {
			BirThread bt = (BirThread)object;
			if(bt.getId() == getId()) {
				return(true);
			}
		}
		return(false);
	}

	/**
	 * Get a hashcode for this object.  This uses the id and multiplies it by
	 * a constant number (31).
	 *
	 * @return int A hashcode for this object.
	 */
	public int hashCode() {
		return(getId() * 31);
	}
}
