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
import ca.mcgill.sable.soot.SootField;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.util.*;

import java.util.*;

/**
 * A class literal value in a Jimple trace
 */

public class ClassLiteral implements ObjectLiteral {

	Hashtable fieldContents = new Hashtable();
	LockLiteral lockValue;
	Vector fields = new Vector();
	SootClass source;
	int id;

	public ClassLiteral(SootClass source, int id) {  
	this.source = source;
	this.id = id;
	}
	public void apply(Switch sw)
	{
		((JimpleLiteralSwitch) sw).caseClassLiteral(this);
	}
	/**
	 * Get a Vector of the SootFields stored in this literal
	 * <p>
	 * All these fields will be non-static, but some may belong
	 * to superclasses of the class of this literal.
	 */

	public Vector getFields() { return fields; }
	/**
	 * Get the value of a specific Soot field
	 * @param field a (non-static) Soot Field stored in the literal
	 */

	public JimpleLiteral getFieldValue(SootField field) {
	return (JimpleLiteral) fieldContents.get(field);
	}
	/**
	 * Get the unique object ID of this instance
	 */

	public int getId() { return id; }
	/**
	 * Get the value of the lock of this object (returns null if the
	 * object has no lock---i.e., was never used in a synchronized block).
	 */

	public LockLiteral getLockValue() { return lockValue; }
	/**
	 * Get the SootClass of which this literal is an instance
	 */

	public SootClass getSource() { return source; }
	public void setFieldValue(SootField field, JimpleLiteral value) {
	if (fieldContents.get(field) == null)
	    fields.addElement(field);
	fieldContents.put(field,value);
	}
	public void setLockValue(JimpleLiteral value) {
	lockValue = (LockLiteral) value;
	}
}
