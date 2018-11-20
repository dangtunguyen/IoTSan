package edu.ksu.cis.bandera.abstraction;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import edu.ksu.cis.bandera.abstraction.typeinference.*;
import edu.ksu.cis.bandera.specification.nnf.*;
import java.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.grimp.*;
public class SpecificationAbstractor {
	private Hashtable parameterTable;
	private String ltlFormula;
/**
 * 
 * @param typeTable edu.ksu.cis.bandera.abstraction.typeinference.TypeTable
 * @param ltlFormula java.lang.String
 * @param parameterTable java.util.Hashtable
 */
public void abstractLTL(CoercionManager cm, TypeTable typeTable, String ltlForm, Hashtable parameterTable) {
	ltlFormula = ltlForm;
	this.parameterTable = parameterTable;
	if (typeTable.size() == 0) return;

	ltlFormula = NNFTransformer.transformLTL(ltlFormula);
	for (Enumeration e = parameterTable.keys(); e.hasMoreElements();) {
		String p = (String) e.nextElement();
		if ("a".equals(p)) continue;
		Value pExp = (Value) parameterTable.get(p);
		String nP = "!" + p;
		String notP = "not_" + p;
		if (ltlFormula.indexOf(nP) >= 0) {
			parameterTable.put(notP, PredicateAbstractor.abs(cm, typeTable, PredicateTransformer.negate(pExp)));
			int idx;
			while((idx = ltlFormula.indexOf("!" + p)) >= 0) {
				ltlFormula = ltlFormula.substring(0, idx) + notP + ltlFormula.substring(idx + nP.length());
			}		
		}
		parameterTable.put(p, PredicateAbstractor.abs(cm, typeTable, PredicateTransformer.pushComplement(pExp)));
	}
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getLtlFormula() {
	return ltlFormula;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getParameterTable() {
	return parameterTable;
}
}
