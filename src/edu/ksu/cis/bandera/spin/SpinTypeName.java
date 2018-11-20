package edu.ksu.cis.bandera.spin;

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

/**
 * Type namer for SPIN translator.
 * <p>
 * Generates PROMELA types from BIR types.
 * <p>
 * Note: the Object parameter to the typeswitch case methods indicates
 * the context.  If null, the variable is top-level, so we want to generate
 * the PROMELA type (e.g., bit, int, short, typedef {...} ). If non-null,
 * the variable is nested inside of a record, array, or collection,
 * so we want to simply return the defined name of the type (since 
 * PROMELA insists that struct field types be identifiers).
 */

public class SpinTypeName extends AbstractTypeSwitch {

	public void caseArray(Array type, Object o)
	{	
		if (o == null)
			setResult(type.typeName());
		else {
			type.getBaseType().apply(this,null);
			//      setResult("{ byte length; " + (String) getResult() + 
			//		" element[" + type.getSize() + "] }");
			/* [Thomas, May 8, 2017]
			 * 
			 * */
			if(type.typeName().equals("STSwitch_arr") || type.typeName().equals("STBulb_arr"))
			{
				setResult("{ byte length; " + (String) getResult() + 
						" element[MAX_SWITCH_DEVICES]; }");
			}
			else if(SpinUtil.isAMapArr(type.typeName()))
			{
				setResult("{ byte length; " + (String) getResult() + 
						" element[MAX_INT_ARRAY_SIZE]; }");
			}
//			else if(type.typeName().equals("STState_arr") || type.typeName().equals("STEvent_arr"))
//			{
//				setResult("{ byte length; " + (String) getResult() + 
//						" element[MAX_STORED_EVENTS]; }");
//			}
			else
			{
				setResult("{ byte length; " + (String) getResult() + 
						" element[MAX_ARRAY_SIZE]; }");
			}
		}	    
	}
	public void caseBool(Bool type, Object o) 
	{
		if (o == null)
			setResult(type.typeName());
		else 
			setResult("bool");
	}
	public void caseTid(Tid type, Object o)
	{
		if (o == null)
			setResult(type.typeName());
		else
			setResult("byte");
	}
	public void caseCollection(Collection type, Object o)
	{
		if (o == null)
			setResult(type.typeName());
		else {
			Type baseType = type.getBaseType();
			baseType.apply(this,null);
			setResult("{ bit inuse[" + type.getSize() + "]; " +
					(String) getResult() + 
					" instance[" + type.getSize() + "]; }");
		}	    
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
				setResult("int"); /* [Thomas, June 21, 2017] int -> short */
		}
	}
	public void caseRecord(Record type, Object o)
	{
		if (o == null)
			setResult(type.typeName());
		else {
			String result = "";
			Vector fields = type.getFields();
			for (int i = 0; i < fields.size(); i++) {
				/* [Thomas, June 20, 2017]
				 * We dont need to print out extra attributes
				 * such as: type_72 STCommand_fanAuto;
				 * */
				if(!((Field)fields.elementAt(i)).getName().startsWith("STCommand_"))
				{
					((Field)fields.elementAt(i)).apply(this,null);
					result += "\n\t" + (String) getResult();
				}
			}
			// PROMELA doesn't allow empty structures (BIR does)
			//      if (fields.size() == 0)
			//    	  	result = "byte dummy;";
			/* [Thomas, May 3rd, 2017]
			 * Add a field to every record: chan ToDeviceChannel = [MAX_CHAN_SIZE] of {STEvent_rec};
			 * */
			result = result + "\n\tbool isAlive;";
			
			if(SpinUtil.isADevice(type.typeName()))
			{
				if(type.typeName().equals("STLocation_rec"))
				{
//					result = result + "\n\tmtype LatestCommandType;";
//					result = result + "\n\tbyte LatestCommandID;";
				}
				else
				{
					result = result + "\n\tbyte currEvtIndex;";
					result = result + "\n\tbyte BroadcastChanIndex;";
				}
				result = result + "\n\tbyte NumReceivedCommands;";
//				result = result + "\n\tchan ToDeviceChannel = [MAX_CHAN_SIZE] of {STEvent_rec};";
//				result = result + "\n\tchan FromDeviceChannel = [MAX_CHAN_SIZE] of {STEvent_rec};";
				result = result + "\n\tbyte NumSubscribers;";
//				result = result + "\n\tchan BroadcastChans[MAX_SUBSCRIBERS] = [MAX_CHAN_SIZE] of {STEvent_rec};";
				result = result + "\n\tbool BroadcastChans[MAX_SUBSCRIBERS];";
				result = result + "\n\tbool isOnline;";
			}
			else if(type.typeName().equals("STEvent_rec"))
			{
				result = result + "\n\tmtype EvtType;";
			}
			setResult("{ " + result + "\n}");
		}
	}
	public void caseRef(Ref type, Object o)
	{
		if (o == null)
		{
			//setResult(type.typeName());
			setResult(type.getTargetType().getName());
		} 
		else
		{
			setResult("short");
		} 
	}
	public void defaultCase(Object obj) {
		throw new RuntimeException("Construct not handled: " + obj);
	}
}
