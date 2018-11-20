package edu.ksu.cis.bandera.jjjc.analysis;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import java.util.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.exception.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.util.*;

public class ClassOrInterfaceAnalysis extends DepthFirstAdapter {
	private SymbolTable symbolTable;
	private Vector exceptions = new Vector();
	private ClassOrInterfaceType currentClassOrInterfaceType = null;
/**
 * 
 * @param symbolTable edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
 */
public ClassOrInterfaceAnalysis(SymbolTable symbolTable) {
	this.symbolTable = symbolTable;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclaration
 */
public void caseAClassDeclaration(AClassDeclaration node) {
	try {
		currentClassOrInterfaceType = symbolTable.getDeclaredType(new Name(node.getId().toString()));

		if (currentClassOrInterfaceType.isLoaded()) return;
		
		symbolTable.setCurrentClassOrInterfaceType(currentClassOrInterfaceType);
		
		Name className = currentClassOrInterfaceType.getName();


		if (node.getModifier() != null) {
			currentClassOrInterfaceType.setModifiers(Util.convertModifiers(node.getModifier().toString()));
		}

		Name objectName = new Name("Object");
		// should be
		// Name objectName = new Name("java.lang.Object");
		// a hack so that we can replace java.lang.Object with abstracted Object
		
		 
		Name superClassName = null;
		ClassOrInterfaceType superClassType = null;
		
		if ((node.getSuper() == null) || (((ASuper)node.getSuper()).getClassType() == null)) {
			if (!className.equals(objectName) && !className.equals("java.lang.Object")) {
				superClassName = objectName;
				superClassType = symbolTable.resolveClassOrInterfaceType(superClassName);
			}
		} else {
			superClassName = new Name((((ASuper)node.getSuper()).getClassType().toString()));
			if (superClassName.equals(className))
				throw new ClassCircularityException("Class named '" + className + "' extends itself");
			superClassType = symbolTable.resolveClassOrInterfaceType(superClassName);
		}

		currentClassOrInterfaceType.setSuperClass(superClassType);
		
		if (node.getInterfaces() != null && ((AInterfaces)node.getInterfaces()).getName() != null) {
			Object[] superInterfaces = ((AInterfaces)node.getInterfaces()).getName().toArray();
				for(int i = 0; i < superInterfaces.length; i++)	{
					ClassOrInterfaceType superInterface =
							symbolTable.resolveClassOrInterfaceType(new Name(superInterfaces[i].toString()));
					currentClassOrInterfaceType.addSuperInterface(superInterface);
				}
			}
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	symbolTable.setCurrentClassOrInterfaceType(null);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceDeclaration
 */
public void caseAInterfaceDeclaration(AInterfaceDeclaration node) {
	try {
		currentClassOrInterfaceType = symbolTable.getDeclaredType(new Name(node.getId().toString()));
		if (currentClassOrInterfaceType.isLoaded()) return;
		symbolTable.setCurrentClassOrInterfaceType(currentClassOrInterfaceType);

		if (node.getModifier() != null) {
			currentClassOrInterfaceType.setModifiers(Util.convertModifiers(node.getModifier().toString()));
		}

		if (node.getName() != null)	{
			Object[] superInterfaces = node.getName().toArray();
			
			for(int i = 0; i < superInterfaces.length; i++)	{
				ClassOrInterfaceType superInterface =
						symbolTable.resolveClassOrInterfaceType(new Name(superInterfaces[i].toString()));
				currentClassOrInterfaceType.addSuperInterface(superInterface);
			}
		}
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	
	symbolTable.setCurrentClassOrInterfaceType(null);
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getExceptions() {
	return exceptions.elements();
}
}
