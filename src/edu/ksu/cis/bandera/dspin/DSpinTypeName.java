package edu.ksu.cis.bandera.dspin;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import edu.ksu.cis.bandera.bir.Collection;


import java.io.*;
import java.util.*;

import edu.ksu.cis.bandera.bir.*;

public class DSpinTypeName extends AbstractTypeSwitch  implements BirConstants 
{

	public void caseArray(Array type, Object o)
	{	
	if (o == null)
	    setResult(type.typeName());
	else {
	    type.getBaseType().apply(this,null);
	    String elementType = (String) getResult();
	    if (type.getBaseType().isKind(REF)) 
		elementType = elementType.substring(0,elementType.length() - 4)
		    + "_aref";
	    setResult("{ byte refIndex; byte length; " 
		      + elementType +"& elements; }");
	}	    
	}
	public void caseBool(Bool type, Object o) 
	{
	if (o == null)
	    setResult(type.typeName());
	else 
	    setResult("bool");
	}
	public void caseCollection(Collection type, Object o)
	{
	setResult("");
	}
	public void caseEnumerated(Enumerated type, Object o) 
	{
	if (o == null)
	    setResult(type.typeName());
	else {
	    if (type.getEnumeratedSize() < 256 && type.getFirstElement() >= 0)
		setResult("byte");
	    else
		setResult("int");
	}
	}
	public void caseField(Field type, Object o)
	{
	type.getType().apply(this,null);
	setResult( (String) getResult() + " " + type.getName() + "; ");
	}
	public void caseLock(Lock type, Object o)
	{
	if (o == null)
	    setResult(type.typeName());
	else {
	    String r = (type.isReentrant()) ? "R" : "";
	    String w = (type.isWaiting()) ? "W" : "";
	    setResult("lock_" + r + w);
	}
	}
	public void caseRange(Range type, Object o) 
	{
	if (o == null)
	    setResult(type.typeName());
	else {
	    if (type.getRangeSize() < 256 && type.getFirstElement() >= 0)
		setResult("byte");
	    else
		setResult("int");
	}
	}
	public void caseRecord(Record type, Object o)
	{
	if (o == null)
	    setResult(type.typeName());
	else {
	    String result = "byte refIndex; ";
	    Vector fields = type.getFields();
	    for (int i = 0; i < fields.size(); i++) {
		((Field)fields.elementAt(i)).apply(this,null);
		result += (String) getResult();
	    }
	    setResult("{ " + result + "}");
	}
	}
	public void caseRef(Ref type, Object o)
	{
	if (o == null)
	    setResult(type.typeName());
	else {
	    type.getTargetType().apply(this,null);
	    String targetTypeName = (String) getResult();
	    if (targetTypeName == null)
		targetTypeName = "object";
	    setResult(targetTypeName + "&");
	}
	}
	public void defaultCase(Object obj) {
	throw new RuntimeException("Construct not handled: " + obj);
	}
}
