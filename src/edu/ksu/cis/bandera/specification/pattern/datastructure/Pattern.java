package edu.ksu.cis.bandera.specification.pattern.datastructure;

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
import edu.ksu.cis.bandera.specification.pattern.exception.*;
import edu.ksu.cis.bandera.specification.ast.*;
import java.util.*;

public class Pattern {
	private String filename;
	private String name;
	private String scope;
	private String format;
	private String compareFormat;
	private String url = "";
	private String description = "";
	private TreeSet parameters = new TreeSet();
	private Hashtable mappings = new Hashtable();
	private Hashtable misc = new Hashtable();
	private boolean hasChanged = false;
	private Vector exceptions = new Vector();
/**
 * 
 * @param name java.lang.String
 * @param pattern java.lang.String
 */
public void addMapping(String name, String pattern)  {
	mappings.put(name, pattern);
}
/**
 * 
 * @param key java.lang.String
 * @param value java.lang.String
 */
public void addMiscData(String key, String value) {
	misc.put(key, value);
}
/**
 * 
 * @param id java.lang.String
 */
public void addParameter(String id) throws PatternException {
	if (parameters.contains(id))
		throw new PatternException("Parameter '" + id
				+ "' has already been declared in pattern with name '" + name
				+ "' and scope '" + scope + "'");
	parameters.add(id);
}
/**
 *
 * @return java.lang.String
 * @param parameters java.util.Hashtable
 */
public String expandFormat(Hashtable parameters) {
	StringBuffer buffer = new StringBuffer();
	for (StringTokenizer tokenizer =
			new StringTokenizer(format, "{}", true); tokenizer.hasMoreTokens();) {
		String token = tokenizer.nextToken();
		if ("{".equals(token)) {
			String param = (String) tokenizer.nextToken();
			String arg = (String) parameters.get(param.trim());
			if (arg != null) {
				buffer.append("{" + arg + "}");
			} else {
				buffer.append("{" + param + "}");
			}
			tokenizer.nextToken();
		} else {
			buffer.append(token);
		}
	}
		
	return buffer.toString();
}
/**
 * 
 * @return java.lang.String
 * @param mapping java.lang.String
 * @param parameters java.util.Hashtable
 */
public String expandMapping(String mapping, Hashtable parameters) throws MappingException {
	if (mappings.get(mapping) == null)
		throw new MappingException("Mapping for " + mapping + " is not declared");

	StringBuffer buffer = new StringBuffer();
	for (StringTokenizer tokenizer =
			new StringTokenizer((String) mappings.get(mapping), "{}", true); tokenizer.hasMoreTokens();) {
		String token = tokenizer.nextToken();
		if ("{".equals(token)) {
			String param = (String) tokenizer.nextToken();
			String arg = (String) parameters.get(param.trim());
			if (arg != null) {
				buffer.append(arg);
			} else {
				buffer.append("{" + param + "}");
			}
			tokenizer.nextToken();
		} else {
			buffer.append(token);
		}
	}
		
	return buffer.toString();
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getCompareFormat() {
	return compareFormat;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getExceptions() {
	return exceptions;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getFilename() {
	return filename;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getFormat() {
	return format;
}
/**
 * 
 * @return java.lang.String
 * @param key java.lang.String
 */
public String getMapping(String key) {
	return (String) mappings.get(key);
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * 
 * @return java.util.Vector
 */
public java.util.Vector getParameters() {
	return new Vector(parameters);
}
/**
 * 
 * @return java.util.Vector
 */
public Vector getParametersOccurenceOrder() {
	Vector v = new Vector();
	for (StringTokenizer tokenizer =
			new StringTokenizer(format, "{}", true); tokenizer.hasMoreTokens();) {
		String token = tokenizer.nextToken();
		if ("{".equals(token)) {
			String param = (String) tokenizer.nextToken();
			v.add(param);
			tokenizer.nextToken();
		}
	}
	return v;
}
/**
 * 
 * @return java.lang.String
 * @param mapping java.lang.String
 */
public String getPattern(String mapping) throws MappingException {
	if (mappings.get(mapping) == null)
		throw new MappingException("Mapping for " + mapping + " is not declared");
	return (String) mappings.get(mapping);
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getScope() {
	return scope;
}
/**
 * 
 * @return java.lang.String
 */
public java.lang.String getURL() {
	return url;
}
/**
 * 
 * @return boolean
 * @param mapping java.lang.String
 */
public boolean hasMapping(String mapping) {
	return mappings.get(mapping) != null;
}
/**
 * 
 * @return boolean
 */
public boolean isHasChanged() {
	return hasChanged;
}
/**
 * 
 * @return boolean
 */
public boolean isValid() {
	return exceptions.size() == 0;
}
/**
 * 
 * @return boolean
 * @param pattern java.lang.String
 */
private boolean isValid(String pattern) {
	Vector v = new Vector();
	for (StringTokenizer tokenizer = new StringTokenizer(pattern, "{}", true);
			tokenizer.hasMoreTokens();) {
		String token = tokenizer.nextToken();
		if ("{".equals(token)) {
			String id = tokenizer.nextToken();
			if (!v.contains(id)) v.add(id);
			tokenizer.nextToken();
		}
	}
	if (parameters.size() != v.size()) return false;

	for (Enumeration e = v.elements(); e.hasMoreElements();) {
		if (!parameters.contains(e.nextElement()))
			return false;
	}
	return true;
}
/**
 * 
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * 
 * @param newExceptions java.util.Vector
 */
public void setExceptions(java.util.Vector newExceptions) {
	exceptions = newExceptions;
}
/**
 * 
 * @param newFilename java.lang.String
 */
public void setFilename(java.lang.String newFilename) {
	filename = newFilename;
}
/**
 * 
 * @param newFormat java.lang.String
 */
public void setFormat(String newFormat) throws PatternException {
	format = newFormat;
	try {
		compareFormat = Reformatter.format(format);
	} catch (Exception e) {
		throw new PatternException(e.getMessage());
	}
}
/**
 * 
 * @param newHasChanged boolean
 */
public void setHasChanged(boolean newHasChanged) {
	hasChanged = newHasChanged;
}
/**
 * 
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * 
 * @param newParameters java.util.Vector
 */
public void setParameters(java.util.Vector newParameters) {
	parameters = new TreeSet(newParameters);
}
/**
 * 
 * @param newScope java.lang.String
 */
public void setScope(java.lang.String newScope) {
	scope = newScope;
}
/**
 * 
 * @param newURL java.lang.String
 */
public void setURL(java.lang.String newURL) {
	url = newURL;
}
/**
 * 
 */
public void validate() {
	if (name == null) exceptions.add("Bad name");
	if (scope == null) exceptions.add("Bad scope");
	if (format == null) exceptions.add("Bad format");
	if (!isValid(format)) exceptions.add("Bad parameter(s) in format");
	for (Enumeration e = mappings.keys(); e.hasMoreElements();) {
		String key = (String) e.nextElement();
		if(!isValid((String) mappings.get(key))) exceptions.add("Bad parameter(s) in " + key + " mapping");
	}
}
}
