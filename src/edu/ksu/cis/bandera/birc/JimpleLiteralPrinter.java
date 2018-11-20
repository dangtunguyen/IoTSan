package edu.ksu.cis.bandera.birc;

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
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.SootField;

import edu.ksu.cis.bandera.bir.ActiveThread;

import java.util.*;

/**
 * A visitor that prints JimpleLiterals neatly (using the notation #i
 * to print references to objects).
 * <p>
 * Demonstrates the use of JimpleLiteralSwitch to visit different
 * types of literals.
 */

public class JimpleLiteralPrinter implements JimpleLiteralSwitch {
	
	Hashtable objectTable =   // Map (ClassLit or ArrayLit) --> Integer
	new Hashtable();  
	int objectCount = 0;
	Vector printQueue = new Vector();
	String prefix;

	public JimpleLiteralPrinter(String prefix) {
	this.prefix = prefix;
	}
	public void caseArrayLiteral(ArrayLiteral literal) { 
	System.out.print("[ ");
	for (int i = 0; i < literal.length; i++) {
	    if (i > 0)
		System.out.print(", ");
	    literal.contents[i].apply(this);
	}
	System.out.print(" ]");
	}
	public void caseBooleanLiteral(BooleanLiteral literal) { 
	System.out.print("" + literal.value);
	}
	public void caseClassLiteral(ClassLiteral literal) { 
	Vector fields = literal.getFields();
	String className = literal.getSource().getName();
	System.out.print(className + " { ");
	for (int i = 0; i < fields.size(); i++) {
	    SootField field = (SootField) fields.elementAt(i);
	    if (i > 0)
		System.out.print(", ");
	    System.out.print(field.getName() + " = ");
	    literal.getFieldValue(field).apply(this);
	}
	System.out.print(" } ");
	LockLiteral lock = literal.getLockValue();
	if (lock != null) 
	    lock.apply(this);
	}
	public void caseIntegerLiteral(IntegerLiteral literal) { 
	System.out.print("" + literal.value);
	}
	public void caseLockLiteral(LockLiteral literal) { 
	ActiveThread holdingThread = literal.getHoldingThread();
	String holding = "-";
	if (holdingThread != null)
	    holding = String.valueOf(holdingThread.getTid());
	String waiting = ";";
	for (int i = 0; i < literal.getNumWaiting(); i++) {
	    if (i > 0)
		waiting += ",";
	    waiting += String.valueOf(literal.getWaitingThread(i).getTid());
	}	    
	System.out.print("<" + holding + waiting + ">");
	}
	public void caseReferenceLiteral(ReferenceLiteral literal) { 
	if (literal.value == null)
	    System.out.print("null");
	else {
	    Integer objectNum = (Integer) objectTable.get(literal.value);
	    if (objectNum == null) {
		objectNum = new Integer(++objectCount);
		objectTable.put(literal.value,objectNum);
		printQueue.addElement(literal.value);
	    }
	    // System.out.print("#" + objectNum.intValue());
	    int objectID = ((ObjectLiteral)literal.value).getId();
	    System.out.print("#" + objectID);
	}
	}
	/**
	 * Flush output
	 * <p>
	 * This method should be called after all static fields and locals
	 * have been printed using the object---it will print any objects
	 * that these variables were pointing to (as well as any objects
	 * those objects point to, etc.).
	 */

	public void flush() {
	while (printQueue.size() > 0) {
	    ObjectLiteral lit = (ObjectLiteral) printQueue.elementAt(0);
	    printQueue.removeElementAt(0);
	    System.out.print(prefix + "#" + lit.getId() + " = ");
	    lit.apply(this);
	    System.out.println();
	}
	}
}
