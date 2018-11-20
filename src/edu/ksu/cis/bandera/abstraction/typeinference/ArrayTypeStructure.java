package edu.ksu.cis.bandera.abstraction.typeinference;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
 * All rights reserved.                                              *
 *                                                                   *
 * Modifications by Robby (robby@cis.ksu.edu) are                    *
 * Copyright (C) 2000 Robby.  All rights reserved.                   *
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
import java.util.*;
public class ArrayTypeStructure implements TypeStructure {
	protected TypeStructure index;
	protected TypeStructure elements;
/**
 *
 * @param elements edu.ksu.cis.bandera.abstraction.typeinference.TypeStructure
 * @param index edu.ksu.cis.bandera.abstraction.typeinference.TypeStructure
 */
public ArrayTypeStructure(TypeStructure elements, TypeStructure index) {
	this.elements = elements;
	this.index = index;
}
/**
 * getCoerceConstraints method comment.
 */
public java.util.Set genCoerceConstraints(TypeStructure ats) {
	Set set = new HashSet();
	if (ats instanceof ArrayTypeStructure) {
		ArrayTypeStructure bts = (ArrayTypeStructure) ats;
		set.addAll(index.genCoerceConstraints(bts.index));
		set.addAll(elements.genCoerceConstraints(bts.elements));
	} else
		System.out.println("Cannot mix type structures.");
	return set;
}
/**
 * getEqualConstraints method comment.
 */
public java.util.Set genEqualConstraints(TypeStructure ats) {
	Set set = new HashSet();
	if (ats instanceof ArrayTypeStructure) {
		ArrayTypeStructure bts = (ArrayTypeStructure) ats;
		set.addAll(index.genEqualConstraints(bts.index));
		set.addAll(elements.genEqualConstraints(bts.elements));
	} else
		System.out.println("Cannot mix type structures.");
	return set;
}
/**
 *
 * @return edu.ksu.cis.bandera.abstraction.typeinference.TypeStructure
 */
public TypeStructure getElements() {
	return elements;
}
/**
 *
 * @return edu.ksu.cis.bandera.abstraction.typeinference.TypeStructure
 */
public TypeStructure getIndex() {
	return index;
}
/**
 *
 * @param newElements edu.ksu.cis.bandera.abstraction.typeinference.TypeStructure
 */
protected void setElements(TypeStructure newElements) {
	elements = newElements;
}
/**
 *
 * @param newIndex edu.ksu.cis.bandera.abstraction.typeinference.TypeStructure
 */
protected void setIndex(TypeStructure newIndex) {
	index = newIndex;
}
}
