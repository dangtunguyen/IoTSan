package edu.ksu.cis.bandera.specification.ast;

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
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.grimp.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.specification.analysis.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.node.*;
import java.util.*;
public class Checker extends DepthFirstAdapter {
	private static Grimp grimp = Grimp.v();
	private Hashtable importedType;
	private Hashtable importedPackage;
	private Hashtable importedAssertion;
	private Hashtable importedAssertionSet;
	private Hashtable importedPredicate;
	private Hashtable importedPredicateSet;
	
	private Hashtable tlPredicates = new Hashtable();
	private Hashtable tlQuantifiers = new Hashtable();
	private Hashtable tlQuantifiedVariables = new Hashtable();
	private Hashtable asAssertions = new Hashtable();
	private Hashtable pqTable = new Hashtable();
	private Vector exceptions = new Vector();
	
	private String currentId;
	private SootClass currentSootClass;
	private Hashtable currentQuantifiers;
	private Hashtable currentPredicates;
	private Hashtable currentAssertions;
	private Vector currentTLQuantifiedVariables;
	private java.util.Vector args;
	private java.util.Vector quantifiedVariables;
	private Value constraint;
/**
 * 
 * @param importedType java.util.Set
 * @param importedPackage java.util.Set
 * @param importedAssertion java.util.Set
 * @param importedAssertionSet java.util.Set
 * @param importedPredicate java.util.Set
 * @param importedPredicateSet java.util.Set
 */
public Checker(Set importedType, Set importedPackage, Set importedAssertion, Set importedAssertionSet, Set importedPredicate, Set importedPredicateSet) {
	this.importedType = new Hashtable();
	this.importedPackage = new Hashtable();
	this.importedAssertion = new Hashtable();
	this.importedAssertionSet = new Hashtable();
	this.importedPredicate = new Hashtable();
	this.importedPredicateSet = new Hashtable();

	Hashtable compiledClasses = CompilationManager.getCompiledClasses();
	
	for (Iterator i = importedType.iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		SootClass sc = (SootClass) compiledClasses.get(name.toString());
		if (sc != null) {
			this.importedType.put(name, sc);
		} else {
			exceptions.addElement("Imported type named '" + name + "' is not declared");
		}
	}

	Hashtable compiledPackages = new Hashtable();
	for (Enumeration e = compiledClasses.keys(); e.hasMoreElements();) {
		Name name = new Name((String) e.nextElement());
		if (name.isSimpleName()) {
			this.importedType.put(name, compiledClasses.get(name.toString()));
		}
		name = name.getSuperName();
		try {
			compiledPackages.put(name, Package.getPackage(name));
		} catch (Exception ex) {}
	}

	for (Iterator i = importedPackage.iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		Package p = (Package) compiledPackages.get(name);
		if (p != null) {
			this.importedPackage.put(name, p);
		} else {
			exceptions.addElement("Imported package named '" + name + "' is not declared");
		}
	}

	for (Iterator i = importedAssertion.iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		try {
			Assertion as = AssertionSet.getAssertion(name);
			this.importedAssertion.put(name, as);
		} catch (Exception e) {
			exceptions.addElement(e.getMessage());
		}
	}
	
	for (Iterator i = importedAssertionSet.iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		try {
			AssertionSet as = AssertionSet.getAssertionSet(name);
			this.importedAssertionSet.put(name, as);
		} catch (Exception e) {
			exceptions.addElement(e.getMessage());
		}
	}
	
	for (Iterator i = importedPredicate.iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		try {
			Predicate p = PredicateSet.getPredicate(name);
			this.importedPredicate.put(name, p);
		} catch (Exception e) {
			exceptions.addElement(e.getMessage());
		}
	}
	
	for (Iterator i = importedPredicateSet.iterator(); i.hasNext();) {
		Name name = (Name) i.next();
		try {
			PredicateSet ps = PredicateSet.getPredicateSet(name);
			this.importedPredicateSet.put(name, ps);
		} catch (Exception e) {
			exceptions.addElement(e.getMessage());
		}
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AArgsArgs
 */
public void caseAArgsArgs(AArgsArgs node) {
	node.getArgs().apply(this);
	args.add(node.getId().toString().trim());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AIdArgs
 */
public void caseAIdArgs(AIdArgs node) {
	args.add(node.getId().toString().trim());
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AIdIds
 */
public void caseAIdIds(AIdIds node) {
	String id = node.toString().trim();
	if (currentQuantifiers.get(id) != null) {
		exceptions.addElement("Can't redefine quantifier with identifier '" + id + "'");
	} else {
		QuantifiedVariable qv = new QuantifiedVariable(id, RefType.v(currentSootClass.getName()));
		quantifiedVariables.add(qv);
		currentQuantifiers.put(id, qv);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AIdsIds
 */
public void caseAIdsIds(AIdsIds node) {
	node.getIds().apply(this);
	String id = node.getId().toString().trim();
	if (currentQuantifiers.get(id) != null) {
		exceptions.addElement("Can't redefine quantifier with identifier '" + id + "'");
	} else {
		QuantifiedVariable qv = new QuantifiedVariable(id, RefType.v(currentSootClass.getName()));
		quantifiedVariables.add(qv);
		currentQuantifiers.put(id, qv);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ANameNames
 */
public void caseANameNames(ANameNames node) {
	Name name = new Name(node.toString());
	try {
		Assertion a = resolveAssertion(name);
		currentAssertions.put(a.getName(), a);
	} catch (Exception e) {
		exceptions.addElement(e.getMessage());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ANamePrimaryTypeExp
 */
public void caseANamePrimaryTypeExp(ANamePrimaryTypeExp node) {
	String name = node.getName().toString().trim();
	QuantifiedVariable qv = (QuantifiedVariable) currentQuantifiers.get(name);
	Name className;
	RefType rt;
	if (qv != null) {
		rt = (RefType) qv.getType();
		className = new Name(rt.className);
	} else {
		className = new Name(name);
		rt = RefType.v(name);
	}
	try {
		currentSootClass = resolveType(className);
		if (qv != null) {
			constraint = grimp.newEqExpr(new Local("@this", rt), new Local("quantification$" + qv.getName(), qv.getType()));
		} else {
			constraint = grimp.newInstanceOfExpr(new Local("@this", rt), rt);
		}
	} catch (Exception e) {
		exceptions.addElement(e.getMessage());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ANamesNames
 */
public void caseANamesNames(ANamesNames node) {
	node.getNames().apply(this);
	Name name = new Name(node.getName().toString());
	try {
		Assertion a = resolveAssertion(name);
		currentAssertions.put(a.getName(), a);
	} catch (Exception e) {
		exceptions.addElement(e.getMessage());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AOpTypeExp
 */
public void caseAOpTypeExp(AOpTypeExp node) {
	SootClass lType, rType = null;
	Value lConstraint, rConstraint;
	node.getPrimaryTypeExp().apply(this);
	lType = currentSootClass;
	lConstraint = constraint;
	node.getTypeExp().apply(this);
	rType = currentSootClass;
	rConstraint = constraint;
	currentSootClass = lub(lType, rType);
	if (node.getTypeOp() instanceof AFilterTypeOp) {
		constraint = new LogicalAndExpr(lConstraint, new ComplementExpr(rConstraint));
	} else {
		constraint = new LogicalOrExpr(lConstraint, rConstraint);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.APredicateExp
 */
public void caseAPredicateExp(APredicateExp node) {
	Name name = new Name(node.getName().toString());
	try {
		Predicate p = resolvePredicate(name);
		currentPredicates.put(node, p);
		args = new Vector();
		if (node.getArgs() != null) {
			node.getArgs().apply(this);
		}
		if (pqTable.get(node) == null) {
			pqTable.put(node, new Vector());
		}
		if (!p.isStatic()) {
			String qId = (String) args.remove(0);
			QuantifiedVariable q = (QuantifiedVariable) currentQuantifiers.get(qId);
			if (q == null) {
				exceptions.addElement("A quantifier with identifier '" + qId + "' is not declared");
				return;
			}
			try {
				ClassOrInterfaceType pType = Package.getClassOrInterfaceType(new Name(p.getType().getFullyQualifiedName()));
				ClassOrInterfaceType qType = Package.getClassOrInterfaceType(new Name(q.getType().toString()));
				if (pType == qType || qType.hasSuperInterface(pType) || qType.hasSuperClass(pType)) {
					((Vector) pqTable.get(node)).add(q);
					p.applyThis(q);
				} else {
					exceptions.addElement("Type mismatch in " + node);
				}
			} catch (Exception e) {
				exceptions.addElement(e.getMessage());
			}
		} else {
			p.applyThis(null);
		}
		for (int i = 0; i < args.size(); i++) {
			String qId = (String) args.elementAt(i);
			QuantifiedVariable q = (QuantifiedVariable) currentQuantifiers.get(qId);
			if (q == null) {
				exceptions.addElement("A quantifier with identifier '" + qId + "' is not declared");
				return;
			}
			ClassOrInterfaceType ct = Package.getClassOrInterfaceType(new Name(p.getParamType(i).getFullyQualifiedName()));
			ClassOrInterfaceType qt = Package.getClassOrInterfaceType(new Name(q.getType().toString()));
			if ((ct == qt) || (ct.hasSuperClass(qt)) || (ct.hasSuperInterface(qt))) {
				((Vector) pqTable.get(node)).add(q);
			} else {
				exceptions.addElement("Type mismatch in " + node);
			}
		}
	} catch (Exception e) {
		exceptions.addElement(e.getMessage());
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.AQtl
 */
public void caseAQtl(AQtl node) {
	quantifiedVariables = new Vector();
	constraint = null;
	node.getTypeExp().apply(this);
	if (constraint instanceof InstanceOfExpr) {
		constraint = null;
	}
	if (node.getIds() != null) {
		node.getIds().apply(this);
	}
	for (Iterator i = quantifiedVariables.iterator(); i.hasNext();) {
		QuantifiedVariable qv = (QuantifiedVariable) i.next();
		qv.setExact(node.getQtlBinding() instanceof AExactQtlBinding);
		qv.setConstraint(constraint);
		currentTLQuantifiedVariables.add(qv);
	}
	quantifiedVariables = null;
	constraint = null;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.specification.node.ATl
 */
public void caseATl(ATl node) {
	currentId = node.getId().toString().trim();
	currentQuantifiers = new Hashtable();
	currentPredicates = new Hashtable();
	currentTLQuantifiedVariables = new Vector();
	{
		Object temp[] = node.getQtl().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PQtl) temp[i]).apply(this);
		}
	}
	{
		Object temp[] = node.getWord().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PWord) temp[i]).apply(this);
		}
	}
	tlPredicates.put(currentId, currentPredicates);
	tlQuantifiers.put(currentId, currentQuantifiers);
	tlQuantifiedVariables.put(currentId, currentTLQuantifiedVariables);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.node.PName
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public edu.ksu.cis.bandera.jjjc.node.PName createASTName(Name name) {
	if (name.isSimpleName()) {
		return new edu.ksu.cis.bandera.jjjc.node.ASimpleName(new edu.ksu.cis.bandera.jjjc.node.TId(name.toString()));
	} else {
		return new edu.ksu.cis.bandera.jjjc.node.AQualifiedName(createASTName(name.getSuperName()), new edu.ksu.cis.bandera.jjjc.node.TDot(), new edu.ksu.cis.bandera.jjjc.node.TId(name.getLastIdentifier()));
	}
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getAEnabledAssertionsTable() {
	return asAssertions;
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getExceptions() {
	return exceptions;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getPredicateQuantifierTable() {
	return pqTable;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getTLPredicatesTable() {
	return tlPredicates;
}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getTLQuantifiedVariables() {
	return tlQuantifiedVariables;
}
/**
 * 
 * @return java.util.Hashtable
 */
public Hashtable getTLQuantifiersTable() {
	return tlQuantifiers;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootClass
 * @param sc1 ca.mcgill.sable.soot.SootClass
 * @param sc2 ca.mcgill.sable.soot.SootClass
 */
public SootClass lub(SootClass sc1, SootClass sc2) {
	if (sc1 == sc2) return sc1;
	ClassOrInterfaceType cit1 = null, cit2 = null;
	try {
		cit1 = Package.getClassOrInterfaceType(new Name(sc1.getName()));
		cit1.loadReferences();
		cit2 = Package.getClassOrInterfaceType(new Name(sc2.getName()));
		cit2.loadReferences();
	} catch (Exception e) {
		exceptions.add(e.getMessage());
		return null;
	}
	
	HashSet set = new HashSet();
	boolean isInterface = true;
	ClassOrInterfaceType base;
	
	if (!cit1.isInterface() && !cit2.isInterface()) {
		isInterface = false;
		base = cit1;
		set.add(cit2);
		for (Enumeration e = cit1.getSuperClasses(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
		for (Enumeration e = cit2.getSuperClasses(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
	} else if (cit1.isInterface() && cit2.isInterface()) {
		base = cit1;
		set.add(cit2);
		for (Enumeration e = cit1.getSuperInterfaces(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
		for (Enumeration e = cit2.getSuperInterfaces(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
	} else if (cit1.isInterface() && !cit2.isInterface()) {
		base = cit1;
		for (Enumeration e = cit1.getSuperInterfaces(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
		for (Enumeration e = cit2.getSuperInterfaces(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
	} else {
		base = cit2;
		for (Enumeration e = cit1.getSuperInterfaces(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
		for (Enumeration e = cit2.getSuperInterfaces(); e.hasMoreElements();) {
			set.add(e.nextElement());
		}
	}

	ClassOrInterfaceType result = null;
	if (isInterface) {
		for (Iterator i = set.iterator(); i.hasNext();) {
			ClassOrInterfaceType type = (ClassOrInterfaceType) i.next();
			if (base == type) {
				result = base;
				break;
			} else if (base.hasSuperInterface(type)) {
				if (result == null) {
					result = type;
				} else {
					if (type.hasSuperInterface(result)) {
						result = type;
					}
				}
			}
		}
	} else {
		for (Iterator i = set.iterator(); i.hasNext();) {
			ClassOrInterfaceType type = (ClassOrInterfaceType) i.next();
			if (base == type) {
				result = base;
				break;
			} else if (base.hasSuperClass(type)) {
				if (result == null) {
					result = type;
				} else {
					if (type.hasSuperClass(result)) {
						result = type;
					}
				}
			}
		}
	}
	return CompilationManager.getSootClassManager().getClass(result.getFullyQualifiedName());
}
/**
 * 
 * @return edu.ksu.cis.bandera.assertion.datastructure.Assertion
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Assertion resolveAssertion(Name name) throws java.lang.Exception {
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
 * @return edu.ksu.cis.bandera.predicate.datastructure.Predicate
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @exception java.lang.Exception The exception description.
 */
public Predicate resolvePredicate(Name name) throws java.lang.Exception {
	Predicate p = (Predicate) importedPredicate.get(name);
	if (p != null)
		return p;

	if (!name.isSimpleName()) {
		try {
			return PredicateSet.getPredicate(name);
		} catch (Exception e) {
			throw new Exception("Can't resolve predicate with name '" + name + "'");
		}
	}

	Vector v = new Vector();

	for (Enumeration e = importedPredicateSet.elements(); e.hasMoreElements();) {
		PredicateSet ps = (PredicateSet) e.nextElement();
		if (ps.getPredicateTable().get(new Name(ps.getName(), name)) != null) {
			v.add(ps);
		}
	}

	if (v.size() > 1)
		throw new Exception("Ambiguous predicate with name '" + name + "'");
	if (v.size() < 1)
		throw new Exception("Can't resolve predicate with name '" + name + "'");

	PredicateSet ps = (PredicateSet) v.firstElement();
	p = (Predicate) ps.getPredicateTable().get(new Name(ps.getName(), name));
	return p;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootClass
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public SootClass resolveType(Name name) throws java.lang.Exception {
	SootClass sc = (SootClass) importedType.get(name);
	if (sc != null)
		return sc;

	Hashtable compiledClasses = CompilationManager.getCompiledClasses();
	
	if (!name.isSimpleName()) {
		sc = (SootClass) compiledClasses.get(name.toString());
		if (sc != null)
			return sc;
		throw new Exception("Can't resolve type with name '" + name + "'");
	}
		
	Vector v = new Vector();

	for (Enumeration e = importedPackage.elements(); e.hasMoreElements();) {
		Package p = (Package) e.nextElement();
		if (p.hasType(name)) {
			if (compiledClasses.get(new Name(p.getName(), name).toString()) != null) {
				v.add(p);
			}
		}
	}

	if (v.size() > 1)
		throw new Exception("Ambiguous type with name '" + name + "'");
	if (v.size() < 1)
		throw new Exception("Can't resolve type with name '" + name + "'");
		
	sc = (SootClass) compiledClasses.get(new Name(((Package) v.firstElement()).getName(), name).toString());
	return sc;
}
}
