package edu.ksu.cis.bandera.smv;

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
import java.io.*;
import java.util.Vector;

import edu.ksu.cis.bandera.bir.*;

/**
 * BIR type switch for declaring SMV state variables.
 * <p>
 * An instance of this class is created by SmvTrans to help it declare
 * all the components of a BIR variable (composite types have multiple
 * components).   Variables are declared by callbacks to the SmvTrans
 * object on declareVar().
 * <p>
 * The SmvTypeDecl is applied to each variable by its type, with the
 * variable itself being passed to the caseXXX() method as the second
 * parameter (the Object context).  
 * <p>
 * If the type is composite, then we recursively apply the type switch
 * to the components of the composite type, but the context parameter
 * is then a string denoting the prefix of the SMV variable name
 * for the components of that variable.
 * <p>
 * Note that the varName() method must be called at the top of
 * each caseXXX() method to record the variable source and
 * other parameters for the subsequent recursive calls (which
 * will not get the StateVar as a param).
 */

public class SmvTypeDecl extends AbstractTypeSwitch implements BirConstants {

	SmvTrans smvTrans;         // Translator class we're working for
	TransSystem system;

	StateVar source;         // BIR var we're declaring the components of
	SmvVar container;        // SmvVar that contains the vars being declared
	boolean constrained;     // constrained = has its own TRANS formula
	SmvVar deadFlag;         // SmvVar that indicates whether this var is live

	String refIndexType;    // SMV type for refIndex
	String instNumType;     // SMV type for instance numbers
	int maxSize;            // maximum size of any collection

	public SmvTypeDecl(SmvTrans smvTrans, TransSystem sys) {
	this.smvTrans = smvTrans;
	this.system = sys;

	// Calculate the maximum collection size and use it to build
	// the refIndex and instNum types (for ref vars)
	StateVarVector targets = sys.refAnyType().getTargets();
	maxSize = 0;
	for (int i = 0; i < targets.size(); i++)
	    if (targets.elementAt(i).getType().isKind(COLLECTION)) {
		int size = ((Collection)targets.elementAt(i).getType())
		    .getSize().getValue();
		if (size > maxSize)
		    maxSize = size;
	    }
	refIndexType = "0 .. " + targets.size();
	instNumType = "0 .. " + (maxSize - 1);
	}
	/**
	 * Declare array variable.
	 * <p>
	 * An array variable is an aggregate with the following components:
	 * <ul>
	 * <li> A length variable recording the length of the array
	 * <li> One or more variables for each possible array slot
	 *  (created via recursive calls).
	 * </ul>
	 */

	public void caseArray(Array type, Object o)
	{
	String name = varName(o);
	SmvVar array = smvTrans.declareVar(name,null,source,null,
					   container,false,deadFlag);
	int size = type.getSize().getValue();
	String lengthType = "0 .. " + size;
	smvTrans.declareVar(smvTrans.lengthVar(name), 
			    lengthType,source,"0",array,constrained,deadFlag);
	for (int i = 0; i < size; i++) {
	    // these components are contained in the array variable
	    this.container = array;  
	    type.getBaseType().apply(this,smvTrans.elementVar(name, i));
	}
	}
	/**
	 * Declare boolean variable.
	 */

	public void caseBool(Bool type, Object o) 
	{
	String name = varName(o);
	smvTrans.declareVar(name,"boolean",source,
			    initValue(type,o),container,constrained,deadFlag);
	}
	/**
	 * Declare collection variable.
	 * <p>
	 * A collection variable is an aggregate with the following components:
	 * <ul>
	 * <li> An inuse variable for each possible collection instance,
	 *  which is true if that instance has been allocated.
	 * <li> One or more variables for each possible collection instance
	 *  (created via recursive calls).
	 * </ul>
	 */

	public void caseCollection(Collection type, Object o)
	{
	String name = varName(o);
	SmvVar col = smvTrans.declareVar(name,null,source,null,
					 container,false,null);
	int size = type.getSize().getValue();
	for (int i = 0; i < size; i++) {
	    SmvVar inuse = smvTrans.declareVar(smvTrans.inUseVar(name,i),
					       "boolean",source, "0",
					       col,constrained,null);
	    // Instances are contained in collection
	    this.container = col;
	    // Collection instance is dead if inuse false
	    this.deadFlag = inuse;
	    type.getBaseType().apply(this,smvTrans.instanceVar(name,i));
	    this.deadFlag = null;
	}
	}
	/**
	 * Declare enumerated variable.
	 * <p>
	 * Since we used named integer constants for each enumerated value,
	 * this is really just a range type.
	 */

	public void caseEnumerated(Enumerated type, Object o) 
	{
	String name = varName(o);
	Vector constants = type.getConstants();
	String smvType = type.getFirstElement() + " .. " +
	    (type.getFirstElement() + type.getEnumeratedSize() - 1);
	smvTrans.declareVar(name,smvType,source,
			    initValue(type,o),container,constrained,deadFlag);
	}
	/**
	 * Declare field variable.
	 */

	public void caseField(Field type, Object o)
	{
	String name = varName(o);
	type.getType().apply(this, smvTrans.fieldVar(name,type));
	}
	/**
	 * Declare lock variable.
	 * <p>
	 * A lock variable is an aggregate with the following components:
	 * <ul>
	 * <li> An owner variable indicating which thread holds the lock
	 * <li> A count variable, recording the number of acquisitions
	 * of the lock (for relocking)
	 * <li> A wait variable for each thread indicating whether that
	 *  thread is blocked in the wait queue of this lock
	 * </ul>
	 */

	public void caseLock(Lock type, Object o)
	{
	String name = varName(o);
	SmvVar lock = smvTrans.declareVar(name,null,source,
					  null,container,false,deadFlag);
	smvTrans.declareVar(smvTrans.ownerVar(name),smvTrans.getThreadType(),
			    source,"NoThread",lock,constrained,deadFlag);
	smvTrans.declareVar(smvTrans.countVar(name),SmvTrans.LOCK_COUNT_TYPE,
			    source,"0",lock,constrained,deadFlag);
	ThreadVector threads = system.getThreads();
	for (int i = 0; i < threads.size(); i++)
	    smvTrans.declareVar(smvTrans.waitVar(name,threads.elementAt(i)),
				"boolean",source,"0",
				lock,constrained,deadFlag);
	}
	/**
	 * Declare range variable.
	 */

	public void caseRange(Range type, Object o) 
	{
	String name = varName(o);
	String smvType = type.getFromVal().getValue() + " .. " +
	    type.getToVal().getValue();
	smvTrans.declareVar(name,smvType,source,
			    initValue(type,o),container,constrained,deadFlag);
	}
	/**
	 * Declare record variable.
	 * <p>
	 * An record variable is an aggregate with 
	 * one or more variables for each field
	 * (created via recursive calls).
	 */

	public void caseRecord(Record type, Object o)
	{
	String name = varName(o);
	Vector fields = type.getFields();
	SmvVar record = smvTrans.declareVar(name,null,source,
					    null,container,false,deadFlag);
	for (int i = 0; i < fields.size(); i++) {
	    // Fields are contained in record var
	    this.container = record;
	    ((Field)fields.elementAt(i)).apply(this, o);
	}
	}
	/**
	 * Declare reference variable.
	 * <p>
	 * A reference is an aggregate with the following components:
	 * <ul>
	 * <li> a refIndex variable, specifying the target pointed to 
	 * (or 0 for null)
	 * <li> an instance variable, specifying the instance number
	 * of the collection.
	 * </ul>
	 * Note that reference variables are unusual in that they may
	 * appear as final values in expressions even though they
	 * are implemented as two variables.  We solve this problem
	 * by defining a "printForm" for an SMV variable that is
	 * used to display the variable in a formula.  The printForm
	 * of a reference R is (R_refIndex * MAX_COLLECT_SIZE + R_instNum),
	 * thus the two components (R_refIndex and R_instNum) are combined
	 * into a single integer for the purposes of assignment and
	 * equality tests.  Dereferencing (the most common operation) 
	 * uses the components seperately.
	 */

	public void caseRef(Ref type, Object o)
	{
	String name = varName(o);
	SmvVar ref = smvTrans.declareVar(name, null,source, null, 
					 container,constrained,deadFlag);
	this.container = ref;
	SmvVar refIndex = 
	    smvTrans.declareVar(smvTrans.refIndexVar(name),refIndexType,source,
				"0",container,false,deadFlag);
	SmvVar instNum = 
	    smvTrans.declareVar(smvTrans.instNumVar(name),instNumType,source,
				"0",container,false,deadFlag);
	ref.setPrintForm("(" + refIndex.getName() + " * MAX_COLLECT_SIZE + " +
			 instNum.getName() + ")");
	}
	public void defaultCase(Object obj) {
	throw new RuntimeException("Construct not handled: " + obj);
	}
	public int getMaxCollectionSize() { return maxSize; }
	/** 
	 * Get initial value of variable.
	 * <p>
	 * If the context is a String, the variable is a component,
	 * so just use the default value of the type.
	 * Otherwise (context is StateVar), get the initial value 
	 * of that variable and translate it.
	 */

	String initValue(Type type, Object context) {
	Expr initVal;
	if (context instanceof String)
	    initVal = type.defaultVal();
	else
	    initVal = ((StateVar)context).getInitVal();
	initVal.apply(smvTrans);
	return smvTrans.getResult().toString();
	}
	/**
	 * Return the name of the variable indicated by the context.
	 * <p>
	 * The context is either a StateVar (for a top-level call) or
	 * a String denoting a prefix of a name (for a nested call to
	 * declare the components of a composite type).
	 * <p>
	 * This must be called at the top of each caseXXX() method to set the
	 * source, container, and constrained fields.
	 */
	String varName(Object context) {
	if (context instanceof String)
	    return (String) context;
	else {
	    source = (StateVar)context;
	    // top-level vars are not contained in any other var
	    container = null;       
	    // constrained if global (locals are set by thread TRANS formula)
	    constrained = (source.getThread() == null);  
	    return source.getName();
	}
	}
}
