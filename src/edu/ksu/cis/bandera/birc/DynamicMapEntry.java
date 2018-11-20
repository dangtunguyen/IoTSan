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
import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

/**
 * An entry in a dynamic map.
 * <p>
 * Binds an allocator site to the parameters of the collection that
 * will hold its instances (key, size, array size).
 */

class DynamicMapEntry {

	Expr alloc;            // NewExpr or NewArrayExpr
	SootClass sootClass;   // for NewExpr only
	ArrayType arrayType;   // for NewArrayExpr only
	Object key;            // Collection key
	int size;              // Collection size (# instances)
	int arrayLen;          // for NewArrayExpr only
	int id;                // Collection id (unique)

	static int count = 0;   // Count to assign id

	DynamicMapEntry(Expr alloc, SootClass sootClass, ArrayType arrayType,
		    Object key, int size, int arrayLen) {
	this.alloc = alloc;
	this.sootClass = sootClass;
	this.arrayType = arrayType;
	this.key = key;
	this.size = size;
	this.arrayLen = arrayLen;
	this.id = ++count;
	}
	public String toString() {
	if (alloc instanceof NewExpr)
	    return "<" + id + "," + alloc + "," + size + ">";
	else 
	    return "<" + id + "," + alloc + "," + size  
		+ "[" + arrayLen + "]>";
	}
}
