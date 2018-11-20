package edu.ksu.cis.bandera.bui;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootMethod;
import ca.mcgill.sable.soot.StoredBody;
import edu.ksu.cis.bandera.abstraction.typeinference.TypeTable;
import edu.ksu.cis.bandera.annotation.AnnotationManager;
import edu.ksu.cis.bandera.birc.Builder;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import edu.ksu.cis.bandera.jjjc.decompiler.Decompiler;
import edu.ksu.cis.bandera.jjjc.exception.CompilerException;
import edu.ksu.cis.bandera.jjjc.gparser.GParser;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GConfigInfoManager;
import edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener.GPotentialRiskScreener;
import edu.ksu.cis.bandera.prog.IdentifyRunnableClasses;
import edu.ksu.cis.bandera.prog.Inline;
import edu.ksu.cis.bandera.prog.KSUOptimizing;
import edu.ksu.cis.bandera.prog.ThreadExpand;
import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.spin.SpinTrans;
import edu.ksu.cis.bandera.spin.SpinUtil;
import edu.ksu.cis.bandera.util.BanderaUtil;

public class GDriver {
	private static String originalClasspath = System.getProperty("java.class.path");
	private static String originalUserDir = System.getProperty("user.dir");
	private TypeTable typeTable = new TypeTable();
	private String classPath;
	private edu.ksu.cis.bandera.bir.TransSystem sys;
	private SootClass[] sootClasses;
	private boolean classificationMode;

	/**
	 * Create a new GDriver.
	 */
	public GDriver(boolean classificationMode) {
		this.classificationMode = classificationMode;
	}

	/**
	 *
	 */
	private void dump(String phase) {
		String path = getTempDir(phase);
		if (!BanderaUtil.mkdirs(path)) {
			System.out.println("***Can't make temporary directory for dumping jimple!");
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
				System.out.println(ex.getMessage());
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
			System.out.println("***Can't make temporary directory for dumping jimple!");
			return "";
		}

		Decompiler decompiler = new Decompiler(CompilationManager.getCompiledClasses(), CompilationManager.getAnnotationManager(), path, "java");
		String output = decompiler.decompile();
		if (tempSuffix.equals("JPF"))
			CompilationManager.getAnnotationManager().setFilenameLinePairAnnotationTable(decompiler.getLineToAnnotationTable());

		return (new File(path)).getAbsolutePath();
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
	 * @return ca.mcgill.sable.soot.SootClass[]
	 */
	private SootClass[] processCompiledSootClasses() {
		Hashtable table = CompilationManager.getCompiledClasses();
		Vector v = new Vector();
		//v.add(CompilationManager.getMainSootClass());
		for (Enumeration e = table.elements(); e.hasMoreElements();) {
			SootClass sc = (SootClass) e.nextElement();
			
			if (SpinUtil.containEvtHandlerMethod(sc) && !v.contains(sc)) {
				v.add(sc);
			}
		}
		sootClasses = new SootClass[v.size()];
		int i = 0;
		for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
			sootClasses[i] = (SootClass) e.nextElement();
		}
		return sootClasses;
	}
	
	private SootClass[] getSootClassesForBIRC(Set<String> smartAppNames) {
		Vector<SootClass> v = new Vector<SootClass>();
		
		for (SootClass sc : this.sootClasses) {
			if (smartAppNames.contains(sc.getName()) && !v.contains(sc)) {
				v.add(sc);
			}
			/* Test code */
//			if(sc.getName().equals("STInitializer") && !v.contains(sc))
//			{
//				v.add(sc);
//			}
		}
		SootClass[] result = new SootClass[v.size()];
		SootClass scSTInitializer = null;
		int i = 1;
		for (Enumeration<SootClass> e = v.elements(); e.hasMoreElements();) {
			SootClass sc = e.nextElement();
			if(sc.getName().equals("STInitializer"))
			{
				scSTInitializer = sc;
			}
			else
			{
				result[i++] = sc;
			}
		}
		result[0] = scSTInitializer;
		return result;
	}

	public static String getTempDir(String suffix) {
		String s = System.getProperty("user.dir") + File.separator + "output" + File.separator + "IotSanOutput";

		if (suffix == null || suffix.equals("")) return s;
		return s + File.separator + suffix;
	}

	public void init(String project_root) {
		this.classPath = project_root;
		
		GPotentialRiskScreener.init(project_root);
		GParser.init(project_root);
		GConfigInfoManager.init(project_root, this.classificationMode);
		
		/* Prepare the Compilation Manager */
		CompilationManager.reset();
//		CompilationManager.setFilename(mainClassFile);
		CompilationManager.setClasspath(this.classPath);
		CompilationManager.setIncludedPackagesOrTypes(new String[0]);
	}

	/**
	 *
	 */
	public void run() {
		System.out.println("Running ...");

		try {
			System.setProperty("user.dir", originalUserDir);
			System.setProperty("java.class.path", this.classPath + File.pathSeparator + originalClasspath);
			
			GPotentialRiskScreener.run(this.classificationMode);
			runJJJC();
			runBIRC();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		CompilationManager.setQuantifiers(new Vector());

		System.out.println("Finished running.");
	}

	/**
	 *
	 */
	private void runBIRC() {
		System.out.println("Running BIRC ...");

		String defaultName = "SmartThings";
		try {
			System.out.println("Running Inliner ...");

			AnnotationManager am = CompilationManager.getAnnotationManager();
			Inline.typeTable = typeTable;
			HashSet methods = getMethodsForInliner();
			java.util.List<SootClass> scList = new ArrayList<SootClass>();
			
			dump("beforeInlined");
			for (Iterator i = methods.iterator(); i.hasNext();) {
				SootMethod sm = (SootMethod) i.next();
//				if(sm.getName().equals("appTouchEvtHandler"))
//				{
//					System.out.println("appTouchEvtHandler");
//				}
//				if(sm.getName().equals("contactClosedEvtHandler"))
//				{
//					System.out.println("contactClosedEvtHandler");
//				}
//				if(sm.getName().equals("changedLocationModeEvtHandler"))
//				{
//					System.out.println("changedLocationModeEvtHandler");
//				}
				Inline.inline(sm, am, false);
				if(!scList.contains(sm.getDeclaringClass()))
				{
					scList.add(sm.getDeclaringClass());
				}
			}
			Inline.typeTable = null;
			SootClass[] classesForBIRC = new SootClass[scList.size()];
			int index = 0;
			for(SootClass sc : scList)
			{
				classesForBIRC[index++] = sc;
			}
			dump("inlined");
			System.out.println("Finished Inliner.");

			KSUOptimizing.packLocalsForClasses(classesForBIRC);
			dump("packed");

			ThreadExpand.expandStart(classesForBIRC, am);
			dump("expanded");
			
			processCompiledSootClasses();
			
			String tempPath = getTempDir("birc");
			if (!BanderaUtil.mkdirs(tempPath)) {
				System.out.println("***Can't make temporary directory to store model checker output!");
				return;
			}

			if(this.classificationMode)
			{
				/* Classification mode */
				List<Map<String, List<String>>> allNewAppConfigInfo = GConfigInfoManager.getGeneratedConfigInfoList();
				int generatedFileIndex = 0;
				
				for(Map<String, List<String>> inputInfoMap : allNewAppConfigInfo)
				{
					/* Set config info for new smart app */
					GConfigInfoManager.putNewSmartAppConfigInfo(inputInfoMap);
	
					for(int treeIndex = 0; treeIndex < GPotentialRiskScreener.dependentForest.size(); treeIndex++)
					{
						defaultName = "SmartThings" + generatedFileIndex++;
						sys = Builder.createTransSystem(this.getSootClassesForBIRC(GPotentialRiskScreener.getSmartAppNames(treeIndex)), 
								defaultName, null, null);
						
						/* Translate into Promela */
						{
							SpinTrans spinTrans = null;
			
							try {
								spinTrans = SpinTrans.translate(sys, null, tempPath);
							}
							catch(java.lang.Error e) {
								System.err.println("Caught Error while translating using SpinTrans: " + e.toString());
								throw e;
							}
							spinTrans.cleanMess();
						}
					}
				}
			}
			else
			{
				/* Verification mode */
				for(int treeIndex = 0; treeIndex < GPotentialRiskScreener.dependentForest.size(); treeIndex++)
				{
					defaultName = "SmartThings" + treeIndex;
					sys = Builder.createTransSystem(this.getSootClassesForBIRC(GPotentialRiskScreener.getSmartAppNames(treeIndex)), 
							defaultName, null, null);
					
					/* Translate into Promela */
					{
						SpinTrans spinTrans = null;
		
						try {
							spinTrans = SpinTrans.translate(sys, null, tempPath);
						}
						catch(java.lang.Error e) {
							System.err.println("Caught Error while translating using SpinTrans: " + e.toString());
							throw e;
						}
						spinTrans.cleanMess();
					}
				}
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println("Finished running BIRC.");
	}

	private boolean runJJJC() {
		System.out.println("Running JJJC ...");

		try {
			CompilationManager.compile();
			dump("fixme");
			generateJava("fixme");
		} catch (CompilerException e) {
			System.out.println("Exception while compiling: " + e.toString());
			return false;
		}
		System.out.println("Finished running JJJC.");

		return true;
	}
}
