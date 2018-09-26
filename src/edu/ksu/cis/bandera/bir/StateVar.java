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

import java.io.*;
import java.util.*;

/**
 * State variable.
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:32:51 $
 */
public class StateVar implements Expr, BirConstants {

	int id;                     // Unique id
	String name;                // Name of variable
	BirThread thread;           // Thread is local to (null if global)
	boolean constant;           // Is a constant (set exactly once)
	Type type;                  // Type of variable
	Expr initVal;               // Initial value
	TransSystem system;         // TransSystem belonging to
	int offset;                 // Offset in BirState 
	int actualSize;             // Actual size (of collection) in BirTrace 
	int actualBaseTypeSize;     // Actual size (of array collect) in trace
	int actualBaseTypeExtent;   // Actual extent in trace

	/* [Thomas, May 24, 2017]
	 * */
	boolean isGlobal;
	boolean isLocal;

	static int varCount = 0;    // Number of variables allocated


	/**
	 * Create a state variable.
	 * @param name name of variable 
	 * @param thread enclosing thread (if null, then var is global)
	 * @param type type of variable
	 * @param initVal the initial value (if null, default for type is used) 
	 */
	public StateVar(String name, BirThread thread, Type type, 
			Expr initVal, TransSystem system) {
		this.name = name;
		this.id = ++varCount;
		this.thread = thread;
		this.type = type;
		this.system = system;
		if (initVal != null)
			this.initVal = initVal;
		else
			this.initVal = type.defaultVal();

		globalLocal = false;
		this.isGlobal = false;
		this.isLocal = false;
	}
	protected StateVar(StateVar other) {
		id = other.id;
		name = other.name;
		thread = other.thread;    
		constant = other.constant;
		type = other.type;
		initVal = other.initVal;
		system = other.system;
		offset = other.offset;
		actualSize = other.actualSize;
		actualBaseTypeSize = other.actualBaseTypeSize;
		actualBaseTypeExtent = other.actualBaseTypeExtent;

		globalLocal = false;
		this.isGlobal = false;
		this.isLocal = false;
	}
	public void apply(Switch sw)
	{
		((ExprSwitch) sw).caseStateVar(this);
	}
	public int getActualBaseTypeExtent() { return actualBaseTypeExtent; }
	public int getActualBaseTypeSize() { return actualBaseTypeSize; }
	public int getActualSize() { return actualSize; }
	public Expr getInitVal() { return initVal; }
	public String getName() { return name; }
	public int getOffset() { return offset; }
	public TransSystem getSystem() { return system; }
	public BirThread getThread() { return thread; }
	public Type getType() { return type; }
	public boolean isConstant() { return constant; }
	public boolean isLocal() { return (thread != null); }
	public void print() {
		System.out.print(getName());
	}
	public void setActualBaseTypeExtent(int actualBaseTypeExtent) { 
		this.actualBaseTypeExtent = actualBaseTypeExtent; 
	}
	public void setActualBaseTypeSize(int actualBaseTypeSize) { 
		this.actualBaseTypeSize = actualBaseTypeSize; 
	}
	public void setActualSize(int actualSize) { this.actualSize = actualSize; }
	public void setConstant(boolean constant) { this.constant = constant; }
	public void setInitVal(Expr val) { this.initVal = val; }
	public void setOffset(int offset) { this.offset = offset; }
	public void setThread(BirThread thread) { this.thread = thread; }
	public String toString() {
		String isLocal = isLocal() ? "" : "* ";
		return "<StateVar " + id + " " + name + " " 
		+ isLocal + type.toString() +  ">";
	}

	/* ********************************************************************
	 * The following additions were made to fix a bug with the generation
	 * of the predicates in Spin.  They mark a StateVar as being associated
	 * with a particular process/thread as well as marking it so that it
	 * can be exported to be a global variable instead of a local.  This
	 * allows a claim to make upon a local.
	 * ******************************************************************* */

	/**
	 * The globalLocal flag marks this StateVar as being referenced in
	 * a global fashion.  In other words, a predicate makes reference
	 * to this local variable so it is migrated out as a global.
	 */
	private boolean globalLocal;

	/**
	 * The globalLocalPid holds the pid for the instance of the process/thread
	 * this global local is associated with.
	 */
	private int globalLocalPid;

	/**
	 * Determine if this StateVar is considered a global local or not.  A
	 * global local is a variable that is declared as a local but must be
	 * moved (or exported) to be a global so that it can be referenced
	 * in a predicate.
	 */
	public boolean isGlobalLocal() {
		return(globalLocal);
	}

	/**
	 * Mark this StateVar so it is considered a global local.  To see
	 * what a global local is look at the description of the method
	 * isGlobalLocal.
	 */
	public void markAsGlobalLocal() {
		setGlobalLocal(true);
	}

	/**
	 * Mark this StateVar so it is not considered a global local.  To see
	 * what a global local is look at the description of the method
	 * isGlobalLocal.
	 */
	public void unmarkAsGlobalLocal() {
		setGlobalLocal(false);
	}

	/**
	 * Set the global local flag for this StateVar.  You should
	 * probably call mark or unmark instead of using this method.  To see
	 * what a global local is look at the description of the method
	 * isGlobalLocal.
	 */
	public void setGlobalLocal(boolean globalLocal) {
		this.globalLocal = globalLocal;
	}

	/**
	 * Get the pid that this StateVar is associated with.
	 */
	public int getGlobalLocalPid() {
		return(globalLocalPid);
	}

	/**
	 * Set the pid that this StateVar is associated with.
	 */
	public void setGlobalLocalPid(int globalLocalPid) {
		this.globalLocalPid = globalLocalPid;
	}
	
	/* [Thomas, May 24, 2017]
	 * */
	public void setIsGlobal()
	{
		this.isGlobal = true;
	}
	public boolean getIsGlobal()
	{
		return this.isGlobal;
	}
	public void setIsLocal()
	{
		this.isLocal = true;
	}
	public boolean getIsLocal()
	{
		return this.isLocal;
	}
}
