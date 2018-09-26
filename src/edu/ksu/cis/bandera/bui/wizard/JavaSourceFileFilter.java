package edu.ksu.cis.bandera.bui.wizard;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import org.apache.log4j.*;

/**
 * Insert the type's description here.
 * Creation date: (4/18/2002 9:29:29 PM)
 * @author: 
 */
public final class JavaSourceFileFilter extends javax.swing.filechooser.FileFilter {
	private final static Category log = Category.getInstance(JavaSourceFileFilter.class);
/**
 * JavaSourceFileFilter constructor comment.
 */
public JavaSourceFileFilter() {
	super();
}
	/**
	 * Accept only files that are Java source files (.java extension).
	 *
	 * @param File file The file to test for acceptance.
	 * @pre file is not null
	 */
public boolean accept(File file) {

	if(file == null) {
		return(false);
	}

	if(file.isDirectory()) {
		return(true);
	}

	if(file.getPath().endsWith(".java")) {
		return(true);
	}

	return(false);
}
	/**
	 * Accept only Java source files (.java extension) and directories.
	 * @see FileView#getName
	 */
public String getDescription() {
	return("*.java");
}
}
