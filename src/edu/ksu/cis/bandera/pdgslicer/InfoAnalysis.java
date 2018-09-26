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

/**
* This class is for
* <ul>
* <li> building up {@link MethodInfo MethodInfo} for
* each method in one class;
* <li> recognizing all native methods;
* <li> filling {@link SootMethodInfoMap SootMethodInfoMap}.
* </ul>
*/

public class InfoAnalysis {
	/**
	* a list of {@link MethodInfo MethodInfo} for all methods in the class.
	*/
	private List methodsInClass = new ArrayList();
	/**
	* a set of {@link SootMethod SootMethods} that are native methods.
	*/ 
	protected static Set nativeMdSig;
/**
* Analyse one class, and build up {@link MethodInfo MethodInfo} for each method in
* the class.
* @param sootClass class need to be analysed.
*/
public InfoAnalysis(SootClass sootClass) {
	List methodsList = sootClass.getMethods();
	SootMethod sootMethod;

	//get native method signatures in the class sootClass
	{
		//InfoAnalysis.nativeMdSig = new ArraySet();
		for (Iterator nt = methodsList.iterator(); nt.hasNext();) {
			sootMethod = (SootMethod) nt.next();
			int modifiers = sootMethod.getModifiers();
			if (Modifier.isNative(modifiers)) {
				InfoAnalysis.nativeMdSig.add(sootMethod);
			}
		}
	}
	for (Iterator mit = methodsList.iterator(); mit.hasNext();) {
		sootMethod = (SootMethod) mit.next();
		if (InfoAnalysis.nativeMdSig.contains(sootMethod)) {
			System.out.println("Method " + sootMethod.getName() + " is a native method");
			continue;
		}
		if (!Slicer.reachableMethods.contains(sootMethod))
			continue;
		IndexMaps indexMaps = new IndexMaps(sootMethod);
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.stmtList = indexMaps.getStmtList();
		methodInfo.originalStmtList = indexMaps.getOriginalStmtList();
		methodInfo.sootClass = sootClass;
		methodInfo.sootMethod = sootMethod;
		methodInfo.indexMaps = indexMaps;
		methodInfo.methodPDG = null;
		methodInfo.REF = indexMaps.getREF();
		methodInfo.MOD = indexMaps.getMOD();
		methodInfo.sCriterion = null;
		methodInfo.sliceSet = null;
		methodInfo.increCriterion = null;
		methodInfo.possibleReadyDependCallSite = new ArraySet();
		methodInfo.removedStmt = new ArraySet();
		methodsInClass.add(methodInfo);
		Slicer.sootMethodInfoMap.put(sootMethod, methodInfo);
	}
}
/**
* Get a list of {@link MethodInfo MethodInfo} for each method in the class.
*/
public List getMethodsInfoList()
	{
	  return methodsInClass;
	}
}
