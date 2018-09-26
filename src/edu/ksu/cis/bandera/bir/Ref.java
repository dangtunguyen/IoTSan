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
import ca.mcgill.sable.soot.SootClass;

import java.util.*;

import org.apache.log4j.Category;

/**
 * A reference type.
 * <p>
 * A reference in BIR is defined with a set of state variables (which
 * must be records or arrays) to which it can refer.  A reference
 * can also refer to a collection, in which case the reference points
 * to an element of the collection.
 * <p>
 * Since reference types can be declared before the state variables
 * to which they refer, reference types must be resolved by finding
 * the variables named in the type and connecting them to the reference
 * type.
 */

public class Ref extends Type implements BirConstants {
  private static Category log =
	Category.getInstance(TransSystem.class.getName());

  TransSystem system;
  StateVarVector targets;
  Vector targetNames;
  Type targetType;
  SootClass sootType;

  /**
   * Create a resolved reference type that can refer to 
   * a specific state variable.
   */

  public Ref(StateVar target) {
    this.targetNames = new Vector(10);
    this.targetNames.addElement(target.getName());
    this.targets = new StateVarVector(1);
    this.targets.addElement(target);
    this.targetType = liftCollectionType(target.getType());
    this.system = target.getSystem();
  }
  /**
   * Create a resolved reference type that can refer to 
   * a specific set of state variables with an empty target type.
   */

  public Ref(StateVarVector targets, TransSystem system) {
    this.targets = targets;
    this.targetType = new Record();
    this.system = system;
  }

  static StateVarVector getCollections(Type target, TransSystem system) {
    StateVarVector stateVars = system.getStateVars();
    StateVarVector collections = new StateVarVector();
    for (int i = 0; i < stateVars.size(); i ++) {
      StateVar var = stateVars.elementAt(i);
      Type type = var.getType();
      if (type.isKind(COLLECTION) && 
	  ((Collection) type).getBaseType() == target)
	collections.addElement(var);
    }

    return collections;
  }

  /**
   * Create a reference type that can refer to 
   * any collection of a specified target type.
   */

  public Ref(Type targetType, TransSystem system) {
    this.targets = getCollections(targetType, system);
    this.targetType = targetType;
    this.system = system;    
  }

  /**
   * Create an empty, unresolved reference type
   */

  public Ref(TransSystem system) {
    this.targetNames = new Vector(10);
    this.system = system;
  }

  public void addTarget(StateVar var) {
    if (targets == null) 
      targets = new StateVarVector(10);
    targets.addElement(var);
    if (targets.size() == 1)
      targetType = liftCollectionType(var.getType());
    else
      targetType = superType(targetType,
			     liftCollectionType(var.getType()));
log.debug("bir.Ref.addTarget: of " + var + " is " + targetType);
  }

  public void addTarget(String name) {
    targetNames.addElement(name);
  }

  public void apply(TypeSwitch sw, Object o) {
    sw.caseRef(this, o);
  }
  /**
   * The target type of a reference type is the least common supertype
   * of all the types of its targets.  The empty record is a supertype
   * of all types since no operations can be performed to it.
   */

  Type computeTargetType() {
    Type result = liftCollectionType(targets.elementAt(0).getType());
    for (int i = 1; i < targets.size(); i++) {
      Type targetType = targets.elementAt(i).getType();
      result = superType(result, liftCollectionType(targetType));
    }
    return result;
  }

  public Expr defaultVal() { 
    return new NullExpr(system); 
  }

  public boolean equals(Object o) {
    return this == o;
  }

  public StateVarVector getTargets() { return targets; }
  public Type getTargetType() { return targetType; }
  public boolean isKind(int kind) { 
    return (kind & REF) != 0;
  }
  /**
   * Test if type is a subtype of another (i.e., contains a subset of
   * its targets).
   */

  public boolean isSubtypeOf(Type type) {
    return isSubtype(targetType, type);
  }
  
  /**
   * Return the base type of a collection.
   */
  
  Type liftCollectionType(Type type) {
    return type.isKind(COLLECTION) ? 
      ((Collection)type).getBaseType() : type;
  }
  /**
   * Resolve a reference type by finding the state variables
   * named in the type and computing the target type.
   */

  public void resolveTargets() {
    targets = new StateVarVector(targetNames.size());
    for (int i = 0; i < targetNames.size(); i++) {
      String name = (String) targetNames.elementAt(i);
      Object key = system.getKeyOf(name);
      if (key == null) 
	throw new RuntimeException("Unknown object in Ref type: " + name);
      StateVar var = system.getVarOfKey(key);
      if (! var.getType().isKind(RECORD|ARRAY|COLLECTION)) 
	throw new RuntimeException("Ref type must refer to record, " + 
				   "array or collection: " + name + 
				   " is a " + var.getType());
      targets.addElement(var);
    }
    targetType = computeTargetType();
  }
  
  boolean isSubtype(Type type1, Type type2) {
    if (type1 == type2)
      return true;
    
    if (type1.isKind(RECORD) && type2.isKind(RECORD))
      return system.isSubtype((Record) type1, (Record) type2);

    if (type1.isKind(ARRAY) && type2.isKind(ARRAY))
      return isSubtype(((Array) type1).getBaseType(),
		       ((Array) type2).getBaseType());

    return false;
  }

  /**
   * Compute the least common supertype of two types.
   */
  
  Type superType(Type type1, Type type2) {
log.debug("Ref.superType of " + type1 + " and " + type2); 
    if (type1 == type2)
      return type1;

    if (type1.isKind(RECORD) && type2.isKind(RECORD))
      return system.superType((Record) type1, (Record) type2);

    Type none = new Record();
    if (type1.isKind(ARRAY) && type2.isKind(ARRAY)) {
      Type base1 = ((Array) type1).getBaseType();
      Type base2 = ((Array) type2).getBaseType();
      Type result = superType(base1, base2);
      if (result.equals(none))
	return result;

      if (((Array) type1).getSize().getValue() > 
	  ((Array) type2).getSize().getValue())
	result = new Array(result, ((Array) type2).getSize());
      else
	result = new Array(result, ((Array) type1).getSize());

      // This test is done in order to eliminate duplicate types.
      // They can cause troubles since == was previously used on types.
      if (result.equals(type1))
	return type1;
      else if (result.equals(type2))
	return type2;
      
      return result;
    }

    return none;
  }

  // Printing
  public String toString() {
    if (this == system.refAnyType())
      return "ref { * }";
    String result = targets.elementAt(0).getName();
    for (int i = 1; i < targets.size(); i++) 
      result += ", " + targets.elementAt(i).getName();
    return "ref { " + result + " }";
  }
}
