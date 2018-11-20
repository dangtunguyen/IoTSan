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
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;

/**
 * DecompilerGotoMapper is a helper class to map the bad gotos.
 * Basically a dead code (at the moment). Deprecated
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
 * @deprecated
**/
public class DecompilerGotoMapper {
	private static Hashtable mapper = new Hashtable();
	private static Hashtable reverse = new Hashtable();
	private static int counter = 0;

/**
 * Return the statement which the id points to. If not found, returns null.
**/
public static Stmt getStmt(String id)
{
	return (Stmt) mapper.get(id);
}
public static String mapStmt(Stmt s)
{
	// If it is already exist, then, retrieve the ID.
	if (mapper.containsValue(s))
	{
		return (String) reverse.get(s);
	}

	// Otherwise, create a new ID.
	String id = "L"+String.valueOf(obtainID());

	// Then put the mapper (ID -> Statement);
	mapper.put(id, s);

	// After that, the reverse mapper (Statement -> ID)
	reverse.put(s,id);
	
	return id;
}
private synchronized static int obtainID()
{
	int temp = counter;
	counter++;
	return temp;
}
public synchronized static void reset()
{
	mapper = new Hashtable();
	reverse = new Hashtable();
	counter = 0;
}
}
