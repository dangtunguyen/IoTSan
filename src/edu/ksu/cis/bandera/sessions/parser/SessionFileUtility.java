package edu.ksu.cis.bandera.sessions.parser;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

public class SessionFileUtility {

    public static String getRelativePath(File base, File file) throws FileNotFoundException {

	if(base == null) {
	    throw new FileNotFoundException("Cannot get a relative path for a null base directory.");
	}

	if(!base.isDirectory()) {
	    throw new FileNotFoundException("Cannot get a relative path for a base directory (" + base.getPath() +
					    ") that is not a directory.");
	}

	if(file == null) {
	    return("");
	}

	//System.out.println("base = " + base);
	//System.out.println("base.getPath() = " + base.getPath());
	//System.out.println("base.getAbsolutePath() = " + base.getAbsolutePath());
	//System.out.println("file = " + file);
	//System.out.println("file.getPath() = " + file.getPath());
	//System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());

	String relativePath = null;

	// are these two files in the same directory -> fileName
	if((relativePath == null) && (base.equals(file.getParentFile()))) {
	    relativePath = file.getPath();
	}

	// is the file in a parent directory of the base -> ..\..\fileName

	// is the file in a sub directory of the base -> some\sub\dir\fileName

	// is the file in a parent's sub directory of the base -> ..\..\some\sub\dir\fileName

	// if all else fails, just use the absolute path
	if(relativePath == null) {
	    try {
		relativePath = file.getCanonicalPath();
	    }
	    catch(IOException ioe) {
		relativePath = file.getAbsolutePath();
	    }
	}

	return(relativePath);
    }

}
