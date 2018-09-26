package edu.ksu.cis.bandera.abstraction.typeinference;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999, 2000   Shawn Laubach (laubach@acm.org)  *
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
import edu.ksu.cis.bandera.abstraction.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
import edu.ksu.cis.bandera.jext.*;
class LeftAssignExprSwitch extends IRNodes {
	private List constraints;
	private TypeInference ti;
	private UnionFindSet ufs;
	private Hashtable dependence;
public LeftAssignExprSwitch(List c, TypeInference t, UnionFindSet ufs, Hashtable dependence) {
	constraints = c;
	ti = t;
	this.ufs = ufs;
	this.dependence = dependence;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/00 11:36:46 AM)
 * @return ca.mcgill.sable.soot.Type
 * @param t ca.mcgill.sable.soot.Type
 */
public void addType(Type type) {
	if (ufs.find(type) == null) {
		ufs.add(type);
		ufs.setAttribute(type, type);
	}
}
public void caseArrayRef(ArrayRef v) {
	TypeStructure varnode = ti.getTypeStructure(v);
	ArrayTypeStructure var = (ArrayTypeStructure) ti.getTypeStructure(v.getBase());
	TypeStructure index = var.getIndex();
	v.getIndex().apply(ti);
	constraints.addAll(index.genEqualConstraints((TypeStructure) ti.getResult()));
	TypeStructure base = var.getElements();
	constraints.addAll(varnode.genEqualConstraints(base));
	setResult(varnode);
}
/**
   * This method was created in VisualAge.
   * @param v ca.mcgill.sable.soot.jimple.InstanceFieldRef
   */
public void caseInstanceFieldRef(InstanceFieldRef v) {
	TypeStructure fs = ti.getTypeStructure(v.getField());
	TypeStructure ts = ti.getTypeStructure(v);
	constraints.addAll(ts.genEqualConstraints(fs));
	setResult(ts);
}
public void caseLocal(Local v) {
	TypeStructure node = ti.getTypeStructure(v);
	setResult(node);
}
public void caseStaticFieldRef(StaticFieldRef v) {
	TypeStructure fs = ti.getTypeStructure(v.getField());
	TypeStructure ts = ti.getTypeStructure(v);
	constraints.addAll(ts.genEqualConstraints(fs));
	setResult(ts);
}
public void defaultCase(Object o) {
	throw new RuntimeException("Unhandled left hand side" + o);
}
}
