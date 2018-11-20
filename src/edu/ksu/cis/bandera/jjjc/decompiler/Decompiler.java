package edu.ksu.cis.bandera.jjjc.decompiler;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000 Roby Joehanes (robbyjo@cis.ksu.edu)            *
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

import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.abstraction.typeinference.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.node.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.Category;

/**
 * <P>Decompiler package is basically the main semi-decompiler class. All components of Bandera
 * works on Jimple-level, which format resembles the Java byte-code.
 * Sometimes, it is preferable to make the intermediate results, like after
 * slicing or abstraction available for user view. This is the need of Decompiler
 * package. Moreover, we definitely need the decompiler in order to use
 * JPF module checker since JPF input is in Java.
 *
 * <P>Why it is called a semi-decompiler? It is because of the usage of annotations
 * in decompiling the codes. The annotation created by the compiler, JJJC, is
 * used extensively to retrieve variable names and all sort of things so that
 * we don't need to deal directly with the Jimple, which makes things a lot
 * simpler.
 *
 * <P>Note that if you inspect this code, you'll see that this solution is somewhat
 * patchy. Yes, it is because other component may inline and do some optimizing
 * stuffs (and probably some other non-reversible actions) to the code. So, there is
 * no way that I can cover all cases (like jumping into an if block). This is it,
 * don't complain. If you don't like it, don't use it.
 *
 * <P>If you're interested on how the decompiler are used in Bandera, you should
 * only resort in Decompiler class, decompile method, in particular.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class Decompiler {

    private static final Category log = Category.getInstance(Decompiler.class);

	private Hashtable classes = null;
	static AnnotationManager annot = null;
	public static String fileSuffix = ".java";
	private String outputPath = ".";
	private String outFilename = "";
	private static Vector imports = new Vector();
	private DecompilerPrinter src = new DecompilerPrinter();
	private int lineCounter;
	private Hashtable lineToAnnotation = new Hashtable();
	static TypeTable typeTable = null;
	public static String ln = System.getProperty("line.separator");

/**
 * You need to supply the hashtable of compiled classes, the annotation manager,
 * output path, and file suffix (usually <TT>".java"</TT>).<BR><BR>
 *
 * The usage is something like this:<BR>
 *    <TT>Slabs decompiler = new Slabs(classes, annotMgr, "/usr/pub/mydir", ".java");</TT><BR>
 * then:<BR>
 *    <TT>decompiler.decompile();</TT><BR>
 * You're done. Currently LineNumberToAnnotation is <b>not</b> implemented yet.
**/
public Decompiler(Hashtable cls, AnnotationManager am, String path, String suffix)
{
	this(cls,am,path,suffix,null);
}
public Decompiler(Hashtable cls, AnnotationManager am, String path, String suffix, TypeTable tt)
{
	classes = cls;
	annot = am;
	if (path != null)
	{
		outputPath = path;
		DecompilerPair.setOutputPath(path);
	}
	if (suffix != null) fileSuffix = suffix;
	typeTable = tt;
}
/**
 * This is meant to add some <TT>import</TT> statements
 * in order to resemble the original. This feature is
 * probably added whenever I have some time.
**/
public static void addImports(String cls)
{
	if (!imports.contains(cls))
	{
		// Add the import statement
		imports.addElement(cls);
	}
}
/**
 * Decompiles all the classes. See constructor's notes
*/
public String decompile()
{
	StringBuffer dump = new StringBuffer();
	//System.out.println("Decompiling:");
	//System.out.print("~~~~~~~~~~~~");
	log.debug("Decompiling:");
	log.debug("~~~~~~~~~~~~");
	for (Enumeration e = classes.elements(); e.hasMoreElements();)
	{
		SootClass sc = (SootClass) e.nextElement();

		dump(sc);
		// Reset the pretty printer
		src.reset();

		// Reset import statements
		imports = new Vector();

		// Determine the header of the Class
		DecompilerInfo.setClass(sc);

		String cn = DecompilerInfo.getClassName();
		String filePath = "";

		// Resolving package names
		if (cn.indexOf(".")!= -1)
		{
			int lastDot = cn.lastIndexOf(".");
			String className = cn.substring(lastDot+1);
			String pkgName = cn.substring(0,lastDot);
			StringTokenizer tok = new StringTokenizer(pkgName,".");
			StringBuffer buf = new StringBuffer();
			while (tok.hasMoreTokens())
			{
				buf.append(File.separator + tok.nextToken());
			}
			filePath = buf.toString();
			addImports("package "+pkgName);
			cn = className;
		}

		String curFileName = cn+"."+fileSuffix;
		//System.out.println("\n"+curFileName);
		//System.out.print("  *** ");
		log.debug("\n"+curFileName);
		log.debug("  *** ");
		curFileName = File.separator + curFileName;

		// Hack all methods, we still can't know the field since all
		// initializations are in <init> or <clinit> methods.
		Vector buf = new Vector();
		lineCounter = 1;
		Hashtable curTable = new Hashtable();

		for (ca.mcgill.sable.util.Iterator it = sc.getMethods().iterator(); it.hasNext();)
		{
			SootMethod sm = (SootMethod) it.next();
			Annotation ma = annot.getAnnotation(sc, sm);

			// Aware the parameter manager
			DecompilerInfo.setMethod(sm);

			// Reset the goto mapper
//			DecompilerGotoMapper.reset();

			// Reset the temporary table
			DecompilerUtil.resetTempTable();

			// Decipher the annotations
			if (DecompilerInfo.isJimpleInitializer())
				DecompilerUtil.setAllowField(true);
			else
				DecompilerUtil.setAllowField(false);

			String curName = DecompilerInfo.getMethodName();
			//System.out.print(curName+"; ");
			log.debug(curName+"; ");
			
			DecompilerSwitch.reset();
			if (!DecompilerInfo.isInterface() && !DecompilerInfo.isAbstract())
			{
				Vector body = DecompilerSwitch.evaluate(ma);
				String hdr = DecompilerInfo.getMethodDeclaration();

				// Then print it!
				if ((body.size()>=1) && (((String) body.get(body.size()-1)).equals("return;")))
				{
					body.removeElementAt(body.size()-1);
				}
				if (hdr.length()>0)
				{
					boolean isInit = DecompilerInfo.isInitializer();
					if (((isInit) && ((body.size()>0) || (DecompilerInfo.getNoOfParam()>0))) || (!isInit))
					{
						Hashtable tbl = DecompilerSwitch.getLineToAnnotation();
						if (body.size()>0)
						{
							String syn = (String) body.get(0);
							if ((syn.startsWith("synchronized (this)")) || (syn.startsWith("synchronized (java.lang.Class.forName(")))
//							if (syn.startsWith("synchronized (java.lang.Class.forName("))
							{
								DecompilerInfo.setSynchro(true);
								hdr = DecompilerInfo.getMethodDeclaration();
								body.removeElementAt(0);
								body.removeElementAt(0);
								body.removeElementAt(body.size()-1);
								renumber(tbl, -2);
							}
						}
						// Add method annotation too
						curTable.put(new DecompilerPair(lineCounter), ma);
						lineCounter += 2;
						buf.add("");
						buf.add(hdr);
						buf.add("{");
						renumber(tbl,lineCounter);
						curTable.putAll(tbl);
						buf.addAll(body);
						lineCounter += body.size()+2;
						buf.add("}");
					}
				}
			} else
			{
				// Abstract or Interface
				if (ma != null)
				{
					curTable.put(new DecompilerPair(lineCounter), ma);
					lineCounter++;
					String hdr = DecompilerInfo.getMethodDeclaration();
					buf.add(hdr+";");
				}
			}
		}

		int importSize = imports.size();
		if (importSize > 0)
		{
			renumber(curTable, importSize+1);
			src.println(printImports());
		}
		src.println(DecompilerInfo.getClassDeclaration());
		src.println("{");

		// After all methods are hacked, the fields should be filled up with
		// default values. So, fields should be able to be printed now.
		Vector fieldDeclaration = DecompilerInfo.getFieldDeclaration();
		renumber(curTable, fieldDeclaration.size()+3);
		src.add(fieldDeclaration);

		// Then print all the methods we've processed so far.
		src.add(buf);
		src.println("}");

		// Add the class declaration annotation.
		Annotation ann = annot.getAnnotation(sc);
		if (importSize == 0)
			curTable.put(new DecompilerPair(1),ann);
		else
			curTable.put(new DecompilerPair(2+importSize),ann);

		// Merge the table
		lineToAnnotation.putAll(curTable);
		dump.append(src.toString()+ln);

		// Make the directory if it doesn't exist
		(new File(outputPath + filePath)).mkdirs();

		// Prepare the file to dump the output
		String completeName = outputPath + filePath + curFileName;
		try
		{
			PrintStream javaPrint = new PrintStream(new FileOutputStream(new File(completeName)));
			javaPrint.print(src.toString());
			javaPrint.close();
		} catch (IOException ex)
		{
			throw new RuntimeException("Cannot create file '" + completeName + "'\n");
		}
	}
	//System.out.println("\nDecompiler Done");
	//System.out.println("~~~~~~~~~~~~~~~");
	log.debug("\nDecompiler Done");
	log.debug("~~~~~~~~~~~~~~~");

	// Dump Annotation Table...
	Hashtable tbl = getLineToAnnotationTable();

	dump.append(ln+"Annotation Table"+ln);
	dump.append(   "----------------"+ln);
	for (Enumeration e = tbl.keys(); e.hasMoreElements();)
	{
		DecompilerPair p = (DecompilerPair) e.nextElement();
		Annotation an = (Annotation) tbl.get(p);
		if (an != null)
			dump.append(p.toString()+" = "+an.toString()+ln);
		else
			dump.append(p.toString()+" = null"+ln);
	}
	return dump.toString()+ln;
}
/**
   *
   */
private void dump(SootClass sc) {
	StoredBody bd = new StoredBody(ca.mcgill.sable.soot.jimple.Jimple.v());
	String className = sc.getName();
	try {
		java.io.File jimpFile = new java.io.File(outputPath + File.separator + className + ".decompiled.jimple");
		java.io.FileOutputStream jimpOut = new java.io.FileOutputStream(jimpFile);
		sc.printTo(bd, new java.io.PrintWriter(jimpOut, true));
		jimpOut.close();
	} catch (java.io.IOException ex) {
	    //System.out.println("***Can't dump! "+ex.getMessage()+"\n");
	    log.error("***Can't dump! ", ex);
	}
}
public Hashtable getLineToAnnotationTable()
{
	Hashtable tbl = new Hashtable();
	tbl.putAll(lineToAnnotation);
	return tbl;
}
private String printImports()
{
	StringBuffer s = new StringBuffer();
	
	for (Enumeration e = imports.elements(); e.hasMoreElements(); )
	{
		String str = (String) e.nextElement();
		if (str.startsWith("package ")) s.append(str + ";\n");
			else s.append("import "+ str +";\n");
	}
	return s.toString();
}
private void renumber(Hashtable tbl, int offset)
{
	Hashtable newTbl = new Hashtable();
	
	for (java.util.Iterator i = tbl.entrySet().iterator(); i.hasNext();) {
		java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
		DecompilerPair p = (DecompilerPair) entry.getKey();
		Annotation an = (Annotation) entry.getValue();
		p.addBy(offset);
		newTbl.put(p, an);
	}
	tbl.clear();
	tbl.putAll(newTbl);
}
}
