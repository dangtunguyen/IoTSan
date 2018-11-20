package edu.ksu.cis.bandera.smv;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999   Matthew Dwyer (dwyer@cis.ksu.edu)            *
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
/**
 * SmvOptions is used to configure the options to be used in invoking
 * the SMV model checker on generated TRANS systems.
 * <p>
 * Most user setable options available through the SMV interface
 * can be configured through this class.  Meaning and default values
 
 * for those options are as follows: 
 * <ul> 
 * <li> <tt>Safety</tt> : property is safety or liveness (defaults to true, i.e., safety)
 * <li> <tt>Invariant</tt> : CTL property of form AGp (i.e., an invariant)
 * <li> <tt>Property</tt> : CTL property or deadlock (defaults to false, i.e., deadlock)
 * <li> <tt>Interleaving</tt> : Interleaving or simultaneous system (defaults to true, i.e., interleaving)
 * </ul>
 */

import edu.ksu.cis.bandera.checker.*;

public class SmvOptions extends CheckerOption
{
	boolean property = false;
	boolean safety = true;
	boolean interleaving = true;
	boolean invariant = false;

	public SmvOptions() { super(); }
	public SmvOptions(String s) { super(s); }
	public boolean getInterleaving() { return interleaving; }
	public boolean getInvariant() { return invariant; }
	public boolean getProperty() { return property; }
	public boolean getSafety() { return safety; }
	public void parseOptions(String s) {}
		public String runOptions() {
		String co = "";
		if (invariant) co = co + "-AG ";
		return co;
		}
	public void setInterleaving(boolean s) { 
	interleaving = s; 
	}
	public void setInvariant(boolean s) { 
	invariant = s; 
	}
	public void setProperty(boolean s) { 
	property = s; 
	}
	public void setSafety(boolean s) { 
	safety = s; 
	}
}
