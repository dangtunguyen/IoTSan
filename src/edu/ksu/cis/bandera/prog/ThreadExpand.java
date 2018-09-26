package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * IotChecker, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratory (santos@cis.ksu.edu)    *
 * Matt Dwyer (dwyer@cis.ksu.edu)			             *	
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.spin.SpinUtil;
import edu.ksu.cis.bandera.annotation.*;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.*;

/**
 * This class implements a Jimple transformation that replaces
 * thread operations, such as <code>t.start()</code>, called on a
 * <code>ThreadType</code> instance, <code>t</code>, with a 
 * dynamic-type-specific version of the start method 
 * <code>IotChecker.start(ThreadType,t)</code>.  This is achieved
 * by inserting explicit type tests over the possible dynamic
 * types of <code>t</.code>.
 *
 * It is assumed that this phase is invoked after inlining as it does
 * not attempt to walk down through the calling hierarchy.
 */
public class ThreadExpand {
	private static Category log = 
	        Category.getInstance(ThreadExpand.class.getName());

	private static AnnotationManager annotationManager;

/**
 *
 */
public static void expandStart(SootClass [] sootClasses, AnnotationManager am) {

	// Thread class has its fields pruned down to "target" which 
	// is made public.

	annotationManager = am;
	SootClassManager scm = sootClasses[0].getManager();
	SootClass jlt = scm.getClass("java.lang.Thread");
	for (Iterator it = jlt.getFields().iterator(); it.hasNext(); ) {
	    SootField jltField = (SootField) it.next();
	    if (jltField.getName().equals("target")) {
		jltField.setModifiers(Modifier.PUBLIC);
	    } else {
	    	jlt.removeField(jltField);
	    } 
	}

	BanderaBuiltin.init(scm);
	JavaLang.init(scm);
	jlt = scm.getClass("java.lang.Thread");
    for (Iterator it = jlt.getMethods().iterator(); it.hasNext();) {
	    SootMethod m = (SootMethod)it.next();
    }  

	for (int i = 0; i < sootClasses.length; i++) {
		/* [Thomas, May 4, 2017]
		 * main = EvtHandler
		 * */
//	   if (sootClasses[i].declaresMethod("main"))
//		expandMethod(sootClasses[i].getMethod("main"));
		for(SootMethod sm : SpinUtil.getEvtHandlerMethods(sootClasses[i]))
		{
			expandMethod(sm);
		}

	   if (sootClasses[i].declaresMethod("run"))
		expandMethod(sootClasses[i].getMethod("run"));
	}
}
/**
 */
private static void expandMethod(SootMethod method) {
    SootClassManager theManager = method.getDeclaringClass().getManager();
    JimpleBody body = (JimpleBody) method.getBody(Jimple.v());
    StmtList sList = body.getStmtList();
    List methodStmtList = new ArrayList();
    List expandStmtList;
    Stmt stmts[] = new Stmt[sList.size()];
    Stmt redirects[] = new Stmt[sList.size()*2];
    int numRedirects = 0;
    Stmt s;
    System.arraycopy(sList.toArray(), 0, stmts, 0, stmts.length);

    for (int si = 0; si < stmts.length; si++) {
        s = stmts[si];

	ThreadFixer.fix(s, body);

        if (s instanceof InvokeStmt) {
    	    InvokeExpr iexpr = (InvokeExpr)((InvokeStmt)s).getInvokeExpr();
    	    if (iexpr instanceof VirtualInvokeExpr &&
                iexpr.getMethod().getName().equals("start") ) {

    	        // If we have something like:
    	        //    x.start();
    	        // then we need to determine whether "x" is a thread
    	        // with a run method and generate a "thread-specific
    	        // start" method call.  For example if "x" is "T" and "T1"
    	        // and "T2" are sub-types of "T" we generate
    	        //    if (x instanceof T1) IotChecker.start(T1,x)
    	        //    else if (x instanceof T2) IotChecker.start(T2,x)
    	        //    else if (x instanceof T) IotChecker.start(T,x)
    	        //    else IotChecker.assert("Run-time type error");
    	        // if "x" was created with a runnable parameter then   
    	        // our "thread-specific start" should be based on the
                // type fo the target, i.e., "x.target".

                expandStmtList = new ArrayList();

    	        Value receiverRef = ((NonStaticInvokeExpr)iexpr).getBase();
     	        SootClass receiverType = null;
    	        if (receiverRef.getType() instanceof RefType) {
    	            receiverType = 
    		        theManager.getClass(
    			    ((RefType)receiverRef.getType()).className);
    	        } else {
     		    log.fatal("ThreadExpand: unexpected type");
     		    throw new java.lang.RuntimeException();
    	        }

                Stmt runnableCase = Jimple.v().newNopStmt();
                Stmt endOfExpansion = Jimple.v().newNopStmt();

	        // Begin atomic 
		Stmt firstStmt = Jimple.v().newInvokeStmt(
    	                             Jimple.v().newStaticInvokeExpr(
    	                               BanderaBuiltin.getMethod("beginAtomic"), 
			    	       new LinkedList()));
    	        expandStmtList.add(firstStmt);

		// Store current statement and first stmt of expansion
		// so that we can redirect jumps 
		redirects[numRedirects++] = s;
		redirects[numRedirects++] = firstStmt;

		// Add local for receiver
        	Local localRunnable = 
		    declareRefLocal(body, 
                                       ca.mcgill.sable.soot.RefType.v(
				            "java.lang.Runnable"));

		// Add local for receiver
        	Local localBool = declareBoolLocal(body);

	        // Test the target field of the object
		SootField targetField;
		targetField = getField("target", receiverType);
		if(targetField == null) {
		    log.debug("target field was not found in " + receiverType + ".  Declaring it.");
		    targetField = 
			new SootField("target", 
				      ca.mcgill.sable.soot.RefType.v(
					  "java.lang.Runnable"),
				      Modifier.PUBLIC);
		    receiverType.addField(targetField);
		}
		/*
		if (receiverType.declaresField("target")) {
		    targetField = receiverType.getField("target");
		} else {
		    targetField = 
			new SootField("target", 
				      ca.mcgill.sable.soot.RefType.v(
					  "java.lang.Runnable"),
				      Modifier.PUBLIC);
		    receiverType.addField(targetField);
		}
		*/

      	        Value targetRef = 
		    Jimple.v().newInstanceFieldRef(receiverRef, targetField);
                expandStmtList.add(Jimple.v().newAssignStmt(localRunnable, targetRef));
                Value ee = Jimple.v().newNeExpr(localRunnable, NullConstant.v()); 
                expandStmtList.add(Jimple.v().newIfStmt(ee, runnableCase));


                // emit the code for type tests and start thread calls
                //    for each runnable class, C, 
                //        from lowest to highest in hierarchy generate:
                Vector jumpTable = new Vector();

                List subTypes  = HierarchyQuery.orderedClasses(receiverType);
                for (int caseIndex = 0; caseIndex < subTypes.size(); 
		     caseIndex++) {
                    SootClass currentType = (SootClass) subTypes.get(caseIndex);

		    if (currentType.getName().equals("java.lang.Thread")) {
                       jumpTable.addElement(null);
                       continue;
                    } 

            	    // record nop that marks "then" part of type test
                    Stmt target = Jimple.v().newNopStmt();
                    jumpTable.addElement(target);

                    // generate the type test
                    Value ioe = Jimple.v().newInstanceOfExpr(
                        receiverRef, RefType.v(currentType.getName()));
                    expandStmtList.add(Jimple.v().newAssignStmt(localBool, ioe));
                    ee = Jimple.v().newEqExpr(localBool, IntConstant.v(1));
                    expandStmtList.add(Jimple.v().newIfStmt(ee, target));
                }

		// if none of the above "then" go to case for receiverType
                Stmt receiverTarget = Jimple.v().newNopStmt();
                expandStmtList.add(Jimple.v().newGotoStmt(receiverTarget));

		LinkedList parms;

                for (int caseIndex = 0; caseIndex < subTypes.size(); 
                     caseIndex++) {

		    if ( ((SootClass) subTypes.get(caseIndex)).getName().equals("java.lang.Thread") ) 
                       continue;

                    // add nop that marks this "then" part
                    expandStmtList.add((Stmt) jumpTable.elementAt(caseIndex));

                    // generate the new startThread invocation
                    parms = new LinkedList();
                    parms.addLast(
                        StringConstant.v(
                            ((SootClass) subTypes.get(caseIndex)).getName()));
                    parms.addLast(receiverRef);
                    expandStmtList.add(
			Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                BanderaBuiltin.getMethod("startThread"), 
			        parms)));

                    // jump over the rest of the cases
                    expandStmtList.add(Jimple.v().newGotoStmt(endOfExpansion));
                }

	        // emit case for receiver type
                expandStmtList.add(receiverTarget);

                parms = new LinkedList();
                parms.addLast(StringConstant.v(receiverType.getName()));
                parms.addLast(receiverRef);
                expandStmtList.add(
                    Jimple.v().newInvokeStmt(
                        Jimple.v().newStaticInvokeExpr(
                            BanderaBuiltin.getMethod("startThread"), parms)));

                // jump over the runnable case
                expandStmtList.add(Jimple.v().newGotoStmt(endOfExpansion));

                expandStmtList.add(runnableCase);

                // emit the code for runnable case
                jumpTable = new Vector();
		SootClass runnableTypes = theManager.getClass("java.lang.Runnable");
                subTypes  = HierarchyQuery.orderedClasses(runnableTypes);
                for (int caseIndex = 0; caseIndex < subTypes.size();
                     caseIndex++) {
                    SootClass currentType = (SootClass) subTypes.get(caseIndex);

		    if (currentType.getName().equals("java.lang.Thread")) {
                       jumpTable.addElement(null);
                       continue;
                    } 

                    // record nop that marks "then" part of type test
                    Stmt target = Jimple.v().newNopStmt();
                    jumpTable.addElement(target);

                    // generate the type test
                    Value ioe = Jimple.v().newInstanceOfExpr(
                        localRunnable, RefType.v(currentType.getName()));
                    expandStmtList.add(Jimple.v().newAssignStmt(localBool, ioe));
                    ee = Jimple.v().newEqExpr(localBool, IntConstant.v(1));
                    expandStmtList.add(Jimple.v().newIfStmt(ee, target));
                }

                // no runnable type matched (can this should be flagged as 
                // "runtime" error?)

                expandStmtList.add(Jimple.v().newGotoStmt(endOfExpansion));

                for (int caseIndex = 0; caseIndex < subTypes.size();
                     caseIndex++) {

		    if ( ((SootClass) subTypes.get(caseIndex)).getName().equals("java.lang.Thread") ) 
                       continue;

                    // add nop that marks this "then" part
                    expandStmtList.add((Stmt) jumpTable.elementAt(caseIndex));

		    // reload target ref to local for invoke
                    expandStmtList.add(
			Jimple.v().newAssignStmt(localRunnable, targetRef));

                    // generate the new startThread invocation
                    parms = new LinkedList();
                    parms.addLast(
                        StringConstant.v(
                            ((SootClass) subTypes.get(caseIndex)).getName()));
                    parms.addLast(localRunnable);
                    expandStmtList.add(
			Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                BanderaBuiltin.getMethod("startThread"), 
				parms)));

                    // jump over the rest of the cases
                    expandStmtList.add(Jimple.v().newGotoStmt(endOfExpansion));
                }
                expandStmtList.add(endOfExpansion);

                // end atomic 
                expandStmtList.add(
                    Jimple.v().newInvokeStmt(
                        Jimple.v().newStaticInvokeExpr(
                            BanderaBuiltin.getMethod("endAtomic"), 
			    new LinkedList())));

		// propogate annotations to expanded stmts 
    		ca.mcgill.sable.util.Iterator it = expandStmtList.iterator();
	        while (it.hasNext()) {
                   Stmt n = (Stmt) it.next();
		   annotationManager.putInlineStmt(n, s, method);
		}

		methodStmtList.addAll(expandStmtList);

	    } else if (iexpr instanceof SpecialInvokeExpr &&
                       iexpr.getMethod().getName().equals("<init>") ) {
	        // If we have something like: 
	        //    x = new Thread(..., runnable, ...);
	        // then replace it by: 
	        //    x = new Thread();
	        //    x.target = runnable;
             
                expandStmtList = new ArrayList();

		SootMethod tc = iexpr.getMethod();

log.debug("Looking for init of java.lang.Runnable instance");

	        // Check for a Runnable constructor parameter and record
	        // record its index in the parameter list
	        int runnableParameter = -1;
	        for (int pi = 0; pi < tc.getParameterCount(); pi++) {
	            Type pt = tc.getParameterType(pi);
	            if (pt instanceof RefType && 
 	                ((RefType)pt).toString().equals(
	 		    "java.lang.Runnable")) {
		    	runnableParameter = pi; 
		    }
		}

		// If a Runnable parameter exists then splice in a
		// default constructor call and an assignment to the
		// target field.
		if (runnableParameter >= 0) {
log.debug("Found init of java.lang.Runnable instance");
		    // Modify constructor call to call default constructor
		    Value base = ((SpecialInvokeExpr)iexpr).getBase();
		    RefType baseType = (RefType)base.getType();

	            // Begin atomic 
		    Stmt firstStmt = 
			    Jimple.v().newInvokeStmt(
    	                        Jimple.v().newStaticInvokeExpr(
    	                            BanderaBuiltin.getMethod("beginAtomic"), 
			    	    new LinkedList()));
    	            expandStmtList.add(firstStmt);

		    // Store current statement and first stmt of expansion
		    // so that we can redirect jumps 
		    redirects[numRedirects++] = s;
		    redirects[numRedirects++] = firstStmt;

		    // Add local
        	    Local localBase = declareRefLocal(body, baseType);

		    // Assign base to a local
		    expandStmtList.add(Jimple.v().newAssignStmt(localBase, base));

		    SootMethod sm = 
			tc.getDeclaringClass().getMethod(
			    "<init>", new LinkedList(), VoidType.v());

		    SpecialInvokeExpr newConstructor =
			Jimple.v().newSpecialInvokeExpr(localBase, sm, 
						    new LinkedList());
		    expandStmtList.add(Jimple.v().newInvokeStmt(newConstructor));

		    // Build assignment to target field
		    SootField targetField;
		    if (tc.getDeclaringClass().declaresField("target")) {
		        targetField = tc.getDeclaringClass().getField("target");
		    } else {
		        targetField = 
			    new SootField("target", 
				          ca.mcgill.sable.soot.RefType.v(
					      "java.lang.Runnable"),
				          Modifier.PUBLIC);
		        tc.getDeclaringClass().addField(targetField);
		    } 

		    Value runInstance = iexpr.getArg(runnableParameter);
		    AssignStmt targetStore =
		        Jimple.v().newAssignStmt(
		  	   Jimple.v().newInstanceFieldRef(base, targetField),
			   runInstance);
		    expandStmtList.add(targetStore);

                    // end atomic 
                    expandStmtList.add(
                        Jimple.v().newInvokeStmt(
                            Jimple.v().newStaticInvokeExpr(
                                BanderaBuiltin.getMethod("endAtomic"), 
			        new LinkedList())));
		}

		// propogate annotations for s, to new stmts
    		ca.mcgill.sable.util.Iterator it = expandStmtList.iterator();
	        while (it.hasNext()) {
                   Stmt n = (Stmt) it.next();
		   annotationManager.putInlineStmt(n, s, method);
		}

		methodStmtList.addAll(expandStmtList);

	    } else {
	        // otherwise add this statement to the statement list 
	        methodStmtList.add(s);
 	    }

	} else {
	    // otherwise add this statement to the statement list 
	    methodStmtList.add(s);
	} 
    }

    // update the statement list
    body.getStmtList().clear();
    body.getStmtList().addAll(methodStmtList);

    // redirect the jumps 
    for (int i = 0; i < numRedirects; i = i + 2) {
       body.redirectJumps(redirects[i], redirects[i+1]);
    }
}

static Local declareBoolLocal(JimpleBody body) {
    Local local;
    if (body.declaresLocal("EXPANDTMP$Bool")) {
        local = body.getLocal("EXPANDTMP$Bool");
    } else {
        local = 
	Jimple.v().newLocal("EXPANDTMP$Bool",
                             ca.mcgill.sable.soot.IntType.v());
	body.addLocal(local);
    }
    return local;
}

static Local declareRefLocal(JimpleBody body, RefType type) {
    Local local;
    if (body.declaresLocal("EXPANDTMP$"+type.className)) {
        local = body.getLocal("EXPANDTMP$"+type.className);
    } else {
        local = 
	Jimple.v().newLocal("EXPANDTMP$"+type.className,
                             ca.mcgill.sable.soot.RefType.v(type.className));
	body.addLocal(local);
    }
    return local;
}


/**
 * Unit test driver.
 * takes as input
 * <ul>
 * <li> a name for a Java source file
 * </ul>
 * compiles the source file, expands all thread starts and prints
 * the resulting jimple. 
 */
public static void main(String args[]) {
        CompilationManager.reset();
        CompilationManager.setFilename(args[0]);
        CompilationManager.setClasspath(".");
        CompilationManager.setIncludedPackagesOrTypes(new String[0]);

        try {
            CompilationManager.compile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Hashtable exceptions = CompilationManager.getExceptions();
        if (exceptions.size() > 0) {
            log.debug("Compilation failed:");
            for (Enumeration e = exceptions.keys(); e.hasMoreElements();) {
                Object filename = e.nextElement();
                log.debug("- " + filename);
                Vector es = (Vector) exceptions.get(filename);
                for (java.util.Iterator i = es.iterator(); i.hasNext();) {
                    log.debug("  * " + i.next());
                }
            }
            return;
        }

        Hashtable storedClasses = CompilationManager.getCompiledClasses();
        int numClasses = 0;
        for (Enumeration scIter = storedClasses.elements();
             scIter.hasMoreElements(); ) {
            SootClass cc = (SootClass) scIter.nextElement();
            cc.resolveIfNecessary();
	    numClasses++;
        }

log.debug("done compiling");

	SootClass [] classes = new SootClass[numClasses];
	numClasses = 0;
        for (Enumeration scIter = storedClasses.elements();
             scIter.hasMoreElements(); numClasses++) {
            SootClass cc = (SootClass) scIter.nextElement();
	    classes[numClasses] = cc;
        }

log.debug("starting method dump");
	for (int i = 0; i < classes.length; i++) {
	    SootClass current = classes[i]; 
	    Object [] methods = current.getMethods().toArray();
	    for (int j = 0; j < methods.length; j++) {
		((SootMethod)methods[j]).getBody(Jimple.v()).printTo(new java.io.PrintWriter(System.out, true), 0);
	    }
	} 	

log.debug("dumping methods of java.lang.Thread");
	SootClass jlt = classes[0].getManager().getClass("java.lang.Thread");
        for (Iterator it = jlt.getMethods().iterator(); it.hasNext();) {
	    SootMethod m = (SootMethod)it.next();
log.debug(m);
        }  


log.debug("starting expansion");
	expandStart(classes, new AnnotationManager());

log.debug("starting expanded method dump");
	for (int i = 0; i < classes.length; i++) {
	    SootClass current = classes[i]; 
	    Object [] methods = current.getMethods().toArray();
	    for (int j = 0; j < methods.length; j++) {
		((SootMethod)methods[j]).getBody(Jimple.v()).printTo(new java.io.PrintWriter(System.out, true), 0);
	    }
	} 	


}


    /**
     * Get the named field, fieldName, from the class given, sootClass.  This
     * will search the entire hierarchy of super classes until it finds the named
     * field or reaches the root/base class for this class.
     *
     * @param String fieldName The name of the field to get.
     * @param SootClass sootClass The class in which to locate the field.
     * @return SootField The field that matches the field name given or null if not found.
     */
    private static SootField getField(String fieldName, SootClass sootClass) {
	SootField field = null;
	if(sootClass == null) {
	    log.info("Cannot get a SootField from a null SootClass.  Returning null.");
	    return(field);
	}

	if(sootClass.declaresField(fieldName)) {
	    log.debug("Found the SootField in this SootClass.");
	    field = sootClass.getField(fieldName);
	}
	else {
	    log.debug("The SootField was not found in this SootClass.");
	    if(sootClass.hasSuperClass()) {
		log.debug("The SootClass has a super class that we are checking ...");
		field = getField(fieldName, sootClass.getSuperClass());
	    }
	}

	return(field);
    }

}
