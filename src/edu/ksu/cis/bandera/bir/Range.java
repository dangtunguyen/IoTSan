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
 * A range type.  
 */

public class Range extends Type implements BirConstants {

	int rangeSize = 0;    // Number of elements in range
	int firstElement = 0;   
	ConstExpr fromVal;
	ConstExpr toVal;

	public Range() {
	this(- (1 << 31), (1 << 31) - 1);
	}
	public Range(int lo, int hi) {
	this.rangeSize = (hi - lo) + 1;
	this.firstElement = lo;
	this.fromVal = new IntLit(lo);
	this.toVal = new IntLit(hi);
	}
	public Range(ConstExpr fromVal, ConstExpr toVal) {
	this.fromVal = fromVal;
	this.toVal = toVal;
	this.firstElement = fromVal.getValue();
	this.rangeSize = (toVal.getValue() - fromVal.getValue()) + 1;
	}
	public void apply(TypeSwitch sw, Object o)
	{
		sw.caseRange(this, o);
	}
	public boolean containsValue(Object value) { 
	int val;
	if (value instanceof Integer) 
	    val = ((Integer)value).intValue();
	else if (value instanceof ConstExpr)
	    val = ((ConstExpr)value).getValue();
	else
	    return false;
	return (val >= firstElement && val < firstElement + rangeSize);
	}
	public Expr defaultVal() { 
	// Zero if in range, else first element
	if (firstElement <= 0 && 0 <= (firstElement + rangeSize))
	    return new IntLit(0);
	else
	    return new IntLit(firstElement); 
	}
	public boolean equals(Object o) {
	return (o instanceof Range) && ((Range)o).rangeSize == this.rangeSize 
	    && ((Range)o).firstElement == this.firstElement;
	}
	public int getFirstElement() { return firstElement; }
	public ConstExpr getFromVal() { return fromVal; }
	public int getRangeSize() { return rangeSize; }
	public ConstExpr getToVal() { return toVal; }
	public boolean isKind(int kind) { 
	return (kind & RANGE) != 0;
	}
	public boolean isSubtypeOf(Type type) { 
	if (! type.isKind(RANGE))
	    return false; 
	Range other = (Range) type;
	return this.fromVal.getValue() >= other.fromVal.getValue()
	    && this.toVal.getValue() <= other.toVal.getValue();
	}
	public String toString() {	
	return "range " + fromVal + ".." + toVal;
	}
}
