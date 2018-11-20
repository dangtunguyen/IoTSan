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
import edu.ksu.cis.bandera.jext.*;

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

import ca.mcgill.sable.soot.ArrayType;

import java.io.*;
import java.util.Vector;
import java.util.Hashtable;

import org.apache.log4j.Category;

/**
 * A dynamic map, defining the collection associated with each allocator site.
 * <p>
 * A dynamic map contains some number of DynamicMapEntry objects, each of
 * which binds an allocator site (NewExpr or NewArrayExpr) to a collection.
 * We don't create collections here, rather we assign each collection a
 * unique key---to store objects from different allocator sites in the
 * same collection, we would provide the same key when adding the
 * entries for those sites (e.g., to store all members of a given
 * class in the same collection, the SootClass object of the allocator
 * could be used as the collection key).   
 * <p>
 * The guts of this class maintains several data structures for accessing
 * the dynamic map entries in various ways (e.g., by collection, by class).
 * <p>
 * Classes are identified with their SootClass object, while arrays
 * are identified with their (Jimple) ArrayType object.
 */

public class DynamicMap {

    private static final Category log = Category.getInstance(DynamicMap.class);

    SootClassManager classManager; 

    /**
     * A vector of classes (SootClass objects) in the map and
     * a hashtable mapping each class to a vector of the
     * map entries for that class.
     */

    Vector classVector = new Vector();
    Hashtable classTable = new Hashtable(); 

    /**
     * A vector of arrays (Jimple ArrayType objects) in the map and
     * a hashtable mapping each array type to a vector of the
     * map entries for that class.
     */

    Vector arrayVector = new Vector();
    Hashtable arrayTable = new Hashtable();

    /**
     * A vector of the collection keys in the map and 
     * a hashtable mapping each collection key to a vector of
     * the map entries for that collection key.
     */

    Hashtable collectTable = new Hashtable();
    Vector collectVector = new Vector();

    DynamicMap(SootClassManager classManager) {
	this.classManager = classManager;
    }
    /**
     * Add a map entry for an array allocator, given the allocator, the
     * collection key, the number of arrays that must be stored, and
     * the maximum length of the arrays.
     */

    void addEntry(NewArrayExpr alloc, Object key, int size, int arrayLen) {
	ArrayType arrayType = (ArrayType) alloc.getType();
	if (arrayType != null) {
	    DynamicMapEntry entry = 
		new DynamicMapEntry(alloc,null,arrayType,key,size,arrayLen);
	    storeByArray(entry,arrayType);
	    storeByCollection(entry, key);
	}
    }
    /**
     * Add a map entry for a class allocator, given the allocator, the
     * collection key, and the number of instances that must be
     * supported.  
     */

    void addEntry(NewExpr alloc, Object key, int size) {
	SootClass sootClass = 
	    classManager.getClass(alloc.getBaseType().className);
	DynamicMapEntry entry = 
	    new DynamicMapEntry(alloc,sootClass,null,key,size,0);
	storeByClass(entry, sootClass);
	storeByCollection(entry, key);
    }
    Vector getAllocsOfArray(Type arrayType) {
	return (Vector) arrayTable.get(arrayType);
    }
    /**
     * These methods return vectors of map entries for a given
     * class/array/collection.
     */

    Vector getAllocsOfClass(SootClass sootClass) {
	return (Vector) classTable.get(sootClass);
    }
    Vector getAllocsOfCollection(Object key) {
	return (Vector) collectTable.get(key);
    }
    Vector getArrays() { return arrayVector; }
    Vector getClasses() { return classVector; }
    /**
     * Get the array type of objects stored in a given collection.
     * <p>
     * Note: all objects stored in a given collection should be
     * of the same array type.
     */

    ArrayType getCollectionArray(Object key) {
	Vector allocs = (Vector) collectTable.get(key);
	DynamicMapEntry entry = (DynamicMapEntry) allocs.elementAt(0);
	return entry.arrayType;
    }
    /**
     * Get the class of objects stored in a given collection.
     * <p>
     * Note: all objects stored in a given collection should be
     * of the same class.
     */

    SootClass getCollectionClass(Object key) {
	Vector allocs = (Vector) collectTable.get(key);
	DynamicMapEntry entry = (DynamicMapEntry) allocs.elementAt(0);
	return entry.sootClass;
    }
    Vector getCollections() { return collectVector; }
    /**
     * Compute the size of a collection by summing the size of
     * the map entries associated with that collection.
     */

    int getCollectionSize(Object key) {
	int size = 0;
	Vector allocs = (Vector) collectTable.get(key);
	for (int i = 0; i < allocs.size(); i++)
	    size += ((DynamicMapEntry)allocs.elementAt(i)).size;
	return size;
    }
    /**
     * Get the interfaces implemented by the classes in the map.
     * @return a vector of SootClass objects representing interfaces.
     */

    Vector getInterfaces() {
	Vector result = new Vector();
	for (int i = 0; i < classVector.size(); i++) {
	    SootClass sootClass = (SootClass) classVector.elementAt(i);
	    Iterator interfaceIt = sootClass.getInterfaces().iterator();
	    while (interfaceIt.hasNext()) {
		SootClass interfaceClass = (SootClass) interfaceIt.next();
		if (! result.contains(interfaceClass))
		    result.addElement(interfaceClass);
	    }
	}
	return result;
    }
    void print() {
	//System.out.println("Dynamic Map:");
	log.debug("Dynamic Map:");
	for (int i = 0; i < classVector.size(); i++) {
	    SootClass sootClass = (SootClass) classVector.elementAt(i);
	    //System.out.println("  class " + sootClass.getName() + ":");
	    log.debug("  class " + sootClass.getName() + ":");
	    Vector classAllocs = (Vector) classTable.get(sootClass);
	    for (int j = 0; j < classAllocs.size(); j++) {
		//System.out.println("    " + classAllocs.elementAt(j));
		log.debug("    " + classAllocs.elementAt(j));
	    }
	}
	for (int i = 0; i < arrayVector.size(); i++) {
	    Type arrayType = (Type) arrayVector.elementAt(i);
	    //System.out.println("  array " + arrayType + ":");
	    log.debug("  array " + arrayType + ":");
	    Vector arrayAllocs = (Vector) arrayTable.get(arrayType);
	    for (int j = 0; j < arrayAllocs.size(); j++) {
		//System.out.println("    " + arrayAllocs.elementAt(j));
		log.debug("    " + arrayAllocs.elementAt(j));
	    }
	}
	for (int i = 0; i < collectVector.size(); i++) {
	    Object key = collectVector.elementAt(i);
	    //System.out.println("  collect " + key + ":");
	    log.debug("  collect " + key + ":");
	    Vector collectAllocs = (Vector) collectTable.get(key);
	    for (int j = 0; j < collectAllocs.size(); j++) {
		//System.out.println("    " + collectAllocs.elementAt(j));
		log.debug("    " + collectAllocs.elementAt(j));
	    }
	}
	Vector interfaces = getInterfaces();
	for (int i = 0; i < interfaces.size(); i++) {
	    SootClass sootClass = (SootClass) interfaces.elementAt(i);
	    //System.out.println("  interface " + sootClass.getName());
	    log.debug("  interface " + sootClass.getName());
	}
    }
    void storeByArray(DynamicMapEntry entry, Type arrayType)  {
	// Indexed by array
	Vector arrayAllocs = (Vector) arrayTable.get(arrayType);
	if (arrayAllocs == null) {
	    arrayAllocs = new Vector();
	    arrayTable.put(arrayType, arrayAllocs);
	    arrayVector.addElement(arrayType);
	}
	arrayAllocs.addElement(entry);
    }
    /**
     * These methods store an entry by class/array/collection in
     * the vectors and hashtables.
     */

    void storeByClass(DynamicMapEntry entry, SootClass sootClass)  {
	// Indexed by class
	Vector classAllocs = (Vector) classTable.get(sootClass);
	if (classAllocs == null) {
	    classAllocs = new Vector();
	    classTable.put(sootClass, classAllocs);
	    classVector.addElement(sootClass);
	}
	classAllocs.addElement(entry);
    }
    void storeByCollection(DynamicMapEntry entry, Object key) {
	// Indexed by collection key
	Vector collectAllocs = (Vector) collectTable.get(key);
	if (collectAllocs == null) {
	    collectAllocs = new Vector();
	    collectTable.put(key, collectAllocs);
	    collectVector.addElement(key);
	}
	collectAllocs.addElement(entry);	
    }
}
