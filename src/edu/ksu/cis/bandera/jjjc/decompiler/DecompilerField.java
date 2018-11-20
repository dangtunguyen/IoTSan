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
 * DecompilerField basically holds the information of a field.
 * The methods and fields are self-explanatory.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class DecompilerField {
	private String name, type, defaultValue, modif;

/**
 * SlabsField constructor takes field name, type, and modifier.
 */
public DecompilerField(String fname, String ftype, String modi) {
	name = fname.trim();
	type = ftype.trim();
	modif = modi.trim();
	defaultValue = "";
	if (modi==null) modif = "";
}
/**
 * 
 * @return Field's default value in String
 */
public String getDefaultValue() {
	return defaultValue;
}
/**
 * 
 * @return The modifier in String
 */
public String getModifier() {
	return modif;
}
/**
 * 
 * @return Field's name
 */
public String getName() {
	return name;
}
/**
 * 
 * @return Field's type in String
 */
public String getType() {
	return type;
}
/**
 * 
 * @param Setting the field's default value in String
 */
public void setDefaultValue(String newDefaultValue) {
	defaultValue = newDefaultValue;
}
/**
 * 
 * @param Setting new modifier
 */
public void setModifier(String newModi) {
	modif=newModi;
}
/**
 * 
 * @param Setting field's name in String
 */
public void setName(String newName) {
	name = newName;
}
/**
 * 
 * @param Setting field's type in String
 */
public void setType(String newType) {
	type = newType;
}
public String toString()
{
	StringBuffer s = new StringBuffer(modif + " " + type + " " + name);
	if ((defaultValue != null) && (!defaultValue.equals("")))
		s.append(" = " + defaultValue);
	s.append(";");
	return s.toString();
}
}
