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
/**
 * DecompilerInfo basically holds all the information the decompiler need.
 * Used in conjunction to DecompilerField.
 * The methods and fields are self-explanatory.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
public class DecompilerInfo {
	private static SootClass sc;
	private static SootMethod sm;
	private static int noOfParam;
	private static Hashtable fieldTable;
	private static boolean synchro;
	private static String[] paramType;
	private static String[] paramName;
	private static Vector boolTemp = new Vector();
	private static Vector boolVar = new Vector();
	private static LinkedList qvDefined = new LinkedList();
public static void decipherParam(Stmt[] stmt)
{
	int max = stmt.length;
	String errorMsg = "Error in parameter at "+getClassName()+ ":"+ getMethodName();

	for (int i=0; i<max; i++)
	{
		if (stmt[i] instanceof IdentityStmt)
		{
			String name = ((IdentityStmt) stmt[i]).getLeftOp().toString();
			String paramID = ((IdentityStmt) stmt[i]).getRightOp().toString();
			if (paramID.startsWith("@parameter"))
			{
				try {
					int id = Integer.parseInt(paramID.substring(10));
					paramName[id] = name;
				} catch (NumberFormatException ex)
				{
					System.out.println(errorMsg+", non-integer parameterID, seemed to be JJJC error!");
				}
			}
		} else
		{
			System.out.println(errorMsg);
		}
	}
}
public static String getClassDeclaration()
{
	// Get class modifier
     String modifier = Modifier.toString(sc.getModifiers());
     StringBuffer buf = new StringBuffer();

     if (modifier.length() > 0) buf.append(modifier + " ");
     if (!isInterface()) buf.append("class ");
	buf.append(getClassName() + " ");

	// Is there any super class ?
	try
	{
		if (sc.hasSuperClass())
		{
			String superName = sc.getSuperClass().getName().trim();
			if (!superName.equals("java.lang.Object"))
				buf.append("extends " + DecompilerUtil.stripImports(superName) + " ");
		}
	} catch (Exception exc)
	{
		System.out.println("No super class error! This shouldn't happen!\n");
	}
     if (sc.getInterfaceCount() > 0)
     {
         buf.append("implements ");
         for(ca.mcgill.sable.util.Iterator i = sc.getInterfaces().iterator(); i.hasNext(); )
         {
             SootClass inter = (SootClass) i.next();
             buf.append(inter.getName());
             if (i.hasNext()) buf.append(", ");
         }
         buf.append(" ");
     }
	return buf.toString();
}
public static String getClassName() { return sc.getName().trim(); }
public static Vector getFieldDeclaration()
{
	Vector buf = new Vector();
	for (Enumeration key = fieldTable.keys(); key.hasMoreElements();)
	{
		DecompilerField sf = (DecompilerField) fieldTable.get((String) key.nextElement());
		String modi = sf.getModifier();
		StringBuffer st = new StringBuffer();
		if (modi.length()>0) st.append(sf.getModifier()+" ");
		String temp = sf.getType();
//		if (temp.equals("boolean")) temp = "int";
		st.append(temp+" "+sf.getName());
		
		String s = sf.getDefaultValue();
		if (s.length()>0) st.append(" = "+s);
		buf.add(st.toString()+";");
	}
	return buf;
}
public static String getMethodDeclaration()
{
	String name = getMethodName();
	if (name.equals("<initHelper>") || name.equals("<clinit>")) return "";

	StringBuffer s = new StringBuffer();
	String modifier = Modifier.toString(sm.getModifiers()).trim();
	
	if (modifier.length()>0) s.append(modifier+" ");
	if (synchro && !Modifier.isSynchronized(sm.getModifiers())) s.append("synchronized ");

	if (name.equals("<init>"))
	{
		s.append(getClassName()+"("); 
	} else
	{
		s.append(getMethodRetType()+" "+name+" (");
	}

	for (int i = 0; i<noOfParam; i++)
	{
		String st = paramType[i];
//		if (st.equals("boolean")) st = "int";
	String pname = paramName[i];
	if (pname == null) pname = "param"+i;
		s.append(st+" "+pname);
		if (i!=noOfParam-1) s.append(", ");
	}
	s.append(")");

     ca.mcgill.sable.util.List l = sm.getExceptions();
     if (l != null && l.size() > 0)
     {
         s.append(" throws ");
         for (ca.mcgill.sable.util.Iterator i = l.iterator(); i.hasNext(); )
         {
             SootClass exc = (SootClass) i.next();
             s.append(exc.getName());
             if (i.hasNext()) s.append(", ");
         }
     }
	return s.toString();
}
public static String getMethodName() { return sm.getName().trim(); }
public static String getMethodRetType()
{
	String s = sm.getReturnType().toString().trim();
//	if (s.equals("boolean")) s = "int";
	return s;
}
public static int getNoOfParam() { return noOfParam; }
private static String getParamType(String name)
{
	if (isParam(name))
	{
		for (int i=0; i<noOfParam; i++)
		{
			if (paramName[i].equals(name)) return paramType[i];
		}
	}
	return "";
}
public static boolean hasField()
{
	return !fieldTable.isEmpty();
}
public static boolean isAbstract() { return Modifier.isAbstract(sm.getModifiers()); }
// The only thing left unchecked is local variables
public static boolean isBool(Value v)
{
	if (isJimpleTemp(v)) return boolTemp.contains(v);
	if (v instanceof Local)
	{
		String st = v.toString().trim();

		// Is it a parameter or not
		if (getParamType(st).equals("boolean")) return true;
		if (boolVar.contains(st)) return true;
	} else
	{
		if (v instanceof FieldRef)
		{
			return ((FieldRef) v).getField().getType().toString().trim().equals("boolean");
		} else
		{
			if (v instanceof InvokeExpr)
			{
				return ((InvokeExpr) v).getMethod().getReturnType().toString().trim().equals("boolean");
			}
		}
	}
	return false;
}
public static boolean isField(String x)
{
	return fieldTable.containsKey(stripThis(x));
}
public static boolean isInitializer()
{
	String name = getMethodName();
	return name.equals("<init>") || name.equals("<clinit>") || name.equals("<initHelper>");
}
public static boolean isInterface() { return Modifier.isInterface(sc.getModifiers()); }
public static boolean isJimpleInitializer()
{
	String name = getMethodName();
	return name.equals("<clinit>") || name.equals("<initHelper>");
}
public static boolean isJimpleTemp(Value v)
{
	String s = v.toString().trim();
	return (v instanceof Local) && (DecompilerUtil.isJimpleID(s));
}
public static boolean isParam(String name)
{
	if (name==null) return false;
	if (paramName == null) return false;

	for (int i=0; i<noOfParam; i++)
	{
		if ((paramName[i]!=null) && (paramName[i].equals(name)))
		{
			return true;
		}
	}
	return false;
}
public static boolean isStaticMethod() { return Modifier.isStatic(sm.getModifiers()); }
public static void putVarInfo(Value lhs, Value rhs)
{
	if ((isJimpleTemp(lhs)) && (rhs.getType().toString().trim().equals("boolean")))
	{
		boolTemp.add(lhs);
	}
}
public static void putVarInfo(String var)
{
	boolVar.add(var);
}
public static int qvDef(Value v)
{
	String name = v.toString();
	// Is a local, a quantified variable tag, and contains no dot (i.e. no complex name)
	if (!(v instanceof Local && name.indexOf("qtvars") != -1 && name.indexOf(".") == -1)) return -1;

	if (qvDefined.contains(name)) return 1;
	qvDefined.add(name);
	return 0;
}
public static void reset()
{
	sc = null; sm = null;
	fieldTable = new Hashtable();
	noOfParam = -1;
}
public static void setClass(SootClass sootc)
{
	sc = sootc;
	fieldTable = new Hashtable();
	ca.mcgill.sable.util.Iterator i;

	for (i=sc.getFields().iterator(); i.hasNext();)
	{
		SootField sf = (SootField) i.next();
		String name = sf.getName();
		String modifier = Modifier.toString(sf.getModifiers()).trim();
		String type = sf.getType().toString().trim();
		fieldTable.put(name, new DecompilerField(name,type,modifier));
	}
}
public static void setFieldDefaultValue(String fieldName, char i)
{
	DecompilerField sf = (DecompilerField) fieldTable.get(stripThis(fieldName));
	sf.setDefaultValue(String.valueOf(i));
}
public static void setFieldDefaultValue(String fieldName, double i)
{
	DecompilerField sf = (DecompilerField) fieldTable.get(stripThis(fieldName));
	sf.setDefaultValue(String.valueOf(i));
}
public static void setFieldDefaultValue(String fieldName, float i)
{
	DecompilerField sf = (DecompilerField) fieldTable.get(stripThis(fieldName));
	sf.setDefaultValue(String.valueOf(i));
}
public static void setFieldDefaultValue(String fieldName, int i)
{
	DecompilerField sf = (DecompilerField) fieldTable.get(stripThis(fieldName));
	sf.setDefaultValue(String.valueOf(i));
}
public static void setFieldDefaultValue(String fieldName, long i)
{
	DecompilerField sf = (DecompilerField) fieldTable.get(stripThis(fieldName));
	sf.setDefaultValue(String.valueOf(i));
}
public static void setFieldDefaultValue(String fieldName, String i)
{
	DecompilerField sf = (DecompilerField) fieldTable.get(stripThis(fieldName));
	sf.setDefaultValue(i);
}
public static void setFieldDefaultValue(String fieldName, boolean i)
{
	DecompilerField sf = (DecompilerField) fieldTable.get(stripThis(fieldName));
	sf.setDefaultValue(String.valueOf(i));
}
public static void setMethod(SootMethod sootm)
{
	sm = sootm;
	noOfParam = sm.getParameterCount();
	paramType = new String[noOfParam];
	paramName = new String[noOfParam];

	for (int i = 0; i<noOfParam; i++)
	{
		String st = sm.getParameterType(i).toString();
		paramType[i] = st;
	}
	synchro = false;
	boolTemp = new Vector();
	boolVar = new Vector();
}
public static void setSynchro(boolean s) { synchro = s; }
private static String stripThis(String x)
{
	if (x.startsWith("this.")) return x.substring(5);
	if (x.startsWith("this().")) return x.substring(7);
	return x;
}
}
