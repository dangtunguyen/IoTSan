/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000, 2001, 2002                              *
 * Venkatesh Prasad Ranganath (rvprasad@cis.ksu.edu)                 *
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

package edu.ksu.cis.bandera.bofa;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.jimple.EnterMonitorStmt;
import ca.mcgill.sable.soot.jimple.ExitMonitorStmt;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.log4j.Category;

/*
 * Demo.java
 * $Id: Demo.java,v 1.4 2002/07/12 22:45:14 rvprasad Exp $
 */

/**
 * This class tests the implementation of BOFA and provides an example of how to use BOFA.
 *
 * @author <a href="http://www.cis.ksu.edu/~rvprasadVenkatesh Prasad Ranganath</a>

 * @version $Name:  $($Revision: 1.4 $)
 */
class Demo {

	/**
	 * Provides the logging capability through log4j.
	 *
	 */
	private static Category cat;

	static {
		cat = Category.getInstance(Demo.class.getName());
	}

	/**
	 * Entry point to the demo.
	 *
	 * @param args mode and arguments.  In case the mode is <i>-jjjc</i> the arguments need to be all
	 * the java source files to be used in compilation.  In case the mode is <i>-soot</i> the
	 * arguments need to be the names of the classes that need to be analyzed.  If <i>-dump</i> is
	 * specified then the * jimple representation of all the classes analyzed by BOFA will be dumped.
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java edu.ksu.cis.bandera.bofa.Demo -{jjjc|soot} <classlist>");
			System.exit(0);
		}
		if (args[0].equals("-jjjc")) {
			jjjc(args);
		} else if (args[0].equals("-soot")) {
			soot(args);
		} // end of else
	} // end of main ()

	/**
	 * Uses JJJC to generate the jimple representation of the given source files.
	 *
	 * @param args names of java source files.
	 */
	static void jjjc(String[] args) {
		LinkedList classes = new LinkedList();
		for (int i = 1; i < args.length; i++) {
			classes.addLast(args[i].trim());
		}
		BOFA.compile(".", classes, new LinkedList());
		BOFA.dumpFile(".");
		BOFA.setAnalysis(Analysis.init());
		BOFA.setMode(BOFA.FLOW_INSENSITIVE);
		BOFA.analyze();
		analyze(Util.convert("ca.mcgill.sable.util.VectorList", CompilationManager.getCompiledClasses().values()));
	} // end of main ()

	/**
	 * Uses Soot to generate the jimple representation of the given classes.
	 *
	 * @param args names of the classes.
	 */
	static void soot(String[] args) {
		SootClassManager scm = new SootClassManager();
		LinkedList storedclasses = new LinkedList();
		java.io.PrintWriter p = new java.io.PrintWriter(System.out, true);
		BuildAndStoreBody basb = new BuildAndStoreBody(Jimple.v(), new StoredBody(ClassFile.v()));
		for (int i = 1; i < args.length; i++) {
			SootClass sc = scm.getClass(args[i]);
			sc.resolveIfNecessary();
			sc.printTo(basb, p);
			storedclasses.add(sc);
			while (sc.hasSuperClass() && !sc.getSuperClass().getName().equals("java.lang.Object")) {
				sc = sc.getSuperClass();
				sc.resolveIfNecessary();
				sc.printTo(basb, p);
				storedclasses.add(sc);
			} // end of while (sc.hasSuperClass())
		}
		BOFA.setAnalysis(Analysis.init());
		BOFA.setMode(BOFA.FLOW_INSENSITIVE);
		BOFA.analyze(scm, storedclasses);
		BOFA.dumpFile(".");
		analyze(storedclasses);
	}

	/**
	 * Analyzes the given collection of classes.
	 *
	 * @param storedClasses a collection of <code>SootClass</code>es representing the classes to be
	 * analyzed.
	 */
	static void analyze(Collection storedClasses) {
		SootClass sc;
		SootMethod sm;
		TempExpr expr = new TempExpr();
		TempStmt stmt = new TempStmt(expr);
		Analysis a = Analysis.init();

		if (storedClasses == null || storedClasses.size() == 0) {
			System.out.println("You need to compile before you do Flow Analysis");
			return;
		}

		for (Iterator e = storedClasses.iterator(); e.hasNext();) {
			Object o = e.next();
			if (o instanceof java.lang.String) {
				cat.warn("SootClass not available.  -- " + o.getClass());
				continue;
			} // end of if (o instanceof java.lang.String)
			sc = (SootClass) o;
			System.out.println("\n\n#####################################################################");
			System.out.println("PROCESSING CLASS " + sc);
			for (Iterator methods = sc.getMethods().iterator(); methods.hasNext();) {
				sm = (SootMethod) methods.next();
				StmtList stmts;
				System.out.println("\n\n**************************************************************************");
				try {
					stmts = ((JimpleBody) sm.getBody(Jimple.v())).getStmtList();
				} catch (RuntimeException ee) {
					cat.warn(ee.getMessage());
					continue;
				} // end of try-catch

				if (!FA.isReachable(sm)) {
					System.out.println("PROCESSING METHOD " + sm + " :	UNREACHABLE");
					cat.debug("Unreachable method: " + sm);
					continue;
				}
				System.out.println("PROCESSING METHOD " + sm);
				expr.init(sm);
				stmt.init(sm, stmts);
				for (Iterator i = stmts.iterator(); i.hasNext();) {
					Stmt s = (Stmt) i.next();
					System.out.println("==================================================================");
					System.out.println("PROCESSING STMT " + s);
					stmt.build(s);
				} // end of for ()
				System.out.println("CALLEE LIST FOR " + sm + ": ");
				for (Iterator i = a.getCallees(sm).iterator(); i.hasNext();) {
					Analysis.ExprStmtMethodTriple ex = (Analysis.ExprStmtMethodTriple) i.next();
					System.out.println(ex.getExpr() + "@" + ex.getStmt() + "[" + ex.getSootMethod() + "]");
				}
				System.out.println("CALLER LIST FOR " + sm + ": ");
				for (Iterator i = a.getCallers(sm).iterator(); i.hasNext();) {
					Analysis.ExprStmtMethodTriple ex = (Analysis.ExprStmtMethodTriple) i.next();
					System.out.println(ex.getExpr() + "@" + ex.getStmt() + "[" + ex.getSootMethod() + "]");
				}
			} // end of for ()
		}
		calllist(BOFA.getRootSootMethods());
	}

	/**
	 * Displays the call graph in a format suitable to generate printable call graph.
	 *
	 * @param roots are the methods from where the call graph should start.
	 */
	static void calllist(Collection roots) {
		Analysis analyser = Analysis.init();
		Set processed = new ArraySet();
		LinkedList tobeprocessed = new LinkedList();
		Set calledprocessed = new ArraySet();
		File file = new File(roots.hashCode() + "1.cg");
		for (int i = 2; file.exists(); i++)
			file = new File(roots.hashCode() + i + ".cg");
		PrintWriter pw = null;
		try {
			file.createNewFile();
			pw = new PrintWriter(new FileOutputStream(file), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Outputting call graph...");
		pw.println("digraph G { ");
		for (Iterator iter = roots.iterator(); iter.hasNext();) {
			SootMethod root = (SootMethod) iter.next();
			do {
				if (!processed.contains(root)) {
					tobeprocessed.addAll(tobeprocessed.size(), analyser.getCallees(root));
					Set called = analyser.getCallees(root);
					for (Iterator i = called.iterator(); i.hasNext();) {
						Analysis.ExprStmtMethodTriple estm = (Analysis.ExprStmtMethodTriple) i.next();
						SootMethod sm = estm.getSootMethod();
						if (!calledprocessed.contains(sm)) {
							calledprocessed.add(sm);
							pw.println("\"" + root + "\"" + " -> " + "\"" + sm + "\"");
						} // end of if
					} // end of for
					calledprocessed.clear();
				} // end of if (!processed.contains(root))
				processed.add(root);
				if (!tobeprocessed.isEmpty())
					root = ((Analysis.ExprStmtMethodTriple) tobeprocessed.removeFirst()).getSootMethod();
			} while (!tobeprocessed.isEmpty()); // end of while (notOver)
		}
		pw.println("}");
		pw.close();
	}
}

/**
 * A jimple expression walker.
 *
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.4 $)
 */
class TempExpr extends AbstractJimpleValueSwitch {
	/**
	 * Method enclosing the expression being walked.
	 *
	 */
	private SootMethod sm;

	/**
	 * The analyzer object.
	 *
	 */
	Analysis analyser;

	/**
	 * Creates a new <code>TempExpr</code> instance.
	 *
	 */
	TempExpr() {
		analyser = Analysis.init();
	}

	/**
	 * Performs initialization before proceeding with walking.
	 *
	 * @param sm the method enclosing the expression being walked.
	 */
	void init(SootMethod sm) {
		this.sm = sm;
	}

	/**
	 * Triggers the walk.
	 *
	 * @param v the expression to be walked on.
	 */
	void build(Value v) {
		v.apply(this);
	}

	/**
	 * Handles the case when <code>Local</code>s are encountered.
	 *
	 * @param v object representing the local.
	 */
	public void caseLocal(Local v) {
		print(v, "LOCALREFERENCE");
	}

	/**
	 * Handles the case when <code>caseArrayRef</code>s are encountered.
	 *
	 * @param v object representing the array reference.
	 */
	public void caseArrayRef(ArrayRef v) {
		build(v.getBase());
		print(v, "ARRAYREFERENCE");
	}

	/**
	 * Handles the case when <code>caseInstanceFieldRef</code>s are encountered.
	 *
	 * @param v object representing the instance field reference.
	 */
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		build(v.getBase());
		print(v, "INSTANCEFIELDREFERENCE");
	}

	/**
	 * Handles the case when <code>caseParameterRef</code>s are encountered.
	 *
	 * @param v object representing parameter reference.
	 */
	public void caseParameterRef(ParameterRef v) {
		print(v, "PARAMETERREFERENCE");
	}

	/**
	 * Handles the case when <code>caseStaticFieldRef</code>s are encountered.
	 *
	 * @param v object representing static field reference.
	 */
	public void caseStaticFieldRef(StaticFieldRef v) {
		print(v, "STATICFIELDREFERENCE");
	}

	/**
	 * Handles the case when <code>caseThisRef</code>s are encountered.
	 *
	 * @param v object representing a reference to <code>this</code> variable.
	 */
	public void caseThisRef(ThisRef v) {
		print(v, "THISREFERENCE");
	}

	/**
	 * Handles the case when <code>caseVirtualInvokeExpr</code>s are encountered.
	 *
	 * @param v object representing a virtual invoke expression.
	 */
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		print(v, "VIRTUALMETHODINVOCATION");
	}

	/**
	 * Handles the case when <code>caseInterfaceInvokeExpr</code>s are encountered.
	 *
	 * @param v object representing a interface invoke expression.
	 */
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		print(v, "INTERFACEMETHODINVOCATION");
	}

	/**
	 * Handles the case when <code>caseSpecialInvokeExpr</code>s are encountered.
	 *
	 * @param v object representing a special invoke expression.
	 */
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		print(v, "SPECIALMETHODINVOCATION");
	}

	/**
	 * Handles cases which we donot intend to handle at this time.
	 *
	 * @param obj object representing anything.
	 */
	public void defaultCase(Object obj) {
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("TEST: We donot handle " + obj + ".");
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	}

	/**
	 * A generic method to print all information available from the analysis about the given
	 * expression.
	 *
	 * @param v the expression of interest.
	 * @param s a parameterizing string.  This just is printed along with the other information.
	 */
	void print(Value v, String s) {
		Collection values = null;
		boolean b = true;
		Analysis.StmtMethodPair smp = null;

		if (!(v instanceof InvokeExpr)) {
			b = analyser.nullReference(v, sm);
			values = analyser.referenceValueSet(v, sm);
		} // end of if !(v instanceOf InvokeExpr)
		if (v instanceof NonStaticInvokeExpr) {
			values = analyser.invokeExprResolution((NonStaticInvokeExpr) v, sm);
		} // end of if (v instanceof NonStaticInvokeExpr)

		System.out.println("-------------------------------------");
		System.out.println(s + " expression:" + v);
		System.out.println("# VALUES :" + values.size());

		for (Iterator t = values.iterator(); t.hasNext();) {
			Object i = t.next();
			if (i instanceof Analysis.ExprStmtMethodTriple) {
				Analysis.ExprStmtMethodTriple a = (Analysis.ExprStmtMethodTriple) i;
				System.out.println(
					"{VALUE: "
						+ a.getExpr()
						+ ", STMT: "
						+ a.getStmtMethodPair().getStmt()
						+ ", METHOD: "
						+ a.getStmtMethodPair().getSootMethod()
						+ "}");
			} else {
				System.out.println("{VALUE: " + i + ", TYPE: " + i.getClass() + "}");
			} // end of else
		} // end of for ()
		if (b) {
			System.out.println("NULLNESS: Maybe.");
		} // end of if ()
		else {
			System.out.println("NULLNESS: Never.");
		} // end of else

		if (v instanceof Local || v instanceof Ref) {
			System.out.println("DEFINE sites:");
			for (Iterator i = analyser.getDefs(v, sm).iterator(); i.hasNext();) {
				smp = (Analysis.StmtMethodPair) i.next();
				System.out.println(smp.getSootMethod() + "{" + smp.getStmt() + "}");
			} // end of for
			System.out.println("USE sites:");
			for (Iterator i = analyser.getUses(v, sm).iterator(); i.hasNext();) {
				smp = (Analysis.StmtMethodPair) i.next();
				System.out.println(smp.getStmt() + " in " + smp.getSootMethod());
			} // end of for
		} // end of if (v instanceof Local)
	}
}

/**
 * A jimple statement walker.
 *
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 * @version $Name:  $($Revision: 1.4 $)
 */
class TempStmt extends AbstractStmtSwitch {
	/**
	 * Method enclosing the statement being walked.
	 *
	 */
	private SootMethod sm;

	/**
	 * Statement graph of the given statement list.
	 *
	 */
	private BriefStmtGraph bsg;

	/**
	 * The expression walker.
	 *
	 */
	private TempExpr expr;

	/**
	 * Provides logging facility through log4j.
	 *
	 */
	private Category cat;

	/**
	 * Creates a new <code>TempStmt</code> instance.
	 *
	 * @param expr the expression walker.
	 */
	TempStmt(TempExpr expr) {
		this.expr = expr;
		cat = Category.getInstance(TempStmt.class.getName());
	}

	/**
	 * Initializes the object with a method and the statement list in the method in which the
	 * statements that will be analyzed are present.
	 *
	 * @param sm the method enclosing the statements to be analyzed.
	 * @param stmts the list of statements in the enclosing method.
	 */
	public void init(SootMethod sm, StmtList stmts) {
		this.sm = sm;
		this.bsg = new BriefStmtGraph(stmts);
		cat.debug("Reached: " + sm);
	}

	/**
	 * Handles the case when <code>caseAssignStmt</code>s are encountered.
	 *
	 * @param v object representing an assignment statement in Jimple.
	 */
	public void caseAssignStmt(AssignStmt v) {
		cat.debug("Assignment: " + v + " | Class of LHS: " + v.getLeftOp().getClass());
		expr.build(v.getLeftOp());
		expr.build(v.getRightOp());
		cat.debug("Leaving AssignStmt");
	}

	/**
	 * Handles the case when <code>caseIdentityStmt</code>s are encountered.
	 *
	 * @param v object representing an identity statement in Jimple.
	 */
	public void caseIdentityStmt(IdentityStmt v) {
		cat.debug("IdentityStmt: " + v);
		expr.build(v.getLeftOp());
		expr.build(v.getRightOp());
		cat.debug("Leaving IdentityStmt");
	}

	/**
	 * Handles the case when <code>caseIfStmt</code>s are encountered.
	 *
	 * @param v object representing an if statement in Jimple.
	 */
	public void caseIfStmt(IfStmt v) {
		cat.debug("IfStmt: " + v);
		expr.build(v.getCondition());
		cat.debug("Leaving IfStmt");
	}

	/**
	 * Handles the case when <code>caseInvokeStmt</code>s are encountered.
	 *
	 * @param v object representing a case statement in Jimple.
	 */
	public void caseInvokeStmt(InvokeStmt v) {
		cat.debug("InvokeStmt: " + v);
		expr.build(v.getInvokeExpr());
		cat.debug("Leaving InvokeStmt");

	}

	/**
	 * Handles the case when <code>caseReturnStmt</code>s are encountered.
	 *
	 * @param v object representing a return statement in Jimple.
	 */
	public void caseReturnStmt(ReturnStmt v) {
		cat.debug("ReturnStmt: " + v);
		expr.build(v.getReturnValue());
		cat.debug("Leaving ReturnStmt");
	}

	public void caseEnterMonitorStmt(EnterMonitorStmt v) {
		cat.debug("EnterMonitorStmt: " + v);
		expr.build(v.getOp());
		cat.debug("Leaving EnterMonitorStmt");
	}

	public void caseExitMonitorStmt(ExitMonitorStmt v) {
		cat.debug("ExitMonitorStmt: " + v);
		expr.build(v.getOp());
		cat.debug("Leaving ExitMonitorStmt");
	}

	/**
	 * Triggers the walk.
	 *
	 * @param v the statement to be walked.
	 */
	void build(Stmt v) {
		v.apply(this);
	}

	/**
	 * Handles statements which we do not intend to handle at this time.
	 *
	 * @param obj object representing entities we donot handle.
	 */
	public void defaultCase(Object obj) {
		cat.debug("We do not handle " + obj.getClass().toString());
	}
}
