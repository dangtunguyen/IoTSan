package edu.ksu.cis.bandera.bui;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000, 2001 Roby Joehanes (robbyjo@cis.ksu.edu)      *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import java.util.*;
import javax.swing.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.gui.*;
import edu.ksu.cis.bandera.abstraction.options.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;

import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.bir.*;
import edu.ksu.cis.bandera.birc.*;
import edu.ksu.cis.bandera.birc.PredicateSet;
import edu.ksu.cis.bandera.birp.*;
import edu.ksu.cis.bandera.bofa.BOFA;
import edu.ksu.cis.bandera.bui.counterexample.*;
import edu.ksu.cis.bandera.dspin.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.decompiler.*;
import edu.ksu.cis.bandera.jjjc.optimizer.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.pdgslicer.*;
import edu.ksu.cis.bandera.pdgslicer.dependency.*;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import edu.ksu.cis.bandera.specification.Compiler;
import edu.ksu.cis.bandera.specification.*;
import edu.ksu.cis.bandera.specification.assertion.*;

//import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet;

import edu.ksu.cis.bandera.specification.pattern.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.node.*;
import edu.ksu.cis.bandera.report.*;
import edu.ksu.cis.bandera.smv.*;
import edu.ksu.cis.bandera.spin.*;
import edu.ksu.cis.bandera.prog.*;
import edu.ksu.cis.bandera.util.*;

import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.jjjc.symboltable.Name;

import edu.ksu.cis.bandera.specification.predicate.PredicateSliceInterestCollector;
import edu.ksu.cis.bandera.specification.predicate.PredicateProcessor;
import edu.ksu.cis.bandera.specification.predicate.PredicateUpdate;

import edu.ksu.cis.bandera.sessions.SessionManager;
import edu.ksu.cis.bandera.sessions.SessionNotFoundException;
import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.Specification;
import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.sessions.BIROptions;
import edu.ksu.cis.bandera.sessions.Predicate;
import edu.ksu.cis.bandera.sessions.Quantification;
import edu.ksu.cis.bandera.sessions.Assertion;
import edu.ksu.cis.bandera.sessions.TemporalProperty;
import edu.ksu.cis.bandera.sessions.Abstraction;
import edu.ksu.cis.bandera.sessions.AbstractionConverter;

import edu.ksu.cis.bandera.checker.OptionsFactory;
import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.spin.SpinOptions;
import edu.ksu.cis.bandera.checker.dspin.DSpinOptions;
import edu.ksu.cis.bandera.checker.jpf.JPFOptions;

import org.apache.log4j.Category;


/**
 * The Driver provides the logic for running a particular session in Bandera.  This
 * class is used by both the command line interface and the graphical interface (BUI).
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Roby Joehanes &lt;robbyjo@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.33 $ - $Date: 2003/06/19 15:32:18 $
 */
public class Driver extends Thread {

	private static final Category log = Category.getInstance(Driver.class);

	private Hashtable importedAssertion = new Hashtable();
	private Hashtable importedAssertionSet = new Hashtable();
	private Hashtable predNodeGrimpTable = new Hashtable();
	private HashSet placeHolders = new HashSet();
	private static HashSet predicates = new HashSet();
	private static HashSet quantifierSliceInterests = new HashSet();
	private edu.ksu.cis.bandera.bir.TransSystem sys;
	private static boolean isCodeChanged = false;
	private boolean isRecompiled = false;
	private static boolean isNotRoboto = true;
	private StringBuffer execSummary = new StringBuffer();
	private static String originalClasspath = System.getProperty("java.class.path");
	private static String originalUserDir = System.getProperty("user.dir");
	private static boolean isDumped = false;
	private static boolean genReport = false;
	private static boolean isDoingBir = false;
	private TypeTable typeTable = new TypeTable();
	private Hashtable options; // **
	private LinkedList qList = new LinkedList(); // robbyjo's patch: To store class/quantified vars info.
	private static boolean isGUI = true;
	private ReportManager rm = new ReportManager();
	private static String expectedResultPath;
	private StringBuffer currentCounterExample;

	private Trail trail = null;

	/**
	 * Create a new Driver.
	 */
	public Driver() {
		this(null);
	}

	/**
	 * Create a new Driver using the given Trail.
	 */
	public Driver(Trail t) {
		trail = t;
		predicates = new HashSet();
		quantifierSliceInterests = new HashSet();
	}

	/**
	 *
	 */
	private void dump(String phase) {
		String path = getTempDir(phase);
		if (!BanderaUtil.mkdirs(path)) {
			String errmsg = "***Can't make temporary directory for dumping jimple!";
			System.out.println(errmsg);
			if (isGUI && isNotRoboto)
				JOptionPane.showMessageDialog(null,errmsg, "Error", JOptionPane.ERROR_MESSAGE);
			else execSummary.append(errmsg);
			return;
		}

		StoredBody bd = new StoredBody(ca.mcgill.sable.soot.jimple.Jimple.v());
		for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements();) {
			SootClass sc = (SootClass) e.nextElement();
			String className = sc.getName();
			try {
				java.io.File jimpFile = new java.io.File(path + File.separator + className + ".jimple");
				java.io.FileOutputStream jimpOut = new java.io.FileOutputStream(jimpFile);
				sc.printTo(bd, new java.io.PrintWriter(jimpOut, true));
				jimpOut.close();
			}
			catch (java.io.IOException ex) {
				if (isGUI && isNotRoboto)
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				else execSummary.append("***Can't dump! "+ex.getMessage()+"\n");
			}
		}
	}

	/**
	 *
	 */
	private void dumpJimpleTrace(JimpleTrace trace) {
		AnnotationManager am = CompilationManager.getAnnotationManager();
		Stmt[] stmts = trace.getStatements();
		Annotation prevAnn = null;
		for (int i = 0; i < stmts.length; i++) {
			try {
				Annotation a = am.getContainingAnnotation(stmts[i]);
				if (prevAnn != a) {
					prevAnn = a;
					System.out.println("****************************************");
					System.out.println("****************************************");
					System.out.println("Step #: " + i);
					SootMethod sm = ((MethodDeclarationAnnotation) am.getMethodAnnotationContainingAnnotation(a)).getSootMethod();
					System.out.println("Control at method: " + sm + ", stmt: " + stmts[i]);
					System.out.println("Annotation: " + a);
					//JimpleStore store = trace.getStore(i);
					//store.print();
				}
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 *
	 * @param tempSuffix java.lang.String
	 */
	private String generateJava(String tempSuffix) {
		String path = getTempDir(tempSuffix);
		if (!BanderaUtil.mkdirs(path)) {
			String errmsg = "***Can't make temporary directory for dumping jimple!";
			System.out.println(errmsg);
			if (isGUI && isNotRoboto)
				JOptionPane.showMessageDialog(null,errmsg, "Error", JOptionPane.ERROR_MESSAGE);
			else execSummary.append(errmsg);
			return "";
		}

		Decompiler decompiler = new Decompiler(CompilationManager.getCompiledClasses(), CompilationManager.getAnnotationManager(), path, "java");
		String output = decompiler.decompile();
		rm.addReport("Decompiler_"+tempSuffix, new DecompilerReport("decompiler_"+tempSuffix, output));
		if (tempSuffix.equals("JPF"))
			CompilationManager.getAnnotationManager().setFilenameLinePairAnnotationTable(decompiler.getLineToAnnotationTable());

		return (new File(path)).getAbsolutePath();
	}


	/**
	 *
	 * @return ca.mcgill.sable.soot.SootClass[]
	 */

	private SootClass[] getClassesForBIRC() {
		HashSet set = getMethodsForInliner();
		//	SootClass[] result = new SootClass[set.size()];
		java.util.List<SootClass> scList = new ArrayList<SootClass>();

		for (Iterator j = set.iterator(); j.hasNext();) {
			/* [Thomas, May 4, 2017]
			 * One SootClass may have several event handler methods,
			 * thus we need to avoid add duplicate classes to result
			 * */
			if(!scList.contains(((SootMethod) j.next()).getDeclaringClass()))
			{
				scList.add(((SootMethod) j.next()).getDeclaringClass());
			}
			//	    result[i] = ((SootMethod) j.next()).getDeclaringClass();
		}
		SootClass[] result = new SootClass[scList.size()];
		int i = 0;
		for(SootClass sc : scList)
		{
			result[i++] = sc;
		}

		return result;
	}

	/**
	 *
	 * @return java.util.HashSet
	 */
	private HashSet getMethodsForInliner() {
		HashSet result = new HashSet();
		//	ca.mcgill.sable.util.LinkedList args = new ca.mcgill.sable.util.LinkedList();
		/* [Thomas, April 29, 2017]
		 * We will accept main function with the following format:
		 * public void main() {}
		 * */
		//	args.add(ca.mcgill.sable.soot.ArrayType.v(RefType.v("java.lang.String"), 1));
		ca.mcgill.sable.util.LinkedList args2 = new ca.mcgill.sable.util.LinkedList();

		HashSet rClasses = IdentifyRunnableClasses.identify(CompilationManager.getCompiledClasses().elements());

		for (Iterator rcIt = rClasses.iterator(); rcIt.hasNext();) {
			SootClass sc = (SootClass) rcIt.next();
			//	    if (sc.declaresMethod("main", args)) {
			if (SpinUtil.containEvtHandlerMethod(sc)) {
				/* [Thomas, May 4, 2017]
				 * Add all methods ending with "EvtHandler"
				 * */
				//	    	result.add(sc.getMethod("main", args));
				result.addAll(SpinUtil.getEvtHandlerMethods(sc));
			} else if (sc.declaresMethod("run", args2)) {
				result.add(sc.getMethod("run", args2));
			}
		}

		return result;
	}

	/**
	 *
	 * @return java.util.Vector
	 */
	public static Vector getSliceInterests() {
		Vector v = (Vector) AssertionSliceInterestCollector.collect(new Vector());
		v.addAll(PredicateSliceInterestCollector.collect(predicates));
		v.addAll(quantifierSliceInterests);

		return v;
	}

	/**
	 *
	 * @return ca.mcgill.sable.soot.SootClass[]
	 */
	private SootClass[] getSootClassesForBIRC() {
		Hashtable table = CompilationManager.getCompiledClasses();
		Vector v = new Vector();
		//v.add(CompilationManager.getMainSootClass());
		for (Enumeration e = table.elements(); e.hasMoreElements();) {
			SootClass sc = (SootClass) e.nextElement();
			//		    if (sc.declaresMethod("run")) {
			/* [Thomas, May 8, 2017]
			 * Add all classes having _EvtHandler methods
			 * */
			if (SpinUtil.containEvtHandlerMethod(sc) && !v.contains(sc)) {
				v.add(sc);
				log.debug("adding class " + sc.getName() + " to the list of SootClasses for BIRC.");
			}
		}
		log.debug("number of SootClasses for BIRC: " + v.size());
		SootClass[] result = new SootClass[v.size()];
		int i = 0;
		for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
			result[i] = (SootClass) e.nextElement();
		}
		return result;
	}

	public String getSummary() { return execSummary.toString(); }

	private String getTempDir(String suffix) {
		SessionManager sm = SessionManager.getInstance();
		Session activeSession = sm.getActiveSession();
		String s = "";
		if(activeSession != null) {
			s = System.getProperty("user.dir") + File.separator + "temp$" + activeSession.getName();
		}
		else {
			s = System.getProperty("user.dir") + File.separator + "temp$NoActiveSession";
		}

		if (suffix == null || suffix.equals("")) return s;
		return s + File.separator + suffix;
	}

	/**
	 *
	 */
	public void initAssertions() throws Exception {
		for (Iterator i = BUI.property.getImportedAssertion().iterator(); i.hasNext();) {
			Name name = (Name) i.next();
			edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion as = AssertionSet.getAssertion(name);
			this.importedAssertion.put(name, as);
		}

		for (Iterator i = BUI.property.getImportedAssertionSet().iterator(); i.hasNext();) {
			Name name = (Name) i.next();
			AssertionSet as = AssertionSet.getAssertionSet(name);
			this.importedAssertionSet.put(name, as);
		}
	}

	public void init(Session ses, boolean dump, boolean report, String expectedPath) {
		isDumped = dump;
		genReport = report;
		isNotRoboto = true;
		expectedResultPath = expectedPath;

		SessionManager sessionManager = SessionManager.getInstance();
		try {
			sessionManager.setActiveSession(ses);
		}
		catch(SessionNotFoundException snfe) {
			execSummary.append("The session (" + ses.getName() +
					") was not found in the SessionManager and cannot be set as the active session.");
			return;
		}

		// Prepare the Compilation Manager
		CompilationManager.reset();
		CompilationManager.setFilename(ses.getMainClassFile().toString());
		CompilationManager.setClasspath(ses.getClasspathString());
		CompilationManager.setIncludedPackagesOrTypes(ses.getIncludesArray());
		AssertionSet.reset();
		edu.ksu.cis.bandera.specification.predicate.datastructure.PredicateSet.reset();

		// Load Specification File
		/*
	  String spec = ses.getSpecFilename();
	  Property prop = new Property();
	  if ((spec != null) && (spec.length() > 0)) {
	  try {
	  prop = new SpecificationSaverLoader().load(spec);
	  execSummary.append("   Spec loaded");
	  String activeProp = ses.getActiveTemporal();
	  if (activeProp != null) {
	  TemporalLogicProperty tlp = prop.getTemporalLogicProperty(activeProp);
	  if ((tlp != null) && (!prop.isActivated(tlp))) {
	  prop.activateOrDeactivateTemporalLogicProperty(tlp);
	  }
	  }
	  HashSet activeAssertion = ses.getActiveAssertions();
	  if ((activeAssertion != null) && (!activeAssertion.isEmpty())) {
	  for (Iterator i = activeAssertion.iterator(); i.hasNext();) {
	  String curActiveAssertion = (String) i.next();
	  if (curActiveAssertion != null) {
	  AssertionProperty assn = prop.getAssertionProperty(curActiveAssertion);
	  if ((assn != null) && (!prop.isActivated(assn))) {
	  prop.activateOrDeactivateAssertionProperty(assn);
	  }
	  }
	  }
	  }
	  execSummary.append(" and activated\n");
	  } catch (Exception e) {
	  execSummary.append("   Cannot load '" + spec + "' specification!\n");
	  }
	  }
	  BUI.property = prop;
		 */

		// Load the options
		BUI.doJJJC = true;
		BUI.doSLABS = ses.isAbstractionEnabled();
		BUI.doChecker = ses.isModelCheckingEnabled();
		BUI.doSlicer = ses.isSlicingEnabled();

		/* Don't need to do this anymore.  The options are parsed only when the
		 * SpinOptionsView is used. -tcw
	 String checkerName = ses.getProperty(Session.CHECKER_NAME_PROPERTY);
	 if(Session.SPIN_CHECKER_NAME_PROPERTY.equals(checkerName)) {
	 SpinOption.spinOptions.parseOptions(ses.getProperty(Session.SPIN_OPTIONS_PROPERTY));
	 }
		 */

		//if (ses.isUseJPF()) BUI.jpfOption.parseOptions(ses.getJpfOptions());
		//if (ses.isUseDSPIN()) DSpinOption.spinOptions.parseOptions(ses.getDspinOptions());
		//if (ses.isUseSMV())
	}

	public void initCmdLine(Session ses, boolean dump, boolean report, String expectedPath) {
		isGUI = false;
		init(ses, dump, report, expectedPath);
	}

	/**
	 *
	 * @param dump boolean
	 * @param report boolean
	 * @param expectedPath java.lang.String
	 */
	public static void initRoboto(boolean dump, boolean report, String expectedPath) {
		isNotRoboto = false;
		isGUI = true;
		isDumped = dump;
		genReport = report;
		expectedResultPath = expectedPath;
	}


	private boolean isCompilationNeeded() {
		if (isCodeChanged || (isGUI && BUI.irOptions.getSLABSJavaCheckBox().isSelected() &&
				!BUI.doSLABS) || (isGUI && BUI.doSLABS)) return true;
		Hashtable compClasses = CompilationManager.getCompiledClasses();
		for (Enumeration e = compClasses.keys(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			try {
				if (!(new File(name+".class")).exists()) return true;
			}
			catch (Exception ex) {
				System.out.println(name+".class doesn't exist!");
			}
		}
		return false;
	}

	/**
	 *
	 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
	 */
	public edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion resolveAssertionName(Name name)
			throws Exception {
		edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion a =
				(edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion) importedAssertion.get(name);
		if (a != null)
			return a;

		if (!name.isSimpleName()) {
			try {
				return AssertionSet.getAssertion(name);
			} catch (Exception e) {
				throw new Exception("Can't resolve assertion with name '" + name + "'");
			}
		}

		Vector v = new Vector();

		for (Enumeration e = importedAssertionSet.elements(); e.hasMoreElements();) {
			AssertionSet as = (AssertionSet) e.nextElement();
			if (as.getAssertionTable().get(new Name(as.getName(), name)) != null) {
				v.add(as);
			}
		}

		if (v.size() > 1)
			throw new Exception("Ambiguous assertion with name '" + name + "'");

		if (v.size() < 1)
			throw new Exception("Can't resolve assertion with name '" + name + "'");

		AssertionSet as = (AssertionSet) v.firstElement();
		a = (edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion)as.getAssertionTable().get(new Name(as.getName(), name));
		return a;
	}

	/**
	 *
	 */
	public void run() {
		printMessage("Running ...");

		try {
			currentCounterExample = new StringBuffer();

			// get the active session that we will run
			SessionManager sessionManager = SessionManager.getInstance();
			Session session = sessionManager.getActiveSession();

			if(session == null) {
				if(isGUI && isNotRoboto) {
					JOptionPane.showMessageDialog(BUI.bui, "There is no active session to be executed", "Information", JOptionPane.INFORMATION_MESSAGE);
					throw new RuntimeException("There is no active session to be executed");
				}
				else {
					System.err.println("There is no active session to be executed.");
					return;
				}
			}
			//Logger.dump(session);

			if(!isGUI) {
				System.out.println("-------------------------------------------------------------------------------");
				System.out.println("| ** Session " + session.getName() + " is selected. **");
				System.out.println("-------------------------------------------------------------------------------");
			}

			System.setProperty("user.dir", originalUserDir);
			System.setProperty("java.class.path", session.getClasspathString() + File.pathSeparator + originalClasspath);

			String sesFilename = session.getMainClassFile().toString();
			isDoingBir = sesFilename.toLowerCase().endsWith(".bir");
			if (!isDoingBir) {
				try {
					BOFA.reset();
				}
				catch (Exception e) {
					log.info("An exception occured while resetting BOFA.", e);
				}

				execSummary.append("   BOFA is reset");
				if (BUI.doJJJC) {
					if (!runJJJC()) {
						throw new RuntimeException("Error in Compiling");
					}

					// signal BUI that it can update the information that is based upon JJJC information
					if(isGUI) {
						BUI.bui.finishedJJJC();
					}

					execSummary.append("   JJJC OK");
					if (isDumped || (isGUI && BUI.irOptions.getJJJCCheckBox().isSelected())) {
						dump("original");
						execSummary.append(" and dumped");
					}
					execSummary.append("\n");
				}

				// load the specification now
				Property property = new Property();
				Specification specification = session.getSpecification();
				if(specification != null) {
					// load the active assertions
					Set assertions = specification.getAssertions();
					if((assertions != null) && (assertions.size() > 0)) {
						AssertionProperty ap = new AssertionProperty("");
						Iterator ai = assertions.iterator();
						while(ai.hasNext()) {
							Assertion assertion = (Assertion)ai.next();
							if(assertion.enabled()) {
								Name assertionName = new Name(assertion.getName());
								ap.addAssertion(assertionName);
							}
						}
						property.addAssertionProperty(ap);
						property.activateOrDeactivateAssertionProperty(ap);
					}

					// load the property
					TemporalProperty temporalProperty = specification.getTemporalProperty();
					if(temporalProperty != null) {
						TemporalLogicProperty temporalLogicProperty = new TemporalLogicProperty("SomeName");
						Pattern pattern = temporalProperty.getPattern();
						temporalLogicProperty.setPatternName(pattern.getName());
						temporalLogicProperty.setPatternScope(pattern.getScope());
						Quantification q = temporalProperty.getQuantification();
						if(q != null) {
							temporalLogicProperty.setQuantifier(q.getQuantificationString());
						}
						Set predicates = temporalProperty.getPredicates();
						if(predicates != null) {
							Iterator pi = predicates.iterator();
							while(pi.hasNext()) {
								Predicate predicate = (Predicate)pi.next();
								// make sure the predicate name is valid for the given pattern!
								temporalLogicProperty.putProposition(predicate.getName(), predicate.getExpression());
							}
						}
						property.addTemporalLogicProperty(temporalLogicProperty);
						property.activateOrDeactivateTemporalLogicProperty(temporalLogicProperty);
					}
				}
				BUI.property = property;

				// Bandera SL
				if(true || session.isSlicingEnabled() || session.isAbstractionEnabled() || session.isModelCheckingEnabled()) {
					if (!runBSL()) {
						throw new RuntimeException("Error in BSL");
					}
					execSummary.append("   BSL OK");

					// signal BUI that it can update the information that is based upon JJJC information
					if(isGUI) {
						BUI.bui.finishedBSL();
					}
					if (isDumped || (isGUI && BUI.irOptions.getBSLCheckBox().isSelected())) {
						dump("bsl");
						execSummary.append(", dumped");
					}
					if (isDumped || (isGUI && BUI.irOptions.getBSLJavaCheckBox().isSelected())) {
						generateJava("bsl");
						execSummary.append(" and decompiled");
					}
					execSummary.append("   Spec Compiled\n");
					//dump("BSL");
				}

				// Slicer
				if(session.isSlicingEnabled()) {
					if (!runSlicer()) {
						throw new RuntimeException("Error in Slicer");
					}
					execSummary.append("   Slicer OK");
					if (isDumped || (isGUI && BUI.irOptions.getSlicingCheckBox().isSelected())) {
						dump("sliced");
						execSummary.append(", dumped");
					}
					if (isDumped || (isGUI && BUI.irOptions.getSlicingJavaCheckBox().isSelected())) {
						generateJava("sliced");
						execSummary.append(" and decompiled");
					}
					execSummary.append("\n");
				}

				// BABS or Inliner
				if (isGUI) {
					if (isRecompiled) {
						Vector v = new Vector();
						for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements();) {
							v.add(e.nextElement());
						}
						BUI.typeGUI.setClasses(v);
						BUI.typeGUI.setAbstraction(session.getAbstraction());
						options = BUI.typeGUI.getOptions();
					}
				}
				else {
					if(session.isAbstractionEnabled()) {
						// load the options object with the current abstraction information
						Abstraction abstraction = session.getAbstraction();
						AbstractionConverter ac = new AbstractionConverter();
						options = ac.convert(abstraction);
					}
				}

				if(session.isAbstractionEnabled()) {
					if (!runSLABS()) {
						throw new RuntimeException("Error in Abstraction");
					}
					execSummary.append("   SLABS OK");
					if (isDumped || (isGUI && BUI.irOptions.getSLABSCheckBox().isSelected())) {
						dump("abstracted");
						execSummary.append(", dumped");
					}

					if (isDumped || (isGUI && BUI.irOptions.getSLABSJavaCheckBox().isSelected())) {
						generateJava("abstracted");
						execSummary.append(" and decompiled");
					}

					execSummary.append("\n");
				}
			}
			else {
			} // end if (!isDoingBir)

			String checkerName = session.getProperty(Session.CHECKER_NAME_PROPERTY);
			boolean usingJPF = Session.JPF_CHECKER_NAME_PROPERTY.equals(checkerName);
			boolean usingSpin = Session.SPIN_CHECKER_NAME_PROPERTY.equals(checkerName);
			boolean usingSMV = Session.SMV_CHECKER_NAME_PROPERTY.equals(checkerName);
			boolean usingDSpin = Session.DSPIN_CHECKER_NAME_PROPERTY.equals(checkerName);
			if((usingJPF) && (!isDoingBir)) {
				runJPF();
			}
			else {
				if (isDoingBir || usingSpin || usingSMV || usingDSpin ||
						(isDumped || (isGUI && BUI.irOptions.getBIRCheckBox().isSelected()))) {
					if (trail != null) {
						throw new RuntimeException("Counter example loading is not implemented for this section (yet, but we promise we will implement this ASAP)");
					}

					runBIRC();
				}
			}

			if (isGUI) {
				//BUI.bui.getSpinLabel().setIcon(IconLibrary.spinIcon);
				//BUI.bui.getJPFLabel().setIcon(IconLibrary.jpfIcon);
				//BUI.bui.getNuSMVLabel().setIcon(IconLibrary.smvIcon);
			}

		}
		catch(Exception catch22) {
			catch22.printStackTrace();
		}

		writeReport();
		// END

		CompilationManager.setQuantifiers(new Vector());
		BUI.isExecuting = false;

		printMessage("Finished running.");
	}

	/**
	 *
	 */
	private void runBIRC() {
		printMessage("Running BIRC ...");

		SessionManager sessionManager = SessionManager.getInstance();
		Session activeSession = sessionManager.getActiveSession();
		if(activeSession == null) {
			System.err.println("Cannot run BIRC without an active session.  Quitting.");
			return;
		}

		String defaultName = "";
		try {
			if (!isDoingBir) {
				printMessage("Running Inliner ...");

				AnnotationManager am = CompilationManager.getAnnotationManager();
				Inline.typeTable = typeTable;
				HashSet methods = getMethodsForInliner();
				java.util.List<SootClass> scList = new ArrayList<SootClass>();
				
				/* [Thomas, May 21, 2017]
				 * Test code
				 * */
				//dump("beforeInlined");
				
				for (Iterator i = methods.iterator(); i.hasNext();) {
					SootMethod sm = (SootMethod) i.next();
					//Inline.inline(sm, am);
					log.debug("Inlining SootMethod " + sm + "...");
					Inline.inline(sm, am, false);

					/* [Thomas, May 4, 2017]
					 * Each SootClass may have several event handler methods,
					 * thus we need to avoid adding duplicate classes
					 * */
					if(!scList.contains(sm.getDeclaringClass()))
					{
						scList.add(sm.getDeclaringClass());
					}
					//classesForBIRC[index++] = sm.getDeclaringClass();
				}
				Inline.typeTable = null;
				SootClass[] classesForBIRC = new SootClass[scList.size()];
				int index = 0;
				for(SootClass sc : scList)
				{
					classesForBIRC[index++] = sc;
				}

				/* [Thomas, May 2nd, 2017]
				 * Test code
				 * */
				dump("inlined");

//				if (isDumped || (isGUI && BUI.irOptions.getBIRCCheckBox().isSelected())) {
//					dump("inlined");
//				}

				printMessage("Finished Inliner.");

				//KSUOptimizing.packLocalsForClasses(getSootClassesForBIRC());
				//KSUOptimizing.packLocalsForClasses(getClassesForBIRC());
				KSUOptimizing.packLocalsForClasses(classesForBIRC);
				dump("packed");

				//ThreadExpand.expandStart(getClassesForBIRC());
				ThreadExpand.expandStart(classesForBIRC, am);
				dump("expanded");

				edu.ksu.cis.bandera.birc.PredicateSet ps = new edu.ksu.cis.bandera.birc.PredicateSet();
				defaultName = activeSession.getOutputName();
				/*
				  for (Enumeration keys = predNodeGrimpTable.keys(); keys.hasMoreElements();) {
				  Object key = keys.nextElement();
				  Value value = (Value) predNodeGrimpTable.get(key);
				  try {
				  value = PredicateUpdate.update(value);
				  } catch (Exception e) {
				  throw new RuntimeException("Predicate '" + key + "' is not reachable in the code");
				  }
				  predNodeGrimpTable.put(key, value);
				  }
				  if (isDumped || (isGUI && BUI.irOptions.getBIRCCheckBox().isSelected())) {
				  dump("updated");
				  }
				 */

				TemporalLogicProperty tlp = BUI.property.getActivatedTemporalLogicProperty();
				String birName = defaultName + ".bir";

				String checkerName = activeSession.getProperty(Session.CHECKER_NAME_PROPERTY);
				boolean usingJPF = Session.JPF_CHECKER_NAME_PROPERTY.equals(checkerName);
				boolean usingSpin = Session.SPIN_CHECKER_NAME_PROPERTY.equals(checkerName);
				boolean usingSMV = Session.SMV_CHECKER_NAME_PROPERTY.equals(checkerName);
				boolean usingDSpin = Session.DSPIN_CHECKER_NAME_PROPERTY.equals(checkerName);
				if (tlp != null && (usingSpin || usingSMV || usingDSpin)) {
					Pattern pattern = PatternSaverLoader.getPattern(tlp.getPatternName(), tlp.getPatternScope());
					Compiler compiler = new Compiler(predNodeGrimpTable, placeHolders, pattern, tlp.getNode());
					String ltlFormula = "!(" + compiler.getFormula("LTL") + ")";
					Hashtable parameterTable = compiler.getParameterTable();
					SpecificationAbstractor sa = new SpecificationAbstractor();
					sa.abstractLTL(new CoercionManager(AbstractionLibraryManager.getAbstractions()), typeTable, ltlFormula, parameterTable);
					ltlFormula = sa.getLtlFormula();
					parameterTable = sa.getParameterTable();
					for (Enumeration e = parameterTable.keys(); e.hasMoreElements();) {
						String key = (String) e.nextElement();
						ca.mcgill.sable.soot.jimple.Expr value = (ca.mcgill.sable.soot.jimple.Expr) parameterTable.get(key);
						try {
							ps.addValuePredicate(key, (ca.mcgill.sable.soot.jimple.Expr) PredicateUpdate.update(value));
						}
						catch (Exception ex) {
							ex.printStackTrace(System.err);
							throw new RuntimeException("Error occured when translating predicate '" + key + "'" +
									"  Exception: " + ex.toString());
						}

						//if (isDumped) {
						//    ((BSLReport) rm.getReport("BSL")).addLTLPredicate(key, value);
						//}
					}

					//if (isDumped) {
					//    ((BSLReport) rm.getReport("BSL")).setLTL(ltlFormula);
					//}

					//System.out.println("");
					//System.out.println("Never claim: " + ltlFormula);
					log.debug("Never claim: " + ltlFormula);
					ps.print();
					//System.out.println("");
					PrintWriter pw = new PrintWriter(new FileWriter(defaultName + ".ltl"));
					pw.print(ltlFormula);
					pw.close();
				}
				else {
					// modify the active session to turn off never claim and use safety instead! -tcw
					String checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
					SpinOptions spinOptions = (SpinOptions)OptionsFactory.createOptionsInstance("Spin");
					spinOptions.init(checkerOptions);
					spinOptions.setApplyNeverClaim(false);
					spinOptions.setAcceptanceCycles(false);
					spinOptions.setSafety(true);
					activeSession.setProperty(Session.CHECKER_OPTIONS_PROPERTY, spinOptions.getCommandLineOptions());
				}

				String tempPath = getTempDir("birc");
				if (!BanderaUtil.mkdirs(tempPath)) {
					String errmsg = "***Can't make temporary directory to store model checker output!";
					System.out.println(errmsg);
					if (isGUI && isNotRoboto)
						JOptionPane.showMessageDialog(null, errmsg, "Error", JOptionPane.ERROR_MESSAGE);
					else
						execSummary.append(errmsg);
					return;
				}

				BIROptions birOptions = activeSession.getBIROptions();
				/* [Thomas, May 9, 2017]
				 * Use default resource bound
				 * */
				//				ResourceBounds resourceBounds = birOptions.getResourceBounds();
				//dump("beforeCreateTransSystem");
				ResourceBounds resourceBounds = null;
				sys = edu.ksu.cis.bandera.birc.Builder.createTransSystem(getSootClassesForBIRC(), defaultName, ps, resourceBounds);

				if (isDumped || (isGUI && BUI.irOptions.getBIRCheckBox().isSelected())) {
					PrintWriter out = new PrintWriter(new FileWriter(birName));
					BirPrinter.print(sys, out);
					out.flush();
					out.close();
				}
			}
			else { // We're doing BIR 
				String birFilename = activeSession.getMainClassFile().toString();
				String ltlFilename = birFilename.substring(0,birFilename.length()-4)+".ltl";
				sys = BirBuilder.parse(birFilename);

				// If no LTL Formula exists, then turn off the never claim
				if (!(new File(ltlFilename)).exists()) {

					String checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
					SpinOptions spinOptions = (SpinOptions)OptionsFactory.createOptionsInstance("Spin");
					spinOptions.init(checkerOptions);
					spinOptions.setApplyNeverClaim(false);
					spinOptions.setAcceptanceCycles(false);
					spinOptions.setSafety(true);
					activeSession.setProperty(Session.CHECKER_OPTIONS_PROPERTY, spinOptions.getCommandLineOptions());

					/*
				      BUI.spinOption.setApplyNeverClaim(false);
				      BUI.spinOption.setAcceptanceCycles(false);
				      BUI.spinOption.setSafety(true);
					 */
				}
			}  // end if (!isDoingBir)

			if(activeSession.isModelCheckingEnabled()) {
				String checkerName = activeSession.getProperty(Session.CHECKER_NAME_PROPERTY);
				boolean usingJPF = Session.JPF_CHECKER_NAME_PROPERTY.equals(checkerName);
				boolean usingSpin = Session.SPIN_CHECKER_NAME_PROPERTY.equals(checkerName);
				boolean usingSMV = Session.SMV_CHECKER_NAME_PROPERTY.equals(checkerName);
				boolean usingDSpin = Session.DSPIN_CHECKER_NAME_PROPERTY.equals(checkerName);
				if(usingSpin) {
					edu.ksu.cis.bandera.bir.Type.booleanType = new edu.ksu.cis.bandera.bir.Bool(); // need to reset this
					runSpin();
				}
				else if(usingSMV) {
					edu.ksu.cis.bandera.bir.Type.booleanType = new edu.ksu.cis.bandera.bir.Bool(); // need to reset this
					runSMV();
				}
				else if(usingDSpin) {
					edu.ksu.cis.bandera.bir.Type.booleanType = new edu.ksu.cis.bandera.bir.Bool(); // need to reset this
					runDSpin();
				}
				else {
					log.warn("The checker was enabled but a valid checker was not set.  checkerName = " + checkerName);
				}
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
			//Logger.keep();
			if (isGUI && isNotRoboto) {
				JOptionPane.showMessageDialog(BUI.bui, e.toString(), "BIRC Phase --- Error", JOptionPane.ERROR_MESSAGE);
				updateTrees();
			}
			else {
				execSummary.append("   BIRC phase error: " + e.getMessage() + "\n");
			}
		}
		finally {
			defaultName = activeSession.getOutputName();
		}

		printMessage("Finished running BIRC.");
	}

	/**
	 *
	 */
	private boolean runBSL() {
		printMessage("Running BSL ...");

		if (isDumped) { // generate report if dumped
			BSLReport bslReport = new BSLReport();
			bslReport.addAssertionsAndPredicates();
			rm.addReport("BSL", bslReport);
		}
		try {
			Property property = BUI.property;
			Hashtable assertionTable = new Hashtable();
			initAssertions();
			for (Iterator i = property.getActivatedAssertionProperties().iterator(); i.hasNext();) {
				AssertionProperty ap = (AssertionProperty) i.next();
				for (Iterator j = ap.getAssertions().iterator(); j.hasNext();) {
					assertionTable.put(resolveAssertionName((Name) j.next()), new HashSet());
				}
			}

			int numOfQuantifiers = 0;
			HashSet v = new HashSet();

			TemporalLogicProperty tlp = property.getActivatedTemporalLogicProperty();

			if (tlp != null) {
				tlp.validate(property.getImportedType(), property.getImportedPackage(), property.getImportedAssertion(), property.getImportedAssertionSet(), property.getImportedPredicate(), property.getImportedPredicateSet());
				if (tlp.getExceptions().size() > 0) {
					StringBuffer buffer = new StringBuffer();
					for (Iterator i = tlp.getExceptions().iterator(); i.hasNext();) {
						buffer.append(i.next() + "\n");
					}
					if (isGUI && isNotRoboto) {
						JOptionPane.showMessageDialog(BUI.bui, buffer.toString(), "Error", JOptionPane.ERROR_MESSAGE);
						BUI.isExecuting = false;
					} else
						execSummary.append("   Spec compilation failed!\n");
					return false;
				}
				Hashtable qTable = tlp.getQuantifiersTable();
				numOfQuantifiers = qTable.size();
				for (Enumeration e = qTable.elements(); e.hasMoreElements();) {
					v.add((QuantifiedVariable) e.nextElement());
				}
			}
			if ((assertionTable.size() > 0) || (numOfQuantifiers > 0)) {
				if (tlp == null)
					CompilationManager.setQuantifiers(new Vector());
				else
					CompilationManager.setQuantifiers(tlp.getQuantifiedVariables());
				CompilationManager.setModifiedMethodTable(AssertionProcessor.process(assertionTable));
				isRecompiled = true;
				CompilationManager.compile();
				isCodeChanged = true;
				if (CompilationManager.getExceptions().size() > 0)
					throw new Exception("Errors occured when compiling the program");
				SootClass mainClass = CompilationManager.getMainSootClass();
				for (Iterator i = CompilationManager.getQuantifiers().iterator(); i.hasNext();) {
					QuantifiedVariable qv = (QuantifiedVariable) i.next();
					String name = "quantification$" + qv.getName();
					SootField sf = CompilationManager.getFieldForQuantifier(name);
					if (sf == null) {
						sf = new SootField(name, qv.getType(), Modifier.STATIC | Modifier.PUBLIC);
						CompilationManager.addFieldForQuantifier(sf);
					}
					mainClass.addField(sf);
					SootClass sc = sf.getDeclaringClass(); // robbyjo's patch
					quantifierSliceInterests.add(new SliceField(sc, sf)); // robbyjo's patch
					// robbyjo's patch: Store class and field association
					qList.add(new QuantifierClassPair(sc.getName(), name));
				}
				CompilationManager.setModifiedMethodTable(new Hashtable());
			}

			// Compile predicates to Grimp
			if (tlp != null) {
				StringBuffer sb = new StringBuffer();
				tlp.validate(property.getImportedType(), property.getImportedPackage(), property.getImportedAssertion(), property.getImportedAssertionSet(), property.getImportedPredicate(), property.getImportedPredicateSet());
				Hashtable pTable = tlp.getPredicatesTable();
				Hashtable pqTable = tlp.getPredicateQuantifierTable();
				Hashtable qphTable = new Hashtable();
				SootClass mainClass = CompilationManager.getMainSootClass();
				Jimple jimple = Jimple.v();
				HashSet visitedP = new HashSet();
				for (Enumeration e = pTable.keys(); e.hasMoreElements();) {
					Object key = e.nextElement();
					edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate p =
							(edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate) pTable.get(key);
					if (p.getExceptions().size() > 0) {
						if (visitedP.contains(p))
							continue;
						visitedP.add(p);
						sb.append("In '" + p + "':\n");
						for (Iterator i = p.getExceptions().iterator(); i.hasNext();) {
							sb.append("* " + ((Exception) i.next()).getMessage() + "\n");
						}
						continue;
					}
					Vector qvs = (Vector) pqTable.get(key);
					Value phThis = null;
					Hashtable phParams = new Hashtable();
					for (int i = 0; i < qvs.size(); i++) {
						QuantifiedVariable q = (QuantifiedVariable) qvs.elementAt(i);
						Value cph = null;
						if (q != null) {
							if (qphTable.get(q) == null) {
								String name = "quantification$" + q.getName();
								cph = jimple.newStaticFieldRef(mainClass.getField(name));
								qphTable.put(q, cph);
								placeHolders.add(cph);
							} else {
								cph = (Value) qphTable.get(q);
							}
							if ((i == 0) && (!p.isStatic())) {
								phThis = cph;
							} else {
								phParams.put(p.isStatic() ? p.getParams().elementAt(i) : p.getParams().elementAt(i - 1), cph);
							}
						}
					}
					Value value = PredicateProcessor.process(phThis, phParams, p);
					predNodeGrimpTable.put(key, value);
					predicates.add(p);
					if (isDumped) // add compiled predicate to report if dumped
						((BSLReport) rm.getReport("BSL")).addCompiledPredicate(p, value);
				}
				String s = sb.toString();
				if (s.length() > 0)
					throw new RuntimeException(s.substring(0, s.length() - 1));
			}
		} catch (Exception e) {
			CompilationManager.setModifiedMethodTable(new Hashtable());
			CompilationManager.setQuantifiers(new Vector());
			if (isGUI && isNotRoboto) {
				JOptionPane.showMessageDialog(BUI.bui, e.getMessage(), "BSL Phase --- Error", JOptionPane.ERROR_MESSAGE);
				BUI.isExecuting = false;
				updateTrees();
			} else {
				execSummary.append("   Spec compilation failed: " + e.getMessage() + "\n");
			}
			return false;
		}
		if (isGUI)
			updateTrees();

		printMessage("Finished running BSL.");

		return true;
	}

	/**
	 *
	 */
	private void runDSpin() {
		printMessage("Running dSpin ...");

		SessionManager sessionManager = SessionManager.getInstance();
		Session activeSession = sessionManager.getActiveSession();
		String checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
		DSpinOptions dSpinOptions = (DSpinOptions)OptionsFactory.createOptionsInstance("DSpin");
		dSpinOptions.init(checkerOptions);
		DSpinTrans spinTrans = DSpinTrans.translate(sys, dSpinOptions);
		spinTrans.runSpin();
		JimpleTrace trace = Builder.interpretTrace(spinTrans.parseOutput());
		if (trace.isVerified()) {
			if (isGUI && isNotRoboto) {
				JOptionPane.showMessageDialog(BUI.bui, "Verified", "Verification Phase", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				execSummary.append("   DSpin OK, verified\n");
			}
		}
		else {
			//dumpJimpleTrace(trace);
			if (isGUI && isNotRoboto) {
				if (!isDoingBir) {
					CounterExample ce = new CounterExample(new BIRCTraceManager(trace, typeTable));
					ce.pack();
					ce.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(BUI.bui, "Model has a counter example", "Verification Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else {
				execSummary.append("   DSpin OK, has a counter example\n");
			}
		}

		printMessage("Finished running dSpin.");
	}

	/**
	 *
	 */
	private boolean runJJJC() {
		printMessage("Running JJJC ...");

		try {
			isCodeChanged = false;
			isRecompiled = true;

			CompilationManager.compile();

			if (CompilationManager.getExceptions().size() > 0) {
				throw new Exception("Errors occured when compiling the program");
			}
			dump("fixme");
			generateJava("fixme");
		} catch (Throwable e) {
			System.out.println("Exception while compiling: " + e.toString());
			e.printStackTrace(System.out);
			if (isGUI && isNotRoboto) {
				JOptionPane.showMessageDialog(BUI.bui, e.getMessage(), "JJJC Phase --- Error", JOptionPane.ERROR_MESSAGE);
				BUI.isExecuting = false;
				Hashtable exceptions = CompilationManager.getExceptions();
				for (Enumeration en = exceptions.keys(); en.hasMoreElements(); )
				{
					String s = (String) en.nextElement();
					System.out.println("At: "+s);
					for (Enumeration enumVar = ((Vector) exceptions.get(s)).elements();
							enumVar.hasMoreElements(); )
					{
						Exception ex = (Exception) enumVar.nextElement();
						ex.printStackTrace();
					}
				}
				updateTrees();
			} else {
				execSummary.append("   JJJC failed: " + CompilationManager.getExceptions() + "\n");
			}
			return false;
		}
		if (isGUI) {
			updateTrees();
		}

		printMessage("Finished running JJJC.");

		return true;
	}

	/**
	 * Run the JPF, do the necessary preprocessing (precompiling and process the predicates)
	 */
	private void runJPF() {
		printMessage("Running JPF ...");

		// Save the current classpath and current directory
		String oldClasspath = System.getProperty("java.class.path");
		String oldUserDir = System.getProperty("user.dir");
		String classpath;
		String tempDir = ".";
		String ltlFormula = null;

		try {
			// Check whether recompilation is needed (esp. after slicing and abstraction)
			// If the source code is not compiled, then compile it anyway.
			// However, to avoid naming conflict, we just spit out the files into the
			// temporary directory temp$<session_name>/JPF
			// Then, we set our working directory and class path accordingly

			if (isCompilationNeeded()) {
				tempDir = generateJava("JPF");
				if (tempDir.equals("")) {
					throw new Exception("Error in dumping Java files for JPF");
				}
				System.setProperty("user.dir", tempDir);
				classpath = tempDir +
						File.pathSeparator + Preferences.getBanderaBuiltinFile().getPath() +
						File.pathSeparator + Preferences.getJPFHomeDir() +
						File.pathSeparator + Preferences.getBanderaHomeDir() +
						File.pathSeparator + Preferences.getAbstractionPath();
				BanderaUtil.runJavac(classpath, tempDir + File.separator + new Name(CompilationManager.getMainSootClass().getName()).getLastIdentifier() + ".java");
				System.setProperty("java.class.path", classpath);
			} else {
				System.setProperty("user.dir", tempDir);
				classpath = CompilationManager.getClasspath();
				System.setProperty("java.class.path", classpath);
			}
			//System.out.println(JPFOption.jpfOption.getOptions());

			// If we don't do checking, just quit. We only need the decompiler stuff
			// to work for debugging...
			if (!BUI.doChecker) return;

			// Do we have temporal properties to check?
			TemporalLogicProperty tlp = BUI.property.getActivatedTemporalLogicProperty();
			if (tlp != null) {
				// So, compile the temporal properties and get the LTL formula
				JPFLTLCompiler comp = new JPFLTLCompiler(tempDir, tlp, qList);
				comp.compile();
				ltlFormula = comp.getFormula("LTL");
				//System.out.println("JPF LTL Formula = " + ltlFormula);
				log.debug("JPF LTL Formula = " + ltlFormula);
			}
			//System.out.println("Invoking JPF with classpath: " + System.getProperty("java.class.path"));
			printMessage("Invoking JPF with classpath: " + System.getProperty("java.class.path"));

			// Fetch the option from the current active session and add -bandera to it
			SessionManager sm = SessionManager.getInstance();
			Session activeSession = sm.getActiveSession();
			String opt = "";
			if(activeSession != null) {
				opt = "-bandera " + activeSession.getProperty(Session.JPF_OPTIONS_PROPERTY);
			}
			else {
				opt = "-bandera";
			}
			log.debug("JPF Options = " + opt);
			StringTokenizer t = new StringTokenizer(opt, " ");
			int maxOption = t.countTokens() + 1;

			// Make sure we have enough space if we have an LTL formula from specification
			if (ltlFormula != null) maxOption += 2;
			if (trail != null) maxOption++; // we're running counter example trail
			String[] options = new String[maxOption];

			// Place all options into the string array (to pass it into JPF)
			int i = 0;

			while (t.hasMoreTokens()) {
				options[i++] = t.nextToken();
			}

			// Then inject our LTL switch if we have the LTL formula
			if (ltlFormula != null)
			{
				options[i++] = "-ltl";
				options[i++] = ltlFormula;
			}

			if (trail != null) // we're running counter example trail
			{
				options[i++] = "-no-search";
			}

			// Supply the class file name to check (without the .class extension)
			options[i] = CompilationManager.getMainSootClass().getName();

			// If it is under GUI mode, display JPF logo
			if (isGUI) BUI.bui.getJPFLabel().setIcon(IconLibrary.jpfSelectedIcon);

			JPFRunner jpfRunner = new JPFRunner();
			CounterExample ce = jpfRunner.runJPF(options, typeTable, trail);
			if(ce == null) {
				if (isGUI && isNotRoboto)
					JOptionPane.showMessageDialog(BUI.bui, "Verified", "Verification Phase", JOptionPane.INFORMATION_MESSAGE);
				else
					execSummary.append("   JPF OK, verified\n");
			}
			else {
				if((isGUI) && (isNotRoboto)) {
					currentce = ce;
					ce.pack();
					ce.setVisible(true);
				}
				else {
					execSummary.append("   JPF OK, has a counter example\n");
				}
			}
		}
		catch (Exception e) {
			// Something wrong in the process and log 'em
			if (isGUI && isNotRoboto) {
				JOptionPane.showMessageDialog(BUI.bui, e.getMessage(), "Verification Phase --- Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				BUI.isExecuting = false;
				updateTrees();
			}
			else {
				execSummary.append("   JPF failed: " + e.getMessage() + "\n");
			}
			//Logger.keep();
		}

		// Restore the old values
		System.setProperty("java.class.path", oldClasspath);
		System.setProperty("user.dir", oldUserDir);   // Won't work! Bugg!!

		printMessage("Finished running JPF.");
	}

	/**
	 *
	 */
	private boolean runSLABS() {
		printMessage("Running SLABS ...");

		if (isDumped) { // generate report if dumped
			SLABSReport slabsReport = new SLABSReport();
			rm.addReport("SLABS", slabsReport);
		}

		boolean completed = false;
		try {
			isCodeChanged = true;
			TypeInference ti = new TypeInference();
			typeTable = ti.type(CompilationManager.getSootClassManager(), options);
			Vector v = new Vector();
			for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements();) {
				v.add(e.nextElement());
			}
			if (isGUI)
				BUI.typeGUI.setInferredTypes(typeTable);

			if (isDumped) // add type info to the report if dumped
				((SLABSReport) rm.getReport("SLABS")).setTypeTable(typeTable);

			new SLABS(CompilationManager.getAnnotationManager(), typeTable, ti.getInterfaceMethodMethod()).abstractClasses(v);
			//if (isGUI) BUI.bui.printMessage("Finished running SLABS.");
			//return true;
			completed = true;
		} catch (Exception e) {
			if (isGUI && isNotRoboto) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(BUI.bui, e.getMessage(), "SLABS Phase --- Error", JOptionPane.ERROR_MESSAGE);
				BUI.isExecuting = false;
				updateTrees();
			} else
				execSummary.append("   SLABS failed: " + e.getMessage() + "\n");

			//if (isGUI) BUI.bui.printMessage("Finished running SLABS.");
			//return false;
			completed = false;
		}

		printMessage("Finished running SLABS.");
		return(completed);
	}

	/**
	 *
	 */
	private boolean runSlicer() {
		printMessage("Running Slicer ...");

		//REPORT CODE STARTS
		if (isDumped) { // generate report if dumped
			SlicerReport slicerReport = new SlicerReport();
			rm.addReport("Slicer", slicerReport);
		}
		//REPORT CODE ENDS

		boolean completed = false;
		try {
			SootClass[] sootClasses = new SootClass[CompilationManager.getCompiledClasses().size()];
			int i = 0;
			for (Enumeration e = CompilationManager.getCompiledClasses().elements(); e.hasMoreElements(); i++) {
				sootClasses[i] = (SootClass) e.nextElement();
			}
			Vector v = (Vector) AssertionSliceInterestCollector.collect(new Vector());
			v.addAll(PredicateSliceInterestCollector.collect(predicates));
			v.addAll(quantifierSliceInterests);
			Slicer slicer = new Slicer(sootClasses, v, CompilationManager.getAnnotationManager());
			slicer.run();
			v = new Vector();
			SootClass[] result = slicer.result();
			for (i = 0; i < result.length; i++) {
				v.add(result[i]);
			}
			isCodeChanged = true;
			Hashtable table = CompilationManager.getCompiledClasses();
			Vector removed = new Vector();
			for (Enumeration e = table.keys(); e.hasMoreElements();) {
				Object key = e.nextElement();
				Object value = table.get(key);
				if (!v.contains(value))
					removed.add(key);
			}
			for (Enumeration e = removed.elements(); e.hasMoreElements();) {
				table.remove(e.nextElement());
			}
			//REPORT CODE STARTS
			if (isDumped) {
				((SlicerReport)rm.getReport("Slicer")).extractReportData(result);
			} // end of if (isDumped)
			//REPORT CODE ENDS

			completed = true;

			if (isGUI) {
				updateTrees();
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (isGUI && isNotRoboto) {
				JOptionPane.showMessageDialog(BUI.bui, e.getMessage(), "Slicer Phase --- Error", JOptionPane.ERROR_MESSAGE);
				BUI.isExecuting = false;
				updateTrees();
			} else
				execSummary.append("   Slicer failed: " + e.getMessage() + "\n");
			//return false;
			completed = false;
		}

		printMessage("Finished running Slicer.");

		return(completed);
	}

	/**
	 *
	 */
	private void runSMV() {
		printMessage("Running SMV ...");

		// this should use the new OptionsFactory and init it from the checker options
		//  in the active session. -tcw
		SmvOptions options = new SmvOptions();
		options.setSafety(true);
		options.setInterleaving(true);
		options.setProperty(false);
		SmvTrans trans = SmvTrans.translate(sys, options);
		if (isGUI)
			BUI.bui.getNuSMVLabel().setIcon(IconLibrary.smvSelectedIcon);
		trans.runSmv();
		JimpleTrace trace = Builder.interpretTrace(trans.parseOutput());
		if (trace.isVerified()) {
			if (isGUI && isNotRoboto)
				JOptionPane.showMessageDialog(BUI.bui, "Verified", "Verification Phase", JOptionPane.INFORMATION_MESSAGE);
			else
				execSummary.append("   SMV OK, verified\n");
		} else
			if (trace.isLimitViolation()) {
				if (isGUI && isNotRoboto)
					JOptionPane.showMessageDialog(BUI.bui, "Resource Limit Exceeded : " + trace.getTrapName(), "Verification Phase Error", JOptionPane.INFORMATION_MESSAGE);
				else
					execSummary.append("   SMV OK, limit exceeded\n");
			} else
				if (!trace.isComplete()) {
					if (isGUI && isNotRoboto)
						JOptionPane.showMessageDialog(BUI.bui, "Checker Did Not Complete", "Verification Phase Error", JOptionPane.INFORMATION_MESSAGE);
					else
						execSummary.append("   SMV FAILED, not complete\n");
				} else {
					//dumpJimpleTrace(trace);
					if (isGUI && isNotRoboto) {
						CounterExample ce = new CounterExample(new BIRCTraceManager(trace, typeTable));
						ce.pack();
						ce.setVisible(true);
					} else
						execSummary.append("   SMV OK, has a counter example\n");
				}
		printMessage("Finished running SMV.");
	}

	/**
	 *
	 */
	private void runSpin() {
		printMessage("Running Spin ...");

		String tempPath = ".";
		if (!isDoingBir) tempPath = getTempDir("birc");

		SessionManager sessionManager = SessionManager.getInstance();
		Session activeSession = sessionManager.getActiveSession();
		String checkerOptions = activeSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
		SpinOptions spinOptions = (SpinOptions)OptionsFactory.createOptionsInstance("Spin");
		spinOptions.init(checkerOptions);

		SpinTrans spinTrans = null;
		try {
			spinTrans = SpinTrans.translate(sys, spinOptions, tempPath);
		}
		catch(java.lang.Error e) {
			System.err.println("Caught Error while translating using SpinTrans: " + e.toString());
			throw e;
		}

		if (isGUI) {
			BUI.bui.getSpinLabel().setIcon(IconLibrary.spinSelectedIcon);
		}

		spinTrans.runSpin();
		JimpleTrace trace = Builder.interpretTrace(spinTrans.parseOutput());

		// determine the output and give it to the user
		String textMessage = "";
		String guiMessage = "";
		String guiTitle = "";
		boolean hasCounterExample = false;
		/*
		 * Note: Most of these cases will probably never happen.  This seems
		 * to be taken care of in the SpinTrans.check(...) method which throws
		 * exceptions (out to runBIRC).
		 */
		if (trace.isVerified()) {
			textMessage = "    SPIN OK, verified\n";
			guiMessage = "Verified";
			guiTitle = "Verification Phase";
			hasCounterExample = false;
		}
		else if (trace.isOutOfMemory()) {
			textMessage = "    SPIN FAILED, out of memory\n";
			guiMessage = "Out of Memory : modify spin options";
			guiTitle = "Verification Phase Error";
			hasCounterExample = false;
		}
		else if (trace.isVectorExceeded()) {
			textMessage = "    SPIN FAILED, vector width exceeded\n";
			guiMessage = "State Vector Width Exceeded : modify spin options";
			guiTitle = "Verification Phase Error";
			hasCounterExample = false;
		}
		else if (trace.isDepthExceeded()) {
			textMessage = "    SPIN FAILED, depth limit exceeded\n";
			guiMessage = "Depth Limit Exceeded : modify spin options";
			guiTitle = "Verification Phase Error";
			hasCounterExample = false;
		}
		else if (trace.isLimitViolation()) {
			textMessage = "    SPIN OK, limit exceeded\n";
			guiMessage = "Resource Limit Exceeded : " + trace.getTrapName();
			guiTitle = "Verification Phase Error";
			hasCounterExample = false;
		}
		else if (!trace.isComplete()) {
			textMessage = "    SPIN FAILED, not complete\n";
			guiMessage = "Checker Did Not Complete";
			guiTitle = "Verification Phase Error";
			hasCounterExample = false;
		}
		else {
			String reason = "";
			if(trace.isLivenessPropertyViolation()) {
				reason = ", Liveness property violation";
			}
			else if(trace.isPropertyViolation()) {
				reason = ", Property violation";
			}
			else if(trace.isDeadlockViolation()) {
				reason = ", Deadlock violation";
			}
			else if(trace.isAssertionViolation()) {
				reason = ", Assertion violation";
			}

			if((reason != null) && (reason.length() > 0)) {
				textMessage = "    SPIN OK, has a counter example" + reason + "\n";
			}
			else {
				textMessage = "    SPIN OK, has a counter example\n";
			}
			guiMessage = "Model has a counter example" + reason;
			guiTitle = "Verification Info";
			//dumpJimpleTrace(trace);
			if (isGUI && isNotRoboto && !isDoingBir) {
				hasCounterExample = true;
			}
			else {
				hasCounterExample = false;
			}
		}

		if(hasCounterExample) {
			//dumpJimpleTrace(trace);
			CounterExample ce = new CounterExample(new BIRCTraceManager(trace, typeTable));
			ce.pack();
			ce.setVisible(true);
		}
		else {
			if (isGUI && isNotRoboto) {
				JOptionPane.showMessageDialog(BUI.bui, guiMessage, guiTitle, JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				execSummary.append(textMessage);
			}
		}

		printMessage("Finished running Spin.");
	}

	/**
	 *
	 */
	private void updateTrees() {
		if (isGUI) {
			BUI.sessionPane.updateLeftTree();
			BUI.predicateBrowser.updateTree();
			BUI.assertionBrowser.updateTree();
		}
	}

	/**
	 *
	 */
	private void writeReport() {
		if (!BanderaUtil.mkdirs(getTempDir("report"))) {
			String errmsg = "***Can't make temporary directory for dumping report!";
			System.out.println(errmsg);
			if (isGUI && isNotRoboto)
				JOptionPane.showMessageDialog(null, errmsg, "Error", JOptionPane.ERROR_MESSAGE);
			else
				execSummary.append(errmsg);
			return;
		}

		for (Enumeration e = rm.getFilteredReportTable().elements(); e.hasMoreElements();) {
			Report r = (Report) e.nextElement();
			String path = getTempDir("report") + File.separator + r.getFilename();
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(path));
				System.out.println("Writing " + path);
				pw.print(r.getTextRepresentation());
				pw.close();
			} catch (IOException ioe) {
				System.out.println("Error writing " + path);
			}
		}
		if (genReport) {
			SessionManager sm = SessionManager.getInstance();
			Session activeSession = sm.getActiveSession();
			if(activeSession != null) {
				String expectedDir = expectedResultPath + File.separator + "temp$" + activeSession.getName();
				String resultDir = "temp$" + activeSession.getName();
				String htmlPath = resultDir + File.separator + "index.html";
				HTMLReportGenerator.generateSessionSummary(htmlPath, activeSession, expectedDir, resultDir);
			}
			else {
				System.err.println("Serious problem.  No active session!");
			}
		}
	}

	/**
	 * Print the message to the appropriate location.  If we are running in GUI, print
	 * it to the BUI message pane.  If we are running in CLI mode, print it to
	 * System.out.
	 *
	 * @param String message The message to be printed.
	 */
	private void printMessage(String message) {
		if (isGUI) {
			BUI.bui.printMessage(message);
		}
		else {
			System.out.println(message);
		}
	}

	private CounterExample currentce = null;
	public CounterExample getCurrentCounterExample() {
		return currentce;
	}
}
