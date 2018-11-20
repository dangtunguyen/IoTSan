package edu.ksu.cis.bandera.specification.predicate.datastructure;

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
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.exception.*;
public class ExpressionPredicate extends Predicate implements Comparable {
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType type
 * @param node edu.ksu.cis.bandera.specification.predicate.node.Node
 * @param expTable java.util.Hashtable
 * @param exceptions java.util.Vector
 * @param exact boolean
 */
protected ExpressionPredicate(Name name, ClassOrInterfaceType type, edu.ksu.cis.bandera.specification.predicate.node.Node node, java.util.Vector exceptions) throws DuplicatePredicateException {
	super(name, type, null, node, exceptions);
}
/**
 * 
 * @param qv edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable
 */
public void applyThis(QuantifiedVariable qv) {}
/**
 * 
 * @return int
 * @param o java.lang.Object
 */
public int compareTo(Object o) {
	if (o instanceof ExpressionPredicate) {
		return name.compareTo(((ExpressionPredicate) o).getName());
	} else {
		return -1;
	}
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	Object o = ((edu.ksu.cis.bandera.specification.predicate.node.AExpressionPropositionDefinition) node).getParams();
	return "EXP " + name + "(" + (o != null? o.toString().trim() : "") + ")" + ": " + constraint.toString().trim();
}
}
