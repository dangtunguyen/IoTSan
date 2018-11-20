package edu.ksu.cis.bandera.prog;

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
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.*;
import java.util.Hashtable;
import edu.ksu.cis.bandera.jext.*;

public class KSUOptimizing
{
	private static Hashtable methodLocalPackingTable;
	public static void clearJJJCTemp(List localList) {
		List tempList = new ArrayList();
		Iterator locIt = localList.iterator();
		while (locIt.hasNext()) {
			Local loc = (Local) locIt.next();
			if ((loc.toString().indexOf("JJJCTEMP$") >= 0)
					|| (loc.toString().indexOf("SLABS$") >= 0)) {
				if (!loc.toString().endsWith("JJJCTEMP$0"))
					tempList.add(loc);
			}
		}
		localList.removeAll(tempList);
	}
	public static void packLocalsForBody(SootMethod sootMethod) {
		StmtBody body = (StmtBody) sootMethod.getBody(Jimple.v());
		Map localToGroup = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
		Map groupToColorCount = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
		Map localToColor = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
		Map localToNewLocal;

		// Assign each local to a group, and set that group's color count to 0.
		{
			Iterator localIt = body.getLocals().iterator();
			while (localIt.hasNext()) {
				Local l = (Local) localIt.next();
				Object g = l.getType();
				localToGroup.put(l, g);
				if (!groupToColorCount.containsKey(g)) {
					groupToColorCount.put(g, new Integer(0));
				}
			}
		}

		// Assign colors to the parameter locals.
		{
			Iterator codeIt = body.getStmtList().iterator();
			while (codeIt.hasNext()) {
				Stmt s = (Stmt) codeIt.next();
				if (s instanceof IdentityStmt && ((IdentityStmt) s).getLeftOp() instanceof Local) {
					Local l = (Local) ((IdentityStmt) s).getLeftOp();
					Object group = localToGroup.get(l);
					int count = ((Integer) groupToColorCount.get(group)).intValue();
					localToColor.put(l, new Integer(count));
					count++;
					groupToColorCount.put(group, new Integer(count));
				}
			}
		}

		// Call the graph colorer.
		FastColorer.assignColorsToLocals(body, localToGroup, localToColor, groupToColorCount);

		// Map each local to a new local.
		{
			List originalLocals = new ArrayList();
			localToNewLocal = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
			Map groupIntToLocal = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
			originalLocals.addAll(body.getLocals());
			body.getLocals().clear();
			Iterator localIt = originalLocals.iterator();
			while (localIt.hasNext()) {
				Local original = (Local) localIt.next();
				Object group = localToGroup.get(original);
				int color = ((Integer) localToColor.get(original)).intValue();
				GroupIntPair pair = new GroupIntPair(group, color);
				Local newLocal;
				if (groupIntToLocal.containsKey(pair))
					newLocal = (Local) groupIntToLocal.get(pair);
				else {
					newLocal = new Local(original.getName(), (Type) group);
					groupIntToLocal.put(pair, newLocal);
					body.getLocals().add(newLocal);
				}
				methodLocalPackingTable.put(new LocalExpr(sootMethod, original), new LocalExpr(sootMethod, newLocal));
				localToNewLocal.put(original, newLocal);
			}
		}


		// Go through all valueBoxes of this method and perform changes
		{
			Iterator codeIt = body.getStmtList().iterator();
			while (codeIt.hasNext()) {
				Stmt s = (Stmt) codeIt.next();
				Iterator boxIt = s.getUseAndDefBoxes().iterator();
				while (boxIt.hasNext()) {
					ValueBox box = (ValueBox) boxIt.next();
					if (box.getValue() instanceof Local) {
						Local l = (Local) box.getValue();
						box.setValue((Local) localToNewLocal.get(l));
					}
				}
			}
		}
	}
	private static void packLocalsForBody2(SootMethod sootMethod) {
		StmtBody body = (StmtBody) sootMethod.getBody(Jimple.v());
		Map localToGroup = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
		Map groupToColorCount = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
		Map localToColor = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
		Hashtable localToNewLocal;

		// Assign each local to a group, and set that group's color count to 0.
		{
			Iterator localIt = body.getLocals().iterator();
			while (localIt.hasNext()) {
				Local l = (Local) localIt.next();

				//******* added by Hongjunn 1/26/00
				//if (l.toString().indexOf("JJJCTEMP$")>=0)
				{
					Object g = l.getType();
					localToGroup.put(l, g);
					if (!groupToColorCount.containsKey(g)) {
						groupToColorCount.put(g, new Integer(0));
					}
				}
			}
		}

		// Assign colors to the parameter locals.
		{
			Iterator codeIt = body.getStmtList().iterator();
			while (codeIt.hasNext()) {
				Stmt s = (Stmt) codeIt.next();
				if (s instanceof IdentityStmt && ((IdentityStmt) s).getLeftOp() instanceof Local) {
					Local l = (Local) ((IdentityStmt) s).getLeftOp();


					//******** added by Hongjun 1/26/00
					//if (l.toString().indexOf("JJJCTEMP$")>=0)
					{
						Object group = localToGroup.get(l);
						int count = ((Integer) groupToColorCount.get(group)).intValue();
						localToColor.put(l, new Integer(count));
						count++;
						groupToColorCount.put(group, new Integer(count));
					}
				}
			}
		}

		// Call the graph colorer.
		FastColorer.assignColorsToLocals(body, localToGroup, localToColor, groupToColorCount);

		// Map each local to a new local.
		{
			List originalLocals = new ArrayList();
			localToNewLocal = new Hashtable(body.getLocalCount() * 2 + 1, 0.7f);
			Map groupIntToLocal = new HashMap(body.getLocalCount() * 2 + 1, 0.7f);
			originalLocals.addAll(body.getLocals());

			//****** this should only clear the JJJCTEMP$ local
			//body.getLocals().clear();

			//System.out.println("locals in body before clear: " + body.getLocals());
			clearJJJCTemp(body.getLocals());

			//System.out.println("locals in body after clear: " + body.getLocals());

			Iterator localIt = originalLocals.iterator();
			while (localIt.hasNext()) {
				Local original = (Local) localIt.next();
				Object group = localToGroup.get(original);
				int color = ((Integer) localToColor.get(original)).intValue();
				GroupIntPair pair = new GroupIntPair(group, color);
				Local newLocal;
				if (groupIntToLocal.containsKey(pair))
					newLocal = (Local) groupIntToLocal.get(pair);
				else {
					newLocal = new Local(original.getName(), (Type) group);
					groupIntToLocal.put(pair, newLocal);

					//****** if statement is added by Hongjun 1/26/00
					if (!body.getLocals().contains(newLocal))
						body.getLocals().add(newLocal);
				}

				// System.out.println("original local : " + original + "   -->  new local : " + newLocal);
				//System.out.println("toString: " + original.toString() + "   toBriefString() : " + original.toBriefString());

				/* [Thomas, May 24, 2017]
				 * Don't replace a local var with a static var
				 * Add: "&& !newLocal.getName().startsWith("_static_")"
				 * */
				if(newLocal.getName().startsWith("_static_") && !body.getLocals().contains(original))
				{
					body.getLocals().add(original);
				}
				
				//added by Hongjun 1/26/00
				if (((original.toString().indexOf("JJJCTEMP$") >= 0)
						|| (original.toString().indexOf("SLABS$") >= 0))
						&& !newLocal.getName().startsWith("_static_")){
					if (!original.toString().endsWith("JJJCTEMP$0")) {
						//System.out.println("original: " + original + "   -->  new local : " + newLocal);
						localToNewLocal.put(original, newLocal);
						methodLocalPackingTable.put(new LocalExpr(sootMethod, original), new LocalExpr(sootMethod, newLocal));
					} else {
						localToNewLocal.put(original, original);
						methodLocalPackingTable.put(new LocalExpr(sootMethod, original), new LocalExpr(sootMethod, original));
					}
				} else {
					localToNewLocal.put(original, original);
					methodLocalPackingTable.put(new LocalExpr(sootMethod, original), new LocalExpr(sootMethod, original));
				}
			}
		}

		//      System.out.println("locals in body after adds: " + body.getLocals());

		// Go through all valueBoxes of this method and perform changes
		{
			Iterator codeIt = body.getStmtList().iterator();
			while (codeIt.hasNext()) {
				Stmt s = (Stmt) codeIt.next();
				Iterator boxIt = s.getUseAndDefBoxes().iterator();
				while (boxIt.hasNext()) {
					ValueBox box = (ValueBox) boxIt.next();
					if (box.getValue() instanceof Local) {
						Local l = (Local) box.getValue();

						//******** added by Hongjun 1/26/00
						//Local newLoc = (Local) localToNewLocal.get(l);
						//if (newLoc !=null)
						box.setValue((Local) localToNewLocal.get(l));
					}
				}
			}
		}
	}
	public static void packLocalsForClasses(SootClass classes[]) {
		methodLocalPackingTable = new Hashtable();
		for (int j = 0; j < classes.length; j++) {
			SootClass sootClass = classes[j];
			List methodsList = sootClass.getMethods();
			Object methods[] = methodsList.toArray();
			SootMethod sootMethod;
			for (int i = 0; i < methods.length; i++) {
				sootMethod = (SootMethod) methods[i];
				packLocalsForBody2(sootMethod);
			}
		}
		AnnotationManager am = CompilationManager.getAnnotationManager();
		am.setLocalPackingTable(methodLocalPackingTable);
	}
}
