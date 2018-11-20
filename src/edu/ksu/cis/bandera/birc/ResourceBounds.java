package edu.ksu.cis.bandera.birc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001-2002tthew Dwyer (dwyer@cis.ksu.edu)    *
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
import java.util.Hashtable;
import java.util.Enumeration;
import edu.ksu.cis.bandera.util.DefaultValues;
/**
 * ResourceBounds provides information about the ranges of values
 * that program data can take on.
 * <p>
 * <code>int</code> values are bounded by <code>intMin</code> 
 * and <code>intMax</code>
 * <p>
 * <code>array</code>s can be allocated of sizes up to <code>arrayMax</code>
 * <p>
 * instances of thread (or runnable) types can be allocated and 
 * <code>start</code>ed up to <code>threadMax</code>
 * <p>
 * The number of instances of a type, <code>C</code>, 
 * that can be allocated at a 
 * given <code>new C</code> expression can be bounded.  A default bound for
 * all classes is defined by <code>allocMax</code>.  
 *
 * Class specific resource bounds can be set by associating an integer 
 * bound with the <code>String</code> name for the class.  Bound/class
 * pairs are stored in the <code>classBoundTable</code>.
 */
public class ResourceBounds {

	int intMin = edu.ksu.cis.bandera.util.DefaultValues.birMinIntRange;
	int intMax = edu.ksu.cis.bandera.util.DefaultValues.birMaxIntRange;
	int allocMax = edu.ksu.cis.bandera.util.DefaultValues.birMaxInstances;
	int arrayMax = edu.ksu.cis.bandera.util.DefaultValues.birMaxArrayLen;
	int threadMax = edu.ksu.cis.bandera.util.DefaultValues.birMaxThreads;
	Hashtable classBoundTable; 

	public ResourceBounds() {
	   classBoundTable = new Hashtable();
        }	

	public ResourceBounds(int intMin, int intMax, 
			      int allocMax, int arrayMax,
                              int threadMax) {
	   classBoundTable = new Hashtable();
	   this.intMin = intMin;
	   this.intMax = intMax;
	   this.arrayMax = arrayMax;
	   this.allocMax = allocMax;
	   this.threadMax = threadMax;
        }	

	public void setIntMax(int i) { intMax = i; }
	public int getIntMax() { return intMax; }

	public void setIntMin(int i) { intMin = i; }
	public int getIntMin() { return intMin; }

	public void setArrayMax(int i) { arrayMax = i; }
	public int getArrayMax() { return arrayMax; }

	public void setThreadMax(int i) { threadMax = i; }
	public int getThreadMax() { return threadMax; }

	public void setDefaultAllocMax(int i) { allocMax = i; }
	public void setAllocMax(String c, int i) { 
	  classBoundTable.put(c, new Integer(i));
        }

	public int getDefaultAllocMax() { return allocMax; }
	public int getAllocMax(String c) { 
	   Integer wrappedBound = (Integer)classBoundTable.get(c);
	   if (wrappedBound == null) 
	     return allocMax;
	   else
	     return wrappedBound.intValue();
	}

	public String toString() {
	  String bs =  "Integer range = [" + intMin + ", " + intMax + 
                       "], Array bound = " + arrayMax +
                       ", Thread bound = " + threadMax +
                       ", default instance bound = " + allocMax;
	  for (Enumeration classes = classBoundTable.keys();
	       classes.hasMoreElements();) {
	    String className = (String)classes.nextElement();
	    bs = bs + ", " + className + " instance bound = " + 
                 (Integer)classBoundTable.get(className);
          }
 	  return bs; 
	}
}


