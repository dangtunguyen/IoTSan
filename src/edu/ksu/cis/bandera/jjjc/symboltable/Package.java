package edu.ksu.cis.bandera.jjjc.symboltable;

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
import java.io.*;
import java.util.*;
import java.util.zip.*;
import edu.ksu.cis.bandera.jjjc.util.Util;
import edu.ksu.cis.bandera.jjjc.exception.*;

public class Package implements Named {
	private static Vector visitedPaths = new Vector();
	private static Hashtable packages;
	private static Hashtable foundTypes;
	private static HashSet notFoundTypes;

	private Name name;
	private Vector paths = new Vector();
	private Hashtable types = new Hashtable();
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
protected Package(Name name) throws InvalidNameException {
	boolean validName = true;

	// should check for validity of the name, problem with Windows filename
	Package p = (Package) packages.get(name.getSuperName());

	this.name = name;
}
/**
 * 
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void addType(ClassOrInterfaceType type) {
	types.put(new Name(type.getName().getLastIdentifier()), type);
	foundTypes.put(type.getName(), type);
}
/**
 * 
 * @return boolean
 * @param otherName edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public boolean equals(Object obj) {
	if (obj == null) return false;
	else if (!(obj instanceof Package)) return false;
	else return this.getName().equals(((Package)obj).getName());
}
/**
 * 
 * @return boolean
 * @param path java.lang.String
 * @param filename java.lang.String
 */
private static boolean exists(String path, String filename) {
    //System.out.println("Package.exists(" + path + ", " + filename + ") ...");
	File dir = new File(path);
	String[] files = dir.list();

	//if(files != null) {
	    for (int i = 0; i < files.length; i++) {
		if (files[i].equals(filename)) {
		    //System.out.println("found file " + filename + " in directory " + path);
		    return true;
		}
	    }
	    //}
	return false;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassType
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public static ClassOrInterfaceType getClassOrInterfaceType(Name name) throws ClassOrInterfaceTypeNotFoundException,
		InvalidNameException {
	if (foundTypes.get(name) != null) return (ClassOrInterfaceType) foundTypes.get(name);
	if (notFoundTypes.contains(name)) throw new ClassOrInterfaceTypeNotFoundException("Could not load type '" + name.toString() + "'");
	Name packageName = name.isSimpleName() ? new Name("") : name.getSuperName();
	Name className = new Name(name.getLastIdentifier());

	if (Package.hasPackage(name))
		throw new InvalidNameException("Could not have class named " + className.toString() + " in package "
				+ packageName.toString() + ", because there is a package named " + name);
	
	Package p = null;
	String path = null;
	boolean isFound = false;
	if (packages.get(packageName) == null) {
		try {
			Class.forName(name.toString());
			isFound = true;
			p = getPackage(packageName);
		} catch (Exception e) {}
	} else {
		p = (Package) packages.get(packageName);

		if (p.types.get(className) != null)
			return (ClassOrInterfaceType) p.types.get(className);

		
		for (int i = 0; i < p.paths.size(); i++) {
			if (exists((String) p.paths.elementAt(i), className.toString() + ".java")) {
				isFound = true;
				path = (String) p.paths.elementAt(i) + File.separator + className.toString() + ".java";
				break;
			}
		}

		if (!isFound) {
			try {
				Class c = Class.forName(name.toString());
				String pName = packageName.toString();
				String cName = className.toString();
				String qName = "".equals(pName) ? cName : pName + "." + cName;
				if (c.getName().equals(qName)) {
					int x = c.getDeclaredMethods().length;
					
					isFound = true;
				}
			} catch (Throwable e) {}
		}
	}
	
	if (isFound) {
		ClassOrInterfaceType c = new ClassOrInterfaceType(p, className, path);
		p.addType(c);
		return c;
	} else {
		notFoundTypes.add(name);
		throw new ClassOrInterfaceTypeNotFoundException("Could not load type '" + name.toString() + "'");
	}	
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Name getName() {
	return name;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Package
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public static Package getPackage(Name name) throws InvalidNameException {
	if (packages.get(name) == null)
		packages.put(name, new Package(name));
			
	return (Package) packages.get(name);
}
/**
 *
 * @return java.util.Hashtable
 */
public static Hashtable getPackages() {
	return packages;
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getPaths() {
	return paths.elements();
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getTypes() {
	return types.elements();
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public static boolean hasPackage(Name name) {
	return (packages.get(name) != null);
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public boolean hasType(Name name) {
	try {
		getClassOrInterfaceType(new Name(this.name, name));
		return true;
	} catch (CompilerException e) {
		return false;
	}
}
/**
 *
 * @param name java.lang.String
 * @param path java.lang.String
 */
protected static void registerPackages(String name, String path) throws InvalidNameException {
	if (visitedPaths.contains(path)) {
		return;
	}
	visitedPaths.addElement(path);
	Name n = new Name(name);
	Package p;
	if (packages.get(n) == null) {
		p = new Package(n);
		packages.put(n, p);
	} else p = (Package) packages.get(n);
	
	p.paths.addElement(path);
	
	String[] files = new File(path).list();

	if (files != null) {
		for (int i = 0; i < files.length; i++) {
			File file = new File(path, files[i]);
			if (file.isDirectory())
				try {
					registerPackages(name + "." + files[i], file.getCanonicalPath());
				} catch (Exception e) {}
		}
	}
}
/**
 * 
 * @param classPath java.lang.String
 */
public static void setClassPath(String classPath) throws CompilerException {
	packages = new Hashtable();
	foundTypes = new Hashtable();
	notFoundTypes = new HashSet();
	visitedPaths = new Vector();
	String[] paths = Util.splitString(classPath, File.pathSeparator);
	for (int i = 0; i < paths.length; i++) {
		String path = paths[i];
		/*if (path.endsWith(".jar") || path.endsWith(".zip")) {
			try {
				ZipFile zipFile = new ZipFile(path);
				for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
					ZipEntry zipEntry = (ZipEntry) e.nextElement();
					if (zipEntry.isDirectory()) {
						String pathName = zipEntry.getName();
						Name n = new Name(pathName.replace(pathName.charAt(pathName.length() - 1), '.'));
						Name defaultPackageName = new Name("");
						Package p;
						if (packages.get(defaultPackageName) == null) {
							p = new Package(defaultPackageName);
							packages.put(defaultPackageName, p);
						} else p = (Package) packages.get(defaultPackageName);
						p.paths.addElement(path + File.separator + n.toString().replace('.', File.separatorChar));
					}
				} 
			} catch (Exception e) {
				System.out.println("Failed to register packages from class path");
				e.printStackTrace();
				System.exit(1);
			}
		} else {*/
			try {
				registerPackages("", new File(path).getCanonicalPath());
			} catch (Exception e) {
				throw new CompilerException("Failed to register packages from class path");
			}
		//}
	}
	try {
		Package p = (Package) packages.get(new Name(""));
		if (p == null) {
			p = new Package(new Name(""));
			packages.put(p.getName(), p);
		}
	} catch (InvalidNameException e) {
		e.printStackTrace();
	}
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return name.toString();
}
}
