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

// Build pDG within the body of one method

import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.pdgslicer.exceptions.*;
import edu.ksu.cis.bandera.annotation.*;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Vector;
//import java.util.HashSet;

/**
 * A class calculating data-dependence, control-dependence for one method.
 * <br>
 * This class includes analysis like reaching definitions for local variables and fields, 
 * postdominators, immediate postdominators, and control-dependence.
 */
public class BuildPDG {
	/**
	 * a list of exit nodes (index) in the method which are stored as BitSet.
	 */
	private BitSet exitNodes;
	MethodInfo methodInfo;
	/**
	* a map from {@link Stmt Stmt} to {@link BitSet BitSet} for postdominators
	* of each statement in the method.
	*/
	private Map stmtToPostdoms;
	/**
	* a map from {@link Stmt Stmt} to {@link Integer Integer} for immediate
	* postdominator of each statement in the method.
	*/
	private Map stmtToImmedPostdom;
	/**
	* statement list of the method.
	*/
	private StmtList stmtList;
	private StmtGraph stmtGraph;
	private JimpleBody jimpleBody;
	/**
	* the result of reaching definition analysis for static field references.
	*/
	private BitSet staticReachDef[];
	/**
	* the result of reaching definition analysis for instance field references.
	*/
	private BitSet instanceReachDef[];
	/**
	* an instance of {@link LockAnalysis LockAnalysis}.
	*/
	private LockAnalysis lockAnalysis = null;
	/**
	* a map from {@link Stmt Stmt} to a {@link List List} of
	* {@link ReadyDependStmt ReadyDependStmt} for ready dependence of
	* each statement in the method.
	*/
	private Map readyDependMap = null;
	/**
	* a map from {@link Stmt Stmt} to a {@link List List} of
	* {@link InterferStmt InterferStmt} for interference dependence of
	* each statement in the method.
	*/
	private Map interferenceMap = null;
	/**
	* a map from {@link CallSite CallSite} to {@link SootMethod SootMethod}
	* where SootMethod is called in the CallSite.
	*/
	private Map callSiteMap = null;
	/**
	* exit nodes without <code>throw</code> statements.
	*/
	private BitSet exitNodesNoThrow;
	/** 
	* a set of {@link DataBox DataBox}.
	*/
	private Set instanceFieldDefStmtList = new ArraySet(); //instance field ref
	/** 
	* a set of {@link DataBox DataBox}.
	*/	
	private Set staticFieldDefStmtList = null;
	/**
	   * The map of data dependence: {@link Stmt Stmt} to 
	   * a {@link ca.mcgill.sable.util.Set Set} of 
	   * {@link DataBox DataBox}.
	   */
	private Map stmtToDdOn;

	private Integer indefiniteNode = null;

	/******************************************************************/
	/**
	* The map of control dependence: {@link Stmt Stmt} to {@link BitSet BitSet}.
	*/
	private Map stmtToCdOn;
	/**
	* The map of divergence dependence from pre-divergence point to reachable statement set (from that point):
	* (@link Stmt Stmt} to {@link BitSet BitSet}.
	*/
	private Map divergenceMap;
	private AnnotationManager annotationManager = null;
  /**
   * Build dependency graph for one method in three steps:
   * <ul>
   * <li> Initialization
   * <li> Dominators calculation
   * <li> Dependencies calculation
   * </ul>
   * <p>
   * @param mdInfo   method to be analysed.
   * @param cfanns   annotation manager.
   */
  public BuildPDG(MethodInfo mdInfo, AnnotationManager cfanns) {
	annotationManager = cfanns;
	prepareToBuild(mdInfo);
	domination();
	dependency();
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-1 16:22:46)
 * This is for control analysis of init method. We assume that return
 * statement of init method control dependent on init invoke of its super class.
 * For example,
 *    public void init(int)
 *   {
 *       int b, size;
 *       Buffer JJJCTEMP$0;
 *       Object[] JJJCTEMP$1;
 *
 *       JJJCTEMP$0 := @this;
 *       b := @parameter0;
 *       specialinvoke JJJCTEMP$0.[java.lang.Object.init():void]();
 *       JJJCTEMP$1 = new Object[b];
 *       JJJCTEMP$0.[Buffer.array:Object[]] = JJJCTEMP$1;
 *       JJJCTEMP$0.[Buffer.putPtr:int] = 0;
 *       JJJCTEMP$0.[Buffer.getPtr:int] = 0;
 *       JJJCTEMP$0.[Buffer.usedSlots:int] = 0;
 *       return;
 *   }
 * Then, the return is control dependent on
 * specialinvoke JJJCTEMP$0.[java.lang.Object.init():void]();
 */
private void cdAnalysisForReturnOfInit() {
	BitSet specialInvokeSet = new BitSet(stmtList.size());
	for (Iterator si = methodInfo.indexMaps.getSpecialInvokeList().iterator(); si.hasNext();) {
		InvokeStmt invoke = (InvokeStmt) si.next();
		SpecialInvokeExpr specInvoke = (SpecialInvokeExpr) invoke.getInvokeExpr();
		if (methodInfo.indexMaps.getThisRefLocal().equals(specInvoke.getBase())) {
			if (specInvoke.getMethod().getName().equals("<init>")) {
				if (methodInfo.sootClass.getSuperClass().equals(specInvoke.getMethod().getDeclaringClass())) {
					int specialInvokeInt = stmtList.indexOf(invoke);
					specialInvokeSet.set(specialInvokeInt);
				} else
					if (methodInfo.sootClass.equals(specInvoke.getMethod().getDeclaringClass())) {
						int specialInvokeInt = stmtList.indexOf(invoke);
						specialInvokeSet.set(specialInvokeInt);
					}
			}
		}
	}
	if (SetUtil.emptyBitSet(specialInvokeSet))
		throw new SlicerException("Can not find special invoke of super class in method " + methodInfo.sootMethod);
	for (int i = 0; i < exitNodesNoThrow.size(); i++) {
		if (exitNodesNoThrow.get(i)) {
			Stmt exitStmt = (Stmt) stmtList.get(i);
			BitSet cdSet = (BitSet) stmtToCdOn.get(exitStmt);
			if (cdSet == null)
				cdSet = new BitSet(stmtList.size());
			cdSet.or(specialInvokeSet);
			stmtToCdOn.put(exitStmt, cdSet);
		}
	}
}
/**
 * Analyse control dependence for statements in <code>catch</code> block.
 * <br>
 * Store the result in {@link #stmtToCdOn stmtToCdOn}.
 * <p>
 * Given no any information on exception flow, all statements in <code>catch</code> 
 * block are considered control dependent on all statements in <code>try</code> block.
 */
private void cdAnalysisForStmtsInCatch() {

	Annotation annForSm = annotationManager.getAnnotation(methodInfo.sootClass, methodInfo.sootMethod);

	Vector currentCFANNs = annForSm.getAllAnnotations(true);

	for (Enumeration e = currentCFANNs.elements(); e.hasMoreElements();) {
		Annotation cfann = (Annotation) e.nextElement();
		
		if (cfann instanceof TryStmtAnnotation) {
			TryStmtAnnotation tryAnnotation = (TryStmtAnnotation) cfann;
			Annotation blockAnn = tryAnnotation.getBlockAnnotation();
			Stmt stmtsInBlock[] = blockAnn.getStatements();
			BitSet stmtsInTryBlock = SetUtil.stmtArrayToBitSet(stmtsInBlock, stmtList);
			Vector catchClauses = tryAnnotation.getCatchClauses();
			
			for (Enumeration en = catchClauses.elements(); en.hasMoreElements();) {
				CatchAnnotation catchAnn = (CatchAnnotation) en.nextElement();
				Stmt stmtsInCatch[] = catchAnn.getStatements();
				for (int i = 0; i < stmtsInCatch.length; i++) {
					Stmt branchStmt = stmtsInCatch[i];
					BitSet cdSet = (BitSet) stmtToCdOn.get(branchStmt);
					if (cdSet == null)
						cdSet = new BitSet(stmtList.size());
					cdSet.or(stmtsInTryBlock);
					stmtToCdOn.put(branchStmt, cdSet);
				}
			}
			
		}
		
	}
}
  /**
   * Clone and change base value for a set of instance field references.
   * <br>
   * For example, <code>a.f</code> can be cloned and changed its base value to a new value, 
   * saying <code>b</code>. Then <code>a.f</code> will be transformed to a new instance 
   * field reference <code>b.f</code>.
   * <p>
   * @param original   a set of {@link ca.mcgill.sable.soot.jimple.InstanceFieldRef InstanceFieldRef}
   * need changing their base values.
   * @param base       new base value for those references in <code>original</code>.
   * @return           a set of new 
   * {@link ca.mcgill.sable.soot.jimple.InstanceFieldRef InstanceFieldRef} with the new base
   *  value <code>base</code>.
   * Empty set is returned, if <code>original</code> is empty.
   */
static Set cloneAndChangeBase(Set original, Value base) {
	Set newFields = new ArraySet();
	for (Iterator orIt = original.iterator(); orIt.hasNext();) {
		InstanceFieldRef insFieldRef = (InstanceFieldRef) orIt.next();
		InstanceFieldRef newInsFieldRef = Jimple.v().newInstanceFieldRef(base, insFieldRef.getField());
		newFields.add(newInsFieldRef);
	}
	return newFields;
}
  /**
   * Collect all statements such that they assign values to instance field references, 
   * including all indirect assignments by method calls. 
   * <br>Store the result in {@link #instanceFieldDefStmtList instanceFieldDefStmtList}.
   */
private void collectInstanceFieldDefStmt() {
	/*
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Set defInstanceRefs = new ArraySet();
		Stmt stmt = (Stmt) stmtIt.next();
		for (Iterator defBoxIt = stmt.getDefBoxes().iterator(); defBoxIt.hasNext();) {
			Value value = ((ValueBox) defBoxIt.next()).getValue();
			if (value instanceof InstanceFieldRef)
				defInstanceRefs.add((InstanceFieldRef) value);
		}
		if (defInstanceRefs.size() != 0)
			instanceFieldDefStmtList.add(new DataBox(stmt, defInstanceRefs));
	}
	*/
	Fields MOD = methodInfo.MOD;
	Set paraFields = MOD.paraFields;
	Set instanceFields = MOD.instanceFields;
	Set otherInsFields = MOD.otherInsFds;
	instanceFieldDefStmtList.addAll(instanceFields);
	instanceFieldDefStmtList.addAll(otherInsFields);
	
	//begin to add instance references which are in called method
	//should collect all the instance reference caused from the parameter
	//passing and object passing
	//using binding function
/*
	for (Iterator siteIt = callSiteMap.keySet().iterator(); siteIt.hasNext();) {
		CallSite site = (CallSite) siteIt.next();
		SootMethod calledMd = (SootMethod) callSiteMap.get(site);
		MethodInfo calledMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(calledMd);
		if (calledMdInfo == null) continue;
		Fields fields = calledMdInfo.MOD;
		Set defInstanceRefs = new ArraySet();

		//it is also necessary to decide what if the call is the this
		//reference call, because of the object reference will be different
		//so it's not a simple union of instanceFields


		Set instanceFieldsInCalledMd = fields.instanceFields;
		Value base = null;
		NonStaticInvokeExpr nonStaticInvokeExpr = null;
		InvokeExpr invokeExpr = site.invokeExpr;
		if (invokeExpr instanceof NonStaticInvokeExpr) {
			nonStaticInvokeExpr = (NonStaticInvokeExpr) invokeExpr;
			base = nonStaticInvokeExpr.getBase();
		} else {
				//System.out.println("We can't process static invoke at this moment");
			return;
		}
		for (Iterator i =instanceFieldsInCalledMd.iterator(); i.hasNext();) {
			DataBox insFieldRefBox = (DataBox) i.next();
			Set defFields = insFieldRefBox.getInterferVars();
			defInstanceRefs.addAll(cloneAndChangeBase(defFields, base));
		}


		//including that paraFields of the other method with the name
		//changed to call method's parameters
		//need to construct the binding function of parameters

		if (paraFields != null) {
			int siteIndex = stmtList.indexOf(site.callStmt);
			for (Iterator kk = paraFields.iterator(); kk.hasNext();) {
				DataBox paraFieldRefBox = (DataBox) kk.next();
				Stmt callSite = paraFieldRefBox.getInterferStmt();
				int callSiteIndex = stmtList.indexOf(callSite);
				if (siteIndex == callSiteIndex)
					defInstanceRefs.addAll(paraFieldRefBox.getInterferVars());
			}
		}
		if (defInstanceRefs.size() != 0)
			instanceFieldDefStmtList.add(new DataBox(site.callStmt, defInstanceRefs));
	}
	*/
}
  /**
   * Collect all static field references ever defined in the method.
   * <p>
   * @return a set of {@link ca.mcgill.sable.soot.jimple.StaticFieldRef StaticFieldRef}.
   * Empty set is returned, if there is no static field reference defined.
   * @see #staticFieldDefStmtList
   */
private Set collectVarsDefined(Set defStmtList) {
	Set varsDefined = new ArraySet();
	for (Iterator i = defStmtList.iterator(); i.hasNext();) {
		DataBox db = (DataBox) i.next();
		Set vars = db.getInterferVars();
		varsDefined.addAll(vars);
	}
	return varsDefined;
}
  /**
   * Compute immediate (post)dominator for each statement in the method.
   * <br>
   * The alorithm of this method is from the book "Advanced compiler design and implementation"
   * by Steven S. Muchnick, 1997
   * <p>
   * @param stmtToBitSet  a map of {@link ca.mcgill.sable.soot.jimple.Stmt Stmt}
   * to {@link java.util.BitSet BitSet} of (post)dominators.
   * @return  a map of {@link ca.mcgill.sable.soot.jimple.Stmt Stmt} to 
   * {@link java.lang.Integer Integer} of immediate (post)dominator.
   * @throws DomOrPostdomNotUniqueException 
   * if there are more than one immediate (post)dominators.
   */
private Map computeImmedDom(Map stmtToBitSet) {
	Map stmtToImmediate = new HashMap(stmtList.size() * 2 + 1, 0.7f);
	Iterator keyStmtIt = stmtToBitSet.keySet().iterator();
	while (keyStmtIt.hasNext()) {
		Stmt keyStmt = (Stmt) keyStmtIt.next();
		int keyStmtIndex = stmtList.indexOf(keyStmt);
		BitSet doms = (BitSet) stmtToBitSet.get(keyStmt);
		BitSet workSet = (BitSet) doms.clone();
		workSet.clear(keyStmtIndex);

		//BitSet immediate = new BitSet(stmtList.size());
		BitSet immediate = (BitSet) workSet.clone();
		for (int i = 0; i < workSet.size(); i++) {
			if (workSet.get(i)) {
				BitSet domX = (BitSet) stmtToBitSet.get((Stmt) stmtList.get(i));
				BitSet domX2 = (BitSet) domX.clone();
				domX2.clear(i);
				domX2.and(immediate);
				immediate.xor(domX2);
			}
		}
		int immT = 0;
		for (int i = 0; i < immediate.size(); i++) {
			if (immediate.get(i)) {
				stmtToImmediate.put(keyStmt, new Integer(i));
				immT++;
			}
		}
		if (immT == 0)
			stmtToImmediate.put(keyStmt, IndexMaps.EntryNode);
		else
			if (immT > 1)
				throw new DomOrPostdomNotUniqueException("immediate dominator and postdominator should be unique or none for statement: " + keyStmt);
	}
	return stmtToImmediate;
}
/**
 * Analyse control dependence for each statement in the method
 * <br>
 * Store the result in {@link #stmtToCdOn stmtToCdOn}.
 * <p>
 * The algorithm of this method is from the compiler news group:
 * <p>
 * From {@link <a href="http://compilers.iecc.com/comparch/article/92-12-065" target="_top">
 * comp.compilers</a>}.
 * <br> === Begin Algorithm ===
 * <p>
 * 1. For a given flow node X, let P(X) denote the nodes that post-dominate
 * X, along with X itself. Compute P(X) by using any of the standard
 * techniques to solve the following backward data flow equations:
 * P(X) = X | (P(Y1) & ... & P(Yn)),
 * P(Stop) = {}
 * where Y1 ... Yn are the successors of X, "|" denotes set union, "&"
 * denotes set intersection, and Stop is a unique exit node for the flow
 * graph. (This part is pretty much straight out of the red dragon book.)
 * <p>
 * 2. Assume that each node in the flow graph has no more than 2 successors,
 * and that if a node does have 2 successors, one is the "true" successor and
 * the other the "false" successor. For a given flow node X, let CD(X)
 * denote the nodes that are control dependent on X. If X has fewer than 2
 * successors, CD(X) is empty. Otherwise, we can find CD(X) as follows:
 * CD(X) = P(X-true) ^ P(X-false)
 * where X-true and X-false are respectively the true and false successors,
 * and "^" denotes symmetric difference (i.e., A ^ B = (A | B) - (A & B)).
 * Furthermore, the edges that should be labelled "true" in the control
 * dependence graph are the ones from X to the nodes in CD(X) & P(X-true);
 * and "false", the ones to the nodes in CD(X) & P(X-false).
 * <p>
 * === End Algorithm ===
 * <p>
 * From {@link <a href="http://compilers.iecc.com/comparch/article/92-12-068" target="_top">
 * comp.compilers</a>}.
 * <br> It looks like the correct solution for a node X with 3 successors, say A,
 * B, and C, would be
 * <p>
 * CD(X) = (P(A) ^ (P(B) | P(C)))
 * | (P(B) ^ (P(A) | P(C)))
 * | (P(C) ^ (P(A) | P(B)))
 * <p>
 * From {@link <a href="http://compilers.iecc.com/comparch/article/92-12-070" target="_top">
 * comp.compilers</a>}.
 * <br> The general case of more than 2 succesoors is handled like this...
 * <br> CD(X) = union(P(S)) - intersection(P(S)), for S in successors(X)
 * <br> (This is a much simpler version of another answer I posted earlier)
 * <p>
 * From {@link <a href="http://compilers.iecc.com/comparch/article/92-12-079" target="_top">
 * comp.compilers</a>}.
 * <br> This can be simplified (also with less computation) further:
 * <br> CD(X) = union(P(S)) - P(X)
 * <br> I think it is not difficut to prove that
 * <br> {intersection(P(S)), for S in successors(X)}=P(X)
 */
private void controlDependAnalysis() {
	stmtToCdOn = new HashMap();
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		List succs = removeExceptionCaught(stmtGraph.getSuccsOf(stmt));
		if (succs.size() < 2)
			continue;
		/*
		if (succs.size() > 2) {
		System.out.println("There are more than 2 successors for stmt: " + stmt);
		System.out.println("Successors: " + succs);
		System.out.println("We do not deal with more than 2 successors");
		continue;
		}
		*/
		//Map CDXMap = new HashMap();
		Stmt xSuccStmt = (Stmt) succs.get(0);
		BitSet PsUnion = (BitSet) postdominatorsOf(xSuccStmt).clone();
		for (int i = 1; i < succs.size(); i++) {
			xSuccStmt = (Stmt) succs.get(i);
			BitSet Ps = (BitSet) postdominatorsOf(xSuccStmt).clone();
			PsUnion.or(Ps);
		}
		BitSet Px = (BitSet) postdominatorsOf(stmt).clone();
		PsUnion.xor(Px);
		//CDXMap.put(stmt, PsUnion); //stmt controls the stmts in Pxtrue;
		// put Pxtrue to stmtToCdOn
		for (int i = 0; i < PsUnion.size(); i++) {
			if (!PsUnion.get(i))
				continue;
			int cdStmtIndex = stmtList.indexOf(stmt);
			Stmt branchStmt = (Stmt) stmtList.get(i);
			BitSet cdSet = (BitSet) stmtToCdOn.get(branchStmt);
			if (cdSet == null)
				cdSet = new BitSet(stmtList.size());
			cdSet.set(cdStmtIndex);
			stmtToCdOn.put(branchStmt, cdSet);
		}
	}
	//control analysis for stmts in catch: all stmts in catch control depend on
	//all stmts in try block.
	cdAnalysisForStmtsInCatch();
	if (methodInfo.sootMethod.getName().equals("<init>"))
		cdAnalysisForReturnOfInit();
}
  /**
   * Get control dependent predecessors, from {@link #stmtToCdOn stmtToCdOn}, 
   * for the statement <code>st</code>.
   * <p>
   * @param st query statement.
   * @return a set of statements represented in indexes with {@link java.util.BitSet BitSet}.
   */ 
  public BitSet controlNodesOf(Stmt st)
	{
	  return (BitSet) stmtToCdOn.get(st);
	}
  /**
   * Get control dependent successors for the statement <code>stmt</code>.
   * <p>
   * @param stmt query statement.
   * @return a set of {@link ca.mcgill.sable.soot.jimple.Stmt} that are 
   * control dependent on <code>stmt</code>. Emtpy set is returned if there is no 
   * control dependent successor.
   */
public Set controlSuccNodesOf(Stmt stmt) {
	Set succNodes = new ArraySet();
	int index = stmtList.indexOf(stmt);
	for (Iterator stmtIt = stmtToCdOn.keySet().iterator(); stmtIt.hasNext();) {
		Stmt controlledStmt = (Stmt) stmtIt.next();
		BitSet controlNodes = (BitSet) stmtToCdOn.get(controlledStmt);
		if (controlNodes.get(index))
			succNodes.add(controlledStmt);
	}
	return succNodes;
}
  /**
   * See if there is a DataBox in <code>dataBoxes</code> with a statement <code>stmt</code>.
   * <p>
   * @param dataBoxes   a set of {@link edu.ksu.cis.bandera.pdgslicer.DataBox DataBox}.
   * @param stmt        query statement.
   * @return <code>true</code> is returned, if contains. 
   * <code>false</code> is returned, if not.
   */
private boolean dataBoxesContains(Set dataBoxes, Stmt stmt)
	{
	  for (Iterator boxIt = dataBoxes.iterator(); boxIt.hasNext();)
	{
	  DataBox db = (DataBox) boxIt.next();
	  if (stmt==db.getStmt()) return true;
	}
	  return false;
	}
/*************************************************************/
/**
   * Analyse data dependence for each statement in the method. Analysis includes data
   * data dependence of instance field references, data dependence of static or static based 
   * field references, and data dependence of parameters for method invokes.
   * <br>
   * Store the result in {@link #stmtToDdOn stmtToDdOn}.
   * <p>
   * @see #ddForStaticAndInstanceFd()
   * @see #ddForParameters()
   */
private void dataDependAnalysis() {
	stmtToDdOn = new HashMap(stmtList.size() * 2 + 1, 0.7f);
	LocalDefs localDefs = new SimpleLocalDefs((CompleteStmtGraph) stmtGraph);
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		Set ddList = new ArraySet();
		for (Iterator useBoxIt = stmt.getUseBoxes().iterator(); useBoxIt.hasNext();) {
			ValueBox useBox = (ValueBox) useBoxIt.next();
			if (useBox.getValue() instanceof Local) {
				Local local = (Local) useBox.getValue();
				List defsOfUse = localDefs.getDefsOfAt(local, stmt);

				//Need to be refined on this 

				for (Iterator defsIt = defsOfUse.iterator(); defsIt.hasNext();) {
					Stmt def = (Stmt) defsIt.next();
					Set tempSet = new ArraySet();
					tempSet.add(local);
					DataBox ddBox = new DataBox(def, tempSet);
					ddList.add(ddBox);
				}
				//if local is Arraytype, including all assignments to the component of this array
				//which can reach to stmt into the data depenendece list
				if (local.getType() instanceof ArrayType)
					ddForArray(ddList, stmt, local);
			} else
				if (useBox.getValue() instanceof InstanceFieldRef) {
					dataDependOfInstanceFieldRef((InstanceFieldRef) useBox.getValue(), stmt, ddList);
				}
		} //end of while

		stmtToDdOn.put(stmt, ddList);
	} //end of while

	//add data dependence for static fields and public class instance fields
	ddForStaticAndInstanceFd();

	//add data dependence for parafields of every callsite
	ddForParameters();
}
/**
   * Get data dependences for one instance field reference <code>insfr</code>
   * using information on reaching definition of instance field references
   * {@link #instanceReachDef intanceReachDef} 
   * and {@link #instanceFieldDefStmtList instanceFieldDefStmtList}.
   * <br> 
   * Store the result in <code>ddList</code>.
   * <p>
   * @param insfr  query field reference.
   * @param stmt   the statement where <code>insfr</code> is used.
   * @param ddList a set of {@link edu.ksu.cis.bandera.pdgslicer.DataBox DataBox}.
   */
private void dataDependOfInstanceFieldRef(InstanceFieldRef insfr, Stmt stmt, Set ddList) {
	Set instanceVarsDefined = collectVarsDefined(instanceFieldDefStmtList);
	Set fieldsDefined = fieldRefToSootField(instanceVarsDefined);
	if (fieldsDefined.contains(insfr.getField())) {
		// reaching definition procedrure of instancefield ref
		//BitSet reachIni = instanceReachDef[stmtList.indexOf(stmt)];
		Object array[] = instanceFieldDefStmtList.toArray();
		for (int mm = 0; mm < array.length; mm++) {
			//if (reachIni.get(mm)) 
			{
				DataBox db2 = (DataBox) array[mm];
				Set vars = db2.getInterferVars();
				Set fields = fieldRefToSootField(vars);
				if (fields.contains(insfr.getField())) {
					Set fieldReferences = getRefsWithCommonField(vars,insfr.getField());
					Stmt defStmt = db2.getInterferStmt();
					if (defStmt.equals(stmt)) {
						if (SlicingMethod.isCallSite(callSiteMap, stmt) != null) {
						} else {
							//integrate path information here
							/*
							 * FIX HERE:
							 * The following statement was commented and the next one introduced.  The reason being that for
							 * fields using simple local control flow graph is insufficient.
							 */
							//if (SlicingMethod.reachableStmtFrom(defStmt, stmtGraph).contains(stmt)) {
							if (true) {
								//integrate never point-to information
								if (BuildPDG.mayPointToTheSameRef(insfr.getBase(), fieldReferences, methodInfo.sootMethod, methodInfo.sootMethod)) {
									DataBox ddBox = new DataBox(defStmt, vars);
									ddList.add(ddBox);
								}
							}
						}
					} else {
						//integrate path information here
						/*
						 * FIX HERE:
						 * The following statement was commented and the next one introduced.  The reason being that for
						 * fields using simple local control flow graph is insufficient.
						 */
						//if (SlicingMethod.reachableStmtFrom(defStmt, stmtGraph).contains(stmt)) {
						if (true) {
							//integrate never point-to information
							if (BuildPDG.mayPointToTheSameRef(insfr.getBase(), fieldReferences, methodInfo.sootMethod, methodInfo.sootMethod)) {
								DataBox ddBox = new DataBox(defStmt, vars);
								ddList.add(ddBox);
							}
						}
					}
				} // end of if
			} //end of if
		} //end of for

	} //end of if 
}
  /**
   * Get data dependences for one static field reference <code>staticVariable</code>
   * using information on reaching definition of static field references
   * {@link #staticReachDef staticReachDef}
   * and {@link #staticFieldDefStmtList staticFieldDefStmtList}.
   * <br> 
   * Store the result in <code>ddList</code>.
   * <p>
   * @param staticVariable  query field reference.
   * @param stmt   the statement where <code>staticVariable</code> is used.
   * @param ddList a set of {@link edu.ksu.cis.bandera.pdgslicer.DataBox DataBox}.
   */
private void dataDependOfStaticFieldRef(StaticFieldRef staticVariable, Stmt stmt, Set ddList) {
	Set staticVarsDefined = collectVarsDefined(staticFieldDefStmtList);
	if (staticVarsDefined.contains(staticVariable)) {
		// reaching definition procedrure of staticfield ref
		BitSet reachIni = staticReachDef[stmtList.indexOf(stmt)];
		Object array[] = staticFieldDefStmtList.toArray();
		for (int mm = 0; mm < array.length; mm++) {
			if (reachIni.get(mm)) {
				DataBox db2 = (DataBox) array[mm];
				Set vars = db2.getInterferVars();
				if (vars.contains(staticVariable)) {
					Stmt defStmt = db2.getInterferStmt();
					if (defStmt.equals(stmt)) {
						if (SlicingMethod.isCallSite(callSiteMap, stmt) != null) {
						} else {
							DataBox ddBox = new DataBox(defStmt, vars);
							ddList.add(ddBox);
						}
					} else {
						DataBox ddBox = new DataBox(defStmt, vars);
						ddList.add(ddBox);
					}
				} // end of if
			} //end of if
		} //end of for

	} //end of if 
}
  /** 
   * Get data dependent predecessors, from {@link #stmtToDdOn stmtToDdOn}
   * for the statement <code>st</code>.
   * <p>
   * @param st   query statement.
   * @return     a set of {@link edu.ksu.cis.bandera.pdgslicer.DataBox DataBox}.
   */
  public Set dataNodesOf(Stmt st)
	{
	  return (Set) stmtToDdOn.get(st);
	}
  /** 
   * Get data dependent successors
   * for the statement <code>st</code>.
   * <p>
   * @param stmt   query statement.
   * @return     a set of {@link ca.mcgill.sable.soot.jimple.Stmt Stmt}.
   */
  public Set dataSuccNodesOf(Stmt stmt)
	{
	  Set succNodes = new ArraySet();
	  for (Iterator stmtIt = stmtToDdOn.keySet().iterator(); stmtIt.hasNext();)
	{
	  Stmt reachedStmt = (Stmt) stmtIt.next();
	  Set dataBoxes = (Set) stmtToDdOn.get(reachedStmt);
	  if (dataBoxesContains(dataBoxes, stmt))
	    succNodes.add(reachedStmt);
	}
	  return succNodes;
	}
/**
 * Insert the method's description here.
 * Creation date: (7/10/2001 1:52:43 PM)
 * @param ddList ca.mcgill.sable.util.Set
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
private void ddForArray(Set ddList, Stmt stmt, Local arrayLocal) {
	//find all assignments to ArrayRef
	Map arrayRefs = new HashMap();
	for (Iterator stmtIt = stmtList.iterator(); stmtIt.hasNext();) {
		Stmt s = (Stmt) stmtIt.next();
		List defList = s.getDefBoxes();
		for (Iterator defBoxIt = defList.iterator(); defBoxIt.hasNext();) {
			ValueBox defBox = (ValueBox) defBoxIt.next();
			Value defValue = defBox.getValue();
			if (defValue instanceof ArrayRef) {
				//Value baseValue  = ((ArrayRef) defValue).getBase();
				arrayRefs.put(defValue, s);
			}
		}
	}
	//see if any base of arrayRef can point to the same reference as arrayLocal, and can
	//reachable from the definition to stmt
	for (Iterator keyIt = arrayRefs.keySet().iterator(); keyIt.hasNext();) {
		ArrayRef defValue = (ArrayRef) keyIt.next();
		Value baseValue = defValue.getBase();
		//if the baseValue and the arrayLocal from parameter can point to the same array by BOFA
		//rightnow we only consider local
		if (baseValue instanceof Local) {
			//String arrayLocalString = arrayLocal.toString();
			//String baseString = ((Local) baseValue).toString();
			//if (arrayLocalString.equals(baseString)) //compare by string for point-to
			if (mayPointToTheSameRef(arrayLocal,baseValue, methodInfo.sootMethod, methodInfo.sootMethod))
			{ //point to the same array
				Stmt defStmt = (Stmt) arrayRefs.get(defValue);
				//see if the defStmt can reach stmt
				if (SlicingMethod.reachableStmtFrom(defStmt, stmtGraph).contains(stmt)) {
					Set vars = new ArraySet();
					vars.add(defValue);
					DataBox ddBox = new DataBox(defStmt, vars);
					ddList.add(ddBox);
				}
			}
		}
	}
}
  /**
   * Analyse data dependence for parameters (field references) of each call site in the method
   * using REF/MOD information.
   * <br>
   * Store the result in {@link #stmtToDdOn stmtToDdOn}.
   */
private void ddForParameters() {
	for (Iterator siteIt = callSiteMap.keySet().iterator(); siteIt.hasNext();) {
		CallSite site = (CallSite) siteIt.next();
		SootMethod calledMd = (SootMethod) callSiteMap.get(site);
		MethodInfo calledMdInfo = (MethodInfo) Slicer.sootMethodInfoMap.get(calledMd);
		if (calledMdInfo==null) continue;
		Fields usedFields = calledMdInfo.REF;
		Stmt callSiteStmt = site.callStmt;
		int callSiteStmtIndex = stmtList.indexOf(callSiteStmt);
		Set ddList = new ArraySet();
		Set usingParas = Fields.parametersLocalize(calledMdInfo, usedFields, site.invokeExpr);
		for (Iterator i = usingParas.iterator(); i.hasNext();) {
			InstanceFieldRef para = (InstanceFieldRef) i.next();
			dataDependOfInstanceFieldRef(para, callSiteStmt, ddList);
		}
		if (ddList.size() == 0)
			continue;
		if (stmtToDdOn.containsKey(callSiteStmt))
			ddList.addAll((Set) stmtToDdOn.get(callSiteStmt));
		stmtToDdOn.put(callSiteStmt, ddList);
	}
}
  /**
   * Analyse data dependence for static field, static based field and intance field references
   * using REF/MOD information.
   * <br>
   * Store the result in {@link #stmtToDdOn stmtToDdOn}.
   * <p>
   * @see #dataDependOfStaticFieldRef(StaticFieldRef,Stmt,Set)
   * @see #dataDependOfInstanceFieldRef(InstanceFieldRef,Stmt,Set)
   */
private void ddForStaticAndInstanceFd() {
	Fields refFields = methodInfo.REF;

	for (Iterator i = refFields.staticFields.iterator(); i.hasNext();) {
		DataBox dbx = (DataBox) i.next();
		Set ddList = new ArraySet();
		Stmt useStmt = dbx.getInterferStmt();
		for (Iterator j = dbx.getInterferVars().iterator(); j.hasNext();) {
			StaticFieldRef stcField = (StaticFieldRef) j.next();
			dataDependOfStaticFieldRef(stcField, useStmt, ddList);
		}
		if (ddList.size() == 0)
			continue;
		if (stmtToDdOn.containsKey(useStmt))
			ddList.addAll((Set) stmtToDdOn.get(useStmt));
		stmtToDdOn.put(useStmt, ddList);
	}

	for (Iterator i = refFields.instanceFields.iterator(); i.hasNext();) {
		DataBox dbx = (DataBox) i.next();
		Set ddList = new ArraySet();
		Stmt useStmt = dbx.getInterferStmt();
		for (Iterator j = dbx.getInterferVars().iterator(); j.hasNext();) {
			InstanceFieldRef insField = (InstanceFieldRef) j.next();
			dataDependOfInstanceFieldRef(insField, useStmt, ddList);
		}
		if (ddList.size() == 0)
			continue;
		if (stmtToDdOn.containsKey(useStmt))
			ddList.addAll((Set) stmtToDdOn.get(useStmt));
		stmtToDdOn.put(useStmt, ddList);
	}
}
/**
   * Get the set of statements that define some mutual static field references
   * with the statement <code>staticDefIndex</code> using 
   * {@link #staticFieldDefStmtList staticFieldDefStmtList}.
   * <p>
   * @param staticDefIndex  the index of a statement in 
   * {@link #staticFieldDefStmtList staticFieldDefStmtList} which defines
   * a static field reference.
   * @return a set of {@link java.lang.Integer Integer} which is index of a statement
   * in {@link #staticFieldDefStmtList staticFieldDefStmtList}.
   * Empty set is returned, if there is no such statement.
   */
private Set defsNotPreserves(int staticDefIndex) {
	Set defsNotPres = new ArraySet();
	Object array[] = staticFieldDefStmtList.toArray();
	defsNotPres.add(new Integer(staticDefIndex));
	DataBox staticBox = (DataBox) array[staticDefIndex];
	Set staticDefs = staticBox.getInterferVars();
	for (int i = 0; i < array.length; i++) {
		DataBox db = (DataBox) array[i];
		Set staticDefs2 = db.getInterferVars();
		Set intersectDefs = SetUtil.setIntersection(staticDefs, staticDefs2);
		if (intersectDefs.size() != 0) {
			boolean hasNoArrayComponent = true;
			for (Iterator varIt = intersectDefs.iterator(); varIt.hasNext();) {
				Value staticVar = (Value) varIt.next();
				Type varType = staticVar.getType();
				if (varType instanceof ArrayType) {

					//to see if the definition is to a component of an array
					Stmt defStmt = db.getStmt();
					List defBoxes = defStmt.getDefBoxes();
					for (Iterator defIt = defBoxes.iterator(); defIt.hasNext();) {
						ValueBox defBox = (ValueBox) defIt.next();
						Value defValue = defBox.getValue();
						if (defValue instanceof ArrayRef)
							hasNoArrayComponent = false;
					}
				}
			}
			if (hasNoArrayComponent)
				defsNotPres.add(new Integer(i));
		}
	}
	return defsNotPres;
}
  /**
   * Analyse control and data dependence for the method.
   * <p>
   * @see #reachingDefOfStaticField()
   * @see #reachingDefOfInstanceField()
   * @see #controlDependAnalysis()
   * @see #dataDependAnalysis()
   * @see #specialInvokeDdAnalysis()
   */
private void dependency() {
	if (staticFieldDefStmtList.size() != 0)
		reachingDefOfStaticField();

	if (instanceFieldDefStmtList.size() != 0)
		reachingDefOfInstanceField();
	controlDependAnalysis();
	dataDependAnalysis();

	//add specialinvoke init into data dependent map stmtToDdOn
	specialInvokeDdAnalysis();
	divergenceDependenceAnalysis();
}
/**
 * Insert the method's description here.
 * Creation date: (4/20/2001 10:21:10 AM)
 */
private void divergenceDependenceAnalysis() {
	divergenceMap = new HashMap();
	Stmt[] statementsInLoop;
	Stmt[] testStatements;
	Annotation annForSm = annotationManager.getAnnotation(methodInfo.sootClass, methodInfo.sootMethod);
	Vector currentCFANNs = annForSm.getAllAnnotations(true);
	for (Enumeration e = currentCFANNs.elements(); e.hasMoreElements();) {
		Annotation cfann = (Annotation) e.nextElement();
		statementsInLoop = null;
		testStatements = null;
		if (cfann instanceof DoWhileStmtAnnotation) {
			DoWhileStmtAnnotation doWhile = (DoWhileStmtAnnotation) cfann;
			statementsInLoop = doWhile.getStatements();
			testStatements = doWhile.getTestStatements();
		} else
			if (cfann instanceof ForStmtAnnotation) {
				ForStmtAnnotation forStmt = (ForStmtAnnotation) cfann;
				statementsInLoop = forStmt.getStatements();
				testStatements = forStmt.getTestStatements();
			} else
				if (cfann instanceof WhileStmtAnnotation) {
					WhileStmtAnnotation whileStmt = (WhileStmtAnnotation) cfann;
					statementsInLoop = whileStmt.getStatements();
					testStatements = whileStmt.getTestStatements();
				}
		if ((statementsInLoop == null) || (testStatements == null))
			continue;
		Stmt preDivergencePoint = null;
		if (testStatements.length == 0)  //for example "for (;;)" there is no test statement
			preDivergencePoint = statementsInLoop[0];
		else
			preDivergencePoint = getConditionalStmtFrom(testStatements);
		Set reachableStmtsFromThePoint = SlicingMethod.reachableStmtSetFrom(preDivergencePoint, stmtGraph);
		BitSet reachableBitSet = SetUtil.stmtSetToBitSet(reachableStmtsFromThePoint, stmtList);
		BitSet stmtsBitSetInLoop = SetUtil.stmtArrayToBitSet(statementsInLoop, stmtList);
		reachableBitSet = SetUtil.bitSetAndNot(reachableBitSet, stmtsBitSetInLoop);
		divergenceMap.put(preDivergencePoint, reachableBitSet);
	}
}
  /**
   * Calculate postdominators and immediate postdominators.
   * <p>
   * @see #postDomAnalysis()
   * @see #immediateDominators()
   */
  private void domination()
	{
	  //dominateAnalysis();
	  postDomAnalysis();
	  immediateDominators();
	}
/**
 * Insert the method's description here.
 * Creation date: (6/15/2001 11:02:44 PM)
 * @return ca.mcgill.sable.util.Set
 * @param refSet ca.mcgill.sable.util.Set
 */
public static Set fieldRefToSootField(Set refSet) {
	Set sootFieldSet = new ArraySet();
	for (Iterator refIt = refSet.iterator(); refIt.hasNext();) {
		FieldRef oneRef = (FieldRef) refIt.next();
		sootFieldSet.add(oneRef.getField());
	}
	return sootFieldSet;
}
  /**
   * Get the map of control dependency.
   * <p>
   * @return {@link #stmtToCdOn stmtToCdOn}.
   */
  public Map getCdMap()
	{
	  return stmtToCdOn;
	}
/**
 * Insert the method's description here.
 * Creation date: (4/20/2001 11:25:56 AM)
 * @return ca.mcgill.sable.soot.jimple.Stmt
 * @param testStmts ca.mcgill.sable.soot.jimple.Stmt[]
 */
private Stmt getConditionalStmtFrom(Stmt[] testStmts) {
	for (int i = 0; i < testStmts.length; i++) {
		if (testStmts[i] instanceof IfStmt)
			return testStmts[i];
	}
	return testStmts[0];
}
  /**
   * Get the map of data dependency.
   * <p>
   * @return {@link #stmtToDdOn stmtToDdOn}.
   */
  public Map getDdMap()
	{
	  return stmtToDdOn;
	}
/**
 * Insert the method's description here.
 * Creation date: (4/20/2001 10:27:38 AM)
 * @return ca.mcgill.sable.util.Map
 */
Map getDivergenceMap() {
	return divergenceMap;
}
  /**
   * Get all statements in handlers in the method.
   * <p>
   * @return a set of {@link ca.mcgill.sable.soot.jimple.Stmt Stmt}.
   * Empty set is returned, if there is no such statement.
   */
private Set getHandlerStmtSet() {
	Set stmtSet = new ArraySet();
	for (Iterator trapIt = jimpleBody.getTraps().iterator(); trapIt.hasNext();) {
		Trap trap = (Trap) trapIt.next();
		Stmt handlerStmt = (Stmt) trap.getHandlerUnit();
		stmtSet.add(handlerStmt);
	}
	return stmtSet;
}
  /**
   * Get the index of statement <code>s</code> from 
   * {@link #instanceFieldDefStmtList instanceFieldDefStmtList}.
   * <p>
   * @return index of <code>s</code>. <code>-1</code> is returned, if
   * <code>s</code> is not in <code>instanceFieldDefStmtList</code>.
   */
   private int getInstanceDefIndexOf(Stmt s)
	{
	  int index = -1;
	  Object array[] = instanceFieldDefStmtList.toArray();
	  for (int i = 0; i<array.length; i++)
	{
	  DataBox db = (DataBox) array[i];
	  Stmt fieldDefStmt = db.getInterferStmt();
	  if (fieldDefStmt.equals(s)) 
	    {
	      index = i;
	      return index;
	    }
	}

	  return index;
	}
  /**
   * Get {@link #instanceFieldDefStmtList instanceFieldDefStmtList}.
   * <p>
   * @return {@link #instanceFieldDefStmtList instanceFieldDefStmtList}.
   */
  public Set getInstanceFieldDefStmtList()
	{
	  return instanceFieldDefStmtList;
	}
  /**
   * Get {@link #interferenceMap interferenceMap}.
   * <p>
   * @return {@link #interferenceMap interferenceMap}.
   */
  public Map getInterferenceMap()
	{
	  return interferenceMap;
	}
  /**
   * Get {@link #lockAnalysis lockAnalysis}.
   * <p>
   * @return {@link #lockAnalysis lockAnalysis}.
   */
  public LockAnalysis getLockAnalysis()
	{
	  return lockAnalysis;
	}
  /**
   * Get {@link #readyDependMap readyDependMap}.
   * <p>
   * @return {@link #readyDependMap readyDependMap}.
   */
  public Map getReadyDependMap()
	{
	  return readyDependMap;
	}
/**
 * Insert the method's description here.
 * Creation date: (7/11/2001 3:18:54 PM)
 * @return ca.mcgill.sable.util.Set
 * @param vars ca.mcgill.sable.util.Set
 * @param sootField ca.mcgill.sable.soot.SootField
 */
private Set getRefsWithCommonField(Set vars, SootField sootField) {
	Set refSet = new ArraySet();
	for (Iterator varIt = vars.iterator(); varIt.hasNext();) {
		Object var = (Object) varIt.next();
		if (var instanceof InstanceFieldRef) {
			InstanceFieldRef insVar = (InstanceFieldRef) var;
			if (insVar.getField().equals(sootField))
				refSet.add(insVar.getBase());
		}
	}
	return refSet;
}
/**
   * Find out the special invoke &lt init &gt statement corresponding to the 
   * <code>new</code> expression. For example, there is a fragment of Jimple code like 
   * <p>
   *       [1] p = new Power;
   * <br>
   *       [2] specialinvoke p.[Power. &lt init &gt ():void]();
   * <br>
   *       [3] i = virtualinvoke p.[Power.power(int,int):int](5, 3);
   * <p>
   * Suppose slice it from statement [3]. Then apparently [3] is data dependent on
   * [1] by <code>p</code>. Statement [2] will not be in the slice, 
   * since there is no explicit definition in [2]. In fact, [2] is an initialization
   * for <code>p</code>. So it can be and should be considered as a definition of 
   * <code>p</code>. In other words, [1] and [2] together should be the definition of
   * <code>p</code> other than only [1].
   * So statement [3] is data dependent on both [1] and [2]. 
   * <br>
   * However, the traditional data dependence analysis dose not cover this condition,
   * since [2] is not a definition statement.
   * <p>
   * Given a statement with <code>new</code> expression, e.g., [1], this method
   * will search out the corresponding special &lt init &gt invoke for it, e.g., [2].
   * This method is called by {@link #specialInvokeDdAnalysis() specialInvokeDdAnalysis()}.
   * <p>
   * @param ddBoxWithNewExpr DataBox which includes a statement with 
   * <code>new</code> expression.
   * @param typeOfNewExpr type of the new expression.
   * @return the corresponding special init invoke.
   * @throws NoInitStmtException if can not find the special init invoke for 
   * <code>ddBoxWithNewExpr</code>.
   */
private Stmt getSpecialInvokeStmtOf(DataBox ddBoxWithNewExpr, SootClass newClass) {
	for (Iterator stmtKeyIt = stmtToDdOn.keySet().iterator(); stmtKeyIt.hasNext();) {
		Stmt stmtK = (Stmt) stmtKeyIt.next();
		if (!(stmtK instanceof InvokeStmt))
			continue;
		InvokeStmt invokeStmtK = (InvokeStmt) stmtK;
		Value invokeExpr = invokeStmtK.getInvokeExpr();
		if (!(invokeExpr instanceof SpecialInvokeExpr))
			continue;
		String methodInvoked = ((InvokeExpr) invokeExpr).getMethod().getName();
		if (!methodInvoked.equals("<init>"))
			continue;

		//String typeOfInvokeBase =
		// ((SpecialInvokeExpr) invokeExpr).getBase().getType().toString();

		SootClass typeOfInvokeBase = ((InvokeExpr) invokeExpr).getMethod().getDeclaringClass();
		//if (!typeOfInvokeBase.equals(typeOfNewExpr))
		//continue;
		if (newClass.equals(typeOfInvokeBase) || newClass.getSuperClass().equals(typeOfInvokeBase)) {
			Set specDdBoxes = (Set) stmtToDdOn.get(stmtK);
			if (specDdBoxes.contains(ddBoxWithNewExpr))
				return stmtK;
		}
	}
	throw new NoInitStmtException("Can not find the special invoke init statement dependent on " + ddBoxWithNewExpr + "  in getSpecialInvokeStmtOf of BuildPDG.java");
}
  /**
   * Find out the special invoke &lt init &gt statement corresponding to the 
   * <code>new</code> expression. For example, there is a fragment of Jimple code like 
   * <p>
   *       [1] p = new Power;
   * <br>
   *       [2] specialinvoke p.[Power. &lt init &gt ():void]();
   * <br>
   *       [3] i = virtualinvoke p.[Power.power(int,int):int](5, 3);
   * <p>
   * Suppose slice it from statement [3]. Then apparently [3] is data dependent on
   * [1] by <code>p</code>. Statement [2] will not be in the slice, 
   * since there is no explicit definition in [2]. In fact, [2] is an initialization
   * for <code>p</code>. So it can be and should be considered as a definition of 
   * <code>p</code>. In other words, [1] and [2] together should be the definition of
   * <code>p</code> other than only [1].
   * So statement [3] is data dependent on both [1] and [2]. 
   * <br>
   * However, the traditional data dependence analysis dose not cover this condition,
   * since [2] is not a definition statement.
   * <p>
   * Given a statement with <code>new</code> expression, e.g., [1], this method
   * will search out the corresponding special &lt init &gt invoke for it, e.g., [2].
   * This method is called by {@link #specialInvokeDdAnalysis() specialInvokeDdAnalysis()}.
   * <p>
   * @param ddBoxWithNewExpr DataBox which includes a statement with 
   * <code>new</code> expression.
   * @param typeOfNewExpr type of the new expression.
   * @return the corresponding special init invoke.
   * @throws NoInitStmtException if can not find the special init invoke for 
   * <code>ddBoxWithNewExpr</code>.
   */
private Stmt getSpecialInvokeStmtOf(DataBox ddBoxWithNewExpr, String typeOfNewExpr) {
	for (Iterator stmtKeyIt = stmtToDdOn.keySet().iterator(); stmtKeyIt.hasNext();) {
		Stmt stmtK = (Stmt) stmtKeyIt.next();
		if (!(stmtK instanceof InvokeStmt))
			continue;
		InvokeStmt invokeStmtK = (InvokeStmt) stmtK;
		Value invokeExpr = invokeStmtK.getInvokeExpr();
		if (!(invokeExpr instanceof SpecialInvokeExpr))
			continue;
		String methodInvoked = ((InvokeExpr) invokeExpr).getMethod().getName();
		if (!methodInvoked.equals("<init>"))
			continue;

		String typeOfInvokeBase =
		 ((SpecialInvokeExpr) invokeExpr).getBase().getType().toString();

		//SootClass typeOfInvokeBase = ((InvokeExpr) invokeExpr).getMethod().getDeclaringClass();
		if (typeOfInvokeBase.equals(typeOfNewExpr))
		//if (newClass.equals(typeOfInvokeBase) || newClass.getSuperClass().equals(typeOfInvokeBase))
		  {
		    Set specDdBoxes = (Set) stmtToDdOn.get(stmtK);
		    if (specDdBoxes.contains(ddBoxWithNewExpr))
		      return stmtK;
		  }
	}
	throw new NoInitStmtException("Can not find the special invoke init statement dependent on " + ddBoxWithNewExpr + "  in getSpecialInvokeStmtOf of BuildPDG.java");
}
  /**
   * Get the index of statement <code>s</code> from 
   * {@link #staticFieldDefStmtList staticFieldDefStmtList}
   * <p>
   * @return index of <code>s</code>. <code>-1</code> is returned, if
   * <code>s</code> is not in <code>staticFieldDefStmtList</code>.
   */
  private int getStaticDefIndexOf(Stmt s)
	{
	  int index = -1;
	  Object array[] = staticFieldDefStmtList.toArray();
	  for (int i = 0; i<array.length; i++)
	{
	  DataBox db = (DataBox) array[i];
	  Stmt fieldDefStmt = db.getInterferStmt();
	  if (fieldDefStmt.equals(s)) 
	    {
	      index = i;
	      return index;
	    }
	}

	  return index;
	}
  /**
   * Compute immediate (post)dominators for {@link #stmtToPostdoms stmtToPostdoms}. 
   * <br> This method is only called by {@link #domination() domination()}. 
   * <br> The result is stored in {@link #stmtToImmedPostdom stmtToImmedPostdom}.
   */
private void immediateDominators() {
	//immediate dominator for one node must be unique or none

	stmtToImmedPostdom = computeImmedDom(stmtToPostdoms);
}
  /**
   * Get immediate postdominator for a statement represented by an index
   * <code>stmtIndex</code>, using {@link #stmtToImmedPostdom stmtToImmedPostdom}.
   * <p>
   * @param stmtIndex index of a statement.
   * @return index of the postdominator for <code>stmtIndex</code>.
   */
  public int immediatePostdominatorOf(int stmtIndex)
	{
	  Stmt stmt = (Stmt) stmtList.get(stmtIndex);
	  return ((Integer) stmtToImmedPostdom.get(stmt)).intValue();
	}
  /**
   * Get immediate postdominator for a statement
   * <code>stmt</code>, using {@link #stmtToImmedPostdom stmtToImmedPostdom}.
   * <p>
   * @param stmt query statement.
   * @return index of the postdominator for <code>stmt</code>.
   */
  public int immediatePostdominatorOf(Stmt stmt)
	{
	  return ((Integer) stmtToImmedPostdom.get(stmt)).intValue();
	}
  /**
   * Get a back point for the method if there is an indefinite loop in the method.
   * <br>
   * The back point is, for example,
   * <p>
   * [1] label-a: stmt1;
   * <br>[2]   ..... ;
   * <br>..... ;
   * <br>[n] goto label-a;
   * <p>
   * statement [n], assumming that statement [1] to [n] is an indefinite loop.
   * <p>
   * @param cfanns annotation manager.
   * @param mdInfo method information.
   * @return the index of back point of an indefinite loop, if any. 
   * {@link IndexMaps#specialExitNode IndexMaps.specialExitNode} is returned,
   * if there is no such back point.
   */
public static Integer indefiniteFrom(AnnotationManager cfanns, MethodInfo mdInfo) {
	Annotation annForSm = cfanns.getAnnotation(mdInfo.sootClass, mdInfo.sootMethod);

	//BlockStmtAnnotation blockAnnForSm = (BlockStmtAnnotation) annForSm;
	Vector currentCFANNs = annForSm.getAllAnnotations(true);
	for (Enumeration e = currentCFANNs.elements(); e.hasMoreElements();) {
		Annotation cfann = (Annotation) e.nextElement();
		if (cfann instanceof ControlFlowAnnotation) {
			ControlFlowAnnotation controlCfann = (ControlFlowAnnotation) cfann;
			if (controlCfann.isIndefinite()) {
				Stmt backPointStmt = controlCfann.getBackpointStmt();
				Integer backIndex = new Integer(mdInfo.originalStmtList.indexOf(backPointStmt));
				return backIndex;
			}
		}
	}
	return IndexMaps.specialExitNode;
}
//new postDomAnalysis
  /**
   * Initialize workList and {@link #stmtToPostdoms stmtToPostdoms}
   * for postdomination analysis which is done by
   * {@link #postDomAnalysis() postDomAnalysis()}.
   * <br>This initialization is required by the algorithm in Muchnick book
   * "Advanced compiler design and implementation"
   * by Steven S. Muchnick, 1997.
   * <p>
   * @param cfanns annotation manager.
   * @return a linked list of {@link Integer Integer} which is the index
   * of statement in {@link #stmtList stmtList}.
   * @throws NoExitNodeException if can not find out an exit node for the method.
   */
private LinkedList initializeWorkList(AnnotationManager cfanns) {
	LinkedList workList = new LinkedList();
	BitSet postDominators = new BitSet(stmtList.size());
	Stmt keyStmt;

	//initialize the exit statement's postdom as itself
	//if there is no exit node, that means there must be an
	//indefinite loop
	if (SetUtil.emptyBitSet(exitNodesNoThrow)) {
		indefiniteNode = indefiniteFrom(cfanns, methodInfo);
		if (indefiniteNode.equals(IndexMaps.specialExitNode))
			throw new NoExitNodeException("Can not find out the exit node or the infinite loop in method " + methodInfo.sootMethod.getName());
		workList.addFirst(indefiniteNode);
		keyStmt = (Stmt) stmtList.get(indefiniteNode.intValue());
		postDominators.set(indefiniteNode.intValue());
		stmtToPostdoms.put(keyStmt, postDominators);
	} else {
		for (int i = 0; i < stmtList.size(); i++) {
			if (exitNodes.get(i)) {
				workList.addFirst(new Integer(i));
				keyStmt = (Stmt) stmtList.get(i);
				postDominators = new BitSet(stmtList.size());
				postDominators.set(i);
				stmtToPostdoms.put(keyStmt, postDominators);
			}
		}
		indefiniteNode = IndexMaps.specialExitNode;
	}
	return workList;
}
/**
   * Get the set of statements that define some mutual instance field references
   * with the statement <code>instanceDefIndex</code> using 
   * {@link #instanceFieldDefStmtList staticFieldDefStmtList}.
   * <p>
   * @param instanceDefIndex  the index of a statement in 
   * {@link #instanceFieldDefStmtList instanceFieldDefStmtList} which defines
   * an instance field reference.
   * @return a set of {@link java.lang.Integer} which is index of a statement
   * in {@link #instanceFieldDefStmtList instanceFieldDefStmtList}.
   * Empty set is returned, if there is no such statement.
   */
private Set instanceDefsNotPreserves(int instanceDefIndex) {
	Set defsNotPres = new ArraySet();
	defsNotPres.add(new Integer(instanceDefIndex));
	Object array[] = instanceFieldDefStmtList.toArray();
	DataBox instanceBox = (DataBox) array[instanceDefIndex];
	Set instanceDefs = instanceBox.getInterferVars();
	for (int i = 0; i < array.length; i++) {
		DataBox db = (DataBox) array[i];
		Set instanceDefs2 = db.getInterferVars();
		Set intersectDefs = SetUtil.setIntersection(instanceDefs, instanceDefs2);
		if (intersectDefs.size() != 0)
			defsNotPres.add(new Integer(i));
	}
	return defsNotPres;
}
/**
 * Insert the method's description here.
 * Creation date: (7/10/2001 3:13:31 PM)
 * @return boolean
 * @param v1 ca.mcgill.sable.soot.jimple.Value
 * @param v2 ca.mcgill.sable.soot.jimple.Value
 * @param enclosingMethod ca.mcgill.sable.soot.SootMethod
 */
public static boolean mayPointToTheSameRef(Value v1, Value v2, SootMethod enclosingMethod1, SootMethod enclosingMethod2) {
	Collection refValues = Slicer.BOFA_Analysis.referenceValueSet(v1, enclosingMethod1);
	Collection refValues2 = Slicer.BOFA_Analysis.referenceValueSet(v2, enclosingMethod2);
	//see if there is common element in refValues and refValues2
	for (Iterator valueIt = refValues.iterator(); valueIt.hasNext();) {
		if (refValues2.contains(valueIt.next()))
			return true;
	}
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (7/10/2001 3:13:31 PM)
 * @return boolean
 * @param v1 ca.mcgill.sable.soot.jimple.Value
 * @param v2 ca.mcgill.sable.soot.jimple.Value
 * @param enclosingMethod ca.mcgill.sable.soot.SootMethod
 */
public static boolean mayPointToTheSameRef(Value v1, Set vs, SootMethod enclosingMethod1, SootMethod enclosingMethod2) {
for (Iterator varIt = vs.iterator(); varIt.hasNext();){
	Value v2 = (Value) varIt.next();
	boolean mayPointToTheSame = BuildPDG.mayPointToTheSameRef(v1, v2, enclosingMethod1, enclosingMethod2);
	if (mayPointToTheSame) return true;
}
	return false;
}
  /**
   * Analyse postdomination for the method.
   * <p>
   * @see #initializeWorkList(AnnotationManager)
   * @see #postdomFixPoint(LinkedList)
   */
private void postDomAnalysis() {
	LinkedList workList = initializeWorkList(annotationManager);
	postdomFixPoint(workList);
}
  /**
   * Calculate postdominators for each statement in the method starting from
   * ininialized <code>workList</code>.
   * <br>
   * The result is stored in {@link #stmtToPostdoms stmtToPostdoms}.
   * <p>
   * The alorithm is from the book
   * "Advanced compiler design and implementation"
   * by Steven S. Muchnick, 1997.
   */
private void postdomFixPoint(LinkedList workList) {
	boolean change = true;
	BitSet postDominators = new BitSet(stmtList.size());
	BitSet tempPostdoms = new BitSet(stmtList.size());
	Set visitedNode = new ArraySet();

	// Initialize postdoms for other statement as all index set
	// but not including exception handling statement

	Set initialWorkList = new ArraySet();
	for (int i = 0; i < workList.size(); i++) {
		initialWorkList.add((Integer) workList.get(i));
	}

	//add start nodes into the worklist 

	workList.addFirst(new Integer(0));
	BitSet indexSetWoE = methodInfo.indexMaps.indexSetWithoutExceptionHandling();
	for (int i = 0; i < indexSetWoE.size(); i++) {
		if (indexSetWoE.get(i))
			postDominators.set(i);
	}
	while (workList.size() != 0) {
		Integer nodeIndex = (Integer) workList.removeFirst();
		visitedNode.add(nodeIndex);
		Stmt nodeStmt = (Stmt) stmtList.get(nodeIndex.intValue());
		if (!initialWorkList.contains(nodeIndex))
			stmtToPostdoms.put(nodeStmt, postDominators);
		List nodeStmtSuccs = stmtGraph.getSuccsOf(nodeStmt);
		//List newSuccs = removeExceptionCaught(nodeStmtSuccs);
		List newSuccs = nodeStmtSuccs;
		for (Iterator succIt = newSuccs.iterator(); succIt.hasNext();) {
			Stmt succStmt = (Stmt) succIt.next();
			Integer succIndex = new Integer(stmtList.indexOf(succStmt));
			if (workList.contains(succIndex) || visitedNode.contains(succIndex)) {
			} else
				workList.addLast(succIndex);
		}
	}
	while (change) {
		change = false;
		workList.clear();
		visitedNode.clear();

		//initializing worklist everytime
		for (Iterator i = initialWorkList.iterator(); i.hasNext();) {
			workList.addFirst((Integer) i.next());
		}
		while (workList.size() != 0) {
			Integer stmtIndex = (Integer) workList.removeFirst();
			visitedNode.add(stmtIndex);
			Stmt keyStmt = (Stmt) stmtList.get(stmtIndex.intValue());
			postDominators = (BitSet) stmtToPostdoms.get(keyStmt);
			if (postDominators == null)
				continue;
			tempPostdoms = (BitSet) postDominators.clone();
			List realSuccs = stmtGraph.getSuccsOf(keyStmt);
			List succs = removeExceptionCaught(realSuccs);
			for (int i = 0; i < succs.size(); i++) {
				Stmt succStmt = (Stmt) succs.get(i);
				BitSet succDom = (BitSet) stmtToPostdoms.get(succStmt);
				tempPostdoms.and(succDom);
			}
			tempPostdoms.set(stmtIndex.intValue());
			if (!tempPostdoms.equals(postDominators)) {
				change = true;
				stmtToPostdoms.remove(keyStmt);
				stmtToPostdoms.put(keyStmt, tempPostdoms);
			}

			//begin to add nodes into workList


			List preds = stmtGraph.getPredsOf(keyStmt);
			for (int i = 0; i < preds.size(); i++) {
				Stmt predStmt = (Stmt) preds.get(i);
				Integer predIndex = new Integer(stmtList.indexOf(predStmt));
				if (workList.contains(predIndex) || visitedNode.contains(predIndex)) {
				} else
					workList.addLast(predIndex);
			}
		}
	}
}
/**
 * Get postdominators of a statement represented by the <code>int</code> index in 
 * {@link #stmtList stmtList} using {@link #stmtToPostdoms stmtToPostdoms}.
 * <p>
 * @param index int index of the query statement.
 * @return a set of postdominators of <code>index</code>
 * represented by {@link BitSet BitSet}.
 */
BitSet postdominatorsOf(int index) {
	Stmt stmt = (Stmt) stmtList.get(index);
	return (BitSet) stmtToPostdoms.get(stmt);
}
/**
 * Get postdominators of a statement using {@link #stmtToPostdoms stmtToPostdoms}.
 * <p>
 * @param stmt query statement.
 * @return a set of postdominators of <code>stmt</code>
 * represented by {@link BitSet BitSet}.
 */
public BitSet postdominatorsOf(Stmt stmt) {
	return (BitSet) stmtToPostdoms.get(stmt);
}
/**
 * Get postdominators of a statement represented by the Integer index in 
 * {@link #stmtList stmtList} using {@link #stmtToPostdoms stmtToPostdoms}.
 * <p>
 * @param stmtIndex Integer index of the query statement.
 * @return a set of postdominators of <code>stmtIndex</code>
 * represented by {@link BitSet BitSet}.
 */
  public BitSet postdominatorsOf(Integer stmtIndex)
	{
	  Stmt stmt = (Stmt) stmtList.get(stmtIndex.intValue());
	  return (BitSet) stmtToPostdoms.get(stmt);
	}
/**
 * Insert the method's description here.
 * Creation date: (4/20/2001 11:33:42 AM)
 * @return java.util.BitSet
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public BitSet preDivergencePointsOf(Stmt stmt) {
	int indexOfStmt = stmtList.indexOf(stmt);
	BitSet preDivergencePoints = new BitSet(stmtList.size());
	for (Iterator keyIt = divergenceMap.keySet().iterator(); keyIt.hasNext();) {
		Stmt divergPoint = (Stmt) keyIt.next();
		BitSet reachableStmts = (BitSet) divergenceMap.get(divergPoint);
		if (reachableStmts.get(indexOfStmt)) {
			int indexOfDivergPoint = stmtList.indexOf(divergPoint);
			preDivergencePoints.set(indexOfDivergPoint);
		}
	}
	return preDivergencePoints;
}
  /** 
   * Prepare to analyse data and control dependence.
   * <p>
   * @param mdInfo method information.
   */
private void prepareToBuild(MethodInfo mdInfo) {
	methodInfo = mdInfo;
	jimpleBody = (JimpleBody) mdInfo.sootMethod.getBody(Jimple.v());
	stmtList = mdInfo.originalStmtList;
	stmtToPostdoms = new HashMap(stmtList.size() * 2 + 1, 0.7f);
	stmtGraph = mdInfo.indexMaps.getStmtGraph();
	IndexMaps im = mdInfo.indexMaps;
	exitNodes = im.exitNodes();

	exitNodesNoThrow = im.exitNodesWithoutThrow(exitNodes);

	callSiteMap = im.getCallSiteMap();
	collectInstanceFieldDefStmt();
	staticFieldDefStmtList = mdInfo.MOD.staticFields;
}
  /**
   * Implement reaching definition for instance field references.
   * <br>
   * There is no reaching definition analysis for field references in 
   * Jimple. It only provides the reaching analysis for simple local variables.
   * <br>This method is a modified implementation of Bit Vector algorithm in the book
   * "Advanced compiler design and implementation"
   * by Steven S. Muchnick, 1997. Page 218.
   * <br>
   * The result is store in {@link #instanceReachDef instanceReachDef}.
   */

private void reachingDefOfInstanceField() {
	//we have to determine the reaching definition of instance fields
	//considering the call site information like MOD and REF
	//this algorithm is like the reaching definition analysis for 
	//static fields
	boolean change = true;
	int instanceFieldDefSize = instanceFieldDefStmtList.size();
	int stmtListSize = stmtList.size();
	BitSet reachIn[] = new BitSet[stmtListSize];
	BitSet preserve[] = new BitSet[stmtListSize];
	BitSet generate[] = new BitSet[stmtListSize];

	//initializing the three array
	//the sequence of array is the same as the sequence of index in stmtList
	//e.g. reachIn[i] indicate that the reach in set of the statement 
	//stmtList.get(i)
	for (int i = 0; i < stmtListSize; i++) {
		//the sequence of each BitSet is determined by the index sequence
		//of statciFieldDefStmt, e.g. reachIn[i].get(3) will get the value
		//for the 3rd stmt in staticFieldDefStmt (staticFieldDefStmt.get(3))
		reachIn[i] = new BitSet(instanceFieldDefSize);
		preserve[i] = new BitSet(instanceFieldDefSize);
		generate[i] = new BitSet(instanceFieldDefSize);

		//initializing each element as 00000000
		//initializing preserve as 111111
		for (int j = 0; j < instanceFieldDefSize; j++) {
			reachIn[i].clear(j);
			preserve[i].set(j);
			generate[i].clear(j);
		}
	}

	//calculate the preserve[i] and generate[i], those are invariant
	for (int i = 0; i < stmtListSize; i++) {
		Stmt stmt = (Stmt) stmtList.get(i);
		int stmtInInstanceDef = getInstanceDefIndexOf(stmt);
		if (stmtInInstanceDef >= 0) {
			//stmt is in the staticDefStmtList
			generate[i].set(stmtInInstanceDef);

			//collect all the defs which defines that the variable in 
			//stmtInStaticDef in to one set
			
			Set defSet = instanceDefsNotPreserves(stmtInInstanceDef);

			//clear all the preserves such that define the variable in 
			//stmtInStaticDef
			for (Iterator k = defSet.iterator(); k.hasNext();) {
				int defNotPreserve = ((Integer) k.next()).intValue();
				preserve[i].clear(defNotPreserve);
			}
			
		}
	}
	while (change) {
		change = false;
		for (int i = 0; i < stmtListSize; i++) {
			Stmt stmt = (Stmt) stmtList.get(i);

			//calculate new reachIn[i], with its all predecessors
			BitSet newReachIn = null;
			List preds = stmtGraph.getPredsOf(stmt);
			for (int ppp = 0; ppp < preds.size(); ppp++) {
				int j=stmtList.indexOf(preds.get(ppp));
				//get preserve of preds j preserve[j]
				//get generate of preds j generate[j]
				BitSet tempReachIn = (BitSet) reachIn[j].clone();
				tempReachIn.and(preserve[j]);
				tempReachIn.or(generate[j]);
				if (ppp == 0)
					newReachIn = (BitSet) tempReachIn.clone();
				else
					newReachIn.or(tempReachIn);
			}
			if (newReachIn != null)

				
				//if newReachIn== null, this means
				//that preds.size() == 0
				if (!reachIn[i].equals(newReachIn)) {
					reachIn[i] = newReachIn;
					change = true;
				}
		}
	}
	instanceReachDef = reachIn;
}
//There is no reaching definition analysis for static field reference in 
//Jimple, it only provides this reaching analysis for local variables
//this method is an implementation of Bit Vector algorithm on P218 of the 
//Muchnick book
  /**
   * Implement reaching definition for static field references.
   * <br>
   * There is no reaching definition analysis for field references in 
   * Jimple. It only provides the reaching analysis for simple local variables.
   * <br>This method is a modified implementation of Bit Vector algorithm in the book
   * "Advanced compiler design and implementation"
   * by Steven S. Muchnick, 1997. Page 218.
   * <br>
   * The result is store in {@link #staticReachDef staticReachDef}.
   */
private void reachingDefOfStaticField() {
	boolean change = true;
	int staticFieldDefSize = staticFieldDefStmtList.size();
	int stmtListSize = stmtList.size();
	BitSet reachIn[] = new BitSet[stmtListSize];
	BitSet preserve[] = new BitSet[stmtListSize];
	BitSet generate[] = new BitSet[stmtListSize];

	//initializing the three array
	//the sequence of array is the same as the sequence of index in stmtList
	//e.g. reachIn[i] indicate that the reach in set of the statement 
	//stmtList.get(i)
	for (int i = 0; i < stmtListSize; i++) {
		//the sequence of each BitSet is determined by the index sequence
		//of statciFieldDefStmt, e.g. reachIn[i].get(3) will get the value
		//for the 3rd stmt in staticFieldDefStmt (staticFieldDefStmt.get(3))
		reachIn[i] = new BitSet(staticFieldDefSize);
		preserve[i] = new BitSet(staticFieldDefSize);
		generate[i] = new BitSet(staticFieldDefSize);

		//initializing each element as 00000000
		//initializing preserve as 111111
		for (int j = 0; j < staticFieldDefSize; j++) {
			reachIn[i].clear(j);
			preserve[i].set(j);
			generate[i].clear(j);
		}
	}


	//calculate the preserve[i] and generate[i], those are invariant
	for (int i = 0; i < stmtListSize; i++) {
		Stmt stmt = (Stmt) stmtList.get(i);
		int stmtInStaticDef = getStaticDefIndexOf(stmt);
		if (stmtInStaticDef >= 0) {
			//stmt is in the staticDefStmtList
			generate[i].set(stmtInStaticDef);

			//collect all the defs which defines that the variable in 
			//stmtInStaticDef in to one set
			Set defSet = defsNotPreserves(stmtInStaticDef);

			//clear all the preserves such that define the variable in 
			//stmtInStaticDef
			for (Iterator k = defSet.iterator(); k.hasNext();) {
				int defNotPreserve = ((Integer) k.next()).intValue();
				preserve[i].clear(defNotPreserve);
			}
		}
	}
	while (change) {
		change = false;
		for (int i = 0; i < stmtListSize; i++) {
			Stmt stmt = (Stmt) stmtList.get(i);

			//calculate new reachIn[i], with its all predecessors
			BitSet newReachIn = null;
			List preds = stmtGraph.getPredsOf(stmt);
			for (int ppp = 0; ppp < preds.size(); ppp++) {
				Stmt predStmt = (Stmt)preds.get(ppp);
				int j= stmtList.indexOf(predStmt);
				//get preserve of preds j preserve[j]
				//get generate of preds j generate[j]
				BitSet tempReachIn = (BitSet) reachIn[j].clone();
				tempReachIn.and(preserve[j]);
				tempReachIn.or(generate[j]);
				if (ppp == 0)
					newReachIn = (BitSet) tempReachIn.clone();
				else
					newReachIn.or(tempReachIn);
			}
			if (newReachIn != null)
				//if newReachIn== null, this means
				//that preds.size() == 0
				if (!reachIn[i].equals(newReachIn)) {
					reachIn[i] = newReachIn;
					change = true;
				}
		}
	}
	staticReachDef = reachIn;
}
  /**
   * Remove, from <code>actualSuccs</code>, statements which are in exception handlers.
   * <p>
   * @param actualSuccs a list of {@link Stmt Stmt}.
   * @return a list{@link Stmt Stmt} without statements in any handler.
   * @see #getHandlerStmtSet()
   */ 
private List removeExceptionCaught(List actualSuccs) {
	List newSuccs = new ArrayList();
	Set trapHandlerStmtSet = getHandlerStmtSet();
	for (Iterator asuccsIt = actualSuccs.iterator(); asuccsIt.hasNext();) {
		Stmt succStmt = (Stmt) asuccsIt.next();
		if (!trapHandlerStmtSet.contains(succStmt))
			newSuccs.add(succStmt);
	}
	return newSuccs;
}
  /** 
   * Set {@link #interferenceMap interferenceMap}.
   * <p>
   * @param iterMap interference map.
   */
  public void setInterferenceMap(Map iterMap)
	{
	  interferenceMap = iterMap;
	}
  /**
   * Set {@link #lockAnalysis lockAnalysis}.
   * <p>
   * @param la an instance of LockAnalysis.
   */
  public void setLockAnalysis(LockAnalysis la)
	{
	  lockAnalysis = la;
	}
  /** 
   * Set {@link #readyDependMap readyDependMap}.
   * <p>
   * @param rm ready dependent map.
   */
  public void setReadyDependMap(Map rm)
	{
	  readyDependMap = rm;
	}
/**
   * Analyse data dependence for special invoke expressions.
   * <br>For example, there is a fragment of Jimple code like 
   * <p>
   *       [1] p = new Power;
   * <br>
   *       [2] specialinvoke p.[Power. &lt init &gt ():void]();
   * <br>
   *       [3] i = virtualinvoke p.[Power.power(int,int):int](5, 3);
   * <p>
   * Suppose slice it from statement [3]. Then apparently [3] is data dependent on
   * [1] by <code>p</code>. Statement [2] will not be in the slice, 
   * since there is no explicit definition in [2]. In fact, [2] is an initialization
   * for <code>p</code>. So it can be and should be considered as a definition of 
   * <code>p</code>. In other words, [1] and [2] together should be the definition of
   * <code>p</code> other than only [1].
   * So statement [3] is data dependent on both [1] and [2]. 
   * <br>
   * However, the traditional data dependence analysis dose not cover this condition,
   * since [2] is not a definition statement.
   * <p>
   * Given a statement with <code>new</code> expression, e.g., [1], this method
   * will search out the corresponding special &lt init &gt invoke for it, e.g., [2].
   */
private void specialInvokeDdAnalysis() {
	for (Iterator keyIt = stmtToDdOn.keySet().iterator(); keyIt.hasNext();) {
		Stmt keyStmt = (Stmt) keyIt.next();
		Set ddBoxesInMap = (Set) stmtToDdOn.get(keyStmt);
		Set ddBoxes = new ArraySet();
		ddBoxes.addAll(ddBoxesInMap);
		for (Iterator i = ddBoxes.iterator(); i.hasNext();) {
			DataBox ddBox = (DataBox) i.next();
			Stmt ddOnStmt = (Stmt) ddBox.getStmt();
			if (ddOnStmt instanceof DefinitionStmt) {
				DefinitionStmt defStmt = (DefinitionStmt) ddOnStmt;
				Value rightOp = defStmt.getRightOp();
				if (rightOp instanceof NewExpr) {
					NewExpr newExpr = (NewExpr) rightOp;
					String className = newExpr.getType().toString();
					SootClass newExprType = IndexMaps.lookupSootClassByName(className);
					Stmt specInvoke = null;
					if (newExprType == null)
						specInvoke = getSpecialInvokeStmtOf(ddBox, className);
					//newExpr.getBaseType() it's the same as getType()
					//as far as I know

					else
						specInvoke = getSpecialInvokeStmtOf(ddBox, newExprType);
					if (specInvoke.equals(keyStmt)) {
					} else {
						DataBox specInvokeDataBox = new DataBox(specInvoke, ddBox.getDependVar());
						//ddBox.getDependVar() must be the definition variable
						//like rn = new Object; and the specInvoke should be data
						//depend on rn

						//set the dataBox as a invoke init data box
						specInvokeDataBox.setToInvokeInit();
						ddBoxesInMap.add(specInvokeDataBox);
						ddBoxesInMap.remove(ddBox);
						ddBox.setToNewExprStmt();
						ddBoxesInMap.add(ddBox);
					}
				}
			} else {
			}
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (4/20/2001 11:33:42 AM)
 * @return java.util.BitSet
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public BitSet succDivergencePointsOf(Stmt stmt) {
	if (divergenceMap.containsKey(stmt))
		return (BitSet) divergenceMap.get(stmt);
	else
		return null;
}
  /*
  private boolean stmtNotIn(Stmt s, List defList)
	{
	  boolean returnValue = true;
	  Iterator listIt = defList.iterator();
	  while (listIt.hasNext())
	{
	  DataBox fields = (DataBox) listIt.next();
	  if (s == fields.getInterferStmt())
	    {
	      returnValue = false;
	      break;
	    }
	}
	  return returnValue;
	}
  */
  public String toString()
	{
	  String pdg = "Stmt to Postdomimators: " + stmtToPostdoms + "\n\n";
	  pdg += "Stmt to Control Depend nodes: " + stmtToCdOn + "\n\n";
	  pdg += "Stmt to Data Depend nodes: " + stmtToDdOn + "\n";
	  return pdg;
	}
}
