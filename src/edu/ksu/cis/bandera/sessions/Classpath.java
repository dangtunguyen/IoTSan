package edu.ksu.cis.bandera.sessions;

import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Classpath {

    private List files;

    public static Classpath parseClasspath(String classpathString) {
	Classpath classpath = new Classpath();

	if((classpathString != null) && (classpathString.length() > 0)) {

	    // assume unix style classpath separator
	    StringTokenizer st = new StringTokenizer(classpathString, ":");

	    if(st.countTokens() <= 0) {
		// since unix didn't work out, try windows classpath separator
		st = new StringTokenizer(classpathString, ";");
	    }
	    if(st.countTokens() <= 0) {
		// since windows and unix didn't work out, try bandera classpath separator
		st = new StringTokenizer(classpathString, "+");
	    }

	    while(st.hasMoreTokens()) {
		String currentToken = st.nextToken();
		classpath.addFile(currentToken);
	    }
	}

	return(classpath);
    }

    private String createString(List l, String s) {
	StringBuffer classpath = new StringBuffer();
	for(int i = 0; i < files.size(); i++) {
	    File currentFile = (File)files.get(i);
	    classpath.append(currentFile.getAbsolutePath());
	    if(i + 1 < files.size()) {
		classpath.append(File.pathSeparator);
	    }
	}
	return(classpath.toString());
    }

    public String toString() {
	if((files == null) || (files.size() <= 0)) {
	    return("");
	}

	return(createString(files, File.pathSeparator));
    }

    public String toWindowsString() {
	if((files == null) || (files.size() <= 0)) {
	    return("");
	}
	return(createString(files, ";"));
    }

    public String toUnixString() {
	if((files == null) || (files.size() <= 0)) {
	    return("");
	}
	return(createString(files, ":"));
    }

    public void addFile(File file) {
	if(files == null) {
	    files = new ArrayList();
	}
	files.add(file);
    }

    public void addFile(String filename) {
	if(filename == null) {
	    return;
	}
	File file = new File(filename);
	addFile(file);
    }

    public void removeFile(File file) {
	if(files == null) {
	    return;
	}
	try {
	    files.remove(file);
	}
	catch(Exception e) {
	    // ignore the exceptions. -tcw
	}
    }

    public void removeFile(String filename) {
	if(filename == null) {
	    return;
	}
	File file = new File(filename);
	removeFile(file);
    }

    public void removeFile(int index) {
	if(files == null) {
	    return;
	}
	try {
	    files.remove(index);
	}
	catch(Exception e) {
	    // ignore the exceptions. -tcw
	}
    }

    public void clearFiles() {
	if(files == null) {
	    return;
	}
	files.clear();
    }

    public List getFiles() {
	return(files);
    }

    public File getFile(int index) {
	if(files == null) {
	    return(null);
	}
	if((index >= 0) && (index < files.size())) {
	    return((File)files.get(index));
	}
	else {
	    return(null);
	}
    }

}
