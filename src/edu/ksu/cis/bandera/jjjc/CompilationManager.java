package edu.ksu.cis.bandera.jjjc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Category;

import ca.mcgill.sable.laleh.java.astfix.JJCParser;
import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootClassManager;
import ca.mcgill.sable.soot.SootField;
import ca.mcgill.sable.soot.StoredBody;
import ca.mcgill.sable.soot.jimple.Jimple;
import ca.mcgill.sable.soot.jimple.Value;
import edu.ksu.cis.bandera.annotation.AnnotationManager;
import edu.ksu.cis.bandera.bofa.BOFA;
import edu.ksu.cis.bandera.jjjc.analysis.AnalysisAdapter;
import edu.ksu.cis.bandera.jjjc.codegenerator.JIJCCodeGenerator;
import edu.ksu.cis.bandera.jjjc.doc.DocTriple;
import edu.ksu.cis.bandera.jjjc.exception.AnalysisException;
import edu.ksu.cis.bandera.jjjc.exception.CompilerException;
import edu.ksu.cis.bandera.jjjc.gparser.GParser;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GDeviceConfigInfo;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GPhoneConfigInfo;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GProcessedSubscriptionInfo;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GSmartAppConfigInfo;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSubscriptionInfo;
import edu.ksu.cis.bandera.jjjc.gparser.util.GParserException;
import edu.ksu.cis.bandera.jjjc.lexer.Lexer;
import edu.ksu.cis.bandera.jjjc.node.Node;
import edu.ksu.cis.bandera.jjjc.node.Start;
import edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType;
import edu.ksu.cis.bandera.jjjc.symboltable.Name;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.UnicodePreprocessor;
import edu.ksu.cis.bandera.specification.assertion.AssertionExtractor;
import edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet;
import edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable;
import edu.ksu.cis.bandera.specification.predicate.PredicateExtractor;
import edu.ksu.cis.bandera.specification.predicate.datastructure.PredicateSet;
import edu.ksu.cis.bandera.util.Preferences;

/**
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.8 $ - $Date: 2003/04/30 19:33:24 $
 */
public class CompilationManager {

	private static Category log = Category.getInstance(CompilationManager.class);

	private static Hashtable<String, Start> fileTable;
	private static Hashtable<String, SymbolTable> symTable;
	private static Hashtable compiledClasses = new Hashtable();
	private static Hashtable<String, Vector<CompilerException>> exceptions = new Hashtable<String, Vector<CompilerException>>();
	private static Hashtable<String, Long> compiledFiles;
	private static AnnotationManager am;
	private static SootClassManager scm;
	private static boolean isRecompile;
	private static String[] includedPackagesOrTypes;
	private static String classpath;
	private static String filename;
	private static String[] lastIncluded;
	private static String lastClasspath;
	private static String lastFilename;
	private static Hashtable modifiedMethodTable = new Hashtable();
	private static Vector quantifiers = new Vector();
	private static Hashtable<String, QuantifiedVariable> quantifierTable = new Hashtable<String, QuantifiedVariable>();
	private static Vector syncTransformed = new Vector();
	private static Hashtable<Object, Object> docComments;
	private static String mainClassQualifiedName;
	private static java.lang.String mainClassSimpleName;
	private static java.util.Hashtable<String, SootField> fieldForQuantifiers;
	private static Vector<DocTriple> docTriples;
	private static boolean doBSL = true;
	private static HashSet<Value> bslAndPair;

	/***********************************************************/
	/* [Thomas, May 5, 2017]
	 * */
	public static String SmartThingsFile;
	private static boolean isSmartThingsCompiled = false;
	/***********************************************************/
	
	/**
	 * 
	 */
	private CompilationManager() {
		compiledFiles = new Hashtable<String, Long>();
	}
	/**
	 * 
	 * @param dt edu.ksu.cis.bandera.jjjc.javadoc.DocTriple
	 */
	public static void addDocTriple(DocTriple dt) {
		docTriples.add(dt);
	}
	/**
	 * 
	 * @param sf ca.mcgill.sable.soot.SootField
	 */
	public static void addFieldForQuantifier(SootField sf) {
		fieldForQuantifiers.put(sf.getName(), sf);
	}
	/**
	 * 
	 * @return edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
	 * @param className edu.ksu.cis.bandera.jjjc.symboltable.Name
	 */
	public static SymbolTable buildSymbolTable(Name className) throws CompilerException {
		ClassOrInterfaceType classOrInterfaceType = Package.getClassOrInterfaceType(className);

		if (!classOrInterfaceType.isLoaded()) {
			if (classOrInterfaceType.getPath() == null) {
				return new SymbolTable(className);
			} else
				return buildSymbolTable(classOrInterfaceType.getPath());
		}
		return classOrInterfaceType.getSymbolTable();
	}
	/**
	 * 
	 * @return edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
	 * @param filename java.lang.String
	 */
	public static SymbolTable buildSymbolTable(String filename) throws CompilerException {
		try {
			filename = new File(filename).getCanonicalPath();
		} catch (Exception e) {
		}
		if (getSymbolTable(filename) != null) {
			return getSymbolTable(filename);
		}
		SymbolTable symbolTable = new SymbolTable(filename);
		symTable.put(filename, symbolTable);
		return symbolTable;
	}
	/**
	 * 
	 */
	public static void compile() throws CompilerException {
		if (classpath == null)
		{
			throw new CompilerException("Classpath must be set first");
		} 
		else if (includedPackagesOrTypes == null)
		{
			throw new CompilerException("Included packages or types must be set first");
		}
		//System.setProperty("user.dir", edu.ksu.cis.bandera.bui.BUI.originalUserDir);
		File banderaBuiltinFile = Preferences.getBanderaBuiltinFile();
		String javaClasspath = System.getProperty("java.class.path");
		log.debug("javaClasspath = " + javaClasspath);
		String myPath = banderaBuiltinFile.getPath() + File.pathSeparator + classpath;
		log.debug("myPath = " + myPath);
		System.setProperty("java.class.path", myPath + File.pathSeparator + javaClasspath);
		Package.setClassPath(myPath);
		String smartAppPath = classpath + "/input/smartapps";
		File smartAppFoler = new File(smartAppPath);
		
		/* [Thomas, June 20, 2017]
		 * Initialize filename
		 * */
		for (File file : smartAppFoler.listFiles()) {
		    if (file.isFile() && file.getName().endsWith(".groovy")) {
		    		filename = smartAppPath + "/" + file.getName();
		    		break;
		    }
		}

		mainClassQualifiedName = null;
		mainClassSimpleName = new File(filename).getName();
		mainClassSimpleName = mainClassSimpleName.substring(0, mainClassSimpleName.indexOf("."));
		fieldForQuantifiers = new Hashtable<String, SootField>();
		isRecompile = false;
		if (("" + lastFilename).equals("" + filename) && ("" + lastClasspath).equals("" + classpath))
		{
			if (lastIncluded != null)
			{
				if (lastIncluded.length == includedPackagesOrTypes.length)
				{
					boolean f = true;
					for (int i = 0; i < includedPackagesOrTypes.length; i++)
					{
						if (!(lastIncluded[i]).equals(includedPackagesOrTypes[i]))
						{
							f = false;
							break;
						}
					}
					if (f)
						isRecompile = true;
				}
			}
		}
		if (isRecompile)
		{
			if (compiledFiles.size() == 0)
				isRecompile = false;
			for (Enumeration<String> e = compiledFiles.keys(); e.hasMoreElements();)
			{
				String filename = e.nextElement();
				long lastModified = compiledFiles.get(filename).longValue();
				long currentLastModified = new File(filename).lastModified();
				if (lastModified != currentLastModified)
				{
					isRecompile = false;
				}
			}
		}
		am = new AnnotationManager();
		scm = new SootClassManager();
		exceptions = new Hashtable<String, Vector<CompilerException>>();
		compiledClasses = new Hashtable();
		symTable = new Hashtable<String, SymbolTable>();
		compiledFiles = new Hashtable<String, Long>();
		docTriples = new Vector<DocTriple>();
		bslAndPair = new HashSet<Value>();
		if (!isRecompile)
		{
			fileTable = new Hashtable<String, Start>();
			lastFilename = filename;
			lastClasspath = classpath;
			lastIncluded = includedPackagesOrTypes;
			syncTransformed = new Vector();
			docComments = new Hashtable<Object, Object>();
		}
		JIJCCodeGenerator codeGenerator = null;
		{
			SootClass bandera = scm.getClass("IotSan");
			bandera.resolve();
		}
		
		try
		{
			/* [Thomas, June 2nd, 2017]
			 * We first need to compile SmartThings.java to load
			 * SmartThings' classes and APIs
			 * */
			if(!isSmartThingsCompiled)
			{
				isSmartThingsCompiled = true;
				compile(SmartThingsFile);
			}
			
			/* [Thomas, June 20, 2017]
			 * Compile all Groovy files in smartAppPath directory
			 * */
			for (File file : smartAppFoler.listFiles()) {
			    if (file.isFile() && file.getName().endsWith(".groovy")) {
			    		filename = smartAppPath + "/" + file.getName();
			    		compile(findFile(filename));
			    }
			}
			
			/*int numClasses = 1;
			Object[] temp = scm.getClasses().toArray();
			while (numClasses != temp.length)
			{
				for (int i = 0; i < temp.length; i++)
				{
					SootClass sc = (SootClass) temp[i];
					String className = sc.getName().trim();
					Name qName = new Name(className);
					if (isCompile(includedPackagesOrTypes, qName) && (compiledClasses.get(className) == null))
					{
						String filename = null;
						ClassOrInterfaceType type = Package.getClassOrInterfaceType(qName);
						if (type.getPath() == null)
							continue;
						for (Enumeration e = type.getContainingPackage().getPaths(); e.hasMoreElements();)
						{
							try
							{
								String packagePath = (String) e.nextElement();
								if (packagePath.equals(""))
								{
									filename = qName.getLastIdentifier().toString().trim() + ".java";
								} else
								{
									filename = packagePath + File.separator + qName.getLastIdentifier().toString().trim() + ".java";
								}
							} catch (Exception ex)
							{
							}
						}
						if ((filename != null) && (compiledClasses.get(filename) == null))
						{
							if (new File(filename).exists())
								codeGenerator = compile(filename);
							else
							{
								codeGenerator = compile(findFile(qName.getLastIdentifier().toString().trim() + ".java"));
							}
						}
					}
				}
				numClasses = temp.length;
				temp = scm.getClasses().toArray();
			}

			if (mainClassQualifiedName == null) {
				throw new CompilerException("Cannot find the main class '" + mainClassSimpleName + "'");
			}

			log.debug("Compiled Classes:");
			if(log.isDebugEnabled()) {
				for (Enumeration<SootClass> e = compiledClasses.elements(); e.hasMoreElements();) {
					SootClass sc = e.nextElement();
					//System.out.println(sc.getName());
					log.debug(sc.getName());
				}
			}

			// invoke bsl
			if (doBSL) {
				AssertionSet.reset();
				PredicateSet.reset();
				Vector<CompilerException> assertionErrors = new Vector<CompilerException>();
				Vector<CompilerException> predicateErrors = new Vector<CompilerException>();
				for (Iterator<DocTriple> i = docTriples.iterator(); i.hasNext();) {
					DocTriple dt = i.next();
					assertionErrors.addAll(AssertionExtractor.extract(dt.sc, dt.sm, dt.tags));
					predicateErrors.addAll(PredicateExtractor.extract(dt.sc, dt.sm, dt.tags));
				}
				if (assertionErrors.size() > 0)
					exceptions.put("Assertion errors:", assertionErrors);
				if (predicateErrors.size() > 0)
					exceptions.put("Predicate errors:", predicateErrors);
			}

			{
				try { BOFA.reset(); } catch (Exception e)
				{ System.out.println("BOFA is already reset."); }
				try { BOFA.analyze(); } catch (Exception e)
				{
					System.out.println("BOFA Failed! "+e.getMessage());
				}

			}*/
		} catch (CompilerException e)
		{
			Vector<CompilerException> exceptions = (codeGenerator != null) ? codeGenerator.getExceptions() : new Vector();
			exceptions.addElement(e);
			CompilationManager.exceptions.put(filename, exceptions);
			
			System.out.println("[compile] " + e.getMessage());
		}
		finally
		{
			System.setProperty("java.class.path", javaClasspath);
		}
	}
	/**
	 * @return edu.ksu.cis.bandera.jjjc.JIJCCodeGenerator
	 * @param filename java.lang.String
	 */
	private static JIJCCodeGenerator compile(String filename) throws CompilerException {
		File file = new File(filename);
		filename = file.getAbsolutePath();

		System.out.println("Compiling: " + filename);
		log.debug("Compiling: " + filename);

		System.out.println("Parsing...");
		log.debug("Parsing...");
		Start ast = null;
		try {
			ast = parseFile(filename);
			compiledFiles.put(filename, new Long(file.lastModified()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnalysisException(e.toString());
		}

		System.out.println("Building symbol table...");
		log.debug("Building symbol table...");
		SymbolTable symbolTable = buildSymbolTable(filename);

		if (mainClassQualifiedName == null) {
			//	    String dotSimpleName = "." + mainClassSimpleName;
			for (Enumeration e = symbolTable.getDeclaredTypes(); e.hasMoreElements();) {
				ClassOrInterfaceType type = (ClassOrInterfaceType) e.nextElement();
				String typeName = type.getFullyQualifiedName();
				//		if (typeName.endsWith(dotSimpleName) || typeName.equals(mainClassSimpleName)) {
				if (!typeName.startsWith("ST")) {
					mainClassQualifiedName = typeName;
					break;
					/* [Thomas, May 4, 2017]
					 * Ignore the check
					 * */
					//		    if (!type.getMethods(new Name("main")).hasMoreElements()) {
					//			throw new CompilerException("Main Java file should contain a main method");
					//		    }
				}
			}
		}

		JIJCCodeGenerator codeGenerator = new JIJCCodeGenerator(symbolTable, scm, am, docComments);
		//System.out.println("Generating code...");
		log.debug("Generating code...");
		try {
			ast.apply(codeGenerator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("------------------------");
		//System.out.println("Finished\n");
		log.debug("Finished");
		Vector classes = codeGenerator.getCompiledClasses();
		for (int i = 0; i < classes.size(); i++) {
			compiledClasses.put(classes.elementAt(i), scm.getClass((String) classes.elementAt(i)));
		}
		if (codeGenerator.getExceptions().elements().hasMoreElements()) {
			exceptions.put(filename, codeGenerator.getExceptions());
		}
		System.gc();
		return codeGenerator;
	}
	/**
	 * 
	 * @param filename java.lang.String
	 * @param classpath java.lang.String
	 * @param includedPackagesOrTypes java.lang.String[]
	 */
	public void compile(String filename, String classpath, String[] includedPackagesOrTypes) throws CompilerException {
		setFilename(filename);
		setClasspath(classpath);
		setIncludedPackagesOrTypes(includedPackagesOrTypes);
		compile();
	}

	// robbyjo's hack begin
	private static String findFile(String f)
	{
		StringTokenizer tok = new StringTokenizer(classpath, File.pathSeparator);

		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			if (!token.endsWith(File.separator)) token += File.separator;
			if (new File(token+f).exists()) return token+f;
		}

		int max = includedPackagesOrTypes.length;
		for (int i = 0; i < max; i++)
		{
			String token = includedPackagesOrTypes[i];
			if (!token.endsWith(File.separator)) token += File.separator;
			if (new File(token+f).exists()) return token+f;
		}

		File parentFile = new File(filename).getParentFile();
		if (parentFile != null)
		{
			String path = parentFile.getAbsolutePath() + File.separator + f;
			if (new File(path).exists()) return path;
		}

		return new File(f).getAbsolutePath();
	}
	// robbyjo's hack end

	/**
	 * 
	 * @return edu.ksu.cis.bandera.annotation.AnnotationManager
	 */
	public static AnnotationManager getAnnotationManager() {
		return am;
	}
	/**
	 * 
	 * @return edu.ksu.cis.bandera.jjjc.node.Node
	 * @param filename java.lang.String
	 */
	public static Node getAST(String filename) {
		return fileTable.get(filename);
	}
	public static String getClasspath() {
		return classpath;
	}
	/**
	 * 
	 * @return java.util.Hashtable
	 */
	public static Hashtable getCompiledClasses() {
		return compiledClasses;
	}
	/**
	 * 
	 * @return java.util.Hashtable
	 */
	public static Hashtable<String, Vector<CompilerException>> getExceptions() {
		return exceptions;
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.SootField
	 * @param name java.lang.String
	 */
	public static SootField getFieldForQuantifier(String name) {
		return fieldForQuantifiers.get(name);
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.SootClass
	 */
	public static SootClass getMainSootClass() {
		return scm.getClass(mainClassQualifiedName);
	}
	/**
	 * 
	 * @return java.util.Hashtable
	 */
	public static java.util.Hashtable getModifiedMethodTable() {
		return modifiedMethodTable;
	}
	/**
	 * 
	 * @return edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable
	 * @param name java.lang.String
	 */
	public static QuantifiedVariable getQuantifier(String name) {
		return quantifierTable.get(name);
	}
	/**
	 * 
	 * @return java.util.Vector
	 */
	public static java.util.Vector getQuantifiers() {
		return quantifiers;
	}
	/**
	 * 
	 * @return ca.mcgill.sable.soot.SootClassManager
	 */
	public static SootClassManager getSootClassManager() {
		return scm;
	}
	/**
	 * 
	 * @return edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
	 * @param filename java.lang.String
	 */
	public static SymbolTable getSymbolTable(String filename) {
		return symTable.get(filename);
	}
	/**
	 * 
	 * @return boolean
	 * @param name java.lang.String
	 */
	private static boolean isCompile(String packageNames[], Name qName) {
		boolean compile = false;
		if (qName.toString().equals("IotSan")) return false;
		if (qName.isSimpleName()) {
			compile = true;
		} else
			if (packageNames != null) {
				String name = qName.toString().trim();
				String sName = qName.getSuperName().toString().trim();
				for (int j = 0; j < packageNames.length; j++) {
					String packageName = packageNames[j];
					if ((packageName != null) && (name.equals(packageName) || sName.equals(packageName))) {
						compile = true;
						j = packageNames.length;
					}
				}
			}
		return compile;
	}
	/**
	 * 
	 * @return boolean
	 */
	public static boolean isDoBSL() {
		return doBSL;
	}
	/**
	 * 
	 * @return boolean
	 * @param node edu.ksu.cis.bandera.jjjc.node.Node
	 */
	public static boolean isSynchTransformed(Node node) {
		return syncTransformed.contains(node);
	}
	/**
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(String args[]) {
		String classPath;
		String javaFileName;
		if (args.length < 2) {
			System.out.println("usage:");
			System.out.println("  java CompilationManager <classpath> <main java file> [packages]");
			return;
		}
		classPath = args[0];
		javaFileName = args[1];

		String[] packageNames = new String[args.length - 1];
		packageNames[0] = "";

		for (int i = 0; i < packageNames.length - 1; i++) {
			packageNames[i + 1] = args[i + 2];
		}

		setClasspath(classPath);
		setFilename(javaFileName);
		setIncludedPackagesOrTypes(packageNames);
		try {
			compile();
		} catch (Exception e) {
			System.out.println(e);
		}
		for (Enumeration<SootClass> e = compiledClasses.elements(); e.hasMoreElements();) {
			SootClass sc = e.nextElement();
			try {
				sc.printTo(new StoredBody(Jimple.v()), new PrintWriter(new FileWriter(sc.getName() + ".jimple"), true));
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		System.exit(0);
	}
	/**
	 * 
	 * @return edu.ksu.cis.bandera.jjjc.node.Start
	 * @param filename java.lang.String
	 */
	@SuppressWarnings("unchecked")
	public static Start parseFile(String filename) throws Exception {
		filename = new File(filename).getCanonicalPath();
		if (fileTable.get(filename) != null)
			return fileTable.get(filename);
		
		Start ast;
		if(filename.endsWith(".java"))
		{
			FileReader fr = new FileReader(filename);
			UnicodePreprocessor preprocessor = new UnicodePreprocessor(
					new PushbackReader(new BufferedReader(fr, 8192), 1024));
			Lexer lexer = new Lexer(new PushbackReader(preprocessor, 1024));
			JJCParser parser = new JJCParser(lexer);
			ast = parser.parse();
			fr.close();
			
			/* [Thomas, June 4th, 2017]
			 * Extract the STCommonAPIs from the ast
			 * */
			if(filename.endsWith("SmartThings.java"))
			{
				GParser.loadJSTCommonAPIs(ast);
			}
			
			Hashtable table = ((AnalysisAdapter) parser.ignoredTokens).getIn();
			for (Enumeration e = table.keys(); e.hasMoreElements();) {
				Object key = e.nextElement();
				Object value = table.get(key);
				if (value.toString().indexOf("/**") >= 0) {
					docComments.put(key, value);
				}
			}
		}
		else if(filename.endsWith(".groovy"))
		{
			GParser gParser = new GParser(filename);
			try {
				ast = gParser.parse();
			} 
			catch(IOException e){
				System.out.println(e.getMessage());
				throw new Exception("[parseFile] error in parsing " + filename);
			} 
			catch(GParserException e){
				System.out.println(e.getMessage());;
				throw new Exception("[parseFile] error in parsing " + filename);
			}
		}
		else
		{
			System.out.println("[parseFile] unexpected file format " + filename);
			throw new Exception("[parseFile] unexpected file format " + filename);
		}
		
		fileTable.put(filename, ast);
		ast.apply(new edu.ksu.cis.bandera.jjjc.ast.SynchronizedMethodTransformer(syncTransformed, docComments));
		return ast;
	}
	
	/**
	 * 
	 */
	public static void reset() {
		compiledClasses = new Hashtable();
		exceptions = new Hashtable<String, Vector<CompilerException>>();
		CompilationManager.setFilename(null);
		CompilationManager.setClasspath(null);
		CompilationManager.setIncludedPackagesOrTypes(null);
	}
	/**
	 * 
	 * @param classpath java.lang.String
	 */
	public static void setClasspath(String classpath) {
		CompilationManager.classpath = classpath;
		CompilationManager.SmartThingsFile = classpath + "/lib/SmartThings.java";
	}
	/**
	 * 
	 * @param newDoBSL boolean
	 */
	public static void setDoBSL(boolean newDoBSL) {
		doBSL = newDoBSL;
	}
	/**
	 * 
	 * @param filename java.lang.String
	 */
	public static void setFilename(String filename) {
		CompilationManager.filename = filename;
	}
	/**
	 * 
	 * @param includedPackagesOrTypes java.lang.String[]
	 */
	public static void setIncludedPackagesOrTypes(String[] includedPackagesOrTypes) {
		if (CompilationManager.includedPackagesOrTypes == includedPackagesOrTypes) return;
		if ((CompilationManager.includedPackagesOrTypes != null) &&
				(includedPackagesOrTypes != null)) {
			if (CompilationManager.includedPackagesOrTypes.length == includedPackagesOrTypes.length) {
				boolean f = true;
				for (int i = 0; i < includedPackagesOrTypes.length; i++) {
					if (!("" + CompilationManager.includedPackagesOrTypes[i]).equals("" + includedPackagesOrTypes[i])) {
						f = false;
						break;
					}
				}
				if (f) return;
			}
		}
		CompilationManager.includedPackagesOrTypes = includedPackagesOrTypes;
		isRecompile = false;
	}
	/**
	 * 
	 * @param newModifiedMethodTable java.util.Hashtable
	 */
	public static void setModifiedMethodTable(java.util.Hashtable newModifiedMethodTable) {
		modifiedMethodTable = newModifiedMethodTable;
	}
	/**
	 * 
	 * @param newQuantifiers java.util.Vector
	 */
	public static void setQuantifiers(java.util.Vector newQuantifiers) {
		quantifiers = newQuantifiers;
		quantifierTable = new Hashtable<String, QuantifiedVariable>();
		for (Iterator i = quantifiers.iterator(); i.hasNext();) {
			QuantifiedVariable qv = (QuantifiedVariable) i.next();
			quantifierTable.put(qv.getName(), qv);
		}
	}

	public static void addLocPredicatePair(Value v)
	{
		bslAndPair.add(v);
	}

	public static boolean hasLocPredicatePair(Value v)
	{
		return bslAndPair.contains(v);
	}
}
