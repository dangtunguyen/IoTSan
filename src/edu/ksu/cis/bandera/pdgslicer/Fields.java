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
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.pdgslicer.exceptions.*;
/**
 * This class is for grouping use and definition of field references
 * in one method.
 */
public class Fields {
  /**
   * A set of {@link DataBox DataBox} for static field reference.
   * One <code>DataBox</code> means a set of static field references
   * used or defined by a statement.
   */
	Set staticFields;
  /**
   * A set of {@link DataBox DataBox} for instance field references 
   * which are declared in current class.
   */
	Set instanceFields; //class fields -- public fields
  /** 
   * A set of {@link DataBox DataBox} for parameter field references.
   * <p>
   * @see IndexMaps#isParaField(InstanceFieldRef,Stmt).
   */
	Set paraFields;
  /**
   * A set of {@link DataBox DataBox} for other instance field references
   * which are declared in other class rather than current class.
   */   
	Set otherInsFds;

  /**
   * Initialize {@link #staticFields staticFields},
   * {@link #instanceFields instanceFields}, and 
   * {@link #paraFields paraFields} to <code>null</code>.
   */
  public Fields()
	{
	  staticFields = null;
	  instanceFields = null;
	  paraFields = null;
	}
  /**
   * Get the index of parameter, if the base value of given instance
   * field reference <code>insFieldRef</code> is a parameter or a local
   * copy of a parameter.
   * <p>
   * @param insFieldRef query instance field reference.
   * @param paraLocals local variables for all parameters.
   * @param simpleLocalCopiesInCalledMd simple local copies graph for the method
   * where <code>insFieldRef</code> is used or defined.
   * @param interStmt the statement which use or define <code>insFieldRef</code>.
   * @return the index of the parameter which is base value of 
   * <code>insFieldRef</code>.
   * @throws BaseValueNonLocalException if the base value of <code>insFieldRef</code>
   * is not a local variable.
   * @throws NoParaFieldFoundException if can not find a parameter corresponding to
   * the base value of <code>insFieldRef</code>.
   */
static int getParaIndex(InstanceFieldRef insFieldRef, Local[] paraLocals, SimpleLocalCopies simpleLocalCopiesInCalledMd, Stmt interStmt) {
	//determin the base using the parameter binding function
	Value base = insFieldRef.getBase();
	if (!(base instanceof Local))
		throw new BaseValueNonLocalException("parameter instance field reference should be local variable");
	Local paraLocal = (Local) base;
	int paraIndex = -1;

	//determine the index of paraLocal
	for (int j = 0; j < paraLocals.length; j++) {
		if (paraLocal.equals(paraLocals[j])) {
			paraIndex = j;
			break;
		} else
			if (simpleLocalCopiesInCalledMd.isLocalCopyOfBefore(paraLocal, paraLocals[j], interStmt)) {
				paraIndex = j;
				break;
			}
	}
	if (paraIndex == -1)
		throw new NoParaFieldFoundException("Parameter instance field reference should be found in the paraLocals array");
	return paraIndex;
}
/**
 * Insert the method's description here.
 * Creation date: (6/18/2001 11:36:41 AM)
 * @return boolean
 * @param value ca.mcgill.sable.soot.jimple.Value
 */
static boolean isThisRef(SootMethod enclosingMethod, Value base, Stmt thisRefStmt) {
	if (thisRefStmt==null) return false;
	Set refSet = Slicer.BOFA_Analysis.referenceValueSet(base, enclosingMethod);
	if (refSet.contains(thisRefStmt))
		return true;
	return false;
}
/**
   * Merge fields information of called method into current fields information
   * of current method in terms of one method call.
   * <p>
   * @param fields all fields information for called method.
   * @param site one call site in current method body.
   * @param methodInfo method information of current method.
   * @param calledMdInfo method information of called method.
   */
public void merge(Fields fields, CallSite site, MethodInfo methodInfo, MethodInfo calledMdInfo) {
	//methodInfo for calling method, not for called method 

	Set newStaticFields = new ArraySet();
	Set staticFdInCalledMd = fields.staticFields;
	for (Iterator i = staticFdInCalledMd.iterator(); i.hasNext();) {
		DataBox dbx = (DataBox) i.next();
		Set staticFdSet = dbx.getInterferVars();
		DataBox newdbx = new DataBox(site.callStmt, staticFdSet);
		if (!this.staticFields.contains(newdbx))
			newStaticFields.add(newdbx);
	}
	this.staticFields.addAll(newStaticFields);
	Set newInstanceFields = new ArraySet();
	newInstanceFields.addAll(this.instanceFields);

	//constructing newLocalFields
	//including that instanceFields of the other method with the name
	//changed to call object
	//need to know the base of call method here

	Value base = null;
	NonStaticInvokeExpr nonStaticInvokeExpr = null;
	boolean nonStaticInvoke = false;
	InvokeExpr invokeExpr = site.invokeExpr;
	if (invokeExpr instanceof NonStaticInvokeExpr) {
		nonStaticInvokeExpr = (NonStaticInvokeExpr) invokeExpr;
		base = nonStaticInvokeExpr.getBase();
		nonStaticInvoke = true;
	} else {
		//System.out.println("We don't process static invoke at this moment");
	}
	Set insFdAtCallSite = new ArraySet();
	if (nonStaticInvoke) {
		//decide if base is the local copy of this


		Set insFdInCallee = fields.instanceFields;
		for (Iterator i = insFdInCallee.iterator(); i.hasNext();) {
			DataBox insFdBox = (DataBox) i.next();
			Set insFields = insFdBox.getInterferVars();
			insFdAtCallSite.addAll(BuildPDG.cloneAndChangeBase(insFields, base));
		}
	}
	this.instanceFields = newInstanceFields;
	Set newParaFields = new ArraySet();
	newParaFields.addAll(this.paraFields);
	Set paraFieldsAtThisSite = new ArraySet();
	Set otherInsFdAtThisSite = new ArraySet();
	Set newOtherInsFd = new ArraySet();
	newOtherInsFd.addAll(this.otherInsFds);

	//filter out parameter fields at this call site, from instance
	//field at this site
	if (insFdAtCallSite != null) {
		for (Iterator i = insFdAtCallSite.iterator(); i.hasNext();) {
			InstanceFieldRef insFd = (InstanceFieldRef) i.next();
			if (methodInfo.indexMaps.isParaField(insFd, site.callStmt))
				paraFieldsAtThisSite.add(insFd);
		}
	}
	Set paraFieldsAfterChangeBase = parametersLocalize(calledMdInfo, fields, invokeExpr);

	//determine if paraFieldsAfterChangeBase are paraFields in current md

	for (Iterator jj = paraFieldsAfterChangeBase.iterator(); jj.hasNext();) {
		InstanceFieldRef paraFieldCalled = (InstanceFieldRef) jj.next();
		if (methodInfo.indexMaps.isParaField(paraFieldCalled, site.callStmt))
			paraFieldsAtThisSite.add(paraFieldCalled);
		else
			if (Fields.isThisRef(methodInfo.sootMethod, paraFieldCalled.getBase(), methodInfo.indexMaps.getThisRefStmt()))
				insFdAtCallSite.add(paraFieldCalled);
			else
				otherInsFdAtThisSite.add(paraFieldCalled);
	}
	if (paraFieldsAtThisSite.size() != 0) {
		DataBox newdbx = new DataBox(site.callStmt, paraFieldsAtThisSite);
		newParaFields.add(newdbx);
	}
	this.paraFields = newParaFields;
	if (!insFdAtCallSite.isEmpty()) {
		DataBox newdbx = new DataBox(site.callStmt, insFdAtCallSite);
		newInstanceFields.add(newdbx);
	}
	this.instanceFields = newInstanceFields;
	if (!otherInsFdAtThisSite.isEmpty()) {
		DataBox newdbx = new DataBox(site.callStmt, otherInsFdAtThisSite);
		newOtherInsFd.add(newdbx);
	}
	this.otherInsFds = newOtherInsFd;
}
  public boolean noFieldsInMethod()
	{
	  return (staticFields.size()==0 && instanceFields.size()==0 &&
	      paraFields.size() == 0 );
	  //&& localFields.size() == 0);
	}
  public boolean noInstanceFieldsInMethod()
	{
	  return instanceFields.size()==0;
	}
  public boolean noParaFields()
	{
	  return paraFields.size() == 0;
	}
  public boolean noStaticFieldsInMethod()
	{
	  return staticFields.size()==0;
	}
  /** 
   * Implement binding function of parameters.
   * <br>For example, given an invoke expression like
   * <code> obj.calledMethod(actualArg1, acutalArg2);</code>,
   * and the method <code>calledMethod(formalArg1, formalArg2){...}</code>,
   * there are some parameter fields in the body of <code>calledMethod</code> such like
   * <code>formalArg1.fd1, formalArg2.fd2</code> etc. To count (merge) those fields into
   * those of current method, it is necessary to change those <code>formalArg</code>
   * base value into <code>actualArg</code> value.
   * <p>
   * @param calledMdInfo method information of called method.
   * @param fields fields information of called method
   * @param invokeExpr invoke (called method) expression in current method.
   * @return a {@link Set Set} of {@link InstanceFieldRef InstanceFieldRef} which is 
   * changed base value from <code>formalArg</code> to <code>actualArg</code>.
   */
static Set parametersLocalize(MethodInfo calledMdInfo, Fields fields, InvokeExpr invokeExpr) {
	SimpleLocalCopies simpleLocalCopiesInCalledMd = new SimpleLocalCopies((CompleteStmtGraph) calledMdInfo.indexMaps.getStmtGraph());
	Local paraLocals[] = calledMdInfo.indexMaps.getParaLocalSet();
	Set paraFieldsInCalledMd = fields.paraFields;
	Set paraFieldsAfterChangeBase = new ArraySet();
	for (Iterator i = paraFieldsInCalledMd.iterator(); i.hasNext();) {
		DataBox insFieldRefBox = (DataBox) i.next();
		Set insFieldRefs = insFieldRefBox.getInterferVars();
		for (Iterator k = insFieldRefs.iterator(); k.hasNext();) {
			Object element = (Object) k.next();
			if (element instanceof Local)
				continue;
			InstanceFieldRef insFieldRef = (InstanceFieldRef) element;

			//parameter index in caller method
			int paraIndex = getParaIndex(insFieldRef, paraLocals, simpleLocalCopiesInCalledMd, insFieldRefBox.getInterferStmt());
			InstanceFieldRef newInsFieldRef = Jimple.v().newInstanceFieldRef(invokeExpr.getArg(paraIndex), insFieldRef.getField());
			paraFieldsAfterChangeBase.add(newInsFieldRef);
		}
	}
	return paraFieldsAfterChangeBase;
}
  public String toString()
	{
	  return "\nStaticFields: " + staticFields + "\n" + 
	"instanceFields: " + instanceFields + "\n" +
	"paraFields: " + paraFields;
	}
}
