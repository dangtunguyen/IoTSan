/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999                                          *
 * John Hatcliff (hatcliff@cis.ksu.edu)                              *
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
import ca.mcgill.sable.soot.baf.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import edu.ksu.cis.bandera.jjjc.exception.CompilerException;
import edu.ksu.cis.bandera.util.*;
import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/*
 * BOFA.java
 * $Id: BOFA.java,v 1.8 2003/04/30 19:33:14 tcw Exp $
 */

/**
 * The main class of Bandera Object Flow Analysis.  It needs to be used along with JJJC of Bandera to
 * do Object Flow Analysis.  It can be used independently if soot support is available.  It serves as
 * a stub for executing independent of Bandera.  It can be invoked from the command prompt.
 *
 * @author <a href="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</a>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.8 $)
 */
public class BOFA
{

	public static final String FLOW_INSENSITIVE = new String("Flow-insensitive");

	public static final String FLOW_SENSITIVE = new String("Flow-sensitive");

	private static final String[] modesArray = { FLOW_INSENSITIVE, FLOW_SENSITIVE };

	public static final java.util.List modes =
		java.util.Collections.unmodifiableList(Arrays.asList(modesArray));

	/**
	 * Indicates whether the Jimple files of the analyzed class should be dumped or not.
	 *
	 */
	private static boolean dump = false;

	/**
	 * Indicates the current mode in which Bandera is functioning.  by default, it is set to flow
	 * insensitive mode of operation.
	 *
	 */
	static String mode;

	/**
	 * Classes in the code from where the analysis will commence.
	 *
	 */
	private static Collection entryPoints = new ArrayList();

	/**
	 * Collection of classes to be analyzed.
	 *
	 * This should include all classes which can be reached from the given classes.
	 *
	 */
	private static Collection storedClasses = null;

	/**
	 * <code>SootClassManager</code> object managing the classes to be analyzed.
	 *
	 */
	static SootClassManager sootClassManager = null;

	static Analysis analyser;

	/**
	 * Central registry of callbacks which can alter or influence the flow graph construction.
	 *
	 * One possible way is to add callbacks to hook the <code>run</code> method as a result of
	 * <code>start</code> method call.  This relation is not evident in the Java source code but
	 * rather in the native code.
	 *
	 */
	static CallBackRegistry fgConstructCallBackReg = new CallBackRegistry();

	/**
	 * Central registry of callbacks which collect data as flow occurs in the flow graph.
	 *
	 * One possible use it to collect start-run pairs.  Another use is to build the call graph.
	 *
	 */
	static CallBackRegistry flowProcessingCallBackReg = new CallBackRegistry();

	private static Logger logger;

	/*
	 * Initializes the logging API used in BOFA.
	 */
	static {
		try {
			logger = LogManager.getLogger(BOFA.class);
			mode = FLOW_INSENSITIVE;
			FA.setMode(mode);
		} catch (Exception  e) {
			e.printStackTrace();
		} // end of try-catch
	}

	static class EntryPoint {
		public final SootClass rClass;
		public final SootMethod rMethod;
		EntryPoint(SootClass rClass, SootMethod rMethod) {
			this.rClass = rClass;
			this.rMethod = rMethod;	
		}
	}

	/**
	 * Compiles the given java classes using JJJC.
	 *
	 * @param classPath the classpath required to compile java files.
	 * @param classNames the names of the Java files which need to be compiled.
	 * @param packageNames the names of the packages needed during compilation.
	 */
	public static void compile(String classPath, Collection classNames, Collection packageNames)
	{
		String[] packages = new String[packageNames.size()];
		SootClass sc;
		int j = 0;

		//System.out.println("Adding relevant classes...");
		logger.debug("Adding relevant classes...");
		for (Iterator i = packageNames.iterator(); i.hasNext(); j++) {
			packages[j] = (String) i.next();
		}

		try {
			CompilationManager.setClasspath(classPath);
			CompilationManager.setIncludedPackagesOrTypes(packages);
			for (Iterator i = classNames.iterator(); i.hasNext();) {
				CompilationManager.setFilename((String)i.next());
				CompilationManager.compile();
				CompilationManager.compile();
			}
		} catch (CompilerException e) {
			logger.error("Compilation failed.", e);
		} // end of try-catch
		/*
		Hashtable exceptions = CompilationManager.getExceptions();
		if (exceptions.size() > 0) {
			System.out.println("Compilation failed:");
			for (Enumeration e = exceptions.keys(); e.hasMoreElements();) {
				Object filename = e.nextElement();
				System.out.println("- " + filename);
				Vector es = (Vector) exceptions.get(filename);
				for (java.util.Iterator i = es.iterator(); i.hasNext();) {
					System.out.println("  * " + i.next());
				}
			}
		}
		*/
		storedClasses = Util.convert("ca.mcgill.sable.util.VectorList",
									 CompilationManager.getCompiledClasses().values());
		sootClassManager = CompilationManager.getSootClassManager();
	}

	/**
	 * Outputs the Jimple representation of all classes available through the class manager.
	 *
	 * @param outputPath the location where the jimple representation should be written.
	 *
	 * @exception RuntimeException if the jimple files could not be created or written.
	 */
	public static void dumpFile(String outputPath)
	{
		StoredBody bd = new StoredBody(Jimple.v());
		String className;
		File jimpFile;
		FileOutputStream jimpOut;
		SootClass sc;
		for (Iterator e = storedClasses.iterator(); e.hasNext();) {
			sc = (SootClass) e.next();
			className = sc.getName();
			try {
				jimpFile = new File(outputPath + File.separator + className + ".original.jimple");
				jimpOut = new FileOutputStream(jimpFile);
				sc.printTo(bd, new PrintWriter(jimpOut, true));
			} catch (IOException ex) {
				throw new RuntimeException("Could not dump jimple file (" + className + ")");
			}
		}
	}

	public static void setAnalysis(Analysis analyser)
	{
		BOFA.analyser = analyser;
	}

	/**
	 * Analyzes the loaded classes.
     *
	 * This interface can be used analyze a set of classes which are "jimplified" using <i>soot</i>
	 * directly.
	 *
	 * @param sootClassManager the class manager managing the classes to be analyzed.
	 * @param storedClasses a collection of compiled classes(<code>SootClass</code>) provided for
	 * analysis.
	 *
	 * @see #analyze() analyze
	 */
	public static void analyze(SootClassManager sootClassManager, Collection storedClasses)
	{
		BOFA.storedClasses = storedClasses;
		BOFA.sootClassManager = sootClassManager;
		analyze();
	}

	/**
	 * Analyzes the classes that are available through JJJC.
	 *
	 * It is assumed that the <code>sootClassManager</code> and the <code>storedClasses</code> has
	 * been initialized by a prior call to <code>compile</code> or <code>analyze(SootClassManager,
	 * Collection</code> was called.  If that is not the case, the available classes from the
	 * <code>edu.ksu.cis.bandera.jjjc.CompilationManager</code> are used to analyze.  The
	 * <code>rootSootClass</code> and <code>rootSootMethod</code> are set via {@link
	 * #setRootSootMethod setRootSootMethod} will be considered as the starting point of analysis.
	 * If either one of them is unset then the first <code>main</code> method encountered in the list
	 * of compiled classes will be considered as the starting point of analysis.
	 *
	 * @exception ClassesNotCompiledException if there are no compiled classes to be analyzed.
	 * @see #analyze(SootClassManager, Collection) analyze
	 * @see #compile compile
	 */
	public static void analyze()
	{
		SootClass sc;

		if (sootClassManager == null)
			sootClassManager = CompilationManager.getSootClassManager();
		if (storedClasses == null)
			storedClasses = Util.convert("ca.mcgill.sable.util.VectorList",
										 CompilationManager.getCompiledClasses().values());
		if (storedClasses == null || storedClasses.size() == 0) {
		    /*
			System.out.println("You need to compile before you do Flow Analysis" + storedClasses +
							   " ******** " + storedClasses.size());
		    */
		    logger.debug("You need to compile before you do Flow Analysis" + storedClasses +
							   " ******** " + storedClasses.size());
			class ClassesNotCompiledException extends RuntimeException {}
			throw new ClassesNotCompiledException();
		}

		if (mode == null) {
			throw new IllegalStateException("Mode has not been set.");
		} // end of if (mode == NO_MODE)


		if (analyser == null) {
			analyser = Analysis.init();
		} // end of if (analyser == null)

		//System.out.println("Running BOFA...");
		logger.debug("Running BOFA...");

		// Check if the user has set the rootSootClass and rootSootMethod.  if either one of them is
		// unset then pick the first main() method.
		if (entryPoints.size() == 0) {
			List mainParams = new ArrayList();
			List runParams = new ArrayList();
			mainParams.add(ArrayType.v(RefType.v("java.lang.String"), 1));
			Type retType = VoidType.v();
			// What if there is more than one main method?  What if there is no main method?
			for (Iterator e = storedClasses.iterator(); e.hasNext();) {
				sc = (SootClass) e.next();
				if (sc.declaresMethod("main", mainParams, retType)) {
					entryPoints.add(new EntryPoint(sc, sc.getMethod("main", mainParams, retType)));
				} else if (sc.declaresMethod("run", runParams, retType)) {
					entryPoints.add(new EntryPoint(sc, sc.getMethod("run", runParams, retType)));
				}
			}
		}
		/*
		 * we will insert hooks to be invoked when a new method implementation is called to extend
		 * the call graph.
		 */
		analyser.callbackInitFA(flowProcessingCallBackReg);
		analyser.callbackInitFG(fgConstructCallBackReg);
		FA.init(sootClassManager, entryPoints);
		FA.run();
	}

	/**
	 * Resets the complete Analysis engine.  All collected data are destroyed.
	 *
	 */
	public static void reset() {
		entryPoints.clear();
		sootClassManager = null;
		storedClasses = null;
		FA.reset();
		if (analyser != null) {
			analyser.reset();
		} // end of if (analyser != null)
    }

	/**
	 * Entry point to BOFA in stand alone mode.
	 *
	 * Currently nothing is displayed after the analysis.  It provides an assurance of whether the
	 * analysis went along fine or failed.
	 *
	 * @param args is the command line arguments.
	 */
	static void main(String[] args)
	{
		LinkedList packageNames = new LinkedList();
		LinkedList classNames = new LinkedList();
		String classPath = ".";
		String outputPath = ".";
		if (args.length == 0)
			printUsage();

		// command line processor
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("--help")) {
					printUsage();
				} else if (args[i].equals("--dump")) {
					dump = true;
				} else if (args[i].equals("--package")) {
					i++;
					while ((i < args.length) && (!args[i].startsWith("--"))) {
						packageNames.addLast(args[i].trim());
						i++;
					}
					i--;
				} else if (args[i].equals("--classpath")) {
					i++;
					if (i < args.length) {
						classPath = args[i];
					} else {
						printUsage();
					}
				} else if (args[i].equals("--outputdir")) {
					i++;
					if (i < args.length) {
						outputPath = args[i];
					} else {
						printUsage();
					}
				} else if (args[i].equals("--source")) {
					i++;
					while ((i < args.length) && (!args[i].startsWith("--"))) {
						classNames.addLast(args[i].trim());
						i++;
					}
					i--;
				} else {
				    //System.out.println("Error parsing argument: " + args[i]);
					logger.error("Error parsing argument: " + args[i]);
					printUsage();
				}
			}
		} catch (Exception ex) {
		    //System.out.println("Error in parsing arguments (" + ex + ")");
			logger.error("Error in parsing arguments (" + ex + ")");
			printUsage();
		}

		// adding relevant classes
		compile(classPath, classNames, packageNames);

		if (dump)
			dumpFile(outputPath);
		// invoke flow analyzer
		analyze();
	}

	/**
	 * Safe exit in case of wrong usage in command line mode.
	 */
	static void printUsage()
	{
		System.out.println("Wrong usage");
		System.out.println("java edu.ksu.cis.bandera.bofa.BOFA --source " +
						   "<source> [--dump] [--classpath <path>] " +
						   "[--package <package>] [--outpurdir <dir>]");
		System.exit(0);
	}

	/**
	 * Provides the methods from which the analysis started.
	 *
	 * @return the methods from which the analysis started.
	 */
	public static Collection getRootSootMethods()
	{
		Collection temp = new ArrayList();
		for (Iterator iter = entryPoints.iterator(); iter.hasNext();) {
			EntryPoint element = (EntryPoint) iter.next();
			temp.add(element.rMethod);
		}
		return temp;
	}

	/**
	 * Set the method from which the analysis should start.
	 *
	 * @param sm is the <code>SootMethod</code> to start the analysis from.
	 * @param sc is the <code>SootClass</code> in which <code>sm</code> is defined.
	 */
	public static void setRootSootMethod(SootMethod sm, SootClass sc) {
		entryPoints.add(new EntryPoint(sc, sm));
	}


	/**
	 * Get the current mode of analysis.
	 * @return the Mode value.
	 */
	public static String getMode() {
		return mode;
	}

	/**
	 * Set the mode of the analysis.  This method resets the engine after the mode is changed.
	 * @param newMode The new Mode value.
	 */
	public static boolean setMode(String newMode) {
		boolean ret = modes.contains(newMode);
		if (ret) {
			mode = newMode;
			FA.setMode(newMode);
			reset();
		} // end of if (modes.contains(newMode))
		return ret;
	}
}
