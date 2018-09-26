package edu.ksu.cis.bandera.bui;

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
import javax.swing.*;
import javax.swing.filechooser.*;

public class FileChooser extends JFileChooser {
	public static JFileChooser chooser;
	public static FileFilter ALL;
	public static FileFilter JAVA;
	public static FileFilter CLASSPATH;
	public static FileFilter PATTERN;
	public static FileFilter DIRECTORY;
	public static FileFilter SESSION;
	public static FileFilter SPECIFICATION;
	public static FileFilter ABSTRACTION;
	public static FileFilter BASL;
	public static FileFilter CLASS;
/**
 * FileChooser constructor comment.
 */
private FileChooser() {
}
/**
 * 
 */
public static void initialize() {
	chooser = new JFileChooser();
	class AllFilter extends FileFilter {
		public String ext = "*.*";
		public String description = "All files";
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (ext == null)
						return false;
					else
						if (("*.*".equals(ext)) || f.getAbsolutePath().endsWith(ext))
							return true;
			}
			return false;
		}
		public String getDescription() {
			return description;
		}
	};
	class JavaFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".java"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "Java source code";
		}
	};
	class ClasspathFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".jar") || f.getAbsolutePath().endsWith(".zip"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "Jar files, Zip files and directories";
		}
	};
	class PatternFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".pattern"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "Pattern file";
		}
	};
	class SpecificationFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".specification"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "Specification file";
		}
	};
	class DirectoryFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					return false;
			}
			return false;
		}
		public String getDescription() {
			return "Directory";
		}
	};
	class SessionFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".session"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "Session file";
		}
	};
	class AbstractionFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".abstraction"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "Abstraction file";
		}
	};
	class BASLFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".basl"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "BASL file";
		}
	};
	class ClassFilter extends FileFilter {
		public boolean accept(java.io.File f) {
			if (f != null) {
				if (f.isDirectory())
					return true;
				else
					if (f.getAbsolutePath().endsWith(".class"))
						return true;
			}
			return false;
		}
		public String getDescription() {
			return "Java class file";
		}
	};
	ALL = new AllFilter();
	JAVA = new JavaFilter();
	CLASSPATH = new ClasspathFilter();
	PATTERN = new PatternFilter();
	SPECIFICATION = new SpecificationFilter();
	DIRECTORY = new DirectoryFilter();
	SESSION = new SessionFilter();
	ABSTRACTION = new AbstractionFilter();
	BASL = new BASLFilter();
	CLASS = new ClassFilter();
}
}
