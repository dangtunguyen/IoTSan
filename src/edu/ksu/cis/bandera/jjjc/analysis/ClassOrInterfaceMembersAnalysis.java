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
import java.lang.reflect.Modifier;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.exception.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.util.*;

public class ClassOrInterfaceMembersAnalysis extends DepthFirstAdapter {
	private SymbolTable symbolTable;
	private Vector exceptions = new Vector();
	private ClassOrInterfaceType currentClassOrInterfaceType = null;
	private ClassOrInterfaceType declaringClassOrInterfaceType = null;
	private Method currentMethod = null;
	private Type currentType = null;
/**
 * 
 * @param symbolTable edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
 */
public ClassOrInterfaceMembersAnalysis(SymbolTable symbolTable) {
	this.symbolTable = symbolTable;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABlockClassBodyDeclaration
 */
public void caseABlockClassBodyDeclaration(ABlockClassBodyDeclaration node) {
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassClassBodyDeclaration
 */
public void caseAClassClassBodyDeclaration(AClassClassBodyDeclaration node) {
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

		if (currentClassOrInterfaceType.isLoaded()) return;

		if (node.getClassBody() != null)	{
			node.getClassBody().apply(this);
		}

		if (!currentClassOrInterfaceType.getConstructors().hasMoreElements()) {
			Method constructor = new Method(new Name(currentClassOrInterfaceType.getName().getLastIdentifier()));
			currentClassOrInterfaceType.addConstructor(constructor);
			constructor.setModifiers(Modifier.PUBLIC);
		}
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	symbolTable.setCurrentClassOrInterfaceType(null);
	currentClassOrInterfaceType = null;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclarationBlockedStmt
 */
public void caseAClassDeclarationBlockedStmt(AClassDeclarationBlockedStmt node) {
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclarationInterfaceMemberDeclaration
 */
public void caseAClassDeclarationInterfaceMemberDeclaration(AClassDeclarationInterfaceMemberDeclaration node) {
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AConstructorDeclaration
 */
public void caseAConstructorDeclaration(AConstructorDeclaration node) {
	try {
		int modifiers = Util.convertModifiers(node.getModifier().toString());
		
		if (node.getConstructorDeclarator() != null) {
			node.getConstructorDeclarator().apply(this);
		}
		
		if (node.getThrows() != null) {
			node.getThrows().apply(this);
		}
		
		currentClassOrInterfaceType.addConstructor(currentMethod);
		currentMethod.setModifiers(modifiers);
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	currentMethod = null;
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AConstructorDeclarator
 */
public void caseAConstructorDeclarator(AConstructorDeclarator node) {
	try {
		Name name = new Name(node.getSimpleName().toString());
		
		if (!name.equals(new Name(currentClassOrInterfaceType.getName().getLastIdentifier())))
			throw new AnalysisException("Constructor declaration '" + name + "' should have name '"
					+ currentClassOrInterfaceType.getName().getLastIdentifier() + "'");
			
		currentMethod = new Method(name);
		
		if (node.getFormalParameter() != null) {
			Object[] formalParameters = node.getFormalParameter().toArray();
			
			for (int i = 0; i < formalParameters.length; i++) {
				((PFormalParameter) formalParameters[i]).apply(this);
			}
		}
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AFieldDeclaration
 */
public void caseAFieldDeclaration(AFieldDeclaration node) {
	try {
		int modifiers = 0;
		if (node.getModifier() != null)
			modifiers = Util.convertModifiers(node.getModifier().toString());
		
		Type type = symbolTable.resolveType(new Name(node.getType().toString()));

		int dims = Util.countArrayDimensions(node.getType().toString());
		
		Object[] variableDeclarators = node.getVariableDeclarator().toArray();
		
		for (int i = 0; i < variableDeclarators.length; i++) {
			AVariableDeclaratorId variableDeclaratorId;
			
			if (variableDeclarators[i] instanceof AIdVariableDeclarator)
				variableDeclaratorId = (AVariableDeclaratorId)
						((AIdVariableDeclarator) variableDeclarators[i]).getVariableDeclaratorId();
			else 
				variableDeclaratorId = (AVariableDeclaratorId)
						((AAssignedVariableDeclarator) variableDeclarators[i]).getVariableDeclaratorId();

			int dimensions = variableDeclaratorId.getDim().size() + dims;
			Type fieldType;
			if (dimensions > 0)
				fieldType = new ArrayType(type, dimensions);
			else
				fieldType = type;
			
			Field f = new Field(new Name(variableDeclaratorId.getId().toString()), fieldType);
			currentClassOrInterfaceType.addField(f);
			f.setModifiers(modifiers);
		}
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AFormalParameter
 */
public void caseAFormalParameter(AFormalParameter node) {
	try {
		int modifiers = 0;
		
		if (node.getModifier() != null)
			 modifiers = Util.convertModifiers(node.getModifier().toString());

		Type type = symbolTable.resolveType(new Name(node.getType().toString()));
			 
		AVariableDeclaratorId variableDeclaratorId = (AVariableDeclaratorId) node.getVariableDeclaratorId();

		int dimensions = variableDeclaratorId.getDim().size() + Util.countArrayDimensions(node.getType().toString());

		if (dimensions > 0)
			type = new ArrayType(type, dimensions);

		Name name = new Name(variableDeclaratorId.getId().toString());	
		
		currentMethod.addParameterVariable(new Variable(modifiers, type, name));
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
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

		if (currentClassOrInterfaceType.isLoaded()) return;

	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	
	if(node.getInterfaceBody() != null)	{
		node.getInterfaceBody().apply(this);
	}
	
	symbolTable.setCurrentClassOrInterfaceType(null);
	currentClassOrInterfaceType = null;
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceDeclarationClassMemberDeclaration
 */
public void caseAInterfaceDeclarationClassMemberDeclaration(AInterfaceDeclarationClassMemberDeclaration node) {
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInterfaceDeclarationInterfaceMemberDeclaration
 */
public void caseAInterfaceDeclarationInterfaceMemberDeclaration(AInterfaceDeclarationInterfaceMemberDeclaration node) {
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AMethodDeclaration
 */
public void caseAMethodDeclaration(AMethodDeclaration node) {
	node.getMethodHeader().apply(this);
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AMethodDeclarator
 */
public void caseAMethodDeclarator(AMethodDeclarator node) {
	try {
		if (node.getDim() != null) {
			int dimensions = node.getDim().size();
			
			if (dimensions > 0) {
				if (currentType instanceof VoidType)
					throw new TypeException("Cannot have a type of array of void as return type for method");
					
				if (currentType instanceof ArrayType)
					currentType = new ArrayType(((ArrayType) currentType).baseType,
							((ArrayType) currentType).nDimensions + dimensions);
				else
					currentType = new ArrayType(currentType, dimensions);
			}
		}
		
		currentMethod = new Method(new Name(node.getId().toString()));
		currentMethod.setReturnType(currentType);
		currentType = null;
		
		if (node.getFormalParameter() != null) {
			Object[] formalParameters = node.getFormalParameter().toArray();
			for (int i = 0; i < formalParameters.length; i++) {
				((PFormalParameter) formalParameters[i]).apply(this);
			}
		}
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AStaticInitializerClassBodyDeclaration
 */
public void caseAStaticInitializerClassBodyDeclaration(AStaticInitializerClassBodyDeclaration node) {
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AThrows
 */
public void caseAThrows(AThrows node) {
	try {
		Object[] exceptionTypes = node.getName().toArray();
		for (int i = 0; i < exceptionTypes.length; i++) {
			ClassOrInterfaceType exceptionType = symbolTable.resolveClassOrInterfaceType(new Name(exceptionTypes[i].toString()));
			currentMethod.addException(exceptionType);
		}
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.ATypedMethodHeader
 */
public void caseATypedMethodHeader(ATypedMethodHeader node) {
	try {
		int modifiers = 0;
		if (node.getModifier() != null) {
			modifiers = Util.convertModifiers(node.getModifier().toString());
		}

		String type = node.getType().toString();
		currentType = symbolTable.resolveType(new Name(type));
		int dimensions = Util.countArrayDimensions(type);
		
		if (dimensions > 0)
			currentType = new ArrayType(currentType, dimensions);
		
		if (node.getMethodDeclarator() != null) {
			node.getMethodDeclarator().apply(this);
		}
		
		if (node.getThrows() != null) {
			node.getThrows().apply(this);
		}
		
		currentClassOrInterfaceType.addMethod(currentMethod);
		currentMethod.setModifiers(modifiers);
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	
	currentMethod = null;
}
/**
 * 
 * @param node ca.mcgill.sable.laleh.java.node.AVoidMethodHeader
 */
public void caseAVoidMethodHeader(AVoidMethodHeader node) {
	try {
		int modifiers = 0;
		if (node.getModifier() != null) {
			modifiers = Util.convertModifiers(node.getModifier().toString());
		}

		currentType = VoidType.TYPE;
		
		if (node.getMethodDeclarator() != null) {
			node.getMethodDeclarator().apply(this);
		}
		
		if (node.getThrows() != null) {
			node.getThrows().apply(this);
		}
		
		currentClassOrInterfaceType.addMethod(currentMethod);
		currentMethod.setModifiers(modifiers);
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
	
	currentMethod = null;
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getExceptions() {
	return exceptions.elements();
}
}
