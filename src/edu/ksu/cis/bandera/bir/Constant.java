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

/**
 * A defined integer or enumerated type constant.
 * <p>
 * Note that this is different from an IntLit since a Constant has a name
 * (and is printed as such).
 */

public class Constant implements ConstExpr, Definition {

	String name;
	int value;
	Type type;
	
	public Constant(String name, int value, Type type) {
	this.name = name;
	this.value = value;
	this.type = type;
	}
	public void apply(Switch sw)
	{
		((ExprSwitch) sw).caseConstant(this);
	}
	public boolean equals(Object o) {
	return (o instanceof Constant) && ((Constant)o).name.equals(this.name)
	    && ((Constant)o).value == this.value;
	}
	public Object getDef() { return new Integer(value); }
	public String getName() { return name; }
	public Type getType() { return type; }
	public int getValue() { return value; }
	public void print() {
	System.out.print(name);
	}
	public String toString() {
	return name;
	}
}
