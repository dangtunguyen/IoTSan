package edu.ksu.cis.bandera.sessions;

import edu.ksu.cis.bandera.abstraction.util.AbstractionClassLoader;

import edu.ksu.cis.bandera.sessions.Abstraction;
import edu.ksu.cis.bandera.sessions.ClassDescription;
import edu.ksu.cis.bandera.sessions.MethodDescription;
import edu.ksu.cis.bandera.sessions.FieldDescription;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;

import ca.mcgill.sable.soot.SootClassManager;
import ca.mcgill.sable.soot.SootField;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.BuildAndStoreBody;
import ca.mcgill.sable.soot.StoredBody;
import ca.mcgill.sable.soot.ClassFile;

import ca.mcgill.sable.soot.jimple.Local;
import ca.mcgill.sable.soot.jimple.Jimple;
import ca.mcgill.sable.soot.jimple.JimpleBody;

import edu.ksu.cis.bandera.abstraction.util.LocalMethod;

import edu.ksu.cis.bandera.jjjc.CompilationManager;

import org.apache.log4j.Category;

/**
 * The AbstractionConverter provides an easy way to convert from one datastructure
 * storing abstraction to another.  In this case, we use the Abstraction object
 * provided in the edu.ksu.cis.bandera.sessions package for storing abstraction
 * information in the session file while we use a Hashtable of abstraction information
 * in the TypeGUI (and other portions of Bandera).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class AbstractionConverter {

    /**
     * The log that will be used to write messages to.
     */
    private static final Category log = Category.getInstance(AbstractionConverter.class.getName());

    /**
     *
     *
     * @param Hashtable abstractionTable The table of abstraction information to be converted.
     * @return Abstraction A new Abstraction object containing the abstraction information provided
     *         in the Hashtable given.
     */
    public Abstraction convert(Hashtable abstractionTable) {

	Abstraction abstraction = new Abstraction();

	if((abstractionTable == null) || (abstractionTable.size() <= 0)) {
	    return(abstraction);
	}

	Iterator i = abstractionTable.keySet().iterator();
	while(i.hasNext()) {
	    Object t = i.next();
	    if(t == null) {
		log.debug("Found a null key when expecting a class, field, method, or local.  That is weird!");
		continue;
	    }
	    if((t instanceof SootField) || (t instanceof SootMethod) || (t instanceof Local)) {
		log.debug("Found a field, method, or local.  Skipping it.");
		continue;
	    }
	    if(t instanceof SootClass) {
		SootClass sc = (SootClass)t;
		ClassDescription classDescription = new ClassDescription();
		classDescription.setName(sc.getName());
		abstraction.addClassDescription(classDescription);
		Object s = abstractionTable.get(sc);
		if(s == null) {
		    log.debug("Found a null set of methods and fields for the SootClass " + sc.getName());
		    continue;
		}
		
		if(s instanceof Set) {
		    Set set = (Set)s;
		    Iterator j = set.iterator();
		    while(j.hasNext()) {
			Object r = j.next();
			if(r == null) {
			    log.debug("Found a null key when expecting a method or field.  That is weird!");
			    continue;
			}
			
			if(r instanceof SootField) {
			    SootField sf = (SootField)r;
			    Object x = abstractionTable.get(sf);
			    if(x == null) {
				log.debug("Found a null abstraction for SootField " + sf.getName());
				continue;
			    }
			    if(x instanceof edu.ksu.cis.bandera.abstraction.Abstraction) {
				edu.ksu.cis.bandera.abstraction.Abstraction a = (edu.ksu.cis.bandera.abstraction.Abstraction)x;
				FieldDescription fd = new FieldDescription();
				fd.setName(sf.getName());
				fd.setAbstraction(a.toString());
				classDescription.addFieldDescription(fd);
			    }
			}
			
			if(r instanceof SootMethod) {
			    SootMethod sm = (SootMethod)r;
			    MethodDescription methodDescription = new MethodDescription();
			    methodDescription.setName(sm.getName());
			    methodDescription.setReturnType(sm.getReturnType().toString());
			    classDescription.addMethodDescription(methodDescription);
			    Object p = abstractionTable.get(sm);
			    if(p == null) {
				log.debug("Found a null set of locals for the SootMethod " + sm.getName());
				continue;
			    }
			    if(p instanceof Set) {
				Set localSet = (Set)p;
				Iterator k = localSet.iterator();
				while(k.hasNext()) {
				    Object o = k.next();
				    if(o == null) {
					log.debug("Found a null key when expecting a local.  This is weird!");
					continue;
				    }
				    if(o instanceof Local) {
					Local local = (Local)o;
					LocalMethod lm = new LocalMethod(sm, local);
					Object n = abstractionTable.get(lm);
					if(n == null) {
					    log.debug("Found a null abstraction for local " + local.getName());
					    continue;
					}
					if(n instanceof edu.ksu.cis.bandera.abstraction.Abstraction) {
					    edu.ksu.cis.bandera.abstraction.Abstraction a = (edu.ksu.cis.bandera.abstraction.Abstraction)n;
					    FieldDescription fd = new FieldDescription();
					    fd.setName(local.getName());
					    fd.setAbstraction(a.toString());
					    methodDescription.addLocal(fd);
					}
				    }
				}
			    }
			}
			
		    }
		}
	    }
	}

	return(abstraction);
    }

    /**
     * Convert the abstraction information stored in the Abstraction object into
     * a Hashtable.
     * The Hashtable will have the following structure:
     * <ul>
     * <li>key: SootClass, value: HashSet(holds SootField and SootMethod objects)</li>
     * <li>key: SootField, value: Abstraction</li>
     * <li>key: SootMethod, value: HashSet(holds Locals)</li>
     * <li>key: LocalMethod (a combination of Local and SootMethod), value: Abstraction</li>
     * </ul>
     *
     * @param Abstraction abstraction The abstraction information to be converted.
     * @return Hashtable The Hashtable of abstraction information.
     */
    public Hashtable convert(Abstraction abstraction) {

	if(abstraction == null) {
	    log.debug("abstraction is null.  Returning without setting up the type tree.");
	    return(new Hashtable(0));
	}

	Hashtable table = new Hashtable();
	SootClassManager sootClassManager = CompilationManager.getSootClassManager();
	Set classDescriptions = abstraction.getClassDescriptions();
	if(classDescriptions != null) {
	    Iterator cdi = classDescriptions.iterator();
	    while(cdi.hasNext()) {
		ClassDescription classDescription = (ClassDescription)cdi.next();
		if(classDescription != null) {
		    log.debug("Now processing class " + classDescription.getName());
		    try {
			HashSet hs = new HashSet();
			String className = classDescription.getName();
			SootClass sootClass = sootClassManager.getClass(className);
			table.put(sootClass, hs);
			Set fieldDescriptions = classDescription.getFieldDescriptions();
			if(fieldDescriptions != null) {
			    Iterator fdi = fieldDescriptions.iterator();
			    while(fdi.hasNext()) {
				FieldDescription fieldDescription = (FieldDescription)fdi.next();
				if(fieldDescription != null) {
				    log.debug("Now processing field " + fieldDescription.getName());
				    try {
					String fieldName = fieldDescription.getName();
					String abstractionName = fieldDescription.getAbstraction();
					SootField sootField = sootClass.getField(fieldName);
					hs.add(sootField);
					log.debug("Getting abstraction with name [" + abstractionName + "].");
					edu.ksu.cis.bandera.abstraction.Abstraction a =
					    (edu.ksu.cis.bandera.abstraction.Abstraction)AbstractionClassLoader.getClass(abstractionName).getMethod("v", new Class[0]).invoke(null, new Object[0]);


					log.debug("Adding field " + sootField.toString() + " with abstraction " + a.toString() + ".");
					table.put(sootField, a);
				    }
				    catch(Exception e) {
					log.warn("An exception occured while loading a field's abstraction.", e);
				    }
				}
			    }
			}
			
			Set methodDescriptions = classDescription.getMethodDescriptions();
			if(methodDescriptions != null) {
			    Iterator mdi = methodDescriptions.iterator();
			    while(mdi.hasNext()) {
				MethodDescription methodDescription = (MethodDescription)mdi.next();
				if(methodDescription != null) {
				    log.debug("Now processing method " + methodDescription.getName());
				    String methodName = methodDescription.getName();
				    SootMethod sootMethod = null;
				    try {
					sootMethod = sootClass.getMethod(methodName);
				    }
				    catch(Exception e) {
					log.info("Exception while getting the SootMethod for method " + methodDescription.getName());
					continue;
				    }
				    //List parameterTypes = ?;
				    //SootMethod sootMethod = sootClass.getMethod(methodName, parameterTypes);
				    hs.add(sootMethod);
				    HashSet methodLocals = new HashSet();
				    table.put(sootMethod, methodLocals);
				    Set locals = methodDescription.getLocals();
				    if(locals != null) {
					Jimple jimple = Jimple.v();
					if (!sootMethod.isBodyStored(jimple)) {
					    new BuildAndStoreBody(jimple, new StoredBody(ClassFile.v()), 0).resolveFor(sootMethod);
					}
					JimpleBody body = (JimpleBody) sootMethod.getBody(jimple);
					Iterator li = locals.iterator();
					while(li.hasNext()) {
					    FieldDescription local = (FieldDescription)li.next();
					    if(local != null) {
						log.debug("Now processing local " + local.getName());
						try {
						    String localName = local.getName();
						    String abstractionName = local.getAbstraction();
						    Local l = body.getLocal(localName);
						    edu.ksu.cis.bandera.abstraction.Abstraction a =
							(edu.ksu.cis.bandera.abstraction.Abstraction)AbstractionClassLoader.getClass(abstractionName).getMethod("v", new Class[0]).invoke(null, new Object[0]);
						    methodLocals.add(l);
						    LocalMethod lm = new LocalMethod(sootMethod, l);
						    log.debug("Adding local " + lm.toString() + " with abstraction " + a.toString() + ".");
						    table.put(lm, a);
						}
						catch(Exception e) {
						    log.warn("An exception occured while loading a local variable's abstraction.", e);
						}
					    }
					}
				    }
				}
			    }
			}
		    }
		    catch(Exception e) {
			log.warn("An exception occured while loading a class's abstraction.", e);
		    }
		}
	    }
	}

	if(log.isDebugEnabled()) {
	    if(table != null) {
		log.debug("The table built that contains the abstraction information has a size of " + table.size());
	    }
	    else {
		log.debug("The table built that contains the abstraction information is null.");
	    }
	}

	return(table);
    }
}
