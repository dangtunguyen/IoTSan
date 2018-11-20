package edu.ksu.cis.bandera.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000  Roby Joehanes (robbyjo@cis.ksu.edu)           *
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

// Adapted from magelang.com tutorial.

import java.io.*;
import java.net.*;
import java.util.*;

public class FileClassLoader extends ClassLoader {

	private String root;
	public static FileClassLoader v = new FileClassLoader();
private FileClassLoader() {
	 // root == null means that we're looking it through the java.class.path system
	 // property
	 root = null;
}
public FileClassLoader (String rootDir) {
	 if (rootDir == null) throw new IllegalArgumentException ("Null root directory");
	 root = rootDir;
}
public static Class load(String name) throws ClassNotFoundException
{
	 return v.loadClass(name);
}
protected Class loadClass (String name, boolean resolve) throws ClassNotFoundException {

	 // Since all support classes of loaded class use same class loader
	 // must check subclass cache of classes for things like Object

	 // Class loaded yet?
	 Class c = findLoadedClass (name);
	 if (c == null) {
		  try {
			   c = findSystemClass (name);
		  } catch (Exception e) {
			   // Ignore these
		  }
	 }

	 if (c == null) {
		  // Convert class name argument to filename
		  // Convert package names into subdirectories
		  String filename = name.replace ('.', File.separatorChar) + ".class";

		  try {
			   // Load class data from file and save in byte array
			   byte data[] = loadClassData(filename);

			   // Convert byte array to Class
			   c = defineClass (name, data, 0, data.length);

			   // If failed, throw exception
			   if (c == null) throw new ClassNotFoundException (name);
		  } catch (IOException e) {
			   throw new ClassNotFoundException ("Error reading file: " + filename);
		  }
	 }

	 // Resolve class definition if approrpriate
	 if (resolve) resolveClass (c);

	 // Return class just created
	 return c ;
}
private byte[] loadClassData (String filename) throws IOException {
	 // Create a file object relative to directory provided
	 File f = null;

	 if (root == null)
	 {
		  // Fetch from the classpath
		  String classPath = System.getProperty("java.class.path");
		  StringTokenizer tok = new StringTokenizer(classPath, File.pathSeparator);
		  boolean found = false;

		  while (tok.hasMoreTokens())
		  {
			   f = new File(tok.nextToken(), filename);
			   if (f.exists()) { found = true; break; }
		  }
		  if (!found) throw new IOException(filename+" cannot be found in class path: "+classPath);
	 } else f = new File (root, filename);

	 // Get size of class file
	 int size = (int)f.length();

	 // Reserve space to read
	 byte buff[] = new byte[size];

	 // Get stream to read from
	 FileInputStream fis = new FileInputStream(f);
	 DataInputStream dis = new DataInputStream (fis);

	 // Read in data
	 dis.readFully (buff);

	 // close stream
	 dis.close();

	 // return data
	 return buff;
}
	public void setRootDirectory(String r)
	{
		if (r != null) root = r;
	}
}
