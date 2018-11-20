package edu.ksu.cis.bandera.pdgslicer.dependency;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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


import edu.ksu.cis.bandera.bui.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.specification.assertion.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.*;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.*;
import javax.swing.JOptionPane;
public class SliceInterestEnv {
	private Dependencies dependFrame;
	private CriterionViewer criterionViewer;
	private Hashtable importedAssertion = new Hashtable();
	private Hashtable importedAssertionSet = new Hashtable();
	private HashSet predicates = new HashSet();
	private HashSet quantifierSliceInterests = new HashSet();
/**
 * 
 */
public SliceInterestEnv() {
}
/**
 * 
 * @return java.util.Vector
 */
Vector getSliceInterests() {
	if (!runSpecCompiler())
		return null;
	Vector v = (Vector) AssertionSliceInterestCollector.collect(new Vector());
	v.addAll(PredicateSliceInterestCollector.collect(predicates));
	v.addAll(quantifierSliceInterests);
	//v.addAll(edu.ksu.cis.bandera.core.CoreDriver.getMiscSliceInterests());
	//v.addAll(edu.ksu.cis.bandera.bui.Driver.getMiscSliceInterests());
	return v;
}
/**
 * 
 */
public void initAssertions() throws Exception {
	for (Iterator i = BUI.property.getImportedAssertion().iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		Assertion as = AssertionSet.getAssertion(name);
		importedAssertion.put(name, as);
	}
	
	for (Iterator i = BUI.property.getImportedAssertionSet().iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		AssertionSet as = AssertionSet.getAssertionSet(name);
		importedAssertionSet.put(name, as);
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public  Assertion resolveAssertionName(Name name) throws Exception {
	Assertion a = (Assertion) importedAssertion.get(name);
	if (a != null)
		return a;

	if (!name.isSimpleName()) {
		try {
			return AssertionSet.getAssertion(name);
		} catch (Exception e) {
			throw new Exception("Can't resolve assertion with name '" + name + "'");
		}
	}

	Vector v = new Vector();

	for (Enumeration e = importedAssertionSet.elements(); e.hasMoreElements();) {
		AssertionSet as = (AssertionSet) e.nextElement();
		if (as.getAssertionTable().get(new Name(as.getName(), name)) != null) {
			v.add(as);
		}
	}

	if (v.size() > 1)
		throw new Exception("Ambiguous assertion with name '" + name + "'");
	if (v.size() < 1)
		throw new Exception("Can't resolve assertion with name '" + name + "'");

	AssertionSet as = (AssertionSet) v.firstElement();
	a = (Assertion) as.getAssertionTable().get(new Name(as.getName(), name));
	return a;
}
/**
 * 
 */
private boolean runSpecCompiler() {
	try {
		Property property = BUI.property;
		Hashtable assertionTable = new Hashtable();
		initAssertions();
		for (Iterator i = property.getActivatedAssertionProperties().iterator(); i.hasNext();) {
			AssertionProperty ap = (AssertionProperty) i.next();
			for (Iterator j = ap.getAssertions().iterator(); j.hasNext();) {
				assertionTable.put(resolveAssertionName((Name) j.next()), new HashSet());
			}
		}
		int numOfQuantifiers = 0;
		HashSet v = new HashSet();
		TemporalLogicProperty tlp = property.getActivatedTemporalLogicProperty();
		if (tlp != null) {
		    tlp.validate(property.getImportedType(), property.getImportedPackage(), property.getImportedAssertion(),
		    property.getImportedAssertionSet(), property.getImportedPredicate(), property.getImportedPredicateSet()); 
		    if (tlp.getExceptions().size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (Iterator i = tlp.getExceptions().iterator(); i.hasNext();) {
			    buffer.append(i.next() + "\n");
			}
			JOptionPane.showMessageDialog(criterionViewer, buffer.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			dependFrame.success = false;
			return false;
		    }
		    Hashtable qTable = tlp.getQuantifiersTable();
		    numOfQuantifiers = qTable.size();
		    for (Enumeration e = qTable.elements(); e.hasMoreElements();) {
			v.add((QuantifiedVariable) e.nextElement());
		    }
		    CompilationManager.setQuantifiers(tlp.getQuantifiedVariables());
		} else {
			CompilationManager.setQuantifiers(new Vector());
		} 
		
		if ((assertionTable.size() > 0) || (numOfQuantifiers > 0)) {
		    CompilationManager.setModifiedMethodTable(AssertionProcessor.process(assertionTable));
		    if (!dependFrame.reRunJJJC()) {
			dependFrame.success = false;
			return false;
		    }
		    SootClass mainClass = CompilationManager.getMainSootClass();
		    for (Iterator i = CompilationManager.getQuantifiers().iterator(); i.hasNext();) {
				QuantifiedVariable qv = (QuantifiedVariable) i.next();
				String name = "quantification$" + qv.getName();
				SootField sf = CompilationManager.getFieldForQuantifier(name);
				mainClass.addField(sf);
				SootClass sc = sf.getDeclaringClass(); // robbyjo's patch
				quantifierSliceInterests.add(new SliceField(sc, sf)); // robbyjo's patch
		    }
		    CompilationManager.setModifiedMethodTable(new Hashtable());
		}
		// Compile predicates to Grimp
		if (tlp != null) {
			StringBuffer sb = new StringBuffer();
			tlp.validate(property.getImportedType(), property.getImportedPackage(), property.getImportedAssertion(), property.getImportedAssertionSet(), property.getImportedPredicate(), property.getImportedPredicateSet());
			Hashtable pTable = tlp.getPredicatesTable();
			Hashtable pqTable = tlp.getPredicateQuantifierTable();
			Hashtable qphTable = new Hashtable();
			SootClass mainClass = CompilationManager.getMainSootClass();
			Jimple jimple = Jimple.v();
			HashSet visitedP = new HashSet();
			for (Enumeration e = pTable.keys(); e.hasMoreElements();) {
				Object key = e.nextElement();
				Predicate p = (Predicate) pTable.get(key);
				if (p.getExceptions().size() > 0) {
					if (visitedP.contains(p)) continue;
					visitedP.add(p);
					sb.append("In '" + p + "':\n");
					for (Iterator i = p.getExceptions().iterator(); i.hasNext();) {
						sb.append("* " + ((Exception) i.next()).getMessage() + "\n");
					}
					continue;
				}
				Vector qvs = (Vector) pqTable.get(key);
				Value phThis = null;
				Hashtable phParams = new Hashtable();
				
				for (int i = 0; i < qvs.size(); i++) {
					QuantifiedVariable q = (QuantifiedVariable) qvs.elementAt(i);
					Value cph = null;
					if (q != null) {
						if (qphTable.get(q) == null) {
							String name = "quantification$" + q.getName();
							cph = jimple.newStaticFieldRef(mainClass.getField(name));
							qphTable.put(q, cph);
						} else {
							cph = (Value) qphTable.get(q);
						}
						if ((i == 0) && (!p.isStatic())) {
							phThis = cph;
						} else {
							phParams.put(q.getName(), cph);
						}
					}
				}
				Value value = PredicateProcessor.process(phThis, phParams, p);
				if (!(p instanceof ExpressionPredicate)) {
					if (value instanceof LocationTestExpr) {
						LocationTestExpr lte = (LocationTestExpr) value;
					} else {
						LocationTestExpr lte = (LocationTestExpr) ((LogicalAndExpr) value).getOp1();
					}
				}
				predicates.add(p);
			}

			String s = sb.toString();
			if (s.length() > 0)
				throw new RuntimeException(s.substring(0, s.length() - 1));
		}
	} catch (Exception e) {
		CompilationManager.setModifiedMethodTable(new Hashtable());
		CompilationManager.setQuantifiers(new Vector());
		JOptionPane.showMessageDialog(criterionViewer, e.getMessage(), "Bandera SL Phase --- Error", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
		dependFrame.success = false;
		return false;
	}
	dependFrame.success = true;
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-9 15:55:54)
 * @param cv edu.ksu.cis.bandera.pdgslicer.dependency.CriterionViewer
 */
void setCriterionViewer(CriterionViewer cv) {
	criterionViewer = cv;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-11 17:43:50)
 * @param dpnd edu.ksu.cis.bandera.pdgslicer.dependency.Dependencies
 */
void setDependFrame(Dependencies dpnd) {
	dependFrame = dpnd;
}
}
