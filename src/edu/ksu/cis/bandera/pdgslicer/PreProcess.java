package edu.ksu.cis.bandera.pdgslicer;

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

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import edu.ksu.cis.bandera.pdgslicer.exceptions.*;
import java.io.*;
import java.util.BitSet;
import java.util.Vector;
import java.util.Enumeration;
/**
 * This class is for extracting and building up slice
 * criterion of each class and method.
 */
public class PreProcess {
  /**
   * A list of {@link ClassInfo ClassInfo}.
   */
	private List classInfoList;
  /**
   * A map from {@link SootMethod SootMethod} to {@link SliceCriterion SliceCriterion}
   * for storing slice criterion of each method.
   */
	private Map methodCritMap = new HashMap();
	private boolean emptySliceCriterion = false;
  /**
   * A set of {@link Local Local}.
   */
	private Set sliceLocalSet = new ArraySet();
/**
 * @param clsList a list of {@link ClassInfo ClassInfo}.
 */
public PreProcess(List clsList) {
	classInfoList = clsList;
}
/**
 * See if slice criterion is empty.
 * <p>
 * @return {@link #emptySliceCriterion emptySliceCriterion}.
 */
boolean emptySliceCriterion() {
	return emptySliceCriterion;
}
/**
 * Extract slice criterion from a set of slice interests.
 * <br> Put those slice criterion into {@link #methodCritMap methodCritMap}.
 * <p>
 * @param sliceInterests a set of {@link SliceInterest SliceInterest}.
 */
public void extracting(Vector sliceInterests) {
	for (Enumeration e = sliceInterests.elements(); e.hasMoreElements();) {
		SliceInterest sliceInterest = (SliceInterest) e.nextElement();
		if (sliceInterest instanceof SliceLocal) {
			SliceLocal sliceLocal = (SliceLocal) sliceInterest;
			sliceLocalSet.add(sliceLocal.getLocal());
		}
	}
	for (Enumeration e = sliceInterests.elements(); e.hasMoreElements();) {
		SliceInterest sliceInterest = (SliceInterest) e.nextElement();
		if (sliceInterest instanceof SliceLocal) {
			SliceLocal sliceLocal = (SliceLocal) sliceInterest;
			putLocalToCritMap(sliceLocal.getSootMethod(), sliceLocal.getLocal());
		} else
			if (sliceInterest instanceof SliceStatement) {
				SliceStatement sliceStatement = (SliceStatement) sliceInterest;
				putStatementToCritMap(sliceStatement.getSootMethod(), sliceStatement.getStmt());
			} else
				if (sliceInterest instanceof SlicePoint) {
					SlicePoint slicePoint = (SlicePoint) sliceInterest;
					putPointToCritMap(slicePoint.getSootMethod(), slicePoint.getStmt());
				} else
					if (sliceInterest instanceof SliceField) {
						SliceField sliceField = (SliceField) sliceInterest;
						putFieldToCritMap(sliceField.getSootClass(), sliceField.getSootField());
					}
	}
	putMdCritMapToMdInfo();
}
	/**
	 * Extract slice criterion for deadlock checking.
	 * <br> First get slice interest for deadlock checking from class
	 * {@link DeadlockRelatedCriterion DeadlockRelatedCriterion}.
	 * Then extract slice criterion from those slice interest by method
	 * {@link #extracting(Vector) extracting(sliceInterests)}.
	 */
public void extractingForDL(SootClass[] classes) {
	DeadlockRelatedCriterion dlCrit = new DeadlockRelatedCriterion(classes);
	Vector sliceInterestForDL = dlCrit.getSliceInterestForDL();
	if (sliceInterestForDL.isEmpty())
		emptySliceCriterion = true;
	else
		extracting(sliceInterestForDL);
}
/**
 * Extract relevant variable map from slice criterion.
 * <p>
 * @return relevant variable map from {@link Stmt Stmt}
 * to a {@link Set Set} of {@link Value Value}.
 * @param sc slice criterion.
 */
static Map extractRelVarMapFromCriterion(SliceCriterion sc) {
	Map relVarMap = new HashMap();
	Set sliceVars = sc.getSliceVars();
	Set variables = new ArraySet();
	variables.addAll(sliceVars);
	Set sliceStatements = sc.getSliceStatements();
	for (Iterator stmtIt = sliceStatements.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		for (Iterator useBoxIt = stmt.getUseBoxes().iterator(); useBoxIt.hasNext();) {
			ValueBox valueBox = (ValueBox) useBoxIt.next();
			Value value = valueBox.getValue();
			if ((value instanceof Local) || (value instanceof FieldRef))
				variables.add(value);
		}
	}
	for (Iterator pointIt = sc.getSlicePoints().iterator(); pointIt.hasNext();)
		relVarMap.put((Stmt) pointIt.next(), variables);
	for (Iterator stmtIt = sliceStatements.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		if (relVarMap.containsKey(stmt))
			continue;
		relVarMap.put(stmt, variables);
	}
	return relVarMap;
}

/**
 * Get all instance field references for a given field
 * which is assigned value by some assigment statement.
 * <p>
 * @return a set of {@link FieldRef FieldRef} which is
 * an instance field reference of a given
 * <code>sootField</code>.
 * @param localAssMap a map of local to its assignments.
 * @param sootField query sootfield.
 */
private Set getInstanceFieldRef(Map localAssMap, SootField sootField) {
	Set returnSet = new ArraySet();
	for (Iterator localIt = localAssMap.keySet().iterator(); localIt.hasNext();) {
		Value defVar = (Value) localIt.next();
		if (!(defVar instanceof InstanceFieldRef))
			continue;
		InstanceFieldRef defInsRef = (InstanceFieldRef) defVar;
		SootField insFd = defInsRef.getField();
		Value baseValue = defInsRef.getBase();
		if (baseValue instanceof Local) {
			Local baseLocal = (Local) baseValue;
			if (insFd.equals(sootField))// && sliceLocalSet.contains(baseLocal))
				//return defInsRef;
			    returnSet.add(defInsRef);
		}
	}
	return returnSet;
}
/**
 * Get all static field references for a given field
 * which is assigned value by some assigment statement.
 * <p>
 * @return a set of {@link FieldRef FieldRef} which is
 * a static field reference of a given
 * <code>staticField</code>.
 * @param localAssMap a map of local to its assignments.
 * @param staticField query sootfield.
 */
private Set getStaticFieldRef(Map localAssMap, SootField staticField) {
	Set returnSet = new ArraySet();
	for (Iterator localIt = localAssMap.keySet().iterator(); localIt.hasNext();) {
		Object definedVar = localIt.next();
		if (definedVar instanceof StaticFieldRef) {
			StaticFieldRef sfr = (StaticFieldRef) definedVar;
			SootField definedFd = sfr.getField();
			if (staticField.equals(definedFd)) {
				returnSet.add(sfr);
			}
		}
	}
	return returnSet;
}
/**
 * Put field into {@link #methodCritMap methodCritMap}. It's including:
 * <br> (1) {@link #putStaticFieldToCritMap(SootClass, SootField)
 * putSTaticfieldToCritMap()}
 * <br> (2) {@link #putPrivateInsFdToCritMap(SootClass, SootField)
 * putPrivateInsFdToCritMap()}
 * <br> (3) {@link #putPubInstanceFdToCritMap(SootClass, SootField)
 * putPubInstanceFdToCritMap()}
 * <p>
 * @param sootClass the class where <code>field</code> is declared.
 * @param field the field need to be added into criterion map.
 */
private void putFieldToCritMap(SootClass sootClass, SootField field) {
	int modifiers = field.getModifiers();
	if (Modifier.isStatic(modifiers))
		putStaticFieldToCritMap(sootClass, field);
	else
		if (Modifier.isPrivate(modifiers))
			putPrivateInsFdToCritMap(sootClass, field);
		else { //if (Modifier.isPublic(modifiers))
			System.out.println("now, we treat fields rather than static and private as public simply, need further programming on protected fields");
			putPubInstanceFdToCritMap(sootClass, field);
		}

		//else if (Modifier.isProtected(modifiers))

}
/**
 * Put field reference into {@link #methodCritMap methodCritMap}.
 * <br> A field reference is a variable, so the <code>field</code>
 * should be added into the set of slice variable in the slice criterion
 * of the given method. Slice criterion
 * consists of slice points set and slice variable set.
 * <p>
 * @param sootMethod the method where the <code>field</code> is referenced.
 * @param field the field need to be added.
 */
private void putFieldToCritMap(SootMethod sootMethod, FieldRef field) {
	if (methodCritMap.containsKey(sootMethod)) {
		SliceCriterion criterion = (SliceCriterion) methodCritMap.get(sootMethod);
		criterion.getSliceVars().add(field);
	} else {
		Set varSet = new ArraySet();
		varSet.add(field);
		methodCritMap.put(sootMethod, new SliceCriterion(new ArraySet(), varSet, new ArraySet()));
	}
}
/**
 * Put a local variable into {@link #methodCritMap methodCritMap}.
 * <br> A local is a variable, so the <code>local</code>
 * should be added into the set of slice variable in the slice criterion
 * of the given method. Slice criterion
 * consists of slice points set and slice variable set.
 * <p>
 * @param sootMethod the method where the <code>local</code> is referenced.
 * @param local the local need to be added.
 */
private void putLocalToCritMap(SootMethod sootMethod, Local local) {
	if (methodCritMap.containsKey(sootMethod)) {
		SliceCriterion criterion = (SliceCriterion) methodCritMap.get(sootMethod);
		criterion.getSliceVars().add(local);
	} else {
		Set varSet = new ArraySet();
		varSet.add(local);
		methodCritMap.put(sootMethod, new SliceCriterion(new ArraySet(), varSet, new ArraySet()));
	}
}
/**
 * Set value for the field <code>sCriterion</code> of
 * {@link MethodInfo MethodInfo} of each method by
 * {@link #methodCritMap methodCritMap}.
 */
private void putMdCritMapToMdInfo() {
	for (Iterator keyIt = methodCritMap.keySet().iterator(); keyIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) keyIt.next();
		/*
		System.out.println("sootMethod hashCode: " + sootMethod+"  " +sootMethod.hashCode());
		for (Iterator mdIt = Slicer.sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
			System.out.println();
			System.out.println("sm hashCode: " + mdIt.next()+"  "+ mdIt.next().hashCode());
		}*/
		MethodInfo mdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		if (mdInfo == null) continue;
		mdInfo.sCriterion = (SliceCriterion) methodCritMap.get(sootMethod);
		Map relVarMap = extractRelVarMapFromCriterion(mdInfo.sCriterion);
		mdInfo.sCriterion.setRelVarMap(relVarMap);
	}
}
/**
 * Put a statement into {@link #methodCritMap methodCritMap}.
 * <br> A statement is a slice point, so the <code>point</code>
 * should be added into the set of slice points in the slice criterion
 * of the given method. Slice criterion
 * consists of slice points set and slice variable set.
 * <p>
 * @param sootMethod the method where the <code>point</code> is.
 * @param point the statement need to be added into slice criterion.
 */
private void putPointToCritMap(SootMethod sootMethod, Stmt point) {
	if (methodCritMap.containsKey(sootMethod)) {
		SliceCriterion criterion = (SliceCriterion) methodCritMap.get(sootMethod);
		criterion.getSlicePoints().add(point);
	} else {
		Set pointSet = new ArraySet();
		pointSet.add(point);
		methodCritMap.put(sootMethod, new SliceCriterion(pointSet, new ArraySet(), new ArraySet()));
	}
}
/**
 * @param sootClass class where the <code>field</code> is declared.
 * @param field the field need to be added into slice criterion.
 */
private void putPrivateInsFdToCritMap(SootClass sootClass, SootField field) {
	for (Iterator classIt = classInfoList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
        // Why is the commented if statement exist  in the first place?
		//if (!classInfo.sootClass.equals(sootClass))
		//	continue;
		for (Iterator mdIt = classInfo.methodsInfoList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			Set fieldRefs = getInstanceFieldRef(mdInfo.indexMaps.localAssMap(), field);
			for (Iterator refIt = fieldRefs.iterator(); refIt.hasNext();){
			    FieldRef fieldRef = (FieldRef) refIt.next();
				putFieldToCritMap(mdInfo.sootMethod, fieldRef);
			}
		}
	}
}
/**
 * @param sootClass class where the <code>field</code> is declared.
 * @param field the field need to be added into slice criterion.
 */
private void putPubInstanceFdToCritMap(SootClass sootClass, SootField field) {
	for (Iterator classIt = classInfoList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		for (Iterator mdIt = classInfo.methodsInfoList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			Set fieldRefs = getInstanceFieldRef(mdInfo.indexMaps.localAssMap(), field);
			for (Iterator refIt = fieldRefs.iterator(); refIt.hasNext();)
			    {
				FieldRef fieldRef = (FieldRef) refIt.next();
				putFieldToCritMap(mdInfo.sootMethod, fieldRef);
			    }
		}
	}
}
/**
 * Put a statement into {@link #methodCritMap methodCritMap}.
 * <br> A statement is a slice point, so the <code>point</code>
 * should be added into the set of slice points in the slice criterion
 * of the given method. Slice criterion
 * consists of slice points set and slice variable set.
 * <p>
 * @param sootMethod the method where the <code>point</code> is.
 * @param point the statement need to be added into slice criterion.
 */
private void putStatementToCritMap(SootMethod sootMethod, Stmt stmt) {
	if (methodCritMap.containsKey(sootMethod)) {
		SliceCriterion criterion = (SliceCriterion) methodCritMap.get(sootMethod);
		criterion.getSliceStatements().add(stmt);
	} else {
		Set stmtSet = new ArraySet();
		stmtSet.add(stmt);
		methodCritMap.put(sootMethod, new SliceCriterion(new ArraySet(), new ArraySet(), stmtSet));
	}
}
/**
 * @param sootClass class where <code>staticField</code> is declared.
 * @param field the field need to be added into slice criterion.
 */
private void putStaticFieldToCritMap(SootClass sootClass, SootField staticField) {
	for (Iterator classIt = classInfoList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		for (Iterator mdIt = classInfo.methodsInfoList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			Set staticFdRefs = getStaticFieldRef(mdInfo.indexMaps.localAssMap(), staticField);
			for (Iterator refIt = staticFdRefs.iterator(); refIt.hasNext();){
			    FieldRef staticFdRef = (FieldRef) refIt.next();
			    putFieldToCritMap(mdInfo.sootMethod, staticFdRef);
}
		}
	}
}
}
