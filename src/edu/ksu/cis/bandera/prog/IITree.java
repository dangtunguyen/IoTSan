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

import org.apache.log4j.Category;

class IITree //hierarchy of regular classes and interfaceMap
{
    private static final Category log = Category.getInstance(IITree.class);
    private IITreeNode classRoot;
    private int maxDepth;
    private int maxNodes; //used as a max size for arrays
    private HashMap implementationsMap; // maps an interface to a set of classes
				        // that implement it and direct subinterfaces
    public IITree(int num) {
	maxDepth = 0;
	maxNodes = num;
	classRoot = null;
	implementationsMap = new HashMap();
    }

    public SootMethod findDeclaringMethod(SootClass sootClass,
					  String    methodName,
					  List      parameterTypes,
					  Type      returnType) {

	log.debug("Looking for method " + methodName + " with return type " + returnType + " and parameter types " +
		  parameterTypes + " in SootClass " + sootClass + "...");

	if (sootClass.declaresMethod(methodName,parameterTypes,returnType)) {
	    // should we avoid the extra test, call getMethod directly,
	    // and catch the exception that it throws if method is not found?
	    return sootClass.getMethod(methodName, parameterTypes, returnType);
	}
	else {
	    if (sootClass.hasSuperClass()) {
		SootClass superClass = sootClass.getSuperClass();
		return findDeclaringMethod(superClass,
					   methodName, 
					   parameterTypes,
					   returnType);
	    } 
	    else {
		String errorMessage = "The SootClass " + sootClass + " doesn't declare method " + methodName +
		    " with return type " + returnType + " and parameter types " + parameterTypes +
		    " and it doesn't have a super class.";
		log.error(errorMessage);
		throw new RuntimeException(errorMessage);
					   
	    }
	}
	
    }

    public void findPairs(Set relevant, SootClass sc/*interface */,
			  String methodName,
			  List      parameterTypes,
			  Type      returnType) {
	Set temp = getImplementations(sc);
	   

	Iterator ti = temp.iterator();
	while(ti.hasNext()) {
	    Object tmpo = ti.next();
	    SootClass inspected = (SootClass)tmpo;
	    if(inspected.hasSuperClass()){
		IITreeNode startNode = get(inspected, classRoot);
		findPairs(relevant, startNode, methodName, parameterTypes, returnType);
	    }
	    else {
		//inspected is a subinterface and we need to do the same for classes
		//that implement it	
		findPairs(relevant, inspected, methodName, parameterTypes, returnType);
	    }
	}
    }

    public void findPairs(Set      relevant,
			  IITreeNode   node,
			  String    methodName,
			  List      parameterTypes,
			  Type      returnType) {

	SootClass sc = node.getElement();

	log.debug("Finding pairs for class " + sc + " for method " + methodName +
		  " with return type " + returnType + " and parameter types " + parameterTypes + "...");

	SootMethod sm;
	if (sc.declaresMethod(methodName,parameterTypes,returnType)) 
	    // should we avoid the extra test, call getMethod directly,
	    // and catch the exception that it throws if method is not found?
	    sm = sc.getMethod(methodName, parameterTypes, returnType);
	else {
	    if (sc.hasSuperClass()) {
		SootClass superClass = sc.getSuperClass();
		sm = findDeclaringMethod(superClass,
					 methodName, 
					 parameterTypes,
					 returnType);
	    } 
	    else {
		log.error("This method, " + methodName + ", was not found in the hierarchy.  " +
			  "returnType = " + returnType + "parameterTypes = " + parameterTypes);
		// one should never get here !!!!!!!!!!!!!!!!!!!!!!!!
		throw new RuntimeException("Method not found in hierarchy: " + methodName);
	    }
	}
	ClassMethodPair finalPair = new ClassMethodPair(sc, sm, node.getDepth());
	relevant.add(finalPair);
	
	//need to do the same for all children of the node
	//that has sootClass as its element
	
	for(int i=0; i<node.getChildrenCount(); i++) {
	    IITreeNode temp = (node.getChildren())[i];
	    recursiveFindPairs(relevant, sm, temp, methodName, parameterTypes, returnType);
	}
    }

    /**
     * Given a SootClass sc finds a node that corresponds to it
     * and returns it
     * If the node doesn't exist then returns null
     */
    public IITreeNode get(SootClass sc, IITreeNode node)
    {
	if(node == null)
	    return null;

	if(sc == null)
	    throw new RuntimeException("bumps");
	if(((node.getElement()).getName()).compareTo(sc.getName()) == 0 )
	    return node;
	else
	    {
		for(int i=0; i<node.getChildrenCount(); i++)
		    {
			IITreeNode temp = this.get(sc, (node.getChildren())[i]); 
			if(temp != null)
			    return temp;
		    }
		
	    }
	return null;
    }
    /**
     * Given an interface Class find all classes that implement it
     * including classes that implement its subinterfaces
     */
    public Set getImplementations(SootClass sc)
    {
	Set implementations = new ArraySet(); 
	Set temp = (Set)implementationsMap.get(sc);
	if(temp == null)
	    {
		System.out.println("No mapping for :"+sc.getName()+" !!!!");
		throw new RuntimeException("No mapping");
	    }
	Iterator ti = temp.iterator();
	while(ti.hasNext())
	    {
		SootClass tempClass = (SootClass)ti.next();
		if(tempClass.hasSuperClass())
		    //this is a regular class
		    //that needs to be added to "implementations" set
		    implementations.add(tempClass);
		else
		    {
			//this is a subinterface
			//and we need to find all classes that implement it
			Iterator ii = this.getImplementations(tempClass).iterator();
			while (ii.hasNext())
			    implementations.add((SootClass)ii.next());
		    }
	    }	

	return implementations;
    }
    public HashMap getImplementationsMap()
    {
	return implementationsMap;
    }
    public int getMaxDepth() 
    {
	return maxDepth;
    }
    public IITreeNode getRoot()
    {
	return classRoot;
    }
    public void insert(Hashtable table)
    {
	LinkedList pendingList = new LinkedList();
	for(Enumeration e = table.elements(); e.hasMoreElements();)
	    {
		SootClass sc = (SootClass)e.nextElement();

		// check whether this is a regular class
		// or an interface
		// put regular class into the tree

		if(sc.hasSuperClass())
		    recursiveInsert(sc);
		if(sc != null)
		    {
	  	        for (Iterator inIt = sc.getInterfaces().iterator(); inIt.hasNext();)
			    {
				SootClass implementedClass = (SootClass) inIt.next();
				if (implementationsMap.containsKey(implementedClass))
				    ((Set) implementationsMap.get(implementedClass)).add(sc);
				else
				    { 
			  	        Set impSet = new ArraySet();
			  	        impSet.add(sc);
					implementationsMap.put(implementedClass, impSet);
				    }
			    }
		    }
	    }
    }
    public void number()
    {
	maxDepth = classRoot.number(0);
    }
    public void print()
    {
	this.classRoot.print();
    }
    private void recursiveFindPairs(Set relevant, 
				    SootMethod sm, 
				    IITreeNode node, 
				    String methodName,
				    List parameterTypes, 
				    Type returnType)
    {
	SootClass sc = node.getElement();
	if (sc.declaresMethod(methodName,parameterTypes,returnType)) 
	    sm = sc.getMethod(methodName, parameterTypes, returnType);
	ClassMethodPair finalPair = new ClassMethodPair(sc, sm, node.getDepth());
	relevant.add(finalPair);

	for(int i=0; i<node.getChildrenCount(); i++)
	    {
		IITreeNode temp = (node.getChildren())[i];
		recursiveFindPairs(relevant, sm, temp, methodName, parameterTypes, returnType);
	    }
	
    }
    private IITreeNode recursiveInsert(SootClass sc)
    {

	IITreeNode temp = get(sc, classRoot);
	if(temp!=null)
	    return temp;
	if(((sc.getName()).compareTo("java.lang.Object"))==0)
	    {
		classRoot = new IITreeNode(sc, maxNodes);
		return classRoot;
	    }

	IITreeNode parent = get(sc.getSuperClass(), classRoot);
	if(parent == null)
	    parent = recursiveInsert(sc.getSuperClass());
	temp = new IITreeNode(sc, maxNodes);
	parent.addChild(temp);
	return temp;
    }
}
