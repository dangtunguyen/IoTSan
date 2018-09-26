package edu.ksu.cis.bandera.abstraction.options;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
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
import java.lang.reflect.Method;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import edu.ksu.cis.bandera.abstraction.options.node.*;
import edu.ksu.cis.bandera.abstraction.options.lexer.*;
import edu.ksu.cis.bandera.abstraction.options.parser.*;
import edu.ksu.cis.bandera.abstraction.options.analysis.*;
public class OptionsSaverLoader {
	private static final Jimple jimple = Jimple.v();
	private Hashtable localMethodNamesTable;
	private SootClassManager scm;
	private SootClass sc;
	private SootMethod sm;
	private ca.mcgill.sable.util.List params;
	private JimpleBody body;
	private Hashtable result;
	private Vector warnings;
	private class Walker extends DepthFirstAdapter {
		public void caseAClassOption(AClassOption node) {
			sc = scm.getClass(Util.deleteChars(node.getName().toString(), " "));
			if (result.get(sc) != null) {
				String msg = "*** WARNING: Redefinition of options for class '" + sc + "'. Discarding the previous one. ****";
				warnings.add(msg);
				//System.out.println(msg);
			}
			result.put(sc, new HashSet());
			{
				Object temp[] = node.getFieldOption().toArray();
				for (int i = 0; i < temp.length; i++) {
					((PFieldOption) temp[i]).apply(this);
				}
			}
			{
				Object temp[] = node.getMethodOption().toArray();
				for (int i = 0; i < temp.length; i++) {
					((PMethodOption) temp[i]).apply(this);
				}
			}
		}
		public void caseAFieldOption(AFieldOption node) {
			String fieldName = node.getId().toString().trim();
			if (sc.declaresField(fieldName)) {
				String typeName = Util.deleteChars(node.getName().toString(), " ");
				try {
					SootField sf = sc.getField(fieldName);
					result.put(sf, AbstractionClassLoader.getClass(typeName).getMethod("v", new Class[0]).invoke(null, new Object[0]));
					((HashSet) result.get(sc)).add(sf);
				} catch (Exception e) {
					String msg = "*** WARNING: Couldn't find abstraction '" + typeName + "'. The '" + sc.getName() + "." + fieldName + "' field's option is ignored. ***";
					warnings.add(msg);
					//System.out.println(msg);
				}
			} else {
				String msg = "*** WARNING: Couldn't find field '" + sc.getName() + "." + fieldName + "'. The field's option is ignored. ***";
				warnings.add(msg);
				//System.out.println(msg);
			}
		}
		public void caseALocalOption(ALocalOption node) {
			String localName = node.getId().toString().trim();
			if (body.declaresLocal(localName)) {
				LocalMethod lm = new LocalMethod(sm, body.getLocal(localName));
				String typeName = Util.deleteChars(node.getName().toString(), " ");
				try {
					result.put(lm, AbstractionClassLoader.getClass(typeName).getMethod("v", new Class[0]).invoke(null, new Object[0]));
					((HashSet) result.get(sm)).add(lm.getLocal());
				} catch (Exception e) {
					String msg = "*** WARNING: Couldn't find abstraction '" + typeName + "'. The '" + lm + "' local's option is ignored. ***";
					warnings.add(msg);
					//System.out.println(msg);
				}
			} else {
				String msg = "*** WARNING: Couldn't find local '" + localName + "' in method '" + sm + "'. The local's option is ignored. ***";
				warnings.add(msg);
				//System.out.println(msg);
			}
		}
		public void caseAMethodOption(AMethodOption node) {
			params = new ca.mcgill.sable.util.LinkedList();
			if (node.getParams() != null) {
				node.getParams().apply(this);
			}
			String mName = node.getId().toString().trim();
			String cName = sc.getName();
			int idx = cName.lastIndexOf(".");
			if (idx >= 0) {
				cName = cName.substring(idx + 1);
			}
			if (mName.equals(cName)) mName = "<init>";
			try {
				sm = sc.getMethod(mName, params);
				((HashSet) result.get(sc)).add(sm);
				if (!sm.isBodyStored(jimple)) {
					new BuildAndStoreBody(jimple, new StoredBody(ClassFile.v()), 0).resolveFor(sm);
				}
				body = (JimpleBody) sm.getBody(jimple);
				result.put(sm, new HashSet());
				{
					Object temp[] = node.getLocalOption().toArray();
					for (int i = 0; i < temp.length; i++) {
						((PLocalOption) temp[i]).apply(this);
					}
				}
			} catch (Exception e) {
				String params = OptionsSaverLoader.this.params.toString();
				params = params.substring(1, params.length() - 1);
				String msg = "*** WARNING: Couldn't find method '" + sc.getName() + "." + mName + "(" + params + ")'. The method's options are ignored. ***";
				warnings.add(msg);
				//System.out.println(msg);
			}
		}
		public void caseAParamParams(AParamParams node) {
			Type t = Util.getType(Util.deleteChars(node.getName().toString(), " "));
			int dimensions = node.getDim().size();
			if (dimensions > 0) {
				t = ArrayType.v((BaseType) t, dimensions);
			}
			params.add(t);
		}
		public void caseAParamsParams(AParamsParams node) {
			node.getParams().apply(this);
			Type t = Util.getType(Util.deleteChars(node.getName().toString(), " "));
			int dimensions = node.getDim().size();
			if (dimensions > 0) {
				t = ArrayType.v((BaseType) t, dimensions);
			}
			params.add(t);
		}
	}
	private Walker walker;
/**
 * 
 */
private OptionsSaverLoader() {
	walker = new Walker();
}
/**
 * 
 * @return java.util.Hashtable
 *   The Hashtable is of type:
 *     SootClass * HashSet&lt;SootField U SootMethod&gt;, that maps class to its to-be-abstracted members
 *     U SootField * String, that maps a field to its abstraction
 *     U SootMethod * HashSet&lt;Local&gt;, that maps a method to its to-be-abtracted parameters/locals
 *     U LocalMethod * String, that maps a (method, local) pair to its abstraction
 *     U String * Vector&lt;String&gt;, that maps the string "WARNINGS" to a vector containing warning messages
 * @param scm ca.mcgill.sable.soot.SootClassManager
 * @param reader java.io.Reader
 */
public static Hashtable load(SootClassManager scm, Reader reader) throws Exception {
	OptionsSaverLoader osl = new OptionsSaverLoader();
	osl.scm = scm;

	osl.result = new Hashtable();
	osl.warnings = new Vector();

	new Parser(new Lexer(new PushbackReader(reader))).parse().apply(osl.walker);

	osl.result.put("WARNINGS", osl.warnings);

	return osl.result;
}
/**
 * 
 * @param PrintWriter java.io.PrintWriter
 * @param table java.util.Hashtable
 *   The Hashtable is of type:
 *     SootClass * HashSet&lt;SootField U SootMethod&gt;, that maps class to its to-be-abstracted members
 *     U SootField * String, that maps a field to its abstraction
 *     U SootMethod * HashSet&lt;Local&gt;, that maps a method to its to-be-abtracted parameters/locals
 *     U LocalMethod * String, that maps a (method, local) pair to its abstraction
 */
public static void save(PrintWriter writer, Hashtable table) {
	for (Enumeration e = table.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		if (key instanceof SootClass) {
			SootClass sc = (SootClass) key;
			writer.println("class " + sc.getName() + " {");
			for (Iterator i = ((HashSet) table.get(sc)).iterator(); i.hasNext();) {
				Object o = i.next();
				if (o instanceof SootField) {
					SootField sf = (SootField) o;
					writer.println("  " + sf.getName() + " " + table.get(sf).getClass().getName() + ";");
				}
			}
			for (Iterator i = ((HashSet) table.get(sc)).iterator(); i.hasNext();) {
				Object o = i.next();
				if (o instanceof SootMethod) {
					SootMethod sm = (SootMethod) o;
					String mName = sm.getName(); 
					if ("<clinit>".equals(mName)) continue;
					if ("<init>".equals(mName)) {
						mName = sm.getDeclaringClass().getName();
						int idx = mName.lastIndexOf(".");
						if (idx >= 0) {
							mName = mName.substring(idx + 1);
						}
					}
					String params = sm.getParameterTypes().toString();
					writer.println("  " + mName + "(" + params.substring(1, params.length() - 1) + ") { ");
					for (Iterator j = ((HashSet) table.get(sm)).iterator(); j.hasNext();) {
						Local lcl = (Local) j.next();
						writer.println("    " + lcl.getName() + " " + table.get(new LocalMethod(sm, lcl)).getClass().getName() + ";");
					}
					writer.println("  }");
				}
			}
			writer.println("}");
			writer.println();
		}
	}
}
}
