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

import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import org.apache.log4j.Category;

/**
 * The entry class of the slicer. This class shows the program structure of the
 * slicer:
* <ul>
   * <li> Collect some basic information of program;
   * <li> Determine slicing criterion;
   * <li> Build program dependency graph for each method in the program;
   * <li> Slice each method in the program;
   * <li> Construct residual program;
   * <li> Dump residual program.
   * </ul>
* Usage of this class:
* <p>
* <code>
* Slicer slicer = new Slicer(sootClassArray, sliceCriterion, annotationManager);
* <br>slicer.run();
* <br> ... ...
* <br>SootClass sliceResult[] = slicer.result();
* </code>
 */

public class Slicer {
    private static final Category log = Category.getInstance(Slicer.class);

	public static boolean Turn_On_BOFA = true;
	public static edu.ksu.cis.bandera.bofa.Analysis BOFA_Analysis = null;
	private PostProcessOnAnnotation postProcessOnAnnotation = null;
	public static AnnotationManager annManagerForSlicer = null;
	/**
	* a set of reachable {@link SootMethod SootMethod}.
	*/
	public static Set reachableMethods;
	/**
	* a set of unreachable {@link SootClass SootClass}.
	*/
	public static Set unreachableClasses;
	/** 
	* an array for residual classes.
	*/
	private SootClass residualSootClassArray[];
	/**
	* a list of {@link ClassInfo ClassInfo}.
	*/
	private List classInfoList = new ArrayList();
	/**
	* a map from {@link SootMethod SootMethod} to
	* {@link MethodInfo MethodInfo}, for all methods
	* of all involved classes.
	*/
	public static Map sootMethodInfoMap = null;
	public static SliceTraceNode sliceTraceRoot;
	/**
	* a set of {@link SliceTraceNode SliceTraceNode}.
	*/
	public static Set allSliceTraceNodes;
	/**
	* a map from {@link SootClass SootClass} to a {@link Set Set} of
	* {@link SootClass SootClass}, representing that one interface
	* is implemented by a set of classes, or one abstract class is
	* implemented(extended) by a set of classes.
	*/
	static Map interfaceImplementedByMap = null;
	/**
	* a {@link Set Set} of {@link MethodInfo MethodInfo}
	* such that the corresponding method has slicing criterion.
	*/
	static Set originalMethods = null;
	/**
	* number of involved classes.
	*/
	static int classNum;
	static SootClass relevantClassArray[];
	private AnnotationManager cfanns;
	/**
	* slicing criterion --- a vector of {@link SliceInterest SliceInterest}.
	*/ 
	private Vector sliceInterestVec;
/**
 * The constructor for the case that there is <b>NO</b> slicing criterion specified
 * by users.
 * <p>
 * @param relClasses an array of {@link SootClass SootClass} which will be analysed and sliced
 * by the slicer.
 * @param annoManager annotation manager.
 */
public Slicer(SootClass relClasses[], AnnotationManager annoManager) {
	SlicingMethod.criterionChanged = false;
	//SlicingMethod.originalMdsFromReadyCallSite = new ArraySet();
	SlicingMethod.alreadyGenCritByReadyCallSite = new ArraySet();
	SlicingMethod.alreadyGenerateCriterionForExits = new ArraySet();
	MethodCallAnalysis.directReadyForWaitCallSites = new ArraySet();
	InfoAnalysis.nativeMdSig = new ArraySet();
	annManagerForSlicer = annoManager;
	relevantClassArray = relClasses;
	cfanns = annoManager;
	classNum = relevantClassArray.length;
	originalMethods = new ArraySet();
	interfaceImplementedByMap = new HashMap();
	sootMethodInfoMap = new HashMap();
	sliceTraceRoot = new SliceTraceNode();
	allSliceTraceNodes = new ArraySet();
	reachableMethods = new ArraySet();
	unreachableClasses = new ArraySet();
	sliceInterestVec = new Vector();

}
/**
 * The constructor for the case that there is slicing criterion specified
 * by users.
 * <p>
 * @param relClasses an array of {@link SootClass SootClass} which will be analysed and sliced
 * by the slicer.
 * @param interestVector slicing criterion.
 * A {@link Vector} of {@link SliceInterest SliceInterest}.
 * @param annoManager annotation manager.
 */
public Slicer(SootClass relClasses[], Vector interestVector, AnnotationManager annoManager) {
	this(relClasses, annoManager);
	sliceInterestVec = interestVector;
}
/**
* Build up {@link #classInfoList classInfoList} 
* and {@link #interfaceImplementedByMap interfaceImplementedByMap} 
* for each class.
* <br> Set value for {@link Slicer#classNum Slicer.classNum}.
*/
private void basicInformation() {
	for (int i = 0; i < relevantClassArray.length; i++) {
		SootClass sootClass = relevantClassArray[i];
		int modifiers = sootClass.getModifiers();
		if (Modifier.isInterface(modifiers)) {
		    //System.out.println("Class " + sootClass.getName() + " is an interface");
		    log.info("Class " + sootClass.getName() + " is an interface");
			continue;
		}
		if (sootClass.getMethods().size() == 0) {
		    //System.out.println("There is no method in class " + sootClass.getName());
			log.error("There is no method in class " + sootClass.getName());
			continue;
		}
		InfoAnalysis information = new InfoAnalysis(sootClass);
		ClassInfo classInfo = new ClassInfo();
		classInfo.sootClass = sootClass;
		classInfo.methodsInfoList = information.getMethodsInfoList();
		classInfoList.add(classInfo);
		if (information.getMethodsInfoList().isEmpty()) {
			//if there is no static field

			if (!haveStaticFields(sootClass))
				Slicer.unreachableClasses.add(sootClass);
			else
				
				//add clinit method into reachableMethods if any
				{
				if (sootClass.declaresMethod("<clinit>"))
					Slicer.reachableMethods.add(sootClass.getMethod("<clinit>"));
			}
		} else
			if (sootClass.declaresMethod("<clinit>"))
				Slicer.reachableMethods.add(sootClass.getMethod("<clinit>"));
	}
	//Slicer.classNum = classInfoList.size();
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-21 1:16:30)
 */
private void buildInterfaceAndSuperClassMap() {
	for (int i = 0; i < relevantClassArray.length; i++) {
		SootClass sootClass = relevantClassArray[i];
		int modifiers = sootClass.getModifiers();
		if (Modifier.isInterface(modifiers)) {
		    //System.out.println("Class " + sootClass.getName() + " is an interface");
			log.info("Class " + sootClass.getName() + " is an interface");
			continue;
		}
		if (sootClass.getMethods().size() == 0) {
		    //System.out.println("There is no method in class " + sootClass.getName());
			log.error("There is no method in class " + sootClass.getName());
			continue;
		}
		for (Iterator inIt = sootClass.getInterfaces().iterator(); inIt.hasNext();) {
			SootClass implementedClass = (SootClass) inIt.next();
			if (interfaceImplementedByMap.containsKey(implementedClass)) {
				((Set) interfaceImplementedByMap.get(implementedClass)).add(sootClass);
			} else {
				Set impSet = new ArraySet();
				impSet.add(sootClass);
				interfaceImplementedByMap.put(implementedClass, impSet);
			}
		}
		//Process abstract class and its subclass
		//added on 10/6/2000
		if (sootClass.hasSuperClass()) {
			try {
				SootClass superClass = sootClass.getSuperClass();
				//int modifier = superClass.getModifiers();
				//if (Modifier.isAbstract(modifier)) {
				//if (!superClass.getName().equals("java.lang.Object")) {
				if (!isObjectClass(superClass)) {
					if (interfaceImplementedByMap.containsKey(superClass)) {
						((Set) interfaceImplementedByMap.get(superClass)).add(sootClass);
					} else {
						Set impSet = new ArraySet();
						impSet.add(sootClass);
						interfaceImplementedByMap.put(superClass, impSet);
					}
				}
			} catch (NoSuperClassException e) {
			}
		}
	}
}
/**
* Build PDG without slicing criterion:
* {@link #basicInformation() basicInformation()},
* {@link #moreInformation() moreInformation()}. 
* <br> This method is only used by the browser of PDG.
*/
public void buildPDG() {
	Slicer.Turn_On_BOFA = true;
	if (Slicer.Turn_On_BOFA) {
		edu.ksu.cis.bandera.bofa.BOFA.reset();
		BOFA_Analysis = edu.ksu.cis.bandera.bofa.Analysis.init();
		edu.ksu.cis.bandera.bofa.BOFA.analyze();
	}
	buildInterfaceAndSuperClassMap();
	collectReachableMethods();
	basicInformation();
	collectOriginalMethods();
	moreInformation();
}
/**
* Collect all methods with slicing criterion into
* {@link #originalMethods originalMethods}.
*/
private void collectOriginalMethods() {
	for (Iterator sootMdIt = Slicer.sootMethodInfoMap.keySet().iterator(); sootMdIt.hasNext();)
	{
		SootMethod sootMethod = (SootMethod) sootMdIt.next();
		MethodInfo mdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(sootMethod);
		if (mdInfo.sCriterion == null) continue;
		originalMethods.add(mdInfo);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-21 0:03:47)
 */
private void collectReachableMethods() {
	Set startingMethods = getStartingMethods();
	//starting methods include all methods with slicing criterion
	//all methods such like "main" and "run";
	reachableMethods.addAll(startingMethods);
	Set visitedMds = new ArraySet();
	LinkedList workList = new LinkedList();
	workList.addAll(startingMethods);
	while (!workList.isEmpty()) {
		SootMethod sm = (SootMethod) workList.removeFirst();
		visitedMds.add(sm);
		int scModifiers = sm.getDeclaringClass().getModifiers();
		int smModifiers = sm.getModifiers();
		if (Modifier.isInterface(scModifiers) || Modifier.isAbstract(smModifiers))
			continue;
		//process sm
		JimpleBody jimpleBody = null;
		try {
			jimpleBody = (JimpleBody) sm.getBody(Jimple.v());
		} catch (RuntimeException re) {
			//this means the method is from jar files
			//re.printStackTrace();
			//System.out.println("There is no method body for method " + sm);
			log.error("There is no method body for method " + sm);
			continue;
		}
		StmtList stmtList = jimpleBody.getStmtList();
		for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
			Stmt stmt = (Stmt) stmtIt.next();
			Set invokedMds = getInvokedMdsFrom(stmt,sm);
			reachableMethods.addAll(invokedMds);
			for (Iterator mdIt = invokedMds.iterator(); mdIt.hasNext();) {
				SootMethod invokedMd = (SootMethod) mdIt.next();
				if (!visitedMds.contains(invokedMd) && !workList.contains(invokedMd))
					workList.addLast(invokedMd);
			}
		}
		SootClass sootClass = sm.getDeclaringClass();
		if (sootClass.declaresMethod("<clinit>")) {
			SootMethod classInitMd = sootClass.getMethod("<clinit>");
			if (!visitedMds.contains(classInitMd) && !workList.contains(classInitMd))
				workList.addLast(classInitMd);
			if (!reachableMethods.contains(classInitMd))
				reachableMethods.add(classInitMd);
		}
	}
}
/**
 * Dump a set of soot classes into files with provided extension.
 * <br> This method is only for internal debugging.
 * <p>
 * @param dumpedClasses an array of soot classes being dumped.
 * @param extension a string for file extension.
 */
private void dumpJimpleCode(SootClass dumpedClasses[], String extension) {
	StoredBody bd = new StoredBody(Jimple.v());
	String outputPath = ".";
	for (int i = 0; i < dumpedClasses.length; i++) {
		SootClass sc = dumpedClasses[i];
		String className = sc.getName();
		try {
			File jimpFile = new File(outputPath + File.separator + className + extension);
			FileOutputStream jimpOut = new FileOutputStream(jimpFile);
			sc.printTo(bd, new PrintWriter(jimpOut, true));
		} catch (IOException ex) {
			throw new RuntimeException("Could not dump jimple file (" + className + ")");
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-21 1:48:49)
 * @return ca.mcgill.sable.util.Set
 * @param invokeExpr ca.mcgill.sable.soot.jimple.InvokeExpr
 */
private Set getInvokedMdsFrom(InvokeExpr invokeExpr, SootMethod enclosingMethod) {
	Set invokedMds = new ArraySet();
	if (Slicer.Turn_On_BOFA && (invokeExpr instanceof NonStaticInvokeExpr)) {
		SootMethod invokedMethod = invokeExpr.getMethod();
		if (invokedMethod.getSignature().startsWith("java.") || invokedMethod.getSignature().startsWith("javax."))
			return invokedMds;
		Collection classVariants = Slicer.BOFA_Analysis.invokeExprResolution((NonStaticInvokeExpr) invokeExpr, enclosingMethod);
		for (Iterator classIt = classVariants.iterator(); classIt.hasNext();) {
			SootClass classVariant = (SootClass) classIt.next();
			invokedMds.add(classVariant.getMethod(invokedMethod.getName(), invokedMethod.getParameterTypes(), invokedMethod.getReturnType()));
		}
	} else {
		SootMethod invokedMd = invokeExpr.getMethod();
		if (invokedMd.getSignature().startsWith("java.") || invokedMd.getSignature().startsWith("javax."))
			return invokedMds;
		SootClass invokedClass = invokedMd.getDeclaringClass();
		invokedMds.add(invokedMd);
		if (interfaceImplementedByMap.containsKey(invokedClass)) {
			List paraTypes = invokedMd.getParameterTypes();
			Type returnType = invokedMd.getReturnType();
			String mdName = invokedMd.getName();
			Set subClasses = (Set) interfaceImplementedByMap.get(invokedClass);
			for (Iterator subIt = subClasses.iterator(); subIt.hasNext();) {
				SootClass subClass = (SootClass) subIt.next();
				if (subClass.declaresMethod(mdName, paraTypes, returnType))
					invokedMds.add(subClass.getMethod(mdName, paraTypes, returnType));
			}
		}
	}
	return invokedMds;
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-21 1:38:48)
 * @return ca.mcgill.sable.util.Set
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
private Set getInvokedMdsFrom(Stmt stmt, SootMethod enclosingMethod) {
	Set invokedMds = new ArraySet();
	if (SlicingMethod.isBanderaInvoke(stmt)) {
	} else
		if (stmt instanceof InvokeStmt) {
			InvokeExpr invokeExpr = (InvokeExpr) ((InvokeStmt) stmt).getInvokeExpr();
			Set invokedMdsByStmt = getInvokedMdsFrom(invokeExpr, enclosingMethod);
			invokedMds.addAll(invokedMdsByStmt);
		} else {
			List valueBoxes = stmt.getUseAndDefBoxes();
			for (Iterator vIt = valueBoxes.iterator(); vIt.hasNext();) {
				ValueBox vbox = (ValueBox) vIt.next();
				Value value = vbox.getValue();
				if (value instanceof InvokeExpr) {
					Set invokedMdsByValue = getInvokedMdsFrom((InvokeExpr) value, enclosingMethod);
					invokedMds.addAll(invokedMdsByValue);
				}
			}
		}
	return invokedMds;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 16:17:08)
 * @return edu.ksu.cis.bandera.pdgslicer.PostProcessOnAnnotation
 */
public PostProcessOnAnnotation getPostProcessOnAnnotation() {
	return postProcessOnAnnotation;
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-21 0:09:43)
 * @return ca.mcgill.sable.util.Set
 */
private Set getStartingMethods() {
	Set startingMds = new ArraySet();
	List paraList = new ArrayList();
	//"main" method
	Hashtable table = CompilationManager.getCompiledClasses();
	SootClass mainClass = CompilationManager.getMainSootClass();
	if (mainClass.declaresMethod("main"))
		startingMds.add(mainClass.getMethod("main"));
	else
		throw new edu.ksu.cis.bandera.pdgslicer.exceptions.SlicerException("can not find main method");
	//"run" methods

	for (java.util.Enumeration e = table.elements(); e.hasMoreElements();) {
		SootClass sc = (SootClass) e.nextElement();
		if (sc.declaresMethod("run", paraList)) {
			startingMds.add(sc.getMethod("run",paraList));
		}
		//if (sc.getName().equals("java.lang.Object") || (sc.getName().equals("Object")))
		if (isObjectClass(sc))
			startingMds.addAll(sc.getMethods());
	}

	// methods with slice criterion
	if (sliceInterestVec.size() == 0)
		return startingMds;
	for (java.util.Enumeration e = sliceInterestVec.elements(); e.hasMoreElements();) {
		SliceInterest sliceInterest = (SliceInterest) e.nextElement();
		if (sliceInterest instanceof SliceLocal) {
			SliceLocal sliceLocal = (SliceLocal) sliceInterest;
			startingMds.add(sliceLocal.getSootMethod());
		} else
			if (sliceInterest instanceof SlicePoint) {
				SlicePoint slicePoint = (SlicePoint) sliceInterest;
				startingMds.add(slicePoint.getSootMethod());
			}
			/*
			else
			if (sliceInterest instanceof SliceField) {
			SliceField sliceField = (SliceField) sliceInterest;
			SootClass fieldClass = sliceField.getSootClass();
			//should see if this field is defined in any method
			startingMds.addAll(fieldClass.getMethods());
			
			}
			*/
	}
	return startingMds;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-18 10:59:00)
 * @return boolean
 * @param sootClass ca.mcgill.sable.soot.SootClass
 */
private boolean haveStaticFields(SootClass sootClass) {
	for (Iterator fdIt = sootClass.getFields().iterator(); fdIt.hasNext();) {
		SootField sf = (SootField) fdIt.next();
		int modifiers = sf.getModifiers();
		if (!Modifier.isPrivate(modifiers) && Modifier.isStatic(modifiers))
			return true;
	}
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-26 20:25:41)
 * @return boolean
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
static boolean isMethodOfObject(SootMethod sm) {
	if (isObjectClass(sm.getDeclaringClass()))
		return true;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-31 9:27:42)
 * @return boolean
 * @param sc ca.mcgill.sable.soot.SootClass
 */
static boolean isObjectClass(SootClass sc) {
	if (sc.getName().equals("java.lang.Object") || sc.getName().equals("Object"))
		return true;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-10-31 16:28:43)
 * @return boolean
 * @param sootClass ca.mcgill.sable.soot.SootClass
 */
 static boolean isRemovableClass(SootClass sootClass, Set residualSootClasses) {
	if (Slicer.interfaceImplementedByMap.containsKey(sootClass)) {
		Set subClasses = (Set) Slicer.interfaceImplementedByMap.get(sootClass);
		for (Iterator subIt = subClasses.iterator(); subIt.hasNext();) {
			SootClass subClass = (SootClass) subIt.next();
			if (residualSootClasses.contains(subClass))
				return false;
		}
		return true;
	}
	return true;
}
/**
* Analyse program for building PDG:
* <br>
* {@link MethodCallAnalysis MethodCallAnalysis} --- construcing naive call graph,
* <br>{@link MethodCallAnalysis#MODREFAnalysis() MODREFAnalysis()} --- data flow for fields in classes,
* <br>{@link MethodCallAnalysis#buildMethodPDG(AnnotationManager) buildMethodPDG(cfanns)} --- control and data dependences,
* <br>{@link InterClassAnalysis InterClassAnalysis} --- other dependences.
*/
private void moreInformation() {
	//this step is to build callMeSiteMap

	//System.out.println("   (3) Constructing call graph .... ");
	log.debug("   (3) Constructing call graph .... ");
	MethodCallAnalysis methodCallAnalysis = new MethodCallAnalysis();
	//System.out.println("   (4) Data flow for fields in class....");
	log.debug("   (4) Data flow for fields in class....");
	methodCallAnalysis.MODREFAnalysis();
	//System.out.println("   (5) Calculating control- and data-dependece for each statement in method ....");
	log.debug("   (5) Calculating control- and data-dependece for each statement in method ....");
	methodCallAnalysis.buildMethodPDG(cfanns);

	//this step is to capture the ready dependence and interference 
	//dependence between classes

	if (classInfoList.size() > 1) {
	    //System.out.println("   (6) Inter-class analysis: synchronization, interference ....");
		log.debug("   (6) Inter-class analysis: synchronization, interference ....");
		InterClassAnalysis interClassAnalysis = new InterClassAnalysis(classInfoList);
	}
}
/**
* Dump slicing result into {@link #residualSootClassArray  residualSootClassArray}.
* <br> Print out summary information on the slicing result.
*/
private void outputResidualCls(boolean sliced) {
	//residualSootClassArray = new SootClass[classInfoList.size()];
	int classnum = 0;
	Set unRemovableClasses = new ArraySet();
	Set residualSootClassSet = new ArraySet();
	List slicedClass = new ArrayList();
	for (Iterator classIt = classInfoList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		slicedClass.add(classInfo.sootClass.getName());
		residualSootClassSet.add(classInfo.sootClass);
		//residualSootClassArray[classnum++] = classInfo.sootClass;
	}
	if (sliced) {
		List removedClass = new ArrayList();
		for (int i = 0; i < relevantClassArray.length; i++) {
			SootClass relClass = relevantClassArray[i];
			if (slicedClass.contains(relClass.getName())) {
				if (Slicer.unreachableClasses.contains(relClass))
					Slicer.unreachableClasses.remove(relClass);
				continue;
			}
			if (Slicer.unreachableClasses.contains(relClass)) {
			} else
				if (isRemovableClass(relClass, residualSootClassSet))
					removedClass.add(relClass.getName());
				else {
					residualSootClassSet.add(relClass);
					unRemovableClasses.add(relClass);
				}
		}
		residualSootClassArray = new SootClass[classInfoList.size() + unRemovableClasses.size()];
		for (Iterator classIt = classInfoList.iterator(); classIt.hasNext();) {
			ClassInfo classInfo = (ClassInfo) classIt.next();
			residualSootClassArray[classnum++] = classInfo.sootClass;
		}
		for (Iterator classIt = unRemovableClasses.iterator(); classIt.hasNext();) {
			residualSootClassArray[classnum++] = (SootClass) classIt.next();
		}
		if (classnum == 0)
			residualSootClassArray = null;
		//System.out.print("\nSuccessfully slicing " + classnum + " vs. " + classNum);
		//System.out.println(" classes: " + residualSootClassSet);
		log.info("\nSuccessfully slicing " + classnum + " vs. " + classNum +
			 " classes: " + residualSootClassSet);
		if (!removedClass.isEmpty()) {
		    if (removedClass.size() > 1) {
			//System.out.println(removedClass.size() + " classes are removed by slicer: " + removedClass);
			log.debug(removedClass.size() + " classes are removed by slicer: " + removedClass);
		    }
		    else {
			//System.out.println(removedClass.size() + " class is removed by slicer: " + removedClass);
			log.debug(removedClass.size() + " class is removed by slicer: " + removedClass);
		    }
		}
		if (!Slicer.unreachableClasses.isEmpty()) {
			if (Slicer.unreachableClasses.size() > 1) {
			    //System.out.println(Slicer.unreachableClasses.size() + " classes are unreachable: " + Slicer.unreachableClasses);
				log.debug(Slicer.unreachableClasses.size() + " classes are unreachable: " + Slicer.unreachableClasses);
			} else
			    //System.out.println(Slicer.unreachableClasses.size() + " class is unreachable: " + Slicer.unreachableClasses);
				log.debug(Slicer.unreachableClasses.size() + " class is unreachable: " + Slicer.unreachableClasses);
		}
		//System.out.println("\n" + slicedClass.size() + " classes are remained: " + slicedClass + "\n");
	} else {
		residualSootClassArray = new SootClass[classInfoList.size()];
		for (Iterator classIt = classInfoList.iterator(); classIt.hasNext();) {
			ClassInfo classInfo = (ClassInfo) classIt.next();
			residualSootClassArray[classnum++] = classInfo.sootClass;
		}
		//System.out.println("\nKeeping all " + classNum + " classes without slicing: " + slicedClass);
		log.debug("\nKeeping all " + classNum + " classes without slicing: " + slicedClass);
		//System.out.println("\n" + slicedClass.size() + " classes are remained: " + slicedClass + "\n");
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-5 10:09:36)
 */
private void postProcessing() {
    //System.out.println("\nConstructing residual program ......");
	log.debug("\nConstructing residual program ......");
	PostProcess postProcess = new PostProcess(classInfoList, cfanns);

	//modify sootclass according to the slice set
	postProcess.resClassCons();

	//Output of the residual classes

	outputResidualCls(true);
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-5 10:09:36)
 */
public void postProcessingOnAnnotation() {
    //System.out.println("Postprocessing ... ...");
	log.debug("Postprocessing ... ...");
	postProcessOnAnnotation = new PostProcessOnAnnotation(classInfoList, cfanns);
	postProcessOnAnnotation.resClassCons();
	//System.out.println("End of post-processing");
	log.debug("End of post-processing");
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-5 10:11:32)
 */
public boolean preProcessing(Vector sliceCriterionVector) {
	boolean emptyCriterion = false;
	//extract slicing criterion
	//System.out.println("\n   (2) Extracting slicing criterion ......");
	log.debug("\n   (2) Extracting slicing criterion ......");
	PreProcess preProcess = new PreProcess(classInfoList);
	if (sliceCriterionVector.size() == 0)
		preProcess.extractingForDL(relevantClassArray);
	else
		preProcess.extracting(sliceCriterionVector);
	if (preProcess.emptySliceCriterion()) {
	    //System.out.println("       *** There is no slicing criterion provided;");
	    //System.out.println("       *** And there is no slicing criterion for deadlock checking can be extracted.");
	    log.info("       *** There is no slicing criterion provided;" +
		     "       *** And there is no slicing criterion for deadlock checking can be extracted.");
		outputResidualCls(false);
		emptyCriterion = true;
		return emptyCriterion;
	}
	//System.out.println("\nSlicing criterion from extracting and grouping: \n");
	log.debug("\nSlicing criterion from extracting and grouping: \n");
	printCriterion();
	System.out.println("\n");
	emptyCriterion = false;
	return emptyCriterion;
}
/**
* Print out slicing criterion for each class and method.
*/
public void printCriterion() {
	for (Iterator classIt = classInfoList.iterator(); classIt.hasNext();) {
		ClassInfo classInfo = (ClassInfo) classIt.next();
		String className = classInfo.sootClass.getName();
		List methodList = classInfo.methodsInfoList;
		for (Iterator mdIt = methodList.iterator(); mdIt.hasNext();) {
			MethodInfo mdInfo = (MethodInfo) mdIt.next();
			String methodName = mdInfo.sootMethod.getName();
			if (mdInfo.sCriterion == null)
				continue;
			//System.out.print("Criterion of " + className + "." + methodName + " is: ");
			//System.out.println(mdInfo.sCriterion);
			log.debug("Criterion of " + className + "." + methodName + " is: " +
				  mdInfo.sCriterion);
			//System.out.println();
		}
	}
}
/**
* Print out Jimple code of a soot class.
* <br> This method is only for internal debugging.
* <p>
* @param sClass a soot class being printed.
*/
static void printJimpleClass(SootClass sClass) {
	// Print Jimple class
	PrintWriter out = new PrintWriter(System.out, true);
	sClass.printTo(new StoredBody(Jimple.v()), out);
}
/**
* Get result of slicing as an array of {@link SootClass SootClass}.
*/
public SootClass[] result() {
	return residualSootClassArray;
}
/**
* Start the slicer after creating an object of {@link Slicer Slicer}.
* <br>
* Four steps are included in this method:
* <ul>
   * <li> {@link #basicInformation() basicInformation} --- Collect some basic information of program;
   * <li> {@link PreProcess PreProcess} --- Determine slicing criterion;
   * <li> {@link #moreInformation() moreInformation()} --- Build program dependency graph for each method in the program;
   * <li> {@link #slicing() slicing()} --- Slice each method in the program,
   * construct residual program, and dump residual program.
   * </ul>
*/
public void run() throws Exception {
			//System.out.println("method variant manager key set 2: " + edu.ksu.cis.bandera.bofa.MethodVariantManager.methodMap);
	try {
		if (Slicer.Turn_On_BOFA) {
			edu.ksu.cis.bandera.bofa.BOFA.reset();
			edu.ksu.cis.bandera.bofa.BOFA.analyze();
			BOFA_Analysis = edu.ksu.cis.bandera.bofa.Analysis.init();
		}
		//dumpJimpleCode(relevantClassArray, ".originalBUI2.jimple");
		//System.out.println("\nCollecting reachable methods ......");
		log.debug("\nCollecting reachable methods ......");
		//System.out.println("method variant manager key set: " + edu.ksu.cis.bandera.bofa.MethodVariantManager.methodMap.keySet());
		buildInterfaceAndSuperClassMap();
		collectReachableMethods();
		//System.out.println("\nBuilding Program Dependence Graph ......");
		log.debug("\nBuilding Program Dependence Graph ......");
		//System.out.println("\n   (1) Collecting information for each method ......");
		log.debug("\n   (1) Collecting information for each method ......");
		basicInformation(); //with classes
		//extract slicing criterion
		boolean emptyCriterion = preProcessing(sliceInterestVec);
		if (emptyCriterion)
			return;
		collectOriginalMethods();
		moreInformation(); //with cfanns
		slicing(); //wiht cfanns
		//dumpJimpleCode(residualSootClassArray, ".slicedBUI2.jimple");
		postProcessing();
	} catch (Exception eee) {
	    //System.out.println("There is exception in slicer");
		log.error("There is exception in slicer", eee);
		//eee.printStackTrace();
		throw eee;
	}
}
/**
* Slice each method need to be sliced ---
* {@link #slicingMethods() slicingMethods()};
* <br>Postprocess for slicing --- {@link PostProcess PostProcess}.
* <br>Dump slicing result --- {@link #outputResidualCls(boolean) outputResidualCls}.
*/
public void slicing() {
	//for concurrent program, slicing criterion may relate to several class
	//preProcess.extracting() will extract slicing criterion from one or more
	//specification items, and distribute the slicing criterion into each 
	//relative class and method. Then store these slicing criterion flap to 
	//corresponding class and method information class
	//System.out.println("\nSlicing .........\n");
	log.debug("\nSlicing .........\n");
	slicingMethods(); //with Slicer.sootMethodInfoMap
	//slice set for every method of every class is in MethodInfo class
	//slicing.getSliceSets() will return a new class list where the
	//slice sets are not null
}
/**
 * Slice each method with {@link SlicingMethod SlicingMethod}.
 * <p>
 * The process of slicing is fix point reaching process:
 * <br> (UP U DOWN)*( slicing criterion)
 * <br> where UP and DOWN is constraint call graph driven up and down.
 */
private void slicingMethods() {
	for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		SootMethod sootMethod = (SootMethod) mdIt.next();
		if (isMethodOfObject(sootMethod)) continue;
		MethodInfo mdInfo = (MethodInfo) sootMethodInfoMap.get(sootMethod);
		SlicingMethod smd = new SlicingMethod(mdInfo);
		smd.slicingMethod(true);
	}

	//Slicing again by the incremental slicing criterion

	while (SlicingMethod.criterionChanged) {
		SlicingMethod.criterionChanged = false; 
		for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
			SootMethod sootMethod = (SootMethod) mdIt.next();
			if (isMethodOfObject(sootMethod)) continue;
			MethodInfo mdInfo = (MethodInfo) sootMethodInfoMap.get(sootMethod);
			if (mdInfo.increCriterion != null) {
				SlicingMethod smd = new SlicingMethod(mdInfo);
				smd.slicingMethodAgain();
			}
		}
	}
}
}
