package edu.ksu.cis.bandera.prog;

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
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.*;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import edu.ksu.cis.bandera.jjjc.*;

public class InvokeResolver {
	static InvokeResolver staticResolver;  // this is a hack
	static Hashtable compiledClasses;
	private IITree iiTree;  // Inheritence/Interface Tree 

	public InvokeResolver(Hashtable storedClasses)
	{
  	iiTree = new IITree(storedClasses.size());
		iiTree.insert(storedClasses);
		iiTree.number();
		compiledClasses = storedClasses;
		staticResolver = this;  // This is a hack
	}
	public Vector getPairs(SootClass sc, String methodName, List parameterTypes, Type returnType)
	{
	Set relevant = new ArraySet();

		if (methodName.equals("<init>")) 
		{
		  // Constructors are special invokes so inheritence doesn't matter
		  return new Vector();

	} else if (sc.hasSuperClass() || ((sc.getName()).compareTo("java.lang.Object")==0))
	{
	    //this is a regular class 
	    //need to output all children along with the methods
	    IITreeNode startNode = iiTree.get(sc, iiTree.getRoot());
	    if(startNode != null)
	    	iiTree.findPairs(relevant, startNode, methodName, parameterTypes, returnType);
	    else
		System.out.println("Declaring SootClass < "+sc+ " > is a library class that wasn't supplied to the program!!!!!!!!!!");
	}
	else
	{
	    //sc here is an interface 
	    iiTree.findPairs(relevant, sc, methodName, parameterTypes, returnType); 
	}

		Vector orderedPairs = new Vector();
		HashMap classMethodMap = new HashMap();
		Iterator initMap = relevant.iterator();
		while (initMap.hasNext()) {
		  ClassMethodPair currentPair = (ClassMethodPair)initMap.next();
		  classMethodMap.put(currentPair.getInvClass(), currentPair.getInvMethod());
		}
	  

		for (int i=iiTree.getMaxDepth(); i>=0; i--) {
		  Iterator pairs = relevant.iterator();
		  while (pairs.hasNext()) {
			ClassMethodPair currentPair = (ClassMethodPair)pairs.next();
			if (currentPair.getDepth() == i) 
			  // If the current pair corresponds to a class that does not
			  // override the invoked method, then we skip over adding the 
			  // pair and lets its parent's "pair" represent the method to 
			  // to be invoked for this (sub)class
			  if (!currentPair.getInvClass().hasSuperClass() ||
				  !classMethodMap.containsKey(currentPair.getInvClass().getSuperClass()) ||
				  currentPair.getInvMethod() != classMethodMap.get(currentPair.getInvClass().getSuperClass())) {
				orderedPairs.addElement(currentPair);
			  }
		  }
		}
		return orderedPairs;
	}
	static public InvokeResolver getResolver() {  // this is a hack
	if (compiledClasses != CompilationManager.getCompiledClasses()) {
	  staticResolver = new InvokeResolver( 
						 CompilationManager.getCompiledClasses());
	}
	  return staticResolver;
	}
	public static void  main(String[] args)
	{
		SootClassManager classManager = new SootClassManager();
	//Prepare the Compilation Manager
	CompilationManager.reset();
	CompilationManager.setFilename(args[0]);
	CompilationManager.setClasspath(".");
	CompilationManager.setIncludedPackagesOrTypes(new String[0]);

		//Compile 
		try
		{
				CompilationManager.compile();
		} catch (Exception e)
		{
		System.out.println(e);
				System.out.println("Compilation failed");
	}
		Hashtable storedClasses = CompilationManager.getCompiledClasses();
	InvokeResolver resolver = new InvokeResolver(storedClasses);

		for(Enumeration e = storedClasses.elements(); e.hasMoreElements();)
		{
			SootClass sc = (SootClass)e.nextElement();
			String className = sc.getName();
			List methodsList = sc.getMethods();
			Iterator mi = methodsList.iterator();

			while( mi.hasNext())
			{
				SootMethod sm = (SootMethod)mi.next();
				System.out.println("\nSoot Method: "+sm.toString());

				if(sm.isBodyStored(Jimple.v()))
				{
					JimpleBody jb = (JimpleBody)sm.getBody(Jimple.v());
					for(Iterator si = jb.getStmtList().iterator(); si.hasNext();
)
					{
						Stmt s = (Stmt)si.next();
						System.out.println(s);

						if (s instanceof JInvokeStmt)
						{

							SootClass methodDecl = (((InvokeExpr)(((JInvokeStmt)s).getInvokeExpr())).getMethod()).getDeclaringClass();
							String methodName = (((InvokeExpr)(((JInvokeStmt)s).getInvokeExpr())).getMethod()).getName();
							List parameterTypes = (((InvokeExpr)(((JInvokeStmt)s).getInvokeExpr())).getMethod()).getParameterTypes();
							Type returnType = (((InvokeExpr)(((JInvokeStmt)s).getInvokeExpr())).getMethod()).getReturnType();
							Vector orderedPairs = resolver.getPairs(methodDecl, methodName, parameterTypes, returnType);
							System.out.println("Class/Method pairs: ");
							for (int i=0; i<orderedPairs.size(); i++) 
							  System.out.println("  "+
								 (ClassMethodPair)orderedPairs.elementAt(i));
						}
					}
				}
			}
		}
	}
}
