package edu.ksu.cis.bandera.specification.datastructure;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jjjc.node.*;
public class QuantifiedVariable {
	private String name;
	private Type type;
	private boolean exact;
	private Value constraint;
/**
 * 
 * @param name java.lang.String
 * @param type ca.mcgill.sable.soot.Type
 */
public QuantifiedVariable(String name, Type type) {
	this.name = name;
	this.type = type;
}
/**
 * 
 * @return boolean
 * @param o java.lang.Object
 */
public boolean equals(Object o) {
	if (o instanceof QuantifiedVariable) {
		QuantifiedVariable other = (QuantifiedVariable) o;
		return name.equals(other.name) && type.equals(other.type) && exact == other.exact;
	}
	return false;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 */
public ca.mcgill.sable.soot.jimple.Value getConstraint() {
	return constraint;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * 
 * @return ca.mcgill.sable.soot.Type
 */
public ca.mcgill.sable.soot.Type getType() {
	return type;
}
/**
 * 
 * @return int
 */
public int hashCode() {
	return (name + ":" + type).hashCode();
}
/**
 * 
 * @return boolean
 */
public boolean isExact() {
	return exact;
}
/**
 * 
 * @param newConstraint ca.mcgill.sable.soot.jimple.Value
 */
public void setConstraint(ca.mcgill.sable.soot.jimple.Value newConstraint) {
	constraint = newConstraint;
}
/**
 * 
 * @param newExact boolean
 */
public void setExact(boolean newExact) {
	exact = newExact;
}

/**
 * Debugging function (robbyjo)
 */
public String toString()
{
    return name + " = " + constraint;
}

}
