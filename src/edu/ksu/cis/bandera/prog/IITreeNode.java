package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.*;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import edu.ksu.cis.bandera.jjjc.*;

class IITreeNode
{
	private SootClass element;
	private int depth;
	private int maxNodes; //used as a max array size
	private IITreeNode[] children;   
	private int childrenCount; //can be used as index where to insert next

	public IITreeNode(SootClass sc, int num)  
	{
		depth = 0;
		element = sc;
	maxNodes = num;
		children = new IITreeNode[maxNodes];
	childrenCount = 0;
	}
	public void addChild(SootClass sc)
	{
	IITreeNode child = new IITreeNode(sc, maxNodes);
	children[childrenCount] = child;
	childrenCount++;
	}
	public void addChild(IITreeNode child)
	{
		children[childrenCount] = child;
		childrenCount++;
	}
	public IITreeNode[] getChildren()
	{
	return children;
	}
	public int getChildrenCount()
	{
	return childrenCount;
	}
	public int getDepth()
	{
	return depth;
	}
	public SootClass getElement()
	{
	return element;
	}
	public int number(int d)
	{
		int tmp;
		int maxd = d;

		depth = d; 
		for (int i=0; i<childrenCount; i++) {
		  tmp = children[i].number(d+1);
		  if (tmp > maxd) maxd = tmp;
		}
		return maxd;
	}
	public void print()
	{ 
	System.out.println();
	System.out.println("Children of "+this.element.getName()+":");
	for(int i=0; i<childrenCount; i++)
	{
	    System.out.print(((children[i]).element).getName()+"\t");
	    
	}
	System.out.println();
		for(int i=0; i<childrenCount; i++)
	    (children[i]).print();
	}
}
