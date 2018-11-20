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
 * A record field.
 */

public class Field extends Type implements BirConstants {

	String name;
	Type type;
	Record record;
	int offset;

	public Field(String name, Type type, Record record) {
	this.name = name;
	this.type = type;
	this.record = record;
	}
	public void apply(TypeSwitch sw, Object o)
	{
		sw.caseField(this, o);
	}
	public Expr defaultVal() { return null; }
	/**
	 * Two fields are equal if they have the same name and type.
	 */
	public boolean equals(Object o) {
	return (o instanceof Field) && ((Field)o).name.equals(this.name)
	    && ((Field)o).type.equals(this.type);
	}
	public String getName() { return name; }
	public int getOffset() { return offset; }
	public Record getRecord() { return record; }
	public Type getType() { return type; }
	public int hashCode() {
	return name.hashCode();
	}
	public boolean isKind(int kind) { 
	return (kind & FIELD) != 0;
	}
	public void setOffset(int offset) { this.offset = offset; }
	// Printing
	public String toString() {
	return name + " : " + type.typeSpec() + "; ";
	}
}
