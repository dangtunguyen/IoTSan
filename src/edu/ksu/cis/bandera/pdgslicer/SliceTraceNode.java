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

import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.annotation.*;
import java.util.*;

/**
 * This class is for PDG browser. Each object of this class will record
 * one node in CFG which is included in the slice of one method. All the 
 * slice trace nodes are linked by dependencies. 
 * <br> For example, suppose we have
 * <br> n1 cd--> n2, n1 dd--> n3, n1 synd--> n4
 * <br> n5 dd--> n2, n6 cd--> n4
 * <br> where --> can be read as "on", and 
 * cd, dd, synd are saying control dependent, data dependent, synchronization dependent
 * repectively.
 * In this case, we say 
 * <br> (1) n1 has 3 slice children: n2(cd), n3(dd), n4(synd), if n1
 * is included in the slice.
 * <br> (2) n2 has 2 slice parents: n5(dd), n1(cd), if both n1 and n5 are in the slice. 
 * In other words, how many slice parents n2 can have depends on how many parents
 * of n2 are in the slice.
 * <br> Finally, all objects of SliceTraceNode for a method could construct a net
 * (bidirectional link) linked by all kinds of dependency(cd, dd, synd etc.).
 */ 
 
public class SliceTraceNode
{
  /**
   * Method where the node is. 
   */
  public MethodInfo methodInfo;
  /**
   * The annotation of the node (statement).
   */
  public Annotation stmtAnnotation = null;
  /**
   * Slice parents of current node.
   * <br> A map from {@link SliceTraceNode SliceTraceNode} to
   * {@link Kind Kind}.
   */
  public Map parents ;   //map from parent (SliceTraceNode) to kind (data, control etc.)
  /**
   * Slice children of current node.
   * <br> A map from {@link SliceTraceNode SliceTraceNode} to
   * {@link Kind Kind}
   */
  public Map children ; 

/**
 * Initial constructor.
 */
public SliceTraceNode() {
	methodInfo = null;
	parents = new HashMap();
	children = new HashMap();
}
  /**
   * Constructor for method and annotation.
   * <p>
   * @param sm sootmethod where the annotation <code>sa</code> is.
   * @param sa annotation for current node (statement).
   */
public SliceTraceNode(SootMethod sm, Annotation sa)
	{
	  methodInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sm);
	  stmtAnnotation = sa;
	  parents = new HashMap();
	  children = new HashMap();
	}
  /**
   * Constructor for method and statement.
   * <p>
   * @param mi method where the statement <code>s</code> is.
   * @param s statement for current node.
   */
public SliceTraceNode(MethodInfo mi, Stmt s) {

	methodInfo = mi;
	parents = new HashMap();
	children = new HashMap();
	Annotation mda = null;
	AnnotationManager annotationManager = CompilationManager.getAnnotationManager();
	try {
		mda = (Annotation) annotationManager.getAnnotation(methodInfo.sootClass, methodInfo.sootMethod);
	} catch (Exception ex) {
		System.out.println("There is an exception in geting Annotation in SliceTraceNode");
		//System.exit(0);
	}
	try {
		stmtAnnotation = mda.getContainingAnnotation(s);
	} catch (AnnotationException ae) {
		System.out.println("there is an AnnotationException! And stmtAnnotation may be null in SliceTraceNode");
	}
	/*
	if (stmtAnnotation.toString().equals("")) 
	System.out.println(methodInfo.sootMethod+"--stmt : " + s +" 's annotation is blank");
	*/
}
/** 
   * Add a slice trace node into current node list.
   * <br> Set the parent as child for current node.
   * <p>
   * @param stn node being added.
   * @param kind the dependency kind of the node <code>stn</code>.
   */
public void add(SliceTraceNode stn, Integer kind) {
	this.children.put(stn, kind);
	stn.parents.put(this, kind);
}
public boolean equals(Object o) {
	if (!(o instanceof SliceTraceNode))
		return false;
	SliceTraceNode stn = (SliceTraceNode) o;
	return (methodInfo == stn.methodInfo) && (stmtAnnotation == stn.stmtAnnotation);
	// &&(nodeKind==stn.nodeKind)&&(sourceNode==stn.sourceNode);
}
  /**
   * See if a slice trace node <code>stn</code> is a child of 
   * current slice trace node.
   * <p>
   * @param stn query slice trace node.
   */
public boolean isChild(SliceTraceNode stn)
	{
Set childrenSet = children.keySet();
if (childrenSet.contains(stn)) return true;
return false;      
	}
public String toString() {
  if (stmtAnnotation.toString().equals(""))
	return "BLANK :: " + methodInfo.sootMethod;
  else
	return stmtAnnotation+" :: " + methodInfo.sootMethod;
}
}
