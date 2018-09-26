package edu.ksu.cis.bandera.birc;

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
import java.util.Hashtable;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.jext.*;

import ca.mcgill.sable.soot.ArrayType; 

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

/**
 * A Jimple type switch for extracting BIR types from Jimple types.
 * <p>
 * The TransSystem class is a factory for the BIR type classes.
 * For Jimple primitive types, the extractor simply calls 
 * the appropriate constructor method in the factory.
 * For Jimple reference types, the extractor maintains
 * a mapping from the Jimple type to the BIR record, array,
 * or reference type.
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/06/19 15:34:12 $
 */
public class TypeExtractor extends BanderaTypeSwitch {

    private static Category log = Category.getInstance(TypeExtractor.class);

    TransSystem system;
    SootClassManager classManager;

    /**
     * These tables map Jimple types to BIR types.
     * <p>
     * Note that a single Jimple type (e.g., a SootClass C) may
     * have multiple BIR types (e.g., C_rec and C_ref).
     */
    Hashtable refTypeTable = new Hashtable();
    Hashtable recordTypeTable = new Hashtable();
    Hashtable arrayTypeTable = new Hashtable();

    public TypeExtractor(TransSystem system, SootClassManager classManager) {
	this.system = system;
	this.classManager = classManager;
    }

    /**
     * Add the fields of a class to a BIR record.
     */
    void addFields(edu.ksu.cis.bandera.bir.Record rec, SootClass sootClass) {
		Iterator fieldIt = sootClass.getFields().iterator();        
		while(fieldIt.hasNext()) {
		    SootField field = (SootField) fieldIt.next();
		    // For all non-static fields
		    if (! Modifier.isStatic(field.getModifiers())) {
				// If type unknown, ignore field
				field.getType().apply(this);
				edu.ksu.cis.bandera.bir.Type type = 
				    (edu.ksu.cis.bandera.bir.Type)getResult();
		
				if (type == null) 
				    continue;
				edu.ksu.cis.bandera.bir.Field birField = 
				    rec.addField(field.getName(), type);
		
				system.setSource(birField,field);
		    }
		}	
    }

    /**
     * Construct name for array type by appending "_arr" to the base
     * type name for each dimension (e.g., int [][] would be int_arr_arr).
     */
    public String arrayName(ArrayType sootArrayType) {
		Type type = sootArrayType.baseType;
		while (type instanceof ArrayType) 
		    type = ((ArrayType)type).baseType;
		String name = type.toString();
		for (int i = 0; i < sootArrayType.numDimensions; i++)
		    name += "_arr";
		return name;
    }

    public void caseArrayType(ArrayType type) {
	// By the time this is called, the ref type should have
	// already been created from the dynamic map
	setResult(refTypeTable.get(type));
    }

    public void caseBooleanType(BooleanType type) {
	setResult(system.booleanType());
    }

    public void caseByteType(ByteType type) {
	setResult(system.rangeType());
    }

    public void caseIntType(IntType type) {
	setResult(system.rangeType());
    }

    public void caseCharType(CharType type) {
	setResult(system.rangeType());
    }

    public void caseLongType(LongType type) {
	setResult(system.rangeType());
    }

    public void caseRefType(RefType type) {
	SootClass sootClass = classManager.getClass(type.className);
	// By the time this is called, the ref type should have
	// already been created from the dynamic map
	setResult(refTypeTable.get(sootClass));
    }

    public void caseShortType(ShortType type) {
	setResult(system.rangeType());
    }

    /**
     * Declare BIR array type for a Soot array type.
     */
    void createArrayType(ArrayType sootArrayType, int arrayLen) {	    
		// Get BIR type for base type
		sootArrayType.baseType.apply(this);
		edu.ksu.cis.bandera.bir.Type baseType = 
		    (edu.ksu.cis.bandera.bir.Type) getResult();
	
		/*
		 * The bug exists here such that no baseType is returned when given java.lang.String as the sootArrayType.baseType.  This
		 * means a null value is passed into the creation of the array.  This shouldn't be allowed so we will throw the
		 * exception here. -tcw
		 */
		if(baseType == null) {
		    RuntimeException e = new RuntimeException("The baseType that was generated was null when given a sootArrayType (" + sootArrayType +
						") and sootArrayType.baseType (" + sootArrayType.baseType + ") and arrayLen (" + arrayLen + ").");
		    log.error("An error occured while getting the base type of an array.", e);
		    throw(e);
		}
	
		edu.ksu.cis.bandera.bir.ConstExpr lenExpr = 
		    new edu.ksu.cis.bandera.bir.IntLit(arrayLen);
		edu.ksu.cis.bandera.bir.Array type = 
		    system.arrayType(baseType,lenExpr);
		String name = arrayName(sootArrayType);
		type.setName(system.createName(null,name));
		system.define(type.getName(), type);
		arrayTypeTable.put(sootArrayType, type);
    }

    /**
     * Create a collection type for a given BIR type and collection size.
     */
    edu.ksu.cis.bandera.bir.Collection createCollectionType(edu.ksu.cis.bandera.bir.Type type, int size) {
		edu.ksu.cis.bandera.bir.ConstExpr sizeExpr = 
		    new edu.ksu.cis.bandera.bir.IntLit(size);
		return system.collectionType(type,sizeExpr);
    }

    /**
     * Declare a record type for a class C (name it C_rec).
     * <p>
     * Note: we flatten the implementation inheritance here
     * by adding all fields from superclasses of C into C_rec.
     * <p>
     * We add a "tid" field for "Thread" objects to hold a thread id 
     * (for implementing operations like join) and 
     * a "BIRLock" field for classes that have monitor statements.
     */

    void createRecordType(SootClass sootClass, 
			  boolean isLocked, boolean isThread) {
		log.debug("createRecordType: " + sootClass);
		edu.ksu.cis.bandera.bir.Record type = system.recordType();
		system.addType(type);
		type.setName(system.createName(null,sootClass.getName() + "_rec"));
		system.define(type.getName(), type);
		recordTypeTable.put(sootClass, type);
		if (isThread) {
		    log.debug("createRecordType: " + sootClass + " is thread");
	            type.addField("tid",system.tidType());
		}
		if (isLocked) {
		    log.debug("createRecordType: " + sootClass + " is locked");
		    type.addField("BIRLock",system.lockType(true,true));
	 	}
		Vector superClasses = getSuperClasses(sootClass);
		for (int i = superClasses.size() - 1; i >= 0; i--) {
		    SootClass supClass = (SootClass) superClasses.elementAt(i);
		    // Don't include fields of predefined classes
		    if (supClass.getName().startsWith("java.") &&
		        !(supClass.getName().startsWith("java.lang.Thread") ||
		          supClass.getName().startsWith("java.lang.Object")) )
		    	continue;  
		    addFields(type,supClass);
		}
		addFields(type,sootClass);
		system.setSource(type,sootClass);
		log.debug("createRecordType: made BIR type " + type + " for Soot class " + sootClass);
    }
    /**
     * Declare a reference type for an array type A (name it A_ref).
     */

    void createRefType(ArrayType sootArrayType) {
		edu.ksu.cis.bandera.bir.Ref type = system.refType();
		String name = arrayName(sootArrayType);
		type.setName(system.createName(null,name + "_ref"));
		system.define(type.getName(), type);
		refTypeTable.put(sootArrayType, type);	
    }
    /**
     * Declare reference type for a class C (name it C_ref).
     */

    void createRefType(SootClass sootClass) {
		if (refTypeTable.get(sootClass) == null) {
		    edu.ksu.cis.bandera.bir.Ref type = system.refType();
		    type.setName(system.createName(null,sootClass.getName() + "_ref"));
		    system.define(type.getName(), type);
		    refTypeTable.put(sootClass, type);
		    // Must create ref types for all super classes and interfaces
		    if (sootClass.hasSuperClass())
		    		createRefType(sootClass.getSuperClass());
		    Iterator intIt = sootClass.getInterfaces().iterator();
		    while (intIt.hasNext()) {
				SootClass intClass = (SootClass) intIt.next();
				createRefType(intClass);
		    }
		}
    }
    public void defaultCase(Type type) {
	setResult(null);
    }
    /**
     * Get BIR array type for Soot array type.
     */

    edu.ksu.cis.bandera.bir.Array getArrayType(ArrayType sootArrayType) {
	return (edu.ksu.cis.bandera.bir.Array) 
	    arrayTypeTable.get(sootArrayType);
    }
    /**
     * Return the set of interfaces implemented by a class.
     * <p>
     * Note: need to look at all super-classes and super-interfaces.
     */

    Vector getInterfaceClasses(SootClass sootClass) {
	Vector result = new Vector();
	Vector supClasses = getSuperClasses(sootClass);
	supClasses.addElement(sootClass);
	for (int i = 0; i < supClasses.size(); i++) {
	    SootClass supClass = (SootClass) supClasses.elementAt(i);
	    Iterator interfaceIt = supClass.getInterfaces().iterator();
	    while (interfaceIt.hasNext()) {
		SootClass interfaceClass = (SootClass) interfaceIt.next();
		if (! result.contains(interfaceClass))
		    result.addElement(interfaceClass);

		// Get ancestors of this interface
	        Iterator iaIt = interfaceClass.getInterfaces().iterator();
	        while (iaIt.hasNext()) {
		    SootClass iaClass = (SootClass) iaIt.next();
		    if (! result.contains(iaClass))
			result.addElement(iaClass);
		}
	    }
	}
	return result;
    }
    /**
     * Get BIR record type of class.
     */
    edu.ksu.cis.bandera.bir.Record getRecordType(SootClass sootClass) {
	return (edu.ksu.cis.bandera.bir.Record) recordTypeTable.get(sootClass);
    }
    edu.ksu.cis.bandera.bir.Record getRecordType(RefType type) {
	SootClass sootClass = classManager.getClass(type.className);
	return (edu.ksu.cis.bandera.bir.Record) recordTypeTable.get(sootClass);
    }
    edu.ksu.cis.bandera.bir.Ref getRefType(ArrayType sootArrayType) {
	return (edu.ksu.cis.bandera.bir.Ref) refTypeTable.get(sootArrayType);
    }
    /**
     * Get the reference type for a class or array.
     */

    edu.ksu.cis.bandera.bir.Ref getRefType(SootClass sootClass) {
	return (edu.ksu.cis.bandera.bir.Ref) refTypeTable.get(sootClass);
    }
    /**
     * Return the set of super classes of a class.
     */

    Vector getSuperClasses(SootClass sootClass) {
	Vector result = new Vector();
	SootClass supClass = sootClass; 
	while (supClass.hasSuperClass()) {
	    supClass = supClass.getSuperClass();	    
	    result.addElement(supClass);
	}
	return result;
    }
}
