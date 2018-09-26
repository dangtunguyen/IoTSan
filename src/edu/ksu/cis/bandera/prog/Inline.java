package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import edu.ksu.cis.bandera.jext.LocalExpr;
import java.util.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;

import org.apache.log4j.Category;

/**
 * 
 * @author
 */
public class Inline {
	private static Category log = Category.getInstance(Inline.class);

	protected static Hashtable methodStack;
	protected static Hashtable inlinedMethods = new Hashtable();
	Hashtable locals;
	AnnotationManager annotationManager;
	public static Hashtable newLocal2original = new Hashtable();
	public static TypeTable typeTable;
	public static boolean isMethodCompiled;
	public static boolean syncGenEnabled;
	protected java.util.Hashtable stmtLocalTable;
	
	/* [Thomas, August 11, 2017]
	 * */
	private static int classIndex = 0;
	private static List<String> methNameList = new ArrayList<String>();
	private static String getMethIndex(String methName)
	{
		if(methNameList.contains(methName))
		{
			return "m" + methNameList.indexOf(methName);
		}
		else
		{
			methNameList.add(methName);
			return "m" + methNameList.indexOf(methName);
		}
	}
	
	/**
	 * Inline2 constructor comment.
	 */
	public Inline() {
		locals = new Hashtable();
	}
	/**
	 * @param method ca.mcgill.sable.soot.SootMethod
	 * @param annotationManager edu.ksu.cis.bandera.annotation.AnnotationManager
	 * @param sge boolean
	 */
	public static void inline(SootMethod method, AnnotationManager annotationManager, boolean sge) {

		syncGenEnabled = sge;
		methodStack = new Hashtable(); 
		RenameExpression.fields = (List) inlinedMethods.get(method);
		if (RenameExpression.fields == null) {
			RenameExpression.fields = new Vector();
			Inline in = new Inline();
			in.inlineMethod(method, annotationManager);
			inlinedMethods.put(method, RenameExpression.fields);
		}

	}

	/**
	 * @param method ca.mcgill.sable.soot.SootMethod
	 * @param annotationManager edu.ksu.cis.bandera.annotation.AnnotationManager
	 */
	protected void inlineMethod(SootMethod method, AnnotationManager annotationManager) {

		if ((stmtLocalTable = annotationManager.getStmtLocalTable()) == null) {
			annotationManager.setStmtLocalTable(stmtLocalTable = new Hashtable());
		}

		ca.mcgill.sable.util.List traps = new ca.mcgill.sable.util.VectorList();
		this.annotationManager = annotationManager;
		ca.mcgill.sable.util.List stmts = 
				inlineMethod(method, (List) null, (Local) null, 
						(Stmt) null, "", traps, method);
		JimpleBody body = (JimpleBody) method.getBody(Jimple.v());
		body.getStmtList().clear();
		body.getStmtList().addAll(stmts);
		body.getTraps().clear();
		body.getTraps().addAll(traps);
		body.getLocals().clear();
		java.util.Enumeration e = locals.elements();
		while (e.hasMoreElements()) {
			Local lcl = (Local) e.nextElement();
			body.addLocal(lcl);
		}
		method.storeBody(Jimple.v(), body);
		List l = new Vector();
		l.addAll(RenameExpression.fields);
		inlinedMethods.put(method, l);

	}

	/**
	 * @param method ca.mcgill.sable.soot.SootMethod
	 * @param annotationManager edu.ksu.cis.bandera.annotation.AnnotationManager
	 */
	protected ca.mcgill.sable.util.List inlineMethod(SootMethod method, List params,
			Value var, Stmt ret, String prefix,
			ca.mcgill.sable.util.List traps,
			SootMethod topMethod) {

		// test for recursion
		if (methodStack.containsKey(method)) {
			log.fatal("Recursive methods not supported: " + method);
			throw new RuntimeException("Recursive methods not supported: " + method);
		} else {
			methodStack.put(method, method);
		}

		/* [Thomas, July 26, 2017] */
		boolean isSpecialInitMethod = false;
		boolean isMethodCompiled = Inline.isMethodCompiled;
		Hashtable stmt2stmts = new Hashtable();
		RenameStatement rs;
		RenameExpression re;
		BuildAndStoreBody bd = new BuildAndStoreBody(Jimple.v(), new StoredBody(ClassFile.v()), 0);
		
		/* [Thomas, July 26, 2017] */
		if(method.getName().equals("<init>"))
		{
			String newPrefix = ((Local)params.get(0)).getName();
			
			isSpecialInitMethod = true;
			rs = new RenameStatement(method, newPrefix, var, params, ret, locals, stmt2stmts, topMethod);
			re = rs.getRenameExpr();
			re.setSpecialInitMethod();
		}
		else
		{
			rs = new RenameStatement(method, prefix, var, params, ret, locals, stmt2stmts, topMethod);
			re = rs.getRenameExpr();
		}
		
		try {
			if (!method.isBodyStored(Jimple.v()))
				bd.resolveFor(method);
		} catch (Exception e) {
			ca.mcgill.sable.util.LinkedList lstPatch = new ca.mcgill.sable.util.LinkedList();
			Type retType = method.getReturnType();
			Stmt myStmt = null;
			Jimple j = Jimple.v();
			if (retType instanceof VoidType) {
				myStmt = j.newReturnVoidStmt();
			} else if (retType instanceof IntType || 
					retType instanceof ByteType || 
					retType instanceof BooleanType || 
					retType instanceof CharType || 
					retType instanceof ShortType) {
				myStmt = j.newReturnStmt(IntConstant.v(0));
			} else if (retType instanceof LongType) {
				myStmt = j.newReturnStmt(LongConstant.v(0));
			} else if (retType instanceof DoubleType) {
				myStmt = j.newReturnStmt(DoubleConstant.v(0.0));
			} else if (retType instanceof FloatType) {
				myStmt = j.newReturnStmt(FloatConstant.v(0));
			} else {
				myStmt = j.newReturnStmt(NullConstant.v());
			}
			lstPatch.addLast(myStmt);

			// pop record of method on inline stack
			methodStack.remove(method);

			return lstPatch;
		}

		Inline.isMethodCompiled = CompilationManager.getCompiledClasses().get(method.getDeclaringClass().getName()) != null;
		JimpleBody body = (JimpleBody) method.getBody(Jimple.v());
		ca.mcgill.sable.util.List res = new ca.mcgill.sable.util.VectorList();
		int i;
		Stmt s;
		ca.mcgill.sable.util.List r;
		Set localSet = new HashSet();
		Stmt stmts[] = new Stmt[body.getStmtList().size()];
		System.arraycopy(body.getStmtList().toArray(), 0, stmts, 0, stmts.length);
		for (i = 0; i < stmts.length; i++) {
			s = stmts[i];

			InlinerChooseFixer.fix(s);

			if (s instanceof InvokeStmt) {
				InvokeStmt is = (InvokeStmt) s;
				if (!skipInline((InvokeExpr) is.getInvokeExpr())) {
					InvokeExpr expr = (InvokeExpr) is.getInvokeExpr();
					List nparams = new Vector();
					if (expr instanceof NonStaticInvokeExpr) {
						nparams.add(re.renameExpr((Local) ((NonStaticInvokeExpr) expr).getBase()));
					}
					for (int j = 0; j < expr.getArgCount(); j++)
						nparams.add(re.renameExpr(expr.getArg(j)));

					String nprefix;
					/* [Thomas, August 10, 2017]
					 * Remove expr.getMethod().getDeclaringClass()
					 * */
					if (expr.getMethod().getName().equals("<init>"))
					{
//						nprefix = prefix + expr.getMethod().getDeclaringClass() + 
//						"_const_";
						nprefix = prefix + classIndex + "_const_";
						classIndex++;
					}
					else
					{
//						nprefix = prefix + expr.getMethod().getName() + "_";
						nprefix = prefix + getMethIndex(expr.getMethod().getName()) + "_";
					}

					ca.mcgill.sable.util.List ntraps = new ca.mcgill.sable.util.VectorList();
					Stmt nret = Jimple.v().newNopStmt();

					//Begin Hierarchy Analysis  

					if (expr instanceof VirtualInvokeExpr || 
							expr instanceof InterfaceInvokeExpr) {
						// Get Tuple for this InvokeExpr
						SootClass methodDecl = expr.getMethod().getDeclaringClass();
						String methodName = expr.getMethod().getName();
						ca.mcgill.sable.util.List paramTypes = expr.getMethod().getParameterTypes();
						Type retType = expr.getMethod().getReturnType();
						Vector pairs = 
								InvokeResolver.getResolver().getPairs(methodDecl, 
										methodName, paramTypes, retType);

						// logging info
						log.debug("For expr " + expr.toString());
						log.debug("Pairs are:");
						for (int pi = 0; pi < pairs.size(); pi++)
							log.debug("  " + pairs.elementAt(pi));

						ClassMethodPair currentPair;
						r = new ca.mcgill.sable.util.VectorList();

						if (pairs.size() == 1) {
							currentPair = (ClassMethodPair) pairs.elementAt(0);
							/* [Thomas, August 10, 2017]
							 * Remove currentPair.getInvMethod().getDeclaringClass()
							 * */
//							r.addAll(inlineMethod(currentPair.getInvMethod(), 
//									new LinkedList(nparams), null, nret, 
//									nprefix + currentPair.getInvMethod().getDeclaringClass() + "_", 
//									ntraps, topMethod));
							r.addAll(inlineMethod(currentPair.getInvMethod(), 
									new LinkedList(nparams), null, nret, 
									nprefix + classIndex + "_", 
									ntraps, topMethod));
							classIndex++;

						} else {

							// Generate code as follows:
							//  if (this instanceof Tuple(1).1) goto l1
							//  if (this instanceof Tuple(2).1) goto l2
							//  error
							//  l1: nop
							//      new-this = (Tuple(1).1)this;
							//      ... inlined body jumps to ret ...
							//  l2: nop
							//      new-this = (Tuple(2).1)this;
							//      ... inlined body jumps to ret ...
							//  ret:
							//
							// This works because the tuples are in
							// leaves-first order from the inheritence
							// tree (so the cascading instanceof tests
							// find the least matching type).

							Vector ifRecord = new Vector();
							Stmt target;
							Local local = 
									Jimple.v().newLocal(nprefix + "INLINETMP", 
											ca.mcgill.sable.soot.BooleanType.v());
							locals.put(nprefix + "INLINETMP", local);
							if (typeTable.size() > 0)
								typeTable.put(local, ConcreteIntegralAbstraction.v());

							for (int pi = 0; pi < pairs.size(); pi++) {
								currentPair = (ClassMethodPair)pairs.elementAt(pi);
								target = Jimple.v().newNopStmt();
								ifRecord.addElement(target);
								Value ioe = Jimple.v().newInstanceOfExpr(
										(Local) nparams.get(0), 
										RefType.v(currentPair.getInvClass().getName()));
								if (typeTable.size() > 0)
									typeTable.put(ioe, ConcreteIntegralAbstraction.v());
								r.add(Jimple.v().newAssignStmt(local, ioe));

								Value ee = Jimple.v().newEqExpr(local, 
										IntConstant.v(1));
								if (typeTable.size() > 0)
									typeTable.put(ee, ConcreteIntegralAbstraction.v());
								r.add(Jimple.v().newIfStmt(ee, target));
							}
							for (int pi = 0; pi < pairs.size(); pi++) {
								currentPair = (ClassMethodPair)pairs.elementAt(pi);
								r.add((Stmt) ifRecord.elementAt(pi));

								// Need to downcast "this" to the "type" for this case
								// of the type test in order for the inlined method body
								// to have the correct run-time type.
								Local newThis =
										Jimple.v().newLocal(nprefix + 
												RefType.v(currentPair.getInvClass().getName()) +
												"_THISTMP1",
												RefType.v(currentPair.getInvClass().getName()));
								locals.put(nprefix + 
										RefType.v(currentPair.getInvClass().getName()) +
										"_THISTMP1", newThis);
								if (typeTable.size() > 0)
									typeTable.put(newThis, 
											RefType.v(currentPair.getInvClass().getName()));
								Value castExpr = Jimple.v().newCastExpr(
										(Local) nparams.get(0), 
										RefType.v(currentPair.getInvClass().getName()));
								r.add(Jimple.v().newAssignStmt(newThis, castExpr));
								LinkedList newparams = new LinkedList(nparams);
								newparams.removeFirst();
								newparams.addFirst(newThis);
								/* [Thomas, August 10, 2017]
								 * Remove currentPair.getInvMethod().getDeclaringClass()
								 * */
//								r.addAll(inlineMethod(currentPair.getInvMethod(), 
//										newparams, null, nret, 
//										nprefix + currentPair.getInvMethod().getDeclaringClass() + "_", 
//										ntraps, topMethod));
								r.addAll(inlineMethod(currentPair.getInvMethod(), 
										newparams, null, nret, 
										nprefix + classIndex + "_", 
										ntraps, topMethod));
								classIndex++;
							}
						}
					} else {
						r = inlineMethod(expr.getMethod(), nparams, null, 
								nret, nprefix, ntraps, topMethod);
					}
					r.add(nret);
					traps.addAll(ntraps);
				} else {
					r = new ca.mcgill.sable.util.VectorList();
					r.add(Jimple.v().newInvokeStmt(
							re.renameExpr(is.getInvokeExpr())));
				}
				stmt2stmts.put(s, r);
			} else if (s instanceof AssignStmt && 
					((AssignStmt) s).getRightOp() instanceof InvokeExpr) {
				InvokeExpr expr = (InvokeExpr) ((AssignStmt) s).getRightOp();
				if (!skipInline(expr)) {
					List nparams = new Vector();
					if (expr instanceof NonStaticInvokeExpr) {
						nparams.add(re.renameExpr(
								(Local)((NonStaticInvokeExpr) expr).getBase()));
					}
					for (int j = 0; j < expr.getArgCount(); j++)
						nparams.add(re.renameExpr(expr.getArg(j)));

					String nprefix;
					/* [Thomas, August 10, 2017]
					 * Remove currentPair.getInvMethod().getDeclaringClass()
					 * */
					if (expr.getMethod().getName().equals("<init>"))
					{
//						nprefix = prefix + expr.getMethod().getDeclaringClass() + "_const_";
						nprefix = prefix + classIndex + "_const_";
						classIndex++;
					}
					else
					{
//						nprefix = prefix + expr.getMethod().getName() + "_";
						nprefix = prefix + getMethIndex(expr.getMethod().getName()) + "_";
					}

					ca.mcgill.sable.util.List ntraps = 
							new ca.mcgill.sable.util.VectorList();

					Stmt nret = Jimple.v().newNopStmt();

					//Begin Hierarchy Analysis 

					if (expr instanceof VirtualInvokeExpr || 
							expr instanceof InterfaceInvokeExpr) {
						// Same deal for the right side of the assignment statement
						// Get Tuple for this InvokeExpr
						SootClass methodDecl = expr.getMethod().getDeclaringClass();
						String methodName = expr.getMethod().getName();
						ca.mcgill.sable.util.List paramTypes = 
								expr.getMethod().getParameterTypes();
						Type retType = expr.getMethod().getReturnType();
						Vector pairs = 
								InvokeResolver.getResolver().getPairs(
										methodDecl, methodName, 
										paramTypes, retType);

						// logging info
						log.debug("For expr " + expr.toString());
						log.debug("Pairs are:");
						for (int pi = 0; pi < pairs.size(); pi++)
							log.debug("  " + pairs.elementAt(pi));

						ClassMethodPair currentPair;
						r = new ca.mcgill.sable.util.VectorList();

						if (pairs.size() == 1) {
							currentPair = (ClassMethodPair) pairs.elementAt(0);
							/* [Thomas, August 10, 2017]
							 * Remove currentPair.getInvMethod().getDeclaringClass()
							 * */
//							r.addAll(inlineMethod(currentPair.getInvMethod(), 
//									new LinkedList(nparams), 
//									re.renameExpr(((AssignStmt) s).getLeftOp()), 
//									nret, nprefix + 
//									currentPair.getInvMethod().getDeclaringClass() + 
//									"_", ntraps, topMethod));
							r.addAll(inlineMethod(currentPair.getInvMethod(), 
									new LinkedList(nparams), 
									re.renameExpr(((AssignStmt) s).getLeftOp()), 
									nret, nprefix + 
									classIndex + 
									"_", ntraps, topMethod));
							classIndex++;

						} else {
							Vector ifRecord = new Vector();
							Stmt target;
							Local local = 
									Jimple.v().newLocal(nprefix + "INLINETMP", 
											ca.mcgill.sable.soot.BooleanType.v());
							locals.put(nprefix + "INLINETMP", local);
							if (typeTable.size() > 0)
								typeTable.put(local, 
										ConcreteIntegralAbstraction.v());
							for (int pi = 0; pi < pairs.size(); pi++) {
								currentPair = (ClassMethodPair)pairs.elementAt(pi);
								target = Jimple.v().newNopStmt();
								ifRecord.addElement(target);
								Value ioe = 
										Jimple.v().newInstanceOfExpr(
												(Local) nparams.get(0), 
												RefType.v(
														currentPair.getInvClass().getName()));
								if (typeTable.size() > 0)
									typeTable.put(ioe, ConcreteIntegralAbstraction.v());
								r.add(Jimple.v().newAssignStmt(local, ioe));

								Value ee = Jimple.v().newEqExpr(local, IntConstant.v(1));
								if (typeTable.size() > 0)
									typeTable.put(ee, ConcreteIntegralAbstraction.v());
								r.add(Jimple.v().newIfStmt(ee, target));
							}
							for (int pi = 0; pi < pairs.size(); pi++) {
								currentPair = (ClassMethodPair) pairs.elementAt(pi);
								r.add((Stmt) ifRecord.elementAt(pi));

								// Need to downcast "this" to the "type" for this case
								// of the type test in order for the inlined method body
								// to have the correct run-time type.
								Local newThis =
										Jimple.v().newLocal(nprefix + 
												RefType.v(currentPair.getInvClass().getName()) +
												"_THISTMP2",
												RefType.v(currentPair.getInvClass().getName()));
								locals.put(nprefix + 
										RefType.v(currentPair.getInvClass().getName()) +
										"_THISTMP2", newThis);
								if (typeTable.size() > 0)
									typeTable.put(newThis, 
											RefType.v(currentPair.getInvClass().getName()));
								Value castExpr = Jimple.v().newCastExpr(
										(Local) nparams.get(0), 
										RefType.v(currentPair.getInvClass().getName()));
								r.add(Jimple.v().newAssignStmt(newThis, castExpr));
								LinkedList newparams = new LinkedList(nparams);
								newparams.removeFirst();
								newparams.addFirst(newThis);

								/* [Thomas, August 10, 2017]
								 * Remove currentPair.getInvMethod().getDeclaringClass()
								 * */
//								r.addAll(inlineMethod(currentPair.getInvMethod(), 
//										newparams,
//										re.renameExpr(((AssignStmt) s).getLeftOp()), 
//										nret, nprefix + 
//										currentPair.getInvMethod().getDeclaringClass() + 
//										"_", ntraps, topMethod));
								r.addAll(inlineMethod(currentPair.getInvMethod(), 
										newparams,
										re.renameExpr(((AssignStmt) s).getLeftOp()), 
										nret, nprefix + 
										classIndex + 
										"_", ntraps, topMethod));
								classIndex++;
							}
						} 
					} else {
						r = inlineMethod(expr.getMethod(), nparams, 
								re.renameExpr(((AssignStmt) s).getLeftOp()), 
								nret, nprefix, ntraps, topMethod);
					}
					r.add(nret);
					traps.addAll(ntraps);
				} else {
					r = rs.renameStmt(s);
				}
				stmt2stmts.put(s, r);
			}
		}
		
		/* [Thomas, July 26, 2017] */
		if(isSpecialInitMethod)
		{
			for (i = 0; i < stmts.length; i++) {
				s = stmts[i];
				
				if((s instanceof JAssignStmt) || (s instanceof JReturnVoidStmt))
				{
					r = rs.renameStmt(s);
					res.addAll(r);
					ca.mcgill.sable.util.Iterator it = r.iterator();
					if (annotationManager != null) {
						while (it.hasNext()) {
							Stmt n = (Stmt) it.next();
							annotationManager.putInlineStmt(n, s, method);
							stmtLocalTable.put(n, localSet);
						}
					}
				}
			}
		}
		else
		{
			for (i = 0; i < stmts.length; i++) {
				s = stmts[i];
				r = rs.renameStmt(s);
				res.addAll(r);
				ca.mcgill.sable.util.Iterator it = r.iterator();
				if (annotationManager != null) {
					while (it.hasNext()) {
						Stmt n = (Stmt) it.next();
						annotationManager.putInlineStmt(n, s, method);
						stmtLocalTable.put(n, localSet);
					}
				}
			}
		}
		ca.mcgill.sable.util.Iterator it = body.getTraps().iterator();
		while (it.hasNext()) {
			Trap t = (Trap) it.next();
			traps.add(Jimple.v().newTrap(t.getException(), (Stmt) rs.renameStmt((Stmt) t.getBeginUnit()).toArray()[0], (Stmt) rs.renameStmt((Stmt) t.getEndUnit()).toArray()[0], (Stmt) rs.renameStmt((Stmt) t.getHandlerUnit()).toArray()[0]));
		}
		java.util.Iterator it2 = locals.values().iterator();
		while (it2.hasNext()) {
			localSet.add(new LocalExpr(method, (Local) it2.next()));
		}
		Inline.isMethodCompiled = isMethodCompiled;

		// pop record of method on inline stack
		methodStack.remove(method);

		return res;
	}
	/**
	 * Defines the classes for which methods should not be inlined, i.e.,
	 * <ul>
	 * <li> Java library calls
	 * <li> IoTSan calls
	 * <li> Abstraction calls
	 * <li> SyncGen calls
	 * </ul>
	 */ 
	public static boolean skipInline(InvokeExpr ie) {
		boolean skip = false;
		String className = ie.getMethod().getDeclaringClass().toString();
		String methodName = ie.getMethod().getName().toString();

		/*if(className.equals("java.util.Random") &&
	  (!methodName.startsWith("nextInt") || methodName.startsWith("nextBoolean"))) {
	  // java.util.Random calls except nextInt and nextBoolean
	  skip = true;
	  }
	  else*/ if(className.startsWith("java")) {
		  // java lib calls
		  skip = true;
	  }
	  else if(className.startsWith("GICluster$") && syncGenEnabled) {
		  // syncgen calls
		  skip = true;
	  }
	  else if(className.startsWith("IoTSan") && !methodName.startsWith("randomInt")) {
		  // bandera calls except randomInt
		  skip = true;
	  }
	  else if(className.equals("edu.ksu.cis.bandera.abstraction.Abstraction")) {
		  // abstraction calls
		  skip = true;
	  }
	  else {
	  }

	  return(skip);
	  /*
	  return className.startsWith("java") ||
	  className.equals("IoTSan") ||
          className.equals("edu.ksu.cis.bandera.abstraction.Abstraction") ||
          (className.startsWith("GICluster$") && syncGenEnabled);
	   */
	}
	public static void main(String args[]) {
		Object m[];
		int i;
		SootClass cls;
		SootClassManager cm = new SootClassManager();
		int j = 0;
		for (j = 0; j < args.length / 2; j++) {
			cls = cm.getClass(args[j * 2]);
			cls.resolveIfNecessary();
			m = cls.getMethods().toArray();
			for (i = 0; i < m.length && !((SootMethod) m[i]).getName().equals(args[j * 2 + 1]); i++);
			BuildAndStoreBody bd = new BuildAndStoreBody(Jimple.v(), new StoredBody(ClassFile.v()), 0);
			if (i == m.length) {
				System.exit(0);
			}
			SootMethod method = (SootMethod) m[i];
			bd.resolveFor(method);
			method.getBody(Jimple.v()).printTo(new java.io.PrintWriter(System.out, true), 0);
			Inline.inline(method, null, false);
			method.getBody(Jimple.v()).printTo(new java.io.PrintWriter(System.out, true), 0);
			Transformations.cleanupCode((JimpleBody) method.getBody(Jimple.v()));
			method.getBody(Jimple.v()).printTo(new java.io.PrintWriter(System.out, true), 0);
		}
	}
}
