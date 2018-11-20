package edu.ksu.cis.bandera.abstraction.util;

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
import java.lang.reflect.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.Type;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.gui.*;
public class AbstractionClassLoader extends ClassLoader {
	private static AbstractionClassLoader acl = new AbstractionClassLoader(AbstractionLibraryManager.abstractionPath);
	private static String abstractionPackage = AbstractionLibraryManager.abstractionPackage;
	private Hashtable abstractionClasses = new Hashtable();
	private String abstractionPath;
/**
 * 
 * @param other java.lang.ClassLoader
 */
private AbstractionClassLoader(AbstractionClassLoader other) {
	if (!new File(other.abstractionPath).exists()) {
		System.out.println("Invalid abstraction path");
		other.abstractionPath = System.getProperty("user.dir");
	}
	abstractionPath = other.abstractionPath;
	for (Enumeration e = other.abstractionClasses.elements(); e.hasMoreElements();) {
		Object o = e.nextElement();
		if (o instanceof Class) {
			Class abstractionClass = (Class) o;
			String qName = abstractionClass.getName();
			String filename = getFilename(qName);
			if (filename == null) continue;
			long lastModified = ((Long) other.abstractionClasses.get(abstractionClass)).longValue();
			if (lastModified == new File(filename).lastModified()) {
				abstractionClasses.put(qName, abstractionClass);
				abstractionClasses.put(abstractionClass, new Long(lastModified));
			}
		}
	}
}
/**
 * 
 * @param abstractionPath java.lang.String
 */
private AbstractionClassLoader(String abstractionPath) {
	if (!new File(abstractionPath).exists()) {
		System.err.println("Invalid abstraction path");
		abstractionPath = System.getProperty("user.dir");
	}
	this.abstractionPath = abstractionPath;
}
/**
 * 
 * @return java.lang.Class
 * @param type ca.mcgill.sable.soot.Type
 */
private static Class getClass(Type type) {
	Class result = null;
	if (type instanceof ArrayType) {
		ArrayType arrayType = (ArrayType) type;
		type = arrayType.baseType;
		String className;
		if (type instanceof BooleanType) {
			className = "Z";
		} else
			if (type instanceof ByteType) {
				className = "B";
			} else
				if (type instanceof CharType) {
					className = "C";
				} else
					if (type instanceof ShortType) {
						className = "S";
					} else
						if (type instanceof IntType) {
							className = "I";
						} else
							if (type instanceof LongType) {
								className = "J";
							} else
								if (type instanceof FloatType) {
									className = "F";
								} else
									if (type instanceof DoubleType) {
										className = "D";
									} else {
										className = "L" + ((RefType) type).className + ";";
									}
		for (int i = 0; i < arrayType.numDimensions; i++) {
			className = "[" + className;
		}
		try {
			result = Class.forName(className);
		} catch (Exception e) {
		}
	} else {
		if (type instanceof BooleanType) {
			result = boolean.class;
		} else
			if (type instanceof ByteType) {
				result = byte.class;
			} else
				if (type instanceof CharType) {
					result = char.class;
				} else
					if (type instanceof ShortType) {
						result = short.class;
					} else
						if (type instanceof IntType) {
							result = int.class;
						} else
							if (type instanceof LongType) {
								result = long.class;
							} else
								if (type instanceof FloatType) {
									result = float.class;
								} else
									if (type instanceof DoubleType) {
										result = double.class;
									} else {
										try {
											getClass(((RefType) type).className);
										} catch (Exception e) {
										}
									}
	}
	return result;
}
/**
 * 
 * @return java.lang.Class
 * @param name java.lang.String
 */
public static Class getClass(String name) {
	if ("".equals(abstractionPackage)) {
		if (!name.startsWith("integral.") && !name.startsWith("real.") ) {
			try {
				return Class.forName(name);
			} catch (Exception e) {
				return null;
			}
		}
	} else {
		if (!name.startsWith(abstractionPackage)) {
			try {
				return Class.forName(name);
			} catch (Exception e) {
				return null;
			}
		}
	}
	String filename = acl.getFilename(name);
	File file = new File(filename);
	if (!file.exists())
		return (Class) acl.abstractionClasses.get(name);
	if (acl.abstractionClasses.get(name) != null) {
		Class c = (Class) acl.abstractionClasses.get(name);
		long lastModified = ((Long) acl.abstractionClasses.get(c)).longValue();
		if (lastModified != file.lastModified()) {
			acl = new AbstractionClassLoader(acl);
		} else {
			acl.resolveClass(c);
			return c;
		}
	}
	InputStream is = null;
	int length = (int) file.length();
	//System.out.println("File " + file + "'s length is " + length);
	byte[] buffer = new byte[length];
	try {
		is = new FileInputStream(file);
		if (is.read(buffer) != length)
			System.out.println("Error in reading abstraction file " + file);
	} catch (Exception e) {
		return null;
	} finally {
		try {
			is.close();
		} catch (Exception e) {
		}
	}

	Class abstractionClass = acl.defineClass(name, buffer, 0, length);
	acl.abstractionClasses.put(name, abstractionClass);
	acl.abstractionClasses.put(abstractionClass, new Long(file.lastModified()));
	acl.resolveClass(abstractionClass);
	return abstractionClass;
}
/**
 * 
 * @return java.lang.String
 * @param className java.lang.String
 */
private String getFilename(String className) {
	int idx = className.lastIndexOf(".");
	if (idx < 0) return null;
	idx = className.lastIndexOf(".", idx - 1);
	if (idx > -1) {
		className = className.substring(idx + 1);
	}
	className = className.replace('.', File.separatorChar);
	return abstractionPath + File.separator + className + ".class";
}
public static String getTokenName(Abstraction lt, int v)
{
	String result = "";
	try {
		String clsName = lt.getClass().getName();
		result = (String) AbstractionClassLoader.invokeMethod(clsName, "getToken", new Class[] {int.class}, null, new Object[] {new Integer(v)});
	} catch (Exception e)
	{
		System.out.println("Warning: Possibly error in abstraction!!");
	}

	return result;
}
/**
 * 
 * @return java.lang.Object
 * @param className java.lang.String
 * @param methodName java.lang.String
 * @param params java.lang.Class[]
 * @param receiver java.lang.Object
 * @param args java.lang.Object[]
 */
public static Object invokeMethod(String className, String methodName, Class[] params, Object receiver, Object[] args) {
	Object result = null;
	if (args.length == params.length) {
		try {
			Class c = AbstractionClassLoader.getClass(className);
			Method m = c.getMethod(methodName, params);
			result = m.invoke(receiver, args);
		} catch (Exception e) {
		}
	}
	return result;
}
/**
 * 
 * @return java.lang.Object
 * @param className java.lang.String
 * @param methodName java.lang.String
 * @param args java.util.Vector
 */
public static Object invokeMethod(String className, String methodName, Object[] params, Object receiver, Vector args) {
	Object result = null;
	if (args.size() == params.length) {
		try {
			Class c = getClass(className);
			Class[] classParams = new Class[params.length];
			for (int i = 0; i < classParams.length; i++) {
				classParams[i] = getClass((Type) params[i]);
			}
			Method m = c.getMethod(methodName, classParams);
			result = m.invoke(receiver, args.toArray());
		} catch (Exception e) {
		}
	}
	return result;
}
/**
 * 
 * @param name java.lang.String
 */
public static void removeClass(String name) {
	if (acl.abstractionClasses.get(name) == null) return;
	acl.abstractionClasses.remove(name);
	acl = new AbstractionClassLoader(acl);
}
}
