package edu.ksu.cis.bandera.sessions;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

import java.io.StringWriter;
import java.io.PrintWriter;

import edu.ksu.cis.bandera.util.DefaultValues;

import org.apache.log4j.Category;

/**
 * ResourceBounds provides information about the ranges of values
 * that program data can take on.
 * <p>
 * <code>int</code> values are bounded by <code>intMin</code> 
 * and <code>intMax</code>
 * <p>
 * <code>array</code>s can be allocated of sizes up to <code>arrayMax</code>
 * <p>
 * instances of thread (or runnable) types can be allocated and 
 * <code>start</code>ed up to <code>threadMax</code>
 * <p>
 * The number of instances of a type, <code>C</code>, 
 * that can be allocated at a 
 * given <code>new C</code> expression can be bounded.  A default bound for
 * all classes is defined by <code>allocMax</code>.  
 *
 * Class specific resource bounds can be set by associating an integer 
 * bound with the <code>String</code> name for the class.  Bound/class
 * pairs are stored in the <code>classBoundTable</code>.
 *
 * Thread specific resource bounds can be set by associating an integer 
 * bound with the <code>String</code> name for the thread.  Bound/class
 * pairs are stored in the <code>threadBoundTable</code>.
 *
 * [***********Note: Dr. Dwyer, Please review these javadoc comments to make
 * sure they are accurate as to what they actually do.***************]
 *
 * @author Matt Dwyer &lt;dwyer@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class ResourceBounds extends Observable implements Cloneable {

    /**
     * The log messages will be written to.
     */
    private static final Category log = Category.getInstance(ResourceBounds.class.getName());

    /**
     * The default minimum for integer values.
     */
    private int intMin;

    /**
     * The default maximum for integer values.
     */
    private int intMax;

    /**
     * A Map for individual variable bounds.
     */
    private Map variableBoundMap;

    /**
     * The default maximum for the number of instances of each class
     * for each allocator.
     */
    private int allocMax;

    /**
     * The default maximum for the size of arrays.
     */
    private int arrayMax;

    /**
     * The default maximum for the number of instances of each thread class
     * for each allocator.
     */
    private int threadMax;

    /**
     * A table of allocation bounds for each class in the system.  This
     * is related to the allocMax field.
     */
    private Hashtable classBoundTable; 

    /**
     * A table of allocation bounds for each thread class in the system.  This is
     * related to the threadMax field.
     */
    private Hashtable threadBoundTable;

    /**
     * Construct the default ResourceBounds.
     */
    public ResourceBounds() {
	this(DefaultValues.birMinIntRange, DefaultValues.birMaxIntRange, DefaultValues.birMaxInstances,
	     DefaultValues.birMaxArrayLen, DefaultValues.birMaxThreads);
    }	

    /**
     * Construct a ResourceBounds based upon the given values.
     *
     * @param int intMin The minimum integer value.
     * @param int intMax The maximum integer value.
     * @param int allocMax The maximum number of instances per class.
     * @param int arrayMax The maximum size of arrays.
     * @param int threadMax The maximum number of threads.
     */
    public ResourceBounds(int intMin, int intMax, int allocMax, int arrayMax, int threadMax) {
	this.intMin = intMin;
	this.intMax = intMax;
	this.arrayMax = arrayMax;
	this.allocMax = allocMax;
	this.threadMax = threadMax;
	classBoundTable = new Hashtable();
	threadBoundTable = new Hashtable();
	variableBoundMap = new HashMap();
    }	

    /**
     * Clone the current resource bound to create a new one exactly like this one.  This
     * clone will produce a &quot;deep-copy&quot; so it will actually duplicate the Hashtable and Map
     * values in the new ResourceBounds.
     *
     * @return Object A ResourceBounds object that holds the same bound values as this object.
     */
    public Object clone() {
	ResourceBounds rb = new ResourceBounds(intMin, intMax, allocMax, arrayMax, threadMax);

	Iterator i = classBoundTable.keySet().iterator();
	while(i.hasNext()) {
	    String className = (String)i.next();
	    int instances = getAllocMax(className);
	    rb.setAllocMax(className, instances);
	}

	i = threadBoundTable.keySet().iterator();
	while(i.hasNext()) {
	    String threadClassName = (String)i.next();
	    int instances = getThreadMax(threadClassName);
	    rb.setThreadMax(threadClassName, instances);
	}

	i = variableBoundMap.keySet().iterator();
	while(i.hasNext()) {
	    String className = (String)i.next();
	    Map classMap = (Map)variableBoundMap.get(className);
	    if((classMap != null) && (classMap.size() > 0)) {
		Iterator cmi = classMap.keySet().iterator();
		while(cmi.hasNext()) {
		    String memberName = (String)cmi.next();
		    Object memberValue = classMap.get(memberName);
		    if(memberValue == null) {
			continue;
		    }
		    else if(memberValue instanceof List) {
			List l = (List)memberValue;
			rb.setFieldMax(className, memberName, ((Integer)l.get(1)).intValue());
			rb.setFieldMin(className, memberName, ((Integer)l.get(0)).intValue());
		    }
		    else if(memberValue instanceof Map) {
			Map methodMap = (Map)memberValue;
			Iterator mmi = methodMap.keySet().iterator();
			while(mmi.hasNext()) {
			    String localName = (String)mmi.next();
			    List l = (List)methodMap.get(localName);
			    rb.setMethodLocalMax(className, memberName, localName, ((Integer)l.get(1)).intValue());
			    rb.setMethodLocalMin(className, memberName, localName, ((Integer)l.get(0)).intValue());
			}
		    }
		    else {
			// not sure what to do here! -tcw
		    }
		}
	    }
	}
	
	return(rb);
    }

    public Map getVariableBoundMap() {
	return(variableBoundMap);
    }

    /**
     * Get the maximum value for the local given that is inside the method and class given.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String methodName The name of the method in which the local is declared.
     * @param String localName The name of the local to get the bounds for.
     * @return int The maximum value for the given local.  If not in the table, this returns the default value (intMax).
     */
    public int getMethodLocalMax(String className, String methodName, String localName) {
	int max = intMax;
	if(variableBoundMap != null) {
	    Map classMap = (Map)variableBoundMap.get(className);
	    if(classMap != null) {
		Map methodMap = (Map)classMap.get(methodName);
		if(methodMap != null) {
		    List boundsList = (List)methodMap.get(localName);
		    if(boundsList != null) {
			Integer wrapper = (Integer)boundsList.get(1);
			if(wrapper != null) {
			    max = wrapper.intValue();
			}
		    }
		}
	    }
	}
	return(max);
    }

    /**
     * Get the minimum value for the local given that is inside the method and class given.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String methodName The name of the method in which the local is declared.
     * @param String localName The name of the local to get the bounds for.
     * @return int The minimum value for the given local.  If not in the table, this returns the default value (intMin).
     */
    public int getMethodLocalMin(String className, String methodName, String localName) {
	int min = intMin;
	if(variableBoundMap != null) {
	    Map classMap = (Map)variableBoundMap.get(className);
	    if(classMap != null) {
		Map methodMap = (Map)classMap.get(methodName);
		if(methodMap != null) {
		    List boundsList = (List)methodMap.get(localName);
		    if(boundsList != null) {
			Integer wrapper = (Integer)boundsList.get(0);
			if(wrapper != null) {
			    min = wrapper.intValue();
			}
		    }
		}
	    }
	}
	return(min);
	
    }

    /**
     * Get the maximum value for the field given that is inside the class given.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String fieldName The name of the field to get the bounds for.
     * @return int The maximum value for the given field.  If not in the table, this returns the default value (intMax).
     */
    public int getFieldMax(String className, String fieldName) {
	int max = intMax;
	if(variableBoundMap != null) {
	    Map classMap = (Map)variableBoundMap.get(className);
	    if(classMap != null) {
		List boundsList = (List)classMap.get(fieldName);
		if(boundsList != null) {
		    Integer wrapper = (Integer)boundsList.get(1);
		    if(wrapper != null) {
			max = wrapper.intValue();
		    }
		}
	    }
	}
	return(max);
    }
    
    /**
     * Get the minimum value for the field given that is inside the class given.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String fieldName The name of the field to get the bounds for.
     * @return int The minimum value for the given field.  If not in the table, this returns the default value (intMin).
     */
    public int getFieldMin(String className, String fieldName) {
	int min = intMin;
	if(variableBoundMap != null) {
	    Map classMap = (Map)variableBoundMap.get(className);
	    if(classMap != null) {
		List boundsList = (List)classMap.get(fieldName);
		if(boundsList != null) {
		    Integer wrapper = (Integer)boundsList.get(0);
		    if(wrapper != null) {
			min = wrapper.intValue();
		    }
		}
	    }
	}
	return(min);
    }

    /**
     * Set the default maximum integer value.
     *
     * @param int intMax The maximum integer value to use for integer variables.
     */
    public void setIntMax(int intMax) {
	this.intMax = intMax;
    }

    /**
     * Get the maximum integer value.
     *
     * @return int The maximum integer value to use for integer variables.
     */
    public int getIntMax() {
	return(intMax);
    }

    /**
     * Set the minimum integer value.
     *
     * @param int intMin The minimum integer value.
     */
    public void setIntMin(int intMin) {
	this.intMin = intMin;
	setChanged();
	notifyObservers();
    }

    /**
     * Get the minimum integer value.
     *
     * @return int The minimum integer value.
     */
    public int getIntMin() {
	return(intMin);
    }

    /**
     * Set the maximum size of arrays.
     *
     * @param int arrayMax The maximum size of any array.
     */
    public void setArrayMax(int arrayMax) {
	this.arrayMax = arrayMax;
	setChanged();
	notifyObservers();
    }

    /**
     * Get the maximum size of arrays.
     *
     * @return int The maximum size of any array.
     */
    public int getArrayMax() {
	return(arrayMax);
    }

    /**
     * Set the maximum number of instances of a class if not
     * specified explicitly.
     *
     * @param int allocMax The default maximum number of instances of a class.
     */
    public void setDefaultAllocMax(int allocMax) {
	this.allocMax = allocMax;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the maximum number of instances of the class given.
     *
     * @param String c The name of a class to place an upper bound on.
     * @param int i The maximum number of instances of this class.
     */
    public void setAllocMax(String c, int i) { 
	classBoundTable.put(c, new Integer(i));
	setChanged();
	notifyObservers();
    }

    /**
     * Get the maximum number of instances of a class if not
     * specified explicitly.
     *
     * @return int The default maximum number of instances of a class.
     */
    public int getDefaultAllocMax() {
	return(allocMax);
    }

    /**
     * Get the maximum number of instances for the class given.
     *
     * @param String c The name of the class for which we are getting the upper bound for.
     * @return int The upper bound for the given class.
     */
    public int getAllocMax(String c) { 
	Integer wrappedBound = (Integer)classBoundTable.get(c);
	if (wrappedBound == null) {
	    return(allocMax);
	}
	else {
	    return(wrappedBound.intValue());
	}
    }

    /**
     * Get the table of allocation upper bounds.  The keys to this table
     * are the class name (String) and the value for each key is the upper
     * bound (Integer).
     *
     * @return Hashtable A table of upper bounds.
     */
    public Hashtable getAllocMaxTable() {
	return(classBoundTable);
    }

    /**
     * Set the default maximum number of instances of threads.
     *
     * @param int threadMax The maximum number of threads to create.
     */
    public void setDefaultThreadMax(int threadMax) {
	this.threadMax = threadMax;
	setChanged();
	notifyObservers();
    }

    /**
     * Get the default maximum number of instances of threads.
     *
     * @return int The maximum number of threads to create.
     */
    public int getDefaultThreadMax() {
	return(threadMax);
    }

    /**
     * Set the maximum number of thread instances for a given thread class.
     *
     * @param String t The name of the thread to bound.
     * @param int i The upper bound for instances of the named thread.
     */
    public void setThreadMax(String t, int i) {
	threadBoundTable.put(t, new Integer(i));
	setChanged();
	notifyObservers();
    }

    /**
     * Get the maximum number of thread instances for the thread class given.
     *
     * @param String t The name of the thread to get the bound for.
     * @return int The bound for the named thread.
     */
    public int getThreadMax(String t) {
	Integer wrappedBound = (Integer)threadBoundTable.get(t);
	if(wrappedBound == null) {
	    return(threadMax);
	}
	else {
	    return(wrappedBound.intValue());
	}
    }

    /**
     * Get the table of thread instance bounds.  The keys are names of
     * threads (String) and the values are upper bounds (Integer).
     *
     * @return Hashtable The table of bounds.
     */
    public Hashtable getThreadMaxTable() {
	return(threadBoundTable);
    }


    /**
     * Print the values of these resource bounds as a string.
     *
     * @return String The value of these resource bounds as a String.
     */
    public String toString() {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	pw.println("ResourceBounds:");
	pw.println(intMin + " <= default integer range bounds <= " + intMax);
	if((variableBoundMap != null) && (variableBoundMap.size() > 0)) {
	    pw.println("variable bound map:");
	    Iterator vbmi = variableBoundMap.keySet().iterator();
	    while(vbmi.hasNext()) {
		String className = (String)vbmi.next();
		Map classMap = (Map)variableBoundMap.get(className);
		if(classMap != null) {
		    Iterator cmi = classMap.keySet().iterator();
		    while(cmi.hasNext()) {
			String memberName = (String)cmi.next();
			Object memberValue = classMap.get(memberName);
			if(memberValue == null) {
			    continue;
			}
			else if(memberValue instanceof List) {
			    List l = (List)memberValue;
			    if(l != null) {
				pw.println(l.get(0).toString() + " <= " + className + "." + memberName + " <= " + l.get(1).toString());
			    }
			}
			else if(memberValue instanceof Map) {
			    Map methodMap = (Map)memberValue;
			    Iterator mmi = methodMap.keySet().iterator();
			    while(mmi.hasNext()) {
				String localName = (String)mmi.next();
				List l = (List)methodMap.get(localName);
				if(l != null) {
				    pw.println(l.get(0).toString() + " <= " + className + "." + memberName + "." + localName + " <= " + l.get(1).toString());
				}
			    }
			}
			else {
			    // not sure what this is! -tcw
			}
		    }
		}
	    }
	}
	pw.println("array length bound <= " + arrayMax);
	pw.println("default instance bound <= " + allocMax);
	if((classBoundTable != null) && (classBoundTable.size() > 0)) {
	    pw.println("instance table:");
	    Iterator classIterator = classBoundTable.keySet().iterator();
	    while(classIterator.hasNext()) {
		String className = (String)classIterator.next();
		int max = getAllocMax(className);
		pw.println("maximum " + className + " instances <= " + max);
	    }
	}
	pw.println("default threads instance bound <= " + threadMax);
	if((threadBoundTable != null) && (threadBoundTable.size() > 0)) {
	    pw.println("thread instance table:");
	    Iterator threadIterator = threadBoundTable.keySet().iterator();
	    while(threadIterator.hasNext()) {
		String className = (String)threadIterator.next();
		int max = getThreadMax(className);
		pw.println("maximum " + className + " thread instances <= " + max);
	    }
	}
	return(sw.toString());
    }

    /**
     * Test the equality of the given object to this resource bounds.  If
     * the given object is of type ResourceBounds and the intMin, intMax, arrayMax,
     * allocMax, threadMax are equal, and the maps/tables are equal, this will return true.
     * Otherwise, the two are not equal.
     *
     * @param Object object The object to test for equality.
     * @return boolean True if the given object's values are equal to this ResourceBound.  False otherwise.
     */
    public boolean equals(Object object) {
   
	boolean equal = false;
	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof ResourceBounds) {
	    ResourceBounds rb = (ResourceBounds)object;
	    if((rb.intMax == intMax) &&
	       (rb.intMin == intMin) &&
	       (rb.arrayMax == arrayMax) &&
	       (rb.allocMax == allocMax) &&
	       (rb.threadMax == threadMax) &&
	       (((rb.variableBoundMap == null) && (variableBoundMap == null)) ||
		(rb.variableBoundMap.equals(variableBoundMap))) &&
	       (((rb.classBoundTable == null) && (classBoundTable == null)) ||
		(rb.classBoundTable.equals(classBoundTable))) &&
	       (((rb.threadBoundTable == null) && (threadBoundTable == null)) ||
		(rb.threadBoundTable.equals(threadBoundTable)))) {
		equal = true;
	    }
	}
	return(equal);
    }

    /**
     * Create the hash for this object.
     *
     * @return int A hash for this object.
     */
    public int hashCode() {
	int hash = 0;
	hash += intMax;
	hash += intMin;
	hash += allocMax;
	hash += arrayMax;
	hash += threadMax;
	if((classBoundTable != null) && (classBoundTable.size() > 0)) {
	    Iterator cbti = classBoundTable.keySet().iterator();
	    while(cbti.hasNext()) {
		hash += cbti.next().hashCode();
	    }
	}
	if((threadBoundTable != null) && (threadBoundTable.size() > 0)) {
	    Iterator tbti = threadBoundTable.keySet().iterator();
	    while(tbti.hasNext()) {
		hash += tbti.next().hashCode();
	    }
	}
	if((variableBoundMap != null) && (variableBoundMap.size() > 0)) {
	    Iterator vbmi = variableBoundMap.keySet().iterator();
	    while(vbmi.hasNext()) {
		hash += vbmi.next().hashCode();
	    }
	}
	return(hash);
    }

    /**
     * Remove the bound associated with the class name given.
     *
     * @param String a The class name whose bound should be removed.
     * @return int The current bound set for the class that was removed.
     */
    public int removeAllocMax(String a) {
	Integer wrappedBound = (Integer)classBoundTable.remove(a);
	if (wrappedBound == null) {
	    return(allocMax);
	}
	else {
	    return(wrappedBound.intValue());
	}
    }

    /**
     * Remove the maximum associated with the given thread name.
     *
     * @param String threadName The name of the thread to remove a max for.
     */
    public void removeThreadMax(String threadName) {
	threadBoundTable.remove(threadName);
    }

    /**
     * Set the resource bounds to the default values and get rid
     * of all current information.
     */
    public void useDefaultValues() {
	classBoundTable = new Hashtable();
	threadBoundTable = new Hashtable();
	variableBoundMap = new HashMap();
	intMin = DefaultValues.birMinIntRange;
	intMax = DefaultValues.birMaxIntRange;
	arrayMax = DefaultValues.birMaxArrayLen;
	allocMax = DefaultValues.birMaxInstances;
	threadMax = DefaultValues.birMaxThreads;
    }

    /**
     * Get the maximum integer value.
     */
    public int getDefaultIntMax() {
	return(intMax);
    }

    /**
     * Get the minimum integer value.
     */
    public int getDefaultIntMin() {
	return(intMin);
    }

    /**
     * Get a Map of the variable bound map such that the keys are Strings that
     * represent the full name of the variables and the value for each is a List
     * where the first value is the min and the second value is the max.  An example
     * of a full name for a variable would be for a local variable named x inside a
     * method named bar which is defined in a class name Foo: Foo.bar.x.
     * 
     * @return java.util.Map
     */
    public Map getFlatVariableBoundMap() {
	Map flatMap = new HashMap();
	if(variableBoundMap != null) {
	    Iterator vbmi = variableBoundMap.keySet().iterator();
	    while(vbmi.hasNext()) {
		String className = (String)vbmi.next();
		Map classMap = (Map)variableBoundMap.get(className);
		if(classMap != null) {
		    Iterator cmi = classMap.keySet().iterator();
		    while(cmi.hasNext()) {
			String memberName = (String)cmi.next();
			String fullName = "";
			Object memberValue = classMap.get(memberName);
			if(memberValue != null) {
			    if(memberValue instanceof List) {
				// this is a field
				List l = (List)memberValue;
				fullName = className + "." + memberName;
				flatMap.put(fullName, l);
			    }
			    else if(memberValue instanceof Map) {
				// this is a method
				Map methodMap = (Map)memberValue;
				Iterator mmi = methodMap.keySet().iterator();
				while(mmi.hasNext()) {
				    String localName = (String)mmi.next();
				    List l = (List)methodMap.get(localName);
				    fullName = className + "." + memberName + "." + localName;
				    flatMap.put(fullName, l);
				}
			    }
			    else {
				// not sure what this is? -tcw
			    }
			}
		    }
		}
	    }
	}
	return(flatMap);
    }

    /**
     * Remove the minimum integer value and the maximum integer value for the field.
     *
     * @param String className The name of the class in which this field is declared.
     * @param String fieldName The name of the field to remove the bounds from.
     */
    public void removeFieldBounds(String className, String fieldName) {
	if(variableBoundMap == null) {
	    return;
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    return;
	}
	classMap.remove(fieldName);
    }

    /**
     * Remove the minimum integer value and the maximum integer value for the method local.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String methodName The name of the method in which the local is declared.
     * @param String localName The name of the local to remove the bounds from.
     */
    public void removeMethodLocalBounds(String className, String methodName, String localName) {
	if(variableBoundMap == null) {
	    return;
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    return;
	}
	Map methodMap = (Map)classMap.get(methodName);
	if(methodMap == null) {
	    return;
	}
	methodMap.remove(localName);
    }

    /**
     * Set the maximum integer value.
     */
    public void setDefaultIntMax(int intMax) {
	this.intMax = intMax;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the minimum integer value.
     */
    public void setDefaultIntMin(int intMin) {
	this.intMin = intMin;
	setChanged();
	notifyObservers();
    }

    /**
     * Set the minimum integer value and the maximum integer value for the field.
     *
     * @param String className The name of the class in which this field is declared.
     * @param String fieldName The name of the field in which to bound.
     * @param int min The minimum value that should be stored in this field.
     * @param int max The maximum value that should be stored in this field.
     */
    public void setFieldBounds(String className, String fieldName, int min, int max) {
	if(variableBoundMap == null) {
	    variableBoundMap = new HashMap();
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    classMap = new HashMap();
	    variableBoundMap.put(className, classMap);
	}
	List bounds = (List)classMap.get(fieldName);
	if(bounds == null) {
	    bounds = new ArrayList(2);
	    bounds.add(new Integer(min));
	    bounds.add(new Integer(max));
	    classMap.put(fieldName, bounds);
	}
	else {
	    Integer minWrapper = new Integer(min);
	    bounds.add(0, minWrapper);
	    Integer maxWrapper = new Integer(max);
	    bounds.add(1, maxWrapper);
	}
    }

    /**
     * Set the maximum integer value for the field.
     *
     * @param String className The name of the class in which this field is declared.
     * @param String fieldName The name of the field in which to bound.
     * @param int max The maximum value that should be stored in this field.
     */
    public void setFieldMax(String className, String fieldName, int max) {
	if(variableBoundMap == null) {
	    variableBoundMap = new HashMap();
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    classMap = new HashMap();
	    variableBoundMap.put(className, classMap);
	}
	List bounds = (List)classMap.get(fieldName);
	if(bounds == null) {
	    bounds = new ArrayList(2);
	    bounds.add(new Integer(getDefaultIntMin()));
	    bounds.add(new Integer(max));
	    classMap.put(fieldName, bounds);
	}
	else {
	    Integer maxWrapper = new Integer(max);
	    bounds.set(1, maxWrapper);
	}
    }

    /**
     * Set the minimum integer value for the field.
     *
     * @param String className The name of the class in which this field is declared.
     * @param String fieldName The name of the field in which to bound.
     * @param int min The minimum value that should be stored in this field.
     */
    public void setFieldMin(String className, String fieldName, int min) {
	if(variableBoundMap == null) {
	    variableBoundMap = new HashMap();
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    classMap = new HashMap();
	    variableBoundMap.put(className, classMap);
	}
	List bounds = (List)classMap.get(fieldName);
	if(bounds == null) {
	    bounds = new ArrayList(2);
	    bounds.add(new Integer(min));
	    bounds.add(new Integer(getDefaultIntMax()));
	    classMap.put(fieldName, bounds);
	}
	else {
	    Integer minWrapper = new Integer(min);
	    bounds.set(0, minWrapper);
	}
    }

    /**
     * Set the minimum integer value and maximum integer value for the method local in one step.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String methodName The name of the method in which the local is declared.
     * @param String localName The name of the local to bound.
     * @param int min The minimum value that this local should store.
     * @param int max The maximum value that this local should store.
     */
    public void setMethodLocalBounds(String className, String methodName, String localName, int min, int max) {
	if(variableBoundMap == null) {
	    variableBoundMap = new HashMap();
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    classMap = new HashMap();
	    variableBoundMap.put(className, classMap);
	}
	Map methodMap = (Map)classMap.get(methodName);
	if(methodMap == null) {
	    methodMap = new HashMap();
	    classMap.put(methodName, methodMap);
	}
	List bounds = (List)methodMap.get(localName);
	if(bounds == null) {
	    bounds = new ArrayList(2);
	    bounds.add(new Integer(min));
	    bounds.add(new Integer(max));
	    methodMap.put(localName, bounds);
	}
	else {
	    Integer minWrapper = new Integer(min);
	    bounds.add(0, minWrapper);
	    Integer maxWrapper = new Integer(max);
	    bounds.add(1, maxWrapper);
	}
    }

    /**
     * Set the maximum integer value for the method local.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String methodName The name of the method in which the local is declared.
     * @param String localName The name of the local to bound.
     * @param int max The maximum value that this local should store.
     */
    public void setMethodLocalMax(String className, String methodName, String localName, int max) {
	if(variableBoundMap == null) {
	    variableBoundMap = new HashMap();
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    classMap = new HashMap();
	    variableBoundMap.put(className, classMap);
	}
	Map methodMap = (Map)classMap.get(methodName);
	if(methodMap == null) {
	    methodMap = new HashMap();
	    classMap.put(methodName, methodMap);
	}
	List bounds = (List)methodMap.get(localName);
	if(bounds == null) {
	    bounds = new ArrayList(2);
	    bounds.add(new Integer(getDefaultIntMin()));
	    bounds.add(new Integer(max));
	    methodMap.put(localName, bounds);
	}
	else {
	    Integer maxWrapper = new Integer(max);
	    bounds.add(1, maxWrapper);
	}
    }

    /**
     * Set the minimum integer value for the method local.
     *
     * @param String className The name of the class in which the method (and thus the local) is declared.
     * @param String methodName The name of the method in which the local is declared.
     * @param String localName The name of the local to bound.
     * @param int min The minimum value that this local should store.
     */
    public void setMethodLocalMin(String className, String methodName, String localName, int min) {
	if(variableBoundMap == null) {
	    variableBoundMap = new HashMap();
	}
	Map classMap = (Map)variableBoundMap.get(className);
	if(classMap == null) {
	    classMap = new HashMap();
	    variableBoundMap.put(className, classMap);
	}
	Map methodMap = (Map)classMap.get(methodName);
	if(methodMap == null) {
	    methodMap = new HashMap();
	    classMap.put(methodName, methodMap);
	}
	List bounds = (List)methodMap.get(localName);
	if(bounds == null) {
	    bounds = new ArrayList(2);
	    bounds.add(new Integer(min));
	    bounds.add(new Integer(getDefaultIntMax()));
	    methodMap.put(localName, bounds);
	}
	else {
	    Integer minWrapper = new Integer(min);
	    bounds.add(0, minWrapper);
	}
    }
}
